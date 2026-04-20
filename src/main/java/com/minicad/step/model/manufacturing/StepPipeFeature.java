package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved PIPE_FEATURE.
 * A pipe feature entity.
 *
 * @param id STEP instance id
 * @param name pipe name
 * @param pipeType pipe type classification (straight, bent)
 * @param outerDiameter outer diameter
 * @param innerDiameter inner diameter
 * @param pipeLength pipe length
 * @param wallThickness wall thickness
 * @param pipeBends pipe bend features for bent pipes
 * @param pipeMaterial pipe material specification
 */
public record StepPipeFeature(
    int id,
    String name,
    String pipeType,
    double outerDiameter,
    double innerDiameter,
    double pipeLength,
    double wallThickness,
    List<StepEntity> pipeBends,
    StepEntity pipeMaterial) implements StepEntity {
}