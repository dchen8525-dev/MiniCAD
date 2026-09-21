package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepCompositeCurveOnSurface3D extends AbstractStepEntity {
    private final List<StepCompositeCurveSegment> segments;
    private final StepEntity surface;
    private final boolean selfIntersect;

    public StepCompositeCurveOnSurface3D(int id, String name, List<StepCompositeCurveSegment> segments, StepEntity surface, boolean selfIntersect) {
        super(id, name);
        this.segments = segments == null ? null : java.util.List.copyOf(segments);
        this.surface = surface;
        this.selfIntersect = selfIntersect;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("segments", segments);
        state.put("surface", surface);
        state.put("selfIntersect", selfIntersect);
        return state;
    }
}
