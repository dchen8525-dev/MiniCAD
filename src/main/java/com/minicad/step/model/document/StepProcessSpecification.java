package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved PROCESS_SPECIFICATION.
 * A process specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceProcess specified variance process
 * @varianceParameters process variance parameters
 * @varianceRanges parameter variance ranges
 * @varianceMaterials material variance requirements
 * @varianceTools tool variance requirements
 * @varianceStatus specification variance status
 */
public record StepProcessSpecification(
    int id,
    String name,
    StepEntity varianceProcess,
    List<String> varianceParameters,
    List<Double> varianceRanges,
    List<StepEntity> varianceMaterials,
    List<StepEntity> varianceTools,
    String varianceStatus) implements StepEntity {
}