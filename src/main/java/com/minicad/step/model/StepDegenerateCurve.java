package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved DEGENERATE_CURVE.
 * A curve that has degenerated to a point or line.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param basisCurve the original curve before degeneration
 */
public final class StepDegenerateCurve extends AbstractStepEntity {
    private final StepEntity basisCurve;

    public StepDegenerateCurve(int id, String name, StepEntity basisCurve) {
        super(id, name);
        this.basisCurve = basisCurve;
    }

    public StepEntity getBasisCurve() {
        return basisCurve;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity basisCurve() { return getBasisCurve(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("basisCurve", basisCurve);
        return state;
    }
}
