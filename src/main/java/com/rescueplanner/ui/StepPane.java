package com.rescueplanner.ui;

import com.rescueplanner.model.Scenario;
import com.rescueplanner.model.TraceabilityRecord;
import javafx.scene.Node;

import java.util.function.Consumer;

public interface StepPane {
  String getStepId();
  String getTitle();
  Node getNode();
  void setScenario(Scenario scenario);
  void setTraceabilityConsumer(Consumer<TraceabilityRecord> consumer);
  void refresh();
  void saveToScenario();
}
