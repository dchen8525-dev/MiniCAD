package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepAssetInstance extends AbstractStepEntity {
    private final StepEntity assetDefinition;
    private final StepEntity assetLocation;
    private final String assetState;
    private final String assetCondition;
    private final List<StepEntity> assetMaintenanceRecords;
    private final String assetStatus;

    public StepAssetInstance(int id, String name, StepEntity assetDefinition, StepEntity assetLocation, String assetState, String assetCondition, List<StepEntity> assetMaintenanceRecords, String assetStatus) {
        super(id, name);
        this.assetDefinition = assetDefinition;
        this.assetLocation = assetLocation;
        this.assetState = assetState;
        this.assetCondition = assetCondition;
        this.assetMaintenanceRecords = assetMaintenanceRecords == null ? null : java.util.List.copyOf(assetMaintenanceRecords);
        this.assetStatus = assetStatus;
    }

    public StepEntity getAssetDefinition() {
        return assetDefinition;
    }

    public StepEntity getAssetLocation() {
        return assetLocation;
    }

    public String getAssetState() {
        return assetState;
    }

    public String getAssetCondition() {
        return assetCondition;
    }

    public List<StepEntity> getAssetMaintenanceRecords() {
        return assetMaintenanceRecords;
    }

    public String getAssetStatus() {
        return assetStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "assetDefinition", assetDefinition,
                "assetLocation", assetLocation, "assetState", assetState, "assetCondition", assetCondition,
                "assetMaintenanceRecords", assetMaintenanceRecords, "assetStatus", assetStatus);
    }
}
