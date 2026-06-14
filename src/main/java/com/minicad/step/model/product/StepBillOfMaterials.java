package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepBillOfMaterials implements StepEntity {
    private final int id;
    private final String name;
    private final String bomId;
    private final List<StepEntity> bomItems;
    private final List<Integer> bomQuantities;
    private final String bomStructure;
    private final int varianceLevel;
    private final String bomStatus;

    public StepBillOfMaterials(int id, String name, String bomId, List<StepEntity> bomItems, List<Integer> bomQuantities, String bomStructure, int varianceLevel, String bomStatus) {
        this.id = id;
        this.name = name;
        this.bomId = bomId;
        this.bomItems = bomItems == null ? null : java.util.List.copyOf(bomItems);
        this.bomQuantities = bomQuantities == null ? null : java.util.List.copyOf(bomQuantities);
        this.bomStructure = bomStructure;
        this.varianceLevel = varianceLevel;
        this.bomStatus = bomStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBillOfMaterials that = (StepBillOfMaterials) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(bomId, that.bomId) && Objects.equals(bomItems, that.bomItems) && Objects.equals(bomQuantities, that.bomQuantities) && Objects.equals(bomStructure, that.bomStructure) && varianceLevel == that.varianceLevel && Objects.equals(bomStatus, that.bomStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, bomId, bomItems, bomQuantities, bomStructure, varianceLevel, bomStatus);
    }

    @Override
    public String toString() {
        return "StepBillOfMaterials{" + "id=" + id + "name=" + name + "bomId=" + bomId + "bomItems=" + bomItems + "bomQuantities=" + bomQuantities + "bomStructure=" + bomStructure + "varianceLevel=" + varianceLevel + "bomStatus=" + bomStatus + "}";
    }
}