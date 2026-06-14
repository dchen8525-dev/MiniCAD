package com.minicad.geometry2d;

import com.minicad.common.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * Minimal composite 2D curve backed by multiple supported segments.
 *
 * @param segments ordered component curves
 */
/**
 * Minimal composite 2D curve backed by multiple supported segments.
 *
 * @param segments ordered component curves
 */
public final class CompositeCurve2 implements Curve2 {
    private final List<Curve2> segments;

    public CompositeCurve2(List<Curve2> segments) {
        this.segments = segments == null ? null : java.util.List.copyOf(segments);
    }

    public List<Curve2> getSegments() {
        return segments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeCurve2 that = (CompositeCurve2) o;
        return Objects.equals(segments, that.segments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(segments);
    }

    @Override
    public String toString() {
        return "CompositeCurve2{" + "segments=" + segments + "}";
    }
}
