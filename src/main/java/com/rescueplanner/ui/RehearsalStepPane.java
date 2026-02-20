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
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class RehearsalStepPane extends AbstractStepPane {
  private final TableView<RehearsalEvent> table = new TableView<>();
  private final ObservableList<RehearsalEvent> items = FXCollections.observableArrayList();
  private final TextArea branchCardsArea = new TextArea();
  private final ContingencyMapView mapView = new ContingencyMapView();

  public RehearsalStepPane() {
    super("7", "7) Rehearsal / Branch-Sequel Playthrough");
    table.setEditable(true);
    branchCardsArea.setPrefRowCount(6);
  }

  @Override
  protected Node buildContent() {
    TableColumn<RehearsalEvent, String> nameCol = new TableColumn<>("Event");
    nameCol.setCellValueFactory(new PropertyValueFactory<>("eventName"));
    nameCol.setCellFactory(TextFieldTableCell.forTableColumn());

    TableColumn<RehearsalEvent, String> indicatorCol = new TableColumn<>("Indicator");
    indicatorCol.setCellValueFactory(new PropertyValueFactory<>("indicator"));
    indicatorCol.setCellFactory(TextFieldTableCell.forTableColumn());

    TableColumn<RehearsalEvent, String> triggerCol = new TableColumn<>("Trigger");
    triggerCol.setCellValueFactory(new PropertyValueFactory<>("triggerThreshold"));
    triggerCol.setCellFactory(TextFieldTableCell.forTableColumn());

    TableColumn<RehearsalEvent, DecisionAction> decisionCol = new TableColumn<>("Decision");
    decisionCol.setCellValueFactory(new PropertyValueFactory<>("decision"));
    decisionCol.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(DecisionAction.values())));

    TableColumn<RehearsalEvent, String> roleCol = new TableColumn<>("Role");
    roleCol.setCellValueFactory(new PropertyValueFactory<>("responsibleRole"));
    roleCol.setCellFactory(TextFieldTableCell.forTableColumn());

    TableColumn<RehearsalEvent, Double> timeCol = new TableColumn<>("Time Impact (hrs)");
    timeCol.setCellValueFactory(new PropertyValueFactory<>("timeImpactHours"));
    timeCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

    TableColumn<RehearsalEvent, Integer> riskCol = new TableColumn<>("Risk Impact");
    riskCol.setCellValueFactory(new PropertyValueFactory<>("riskImpact"));
    riskCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

    TableColumn<RehearsalEvent, String> gateCol = new TableColumn<>("Safety Gate");
    gateCol.setCellValueFactory(new PropertyValueFactory<>("safetyGateInvoked"));
    gateCol.setCellFactory(TextFieldTableCell.forTableColumn());

    table.getColumns().addAll(nameCol, indicatorCol, triggerCol, decisionCol, roleCol, timeCol, riskCol, gateCol);
    table.setItems(items);
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    Button addButton = new Button("Add Event");
    addButton.setOnAction(event -> items.add(new RehearsalEvent("E#", "New event")));
    Button removeButton = new Button("Remove Event");
    removeButton.setOnAction(event -> {
      RehearsalEvent selected = table.getSelectionModel().getSelectedItem();
      if (selected != null) {
        items.remove(selected);
      }
    });

    HBox buttons = new HBox(8, addButton, removeButton);

    VBox box = new VBox(10, table, buttons,
      new javafx.scene.control.Label("Branch Cards"), branchCardsArea,
      new javafx.scene.control.Label("Contingency Map"), mapView);
    return box;
  }

  @Override
  protected TraceabilityRecord onGenerateDraft() {
    DraftResult result = DraftGenerator.generateRehearsalDraft(scenario);
    scenario.getRehearsalPlan().setDraft(result.getDraft());
    branchCardsArea.setText(result.getDraft());
    draftArea.setText(result.getDraft());

    // Decision log entries for every rehearsal event
    List<DecisionLogEntry> logEntries = new ArrayList<>();
    for (RehearsalEvent event : scenario.getRehearsalPlan().getEvents()) {
      logEntries.add(new DecisionLogEntry(Instant.now(), event.getId(), event.getDecision(),
        "Generated during rehearsal draft"));
    }
    scenario.getRehearsalPlan().setDecisionLog(logEntries);

    mapView.update(scenario.getCoaSet().getCoas(), scenario.getRehearsalPlan().getEvents());
    return result.getTraceability();
  }

  @Override
  public void refresh() {
    RehearsalPlan plan = scenario.getRehearsalPlan();
    items.setAll(plan.getEvents());
    branchCardsArea.setText(nullSafe(plan.getDraft()));
    draftArea.setText(nullSafe(plan.getDraft()));
    mapView.update(scenario.getCoaSet().getCoas(), plan.getEvents());
  }

  @Override
  public void saveToScenario() {
    RehearsalPlan plan = scenario.getRehearsalPlan();
    plan.setEvents(new ArrayList<>(items));
    plan.setDraft(branchCardsArea.getText());
  }

  private String nullSafe(String value) {
    return value == null ? "" : value;
  }
}
