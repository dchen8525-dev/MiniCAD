package com.minicad.step.model;

import java.util.Map;

/**
 * UNARY_GENERIC_EXPRESSION entity model (ABSTRACT SUPERTYPE).
 * Base expression with single operand.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param operand reference to generic_expression
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepUnaryGenericExpression extends AbstractStepEntity {
    private final Object operand; // generic_expression reference
    private final String entityName;

    public StepUnaryGenericExpression(
        int id,
        String name,
        Object operand,
        String entityName) {
        super(id, name);
        this.operand = operand;
        this.entityName = entityName;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "operand", operand, "entityName", entityName);
    }
}
