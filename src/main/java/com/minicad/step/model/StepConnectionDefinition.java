package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CONNECTION_DEFINITION.
 * A connection definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceConnection defined variance connection
 * @varianceFrom source variance component
 * @varianceTo target variance component
 * @varianceType connection variance type
 * @varianceInterface connection variance interface specification
 * @varianceStatus definition variance status
 */
/**
 * Resolved CONNECTION_DEFINITION.
 * A connection definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceConnection defined variance connection
 * @varianceFrom source variance component
 * @varianceTo target variance component
 * @varianceType connection variance type
 * @varianceInterface connection variance interface specification
 * @varianceStatus definition variance status
 */
public final class StepConnectionDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceConnection;
    private final StepEntity varianceFrom;
    private final StepEntity varianceTo;
    private final String varianceType;
    private final StepEntity varianceInterface;
    private final String varianceStatus;

    public StepConnectionDefinition(int id, String name, StepEntity varianceConnection, StepEntity varianceFrom, StepEntity varianceTo, String varianceType, StepEntity varianceInterface, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceConnection = varianceConnection;
        this.varianceFrom = varianceFrom;
        this.varianceTo = varianceTo;
        this.varianceType = varianceType;
        this.varianceInterface = varianceInterface;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceConnection() {
        return varianceConnection;
    }

    public StepEntity getVarianceFrom() {
        return varianceFrom;
    }

    public StepEntity getVarianceTo() {
        return varianceTo;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public StepEntity getVarianceInterface() {
        return varianceInterface;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConnectionDefinition that = (StepConnectionDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceConnection, that.varianceConnection) && Objects.equals(varianceFrom, that.varianceFrom) && Objects.equals(varianceTo, that.varianceTo) && Objects.equals(varianceType, that.varianceType) && Objects.equals(varianceInterface, that.varianceInterface) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceConnection, varianceFrom, varianceTo, varianceType, varianceInterface, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepConnectionDefinition{" + "id=" + id + "name=" + name + "varianceConnection=" + varianceConnection + "varianceFrom=" + varianceFrom + "varianceTo=" + varianceTo + "varianceType=" + varianceType + "varianceInterface=" + varianceInterface + "varianceStatus=" + varianceStatus + "}";
    }
}