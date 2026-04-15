package com.minicad.step.model;

import java.util.List;

/**
 * Resolved VERSION_HISTORY.
 * A version history entity.
 *
 * @param id STEP instance id
 * @param name history name
 * @varianceItem versioned variance item
 * @varianceVersions version variance entries
 * @varianceCurrent current variance version reference
 * @varianceAuthor version variance author
 * @varianceStatus history variance status
 */
public record StepVersionHistory(
    int id,
    String name,
    StepEntity varianceItem,
    List<StepEntity> varianceVersions,
    StepEntity varianceCurrent,
    StepEntity varianceAuthor,
    String varianceStatus) implements StepEntity {
}