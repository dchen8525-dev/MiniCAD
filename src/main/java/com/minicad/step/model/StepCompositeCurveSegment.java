package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved COMPOSITE_CURVE_SEGMENT.
 *
 * @param id STEP id
 * @param transition transition-code enum
 * @param sameSense same-sense flag
 * @param parentCurve parent curve
 */
public final class StepCompositeCurveSegment extends AbstractStepEntity {
    private final String transition;
    private final boolean sameSense;
    private final StepEntity parentCurve;

    public StepCompositeCurveSegment(int id, String name, String transition, boolean sameSense, StepEntity parentCurve) {
        super(id, name != null ? name : "");
        this.transition = transition;
        this.sameSense = sameSense;
        this.parentCurve = parentCurve;
    }

    public StepCompositeCurveSegment(int id, String transition, boolean sameSense, StepEntity parentCurve) {
        this(id, "", transition, sameSense, parentCurve);
    }

    public String getTransition() {
        return transition;
    }

    public boolean isSameSense() {
        return sameSense;
    }

    public StepEntity getParentCurve() {
        return parentCurve;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public String transition() { return getTransition(); }
    public boolean sameSense() { return isSameSense(); }
    public StepEntity parentCurve() { return getParentCurve(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("transition", transition);
        state.put("sameSense", sameSense);
        state.put("parentCurve", parentCurve);
        return state;
    }
}
