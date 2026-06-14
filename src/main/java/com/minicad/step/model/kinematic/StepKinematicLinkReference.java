package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * Resolved KINEMATIC_LINK_REFERENCE.
 */
/**
 * Resolved KINEMATIC_LINK_REFERENCE.
 */
public final class StepKinematicLinkReference implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity link;

    public StepKinematicLinkReference(int id, String name, StepEntity link) {
        this.id = id;
        this.name = name;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getLink() {
        return link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepKinematicLinkReference that = (StepKinematicLinkReference) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, link);
    }

    @Override
    public String toString() {
        return "StepKinematicLinkReference{" + "id=" + id + "name=" + name + "link=" + link + "}";
    }
}
