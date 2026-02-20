package com.rescueplanner.model;

public class RiskItem {
  private String id;
  private String description;
  private int likelihood;
  private int impact;
  private String mitigation;
  private String ownerRole;

  public RiskItem() {
  }

  public RiskItem(String id, String description, int likelihood, int impact, String mitigation, String ownerRole) {
    this.id = id;
    this.description = description;
    this.likelihood = likelihood;
    this.impact = impact;
    this.mitigation = mitigation;
    this.ownerRole = ownerRole;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getLikelihood() {
    return likelihood;
  }

  public void setLikelihood(int likelihood) {
    this.likelihood = likelihood;
  }

  public int getImpact() {
    return impact;
  }

  public void setImpact(int impact) {
    this.impact = impact;
  }

  public String getMitigation() {
    return mitigation;
  }

  public void setMitigation(String mitigation) {
    this.mitigation = mitigation;
  }

  public String getOwnerRole() {
    return ownerRole;
  }

  public void setOwnerRole(String ownerRole) {
    this.ownerRole = ownerRole;
  }
}
