package com.minicad.app;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StepPreviewJsonExporterTest {

    @Test
    void shouldExportPreviewJsonForMinimalSquare() {
        String json = StepPreviewJsonExporter.export("""
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

        assertTrue(json.contains("\"entityCount\":37"));
        assertTrue(json.contains("\"solidCount\":1"));
        assertTrue(json.contains("\"shellCount\":1"));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":4"));
        assertTrue(json.contains("\"bounds\":{\"min\":[0.0,0.0,0.0],\"max\":[1.0,1.0,0.0]}"));
        assertTrue(json.contains("\"normal\":[0.0,0.0,1.0]"));
    }

    @Test
    void shouldExportSampledCircularEdgePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('CENTER',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('E',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('N',(0.0,1.0,0.0));
                #4=CARTESIAN_POINT('W',(-1.0,0.0,0.0));
                #5=CARTESIAN_POINT('S',(0.0,-1.0,0.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #13=CIRCLE('C0',#12,1.0);
                #20=PLANE('PL0',#12);
                #30=VERTEX_POINT('V0',#2);
                #31=VERTEX_POINT('V1',#3);
                #32=VERTEX_POINT('V2',#4);
                #33=VERTEX_POINT('V3',#5);
                #40=EDGE_CURVE('E0',#30,#31,#13,.T.);
                #41=EDGE_CURVE('E1',#31,#32,#13,.T.);
                #42=EDGE_CURVE('E2',#32,#33,#13,.T.);
                #43=EDGE_CURVE('E3',#33,#30,#13,.T.);
                #50=ORIENTED_EDGE('OE0',$,$,#40,.T.);
                #51=ORIENTED_EDGE('OE1',$,$,#41,.T.);
                #52=ORIENTED_EDGE('OE2',$,$,#42,.T.);
                #53=ORIENTED_EDGE('OE3',$,$,#43,.T.);
                #60=EDGE_LOOP('L0',(#50,#51,#52,#53));
                #61=FACE_OUTER_BOUND('B0',#60,.T.);
                #70=ADVANCED_FACE('F0',(#61),#20,.T.);
                #80=OPEN_SHELL('OS',(#70));
                ENDSEC;
                """);

        assertTrue(json.contains("\"edgeCount\":4"));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"outer\":true"));
        assertTrue(json.contains("\"points\":[[1.0,0.0,0.0]"));
        assertTrue(json.contains("[0.0,1.0,0.0]"));
    }

    @Test
    void shouldExportInnerLoopForRoundHoleExample() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/plate-with-round-hole.step")));

        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":8"));
        assertTrue(json.contains("\"outer\":false"));
    }

    @Test
    void shouldSkipUnsupportedCylindricalFacesInPreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P0',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('P1',(0.0,2.0,0.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #13=CYLINDRICAL_SURFACE('CY0',#12,2.0);
                #20=VERTEX_POINT('V0',#2);
                #21=VERTEX_POINT('V1',#3);
                #30=CIRCLE('C0',#12,2.0);
                #40=EDGE_CURVE('E0',#20,#21,#30,.T.);
                #50=ORIENTED_EDGE('OE0',$,$,#40,.T.);
                #60=EDGE_LOOP('L0',(#50));
                #61=FACE_OUTER_BOUND('B0',#60,.T.);
                #70=ADVANCED_FACE('F0',(#61),#13,.T.);
                #80=OPEN_SHELL('OS',(#70));
                ENDSEC;
                """);

        assertTrue(json.contains("\"faceCount\":0"));
        assertTrue(json.contains("\"edgeCount\":0"));
        assertTrue(json.contains("\"unsupportedFaceCount\":1"));
    }
}
