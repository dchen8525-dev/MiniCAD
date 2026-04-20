package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal PRE_DEFINED_POINT_MARKER_SYMBOL.
 *
 * @param id step id
 * @param name predefined point marker symbol name
 */
public record StepPreDefinedPointMarkerSymbol(int id, String name) implements StepEntity {
}
