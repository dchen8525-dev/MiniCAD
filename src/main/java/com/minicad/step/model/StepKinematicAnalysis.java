package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepKinematicAnalysis implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity mechanism;
    private final List<StepEntity> inputMotion;
    private final List<StepEntity> outputMotion;
    private final List<Double> motionRange;
    private final List<StepEntity> kinematicConstraints;

    public StepKinematicAnalysis(int id, String name, StepEntity mechanism, List<StepEntity> inputMotion, List<StepEntity> outputMotion, List<Double> motionRange, List<StepEntity> kinematicConstraints) {
        this.id = id;
        this.name = name;
        this.mechanism = mechanism;
        this.inputMotion = inputMotion == null ? null : java.util.List.copyOf(inputMotion);
        this.outputMotion = outputMotion == null ? null : java.util.List.copyOf(outputMotion);
        this.motionRange = motionRange == null ? null : java.util.List.copyOf(motionRange);
        this.kinematicConstraints = kinematicConstraints == null ? null : java.util.List.copyOf(kinematicConstraints);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepKinematicAnalysis that = (StepKinematicAnalysis) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(mechanism, that.mechanism) && Objects.equals(inputMotion, that.inputMotion) && Objects.equals(outputMotion, that.outputMotion) && Objects.equals(motionRange, that.motionRange) && Objects.equals(kinematicConstraints, that.kinematicConstraints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, mechanism, inputMotion, outputMotion, motionRange, kinematicConstraints);
    }

    @Override
    public String toString() {
        return "StepKinematicAnalysis{" + "id=" + id + "name=" + name + "mechanism=" + mechanism + "inputMotion=" + inputMotion + "outputMotion=" + outputMotion + "motionRange=" + motionRange + "kinematicConstraints=" + kinematicConstraints + "}";
    }
}