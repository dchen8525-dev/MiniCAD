package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved DESIGNED_PART_DESIGN_VERSION.
 * A design version associated with a designed part.
 *
 * @param id STEP instance id
 * @param name part name
 * @param description part description
 * @param frameOfReference product context
 */
public record StepDesignedPartDesignVersion(
    int id,
    String name,
    String description,
    StepEntity frameOfReference) implements StepEntity {
}
