package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved BASELINE_INSTANCE.
 * A baseline instance entity.
 *
 * @param id STEP instance id
 * @param name baseline instance name
 * @param baselineDefinition baseline variance definition reference
 * @param baselineActualValues baseline variance actual values
 * @param baselineVariance baseline variance deviation from baseline
 * @param baselineStatus baseline variance status
 */
public record StepBaselineInstance(
    int id,
    String name,
    StepEntity baselineDefinition,
    List<Double> baselineActualValues,
    double baselineVariance,
    String baselineStatus) implements StepEntity {
}