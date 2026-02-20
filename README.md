# Rescue Cell Planner (Training)

A Java 21 + JavaFX desktop training tool that guides a 10-person rescue cell through a compressed intel-to-ops planning cycle. The app focuses on decision-support, governance, uncertainty, and simulation-driven robustness testing (no procedural rescue tactics).

## Requirements
- Java 17
- Gradle 8+ (or use your own wrapper)

## Run
```bash
gradle run
```

## Tests
```bash
gradle test
```

## Build Rescue Packet Exports
Use the app toolbar:
- `Export HTML` or `Export PDF`

Sample export included in `sample/RescuePacket-Avalanche.html`.

## Optional Simulation Worker (Local Distribution)
A minimal HTTP worker is included for distributed simulation batches.

Run a worker on port 8090:
```bash
java -cp build/classes/java/main:build/resources/main com.rescueplanner.worker.WorkerMain 8090
```

The controller can be pointed to one or more worker URLs (see the Simulation dialog in-app).

## Notes
- All data is local-only JSON.
- The app ships with a prefilled training scenario: "Avalanche Aftermath: Missing Hiker Recovery (Training Case)".
- This is a training environment and should not be used for real-world operations.
