package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved REPAIR_RECORD.
 * A repair record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment repaired variance equipment
 * @varianceProblem repair variance problem description
 * @varianceCause repair variance root cause
 * @varianceDate repair variance date
 * @varianceActions repair variance actions
 * @varianceStatus record variance status
 */
/**
 * Resolved REPAIR_RECORD.
 * A repair record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment repaired variance equipment
 * @varianceProblem repair variance problem description
 * @varianceCause repair variance root cause
 * @varianceDate repair variance date
 * @varianceActions repair variance actions
 * @varianceStatus record variance status
 */
public final class StepRepairRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceEquipment;
    private final String varianceProblem;
    private final String varianceCause;
    private final StepEntity varianceDate;
    private final List<String> varianceActions;
    private final String varianceStatus;

    public StepRepairRecord(int id, String name, StepEntity varianceEquipment, String varianceProblem, String varianceCause, StepEntity varianceDate, List<String> varianceActions, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceEquipment = varianceEquipment;
        this.varianceProblem = varianceProblem;
        this.varianceCause = varianceCause;
        this.varianceDate = varianceDate;
        this.varianceActions = varianceActions == null ? null : java.util.List.copyOf(varianceActions);
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceEquipment() {
        return varianceEquipment;
    }

    public String getVarianceProblem() {
        return varianceProblem;
    }

    public String getVarianceCause() {
        return varianceCause;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public List<String> getVarianceActions() {
        return varianceActions;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRepairRecord that = (StepRepairRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceEquipment, that.varianceEquipment) && Objects.equals(varianceProblem, that.varianceProblem) && Objects.equals(varianceCause, that.varianceCause) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceActions, that.varianceActions) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceEquipment, varianceProblem, varianceCause, varianceDate, varianceActions, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepRepairRecord{" + "id=" + id + "name=" + name + "varianceEquipment=" + varianceEquipment + "varianceProblem=" + varianceProblem + "varianceCause=" + varianceCause + "varianceDate=" + varianceDate + "varianceActions=" + varianceActions + "varianceStatus=" + varianceStatus + "}";
    }
}