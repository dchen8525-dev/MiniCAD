package com.minicad.step.model;

import java.util.List;

/**
 * Resolved INSTALLATION_RECORD.
 * An installation record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment installed variance equipment
 * @varianceLocation installation variance location
 * @varianceDate installation variance date
 * @varianceInstaller installer variance person/team
 * @varianceChecks installation variance verification checks
 * @varianceStatus record variance status
 */
public record StepInstallationRecord(
    int id,
    String name,
    StepEntity varianceEquipment,
    String varianceLocation,
    StepEntity varianceDate,
    StepEntity varianceInstaller,
    List<String> varianceChecks,
    String varianceStatus) implements StepEntity {
}