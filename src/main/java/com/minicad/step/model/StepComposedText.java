package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COMPOSED_TEXT.
 * Composed text with extent information.
 *
 * @param id STEP instance id
 * @param name text name
 * @param collection collection of text elements
 * @param extent bounding extent
 */
public final class StepComposedText extends AbstractStepEntity {
    private final List<StepEntity> collection;
    private final StepEntity extent;

    public StepComposedText(int id, String name, List<StepEntity> collection, StepEntity extent) {
        super(id, name);
        this.collection = collection == null ? null : java.util.List.copyOf(collection);
        this.extent = extent;
    }

    public List<StepEntity> getCollection() {
        return collection;
    }

    public StepEntity getExtent() {
        return extent;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("collection", collection);
        state.put("extent", extent);
        return state;
    }
}
