package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal EFFECTIVITY metadata.
 *
 * @param id STEP instance id
 * @param effectivityId effectivity identifier
 */
public final class StepEffectivity extends AbstractStepEntity {
    private final String effectivityId;

    public StepEffectivity(int id, String effectivityId) {
        super(id, "");
        this.effectivityId = effectivityId;
    }

    public String getEffectivityId() {
        return effectivityId;
    }

    public String getName() {
        return effectivityId != null ? effectivityId : "";
    }

    // Record-style accessor - name from effectivityId
    public String name() {
        return effectivityId;
    }

    public String effectivityId() {
        return effectivityId;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "effectivityId", effectivityId);
    }
}
