package com.minicad.step.model;

import java.util.List;

/**
 * Resolved HISTORY_RECORD.
 * A history record entity.
 *
 * @param id STEP instance id
 * @param name history name
 * @param historyType history variance type
 * @param historyAction history variance action description
 * @param historyTarget history variance target reference
 * @param historyActor history variance actor reference
 * @param historyTimestamp history variance timestamp
 * @param historyStatus history variance status
 */
public record StepHistoryRecord(
    int id,
    String name,
    String historyType,
    String historyAction,
    StepEntity historyTarget,
    StepEntity historyActor,
    StepEntity historyTimestamp,
    String historyStatus) implements StepEntity {
}