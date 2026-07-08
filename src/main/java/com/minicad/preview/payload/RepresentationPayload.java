package com.minicad.preview.payload;

import java.util.List;

/**
 * Representation payload for assembly representation.
 */
public final class RepresentationPayload {
    private final int id;
    private final String name;
    private final List<String> layers;
    private final ColorPayload color;
    private final List<EdgePayload> edges;
    private final List<FacePayload> faces;

    public RepresentationPayload(int id, String name, List<String> layers, ColorPayload color, List<EdgePayload> edges, List<FacePayload> faces) {
        this.id = id;
        this.name = name;
        this.layers = PreviewPayloadCopies.copy(layers);
        this.color = color;
        this.edges = PreviewPayloadCopies.copy(edges);
        this.faces = PreviewPayloadCopies.copy(faces);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public List<String> getLayers() { return layers; }
    public ColorPayload getColor() { return color; }
    public List<EdgePayload> getEdges() { return edges; }
    public List<FacePayload> getFaces() { return faces; }

    // Record-style accessors
    public int id() { return id; }
    public String name() { return name; }
    public List<String> layers() { return layers; }
    public ColorPayload color() { return color; }
    public List<EdgePayload> edges() { return edges; }
    public List<FacePayload> faces() { return faces; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RepresentationPayload)) return false;
        RepresentationPayload that = (RepresentationPayload) o;
        return id == that.id && java.util.Objects.equals(name, that.name) && java.util.Objects.equals(layers, that.layers) && java.util.Objects.equals(color, that.color) && java.util.Objects.equals(edges, that.edges) && java.util.Objects.equals(faces, that.faces);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(id, name, layers, color, edges, faces);
    }

    @Override public String toString() {
        return "RepresentationPayload{" + "id=id, name=name, layers=layers, color=color, edges=edges, faces=faces" + "}";
    }
}