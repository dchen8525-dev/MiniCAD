package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal COLOUR_SPECIFICATION.
 *
 * @param id step id
 * @param name colour name
 */
public record StepColourSpecification(int id, String name) implements StepEntity {
}
