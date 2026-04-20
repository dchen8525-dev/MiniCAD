package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CORRECTIVE_ACTION.
 * A corrective action entity.
 *
 * @param id STEP instance id
 * @param name action name
 * @varianceProblem identified variance problem
 * @varianceRootCause root variance cause
 * @varianceAction corrective variance action description
 * @varianceResponsible responsible variance person
 * @varianceTarget target variance completion date
 * @varianceStatus action variance status
 * @varianceEffectiveness effectiveness variance verification
 */
public record StepCorrectiveAction(
    int id,
    String name,
    StepEntity varianceProblem,
    String varianceRootCause,
    String varianceAction,
    StepEntity varianceResponsible,
    StepEntity varianceTarget,
    String varianceStatus,
    String varianceEffectiveness) implements StepEntity {
}