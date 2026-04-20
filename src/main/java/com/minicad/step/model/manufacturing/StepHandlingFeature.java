package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved HANDLING_FEATURE.
 * A handling feature entity.
 *
 * @param id STEP instance id
 * @param name handling name
 * @param handlingType handling type (lift, grab, support, transport)
 * @param handlingGeometry handling geometry representation
 * @param handlingPoints handling point locations
 * @param handlingWeight handling weight capacity
 * @param handlingEquipment handling equipment reference
 */
public record StepHandlingFeature(
    int id,
    String name,
    String handlingType,
    StepEntity handlingGeometry,
    List<StepEntity> handlingPoints,
    double handlingWeight,
    StepEntity handlingEquipment) implements StepEntity {
}