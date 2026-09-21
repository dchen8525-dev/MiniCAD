package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CASTING_FEATURE.
 * A casting feature entity.
 *
 * @param id STEP instance id
 * @param name casting name
 * @param castingType casting type classification (sand, investment, die casting)
 * @param moldGeometry mold geometry representation
 * @param gatingSystem gating system features
 * @param riserFeatures riser/feeder features
 * @param partingSurface parting surface geometry
 * @param castingMaterial casting material specification
 */
public final class StepCastingFeature extends AbstractStepEntity {
    private final String castingType;
    private final StepEntity moldGeometry;
    private final List<StepEntity> gatingSystem;
    private final List<StepEntity> riserFeatures;
    private final StepEntity partingSurface;
    private final StepEntity castingMaterial;

    public StepCastingFeature(int id, String name, String castingType, StepEntity moldGeometry, List<StepEntity> gatingSystem, List<StepEntity> riserFeatures, StepEntity partingSurface, StepEntity castingMaterial) {
        super(id, name);
        this.castingType = castingType;
        this.moldGeometry = moldGeometry;
        this.gatingSystem = gatingSystem == null ? null : java.util.List.copyOf(gatingSystem);
        this.riserFeatures = riserFeatures == null ? null : java.util.List.copyOf(riserFeatures);
        this.partingSurface = partingSurface;
        this.castingMaterial = castingMaterial;
    }

    public String getCastingType() {
        return castingType;
    }

    public StepEntity getMoldGeometry() {
        return moldGeometry;
    }

    public List<StepEntity> getGatingSystem() {
        return gatingSystem;
    }

    public List<StepEntity> getRiserFeatures() {
        return riserFeatures;
    }

    public StepEntity getPartingSurface() {
        return partingSurface;
    }

    public StepEntity getCastingMaterial() {
        return castingMaterial;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("castingType", castingType);
        state.put("moldGeometry", moldGeometry);
        state.put("gatingSystem", gatingSystem);
        state.put("riserFeatures", riserFeatures);
        state.put("partingSurface", partingSurface);
        state.put("castingMaterial", castingMaterial);
        return state;
    }
}
