package com.minicad.step.model;

import java.util.List;

/**
 * Resolved INDEX_INSTANCE.
 * An index instance entity.
 *
 * @param id STEP instance id
 * @param name index instance name
 * @param indexDefinition index variance definition reference
 * @param indexState index variance state
 * @param indexEntries index variance entry count
 * @param indexSize index variance size in bytes
 * @param indexStatus index variance status
 */
public record StepIndexInstance(
    int id,
    String name,
    StepEntity indexDefinition,
    String indexState,
    long indexEntries,
    long indexSize,
    String indexStatus) implements StepEntity {
}