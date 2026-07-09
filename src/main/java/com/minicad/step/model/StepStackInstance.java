package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved STACK_INSTANCE.
 * A stack instance entity.
 *
 * @param id STEP instance id
 * @param name stack instance name
 * @param stackDefinition stack variance definition reference
 * @param stackState stack variance state
 * @param stackDepth stack variance current depth
 * @param stackStatus stack variance status
 */
/**
 * Resolved STACK_INSTANCE.
 * A stack instance entity.
 *
 * @param id STEP instance id
 * @param name stack instance name
 * @param stackDefinition stack variance definition reference
 * @param stackState stack variance state
 * @param stackDepth stack variance current depth
 * @param stackStatus stack variance status
 */
public final class StepStackInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity stackDefinition;
    private final String stackState;
    private final int stackDepth;
    private final String stackStatus;

    public StepStackInstance(int id, String name, StepEntity stackDefinition, String stackState, int stackDepth, String stackStatus) {
        this.id = id;
        this.name = name;
        this.stackDefinition = stackDefinition;
        this.stackState = stackState;
        this.stackDepth = stackDepth;
        this.stackStatus = stackStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getStackDefinition() {
        return stackDefinition;
    }

    public String getStackState() {
        return stackState;
    }

    public int getStackDepth() {
        return stackDepth;
    }

    public String getStackStatus() {
        return stackStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStackInstance that = (StepStackInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(stackDefinition, that.stackDefinition) && Objects.equals(stackState, that.stackState) && stackDepth == that.stackDepth && Objects.equals(stackStatus, that.stackStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, stackDefinition, stackState, stackDepth, stackStatus);
    }

    @Override
    public String toString() {
        return "StepStackInstance{" + "id=" + id + "name=" + name + "stackDefinition=" + stackDefinition + "stackState=" + stackState + "stackDepth=" + stackDepth + "stackStatus=" + stackStatus + "}";
    }
}