package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal PRODUCT_DEFINITION_EFFECTIVITY metadata.
 *
 * @param id STEP instance id
 * @param effectivityId effectivity identifier
 * @param usage usage text
 * @param productDefinition affected product definition
 */
public final class StepProductDefinitionEffectivity extends AbstractStepEntity {
    private final String effectivityId;
    private final String usage;
    private final StepProductDefinition productDefinition;

    public StepProductDefinitionEffectivity(int id, String effectivityId, String usage, StepProductDefinition productDefinition) {
        super(id, "");
        this.effectivityId = effectivityId;
        this.usage = usage;
        this.productDefinition = productDefinition;
    }

    public String getEffectivityId() {
        return effectivityId;
    }

    public String getUsage() {
        return usage;
    }

    public StepProductDefinition getProductDefinition() {
        return productDefinition;
    }

    // Record-style accessors
    public String effectivityId() {
        return effectivityId;
    }

    public String usage() {
        return usage;
    }

    public StepProductDefinition productDefinition() {
        return productDefinition;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("effectivityId", effectivityId);
        state.put("usage", usage);
        state.put("productDefinition", productDefinition);
        return state;
    }
}
