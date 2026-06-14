package com.minicad.step.model.base;

/**
 * Minimal representation item marker.
 *
 * @param id STEP instance id
 * @param name item name
 */
/**
 * Minimal representation item marker.
 *
 * @param id STEP instance id
 * @param name item name
 */
public final class StepRepresentationItem implements StepEntity {
    private final int id;
    private final String name;

    public StepRepresentationItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRepresentationItem that = (StepRepresentationItem) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepRepresentationItem{" + "id=" + id + "name=" + name + "}";
    }
}
