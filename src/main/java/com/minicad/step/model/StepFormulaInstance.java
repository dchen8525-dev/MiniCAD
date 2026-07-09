package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved FORMULA_INSTANCE.
 * A formula instance entity.
 *
 * @param id STEP instance id
 * @param name formula instance name
 * @param formulaDefinition formula variance definition reference
 * @param formulaResult formula variance result value
 * @param formulaStatus formula variance status
 */
/**
 * Resolved FORMULA_INSTANCE.
 * A formula instance entity.
 *
 * @param id STEP instance id
 * @param name formula instance name
 * @param formulaDefinition formula variance definition reference
 * @param formulaResult formula variance result value
 * @param formulaStatus formula variance status
 */
public final class StepFormulaInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity formulaDefinition;
    private final double formulaResult;
    private final String formulaStatus;

    public StepFormulaInstance(int id, String name, StepEntity formulaDefinition, double formulaResult, String formulaStatus) {
        this.id = id;
        this.name = name;
        this.formulaDefinition = formulaDefinition;
        this.formulaResult = formulaResult;
        this.formulaStatus = formulaStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getFormulaDefinition() {
        return formulaDefinition;
    }

    public double getFormulaResult() {
        return formulaResult;
    }

    public String getFormulaStatus() {
        return formulaStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFormulaInstance that = (StepFormulaInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(formulaDefinition, that.formulaDefinition) && formulaResult == that.formulaResult && Objects.equals(formulaStatus, that.formulaStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, formulaDefinition, formulaResult, formulaStatus);
    }

    @Override
    public String toString() {
        return "StepFormulaInstance{" + "id=" + id + "name=" + name + "formulaDefinition=" + formulaDefinition + "formulaResult=" + formulaResult + "formulaStatus=" + formulaStatus + "}";
    }
}