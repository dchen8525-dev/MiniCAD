package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepChangeRequest implements StepEntity {
    private final int id;
    private final String name;
    private final String requestType;
    private final String requestDescription;
    private final List<StepEntity> affectedItems;
    private final String requestStatus;
    private final StepEntity requestDate;
    private final StepEntity requestAuthor;
    private final String requestReason;

    public StepChangeRequest(int id, String name, String requestType, String requestDescription, List<StepEntity> affectedItems, String requestStatus, StepEntity requestDate, StepEntity requestAuthor, String requestReason) {
        this.id = id;
        this.name = name;
        this.requestType = requestType;
        this.requestDescription = requestDescription;
        this.affectedItems = affectedItems == null ? null : java.util.List.copyOf(affectedItems);
        this.requestStatus = requestStatus;
        this.requestDate = requestDate;
        this.requestAuthor = requestAuthor;
        this.requestReason = requestReason;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getRequestDescription() {
        return requestDescription;
    }

    public List<StepEntity> getAffectedItems() {
        return affectedItems;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public StepEntity getRequestDate() {
        return requestDate;
    }

    public StepEntity getRequestAuthor() {
        return requestAuthor;
    }

    public String getRequestReason() {
        return requestReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepChangeRequest that = (StepChangeRequest) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(requestType, that.requestType) && Objects.equals(requestDescription, that.requestDescription) && Objects.equals(affectedItems, that.affectedItems) && Objects.equals(requestStatus, that.requestStatus) && Objects.equals(requestDate, that.requestDate) && Objects.equals(requestAuthor, that.requestAuthor) && Objects.equals(requestReason, that.requestReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, requestType, requestDescription, affectedItems, requestStatus, requestDate, requestAuthor, requestReason);
    }

    @Override
    public String toString() {
        return "StepChangeRequest{" + "id=" + id + "name=" + name + "requestType=" + requestType + "requestDescription=" + requestDescription + "affectedItems=" + affectedItems + "requestStatus=" + requestStatus + "requestDate=" + requestDate + "requestAuthor=" + requestAuthor + "requestReason=" + requestReason + "}";
    }
}