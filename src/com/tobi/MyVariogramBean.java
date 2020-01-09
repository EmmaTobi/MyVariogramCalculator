package com.tobi;

import javafx.beans.property.SimpleDoubleProperty;

public class MyVariogramBean {

    private SimpleDoubleProperty variogram = new SimpleDoubleProperty(this, "variogram", 0.0);
    private SimpleDoubleProperty pairs = new SimpleDoubleProperty(this, "pairs", 0.0);
    private SimpleDoubleProperty distance = new SimpleDoubleProperty(this, "distance", 0.0);


    public MyVariogramBean(double variogram, double pairs, double distance) {
        this.setVariogram(variogram);
        this.setPairs(pairs);
        this.setDistance(distance);
    }

    public double getVariogram() {
        return variogram.get();
    }

    public SimpleDoubleProperty variogramProperty() {
        return variogram;
    }

    public void setVariogram(double variogram) {
        this.variogram.set(variogram);
    }

    public double getPairs() {
        return pairs.get();
    }

    public SimpleDoubleProperty pairsProperty() {
        return pairs;
    }

    public void setPairs(double pairs) {
        this.pairs.set(pairs);
    }

    public double getDistance() {
        return distance.get();
    }

    public SimpleDoubleProperty distanceProperty() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance.set(distance);
    }

    @Override
    public String toString() {
        return "MyVariogramBean{"+
                "variogram=" + getVariogram() +
                ", pairs=" + getPairs() +
                ", distance= "+getDistance()+
                '}';
    }
}
