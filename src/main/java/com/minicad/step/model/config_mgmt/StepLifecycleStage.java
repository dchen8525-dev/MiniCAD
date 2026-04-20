package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved LIFECYCLE_STAGE.
 * A lifecycle stage entity.
 *
 * @param id STEP instance id
 * @param name stage name
 * @param stageType lifecycle stage type (design, manufacturing, service, disposal)
 * @param stageDescription stage description
 * @param stageStartDate stage start date
 * @param stageEndDate stage end date
 * @param stageOwner stage owner/responsible party
 * @param stageRequirements stage-specific requirements
 */
public record StepLifecycleStage(
    int id,
    String name,
    String stageType,
    String stageDescription,
    StepEntity stageStartDate,
    StepEntity stageEndDate,
    StepEntity stageOwner,
    List<StepEntity> stageRequirements) implements StepEntity {
}