package com.rescueplanner.model;

public class SafetyGate {
  private String name;
  private String threshold;
  private String action;
  private String ownerRole;

  public SafetyGate() {
  }

  public SafetyGate(String name, String threshold, String action, String ownerRole) {
    this.name = name;
    this.threshold = threshold;
    this.action = action;
    this.ownerRole = ownerRole;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getThreshold() {
    return threshold;
  }

  public void setThreshold(String threshold) {
    this.threshold = threshold;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getOwnerRole() {
    return ownerRole;
  }

  public void setOwnerRole(String ownerRole) {
    this.ownerRole = ownerRole;
  }
}
