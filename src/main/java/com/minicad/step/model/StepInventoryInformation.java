package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepInventoryInformation extends AbstractStepEntity {
    private final List<StepEntity> inventoryItems;
    private final List<Integer> varianceQuantities;
    private final String varianceLocation;
    private final int varianceThreshold;
    private final String inventoryStatus;
    private final StepEntity lastUpdated;

    public StepInventoryInformation(int id, String name, List<StepEntity> inventoryItems, List<Integer> varianceQuantities, String varianceLocation, int varianceThreshold, String inventoryStatus, StepEntity lastUpdated) {
        super(id, name);
        this.inventoryItems = inventoryItems == null ? null : java.util.List.copyOf(inventoryItems);
        this.varianceQuantities = varianceQuantities == null ? null : java.util.List.copyOf(varianceQuantities);
        this.varianceLocation = varianceLocation;
        this.varianceThreshold = varianceThreshold;
        this.inventoryStatus = inventoryStatus;
        this.lastUpdated = lastUpdated;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("inventoryItems", inventoryItems);
        state.put("varianceQuantities", varianceQuantities);
        state.put("varianceLocation", varianceLocation);
        state.put("varianceThreshold", varianceThreshold);
        state.put("inventoryStatus", inventoryStatus);
        state.put("lastUpdated", lastUpdated);
        return state;
    }
}
