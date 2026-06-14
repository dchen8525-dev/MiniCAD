package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved GUIDELINE_INSTANCE.
 * A guideline instance entity.
 *
 * @param id STEP instance id
 * @param name guideline instance name
 * @param guidelineDefinition guideline variance definition reference
 * @param guidelineState guideline variance state
 * @param guidelineAppliedCount guideline variance applied count
 * @param guidelineStatus guideline variance status
 */
/**
 * Resolved GUIDELINE_INSTANCE.
 * A guideline instance entity.
 *
 * @param id STEP instance id
 * @param name guideline instance name
 * @param guidelineDefinition guideline variance definition reference
 * @param guidelineState guideline variance state
 * @param guidelineAppliedCount guideline variance applied count
 * @param guidelineStatus guideline variance status
 */
public final class StepGuidelineInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity guidelineDefinition;
    private final String guidelineState;
    private final int guidelineAppliedCount;
    private final String guidelineStatus;

    public StepGuidelineInstance(int id, String name, StepEntity guidelineDefinition, String guidelineState, int guidelineAppliedCount, String guidelineStatus) {
        this.id = id;
        this.name = name;
        this.guidelineDefinition = guidelineDefinition;
        this.guidelineState = guidelineState;
        this.guidelineAppliedCount = guidelineAppliedCount;
        this.guidelineStatus = guidelineStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getGuidelineDefinition() {
        return guidelineDefinition;
    }

    public String getGuidelineState() {
        return guidelineState;
    }

    public int getGuidelineAppliedCount() {
        return guidelineAppliedCount;
    }

    public String getGuidelineStatus() {
        return guidelineStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGuidelineInstance that = (StepGuidelineInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(guidelineDefinition, that.guidelineDefinition) && Objects.equals(guidelineState, that.guidelineState) && guidelineAppliedCount == that.guidelineAppliedCount && Objects.equals(guidelineStatus, that.guidelineStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, guidelineDefinition, guidelineState, guidelineAppliedCount, guidelineStatus);
    }

    @Override
    public String toString() {
        return "StepGuidelineInstance{" + "id=" + id + "name=" + name + "guidelineDefinition=" + guidelineDefinition + "guidelineState=" + guidelineState + "guidelineAppliedCount=" + guidelineAppliedCount + "guidelineStatus=" + guidelineStatus + "}";
    }
}