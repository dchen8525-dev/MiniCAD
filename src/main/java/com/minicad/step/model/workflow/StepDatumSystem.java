package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved DATUM_SYSTEM.
 * A datum system entity with multiple datum references.
 *
 * @param id STEP instance id
 * @param name system name
 * @param datums ordered list of datums in the system
 * @param systemType datum system type classification
 * @param tolerance tolerance that uses this datum system
 */
public record StepDatumSystem(
    int id,
    String name,
    List<StepEntity> datums,
    String systemType,
    StepEntity tolerance) implements StepEntity {
}