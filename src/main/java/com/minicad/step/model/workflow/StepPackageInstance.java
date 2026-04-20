package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved PACKAGE_INSTANCE.
 * A package instance entity.
 *
 * @param id STEP instance id
 * @param name package instance name
 * @param packageDefinition package variance definition reference
 * @param packageState package variance state
 * @param packageVersion package variance version
 * @param packageInstalled package variance installed flag
 * @param packageStatus package variance status
 */
public record StepPackageInstance(
    int id,
    String name,
    StepEntity packageDefinition,
    String packageState,
    String packageVersion,
    boolean packageInstalled,
    String packageStatus) implements StepEntity {
}