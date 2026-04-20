package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal fill area style colour.
 *
 * @param id STEP instance id
 * @param name style name
 * @param colour referenced colour
 */
public record StepFillAreaStyleColour(int id, String name, StepEntity colour) implements StepEntity {
}
