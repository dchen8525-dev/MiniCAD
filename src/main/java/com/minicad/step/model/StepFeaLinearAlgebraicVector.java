package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FEA_LINEAR_ALGEBRAIC_VECTOR.
 * A vector used in finite element linear algebra computations.
 */
/**
 * Resolved FEA_LINEAR_ALGEBRAIC_VECTOR.
 * A vector used in finite element linear algebra computations.
 */
public final class StepFeaLinearAlgebraicVector implements StepEntity {
    private final int id;
    private final String name;
    private final int size;
    private final List<Double> values;

    public StepFeaLinearAlgebraicVector(int id, String name, int size, List<Double> values) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.values = values == null ? null : java.util.List.copyOf(values);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public List<Double> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaLinearAlgebraicVector that = (StepFeaLinearAlgebraicVector) o;
        return id == that.id && Objects.equals(name, that.name) && size == that.size && Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, size, values);
    }

    @Override
    public String toString() {
        return "StepFeaLinearAlgebraicVector{" + "id=" + id + "name=" + name + "size=" + size + "values=" + values + "}";
    }
}
