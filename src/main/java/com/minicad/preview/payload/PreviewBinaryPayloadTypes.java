package com.minicad.preview.payload;

import com.minicad.common.MiniCadIssue;

import java.util.List;

/**
 * Binary payload types for STEP preview export.
 * Extracted from StepPreviewPayloadTypes to reduce file size.
 */
final class BinaryPreviewPayload {
    private final PreviewStats stats;
    private final BoundsPayload bounds;
    private final ValidationPayload validation;
    private final ProductMetadataExtractor.ProductMetadata product;
    private final UnitExtractor.UnitInfo units;
    private final List<PmiPayload> pmi;
    private final List<MiniCadIssue> issues;
    private final List<UnsupportedBooleanPayload> unsupportedBooleans;
    private final List<UnsupportedFacePayload> unsupportedFaces;
    private final List<BinaryEdgePayload> edges;
    private final List<BinaryFacePayload> faces;
    private final List<BinaryRepresentationPayload> representations;
    private final List<InstancePayload> instances;

    public BinaryPreviewPayload(PreviewStats stats, BoundsPayload bounds, ValidationPayload validation, ProductMetadataExtractor.ProductMetadata product, UnitExtractor.UnitInfo units, List<PmiPayload> pmi, List<MiniCadIssue> issues, List<UnsupportedBooleanPayload> unsupportedBooleans, List<UnsupportedFacePayload> unsupportedFaces, List<BinaryEdgePayload> edges, List<BinaryFacePayload> faces, List<BinaryRepresentationPayload> representations, List<InstancePayload> instances) {
        this.stats = stats;
        this.bounds = bounds;
        this.validation = validation;
        this.product = product;
        this.units = units;
        this.pmi = PreviewPayloadCopies.copy(pmi);
        this.issues = PreviewPayloadCopies.copy(issues);
        this.unsupportedBooleans = PreviewPayloadCopies.copy(unsupportedBooleans);
        this.unsupportedFaces = PreviewPayloadCopies.copy(unsupportedFaces);
        this.edges = PreviewPayloadCopies.copy(edges);
        this.faces = PreviewPayloadCopies.copy(faces);
        this.representations = PreviewPayloadCopies.copy(representations);
        this.instances = PreviewPayloadCopies.copy(instances);
    }

    public PreviewStats getStats() {
        return stats;
    }
    public BoundsPayload getBounds() {
        return bounds;
    }
    public ValidationPayload getValidation() {
        return validation;
    }
    public ProductMetadataExtractor.ProductMetadata getProduct() {
        return product;
    }
    public UnitExtractor.UnitInfo getUnits() {
        return units;
    }
    public List<PmiPayload> getPmi() {
        return pmi;
    }
    public List<MiniCadIssue> getIssues() {
        return issues;
    }
    public List<UnsupportedBooleanPayload> getUnsupportedBooleans() {
        return unsupportedBooleans;
    }
    public List<UnsupportedFacePayload> getUnsupportedFaces() {
        return unsupportedFaces;
    }
    public List<BinaryEdgePayload> getEdges() {
        return edges;
    }
    public List<BinaryFacePayload> getFaces() {
        return faces;
    }
    public List<BinaryRepresentationPayload> getRepresentations() {
        return representations;
    }
    public List<InstancePayload> getInstances() {
        return instances;
    }

    // Record-style accessors
    public PreviewStats stats() { return stats; }
    public BoundsPayload bounds() { return bounds; }
    public ValidationPayload validation() { return validation; }
    public ProductMetadataExtractor.ProductMetadata product() { return product; }
    public UnitExtractor.UnitInfo units() { return units; }
    public List<PmiPayload> pmi() { return pmi; }
    public List<MiniCadIssue> issues() { return issues; }
    public List<UnsupportedBooleanPayload> unsupportedBooleans() { return unsupportedBooleans; }
    public List<UnsupportedFacePayload> unsupportedFaces() { return unsupportedFaces; }
    public List<BinaryEdgePayload> edges() { return edges; }
    public List<BinaryFacePayload> faces() { return faces; }
    public List<BinaryRepresentationPayload> representations() { return representations; }
    public List<InstancePayload> instances() { return instances; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BinaryPreviewPayload)) return false;
        BinaryPreviewPayload that = (BinaryPreviewPayload) o;
        return java.util.Objects.equals(stats, that.stats) && java.util.Objects.equals(bounds, that.bounds) && java.util.Objects.equals(validation, that.validation) && java.util.Objects.equals(product, that.product) && java.util.Objects.equals(units, that.units) && java.util.Objects.equals(pmi, that.pmi) && java.util.Objects.equals(issues, that.issues) && java.util.Objects.equals(unsupportedBooleans, that.unsupportedBooleans) && java.util.Objects.equals(unsupportedFaces, that.unsupportedFaces) && java.util.Objects.equals(edges, that.edges) && java.util.Objects.equals(faces, that.faces) && java.util.Objects.equals(representations, that.representations) && java.util.Objects.equals(instances, that.instances);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(stats, bounds, validation, product, units, pmi, issues, unsupportedBooleans, unsupportedFaces, edges, faces, representations, instances);
    }

    @Override public String toString() {
        return "BinaryPreviewPayload{" + "stats=stats, bounds=bounds, validation=validation, product=product, units=units, pmi=pmi, issues=issues, unsupportedBooleans=unsupportedBooleans, unsupportedFaces=unsupportedFaces, edges=edges, faces=faces, representations=representations, instances=instances" + "}";
    }
}

final class BinaryRepresentationPayload {
    private final int id;
    private final String name;
    private final List<String> layers;
    private final ColorPayload color;
    private final List<BinaryEdgePayload> edges;
    private final List<BinaryFacePayload> faces;

    public BinaryRepresentationPayload(int id, String name, List<String> layers, ColorPayload color, List<BinaryEdgePayload> edges, List<BinaryFacePayload> faces) {
        this.id = id;
        this.name = name;
        this.layers = PreviewPayloadCopies.copy(layers);
        this.color = color;
        this.edges = PreviewPayloadCopies.copy(edges);
        this.faces = PreviewPayloadCopies.copy(faces);
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public List<String> getLayers() {
        return layers;
    }
    public ColorPayload getColor() {
        return color;
    }
    public List<BinaryEdgePayload> getEdges() {
        return edges;
    }
    public List<BinaryFacePayload> getFaces() {
        return faces;
    }

    // Record-style accessors
    public int id() { return id; }
    public String name() { return name; }
    public List<String> layers() { return layers; }
    public ColorPayload color() { return color; }
    public List<BinaryEdgePayload> edges() { return edges; }
    public List<BinaryFacePayload> faces() { return faces; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BinaryRepresentationPayload)) return false;
        BinaryRepresentationPayload that = (BinaryRepresentationPayload) o;
        return id == that.id && java.util.Objects.equals(name, that.name) && java.util.Objects.equals(layers, that.layers) && java.util.Objects.equals(color, that.color) && java.util.Objects.equals(edges, that.edges) && java.util.Objects.equals(faces, that.faces);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(id, name, layers, color, edges, faces);
    }

    @Override public String toString() {
        return "BinaryRepresentationPayload{" + "id=id, name=name, layers=layers, color=color, edges=edges, faces=faces" + "}";
    }
}

final class BinaryEdgePayload {
    private final int stepId;
    private final int pointOffset;
    private final int pointCount;
    private final EdgeCurvePayload curve;
    private final ColorPayload color;

    public BinaryEdgePayload(int stepId, int pointOffset, int pointCount, EdgeCurvePayload curve, ColorPayload color) {
        this.stepId = stepId;
        this.pointOffset = pointOffset;
        this.pointCount = pointCount;
        this.curve = curve;
        this.color = color;
    }

    public int getStepId() {
        return stepId;
    }
    public int getPointOffset() {
        return pointOffset;
    }
    public int getPointCount() {
        return pointCount;
    }
    public EdgeCurvePayload getCurve() {
        return curve;
    }
    public ColorPayload getColor() {
        return color;
    }

    // Record-style accessors
    public int stepId() { return stepId; }
    public int pointOffset() { return pointOffset; }
    public int pointCount() { return pointCount; }
    public EdgeCurvePayload curve() { return curve; }
    public ColorPayload color() { return color; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryEdgePayload that = (BinaryEdgePayload) o;
        return stepId == that.stepId && pointOffset == that.pointOffset && pointCount == that.pointCount && java.util.Objects.equals(curve, that.curve) && java.util.Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(stepId, pointOffset, pointCount, curve, color);
    }

    @Override
    public String toString() {
        return "BinaryEdgePayload{stepId=" + stepId + ", pointOffset=" + pointOffset + ", pointCount=" + pointCount + ", curve=" + curve + ", color=" + color + "}";
    }
}

final class BinaryFacePayload {
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

final class BinaryLoopPayload {
    private final boolean outer;
    private final int pointOffset;
    private final int pointCount;

    public BinaryLoopPayload(boolean outer, int pointOffset, int pointCount) {
        this.outer = outer;
        this.pointOffset = pointOffset;
        this.pointCount = pointCount;
    }

    public boolean getOuter() {
        return outer;
    }
    public int getPointOffset() {
        return pointOffset;
    }
    public int getPointCount() {
        return pointCount;
    }

    // Record-style accessors
    public boolean outer() { return outer; }
    public int pointOffset() { return pointOffset; }
    public int pointCount() { return pointCount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryLoopPayload that = (BinaryLoopPayload) o;
        return outer == that.outer && pointOffset == that.pointOffset && pointCount == that.pointCount;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(Boolean.hashCode(outer), pointOffset, pointCount);
    }

    @Override
    public String toString() {
        return "BinaryLoopPayload{outer=" + outer + ", pointOffset=" + pointOffset + ", pointCount=" + pointCount + "}";
    }
}