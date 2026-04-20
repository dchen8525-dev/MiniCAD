package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FUNCTION_SPECIFICATION.
 * A function specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceFunction specified variance function
 * @varianceInputs function variance inputs
 * @varianceOutputs function variance outputs
 * @variancePerformance performance variance requirements
 * @varianceReliability reliability variance requirements
 * @varianceStatus specification variance status
 */
public record StepFunctionSpecification(
    int id,
    String name,
    String varianceFunction,
    List<StepEntity> varianceInputs,
    List<StepEntity> varianceOutputs,
    List<Double> variancePerformance,
    List<Double> varianceReliability,
    String varianceStatus) implements StepEntity {
}