package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved DATUM_SYSTEM_REFERENCE.
 * A datum system reference entity.
 *
 * @param id STEP instance id
 * @param name datum system name
 * @param datumSystem the datum system being referenced
 * @param precedenceLevel precedence level in the datum system
 */
public record StepDatumSystemReference(
    int id,
    String name,
    StepEntity datumSystem,
    int precedenceLevel) implements StepEntity {
}