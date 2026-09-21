package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ASSET_DEFINITION.
 * An asset definition entity.
 *
 * @param id STEP instance id
 * @param name asset name
 * @param assetType asset variance type
 * @param assetCategory asset variance category
 * @param assetProperties asset variance properties
 * @param assetLifecycle asset variance lifecycle info
 * @param assetStatus asset variance status
 */
public final class StepAssetDefinition extends AbstractStepEntity {
    private final String assetType;
    private final String assetCategory;
    private final List<String> assetProperties;
    private final String assetLifecycle;
    private final String assetStatus;

    public StepAssetDefinition(int id, String name, String assetType, String assetCategory, List<String> assetProperties, String assetLifecycle, String assetStatus) {
        super(id, name);
        this.assetType = assetType;
        this.assetCategory = assetCategory;
        this.assetProperties = assetProperties == null ? null : java.util.List.copyOf(assetProperties);
        this.assetLifecycle = assetLifecycle;
        this.assetStatus = assetStatus;
    }

    public String getAssetType() {
        return assetType;
    }

    public String getAssetCategory() {
        return assetCategory;
    }

    public List<String> getAssetProperties() {
        return assetProperties;
    }

    public String getAssetLifecycle() {
        return assetLifecycle;
    }

    public String getAssetStatus() {
        return assetStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("assetType", assetType);
        state.put("assetCategory", assetCategory);
        state.put("assetProperties", assetProperties);
        state.put("assetLifecycle", assetLifecycle);
        state.put("assetStatus", assetStatus);
        return state;
    }
}
