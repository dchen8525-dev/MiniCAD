package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved NODE.
 * A finite element analysis node (grid point).
 */
/**
 * Resolved NODE.
 * A finite element analysis node (grid point).
 */
public final class StepFeaNode implements StepEntity {
    private final int id;
    private final String name;
    private final double x;
    private final double y;
    private final double z;

    public StepFeaNode(int id, String name, double x, double y, double z) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaNode that = (StepFeaNode) o;
        return id == that.id && Objects.equals(name, that.name) && x == that.x && y == that.y && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, x, y, z);
    }

    @Override
    public String toString() {
        return "StepFeaNode{" + "id=" + id + "name=" + name + "x=" + x + "y=" + y + "z=" + z + "}";
    }
}
