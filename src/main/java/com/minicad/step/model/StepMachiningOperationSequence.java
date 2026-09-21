package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MACHINING_OPERATION_SEQUENCE.
 * A machining operation sequence entity.
 *
 * @param id STEP instance id
 * @param name sequence name
 * @param operations list of machining operations
 * @param sequenceType sequence type classification
 */
public final class StepMachiningOperationSequence extends AbstractStepEntity {
    private final List<StepEntity> operations;
    private final String sequenceType;

    public StepMachiningOperationSequence(int id, String name, List<StepEntity> operations, String sequenceType) {
        super(id, name);
        this.operations = operations == null ? null : java.util.List.copyOf(operations);
        this.sequenceType = sequenceType;
    }

    public List<StepEntity> getOperations() {
        return operations;
    }

    public String getSequenceType() {
        return sequenceType;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("operations", operations);
        state.put("sequenceType", sequenceType);
        return state;
    }
}
