package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal parse-only conic curve for PARABOLA and HYPERBOLA.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param position curve placement
 * @param parameters numeric conic parameters
 * @param entityName concrete STEP entity name
 */
/**
 * Minimal parse-only conic curve for PARABOLA and HYPERBOLA.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param position curve placement
 * @param parameters numeric conic parameters
 * @param entityName concrete STEP entity name
 */
public final class StepConicCurve implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity position;
    private final List<Double> parameters;
    private final String entityName;

    public StepConicCurve(int id, String name, StepEntity position, List<Double> parameters, String entityName) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.parameters = parameters == null ? null : java.util.List.copyOf(parameters);
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

    public List<Double> getParameters() {
        return parameters;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity position() { return getPosition(); }
    public List<Double> parameters() { return getParameters(); }
    public String entityName() { return getEntityName(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConicCurve that = (StepConicCurve) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && Objects.equals(parameters, that.parameters) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, parameters, entityName);
    }

    @Override
    public String toString() {
        return "StepConicCurve{" + "id=" + id + "name=" + name + "position=" + position + "parameters=" + parameters + "entityName=" + entityName + "}";
    }
}
