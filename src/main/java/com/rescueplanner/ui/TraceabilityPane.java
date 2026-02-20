package com.rescueplanner.ui;

import com.rescueplanner.model.TraceabilityRecord;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class TraceabilityPane extends VBox {
  private final ListView<String> inputsList = new ListView<>();
  private final ListView<String> rulesList = new ListView<>();
  private final ListView<String> outputsList = new ListView<>();

  public TraceabilityPane() {
    setSpacing(8);
    setPadding(new Insets(12));
    getStyleClass().add("traceability-pane");

    Label title = new Label("Traceability");
    title.getStyleClass().add("traceability-title");

    getChildren().addAll(
      title,
      new Label("Inputs Used"),
      inputsList,
      new Label("Rules Applied"),
      rulesList,
      new Label("Outputs Created"),
      outputsList
    );

    inputsList.setPrefHeight(120);
    rulesList.setPrefHeight(120);
    outputsList.setPrefHeight(120);
  }

  public void setRecord(TraceabilityRecord record) {
    if (record == null) {
      inputsList.setItems(FXCollections.observableArrayList("No draft generated yet"));
      rulesList.setItems(FXCollections.observableArrayList("--"));
      outputsList.setItems(FXCollections.observableArrayList("--"));
      return;
    }
    inputsList.setItems(FXCollections.observableArrayList(record.getInputsUsed()));
    rulesList.setItems(FXCollections.observableArrayList(record.getRulesApplied()));
    outputsList.setItems(FXCollections.observableArrayList(record.getOutputsCreated()));
  }
}
