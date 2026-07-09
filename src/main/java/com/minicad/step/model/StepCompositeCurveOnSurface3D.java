package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COMPOSITE_CURVE_ON_SURFACE_3D.
 * A composite curve that lies on a 3D surface.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param segments composite curve segments
 * @param surface the surface on which the curve lies
 * @param selfIntersect whether the curve self-intersects
 */
/**
 * Resolved COMPOSITE_CURVE_ON_SURFACE_3D.
 * A composite curve that lies on a 3D surface.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param segments composite curve segments
 * @param surface the surface on which the curve lies
 * @param selfIntersect whether the curve self-intersects
 */
public final class StepCompositeCurveOnSurface3D implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepCompositeCurveSegment> segments;
    private final StepEntity surface;
    private final boolean selfIntersect;

    public StepCompositeCurveOnSurface3D(int id, String name, List<StepCompositeCurveSegment> segments, StepEntity surface, boolean selfIntersect) {
        this.id = id;
        this.name = name;
        this.segments = segments == null ? null : java.util.List.copyOf(segments);
        this.surface = surface;
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

    public StepEntity getSurface() {
        return surface;
    }

    public boolean isSelfIntersect() {
        return selfIntersect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCompositeCurveOnSurface3D that = (StepCompositeCurveOnSurface3D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(segments, that.segments) && Objects.equals(surface, that.surface) && selfIntersect == that.selfIntersect;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, segments, surface, selfIntersect);
    }

    @Override
    public String toString() {
        return "StepCompositeCurveOnSurface3D{" + "id=" + id + "name=" + name + "segments=" + segments + "surface=" + surface + "selfIntersect=" + selfIntersect + "}";
    }
}
