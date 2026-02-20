package com.rescueplanner.sim;

public class WorkerResponse {
  private SimulationResult result;

  public WorkerResponse() {
  }

  public WorkerResponse(SimulationResult result) {
    this.result = result;
  }

  public SimulationResult getResult() {
    return result;
  }

  public void setResult(SimulationResult result) {
    this.result = result;
  }
}
