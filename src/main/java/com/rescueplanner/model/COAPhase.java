package com.rescueplanner.model;

public class COAPhase {
  private String name;
  private String description;
  private String timing;
  private String responsibilities;

  public COAPhase() {
  }

  public COAPhase(String name, String description, String timing, String responsibilities) {
    this.name = name;
    this.description = description;
    this.timing = timing;
    this.responsibilities = responsibilities;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTiming() {
    return timing;
  }

  public void setTiming(String timing) {
    this.timing = timing;
  }

  public String getResponsibilities() {
    return responsibilities;
  }

  public void setResponsibilities(String responsibilities) {
    this.responsibilities = responsibilities;
  }
}
