package com.example.pimainference.controller;

import com.example.pimainference.helper.CSVFileHelper;
import com.example.pimainference.payload.ApiResponse;
import com.example.pimainference.payload.Prediction;
import com.example.pimainference.service.PimaInferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


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

    @PostMapping("/prediction/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (CSVFileHelper.hasCSVFormat(file)) {
            try {
                List<Prediction> predictions = CSVFileHelper.csvToPredictions(file.getInputStream());

                pimaInferenceService.predictionSocket(predictions);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ApiResponse(false, message));
            }
        }
        message = "Please upload a prediction csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false,message));
    }

}
