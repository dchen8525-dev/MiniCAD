package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepAssetDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String assetType;
    private final String assetCategory;
    private final List<String> assetProperties;
    private final String assetLifecycle;
    private final String assetStatus;

    public StepAssetDefinition(int id, String name, String assetType, String assetCategory, List<String> assetProperties, String assetLifecycle, String assetStatus) {
        this.id = id;
        this.name = name;
        this.assetType = assetType;
        this.assetCategory = assetCategory;
        this.assetProperties = assetProperties == null ? null : java.util.List.copyOf(assetProperties);
        this.assetLifecycle = assetLifecycle;
        this.assetStatus = assetStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAssetDefinition that = (StepAssetDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(assetType, that.assetType) && Objects.equals(assetCategory, that.assetCategory) && Objects.equals(assetProperties, that.assetProperties) && Objects.equals(assetLifecycle, that.assetLifecycle) && Objects.equals(assetStatus, that.assetStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, assetType, assetCategory, assetProperties, assetLifecycle, assetStatus);
    }

    @Override
    public String toString() {
        return "StepAssetDefinition{" + "id=" + id + "name=" + name + "assetType=" + assetType + "assetCategory=" + assetCategory + "assetProperties=" + assetProperties + "assetLifecycle=" + assetLifecycle + "assetStatus=" + assetStatus + "}";
    }
}