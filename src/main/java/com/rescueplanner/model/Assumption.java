package com.rescueplanner.model;

public class Assumption {
  private String statement;
  private ConfidenceLevel confidence;
  private FragilityTag fragility;

  public Assumption() {
  }

  public Assumption(String statement, ConfidenceLevel confidence, FragilityTag fragility) {
    this.statement = statement;
    this.confidence = confidence;
    this.fragility = fragility;
  }

  public String getStatement() {
    return statement;
  }

  public void setStatement(String statement) {
    this.statement = statement;
  }

  public ConfidenceLevel getConfidence() {
    return confidence;
  }

  public void setConfidence(ConfidenceLevel confidence) {
    this.confidence = confidence;
  }

  public FragilityTag getFragility() {
    return fragility;
  }

  public void setFragility(FragilityTag fragility) {
    this.fragility = fragility;
  }
}
