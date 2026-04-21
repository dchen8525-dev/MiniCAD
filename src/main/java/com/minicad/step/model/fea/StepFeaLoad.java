package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved LOAD.
 * A finite element analysis load entity.
 */
public record StepFeaLoad(
    int id,
    String name,
    String loadType,
    StepEntity appliedTo,
    double magnitude) implements StepEntity {
}
