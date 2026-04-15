package com.minicad.step.model;

import java.util.List;

/**
 * Resolved VENTILATION_FEATURE.
 * A ventilation feature entity.
 *
 * @param id STEP instance id
 * @param name ventilation name
 * @param ventilationType ventilation type (natural, forced, exhaust)
 * @param ventilationGeometry ventilation geometry representation
 * @varianceAirflow variance airflow capacity
 * @param inletFeatures inlet features
 * @param outletFeatures outlet features
 * @param ventilationControl ventilation control specification
 */
public record StepVentilationFeature(
    int id,
    String name,
    String ventilationType,
    StepEntity ventilationGeometry,
    double varianceAirflow,
    List<StepEntity> inletFeatures,
    List<StepEntity> outletFeatures,
    StepEntity ventilationControl) implements StepEntity {
}