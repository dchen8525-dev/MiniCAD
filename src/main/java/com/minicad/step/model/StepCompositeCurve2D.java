package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COMPOSITE_CURVE_2D.
 * A connected sequence of curve segments in 2D.
 *
 * @param id step id
 * @param name step label
 * @param segments ordered list of composite curve segments
 * @param selfIntersect whether the curve self-intersects
 */
/**
 * Resolved COMPOSITE_CURVE_2D.
 * A connected sequence of curve segments in 2D.
 *
 * @param id step id
 * @param name step label
 * @param segments ordered list of composite curve segments
 * @param selfIntersect whether the curve self-intersects
 */
public final class StepCompositeCurve2D implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepCompositeCurveSegment> segments;
    private final boolean selfIntersect;

    public StepCompositeCurve2D(int id, String name, List<StepCompositeCurveSegment> segments, boolean selfIntersect) {
        this.id = id;
        this.name = name;
        this.segments = segments == null ? null : java.util.List.copyOf(segments);
        this.selfIntersect = selfIntersect;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepCompositeCurveSegment> getSegments() {
        return segments;
    }

    public boolean isSelfIntersect() {
        return selfIntersect;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepCompositeCurveSegment> segments() { return getSegments(); }
    public boolean selfIntersect() { return isSelfIntersect(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCompositeCurve2D that = (StepCompositeCurve2D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(segments, that.segments) && selfIntersect == that.selfIntersect;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, segments, selfIntersect);
    }

    @Override
    public String toString() {
        return "StepCompositeCurve2D{" + "id=" + id + "name=" + name + "segments=" + segments + "selfIntersect=" + selfIntersect + "}";
    }
}
