package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepAssetDefinition(
    int id,
    String name,
    String assetType,
    String assetCategory,
    List<String> assetProperties,
    String assetLifecycle,
    String assetStatus) implements StepEntity {
}