package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved WITH_DESCRIPTIVE_REPRESENTATION_ITEM.
 * A representation that includes descriptive text items.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param description descriptive text
 * @param items representation items
 * @param context representation context
 */
public final class StepWithDescriptiveRepresentationItem extends AbstractStepEntity {
    private final String description;
    private final List<StepEntity> items;
    private final StepEntity context;

    public StepWithDescriptiveRepresentationItem(int id, String name, String description, List<StepEntity> items, StepEntity context) {
        super(id, name);
        this.description = description;
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
    }

    public String getDescription() {
        return description;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public StepEntity getContext() {
        return context;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("items", items);
        state.put("context", context);
        return state;
    }
}
