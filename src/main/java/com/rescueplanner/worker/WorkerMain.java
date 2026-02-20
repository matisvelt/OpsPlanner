package com.rescueplanner.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rescueplanner.sim.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class WorkerMain {
  public static void main(String[] args) throws IOException {
    int port = 8080;
    if (args.length > 0) {
      try {
        port = Integer.parseInt(args[0]);
      } catch (NumberFormatException ignored) {
        port = 8080;
      }
    }

    ObjectMapper mapper = new ObjectMapper();
    SimulationEngine engine = new SimulationEngine();

    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
    server.createContext("/simulate", exchange -> handleSimulation(exchange, mapper, engine));
    server.start();
    System.out.println("Simulation worker listening on port " + port);
  }

  private static void handleSimulation(HttpExchange exchange, ObjectMapper mapper, SimulationEngine engine) throws IOException {
    if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
      exchange.sendResponseHeaders(405, -1);
      return;
    }

    WorkerRequest request;
    try (InputStream input = exchange.getRequestBody()) {
      request = mapper.readValue(input, WorkerRequest.class);
    } catch (Exception ex) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }

    SimulationConfig config = request.getConfig();
    if (config == null) {
      config = new SimulationConfig();
    }
    config.setRuns(request.getRuns());
    config.setSeed(request.getSeed());

    SimulationResult result = engine.runSimulation(request.getInputs(), config);
    WorkerResponse response = new WorkerResponse(result);
    byte[] payload = mapper.writeValueAsBytes(response);

    exchange.getResponseHeaders().add("Content-Type", "application/json");
    exchange.sendResponseHeaders(200, payload.length);
    try (OutputStream output = exchange.getResponseBody()) {
      output.write(payload);
    }
  }
}
