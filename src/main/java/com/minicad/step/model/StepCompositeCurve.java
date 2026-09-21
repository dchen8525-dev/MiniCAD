package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COMPOSITE_CURVE.
 *
 * @param id STEP id
 * @param name STEP label
 * @param segments ordered curve segments
 * @param selfIntersect self-intersection flag
 */
public final class StepCompositeCurve extends AbstractStepEntity {
    private final List<StepCompositeCurveSegment> segments;
    private final boolean selfIntersect;

    public StepCompositeCurve(int id, String name, List<StepCompositeCurveSegment> segments, boolean selfIntersect) {
        super(id, name);
        this.segments = segments == null ? null : java.util.List.copyOf(segments);
        this.selfIntersect = selfIntersect;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("segments", segments);
        state.put("selfIntersect", selfIntersect);
        return state;
    }
}
