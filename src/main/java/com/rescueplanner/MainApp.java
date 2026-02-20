package com.rescueplanner;

import com.rescueplanner.model.Scenario;
import com.rescueplanner.model.TraceabilityRecord;
import com.rescueplanner.service.*;
import com.rescueplanner.ui.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends Application {
  private Scenario scenario;
  private final ScenarioRepository repository = new ScenarioRepository();
  private final RescuePacketExporter exporter = new RescuePacketExporter();
  private final DraftOrchestrator draftOrchestrator = new DraftOrchestrator();
  private final SimulationCoordinator simulationCoordinator = new SimulationCoordinator();
  private final TraceabilityPane traceabilityPane = new TraceabilityPane();

  private final List<StepPane> steps = new ArrayList<>();
  private StepPane currentStep;

  @Override
  public void start(Stage stage) {
    scenario = ScenarioFactory.prefilledTrainingScenario();

    steps.add(new IncidentStepPane());
    steps.add(new ConstraintsStepPane());
    steps.add(new InfoRequirementsStepPane());
    steps.add(new EnvironmentStepPane());
    steps.add(new RiskStepPane());
    steps.add(new COAStepPane());
    steps.add(new RehearsalStepPane());
    steps.add(new DecisionGatesStepPane());
    steps.add(new TeamResponsibilitiesStepPane());
    steps.add(new MissionConopsStepPane());
    steps.add(new AssessmentStepPane());

    for (StepPane step : steps) {
      step.setScenario(scenario);
      step.setTraceabilityConsumer(record -> updateTraceability(record, step));
    }

    BorderPane root = new BorderPane();
    root.setTop(buildHeader(stage));
    root.setRight(traceabilityPane);

    StackPane centerPane = new StackPane();
    centerPane.setPadding(new Insets(8));
    root.setCenter(centerPane);
    root.setLeft(buildNavigation(centerPane));

    selectStep(0, centerPane);

    Scene scene = new Scene(root, 1400, 900);
    scene.getStylesheets().add(getClass().getResource("/com/rescueplanner/style.css").toExternalForm());
    stage.setTitle("Rescue Cell Planner (Training)");
    stage.setScene(scene);
    stage.show();
  }

  private VBox buildHeader(Stage stage) {
    Label banner = new Label("TRAINING / UNCLASSIFIED");
    banner.getStyleClass().add("banner");

    ToolBar toolBar = new ToolBar();
    Button newButton = new Button("New");
    Button loadButton = new Button("Load JSON");
    Button saveButton = new Button("Save JSON");
    Button exportHtmlButton = new Button("Export HTML");
    Button exportPdfButton = new Button("Export PDF");
    Button runSimButton = new Button("Run Simulation");

    toolBar.getItems().addAll(newButton, loadButton, saveButton, new Separator(), exportHtmlButton, exportPdfButton,
      new Separator(), runSimButton);

    newButton.setOnAction(event -> {
      scenario = ScenarioFactory.prefilledTrainingScenario();
      rebindScenario();
    });

    loadButton.setOnAction(event -> {
      FileChooser chooser = new FileChooser();
      chooser.setTitle("Load Scenario JSON");
      chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
      File file = chooser.showOpenDialog(stage);
      if (file != null) {
        try {
          scenario = repository.load(file.toPath());
          rebindScenario();
        } catch (Exception ex) {
          showAlert("Load failed", ex.getMessage());
        }
      }
    });

    saveButton.setOnAction(event -> {
      saveCurrentStep();
      FileChooser chooser = new FileChooser();
      chooser.setTitle("Save Scenario JSON");
      chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
      File file = chooser.showSaveDialog(stage);
      if (file != null) {
        try {
          repository.save(file.toPath(), scenario);
        } catch (Exception ex) {
          showAlert("Save failed", ex.getMessage());
        }
      }
    });

    exportHtmlButton.setOnAction(event -> {
      saveCurrentStep();
      draftOrchestrator.generateAll(scenario);
      FileChooser chooser = new FileChooser();
      chooser.setTitle("Export Rescue Packet (HTML)");
      chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML", "*.html"));
      File file = chooser.showSaveDialog(stage);
      if (file != null) {
        try {
          exporter.exportHtml(file.toPath(), scenario);
        } catch (Exception ex) {
          showAlert("Export failed", ex.getMessage());
        }
      }
    });

    exportPdfButton.setOnAction(event -> {
      saveCurrentStep();
      draftOrchestrator.generateAll(scenario);
      FileChooser chooser = new FileChooser();
      chooser.setTitle("Export Rescue Packet (PDF)");
      chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
      File file = chooser.showSaveDialog(stage);
      if (file != null) {
        try {
          exporter.exportPdf(file.toPath(), scenario);
        } catch (Exception ex) {
          showAlert("Export failed", ex.getMessage());
        }
      }
    });

    runSimButton.setOnAction(event -> {
      saveCurrentStep();
      SimulationDialog.show(stage, scenario, simulationCoordinator);
    });

    VBox header = new VBox(banner, toolBar);
    return header;
  }

  private Node buildNavigation(StackPane centerPane) {
    ListView<String> listView = new ListView<>();
    listView.setItems(FXCollections.observableArrayList(steps.stream().map(StepPane::getTitle).toList()));
    listView.getStyleClass().add("nav-list");
    listView.getSelectionModel().selectedIndexProperty().addListener((obs, old, selected) -> {
      if (selected == null || selected.intValue() < 0) {
        return;
      }
      selectStep(selected.intValue(), centerPane);
    });
    listView.getSelectionModel().selectFirst();
    return listView;
  }

  private void selectStep(int index, StackPane centerPane) {
    saveCurrentStep();
    currentStep = steps.get(index);
    currentStep.setScenario(scenario);
    currentStep.refresh();
    TraceabilityRecord record = scenario.getTraceability().get(currentStep.getStepId());
    traceabilityPane.setRecord(record);

    centerPane.getChildren().clear();
    centerPane.getChildren().add(currentStep.getNode());
  }

  private void saveCurrentStep() {
    if (currentStep != null) {
      currentStep.saveToScenario();
    }
  }

  private void rebindScenario() {
    for (StepPane step : steps) {
      step.setScenario(scenario);
      step.refresh();
    }
    traceabilityPane.setRecord(null);
  }

  private void updateTraceability(TraceabilityRecord record, StepPane step) {
    scenario.getTraceability().put(record.getStepId(), record);
    if (currentStep != null && currentStep == step) {
      traceabilityPane.setRecord(record);
    }
  }

  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.showAndWait();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
