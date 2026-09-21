package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FEATURE_CONTROL_FRAME.
 * A GD&T feature control frame containing tolerances and datum references.
 *
 * @param id STEP instance id
 * @param name frame name
 * @param datumSystem datum references
 * @param tolerance the geometric tolerance value
 */
public final class StepFeatureControlFrame extends AbstractStepEntity {
    private final List<StepEntity> datumSystem;
    private final StepEntity tolerance;

    public StepFeatureControlFrame(int id, String name, List<StepEntity> datumSystem, StepEntity tolerance) {
        super(id, name);
        this.datumSystem = datumSystem == null ? null : java.util.List.copyOf(datumSystem);
        this.tolerance = tolerance;
    }

    public List<StepEntity> getDatumSystem() {
        return datumSystem;
    }

    public StepEntity getTolerance() {
        return tolerance;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("datumSystem", datumSystem);
        state.put("tolerance", tolerance);
        return state;
    }
}
