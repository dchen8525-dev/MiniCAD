package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepDemandForecast extends AbstractStepEntity {
    private final List<StepEntity> varianceProducts;
    private final List<Double> varianceForecast;
    private final List<Double> varianceHistory;
    private final String varianceMethod;
    private final double varianceAccuracy;
    private final String varianceStatus;

    public StepDemandForecast(int id, String name, List<StepEntity> varianceProducts, List<Double> varianceForecast, List<Double> varianceHistory, String varianceMethod, double varianceAccuracy, String varianceStatus) {
        super(id, name);
        this.varianceProducts = varianceProducts == null ? null : java.util.List.copyOf(varianceProducts);
        this.varianceForecast = varianceForecast == null ? null : java.util.List.copyOf(varianceForecast);
        this.varianceHistory = varianceHistory == null ? null : java.util.List.copyOf(varianceHistory);
        this.varianceMethod = varianceMethod;
        this.varianceAccuracy = varianceAccuracy;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "varianceProducts", varianceProducts,
                "varianceForecast", varianceForecast, "varianceHistory", varianceHistory,
                "varianceMethod", varianceMethod, "varianceAccuracy", varianceAccuracy,
                "varianceStatus", varianceStatus);
    }
}
