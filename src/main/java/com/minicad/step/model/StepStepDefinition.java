package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepStepDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;
    private final String stepType;

    public StepStepDefinition(int id, String name, StepEntity profile, Double depth, StepEntity direction, String stepType) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.depth = depth;
        this.direction = direction;
        this.stepType = stepType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStepDefinition that = (StepStepDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(profile, that.profile) && Objects.equals(depth, that.depth) && Objects.equals(direction, that.direction) && Objects.equals(stepType, that.stepType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profile, depth, direction, stepType);
    }

    @Override
    public String toString() {
        return "StepStepDefinition{" + "id=" + id + "name=" + name + "profile=" + profile + "depth=" + depth + "direction=" + direction + "stepType=" + stepType + "}";
    }
}