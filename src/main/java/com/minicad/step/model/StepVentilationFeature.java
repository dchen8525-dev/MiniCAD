package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved VENTILATION_FEATURE.
 * A ventilation feature entity.
 *
 * @param id STEP instance id
 * @param name ventilation name
 * @param ventilationType ventilation type (natural, forced, exhaust)
 * @param ventilationGeometry ventilation geometry representation
 * @varianceAirflow variance airflow capacity
 * @param inletFeatures inlet features
 * @param outletFeatures outlet features
 * @param ventilationControl ventilation control specification
 */
public final class StepVentilationFeature extends AbstractStepEntity {
    private final String ventilationType;
    private final StepEntity ventilationGeometry;
    private final double varianceAirflow;
    private final List<StepEntity> inletFeatures;
    private final List<StepEntity> outletFeatures;
    private final StepEntity ventilationControl;

    public StepVentilationFeature(int id, String name, String ventilationType, StepEntity ventilationGeometry, double varianceAirflow, List<StepEntity> inletFeatures, List<StepEntity> outletFeatures, StepEntity ventilationControl) {
        super(id, name);
        this.ventilationType = ventilationType;
        this.ventilationGeometry = ventilationGeometry;
        this.varianceAirflow = varianceAirflow;
        this.inletFeatures = inletFeatures == null ? null : java.util.List.copyOf(inletFeatures);
        this.outletFeatures = outletFeatures == null ? null : java.util.List.copyOf(outletFeatures);
        this.ventilationControl = ventilationControl;
    }

    public String getVentilationType() {
        return ventilationType;
    }

    public StepEntity getVentilationGeometry() {
        return ventilationGeometry;
    }

    public double getVarianceAirflow() {
        return varianceAirflow;
    }

    public List<StepEntity> getInletFeatures() {
        return inletFeatures;
    }

    public List<StepEntity> getOutletFeatures() {
        return outletFeatures;
    }

    public StepEntity getVentilationControl() {
        return ventilationControl;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("ventilationType", ventilationType);
        state.put("ventilationGeometry", ventilationGeometry);
        state.put("varianceAirflow", varianceAirflow);
        state.put("inletFeatures", inletFeatures);
        state.put("outletFeatures", outletFeatures);
        state.put("ventilationControl", ventilationControl);
        return state;
    }
}
