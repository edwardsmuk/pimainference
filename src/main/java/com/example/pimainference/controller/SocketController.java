package com.example.pimainference.controller;

import com.example.pimainference.payload.ApiResponse;
import com.example.pimainference.payload.Prediction;
import com.example.pimainference.service.PimaInferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class SocketController {

    private static final Logger logger = LoggerFactory.getLogger(PimaInferenceController.class);

    @Autowired
    private PimaInferenceService pimaInferenceService;

    @MessageMapping("/prediction/socket")
    public ResponseEntity<?> predictionSocket(@RequestBody Prediction prediction) throws Exception{
        logger.info("Prediction Socket endpoint called");
        pimaInferenceService.predictionSocket(prediction);
        return ResponseEntity.ok().body(new ApiResponse(true, "Submitted prediction request "+prediction.toString()));
    }
}
