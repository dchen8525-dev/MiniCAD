package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved METRIC_INSTANCE.
 * A metric instance entity.
 *
 * @param id STEP instance id
 * @param name metric instance name
 * @param metricDefinition metric variance definition reference
 * @param metricValue metric variance current value
 * @param metricTrend metric variance trend direction
 * @param metricHistory metric variance historical values
 * @param metricStatus metric variance status
 */
public record StepMetricInstance(
    int id,
    String name,
    StepEntity metricDefinition,
    double metricValue,
    String metricTrend,
    List<Double> metricHistory,
    String metricStatus) implements StepEntity {
}