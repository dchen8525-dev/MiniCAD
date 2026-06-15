package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COMPOSITE_CURVE.
 *
 * @param id STEP id
 * @param name STEP label
 * @param segments ordered curve segments
 * @param selfIntersect self-intersection flag
 */
/**
 * Resolved COMPOSITE_CURVE.
 *
 * @param id STEP id
 * @param name STEP label
 * @param segments ordered curve segments
 * @param selfIntersect self-intersection flag
 */
public final class StepCompositeCurve implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepCompositeCurveSegment> segments;
    private final boolean selfIntersect;

    public StepCompositeCurve(int id, String name, List<StepCompositeCurveSegment> segments, boolean selfIntersect) {
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
        StepCompositeCurve that = (StepCompositeCurve) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(segments, that.segments) && selfIntersect == that.selfIntersect;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, segments, selfIntersect);
    }

    @Override
    public String toString() {
        return "StepCompositeCurve{" + "id=" + id + "name=" + name + "segments=" + segments + "selfIntersect=" + selfIntersect + "}";
    }
}
