package com.rescueplanner.service;

import com.rescueplanner.model.*;

import java.util.List;
import java.util.stream.Collectors;

public class DraftGenerator {
  private DraftGenerator() {
  }

  public static DraftResult generateIncidentDraft(Scenario scenario) {
    IncidentInput input = scenario.getIncidentInput();
    StringBuilder sb = new StringBuilder();
    sb.append("Incident Summary (Training):\n");
    sb.append("Title: ").append(nullSafe(input.getTitle())).append("\n");
    sb.append("Context: ").append(nullSafe(input.getContext())).append("\n");
    sb.append("Last Known Area: ").append(nullSafe(input.getLastKnownArea())).append("\n");
    sb.append("Time Last Seen: ").append(nullSafe(input.getTimeLastSeen())).append("\n");
    sb.append("Daylight Window: ").append(nullSafe(input.getDaylightWindow())).append("\n");
    sb.append("Weather: ").append(nullSafe(input.getWeatherSummary())).append("\n");
    sb.append("Comms: ").append(nullSafe(input.getCommsSummary())).append("\n");
    sb.append("Medical Risk: ").append(nullSafe(input.getMedicalRiskSummary())).append("\n");
    sb.append("Note: This draft is for planning and governance only; it does not include procedural rescue actions.");

    TraceabilityRecord trace = new TraceabilityRecord("1");
    trace.getInputsUsed().add("Incident title, context, last known area, time last seen");
    trace.getInputsUsed().add("Weather, comms, medical risk summaries");
    trace.getRulesApplied().add("Deterministic summary template for training context");
    trace.getOutputsCreated().add("Incident Summary Draft");

    return new DraftResult(sb.toString(), trace);
  }

  public static DraftResult generateConstraintsDraft(Scenario scenario) {
    ConstraintsAuthorities ca = scenario.getConstraintsAuthorities();
    StringBuilder sb = new StringBuilder();
    sb.append("Constraints & Authorities Summary:\n");
    sb.append("Legal Authority: ").append(nullSafe(ca.getLegalAuthority())).append("\n");
    sb.append("Safety Policy: ").append(nullSafe(ca.getSafetyPolicy())).append("\n");
    sb.append("Time Windows: ").append(nullSafe(ca.getTimeWindows())).append("\n");
    sb.append("Weather/Aviation Restrictions: ").append(nullSafe(ca.getWeatherAviationRestrictions())).append("\n");
    sb.append("Medical Constraints: ").append(nullSafe(ca.getMedicalConstraints())).append("\n");
    sb.append("Planning Guidance: Comply with gates and stop/go thresholds before committing resources.");

    TraceabilityRecord trace = new TraceabilityRecord("2");
    trace.getInputsUsed().add("Legal authority, safety policy, time windows");
    trace.getInputsUsed().add("Weather/aviation restrictions, medical constraints");
    trace.getRulesApplied().add("Constraint compilation rule set (no procedural guidance)");
    trace.getOutputsCreated().add("Constraints Summary Draft");

    return new DraftResult(sb.toString(), trace);
  }

  public static DraftResult generateInfoRequirementsDraft(Scenario scenario) {
    InformationRequirements info = scenario.getInformationRequirements();
    StringBuilder sb = new StringBuilder();
    sb.append("Information Requirements & Indicators:\n");
    for (InfoRequirement req : info.getRequirements()) {
      sb.append("- ").append(nullSafe(req.getRequirement()))
        .append(" | Indicator: ").append(nullSafe(req.getIndicator()))
        .append(" | Priority: ").append(nullSafe(req.getPriority()))
        .append(" | Status: ").append(nullSafe(req.getStatus()))
        .append("\n");
    }
    sb.append("Derived Needs: ").append(nullSafe(info.getDerivedNeeds()));

    TraceabilityRecord trace = new TraceabilityRecord("3");
    trace.getInputsUsed().add("Information requirements list and indicator fields");
    trace.getRulesApplied().add("Structured requirement compilation rule");
    trace.getOutputsCreated().add("Info Requirements Draft");

    return new DraftResult(sb.toString(), trace);
  }

  public static DraftResult generateEnvironmentDraft(Scenario scenario) {
    EnvironmentModel env = scenario.getEnvironmentModel();
    StringBuilder sb = new StringBuilder();
    sb.append("Environment/System Model Summary:\n");
    for (Sector sector : env.getSectors()) {
      sb.append("- Sector ").append(nullSafe(sector.getName()))
        .append(" | Size: ").append(nullSafe(sector.getSize()))
        .append(" | Access: ").append(sector.getAccessDifficultyIndex())
        .append(" | Hazard: ").append(sector.getAvalancheHazardIndex())
        .append(" | Visibility: ").append(sector.getVisibilityIndex())
        .append(" | Comms: ").append(String.format("%.2f", sector.getCommsReliability()))
        .append(" | Time: ").append(String.format("%.1f-%.1f", sector.getTimeToSearchMinHours(), sector.getTimeToSearchMaxHours()))
        .append(" hrs | Weight: ").append(String.format("%.2f", sector.getProbabilityWeight()))
        .append("\n");
    }
    if (!env.getDependencies().isEmpty()) {
      sb.append("Dependencies:\n");
      for (Dependency dep : env.getDependencies()) {
        sb.append("- ").append(nullSafe(dep.getDependent())).append(" depends on ")
          .append(nullSafe(dep.getPrerequisite())).append("\n");
      }
    }
    sb.append("Uncertainty Map Notes: ").append(nullSafe(env.getUncertaintyMap()));

    TraceabilityRecord trace = new TraceabilityRecord("4");
    trace.getInputsUsed().add("Sector attributes, dependencies, uncertainty map");
    trace.getRulesApplied().add("Sector summary aggregation rule");
    trace.getOutputsCreated().add("Environment Summary Draft");

    return new DraftResult(sb.toString(), trace);
  }

  public static DraftResult generateRiskDraft(Scenario scenario) {
    RiskRegister register = scenario.getRiskRegister();
    StringBuilder sb = new StringBuilder();
    sb.append("Risk Register Summary:\n");
    for (RiskItem risk : register.getRisks()) {
      sb.append("- [").append(nullSafe(risk.getId())).append("] ")
        .append(nullSafe(risk.getDescription()))
        .append(" | L:").append(risk.getLikelihood())
        .append(" I:").append(risk.getImpact())
        .append(" | Mitigation: ").append(nullSafe(risk.getMitigation()))
        .append(" | Owner: ").append(nullSafe(risk.getOwnerRole()))
        .append("\n");
    }
    sb.append("Assumptions:\n");
    for (Assumption assumption : register.getAssumptions()) {
      sb.append("- ").append(nullSafe(assumption.getStatement()))
        .append(" (Confidence: ").append(assumption.getConfidence())
        .append(", Fragility: ").append(assumption.getFragility())
        .append(")\n");
    }

    TraceabilityRecord trace = new TraceabilityRecord("5");
    trace.getInputsUsed().add("Risk items and assumptions");
    trace.getRulesApplied().add("Risk register compilation rule");
    trace.getOutputsCreated().add("Risk Summary Draft");

    return new DraftResult(sb.toString(), trace);
  }

  public static DraftResult generateCoaDraft(Scenario scenario) {
    COASet coaSet = scenario.getCoaSet();
    StringBuilder sb = new StringBuilder();
    sb.append("COA Summary (Abstract, Non-Tactical):\n");
    for (COA coa : coaSet.getCoas()) {
      sb.append("\n").append(coa.getName()).append("\n");
      sb.append("Objective: ").append(nullSafe(coa.getObjective())).append("\n");
      sb.append("Phases:\n");
      for (COAPhase phase : coa.getPhases()) {
        sb.append("- ").append(nullSafe(phase.getName())).append(": ")
          .append(nullSafe(phase.getDescription())).append(" (Timing: ")
          .append(nullSafe(phase.getTiming())).append(")\n");
      }
      sb.append("Safety Gates: ");
      sb.append(coa.getSafetyGates().stream()
          .map(SafetyGate::getName)
          .collect(Collectors.joining(", ")));
      sb.append("\nKey Assumptions: ").append(String.join("; ", coa.getKeyAssumptions())).append("\n");
      sb.append("Risks: ").append(String.join("; ", coa.getRisks())).append("\n");
      sb.append("Info Needs: ").append(String.join("; ", coa.getInfoNeeds())).append("\n");
      sb.append("Assessment Measures: ").append(String.join("; ", coa.getAssessmentMeasures())).append("\n");
    }

    TraceabilityRecord trace = new TraceabilityRecord("6");
    trace.getInputsUsed().add("COA objectives, phases, safety gates, assumptions, risks");
    trace.getRulesApplied().add("COA formatting and non-tactical constraint rule");
    trace.getOutputsCreated().add("COA Summary Draft");

    return new DraftResult(sb.toString(), trace);
  }

  public static DraftResult generateRehearsalDraft(Scenario scenario) {
    RehearsalPlan plan = scenario.getRehearsalPlan();
    StringBuilder sb = new StringBuilder();
    sb.append("Branch Cards:\n");
    for (RehearsalEvent event : plan.getEvents()) {
      sb.append("- Event: ").append(nullSafe(event.getEventName())).append("\n")
        .append("  Indicator: ").append(nullSafe(event.getIndicator())).append("\n")
        .append("  Trigger: ").append(nullSafe(event.getTriggerThreshold())).append("\n")
        .append("  Decision: ").append(event.getDecision()).append("\n")
        .append("  Role: ").append(nullSafe(event.getResponsibleRole())).append("\n")
        .append("  Time Impact: ").append(event.getTimeImpactHours()).append(" hrs\n")
        .append("  Risk Impact: ").append(event.getRiskImpact()).append("\n")
        .append("  Safety Gate: ").append(nullSafe(event.getSafetyGateInvoked())).append("\n\n");
    }

    TraceabilityRecord trace = new TraceabilityRecord("7");
    trace.getInputsUsed().add("Rehearsal event list with indicators, triggers, decisions");
    trace.getRulesApplied().add("Branch card generation rule");
    trace.getOutputsCreated().add("Branch Cards Draft");

    return new DraftResult(sb.toString(), trace);
  }

  public static DraftResult generateDecisionGatesDraft(Scenario scenario) {
    DecisionGates gates = scenario.getDecisionGates();
    StringBuilder sb = new StringBuilder();
    sb.append("Decision & Safety Gates:\n");
    for (SafetyGate gate : gates.getGates()) {
      sb.append("- ").append(nullSafe(gate.getName()))
        .append(" | Threshold: ").append(nullSafe(gate.getThreshold()))
        .append(" | Action: ").append(nullSafe(gate.getAction()))
        .append(" | Owner: ").append(nullSafe(gate.getOwnerRole()))
        .append("\n");
    }
    sb.append("Stop/Go Criteria: ").append(nullSafe(gates.getStopGoCriteria()));

    TraceabilityRecord trace = new TraceabilityRecord("8");
    trace.getInputsUsed().add("Safety gates and stop/go criteria");
    trace.getRulesApplied().add("Gate list formatting rule");
    trace.getOutputsCreated().add("Decision Gates Draft");

    return new DraftResult(sb.toString(), trace);
  }

  public static DraftResult generateTeamDraft(Scenario scenario) {
    TeamResponsibilities responsibilities = scenario.getTeamResponsibilities();
    StringBuilder sb = new StringBuilder();
    sb.append("RACI Matrix Summary:\n");
    for (RaciEntry entry : responsibilities.getRaci()) {
      sb.append("- Task: ").append(nullSafe(entry.getTask()))
        .append(" | Role: ").append(nullSafe(entry.getRole()))
        .append(" | ").append(entry.getResponsibility())
        .append(" | Details: ").append(nullSafe(entry.getDetails()))
        .append("\n");
    }
    sb.append("Notes: ").append(nullSafe(responsibilities.getNotes()));

    TraceabilityRecord trace = new TraceabilityRecord("9");
    trace.getInputsUsed().add("RACI entries and notes");
    trace.getRulesApplied().add("RACI summary rule");
    trace.getOutputsCreated().add("Team Responsibilities Draft");

    return new DraftResult(sb.toString(), trace);
  }

  public static DraftResult generateMissionDraft(Scenario scenario) {
    MissionConops mission = scenario.getMissionConops();
    IncidentInput incident = scenario.getIncidentInput();
    ConstraintsAuthorities constraints = scenario.getConstraintsAuthorities();
    EnvironmentModel env = scenario.getEnvironmentModel();

    String sectors = env.getSectors().stream().map(Sector::getName).collect(Collectors.joining(", "));
    String timeWindow = nullSafe(incident.getDaylightWindow());
    String constraintsText = nullSafe(constraints.getWeatherAviationRestrictions());

    String missionStatement = "Rescue Cell will conduct coordinated search and recovery of missing hiker within "
      + (timeWindow.isBlank() ? "the operational time window" : timeWindow)
      + " across " + (sectors.isBlank() ? "designated sectors" : sectors)
      + " under " + (constraintsText.isBlank() ? "safety and legal constraints" : constraintsText)
      + " in order to achieve the end state: locate, stabilize, and handover.";

    mission.setMissionStatement(missionStatement);

    StringBuilder sb = new StringBuilder();
    sb.append("Mission Statement:\n").append(missionStatement).append("\n\n");
    sb.append("CONOPS Phase 0: ").append(nullSafe(mission.getConopsPhase0())).append("\n");
    sb.append("CONOPS Phase 1: ").append(nullSafe(mission.getConopsPhase1())).append("\n");
    sb.append("CONOPS Phase 2: ").append(nullSafe(mission.getConopsPhase2())).append("\n");
    sb.append("CONOPS Phase 3: ").append(nullSafe(mission.getConopsPhase3())).append("\n");
    sb.append("Timeline: ").append(nullSafe(mission.getTimeline()));

    TraceabilityRecord trace = new TraceabilityRecord("10");
    trace.getInputsUsed().add("Incident daylight window, sector list, constraints");
    trace.getRulesApplied().add("Mission statement template rule");
    trace.getOutputsCreated().add("Mission Statement and CONOPS Draft");

    return new DraftResult(sb.toString(), trace);
  }

  public static DraftResult generateAssessmentDraft(Scenario scenario) {
    AssessmentFeedback assessment = scenario.getAssessmentFeedback();
    StringBuilder sb = new StringBuilder();
    sb.append("Assessment & Feedback Loop:\n");
    sb.append("MOEs: ").append(nullSafe(assessment.getMoes())).append("\n");
    sb.append("MOPs: ").append(nullSafe(assessment.getMops())).append("\n");
    sb.append("Review Triggers: ").append(nullSafe(assessment.getReviewTriggers())).append("\n");
    sb.append("Learning Log: ").append(nullSafe(assessment.getLearningLog()));

    TraceabilityRecord trace = new TraceabilityRecord("11");
    trace.getInputsUsed().add("MOEs, MOPs, review triggers, learning log");
    trace.getRulesApplied().add("Assessment compilation rule");
    trace.getOutputsCreated().add("Assessment Draft");

    return new DraftResult(sb.toString(), trace);
  }

  private static String nullSafe(String value) {
    return value == null ? "" : value.trim();
  }
}
