package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepComplexClippingResult implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity firstOperand;
    private final StepEntity secondOperand;
    private final String operator;

    public StepComplexClippingResult(int id, String name, StepEntity firstOperand, StepEntity secondOperand, String operator) {
        this.id = id;
        this.name = name;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
        this.operator = operator;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepComplexClippingResult that = (StepComplexClippingResult) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(firstOperand, that.firstOperand) && Objects.equals(secondOperand, that.secondOperand) && Objects.equals(operator, that.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, firstOperand, secondOperand, operator);
    }

    @Override
    public String toString() {
        return "StepComplexClippingResult{" + "id=" + id + "name=" + name + "firstOperand=" + firstOperand + "secondOperand=" + secondOperand + "operator=" + operator + "}";
    }
}
