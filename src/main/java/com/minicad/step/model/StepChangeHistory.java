package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CHANGE_HISTORY.
 * A change history entity.
 *
 * @param id STEP instance id
 * @param name history name
 * @varianceItem changed variance item
 * @varianceChanges change variance entries
 * @varianceCurrent current variance state
 * @varianceBaseline baseline variance reference
 * @varianceStatus history variance status
 */
public record StepChangeHistory(
    int id,
    String name,
    StepEntity varianceItem,
    List<StepEntity> varianceChanges,
    StepEntity varianceCurrent,
    StepEntity varianceBaseline,
    String varianceStatus) implements StepEntity {
}