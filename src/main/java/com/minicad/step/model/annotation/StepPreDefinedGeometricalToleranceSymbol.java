package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL.
 *
 * @param id step id
 * @param name predefined geometrical tolerance symbol name
 */
public record StepPreDefinedGeometricalToleranceSymbol(int id, String name) implements StepEntity {
}
