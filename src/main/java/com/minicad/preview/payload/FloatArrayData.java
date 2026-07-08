package com.minicad.preview.payload;

/**
 * Float array data for STEP preview export.
 * Extracted from PreviewMeshPayloadTypes.
 */
public final class FloatArrayData {
    private final float[] values;
    private final int count;
    private final float[] min;
    private final float[] max;

    public FloatArrayData(float[] values, int count, float[] min, float[] max) {
        this.values = PreviewPayloadCopies.copy(values);
        this.count = count;
        this.min = PreviewPayloadCopies.copy(min);
        this.max = PreviewPayloadCopies.copy(max);
    }

    public float[] getValues() { return values; }
    public int getCount() { return count; }
    public float[] getMin() { return min; }
    public float[] getMax() { return max; }

    // Record-style accessors
    public float[] values() { return values; }
    public int count() { return count; }
    public float[] min() { return min; }
    public float[] max() { return max; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FloatArrayData that = (FloatArrayData) o;
        return java.util.Arrays.equals(values, that.values) && count == that.count && java.util.Arrays.equals(min, that.min) && java.util.Arrays.equals(max, that.max);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(java.util.Arrays.hashCode(values), count, java.util.Arrays.hashCode(min), java.util.Arrays.hashCode(max));
    }

    @Override
    public String toString() {
        return "FloatArrayData{values=" + java.util.Arrays.toString(values) + ", count=" + count + ", min=" + java.util.Arrays.toString(min) + ", max=" + java.util.Arrays.toString(max) + "}";
    }
}