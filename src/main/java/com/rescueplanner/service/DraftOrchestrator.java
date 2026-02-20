package com.rescueplanner.service;

import com.rescueplanner.model.Scenario;
import com.rescueplanner.model.TraceabilityRecord;

public class DraftOrchestrator {
  public void generateAll(Scenario scenario) {
    apply("1", DraftGenerator.generateIncidentDraft(scenario), scenario);
    apply("2", DraftGenerator.generateConstraintsDraft(scenario), scenario);
    apply("3", DraftGenerator.generateInfoRequirementsDraft(scenario), scenario);
    apply("4", DraftGenerator.generateEnvironmentDraft(scenario), scenario);
    apply("5", DraftGenerator.generateRiskDraft(scenario), scenario);
    apply("6", DraftGenerator.generateCoaDraft(scenario), scenario);
    apply("7", DraftGenerator.generateRehearsalDraft(scenario), scenario);
    apply("8", DraftGenerator.generateDecisionGatesDraft(scenario), scenario);
    apply("9", DraftGenerator.generateTeamDraft(scenario), scenario);
    apply("10", DraftGenerator.generateMissionDraft(scenario), scenario);
    apply("11", DraftGenerator.generateAssessmentDraft(scenario), scenario);
  }

  private void apply(String stepId, DraftResult result, Scenario scenario) {
    if (result == null) {
      return;
    }
    TraceabilityRecord record = result.getTraceability();
    if (record != null) {
      scenario.getTraceability().put(stepId, record);
    }

    switch (stepId) {
      case "1" -> scenario.getIncidentInput().setDraft(result.getDraft());
      case "2" -> scenario.getConstraintsAuthorities().setDraft(result.getDraft());
      case "3" -> scenario.getInformationRequirements().setDraft(result.getDraft());
      case "4" -> scenario.getEnvironmentModel().setDraft(result.getDraft());
      case "5" -> scenario.getRiskRegister().setDraft(result.getDraft());
      case "6" -> scenario.getCoaSet().setDraft(result.getDraft());
      case "7" -> scenario.getRehearsalPlan().setDraft(result.getDraft());
      case "8" -> scenario.getDecisionGates().setDraft(result.getDraft());
      case "9" -> scenario.getTeamResponsibilities().setDraft(result.getDraft());
      case "10" -> scenario.getMissionConops().setDraft(result.getDraft());
      case "11" -> scenario.getAssessmentFeedback().setDraft(result.getDraft());
      default -> { }
    }
  }
}
