package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal CSG_SOLID parse-only solid model.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param treeRootExpression CSG tree root expression
 */
/**
 * Minimal CSG_SOLID parse-only solid model.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param treeRootExpression CSG tree root expression
 */
public final class StepCsgSolid implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity treeRootExpression;

    public StepCsgSolid(int id, String name, StepEntity treeRootExpression) {
        this.id = id;
        this.name = name;
        this.treeRootExpression = treeRootExpression;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getTreeRootExpression() {
        return treeRootExpression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCsgSolid that = (StepCsgSolid) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(treeRootExpression, that.treeRootExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, treeRootExpression);
    }

    @Override
    public String toString() {
        return "StepCsgSolid{" + "id=" + id + "name=" + name + "treeRootExpression=" + treeRootExpression + "}";
    }
}
