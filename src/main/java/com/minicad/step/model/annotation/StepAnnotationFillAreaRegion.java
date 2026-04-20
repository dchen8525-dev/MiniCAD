package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ANNOTATION_FILL_AREA_REGION.
 */
public record StepAnnotationFillAreaRegion(
    int id,
    String name,
    List<StepEntity> regions
) implements StepEntity {

    public StepAnnotationFillAreaRegion {
        regions = List.copyOf(regions);
    }
}
