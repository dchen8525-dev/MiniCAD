package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepReviewRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String reviewType;
    private final String reviewResult;
    private final List<String> reviewComments;
    private final StepEntity reviewReviewer;
    private final StepEntity reviewTimestamp;
    private final String reviewStatus;

    public StepReviewRecord(int id, String name, String reviewType, String reviewResult, List<String> reviewComments, StepEntity reviewReviewer, StepEntity reviewTimestamp, String reviewStatus) {
        this.id = id;
        this.name = name;
        this.reviewType = reviewType;
        this.reviewResult = reviewResult;
        this.reviewComments = reviewComments == null ? null : java.util.List.copyOf(reviewComments);
        this.reviewReviewer = reviewReviewer;
        this.reviewTimestamp = reviewTimestamp;
        this.reviewStatus = reviewStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepReviewRecord that = (StepReviewRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(reviewType, that.reviewType) && Objects.equals(reviewResult, that.reviewResult) && Objects.equals(reviewComments, that.reviewComments) && Objects.equals(reviewReviewer, that.reviewReviewer) && Objects.equals(reviewTimestamp, that.reviewTimestamp) && Objects.equals(reviewStatus, that.reviewStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, reviewType, reviewResult, reviewComments, reviewReviewer, reviewTimestamp, reviewStatus);
    }

    @Override
    public String toString() {
        return "StepReviewRecord{" + "id=" + id + "name=" + name + "reviewType=" + reviewType + "reviewResult=" + reviewResult + "reviewComments=" + reviewComments + "reviewReviewer=" + reviewReviewer + "reviewTimestamp=" + reviewTimestamp + "reviewStatus=" + reviewStatus + "}";
    }
}