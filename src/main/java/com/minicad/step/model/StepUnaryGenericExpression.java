package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;

/**
 * UNARY_GENERIC_EXPRESSION entity model (ABSTRACT SUPERTYPE).
 * Base expression with single operand.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param operand reference to generic_expression
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepUnaryGenericExpression implements StepEntity {
    private final int id;
    private final String name;
    private final Object operand; // generic_expression reference
    private final String entityName;

    public StepUnaryGenericExpression(
        int id,
        String name,
        Object operand,
        String entityName) {
        this.id = id;
        this.name = name;
        this.operand = operand;
        this.entityName = entityName;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Object getOperand() {
        return operand;
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepUnaryGenericExpression that = (StepUnaryGenericExpression) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "StepUnaryGenericExpression{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", operand=" + operand +
            ", entityName='" + entityName + '\'' +
            '}';
    }
}