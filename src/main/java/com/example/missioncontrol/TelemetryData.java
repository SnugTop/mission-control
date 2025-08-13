package com.example.missioncontrol;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The TelemetryData class represents a single telemetry data reading from a satellite.
 * It stores information such as the timestamp, value of the reading, component type, and alert limits.
 */
public class TelemetryData {

    private Date timestamp;
    private String timestampString;
    private double value;
    private String component;
    private double redHighLimit;
    private double redLowLimit;

    /**
     * Constructor for creating a TelemetryData object.
     * Converts the timestamp string into a Date object for time-based comparisons.
     *
     * @param timestampString the timestamp of the reading as a string in the format "yyyyMMdd HH:mm:ss.SSS"
     * @param value           the actual telemetry reading value (e.g., battery voltage, temperature)
     * @param component       the component being monitored (e.g., "BATT" for battery, "TSTAT" for thermostat)
     * @param redHighLimit    the threshold above which a "RED HIGH" alert is triggered
     * @param redLowLimit     the threshold below which a "RED LOW" alert is triggered
     */
    public TelemetryData(String timestampString, double value, String component, double redHighLimit, double redLowLimit) {
        this.timestampString = timestampString;
        this.value = value;
        this.component = component;
        this.redHighLimit = redHighLimit;
        this.redLowLimit = redLowLimit;
        try {
            // Parse the timestamp string to a Date object
            this.timestamp = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS").parse(timestampString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the telemetry reading value.
     *
     * @return the telemetry reading value
     */
    public double getValue() {
        return value;
    }

    /**
     * Gets the component type (e.g., "BATT" or "TSTAT").
     *
     * @return the component being monitored
     */
    public String getComponent() {
        return component;
    }

    /**
     * Gets the red high limit threshold.
     * A reading above this limit triggers a "RED HIGH" alert.
     *
     * @return the red high limit
     */
    public double getRedHighLimit() {
        return redHighLimit;
    }

    /**
     * Gets the red low limit threshold.
     * A reading below this limit triggers a "RED LOW" alert.
     *
     * @return the red low limit
     */
    public double getRedLowLimit() {
        return redLowLimit;
    }

    /**
     * Gets the timestamp as a Date object.
     *
     * @return the timestamp of the telemetry reading
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the original timestamp as a string.
     *
     * @return the timestamp of the telemetry reading as a string
     */
    public String getTimestampString() {
        return timestampString;
    }
}
