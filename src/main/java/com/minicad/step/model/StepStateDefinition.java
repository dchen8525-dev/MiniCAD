package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved STATE_DEFINITION.
 * A state definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceState defined variance state
 * @varianceConditions state variance conditions
 * @varianceActions state variance actions
 * @varianceTransitions state variance transitions
 * @varianceInitial initial variance state flag
 * @varianceStatus definition variance status
 */
/**
 * Resolved STATE_DEFINITION.
 * A state definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceState defined variance state
 * @varianceConditions state variance conditions
 * @varianceActions state variance actions
 * @varianceTransitions state variance transitions
 * @varianceInitial initial variance state flag
 * @varianceStatus definition variance status
 */
public final class StepStateDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String varianceState;
    private final List<String> varianceConditions;
    private final List<StepEntity> varianceActions;
    private final List<StepEntity> varianceTransitions;
    private final boolean varianceInitial;
    private final String varianceStatus;

    public StepStateDefinition(int id, String name, String varianceState, List<String> varianceConditions, List<StepEntity> varianceActions, List<StepEntity> varianceTransitions, boolean varianceInitial, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceState = varianceState;
        this.varianceConditions = varianceConditions == null ? null : java.util.List.copyOf(varianceConditions);
        this.varianceActions = varianceActions == null ? null : java.util.List.copyOf(varianceActions);
        this.varianceTransitions = varianceTransitions == null ? null : java.util.List.copyOf(varianceTransitions);
        this.varianceInitial = varianceInitial;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVarianceState() {
        return varianceState;
    }

    public List<String> getVarianceConditions() {
        return varianceConditions;
    }

    public List<StepEntity> getVarianceActions() {
        return varianceActions;
    }

    public List<StepEntity> getVarianceTransitions() {
        return varianceTransitions;
    }

    public boolean isVarianceInitial() {
        return varianceInitial;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStateDefinition that = (StepStateDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceState, that.varianceState) && Objects.equals(varianceConditions, that.varianceConditions) && Objects.equals(varianceActions, that.varianceActions) && Objects.equals(varianceTransitions, that.varianceTransitions) && varianceInitial == that.varianceInitial && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceState, varianceConditions, varianceActions, varianceTransitions, varianceInitial, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepStateDefinition{" + "id=" + id + "name=" + name + "varianceState=" + varianceState + "varianceConditions=" + varianceConditions + "varianceActions=" + varianceActions + "varianceTransitions=" + varianceTransitions + "varianceInitial=" + varianceInitial + "varianceStatus=" + varianceStatus + "}";
    }
}