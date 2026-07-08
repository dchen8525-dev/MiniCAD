package com.minicad.preview.payload;

import java.util.List;

/**
 * Binary face payload for STEP preview export.
 */
public final class BinaryFacePayload {
    private final int stepId;
    private final String name;
    private final String surfaceType;
    private final PointPayload origin;
    private final VectorPayload normal;
    private final boolean sameSense;
    private final ColorPayload color;
    private final List<String> layers;
    private final FaceSurfacePayload surface;
    private final List<ParametricLoopPayload> uvLoops;
    private final List<BinaryLoopPayload> loops;
    private final int triangleOffset;
    private final int triangleCount;

    public BinaryFacePayload(int stepId, String name, String surfaceType, PointPayload origin, VectorPayload normal, boolean sameSense, ColorPayload color, List<String> layers, FaceSurfacePayload surface, List<ParametricLoopPayload> uvLoops, List<BinaryLoopPayload> loops, int triangleOffset, int triangleCount) {
        this.stepId = stepId;
        this.name = name;
        this.surfaceType = surfaceType;
        this.origin = origin;
        this.normal = normal;
        this.sameSense = sameSense;
        this.color = color;
        this.layers = PreviewPayloadCopies.copy(layers);
        this.surface = surface;
        this.uvLoops = PreviewPayloadCopies.copy(uvLoops);
        this.loops = PreviewPayloadCopies.copy(loops);
        this.triangleOffset = triangleOffset;
        this.triangleCount = triangleCount;
    }

    public int getStepId() {
        return stepId;
    }
    public String getName() {
        return name;
    }
    public String getSurfaceType() {
        return surfaceType;
    }
    public PointPayload getOrigin() {
        return origin;
    }
    public VectorPayload getNormal() {
        return normal;
    }
    public boolean getSameSense() {
        return sameSense;
    }
    public ColorPayload getColor() {
        return color;
    }
    public List<String> getLayers() {
        return layers;
    }
    public FaceSurfacePayload getSurface() {
        return surface;
    }
    public List<ParametricLoopPayload> getUvLoops() {
        return uvLoops;
    }
    public List<BinaryLoopPayload> getLoops() {
        return loops;
    }
    public int getTriangleOffset() {
        return triangleOffset;
    }
    public int getTriangleCount() {
        return triangleCount;
    }

    // Record-style accessors
    public int stepId() { return stepId; }
    public String name() { return name; }
    public String surfaceType() { return surfaceType; }
    public PointPayload origin() { return origin; }
    public VectorPayload normal() { return normal; }
    public boolean sameSense() { return sameSense; }
    public ColorPayload color() { return color; }
    public List<String> layers() { return layers; }
    public FaceSurfacePayload surface() { return surface; }
    public List<ParametricLoopPayload> uvLoops() { return uvLoops; }
    public List<BinaryLoopPayload> loops() { return loops; }
    public int triangleOffset() { return triangleOffset; }
    public int triangleCount() { return triangleCount; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BinaryFacePayload)) return false;
        BinaryFacePayload that = (BinaryFacePayload) o;
        return stepId == that.stepId && java.util.Objects.equals(name, that.name) && java.util.Objects.equals(surfaceType, that.surfaceType) && java.util.Objects.equals(origin, that.origin) && java.util.Objects.equals(normal, that.normal) && sameSense == that.sameSense && java.util.Objects.equals(color, that.color) && java.util.Objects.equals(layers, that.layers) && java.util.Objects.equals(surface, that.surface) && java.util.Objects.equals(uvLoops, that.uvLoops) && java.util.Objects.equals(loops, that.loops) && triangleOffset == that.triangleOffset && triangleCount == that.triangleCount;
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(stepId, name, surfaceType, origin, normal, sameSense, color, layers, surface, uvLoops, loops, triangleOffset, triangleCount);
    }

    @Override public String toString() {
        return "BinaryFacePayload{" + "stepId=stepId, name=name, surfaceType=surfaceType, origin=origin, normal=normal, sameSense=sameSense, color=color, layers=layers, surface=surface, uvLoops=uvLoops, loops=loops, triangleOffset=triangleOffset, triangleCount=triangleCount" + "}";
    }
}