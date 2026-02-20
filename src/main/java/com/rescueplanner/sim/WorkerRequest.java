package com.rescueplanner.sim;

public class WorkerRequest {
  private long seed;
  private int runs;
  private SimulationConfig config;
  private SimulationInputs inputs;

  public WorkerRequest() {
  }

  public long getSeed() {
    return seed;
  }

  public void setSeed(long seed) {
    this.seed = seed;
  }

  public int getRuns() {
    return runs;
  }

  public void setRuns(int runs) {
    this.runs = runs;
  }

  public SimulationConfig getConfig() {
    return config;
  }

  public void setConfig(SimulationConfig config) {
    this.config = config;
  }

  public SimulationInputs getInputs() {
    return inputs;
  }

  public void setInputs(SimulationInputs inputs) {
    this.inputs = inputs;
  }
}
