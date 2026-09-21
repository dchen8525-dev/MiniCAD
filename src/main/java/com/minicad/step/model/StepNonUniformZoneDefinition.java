package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved NON_UNIFORM_ZONE_DEFINITION.
 * A tolerance zone definition that varies non-uniformly across the feature.
 */
public final class StepNonUniformZoneDefinition extends AbstractStepEntity {
    private final String zoneType;
    private final StepEntity definingCurve;
    private final Double variationMagnitude;

    public StepNonUniformZoneDefinition(int id, String name, String zoneType, StepEntity definingCurve, Double variationMagnitude) {
        super(id, name);
        this.zoneType = zoneType;
        this.definingCurve = definingCurve;
        this.variationMagnitude = variationMagnitude;
    }

    public String getZoneType() {
        return zoneType;
    }

    public StepEntity getDefiningCurve() {
        return definingCurve;
    }

    public Double getVariationMagnitude() {
        return variationMagnitude;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("zoneType", zoneType);
        state.put("definingCurve", definingCurve);
        state.put("variationMagnitude", variationMagnitude);
        return state;
    }
}
