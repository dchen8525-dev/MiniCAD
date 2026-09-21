package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved FILL_AREA_STYLE_OUTLINE.
 */
public final class StepFillAreaStyleOutline extends AbstractStepEntity {
    private final StepEntity style;

    public StepFillAreaStyleOutline(int id, String name, StepEntity style) {
        super(id, name);
        this.style = style;
    }

    public StepEntity getStyle() {
        return style;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("style", style);
        return state;
    }
}
