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

    @Value("${resource.path}")
    String resourcePath;
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
//        weaselLoader.minF   = 4;	// represents the minimal length for training SFA words. default: 4.
//        weaselLoader.maxF   = 6;	// represents the maximal length for training SFA words. default: 6.
//        weaselLoader.maxS   = 4; 	// symbols of the discretization alphabet. default: 4.
        Classifier clf=null;
        try{
            weaselLoader = Classifier.load(new File(wclfPath));
            logger.info(String.format("Predicting probabilities for %d samples",ts.length));
            Classifier.Predictions p = weaselLoader.predictProbabilities(ts);
            logger.info(String.format("Done !"));
            return p.probabilities;
        }catch (FileNotFoundException fileNotFoundException){
            logger.error(String.format("Weasel model at [%s] not found or property weasel.model.path not set", wclfPath));
            throw new Exception("Model could not be loaded");
        }

    }

    public double[][] train() throws Exception {
        String[] datasets = new String[]{
                "Car", "FreezerRegularTrain", "MoteStrain",
                "SonyAIBORobotSurface2", "Wafer", "DodgerLoopWeekend",
                "FreezerSmallTrain", "Plane", "ItalyPowerDemand", "SonyAIBORobotSurface1",
                "Trace"
        };
//        String[] datasets = new String[]{
//                "Car", "FreezerRegularTrain", "MoteStrain",
//                "SonyAIBORobotSurface2", "Wafer", "DodgerLoopWeekend",
//                "FreezerSmallTrain", "Plane", "StarLightCurves", "FordA", "ItalyPowerDemand", "SonyAIBORobotSurface1",
//                "Trace"
//        };
//        String[] datasets = new String[]{
//                "FordA", "ItalyPowerDemand",
//                "Trace"
//        };
        double[][] res = null;
        for (String ds: datasets){
            logger.info(String.format("Training for %s",ds));
        TimeSeries[] train = TimeSeriesLoader.loadDataset(String.format("%s/train_%s.csv",resourcePath,ds));

        TimeSeries[] test = TimeSeriesLoader.loadDataset(String.format("%s/test_%s.csv",resourcePath,ds));
        File model = new File(String.format("%s/model_%s",resourcePath,ds));
        WEASELClassifier weaselLoader = new WEASELClassifier();
        WEASELClassifier.minF = 4;	// represents the minimal length for training SFA words. default: 4.
        WEASELClassifier.maxF = 6;	// represents the maximal length for training SFA words. default: 6.
        WEASELClassifier.maxS = 4; 	// symbols of the discretion alphabet. default: 4.
        if(!model.exists()) {
            weaselLoader.fit(train);
            weaselLoader.save(model);
        }else weaselLoader = weaselLoader.load(model);

        logger.info(String.format("Done for %s",ds));
        logger.info(String.format("Predicting proba for %d samples",test.length));
        Classifier.Predictions p = weaselLoader.predictProbabilities(test);
        int correct = weaselLoader.score(test).correct.get();
        float accuracy = ((float)correct/(float)test.length )* 100;
        logger.info(String.format("Done ! [%s] %.2f ",ds,accuracy));
        res = p.probabilities;
        }

        return res;

    }

}
