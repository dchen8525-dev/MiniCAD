package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ARCHIVE_INSTANCE.
 * An archive instance entity.
 *
 * @param id STEP instance id
 * @param name archive instance name
 * @param archiveDefinition archive variance definition reference
 * @param archiveTime archive variance creation time
 * @param archiveSize archive variance size
 * @param archiveEntries archive variance entry count
 * @param archiveStatus archive variance status
 */
public record StepArchiveInstance(
    int id,
    String name,
    StepEntity archiveDefinition,
    StepEntity archiveTime,
    long archiveSize,
    int archiveEntries,
    String archiveStatus) implements StepEntity {
}