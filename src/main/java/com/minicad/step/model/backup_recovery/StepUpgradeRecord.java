package com.minicad.step.model.backup_recovery;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved UPGRADE_RECORD.
 * An upgrade record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment upgraded variance equipment
 * @varianceFrom upgrade variance from version
 * @varianceTo upgrade variance to version
 * @varianceDate upgrade variance date
 * @varianceChanges upgrade variance changes
 * @varianceStatus record variance status
 */
public record StepUpgradeRecord(
    int id,
    String name,
    StepEntity varianceEquipment,
    String varianceFrom,
    String varianceTo,
    StepEntity varianceDate,
    List<String> varianceChanges,
    String varianceStatus) implements StepEntity {
}