import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TelemetryData {
    private Date timestamp;
    private String timestampString;
    private double value; 
    private String component;
    private double redHighLimit; 
    private double redLowLimit; 

    public TelemetryData(String timestampString, double value, String component, double redHighLimit, double redLowLimit) {
        this.timestampString = timestampString;
        this.value = value;
        this.component = component;
        this.redHighLimit = redHighLimit;
        this.redLowLimit = redLowLimit;
        try {
            this.timestamp = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS").parse(timestampString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public double getValue() {
        return value;
    }

    public String getComponent() {
        return component;
    }

    public double getRedHighLimit() {
        return redHighLimit;
    }

    public double getRedLowLimit() {
        return redLowLimit;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getTimestampString() {
        return timestampString;
    }
}
