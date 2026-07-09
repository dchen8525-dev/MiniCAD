package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;

import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepAxis2Placement3D;
import java.util.Objects;

/**
 * Resolved POLYGONAL_BOUNDED_HALF_SPACE.
 * A half-space bounded by a polygonal face.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param basisSurface the half-space surface
 * @param position placement for the polygon
 * @param polygonPoints vertices of the bounding polygon
 * @param sameSense orientation flag
 */
/**
 * Resolved POLYGONAL_BOUNDED_HALF_SPACE.
 * A half-space bounded by a polygonal face.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param basisSurface the half-space surface
 * @param position placement for the polygon
 * @param polygonPoints vertices of the bounding polygon
 * @param sameSense orientation flag
 */
public final class StepPolygonalBoundedHalfSpace implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity basisSurface;
    private final StepAxis2Placement3D position;
    private final List<StepCartesianPoint> polygonPoints;
    private final boolean sameSense;

    public StepPolygonalBoundedHalfSpace(int id, String name, StepEntity basisSurface, StepAxis2Placement3D position, List<StepCartesianPoint> polygonPoints, boolean sameSense) {
        this.id = id;
        this.name = name;
        this.basisSurface = basisSurface;
        this.position = position;
        this.polygonPoints = polygonPoints == null ? null : java.util.List.copyOf(polygonPoints);
        this.sameSense = sameSense;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getBasisSurface() {
        return basisSurface;
    }

    public StepAxis2Placement3D getPosition() {
        return position;
    }

    public List<StepCartesianPoint> getPolygonPoints() {
        return polygonPoints;
    }

    public boolean isSameSense() {
        return sameSense;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity basisSurface() { return getBasisSurface(); }
    public StepAxis2Placement3D position() { return getPosition(); }
    public List<StepCartesianPoint> polygonPoints() { return getPolygonPoints(); }
    public boolean sameSense() { return isSameSense(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPolygonalBoundedHalfSpace that = (StepPolygonalBoundedHalfSpace) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(basisSurface, that.basisSurface) && Objects.equals(position, that.position) && Objects.equals(polygonPoints, that.polygonPoints) && sameSense == that.sameSense;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, basisSurface, position, polygonPoints, sameSense);
    }

    @Override
    public String toString() {
        return "StepPolygonalBoundedHalfSpace{" + "id=" + id + "name=" + name + "basisSurface=" + basisSurface + "position=" + position + "polygonPoints=" + polygonPoints + "sameSense=" + sameSense + "}";
    }
}
