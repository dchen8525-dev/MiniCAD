package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * Minimal composite curve backed by multiple supported segments.
 *
 * @param segments ordered component curves
 */
/**
 * Minimal composite curve backed by multiple supported segments.
 *
 * @param segments ordered component curves
 */
public final class CompositeCurve3 implements Curve3 {
    private final List<Curve3> segments;

    public CompositeCurve3(List<Curve3> segments) {
        this.segments = segments == null ? null : java.util.List.copyOf(segments);
    }

    public List<Curve3> getSegments() {
        return segments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeCurve3 that = (CompositeCurve3) o;
        return Objects.equals(segments, that.segments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(segments);
    }

    @Override
    public String toString() {
        return "CompositeCurve3{" + "segments=" + segments + "}";
    }
}
