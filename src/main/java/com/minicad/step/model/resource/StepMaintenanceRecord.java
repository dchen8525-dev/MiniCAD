package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved MAINTENANCE_RECORD.
 * A maintenance record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment maintained variance equipment
 * @varianceType maintenance variance type
 * @varianceDate maintenance variance date
 * @varianceActions maintenance variance actions
 * @varianceParts maintenance variance parts used
 * @varianceCost maintenance variance cost
 * @varianceStatus record variance status
 */
public record StepMaintenanceRecord(
    int id,
    String name,
    StepEntity varianceEquipment,
    String varianceType,
    StepEntity varianceDate,
    List<String> varianceActions,
    List<StepEntity> varianceParts,
    double varianceCost,
    String varianceStatus) implements StepEntity {
}