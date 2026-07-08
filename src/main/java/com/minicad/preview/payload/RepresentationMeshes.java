package com.minicad.preview.payload;

import java.util.List;

/**
 * Representation meshes for STEP preview export.
 * Extracted from PreviewMeshPayloadTypes.
 */
public final class RepresentationMeshes {
    private final String name;
    private final List<FaceNode> faces;
    private final List<EdgeNode> edges;

    public RepresentationMeshes(String name, List<FaceNode> faces, List<EdgeNode> edges) {
        this.name = name;
        this.faces = PreviewPayloadCopies.copy(faces);
        this.edges = PreviewPayloadCopies.copy(edges);
    }

    public String getName() { return name; }
    public List<FaceNode> getFaces() { return faces; }
    public List<EdgeNode> getEdges() { return edges; }

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