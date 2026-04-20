package com.minicad.step.model.log_audit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepTrackingInformation(
    int id,
    String name,
    String trackingId,
    List<StepEntity> trackingItems,
    String varianceLocation,
    String varianceStatus,
    List<StepEntity> varianceHistory,
    StepEntity trackingService) implements StepEntity {
}