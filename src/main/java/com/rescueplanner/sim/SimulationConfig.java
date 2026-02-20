package com.rescueplanner.sim;

public class SimulationConfig {
  private int runs = 5000;
  private long seed = System.currentTimeMillis();
  private double weatherMean = 0.55;
  private double hazardMean = 0.6;
  private double commsMean = 0.65;
  private double accessDelayMean = 0.6;
  private double accessDelayStd = 0.3;
  private double survivabilityMeanHours = 6.0;
  private double survivabilityStdHours = 1.5;
  private double weatherAbortThreshold = 0.85;
  private double hazardAbortThreshold = 0.8;
  private double commsAbortThreshold = 0.4;

  public SimulationConfig() {
  }

  public int getRuns() {
    return runs;
  }

  public void setRuns(int runs) {
    this.runs = runs;
  }

  public long getSeed() {
    return seed;
  }

  public void setSeed(long seed) {
    this.seed = seed;
  }

  public double getWeatherMean() {
    return weatherMean;
  }

  public void setWeatherMean(double weatherMean) {
    this.weatherMean = weatherMean;
  }

  public double getHazardMean() {
    return hazardMean;
  }

  public void setHazardMean(double hazardMean) {
    this.hazardMean = hazardMean;
  }

  public double getCommsMean() {
    return commsMean;
  }

  public void setCommsMean(double commsMean) {
    this.commsMean = commsMean;
  }

  public double getAccessDelayMean() {
    return accessDelayMean;
  }

  public void setAccessDelayMean(double accessDelayMean) {
    this.accessDelayMean = accessDelayMean;
  }

  public double getAccessDelayStd() {
    return accessDelayStd;
  }

  public void setAccessDelayStd(double accessDelayStd) {
    this.accessDelayStd = accessDelayStd;
  }

  public double getSurvivabilityMeanHours() {
    return survivabilityMeanHours;
  }

  public void setSurvivabilityMeanHours(double survivabilityMeanHours) {
    this.survivabilityMeanHours = survivabilityMeanHours;
  }

  public double getSurvivabilityStdHours() {
    return survivabilityStdHours;
  }

  public void setSurvivabilityStdHours(double survivabilityStdHours) {
    this.survivabilityStdHours = survivabilityStdHours;
  }

  public double getWeatherAbortThreshold() {
    return weatherAbortThreshold;
  }

  public void setWeatherAbortThreshold(double weatherAbortThreshold) {
    this.weatherAbortThreshold = weatherAbortThreshold;
  }

  public double getHazardAbortThreshold() {
    return hazardAbortThreshold;
  }

  public void setHazardAbortThreshold(double hazardAbortThreshold) {
    this.hazardAbortThreshold = hazardAbortThreshold;
  }

  public double getCommsAbortThreshold() {
    return commsAbortThreshold;
  }

  public void setCommsAbortThreshold(double commsAbortThreshold) {
    this.commsAbortThreshold = commsAbortThreshold;
  }

  public int normalizedRuns() {
    if (runs < 5000) {
      return 5000;
    }
    if (runs > 100000) {
      return 100000;
    }
    return runs;
  }
}
