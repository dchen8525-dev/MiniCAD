package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved FILL_AREA_WITH_OUTLINE.
 */
public final class StepFillAreaWithOutline extends AbstractStepEntity {
    private final List<StepEntity> outlines;

    public StepFillAreaWithOutline(int id, String name, List<StepEntity> outlines) {
        super(id, name);
        this.outlines = outlines == null ? null : java.util.List.copyOf(outlines);
    }

    public List<StepEntity> getOutlines() {
        return outlines;
    }

    // Record-style accessor
    public List<StepEntity> outlines() {
        return outlines;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "outlines", outlines);
    }
}
