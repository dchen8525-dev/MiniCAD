package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

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
public final class StepGeometricRepresentationContext extends AbstractStepEntity {
    private final int coordinateSpaceDimension;
    private final String contextIdentifier;
    private final String contextType;
    private final StepGlobalUnitAssignedContext globalUnitAssignedContext;
    private final StepGlobalUncertaintyAssignedContext globalUncertaintyAssignedContext;

    public StepGeometricRepresentationContext(int id, int coordinateSpaceDimension, String contextIdentifier, String contextType, StepGlobalUnitAssignedContext globalUnitAssignedContext, StepGlobalUncertaintyAssignedContext globalUncertaintyAssignedContext) {
        super(id, "");
        this.coordinateSpaceDimension = coordinateSpaceDimension;
        this.contextIdentifier = contextIdentifier;
        this.contextType = contextType;
        this.globalUnitAssignedContext = globalUnitAssignedContext;
        this.globalUncertaintyAssignedContext = globalUncertaintyAssignedContext;
    }

    public String getName() {
        return contextIdentifier != null ? contextIdentifier : "";
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
    public int coordinateSpaceDimension() { return getCoordinateSpaceDimension(); }
    public String contextIdentifier() { return getContextIdentifier(); }
    public String contextType() { return getContextType(); }
    public StepGlobalUnitAssignedContext globalUnitAssignedContext() { return getGlobalUnitAssignedContext(); }
    public StepGlobalUncertaintyAssignedContext globalUncertaintyAssignedContext() { return getGlobalUncertaintyAssignedContext(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("coordinateSpaceDimension", coordinateSpaceDimension);
        state.put("contextIdentifier", contextIdentifier);
        state.put("contextType", contextType);
        state.put("globalUnitAssignedContext", globalUnitAssignedContext);
        state.put("globalUncertaintyAssignedContext", globalUncertaintyAssignedContext);
        return state;
    }
}
