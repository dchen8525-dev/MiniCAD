package com.minicad.geometry2d;

import com.minicad.common.Preconditions;

import java.util.List;

/**
 * Minimal composite 2D curve backed by multiple supported segments.
 *
 * @param segments ordered component curves
 */
public record CompositeCurve2(List<Curve2> segments) implements Curve2 {

    public CompositeCurve2 {
        segments = List.copyOf(segments);
        if (segments.isEmpty()) {
            throw new IllegalArgumentException("segments must not be empty");
        }
        for (Curve2 segment : segments) {
            Preconditions.requireNonNull(segment, "segments");
        }
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        for (Curve2 segment : segments) {
            if (segment.contains(point)) {
                return true;
            }
        }
        return false;
    }
}
