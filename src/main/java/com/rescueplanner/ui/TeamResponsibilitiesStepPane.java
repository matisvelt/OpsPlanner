package com.rescueplanner.ui;

import com.rescueplanner.model.*;
import com.rescueplanner.service.DraftGenerator;
import com.rescueplanner.service.DraftResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class TeamResponsibilitiesStepPane extends AbstractStepPane {
  private final TableView<RaciEntry> table = new TableView<>();
  private final ObservableList<RaciEntry> items = FXCollections.observableArrayList();
  private final TextArea notesArea = new TextArea();

  public TeamResponsibilitiesStepPane() {
    super("9", "9) Team Responsibilities");
    table.setEditable(true);
    notesArea.setPrefRowCount(3);
    buildLayout(buildContent());
  }

  @Override
  protected Node buildContent() {
    TableColumn<RaciEntry, String> taskCol = new TableColumn<>("Task");
    taskCol.setCellValueFactory(new PropertyValueFactory<>("task"));
    taskCol.setCellFactory(TextFieldTableCell.forTableColumn());

    TableColumn<RaciEntry, String> roleCol = new TableColumn<>("Role");
    roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
    roleCol.setCellFactory(TextFieldTableCell.forTableColumn());

    TableColumn<RaciEntry, ResponsibilityType> respCol = new TableColumn<>("R/A/C/I");
    respCol.setCellValueFactory(new PropertyValueFactory<>("responsibility"));
    respCol.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(ResponsibilityType.values())));

    TableColumn<RaciEntry, String> detailCol = new TableColumn<>("Details");
    detailCol.setCellValueFactory(new PropertyValueFactory<>("details"));
    detailCol.setCellFactory(TextFieldTableCell.forTableColumn());

    table.getColumns().addAll(taskCol, roleCol, respCol, detailCol);
    table.setItems(items);
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    Button addButton = new Button("Add Entry");
    addButton.setOnAction(FxUtil.debugAction("Team Responsibilities - Add Entry", () ->
      items.add(new RaciEntry("Task", "Role", ResponsibilityType.R, ""))));
    Button removeButton = new Button("Remove Entry");
    removeButton.setOnAction(FxUtil.debugAction("Team Responsibilities - Remove Entry", () -> {
      RaciEntry selected = table.getSelectionModel().getSelectedItem();
      if (selected != null) {
        items.remove(selected);
      }
    }));

    HBox buttons = new HBox(8, addButton, removeButton);

    VBox box = new VBox(10, table, buttons, new javafx.scene.control.Label("Notes"), notesArea);
    return box;
  }

  @Override
  protected TraceabilityRecord onGenerateDraft() {
    DraftResult result = DraftGenerator.generateTeamDraft(scenario);
    scenario.getTeamResponsibilities().setDraft(result.getDraft());
    draftArea.setText(result.getDraft());
    return result.getTraceability();
  }

  @Override
  public void refresh() {
    TeamResponsibilities team = scenario.getTeamResponsibilities();
    items.setAll(team.getRaci());
    notesArea.setText(nullSafe(team.getNotes()));
    draftArea.setText(nullSafe(team.getDraft()));
  }

  @Override
  public void saveToScenario() {
    TeamResponsibilities team = scenario.getTeamResponsibilities();
    team.setRaci(new ArrayList<>(items));
    team.setNotes(notesArea.getText());
    team.setDraft(draftArea.getText());
  }

  private String nullSafe(String value) {
    return value == null ? "" : value;
  }
}
