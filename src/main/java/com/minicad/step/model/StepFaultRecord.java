package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FAULT_RECORD.
 * A fault record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceSystem faulty variance system
 * @varianceFault fault variance description
 * @varianceCode fault variance code
 * @varianceDate fault variance date
 * @varianceDiagnosis diagnosis variance result
 * @varianceRemedy remedy variance action
 * @varianceStatus record variance status
 */
public final class StepFaultRecord extends AbstractStepEntity {
    private final StepEntity varianceSystem;
    private final String varianceFault;
    private final String varianceCode;
    private final StepEntity varianceDate;
    private final String varianceDiagnosis;
    private final String varianceRemedy;
    private final String varianceStatus;

    public StepFaultRecord(int id, String name, StepEntity varianceSystem, String varianceFault, String varianceCode, StepEntity varianceDate, String varianceDiagnosis, String varianceRemedy, String varianceStatus) {
        super(id, name);
        this.varianceSystem = varianceSystem;
        this.varianceFault = varianceFault;
        this.varianceCode = varianceCode;
        this.varianceDate = varianceDate;
        this.varianceDiagnosis = varianceDiagnosis;
        this.varianceRemedy = varianceRemedy;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceSystem() {
        return varianceSystem;
    }

    public String getVarianceFault() {
        return varianceFault;
    }

    public String getVarianceCode() {
        return varianceCode;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVarianceDiagnosis() {
        return varianceDiagnosis;
    }

    public String getVarianceRemedy() {
        return varianceRemedy;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceSystem", varianceSystem);
        state.put("varianceFault", varianceFault);
        state.put("varianceCode", varianceCode);
        state.put("varianceDate", varianceDate);
        state.put("varianceDiagnosis", varianceDiagnosis);
        state.put("varianceRemedy", varianceRemedy);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
