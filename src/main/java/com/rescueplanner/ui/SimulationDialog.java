package com.rescueplanner.ui;

import com.rescueplanner.model.COA;
import com.rescueplanner.model.Scenario;
import com.rescueplanner.service.SimulationCoordinator;
import com.rescueplanner.sim.SimulationConfig;
import com.rescueplanner.sim.SimulationResult;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationDialog {
  private SimulationDialog() {
  }

  public static void show(Stage owner, Scenario scenario, SimulationCoordinator coordinator) {
    Stage stage = new Stage();
    stage.setTitle("Simulation - Monte Carlo Stress Test");
    stage.initOwner(owner);
    stage.initModality(Modality.WINDOW_MODAL);

    ComboBox<COA> coaBox = new ComboBox<>(FXCollections.observableArrayList(scenario.getCoaSet().getCoas()));
    coaBox.setPrefWidth(320);
    coaBox.setCellFactory(listView -> new ListCell<>() {
      @Override
      protected void updateItem(COA item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText("");
        } else {
          setText(item.getName());
        }
      }
    });
    coaBox.setButtonCell(new ListCell<>() {
      @Override
      protected void updateItem(COA item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText("");
        } else {
          setText(item.getName());
        }
      }
    });
    coaBox.getSelectionModel().selectFirst();

    TextField runsField = new TextField("5000");
    TextField seedField = new TextField(String.valueOf(System.currentTimeMillis()));
    TextArea workerArea = new TextArea();
    workerArea.setPromptText("Optional worker URLs, one per line (e.g., http://localhost:8090)");
    workerArea.setPrefRowCount(3);

    TextArea outputArea = new TextArea();
    outputArea.setPrefRowCount(10);

    Button runButton = new Button("Run Simulation");
    ProgressIndicator indicator = new ProgressIndicator();
    indicator.setVisible(false);

    runButton.setOnAction(event -> {
      COA coa = coaBox.getSelectionModel().getSelectedItem();
      int runs;
      long seed;
      try {
        runs = Integer.parseInt(runsField.getText().trim());
        seed = Long.parseLong(seedField.getText().trim());
      } catch (NumberFormatException ex) {
        showAlert("Invalid input", "Runs and seed must be numeric.");
        return;
      }

      SimulationConfig config = new SimulationConfig();
      config.setRuns(runs);
      config.setSeed(seed);

      List<String> workers = Arrays.stream(workerArea.getText().split("\\r?\\n"))
        .map(String::trim)
        .filter(line -> !line.isEmpty())
        .collect(Collectors.toList());

      indicator.setVisible(true);
      runButton.setDisable(true);

      Task<SimulationResult> task = new Task<>() {
        @Override
        protected SimulationResult call() throws Exception {
          return coordinator.runSimulation(scenario, coa, config, workers);
        }
      };

      task.setOnSucceeded(evt -> {
        SimulationResult result = task.getValue();
        outputArea.setText(result.toSummary());
        indicator.setVisible(false);
        runButton.setDisable(false);
      });

      task.setOnFailed(evt -> {
        outputArea.setText("Simulation failed: " + task.getException().getMessage());
        indicator.setVisible(false);
        runButton.setDisable(false);
      });

      new Thread(task).start();
    });

    GridPane form = new GridPane();
    form.setHgap(10);
    form.setVgap(10);
    form.add(new Label("COA"), 0, 0);
    form.add(coaBox, 1, 0);
    form.add(new Label("Runs (5k-100k)"), 0, 1);
    form.add(runsField, 1, 1);
    form.add(new Label("Seed"), 0, 2);
    form.add(seedField, 1, 2);

    HBox actions = new HBox(8, runButton, indicator);

    VBox root = new VBox(10, form, new Label("Worker URLs (optional)"), workerArea, actions,
      new Label("Results"), outputArea);
    root.setPadding(new Insets(12));

    stage.setScene(new Scene(root, 640, 520));
    stage.show();
  }

  private static void showAlert(String title, String message) {
    Platform.runLater(() -> {
      Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
      alert.setTitle(title);
      alert.setHeaderText(null);
      alert.showAndWait();
    });
  }
}
