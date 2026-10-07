package model;

import java.util.ArrayList;
import java.util.List;

public class ProteinModel {

    public static final double PROTEIN_PER_KG = 1.6;
    public static final double POUNDS_TO_KG = 0.45359237;

    private double weight;
    private boolean pounds;

    private double weightKg;
    private double proteinNorm;

    private boolean hasData;

    private final List<ModelObserver> observers = new ArrayList<>();

    public void addObserver(ModelObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(ModelObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (ModelObserver observer : new ArrayList<>(observers)) {
            observer.modelChanged();
        }
    }

    public void setData(double weight, boolean pounds) {
        this.weight = weight;
        this.pounds = pounds;
        this.hasData = true;

        calculate();
        notifyObservers();
    }

    private void calculate() {
        weightKg = pounds
            ? weight * POUNDS_TO_KG
            : weight;

        proteinNorm = weightKg * PROTEIN_PER_KG;
    }

    public double getWeight() {
        return weight;
    }

    public boolean isPounds() {
        return pounds;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public double getProteinNorm() {
        return proteinNorm;
    }

    public boolean hasData() {
        return hasData;
    }
}