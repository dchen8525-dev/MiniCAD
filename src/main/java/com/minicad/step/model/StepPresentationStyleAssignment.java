package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal presentation style assignment.
 *
 * @param id STEP instance id
 * @param styles referenced presentation styles
 */
public final class StepPresentationStyleAssignment extends AbstractStepEntity {
    private final List<StepEntity> styles;

    public StepPresentationStyleAssignment(int id, String name, List<StepEntity> styles) {
        super(id, name != null ? name : "");
        this.styles = styles == null ? null : java.util.List.copyOf(styles);
    }

    public StepPresentationStyleAssignment(int id, List<StepEntity> styles) {
        this(id, "", styles);
    }

    public List<StepEntity> getStyles() {
        return styles;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepEntity> styles() { return getStyles(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("styles", styles);
        return state;
    }
}
