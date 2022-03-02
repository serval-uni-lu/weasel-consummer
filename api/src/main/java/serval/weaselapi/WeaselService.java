package serval.weaselapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import sfa.classification.Classifier;
import sfa.classification.WEASELClassifier;
import sfa.timeseries.TimeSeries;
import sfa.timeseries.TimeSeriesLoader;

import java.io.File;
import java.io.FileNotFoundException;

@Service
public class WeaselService {
    Logger logger = LoggerFactory.getLogger(WeaselService.class);

    @Value("${weasel.model.path}")
    String wclfPath;
    @Value("${weasel.model.path.second}")
    String wclfPath2;

    @Value("${weasel.ds.train}")
    String wTrainPath;

    @Value("${weasel.ds.test}")
    String wTestPath;

    public double[][] getPredictions(TimeSeries[] ts) throws Exception {
        WEASELClassifier weaselLoader = new WEASELClassifier();
        weaselLoader.minF   = 4;	// represents the minimal length for training SFA words. default: 4.
        weaselLoader.maxF   = 6;	// represents the maximal length for training SFA words. default: 6.
        weaselLoader.maxS   = 4; 	// symbols of the discretization alphabet. default: 4.
        Classifier clf=null;
        try{
            weaselLoader = weaselLoader.load(new File(wclfPath2));
            logger.info(String.format("Predicting probabilities for %d samples",ts.length));
            Classifier.Predictions p = weaselLoader.predictProbabilities(ts);
            logger.info(String.format("Done !"));
            return p.probabilities;
        }catch (FileNotFoundException fileNotFoundException){
            logger.error(String.format("Weasel model at [%s] not found or path not provided in application.properties", wclfPath));
            throw new Exception("Model could not be loaded");
        }

    }

    public double[][] train() throws Exception {
        TimeSeries[] train = TimeSeriesLoader.loadDataset(wTrainPath);
        TimeSeries[] test = TimeSeriesLoader.loadDataset(wTestPath);
        File model = new File(wclfPath2);
        WEASELClassifier weaselLoader = new WEASELClassifier();
        WEASELClassifier.minF = 4;	// represents the minimal length for training SFA words. default: 4.
        WEASELClassifier.maxF = 6;	// represents the maximal length for training SFA words. default: 6.
        WEASELClassifier.maxS = 4; 	// symbols of the discretization alphabet. default: 4.

        if(!model.exists()) {
            weaselLoader.fit(train);
            weaselLoader.save(model);
        }else weaselLoader = weaselLoader.load(model);

        logger.info(String.format("Predicting proba for %d samples",test.length));
        Classifier.Predictions p = weaselLoader.predictProbabilities(test);
        logger.info(String.format("Done !"));
        return p.probabilities;
    }

}
