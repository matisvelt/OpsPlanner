package com.rescueplanner.service;

import com.rescueplanner.model.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RescuePacketExporter {
  public void exportHtml(Path path, Scenario scenario) throws IOException {
    String html = toHtml(scenario);
    Files.write(path, html.getBytes(StandardCharsets.UTF_8));
  }

  public void exportPdf(Path path, Scenario scenario) throws IOException {
    List<String> lines = toPlainTextLines(scenario);
    try (PDDocument doc = new PDDocument()) {
      PDPage page = new PDPage(PDRectangle.LETTER);
      doc.addPage(page);
      PDPageContentStream content = new PDPageContentStream(doc, page);
      content.setFont(PDType1Font.HELVETICA, 10);
      content.beginText();
      float margin = 40;
      float yStart = page.getMediaBox().getHeight() - margin;
      float y = yStart;
      float leading = 12;
      content.newLineAtOffset(margin, y);

      for (String line : wrapLines(lines, 95)) {
        if (y < margin) {
          content.endText();
          content.close();
          page = new PDPage(PDRectangle.LETTER);
          doc.addPage(page);
          content = new PDPageContentStream(doc, page);
          content.setFont(PDType1Font.HELVETICA, 10);
          content.beginText();
          y = yStart;
          content.newLineAtOffset(margin, y);
        }
        content.showText(line);
        content.newLineAtOffset(0, -leading);
        y -= leading;
      }

      content.endText();
      content.close();
      doc.save(path.toFile());
    }
  }

  public String toHtml(Scenario scenario) {
    StringBuilder sb = new StringBuilder();
    sb.append("<!DOCTYPE html><html><head><meta charset='utf-8'>");
    sb.append("<title>Rescue Packet</title>");
    sb.append("<style>body{font-family:Arial,sans-serif;background:#f7f7f7;color:#1b1b1b;padding:24px;}\n");
    sb.append(".banner{background:#1b2a41;color:#fff;padding:8px 12px;font-weight:700;}\n");
    sb.append(".section{background:#fff;padding:16px;margin:16px 0;border-radius:6px;box-shadow:0 1px 3px rgba(0,0,0,0.08);}\n");
    sb.append("h2{margin:0 0 8px 0;font-size:18px;}\n");
    sb.append("pre{white-space:pre-wrap;font-family:inherit;}\n");
    sb.append("</style></head><body>");
    sb.append("<div class='banner'>").append(escapeHtml(scenario.getClassificationBanner())).append("</div>");
    sb.append("<h1>Rescue Packet</h1>");
    sb.append("<div class='section'><h2>Scenario</h2><pre>")
      .append(escapeHtml(scenario.getTitle()))
      .append("</pre></div>");

    addSection(sb, "1) Incident Input", scenario.getIncidentInput().getDraft());
    addSection(sb, "2) Constraints & Authorities", scenario.getConstraintsAuthorities().getDraft());
    addSection(sb, "3) Information Requirements", scenario.getInformationRequirements().getDraft());
    addSection(sb, "4) Environment/System Model", scenario.getEnvironmentModel().getDraft());
    addSection(sb, "5) Risk Register + Assumptions", scenario.getRiskRegister().getDraft());
    addSection(sb, "6) COA Builder", scenario.getCoaSet().getDraft());
    addSection(sb, "7) Rehearsal / Branch-Sequel", scenario.getRehearsalPlan().getDraft());
    addSection(sb, "8) Decision & Safety Gates", scenario.getDecisionGates().getDraft());
    addSection(sb, "9) Team Responsibilities", scenario.getTeamResponsibilities().getDraft());
    addSection(sb, "10) Mission Statement + CONOPS", scenario.getMissionConops().getDraft());
    addSection(sb, "11) Assessment & Feedback Loop", scenario.getAssessmentFeedback().getDraft());

    sb.append("</body></html>");
    return sb.toString();
  }

  private void addSection(StringBuilder sb, String title, String body) {
    sb.append("<div class='section'><h2>").append(escapeHtml(title)).append("</h2><pre>");
    sb.append(escapeHtml(body == null ? "" : body));
    sb.append("</pre></div>");
  }

  private List<String> toPlainTextLines(Scenario scenario) {
    List<String> lines = new ArrayList<>();
    lines.add(scenario.getClassificationBanner());
    lines.add("Rescue Packet");
    lines.add("Scenario: " + nullSafe(scenario.getTitle()));
    addLines(lines, "1) Incident Input", scenario.getIncidentInput().getDraft());
    addLines(lines, "2) Constraints & Authorities", scenario.getConstraintsAuthorities().getDraft());
    addLines(lines, "3) Information Requirements", scenario.getInformationRequirements().getDraft());
    addLines(lines, "4) Environment/System Model", scenario.getEnvironmentModel().getDraft());
    addLines(lines, "5) Risk Register + Assumptions", scenario.getRiskRegister().getDraft());
    addLines(lines, "6) COA Builder", scenario.getCoaSet().getDraft());
    addLines(lines, "7) Rehearsal / Branch-Sequel", scenario.getRehearsalPlan().getDraft());
    addLines(lines, "8) Decision & Safety Gates", scenario.getDecisionGates().getDraft());
    addLines(lines, "9) Team Responsibilities", scenario.getTeamResponsibilities().getDraft());
    addLines(lines, "10) Mission Statement + CONOPS", scenario.getMissionConops().getDraft());
    addLines(lines, "11) Assessment & Feedback Loop", scenario.getAssessmentFeedback().getDraft());
    return lines;
  }

  private void addLines(List<String> lines, String title, String body) {
    lines.add("\n" + title);
    if (body != null) {
      for (String line : body.split("\\r?\\n")) {
        lines.add(line);
      }
    }
  }

  private List<String> wrapLines(List<String> lines, int max) {
    List<String> wrapped = new ArrayList<>();
    for (String line : lines) {
      if (line.length() <= max) {
        wrapped.add(line);
        continue;
      }
      String remaining = line;
      while (remaining.length() > max) {
        int breakAt = remaining.lastIndexOf(' ', max);
        if (breakAt < 0) {
          breakAt = max;
        }
        wrapped.add(remaining.substring(0, breakAt));
        remaining = remaining.substring(Math.min(breakAt + 1, remaining.length()));
      }
      if (!remaining.isEmpty()) {
        wrapped.add(remaining);
      }
    }
    return wrapped;
  }

  private String escapeHtml(String input) {
    if (input == null) {
      return "";
    }
    return input.replace("&", "&amp;")
      .replace("<", "&lt;")
      .replace(">", "&gt;");
  }

  private String nullSafe(String value) {
    return value == null ? "" : value;
  }
}
