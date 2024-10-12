/**
 * Utility class to generate alert messages for satellite telemetry violations.
 * This class formats the alert data as a JSON-like string.
 */
public class AlertGenerator {

    /**
     * Generates an alert message in a JSON-like format.
     *
     * @param satelliteId the ID of the satellite that triggered the alert
     * @param severity    the severity level of the alert ("RED LOW" or "RED HIGH")
     * @param component   the satellite component that triggered the alert (e.g., "BATT", "TSTAT")
     * @param timestamp   the timestamp of the first reading that triggered the alert
     * @return a formatted string representing the alert message in JSON format
     */
    public static String generateAlert(int satelliteId, String severity, String component, String timestamp) {
        return String.format(
            "{\n  \"satelliteId\": %d,\n  \"severity\": \"%s\",\n  \"component\": \"%s\",\n  \"timestamp\": \"%s\"\n}",
            satelliteId, severity, component, timestamp
        );
    }
}
