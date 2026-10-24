package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepTransitionDefinition extends AbstractStepEntity {
    private final StepEntity varianceFrom;
    private final StepEntity varianceTo;
    private final String varianceTrigger;
    private final String varianceGuard;
    private final StepEntity varianceAction;
    private final String varianceStatus;

    public StepTransitionDefinition(int id, String name, StepEntity varianceFrom, StepEntity varianceTo, String varianceTrigger, String varianceGuard, StepEntity varianceAction, String varianceStatus) {
        super(id, name);
        this.varianceFrom = varianceFrom;
        this.varianceTo = varianceTo;
        this.varianceTrigger = varianceTrigger;
        this.varianceGuard = varianceGuard;
        this.varianceAction = varianceAction;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "varianceFrom", varianceFrom, "varianceTo", varianceTo,
                "varianceTrigger", varianceTrigger, "varianceGuard", varianceGuard,
                "varianceAction", varianceAction, "varianceStatus", varianceStatus);
    }
}
