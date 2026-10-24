package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved KINEMATIC_PATH.
 * A path through a kinematic mechanism defining the chain of pairs.
 */
public final class StepKinematicPath extends AbstractStepEntity {
    private final String description;
    private final StepEntity startLink;
    private final StepEntity endLink;
    private final List<StepEntity> pairs;

    public StepKinematicPath(int id, String name, String description, StepEntity startLink, StepEntity endLink, List<StepEntity> pairs) {
        super(id, name);
        this.description = description;
        this.startLink = startLink;
        this.endLink = endLink;
        this.pairs = pairs == null ? null : java.util.List.copyOf(pairs);
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "startLink", startLink,
                "endLink", endLink, "pairs", pairs);
    }
}
