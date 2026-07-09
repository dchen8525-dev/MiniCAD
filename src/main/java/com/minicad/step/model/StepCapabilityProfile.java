package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CAPABILITY_PROFILE.
 * A capability profile entity.
 *
 * @param id STEP instance id
 * @param name profile name
 * @varianceResource resource variance reference
 * @varianceCapabilities capability variance list
 * @varianceCapacities capacity variance values
 * @varianceAccuracy accuracy variance specifications
 * @varianceStatus profile variance status
 */
/**
 * Resolved CAPABILITY_PROFILE.
 * A capability profile entity.
 *
 * @param id STEP instance id
 * @param name profile name
 * @varianceResource resource variance reference
 * @varianceCapabilities capability variance list
 * @varianceCapacities capacity variance values
 * @varianceAccuracy accuracy variance specifications
 * @varianceStatus profile variance status
 */
public final class StepCapabilityProfile implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceResource;
    private final List<String> varianceCapabilities;
    private final List<Double> varianceCapacities;
    private final List<Double> varianceAccuracy;
    private final String varianceStatus;

    public StepCapabilityProfile(int id, String name, StepEntity varianceResource, List<String> varianceCapabilities, List<Double> varianceCapacities, List<Double> varianceAccuracy, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceResource = varianceResource;
        this.varianceCapabilities = varianceCapabilities == null ? null : java.util.List.copyOf(varianceCapabilities);
        this.varianceCapacities = varianceCapacities == null ? null : java.util.List.copyOf(varianceCapacities);
        this.varianceAccuracy = varianceAccuracy == null ? null : java.util.List.copyOf(varianceAccuracy);
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceResource() {
        return varianceResource;
    }

    public List<String> getVarianceCapabilities() {
        return varianceCapabilities;
    }

    public List<Double> getVarianceCapacities() {
        return varianceCapacities;
    }

    public List<Double> getVarianceAccuracy() {
        return varianceAccuracy;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCapabilityProfile that = (StepCapabilityProfile) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceResource, that.varianceResource) && Objects.equals(varianceCapabilities, that.varianceCapabilities) && Objects.equals(varianceCapacities, that.varianceCapacities) && Objects.equals(varianceAccuracy, that.varianceAccuracy) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceResource, varianceCapabilities, varianceCapacities, varianceAccuracy, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepCapabilityProfile{" + "id=" + id + "name=" + name + "varianceResource=" + varianceResource + "varianceCapabilities=" + varianceCapabilities + "varianceCapacities=" + varianceCapacities + "varianceAccuracy=" + varianceAccuracy + "varianceStatus=" + varianceStatus + "}";
    }
}