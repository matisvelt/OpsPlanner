package com.rescueplanner.model;

public class Sector {
  private String name;
  private String size;
  private int accessDifficultyIndex;
  private int avalancheHazardIndex;
  private int visibilityIndex;
  private double commsReliability;
  private double timeToSearchMinHours;
  private double timeToSearchMaxHours;
  private double probabilityWeight;

  public Sector() {
  }

  public Sector(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public int getAccessDifficultyIndex() {
    return accessDifficultyIndex;
  }

  public void setAccessDifficultyIndex(int accessDifficultyIndex) {
    this.accessDifficultyIndex = accessDifficultyIndex;
  }

  public int getAvalancheHazardIndex() {
    return avalancheHazardIndex;
  }

  public void setAvalancheHazardIndex(int avalancheHazardIndex) {
    this.avalancheHazardIndex = avalancheHazardIndex;
  }

  public int getVisibilityIndex() {
    return visibilityIndex;
  }

  public void setVisibilityIndex(int visibilityIndex) {
    this.visibilityIndex = visibilityIndex;
  }

  public double getCommsReliability() {
    return commsReliability;
  }

  public void setCommsReliability(double commsReliability) {
    this.commsReliability = commsReliability;
  }

  public double getTimeToSearchMinHours() {
    return timeToSearchMinHours;
  }

  public void setTimeToSearchMinHours(double timeToSearchMinHours) {
    this.timeToSearchMinHours = timeToSearchMinHours;
  }

  public double getTimeToSearchMaxHours() {
    return timeToSearchMaxHours;
  }

  public void setTimeToSearchMaxHours(double timeToSearchMaxHours) {
    this.timeToSearchMaxHours = timeToSearchMaxHours;
  }

  public double getProbabilityWeight() {
    return probabilityWeight;
  }

  public void setProbabilityWeight(double probabilityWeight) {
    this.probabilityWeight = probabilityWeight;
  }
}
