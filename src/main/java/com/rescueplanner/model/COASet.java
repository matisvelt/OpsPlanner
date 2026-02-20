package com.rescueplanner.model;

import java.util.ArrayList;
import java.util.List;

public class COASet {
  private List<COA> coas = new ArrayList<>();
  private String draft;

  public COASet() {
  }

  public List<COA> getCoas() {
    return coas;
  }

  public void setCoas(List<COA> coas) {
    this.coas = coas;
  }

  public String getDraft() {
    return draft;
  }

  public void setDraft(String draft) {
    this.draft = draft;
  }
}
