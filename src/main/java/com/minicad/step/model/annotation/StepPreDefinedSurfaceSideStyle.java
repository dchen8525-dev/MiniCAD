package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal PRE_DEFINED_SURFACE_SIDE_STYLE.
 *
 * @param id step id
 * @param name predefined surface side style name
 */
public record StepPreDefinedSurfaceSideStyle(int id, String name) implements StepEntity {
}
