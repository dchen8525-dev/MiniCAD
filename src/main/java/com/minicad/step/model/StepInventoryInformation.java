package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved INVENTORY_INFORMATION.
 * An inventory information entity.
 *
 * @param id STEP instance id
 * @param name inventory name
 * @param inventoryItems inventory items
 * @varianceQuantities item variance quantities in stock
 * @varianceLocation inventory variance location
 * @varianceThreshold reorder variance threshold
 * @param inventoryStatus inventory status
 * @param lastUpdated last update date
 */
/**
 * Resolved INVENTORY_INFORMATION.
 * An inventory information entity.
 *
 * @param id STEP instance id
 * @param name inventory name
 * @param inventoryItems inventory items
 * @varianceQuantities item variance quantities in stock
 * @varianceLocation inventory variance location
 * @varianceThreshold reorder variance threshold
 * @param inventoryStatus inventory status
 * @param lastUpdated last update date
 */
public final class StepInventoryInformation implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> inventoryItems;
    private final List<Integer> varianceQuantities;
    private final String varianceLocation;
    private final int varianceThreshold;
    private final String inventoryStatus;
    private final StepEntity lastUpdated;

    public StepInventoryInformation(int id, String name, List<StepEntity> inventoryItems, List<Integer> varianceQuantities, String varianceLocation, int varianceThreshold, String inventoryStatus, StepEntity lastUpdated) {
        this.id = id;
        this.name = name;
        this.inventoryItems = inventoryItems == null ? null : java.util.List.copyOf(inventoryItems);
        this.varianceQuantities = varianceQuantities == null ? null : java.util.List.copyOf(varianceQuantities);
        this.varianceLocation = varianceLocation;
        this.varianceThreshold = varianceThreshold;
        this.inventoryStatus = inventoryStatus;
        this.lastUpdated = lastUpdated;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getInventoryItems() {
        return inventoryItems;
    }

    public List<Integer> getVarianceQuantities() {
        return varianceQuantities;
    }

    public String getVarianceLocation() {
        return varianceLocation;
    }

    public int getVarianceThreshold() {
        return varianceThreshold;
    }

    public String getInventoryStatus() {
        return inventoryStatus;
    }

    public StepEntity getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepInventoryInformation that = (StepInventoryInformation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(inventoryItems, that.inventoryItems) && Objects.equals(varianceQuantities, that.varianceQuantities) && Objects.equals(varianceLocation, that.varianceLocation) && varianceThreshold == that.varianceThreshold && Objects.equals(inventoryStatus, that.inventoryStatus) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, inventoryItems, varianceQuantities, varianceLocation, varianceThreshold, inventoryStatus, lastUpdated);
    }

    @Override
    public String toString() {
        return "StepInventoryInformation{" + "id=" + id + "name=" + name + "inventoryItems=" + inventoryItems + "varianceQuantities=" + varianceQuantities + "varianceLocation=" + varianceLocation + "varianceThreshold=" + varianceThreshold + "inventoryStatus=" + inventoryStatus + "lastUpdated=" + lastUpdated + "}";
    }
}