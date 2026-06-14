package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PREVENTIVE_ACTION.
 * A preventive action entity.
 *
 * @param id STEP instance id
 * @param name action name
 * @variancePotentialPotential variance issue
 * @varianceCause potential variance cause
 * @varianceAction preventive variance action description
 * @varianceResponsible responsible variance person
 * @varianceTarget target variance completion date
 * @varianceStatus action variance status
 * @varianceVerification verification variance method
 */
/**
 * Resolved PREVENTIVE_ACTION.
 * A preventive action entity.
 *
 * @param id STEP instance id
 * @param name action name
 * @variancePotentialPotential variance issue
 * @varianceCause potential variance cause
 * @varianceAction preventive variance action description
 * @varianceResponsible responsible variance person
 * @varianceTarget target variance completion date
 * @varianceStatus action variance status
 * @varianceVerification verification variance method
 */
public final class StepPreventiveAction implements StepEntity {
    private final int id;
    private final String name;
    private final String variancePotential;
    private final String varianceCause;
    private final String varianceAction;
    private final StepEntity varianceResponsible;
    private final StepEntity varianceTarget;
    private final String varianceStatus;
    private final String varianceVerification;

    public StepPreventiveAction(int id, String name, String variancePotential, String varianceCause, String varianceAction, StepEntity varianceResponsible, StepEntity varianceTarget, String varianceStatus, String varianceVerification) {
        this.id = id;
        this.name = name;
        this.variancePotential = variancePotential;
        this.varianceCause = varianceCause;
        this.varianceAction = varianceAction;
        this.varianceResponsible = varianceResponsible;
        this.varianceTarget = varianceTarget;
        this.varianceStatus = varianceStatus;
        this.varianceVerification = varianceVerification;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVariancePotential() {
        return variancePotential;
    }

    public String getVarianceCause() {
        return varianceCause;
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

    public String getVarianceVerification() {
        return varianceVerification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPreventiveAction that = (StepPreventiveAction) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(variancePotential, that.variancePotential) && Objects.equals(varianceCause, that.varianceCause) && Objects.equals(varianceAction, that.varianceAction) && Objects.equals(varianceResponsible, that.varianceResponsible) && Objects.equals(varianceTarget, that.varianceTarget) && Objects.equals(varianceStatus, that.varianceStatus) && Objects.equals(varianceVerification, that.varianceVerification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, variancePotential, varianceCause, varianceAction, varianceResponsible, varianceTarget, varianceStatus, varianceVerification);
    }

    @Override
    public String toString() {
        return "StepPreventiveAction{" + "id=" + id + "name=" + name + "variancePotential=" + variancePotential + "varianceCause=" + varianceCause + "varianceAction=" + varianceAction + "varianceResponsible=" + varianceResponsible + "varianceTarget=" + varianceTarget + "varianceStatus=" + varianceStatus + "varianceVerification=" + varianceVerification + "}";
    }
}