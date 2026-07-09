package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved INSPECTION_RESULT.
 * An inspection result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @param inspectionItem inspected item reference
 * @param measuredValues measured values
 * @param nominalValues nominal values
 * @param deviationValues deviation from nominal
 * @param passFailStatus pass/fail status for each check
 * @param inspector inspector person/organization
 * @param inspectionDate date of inspection
 */
/**
 * Resolved INSPECTION_RESULT.
 * An inspection result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @param inspectionItem inspected item reference
 * @param measuredValues measured values
 * @param nominalValues nominal values
 * @param deviationValues deviation from nominal
 * @param passFailStatus pass/fail status for each check
 * @param inspector inspector person/organization
 * @param inspectionDate date of inspection
 */
public final class StepInspectionResult implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity inspectionItem;
    private final List<Double> measuredValues;
    private final List<Double> nominalValues;
    private final List<Double> deviationValues;
    private final List<String> passFailStatus;
    private final StepEntity inspector;
    private final StepEntity inspectionDate;

    public StepInspectionResult(int id, String name, StepEntity inspectionItem, List<Double> measuredValues, List<Double> nominalValues, List<Double> deviationValues, List<String> passFailStatus, StepEntity inspector, StepEntity inspectionDate) {
        this.id = id;
        this.name = name;
        this.inspectionItem = inspectionItem;
        this.measuredValues = measuredValues == null ? null : java.util.List.copyOf(measuredValues);
        this.nominalValues = nominalValues == null ? null : java.util.List.copyOf(nominalValues);
        this.deviationValues = deviationValues == null ? null : java.util.List.copyOf(deviationValues);
        this.passFailStatus = passFailStatus == null ? null : java.util.List.copyOf(passFailStatus);
        this.inspector = inspector;
        this.inspectionDate = inspectionDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getInspectionItem() {
        return inspectionItem;
    }

    public List<Double> getMeasuredValues() {
        return measuredValues;
    }

    public List<Double> getNominalValues() {
        return nominalValues;
    }

    public List<Double> getDeviationValues() {
        return deviationValues;
    }

    public List<String> getPassFailStatus() {
        return passFailStatus;
    }

    public StepEntity getInspector() {
        return inspector;
    }

    public StepEntity getInspectionDate() {
        return inspectionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepInspectionResult that = (StepInspectionResult) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(inspectionItem, that.inspectionItem) && Objects.equals(measuredValues, that.measuredValues) && Objects.equals(nominalValues, that.nominalValues) && Objects.equals(deviationValues, that.deviationValues) && Objects.equals(passFailStatus, that.passFailStatus) && Objects.equals(inspector, that.inspector) && Objects.equals(inspectionDate, that.inspectionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, inspectionItem, measuredValues, nominalValues, deviationValues, passFailStatus, inspector, inspectionDate);
    }

    @Override
    public String toString() {
        return "StepInspectionResult{" + "id=" + id + "name=" + name + "inspectionItem=" + inspectionItem + "measuredValues=" + measuredValues + "nominalValues=" + nominalValues + "deviationValues=" + deviationValues + "passFailStatus=" + passFailStatus + "inspector=" + inspector + "inspectionDate=" + inspectionDate + "}";
    }
}