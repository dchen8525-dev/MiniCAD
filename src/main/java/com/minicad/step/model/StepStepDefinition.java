package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved STEP_DEFINITION (manufacturing step feature).
 * A step definition entity.
 *
 * @param id STEP instance id
 * @param name step name
 * @param profile profile definition
 * @param depth step depth
 * @param direction step direction
 * @param stepType step type
 */
public final class StepStepDefinition extends AbstractStepEntity {
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;
    private final String stepType;

    public StepStepDefinition(int id, String name, StepEntity profile, Double depth, StepEntity direction, String stepType) {
        super(id, name);
        this.profile = profile;
        this.depth = depth;
        this.direction = direction;
        this.stepType = stepType;
    }

    public StepEntity getProfile() {
        return profile;
    }

    public Double getDepth() {
        return depth;
    }

    public StepEntity getDirection() {
        return direction;
    }

    public String getStepType() {
        return stepType;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "profile", profile, "depth", depth, "direction", direction,
                "stepType", stepType);
    }
}
