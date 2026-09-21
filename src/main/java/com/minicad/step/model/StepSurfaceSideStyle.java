package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal surface side style.
 *
 * @param id STEP instance id
 * @param name style name
 * @param styles supported surface style components
 */
public final class StepSurfaceSideStyle extends AbstractStepEntity {
    private final List<StepEntity> styles;

    public StepSurfaceSideStyle(int id, String name, List<StepEntity> styles) {
        super(id, name);
        this.styles = styles == null ? null : java.util.List.copyOf(styles);
    }

    public List<StepEntity> getStyles() {
        return styles;
    }

    // Record-style accessor
    public List<StepEntity> styles() {
        return styles;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("styles", styles);
        return state;
    }
}
