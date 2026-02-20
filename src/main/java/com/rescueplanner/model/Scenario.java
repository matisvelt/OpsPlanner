package com.rescueplanner.model;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Scenario {
  private String id;
  private String title;
  private String classificationBanner;
  private Instant createdAt;
  private Instant updatedAt;
  private IncidentInput incidentInput;
  private ConstraintsAuthorities constraintsAuthorities;
  private InformationRequirements informationRequirements;
  private EnvironmentModel environmentModel;
  private RiskRegister riskRegister;
  private COASet coaSet;
  private RehearsalPlan rehearsalPlan;
  private DecisionGates decisionGates;
  private TeamResponsibilities teamResponsibilities;
  private MissionConops missionConops;
  private AssessmentFeedback assessmentFeedback;
  private Map<String, TraceabilityRecord> traceability = new HashMap<>();

  public Scenario() {
    this.id = UUID.randomUUID().toString();
    this.classificationBanner = "TRAINING / UNCLASSIFIED";
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
    this.incidentInput = new IncidentInput();
    this.constraintsAuthorities = new ConstraintsAuthorities();
    this.informationRequirements = new InformationRequirements();
    this.environmentModel = new EnvironmentModel();
    this.riskRegister = new RiskRegister();
    this.coaSet = new COASet();
    this.rehearsalPlan = new RehearsalPlan();
    this.decisionGates = new DecisionGates();
    this.teamResponsibilities = new TeamResponsibilities();
    this.missionConops = new MissionConops();
    this.assessmentFeedback = new AssessmentFeedback();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getClassificationBanner() {
    return classificationBanner;
  }

  public void setClassificationBanner(String classificationBanner) {
    this.classificationBanner = classificationBanner;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public IncidentInput getIncidentInput() {
    return incidentInput;
  }

  public void setIncidentInput(IncidentInput incidentInput) {
    this.incidentInput = incidentInput;
  }

  public ConstraintsAuthorities getConstraintsAuthorities() {
    return constraintsAuthorities;
  }

  public void setConstraintsAuthorities(ConstraintsAuthorities constraintsAuthorities) {
    this.constraintsAuthorities = constraintsAuthorities;
  }

  public InformationRequirements getInformationRequirements() {
    return informationRequirements;
  }

  public void setInformationRequirements(InformationRequirements informationRequirements) {
    this.informationRequirements = informationRequirements;
  }

  public EnvironmentModel getEnvironmentModel() {
    return environmentModel;
  }

  public void setEnvironmentModel(EnvironmentModel environmentModel) {
    this.environmentModel = environmentModel;
  }

  public RiskRegister getRiskRegister() {
    return riskRegister;
  }

  public void setRiskRegister(RiskRegister riskRegister) {
    this.riskRegister = riskRegister;
  }

  public COASet getCoaSet() {
    return coaSet;
  }

  public void setCoaSet(COASet coaSet) {
    this.coaSet = coaSet;
  }

  public RehearsalPlan getRehearsalPlan() {
    return rehearsalPlan;
  }

  public void setRehearsalPlan(RehearsalPlan rehearsalPlan) {
    this.rehearsalPlan = rehearsalPlan;
  }

  public DecisionGates getDecisionGates() {
    return decisionGates;
  }

  public void setDecisionGates(DecisionGates decisionGates) {
    this.decisionGates = decisionGates;
  }

  public TeamResponsibilities getTeamResponsibilities() {
    return teamResponsibilities;
  }

  public void setTeamResponsibilities(TeamResponsibilities teamResponsibilities) {
    this.teamResponsibilities = teamResponsibilities;
  }

  public MissionConops getMissionConops() {
    return missionConops;
  }

  public void setMissionConops(MissionConops missionConops) {
    this.missionConops = missionConops;
  }

  public AssessmentFeedback getAssessmentFeedback() {
    return assessmentFeedback;
  }

  public void setAssessmentFeedback(AssessmentFeedback assessmentFeedback) {
    this.assessmentFeedback = assessmentFeedback;
  }

  public Map<String, TraceabilityRecord> getTraceability() {
    return traceability;
  }

  public void setTraceability(Map<String, TraceabilityRecord> traceability) {
    this.traceability = traceability;
  }

  public void touch() {
    this.updatedAt = Instant.now();
  }
}
