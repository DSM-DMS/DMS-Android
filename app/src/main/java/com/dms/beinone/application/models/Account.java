package com.dms.beinone.application.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BeINone on 2017-08-01.
 */

public class Account {


    @SerializedName("stay_value")
    private int stayValue;
    @SerializedName("number")
    private int number;
    @SerializedName("stayup_date")
    private String signUpDate;
    @SerializedName("name")
    private String name;
    @SerializedName("goingout_sun")
    private String goingOutSun;
    @SerializedName("goingout_sat")
    private String goingOutSat;
    @SerializedName("extension_11_class")
    private int extension_11_class;
    @SerializedName("extension_12_class")
    private int extension_12_class;
    @SerializedName("extension_11_seat")
    private int extension_11_seat;
    @SerializedName("extension_12_seat")
    private int extension_12_seat;


    public int getStayValue() {
        return stayValue;
    }

    public void setStayValue(int stayValue) {
        this.stayValue = stayValue;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(String signUpDate) {
        this.signUpDate = signUpDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoingOutSun() {
        return goingOutSun;
    }

    public void setGoingOutSun(String goingOutSun) {
        this.goingOutSun = goingOutSun;
    }

    public String getGoingOutSat() {
        return goingOutSat;
    }

    public void setGoingOutSat(String goingOutSat) {
        this.goingOutSat = goingOutSat;
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




}
