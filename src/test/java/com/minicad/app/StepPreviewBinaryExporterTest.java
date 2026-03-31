package com.minicad.app;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StepPreviewBinaryExporterTest {

    @Test
    void shouldExportGlbPreviewPacketForMinimalSquare() {
        byte[] binary = StepPreviewJsonExporter.exportGlb("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AXIS',#1,#10,#11);
                #13=PLANE('PL0',#12);
                #20=VERTEX_POINT('V0',#1);
                #21=VERTEX_POINT('V1',#2);
                #22=VERTEX_POINT('V2',#3);
                #23=VERTEX_POINT('V3',#4);
                #30=DIRECTION('D1',(1.0,0.0,0.0));
                #31=VECTOR('VE1',#30,1.0);
                #32=LINE('L1',#1,#31);
                #33=DIRECTION('D2',(0.0,1.0,0.0));
                #34=VECTOR('VE2',#33,1.0);
                #35=LINE('L2',#2,#34);
                #36=DIRECTION('D3',(-1.0,0.0,0.0));
                #37=VECTOR('VE3',#36,1.0);
                #38=LINE('L3',#3,#37);
                #39=DIRECTION('D4',(0.0,-1.0,0.0));
                #40=VECTOR('VE4',#39,1.0);
                #41=LINE('L4',#4,#40);
                #50=EDGE_CURVE('E1',#20,#21,#32,.T.);
                #51=EDGE_CURVE('E2',#21,#22,#35,.T.);
                #52=EDGE_CURVE('E3',#22,#23,#38,.T.);
                #53=EDGE_CURVE('E4',#23,#20,#41,.T.);
                #60=ORIENTED_EDGE('OE1',$,$,#50,.T.);
                #61=ORIENTED_EDGE('OE2',$,$,#51,.T.);
                #62=ORIENTED_EDGE('OE3',$,$,#52,.T.);
                #63=ORIENTED_EDGE('OE4',$,$,#53,.T.);
                #70=EDGE_LOOP('LOOP',(#60,#61,#62,#63));
                #71=FACE_OUTER_BOUND('FOB',#70,.T.);
                #80=ADVANCED_FACE('F0',(#71),#13,.T.);
                #90=CLOSED_SHELL('CS',(#80));
                #100=MANIFOLD_SOLID_BREP('S0',#90);
                ENDSEC;
                """);

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
        String metadata = new String(binary, 20, jsonChunkLength, StandardCharsets.UTF_8).trim();

        assertEquals(binary.length, totalLength);
        assertEquals(0x4E4F534A, jsonChunkType);
        assertTrue(metadata.contains("\"version\":\"2.0\""));
        assertTrue(metadata.contains("\"scenes\":["));
        assertTrue(metadata.contains("\"preview\":{"));
        assertTrue(metadata.contains("\"faceCount\":1"));
        assertTrue(metadata.contains("\"edgeCount\":4"));
        assertTrue(metadata.contains("\"surface\":{\"type\":\"plane_face\""));
        assertTrue(metadata.contains("\"surfaceLoops\":["));
        assertTrue(metadata.contains("\"meshes\":["));
        assertTrue(metadata.contains("\"materials\":["));
    }

    @Test
    void shouldEmbedParametricCircleMetadataForRoundEdges() throws Exception {
        byte[] binary = StepPreviewJsonExporter.exportGlb(Files.readString(Path.of("examples/plate-with-round-hole.step")));

        ByteBuffer header = ByteBuffer.wrap(binary).order(ByteOrder.LITTLE_ENDIAN);
        int jsonChunkLength = header.getInt(12);
        String metadata = new String(binary, 20, jsonChunkLength, StandardCharsets.UTF_8).trim();

        assertTrue(metadata.contains("\"curve\":{\"type\":\"circle_arc\""));
        assertTrue(metadata.contains("\"radius\":"));
        assertTrue(metadata.contains("\"sweepAngle\":"));
        assertTrue(metadata.contains("\"xDirection\":"));
    }

    @Test
    void shouldEmbedParametricCylinderMetadataForCylindricalFaces() throws Exception {
        byte[] binary = StepPreviewJsonExporter.exportGlb(Files.readString(Path.of("examples/cylindrical-band.step")));

        ByteBuffer header = ByteBuffer.wrap(binary).order(ByteOrder.LITTLE_ENDIAN);
        int jsonChunkLength = header.getInt(12);
        String metadata = new String(binary, 20, jsonChunkLength, StandardCharsets.UTF_8).trim();

        assertTrue(metadata.contains("\"surface\":{\"type\":\"cylindrical_strip\""));
        assertTrue(metadata.contains("\"radius\":"));
        assertTrue(metadata.contains("\"lowerHeight\":"));
        assertTrue(metadata.contains("\"upperHeight\":"));
    }

    @Test
    void shouldEmbedParametricConeMetadataForConicalFaces() throws Exception {
        byte[] binary = StepPreviewJsonExporter.exportGlb(Files.readString(Path.of("examples/conical-band.step")));

        ByteBuffer header = ByteBuffer.wrap(binary).order(ByteOrder.LITTLE_ENDIAN);
        int jsonChunkLength = header.getInt(12);
        String metadata = new String(binary, 20, jsonChunkLength, StandardCharsets.UTF_8).trim();

        assertTrue(metadata.contains("\"surface\":{\"type\":\"conical_strip\""));
        assertTrue(metadata.contains("\"semiAngle\":"));
        assertTrue(metadata.contains("\"lowerHeight\":"));
        assertTrue(metadata.contains("\"upperHeight\":"));
    }

    @Test
    void shouldEmbedParametricTorusMetadataForToroidalFaces() throws Exception {
        byte[] binary = StepPreviewJsonExporter.exportGlb(Files.readString(Path.of("examples/toroidal-band.step")));

        ByteBuffer header = ByteBuffer.wrap(binary).order(ByteOrder.LITTLE_ENDIAN);
        int jsonChunkLength = header.getInt(12);
        String metadata = new String(binary, 20, jsonChunkLength, StandardCharsets.UTF_8).trim();

        assertTrue(metadata.contains("\"surface\":{\"type\":\"toroidal_strip\""));
        assertTrue(metadata.contains("\"minorRadius\":"));
        assertTrue(metadata.contains("\"lowerHeight\":"));
        assertTrue(metadata.contains("\"upperHeight\":"));
    }

    @Test
    void shouldEmbedParametricBsplineMetadataForBsplineFaces() throws Exception {
        byte[] binary = StepPreviewJsonExporter.exportGlb(Files.readString(Path.of("examples/bspline-patch.step")));

        ByteBuffer header = ByteBuffer.wrap(binary).order(ByteOrder.LITTLE_ENDIAN);
        int jsonChunkLength = header.getInt(12);
        String metadata = new String(binary, 20, jsonChunkLength, StandardCharsets.UTF_8).trim();

        assertTrue(metadata.contains("\"surface\":{\"type\":\"bspline_surface\""));
        assertTrue(metadata.contains("\"uDegree\":"));
        assertTrue(metadata.contains("\"vDegree\":"));
        assertTrue(metadata.contains("\"controlPoints\":"));
        assertTrue(metadata.contains("\"surfaceUvLoops\":["));
    }
}
