package com.rescueplanner.model;

public class RaciEntry {
  private String task;
  private String role;
  private ResponsibilityType responsibility;
  private String details;

  public RaciEntry() {
  }

  public RaciEntry(String task, String role, ResponsibilityType responsibility, String details) {
    this.task = task;
    this.role = role;
    this.responsibility = responsibility;
    this.details = details;
  }

  public String getTask() {
    return task;
  }

  public void setTask(String task) {
    this.task = task;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public ResponsibilityType getResponsibility() {
    return responsibility;
  }

  public void setResponsibility(ResponsibilityType responsibility) {
    this.responsibility = responsibility;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }
}
