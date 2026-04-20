package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FEA_GROUP_REPRESENTATION.
 * A grouping of finite element representations.
 */
public record StepFeaGroupRepresentation(
    int id,
    String name,
    List<StepEntity> representations,
    String groupType
) implements StepEntity {

    public StepFeaGroupRepresentation {
        representations = List.copyOf(representations);
    }
}
