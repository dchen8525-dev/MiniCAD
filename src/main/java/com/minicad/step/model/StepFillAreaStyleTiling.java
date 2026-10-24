package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "tilingPattern", tilingPattern);
    }
}
