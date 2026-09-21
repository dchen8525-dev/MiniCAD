package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepCommentRecord extends AbstractStepEntity {
    private final String commentType;
    private final String commentText;
    private final StepEntity commentTarget;
    private final StepEntity commentAuthor;
    private final StepEntity commentTimestamp;
    private final String commentStatus;

    public StepCommentRecord(int id, String name, String commentType, String commentText, StepEntity commentTarget, StepEntity commentAuthor, StepEntity commentTimestamp, String commentStatus) {
        super(id, name);
        this.commentType = commentType;
        this.commentText = commentText;
        this.commentTarget = commentTarget;
        this.commentAuthor = commentAuthor;
        this.commentTimestamp = commentTimestamp;
        this.commentStatus = commentStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("commentType", commentType);
        state.put("commentText", commentText);
        state.put("commentTarget", commentTarget);
        state.put("commentAuthor", commentAuthor);
        state.put("commentTimestamp", commentTimestamp);
        state.put("commentStatus", commentStatus);
        return state;
    }
}
