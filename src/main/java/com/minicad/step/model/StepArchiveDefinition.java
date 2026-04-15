package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ARCHIVE_DEFINITION.
 * An archive definition entity.
 *
 * @param id STEP instance id
 * @param name archive name
 * @param archiveType archive variance type
 * @param archiveSource archive variance source reference
 * @param archiveTarget archive variance target reference
 * @param archiveFormat archive variance format
 * @param archiveRetention archive variance retention period
 * @param archiveStatus archive variance status
 */
public record StepArchiveDefinition(
    int id,
    String name,
    String archiveType,
    StepEntity archiveSource,
    StepEntity archiveTarget,
    String archiveFormat,
    int archiveRetention,
    String archiveStatus) implements StepEntity {
}