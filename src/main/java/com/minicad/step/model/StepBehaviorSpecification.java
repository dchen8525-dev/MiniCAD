package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved BEHAVIOR_SPECIFICATION.
 * A behavior specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceBehavior specified variance behavior
 * @varianceConditions behavior variance conditions
 * @varianceActions behavior variance actions
 * @varianceEvents behavior variance triggering events
 * @variancePriority behavior variance priority
 * @varianceStatus specification variance status
 */
/**
 * Resolved BEHAVIOR_SPECIFICATION.
 * A behavior specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceBehavior specified variance behavior
 * @varianceConditions behavior variance conditions
 * @varianceActions behavior variance actions
 * @varianceEvents behavior variance triggering events
 * @variancePriority behavior variance priority
 * @varianceStatus specification variance status
 */
public final class StepBehaviorSpecification implements StepEntity {
    private final int id;
    private final String name;
    private final String varianceBehavior;
    private final List<String> varianceConditions;
    private final List<StepEntity> varianceActions;
    private final List<String> varianceEvents;
    private final int variancePriority;
    private final String varianceStatus;

    public StepBehaviorSpecification(int id, String name, String varianceBehavior, List<String> varianceConditions, List<StepEntity> varianceActions, List<String> varianceEvents, int variancePriority, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceBehavior = varianceBehavior;
        this.varianceConditions = varianceConditions == null ? null : java.util.List.copyOf(varianceConditions);
        this.varianceActions = varianceActions == null ? null : java.util.List.copyOf(varianceActions);
        this.varianceEvents = varianceEvents == null ? null : java.util.List.copyOf(varianceEvents);
        this.variancePriority = variancePriority;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVarianceBehavior() {
        return varianceBehavior;
    }

    public List<String> getVarianceConditions() {
        return varianceConditions;
    }

    public List<StepEntity> getVarianceActions() {
        return varianceActions;
    }

    public List<String> getVarianceEvents() {
        return varianceEvents;
    }

    public int getVariancePriority() {
        return variancePriority;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBehaviorSpecification that = (StepBehaviorSpecification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceBehavior, that.varianceBehavior) && Objects.equals(varianceConditions, that.varianceConditions) && Objects.equals(varianceActions, that.varianceActions) && Objects.equals(varianceEvents, that.varianceEvents) && variancePriority == that.variancePriority && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceBehavior, varianceConditions, varianceActions, varianceEvents, variancePriority, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepBehaviorSpecification{" + "id=" + id + "name=" + name + "varianceBehavior=" + varianceBehavior + "varianceConditions=" + varianceConditions + "varianceActions=" + varianceActions + "varianceEvents=" + varianceEvents + "variancePriority=" + variancePriority + "varianceStatus=" + varianceStatus + "}";
    }
}