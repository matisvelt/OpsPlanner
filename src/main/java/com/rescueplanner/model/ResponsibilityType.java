package com.rescueplanner.model;

public enum ResponsibilityType {
  R("Responsible"),
  A("Accountable"),
  C("Consulted"),
  I("Informed");

  private final String label;

  ResponsibilityType(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
