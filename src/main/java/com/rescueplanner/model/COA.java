package com.rescueplanner.model;

import java.util.ArrayList;
import java.util.List;

public class COA {
  private String id;
  private String name;
  private String objective;
  private List<COAPhase> phases = new ArrayList<>();
  private List<ResourceAssignment> resourceAssignments = new ArrayList<>();
  private List<SafetyGate> safetyGates = new ArrayList<>();
  private List<String> keyAssumptions = new ArrayList<>();
  private List<String> risks = new ArrayList<>();
  private List<String> infoNeeds = new ArrayList<>();
  private List<String> assessmentMeasures = new ArrayList<>();

  public COA() {
  }

  public COA(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getObjective() {
    return objective;
  }

  public void setObjective(String objective) {
    this.objective = objective;
  }

  public List<COAPhase> getPhases() {
    return phases;
  }

  public void setPhases(List<COAPhase> phases) {
    this.phases = phases;
  }

  public List<ResourceAssignment> getResourceAssignments() {
    return resourceAssignments;
  }

  public void setResourceAssignments(List<ResourceAssignment> resourceAssignments) {
    this.resourceAssignments = resourceAssignments;
  }

  public List<SafetyGate> getSafetyGates() {
    return safetyGates;
  }

  public void setSafetyGates(List<SafetyGate> safetyGates) {
    this.safetyGates = safetyGates;
  }

  public List<String> getKeyAssumptions() {
    return keyAssumptions;
  }

  public void setKeyAssumptions(List<String> keyAssumptions) {
    this.keyAssumptions = keyAssumptions;
  }

  public List<String> getRisks() {
    return risks;
  }

  public void setRisks(List<String> risks) {
    this.risks = risks;
  }

  public List<String> getInfoNeeds() {
    return infoNeeds;
  }

  public void setInfoNeeds(List<String> infoNeeds) {
    this.infoNeeds = infoNeeds;
  }

  public List<String> getAssessmentMeasures() {
    return assessmentMeasures;
  }

  public void setAssessmentMeasures(List<String> assessmentMeasures) {
    this.assessmentMeasures = assessmentMeasures;
  }
}
