package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepChangeRequest extends AbstractStepEntity {
    private final String requestType;
    private final String requestDescription;
    private final List<StepEntity> affectedItems;
    private final String requestStatus;
    private final StepEntity requestDate;
    private final StepEntity requestAuthor;
    private final String requestReason;

    public StepChangeRequest(int id, String name, String requestType, String requestDescription, List<StepEntity> affectedItems, String requestStatus, StepEntity requestDate, StepEntity requestAuthor, String requestReason) {
        super(id, name);
        this.requestType = requestType;
        this.requestDescription = requestDescription;
        this.affectedItems = affectedItems == null ? null : java.util.List.copyOf(affectedItems);
        this.requestStatus = requestStatus;
        this.requestDate = requestDate;
        this.requestAuthor = requestAuthor;
        this.requestReason = requestReason;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("requestType", requestType);
        state.put("requestDescription", requestDescription);
        state.put("affectedItems", affectedItems);
        state.put("requestStatus", requestStatus);
        state.put("requestDate", requestDate);
        state.put("requestAuthor", requestAuthor);
        state.put("requestReason", requestReason);
        return state;
    }
}
