package com.minicad.step.model.management.security;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ACCESS_FEATURE.
 * An access feature entity.
 *
 * @param id STEP instance id
 * @param name access name
 * @param accessType access type (door, panel, hatch, inspection)
 * @param accessGeometry access geometry representation
 * @param accessOpening access opening dimensions
 * @param accessLocation access location placement
 * @varianceFrequency access variance frequency (regular, emergency)
 */
/**
 * Resolved ACCESS_FEATURE.
 * An access feature entity.
 *
 * @param id STEP instance id
 * @param name access name
 * @param accessType access type (door, panel, hatch, inspection)
 * @param accessGeometry access geometry representation
 * @param accessOpening access opening dimensions
 * @param accessLocation access location placement
 * @varianceFrequency access variance frequency (regular, emergency)
 */
public final class StepAccessFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String accessType;
    private final StepEntity accessGeometry;
    private final List<Double> accessOpening;
    private final StepEntity accessLocation;
    private final String varianceFrequency;

    public StepAccessFeature(int id, String name, String accessType, StepEntity accessGeometry, List<Double> accessOpening, StepEntity accessLocation, String varianceFrequency) {
        this.id = id;
        this.name = name;
        this.accessType = accessType;
        this.accessGeometry = accessGeometry;
        this.accessOpening = accessOpening == null ? null : java.util.List.copyOf(accessOpening);
        this.accessLocation = accessLocation;
        this.varianceFrequency = varianceFrequency;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAccessType() {
        return accessType;
    }

    public StepEntity getAccessGeometry() {
        return accessGeometry;
    }

    public List<Double> getAccessOpening() {
        return accessOpening;
    }

    public StepEntity getAccessLocation() {
        return accessLocation;
    }

    public String getVarianceFrequency() {
        return varianceFrequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAccessFeature that = (StepAccessFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(accessType, that.accessType) && Objects.equals(accessGeometry, that.accessGeometry) && Objects.equals(accessOpening, that.accessOpening) && Objects.equals(accessLocation, that.accessLocation) && Objects.equals(varianceFrequency, that.varianceFrequency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, accessType, accessGeometry, accessOpening, accessLocation, varianceFrequency);
    }

    @Override
    public String toString() {
        return "StepAccessFeature{" + "id=" + id + "name=" + name + "accessType=" + accessType + "accessGeometry=" + accessGeometry + "accessOpening=" + accessOpening + "accessLocation=" + accessLocation + "varianceFrequency=" + varianceFrequency + "}";
    }
}