package com.rescueplanner.model;

public class ConstraintsAuthorities {
  private String legalAuthority;
  private String safetyPolicy;
  private String timeWindows;
  private String weatherAviationRestrictions;
  private String medicalConstraints;
  private String draft;

  public ConstraintsAuthorities() {
  }

  public String getLegalAuthority() {
    return legalAuthority;
  }

  public void setLegalAuthority(String legalAuthority) {
    this.legalAuthority = legalAuthority;
  }

  public String getSafetyPolicy() {
    return safetyPolicy;
  }

  public void setSafetyPolicy(String safetyPolicy) {
    this.safetyPolicy = safetyPolicy;
  }

  public String getTimeWindows() {
    return timeWindows;
  }

  public void setTimeWindows(String timeWindows) {
    this.timeWindows = timeWindows;
  }

  public String getWeatherAviationRestrictions() {
    return weatherAviationRestrictions;
  }

  public void setWeatherAviationRestrictions(String weatherAviationRestrictions) {
    this.weatherAviationRestrictions = weatherAviationRestrictions;
  }

  public String getMedicalConstraints() {
    return medicalConstraints;
  }

  public void setMedicalConstraints(String medicalConstraints) {
    this.medicalConstraints = medicalConstraints;
  }

  public String getDraft() {
    return draft;
  }

  public void setDraft(String draft) {
    this.draft = draft;
  }
}
