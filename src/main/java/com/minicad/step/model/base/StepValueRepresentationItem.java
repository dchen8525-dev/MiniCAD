package com.minicad.step.model.base;

/**
 * Minimal value representation item.
 *
 * @param id STEP instance id
 * @param name item name
 * @param valueType typed wrapper name
 * @param valueText unwrapped literal text
 */
/**
 * Minimal value representation item.
 *
 * @param id STEP instance id
 * @param name item name
 * @param valueType typed wrapper name
 * @param valueText unwrapped literal text
 */
public final class StepValueRepresentationItem implements StepEntity {
    private final int id;
    private final String name;
    private final String valueType;
    private final String valueText;

    public StepValueRepresentationItem(int id, String name, String valueType, String valueText) {
        this.id = id;
        this.name = name;
        this.valueType = valueType;
        this.valueText = valueText;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValueType() {
        return valueType;
    }

    public String getValueText() {
        return valueText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepValueRepresentationItem that = (StepValueRepresentationItem) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(valueType, that.valueType) && Objects.equals(valueText, that.valueText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, valueType, valueText);
    }

    @Override
    public String toString() {
        return "StepValueRepresentationItem{" + "id=" + id + "name=" + name + "valueType=" + valueType + "valueText=" + valueText + "}";
    }
}
