package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ERROR_INSTANCE.
 * An error instance entity.
 *
 * @param id STEP instance id
 * @param name error instance name
 * @param errorDefinition error variance definition reference
 * @param errorContext error variance context
 * @param errorTime error variance occurrence time
 * @param errorStackTrace error variance stack trace
 * @param errorResolved error variance resolved flag
 * @param errorStatus error variance status
 */
public record StepErrorInstance(
    int id,
    String name,
    StepEntity errorDefinition,
    String errorContext,
    StepEntity errorTime,
    String errorStackTrace,
    boolean errorResolved,
    String errorStatus) implements StepEntity {
}