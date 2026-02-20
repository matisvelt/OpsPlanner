package com.rescueplanner.ui;

import com.rescueplanner.model.IncidentInput;
import com.rescueplanner.model.TraceabilityRecord;
import com.rescueplanner.service.DraftGenerator;
import com.rescueplanner.service.DraftResult;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class IncidentStepPane extends AbstractStepPane {
  private final TextField titleField = new TextField();
  private final TextArea contextArea = new TextArea();
  private final TextField lastKnownField = new TextField();
  private final TextField timeLastSeenField = new TextField();
  private final TextField daylightField = new TextField();
  private final TextArea weatherArea = new TextArea();
  private final TextArea commsArea = new TextArea();
  private final TextArea medicalArea = new TextArea();

  public IncidentStepPane() {
    super("1", "1) Incident Input");
    contextArea.setPrefRowCount(3);
    weatherArea.setPrefRowCount(2);
    commsArea.setPrefRowCount(2);
    medicalArea.setPrefRowCount(2);
    buildLayout(buildContent());
  }

  @Override
  protected Node buildContent() {
    GridPane grid = FxUtil.createFormGrid();
    FxUtil.addRow(grid, 0, "Title", titleField);
    FxUtil.addRow(grid, 1, "Context", contextArea);
    FxUtil.addRow(grid, 2, "Last Known Area", lastKnownField);
    FxUtil.addRow(grid, 3, "Time Last Seen", timeLastSeenField);
    FxUtil.addRow(grid, 4, "Daylight Window", daylightField);
    FxUtil.addRow(grid, 5, "Weather Summary", weatherArea);
    FxUtil.addRow(grid, 6, "Comms Summary", commsArea);
    FxUtil.addRow(grid, 7, "Medical Risk Summary", medicalArea);
    return grid;
  }

  @Override
  protected TraceabilityRecord onGenerateDraft() {
    DraftResult result = DraftGenerator.generateIncidentDraft(scenario);
    scenario.getIncidentInput().setDraft(result.getDraft());
    draftArea.setText(result.getDraft());
    return result.getTraceability();
  }

  @Override
  public void refresh() {
    IncidentInput incident = scenario.getIncidentInput();
    titleField.setText(nullSafe(incident.getTitle()));
    contextArea.setText(nullSafe(incident.getContext()));
    lastKnownField.setText(nullSafe(incident.getLastKnownArea()));
    timeLastSeenField.setText(nullSafe(incident.getTimeLastSeen()));
    daylightField.setText(nullSafe(incident.getDaylightWindow()));
    weatherArea.setText(nullSafe(incident.getWeatherSummary()));
    commsArea.setText(nullSafe(incident.getCommsSummary()));
    medicalArea.setText(nullSafe(incident.getMedicalRiskSummary()));
    draftArea.setText(nullSafe(incident.getDraft()));
  }

  @Override
  public void saveToScenario() {
    IncidentInput incident = scenario.getIncidentInput();
    incident.setTitle(titleField.getText());
    incident.setContext(contextArea.getText());
    incident.setLastKnownArea(lastKnownField.getText());
    incident.setTimeLastSeen(timeLastSeenField.getText());
    incident.setDaylightWindow(daylightField.getText());
    incident.setWeatherSummary(weatherArea.getText());
    incident.setCommsSummary(commsArea.getText());
    incident.setMedicalRiskSummary(medicalArea.getText());
    incident.setDraft(draftArea.getText());
    scenario.setTitle(incident.getTitle());
  }

  private String nullSafe(String value) {
    return value == null ? "" : value;
  }
}
