package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved LOCATOR_FEATURE.
 * A locator feature entity.
 *
 * @param id STEP instance id
 * @param name locator name
 * @param locatorType locator type (pin, surface, datum)
 * @param locatorGeometry locator geometry representation
 * @param locatorPosition locator position placement
 * @varianceTolerance locator variance tolerance
 * @param locatorMaterial locator material reference
 */
public record StepLocatorFeature(
    int id,
    String name,
    String locatorType,
    StepEntity locatorGeometry,
    StepEntity locatorPosition,
    double varianceTolerance,
    StepEntity locatorMaterial) implements StepEntity {
}