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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;

public class RiskStepPane extends AbstractStepPane {
  private final TableView<RiskItem> riskTable = new TableView<>();
  private final ObservableList<RiskItem> riskItems = FXCollections.observableArrayList();
  private final TableView<Assumption> assumptionTable = new TableView<>();
  private final ObservableList<Assumption> assumptions = FXCollections.observableArrayList();

  public RiskStepPane() {
    super("5", "5) Risk Register + Assumptions");
    riskTable.setEditable(true);
    assumptionTable.setEditable(true);
    buildLayout(buildContent());
  }

  @Override
  protected Node buildContent() {
    TableColumn<RiskItem, String> idCol = new TableColumn<>("ID");
    idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    idCol.setCellFactory(TextFieldTableCell.forTableColumn());

    TableColumn<RiskItem, String> descCol = new TableColumn<>("Description");
    descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    descCol.setCellFactory(TextFieldTableCell.forTableColumn());

    TableColumn<RiskItem, Integer> likelihoodCol = new TableColumn<>("L");
    likelihoodCol.setCellValueFactory(new PropertyValueFactory<>("likelihood"));
    likelihoodCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

    TableColumn<RiskItem, Integer> impactCol = new TableColumn<>("I");
    impactCol.setCellValueFactory(new PropertyValueFactory<>("impact"));
    impactCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

    TableColumn<RiskItem, String> mitigationCol = new TableColumn<>("Mitigation");
    mitigationCol.setCellValueFactory(new PropertyValueFactory<>("mitigation"));
    mitigationCol.setCellFactory(TextFieldTableCell.forTableColumn());

    TableColumn<RiskItem, String> ownerCol = new TableColumn<>("Owner");
    ownerCol.setCellValueFactory(new PropertyValueFactory<>("ownerRole"));
    ownerCol.setCellFactory(TextFieldTableCell.forTableColumn());

    riskTable.getColumns().addAll(idCol, descCol, likelihoodCol, impactCol, mitigationCol, ownerCol);
    riskTable.setItems(riskItems);
    riskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    Button addRisk = new Button("Add Risk");
    addRisk.setOnAction(FxUtil.debugAction("Risk Register - Add Risk", () ->
      riskItems.add(new RiskItem("R#", "New risk", 3, 3, "Mitigation", "Owner"))));
    Button removeRisk = new Button("Remove Risk");
    removeRisk.setOnAction(FxUtil.debugAction("Risk Register - Remove Risk", () -> {
      RiskItem selected = riskTable.getSelectionModel().getSelectedItem();
      if (selected != null) {
        riskItems.remove(selected);
      }
    }));

    HBox riskButtons = new HBox(8, addRisk, removeRisk);

    TableColumn<Assumption, String> statementCol = new TableColumn<>("Assumption");
    statementCol.setCellValueFactory(new PropertyValueFactory<>("statement"));
    statementCol.setCellFactory(TextFieldTableCell.forTableColumn());

    TableColumn<Assumption, ConfidenceLevel> confidenceCol = new TableColumn<>("Confidence");
    confidenceCol.setCellValueFactory(new PropertyValueFactory<>("confidence"));
    confidenceCol.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(ConfidenceLevel.values())));

    TableColumn<Assumption, FragilityTag> fragilityCol = new TableColumn<>("Fragility");
    fragilityCol.setCellValueFactory(new PropertyValueFactory<>("fragility"));
    fragilityCol.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(FragilityTag.values())));

    assumptionTable.getColumns().addAll(statementCol, confidenceCol, fragilityCol);
    assumptionTable.setItems(assumptions);
    assumptionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    Button addAssumption = new Button("Add Assumption");
    addAssumption.setOnAction(FxUtil.debugAction("Risk Register - Add Assumption", () ->
      assumptions.add(new Assumption("New assumption", ConfidenceLevel.MEDIUM, FragilityTag.FRAGILE))));
    Button removeAssumption = new Button("Remove Assumption");
    removeAssumption.setOnAction(FxUtil.debugAction("Risk Register - Remove Assumption", () -> {
      Assumption selected = assumptionTable.getSelectionModel().getSelectedItem();
      if (selected != null) {
        assumptions.remove(selected);
      }
    }));

    HBox assumptionButtons = new HBox(8, addAssumption, removeAssumption);

    VBox box = new VBox(10,
      new javafx.scene.control.Label("Risks"), riskTable, riskButtons,
      new javafx.scene.control.Label("Assumptions"), assumptionTable, assumptionButtons);
    return box;
  }

  @Override
  protected TraceabilityRecord onGenerateDraft() {
    DraftResult result = DraftGenerator.generateRiskDraft(scenario);
    scenario.getRiskRegister().setDraft(result.getDraft());
    draftArea.setText(result.getDraft());
    return result.getTraceability();
  }

  @Override
  public void refresh() {
    RiskRegister register = scenario.getRiskRegister();
    riskItems.setAll(register.getRisks());
    assumptions.setAll(register.getAssumptions());
    draftArea.setText(nullSafe(register.getDraft()));
  }

  @Override
  public void saveToScenario() {
    RiskRegister register = scenario.getRiskRegister();
    register.setRisks(new ArrayList<>(riskItems));
    register.setAssumptions(new ArrayList<>(assumptions));
    register.setDraft(draftArea.getText());
  }

  private String nullSafe(String value) {
    return value == null ? "" : value;
  }
}
