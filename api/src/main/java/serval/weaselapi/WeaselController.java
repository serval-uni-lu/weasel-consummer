package serval.weaselapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfa.timeseries.TimeSeries;

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
    public ResponseEntity<double[][]> predict(@RequestBody TimeSeriesDTO[] ts){
        double[][] results = new double[][]{{-1}};
        try {
            TimeSeries[] timeSeries = new TimeSeries[ts.length];
            int i = 0;

            for(TimeSeriesDTO timeSeriesDTO:ts){
                timeSeries[i] = timeSeriesDTO.getTimeSeries();
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

    @GetMapping("/train")
    public double[][] train(){
        try {
            return weaselService.train();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
            return new double[][]{{-1}};
        }
    }


}
