package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepCommentRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String commentType;
    private final String commentText;
    private final StepEntity commentTarget;
    private final StepEntity commentAuthor;
    private final StepEntity commentTimestamp;
    private final String commentStatus;

    public StepCommentRecord(int id, String name, String commentType, String commentText, StepEntity commentTarget, StepEntity commentAuthor, StepEntity commentTimestamp, String commentStatus) {
        this.id = id;
        this.name = name;
        this.commentType = commentType;
        this.commentText = commentText;
        this.commentTarget = commentTarget;
        this.commentAuthor = commentAuthor;
        this.commentTimestamp = commentTimestamp;
        this.commentStatus = commentStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCommentType() {
        return commentType;
    }

    public String getCommentText() {
        return commentText;
    }

    public StepEntity getCommentTarget() {
        return commentTarget;
    }

    public StepEntity getCommentAuthor() {
        return commentAuthor;
    }

    public StepEntity getCommentTimestamp() {
        return commentTimestamp;
    }

    public String getCommentStatus() {
        return commentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCommentRecord that = (StepCommentRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(commentType, that.commentType) && Objects.equals(commentText, that.commentText) && Objects.equals(commentTarget, that.commentTarget) && Objects.equals(commentAuthor, that.commentAuthor) && Objects.equals(commentTimestamp, that.commentTimestamp) && Objects.equals(commentStatus, that.commentStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, commentType, commentText, commentTarget, commentAuthor, commentTimestamp, commentStatus);
    }

    @Override
    public String toString() {
        return "StepCommentRecord{" + "id=" + id + "name=" + name + "commentType=" + commentType + "commentText=" + commentText + "commentTarget=" + commentTarget + "commentAuthor=" + commentAuthor + "commentTimestamp=" + commentTimestamp + "commentStatus=" + commentStatus + "}";
    }
}