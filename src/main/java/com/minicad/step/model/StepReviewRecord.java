package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepReviewRecord extends AbstractStepEntity {
    private final String reviewType;
    private final String reviewResult;
    private final List<String> reviewComments;
    private final StepEntity reviewReviewer;
    private final StepEntity reviewTimestamp;
    private final String reviewStatus;

    public StepReviewRecord(int id, String name, String reviewType, String reviewResult, List<String> reviewComments, StepEntity reviewReviewer, StepEntity reviewTimestamp, String reviewStatus) {
        super(id, name);
        this.reviewType = reviewType;
        this.reviewResult = reviewResult;
        this.reviewComments = reviewComments == null ? null : java.util.List.copyOf(reviewComments);
        this.reviewReviewer = reviewReviewer;
        this.reviewTimestamp = reviewTimestamp;
        this.reviewStatus = reviewStatus;
    }

    public String getReviewType() {
        return reviewType;
    }

    public String getReviewResult() {
        return reviewResult;
    }

    public List<String> getReviewComments() {
        return reviewComments;
    }

    public StepEntity getReviewReviewer() {
        return reviewReviewer;
    }

    public StepEntity getReviewTimestamp() {
        return reviewTimestamp;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("reviewType", reviewType);
        state.put("reviewResult", reviewResult);
        state.put("reviewComments", reviewComments);
        state.put("reviewReviewer", reviewReviewer);
        state.put("reviewTimestamp", reviewTimestamp);
        state.put("reviewStatus", reviewStatus);
        return state;
    }
}
