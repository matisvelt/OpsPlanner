package com.rescueplanner.ui;

import com.rescueplanner.model.MissionConops;
import com.rescueplanner.model.TraceabilityRecord;
import com.rescueplanner.service.DraftGenerator;
import com.rescueplanner.service.DraftResult;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class MissionConopsStepPane extends AbstractStepPane {
  private final TextArea missionStatementArea = new TextArea();
  private final TextArea phase0Area = new TextArea();
  private final TextArea phase1Area = new TextArea();
  private final TextArea phase2Area = new TextArea();
  private final TextArea phase3Area = new TextArea();
  private final TextArea timelineArea = new TextArea();

  public MissionConopsStepPane() {
    super("10", "10) Mission Statement + CONOPS");
    missionStatementArea.setPrefRowCount(3);
    phase0Area.setPrefRowCount(2);
    phase1Area.setPrefRowCount(2);
    phase2Area.setPrefRowCount(2);
    phase3Area.setPrefRowCount(2);
    timelineArea.setPrefRowCount(2);
  }

  @Override
  protected Node buildContent() {
    GridPane grid = FxUtil.createFormGrid();
    FxUtil.addRow(grid, 0, "Mission Statement", missionStatementArea);
    FxUtil.addRow(grid, 1, "Phase 0", phase0Area);
    FxUtil.addRow(grid, 2, "Phase 1", phase1Area);
    FxUtil.addRow(grid, 3, "Phase 2", phase2Area);
    FxUtil.addRow(grid, 4, "Phase 3", phase3Area);
    FxUtil.addRow(grid, 5, "Timeline", timelineArea);
    return grid;
  }

  @Override
  protected TraceabilityRecord onGenerateDraft() {
    DraftResult result = DraftGenerator.generateMissionDraft(scenario);
    scenario.getMissionConops().setDraft(result.getDraft());
    missionStatementArea.setText(scenario.getMissionConops().getMissionStatement());
    draftArea.setText(result.getDraft());
    return result.getTraceability();
  }

  @Override
  public void refresh() {
    MissionConops mission = scenario.getMissionConops();
    missionStatementArea.setText(nullSafe(mission.getMissionStatement()));
    phase0Area.setText(nullSafe(mission.getConopsPhase0()));
    phase1Area.setText(nullSafe(mission.getConopsPhase1()));
    phase2Area.setText(nullSafe(mission.getConopsPhase2()));
    phase3Area.setText(nullSafe(mission.getConopsPhase3()));
    timelineArea.setText(nullSafe(mission.getTimeline()));
    draftArea.setText(nullSafe(mission.getDraft()));
  }

  @Override
  public void saveToScenario() {
    MissionConops mission = scenario.getMissionConops();
    mission.setMissionStatement(missionStatementArea.getText());
    mission.setConopsPhase0(phase0Area.getText());
    mission.setConopsPhase1(phase1Area.getText());
    mission.setConopsPhase2(phase2Area.getText());
    mission.setConopsPhase3(phase3Area.getText());
    mission.setTimeline(timelineArea.getText());
    mission.setDraft(draftArea.getText());
  }

  private String nullSafe(String value) {
    return value == null ? "" : value;
  }
}
