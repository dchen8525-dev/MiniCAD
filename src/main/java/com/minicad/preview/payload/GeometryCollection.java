package com.minicad.preview.payload;

import java.util.List;

/**
 * Geometry collection payload for STEP preview export.
 */
public final class GeometryCollection {
    private final List<EdgePayload> edges;
    private final List<FacePayload> faces;
    private final List<UnsupportedFacePayload> unsupportedFaces;

    public GeometryCollection(List<EdgePayload> edges, List<FacePayload> faces, List<UnsupportedFacePayload> unsupportedFaces) {
        this.edges = PreviewPayloadCopies.copy(edges);
        this.faces = PreviewPayloadCopies.copy(faces);
        this.unsupportedFaces = PreviewPayloadCopies.copy(unsupportedFaces);
    }

    public List<EdgePayload> getEdges() { return edges; }
    public List<FacePayload> getFaces() { return faces; }
    public List<UnsupportedFacePayload> getUnsupportedFaces() { return unsupportedFaces; }

    public List<EdgePayload> edges() { return edges; }
    public List<FacePayload> faces() { return faces; }
    public List<UnsupportedFacePayload> unsupportedFaces() { return unsupportedFaces; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeometryCollection)) return false;
        GeometryCollection that = (GeometryCollection) o;
        return java.util.Objects.equals(edges, that.edges) && java.util.Objects.equals(faces, that.faces) && java.util.Objects.equals(unsupportedFaces, that.unsupportedFaces);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(edges, faces, unsupportedFaces);
    }

    @Override public String toString() {
        return "GeometryCollection{" + "edges=edges, faces=faces, unsupportedFaces=unsupportedFaces" + "}";
    }
}