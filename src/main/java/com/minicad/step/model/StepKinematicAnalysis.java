package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved KINEMATIC_ANALYSIS.
 * A kinematic analysis entity.
 *
 * @param id STEP instance id
 * @param name analysis name
 * @param mechanism reference kinematic structure
 * @param inputMotion input motion parameters
 * @param outputMotion output motion results
 * @param motionRange motion range limits
 * @param kinematicConstraints kinematic constraints
 */
public final class StepKinematicAnalysis extends AbstractStepEntity {
    private final StepEntity mechanism;
    private final List<StepEntity> inputMotion;
    private final List<StepEntity> outputMotion;
    private final List<Double> motionRange;
    private final List<StepEntity> kinematicConstraints;

    public StepKinematicAnalysis(int id, String name, StepEntity mechanism, List<StepEntity> inputMotion, List<StepEntity> outputMotion, List<Double> motionRange, List<StepEntity> kinematicConstraints) {
        super(id, name);
        this.mechanism = mechanism;
        this.inputMotion = inputMotion == null ? null : java.util.List.copyOf(inputMotion);
        this.outputMotion = outputMotion == null ? null : java.util.List.copyOf(outputMotion);
        this.motionRange = motionRange == null ? null : java.util.List.copyOf(motionRange);
        this.kinematicConstraints = kinematicConstraints == null ? null : java.util.List.copyOf(kinematicConstraints);
    }

    public StepEntity getMechanism() {
        return mechanism;
    }

    public List<StepEntity> getInputMotion() {
        return inputMotion;
    }

    public List<StepEntity> getOutputMotion() {
        return outputMotion;
    }

    public List<Double> getMotionRange() {
        return motionRange;
    }

    public List<StepEntity> getKinematicConstraints() {
        return kinematicConstraints;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("mechanism", mechanism);
        state.put("inputMotion", inputMotion);
        state.put("outputMotion", outputMotion);
        state.put("motionRange", motionRange);
        state.put("kinematicConstraints", kinematicConstraints);
        return state;
    }
}
