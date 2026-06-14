package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
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
public final class StepConfigurationItem implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity itemConceived;
    private final String purpose;

    public StepConfigurationItem(int id, String name, String description, StepEntity itemConceived, String purpose) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.itemConceived = itemConceived;
        this.purpose = purpose;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConfigurationItem that = (StepConfigurationItem) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(itemConceived, that.itemConceived) && Objects.equals(purpose, that.purpose);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, itemConceived, purpose);
    }

    @Override
    public String toString() {
        return "StepConfigurationItem{" + "id=" + id + "name=" + name + "description=" + description + "itemConceived=" + itemConceived + "purpose=" + purpose + "}";
    }
}
