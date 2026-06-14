package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CORRECTIVE_ACTION.
 * A corrective action entity.
 *
 * @param id STEP instance id
 * @param name action name
 * @varianceProblem identified variance problem
 * @varianceRootCause root variance cause
 * @varianceAction corrective variance action description
 * @varianceResponsible responsible variance person
 * @varianceTarget target variance completion date
 * @varianceStatus action variance status
 * @varianceEffectiveness effectiveness variance verification
 */
/**
 * Resolved CORRECTIVE_ACTION.
 * A corrective action entity.
 *
 * @param id STEP instance id
 * @param name action name
 * @varianceProblem identified variance problem
 * @varianceRootCause root variance cause
 * @varianceAction corrective variance action description
 * @varianceResponsible responsible variance person
 * @varianceTarget target variance completion date
 * @varianceStatus action variance status
 * @varianceEffectiveness effectiveness variance verification
 */
public final class StepCorrectiveAction implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceProblem;
    private final String varianceRootCause;
    private final String varianceAction;
    private final StepEntity varianceResponsible;
    private final StepEntity varianceTarget;
    private final String varianceStatus;
    private final String varianceEffectiveness;

    public StepCorrectiveAction(int id, String name, StepEntity varianceProblem, String varianceRootCause, String varianceAction, StepEntity varianceResponsible, StepEntity varianceTarget, String varianceStatus, String varianceEffectiveness) {
        this.id = id;
        this.name = name;
        this.varianceProblem = varianceProblem;
        this.varianceRootCause = varianceRootCause;
        this.varianceAction = varianceAction;
        this.varianceResponsible = varianceResponsible;
        this.varianceTarget = varianceTarget;
        this.varianceStatus = varianceStatus;
        this.varianceEffectiveness = varianceEffectiveness;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceProblem() {
        return varianceProblem;
    }

    public String getVarianceRootCause() {
        return varianceRootCause;
    }

    public String getVarianceAction() {
        return varianceAction;
    }

    public StepEntity getVarianceResponsible() {
        return varianceResponsible;
    }

    public StepEntity getVarianceTarget() {
        return varianceTarget;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    public String getVarianceEffectiveness() {
        return varianceEffectiveness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCorrectiveAction that = (StepCorrectiveAction) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceProblem, that.varianceProblem) && Objects.equals(varianceRootCause, that.varianceRootCause) && Objects.equals(varianceAction, that.varianceAction) && Objects.equals(varianceResponsible, that.varianceResponsible) && Objects.equals(varianceTarget, that.varianceTarget) && Objects.equals(varianceStatus, that.varianceStatus) && Objects.equals(varianceEffectiveness, that.varianceEffectiveness);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceProblem, varianceRootCause, varianceAction, varianceResponsible, varianceTarget, varianceStatus, varianceEffectiveness);
    }

    @Override
    public String toString() {
        return "StepCorrectiveAction{" + "id=" + id + "name=" + name + "varianceProblem=" + varianceProblem + "varianceRootCause=" + varianceRootCause + "varianceAction=" + varianceAction + "varianceResponsible=" + varianceResponsible + "varianceTarget=" + varianceTarget + "varianceStatus=" + varianceStatus + "varianceEffectiveness=" + varianceEffectiveness + "}";
    }
}