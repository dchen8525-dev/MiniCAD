package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FEATURE_PATTERN.
 * Represents a feature pattern definition in manufacturing.
 *
 * @param id STEP instance id
 * @param name pattern name
 * @param baseFeature base feature being patterned
 * @param patternType pattern type (linear, circular, mirror, etc)
 * @param parameters pattern parameters (spacing, count, angle, etc)
 */
/**
 * Resolved FEATURE_PATTERN.
 * Represents a feature pattern definition in manufacturing.
 *
 * @param id STEP instance id
 * @param name pattern name
 * @param baseFeature base feature being patterned
 * @param patternType pattern type (linear, circular, mirror, etc)
 * @param parameters pattern parameters (spacing, count, angle, etc)
 */
public final class StepFeaturePattern implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity baseFeature;
    private final String patternType;
    private final List<Double> parameters;

    public StepFeaturePattern(int id, String name, StepEntity baseFeature, String patternType, List<Double> parameters) {
        this.id = id;
        this.name = name;
        this.baseFeature = baseFeature;
        this.patternType = patternType;
        this.parameters = parameters == null ? null : java.util.List.copyOf(parameters);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getBaseFeature() {
        return baseFeature;
    }

    public String getPatternType() {
        return patternType;
    }

    public List<Double> getParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaturePattern that = (StepFeaturePattern) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(baseFeature, that.baseFeature) && Objects.equals(patternType, that.patternType) && Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, baseFeature, patternType, parameters);
    }

    @Override
    public String toString() {
        return "StepFeaturePattern{" + "id=" + id + "name=" + name + "baseFeature=" + baseFeature + "patternType=" + patternType + "parameters=" + parameters + "}";
    }
}