package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved NODE.
 * A finite element analysis node (grid point).
 */
public record StepFeaNode(
    int id,
    String name,
    double x,
    double y,
    double z) implements StepEntity {
}
