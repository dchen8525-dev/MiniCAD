package com.minicad.preview.payload;

/**
 * Point range for binary geometry buffers.
 */
public final class PointRange {
    private final int offset;
    private final int count;

    public PointRange(int offset, int count) {
        this.offset = offset;
        this.count = count;
    }

    public int getOffset() { return offset; }
    public int getCount() { return count; }

    public int offset() { return offset; }
    public int count() { return count; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointRange that = (PointRange) o;
        return offset == that.offset && count == that.count;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(offset, count);
    }

    @Override
    public String toString() {
        return "PointRange{offset=" + offset + ", count=" + count + "}";
    }
}
