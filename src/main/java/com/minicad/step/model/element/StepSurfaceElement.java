package com.minicad.step.model.element;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
/**
 * Resolved SURFACE_ELEMENT.
 * A 2D surface finite element.
 */
public record StepSurfaceElement(
    int id,
    String name,
    List<StepEntity> nodes,
    StepEntity elementProperty) implements StepEntity {
}
