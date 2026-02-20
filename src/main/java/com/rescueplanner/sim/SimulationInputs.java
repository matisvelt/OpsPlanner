package com.rescueplanner.sim;

import com.rescueplanner.model.COA;
import com.rescueplanner.model.EnvironmentModel;
import com.rescueplanner.model.Sector;
import com.rescueplanner.model.Scenario;

import java.util.List;

public class SimulationInputs {
  private double baseSearchTimeHours;
  private double hazardBaseline;
  private double commsBaseline;
  private double accessDelayBaseline;

  public SimulationInputs() {
  }

  public double getBaseSearchTimeHours() {
    return baseSearchTimeHours;
  }

  public void setBaseSearchTimeHours(double baseSearchTimeHours) {
    this.baseSearchTimeHours = baseSearchTimeHours;
  }

  public double getHazardBaseline() {
    return hazardBaseline;
  }

  public void setHazardBaseline(double hazardBaseline) {
    this.hazardBaseline = hazardBaseline;
  }

  public double getCommsBaseline() {
    return commsBaseline;
  }

  public void setCommsBaseline(double commsBaseline) {
    this.commsBaseline = commsBaseline;
  }

  public double getAccessDelayBaseline() {
    return accessDelayBaseline;
  }

  public void setAccessDelayBaseline(double accessDelayBaseline) {
    this.accessDelayBaseline = accessDelayBaseline;
  }

  public static SimulationInputs fromScenario(Scenario scenario, COA coa) {
    SimulationInputs inputs = new SimulationInputs();
    EnvironmentModel env = scenario.getEnvironmentModel();
    List<Sector> sectors = env.getSectors();
    if (sectors.isEmpty()) {
      inputs.setBaseSearchTimeHours(4.0);
      inputs.setHazardBaseline(0.6);
      inputs.setCommsBaseline(0.6);
      inputs.setAccessDelayBaseline(0.5);
      return inputs;
    }

    double weightedTime = 0.0;
    double hazard = 0.0;
    double comms = 0.0;
    double totalWeight = 0.0;

    for (Sector sector : sectors) {
      double weight = sector.getProbabilityWeight();
      totalWeight += weight;
      double avgTime = (sector.getTimeToSearchMinHours() + sector.getTimeToSearchMaxHours()) / 2.0;
      weightedTime += avgTime * weight;
      hazard += sector.getAvalancheHazardIndex() * weight;
      comms += sector.getCommsReliability() * weight;
    }

    if (totalWeight <= 0.0) {
      totalWeight = 1.0;
    }

    inputs.setBaseSearchTimeHours(weightedTime / totalWeight);
    inputs.setHazardBaseline((hazard / totalWeight) / 5.0);
    inputs.setCommsBaseline(comms / totalWeight);

    double accessDelay = 0.6;
    if (coa != null && coa.getName() != null && coa.getName().toLowerCase().contains("safety-first")) {
      accessDelay = 0.8;
    }
    inputs.setAccessDelayBaseline(accessDelay);

    return inputs;
  }
}
