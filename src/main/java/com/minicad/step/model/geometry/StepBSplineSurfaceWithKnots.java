package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
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
public final class StepBSplineSurfaceWithKnots implements StepEntity {
    private final int id;
    private final String name;
    private final int uDegree;
    private final int vDegree;
    private final List<List<StepCartesianPoint>> controlPoints;
    private final String surfaceForm;
    private final boolean uClosed;
    private final boolean vClosed;
    private final boolean selfIntersect;
    private final List<Integer> uMultiplicities;
    private final List<Integer> vMultiplicities;
    private final List<Double> uKnots;
    private final List<Double> vKnots;
    private final String knotSpec;

    public StepBSplineSurfaceWithKnots(int id, String name, int uDegree, int vDegree, List<List<StepCartesianPoint>> controlPoints, String surfaceForm, boolean uClosed, boolean vClosed, boolean selfIntersect, List<Integer> uMultiplicities, List<Integer> vMultiplicities, List<Double> uKnots, List<Double> vKnots, String knotSpec) {
        this.id = id;
        this.name = name;
        this.uDegree = uDegree;
        this.vDegree = vDegree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.surfaceForm = surfaceForm;
        this.uClosed = uClosed;
        this.vClosed = vClosed;
        this.selfIntersect = selfIntersect;
        this.uMultiplicities = uMultiplicities == null ? null : java.util.List.copyOf(uMultiplicities);
        this.vMultiplicities = vMultiplicities == null ? null : java.util.List.copyOf(vMultiplicities);
        this.uKnots = uKnots == null ? null : java.util.List.copyOf(uKnots);
        this.vKnots = vKnots == null ? null : java.util.List.copyOf(vKnots);
        this.knotSpec = knotSpec;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBSplineSurfaceWithKnots that = (StepBSplineSurfaceWithKnots) o;
        return id == that.id && Objects.equals(name, that.name) && uDegree == that.uDegree && vDegree == that.vDegree && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(surfaceForm, that.surfaceForm) && uClosed == that.uClosed && vClosed == that.vClosed && selfIntersect == that.selfIntersect && Objects.equals(uMultiplicities, that.uMultiplicities) && Objects.equals(vMultiplicities, that.vMultiplicities) && Objects.equals(uKnots, that.uKnots) && Objects.equals(vKnots, that.vKnots) && Objects.equals(knotSpec, that.knotSpec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, uDegree, vDegree, controlPoints, surfaceForm, uClosed, vClosed, selfIntersect, uMultiplicities, vMultiplicities, uKnots, vKnots, knotSpec);
    }

    @Override
    public String toString() {
        return "StepBSplineSurfaceWithKnots{" + "id=" + id + "name=" + name + "uDegree=" + uDegree + "vDegree=" + vDegree + "controlPoints=" + controlPoints + "surfaceForm=" + surfaceForm + "uClosed=" + uClosed + "vClosed=" + vClosed + "selfIntersect=" + selfIntersect + "uMultiplicities=" + uMultiplicities + "vMultiplicities=" + vMultiplicities + "uKnots=" + uKnots + "vKnots=" + vKnots + "knotSpec=" + knotSpec + "}";
    }
}
