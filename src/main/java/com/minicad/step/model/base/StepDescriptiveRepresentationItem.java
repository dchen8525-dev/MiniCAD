package com.minicad.step.model.base;

/**
 * Minimal descriptive representation item.
 *
 * @param id STEP instance id
 * @param name item name
 * @param description descriptive text
 */
/**
 * Minimal descriptive representation item.
 *
 * @param id STEP instance id
 * @param name item name
 * @param description descriptive text
 */
public final class StepDescriptiveRepresentationItem implements StepEntity {
    private final int id;
    private final String name;
    private final String description;

    public StepDescriptiveRepresentationItem(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDescriptiveRepresentationItem that = (StepDescriptiveRepresentationItem) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "StepDescriptiveRepresentationItem{" + "id=" + id + "name=" + name + "description=" + description + "}";
    }
}
