package com.dms.beinone.application.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by BeINone on 2017-07-15.
 */

public class ApplyStatus {



    @SerializedName("extension_11_class")
    private boolean extensionClass11;
    @SerializedName("extension_11_seat")
    private int extensionSeat11;

    @SerializedName("extension_12_class")
    private boolean extensionClass12;
    @SerializedName("extension_12_seat")
    private int extensionSeat12;

    @SerializedName("name")
    private String extensionName;

    private boolean sat;
    private boolean sun;

    @SerializedName("signup_date")
    private String signUpDate;

    public boolean isExtensionClass11() {
        return extensionClass11;
    }

    public void setExtensionClass11(boolean extensionClass11) {
        this.extensionClass11 = extensionClass11;
    }

    public int getExtensionSeat11() {
        return extensionSeat11;
    }

    public void setExtensionSeat11(int extensionSeat11) {
        this.extensionSeat11 = extensionSeat11;
    }

    public boolean isExtensionClass12() {
        return extensionClass12;
    }

    public void setExtensionClass12(boolean extensionClass12) {
        this.extensionClass12 = extensionClass12;
    }

    public int getExtensionSeat12() {
        return extensionSeat12;
    }

    public void setExtensionSeat12(int extensionSeat12) {
        this.extensionSeat12 = extensionSeat12;
    }

    public String getExtensionName() {
        return extensionName;
    }

    public void setExtensionName(String extensionName) {
        this.extensionName = extensionName;
    }

    public boolean isSat() {
        return sat;
    }

    public void setSat(boolean sat) {
        this.sat = sat;
    }

    public boolean isSun() {
        return sun;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }

    public String getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(String signUpDate) {
        this.signUpDate = signUpDate;
    }

    public int getStayValue() {
        return stayValue;
    }

    public void setStayValue(int stayValue) {
        this.stayValue = stayValue;
    }

    @SerializedName("value")
    private int stayValue;


}
