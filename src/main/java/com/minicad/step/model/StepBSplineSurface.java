package com.minicad.step.model;

import java.util.List;
import java.util.Objects;

/**
 * Minimal B_SPLINE_SURFACE without knot data.
 *
 * @param id STEP id
 * @param name STEP label
 * @param uDegree U degree
 * @param vDegree V degree
 * @param controlPoints control-point grid indexed as [u][v]
 * @param surfaceForm surface form enum
 * @param uClosed U closed flag
 * @param vClosed V closed flag
 * @param selfIntersect self-intersection flag
 */
public final class StepBSplineSurface extends AbstractStepControlPointSurface {
    private final boolean uClosed;
    private final boolean vClosed;
    private final boolean selfIntersect;

    public StepBSplineSurface(int id, String name, int uDegree, int vDegree, List<List<StepCartesianPoint>> controlPoints, String surfaceForm, boolean uClosed, boolean vClosed, boolean selfIntersect) {
        super(id, name, uDegree, vDegree, controlPoints, surfaceForm);
        this.uClosed = uClosed;
        this.vClosed = vClosed;
        this.selfIntersect = selfIntersect;
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

    public boolean uClosed() { return isUClosed(); }
    public boolean vClosed() { return isVClosed(); }
    public boolean selfIntersect() { return isSelfIntersect(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBSplineSurface that = (StepBSplineSurface) o;
        return getId() == that.getId() && Objects.equals(getName(), that.getName()) && getUDegree() == that.getUDegree() && getVDegree() == that.getVDegree() && Objects.equals(getControlPoints(), that.getControlPoints()) && Objects.equals(getSurfaceForm(), that.getSurfaceForm()) && uClosed == that.uClosed && vClosed == that.vClosed && selfIntersect == that.selfIntersect;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getUDegree(), getVDegree(), getControlPoints(), getSurfaceForm(), uClosed, vClosed, selfIntersect);
    }

    @Override
    public String toString() {
        return "StepBSplineSurface{" + "id=" + getId() + "name=" + getName() + "uDegree=" + getUDegree() + "vDegree=" + getVDegree() + "controlPoints=" + getControlPoints() + "surfaceForm=" + getSurfaceForm() + "uClosed=" + uClosed + "vClosed=" + vClosed + "selfIntersect=" + selfIntersect + "}";
    }
}
