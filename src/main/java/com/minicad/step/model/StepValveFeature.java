package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepValveFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String valveType;
    private final double portDiameter;
    private final StepEntity valveBody;
    private final StepEntity valveActuator;
    private final StepEntity valveMaterial;
    private final String flowDirection;

    public StepValveFeature(int id, String name, String valveType, double portDiameter, StepEntity valveBody, StepEntity valveActuator, StepEntity valveMaterial, String flowDirection) {
        this.id = id;
        this.name = name;
        this.valveType = valveType;
        this.portDiameter = portDiameter;
        this.valveBody = valveBody;
        this.valveActuator = valveActuator;
        this.valveMaterial = valveMaterial;
        this.flowDirection = flowDirection;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepValveFeature that = (StepValveFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(valveType, that.valveType) && portDiameter == that.portDiameter && Objects.equals(valveBody, that.valveBody) && Objects.equals(valveActuator, that.valveActuator) && Objects.equals(valveMaterial, that.valveMaterial) && Objects.equals(flowDirection, that.flowDirection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, valveType, portDiameter, valveBody, valveActuator, valveMaterial, flowDirection);
    }

    @Override
    public String toString() {
        return "StepValveFeature{" + "id=" + id + "name=" + name + "valveType=" + valveType + "portDiameter=" + portDiameter + "valveBody=" + valveBody + "valveActuator=" + valveActuator + "valveMaterial=" + valveMaterial + "flowDirection=" + flowDirection + "}";
    }
}