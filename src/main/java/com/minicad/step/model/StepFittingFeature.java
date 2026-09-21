package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FITTING_FEATURE.
 * A fitting feature entity.
 *
 * @param id STEP instance id
 * @param name fitting name
 * @param fittingType fitting type (elbow, tee, reducer, coupling)
 * @param fittingAngle fitting angle for elbows
 * @param connectionEnds connection end features
 * @param fittingMaterial fitting material specification
 * @param fittingStandard fitting standard specification
 */
public final class StepFittingFeature extends AbstractStepEntity {
    private final String fittingType;
    private final double fittingAngle;
    private final List<StepEntity> connectionEnds;
    private final StepEntity fittingMaterial;
    private final String fittingStandard;

    public StepFittingFeature(int id, String name, String fittingType, double fittingAngle, List<StepEntity> connectionEnds, StepEntity fittingMaterial, String fittingStandard) {
        super(id, name);
        this.fittingType = fittingType;
        this.fittingAngle = fittingAngle;
        this.connectionEnds = connectionEnds == null ? null : java.util.List.copyOf(connectionEnds);
        this.fittingMaterial = fittingMaterial;
        this.fittingStandard = fittingStandard;
    }

    public String getFittingType() {
        return fittingType;
    }

    public double getFittingAngle() {
        return fittingAngle;
    }

    public List<StepEntity> getConnectionEnds() {
        return connectionEnds;
    }

    public StepEntity getFittingMaterial() {
        return fittingMaterial;
    }

    public String getFittingStandard() {
        return fittingStandard;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("fittingType", fittingType);
        state.put("fittingAngle", fittingAngle);
        state.put("connectionEnds", connectionEnds);
        state.put("fittingMaterial", fittingMaterial);
        state.put("fittingStandard", fittingStandard);
        return state;
    }
}
