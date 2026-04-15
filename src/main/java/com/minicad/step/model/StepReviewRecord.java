package com.minicad.step.model;

import java.util.List;

/**
 * Resolved REVIEW_RECORD.
 * A review record entity.
 *
 * @param id STEP instance id
 * @param name review name
 * @param reviewType review variance type
 * @param reviewResult review variance result (approved/rejected)
 * @param reviewComments review variance comments
 * @param reviewReviewer review variance reviewer reference
 * @param reviewTimestamp review variance timestamp
 * @param reviewStatus review variance status
 */
public record StepReviewRecord(
    int id,
    String name,
    String reviewType,
    String reviewResult,
    List<String> reviewComments,
    StepEntity reviewReviewer,
    StepEntity reviewTimestamp,
    String reviewStatus) implements StepEntity {
}