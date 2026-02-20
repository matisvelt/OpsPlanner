package com.rescueplanner.ui;

import com.rescueplanner.model.*;
import com.rescueplanner.service.DraftGenerator;
import com.rescueplanner.service.DraftResult;
import com.rescueplanner.service.ScenarioFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class COAStepPane extends AbstractStepPane {
  private final TableView<COA> coaTable = new TableView<>();
  private final ObservableList<COA> coaItems = FXCollections.observableArrayList();
  private COA currentCoa;

  private final Label selectedLabel = new Label("Select a COA to edit details.");
  private final TextArea phasesArea = new TextArea();
  private final TextArea resourcesArea = new TextArea();
  private final TextArea safetyGatesArea = new TextArea();
  private final TextArea assumptionsArea = new TextArea();
  private final TextArea risksArea = new TextArea();
  private final TextArea infoNeedsArea = new TextArea();
  private final TextArea assessmentArea = new TextArea();

  public COAStepPane() {
    super("6", "6) COA Builder");
    coaTable.setEditable(true);
    phasesArea.setPrefRowCount(3);
    resourcesArea.setPrefRowCount(3);
    safetyGatesArea.setPrefRowCount(3);
    assumptionsArea.setPrefRowCount(2);
    risksArea.setPrefRowCount(2);
    infoNeedsArea.setPrefRowCount(2);
    assessmentArea.setPrefRowCount(2);
    buildLayout(buildContent());
  }

  @Override
  protected Node buildContent() {
    TableColumn<COA, String> idCol = new TableColumn<>("ID");
    idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    idCol.setCellFactory(TextFieldTableCell.forTableColumn());

    TableColumn<COA, String> nameCol = new TableColumn<>("Name");
    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    nameCol.setPrefWidth(260);

    TableColumn<COA, String> objCol = new TableColumn<>("Objective");
    objCol.setCellValueFactory(new PropertyValueFactory<>("objective"));
    objCol.setCellFactory(TextFieldTableCell.forTableColumn());
    objCol.setPrefWidth(260);

    coaTable.getColumns().addAll(idCol, nameCol, objCol);
    coaTable.setItems(coaItems);
    coaTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    Button addButton = new Button("Add COA");
    addButton.setOnAction(FxUtil.debugAction("COA Builder - Add COA", () ->
      coaItems.add(new COA("COA-#", "New COA"))));
    Button removeButton = new Button("Remove COA");
    removeButton.setOnAction(FxUtil.debugAction("COA Builder - Remove COA", () -> {
      COA selected = coaTable.getSelectionModel().getSelectedItem();
      if (selected != null) {
        coaItems.remove(selected);
      }
    }));
    Button defaultsButton = new Button("Generate Default COAs");
    defaultsButton.setOnAction(FxUtil.debugAction("COA Builder - Generate Defaults", () -> {
      coaItems.setAll(ScenarioFactory.defaultCoas());
      coaTable.getSelectionModel().selectFirst();
      loadCurrentCoa();
    }));

    HBox buttons = new HBox(8, addButton, removeButton, defaultsButton);

    coaTable.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
      saveCurrentCoaDetails(old);
      currentCoa = selected;
      loadCurrentCoa();
    });

    VBox details = new VBox(8,
      selectedLabel,
      new Label("Phases (one per line: name | description | timing | responsibilities)"), phasesArea,
      new Label("Resource Assignments (role | allocation | notes)"), resourcesArea,
      new Label("Safety Gates (name | threshold | action | owner)"), safetyGatesArea,
      new Label("Key Assumptions (one per line)"), assumptionsArea,
      new Label("Risks (one per line)"), risksArea,
      new Label("Info Needs (one per line)"), infoNeedsArea,
      new Label("Assessment Measures (one per line)"), assessmentArea
    );

    VBox box = new VBox(10, coaTable, buttons, details);
    return box;
  }

  @Override
  protected TraceabilityRecord onGenerateDraft() {
    DraftResult result = DraftGenerator.generateCoaDraft(scenario);
    scenario.getCoaSet().setDraft(result.getDraft());
    draftArea.setText(result.getDraft());
    return result.getTraceability();
  }

  @Override
  public void refresh() {
    coaItems.setAll(scenario.getCoaSet().getCoas());
    coaTable.getSelectionModel().selectFirst();
    currentCoa = coaTable.getSelectionModel().getSelectedItem();
    loadCurrentCoa();
    draftArea.setText(nullSafe(scenario.getCoaSet().getDraft()));
  }

  @Override
  public void saveToScenario() {
    saveCurrentCoaDetails(currentCoa);
    scenario.getCoaSet().setCoas(new ArrayList<>(coaItems));
    scenario.getCoaSet().setDraft(draftArea.getText());
  }

  private void loadCurrentCoa() {
    if (currentCoa == null) {
      selectedLabel.setText("Select a COA to edit details.");
      phasesArea.clear();
      resourcesArea.clear();
      safetyGatesArea.clear();
      assumptionsArea.clear();
      risksArea.clear();
      infoNeedsArea.clear();
      assessmentArea.clear();
      return;
    }
    selectedLabel.setText("Editing: " + currentCoa.getName());
    phasesArea.setText(serializePhases(currentCoa.getPhases()));
    resourcesArea.setText(serializeResources(currentCoa.getResourceAssignments()));
    safetyGatesArea.setText(serializeGates(currentCoa.getSafetyGates()));
    assumptionsArea.setText(String.join("\n", currentCoa.getKeyAssumptions()));
    risksArea.setText(String.join("\n", currentCoa.getRisks()));
    infoNeedsArea.setText(String.join("\n", currentCoa.getInfoNeeds()));
    assessmentArea.setText(String.join("\n", currentCoa.getAssessmentMeasures()));
  }

  private void saveCurrentCoaDetails(COA coa) {
    if (coa == null) {
      return;
    }
    coa.setPhases(parsePhases(phasesArea.getText()));
    coa.setResourceAssignments(parseResources(resourcesArea.getText()));
    coa.setSafetyGates(parseGates(safetyGatesArea.getText()));
    coa.setKeyAssumptions(parseLines(assumptionsArea.getText()));
    coa.setRisks(parseLines(risksArea.getText()));
    coa.setInfoNeeds(parseLines(infoNeedsArea.getText()));
    coa.setAssessmentMeasures(parseLines(assessmentArea.getText()));
  }

  private String serializePhases(List<COAPhase> phases) {
    return phases.stream()
      .map(phase -> String.join(" | ",
        safe(phase.getName()),
        safe(phase.getDescription()),
        safe(phase.getTiming()),
        safe(phase.getResponsibilities())))
      .collect(Collectors.joining("\n"));
  }

  private String serializeResources(List<ResourceAssignment> resources) {
    return resources.stream()
      .map(resource -> String.join(" | ",
        safe(resource.getRole()),
        safe(resource.getAllocation()),
        safe(resource.getNotes())))
      .collect(Collectors.joining("\n"));
  }

  private String serializeGates(List<SafetyGate> gates) {
    return gates.stream()
      .map(gate -> String.join(" | ",
        safe(gate.getName()),
        safe(gate.getThreshold()),
        safe(gate.getAction()),
        safe(gate.getOwnerRole())))
      .collect(Collectors.joining("\n"));
  }

  private List<COAPhase> parsePhases(String text) {
    List<COAPhase> phases = new ArrayList<>();
    for (String line : splitLines(text)) {
      String[] parts = line.split("\\|", -1);
      if (parts.length >= 4) {
        phases.add(new COAPhase(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()));
      } else if (!line.isBlank()) {
        phases.add(new COAPhase(line.trim(), "", "", ""));
      }
    }
    return phases;
  }

  private List<ResourceAssignment> parseResources(String text) {
    List<ResourceAssignment> resources = new ArrayList<>();
    for (String line : splitLines(text)) {
      String[] parts = line.split("\\|", -1);
      if (parts.length >= 3) {
        resources.add(new ResourceAssignment(parts[0].trim(), parts[1].trim(), parts[2].trim()));
      } else if (!line.isBlank()) {
        resources.add(new ResourceAssignment(line.trim(), "", ""));
      }
    }
    return resources;
  }

  private List<SafetyGate> parseGates(String text) {
    List<SafetyGate> gates = new ArrayList<>();
    for (String line : splitLines(text)) {
      String[] parts = line.split("\\|", -1);
      if (parts.length >= 4) {
        gates.add(new SafetyGate(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()));
      } else if (!line.isBlank()) {
        gates.add(new SafetyGate(line.trim(), "", "", ""));
      }
    }
    return gates;
  }

  private List<String> parseLines(String text) {
    return splitLines(text).stream()
      .filter(line -> !line.isBlank())
      .collect(Collectors.toList());
  }

  private List<String> splitLines(String text) {
    if (text == null || text.isBlank()) {
      return new ArrayList<>();
    }
    return Arrays.stream(text.split("\\r?\\n"))
      .map(String::trim)
      .collect(Collectors.toList());
  }

  private String safe(String value) {
    return value == null ? "" : value;
  }

  private String nullSafe(String value) {
    return value == null ? "" : value;
  }
}
