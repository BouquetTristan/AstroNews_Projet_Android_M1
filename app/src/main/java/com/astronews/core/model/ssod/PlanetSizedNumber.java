package com.astronews.core.model.ssod;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class PlanetSizedNumber {
    private float value;
    private int massExponent;
    private int volExponent;

    public PlanetSizedNumber() {
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getMassExponent() {
        return massExponent;
    }

    public void setMassExponent(int massExponent) {
        this.massExponent = massExponent;
    }

    public int getVolExponent() {
        return volExponent;
    }

    public void setVolExponent(int volExponent) {
        this.volExponent = volExponent;
    }
}
