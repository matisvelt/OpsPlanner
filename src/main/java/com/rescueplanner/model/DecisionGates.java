package com.rescueplanner.model;

import java.util.ArrayList;
import java.util.List;

public class DecisionGates {
  private List<SafetyGate> gates = new ArrayList<>();
  private String stopGoCriteria;
  private String draft;

  public DecisionGates() {
  }

  public List<SafetyGate> getGates() {
    return gates;
  }

  public void setGates(List<SafetyGate> gates) {
    this.gates = gates;
  }

  public String getStopGoCriteria() {
    return stopGoCriteria;
  }

  public void setStopGoCriteria(String stopGoCriteria) {
    this.stopGoCriteria = stopGoCriteria;
  }

  public String getDraft() {
    return draft;
  }

  public void setDraft(String draft) {
    this.draft = draft;
  }
}
