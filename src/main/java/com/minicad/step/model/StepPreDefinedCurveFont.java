package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal PRE_DEFINED_CURVE_FONT.
 *
 * @param id step id
 * @param name predefined font name
 */
public final class StepPreDefinedCurveFont extends AbstractStepEntity {
    public StepPreDefinedCurveFont(int id, String name) {
        super(id, name);
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName());
    }
}
