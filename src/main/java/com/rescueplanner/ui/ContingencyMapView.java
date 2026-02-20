package com.rescueplanner.ui;

import com.rescueplanner.model.COA;
import com.rescueplanner.model.RehearsalEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

public class ContingencyMapView extends VBox {
  private final Canvas canvas = new Canvas(520, 320);

  public ContingencyMapView() {
    getChildren().add(canvas);
  }

  public void update(List<COA> coas, List<RehearsalEvent> events) {
    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.setFill(Color.web("#f1f4f6"));
    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

    gc.setStroke(Color.web("#7a8794"));
    gc.setFill(Color.web("#1b2a41"));
    gc.setLineWidth(1.2);

    double leftX = 20;
    double midX = 210;
    double rightX = 400;
    double rowHeight = 28;

    int maxRows = Math.max(coas.size(), events.size());
    if (maxRows == 0) {
      gc.setFill(Color.web("#4a5568"));
      gc.fillText("No events configured", 20, 30);
      return;
    }

    for (int i = 0; i < coas.size(); i++) {
      double y = 30 + i * rowHeight;
      drawNode(gc, leftX, y, 160, 20, coas.get(i).getId());
    }

    for (int i = 0; i < events.size(); i++) {
      double y = 30 + i * rowHeight;
      drawNode(gc, midX, y, 170, 20, events.get(i).getId());
    }

    for (int i = 0; i < events.size(); i++) {
      double y = 30 + i * rowHeight;
      String decision = events.get(i).getDecision() == null ? "Decision" : events.get(i).getDecision().name();
      drawNode(gc, rightX, y, 100, 20, decision);
    }

    for (int i = 0; i < Math.min(coas.size(), events.size()); i++) {
      double y = 40 + i * rowHeight;
      gc.setStroke(Color.web("#9aa5b1"));
      gc.strokeLine(leftX + 160, y, midX, y);
      gc.strokeLine(midX + 170, y, rightX, y);
    }
  }

  private void drawNode(GraphicsContext gc, double x, double y, double w, double h, String text) {
    gc.setFill(Color.web("#ffffff"));
    gc.fillRoundRect(x, y, w, h, 6, 6);
    gc.setStroke(Color.web("#1b2a41"));
    gc.strokeRoundRect(x, y, w, h, 6, 6);
    gc.setFill(Color.web("#1b2a41"));
    gc.fillText(text == null ? "" : text, x + 6, y + 14);
  }
}
