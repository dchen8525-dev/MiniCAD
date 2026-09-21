package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepPreventiveAction extends AbstractStepEntity {
    private final String variancePotential;
    private final String varianceCause;
    private final String varianceAction;
    private final StepEntity varianceResponsible;
    private final StepEntity varianceTarget;
    private final String varianceStatus;
    private final String varianceVerification;

    public StepPreventiveAction(int id, String name, String variancePotential, String varianceCause, String varianceAction, StepEntity varianceResponsible, StepEntity varianceTarget, String varianceStatus, String varianceVerification) {
        super(id, name);
        this.variancePotential = variancePotential;
        this.varianceCause = varianceCause;
        this.varianceAction = varianceAction;
        this.varianceResponsible = varianceResponsible;
        this.varianceTarget = varianceTarget;
        this.varianceStatus = varianceStatus;
        this.varianceVerification = varianceVerification;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("variancePotential", variancePotential);
        state.put("varianceCause", varianceCause);
        state.put("varianceAction", varianceAction);
        state.put("varianceResponsible", varianceResponsible);
        state.put("varianceTarget", varianceTarget);
        state.put("varianceStatus", varianceStatus);
        state.put("varianceVerification", varianceVerification);
        return state;
    }
}
