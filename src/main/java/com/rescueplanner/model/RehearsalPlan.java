package com.rescueplanner.model;

import java.util.ArrayList;
import java.util.List;

public class RehearsalPlan {
  private List<RehearsalEvent> events = new ArrayList<>();
  private List<DecisionLogEntry> decisionLog = new ArrayList<>();
  private String draft;

  public RehearsalPlan() {
  }

  public List<RehearsalEvent> getEvents() {
    return events;
  }

  public void setEvents(List<RehearsalEvent> events) {
    this.events = events;
  }

  public List<DecisionLogEntry> getDecisionLog() {
    return decisionLog;
  }

  public void setDecisionLog(List<DecisionLogEntry> decisionLog) {
    this.decisionLog = decisionLog;
  }

  public String getDraft() {
    return draft;
  }

  public void setDraft(String draft) {
    this.draft = draft;
  }
}
