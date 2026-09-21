package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepInspectionResult extends AbstractStepEntity {
    private final StepEntity inspectionItem;
    private final List<Double> measuredValues;
    private final List<Double> nominalValues;
    private final List<Double> deviationValues;
    private final List<String> passFailStatus;
    private final StepEntity inspector;
    private final StepEntity inspectionDate;

    public StepInspectionResult(int id, String name, StepEntity inspectionItem, List<Double> measuredValues, List<Double> nominalValues, List<Double> deviationValues, List<String> passFailStatus, StepEntity inspector, StepEntity inspectionDate) {
        super(id, name);
        this.inspectionItem = inspectionItem;
        this.measuredValues = measuredValues == null ? null : java.util.List.copyOf(measuredValues);
        this.nominalValues = nominalValues == null ? null : java.util.List.copyOf(nominalValues);
        this.deviationValues = deviationValues == null ? null : java.util.List.copyOf(deviationValues);
        this.passFailStatus = passFailStatus == null ? null : java.util.List.copyOf(passFailStatus);
        this.inspector = inspector;
        this.inspectionDate = inspectionDate;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("inspectionItem", inspectionItem);
        state.put("measuredValues", measuredValues);
        state.put("nominalValues", nominalValues);
        state.put("deviationValues", deviationValues);
        state.put("passFailStatus", passFailStatus);
        state.put("inspector", inspector);
        state.put("inspectionDate", inspectionDate);
        return state;
    }
}
