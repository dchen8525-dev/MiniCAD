package com.minicad.step.model;

import java.util.List;

/**
 * Shared core of the resolved control-point curve entities.
 *
 * <p>B_SPLINE_CURVE, BEZIER_CURVE, UNIFORM_CURVE, QUASI_UNIFORM_CURVE and PIECEWISE_BEZIER_CURVE -
 * together with the converter's 2D and rational variants - all resolve to the same five fields: an
 * instance id, a label, a polynomial degree, control-point references and a curve-form enum. Every
 * one of those twelve entities used to declare, null-check, copy and expose them itself, byte for
 * byte. This type owns them once; a concrete entity keeps only the fields that actually tell it
 * apart from its siblings (closed/self-intersecting flags, knot data, weights, breakpoints).
 *
 * <p>The core exists so that the shared half is written once, not so that the entities become one
 * family. Nothing is allowed to branch on it: every dispatch table and every {@code instanceof}
 * chain in the tree keys on the concrete entity classes, and the subclasses keep exactly the type
 * identity they had before this type existed.
 */
public abstract class AbstractStepControlPointCurve implements StepEntity {

    private final int id;
    private final String name;
    private final int degree;
    private final List<StepCartesianPoint> controlPoints;
    private final String curveForm;

    protected AbstractStepControlPointCurve(
            int id, String name, int degree, List<StepCartesianPoint> controlPoints, String curveForm) {
        this.id = id;
        this.name = name;
        this.degree = degree;
        this.controlPoints = controlPoints == null ? null : List.copyOf(controlPoints);
        this.curveForm = curveForm;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
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

    // Record-style accessors
    public int degree() { return getDegree(); }
    public List<StepCartesianPoint> controlPoints() { return getControlPoints(); }
    public String curveForm() { return getCurveForm(); }
}
