package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepAssetInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity assetDefinition;
    private final StepEntity assetLocation;
    private final String assetState;
    private final String assetCondition;
    private final List<StepEntity> assetMaintenanceRecords;
    private final String assetStatus;

    public StepAssetInstance(int id, String name, StepEntity assetDefinition, StepEntity assetLocation, String assetState, String assetCondition, List<StepEntity> assetMaintenanceRecords, String assetStatus) {
        this.id = id;
        this.name = name;
        this.assetDefinition = assetDefinition;
        this.assetLocation = assetLocation;
        this.assetState = assetState;
        this.assetCondition = assetCondition;
        this.assetMaintenanceRecords = assetMaintenanceRecords == null ? null : java.util.List.copyOf(assetMaintenanceRecords);
        this.assetStatus = assetStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAssetInstance that = (StepAssetInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(assetDefinition, that.assetDefinition) && Objects.equals(assetLocation, that.assetLocation) && Objects.equals(assetState, that.assetState) && Objects.equals(assetCondition, that.assetCondition) && Objects.equals(assetMaintenanceRecords, that.assetMaintenanceRecords) && Objects.equals(assetStatus, that.assetStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, assetDefinition, assetLocation, assetState, assetCondition, assetMaintenanceRecords, assetStatus);
    }

    @Override
    public String toString() {
        return "StepAssetInstance{" + "id=" + id + "name=" + name + "assetDefinition=" + assetDefinition + "assetLocation=" + assetLocation + "assetState=" + assetState + "assetCondition=" + assetCondition + "assetMaintenanceRecords=" + assetMaintenanceRecords + "assetStatus=" + assetStatus + "}";
    }
}