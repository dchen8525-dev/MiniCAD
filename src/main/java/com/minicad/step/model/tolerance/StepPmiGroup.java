package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved PMI_GROUP.
 */
public record StepPmiGroup(
    int id,
    String name,
    List<StepEntity> members
) implements StepEntity {

    public StepPmiGroup {
        members = List.copyOf(members);
    }
}
