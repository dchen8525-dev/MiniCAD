package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CONFIGURATION_EFFECTIVITY.
 * Specifies when a configuration-managed item becomes effective.
 *
 * @param id STEP instance id
 * @param name effectivity name
 * @param configuration configuration item reference
 * @param itemConceived product definition being configured
 */
public final class StepConfigurationEffectivity extends AbstractStepEntity {
    private final StepEntity configuration;
    private final StepEntity itemConceived;

    public StepConfigurationEffectivity(int id, String name, StepEntity configuration, StepEntity itemConceived) {
        super(id, name);
        this.configuration = configuration;
        this.itemConceived = itemConceived;
    }

    public StepEntity getConfiguration() {
        return configuration;
    }

    public StepEntity getItemConceived() {
        return itemConceived;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("configuration", configuration);
        state.put("itemConceived", itemConceived);
        return state;
    }
}
