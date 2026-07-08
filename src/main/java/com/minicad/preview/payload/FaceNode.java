package com.minicad.preview.payload;

/**
 * Face node for STEP preview export.
 * Extracted from PreviewMeshPayloadTypes.
 */
public final class FaceNode {
    private final FacePayload face;
    private final int meshIndex;
    private final String name;

    public FaceNode(FacePayload face, int meshIndex, String name) {
        this.face = face;
        this.meshIndex = meshIndex;
        this.name = name;
    }

    public FacePayload getFace() { return face; }
    public int getMeshIndex() { return meshIndex; }
    public String getName() { return name; }

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