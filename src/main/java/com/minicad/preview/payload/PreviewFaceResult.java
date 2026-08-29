package com.minicad.preview.payload;

/**
 * Preview face result payload for STEP preview export.
 */
public final class PreviewFaceResult {
    private final FacePayload face;
    private final UnsupportedFacePayload unsupportedFace;

    public PreviewFaceResult(FacePayload face, UnsupportedFacePayload unsupportedFace) {
        this.face = face;
        this.unsupportedFace = unsupportedFace;
    }

    public FacePayload getFace() { return face; }
    public UnsupportedFacePayload getUnsupportedFace() { return unsupportedFace; }

    public FacePayload face() { return face; }
    public UnsupportedFacePayload unsupportedFace() { return unsupportedFace; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PreviewFaceResult that = (PreviewFaceResult) o;
        return java.util.Objects.equals(face, that.face) && java.util.Objects.equals(unsupportedFace, that.unsupportedFace);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(face, unsupportedFace);
    }

    @Override
    public String toString() {
        return "PreviewFaceResult{face=" + face + ", unsupportedFace=" + unsupportedFace + "}";
    }
}
