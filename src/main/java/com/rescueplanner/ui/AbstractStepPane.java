package com.rescueplanner.ui;

import com.rescueplanner.model.Scenario;
import com.rescueplanner.model.TraceabilityRecord;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public abstract class AbstractStepPane extends VBox implements StepPane {
  protected final String stepId;
  protected final String title;
  protected Scenario scenario;
  protected final TextArea draftArea = new TextArea();
  protected final Button generateButton = new Button("Generate Draft");
  protected Consumer<TraceabilityRecord> traceabilityConsumer = record -> {};

  protected AbstractStepPane(String stepId, String title) {
    this.stepId = stepId;
    this.title = title;
    setSpacing(12);
    setPadding(new Insets(16));

    generateButton.setOnAction(FxUtil.debugAction(title + " - Generate Draft", () -> {
      saveToScenario();
      TraceabilityRecord record = onGenerateDraft();
      if (record != null) {
        traceabilityConsumer.accept(record);
      }
    }));
  }

  protected abstract Node buildContent();

  protected abstract TraceabilityRecord onGenerateDraft();

  protected void buildLayout(Node content) {
    Label titleLabel = new Label(title);
    titleLabel.getStyleClass().add("step-title");

    HBox actions = new HBox(12, generateButton);

    draftArea.setPromptText("Draft output for this step");
    draftArea.setPrefRowCount(6);

    getChildren().setAll(titleLabel, content, actions, new Label("Draft Output"), draftArea);
  }

  @Override
  public String getStepId() {
    return stepId;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public Node getNode() {
    return this;
  }

  @Override
  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
  }

  @Override
  public void setTraceabilityConsumer(Consumer<TraceabilityRecord> consumer) {
    this.traceabilityConsumer = consumer == null ? record -> {} : consumer;
  }
}
