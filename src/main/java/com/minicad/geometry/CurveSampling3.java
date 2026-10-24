package com.minicad.geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleFunction;

/**
 * Uniform parameter sampling shared by the closed-form 3D curves.
 *
 * <p>{@link Circle}, {@link Ellipse3}, {@link Parabola3}, {@link Clothoid3} and
 * {@link SurfaceCurve3} used to carry their own copy of the same loop: one for the
 * full domain and, for the conics, a second one for an arbitrary range. The loop is
 * trivial, which is why every copy was individually correct - but the endpoint
 * arithmetic is what decides every sample, and the copies had to stay bit-identical
 * with each other and with {@code geometry2d.CurveSampling2}.</p>
 */
final class CurveSampling3 {

    private CurveSampling3() {
    }

    /**
     * Samples {@code [start, end]} at {@code segments} equal parameter steps,
     * endpoints included.
     *
     * @param segments number of segments
     * @param start first parameter
     * @param end last parameter
     * @param pointAt evaluation of the curve at one parameter
     * @return {@code segments + 1} points, the first at {@code start} and the last at {@code end}
     */
    static List<CartesianPoint> between(
            int segments, double start, double end, DoubleFunction<CartesianPoint> pointAt) {
        List<CartesianPoint> points = new ArrayList<>();
        for (int i = 0; i <= segments; i++) {
            points.add(pointAt.apply(start + (end - start) * i / segments));
        }
        return List.copyOf(points);
    }
}
