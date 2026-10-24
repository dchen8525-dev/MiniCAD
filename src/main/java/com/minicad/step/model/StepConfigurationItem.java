package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved CONFIGURATION_ITEM.
 * A configuration-managed product definition.
 *
 * @param id STEP instance id
 * @param name item name
 * @param description item description
 * @param itemConceived product definition being configured
 * @param purpose configuration purpose
 */
public final class StepConfigurationItem extends AbstractStepEntity {
    private final String description;
    private final StepEntity itemConceived;
    private final String purpose;

    public StepConfigurationItem(int id, String name, String description, StepEntity itemConceived, String purpose) {
        super(id, name);
        this.description = description;
        this.itemConceived = itemConceived;
        this.purpose = purpose;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getItemConceived() {
        return itemConceived;
    }

    public String getPurpose() {
        return purpose;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "itemConceived", itemConceived,
                "purpose", purpose);
    }
}
