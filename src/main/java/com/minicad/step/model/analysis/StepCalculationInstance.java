package com.minicad.step.model.analysis;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CALCULATION_INSTANCE.
 * A calculation instance entity.
 *
 * @param id STEP instance id
 * @param name calculation instance name
 * @param calculationDefinition calculation variance definition reference
 * @param calculationInputValues calculation variance input values
 * @param calculationOutputValues calculation variance output values
 * @param calculationError calculation variance error estimate
 * @param calculationStatus calculation variance status
 */
public record StepCalculationInstance(
    int id,
    String name,
    StepEntity calculationDefinition,
    List<Double> calculationInputValues,
    List<Double> calculationOutputValues,
    double calculationError,
    String calculationStatus) implements StepEntity {
}