package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved FEA_GROUP_REPRESENTATION.
 * A grouping of finite element representations.
 */
public final class StepFeaGroupRepresentation extends AbstractStepEntity {
    private final List<StepEntity> representations;
    private final String groupType;

    public StepFeaGroupRepresentation(int id, String name, List<StepEntity> representations, String groupType) {
        super(id, name);
        this.representations = representations == null ? null : java.util.List.copyOf(representations);
        this.groupType = groupType;
    }

    public List<StepEntity> getRepresentations() {
        return representations;
    }

    public String getGroupType() {
        return groupType;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "representations", representations, "groupType", groupType);
    }
}
