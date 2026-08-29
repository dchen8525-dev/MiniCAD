package com.minicad.preview.payload;

import java.util.List;

/**
 * Face payload for face geometry representation.
 */
public final class FacePayload {
    private final int stepId;
    private final String name;
    private final String surfaceType;
    private final PointPayload origin;
    private final VectorPayload normal;
    private final boolean sameSense;
    private final ColorPayload color;
    private final double transparency;
    private final PbrPayload pbr;
    private final List<String> layers;
    private final List<LoopPayload> loops;
    private final List<PointPayload> triangles;
    private final FaceSurfacePayload surface;
    private final List<ParametricLoopPayload> uvLoops;

    public FacePayload(int stepId, String name, String surfaceType, PointPayload origin, VectorPayload normal, boolean sameSense, ColorPayload color, double transparency, PbrPayload pbr, List<String> layers, List<LoopPayload> loops, List<PointPayload> triangles, FaceSurfacePayload surface, List<ParametricLoopPayload> uvLoops) {
        this.stepId = stepId;
        this.name = name;
        this.surfaceType = surfaceType;
        this.origin = origin;
        this.normal = normal;
        this.sameSense = sameSense;
        this.color = color;
        this.transparency = transparency;
        this.pbr = pbr;
        this.layers = PreviewPayloadCopies.copy(layers);
        this.loops = PreviewPayloadCopies.copy(loops);
        this.triangles = PreviewPayloadCopies.copy(triangles);
        this.surface = surface;
        this.uvLoops = PreviewPayloadCopies.copy(uvLoops);
    }

    public int getStepId() { return stepId; }
    public String getName() { return name; }
    public String getSurfaceType() { return surfaceType; }
    public PointPayload getOrigin() { return origin; }
    public VectorPayload getNormal() { return normal; }
    public boolean getSameSense() { return sameSense; }
    public ColorPayload getColor() { return color; }
    public double getTransparency() { return transparency; }
    public PbrPayload getPbr() { return pbr; }
    public List<String> getLayers() { return layers; }
    public List<LoopPayload> getLoops() { return loops; }
    public List<PointPayload> getTriangles() { return triangles; }
    public FaceSurfacePayload getSurface() { return surface; }
    public List<ParametricLoopPayload> getUvLoops() { return uvLoops; }

    // Record-style accessors
    public int stepId() { return stepId; }
    public String name() { return name; }
    public String surfaceType() { return surfaceType; }
    public PointPayload origin() { return origin; }
    public VectorPayload normal() { return normal; }
    public boolean sameSense() { return sameSense; }
    public ColorPayload color() { return color; }
    public double transparency() { return transparency; }
    public PbrPayload pbr() { return pbr; }
    public List<String> layers() { return layers; }
    public List<LoopPayload> loops() { return loops; }
    public List<PointPayload> triangles() { return triangles; }
    public FaceSurfacePayload surface() { return surface; }
    public List<ParametricLoopPayload> uvLoops() { return uvLoops; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacePayload)) return false;
        FacePayload that = (FacePayload) o;
        return stepId == that.stepId && java.util.Objects.equals(name, that.name) && java.util.Objects.equals(surfaceType, that.surfaceType) && java.util.Objects.equals(origin, that.origin) && java.util.Objects.equals(normal, that.normal) && sameSense == that.sameSense && java.util.Objects.equals(color, that.color) && Double.compare(that.transparency, transparency) == 0 && java.util.Objects.equals(pbr, that.pbr) && java.util.Objects.equals(layers, that.layers) && java.util.Objects.equals(loops, that.loops) && java.util.Objects.equals(triangles, that.triangles) && java.util.Objects.equals(surface, that.surface) && java.util.Objects.equals(uvLoops, that.uvLoops);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(stepId, name, surfaceType, origin, normal, sameSense, color, Double.hashCode(transparency), pbr, layers, loops, triangles, surface, uvLoops);
    }

    @Override public String toString() {
        return "FacePayload{" + "stepId=stepId, name=name, surfaceType=surfaceType, origin=origin, normal=normal, sameSense=sameSense, color=color, transparency=transparency, pbr=pbr, layers=layers, loops=loops, triangles=triangles, surface=surface, uvLoops=uvLoops" + "}";
    }
}
