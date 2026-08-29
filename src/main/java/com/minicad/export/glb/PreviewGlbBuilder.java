package com.minicad.export.glb;

import com.minicad.common.Epsilon;
import com.minicad.export.json.PreviewSerializers;
import com.minicad.preview.payload.ColorPayload;
import com.minicad.preview.payload.EdgePayload;
import com.minicad.preview.payload.EdgeCurvePayload;
import com.minicad.preview.payload.FacePayload;
import com.minicad.preview.payload.FaceSurfacePayload;
import com.minicad.preview.payload.InstancePayload;
import com.minicad.preview.payload.LoopPayload;
import com.minicad.preview.payload.ParametricLoopPayload;
import com.minicad.preview.payload.PbrPayload;
import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.payload.PreviewPayload;
import com.minicad.preview.payload.RepresentationPayload;
import com.minicad.preview.payload.RepresentationMeshes;
import com.minicad.preview.payload.FaceNode;
import com.minicad.preview.payload.EdgeNode;
import com.minicad.preview.payload.FloatArrayData;
import com.minicad.preview.payload.IntArrayData;
import com.minicad.preview.payload.IndexedTriangleMesh;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GLB scene builder extracted from PreviewSerializers.
 * Builds glTF 2.0 binary geometry for preview payloads.
 */
public final class PreviewGlbBuilder {

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

    public PreviewGlbBuilder() {}

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
                        PreviewSerializers.gltfMatrix(instance.localMatrix())
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
        scene.put("extras", Map.of("preview", PreviewSerializers.previewMetadata(payload)));

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
        PreviewSerializers.appendJsonValue(json, document);
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
        validateIndexedTriangleMesh(face.stepId(), meshData);
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
            PreviewSerializers.writeFloatLE(binary, value);
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
        // Bounds of an empty mesh accumulate to +/-Infinity, which is not valid
        // JSON and is rejected by GLTFLoader; glTF allows omitting min/max.
        if (includeBounds && data.count() > 0 && data.min() != null && data.max() != null) {
            accessor.put("min", List.of((double) data.min()[0], (double) data.min()[1], (double) data.min()[2]));
            accessor.put("max", List.of((double) data.max()[0], (double) data.max()[1], (double) data.max()[2]));
        }
        accessors.add(accessor);
        return accessors.size() - 1;
    }

    private int addIndexAccessor(IntArrayData data) {
        int byteOffset = binary.size();
        for (int value : data.values()) {
            PreviewSerializers.writeIntLE(binary, value);
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
                List.of("法向", formatPointValue(PreviewSerializers.vectorList(face.normal())))
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
                List.of("法向", formatPointValue(PreviewSerializers.vectorList(face.normal())))
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
                List.of("起点", formatPointValue(PreviewSerializers.pointList(edge.points().get(0)))),
                List.of("终点", formatPointValue(PreviewSerializers.pointList(edge.points().get(edge.points().size() - 1))))
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
                List.of("起点", formatPointValue(PreviewSerializers.pointList(edge.points().get(0)))),
                List.of("终点", formatPointValue(PreviewSerializers.pointList(edge.points().get(edge.points().size() - 1))))
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
                    "points", loop.points().stream().map(PreviewSerializers::pointList).collect(Collectors.toList())
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
                            .collect(Collectors.toList())
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

    private static void validateIndexedTriangleMesh(int stepId, IndexedTriangleMesh meshData) {
        int vertexCount = meshData.positions().count();
        int normalCount = meshData.normals().count();
        int indexCount = meshData.indices().count();
        if (vertexCount == 0 && normalCount == 0 && indexCount == 0) {
            return;
        }
        if (vertexCount <= 0) {
            throw new IllegalArgumentException("face #" + stepId + " generated invalid GLB vertices");
        }
        if (meshData.positions().values().length != vertexCount * 3) {
            throw new IllegalArgumentException("face #" + stepId + " position buffer does not match vertex count");
        }
        for (float position : meshData.positions().values()) {
            if (!Float.isFinite(position)) {
                throw new IllegalArgumentException("face #" + stepId + " GLB position contains a non-finite value");
            }
        }
        if (normalCount != vertexCount || meshData.normals().values().length != vertexCount * 3) {
            throw new IllegalArgumentException("face #" + stepId + " normal buffer does not match vertex count");
        }
        if (indexCount <= 0 || indexCount % 3 != 0) {
            throw new IllegalArgumentException("face #" + stepId + " index buffer is not made of triangles");
        }
        for (int index : meshData.indices().values()) {
            if (index < 0 || index >= vertexCount) {
                throw new IllegalArgumentException("face #" + stepId + " GLB index " + index
                        + " is outside vertex count " + vertexCount);
            }
        }
        float[] normals = meshData.normals().values();
        for (int i = 0; i < normals.length; i += 3) {
            double nx = normals[i];
            double ny = normals[i + 1];
            double nz = normals[i + 2];
            double length = Math.sqrt(nx * nx + ny * ny + nz * nz);
            if (!Float.isFinite(normals[i]) || !Float.isFinite(normals[i + 1]) || !Float.isFinite(normals[i + 2])
                    || !Double.isFinite(length) || Math.abs(length - 1.0) > 1.0e-4) {
                throw new IllegalArgumentException("face #" + stepId + " GLB normal at vertex "
                        + (i / 3) + " is not normalized");
            }
        }
    }
}
