package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TWO5D_MANUFACTURING_FEATURE.
 * Represents a 2.5D manufacturing feature (hole, slot, step, etc).
 *
 * @param id STEP instance id
 * @param name feature name
 * @param featureType type of manufacturing feature
 * @param profile profile definition
 * @param depth feature depth
 * @param direction direction of feature
 */
public final class StepTwo5DManufacturingFeature extends AbstractStepEntity {
    private final String featureType;
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;

    public StepTwo5DManufacturingFeature(int id, String name, String featureType, StepEntity profile, Double depth, StepEntity direction) {
        super(id, name);
        this.featureType = featureType;
        this.profile = profile;
        this.depth = depth;
        this.direction = direction;
    }

    public String getFeatureType() {
        return featureType;
    }

    public StepEntity getProfile() {
        return profile;
    }

    public Double getDepth() {
        return depth;
    }

    public StepEntity getDirection() {
        return direction;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("featureType", featureType);
        state.put("profile", profile);
        state.put("depth", depth);
        state.put("direction", direction);
        return state;
    }
}
