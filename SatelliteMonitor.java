import java.io.*;
import java.text.*;
import java.util.*;

public class SatelliteMonitor {
    private static final long FIVE_MINUTES_IN_MILLIS = 5 * 60 * 1000;

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

}