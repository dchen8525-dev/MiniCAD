package com.minicad.export.json;

import com.minicad.common.Epsilon;
import com.minicad.common.MiniCadIssue;
import com.minicad.helper.ProductMetadataExtractor;
import com.minicad.helper.UnitExtractor;
import com.minicad.preview.builder.PmiPayload;
import com.minicad.preview.builder.PmiTargetPayload;
import com.minicad.preview.payload.BoundsPayload;
import com.minicad.preview.payload.BinaryEdgePayload;
import com.minicad.preview.payload.BinaryFacePayload;
import com.minicad.preview.payload.BinaryLoopPayload;
import com.minicad.preview.payload.BinaryPreviewPayload;
import com.minicad.preview.payload.BinaryRepresentationPayload;
import com.minicad.preview.payload.ColorPayload;
import com.minicad.preview.payload.EdgeCurvePayload;
import com.minicad.preview.payload.EdgePayload;
import com.minicad.preview.payload.FacePayload;
import com.minicad.preview.payload.FaceSurfacePayload;
import com.minicad.preview.payload.InstancePayload;
import com.minicad.preview.payload.LoopPayload;
import com.minicad.preview.payload.ParametricLoopPayload;
import com.minicad.preview.payload.PbrPayload;
import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.payload.PreviewPayload;
import com.minicad.preview.payload.PreviewStats;
import com.minicad.preview.payload.RepresentationPayload;
import com.minicad.preview.payload.UnsupportedBooleanPayload;
import com.minicad.preview.payload.UnsupportedFacePayload;
import com.minicad.preview.payload.ValidationCheckPayload;
import com.minicad.preview.payload.ValidationPayload;
import com.minicad.preview.payload.ValidationReportPayload;
import com.minicad.preview.payload.VectorPayload;
import com.minicad.export.glb.PreviewGlbBuilder;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serialization utilities for preview payloads: JSON, binary geometry,
 * and GLB output. Extracted from StepPreviewJsonExporter.
 */
public final class PreviewSerializers {

    private static final int GLB_MAGIC = 0x46546C67;
    private static final int GLB_VERSION = 2;
    private static final int GLB_JSON_CHUNK_TYPE = 0x4E4F534A;
    private static final int GLB_BIN_CHUNK_TYPE = 0x004E4942;

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
        json.append(",\"issues\":");
        appendIssues(json, payload.issues());
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
        PreviewGlbBuilder builder = new PreviewGlbBuilder();
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
        byte[] glb = output.toByteArray();
        validateGlb(glb);
        return glb;
    }

    public static void validateGlb(byte[] glb) {
        if (glb == null) {
            throw new IllegalArgumentException("GLB payload must not be null");
        }
        if (glb.length < 28) {
            throw new IllegalArgumentException("GLB payload is too short: " + glb.length);
        }
        if (glb.length % 4 != 0) {
            throw new IllegalArgumentException("GLB payload length must be 4-byte aligned: " + glb.length);
        }
        ByteBuffer buffer = ByteBuffer.wrap(glb).order(ByteOrder.LITTLE_ENDIAN);
        int magic = buffer.getInt(0);
        if (magic != GLB_MAGIC) {
            throw new IllegalArgumentException("invalid GLB magic: 0x" + Integer.toHexString(magic));
        }
        int version = buffer.getInt(4);
        if (version != GLB_VERSION) {
            throw new IllegalArgumentException("unsupported GLB version: " + version);
        }
        int totalLength = buffer.getInt(8);
        if (totalLength != glb.length) {
            throw new IllegalArgumentException("GLB length header " + totalLength + " does not match payload length " + glb.length);
        }
        int offset = 12;
        offset = validateChunk(buffer, glb.length, offset, GLB_JSON_CHUNK_TYPE, "JSON");
        if (offset == glb.length) {
            return;
        }
        offset = validateChunk(buffer, glb.length, offset, GLB_BIN_CHUNK_TYPE, "BIN");
        if (offset != glb.length) {
            throw new IllegalArgumentException("GLB contains trailing bytes after BIN chunk: " + (glb.length - offset));
        }
    }

    private static int validateChunk(ByteBuffer buffer, int totalLength, int offset, int expectedType, String label) {
        if (offset + 8 > totalLength) {
            throw new IllegalArgumentException("GLB " + label + " chunk header is truncated");
        }
        int chunkLength = buffer.getInt(offset);
        int chunkType = buffer.getInt(offset + 4);
        if (chunkType != expectedType) {
            throw new IllegalArgumentException("GLB " + label + " chunk type mismatch: 0x" + Integer.toHexString(chunkType));
        }
        if (chunkLength < 0) {
            throw new IllegalArgumentException("GLB " + label + " chunk length is negative: " + chunkLength);
        }
        if (chunkLength % 4 != 0) {
            throw new IllegalArgumentException("GLB " + label + " chunk length must be 4-byte aligned: " + chunkLength);
        }
        long nextOffset = (long) offset + 8L + chunkLength;
        if (nextOffset > totalLength) {
            throw new IllegalArgumentException("GLB " + label + " chunk extends beyond payload length");
        }
        return (int) nextOffset;
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
                payload.issues(),
                payload.unsupportedBooleans(),
                payload.unsupportedFaces(),
                payload.edges().stream().map(edge -> toBinaryEdge(edge, geometry)).collect(Collectors.toList()),
                payload.faces().stream().map(face -> toBinaryFace(face, geometry)).collect(Collectors.toList()),
                payload.representations().stream().map(representation -> new BinaryRepresentationPayload(
                        representation.id(),
                        representation.name(),
                        representation.layers(),
                        representation.color(),
                        representation.edges().stream().map(edge -> toBinaryEdge(edge, geometry)).collect(Collectors.toList()),
                        representation.faces().stream().map(face -> toBinaryFace(face, geometry)).collect(Collectors.toList())
                )).collect(Collectors.toList()),
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
                .collect(Collectors.toList());
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
        json.append(",\"issues\":");
        appendIssues(json, payload.issues());
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
        preview.put("issues", issueMaps(payload.issues()));
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
        if (value instanceof String) {
            String text = (String) value;
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
        if (value instanceof Map<?, ?>) {
            json.append('{');
            boolean first = true;
            for (Map.Entry<?, ?> entry : stableEntries((Map<?, ?>) value)) {
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
        if (value instanceof List<?>) {
            List<?> list = (List<?>) value;
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

    /**
     * Entries of {@code map} in an order that is stable across JVM runs.
     *
     * {@code Map.of(...)} / {@code Map.ofEntries(...)} return JDK immutable maps
     * whose iteration order is deliberately randomised per JVM run
     * (see {@code java.util.ImmutableCollections#SALT32L}). Serialising one
     * directly therefore yields a different but equivalent JSON document on
     * every run, which breaks any digest pinned over the export. Sorting the
     * entries by their rendered key removes that instability at the single
     * point where maps are written, so no call site can reintroduce it.
     *
     * Insertion-ordered maps (LinkedHashMap, the convention used by every map
     * builder in this class) are passed through untouched so their intended
     * field order is preserved.
     */
    private static List<Map.Entry<?, ?>> stableEntries(Map<?, ?> map) {
        List<Map.Entry<?, ?>> entries = new ArrayList<>(map.entrySet());
        if (!map.getClass().getName().startsWith("java.util.ImmutableCollections$")) {
            return entries;
        }
        entries.sort(Comparator.comparing(e -> String.valueOf(e.getKey())));
        return entries;
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
            map.put("leader", item.leader().stream().map(PreviewSerializers::pointList).collect(Collectors.toList()));
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
            }).collect(Collectors.toList()));
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

    private static List<Map<String, Object>> issueMaps(List<MiniCadIssue> issues) {
        List<Map<String, Object>> list = new ArrayList<>(issues.size());
        for (MiniCadIssue issue : issues) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("severity", issue.severity().name());
            map.put("code", issue.code());
            if (issue.entityId() != null) {
                map.put("entityId", issue.entityId());
            }
            if (issue.entityType() != null) {
                map.put("entityType", issue.entityType());
            }
            map.put("message", issue.message());
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

    public static void appendIssues(StringBuilder json, List<MiniCadIssue> issues) {
        json.append('[');
        for (int i = 0; i < issues.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            MiniCadIssue issue = issues.get(i);
            json.append('{');
            json.append("\"severity\":").append(quote(issue.severity().name()));
            json.append(",\"code\":").append(quote(issue.code()));
            if (issue.entityId() != null) {
                json.append(",\"entityId\":").append(issue.entityId());
            }
            if (issue.entityType() != null) {
                json.append(",\"entityType\":").append(quote(issue.entityType()));
            }
            json.append(",\"message\":").append(quote(issue.message()));
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
            if (ch == '\\') {
                escaped.append("\\\\");
            } else if (ch == '"') {
                escaped.append("\\\"");
            } else if (ch == '\n') {
                escaped.append("\\n");
            } else if (ch == '\r') {
                escaped.append("\\r");
            } else if (ch == '\t') {
                escaped.append("\\t");
            } else if (ch == '\b') {
                escaped.append("\\b");
            } else if (ch == '\f') {
                escaped.append("\\f");
            } else if (ch < 0x20) {
                escaped.append(String.format("\\u%04x", (int) ch));
            } else {
                escaped.append(ch);
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
            // Insertion-ordered on purpose: Map.of() iterates its entries in an
            // order that is randomised per JVM run, which makes the serialised
            // JSON (and any digest pinned over it) unstable across runs.
            Map<String, Object> value = new LinkedHashMap<>();
            value.put("outer", loop.outer());
            value.put("points", loop.points().stream()
                    .map(point -> List.of(point.u(), point.v()))
                    .collect(Collectors.toList()));
            values.add(value);
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
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;

        public double minX() { return minX; }
        public double minY() { return minY; }
        public double minZ() { return minZ; }
        public double maxX() { return maxX; }
        public double maxY() { return maxY; }
        public double maxZ() { return maxZ; }

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

    public static final class PointRange {
        private final int offset;
        private final int count;

        public PointRange(int offset, int count) {
            this.offset = offset;
            this.count = count;
        }

        public int offset() { return offset; }
        public int count() { return count; }
    }
}
