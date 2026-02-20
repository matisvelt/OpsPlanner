package com.rescueplanner.persistence;

import com.rescueplanner.model.Scenario;
import com.rescueplanner.service.ScenarioFactory;
import com.rescueplanner.service.ScenarioRepository;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ScenarioRepositoryTest {
  @Test
  void saveAndLoadScenario() throws Exception {
    Scenario scenario = ScenarioFactory.prefilledTrainingScenario();
    ScenarioRepository repository = new ScenarioRepository();

    Path temp = Files.createTempFile("scenario", ".json");
    repository.save(temp, scenario);

    Scenario loaded = repository.load(temp);
    assertEquals(scenario.getTitle(), loaded.getTitle());
    assertNotNull(loaded.getEnvironmentModel());
    assertFalse(loaded.getEnvironmentModel().getSectors().isEmpty());
  }
}
