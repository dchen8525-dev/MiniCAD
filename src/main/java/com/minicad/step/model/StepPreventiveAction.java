package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PREVENTIVE_ACTION.
 * A preventive action entity.
 *
 * @param id STEP instance id
 * @param name action name
 * @variancePotentialPotential variance issue
 * @varianceCause potential variance cause
 * @varianceAction preventive variance action description
 * @varianceResponsible responsible variance person
 * @varianceTarget target variance completion date
 * @varianceStatus action variance status
 * @varianceVerification verification variance method
 */
public record StepPreventiveAction(
    int id,
    String name,
    String variancePotential,
    String varianceCause,
    String varianceAction,
    StepEntity varianceResponsible,
    StepEntity varianceTarget,
    String varianceStatus,
    String varianceVerification) implements StepEntity {
}