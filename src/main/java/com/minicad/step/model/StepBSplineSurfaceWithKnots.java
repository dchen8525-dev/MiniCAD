package com.minicad.step.model;

import java.util.List;
import java.util.Objects;

/**
 * Resolved non-rational B_SPLINE_SURFACE_WITH_KNOTS.
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
 * @param uMultiplicities U multiplicities
 * @param vMultiplicities V multiplicities
 * @param uKnots unique U knots
 * @param vKnots unique V knots
 * @param knotSpec knot-spec enum
 */
public final class StepBSplineSurfaceWithKnots extends AbstractStepControlPointSurface {
    private final boolean uClosed;
    private final boolean vClosed;
    private final boolean selfIntersect;
    private final List<Integer> uMultiplicities;
    private final List<Integer> vMultiplicities;
    private final List<Double> uKnots;
    private final List<Double> vKnots;
    private final String knotSpec;

    public StepBSplineSurfaceWithKnots(int id, String name, int uDegree, int vDegree, List<List<StepCartesianPoint>> controlPoints, String surfaceForm, boolean uClosed, boolean vClosed, boolean selfIntersect, List<Integer> uMultiplicities, List<Integer> vMultiplicities, List<Double> uKnots, List<Double> vKnots, String knotSpec) {
        super(id, name, uDegree, vDegree, controlPoints, surfaceForm);
        this.uClosed = uClosed;
        this.vClosed = vClosed;
        this.selfIntersect = selfIntersect;
        this.uMultiplicities = uMultiplicities == null ? null : java.util.List.copyOf(uMultiplicities);
        this.vMultiplicities = vMultiplicities == null ? null : java.util.List.copyOf(vMultiplicities);
        this.uKnots = uKnots == null ? null : java.util.List.copyOf(uKnots);
        this.vKnots = vKnots == null ? null : java.util.List.copyOf(vKnots);
        this.knotSpec = knotSpec;
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

    public List<Integer> getUMultiplicities() {
        return uMultiplicities;
    }

    public List<Integer> getVMultiplicities() {
        return vMultiplicities;
    }

    public List<Double> getUKnots() {
        return uKnots;
    }

    public List<Double> getVKnots() {
        return vKnots;
    }

    public String getKnotSpec() {
        return knotSpec;
    }

    // Record-style accessors
    public boolean uClosed() { return isUClosed(); }
    public boolean vClosed() { return isVClosed(); }
    public boolean selfIntersect() { return isSelfIntersect(); }
    public List<Integer> uMultiplicities() { return getUMultiplicities(); }
    public List<Integer> vMultiplicities() { return getVMultiplicities(); }
    public List<Double> uKnots() { return getUKnots(); }
    public List<Double> vKnots() { return getVKnots(); }
    public String knotSpec() { return getKnotSpec(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBSplineSurfaceWithKnots that = (StepBSplineSurfaceWithKnots) o;
        return getId() == that.getId() && Objects.equals(getName(), that.getName()) && getUDegree() == that.getUDegree() && getVDegree() == that.getVDegree() && Objects.equals(getControlPoints(), that.getControlPoints()) && Objects.equals(getSurfaceForm(), that.getSurfaceForm()) && uClosed == that.uClosed && vClosed == that.vClosed && selfIntersect == that.selfIntersect && Objects.equals(uMultiplicities, that.uMultiplicities) && Objects.equals(vMultiplicities, that.vMultiplicities) && Objects.equals(uKnots, that.uKnots) && Objects.equals(vKnots, that.vKnots) && Objects.equals(knotSpec, that.knotSpec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getUDegree(), getVDegree(), getControlPoints(), getSurfaceForm(), uClosed, vClosed, selfIntersect, uMultiplicities, vMultiplicities, uKnots, vKnots, knotSpec);
    }

    @Override
    public String toString() {
        return "StepBSplineSurfaceWithKnots{" + "id=" + getId() + "name=" + getName() + "uDegree=" + getUDegree() + "vDegree=" + getVDegree() + "controlPoints=" + getControlPoints() + "surfaceForm=" + getSurfaceForm() + "uClosed=" + uClosed + "vClosed=" + vClosed + "selfIntersect=" + selfIntersect + "uMultiplicities=" + uMultiplicities + "vMultiplicities=" + vMultiplicities + "uKnots=" + uKnots + "vKnots=" + vKnots + "knotSpec=" + knotSpec + "}";
    }
}
