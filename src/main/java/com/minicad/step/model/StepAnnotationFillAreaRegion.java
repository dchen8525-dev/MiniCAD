package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved ANNOTATION_FILL_AREA_REGION.
 */
public final class StepAnnotationFillAreaRegion extends AbstractStepEntity {
    private final List<StepEntity> regions;

    public StepAnnotationFillAreaRegion(int id, String name, List<StepEntity> regions) {
        super(id, name);
        this.regions = regions == null ? null : java.util.List.copyOf(regions);
    }

    public List<StepEntity> getRegions() {
        return regions;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "regions", regions);
    }
}
