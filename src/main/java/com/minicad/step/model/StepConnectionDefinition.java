package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepConnectionDefinition extends AbstractStepEntity {
    private final StepEntity varianceConnection;
    private final StepEntity varianceFrom;
    private final StepEntity varianceTo;
    private final String varianceType;
    private final StepEntity varianceInterface;
    private final String varianceStatus;

    public StepConnectionDefinition(int id, String name, StepEntity varianceConnection, StepEntity varianceFrom, StepEntity varianceTo, String varianceType, StepEntity varianceInterface, String varianceStatus) {
        super(id, name);
        this.varianceConnection = varianceConnection;
        this.varianceFrom = varianceFrom;
        this.varianceTo = varianceTo;
        this.varianceType = varianceType;
        this.varianceInterface = varianceInterface;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "varianceConnection", varianceConnection,
                "varianceFrom", varianceFrom, "varianceTo", varianceTo, "varianceType", varianceType,
                "varianceInterface", varianceInterface, "varianceStatus", varianceStatus);
    }
}
