package com.rescueplanner.model;

public enum Role {
  TEAM_LEAD("Team Lead (Incident Commander)"),
  SAFETY_OFFICER("Safety Officer (Avalanche Hazard Lead)"),
  OPS_COORDINATOR("Ops Coordinator"),
  INTEL_INFO_OFFICER("Intel/Info Officer"),
  COMMS_TRACKING("Comms & Tracking"),
  MEDICAL_LEAD("Medical Lead"),
  MOBILITY_LOGISTICS("Mobility/Logistics"),
  LIAISON("Liaison"),
  TECHNICAL_SPECIALIST("Technical Specialist"),
  RECORDER_ASSESSMENT("Recorder/Assessment");

  private final String label;

  Role(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public static Role fromLabel(String label) {
    for (Role role : values()) {
      if (role.label.equalsIgnoreCase(label) || role.name().equalsIgnoreCase(label)) {
        return role;
      }
    }
    return TEAM_LEAD;
  }
}
