package com.minicad.step.model.technical.kinematic;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved PRISMATIC_PAIR.
 * A prismatic (sliding) kinematic pair allowing linear translation along one axis.
 */
/**
 * Resolved PRISMATIC_PAIR.
 * A prismatic (sliding) kinematic pair allowing linear translation along one axis.
 */
public final class StepPrismaticPair implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity position;
    private final StepEntity lineOfAction;
    private final StepEntity link1;
    private final StepEntity link2;

    public StepPrismaticPair(int id, String name, String description, StepEntity position, StepEntity lineOfAction, StepEntity link1, StepEntity link2) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.position = position;
        this.lineOfAction = lineOfAction;
        this.link1 = link1;
        this.link2 = link2;
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

    public StepEntity getPosition() {
        return position;
    }

    public StepEntity getLineOfAction() {
        return lineOfAction;
    }

    public StepEntity getLink1() {
        return link1;
    }

    public StepEntity getLink2() {
        return link2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPrismaticPair that = (StepPrismaticPair) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(position, that.position) && Objects.equals(lineOfAction, that.lineOfAction) && Objects.equals(link1, that.link1) && Objects.equals(link2, that.link2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, position, lineOfAction, link1, link2);
    }

    @Override
    public String toString() {
        return "StepPrismaticPair{" + "id=" + id + "name=" + name + "description=" + description + "position=" + position + "lineOfAction=" + lineOfAction + "link1=" + link1 + "link2=" + link2 + "}";
    }
}
