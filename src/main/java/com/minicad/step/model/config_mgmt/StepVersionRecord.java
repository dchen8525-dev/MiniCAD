package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved VERSION_RECORD.
 * A version record entity.
 *
 * @param id STEP instance id
 * @param name version name
 * @param versionNumber version variance number
 * @param versionLabel version variance label
 * @param versionTarget version variance target reference
 * @param versionAuthor version variance author reference
 * @param versionTimestamp version variance timestamp
 * @param versionStatus version variance status
 */
public record StepVersionRecord(
    int id,
    String name,
    String versionNumber,
    String versionLabel,
    StepEntity versionTarget,
    StepEntity versionAuthor,
    StepEntity versionTimestamp,
    String versionStatus) implements StepEntity {
}