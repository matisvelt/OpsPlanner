package com.rescueplanner.model;

public class InfoRequirement {
  private String requirement;
  private String indicator;
  private String priority;
  private String status;

  public InfoRequirement() {
  }

  public InfoRequirement(String requirement, String indicator, String priority, String status) {
    this.requirement = requirement;
    this.indicator = indicator;
    this.priority = priority;
    this.status = status;
  }

  public String getRequirement() {
    return requirement;
  }

  public void setRequirement(String requirement) {
    this.requirement = requirement;
  }

  public String getIndicator() {
    return indicator;
  }

  public void setIndicator(String indicator) {
    this.indicator = indicator;
  }

  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
