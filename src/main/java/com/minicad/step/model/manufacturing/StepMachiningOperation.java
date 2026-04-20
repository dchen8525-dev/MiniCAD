package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved MACHINING_OPERATION.
 * Represents a machining operation in manufacturing.
 *
 * @param id STEP instance id
 * @param name operation name
 * @param status operation status
 * @param features features being machined
 */
public record StepMachiningOperation(
    int id,
    String name,
    StepEntity status,
    List<StepEntity> features) implements StepEntity {
}