package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal annotation plane occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item referenced plane
 * @param elements optional annotation plane elements
 */
public final class StepAnnotationPlane extends AbstractStepEntity {
    private final List<StepPresentationStyleAssignment> styles;
    private final StepPlane item;
    private final List<StepEntity> elements;

    public StepAnnotationPlane(int id, String name, List<StepPresentationStyleAssignment> styles, StepPlane item, List<StepEntity> elements) {
        super(id, name);
        this.styles = styles == null ? null : java.util.List.copyOf(styles);
        this.item = item;
        this.elements = elements == null ? null : java.util.List.copyOf(elements);
    }

    public List<StepPresentationStyleAssignment> getStyles() {
        return styles;
    }

    public StepPlane getItem() {
        return item;
    }

    public List<StepEntity> getElements() {
        return elements;
    }

    // Record-style accessor
    public List<StepEntity> elements() {
        return elements;
    }

    public List<StepPresentationStyleAssignment> styles() {
        return styles;
    }

    public StepPlane item() {
        return item;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("styles", styles);
        state.put("item", item);
        state.put("elements", elements);
        return state;
    }
}
