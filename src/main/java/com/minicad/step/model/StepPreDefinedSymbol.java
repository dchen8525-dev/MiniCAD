package com.minicad.step.model;

/**
 * Minimal PRE_DEFINED_SYMBOL.
 *
 * @param id step id
 * @param name predefined symbol name
 */
public record StepPreDefinedSymbol(int id, String name) implements StepEntity {
}
