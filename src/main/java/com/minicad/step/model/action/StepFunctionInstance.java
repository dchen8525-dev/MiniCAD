package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FUNCTION_INSTANCE.
 * A function instance entity.
 *
 * @param id STEP instance id
 * @param name function instance name
 * @param functionDefinition function variance definition reference
 * @param functionState function variance state
 * @param functionCallCount function variance call count
 * @param functionLastError function variance last error
 * @param functionStatus function variance status
 */
public record StepFunctionInstance(
    int id,
    String name,
    StepEntity functionDefinition,
    String functionState,
    int functionCallCount,
    String functionLastError,
    String functionStatus) implements StepEntity {
}