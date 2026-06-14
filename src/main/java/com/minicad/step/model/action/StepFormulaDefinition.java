package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FORMULA_DEFINITION.
 * A formula definition entity.
 *
 * @param id STEP instance id
 * @param name formula name
 * @param formulaType formula variance type
 * @param formulaExpression formula variance expression
 * @param formulaVariables formula variance variables used
 * @param formulaUnit formula variance result unit
 * @param formulaStatus formula variance status
 */
/**
 * Resolved FORMULA_DEFINITION.
 * A formula definition entity.
 *
 * @param id STEP instance id
 * @param name formula name
 * @param formulaType formula variance type
 * @param formulaExpression formula variance expression
 * @param formulaVariables formula variance variables used
 * @param formulaUnit formula variance result unit
 * @param formulaStatus formula variance status
 */
public final class StepFormulaDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String formulaType;
    private final String formulaExpression;
    private final List<String> formulaVariables;
    private final StepEntity formulaUnit;
    private final String formulaStatus;

    public StepFormulaDefinition(int id, String name, String formulaType, String formulaExpression, List<String> formulaVariables, StepEntity formulaUnit, String formulaStatus) {
        this.id = id;
        this.name = name;
        this.formulaType = formulaType;
        this.formulaExpression = formulaExpression;
        this.formulaVariables = formulaVariables == null ? null : java.util.List.copyOf(formulaVariables);
        this.formulaUnit = formulaUnit;
        this.formulaStatus = formulaStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFormulaType() {
        return formulaType;
    }

    public String getFormulaExpression() {
        return formulaExpression;
    }

    public List<String> getFormulaVariables() {
        return formulaVariables;
    }

    public StepEntity getFormulaUnit() {
        return formulaUnit;
    }

    public String getFormulaStatus() {
        return formulaStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFormulaDefinition that = (StepFormulaDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(formulaType, that.formulaType) && Objects.equals(formulaExpression, that.formulaExpression) && Objects.equals(formulaVariables, that.formulaVariables) && Objects.equals(formulaUnit, that.formulaUnit) && Objects.equals(formulaStatus, that.formulaStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, formulaType, formulaExpression, formulaVariables, formulaUnit, formulaStatus);
    }

    @Override
    public String toString() {
        return "StepFormulaDefinition{" + "id=" + id + "name=" + name + "formulaType=" + formulaType + "formulaExpression=" + formulaExpression + "formulaVariables=" + formulaVariables + "formulaUnit=" + formulaUnit + "formulaStatus=" + formulaStatus + "}";
    }
}