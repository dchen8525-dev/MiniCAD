package com.minicad.step.model.fea;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FEA_GROUP_REPRESENTATION.
 * A grouping of finite element representations.
 */
/**
 * Resolved FEA_GROUP_REPRESENTATION.
 * A grouping of finite element representations.
 */
public final class StepFeaGroupRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> representations;
    private final String groupType;

    public StepFeaGroupRepresentation(int id, String name, List<StepEntity> representations, String groupType) {
        this.id = id;
        this.name = name;
        this.representations = representations == null ? null : java.util.List.copyOf(representations);
        this.groupType = groupType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getRepresentations() {
        return representations;
    }

    public String getGroupType() {
        return groupType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaGroupRepresentation that = (StepFeaGroupRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(representations, that.representations) && Objects.equals(groupType, that.groupType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, representations, groupType);
    }

    @Override
    public String toString() {
        return "StepFeaGroupRepresentation{" + "id=" + id + "name=" + name + "representations=" + representations + "groupType=" + groupType + "}";
    }
}
