package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved LOOP_DEFINITION.
 * A loop definition entity.
 *
 * @param id STEP instance id
 * @param name loop name
 * @param loopType loop variance type
 * @param loopCondition loop variance condition
 * @param loopBody loop variance body reference
 * @param loopMaxIterations loop variance max iterations
 * @param loopStatus loop variance status
 */
/**
 * Resolved LOOP_DEFINITION.
 * A loop definition entity.
 *
 * @param id STEP instance id
 * @param name loop name
 * @param loopType loop variance type
 * @param loopCondition loop variance condition
 * @param loopBody loop variance body reference
 * @param loopMaxIterations loop variance max iterations
 * @param loopStatus loop variance status
 */
public final class StepLoopDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String loopType;
    private final String loopCondition;
    private final StepEntity loopBody;
    private final int loopMaxIterations;
    private final String loopStatus;

    public StepLoopDefinition(int id, String name, String loopType, String loopCondition, StepEntity loopBody, int loopMaxIterations, String loopStatus) {
        this.id = id;
        this.name = name;
        this.loopType = loopType;
        this.loopCondition = loopCondition;
        this.loopBody = loopBody;
        this.loopMaxIterations = loopMaxIterations;
        this.loopStatus = loopStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLoopType() {
        return loopType;
    }

    public String getLoopCondition() {
        return loopCondition;
    }

    public StepEntity getLoopBody() {
        return loopBody;
    }

    public int getLoopMaxIterations() {
        return loopMaxIterations;
    }

    public String getLoopStatus() {
        return loopStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLoopDefinition that = (StepLoopDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(loopType, that.loopType) && Objects.equals(loopCondition, that.loopCondition) && Objects.equals(loopBody, that.loopBody) && loopMaxIterations == that.loopMaxIterations && Objects.equals(loopStatus, that.loopStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, loopType, loopCondition, loopBody, loopMaxIterations, loopStatus);
    }

    @Override
    public String toString() {
        return "StepLoopDefinition{" + "id=" + id + "name=" + name + "loopType=" + loopType + "loopCondition=" + loopCondition + "loopBody=" + loopBody + "loopMaxIterations=" + loopMaxIterations + "loopStatus=" + loopStatus + "}";
    }
}