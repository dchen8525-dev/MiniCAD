package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TRANSPORT_FEATURE.
 * A transport feature entity.
 *
 * @param id STEP instance id
 * @param name transport name
 * @param transportType transport type (conveyor, crane, truck, rail)
 * @param transportGeometry transport geometry representation
 * @varianceCapacity transport variance capacity
 * @param transportRoute transport route/path reference
 * @varianceSpeed transport variance speed
 * @param transportStandard transport standard reference
 */
/**
 * Resolved TRANSPORT_FEATURE.
 * A transport feature entity.
 *
 * @param id STEP instance id
 * @param name transport name
 * @param transportType transport type (conveyor, crane, truck, rail)
 * @param transportGeometry transport geometry representation
 * @varianceCapacity transport variance capacity
 * @param transportRoute transport route/path reference
 * @varianceSpeed transport variance speed
 * @param transportStandard transport standard reference
 */
public final class StepTransportFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String transportType;
    private final StepEntity transportGeometry;
    private final double varianceCapacity;
    private final StepEntity transportRoute;
    private final double varianceSpeed;
    private final String transportStandard;

    public StepTransportFeature(int id, String name, String transportType, StepEntity transportGeometry, double varianceCapacity, StepEntity transportRoute, double varianceSpeed, String transportStandard) {
        this.id = id;
        this.name = name;
        this.transportType = transportType;
        this.transportGeometry = transportGeometry;
        this.varianceCapacity = varianceCapacity;
        this.transportRoute = transportRoute;
        this.varianceSpeed = varianceSpeed;
        this.transportStandard = transportStandard;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTransportType() {
        return transportType;
    }

    public StepEntity getTransportGeometry() {
        return transportGeometry;
    }

    public double getVarianceCapacity() {
        return varianceCapacity;
    }

    public StepEntity getTransportRoute() {
        return transportRoute;
    }

    public double getVarianceSpeed() {
        return varianceSpeed;
    }

    public String getTransportStandard() {
        return transportStandard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTransportFeature that = (StepTransportFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(transportType, that.transportType) && Objects.equals(transportGeometry, that.transportGeometry) && varianceCapacity == that.varianceCapacity && Objects.equals(transportRoute, that.transportRoute) && varianceSpeed == that.varianceSpeed && Objects.equals(transportStandard, that.transportStandard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, transportType, transportGeometry, varianceCapacity, transportRoute, varianceSpeed, transportStandard);
    }

    @Override
    public String toString() {
        return "StepTransportFeature{" + "id=" + id + "name=" + name + "transportType=" + transportType + "transportGeometry=" + transportGeometry + "varianceCapacity=" + varianceCapacity + "transportRoute=" + transportRoute + "varianceSpeed=" + varianceSpeed + "transportStandard=" + transportStandard + "}";
    }
}