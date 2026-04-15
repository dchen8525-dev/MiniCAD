package com.minicad.step.model;

import java.util.List;

/**
 * Resolved EQUIPMENT_STOCK.
 * An equipment stock entity.
 *
 * @param id STEP instance id
 * @param name stock name
 * @param equipmentType equipment type classification
 * @varianceQuantity stock variance quantity available
 * @varianceCondition equipment variance condition
 * @varianceLocation stock variance location
 * @varianceValue equipment variance value
 * @varianceMaintenance last variance maintenance date
 * @varianceStatus stock variance status
 */
public record StepEquipmentStock(
    int id,
    String name,
    String equipmentType,
    int varianceQuantity,
    String varianceCondition,
    String varianceLocation,
    double varianceValue,
    StepEntity varianceMaintenance,
    String varianceStatus) implements StepEntity {
}