package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved KINEMATIC_FRAME_BASED_TRANSFORMATION.
 * A transformation defined by the relative positioning of kinematic frames.
 */
public final class StepKinematicFrameBasedTransformation extends AbstractStepEntity {
    private final String description;
    private final StepEntity sourceFrame;
    private final StepEntity targetFrame;

    public StepKinematicFrameBasedTransformation(int id, String name, String description, StepEntity sourceFrame, StepEntity targetFrame) {
        super(id, name);
        this.description = description;
        this.sourceFrame = sourceFrame;
        this.targetFrame = targetFrame;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getSourceFrame() {
        return sourceFrame;
    }

    public StepEntity getTargetFrame() {
        return targetFrame;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("sourceFrame", sourceFrame);
        state.put("targetFrame", targetFrame);
        return state;
    }
}
