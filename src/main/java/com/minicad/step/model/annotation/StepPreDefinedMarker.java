package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal PRE_DEFINED_MARKER.
 *
 * @param id step id
 * @param name predefined marker name
 */
public record StepPreDefinedMarker(int id, String name) implements StepEntity {
}
