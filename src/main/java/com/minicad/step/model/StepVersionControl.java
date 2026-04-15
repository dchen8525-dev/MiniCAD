package com.minicad.step.model;

import java.util.List;

/**
 * Resolved VERSION_CONTROL.
 * A version control entity.
 *
 * @param id STEP instance id
 * @param name version name
 * @param versionNumber version number/identifier
 * @param versionDescription version description
 * @param versionDate version release date
 * @param versionAuthor version author
 * @param versionStatus version status (draft, released, archived)
 * @param previousVersion previous version reference
 * @param versionChanges changes from previous version
 */
public record StepVersionControl(
    int id,
    String name,
    String versionNumber,
    String versionDescription,
    StepEntity versionDate,
    StepEntity versionAuthor,
    String versionStatus,
    StepEntity previousVersion,
    List<StepEntity> versionChanges) implements StepEntity {
}