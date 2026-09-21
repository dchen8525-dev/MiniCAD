package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepBehaviorSpecification extends AbstractStepEntity {
    private final String varianceBehavior;
    private final List<String> varianceConditions;
    private final List<StepEntity> varianceActions;
    private final List<String> varianceEvents;
    private final int variancePriority;
    private final String varianceStatus;

    public StepBehaviorSpecification(int id, String name, String varianceBehavior, List<String> varianceConditions, List<StepEntity> varianceActions, List<String> varianceEvents, int variancePriority, String varianceStatus) {
        super(id, name);
        this.varianceBehavior = varianceBehavior;
        this.varianceConditions = varianceConditions == null ? null : java.util.List.copyOf(varianceConditions);
        this.varianceActions = varianceActions == null ? null : java.util.List.copyOf(varianceActions);
        this.varianceEvents = varianceEvents == null ? null : java.util.List.copyOf(varianceEvents);
        this.variancePriority = variancePriority;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceBehavior", varianceBehavior);
        state.put("varianceConditions", varianceConditions);
        state.put("varianceActions", varianceActions);
        state.put("varianceEvents", varianceEvents);
        state.put("variancePriority", variancePriority);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
