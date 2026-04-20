package com.minicad.step.model.analysis;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepKinematicAnalysis(
    int id,
    String name,
    StepEntity mechanism,
    List<StepEntity> inputMotion,
    List<StepEntity> outputMotion,
    List<Double> motionRange,
    List<StepEntity> kinematicConstraints) implements StepEntity {
}