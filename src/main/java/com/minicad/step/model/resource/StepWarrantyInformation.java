package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved WARRANTY_INFORMATION.
 * A warranty information entity.
 *
 * @param id STEP instance id
 * @param name warranty name
 * @param warrantyType warranty type (standard, extended, service)
 * @param warrantyPeriod warranty period duration
 * @param warrantyStart warranty start date
 * @param warrantyEnd warranty end date
 * @varianceConditions warranty variance conditions
 * @param warrantyProvider warranty provider reference
 * @param warrantyStatus warranty status (active, expired)
 */
public record StepWarrantyInformation(
    int id,
    String name,
    String warrantyType,
    double warrantyPeriod,
    StepEntity warrantyStart,
    StepEntity warrantyEnd,
    List<String> varianceConditions,
    StepEntity warrantyProvider,
    String warrantyStatus) implements StepEntity {
}