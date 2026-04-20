package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FRAMEWORK_INSTANCE.
 * A framework instance entity.
 *
 * @param id STEP instance id
 * @param name framework instance name
 * @param frameworkDefinition framework variance definition reference
 * @param frameworkState framework variance state
 * @param frameworkVersion framework variance version
 * @param frameworkConfig framework variance configuration
 * @param frameworkStatus framework variance status
 */
public record StepFrameworkInstance(
    int id,
    String name,
    StepEntity frameworkDefinition,
    String frameworkState,
    String frameworkVersion,
    List<String> frameworkConfig,
    String frameworkStatus) implements StepEntity {
}