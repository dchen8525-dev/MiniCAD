package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved PRISMATIC_PAIR.
 * A prismatic (sliding) kinematic pair allowing linear translation along one axis.
 */
public final class StepPrismaticPair extends AbstractStepEntity {
    private final String description;
    private final StepEntity position;
    private final StepEntity lineOfAction;
    private final StepEntity link1;
    private final StepEntity link2;

    public StepPrismaticPair(int id, String name, String description, StepEntity position, StepEntity lineOfAction, StepEntity link1, StepEntity link2) {
        super(id, name);
        this.description = description;
        this.position = position;
        this.lineOfAction = lineOfAction;
        this.link1 = link1;
        this.link2 = link2;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("position", position);
        state.put("lineOfAction", lineOfAction);
        state.put("link1", link1);
        state.put("link2", link2);
        return state;
    }
}
