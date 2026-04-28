package com.minicad.app;

import com.minicad.common.Epsilon;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serialization utilities for preview payloads: JSON, binary geometry,
 * and GLB output. Extracted from StepPreviewJsonExporter.
 */
public final class PreviewSerializers {

    private PreviewSerializers() {}

    // ─── Top-level serializers ───────────────────────────────────────────

    public static String toJson(PreviewPayload payload) {
        StringBuilder json = new StringBuilder(4096);
        json.append('{');
        json.append("\"stats\":");
        appendStats(json, payload.stats());
        json.append(",\"bounds\":");
        appendBounds(json, payload.bounds());
        json.append(",\"validation\":");
        appendValidation(json, payload.validation());
        json.append(",\"product\":");
        appendProductMetadata(json, payload.product());
        json.append(",\"units\":");
        appendUnitInfo(json, payload.units());
        json.append(",\"pmi\":");
        appendPmi(json, payload.pmi());
        json.append(",\"unsupportedBooleans\":");
        appendUnsupportedBooleans(json, payload.unsupportedBooleans());
        json.append(",\"unsupportedFaces\":");
        appendUnsupportedFaces(json, payload.unsupportedFaces());
        json.append(",\"edges\":");
        appendEdges(json, payload.edges());
        json.append(",\"faces\":");
        appendFaces(json, payload.faces());
        json.append(",\"representations\":");
        appendRepresentations(json, payload.representations());
        json.append(",\"instances\":");
        appendInstances(json, payload.instances());
        json.append('}');
        return json.toString();
    }

    public static byte[] toBinary(PreviewPayload payload) {
        BinaryGeometryBuffer geometry = new BinaryGeometryBuffer();
        BinaryPreviewPayload binaryPayload = toBinaryPayload(payload, geometry);
        byte[] metadata = toBinaryMetadataJson(binaryPayload).getBytes(StandardCharsets.UTF_8);
        int geometryOffset = alignTo4(16 + metadata.length);
        ByteArrayOutputStream output = new ByteArrayOutputStream(geometryOffset + geometry.size());
        output.writeBytes(new byte[]{'M', 'C', 'P', 'B'});
        writeIntLE(output, 1);
        writeIntLE(output, metadata.length);
        writeIntLE(output, geometryOffset);
        output.writeBytes(metadata);
        while (output.size() < geometryOffset) {
            output.write(0);
        }
        output.writeBytes(geometry.toByteArray());
        return output.toByteArray();
    }

    public static byte[] toGlb(PreviewPayload payload) {
        GlbSceneBuilder builder = new GlbSceneBuilder();
        byte[] jsonBytes = builder.buildJson(payload).getBytes(StandardCharsets.UTF_8);
        byte[] paddedJson = padChunk(jsonBytes);
        byte[] binaryChunk = builder.binaryChunk();
        byte[] paddedBinary = padChunk(binaryChunk);

        ByteArrayOutputStream output = new ByteArrayOutputStream(12 + 8 + paddedJson.length + 8 + paddedBinary.length);
        writeIntLE(output, 0x46546C67);
        writeIntLE(output, 2);
        writeIntLE(output, 12 + 8 + paddedJson.length + 8 + paddedBinary.length);
        writeIntLE(output, paddedJson.length);
        writeIntLE(output, 0x4E4F534A);
        output.writeBytes(paddedJson);
        writeIntLE(output, paddedBinary.length);
        writeIntLE(output, 0x004E4942);
        output.writeBytes(paddedBinary);
        return output.toByteArray();
    }

    public static byte[] padChunk(byte[] bytes) {
        int paddedLength = alignTo4(bytes.length);
        if (paddedLength == bytes.length) {
            return bytes;
        }
        byte[] padded = new byte[paddedLength];
        System.arraycopy(bytes, 0, padded, 0, bytes.length);
        for (int i = bytes.length; i < padded.length; i++) {
            padded[i] = 0x20;
        }
        return padded;
    }

    // ─── Binary payload conversion ───────────────────────────────────────

    public static BinaryPreviewPayload toBinaryPayload(PreviewPayload payload, BinaryGeometryBuffer geometry) {
        return new BinaryPreviewPayload(
                payload.stats(),
                payload.bounds(),
                payload.validation(),
                payload.product(),
                payload.units(),
                payload.pmi(),
                payload.unsupportedBooleans(),
                payload.unsupportedFaces(),
                payload.edges().stream().map(edge -> toBinaryEdge(edge, geometry)).toList(),
                payload.faces().stream().map(face -> toBinaryFace(face, geometry)).toList(),
                payload.representations().stream().map(representation -> new BinaryRepresentationPayload(
                        representation.id(),
                        representation.name(),
                        representation.layers(),
                        representation.color(),
                        representation.edges().stream().map(edge -> toBinaryEdge(edge, geometry)).toList(),
                        representation.faces().stream().map(face -> toBinaryFace(face, geometry)).toList()
                )).toList(),
                payload.instances()
        );
    }

    public static BinaryEdgePayload toBinaryEdge(EdgePayload edge, BinaryGeometryBuffer geometry) {
        PointRange range = geometry.append(edge.points());
        return new BinaryEdgePayload(edge.stepId(), range.offset(), range.count(), edge.curve(), edge.color());
    }

    public static BinaryFacePayload toBinaryFace(FacePayload face, BinaryGeometryBuffer geometry) {
        PointRange triangles = geometry.append(face.triangles());
        List<BinaryLoopPayload> loops = face.loops().stream()
                .map(loop -> {
                    PointRange range = geometry.append(loop.points());
                    return new BinaryLoopPayload(loop.outer(), range.offset(), range.count());
                })
                .toList();
        return new BinaryFacePayload(
                face.stepId(),
                face.name(),
                face.surfaceType(),
                face.origin(),
                face.normal(),
                face.sameSense(),
                face.color(),
                face.layers(),
                face.surface(),
                face.uvLoops(),
                loops,
                triangles.offset(),
                triangles.count()
        );
    }

    public static String toBinaryMetadataJson(BinaryPreviewPayload payload) {
        StringBuilder json = new StringBuilder(4096);
        json.append('{');
        json.append("\"format\":\"binary-preview-v1\"");
        json.append(",\"pointEncoding\":\"float32-le\"");
        json.append(",\"pointStride\":3");
        json.append(",\"stats\":");
        appendStats(json, payload.stats());
        json.append(",\"bounds\":");
        appendBounds(json, payload.bounds());
        json.append(",\"validation\":");
        appendValidation(json, payload.validation());
        json.append(",\"product\":");
        appendProductMetadata(json, payload.product());
        json.append(",\"units\":");
        appendUnitInfo(json, payload.units());
        json.append(",\"pmi\":");
        appendPmi(json, payload.pmi());
        json.append(",\"unsupportedBooleans\":");
        appendUnsupportedBooleans(json, payload.unsupportedBooleans());
        json.append(",\"unsupportedFaces\":");
        appendUnsupportedFaces(json, payload.unsupportedFaces());
        json.append(",\"edges\":");
        appendBinaryEdges(json, payload.edges());
        json.append(",\"faces\":");
        appendBinaryFaces(json, payload.faces());
        json.append(",\"representations\":");
        appendBinaryRepresentations(json, payload.representations());
        json.append(",\"instances\":");
        appendInstances(json, payload.instances());
        json.append('}');
        return json.toString();
    }

    // ─── Binary-specific appenders ───────────────────────────────────────

    public static void appendBinaryEdges(StringBuilder json, List<BinaryEdgePayload> edges) {
        json.append('[');
        for (int i = 0; i < edges.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            BinaryEdgePayload edge = edges.get(i);
            json.append('{');
            json.append("\"id\":").append(edge.stepId());
            json.append(",\"pointOffset\":").append(edge.pointOffset());
            json.append(",\"pointCount\":").append(edge.pointCount());
            if (edge.curve() != null) {
                json.append(",\"curve\":");
                appendJsonValue(json, previewEdgeCurveMap(edge.curve()));
            }
            if (edge.color() != null) {
                json.append(",\"color\":");
                appendColor(json, edge.color());
            }
            json.append('}');
        }
        json.append(']');
    }

    public static void appendBinaryFaces(StringBuilder json, List<BinaryFacePayload> faces) {
        json.append('[');
        for (int i = 0; i < faces.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            BinaryFacePayload face = faces.get(i);
            json.append('{');
            json.append("\"id\":").append(face.stepId());
            json.append(",\"name\":").append(quote(face.name()));
            json.append(",\"surfaceType\":").append(quote(face.surfaceType()));
            json.append(",\"origin\":");
            appendPoint(json, face.origin());
            json.append(",\"normal\":");
            appendVector(json, face.normal());
            json.append(",\"sameSense\":").append(face.sameSense());
            json.append(",\"color\":");
            appendColor(json, face.color());
            json.append(",\"layers\":");
            appendStringList(json, face.layers());
            if (face.surface() != null) {
                json.append(",\"surface\":");
                appendJsonValue(json, previewFaceSurfaceMap(face.surface()));
            }
            if (face.uvLoops() != null && !face.uvLoops().isEmpty()) {
                json.append(",\"surfaceUvLoops\":");
                appendJsonValue(json, previewUvLoopMaps(face.uvLoops()));
            }
            json.append(",\"loops\":");
            appendBinaryLoops(json, face.loops());
            json.append(",\"triangleOffset\":").append(face.triangleOffset());
            json.append(",\"triangleCount\":").append(face.triangleCount());
            json.append('}');
        }
        json.append(']');
    }

    public static void appendBinaryLoops(StringBuilder json, List<BinaryLoopPayload> loops) {
        json.append('[');
        for (int i = 0; i < loops.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            BinaryLoopPayload loop = loops.get(i);
            json.append('{');
            json.append("\"outer\":").append(loop.outer());
            json.append(",\"pointOffset\":").append(loop.pointOffset());
            json.append(",\"pointCount\":").append(loop.pointCount());
            json.append('}');
        }
        json.append(']');
    }

    public static void appendBinaryRepresentations(StringBuilder json, List<BinaryRepresentationPayload> representations) {
        json.append('[');
        for (int i = 0; i < representations.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            BinaryRepresentationPayload representation = representations.get(i);
            json.append('{');
            json.append("\"id\":").append(representation.id());
            json.append(",\"name\":").append(quote(representation.name()));
            json.append(",\"layers\":");
            appendStringList(json, representation.layers());
            json.append(",\"color\":");
            appendColor(json, representation.color());
            json.append(",\"edges\":");
            appendBinaryEdges(json, representation.edges());
            json.append(",\"faces\":");
            appendBinaryFaces(json, representation.faces());
            json.append('}');
        }
        json.append(']');
    }

    // ─── Preview map builders ────────────────────────────────────────────

    public static Map<String, Object> previewMetadata(PreviewPayload payload) {
        Map<String, Object> preview = new LinkedHashMap<>();
        preview.put("stats", previewStatsMap(payload.stats()));
        preview.put("bounds", boundsMap(payload.bounds()));
        preview.put("validation", validationMap(payload.validation()));
        preview.put("pmi", pmiMaps(payload.pmi()));
        preview.put("unsupportedBooleans", unsupportedBooleanMaps(payload.unsupportedBooleans()));
        preview.put("unsupportedFaces", unsupportedFaceMaps(payload.unsupportedFaces()));
        preview.put("instances", instanceMaps(payload.instances()));
        return preview;
    }

    public static Map<String, Object> previewStatsMap(PreviewStats stats) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("entityCount", stats.entityCount());
        map.put("solidCount", stats.solidCount());
        map.put("shellCount", stats.shellCount());
        map.put("faceCount", stats.faceCount());
        map.put("edgeCount", stats.edgeCount());
        map.put("unsupportedFaceCount", stats.unsupportedFaceCount());
        map.put("unsupportedBooleanCount", stats.unsupportedBooleanCount());
        return map;
    }

    public static Map<String, Object> boundsMap(BoundsPayload bounds) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("min", pointList(bounds.min()));
        map.put("max", pointList(bounds.max()));
        return map;
    }

    public static Map<String, Object> validationMap(ValidationPayload validation) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("representationCount", validation.representationCount());
        map.put("instanceCount", validation.instanceCount());
        map.put("renderedFaceCount", validation.renderedFaceCount());
        map.put("renderedEdgeCount", validation.renderedEdgeCount());
        map.put("approxSurfaceArea", validation.approxSurfaceArea());
        map.put("approxEdgeLength", validation.approxEdgeLength());
        map.put("center", pointList(validation.center()));
        map.put("report", validationReportMap(validation.report()));
        map.put("nativeChecks", validationChecks(validation.report().checks()));
        return map;
    }

    // ─── Low-level binary helpers ────────────────────────────────────────

    public static int alignTo4(int value) {
        int remainder = value % 4;
        return remainder == 0 ? value : value + (4 - remainder);
    }

    public static void writeIntLE(ByteArrayOutputStream output, int value) {
        output.write(value & 0xFF);
        output.write((value >>> 8) & 0xFF);
        output.write((value >>> 16) & 0xFF);
        output.write((value >>> 24) & 0xFF);
    }

    public static void writeFloatLE(ByteArrayOutputStream output, float value) {
        writeIntLE(output, Float.floatToRawIntBits(value));
    }

    // ─── JSON value writer ───────────────────────────────────────────────

    public static void appendJsonValue(StringBuilder json, Object value) {
        if (value == null) {
            json.append("null");
            return;
        }
        if (value instanceof String text) {
            json.append(quote(text));
            return;
        }
        if (value instanceof Boolean || value instanceof Integer || value instanceof Long) {
            json.append(value);
            return;
        }
        if (value instanceof Float || value instanceof Double) {
            json.append(format(((Number) value).doubleValue()));
            return;
        }
        if (value instanceof Map<?, ?> map) {
            json.append('{');
            boolean first = true;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!first) {
                    json.append(',');
                }
                first = false;
                json.append(quote(String.valueOf(entry.getKey()))).append(':');
                appendJsonValue(json, entry.getValue());
            }
            json.append('}');
            return;
        }
        if (value instanceof List<?> list) {
            json.append('[');
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    json.append(',');
                }
                appendJsonValue(json, list.get(i));
            }
            json.append(']');
            return;
        }
        throw new IllegalArgumentException("unsupported json value: " + value.getClass().getName());
    }

    // ─── Map builders for nested payloads ────────────────────────────────

    private static Map<String, Object> validationReportMap(ValidationReportPayload report) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", report.status());
        map.put("okCount", report.okCount());
        map.put("warnCount", report.warnCount());
        map.put("checks", validationChecks(report.checks()));
        return map;
    }

    private static List<Map<String, Object>> validationChecks(List<ValidationCheckPayload> checks) {
        List<Map<String, Object>> list = new ArrayList<>(checks.size());
        for (ValidationCheckPayload check : checks) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("propertyId", check.propertyId());
            map.put("name", check.name());
            map.put("measureType", check.measureType());
            map.put("expected", check.expected());
            map.put("actual", check.actual());
            map.put("delta", check.delta());
            map.put("status", check.status());
            map.put("matches", check.matches());
            list.add(map);
        }
        return List.copyOf(list);
    }

    private static List<Map<String, Object>> pmiMaps(List<PmiPayload> pmi) {
        List<Map<String, Object>> list = new ArrayList<>(pmi.size());
        for (PmiPayload item : pmi) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", item.name());
            map.put("text", item.text());
            map.put("position", pointList(item.position()));
            map.put("leader", item.leader().stream().map(PreviewSerializers::pointList).toList());
            map.put("targetIds", item.targetIds());
            map.put("targets", item.targets().stream().map(target -> {
                Map<String, Object> targetMap = new LinkedHashMap<>();
                targetMap.put("id", target.id());
                targetMap.put("type", target.type());
                targetMap.put("name", target.name());
                targetMap.put("instanceIds", target.instanceIds());
                if (target.viaRelationshipType() != null) {
                    targetMap.put("viaRelationshipType", target.viaRelationshipType());
                }
                if (target.viaRelationshipId() != null) {
                    targetMap.put("viaRelationshipId", target.viaRelationshipId());
                }
                if (target.viaUsageType() != null) {
                    targetMap.put("viaUsageType", target.viaUsageType());
                }
                if (target.viaUsageId() != null) {
                    targetMap.put("viaUsageId", target.viaUsageId());
                }
                if (target.viaDefinitionType() != null) {
                    targetMap.put("viaDefinitionType", target.viaDefinitionType());
                }
                if (target.viaDefinitionId() != null) {
                    targetMap.put("viaDefinitionId", target.viaDefinitionId());
                }
                return targetMap;
            }).toList());
            list.add(map);
        }
        return List.copyOf(list);
    }

    private static List<Map<String, Object>> unsupportedFaceMaps(List<UnsupportedFacePayload> unsupportedFaces) {
        List<Map<String, Object>> list = new ArrayList<>(unsupportedFaces.size());
        for (UnsupportedFacePayload face : unsupportedFaces) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", face.stepId());
            map.put("name", face.name());
            map.put("surfaceType", face.surfaceType());
            map.put("reason", face.reason());
            list.add(map);
        }
        return List.copyOf(list);
    }

    private static List<Map<String, Object>> unsupportedBooleanMaps(List<UnsupportedBooleanPayload> unsupportedBooleans) {
        List<Map<String, Object>> list = new ArrayList<>(unsupportedBooleans.size());
        for (UnsupportedBooleanPayload item : unsupportedBooleans) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.stepId());
            map.put("name", item.name());
            map.put("type", item.type());
            map.put("reason", item.reason());
            list.add(map);
        }
        return List.copyOf(list);
    }

    private static List<Map<String, Object>> instanceMaps(List<InstancePayload> instances) {
        List<Map<String, Object>> list = new ArrayList<>(instances.size());
        for (InstancePayload instance : instances) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", instance.id());
            map.put("parentId", instance.parentId());
            map.put("productDefinitionId", instance.productDefinitionId());
            map.put("occurrenceId", instance.occurrenceId());
            map.put("representationId", instance.representationId());
            map.put("representationIds", instance.representationIds());
            map.put("label", instance.label());
            map.put("description", instance.description());
            map.put("depth", instance.depth());
            list.add(map);
        }
        return List.copyOf(list);
    }

    // ─── JSON appenders ──────────────────────────────────────────────────

    public static void appendStats(StringBuilder json, PreviewStats stats) {
        json.append('{');
        json.append("\"entityCount\":").append(stats.entityCount());
        json.append(",\"solidCount\":").append(stats.solidCount());
        json.append(",\"shellCount\":").append(stats.shellCount());
        json.append(",\"faceCount\":").append(stats.faceCount());
        json.append(",\"edgeCount\":").append(stats.edgeCount());
        json.append(",\"unsupportedFaceCount\":").append(stats.unsupportedFaceCount());
        json.append(",\"unsupportedBooleanCount\":").append(stats.unsupportedBooleanCount());
        json.append('}');
    }

    public static void appendBounds(StringBuilder json, BoundsPayload bounds) {
        json.append('{');
        json.append("\"min\":");
        appendPoint(json, bounds.min());
        json.append(",\"max\":");
        appendPoint(json, bounds.max());
        json.append('}');
    }

    public static void appendValidation(StringBuilder json, ValidationPayload validation) {
        json.append('{');
        json.append("\"representationCount\":").append(validation.representationCount());
        json.append(",\"instanceCount\":").append(validation.instanceCount());
        json.append(",\"renderedFaceCount\":").append(validation.renderedFaceCount());
        json.append(",\"renderedEdgeCount\":").append(validation.renderedEdgeCount());
        json.append(",\"approxSurfaceArea\":").append(format(validation.approxSurfaceArea()));
        json.append(",\"approxEdgeLength\":").append(format(validation.approxEdgeLength()));
        json.append(",\"center\":");
        appendPoint(json, validation.center());
        json.append(",\"report\":");
        appendValidationReport(json, validation.report());
        json.append(",\"nativeChecks\":");
        appendValidationChecks(json, validation.report().checks());
        json.append('}');
    }

    private static void appendValidationReport(StringBuilder json, ValidationReportPayload report) {
        json.append('{');
        json.append("\"status\":").append(quote(report.status()));
        json.append(",\"okCount\":").append(report.okCount());
        json.append(",\"warnCount\":").append(report.warnCount());
        json.append(",\"checks\":");
        appendValidationChecks(json, report.checks());
        json.append('}');
    }

    private static void appendValidationChecks(StringBuilder json, List<ValidationCheckPayload> checks) {
        json.append('[');
        for (int i = 0; i < checks.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            ValidationCheckPayload check = checks.get(i);
            json.append('{');
            json.append("\"propertyId\":").append(quote(check.propertyId()));
            json.append(',');
            json.append("\"name\":").append(quote(check.name()));
            json.append(",\"measureType\":").append(quote(check.measureType()));
            json.append(",\"expected\":").append(format(check.expected()));
            json.append(",\"actual\":").append(format(check.actual()));
            json.append(",\"delta\":").append(format(check.delta()));
            json.append(",\"status\":").append(quote(check.status()));
            json.append(",\"matches\":").append(check.matches());
            json.append('}');
        }
        json.append(']');
    }

    public static void appendProductMetadata(StringBuilder json, ProductMetadataExtractor.ProductMetadata product) {
        json.append('{');
        json.append("\"fileName\":").append(quoteNullable(product.fileName()));
        json.append(",\"fileDescription\":").append(quoteNullable(product.fileDescription()));
        json.append(",\"productName\":").append(quoteNullable(product.productName()));
        json.append(",\"productDescription\":").append(quoteNullable(product.productDescription()));
        json.append(",\"productIdentifier\":").append(quoteNullable(product.productIdentifier()));
        json.append(",\"schemas\":");
        appendStringList(json, product.schemaNames());
        json.append(",\"components\":");
        appendComponentList(json, product.components());
        json.append('}');
    }

    public static void appendUnitInfo(StringBuilder json, UnitExtractor.UnitInfo units) {
        json.append('{');
        json.append("\"lengthUnit\":").append(quoteNullable(units.lengthUnit()));
        json.append(",\"scaleToMeters\":");
        if (units.scaleToMeters() != null) {
            json.append(format(units.scaleToMeters()));
        } else {
            json.append("null");
        }
        json.append(",\"angleUnit\":").append(quoteNullable(units.angleUnit()));
        json.append('}');
    }

    private static void appendComponentList(StringBuilder json, List<ProductMetadataExtractor.ProductMetadata.ComponentInfo> list) {
        json.append('[');
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) json.append(',');
            ProductMetadataExtractor.ProductMetadata.ComponentInfo c = list.get(i);
            json.append('{');
            json.append("\"name\":").append(quoteNullable(c.name()));
            json.append(",\"identifier\":").append(quoteNullable(c.identifier()));
            json.append(",\"description\":").append(quoteNullable(c.description()));
            json.append('}');
        }
        json.append(']');
    }

    private static String quoteNullable(String s) {
        return s == null ? "null" : quote(s);
    }

    public static void appendPmi(StringBuilder json, List<PmiPayload> pmi) {
        json.append('[');
        for (int i = 0; i < pmi.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            PmiPayload item = pmi.get(i);
            json.append('{');
            json.append("\"name\":").append(quote(item.name()));
            json.append(",\"text\":").append(quote(item.text()));
            json.append(",\"position\":");
            appendPoint(json, item.position());
            json.append(",\"leader\":");
            appendPoints(json, item.leader());
            json.append(",\"targetIds\":");
            appendIntegerList(json, item.targetIds());
            json.append(",\"targets\":");
            appendPmiTargets(json, item.targets());
            json.append('}');
        }
        json.append(']');
    }

    public static void appendUnsupportedFaces(StringBuilder json, List<UnsupportedFacePayload> unsupportedFaces) {
        json.append('[');
        for (int i = 0; i < unsupportedFaces.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            UnsupportedFacePayload face = unsupportedFaces.get(i);
            json.append('{');
            json.append("\"id\":").append(face.stepId());
            json.append(",\"name\":").append(quote(face.name()));
            json.append(",\"surfaceType\":").append(quote(face.surfaceType()));
            json.append(",\"reason\":").append(quote(face.reason()));
            json.append('}');
        }
        json.append(']');
    }

    public static void appendUnsupportedBooleans(StringBuilder json, List<UnsupportedBooleanPayload> unsupportedBooleans) {
        json.append('[');
        for (int i = 0; i < unsupportedBooleans.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            UnsupportedBooleanPayload item = unsupportedBooleans.get(i);
            json.append('{');
            json.append("\"id\":").append(item.stepId());
            json.append(",\"name\":").append(quote(item.name()));
            json.append(",\"type\":").append(quote(item.type()));
            json.append(",\"reason\":").append(quote(item.reason()));
            json.append('}');
        }
        json.append(']');
    }

    private static void appendPmiTargets(StringBuilder json, List<PmiTargetPayload> targets) {
        json.append('[');
        for (int i = 0; i < targets.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            PmiTargetPayload target = targets.get(i);
            json.append('{');
            json.append("\"id\":").append(target.id());
            json.append(",\"type\":").append(quote(target.type()));
            json.append(",\"name\":").append(quote(target.name()));
            json.append(",\"instanceIds\":");
            appendQuotedList(json, target.instanceIds());
            if (target.viaRelationshipType() != null) {
                json.append(",\"viaRelationshipType\":").append(quote(target.viaRelationshipType()));
            }
            if (target.viaRelationshipId() != null) {
                json.append(",\"viaRelationshipId\":").append(target.viaRelationshipId());
            }
            if (target.viaUsageType() != null) {
                json.append(",\"viaUsageType\":").append(quote(target.viaUsageType()));
            }
            if (target.viaUsageId() != null) {
                json.append(",\"viaUsageId\":").append(target.viaUsageId());
            }
            if (target.viaDefinitionType() != null) {
                json.append(",\"viaDefinitionType\":").append(quote(target.viaDefinitionType()));
            }
            if (target.viaDefinitionId() != null) {
                json.append(",\"viaDefinitionId\":").append(target.viaDefinitionId());
            }
            json.append('}');
        }
        json.append(']');
    }

    public static void appendEdges(StringBuilder json, List<EdgePayload> edges) {
        json.append('[');
        for (int i = 0; i < edges.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            EdgePayload edge = edges.get(i);
            json.append('{');
            json.append("\"id\":").append(edge.stepId());
            json.append(",\"points\":");
            appendPoints(json, edge.points());
            if (edge.curve() != null) {
                json.append(",\"curve\":");
                appendJsonValue(json, previewEdgeCurveMap(edge.curve()));
            }
            if (edge.color() != null) {
                json.append(",\"color\":");
                appendColor(json, edge.color());
            }
            json.append('}');
        }
        json.append(']');
    }

    public static void appendFaces(StringBuilder json, List<FacePayload> faces) {
        json.append('[');
        for (int i = 0; i < faces.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            appendFace(json, faces.get(i));
        }
        json.append(']');
    }

    public static void appendRepresentations(StringBuilder json, List<RepresentationPayload> representations) {
        json.append('[');
        for (int i = 0; i < representations.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            RepresentationPayload representation = representations.get(i);
            json.append('{');
            json.append("\"id\":").append(representation.id());
            json.append(",\"name\":").append(quote(representation.name()));
            json.append(",\"layers\":");
            appendStringList(json, representation.layers());
            json.append(",\"color\":");
            appendColor(json, representation.color());
            json.append(",\"edges\":");
            appendEdges(json, representation.edges());
            json.append(",\"faces\":");
            appendFaces(json, representation.faces());
            json.append('}');
        }
        json.append(']');
    }

    public static void appendInstances(StringBuilder json, List<InstancePayload> instances) {
        json.append('[');
        for (int i = 0; i < instances.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            InstancePayload instance = instances.get(i);
            json.append('{');
            json.append("\"id\":").append(quote(instance.id()));
            json.append(",\"parentId\":").append(instance.parentId() == null ? "null" : quote(instance.parentId()));
            json.append(",\"productDefinitionId\":").append(instance.productDefinitionId());
            json.append(",\"occurrenceId\":").append(instance.occurrenceId() == null ? "null" : instance.occurrenceId());
            json.append(",\"representationId\":").append(instance.representationId() == null ? "null" : instance.representationId());
            json.append(",\"representationIds\":");
            appendIntegerList(json, instance.representationIds());
            json.append(",\"label\":").append(quote(instance.label()));
            json.append(",\"description\":").append(quote(instance.description()));
            json.append(",\"localMatrix\":");
            appendMatrix(json, instance.localMatrix());
            json.append(",\"matrix\":");
            appendMatrix(json, instance.worldMatrix());
            json.append(",\"depth\":").append(instance.depth());
            json.append('}');
        }
        json.append(']');
    }

    public static void appendIntegerList(StringBuilder json, List<Integer> values) {
        json.append('[');
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            json.append(values.get(i));
        }
        json.append(']');
    }

    public static void appendStringList(StringBuilder json, List<String> values) {
        appendQuotedList(json, values);
    }

    public static void appendQuotedList(StringBuilder json, List<String> values) {
        json.append('[');
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            json.append(quote(values.get(i)));
        }
        json.append(']');
    }

    private static void appendFace(StringBuilder json, FacePayload face) {
        json.append('{');
        json.append("\"id\":").append(face.stepId());
        json.append(",\"name\":").append(quote(face.name()));
        json.append(',');
        json.append("\"surfaceType\":").append(quote(face.surfaceType()));
        json.append(',');
        json.append("\"origin\":");
        appendPoint(json, face.origin());
        json.append(",\"normal\":");
        appendVector(json, face.normal());
        json.append(",\"sameSense\":").append(face.sameSense());
        json.append(",\"color\":");
        appendColor(json, face.color());
        json.append(",\"transparency\":").append(format(face.transparency()));
        if (face.pbr() != null) {
            json.append(",\"pbr\":");
            appendPbr(json, face.pbr());
        }
        json.append(",\"layers\":");
        appendStringList(json, face.layers());
        if (face.surface() != null) {
            json.append(",\"surface\":");
            appendJsonValue(json, previewFaceSurfaceMap(face.surface()));
        }
        if (face.uvLoops() != null && !face.uvLoops().isEmpty()) {
            json.append(",\"surfaceUvLoops\":");
            appendJsonValue(json, previewUvLoopMaps(face.uvLoops()));
        }
        json.append(",\"loops\":");
        appendLoops(json, face.loops());
        json.append(",\"triangles\":");
        appendPoints(json, face.triangles());
        json.append('}');
    }

    public static void appendColor(StringBuilder json, ColorPayload color) {
        if (color == null) {
            json.append("null");
            return;
        }
        json.append('[')
                .append(color.red())
                .append(',')
                .append(color.green())
                .append(',')
                .append(color.blue())
                .append(']');
    }

    private static void appendPbr(StringBuilder json, PbrPayload pbr) {
        json.append('{');
        json.append("\"diffuse\":").append(format(pbr.diffuse()));
        json.append(",\"specular\":").append(format(pbr.specular()));
        if (pbr.specularExponent() != null) {
            json.append(",\"specularExponent\":").append(format(pbr.specularExponent()));
        }
        if (pbr.specularColor() != null) {
            json.append(",\"specularColor\":[")
                    .append(pbr.specularColor()[0]).append(',')
                    .append(pbr.specularColor()[1]).append(',')
                    .append(pbr.specularColor()[2]).append(']');
        }
        json.append('}');
    }

    private static void appendLoops(StringBuilder json, List<LoopPayload> loops) {
        json.append('[');
        for (int i = 0; i < loops.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            LoopPayload loop = loops.get(i);
            json.append('{');
            json.append("\"outer\":").append(loop.outer());
            json.append(",\"points\":");
            appendPoints(json, loop.points());
            json.append('}');
        }
        json.append(']');
    }

    public static void appendPoints(StringBuilder json, List<PointPayload> points) {
        json.append('[');
        for (int i = 0; i < points.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            appendPoint(json, points.get(i));
        }
        json.append(']');
    }

    public static void appendPoint(StringBuilder json, PointPayload point) {
        json.append('[')
                .append(format(point.x()))
                .append(',')
                .append(format(point.y()))
                .append(',')
                .append(format(point.z()))
                .append(']');
    }

    public static void appendVector(StringBuilder json, VectorPayload vector) {
        json.append('[')
                .append(format(vector.x()))
                .append(',')
                .append(format(vector.y()))
                .append(',')
                .append(format(vector.z()))
                .append(']');
    }

    public static void appendMatrix(StringBuilder json, double[] matrix) {
        json.append('[');
        for (int i = 0; i < matrix.length; i++) {
            if (i > 0) {
                json.append(',');
            }
            json.append(format(matrix[i]));
        }
        json.append(']');
    }

    // ─── String helpers ──────────────────────────────────────────────────

    public static String quote(String text) {
        StringBuilder escaped = new StringBuilder(text.length() + 16);
        escaped.append('"');
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            switch (ch) {
                case '\\' -> escaped.append("\\\\");
                case '"' -> escaped.append("\\\"");
                case '\n' -> escaped.append("\\n");
                case '\r' -> escaped.append("\\r");
                case '\t' -> escaped.append("\\t");
                case '\b' -> escaped.append("\\b");
                case '\f' -> escaped.append("\\f");
                default -> {
                    if (ch < 0x20) {
                        escaped.append(String.format("\\u%04x", (int) ch));
                    } else {
                        escaped.append(ch);
                    }
                }
            }
        }
        escaped.append('"');
        return escaped.toString();
    }

    public static String format(double value) {
        return Double.toString(value);
    }

    // ─── Preview surface/curve maps ──────────────────────────────────────

    public static Map<String, Object> previewFaceSurfaceMap(FaceSurfacePayload surface) {
        Map<String, Object> value = new LinkedHashMap<>();
        value.put("type", surface.type());
        if (surface.sourceType() != null) {
            value.put("sourceType", surface.sourceType());
        }
        if (surface.sourceStepId() != null) {
            value.put("sourceStepId", surface.sourceStepId());
        }
        if (surface.basisType() != null) {
            value.put("basisType", surface.basisType());
        }
        if (surface.basisStepId() != null) {
            value.put("basisStepId", surface.basisStepId());
        }
        if (surface.orientation() != null) {
            value.put("orientation", surface.orientation());
        }
        if (surface.offsetDistance() != null) {
            value.put("offsetDistance", surface.offsetDistance());
        }
        if (surface.trimU1() != null) {
            value.put("trimU1", surface.trimU1());
        }
        if (surface.trimU2() != null) {
            value.put("trimU2", surface.trimU2());
        }
        if (surface.trimV1() != null) {
            value.put("trimV1", surface.trimV1());
        }
        if (surface.trimV2() != null) {
            value.put("trimV2", surface.trimV2());
        }
        if (surface.implicitOuter() != null) {
            value.put("implicitOuter", surface.implicitOuter());
        }
        if (surface.transformScale() != null) {
            value.put("transformScale", surface.transformScale());
        }
        if (surface.center() != null) {
            value.put("center", surface.center());
        }
        if (surface.axis() != null) {
            value.put("axis", surface.axis());
        }
        if (surface.xDirection() != null) {
            value.put("xDirection", surface.xDirection());
        }
        value.put("radius", surface.radius());
        if (surface.minorRadius() != null) {
            value.put("minorRadius", surface.minorRadius());
        }
        if (surface.semiAngle() != null) {
            value.put("semiAngle", surface.semiAngle());
        }
        value.put("lowerHeight", surface.lowerHeight());
        value.put("upperHeight", surface.upperHeight());
        value.put("startAngle", surface.startAngle());
        value.put("sweepAngle", surface.sweepAngle());
        if (surface.uDegree() != null) {
            value.put("uDegree", surface.uDegree());
        }
        if (surface.vDegree() != null) {
            value.put("vDegree", surface.vDegree());
        }
        if (surface.controlPoints() != null) {
            value.put("controlPoints", surface.controlPoints());
        }
        if (surface.uMultiplicities() != null) {
            value.put("uMultiplicities", surface.uMultiplicities());
        }
        if (surface.vMultiplicities() != null) {
            value.put("vMultiplicities", surface.vMultiplicities());
        }
        if (surface.uKnots() != null) {
            value.put("uKnots", surface.uKnots());
        }
        if (surface.vKnots() != null) {
            value.put("vKnots", surface.vKnots());
        }
        return value;
    }

    public static List<Map<String, Object>> previewUvLoopMaps(List<ParametricLoopPayload> loops) {
        List<Map<String, Object>> values = new ArrayList<>(loops.size());
        for (ParametricLoopPayload loop : loops) {
            values.add(Map.of(
                    "outer", loop.outer(),
                    "points", loop.points().stream()
                            .map(point -> List.of(point.u(), point.v()))
                            .toList()
            ));
        }
        return List.copyOf(values);
    }

    public static Map<String, Object> previewEdgeCurveMap(EdgeCurvePayload curve) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("stepId", curve.stepId());
        map.put("type", curve.type());
        if (curve.basisType() != null) {
            map.put("basisType", curve.basisType());
        }
        if (curve.basisStepId() != null) {
            map.put("basisStepId", curve.basisStepId());
        }
        if (curve.center() != null) {
            map.put("center", curve.center());
        }
        if (curve.axis() != null) {
            map.put("axis", curve.axis());
        }
        if (curve.xDirection() != null) {
            map.put("xDirection", curve.xDirection());
        }
        if (curve.radius() != null) {
            map.put("radius", curve.radius());
        }
        if (curve.semiAxis1() != null) {
            map.put("semiAxis1", curve.semiAxis1());
        }
        if (curve.semiAxis2() != null) {
            map.put("semiAxis2", curve.semiAxis2());
        }
        if (curve.orientation() != null) {
            map.put("orientation", curve.orientation());
        }
        if (curve.senseAgreement() != null) {
            map.put("senseAgreement", curve.senseAgreement());
        }
        if (curve.offsetDistance() != null) {
            map.put("offsetDistance", curve.offsetDistance());
        }
        if (curve.selfIntersect() != null) {
            map.put("selfIntersect", curve.selfIntersect());
        }
        if (curve.refDirection() != null) {
            map.put("refDirection", curve.refDirection());
        }
        if (curve.transformScale() != null) {
            map.put("transformScale", curve.transformScale());
        }
        if (curve.masterRepresentation() != null) {
            map.put("masterRepresentation", curve.masterRepresentation());
        }
        if (curve.associatedSurfaceTypes() != null) {
            map.put("associatedSurfaceTypes", curve.associatedSurfaceTypes());
        }
        if (curve.associatedSurfaceStepIds() != null) {
            map.put("associatedSurfaceStepIds", curve.associatedSurfaceStepIds());
        }
        if (curve.sourceType() != null) {
            map.put("sourceType", curve.sourceType());
        }
        if (curve.sourceStepId() != null) {
            map.put("sourceStepId", curve.sourceStepId());
        }
        map.put("startAngle", curve.startAngle());
        map.put("sweepAngle", curve.sweepAngle());
        return map;
    }

    // ─── Utility list/map helpers ────────────────────────────────────────

    public static List<Double> pointList(PointPayload point) {
        return List.of(point.x(), point.y(), point.z());
    }

    public static List<Double> vectorList(VectorPayload vector) {
        return List.of(vector.x(), vector.y(), vector.z());
    }

    public static List<Double> gltfMatrix(double[] rowMajorMatrix) {
        return List.of(
                rowMajorMatrix[0], rowMajorMatrix[4], rowMajorMatrix[8], rowMajorMatrix[12],
                rowMajorMatrix[1], rowMajorMatrix[5], rowMajorMatrix[9], rowMajorMatrix[13],
                rowMajorMatrix[2], rowMajorMatrix[6], rowMajorMatrix[10], rowMajorMatrix[14],
                rowMajorMatrix[3], rowMajorMatrix[7], rowMajorMatrix[11], rowMajorMatrix[15]
        );
    }

    // ─── Inner classes ───────────────────────────────────────────────────

    public static final class BoundsAccumulator {
        private double minX = Double.POSITIVE_INFINITY;
        private double minY = Double.POSITIVE_INFINITY;
        private double minZ = Double.POSITIVE_INFINITY;
        private double maxX = Double.NEGATIVE_INFINITY;
        private double maxY = Double.NEGATIVE_INFINITY;
        private double maxZ = Double.NEGATIVE_INFINITY;

        public void include(PointPayload point) {
            minX = Math.min(minX, point.x());
            minY = Math.min(minY, point.y());
            minZ = Math.min(minZ, point.z());
            maxX = Math.max(maxX, point.x());
            maxY = Math.max(maxY, point.y());
            maxZ = Math.max(maxZ, point.z());
        }

        public boolean isEmpty() {
            return !Double.isFinite(minX);
        }

        public BoundsPayload toPayload() {
            if (!Double.isFinite(minX)) {
                PointPayload zero = new PointPayload(0.0, 0.0, 0.0);
                return new BoundsPayload(zero, zero);
            }
            return new BoundsPayload(new PointPayload(minX, minY, minZ), new PointPayload(maxX, maxY, maxZ));
        }
    }

    public static final class BinaryGeometryBuffer {
        private final ByteArrayOutputStream output = new ByteArrayOutputStream();
        private int pointCount;

        public PointRange append(List<PointPayload> points) {
            int offset = pointCount;
            for (PointPayload point : points) {
                writeFloatLE(output, (float) point.x());
                writeFloatLE(output, (float) point.y());
                writeFloatLE(output, (float) point.z());
                pointCount++;
            }
            return new PointRange(offset, points.size());
        }

        public int size() {
            return output.size();
        }

        public byte[] toByteArray() {
            return output.toByteArray();
        }
    }

    public static final class GlbSceneBuilder {
        private static final ColorPayload DEFAULT_FACE_COLOR = new ColorPayload(200, 122, 82);
        private static final ColorPayload DEFAULT_EDGE_COLOR = new ColorPayload(155, 133, 120);

        private final ByteArrayOutputStream binary = new ByteArrayOutputStream();
        private final List<Map<String, Object>> bufferViews = new ArrayList<>();
        private final List<Map<String, Object>> accessors = new ArrayList<>();
        private final List<Map<String, Object>> materials = new ArrayList<>();
        private final List<Map<String, Object>> meshes = new ArrayList<>();
        private final List<Map<String, Object>> nodes = new ArrayList<>();
        private final Map<String, Integer> materialCache = new LinkedHashMap<>();
        private int faceMeshCount;
        private int edgeMeshCount;
        private long faceVertexCount;
        private long faceIndexCount;
        private long lineVertexCount;
        private int maxFaceVertexCount;
        private int maxFaceIndexCount;
        private int parametricFaceCount;
        private int uvLoopFaceCount;

        public String buildJson(PreviewPayload payload) {
            boolean assemblyMode = !payload.instances().isEmpty() && !payload.representations().isEmpty();
            int rootNode = addNode("MiniCADPreview", null, List.of(), Map.of("kind", "root"), null);

            if (assemblyMode) {
                Map<Integer, RepresentationMeshes> representationMeshes = new LinkedHashMap<>();
                for (RepresentationPayload representation : payload.representations()) {
                    representationMeshes.put(representation.id(), buildRepresentationMeshes(representation));
                }
                Map<String, Integer> instanceNodes = new LinkedHashMap<>();
                for (InstancePayload instance : payload.instances()) {
                    Map<String, Object> extras = new LinkedHashMap<>();
                    extras.put("kind", "instance");
                    extras.put("instanceId", instance.id());
                    extras.put("label", instance.label());
                    extras.put("description", instance.description());
                    extras.put("depth", instance.depth());
                    extras.put("representationCount", instance.representationIds().size());
                    int instanceNode = addNode(
                            instance.label() == null || instance.label().isBlank() ? instance.id() : instance.label(),
                            null,
                            new ArrayList<>(),
                            extras,
                            gltfMatrix(instance.localMatrix())
                    );
                    instanceNodes.put(instance.id(), instanceNode);
                }
                for (InstancePayload instance : payload.instances()) {
                    int parent = instance.parentId() != null && instanceNodes.containsKey(instance.parentId())
                            ? instanceNodes.get(instance.parentId())
                            : rootNode;
                    childList(nodes.get(parent)).add(instanceNodes.get(instance.id()));
                    for (Integer representationId : instance.representationIds()) {
                        RepresentationMeshes representation = representationMeshes.get(representationId);
                        if (representation == null) {
                            continue;
                        }
                        for (FaceNode faceNode : representation.faces()) {
                            childList(nodes.get(instanceNodes.get(instance.id()))).add(addNode(
                                    faceNode.name(),
                                    faceNode.meshIndex(),
                                    List.of(),
                                    instanceFaceExtras(faceNode.face(), instance, representation.name()),
                                    null
                            ));
                        }
                        for (EdgeNode edgeNode : representation.edges()) {
                            childList(nodes.get(instanceNodes.get(instance.id()))).add(addNode(
                                    edgeNode.name(),
                                    edgeNode.meshIndex(),
                                    List.of(),
                                    instanceEdgeExtras(edgeNode.edge(), instance, representation.name()),
                                    null
                            ));
                        }
                    }
                }
            } else {
                for (FacePayload face : payload.faces()) {
                    int meshIndex = addFaceMesh(face);
                    childList(nodes.get(rootNode)).add(addNode(
                            face.name(),
                            meshIndex,
                            List.of(),
                            legacyFaceExtras(face),
                            null
                    ));
                }
                for (EdgePayload edge : payload.edges()) {
                    int meshIndex = addEdgeMesh(edge, edge.color());
                    childList(nodes.get(rootNode)).add(addNode(
                            "Edge #" + edge.stepId(),
                            meshIndex,
                            List.of(),
                            legacyEdgeExtras(edge),
                            null
                    ));
                }
            }

            Map<String, Object> scene = new LinkedHashMap<>();
            scene.put("nodes", List.of(rootNode));
            scene.put("extras", Map.of("preview", previewMetadata(payload)));

            Map<String, Object> document = new LinkedHashMap<>();
            document.put("asset", Map.of("version", "2.0", "generator", "MiniCAD"));
            document.put("scene", 0);
            document.put("scenes", List.of(scene));
            document.put("nodes", nodes);
            document.put("meshes", meshes);
            document.put("materials", materials);
            document.put("bufferViews", bufferViews);
            document.put("accessors", accessors);
            document.put("buffers", List.of(Map.of("byteLength", binary.size())));

            StringBuilder json = new StringBuilder(4096);
            appendJsonValue(json, document);
            return json.toString();
        }

        public byte[] binaryChunk() {
            return binary.toByteArray();
        }

        public int faceMeshCount() {
            return faceMeshCount;
        }

        public int edgeMeshCount() {
            return edgeMeshCount;
        }

        public int parametricFaceCount() {
            return parametricFaceCount;
        }

        public int uvLoopFaceCount() {
            return uvLoopFaceCount;
        }

        public int nodeCount() {
            return nodes.size();
        }

        public int materialCount() {
            return materials.size();
        }

        public int accessorCount() {
            return accessors.size();
        }

        public int bufferViewCount() {
            return bufferViews.size();
        }

        public long faceVertexCount() {
            return faceVertexCount;
        }

        public long faceIndexCount() {
            return faceIndexCount;
        }

        public long lineVertexCount() {
            return lineVertexCount;
        }

        public int maxFaceVertexCount() {
            return maxFaceVertexCount;
        }

        public int maxFaceIndexCount() {
            return maxFaceIndexCount;
        }

        private RepresentationMeshes buildRepresentationMeshes(RepresentationPayload representation) {
            List<FaceNode> faces = new ArrayList<>();
            for (FacePayload face : representation.faces()) {
                faces.add(new FaceNode(
                        face,
                        addFaceMesh(face),
                        face.name() == null || face.name().isBlank() ? "Face #" + face.stepId() : face.name()
                ));
            }
            List<EdgeNode> edges = new ArrayList<>();
            for (EdgePayload edge : representation.edges()) {
                edges.add(new EdgeNode(
                        edge,
                        addEdgeMesh(edge, edge.color() != null ? edge.color() : representation.color()),
                        "Edge #" + edge.stepId()
                ));
            }
            return new RepresentationMeshes(representation.name(), List.copyOf(faces), List.copyOf(edges));
        }

        private int addFaceMesh(FacePayload face) {
            IndexedTriangleMesh meshData = indexedTriangleMesh(face.triangles());
            int positionAccessor = addAccessor(meshData.positions(), true);
            int normalAccessor = addAccessor(meshData.normals(), false);
            int indexAccessor = addIndexAccessor(meshData.indices());
            int materialIndex = materialIndex(face);
            Map<String, Object> primitive = new LinkedHashMap<>();
            primitive.put("attributes", Map.of(
                    "POSITION", positionAccessor,
                    "NORMAL", normalAccessor
            ));
            primitive.put("indices", indexAccessor);
            primitive.put("material", materialIndex);
            Map<String, Object> mesh = new LinkedHashMap<>();
            mesh.put("primitives", List.of(primitive));
            meshes.add(mesh);
            faceMeshCount += 1;
            faceVertexCount += meshData.positions().count();
            faceIndexCount += meshData.indices().count();
            maxFaceVertexCount = Math.max(maxFaceVertexCount, meshData.positions().count());
            maxFaceIndexCount = Math.max(maxFaceIndexCount, meshData.indices().count());
            return meshes.size() - 1;
        }

        private int addEdgeMesh(EdgePayload edge, ColorPayload color) {
            FloatArrayData positions = floatArray(edge.points());
            int positionAccessor = addAccessor(positions, true);
            int materialIndex = materialIndex(color == null ? DEFAULT_EDGE_COLOR : color, true);
            Map<String, Object> primitive = new LinkedHashMap<>();
            primitive.put("attributes", Map.of("POSITION", positionAccessor));
            primitive.put("material", materialIndex);
            primitive.put("mode", 3);
            Map<String, Object> mesh = new LinkedHashMap<>();
            mesh.put("primitives", List.of(primitive));
            meshes.add(mesh);
            edgeMeshCount += 1;
            lineVertexCount += positions.count();
            return meshes.size() - 1;
        }

        private int addAccessor(FloatArrayData data, boolean includeBounds) {
            int byteOffset = binary.size();
            for (float value : data.values()) {
                writeFloatLE(binary, value);
            }
            Map<String, Object> bufferView = new LinkedHashMap<>();
            bufferView.put("buffer", 0);
            bufferView.put("byteOffset", byteOffset);
            bufferView.put("byteLength", data.values().length * Float.BYTES);
            bufferView.put("target", 34962);
            bufferViews.add(bufferView);

            Map<String, Object> accessor = new LinkedHashMap<>();
            accessor.put("bufferView", bufferViews.size() - 1);
            accessor.put("componentType", 5126);
            accessor.put("count", data.count());
            accessor.put("type", "VEC3");
            if (includeBounds && data.min() != null && data.max() != null) {
                accessor.put("min", List.of((double) data.min()[0], (double) data.min()[1], (double) data.min()[2]));
                accessor.put("max", List.of((double) data.max()[0], (double) data.max()[1], (double) data.max()[2]));
            }
            accessors.add(accessor);
            return accessors.size() - 1;
        }

        private int addIndexAccessor(IntArrayData data) {
            int byteOffset = binary.size();
            for (int value : data.values()) {
                writeIntLE(binary, value);
            }
            Map<String, Object> bufferView = new LinkedHashMap<>();
            bufferView.put("buffer", 0);
            bufferView.put("byteOffset", byteOffset);
            bufferView.put("byteLength", data.values().length * Integer.BYTES);
            bufferView.put("target", 34963);
            bufferViews.add(bufferView);

            Map<String, Object> accessor = new LinkedHashMap<>();
            accessor.put("bufferView", bufferViews.size() - 1);
            accessor.put("componentType", 5125);
            accessor.put("count", data.count());
            accessor.put("type", "SCALAR");
            accessors.add(accessor);
            return accessors.size() - 1;
        }

        private int materialIndex(FacePayload face) {
            ColorPayload color = face.color() != null ? face.color() : DEFAULT_FACE_COLOR;
            double alpha = 1.0 - face.transparency();
            double metallic, roughness;
            if (face.pbr() != null) {
                PbrPayload pbr = face.pbr();
                metallic = Math.sqrt(pbr.specular());
                roughness = 1.0 - pbr.diffuse();
            } else {
                metallic = 0.08;
                roughness = 0.48;
            }
            long alphaRounded = Math.round(alpha * 100);
            long metallicRounded = Math.round(metallic * 100);
            long roughnessRounded = Math.round(roughness * 100);
            String key = "f:" + color.red() + "," + color.green() + "," + color.blue()
                    + ",a" + alphaRounded + ",m" + metallicRounded + ",r" + roughnessRounded;
            Integer existing = materialCache.get(key);
            if (existing != null) {
                return existing;
            }
            Map<String, Object> gltfPbr = new LinkedHashMap<>();
            gltfPbr.put("baseColorFactor", List.of(
                    color.red() / 255.0,
                    color.green() / 255.0,
                    color.blue() / 255.0,
                    alpha
            ));
            gltfPbr.put("metallicFactor", metallic);
            gltfPbr.put("roughnessFactor", roughness);
            Map<String, Object> material = new LinkedHashMap<>();
            material.put("pbrMetallicRoughness", gltfPbr);
            material.put("doubleSided", true);
            material.put("alphaMode", "BLEND");
            materials.add(material);
            int index = materials.size() - 1;
            materialCache.put(key, index);
            return index;
        }

        private int materialIndex(ColorPayload color, boolean line) {
            String key = (line ? "line:" : "face:") + color.red() + "," + color.green() + "," + color.blue();
            Integer existing = materialCache.get(key);
            if (existing != null) {
                return existing;
            }
            Map<String, Object> pbr = new LinkedHashMap<>();
            pbr.put("baseColorFactor", List.of(
                    color.red() / 255.0,
                    color.green() / 255.0,
                    color.blue() / 255.0,
                    line ? 0.72 : 0.62
            ));
            pbr.put("metallicFactor", 0.08);
            pbr.put("roughnessFactor", 0.48);
            Map<String, Object> material = new LinkedHashMap<>();
            material.put("pbrMetallicRoughness", pbr);
            material.put("doubleSided", !line);
            material.put("alphaMode", "BLEND");
            materials.add(material);
            int index = materials.size() - 1;
            materialCache.put(key, index);
            return index;
        }

        private int addNode(String name, Integer mesh, List<Integer> children, Map<String, Object> extras, List<Double> matrix) {
            Map<String, Object> node = new LinkedHashMap<>();
            if (name != null && !name.isBlank()) {
                node.put("name", name);
            }
            if (mesh != null) {
                node.put("mesh", mesh);
            }
            if (!children.isEmpty()) {
                node.put("children", new ArrayList<>(children));
            }
            if (!extras.isEmpty()) {
                node.put("extras", extras);
            }
            if (matrix != null) {
                node.put("matrix", matrix);
            }
            nodes.add(node);
            return nodes.size() - 1;
        }

        @SuppressWarnings("unchecked")
        private List<Integer> childList(Map<String, Object> node) {
            return (List<Integer>) node.computeIfAbsent("children", ignored -> new ArrayList<Integer>());
        }

        private Map<String, Object> legacyFaceExtras(FacePayload face) {
            Map<String, Object> extras = new LinkedHashMap<>();
            extras.put("kind", "face");
            extras.put("stepId", face.stepId());
            extras.put("sameSense", face.sameSense());
            if (face.surface() != null) {
                parametricFaceCount += 1;
                extras.put("surface", faceSurfaceValue(face.surface()));
                if ("plane_face".equals(face.surface().type())) {
                    extras.put("surfaceLoops", loopValues(face.loops()));
                }
            }
            if (face.uvLoops() != null && !face.uvLoops().isEmpty()) {
                uvLoopFaceCount += 1;
                extras.put("surfaceUvLoops", uvLoopValues(face.uvLoops()));
            }
            extras.put("selection", List.of(
                    List.of("类型", "面"),
                    List.of("STEP", "#" + face.stepId()),
                    List.of("名称", face.name() == null ? "" : face.name()),
                    List.of("曲面", face.surfaceType() == null ? "PLANE" : face.surfaceType()),
                    List.of("颜色", formatColorValue(face.color())),
                    List.of("透明度", face.transparency() > 0 ? String.format("%.2f", face.transparency()) : "无"),
                    List.of("图层", formatLayersValue(face.layers())),
                    List.of("边界环", String.valueOf(face.loops().size())),
                    List.of("内环", String.valueOf(face.loops().stream().filter(loop -> !loop.outer()).count())),
                    List.of("法向", formatPointValue(vectorList(face.normal())))
            ));
            return extras;
        }

        private Map<String, Object> instanceFaceExtras(FacePayload face, InstancePayload instance, String representationName) {
            Map<String, Object> extras = legacyFaceExtras(face);
            extras.put("instanceId", instance.id());
            extras.put("selection", List.of(
                    List.of("类型", (instance.label() == null || instance.label().isBlank() ? instance.id() : instance.label()) + " / 面"),
                    List.of("STEP", "#" + face.stepId()),
                    List.of("名称", face.name() == null ? "" : face.name()),
                    List.of("曲面", face.surfaceType() == null ? "PLANE" : face.surfaceType()),
                    List.of("表示", representationName == null ? "" : representationName),
                    List.of("实例", instance.id()),
                    List.of("颜色", formatColorValue(face.color())),
                    List.of("透明度", face.transparency() > 0 ? String.format("%.2f", face.transparency()) : "无"),
                    List.of("图层", formatLayersValue(face.layers())),
                    List.of("边界环", String.valueOf(face.loops().size())),
                    List.of("内环", String.valueOf(face.loops().stream().filter(loop -> !loop.outer()).count())),
                    List.of("法向", formatPointValue(vectorList(face.normal())))
            ));
            return extras;
        }

        private Map<String, Object> legacyEdgeExtras(EdgePayload edge) {
            Map<String, Object> extras = new LinkedHashMap<>();
            extras.put("kind", "edge");
            extras.put("stepId", edge.stepId());
            if (edge.curve() != null) {
                extras.put("curve", edgeCurveValue(edge.curve()));
            }
            extras.put("selection", List.of(
                    List.of("类型", "边"),
                    List.of("STEP", "#" + edge.stepId()),
                    List.of("采样点", String.valueOf(edge.points().size())),
                    List.of("线段数", String.valueOf(Math.max(0, edge.points().size() - 1))),
                    List.of("起点", formatPointValue(pointList(edge.points().getFirst()))),
                    List.of("终点", formatPointValue(pointList(edge.points().getLast())))
            ));
            return extras;
        }

        private Map<String, Object> instanceEdgeExtras(EdgePayload edge, InstancePayload instance, String representationName) {
            Map<String, Object> extras = legacyEdgeExtras(edge);
            extras.put("instanceId", instance.id());
            extras.put("selection", List.of(
                    List.of("类型", (instance.label() == null || instance.label().isBlank() ? instance.id() : instance.label()) + " / 边"),
                    List.of("STEP", "#" + edge.stepId()),
                    List.of("表示", representationName == null ? "" : representationName),
                    List.of("实例", instance.id()),
                    List.of("采样点", String.valueOf(edge.points().size())),
                    List.of("线段数", String.valueOf(Math.max(0, edge.points().size() - 1))),
                    List.of("起点", formatPointValue(pointList(edge.points().getFirst()))),
                    List.of("终点", formatPointValue(pointList(edge.points().getLast())))
            ));
            return extras;
        }

        private String formatPointValue(List<Double> point) {
            return point.stream().map(value -> String.format("%.3f", value)).collect(Collectors.joining(", "));
        }

        private String formatColorValue(ColorPayload color) {
            if (color == null) {
                return "未指定";
            }
            return "rgb(" + color.red() + ", " + color.green() + ", " + color.blue() + ")";
        }

        private String formatLayersValue(List<String> layers) {
            return layers == null || layers.isEmpty() ? "未指定" : String.join(", ", layers);
        }

        private List<Map<String, Object>> loopValues(List<LoopPayload> loops) {
            List<Map<String, Object>> values = new ArrayList<>(loops.size());
            for (LoopPayload loop : loops) {
                values.add(Map.of(
                        "outer", loop.outer(),
                        "points", loop.points().stream().map(PreviewSerializers::pointList).toList()
                ));
            }
            return values;
        }

        private Map<String, Object> faceSurfaceValue(FaceSurfacePayload surface) {
            Map<String, Object> value = new LinkedHashMap<>();
            value.put("type", surface.type());
            if (surface.sourceType() != null) {
                value.put("sourceType", surface.sourceType());
            }
            if (surface.sourceStepId() != null) {
                value.put("sourceStepId", surface.sourceStepId());
            }
            if (surface.basisType() != null) {
                value.put("basisType", surface.basisType());
            }
            if (surface.basisStepId() != null) {
                value.put("basisStepId", surface.basisStepId());
            }
            if (surface.orientation() != null) {
                value.put("orientation", surface.orientation());
            }
            if (surface.offsetDistance() != null) {
                value.put("offsetDistance", surface.offsetDistance());
            }
            if (surface.trimU1() != null) {
                value.put("trimU1", surface.trimU1());
            }
            if (surface.trimU2() != null) {
                value.put("trimU2", surface.trimU2());
            }
            if (surface.trimV1() != null) {
                value.put("trimV1", surface.trimV1());
            }
            if (surface.trimV2() != null) {
                value.put("trimV2", surface.trimV2());
            }
            if (surface.implicitOuter() != null) {
                value.put("implicitOuter", surface.implicitOuter());
            }
            if (surface.transformScale() != null) {
                value.put("transformScale", surface.transformScale());
            }
            if (surface.center() != null) {
                value.put("center", surface.center());
            }
            if (surface.axis() != null) {
                value.put("axis", surface.axis());
            }
            if (surface.xDirection() != null) {
                value.put("xDirection", surface.xDirection());
            }
            value.put("radius", surface.radius());
            if (surface.minorRadius() != null) {
                value.put("minorRadius", surface.minorRadius());
            }
            if (surface.semiAngle() != null) {
                value.put("semiAngle", surface.semiAngle());
            }
            value.put("lowerHeight", surface.lowerHeight());
            value.put("upperHeight", surface.upperHeight());
            value.put("startAngle", surface.startAngle());
            value.put("sweepAngle", surface.sweepAngle());
            if (surface.uDegree() != null) {
                value.put("uDegree", surface.uDegree());
            }
            if (surface.vDegree() != null) {
                value.put("vDegree", surface.vDegree());
            }
            if (surface.controlPoints() != null) {
                value.put("controlPoints", surface.controlPoints());
            }
            if (surface.uMultiplicities() != null) {
                value.put("uMultiplicities", surface.uMultiplicities());
            }
            if (surface.vMultiplicities() != null) {
                value.put("vMultiplicities", surface.vMultiplicities());
            }
            if (surface.uKnots() != null) {
                value.put("uKnots", surface.uKnots());
            }
            if (surface.vKnots() != null) {
                value.put("vKnots", surface.vKnots());
            }
            return value;
        }

        private List<Map<String, Object>> uvLoopValues(List<ParametricLoopPayload> loops) {
            List<Map<String, Object>> values = new ArrayList<>(loops.size());
            for (ParametricLoopPayload loop : loops) {
                values.add(Map.of(
                        "outer", loop.outer(),
                        "points", loop.points().stream()
                                .map(point -> List.of(point.u(), point.v()))
                                .toList()
                ));
            }
            return values;
        }

        private Map<String, Object> edgeCurveValue(EdgeCurvePayload curve) {
            Map<String, Object> value = new LinkedHashMap<>();
            value.put("stepId", curve.stepId());
            value.put("type", curve.type());
            if (curve.basisType() != null) {
                value.put("basisType", curve.basisType());
            }
            if (curve.basisStepId() != null) {
                value.put("basisStepId", curve.basisStepId());
            }
            value.put("center", curve.center());
            value.put("axis", curve.axis());
            value.put("xDirection", curve.xDirection());
            value.put("startAngle", curve.startAngle());
            value.put("sweepAngle", curve.sweepAngle());
            if (curve.radius() != null) {
                value.put("radius", curve.radius());
            }
            if (curve.semiAxis1() != null) {
                value.put("semiAxis1", curve.semiAxis1());
            }
            if (curve.semiAxis2() != null) {
                value.put("semiAxis2", curve.semiAxis2());
            }
            if (curve.orientation() != null) {
                value.put("orientation", curve.orientation());
            }
            if (curve.senseAgreement() != null) {
                value.put("senseAgreement", curve.senseAgreement());
            }
            if (curve.offsetDistance() != null) {
                value.put("offsetDistance", curve.offsetDistance());
            }
            if (curve.selfIntersect() != null) {
                value.put("selfIntersect", curve.selfIntersect());
            }
            if (curve.refDirection() != null) {
                value.put("refDirection", curve.refDirection());
            }
            if (curve.transformScale() != null) {
                value.put("transformScale", curve.transformScale());
            }
            if (curve.masterRepresentation() != null) {
                value.put("masterRepresentation", curve.masterRepresentation());
            }
            if (curve.associatedSurfaceTypes() != null) {
                value.put("associatedSurfaceTypes", curve.associatedSurfaceTypes());
            }
            if (curve.associatedSurfaceStepIds() != null) {
                value.put("associatedSurfaceStepIds", curve.associatedSurfaceStepIds());
            }
            if (curve.sourceType() != null) {
                value.put("sourceType", curve.sourceType());
            }
            if (curve.sourceStepId() != null) {
                value.put("sourceStepId", curve.sourceStepId());
            }
            return value;
        }

        private FloatArrayData floatArray(List<PointPayload> points) {
            float[] values = new float[points.size() * 3];
            float[] min = new float[]{Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY};
            float[] max = new float[]{Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
            int index = 0;
            for (PointPayload point : points) {
                values[index++] = (float) point.x();
                values[index++] = (float) point.y();
                values[index++] = (float) point.z();
                min[0] = Math.min(min[0], (float) point.x());
                min[1] = Math.min(min[1], (float) point.y());
                min[2] = Math.min(min[2], (float) point.z());
                max[0] = Math.max(max[0], (float) point.x());
                max[1] = Math.max(max[1], (float) point.y());
                max[2] = Math.max(max[2], (float) point.z());
            }
            return new FloatArrayData(values, points.size(), min, max);
        }

        private FloatArrayData triangleNormals(List<PointPayload> triangles) {
            float[] values = new float[triangles.size() * 3];
            for (int i = 0; i + 2 < triangles.size(); i += 3) {
                PointPayload a = triangles.get(i);
                PointPayload b = triangles.get(i + 1);
                PointPayload c = triangles.get(i + 2);
                double abx = b.x() - a.x();
                double aby = b.y() - a.y();
                double abz = b.z() - a.z();
                double acx = c.x() - a.x();
                double acy = c.y() - a.y();
                double acz = c.z() - a.z();
                double nx = aby * acz - abz * acy;
                double ny = abz * acx - abx * acz;
                double nz = abx * acy - aby * acx;
                double norm = Math.sqrt(nx * nx + ny * ny + nz * nz);
                if (norm <= Epsilon.EPS) {
                    nx = 0.0;
                    ny = 0.0;
                    nz = 1.0;
                } else {
                    nx /= norm;
                    ny /= norm;
                    nz /= norm;
                }
                for (int vertex = 0; vertex < 3; vertex++) {
                    int base = (i + vertex) * 3;
                    values[base] = (float) nx;
                    values[base + 1] = (float) ny;
                    values[base + 2] = (float) nz;
                }
            }
            return new FloatArrayData(values, triangles.size(), null, null);
        }

        private IndexedTriangleMesh indexedTriangleMesh(List<PointPayload> triangles) {
            Map<PointPayload, Integer> indexByPoint = new LinkedHashMap<>();
            List<PointPayload> uniquePoints = new ArrayList<>();
            List<Integer> indices = new ArrayList<>(triangles.size());
            List<double[]> normalSums = new ArrayList<>();

            for (int i = 0; i + 2 < triangles.size(); i += 3) {
                PointPayload a = triangles.get(i);
                PointPayload b = triangles.get(i + 1);
                PointPayload c = triangles.get(i + 2);
                double abx = b.x() - a.x();
                double aby = b.y() - a.y();
                double abz = b.z() - a.z();
                double acx = c.x() - a.x();
                double acy = c.y() - a.y();
                double acz = c.z() - a.z();
                double nx = aby * acz - abz * acy;
                double ny = abz * acx - abx * acz;
                double nz = abx * acy - aby * acx;
                double norm = Math.sqrt(nx * nx + ny * ny + nz * nz);
                if (norm <= Epsilon.EPS) {
                    nx = 0.0;
                    ny = 0.0;
                    nz = 1.0;
                } else {
                    nx /= norm;
                    ny /= norm;
                    nz /= norm;
                }

                for (PointPayload point : List.of(a, b, c)) {
                    Integer existing = indexByPoint.get(point);
                    int index;
                    if (existing == null) {
                        index = uniquePoints.size();
                        indexByPoint.put(point, index);
                        uniquePoints.add(point);
                        normalSums.add(new double[]{0.0, 0.0, 0.0});
                    } else {
                        index = existing;
                    }
                    double[] normal = normalSums.get(index);
                    normal[0] += nx;
                    normal[1] += ny;
                    normal[2] += nz;
                    indices.add(index);
                }
            }

            float[] normalValues = new float[uniquePoints.size() * 3];
            for (int index = 0; index < uniquePoints.size(); index++) {
                double[] normal = normalSums.get(index);
                double norm = Math.sqrt(normal[0] * normal[0] + normal[1] * normal[1] + normal[2] * normal[2]);
                int base = index * 3;
                if (norm <= Epsilon.EPS) {
                    normalValues[base] = 0.0f;
                    normalValues[base + 1] = 0.0f;
                    normalValues[base + 2] = 1.0f;
                } else {
                    normalValues[base] = (float) (normal[0] / norm);
                    normalValues[base + 1] = (float) (normal[1] / norm);
                    normalValues[base + 2] = (float) (normal[2] / norm);
                }
            }

            int[] indexValues = new int[indices.size()];
            for (int index = 0; index < indices.size(); index++) {
                indexValues[index] = indices.get(index);
            }
            return new IndexedTriangleMesh(
                    floatArray(uniquePoints),
                    new FloatArrayData(normalValues, uniquePoints.size(), null, null),
                    new IntArrayData(indexValues, indexValues.length)
            );
        }
    }
}
