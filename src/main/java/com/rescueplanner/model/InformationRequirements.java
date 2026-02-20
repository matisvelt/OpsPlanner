package com.rescueplanner.model;

import java.util.ArrayList;
import java.util.List;

public class InformationRequirements {
  private List<InfoRequirement> requirements = new ArrayList<>();
  private String derivedNeeds;
  private String draft;

  public InformationRequirements() {
  }

  public List<InfoRequirement> getRequirements() {
    return requirements;
  }

  public void setRequirements(List<InfoRequirement> requirements) {
    this.requirements = requirements;
  }

  public String getDerivedNeeds() {
    return derivedNeeds;
  }

  public void setDerivedNeeds(String derivedNeeds) {
    this.derivedNeeds = derivedNeeds;
  }

  public String getDraft() {
    return draft;
  }

  public void setDraft(String draft) {
    this.draft = draft;
  }
}
