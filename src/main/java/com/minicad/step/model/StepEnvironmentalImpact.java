package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ENVIRONMENTAL_IMPACT.
 * An environmental impact entity.
 *
 * @param id STEP instance id
 * @param name impact name
 * @param impactType impact type (energy, waste, emissions)
 * @param impactValue impact value measurement
 * @param impactUnit impact unit specification
 * @varianceTarget target variance reduction value
 * @param mitigationMeasures mitigation measures
 * @varianceStatus impact variance status
 */
public final class StepEnvironmentalImpact extends AbstractStepEntity {
    private final String impactType;
    private final double impactValue;
    private final StepEntity impactUnit;
    private final double varianceTarget;
    private final List<StepEntity> mitigationMeasures;
    private final String varianceStatus;

    public StepEnvironmentalImpact(int id, String name, String impactType, double impactValue, StepEntity impactUnit, double varianceTarget, List<StepEntity> mitigationMeasures, String varianceStatus) {
        super(id, name);
        this.impactType = impactType;
        this.impactValue = impactValue;
        this.impactUnit = impactUnit;
        this.varianceTarget = varianceTarget;
        this.mitigationMeasures = mitigationMeasures == null ? null : java.util.List.copyOf(mitigationMeasures);
        this.varianceStatus = varianceStatus;
    }

    public String getImpactType() {
        return impactType;
    }

    public double getImpactValue() {
        return impactValue;
    }

    public StepEntity getImpactUnit() {
        return impactUnit;
    }

    public double getVarianceTarget() {
        return varianceTarget;
    }

    public List<StepEntity> getMitigationMeasures() {
        return mitigationMeasures;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("impactType", impactType);
        state.put("impactValue", impactValue);
        state.put("impactUnit", impactUnit);
        state.put("varianceTarget", varianceTarget);
        state.put("mitigationMeasures", mitigationMeasures);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
