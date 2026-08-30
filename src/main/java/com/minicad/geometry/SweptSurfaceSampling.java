package com.minicad.geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared profile sampling for swept surfaces (translation, projection, ...).
 *
 * <p>A swept surface takes its U parameter from an arbitrary {@link Curve3},
 * and {@code Curve3} exposes no parameter domain — a line's is infinite, a
 * B-spline's is its knot range. Sampling U therefore has to go through
 * {@link Curve3#sample(int)}, which every curve implements in its own domain,
 * rather than through a guessed {@code [0, 1]} or {@code [start, end]} range.</p>
 */
final class SweptSurfaceSampling {

    private SweptSurfaceSampling() {}

    /**
     * Samples the profile into {@code segments + 1} points spanning its own
     * parameter domain.
     *
     * <p>{@link Curve3#sample(int)} defaults to an empty list, and an empty
     * sample would make the whole surface grid collapse to nothing (empty
     * bounding box, no mesh). Every curve in this kernel overrides it, but the
     * fallback keeps a curve with a default {@code sample} from silently
     * producing an empty surface.</p>
     *
     * @param profile  the swept profile curve
     * @param segments number of segments along U
     * @return {@code segments + 1} profile points, never empty
     */
    static List<CartesianPoint> sampleProfile(Curve3 profile, int segments) {
        int count = Math.max(segments, 1);
        List<CartesianPoint> sampled = profile.sample(count);
        if (!sampled.isEmpty()) {
            return sampled;
        }
        List<CartesianPoint> fallback = new ArrayList<>(count + 1);
        for (int i = 0; i <= count; i++) {
            fallback.add(profile.pointAt((double) i / count));
        }
        return fallback;
    }
}
