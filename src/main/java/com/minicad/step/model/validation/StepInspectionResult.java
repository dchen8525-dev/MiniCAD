package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved INSPECTION_RESULT.
 * An inspection result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @param inspectionItem inspected item reference
 * @param measuredValues measured values
 * @param nominalValues nominal values
 * @param deviationValues deviation from nominal
 * @param passFailStatus pass/fail status for each check
 * @param inspector inspector person/organization
 * @param inspectionDate date of inspection
 */
public record StepInspectionResult(
    int id,
    String name,
    StepEntity inspectionItem,
    List<Double> measuredValues,
    List<Double> nominalValues,
    List<Double> deviationValues,
    List<String> passFailStatus,
    StepEntity inspector,
    StepEntity inspectionDate) implements StepEntity {
}