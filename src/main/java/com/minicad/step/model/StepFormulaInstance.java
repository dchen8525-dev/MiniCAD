package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved FORMULA_INSTANCE.
 * A formula instance entity.
 *
 * @param id STEP instance id
 * @param name formula instance name
 * @param formulaDefinition formula variance definition reference
 * @param formulaResult formula variance result value
 * @param formulaStatus formula variance status
 */
public final class StepFormulaInstance extends AbstractStepEntity {
    private final StepEntity formulaDefinition;
    private final double formulaResult;
    private final String formulaStatus;

    public StepFormulaInstance(int id, String name, StepEntity formulaDefinition, double formulaResult, String formulaStatus) {
        super(id, name);
        this.formulaDefinition = formulaDefinition;
        this.formulaResult = formulaResult;
        this.formulaStatus = formulaStatus;
    }

    public StepEntity getFormulaDefinition() {
        return formulaDefinition;
    }

    public double getFormulaResult() {
        return formulaResult;
    }

    public String getFormulaStatus() {
        return formulaStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("formulaDefinition", formulaDefinition);
        state.put("formulaResult", formulaResult);
        state.put("formulaStatus", formulaStatus);
        return state;
    }
}
