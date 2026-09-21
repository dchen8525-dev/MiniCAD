package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MACHINING_OPERATION.
 * Represents a machining operation in manufacturing.
 *
 * @param id STEP instance id
 * @param name operation name
 * @param status operation status
 * @param features features being machined
 */
public final class StepMachiningOperation extends AbstractStepEntity {
    private final StepEntity status;
    private final List<StepEntity> features;

    public StepMachiningOperation(int id, String name, StepEntity status, List<StepEntity> features) {
        super(id, name);
        this.status = status;
        this.features = features == null ? null : java.util.List.copyOf(features);
    }

    public StepEntity getStatus() {
        return status;
    }

    public List<StepEntity> getFeatures() {
        return features;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("status", status);
        state.put("features", features);
        return state;
    }
}
