package com.minicad.preview.payload;

/**
 * Edge node for STEP preview export.
 * Extracted from PreviewMeshPayloadTypes.
 */
public final class EdgeNode {
    private final EdgePayload edge;
    private final int meshIndex;
    private final String name;

    public EdgeNode(EdgePayload edge, int meshIndex, String name) {
        this.edge = edge;
        this.meshIndex = meshIndex;
        this.name = name;
    }

    public EdgePayload getEdge() { return edge; }
    public int getMeshIndex() { return meshIndex; }
    public String getName() { return name; }

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