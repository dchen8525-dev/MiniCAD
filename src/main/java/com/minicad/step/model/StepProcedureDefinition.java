package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PROCEDURE_DEFINITION.
 * A procedure definition entity.
 *
 * @param id STEP instance id
 * @param name procedure name
 * @param procedureType procedure variance type
 * @param procedureSteps procedure variance steps
 * @param procedureInputs procedure variance required inputs
 * @param procedureOutputs procedure variance expected outputs
 * @param procedureStatus procedure variance status
 */
public final class StepProcedureDefinition extends AbstractStepEntity {
    private final String procedureType;
    private final List<String> procedureSteps;
    private final List<String> procedureInputs;
    private final List<String> procedureOutputs;
    private final String procedureStatus;

    public StepProcedureDefinition(int id, String name, String procedureType, List<String> procedureSteps, List<String> procedureInputs, List<String> procedureOutputs, String procedureStatus) {
        super(id, name);
        this.procedureType = procedureType;
        this.procedureSteps = procedureSteps == null ? null : java.util.List.copyOf(procedureSteps);
        this.procedureInputs = procedureInputs == null ? null : java.util.List.copyOf(procedureInputs);
        this.procedureOutputs = procedureOutputs == null ? null : java.util.List.copyOf(procedureOutputs);
        this.procedureStatus = procedureStatus;
    }

    public String getProcedureType() {
        return procedureType;
    }

    public List<String> getProcedureSteps() {
        return procedureSteps;
    }

    public List<String> getProcedureInputs() {
        return procedureInputs;
    }

    public List<String> getProcedureOutputs() {
        return procedureOutputs;
    }

    public String getProcedureStatus() {
        return procedureStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("procedureType", procedureType);
        state.put("procedureSteps", procedureSteps);
        state.put("procedureInputs", procedureInputs);
        state.put("procedureOutputs", procedureOutputs);
        state.put("procedureStatus", procedureStatus);
        return state;
    }
}
