package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COMPOSITE_TEXT.
 * Text composed of multiple text literals and text paths.
 *
 * @param id STEP instance id
 * @param name text name
 * @param collection collection of text elements
 */
public final class StepCompositeText extends AbstractStepEntity {
    private final List<StepEntity> collection;

    public StepCompositeText(int id, String name, List<StepEntity> collection) {
        super(id, name);
        this.collection = collection == null ? null : java.util.List.copyOf(collection);
    }

    public List<StepEntity> getCollection() {
        return collection;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("collection", collection);
        return state;
    }
}
