package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal PRE_DEFINED_DIMENSION_SYMBOL.
 *
 * @param id step id
 * @param name predefined dimension symbol name
 */
public record StepPreDefinedDimensionSymbol(int id, String name) implements StepEntity {
}
