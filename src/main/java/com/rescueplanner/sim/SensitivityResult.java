package com.rescueplanner.sim;

public class SensitivityResult {
  private String variable;
  private double effect;

  public SensitivityResult() {
  }

  public SensitivityResult(String variable, double effect) {
    this.variable = variable;
    this.effect = effect;
  }

  public String getVariable() {
    return variable;
  }

  public void setVariable(String variable) {
    this.variable = variable;
  }

  public double getEffect() {
    return effect;
  }

  public void setEffect(double effect) {
    this.effect = effect;
  }
}
