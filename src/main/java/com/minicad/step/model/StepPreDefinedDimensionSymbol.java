package com.minicad.step.model;

/**
 * Minimal PRE_DEFINED_DIMENSION_SYMBOL.
 *
 * @param id step id
 * @param name predefined dimension symbol name
 */
public record StepPreDefinedDimensionSymbol(int id, String name) implements StepEntity {
}
