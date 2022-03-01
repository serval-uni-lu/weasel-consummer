package serval.weaselapi;

import sfa.timeseries.TimeSeries;

import java.util.Arrays;

public class TimeSeriesDTO {
    double timestamp;
    String values;

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    //Required to convert request input to Weasel TimeSerie model
    public TimeSeries getTimeSeries(){
        return new TimeSeries(
                Arrays.stream(this.values.split("\\s"))
                        .mapToDouble(Double::valueOf)
                        .toArray());
    }
}
