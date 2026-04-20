package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TOOL_STOCK.
 * A tool stock entity.
 *
 * @param id STEP instance id
 * @param name stock name
 * @param toolType tool type classification
 * @varianceQuantity stock variance quantity available
 * @varianceCondition tool variance condition (new, used, reconditioned)
 * @varianceLocation stock variance location
 * @varianceCost unit variance cost
 * @varianceLife tool variance expected life
 * @varianceStatus stock variance status
 */
public record StepToolStock(
    int id,
    String name,
    String toolType,
    int varianceQuantity,
    String varianceCondition,
    String varianceLocation,
    double varianceCost,
    double varianceLife,
    String varianceStatus) implements StepEntity {
}