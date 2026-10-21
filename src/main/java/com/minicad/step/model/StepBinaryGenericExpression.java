package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * BINARY_GENERIC_EXPRESSION entity model (ABSTRACT SUPERTYPE).
 * Base expression with exactly two operands.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param operands LIST [2:2] OF generic_expression references
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepBinaryGenericExpression extends AbstractStepEntity {
    private final List<String> operands; // LIST [2:2] OF generic_expression literal names
    private final String entityName;

    public StepBinaryGenericExpression(
        int id,
        String name,
        List<String> operands,
        String entityName) {
        super(id, name);
        this.operands = operands;
        this.entityName = entityName;
    }

    public List<String> getOperands() {
        return operands;
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("operands", operands);
        state.put("entityName", entityName);
        return state;
    }
}
