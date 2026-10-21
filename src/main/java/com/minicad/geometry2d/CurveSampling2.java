package com.minicad.geometry2d;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleFunction;

/**
 * Uniform parameter sampling shared by the closed-form 2D curves.
 *
 * <p>{@link Circle2}, {@link Ellipse2}, {@link Line2} and {@link Parabola2} used to
 * carry their own copy of the same loop: one for the full domain and, for the two
 * conics, a second one for an arbitrary range. The loop is trivial, which is why
 * every copy was individually correct - but the endpoint arithmetic is what decides
 * every sample, and four copies meant four places that had to stay bit-identical.</p>
 */
final class CurveSampling2 {

    private CurveSampling2() {
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
    static List<Point2> between(int segments, double start, double end, DoubleFunction<Point2> pointAt) {
        List<Point2> points = new ArrayList<>();
        for (int i = 0; i <= segments; i++) {
            points.add(pointAt.apply(start + (end - start) * i / segments));
        }
        return List.copyOf(points);
    }
}
