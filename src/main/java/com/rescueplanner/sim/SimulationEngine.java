package com.rescueplanner.sim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SimulationEngine {
  public SimulationResult runSimulation(SimulationInputs inputs, SimulationConfig config) {
    int runs = config.normalizedRuns();
    Random random = new Random(config.getSeed());

    int successCount = 0;
    int abortCount = 0;

    List<Double> successTimes = new ArrayList<>();

    double weatherSuccess = 0.0;
    double weatherFail = 0.0;
    double hazardSuccess = 0.0;
    double hazardFail = 0.0;
    double commsSuccess = 0.0;
    double commsFail = 0.0;
    double accessSuccess = 0.0;
    double accessFail = 0.0;

    int successTotal = 0;
    int failTotal = 0;

    for (int i = 0; i < runs; i++) {
      double weather = clamp(normal(random, config.getWeatherMean(), 0.15));
      double hazardMean = (inputs.getHazardBaseline() + config.getHazardMean()) / 2.0;
      double commsMean = (inputs.getCommsBaseline() + config.getCommsMean()) / 2.0;
      double hazard = clamp(normal(random, hazardMean, 0.2));
      double comms = clamp(normal(random, commsMean, 0.15));
      double accessDelay = Math.max(0.0,
        normal(random, inputs.getAccessDelayBaseline() + config.getAccessDelayMean(), config.getAccessDelayStd()));
      double survivability = Math.max(0.5,
        normal(random, config.getSurvivabilityMeanHours(), config.getSurvivabilityStdHours()));

      boolean abort = weather >= config.getWeatherAbortThreshold()
        || hazard >= config.getHazardAbortThreshold()
        || comms <= config.getCommsAbortThreshold();

      double baseTime = inputs.getBaseSearchTimeHours();
      double timeToContact = baseTime * (1.0 + weather * 0.45 + hazard * 0.35) + accessDelay;
      timeToContact *= (1.0 + (1.0 - comms) * 0.2);

      if (abort) {
        abortCount++;
        failTotal++;
        weatherFail += weather;
        hazardFail += hazard;
        commsFail += comms;
        accessFail += accessDelay;
        continue;
      }

      if (timeToContact <= survivability) {
        successCount++;
        successTimes.add(timeToContact);
        successTotal++;
        weatherSuccess += weather;
        hazardSuccess += hazard;
        commsSuccess += comms;
        accessSuccess += accessDelay;
      } else {
        failTotal++;
        weatherFail += weather;
        hazardFail += hazard;
        commsFail += comms;
        accessFail += accessDelay;
      }
    }

    SimulationResult result = new SimulationResult();
    result.setRuns(runs);
    result.setSuccessProbability((double) successCount / runs);
    result.setAbortProbability((double) abortCount / runs);

    double expected = successTimes.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    result.setExpectedTimeToContact(expected);
    result.setMedianTimeToContact(percentile(successTimes, 0.5));
    result.setP90TimeToContact(percentile(successTimes, 0.9));

    List<SensitivityResult> sensitivities = new ArrayList<>();
    sensitivities.add(new SensitivityResult("Weather severity", sensitivityEffect(weatherSuccess, weatherFail, successTotal, failTotal)));
    sensitivities.add(new SensitivityResult("Hazard fluctuation", sensitivityEffect(hazardSuccess, hazardFail, successTotal, failTotal)));
    sensitivities.add(new SensitivityResult("Comms uptime", sensitivityEffect(commsSuccess, commsFail, successTotal, failTotal)));
    sensitivities.add(new SensitivityResult("Access delay", sensitivityEffect(accessSuccess, accessFail, successTotal, failTotal)));

    sensitivities.sort(Comparator.comparingDouble(SensitivityResult::getEffect).reversed());
    if (sensitivities.size() > 3) {
      sensitivities = sensitivities.subList(0, 3);
    }
    result.setSensitivities(sensitivities);

    return result;
  }

  private double percentile(List<Double> values, double pct) {
    if (values.isEmpty()) {
      return 0.0;
    }
    List<Double> sorted = new ArrayList<>(values);
    Collections.sort(sorted);
    int index = (int) Math.min(sorted.size() - 1, Math.round(pct * (sorted.size() - 1)));
    return sorted.get(index);
  }

  private double sensitivityEffect(double successSum, double failSum, int successCount, int failCount) {
    double successMean = successCount == 0 ? 0.0 : successSum / successCount;
    double failMean = failCount == 0 ? 0.0 : failSum / failCount;
    return Math.abs(failMean - successMean);
  }

  private double normal(Random random, double mean, double std) {
    return mean + random.nextGaussian() * std;
  }

  private double clamp(double value) {
    if (value < 0.0) {
      return 0.0;
    }
    if (value > 1.0) {
      return 1.0;
    }
    return value;
  }
}
