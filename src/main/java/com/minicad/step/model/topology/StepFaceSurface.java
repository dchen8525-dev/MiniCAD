package com.minicad.step.model.topology;

import java.util.List;

import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.base.StepFaceEntity;
import java.util.Objects;

/**
 * Resolved FACE_SURFACE.
 *
 * @param id step id
 * @param name step label
 * @param bounds face bounds
 * @param faceGeometry supporting surface
 * @param sameSense orientation flag
 */
/**
 * Resolved FACE_SURFACE.
 *
 * @param id step id
 * @param name step label
 * @param bounds face bounds
 * @param faceGeometry supporting surface
 * @param sameSense orientation flag
 */
public final class StepFaceSurface implements StepFaceEntity {
    private final int id;
    private final String name;
    private final List<StepFaceBound> bounds;
    private final StepEntity faceGeometry;
    private final boolean sameSense;

    public StepFaceSurface(int id, String name, List<StepFaceBound> bounds, StepEntity faceGeometry, boolean sameSense) {
        this.id = id;
        this.name = name;
        this.bounds = bounds == null ? null : java.util.List.copyOf(bounds);
        this.faceGeometry = faceGeometry;
        this.sameSense = sameSense;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepFaceBound> getBounds() {
        return bounds;
    }

    public StepEntity getFaceGeometry() {
        return faceGeometry;
    }

    public boolean isSameSense() {
        return sameSense;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepFaceBound> bounds() { return getBounds(); }
    public StepEntity faceGeometry() { return getFaceGeometry(); }
    public boolean sameSense() { return isSameSense(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFaceSurface that = (StepFaceSurface) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(bounds, that.bounds) && Objects.equals(faceGeometry, that.faceGeometry) && sameSense == that.sameSense;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, bounds, faceGeometry, sameSense);
    }

    @Override
    public String toString() {
        return "StepFaceSurface{" + "id=" + id + "name=" + name + "bounds=" + bounds + "faceGeometry=" + faceGeometry + "sameSense=" + sameSense + "}";
    }
}
