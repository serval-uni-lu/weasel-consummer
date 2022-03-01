package serval.weaselapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sfa.classification.Classifier;
import sfa.classification.WEASELClassifier;
import sfa.timeseries.TimeSeries;

import java.io.File;
import java.io.FileNotFoundException;

@Service
public class WeaselService {
    Logger logger = LoggerFactory.getLogger(WeaselService.class);

    @Value("${weasel.model.path}")
    String wclfPath;

    public Double[] getPredictions(TimeSeries[] ts) throws Exception {
        WEASELClassifier weaselLoader = new WEASELClassifier();
        weaselLoader.minF   = 4;	// represents the minimal length for training SFA words. default: 4.
        weaselLoader.maxF   = 6;	// represents the maximal length for training SFA words. default: 6.
        weaselLoader.maxS   = 4; 	// symbols of the discretization alphabet. default: 4.
        Classifier clf=null;
        try{
            clf = Classifier.load(new File(wclfPath));
            weaselLoader = (WEASELClassifier) clf;
            Classifier.Predictions p = weaselLoader.score(ts);
            return clf.predict(ts);
        }catch (FileNotFoundException fileNotFoundException){
            logger.error(String.format("Weasel model at [%s] not found or path not provided in application.properties", wclfPath));
            throw new Exception("Model could not be loaded");
        }

    }

}
