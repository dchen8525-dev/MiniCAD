package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CONFIGURATION_RECORD.
 * A configuration record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItem configured variance item
 * @varianceSettings configuration variance settings
 * @varianceDate configuration variance date
 * @varianceReason configuration variance reason
 * @variancePrevious previous variance configuration
 * @varianceStatus record variance status
 */
public record StepConfigurationRecord(
    int id,
    String name,
    StepEntity varianceItem,
    List<String> varianceSettings,
    StepEntity varianceDate,
    String varianceReason,
    StepEntity variancePrevious,
    String varianceStatus) implements StepEntity {
}