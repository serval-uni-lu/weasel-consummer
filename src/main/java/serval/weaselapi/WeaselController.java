package serval.weaselapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sfa.classification.Classifier;
import sfa.timeseries.TimeSeries;

import java.io.FileNotFoundException;
import java.util.Arrays;

@RestController
@RequestMapping("/model")
public class WeaselController {
    Logger logger = LoggerFactory.getLogger(WeaselController.class);
    final
    WeaselService weaselService;

    public WeaselController(WeaselService weaselService) {
        this.weaselService = weaselService;
    }

    @PostMapping("/predict")
    public ResponseEntity<Double[]> predict(@RequestBody TimeSeriesMojo[] ts){
        Double[] results = new Double[0];
        try {
            TimeSeries[] timeSeries = new TimeSeries[ts.length];
            int i = 0;
            //Required to convert request input to Weasel TimeSerie model
            for(TimeSeriesMojo timeSeriesMojo:ts){
                timeSeries[i] = new TimeSeries(
                        Arrays.stream(timeSeriesMojo.getValues().split("\\s"))
                                .mapToDouble(Double::valueOf)
                                .toArray());
                timeSeries[i].norm();
                i++;
            }
            results = weaselService.getPredictions(timeSeries);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
            return ResponseEntity.internalServerError().body(results);
        }
        return ResponseEntity.ok(results);
    }


}
