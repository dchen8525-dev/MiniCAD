package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved QUASI_UNIFORM_CURVE marker with inherited B-spline data when present.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 * @param degree spline degree, or {@code -1} when this is only a marker
 * @param controlPoints control-point references
 * @param curveForm curve form enum
 * @param closedCurve closed flag
 * @param selfIntersect self-intersection flag
 */
/**
 * Resolved QUASI_UNIFORM_CURVE marker with inherited B-spline data when present.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 * @param degree spline degree, or {@code -1} when this is only a marker
 * @param controlPoints control-point references
 * @param curveForm curve form enum
 * @param closedCurve closed flag
 * @param selfIntersect self-intersection flag
 */
public final class StepQuasiUniformCurve implements StepEntity {
    private final int id;
    private final String name;
    private final int degree;
    private final List<StepCartesianPoint> controlPoints;
    private final String curveForm;
    private final boolean closedCurve;
    private final boolean selfIntersect;

    public StepQuasiUniformCurve(int id, String name, int degree, List<StepCartesianPoint> controlPoints, String curveForm, boolean closedCurve, boolean selfIntersect) {
        this.id = id;
        this.name = name;
        this.degree = degree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.curveForm = curveForm;
        this.closedCurve = closedCurve;
        this.selfIntersect = selfIntersect;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDegree() {
        return degree;
    }

    public List<StepCartesianPoint> getControlPoints() {
        return controlPoints;
    }

    public String getCurveForm() {
        return curveForm;
    }

    public boolean isClosedCurve() {
        return closedCurve;
    }

    public boolean isSelfIntersect() {
        return selfIntersect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepQuasiUniformCurve that = (StepQuasiUniformCurve) o;
        return id == that.id && Objects.equals(name, that.name) && degree == that.degree && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(curveForm, that.curveForm) && closedCurve == that.closedCurve && selfIntersect == that.selfIntersect;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, degree, controlPoints, curveForm, closedCurve, selfIntersect);
    }

    @Override
    public String toString() {
        return "StepQuasiUniformCurve{" + "id=" + id + "name=" + name + "degree=" + degree + "controlPoints=" + controlPoints + "curveForm=" + curveForm + "closedCurve=" + closedCurve + "selfIntersect=" + selfIntersect + "}";
    }
}
