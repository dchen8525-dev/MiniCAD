package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Minimal parse-only CSG primitive solid.
 *
 * @param id step id
 * @param name step label
 * @param position primitive placement
 * @param dimensions primitive numeric parameters in STEP order
 * @param entityName concrete STEP entity name
 */
public final class StepCsgPrimitive extends AbstractStepEntity {
    private final StepEntity position;
    private final List<Double> dimensions;
    private final String entityName;

    public StepCsgPrimitive(int id, String name, StepEntity position, List<Double> dimensions, String entityName) {
        super(id, name);
        this.position = position;
        this.dimensions = dimensions == null ? null : java.util.List.copyOf(dimensions);
        this.entityName = entityName;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "position", position, "dimensions", dimensions,
                "entityName", entityName);
    }
}
