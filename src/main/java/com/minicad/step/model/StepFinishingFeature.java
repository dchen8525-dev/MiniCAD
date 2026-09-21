package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FINISHING_FEATURE.
 * A finishing feature entity.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param finishingType finishing type (polishing, grinding, honing, lapping)
 * @param surfaceGeometry surface geometry to be finished
 * @param surfaceRoughness target surface roughness (Ra)
 * @param finishingParameters finishing process parameters
 * @param finishingMaterial finishing material/tool reference
 */
public final class StepFinishingFeature extends AbstractStepEntity {
    private final String finishingType;
    private final StepEntity surfaceGeometry;
    private final double surfaceRoughness;
    private final List<Double> finishingParameters;
    private final StepEntity finishingMaterial;

    public StepFinishingFeature(int id, String name, String finishingType, StepEntity surfaceGeometry, double surfaceRoughness, List<Double> finishingParameters, StepEntity finishingMaterial) {
        super(id, name);
        this.finishingType = finishingType;
        this.surfaceGeometry = surfaceGeometry;
        this.surfaceRoughness = surfaceRoughness;
        this.finishingParameters = finishingParameters == null ? null : java.util.List.copyOf(finishingParameters);
        this.finishingMaterial = finishingMaterial;
    }

    public String getFinishingType() {
        return finishingType;
    }

    public StepEntity getSurfaceGeometry() {
        return surfaceGeometry;
    }

    public double getSurfaceRoughness() {
        return surfaceRoughness;
    }

    public List<Double> getFinishingParameters() {
        return finishingParameters;
    }

    public StepEntity getFinishingMaterial() {
        return finishingMaterial;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("finishingType", finishingType);
        state.put("surfaceGeometry", surfaceGeometry);
        state.put("surfaceRoughness", surfaceRoughness);
        state.put("finishingParameters", finishingParameters);
        state.put("finishingMaterial", finishingMaterial);
        return state;
    }
}
