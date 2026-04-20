package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal DRAUGHTING_PRE_DEFINED_TEXT_FONT.
 *
 * @param id step id
 * @param name predefined draughting text font name
 */
public record StepDraughtingPreDefinedTextFont(int id, String name) implements StepEntity {
}
