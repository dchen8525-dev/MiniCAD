package com.minicad.preview.payload;

import com.minicad.common.MiniCadIssue;
import com.minicad.helper.ProductMetadataExtractor;
import com.minicad.helper.UnitExtractor;
import com.minicad.preview.builder.PmiPayload;

import java.util.List;

/**
 * Main preview payload for STEP preview export.
 */
public final class PreviewPayload {
    private final PreviewStats stats;
    private final BoundsPayload bounds;
    private final ValidationPayload validation;
    private final ProductMetadataExtractor.ProductMetadata product;
    private final UnitExtractor.UnitInfo units;
    private final List<PmiPayload> pmi;
    private final List<MiniCadIssue> issues;
    private final List<UnsupportedBooleanPayload> unsupportedBooleans;
    private final List<UnsupportedFacePayload> unsupportedFaces;
    private final List<EdgePayload> edges;
    private final List<FacePayload> faces;
    private final List<RepresentationPayload> representations;
    private final List<InstancePayload> instances;

    public PreviewPayload(PreviewStats stats, BoundsPayload bounds, ValidationPayload validation, ProductMetadataExtractor.ProductMetadata product, UnitExtractor.UnitInfo units, List<PmiPayload> pmi, List<MiniCadIssue> issues, List<UnsupportedBooleanPayload> unsupportedBooleans, List<UnsupportedFacePayload> unsupportedFaces, List<EdgePayload> edges, List<FacePayload> faces, List<RepresentationPayload> representations, List<InstancePayload> instances) {
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

    public PreviewStats getStats() { return stats; }
    public BoundsPayload getBounds() { return bounds; }
    public ValidationPayload getValidation() { return validation; }
    public ProductMetadataExtractor.ProductMetadata getProduct() { return product; }
    public UnitExtractor.UnitInfo getUnits() { return units; }
    public List<PmiPayload> getPmi() { return pmi; }
    public List<MiniCadIssue> getIssues() { return issues; }
    public List<UnsupportedBooleanPayload> getUnsupportedBooleans() { return unsupportedBooleans; }
    public List<UnsupportedFacePayload> getUnsupportedFaces() { return unsupportedFaces; }
    public List<EdgePayload> getEdges() { return edges; }
    public List<FacePayload> getFaces() { return faces; }
    public List<RepresentationPayload> getRepresentations() { return representations; }
    public List<InstancePayload> getInstances() { return instances; }

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
    public List<EdgePayload> edges() { return edges; }
    public List<FacePayload> faces() { return faces; }
    public List<RepresentationPayload> representations() { return representations; }
    public List<InstancePayload> instances() { return instances; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreviewPayload)) return false;
        PreviewPayload that = (PreviewPayload) o;
        return java.util.Objects.equals(stats, that.stats) && java.util.Objects.equals(bounds, that.bounds) && java.util.Objects.equals(validation, that.validation) && java.util.Objects.equals(product, that.product) && java.util.Objects.equals(units, that.units) && java.util.Objects.equals(pmi, that.pmi) && java.util.Objects.equals(issues, that.issues) && java.util.Objects.equals(unsupportedBooleans, that.unsupportedBooleans) && java.util.Objects.equals(unsupportedFaces, that.unsupportedFaces) && java.util.Objects.equals(edges, that.edges) && java.util.Objects.equals(faces, that.faces) && java.util.Objects.equals(representations, that.representations) && java.util.Objects.equals(instances, that.instances);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(stats, bounds, validation, product, units, pmi, issues, unsupportedBooleans, unsupportedFaces, edges, faces, representations, instances);
    }

    @Override public String toString() {
        return "PreviewPayload{" + "stats=stats, bounds=bounds, validation=validation, product=product, units=units, pmi=pmi, issues=issues, unsupportedBooleans=unsupportedBooleans, unsupportedFaces=unsupportedFaces, edges=edges, faces=faces, representations=representations, instances=instances" + "}";
    }
}
