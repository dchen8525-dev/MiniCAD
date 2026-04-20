package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FILTER_INSTANCE.
 * A filter instance entity.
 *
 * @param id STEP instance id
 * @param name filter instance name
 * @param filterDefinition filter variance definition reference
 * @param filterState filter variance state
 * @param filterMatchCount filter variance match count
 * @param filterRejectCount filter variance reject count
 * @param filterStatus filter variance status
 */
public record StepFilterInstance(
    int id,
    String name,
    StepEntity filterDefinition,
    String filterState,
    int filterMatchCount,
    int filterRejectCount,
    String filterStatus) implements StepEntity {
}