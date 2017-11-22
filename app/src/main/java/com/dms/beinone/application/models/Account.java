package com.dms.beinone.application.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BeINone on 2017-08-01.
 */

public class Account {


    @SerializedName("stay_value")
    private int stayValue;
    private int number;

    @SerializedName("stayup_date")
    private String signupDate;
    private String name;

    @SerializedName("goingout_sun")
    private String goingoutSun;
    @SerializedName("goingout_sat")
    private String goingoutSat;

    private int extension_11_class;
    private int extension_12_class;
    private int extension_11_seat;
    private int extension_12_seat;


    private int merit;
    private int demerit;

    public String getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(String signupDate) {
        this.signupDate = signupDate;
    }

    public String getGoingoutSun() {
        return goingoutSun;
    }

    public void setGoingoutSun(String goingoutSun) {
        this.goingoutSun = goingoutSun;
    }

    public String getGoingoutSat() {
        return goingoutSat;
    }

    public void setGoingoutSat(String goingoutSat) {
        this.goingoutSat = goingoutSat;
    }

    public int getExtension_11_class() {
        return extension_11_class;
    }

    public void setExtension_11_class(int extension_11_class) {
        this.extension_11_class = extension_11_class;
    }

    public int getExtension_12_class() {
        return extension_12_class;
    }

    public void setExtension_12_class(int extension_12_class) {
        this.extension_12_class = extension_12_class;
    }

    public int getExtension_11_seat() {
        return extension_11_seat;
    }

    public void setExtension_11_seat(int extension_11_seat) {
        this.extension_11_seat = extension_11_seat;
    }

    public int getExtension_12_seat() {
        return extension_12_seat;
    }

    public void setExtension_12_seat(int extension_12_seat) {
        this.extension_12_seat = extension_12_seat;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMerit() {
        return merit;
    }

    public void setMerit(int merit) {
        this.merit = merit;
    }

    public int getDemerit() {
        return demerit;
    }

    public void setDemerit(int demerit) {
        this.demerit = demerit;
    }

    public int getStayValue() {
        return stayValue;
    }

    public void setStayValue(int stayValue) {
        this.stayValue = stayValue;
    }
}
