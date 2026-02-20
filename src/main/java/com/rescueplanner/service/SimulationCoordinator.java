package com.rescueplanner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rescueplanner.model.COA;
import com.rescueplanner.model.Scenario;
import com.rescueplanner.sim.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SimulationCoordinator {
  private final SimulationEngine engine = new SimulationEngine();
  private final ObjectMapper mapper = new ObjectMapper();

  public SimulationResult runSimulation(Scenario scenario, COA coa, SimulationConfig config, List<String> workerUrls) throws Exception {
    SimulationInputs inputs = SimulationInputs.fromScenario(scenario, coa);
    if (workerUrls == null || workerUrls.isEmpty()) {
      return engine.runSimulation(inputs, config);
    }

    int totalRuns = config.normalizedRuns();
    int workers = workerUrls.size();
    int base = totalRuns / workers;
    int remainder = totalRuns % workers;

    HttpClient client = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(5))
      .build();

    List<SimulationResult> partials = new ArrayList<>();
    int assigned = 0;

    for (int i = 0; i < workers; i++) {
      int runs = base + (i < remainder ? 1 : 0);
      if (runs <= 0) {
        continue;
      }
      assigned += runs;
      WorkerRequest request = new WorkerRequest();
      SimulationConfig workerConfig = new SimulationConfig();
      workerConfig.setRuns(runs);
      workerConfig.setSeed(config.getSeed() + i * 31L);
      workerConfig.setWeatherMean(config.getWeatherMean());
      workerConfig.setHazardMean(config.getHazardMean());
      workerConfig.setCommsMean(config.getCommsMean());
      workerConfig.setAccessDelayMean(config.getAccessDelayMean());
      workerConfig.setAccessDelayStd(config.getAccessDelayStd());
      workerConfig.setSurvivabilityMeanHours(config.getSurvivabilityMeanHours());
      workerConfig.setSurvivabilityStdHours(config.getSurvivabilityStdHours());
      workerConfig.setWeatherAbortThreshold(config.getWeatherAbortThreshold());
      workerConfig.setHazardAbortThreshold(config.getHazardAbortThreshold());
      workerConfig.setCommsAbortThreshold(config.getCommsAbortThreshold());

      request.setRuns(runs);
      request.setSeed(workerConfig.getSeed());
      request.setConfig(workerConfig);
      request.setInputs(inputs);

      byte[] payload = mapper.writeValueAsBytes(request);
      String url = workerUrls.get(i);
      HttpRequest httpRequest = HttpRequest.newBuilder()
        .uri(URI.create(url + "/simulate"))
        .timeout(Duration.ofSeconds(30))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofByteArray(payload))
        .build();

      HttpResponse<byte[]> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
      if (response.statusCode() != 200) {
        throw new IllegalStateException("Worker returned status " + response.statusCode());
      }
      WorkerResponse workerResponse = mapper.readValue(response.body(), WorkerResponse.class);
      partials.add(workerResponse.getResult());
    }

    return aggregateResults(partials, assigned);
  }

  private SimulationResult aggregateResults(List<SimulationResult> partials, int totalRuns) {
    SimulationResult result = new SimulationResult();
    result.setRuns(totalRuns);

    double successWeighted = 0.0;
    double abortWeighted = 0.0;
    double expectedWeighted = 0.0;
    double medianWeighted = 0.0;
    double p90Weighted = 0.0;

    List<SensitivityResult> sensitivityAggregates = new ArrayList<>();

    int counted = 0;
    for (SimulationResult partial : partials) {
      int runs = partial.getRuns();
      counted += runs;
      successWeighted += partial.getSuccessProbability() * runs;
      abortWeighted += partial.getAbortProbability() * runs;
      expectedWeighted += partial.getExpectedTimeToContact() * runs;
      medianWeighted += partial.getMedianTimeToContact() * runs;
      p90Weighted += partial.getP90TimeToContact() * runs;

      for (SensitivityResult s : partial.getSensitivities()) {
        sensitivityAggregates.add(new SensitivityResult(s.getVariable(), s.getEffect() * runs));
      }
    }

    if (counted == 0) {
      return result;
    }

    result.setSuccessProbability(successWeighted / counted);
    result.setAbortProbability(abortWeighted / counted);
    result.setExpectedTimeToContact(expectedWeighted / counted);
    result.setMedianTimeToContact(medianWeighted / counted);
    result.setP90TimeToContact(p90Weighted / counted);

    sensitivityAggregates.sort((a, b) -> Double.compare(b.getEffect(), a.getEffect()));
    if (sensitivityAggregates.size() > 3) {
      sensitivityAggregates = sensitivityAggregates.subList(0, 3);
    }
    for (SensitivityResult s : sensitivityAggregates) {
      s.setEffect(s.getEffect() / counted);
    }
    result.setSensitivities(sensitivityAggregates);

    return result;
  }
}
