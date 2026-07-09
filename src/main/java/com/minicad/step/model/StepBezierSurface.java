package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved BEZIER_SURFACE marker with inherited B-spline data when present.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 * @param uDegree U degree, or {@code -1} when this is only a marker
 * @param vDegree V degree, or {@code -1} when this is only a marker
 * @param controlPoints control-point grid indexed as [u][v]
 * @param surfaceForm surface form enum
 * @param uClosed U closed flag
 * @param vClosed V closed flag
 * @param selfIntersect self-intersection flag
 */
/**
 * Resolved BEZIER_SURFACE marker with inherited B-spline data when present.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 * @param uDegree U degree, or {@code -1} when this is only a marker
 * @param vDegree V degree, or {@code -1} when this is only a marker
 * @param controlPoints control-point grid indexed as [u][v]
 * @param surfaceForm surface form enum
 * @param uClosed U closed flag
 * @param vClosed V closed flag
 * @param selfIntersect self-intersection flag
 */
public final class StepBezierSurface implements StepEntity {
    private final int id;
    private final String name;
    private final int uDegree;
    private final int vDegree;
    private final List<List<StepCartesianPoint>> controlPoints;
    private final String surfaceForm;
    private final boolean uClosed;
    private final boolean vClosed;
    private final boolean selfIntersect;

    public StepBezierSurface(int id, String name, int uDegree, int vDegree, List<List<StepCartesianPoint>> controlPoints, String surfaceForm, boolean uClosed, boolean vClosed, boolean selfIntersect) {
        this.id = id;
        this.name = name;
        this.uDegree = uDegree;
        this.vDegree = vDegree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.surfaceForm = surfaceForm;
        this.uClosed = uClosed;
        this.vClosed = vClosed;
        this.selfIntersect = selfIntersect;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getUDegree() {
        return uDegree;
    }

    public int getVDegree() {
        return vDegree;
    }

    public List<List<StepCartesianPoint>> getControlPoints() {
        return controlPoints;
    }

    public String getSurfaceForm() {
        return surfaceForm;
    }

    public boolean isUClosed() {
        return uClosed;
    }

    public boolean isVClosed() {
        return vClosed;
    }

    public boolean isSelfIntersect() {
        return selfIntersect;
    }

    // Record-style accessors
    public int uDegree() { return getUDegree(); }
    public int vDegree() { return getVDegree(); }
    public List<List<StepCartesianPoint>> controlPoints() { return getControlPoints(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBezierSurface that = (StepBezierSurface) o;
        return id == that.id && Objects.equals(name, that.name) && uDegree == that.uDegree && vDegree == that.vDegree && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(surfaceForm, that.surfaceForm) && uClosed == that.uClosed && vClosed == that.vClosed && selfIntersect == that.selfIntersect;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, uDegree, vDegree, controlPoints, surfaceForm, uClosed, vClosed, selfIntersect);
    }

    @Override
    public String toString() {
        return "StepBezierSurface{" + "id=" + id + "name=" + name + "uDegree=" + uDegree + "vDegree=" + vDegree + "controlPoints=" + controlPoints + "surfaceForm=" + surfaceForm + "uClosed=" + uClosed + "vClosed=" + vClosed + "selfIntersect=" + selfIntersect + "}";
    }
}
