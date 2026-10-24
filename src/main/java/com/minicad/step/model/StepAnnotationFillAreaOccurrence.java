package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Minimal annotation fill area occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item referenced fill area
 * @param fillStyleTarget target point for fill styling
 */
public final class StepAnnotationFillAreaOccurrence extends AbstractStepEntity {
    private final List<StepPresentationStyleAssignment> styles;
    private final StepAnnotationFillArea item;
    private final StepEntity fillStyleTarget;

    public StepAnnotationFillAreaOccurrence(int id, String name, List<StepPresentationStyleAssignment> styles, StepAnnotationFillArea item, StepEntity fillStyleTarget) {
        super(id, name);
        this.styles = styles == null ? null : java.util.List.copyOf(styles);
        this.item = item;
        this.fillStyleTarget = fillStyleTarget;
    }

    public List<StepPresentationStyleAssignment> getStyles() {
        return styles;
    }

    public StepAnnotationFillArea getItem() {
        return item;
    }

    // Record-style accessors
    public String name() {
        return getName();
    }

    public List<StepPresentationStyleAssignment> styles() {
        return styles;
    }

    public StepAnnotationFillArea item() {
        return item;
    }

    public StepEntity fillStyleTarget() {
        return fillStyleTarget;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "styles", styles, "item", item,
                "fillStyleTarget", fillStyleTarget);
    }
}
