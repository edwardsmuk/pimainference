package com.example.pimainference.helper;

import com.example.pimainference.payload.Prediction;
import org.apache.commons.csv.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVFileHelper {
    public static String TYPE = "text/csv";
    static String[] HEADERs = { "Pregnancies","Glucose","BloodPressure","SkinThickness","Insulin","BMI","DiabetesPedigreeFunction","Age","Outcome"};

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<Prediction> csvToPredictions(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Prediction> predictions = new ArrayList<Prediction>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Prediction prediction = new Prediction(
                        Float.parseFloat(csvRecord.get("Pregnancies")),
                        Float.parseFloat(csvRecord.get("Glucose")),
                        Float.parseFloat(csvRecord.get("BloodPressure")),
                        Float.parseFloat(csvRecord.get("SkinThickness")),
                        Float.parseFloat(csvRecord.get("Insulin")),
                        Float.parseFloat(csvRecord.get("BMI")),
                        Float.parseFloat(csvRecord.get("DiabetesPedigreeFunction")),
                        Float.parseFloat(csvRecord.get("Age")),
                        Float.parseFloat(csvRecord.get("Outcome"))
                );

                predictions.add(prediction);
            }

            return predictions;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream predictionsToCSV(List<Prediction> predictions) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (Prediction prediction : predictions) {
                List<String> data = Arrays.asList(
                        String.valueOf(prediction.getPregnancies()),
                        String.valueOf( prediction.getGlucose()),
                        String.valueOf( prediction.getBloodPressure()),
                        String.valueOf( prediction.getSkinThickness()),
                        String.valueOf( prediction.getInsulin()),
                        String.valueOf( prediction.getBmi()),
                        String.valueOf( prediction.getAge()),
                        String.valueOf( prediction.getOutcome())
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}
