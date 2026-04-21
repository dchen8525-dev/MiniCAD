package com.minicad.step.model.element;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
/**
 * Resolved UNIFORM_SURFACE_ELEMENT.
 * A uniform surface finite element.
 */
public record StepUniformSurfaceElement(
    int id,
    String name,
    List<StepEntity> nodes,
    StepEntity elementProperty) implements StepEntity {
}
