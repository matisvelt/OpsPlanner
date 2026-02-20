package com.rescueplanner.model;

public class IncidentInput {
  private String title;
  private String context;
  private String lastKnownArea;
  private String timeLastSeen;
  private String daylightWindow;
  private String weatherSummary;
  private String commsSummary;
  private String medicalRiskSummary;
  private String draft;

  public IncidentInput() {
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public String getLastKnownArea() {
    return lastKnownArea;
  }

  public void setLastKnownArea(String lastKnownArea) {
    this.lastKnownArea = lastKnownArea;
  }

  public String getTimeLastSeen() {
    return timeLastSeen;
  }

  public void setTimeLastSeen(String timeLastSeen) {
    this.timeLastSeen = timeLastSeen;
  }

  public String getDaylightWindow() {
    return daylightWindow;
  }

  public void setDaylightWindow(String daylightWindow) {
    this.daylightWindow = daylightWindow;
  }

  public String getWeatherSummary() {
    return weatherSummary;
  }

  public void setWeatherSummary(String weatherSummary) {
    this.weatherSummary = weatherSummary;
  }

  public String getCommsSummary() {
    return commsSummary;
  }

  public void setCommsSummary(String commsSummary) {
    this.commsSummary = commsSummary;
  }

  public String getMedicalRiskSummary() {
    return medicalRiskSummary;
  }

  public void setMedicalRiskSummary(String medicalRiskSummary) {
    this.medicalRiskSummary = medicalRiskSummary;
  }

  public String getDraft() {
    return draft;
  }

  public void setDraft(String draft) {
    this.draft = draft;
  }
}
