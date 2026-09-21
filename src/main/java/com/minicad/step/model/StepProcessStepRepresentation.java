package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PROCESS_STEP_REPRESENTATION.
 * A process step representation entity.
 *
 * @param id STEP instance id
 * @param name step name
 * @param stepType step type
 * @param stepParameters step parameters
 * @param operations operations in this step
 */
public final class StepProcessStepRepresentation extends AbstractStepEntity {
    private final String stepType;
    private final List<StepEntity> stepParameters;
    private final List<StepEntity> operations;

    public StepProcessStepRepresentation(int id, String name, String stepType, List<StepEntity> stepParameters, List<StepEntity> operations) {
        super(id, name);
        this.stepType = stepType;
        this.stepParameters = stepParameters == null ? null : java.util.List.copyOf(stepParameters);
        this.operations = operations == null ? null : java.util.List.copyOf(operations);
    }

    public String getStepType() {
        return stepType;
    }

    public List<StepEntity> getStepParameters() {
        return stepParameters;
    }

    public List<StepEntity> getOperations() {
        return operations;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("stepType", stepType);
        state.put("stepParameters", stepParameters);
        state.put("operations", operations);
        return state;
    }
}
