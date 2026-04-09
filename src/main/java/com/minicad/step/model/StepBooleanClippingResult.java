package com.minicad.step.model;

/**
 * Minimal BOOLEAN_CLIPPING_RESULT.
 *
 * @param id step id
 * @param name inherited representation-item name
 * @param operator boolean operator enum token
 * @param firstOperand first boolean operand
 * @param secondOperand second boolean operand
 */
public record StepBooleanClippingResult(
    int id, String name, String operator, StepEntity firstOperand, StepEntity secondOperand)
    implements StepEntity {}
