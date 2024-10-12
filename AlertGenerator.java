public class AlertGenerator {

    public static String generateAlert(int satelliteId, String severity, String component, String timestamp) {
        return String.format(
            "{\n  \"satelliteId\": %d,\n  \"severity\": \"%s\",\n  \"component\": \"%s\",\n  \"timestamp\": \"%s\"\n}",
            satelliteId, severity, component, timestamp
        );
    }
}
