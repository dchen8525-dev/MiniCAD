package com.minicad.common;

import java.util.List;

/**
 * A B-spline knot vector: the unique knot values with their multiplicities, plus
 * the multiplicity-expanded vector cached after first use.
 *
 * <p>This is dimension-free, which is why it lives in {@code com.minicad.common}
 * next to {@link BSplineKernel}: the 2D and 3D curve families and the surface
 * family all keep their knots in one of these. Before it existed the caching body
 * was written out eight times - once per curve class and twice per surface class -
 * so a change to it, or a missing one, could not be seen from any single copy.</p>
 *
 * <p>A knot vector carries two different readings of "the natural parameter
 * domain", and the families do not agree on which they use:</p>
 * <ul>
 *   <li>{@link #start()} / {@link #end()} are the first and last <em>unique</em>
 *       knot value. Both curve families use these.</li>
 *   <li>{@link #expandedStart(int)} / {@link #expandedEnd(int)} are the entries of
 *       the expanded vector at the degree and at the control-point count. Both
 *       surface families use these.</li>
 * </ul>
 *
 * <p>For a clamped vector the two readings coincide, which is why they were
 * conflated; for an unclamped one they do not, which is why they are named apart
 * here instead of being merged into one "domain" accessor.</p>
 */
public final class KnotVector {

    private final List<Double> knots;
    private final List<Integer> multiplicities;
    private volatile List<Double> expanded;

    /**
     * @param knots unique knot values
     * @param multiplicities multiplicity per unique value, same size as {@code knots}
     * @throws GeometryException when the two sizes differ
     */
    public KnotVector(List<Double> knots, List<Integer> multiplicities) {
        this.knots = knots == null ? List.of() : List.copyOf(knots);
        this.multiplicities = multiplicities == null ? List.of() : List.copyOf(multiplicities);
        if (this.knots.size() != this.multiplicities.size()) {
            throw new GeometryException("knot values and multiplicities must have equal size");
        }
    }

    /**
     * @return the unique knot values
     */
    public List<Double> knots() {
        return knots;
    }

    /**
     * @return the multiplicity of each unique knot value
     */
    public List<Integer> multiplicities() {
        return multiplicities;
    }

    /**
     * Returns the multiplicity-expanded knot vector, building it on first use and
     * reusing the same instance afterwards.
     *
     * @return the expanded knot vector
     */
    public List<Double> expanded() {
        List<Double> local = expanded;
        if (local == null) {
            local = BSplineKernel.expandedKnots(knots, multiplicities);
            expanded = local;
        }
        return local;
    }

    /**
     * First unique knot value - the start of the domain a curve reports.
     *
     * @return the first knot, or {@code 0.0} when there is none
     */
    public double start() {
        return BSplineKernel.knotStart(knots);
    }

    /**
     * Last unique knot value - the end of the domain a curve reports.
     *
     * @return the last knot, or {@code 1.0} when there is none
     */
    public double end() {
        return BSplineKernel.knotEnd(knots);
    }

    /**
     * Expanded-vector entry at the degree - the start of the domain a surface reports.
     *
     * @param degree spline degree
     * @return the knot at that index of the expanded vector
     */
    public double expandedStart(int degree) {
        return expanded().get(degree);
    }

    /**
     * Expanded-vector entry at the control-point count - the end of the domain a
     * surface reports.
     *
     * @param controlPointCount number of control points
     * @return the knot at that index of the expanded vector
     */
    public double expandedEnd(int controlPointCount) {
        return expanded().get(controlPointCount);
    }
}
