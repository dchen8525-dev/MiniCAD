package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal product definition root.
 *
 * @param id STEP instance id
 * @param identifier business identifier
 * @param name product name
 * @param description optional description
 * @param frameOfReference product contexts
 */
public record StepProduct(
        int id,
        String identifier,
        String name,
        String description,
        List<StepProductContext> frameOfReference
) implements StepEntity {

    public StepProduct {
        frameOfReference = List.copyOf(frameOfReference);
    }
}
