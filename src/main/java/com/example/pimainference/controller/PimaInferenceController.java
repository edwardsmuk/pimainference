package com.example.pimainference.controller;

import com.example.pimainference.payload.ApiResponse;
import com.example.pimainference.payload.Prediction;
import com.example.pimainference.service.PimaInferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
public class PimaInferenceController {
    private static final Logger logger = LoggerFactory.getLogger(PimaInferenceController.class);

    @Autowired
    private PimaInferenceService pimaInferenceService;

    @PostMapping("/diabetes/predict")
    public ResponseEntity<?> predict(@RequestBody Prediction message ){
        logger.info("****** predict controller:");
        //RecordEntry predicted = pimaService.predict(recordEntry);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/prediction/socket")
    public ResponseEntity<?> predictionSocket( @RequestBody Prediction prediction) throws Exception{
        logger.info("*********** predictionSocket controller:*************");
        pimaInferenceService.predictionSocket(prediction);

        return ResponseEntity.ok().body(new ApiResponse(true, "Submitted prediction request "+prediction.toString()));
    }
}
