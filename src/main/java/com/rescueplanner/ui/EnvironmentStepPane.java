package com.rescueplanner.ui;

import com.rescueplanner.model.Dependency;
import com.rescueplanner.model.EnvironmentModel;
import com.rescueplanner.model.Sector;
import com.rescueplanner.model.TraceabilityRecord;
import com.rescueplanner.service.DraftGenerator;
import com.rescueplanner.service.DraftResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnvironmentStepPane extends AbstractStepPane {
  private final TableView<Sector> table = new TableView<>();
  private final ObservableList<Sector> items = FXCollections.observableArrayList();
  private final TextArea dependenciesArea = new TextArea();
  private final TextArea uncertaintyArea = new TextArea();

  public EnvironmentStepPane() {
    super("4", "4) Environment/System Model");
    table.setEditable(true);
    dependenciesArea.setPrefRowCount(3);
    uncertaintyArea.setPrefRowCount(3);
    buildLayout(buildContent());
  }

  @Override
  protected Node buildContent() {
    TableColumn<Sector, String> nameCol = new TableColumn<>("Sector");
    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    nameCol.setCellFactory(TextFieldTableCell.forTableColumn());

    TableColumn<Sector, String> sizeCol = new TableColumn<>("Size");
    sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
    sizeCol.setCellFactory(TextFieldTableCell.forTableColumn());

    TableColumn<Sector, Integer> accessCol = new TableColumn<>("Access");
    accessCol.setCellValueFactory(new PropertyValueFactory<>("accessDifficultyIndex"));
    accessCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

    TableColumn<Sector, Integer> hazardCol = new TableColumn<>("Hazard");
    hazardCol.setCellValueFactory(new PropertyValueFactory<>("avalancheHazardIndex"));
    hazardCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

    TableColumn<Sector, Integer> visibilityCol = new TableColumn<>("Visibility");
    visibilityCol.setCellValueFactory(new PropertyValueFactory<>("visibilityIndex"));
    visibilityCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

    TableColumn<Sector, Double> commsCol = new TableColumn<>("Comms");
    commsCol.setCellValueFactory(new PropertyValueFactory<>("commsReliability"));
    commsCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

    TableColumn<Sector, Double> timeMinCol = new TableColumn<>("Time Min");
    timeMinCol.setCellValueFactory(new PropertyValueFactory<>("timeToSearchMinHours"));
    timeMinCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

    TableColumn<Sector, Double> timeMaxCol = new TableColumn<>("Time Max");
    timeMaxCol.setCellValueFactory(new PropertyValueFactory<>("timeToSearchMaxHours"));
    timeMaxCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

    TableColumn<Sector, Double> weightCol = new TableColumn<>("Weight");
    weightCol.setCellValueFactory(new PropertyValueFactory<>("probabilityWeight"));
    weightCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

    table.getColumns().addAll(nameCol, sizeCol, accessCol, hazardCol, visibilityCol, commsCol, timeMinCol, timeMaxCol, weightCol);
    table.setItems(items);
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    Button addButton = new Button("Add");
    addButton.setOnAction(FxUtil.debugAction("Environment - Add Sector", () ->
      items.add(new Sector("New"))));
    Button removeButton = new Button("Remove");
    removeButton.setOnAction(FxUtil.debugAction("Environment - Remove Sector", () -> {
      Sector selected = table.getSelectionModel().getSelectedItem();
      if (selected != null) {
        items.remove(selected);
      }
    }));

    HBox buttons = new HBox(8, addButton, removeButton);

    VBox box = new VBox(10, table, buttons,
      new javafx.scene.control.Label("Dependencies (one per line: prerequisite -> dependent)"), dependenciesArea,
      new javafx.scene.control.Label("Uncertainty Map"), uncertaintyArea);
    return box;
  }

  @Override
  protected TraceabilityRecord onGenerateDraft() {
    DraftResult result = DraftGenerator.generateEnvironmentDraft(scenario);
    scenario.getEnvironmentModel().setDraft(result.getDraft());
    draftArea.setText(result.getDraft());
    return result.getTraceability();
  }

  @Override
  public void refresh() {
    EnvironmentModel env = scenario.getEnvironmentModel();
    items.setAll(env.getSectors());
    dependenciesArea.setText(env.getDependencies().stream()
      .map(dep -> dep.getPrerequisite() + " -> " + dep.getDependent())
      .collect(Collectors.joining("\n")));
    uncertaintyArea.setText(nullSafe(env.getUncertaintyMap()));
    draftArea.setText(nullSafe(env.getDraft()));
  }

  @Override
  public void saveToScenario() {
    EnvironmentModel env = scenario.getEnvironmentModel();
    env.setSectors(new ArrayList<>(items));
    env.setDependencies(parseDependencies(dependenciesArea.getText()));
    env.setUncertaintyMap(uncertaintyArea.getText());
    env.setDraft(draftArea.getText());
  }

  private List<Dependency> parseDependencies(String text) {
    List<Dependency> deps = new ArrayList<>();
    if (text == null || text.isBlank()) {
      return deps;
    }
    String[] lines = text.split("\\r?\\n");
    for (String line : lines) {
      String trimmed = line.trim();
      if (trimmed.isEmpty()) {
        continue;
      }
      if (trimmed.contains("->")) {
        String[] parts = trimmed.split("->", 2);
        deps.add(new Dependency(parts[0].trim(), parts[1].trim()));
      } else {
        deps.add(new Dependency(trimmed, "Dependent"));
      }
    }
    return deps;
  }

  private String nullSafe(String value) {
    return value == null ? "" : value;
  }
}
