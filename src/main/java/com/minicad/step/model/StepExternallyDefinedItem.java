package com.minicad.step.model;

/**
 * Minimal externally defined item metadata.
 *
 * @param id STEP instance id
 * @param itemId external item identifier
 * @param source external source
 * @param entityName concrete STEP entity name
 */
public record StepExternallyDefinedItem(
        int id,
        String itemId,
        StepExternalSource source,
        String entityName
) implements StepEntity {

    @Override
    public String name() {
        return itemId;
    }
}
