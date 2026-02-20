package com.rescueplanner.sim;

import java.util.ArrayList;
import java.util.List;

public class SimulationResult {
  private int runs;
  private double successProbability;
  private double abortProbability;
  private double expectedTimeToContact;
  private double medianTimeToContact;
  private double p90TimeToContact;
  private List<SensitivityResult> sensitivities = new ArrayList<>();

  public SimulationResult() {
  }

  public int getRuns() {
    return runs;
  }

  public void setRuns(int runs) {
    this.runs = runs;
  }

  public double getSuccessProbability() {
    return successProbability;
  }

  public void setSuccessProbability(double successProbability) {
    this.successProbability = successProbability;
  }

  public double getAbortProbability() {
    return abortProbability;
  }

  public void setAbortProbability(double abortProbability) {
    this.abortProbability = abortProbability;
  }

  public double getExpectedTimeToContact() {
    return expectedTimeToContact;
  }

  public void setExpectedTimeToContact(double expectedTimeToContact) {
    this.expectedTimeToContact = expectedTimeToContact;
  }

  public double getMedianTimeToContact() {
    return medianTimeToContact;
  }

  public void setMedianTimeToContact(double medianTimeToContact) {
    this.medianTimeToContact = medianTimeToContact;
  }

  public double getP90TimeToContact() {
    return p90TimeToContact;
  }

  public void setP90TimeToContact(double p90TimeToContact) {
    this.p90TimeToContact = p90TimeToContact;
  }

  public List<SensitivityResult> getSensitivities() {
    return sensitivities;
  }

  public void setSensitivities(List<SensitivityResult> sensitivities) {
    this.sensitivities = sensitivities;
  }

  public String toSummary() {
    StringBuilder sb = new StringBuilder();
    sb.append("Runs: ").append(runs).append("\n");
    sb.append(String.format("Success Probability: %.2f%%\n", successProbability * 100));
    sb.append(String.format("Abort Probability: %.2f%%\n", abortProbability * 100));
    sb.append(String.format("Expected Time-to-Contact: %.2f hrs\n", expectedTimeToContact));
    sb.append(String.format("Median Time-to-Contact: %.2f hrs\n", medianTimeToContact));
    sb.append(String.format("P90 Time-to-Contact: %.2f hrs\n", p90TimeToContact));
    sb.append("Top Sensitivities:\n");
    for (SensitivityResult result : sensitivities) {
      sb.append("- ").append(result.getVariable())
        .append(" (effect: ")
        .append(String.format("%.3f", result.getEffect()))
        .append(")\n");
    }
    return sb.toString();
  }
}
