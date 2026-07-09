package com.minicad.step.model.product;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal BOOLEAN_CLIPPING_RESULT.
 *
 * @param id step id
 * @param name inherited representation-item name
 * @param operator boolean operator enum token
 * @param firstOperand first boolean operand
 * @param secondOperand second boolean operand
 */
/**
 * Minimal BOOLEAN_CLIPPING_RESULT.
 *
 * @param id step id
 * @param name inherited representation-item name
 * @param operator boolean operator enum token
 * @param firstOperand first boolean operand
 * @param secondOperand second boolean operand
 */
public final class StepBooleanClippingResult implements StepEntity {
    private final int id;
    private final String name;
    private final String operator;
    private final StepEntity firstOperand;
    private final StepEntity secondOperand;

    public StepBooleanClippingResult(int id, String name, String operator, StepEntity firstOperand, StepEntity secondOperand) {
        this.id = id;
        this.name = name;
        this.operator = operator;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public String operator() { return getOperator(); }
    public StepEntity firstOperand() { return getFirstOperand(); }
    public StepEntity secondOperand() { return getSecondOperand(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBooleanClippingResult that = (StepBooleanClippingResult) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(operator, that.operator) && Objects.equals(firstOperand, that.firstOperand) && Objects.equals(secondOperand, that.secondOperand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, operator, firstOperand, secondOperand);
    }

    @Override
    public String toString() {
        return "StepBooleanClippingResult{" + "id=" + id + "name=" + name + "operator=" + operator + "firstOperand=" + firstOperand + "secondOperand=" + secondOperand + "}";
    }
}
