package com.minicad.step.model;

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
public final class StepQuasiUniformCurve extends AbstractStepControlPointCurve {
    private final boolean closedCurve;
    private final boolean selfIntersect;

    public StepQuasiUniformCurve(int id, String name, int degree, List<StepCartesianPoint> controlPoints, String curveForm, boolean closedCurve, boolean selfIntersect) {
        super(id, name, degree, controlPoints, curveForm);
        this.closedCurve = closedCurve;
        this.selfIntersect = selfIntersect;
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
        return getId() == that.getId() && Objects.equals(getName(), that.getName()) && getDegree() == that.getDegree() && Objects.equals(getControlPoints(), that.getControlPoints()) && Objects.equals(getCurveForm(), that.getCurveForm()) && closedCurve == that.closedCurve && selfIntersect == that.selfIntersect;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDegree(), getControlPoints(), getCurveForm(), closedCurve, selfIntersect);
    }

    @Override
    public String toString() {
        return "StepQuasiUniformCurve{" + "id=" + getId() + "name=" + getName() + "degree=" + getDegree() + "controlPoints=" + getControlPoints() + "curveForm=" + getCurveForm() + "closedCurve=" + closedCurve + "selfIntersect=" + selfIntersect + "}";
    }
}
