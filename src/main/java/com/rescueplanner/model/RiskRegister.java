package com.rescueplanner.model;

import java.util.ArrayList;
import java.util.List;

public class RiskRegister {
  private List<RiskItem> risks = new ArrayList<>();
  private List<Assumption> assumptions = new ArrayList<>();
  private String draft;

  public RiskRegister() {
  }

  public List<RiskItem> getRisks() {
    return risks;
  }

  public void setRisks(List<RiskItem> risks) {
    this.risks = risks;
  }

  public List<Assumption> getAssumptions() {
    return assumptions;
  }

  public void setAssumptions(List<Assumption> assumptions) {
    this.assumptions = assumptions;
  }

  public String getDraft() {
    return draft;
  }

  public void setDraft(String draft) {
    this.draft = draft;
  }
}
