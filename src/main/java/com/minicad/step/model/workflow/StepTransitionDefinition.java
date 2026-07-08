package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TRANSITION_DEFINITION.
 * A transition definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceFrom source variance state
 * @varianceTo target variance state
 * @varianceTrigger transition variance trigger condition
 * @varianceGuard transition variance guard condition
 * @varianceAction transition variance action
 * @varianceStatus definition variance status
 */
/**
 * Resolved TRANSITION_DEFINITION.
 * A transition definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceFrom source variance state
 * @varianceTo target variance state
 * @varianceTrigger transition variance trigger condition
 * @varianceGuard transition variance guard condition
 * @varianceAction transition variance action
 * @varianceStatus definition variance status
 */
public final class StepTransitionDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceFrom;
    private final StepEntity varianceTo;
    private final String varianceTrigger;
    private final String varianceGuard;
    private final StepEntity varianceAction;
    private final String varianceStatus;

    public StepTransitionDefinition(int id, String name, StepEntity varianceFrom, StepEntity varianceTo, String varianceTrigger, String varianceGuard, StepEntity varianceAction, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceFrom = varianceFrom;
        this.varianceTo = varianceTo;
        this.varianceTrigger = varianceTrigger;
        this.varianceGuard = varianceGuard;
        this.varianceAction = varianceAction;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceFrom() {
        return varianceFrom;
    }

    public StepEntity getVarianceTo() {
        return varianceTo;
    }

    public String getVarianceTrigger() {
        return varianceTrigger;
    }

    public String getVarianceGuard() {
        return varianceGuard;
    }

    public StepEntity getVarianceAction() {
        return varianceAction;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTransitionDefinition that = (StepTransitionDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceFrom, that.varianceFrom) && Objects.equals(varianceTo, that.varianceTo) && Objects.equals(varianceTrigger, that.varianceTrigger) && Objects.equals(varianceGuard, that.varianceGuard) && Objects.equals(varianceAction, that.varianceAction) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceFrom, varianceTo, varianceTrigger, varianceGuard, varianceAction, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepTransitionDefinition{" + "id=" + id + "name=" + name + "varianceFrom=" + varianceFrom + "varianceTo=" + varianceTo + "varianceTrigger=" + varianceTrigger + "varianceGuard=" + varianceGuard + "varianceAction=" + varianceAction + "varianceStatus=" + varianceStatus + "}";
    }
}