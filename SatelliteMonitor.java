import java.io.*;
import java.text.*;
import java.util.*;

public class SatelliteMonitor {
    private static final long FIVE_MINUTES_IN_MILLIS = 5 * 60 * 1000; // 5 minutes in milliseconds to check for violations

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(new File("TelemetryData.txt"));
            Map<Integer, List<TelemetryData>> telemetryDataMap = new HashMap<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                processTelemetryData(line, telemetryDataMap);
            }

            for (int satelliteId : telemetryDataMap.keySet()) {
                List<TelemetryData> readings = telemetryDataMap.get(satelliteId);
                checkForViolations(readings, satelliteId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processTelemetryData(String line, Map<Integer, List<TelemetryData>> telemetryDataMap) {
        String[] parts = line.split("\\|");
    
        String timestamp = parts[0];
        int satelliteId = Integer.parseInt(parts[1]);
        double redHighLimit = Double.parseDouble(parts[2]);
        double yellowHighLimit = Double.parseDouble(parts[3]);
        double yellowLowLimit = Double.parseDouble(parts[4]);
        double redLowLimit = Double.parseDouble(parts[5]);
        double rawValue = Double.parseDouble(parts[6]);
        String component = parts[7];
    
        
        TelemetryData data = new TelemetryData(timestamp, rawValue, component, redHighLimit, redLowLimit);
    
        
        telemetryDataMap.computeIfAbsent(satelliteId, k -> new ArrayList<>()).add(data);
    }

    private static void checkForViolations(List<TelemetryData> readings, int satelliteId) {
        Map<String, List<TelemetryData>> componentReadings = new HashMap<>();
        for (TelemetryData data : readings) {
            componentReadings.computeIfAbsent(data.getComponent(), k -> new ArrayList<>()).add(data);
        }

        for (Map.Entry<String, List<TelemetryData>> entry : componentReadings.entrySet()) {
            String component = entry.getKey();
            List<TelemetryData> componentData = entry.getValue();
            checkForRedLowViolation(componentData, satelliteId, component);
            checkForRedHighViolation(componentData, satelliteId, component);
        }
    }

    private static void checkForRedLowViolation(List<TelemetryData> readings, int satelliteId, String component) {
        if (readings.size() < 3) return;

        for (int i = 0; i <= readings.size() - 3; i++) {
            TelemetryData first = readings.get(i);
            TelemetryData second = readings.get(i + 1);
            TelemetryData third = readings.get(i + 2);

            if (third.getTimestamp().getTime() - first.getTimestamp().getTime() <= FIVE_MINUTES_IN_MILLIS &&
                first.getValue() < first.getRedLowLimit() && second.getValue() < second.getRedLowLimit() &&
                third.getValue() < third.getRedLowLimit()) {
                System.out.println(AlertGenerator.generateAlert(satelliteId, "RED LOW", component, first.getTimestampString()));
            }
        }
    }

    private static void checkForRedHighViolation(List<TelemetryData> readings, int satelliteId, String component) {
        if (readings.size() < 3) return;

        for (int i = 0; i <= readings.size() - 3; i++) {
            TelemetryData first = readings.get(i);
            TelemetryData second = readings.get(i + 1);
            TelemetryData third = readings.get(i + 2);

            if (third.getTimestamp().getTime() - first.getTimestamp().getTime() <= FIVE_MINUTES_IN_MILLIS &&
                first.getValue() > first.getRedHighLimit() && second.getValue() > second.getRedHighLimit() &&
                third.getValue() > third.getRedHighLimit()) {
                System.out.println(AlertGenerator.generateAlert(satelliteId, "RED HIGH", component, first.getTimestampString()));
            }
        }
    }
}
