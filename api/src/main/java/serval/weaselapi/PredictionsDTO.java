package serval.weaselapi;

import sfa.classification.Classifier;

public class PredictionsDTO extends Classifier.Predictions {
    public PredictionsDTO(Double[] labels, int bestCorrect) {
        super(labels, bestCorrect);
    }

    public PredictionsDTO(Double[] predictions, double[][] probabilities, int[] realLabels) {
        super(predictions, probabilities, realLabels);
    }

}
