package com.example.pimainference.payload;

import lombok.Data;

@Data
public class Prediction {
    private float pregnancies;
    private float glucose;
    private float bloodPressure;
    private float skinThickness;
    private float insulin;
    private float bmi;
    private float diabetesPedigreeFunction;
    private float age;
    private float outcome;

    public Prediction(){}
}
