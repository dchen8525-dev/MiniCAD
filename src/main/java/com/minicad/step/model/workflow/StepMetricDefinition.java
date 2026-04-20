package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved METRIC_DEFINITION.
 * A metric definition entity.
 *
 * @param id STEP instance id
 * @param name metric name
 * @param metricType metric variance type
 * @param metricUnit metric variance unit
 * @param metricRange metric variance valid range
 * @param metricFormula metric variance calculation formula
 * @param metricStatus metric variance status
 */
public record StepMetricDefinition(
    int id,
    String name,
    String metricType,
    StepEntity metricUnit,
    List<Double> metricRange,
    String metricFormula,
    String metricStatus) implements StepEntity {
}