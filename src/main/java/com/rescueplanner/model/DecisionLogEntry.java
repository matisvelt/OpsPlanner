package com.rescueplanner.model;

import java.time.Instant;

public class DecisionLogEntry {
  private Instant timestamp;
  private String eventId;
  private DecisionAction decision;
  private String rationale;

  public DecisionLogEntry() {
  }

  public DecisionLogEntry(Instant timestamp, String eventId, DecisionAction decision, String rationale) {
    this.timestamp = timestamp;
    this.eventId = eventId;
    this.decision = decision;
    this.rationale = rationale;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }

  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public DecisionAction getDecision() {
    return decision;
  }

  public void setDecision(DecisionAction decision) {
    this.decision = decision;
  }

  public String getRationale() {
    return rationale;
  }

  public void setRationale(String rationale) {
    this.rationale = rationale;
  }
}
