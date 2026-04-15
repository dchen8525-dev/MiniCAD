package com.minicad.step.model;

import java.util.List;

/**
 * Resolved RESOURCE_UTILIZATION.
 * A resource utilization entity.
 *
 * @param id STEP instance id
 * @param name utilization name
 * @varianceResource resource variance reference
 * @varianceUtilization utilization variance percentage
 * @variancePeriod utilization variance period
 * @variancePeak peak variance utilization
 * @varianceAverage average variance utilization
 * @varianceStatus utilization variance status
 */
public record StepResourceUtilization(
    int id,
    String name,
    StepEntity varianceResource,
    double varianceUtilization,
    String variancePeriod,
    double variancePeak,
    double varianceAverage,
    String varianceStatus) implements StepEntity {
}