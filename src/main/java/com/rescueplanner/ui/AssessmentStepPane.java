package com.rescueplanner.ui;

import com.rescueplanner.model.AssessmentFeedback;
import com.rescueplanner.model.TraceabilityRecord;
import com.rescueplanner.service.DraftGenerator;
import com.rescueplanner.service.DraftResult;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class AssessmentStepPane extends AbstractStepPane {
  private final TextArea moesArea = new TextArea();
  private final TextArea mopsArea = new TextArea();
  private final TextArea reviewArea = new TextArea();
  private final TextArea learningArea = new TextArea();

  public AssessmentStepPane() {
    super("11", "11) Assessment & Feedback Loop");
    moesArea.setPrefRowCount(2);
    mopsArea.setPrefRowCount(2);
    reviewArea.setPrefRowCount(2);
    learningArea.setPrefRowCount(3);
  }

  @Override
  protected Node buildContent() {
    GridPane grid = FxUtil.createFormGrid();
    FxUtil.addRow(grid, 0, "MOEs", moesArea);
    FxUtil.addRow(grid, 1, "MOPs", mopsArea);
    FxUtil.addRow(grid, 2, "Review Triggers", reviewArea);
    FxUtil.addRow(grid, 3, "Learning Log", learningArea);
    return grid;
  }

  @Override
  protected TraceabilityRecord onGenerateDraft() {
    DraftResult result = DraftGenerator.generateAssessmentDraft(scenario);
    scenario.getAssessmentFeedback().setDraft(result.getDraft());
    draftArea.setText(result.getDraft());
    return result.getTraceability();
  }

  @Override
  public void refresh() {
    AssessmentFeedback feedback = scenario.getAssessmentFeedback();
    moesArea.setText(nullSafe(feedback.getMoes()));
    mopsArea.setText(nullSafe(feedback.getMops()));
    reviewArea.setText(nullSafe(feedback.getReviewTriggers()));
    learningArea.setText(nullSafe(feedback.getLearningLog()));
    draftArea.setText(nullSafe(feedback.getDraft()));
  }

  @Override
  public void saveToScenario() {
    AssessmentFeedback feedback = scenario.getAssessmentFeedback();
    feedback.setMoes(moesArea.getText());
    feedback.setMops(mopsArea.getText());
    feedback.setReviewTriggers(reviewArea.getText());
    feedback.setLearningLog(learningArea.getText());
    feedback.setDraft(draftArea.getText());
  }

  private String nullSafe(String value) {
    return value == null ? "" : value;
  }
}
