package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal PRE_DEFINED_POINT_MARKER_SYMBOL.
 *
 * @param id step id
 * @param name predefined point marker symbol name
 */
public final class StepPreDefinedPointMarkerSymbol extends AbstractStepEntity {
    public StepPreDefinedPointMarkerSymbol(int id, String name) {
        super(id, name);
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName());
    }
}
