package com.rescueplanner.service;

import com.rescueplanner.model.TraceabilityRecord;

public class DraftResult {
  private final String draft;
  private final TraceabilityRecord traceability;

  public DraftResult(String draft, TraceabilityRecord traceability) {
    this.draft = draft;
    this.traceability = traceability;
  }

  public String getDraft() {
    return draft;
  }

  public TraceabilityRecord getTraceability() {
    return traceability;
  }
}
