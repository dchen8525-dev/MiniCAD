package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved LOOP_INSTANCE.
 * A loop instance entity.
 *
 * @param id STEP instance id
 * @param name loop instance name
 * @param loopDefinition loop variance definition reference
 * @param loopState loop variance state
 * @param loopIteration loop variance current iteration
 * @param loopCompleted loop variance completed flag
 * @param loopStatus loop variance status
 */
/**
 * Resolved LOOP_INSTANCE.
 * A loop instance entity.
 *
 * @param id STEP instance id
 * @param name loop instance name
 * @param loopDefinition loop variance definition reference
 * @param loopState loop variance state
 * @param loopIteration loop variance current iteration
 * @param loopCompleted loop variance completed flag
 * @param loopStatus loop variance status
 */
public final class StepLoopInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity loopDefinition;
    private final String loopState;
    private final int loopIteration;
    private final boolean loopCompleted;
    private final String loopStatus;

    public StepLoopInstance(int id, String name, StepEntity loopDefinition, String loopState, int loopIteration, boolean loopCompleted, String loopStatus) {
        this.id = id;
        this.name = name;
        this.loopDefinition = loopDefinition;
        this.loopState = loopState;
        this.loopIteration = loopIteration;
        this.loopCompleted = loopCompleted;
        this.loopStatus = loopStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getLoopDefinition() {
        return loopDefinition;
    }

    public String getLoopState() {
        return loopState;
    }

    public int getLoopIteration() {
        return loopIteration;
    }

    public boolean isLoopCompleted() {
        return loopCompleted;
    }

    public String getLoopStatus() {
        return loopStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLoopInstance that = (StepLoopInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(loopDefinition, that.loopDefinition) && Objects.equals(loopState, that.loopState) && loopIteration == that.loopIteration && loopCompleted == that.loopCompleted && Objects.equals(loopStatus, that.loopStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, loopDefinition, loopState, loopIteration, loopCompleted, loopStatus);
    }

    @Override
    public String toString() {
        return "StepLoopInstance{" + "id=" + id + "name=" + name + "loopDefinition=" + loopDefinition + "loopState=" + loopState + "loopIteration=" + loopIteration + "loopCompleted=" + loopCompleted + "loopStatus=" + loopStatus + "}";
    }
}