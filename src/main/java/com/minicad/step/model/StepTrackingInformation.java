package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TRACKING_INFORMATION.
 * A tracking information entity.
 *
 * @param id STEP instance id
 * @param name tracking name
 * @param trackingId tracking identifier/number
 * @param trackingItems items being tracked
 * @varianceLocation current variance location
 * @varianceStatus tracking variance status
 * @varianceHistory tracking variance history events
 * @param trackingService tracking service reference
 */
public final class StepTrackingInformation extends AbstractStepEntity {
    private final String trackingId;
    private final List<StepEntity> trackingItems;
    private final String varianceLocation;
    private final String varianceStatus;
    private final List<StepEntity> varianceHistory;
    private final StepEntity trackingService;

    public StepTrackingInformation(int id, String name, String trackingId, List<StepEntity> trackingItems, String varianceLocation, String varianceStatus, List<StepEntity> varianceHistory, StepEntity trackingService) {
        super(id, name);
        this.trackingId = trackingId;
        this.trackingItems = trackingItems == null ? null : java.util.List.copyOf(trackingItems);
        this.varianceLocation = varianceLocation;
        this.varianceStatus = varianceStatus;
        this.varianceHistory = varianceHistory == null ? null : java.util.List.copyOf(varianceHistory);
        this.trackingService = trackingService;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public List<StepEntity> getTrackingItems() {
        return trackingItems;
    }

    public String getVarianceLocation() {
        return varianceLocation;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    public List<StepEntity> getVarianceHistory() {
        return varianceHistory;
    }

    public StepEntity getTrackingService() {
        return trackingService;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("trackingId", trackingId);
        state.put("trackingItems", trackingItems);
        state.put("varianceLocation", varianceLocation);
        state.put("varianceStatus", varianceStatus);
        state.put("varianceHistory", varianceHistory);
        state.put("trackingService", trackingService);
        return state;
    }
}
