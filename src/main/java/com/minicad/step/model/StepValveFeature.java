package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved VALVE_FEATURE.
 * A valve feature entity.
 *
 * @param id STEP instance id
 * @param name valve name
 * @param valveType valve type classification (gate, ball, check, globe)
 * @param portDiameter port/flow diameter
 * @param valveBody valve body geometry
 * @valveActuator valve actuator reference
 * @param valveMaterial valve material specification
 * @param flowDirection flow direction specification
 */
public final class StepValveFeature extends AbstractStepEntity {
    private final String valveType;
    private final double portDiameter;
    private final StepEntity valveBody;
    private final StepEntity valveActuator;
    private final StepEntity valveMaterial;
    private final String flowDirection;

    public StepValveFeature(int id, String name, String valveType, double portDiameter, StepEntity valveBody, StepEntity valveActuator, StepEntity valveMaterial, String flowDirection) {
        super(id, name);
        this.valveType = valveType;
        this.portDiameter = portDiameter;
        this.valveBody = valveBody;
        this.valveActuator = valveActuator;
        this.valveMaterial = valveMaterial;
        this.flowDirection = flowDirection;
    }

    public String getValveType() {
        return valveType;
    }

    public double getPortDiameter() {
        return portDiameter;
    }

    public StepEntity getValveBody() {
        return valveBody;
    }

    public StepEntity getValveActuator() {
        return valveActuator;
    }

    public StepEntity getValveMaterial() {
        return valveMaterial;
    }

    public String getFlowDirection() {
        return flowDirection;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("valveType", valveType);
        state.put("portDiameter", portDiameter);
        state.put("valveBody", valveBody);
        state.put("valveActuator", valveActuator);
        state.put("valveMaterial", valveMaterial);
        state.put("flowDirection", flowDirection);
        return state;
    }
}
