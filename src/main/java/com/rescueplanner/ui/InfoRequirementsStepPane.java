package com.rescueplanner.ui;

import com.rescueplanner.model.InfoRequirement;
import com.rescueplanner.model.InformationRequirements;
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

public class InfoRequirementsStepPane extends AbstractStepPane {
  private final TableView<InfoRequirement> table = new TableView<>();
  private final ObservableList<InfoRequirement> items = FXCollections.observableArrayList();
  private final TextArea derivedNeedsArea = new TextArea();

  public InfoRequirementsStepPane() {
    super("3", "3) Information Requirements + Indicators");
    table.setEditable(true);
    derivedNeedsArea.setPrefRowCount(3);
    buildLayout(buildContent());
  }

  @Override
  protected Node buildContent() {
    TableColumn<InfoRequirement, String> requirementCol = new TableColumn<>("Requirement");
    requirementCol.setCellValueFactory(new PropertyValueFactory<>("requirement"));
    requirementCol.setCellFactory(TextFieldTableCell.forTableColumn());
    requirementCol.setPrefWidth(220);

    TableColumn<InfoRequirement, String> indicatorCol = new TableColumn<>("Indicator");
    indicatorCol.setCellValueFactory(new PropertyValueFactory<>("indicator"));
    indicatorCol.setCellFactory(TextFieldTableCell.forTableColumn());
    indicatorCol.setPrefWidth(220);

    TableColumn<InfoRequirement, String> priorityCol = new TableColumn<>("Priority");
    priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
    priorityCol.setCellFactory(TextFieldTableCell.forTableColumn());
    priorityCol.setPrefWidth(100);

    TableColumn<InfoRequirement, String> statusCol = new TableColumn<>("Status");
    statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    statusCol.setCellFactory(TextFieldTableCell.forTableColumn());
    statusCol.setPrefWidth(100);

    table.getColumns().addAll(requirementCol, indicatorCol, priorityCol, statusCol);
    table.setItems(items);
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    Button addButton = new Button("Add");
    addButton.setOnAction(FxUtil.debugAction("Info Requirements - Add", () ->
      items.add(new InfoRequirement("New requirement", "Indicator", "Medium", "Open"))));
    Button removeButton = new Button("Remove");
    removeButton.setOnAction(FxUtil.debugAction("Info Requirements - Remove", () -> {
      InfoRequirement selected = table.getSelectionModel().getSelectedItem();
      if (selected != null) {
        items.remove(selected);
      }
    }));

    HBox buttons = new HBox(8, addButton, removeButton);

    VBox box = new VBox(10, table, buttons, new javafx.scene.control.Label("Derived Needs"), derivedNeedsArea);
    return box;
  }

  @Override
  protected TraceabilityRecord onGenerateDraft() {
    DraftResult result = DraftGenerator.generateInfoRequirementsDraft(scenario);
    scenario.getInformationRequirements().setDraft(result.getDraft());
    draftArea.setText(result.getDraft());
    return result.getTraceability();
  }

  @Override
  public void refresh() {
    InformationRequirements info = scenario.getInformationRequirements();
    items.setAll(info.getRequirements());
    derivedNeedsArea.setText(nullSafe(info.getDerivedNeeds()));
    draftArea.setText(nullSafe(info.getDraft()));
  }

  @Override
  public void saveToScenario() {
    InformationRequirements info = scenario.getInformationRequirements();
    info.setRequirements(new ArrayList<>(items));
    info.setDerivedNeeds(derivedNeedsArea.getText());
    info.setDraft(draftArea.getText());
  }

  private String nullSafe(String value) {
    return value == null ? "" : value;
  }
}
