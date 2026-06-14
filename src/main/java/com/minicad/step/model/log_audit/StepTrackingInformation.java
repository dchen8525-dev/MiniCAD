package com.minicad.step.model.log_audit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTrackingInformation implements StepEntity {
    private final int id;
    private final String name;
    private final String trackingId;
    private final List<StepEntity> trackingItems;
    private final String varianceLocation;
    private final String varianceStatus;
    private final List<StepEntity> varianceHistory;
    private final StepEntity trackingService;

    public StepTrackingInformation(int id, String name, String trackingId, List<StepEntity> trackingItems, String varianceLocation, String varianceStatus, List<StepEntity> varianceHistory, StepEntity trackingService) {
        this.id = id;
        this.name = name;
        this.trackingId = trackingId;
        this.trackingItems = trackingItems == null ? null : java.util.List.copyOf(trackingItems);
        this.varianceLocation = varianceLocation;
        this.varianceStatus = varianceStatus;
        this.varianceHistory = varianceHistory == null ? null : java.util.List.copyOf(varianceHistory);
        this.trackingService = trackingService;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTrackingInformation that = (StepTrackingInformation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(trackingId, that.trackingId) && Objects.equals(trackingItems, that.trackingItems) && Objects.equals(varianceLocation, that.varianceLocation) && Objects.equals(varianceStatus, that.varianceStatus) && Objects.equals(varianceHistory, that.varianceHistory) && Objects.equals(trackingService, that.trackingService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, trackingId, trackingItems, varianceLocation, varianceStatus, varianceHistory, trackingService);
    }

    @Override
    public String toString() {
        return "StepTrackingInformation{" + "id=" + id + "name=" + name + "trackingId=" + trackingId + "trackingItems=" + trackingItems + "varianceLocation=" + varianceLocation + "varianceStatus=" + varianceStatus + "varianceHistory=" + varianceHistory + "trackingService=" + trackingService + "}";
    }
}