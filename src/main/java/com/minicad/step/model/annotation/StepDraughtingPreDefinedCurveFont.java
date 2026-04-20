package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal DRAUGHTING_PRE_DEFINED_CURVE_FONT.
 *
 * @param id step id
 * @param name predefined font name
 */
public record StepDraughtingPreDefinedCurveFont(int id, String name) implements StepEntity {
}
