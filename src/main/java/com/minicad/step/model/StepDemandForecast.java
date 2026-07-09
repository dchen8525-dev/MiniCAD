package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved DEMAND_FORECAST.
 * A demand forecast entity.
 *
 * @param id STEP instance id
 * @param name forecast name
 * @varianceProducts forecast variance products
 * @varianceForecast forecast variance quantities by period
 * @varianceHistory historical variance data
 * @varianceMethod forecast variance method
 * @varianceAccuracy forecast variance accuracy
 * @varianceStatus forecast variance status
 */
/**
 * Resolved DEMAND_FORECAST.
 * A demand forecast entity.
 *
 * @param id STEP instance id
 * @param name forecast name
 * @varianceProducts forecast variance products
 * @varianceForecast forecast variance quantities by period
 * @varianceHistory historical variance data
 * @varianceMethod forecast variance method
 * @varianceAccuracy forecast variance accuracy
 * @varianceStatus forecast variance status
 */
public final class StepDemandForecast implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> varianceProducts;
    private final List<Double> varianceForecast;
    private final List<Double> varianceHistory;
    private final String varianceMethod;
    private final double varianceAccuracy;
    private final String varianceStatus;

    public StepDemandForecast(int id, String name, List<StepEntity> varianceProducts, List<Double> varianceForecast, List<Double> varianceHistory, String varianceMethod, double varianceAccuracy, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceProducts = varianceProducts == null ? null : java.util.List.copyOf(varianceProducts);
        this.varianceForecast = varianceForecast == null ? null : java.util.List.copyOf(varianceForecast);
        this.varianceHistory = varianceHistory == null ? null : java.util.List.copyOf(varianceHistory);
        this.varianceMethod = varianceMethod;
        this.varianceAccuracy = varianceAccuracy;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getVarianceProducts() {
        return varianceProducts;
    }

    public List<Double> getVarianceForecast() {
        return varianceForecast;
    }

    public List<Double> getVarianceHistory() {
        return varianceHistory;
    }

    public String getVarianceMethod() {
        return varianceMethod;
    }

    public double getVarianceAccuracy() {
        return varianceAccuracy;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDemandForecast that = (StepDemandForecast) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceProducts, that.varianceProducts) && Objects.equals(varianceForecast, that.varianceForecast) && Objects.equals(varianceHistory, that.varianceHistory) && Objects.equals(varianceMethod, that.varianceMethod) && varianceAccuracy == that.varianceAccuracy && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceProducts, varianceForecast, varianceHistory, varianceMethod, varianceAccuracy, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepDemandForecast{" + "id=" + id + "name=" + name + "varianceProducts=" + varianceProducts + "varianceForecast=" + varianceForecast + "varianceHistory=" + varianceHistory + "varianceMethod=" + varianceMethod + "varianceAccuracy=" + varianceAccuracy + "varianceStatus=" + varianceStatus + "}";
    }
}