package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepLifecycleStage implements StepEntity {
    private final int id;
    private final String name;
    private final String stageType;
    private final String stageDescription;
    private final StepEntity stageStartDate;
    private final StepEntity stageEndDate;
    private final StepEntity stageOwner;
    private final List<StepEntity> stageRequirements;

    public StepLifecycleStage(int id, String name, String stageType, String stageDescription, StepEntity stageStartDate, StepEntity stageEndDate, StepEntity stageOwner, List<StepEntity> stageRequirements) {
        this.id = id;
        this.name = name;
        this.stageType = stageType;
        this.stageDescription = stageDescription;
        this.stageStartDate = stageStartDate;
        this.stageEndDate = stageEndDate;
        this.stageOwner = stageOwner;
        this.stageRequirements = stageRequirements == null ? null : java.util.List.copyOf(stageRequirements);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStageType() {
        return stageType;
    }

    public String getStageDescription() {
        return stageDescription;
    }

    public StepEntity getStageStartDate() {
        return stageStartDate;
    }

    public StepEntity getStageEndDate() {
        return stageEndDate;
    }

    public StepEntity getStageOwner() {
        return stageOwner;
    }

    public List<StepEntity> getStageRequirements() {
        return stageRequirements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLifecycleStage that = (StepLifecycleStage) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(stageType, that.stageType) && Objects.equals(stageDescription, that.stageDescription) && Objects.equals(stageStartDate, that.stageStartDate) && Objects.equals(stageEndDate, that.stageEndDate) && Objects.equals(stageOwner, that.stageOwner) && Objects.equals(stageRequirements, that.stageRequirements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, stageType, stageDescription, stageStartDate, stageEndDate, stageOwner, stageRequirements);
    }

    @Override
    public String toString() {
        return "StepLifecycleStage{" + "id=" + id + "name=" + name + "stageType=" + stageType + "stageDescription=" + stageDescription + "stageStartDate=" + stageStartDate + "stageEndDate=" + stageEndDate + "stageOwner=" + stageOwner + "stageRequirements=" + stageRequirements + "}";
    }
}