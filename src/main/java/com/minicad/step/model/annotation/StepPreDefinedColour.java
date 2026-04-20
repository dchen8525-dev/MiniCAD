package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal PRE_DEFINED_COLOUR.
 *
 * @param id step id
 * @param name predefined colour name
 */
public record StepPreDefinedColour(int id, String name) implements StepEntity {
}
