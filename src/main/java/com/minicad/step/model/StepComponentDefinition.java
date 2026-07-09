package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COMPONENT_DEFINITION.
 * A component definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceComponent defined variance component
 * @varianceFunction component variance function
 * @varianceInterface component variance interface specification
 * @varianceDependencies component variance dependencies
 * @varianceProperties component variance properties
 * @varianceStatus definition variance status
 */
/**
 * Resolved COMPONENT_DEFINITION.
 * A component definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceComponent defined variance component
 * @varianceFunction component variance function
 * @varianceInterface component variance interface specification
 * @varianceDependencies component variance dependencies
 * @varianceProperties component variance properties
 * @varianceStatus definition variance status
 */
public final class StepComponentDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceComponent;
    private final String varianceFunction;
    private final StepEntity varianceInterface;
    private final List<StepEntity> varianceDependencies;
    private final List<StepEntity> varianceProperties;
    private final String varianceStatus;

    public StepComponentDefinition(int id, String name, StepEntity varianceComponent, String varianceFunction, StepEntity varianceInterface, List<StepEntity> varianceDependencies, List<StepEntity> varianceProperties, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceComponent = varianceComponent;
        this.varianceFunction = varianceFunction;
        this.varianceInterface = varianceInterface;
        this.varianceDependencies = varianceDependencies == null ? null : java.util.List.copyOf(varianceDependencies);
        this.varianceProperties = varianceProperties == null ? null : java.util.List.copyOf(varianceProperties);
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceComponent() {
        return varianceComponent;
    }

    public String getVarianceFunction() {
        return varianceFunction;
    }

    public StepEntity getVarianceInterface() {
        return varianceInterface;
    }

    public List<StepEntity> getVarianceDependencies() {
        return varianceDependencies;
    }

    public List<StepEntity> getVarianceProperties() {
        return varianceProperties;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepComponentDefinition that = (StepComponentDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceComponent, that.varianceComponent) && Objects.equals(varianceFunction, that.varianceFunction) && Objects.equals(varianceInterface, that.varianceInterface) && Objects.equals(varianceDependencies, that.varianceDependencies) && Objects.equals(varianceProperties, that.varianceProperties) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceComponent, varianceFunction, varianceInterface, varianceDependencies, varianceProperties, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepComponentDefinition{" + "id=" + id + "name=" + name + "varianceComponent=" + varianceComponent + "varianceFunction=" + varianceFunction + "varianceInterface=" + varianceInterface + "varianceDependencies=" + varianceDependencies + "varianceProperties=" + varianceProperties + "varianceStatus=" + varianceStatus + "}";
    }
}