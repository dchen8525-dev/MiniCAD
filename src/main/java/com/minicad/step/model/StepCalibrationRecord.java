package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepCalibrationRecord extends AbstractStepEntity {
    private final StepEntity varianceEquipment;
    private final StepEntity varianceStandard;
    private final StepEntity varianceDate;
    private final List<Double> varianceResults;
    private final StepEntity varianceNext;
    private final String varianceStatus;

    public StepCalibrationRecord(int id, String name, StepEntity varianceEquipment, StepEntity varianceStandard, StepEntity varianceDate, List<Double> varianceResults, StepEntity varianceNext, String varianceStatus) {
        super(id, name);
        this.varianceEquipment = varianceEquipment;
        this.varianceStandard = varianceStandard;
        this.varianceDate = varianceDate;
        this.varianceResults = varianceResults == null ? null : java.util.List.copyOf(varianceResults);
        this.varianceNext = varianceNext;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceEquipment", varianceEquipment);
        state.put("varianceStandard", varianceStandard);
        state.put("varianceDate", varianceDate);
        state.put("varianceResults", varianceResults);
        state.put("varianceNext", varianceNext);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
