package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SURFACE_CURVE with supported 3D curve geometry.
 *
 * @param id step id
 * @param entityName step entity name
 * @param name step label
 * @param curve3d referenced 3D curve
 * @param associatedGeometry associated PCURVE or surface-geometry items
 * @param masterRepresentation preferred representation enum
 */
/**
 * Resolved SURFACE_CURVE with supported 3D curve geometry.
 *
 * @param id step id
 * @param entityName step entity name
 * @param name step label
 * @param curve3d referenced 3D curve
 * @param associatedGeometry associated PCURVE or surface-geometry items
 * @param masterRepresentation preferred representation enum
 */
public final class StepSurfaceCurve implements StepEntity {
    private final int id;
    private final String entityName;
    private final String name;
    private final StepEntity curve3d;
    private final List<StepEntity> associatedGeometry;
    private final String masterRepresentation;

    public StepSurfaceCurve(int id, String entityName, String name, StepEntity curve3d, List<StepEntity> associatedGeometry, String masterRepresentation) {
        this.id = id;
        this.entityName = entityName;
        this.name = name;
        this.curve3d = curve3d;
        this.associatedGeometry = associatedGeometry == null ? null : java.util.List.copyOf(associatedGeometry);
        this.masterRepresentation = masterRepresentation;
    }

    public int getId() {
        return id;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getName() {
        return name;
    }

    public StepEntity getCurve3d() {
        return curve3d;
    }

    public List<StepEntity> getAssociatedGeometry() {
        return associatedGeometry;
    }

    public String getMasterRepresentation() {
        return masterRepresentation;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String entityName() { return getEntityName(); }
    public String name() { return getName(); }
    public StepEntity curve3d() { return getCurve3d(); }
    public List<StepEntity> associatedGeometry() { return getAssociatedGeometry(); }
    public String masterRepresentation() { return getMasterRepresentation(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceCurve that = (StepSurfaceCurve) o;
        return id == that.id && Objects.equals(entityName, that.entityName) && Objects.equals(name, that.name) && Objects.equals(curve3d, that.curve3d) && Objects.equals(associatedGeometry, that.associatedGeometry) && Objects.equals(masterRepresentation, that.masterRepresentation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityName, name, curve3d, associatedGeometry, masterRepresentation);
    }

    @Override
    public String toString() {
        return "StepSurfaceCurve{" + "id=" + id + "entityName=" + entityName + "name=" + name + "curve3d=" + curve3d + "associatedGeometry=" + associatedGeometry + "masterRepresentation=" + masterRepresentation + "}";
    }
}
