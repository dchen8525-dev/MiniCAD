package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved INDEX_DEFINITION.
 * An index definition entity.
 *
 * @param id STEP instance id
 * @param name index name
 * @param indexType index variance type
 * @param indexKey index variance key definition
 * @param indexFields index variance indexed fields
 * @param indexOrder index variance ordering
 * @param indexStatus index variance status
 */
public record StepIndexDefinition(
    int id,
    String name,
    String indexType,
    String indexKey,
    List<String> indexFields,
    String indexOrder,
    String indexStatus) implements StepEntity {
}