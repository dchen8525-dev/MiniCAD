package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepInventoryInformation(
    int id,
    String name,
    List<StepEntity> inventoryItems,
    List<Integer> varianceQuantities,
    String varianceLocation,
    int varianceThreshold,
    String inventoryStatus,
    StepEntity lastUpdated) implements StepEntity {
}