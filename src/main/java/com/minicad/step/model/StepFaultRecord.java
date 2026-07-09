package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepFaultRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceSystem;
    private final String varianceFault;
    private final String varianceCode;
    private final StepEntity varianceDate;
    private final String varianceDiagnosis;
    private final String varianceRemedy;
    private final String varianceStatus;

    public StepFaultRecord(int id, String name, StepEntity varianceSystem, String varianceFault, String varianceCode, StepEntity varianceDate, String varianceDiagnosis, String varianceRemedy, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceSystem = varianceSystem;
        this.varianceFault = varianceFault;
        this.varianceCode = varianceCode;
        this.varianceDate = varianceDate;
        this.varianceDiagnosis = varianceDiagnosis;
        this.varianceRemedy = varianceRemedy;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFaultRecord that = (StepFaultRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceSystem, that.varianceSystem) && Objects.equals(varianceFault, that.varianceFault) && Objects.equals(varianceCode, that.varianceCode) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceDiagnosis, that.varianceDiagnosis) && Objects.equals(varianceRemedy, that.varianceRemedy) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceSystem, varianceFault, varianceCode, varianceDate, varianceDiagnosis, varianceRemedy, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepFaultRecord{" + "id=" + id + "name=" + name + "varianceSystem=" + varianceSystem + "varianceFault=" + varianceFault + "varianceCode=" + varianceCode + "varianceDate=" + varianceDate + "varianceDiagnosis=" + varianceDiagnosis + "varianceRemedy=" + varianceRemedy + "varianceStatus=" + varianceStatus + "}";
    }
}