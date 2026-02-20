package com.rescueplanner.model;

import java.util.ArrayList;
import java.util.List;

public class TraceabilityRecord {
  private String stepId;
  private List<String> inputsUsed = new ArrayList<>();
  private List<String> rulesApplied = new ArrayList<>();
  private List<String> outputsCreated = new ArrayList<>();

  public TraceabilityRecord() {
  }

  public TraceabilityRecord(String stepId) {
    this.stepId = stepId;
  }

  public String getStepId() {
    return stepId;
  }

  public void setStepId(String stepId) {
    this.stepId = stepId;
  }

  public List<String> getInputsUsed() {
    return inputsUsed;
  }

  public void setInputsUsed(List<String> inputsUsed) {
    this.inputsUsed = inputsUsed;
  }

  public List<String> getRulesApplied() {
    return rulesApplied;
  }

  public void setRulesApplied(List<String> rulesApplied) {
    this.rulesApplied = rulesApplied;
  }

  public List<String> getOutputsCreated() {
    return outputsCreated;
  }

  public void setOutputsCreated(List<String> outputsCreated) {
    this.outputsCreated = outputsCreated;
  }
}
