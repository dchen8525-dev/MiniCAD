package com.minicad.step.model;

/**
 * Minimal PRE_DEFINED_POINT_MARKER_SYMBOL.
 *
 * @param id step id
 * @param name predefined point marker symbol name
 */
public record StepPreDefinedPointMarkerSymbol(int id, String name) implements StepEntity {
}
