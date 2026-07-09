package com.minicad.step.model.fea;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved FEA_CONSTANT_FUNCTION_3D.
 * A constant scalar or vector function in 3D FEA space.
 */
/**
 * Resolved FEA_CONSTANT_FUNCTION_3D.
 * A constant scalar or vector function in 3D FEA space.
 */
public final class StepFeaConstantFunction3d implements StepEntity {
    private final int id;
    private final String name;
    private final Double value;
    private final StepEntity functionSpace;

    public StepFeaConstantFunction3d(int id, String name, Double value, StepEntity functionSpace) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.functionSpace = functionSpace;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }

    public StepEntity getFunctionSpace() {
        return functionSpace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaConstantFunction3d that = (StepFeaConstantFunction3d) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(value, that.value) && Objects.equals(functionSpace, that.functionSpace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, value, functionSpace);
    }

    @Override
    public String toString() {
        return "StepFeaConstantFunction3d{" + "id=" + id + "name=" + name + "value=" + value + "functionSpace=" + functionSpace + "}";
    }
}
