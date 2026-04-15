package com.minicad.step.model;

import java.util.List;

/**
 * Resolved COMMENT_RECORD.
 * A comment record entity.
 *
 * @param id STEP instance id
 * @param name comment name
 * @param commentType comment variance type
 * @param commentText comment variance text
 * @param commentTarget comment variance target reference
 * @param commentAuthor comment variance author reference
 * @param commentTimestamp comment variance timestamp
 * @param commentStatus comment variance status
 */
public record StepCommentRecord(
    int id,
    String name,
    String commentType,
    String commentText,
    StepEntity commentTarget,
    StepEntity commentAuthor,
    StepEntity commentTimestamp,
    String commentStatus) implements StepEntity {
}