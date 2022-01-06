package com.example.hello;

import java.util.Map;

public class Rates {
    private Map<String, Double> data;

    public Double getEGP() {
        this.EGP =  data.get("EGP");
        return EGP;
    }

    public void setEGP(Double EGP) {
        this.EGP = EGP;
    }

    private Double EGP;

    public Map<String, Double> getData() {
        return data;
    }

    public void setData(Map<String, Double> result) {
        this.data = result;
    }

}
