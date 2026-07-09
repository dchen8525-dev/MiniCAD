package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved STACK_DEFINITION.
 * A stack definition entity.
 *
 * @param id STEP instance id
 * @param name stack name
 * @param stackType stack variance type
 * @param stackCapacity stack variance capacity
 * @param stackPolicy stack variance policy
 * @param stackStatus stack variance status
 */
/**
 * Resolved STACK_DEFINITION.
 * A stack definition entity.
 *
 * @param id STEP instance id
 * @param name stack name
 * @param stackType stack variance type
 * @param stackCapacity stack variance capacity
 * @param stackPolicy stack variance policy
 * @param stackStatus stack variance status
 */
public final class StepStackDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String stackType;
    private final int stackCapacity;
    private final String stackPolicy;
    private final String stackStatus;

    public StepStackDefinition(int id, String name, String stackType, int stackCapacity, String stackPolicy, String stackStatus) {
        this.id = id;
        this.name = name;
        this.stackType = stackType;
        this.stackCapacity = stackCapacity;
        this.stackPolicy = stackPolicy;
        this.stackStatus = stackStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStackType() {
        return stackType;
    }

    public int getStackCapacity() {
        return stackCapacity;
    }

    public String getStackPolicy() {
        return stackPolicy;
    }

    public String getStackStatus() {
        return stackStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStackDefinition that = (StepStackDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(stackType, that.stackType) && stackCapacity == that.stackCapacity && Objects.equals(stackPolicy, that.stackPolicy) && Objects.equals(stackStatus, that.stackStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, stackType, stackCapacity, stackPolicy, stackStatus);
    }

    @Override
    public String toString() {
        return "StepStackDefinition{" + "id=" + id + "name=" + name + "stackType=" + stackType + "stackCapacity=" + stackCapacity + "stackPolicy=" + stackPolicy + "stackStatus=" + stackStatus + "}";
    }
}