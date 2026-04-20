package com.minicad.step.model.system;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved EXECUTION_CONTEXT.
 * An execution context entity.
 *
 * @param id STEP instance id
 * @param name context name
 * @param contextType context variance type
 * @param contextVariables context variance variable values
 * @param contextParent context variance parent context reference
 * @param contextDepth context variance nesting depth
 * @param contextStatus context variance status
 */
public record StepExecutionContext(
    int id,
    String name,
    String contextType,
    List<String> contextVariables,
    StepEntity contextParent,
    int contextDepth,
    String contextStatus) implements StepEntity {
}