package com.minicad.step.model.expression;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * MULTIPLE_ARITY_GENERIC_EXPRESSION entity model (ABSTRACT SUPERTYPE).
 * Base expression with variable number of operands.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param operands LIST [1:?] OF generic_expression references
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepMultipleArityGenericExpression implements StepEntity {
    private final int id;
    private final String name;
    private final List<Object> operands; // LIST [1:?] OF generic_expression references
    private final String entityName;

    public StepMultipleArityGenericExpression(
        int id,
        String name,
        List<Object> operands,
        String entityName) {
        this.id = id;
        this.name = name;
        this.operands = operands;
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

    public List<Object> getOperands() {
        return operands;
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMultipleArityGenericExpression that = (StepMultipleArityGenericExpression) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "StepMultipleArityGenericExpression{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", operands=" + operands +
            ", entityName='" + entityName + '\'' +
            '}';
    }
}