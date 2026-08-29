package com.minicad.preview.payload;

/**
 * Indexed triangle mesh for STEP preview export.
 * Extracted from PreviewMeshPayloadTypes.
 */
public final class IndexedTriangleMesh {
    private final FloatArrayData positions;
    private final FloatArrayData normals;
    private final IntArrayData indices;

    public IndexedTriangleMesh(FloatArrayData positions, FloatArrayData normals, IntArrayData indices) {
        this.positions = positions;
        this.normals = normals;
        this.indices = indices;
    }

    public FloatArrayData getPositions() { return positions; }
    public FloatArrayData getNormals() { return normals; }
    public IntArrayData getIndices() { return indices; }

    // Record-style accessors
    public FloatArrayData positions() { return positions; }
    public FloatArrayData normals() { return normals; }
    public IntArrayData indices() { return indices; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexedTriangleMesh that = (IndexedTriangleMesh) o;
        return java.util.Objects.equals(positions, that.positions) && java.util.Objects.equals(normals, that.normals) && java.util.Objects.equals(indices, that.indices);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(positions, normals, indices);
    }

    @Override
    public String toString() {
        return "IndexedTriangleMesh{positions=" + positions + ", normals=" + normals + ", indices=" + indices + "}";
    }
}
