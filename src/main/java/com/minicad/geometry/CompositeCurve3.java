package com.minicad.geometry;

import com.minicad.common.Preconditions;

import java.util.List;

/**
 * Minimal composite curve backed by multiple supported segments.
 *
 * @param segments ordered component curves
 */
public record CompositeCurve3(List<Curve3> segments) implements Curve3 {

    public CompositeCurve3 {
        segments = List.copyOf(segments);
        if (segments.isEmpty()) {
            throw new IllegalArgumentException("segments must not be empty");
        }
        for (Curve3 segment : segments) {
            Preconditions.requireNonNull(segment, "segments");
        }
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        for (Curve3 segment : segments) {
            if (segment.contains(point)) {
                return true;
            }
        }
        return false;
    }
}
