package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved BILL_OF_MATERIALS.
 * A bill of materials entity.
 *
 * @param id STEP instance id
 * @param name BOM name
 * @param bomId BOM identifier
 * @param bomItems BOM line items
 * @param bomQuantities quantities for each item
 * @param bomStructure BOM structure type (flat, hierarchical)
 * @varianceLevel BOM variance level in hierarchy
 * @param bomStatus BOM status (current, revision)
 */
public final class StepBillOfMaterials extends AbstractStepEntity {
    private final String bomId;
    private final List<StepEntity> bomItems;
    private final List<Integer> bomQuantities;
    private final String bomStructure;
    private final int varianceLevel;
    private final String bomStatus;

    public StepBillOfMaterials(int id, String name, String bomId, List<StepEntity> bomItems, List<Integer> bomQuantities, String bomStructure, int varianceLevel, String bomStatus) {
        super(id, name);
        this.bomId = bomId;
        this.bomItems = bomItems == null ? null : java.util.List.copyOf(bomItems);
        this.bomQuantities = bomQuantities == null ? null : java.util.List.copyOf(bomQuantities);
        this.bomStructure = bomStructure;
        this.varianceLevel = varianceLevel;
        this.bomStatus = bomStatus;
    }

    public String getBomId() {
        return bomId;
    }

    public List<StepEntity> getBomItems() {
        return bomItems;
    }

    public List<Integer> getBomQuantities() {
        return bomQuantities;
    }

    public String getBomStructure() {
        return bomStructure;
    }

    public int getVarianceLevel() {
        return varianceLevel;
    }

    public String getBomStatus() {
        return bomStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "bomId", bomId, "bomItems", bomItems,
                "bomQuantities", bomQuantities, "bomStructure", bomStructure, "varianceLevel", varianceLevel,
                "bomStatus", bomStatus);
    }
}
