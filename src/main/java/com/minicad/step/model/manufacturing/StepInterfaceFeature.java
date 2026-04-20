package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved INTERFACE_FEATURE.
 * An interface feature entity.
 *
 * @param id STEP instance id
 * @param name interface name
 * @param interfaceType interface type (mechanical, electrical, data)
 * @param interfaceGeometry interface geometry representation
 * @param interfacePosition interface position placement
 * @varianceConnections variance connections count
 * @param interfaceStandard interface standard reference
 * @param matingInterface mating interface reference
 */
public record StepInterfaceFeature(
    int id,
    String name,
    String interfaceType,
    StepEntity interfaceGeometry,
    StepEntity interfacePosition,
    int varianceConnections,
    String interfaceStandard,
    StepEntity matingInterface) implements StepEntity {
}