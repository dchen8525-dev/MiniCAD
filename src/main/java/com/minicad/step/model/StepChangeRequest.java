package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CHANGE_REQUEST.
 * A change request entity.
 *
 * @param id STEP instance id
 * @param name request name
 * @param requestType change request type
 * @param requestDescription change request description
 * @param affectedItems items affected by change
 * @param requestStatus request status (pending, approved, rejected)
 * @param requestDate request submission date
 * @param requestAuthor request author
 * @param requestReason reason for change request
 */
public record StepChangeRequest(
    int id,
    String name,
    String requestType,
    String requestDescription,
    List<StepEntity> affectedItems,
    String requestStatus,
    StepEntity requestDate,
    StepEntity requestAuthor,
    String requestReason) implements StepEntity {
}