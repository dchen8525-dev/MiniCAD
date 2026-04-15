package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PACKAGE_DEFINITION.
 * A package definition entity.
 *
 * @param id STEP instance id
 * @param name package name
 * @param packageType package variance type
 * @param packageDescription package variance description
 * @param packageContents package variance content definitions
 * @param packageDependencies package variance dependencies
 * @param packageStatus package variance status
 */
public record StepPackageDefinition2(
    int id,
    String name,
    String packageType,
    String packageDescription,
    List<StepEntity> packageContents,
    List<StepEntity> packageDependencies,
    String packageStatus) implements StepEntity {
}