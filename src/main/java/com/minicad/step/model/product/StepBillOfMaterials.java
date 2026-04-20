package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepBillOfMaterials(
    int id,
    String name,
    String bomId,
    List<StepEntity> bomItems,
    List<Integer> bomQuantities,
    String bomStructure,
    int varianceLevel,
    String bomStatus) implements StepEntity {
}