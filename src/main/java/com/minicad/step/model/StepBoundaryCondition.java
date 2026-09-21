package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved BOUNDARY_CONDITION.
 * A boundary condition entity.
 *
 * @param id STEP instance id
 * @param name boundary condition name
 * @param conditionType condition variance type
 * @param conditionLocation condition variance location reference
 * @param conditionConstraints condition variance constraints
 * @param conditionStatus condition variance status
 */
public final class StepBoundaryCondition extends AbstractStepEntity {
    private final String conditionType;
    private final StepEntity conditionLocation;
    private final List<String> conditionConstraints;
    private final String conditionStatus;

    public StepBoundaryCondition(int id, String name, String conditionType, StepEntity conditionLocation, List<String> conditionConstraints, String conditionStatus) {
        super(id, name);
        this.conditionType = conditionType;
        this.conditionLocation = conditionLocation;
        this.conditionConstraints = conditionConstraints == null ? null : java.util.List.copyOf(conditionConstraints);
        this.conditionStatus = conditionStatus;
    }

    public String getConditionType() {
        return conditionType;
    }

    public StepEntity getConditionLocation() {
        return conditionLocation;
    }

    public List<String> getConditionConstraints() {
        return conditionConstraints;
    }

    public String getConditionStatus() {
        return conditionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("conditionType", conditionType);
        state.put("conditionLocation", conditionLocation);
        state.put("conditionConstraints", conditionConstraints);
        state.put("conditionStatus", conditionStatus);
        return state;
    }
}
