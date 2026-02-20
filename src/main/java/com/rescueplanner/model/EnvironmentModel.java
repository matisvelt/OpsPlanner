package com.rescueplanner.model;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentModel {
  private List<Sector> sectors = new ArrayList<>();
  private List<Dependency> dependencies = new ArrayList<>();
  private String uncertaintyMap;
  private String draft;

  public EnvironmentModel() {
  }

  public List<Sector> getSectors() {
    return sectors;
  }

  public void setSectors(List<Sector> sectors) {
    this.sectors = sectors;
  }

  public List<Dependency> getDependencies() {
    return dependencies;
  }

  public void setDependencies(List<Dependency> dependencies) {
    this.dependencies = dependencies;
  }

  public String getUncertaintyMap() {
    return uncertaintyMap;
  }

  public void setUncertaintyMap(String uncertaintyMap) {
    this.uncertaintyMap = uncertaintyMap;
  }

  public String getDraft() {
    return draft;
  }

  public void setDraft(String draft) {
    this.draft = draft;
  }
}
