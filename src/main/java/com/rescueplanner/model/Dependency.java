package com.rescueplanner.model;

public class Dependency {
  private String prerequisite;
  private String dependent;

  public Dependency() {
  }

  public Dependency(String prerequisite, String dependent) {
    this.prerequisite = prerequisite;
    this.dependent = dependent;
  }

  public String getPrerequisite() {
    return prerequisite;
  }

  public void setPrerequisite(String prerequisite) {
    this.prerequisite = prerequisite;
  }

  public String getDependent() {
    return dependent;
  }

  public void setDependent(String dependent) {
    this.dependent = dependent;
  }
}
