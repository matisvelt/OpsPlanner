package com.rescueplanner.sim;

import com.rescueplanner.model.COA;
import com.rescueplanner.model.Scenario;
import com.rescueplanner.service.ScenarioFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimulationEngineTest {
  @Test
  void simulationProducesBoundedResults() {
    Scenario scenario = ScenarioFactory.prefilledTrainingScenario();
    COA coa = scenario.getCoaSet().getCoas().get(0);

    SimulationConfig config = new SimulationConfig();
    config.setRuns(5000);
    config.setSeed(42L);

    SimulationInputs inputs = SimulationInputs.fromScenario(scenario, coa);
    SimulationEngine engine = new SimulationEngine();
    SimulationResult result = engine.runSimulation(inputs, config);

    assertEquals(5000, result.getRuns());
    assertTrue(result.getSuccessProbability() >= 0 && result.getSuccessProbability() <= 1);
    assertTrue(result.getAbortProbability() >= 0 && result.getAbortProbability() <= 1);
    assertTrue(result.getExpectedTimeToContact() >= 0);
    assertTrue(result.getSensitivities().size() <= 3);
  }

  @Test
  void normalizedRunsRespectsBounds() {
    SimulationConfig config = new SimulationConfig();
    config.setRuns(10);
    assertEquals(5000, config.normalizedRuns());

    config.setRuns(200000);
    assertEquals(100000, config.normalizedRuns());
  }
}
