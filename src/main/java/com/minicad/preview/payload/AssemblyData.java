package com.minicad.preview.payload;

import java.util.List;

/**
 * Assembly data payload for STEP preview export.
 */
public final class AssemblyData {
    private final List<RepresentationPayload> representations;
    private final List<InstancePayload> instances;
    private final List<UnsupportedFacePayload> unsupportedFaces;
    private final GeometrySummary summary;
    private final BoundsPayload bounds;

    public AssemblyData(List<RepresentationPayload> representations, List<InstancePayload> instances, List<UnsupportedFacePayload> unsupportedFaces, GeometrySummary summary, BoundsPayload bounds) {
        this.representations = PreviewPayloadCopies.copy(representations);
        this.instances = PreviewPayloadCopies.copy(instances);
        this.unsupportedFaces = PreviewPayloadCopies.copy(unsupportedFaces);
        this.summary = summary;
        this.bounds = bounds;
    }

    public List<RepresentationPayload> getRepresentations() { return representations; }
    public List<InstancePayload> getInstances() { return instances; }
    public List<UnsupportedFacePayload> getUnsupportedFaces() { return unsupportedFaces; }
    public GeometrySummary getSummary() { return summary; }
    public BoundsPayload getBounds() { return bounds; }

    public List<RepresentationPayload> representations() { return representations; }
    public List<InstancePayload> instances() { return instances; }
    public List<UnsupportedFacePayload> unsupportedFaces() { return unsupportedFaces; }
    public GeometrySummary summary() { return summary; }
    public BoundsPayload bounds() { return bounds; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssemblyData)) return false;
        AssemblyData that = (AssemblyData) o;
        return java.util.Objects.equals(representations, that.representations) && java.util.Objects.equals(instances, that.instances) && java.util.Objects.equals(unsupportedFaces, that.unsupportedFaces) && java.util.Objects.equals(summary, that.summary) && java.util.Objects.equals(bounds, that.bounds);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(representations, instances, unsupportedFaces, summary, bounds);
    }

    @Override public String toString() {
        return "AssemblyData{" + "representations=representations, instances=instances, unsupportedFaces=unsupportedFaces, summary=summary, bounds=bounds" + "}";
    }
}
