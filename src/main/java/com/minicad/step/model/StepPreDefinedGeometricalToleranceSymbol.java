package com.minicad.step.model;

/**
 * Minimal PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL.
 *
 * @param id step id
 * @param name predefined geometrical tolerance symbol name
 */
public record StepPreDefinedGeometricalToleranceSymbol(int id, String name) implements StepEntity {
}
