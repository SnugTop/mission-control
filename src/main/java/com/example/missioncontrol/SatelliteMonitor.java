package com.example.missioncontrol;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The SatelliteMonitor class reads telemetry data from satellites,
 * checks for violations based on thresholds (Red Low/High limits), and generates alerts.
 * Violations are determined based on consecutive readings within a 5-minute window.
 */
public class SatelliteMonitor {

    private static final long FIVE_MINUTES_IN_MILLIS = 5 * 60 * 1000; // 5 minutes in milliseconds to check for violations

    /**
     * The main method that reads telemetry data from a file, processes it,
     * and checks for violations.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try (InputStream in = SatelliteMonitor.class.getClassLoader()
                .getResourceAsStream("TelemetryData.txt")) {

            if (in == null) {
                System.err.println("TelemetryData.txt not found on classpath");
                return;
            }

            try (Scanner scanner = new Scanner(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                Map<Integer, List<TelemetryData>> telemetryDataMap = new HashMap<>();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    processTelemetryData(line, telemetryDataMap);
                }
                for (int satelliteId : telemetryDataMap.keySet()) {
                    List<TelemetryData> readings = telemetryDataMap.get(satelliteId);
                    checkForViolations(readings, satelliteId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes a line of telemetry data and stores the information in the telemetry data map.
     *
     * @param line               the line of telemetry data to process
     * @param telemetryDataMap    the map where telemetry data is stored, categorized by satellite ID
     */
    private static void processTelemetryData(String line, Map<Integer, List<TelemetryData>> telemetryDataMap) {
        String[] parts = line.split("\\|");

        String timestamp = parts[0];
        int satelliteId = Integer.parseInt(parts[1]);
        double redHighLimit = Double.parseDouble(parts[2]);
        double yellowHighLimit = Double.parseDouble(parts[3]); // Not used in violation checks
        double yellowLowLimit = Double.parseDouble(parts[4]);  // Not used in violation checks
        double redLowLimit = Double.parseDouble(parts[5]);
        double rawValue = Double.parseDouble(parts[6]);
        String component = parts[7];

        // Creating and storing the telemetry data in the map
        TelemetryData data = new TelemetryData(timestamp, rawValue, component, redHighLimit, redLowLimit);
        telemetryDataMap.computeIfAbsent(satelliteId, k -> new ArrayList<>()).add(data);
    }

    /**
     * Checks for violations by grouping the telemetry data by component and
     * determining if there are any red high/low violations.
     *
     * @param readings    the list of telemetry data readings for the satellite
     * @param satelliteId the satellite ID associated with these readings
     */
    private static void checkForViolations(List<TelemetryData> readings, int satelliteId) {
        Map<String, List<TelemetryData>> componentReadings = new HashMap<>();

        // Grouping telemetry data by component
        for (TelemetryData data : readings) {
            componentReadings.computeIfAbsent(data.getComponent(), k -> new ArrayList<>()).add(data);
        }

        // Checking for red low and red high violations for each component
        for (Map.Entry<String, List<TelemetryData>> entry : componentReadings.entrySet()) {
            String component = entry.getKey();
            List<TelemetryData> componentData = entry.getValue();
            checkForRedLowViolation(componentData, satelliteId, component);
            checkForRedHighViolation(componentData, satelliteId, component);
        }
    }

    /**
     * Checks for red low violations where three consecutive telemetry readings are below the red low limit
     * within a 5-minute window.
     *
     * @param readings    the list of telemetry data readings for the component
     * @param satelliteId the satellite ID associated with these readings
     * @param component   the component being checked
     */
    private static void checkForRedLowViolation(List<TelemetryData> readings, int satelliteId, String component) {
        if (readings.size() < 3) return;

        // Check consecutive readings for violation
        for (int i = 0; i <= readings.size() - 3; i++) {
            TelemetryData first = readings.get(i);
            TelemetryData second = readings.get(i + 1);
            TelemetryData third = readings.get(i + 2);

            if (third.getTimestamp().getTime() - first.getTimestamp().getTime() <= FIVE_MINUTES_IN_MILLIS &&
                first.getValue() < first.getRedLowLimit() && second.getValue() < second.getRedLowLimit() &&
                third.getValue() < third.getRedLowLimit()) {
                // Generate alert for red low violation
                System.out.println(AlertGenerator.generateAlert(satelliteId, "RED LOW", component, first.getTimestampString()));
            }
        }
    }

    /**
     * Checks for red high violations where three consecutive telemetry readings are above the red high limit
     * within a 5-minute window.
     *
     * @param readings    the list of telemetry data readings for the component
     * @param satelliteId the satellite ID associated with these readings
     * @param component   the component being checked
     */
    private static void checkForRedHighViolation(List<TelemetryData> readings, int satelliteId, String component) {
        if (readings.size() < 3) return;

        // Check consecutive readings for violation
        for (int i = 0; i <= readings.size() - 3; i++) {
            TelemetryData first = readings.get(i);
            TelemetryData second = readings.get(i + 1);
            TelemetryData third = readings.get(i + 2);

            if (third.getTimestamp().getTime() - first.getTimestamp().getTime() <= FIVE_MINUTES_IN_MILLIS &&
                first.getValue() > first.getRedHighLimit() && second.getValue() > second.getRedHighLimit() &&
                third.getValue() > third.getRedHighLimit()) {
                // Generate alert for red high violation
                System.out.println(AlertGenerator.generateAlert(satelliteId, "RED HIGH", component, first.getTimestampString()));
            }
        }
    }
}
