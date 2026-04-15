package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ASSET_INSTANCE.
 * An asset instance entity.
 *
 * @param id STEP instance id
 * @param name asset instance name
 * @param assetDefinition asset variance definition reference
 * @param assetLocation asset variance location reference
 * @param assetState asset variance state
 * @param assetCondition asset variance condition
 * @param assetMaintenanceRecords asset variance maintenance records
 * @param assetStatus asset variance status
 */
public record StepAssetInstance(
    int id,
    String name,
    StepEntity assetDefinition,
    StepEntity assetLocation,
    String assetState,
    String assetCondition,
    List<StepEntity> assetMaintenanceRecords,
    String assetStatus) implements StepEntity {
}