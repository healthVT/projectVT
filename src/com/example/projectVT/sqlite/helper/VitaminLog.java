package com.example.projectVT.sqlite.helper;

import java.util.Date;

/**
 * Created by Jay on 4/19/14.
 */
public class VitaminLog {
    Date logDate;
    long id;
    long userId;
    String foodName;
    double vitaminA;
    double vitaminC;
    double vitaminD;
    double vitaminE;
    double vitaminK;
    double vitaminB1;
    double vitaminB2;
    double vitaminB3;
    double vitaminB6;
    double vitaminB12;
    double pantothenic;
    double biotin;

    public VitaminLog(){}

    public VitaminLog(long userId, String foodName, double vitaminA, double vitaminC, double vitaminD, double vitaminE, double vitaminK, double vitaminB1, double vitaminB2, double vitaminB3, double vitaminB6, double vitaminB12){
        this.userId = userId;
        this.foodName = foodName;
        this.vitaminA = vitaminA;
        this.vitaminC = vitaminC;
        this.vitaminD = vitaminD;
        this.vitaminE = vitaminE;
        this.vitaminK = vitaminK;
        this.vitaminB1 = vitaminB1;
        this.vitaminB2 = vitaminB2;
        this.vitaminB3 = vitaminB3;
        this.vitaminB6 = vitaminB6;
        this.vitaminB12 = vitaminB12;


    }

    public long getId(){ return id; }
    public long getUserId(){ return userId; }
    public String getFoodName(){ return foodName; }
    public double getVitaminA(){ return vitaminA; }
    public double getVitaminC(){ return vitaminC; }
    public double getVitaminD(){ return vitaminD; }
    public double getVitaminE(){ return vitaminE; }
    public double getVitaminK(){ return vitaminK; }
    public double getVitaminB1(){ return vitaminB1; }
    public double getVitaminB2(){ return vitaminB2; }
    public double getVitaminB3(){ return vitaminB3; }
    public double getVitaminB6(){ return vitaminB6; }
    public double getVitaminB12(){ return vitaminB12; }

}
