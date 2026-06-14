package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved KINEMATIC_PATH.
 * A path through a kinematic mechanism defining the chain of pairs.
 */
/**
 * Resolved KINEMATIC_PATH.
 * A path through a kinematic mechanism defining the chain of pairs.
 */
public final class StepKinematicPath implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity startLink;
    private final StepEntity endLink;
    private final List<StepEntity> pairs;

    public StepKinematicPath(int id, String name, String description, StepEntity startLink, StepEntity endLink, List<StepEntity> pairs) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startLink = startLink;
        this.endLink = endLink;
        this.pairs = pairs == null ? null : java.util.List.copyOf(pairs);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getStartLink() {
        return startLink;
    }

    public StepEntity getEndLink() {
        return endLink;
    }

    public List<StepEntity> getPairs() {
        return pairs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepKinematicPath that = (StepKinematicPath) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(startLink, that.startLink) && Objects.equals(endLink, that.endLink) && Objects.equals(pairs, that.pairs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, startLink, endLink, pairs);
    }

    @Override
    public String toString() {
        return "StepKinematicPath{" + "id=" + id + "name=" + name + "description=" + description + "startLink=" + startLink + "endLink=" + endLink + "pairs=" + pairs + "}";
    }
}
