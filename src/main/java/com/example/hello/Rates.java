package com.example.hello;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Map;

public class Rates {
    private Map<String, Double> data;
    private Double EGP;


    @JsonGetter("EGP")
    public Double getEGP() {
        this.EGP =  data.get("EGP");
        return EGP;
    }

    @JsonGetter("data")
    public Map<String, Double> getData() {
        return data;
    }

}
