package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal BOOLEAN_RESULT.
 *
 * @param id step id
 * @param name inherited representation-item name
 * @param operator boolean operator enum token
 * @param firstOperand first boolean operand
 * @param secondOperand second boolean operand
 */
public final class StepBooleanResult extends AbstractStepEntity {
    private final String operator;
    private final StepEntity firstOperand;
    private final StepEntity secondOperand;

    public StepBooleanResult(int id, String name, String operator, StepEntity firstOperand, StepEntity secondOperand) {
        super(id, name);
        this.operator = operator;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }

    public String getOperator() {
        return operator;
    }

    public StepEntity getFirstOperand() {
        return firstOperand;
    }

    public StepEntity getSecondOperand() {
        return secondOperand;
    }

    // Record-style accessors for compatibility
    public int id() { return getId(); }
    public String name() { return getName(); }
    public String operator() { return getOperator(); }
    public StepEntity firstOperand() { return getFirstOperand(); }
    public StepEntity secondOperand() { return getSecondOperand(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("operator", operator);
        state.put("firstOperand", firstOperand);
        state.put("secondOperand", secondOperand);
        return state;
    }
}
