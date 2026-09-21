package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PROCEDURE_INSTANCE.
 * A procedure instance entity.
 *
 * @param id STEP instance id
 * @param name procedure instance name
 * @param procedureDefinition procedure variance definition reference
 * @param procedureState procedure variance state
 * @param procedureCurrentStep procedure variance current step
 * @param procedureCompletedSteps procedure variance completed steps
 * @param procedureStatus procedure variance status
 */
public final class StepProcedureInstance extends AbstractStepEntity {
    private final StepEntity procedureDefinition;
    private final String procedureState;
    private final int procedureCurrentStep;
    private final List<Integer> procedureCompletedSteps;
    private final String procedureStatus;

    public StepProcedureInstance(int id, String name, StepEntity procedureDefinition, String procedureState, int procedureCurrentStep, List<Integer> procedureCompletedSteps, String procedureStatus) {
        super(id, name);
        this.procedureDefinition = procedureDefinition;
        this.procedureState = procedureState;
        this.procedureCurrentStep = procedureCurrentStep;
        this.procedureCompletedSteps = procedureCompletedSteps == null ? null : java.util.List.copyOf(procedureCompletedSteps);
        this.procedureStatus = procedureStatus;
    }

    public StepEntity getProcedureDefinition() {
        return procedureDefinition;
    }

    public String getProcedureState() {
        return procedureState;
    }

    public int getProcedureCurrentStep() {
        return procedureCurrentStep;
    }

    public List<Integer> getProcedureCompletedSteps() {
        return procedureCompletedSteps;
    }

    public String getProcedureStatus() {
        return procedureStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("procedureDefinition", procedureDefinition);
        state.put("procedureState", procedureState);
        state.put("procedureCurrentStep", procedureCurrentStep);
        state.put("procedureCompletedSteps", procedureCompletedSteps);
        state.put("procedureStatus", procedureStatus);
        return state;
    }
}
