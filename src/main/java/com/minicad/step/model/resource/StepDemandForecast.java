package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepDemandForecast(
    int id,
    String name,
    List<StepEntity> varianceProducts,
    List<Double> varianceForecast,
    List<Double> varianceHistory,
    String varianceMethod,
    double varianceAccuracy,
    String varianceStatus) implements StepEntity {
}