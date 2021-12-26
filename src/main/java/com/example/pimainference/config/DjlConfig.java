package com.example.pimainference.config;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import com.example.pimainference.payload.Prediction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Value;

@Configuration
public class DjlConfig {

    //Custom Translator
    public static final class PimaInferenceTranslator implements Translator<Prediction, float[][]> {

        private static final Logger logger = LoggerFactory.getLogger(PimaInferenceTranslator.class);

        @Value("${zoomodel.model.urls}")
        private String modelUrls;

        @Override
        public NDList processInput(TranslatorContext ctx, Prediction input) {

            NDManager manager = ctx.getNDManager();
            NDArray array = manager.create(new float[]{
                    input.getPregnancies(),
                    input.getGlucose(),
                    input.getBloodPressure(),
                    input.getSkinThickness(),
                    input.getInsulin(),
                    input.getBmi(),
                    input.getDiabetesPedigreeFunction(),
                    input.getAge()
            });
            logger.info("Input NDArray: "+array.toString());
            NDList list = new NDList(array);
            logger.info("Input NDList: "+list.toString());
            return new NDList(array);
        }

        @Override
        public float[][] processOutput(TranslatorContext ctx, NDList list) {
            NDList result = new NDList();
            logger.info("Output NDArray: " +list.singletonOrThrow().toString());
            long numOutputs = list.singletonOrThrow().getShape().get(0);
            logger.info("Number of Outputs for the model: " + numOutputs);
            for (int i = 0; i < numOutputs; i++) {
                logger.info("Predicted Outcome : "+ i + " " + list.singletonOrThrow().get(i));
                result.add(list.singletonOrThrow().get(i));
            }
            return result.stream().map(NDArray::toFloatArray).toArray(float[][]::new);
        }

        @Override
        public Batchifier getBatchifier() {
            return Batchifier.STACK;
        }

        @Bean
        public ZooModel pimaInferenceModel() throws Exception {
            Criteria<Prediction, float[][]> criteria =
                    Criteria.builder()
                            .setTypes(Prediction.class, float[][].class)
                            .optModelUrls(modelUrls)
                            .optEngine("TensorFlow")
                            .optOption("Tags", "serve")
                            .optOption("SignatureDefKey", "serving_default")
                            .optTranslator(new PimaInferenceTranslator())
                            .optProgress(new ProgressBar())
                            .build();
            return ModelZoo.loadModel(criteria);
        }
    }

}
