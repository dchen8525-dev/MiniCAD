package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FEA_LINEAR_ALGEBRAIC_VECTOR.
 * A vector used in finite element linear algebra computations.
 */
public final class StepFeaLinearAlgebraicVector extends AbstractStepEntity {
    private final int size;
    private final List<Double> values;

    public StepFeaLinearAlgebraicVector(int id, String name, int size, List<Double> values) {
        super(id, name);
        this.size = size;
        this.values = values == null ? null : java.util.List.copyOf(values);
    }

    public int getSize() {
        return size;
    }

    public List<Double> getValues() {
        return values;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("size", size);
        state.put("values", values);
        return state;
    }
}
