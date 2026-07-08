package com.minicad.preview.payload;

import java.util.List;

/**
 * Representation build result payload for STEP preview export.
 */
public final class RepresentationBuildResult {
    private final RepresentationPayload payload;
    private final List<UnsupportedFacePayload> unsupportedFaces;

    public RepresentationBuildResult(RepresentationPayload payload, List<UnsupportedFacePayload> unsupportedFaces) {
        this.payload = payload;
        this.unsupportedFaces = PreviewPayloadCopies.copy(unsupportedFaces);
    }

    public RepresentationPayload getPayload() { return payload; }
    public List<UnsupportedFacePayload> getUnsupportedFaces() { return unsupportedFaces; }

    public RepresentationPayload payload() { return payload; }
    public List<UnsupportedFacePayload> unsupportedFaces() { return unsupportedFaces; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RepresentationBuildResult)) return false;
        RepresentationBuildResult that = (RepresentationBuildResult) o;
        return java.util.Objects.equals(payload, that.payload) && java.util.Objects.equals(unsupportedFaces, that.unsupportedFaces);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(payload, unsupportedFaces);
    }

    @Override public String toString() {
        return "RepresentationBuildResult{" + "payload=payload, unsupportedFaces=unsupportedFaces" + "}";
    }
}