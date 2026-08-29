package com.minicad.preview.payload;

/**
 * Int array data for STEP preview export.
 * Extracted from PreviewMeshPayloadTypes.
 */
public final class IntArrayData {
    private final int[] values;
    private final int count;

    public IntArrayData(int[] values, int count) {
        this.values = PreviewPayloadCopies.copy(values);
        this.count = count;
    }

    public int[] getValues() { return values; }
    public int getCount() { return count; }

    // Record-style accessors
    public int[] values() { return values; }
    public int count() { return count; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntArrayData that = (IntArrayData) o;
        return java.util.Arrays.equals(values, that.values) && count == that.count;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(java.util.Arrays.hashCode(values), count);
    }

    @Override
    public String toString() {
        return "IntArrayData{values=" + java.util.Arrays.toString(values) + ", count=" + count + "}";
    }
}
