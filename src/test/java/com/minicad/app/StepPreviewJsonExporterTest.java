package com.minicad.app;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StepPreviewJsonExporterTest {

    @Test
    void shouldExportSameGlbFromCompiledDocument() {
        CompiledStepDocument compiled = CompiledStepDocument.compile(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#10=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#11=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_3D('AXIS',#1,#10,#11);\n"
        + "#13=PLANE('PL0',#12);\n"
        + "#20=VERTEX_POINT('V0',#1);\n"
        + "#21=VERTEX_POINT('V1',#2);\n"
        + "#22=VERTEX_POINT('V2',#3);\n"
        + "#23=VERTEX_POINT('V3',#4);\n"
        + "#30=DIRECTION('D1',(1.0,0.0,0.0));\n"
        + "#31=VECTOR('VE1',#30,1.0);\n"
        + "#32=LINE('L1',#1,#31);\n"
        + "#33=DIRECTION('D2',(0.0,1.0,0.0));\n"
        + "#34=VECTOR('VE2',#33,1.0);\n"
        + "#35=LINE('L2',#2,#34);\n"
        + "#36=DIRECTION('D3',(-1.0,0.0,0.0));\n"
        + "#37=VECTOR('VE3',#36,1.0);\n"
        + "#38=LINE('L3',#3,#37);\n"
        + "#39=DIRECTION('D4',(0.0,-1.0,0.0));\n"
        + "#40=VECTOR('VE4',#39,1.0);\n"
        + "#41=LINE('L4',#4,#40);\n"
        + "#50=EDGE_CURVE('E1',#20,#21,#32,.T.);\n"
        + "#51=EDGE_CURVE('E2',#21,#22,#35,.T.);\n"
        + "#52=EDGE_CURVE('E3',#22,#23,#38,.T.);\n"
        + "#53=EDGE_CURVE('E4',#23,#20,#41,.T.);\n"
        + "#60=ORIENTED_EDGE('OE1',$,$,#50,.T.);\n"
        + "#61=ORIENTED_EDGE('OE2',$,$,#51,.T.);\n"
        + "#62=ORIENTED_EDGE('OE3',$,$,#52,.T.);\n"
        + "#63=ORIENTED_EDGE('OE4',$,$,#53,.T.);\n"
        + "#70=EDGE_LOOP('LOOP',(#60,#61,#62,#63));\n"
        + "#71=FACE_OUTER_BOUND('FOB',#70,.T.);\n"
        + "#80=ADVANCED_FACE('F0',(#71),#13,.T.);\n"
        + "#90=CLOSED_SHELL('CS',(#80));\n"
        + "#100=MANIFOLD_SOLID_BREP('S0',#90);\n"
        + "ENDSEC;"
        );

        byte[] direct = StepPreviewJsonExporter.exportGlb(compiled.stepText());
        byte[] compiledBinary = StepPreviewJsonExporter.exportGlb(compiled);

        assertArrayEquals(direct, compiledBinary);
    }

    @Test
    void shouldExportGlbPreviewPacketForMinimalSquare() {
        byte[] binary = StepPreviewJsonExporter.exportGlb(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#10=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#11=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_3D('AXIS',#1,#10,#11);\n"
        + "#13=PLANE('PL0',#12);\n"
        + "#20=VERTEX_POINT('V0',#1);\n"
        + "#21=VERTEX_POINT('V1',#2);\n"
        + "#22=VERTEX_POINT('V2',#3);\n"
        + "#23=VERTEX_POINT('V3',#4);\n"
        + "#30=DIRECTION('D1',(1.0,0.0,0.0));\n"
        + "#31=VECTOR('VE1',#30,1.0);\n"
        + "#32=LINE('L1',#1,#31);\n"
        + "#33=DIRECTION('D2',(0.0,1.0,0.0));\n"
        + "#34=VECTOR('VE2',#33,1.0);\n"
        + "#35=LINE('L2',#2,#34);\n"
        + "#36=DIRECTION('D3',(-1.0,0.0,0.0));\n"
        + "#37=VECTOR('VE3',#36,1.0);\n"
        + "#38=LINE('L3',#3,#37);\n"
        + "#39=DIRECTION('D4',(0.0,-1.0,0.0));\n"
        + "#40=VECTOR('VE4',#39,1.0);\n"
        + "#41=LINE('L4',#4,#40);\n"
        + "#50=EDGE_CURVE('E1',#20,#21,#32,.T.);\n"
        + "#51=EDGE_CURVE('E2',#21,#22,#35,.T.);\n"
        + "#52=EDGE_CURVE('E3',#22,#23,#38,.T.);\n"
        + "#53=EDGE_CURVE('E4',#23,#20,#41,.T.);\n"
        + "#60=ORIENTED_EDGE('OE1',$,$,#50,.T.);\n"
        + "#61=ORIENTED_EDGE('OE2',$,$,#51,.T.);\n"
        + "#62=ORIENTED_EDGE('OE3',$,$,#52,.T.);\n"
        + "#63=ORIENTED_EDGE('OE4',$,$,#53,.T.);\n"
        + "#70=EDGE_LOOP('LOOP',(#60,#61,#62,#63));\n"
        + "#71=FACE_OUTER_BOUND('FOB',#70,.T.);\n"
        + "#80=ADVANCED_FACE('F0',(#71),#13,.T.);\n"
        + "#90=CLOSED_SHELL('CS',(#80));\n"
        + "#100=MANIFOLD_SOLID_BREP('S0',#90);\n"
        + "ENDSEC;"
        );

        assertTrue(binary.length > 16);
        assertEquals('g', binary[0]);
        assertEquals('l', binary[1]);
        assertEquals('T', binary[2]);
        assertEquals('F', binary[3]);

        ByteBuffer header = ByteBuffer.wrap(binary).order(ByteOrder.LITTLE_ENDIAN);
        assertEquals(2, header.getInt(4));
        int totalLength = header.getInt(8);
        int jsonChunkLength = header.getInt(12);
        int jsonChunkType = header.getInt(16);
        String metadata = metadataFromGlb(binary);

        PreviewSerializers.validateGlb(binary);
        assertEquals(binary.length, totalLength);
        assertEquals(0x4E4F534A, jsonChunkType);
        assertMetadataContains(metadata,
                "\"version\":\"2.0\"",
                "\"scenes\":[",
                "\"preview\":{",
                "\"faceCount\":1",
                "\"edgeCount\":4",
                "\"surface\":{\"type\":\"plane_face\"",
                "\"surfaceLoops\":[",
                "\"meshes\":[",
                "\"materials\":[");
    }

    @Test
    void glbValidatorShouldRejectMalformedHeadersAndChunks() {
        byte[] binary = StepPreviewJsonExporter.exportGlb(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#10=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#11=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_3D('AXIS',#1,#10,#11);\n"
        + "#13=PLANE('PL0',#12);\n"
        + "#20=VERTEX_POINT('V0',#1);\n"
        + "#21=VERTEX_POINT('V1',#2);\n"
        + "#22=VERTEX_POINT('V2',#3);\n"
        + "#23=VERTEX_POINT('V3',#4);\n"
        + "#30=DIRECTION('D1',(1.0,0.0,0.0));\n"
        + "#31=VECTOR('VE1',#30,1.0);\n"
        + "#32=LINE('L1',#1,#31);\n"
        + "#33=DIRECTION('D2',(0.0,1.0,0.0));\n"
        + "#34=VECTOR('VE2',#33,1.0);\n"
        + "#35=LINE('L2',#2,#34);\n"
        + "#36=DIRECTION('D3',(-1.0,0.0,0.0));\n"
        + "#37=VECTOR('VE3',#36,1.0);\n"
        + "#38=LINE('L3',#3,#37);\n"
        + "#39=DIRECTION('D4',(0.0,-1.0,0.0));\n"
        + "#40=VECTOR('VE4',#39,1.0);\n"
        + "#41=LINE('L4',#4,#40);\n"
        + "#50=EDGE_CURVE('E1',#20,#21,#32,.T.);\n"
        + "#51=EDGE_CURVE('E2',#21,#22,#35,.T.);\n"
        + "#52=EDGE_CURVE('E3',#22,#23,#38,.T.);\n"
        + "#53=EDGE_CURVE('E4',#23,#20,#41,.T.);\n"
        + "#60=ORIENTED_EDGE('OE1',$,$,#50,.T.);\n"
        + "#61=ORIENTED_EDGE('OE2',$,$,#51,.T.);\n"
        + "#62=ORIENTED_EDGE('OE3',$,$,#52,.T.);\n"
        + "#63=ORIENTED_EDGE('OE4',$,$,#53,.T.);\n"
        + "#70=EDGE_LOOP('LOOP',(#60,#61,#62,#63));\n"
        + "#71=FACE_OUTER_BOUND('FOB',#70,.T.);\n"
        + "#80=ADVANCED_FACE('F0',(#71),#13,.T.);\n"
        + "#90=CLOSED_SHELL('CS',(#80));\n"
        + "#100=MANIFOLD_SOLID_BREP('S0',#90);\n"
        + "ENDSEC;"
        );
        PreviewSerializers.validateGlb(binary);

        byte[] badLength = binary.clone();
        writeIntLE(badLength, 8, binary.length + 4);
        assertThrows(IllegalArgumentException.class, () -> PreviewSerializers.validateGlb(badLength));

        byte[] badJsonType = binary.clone();
        writeIntLE(badJsonType, 16, 0x004E4942);
        assertThrows(IllegalArgumentException.class, () -> PreviewSerializers.validateGlb(badJsonType));

        byte[] badJsonLength = binary.clone();
        writeIntLE(badJsonLength, 12, binary.length);
        assertThrows(IllegalArgumentException.class, () -> PreviewSerializers.validateGlb(badJsonLength));
    }

    @Test
    void glbMeshesShouldIncludeNormalizedNormalsMatchingPositions() {
        byte[] binary = PreviewSerializers.toGlb(nonEmptyTrianglePreviewPayload());

        JSONObject gltf = JSONObject.parseObject(metadataFromGlb(binary));
        JSONObject primitive = gltf.getJSONArray("meshes")
                .getJSONObject(0)
                .getJSONArray("primitives")
                .getJSONObject(0);
        JSONObject attributes = primitive.getJSONObject("attributes");
        Integer positionAccessorIndex = attributes.getInteger("POSITION");
        Integer normalAccessorIndex = attributes.getInteger("NORMAL");
        assertNotNull(positionAccessorIndex);
        assertNotNull(normalAccessorIndex);

        JSONObject positionAccessor = gltf.getJSONArray("accessors").getJSONObject(positionAccessorIndex);
        JSONObject normalAccessor = gltf.getJSONArray("accessors").getJSONObject(normalAccessorIndex);
        assertEquals(positionAccessor.getIntValue("count"), normalAccessor.getIntValue("count"));
        assertEquals("VEC3", normalAccessor.getString("type"));
        assertEquals(5126, normalAccessor.getIntValue("componentType"));

        ByteBuffer bin = glbBinChunk(binary).order(ByteOrder.LITTLE_ENDIAN);
        float[] normals = readVec3Accessor(bin, gltf, normalAccessor);
        for (int i = 0; i < normals.length; i += 3) {
            double nx = normals[i];
            double ny = normals[i + 1];
            double nz = normals[i + 2];
            double length = Math.sqrt(nx * nx + ny * ny + nz * nz);
            assertEquals(1.0, length, 1.0e-5, "normal at vertex " + (i / 3) + " must be normalized");
        }

        JSONArray indices = gltf.getJSONArray("accessors");
        JSONObject indexAccessor = indices.getJSONObject(primitive.getIntValue("indices"));
        assertEquals(0, indexAccessor.getIntValue("count") % 3);
    }

    @Test
    void shouldEmbedParametricCircleMetadataForRoundEdges() throws Exception {
        byte[] binary = StepPreviewJsonExporter.exportGlb(Files.readString(Path.of("examples/plate-with-round-hole.step")));
        String metadata = metadataFromGlb(binary);

        assertMetadataContains(metadata,
                "\"curve\":{",
                "\"type\":\"circle_arc\"",
                "\"radius\":",
                "\"sweepAngle\":",
                "\"xDirection\":");
    }

    @Test
    void shouldEmbedParametricCylinderMetadataForCylindricalFaces() throws Exception {
        byte[] binary = StepPreviewJsonExporter.exportGlb(Files.readString(Path.of("examples/cylindrical-band.step")));
        String metadata = metadataFromGlb(binary);

        assertMetadataContains(metadata,
                "\"surface\":{\"type\":\"cylindrical_strip\"",
                "\"radius\":",
                "\"lowerHeight\":",
                "\"upperHeight\":");
    }

    @Test
    void shouldEmbedParametricConeMetadataForConicalFaces() throws Exception {
        byte[] binary = StepPreviewJsonExporter.exportGlb(Files.readString(Path.of("examples/conical-band.step")));
        String metadata = metadataFromGlb(binary);

        assertMetadataContains(metadata,
                "\"surface\":{\"type\":\"conical_strip\"",
                "\"semiAngle\":",
                "\"lowerHeight\":",
                "\"upperHeight\":");
    }

    @Test
    void shouldEmbedParametricTorusMetadataForToroidalFaces() throws Exception {
        byte[] binary = StepPreviewJsonExporter.exportGlb(Files.readString(Path.of("examples/toroidal-band.step")));
        String metadata = metadataFromGlb(binary);

        assertMetadataContains(metadata,
                "\"surface\":{\"type\":\"toroidal_strip\"",
                "\"minorRadius\":",
                "\"lowerHeight\":",
                "\"upperHeight\":");
    }

    @Test
    void shouldEmbedParametricBsplineMetadataForBsplineFaces() throws Exception {
        byte[] binary = StepPreviewJsonExporter.exportGlb(Files.readString(Path.of("examples/bspline-patch.step")));
        String metadata = metadataFromGlb(binary);

        assertMetadataContains(metadata,
                "\"surface\":{\"type\":\"bspline_surface\"",
                "\"uDegree\":",
                "\"vDegree\":",
                "\"controlPoints\":",
                "\"surfaceUvLoops\":[");
    }

    @Test
    void shouldEmbedCurveMetadataInBinaryPreviewForImplicitBsplineCurveSubtypeMarkers() {
        byte[] binary = StepPreviewJsonExporter.exportBinary(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(2.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(3.0,1.0,0.0));\n"
        + "#10=(BEZIER_CURVE() B_SPLINE_CURVE(3,(#1,#2,#3,#4),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BZ0'));\n"
        + "#11=GEOMETRIC_CURVE_SET('GCS0',(#10));\n"
        + "ENDSEC;"
        );
        String metadata = metadataFromBinary(binary);

        assertMetadataContains(metadata,
                "\"format\":\"binary-preview-v1\"",
                "\"edges\":[{\"id\":10",
                "\"curve\":{\"stepId\":10,\"type\":\"BEZIER_CURVE\"");
    }

    @Test
    void shouldEmbedCurveMetadataInBinaryPreviewForTopologicalWrapperEdges() {
        byte[] binary = StepPreviewJsonExporter.exportBinary(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=VECTOR('VX',#3,1.0);\n"
        + "#5=LINE('L0',#1,#4);\n"
        + "#6=TRIMMED_CURVE('TC0',#5,(#1),(#2),.T.,.CARTESIAN.);\n"
        + "#7=ORIENTED_CURVE('OC0',#6,.F.);\n"
        + "#8=VERTEX_POINT('V0',#1);\n"
        + "#9=VERTEX_POINT('V1',#2);\n"
        + "#10=EDGE_CURVE('E0',#8,#9,#7,.T.);\n"
        + "#11=CONNECTED_EDGE_SET('CES',(#10));\n"
        + "ENDSEC;"
        );
        String metadata = metadataFromBinary(binary);

        assertMetadataContains(metadata,
                "\"edges\":[{\"id\":10",
                "\"curve\":{\"stepId\":7,\"type\":\"ORIENTED_CURVE\",\"basisType\":\"TRIMMED_CURVE\",\"basisStepId\":6,\"orientation\":false");
    }

    @Test
    void shouldEmbedCurveMetadataInBinaryRepresentationEdges() {
        byte[] binary = StepPreviewJsonExporter.exportBinary(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('SHIFT',(1.0,1.0,0.0));\n"
        + "#4=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#5=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#6=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#7=VECTOR('VX',#4,1.0);\n"
        + "#8=LINE('L0',#1,#7);\n"
        + "#9=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T',#4,#5,#3,1.0,#6);\n"
        + "#10=CURVE_REPLICA('CR',#8,#9);\n"
        + "#12=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));\n"
        + "#13=SHAPE_REPRESENTATION('R0',(#10),#12);\n"
        + "ENDSEC;"
        );
        String metadata = metadataFromBinary(binary);

        assertMetadataContains(metadata,
                "\"representations\":[{\"id\":13",
                "\"edges\":[{\"id\":10",
                "\"curve\":{\"stepId\":10,\"type\":\"CURVE_REPLICA\",\"basisType\":\"LINE\",\"basisStepId\":8,\"transformScale\":1.0");
    }

    @Test
    void shouldEmbedOffsetCurveMetadataInBinaryPreviewAndGlbExtras() {
        String step = 
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#3=VECTOR('VX',#2,1.0);\n"
        + "#4=LINE('L0',#1,#3);\n"
        + "#5=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#6=OFFSET_CURVE_3D('OC3',#4,0.5,.F.,#5);\n"
        + "#7=GEOMETRIC_CURVE_SET('GCS0',(#6));\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"edges\":[{\"id\":6",
                "\"curve\":{\"stepId\":6,\"type\":\"OFFSET_CURVE_3D\",\"basisType\":\"LINE\",\"basisStepId\":4,\"offsetDistance\":0.5,\"selfIntersect\":false,\"refDirection\":[0.0,0.0,1.0]");
        assertMetadataContains(glbMetadata,
                "\"kind\":\"edge\"",
                "\"curve\":{\"stepId\":6,\"type\":\"OFFSET_CURVE_3D\",\"basisType\":\"LINE\",\"basisStepId\":4",
                "\"offsetDistance\":0.5",
                "\"selfIntersect\":false",
                "\"refDirection\":[0.0,0.0,1.0]");
    }

    @Test
    void shouldEmbedSurfaceCurveAssociationMetadataInBinaryPreviewAndGlbExtras() {
        String step = 
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX0',#1,#2,#3);\n"
        + "#5=CYLINDRICAL_SURFACE('CY0',#4,1.0);\n"
        + "#6=CARTESIAN_POINT('P0',(1.0,0.0,0.0));\n"
        + "#7=DIRECTION('DU',(0.0,0.0,1.0));\n"
        + "#8=VECTOR('VU',#7,1.0);\n"
        + "#9=LINE('L0',#6,#8);\n"
        + "#10=CARTESIAN_POINT('UV0',(0.0,0.0));\n"
        + "#11=DIRECTION('DV',(0.0,1.0));\n"
        + "#12=VECTOR('VV',#11,1.0);\n"
        + "#13=LINE('UVL0',#10,#12);\n"
        + "#14=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');\n"
        + "#15=DEFINITIONAL_REPRESENTATION('DEF0',(#13),#14);\n"
        + "#16=PCURVE('PC0',#5,#15);\n"
        + "#17=SURFACE_CURVE('SC0',#9,(#16),.PCURVE_S1.);\n"
        + "#18=GEOMETRIC_CURVE_SET('GCS0',(#17));\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"edges\":[{\"id\":17",
                "\"curve\":{\"stepId\":17,\"type\":\"SURFACE_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":9",
                "\"masterRepresentation\":\"PCURVE_S1\"",
                "\"associatedSurfaceTypes\":[\"CYLINDRICAL_SURFACE\"]",
                "\"associatedSurfaceStepIds\":[5]");
        assertMetadataContains(glbMetadata,
                "\"kind\":\"edge\"",
                "\"curve\":{\"stepId\":17,\"type\":\"SURFACE_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":9",
                "\"masterRepresentation\":\"PCURVE_S1\"",
                "\"associatedSurfaceTypes\":[\"CYLINDRICAL_SURFACE\"]",
                "\"associatedSurfaceStepIds\":[5]");
    }

    @Test
    void shouldEmbedSeamCurveAssociationMetadataInBinaryPreviewAndGlbExtras() {
        String step = 
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#4=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#5=AXIS2_PLACEMENT_3D('AX0',#1,#3,#4);\n"
        + "#6=AXIS2_PLACEMENT_3D('AX1',#2,#3,#4);\n"
        + "#7=CONICAL_SURFACE('CN0',#5,1.0,0.4636476090008061);\n"
        + "#8=CONICAL_SURFACE('CN1',#6,1.0,0.4636476090008061);\n"
        + "#10=CARTESIAN_POINT('P0',(1.0,0.0,0.0));\n"
        + "#11=DIRECTION('DU',(1.0,0.0,1.0));\n"
        + "#12=VECTOR('VU',#11,1.0);\n"
        + "#13=LINE('L0',#10,#12);\n"
        + "#20=CARTESIAN_POINT('UV0',(0.0,0.0));\n"
        + "#21=DIRECTION('DV0',(0.0,1.0));\n"
        + "#22=VECTOR('VV0',#21,1.0);\n"
        + "#23=LINE('GOOD',#20,#22);\n"
        + "#24=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');\n"
        + "#25=DEFINITIONAL_REPRESENTATION('DEF0',(#23),#24);\n"
        + "#26=PCURVE('PC0',#7,#25);\n"
        + "#27=CARTESIAN_POINT('UV1',(3.141592653589793,0.0));\n"
        + "#28=DIRECTION('DV1',(0.0,1.0));\n"
        + "#29=VECTOR('VV1',#28,1.0);\n"
        + "#30=LINE('BAD',#27,#29);\n"
        + "#31=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');\n"
        + "#32=DEFINITIONAL_REPRESENTATION('DEF1',(#30),#31);\n"
        + "#33=PCURVE('PC1',#8,#32);\n"
        + "#34=SEAM_CURVE('SEAM0',#13,(#33,#26),.PCURVE_S1.);\n"
        + "#35=GEOMETRIC_CURVE_SET('GCS0',(#34));\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"edges\":[{\"id\":34",
                "\"curve\":{\"stepId\":34,\"type\":\"SEAM_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":13",
                "\"masterRepresentation\":\"PCURVE_S1\"",
                "\"associatedSurfaceTypes\":[\"CONICAL_SURFACE\",\"CONICAL_SURFACE\"]",
                "\"associatedSurfaceStepIds\":[8,7]");
        assertMetadataContains(glbMetadata,
                "\"kind\":\"edge\"",
                "\"curve\":{\"stepId\":34,\"type\":\"SEAM_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":13",
                "\"masterRepresentation\":\"PCURVE_S1\"",
                "\"associatedSurfaceTypes\":[\"CONICAL_SURFACE\",\"CONICAL_SURFACE\"]",
                "\"associatedSurfaceStepIds\":[8,7]");
    }

    @Test
    void shouldPropagateSurfaceCurveAssociationMetadataThroughProjectionWrapperInBinaryAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX0',#1,#2,#3);\n"
        + "#5=CYLINDRICAL_SURFACE('CY0',#4,1.0);\n"
        + "#6=CARTESIAN_POINT('P0',(1.0,0.0,0.0));\n"
        + "#7=DIRECTION('DU',(0.0,0.0,1.0));\n"
        + "#8=VECTOR('VU',#7,1.0);\n"
        + "#9=LINE('L0',#6,#8);\n"
        + "#10=CARTESIAN_POINT('UV0',(0.0,0.0));\n"
        + "#11=DIRECTION('DV',(0.0,1.0));\n"
        + "#12=VECTOR('VV',#11,1.0);\n"
        + "#13=LINE('UVL0',#10,#12);\n"
        + "#14=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');\n"
        + "#15=DEFINITIONAL_REPRESENTATION('DEF0',(#13),#14);\n"
        + "#16=PCURVE('PC0',#5,#15);\n"
        + "#17=SURFACE_CURVE('SC0',#9,(#16),.PCURVE_S1.);\n"
        + "#18=PRESENTATION_STYLE_ASSIGNMENT(());\n"
        + "#19=(PROJECTION_CURVE('PCW0',(#18),#17)\n"
        + "    ANNOTATION_CURVE_OCCURRENCE('PCW0',(#18),#17)\n"
        + "    STYLED_ITEM('PCW0',(#18),#17)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('PCW0'));\n"
        + "#20=GEOMETRIC_CURVE_SET('GCS0',(#19));\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"edges\":[{\"id\":19",
                "\"curve\":{\"stepId\":19,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"SURFACE_CURVE\",\"basisStepId\":17",
                "\"masterRepresentation\":\"PCURVE_S1\"",
                "\"associatedSurfaceTypes\":[\"CYLINDRICAL_SURFACE\"]",
                "\"associatedSurfaceStepIds\":[5]");
        assertMetadataContains(glbMetadata,
                "\"kind\":\"edge\"",
                "\"curve\":{\"stepId\":19,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"SURFACE_CURVE\",\"basisStepId\":17",
                "\"masterRepresentation\":\"PCURVE_S1\"",
                "\"associatedSurfaceTypes\":[\"CYLINDRICAL_SURFACE\"]",
                "\"associatedSurfaceStepIds\":[5]");
    }

    @Test
    void shouldPropagateSeamCurveAssociationMetadataThroughProjectionWrapperInBinaryAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#4=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#5=AXIS2_PLACEMENT_3D('AX0',#1,#3,#4);\n"
        + "#6=AXIS2_PLACEMENT_3D('AX1',#2,#3,#4);\n"
        + "#7=CONICAL_SURFACE('CN0',#5,1.0,0.4636476090008061);\n"
        + "#8=CONICAL_SURFACE('CN1',#6,1.0,0.4636476090008061);\n"
        + "#10=CARTESIAN_POINT('P0',(1.0,0.0,0.0));\n"
        + "#11=DIRECTION('DU',(1.0,0.0,1.0));\n"
        + "#12=VECTOR('VU',#11,1.0);\n"
        + "#13=LINE('L0',#10,#12);\n"
        + "#20=CARTESIAN_POINT('UV0',(0.0,0.0));\n"
        + "#21=DIRECTION('DV0',(0.0,1.0));\n"
        + "#22=VECTOR('VV0',#21,1.0);\n"
        + "#23=LINE('GOOD',#20,#22);\n"
        + "#24=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');\n"
        + "#25=DEFINITIONAL_REPRESENTATION('DEF0',(#23),#24);\n"
        + "#26=PCURVE('PC0',#7,#25);\n"
        + "#27=CARTESIAN_POINT('UV1',(3.141592653589793,0.0));\n"
        + "#28=DIRECTION('DV1',(0.0,1.0));\n"
        + "#29=VECTOR('VV1',#28,1.0);\n"
        + "#30=LINE('BAD',#27,#29);\n"
        + "#31=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');\n"
        + "#32=DEFINITIONAL_REPRESENTATION('DEF1',(#30),#31);\n"
        + "#33=PCURVE('PC1',#8,#32);\n"
        + "#34=SEAM_CURVE('SEAM0',#13,(#33,#26),.PCURVE_S1.);\n"
        + "#35=PRESENTATION_STYLE_ASSIGNMENT(());\n"
        + "#36=(PROJECTION_CURVE('PCW0',(#35),#34)\n"
        + "    ANNOTATION_CURVE_OCCURRENCE('PCW0',(#35),#34)\n"
        + "    STYLED_ITEM('PCW0',(#35),#34)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('PCW0'));\n"
        + "#37=GEOMETRIC_CURVE_SET('GCS0',(#36));\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"edges\":[{\"id\":36",
                "\"curve\":{\"stepId\":36,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"SEAM_CURVE\",\"basisStepId\":34",
                "\"masterRepresentation\":\"PCURVE_S1\"",
                "\"associatedSurfaceTypes\":[\"CONICAL_SURFACE\",\"CONICAL_SURFACE\"]",
                "\"associatedSurfaceStepIds\":[8,7]");
        assertMetadataContains(glbMetadata,
                "\"kind\":\"edge\"",
                "\"curve\":{\"stepId\":36,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"SEAM_CURVE\",\"basisStepId\":34",
                "\"masterRepresentation\":\"PCURVE_S1\"",
                "\"associatedSurfaceTypes\":[\"CONICAL_SURFACE\",\"CONICAL_SURFACE\"]",
                "\"associatedSurfaceStepIds\":[8,7]");
    }

    @Test
    void shouldPreserveCurveMetadataForMappedAnnotationSymbolEdgesInBinaryAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#3=VECTOR('VX',#2,1.0);\n"
        + "#4=LINE('L0',#1,#3);\n"
        + "#5=PRESENTATION_STYLE_ASSIGNMENT(());\n"
        + "#6=(PROJECTION_CURVE('PC0',(#5),#4)\n"
        + "    ANNOTATION_CURVE_OCCURRENCE('PC0',(#5),#4)\n"
        + "    STYLED_ITEM('PC0',(#5),#4)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('PC0'));\n"
        + "#7=CARTESIAN_POINT('O2',(2.0,0.0));\n"
        + "#8=DIRECTION('DX2',(1.0,0.0));\n"
        + "#9=AXIS2_PLACEMENT_2D('MAP',#7,#8);\n"
        + "#10=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));\n"
        + "#11=REPRESENTATION('SYM',(#6),#10);\n"
        + "#12=SYMBOL_REPRESENTATION_MAP(#9,#11);\n"
        + "#13=CARTESIAN_POINT('O3',(3.0,0.0));\n"
        + "#14=DIRECTION('DX3',(1.0,0.0));\n"
        + "#15=AXIS2_PLACEMENT_2D('TGT',#13,#14);\n"
        + "#16=ANNOTATION_SYMBOL('AS0',#12,#15);\n"
        + "#17=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#5),#16);\n"
        + "#18=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));\n"
        + "#19=SHAPE_REPRESENTATION('ANN',(#17),#18);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"representations\":[{\"id\":19",
                "\"curve\":{\"stepId\":6,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":4",
                "\"sourceType\":\"ANNOTATION_SYMBOL_OCCURRENCE\"",
                "\"sourceStepId\":17");
        assertMetadataContains(glbMetadata,
                "\"kind\":\"edge\"",
                "\"curve\":{\"stepId\":6,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":4",
                "\"sourceType\":\"ANNOTATION_SYMBOL_OCCURRENCE\"",
                "\"sourceStepId\":17");
    }

    @Test
    void shouldPreserveCurveMetadataForMappedAnnotationTextEdgesInBinaryAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#3=VECTOR('VX',#2,1.0);\n"
        + "#4=LINE('L0',#1,#3);\n"
        + "#5=PRESENTATION_STYLE_ASSIGNMENT(());\n"
        + "#6=(PROJECTION_CURVE('PC0',(#5),#4)\n"
        + "    ANNOTATION_CURVE_OCCURRENCE('PC0',(#5),#4)\n"
        + "    STYLED_ITEM('PC0',(#5),#4)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('PC0'));\n"
        + "#7=CARTESIAN_POINT('O2',(2.0,0.0));\n"
        + "#8=DIRECTION('DX2',(1.0,0.0));\n"
        + "#9=AXIS2_PLACEMENT_2D('MAP',#7,#8);\n"
        + "#10=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));\n"
        + "#11=REPRESENTATION('SYM',(#6),#10);\n"
        + "#12=REPRESENTATION_MAP(#9,#11);\n"
        + "#13=CARTESIAN_POINT('O3',(3.0,0.0));\n"
        + "#14=DIRECTION('DX3',(1.0,0.0));\n"
        + "#15=AXIS2_PLACEMENT_2D('TGT',#13,#14);\n"
        + "#16=ANNOTATION_TEXT('AT0',#12,#15);\n"
        + "#17=ANNOTATION_TEXT_CHARACTER('ATC0',#12,#15);\n"
        + "#18=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));\n"
        + "#19=SHAPE_REPRESENTATION('ANN',(#16,#17),#18);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"representations\":[{\"id\":19",
                "\"curve\":{\"stepId\":6,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":4");
        assertMetadataContains(glbMetadata,
                "\"kind\":\"edge\"",
                "\"curve\":{\"stepId\":6,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":4");
    }

    @Test
    void shouldPreserveMappedAnnotationCarrierMetadataForSymbolAndSubfigureOccurrencesInBinaryAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#3=VECTOR('VX',#2,1.0);\n"
        + "#4=LINE('L0',#1,#3);\n"
        + "#5=PRESENTATION_STYLE_ASSIGNMENT(());\n"
        + "#6=(PROJECTION_CURVE('PC0',(#5),#4)\n"
        + "    ANNOTATION_CURVE_OCCURRENCE('PC0',(#5),#4)\n"
        + "    STYLED_ITEM('PC0',(#5),#4)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('PC0'));\n"
        + "#7=CARTESIAN_POINT('O2',(2.0,0.0));\n"
        + "#8=DIRECTION('DX2',(1.0,0.0));\n"
        + "#9=AXIS2_PLACEMENT_2D('MAP',#7,#8);\n"
        + "#10=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));\n"
        + "#11=REPRESENTATION('SYM',(#6),#10);\n"
        + "#12=SYMBOL_REPRESENTATION_MAP(#9,#11);\n"
        + "#13=CARTESIAN_POINT('O3',(3.0,0.0));\n"
        + "#14=DIRECTION('DX3',(1.0,0.0));\n"
        + "#15=AXIS2_PLACEMENT_2D('TGT',#13,#14);\n"
        + "#16=ANNOTATION_SYMBOL('AS0',#12,#15);\n"
        + "#17=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#5),#16);\n"
        + "#18=ANNOTATION_SUBFIGURE_OCCURRENCE('SUB0',(#5),#16);\n"
        + "#19=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));\n"
        + "#20=SHAPE_REPRESENTATION('ANN',(#17,#18),#19);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"representations\":[{\"id\":20",
                "\"curve\":{\"stepId\":6,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":4",
                "\"sourceType\":\"ANNOTATION_SYMBOL_OCCURRENCE\"",
                "\"sourceStepId\":17",
                "\"sourceType\":\"ANNOTATION_SUBFIGURE_OCCURRENCE\"",
                "\"sourceStepId\":18");
        assertMetadataContains(glbMetadata,
                "\"kind\":\"edge\"",
                "\"curve\":{\"stepId\":6,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":4",
                "\"sourceType\":\"ANNOTATION_SYMBOL_OCCURRENCE\"",
                "\"sourceStepId\":17",
                "\"sourceType\":\"ANNOTATION_SUBFIGURE_OCCURRENCE\"",
                "\"sourceStepId\":18");
    }

    @Test
    void shouldPreserveMappedAnnotationCarrierMetadataForDraughtingAnnotationOccurrenceInBinaryAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#3=VECTOR('VX',#2,1.0);\n"
        + "#4=LINE('L0',#1,#3);\n"
        + "#5=PRESENTATION_STYLE_ASSIGNMENT(());\n"
        + "#6=(PROJECTION_CURVE('PC0',(#5),#4)\n"
        + "    ANNOTATION_CURVE_OCCURRENCE('PC0',(#5),#4)\n"
        + "    STYLED_ITEM('PC0',(#5),#4)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('PC0'));\n"
        + "#7=CARTESIAN_POINT('O2',(2.0,0.0));\n"
        + "#8=DIRECTION('DX2',(1.0,0.0));\n"
        + "#9=AXIS2_PLACEMENT_2D('MAP',#7,#8);\n"
        + "#10=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));\n"
        + "#11=REPRESENTATION('SYM',(#6),#10);\n"
        + "#12=SYMBOL_REPRESENTATION_MAP(#9,#11);\n"
        + "#13=CARTESIAN_POINT('O3',(3.0,0.0));\n"
        + "#14=DIRECTION('DX3',(1.0,0.0));\n"
        + "#15=AXIS2_PLACEMENT_2D('TGT',#13,#14);\n"
        + "#16=ANNOTATION_SYMBOL('AS0',#12,#15);\n"
        + "#17=(DRAUGHTING_ANNOTATION_OCCURRENCE('DAO0',(#5),#16)\n"
        + "    STYLED_ITEM('DAO0',(#5),#16)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('DAO0'));\n"
        + "#18=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));\n"
        + "#19=SHAPE_REPRESENTATION('ANN',(#17),#18);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"representations\":[{\"id\":19",
                "\"curve\":{\"stepId\":6,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":4",
                "\"sourceType\":\"DRAUGHTING_ANNOTATION_OCCURRENCE\"",
                "\"sourceStepId\":17");
        assertMetadataContains(glbMetadata,
                "\"kind\":\"edge\"",
                "\"curve\":{\"stepId\":6,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":4",
                "\"sourceType\":\"DRAUGHTING_ANNOTATION_OCCURRENCE\"",
                "\"sourceStepId\":17");
    }

    @Test
    void shouldEmbedPmiRelationshipMetadataInBinaryPreviewAndGlb() {
        String calloutStep = 
        "DATA;\n"
        + "#1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');\n"
        + "#2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#3=REPRESENTATION('REP_A',(#1),#2);\n"
        + "#4=PROPERTY_DEFINITION('PD','',#1);\n"
        + "#5=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#6=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#7=ANNOTATION_TEXT_OCCURRENCE('NOTE_A','base',#5);\n"
        + "#8=ANNOTATION_TEXT_OCCURRENCE('NOTE_B','child',#6);\n"
        + "#9=DRAUGHTING_CALLOUT('CALLOUT_A',(#7));\n"
        + "#10=DRAUGHTING_CALLOUT('CALLOUT_B',(#8));\n"
        + "#11=PLACED_TARGET('PT','target',#4,#3,#9);\n"
        + "#12=DRAUGHTING_CALLOUT_RELATIONSHIP('REL','carry',#9,#10);\n"
        + "ENDSEC;";
        String occurrenceStep = 
        "DATA;\n"
        + "#1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');\n"
        + "#2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#3=REPRESENTATION('REP_A',(#1),#2);\n"
        + "#4=PROPERTY_DEFINITION('PD','',#1);\n"
        + "#5=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYM'));\n"
        + "#6=REPRESENTATION('SYM',(),#5);\n"
        + "#7=CARTESIAN_POINT('O',(0.0,0.0));\n"
        + "#8=DIRECTION('X',(1.0,0.0));\n"
        + "#9=AXIS2_PLACEMENT_2D('MAP',#7,#8);\n"
        + "#10=SYMBOL_REPRESENTATION_MAP(#9,#6);\n"
        + "#11=CARTESIAN_POINT('P',(3.0,4.0));\n"
        + "#12=AXIS2_PLACEMENT_2D('TGT',#11,#8);\n"
        + "#13=ANNOTATION_SYMBOL('AS0',#10,#12);\n"
        + "#14=PRESENTATION_STYLE_ASSIGNMENT(());\n"
        + "#15=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#16=DIRECTION('DIR0',(1.0,0.0,0.0));\n"
        + "#17=VECTOR('V0',#16,1.0);\n"
        + "#18=LINE('L0',#15,#17);\n"
        + "#19=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#14),#18);\n"
        + "#20=TERMINATOR_SYMBOL('TS0',(#14),#13,#19);\n"
        + "#21=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#14),#13);\n"
        + "#22=ANNOTATION_OCCURRENCE_RELATIONSHIP('REL','links symbol to terminator',#21,#20);\n"
        + "#23=ANNOTATION_TEXT_OCCURRENCE('NOTE','occurrence-links',#15);\n"
        + "#24=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC','',#4,#3,#23,#22);\n"
        + "ENDSEC;";

        String calloutBinary = metadataFromBinary(StepPreviewJsonExporter.exportBinary(calloutStep));
        String calloutGlb = metadataFromGlb(StepPreviewJsonExporter.exportGlb(calloutStep));
        String occurrenceBinary = metadataFromBinary(StepPreviewJsonExporter.exportBinary(occurrenceStep));
        String occurrenceGlb = metadataFromGlb(StepPreviewJsonExporter.exportGlb(occurrenceStep));

        assertMetadataContains(calloutBinary,
                "\"name\":\"CALLOUT_B\"",
                "\"viaRelationshipType\":\"DRAUGHTING_CALLOUT_RELATIONSHIP\"",
                "\"viaRelationshipId\":12");
        assertMetadataContains(calloutGlb,
                "\"name\":\"CALLOUT_B\"",
                "\"viaRelationshipType\":\"DRAUGHTING_CALLOUT_RELATIONSHIP\"",
                "\"viaRelationshipId\":12");
        assertMetadataContains(occurrenceBinary,
                "\"name\":\"NOTE\"",
                "\"viaRelationshipType\":\"ANNOTATION_OCCURRENCE_RELATIONSHIP\"",
                "\"viaRelationshipId\":22");
        assertMetadataContains(occurrenceGlb,
                "\"name\":\"NOTE\"",
                "\"viaRelationshipType\":\"ANNOTATION_OCCURRENCE_RELATIONSHIP\"",
                "\"viaRelationshipId\":22");
    }

    @Test
    void shouldEmbedPmiAssociativityMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');\n"
        + "#2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#3=REPRESENTATION('REP_A',(#1),#2);\n"
        + "#4=PROPERTY_DEFINITION('PD','',#1);\n"
        + "#5=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYM'));\n"
        + "#6=REPRESENTATION('SYM',(),#5);\n"
        + "#7=CARTESIAN_POINT('O',(0.0,0.0));\n"
        + "#8=DIRECTION('X',(1.0,0.0));\n"
        + "#9=AXIS2_PLACEMENT_2D('MAP',#7,#8);\n"
        + "#10=SYMBOL_REPRESENTATION_MAP(#9,#6);\n"
        + "#11=CARTESIAN_POINT('P',(3.0,4.0));\n"
        + "#12=AXIS2_PLACEMENT_2D('TGT',#11,#8);\n"
        + "#13=ANNOTATION_SYMBOL('AS0',#10,#12);\n"
        + "#14=PRESENTATION_STYLE_ASSIGNMENT(());\n"
        + "#15=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#14),#13);\n"
        + "#16=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#17=DIRECTION('DIR0',(1.0,0.0,0.0));\n"
        + "#18=VECTOR('V0',#17,1.0);\n"
        + "#19=LINE('L0',#16,#18);\n"
        + "#20=PROJECTION_CURVE('PC0',(#14),#19);\n"
        + "#21=TERMINATOR_SYMBOL('TS0',(#14),#13,#20);\n"
        + "#22=ANNOTATION_OCCURRENCE_ASSOCIATIVITY('AOA','assoc',#15,#21);\n"
        + "#23=DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY('DCTPCA','assoc',#21,#20);\n"
        + "#24=ANNOTATION_TEXT_OCCURRENCE('NOTE_A','assoc',#16);\n"
        + "#25=ANNOTATION_TEXT_OCCURRENCE('NOTE_B','dim',#16);\n"
        + "#26=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_A','',#4,#3,#24,#22);\n"
        + "#27=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_B','',#4,#3,#25,#23);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_A\"",
                "\"name\":\"NOTE_B\"",
                "\"viaRelationshipType\":\"ANNOTATION_OCCURRENCE_ASSOCIATIVITY\"",
                "\"viaRelationshipId\":22",
                "\"viaRelationshipType\":\"DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY\"",
                "\"viaRelationshipId\":23");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_A\"",
                "\"name\":\"NOTE_B\"",
                "\"viaRelationshipType\":\"ANNOTATION_OCCURRENCE_ASSOCIATIVITY\"",
                "\"viaRelationshipId\":22",
                "\"viaRelationshipType\":\"DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY\"",
                "\"viaRelationshipId\":23");
    }

    @Test
    void shouldEmbedPmiUsageMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_BASE',(),#9);\n"
        + "#11=REPRESENTATION('REP_USAGE_A',(),#9);\n"
        + "#12=REPRESENTATION('REP_USAGE_B',(),#9);\n"
        + "#13=REPRESENTATION('REP_USAGE_C',(),#9);\n"
        + "#14=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#15=ITEM_IDENTIFIED_REPRESENTATION_USAGE('IU','',#8,#11,#8);\n"
        + "#16=CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE('CIU','',#15,(#11,#12),(#17),#8);\n"
        + "#17=REPRESENTATION_RELATIONSHIP('RR','',#11,#12);\n"
        + "#18=ANNOTATION_TEXT_OCCURRENCE('NOTE_USAGE_A','',#40);\n"
        + "#19=DRAUGHTING_CALLOUT('CALLOUT0',(#18));\n"
        + "#20=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU','',#18,#11);\n"
        + "#21=CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE('CGU','',#19,(#12,#13),(#22),#12);\n"
        + "#22=REPRESENTATION_RELATIONSHIP('RR2','',#12,#13);\n"
        + "#23=DRAUGHTING_MODEL_ITEM_ASSOCIATION('DMA','',#16,#12,#19);\n"
        + "#24=PMI_REQUIREMENT_ITEM_ASSOCIATION('PMI0','',#21,#10,#31,#8);\n"
        + "#25=MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION('MDR0','',#23,#10,#32,#8);\n"
        + "#26=PLACED_TARGET('PT0','',#24,#13,#8);\n"
        + "#30=GEOMETRIC_SET('GS',());\n"
        + "#31=ANNOTATION_TEXT_OCCURRENCE('NOTE_PMI_NESTED','',#40);\n"
        + "#32=ANNOTATION_TEXT_OCCURRENCE('NOTE_MDR_NESTED','',#41);\n"
        + "#33=ANNOTATION_TEXT_OCCURRENCE('NOTE_PLACED_TARGET_NESTED','',#42);\n"
        + "#34=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#26,#10,#33,#8);\n"
        + "#40=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#41=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#42=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_PMI_NESTED\"",
                "\"name\":\"NOTE_MDR_NESTED\"",
                "\"name\":\"NOTE_PLACED_TARGET_NESTED\"",
                "\"name\":\"CALLOUT0\"",
                "\"viaUsageType\":\"DRAUGHTING_MODEL_ITEM_ASSOCIATION\"",
                "\"viaUsageId\":23",
                "\"viaUsageType\":\"PMI_REQUIREMENT_ITEM_ASSOCIATION\"",
                "\"viaUsageId\":24",
                "\"viaUsageType\":\"MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION\"",
                "\"viaUsageId\":25",
                "\"viaDefinitionType\":\"PLACED_TARGET\"",
                "\"viaDefinitionId\":26",
                "\"viaUsageType\":\"PMI_REQUIREMENT_ITEM_ASSOCIATION\"",
                "\"viaUsageId\":34");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_PMI_NESTED\"",
                "\"name\":\"NOTE_MDR_NESTED\"",
                "\"name\":\"NOTE_PLACED_TARGET_NESTED\"",
                "\"name\":\"CALLOUT0\"",
                "\"viaUsageType\":\"DRAUGHTING_MODEL_ITEM_ASSOCIATION\"",
                "\"viaUsageId\":23",
                "\"viaUsageType\":\"PMI_REQUIREMENT_ITEM_ASSOCIATION\"",
                "\"viaUsageId\":24",
                "\"viaUsageType\":\"MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION\"",
                "\"viaUsageId\":25",
                "\"viaDefinitionType\":\"PLACED_TARGET\"",
                "\"viaDefinitionId\":26",
                "\"viaUsageType\":\"PMI_REQUIREMENT_ITEM_ASSOCIATION\"",
                "\"viaUsageId\":34");
    }

    @Test
    void shouldEmbedPlaceholderAssociationUsageMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');\n"
        + "#2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#3=DRAUGHTING_MODEL('DM',(#1),#2);\n"
        + "#4=PROPERTY_DEFINITION('PD','',#1);\n"
        + "#5=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#6=CARTESIAN_POINT('P1',(1.0,1.0,0.0));\n"
        + "#7=ANNOTATION_POINT_OCCURRENCE('APO',(),#5);\n"
        + "#8=GEOMETRIC_SET('PHSET',(#6));\n"
        + "#9=ANNOTATION_PLACEHOLDER_OCCURRENCE('PH',(),#8,.TITLE.,1.0);\n"
        + "#10=ANNOTATION_SYMBOL_OCCURRENCE('ASO',(),#9);\n"
        + "#11=REPRESENTATION('REP_A',(),#2);\n"
        + "#12=REPRESENTATION('REP_B',(),#2);\n"
        + "#13=REPRESENTATION('REP_C',(),#2);\n"
        + "#14=REPRESENTATION_RELATIONSHIP('RR','chain',#12,#13);\n"
        + "#15=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU','',#7,#11);\n"
        + "#16=CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE('CGU','',#10,(#12,#13),(#14),#11);\n"
        + "#17=DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER('DMIAP','assocph',#4,#3,#7,#9);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"APO\"",
                "\"name\":\"PH\"",
                "\"name\":\"ASO\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"id\":3,\"type\":\"representation\",\"name\":\"DM\"",
                "\"viaUsageType\":\"GEOMETRIC_ITEM_SPECIFIC_USAGE\"",
                "\"viaUsageId\":15",
                "\"viaUsageType\":\"CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE\"",
                "\"viaUsageId\":16",
                "\"viaUsageType\":\"DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER\"",
                "\"viaUsageId\":17");
        assertMetadataContains(glbMetadata,
                "\"name\":\"APO\"",
                "\"name\":\"PH\"",
                "\"name\":\"ASO\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"id\":3,\"type\":\"representation\",\"name\":\"DM\"",
                "\"viaUsageType\":\"GEOMETRIC_ITEM_SPECIFIC_USAGE\"",
                "\"viaUsageId\":15",
                "\"viaUsageType\":\"CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE\"",
                "\"viaUsageId\":16",
                "\"viaUsageType\":\"DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER\"",
                "\"viaUsageId\":17");
    }

    @Test
    void shouldEmbedRepresentationUsageAssociationMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');\n"
        + "#2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#3=REPRESENTATION('REP_A',(#1),#2);\n"
        + "#4=REPRESENTATION('REP_B',(#1),#2);\n"
        + "#5=REPRESENTATION('REP_C',(#1),#2);\n"
        + "#6=REPRESENTATION_RELATIONSHIP('RR','chain',#4,#5);\n"
        + "#7=PROPERTY_DEFINITION('PD','',#1);\n"
        + "#8=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#9=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#8);\n"
        + "#10=GEOMETRIC_CURVE_SET('LEADER',(#8));\n"
        + "#11=DRAUGHTING_CALLOUT('CALLOUT',(#9,#10));\n"
        + "#12=ITEM_IDENTIFIED_REPRESENTATION_USAGE('USAGE','generic',#7,#3,#11);\n"
        + "#13=PLACED_TARGET('PT','target',#7,#4,#11);\n"
        + "#14=CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE('CBIIRU','chain',#7,(#4,#5),(#6),#11);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"CALLOUT\"",
                "\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"id\":4,\"type\":\"representation\",\"name\":\"REP_B\"",
                "\"id\":5,\"type\":\"representation\",\"name\":\"REP_C\"",
                "\"viaUsageType\":\"ITEM_IDENTIFIED_REPRESENTATION_USAGE\"",
                "\"viaUsageId\":12",
                "\"viaUsageType\":\"PLACED_TARGET\"",
                "\"viaUsageId\":13",
                "\"viaUsageType\":\"CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE\"",
                "\"viaUsageId\":14",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":6");
        assertMetadataContains(glbMetadata,
                "\"name\":\"CALLOUT\"",
                "\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"id\":4,\"type\":\"representation\",\"name\":\"REP_B\"",
                "\"id\":5,\"type\":\"representation\",\"name\":\"REP_C\"",
                "\"viaUsageType\":\"ITEM_IDENTIFIED_REPRESENTATION_USAGE\"",
                "\"viaUsageId\":12",
                "\"viaUsageType\":\"PLACED_TARGET\"",
                "\"viaUsageId\":13",
                "\"viaUsageType\":\"CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE\"",
                "\"viaUsageId\":14",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":6");
    }

    @Test
    void shouldEmbedDirectAnnotationContentUsageMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','ANN'));\n"
        + "#2=REPRESENTATION('REP_USED',(),#1);\n"
        + "#3=REPRESENTATION('REP_A',(),#1);\n"
        + "#4=REPRESENTATION('REP_B',(),#1);\n"
        + "#5=REPRESENTATION_RELATIONSHIP('RR','chain',#3,#4);\n"
        + "#34=REPRESENTATION_RELATIONSHIP('RR_USED','used chain',#2,#3);\n"
        + "#35=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#36=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#37=DIRECTION('DZ3',(0.0,0.0,1.0));\n"
        + "#38=DIRECTION('DX3',(1.0,0.0,0.0));\n"
        + "#39=AXIS2_PLACEMENT_3D('AX0',#35,#37,#38);\n"
        + "#40=AXIS2_PLACEMENT_3D('AX1',#36,#37,#38);\n"
        + "#41=ITEM_DEFINED_TRANSFORMATION('T1','',#39,#40);\n"
        + "#42=(REPRESENTATION_RELATIONSHIP('RRT_USED','',#2,#4)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#41));\n"
        + "#43=SHAPE_REPRESENTATION_RELATIONSHIP('SRR_USED','',#2,#3);\n"
        + "#6=PROPERTY_DEFINITION('PD','',#2);\n"
        + "#7=CARTESIAN_POINT('O',(0.0,0.0));\n"
        + "#8=DIRECTION('X',(1.0,0.0));\n"
        + "#9=AXIS2_PLACEMENT_2D('MAP',#7,#8);\n"
        + "#10=REPRESENTATION('SYMREP',(),#1);\n"
        + "#11=SYMBOL_REPRESENTATION_MAP(#9,#10);\n"
        + "#12=CARTESIAN_POINT('P0',(10.0,20.0));\n"
        + "#13=AXIS2_PLACEMENT_2D('TGT0',#12,#8);\n"
        + "#14=ANNOTATION_SYMBOL('AS0',#11,#13);\n"
        + "#15=REPRESENTATION_MAP(#9,#10);\n"
        + "#16=CARTESIAN_POINT('P1',(30.0,40.0));\n"
        + "#17=AXIS2_PLACEMENT_2D('TGT1',#16,#8);\n"
        + "#18=ANNOTATION_TEXT('AT0',#15,#17);\n"
        + "#19=ANNOTATION_TEXT_CHARACTER('ATC0',#15,#17);\n"
        + "#20=CARTESIAN_POINT('F0',(0.0,0.0,0.0));\n"
        + "#21=CARTESIAN_POINT('F1',(1.0,0.0,0.0));\n"
        + "#22=CARTESIAN_POINT('F2',(1.0,1.0,0.0));\n"
        + "#23=POLYLINE('PL0',(#20,#21,#22));\n"
        + "#24=(ANNOTATION_FILL_AREA('FA0',(#23))\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('FA0'));\n"
        + "#25=ANNOTATION_TEXT_OCCURRENCE('NOTE','',#20);\n"
        + "#26=GEOMETRIC_ITEM_SPECIFIC_USAGE('G0','',#25,#14);\n"
        + "#27=GEOMETRIC_ITEM_SPECIFIC_USAGE('G1','',#25,#18);\n"
        + "#28=GEOMETRIC_ITEM_SPECIFIC_USAGE('G2','',#25,#19);\n"
        + "#29=GEOMETRIC_ITEM_SPECIFIC_USAGE('G3','',#25,#24);\n"
        + "#30=ITEM_IDENTIFIED_REPRESENTATION_USAGE('IU','',#6,#2,#14);\n"
        + "#31=CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE('CIU','',#6,(#3,#4),(#5),#18);\n"
        + "#32=DRAUGHTING_MODEL_ITEM_ASSOCIATION('DMA','',#6,#2,#19);\n"
        + "#33=PMI_REQUIREMENT_ITEM_ASSOCIATION('PMI','',#6,#2,#24,#6);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"AS0\"",
                "\"name\":\"AT0\"",
                "\"name\":\"ATC0\"",
                "\"name\":\"FA0\"",
                "\"id\":14,\"type\":\"annotation_symbol\",\"name\":\"AS0\"",
                "\"id\":18,\"type\":\"annotation_text\",\"name\":\"AT0\"",
                "\"id\":19,\"type\":\"annotation_text_character\",\"name\":\"ATC0\"",
                "\"id\":24,\"type\":\"annotation_fill_area\",\"name\":\"FA0\"",
                "\"id\":2,\"type\":\"representation\",\"name\":\"REP_USED\"",
                "\"id\":4,\"type\":\"representation\",\"name\":\"REP_B\"",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":43",
                "\"viaUsageType\":\"GEOMETRIC_ITEM_SPECIFIC_USAGE\"",
                "\"viaUsageId\":26",
                "\"viaUsageId\":27",
                "\"viaUsageId\":28",
                "\"viaUsageId\":29",
                "\"viaUsageType\":\"ITEM_IDENTIFIED_REPRESENTATION_USAGE\"",
                "\"viaUsageId\":30",
                "\"viaUsageType\":\"CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE\"",
                "\"viaUsageId\":31",
                "\"viaUsageType\":\"DRAUGHTING_MODEL_ITEM_ASSOCIATION\"",
                "\"viaUsageId\":32",
                "\"viaUsageType\":\"PMI_REQUIREMENT_ITEM_ASSOCIATION\"",
                "\"viaUsageId\":33");
        assertMetadataContains(glbMetadata,
                "\"name\":\"AS0\"",
                "\"name\":\"AT0\"",
                "\"name\":\"ATC0\"",
                "\"name\":\"FA0\"",
                "\"id\":14,\"type\":\"annotation_symbol\",\"name\":\"AS0\"",
                "\"id\":18,\"type\":\"annotation_text\",\"name\":\"AT0\"",
                "\"id\":19,\"type\":\"annotation_text_character\",\"name\":\"ATC0\"",
                "\"id\":24,\"type\":\"annotation_fill_area\",\"name\":\"FA0\"",
                "\"id\":2,\"type\":\"representation\",\"name\":\"REP_USED\"",
                "\"id\":4,\"type\":\"representation\",\"name\":\"REP_B\"",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":43",
                "\"viaUsageType\":\"GEOMETRIC_ITEM_SPECIFIC_USAGE\"",
                "\"viaUsageId\":26",
                "\"viaUsageId\":27",
                "\"viaUsageId\":28",
                "\"viaUsageId\":29",
                "\"viaUsageType\":\"ITEM_IDENTIFIED_REPRESENTATION_USAGE\"",
                "\"viaUsageId\":30",
                "\"viaUsageType\":\"CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE\"",
                "\"viaUsageId\":31",
                "\"viaUsageType\":\"DRAUGHTING_MODEL_ITEM_ASSOCIATION\"",
                "\"viaUsageId\":32",
                "\"viaUsageType\":\"PMI_REQUIREMENT_ITEM_ASSOCIATION\"",
                "\"viaUsageId\":33");
    }

    @Test
    void shouldEmbedPathAndWireUsageTargetMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=VECTOR('V0',#3,1.0);\n"
        + "#5=LINE('L0',#1,#4);\n"
        + "#6=VERTEX_POINT('VP0',#1);\n"
        + "#7=VERTEX_POINT('VP1',#2);\n"
        + "#8=EDGE_CURVE('E0',#6,#7,#5,.T.);\n"
        + "#9=ORIENTED_EDGE('OE0',$,$,#8,.T.);\n"
        + "#10=OPEN_PATH('OP',(#9));\n"
        + "#11=CONNECTED_EDGE_SET('CES',(#9));\n"
        + "#12=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');\n"
        + "#13=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#14=REPRESENTATION('REP_A',(),#13);\n"
        + "#15=REPRESENTATION('REP_B',(),#13);\n"
        + "#16=REPRESENTATION_RELATIONSHIP('RR','chain',#14,#15);\n"
        + "#17=ANNOTATION_TEXT_OCCURRENCE('NOTE','',#1);\n"
        + "#18=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU','',#17,#10);\n"
        + "#19=CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE('CGU','',#17,(#14,#15),(#16),#11);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE\"",
                "\"id\":10,\"type\":\"path\",\"name\":\"OP\"",
                "\"id\":11,\"type\":\"edge_set\",\"name\":\"CES\"");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE\"",
                "\"id\":10,\"type\":\"path\",\"name\":\"OP\"",
                "\"id\":11,\"type\":\"edge_set\",\"name\":\"CES\"");
    }

    @Test
    void shouldEmbedShellModelAndSolidUsageTargetMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#5=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#6=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#7=AXIS2_PLACEMENT_3D('AX',#1,#5,#6);\n"
        + "#8=PLANE('PL0',#7);\n"
        + "#13=POLY_LOOP('LOOP',(#1,#2,#3,#4));\n"
        + "#14=FACE_OUTER_BOUND('FOB',#13,.T.);\n"
        + "#15=ADVANCED_FACE('FACE0',(#14),#8,.T.);\n"
        + "#16=OPEN_SHELL('OSH',(#15));\n"
        + "#17=FACE_BASED_SURFACE_MODEL('FBM',(#16));\n"
        + "#18=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#19=REPRESENTATION('REP_A',(),#18);\n"
        + "#20=REPRESENTATION('REP_B',(),#18);\n"
        + "#21=REPRESENTATION_RELATIONSHIP('RR','chain',#19,#20);\n"
        + "#22=ANNOTATION_TEXT_OCCURRENCE('NOTE_SHELL','',#1);\n"
        + "#23=ANNOTATION_TEXT_OCCURRENCE('NOTE_MODEL','',#2);\n"
        + "#24=ANNOTATION_TEXT_OCCURRENCE('NOTE_SOLID','',#3);\n"
        + "#25=BLOCK('BLK',#7,1.0,1.0,1.0);\n"
        + "#26=POINT_SET('PS',(#1,#2));\n"
        + "#27=GEOMETRIC_CURVE_SET('GCS',(#26));\n"
        + "#28=GEOMETRIC_SET('GS',(#27));\n"
        + "#29=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU_SHELL','',#22,#16);\n"
        + "#30=CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE('CGU_MODEL','',#23,(#19,#20),(#21),#17);\n"
        + "#31=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU_SOLID','',#24,#25);\n"
        + "#32=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU_SET','',#24,#28);\n"
        + "#33=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#29,#19,#22,#22);\n"
        + "#34=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#30,#19,#23,#23);\n"
        + "#35=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#31,#19,#24,#24);\n"
        + "#36=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#32,#19,#24,#24);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_SHELL\"",
                "\"name\":\"NOTE_MODEL\"",
                "\"name\":\"NOTE_SOLID\"",
                "\"id\":26,\"type\":\"point_set\",\"name\":\"PS\"",
                "\"id\":27,\"type\":\"curve_set\",\"name\":\"GCS\"",
                "\"id\":28,\"type\":\"geometric_set\",\"name\":\"GS\"",
                "\"id\":16,\"type\":\"shell\",\"name\":\"OSH\"",
                "\"id\":17,\"type\":\"surface_model\",\"name\":\"FBM\"",
                "\"id\":25,\"type\":\"solid\",\"name\":\"BLK\"");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_SHELL\"",
                "\"name\":\"NOTE_MODEL\"",
                "\"name\":\"NOTE_SOLID\"",
                "\"id\":26,\"type\":\"point_set\",\"name\":\"PS\"",
                "\"id\":27,\"type\":\"curve_set\",\"name\":\"GCS\"",
                "\"id\":28,\"type\":\"geometric_set\",\"name\":\"GS\"",
                "\"id\":16,\"type\":\"shell\",\"name\":\"OSH\"",
                "\"id\":17,\"type\":\"surface_model\",\"name\":\"FBM\"",
                "\"id\":25,\"type\":\"solid\",\"name\":\"BLK\"");
    }

    @Test
    void shouldEmbedVertexLoopUsageTargetMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#8=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#9=VERTEX_POINT('VP0',#8);\n"
        + "#10=VERTEX_LOOP('VLOOP',#9);\n"
        + "#11=ANNOTATION_TEXT_OCCURRENCE('NOTE','',#8);\n"
        + "#12=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#13=REPRESENTATION('REP_A',(),#12);\n"
        + "#14=REPRESENTATION('REP_B',(),#12);\n"
        + "#15=REPRESENTATION_RELATIONSHIP('RR','chain',#13,#14);\n"
        + "#16=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU','',#11,#10);\n"
        + "#17=CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE('CGU','',#11,(#13,#14),(#15),#10);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE\"",
                "\"id\":10,\"type\":\"loop\",\"name\":\"VLOOP\"");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE\"",
                "\"id\":10,\"type\":\"loop\",\"name\":\"VLOOP\"");
    }

    @Test
    void shouldEmbedPmiDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA_BASE','base',#7,.T.);\n"
        + "#9=SPOTFACE_HOLE_OCCURRENCE('SA_OCC','occurrence',#7,.T.,#8);\n"
        + "#10=PROPERTY_DEFINITION('PD_ROOT','',#9);\n"
        + "#11=PROPERTY_DEFINITION('PD_TARGET','',#8);\n"
        + "#12=PROPERTY_DEFINITION_RELATIONSHIP('PDR','link',#10,#11);\n"
        + "#13=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#14=REPRESENTATION('REP_USED',(),#13);\n"
        + "#15=REPRESENTATION('REP_PROP',(),#13);\n"
        + "#16=REPRESENTATION('REP_DATUM',(),#13);\n"
        + "#17=PROPERTY_DEFINITION_REPRESENTATION(#11,#15);\n"
        + "#18=PLACED_DATUM_TARGET_FEATURE(#11,#16);\n"
        + "#19=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#20=ANNOTATION_TEXT_OCCURRENCE('NOTE','semantic',#19);\n"
        + "#21=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC','semantic link',#12,#14,#20,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE\"",
                "\"id\":14,\"type\":\"representation\",\"name\":\"REP_USED\"",
                "\"id\":15,\"type\":\"representation\",\"name\":\"REP_PROP\"",
                "\"id\":16,\"type\":\"representation\",\"name\":\"REP_DATUM\"",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE\"",
                "\"id\":14,\"type\":\"representation\",\"name\":\"REP_USED\"",
                "\"id\":15,\"type\":\"representation\",\"name\":\"REP_PROP\"",
                "\"id\":16,\"type\":\"representation\",\"name\":\"REP_DATUM\"",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12");
    }

    @Test
    void shouldEmbedRequirementAndRelationshipDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=GENERAL_PROPERTY('GP1','gp1','');\n"
        + "#9=GENERAL_PROPERTY('GP2','gp2','');\n"
        + "#10=GENERAL_PROPERTY_RELATIONSHIP('LINK','',#8,#9);\n"
        + "#11=PROPERTY_DEFINITION('PD_GP2','',#9);\n"
        + "#12=SPOTFACE_HOLE_OCCURRENCE('SA_OCC','occurrence',#7,.T.,#13);\n"
        + "#13=SHAPE_ASPECT('SA_BASE','base',#7,.T.);\n"
        + "#14=PROPERTY_DEFINITION('PD_OCC','',#12);\n"
        + "#15=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#16=REPRESENTATION('REP_USED_GP',(),#15);\n"
        + "#17=REPRESENTATION('REP_GP',(),#15);\n"
        + "#18=REPRESENTATION('REP_USED_REQ',(),#15);\n"
        + "#19=REPRESENTATION('REP_REQ',(),#15);\n"
        + "#20=ACTION_PROPERTY_REPRESENTATION(#11,#17);\n"
        + "#21=PROPERTY_DEFINITION_REPRESENTATION(#14,#19);\n"
        + "#22=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#23=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#24=ANNOTATION_TEXT_OCCURRENCE('NOTE_GP','',#22);\n"
        + "#25=ANNOTATION_TEXT_OCCURRENCE('NOTE_REQ','',#23);\n"
        + "#26=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_GP','',#8,#16,#24,#7);\n"
        + "#27=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_REQ','',#13,#18,#25,#12);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_GP\"",
                "\"name\":\"NOTE_REQ\"",
                "\"id\":17,\"type\":\"representation\",\"name\":\"REP_GP\"",
                "\"id\":19,\"type\":\"representation\",\"name\":\"REP_REQ\"",
                "\"viaDefinitionType\":\"GENERAL_PROPERTY_RELATIONSHIP\"",
                "\"viaDefinitionId\":10",
                "\"viaDefinitionType\":\"SPOTFACE_HOLE_OCCURRENCE\"",
                "\"viaDefinitionId\":12");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_GP\"",
                "\"name\":\"NOTE_REQ\"",
                "\"id\":17,\"type\":\"representation\",\"name\":\"REP_GP\"",
                "\"id\":19,\"type\":\"representation\",\"name\":\"REP_REQ\"",
                "\"viaDefinitionType\":\"GENERAL_PROPERTY_RELATIONSHIP\"",
                "\"viaDefinitionId\":10",
                "\"viaDefinitionType\":\"SPOTFACE_HOLE_OCCURRENCE\"",
                "\"viaDefinitionId\":12");
    }

    @Test
    void shouldEmbedProductAndRelationshipFamilyDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PROD_A','Product A','',(#2));\n"
        + "#4=PRODUCT('PROD_B','Product B','',(#2));\n"
        + "#5=PRODUCT_DEFINITION_FORMATION('vA','',#3);\n"
        + "#6=PRODUCT_DEFINITION_FORMATION('vB','',#4);\n"
        + "#7=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#8=PRODUCT_DEFINITION('pd-a','',#5,#7);\n"
        + "#9=PRODUCT_DEFINITION('pd-b','',#6,#7);\n"
        + "#10=PRODUCT_DEFINITION_SHAPE('pds-a','shape a',#8);\n"
        + "#11=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#12=REPRESENTATION('REP_PROD',(),#11);\n"
        + "#13=SHAPE_DEFINITION_REPRESENTATION(#10,#12);\n"
        + "#14=PRODUCT_RELATIONSHIP('PR','contains','',#4,#3);\n"
        + "#15=PRODUCT_DEFINITION_FORMATION_RELATIONSHIP('PFR','versions','',#6,#5);\n"
        + "#16=NEXT_ASSEMBLY_USAGE_OCCURRENCE('NAUO','occ','',#9,#8);\n"
        + "#17=PRODUCT_DEFINITION_RELATIONSHIP('PDR_1','peer-1','',#9,#8);\n"
        + "#18=PRODUCT_DEFINITION_RELATIONSHIP('PDR_2','peer-2','',#9,#8);\n"
        + "#19=PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP('PDRR','links','',#17,#18);\n"
        + "#20=GROUP('G1','g1');\n"
        + "#21=GROUP('G2','g2');\n"
        + "#22=GROUP_RELATIONSHIP('GR','',#20,#21);\n"
        + "#23=DOCUMENT_TYPE('spec');\n"
        + "#24=DOCUMENT('DOC-1','Spec A','',#23);\n"
        + "#25=DOCUMENT('DOC-2','Spec B','',#23);\n"
        + "#26=DOCUMENT_RELATIONSHIP('DR','',#24,#25);\n"
        + "#27=ORGANIZATION('ORG-1','Org A','');\n"
        + "#28=ORGANIZATION('ORG-2','Org B','');\n"
        + "#29=ORGANIZATION_RELATIONSHIP('OR','',#27,#28);\n"
        + "#30=PRODUCT_CATEGORY('CAT_A','cat a');\n"
        + "#31=PRODUCT_CATEGORY('CAT_B','cat b');\n"
        + "#32=PRODUCT_CATEGORY_RELATIONSHIP('CR','',#30,#31);\n"
        + "#33=EFFECTIVITY('E-1');\n"
        + "#34=EFFECTIVITY('E-2');\n"
        + "#35=EFFECTIVITY_RELATIONSHIP('ER','',#33,#34);\n"
        + "#36=PROPERTY_DEFINITION('PD_GROUP','',#21);\n"
        + "#37=PROPERTY_DEFINITION('PD_DOC','',#25);\n"
        + "#38=PROPERTY_DEFINITION('PD_ORG','',#28);\n"
        + "#39=PROPERTY_DEFINITION('PD_CAT','',#31);\n"
        + "#40=PROPERTY_DEFINITION('PD_EFF','',#34);\n"
        + "#41=PROPERTY_DEFINITION_REPRESENTATION(#36,#12);\n"
        + "#42=PROPERTY_DEFINITION_REPRESENTATION(#37,#12);\n"
        + "#43=PROPERTY_DEFINITION_REPRESENTATION(#38,#12);\n"
        + "#44=PROPERTY_DEFINITION_REPRESENTATION(#39,#12);\n"
        + "#45=PROPERTY_DEFINITION_REPRESENTATION(#40,#12);\n"
        + "#46=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#47=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#48=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#49=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#50=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#51=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#52=CARTESIAN_POINT('P6',(6.0,0.0,0.0));\n"
        + "#53=CARTESIAN_POINT('P7',(7.0,0.0,0.0));\n"
        + "#54=CARTESIAN_POINT('P8',(8.0,0.0,0.0));\n"
        + "#55=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRODUCT','',#46);\n"
        + "#56=ANNOTATION_TEXT_OCCURRENCE('NOTE_FORMATION','',#47);\n"
        + "#57=ANNOTATION_TEXT_OCCURRENCE('NOTE_OCCURRENCE','',#48);\n"
        + "#58=ANNOTATION_TEXT_OCCURRENCE('NOTE_PDRR','',#49);\n"
        + "#59=ANNOTATION_TEXT_OCCURRENCE('NOTE_GROUP','',#50);\n"
        + "#60=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOCUMENT','',#51);\n"
        + "#61=ANNOTATION_TEXT_OCCURRENCE('NOTE_ORGANIZATION','',#52);\n"
        + "#62=ANNOTATION_TEXT_OCCURRENCE('NOTE_CATEGORY','',#53);\n"
        + "#63=ANNOTATION_TEXT_OCCURRENCE('NOTE_EFFECTIVITY','',#54);\n"
        + "#64=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#4,#12,#55,#10);\n"
        + "#65=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#6,#12,#56,#10);\n"
        + "#66=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#16,#12,#57,#10);\n"
        + "#67=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#19,#12,#58,#10);\n"
        + "#68=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#20,#12,#59,#10);\n"
        + "#69=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#24,#12,#60,#10);\n"
        + "#70=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#27,#12,#61,#10);\n"
        + "#71=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#30,#12,#62,#10);\n"
        + "#72=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#33,#12,#63,#10);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"PRODUCT_RELATIONSHIP\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_FORMATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"NEXT_ASSEMBLY_USAGE_OCCURRENCE\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"GROUP\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"GROUP_RELATIONSHIP\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"DOCUMENT\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"DOCUMENT_TYPE\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"DOCUMENT_RELATIONSHIP\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"ORGANIZATION\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"ORGANIZATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"PRODUCT_CATEGORY\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"PRODUCT_CATEGORY_RELATIONSHIP\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"EFFECTIVITY\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"EFFECTIVITY_RELATIONSHIP\"",
                "\"viaDefinitionId\":35");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"PRODUCT_RELATIONSHIP\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_FORMATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"NEXT_ASSEMBLY_USAGE_OCCURRENCE\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"GROUP\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"GROUP_RELATIONSHIP\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"DOCUMENT\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"DOCUMENT_TYPE\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"DOCUMENT_RELATIONSHIP\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"ORGANIZATION\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"ORGANIZATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"PRODUCT_CATEGORY\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"PRODUCT_CATEGORY_RELATIONSHIP\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"EFFECTIVITY\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"EFFECTIVITY_RELATIONSHIP\"",
                "\"viaDefinitionId\":35");
    }

    @Test
    void shouldEmbedDirectRelationshipCarrierDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd-a','',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION('pd-b','',#4,#5);\n"
        + "#8=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#9=PROPERTY_DEFINITION('PD_ROOT','',#8);\n"
        + "#10=PROPERTY_DEFINITION('PD_TARGET','',#8);\n"
        + "#11=PROPERTY_DEFINITION_RELATIONSHIP('PDR','link',#9,#10);\n"
        + "#12=GENERAL_PROPERTY('GP1','gp1','');\n"
        + "#13=GENERAL_PROPERTY('GP2','gp2','');\n"
        + "#14=GENERAL_PROPERTY_RELATIONSHIP('GPR','',#12,#13);\n"
        + "#15=SHAPE_ASPECT('SA1','sa1',#8,.T.);\n"
        + "#16=SHAPE_ASPECT('SA2','sa2',#8,.T.);\n"
        + "#17=SHAPE_ASPECT_RELATIONSHIP('SAR','',#15,#16);\n"
        + "#18=PRODUCT_DEFINITION_RELATIONSHIP('PDR_A','peer-a','',#7,#6);\n"
        + "#19=PRODUCT_DEFINITION_RELATIONSHIP('PDR_B','peer-b','',#7,#6);\n"
        + "#20=PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP('PDRR','links','',#18,#19);\n"
        + "#21=PROPERTY_DEFINITION('PD_GP','',#13);\n"
        + "#22=PROPERTY_DEFINITION('PD_SA','',#16);\n"
        + "#23=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#24=REPRESENTATION('REP_PROP',(),#23);\n"
        + "#25=REPRESENTATION('REP_GP',(),#23);\n"
        + "#26=REPRESENTATION('REP_SA',(),#23);\n"
        + "#27=REPRESENTATION('REP_PDRR',(),#23);\n"
        + "#28=PROPERTY_DEFINITION_REPRESENTATION(#10,#24);\n"
        + "#29=ACTION_PROPERTY_REPRESENTATION(#21,#25);\n"
        + "#30=PROPERTY_DEFINITION_REPRESENTATION(#22,#26);\n"
        + "#31=SHAPE_DEFINITION_REPRESENTATION(#8,#27);\n"
        + "#32=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#33=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#34=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#35=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#36=ANNOTATION_TEXT_OCCURRENCE('NOTE_PDR','',#32);\n"
        + "#37=ANNOTATION_TEXT_OCCURRENCE('NOTE_GPR','',#33);\n"
        + "#38=ANNOTATION_TEXT_OCCURRENCE('NOTE_SAR','',#34);\n"
        + "#39=ANNOTATION_TEXT_OCCURRENCE('NOTE_PDRR','',#35);\n"
        + "#40=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#11,#24,#36,#8);\n"
        + "#41=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#14,#25,#37,#8);\n"
        + "#42=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#17,#26,#38,#8);\n"
        + "#43=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#20,#27,#39,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_PDR\"",
                "\"name\":\"NOTE_GPR\"",
                "\"name\":\"NOTE_SAR\"",
                "\"name\":\"NOTE_PDRR\"",
                "\"id\":24,\"type\":\"representation\",\"name\":\"REP_PROP\"",
                "\"id\":25,\"type\":\"representation\",\"name\":\"REP_GP\"",
                "\"id\":26,\"type\":\"representation\",\"name\":\"REP_SA\"",
                "\"id\":27,\"type\":\"representation\",\"name\":\"REP_PDRR\"",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_RELATIONSHIP\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"GENERAL_PROPERTY_RELATIONSHIP\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"SHAPE_ASPECT_RELATIONSHIP\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP\"",
                "\"viaDefinitionId\":20");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_PDR\"",
                "\"name\":\"NOTE_GPR\"",
                "\"name\":\"NOTE_SAR\"",
                "\"name\":\"NOTE_PDRR\"",
                "\"id\":24,\"type\":\"representation\",\"name\":\"REP_PROP\"",
                "\"id\":25,\"type\":\"representation\",\"name\":\"REP_GP\"",
                "\"id\":26,\"type\":\"representation\",\"name\":\"REP_SA\"",
                "\"id\":27,\"type\":\"representation\",\"name\":\"REP_PDRR\"",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_RELATIONSHIP\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"GENERAL_PROPERTY_RELATIONSHIP\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"SHAPE_ASPECT_RELATIONSHIP\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP\"",
                "\"viaDefinitionId\":20");
    }

    @Test
    void shouldEmbedDirectProductAndMetadataRelationshipCarrierDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PROD_A','Product A','',(#2));\n"
        + "#4=PRODUCT('PROD_B','Product B','',(#2));\n"
        + "#5=PRODUCT_DEFINITION_FORMATION('vA','',#3);\n"
        + "#6=PRODUCT_DEFINITION_FORMATION('vB','',#4);\n"
        + "#7=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#8=PRODUCT_DEFINITION('pd-a','',#5,#7);\n"
        + "#9=PRODUCT_DEFINITION('pd-b','',#6,#7);\n"
        + "#10=PRODUCT_DEFINITION_SHAPE('pds-a','shape a',#8);\n"
        + "#11=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#12=REPRESENTATION('REP_REL',(),#11);\n"
        + "#13=SHAPE_DEFINITION_REPRESENTATION(#10,#12);\n"
        + "#14=PRODUCT_RELATIONSHIP('PR','contains','',#4,#3);\n"
        + "#15=PRODUCT_DEFINITION_FORMATION_RELATIONSHIP('PFR','versions','',#6,#5);\n"
        + "#16=GROUP('G1','g1');\n"
        + "#17=GROUP('G2','g2');\n"
        + "#18=GROUP_RELATIONSHIP('GR','',#16,#17);\n"
        + "#19=DOCUMENT_TYPE('spec');\n"
        + "#20=DOCUMENT('DOC-1','Spec A','',#19);\n"
        + "#21=DOCUMENT('DOC-2','Spec B','',#19);\n"
        + "#22=DOCUMENT_RELATIONSHIP('DR','',#20,#21);\n"
        + "#23=ORGANIZATION('ORG-1','Org A','');\n"
        + "#24=ORGANIZATION('ORG-2','Org B','');\n"
        + "#25=ORGANIZATION_RELATIONSHIP('OR','',#23,#24);\n"
        + "#26=PRODUCT_CATEGORY('CAT_A','cat a');\n"
        + "#27=PRODUCT_CATEGORY('CAT_B','cat b');\n"
        + "#28=PRODUCT_CATEGORY_RELATIONSHIP('CR','',#26,#27);\n"
        + "#29=EFFECTIVITY('E-1');\n"
        + "#30=EFFECTIVITY('E-2');\n"
        + "#31=EFFECTIVITY_RELATIONSHIP('ER','',#29,#30);\n"
        + "#32=PROPERTY_DEFINITION('PD_GROUP','',#17);\n"
        + "#33=PROPERTY_DEFINITION('PD_DOC','',#21);\n"
        + "#34=PROPERTY_DEFINITION('PD_ORG','',#24);\n"
        + "#35=PROPERTY_DEFINITION('PD_CAT','',#27);\n"
        + "#36=PROPERTY_DEFINITION('PD_EFF','',#30);\n"
        + "#37=PROPERTY_DEFINITION_REPRESENTATION(#32,#12);\n"
        + "#38=PROPERTY_DEFINITION_REPRESENTATION(#33,#12);\n"
        + "#39=PROPERTY_DEFINITION_REPRESENTATION(#34,#12);\n"
        + "#40=PROPERTY_DEFINITION_REPRESENTATION(#35,#12);\n"
        + "#41=PROPERTY_DEFINITION_REPRESENTATION(#36,#12);\n"
        + "#42=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#43=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#44=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#45=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#46=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#47=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#48=CARTESIAN_POINT('P6',(6.0,0.0,0.0));\n"
        + "#49=ANNOTATION_TEXT_OCCURRENCE('NOTE_PR','',#42);\n"
        + "#50=ANNOTATION_TEXT_OCCURRENCE('NOTE_PFR','',#43);\n"
        + "#51=ANNOTATION_TEXT_OCCURRENCE('NOTE_GR','',#44);\n"
        + "#52=ANNOTATION_TEXT_OCCURRENCE('NOTE_DR','',#45);\n"
        + "#53=ANNOTATION_TEXT_OCCURRENCE('NOTE_OR','',#46);\n"
        + "#54=ANNOTATION_TEXT_OCCURRENCE('NOTE_CR','',#47);\n"
        + "#55=ANNOTATION_TEXT_OCCURRENCE('NOTE_ER','',#48);\n"
        + "#56=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#14,#12,#49,#10);\n"
        + "#57=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#15,#12,#50,#10);\n"
        + "#58=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#18,#12,#51,#10);\n"
        + "#59=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#22,#12,#52,#10);\n"
        + "#60=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#25,#12,#53,#10);\n"
        + "#61=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#28,#12,#54,#10);\n"
        + "#62=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#31,#12,#55,#10);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_PR\"",
                "\"name\":\"NOTE_PFR\"",
                "\"name\":\"NOTE_GR\"",
                "\"name\":\"NOTE_DR\"",
                "\"name\":\"NOTE_OR\"",
                "\"name\":\"NOTE_CR\"",
                "\"name\":\"NOTE_ER\"",
                "\"id\":12,\"type\":\"representation\",\"name\":\"REP_REL\"",
                "\"viaDefinitionType\":\"PRODUCT_RELATIONSHIP\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_FORMATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"GROUP_RELATIONSHIP\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"DOCUMENT_RELATIONSHIP\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"ORGANIZATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"PRODUCT_CATEGORY_RELATIONSHIP\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"EFFECTIVITY_RELATIONSHIP\"",
                "\"viaDefinitionId\":31");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_PR\"",
                "\"name\":\"NOTE_PFR\"",
                "\"name\":\"NOTE_GR\"",
                "\"name\":\"NOTE_DR\"",
                "\"name\":\"NOTE_OR\"",
                "\"name\":\"NOTE_CR\"",
                "\"name\":\"NOTE_ER\"",
                "\"id\":12,\"type\":\"representation\",\"name\":\"REP_REL\"",
                "\"viaDefinitionType\":\"PRODUCT_RELATIONSHIP\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_FORMATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"GROUP_RELATIONSHIP\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"DOCUMENT_RELATIONSHIP\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"ORGANIZATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"PRODUCT_CATEGORY_RELATIONSHIP\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"EFFECTIVITY_RELATIONSHIP\"",
                "\"viaDefinitionId\":31");
    }

    @Test
    void shouldEmbedExternalCategoryAndLayerDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_META_EXT',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=PRODUCT_RELATED_PRODUCT_CATEGORY('CAT_LINK','',(#3));\n"
        + "#13=DOCUMENT_TYPE('spec');\n"
        + "#14=DOCUMENT('DOC-1','Spec A','',#13);\n"
        + "#15=DOCUMENT('DOC-2','Spec B','',#13);\n"
        + "#16=DOCUMENT_RELATIONSHIP('DR','',#14,#15);\n"
        + "#17=DOCUMENT_REFERENCE(#14,'internal');\n"
        + "#18=PROPERTY_DEFINITION('PD_DOC','',#15);\n"
        + "#19=PROPERTY_DEFINITION_REPRESENTATION(#18,#10);\n"
        + "#20=EXTERNAL_SOURCE('SRC_A');\n"
        + "#21=EXTERNAL_SOURCE('SRC_B');\n"
        + "#22=EXTERNAL_SOURCE_RELATIONSHIP('SR','',#20,#21);\n"
        + "#23=EXTERNALLY_DEFINED_ITEM('EXT-1',#21);\n"
        + "#24=PROPERTY_DEFINITION('PD_EXT','',#23);\n"
        + "#25=PROPERTY_DEFINITION_REPRESENTATION(#24,#10);\n"
        + "#26=PRESENTATION_LAYER_ASSIGNMENT('LAYER_A','',(#10));\n"
        + "#27=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#28=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#29=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#30=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#31=ANNOTATION_TEXT_OCCURRENCE('NOTE_CATEGORY','',#27);\n"
        + "#32=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOCUMENT_REF','',#28);\n"
        + "#33=ANNOTATION_TEXT_OCCURRENCE('NOTE_EXTERNAL_SOURCE','',#29);\n"
        + "#34=ANNOTATION_TEXT_OCCURRENCE('NOTE_LAYER','',#30);\n"
        + "#35=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#12,#10,#31,#8);\n"
        + "#36=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#17,#10,#32,#8);\n"
        + "#37=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#20,#10,#33,#8);\n"
        + "#38=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#26,#10,#34,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"PRODUCT_RELATED_PRODUCT_CATEGORY\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"DOCUMENT_REFERENCE\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"DOCUMENT\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"DOCUMENT_TYPE\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"EXTERNALLY_DEFINED_ITEM\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"DOCUMENT_RELATIONSHIP\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE_RELATIONSHIP\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"PRESENTATION_LAYER_ASSIGNMENT\"",
                "\"viaDefinitionId\":26");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"PRODUCT_RELATED_PRODUCT_CATEGORY\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"DOCUMENT_REFERENCE\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"DOCUMENT\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"DOCUMENT_TYPE\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"EXTERNALLY_DEFINED_ITEM\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"DOCUMENT_RELATIONSHIP\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE_RELATIONSHIP\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"PRESENTATION_LAYER_ASSIGNMENT\"",
                "\"viaDefinitionId\":26");
    }

    @Test
    void shouldEmbedBareAssignmentDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_META_ASSIGN',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=APPROVAL_STATUS('approved');\n"
        + "#13=APPROVAL(#12,'design');\n"
        + "#14=APPROVAL_ASSIGNMENT(#13);\n"
        + "#15=SECURITY_CLASSIFICATION_LEVEL('unclassified');\n"
        + "#16=SECURITY_CLASSIFICATION('sec','export control',#15);\n"
        + "#17=SECURITY_CLASSIFICATION_ASSIGNMENT(#16);\n"
        + "#18=CONTRACT_TYPE('purchase');\n"
        + "#19=CONTRACT('C-1','supply',#18);\n"
        + "#20=CONTRACT_ASSIGNMENT(#19);\n"
        + "#21=CERTIFICATION_TYPE('material');\n"
        + "#22=CERTIFICATION('CERT-1','compliance',#21);\n"
        + "#23=CERTIFICATION_ASSIGNMENT(#22);\n"
        + "#24=PERSON('p-1','Doe','Jane',$,$,$);\n"
        + "#25=ORGANIZATION('org-1','Acme','engineering');\n"
        + "#26=PERSON_AND_ORGANIZATION(#24,#25);\n"
        + "#27=PERSON_AND_ORGANIZATION_ROLE('creator');\n"
        + "#28=PERSON_AND_ORGANIZATION_ASSIGNMENT(#26,#27);\n"
        + "#29=LANGUAGE('en-US');\n"
        + "#30=LANGUAGE_ASSIGNMENT(#29);\n"
        + "#90=APPROVAL_ROLE('authorizer');\n"
        + "#91=APPROVAL_PERSON_ORGANIZATION(#26,#13,#90);\n"
        + "#93=CALENDAR_DATE(2026,11,4);\n"
        + "#94=COORDINATED_UNIVERSAL_TIME_OFFSET(8,0,.AHEAD.);\n"
        + "#95=LOCAL_TIME(9,15,$,#94);\n"
        + "#96=DATE_AND_TIME(#93,#95);\n"
        + "#92=APPROVAL_DATE_TIME(#96,#13);\n"
        + "#31=GROUP('G1','group');\n"
        + "#32=GROUP_ASSIGNMENT(#31);\n"
        + "#33=CLASSIFICATION_ROLE('family');\n"
        + "#34=CLASSIFICATION_ASSIGNMENT(#31,#33);\n"
        + "#35=CALENDAR_DATE(2026,11,4);\n"
        + "#36=DATE_ROLE('release');\n"
        + "#37=DATE_ASSIGNMENT(#35,#36);\n"
        + "#38=COORDINATED_UNIVERSAL_TIME_OFFSET(8,0,.AHEAD.);\n"
        + "#39=LOCAL_TIME(9,15,$,#38);\n"
        + "#40=DATE_AND_TIME(#35,#39);\n"
        + "#41=DATE_TIME_ROLE('created');\n"
        + "#42=DATE_TIME_ASSIGNMENT(#40,#41);\n"
        + "#43=IDENTIFICATION_ROLE('ext role');\n"
        + "#44=EXTERNAL_SOURCE('SRC_EXT');\n"
        + "#45=EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT-42',#43,#44);\n"
        + "#97=EXTERNAL_SOURCE('SRC_LINK');\n"
        + "#98=EXTERNAL_SOURCE_RELATIONSHIP('SR','',#44,#97);\n"
        + "#99=EXTERNALLY_DEFINED_ITEM('EXT-REF',#97);\n"
        + "#46=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#47=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#48=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#49=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#50=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#51=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#52=CARTESIAN_POINT('P6',(6.0,0.0,0.0));\n"
        + "#53=CARTESIAN_POINT('P7',(7.0,0.0,0.0));\n"
        + "#54=CARTESIAN_POINT('P8',(8.0,0.0,0.0));\n"
        + "#55=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL','',#46);\n"
        + "#56=ANNOTATION_TEXT_OCCURRENCE('NOTE_SECURITY','',#47);\n"
        + "#57=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONTRACT','',#48);\n"
        + "#58=ANNOTATION_TEXT_OCCURRENCE('NOTE_CERTIFICATION','',#49);\n"
        + "#59=ANNOTATION_TEXT_OCCURRENCE('NOTE_PERSON_ORG','',#50);\n"
        + "#60=ANNOTATION_TEXT_OCCURRENCE('NOTE_LANGUAGE','',#51);\n"
        + "#61=ANNOTATION_TEXT_OCCURRENCE('NOTE_GROUP_ASSIGN','',#52);\n"
        + "#62=ANNOTATION_TEXT_OCCURRENCE('NOTE_CLASS_ASSIGN','',#53);\n"
        + "#63=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_ASSIGN','',#54);\n"
        + "#64=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_TIME_ASSIGN','',#46);\n"
        + "#65=ANNOTATION_TEXT_OCCURRENCE('NOTE_EXTERNAL_ID','',#47);\n"
        + "#66=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#14,#10,#55,#8);\n"
        + "#67=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#17,#10,#56,#8);\n"
        + "#68=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#20,#10,#57,#8);\n"
        + "#69=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#23,#10,#58,#8);\n"
        + "#70=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#28,#10,#59,#8);\n"
        + "#71=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#30,#10,#60,#8);\n"
        + "#72=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#32,#10,#61,#8);\n"
        + "#73=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#34,#10,#62,#8);\n"
        + "#74=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#37,#10,#63,#8);\n"
        + "#75=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#42,#10,#64,#8);\n"
        + "#76=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#45,#10,#65,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"APPROVAL_ASSIGNMENT\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"APPROVAL\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"APPROVAL_STATUS\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"APPROVAL_PERSON_ORGANIZATION\"",
                "\"viaDefinitionId\":91",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"PERSON\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"ORGANIZATION\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"APPROVAL_ROLE\"",
                "\"viaDefinitionId\":90",
                "\"viaDefinitionType\":\"APPROVAL_DATE_TIME\"",
                "\"viaDefinitionId\":92",
                "\"viaDefinitionType\":\"DATE_AND_TIME\"",
                "\"viaDefinitionId\":96",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":93",
                "\"viaDefinitionType\":\"LOCAL_TIME\"",
                "\"viaDefinitionId\":95",
                "\"viaDefinitionType\":\"COORDINATED_UNIVERSAL_TIME_OFFSET\"",
                "\"viaDefinitionId\":94",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION_LEVEL\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"CONTRACT_ASSIGNMENT\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"CONTRACT\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"CONTRACT_TYPE\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CERTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"CERTIFICATION\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"CERTIFICATION_TYPE\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"PERSON\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"ORGANIZATION\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION_ROLE\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"LANGUAGE_ASSIGNMENT\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"LANGUAGE\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"GROUP_ASSIGNMENT\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"GROUP\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"CLASSIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"CLASSIFICATION_ROLE\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"DATE_ASSIGNMENT\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"DATE_TIME_ASSIGNMENT\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"DATE_AND_TIME\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"LOCAL_TIME\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionType\":\"COORDINATED_UNIVERSAL_TIME_OFFSET\"",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"EXTERNAL_IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE\"",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE_RELATIONSHIP\"",
                "\"viaDefinitionId\":98",
                "\"viaDefinitionType\":\"EXTERNALLY_DEFINED_ITEM\"",
                "\"viaDefinitionId\":99");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"APPROVAL_ASSIGNMENT\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"APPROVAL\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"APPROVAL_STATUS\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"APPROVAL_PERSON_ORGANIZATION\"",
                "\"viaDefinitionId\":91",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"PERSON\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"ORGANIZATION\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"APPROVAL_ROLE\"",
                "\"viaDefinitionId\":90",
                "\"viaDefinitionType\":\"APPROVAL_DATE_TIME\"",
                "\"viaDefinitionId\":92",
                "\"viaDefinitionType\":\"DATE_AND_TIME\"",
                "\"viaDefinitionId\":96",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":93",
                "\"viaDefinitionType\":\"LOCAL_TIME\"",
                "\"viaDefinitionId\":95",
                "\"viaDefinitionType\":\"COORDINATED_UNIVERSAL_TIME_OFFSET\"",
                "\"viaDefinitionId\":94",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION_LEVEL\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"CONTRACT_ASSIGNMENT\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"CONTRACT\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"CONTRACT_TYPE\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CERTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"CERTIFICATION\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"CERTIFICATION_TYPE\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"PERSON\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"ORGANIZATION\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION_ROLE\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"LANGUAGE_ASSIGNMENT\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"LANGUAGE\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"GROUP_ASSIGNMENT\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"GROUP\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"CLASSIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"CLASSIFICATION_ROLE\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"DATE_ASSIGNMENT\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"DATE_TIME_ASSIGNMENT\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"DATE_AND_TIME\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"LOCAL_TIME\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionType\":\"COORDINATED_UNIVERSAL_TIME_OFFSET\"",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"EXTERNAL_IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE\"",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE_RELATIONSHIP\"",
                "\"viaDefinitionId\":98",
                "\"viaDefinitionType\":\"EXTERNALLY_DEFINED_ITEM\"",
                "\"viaDefinitionId\":99");
    }

    @Test
    void shouldEmbedAppliedGroupAssignmentDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_META_GROUP',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=GROUP('G1','group');\n"
        + "#13=APPLIED_GROUP_ASSIGNMENT(#12,(#8));\n"
        + "#14=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#15=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPLIED_GROUP','',#14);\n"
        + "#16=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#13,#10,#15,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"APPLIED_GROUP_ASSIGNMENT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"GROUP\"",
                "\"viaDefinitionId\":12");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"APPLIED_GROUP_ASSIGNMENT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"GROUP\"",
                "\"viaDefinitionId\":12");
    }

    @Test
    void shouldEmbedMetadataWrapperDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=PROPERTY_DEFINITION('PD_TARGET','',#8);\n"
        + "#10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#11=REPRESENTATION('REP_TARGET',(),#10);\n"
        + "#12=PROPERTY_DEFINITION_REPRESENTATION(#9,#11);\n"
        + "#13=NAME_ATTRIBUTE('N0',#8);\n"
        + "#14=DESCRIPTION_ATTRIBUTE('D0',#8);\n"
        + "#15=ID_ATTRIBUTE('I0',#8);\n"
        + "#16=IDENTIFICATION_ROLE('role');\n"
        + "#17=EXTERNAL_SOURCE('src');\n"
        + "#18=APPLIED_NAME_ASSIGNMENT('APPLIED_NAME',(#8));\n"
        + "#19=APPLIED_IDENTIFICATION_ASSIGNMENT('APPLIED_ID',#16,(#8));\n"
        + "#20=APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT('APPLIED_EXT',#16,#17,(#8));\n"
        + "#90=EXTERNAL_SOURCE('src-linked');\n"
        + "#91=EXTERNAL_SOURCE_RELATIONSHIP('SR','',#17,#90);\n"
        + "#92=EXTERNALLY_DEFINED_ITEM('EXT-APPLIED',#90);\n"
        + "#21=ATTRIBUTE_ASSERTION(#9,#11);\n"
        + "#22=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#23=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#24=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#25=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#26=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#27=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#28=CARTESIAN_POINT('P6',(6.0,0.0,0.0));\n"
        + "#29=ANNOTATION_TEXT_OCCURRENCE('NOTE_NAME_ATTR','',#22);\n"
        + "#30=ANNOTATION_TEXT_OCCURRENCE('NOTE_DESC_ATTR','',#23);\n"
        + "#31=ANNOTATION_TEXT_OCCURRENCE('NOTE_ID_ATTR','',#24);\n"
        + "#32=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPLIED_NAME','',#25);\n"
        + "#33=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPLIED_ID','',#26);\n"
        + "#34=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPLIED_EXT','',#27);\n"
        + "#35=ANNOTATION_TEXT_OCCURRENCE('NOTE_ASSERT','',#28);\n"
        + "#36=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#13,#11,#29,#8);\n"
        + "#37=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#14,#11,#30,#8);\n"
        + "#38=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#15,#11,#31,#8);\n"
        + "#39=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#18,#11,#32,#8);\n"
        + "#40=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#19,#11,#33,#8);\n"
        + "#41=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#20,#11,#34,#8);\n"
        + "#42=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#21,#11,#35,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"NAME_ATTRIBUTE\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"DESCRIPTION_ATTRIBUTE\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"ID_ATTRIBUTE\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"APPLIED_NAME_ASSIGNMENT\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"APPLIED_IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"IDENTIFICATION_ROLE\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE_RELATIONSHIP\"",
                "\"viaDefinitionId\":91",
                "\"viaDefinitionType\":\"EXTERNALLY_DEFINED_ITEM\"",
                "\"viaDefinitionId\":92",
                "\"viaDefinitionType\":\"ATTRIBUTE_ASSERTION\"",
                "\"viaDefinitionId\":21");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"NAME_ATTRIBUTE\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"DESCRIPTION_ATTRIBUTE\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"ID_ATTRIBUTE\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"APPLIED_NAME_ASSIGNMENT\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"APPLIED_IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"IDENTIFICATION_ROLE\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE_RELATIONSHIP\"",
                "\"viaDefinitionId\":91",
                "\"viaDefinitionType\":\"EXTERNALLY_DEFINED_ITEM\"",
                "\"viaDefinitionId\":92",
                "\"viaDefinitionType\":\"ATTRIBUTE_ASSERTION\"",
                "\"viaDefinitionId\":21");
    }

    @Test
    void shouldEmbedAppliedAndPlainMetadataAssignmentDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_TARGET',(),#9);\n"
        + "#11=CALENDAR_DATE(2026,11,4);\n"
        + "#12=DATE_ROLE('release');\n"
        + "#13=COORDINATED_UNIVERSAL_TIME_OFFSET(8,0,.AHEAD.);\n"
        + "#14=LOCAL_TIME(9,15,$,#13);\n"
        + "#15=DATE_AND_TIME(#11,#14);\n"
        + "#16=DATE_TIME_ROLE('created');\n"
        + "#17=APPROVAL_STATUS('approved');\n"
        + "#18=APPROVAL(#17,'design');\n"
        + "#19=SECURITY_CLASSIFICATION_LEVEL('unclassified');\n"
        + "#20=SECURITY_CLASSIFICATION('sec','export control',#19);\n"
        + "#21=DOCUMENT_TYPE('specification');\n"
        + "#22=DOCUMENT('DOC-1','Spec','primary spec',#21);\n"
        + "#23=CONTRACT_TYPE('purchase');\n"
        + "#24=CONTRACT('C-1','supply',#23);\n"
        + "#25=CERTIFICATION_TYPE('material');\n"
        + "#26=CERTIFICATION('CERT-1','compliance',#25);\n"
        + "#27=PERSON('p-1','Doe','Jane',$,$,$);\n"
        + "#28=ORGANIZATION('org-1','Acme','engineering');\n"
        + "#29=PERSON_AND_ORGANIZATION(#27,#28);\n"
        + "#30=PERSON_AND_ORGANIZATION_ROLE('creator');\n"
        + "#31=ORGANIZATION_ROLE('owner');\n"
        + "#32=LANGUAGE('en-US');\n"
        + "#90=APPROVAL_ROLE('authorizer');\n"
        + "#91=APPROVAL_PERSON_ORGANIZATION(#29,#18,#90);\n"
        + "#92=APPROVAL_DATE_TIME(#15,#18);\n"
        + "#33=APPLIED_DATE_ASSIGNMENT(#11,#12,(#8));\n"
        + "#34=APPLIED_DATE_AND_TIME_ASSIGNMENT(#15,#16,(#8));\n"
        + "#35=APPLIED_APPROVAL_ASSIGNMENT(#18,(#8));\n"
        + "#36=APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT(#20,(#8));\n"
        + "#37=APPLIED_DOCUMENT_REFERENCE(#22,'internal',(#8));\n"
        + "#38=APPLIED_CONTRACT_ASSIGNMENT(#24,(#8));\n"
        + "#39=APPLIED_CERTIFICATION_ASSIGNMENT(#26,(#8));\n"
        + "#40=APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT(#29,#30,(#8));\n"
        + "#41=APPLIED_ORGANIZATION_ASSIGNMENT(#28,#31,(#8));\n"
        + "#42=APPLIED_LANGUAGE_ASSIGNMENT(#32,(#8));\n"
        + "#43=NAME_ASSIGNMENT('NAME_META');\n"
        + "#44=IDENTIFICATION_ROLE('role');\n"
        + "#45=IDENTIFICATION_ASSIGNMENT('ID_META',#44);\n"
        + "#46=EXTERNAL_SOURCE('SRC');\n"
        + "#47=EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT_META',#44,#46);\n"
        + "#48=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#49=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#50=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#51=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#52=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#53=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#54=CARTESIAN_POINT('P6',(6.0,0.0,0.0));\n"
        + "#55=CARTESIAN_POINT('P7',(7.0,0.0,0.0));\n"
        + "#56=CARTESIAN_POINT('P8',(8.0,0.0,0.0));\n"
        + "#57=CARTESIAN_POINT('P9',(9.0,0.0,0.0));\n"
        + "#58=CARTESIAN_POINT('P10',(10.0,0.0,0.0));\n"
        + "#59=CARTESIAN_POINT('P11',(11.0,0.0,0.0));\n"
        + "#60=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE','',#48);\n"
        + "#61=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_TIME','',#49);\n"
        + "#62=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL','',#50);\n"
        + "#63=ANNOTATION_TEXT_OCCURRENCE('NOTE_SECURITY','',#51);\n"
        + "#64=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOCUMENT','',#52);\n"
        + "#65=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONTRACT','',#53);\n"
        + "#66=ANNOTATION_TEXT_OCCURRENCE('NOTE_CERTIFICATION','',#54);\n"
        + "#67=ANNOTATION_TEXT_OCCURRENCE('NOTE_PERSON_ORG','',#55);\n"
        + "#68=ANNOTATION_TEXT_OCCURRENCE('NOTE_ORGANIZATION','',#56);\n"
        + "#69=ANNOTATION_TEXT_OCCURRENCE('NOTE_LANGUAGE','',#57);\n"
        + "#70=ANNOTATION_TEXT_OCCURRENCE('NOTE_NAME_ASSIGN','',#58);\n"
        + "#71=ANNOTATION_TEXT_OCCURRENCE('NOTE_ID_ASSIGN','',#59);\n"
        + "#72=ANNOTATION_TEXT_OCCURRENCE('NOTE_EXT_ID_ASSIGN','',#60);\n"
        + "#73=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#33,#10,#60,#8);\n"
        + "#74=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#34,#10,#61,#8);\n"
        + "#75=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#35,#10,#62,#8);\n"
        + "#76=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#36,#10,#63,#8);\n"
        + "#77=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#37,#10,#64,#8);\n"
        + "#78=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#38,#10,#65,#8);\n"
        + "#79=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#39,#10,#66,#8);\n"
        + "#80=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#40,#10,#67,#8);\n"
        + "#81=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#41,#10,#68,#8);\n"
        + "#82=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#42,#10,#69,#8);\n"
        + "#83=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#43,#10,#70,#8);\n"
        + "#84=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#45,#10,#71,#8);\n"
        + "#85=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#47,#10,#72,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"APPLIED_DATE_ASSIGNMENT\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"APPLIED_DATE_AND_TIME_ASSIGNMENT\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"DATE_AND_TIME\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"LOCAL_TIME\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"COORDINATED_UNIVERSAL_TIME_OFFSET\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"APPLIED_APPROVAL_ASSIGNMENT\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"APPROVAL\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"APPROVAL_PERSON_ORGANIZATION\"",
                "\"viaDefinitionId\":91",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"PERSON\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"ORGANIZATION\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"APPROVAL_ROLE\"",
                "\"viaDefinitionId\":90",
                "\"viaDefinitionType\":\"APPROVAL_DATE_TIME\"",
                "\"viaDefinitionId\":92",
                "\"viaDefinitionType\":\"DATE_AND_TIME\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"LOCAL_TIME\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"COORDINATED_UNIVERSAL_TIME_OFFSET\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"APPLIED_DOCUMENT_REFERENCE\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"DOCUMENT\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"APPLIED_CONTRACT_ASSIGNMENT\"",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"CONTRACT\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"APPLIED_CERTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionType\":\"CERTIFICATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"PERSON\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"APPLIED_ORGANIZATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionType\":\"ORGANIZATION\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"APPLIED_LANGUAGE_ASSIGNMENT\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"NAME_ASSIGNMENT\"",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionType\":\"IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionType\":\"EXTERNAL_IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionType\":\"DATE_ROLE\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"DATE_TIME_ROLE\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"APPROVAL_STATUS\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION_LEVEL\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"DOCUMENT_TYPE\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"CONTRACT_TYPE\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"CERTIFICATION_TYPE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION_ROLE\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"ORGANIZATION_ROLE\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"LANGUAGE\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"IDENTIFICATION_ROLE\"",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE\"",
                "\"viaDefinitionId\":46");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"APPLIED_DATE_ASSIGNMENT\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"APPLIED_DATE_AND_TIME_ASSIGNMENT\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"DATE_AND_TIME\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"LOCAL_TIME\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"COORDINATED_UNIVERSAL_TIME_OFFSET\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"APPLIED_APPROVAL_ASSIGNMENT\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"APPROVAL\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"APPROVAL_PERSON_ORGANIZATION\"",
                "\"viaDefinitionId\":91",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"PERSON\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"ORGANIZATION\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"APPROVAL_ROLE\"",
                "\"viaDefinitionId\":90",
                "\"viaDefinitionType\":\"APPROVAL_DATE_TIME\"",
                "\"viaDefinitionId\":92",
                "\"viaDefinitionType\":\"DATE_AND_TIME\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"LOCAL_TIME\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"COORDINATED_UNIVERSAL_TIME_OFFSET\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"APPLIED_DOCUMENT_REFERENCE\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"DOCUMENT\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"APPLIED_CONTRACT_ASSIGNMENT\"",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"CONTRACT\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"APPLIED_CERTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionType\":\"CERTIFICATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"PERSON\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"APPLIED_ORGANIZATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionType\":\"ORGANIZATION\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"APPLIED_LANGUAGE_ASSIGNMENT\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"NAME_ASSIGNMENT\"",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionType\":\"IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionType\":\"EXTERNAL_IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionType\":\"DATE_ROLE\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"DATE_TIME_ROLE\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"APPROVAL_STATUS\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION_LEVEL\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"DOCUMENT_TYPE\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"CONTRACT_TYPE\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"CERTIFICATION_TYPE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION_ROLE\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"ORGANIZATION_ROLE\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"LANGUAGE\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"IDENTIFICATION_ROLE\"",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE\"",
                "\"viaDefinitionId\":46");
    }

    @Test
    void shouldPreserveRepresentationSubtypeNameInBinaryPreviewAndGlbDefinitionMetadata() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=SHAPE_REPRESENTATION('REP_SHAPE',(),#9);\n"
        + "#11=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#12=ANNOTATION_TEXT_OCCURRENCE('NOTE_SHAPE_REP','',#11);\n"
        + "#13=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#10,#10,#12,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION\"",
                "\"viaDefinitionId\":10");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION\"",
                "\"viaDefinitionId\":10");
    }

    @Test
    void shouldEmbedRepresentationRelationshipDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_USAGE',(),#9);\n"
        + "#11=REPRESENTATION('REP_REL_A',(),#9);\n"
        + "#12=REPRESENTATION('REP_REL_B',(),#9);\n"
        + "#13=REPRESENTATION('REP_REL_C',(),#9);\n"
        + "#14=REPRESENTATION('REP_REL_D',(),#9);\n"
        + "#15=REPRESENTATION('REP_REL_E',(),#9);\n"
        + "#16=REPRESENTATION('REP_REL_F',(),#9);\n"
        + "#17=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#18=REPRESENTATION_RELATIONSHIP('RR','',#11,#12);\n"
        + "#19=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#20=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#21=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#22=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#23=AXIS2_PLACEMENT_3D('AX0',#19,#21,#22);\n"
        + "#24=AXIS2_PLACEMENT_3D('AX1',#20,#21,#22);\n"
        + "#25=ITEM_DEFINED_TRANSFORMATION('T1','',#23,#24);\n"
        + "#26=(REPRESENTATION_RELATIONSHIP('RRT','',#13,#14)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#25));\n"
        + "#27=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#15,#16);\n"
        + "#30=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#31=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#32=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#33=ANNOTATION_TEXT_OCCURRENCE('NOTE_REP_REL','',#30);\n"
        + "#34=ANNOTATION_TEXT_OCCURRENCE('NOTE_REP_REL_XFORM','',#31);\n"
        + "#35=ANNOTATION_TEXT_OCCURRENCE('NOTE_SHAPE_REP_REL','',#32);\n"
        + "#36=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#18,#10,#33,#8);\n"
        + "#37=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#26,#10,#34,#8);\n"
        + "#38=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#27,#10,#35,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":27");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":27");
    }

    @Test
    void shouldEmbedShapeRepresentationLinkDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('ASM','Assembly','Assembly',(#2));\n"
        + "#4=PRODUCT('COMP','Component','Component',(#2));\n"
        + "#5=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#6=PRODUCT_DEFINITION_FORMATION('v1','',#4);\n"
        + "#7=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#8=PRODUCT_DEFINITION('asm_pd','assembly',#5,#7);\n"
        + "#9=PRODUCT_DEFINITION('comp_pd','component',#6,#7);\n"
        + "#10=PRODUCT_DEFINITION_SHAPE('asm_shape','',#8);\n"
        + "#11=PRODUCT_DEFINITION_SHAPE('comp_shape','',#9);\n"
        + "#12=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ-1','OCC','component usage',#8,#9,'R1');\n"
        + "#13=PRODUCT_DEFINITION_SHAPE('occ_shape','',#12);\n"
        + "#20=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#21=REPRESENTATION('REP_COMP',(),#20);\n"
        + "#22=REPRESENTATION('REP_OCC',(),#20);\n"
        + "#23=REPRESENTATION('REP_USAGE_OCC',(),#20);\n"
        + "#24=REPRESENTATION('REP_USAGE_PD',(),#20);\n"
        + "#25=REPRESENTATION('REP_USAGE_PDS',(),#20);\n"
        + "#26=SHAPE_DEFINITION_REPRESENTATION(#11,#21);\n"
        + "#27=REPRESENTATION_RELATIONSHIP('CTX','occ ctx',#21,#22);\n"
        + "#28=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#27,#13);\n"
        + "#29=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#30=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#31=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#38=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#32=ANNOTATION_TEXT_OCCURRENCE('NOTE_OCC','occ',#29);\n"
        + "#33=ANNOTATION_TEXT_OCCURRENCE('NOTE_PD','pd',#30);\n"
        + "#34=ANNOTATION_TEXT_OCCURRENCE('NOTE_PDS','pds',#31);\n"
        + "#39=ANNOTATION_TEXT_OCCURRENCE('NOTE_SDR','sdr',#38);\n"
        + "#35=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_OCC','',#12,#23,#32,#13);\n"
        + "#36=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_PD','',#9,#24,#33,#11);\n"
        + "#37=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_PDS','',#11,#25,#34,#11);\n"
        + "#40=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_SDR','',#26,#25,#39,#11);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"SHAPE_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"CONTEXT_DEPENDENT_SHAPE_REPRESENTATION\"",
                "\"viaDefinitionId\":28");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"SHAPE_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"CONTEXT_DEPENDENT_SHAPE_REPRESENTATION\"",
                "\"viaDefinitionId\":28");
    }

    @Test
    void shouldEmbedTransformedAssemblyShapeDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('ASM','Assembly','Assembly',(#2));\n"
        + "#4=PRODUCT('COMP','Component','Component',(#2));\n"
        + "#5=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#6=PRODUCT_DEFINITION_FORMATION('v1','',#4);\n"
        + "#7=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#8=PRODUCT_DEFINITION('asm_pd','assembly',#5,#7);\n"
        + "#9=PRODUCT_DEFINITION('comp_pd','component',#6,#7);\n"
        + "#10=PRODUCT_DEFINITION_SHAPE('asm_shape','',#8);\n"
        + "#11=PRODUCT_DEFINITION_SHAPE('comp_shape','',#9);\n"
        + "#12=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ-1','OCC','component usage',#8,#9,'R1');\n"
        + "#13=PRODUCT_DEFINITION_SHAPE('occ_shape','',#12);\n"
        + "#20=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#21=REPRESENTATION('REP_COMP',(),#20);\n"
        + "#22=REPRESENTATION('REP_OCC',(),#20);\n"
        + "#23=REPRESENTATION('REP_USAGE_OCC',(),#20);\n"
        + "#24=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#25=CARTESIAN_POINT('TX1',(5.0,0.0,0.0));\n"
        + "#26=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#27=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#28=AXIS2_PLACEMENT_3D('AX0',#24,#26,#27);\n"
        + "#29=AXIS2_PLACEMENT_3D('AX1',#25,#26,#27);\n"
        + "#30=ITEM_DEFINED_TRANSFORMATION('T1','',#28,#29);\n"
        + "#31=(REPRESENTATION_RELATIONSHIP('CTX','occ ctx',#21,#22)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#30));\n"
        + "#32=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#31,#13);\n"
        + "#33=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#34=ANNOTATION_TEXT_OCCURRENCE('NOTE_OCC','occ',#33);\n"
        + "#35=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_OCC','',#12,#23,#34,#13);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"CONTEXT_DEPENDENT_SHAPE_REPRESENTATION\"",
                "\"viaDefinitionId\":32");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"CONTEXT_DEPENDENT_SHAPE_REPRESENTATION\"",
                "\"viaDefinitionId\":32");
    }

    @Test
    void shouldEmbedProductShapeRepresentationRelationshipMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_USAGE',(),#9);\n"
        + "#11=REPRESENTATION('REP_REL_A',(),#9);\n"
        + "#12=REPRESENTATION('REP_REL_B',(),#9);\n"
        + "#13=REPRESENTATION('REP_REL_C',(),#9);\n"
        + "#14=REPRESENTATION('REP_REL_D',(),#9);\n"
        + "#15=REPRESENTATION('REP_REL_E',(),#9);\n"
        + "#16=REPRESENTATION('REP_REL_F',(),#9);\n"
        + "#17=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#18=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);\n"
        + "#19=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#20=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#21=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#22=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#23=AXIS2_PLACEMENT_3D('AX0',#19,#21,#22);\n"
        + "#24=AXIS2_PLACEMENT_3D('AX1',#20,#21,#22);\n"
        + "#25=ITEM_DEFINED_TRANSFORMATION('T1','',#23,#24);\n"
        + "#26=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#12)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#25));\n"
        + "#27=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#13);\n"
        + "#30=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#31=ANNOTATION_TEXT_OCCURRENCE('NOTE_PDS','',#30);\n"
        + "#32=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#7,#10,#31,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_SHAPE\"",
                "\"viaDefinitionId\":7",
                "\"viaDefinitionType\":\"SHAPE_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":27");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_SHAPE\"",
                "\"viaDefinitionId\":7",
                "\"viaDefinitionType\":\"SHAPE_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":27");
    }

    @Test
    void shouldEmbedContextAndProtocolDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=APPLICATION_PROTOCOL_DEFINITION('draft','AP203',2026,#1);\n"
        + "#3=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#4=PRODUCT('PRT','Part','Part',(#3));\n"
        + "#5=PRODUCT_DEFINITION_FORMATION('v1','',#4);\n"
        + "#6=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#7=PRODUCT_DEFINITION('pd','part def',#5,#6);\n"
        + "#8=PRODUCT_DEFINITION_SHAPE('pds','shape',#7);\n"
        + "#9=SHAPE_ASPECT('SA0','base',#8,.T.);\n"
        + "#10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#11=REPRESENTATION('REP_META_CTX',(),#10);\n"
        + "#82=REPRESENTATION('REP_META_AUX0',(),#10);\n"
        + "#83=REPRESENTATION('REP_META_AUX1',(),#10);\n"
        + "#84=REPRESENTATION_RELATIONSHIP('RR_VAR','',#11,#82);\n"
        + "#85=CARTESIAN_POINT('TX0',(20.0,0.0,0.0));\n"
        + "#86=CARTESIAN_POINT('TX1',(21.0,0.0,0.0));\n"
        + "#87=DIRECTION('DZV',(0.0,0.0,1.0));\n"
        + "#88=DIRECTION('DXV',(1.0,0.0,0.0));\n"
        + "#89=AXIS2_PLACEMENT_3D('AXV0',#85,#87,#88);\n"
        + "#90=AXIS2_PLACEMENT_3D('AXV1',#86,#87,#88);\n"
        + "#91=ITEM_DEFINED_TRANSFORMATION('TV','',#89,#90);\n"
        + "#92=(REPRESENTATION_RELATIONSHIP('RRT_VAR','',#11,#83)\n"
        + "    REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#91));\n"
        + "#93=SHAPE_REPRESENTATION_RELATIONSHIP('SRR_VAR','',#11,#82);\n"
        + "#12=SHAPE_DEFINITION_REPRESENTATION(#8,#11);\n"
        + "#13=CHARACTERIZED_OBJECT('CO','characterized object');\n"
        + "#14=PROPERTY_DEFINITION('PD_CO','',#13);\n"
        + "#15=ABSTRACT_VARIABLE(#14,#11);\n"
        + "#16=ROW_VARIABLE(#14,#11);\n"
        + "#17=SCALAR_VARIABLE(#14,#11);\n"
        + "#18=FORWARD_CHAINING_RULE_PREMISE(#14,#11);\n"
        + "#19=BACK_CHAINING_RULE_BODY(#14,#11);\n"
        + "#20=CALENDAR_DATE(2026,11,4);\n"
        + "#21=DATE_ROLE('release');\n"
        + "#22=APPLIED_DATE_ASSIGNMENT(#20,#21,(#9));\n"
        + "#23=COORDINATED_UNIVERSAL_TIME_OFFSET(8,0,.AHEAD.);\n"
        + "#24=LOCAL_TIME(9,15,$,#23);\n"
        + "#25=DATE_AND_TIME(#20,#24);\n"
        + "#26=DATE_TIME_ROLE('created');\n"
        + "#27=APPLIED_DATE_AND_TIME_ASSIGNMENT(#25,#26,(#9));\n"
        + "#28=PERSON('p-1','Doe','Jane',$,$,$);\n"
        + "#29=ORGANIZATION('org-1','Acme','engineering');\n"
        + "#30=PERSON_AND_ORGANIZATION(#28,#29);\n"
        + "#31=PERSON_AND_ORGANIZATION_ROLE('creator');\n"
        + "#32=APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT(#30,#31,(#9));\n"
        + "#40=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#41=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#42=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#43=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#44=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#45=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#46=CARTESIAN_POINT('P6',(6.0,0.0,0.0));\n"
        + "#47=CARTESIAN_POINT('P7',(7.0,0.0,0.0));\n"
        + "#48=CARTESIAN_POINT('P8',(8.0,0.0,0.0));\n"
        + "#49=CARTESIAN_POINT('P9',(9.0,0.0,0.0));\n"
        + "#50=CARTESIAN_POINT('P10',(10.0,0.0,0.0));\n"
        + "#51=CARTESIAN_POINT('P11',(11.0,0.0,0.0));\n"
        + "#52=CARTESIAN_POINT('P12',(12.0,0.0,0.0));\n"
        + "#53=ANNOTATION_TEXT_OCCURRENCE('NOTE_APP_PROTOCOL','',#40);\n"
        + "#54=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRODUCT_CONTEXT','',#41);\n"
        + "#55=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRODUCT_DEF_CONTEXT','',#42);\n"
        + "#56=ANNOTATION_TEXT_OCCURRENCE('NOTE_CHARACTERIZED_OBJECT','',#43);\n"
        + "#57=ANNOTATION_TEXT_OCCURRENCE('NOTE_ABSTRACT_VARIABLE','',#44);\n"
        + "#58=ANNOTATION_TEXT_OCCURRENCE('NOTE_ROW_VARIABLE','',#45);\n"
        + "#59=ANNOTATION_TEXT_OCCURRENCE('NOTE_SCALAR_VARIABLE','',#46);\n"
        + "#60=ANNOTATION_TEXT_OCCURRENCE('NOTE_FORWARD_RULE','',#47);\n"
        + "#61=ANNOTATION_TEXT_OCCURRENCE('NOTE_BACK_RULE','',#48);\n"
        + "#62=ANNOTATION_TEXT_OCCURRENCE('NOTE_CALENDAR_DATE','',#49);\n"
        + "#63=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_AND_TIME','',#50);\n"
        + "#64=ANNOTATION_TEXT_OCCURRENCE('NOTE_LOCAL_TIME','',#51);\n"
        + "#65=ANNOTATION_TEXT_OCCURRENCE('NOTE_UTC_OFFSET','',#52);\n"
        + "#66=CARTESIAN_POINT('P13',(13.0,0.0,0.0));\n"
        + "#67=ANNOTATION_TEXT_OCCURRENCE('NOTE_PERSON','',#66);\n"
        + "#68=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#2,#11,#53,#9);\n"
        + "#69=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#3,#11,#54,#9);\n"
        + "#70=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#6,#11,#55,#9);\n"
        + "#71=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#13,#11,#56,#9);\n"
        + "#72=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#15,#11,#57,#9);\n"
        + "#73=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#16,#11,#58,#9);\n"
        + "#74=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#17,#11,#59,#9);\n"
        + "#75=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#18,#11,#60,#9);\n"
        + "#76=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#19,#11,#61,#9);\n"
        + "#77=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#20,#11,#62,#9);\n"
        + "#78=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#25,#11,#63,#9);\n"
        + "#79=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#24,#11,#64,#9);\n"
        + "#80=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#23,#11,#65,#9);\n"
        + "#81=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#28,#11,#67,#9);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"APPLICATION_PROTOCOL_DEFINITION\"",
                "\"viaDefinitionId\":2",
                "\"viaDefinitionType\":\"APPLICATION_CONTEXT\"",
                "\"viaDefinitionId\":1",
                "\"viaDefinitionType\":\"PRODUCT_CONTEXT\"",
                "\"viaDefinitionId\":3",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_CONTEXT\"",
                "\"viaDefinitionId\":6",
                "\"viaDefinitionType\":\"CHARACTERIZED_OBJECT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"ABSTRACT_VARIABLE\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"ROW_VARIABLE\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"SCALAR_VARIABLE\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"FORWARD_CHAINING_RULE_PREMISE\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"BACK_CHAINING_RULE_BODY\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":84",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":92",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":91",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":89",
                "\"viaDefinitionId\":90",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":85",
                "\"viaDefinitionId\":86",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":87",
                "\"viaDefinitionId\":88",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":93",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"DATE_AND_TIME\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"LOCAL_TIME\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"COORDINATED_UNIVERSAL_TIME_OFFSET\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"PERSON\"",
                "\"viaDefinitionId\":28");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"APPLICATION_PROTOCOL_DEFINITION\"",
                "\"viaDefinitionId\":2",
                "\"viaDefinitionType\":\"APPLICATION_CONTEXT\"",
                "\"viaDefinitionId\":1",
                "\"viaDefinitionType\":\"PRODUCT_CONTEXT\"",
                "\"viaDefinitionId\":3",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_CONTEXT\"",
                "\"viaDefinitionId\":6",
                "\"viaDefinitionType\":\"CHARACTERIZED_OBJECT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"ABSTRACT_VARIABLE\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"ROW_VARIABLE\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"SCALAR_VARIABLE\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"FORWARD_CHAINING_RULE_PREMISE\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"BACK_CHAINING_RULE_BODY\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":84",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":92",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":91",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":89",
                "\"viaDefinitionId\":90",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":85",
                "\"viaDefinitionId\":86",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":87",
                "\"viaDefinitionId\":88",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":93",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"DATE_AND_TIME\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"LOCAL_TIME\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"COORDINATED_UNIVERSAL_TIME_OFFSET\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"PERSON\"",
                "\"viaDefinitionId\":28");
    }

    @Test
    void shouldEmbedMetadataRolesStatusesAndTypesInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_META_ROLES',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=APPROVAL_STATUS('released');\n"
        + "#13=APPROVAL(#12,'design');\n"
        + "#14=APPLIED_APPROVAL_ASSIGNMENT(#13,(#8));\n"
        + "#15=SECURITY_CLASSIFICATION_LEVEL('controlled');\n"
        + "#16=SECURITY_CLASSIFICATION('sec','purpose',#15);\n"
        + "#17=APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT(#16,(#8));\n"
        + "#18=CONTRACT_TYPE('purchase');\n"
        + "#19=CONTRACT('C-1','supply',#18);\n"
        + "#20=APPLIED_CONTRACT_ASSIGNMENT(#19,(#8));\n"
        + "#21=CERTIFICATION_TYPE('material');\n"
        + "#22=CERTIFICATION('CERT-1','compliance',#21);\n"
        + "#23=APPLIED_CERTIFICATION_ASSIGNMENT(#22,(#8));\n"
        + "#24=PERSON('p-1','Doe','Jane',$,$,$);\n"
        + "#25=ORGANIZATION('org-1','Acme','engineering');\n"
        + "#26=PERSON_AND_ORGANIZATION(#24,#25);\n"
        + "#27=APPROVAL_ROLE('authorizer');\n"
        + "#28=APPROVAL_PERSON_ORGANIZATION(#26,#13,#27);\n"
        + "#108=APPROVAL_DATE_TIME(#41,#13);\n"
        + "#29=PERSON_AND_ORGANIZATION_ROLE('creator');\n"
        + "#30=APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT(#26,#29,(#8));\n"
        + "#31=ORGANIZATION_ROLE('owner');\n"
        + "#32=APPLIED_ORGANIZATION_ASSIGNMENT(#25,#31,(#8));\n"
        + "#33=GROUP('G1','group');\n"
        + "#34=CLASSIFICATION_ROLE('family');\n"
        + "#35=APPLIED_CLASSIFICATION_ASSIGNMENT(#33,#34,(#8));\n"
        + "#36=CALENDAR_DATE(2026,11,4);\n"
        + "#37=DATE_ROLE('release');\n"
        + "#38=APPLIED_DATE_ASSIGNMENT(#36,#37,(#8));\n"
        + "#39=COORDINATED_UNIVERSAL_TIME_OFFSET(8,0,.AHEAD.);\n"
        + "#40=LOCAL_TIME(9,15,$,#39);\n"
        + "#41=DATE_AND_TIME(#36,#40);\n"
        + "#42=DATE_TIME_ROLE('created');\n"
        + "#43=APPLIED_DATE_AND_TIME_ASSIGNMENT(#41,#42,(#8));\n"
        + "#44=IDENTIFICATION_ROLE('identifier');\n"
        + "#45=EXTERNAL_SOURCE('SRC_EXT');\n"
        + "#46=APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT-42',#44,#45,(#8));\n"
        + "#47=DOCUMENT_TYPE('spec');\n"
        + "#48=DOCUMENT('DOC-1','Spec A','',#47);\n"
        + "#49=PROPERTY_DEFINITION('PD_DOC','',#48);\n"
        + "#50=PROPERTY_DEFINITION_REPRESENTATION(#49,#10);\n"
        + "#51=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#52=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#53=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#54=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#55=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#56=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#57=CARTESIAN_POINT('P6',(6.0,0.0,0.0));\n"
        + "#58=CARTESIAN_POINT('P7',(7.0,0.0,0.0));\n"
        + "#59=CARTESIAN_POINT('P8',(8.0,0.0,0.0));\n"
        + "#60=CARTESIAN_POINT('P9',(9.0,0.0,0.0));\n"
        + "#61=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL_STATUS','',#51);\n"
        + "#62=ANNOTATION_TEXT_OCCURRENCE('NOTE_SECURITY_LEVEL','',#52);\n"
        + "#63=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONTRACT_TYPE','',#53);\n"
        + "#64=ANNOTATION_TEXT_OCCURRENCE('NOTE_CERTIFICATION_TYPE','',#54);\n"
        + "#65=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL_ROLE','',#55);\n"
        + "#66=ANNOTATION_TEXT_OCCURRENCE('NOTE_PERSON_ORG_ROLE','',#56);\n"
        + "#67=ANNOTATION_TEXT_OCCURRENCE('NOTE_ORG_ROLE','',#57);\n"
        + "#68=ANNOTATION_TEXT_OCCURRENCE('NOTE_CLASS_ROLE','',#58);\n"
        + "#69=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_ROLE','',#59);\n"
        + "#70=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_TIME_ROLE','',#60);\n"
        + "#71=CARTESIAN_POINT('P10',(10.0,0.0,0.0));\n"
        + "#72=CARTESIAN_POINT('P11',(11.0,0.0,0.0));\n"
        + "#73=ANNOTATION_TEXT_OCCURRENCE('NOTE_ID_ROLE','',#71);\n"
        + "#74=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOCUMENT_TYPE','',#72);\n"
        + "#75=CARTESIAN_POINT('P12',(12.0,0.0,0.0));\n"
        + "#76=CARTESIAN_POINT('P13',(13.0,0.0,0.0));\n"
        + "#77=CARTESIAN_POINT('P14',(14.0,0.0,0.0));\n"
        + "#78=CARTESIAN_POINT('P15',(15.0,0.0,0.0));\n"
        + "#79=CARTESIAN_POINT('P16',(16.0,0.0,0.0));\n"
        + "#80=CARTESIAN_POINT('P17',(17.0,0.0,0.0));\n"
        + "#81=CARTESIAN_POINT('P18',(18.0,0.0,0.0));\n"
        + "#82=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL','',#75);\n"
        + "#83=ANNOTATION_TEXT_OCCURRENCE('NOTE_SECURITY','',#76);\n"
        + "#84=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONTRACT','',#77);\n"
        + "#85=ANNOTATION_TEXT_OCCURRENCE('NOTE_CERTIFICATION','',#78);\n"
        + "#86=ANNOTATION_TEXT_OCCURRENCE('NOTE_PERSON_ORG','',#79);\n"
        + "#87=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL_PERSON_ORG','',#80);\n"
        + "#88=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL_DATE_TIME','',#81);\n"
        + "#89=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#12,#10,#61,#8);\n"
        + "#90=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#15,#10,#62,#8);\n"
        + "#91=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#18,#10,#63,#8);\n"
        + "#92=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#21,#10,#64,#8);\n"
        + "#93=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#27,#10,#65,#8);\n"
        + "#94=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#29,#10,#66,#8);\n"
        + "#95=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#31,#10,#67,#8);\n"
        + "#96=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#34,#10,#68,#8);\n"
        + "#97=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#37,#10,#69,#8);\n"
        + "#98=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#42,#10,#70,#8);\n"
        + "#99=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#44,#10,#73,#8);\n"
        + "#100=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#47,#10,#74,#8);\n"
        + "#101=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#13,#10,#82,#8);\n"
        + "#102=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#16,#10,#83,#8);\n"
        + "#103=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#19,#10,#84,#8);\n"
        + "#104=PMI_REQUIREMENT_ITEM_ASSOCIATION('A15','',#22,#10,#85,#8);\n"
        + "#105=PMI_REQUIREMENT_ITEM_ASSOCIATION('A16','',#26,#10,#86,#8);\n"
        + "#106=PMI_REQUIREMENT_ITEM_ASSOCIATION('A17','',#28,#10,#87,#8);\n"
        + "#107=PMI_REQUIREMENT_ITEM_ASSOCIATION('A18','',#108,#10,#88,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"APPROVAL_STATUS\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION_LEVEL\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"CONTRACT_TYPE\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CERTIFICATION_TYPE\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"APPROVAL_ROLE\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION_ROLE\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"ORGANIZATION_ROLE\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"CLASSIFICATION_ROLE\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"DATE_ROLE\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"DATE_TIME_ROLE\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"IDENTIFICATION_ROLE\"",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionType\":\"DOCUMENT_TYPE\"",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionType\":\"APPROVAL\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"CONTRACT\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"CERTIFICATION\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"APPROVAL_PERSON_ORGANIZATION\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"APPROVAL_DATE_TIME\"",
                "\"viaDefinitionId\":108");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"APPROVAL_STATUS\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION_LEVEL\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"CONTRACT_TYPE\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CERTIFICATION_TYPE\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"APPROVAL_ROLE\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION_ROLE\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"ORGANIZATION_ROLE\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"CLASSIFICATION_ROLE\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"DATE_ROLE\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"DATE_TIME_ROLE\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"IDENTIFICATION_ROLE\"",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionType\":\"DOCUMENT_TYPE\"",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionType\":\"APPROVAL\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"CONTRACT\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"CERTIFICATION\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"APPROVAL_PERSON_ORGANIZATION\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"APPROVAL_DATE_TIME\"",
                "\"viaDefinitionId\":108");
    }

    @Test
    void shouldEmbedDirectMetadataLeafAndEndpointLinkDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_DIRECT_META',(),#9);\n"
        + "#11=REPRESENTATION('REP_AUX',(),#9);\n"
        + "#12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);\n"
        + "#13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#15=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#16=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);\n"
        + "#18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);\n"
        + "#19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);\n"
        + "#20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));\n"
        + "#21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);\n"
        + "#22=CALENDAR_DATE(2026,11,4);\n"
        + "#23=COORDINATED_UNIVERSAL_TIME_OFFSET(8,0,.AHEAD.);\n"
        + "#24=LOCAL_TIME(9,15,$,#23);\n"
        + "#25=DATE_AND_TIME(#22,#24);\n"
        + "#26=APPROVAL_STATUS('released');\n"
        + "#27=APPROVAL(#26,'design');\n"
        + "#28=APPROVAL_ROLE('authorizer');\n"
        + "#29=PERSON('p-1','Doe','Jane',$,$,$);\n"
        + "#30=ORGANIZATION('org-1','Acme','engineering');\n"
        + "#31=PERSON_AND_ORGANIZATION(#29,#30);\n"
        + "#32=APPROVAL_PERSON_ORGANIZATION(#31,#27,#28);\n"
        + "#33=APPROVAL_DATE_TIME(#25,#27);\n"
        + "#34=SECURITY_CLASSIFICATION_LEVEL('controlled');\n"
        + "#35=SECURITY_CLASSIFICATION('sec','purpose',#34);\n"
        + "#36=CONTRACT_TYPE('purchase');\n"
        + "#37=CONTRACT('C-1','supply',#36);\n"
        + "#38=CERTIFICATION_TYPE('material');\n"
        + "#39=CERTIFICATION('CERT-1','compliance',#38);\n"
        + "#40=PERSON_AND_ORGANIZATION_ROLE('creator');\n"
        + "#41=ORGANIZATION_ROLE('owner');\n"
        + "#42=CLASSIFICATION_ROLE('family');\n"
        + "#43=DATE_ROLE('release');\n"
        + "#44=DATE_TIME_ROLE('created');\n"
        + "#45=IDENTIFICATION_ROLE('identifier');\n"
        + "#46=DOCUMENT_TYPE('spec');\n"
        + "#47=LANGUAGE('en-US');\n"
        + "#48=PROPERTY_DEFINITION('PD_CAL_DATE','',#22);\n"
        + "#49=PROPERTY_DEFINITION_REPRESENTATION(#48,#10);\n"
        + "#50=PROPERTY_DEFINITION('PD_ZONE','',#23);\n"
        + "#51=PROPERTY_DEFINITION_REPRESENTATION(#50,#10);\n"
        + "#52=PROPERTY_DEFINITION('PD_LOCAL_TIME','',#24);\n"
        + "#53=PROPERTY_DEFINITION_REPRESENTATION(#52,#10);\n"
        + "#54=PROPERTY_DEFINITION('PD_DATE_TIME','',#25);\n"
        + "#55=PROPERTY_DEFINITION_REPRESENTATION(#54,#10);\n"
        + "#56=PROPERTY_DEFINITION('PD_APPROVAL_STATUS','',#26);\n"
        + "#57=PROPERTY_DEFINITION_REPRESENTATION(#56,#10);\n"
        + "#58=PROPERTY_DEFINITION('PD_APPROVAL','',#27);\n"
        + "#59=PROPERTY_DEFINITION_REPRESENTATION(#58,#10);\n"
        + "#60=PROPERTY_DEFINITION('PD_SECURITY_LEVEL','',#34);\n"
        + "#61=PROPERTY_DEFINITION_REPRESENTATION(#60,#10);\n"
        + "#62=PROPERTY_DEFINITION('PD_SECURITY','',#35);\n"
        + "#63=PROPERTY_DEFINITION_REPRESENTATION(#62,#10);\n"
        + "#64=PROPERTY_DEFINITION('PD_CONTRACT_TYPE','',#36);\n"
        + "#65=PROPERTY_DEFINITION_REPRESENTATION(#64,#10);\n"
        + "#66=PROPERTY_DEFINITION('PD_CONTRACT','',#37);\n"
        + "#67=PROPERTY_DEFINITION_REPRESENTATION(#66,#10);\n"
        + "#68=PROPERTY_DEFINITION('PD_CERT_TYPE','',#38);\n"
        + "#69=PROPERTY_DEFINITION_REPRESENTATION(#68,#10);\n"
        + "#70=PROPERTY_DEFINITION('PD_CERT','',#39);\n"
        + "#71=PROPERTY_DEFINITION_REPRESENTATION(#70,#10);\n"
        + "#72=PROPERTY_DEFINITION('PD_PERSON','',#29);\n"
        + "#73=PROPERTY_DEFINITION_REPRESENTATION(#72,#10);\n"
        + "#74=PROPERTY_DEFINITION('PD_PERSON_ORG','',#31);\n"
        + "#75=PROPERTY_DEFINITION_REPRESENTATION(#74,#10);\n"
        + "#76=PROPERTY_DEFINITION('PD_APPROVAL_ROLE','',#28);\n"
        + "#77=PROPERTY_DEFINITION_REPRESENTATION(#76,#10);\n"
        + "#78=PROPERTY_DEFINITION('PD_PERSON_ORG_ROLE','',#40);\n"
        + "#79=PROPERTY_DEFINITION_REPRESENTATION(#78,#10);\n"
        + "#80=PROPERTY_DEFINITION('PD_ORG_ROLE','',#41);\n"
        + "#81=PROPERTY_DEFINITION_REPRESENTATION(#80,#10);\n"
        + "#82=PROPERTY_DEFINITION('PD_CLASS_ROLE','',#42);\n"
        + "#83=PROPERTY_DEFINITION_REPRESENTATION(#82,#10);\n"
        + "#84=PROPERTY_DEFINITION('PD_DATE_ROLE','',#43);\n"
        + "#85=PROPERTY_DEFINITION_REPRESENTATION(#84,#10);\n"
        + "#86=PROPERTY_DEFINITION('PD_DATE_TIME_ROLE','',#44);\n"
        + "#87=PROPERTY_DEFINITION_REPRESENTATION(#86,#10);\n"
        + "#88=PROPERTY_DEFINITION('PD_ID_ROLE','',#45);\n"
        + "#89=PROPERTY_DEFINITION_REPRESENTATION(#88,#10);\n"
        + "#90=PROPERTY_DEFINITION('PD_DOC_TYPE','',#46);\n"
        + "#91=PROPERTY_DEFINITION_REPRESENTATION(#90,#10);\n"
        + "#92=PROPERTY_DEFINITION('PD_LANGUAGE','',#47);\n"
        + "#93=PROPERTY_DEFINITION_REPRESENTATION(#92,#10);\n"
        + "#100=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#101=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#102=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#103=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#104=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#105=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#106=CARTESIAN_POINT('P6',(6.0,0.0,0.0));\n"
        + "#107=CARTESIAN_POINT('P7',(7.0,0.0,0.0));\n"
        + "#108=CARTESIAN_POINT('P8',(8.0,0.0,0.0));\n"
        + "#109=CARTESIAN_POINT('P9',(9.0,0.0,0.0));\n"
        + "#110=CARTESIAN_POINT('P10',(10.0,0.0,0.0));\n"
        + "#111=CARTESIAN_POINT('P11',(11.0,0.0,0.0));\n"
        + "#112=CARTESIAN_POINT('P12',(12.0,0.0,0.0));\n"
        + "#113=CARTESIAN_POINT('P13',(13.0,0.0,0.0));\n"
        + "#114=CARTESIAN_POINT('P14',(14.0,0.0,0.0));\n"
        + "#115=CARTESIAN_POINT('P15',(15.0,0.0,0.0));\n"
        + "#116=CARTESIAN_POINT('P16',(16.0,0.0,0.0));\n"
        + "#117=ANNOTATION_TEXT_OCCURRENCE('NOTE_CAL_DATE','',#100);\n"
        + "#118=ANNOTATION_TEXT_OCCURRENCE('NOTE_ZONE','',#101);\n"
        + "#119=ANNOTATION_TEXT_OCCURRENCE('NOTE_LOCAL_TIME','',#102);\n"
        + "#120=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_TIME','',#103);\n"
        + "#121=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL_STATUS','',#104);\n"
        + "#122=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL','',#105);\n"
        + "#123=ANNOTATION_TEXT_OCCURRENCE('NOTE_SECURITY_LEVEL','',#106);\n"
        + "#124=ANNOTATION_TEXT_OCCURRENCE('NOTE_SECURITY','',#107);\n"
        + "#125=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONTRACT_TYPE','',#108);\n"
        + "#126=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONTRACT','',#109);\n"
        + "#127=ANNOTATION_TEXT_OCCURRENCE('NOTE_CERT_TYPE','',#110);\n"
        + "#128=ANNOTATION_TEXT_OCCURRENCE('NOTE_CERT','',#111);\n"
        + "#129=ANNOTATION_TEXT_OCCURRENCE('NOTE_PERSON','',#112);\n"
        + "#130=ANNOTATION_TEXT_OCCURRENCE('NOTE_PERSON_ORG','',#113);\n"
        + "#131=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL_ROLE','',#114);\n"
        + "#132=ANNOTATION_TEXT_OCCURRENCE('NOTE_PERSON_ORG_ROLE','',#115);\n"
        + "#133=ANNOTATION_TEXT_OCCURRENCE('NOTE_ORG_ROLE','',#116);\n"
        + "#134=CARTESIAN_POINT('P17',(17.0,0.0,0.0));\n"
        + "#135=CARTESIAN_POINT('P18',(18.0,0.0,0.0));\n"
        + "#136=CARTESIAN_POINT('P19',(19.0,0.0,0.0));\n"
        + "#137=CARTESIAN_POINT('P20',(20.0,0.0,0.0));\n"
        + "#138=CARTESIAN_POINT('P21',(21.0,0.0,0.0));\n"
        + "#139=ANNOTATION_TEXT_OCCURRENCE('NOTE_CLASS_ROLE','',#134);\n"
        + "#140=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_ROLE','',#135);\n"
        + "#141=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_TIME_ROLE','',#136);\n"
        + "#142=ANNOTATION_TEXT_OCCURRENCE('NOTE_ID_ROLE','',#137);\n"
        + "#143=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOC_TYPE','',#138);\n"
        + "#144=ANNOTATION_TEXT_OCCURRENCE('NOTE_LANGUAGE','',#139);\n"
        + "#145=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#22,#10,#117,#8);\n"
        + "#146=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#23,#10,#118,#8);\n"
        + "#147=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#24,#10,#119,#8);\n"
        + "#148=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#25,#10,#120,#8);\n"
        + "#149=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#26,#10,#121,#8);\n"
        + "#150=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#27,#10,#122,#8);\n"
        + "#151=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#34,#10,#123,#8);\n"
        + "#152=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#35,#10,#124,#8);\n"
        + "#153=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#36,#10,#125,#8);\n"
        + "#154=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#37,#10,#126,#8);\n"
        + "#155=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#38,#10,#127,#8);\n"
        + "#156=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#39,#10,#128,#8);\n"
        + "#157=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#29,#10,#129,#8);\n"
        + "#158=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#31,#10,#130,#8);\n"
        + "#159=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#28,#10,#131,#8);\n"
        + "#160=PMI_REQUIREMENT_ITEM_ASSOCIATION('A15','',#40,#10,#132,#8);\n"
        + "#161=PMI_REQUIREMENT_ITEM_ASSOCIATION('A16','',#41,#10,#133,#8);\n"
        + "#162=PMI_REQUIREMENT_ITEM_ASSOCIATION('A17','',#42,#10,#139,#8);\n"
        + "#163=PMI_REQUIREMENT_ITEM_ASSOCIATION('A18','',#43,#10,#140,#8);\n"
        + "#164=PMI_REQUIREMENT_ITEM_ASSOCIATION('A19','',#44,#10,#141,#8);\n"
        + "#165=PMI_REQUIREMENT_ITEM_ASSOCIATION('A20','',#45,#10,#142,#8);\n"
        + "#166=PMI_REQUIREMENT_ITEM_ASSOCIATION('A21','',#46,#10,#143,#8);\n"
        + "#167=PMI_REQUIREMENT_ITEM_ASSOCIATION('A22','',#47,#10,#144,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_CAL_DATE\"",
                "\"name\":\"NOTE_ZONE\"",
                "\"name\":\"NOTE_LOCAL_TIME\"",
                "\"name\":\"NOTE_DATE_TIME\"",
                "\"name\":\"NOTE_APPROVAL_STATUS\"",
                "\"name\":\"NOTE_APPROVAL\"",
                "\"name\":\"NOTE_SECURITY_LEVEL\"",
                "\"name\":\"NOTE_SECURITY\"",
                "\"name\":\"NOTE_CONTRACT_TYPE\"",
                "\"name\":\"NOTE_CONTRACT\"",
                "\"name\":\"NOTE_CERT_TYPE\"",
                "\"name\":\"NOTE_CERT\"",
                "\"name\":\"NOTE_PERSON\"",
                "\"name\":\"NOTE_PERSON_ORG\"",
                "\"name\":\"NOTE_APPROVAL_ROLE\"",
                "\"name\":\"NOTE_PERSON_ORG_ROLE\"",
                "\"name\":\"NOTE_ORG_ROLE\"",
                "\"name\":\"NOTE_CLASS_ROLE\"",
                "\"name\":\"NOTE_DATE_ROLE\"",
                "\"name\":\"NOTE_DATE_TIME_ROLE\"",
                "\"name\":\"NOTE_ID_ROLE\"",
                "\"name\":\"NOTE_DOC_TYPE\"",
                "\"name\":\"NOTE_LANGUAGE\"",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"COORDINATED_UNIVERSAL_TIME_OFFSET\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"LOCAL_TIME\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"DATE_AND_TIME\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"APPROVAL_STATUS\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"APPROVAL\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION_LEVEL\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"CONTRACT_TYPE\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"CONTRACT\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"CERTIFICATION_TYPE\"",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"CERTIFICATION\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionType\":\"PERSON\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"APPROVAL_ROLE\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION_ROLE\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"ORGANIZATION_ROLE\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionType\":\"CLASSIFICATION_ROLE\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"DATE_ROLE\"",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionType\":\"DATE_TIME_ROLE\"",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionType\":\"IDENTIFICATION_ROLE\"",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionType\":\"DOCUMENT_TYPE\"",
                "\"viaDefinitionId\":46",
                "\"viaDefinitionType\":\"LANGUAGE\"",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionType\":\"APPROVAL_PERSON_ORGANIZATION\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"APPROVAL_DATE_TIME\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":48",
                "\"viaDefinitionId\":50",
                "\"viaDefinitionId\":52",
                "\"viaDefinitionId\":54",
                "\"viaDefinitionId\":56",
                "\"viaDefinitionId\":58",
                "\"viaDefinitionId\":60",
                "\"viaDefinitionId\":62",
                "\"viaDefinitionId\":64",
                "\"viaDefinitionId\":66",
                "\"viaDefinitionId\":68",
                "\"viaDefinitionId\":70",
                "\"viaDefinitionId\":72",
                "\"viaDefinitionId\":74",
                "\"viaDefinitionId\":76",
                "\"viaDefinitionId\":78",
                "\"viaDefinitionId\":80",
                "\"viaDefinitionId\":82",
                "\"viaDefinitionId\":84",
                "\"viaDefinitionId\":86",
                "\"viaDefinitionId\":88",
                "\"viaDefinitionId\":90",
                "\"viaDefinitionId\":92",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":49",
                "\"viaDefinitionId\":51",
                "\"viaDefinitionId\":53",
                "\"viaDefinitionId\":55",
                "\"viaDefinitionId\":57",
                "\"viaDefinitionId\":59",
                "\"viaDefinitionId\":61",
                "\"viaDefinitionId\":63",
                "\"viaDefinitionId\":65",
                "\"viaDefinitionId\":67",
                "\"viaDefinitionId\":69",
                "\"viaDefinitionId\":71",
                "\"viaDefinitionId\":73",
                "\"viaDefinitionId\":75",
                "\"viaDefinitionId\":77",
                "\"viaDefinitionId\":79",
                "\"viaDefinitionId\":81",
                "\"viaDefinitionId\":83",
                "\"viaDefinitionId\":85",
                "\"viaDefinitionId\":87",
                "\"viaDefinitionId\":89",
                "\"viaDefinitionId\":91",
                "\"viaDefinitionId\":93",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":21");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_CAL_DATE\"",
                "\"name\":\"NOTE_ZONE\"",
                "\"name\":\"NOTE_LOCAL_TIME\"",
                "\"name\":\"NOTE_DATE_TIME\"",
                "\"name\":\"NOTE_APPROVAL_STATUS\"",
                "\"name\":\"NOTE_APPROVAL\"",
                "\"name\":\"NOTE_SECURITY_LEVEL\"",
                "\"name\":\"NOTE_SECURITY\"",
                "\"name\":\"NOTE_CONTRACT_TYPE\"",
                "\"name\":\"NOTE_CONTRACT\"",
                "\"name\":\"NOTE_CERT_TYPE\"",
                "\"name\":\"NOTE_CERT\"",
                "\"name\":\"NOTE_PERSON\"",
                "\"name\":\"NOTE_PERSON_ORG\"",
                "\"name\":\"NOTE_APPROVAL_ROLE\"",
                "\"name\":\"NOTE_PERSON_ORG_ROLE\"",
                "\"name\":\"NOTE_ORG_ROLE\"",
                "\"name\":\"NOTE_CLASS_ROLE\"",
                "\"name\":\"NOTE_DATE_ROLE\"",
                "\"name\":\"NOTE_DATE_TIME_ROLE\"",
                "\"name\":\"NOTE_ID_ROLE\"",
                "\"name\":\"NOTE_DOC_TYPE\"",
                "\"name\":\"NOTE_LANGUAGE\"",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"COORDINATED_UNIVERSAL_TIME_OFFSET\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"LOCAL_TIME\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"DATE_AND_TIME\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"APPROVAL_STATUS\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"APPROVAL\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION_LEVEL\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"CONTRACT_TYPE\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"CONTRACT\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"CERTIFICATION_TYPE\"",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"CERTIFICATION\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionType\":\"PERSON\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"APPROVAL_ROLE\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION_ROLE\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"ORGANIZATION_ROLE\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionType\":\"CLASSIFICATION_ROLE\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"DATE_ROLE\"",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionType\":\"DATE_TIME_ROLE\"",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionType\":\"IDENTIFICATION_ROLE\"",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionType\":\"DOCUMENT_TYPE\"",
                "\"viaDefinitionId\":46",
                "\"viaDefinitionType\":\"LANGUAGE\"",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionType\":\"APPROVAL_PERSON_ORGANIZATION\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"APPROVAL_DATE_TIME\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":48",
                "\"viaDefinitionId\":50",
                "\"viaDefinitionId\":52",
                "\"viaDefinitionId\":54",
                "\"viaDefinitionId\":56",
                "\"viaDefinitionId\":58",
                "\"viaDefinitionId\":60",
                "\"viaDefinitionId\":62",
                "\"viaDefinitionId\":64",
                "\"viaDefinitionId\":66",
                "\"viaDefinitionId\":68",
                "\"viaDefinitionId\":70",
                "\"viaDefinitionId\":72",
                "\"viaDefinitionId\":74",
                "\"viaDefinitionId\":76",
                "\"viaDefinitionId\":78",
                "\"viaDefinitionId\":80",
                "\"viaDefinitionId\":82",
                "\"viaDefinitionId\":84",
                "\"viaDefinitionId\":86",
                "\"viaDefinitionId\":88",
                "\"viaDefinitionId\":90",
                "\"viaDefinitionId\":92",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":49",
                "\"viaDefinitionId\":51",
                "\"viaDefinitionId\":53",
                "\"viaDefinitionId\":55",
                "\"viaDefinitionId\":57",
                "\"viaDefinitionId\":59",
                "\"viaDefinitionId\":61",
                "\"viaDefinitionId\":63",
                "\"viaDefinitionId\":65",
                "\"viaDefinitionId\":67",
                "\"viaDefinitionId\":69",
                "\"viaDefinitionId\":71",
                "\"viaDefinitionId\":73",
                "\"viaDefinitionId\":75",
                "\"viaDefinitionId\":77",
                "\"viaDefinitionId\":79",
                "\"viaDefinitionId\":81",
                "\"viaDefinitionId\":83",
                "\"viaDefinitionId\":85",
                "\"viaDefinitionId\":87",
                "\"viaDefinitionId\":89",
                "\"viaDefinitionId\":91",
                "\"viaDefinitionId\":93",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":21");
    }

    @Test
    void shouldEmbedDirectMetadataWrapperLinkDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_META_WRAP',(),#9);\n"
        + "#11=REPRESENTATION('REP_AUX',(),#9);\n"
        + "#12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);\n"
        + "#13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#15=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#16=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);\n"
        + "#18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);\n"
        + "#19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);\n"
        + "#20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));\n"
        + "#21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);\n"
        + "#22=NAME_ATTRIBUTE('N0',#8);\n"
        + "#23=DESCRIPTION_ATTRIBUTE('D0',#8);\n"
        + "#24=ID_ATTRIBUTE('I0',#8);\n"
        + "#25=NAME_ASSIGNMENT('NAME_META');\n"
        + "#26=IDENTIFICATION_ROLE('role');\n"
        + "#27=IDENTIFICATION_ASSIGNMENT('ID_META',#26);\n"
        + "#28=EXTERNAL_SOURCE('SRC');\n"
        + "#29=EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT_META',#26,#28);\n"
        + "#30=APPLIED_NAME_ASSIGNMENT('APPLIED_NAME',(#8));\n"
        + "#31=APPLIED_IDENTIFICATION_ASSIGNMENT('APPLIED_ID',#26,(#8));\n"
        + "#32=APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT('APPLIED_EXT',#26,#28,(#8));\n"
        + "#33=EXTERNAL_SOURCE('SRC_LINK');\n"
        + "#34=EXTERNAL_SOURCE_RELATIONSHIP('SR','',#28,#33);\n"
        + "#35=EXTERNALLY_DEFINED_ITEM('EXT-LINK',#33);\n"
        + "#36=PROPERTY_DEFINITION('PD_NAME_ATTR','',#22);\n"
        + "#37=PROPERTY_DEFINITION_REPRESENTATION(#36,#10);\n"
        + "#38=PROPERTY_DEFINITION('PD_DESC_ATTR','',#23);\n"
        + "#39=PROPERTY_DEFINITION_REPRESENTATION(#38,#10);\n"
        + "#40=PROPERTY_DEFINITION('PD_ID_ATTR','',#24);\n"
        + "#41=PROPERTY_DEFINITION_REPRESENTATION(#40,#10);\n"
        + "#42=PROPERTY_DEFINITION('PD_NAME_ASSIGN','',#25);\n"
        + "#43=PROPERTY_DEFINITION_REPRESENTATION(#42,#10);\n"
        + "#44=PROPERTY_DEFINITION('PD_ID_ASSIGN','',#27);\n"
        + "#45=PROPERTY_DEFINITION_REPRESENTATION(#44,#10);\n"
        + "#46=PROPERTY_DEFINITION('PD_EXT_ASSIGN','',#29);\n"
        + "#47=PROPERTY_DEFINITION_REPRESENTATION(#46,#10);\n"
        + "#48=PROPERTY_DEFINITION('PD_APPLIED_NAME','',#30);\n"
        + "#49=PROPERTY_DEFINITION_REPRESENTATION(#48,#10);\n"
        + "#50=PROPERTY_DEFINITION('PD_APPLIED_ID','',#31);\n"
        + "#51=PROPERTY_DEFINITION_REPRESENTATION(#50,#10);\n"
        + "#52=PROPERTY_DEFINITION('PD_APPLIED_EXT','',#32);\n"
        + "#53=PROPERTY_DEFINITION_REPRESENTATION(#52,#10);\n"
        + "#60=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#61=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#62=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#63=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#64=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#65=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#66=CARTESIAN_POINT('P6',(6.0,0.0,0.0));\n"
        + "#67=CARTESIAN_POINT('P7',(7.0,0.0,0.0));\n"
        + "#68=CARTESIAN_POINT('P8',(8.0,0.0,0.0));\n"
        + "#69=ANNOTATION_TEXT_OCCURRENCE('NOTE_NAME_ATTR','',#60);\n"
        + "#70=ANNOTATION_TEXT_OCCURRENCE('NOTE_DESC_ATTR','',#61);\n"
        + "#71=ANNOTATION_TEXT_OCCURRENCE('NOTE_ID_ATTR','',#62);\n"
        + "#72=ANNOTATION_TEXT_OCCURRENCE('NOTE_NAME_ASSIGN','',#63);\n"
        + "#73=ANNOTATION_TEXT_OCCURRENCE('NOTE_ID_ASSIGN','',#64);\n"
        + "#74=ANNOTATION_TEXT_OCCURRENCE('NOTE_EXT_ASSIGN','',#65);\n"
        + "#75=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPLIED_NAME','',#66);\n"
        + "#76=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPLIED_ID','',#67);\n"
        + "#77=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPLIED_EXT','',#68);\n"
        + "#78=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#22,#10,#69,#8);\n"
        + "#79=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#23,#10,#70,#8);\n"
        + "#80=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#24,#10,#71,#8);\n"
        + "#81=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#25,#10,#72,#8);\n"
        + "#82=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#27,#10,#73,#8);\n"
        + "#83=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#29,#10,#74,#8);\n"
        + "#84=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#30,#10,#75,#8);\n"
        + "#85=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#31,#10,#76,#8);\n"
        + "#86=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#32,#10,#77,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_NAME_ATTR\"",
                "\"name\":\"NOTE_DESC_ATTR\"",
                "\"name\":\"NOTE_ID_ATTR\"",
                "\"name\":\"NOTE_NAME_ASSIGN\"",
                "\"name\":\"NOTE_ID_ASSIGN\"",
                "\"name\":\"NOTE_EXT_ASSIGN\"",
                "\"name\":\"NOTE_APPLIED_NAME\"",
                "\"name\":\"NOTE_APPLIED_ID\"",
                "\"name\":\"NOTE_APPLIED_EXT\"",
                "\"viaDefinitionType\":\"NAME_ATTRIBUTE\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"DESCRIPTION_ATTRIBUTE\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"ID_ATTRIBUTE\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"NAME_ASSIGNMENT\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"EXTERNAL_IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"APPLIED_NAME_ASSIGNMENT\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"APPLIED_IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"IDENTIFICATION_ROLE\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE_RELATIONSHIP\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"EXTERNALLY_DEFINED_ITEM\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionId\":46",
                "\"viaDefinitionId\":48",
                "\"viaDefinitionId\":50",
                "\"viaDefinitionId\":52",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionId\":49",
                "\"viaDefinitionId\":51",
                "\"viaDefinitionId\":53",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":21");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_NAME_ATTR\"",
                "\"name\":\"NOTE_DESC_ATTR\"",
                "\"name\":\"NOTE_ID_ATTR\"",
                "\"name\":\"NOTE_NAME_ASSIGN\"",
                "\"name\":\"NOTE_ID_ASSIGN\"",
                "\"name\":\"NOTE_EXT_ASSIGN\"",
                "\"name\":\"NOTE_APPLIED_NAME\"",
                "\"name\":\"NOTE_APPLIED_ID\"",
                "\"name\":\"NOTE_APPLIED_EXT\"",
                "\"viaDefinitionType\":\"NAME_ATTRIBUTE\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"DESCRIPTION_ATTRIBUTE\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"ID_ATTRIBUTE\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"NAME_ASSIGNMENT\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"EXTERNAL_IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"APPLIED_NAME_ASSIGNMENT\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"APPLIED_IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"IDENTIFICATION_ROLE\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE_RELATIONSHIP\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"EXTERNALLY_DEFINED_ITEM\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionId\":46",
                "\"viaDefinitionId\":48",
                "\"viaDefinitionId\":50",
                "\"viaDefinitionId\":52",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionId\":49",
                "\"viaDefinitionId\":51",
                "\"viaDefinitionId\":53",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":21");
    }

    @Test
    void shouldEmbedDirectDocumentGroupAndExternalSourceLinkDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_DOC_META',(),#9);\n"
        + "#11=REPRESENTATION('REP_AUX',(),#9);\n"
        + "#12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);\n"
        + "#13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#15=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#16=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);\n"
        + "#18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);\n"
        + "#19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);\n"
        + "#20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));\n"
        + "#21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);\n"
        + "#22=GROUP('G1','group');\n"
        + "#23=DOCUMENT_TYPE('spec');\n"
        + "#24=DOCUMENT('DOC-1','Spec A','',#23);\n"
        + "#25=DOCUMENT_REFERENCE(#24,'internal');\n"
        + "#26=APPLIED_DOCUMENT_REFERENCE(#24,'applied',(#8));\n"
        + "#27=EXTERNAL_SOURCE('SRC');\n"
        + "#28=EXTERNALLY_DEFINED_ITEM('EXT-1',#27);\n"
        + "#29=EXTERNAL_SOURCE('SRC_LINK');\n"
        + "#30=EXTERNAL_SOURCE_RELATIONSHIP('SR','',#27,#29);\n"
        + "#31=DOCUMENT_USAGE_CONSTRAINT(#24,'SECTION','7.1');\n"
        + "#32=PROPERTY_DEFINITION('PD_GROUP','',#22);\n"
        + "#33=PROPERTY_DEFINITION_REPRESENTATION(#32,#10);\n"
        + "#34=PROPERTY_DEFINITION('PD_DOCUMENT','',#24);\n"
        + "#35=PROPERTY_DEFINITION_REPRESENTATION(#34,#10);\n"
        + "#36=PROPERTY_DEFINITION('PD_DOC_REF','',#25);\n"
        + "#37=PROPERTY_DEFINITION_REPRESENTATION(#36,#10);\n"
        + "#38=PROPERTY_DEFINITION('PD_APPLIED_DOC_REF','',#26);\n"
        + "#39=PROPERTY_DEFINITION_REPRESENTATION(#38,#10);\n"
        + "#40=PROPERTY_DEFINITION('PD_EXT_SOURCE','',#27);\n"
        + "#41=PROPERTY_DEFINITION_REPRESENTATION(#40,#10);\n"
        + "#42=PROPERTY_DEFINITION('PD_EXT_ITEM','',#28);\n"
        + "#43=PROPERTY_DEFINITION_REPRESENTATION(#42,#10);\n"
        + "#44=PROPERTY_DEFINITION('PD_DOC_USAGE','',#31);\n"
        + "#45=PROPERTY_DEFINITION_REPRESENTATION(#44,#10);\n"
        + "#50=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#51=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#52=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#53=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#54=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#55=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#56=CARTESIAN_POINT('P6',(6.0,0.0,0.0));\n"
        + "#57=ANNOTATION_TEXT_OCCURRENCE('NOTE_GROUP','',#50);\n"
        + "#58=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOCUMENT','',#51);\n"
        + "#59=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOC_REF','',#52);\n"
        + "#60=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPLIED_DOC_REF','',#53);\n"
        + "#61=ANNOTATION_TEXT_OCCURRENCE('NOTE_EXT_SOURCE','',#54);\n"
        + "#62=ANNOTATION_TEXT_OCCURRENCE('NOTE_EXT_ITEM','',#55);\n"
        + "#63=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOC_USAGE','',#56);\n"
        + "#64=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#22,#10,#57,#8);\n"
        + "#65=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#24,#10,#58,#8);\n"
        + "#66=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#25,#10,#59,#8);\n"
        + "#67=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#26,#10,#60,#8);\n"
        + "#68=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#27,#10,#61,#8);\n"
        + "#69=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#28,#10,#62,#8);\n"
        + "#70=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#31,#10,#63,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_GROUP\"",
                "\"name\":\"NOTE_DOCUMENT\"",
                "\"name\":\"NOTE_DOC_REF\"",
                "\"name\":\"NOTE_APPLIED_DOC_REF\"",
                "\"name\":\"NOTE_EXT_SOURCE\"",
                "\"name\":\"NOTE_EXT_ITEM\"",
                "\"name\":\"NOTE_DOC_USAGE\"",
                "\"viaDefinitionType\":\"GROUP\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"DOCUMENT\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"DOCUMENT_REFERENCE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"APPLIED_DOCUMENT_REFERENCE\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"EXTERNALLY_DEFINED_ITEM\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"DOCUMENT_USAGE_CONSTRAINT\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"DOCUMENT_TYPE\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE_RELATIONSHIP\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":21");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_GROUP\"",
                "\"name\":\"NOTE_DOCUMENT\"",
                "\"name\":\"NOTE_DOC_REF\"",
                "\"name\":\"NOTE_APPLIED_DOC_REF\"",
                "\"name\":\"NOTE_EXT_SOURCE\"",
                "\"name\":\"NOTE_EXT_ITEM\"",
                "\"name\":\"NOTE_DOC_USAGE\"",
                "\"viaDefinitionType\":\"GROUP\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"DOCUMENT\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"DOCUMENT_REFERENCE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"APPLIED_DOCUMENT_REFERENCE\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"EXTERNALLY_DEFINED_ITEM\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"DOCUMENT_USAGE_CONSTRAINT\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"DOCUMENT_TYPE\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE_RELATIONSHIP\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":21");
    }

    @Test
    void shouldEmbedDirectAssignmentCarrierLinkDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_ASSIGN',(),#9);\n"
        + "#11=REPRESENTATION('REP_AUX',(),#9);\n"
        + "#12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);\n"
        + "#13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#15=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#16=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);\n"
        + "#18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);\n"
        + "#19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);\n"
        + "#20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));\n"
        + "#21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);\n"
        + "#22=APPROVAL_STATUS('approved');\n"
        + "#23=APPROVAL(#22,'design');\n"
        + "#24=APPROVAL_ASSIGNMENT(#23);\n"
        + "#25=SECURITY_CLASSIFICATION_LEVEL('controlled');\n"
        + "#26=SECURITY_CLASSIFICATION('sec','purpose',#25);\n"
        + "#27=SECURITY_CLASSIFICATION_ASSIGNMENT(#26);\n"
        + "#28=DATE_ROLE('release');\n"
        + "#29=CALENDAR_DATE(2026,11,4);\n"
        + "#30=DATE_ASSIGNMENT(#29,#28);\n"
        + "#31=LANGUAGE('en-US');\n"
        + "#32=LANGUAGE_ASSIGNMENT(#31);\n"
        + "#33=GROUP('G1','group');\n"
        + "#34=GROUP_ASSIGNMENT(#33);\n"
        + "#35=PERSON('p-1','Doe','Jane',$,$,$);\n"
        + "#36=ORGANIZATION('org-1','Acme','engineering');\n"
        + "#37=PERSON_AND_ORGANIZATION(#35,#36);\n"
        + "#38=PERSON_AND_ORGANIZATION_ROLE('creator');\n"
        + "#39=APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT(#37,#38,(#8));\n"
        + "#40=PROPERTY_DEFINITION('PD_APPROVAL_ASSIGN','',#24);\n"
        + "#41=PROPERTY_DEFINITION_REPRESENTATION(#40,#10);\n"
        + "#42=PROPERTY_DEFINITION('PD_SECURITY_ASSIGN','',#27);\n"
        + "#43=PROPERTY_DEFINITION_REPRESENTATION(#42,#10);\n"
        + "#44=PROPERTY_DEFINITION('PD_DATE_ASSIGN','',#30);\n"
        + "#45=PROPERTY_DEFINITION_REPRESENTATION(#44,#10);\n"
        + "#46=PROPERTY_DEFINITION('PD_LANG_ASSIGN','',#32);\n"
        + "#47=PROPERTY_DEFINITION_REPRESENTATION(#46,#10);\n"
        + "#48=PROPERTY_DEFINITION('PD_GROUP_ASSIGN','',#34);\n"
        + "#49=PROPERTY_DEFINITION_REPRESENTATION(#48,#10);\n"
        + "#50=PROPERTY_DEFINITION('PD_APPLIED_PERSON_ORG_ASSIGN','',#39);\n"
        + "#51=PROPERTY_DEFINITION_REPRESENTATION(#50,#10);\n"
        + "#60=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#61=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#62=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#63=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#64=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#65=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#66=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL_ASSIGN','',#60);\n"
        + "#67=ANNOTATION_TEXT_OCCURRENCE('NOTE_SECURITY_ASSIGN','',#61);\n"
        + "#68=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_ASSIGN','',#62);\n"
        + "#69=ANNOTATION_TEXT_OCCURRENCE('NOTE_LANG_ASSIGN','',#63);\n"
        + "#70=ANNOTATION_TEXT_OCCURRENCE('NOTE_GROUP_ASSIGN','',#64);\n"
        + "#71=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPLIED_PERSON_ORG_ASSIGN','',#65);\n"
        + "#72=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#24,#10,#66,#8);\n"
        + "#73=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#27,#10,#67,#8);\n"
        + "#74=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#30,#10,#68,#8);\n"
        + "#75=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#32,#10,#69,#8);\n"
        + "#76=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#34,#10,#70,#8);\n"
        + "#77=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#39,#10,#71,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_APPROVAL_ASSIGN\"",
                "\"name\":\"NOTE_SECURITY_ASSIGN\"",
                "\"name\":\"NOTE_DATE_ASSIGN\"",
                "\"name\":\"NOTE_LANG_ASSIGN\"",
                "\"name\":\"NOTE_GROUP_ASSIGN\"",
                "\"name\":\"NOTE_APPLIED_PERSON_ORG_ASSIGN\"",
                "\"viaDefinitionType\":\"APPROVAL_ASSIGNMENT\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"DATE_ASSIGNMENT\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"LANGUAGE_ASSIGNMENT\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"GROUP_ASSIGNMENT\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionId\":46",
                "\"viaDefinitionId\":48",
                "\"viaDefinitionId\":50",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionId\":49",
                "\"viaDefinitionId\":51",
                "\"viaDefinitionType\":\"APPROVAL\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"LANGUAGE\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"GROUP\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_APPROVAL_ASSIGN\"",
                "\"name\":\"NOTE_SECURITY_ASSIGN\"",
                "\"name\":\"NOTE_DATE_ASSIGN\"",
                "\"name\":\"NOTE_LANG_ASSIGN\"",
                "\"name\":\"NOTE_GROUP_ASSIGN\"",
                "\"name\":\"NOTE_APPLIED_PERSON_ORG_ASSIGN\"",
                "\"viaDefinitionType\":\"APPROVAL_ASSIGNMENT\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"DATE_ASSIGNMENT\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"LANGUAGE_ASSIGNMENT\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"GROUP_ASSIGNMENT\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionId\":46",
                "\"viaDefinitionId\":48",
                "\"viaDefinitionId\":50",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionId\":49",
                "\"viaDefinitionId\":51",
                "\"viaDefinitionType\":\"APPROVAL\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"LANGUAGE\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"GROUP\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19");
    }

    @Test
    void shouldEmbedApprovalPersonAndDateLeafMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_APPROVAL_CHAIN',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=APPROVAL_STATUS('released');\n"
        + "#13=APPROVAL(#12,'design');\n"
        + "#14=PERSON('p-1','Doe','Jane',$,$,$);\n"
        + "#15=ORGANIZATION('org-1','Acme','engineering');\n"
        + "#16=PERSON_AND_ORGANIZATION(#14,#15);\n"
        + "#17=APPROVAL_ROLE('authorizer');\n"
        + "#18=APPROVAL_PERSON_ORGANIZATION(#16,#13,#17);\n"
        + "#19=CALENDAR_DATE(2026,11,4);\n"
        + "#20=COORDINATED_UNIVERSAL_TIME_OFFSET(8,0,.AHEAD.);\n"
        + "#21=LOCAL_TIME(9,15,$,#20);\n"
        + "#22=DATE_AND_TIME(#19,#21);\n"
        + "#23=APPROVAL_DATE_TIME(#22,#13);\n"
        + "#24=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#25=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#26=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL_PERSON','',#24);\n"
        + "#27=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL_DATE','',#25);\n"
        + "#28=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#18,#10,#26,#8);\n"
        + "#29=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#23,#10,#27,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"APPROVAL_PERSON_ORGANIZATION\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"PERSON\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"ORGANIZATION\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"APPROVAL_ROLE\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"APPROVAL_STATUS\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"APPROVAL_DATE_TIME\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"DATE_AND_TIME\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"LOCAL_TIME\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"COORDINATED_UNIVERSAL_TIME_OFFSET\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"APPROVAL\"",
                "\"viaDefinitionId\":13");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"APPROVAL_PERSON_ORGANIZATION\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"PERSON\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"ORGANIZATION\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"APPROVAL_ROLE\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"APPROVAL_STATUS\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"APPROVAL_DATE_TIME\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"DATE_AND_TIME\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"LOCAL_TIME\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"COORDINATED_UNIVERSAL_TIME_OFFSET\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"APPROVAL\"",
                "\"viaDefinitionId\":13");
    }

    @Test
    void shouldEmbedRepresentationContextAndLeafDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=REPRESENTATION_CONTEXT('GEN','GENERAL');\n"
        + "#10=REPRESENTATION('REP_CONTEXT_ONLY',(),#9);\n"
        + "#11=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));\n"
        + "#12=UNCERTAINTY_MEASURE_WITH_UNIT(LENGTH_MEASURE(0.01),#11,'distance_accuracy_value','confusion');\n"
        + "#13=(GEOMETRIC_REPRESENTATION_CONTEXT(3)\n"
        + "    GLOBAL_UNIT_ASSIGNED_CONTEXT((#11))\n"
        + "    GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT((#12))\n"
        + "    REPRESENTATION_CONTEXT('GEO','MODEL'));\n"
        + "#14=REPRESENTATION('REP_GEOM_CONTEXT',(),#13);\n"
        + "#15=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#16=SHAPE_DEFINITION_REPRESENTATION(#7,#14);\n"
        + "#17=CARTESIAN_POINT('O0',(0.0,0.0,0.0));\n"
        + "#18=DIRECTION('X0',(1.0,0.0,0.0));\n"
        + "#19=DIRECTION('Y0',(0.0,1.0,0.0));\n"
        + "#20=AXIS2_PLACEMENT_3D('A0',#17,#18,#19);\n"
        + "#21=CARTESIAN_POINT('O1',(5.0,0.0,0.0));\n"
        + "#22=AXIS2_PLACEMENT_3D('A1',#21,#18,#19);\n"
        + "#23=ITEM_DEFINED_TRANSFORMATION('MOVE','translate x',#20,#22);\n"
        + "#24=(REPRESENTATION_RELATIONSHIP('RWT','',#10,#14)\n"
        + "    REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#23));\n"
        + "#25=REPRESENTATION_ITEM('REP_ITEM_ONLY');\n"
        + "#26=PROPERTY_DEFINITION('PD_REP_ITEM','',#25);\n"
        + "#27=PROPERTY_DEFINITION_REPRESENTATION(#26,#10);\n"
        + "#28=(GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('GEOM_ITEM_ONLY'));\n"
        + "#29=PROPERTY_DEFINITION('PD_GEOM_ITEM','',#28);\n"
        + "#30=PROPERTY_DEFINITION_REPRESENTATION(#29,#14);\n"
        + "#31=(TOPOLOGICAL_REPRESENTATION_ITEM('TOPO_ITEM_ONLY'));\n"
        + "#32=PROPERTY_DEFINITION('PD_TOPO_ITEM','',#31);\n"
        + "#33=PROPERTY_DEFINITION_REPRESENTATION(#32,#10);\n"
        + "#40=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#41=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#42=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#43=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#44=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#45=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#46=CARTESIAN_POINT('P6',(6.0,0.0,0.0));\n"
        + "#47=CARTESIAN_POINT('P7',(7.0,0.0,0.0));\n"
        + "#48=ANNOTATION_TEXT_OCCURRENCE('NOTE_REP_CONTEXT','',#40);\n"
        + "#49=ANNOTATION_TEXT_OCCURRENCE('NOTE_GEOM_CONTEXT','',#41);\n"
        + "#50=ANNOTATION_TEXT_OCCURRENCE('NOTE_SI_UNIT','',#42);\n"
        + "#51=ANNOTATION_TEXT_OCCURRENCE('NOTE_UNCERTAINTY_CTX','',#43);\n"
        + "#52=ANNOTATION_TEXT_OCCURRENCE('NOTE_TRANSFORM','',#44);\n"
        + "#53=ANNOTATION_TEXT_OCCURRENCE('NOTE_REP_ITEM','',#45);\n"
        + "#54=ANNOTATION_TEXT_OCCURRENCE('NOTE_GEOM_ITEM','',#46);\n"
        + "#55=ANNOTATION_TEXT_OCCURRENCE('NOTE_TOPO_ITEM','',#47);\n"
        + "#56=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#9,#10,#48,#8);\n"
        + "#57=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#13,#14,#49,#8);\n"
        + "#58=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#11,#14,#50,#8);\n"
        + "#59=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#12,#14,#51,#8);\n"
        + "#60=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#23,#10,#52,#8);\n"
        + "#61=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#25,#10,#53,#8);\n"
        + "#62=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#28,#14,#54,#8);\n"
        + "#63=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#31,#10,#55,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"REPRESENTATION_CONTEXT\"",
                "\"viaDefinitionId\":9",
                "\"viaDefinitionType\":\"GEOMETRIC_REPRESENTATION_CONTEXT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"GLOBAL_UNIT_ASSIGNED_CONTEXT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"SI_UNIT\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"UNCERTAINTY_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"GEOMETRIC_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"TOPOLOGICAL_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":31");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"REPRESENTATION_CONTEXT\"",
                "\"viaDefinitionId\":9",
                "\"viaDefinitionType\":\"GEOMETRIC_REPRESENTATION_CONTEXT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"GLOBAL_UNIT_ASSIGNED_CONTEXT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"SI_UNIT\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"UNCERTAINTY_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"GEOMETRIC_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"TOPOLOGICAL_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":31");
    }

    @Test
    void shouldEmbedMeasureAndUnitDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_UNIT_CHAIN',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));\n"
        + "#13=MEASURE_WITH_UNIT(LENGTH_MEASURE(12.5),#12);\n"
        + "#14=(CONVERSION_BASED_UNIT('DEGREE',#13) NAMED_UNIT(*) PLANE_ANGLE_UNIT());\n"
        + "#15=(THERMODYNAMIC_TEMPERATURE_UNIT() NAMED_UNIT(*) SI_UNIT($,.KELVIN.));\n"
        + "#16=(CONVERSION_BASED_UNIT_WITH_OFFSET(THERMODYNAMIC_TEMPERATURE_MEASURE(273.15))\n"
        + "    CONVERSION_BASED_UNIT('DEG_C',#13)\n"
        + "    NAMED_UNIT(*)\n"
        + "    THERMODYNAMIC_TEMPERATURE_UNIT());\n"
        + "#17=(CONTEXT_DEPENDENT_UNIT('BOX') NAMED_UNIT(*));\n"
        + "#18=(FORCE_UNIT() NAMED_UNIT(*) SI_UNIT($,.NEWTON.));\n"
        + "#19=DERIVED_UNIT_ELEMENT(#18,1.0);\n"
        + "#20=DERIVED_UNIT((#19));\n"
        + "#21=UNCERTAINTY_MEASURE_WITH_UNIT(LENGTH_MEASURE(0.01),#12,'distance_accuracy_value','confusion');\n"
        + "#22=LENGTH_MEASURE_WITH_UNIT(LENGTH_MEASURE(3.5),#12);\n"
        + "#23=PLANE_ANGLE_MEASURE_WITH_UNIT(PLANE_ANGLE_MEASURE(0.25),#14);\n"
        + "#24=PROPERTY_DEFINITION('PD_MWU','',#13);\n"
        + "#25=PROPERTY_DEFINITION('PD_TYPED','',#14);\n"
        + "#26=PROPERTY_DEFINITION('PD_OFFSET','',#16);\n"
        + "#27=PROPERTY_DEFINITION('PD_CTX_UNIT','',#17);\n"
        + "#28=PROPERTY_DEFINITION('PD_DERIVED','',#20);\n"
        + "#29=PROPERTY_DEFINITION('PD_UNCERTAINTY','',#21);\n"
        + "#30=PROPERTY_DEFINITION('PD_LEN_TYPED','',#22);\n"
        + "#31=PROPERTY_DEFINITION('PD_ANGLE_TYPED','',#23);\n"
        + "#32=PROPERTY_DEFINITION_REPRESENTATION(#24,#10);\n"
        + "#33=PROPERTY_DEFINITION_REPRESENTATION(#25,#10);\n"
        + "#34=PROPERTY_DEFINITION_REPRESENTATION(#26,#10);\n"
        + "#35=PROPERTY_DEFINITION_REPRESENTATION(#27,#10);\n"
        + "#36=PROPERTY_DEFINITION_REPRESENTATION(#28,#10);\n"
        + "#37=PROPERTY_DEFINITION_REPRESENTATION(#29,#10);\n"
        + "#38=PROPERTY_DEFINITION_REPRESENTATION(#30,#10);\n"
        + "#39=PROPERTY_DEFINITION_REPRESENTATION(#31,#10);\n"
        + "#70=MASS_UNIT();\n"
        + "#71=TIME_UNIT();\n"
        + "#72=AREA_UNIT();\n"
        + "#73=VOLUME_UNIT();\n"
        + "#74=(SOLID_ANGLE_UNIT() NAMED_UNIT(*) SI_UNIT($,.STERADIAN.));\n"
        + "#75=RATIO_UNIT();\n"
        + "#76=MASS_MEASURE_WITH_UNIT(MASS_MEASURE(3.0),#70);\n"
        + "#77=TIME_MEASURE_WITH_UNIT(TIME_MEASURE(2.0),#71);\n"
        + "#78=AREA_MEASURE_WITH_UNIT(AREA_MEASURE(6.0),#72);\n"
        + "#79=VOLUME_MEASURE_WITH_UNIT(VOLUME_MEASURE(7.0),#73);\n"
        + "#80=SOLID_ANGLE_MEASURE_WITH_UNIT(SOLID_ANGLE_MEASURE(1.5),#74);\n"
        + "#81=RATIO_MEASURE_WITH_UNIT(RATIO_MEASURE(0.25),#75);\n"
        + "#82=PROPERTY_DEFINITION('PD_MASS_TYPED','',#76);\n"
        + "#83=PROPERTY_DEFINITION('PD_TIME_TYPED','',#77);\n"
        + "#84=PROPERTY_DEFINITION('PD_AREA_TYPED','',#78);\n"
        + "#85=PROPERTY_DEFINITION('PD_VOLUME_TYPED','',#79);\n"
        + "#86=PROPERTY_DEFINITION('PD_SOLID_ANGLE_TYPED','',#80);\n"
        + "#87=PROPERTY_DEFINITION('PD_RATIO_TYPED','',#81);\n"
        + "#88=PROPERTY_DEFINITION_REPRESENTATION(#82,#10);\n"
        + "#89=PROPERTY_DEFINITION_REPRESENTATION(#83,#10);\n"
        + "#90=PROPERTY_DEFINITION_REPRESENTATION(#84,#10);\n"
        + "#91=PROPERTY_DEFINITION_REPRESENTATION(#85,#10);\n"
        + "#92=PROPERTY_DEFINITION_REPRESENTATION(#86,#10);\n"
        + "#93=PROPERTY_DEFINITION_REPRESENTATION(#87,#10);\n"
        + "#112=FREQUENCY_UNIT();\n"
        + "#113=FORCE_UNIT();\n"
        + "#114=PRESSURE_UNIT();\n"
        + "#115=ENERGY_UNIT();\n"
        + "#116=POWER_UNIT();\n"
        + "#117=ELECTRIC_POTENTIAL_UNIT();\n"
        + "#118=RESISTANCE_UNIT();\n"
        + "#119=CONDUCTANCE_UNIT();\n"
        + "#120=MAGNETIC_FLUX_UNIT();\n"
        + "#121=ILLUMINANCE_UNIT();\n"
        + "#122=LUMINOUS_FLUX_UNIT();\n"
        + "#123=LUMINOUS_INTENSITY_UNIT();\n"
        + "#124=FREQUENCY_MEASURE_WITH_UNIT(FREQUENCY_MEASURE(50.0),#112);\n"
        + "#125=FORCE_MEASURE_WITH_UNIT(FORCE_MEASURE(100.0),#113);\n"
        + "#126=PRESSURE_MEASURE_WITH_UNIT(PRESSURE_MEASURE(1.5),#114);\n"
        + "#127=ENERGY_MEASURE_WITH_UNIT(ENERGY_MEASURE(42.0),#115);\n"
        + "#128=POWER_MEASURE_WITH_UNIT(POWER_MEASURE(3.5),#116);\n"
        + "#129=ELECTRIC_POTENTIAL_MEASURE_WITH_UNIT(ELECTRIC_POTENTIAL_MEASURE(220.0),#117);\n"
        + "#130=RESISTANCE_MEASURE_WITH_UNIT(RESISTANCE_MEASURE(10.0),#118);\n"
        + "#131=CONDUCTANCE_MEASURE_WITH_UNIT(CONDUCTANCE_MEASURE(0.1),#119);\n"
        + "#132=MAGNETIC_FLUX_MEASURE_WITH_UNIT(MAGNETIC_FLUX_MEASURE(0.02),#120);\n"
        + "#133=ILLUMINANCE_MEASURE_WITH_UNIT(ILLUMINANCE_MEASURE(500.0),#121);\n"
        + "#134=LUMINOUS_FLUX_MEASURE_WITH_UNIT(LUMINOUS_FLUX_MEASURE(800.0),#122);\n"
        + "#135=LUMINOUS_INTENSITY_MEASURE_WITH_UNIT(LUMINOUS_INTENSITY_MEASURE(120.0),#123);\n"
        + "#136=PROPERTY_DEFINITION('PD_FREQ_TYPED','',#124);\n"
        + "#137=PROPERTY_DEFINITION('PD_FORCE_TYPED','',#125);\n"
        + "#138=PROPERTY_DEFINITION('PD_PRESSURE_TYPED','',#126);\n"
        + "#139=PROPERTY_DEFINITION('PD_ENERGY_TYPED','',#127);\n"
        + "#140=PROPERTY_DEFINITION('PD_POWER_TYPED','',#128);\n"
        + "#141=PROPERTY_DEFINITION('PD_ELECTRIC_POTENTIAL_TYPED','',#129);\n"
        + "#142=PROPERTY_DEFINITION('PD_RESISTANCE_TYPED','',#130);\n"
        + "#143=PROPERTY_DEFINITION('PD_CONDUCTANCE_TYPED','',#131);\n"
        + "#144=PROPERTY_DEFINITION('PD_MAGNETIC_FLUX_TYPED','',#132);\n"
        + "#145=PROPERTY_DEFINITION('PD_ILLUMINANCE_TYPED','',#133);\n"
        + "#146=PROPERTY_DEFINITION('PD_LUMINOUS_FLUX_TYPED','',#134);\n"
        + "#147=PROPERTY_DEFINITION('PD_LUMINOUS_INTENSITY_TYPED','',#135);\n"
        + "#148=PROPERTY_DEFINITION_REPRESENTATION(#136,#10);\n"
        + "#149=PROPERTY_DEFINITION_REPRESENTATION(#137,#10);\n"
        + "#150=PROPERTY_DEFINITION_REPRESENTATION(#138,#10);\n"
        + "#151=PROPERTY_DEFINITION_REPRESENTATION(#139,#10);\n"
        + "#152=PROPERTY_DEFINITION_REPRESENTATION(#140,#10);\n"
        + "#153=PROPERTY_DEFINITION_REPRESENTATION(#141,#10);\n"
        + "#154=PROPERTY_DEFINITION_REPRESENTATION(#142,#10);\n"
        + "#155=PROPERTY_DEFINITION_REPRESENTATION(#143,#10);\n"
        + "#156=PROPERTY_DEFINITION_REPRESENTATION(#144,#10);\n"
        + "#157=PROPERTY_DEFINITION_REPRESENTATION(#145,#10);\n"
        + "#158=PROPERTY_DEFINITION_REPRESENTATION(#146,#10);\n"
        + "#159=PROPERTY_DEFINITION_REPRESENTATION(#147,#10);\n"
        + "#196=AMOUNT_OF_SUBSTANCE_UNIT();\n"
        + "#197=ELECTRIC_CHARGE_UNIT();\n"
        + "#198=CAPACITANCE_UNIT();\n"
        + "#199=MAGNETIC_FLUX_DENSITY_UNIT();\n"
        + "#200=INDUCTANCE_UNIT();\n"
        + "#201=RADIOACTIVITY_UNIT();\n"
        + "#202=ABSORBED_DOSE_UNIT();\n"
        + "#203=DOSE_EQUIVALENT_UNIT();\n"
        + "#204=ACCELERATION_UNIT();\n"
        + "#205=VELOCITY_UNIT();\n"
        + "#206=THERMAL_RESISTANCE_UNIT();\n"
        + "#207=AMOUNT_OF_SUBSTANCE_MEASURE_WITH_UNIT(AMOUNT_OF_SUBSTANCE_MEASURE(2.5),#196);\n"
        + "#208=ELECTRIC_CHARGE_MEASURE_WITH_UNIT(ELECTRIC_CHARGE_MEASURE(1.6),#197);\n"
        + "#209=CAPACITANCE_MEASURE_WITH_UNIT(CAPACITANCE_MEASURE(0.047),#198);\n"
        + "#210=MAGNETIC_FLUX_DENSITY_MEASURE_WITH_UNIT(MAGNETIC_FLUX_DENSITY_MEASURE(0.12),#199);\n"
        + "#211=INDUCTANCE_MEASURE_WITH_UNIT(INDUCTANCE_MEASURE(0.008),#200);\n"
        + "#212=RADIOACTIVITY_MEASURE_WITH_UNIT(RADIOACTIVITY_MEASURE(3.0),#201);\n"
        + "#213=ABSORBED_DOSE_MEASURE_WITH_UNIT(ABSORBED_DOSE_MEASURE(0.4),#202);\n"
        + "#214=DOSE_EQUIVALENT_MEASURE_WITH_UNIT(DOSE_EQUIVALENT_MEASURE(0.6),#203);\n"
        + "#215=ACCELERATION_MEASURE_WITH_UNIT(ACCELERATION_MEASURE(9.81),#204);\n"
        + "#216=VELOCITY_MEASURE_WITH_UNIT(VELOCITY_MEASURE(12.0),#205);\n"
        + "#217=THERMAL_RESISTANCE_MEASURE_WITH_UNIT(THERMAL_RESISTANCE_MEASURE(0.15),#206);\n"
        + "#218=PROPERTY_DEFINITION('PD_AMOUNT_TYPED','',#207);\n"
        + "#219=PROPERTY_DEFINITION('PD_CHARGE_TYPED','',#208);\n"
        + "#220=PROPERTY_DEFINITION('PD_CAPACITANCE_TYPED','',#209);\n"
        + "#221=PROPERTY_DEFINITION('PD_FLUX_DENSITY_TYPED','',#210);\n"
        + "#222=PROPERTY_DEFINITION('PD_INDUCTANCE_TYPED','',#211);\n"
        + "#223=PROPERTY_DEFINITION('PD_RADIOACTIVITY_TYPED','',#212);\n"
        + "#224=PROPERTY_DEFINITION('PD_ABSORBED_DOSE_TYPED','',#213);\n"
        + "#225=PROPERTY_DEFINITION('PD_DOSE_EQUIVALENT_TYPED','',#214);\n"
        + "#226=PROPERTY_DEFINITION('PD_ACCELERATION_TYPED','',#215);\n"
        + "#227=PROPERTY_DEFINITION('PD_VELOCITY_TYPED','',#216);\n"
        + "#228=PROPERTY_DEFINITION('PD_THERMAL_RESISTANCE_TYPED','',#217);\n"
        + "#229=PROPERTY_DEFINITION_REPRESENTATION(#218,#10);\n"
        + "#230=PROPERTY_DEFINITION_REPRESENTATION(#219,#10);\n"
        + "#231=PROPERTY_DEFINITION_REPRESENTATION(#220,#10);\n"
        + "#232=PROPERTY_DEFINITION_REPRESENTATION(#221,#10);\n"
        + "#233=PROPERTY_DEFINITION_REPRESENTATION(#222,#10);\n"
        + "#234=PROPERTY_DEFINITION_REPRESENTATION(#223,#10);\n"
        + "#235=PROPERTY_DEFINITION_REPRESENTATION(#224,#10);\n"
        + "#236=PROPERTY_DEFINITION_REPRESENTATION(#225,#10);\n"
        + "#237=PROPERTY_DEFINITION_REPRESENTATION(#226,#10);\n"
        + "#238=PROPERTY_DEFINITION_REPRESENTATION(#227,#10);\n"
        + "#239=PROPERTY_DEFINITION_REPRESENTATION(#228,#10);\n"
        + "#40=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#41=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#42=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#43=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#44=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#45=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#46=CARTESIAN_POINT('P6',(6.0,0.0,0.0));\n"
        + "#47=CARTESIAN_POINT('P7',(7.0,0.0,0.0));\n"
        + "#48=ANNOTATION_TEXT_OCCURRENCE('NOTE_MWU','',#40);\n"
        + "#49=ANNOTATION_TEXT_OCCURRENCE('NOTE_TYPED_UNIT','',#41);\n"
        + "#50=ANNOTATION_TEXT_OCCURRENCE('NOTE_OFFSET_UNIT','',#42);\n"
        + "#51=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONTEXT_UNIT','',#43);\n"
        + "#52=ANNOTATION_TEXT_OCCURRENCE('NOTE_DERIVED_UNIT','',#44);\n"
        + "#53=ANNOTATION_TEXT_OCCURRENCE('NOTE_UNCERTAINTY','',#45);\n"
        + "#54=ANNOTATION_TEXT_OCCURRENCE('NOTE_LENGTH_TYPED','',#46);\n"
        + "#55=ANNOTATION_TEXT_OCCURRENCE('NOTE_ANGLE_TYPED','',#47);\n"
        + "#94=CARTESIAN_POINT('P8',(8.0,0.0,0.0));\n"
        + "#95=CARTESIAN_POINT('P9',(9.0,0.0,0.0));\n"
        + "#96=CARTESIAN_POINT('P10',(10.0,0.0,0.0));\n"
        + "#97=CARTESIAN_POINT('P11',(11.0,0.0,0.0));\n"
        + "#98=CARTESIAN_POINT('P12',(12.0,0.0,0.0));\n"
        + "#99=CARTESIAN_POINT('P13',(13.0,0.0,0.0));\n"
        + "#100=ANNOTATION_TEXT_OCCURRENCE('NOTE_MASS_TYPED','',#94);\n"
        + "#101=ANNOTATION_TEXT_OCCURRENCE('NOTE_TIME_TYPED','',#95);\n"
        + "#102=ANNOTATION_TEXT_OCCURRENCE('NOTE_AREA_TYPED','',#96);\n"
        + "#103=ANNOTATION_TEXT_OCCURRENCE('NOTE_VOLUME_TYPED','',#97);\n"
        + "#104=ANNOTATION_TEXT_OCCURRENCE('NOTE_SOLID_ANGLE_TYPED','',#98);\n"
        + "#105=ANNOTATION_TEXT_OCCURRENCE('NOTE_RATIO_TYPED','',#99);\n"
        + "#160=CARTESIAN_POINT('P14',(14.0,0.0,0.0));\n"
        + "#161=CARTESIAN_POINT('P15',(15.0,0.0,0.0));\n"
        + "#162=CARTESIAN_POINT('P16',(16.0,0.0,0.0));\n"
        + "#163=CARTESIAN_POINT('P17',(17.0,0.0,0.0));\n"
        + "#164=CARTESIAN_POINT('P18',(18.0,0.0,0.0));\n"
        + "#165=CARTESIAN_POINT('P19',(19.0,0.0,0.0));\n"
        + "#166=CARTESIAN_POINT('P20',(20.0,0.0,0.0));\n"
        + "#167=CARTESIAN_POINT('P21',(21.0,0.0,0.0));\n"
        + "#168=CARTESIAN_POINT('P22',(22.0,0.0,0.0));\n"
        + "#169=CARTESIAN_POINT('P23',(23.0,0.0,0.0));\n"
        + "#170=CARTESIAN_POINT('P24',(24.0,0.0,0.0));\n"
        + "#171=CARTESIAN_POINT('P25',(25.0,0.0,0.0));\n"
        + "#172=ANNOTATION_TEXT_OCCURRENCE('NOTE_FREQUENCY_TYPED','',#160);\n"
        + "#173=ANNOTATION_TEXT_OCCURRENCE('NOTE_FORCE_TYPED','',#161);\n"
        + "#174=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRESSURE_TYPED','',#162);\n"
        + "#175=ANNOTATION_TEXT_OCCURRENCE('NOTE_ENERGY_TYPED','',#163);\n"
        + "#176=ANNOTATION_TEXT_OCCURRENCE('NOTE_POWER_TYPED','',#164);\n"
        + "#177=ANNOTATION_TEXT_OCCURRENCE('NOTE_ELECTRIC_POTENTIAL_TYPED','',#165);\n"
        + "#178=ANNOTATION_TEXT_OCCURRENCE('NOTE_RESISTANCE_TYPED','',#166);\n"
        + "#179=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONDUCTANCE_TYPED','',#167);\n"
        + "#180=ANNOTATION_TEXT_OCCURRENCE('NOTE_MAGNETIC_FLUX_TYPED','',#168);\n"
        + "#181=ANNOTATION_TEXT_OCCURRENCE('NOTE_ILLUMINANCE_TYPED','',#169);\n"
        + "#182=ANNOTATION_TEXT_OCCURRENCE('NOTE_LUMINOUS_FLUX_TYPED','',#170);\n"
        + "#183=ANNOTATION_TEXT_OCCURRENCE('NOTE_LUMINOUS_INTENSITY_TYPED','',#171);\n"
        + "#240=CARTESIAN_POINT('P26',(26.0,0.0,0.0));\n"
        + "#241=CARTESIAN_POINT('P27',(27.0,0.0,0.0));\n"
        + "#242=CARTESIAN_POINT('P28',(28.0,0.0,0.0));\n"
        + "#243=CARTESIAN_POINT('P29',(29.0,0.0,0.0));\n"
        + "#244=CARTESIAN_POINT('P30',(30.0,0.0,0.0));\n"
        + "#245=CARTESIAN_POINT('P31',(31.0,0.0,0.0));\n"
        + "#246=CARTESIAN_POINT('P32',(32.0,0.0,0.0));\n"
        + "#247=CARTESIAN_POINT('P33',(33.0,0.0,0.0));\n"
        + "#248=CARTESIAN_POINT('P34',(34.0,0.0,0.0));\n"
        + "#249=CARTESIAN_POINT('P35',(35.0,0.0,0.0));\n"
        + "#250=CARTESIAN_POINT('P36',(36.0,0.0,0.0));\n"
        + "#251=ANNOTATION_TEXT_OCCURRENCE('NOTE_AMOUNT_TYPED','',#240);\n"
        + "#252=ANNOTATION_TEXT_OCCURRENCE('NOTE_CHARGE_TYPED','',#241);\n"
        + "#253=ANNOTATION_TEXT_OCCURRENCE('NOTE_CAPACITANCE_TYPED','',#242);\n"
        + "#254=ANNOTATION_TEXT_OCCURRENCE('NOTE_FLUX_DENSITY_TYPED','',#243);\n"
        + "#255=ANNOTATION_TEXT_OCCURRENCE('NOTE_INDUCTANCE_TYPED','',#244);\n"
        + "#256=ANNOTATION_TEXT_OCCURRENCE('NOTE_RADIOACTIVITY_TYPED','',#245);\n"
        + "#257=ANNOTATION_TEXT_OCCURRENCE('NOTE_ABSORBED_DOSE_TYPED','',#246);\n"
        + "#258=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOSE_EQUIVALENT_TYPED','',#247);\n"
        + "#259=ANNOTATION_TEXT_OCCURRENCE('NOTE_ACCELERATION_TYPED','',#248);\n"
        + "#260=ANNOTATION_TEXT_OCCURRENCE('NOTE_VELOCITY_TYPED','',#249);\n"
        + "#261=ANNOTATION_TEXT_OCCURRENCE('NOTE_THERMAL_RESISTANCE_TYPED','',#250);\n"
        + "#56=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#13,#10,#48,#8);\n"
        + "#57=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#14,#10,#49,#8);\n"
        + "#58=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#16,#10,#50,#8);\n"
        + "#59=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#17,#10,#51,#8);\n"
        + "#60=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#20,#10,#52,#8);\n"
        + "#61=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#21,#10,#53,#8);\n"
        + "#62=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#22,#10,#54,#8);\n"
        + "#63=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#23,#10,#55,#8);\n"
        + "#106=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#76,#10,#100,#8);\n"
        + "#107=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#77,#10,#101,#8);\n"
        + "#108=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#78,#10,#102,#8);\n"
        + "#109=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#79,#10,#103,#8);\n"
        + "#110=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#80,#10,#104,#8);\n"
        + "#111=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#81,#10,#105,#8);\n"
        + "#184=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#124,#10,#172,#8);\n"
        + "#185=PMI_REQUIREMENT_ITEM_ASSOCIATION('A15','',#125,#10,#173,#8);\n"
        + "#186=PMI_REQUIREMENT_ITEM_ASSOCIATION('A16','',#126,#10,#174,#8);\n"
        + "#187=PMI_REQUIREMENT_ITEM_ASSOCIATION('A17','',#127,#10,#175,#8);\n"
        + "#188=PMI_REQUIREMENT_ITEM_ASSOCIATION('A18','',#128,#10,#176,#8);\n"
        + "#189=PMI_REQUIREMENT_ITEM_ASSOCIATION('A19','',#129,#10,#177,#8);\n"
        + "#190=PMI_REQUIREMENT_ITEM_ASSOCIATION('A20','',#130,#10,#178,#8);\n"
        + "#191=PMI_REQUIREMENT_ITEM_ASSOCIATION('A21','',#131,#10,#179,#8);\n"
        + "#192=PMI_REQUIREMENT_ITEM_ASSOCIATION('A22','',#132,#10,#180,#8);\n"
        + "#193=PMI_REQUIREMENT_ITEM_ASSOCIATION('A23','',#133,#10,#181,#8);\n"
        + "#194=PMI_REQUIREMENT_ITEM_ASSOCIATION('A24','',#134,#10,#182,#8);\n"
        + "#195=PMI_REQUIREMENT_ITEM_ASSOCIATION('A25','',#135,#10,#183,#8);\n"
        + "#262=PMI_REQUIREMENT_ITEM_ASSOCIATION('A26','',#207,#10,#251,#8);\n"
        + "#263=PMI_REQUIREMENT_ITEM_ASSOCIATION('A27','',#208,#10,#252,#8);\n"
        + "#264=PMI_REQUIREMENT_ITEM_ASSOCIATION('A28','',#209,#10,#253,#8);\n"
        + "#265=PMI_REQUIREMENT_ITEM_ASSOCIATION('A29','',#210,#10,#254,#8);\n"
        + "#266=PMI_REQUIREMENT_ITEM_ASSOCIATION('A30','',#211,#10,#255,#8);\n"
        + "#267=PMI_REQUIREMENT_ITEM_ASSOCIATION('A31','',#212,#10,#256,#8);\n"
        + "#268=PMI_REQUIREMENT_ITEM_ASSOCIATION('A32','',#213,#10,#257,#8);\n"
        + "#269=PMI_REQUIREMENT_ITEM_ASSOCIATION('A33','',#214,#10,#258,#8);\n"
        + "#270=PMI_REQUIREMENT_ITEM_ASSOCIATION('A34','',#215,#10,#259,#8);\n"
        + "#271=PMI_REQUIREMENT_ITEM_ASSOCIATION('A35','',#216,#10,#260,#8);\n"
        + "#272=PMI_REQUIREMENT_ITEM_ASSOCIATION('A36','',#217,#10,#261,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"SI_UNIT\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"CONVERSION_BASED_UNIT\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"CONVERSION_BASED_UNIT_WITH_OFFSET\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"CONTEXT_DEPENDENT_UNIT\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"DERIVED_UNIT\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"DERIVED_UNIT_ELEMENT\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"UNCERTAINTY_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"LENGTH_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"PLANE_ANGLE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"MASS_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":76",
                "\"viaDefinitionType\":\"TIME_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":77",
                "\"viaDefinitionType\":\"AREA_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":78",
                "\"viaDefinitionType\":\"VOLUME_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":79",
                "\"viaDefinitionType\":\"SOLID_ANGLE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":80",
                "\"viaDefinitionType\":\"RATIO_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":81",
                "\"viaDefinitionType\":\"FREQUENCY_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":124",
                "\"viaDefinitionType\":\"FORCE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":125",
                "\"viaDefinitionType\":\"PRESSURE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":126",
                "\"viaDefinitionType\":\"ENERGY_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":127",
                "\"viaDefinitionType\":\"POWER_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":128",
                "\"viaDefinitionType\":\"ELECTRIC_POTENTIAL_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":129",
                "\"viaDefinitionType\":\"RESISTANCE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":130",
                "\"viaDefinitionType\":\"CONDUCTANCE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":131",
                "\"viaDefinitionType\":\"MAGNETIC_FLUX_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":132",
                "\"viaDefinitionType\":\"ILLUMINANCE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":133",
                "\"viaDefinitionType\":\"LUMINOUS_FLUX_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":134",
                "\"viaDefinitionType\":\"LUMINOUS_INTENSITY_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":135",
                "\"viaDefinitionType\":\"AMOUNT_OF_SUBSTANCE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":207",
                "\"viaDefinitionType\":\"ELECTRIC_CHARGE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":208",
                "\"viaDefinitionType\":\"CAPACITANCE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":209",
                "\"viaDefinitionType\":\"MAGNETIC_FLUX_DENSITY_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":210",
                "\"viaDefinitionType\":\"INDUCTANCE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":211",
                "\"viaDefinitionType\":\"RADIOACTIVITY_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":212",
                "\"viaDefinitionType\":\"ABSORBED_DOSE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":213",
                "\"viaDefinitionType\":\"DOSE_EQUIVALENT_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":214",
                "\"viaDefinitionType\":\"ACCELERATION_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":215",
                "\"viaDefinitionType\":\"VELOCITY_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":216",
                "\"viaDefinitionType\":\"THERMAL_RESISTANCE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":217");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"SI_UNIT\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"CONVERSION_BASED_UNIT\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"CONVERSION_BASED_UNIT_WITH_OFFSET\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"CONTEXT_DEPENDENT_UNIT\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"DERIVED_UNIT\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"DERIVED_UNIT_ELEMENT\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"UNCERTAINTY_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"LENGTH_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"PLANE_ANGLE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"MASS_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":76",
                "\"viaDefinitionType\":\"TIME_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":77",
                "\"viaDefinitionType\":\"AREA_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":78",
                "\"viaDefinitionType\":\"VOLUME_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":79",
                "\"viaDefinitionType\":\"SOLID_ANGLE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":80",
                "\"viaDefinitionType\":\"RATIO_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":81",
                "\"viaDefinitionType\":\"FREQUENCY_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":124",
                "\"viaDefinitionType\":\"FORCE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":125",
                "\"viaDefinitionType\":\"PRESSURE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":126",
                "\"viaDefinitionType\":\"ENERGY_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":127",
                "\"viaDefinitionType\":\"POWER_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":128",
                "\"viaDefinitionType\":\"ELECTRIC_POTENTIAL_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":129",
                "\"viaDefinitionType\":\"RESISTANCE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":130",
                "\"viaDefinitionType\":\"CONDUCTANCE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":131",
                "\"viaDefinitionType\":\"MAGNETIC_FLUX_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":132",
                "\"viaDefinitionType\":\"ILLUMINANCE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":133",
                "\"viaDefinitionType\":\"LUMINOUS_FLUX_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":134",
                "\"viaDefinitionType\":\"LUMINOUS_INTENSITY_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":135",
                "\"viaDefinitionType\":\"AMOUNT_OF_SUBSTANCE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":207",
                "\"viaDefinitionType\":\"ELECTRIC_CHARGE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":208",
                "\"viaDefinitionType\":\"CAPACITANCE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":209",
                "\"viaDefinitionType\":\"MAGNETIC_FLUX_DENSITY_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":210",
                "\"viaDefinitionType\":\"INDUCTANCE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":211",
                "\"viaDefinitionType\":\"RADIOACTIVITY_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":212",
                "\"viaDefinitionType\":\"ABSORBED_DOSE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":213",
                "\"viaDefinitionType\":\"DOSE_EQUIVALENT_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":214",
                "\"viaDefinitionType\":\"ACCELERATION_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":215",
                "\"viaDefinitionType\":\"VELOCITY_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":216",
                "\"viaDefinitionType\":\"THERMAL_RESISTANCE_MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":217");
    }

    @Test
    void shouldEmbedUserDefinedAndRepresentationLeafDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_META_LEAF',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');\n"
        + "#13=VALUE_REPRESENTATION_ITEM('roughness',DESCRIPTIVE_MEASURE('Ra 3.2'));\n"
        + "#14=ADDRESS('HQ','42','Market St',$,'Shanghai','Shanghai','200000','CN',$,'+86','cad@example.com',$);\n"
        + "#15=PROPERTY_DEFINITION('PD_DESC','',#12);\n"
        + "#16=PROPERTY_DEFINITION('PD_VAL','',#13);\n"
        + "#17=PROPERTY_DEFINITION('PD_ADDR','',#14);\n"
        + "#18=PROPERTY_DEFINITION_REPRESENTATION(#15,#10);\n"
        + "#19=PROPERTY_DEFINITION_REPRESENTATION(#16,#10);\n"
        + "#20=PROPERTY_DEFINITION_REPRESENTATION(#17,#10);\n"
        + "#21=CARTESIAN_POINT('M0',(0.0,0.0));\n"
        + "#22=DIRECTION('DX0',(1.0,0.0));\n"
        + "#23=AXIS2_PLACEMENT_2D('MAP0',#21,#22);\n"
        + "#24=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYM'));\n"
        + "#25=REPRESENTATION('REP_UDEF',(),#24);\n"
        + "#26=REPRESENTATION_MAP(#23,#25);\n"
        + "#27=CARTESIAN_POINT('T0',(3.0,4.0));\n"
        + "#28=AXIS2_PLACEMENT_2D('TGT0',#27,#22);\n"
        + "#29=USER_DEFINED_CURVE_FONT('UCF0',#26,#28);\n"
        + "#30=USER_DEFINED_MARKER('UDM0',#26,#28);\n"
        + "#31=USER_DEFINED_TERMINATOR_SYMBOL('UDT0',#26,#28);\n"
        + "#32=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#33=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#34=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#35=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#36=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#37=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#38=ANNOTATION_TEXT_OCCURRENCE('NOTE_DESC_LEAF','',#32);\n"
        + "#39=ANNOTATION_TEXT_OCCURRENCE('NOTE_VALUE_LEAF','',#33);\n"
        + "#40=ANNOTATION_TEXT_OCCURRENCE('NOTE_ADDRESS_LEAF','',#34);\n"
        + "#41=ANNOTATION_TEXT_OCCURRENCE('NOTE_USER_CURVE_FONT','',#35);\n"
        + "#42=ANNOTATION_TEXT_OCCURRENCE('NOTE_USER_MARKER','',#36);\n"
        + "#43=ANNOTATION_TEXT_OCCURRENCE('NOTE_USER_TERMINATOR','',#37);\n"
        + "#44=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#12,#10,#38,#8);\n"
        + "#45=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#13,#10,#39,#8);\n"
        + "#46=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#14,#10,#40,#8);\n"
        + "#47=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#29,#10,#41,#8);\n"
        + "#48=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#30,#10,#42,#8);\n"
        + "#49=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#31,#10,#43,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"DESCRIPTIVE_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"VALUE_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"ADDRESS\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"USER_DEFINED_CURVE_FONT\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"REPRESENTATION_MAP\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_2D\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"USER_DEFINED_MARKER\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"USER_DEFINED_TERMINATOR_SYMBOL\"",
                "\"viaDefinitionId\":31");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"DESCRIPTIVE_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"VALUE_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"ADDRESS\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"USER_DEFINED_CURVE_FONT\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"REPRESENTATION_MAP\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_2D\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"USER_DEFINED_MARKER\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"USER_DEFINED_TERMINATOR_SYMBOL\"",
                "\"viaDefinitionId\":31");
    }

    @Test
    void shouldEmbedMeasureRepresentationItemMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_MEASURE',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=(NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));\n"
        + "#13=MEASURE_REPRESENTATION_ITEM('thickness',LENGTH_MEASURE(2.5),#12);\n"
        + "#14=PROPERTY_DEFINITION('PD_MEASURE','',#13);\n"
        + "#15=PROPERTY_DEFINITION_REPRESENTATION(#14,#10);\n"
        + "#16=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#17=ANNOTATION_TEXT_OCCURRENCE('NOTE_MEASURE','',#16);\n"
        + "#18=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#13,#10,#17,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_MEASURE\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_MEASURE\"",
                "\"viaDefinitionType\":\"MEASURE_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"SI_UNIT\"",
                "\"viaDefinitionId\":12");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_MEASURE\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_MEASURE\"",
                "\"viaDefinitionType\":\"MEASURE_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"SI_UNIT\"",
                "\"viaDefinitionId\":12");
    }

    @Test
    void shouldEmbedDirectRepresentationItemLinkDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_USED',(),#9);\n"
        + "#11=REPRESENTATION('REP_AUX',(),#9);\n"
        + "#12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);\n"
        + "#13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#15=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#16=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);\n"
        + "#18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);\n"
        + "#19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);\n"
        + "#20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));\n"
        + "#21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);\n"
        + "#22=REPRESENTATION_ITEM('REP_ITEM_ONLY');\n"
        + "#23=PROPERTY_DEFINITION('PD_REP_ITEM','',#22);\n"
        + "#24=PROPERTY_DEFINITION_REPRESENTATION(#23,#10);\n"
        + "#25=(GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('GEOM_ITEM_ONLY'));\n"
        + "#26=PROPERTY_DEFINITION('PD_GEOM_ITEM','',#25);\n"
        + "#27=PROPERTY_DEFINITION_REPRESENTATION(#26,#10);\n"
        + "#28=(TOPOLOGICAL_REPRESENTATION_ITEM('TOPO_ITEM_ONLY'));\n"
        + "#29=PROPERTY_DEFINITION('PD_TOPO_ITEM','',#28);\n"
        + "#30=PROPERTY_DEFINITION_REPRESENTATION(#29,#10);\n"
        + "#31=(NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));\n"
        + "#32=MEASURE_REPRESENTATION_ITEM('thickness',LENGTH_MEASURE(2.5),#31);\n"
        + "#33=PROPERTY_DEFINITION('PD_MEASURE','',#32);\n"
        + "#34=PROPERTY_DEFINITION_REPRESENTATION(#33,#10);\n"
        + "#35=DESCRIPTIVE_REPRESENTATION_ITEM('DESC_ITEM','descriptive');\n"
        + "#36=PROPERTY_DEFINITION('PD_DESC_ITEM','',#35);\n"
        + "#37=PROPERTY_DEFINITION_REPRESENTATION(#36,#10);\n"
        + "#38=VALUE_REPRESENTATION_ITEM('VALUE_ITEM',INTEGER_REPRESENTATION_ITEM(7));\n"
        + "#39=PROPERTY_DEFINITION('PD_VALUE_ITEM','',#38);\n"
        + "#40=PROPERTY_DEFINITION_REPRESENTATION(#39,#10);\n"
        + "#41=MEASURE_WITH_UNIT(LENGTH_MEASURE(9.5),#31);\n"
        + "#42=PROPERTY_DEFINITION('PD_MEASURE_WITH_UNIT','',#41);\n"
        + "#43=PROPERTY_DEFINITION_REPRESENTATION(#42,#10);\n"
        + "#44=ADDRESS('HQ','','Main St','','Shanghai','Shanghai','200000','CN','','','','');\n"
        + "#45=PROPERTY_DEFINITION('PD_ADDRESS','',#44);\n"
        + "#46=PROPERTY_DEFINITION_REPRESENTATION(#45,#10);\n"
        + "#47=CHARACTERIZED_OBJECT('CHAR_OBJ','characterized');\n"
        + "#48=PROPERTY_DEFINITION('PD_CHAR_OBJ','',#47);\n"
        + "#49=PROPERTY_DEFINITION_REPRESENTATION(#48,#10);\n"
        + "#50=DIMENSIONAL_EXPONENTS(1.0,0.0,0.0,0.0,0.0,0.0,0.0);\n"
        + "#51=PROPERTY_DEFINITION('PD_DIM_EXP','',#50);\n"
        + "#52=PROPERTY_DEFINITION_REPRESENTATION(#51,#10);\n"
        + "#53=(VERTEX() TOPOLOGICAL_REPRESENTATION_ITEM('VERT_MARK'));\n"
        + "#54=PROPERTY_DEFINITION('PD_VERTEX','',#53);\n"
        + "#55=PROPERTY_DEFINITION_REPRESENTATION(#54,#10);\n"
        + "#56=(EDGE() TOPOLOGICAL_REPRESENTATION_ITEM('EDGE_MARK'));\n"
        + "#57=PROPERTY_DEFINITION('PD_EDGE','',#56);\n"
        + "#58=PROPERTY_DEFINITION_REPRESENTATION(#57,#10);\n"
        + "#60=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#61=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#62=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#63=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#64=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#65=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#66=CARTESIAN_POINT('P6',(6.0,0.0,0.0));\n"
        + "#67=CARTESIAN_POINT('P7',(7.0,0.0,0.0));\n"
        + "#68=CARTESIAN_POINT('P8',(8.0,0.0,0.0));\n"
        + "#69=CARTESIAN_POINT('P9',(9.0,0.0,0.0));\n"
        + "#70=CARTESIAN_POINT('P10',(10.0,0.0,0.0));\n"
        + "#71=ANNOTATION_TEXT_OCCURRENCE('NOTE_REP_ITEM','',#60);\n"
        + "#72=ANNOTATION_TEXT_OCCURRENCE('NOTE_GEOM_ITEM','',#61);\n"
        + "#73=ANNOTATION_TEXT_OCCURRENCE('NOTE_TOPO_ITEM','',#62);\n"
        + "#74=ANNOTATION_TEXT_OCCURRENCE('NOTE_MEASURE_ITEM','',#63);\n"
        + "#75=ANNOTATION_TEXT_OCCURRENCE('NOTE_DESC_ITEM','',#64);\n"
        + "#76=ANNOTATION_TEXT_OCCURRENCE('NOTE_VALUE_ITEM','',#65);\n"
        + "#77=ANNOTATION_TEXT_OCCURRENCE('NOTE_MEASURE_WITH_UNIT','',#66);\n"
        + "#78=ANNOTATION_TEXT_OCCURRENCE('NOTE_ADDRESS','',#67);\n"
        + "#79=ANNOTATION_TEXT_OCCURRENCE('NOTE_CHAR_OBJ','',#68);\n"
        + "#80=ANNOTATION_TEXT_OCCURRENCE('NOTE_DIM_EXP','',#69);\n"
        + "#81=ANNOTATION_TEXT_OCCURRENCE('NOTE_VERTEX','',#70);\n"
        + "#82=ANNOTATION_TEXT_OCCURRENCE('NOTE_EDGE','',#71);\n"
        + "#83=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#22,#10,#71,#8);\n"
        + "#84=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#25,#10,#72,#8);\n"
        + "#85=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#28,#10,#73,#8);\n"
        + "#86=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#32,#10,#74,#8);\n"
        + "#87=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#35,#10,#75,#8);\n"
        + "#88=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#38,#10,#76,#8);\n"
        + "#89=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#41,#10,#77,#8);\n"
        + "#90=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#44,#10,#78,#8);\n"
        + "#91=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#47,#10,#79,#8);\n"
        + "#92=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#50,#10,#80,#8);\n"
        + "#93=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#53,#10,#81,#8);\n"
        + "#94=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#56,#10,#82,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_REP_ITEM\"",
                "\"name\":\"NOTE_GEOM_ITEM\"",
                "\"name\":\"NOTE_TOPO_ITEM\"",
                "\"name\":\"NOTE_MEASURE_ITEM\"",
                "\"name\":\"NOTE_DESC_ITEM\"",
                "\"name\":\"NOTE_VALUE_ITEM\"",
                "\"name\":\"NOTE_MEASURE_WITH_UNIT\"",
                "\"name\":\"NOTE_ADDRESS\"",
                "\"name\":\"NOTE_CHAR_OBJ\"",
                "\"name\":\"NOTE_DIM_EXP\"",
                "\"name\":\"NOTE_VERTEX\"",
                "\"name\":\"NOTE_EDGE\"",
                "\"viaDefinitionType\":\"REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"GEOMETRIC_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"TOPOLOGICAL_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"MEASURE_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"DESCRIPTIVE_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"VALUE_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionType\":\"ADDRESS\"",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionType\":\"CHARACTERIZED_OBJECT\"",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionType\":\"DIMENSIONAL_EXPONENTS\"",
                "\"viaDefinitionId\":50",
                "\"viaDefinitionType\":\"VERTEX\"",
                "\"viaDefinitionId\":53",
                "\"viaDefinitionType\":\"EDGE\"",
                "\"viaDefinitionId\":56",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionId\":48",
                "\"viaDefinitionId\":51",
                "\"viaDefinitionId\":54",
                "\"viaDefinitionId\":57",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionId\":46",
                "\"viaDefinitionId\":49",
                "\"viaDefinitionId\":52",
                "\"viaDefinitionId\":55",
                "\"viaDefinitionId\":58",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"SI_UNIT\"",
                "\"viaDefinitionId\":31");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_REP_ITEM\"",
                "\"name\":\"NOTE_GEOM_ITEM\"",
                "\"name\":\"NOTE_TOPO_ITEM\"",
                "\"name\":\"NOTE_MEASURE_ITEM\"",
                "\"name\":\"NOTE_DESC_ITEM\"",
                "\"name\":\"NOTE_VALUE_ITEM\"",
                "\"name\":\"NOTE_MEASURE_WITH_UNIT\"",
                "\"name\":\"NOTE_ADDRESS\"",
                "\"name\":\"NOTE_CHAR_OBJ\"",
                "\"name\":\"NOTE_DIM_EXP\"",
                "\"name\":\"NOTE_VERTEX\"",
                "\"name\":\"NOTE_EDGE\"",
                "\"viaDefinitionType\":\"REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"GEOMETRIC_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"TOPOLOGICAL_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"MEASURE_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"DESCRIPTIVE_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"VALUE_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"MEASURE_WITH_UNIT\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionType\":\"ADDRESS\"",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionType\":\"CHARACTERIZED_OBJECT\"",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionType\":\"DIMENSIONAL_EXPONENTS\"",
                "\"viaDefinitionId\":50",
                "\"viaDefinitionType\":\"VERTEX\"",
                "\"viaDefinitionId\":53",
                "\"viaDefinitionType\":\"EDGE\"",
                "\"viaDefinitionId\":56",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionId\":48",
                "\"viaDefinitionId\":51",
                "\"viaDefinitionId\":54",
                "\"viaDefinitionId\":57",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionId\":46",
                "\"viaDefinitionId\":49",
                "\"viaDefinitionId\":52",
                "\"viaDefinitionId\":55",
                "\"viaDefinitionId\":58",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"SI_UNIT\"",
                "\"viaDefinitionId\":31");
    }

    @Test
    void shouldEmbedDirectMapTransformAndPointReplicaLinkDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_USED',(),#9);\n"
        + "#11=REPRESENTATION('REP_AUX',(),#9);\n"
        + "#12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);\n"
        + "#13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#15=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#16=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);\n"
        + "#18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);\n"
        + "#19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);\n"
        + "#20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));\n"
        + "#21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);\n"
        + "#22=CARTESIAN_POINT('MAP_P0',(2.0,0.0));\n"
        + "#23=DIRECTION('MAP_D0',(1.0,0.0));\n"
        + "#24=AXIS2_PLACEMENT_2D('MAP0',#22,#23);\n"
        + "#25=REPRESENTATION('MAP_REP',(),#9);\n"
        + "#26=REPRESENTATION_MAP(#24,#25);\n"
        + "#27=SYMBOL_REPRESENTATION_MAP(#24,#25);\n"
        + "#28=CARTESIAN_POINT('MAP_P1',(3.0,0.0));\n"
        + "#29=DIRECTION('MAP_D1',(0.0,1.0));\n"
        + "#30=AXIS2_PLACEMENT_2D('TGT0',#28,#29);\n"
        + "#31=MAPPED_ITEM(#26,#30);\n"
        + "#32=CARTESIAN_POINT('TR0',(4.0,0.0,0.0));\n"
        + "#33=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#34=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',#16,#33,#32,1.0,#15);\n"
        + "#35=CARTESIAN_POINT('PR_P',(5.0,0.0,0.0));\n"
        + "#36=POINT_REPLICA('PR0',#35,#34);\n"
        + "#40=PROPERTY_DEFINITION('PD_REP_MAP','',#26);\n"
        + "#41=PROPERTY_DEFINITION('PD_SYM_MAP','',#27);\n"
        + "#42=PROPERTY_DEFINITION('PD_MAPPED','',#31);\n"
        + "#43=PROPERTY_DEFINITION('PD_ITEM_XF','',#19);\n"
        + "#44=PROPERTY_DEFINITION('PD_CART_XF','',#34);\n"
        + "#45=PROPERTY_DEFINITION('PD_POINT_REPLICA','',#36);\n"
        + "#50=PROPERTY_DEFINITION_REPRESENTATION(#40,#10);\n"
        + "#51=PROPERTY_DEFINITION_REPRESENTATION(#41,#10);\n"
        + "#52=PROPERTY_DEFINITION_REPRESENTATION(#42,#10);\n"
        + "#53=PROPERTY_DEFINITION_REPRESENTATION(#43,#10);\n"
        + "#54=PROPERTY_DEFINITION_REPRESENTATION(#44,#10);\n"
        + "#55=PROPERTY_DEFINITION_REPRESENTATION(#45,#10);\n"
        + "#60=ANNOTATION_TEXT_OCCURRENCE('NOTE_REP_MAP','',#13);\n"
        + "#61=ANNOTATION_TEXT_OCCURRENCE('NOTE_SYM_MAP','',#14);\n"
        + "#62=ANNOTATION_TEXT_OCCURRENCE('NOTE_MAPPED_ITEM','',#32);\n"
        + "#63=ANNOTATION_TEXT_OCCURRENCE('NOTE_ITEM_XF','',#35);\n"
        + "#64=ANNOTATION_TEXT_OCCURRENCE('NOTE_CART_XF','',#13);\n"
        + "#65=ANNOTATION_TEXT_OCCURRENCE('NOTE_POINT_REPLICA','',#14);\n"
        + "#70=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#26,#10,#60,#8);\n"
        + "#71=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#27,#10,#61,#8);\n"
        + "#72=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#31,#10,#62,#8);\n"
        + "#73=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#19,#10,#63,#8);\n"
        + "#74=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#34,#10,#64,#8);\n"
        + "#75=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#36,#10,#65,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_REP_MAP\"",
                "\"name\":\"NOTE_SYM_MAP\"",
                "\"name\":\"NOTE_MAPPED_ITEM\"",
                "\"name\":\"NOTE_ITEM_XF\"",
                "\"name\":\"NOTE_CART_XF\"",
                "\"name\":\"NOTE_POINT_REPLICA\"",
                "\"viaDefinitionType\":\"REPRESENTATION_MAP\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"SYMBOL_REPRESENTATION_MAP\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"MAPPED_ITEM\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"CARTESIAN_TRANSFORMATION_OPERATOR_3D\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"POINT_REPLICA\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":50",
                "\"viaDefinitionId\":55",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_2D\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":21");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_REP_MAP\"",
                "\"name\":\"NOTE_SYM_MAP\"",
                "\"name\":\"NOTE_MAPPED_ITEM\"",
                "\"name\":\"NOTE_ITEM_XF\"",
                "\"name\":\"NOTE_CART_XF\"",
                "\"name\":\"NOTE_POINT_REPLICA\"",
                "\"viaDefinitionType\":\"REPRESENTATION_MAP\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"SYMBOL_REPRESENTATION_MAP\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"MAPPED_ITEM\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"CARTESIAN_TRANSFORMATION_OPERATOR_3D\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"POINT_REPLICA\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":50",
                "\"viaDefinitionId\":55",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_2D\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":21");
    }

    @Test
    void shouldEmbedDirectGenericPointAndCurveLinkDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_USED',(),#9);\n"
        + "#11=REPRESENTATION('REP_AUX',(),#9);\n"
        + "#12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);\n"
        + "#13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#15=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#16=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);\n"
        + "#18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);\n"
        + "#19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);\n"
        + "#20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));\n"
        + "#21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);\n"
        + "#22=(POINT() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('PT0'));\n"
        + "#23=(CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('CV0'));\n"
        + "#24=PROPERTY_DEFINITION('PD_POINT','',#22);\n"
        + "#25=PROPERTY_DEFINITION('PD_CURVE','',#23);\n"
        + "#26=PROPERTY_DEFINITION_REPRESENTATION(#24,#10);\n"
        + "#27=PROPERTY_DEFINITION_REPRESENTATION(#25,#10);\n"
        + "#28=CARTESIAN_POINT('N0',(2.0,0.0,0.0));\n"
        + "#29=CARTESIAN_POINT('N1',(3.0,0.0,0.0));\n"
        + "#30=ANNOTATION_TEXT_OCCURRENCE('NOTE_POINT_GENERIC','',#28);\n"
        + "#31=ANNOTATION_TEXT_OCCURRENCE('NOTE_CURVE_GENERIC','',#29);\n"
        + "#32=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#22,#10,#30,#8);\n"
        + "#33=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#23,#10,#31,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_POINT_GENERIC\"",
                "\"name\":\"NOTE_CURVE_GENERIC\"",
                "\"viaDefinitionType\":\"POINT\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"CURVE\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":21");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_POINT_GENERIC\"",
                "\"name\":\"NOTE_CURVE_GENERIC\"",
                "\"viaDefinitionType\":\"POINT\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"CURVE\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":21");
    }

    @Test
    void shouldEmbedDirectRepresentationAndShapeAspectOccurrenceDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_DIRECT',(),#9);\n"
        + "#11=REPRESENTATION('REP_AUX',(),#9);\n"
        + "#12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);\n"
        + "#13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#15=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#16=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);\n"
        + "#18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);\n"
        + "#19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);\n"
        + "#20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));\n"
        + "#21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);\n"
        + "#22=SHAPE_ASPECT_OCCURRENCE('SAO0','occ',#7,.T.,#8);\n"
        + "#23=CARTESIAN_POINT('N0',(2.0,0.0,0.0));\n"
        + "#24=CARTESIAN_POINT('N1',(3.0,0.0,0.0));\n"
        + "#25=ANNOTATION_TEXT_OCCURRENCE('NOTE_REP_DIRECT','',#23);\n"
        + "#26=ANNOTATION_TEXT_OCCURRENCE('NOTE_SHAPE_OCC','',#24);\n"
        + "#27=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#10,#10,#25,#8);\n"
        + "#28=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#22,#10,#26,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_REP_DIRECT\"",
                "\"name\":\"NOTE_SHAPE_OCC\"",
                "\"viaDefinitionType\":\"REPRESENTATION\"",
                "\"viaDefinitionId\":10",
                "\"viaDefinitionType\":\"SHAPE_ASPECT_OCCURRENCE\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"GEOMETRIC_REPRESENTATION_CONTEXT\"",
                "\"viaDefinitionId\":9",
                "\"viaDefinitionType\":\"SHAPE_ASPECT\"",
                "\"viaDefinitionId\":8");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_REP_DIRECT\"",
                "\"name\":\"NOTE_SHAPE_OCC\"",
                "\"viaDefinitionType\":\"REPRESENTATION\"",
                "\"viaDefinitionId\":10",
                "\"viaDefinitionType\":\"SHAPE_ASPECT_OCCURRENCE\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"GEOMETRIC_REPRESENTATION_CONTEXT\"",
                "\"viaDefinitionId\":9",
                "\"viaDefinitionType\":\"SHAPE_ASPECT\"",
                "\"viaDefinitionId\":8");
    }

    @Test
    void shouldEmbedDirectGeometricLeafLinkDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_USED',(),#9);\n"
        + "#11=REPRESENTATION('REP_AUX',(),#9);\n"
        + "#12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);\n"
        + "#13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#15=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#16=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);\n"
        + "#18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);\n"
        + "#19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);\n"
        + "#20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));\n"
        + "#21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);\n"
        + "#22=CARTESIAN_POINT('DP0',(2.0,0.0,0.0));\n"
        + "#23=PROPERTY_DEFINITION('PD_POINT','',#22);\n"
        + "#24=PROPERTY_DEFINITION_REPRESENTATION(#23,#10);\n"
        + "#25=DIRECTION('DD0',(0.0,1.0,0.0));\n"
        + "#26=PROPERTY_DEFINITION('PD_DIRECTION','',#25);\n"
        + "#27=PROPERTY_DEFINITION_REPRESENTATION(#26,#10);\n"
        + "#28=VECTOR('V0',#25,2.5);\n"
        + "#29=PROPERTY_DEFINITION('PD_VECTOR','',#28);\n"
        + "#30=PROPERTY_DEFINITION_REPRESENTATION(#29,#10);\n"
        + "#31=AXIS2_PLACEMENT_3D('AX2',#22,#15,#16);\n"
        + "#32=PROPERTY_DEFINITION('PD_AXIS','',#31);\n"
        + "#33=PROPERTY_DEFINITION_REPRESENTATION(#32,#10);\n"
        + "#34=PLANE('PL0',#31);\n"
        + "#35=PROPERTY_DEFINITION('PD_PLANE','',#34);\n"
        + "#36=PROPERTY_DEFINITION_REPRESENTATION(#35,#10);\n"
        + "#40=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#41=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#42=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#43=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#44=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#45=ANNOTATION_TEXT_OCCURRENCE('NOTE_POINT','',#40);\n"
        + "#46=ANNOTATION_TEXT_OCCURRENCE('NOTE_DIRECTION','',#41);\n"
        + "#47=ANNOTATION_TEXT_OCCURRENCE('NOTE_VECTOR','',#42);\n"
        + "#48=ANNOTATION_TEXT_OCCURRENCE('NOTE_AXIS','',#43);\n"
        + "#49=ANNOTATION_TEXT_OCCURRENCE('NOTE_PLANE','',#44);\n"
        + "#50=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#22,#10,#45,#8);\n"
        + "#51=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#25,#10,#46,#8);\n"
        + "#52=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#28,#10,#47,#8);\n"
        + "#53=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#31,#10,#48,#8);\n"
        + "#54=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#34,#10,#49,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_POINT\"",
                "\"name\":\"NOTE_DIRECTION\"",
                "\"name\":\"NOTE_VECTOR\"",
                "\"name\":\"NOTE_AXIS\"",
                "\"name\":\"NOTE_PLANE\"",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"VECTOR\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"PLANE\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":21");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_POINT\"",
                "\"name\":\"NOTE_DIRECTION\"",
                "\"name\":\"NOTE_VECTOR\"",
                "\"name\":\"NOTE_AXIS\"",
                "\"name\":\"NOTE_PLANE\"",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"VECTOR\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"PLANE\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":21");
    }

    @Test
    void shouldEmbedDirectCurveAndSurfaceLinkDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_USED',(),#9);\n"
        + "#11=REPRESENTATION('REP_AUX',(),#9);\n"
        + "#12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);\n"
        + "#13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#15=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#16=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);\n"
        + "#18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);\n"
        + "#19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);\n"
        + "#20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));\n"
        + "#21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);\n"
        + "#22=CARTESIAN_POINT('P0',(2.0,0.0,0.0));\n"
        + "#23=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#24=VECTOR('V0',#23,2.5);\n"
        + "#25=LINE('L0',#22,#24);\n"
        + "#26=PROPERTY_DEFINITION('PD_LINE','',#25);\n"
        + "#27=PROPERTY_DEFINITION_REPRESENTATION(#26,#10);\n"
        + "#28=CIRCLE('C0',#17,5.0);\n"
        + "#29=PROPERTY_DEFINITION('PD_CIRCLE','',#28);\n"
        + "#30=PROPERTY_DEFINITION_REPRESENTATION(#29,#10);\n"
        + "#31=ELLIPSE('E0',#17,6.0,3.0);\n"
        + "#32=PROPERTY_DEFINITION('PD_ELLIPSE','',#31);\n"
        + "#33=PROPERTY_DEFINITION_REPRESENTATION(#32,#10);\n"
        + "#34=POLYLINE('PL0',(#13,#14,#22));\n"
        + "#35=PROPERTY_DEFINITION('PD_POLYLINE','',#34);\n"
        + "#36=PROPERTY_DEFINITION_REPRESENTATION(#35,#10);\n"
        + "#37=TRIMMED_CURVE('TC0',#25,(#22),(#14),.T.,.PARAMETER.);\n"
        + "#38=PROPERTY_DEFINITION('PD_TRIM','',#37);\n"
        + "#39=PROPERTY_DEFINITION_REPRESENTATION(#38,#10);\n"
        + "#40=CYLINDRICAL_SURFACE('CYL0',#17,2.0);\n"
        + "#41=PROPERTY_DEFINITION('PD_CYL','',#40);\n"
        + "#42=PROPERTY_DEFINITION_REPRESENTATION(#41,#10);\n"
        + "#43=SURFACE_OF_LINEAR_EXTRUSION('SOLE0',#25,#24);\n"
        + "#44=PROPERTY_DEFINITION('PD_SOLE','',#43);\n"
        + "#45=PROPERTY_DEFINITION_REPRESENTATION(#44,#10);\n"
        + "#46=AXIS1_PLACEMENT('AXIS1',#22,#15);\n"
        + "#47=SURFACE_OF_REVOLUTION('SOR0',#25,#46);\n"
        + "#48=PROPERTY_DEFINITION('PD_SOR','',#47);\n"
        + "#49=PROPERTY_DEFINITION_REPRESENTATION(#48,#10);\n"
        + "#60=CARTESIAN_POINT('N0',(0.0,0.0,0.0));\n"
        + "#61=CARTESIAN_POINT('N1',(1.0,0.0,0.0));\n"
        + "#62=CARTESIAN_POINT('N2',(2.0,0.0,0.0));\n"
        + "#63=CARTESIAN_POINT('N3',(3.0,0.0,0.0));\n"
        + "#64=CARTESIAN_POINT('N4',(4.0,0.0,0.0));\n"
        + "#65=CARTESIAN_POINT('N5',(5.0,0.0,0.0));\n"
        + "#66=CARTESIAN_POINT('N6',(6.0,0.0,0.0));\n"
        + "#67=ANNOTATION_TEXT_OCCURRENCE('NOTE_LINE','',#60);\n"
        + "#68=ANNOTATION_TEXT_OCCURRENCE('NOTE_CIRCLE','',#61);\n"
        + "#69=ANNOTATION_TEXT_OCCURRENCE('NOTE_ELLIPSE','',#62);\n"
        + "#70=ANNOTATION_TEXT_OCCURRENCE('NOTE_POLYLINE','',#63);\n"
        + "#71=ANNOTATION_TEXT_OCCURRENCE('NOTE_TRIM','',#64);\n"
        + "#72=ANNOTATION_TEXT_OCCURRENCE('NOTE_CYL','',#65);\n"
        + "#73=ANNOTATION_TEXT_OCCURRENCE('NOTE_SOLE','',#66);\n"
        + "#74=ANNOTATION_TEXT_OCCURRENCE('NOTE_SOR','',#67);\n"
        + "#75=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#25,#10,#67,#8);\n"
        + "#76=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#28,#10,#68,#8);\n"
        + "#77=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#31,#10,#69,#8);\n"
        + "#78=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#34,#10,#70,#8);\n"
        + "#79=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#37,#10,#71,#8);\n"
        + "#80=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#40,#10,#72,#8);\n"
        + "#81=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#43,#10,#73,#8);\n"
        + "#82=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#47,#10,#74,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_LINE\"",
                "\"name\":\"NOTE_CIRCLE\"",
                "\"name\":\"NOTE_ELLIPSE\"",
                "\"name\":\"NOTE_POLYLINE\"",
                "\"name\":\"NOTE_TRIM\"",
                "\"name\":\"NOTE_CYL\"",
                "\"name\":\"NOTE_SOLE\"",
                "\"name\":\"NOTE_SOR\"",
                "\"viaDefinitionType\":\"LINE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"CIRCLE\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"ELLIPSE\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"POLYLINE\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"TRIMMED_CURVE\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"CYLINDRICAL_SURFACE\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"SURFACE_OF_LINEAR_EXTRUSION\"",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionType\":\"SURFACE_OF_REVOLUTION\"",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionId\":49",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":21");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_LINE\"",
                "\"name\":\"NOTE_CIRCLE\"",
                "\"name\":\"NOTE_ELLIPSE\"",
                "\"name\":\"NOTE_POLYLINE\"",
                "\"name\":\"NOTE_TRIM\"",
                "\"name\":\"NOTE_CYL\"",
                "\"name\":\"NOTE_SOLE\"",
                "\"name\":\"NOTE_SOR\"",
                "\"viaDefinitionType\":\"LINE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"CIRCLE\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"ELLIPSE\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"POLYLINE\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"TRIMMED_CURVE\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"CYLINDRICAL_SURFACE\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"SURFACE_OF_LINEAR_EXTRUSION\"",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionType\":\"SURFACE_OF_REVOLUTION\"",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionId\":49",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":21");
    }

    @Test
    void shouldEmbedDirectConicCurveLinkDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_A',(),#9);\n"
        + "#11=REPRESENTATION('REP_B',(),#9);\n"
        + "#12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);\n"
        + "#13=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#14=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#15=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#16=AXIS2_PLACEMENT_3D('AX3',#13,#14,#15);\n"
        + "#17=PARABOLA('PAR0',#16,2.0);\n"
        + "#18=HYPERBOLA('HYP0',#16,4.0,2.0);\n"
        + "#19=DEGENERATE_CONIC('DC0',#16);\n"
        + "#20=PROPERTY_DEFINITION('PD_PAR','',#17);\n"
        + "#21=PROPERTY_DEFINITION('PD_HYP','',#18);\n"
        + "#22=PROPERTY_DEFINITION('PD_DC','',#19);\n"
        + "#23=PROPERTY_DEFINITION_REPRESENTATION(#20,#10);\n"
        + "#24=PROPERTY_DEFINITION_REPRESENTATION(#21,#10);\n"
        + "#25=PROPERTY_DEFINITION_REPRESENTATION(#22,#10);\n"
        + "#26=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#27=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#28=DIRECTION('TZ',(0.0,0.0,1.0));\n"
        + "#29=DIRECTION('TX',(1.0,0.0,0.0));\n"
        + "#30=AXIS2_PLACEMENT_3D('AX0',#26,#28,#29);\n"
        + "#31=AXIS2_PLACEMENT_3D('AX1',#27,#28,#29);\n"
        + "#32=ITEM_DEFINED_TRANSFORMATION('T1','',#30,#31);\n"
        + "#33=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#32));\n"
        + "#34=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);\n"
        + "#35=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONIC','',#13);\n"
        + "#36=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#17,#10,#35,#8);\n"
        + "#37=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#18,#10,#35,#8);\n"
        + "#38=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#19,#10,#35,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_CONIC\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_B\"",
                "\"viaDefinitionType\":\"PARABOLA\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"HYPERBOLA\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"DEGENERATE_CONIC\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionId\":25");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_CONIC\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_B\"",
                "\"viaDefinitionType\":\"PARABOLA\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"HYPERBOLA\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"DEGENERATE_CONIC\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionId\":25");
    }

    @Test
    void shouldEmbedDirectSplineCurveAndSurfaceLinkDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_A',(),#9);\n"
        + "#11=REPRESENTATION('REP_B',(),#9);\n"
        + "#12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);\n"
        + "#13=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#15=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#16=CARTESIAN_POINT('P3',(0.0,1.0,1.0));\n"
        + "#17=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#18=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#19=AXIS2_PLACEMENT_3D('AX0',#13,#17,#18);\n"
        + "#20=ITEM_DEFINED_TRANSFORMATION('T1','',#19,#19);\n"
        + "#21=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#20));\n"
        + "#22=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);\n"
        + "#30=(B_SPLINE_CURVE('BSC0',2,(#13,#14,#15),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BSC0'));\n"
        + "#31=(B_SPLINE_CURVE('BSK0',2,(#13,#14,#15),.UNSPECIFIED.,.F.,.F.)\n"
        + "     B_SPLINE_CURVE_WITH_KNOTS((3,3),(0.0,1.0),.UNSPECIFIED.)\n"
        + "     BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BSK0'));\n"
        + "#32=(B_SPLINE_CURVE('RBC0',2,(#13,#14,#15),.UNSPECIFIED.,.F.,.F.)\n"
        + "     B_SPLINE_CURVE_WITH_KNOTS((3,3),(0.0,1.0),.UNSPECIFIED.)\n"
        + "     RATIONAL_B_SPLINE_CURVE((1.0,0.5,1.0))\n"
        + "     BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('RBC0'));\n"
        + "#33=(BEZIER_CURVE() B_SPLINE_CURVE(2,(#13,#14,#15),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BZ0'));\n"
        + "#34=(UNIFORM_CURVE() B_SPLINE_CURVE(2,(#13,#14,#15),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('UC0'));\n"
        + "#35=(QUASI_UNIFORM_CURVE() B_SPLINE_CURVE(2,(#13,#14,#15),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('QUC0'));\n"
        + "#36=(PIECEWISE_BEZIER_CURVE() BEZIER_CURVE() B_SPLINE_CURVE(2,(#13,#14,#15),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('PBC0'));\n"
        + "#40=(B_SPLINE_SURFACE(1,1,((#13,#14),(#15,#16)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BSS0'));\n"
        + "#41=(B_SPLINE_SURFACE(1,1,((#13,#14),(#15,#16)),.UNSPECIFIED.,.F.,.F.,.F.)\n"
        + "     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.)\n"
        + "     BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BSK0'));\n"
        + "#42=(B_SPLINE_SURFACE(1,1,((#13,#14),(#15,#16)),.UNSPECIFIED.,.F.,.F.,.F.)\n"
        + "     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.)\n"
        + "     RATIONAL_B_SPLINE_SURFACE(((1.0,1.0),(1.0,1.0)))\n"
        + "     BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('RBS0'));\n"
        + "#43=(BEZIER_SURFACE() B_SPLINE_SURFACE(1,1,((#13,#14),(#15,#16)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BZS0'));\n"
        + "#44=(UNIFORM_SURFACE() B_SPLINE_SURFACE(1,1,((#13,#14),(#15,#16)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('US0'));\n"
        + "#45=(QUASI_UNIFORM_SURFACE() B_SPLINE_SURFACE(1,1,((#13,#14),(#15,#16)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('QUS0'));\n"
        + "#46=(PIECEWISE_BEZIER_SURFACE() BEZIER_SURFACE() B_SPLINE_SURFACE(1,1,((#13,#14),(#15,#16)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('PBS0'));\n"
        + "#130=PROPERTY_DEFINITION('PD_BSC','',#30);\n"
        + "#131=PROPERTY_DEFINITION('PD_BSK','',#31);\n"
        + "#132=PROPERTY_DEFINITION('PD_RBC','',#32);\n"
        + "#133=PROPERTY_DEFINITION('PD_BZ','',#33);\n"
        + "#134=PROPERTY_DEFINITION('PD_UC','',#34);\n"
        + "#135=PROPERTY_DEFINITION('PD_QUC','',#35);\n"
        + "#136=PROPERTY_DEFINITION('PD_PBC','',#36);\n"
        + "#137=PROPERTY_DEFINITION('PD_BSS','',#40);\n"
        + "#138=PROPERTY_DEFINITION('PD_BSKS','',#41);\n"
        + "#139=PROPERTY_DEFINITION('PD_RBS','',#42);\n"
        + "#140=PROPERTY_DEFINITION('PD_BZS','',#43);\n"
        + "#141=PROPERTY_DEFINITION('PD_US','',#44);\n"
        + "#142=PROPERTY_DEFINITION('PD_QUS','',#45);\n"
        + "#143=PROPERTY_DEFINITION('PD_PBS','',#46);\n"
        + "#160=PROPERTY_DEFINITION_REPRESENTATION(#130,#10);\n"
        + "#161=PROPERTY_DEFINITION_REPRESENTATION(#131,#10);\n"
        + "#162=PROPERTY_DEFINITION_REPRESENTATION(#132,#10);\n"
        + "#163=PROPERTY_DEFINITION_REPRESENTATION(#133,#10);\n"
        + "#164=PROPERTY_DEFINITION_REPRESENTATION(#134,#10);\n"
        + "#165=PROPERTY_DEFINITION_REPRESENTATION(#135,#10);\n"
        + "#166=PROPERTY_DEFINITION_REPRESENTATION(#136,#10);\n"
        + "#167=PROPERTY_DEFINITION_REPRESENTATION(#137,#10);\n"
        + "#168=PROPERTY_DEFINITION_REPRESENTATION(#138,#10);\n"
        + "#169=PROPERTY_DEFINITION_REPRESENTATION(#139,#10);\n"
        + "#170=PROPERTY_DEFINITION_REPRESENTATION(#140,#10);\n"
        + "#171=PROPERTY_DEFINITION_REPRESENTATION(#141,#10);\n"
        + "#172=PROPERTY_DEFINITION_REPRESENTATION(#142,#10);\n"
        + "#173=PROPERTY_DEFINITION_REPRESENTATION(#143,#10);\n"
        + "#200=ANNOTATION_TEXT_OCCURRENCE('NOTE_SPLINE','',#13);\n"
        + "#201=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#30,#10,#200,#8);\n"
        + "#202=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#31,#10,#200,#8);\n"
        + "#203=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#32,#10,#200,#8);\n"
        + "#204=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#33,#10,#200,#8);\n"
        + "#205=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#34,#10,#200,#8);\n"
        + "#206=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#35,#10,#200,#8);\n"
        + "#207=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#36,#10,#200,#8);\n"
        + "#208=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#40,#10,#200,#8);\n"
        + "#209=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#41,#10,#200,#8);\n"
        + "#210=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#42,#10,#200,#8);\n"
        + "#211=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#43,#10,#200,#8);\n"
        + "#212=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#44,#10,#200,#8);\n"
        + "#213=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#45,#10,#200,#8);\n"
        + "#214=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#46,#10,#200,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_SPLINE\"",
                "\"viaDefinitionType\":\"B_SPLINE_CURVE\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"B_SPLINE_CURVE_WITH_KNOTS\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"RATIONAL_B_SPLINE_CURVE\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"BEZIER_CURVE\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"UNIFORM_CURVE\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"QUASI_UNIFORM_CURVE\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"PIECEWISE_BEZIER_CURVE\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"B_SPLINE_SURFACE\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"B_SPLINE_SURFACE_WITH_KNOTS\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionType\":\"RATIONAL_B_SPLINE_SURFACE\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"BEZIER_SURFACE\"",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionType\":\"UNIFORM_SURFACE\"",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionType\":\"QUASI_UNIFORM_SURFACE\"",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionType\":\"PIECEWISE_BEZIER_SURFACE\"",
                "\"viaDefinitionId\":46",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":130",
                "\"viaDefinitionId\":143",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":160",
                "\"viaDefinitionId\":173");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_SPLINE\"",
                "\"viaDefinitionType\":\"B_SPLINE_CURVE\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"B_SPLINE_CURVE_WITH_KNOTS\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"RATIONAL_B_SPLINE_CURVE\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"BEZIER_CURVE\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"UNIFORM_CURVE\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"QUASI_UNIFORM_CURVE\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"PIECEWISE_BEZIER_CURVE\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"B_SPLINE_SURFACE\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"B_SPLINE_SURFACE_WITH_KNOTS\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionType\":\"RATIONAL_B_SPLINE_SURFACE\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"BEZIER_SURFACE\"",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionType\":\"UNIFORM_SURFACE\"",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionType\":\"QUASI_UNIFORM_SURFACE\"",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionType\":\"PIECEWISE_BEZIER_SURFACE\"",
                "\"viaDefinitionId\":46",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":130",
                "\"viaDefinitionId\":143",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":160",
                "\"viaDefinitionId\":173");
    }

    @Test
    void shouldEmbedDirectWrapperCurveAndPrimitiveSurfaceLinkDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_USED',(),#9);\n"
        + "#11=REPRESENTATION('REP_AUX',(),#9);\n"
        + "#12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);\n"
        + "#13=CARTESIAN_POINT('O0',(0.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('O1',(1.0,0.0,0.0));\n"
        + "#15=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#16=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#17=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#18=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);\n"
        + "#19=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);\n"
        + "#20=ITEM_DEFINED_TRANSFORMATION('T1','',#18,#19);\n"
        + "#21=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#20));\n"
        + "#22=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);\n"
        + "#23=VECTOR('V0',#17,2.5);\n"
        + "#24=LINE('L0',#13,#23);\n"
        + "#25=PLANE('PL_REF',#18);\n"
        + "#26=CARTESIAN_POINT('UV0',(0.0,0.0));\n"
        + "#27=DIRECTION('DUV',(0.0,1.0));\n"
        + "#28=VECTOR('VUV',#27,1.0);\n"
        + "#29=LINE('UVL0',#26,#28);\n"
        + "#30=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');\n"
        + "#31=DEFINITIONAL_REPRESENTATION('DEF0',(#29),#30);\n"
        + "#32=PCURVE('PC0',#25,#31);\n"
        + "#33=SURFACE_CURVE('SC0',#24,(#32),.PCURVE_S1.);\n"
        + "#34=PROPERTY_DEFINITION('PD_SC','',#33);\n"
        + "#35=PROPERTY_DEFINITION_REPRESENTATION(#34,#10);\n"
        + "#36=SEAM_CURVE('SM0',#24,(#32,#32),.PCURVE_S1.);\n"
        + "#37=PROPERTY_DEFINITION('PD_SM','',#36);\n"
        + "#38=PROPERTY_DEFINITION_REPRESENTATION(#37,#10);\n"
        + "#39=OFFSET_CURVE_2D('OC2D',#32,1.0,.F.);\n"
        + "#40=PROPERTY_DEFINITION('PD_OC2D','',#39);\n"
        + "#41=PROPERTY_DEFINITION_REPRESENTATION(#40,#10);\n"
        + "#42=OFFSET_CURVE_3D('OC3D',#24,2.0,.F.,#17);\n"
        + "#43=PROPERTY_DEFINITION('PD_OC3D','',#42);\n"
        + "#44=PROPERTY_DEFINITION_REPRESENTATION(#43,#10);\n"
        + "#45=CONICAL_SURFACE('CN0',#18,4.0,0.5);\n"
        + "#46=PROPERTY_DEFINITION('PD_CONE','',#45);\n"
        + "#47=PROPERTY_DEFINITION_REPRESENTATION(#46,#10);\n"
        + "#48=SPHERICAL_SURFACE('SP0',#18,6.0);\n"
        + "#49=PROPERTY_DEFINITION('PD_SPH','',#48);\n"
        + "#50=PROPERTY_DEFINITION_REPRESENTATION(#49,#10);\n"
        + "#51=TOROIDAL_SURFACE('TO0',#18,8.0,2.0);\n"
        + "#52=PROPERTY_DEFINITION('PD_TOR','',#51);\n"
        + "#53=PROPERTY_DEFINITION_REPRESENTATION(#52,#10);\n"
        + "#70=CARTESIAN_POINT('N0',(0.0,0.0,0.0));\n"
        + "#71=CARTESIAN_POINT('N1',(1.0,0.0,0.0));\n"
        + "#72=CARTESIAN_POINT('N2',(2.0,0.0,0.0));\n"
        + "#73=CARTESIAN_POINT('N3',(3.0,0.0,0.0));\n"
        + "#74=CARTESIAN_POINT('N4',(4.0,0.0,0.0));\n"
        + "#75=CARTESIAN_POINT('N5',(5.0,0.0,0.0));\n"
        + "#76=ANNOTATION_TEXT_OCCURRENCE('NOTE_SC','',#70);\n"
        + "#77=ANNOTATION_TEXT_OCCURRENCE('NOTE_SM','',#71);\n"
        + "#78=ANNOTATION_TEXT_OCCURRENCE('NOTE_OC2D','',#72);\n"
        + "#79=ANNOTATION_TEXT_OCCURRENCE('NOTE_OC3D','',#73);\n"
        + "#80=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONE','',#74);\n"
        + "#81=ANNOTATION_TEXT_OCCURRENCE('NOTE_SPH','',#75);\n"
        + "#82=ANNOTATION_TEXT_OCCURRENCE('NOTE_TOR','',#75);\n"
        + "#83=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#33,#10,#76,#8);\n"
        + "#84=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#36,#10,#77,#8);\n"
        + "#85=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#39,#10,#78,#8);\n"
        + "#86=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#42,#10,#79,#8);\n"
        + "#87=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#45,#10,#80,#8);\n"
        + "#88=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#48,#10,#81,#8);\n"
        + "#89=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#51,#10,#82,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_SC\"",
                "\"name\":\"NOTE_SM\"",
                "\"name\":\"NOTE_OC2D\"",
                "\"name\":\"NOTE_OC3D\"",
                "\"name\":\"NOTE_CONE\"",
                "\"name\":\"NOTE_SPH\"",
                "\"name\":\"NOTE_TOR\"",
                "\"viaDefinitionType\":\"SURFACE_CURVE\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"SEAM_CURVE\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"OFFSET_CURVE_2D\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionType\":\"OFFSET_CURVE_3D\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"CONICAL_SURFACE\"",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionType\":\"SPHERICAL_SURFACE\"",
                "\"viaDefinitionId\":48",
                "\"viaDefinitionType\":\"TOROIDAL_SURFACE\"",
                "\"viaDefinitionId\":51",
                "\"viaDefinitionType\":\"PCURVE\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"PLANE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"DEFINITIONAL_REPRESENTATION\"",
                "\"viaDefinitionId\":31");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_SC\"",
                "\"name\":\"NOTE_SM\"",
                "\"name\":\"NOTE_OC2D\"",
                "\"name\":\"NOTE_OC3D\"",
                "\"name\":\"NOTE_CONE\"",
                "\"name\":\"NOTE_SPH\"",
                "\"name\":\"NOTE_TOR\"",
                "\"viaDefinitionType\":\"SURFACE_CURVE\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"SEAM_CURVE\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"OFFSET_CURVE_2D\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionType\":\"OFFSET_CURVE_3D\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"CONICAL_SURFACE\"",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionType\":\"SPHERICAL_SURFACE\"",
                "\"viaDefinitionId\":48",
                "\"viaDefinitionType\":\"TOROIDAL_SURFACE\"",
                "\"viaDefinitionId\":51",
                "\"viaDefinitionType\":\"PCURVE\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"PLANE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"DEFINITIONAL_REPRESENTATION\"",
                "\"viaDefinitionId\":31");
    }

    @Test
    void shouldEmbedDirectCompositeReplicaAndWrapperSurfaceLinkDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_USED',(),#9);\n"
        + "#11=REPRESENTATION('REP_AUX',(),#9);\n"
        + "#12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);\n"
        + "#13=CARTESIAN_POINT('O0',(0.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('O1',(1.0,0.0,0.0));\n"
        + "#15=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#16=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);\n"
        + "#18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);\n"
        + "#19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);\n"
        + "#20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));\n"
        + "#21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);\n"
        + "#22=VECTOR('VX',#16,1.0);\n"
        + "#23=LINE('L0',#13,#22);\n"
        + "#24=COMPOSITE_CURVE_SEGMENT(.CONTINUOUS.,.T.,#23);\n"
        + "#25=(COMPOSITE_CURVE('CC0',(#24),.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('cc-name'));\n"
        + "#26=(COMPOSITE_CURVE_ON_SURFACE('CCS0',(#24),.F.) COMPOSITE_CURVE('CCS0',(#24),.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('ccs-name'));\n"
        + "#27=PLANE('PL0',#17);\n"
        + "#28=CARTESIAN_POINT('UV0',(0.0,0.0));\n"
        + "#29=DIRECTION('DUV',(1.0,0.0));\n"
        + "#30=VECTOR('VUV',#29,1.0);\n"
        + "#31=LINE('UL0',#28,#30);\n"
        + "#32=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');\n"
        + "#33=DEFINITIONAL_REPRESENTATION('DEF0',(#31),#32);\n"
        + "#34=DEGENERATE_PCURVE('DPC0',#27,#33);\n"
        + "#35=RECTANGULAR_TRIMMED_SURFACE('RTS0',#27,0.0,1.0,0.0,1.0,.T.,.T.);\n"
        + "#36=CURVE_BOUNDED_SURFACE('CBS0',#27,(#25),.T.);\n"
        + "#37=ORIENTED_SURFACE('OS0',#35,.T.);\n"
        + "#38=OFFSET_SURFACE('OFS0',#27,1.0,.F.);\n"
        + "#39=CURVE_REPLICA('CR0',#25,#81);\n"
        + "#40=SURFACE_REPLICA('SR0',#35,#81);\n"
        + "#50=PROPERTY_DEFINITION('PD_CCS','',#26);\n"
        + "#51=PROPERTY_DEFINITION('PD_CC','',#25);\n"
        + "#52=PROPERTY_DEFINITION('PD_SEG','',#24);\n"
        + "#53=PROPERTY_DEFINITION('PD_DPC','',#34);\n"
        + "#54=PROPERTY_DEFINITION('PD_RTS','',#35);\n"
        + "#55=PROPERTY_DEFINITION('PD_CBS','',#36);\n"
        + "#56=PROPERTY_DEFINITION('PD_OS','',#37);\n"
        + "#57=PROPERTY_DEFINITION('PD_OFS','',#38);\n"
        + "#58=PROPERTY_DEFINITION('PD_CR','',#39);\n"
        + "#59=PROPERTY_DEFINITION('PD_SR','',#40);\n"
        + "#60=PROPERTY_DEFINITION_REPRESENTATION(#50,#10);\n"
        + "#61=PROPERTY_DEFINITION_REPRESENTATION(#51,#10);\n"
        + "#62=PROPERTY_DEFINITION_REPRESENTATION(#52,#10);\n"
        + "#63=PROPERTY_DEFINITION_REPRESENTATION(#53,#10);\n"
        + "#64=PROPERTY_DEFINITION_REPRESENTATION(#54,#10);\n"
        + "#65=PROPERTY_DEFINITION_REPRESENTATION(#55,#10);\n"
        + "#66=PROPERTY_DEFINITION_REPRESENTATION(#56,#10);\n"
        + "#67=PROPERTY_DEFINITION_REPRESENTATION(#57,#10);\n"
        + "#68=PROPERTY_DEFINITION_REPRESENTATION(#58,#10);\n"
        + "#69=PROPERTY_DEFINITION_REPRESENTATION(#59,#10);\n"
        + "#70=ANNOTATION_TEXT_OCCURRENCE('NOTE_WRAP','',#13);\n"
        + "#71=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#24,#10,#70,#8);\n"
        + "#72=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#25,#10,#70,#8);\n"
        + "#73=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#26,#10,#70,#8);\n"
        + "#74=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#34,#10,#70,#8);\n"
        + "#75=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#35,#10,#70,#8);\n"
        + "#76=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#36,#10,#70,#8);\n"
        + "#77=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#37,#10,#70,#8);\n"
        + "#78=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#38,#10,#70,#8);\n"
        + "#79=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#39,#10,#70,#8);\n"
        + "#80=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#40,#10,#70,#8);\n"
        + "#81=CARTESIAN_TRANSFORMATION_OPERATOR_3D('CTR0',#16,#15,#13,1.0,#16);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_WRAP\"",
                "\"viaDefinitionType\":\"COMPOSITE_CURVE_SEGMENT\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"COMPOSITE_CURVE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"COMPOSITE_CURVE_ON_SURFACE\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"DEGENERATE_PCURVE\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"RECTANGULAR_TRIMMED_SURFACE\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"CURVE_BOUNDED_SURFACE\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"ORIENTED_SURFACE\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"OFFSET_SURFACE\"",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"CURVE_REPLICA\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionType\":\"SURFACE_REPLICA\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":50",
                "\"viaDefinitionId\":59",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":60",
                "\"viaDefinitionId\":69");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_WRAP\"",
                "\"viaDefinitionType\":\"COMPOSITE_CURVE_SEGMENT\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"COMPOSITE_CURVE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"COMPOSITE_CURVE_ON_SURFACE\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"DEGENERATE_PCURVE\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"RECTANGULAR_TRIMMED_SURFACE\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"CURVE_BOUNDED_SURFACE\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"ORIENTED_SURFACE\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"OFFSET_SURFACE\"",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"CURVE_REPLICA\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionType\":\"SURFACE_REPLICA\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":50",
                "\"viaDefinitionId\":59",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":60",
                "\"viaDefinitionId\":69");
    }

    @Test
    void shouldEmbedDirectTopologyAndSurfaceContainerLinkDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=SHAPE_REPRESENTATION('REP_A',(),#9);\n"
        + "#11=SHAPE_REPRESENTATION('REP_B',(),#9);\n"
        + "#12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#13=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#15=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#16=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#17=VECTOR('VX',#16,1.0);\n"
        + "#18=LINE('L0',#12,#17);\n"
        + "#19=VERTEX_POINT('V0',#12);\n"
        + "#20=VERTEX_POINT('V1',#13);\n"
        + "#21=EDGE_CURVE('E0',#19,#20,#18,.T.);\n"
        + "#22=ORIENTED_EDGE('OE0',$,$,#21,.T.);\n"
        + "#23=PATH('PTH',(#22));\n"
        + "#24=OPEN_PATH('OP0',(#22));\n"
        + "#25=ORIENTED_PATH('OP1',#23,.F.);\n"
        + "#26=EDGE_LOOP('EL0',(#22));\n"
        + "#27=POLY_LOOP('PL0',(#12,#13,#14,#15));\n"
        + "#28=CONNECTED_EDGE_SET('CES0',(#21));\n"
        + "#29=EDGE_BASED_WIREFRAME_MODEL('EBWM',(#28));\n"
        + "#30=WIRE_SHELL('WS0',(#26));\n"
        + "#31=VERTEX_LOOP('VL0',#19);\n"
        + "#32=VERTEX_SHELL('VS0',#31);\n"
        + "#33=SHELL_BASED_WIREFRAME_MODEL('SBWM',(#30,#32));\n"
        + "#34=POINT_SET('PS0',(#12,#13));\n"
        + "#35=GEOMETRIC_CURVE_SET('GCS0',(#18));\n"
        + "#36=GEOMETRIC_SET('GS0',(#34,#35,#25,#27,#29,#33));\n"
        + "#37=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#38=AXIS2_PLACEMENT_3D('AX',#12,#37,#16);\n"
        + "#39=PLANE('PL',#38);\n"
        + "#40=FACE_BOUND('FB',#27,.T.);\n"
        + "#41=ADVANCED_FACE('AF0',(#40),#39,.T.);\n"
        + "#42=ORIENTED_FACE('OF0',#41,.F.);\n"
        + "#43=FACE_SURFACE('FS0',(#40),#39,.T.);\n"
        + "#44=OPEN_SHELL('OS0',(#41));\n"
        + "#45=SURFACED_OPEN_SHELL('SOS0',(#43));\n"
        + "#46=ORIENTED_OPEN_SHELL('OOS0',#44,.F.);\n"
        + "#47=CLOSED_SHELL('CS0',(#41));\n"
        + "#48=ORIENTED_CLOSED_SHELL('OCS0',#47,.F.);\n"
        + "#49=CONNECTED_FACE_SET('CFS0',(#41));\n"
        + "#50=CONNECTED_FACE_SUB_SET('CFSS0',(#41),#49);\n"
        + "#51=FACE_BASED_SURFACE_MODEL('FBSM0',(#49,#44));\n"
        + "#52=SHELL_BASED_SURFACE_MODEL('SBSM0',(#44,#45,#46,#47,#48));\n"
        + "#53=SUBPATH('SP0',(#22),#23);\n"
        + "#54=(FACE() TOPOLOGICAL_REPRESENTATION_ITEM('F0') REPRESENTATION_ITEM('F0'));\n"
        + "#55=(SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('S0'));\n"
        + "#56=(BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BC0'));\n"
        + "#57=(BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BS0'));\n"
        + "#60=PROPERTY_DEFINITION('PD_VP','',#19);\n"
        + "#61=PROPERTY_DEFINITION('PD_OP','',#25);\n"
        + "#62=PROPERTY_DEFINITION('PD_EL','',#26);\n"
        + "#63=PROPERTY_DEFINITION('PD_CES','',#28);\n"
        + "#64=PROPERTY_DEFINITION('PD_EBWM','',#29);\n"
        + "#65=PROPERTY_DEFINITION('PD_WS','',#30);\n"
        + "#66=PROPERTY_DEFINITION('PD_SBWM','',#33);\n"
        + "#67=PROPERTY_DEFINITION('PD_PS','',#34);\n"
        + "#68=PROPERTY_DEFINITION('PD_GCS','',#35);\n"
        + "#69=PROPERTY_DEFINITION('PD_GS','',#36);\n"
        + "#70=PROPERTY_DEFINITION('PD_AF','',#41);\n"
        + "#71=PROPERTY_DEFINITION('PD_FS','',#43);\n"
        + "#72=PROPERTY_DEFINITION('PD_OS','',#44);\n"
        + "#73=PROPERTY_DEFINITION('PD_SOS','',#45);\n"
        + "#74=PROPERTY_DEFINITION('PD_OOS','',#46);\n"
        + "#75=PROPERTY_DEFINITION('PD_CS','',#47);\n"
        + "#76=PROPERTY_DEFINITION('PD_OCS','',#48);\n"
        + "#77=PROPERTY_DEFINITION('PD_CFS','',#49);\n"
        + "#78=PROPERTY_DEFINITION('PD_CFSS','',#50);\n"
        + "#79=PROPERTY_DEFINITION('PD_FBSM','',#51);\n"
        + "#80=PROPERTY_DEFINITION('PD_SBSM','',#52);\n"
        + "#81=PROPERTY_DEFINITION_REPRESENTATION(#60,#10);\n"
        + "#82=PROPERTY_DEFINITION_REPRESENTATION(#61,#10);\n"
        + "#83=PROPERTY_DEFINITION_REPRESENTATION(#62,#10);\n"
        + "#84=PROPERTY_DEFINITION_REPRESENTATION(#63,#10);\n"
        + "#85=PROPERTY_DEFINITION_REPRESENTATION(#64,#10);\n"
        + "#86=PROPERTY_DEFINITION_REPRESENTATION(#65,#10);\n"
        + "#87=PROPERTY_DEFINITION_REPRESENTATION(#66,#10);\n"
        + "#88=PROPERTY_DEFINITION_REPRESENTATION(#67,#10);\n"
        + "#89=PROPERTY_DEFINITION_REPRESENTATION(#68,#10);\n"
        + "#90=PROPERTY_DEFINITION_REPRESENTATION(#69,#10);\n"
        + "#91=PROPERTY_DEFINITION_REPRESENTATION(#70,#10);\n"
        + "#92=PROPERTY_DEFINITION_REPRESENTATION(#71,#10);\n"
        + "#93=PROPERTY_DEFINITION_REPRESENTATION(#72,#10);\n"
        + "#94=PROPERTY_DEFINITION_REPRESENTATION(#73,#10);\n"
        + "#95=PROPERTY_DEFINITION_REPRESENTATION(#74,#10);\n"
        + "#96=PROPERTY_DEFINITION_REPRESENTATION(#75,#10);\n"
        + "#97=PROPERTY_DEFINITION_REPRESENTATION(#76,#10);\n"
        + "#98=PROPERTY_DEFINITION_REPRESENTATION(#77,#10);\n"
        + "#99=PROPERTY_DEFINITION_REPRESENTATION(#78,#10);\n"
        + "#100=PROPERTY_DEFINITION_REPRESENTATION(#79,#10);\n"
        + "#101=PROPERTY_DEFINITION_REPRESENTATION(#80,#10);\n"
        + "#141=PROPERTY_DEFINITION('PD_SP','',#53);\n"
        + "#142=PROPERTY_DEFINITION('PD_F','',#54);\n"
        + "#143=PROPERTY_DEFINITION('PD_S','',#55);\n"
        + "#144=PROPERTY_DEFINITION('PD_BC','',#56);\n"
        + "#145=PROPERTY_DEFINITION('PD_BS','',#57);\n"
        + "#146=PROPERTY_DEFINITION_REPRESENTATION(#141,#10);\n"
        + "#147=PROPERTY_DEFINITION_REPRESENTATION(#142,#10);\n"
        + "#148=PROPERTY_DEFINITION_REPRESENTATION(#143,#10);\n"
        + "#149=PROPERTY_DEFINITION_REPRESENTATION(#144,#10);\n"
        + "#150=PROPERTY_DEFINITION_REPRESENTATION(#145,#10);\n"
        + "#102=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);\n"
        + "#103=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#104=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#105=DIRECTION('TZ',(0.0,0.0,1.0));\n"
        + "#106=DIRECTION('TX',(1.0,0.0,0.0));\n"
        + "#107=AXIS2_PLACEMENT_3D('AX0',#103,#105,#106);\n"
        + "#108=AXIS2_PLACEMENT_3D('AX1',#104,#105,#106);\n"
        + "#109=ITEM_DEFINED_TRANSFORMATION('T1','',#107,#108);\n"
        + "#110=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#109));\n"
        + "#111=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);\n"
        + "#112=CARTESIAN_POINT('NOTE_P',(0.0,0.0,0.0));\n"
        + "#113=ANNOTATION_TEXT_OCCURRENCE('NOTE_TOPO','direct topology link',#112);\n"
        + "#120=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#19,#10,#113,#8);\n"
        + "#121=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#25,#10,#113,#8);\n"
        + "#122=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#26,#10,#113,#8);\n"
        + "#123=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#28,#10,#113,#8);\n"
        + "#124=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#29,#10,#113,#8);\n"
        + "#125=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#30,#10,#113,#8);\n"
        + "#126=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#33,#10,#113,#8);\n"
        + "#127=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#34,#10,#113,#8);\n"
        + "#128=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#35,#10,#113,#8);\n"
        + "#129=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#36,#10,#113,#8);\n"
        + "#130=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#41,#10,#113,#8);\n"
        + "#131=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#43,#10,#113,#8);\n"
        + "#132=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#44,#10,#113,#8);\n"
        + "#133=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#45,#10,#113,#8);\n"
        + "#134=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#46,#10,#113,#8);\n"
        + "#135=PMI_REQUIREMENT_ITEM_ASSOCIATION('A15','',#47,#10,#113,#8);\n"
        + "#136=PMI_REQUIREMENT_ITEM_ASSOCIATION('A16','',#48,#10,#113,#8);\n"
        + "#137=PMI_REQUIREMENT_ITEM_ASSOCIATION('A17','',#49,#10,#113,#8);\n"
        + "#138=PMI_REQUIREMENT_ITEM_ASSOCIATION('A18','',#50,#10,#113,#8);\n"
        + "#139=PMI_REQUIREMENT_ITEM_ASSOCIATION('A19','',#51,#10,#113,#8);\n"
        + "#140=PMI_REQUIREMENT_ITEM_ASSOCIATION('A20','',#52,#10,#113,#8);\n"
        + "#151=PMI_REQUIREMENT_ITEM_ASSOCIATION('A21','',#53,#10,#113,#8);\n"
        + "#152=PMI_REQUIREMENT_ITEM_ASSOCIATION('A22','',#54,#10,#113,#8);\n"
        + "#153=PMI_REQUIREMENT_ITEM_ASSOCIATION('A23','',#55,#10,#113,#8);\n"
        + "#154=PMI_REQUIREMENT_ITEM_ASSOCIATION('A24','',#56,#10,#113,#8);\n"
        + "#155=PMI_REQUIREMENT_ITEM_ASSOCIATION('A25','',#57,#10,#113,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_TOPO\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_B\"",
                "\"viaDefinitionType\":\"VERTEX_POINT\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"ORIENTED_PATH\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"EDGE_LOOP\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"CONNECTED_EDGE_SET\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"EDGE_BASED_WIREFRAME_MODEL\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"WIRE_SHELL\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"SHELL_BASED_WIREFRAME_MODEL\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"POINT_SET\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"GEOMETRIC_CURVE_SET\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"GEOMETRIC_SET\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"ADVANCED_FACE\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionType\":\"FACE_SURFACE\"",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionType\":\"OPEN_SHELL\"",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionType\":\"SURFACED_OPEN_SHELL\"",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionType\":\"ORIENTED_OPEN_SHELL\"",
                "\"viaDefinitionId\":46",
                "\"viaDefinitionType\":\"CLOSED_SHELL\"",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionType\":\"ORIENTED_CLOSED_SHELL\"",
                "\"viaDefinitionId\":48",
                "\"viaDefinitionType\":\"CONNECTED_FACE_SET\"",
                "\"viaDefinitionId\":49",
                "\"viaDefinitionType\":\"CONNECTED_FACE_SUB_SET\"",
                "\"viaDefinitionId\":50",
                "\"viaDefinitionType\":\"FACE_BASED_SURFACE_MODEL\"",
                "\"viaDefinitionId\":51",
                "\"viaDefinitionType\":\"SHELL_BASED_SURFACE_MODEL\"",
                "\"viaDefinitionId\":52",
                "\"viaDefinitionType\":\"SUBPATH\"",
                "\"viaDefinitionId\":53",
                "\"viaDefinitionType\":\"FACE\"",
                "\"viaDefinitionId\":54",
                "\"viaDefinitionType\":\"SURFACE\"",
                "\"viaDefinitionId\":55",
                "\"viaDefinitionType\":\"BOUNDED_CURVE\"",
                "\"viaDefinitionId\":56",
                "\"viaDefinitionType\":\"BOUNDED_SURFACE\"",
                "\"viaDefinitionId\":57",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":60",
                "\"viaDefinitionId\":80",
                "\"viaDefinitionId\":141",
                "\"viaDefinitionId\":145",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":81",
                "\"viaDefinitionId\":101",
                "\"viaDefinitionId\":146",
                "\"viaDefinitionId\":150",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":102",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":110",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":109",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":107",
                "\"viaDefinitionId\":108",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":103",
                "\"viaDefinitionId\":104",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":105",
                "\"viaDefinitionId\":106",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":111");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_TOPO\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_B\"",
                "\"viaDefinitionType\":\"VERTEX_POINT\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"ORIENTED_PATH\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"EDGE_LOOP\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"CONNECTED_EDGE_SET\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"EDGE_BASED_WIREFRAME_MODEL\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"WIRE_SHELL\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"SHELL_BASED_WIREFRAME_MODEL\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"POINT_SET\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"GEOMETRIC_CURVE_SET\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"GEOMETRIC_SET\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"ADVANCED_FACE\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionType\":\"FACE_SURFACE\"",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionType\":\"OPEN_SHELL\"",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionType\":\"SURFACED_OPEN_SHELL\"",
                "\"viaDefinitionId\":45",
                "\"viaDefinitionType\":\"ORIENTED_OPEN_SHELL\"",
                "\"viaDefinitionId\":46",
                "\"viaDefinitionType\":\"CLOSED_SHELL\"",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionType\":\"ORIENTED_CLOSED_SHELL\"",
                "\"viaDefinitionId\":48",
                "\"viaDefinitionType\":\"CONNECTED_FACE_SET\"",
                "\"viaDefinitionId\":49",
                "\"viaDefinitionType\":\"CONNECTED_FACE_SUB_SET\"",
                "\"viaDefinitionId\":50",
                "\"viaDefinitionType\":\"FACE_BASED_SURFACE_MODEL\"",
                "\"viaDefinitionId\":51",
                "\"viaDefinitionType\":\"SHELL_BASED_SURFACE_MODEL\"",
                "\"viaDefinitionId\":52",
                "\"viaDefinitionType\":\"SUBPATH\"",
                "\"viaDefinitionId\":53",
                "\"viaDefinitionType\":\"FACE\"",
                "\"viaDefinitionId\":54",
                "\"viaDefinitionType\":\"SURFACE\"",
                "\"viaDefinitionId\":55",
                "\"viaDefinitionType\":\"BOUNDED_CURVE\"",
                "\"viaDefinitionId\":56",
                "\"viaDefinitionType\":\"BOUNDED_SURFACE\"",
                "\"viaDefinitionId\":57",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":60",
                "\"viaDefinitionId\":80",
                "\"viaDefinitionId\":141",
                "\"viaDefinitionId\":145",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":81",
                "\"viaDefinitionId\":101",
                "\"viaDefinitionId\":146",
                "\"viaDefinitionId\":150",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":102",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":110",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":109",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":107",
                "\"viaDefinitionId\":108",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":103",
                "\"viaDefinitionId\":104",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":105",
                "\"viaDefinitionId\":106",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":111");
    }

    @Test
    void shouldEmbedDirectSolidAndProfileLinkDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=SHAPE_REPRESENTATION('REP_A',(),#9);\n"
        + "#11=SHAPE_REPRESENTATION('REP_B',(),#9);\n"
        + "#12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#13=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#14=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#15=AXIS2_PLACEMENT_3D('AX3',#12,#13,#14);\n"
        + "#16=AXIS2_PLACEMENT_2D('AX2',#12,#14);\n"
        + "#17=BLOCK('BLK0',#15,1.0,2.0,3.0);\n"
        + "#18=CLOSED_SHELL('CS0',());\n"
        + "#19=MANIFOLD_SOLID_BREP('MSB0',#18);\n"
        + "#20=BREP_WITH_VOIDS('BV0',#18,());\n"
        + "#21=RECTANGLE_PROFILE_DEF(.AREA.,'RPD',#16,1.0,2.0);\n"
        + "#22=EXTRUDED_AREA_SOLID('EAS0',#21,#15,#13,4.0);\n"
        + "#23=AXIS1_PLACEMENT('AX1',#12,#13);\n"
        + "#24=REVOLVED_AREA_SOLID('RAS0',#21,#15,#23,1.57079632679);\n"
        + "#25=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T3',#14,#13,#12,1.0,#14);\n"
        + "#26=SOLID_REPLICA('SR0',#19,#25);\n"
        + "#27=HALF_SPACE_SOLID('HS0',#28,.F.);\n"
        + "#28=PLANE('PL0',#15);\n"
        + "#29=CSG_SOLID('CSG0',#17);\n"
        + "#30=(BOOLEAN_RESULT(.UNION.,#19,#29) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BR0'));\n"
        + "#31=(BOOLEAN_CLIPPING_RESULT(.DIFFERENCE.,#29,#19) BOOLEAN_RESULT(.DIFFERENCE.,#29,#19) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BCR0'));\n"
        + "#130=SPHERE('SP0',#15,2.0);\n"
        + "#131=RIGHT_CIRCULAR_CYLINDER('RCY0',#23,5.0,2.0);\n"
        + "#132=TORUS('TOR0',#23,5.0,1.0);\n"
        + "#133=RIGHT_ANGULAR_WEDGE('WED0',#15,4.0,3.0,2.0,2.5);\n"
        + "#134=CIRCLE_PROFILE_DEF(.AREA.,'C',#16,2.0);\n"
        + "#135=CENTERED_RECTANGLE_PROFILE_DEF(.AREA.,'CR',#16,3.0,5.0);\n"
        + "#136=ELLIPSE_PROFILE_DEF(.AREA.,'E',#16,3.0,1.5);\n"
        + "#137=ROUNDED_RECTANGLE_PROFILE_DEF(.AREA.,'RR',#16,6.0,4.0,0.5);\n"
        + "#138=CIRCULAR_HOLLOW_PROFILE_DEF(.AREA.,'CH',#16,3.0,0.5);\n"
        + "#139=POLYLINE('PLC',(#12,#12,#12,#12));\n"
        + "#140=POLYLINE('PLO',(#12,#12,#12));\n"
        + "#141=ARBITRARY_CLOSED_PROFILE_DEF(.AREA.,'ACP',#139);\n"
        + "#142=ARBITRARY_PROFILE_DEF(.AREA.,'AP',#139);\n"
        + "#143=ARBITRARY_OPEN_PROFILE_DEF(.CURVE.,'AOP',#140);\n"
        + "#230=PROPERTY_DEFINITION('PD_BLK','',#17);\n"
        + "#231=PROPERTY_DEFINITION('PD_MSB','',#19);\n"
        + "#232=PROPERTY_DEFINITION('PD_BV','',#20);\n"
        + "#233=PROPERTY_DEFINITION('PD_RPD','',#21);\n"
        + "#234=PROPERTY_DEFINITION('PD_EAS','',#22);\n"
        + "#235=PROPERTY_DEFINITION('PD_RAS','',#24);\n"
        + "#236=PROPERTY_DEFINITION('PD_SR','',#26);\n"
        + "#237=PROPERTY_DEFINITION('PD_HS','',#27);\n"
        + "#238=PROPERTY_DEFINITION('PD_CSG','',#29);\n"
        + "#239=PROPERTY_DEFINITION('PD_BR','',#30);\n"
        + "#240=PROPERTY_DEFINITION('PD_BCR','',#31);\n"
        + "#145=PROPERTY_DEFINITION('PD_SPH','',#130);\n"
        + "#146=PROPERTY_DEFINITION('PD_RCY','',#131);\n"
        + "#147=PROPERTY_DEFINITION('PD_TOR','',#132);\n"
        + "#148=PROPERTY_DEFINITION('PD_WED','',#133);\n"
        + "#149=PROPERTY_DEFINITION('PD_C','',#134);\n"
        + "#150=PROPERTY_DEFINITION('PD_CR','',#135);\n"
        + "#151=PROPERTY_DEFINITION('PD_E','',#136);\n"
        + "#152=PROPERTY_DEFINITION('PD_RR','',#137);\n"
        + "#153=PROPERTY_DEFINITION('PD_CH','',#138);\n"
        + "#154=PROPERTY_DEFINITION('PD_ACP','',#141);\n"
        + "#155=PROPERTY_DEFINITION('PD_AP','',#142);\n"
        + "#156=PROPERTY_DEFINITION('PD_AOP','',#143);\n"
        + "#241=PROPERTY_DEFINITION_REPRESENTATION(#230,#10);\n"
        + "#242=PROPERTY_DEFINITION_REPRESENTATION(#231,#10);\n"
        + "#243=PROPERTY_DEFINITION_REPRESENTATION(#232,#10);\n"
        + "#244=PROPERTY_DEFINITION_REPRESENTATION(#233,#10);\n"
        + "#245=PROPERTY_DEFINITION_REPRESENTATION(#234,#10);\n"
        + "#246=PROPERTY_DEFINITION_REPRESENTATION(#235,#10);\n"
        + "#247=PROPERTY_DEFINITION_REPRESENTATION(#236,#10);\n"
        + "#248=PROPERTY_DEFINITION_REPRESENTATION(#237,#10);\n"
        + "#249=PROPERTY_DEFINITION_REPRESENTATION(#238,#10);\n"
        + "#250=PROPERTY_DEFINITION_REPRESENTATION(#239,#10);\n"
        + "#251=PROPERTY_DEFINITION_REPRESENTATION(#240,#10);\n"
        + "#157=PROPERTY_DEFINITION_REPRESENTATION(#145,#10);\n"
        + "#158=PROPERTY_DEFINITION_REPRESENTATION(#146,#10);\n"
        + "#159=PROPERTY_DEFINITION_REPRESENTATION(#147,#10);\n"
        + "#160=PROPERTY_DEFINITION_REPRESENTATION(#148,#10);\n"
        + "#161=PROPERTY_DEFINITION_REPRESENTATION(#149,#10);\n"
        + "#162=PROPERTY_DEFINITION_REPRESENTATION(#150,#10);\n"
        + "#163=PROPERTY_DEFINITION_REPRESENTATION(#151,#10);\n"
        + "#164=PROPERTY_DEFINITION_REPRESENTATION(#152,#10);\n"
        + "#165=PROPERTY_DEFINITION_REPRESENTATION(#153,#10);\n"
        + "#166=PROPERTY_DEFINITION_REPRESENTATION(#154,#10);\n"
        + "#167=PROPERTY_DEFINITION_REPRESENTATION(#155,#10);\n"
        + "#168=PROPERTY_DEFINITION_REPRESENTATION(#156,#10);\n"
        + "#51=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);\n"
        + "#52=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#53=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#54=DIRECTION('TZ',(0.0,0.0,1.0));\n"
        + "#55=DIRECTION('TX',(1.0,0.0,0.0));\n"
        + "#56=AXIS2_PLACEMENT_3D('AX0',#52,#54,#55);\n"
        + "#57=AXIS2_PLACEMENT_3D('AX1',#53,#54,#55);\n"
        + "#58=ITEM_DEFINED_TRANSFORMATION('T1','',#56,#57);\n"
        + "#59=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#58));\n"
        + "#60=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);\n"
        + "#61=ANNOTATION_TEXT_OCCURRENCE('NOTE_SOLID','',#12);\n"
        + "#62=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#17,#10,#61,#8);\n"
        + "#63=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#19,#10,#61,#8);\n"
        + "#64=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#20,#10,#61,#8);\n"
        + "#65=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#21,#10,#61,#8);\n"
        + "#66=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#22,#10,#61,#8);\n"
        + "#67=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#24,#10,#61,#8);\n"
        + "#68=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#26,#10,#61,#8);\n"
        + "#69=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#27,#10,#61,#8);\n"
        + "#70=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#29,#10,#61,#8);\n"
        + "#71=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#30,#10,#61,#8);\n"
        + "#169=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#130,#10,#61,#8);\n"
        + "#170=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#131,#10,#61,#8);\n"
        + "#171=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#132,#10,#61,#8);\n"
        + "#172=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#133,#10,#61,#8);\n"
        + "#173=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#134,#10,#61,#8);\n"
        + "#174=PMI_REQUIREMENT_ITEM_ASSOCIATION('A15','',#135,#10,#61,#8);\n"
        + "#175=PMI_REQUIREMENT_ITEM_ASSOCIATION('A16','',#136,#10,#61,#8);\n"
        + "#176=PMI_REQUIREMENT_ITEM_ASSOCIATION('A17','',#137,#10,#61,#8);\n"
        + "#177=PMI_REQUIREMENT_ITEM_ASSOCIATION('A18','',#138,#10,#61,#8);\n"
        + "#178=PMI_REQUIREMENT_ITEM_ASSOCIATION('A19','',#141,#10,#61,#8);\n"
        + "#179=PMI_REQUIREMENT_ITEM_ASSOCIATION('A20','',#142,#10,#61,#8);\n"
        + "#180=PMI_REQUIREMENT_ITEM_ASSOCIATION('A21','',#143,#10,#61,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_SOLID\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_B\"",
                "\"viaDefinitionType\":\"BLOCK\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"MANIFOLD_SOLID_BREP\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"BREP_WITH_VOIDS\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"RECTANGLE_PROFILE_DEF\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"EXTRUDED_AREA_SOLID\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"REVOLVED_AREA_SOLID\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"SOLID_REPLICA\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"HALF_SPACE_SOLID\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"CSG_SOLID\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"BOOLEAN_RESULT\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"SPHERE\"",
                "\"viaDefinitionId\":130",
                "\"viaDefinitionType\":\"RIGHT_CIRCULAR_CYLINDER\"",
                "\"viaDefinitionId\":131",
                "\"viaDefinitionType\":\"TORUS\"",
                "\"viaDefinitionId\":132",
                "\"viaDefinitionType\":\"RIGHT_ANGULAR_WEDGE\"",
                "\"viaDefinitionId\":133",
                "\"viaDefinitionType\":\"CIRCLE_PROFILE_DEF\"",
                "\"viaDefinitionId\":134",
                "\"viaDefinitionType\":\"CENTERED_RECTANGLE_PROFILE_DEF\"",
                "\"viaDefinitionId\":135",
                "\"viaDefinitionType\":\"ELLIPSE_PROFILE_DEF\"",
                "\"viaDefinitionId\":136",
                "\"viaDefinitionType\":\"ROUNDED_RECTANGLE_PROFILE_DEF\"",
                "\"viaDefinitionId\":137",
                "\"viaDefinitionType\":\"CIRCULAR_HOLLOW_PROFILE_DEF\"",
                "\"viaDefinitionId\":138",
                "\"viaDefinitionType\":\"ARBITRARY_CLOSED_PROFILE_DEF\"",
                "\"viaDefinitionId\":141",
                "\"viaDefinitionType\":\"ARBITRARY_PROFILE_DEF\"",
                "\"viaDefinitionId\":142",
                "\"viaDefinitionType\":\"ARBITRARY_OPEN_PROFILE_DEF\"",
                "\"viaDefinitionId\":143",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":230",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":241");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_SOLID\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_B\"",
                "\"viaDefinitionType\":\"BLOCK\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"MANIFOLD_SOLID_BREP\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"BREP_WITH_VOIDS\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"RECTANGLE_PROFILE_DEF\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"EXTRUDED_AREA_SOLID\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"REVOLVED_AREA_SOLID\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"SOLID_REPLICA\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"HALF_SPACE_SOLID\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"CSG_SOLID\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"BOOLEAN_RESULT\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"SPHERE\"",
                "\"viaDefinitionId\":130",
                "\"viaDefinitionType\":\"RIGHT_CIRCULAR_CYLINDER\"",
                "\"viaDefinitionId\":131",
                "\"viaDefinitionType\":\"TORUS\"",
                "\"viaDefinitionId\":132",
                "\"viaDefinitionType\":\"RIGHT_ANGULAR_WEDGE\"",
                "\"viaDefinitionId\":133",
                "\"viaDefinitionType\":\"CIRCLE_PROFILE_DEF\"",
                "\"viaDefinitionId\":134",
                "\"viaDefinitionType\":\"CENTERED_RECTANGLE_PROFILE_DEF\"",
                "\"viaDefinitionId\":135",
                "\"viaDefinitionType\":\"ELLIPSE_PROFILE_DEF\"",
                "\"viaDefinitionId\":136",
                "\"viaDefinitionType\":\"ROUNDED_RECTANGLE_PROFILE_DEF\"",
                "\"viaDefinitionId\":137",
                "\"viaDefinitionType\":\"CIRCULAR_HOLLOW_PROFILE_DEF\"",
                "\"viaDefinitionId\":138",
                "\"viaDefinitionType\":\"ARBITRARY_CLOSED_PROFILE_DEF\"",
                "\"viaDefinitionId\":141",
                "\"viaDefinitionType\":\"ARBITRARY_PROFILE_DEF\"",
                "\"viaDefinitionId\":142",
                "\"viaDefinitionType\":\"ARBITRARY_OPEN_PROFILE_DEF\"",
                "\"viaDefinitionId\":143",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":230",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":241");
    }

    @Test
    void shouldEmbedAnnotationMapDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_BASE',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=CARTESIAN_POINT('M0',(0.0,0.0));\n"
        + "#13=DIRECTION('DX0',(1.0,0.0));\n"
        + "#14=AXIS2_PLACEMENT_2D('MAP0',#12,#13);\n"
        + "#15=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYM'));\n"
        + "#16=REPRESENTATION('REP_SYM',(),#15);\n"
        + "#17=SYMBOL_REPRESENTATION_MAP(#14,#16);\n"
        + "#18=CARTESIAN_POINT('T0',(3.0,4.0));\n"
        + "#19=AXIS2_PLACEMENT_2D('TGT0',#18,#13);\n"
        + "#20=ANNOTATION_SYMBOL('AS0',#17,#19);\n"
        + "#21=PRESENTATION_STYLE_ASSIGNMENT(());\n"
        + "#22=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#21),#20);\n"
        + "#23=CARTESIAN_POINT('M1',(0.0,0.0));\n"
        + "#24=AXIS2_PLACEMENT_2D('MAP1',#23,#13);\n"
        + "#25=REPRESENTATION('REP_TXT',(),#15);\n"
        + "#26=REPRESENTATION_MAP(#24,#25);\n"
        + "#27=CARTESIAN_POINT('T1',(6.0,7.0));\n"
        + "#28=AXIS2_PLACEMENT_2D('TGT1',#27,#13);\n"
        + "#29=ANNOTATION_TEXT('AT0',#26,#28);\n"
        + "#30=ANNOTATION_TEXT_CHARACTER('ATC0',#26,#28);\n"
        + "#31=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#32=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#33=ANNOTATION_TEXT_OCCURRENCE('NOTE_A','base',#31);\n"
        + "#34=ANNOTATION_TEXT_OCCURRENCE('NOTE_B','child',#32);\n"
        + "#35=DRAUGHTING_CALLOUT('CALLOUT_A',(#33,#22));\n"
        + "#36=DRAUGHTING_CALLOUT('CALLOUT_B',(#34,#22));\n"
        + "#37=DRAUGHTING_CALLOUT_RELATIONSHIP('REL','carry',#35,#36);\n"
        + "#38=ANNOTATION_OCCURRENCE_RELATIONSHIP('AOR','link',#22,#34);\n"
        + "#39=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#17,#10,#33,#8);\n"
        + "#40=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#20,#10,#34,#8);\n"
        + "#41=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#26,#10,#33,#8);\n"
        + "#42=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#29,#10,#34,#8);\n"
        + "#43=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#30,#10,#33,#8);\n"
        + "#44=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#35,#10,#34,#8);\n"
        + "#45=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#37,#10,#33,#8);\n"
        + "#46=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#38,#10,#34,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"DRAUGHTING_CALLOUT_RELATIONSHIP\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"ANNOTATION_OCCURRENCE_RELATIONSHIP\"",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"ANNOTATION_SYMBOL\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"SYMBOL_REPRESENTATION_MAP\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_2D\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"REPRESENTATION\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"ANNOTATION_TEXT\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"ANNOTATION_TEXT_CHARACTER\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"DRAUGHTING_CALLOUT\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"REPRESENTATION_MAP\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionId\":28");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"DRAUGHTING_CALLOUT_RELATIONSHIP\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"ANNOTATION_OCCURRENCE_RELATIONSHIP\"",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"ANNOTATION_SYMBOL\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"SYMBOL_REPRESENTATION_MAP\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_2D\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"REPRESENTATION\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"ANNOTATION_TEXT\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"ANNOTATION_TEXT_CHARACTER\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"DRAUGHTING_CALLOUT\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"REPRESENTATION_MAP\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionId\":28");
    }

    @Test
    void shouldEmbedAnnotationWrapperDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));\n"
        + "#10=REPRESENTATION('REP_ANNOTATION_OFFSET',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#13=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#15=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#16=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#17=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#18=VECTOR('VX',#15,1.0);\n"
        + "#19=AXIS2_PLACEMENT_3D('AX3',#12,#17,#15);\n"
        + "#20=LINE('L0',#12,#18);\n"
        + "#21=OFFSET_CURVE_3D('OC3',#20,0.5,.F.,#16);\n"
        + "#22=ORIENTED_CURVE('ORC0',#21,.F.);\n"
        + "#23=B_SPLINE_CURVE_WITH_KNOTS('BC0',1,(#12,#13),.UNSPECIFIED.,.F.,.F.,(2,2),(0.0,1.0),.PIECEWISE_BEZIER_KNOTS.);\n"
        + "#24=(B_SPLINE_SURFACE(1,1,((#12,#13),(#14,#12)),.UNSPECIFIED.,.F.,.F.,.F.)\n"
        + "     RATIONAL_B_SPLINE_SURFACE(((1.0,1.0),(1.0,1.0))));\n"
        + "#25=POLYLINE('PL0',(#12,#13,#14));\n"
        + "#26=PRESENTATION_STYLE_ASSIGNMENT(());\n"
        + "#27=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#26),#22);\n"
        + "#28=ANNOTATION_FILL_AREA('AFA0',(#25));\n"
        + "#29=ANNOTATION_FILL_AREA_OCCURRENCE('AFAO0',(#26),#28,#12);\n"
        + "#30=GEOMETRIC_SET('GS0',(#12,#13));\n"
        + "#31=ANNOTATION_PLACEHOLDER_OCCURRENCE('APO0',(#26),#30,.END.,1.0);\n"
        + "#32=(ANNOTATION_POINT_OCCURRENCE('AP0',(#26),#12) DRAUGHTING_ANNOTATION_OCCURRENCE('AP0',(#26),#12));\n"
        + "#33=ANNOTATION_TEXT_OCCURRENCE('AT0','note',#13);\n"
        + "#34=ANNOTATION_CURVE_OCCURRENCE('LC0',(#26),#20);\n"
        + "#35=DIMENSION_CURVE('DC0',(#26),#20);\n"
        + "#36=LEADER_CURVE('LD0',(#26),#20);\n"
        + "#37=PROJECTION_CURVE('PC0',(#26),#20);\n"
        + "#38=SYMBOL_REPRESENTATION_MAP(#19,#10);\n"
        + "#39=ANNOTATION_SYMBOL('AS0',#38,#19);\n"
        + "#40=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#26),#39);\n"
        + "#41=ANNOTATION_SUBFIGURE_OCCURRENCE('SUB0',(#26),#39);\n"
        + "#42=TERMINATOR_SYMBOL('TS0',(#26),#39,#34);\n"
        + "#60=PROPERTY_DEFINITION('PD_OC3','',#21);\n"
        + "#61=PROPERTY_DEFINITION('PD_ORC','',#22);\n"
        + "#62=PROPERTY_DEFINITION('PD_BSC','',#23);\n"
        + "#63=PROPERTY_DEFINITION('PD_RBS','',#24);\n"
        + "#64=PROPERTY_DEFINITION('PD_ACO','',#27);\n"
        + "#65=PROPERTY_DEFINITION('PD_AFA','',#28);\n"
        + "#66=PROPERTY_DEFINITION('PD_AFAO','',#29);\n"
        + "#67=PROPERTY_DEFINITION('PD_APO','',#31);\n"
        + "#68=PROPERTY_DEFINITION('PD_AP','',#32);\n"
        + "#69=PROPERTY_DEFINITION('PD_AT','',#33);\n"
        + "#70=PROPERTY_DEFINITION('PD_DC','',#35);\n"
        + "#71=PROPERTY_DEFINITION('PD_LD','',#36);\n"
        + "#72=PROPERTY_DEFINITION('PD_PC','',#37);\n"
        + "#73=PROPERTY_DEFINITION('PD_ASO','',#40);\n"
        + "#74=PROPERTY_DEFINITION('PD_SUB','',#41);\n"
        + "#75=PROPERTY_DEFINITION('PD_TS','',#42);\n"
        + "#80=PROPERTY_DEFINITION_REPRESENTATION(#60,#10);\n"
        + "#81=PROPERTY_DEFINITION_REPRESENTATION(#61,#10);\n"
        + "#82=PROPERTY_DEFINITION_REPRESENTATION(#62,#10);\n"
        + "#83=PROPERTY_DEFINITION_REPRESENTATION(#63,#10);\n"
        + "#84=PROPERTY_DEFINITION_REPRESENTATION(#64,#10);\n"
        + "#85=PROPERTY_DEFINITION_REPRESENTATION(#65,#10);\n"
        + "#86=PROPERTY_DEFINITION_REPRESENTATION(#66,#10);\n"
        + "#87=PROPERTY_DEFINITION_REPRESENTATION(#67,#10);\n"
        + "#88=PROPERTY_DEFINITION_REPRESENTATION(#68,#10);\n"
        + "#89=PROPERTY_DEFINITION_REPRESENTATION(#69,#10);\n"
        + "#90=PROPERTY_DEFINITION_REPRESENTATION(#70,#10);\n"
        + "#91=PROPERTY_DEFINITION_REPRESENTATION(#71,#10);\n"
        + "#92=PROPERTY_DEFINITION_REPRESENTATION(#72,#10);\n"
        + "#93=PROPERTY_DEFINITION_REPRESENTATION(#73,#10);\n"
        + "#94=PROPERTY_DEFINITION_REPRESENTATION(#74,#10);\n"
        + "#95=PROPERTY_DEFINITION_REPRESENTATION(#75,#10);\n"
        + "#100=CARTESIAN_POINT('N0',(0.0,0.0,0.0));\n"
        + "#101=CARTESIAN_POINT('N1',(1.0,0.0,0.0));\n"
        + "#102=CARTESIAN_POINT('N2',(2.0,0.0,0.0));\n"
        + "#103=CARTESIAN_POINT('N3',(3.0,0.0,0.0));\n"
        + "#104=CARTESIAN_POINT('N4',(4.0,0.0,0.0));\n"
        + "#105=CARTESIAN_POINT('N5',(5.0,0.0,0.0));\n"
        + "#106=CARTESIAN_POINT('N6',(6.0,0.0,0.0));\n"
        + "#107=CARTESIAN_POINT('N7',(7.0,0.0,0.0));\n"
        + "#108=CARTESIAN_POINT('N8',(8.0,0.0,0.0));\n"
        + "#109=CARTESIAN_POINT('N9',(9.0,0.0,0.0));\n"
        + "#110=CARTESIAN_POINT('N10',(10.0,0.0,0.0));\n"
        + "#111=CARTESIAN_POINT('N11',(11.0,0.0,0.0));\n"
        + "#112=CARTESIAN_POINT('N12',(12.0,0.0,0.0));\n"
        + "#113=CARTESIAN_POINT('N13',(13.0,0.0,0.0));\n"
        + "#114=CARTESIAN_POINT('N14',(14.0,0.0,0.0));\n"
        + "#115=CARTESIAN_POINT('N15',(15.0,0.0,0.0));\n"
        + "#116=ANNOTATION_TEXT_OCCURRENCE('NOTE_OC3','',#100);\n"
        + "#117=ANNOTATION_TEXT_OCCURRENCE('NOTE_ORC','',#101);\n"
        + "#118=ANNOTATION_TEXT_OCCURRENCE('NOTE_BSC','',#102);\n"
        + "#119=ANNOTATION_TEXT_OCCURRENCE('NOTE_RBS','',#103);\n"
        + "#120=ANNOTATION_TEXT_OCCURRENCE('NOTE_ACO','',#104);\n"
        + "#121=ANNOTATION_TEXT_OCCURRENCE('NOTE_AFA','',#105);\n"
        + "#122=ANNOTATION_TEXT_OCCURRENCE('NOTE_AFAO','',#106);\n"
        + "#123=ANNOTATION_TEXT_OCCURRENCE('NOTE_APO','',#107);\n"
        + "#124=ANNOTATION_TEXT_OCCURRENCE('NOTE_AP','',#108);\n"
        + "#125=ANNOTATION_TEXT_OCCURRENCE('NOTE_AT','',#109);\n"
        + "#126=ANNOTATION_TEXT_OCCURRENCE('NOTE_DC','',#110);\n"
        + "#127=ANNOTATION_TEXT_OCCURRENCE('NOTE_LD','',#111);\n"
        + "#128=ANNOTATION_TEXT_OCCURRENCE('NOTE_PC','',#112);\n"
        + "#129=ANNOTATION_TEXT_OCCURRENCE('NOTE_ASO','',#113);\n"
        + "#130=ANNOTATION_TEXT_OCCURRENCE('NOTE_SUB','',#114);\n"
        + "#131=ANNOTATION_TEXT_OCCURRENCE('NOTE_TS','',#115);\n"
        + "#140=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#21,#10,#116,#8);\n"
        + "#141=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#22,#10,#117,#8);\n"
        + "#142=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#23,#10,#118,#8);\n"
        + "#143=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#24,#10,#119,#8);\n"
        + "#144=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#27,#10,#120,#8);\n"
        + "#145=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#28,#10,#121,#8);\n"
        + "#146=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#29,#10,#122,#8);\n"
        + "#147=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#31,#10,#123,#8);\n"
        + "#148=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#32,#10,#124,#8);\n"
        + "#149=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#33,#10,#125,#8);\n"
        + "#150=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#35,#10,#126,#8);\n"
        + "#151=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#36,#10,#127,#8);\n"
        + "#152=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#37,#10,#128,#8);\n"
        + "#153=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#40,#10,#129,#8);\n"
        + "#154=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#41,#10,#130,#8);\n"
        + "#155=PMI_REQUIREMENT_ITEM_ASSOCIATION('A15','',#42,#10,#131,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"ANNOTATION_CURVE_OCCURRENCE\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"ANNOTATION_FILL_AREA\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"ANNOTATION_FILL_AREA_OCCURRENCE\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"ANNOTATION_PLACEHOLDER_OCCURRENCE\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"ANNOTATION_POINT_OCCURRENCE\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"ANNOTATION_TEXT_OCCURRENCE\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"ANNOTATION_SYMBOL_OCCURRENCE\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"ANNOTATION_SUBFIGURE_OCCURRENCE\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionType\":\"TERMINATOR_SYMBOL\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"ANNOTATION_SYMBOL\"",
                "\"viaDefinitionId\":39");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"ANNOTATION_CURVE_OCCURRENCE\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"ANNOTATION_FILL_AREA\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"ANNOTATION_FILL_AREA_OCCURRENCE\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"ANNOTATION_PLACEHOLDER_OCCURRENCE\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"ANNOTATION_POINT_OCCURRENCE\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"ANNOTATION_TEXT_OCCURRENCE\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"ANNOTATION_SYMBOL_OCCURRENCE\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"ANNOTATION_SUBFIGURE_OCCURRENCE\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionType\":\"TERMINATOR_SYMBOL\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"ANNOTATION_SYMBOL\"",
                "\"viaDefinitionId\":39");
    }

    @Test
    void shouldEmbedPresentationStyleDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_BASE_STYLE',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=REPRESENTATION('REP_STYLE_LEAF',(),#9);\n"
        + "#13=CARTESIAN_POINT('M0',(0.0,0.0));\n"
        + "#14=DIRECTION('DX0',(1.0,0.0));\n"
        + "#15=AXIS2_PLACEMENT_2D('MAP0',#13,#14);\n"
        + "#16=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYM'));\n"
        + "#17=REPRESENTATION('REP_STYLE_MAP',(),#16);\n"
        + "#18=REPRESENTATION_MAP(#15,#17);\n"
        + "#19=CARTESIAN_POINT('T0',(3.0,4.0));\n"
        + "#20=AXIS2_PLACEMENT_2D('TGT0',#19,#14);\n"
        + "#21=USER_DEFINED_CURVE_FONT('UCF0',#18,#20);\n"
        + "#22=PRE_DEFINED_COLOUR('yellow');\n"
        + "#23=FILL_AREA_STYLE_COLOUR('',#22);\n"
        + "#24=FILL_AREA_STYLE('',(#23));\n"
        + "#25=SURFACE_STYLE_FILL_AREA(#24);\n"
        + "#26=CURVE_STYLE('CS0',#21,0.25,#22);\n"
        + "#27=SURFACE_STYLE_BOUNDARY(#26);\n"
        + "#28=SURFACE_STYLE_PARAMETER_LINE(#26);\n"
        + "#29=SURFACE_STYLE_CONTROL_GRID(#26);\n"
        + "#30=SURFACE_STYLE_SEGMENTATION_CURVE(#26);\n"
        + "#31=SURFACE_STYLE_SILHOUETTE(#26);\n"
        + "#32=SURFACE_SIDE_STYLE('',(#25,#27,#28,#29,#30,#31));\n"
        + "#33=SURFACE_STYLE_USAGE(.BOTH.,#32);\n"
        + "#34=PRESENTATION_STYLE_ASSIGNMENT((#33));\n"
        + "#35=CHARACTER_GLYPH_STYLE_STROKE(#26);\n"
        + "#36=CHARACTER_GLYPH_STYLE_OUTLINE(#26);\n"
        + "#37=CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS(#26,#24);\n"
        + "#38=TEXT_STYLE_FOR_DEFINED_FONT(#22);\n"
        + "#39=TEXT_STYLE('TS0',#38);\n"
        + "#40=TEXT_STYLE_WITH_SPACING('TS1',#38,0.15);\n"
        + "#41=TEXT_STYLE_WITH_BOX_CHARACTERISTICS('TS2',#38,(BOX_HEIGHT(1.2)));\n"
        + "#42=PRE_DEFINED_CURVE_FONT('solid');\n"
        + "#43=CURVE_STYLE('CS1',#42,0.2,#22);\n"
        + "#44=PROPERTY_DEFINITION('PD_CURVE_FONT','',#43);\n"
        + "#45=PROPERTY_DEFINITION_REPRESENTATION(#44,#12);\n"
        + "#46=PRE_DEFINED_MARKER('dot');\n"
        + "#47=POINT_STYLE('PS0',#46,2.5,#22);\n"
        + "#48=PROPERTY_DEFINITION('PD_MARKER','',#47);\n"
        + "#49=PROPERTY_DEFINITION_REPRESENTATION(#48,#12);\n"
        + "#50=TEXT_STYLE_WITH_JUSTIFICATION('TS3',#38,.LEFT.);\n"
        + "#51=PROPERTY_DEFINITION('PD_COLOUR','',#50);\n"
        + "#52=PROPERTY_DEFINITION_REPRESENTATION(#51,#12);\n"
        + "#60=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#61=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#62=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#63=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#64=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#65=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#66=CARTESIAN_POINT('P6',(6.0,0.0,0.0));\n"
        + "#67=CARTESIAN_POINT('P7',(7.0,0.0,0.0));\n"
        + "#68=CARTESIAN_POINT('P8',(8.0,0.0,0.0));\n"
        + "#69=CARTESIAN_POINT('P9',(9.0,0.0,0.0));\n"
        + "#70=CARTESIAN_POINT('P10',(10.0,0.0,0.0));\n"
        + "#71=CARTESIAN_POINT('P11',(11.0,0.0,0.0));\n"
        + "#72=ANNOTATION_TEXT_OCCURRENCE('NOTE_CURVE_STYLE','',#60);\n"
        + "#73=ANNOTATION_TEXT_OCCURRENCE('NOTE_SURFACE_USAGE','',#61);\n"
        + "#74=ANNOTATION_TEXT_OCCURRENCE('NOTE_STYLE_ASSIGNMENT','',#62);\n"
        + "#75=ANNOTATION_TEXT_OCCURRENCE('NOTE_GLYPH_STROKE','',#63);\n"
        + "#76=ANNOTATION_TEXT_OCCURRENCE('NOTE_GLYPH_OUTLINE','',#64);\n"
        + "#77=ANNOTATION_TEXT_OCCURRENCE('NOTE_GLYPH_OUTLINE_FILL','',#65);\n"
        + "#78=ANNOTATION_TEXT_OCCURRENCE('NOTE_TEXT_STYLE','',#66);\n"
        + "#79=ANNOTATION_TEXT_OCCURRENCE('NOTE_TEXT_STYLE_SPACING','',#67);\n"
        + "#80=ANNOTATION_TEXT_OCCURRENCE('NOTE_TEXT_STYLE_BOX','',#68);\n"
        + "#81=ANNOTATION_TEXT_OCCURRENCE('NOTE_LEAF_CURVE_FONT','',#69);\n"
        + "#82=ANNOTATION_TEXT_OCCURRENCE('NOTE_LEAF_MARKER','',#70);\n"
        + "#83=ANNOTATION_TEXT_OCCURRENCE('NOTE_LEAF_COLOUR','',#71);\n"
        + "#84=ANNOTATION_TEXT_OCCURRENCE('NOTE_TEXT_STYLE_JUST','',#71);\n"
        + "#90=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#26,#10,#72,#8);\n"
        + "#91=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#33,#10,#73,#8);\n"
        + "#92=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#34,#10,#74,#8);\n"
        + "#93=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#35,#10,#75,#8);\n"
        + "#94=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#36,#10,#76,#8);\n"
        + "#95=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#37,#10,#77,#8);\n"
        + "#96=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#39,#10,#78,#8);\n"
        + "#97=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#40,#10,#79,#8);\n"
        + "#98=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#41,#10,#80,#8);\n"
        + "#99=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#42,#10,#81,#8);\n"
        + "#100=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#46,#10,#82,#8);\n"
        + "#101=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#22,#10,#83,#8);\n"
        + "#102=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#50,#10,#84,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"CURVE_STYLE\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"SURFACE_STYLE_USAGE\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"PRESENTATION_STYLE_ASSIGNMENT\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"SURFACE_SIDE_STYLE\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"SURFACE_STYLE_FILL_AREA\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"FILL_AREA_STYLE\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"FILL_AREA_STYLE_COLOUR\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"SURFACE_STYLE_BOUNDARY\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"SURFACE_STYLE_PARAMETER_LINE\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"SURFACE_STYLE_CONTROL_GRID\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"SURFACE_STYLE_SEGMENTATION_CURVE\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"SURFACE_STYLE_SILHOUETTE\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"CHARACTER_GLYPH_STYLE_STROKE\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"CHARACTER_GLYPH_STYLE_OUTLINE\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"TEXT_STYLE\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionType\":\"TEXT_STYLE_WITH_SPACING\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"TEXT_STYLE_WITH_BOX_CHARACTERISTICS\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionType\":\"TEXT_STYLE_WITH_JUSTIFICATION\"",
                "\"viaDefinitionId\":50",
                "\"viaDefinitionType\":\"TEXT_STYLE_FOR_DEFINED_FONT\"",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"POINT_STYLE\"",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionType\":\"PRE_DEFINED_COLOUR\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"PRE_DEFINED_CURVE_FONT\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"PRE_DEFINED_MARKER\"",
                "\"viaDefinitionId\":46");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"CURVE_STYLE\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"SURFACE_STYLE_USAGE\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"PRESENTATION_STYLE_ASSIGNMENT\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"SURFACE_SIDE_STYLE\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"SURFACE_STYLE_FILL_AREA\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"FILL_AREA_STYLE\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"FILL_AREA_STYLE_COLOUR\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"SURFACE_STYLE_BOUNDARY\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"SURFACE_STYLE_PARAMETER_LINE\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"SURFACE_STYLE_CONTROL_GRID\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"SURFACE_STYLE_SEGMENTATION_CURVE\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"SURFACE_STYLE_SILHOUETTE\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"CHARACTER_GLYPH_STYLE_STROKE\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"CHARACTER_GLYPH_STYLE_OUTLINE\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"TEXT_STYLE\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionType\":\"TEXT_STYLE_WITH_SPACING\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"TEXT_STYLE_WITH_BOX_CHARACTERISTICS\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionType\":\"TEXT_STYLE_WITH_JUSTIFICATION\"",
                "\"viaDefinitionId\":50",
                "\"viaDefinitionType\":\"TEXT_STYLE_FOR_DEFINED_FONT\"",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"POINT_STYLE\"",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionType\":\"PRE_DEFINED_COLOUR\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"PRE_DEFINED_CURVE_FONT\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"PRE_DEFINED_MARKER\"",
                "\"viaDefinitionId\":46");
    }

    @Test
    void shouldEmbedColourAndSurfaceStyleLeafMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_SURFACE_STYLE_LEAF',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=COLOUR_RGB('Amber',1.0,0.75,0.0);\n"
        + "#13=COLOUR_SPECIFICATION('amber-spec');\n"
        + "#14=COLOUR();\n"
        + "#15=FILL_AREA_STYLE_COLOUR('',#12);\n"
        + "#16=FILL_AREA_STYLE('',(#15));\n"
        + "#17=SURFACE_STYLE_FILL_AREA(#16);\n"
        + "#18=SURFACE_STYLE_TRANSPARENT(0.35);\n"
        + "#19=SURFACE_STYLE_REFLECTANCE_AMBIENT(0.2);\n"
        + "#20=SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE(0.2,0.6);\n"
        + "#21=SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR(0.2,0.6,0.4,32.0,#13);\n"
        + "#22=SURFACE_SIDE_STYLE('',(#17,#18,#19,#20,#21));\n"
        + "#23=SURFACE_STYLE_USAGE(.BOTH.,#22);\n"
        + "#24=PRESENTATION_STYLE_ASSIGNMENT((#23));\n"
        + "#25=PRE_DEFINED_SURFACE_SIDE_STYLE('both');\n"
        + "#26=PROPERTY_DEFINITION('PD_RGB','',#12);\n"
        + "#27=PROPERTY_DEFINITION('PD_SPEC','',#13);\n"
        + "#28=PROPERTY_DEFINITION('PD_COLOUR','',#14);\n"
        + "#29=PROPERTY_DEFINITION('PD_PRE_SIDE','',#25);\n"
        + "#30=PROPERTY_DEFINITION_REPRESENTATION(#26,#10);\n"
        + "#31=PROPERTY_DEFINITION_REPRESENTATION(#27,#10);\n"
        + "#32=PROPERTY_DEFINITION_REPRESENTATION(#28,#10);\n"
        + "#33=PROPERTY_DEFINITION_REPRESENTATION(#29,#10);\n"
        + "#40=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#41=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#42=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#43=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#44=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#45=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#46=CARTESIAN_POINT('P6',(6.0,0.0,0.0));\n"
        + "#47=ANNOTATION_TEXT_OCCURRENCE('NOTE_RGB','',#40);\n"
        + "#48=ANNOTATION_TEXT_OCCURRENCE('NOTE_SPEC','',#41);\n"
        + "#49=ANNOTATION_TEXT_OCCURRENCE('NOTE_COLOUR','',#42);\n"
        + "#50=ANNOTATION_TEXT_OCCURRENCE('NOTE_TRANSPARENT','',#43);\n"
        + "#51=ANNOTATION_TEXT_OCCURRENCE('NOTE_AMBIENT','',#44);\n"
        + "#52=ANNOTATION_TEXT_OCCURRENCE('NOTE_AMBIENT_DIFFUSE','',#45);\n"
        + "#53=ANNOTATION_TEXT_OCCURRENCE('NOTE_SPECULAR','',#46);\n"
        + "#54=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRE_SIDE','',#46);\n"
        + "#55=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#12,#10,#47,#8);\n"
        + "#56=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#13,#10,#48,#8);\n"
        + "#57=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#14,#10,#49,#8);\n"
        + "#58=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#18,#10,#50,#8);\n"
        + "#59=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#19,#10,#51,#8);\n"
        + "#60=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#20,#10,#52,#8);\n"
        + "#61=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#21,#10,#53,#8);\n"
        + "#62=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#25,#10,#54,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"SURFACE_STYLE_TRANSPARENT\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"SURFACE_STYLE_REFLECTANCE_AMBIENT\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"PRE_DEFINED_SURFACE_SIDE_STYLE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"COLOUR_RGB\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"COLOUR_SPECIFICATION\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"COLOUR\"",
                "\"viaDefinitionId\":14");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"SURFACE_STYLE_TRANSPARENT\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"SURFACE_STYLE_REFLECTANCE_AMBIENT\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"PRE_DEFINED_SURFACE_SIDE_STYLE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"COLOUR_RGB\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"COLOUR_SPECIFICATION\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"COLOUR\"",
                "\"viaDefinitionId\":14");
    }

    @Test
    void shouldEmbedStyledAndLayerCarrierMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_STYLE_WRAP',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#13=DIRECTION('N',(0.0,0.0,1.0));\n"
        + "#14=DIRECTION('X',(1.0,0.0,0.0));\n"
        + "#15=AXIS2_PLACEMENT_3D('AX',#12,#13,#14);\n"
        + "#16=PLANE('PL0',#15);\n"
        + "#17=PRE_DEFINED_COLOUR('yellow');\n"
        + "#18=PRE_DEFINED_CURVE_FONT('solid');\n"
        + "#19=CURVE_STYLE('CS0',#18,0.2,#17);\n"
        + "#20=PRESENTATION_STYLE_ASSIGNMENT((#19));\n"
        + "#21=STYLED_ITEM('S0',(#20),#16);\n"
        + "#22=OVER_RIDING_STYLED_ITEM('OS0',(#20),#16,#21);\n"
        + "#23=PRESENTATION_LAYER_ASSIGNMENT('L1','layer one',(#16,#21,#22));\n"
        + "#30=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#31=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#32=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#33=ANNOTATION_TEXT_OCCURRENCE('NOTE_STYLED','',#30);\n"
        + "#34=ANNOTATION_TEXT_OCCURRENCE('NOTE_OVERRIDE','',#31);\n"
        + "#35=ANNOTATION_TEXT_OCCURRENCE('NOTE_LAYER','',#32);\n"
        + "#40=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#21,#10,#33,#8);\n"
        + "#41=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#22,#10,#34,#8);\n"
        + "#42=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#23,#10,#35,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"STYLED_ITEM\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"OVER_RIDING_STYLED_ITEM\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"PRESENTATION_LAYER_ASSIGNMENT\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"PRESENTATION_STYLE_ASSIGNMENT\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"CURVE_STYLE\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"PLANE\"",
                "\"viaDefinitionId\":16");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"STYLED_ITEM\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"OVER_RIDING_STYLED_ITEM\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"PRESENTATION_LAYER_ASSIGNMENT\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"PRESENTATION_STYLE_ASSIGNMENT\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"CURVE_STYLE\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"PLANE\"",
                "\"viaDefinitionId\":16");
    }

    @Test
    void shouldEmbedMappedTransformationAndPlacementCarrierMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_MAP_WRAP',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#13=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#14=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#15=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#16=AXIS1_PLACEMENT('AX1',#12,#13);\n"
        + "#17=AXIS2_PLACEMENT_3D('AX0',#12,#13,#14);\n"
        + "#18=CARTESIAN_POINT('T1',(5.0,0.0,0.0));\n"
        + "#19=AXIS2_PLACEMENT_3D('AX1T',#18,#13,#14);\n"
        + "#20=ITEM_DEFINED_TRANSFORMATION('MOVE','translate x',#17,#19);\n"
        + "#21=CARTESIAN_POINT('ORIG',(2.0,3.0,4.0));\n"
        + "#22=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',#14,#15,#21,1.5,#13);\n"
        + "#23=CARTESIAN_POINT('M0',(0.0,0.0));\n"
        + "#24=DIRECTION('DX2',(1.0,0.0));\n"
        + "#25=AXIS2_PLACEMENT_2D('MAP0',#23,#24);\n"
        + "#26=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','MAP'));\n"
        + "#27=REPRESENTATION('REP_MAPPED',(),#26);\n"
        + "#28=REPRESENTATION_MAP(#25,#27);\n"
        + "#29=MAPPED_ITEM(#28,#22);\n"
        + "#30=POINT_REPLICA('PR0',#12,#22);\n"
        + "#40=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#41=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#42=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#43=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#44=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#45=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#46=ANNOTATION_TEXT_OCCURRENCE('NOTE_MAPPED','',#40);\n"
        + "#47=ANNOTATION_TEXT_OCCURRENCE('NOTE_REPLICA','',#41);\n"
        + "#48=ANNOTATION_TEXT_OCCURRENCE('NOTE_ITEM_TRANSFORM','',#42);\n"
        + "#49=ANNOTATION_TEXT_OCCURRENCE('NOTE_CART_TRANSFORM','',#43);\n"
        + "#50=ANNOTATION_TEXT_OCCURRENCE('NOTE_AXIS1','',#44);\n"
        + "#51=ANNOTATION_TEXT_OCCURRENCE('NOTE_AXIS2','',#45);\n"
        + "#60=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#29,#10,#46,#8);\n"
        + "#61=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#30,#10,#47,#8);\n"
        + "#62=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#20,#10,#48,#8);\n"
        + "#63=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#22,#10,#49,#8);\n"
        + "#64=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#16,#10,#50,#8);\n"
        + "#65=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#17,#10,#51,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"MAPPED_ITEM\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"POINT_REPLICA\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"CARTESIAN_TRANSFORMATION_OPERATOR_3D\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"AXIS1_PLACEMENT\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"REPRESENTATION_MAP\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_2D\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"REPRESENTATION\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionId\":15");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"MAPPED_ITEM\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"POINT_REPLICA\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"CARTESIAN_TRANSFORMATION_OPERATOR_3D\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"AXIS1_PLACEMENT\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"REPRESENTATION_MAP\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_2D\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"REPRESENTATION\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionId\":15");
    }

    @Test
    void shouldEmbedTopologyAndContainerCarrierMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_TOPO_WRAP',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#13=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#15=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#16=VECTOR('VX',#15,1.0);\n"
        + "#17=LINE('L0',#12,#16);\n"
        + "#18=VERTEX_POINT('V0',#12);\n"
        + "#19=VERTEX_POINT('V1',#13);\n"
        + "#20=EDGE_CURVE('E0',#18,#19,#17,.T.);\n"
        + "#21=ORIENTED_EDGE('OE0',$,$,#20,.T.);\n"
        + "#22=PATH('PTH',(#21));\n"
        + "#23=OPEN_PATH('OP0',(#21));\n"
        + "#24=ORIENTED_PATH('OP1',#22,.F.);\n"
        + "#25=EDGE_LOOP('EL0',(#21));\n"
        + "#26=POLY_LOOP('PL0',(#12,#13,#14));\n"
        + "#27=CONNECTED_EDGE_SET('CES0',(#20));\n"
        + "#28=EDGE_BASED_WIREFRAME_MODEL('EBWM',(#27));\n"
        + "#29=WIRE_SHELL('WS0',(#25));\n"
        + "#30=SHELL_BASED_WIREFRAME_MODEL('SBWM',(#29));\n"
        + "#31=POINT_SET('PS0',(#12,#13));\n"
        + "#32=GEOMETRIC_CURVE_SET('GCS0',(#17));\n"
        + "#33=GEOMETRIC_SET('GS0',(#31,#32,#24,#26,#28,#30));\n"
        + "#40=CARTESIAN_POINT('N0',(0.0,0.0,0.0));\n"
        + "#41=CARTESIAN_POINT('N1',(1.0,0.0,0.0));\n"
        + "#42=CARTESIAN_POINT('N2',(2.0,0.0,0.0));\n"
        + "#43=CARTESIAN_POINT('N3',(3.0,0.0,0.0));\n"
        + "#44=CARTESIAN_POINT('N4',(4.0,0.0,0.0));\n"
        + "#45=CARTESIAN_POINT('N5',(5.0,0.0,0.0));\n"
        + "#46=ANNOTATION_TEXT_OCCURRENCE('NOTE_PATH','',#40);\n"
        + "#47=ANNOTATION_TEXT_OCCURRENCE('NOTE_LOOP','',#41);\n"
        + "#48=ANNOTATION_TEXT_OCCURRENCE('NOTE_WIREFRAME','',#42);\n"
        + "#49=ANNOTATION_TEXT_OCCURRENCE('NOTE_SHELL_WIREFRAME','',#43);\n"
        + "#50=ANNOTATION_TEXT_OCCURRENCE('NOTE_POINT_SET','',#44);\n"
        + "#51=ANNOTATION_TEXT_OCCURRENCE('NOTE_GEOMETRIC_SET','',#45);\n"
        + "#60=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#24,#10,#46,#8);\n"
        + "#61=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#25,#10,#47,#8);\n"
        + "#62=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#28,#10,#48,#8);\n"
        + "#63=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#30,#10,#49,#8);\n"
        + "#64=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#31,#10,#50,#8);\n"
        + "#65=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#33,#10,#51,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"ORIENTED_PATH\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"PATH\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"EDGE_LOOP\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"EDGE_BASED_WIREFRAME_MODEL\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"SHELL_BASED_WIREFRAME_MODEL\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"POINT_SET\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"GEOMETRIC_SET\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"GEOMETRIC_CURVE_SET\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"CONNECTED_EDGE_SET\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"WIRE_SHELL\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"ORIENTED_EDGE\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"EDGE_CURVE\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"VERTEX_POINT\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"LINE\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"POLY_LOOP\"",
                "\"viaDefinitionId\":26");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"ORIENTED_PATH\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"PATH\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"EDGE_LOOP\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"EDGE_BASED_WIREFRAME_MODEL\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"SHELL_BASED_WIREFRAME_MODEL\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"POINT_SET\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"GEOMETRIC_SET\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"GEOMETRIC_CURVE_SET\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"CONNECTED_EDGE_SET\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"WIRE_SHELL\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"ORIENTED_EDGE\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"EDGE_CURVE\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"VERTEX_POINT\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"LINE\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"POLY_LOOP\"",
                "\"viaDefinitionId\":26");
    }

    @Test
    void shouldEmbedSurfaceContainerCarrierMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_SURF_WRAP',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#13=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#15=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#16=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#17=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#18=AXIS2_PLACEMENT_3D('AX',#12,#16,#17);\n"
        + "#19=PLANE('PL',#18);\n"
        + "#20=POLY_LOOP('PL0',(#12,#13,#14,#15));\n"
        + "#21=FACE_BOUND('FB',#20,.T.);\n"
        + "#22=ADVANCED_FACE('AF0',(#21),#19,.T.);\n"
        + "#23=ORIENTED_FACE('OF0',#22,.F.);\n"
        + "#24=FACE_SURFACE('FS0',(#21),#19,.T.);\n"
        + "#25=OPEN_SHELL('OS0',(#22));\n"
        + "#26=SURFACED_OPEN_SHELL('SOS0',(#24));\n"
        + "#27=ORIENTED_OPEN_SHELL('OOS0',#25,.F.);\n"
        + "#28=CLOSED_SHELL('CS0',(#22));\n"
        + "#29=ORIENTED_CLOSED_SHELL('OCS0',#28,.F.);\n"
        + "#30=CONNECTED_FACE_SET('CFS0',(#22));\n"
        + "#31=CONNECTED_FACE_SUB_SET('CFSS0',(#22),#30);\n"
        + "#32=FACE_BASED_SURFACE_MODEL('FBSM0',(#30,#25));\n"
        + "#33=SHELL_BASED_SURFACE_MODEL('SBSM0',(#25,#26,#27,#28,#29));\n"
        + "#40=CARTESIAN_POINT('N0',(0.0,0.0,0.0));\n"
        + "#41=CARTESIAN_POINT('N1',(1.0,0.0,0.0));\n"
        + "#42=CARTESIAN_POINT('N2',(2.0,0.0,0.0));\n"
        + "#43=CARTESIAN_POINT('N3',(3.0,0.0,0.0));\n"
        + "#44=CARTESIAN_POINT('N4',(4.0,0.0,0.0));\n"
        + "#45=CARTESIAN_POINT('N5',(5.0,0.0,0.0));\n"
        + "#52=CARTESIAN_POINT('N6',(6.0,0.0,0.0));\n"
        + "#53=CARTESIAN_POINT('N7',(7.0,0.0,0.0));\n"
        + "#46=ANNOTATION_TEXT_OCCURRENCE('NOTE_AF','',#40);\n"
        + "#47=ANNOTATION_TEXT_OCCURRENCE('NOTE_OF','',#41);\n"
        + "#48=ANNOTATION_TEXT_OCCURRENCE('NOTE_FS','',#42);\n"
        + "#49=ANNOTATION_TEXT_OCCURRENCE('NOTE_OS','',#43);\n"
        + "#50=ANNOTATION_TEXT_OCCURRENCE('NOTE_CFS','',#44);\n"
        + "#51=ANNOTATION_TEXT_OCCURRENCE('NOTE_MODEL','',#45);\n"
        + "#54=ANNOTATION_TEXT_OCCURRENCE('NOTE_CFSS','',#52);\n"
        + "#55=ANNOTATION_TEXT_OCCURRENCE('NOTE_FBSM','',#53);\n"
        + "#60=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#22,#10,#46,#8);\n"
        + "#61=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#23,#10,#47,#8);\n"
        + "#62=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#24,#10,#48,#8);\n"
        + "#63=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#25,#10,#49,#8);\n"
        + "#64=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#30,#10,#50,#8);\n"
        + "#65=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#33,#10,#51,#8);\n"
        + "#66=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#31,#10,#54,#8);\n"
        + "#67=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#32,#10,#55,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_AF\"",
                "\"name\":\"NOTE_OF\"",
                "\"name\":\"NOTE_FS\"",
                "\"name\":\"NOTE_OS\"",
                "\"name\":\"NOTE_CFS\"",
                "\"name\":\"NOTE_MODEL\"",
                "\"name\":\"NOTE_CFSS\"",
                "\"name\":\"NOTE_FBSM\"",
                "\"viaDefinitionType\":\"ADVANCED_FACE\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"ORIENTED_FACE\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"FACE_SURFACE\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"OPEN_SHELL\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"SURFACED_OPEN_SHELL\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"ORIENTED_OPEN_SHELL\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"CLOSED_SHELL\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"ORIENTED_CLOSED_SHELL\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"CONNECTED_FACE_SET\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"CONNECTED_FACE_SUB_SET\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"FACE_BASED_SURFACE_MODEL\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"SHELL_BASED_SURFACE_MODEL\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"FACE_BOUND\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"PLANE\"",
                "\"viaDefinitionId\":19");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_AF\"",
                "\"name\":\"NOTE_OF\"",
                "\"name\":\"NOTE_FS\"",
                "\"name\":\"NOTE_OS\"",
                "\"name\":\"NOTE_CFS\"",
                "\"name\":\"NOTE_MODEL\"",
                "\"name\":\"NOTE_CFSS\"",
                "\"name\":\"NOTE_FBSM\"",
                "\"viaDefinitionType\":\"ADVANCED_FACE\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"ORIENTED_FACE\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"FACE_SURFACE\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"OPEN_SHELL\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"SURFACED_OPEN_SHELL\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"ORIENTED_OPEN_SHELL\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"CLOSED_SHELL\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"ORIENTED_CLOSED_SHELL\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"CONNECTED_FACE_SET\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"CONNECTED_FACE_SUB_SET\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"FACE_BASED_SURFACE_MODEL\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"SHELL_BASED_SURFACE_MODEL\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"FACE_BOUND\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"PLANE\"",
                "\"viaDefinitionId\":19");
    }

    @Test
    void shouldEmbedPathAndWireSemanticDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=VERTEX_POINT('VP0',#1);\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=VECTOR('V0',#3,1.0);\n"
        + "#5=LINE('L0',#1,#4);\n"
        + "#6=EDGE_CURVE('E0',#2,#2,#5,.T.);\n"
        + "#7=ORIENTED_EDGE('',*,*,#6,.T.);\n"
        + "#8=PATH('OP',(#7));\n"
        + "#9=CONNECTED_EDGE_SET('CES',(#7));\n"
        + "#10=EDGE_LOOP('EL',(#7));\n"
        + "#11=WIRE_SHELL('WS',(#10));\n"
        + "#12=VERTEX_LOOP('VL',#2);\n"
        + "#13=VERTEX_SHELL('VS',#12);\n"
        + "#14=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#15=REPRESENTATION('REP_A',(),#14);\n"
        + "#16=ANNOTATION_TEXT_OCCURRENCE('NOTE','targets',#1);\n"
        + "#17=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#8,#15,#16,#8);\n"
        + "#18=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#9,#15,#16,#9);\n"
        + "#19=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#10,#15,#16,#10);\n"
        + "#20=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#11,#15,#16,#11);\n"
        + "#21=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#13,#15,#16,#13);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE\"",
                "\"id\":15,\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"id\":8,\"type\":\"path\",\"name\":\"OP\"",
                "\"id\":9,\"type\":\"edge_set\",\"name\":\"CES\"",
                "\"id\":10,\"type\":\"loop\",\"name\":\"EL\"",
                "\"id\":11,\"type\":\"wire_shell\",\"name\":\"WS\"",
                "\"id\":13,\"type\":\"vertex_shell\",\"name\":\"VS\"",
                "\"viaDefinitionType\":\"PATH\"",
                "\"viaDefinitionId\":8",
                "\"viaDefinitionType\":\"CONNECTED_EDGE_SET\"",
                "\"viaDefinitionId\":9",
                "\"viaDefinitionType\":\"EDGE_LOOP\"",
                "\"viaDefinitionId\":10",
                "\"viaDefinitionType\":\"WIRE_SHELL\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"VERTEX_SHELL\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"ORIENTED_EDGE\"",
                "\"viaDefinitionId\":7",
                "\"viaDefinitionType\":\"EDGE_CURVE\"",
                "\"viaDefinitionId\":6",
                "\"viaDefinitionType\":\"LINE\"",
                "\"viaDefinitionId\":5",
                "\"viaDefinitionType\":\"VERTEX_POINT\"",
                "\"viaDefinitionId\":2",
                "\"viaDefinitionType\":\"VERTEX_LOOP\"",
                "\"viaDefinitionId\":12");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE\"",
                "\"id\":15,\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"id\":8,\"type\":\"path\",\"name\":\"OP\"",
                "\"id\":9,\"type\":\"edge_set\",\"name\":\"CES\"",
                "\"id\":10,\"type\":\"loop\",\"name\":\"EL\"",
                "\"id\":11,\"type\":\"wire_shell\",\"name\":\"WS\"",
                "\"id\":13,\"type\":\"vertex_shell\",\"name\":\"VS\"",
                "\"viaDefinitionType\":\"PATH\"",
                "\"viaDefinitionId\":8",
                "\"viaDefinitionType\":\"CONNECTED_EDGE_SET\"",
                "\"viaDefinitionId\":9",
                "\"viaDefinitionType\":\"EDGE_LOOP\"",
                "\"viaDefinitionId\":10",
                "\"viaDefinitionType\":\"WIRE_SHELL\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"VERTEX_SHELL\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"ORIENTED_EDGE\"",
                "\"viaDefinitionId\":7",
                "\"viaDefinitionType\":\"EDGE_CURVE\"",
                "\"viaDefinitionId\":6",
                "\"viaDefinitionType\":\"LINE\"",
                "\"viaDefinitionId\":5",
                "\"viaDefinitionType\":\"VERTEX_POINT\"",
                "\"viaDefinitionId\":2",
                "\"viaDefinitionType\":\"VERTEX_LOOP\"",
                "\"viaDefinitionId\":12");
    }

    @Test
    void shouldEmbedShellAndModelSemanticDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(0.0,1.0,0.0));\n"
        + "#4=DIRECTION('N',(0.0,0.0,1.0));\n"
        + "#5=DIRECTION('X',(1.0,0.0,0.0));\n"
        + "#6=AXIS2_PLACEMENT_3D('AX',#1,#4,#5);\n"
        + "#7=PLANE('PL',#6);\n"
        + "#8=POLY_LOOP('PL0',(#1,#2,#3));\n"
        + "#9=FACE_BOUND('FB',#8,.T.);\n"
        + "#10=FACE_SURFACE('AF',(#9),#7,.T.);\n"
        + "#11=CONNECTED_FACE_SET('CFS',(#10));\n"
        + "#12=CONNECTED_FACE_SUB_SET('CFSS',(#10),#11);\n"
        + "#13=OPEN_SHELL('OS',(#10));\n"
        + "#14=SURFACED_OPEN_SHELL('SOS',(#10));\n"
        + "#15=ORIENTED_OPEN_SHELL('OOS',#13,.T.);\n"
        + "#16=CLOSED_SHELL('CS',(#10));\n"
        + "#17=ORIENTED_CLOSED_SHELL('OCS',#16,.T.);\n"
        + "#18=FACE_BASED_SURFACE_MODEL('FBSM',(#11));\n"
        + "#19=SHELL_BASED_SURFACE_MODEL('SBSM',(#13,#16));\n"
        + "#20=VERTEX_POINT('VP0',#1);\n"
        + "#21=VERTEX_POINT('VP1',#2);\n"
        + "#22=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#23=VECTOR('V0',#22,1.0);\n"
        + "#24=LINE('L0',#1,#23);\n"
        + "#25=EDGE_CURVE('E0',#20,#21,#24,.T.);\n"
        + "#26=ORIENTED_EDGE('OE0',$,$,#25,.T.);\n"
        + "#27=CONNECTED_EDGE_SET('CES',(#26));\n"
        + "#28=EDGE_BASED_WIREFRAME_MODEL('EBWM',(#27));\n"
        + "#29=EDGE_LOOP('EL',(#26));\n"
        + "#30=WIRE_SHELL('WS',(#29));\n"
        + "#31=VERTEX_LOOP('VL',#20);\n"
        + "#32=VERTEX_SHELL('VS',#31);\n"
        + "#33=SHELL_BASED_WIREFRAME_MODEL('SBWM',(#30,#32));\n"
        + "#34=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#35=REPRESENTATION('REP_A',(),#34);\n"
        + "#36=ANNOTATION_TEXT_OCCURRENCE('NOTE','containers',#1);\n"
        + "#37=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#11,#35,#36,#11);\n"
        + "#38=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#12,#35,#36,#12);\n"
        + "#39=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#13,#35,#36,#13);\n"
        + "#40=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#14,#35,#36,#14);\n"
        + "#41=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#15,#35,#36,#15);\n"
        + "#42=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#16,#35,#36,#16);\n"
        + "#43=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#17,#35,#36,#17);\n"
        + "#44=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#18,#35,#36,#18);\n"
        + "#45=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#19,#35,#36,#19);\n"
        + "#46=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#28,#35,#36,#28);\n"
        + "#47=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#33,#35,#36,#33);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE\"",
                "\"id\":35,\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"id\":11,\"type\":\"face_set\",\"name\":\"CFS\"",
                "\"id\":12,\"type\":\"face_set\",\"name\":\"CFSS\"",
                "\"id\":13,\"type\":\"shell\",\"name\":\"OS\"",
                "\"id\":14,\"type\":\"shell\",\"name\":\"SOS\"",
                "\"id\":15,\"type\":\"shell\",\"name\":\"OOS\"",
                "\"id\":16,\"type\":\"shell\",\"name\":\"CS\"",
                "\"id\":17,\"type\":\"shell\",\"name\":\"OCS\"",
                "\"id\":18,\"type\":\"surface_model\",\"name\":\"FBSM\"",
                "\"id\":19,\"type\":\"surface_model\",\"name\":\"SBSM\"",
                "\"id\":28,\"type\":\"wireframe_model\",\"name\":\"EBWM\"",
                "\"id\":33,\"type\":\"wireframe_model\",\"name\":\"SBWM\"",
                "\"viaDefinitionType\":\"CONNECTED_FACE_SET\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"CONNECTED_FACE_SUB_SET\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"OPEN_SHELL\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"SURFACED_OPEN_SHELL\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"ORIENTED_OPEN_SHELL\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"CLOSED_SHELL\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"ORIENTED_CLOSED_SHELL\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"FACE_BASED_SURFACE_MODEL\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"SHELL_BASED_SURFACE_MODEL\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"EDGE_BASED_WIREFRAME_MODEL\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"SHELL_BASED_WIREFRAME_MODEL\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"CONNECTED_EDGE_SET\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"WIRE_SHELL\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"VERTEX_SHELL\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"FACE_SURFACE\"",
                "\"viaDefinitionId\":10",
                "\"viaDefinitionType\":\"FACE_BOUND\"",
                "\"viaDefinitionId\":9",
                "\"viaDefinitionType\":\"PLANE\"",
                "\"viaDefinitionId\":7");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE\"",
                "\"id\":35,\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"id\":11,\"type\":\"face_set\",\"name\":\"CFS\"",
                "\"id\":12,\"type\":\"face_set\",\"name\":\"CFSS\"",
                "\"id\":13,\"type\":\"shell\",\"name\":\"OS\"",
                "\"id\":14,\"type\":\"shell\",\"name\":\"SOS\"",
                "\"id\":15,\"type\":\"shell\",\"name\":\"OOS\"",
                "\"id\":16,\"type\":\"shell\",\"name\":\"CS\"",
                "\"id\":17,\"type\":\"shell\",\"name\":\"OCS\"",
                "\"id\":18,\"type\":\"surface_model\",\"name\":\"FBSM\"",
                "\"id\":19,\"type\":\"surface_model\",\"name\":\"SBSM\"",
                "\"id\":28,\"type\":\"wireframe_model\",\"name\":\"EBWM\"",
                "\"id\":33,\"type\":\"wireframe_model\",\"name\":\"SBWM\"",
                "\"viaDefinitionType\":\"CONNECTED_FACE_SET\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"CONNECTED_FACE_SUB_SET\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"OPEN_SHELL\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"SURFACED_OPEN_SHELL\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"ORIENTED_OPEN_SHELL\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"CLOSED_SHELL\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"ORIENTED_CLOSED_SHELL\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"FACE_BASED_SURFACE_MODEL\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"SHELL_BASED_SURFACE_MODEL\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"EDGE_BASED_WIREFRAME_MODEL\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"SHELL_BASED_WIREFRAME_MODEL\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"CONNECTED_EDGE_SET\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"WIRE_SHELL\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"VERTEX_SHELL\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"FACE_SURFACE\"",
                "\"viaDefinitionId\":10",
                "\"viaDefinitionType\":\"FACE_BOUND\"",
                "\"viaDefinitionId\":9",
                "\"viaDefinitionType\":\"PLANE\"",
                "\"viaDefinitionId\":7");
    }

    @Test
    void shouldEmbedSolidDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=BLOCK('BLK',#4,10.0,20.0,30.0);\n"
        + "#6=CARTESIAN_POINT('PZ',(0.0,0.0,15.0));\n"
        + "#7=AXIS2_PLACEMENT_3D('PLAX',#6,#2,#3);\n"
        + "#8=PLANE('PLANE',#7);\n"
        + "#9=HALF_SPACE_SOLID('HS',#8,.T.);\n"
        + "#10=(BOOLEAN_RESULT(.DIFFERENCE.,#5,#9) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BOOL0'));\n"
        + "#11=CSG_SOLID('CSG0',#10);\n"
        + "#12=(BOOLEAN_CLIPPING_RESULT(.DIFFERENCE.,#5,#9) BOOLEAN_RESULT(.DIFFERENCE.,#5,#9) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BCR0'));\n"
        + "#20=CARTESIAN_POINT('P2D',(0.0,0.0));\n"
        + "#21=DIRECTION('DX2',(1.0,0.0));\n"
        + "#22=AXIS2_PLACEMENT_2D('PROFILE_AX',#20,#21);\n"
        + "#23=RECTANGLE_PROFILE_DEF(.AREA.,'RPD',#22,4.0,2.0);\n"
        + "#24=DIRECTION('DIR',(0.0,0.0,1.0));\n"
        + "#25=EXTRUDED_AREA_SOLID('EX0',#23,#4,#24,5.0);\n"
        + "#26=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TX',#2,#3,#1,1.0,$);\n"
        + "#27=SOLID_REPLICA('SR0',#25,#26);\n"
        + "#30=CARTESIAN_POINT('Q0',(0.0,0.0,0.0));\n"
        + "#31=CARTESIAN_POINT('Q1',(1.0,0.0,0.0));\n"
        + "#32=CARTESIAN_POINT('Q2',(1.0,1.0,0.0));\n"
        + "#33=CARTESIAN_POINT('Q3',(0.0,1.0,0.0));\n"
        + "#34=DIRECTION('NZ',(0.0,0.0,1.0));\n"
        + "#35=DIRECTION('NX',(1.0,0.0,0.0));\n"
        + "#36=AXIS2_PLACEMENT_3D('SAX',#30,#34,#35);\n"
        + "#37=PLANE('SPL',#36);\n"
        + "#38=VERTEX_POINT('V0',#30);\n"
        + "#39=VERTEX_POINT('V1',#31);\n"
        + "#40=VERTEX_POINT('V2',#32);\n"
        + "#41=VERTEX_POINT('V3',#33);\n"
        + "#42=DIRECTION('D1',(1.0,0.0,0.0));\n"
        + "#43=VECTOR('VE1',#42,1.0);\n"
        + "#44=LINE('L1',#30,#43);\n"
        + "#45=DIRECTION('D2',(0.0,1.0,0.0));\n"
        + "#46=VECTOR('VE2',#45,1.0);\n"
        + "#47=LINE('L2',#31,#46);\n"
        + "#48=DIRECTION('D3',(-1.0,0.0,0.0));\n"
        + "#49=VECTOR('VE3',#48,1.0);\n"
        + "#50=LINE('L3',#32,#49);\n"
        + "#51=DIRECTION('D4',(0.0,-1.0,0.0));\n"
        + "#52=VECTOR('VE4',#51,1.0);\n"
        + "#53=LINE('L4',#33,#52);\n"
        + "#54=EDGE_CURVE('E1',#38,#39,#44,.T.);\n"
        + "#55=EDGE_CURVE('E2',#39,#40,#47,.T.);\n"
        + "#56=EDGE_CURVE('E3',#40,#41,#50,.T.);\n"
        + "#57=EDGE_CURVE('E4',#41,#38,#53,.T.);\n"
        + "#58=ORIENTED_EDGE('OE1',$,$,#54,.T.);\n"
        + "#59=ORIENTED_EDGE('OE2',$,$,#55,.T.);\n"
        + "#60=ORIENTED_EDGE('OE3',$,$,#56,.T.);\n"
        + "#61=ORIENTED_EDGE('OE4',$,$,#57,.T.);\n"
        + "#62=EDGE_LOOP('LOOP',(#58,#59,#60,#61));\n"
        + "#63=FACE_OUTER_BOUND('FOB',#62,.T.);\n"
        + "#64=ADVANCED_FACE('AF0',(#63),#37,.T.);\n"
        + "#65=CLOSED_SHELL('CS0',(#64));\n"
        + "#66=MANIFOLD_SOLID_BREP('MSB0',#65);\n"
        + "#67=BREP_WITH_VOIDS('BWV0',#65,());\n"
        + "#70=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#71=REPRESENTATION('REP_A',(),#70);\n"
        + "#72=ANNOTATION_TEXT_OCCURRENCE('NOTE','solids',#1);\n"
        + "#73=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#66,#71,#72,#66);\n"
        + "#74=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#67,#71,#72,#67);\n"
        + "#75=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#25,#71,#72,#25);\n"
        + "#76=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#27,#71,#72,#27);\n"
        + "#77=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#5,#71,#72,#5);\n"
        + "#78=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#11,#71,#72,#11);\n"
        + "#79=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#10,#71,#72,#10);\n"
        + "#80=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#12,#71,#72,#12);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE\"",
                "\"viaDefinitionType\":\"MANIFOLD_SOLID_BREP\"",
                "\"viaDefinitionId\":66",
                "\"viaDefinitionType\":\"BREP_WITH_VOIDS\"",
                "\"viaDefinitionId\":67",
                "\"viaDefinitionType\":\"EXTRUDED_AREA_SOLID\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"SOLID_REPLICA\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"BLOCK\"",
                "\"viaDefinitionId\":5",
                "\"viaDefinitionType\":\"CSG_SOLID\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"BOOLEAN_RESULT\"",
                "\"viaDefinitionId\":10",
                "\"viaDefinitionType\":\"BOOLEAN_CLIPPING_RESULT\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"HALF_SPACE_SOLID\"",
                "\"viaDefinitionId\":9",
                "\"viaDefinitionType\":\"CLOSED_SHELL\"",
                "\"viaDefinitionId\":65");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE\"",
                "\"viaDefinitionType\":\"MANIFOLD_SOLID_BREP\"",
                "\"viaDefinitionId\":66",
                "\"viaDefinitionType\":\"BREP_WITH_VOIDS\"",
                "\"viaDefinitionId\":67",
                "\"viaDefinitionType\":\"EXTRUDED_AREA_SOLID\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"SOLID_REPLICA\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"BLOCK\"",
                "\"viaDefinitionId\":5",
                "\"viaDefinitionType\":\"CSG_SOLID\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"BOOLEAN_RESULT\"",
                "\"viaDefinitionId\":10",
                "\"viaDefinitionType\":\"BOOLEAN_CLIPPING_RESULT\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"HALF_SPACE_SOLID\"",
                "\"viaDefinitionId\":9",
                "\"viaDefinitionType\":\"CLOSED_SHELL\"",
                "\"viaDefinitionId\":65");
    }

    @Test
    void shouldEmbedPrimitiveSurfaceAndProfileDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));\n"
        + "#10=REPRESENTATION('REP_PRIMITIVE_SOLID',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#13=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#15=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#16=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#17=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#18=VECTOR('VX',#17,1.0);\n"
        + "#19=AXIS1_PLACEMENT('AX1',#12,#16);\n"
        + "#20=AXIS2_PLACEMENT_3D('AX2',#12,#16,#17);\n"
        + "#21=AXIS2_PLACEMENT_2D('AX2D',#12,#17);\n"
        + "#22=LINE('L0',#12,#18);\n"
        + "#23=CIRCLE('C0',#20,1.0);\n"
        + "#24=ELLIPSE('E0',#20,2.0,1.0);\n"
        + "#25=PLANE('PL0',#20);\n"
        + "#26=CYLINDRICAL_SURFACE('CY0',#20,1.0);\n"
        + "#27=CONICAL_SURFACE('CN0',#20,1.0,0.5);\n"
        + "#28=TOROIDAL_SURFACE('TO0',#20,5.0,1.0);\n"
        + "#29=SURFACE_OF_LINEAR_EXTRUSION('SLE0',#22,#18);\n"
        + "#30=SURFACE_OF_REVOLUTION('SOR0',#22,#19);\n"
        + "#31=POINT_SET('PS0',(#12,#13));\n"
        + "#32=VERTEX_POINT('V0',#12);\n"
        + "#33=VERTEX_POINT('V1',#13);\n"
        + "#34=VERTEX_POINT('V2',#14);\n"
        + "#35=VERTEX_POINT('V3',#15);\n"
        + "#36=EDGE_CURVE('E0',#32,#33,#22,.T.);\n"
        + "#37=EDGE_CURVE('E1',#33,#34,#22,.T.);\n"
        + "#38=EDGE_CURVE('E2',#34,#35,#22,.T.);\n"
        + "#39=EDGE_CURVE('E3',#35,#32,#22,.T.);\n"
        + "#40=ORIENTED_EDGE('OE0',$,$,#36,.T.);\n"
        + "#41=ORIENTED_EDGE('OE1',$,$,#37,.T.);\n"
        + "#42=ORIENTED_EDGE('OE2',$,$,#38,.T.);\n"
        + "#43=ORIENTED_EDGE('OE3',$,$,#39,.T.);\n"
        + "#44=EDGE_LOOP('EL0',(#40,#41,#42,#43));\n"
        + "#45=FACE_OUTER_BOUND('FOB0',#44,.T.);\n"
        + "#46=ADVANCED_FACE('AF0',(#45),#25,.T.);\n"
        + "#47=ORIENTED_FACE('OF0',#46,.F.);\n"
        + "#48=CLOSED_SHELL('CS0',(#46));\n"
        + "#49=MANIFOLD_SOLID_BREP('MSB0',#48);\n"
        + "#50=BREP_WITH_VOIDS('BV0',#48,());\n"
        + "#51=RECTANGLE_PROFILE_DEF(.AREA.,'RPD',#21,1.0,1.0);\n"
        + "#52=EXTRUDED_AREA_SOLID('EAS0',#51,#20,#16,2.0);\n"
        + "#53=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T3',#17,#16,#12,1.0,#17);\n"
        + "#54=SOLID_REPLICA('SR0',#49,#53);\n"
        + "#55=BLOCK('BLK0',#20,1.0,2.0,3.0);\n"
        + "#56=CSG_SOLID('CSG0',#55);\n"
        + "#57=(BOOLEAN_RESULT(.UNION.,#49,#56) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BR0'));\n"
        + "#58=(BOOLEAN_CLIPPING_RESULT(.DIFFERENCE.,#56,#49) BOOLEAN_RESULT(.DIFFERENCE.,#56,#49) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BCR0'));\n"
        + "#100=CARTESIAN_POINT('N0',(0.0,0.0,0.0));\n"
        + "#101=ANNOTATION_TEXT_OCCURRENCE('NOTE','primitive',#100);\n"
        + "#140=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#21,#10,#101,#8);\n"
        + "#141=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#22,#10,#101,#8);\n"
        + "#142=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#23,#10,#101,#8);\n"
        + "#143=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#24,#10,#101,#8);\n"
        + "#144=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#25,#10,#101,#8);\n"
        + "#145=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#26,#10,#101,#8);\n"
        + "#146=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#27,#10,#101,#8);\n"
        + "#147=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#28,#10,#101,#8);\n"
        + "#148=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#29,#10,#101,#8);\n"
        + "#149=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#30,#10,#101,#8);\n"
        + "#150=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#51,#10,#101,#8);\n"
        + "#151=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#52,#10,#101,#8);\n"
        + "#152=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#54,#10,#101,#8);\n"
        + "#153=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#55,#10,#101,#8);\n"
        + "#154=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#56,#10,#101,#8);\n"
        + "#155=PMI_REQUIREMENT_ITEM_ASSOCIATION('A15','',#57,#10,#101,#8);\n"
        + "#156=PMI_REQUIREMENT_ITEM_ASSOCIATION('A16','',#58,#10,#101,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE\"",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_2D\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"LINE\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"CIRCLE\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"ELLIPSE\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"PLANE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"CYLINDRICAL_SURFACE\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"CONICAL_SURFACE\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"TOROIDAL_SURFACE\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"SURFACE_OF_LINEAR_EXTRUSION\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"SURFACE_OF_REVOLUTION\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"RECTANGLE_PROFILE_DEF\"",
                "\"viaDefinitionId\":51",
                "\"viaDefinitionType\":\"EXTRUDED_AREA_SOLID\"",
                "\"viaDefinitionId\":52",
                "\"viaDefinitionType\":\"SOLID_REPLICA\"",
                "\"viaDefinitionId\":54",
                "\"viaDefinitionType\":\"BLOCK\"",
                "\"viaDefinitionId\":55",
                "\"viaDefinitionType\":\"CSG_SOLID\"",
                "\"viaDefinitionId\":56",
                "\"viaDefinitionType\":\"BOOLEAN_RESULT\"",
                "\"viaDefinitionId\":57",
                "\"viaDefinitionType\":\"BOOLEAN_CLIPPING_RESULT\"",
                "\"viaDefinitionId\":58");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE\"",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_2D\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"LINE\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"CIRCLE\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"ELLIPSE\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"PLANE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"CYLINDRICAL_SURFACE\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"CONICAL_SURFACE\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"TOROIDAL_SURFACE\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"SURFACE_OF_LINEAR_EXTRUSION\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"SURFACE_OF_REVOLUTION\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"RECTANGLE_PROFILE_DEF\"",
                "\"viaDefinitionId\":51",
                "\"viaDefinitionType\":\"EXTRUDED_AREA_SOLID\"",
                "\"viaDefinitionId\":52",
                "\"viaDefinitionType\":\"SOLID_REPLICA\"",
                "\"viaDefinitionId\":54",
                "\"viaDefinitionType\":\"BLOCK\"",
                "\"viaDefinitionId\":55",
                "\"viaDefinitionType\":\"CSG_SOLID\"",
                "\"viaDefinitionId\":56",
                "\"viaDefinitionType\":\"BOOLEAN_RESULT\"",
                "\"viaDefinitionId\":57",
                "\"viaDefinitionType\":\"BOOLEAN_CLIPPING_RESULT\"",
                "\"viaDefinitionId\":58");
    }

    @Test
    void shouldEmbedGeometricLeafAndHalfSpaceDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));\n"
        + "#10=REPRESENTATION('REP_GEOM_LEAF',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#13=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#14=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#21=(SURFACE_MODEL() REPRESENTATION_ITEM('SM0'));\n"
        + "#22=(SOLID_MODEL() REPRESENTATION_ITEM('SO0'));\n"
        + "#17=AXIS2_PLACEMENT_3D('AX2',#12,#13,#14);\n"
        + "#24=BOX_DOMAIN(#12,2.0,3.0,4.0);\n"
        + "#25=PLANE('PLN',#17);\n"
        + "#26=HALF_SPACE_SOLID('HS',#25,.T.);\n"
        + "#27=BOXED_HALF_SPACE('BHS',#25,.F.,#24);\n"
        + "#70=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#71=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#72=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#73=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#74=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#84=ANNOTATION_TEXT_OCCURRENCE('NOTE_SURFACE_MODEL','',#70);\n"
        + "#85=ANNOTATION_TEXT_OCCURRENCE('NOTE_SOLID_MODEL','',#71);\n"
        + "#86=ANNOTATION_TEXT_OCCURRENCE('NOTE_BOX_DOMAIN','',#72);\n"
        + "#87=ANNOTATION_TEXT_OCCURRENCE('NOTE_HALF_SPACE','',#73);\n"
        + "#88=ANNOTATION_TEXT_OCCURRENCE('NOTE_BOXED_HALF_SPACE','',#74);\n"
        + "#98=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#21,#10,#84,#8);\n"
        + "#99=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#22,#10,#85,#8);\n"
        + "#100=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#24,#10,#86,#8);\n"
        + "#101=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#26,#10,#87,#8);\n"
        + "#102=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#27,#10,#88,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_SURFACE_MODEL\"",
                "\"name\":\"NOTE_SOLID_MODEL\"",
                "\"name\":\"NOTE_BOX_DOMAIN\"",
                "\"name\":\"NOTE_HALF_SPACE\"",
                "\"name\":\"NOTE_BOXED_HALF_SPACE\"",
                "\"viaDefinitionType\":\"SURFACE_MODEL\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"SOLID_MODEL\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"BOX_DOMAIN\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"HALF_SPACE_SOLID\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"BOXED_HALF_SPACE\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"PLANE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":12");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_SURFACE_MODEL\"",
                "\"name\":\"NOTE_SOLID_MODEL\"",
                "\"name\":\"NOTE_BOX_DOMAIN\"",
                "\"name\":\"NOTE_HALF_SPACE\"",
                "\"name\":\"NOTE_BOXED_HALF_SPACE\"",
                "\"viaDefinitionType\":\"SURFACE_MODEL\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"SOLID_MODEL\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"BOX_DOMAIN\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"HALF_SPACE_SOLID\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"BOXED_HALF_SPACE\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"PLANE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":12");
    }

    @Test
    void shouldEmbedRationalCurveAndProfileDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));\n"
        + "#10=REPRESENTATION('REP_PROFILE_CURVE',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#13=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#15=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#16=AXIS2_PLACEMENT_2D('AX2',#12,#15);\n"
        + "#17=POLYLINE('PLC',(#12,#13,#14,#12));\n"
        + "#18=POLYLINE('PLO',(#12,#13,#14));\n"
        + "#19=(B_SPLINE_CURVE('RBC0',2,(#12,#13,#14),.UNSPECIFIED.,.F.,.F.)\n"
        + "     B_SPLINE_CURVE_WITH_KNOTS((3,3),(0.0,1.0),.UNSPECIFIED.)\n"
        + "     RATIONAL_B_SPLINE_CURVE((1.0,0.5,1.0)));\n"
        + "#20=CIRCLE_PROFILE_DEF(.AREA.,'C',#16,2.0);\n"
        + "#21=RECTANGLE_PROFILE_DEF(.AREA.,'R',#16,4.0,2.0);\n"
        + "#22=CENTERED_RECTANGLE_PROFILE_DEF(.AREA.,'CR',#16,3.0,5.0);\n"
        + "#23=ELLIPSE_PROFILE_DEF(.AREA.,'E',#16,3.0,1.5);\n"
        + "#24=ROUNDED_RECTANGLE_PROFILE_DEF(.AREA.,'RR',#16,6.0,4.0,0.5);\n"
        + "#25=CIRCULAR_HOLLOW_PROFILE_DEF(.AREA.,'CH',#16,3.0,0.5);\n"
        + "#26=ARBITRARY_CLOSED_PROFILE_DEF(.AREA.,'ACP',#17);\n"
        + "#27=ARBITRARY_PROFILE_DEF(.AREA.,'AP',#17);\n"
        + "#28=ARBITRARY_OPEN_PROFILE_DEF(.CURVE.,'AOP',#18);\n"
        + "#100=CARTESIAN_POINT('N0',(0.0,0.0,0.0));\n"
        + "#101=ANNOTATION_TEXT_OCCURRENCE('NOTE','profiles',#100);\n"
        + "#130=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#19,#10,#101,#8);\n"
        + "#131=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#20,#10,#101,#8);\n"
        + "#132=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#21,#10,#101,#8);\n"
        + "#133=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#22,#10,#101,#8);\n"
        + "#134=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#23,#10,#101,#8);\n"
        + "#135=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#24,#10,#101,#8);\n"
        + "#136=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#25,#10,#101,#8);\n"
        + "#137=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#26,#10,#101,#8);\n"
        + "#138=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#27,#10,#101,#8);\n"
        + "#139=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#28,#10,#101,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE\"",
                "\"viaDefinitionType\":\"RATIONAL_B_SPLINE_CURVE\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"CIRCLE_PROFILE_DEF\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"RECTANGLE_PROFILE_DEF\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"CENTERED_RECTANGLE_PROFILE_DEF\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"ELLIPSE_PROFILE_DEF\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"ROUNDED_RECTANGLE_PROFILE_DEF\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"CIRCULAR_HOLLOW_PROFILE_DEF\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"ARBITRARY_CLOSED_PROFILE_DEF\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"ARBITRARY_PROFILE_DEF\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"ARBITRARY_OPEN_PROFILE_DEF\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_2D\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"POLYLINE\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"POLYLINE\"",
                "\"viaDefinitionId\":18");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE\"",
                "\"viaDefinitionType\":\"RATIONAL_B_SPLINE_CURVE\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"CIRCLE_PROFILE_DEF\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"RECTANGLE_PROFILE_DEF\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"CENTERED_RECTANGLE_PROFILE_DEF\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"ELLIPSE_PROFILE_DEF\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"ROUNDED_RECTANGLE_PROFILE_DEF\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"CIRCULAR_HOLLOW_PROFILE_DEF\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"ARBITRARY_CLOSED_PROFILE_DEF\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"ARBITRARY_PROFILE_DEF\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"ARBITRARY_OPEN_PROFILE_DEF\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_2D\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"POLYLINE\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"POLYLINE\"",
                "\"viaDefinitionId\":18");
    }

    @Test
    void shouldEmbedWrapperCurveSurfaceAndTopologyLeafMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));\n"
        + "#10=REPRESENTATION('REP_WRAPPER_LEAF',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#13=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#15=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#16=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#17=VECTOR('VX',#16,1.0);\n"
        + "#18=AXIS2_PLACEMENT_3D('AX3',#12,#15,#16);\n"
        + "#19=LINE('L0',#12,#17);\n"
        + "#32=PLANE('PL0',#18);\n"
        + "#33=RECTANGULAR_TRIMMED_SURFACE('RTS',#32,0.0,1.0,0.0,1.0,.T.,.T.);\n"
        + "#34=POLYLINE('PL1',(#12,#13,#14));\n"
        + "#35=CURVE_BOUNDED_SURFACE('CBS',#32,(#34),.F.);\n"
        + "#36=ORIENTED_SURFACE('OS',#32,.T.);\n"
        + "#37=OFFSET_SURFACE('OFS',#32,1.0,.F.);\n"
        + "#38=SPHERICAL_SURFACE('SPH',#18,2.0);\n"
        + "#39=DEGENERATE_TOROIDAL_SURFACE('DTS',#18,5.0,1.0,.T.);\n"
        + "#40=CARTESIAN_POINT('UV0',(0.0,0.0));\n"
        + "#41=DIRECTION('UX',(1.0,0.0));\n"
        + "#42=VECTOR('UVV',#41,1.0);\n"
        + "#43=LINE('UVL',#40,#42);\n"
        + "#44=REPRESENTATION_CONTEXT('PC','PARAMETRIC');\n"
        + "#45=DEFINITIONAL_REPRESENTATION('DPR',(#43),#44);\n"
        + "#46=DEGENERATE_PCURVE('DPC',#32,#45);\n"
        + "#47=(VERTEX() TOPOLOGICAL_REPRESENTATION_ITEM('VTX0'));\n"
        + "#48=VERTEX_POINT('VP0',#12);\n"
        + "#49=VERTEX_POINT('VP1',#13);\n"
        + "#50=EDGE_CURVE('EC0',#48,#49,#19,.T.);\n"
        + "#51=(EDGE() TOPOLOGICAL_REPRESENTATION_ITEM('ED0'));\n"
        + "#52=SUBEDGE('SE0',#48,#49,#50);\n"
        + "#53=POLY_LOOP('LOOP0',(#12,#13,#14));\n"
        + "#54=(FACE() TOPOLOGICAL_REPRESENTATION_ITEM('FC0'));\n"
        + "#110=CARTESIAN_POINT('N0',(0.0,0.0,0.0));\n"
        + "#157=ANNOTATION_TEXT_OCCURRENCE('NOTE','wrappers',#110);\n"
        + "#170=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#33,#10,#157,#8);\n"
        + "#171=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#35,#10,#157,#8);\n"
        + "#172=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#36,#10,#157,#8);\n"
        + "#173=PMI_REQUIREMENT_ITEM_ASSOCIATION('A15','',#37,#10,#157,#8);\n"
        + "#174=PMI_REQUIREMENT_ITEM_ASSOCIATION('A16','',#38,#10,#157,#8);\n"
        + "#175=PMI_REQUIREMENT_ITEM_ASSOCIATION('A17','',#39,#10,#157,#8);\n"
        + "#176=PMI_REQUIREMENT_ITEM_ASSOCIATION('A18','',#46,#10,#157,#8);\n"
        + "#177=PMI_REQUIREMENT_ITEM_ASSOCIATION('A19','',#47,#10,#157,#8);\n"
        + "#178=PMI_REQUIREMENT_ITEM_ASSOCIATION('A20','',#51,#10,#157,#8);\n"
        + "#179=PMI_REQUIREMENT_ITEM_ASSOCIATION('A21','',#52,#10,#157,#8);\n"
        + "#180=PMI_REQUIREMENT_ITEM_ASSOCIATION('A22','',#53,#10,#157,#8);\n"
        + "#181=PMI_REQUIREMENT_ITEM_ASSOCIATION('A23','',#54,#10,#157,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE\"",
                "\"viaDefinitionType\":\"RECTANGULAR_TRIMMED_SURFACE\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"CURVE_BOUNDED_SURFACE\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"ORIENTED_SURFACE\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"OFFSET_SURFACE\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"SPHERICAL_SURFACE\"",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"DEGENERATE_TOROIDAL_SURFACE\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionType\":\"DEGENERATE_PCURVE\"",
                "\"viaDefinitionId\":46",
                "\"viaDefinitionType\":\"VERTEX\"",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionType\":\"EDGE\"",
                "\"viaDefinitionId\":51",
                "\"viaDefinitionType\":\"SUBEDGE\"",
                "\"viaDefinitionId\":52",
                "\"viaDefinitionType\":\"POLY_LOOP\"",
                "\"viaDefinitionId\":53",
                "\"viaDefinitionType\":\"FACE\"",
                "\"viaDefinitionId\":54");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE\"",
                "\"viaDefinitionType\":\"RECTANGULAR_TRIMMED_SURFACE\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"CURVE_BOUNDED_SURFACE\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"ORIENTED_SURFACE\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"OFFSET_SURFACE\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"SPHERICAL_SURFACE\"",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"DEGENERATE_TOROIDAL_SURFACE\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionType\":\"DEGENERATE_PCURVE\"",
                "\"viaDefinitionId\":46",
                "\"viaDefinitionType\":\"VERTEX\"",
                "\"viaDefinitionId\":47",
                "\"viaDefinitionType\":\"EDGE\"",
                "\"viaDefinitionId\":51",
                "\"viaDefinitionType\":\"SUBEDGE\"",
                "\"viaDefinitionId\":52",
                "\"viaDefinitionType\":\"POLY_LOOP\"",
                "\"viaDefinitionId\":53",
                "\"viaDefinitionType\":\"FACE\"",
                "\"viaDefinitionId\":54");
    }

    @Test
    void shouldEmbedReplicaShellAndCurveWrapperDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));\n"
        + "#10=REPRESENTATION('REP_WRAPPER_SHELL',(),#9);\n"
        + "#11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);\n"
        + "#12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#13=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#15=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#16=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#17=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#18=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#19=VECTOR('VX',#17,2.0);\n"
        + "#20=AXIS2_PLACEMENT_3D('AX3',#12,#16,#17);\n"
        + "#21=LINE('L0',#12,#19);\n"
        + "#22=TRIMMED_CURVE('TC0',#21,(#12),(#13),.T.,.CARTESIAN.);\n"
        + "#23=REPRESENTATION_CONTEXT('UV','PARAMETRIC');\n"
        + "#24=DEFINITIONAL_REPRESENTATION('DR0',(#21),#23);\n"
        + "#25=PCURVE('PC0',#32,#24);\n"
        + "#26=SURFACE_CURVE('SC0',#21,(#25),.PCURVE_S1.);\n"
        + "#27=PCURVE('PC1',#32,#24);\n"
        + "#28=SEAM_CURVE('SEAM0',#21,(#25,#27),.PCURVE_S1.);\n"
        + "#29=COMPOSITE_CURVE_SEGMENT(.CONTINUOUS.,.T.,#21);\n"
        + "#30=(COMPOSITE_CURVE('CC0',(#29),.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('cc-name'));\n"
        + "#31=(COMPOSITE_CURVE_ON_SURFACE('CCS0',(#29),.F.) COMPOSITE_CURVE('CCS0',(#29),.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('ccs-name'));\n"
        + "#32=PLANE('PL0',#20);\n"
        + "#33=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T3',#17,#18,#12,1.0,#16);\n"
        + "#34=CURVE_REPLICA('CR0',#21,#33);\n"
        + "#35=SURFACE_REPLICA('SR0',#32,#33);\n"
        + "#36=VERTEX_POINT('V0',#12);\n"
        + "#37=VERTEX_POINT('V1',#13);\n"
        + "#38=VERTEX_POINT('V2',#14);\n"
        + "#39=VERTEX_POINT('V3',#15);\n"
        + "#40=EDGE_CURVE('E0',#36,#37,#21,.T.);\n"
        + "#41=EDGE_CURVE('E1',#37,#38,#21,.T.);\n"
        + "#42=EDGE_CURVE('E2',#38,#39,#21,.T.);\n"
        + "#43=EDGE_CURVE('E3',#39,#36,#21,.T.);\n"
        + "#44=ORIENTED_EDGE('OE0',$,$,#40,.T.);\n"
        + "#45=ORIENTED_EDGE('OE1',$,$,#41,.T.);\n"
        + "#46=ORIENTED_EDGE('OE2',$,$,#42,.T.);\n"
        + "#47=ORIENTED_EDGE('OE3',$,$,#43,.T.);\n"
        + "#48=EDGE_LOOP('EL0',(#44,#45,#46,#47));\n"
        + "#49=VERTEX_LOOP('VL0',#36);\n"
        + "#50=FACE_OUTER_BOUND('FOB0',#48,.T.);\n"
        + "#51=ADVANCED_FACE('F0',(#50),#32,.T.);\n"
        + "#52=OPEN_SHELL('OS0',(#51));\n"
        + "#53=ORIENTED_OPEN_SHELL('OOS0',#52,.F.);\n"
        + "#54=CLOSED_SHELL('CS0',(#51));\n"
        + "#55=ORIENTED_CLOSED_SHELL('OCS0',#54,.F.);\n"
        + "#56=CONNECTED_EDGE_SET('CES0',(#40,#41,#42,#43));\n"
        + "#57=CONNECTED_FACE_SET('CFS0',(#51));\n"
        + "#58=(CONNECTED_FACE_SUB_SET('CFSS0',(#51),#57) CONNECTED_FACE_SET('CFSS0',(#51)));\n"
        + "#100=CARTESIAN_POINT('N0',(0.0,0.0,0.0));\n"
        + "#120=ANNOTATION_TEXT_OCCURRENCE('NOTE','wrappers',#100);\n"
        + "#140=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#22,#10,#120,#8);\n"
        + "#141=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#25,#10,#120,#8);\n"
        + "#142=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#26,#10,#120,#8);\n"
        + "#143=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#28,#10,#120,#8);\n"
        + "#144=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#30,#10,#120,#8);\n"
        + "#145=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#31,#10,#120,#8);\n"
        + "#146=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#33,#10,#120,#8);\n"
        + "#147=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#34,#10,#120,#8);\n"
        + "#148=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#35,#10,#120,#8);\n"
        + "#149=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#36,#10,#120,#8);\n"
        + "#150=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#48,#10,#120,#8);\n"
        + "#151=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#49,#10,#120,#8);\n"
        + "#152=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#50,#10,#120,#8);\n"
        + "#153=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#52,#10,#120,#8);\n"
        + "#154=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#53,#10,#120,#8);\n"
        + "#155=PMI_REQUIREMENT_ITEM_ASSOCIATION('A15','',#54,#10,#120,#8);\n"
        + "#156=PMI_REQUIREMENT_ITEM_ASSOCIATION('A16','',#55,#10,#120,#8);\n"
        + "#157=PMI_REQUIREMENT_ITEM_ASSOCIATION('A17','',#56,#10,#120,#8);\n"
        + "#158=PMI_REQUIREMENT_ITEM_ASSOCIATION('A18','',#57,#10,#120,#8);\n"
        + "#159=PMI_REQUIREMENT_ITEM_ASSOCIATION('A19','',#58,#10,#120,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE\"",
                "\"viaDefinitionType\":\"TRIMMED_CURVE\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"PCURVE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"SURFACE_CURVE\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"SEAM_CURVE\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"COMPOSITE_CURVE\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"COMPOSITE_CURVE_ON_SURFACE\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"CARTESIAN_TRANSFORMATION_OPERATOR_3D\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"CURVE_REPLICA\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"SURFACE_REPLICA\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"VERTEX_POINT\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"EDGE_LOOP\"",
                "\"viaDefinitionId\":48",
                "\"viaDefinitionType\":\"VERTEX_LOOP\"",
                "\"viaDefinitionId\":49",
                "\"viaDefinitionType\":\"FACE_BOUND\"",
                "\"viaDefinitionId\":50",
                "\"viaDefinitionType\":\"OPEN_SHELL\"",
                "\"viaDefinitionId\":52",
                "\"viaDefinitionType\":\"ORIENTED_OPEN_SHELL\"",
                "\"viaDefinitionId\":53",
                "\"viaDefinitionType\":\"CLOSED_SHELL\"",
                "\"viaDefinitionId\":54",
                "\"viaDefinitionType\":\"ORIENTED_CLOSED_SHELL\"",
                "\"viaDefinitionId\":55",
                "\"viaDefinitionType\":\"CONNECTED_EDGE_SET\"",
                "\"viaDefinitionId\":56",
                "\"viaDefinitionType\":\"CONNECTED_FACE_SET\"",
                "\"viaDefinitionId\":57",
                "\"viaDefinitionType\":\"CONNECTED_FACE_SUB_SET\"",
                "\"viaDefinitionId\":58");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE\"",
                "\"viaDefinitionType\":\"TRIMMED_CURVE\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"PCURVE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"SURFACE_CURVE\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"SEAM_CURVE\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"COMPOSITE_CURVE\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"COMPOSITE_CURVE_ON_SURFACE\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"CARTESIAN_TRANSFORMATION_OPERATOR_3D\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"CURVE_REPLICA\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"SURFACE_REPLICA\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"VERTEX_POINT\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"EDGE_LOOP\"",
                "\"viaDefinitionId\":48",
                "\"viaDefinitionType\":\"VERTEX_LOOP\"",
                "\"viaDefinitionId\":49",
                "\"viaDefinitionType\":\"FACE_BOUND\"",
                "\"viaDefinitionId\":50",
                "\"viaDefinitionType\":\"OPEN_SHELL\"",
                "\"viaDefinitionId\":52",
                "\"viaDefinitionType\":\"ORIENTED_OPEN_SHELL\"",
                "\"viaDefinitionId\":53",
                "\"viaDefinitionType\":\"CLOSED_SHELL\"",
                "\"viaDefinitionId\":54",
                "\"viaDefinitionType\":\"ORIENTED_CLOSED_SHELL\"",
                "\"viaDefinitionId\":55",
                "\"viaDefinitionType\":\"CONNECTED_EDGE_SET\"",
                "\"viaDefinitionId\":56",
                "\"viaDefinitionType\":\"CONNECTED_FACE_SET\"",
                "\"viaDefinitionId\":57",
                "\"viaDefinitionType\":\"CONNECTED_FACE_SUB_SET\"",
                "\"viaDefinitionId\":58");
    }

    @Test
    void shouldEmbedProductAndAssemblyShapeDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('ASM','Assembly','Assembly',(#2));\n"
        + "#4=PRODUCT('COMP','Component','Component',(#2));\n"
        + "#5=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#6=PRODUCT_DEFINITION_FORMATION('v1','',#4);\n"
        + "#7=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#8=PRODUCT_DEFINITION('asm_pd','assembly',#5,#7);\n"
        + "#9=PRODUCT_DEFINITION('comp_pd','component',#6,#7);\n"
        + "#10=PRODUCT_DEFINITION_SHAPE('asm_shape','',#8);\n"
        + "#11=PRODUCT_DEFINITION_SHAPE('comp_shape','',#9);\n"
        + "#12=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ-1','OCC','component usage',#8,#9,'R1');\n"
        + "#13=PRODUCT_DEFINITION_SHAPE('occ_shape','',#12);\n"
        + "#20=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#21=REPRESENTATION('REP_COMP',(),#20);\n"
        + "#22=REPRESENTATION('REP_OCC',(),#20);\n"
        + "#23=REPRESENTATION('REP_USAGE_OCC',(),#20);\n"
        + "#24=REPRESENTATION('REP_USAGE_PD',(),#20);\n"
        + "#25=REPRESENTATION('REP_USAGE_PDS',(),#20);\n"
        + "#26=SHAPE_DEFINITION_REPRESENTATION(#11,#21);\n"
        + "#27=REPRESENTATION_RELATIONSHIP('CTX','occ ctx',#21,#22);\n"
        + "#28=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#27,#13);\n"
        + "#29=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#30=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#31=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#32=ANNOTATION_TEXT_OCCURRENCE('NOTE_OCC','occ',#29);\n"
        + "#33=ANNOTATION_TEXT_OCCURRENCE('NOTE_PD','pd',#30);\n"
        + "#34=ANNOTATION_TEXT_OCCURRENCE('NOTE_PDS','pds',#31);\n"
        + "#35=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_OCC','',#12,#23,#32,#13);\n"
        + "#36=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_PD','',#9,#24,#33,#11);\n"
        + "#37=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_PDS','',#11,#25,#34,#11);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_OCC\"",
                "\"name\":\"NOTE_PD\"",
                "\"name\":\"NOTE_PDS\"",
                "\"id\":23,\"type\":\"representation\",\"name\":\"REP_USAGE_OCC\"",
                "\"id\":24,\"type\":\"representation\",\"name\":\"REP_USAGE_PD\"",
                "\"id\":25,\"type\":\"representation\",\"name\":\"REP_USAGE_PDS\"",
                "\"viaDefinitionType\":\"NEXT_ASSEMBLY_USAGE_OCCURRENCE\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION\"",
                "\"viaDefinitionId\":9",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_SHAPE\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"SHAPE_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"CONTEXT_DEPENDENT_SHAPE_REPRESENTATION\"",
                "\"viaDefinitionId\":28");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_OCC\"",
                "\"name\":\"NOTE_PD\"",
                "\"name\":\"NOTE_PDS\"",
                "\"id\":23,\"type\":\"representation\",\"name\":\"REP_USAGE_OCC\"",
                "\"id\":24,\"type\":\"representation\",\"name\":\"REP_USAGE_PD\"",
                "\"id\":25,\"type\":\"representation\",\"name\":\"REP_USAGE_PDS\"",
                "\"viaDefinitionType\":\"NEXT_ASSEMBLY_USAGE_OCCURRENCE\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION\"",
                "\"viaDefinitionId\":9",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_SHAPE\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"SHAPE_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"CONTEXT_DEPENDENT_SHAPE_REPRESENTATION\"",
                "\"viaDefinitionId\":28");
    }

    @Test
    void shouldEmbedAdditionalPropertyRepresentationLinkMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=PROPERTY_DEFINITION('PD0','',#8);\n"
        + "#10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#11=REPRESENTATION('REP_ACTION',(),#10);\n"
        + "#12=REPRESENTATION('REP_CONTACT',(),#10);\n"
        + "#13=REPRESENTATION('REP_KDEF',(),#10);\n"
        + "#14=REPRESENTATION('REP_KMECH',(),#10);\n"
        + "#15=REPRESENTATION('REP_KREL',(),#10);\n"
        + "#16=REPRESENTATION('REP_KTOPO',(),#10);\n"
        + "#17=REPRESENTATION('REP_RESOURCE',(),#10);\n"
        + "#18=ACTION_PROPERTY_REPRESENTATION(#9,#11);\n"
        + "#19=CONTACT_RATIO_REPRESENTATION(#9,#12);\n"
        + "#20=KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION(#9,#13);\n"
        + "#21=KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION(#9,#14);\n"
        + "#22=KINEMATIC_PROPERTY_REPRESENTATION_RELATION(#9,#15);\n"
        + "#23=KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION(#9,#16);\n"
        + "#24=RESOURCE_PROPERTY_REPRESENTATION(#9,#17);\n"
        + "#28=REPRESENTATION_RELATIONSHIP('RR','',#11,#12);\n"
        + "#29=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#30=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#31=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#32=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#33=AXIS2_PLACEMENT_3D('AX0',#29,#31,#32);\n"
        + "#34=AXIS2_PLACEMENT_3D('AX1',#30,#31,#32);\n"
        + "#35=ITEM_DEFINED_TRANSFORMATION('T1','',#33,#34);\n"
        + "#36=(REPRESENTATION_RELATIONSHIP('RRT','',#11,#13)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#35));\n"
        + "#37=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#11,#14);\n"
        + "#25=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#26=ANNOTATION_TEXT_OCCURRENCE('NOTE','links',#25);\n"
        + "#27=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC','',#9,#11,#26,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_ACTION\"",
                "\"id\":12,\"type\":\"representation\",\"name\":\"REP_CONTACT\"",
                "\"id\":13,\"type\":\"representation\",\"name\":\"REP_KDEF\"",
                "\"id\":14,\"type\":\"representation\",\"name\":\"REP_KMECH\"",
                "\"id\":15,\"type\":\"representation\",\"name\":\"REP_KREL\"",
                "\"id\":16,\"type\":\"representation\",\"name\":\"REP_KTOPO\"",
                "\"id\":17,\"type\":\"representation\",\"name\":\"REP_RESOURCE\"",
                "\"viaDefinitionType\":\"ACTION_PROPERTY_REPRESENTATION\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CONTACT_RATIO_REPRESENTATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"KINEMATIC_PROPERTY_REPRESENTATION_RELATION\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"RESOURCE_PROPERTY_REPRESENTATION\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":37");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_ACTION\"",
                "\"id\":12,\"type\":\"representation\",\"name\":\"REP_CONTACT\"",
                "\"id\":13,\"type\":\"representation\",\"name\":\"REP_KDEF\"",
                "\"id\":14,\"type\":\"representation\",\"name\":\"REP_KMECH\"",
                "\"id\":15,\"type\":\"representation\",\"name\":\"REP_KREL\"",
                "\"id\":16,\"type\":\"representation\",\"name\":\"REP_KTOPO\"",
                "\"id\":17,\"type\":\"representation\",\"name\":\"REP_RESOURCE\"",
                "\"viaDefinitionType\":\"ACTION_PROPERTY_REPRESENTATION\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CONTACT_RATIO_REPRESENTATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"KINEMATIC_PROPERTY_REPRESENTATION_RELATION\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"RESOURCE_PROPERTY_REPRESENTATION\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":37");
    }

    @Test
    void shouldEmbedDirectAdditionalPropertyRepresentationLinkDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=PROPERTY_DEFINITION('PD0','',#8);\n"
        + "#10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#11=REPRESENTATION('REP_ACTION',(),#10);\n"
        + "#12=REPRESENTATION('REP_CONTACT',(),#10);\n"
        + "#13=REPRESENTATION('REP_KDEF',(),#10);\n"
        + "#14=REPRESENTATION('REP_KMECH',(),#10);\n"
        + "#15=REPRESENTATION('REP_KREL',(),#10);\n"
        + "#16=REPRESENTATION('REP_KTOPO',(),#10);\n"
        + "#17=REPRESENTATION('REP_RESOURCE',(),#10);\n"
        + "#18=ACTION_PROPERTY_REPRESENTATION(#9,#11);\n"
        + "#19=CONTACT_RATIO_REPRESENTATION(#9,#12);\n"
        + "#20=KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION(#9,#13);\n"
        + "#21=KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION(#9,#14);\n"
        + "#22=KINEMATIC_PROPERTY_REPRESENTATION_RELATION(#9,#15);\n"
        + "#23=KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION(#9,#16);\n"
        + "#24=RESOURCE_PROPERTY_REPRESENTATION(#9,#17);\n"
        + "#25=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#26=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#27=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#28=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#29=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#30=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#31=CARTESIAN_POINT('P6',(6.0,0.0,0.0));\n"
        + "#32=ANNOTATION_TEXT_OCCURRENCE('NOTE_ACTION','',#25);\n"
        + "#33=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONTACT','',#26);\n"
        + "#34=ANNOTATION_TEXT_OCCURRENCE('NOTE_KDEF','',#27);\n"
        + "#35=ANNOTATION_TEXT_OCCURRENCE('NOTE_KMECH','',#28);\n"
        + "#36=ANNOTATION_TEXT_OCCURRENCE('NOTE_KREL','',#29);\n"
        + "#37=ANNOTATION_TEXT_OCCURRENCE('NOTE_KTOPO','',#30);\n"
        + "#38=ANNOTATION_TEXT_OCCURRENCE('NOTE_RESOURCE','',#31);\n"
        + "#39=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#18,#11,#32,#7);\n"
        + "#40=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#19,#12,#33,#7);\n"
        + "#41=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#20,#13,#34,#7);\n"
        + "#42=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#21,#14,#35,#7);\n"
        + "#43=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#22,#15,#36,#7);\n"
        + "#44=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#23,#16,#37,#7);\n"
        + "#45=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#24,#17,#38,#7);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_ACTION\"",
                "\"name\":\"NOTE_CONTACT\"",
                "\"name\":\"NOTE_KDEF\"",
                "\"name\":\"NOTE_KMECH\"",
                "\"name\":\"NOTE_KREL\"",
                "\"name\":\"NOTE_KTOPO\"",
                "\"name\":\"NOTE_RESOURCE\"",
                "\"viaDefinitionType\":\"ACTION_PROPERTY_REPRESENTATION\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CONTACT_RATIO_REPRESENTATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"KINEMATIC_PROPERTY_REPRESENTATION_RELATION\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"RESOURCE_PROPERTY_REPRESENTATION\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":9");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_ACTION\"",
                "\"name\":\"NOTE_CONTACT\"",
                "\"name\":\"NOTE_KDEF\"",
                "\"name\":\"NOTE_KMECH\"",
                "\"name\":\"NOTE_KREL\"",
                "\"name\":\"NOTE_KTOPO\"",
                "\"name\":\"NOTE_RESOURCE\"",
                "\"viaDefinitionType\":\"ACTION_PROPERTY_REPRESENTATION\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"CONTACT_RATIO_REPRESENTATION\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"KINEMATIC_PROPERTY_REPRESENTATION_RELATION\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"RESOURCE_PROPERTY_REPRESENTATION\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":9");
    }

    @Test
    void shouldEmbedPlacedDatumTargetFeatureMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA_BASE','base',#7,.T.);\n"
        + "#9=SHAPE_ASPECT('SA_TARGET','target',#7,.T.);\n"
        + "#10=PROPERTY_DEFINITION('PD_TARGET','',#9);\n"
        + "#11=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#12=REPRESENTATION('REP_DATUM',(),#11);\n"
        + "#13=PLACED_DATUM_TARGET_FEATURE(#10,#12);\n"
        + "#14=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#15=ANNOTATION_TEXT_OCCURRENCE('NOTE','datum',#14);\n"
        + "#16=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC','',#10,#12,#15,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE\"",
                "\"id\":12,\"type\":\"representation\",\"name\":\"REP_DATUM\"",
                "\"viaDefinitionType\":\"PLACED_DATUM_TARGET_FEATURE\"",
                "\"viaDefinitionId\":13");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE\"",
                "\"id\":12,\"type\":\"representation\",\"name\":\"REP_DATUM\"",
                "\"viaDefinitionType\":\"PLACED_DATUM_TARGET_FEATURE\"",
                "\"viaDefinitionId\":13");
    }

    @Test
    void shouldEmbedRuleAndDatumPropertyRepresentationRelationshipMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA_TARGET','target',#7,.T.);\n"
        + "#9=PROPERTY_DEFINITION('PD_TARGET','',#8);\n"
        + "#10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#11=REPRESENTATION('REP_RULE',(),#10);\n"
        + "#28=REPRESENTATION('REP_AUX',(),#10);\n"
        + "#12=FORWARD_CHAINING_RULE_PREMISE(#9,#11);\n"
        + "#13=BACK_CHAINING_RULE_BODY(#9,#11);\n"
        + "#14=PLACED_DATUM_TARGET_FEATURE(#9,#11);\n"
        + "#15=REPRESENTATION_RELATIONSHIP('RR','',#11,#28);\n"
        + "#16=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#17=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#18=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#19=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#20=AXIS2_PLACEMENT_3D('AX0',#16,#18,#19);\n"
        + "#21=AXIS2_PLACEMENT_3D('AX1',#17,#18,#19);\n"
        + "#22=ITEM_DEFINED_TRANSFORMATION('T1','',#20,#21);\n"
        + "#23=(REPRESENTATION_RELATIONSHIP('RRT','',#11,#28)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#22));\n"
        + "#24=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#11,#28);\n"
        + "#25=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#26=ANNOTATION_TEXT_OCCURRENCE('NOTE_RULE_DATUM','',#25);\n"
        + "#27=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC','',#9,#11,#26,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_RULE_DATUM\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_RULE\"",
                "\"viaDefinitionType\":\"FORWARD_CHAINING_RULE_PREMISE\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"BACK_CHAINING_RULE_BODY\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"PLACED_DATUM_TARGET_FEATURE\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":24");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_RULE_DATUM\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_RULE\"",
                "\"viaDefinitionType\":\"FORWARD_CHAINING_RULE_PREMISE\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"BACK_CHAINING_RULE_BODY\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"PLACED_DATUM_TARGET_FEATURE\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":24");
    }

    @Test
    void shouldEmbedPropertyDefinitionRepresentationMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=PROPERTY_DEFINITION('PD0','',#8);\n"
        + "#10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#11=REPRESENTATION('REP_PROP',(),#10);\n"
        + "#12=PROPERTY_DEFINITION_REPRESENTATION(#9,#11);\n"
        + "#13=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#14=ANNOTATION_TEXT_OCCURRENCE('NOTE','prop',#13);\n"
        + "#15=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC','',#9,#11,#14,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_PROP\"",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":12");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_PROP\"",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":12");
    }

    @Test
    void shouldEmbedDirectPropertyAndAttributeRepresentationRelationshipMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=PROPERTY_DEFINITION('PD0','',#8);\n"
        + "#10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#11=REPRESENTATION('REP_PROP',(),#10);\n"
        + "#12=REPRESENTATION('REP_ASSERT',(),#10);\n"
        + "#13=PROPERTY_DEFINITION_REPRESENTATION(#9,#11);\n"
        + "#14=ATTRIBUTE_ASSERTION(#9,#12);\n"
        + "#15=REPRESENTATION_RELATIONSHIP('RR','',#11,#12);\n"
        + "#16=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#17=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#18=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#19=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#20=AXIS2_PLACEMENT_3D('AX0',#16,#18,#19);\n"
        + "#21=AXIS2_PLACEMENT_3D('AX1',#17,#18,#19);\n"
        + "#22=ITEM_DEFINED_TRANSFORMATION('T1','',#20,#21);\n"
        + "#23=(REPRESENTATION_RELATIONSHIP('RRT','',#11,#12)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#22));\n"
        + "#24=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#11,#12);\n"
        + "#25=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#26=ANNOTATION_TEXT_OCCURRENCE('NOTE','direct property links',#25);\n"
        + "#27=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC','',#9,#11,#26,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_PROP\"",
                "\"id\":12,\"type\":\"representation\",\"name\":\"REP_ASSERT\"",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"ATTRIBUTE_ASSERTION\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":24");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_PROP\"",
                "\"id\":12,\"type\":\"representation\",\"name\":\"REP_ASSERT\"",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"ATTRIBUTE_ASSERTION\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":24");
    }

    @Test
    void shouldEmbedDirectPropertyAndAttributeLinkDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=PROPERTY_DEFINITION('PD0','',#8);\n"
        + "#10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#11=REPRESENTATION('REP_PROP',(),#10);\n"
        + "#12=REPRESENTATION('REP_ASSERT',(),#10);\n"
        + "#13=PROPERTY_DEFINITION_REPRESENTATION(#9,#11);\n"
        + "#14=ATTRIBUTE_ASSERTION(#9,#12);\n"
        + "#15=REPRESENTATION_RELATIONSHIP('RR','',#11,#12);\n"
        + "#16=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#17=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#18=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#19=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#20=AXIS2_PLACEMENT_3D('AX0',#16,#18,#19);\n"
        + "#21=AXIS2_PLACEMENT_3D('AX1',#17,#18,#19);\n"
        + "#22=ITEM_DEFINED_TRANSFORMATION('T1','',#20,#21);\n"
        + "#23=(REPRESENTATION_RELATIONSHIP('RRT','',#11,#12)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#22));\n"
        + "#24=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#11,#12);\n"
        + "#25=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#26=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#27=ANNOTATION_TEXT_OCCURRENCE('NOTE_PDR','direct pdr',#25);\n"
        + "#28=ANNOTATION_TEXT_OCCURRENCE('NOTE_ASSERT','direct assert',#26);\n"
        + "#29=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_PDR','',#13,#11,#27,#8);\n"
        + "#30=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_ASSERT','',#14,#12,#28,#8);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_PDR\"",
                "\"name\":\"NOTE_ASSERT\"",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"ATTRIBUTE_ASSERTION\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":24");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_PDR\"",
                "\"name\":\"NOTE_ASSERT\"",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"ATTRIBUTE_ASSERTION\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"AXIS2_PLACEMENT_3D\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"CARTESIAN_POINT\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"DIRECTION\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":24");
    }

    @Test
    void shouldEmbedDirectPredefinedAndColourLeafDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA0','base',#7,.T.);\n"
        + "#9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#10=REPRESENTATION('REP_PREDEF',(),#9);\n"
        + "#11=REPRESENTATION('REP_AUX',(),#9);\n"
        + "#12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);\n"
        + "#13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));\n"
        + "#14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));\n"
        + "#15=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#16=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);\n"
        + "#18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);\n"
        + "#19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);\n"
        + "#20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));\n"
        + "#21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);\n"
        + "#22=PRE_DEFINED_COLOUR('yellow');\n"
        + "#23=DRAUGHTING_PRE_DEFINED_COLOUR('red');\n"
        + "#24=COLOUR_RGB('Amber',1.0,0.75,0.0);\n"
        + "#25=COLOUR_SPECIFICATION('amber-spec');\n"
        + "#26=COLOUR();\n"
        + "#27=PRE_DEFINED_CURVE_FONT('solid');\n"
        + "#28=DRAUGHTING_PRE_DEFINED_CURVE_FONT('chain');\n"
        + "#29=PRE_DEFINED_TEXT_FONT('iso');\n"
        + "#30=DRAUGHTING_PRE_DEFINED_TEXT_FONT('cadfont');\n"
        + "#31=PRE_DEFINED_TERMINATOR_SYMBOL('arrow');\n"
        + "#32=PRE_DEFINED_SYMBOL('sym');\n"
        + "#33=PRE_DEFINED_DIMENSION_SYMBOL('dim');\n"
        + "#34=PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL('tol');\n"
        + "#35=PRE_DEFINED_ITEM('pdi');\n"
        + "#36=PRE_DEFINED_MARKER('dot');\n"
        + "#40=PROPERTY_DEFINITION('PD0','',#22);\n"
        + "#41=PROPERTY_DEFINITION('PD1','',#23);\n"
        + "#42=PROPERTY_DEFINITION('PD2','',#24);\n"
        + "#43=PROPERTY_DEFINITION('PD3','',#25);\n"
        + "#44=PROPERTY_DEFINITION('PD4','',#26);\n"
        + "#45=PROPERTY_DEFINITION('PD5','',#27);\n"
        + "#46=PROPERTY_DEFINITION('PD6','',#28);\n"
        + "#47=PROPERTY_DEFINITION('PD7','',#29);\n"
        + "#48=PROPERTY_DEFINITION('PD8','',#30);\n"
        + "#49=PROPERTY_DEFINITION('PD9','',#31);\n"
        + "#50=PROPERTY_DEFINITION('PD10','',#32);\n"
        + "#51=PROPERTY_DEFINITION('PD11','',#33);\n"
        + "#52=PROPERTY_DEFINITION('PD12','',#34);\n"
        + "#53=PROPERTY_DEFINITION('PD13','',#35);\n"
        + "#54=PROPERTY_DEFINITION('PD14','',#36);\n"
        + "#60=PROPERTY_DEFINITION_REPRESENTATION(#40,#10);\n"
        + "#61=PROPERTY_DEFINITION_REPRESENTATION(#41,#10);\n"
        + "#62=PROPERTY_DEFINITION_REPRESENTATION(#42,#10);\n"
        + "#63=PROPERTY_DEFINITION_REPRESENTATION(#43,#10);\n"
        + "#64=PROPERTY_DEFINITION_REPRESENTATION(#44,#10);\n"
        + "#65=PROPERTY_DEFINITION_REPRESENTATION(#45,#10);\n"
        + "#66=PROPERTY_DEFINITION_REPRESENTATION(#46,#10);\n"
        + "#67=PROPERTY_DEFINITION_REPRESENTATION(#47,#10);\n"
        + "#68=PROPERTY_DEFINITION_REPRESENTATION(#48,#10);\n"
        + "#69=PROPERTY_DEFINITION_REPRESENTATION(#49,#10);\n"
        + "#70=PROPERTY_DEFINITION_REPRESENTATION(#50,#10);\n"
        + "#71=PROPERTY_DEFINITION_REPRESENTATION(#51,#10);\n"
        + "#72=PROPERTY_DEFINITION_REPRESENTATION(#52,#10);\n"
        + "#73=PROPERTY_DEFINITION_REPRESENTATION(#53,#10);\n"
        + "#74=PROPERTY_DEFINITION_REPRESENTATION(#54,#10);\n"
        + "#80=ANNOTATION_TEXT_OCCURRENCE('NOTE_PREDEF_COLOUR','',#13);\n"
        + "#81=ANNOTATION_TEXT_OCCURRENCE('NOTE_DRAUGHT_COLOUR','',#14);\n"
        + "#82=ANNOTATION_TEXT_OCCURRENCE('NOTE_RGB','',#13);\n"
        + "#83=ANNOTATION_TEXT_OCCURRENCE('NOTE_SPEC','',#14);\n"
        + "#84=ANNOTATION_TEXT_OCCURRENCE('NOTE_COLOUR','',#13);\n"
        + "#85=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRE_FONT','',#14);\n"
        + "#86=ANNOTATION_TEXT_OCCURRENCE('NOTE_DRAUGHT_FONT','',#13);\n"
        + "#87=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRE_TEXT_FONT','',#14);\n"
        + "#88=ANNOTATION_TEXT_OCCURRENCE('NOTE_DRAUGHT_TEXT_FONT','',#13);\n"
        + "#89=ANNOTATION_TEXT_OCCURRENCE('NOTE_TERM','',#14);\n"
        + "#90=ANNOTATION_TEXT_OCCURRENCE('NOTE_SYMBOL','',#13);\n"
        + "#91=ANNOTATION_TEXT_OCCURRENCE('NOTE_DIM_SYMBOL','',#14);\n"
        + "#92=ANNOTATION_TEXT_OCCURRENCE('NOTE_TOL_SYMBOL','',#13);\n"
        + "#93=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRE_ITEM','',#14);\n"
        + "#94=ANNOTATION_TEXT_OCCURRENCE('NOTE_MARKER','',#13);\n"
        + "#100=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#22,#10,#80,#8);\n"
        + "#101=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#23,#10,#81,#8);\n"
        + "#102=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#24,#10,#82,#8);\n"
        + "#103=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#25,#10,#83,#8);\n"
        + "#104=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#26,#10,#84,#8);\n"
        + "#105=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#27,#10,#85,#8);\n"
        + "#106=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#28,#10,#86,#8);\n"
        + "#107=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#29,#10,#87,#8);\n"
        + "#108=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#30,#10,#88,#8);\n"
        + "#109=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#31,#10,#89,#8);\n"
        + "#110=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#32,#10,#90,#8);\n"
        + "#111=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#33,#10,#91,#8);\n"
        + "#112=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#34,#10,#92,#8);\n"
        + "#113=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#35,#10,#93,#8);\n"
        + "#114=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#36,#10,#94,#8);\n"
        + "ENDSEC;"
        );
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"PRE_DEFINED_COLOUR\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"DRAUGHTING_PRE_DEFINED_COLOUR\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"COLOUR_RGB\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"COLOUR_SPECIFICATION\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"COLOUR\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"PRE_DEFINED_CURVE_FONT\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"DRAUGHTING_PRE_DEFINED_CURVE_FONT\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"PRE_DEFINED_TEXT_FONT\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"DRAUGHTING_PRE_DEFINED_TEXT_FONT\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"PRE_DEFINED_TERMINATOR_SYMBOL\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"PRE_DEFINED_SYMBOL\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"PRE_DEFINED_DIMENSION_SYMBOL\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"PRE_DEFINED_ITEM\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"PRE_DEFINED_MARKER\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionId\":54",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":60",
                "\"viaDefinitionId\":74",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"PRE_DEFINED_COLOUR\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"DRAUGHTING_PRE_DEFINED_COLOUR\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"COLOUR_RGB\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"COLOUR_SPECIFICATION\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"COLOUR\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"PRE_DEFINED_CURVE_FONT\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"DRAUGHTING_PRE_DEFINED_CURVE_FONT\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"PRE_DEFINED_TEXT_FONT\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"DRAUGHTING_PRE_DEFINED_TEXT_FONT\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"PRE_DEFINED_TERMINATOR_SYMBOL\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"PRE_DEFINED_SYMBOL\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"PRE_DEFINED_DIMENSION_SYMBOL\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL\"",
                "\"viaDefinitionId\":34",
                "\"viaDefinitionType\":\"PRE_DEFINED_ITEM\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"PRE_DEFINED_MARKER\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionId\":54",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":60",
                "\"viaDefinitionId\":74",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":19");
    }

    @Test
    void shouldEmbedDirectProductCategoryAndEffectivityDefinitionMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=PRODUCT_CATEGORY('CAT_A','cat a');\n"
        + "#9=PRODUCT_RELATED_PRODUCT_CATEGORY('CAT_LINK','',(#3));\n"
        + "#10=GENERAL_PROPERTY('GP-1','gp','general property');\n"
        + "#11=EFFECTIVITY('EFF-1');\n"
        + "#12=PRODUCT_DEFINITION_EFFECTIVITY('PDE-1','serial usage',#6);\n"
        + "#13=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#14=REPRESENTATION('REP_UPSTREAM',(),#13);\n"
        + "#20=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#21=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#22=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#23=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#24=CARTESIAN_POINT('P4',(4.0,0.0,0.0));\n"
        + "#25=CARTESIAN_POINT('P5',(5.0,0.0,0.0));\n"
        + "#26=CARTESIAN_POINT('P6',(6.0,0.0,0.0));\n"
        + "#27=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRODUCT','',#20);\n"
        + "#28=ANNOTATION_TEXT_OCCURRENCE('NOTE_FORMATION','',#21);\n"
        + "#29=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRODUCT_DEF','',#22);\n"
        + "#30=ANNOTATION_TEXT_OCCURRENCE('NOTE_PDS','',#23);\n"
        + "#31=ANNOTATION_TEXT_OCCURRENCE('NOTE_CATEGORY','',#24);\n"
        + "#32=ANNOTATION_TEXT_OCCURRENCE('NOTE_RELATED_CATEGORY','',#25);\n"
        + "#33=ANNOTATION_TEXT_OCCURRENCE('NOTE_GENERAL_PROPERTY','',#26);\n"
        + "#34=ANNOTATION_TEXT_OCCURRENCE('NOTE_EFFECTIVITY','',#20);\n"
        + "#35=ANNOTATION_TEXT_OCCURRENCE('NOTE_PDE','',#21);\n"
        + "#40=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#3,#14,#27,#7);\n"
        + "#41=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#4,#14,#28,#7);\n"
        + "#42=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#6,#14,#29,#7);\n"
        + "#43=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#7,#14,#30,#7);\n"
        + "#44=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#8,#14,#31,#7);\n"
        + "#45=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#9,#14,#32,#7);\n"
        + "#46=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#10,#14,#33,#7);\n"
        + "#47=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#11,#14,#34,#7);\n"
        + "#48=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#12,#14,#35,#7);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"viaDefinitionType\":\"PRODUCT\"",
                "\"viaDefinitionId\":3",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_FORMATION\"",
                "\"viaDefinitionId\":4",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION\"",
                "\"viaDefinitionId\":6",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_SHAPE\"",
                "\"viaDefinitionId\":7",
                "\"viaDefinitionType\":\"PRODUCT_CATEGORY\"",
                "\"viaDefinitionId\":8",
                "\"viaDefinitionType\":\"PRODUCT_RELATED_PRODUCT_CATEGORY\"",
                "\"viaDefinitionId\":9",
                "\"viaDefinitionType\":\"GENERAL_PROPERTY\"",
                "\"viaDefinitionId\":10",
                "\"viaDefinitionType\":\"EFFECTIVITY\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_EFFECTIVITY\"",
                "\"viaDefinitionId\":12");
        assertMetadataContains(glbMetadata,
                "\"viaDefinitionType\":\"PRODUCT\"",
                "\"viaDefinitionId\":3",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_FORMATION\"",
                "\"viaDefinitionId\":4",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION\"",
                "\"viaDefinitionId\":6",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_SHAPE\"",
                "\"viaDefinitionId\":7",
                "\"viaDefinitionType\":\"PRODUCT_CATEGORY\"",
                "\"viaDefinitionId\":8",
                "\"viaDefinitionType\":\"PRODUCT_RELATED_PRODUCT_CATEGORY\"",
                "\"viaDefinitionId\":9",
                "\"viaDefinitionType\":\"GENERAL_PROPERTY\"",
                "\"viaDefinitionId\":10",
                "\"viaDefinitionType\":\"EFFECTIVITY\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_EFFECTIVITY\"",
                "\"viaDefinitionId\":12");
    }

    @Test
    void shouldEmbedGeneralPropertyAndShapeAspectRelationshipMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=GENERAL_PROPERTY('GP1','gp1','');\n"
        + "#9=GENERAL_PROPERTY('GP2','gp2','');\n"
        + "#10=GENERAL_PROPERTY_RELATIONSHIP('LINK','',#8,#9);\n"
        + "#11=PROPERTY_DEFINITION('PD_GP2','',#9);\n"
        + "#12=SHAPE_ASPECT('SA_BASE','base',#7,.T.);\n"
        + "#13=SHAPE_ASPECT('SA_TARGET','target',#7,.T.);\n"
        + "#14=SHAPE_ASPECT_RELATIONSHIP('SAR','',#12,#13);\n"
        + "#15=PROPERTY_DEFINITION('PD_TARGET','',#13);\n"
        + "#16=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#17=REPRESENTATION('REP_GP',(),#16);\n"
        + "#18=REPRESENTATION('REP_SA',(),#16);\n"
        + "#19=ACTION_PROPERTY_REPRESENTATION(#11,#17);\n"
        + "#20=PROPERTY_DEFINITION_REPRESENTATION(#15,#18);\n"
        + "#21=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#22=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#23=ANNOTATION_TEXT_OCCURRENCE('NOTE_GP','',#21);\n"
        + "#24=ANNOTATION_TEXT_OCCURRENCE('NOTE_SA','',#22);\n"
        + "#25=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_GP','',#8,#17,#23,#7);\n"
        + "#26=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_SA','',#12,#18,#24,#7);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_GP\"",
                "\"name\":\"NOTE_SA\"",
                "\"id\":17,\"type\":\"representation\",\"name\":\"REP_GP\"",
                "\"id\":18,\"type\":\"representation\",\"name\":\"REP_SA\"",
                "\"viaDefinitionType\":\"GENERAL_PROPERTY_RELATIONSHIP\"",
                "\"viaDefinitionId\":10",
                "\"viaDefinitionType\":\"SHAPE_ASPECT_RELATIONSHIP\"",
                "\"viaDefinitionId\":14");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_GP\"",
                "\"name\":\"NOTE_SA\"",
                "\"id\":17,\"type\":\"representation\",\"name\":\"REP_GP\"",
                "\"id\":18,\"type\":\"representation\",\"name\":\"REP_SA\"",
                "\"viaDefinitionType\":\"GENERAL_PROPERTY_RELATIONSHIP\"",
                "\"viaDefinitionId\":10",
                "\"viaDefinitionType\":\"SHAPE_ASPECT_RELATIONSHIP\"",
                "\"viaDefinitionId\":14");
    }

    @Test
    void shouldEmbedRequirementSemanticDefinitionSubtypeMetadataInBinaryPreviewAndGlb() {
        String step = 
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('PRT','Part','Part',(#2));\n"
        + "#4=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#6=PRODUCT_DEFINITION('pd','part def',#4,#5);\n"
        + "#7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);\n"
        + "#8=SHAPE_ASPECT('SA_BASE','base',#7,.T.);\n"
        + "#9=SPOTFACE_HOLE_OCCURRENCE('SA_OCC','occurrence',#7,.T.,#8);\n"
        + "#10=PROPERTY_DEFINITION('PD_OCC','',#9);\n"
        + "#11=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#12=REPRESENTATION('REP_REQ',(),#11);\n"
        + "#13=PROPERTY_DEFINITION_REPRESENTATION(#10,#12);\n"
        + "#14=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#15=ANNOTATION_TEXT_OCCURRENCE('NOTE_REQ','',#14);\n"
        + "#16=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_REQ','',#8,#12,#15,#9);\n"
        + "ENDSEC;";
        String binaryMetadata = metadataFromBinary(StepPreviewJsonExporter.exportBinary(step));
        String glbMetadata = metadataFromGlb(StepPreviewJsonExporter.exportGlb(step));

        assertMetadataContains(binaryMetadata,
                "\"name\":\"NOTE_REQ\"",
                "\"id\":12,\"type\":\"representation\",\"name\":\"REP_REQ\"",
                "\"viaDefinitionType\":\"SPOTFACE_HOLE_OCCURRENCE\"",
                "\"viaDefinitionId\":9");
        assertMetadataContains(glbMetadata,
                "\"name\":\"NOTE_REQ\"",
                "\"id\":12,\"type\":\"representation\",\"name\":\"REP_REQ\"",
                "\"viaDefinitionType\":\"SPOTFACE_HOLE_OCCURRENCE\"",
                "\"viaDefinitionId\":9");
    }

    @Test
    void shouldEmbedParametricSurfaceMetadataInBinaryPreviewForBsplineFaces() throws Exception {
        byte[] binary = StepPreviewJsonExporter.exportBinary(Files.readString(Path.of("examples/bspline-patch.step")));
        String metadata = metadataFromBinary(binary);

        assertMetadataContains(metadata,
                "\"format\":\"binary-preview-v1\"",
                "\"faces\":[{\"id\":",
                "\"surface\":{\"type\":\"bspline_surface\"",
                "\"uDegree\":",
                "\"vDegree\":",
                "\"controlPoints\":",
                "\"surfaceUvLoops\":[");
    }

    @Test
    void shouldEmbedParametricSurfaceMetadataInBinaryPreviewForCylindricalFaces() throws Exception {
        byte[] binary = StepPreviewJsonExporter.exportBinary(Files.readString(Path.of("examples/cylindrical-band.step")));
        String metadata = metadataFromBinary(binary);

        assertMetadataContains(metadata,
                "\"surface\":{\"type\":\"cylindrical_strip\"",
                "\"radius\":",
                "\"lowerHeight\":",
                "\"upperHeight\":");
    }

    @Test
    void shouldEmbedSurfaceWrapperMetadataInBinaryPreviewForOffsetSurface() {
        byte[] binary = StepPreviewJsonExporter.exportBinary(surfaceOfRevolutionFaceStep(
                "#11=OFFSET_SURFACE('OFS0',#10,1.0,.F.);",
                "#11",
                "2.0,0.0,0.0",
                "-2.0,0.0,0.0",
                "-2.0,0.0,1.0",
                "2.0,0.0,1.0"
        ));
        String metadata = metadataFromBinary(binary);

        assertMetadataContains(metadata,
                "\"surfaceType\":\"OFFSET_SURFACE\"",
                "\"surface\":{\"type\":\"surface_of_revolution\"",
                "\"sourceType\":\"OFFSET_SURFACE\"",
                "\"sourceStepId\":11",
                "\"basisType\":\"SURFACE_OF_REVOLUTION\"",
                "\"basisStepId\":10",
                "\"offsetDistance\":1.0");
    }

    @Test
    void shouldEmbedSurfaceWrapperMetadataInBinaryPreviewForSurfaceReplica() {
        byte[] binary = StepPreviewJsonExporter.exportBinary(surfaceOfRevolutionFaceStep(
                
        "#100=CARTESIAN_POINT('T0',(0.0,0.0,2.0));\n"
        + "#101=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#100,1.0,$);\n"
        + "#102=SURFACE_REPLICA('SR0',#10,#101);"
                "#102",
                "1.0,0.0,2.0",
                "-1.0,0.0,2.0",
                "-1.0,0.0,3.0",
                "1.0,0.0,3.0"
        ));
        String metadata = metadataFromBinary(binary);

        assertMetadataContains(metadata,
                "\"surfaceType\":\"SURFACE_REPLICA\"",
                "\"surface\":{\"type\":\"surface_of_revolution\"",
                "\"sourceType\":\"SURFACE_REPLICA\"",
                "\"sourceStepId\":102",
                "\"basisType\":\"SURFACE_OF_REVOLUTION\"",
                "\"basisStepId\":10",
                "\"transformScale\":1.0");
    }

    @Test
    void shouldEmbedSurfaceWrapperMetadataInBinaryPreviewForOrientedSurface() {
        byte[] binary = StepPreviewJsonExporter.exportBinary(surfaceOfRevolutionFaceStep(
                "#11=ORIENTED_SURFACE('OS0',#10,.T.);",
                "#11",
                "2.0,0.0,0.0",
                "-2.0,0.0,0.0",
                "-2.0,0.0,1.0",
                "2.0,0.0,1.0"
        ));
        String metadata = metadataFromBinary(binary);

        assertMetadataContains(metadata,
                "\"surfaceType\":\"ORIENTED_SURFACE\"",
                "\"surface\":{\"type\":\"surface_of_revolution\"",
                "\"sourceType\":\"ORIENTED_SURFACE\"",
                "\"sourceStepId\":11",
                "\"basisType\":\"SURFACE_OF_REVOLUTION\"",
                "\"basisStepId\":10",
                "\"orientation\":true");
    }

    @Test
    void shouldEmbedSurfaceWrapperMetadataInBinaryPreviewForRectangularTrimmedSurface() {
        byte[] binary = StepPreviewJsonExporter.exportBinary(surfaceOfRevolutionFaceStep(
                "#11=RECTANGULAR_TRIMMED_SURFACE('RTS0',#10,0.0,3.141592653589793,0.0,1.0,.T.,.T.);",
                "#11",
                "2.0,0.0,0.0",
                "-2.0,0.0,0.0",
                "-2.0,0.0,1.0",
                "2.0,0.0,1.0"
        ));
        String metadata = metadataFromBinary(binary);

        assertMetadataContains(metadata,
                "\"surfaceType\":\"RECTANGULAR_TRIMMED_SURFACE\"",
                "\"surface\":{\"type\":\"surface_of_revolution\"",
                "\"sourceType\":\"RECTANGULAR_TRIMMED_SURFACE\"",
                "\"sourceStepId\":11",
                "\"basisType\":\"SURFACE_OF_REVOLUTION\"",
                "\"basisStepId\":10",
                "\"trimU1\":0.0",
                "\"trimU2\":3.141592653589793",
                "\"trimV1\":0.0",
                "\"trimV2\":1.0");
    }

    @Test
    void shouldEmbedSurfaceWrapperMetadataInBinaryPreviewForCurveBoundedSurface() {
        byte[] binary = StepPreviewJsonExporter.exportBinary(surfaceOfRevolutionFaceStep(
                "#11=CURVE_BOUNDED_SURFACE('CBS0',#10,(#30),.T.);",
                "#11",
                "2.0,0.0,0.0",
                "-2.0,0.0,0.0",
                "-2.0,0.0,1.0",
                "2.0,0.0,1.0"
        ));
        String metadata = metadataFromBinary(binary);

        assertMetadataContains(metadata,
                "\"surfaceType\":\"CURVE_BOUNDED_SURFACE\"",
                "\"surface\":{\"type\":\"surface_of_revolution\"",
                "\"sourceType\":\"CURVE_BOUNDED_SURFACE\"",
                "\"sourceStepId\":11",
                "\"basisType\":\"SURFACE_OF_REVOLUTION\"",
                "\"basisStepId\":10",
                "\"implicitOuter\":true");
    }

    @Test
    void shouldEmbedSurfaceWrapperMetadataInGlbExtrasForOffsetSurface() {
        byte[] binary = StepPreviewJsonExporter.exportGlb(surfaceOfRevolutionFaceStep(
                "#11=OFFSET_SURFACE('OFS0',#10,1.0,.F.);",
                "#11",
                "2.0,0.0,0.0",
                "-2.0,0.0,0.0",
                "-2.0,0.0,1.0",
                "2.0,0.0,1.0"
        ));
        String metadata = metadataFromGlb(binary);

        assertMetadataContains(metadata,
                "\"kind\":\"face\"",
                "\"stepId\":50",
                "\"surface\":{\"type\":\"surface_of_revolution\"",
                "\"sourceType\":\"OFFSET_SURFACE\"",
                "\"sourceStepId\":11",
                "\"basisType\":\"SURFACE_OF_REVOLUTION\"",
                "\"basisStepId\":10",
                "\"offsetDistance\":1.0");
    }

    @Test
    void shouldEmbedSurfaceWrapperMetadataInGlbExtrasForOrientedSurface() {
        byte[] binary = StepPreviewJsonExporter.exportGlb(surfaceOfRevolutionFaceStep(
                "#11=ORIENTED_SURFACE('OS0',#10,.T.);",
                "#11",
                "2.0,0.0,0.0",
                "-2.0,0.0,0.0",
                "-2.0,0.0,1.0",
                "2.0,0.0,1.0"
        ));
        String metadata = metadataFromGlb(binary);

        assertMetadataContains(metadata,
                "\"kind\":\"face\"",
                "\"stepId\":50",
                "\"surface\":{\"type\":\"surface_of_revolution\"",
                "\"sourceType\":\"ORIENTED_SURFACE\"",
                "\"sourceStepId\":11",
                "\"basisType\":\"SURFACE_OF_REVOLUTION\"",
                "\"basisStepId\":10",
                "\"orientation\":true");
    }

    @Test
    void shouldEmbedSurfaceWrapperMetadataInGlbExtrasForRectangularTrimmedSurface() {
        byte[] binary = StepPreviewJsonExporter.exportGlb(surfaceOfRevolutionFaceStep(
                "#11=RECTANGULAR_TRIMMED_SURFACE('RTS0',#10,0.0,3.141592653589793,0.0,1.0,.T.,.T.);",
                "#11",
                "2.0,0.0,0.0",
                "-2.0,0.0,0.0",
                "-2.0,0.0,1.0",
                "2.0,0.0,1.0"
        ));
        String metadata = metadataFromGlb(binary);

        assertMetadataContains(metadata,
                "\"kind\":\"face\"",
                "\"stepId\":50",
                "\"surface\":{\"type\":\"surface_of_revolution\"",
                "\"sourceType\":\"RECTANGULAR_TRIMMED_SURFACE\"",
                "\"sourceStepId\":11",
                "\"basisType\":\"SURFACE_OF_REVOLUTION\"",
                "\"basisStepId\":10",
                "\"trimU1\":0.0",
                "\"trimU2\":3.141592653589793",
                "\"trimV1\":0.0",
                "\"trimV2\":1.0");
    }

    @Test
    void shouldEmbedSurfaceWrapperMetadataInGlbExtrasForCurveBoundedSurface() {
        byte[] binary = StepPreviewJsonExporter.exportGlb(surfaceOfRevolutionFaceStep(
                "#11=CURVE_BOUNDED_SURFACE('CBS0',#10,(#30),.T.);",
                "#11",
                "2.0,0.0,0.0",
                "-2.0,0.0,0.0",
                "-2.0,0.0,1.0",
                "2.0,0.0,1.0"
        ));
        String metadata = metadataFromGlb(binary);

        assertMetadataContains(metadata,
                "\"kind\":\"face\"",
                "\"stepId\":50",
                "\"surface\":{\"type\":\"surface_of_revolution\"",
                "\"sourceType\":\"CURVE_BOUNDED_SURFACE\"",
                "\"sourceStepId\":11",
                "\"basisType\":\"SURFACE_OF_REVOLUTION\"",
                "\"basisStepId\":10",
                "\"implicitOuter\":true");
    }

    @Test
    void shouldEmbedSurfaceWrapperMetadataInGlbExtrasForSurfaceReplica() {
        byte[] binary = StepPreviewJsonExporter.exportGlb(surfaceOfRevolutionFaceStep(
                
        "#100=CARTESIAN_POINT('T0',(0.0,0.0,2.0));\n"
        + "#101=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#100,1.0,$);\n"
        + "#102=SURFACE_REPLICA('SR0',#10,#101);"
                "#102",
                "1.0,0.0,2.0",
                "-1.0,0.0,2.0",
                "-1.0,0.0,3.0",
                "1.0,0.0,3.0"
        ));
        String metadata = metadataFromGlb(binary);

        assertMetadataContains(metadata,
                "\"kind\":\"face\"",
                "\"stepId\":50",
                "\"surface\":{\"type\":\"surface_of_revolution\"",
                "\"sourceType\":\"SURFACE_REPLICA\"",
                "\"sourceStepId\":102",
                "\"basisType\":\"SURFACE_OF_REVOLUTION\"",
                "\"basisStepId\":10",
                "\"transformScale\":1.0");
    }

    private static String metadataFromGlb(byte[] binary) {
        ByteBuffer header = ByteBuffer.wrap(binary).order(ByteOrder.LITTLE_ENDIAN);
        int jsonChunkLength = header.getInt(12);
        return new String(binary, 20, jsonChunkLength, StandardCharsets.UTF_8).trim();
    }

    private static ByteBuffer glbBinChunk(byte[] binary) {
        ByteBuffer header = ByteBuffer.wrap(binary).order(ByteOrder.LITTLE_ENDIAN);
        int jsonChunkLength = header.getInt(12);
        int binHeaderOffset = 20 + jsonChunkLength;
        int binLength = header.getInt(binHeaderOffset);
        int binType = header.getInt(binHeaderOffset + 4);
        assertEquals(0x004E4942, binType);
        return ByteBuffer.wrap(binary, binHeaderOffset + 8, binLength).slice().order(ByteOrder.LITTLE_ENDIAN);
    }

    private static float[] readVec3Accessor(ByteBuffer bin, JSONObject gltf, JSONObject accessor) {
        JSONArray bufferViews = gltf.getJSONArray("bufferViews");
        JSONObject bufferView = bufferViews.getJSONObject(accessor.getIntValue("bufferView"));
        int offset = bufferView.getIntValue("byteOffset") + accessor.getIntValue("byteOffset");
        int count = accessor.getIntValue("count");
        float[] values = new float[count * 3];
        ByteBuffer view = bin.duplicate().order(ByteOrder.LITTLE_ENDIAN);
        view.position(offset);
        for (int i = 0; i < values.length; i++) {
            values[i] = view.getFloat();
        }
        return values;
    }

    private static PreviewPayload nonEmptyTrianglePreviewPayload() {
        PointPayload p0 = new PointPayload(0.0, 0.0, 0.0);
        PointPayload p1 = new PointPayload(1.0, 0.0, 0.0);
        PointPayload p2 = new PointPayload(1.0, 1.0, 0.0);
        PointPayload p3 = new PointPayload(0.0, 1.0, 0.0);
        FacePayload face = new FacePayload(
                42,
                "square",
                "plane",
                p0,
                new VectorPayload(0.0, 0.0, 1.0),
                true,
                new ColorPayload(180, 110, 70),
                0.0,
                null,
                List.of(),
                List.of(new LoopPayload(true, List.of(p0, p1, p2, p3, p0))),
                List.of(p0, p1, p2, p0, p2, p3),
                null,
                List.of()
        );
        ValidationReportPayload report = new ValidationReportPayload("ok", 0, 0, List.of());
        ValidationPayload validation = new ValidationPayload(
                0,
                0,
                1,
                0,
                1.0,
                0.0,
                new PointPayload(0.5, 0.5, 0.0),
                report
        );
        return new PreviewPayload(
                new PreviewStats(1, 0, 0, 1, 0, 0, 0),
                new BoundsPayload(p0, p2),
                validation,
                null,
                null,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(face),
                List.of(),
                List.of()
        );
    }

    private static String metadataFromBinary(byte[] binary) {
        ByteBuffer header = ByteBuffer.wrap(binary).order(ByteOrder.LITTLE_ENDIAN);
        int metadataLength = header.getInt(8);
        return new String(binary, 16, metadataLength, StandardCharsets.UTF_8).trim();
    }

    private static void writeIntLE(byte[] bytes, int offset, int value) {
        bytes[offset] = (byte) (value & 0xff);
        bytes[offset + 1] = (byte) ((value >>> 8) & 0xff);
        bytes[offset + 2] = (byte) ((value >>> 16) & 0xff);
        bytes[offset + 3] = (byte) ((value >>> 24) & 0xff);
    }

    private static String surfaceOfRevolutionFaceStep(
            String surfaceDeclarations,
            String faceGeometryRef,
            String p00,
            String p10,
            String p11,
            String p01
    ) {
        return 
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('B0',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('B1',(1.0,0.0,1.0));\n"
        + "#4=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#5=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#6=AXIS1_PLACEMENT('AX1',#1,#4);\n"
        + "#7=VECTOR('VZ',#4,1.0);\n"
        + "#8=LINE('GEN',#2,#7);\n"
        + "#9=AXIS2_PLACEMENT_3D('AC0',#1,#4,#5);\n"
        + "#10=SURFACE_OF_REVOLUTION('SOR0',#8,#6);\n"
        + "%s\n"
        + "#12=CARTESIAN_POINT('P00',(%s));\n"
        + "#13=CARTESIAN_POINT('P10',(%s));\n"
        + "#14=CARTESIAN_POINT('P11',(%s));\n"
        + "#15=CARTESIAN_POINT('P01',(%s));\n"
        + "#16=VERTEX_POINT('V0',#12);\n"
        + "#17=VERTEX_POINT('V1',#13);\n"
        + "#18=VERTEX_POINT('V2',#14);\n"
        + "#19=VERTEX_POINT('V3',#15);\n"
        + "#20=AXIS2_PLACEMENT_3D('AC1',#3,#4,#5);\n"
        + "#30=CIRCLE('C0',#9,1.0);\n"
        + "#31=LINE('L0',#13,#7);\n"
        + "#32=CIRCLE('C1',#20,1.0);\n"
        + "#33=LINE('L1',#12,#7);\n"
        + "#40=EDGE_CURVE('E0',#16,#17,#30,.T.);\n"
        + "#41=EDGE_CURVE('E1',#17,#18,#31,.T.);\n"
        + "#42=EDGE_CURVE('E2',#19,#18,#32,.T.);\n"
        + "#43=EDGE_CURVE('E3',#16,#19,#33,.T.);\n"
        + "#44=ORIENTED_EDGE('OE0',$,$,#40,.T.);\n"
        + "#45=ORIENTED_EDGE('OE1',$,$,#41,.T.);\n"
        + "#46=ORIENTED_EDGE('OE2',$,$,#42,.F.);\n"
        + "#47=ORIENTED_EDGE('OE3',$,$,#43,.F.);\n"
        + "#48=EDGE_LOOP('L0',(#44,#45,#46,#47));\n"
        + "#49=FACE_OUTER_BOUND('B0',#48,.T.);\n"
        + "#50=ADVANCED_FACE('F0',(#49),%s,.T.);\n"
        + "#51=OPEN_SHELL('OS',(#50));\n"
        + "ENDSEC;"
        );
    }

    private static void assertMetadataContains(String metadata, String... fragments) {
        for (String fragment : fragments) {
            assertTrue(metadata.contains(fragment), metadata);
        }
    }

    @Test
    void shouldExportGlbForEngineStp() throws Exception {
        byte[] raw = Files.readAllBytes(Path.of("examples/engine.stp"));
        String stepText = new String(raw, java.nio.charset.StandardCharsets.ISO_8859_1);
        byte[] binary = StepPreviewJsonExporter.exportGlb(stepText);
        String metadata = metadataFromGlb(binary);
        // engine.stp: 93K entities, 31 solids, small number of unsupported faces relative to total
        assertTrue(metadata.contains("\"entityCount\":93829"), "engine.stp should have 93829 entities");
        assertTrue(metadata.contains("\"solidCount\":31"), "engine.stp should have 31 solids");
    }

    @Test
    void shouldExportGlbForFanStp() throws Exception {
        byte[] binary = StepPreviewJsonExporter.exportGlb(Files.readString(Path.of("examples/fan.stp")));
        String metadata = metadataFromGlb(binary);
        // fan.stp: 42K entities, 1 solid, 1 unsupported face from assembly representation
        assertTrue(metadata.contains("\"entityCount\":41905"), "fan.stp should have 41905 entities");
        assertTrue(metadata.contains("\"solidCount\":1"), "fan.stp should have 1 solid");
    }

    @Test
    void shouldExportJsonForToroidalSeamWithZeroUnsupportedFaces() throws Exception {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/toroidal-seam-two-holes.step")));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), "toroidal-seam should have 0 unsupported faces");
        assertTrue(json.contains("\"product\":"), "should include product metadata");
        assertTrue(json.contains("\"units\":"), "should include unit metadata");
    }

    @Test
    void shouldExportInchUnitScaleInJsonMetadata() {
        String json = StepPreviewJsonExporter.export(
        "DATA;\n"
        + "#1=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));\n"
        + "#2=MEASURE_WITH_UNIT(LENGTH_MEASURE(25.4),#1);\n"
        + "#3=(CONVERSION_BASED_UNIT('INCH',#2) NAMED_UNIT(*) LENGTH_UNIT());\n"
        + "#4=(GEOMETRIC_REPRESENTATION_CONTEXT(3)\n"
        + "    GLOBAL_UNIT_ASSIGNED_CONTEXT((#3))\n"
        + "    REPRESENTATION_CONTEXT('ID','MODEL'));\n"
        + "ENDSEC;"
        );

        assertTrue(json.contains("\"units\":{\"lengthUnit\":\"INCH\",\"scaleToMeters\":0.0254"), json);
        assertTrue(json.contains("\"code\":\"units.coordinates_not_normalized\""), json);
    }

    @Test
    void shouldKeepAssemblyTransformsInSourceUnitsWhenLengthUnitsAreNotNormalized() {
        String json = StepPreviewJsonExporter.export(
        "DATA;\n"
        + "#1=APPLICATION_CONTEXT('mechanical design');\n"
        + "#2=PRODUCT_CONTEXT('part definition','mechanical',#1);\n"
        + "#3=PRODUCT('ASM','Assembly','Assembly',(#2));\n"
        + "#4=PRODUCT('COMP','Component','Component',(#2));\n"
        + "#5=PRODUCT_DEFINITION_FORMATION('v1','',#3);\n"
        + "#6=PRODUCT_DEFINITION_FORMATION('v1','',#4);\n"
        + "#7=PRODUCT_DEFINITION_CONTEXT('design','released',#1);\n"
        + "#8=PRODUCT_DEFINITION('asm_pd','assembly',#5,#7);\n"
        + "#9=PRODUCT_DEFINITION('comp_pd','component',#6,#7);\n"
        + "#10=PRODUCT_DEFINITION_SHAPE('asm_shape','',#8);\n"
        + "#11=PRODUCT_DEFINITION_SHAPE('comp_shape','',#9);\n"
        + "#12=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));\n"
        + "#13=MEASURE_WITH_UNIT(LENGTH_MEASURE(25.4),#12);\n"
        + "#14=(CONVERSION_BASED_UNIT('INCH',#13) NAMED_UNIT(*) LENGTH_UNIT());\n"
        + "#15=(GEOMETRIC_REPRESENTATION_CONTEXT(3)\n"
        + "    GLOBAL_UNIT_ASSIGNED_CONTEXT((#14))\n"
        + "    REPRESENTATION_CONTEXT('ID','CTX'));\n"
        + "#16=SHAPE_REPRESENTATION('ASM_REP',(),#15);\n"
        + "#17=SHAPE_REPRESENTATION('COMP_REP',(),#15);\n"
        + "#18=SHAPE_DEFINITION_REPRESENTATION(#10,#16);\n"
        + "#19=SHAPE_DEFINITION_REPRESENTATION(#11,#17);\n"
        + "#20=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#21=CARTESIAN_POINT('T',(1.0,0.0,0.0));\n"
        + "#22=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#23=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#24=AXIS2_PLACEMENT_3D('AX0',#20,#22,#23);\n"
        + "#25=AXIS2_PLACEMENT_3D('AX1',#21,#22,#23);\n"
        + "#26=ITEM_DEFINED_TRANSFORMATION('T1','',#24,#25);\n"
        + "#27=(REPRESENTATION_RELATIONSHIP('CTX','occ ctx',#16,#17)\n"
        + "     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#26));\n"
        + "#28=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ-1','OCC','component usage',#8,#9,'R1');\n"
        + "#29=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#27,#28);\n"
        + "ENDSEC;"
        );

        JSONObject payload = JSONObject.parseObject(json);
        JSONObject units = payload.getJSONObject("units");
        JSONArray instances = payload.getJSONArray("instances");
        JSONObject child = instances.stream()
                .map(JSONObject.class::cast)
                .filter(instance -> Integer.valueOf(28).equals(instance.getInteger("occurrenceId")))
                .findFirst()
                .orElseThrow();
        JSONArray matrix = child.getJSONArray("matrix");

        assertEquals("INCH", units.getString("lengthUnit"));
        assertEquals(0.0254, units.getDoubleValue("scaleToMeters"), 1.0e-12);
        assertEquals(1.0, matrix.getDoubleValue(3), 1.0e-12);
        assertTrue(json.contains("\"code\":\"units.coordinates_not_normalized\""), json);
    }
}
