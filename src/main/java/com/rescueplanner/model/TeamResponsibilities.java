package com.rescueplanner.model;

import java.util.ArrayList;
import java.util.List;

public class TeamResponsibilities {
  private List<RaciEntry> raci = new ArrayList<>();
  private String notes;
  private String draft;

  public TeamResponsibilities() {
  }

  public List<RaciEntry> getRaci() {
    return raci;
  }

  public void setRaci(List<RaciEntry> raci) {
    this.raci = raci;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getDraft() {
    return draft;
  }

  public void setDraft(String draft) {
    this.draft = draft;
  }
}
