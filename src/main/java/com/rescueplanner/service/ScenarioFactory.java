package com.rescueplanner.service;

import com.rescueplanner.model.*;

import java.util.ArrayList;
import java.util.List;

public class ScenarioFactory {
  private ScenarioFactory() {
  }

  public static Scenario prefilledTrainingScenario() {
    Scenario scenario = new Scenario();
    scenario.setTitle("Avalanche Aftermath: Missing Hiker Recovery (Training Case)");

    IncidentInput incident = scenario.getIncidentInput();
    incident.setTitle("Avalanche Aftermath: Missing Hiker Recovery (Training Case)");
    incident.setContext("A solo hiker is overdue after an avalanche event in a mountainous region. "
      + "Last known area is an approximate polygon spanning Sectors A/B/C based on cell ping and witness report. "
      + "Weather is deteriorating (wind, snowfall) and daylight is limited.");
    incident.setLastKnownArea("Polygon across Sectors A/B/C with uncertainty gradient toward Sector C");
    incident.setTimeLastSeen("~4 hours ago (training estimate)");
    incident.setDaylightWindow("Approx. 3 hours of usable light remaining (training estimate)");
    incident.setWeatherSummary("Wind increasing, snowfall forecast; visibility trending downward.");
    incident.setCommsSummary("Patchy coverage; reliable only in lower elevations and ridgeline gaps.");
    incident.setMedicalRiskSummary("Hypothermia risk increases with time; survivability window uncertain.");

    ConstraintsAuthorities constraints = scenario.getConstraintsAuthorities();
    constraints.setLegalAuthority("Search authorized under regional SAR protocol with park service oversight.");
    constraints.setSafetyPolicy("No entry into high-hazard zones without explicit Safety Officer gate approval.");
    constraints.setTimeWindows("Primary search window: daylight remaining; night ops require separate approval.");
    constraints.setWeatherAviationRestrictions("Aviation restricted above wind index threshold; whiteout risk.");
    constraints.setMedicalConstraints("Rapid transfer to medical care required if located; triage protocols apply.");

    InformationRequirements info = scenario.getInformationRequirements();
    List<InfoRequirement> reqs = new ArrayList<>();
    reqs.add(new InfoRequirement("Last known location confidence", "Updated cell ping or witness validation", "High", "Open"));
    reqs.add(new InfoRequirement("Victim survivability time window", "Medical lead estimate vs exposure", "High", "Open"));
    reqs.add(new InfoRequirement("Avalanche hazard index by sector", "Avalanche bulletin + local observation", "High", "Open"));
    reqs.add(new InfoRequirement("Access time to each sector", "Route status + mobility estimate", "Medium", "Open"));
    reqs.add(new InfoRequirement("Comms reliability by sector", "Radio checks + relay test", "Medium", "Open"));
    reqs.add(new InfoRequirement("Helicopter constraints and go/no-go factors", "Aviation liaison update", "Medium", "Open"));
    reqs.add(new InfoRequirement("Medical evacuation constraints", "Medical lead + transport availability", "Medium", "Open"));
    info.setRequirements(reqs);
    info.setDerivedNeeds("Clarify sector probability weights and confirm road access status before committing resources.");

    EnvironmentModel env = scenario.getEnvironmentModel();
    env.getSectors().add(makeSector("A", "Ridge bowl", 3, 4, 2, 0.60, 1.5, 3.0, 0.35));
    env.getSectors().add(makeSector("B", "Tree line traverse", 4, 3, 2, 0.50, 2.0, 4.0, 0.25));
    env.getSectors().add(makeSector("C", "Upper basin", 5, 5, 1, 0.30, 3.0, 6.0, 0.20));
    env.getSectors().add(makeSector("D", "Lower drainage", 2, 2, 3, 0.70, 1.0, 2.5, 0.15));
    env.getSectors().add(makeSector("E", "Trailhead area", 1, 1, 4, 0.90, 0.5, 1.0, 0.05));
    env.getDependencies().add(new Dependency("Road 2 open", "Sector C access"));
    env.getDependencies().add(new Dependency("Wind index below threshold", "Air support availability"));
    env.setUncertaintyMap("Uncertainty highest in Sector C due to limited visibility and comms gaps.");

    RiskRegister riskRegister = scenario.getRiskRegister();
    riskRegister.getRisks().add(new RiskItem("R1", "Secondary avalanche in Sector C", 4, 5,
      "Gate entry on hazard index and visibility updates", "Safety Officer"));
    riskRegister.getRisks().add(new RiskItem("R2", "Comms relay failure in upper sectors", 3, 4,
      "Establish relay plan and periodic check-ins", "Comms & Tracking"));
    riskRegister.getRisks().add(new RiskItem("R3", "Access route blocked by snowfall", 3, 4,
      "Alternate route planning and mobility staging", "Mobility/Logistics"));
    riskRegister.getRisks().add(new RiskItem("R4", "Survivability window shorter than expected", 3, 5,
      "Prioritize high-probability sectors and medical readiness", "Medical Lead"));
    riskRegister.getAssumptions().add(new Assumption("Helicopter availability remains uncertain", ConfidenceLevel.LOW, FragilityTag.VOLATILE));
    riskRegister.getAssumptions().add(new Assumption("Sector probability weights remain stable for 2 hours", ConfidenceLevel.MEDIUM, FragilityTag.FRAGILE));
    riskRegister.getAssumptions().add(new Assumption("Road 2 remains open", ConfidenceLevel.LOW, FragilityTag.FRAGILE));

    COASet coaSet = scenario.getCoaSet();
    coaSet.getCoas().addAll(defaultCoas());

    RehearsalPlan rehearsal = scenario.getRehearsalPlan();
    rehearsal.getEvents().add(event("E1", "Weather worsens faster than forecast", "Wind gusts exceed forecast trend",
      "Wind index crosses threshold", DecisionAction.BRANCH, "Safety Officer", 1.0, 4, "Weather cutoff"));
    rehearsal.getEvents().add(event("E2", "Secondary avalanche risk rises", "Avalanche bulletin escalates",
      "Hazard index >= 4", DecisionAction.REALLOCATE, "Safety Officer", 1.0, 5, "Avalanche risk gate"));
    rehearsal.getEvents().add(event("E3", "New witness report changes last-known location", "Credible witness update",
      "Confidence shift > 20%", DecisionAction.BRANCH, "Intel/Info Officer", 0.5, 3, "Info update gate"));
    rehearsal.getEvents().add(event("E4", "Drone feed unavailable", "No video link or telemetry",
      "Feed loss > 10 min", DecisionAction.CONTINUE, "Technical Specialist", 0.5, 2, "Comms reliability gate"));
    rehearsal.getEvents().add(event("E5", "Radio relay fails", "Missed check-ins",
      "Two consecutive misses", DecisionAction.REALLOCATE, "Comms & Tracking", 0.5, 4, "Comms blackout gate"));
    rehearsal.getEvents().add(event("E6", "Vehicle route blocked", "Road 2 closure",
      "Confirmed by liaison", DecisionAction.BRANCH, "Mobility/Logistics", 1.5, 4, "Access gate"));
    rehearsal.getEvents().add(event("E7", "Medical condition estimate worsens", "Updated exposure estimate",
      "Survivability window reduced", DecisionAction.REALLOCATE, "Medical Lead", 0.5, 5, "Medical threshold gate"));
    rehearsal.getEvents().add(event("E8", "Helicopter grounded", "Aviation update",
      "No-fly declared", DecisionAction.CONTINUE, "Liaison", 0.0, 3, "Aviation gate"));
    rehearsal.getEvents().add(event("E9", "Responder injury occurs", "Medical call-in",
      "Any responder injury", DecisionAction.ABORT, "Team Lead", 1.0, 5, "Personnel safety gate"));
    rehearsal.getEvents().add(event("E10", "Comms coverage improves", "New relay established",
      "Relay stable for 20 min", DecisionAction.CONTINUE, "Comms & Tracking", -0.5, 1, "Comms reliability gate"));

    DecisionGates decisionGates = scenario.getDecisionGates();
    decisionGates.getGates().add(new SafetyGate("Weather cutoff", "Wind index >= threshold or visibility <= 2", "Pause/Branch", "Safety Officer"));
    decisionGates.getGates().add(new SafetyGate("Avalanche risk gate", "Hazard index >= 4 in a sector", "Stop/Go by sector", "Safety Officer"));
    decisionGates.getGates().add(new SafetyGate("Comms blackout gate", "No check-in for 30 min", "Pause and re-establish comms", "Comms & Tracking"));
    decisionGates.getGates().add(new SafetyGate("Medical threshold gate", "Survivability window < 2 hours", "Reprioritize and compress timeline", "Medical Lead"));
    decisionGates.setStopGoCriteria("Stop or branch if any gate triggers beyond threshold; continue only with Safety Officer concurrence.");

    TeamResponsibilities team = scenario.getTeamResponsibilities();
    team.getRaci().add(new RaciEntry("Set incident priorities", Role.TEAM_LEAD.getLabel(), ResponsibilityType.A, "Approve COA selection"));
    team.getRaci().add(new RaciEntry("Manage safety thresholds", Role.SAFETY_OFFICER.getLabel(), ResponsibilityType.R, "Own stop/go criteria"));
    team.getRaci().add(new RaciEntry("Update information requirements", Role.INTEL_INFO_OFFICER.getLabel(), ResponsibilityType.R, "Track indicators and warnings"));
    team.getRaci().add(new RaciEntry("Coordinate tasking and timing", Role.OPS_COORDINATOR.getLabel(), ResponsibilityType.R, "Sequence phases"));
    team.getRaci().add(new RaciEntry("Maintain comms and tracking", Role.COMMS_TRACKING.getLabel(), ResponsibilityType.R, "Check-in schedule"));
    team.getRaci().add(new RaciEntry("Medical readiness and triage", Role.MEDICAL_LEAD.getLabel(), ResponsibilityType.R, "Medical thresholds"));
    team.getRaci().add(new RaciEntry("Mobility and access planning", Role.MOBILITY_LOGISTICS.getLabel(), ResponsibilityType.R, "Route status"));
    team.getRaci().add(new RaciEntry("External coordination", Role.LIAISON.getLabel(), ResponsibilityType.R, "Authorities and aviation updates"));
    team.getRaci().add(new RaciEntry("Technical support", Role.TECHNICAL_SPECIALIST.getLabel(), ResponsibilityType.C, "Sensor availability"));
    team.getRaci().add(new RaciEntry("Decision log & assessment", Role.RECORDER_ASSESSMENT.getLabel(), ResponsibilityType.R, "Capture decisions"));
    team.setNotes("Roles can be combined depending on staffing; maintain clear accountability.");

    MissionConops mission = scenario.getMissionConops();
    mission.setConopsPhase0("Phase 0 (0-30 min): Confirm constraints, establish comms plan, validate latest intel.");
    mission.setConopsPhase1("Phase 1 (30-120 min): Execute prioritized sector search sequence per COA with safety gates.");
    mission.setConopsPhase2("Phase 2 (120-240 min): Expand to lower-probability sectors; reassess based on indicators.");
    mission.setConopsPhase3("Phase 3 (240+ min): Consolidate findings, transition to recovery/handover." );
    mission.setTimeline("Initial decision within 30 minutes; hourly reassessment or upon trigger.");

    AssessmentFeedback assessment = scenario.getAssessmentFeedback();
    assessment.setMoes("Time to locate, adherence to safety gates, probability-weighted coverage.");
    assessment.setMops("Check-in compliance, comms uptime, gate trigger response times.");
    assessment.setReviewTriggers("Any gate trigger, major intel shift, or weather severity change.");
    assessment.setLearningLog("Capture decisions, what changed, and why for after-action review.");

    return scenario;
  }

  public static List<COA> defaultCoas() {
    List<COA> coas = new ArrayList<>();
    coas.add(defaultCoa1());
    coas.add(defaultCoa2());
    coas.add(defaultCoa3());
    coas.add(defaultCoa4());
    return coas;
  }

  private static Sector makeSector(String name, String size, int access, int hazard, int visibility,
                                   double comms, double minTime, double maxTime, double weight) {
    Sector sector = new Sector(name);
    sector.setSize(size);
    sector.setAccessDifficultyIndex(access);
    sector.setAvalancheHazardIndex(hazard);
    sector.setVisibilityIndex(visibility);
    sector.setCommsReliability(comms);
    sector.setTimeToSearchMinHours(minTime);
    sector.setTimeToSearchMaxHours(maxTime);
    sector.setProbabilityWeight(weight);
    return sector;
  }

  private static RehearsalEvent event(String id, String name, String indicator, String trigger,
                                      DecisionAction decision, String role, double timeImpact, int riskImpact, String gate) {
    RehearsalEvent event = new RehearsalEvent(id, name);
    event.setIndicator(indicator);
    event.setTriggerThreshold(trigger);
    event.setDecision(decision);
    event.setResponsibleRole(role);
    event.setTimeImpactHours(timeImpact);
    event.setRiskImpact(riskImpact);
    event.setSafetyGateInvoked(gate);
    return event;
  }

  private static COA defaultCoa1() {
    COA coa = new COA("COA-1", "Ground-first prioritized by probability distribution");
    coa.setObjective("Allocate ground teams to highest-probability sectors while preserving safety gates.");
    coa.getPhases().add(new COAPhase("Phase 0", "Confirm access, comms, and hazard updates", "0-30 min", "Team Lead, Safety Officer"));
    coa.getPhases().add(new COAPhase("Phase 1", "Search Sector A then B based on probability weights", "30-120 min", "Ops Coordinator"));
    coa.getPhases().add(new COAPhase("Phase 2", "Expand to Sector C/D if gates permit", "120-240 min", "Ops Coordinator"));
    coa.getPhases().add(new COAPhase("Phase 3", "Consolidate findings and transition to handover", "240+ min", "Team Lead"));
    coa.getResourceAssignments().add(new ResourceAssignment(Role.OPS_COORDINATOR.getLabel(), "Direct sequencing", "Maintain gate checks"));
    coa.getResourceAssignments().add(new ResourceAssignment(Role.SAFETY_OFFICER.getLabel(), "Hazard oversight", "Gate authority"));
    coa.getResourceAssignments().add(new ResourceAssignment(Role.COMMS_TRACKING.getLabel(), "Relay and check-ins", "Comms stability"));
    coa.getSafetyGates().add(new SafetyGate("Avalanche risk gate", "Hazard index >= 4", "Stop/Go by sector", Role.SAFETY_OFFICER.getLabel()));
    coa.getSafetyGates().add(new SafetyGate("Comms blackout gate", "No check-in for 30 min", "Pause/Recover", Role.COMMS_TRACKING.getLabel()));
    coa.getKeyAssumptions().add("Ground access remains viable for Sectors A/B");
    coa.getRisks().add("Time loss if access blocked");
    coa.getInfoNeeds().add("Updated probability weights by sector");
    coa.getAssessmentMeasures().add("Coverage of A/B within first 2 hours");
    return coa;
  }

  private static COA defaultCoa2() {
    COA coa = new COA("COA-2", "Safety-first constrained search (minimize responder exposure)");
    coa.setObjective("Limit exposure by restricting search to low-hazard sectors unless gates are cleared.");
    coa.getPhases().add(new COAPhase("Phase 0", "Confirm gates and hazard updates", "0-30 min", "Safety Officer"));
    coa.getPhases().add(new COAPhase("Phase 1", "Focus on low-hazard sectors D/E", "30-120 min", "Ops Coordinator"));
    coa.getPhases().add(new COAPhase("Phase 2", "Conditional expansion to A/B with safety concurrence", "120-240 min", "Team Lead"));
    coa.getPhases().add(new COAPhase("Phase 3", "Reassess and transition to recovery/handover", "240+ min", "Team Lead"));
    coa.getResourceAssignments().add(new ResourceAssignment(Role.SAFETY_OFFICER.getLabel(), "Hazard authority", "Hard stop on high risk"));
    coa.getResourceAssignments().add(new ResourceAssignment(Role.MEDICAL_LEAD.getLabel(), "Medical readiness", "Exposure monitoring"));
    coa.getSafetyGates().add(new SafetyGate("Weather cutoff", "Visibility <= 2", "Pause/Branch", Role.SAFETY_OFFICER.getLabel()));
    coa.getSafetyGates().add(new SafetyGate("Avalanche risk gate", "Hazard index >= 4", "No entry", Role.SAFETY_OFFICER.getLabel()));
    coa.getKeyAssumptions().add("Low-hazard sectors still allow meaningful coverage");
    coa.getRisks().add("Lower probability of contact if high-probability sectors deferred");
    coa.getInfoNeeds().add("Hazard updates every 30 minutes");
    coa.getAssessmentMeasures().add("Responder exposure kept below threshold");
    return coa;
  }

  private static COA defaultCoa3() {
    COA coa = new COA("COA-3", "Parallel sweep with comms relay emphasis");
    coa.setObjective("Run parallel sector checks supported by strong comms relay to reduce time to contact.");
    coa.getPhases().add(new COAPhase("Phase 0", "Establish comms relay and check-in plan", "0-30 min", "Comms & Tracking"));
    coa.getPhases().add(new COAPhase("Phase 1", "Parallel coverage of A/B/D", "30-120 min", "Ops Coordinator"));
    coa.getPhases().add(new COAPhase("Phase 2", "Expand to C if gates allow", "120-240 min", "Safety Officer"));
    coa.getPhases().add(new COAPhase("Phase 3", "Consolidate findings", "240+ min", "Team Lead"));
    coa.getResourceAssignments().add(new ResourceAssignment(Role.COMMS_TRACKING.getLabel(), "Relay emphasis", "Redundancy plan"));
    coa.getResourceAssignments().add(new ResourceAssignment(Role.OPS_COORDINATOR.getLabel(), "Parallel sequencing", "Deconflict timelines"));
    coa.getSafetyGates().add(new SafetyGate("Comms blackout gate", "Check-in lapse", "Pause and re-establish", Role.COMMS_TRACKING.getLabel()));
    coa.getKeyAssumptions().add("Comms relay can be sustained across parallel teams");
    coa.getRisks().add("Higher coordination load" );
    coa.getInfoNeeds().add("Relay coverage map" );
    coa.getAssessmentMeasures().add("Time-to-contact reduction vs baseline" );
    return coa;
  }

  private static COA defaultCoa4() {
    COA coa = new COA("COA-4", "Air-assisted reconnaissance dependent on weather gate");
    coa.setObjective("Use air reconnaissance for rapid situational update if aviation gates permit.");
    coa.getPhases().add(new COAPhase("Phase 0", "Confirm aviation go/no-go", "0-30 min", "Liaison"));
    coa.getPhases().add(new COAPhase("Phase 1", "Air recon to update sector probabilities", "30-90 min", "Technical Specialist"));
    coa.getPhases().add(new COAPhase("Phase 2", "Ground teams follow updated priorities", "90-240 min", "Ops Coordinator"));
    coa.getPhases().add(new COAPhase("Phase 3", "Consolidate findings", "240+ min", "Team Lead"));
    coa.getResourceAssignments().add(new ResourceAssignment(Role.LIAISON.getLabel(), "Aviation coordination", "Gate authority"));
    coa.getResourceAssignments().add(new ResourceAssignment(Role.TECHNICAL_SPECIALIST.getLabel(), "Sensor support", "Abstract only"));
    coa.getSafetyGates().add(new SafetyGate("Aviation gate", "Wind index below threshold", "Allow recon", Role.LIAISON.getLabel()));
    coa.getKeyAssumptions().add("Air recon is permitted under weather constraints");
    coa.getRisks().add("Aviation cancellation delays" );
    coa.getInfoNeeds().add("Wind index trend" );
    coa.getAssessmentMeasures().add("Updated probability distribution within 90 minutes" );
    return coa;
  }
}
