package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CALIBRATION_RECORD.
 * A calibration record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment calibrated variance equipment
 * @varianceStandard calibration variance standard
 * @varianceDate calibration variance date
 * @varianceResults calibration variance results
 * @varianceNext next variance calibration date
 * @varianceStatus record variance status
 */
/**
 * Resolved CALIBRATION_RECORD.
 * A calibration record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment calibrated variance equipment
 * @varianceStandard calibration variance standard
 * @varianceDate calibration variance date
 * @varianceResults calibration variance results
 * @varianceNext next variance calibration date
 * @varianceStatus record variance status
 */
public final class StepCalibrationRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceEquipment;
    private final StepEntity varianceStandard;
    private final StepEntity varianceDate;
    private final List<Double> varianceResults;
    private final StepEntity varianceNext;
    private final String varianceStatus;

    public StepCalibrationRecord(int id, String name, StepEntity varianceEquipment, StepEntity varianceStandard, StepEntity varianceDate, List<Double> varianceResults, StepEntity varianceNext, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceEquipment = varianceEquipment;
        this.varianceStandard = varianceStandard;
        this.varianceDate = varianceDate;
        this.varianceResults = varianceResults == null ? null : java.util.List.copyOf(varianceResults);
        this.varianceNext = varianceNext;
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

    public StepEntity getVarianceStandard() {
        return varianceStandard;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public List<Double> getVarianceResults() {
        return varianceResults;
    }

    public StepEntity getVarianceNext() {
        return varianceNext;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCalibrationRecord that = (StepCalibrationRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceEquipment, that.varianceEquipment) && Objects.equals(varianceStandard, that.varianceStandard) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceResults, that.varianceResults) && Objects.equals(varianceNext, that.varianceNext) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceEquipment, varianceStandard, varianceDate, varianceResults, varianceNext, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepCalibrationRecord{" + "id=" + id + "name=" + name + "varianceEquipment=" + varianceEquipment + "varianceStandard=" + varianceStandard + "varianceDate=" + varianceDate + "varianceResults=" + varianceResults + "varianceNext=" + varianceNext + "varianceStatus=" + varianceStatus + "}";
    }
}