package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved RESPONSIBILITY_ASSIGNMENT.
 * A responsibility assignment entity.
 *
 * @param id STEP instance id
 * @param name assignment name
 * @varianceTask assigned variance task/activity
 * @variancePerson responsible variance person
 * @varianceRole assigned variance role
 * @varianceAuthority assigned variance authority level
 * @varianceStart assignment variance start date
 * @varianceEnd assignment variance end date
 * @varianceStatus assignment variance status
 */
public final class StepResponsibilityAssignment extends AbstractStepEntity {
    private final StepEntity varianceTask;
    private final StepEntity variancePerson;
    private final StepEntity varianceRole;
    private final int varianceAuthority;
    private final StepEntity varianceStart;
    private final StepEntity varianceEnd;
    private final String varianceStatus;

    public StepResponsibilityAssignment(int id, String name, StepEntity varianceTask, StepEntity variancePerson, StepEntity varianceRole, int varianceAuthority, StepEntity varianceStart, StepEntity varianceEnd, String varianceStatus) {
        super(id, name);
        this.varianceTask = varianceTask;
        this.variancePerson = variancePerson;
        this.varianceRole = varianceRole;
        this.varianceAuthority = varianceAuthority;
        this.varianceStart = varianceStart;
        this.varianceEnd = varianceEnd;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceTask() {
        return varianceTask;
    }

    public StepEntity getVariancePerson() {
        return variancePerson;
    }

    public StepEntity getVarianceRole() {
        return varianceRole;
    }

    public int getVarianceAuthority() {
        return varianceAuthority;
    }

    public StepEntity getVarianceStart() {
        return varianceStart;
    }

    public StepEntity getVarianceEnd() {
        return varianceEnd;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceTask", varianceTask);
        state.put("variancePerson", variancePerson);
        state.put("varianceRole", varianceRole);
        state.put("varianceAuthority", varianceAuthority);
        state.put("varianceStart", varianceStart);
        state.put("varianceEnd", varianceEnd);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
