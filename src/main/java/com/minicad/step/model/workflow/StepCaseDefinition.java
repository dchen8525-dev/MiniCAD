package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CASE_DEFINITION.
 * A case definition entity.
 *
 * @param id STEP instance id
 * @param name case name
 * @param caseType case variance type
 * @param caseScenario case variance scenario
 * @param caseInputs case variance inputs
 * @param caseExpectedOutputs case variance expected outputs
 * @param caseStatus case variance status
 */
public record StepCaseDefinition(
    int id,
    String name,
    String caseType,
    StepEntity caseScenario,
    List<String> caseInputs,
    List<String> caseExpectedOutputs,
    String caseStatus) implements StepEntity {
}