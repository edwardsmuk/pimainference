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

    public Prediction(float pregnancies, float glucose, float bloodPressure, float skinThickness, float insulin, float bmi, float diabetesPedigreeFunction, float age, float outcome) {
        this.pregnancies = pregnancies;
        this.glucose = glucose;
        this.bloodPressure = bloodPressure;
        this.skinThickness = skinThickness;
        this.insulin = insulin;
        this.bmi = bmi;
        this.diabetesPedigreeFunction = diabetesPedigreeFunction;
        this.age = age;
        this.outcome = outcome;
    }
}
