package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal fill area style containing fill colour definitions.
 *
 * @param id STEP instance id
 * @param name style name
 * @param styles supported fill area style components
 */
public final class StepFillAreaStyle extends AbstractStepEntity {
    private final List<StepFillAreaStyleColour> styles;

    public StepFillAreaStyle(int id, String name, List<StepFillAreaStyleColour> styles) {
        super(id, name);
        this.styles = styles == null ? null : java.util.List.copyOf(styles);
    }

    public List<StepFillAreaStyleColour> getStyles() {
        return styles;
    }

    // Record-style accessor
    public List<StepFillAreaStyleColour> styles() {
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
