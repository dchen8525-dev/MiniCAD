package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PMI_GROUP.
 */
public final class StepPmiGroup extends AbstractStepEntity {
    private final List<StepEntity> members;

    public StepPmiGroup(int id, String name, List<StepEntity> members) {
        super(id, name);
        this.members = members == null ? null : java.util.List.copyOf(members);
    }

    public List<StepEntity> getMembers() {
        return members;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("members", members);
        return state;
    }
}
