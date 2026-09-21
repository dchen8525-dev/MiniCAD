package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FORMULA_DEFINITION.
 * A formula definition entity.
 *
 * @param id STEP instance id
 * @param name formula name
 * @param formulaType formula variance type
 * @param formulaExpression formula variance expression
 * @param formulaVariables formula variance variables used
 * @param formulaUnit formula variance result unit
 * @param formulaStatus formula variance status
 */
public final class StepFormulaDefinition extends AbstractStepEntity {
    private final String formulaType;
    private final String formulaExpression;
    private final List<String> formulaVariables;
    private final StepEntity formulaUnit;
    private final String formulaStatus;

    public StepFormulaDefinition(int id, String name, String formulaType, String formulaExpression, List<String> formulaVariables, StepEntity formulaUnit, String formulaStatus) {
        super(id, name);
        this.formulaType = formulaType;
        this.formulaExpression = formulaExpression;
        this.formulaVariables = formulaVariables == null ? null : java.util.List.copyOf(formulaVariables);
        this.formulaUnit = formulaUnit;
        this.formulaStatus = formulaStatus;
    }

    public String getFormulaType() {
        return formulaType;
    }

    public String getFormulaExpression() {
        return formulaExpression;
    }

    public List<String> getFormulaVariables() {
        return formulaVariables;
    }

    public StepEntity getFormulaUnit() {
        return formulaUnit;
    }

    public String getFormulaStatus() {
        return formulaStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("formulaType", formulaType);
        state.put("formulaExpression", formulaExpression);
        state.put("formulaVariables", formulaVariables);
        state.put("formulaUnit", formulaUnit);
        state.put("formulaStatus", formulaStatus);
        return state;
    }
}
