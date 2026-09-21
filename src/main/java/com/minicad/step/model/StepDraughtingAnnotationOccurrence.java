package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal draughting annotation occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles assigned styles
 * @param item styled target item
 */
public final class StepDraughtingAnnotationOccurrence extends AbstractStepEntity {
    private final List<StepPresentationStyleAssignment> styles;
    private final StepEntity item;

    public StepDraughtingAnnotationOccurrence(int id, String name, List<StepPresentationStyleAssignment> styles, StepEntity item) {
        super(id, name);
        this.styles = styles == null ? null : java.util.List.copyOf(styles);
        this.item = item;
    }

    public List<StepPresentationStyleAssignment> getStyles() {
        return styles;
    }

    public StepEntity getItem() {
        return item;
    }

    // Record-style accessors
    public String name() {
        return getName();
    }

    public List<StepPresentationStyleAssignment> styles() {
        return styles;
    }

    public StepEntity item() {
        return item;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("styles", styles);
        state.put("item", item);
        return state;
    }
}
