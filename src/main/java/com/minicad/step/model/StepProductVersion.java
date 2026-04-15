package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PRODUCT_VERSION.
 * A product version entity.
 *
 * @param id STEP instance id
 * @param name version name
 * @param versionId version identifier
 * @param description version description
 * @param product relating product
 * @param versionContext version context information
 */
public record StepProductVersion(
    int id,
    String name,
    String versionId,
    String description,
    StepEntity product,
    StepEntity versionContext) implements StepEntity {
}