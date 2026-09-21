package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StepCameraImage extends AbstractStepEntity {
    private final String description;
    private final int horizontalResolution;
    private final int verticalResolution;

    public StepCameraImage(int id, String name, String description, int horizontalResolution, int verticalResolution) {
        super(id, name);
        this.description = description;
        this.horizontalResolution = horizontalResolution;
        this.verticalResolution = verticalResolution;
    }

    public String getDescription() {
        return description;
    }

    public int getHorizontalResolution() {
        return horizontalResolution;
    }

    public int getVerticalResolution() {
        return verticalResolution;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("horizontalResolution", horizontalResolution);
        state.put("verticalResolution", verticalResolution);
        return state;
    }
}
