package com.rescueplanner.ui;

import com.rescueplanner.model.DecisionGates;
import com.rescueplanner.model.SafetyGate;
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

import java.util.ArrayList;

public class DecisionGatesStepPane extends AbstractStepPane {
  private final TableView<SafetyGate> table = new TableView<>();
  private final ObservableList<SafetyGate> items = FXCollections.observableArrayList();
  private final TextArea criteriaArea = new TextArea();

  public DecisionGatesStepPane() {
    super("8", "8) Decision & Safety Gates");
    table.setEditable(true);
    criteriaArea.setPrefRowCount(3);
  }

  @Override
  protected Node buildContent() {
    TableColumn<SafetyGate, String> nameCol = new TableColumn<>("Gate");
    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    nameCol.setCellFactory(TextFieldTableCell.forTableColumn());

    TableColumn<SafetyGate, String> thresholdCol = new TableColumn<>("Threshold");
    thresholdCol.setCellValueFactory(new PropertyValueFactory<>("threshold"));
    thresholdCol.setCellFactory(TextFieldTableCell.forTableColumn());

    TableColumn<SafetyGate, String> actionCol = new TableColumn<>("Action");
    actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
    actionCol.setCellFactory(TextFieldTableCell.forTableColumn());

    TableColumn<SafetyGate, String> ownerCol = new TableColumn<>("Owner");
    ownerCol.setCellValueFactory(new PropertyValueFactory<>("ownerRole"));
    ownerCol.setCellFactory(TextFieldTableCell.forTableColumn());

    table.getColumns().addAll(nameCol, thresholdCol, actionCol, ownerCol);
    table.setItems(items);
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    Button addButton = new Button("Add Gate");
    addButton.setOnAction(event -> items.add(new SafetyGate("New Gate", "Threshold", "Action", "Owner")));
    Button removeButton = new Button("Remove Gate");
    removeButton.setOnAction(event -> {
      SafetyGate selected = table.getSelectionModel().getSelectedItem();
      if (selected != null) {
        items.remove(selected);
      }
    });

    HBox buttons = new HBox(8, addButton, removeButton);

    VBox box = new VBox(10, table, buttons, new javafx.scene.control.Label("Stop/Go Criteria"), criteriaArea);
    return box;
  }

  @Override
  protected TraceabilityRecord onGenerateDraft() {
    DraftResult result = DraftGenerator.generateDecisionGatesDraft(scenario);
    scenario.getDecisionGates().setDraft(result.getDraft());
    draftArea.setText(result.getDraft());
    return result.getTraceability();
  }

  @Override
  public void refresh() {
    DecisionGates gates = scenario.getDecisionGates();
    items.setAll(gates.getGates());
    criteriaArea.setText(nullSafe(gates.getStopGoCriteria()));
    draftArea.setText(nullSafe(gates.getDraft()));
  }

  @Override
  public void saveToScenario() {
    DecisionGates gates = scenario.getDecisionGates();
    gates.setGates(new ArrayList<>(items));
    gates.setStopGoCriteria(criteriaArea.getText());
    gates.setDraft(draftArea.getText());
  }

  private String nullSafe(String value) {
    return value == null ? "" : value;
  }
}
