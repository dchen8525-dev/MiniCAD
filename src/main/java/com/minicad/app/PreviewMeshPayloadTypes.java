package com.minicad.app;

import java.util.List;

/**
 * Mesh and geometry payload types for STEP preview export.
 * Extracted from StepPreviewPayloadTypes to reduce file size.
 */
final class GeometrySummary {
    private final int faceCount;
    private final int edgeCount;
    private final double approxSurfaceArea;
    private final double approxEdgeLength;

    public GeometrySummary(int faceCount, int edgeCount, double approxSurfaceArea, double approxEdgeLength) {
        this.faceCount = faceCount;
        this.edgeCount = edgeCount;
        this.approxSurfaceArea = approxSurfaceArea;
        this.approxEdgeLength = approxEdgeLength;
    }

    public int getFaceCount() {
        return faceCount;
    }
    public int getEdgeCount() {
        return edgeCount;
    }
    public double getApproxSurfaceArea() {
        return approxSurfaceArea;
    }
    public double getApproxEdgeLength() {
        return approxEdgeLength;
    }

    // Record-style accessors
    public int faceCount() { return faceCount; }
    public int edgeCount() { return edgeCount; }
    public double approxSurfaceArea() { return approxSurfaceArea; }
    public double approxEdgeLength() { return approxEdgeLength; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeometrySummary that = (GeometrySummary) o;
        return faceCount == that.faceCount && edgeCount == that.edgeCount && Double.compare(that.approxSurfaceArea, approxSurfaceArea) == 0 && Double.compare(that.approxEdgeLength, approxEdgeLength) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(faceCount, edgeCount, Double.hashCode(approxSurfaceArea), Double.hashCode(approxEdgeLength));
    }

    @Override
    public String toString() {
        return "GeometrySummary{faceCount=" + faceCount + ", edgeCount=" + edgeCount + ", approxSurfaceArea=" + approxSurfaceArea + ", approxEdgeLength=" + approxEdgeLength + "}";
    }
}

final class RepresentationMeshes {
    private final String name;
    private final List<FaceNode> faces;
    private final List<EdgeNode> edges;

    public RepresentationMeshes(String name, List<FaceNode> faces, List<EdgeNode> edges) {
        this.name = name;
        this.faces = PreviewPayloadCopies.copy(faces);
        this.edges = PreviewPayloadCopies.copy(edges);
    }

    public String getName() {
        return name;
    }
    public List<FaceNode> getFaces() {
        return faces;
    }
    public List<EdgeNode> getEdges() {
        return edges;
    }

    // Record-style accessors
    public String name() { return name; }
    public List<FaceNode> faces() { return faces; }
    public List<EdgeNode> edges() { return edges; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepresentationMeshes that = (RepresentationMeshes) o;
        return java.util.Objects.equals(name, that.name) && java.util.Objects.equals(faces, that.faces) && java.util.Objects.equals(edges, that.edges);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, faces, edges);
    }

    @Override
    public String toString() {
        return "RepresentationMeshes{name=" + name + ", faces=" + faces + ", edges=" + edges + "}";
    }
}

final class FaceNode {
    private final FacePayload face;
    private final int meshIndex;
    private final String name;

    public FaceNode(FacePayload face, int meshIndex, String name) {
        this.face = face;
        this.meshIndex = meshIndex;
        this.name = name;
    }

    public FacePayload getFace() {
        return face;
    }
    public int getMeshIndex() {
        return meshIndex;
    }
    public String getName() {
        return name;
    }

    // Record-style accessors
    public FacePayload face() { return face; }
    public int meshIndex() { return meshIndex; }
    public String name() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FaceNode that = (FaceNode) o;
        return java.util.Objects.equals(face, that.face) && meshIndex == that.meshIndex && java.util.Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(face, meshIndex, name);
    }

    @Override
    public String toString() {
        return "FaceNode{face=" + face + ", meshIndex=" + meshIndex + ", name=" + name + "}";
    }
}

final class EdgeNode {
    private final EdgePayload edge;
    private final int meshIndex;
    private final String name;

    public EdgeNode(EdgePayload edge, int meshIndex, String name) {
        this.edge = edge;
        this.meshIndex = meshIndex;
        this.name = name;
    }

    public EdgePayload getEdge() {
        return edge;
    }
    public int getMeshIndex() {
        return meshIndex;
    }
    public String getName() {
        return name;
    }

    // Record-style accessors
    public EdgePayload edge() { return edge; }
    public int meshIndex() { return meshIndex; }
    public String name() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeNode that = (EdgeNode) o;
        return java.util.Objects.equals(edge, that.edge) && meshIndex == that.meshIndex && java.util.Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(edge, meshIndex, name);
    }

    @Override
    public String toString() {
        return "EdgeNode{edge=" + edge + ", meshIndex=" + meshIndex + ", name=" + name + "}";
    }
}

final class FloatArrayData {
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

    public float[] getValues() {
        return values;
    }
    public int getCount() {
        return count;
    }
    public float[] getMin() {
        return min;
    }
    public float[] getMax() {
        return max;
    }

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

final class IntArrayData {
    private final int[] values;
    private final int count;

    public IntArrayData(int[] values, int count) {
        this.values = PreviewPayloadCopies.copy(values);
        this.count = count;
    }

    public int[] getValues() {
        return values;
    }
    public int getCount() {
        return count;
    }

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

final class IndexedTriangleMesh {
    private final FloatArrayData positions;
    private final FloatArrayData normals;
    private final IntArrayData indices;

    public IndexedTriangleMesh(FloatArrayData positions, FloatArrayData normals, IntArrayData indices) {
        this.positions = positions;
        this.normals = normals;
        this.indices = indices;
    }

    public FloatArrayData getPositions() {
        return positions;
    }
    public FloatArrayData getNormals() {
        return normals;
    }
    public IntArrayData getIndices() {
        return indices;
    }

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