package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved FORGING_FEATURE.
 * A forging feature entity.
 *
 * @param id STEP instance id
 * @param name forging name
 * @param forgingType forging type classification (open die, closed die, upset)
 * @param forgingGeometry forging geometry representation
 * @param dieFlash die flash allowance
 * @param forgingGrain grain direction specification
 * @param forgingMaterial forging material specification
 * @param forgingTemperature forging temperature range
 */
public final class StepForgingFeature extends AbstractStepEntity {
    private final String forgingType;
    private final StepEntity forgingGeometry;
    private final double dieFlash;
    private final StepEntity forgingGrain;
    private final StepEntity forgingMaterial;
    private final List<Double> forgingTemperature;

    public StepForgingFeature(int id, String name, String forgingType, StepEntity forgingGeometry, double dieFlash, StepEntity forgingGrain, StepEntity forgingMaterial, List<Double> forgingTemperature) {
        super(id, name);
        this.forgingType = forgingType;
        this.forgingGeometry = forgingGeometry;
        this.dieFlash = dieFlash;
        this.forgingGrain = forgingGrain;
        this.forgingMaterial = forgingMaterial;
        this.forgingTemperature = forgingTemperature == null ? null : java.util.List.copyOf(forgingTemperature);
    }

    public String getForgingType() {
        return forgingType;
    }

    public StepEntity getForgingGeometry() {
        return forgingGeometry;
    }

    public double getDieFlash() {
        return dieFlash;
    }

    public StepEntity getForgingGrain() {
        return forgingGrain;
    }

    public StepEntity getForgingMaterial() {
        return forgingMaterial;
    }

    public List<Double> getForgingTemperature() {
        return forgingTemperature;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "forgingType", forgingType,
                "forgingGeometry", forgingGeometry, "dieFlash", dieFlash, "forgingGrain", forgingGrain,
                "forgingMaterial", forgingMaterial, "forgingTemperature", forgingTemperature);
    }
}
