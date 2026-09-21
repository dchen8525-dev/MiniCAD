package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COMPLEX_CLIPPING_RESULT.
 * A clipping result from a complex tree of boolean operations.
 *
 * @param id STEP instance id
 * @param name result name
 * @param firstOperand first operand
 * @param secondOperand second operand
 * @param operator boolean operator
 */
public final class StepComplexClippingResult extends AbstractStepEntity {
    private final StepEntity firstOperand;
    private final StepEntity secondOperand;
    private final String operator;

    public StepComplexClippingResult(int id, String name, StepEntity firstOperand, StepEntity secondOperand, String operator) {
        super(id, name);
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
        this.operator = operator;
    }

    public StepEntity getFirstOperand() {
        return firstOperand;
    }

    public StepEntity getSecondOperand() {
        return secondOperand;
    }

    public String getOperator() {
        return operator;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity firstOperand() { return getFirstOperand(); }
    public StepEntity secondOperand() { return getSecondOperand(); }
    public String operator() { return getOperator(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("firstOperand", firstOperand);
        state.put("secondOperand", secondOperand);
        state.put("operator", operator);
        return state;
    }
}
