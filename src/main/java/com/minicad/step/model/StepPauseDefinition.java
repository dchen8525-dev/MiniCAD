package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PAUSE_DEFINITION.
 * A pause definition entity.
 *
 * @param id STEP instance id
 * @param name pause name
 * @param pauseType pause variance type
 * @param pauseCondition pause variance condition
 * @param pauseResumeCondition pause variance resume condition
 * @param pauseTimeout pause variance max pause time
 * @param pauseStatus pause variance status
 */
/**
 * Resolved PAUSE_DEFINITION.
 * A pause definition entity.
 *
 * @param id STEP instance id
 * @param name pause name
 * @param pauseType pause variance type
 * @param pauseCondition pause variance condition
 * @param pauseResumeCondition pause variance resume condition
 * @param pauseTimeout pause variance max pause time
 * @param pauseStatus pause variance status
 */
public final class StepPauseDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String pauseType;
    private final String pauseCondition;
    private final String pauseResumeCondition;
    private final int pauseTimeout;
    private final String pauseStatus;

    public StepPauseDefinition(int id, String name, String pauseType, String pauseCondition, String pauseResumeCondition, int pauseTimeout, String pauseStatus) {
        this.id = id;
        this.name = name;
        this.pauseType = pauseType;
        this.pauseCondition = pauseCondition;
        this.pauseResumeCondition = pauseResumeCondition;
        this.pauseTimeout = pauseTimeout;
        this.pauseStatus = pauseStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPauseType() {
        return pauseType;
    }

    public String getPauseCondition() {
        return pauseCondition;
    }

    public String getPauseResumeCondition() {
        return pauseResumeCondition;
    }

    public int getPauseTimeout() {
        return pauseTimeout;
    }

    public String getPauseStatus() {
        return pauseStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPauseDefinition that = (StepPauseDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(pauseType, that.pauseType) && Objects.equals(pauseCondition, that.pauseCondition) && Objects.equals(pauseResumeCondition, that.pauseResumeCondition) && pauseTimeout == that.pauseTimeout && Objects.equals(pauseStatus, that.pauseStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pauseType, pauseCondition, pauseResumeCondition, pauseTimeout, pauseStatus);
    }

    @Override
    public String toString() {
        return "StepPauseDefinition{" + "id=" + id + "name=" + name + "pauseType=" + pauseType + "pauseCondition=" + pauseCondition + "pauseResumeCondition=" + pauseResumeCondition + "pauseTimeout=" + pauseTimeout + "pauseStatus=" + pauseStatus + "}";
    }
}