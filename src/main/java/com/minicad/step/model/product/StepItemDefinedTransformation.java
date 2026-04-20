package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.geometry.StepAxis2Placement3D;
/**
 * Minimal item-defined transformation between two placement items.
 *
 * @param id STEP instance id
 * @param name transformation name
 * @param description optional description
 * @param transformItem1 source placement item
 * @param transformItem2 target placement item
 */
public record StepItemDefinedTransformation(
        int id,
        String name,
        String description,
        StepAxis2Placement3D transformItem1,
        StepAxis2Placement3D transformItem2
) implements StepEntity {
}
