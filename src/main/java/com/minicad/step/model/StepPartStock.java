package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PART_STOCK.
 * A part stock entity.
 *
 * @param id STEP instance id
 * @param name stock name
 * @param partType part type/part number
 * @varianceQuantity stock variance quantity available
 * @varianceLocation stock variance location
 * @varianceCost unit variance cost
 * @varianceMin reorder variance minimum threshold
 * @varianceMax stock variance maximum limit
 * @varianceStatus stock variance status
 */
public record StepPartStock(
    int id,
    String name,
    String partType,
    int varianceQuantity,
    String varianceLocation,
    double varianceCost,
    int varianceMin,
    int varianceMax,
    String varianceStatus) implements StepEntity {
}