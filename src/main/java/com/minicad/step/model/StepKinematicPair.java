package com.minicad.step.model;

import java.util.Map;

public final class StepKinematicPair extends AbstractStepEntity {
    private final String description;
    private final StepEntity link1;
    private final StepEntity link2;

    public StepKinematicPair(int id, String name, String description, StepEntity link1, StepEntity link2) {
        super(id, name);
        this.description = description;
        this.link1 = link1;
        this.link2 = link2;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getLink1() {
        return link1;
    }

    public StepEntity getLink2() {
        return link2;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "link1", link1, "link2", link2);
    }
}
