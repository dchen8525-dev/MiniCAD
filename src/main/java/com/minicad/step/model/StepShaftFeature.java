package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SHAFT_FEATURE.
 * A shaft feature entity.
 *
 * @param id STEP instance id
 * @param name shaft name
 * @param shaftDiameter shaft diameter
 * @param shaftLength shaft length
 * @param shaftType shaft type classification (solid, hollow, stepped)
 * @param features features on the shaft (keyways, threads, grooves)
 * @param shaftMaterial shaft material specification
 * @param surfaceTreatment surface treatment specification
 */
public final class StepShaftFeature extends AbstractStepEntity {
    private final double shaftDiameter;
    private final double shaftLength;
    private final String shaftType;
    private final List<StepEntity> features;
    private final StepEntity shaftMaterial;
    private final StepEntity surfaceTreatment;

    public StepShaftFeature(int id, String name, double shaftDiameter, double shaftLength, String shaftType, List<StepEntity> features, StepEntity shaftMaterial, StepEntity surfaceTreatment) {
        super(id, name);
        this.shaftDiameter = shaftDiameter;
        this.shaftLength = shaftLength;
        this.shaftType = shaftType;
        this.features = features == null ? null : java.util.List.copyOf(features);
        this.shaftMaterial = shaftMaterial;
        this.surfaceTreatment = surfaceTreatment;
    }

    public double getShaftDiameter() {
        return shaftDiameter;
    }

    public double getShaftLength() {
        return shaftLength;
    }

    public String getShaftType() {
        return shaftType;
    }

    public List<StepEntity> getFeatures() {
        return features;
    }

    public StepEntity getShaftMaterial() {
        return shaftMaterial;
    }

    public StepEntity getSurfaceTreatment() {
        return surfaceTreatment;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("shaftDiameter", shaftDiameter);
        state.put("shaftLength", shaftLength);
        state.put("shaftType", shaftType);
        state.put("features", features);
        state.put("shaftMaterial", shaftMaterial);
        state.put("surfaceTreatment", surfaceTreatment);
        return state;
    }
}
