package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved MATERIAL_PROPERTY_REPRESENTATION.
 * A material property representation entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param propertyName property variance name
 * @param propertyValue property variance value
 * @param propertyUnit property variance unit reference
 * @param propertyStatus property variance status
 */
public record StepMaterialPropertyRepresentation(
    int id,
    String name,
    String propertyName,
    double propertyValue,
    StepEntity propertyUnit,
    String propertyStatus) implements StepEntity {
}