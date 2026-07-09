package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepFeaturePatternInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity patternDef;
    private final StepEntity instancePosition;
    private final int instanceIndex;
    private final StepEntity replicatedFeature;

    public StepFeaturePatternInstance(int id, String name, StepEntity patternDef, StepEntity instancePosition, int instanceIndex, StepEntity replicatedFeature) {
        this.id = id;
        this.name = name;
        this.patternDef = patternDef;
        this.instancePosition = instancePosition;
        this.instanceIndex = instanceIndex;
        this.replicatedFeature = replicatedFeature;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaturePatternInstance that = (StepFeaturePatternInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(patternDef, that.patternDef) && Objects.equals(instancePosition, that.instancePosition) && instanceIndex == that.instanceIndex && Objects.equals(replicatedFeature, that.replicatedFeature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, patternDef, instancePosition, instanceIndex, replicatedFeature);
    }

    @Override
    public String toString() {
        return "StepFeaturePatternInstance{" + "id=" + id + "name=" + name + "patternDef=" + patternDef + "instancePosition=" + instancePosition + "instanceIndex=" + instanceIndex + "replicatedFeature=" + replicatedFeature + "}";
    }
}