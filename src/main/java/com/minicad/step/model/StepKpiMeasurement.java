package com.minicad.step.model;

import java.util.List;

/**
 * Resolved KPI_MEASUREMENT.
 * A KPI measurement entity.
 *
 * @param id STEP instance id
 * @param name measurement name
 * @varianceKpi KPI variance reference
 * @varianceValue measured variance value
 * @varianceDate measurement variance date
 * @variancePeriod measurement variance period
 * @varianceStatus measurement variance status
 * @varianceComment measurement variance comment
 */
public record StepKpiMeasurement(
    int id,
    String name,
    StepEntity varianceKpi,
    double varianceValue,
    StepEntity varianceDate,
    String variancePeriod,
    String varianceStatus,
    String varianceComment) implements StepEntity {
}