package com.rescueplanner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rescueplanner.model.Scenario;

import java.io.IOException;
import java.nio.file.Path;

public class ScenarioRepository {
  private final ObjectMapper mapper;

  public ScenarioRepository() {
    this.mapper = new ObjectMapper();
    this.mapper.registerModule(new JavaTimeModule());
    this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  public void save(Path path, Scenario scenario) throws IOException {
    scenario.touch();
    mapper.writeValue(path.toFile(), scenario);
  }

  public Scenario load(Path path) throws IOException {
    return mapper.readValue(path.toFile(), Scenario.class);
  }
}
