package com.minicad.step.model;

/**
 * Minimal PRE_DEFINED_TERMINATOR_SYMBOL.
 *
 * @param id step id
 * @param name predefined terminator symbol name
 */
public record StepPreDefinedTerminatorSymbol(int id, String name) implements StepEntity {
}
