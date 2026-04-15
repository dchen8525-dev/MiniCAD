package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ANALYSIS_MODEL.
 * An analysis model entity.
 *
 * @param id STEP instance id
 * @param name model name
 * @param modelType analysis model type (structural, thermal, fluid)
 * @param modelGeometry geometry for analysis
 * @param mesh mesh representation
 * @param boundaryConditions boundary conditions
 * @param loads applied loads
 * @param materialProperties material properties for analysis
 */
public record StepAnalysisModel(
    int id,
    String name,
    String modelType,
    StepEntity modelGeometry,
    StepEntity mesh,
    List<StepEntity> boundaryConditions,
    List<StepEntity> loads,
    List<StepEntity> materialProperties) implements StepEntity {
}