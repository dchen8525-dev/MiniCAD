package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved HOUSING_FEATURE.
 * A housing feature entity.
 *
 * @param id STEP instance id
 * @param name housing name
 * @param housingType housing type classification
 * @param bearingSeats bearing seat features
 * @param mountingFeatures mounting features (bolt holes, dowels)
 * @param sealGrooves seal groove features
 * @param housingMaterial housing material specification
 * @param housingGeometry housing geometry representation
 */
public final class StepHousingFeature extends AbstractStepEntity {
    private final String housingType;
    private final List<StepEntity> bearingSeats;
    private final List<StepEntity> mountingFeatures;
    private final List<StepEntity> sealGrooves;
    private final StepEntity housingMaterial;
    private final StepEntity housingGeometry;

    public StepHousingFeature(int id, String name, String housingType, List<StepEntity> bearingSeats, List<StepEntity> mountingFeatures, List<StepEntity> sealGrooves, StepEntity housingMaterial, StepEntity housingGeometry) {
        super(id, name);
        this.housingType = housingType;
        this.bearingSeats = bearingSeats == null ? null : java.util.List.copyOf(bearingSeats);
        this.mountingFeatures = mountingFeatures == null ? null : java.util.List.copyOf(mountingFeatures);
        this.sealGrooves = sealGrooves == null ? null : java.util.List.copyOf(sealGrooves);
        this.housingMaterial = housingMaterial;
        this.housingGeometry = housingGeometry;
    }

    public String getHousingType() {
        return housingType;
    }

    public List<StepEntity> getBearingSeats() {
        return bearingSeats;
    }

    public List<StepEntity> getMountingFeatures() {
        return mountingFeatures;
    }

    public List<StepEntity> getSealGrooves() {
        return sealGrooves;
    }

    public StepEntity getHousingMaterial() {
        return housingMaterial;
    }

    public StepEntity getHousingGeometry() {
        return housingGeometry;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("housingType", housingType);
        state.put("bearingSeats", bearingSeats);
        state.put("mountingFeatures", mountingFeatures);
        state.put("sealGrooves", sealGrooves);
        state.put("housingMaterial", housingMaterial);
        state.put("housingGeometry", housingGeometry);
        return state;
    }
}
