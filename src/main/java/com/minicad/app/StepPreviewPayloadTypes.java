package com.minicad.app;

import com.minicad.common.Epsilon;
import com.minicad.common.MiniCadIssue;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Vector3;

import java.util.List;
import java.util.stream.Collectors;

final class PreviewPayload {
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
    public List<EdgePayload> getEdges() {
        return edges;
    }
    public List<FacePayload> getFaces() {
        return faces;
    }
    public List<RepresentationPayload> getRepresentations() {
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

final class AssemblyData {
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

    public List<RepresentationPayload> getRepresentations() {
        return representations;
    }
    public List<InstancePayload> getInstances() {
        return instances;
    }
    public List<UnsupportedFacePayload> getUnsupportedFaces() {
        return unsupportedFaces;
    }
    public GeometrySummary getSummary() {
        return summary;
    }
    public BoundsPayload getBounds() {
        return bounds;
    }

    // Record-style accessors
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

final class AssemblyMetrics {
    private final GeometrySummary summary;
    private final BoundsPayload bounds;

    public AssemblyMetrics(GeometrySummary summary, BoundsPayload bounds) {
        this.summary = summary;
        this.bounds = bounds;
    }

    public GeometrySummary getSummary() {
        return summary;
    }
    public BoundsPayload getBounds() {
        return bounds;
    }

    // Record-style accessors
    public GeometrySummary summary() { return summary; }
    public BoundsPayload bounds() { return bounds; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssemblyMetrics)) return false;
        AssemblyMetrics that = (AssemblyMetrics) o;
        return java.util.Objects.equals(summary, that.summary) && java.util.Objects.equals(bounds, that.bounds);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(summary, bounds);
    }

    @Override public String toString() {
        return "AssemblyMetrics{" + "summary=summary, bounds=bounds" + "}";
    }
}

final class GeometryCollection {
    private final List<EdgePayload> edges;
    private final List<FacePayload> faces;
    private final List<UnsupportedFacePayload> unsupportedFaces;

    public GeometryCollection(List<EdgePayload> edges, List<FacePayload> faces, List<UnsupportedFacePayload> unsupportedFaces) {
        this.edges = PreviewPayloadCopies.copy(edges);
        this.faces = PreviewPayloadCopies.copy(faces);
        this.unsupportedFaces = PreviewPayloadCopies.copy(unsupportedFaces);
    }

    public List<EdgePayload> getEdges() {
        return edges;
    }
    public List<FacePayload> getFaces() {
        return faces;
    }
    public List<UnsupportedFacePayload> getUnsupportedFaces() {
        return unsupportedFaces;
    }

    // Record-style accessors
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

final class RepresentationBuildResult {
    private final RepresentationPayload payload;
    private final List<UnsupportedFacePayload> unsupportedFaces;

    public RepresentationBuildResult(RepresentationPayload payload, List<UnsupportedFacePayload> unsupportedFaces) {
        this.payload = payload;
        this.unsupportedFaces = PreviewPayloadCopies.copy(unsupportedFaces);
    }

    public RepresentationPayload getPayload() {
        return payload;
    }
    public List<UnsupportedFacePayload> getUnsupportedFaces() {
        return unsupportedFaces;
    }

    // Record-style accessors
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

final class PreviewStats {
    private final int entityCount;
    private final int solidCount;
    private final int shellCount;
    private final int faceCount;
    private final int edgeCount;
    private final int unsupportedFaceCount;
    private final int unsupportedBooleanCount;

    public PreviewStats(int entityCount, int solidCount, int shellCount, int faceCount, int edgeCount, int unsupportedFaceCount, int unsupportedBooleanCount) {
        this.entityCount = entityCount;
        this.solidCount = solidCount;
        this.shellCount = shellCount;
        this.faceCount = faceCount;
        this.edgeCount = edgeCount;
        this.unsupportedFaceCount = unsupportedFaceCount;
        this.unsupportedBooleanCount = unsupportedBooleanCount;
    }

    public int getEntityCount() {
        return entityCount;
    }
    public int getSolidCount() {
        return solidCount;
    }
    public int getShellCount() {
        return shellCount;
    }
    public int getFaceCount() {
        return faceCount;
    }
    public int getEdgeCount() {
        return edgeCount;
    }
    public int getUnsupportedFaceCount() {
        return unsupportedFaceCount;
    }
    public int getUnsupportedBooleanCount() {
        return unsupportedBooleanCount;
    }

    // Record-style accessors
    public int entityCount() { return entityCount; }
    public int solidCount() { return solidCount; }
    public int shellCount() { return shellCount; }
    public int faceCount() { return faceCount; }
    public int edgeCount() { return edgeCount; }
    public int unsupportedFaceCount() { return unsupportedFaceCount; }
    public int unsupportedBooleanCount() { return unsupportedBooleanCount; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreviewStats)) return false;
        PreviewStats that = (PreviewStats) o;
        return entityCount == that.entityCount && solidCount == that.solidCount && shellCount == that.shellCount && faceCount == that.faceCount && edgeCount == that.edgeCount && unsupportedFaceCount == that.unsupportedFaceCount && unsupportedBooleanCount == that.unsupportedBooleanCount;
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(entityCount, solidCount, shellCount, faceCount, edgeCount, unsupportedFaceCount, unsupportedBooleanCount);
    }

    @Override public String toString() {
        return "PreviewStats{" + "entityCount=entityCount, solidCount=solidCount, shellCount=shellCount, faceCount=faceCount, edgeCount=edgeCount, unsupportedFaceCount=unsupportedFaceCount, unsupportedBooleanCount=unsupportedBooleanCount" + "}";
    }
}

final class BoundsPayload {
    private final PointPayload min;
    private final PointPayload max;

    public BoundsPayload(PointPayload min, PointPayload max) {
        this.min = min;
        this.max = max;
    }

    public PointPayload getMin() {
        return min;
    }
    public PointPayload getMax() {
        return max;
    }

    // Record-style accessors
    public PointPayload min() { return min; }
    public PointPayload max() { return max; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoundsPayload that = (BoundsPayload) o;
        return java.util.Objects.equals(min, that.min) && java.util.Objects.equals(max, that.max);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(min, max);
    }

    @Override
    public String toString() {
        return "BoundsPayload{min=" + min + ", max=" + max + "}";
    }
}

final class ValidationPayload {
    private final int representationCount;
    private final int instanceCount;
    private final int renderedFaceCount;
    private final int renderedEdgeCount;
    private final double approxSurfaceArea;
    private final double approxEdgeLength;
    private final PointPayload center;
    private final ValidationReportPayload report;

    public ValidationPayload(int representationCount, int instanceCount, int renderedFaceCount, int renderedEdgeCount, double approxSurfaceArea, double approxEdgeLength, PointPayload center, ValidationReportPayload report) {
        this.representationCount = representationCount;
        this.instanceCount = instanceCount;
        this.renderedFaceCount = renderedFaceCount;
        this.renderedEdgeCount = renderedEdgeCount;
        this.approxSurfaceArea = approxSurfaceArea;
        this.approxEdgeLength = approxEdgeLength;
        this.center = center;
        this.report = report;
    }

    public int getRepresentationCount() {
        return representationCount;
    }
    public int getInstanceCount() {
        return instanceCount;
    }
    public int getRenderedFaceCount() {
        return renderedFaceCount;
    }
    public int getRenderedEdgeCount() {
        return renderedEdgeCount;
    }
    public double getApproxSurfaceArea() {
        return approxSurfaceArea;
    }
    public double getApproxEdgeLength() {
        return approxEdgeLength;
    }
    public PointPayload getCenter() {
        return center;
    }
    public ValidationReportPayload getReport() {
        return report;
    }

    // Record-style accessors
    public int representationCount() { return representationCount; }
    public int instanceCount() { return instanceCount; }
    public int renderedFaceCount() { return renderedFaceCount; }
    public int renderedEdgeCount() { return renderedEdgeCount; }
    public double approxSurfaceArea() { return approxSurfaceArea; }
    public double approxEdgeLength() { return approxEdgeLength; }
    public PointPayload center() { return center; }
    public ValidationReportPayload report() { return report; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidationPayload)) return false;
        ValidationPayload that = (ValidationPayload) o;
        return representationCount == that.representationCount && instanceCount == that.instanceCount && renderedFaceCount == that.renderedFaceCount && renderedEdgeCount == that.renderedEdgeCount && Double.compare(that.approxSurfaceArea, approxSurfaceArea) == 0 && Double.compare(that.approxEdgeLength, approxEdgeLength) == 0 && java.util.Objects.equals(center, that.center) && java.util.Objects.equals(report, that.report);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(representationCount, instanceCount, renderedFaceCount, renderedEdgeCount, Double.hashCode(approxSurfaceArea), Double.hashCode(approxEdgeLength), center, report);
    }

    @Override public String toString() {
        return "ValidationPayload{" + "representationCount=representationCount, instanceCount=instanceCount, renderedFaceCount=renderedFaceCount, renderedEdgeCount=renderedEdgeCount, approxSurfaceArea=approxSurfaceArea, approxEdgeLength=approxEdgeLength, center=center, report=report" + "}";
    }
}

final class ValidationReportPayload {
    private final String status;
    private final int okCount;
    private final int warnCount;
    private final List<ValidationCheckPayload> checks;

    public ValidationReportPayload(String status, int okCount, int warnCount, List<ValidationCheckPayload> checks) {
        this.status = status;
        this.okCount = okCount;
        this.warnCount = warnCount;
        this.checks = PreviewPayloadCopies.copy(checks);
    }

    public String getStatus() {
        return status;
    }
    public int getOkCount() {
        return okCount;
    }
    public int getWarnCount() {
        return warnCount;
    }
    public List<ValidationCheckPayload> getChecks() {
        return checks;
    }

    // Record-style accessors
    public String status() { return status; }
    public int okCount() { return okCount; }
    public int warnCount() { return warnCount; }
    public List<ValidationCheckPayload> checks() { return checks; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidationReportPayload)) return false;
        ValidationReportPayload that = (ValidationReportPayload) o;
        return java.util.Objects.equals(status, that.status) && okCount == that.okCount && warnCount == that.warnCount && java.util.Objects.equals(checks, that.checks);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(status, okCount, warnCount, checks);
    }

    @Override public String toString() {
        return "ValidationReportPayload{" + "status=status, okCount=okCount, warnCount=warnCount, checks=checks" + "}";
    }
}

final class ValidationCheckPayload {
    private final String propertyId;
    private final String name;
    private final String measureType;
    private final double expected;
    private final double actual;
    private final double delta;
    private final String status;
    private final boolean matches;

    public ValidationCheckPayload(String propertyId, String name, String measureType, double expected, double actual, double delta, String status, boolean matches) {
        this.propertyId = propertyId;
        this.name = name;
        this.measureType = measureType;
        this.expected = expected;
        this.actual = actual;
        this.delta = delta;
        this.status = status;
        this.matches = matches;
    }

    public String getPropertyId() {
        return propertyId;
    }
    public String getName() {
        return name;
    }
    public String getMeasureType() {
        return measureType;
    }
    public double getExpected() {
        return expected;
    }
    public double getActual() {
        return actual;
    }
    public double getDelta() {
        return delta;
    }
    public String getStatus() {
        return status;
    }
    public boolean getMatches() {
        return matches;
    }

    // Record-style accessors
    public String propertyId() { return propertyId; }
    public String name() { return name; }
    public String measureType() { return measureType; }
    public double expected() { return expected; }
    public double actual() { return actual; }
    public double delta() { return delta; }
    public String status() { return status; }
    public boolean matches() { return matches; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidationCheckPayload)) return false;
        ValidationCheckPayload that = (ValidationCheckPayload) o;
        return java.util.Objects.equals(propertyId, that.propertyId) && java.util.Objects.equals(name, that.name) && java.util.Objects.equals(measureType, that.measureType) && Double.compare(that.expected, expected) == 0 && Double.compare(that.actual, actual) == 0 && Double.compare(that.delta, delta) == 0 && java.util.Objects.equals(status, that.status) && matches == that.matches;
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(propertyId, name, measureType, Double.hashCode(expected), Double.hashCode(actual), Double.hashCode(delta), status, matches);
    }

    @Override public String toString() {
        return "ValidationCheckPayload{" + "propertyId=propertyId, name=name, measureType=measureType, expected=expected, actual=actual, delta=delta, status=status, matches=matches" + "}";
    }
}

final class RepresentationPayload {
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
    public List<EdgePayload> getEdges() {
        return edges;
    }
    public List<FacePayload> getFaces() {
        return faces;
    }

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

final class InstancePayload {
    private final String id;
    private final String parentId;
    private final int productDefinitionId;
    private final Integer occurrenceId;
    private final Integer representationId;
    private final List<Integer> representationIds;
    private final String label;
    private final String description;
    private final double[] localMatrix;
    private final double[] worldMatrix;
    private final int depth;

    public InstancePayload(String id, String parentId, int productDefinitionId, Integer occurrenceId, Integer representationId, List<Integer> representationIds, String label, String description, double[] localMatrix, double[] worldMatrix, int depth) {
        this.id = id;
        this.parentId = parentId;
        this.productDefinitionId = productDefinitionId;
        this.occurrenceId = occurrenceId;
        this.representationId = representationId;
        this.representationIds = PreviewPayloadCopies.copy(representationIds);
        this.label = label;
        this.description = description;
        this.localMatrix = PreviewPayloadCopies.copy(localMatrix);
        this.worldMatrix = PreviewPayloadCopies.copy(worldMatrix);
        this.depth = depth;
    }

    public String getId() {
        return id;
    }
    public String getParentId() {
        return parentId;
    }
    public int getProductDefinitionId() {
        return productDefinitionId;
    }
    public Integer getOccurrenceId() {
        return occurrenceId;
    }
    public Integer getRepresentationId() {
        return representationId;
    }
    public List<Integer> getRepresentationIds() {
        return representationIds;
    }
    public String getLabel() {
        return label;
    }
    public String getDescription() {
        return description;
    }
    public double[] getLocalMatrix() {
        return localMatrix;
    }
    public double[] getWorldMatrix() {
        return worldMatrix;
    }
    public int getDepth() {
        return depth;
    }

    // Record-style accessors
    public String id() { return id; }
    public String parentId() { return parentId; }
    public int productDefinitionId() { return productDefinitionId; }
    public Integer occurrenceId() { return occurrenceId; }
    public Integer representationId() { return representationId; }
    public List<Integer> representationIds() { return representationIds; }
    public String label() { return label; }
    public String description() { return description; }
    public double[] localMatrix() { return localMatrix; }
    public double[] worldMatrix() { return worldMatrix; }
    public int depth() { return depth; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstancePayload)) return false;
        InstancePayload that = (InstancePayload) o;
        return java.util.Objects.equals(id, that.id) && java.util.Objects.equals(parentId, that.parentId) && productDefinitionId == that.productDefinitionId && java.util.Objects.equals(occurrenceId, that.occurrenceId) && java.util.Objects.equals(representationId, that.representationId) && java.util.Objects.equals(representationIds, that.representationIds) && java.util.Objects.equals(label, that.label) && java.util.Objects.equals(description, that.description) && java.util.Arrays.equals(localMatrix, that.localMatrix) && java.util.Arrays.equals(worldMatrix, that.worldMatrix) && depth == that.depth;
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(id, parentId, productDefinitionId, occurrenceId, representationId, representationIds, label, description, java.util.Arrays.hashCode(localMatrix), java.util.Arrays.hashCode(worldMatrix), depth);
    }

    @Override public String toString() {
        return "InstancePayload{" + "id=id, parentId=parentId, productDefinitionId=productDefinitionId, occurrenceId=occurrenceId, representationId=representationId, representationIds=representationIds, label=label, description=description, localMatrix=java.util.Arrays.toString(localMatrix), worldMatrix=java.util.Arrays.toString(worldMatrix), depth=depth" + "}";
    }
}

final class PointRange {
    private final int offset;
    private final int count;

    public PointRange(int offset, int count) {
        this.offset = offset;
        this.count = count;
    }

    public int getOffset() {
        return offset;
    }
    public int getCount() {
        return count;
    }

    // Record-style accessors
    public int offset() { return offset; }
    public int count() { return count; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointRange that = (PointRange) o;
        return offset == that.offset && count == that.count;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(offset, count);
    }

    @Override
    public String toString() {
        return "PointRange{offset=" + offset + ", count=" + count + "}";
    }
}

final class UvPoint {
    private final double u;
    private final double v;

    public UvPoint(double u, double v) {
        this.u = u;
        this.v = v;
    }

    public double getU() {
        return u;
    }
    public double getV() {
        return v;
    }

    // Record-style accessors
    public double u() { return u; }
    public double v() { return v; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UvPoint that = (UvPoint) o;
        return Double.compare(that.u, u) == 0 && Double.compare(that.v, v) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(Double.hashCode(u), Double.hashCode(v));
    }

    @Override
    public String toString() {
        return "UvPoint{u=" + u + ", v=" + v + "}";
    }
}

final class ParametricLoopPayload {
    private final boolean outer;
    private final List<UvPoint> points;

    public ParametricLoopPayload(boolean outer, List<UvPoint> points) {
        this.outer = outer;
        this.points = PreviewPayloadCopies.copy(points);
    }

    public boolean getOuter() {
        return outer;
    }
    public List<UvPoint> getPoints() {
        return points;
    }

    // Record-style accessors
    public boolean outer() { return outer; }
    public List<UvPoint> points() { return points; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParametricLoopPayload that = (ParametricLoopPayload) o;
        return outer == that.outer && java.util.Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(Boolean.hashCode(outer), points);
    }

    @Override
    public String toString() {
        return "ParametricLoopPayload{outer=" + outer + ", points=" + points + "}";
    }
}

final class UvBounds {
    private final double minU;
    private final double minV;
    private final double maxU;
    private final double maxV;

    public UvBounds(double minU, double minV, double maxU, double maxV) {
        this.minU = minU;
        this.minV = minV;
        this.maxU = maxU;
        this.maxV = maxV;
    }

    public double getMinU() {
        return minU;
    }
    public double getMinV() {
        return minV;
    }
    public double getMaxU() {
        return maxU;
    }
    public double getMaxV() {
        return maxV;
    }

    // Record-style accessors
    public double minU() { return minU; }
    public double minV() { return minV; }
    public double maxU() { return maxU; }
    public double maxV() { return maxV; }
    public double uSpan() { return maxU - minU; }
    public double vSpan() { return maxV - minV; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UvBounds that = (UvBounds) o;
        return Double.compare(that.minU, minU) == 0 && Double.compare(that.minV, minV) == 0 && Double.compare(that.maxU, maxU) == 0 && Double.compare(that.maxV, maxV) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(Double.hashCode(minU), Double.hashCode(minV), Double.hashCode(maxU), Double.hashCode(maxV));
    }

    @Override
    public String toString() {
        return "UvBounds{minU=" + minU + ", minV=" + minV + ", maxU=" + maxU + ", maxV=" + maxV + "}";
    }
}

final class EdgePayload {
    private final int stepId;
    private final List<PointPayload> points;
    private final EdgeCurvePayload curve;
    private final ColorPayload color;

    public EdgePayload(int stepId, List<PointPayload> points, EdgeCurvePayload curve, ColorPayload color) {
        this.stepId = stepId;
        this.points = PreviewPayloadCopies.copy(points);
        this.curve = curve;
        this.color = color;
    }

    public int getStepId() {
        return stepId;
    }
    public List<PointPayload> getPoints() {
        return points;
    }
    public EdgeCurvePayload getCurve() {
        return curve;
    }
    public ColorPayload getColor() {
        return color;
    }

    // Record-style accessors
    public int stepId() { return stepId; }
    public List<PointPayload> points() { return points; }
    public EdgeCurvePayload curve() { return curve; }
    public ColorPayload color() { return color; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgePayload that = (EdgePayload) o;
        return stepId == that.stepId && java.util.Objects.equals(points, that.points) && java.util.Objects.equals(curve, that.curve) && java.util.Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(stepId, points, curve, color);
    }

    @Override
    public String toString() {
        return "EdgePayload{stepId=" + stepId + ", points=" + points + ", curve=" + curve + ", color=" + color + "}";
    }
}

final class EdgeCurvePayload {
    private final int stepId;
    private final String type;
    private final String basisType;
    private final Integer basisStepId;
    private final List<Double> center;
    private final List<Double> axis;
    private final List<Double> xDirection;
    private final Double radius;
    private final Double semiAxis1;
    private final Double semiAxis2;
    private final Boolean orientation;
    private final Boolean senseAgreement;
    private final Double offsetDistance;
    private final Boolean selfIntersect;
    private final List<Double> refDirection;
    private final Double transformScale;
    private final String masterRepresentation;
    private final List<String> associatedSurfaceTypes;
    private final List<Integer> associatedSurfaceStepIds;
    private final String sourceType;
    private final Integer sourceStepId;
    private final double startAngle;
    private final double sweepAngle;

    public EdgeCurvePayload(int stepId, String type, String basisType, Integer basisStepId, List<Double> center, List<Double> axis, List<Double> xDirection, Double radius, Double semiAxis1, Double semiAxis2, Boolean orientation, Boolean senseAgreement, Double offsetDistance, Boolean selfIntersect, List<Double> refDirection, Double transformScale, String masterRepresentation, List<String> associatedSurfaceTypes, List<Integer> associatedSurfaceStepIds, String sourceType, Integer sourceStepId, double startAngle, double sweepAngle) {
        this.stepId = stepId;
        this.type = type;
        this.basisType = basisType;
        this.basisStepId = basisStepId;
        this.center = PreviewPayloadCopies.copy(center);
        this.axis = PreviewPayloadCopies.copy(axis);
        this.xDirection = PreviewPayloadCopies.copy(xDirection);
        this.radius = radius;
        this.semiAxis1 = semiAxis1;
        this.semiAxis2 = semiAxis2;
        this.orientation = orientation;
        this.senseAgreement = senseAgreement;
        this.offsetDistance = offsetDistance;
        this.selfIntersect = selfIntersect;
        this.refDirection = PreviewPayloadCopies.copy(refDirection);
        this.transformScale = transformScale;
        this.masterRepresentation = masterRepresentation;
        this.associatedSurfaceTypes = PreviewPayloadCopies.copy(associatedSurfaceTypes);
        this.associatedSurfaceStepIds = PreviewPayloadCopies.copy(associatedSurfaceStepIds);
        this.sourceType = sourceType;
        this.sourceStepId = sourceStepId;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
    }

    public int getStepId() {
        return stepId;
    }
    public String getType() {
        return type;
    }
    public String getBasisType() {
        return basisType;
    }
    public Integer getBasisStepId() {
        return basisStepId;
    }
    public List<Double> getCenter() {
        return center;
    }
    public List<Double> getAxis() {
        return axis;
    }
    public List<Double> getXDirection() {
        return xDirection;
    }
    public Double getRadius() {
        return radius;
    }
    public Double getSemiAxis1() {
        return semiAxis1;
    }
    public Double getSemiAxis2() {
        return semiAxis2;
    }
    public Boolean getOrientation() {
        return orientation;
    }
    public Boolean getSenseAgreement() {
        return senseAgreement;
    }
    public Double getOffsetDistance() {
        return offsetDistance;
    }
    public Boolean getSelfIntersect() {
        return selfIntersect;
    }
    public List<Double> getRefDirection() {
        return refDirection;
    }
    public Double getTransformScale() {
        return transformScale;
    }
    public String getMasterRepresentation() {
        return masterRepresentation;
    }
    public List<String> getAssociatedSurfaceTypes() {
        return associatedSurfaceTypes;
    }
    public List<Integer> getAssociatedSurfaceStepIds() {
        return associatedSurfaceStepIds;
    }
    public String getSourceType() {
        return sourceType;
    }
    public Integer getSourceStepId() {
        return sourceStepId;
    }
    public double getStartAngle() {
        return startAngle;
    }
    public double getSweepAngle() {
        return sweepAngle;
    }

    // Record-style accessors
    public int stepId() { return stepId; }
    public String type() { return type; }
    public String basisType() { return basisType; }
    public Integer basisStepId() { return basisStepId; }
    public List<Double> center() { return center; }
    public List<Double> axis() { return axis; }
    public List<Double> xDirection() { return xDirection; }
    public Double radius() { return radius; }
    public Double semiAxis1() { return semiAxis1; }
    public Double semiAxis2() { return semiAxis2; }
    public Boolean orientation() { return orientation; }
    public Boolean senseAgreement() { return senseAgreement; }
    public Double offsetDistance() { return offsetDistance; }
    public Boolean selfIntersect() { return selfIntersect; }
    public List<Double> refDirection() { return refDirection; }
    public Double transformScale() { return transformScale; }
    public String masterRepresentation() { return masterRepresentation; }
    public List<String> associatedSurfaceTypes() { return associatedSurfaceTypes; }
    public List<Integer> associatedSurfaceStepIds() { return associatedSurfaceStepIds; }
    public String sourceType() { return sourceType; }
    public Integer sourceStepId() { return sourceStepId; }
    public double startAngle() { return startAngle; }
    public double sweepAngle() { return sweepAngle; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdgeCurvePayload)) return false;
        EdgeCurvePayload that = (EdgeCurvePayload) o;
        return stepId == that.stepId && java.util.Objects.equals(type, that.type) && java.util.Objects.equals(basisType, that.basisType) && java.util.Objects.equals(basisStepId, that.basisStepId) && java.util.Objects.equals(center, that.center) && java.util.Objects.equals(axis, that.axis) && java.util.Objects.equals(xDirection, that.xDirection) && java.util.Objects.equals(radius, that.radius) && java.util.Objects.equals(semiAxis1, that.semiAxis1) && java.util.Objects.equals(semiAxis2, that.semiAxis2) && java.util.Objects.equals(orientation, that.orientation) && java.util.Objects.equals(senseAgreement, that.senseAgreement) && java.util.Objects.equals(offsetDistance, that.offsetDistance) && java.util.Objects.equals(selfIntersect, that.selfIntersect) && java.util.Objects.equals(refDirection, that.refDirection) && java.util.Objects.equals(transformScale, that.transformScale) && java.util.Objects.equals(masterRepresentation, that.masterRepresentation) && java.util.Objects.equals(associatedSurfaceTypes, that.associatedSurfaceTypes) && java.util.Objects.equals(associatedSurfaceStepIds, that.associatedSurfaceStepIds) && java.util.Objects.equals(sourceType, that.sourceType) && java.util.Objects.equals(sourceStepId, that.sourceStepId) && Double.compare(that.startAngle, startAngle) == 0 && Double.compare(that.sweepAngle, sweepAngle) == 0;
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(stepId, type, basisType, basisStepId, center, axis, xDirection, radius, semiAxis1, semiAxis2, orientation, senseAgreement, offsetDistance, selfIntersect, refDirection, transformScale, masterRepresentation, associatedSurfaceTypes, associatedSurfaceStepIds, sourceType, sourceStepId, Double.hashCode(startAngle), Double.hashCode(sweepAngle));
    }

    @Override public String toString() {
        return "EdgeCurvePayload{" + "stepId=stepId, type=type, basisType=basisType, basisStepId=basisStepId, center=center, axis=axis, xDirection=xDirection, radius=radius, semiAxis1=semiAxis1, semiAxis2=semiAxis2, orientation=orientation, senseAgreement=senseAgreement, offsetDistance=offsetDistance, selfIntersect=selfIntersect, refDirection=refDirection, transformScale=transformScale, masterRepresentation=masterRepresentation, associatedSurfaceTypes=associatedSurfaceTypes, associatedSurfaceStepIds=associatedSurfaceStepIds, sourceType=sourceType, sourceStepId=sourceStepId, startAngle=startAngle, sweepAngle=sweepAngle" + "}";
    }
}

final class FaceSurfacePayload {
    private final String type;
    private final List<Double> center;
    private final List<Double> axis;
    private final List<Double> xDirection;
    private final double radius;
    private final Double minorRadius;
    private final Double semiAngle;
    private final double lowerHeight;
    private final double upperHeight;
    private final double startAngle;
    private final double sweepAngle;
    private final Integer uDegree;
    private final Integer vDegree;
    private final List<List<List<Double>>> controlPoints;
    private final List<Integer> uMultiplicities;
    private final List<Integer> vMultiplicities;
    private final List<Double> uKnots;
    private final List<Double> vKnots;
    private final String sourceType;
    private final Integer sourceStepId;
    private final String basisType;
    private final Integer basisStepId;
    private final Boolean orientation;
    private final Double offsetDistance;
    private final Double trimU1;
    private final Double trimU2;
    private final Double trimV1;
    private final Double trimV2;
    private final Boolean implicitOuter;
    private final Double transformScale;

    public FaceSurfacePayload(String type, List<Double> center, List<Double> axis, List<Double> xDirection, double radius, Double minorRadius, Double semiAngle, double lowerHeight, double upperHeight, double startAngle, double sweepAngle, Integer uDegree, Integer vDegree, List<List<List<Double>>> controlPoints, List<Integer> uMultiplicities, List<Integer> vMultiplicities, List<Double> uKnots, List<Double> vKnots, String sourceType, Integer sourceStepId, String basisType, Integer basisStepId, Boolean orientation, Double offsetDistance, Double trimU1, Double trimU2, Double trimV1, Double trimV2, Boolean implicitOuter, Double transformScale) {
        this.type = type;
        this.center = PreviewPayloadCopies.copy(center);
        this.axis = PreviewPayloadCopies.copy(axis);
        this.xDirection = PreviewPayloadCopies.copy(xDirection);
        this.radius = radius;
        this.minorRadius = minorRadius;
        this.semiAngle = semiAngle;
        this.lowerHeight = lowerHeight;
        this.upperHeight = upperHeight;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
        this.uDegree = uDegree;
        this.vDegree = vDegree;
        this.controlPoints = PreviewPayloadCopies.copyControlPoints(controlPoints);
        this.uMultiplicities = PreviewPayloadCopies.copy(uMultiplicities);
        this.vMultiplicities = PreviewPayloadCopies.copy(vMultiplicities);
        this.uKnots = PreviewPayloadCopies.copy(uKnots);
        this.vKnots = PreviewPayloadCopies.copy(vKnots);
        this.sourceType = sourceType;
        this.sourceStepId = sourceStepId;
        this.basisType = basisType;
        this.basisStepId = basisStepId;
        this.orientation = orientation;
        this.offsetDistance = offsetDistance;
        this.trimU1 = trimU1;
        this.trimU2 = trimU2;
        this.trimV1 = trimV1;
        this.trimV2 = trimV2;
        this.implicitOuter = implicitOuter;
        this.transformScale = transformScale;
    }

    // Convenience constructor for basic surface types with 17 parameters
    public FaceSurfacePayload(String type, List<Double> center, List<Double> axis, List<Double> xDirection, double radius, Double minorRadius, Double semiAngle, double lowerHeight, double upperHeight, double startAngle, double sweepAngle, Integer uDegree, Integer vDegree, List<List<List<Double>>> controlPoints, List<Integer> uMultiplicities, List<Integer> vMultiplicities, List<Double> uKnots) {
        this(type, center, axis, xDirection, radius, minorRadius, semiAngle, lowerHeight, upperHeight, startAngle, sweepAngle, uDegree, vDegree, controlPoints, uMultiplicities, vMultiplicities, uKnots, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public String getType() {
        return type;
    }
    public List<Double> getCenter() {
        return center;
    }
    public List<Double> getAxis() {
        return axis;
    }
    public List<Double> getXDirection() {
        return xDirection;
    }
    public double getRadius() {
        return radius;
    }
    public Double getMinorRadius() {
        return minorRadius;
    }
    public Double getSemiAngle() {
        return semiAngle;
    }
    public double getLowerHeight() {
        return lowerHeight;
    }
    public double getUpperHeight() {
        return upperHeight;
    }
    public double getStartAngle() {
        return startAngle;
    }
    public double getSweepAngle() {
        return sweepAngle;
    }
    public Integer getUDegree() {
        return uDegree;
    }
    public Integer getVDegree() {
        return vDegree;
    }
    public List<List<List<Double>>> getControlPoints() {
        return controlPoints;
    }
    public List<Integer> getUMultiplicities() {
        return uMultiplicities;
    }
    public List<Integer> getVMultiplicities() {
        return vMultiplicities;
    }
    public List<Double> getUKnots() {
        return uKnots;
    }
    public List<Double> getVKnots() {
        return vKnots;
    }
    public String getSourceType() {
        return sourceType;
    }
    public Integer getSourceStepId() {
        return sourceStepId;
    }
    public String getBasisType() {
        return basisType;
    }
    public Integer getBasisStepId() {
        return basisStepId;
    }
    public Boolean getOrientation() {
        return orientation;
    }
    public Double getOffsetDistance() {
        return offsetDistance;
    }
    public Double getTrimU1() {
        return trimU1;
    }
    public Double getTrimU2() {
        return trimU2;
    }
    public Double getTrimV1() {
        return trimV1;
    }
    public Double getTrimV2() {
        return trimV2;
    }
    public Boolean getImplicitOuter() {
        return implicitOuter;
    }
    public Double getTransformScale() {
        return transformScale;
    }

    // Record-style accessors
    public String type() { return type; }
    public List<Double> center() { return center; }
    public List<Double> axis() { return axis; }
    public List<Double> xDirection() { return xDirection; }
    public double radius() { return radius; }
    public Double minorRadius() { return minorRadius; }
    public Double semiAngle() { return semiAngle; }
    public double lowerHeight() { return lowerHeight; }
    public double upperHeight() { return upperHeight; }
    public double startAngle() { return startAngle; }
    public double sweepAngle() { return sweepAngle; }
    public Integer uDegree() { return uDegree; }
    public Integer vDegree() { return vDegree; }
    public List<List<List<Double>>> controlPoints() { return controlPoints; }
    public List<Integer> uMultiplicities() { return uMultiplicities; }
    public List<Integer> vMultiplicities() { return vMultiplicities; }
    public List<Double> uKnots() { return uKnots; }
    public List<Double> vKnots() { return vKnots; }
    public String sourceType() { return sourceType; }
    public Integer sourceStepId() { return sourceStepId; }
    public String basisType() { return basisType; }
    public Integer basisStepId() { return basisStepId; }
    public Boolean orientation() { return orientation; }
    public Double offsetDistance() { return offsetDistance; }
    public Double trimU1() { return trimU1; }
    public Double trimU2() { return trimU2; }
    public Double trimV1() { return trimV1; }
    public Double trimV2() { return trimV2; }
    public Boolean implicitOuter() { return implicitOuter; }
    public Double transformScale() { return transformScale; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FaceSurfacePayload)) return false;
        FaceSurfacePayload that = (FaceSurfacePayload) o;
        return java.util.Objects.equals(type, that.type) && java.util.Objects.equals(center, that.center) && java.util.Objects.equals(axis, that.axis) && java.util.Objects.equals(xDirection, that.xDirection) && Double.compare(that.radius, radius) == 0 && java.util.Objects.equals(minorRadius, that.minorRadius) && java.util.Objects.equals(semiAngle, that.semiAngle) && Double.compare(that.lowerHeight, lowerHeight) == 0 && Double.compare(that.upperHeight, upperHeight) == 0 && Double.compare(that.startAngle, startAngle) == 0 && Double.compare(that.sweepAngle, sweepAngle) == 0 && java.util.Objects.equals(uDegree, that.uDegree) && java.util.Objects.equals(vDegree, that.vDegree) && java.util.Objects.equals(controlPoints, that.controlPoints) && java.util.Objects.equals(uMultiplicities, that.uMultiplicities) && java.util.Objects.equals(vMultiplicities, that.vMultiplicities) && java.util.Objects.equals(uKnots, that.uKnots) && java.util.Objects.equals(vKnots, that.vKnots) && java.util.Objects.equals(sourceType, that.sourceType) && java.util.Objects.equals(sourceStepId, that.sourceStepId) && java.util.Objects.equals(basisType, that.basisType) && java.util.Objects.equals(basisStepId, that.basisStepId) && java.util.Objects.equals(orientation, that.orientation) && java.util.Objects.equals(offsetDistance, that.offsetDistance) && java.util.Objects.equals(trimU1, that.trimU1) && java.util.Objects.equals(trimU2, that.trimU2) && java.util.Objects.equals(trimV1, that.trimV1) && java.util.Objects.equals(trimV2, that.trimV2) && java.util.Objects.equals(implicitOuter, that.implicitOuter) && java.util.Objects.equals(transformScale, that.transformScale);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(type, center, axis, xDirection, Double.hashCode(radius), minorRadius, semiAngle, Double.hashCode(lowerHeight), Double.hashCode(upperHeight), Double.hashCode(startAngle), Double.hashCode(sweepAngle), uDegree, vDegree, controlPoints, uMultiplicities, vMultiplicities, uKnots, vKnots, sourceType, sourceStepId, basisType, basisStepId, orientation, offsetDistance, trimU1, trimU2, trimV1, trimV2, implicitOuter, transformScale);
    }

    @Override public String toString() {
        return "FaceSurfacePayload{" + "type=type, center=center, axis=axis, xDirection=xDirection, radius=radius, minorRadius=minorRadius, semiAngle=semiAngle, lowerHeight=lowerHeight, upperHeight=upperHeight, startAngle=startAngle, sweepAngle=sweepAngle, uDegree=uDegree, vDegree=vDegree, controlPoints=controlPoints, uMultiplicities=uMultiplicities, vMultiplicities=vMultiplicities, uKnots=uKnots, vKnots=vKnots, sourceType=sourceType, sourceStepId=sourceStepId, basisType=basisType, basisStepId=basisStepId, orientation=orientation, offsetDistance=offsetDistance, trimU1=trimU1, trimU2=trimU2, trimV1=trimV1, trimV2=trimV2, implicitOuter=implicitOuter, transformScale=transformScale" + "}";
    }
}

final class FacePayload {
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
    public double getTransparency() {
        return transparency;
    }
    public PbrPayload getPbr() {
        return pbr;
    }
    public List<String> getLayers() {
        return layers;
    }
    public List<LoopPayload> getLoops() {
        return loops;
    }
    public List<PointPayload> getTriangles() {
        return triangles;
    }
    public FaceSurfacePayload getSurface() {
        return surface;
    }
    public List<ParametricLoopPayload> getUvLoops() {
        return uvLoops;
    }

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

final class UnsupportedFacePayload {
    private final int stepId;
    private final String name;
    private final String surfaceType;
    private final String reason;

    public UnsupportedFacePayload(int stepId, String name, String surfaceType, String reason) {
        this.stepId = stepId;
        this.name = name;
        this.surfaceType = surfaceType;
        this.reason = reason;
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
    public String getReason() {
        return reason;
    }

    // Record-style accessors
    public int stepId() { return stepId; }
    public String name() { return name; }
    public String surfaceType() { return surfaceType; }
    public String reason() { return reason; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnsupportedFacePayload)) return false;
        UnsupportedFacePayload that = (UnsupportedFacePayload) o;
        return stepId == that.stepId && java.util.Objects.equals(name, that.name) && java.util.Objects.equals(surfaceType, that.surfaceType) && java.util.Objects.equals(reason, that.reason);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(stepId, name, surfaceType, reason);
    }

    @Override public String toString() {
        return "UnsupportedFacePayload{" + "stepId=stepId, name=name, surfaceType=surfaceType, reason=reason" + "}";
    }
}

final class UnsupportedBooleanPayload {
    private final int stepId;
    private final String name;
    private final String type;
    private final String reason;

    public UnsupportedBooleanPayload(int stepId, String name, String type, String reason) {
        this.stepId = stepId;
        this.name = name;
        this.type = type;
        this.reason = reason;
    }

    public int getStepId() {
        return stepId;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public String getReason() {
        return reason;
    }

    // Record-style accessors
    public int stepId() { return stepId; }
    public String name() { return name; }
    public String type() { return type; }
    public String reason() { return reason; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnsupportedBooleanPayload)) return false;
        UnsupportedBooleanPayload that = (UnsupportedBooleanPayload) o;
        return stepId == that.stepId && java.util.Objects.equals(name, that.name) && java.util.Objects.equals(type, that.type) && java.util.Objects.equals(reason, that.reason);
    }

    @Override public int hashCode() {
        return java.util.Objects.hash(stepId, name, type, reason);
    }

    @Override public String toString() {
        return "UnsupportedBooleanPayload{" + "stepId=stepId, name=name, type=type, reason=reason" + "}";
    }
}

final class PreviewFaceResult {
    private final FacePayload face;
    private final UnsupportedFacePayload unsupportedFace;

    public PreviewFaceResult(FacePayload face, UnsupportedFacePayload unsupportedFace) {
        this.face = face;
        this.unsupportedFace = unsupportedFace;
    }

    public FacePayload getFace() {
        return face;
    }
    public UnsupportedFacePayload getUnsupportedFace() {
        return unsupportedFace;
    }

    // Record-style accessors
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

final class SurfacePatch {
    private final List<CartesianPoint> bottom;
    private final List<CartesianPoint> top;
    private final List<CartesianPoint> left;
    private final List<CartesianPoint> right;

    public SurfacePatch(
            List<CartesianPoint> bottom,
            List<CartesianPoint> top,
            List<CartesianPoint> left,
            List<CartesianPoint> right
    ) {
        this.bottom = PreviewPayloadCopies.copy(bottom);
        this.top = PreviewPayloadCopies.copy(top);
        this.left = PreviewPayloadCopies.copy(left);
        this.right = PreviewPayloadCopies.copy(right);
    }

    public List<CartesianPoint> getBottom() {
        return bottom;
    }

    public List<CartesianPoint> getTop() {
        return top;
    }

    public List<CartesianPoint> getLeft() {
        return left;
    }

    public List<CartesianPoint> getRight() {
        return right;
    }

    int uSegments() {
        return bottom.size() - 1;
    }

    int vSegments() {
        return left.size() - 1;
    }

    CartesianPoint pointAt(double u, double v) {
        CartesianPoint c0 = sample(bottom, u);
        CartesianPoint c1 = sample(top, u);
        CartesianPoint d0 = sample(left, v);
        CartesianPoint d1 = sample(right, v);
        CartesianPoint p00 = bottom.get(0);
        CartesianPoint p10 = bottom.get(bottom.size() - 1);
        CartesianPoint p01 = top.get(0);
        CartesianPoint p11 = top.get(top.size() - 1);
        return bilinearBlend(c0, c1, d0, d1, p00, p10, p01, p11, u, v);
    }

    Vector3 normalAt(double u, double v) {
        double du = Math.max(1.0 / Math.max(uSegments(), 1), 1.0e-3);
        double dv = Math.max(1.0 / Math.max(vSegments(), 1), 1.0e-3);
        CartesianPoint p = pointAt(u, v);
        CartesianPoint pu = pointAt(Math.min(1.0, u + du), v);
        CartesianPoint pv = pointAt(u, Math.min(1.0, v + dv));
        Vector3 normal = pu.subtract(p).cross(pv.subtract(p));
        if (normal.norm() <= Epsilon.EPS) {
            return new Vector3(0.0, 0.0, 1.0);
        }
        return normal.normalize().asVector();
    }

    private static CartesianPoint sample(List<CartesianPoint> polyline, double t) {
        double clamped = Math.max(0.0, Math.min(1.0, t));
        double scaled = clamped * (polyline.size() - 1);
        int low = Math.min((int) Math.floor(scaled), polyline.size() - 1);
        int high = Math.min(low + 1, polyline.size() - 1);
        double alpha = scaled - low;
        return interpolate(polyline.get(low), polyline.get(high), alpha);
    }

    private static CartesianPoint bilinearBlend(
            CartesianPoint c0,
            CartesianPoint c1,
            CartesianPoint d0,
            CartesianPoint d1,
            CartesianPoint p00,
            CartesianPoint p10,
            CartesianPoint p01,
            CartesianPoint p11,
            double u,
            double v
    ) {
        double x = (1.0 - v) * c0.x() + v * c1.x() + (1.0 - u) * d0.x() + u * d1.x()
                - ((1.0 - u) * (1.0 - v) * p00.x() + u * (1.0 - v) * p10.x()
                + (1.0 - u) * v * p01.x() + u * v * p11.x());
        double y = (1.0 - v) * c0.y() + v * c1.y() + (1.0 - u) * d0.y() + u * d1.y()
                - ((1.0 - u) * (1.0 - v) * p00.y() + u * (1.0 - v) * p10.y()
                + (1.0 - u) * v * p01.y() + u * v * p11.y());
        double z = (1.0 - v) * c0.z() + v * c1.z() + (1.0 - u) * d0.z() + u * d1.z()
                - ((1.0 - u) * (1.0 - v) * p00.z() + u * (1.0 - v) * p10.z()
                + (1.0 - u) * v * p01.z() + u * v * p11.z());
        return new CartesianPoint(x, y, z);
    }

    private static CartesianPoint interpolate(CartesianPoint a, CartesianPoint b, double alpha) {
        return new CartesianPoint(
                a.x() + (b.x() - a.x()) * alpha,
                a.y() + (b.y() - a.y()) * alpha,
                a.z() + (b.z() - a.z()) * alpha
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SurfacePatch)) return false;
        SurfacePatch that = (SurfacePatch) o;
        return java.util.Objects.equals(bottom, that.bottom)
                && java.util.Objects.equals(top, that.top)
                && java.util.Objects.equals(left, that.left)
                && java.util.Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(bottom, top, left, right);
    }

    @Override
    public String toString() {
        return "SurfacePatch{bottom=" + bottom + ", top=" + top + ", left=" + left + ", right=" + right + "}";
    }
}

final class LoopPayload {
    private final boolean outer;
    private final List<PointPayload> points;

    public LoopPayload(boolean outer, List<PointPayload> points) {
        this.outer = outer;
        this.points = PreviewPayloadCopies.copy(points);
    }

    public boolean getOuter() {
        return outer;
    }
    public List<PointPayload> getPoints() {
        return points;
    }

    // Record-style accessors
    public boolean outer() { return outer; }
    public List<PointPayload> points() { return points; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoopPayload that = (LoopPayload) o;
        return outer == that.outer && java.util.Objects.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(Boolean.hashCode(outer), points);
    }

    @Override
    public String toString() {
        return "LoopPayload{outer=" + outer + ", points=" + points + "}";
    }
}

final class PointPayload {
    private final double x;
    private final double y;
    private final double z;

    public PointPayload(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }

    // Record-style accessors
    public double x() { return x; }
    public double y() { return y; }
    public double z() { return z; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointPayload that = (PointPayload) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Double.compare(that.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(Double.hashCode(x), Double.hashCode(y), Double.hashCode(z));
    }

    @Override
    public String toString() {
        return "PointPayload{x=" + x + ", y=" + y + ", z=" + z + "}";
    }
}

final class VectorPayload {
    private final double x;
    private final double y;
    private final double z;

    public VectorPayload(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }

    // Record-style accessors
    public double x() { return x; }
    public double y() { return y; }
    public double z() { return z; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VectorPayload that = (VectorPayload) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Double.compare(that.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(Double.hashCode(x), Double.hashCode(y), Double.hashCode(z));
    }

    @Override
    public String toString() {
        return "VectorPayload{x=" + x + ", y=" + y + ", z=" + z + "}";
    }
}

final class ColorPayload {
    private final int red;
    private final int green;
    private final int blue;

    public ColorPayload(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }
    public int getGreen() {
        return green;
    }
    public int getBlue() {
        return blue;
    }

    // Record-style accessors
    public int red() { return red; }
    public int green() { return green; }
    public int blue() { return blue; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColorPayload that = (ColorPayload) o;
        return red == that.red && green == that.green && blue == that.blue;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(red, green, blue);
    }

    @Override
    public String toString() {
        return "ColorPayload{red=" + red + ", green=" + green + ", blue=" + blue + "}";
    }
}

final class PbrPayload {
    private final double diffuse;
    private final double specular;
    private final Double specularExponent;
    private final int[] specularColor;

    public PbrPayload(double diffuse, double specular, Double specularExponent, int[] specularColor) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.specularExponent = specularExponent;
        this.specularColor = PreviewPayloadCopies.copy(specularColor);
    }

    public double getDiffuse() {
        return diffuse;
    }
    public double getSpecular() {
        return specular;
    }
    public Double getSpecularExponent() {
        return specularExponent;
    }
    public int[] getSpecularColor() {
        return specularColor;
    }

    // Record-style accessors
    public double diffuse() { return diffuse; }
    public double specular() { return specular; }
    public Double specularExponent() { return specularExponent; }
    public int[] specularColor() { return specularColor; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PbrPayload that = (PbrPayload) o;
        return Double.compare(that.diffuse, diffuse) == 0 && Double.compare(that.specular, specular) == 0 && java.util.Objects.equals(specularExponent, that.specularExponent) && java.util.Arrays.equals(specularColor, that.specularColor);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(Double.hashCode(diffuse), Double.hashCode(specular), specularExponent, java.util.Arrays.hashCode(specularColor));
    }

    @Override
    public String toString() {
        return "PbrPayload{diffuse=" + diffuse + ", specular=" + specular + ", specularExponent=" + specularExponent + ", specularColor=" + java.util.Arrays.toString(specularColor) + "}";
    }
}

final class PreviewPayloadCopies {
    private PreviewPayloadCopies() {
    }

    static <T> List<T> copy(List<T> values) {
        return values == null ? null : List.copyOf(values);
    }

    static List<List<List<Double>>> copyControlPoints(List<List<List<Double>>> values) {
        if (values == null) {
            return null;
        }
        return values.stream()
                .map(plane -> plane == null ? null : plane.stream()
                        .map(row -> row == null ? null : List.copyOf(row))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    static double[] copy(double[] values) {
        return values == null ? null : values.clone();
    }

    static float[] copy(float[] values) {
        return values == null ? null : values.clone();
    }

    static int[] copy(int[] values) {
        return values == null ? null : values.clone();
    }
}
