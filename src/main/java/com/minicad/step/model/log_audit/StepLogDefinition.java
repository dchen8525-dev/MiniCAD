package com.minicad.step.model.log_audit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved LOG_DEFINITION.
 * A log definition entity.
 *
 * @param id STEP instance id
 * @param name log name
 * @param logType log variance type
 * @param logLevel log variance level
 * @param logFormat log variance format
 * @param logRetention log variance retention period
 * @param logStatus log variance status
 */
public record StepLogDefinition(
    int id,
    String name,
    String logType,
    String logLevel,
    String logFormat,
    int logRetention,
    String logStatus) implements StepEntity {
}