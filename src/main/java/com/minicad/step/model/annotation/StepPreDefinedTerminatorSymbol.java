package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal PRE_DEFINED_TERMINATOR_SYMBOL.
 *
 * @param id step id
 * @param name predefined terminator symbol name
 */
public record StepPreDefinedTerminatorSymbol(int id, String name) implements StepEntity {
}
