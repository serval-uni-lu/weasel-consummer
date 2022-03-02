package serval.weaselapi;

import sfa.timeseries.TimeSeries;

import java.util.Arrays;

public class TimeSeriesDTO {
    double timestamp;
    double[] values;

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    //Required to convert request input to Weasel TimeSerie model
    public TimeSeries getTimeSeries(){
        return new TimeSeries(
                values);
    }

}
