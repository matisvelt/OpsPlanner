package com.rescueplanner.model;

public class ResourceAssignment {
  private String role;
  private String allocation;
  private String notes;

  public ResourceAssignment() {
  }

  public ResourceAssignment(String role, String allocation, String notes) {
    this.role = role;
    this.allocation = allocation;
    this.notes = notes;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getAllocation() {
    return allocation;
  }

  public void setAllocation(String allocation) {
    this.allocation = allocation;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }
}
