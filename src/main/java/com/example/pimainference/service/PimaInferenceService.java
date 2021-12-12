package com.example.pimainference.service;

import com.example.pimainference.payload.Prediction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.example.pimainference.socket.WebSocketConfiguration.MESSAGE_PREFIX;

@Service
public class PimaInferenceService {
    private static final Logger logger = LoggerFactory.getLogger(PimaInferenceService.class);
    private final SimpMessagingTemplate websocket;

    public PimaInferenceService(SimpMessagingTemplate websocket){
        this.websocket = websocket;
    }

    public void predictionSocket(Prediction prediction){
        logger.info("****** predictionSocket  service:");
        websocket.convertAndSend(MESSAGE_PREFIX+"/prediction/response", prediction);
        //return prediction;
    }




}
