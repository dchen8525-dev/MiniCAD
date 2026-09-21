package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved FILL_AREA_STYLE_TILING.
 */
public final class StepFillAreaStyleTiling extends AbstractStepEntity {
    private final StepEntity tilingPattern;

    public StepFillAreaStyleTiling(int id, String name, StepEntity tilingPattern) {
        super(id, name);
        this.tilingPattern = tilingPattern;
    }

    public StepEntity getTilingPattern() {
        return tilingPattern;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("tilingPattern", tilingPattern);
        return state;
    }
}
