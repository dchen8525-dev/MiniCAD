package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
/**
 * Resolved FEA_MODEL.
 * A finite element analysis model.
 */
public record StepFeaModel(
    int id,
    String name,
    String modelType,
    List<StepEntity> elements,
    List<StepEntity> loads,
    List<StepEntity> boundaryConditions) implements StepEntity {
}
