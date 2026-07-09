package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PROCEDURE_INSTANCE.
 * A procedure instance entity.
 *
 * @param id STEP instance id
 * @param name procedure instance name
 * @param procedureDefinition procedure variance definition reference
 * @param procedureState procedure variance state
 * @param procedureCurrentStep procedure variance current step
 * @param procedureCompletedSteps procedure variance completed steps
 * @param procedureStatus procedure variance status
 */
/**
 * Resolved PROCEDURE_INSTANCE.
 * A procedure instance entity.
 *
 * @param id STEP instance id
 * @param name procedure instance name
 * @param procedureDefinition procedure variance definition reference
 * @param procedureState procedure variance state
 * @param procedureCurrentStep procedure variance current step
 * @param procedureCompletedSteps procedure variance completed steps
 * @param procedureStatus procedure variance status
 */
public final class StepProcedureInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity procedureDefinition;
    private final String procedureState;
    private final int procedureCurrentStep;
    private final List<Integer> procedureCompletedSteps;
    private final String procedureStatus;

    public StepProcedureInstance(int id, String name, StepEntity procedureDefinition, String procedureState, int procedureCurrentStep, List<Integer> procedureCompletedSteps, String procedureStatus) {
        this.id = id;
        this.name = name;
        this.procedureDefinition = procedureDefinition;
        this.procedureState = procedureState;
        this.procedureCurrentStep = procedureCurrentStep;
        this.procedureCompletedSteps = procedureCompletedSteps == null ? null : java.util.List.copyOf(procedureCompletedSteps);
        this.procedureStatus = procedureStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getProcedureDefinition() {
        return procedureDefinition;
    }

    public String getProcedureState() {
        return procedureState;
    }

    public int getProcedureCurrentStep() {
        return procedureCurrentStep;
    }

    public List<Integer> getProcedureCompletedSteps() {
        return procedureCompletedSteps;
    }

    public String getProcedureStatus() {
        return procedureStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProcedureInstance that = (StepProcedureInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(procedureDefinition, that.procedureDefinition) && Objects.equals(procedureState, that.procedureState) && procedureCurrentStep == that.procedureCurrentStep && Objects.equals(procedureCompletedSteps, that.procedureCompletedSteps) && Objects.equals(procedureStatus, that.procedureStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, procedureDefinition, procedureState, procedureCurrentStep, procedureCompletedSteps, procedureStatus);
    }

    @Override
    public String toString() {
        return "StepProcedureInstance{" + "id=" + id + "name=" + name + "procedureDefinition=" + procedureDefinition + "procedureState=" + procedureState + "procedureCurrentStep=" + procedureCurrentStep + "procedureCompletedSteps=" + procedureCompletedSteps + "procedureStatus=" + procedureStatus + "}";
    }
}