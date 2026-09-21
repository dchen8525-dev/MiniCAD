package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal surface style usage.
 *
 * @param id STEP instance id
 * @param side side enum
 * @param style referenced side style
 */
public final class StepSurfaceStyleUsage extends AbstractStepEntity {
    private final String side;
    private final StepSurfaceSideStyle style;

    public StepSurfaceStyleUsage(int id, String side, StepSurfaceSideStyle style) {
        super(id, "");
        this.side = side;
        this.style = style;
    }

    public String getSide() {
        return side;
    }

    public StepSurfaceSideStyle getStyle() {
        return style;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public String side() { return getSide(); }
    public StepSurfaceSideStyle style() { return getStyle(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("side", side);
        state.put("style", style);
        return state;
    }
}
