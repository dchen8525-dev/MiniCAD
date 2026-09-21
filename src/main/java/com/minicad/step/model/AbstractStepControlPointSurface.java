package com.minicad.step.model;

import java.util.List;

/**
 * Shared core of the resolved control-point surface entities.
 *
 * <p>B_SPLINE_SURFACE, BEZIER_SURFACE, UNIFORM_SURFACE, QUASI_UNIFORM_SURFACE and
 * PIECEWISE_BEZIER_SURFACE - together with the rational variant - all resolve to the same six
 * fields: an instance id, a label, a u degree, a v degree, the control-point grid and a
 * surface-form enum. Every one of those eight entities used to declare, null-check, copy and expose
 * them itself, byte for byte. This type owns them once; a concrete entity keeps only the fields
 * that actually tell it apart from its siblings (closed/self-intersecting flags, knot and
 * breakpoint data, weights).
 *
 * <p>The core exists so that the shared half is written once, not so that the entities become one
 * family. Nothing is allowed to branch on it: every dispatch table and every {@code instanceof}
 * chain in the tree keys on the concrete entity classes, and the subclasses keep exactly the type
 * identity they had before this type existed.
 *
 * <p>What is left here of that shared half is the geometry: the two degrees, the control-point grid
 * and the surface form. The value contract is not one of them, because it is not particular to
 * surfaces - this type and its curve counterpart each carried a byte-identical copy of
 * {@code equals}, {@code hashCode}, {@code toString} and the {@code id}/{@code name} pair they
 * read. Both now inherit that from {@link AbstractStepEntity}, which owns it for the whole package,
 * so the algorithm is written once rather than three times. An entity still declares its own
 * {@code components()}, and the order it lists them in is load-bearing twice over - it is what
 * {@code toString} prints and what {@code hashCode} folds - so it stays pinned, per entity, against
 * the values the retired bodies produced.
 */
public abstract class AbstractStepControlPointSurface extends AbstractStepEntity {

    private final int uDegree;
    private final int vDegree;
    private final List<List<StepCartesianPoint>> controlPoints;
    private final String surfaceForm;

    protected AbstractStepControlPointSurface(
            int id,
            String name,
            int uDegree,
            int vDegree,
            List<List<StepCartesianPoint>> controlPoints,
            String surfaceForm) {
        super(id, name);
        this.uDegree = uDegree;
        this.vDegree = vDegree;
        this.controlPoints = controlPoints == null ? null : List.copyOf(controlPoints);
        this.surfaceForm = surfaceForm;
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

    // Record-style accessors
    public int uDegree() { return getUDegree(); }
    public int vDegree() { return getVDegree(); }
    public List<List<StepCartesianPoint>> controlPoints() { return getControlPoints(); }
    public String surfaceForm() { return getSurfaceForm(); }
}
