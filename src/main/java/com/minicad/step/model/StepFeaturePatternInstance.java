package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FEATURE_PATTERN_INSTANCE.
 * A feature pattern instance entity.
 *
 * @param id STEP instance id
 * @param name instance name
 * @param patternDef pattern definition reference
 * @param instancePosition instance position in pattern
 * @param instanceIndex instance index number
 * @param replicatedFeature replicated feature at this position
 */
public final class StepFeaturePatternInstance extends AbstractStepEntity {
    private final StepEntity patternDef;
    private final StepEntity instancePosition;
    private final int instanceIndex;
    private final StepEntity replicatedFeature;

    public StepFeaturePatternInstance(int id, String name, StepEntity patternDef, StepEntity instancePosition, int instanceIndex, StepEntity replicatedFeature) {
        super(id, name);
        this.patternDef = patternDef;
        this.instancePosition = instancePosition;
        this.instanceIndex = instanceIndex;
        this.replicatedFeature = replicatedFeature;
    }

    public StepEntity getPatternDef() {
        return patternDef;
    }

    public StepEntity getInstancePosition() {
        return instancePosition;
    }

    public int getInstanceIndex() {
        return instanceIndex;
    }

    public StepEntity getReplicatedFeature() {
        return replicatedFeature;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("patternDef", patternDef);
        state.put("instancePosition", instancePosition);
        state.put("instanceIndex", instanceIndex);
        state.put("replicatedFeature", replicatedFeature);
        return state;
    }
}
