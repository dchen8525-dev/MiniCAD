package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepComplexClippingResult(
    int id,
    String name,
    StepEntity firstOperand,
    StepEntity secondOperand,
    String operator) implements StepEntity {
}
