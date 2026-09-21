package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FEA_LINEAR_ALGEBRAIC_MATRIX.
 * A matrix used in finite element linear algebra computations.
 */
public final class StepFeaLinearAlgebraicMatrix extends AbstractStepEntity {
    private final int rows;
    private final int cols;
    private final List<Double> values;

    public StepFeaLinearAlgebraicMatrix(int id, String name, int rows, int cols, List<Double> values) {
        super(id, name);
        this.rows = rows;
        this.cols = cols;
        this.values = values == null ? null : java.util.List.copyOf(values);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public List<Double> getValues() {
        return values;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("rows", rows);
        state.put("cols", cols);
        state.put("values", values);
        return state;
    }
}
