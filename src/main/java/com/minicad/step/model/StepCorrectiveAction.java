package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepCorrectiveAction extends AbstractStepEntity {
    private final StepEntity varianceProblem;
    private final String varianceRootCause;
    private final String varianceAction;
    private final StepEntity varianceResponsible;
    private final StepEntity varianceTarget;
    private final String varianceStatus;
    private final String varianceEffectiveness;

    public StepCorrectiveAction(int id, String name, StepEntity varianceProblem, String varianceRootCause, String varianceAction, StepEntity varianceResponsible, StepEntity varianceTarget, String varianceStatus, String varianceEffectiveness) {
        super(id, name);
        this.varianceProblem = varianceProblem;
        this.varianceRootCause = varianceRootCause;
        this.varianceAction = varianceAction;
        this.varianceResponsible = varianceResponsible;
        this.varianceTarget = varianceTarget;
        this.varianceStatus = varianceStatus;
        this.varianceEffectiveness = varianceEffectiveness;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceProblem", varianceProblem);
        state.put("varianceRootCause", varianceRootCause);
        state.put("varianceAction", varianceAction);
        state.put("varianceResponsible", varianceResponsible);
        state.put("varianceTarget", varianceTarget);
        state.put("varianceStatus", varianceStatus);
        state.put("varianceEffectiveness", varianceEffectiveness);
        return state;
    }
}
