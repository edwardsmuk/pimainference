package com.example.pimainference.service;

import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.ZooModel;
import com.example.pimainference.payload.Prediction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.pimainference.socket.WebSocketConfiguration.MESSAGE_PREFIX;

@Service
public class PimaInferenceService {
    private static final Logger logger = LoggerFactory.getLogger(PimaInferenceService.class);
    private final SimpMessagingTemplate websocket;

    @Autowired
    private ZooModel pimaInferenceModel;

    public PimaInferenceService(SimpMessagingTemplate websocket){
        this.websocket = websocket;
    }

    public void predictionSocket(Prediction prediction){
        logger.info("Predict Socket");
        this.predict(prediction);
        websocket.convertAndSend(MESSAGE_PREFIX+"/prediction/response", prediction);
    }

    public void predictionSocket(List<Prediction> predictions){
        logger.info("Batch predict Socket");
        for(Prediction prediction: predictions) {
            this.predict(prediction);
            websocket.convertAndSend(MESSAGE_PREFIX + "/prediction/response", prediction);
        }
    }

    public Prediction predict(Prediction prediction) {
        try (Predictor<Prediction, float[][]> predictor = pimaInferenceModel.newPredictor()) {
            float[][] outcome = predictor.predict(prediction);
            prediction.setOutcome(outcome[0][0]);
            logger.info("Prediction outcome: "+ prediction.toString());
            return prediction;
        } catch (Exception e) {
            logger.error("Failed to predict the outcome"+ e.getMessage());
            throw new RuntimeException("Failed to predict the outcome", e);
        }
    }
}
