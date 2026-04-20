package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved MATERIAL_STOCK.
 * A material stock entity.
 *
 * @param id STEP instance id
 * @param name stock name
 * @param materialType material type classification
 * @varianceQuantity stock variance quantity available
 * @varianceUnit quantity variance unit
 * @varianceLocation stock variance location
 * @varianceCost unit variance cost
 * @varianceLeadTime procurement variance lead time
 * @varianceStatus stock variance status
 */
public record StepMaterialStock(
    int id,
    String name,
    String materialType,
    int varianceQuantity,
    StepEntity varianceUnit,
    String varianceLocation,
    double varianceCost,
    double varianceLeadTime,
    String varianceStatus) implements StepEntity {
}