package com.rescueplanner.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class FxUtil {
  private FxUtil() {
  }

  public static GridPane createFormGrid() {
    GridPane grid = new GridPane();
    grid.setHgap(12);
    grid.setVgap(10);
    grid.setPadding(new Insets(6, 0, 6, 0));
    return grid;
  }

  public static void addRow(GridPane grid, int row, String label, Node field) {
    Label lbl = new Label(label);
    lbl.getStyleClass().add("form-label");
    grid.add(lbl, 0, row);
    grid.add(field, 1, row);
  }

  public static EventHandler<ActionEvent> debugAction(String name, Runnable action) {
    return event -> {
      System.out.println("[UI] " + name);
      try {
        action.run();
      } catch (Exception ex) {
        ex.printStackTrace();
        String message = ex.getMessage();
        if (message == null || message.isBlank()) {
          message = ex.getClass().getSimpleName();
        }
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Action failed");
        alert.setHeaderText(name);
        alert.showAndWait();
      }
    };
  }
}
