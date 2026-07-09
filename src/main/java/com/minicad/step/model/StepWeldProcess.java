package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved WELD_PROCESS.
 * A weld process entity.
 *
 * @param id STEP instance id
 * @param name process name
 * @param processType process variance type
 * @param processParameters process variance parameters
 * @param processEquipment process variance equipment reference
 * @param processStatus process variance status
 */
/**
 * Resolved WELD_PROCESS.
 * A weld process entity.
 *
 * @param id STEP instance id
 * @param name process name
 * @param processType process variance type
 * @param processParameters process variance parameters
 * @param processEquipment process variance equipment reference
 * @param processStatus process variance status
 */
public final class StepWeldProcess implements StepEntity {
    private final int id;
    private final String name;
    private final String processType;
    private final List<String> processParameters;
    private final StepEntity processEquipment;
    private final String processStatus;

    public StepWeldProcess(int id, String name, String processType, List<String> processParameters, StepEntity processEquipment, String processStatus) {
        this.id = id;
        this.name = name;
        this.processType = processType;
        this.processParameters = processParameters == null ? null : java.util.List.copyOf(processParameters);
        this.processEquipment = processEquipment;
        this.processStatus = processStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProcessType() {
        return processType;
    }

    public List<String> getProcessParameters() {
        return processParameters;
    }

    public StepEntity getProcessEquipment() {
        return processEquipment;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepWeldProcess that = (StepWeldProcess) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(processType, that.processType) && Objects.equals(processParameters, that.processParameters) && Objects.equals(processEquipment, that.processEquipment) && Objects.equals(processStatus, that.processStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, processType, processParameters, processEquipment, processStatus);
    }

    @Override
    public String toString() {
        return "StepWeldProcess{" + "id=" + id + "name=" + name + "processType=" + processType + "processParameters=" + processParameters + "processEquipment=" + processEquipment + "processStatus=" + processStatus + "}";
    }
}