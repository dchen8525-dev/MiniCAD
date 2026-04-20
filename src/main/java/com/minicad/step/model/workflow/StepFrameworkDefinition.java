package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FRAMEWORK_DEFINITION.
 * A framework definition entity.
 *
 * @param id STEP instance id
 * @param name framework name
 * @param frameworkType framework variance type
 * @param frameworkDescription framework variance description
 * @param frameworkModules framework variance module definitions
 * @param frameworkExtensions framework variance extension points
 * @param frameworkStatus framework variance status
 */
public record StepFrameworkDefinition(
    int id,
    String name,
    String frameworkType,
    String frameworkDescription,
    List<StepEntity> frameworkModules,
    List<String> frameworkExtensions,
    String frameworkStatus) implements StepEntity {
}