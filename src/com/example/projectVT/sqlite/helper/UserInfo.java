package com.example.projectVT.sqlite.helper;

/**
 * Created by Jay on 4/14/14.
 */


public class UserInfo {

    long id;
    String name;
    int feet;
    int inch;
    int weight;
    String gender;

    public UserInfo(){}

    public UserInfo(String name, int ft, int in, int weight, String gender){
        this.name = name;
        this.feet = ft;
        this.inch = in;
        this.weight = weight;
        this.gender = gender;
    }

    public UserInfo(long id, String name, int ft, int in, int weight, String gender){
        this.id = id;
        this.name = name;
        this.feet = ft;
        this.inch = in;
        this.weight = weight;
        this.gender = gender;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setFeet(int feet){
        this.feet = feet;
    }

    public void setInch(int inch){
        this.inch = inch;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public void setGender(String gender){
        this.gender = gender;
    }


    public String getName(){
        return this.name;
    }

    public int getFeet(){
        return feet;
    }

    public int getInch(){
        return inch;
    }

    public int getWeight(){
        return weight;
    }

    public String getGender(){ return gender; }

    public long getId(){ return id; }
}
