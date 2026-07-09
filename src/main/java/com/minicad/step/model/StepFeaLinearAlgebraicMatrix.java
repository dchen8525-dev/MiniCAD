package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FEA_LINEAR_ALGEBRAIC_MATRIX.
 * A matrix used in finite element linear algebra computations.
 */
/**
 * Resolved FEA_LINEAR_ALGEBRAIC_MATRIX.
 * A matrix used in finite element linear algebra computations.
 */
public final class StepFeaLinearAlgebraicMatrix implements StepEntity {
    private final int id;
    private final String name;
    private final int rows;
    private final int cols;
    private final List<Double> values;

    public StepFeaLinearAlgebraicMatrix(int id, String name, int rows, int cols, List<Double> values) {
        this.id = id;
        this.name = name;
        this.rows = rows;
        this.cols = cols;
        this.values = values == null ? null : java.util.List.copyOf(values);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaLinearAlgebraicMatrix that = (StepFeaLinearAlgebraicMatrix) o;
        return id == that.id && Objects.equals(name, that.name) && rows == that.rows && cols == that.cols && Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, rows, cols, values);
    }

    @Override
    public String toString() {
        return "StepFeaLinearAlgebraicMatrix{" + "id=" + id + "name=" + name + "rows=" + rows + "cols=" + cols + "values=" + values + "}";
    }
}
