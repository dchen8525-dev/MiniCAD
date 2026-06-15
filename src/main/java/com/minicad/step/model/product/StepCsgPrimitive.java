package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal parse-only CSG primitive solid.
 *
 * @param id step id
 * @param name step label
 * @param position primitive placement
 * @param dimensions primitive numeric parameters in STEP order
 * @param entityName concrete STEP entity name
 */
/**
 * Minimal parse-only CSG primitive solid.
 *
 * @param id step id
 * @param name step label
 * @param position primitive placement
 * @param dimensions primitive numeric parameters in STEP order
 * @param entityName concrete STEP entity name
 */
public final class StepCsgPrimitive implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity position;
    private final List<Double> dimensions;
    private final String entityName;

    public StepCsgPrimitive(int id, String name, StepEntity position, List<Double> dimensions, String entityName) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.dimensions = dimensions == null ? null : java.util.List.copyOf(dimensions);
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getPosition() {
        return position;
    }

    public List<Double> getDimensions() {
        return dimensions;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity position() { return getPosition(); }
    public List<Double> dimensions() { return getDimensions(); }
    public String entityName() { return getEntityName(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCsgPrimitive that = (StepCsgPrimitive) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && Objects.equals(dimensions, that.dimensions) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, dimensions, entityName);
    }

    @Override
    public String toString() {
        return "StepCsgPrimitive{" + "id=" + id + "name=" + name + "position=" + position + "dimensions=" + dimensions + "entityName=" + entityName + "}";
    }
}
