package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved KINEMATIC_FRAME_BASED_TRANSFORMATION.
 * A transformation defined by the relative positioning of kinematic frames.
 */
/**
 * Resolved KINEMATIC_FRAME_BASED_TRANSFORMATION.
 * A transformation defined by the relative positioning of kinematic frames.
 */
public final class StepKinematicFrameBasedTransformation implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity sourceFrame;
    private final StepEntity targetFrame;

    public StepKinematicFrameBasedTransformation(int id, String name, String description, StepEntity sourceFrame, StepEntity targetFrame) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sourceFrame = sourceFrame;
        this.targetFrame = targetFrame;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepKinematicFrameBasedTransformation that = (StepKinematicFrameBasedTransformation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(sourceFrame, that.sourceFrame) && Objects.equals(targetFrame, that.targetFrame);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, sourceFrame, targetFrame);
    }

    @Override
    public String toString() {
        return "StepKinematicFrameBasedTransformation{" + "id=" + id + "name=" + name + "description=" + description + "sourceFrame=" + sourceFrame + "targetFrame=" + targetFrame + "}";
    }
}
