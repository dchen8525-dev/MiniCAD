package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.unit.StepGlobalUnitAssignedContext;
import java.util.Objects;
/**
 * Minimal geometric representation context.
 *
 * @param id STEP instance id
 * @param coordinateSpaceDimension coordinate space dimension
 * @param contextIdentifier context identifier
 * @param contextType context type
 * @param globalUnitAssignedContext optional global unit assignments from the same complex entity
 * @param globalUncertaintyAssignedContext optional global uncertainty assignments from the same complex entity
 */
/**
 * Minimal geometric representation context.
 *
 * @param id STEP instance id
 * @param coordinateSpaceDimension coordinate space dimension
 * @param contextIdentifier context identifier
 * @param contextType context type
 * @param globalUnitAssignedContext optional global unit assignments from the same complex entity
 * @param globalUncertaintyAssignedContext optional global uncertainty assignments from the same complex entity
 */
public final class StepGeometricRepresentationContext implements StepEntity {
    private final int id;
    private final int coordinateSpaceDimension;
    private final String contextIdentifier;
    private final String contextType;
    private final StepGlobalUnitAssignedContext globalUnitAssignedContext;
    private final StepGlobalUncertaintyAssignedContext globalUncertaintyAssignedContext;

    public StepGeometricRepresentationContext(int id, int coordinateSpaceDimension, String contextIdentifier, String contextType, StepGlobalUnitAssignedContext globalUnitAssignedContext, StepGlobalUncertaintyAssignedContext globalUncertaintyAssignedContext) {
        this.id = id;
        this.coordinateSpaceDimension = coordinateSpaceDimension;
        this.contextIdentifier = contextIdentifier;
        this.contextType = contextType;
        this.globalUnitAssignedContext = globalUnitAssignedContext;
        this.globalUncertaintyAssignedContext = globalUncertaintyAssignedContext;
    }

    public int getId() {
        return id;
    }

    public int getCoordinateSpaceDimension() {
        return coordinateSpaceDimension;
    }

    public String getContextIdentifier() {
        return contextIdentifier;
    }

    public String getContextType() {
        return contextType;
    }

    public StepGlobalUnitAssignedContext getGlobalUnitAssignedContext() {
        return globalUnitAssignedContext;
    }

    public StepGlobalUncertaintyAssignedContext getGlobalUncertaintyAssignedContext() {
        return globalUncertaintyAssignedContext;
    }

    // Record-style accessors
    public StepGlobalUnitAssignedContext globalUnitAssignedContext() { return getGlobalUnitAssignedContext(); }
    public StepGlobalUncertaintyAssignedContext globalUncertaintyAssignedContext() { return getGlobalUncertaintyAssignedContext(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGeometricRepresentationContext that = (StepGeometricRepresentationContext) o;
        return id == that.id && coordinateSpaceDimension == that.coordinateSpaceDimension && Objects.equals(contextIdentifier, that.contextIdentifier) && Objects.equals(contextType, that.contextType) && Objects.equals(globalUnitAssignedContext, that.globalUnitAssignedContext) && Objects.equals(globalUncertaintyAssignedContext, that.globalUncertaintyAssignedContext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, coordinateSpaceDimension, contextIdentifier, contextType, globalUnitAssignedContext, globalUncertaintyAssignedContext);
    }

    @Override
    public String toString() {
        return "StepGeometricRepresentationContext{" + "id=" + id + "coordinateSpaceDimension=" + coordinateSpaceDimension + "contextIdentifier=" + contextIdentifier + "contextType=" + contextType + "globalUnitAssignedContext=" + globalUnitAssignedContext + "globalUncertaintyAssignedContext=" + globalUncertaintyAssignedContext + "}";
    }
}
