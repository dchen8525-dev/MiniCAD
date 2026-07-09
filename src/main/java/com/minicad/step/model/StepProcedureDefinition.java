package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PROCEDURE_DEFINITION.
 * A procedure definition entity.
 *
 * @param id STEP instance id
 * @param name procedure name
 * @param procedureType procedure variance type
 * @param procedureSteps procedure variance steps
 * @param procedureInputs procedure variance required inputs
 * @param procedureOutputs procedure variance expected outputs
 * @param procedureStatus procedure variance status
 */
/**
 * Resolved PROCEDURE_DEFINITION.
 * A procedure definition entity.
 *
 * @param id STEP instance id
 * @param name procedure name
 * @param procedureType procedure variance type
 * @param procedureSteps procedure variance steps
 * @param procedureInputs procedure variance required inputs
 * @param procedureOutputs procedure variance expected outputs
 * @param procedureStatus procedure variance status
 */
public final class StepProcedureDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String procedureType;
    private final List<String> procedureSteps;
    private final List<String> procedureInputs;
    private final List<String> procedureOutputs;
    private final String procedureStatus;

    public StepProcedureDefinition(int id, String name, String procedureType, List<String> procedureSteps, List<String> procedureInputs, List<String> procedureOutputs, String procedureStatus) {
        this.id = id;
        this.name = name;
        this.procedureType = procedureType;
        this.procedureSteps = procedureSteps == null ? null : java.util.List.copyOf(procedureSteps);
        this.procedureInputs = procedureInputs == null ? null : java.util.List.copyOf(procedureInputs);
        this.procedureOutputs = procedureOutputs == null ? null : java.util.List.copyOf(procedureOutputs);
        this.procedureStatus = procedureStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProcedureType() {
        return procedureType;
    }

    public List<String> getProcedureSteps() {
        return procedureSteps;
    }

    public List<String> getProcedureInputs() {
        return procedureInputs;
    }

    public List<String> getProcedureOutputs() {
        return procedureOutputs;
    }

    public String getProcedureStatus() {
        return procedureStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProcedureDefinition that = (StepProcedureDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(procedureType, that.procedureType) && Objects.equals(procedureSteps, that.procedureSteps) && Objects.equals(procedureInputs, that.procedureInputs) && Objects.equals(procedureOutputs, that.procedureOutputs) && Objects.equals(procedureStatus, that.procedureStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, procedureType, procedureSteps, procedureInputs, procedureOutputs, procedureStatus);
    }

    @Override
    public String toString() {
        return "StepProcedureDefinition{" + "id=" + id + "name=" + name + "procedureType=" + procedureType + "procedureSteps=" + procedureSteps + "procedureInputs=" + procedureInputs + "procedureOutputs=" + procedureOutputs + "procedureStatus=" + procedureStatus + "}";
    }
}