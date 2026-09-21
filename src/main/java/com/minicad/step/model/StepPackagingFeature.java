package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PACKAGING_FEATURE.
 * A packaging feature entity.
 *
 * @param id STEP instance id
 * @param name packaging name
 * @param packagingType packaging type (box, pallet, crate)
 * @param packagingGeometry packaging geometry representation
 * @param packagingMaterial packaging material specification
 * @param packagingWeight packaging weight
 * @param packagingDimensions packaging dimensions (L, W, H)
 * @param packagingStandard packaging standard reference
 */
public final class StepPackagingFeature extends AbstractStepEntity {
    private final String packagingType;
    private final StepEntity packagingGeometry;
    private final StepEntity packagingMaterial;
    private final double packagingWeight;
    private final List<Double> packagingDimensions;
    private final String packagingStandard;

    public StepPackagingFeature(int id, String name, String packagingType, StepEntity packagingGeometry, StepEntity packagingMaterial, double packagingWeight, List<Double> packagingDimensions, String packagingStandard) {
        super(id, name);
        this.packagingType = packagingType;
        this.packagingGeometry = packagingGeometry;
        this.packagingMaterial = packagingMaterial;
        this.packagingWeight = packagingWeight;
        this.packagingDimensions = packagingDimensions == null ? null : java.util.List.copyOf(packagingDimensions);
        this.packagingStandard = packagingStandard;
    }

    public String getPackagingType() {
        return packagingType;
    }

    public StepEntity getPackagingGeometry() {
        return packagingGeometry;
    }

    public StepEntity getPackagingMaterial() {
        return packagingMaterial;
    }

    public double getPackagingWeight() {
        return packagingWeight;
    }

    public List<Double> getPackagingDimensions() {
        return packagingDimensions;
    }

    public String getPackagingStandard() {
        return packagingStandard;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("packagingType", packagingType);
        state.put("packagingGeometry", packagingGeometry);
        state.put("packagingMaterial", packagingMaterial);
        state.put("packagingWeight", packagingWeight);
        state.put("packagingDimensions", packagingDimensions);
        state.put("packagingStandard", packagingStandard);
        return state;
    }
}
