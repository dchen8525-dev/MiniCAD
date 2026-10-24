package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepFeaturePattern extends AbstractStepEntity {
    private final StepEntity baseFeature;
    private final String patternType;
    private final List<Double> parameters;

    public StepFeaturePattern(int id, String name, StepEntity baseFeature, String patternType, List<Double> parameters) {
        super(id, name);
        this.baseFeature = baseFeature;
        this.patternType = patternType;
        this.parameters = parameters == null ? null : java.util.List.copyOf(parameters);
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "baseFeature", baseFeature, "patternType", patternType,
                "parameters", parameters);
    }
}
