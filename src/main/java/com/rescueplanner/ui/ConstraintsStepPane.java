package com.rescueplanner.ui;

import com.rescueplanner.model.ConstraintsAuthorities;
import com.rescueplanner.model.TraceabilityRecord;
import com.rescueplanner.service.DraftGenerator;
import com.rescueplanner.service.DraftResult;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class ConstraintsStepPane extends AbstractStepPane {
  private final TextArea legalAuthorityArea = new TextArea();
  private final TextArea safetyPolicyArea = new TextArea();
  private final TextArea timeWindowsArea = new TextArea();
  private final TextArea weatherAviationArea = new TextArea();
  private final TextArea medicalConstraintsArea = new TextArea();

  public ConstraintsStepPane() {
    super("2", "2) Constraints & Authorities");
    legalAuthorityArea.setPrefRowCount(2);
    safetyPolicyArea.setPrefRowCount(2);
    timeWindowsArea.setPrefRowCount(2);
    weatherAviationArea.setPrefRowCount(2);
    medicalConstraintsArea.setPrefRowCount(2);
  }

  @Override
  protected Node buildContent() {
    GridPane grid = FxUtil.createFormGrid();
    FxUtil.addRow(grid, 0, "Legal Authority", legalAuthorityArea);
    FxUtil.addRow(grid, 1, "Safety Policy", safetyPolicyArea);
    FxUtil.addRow(grid, 2, "Time Windows", timeWindowsArea);
    FxUtil.addRow(grid, 3, "Weather/Aviation Restrictions", weatherAviationArea);
    FxUtil.addRow(grid, 4, "Medical Constraints", medicalConstraintsArea);
    return grid;
  }

  @Override
  protected TraceabilityRecord onGenerateDraft() {
    DraftResult result = DraftGenerator.generateConstraintsDraft(scenario);
    scenario.getConstraintsAuthorities().setDraft(result.getDraft());
    draftArea.setText(result.getDraft());
    return result.getTraceability();
  }

  @Override
  public void refresh() {
    ConstraintsAuthorities ca = scenario.getConstraintsAuthorities();
    legalAuthorityArea.setText(nullSafe(ca.getLegalAuthority()));
    safetyPolicyArea.setText(nullSafe(ca.getSafetyPolicy()));
    timeWindowsArea.setText(nullSafe(ca.getTimeWindows()));
    weatherAviationArea.setText(nullSafe(ca.getWeatherAviationRestrictions()));
    medicalConstraintsArea.setText(nullSafe(ca.getMedicalConstraints()));
    draftArea.setText(nullSafe(ca.getDraft()));
  }

  @Override
  public void saveToScenario() {
    ConstraintsAuthorities ca = scenario.getConstraintsAuthorities();
    ca.setLegalAuthority(legalAuthorityArea.getText());
    ca.setSafetyPolicy(safetyPolicyArea.getText());
    ca.setTimeWindows(timeWindowsArea.getText());
    ca.setWeatherAviationRestrictions(weatherAviationArea.getText());
    ca.setMedicalConstraints(medicalConstraintsArea.getText());
    ca.setDraft(draftArea.getText());
  }

  private String nullSafe(String value) {
    return value == null ? "" : value;
  }
}
