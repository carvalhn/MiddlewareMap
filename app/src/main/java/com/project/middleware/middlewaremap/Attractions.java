package com.project.middleware.middlewaremap;

import java.io.Serializable;

/**
 * Created by Yujing on 10/02/2015.
 */
public class Attractions implements Serializable{
    private String name;
    private Double longt,lat;

    public Attractions(){}
    public Attractions(String name, Double longt, Double lat) {
        this.name = name;
        this.longt = longt;
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLongt() {
        return longt;
    }

    public void setLongt(Double longt) {
        this.longt = longt;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}
