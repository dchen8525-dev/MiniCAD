package com.minicad.step.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
 *
 * <p>It also owns the value contract. {@code equals}, {@code hashCode} and {@code toString} were
 * the same three algorithms written out once per entity - sixty bodies, each listing this class's
 * fields and then its own. They now derive from a single {@link #components()} map, so a new field
 * cannot be added to the constructor and forgotten in one of the three.
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

    /**
     * This entity's state, as the value contract sees it: label to value, in the order the previous
     * hand-written {@code toString} printed the pairs and the order the previous {@code hashCode}
     * folded them. The order is not cosmetic - it is pinned, per entity, against the values the
     * retired bodies produced (see {@code ControlPointEntityCoreConvergenceTest}), because
     * reordering a component changes both the printed string and the hash.
     *
     * @return an ordered map of component label to value
     */
    protected abstract Map<String, Object> components();

    private Map<String, Object> componentCache;

    private Map<String, Object> componentMap() {
        if (componentCache == null) {
            // The entity is immutable, so the map is built at most once and then answers every
            // equals/hashCode/toString call - which is cheaper than the boxing the retired bodies
            // did on each call.
            componentCache = components();
        }
        return componentCache;
    }

    private List<Object> componentValues() {
        return new ArrayList<>(componentMap().values());
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return componentValues().equals(((AbstractStepControlPointCurve) o).componentValues());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(componentValues().toArray());
    }

    @Override
    public final String toString() {
        StringBuilder text = new StringBuilder(getClass().getSimpleName()).append('{');
        componentMap().forEach((label, value) -> text.append(label).append('=').append(value));
        return text.append('}').toString();
    }
}
