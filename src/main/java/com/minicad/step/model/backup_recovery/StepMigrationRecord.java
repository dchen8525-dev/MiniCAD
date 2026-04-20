package com.minicad.step.model.backup_recovery;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved MIGRATION_RECORD.
 * A migration record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceData migrated variance data
 * @varianceFrom migration variance source
 * @varianceTo migration variance destination
 * @varianceDate migration variance date
 * @varianceFormat migration variance format conversion
 * @varianceStatus record variance status
 */
public record StepMigrationRecord(
    int id,
    String name,
    StepEntity varianceData,
    String varianceFrom,
    String varianceTo,
    StepEntity varianceDate,
    String varianceFormat,
    String varianceStatus) implements StepEntity {
}