package com.rescueplanner.model;

public class RehearsalEvent {
  private String id;
  private String eventName;
  private String indicator;
  private String triggerThreshold;
  private DecisionAction decision;
  private String responsibleRole;
  private double timeImpactHours;
  private int riskImpact;
  private String safetyGateInvoked;

  public RehearsalEvent() {
  }

  public RehearsalEvent(String id, String eventName) {
    this.id = id;
    this.eventName = eventName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public String getIndicator() {
    return indicator;
  }

  public void setIndicator(String indicator) {
    this.indicator = indicator;
  }

  public String getTriggerThreshold() {
    return triggerThreshold;
  }

  public void setTriggerThreshold(String triggerThreshold) {
    this.triggerThreshold = triggerThreshold;
  }

  public DecisionAction getDecision() {
    return decision;
  }

  public void setDecision(DecisionAction decision) {
    this.decision = decision;
  }

  public String getResponsibleRole() {
    return responsibleRole;
  }

  public void setResponsibleRole(String responsibleRole) {
    this.responsibleRole = responsibleRole;
  }

  public double getTimeImpactHours() {
    return timeImpactHours;
  }

  public void setTimeImpactHours(double timeImpactHours) {
    this.timeImpactHours = timeImpactHours;
  }

  public int getRiskImpact() {
    return riskImpact;
  }

  public void setRiskImpact(int riskImpact) {
    this.riskImpact = riskImpact;
  }

  public String getSafetyGateInvoked() {
    return safetyGateInvoked;
  }

  public void setSafetyGateInvoked(String safetyGateInvoked) {
    this.safetyGateInvoked = safetyGateInvoked;
  }
}
