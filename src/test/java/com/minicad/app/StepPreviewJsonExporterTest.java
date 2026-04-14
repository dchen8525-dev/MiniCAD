package com.minicad.app;

import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepOrientedEdge;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepParser;
import com.minicad.topology.FaceBound;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        assertJsonContains(json,
                "\"entityCount\":37",
                "\"solidCount\":1",
                "\"shellCount\":1",
                "\"faceCount\":1",
                "\"edgeCount\":4",
                "\"bounds\":{\"min\":[0.0,0.0,0.0],\"max\":[1.0,1.0,0.0]}",
                "\"normal\":[0.0,0.0,1.0]");
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

        assertJsonContains(json,
                "\"edgeCount\":4",
                "\"faceCount\":1",
                "\"outer\":true",
                "\"points\":[[1.0,0.0,0.0]",
                "[0.0,1.0,0.0]");
    }

    @Test
    void shouldExportPathLikeStandaloneEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #5=DIRECTION('DX',(1.0,0.0,0.0));
                #6=DIRECTION('DY',(0.0,1.0,0.0));
                #7=DIRECTION('NX',(-1.0,0.0,0.0));
                #8=VECTOR('VX',#5,1.0);
                #9=VECTOR('VY',#6,1.0);
                #10=VECTOR('VNX',#7,1.0);
                #11=LINE('L0',#1,#8);
                #12=LINE('L1',#2,#9);
                #13=LINE('L2',#3,#10);
                #20=VERTEX_POINT('V0',#1);
                #21=VERTEX_POINT('V1',#2);
                #22=VERTEX_POINT('V2',#3);
                #23=VERTEX_POINT('V3',#4);
                #30=EDGE_CURVE('E0',#20,#21,#11,.T.);
                #31=EDGE_CURVE('E1',#21,#22,#12,.T.);
                #32=EDGE_CURVE('E2',#22,#23,#13,.T.);
                #40=ORIENTED_EDGE('OE0',$,$,#30,.T.);
                #41=ORIENTED_EDGE('OE1',$,$,#31,.T.);
                #42=ORIENTED_EDGE('OE2',$,$,#32,.T.);
                #50=PATH('PTH',(#40));
                #51=OPEN_PATH('OP',(#41));
                #52=SUBPATH('SP',(#42),#50);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"faceCount\":0",
                "\"edgeCount\":3",
                "\"unsupportedFaceCount\":0",
                "\"id\":30",
                "\"id\":31",
                "\"id\":32");
    }

    @Test
    void shouldExportConnectedEdgeSetAndWireShellStandaloneEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=DIRECTION('DY',(0.0,1.0,0.0));
                #6=VECTOR('VX',#4,1.0);
                #7=VECTOR('VY',#5,1.0);
                #8=LINE('L0',#1,#6);
                #9=LINE('L1',#2,#7);
                #10=VERTEX_POINT('V0',#1);
                #11=VERTEX_POINT('V1',#2);
                #12=VERTEX_POINT('V2',#3);
                #20=EDGE_CURVE('E0',#10,#11,#8,.T.);
                #21=EDGE_CURVE('E1',#11,#12,#9,.T.);
                #40=CONNECTED_EDGE_SET('CES',(#20,#21));
                #41=POLY_LOOP('PL0',(#1,#2,#3));
                #42=WIRE_SHELL('WS0',(#41));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"faceCount\":0",
                "\"edgeCount\":3",
                "\"id\":20",
                "\"id\":21",
                "\"id\":41");
    }

    @Test
    void shouldExportPointSetAndVertexShellAsPmi() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,1.0,0.0));
                #3=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #4=POINT_SET('PS0',(#1,#2));
                #5=VERTEX_POINT('V0',#3);
                #6=VERTEX_LOOP('VL0',#5);
                #7=VERTEX_SHELL('VS0',#6);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"faceCount\":0",
                "\"edgeCount\":0",
                "\"name\":\"PS0[0]\"",
                "\"name\":\"PS0[1]\"",
                "\"name\":\"VS0\"",
                "\"position\":[0.0,0.0,0.0]",
                "\"position\":[1.0,1.0,0.0]",
                "\"position\":[2.0,0.0,0.0]");
    }

    @Test
    void shouldExportCurveAndPointReplicasFromGeometricSet() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('SHIFT',(1.0,1.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=DIRECTION('DY',(0.0,1.0,0.0));
                #6=DIRECTION('DZ',(0.0,0.0,1.0));
                #7=VECTOR('VX',#4,1.0);
                #8=LINE('L0',#1,#7);
                #9=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T',#4,#5,#3,1.0,#6);
                #10=CURVE_REPLICA('CR',#8,#9);
                #11=POINT_REPLICA('PR',#2,#9);
                #12=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #13=SHAPE_REPRESENTATION('R0',(#10),#12);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"faceCount\":0",
                "\"edgeCount\":0",
                "\"representationCount\":1",
                "\"representations\":[{\"id\":13",
                "\"edges\":[{\"id\":10",
                "\"points\":[[1.0,1.0,0.0],[2.0,1.0,0.0]]",
                "\"name\":\"PR\"",
                "\"position\":[2.0,1.0,0.0]");
    }

    @Test
    void shouldExpandMappedItemRepresentationGeometry() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #5=CARTESIAN_POINT('T',(2.0,0.0,0.0));
                #10=DIRECTION('DX',(1.0,0.0,0.0));
                #11=DIRECTION('DY',(0.0,1.0,0.0));
                #12=DIRECTION('DZ',(0.0,0.0,1.0));
                #13=AXIS2_PLACEMENT_3D('AX0',#1,#12,#10);
                #14=AXIS2_PLACEMENT_3D('AX1',#5,#12,#10);
                #15=PLANE('PL0',#13);
                #20=VERTEX_POINT('V0',#1);
                #21=VERTEX_POINT('V1',#2);
                #22=VERTEX_POINT('V2',#3);
                #23=VERTEX_POINT('V3',#4);
                #30=VECTOR('VX',#10,1.0);
                #31=VECTOR('VY',#11,1.0);
                #32=VECTOR('VNX',#10,1.0);
                #33=VECTOR('VNY',#11,1.0);
                #34=LINE('L0',#1,#30);
                #35=LINE('L1',#2,#31);
                #36=LINE('L2',#3,#30);
                #37=LINE('L3',#4,#31);
                #40=EDGE_CURVE('E0',#20,#21,#34,.T.);
                #41=EDGE_CURVE('E1',#21,#22,#35,.T.);
                #42=EDGE_CURVE('E2',#22,#23,#36,.F.);
                #43=EDGE_CURVE('E3',#23,#20,#37,.F.);
                #50=ORIENTED_EDGE('OE0',$,$,#40,.T.);
                #51=ORIENTED_EDGE('OE1',$,$,#41,.T.);
                #52=ORIENTED_EDGE('OE2',$,$,#42,.T.);
                #53=ORIENTED_EDGE('OE3',$,$,#43,.T.);
                #60=EDGE_LOOP('LP',(#50,#51,#52,#53));
                #61=FACE_OUTER_BOUND('FB',#60,.T.);
                #62=ADVANCED_FACE('F0',(#61),#15,.T.);
                #63=OPEN_SHELL('SH',(#62));
                #70=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #71=SHAPE_REPRESENTATION('BASE',(#63),#70);
                #72=REPRESENTATION_MAP(#13,#71);
                #73=MAPPED_ITEM(#72,#14);
                #74=SHAPE_REPRESENTATION('TOP',(#73),#70);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"representationCount\":2",
                "\"id\":71",
                "\"id\":74",
                "\"points\":[[2.0,0.0,0.0],[3.0,0.0,0.0],[3.0,1.0,0.0],[2.0,1.0,0.0],[2.0,0.0,0.0]]");
    }

    @Test
    void shouldExportManifoldSolidBrepFacesFromAdvancedBrepShapeRepresentation() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
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
                #110=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #111=ADVANCED_BREP_SHAPE_REPRESENTATION('BREP_REP',(#100),#110);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"representationCount\":1",
                "\"representations\":[{\"id\":111",
                "\"faces\":[{\"id\":80",
                "\"surfaceType\":\"PLANE\"",
                "\"edgeCount\":4",
                "\"faceCount\":1",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExpandRepresentationRelationshipGeometry() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=POLYLINE('PL0',(#1,#2,#3));
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #11=SHAPE_REPRESENTATION('BASE',(#4),#10);
                #12=SHAPE_REPRESENTATION('LINKED',(),#10);
                #13=REPRESENTATION_RELATIONSHIP('RR','link',#11,#12);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"representationCount\":2",
                "\"id\":11",
                "\"id\":12",
                "\"edges\":[{\"id\":4",
                "\"points\":[[0.0,0.0,0.0],[1.0,0.0,0.0],[1.0,1.0,0.0]]");
    }

    @Test
    void shouldExpandRepresentationRelationshipWithTransformationGeometry() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('T',(3.0,0.0,0.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=DIRECTION('DX',(1.0,0.0,0.0));
                #6=AXIS2_PLACEMENT_3D('AX0',#1,#4,#5);
                #7=AXIS2_PLACEMENT_3D('AX1',#3,#4,#5);
                #8=ITEM_DEFINED_TRANSFORMATION('MOVE','translate x',#6,#7);
                #9=POLYLINE('PL0',(#1,#2));
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #11=SHAPE_REPRESENTATION('BASE',(#9),#10);
                #12=SHAPE_REPRESENTATION('TARGET',(),#10);
                #13=(REPRESENTATION_RELATIONSHIP('RRX','xform',#11,#12)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#8));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"representationCount\":2",
                "\"id\":12",
                "\"points\":[[3.0,0.0,0.0],[4.0,0.0,0.0]]");
    }

    @Test
    void shouldExportOrientedPathStandaloneEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #10=VERTEX_POINT('V0',#1);
                #11=VERTEX_POINT('V1',#2);
                #12=VERTEX_POINT('V2',#3);
                #20=DIRECTION('DX',(1.0,0.0,0.0));
                #21=VECTOR('VX',#20,1.0);
                #22=LINE('L0',#1,#21);
                #23=DIRECTION('DY',(0.0,1.0,0.0));
                #24=VECTOR('VY',#23,1.0);
                #25=LINE('L1',#2,#24);
                #30=EDGE_CURVE('E0',#10,#11,#22,.T.);
                #31=EDGE_CURVE('E1',#11,#12,#25,.T.);
                #40=ORIENTED_EDGE('OE0',$,$,#30,.T.);
                #41=ORIENTED_EDGE('OE1',$,$,#31,.T.);
                #50=PATH('PTH',(#40,#41));
                #51=ORIENTED_PATH('OP',#50,.F.);
                #60=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #61=SHAPE_REPRESENTATION('WIRE',(#51),#60);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"representationCount\":1",
                "\"edgeCount\":2",
                "\"representations\":[{\"id\":61",
                "\"points\":[[0.0,0.0,0.0],[1.0,0.0,0.0]]",
                "\"points\":[[1.0,0.0,0.0],[1.0,1.0,0.0]]");
    }

    @Test
    void shouldExportAnnotationCurveWrappersAsStandaloneEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,1.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=VECTOR('VX',#4,1.0);
                #6=LINE('L0',#1,#5);
                #7=DIRECTION('DY',(0.0,1.0,0.0));
                #8=VECTOR('VY',#7,1.0);
                #9=LINE('L1',#1,#8);
                #10=PRESENTATION_STYLE_ASSIGNMENT(());
                #11=(LEADER_CURVE('LC0',(#10),#6)
                    ANNOTATION_CURVE_OCCURRENCE('LC0',(#10),#6)
                    STYLED_ITEM('LC0',(#10),#6)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('LC0'));
                #12=(DIMENSION_CURVE('DC0',(#10),#9)
                    ANNOTATION_CURVE_OCCURRENCE('DC0',(#10),#9)
                    STYLED_ITEM('DC0',(#10),#9)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('DC0'));
                #20=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #21=SHAPE_REPRESENTATION('ANN',(#11,#12),#20);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"representationCount\":1",
                "\"edgeCount\":2",
                "\"representations\":[{\"id\":21",
                "\"points\":[[0.0,0.0,0.0],[1.0,0.0,0.0]]",
                "\"points\":[[0.0,0.0,0.0],[0.0,1.0,0.0]]");
    }

    @Test
    void shouldExportProjectionCurveAndTerminatorAsStandaloneEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=VECTOR('VX',#3,1.0);
                #5=LINE('L0',#1,#4);
                #6=PRESENTATION_STYLE_ASSIGNMENT(());
                #7=(PROJECTION_CURVE('PC0',(#6),#5)
                    ANNOTATION_CURVE_OCCURRENCE('PC0',(#6),#5)
                    STYLED_ITEM('PC0',(#6),#5)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PC0'));
                #8=CARTESIAN_POINT('O2',(2.0,0.0));
                #9=DIRECTION('DX2',(1.0,0.0));
                #10=AXIS2_PLACEMENT_2D('MAP',#8,#9);
                #11=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #12=REPRESENTATION('SYM',(),#11);
                #13=SYMBOL_REPRESENTATION_MAP(#10,#12);
                #14=CARTESIAN_POINT('O3',(3.0,0.0));
                #15=DIRECTION('DX3',(1.0,0.0));
                #16=AXIS2_PLACEMENT_2D('TGT',#14,#15);
                #17=ANNOTATION_SYMBOL('AS0',#13,#16);
                #18=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#6),#17);
                #19=TERMINATOR_SYMBOL('TS0',(#6),#17,#7);
                #20=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #21=SHAPE_REPRESENTATION('ANN',(#19),#20);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"representationCount\":1",
                "\"edgeCount\":2",
                "\"curve\":{\"stepId\":7,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":5",
                "\"curve\":{\"stepId\":19,\"type\":\"TERMINATOR_SYMBOL\",\"basisType\":\"PROJECTION_CURVE\",\"basisStepId\":7",
                "\"points\":[[0.0,0.0,0.0],[1.0,0.0,0.0]]");
    }

    @Test
    void shouldPreserveCurveMetadataForMappedAnnotationSymbolEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=PRESENTATION_STYLE_ASSIGNMENT(());
                #6=(PROJECTION_CURVE('PC0',(#5),#4)
                    ANNOTATION_CURVE_OCCURRENCE('PC0',(#5),#4)
                    STYLED_ITEM('PC0',(#5),#4)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PC0'));
                #7=CARTESIAN_POINT('O2',(2.0,0.0));
                #8=DIRECTION('DX2',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('MAP',#7,#8);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #11=REPRESENTATION('SYM',(#6),#10);
                #12=SYMBOL_REPRESENTATION_MAP(#9,#11);
                #13=CARTESIAN_POINT('O3',(3.0,0.0));
                #14=DIRECTION('DX3',(1.0,0.0));
                #15=AXIS2_PLACEMENT_2D('TGT',#13,#14);
                #16=ANNOTATION_SYMBOL('AS0',#12,#15);
                #17=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#5),#16);
                #18=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #19=SHAPE_REPRESENTATION('ANN',(#17),#18);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":3",
                "\"curve\":{\"stepId\":6,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":4",
                "\"sourceType\":\"ANNOTATION_SYMBOL_OCCURRENCE\"",
                "\"sourceStepId\":17",
                "\"representations\":[{\"id\":19",
                "\"curve\":{\"stepId\":6,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":4");
    }

    @Test
    void shouldPreserveCurveMetadataForMappedAnnotationTextEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=PRESENTATION_STYLE_ASSIGNMENT(());
                #6=(PROJECTION_CURVE('PC0',(#5),#4)
                    ANNOTATION_CURVE_OCCURRENCE('PC0',(#5),#4)
                    STYLED_ITEM('PC0',(#5),#4)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PC0'));
                #7=CARTESIAN_POINT('O2',(2.0,0.0));
                #8=DIRECTION('DX2',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('MAP',#7,#8);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #11=REPRESENTATION('SYM',(#6),#10);
                #12=REPRESENTATION_MAP(#9,#11);
                #13=CARTESIAN_POINT('O3',(3.0,0.0));
                #14=DIRECTION('DX3',(1.0,0.0));
                #15=AXIS2_PLACEMENT_2D('TGT',#13,#14);
                #16=ANNOTATION_TEXT('AT0',#12,#15);
                #17=ANNOTATION_TEXT_CHARACTER('ATC0',#12,#15);
                #18=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #19=SHAPE_REPRESENTATION('ANN',(#16,#17),#18);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":3",
                "\"curve\":{\"stepId\":6,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":4",
                "\"representations\":[{\"id\":19");
    }

    @Test
    void shouldPreserveMappedAnnotationCarrierMetadataForSymbolAndSubfigureOccurrences() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=PRESENTATION_STYLE_ASSIGNMENT(());
                #6=(PROJECTION_CURVE('PC0',(#5),#4)
                    ANNOTATION_CURVE_OCCURRENCE('PC0',(#5),#4)
                    STYLED_ITEM('PC0',(#5),#4)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PC0'));
                #7=CARTESIAN_POINT('O2',(2.0,0.0));
                #8=DIRECTION('DX2',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('MAP',#7,#8);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #11=REPRESENTATION('SYM',(#6),#10);
                #12=SYMBOL_REPRESENTATION_MAP(#9,#11);
                #13=CARTESIAN_POINT('O3',(3.0,0.0));
                #14=DIRECTION('DX3',(1.0,0.0));
                #15=AXIS2_PLACEMENT_2D('TGT',#13,#14);
                #16=ANNOTATION_SYMBOL('AS0',#12,#15);
                #17=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#5),#16);
                #18=ANNOTATION_SUBFIGURE_OCCURRENCE('SUB0',(#5),#16);
                #19=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #20=SHAPE_REPRESENTATION('ANN',(#17,#18),#19);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":4",
                "\"curve\":{\"stepId\":6,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":4",
                "\"sourceType\":\"ANNOTATION_SYMBOL_OCCURRENCE\"",
                "\"sourceStepId\":17",
                "\"sourceType\":\"ANNOTATION_SUBFIGURE_OCCURRENCE\"",
                "\"sourceStepId\":18",
                "\"representations\":[{\"id\":20");
    }

    @Test
    void shouldPreserveMappedAnnotationCarrierMetadataForDraughtingAnnotationOccurrence() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=PRESENTATION_STYLE_ASSIGNMENT(());
                #6=(PROJECTION_CURVE('PC0',(#5),#4)
                    ANNOTATION_CURVE_OCCURRENCE('PC0',(#5),#4)
                    STYLED_ITEM('PC0',(#5),#4)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PC0'));
                #7=CARTESIAN_POINT('O2',(2.0,0.0));
                #8=DIRECTION('DX2',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('MAP',#7,#8);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #11=REPRESENTATION('SYM',(#6),#10);
                #12=SYMBOL_REPRESENTATION_MAP(#9,#11);
                #13=CARTESIAN_POINT('O3',(3.0,0.0));
                #14=DIRECTION('DX3',(1.0,0.0));
                #15=AXIS2_PLACEMENT_2D('TGT',#13,#14);
                #16=ANNOTATION_SYMBOL('AS0',#12,#15);
                #17=(DRAUGHTING_ANNOTATION_OCCURRENCE('DAO0',(#5),#16)
                    STYLED_ITEM('DAO0',(#5),#16)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('DAO0'));
                #18=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #19=SHAPE_REPRESENTATION('ANN',(#17),#18);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":3",
                "\"curve\":{\"stepId\":6,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":4",
                "\"sourceType\":\"DRAUGHTING_ANNOTATION_OCCURRENCE\"",
                "\"sourceStepId\":17",
                "\"representations\":[{\"id\":19");
    }

    @Test
    void shouldExportAnnotationCurveFamilyWithPathAndWireCarriersAsStandaloneEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=VERTEX_POINT('V0',#1);
                #5=VERTEX_POINT('V1',#2);
                #6=VERTEX_POINT('V2',#3);
                #7=DIRECTION('DX',(1.0,0.0,0.0));
                #8=DIRECTION('DY',(0.0,1.0,0.0));
                #9=VECTOR('VX',#7,1.0);
                #10=VECTOR('VY',#8,1.0);
                #11=LINE('L0',#1,#9);
                #12=LINE('L1',#2,#10);
                #13=EDGE_CURVE('E0',#4,#5,#11,.T.);
                #14=EDGE_CURVE('E1',#5,#6,#12,.T.);
                #15=ORIENTED_EDGE('OE0',$,$,#13,.T.);
                #16=ORIENTED_EDGE('OE1',$,$,#14,.T.);
                #17=PATH('PTH',(#15));
                #18=OPEN_PATH('OPH',(#16));
                #19=CONNECTED_EDGE_SET('CES',(#13,#14));
                #20=POLY_LOOP('PL0',(#1,#2,#3));
                #21=WIRE_SHELL('WS0',(#20));
                #22=PRESENTATION_STYLE_ASSIGNMENT(());
                #23=(ANNOTATION_CURVE_OCCURRENCE('AC0',(#22),#17)
                    STYLED_ITEM('AC0',(#22),#17)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('AC0'));
                #24=(LEADER_CURVE('LC0',(#22),#18)
                    ANNOTATION_CURVE_OCCURRENCE('LC0',(#22),#18)
                    STYLED_ITEM('LC0',(#22),#18)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('LC0'));
                #25=(DIMENSION_CURVE('DC0',(#22),#19)
                    ANNOTATION_CURVE_OCCURRENCE('DC0',(#22),#19)
                    STYLED_ITEM('DC0',(#22),#19)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('DC0'));
                #26=(PROJECTION_CURVE('PC0',(#22),#21)
                    ANNOTATION_CURVE_OCCURRENCE('PC0',(#22),#21)
                    STYLED_ITEM('PC0',(#22),#21)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PC0'));
                #30=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #31=SHAPE_REPRESENTATION('ANN',(#23,#24,#25,#26),#30);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"representationCount\":1",
                "\"edgeCount\":3",
                "\"id\":13",
                "\"id\":14",
                "\"id\":20");
    }

    @Test
    void shouldExportAnnotationFillAreaWithPathAndWireBoundariesAsStandaloneEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=VERTEX_POINT('V0',#1);
                #5=VERTEX_POINT('V1',#2);
                #6=VERTEX_POINT('V2',#3);
                #7=DIRECTION('DX',(1.0,0.0,0.0));
                #8=DIRECTION('DY',(0.0,1.0,0.0));
                #9=VECTOR('VX',#7,1.0);
                #10=VECTOR('VY',#8,1.0);
                #11=LINE('L0',#1,#9);
                #12=LINE('L1',#2,#10);
                #13=EDGE_CURVE('E0',#4,#5,#11,.T.);
                #14=EDGE_CURVE('E1',#5,#6,#12,.T.);
                #15=CONNECTED_EDGE_SET('CES',(#13,#14));
                #16=POLY_LOOP('PL0',(#1,#2,#3));
                #17=WIRE_SHELL('WS0',(#16));
                #18=(ANNOTATION_FILL_AREA('FA0',(#15,#17))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                #30=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #31=SHAPE_REPRESENTATION('ANN',(#18),#30);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"representationCount\":1",
                "\"edgeCount\":3",
                "\"id\":13",
                "\"id\":14",
                "\"id\":16");
    }

    @Test
    void shouldExportAnnotationCurveAndFillAreaWithWireframeModelCarriersAsStandaloneEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=VERTEX_POINT('V0',#1);
                #5=VERTEX_POINT('V1',#2);
                #6=VERTEX_POINT('V2',#3);
                #7=DIRECTION('DX',(1.0,0.0,0.0));
                #8=DIRECTION('DY',(0.0,1.0,0.0));
                #9=VECTOR('VX',#7,1.0);
                #10=VECTOR('VY',#8,1.0);
                #11=LINE('L0',#1,#9);
                #12=LINE('L1',#2,#10);
                #13=EDGE_CURVE('E0',#4,#5,#11,.T.);
                #14=EDGE_CURVE('E1',#5,#6,#12,.T.);
                #15=CONNECTED_EDGE_SET('CES0',(#13,#14));
                #16=EDGE_BASED_WIREFRAME_MODEL('EBWM',(#15));
                #17=POLY_LOOP('PL0',(#1,#2,#3));
                #18=WIRE_SHELL('WS0',(#17));
                #19=SHELL_BASED_WIREFRAME_MODEL('SBWM',(#18));
                #20=PRESENTATION_STYLE_ASSIGNMENT(());
                #21=(ANNOTATION_CURVE_OCCURRENCE('AC0',(#20),#16)
                    STYLED_ITEM('AC0',(#20),#16)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('AC0'));
                #22=(PROJECTION_CURVE('PC0',(#20),#19)
                    ANNOTATION_CURVE_OCCURRENCE('PC0',(#20),#19)
                    STYLED_ITEM('PC0',(#20),#19)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PC0'));
                #23=(ANNOTATION_FILL_AREA('FA0',(#16,#19))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                #30=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #31=SHAPE_REPRESENTATION('ANN',(#21,#22,#23),#30);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"representationCount\":1",
                "\"edgeCount\":3",
                "\"id\":13",
                "\"id\":14",
                "\"id\":17");
    }

    @Test
    void shouldExportEdgeCurvesBackedByAnnotationCurveWrappers() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,1.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=VECTOR('VX',#4,1.0);
                #6=LINE('L0',#1,#5);
                #7=DIRECTION('DY',(0.0,1.0,0.0));
                #8=VECTOR('VY',#7,1.0);
                #9=LINE('L1',#1,#8);
                #10=PRESENTATION_STYLE_ASSIGNMENT(());
                #11=(LEADER_CURVE('LC0',(#10),#6)
                    ANNOTATION_CURVE_OCCURRENCE('LC0',(#10),#6)
                    STYLED_ITEM('LC0',(#10),#6)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('LC0'));
                #12=(DIMENSION_CURVE('DC0',(#10),#9)
                    ANNOTATION_CURVE_OCCURRENCE('DC0',(#10),#9)
                    STYLED_ITEM('DC0',(#10),#9)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('DC0'));
                #20=VERTEX_POINT('V0',#1);
                #21=VERTEX_POINT('V1',#2);
                #22=VERTEX_POINT('V2',#3);
                #30=EDGE_CURVE('E0',#20,#21,#11,.T.);
                #31=EDGE_CURVE('E1',#20,#22,#12,.T.);
                #32=CONNECTED_EDGE_SET('CES',(#30,#31));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":4",
                "\"id\":30",
                "\"id\":31",
                "[0.0,0.0,0.0],[1.0,0.0,0.0]",
                "[0.0,0.0,0.0],[0.0,1.0,0.0]");
    }

    @Test
    void shouldExportEdgeCurvesBackedByProjectionAndTerminatorWrappers() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=VECTOR('VX',#3,1.0);
                #5=LINE('L0',#1,#4);
                #6=PRESENTATION_STYLE_ASSIGNMENT(());
                #7=(PROJECTION_CURVE('PC0',(#6),#5)
                    ANNOTATION_CURVE_OCCURRENCE('PC0',(#6),#5)
                    STYLED_ITEM('PC0',(#6),#5)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PC0'));
                #8=CARTESIAN_POINT('O2',(2.0,0.0));
                #9=DIRECTION('DX2',(1.0,0.0));
                #10=AXIS2_PLACEMENT_2D('MAP',#8,#9);
                #11=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #12=REPRESENTATION('SYM',(),#11);
                #13=SYMBOL_REPRESENTATION_MAP(#10,#12);
                #14=CARTESIAN_POINT('O3',(3.0,0.0));
                #15=DIRECTION('DX3',(1.0,0.0));
                #16=AXIS2_PLACEMENT_2D('TGT',#14,#15);
                #17=ANNOTATION_SYMBOL('AS0',#13,#16);
                #18=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#6),#17);
                #19=TERMINATOR_SYMBOL('TS0',(#6),#17,#7);
                #20=VERTEX_POINT('V0',#1);
                #21=VERTEX_POINT('V1',#2);
                #22=EDGE_CURVE('E0',#20,#21,#7,.T.);
                #23=EDGE_CURVE('E1',#20,#21,#19,.T.);
                #24=CONNECTED_EDGE_SET('CES',(#22,#23));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":3",
                "\"id\":22",
                "\"id\":23",
                "[0.0,0.0,0.0],[1.0,0.0,0.0]");
    }

    @Test
    void shouldPreviewPlanarFaceWithWrapperBackedEdgesThroughProjectedFallback() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=PLANE('PL0',#7);
                #9=PRESENTATION_STYLE_ASSIGNMENT(());
                #10=VECTOR('VX',#6,1.0);
                #11=LINE('L0',#1,#10);
                #12=(LEADER_CURVE('LC0',(#9),#11)
                    ANNOTATION_CURVE_OCCURRENCE('LC0',(#9),#11)
                    STYLED_ITEM('LC0',(#9),#11)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('LC0'));
                #13=DIRECTION('DY',(0.0,1.0,0.0));
                #14=VECTOR('VY',#13,1.0);
                #15=LINE('L1',#2,#14);
                #16=(DIMENSION_CURVE('DC0',(#9),#15)
                    ANNOTATION_CURVE_OCCURRENCE('DC0',(#9),#15)
                    STYLED_ITEM('DC0',(#9),#15)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('DC0'));
                #17=DIRECTION('MX',(-1.0,0.0,0.0));
                #18=VECTOR('VMX',#17,1.0);
                #19=LINE('L2',#3,#18);
                #20=(PROJECTION_CURVE('PC0',(#9),#19)
                    ANNOTATION_CURVE_OCCURRENCE('PC0',(#9),#19)
                    STYLED_ITEM('PC0',(#9),#19)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PC0'));
                #21=DIRECTION('MY',(0.0,-1.0,0.0));
                #22=VECTOR('VMY',#21,1.0);
                #23=LINE('L3',#4,#22);
                #24=(DRAUGHTING_ANNOTATION_OCCURRENCE('DA0',(#9),#23)
                    STYLED_ITEM('DA0',(#9),#23)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('DA0'));
                #30=VERTEX_POINT('V0',#1);
                #31=VERTEX_POINT('V1',#2);
                #32=VERTEX_POINT('V2',#3);
                #33=VERTEX_POINT('V3',#4);
                #40=EDGE_CURVE('E0',#30,#31,#12,.T.);
                #41=EDGE_CURVE('E1',#31,#32,#16,.T.);
                #42=EDGE_CURVE('E2',#32,#33,#20,.T.);
                #43=EDGE_CURVE('E3',#33,#30,#24,.T.);
                #50=ORIENTED_EDGE('OE0',$,$,#40,.T.);
                #51=ORIENTED_EDGE('OE1',$,$,#41,.T.);
                #52=ORIENTED_EDGE('OE2',$,$,#42,.T.);
                #53=ORIENTED_EDGE('OE3',$,$,#43,.T.);
                #60=EDGE_LOOP('LOOP0',(#50,#51,#52,#53));
                #61=FACE_OUTER_BOUND('FOB',#60,.T.);
                #62=ADVANCED_FACE('F0',(#61),#8,.T.);
                #63=CLOSED_SHELL('CS0',(#62));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"faceCount\":1",
                "\"unsupportedFaceCount\":0",
                "\"id\":40",
                "\"id\":43");
    }

    @Test
    void shouldExportTrimmedCurveBackedByProjectionWrapper() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=VECTOR('VX',#3,1.0);
                #5=LINE('L0',#1,#4);
                #6=PRESENTATION_STYLE_ASSIGNMENT(());
                #7=(PROJECTION_CURVE('PC0',(#6),#5)
                    ANNOTATION_CURVE_OCCURRENCE('PC0',(#6),#5)
                    STYLED_ITEM('PC0',(#6),#5)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PC0'));
                #8=TRIMMED_CURVE('TC0',#7,(#1),(#2),.T.,.CARTESIAN.);
                #9=VERTEX_POINT('V0',#1);
                #10=VERTEX_POINT('V1',#2);
                #11=EDGE_CURVE('E0',#9,#10,#8,.T.);
                #12=CONNECTED_EDGE_SET('CES',(#11));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":2",
                "\"id\":11",
                "[0.0,0.0,0.0],[1.0,0.0,0.0]");
    }

    @Test
    void shouldExportEdgeCurveBackedByOrientedAnnotationWrapper() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=VECTOR('VX',#3,1.0);
                #5=LINE('L0',#1,#4);
                #6=PRESENTATION_STYLE_ASSIGNMENT(());
                #7=(PROJECTION_CURVE('PC0',(#6),#5)
                    ANNOTATION_CURVE_OCCURRENCE('PC0',(#6),#5)
                    STYLED_ITEM('PC0',(#6),#5)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PC0'));
                #8=ORIENTED_CURVE('OC0',#7,.F.);
                #9=VERTEX_POINT('V0',#1);
                #10=VERTEX_POINT('V1',#2);
                #11=EDGE_CURVE('E0',#9,#10,#8,.T.);
                #12=CONNECTED_EDGE_SET('CES',(#11));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":2",
                "\"id\":11",
                "[0.0,0.0,0.0],[1.0,0.0,0.0]");
    }

    @Test
    void shouldExportEdgeCurveBackedByReplicaOfAnnotationWrapper() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('T',(10.0,0.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=VECTOR('VX',#4,1.0);
                #6=LINE('L0',#1,#5);
                #7=PRESENTATION_STYLE_ASSIGNMENT(());
                #8=(PROJECTION_CURVE('PC0',(#7),#6)
                    ANNOTATION_CURVE_OCCURRENCE('PC0',(#7),#6)
                    STYLED_ITEM('PC0',(#7),#6)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PC0'));
                #9=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#3,1.0,$);
                #10=CURVE_REPLICA('CR0',#8,#9);
                #11=VERTEX_POINT('V0',#3);
                #12=CARTESIAN_POINT('P2',(11.0,0.0,0.0));
                #13=VERTEX_POINT('V1',#12);
                #14=EDGE_CURVE('E0',#11,#13,#10,.T.);
                #15=CONNECTED_EDGE_SET('CES',(#14));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"id\":14",
                "[10.0,0.0,0.0],[11.0,0.0,0.0]");
    }

    @Test
    void shouldExportEdgeCurveBackedByOrientedReplicaOfAnnotationWrapper() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('T',(10.0,0.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=VECTOR('VX',#4,1.0);
                #6=LINE('L0',#1,#5);
                #7=PRESENTATION_STYLE_ASSIGNMENT(());
                #8=(PROJECTION_CURVE('PC0',(#7),#6)
                    ANNOTATION_CURVE_OCCURRENCE('PC0',(#7),#6)
                    STYLED_ITEM('PC0',(#7),#6)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PC0'));
                #9=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#3,1.0,$);
                #10=CURVE_REPLICA('CR0',#8,#9);
                #11=ORIENTED_CURVE('OC0',#10,.F.);
                #12=VERTEX_POINT('V0',#3);
                #13=CARTESIAN_POINT('P2',(11.0,0.0,0.0));
                #14=VERTEX_POINT('V1',#13);
                #15=EDGE_CURVE('E0',#12,#14,#11,.T.);
                #16=CONNECTED_EDGE_SET('CES',(#15));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"id\":15",
                "[10.0,0.0,0.0],[11.0,0.0,0.0]");
    }

    @Test
    void shouldPreviewPlanarFaceWithOrientedWrapperBackedEdgesThroughProjectedFallback() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=PLANE('PL0',#7);
                #9=PRESENTATION_STYLE_ASSIGNMENT(());
                #10=VECTOR('VX',#6,1.0);
                #11=LINE('L0',#1,#10);
                #12=(PROJECTION_CURVE('PC0',(#9),#11)
                    ANNOTATION_CURVE_OCCURRENCE('PC0',(#9),#11)
                    STYLED_ITEM('PC0',(#9),#11)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PC0'));
                #13=ORIENTED_CURVE('OC0',#12,.F.);
                #14=DIRECTION('DY',(0.0,1.0,0.0));
                #15=VECTOR('VY',#14,1.0);
                #16=LINE('L1',#2,#15);
                #17=DIRECTION('MX',(-1.0,0.0,0.0));
                #18=VECTOR('VMX',#17,1.0);
                #19=LINE('L2',#3,#18);
                #20=DIRECTION('MY',(0.0,-1.0,0.0));
                #21=VECTOR('VMY',#20,1.0);
                #22=LINE('L3',#4,#21);
                #30=VERTEX_POINT('V0',#1);
                #31=VERTEX_POINT('V1',#2);
                #32=VERTEX_POINT('V2',#3);
                #33=VERTEX_POINT('V3',#4);
                #40=EDGE_CURVE('E0',#30,#31,#13,.T.);
                #41=EDGE_CURVE('E1',#31,#32,#16,.T.);
                #42=EDGE_CURVE('E2',#32,#33,#19,.T.);
                #43=EDGE_CURVE('E3',#33,#30,#22,.T.);
                #50=ORIENTED_EDGE('OE0',$,$,#40,.T.);
                #51=ORIENTED_EDGE('OE1',$,$,#41,.T.);
                #52=ORIENTED_EDGE('OE2',$,$,#42,.T.);
                #53=ORIENTED_EDGE('OE3',$,$,#43,.T.);
                #60=EDGE_LOOP('LOOP0',(#50,#51,#52,#53));
                #61=FACE_OUTER_BOUND('FOB',#60,.T.);
                #62=ADVANCED_FACE('F0',(#61),#8,.T.);
                #63=CLOSED_SHELL('CS0',(#62));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"faceCount\":1",
                "\"unsupportedFaceCount\":0",
                "\"id\":40");
    }

    @Test
    void shouldExportAnnotationSymbolAndTextMappedGeometry() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=DIRECTION('DX',(1.0,0.0));
                #5=POLYLINE('SYMPL',(#1,#2));
                #6=POLYLINE('PL0',(#1,#2));
                #10=CARTESIAN_POINT('M0',(0.0,0.0));
                #11=AXIS2_PLACEMENT_2D('MAP0',#10,#3);
                #12=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #13=REPRESENTATION('SYM',(#5),#12);
                #14=SYMBOL_REPRESENTATION_MAP(#11,#13);
                #15=CARTESIAN_POINT('T0',(3.0,4.0));
                #16=AXIS2_PLACEMENT_2D('TGT0',#15,#3);
                #17=ANNOTATION_SYMBOL('AS0',#14,#16);
                #18=PRESENTATION_STYLE_ASSIGNMENT(());
                #19=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#18),#17);
                #20=CARTESIAN_POINT('M1',(0.0,0.0));
                #21=AXIS2_PLACEMENT_2D('MAP1',#20,#3);
                #22=REPRESENTATION('TXTREP',(#6),#12);
                #23=REPRESENTATION_MAP(#21,#22);
                #24=CARTESIAN_POINT('T1',(6.0,7.0));
                #25=AXIS2_PLACEMENT_2D('TGT1',#24,#3);
                #26=ANNOTATION_TEXT('AT0',#23,#25);
                #27=ANNOTATION_TEXT_CHARACTER('ATC0',#23,#25);
                #30=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #31=SHAPE_REPRESENTATION('ANN',(#19,#26,#27),#30);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"representationCount\":1",
                "\"edgeCount\":5",
                "\"points\":[[3.0,4.0,0.0],[4.0,4.0,0.0]]",
                "\"points\":[[6.0,7.0,0.0],[7.0,7.0,0.0]]");
    }

    @Test
    void shouldSampleAnnotationMappedGeometryInCalloutLeader() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=DIRECTION('DX',(1.0,0.0));
                #5=POLYLINE('SYMPL',(#1,#2));
                #10=CARTESIAN_POINT('MAP',(0.0,0.0));
                #11=AXIS2_PLACEMENT_2D('MAPAX',#10,#3);
                #12=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #13=REPRESENTATION('SYM',(#5),#12);
                #14=SYMBOL_REPRESENTATION_MAP(#11,#13);
                #15=CARTESIAN_POINT('TGT',(5.0,2.0));
                #16=AXIS2_PLACEMENT_2D('TGTAX',#15,#3);
                #17=ANNOTATION_SYMBOL('AS0',#14,#16);
                #18=PRESENTATION_STYLE_ASSIGNMENT(());
                #19=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#18),#17);
                #20=CARTESIAN_POINT('TXT',(9.0,9.0,0.0));
                #21=ANNOTATION_TEXT_OCCURRENCE('NOTE','note',#20);
                #22=DRAUGHTING_CALLOUT('CALLOUT',(#21,#19));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"CALLOUT\"",
                "\"leader\":[[5.0,2.0,0.0],[6.0,2.0,0.0]]");
    }

    @Test
    void shouldExportAnnotationFillAreaAsStandaloneEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=POLYLINE('PL0',(#1,#2,#3));
                #5=(ANNOTATION_FILL_AREA('FA0',(#4))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                #6=PRESENTATION_STYLE_ASSIGNMENT(());
                #7=(ANNOTATION_FILL_AREA_OCCURRENCE('FAO0',(#6),#5,#1)
                    STYLED_ITEM('FAO0',(#6),#5)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FAO0'));
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #11=SHAPE_REPRESENTATION('ANN',(#7),#10);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"representationCount\":1",
                "\"edgeCount\":1",
                "\"points\":[[0.0,0.0,0.0],[1.0,0.0,0.0],[1.0,1.0,0.0]]");
    }

    @Test
    void shouldSampleAnnotationFillAreaInCalloutLeader() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=POLYLINE('PL0',(#1,#2,#3));
                #5=(ANNOTATION_FILL_AREA('FA0',(#4))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                #6=PRESENTATION_STYLE_ASSIGNMENT(());
                #7=(ANNOTATION_FILL_AREA_OCCURRENCE('FAO0',(#6),#5,#1)
                    STYLED_ITEM('FAO0',(#6),#5)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FAO0'));
                #8=CARTESIAN_POINT('TXT',(3.0,3.0,0.0));
                #9=ANNOTATION_TEXT_OCCURRENCE('NOTE','fill',#8);
                #10=DRAUGHTING_CALLOUT('CALLOUT',(#9,#7));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"CALLOUT\"",
                "\"leader\":[[0.0,0.0,0.0],[1.0,0.0,0.0],[1.0,1.0,0.0]]");
    }

    @Test
    void shouldSampleDirectAnnotationContentInCalloutLeader() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0));
                #3=DIRECTION('DX',(1.0,0.0));
                #4=POLYLINE('PL0',(#1,#2));
                #5=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMCTX'));
                #6=REPRESENTATION('SYMREP',(#4),#5);
                #7=AXIS2_PLACEMENT_2D('MAP',#1,#3);
                #8=SYMBOL_REPRESENTATION_MAP(#7,#6);
                #9=REPRESENTATION_MAP(#7,#6);
                #10=CARTESIAN_POINT('TXT3',(3.0,4.0));
                #11=AXIS2_PLACEMENT_2D('TGT0',#10,#3);
                #12=ANNOTATION_SYMBOL('AS0',#8,#11);
                #13=CARTESIAN_POINT('TXT4',(6.0,7.0));
                #14=AXIS2_PLACEMENT_2D('TGT1',#13,#3);
                #15=ANNOTATION_TEXT('AT0',#9,#14);
                #16=ANNOTATION_TEXT_CHARACTER('ATC0',#9,#14);
                #17=(ANNOTATION_FILL_AREA('FA0',(#4))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                #18=CARTESIAN_POINT('TXT',(9.0,9.0,0.0));
                #19=ANNOTATION_TEXT_OCCURRENCE('NOTE','note',#18);
                #20=DRAUGHTING_CALLOUT('SYM_CALLOUT',(#19,#12));
                #21=DRAUGHTING_CALLOUT('TEXT_CALLOUT',(#19,#15));
                #22=DRAUGHTING_CALLOUT('TEXT_CHAR_CALLOUT',(#19,#16));
                #23=DRAUGHTING_CALLOUT('FILL_CALLOUT',(#19,#17));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"SYM_CALLOUT\"",
                "\"name\":\"TEXT_CALLOUT\"",
                "\"name\":\"TEXT_CHAR_CALLOUT\"",
                "\"name\":\"FILL_CALLOUT\"",
                "[3.0,4.0,0.0]",
                "[4.0,4.0,0.0]",
                "[6.0,7.0,0.0]",
                "[7.0,7.0,0.0]",
                "[0.0,0.0,0.0]",
                "[1.0,0.0,0.0]");
    }

    @Test
    void shouldSampleOccurrenceWrappersInCalloutLeader() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0));
                #3=DIRECTION('DX',(1.0,0.0));
                #4=POLYLINE('PL0',(#1,#2));
                #5=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMCTX'));
                #6=REPRESENTATION('SYMREP',(#4),#5);
                #7=AXIS2_PLACEMENT_2D('MAP',#1,#3);
                #8=SYMBOL_REPRESENTATION_MAP(#7,#6);
                #9=CARTESIAN_POINT('TXT3',(3.0,4.0));
                #10=AXIS2_PLACEMENT_2D('TGT0',#9,#3);
                #11=ANNOTATION_SYMBOL('AS0',#8,#10);
                #12=PRESENTATION_STYLE_ASSIGNMENT(());
                #13=ANNOTATION_SUBFIGURE_OCCURRENCE('SUB0',(#12),#11);
                #14=(DRAUGHTING_ANNOTATION_OCCURRENCE('DAO0',(#12),#11)
                    STYLED_ITEM('DAO0',(#12),#11)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('DAO0'));
                #15=CARTESIAN_POINT('TXT',(9.0,9.0,0.0));
                #16=ANNOTATION_TEXT_OCCURRENCE('NOTE','note',#15);
                #17=DRAUGHTING_CALLOUT('SUB_CALLOUT',(#16,#13));
                #18=DRAUGHTING_CALLOUT('DAO_CALLOUT',(#16,#14));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"SUB_CALLOUT\"",
                "\"name\":\"DAO_CALLOUT\"",
                "[3.0,4.0,0.0]",
                "[4.0,4.0,0.0]");
    }

    @Test
    void shouldExportAnnotationWrapperOccurrencesWithAdditionalItemsPmi() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=POINT_SET('PS0',(#1));
                #3=PRESENTATION_STYLE_ASSIGNMENT(());
                #4=(ANNOTATION_PLACEHOLDER_OCCURRENCE('PH0',(#3),#2,.ANNOTATION_TEXT.,1.0)
                    STYLED_ITEM('PH0',(#3),#2)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PH0'));
                #5=(ANNOTATION_POINT_OCCURRENCE('AP0',(#3),#1)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('AP0',(#3),#1));
                #6=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#3),#4);
                #7=ANNOTATION_SUBFIGURE_OCCURRENCE('SUB0',(#3),#5);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"ASO0\"",
                "\"name\":\"SUB0\"",
                "[1.0,2.0,3.0]");
    }

    @Test
    void shouldExportPointContainersAsPmiOccurrences() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #4=POINT_SET('PS',(#1,#2));
                #5=VERTEX_POINT('V0',#3);
                #6=VERTEX_LOOP('VL',#5);
                #7=VERTEX_SHELL('VS',#6);
                #8=GEOMETRIC_SET('GS',(#1,#2));
                #9=PRESENTATION_STYLE_ASSIGNMENT(());
                #10=GEOMETRIC_SET('PHGS',(#2));
                #11=(ANNOTATION_PLACEHOLDER_OCCURRENCE('PH',(#9),#10,.ANNOTATION_TEXT.,1.0)
                    STYLED_ITEM('PH',(#9),#10)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PH'));
                #12=DIRECTION('DX',(1.0,0.0,0.0));
                #13=DIRECTION('DY',(0.0,1.0,0.0));
                #14=DIRECTION('DZ',(0.0,0.0,1.0));
                #15=CARTESIAN_POINT('SHIFT',(5.0,0.0,0.0));
                #16=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T',#12,#13,#15,1.0,#14);
                #17=POINT_REPLICA('PR',#1,#16);
                #18=AXIS2_PLACEMENT_3D('AX',#1,#14,#12);
                #19=PLANE('PL',#18);
                #20=(ANNOTATION_PLANE((#3))
                    STYLED_ITEM('AP',(#9),#19)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('AP'));
                #23=(DRAUGHTING_ANNOTATION_OCCURRENCE('DAO_GS',(#9),#8)
                    STYLED_ITEM('DAO_GS',(#9),#8)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('DAO_GS'));
                #24=(DRAUGHTING_ANNOTATION_OCCURRENCE('DAO_PS',(#9),#4)
                    STYLED_ITEM('DAO_PS',(#9),#4)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('DAO_PS'));
                #25=(DRAUGHTING_ANNOTATION_OCCURRENCE('DAO_VS',(#9),#7)
                    STYLED_ITEM('DAO_VS',(#9),#7)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('DAO_VS'));
                #26=(DRAUGHTING_ANNOTATION_OCCURRENCE('DAO_PR',(#9),#17)
                    STYLED_ITEM('DAO_PR',(#9),#17)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('DAO_PR'));
                #27=(DRAUGHTING_ANNOTATION_OCCURRENCE('DAO_PH',(#9),#11)
                    STYLED_ITEM('DAO_PH',(#9),#11)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('DAO_PH'));
                #28=(DRAUGHTING_ANNOTATION_OCCURRENCE('DAO_AP',(#9),#20)
                    STYLED_ITEM('DAO_AP',(#9),#20)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('DAO_AP'));
                #29=ANNOTATION_OCCURRENCE_RELATIONSHIP('REL','link',#11,#20);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"DAO_GS\"",
                "\"position\":[0.0,0.0,0.0]",
                "\"name\":\"DAO_PS\"",
                "\"position\":[0.0,0.0,0.0]",
                "\"name\":\"DAO_VS\"",
                "\"position\":[2.0,0.0,0.0]",
                "\"name\":\"DAO_PH\"",
                "\"position\":[1.0,0.0,0.0]",
                "\"name\":\"DAO_AP\"",
                "\"position\":[2.0,0.0,0.0]",
                "\"name\":\"DAO_PR\"",
                "\"position\":[5.0,0.0,0.0]",
                "\"name\":\"REL\"",
                "\"position\":[2.0,0.0,0.0]");
    }

    @Test
    void shouldExportAnnotationPointLikePmi() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=CARTESIAN_POINT('P1',(4.0,5.0,6.0));
                #3=CARTESIAN_POINT('P2',(7.0,8.0,0.0));
                #4=PRESENTATION_STYLE_ASSIGNMENT(());
                #5=(ANNOTATION_POINT_OCCURRENCE('AP0',(#4),#1)
                    STYLED_ITEM('AP0',(#4),#1)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('AP0'));
                #6=GEOMETRIC_SET('GS0',(#2));
                #7=(ANNOTATION_PLACEHOLDER_OCCURRENCE('PH0',(#4),#6,.ANNOTATION_TEXT.,2.5)
                    STYLED_ITEM('PH0',(#4),#6)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PH0'));
                #8=POLYLINE('FA0',(#1,#2,#1));
                #9=(ANNOTATION_FILL_AREA('FA0',(#8))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                #10=(ANNOTATION_FILL_AREA_OCCURRENCE('FAO0',(#4),#9,#3)
                    STYLED_ITEM('FAO0',(#4),#9)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FAO0'));
                #11=CARTESIAN_POINT('S0',(9.0,10.0));
                #12=DIRECTION('DX',(1.0,0.0));
                #13=AXIS2_PLACEMENT_2D('MAP',#11,#12);
                #14=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #15=REPRESENTATION('SYM',(),#14);
                #16=SYMBOL_REPRESENTATION_MAP(#13,#15);
                #17=CARTESIAN_POINT('S1',(11.0,12.0));
                #18=DIRECTION('DX2',(1.0,0.0));
                #19=AXIS2_PLACEMENT_2D('TGT',#17,#18);
                #20=ANNOTATION_SYMBOL('AS0',#16,#19);
                #21=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#4),#20);
                #22=(DRAUGHTING_ANNOTATION_OCCURRENCE('DAO0',(#4),#20)
                    STYLED_ITEM('DAO0',(#4),#20)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('DAO0'));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"AP0\"",
                "\"position\":[1.0,2.0,3.0]",
                "\"name\":\"PH0\"",
                "\"position\":[4.0,5.0,6.0]",
                "\"name\":\"FAO0\"",
                "\"position\":[7.0,8.0,0.0]",
                "\"name\":\"ASO0\"",
                "\"position\":[11.0,12.0,0.0]",
                "\"name\":\"DAO0\"");
    }

    @Test
    void shouldExportAnnotationPlaceholderOccurrenceWithPointContainersPmi() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=POINT_SET('PS0',(#1,#2));
                #4=GEOMETRIC_CURVE_SET('GCS0',(#2));
                #5=PRESENTATION_STYLE_ASSIGNMENT(());
                #6=(ANNOTATION_PLACEHOLDER_OCCURRENCE('PH_PS',(#5),#3,.ANNOTATION_TEXT.,1.0)
                    STYLED_ITEM('PH_PS',(#5),#3)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PH_PS'));
                #7=(ANNOTATION_PLACEHOLDER_OCCURRENCE('PH_GCS',(#5),#4,.ANNOTATION_TEXT.,1.0)
                    STYLED_ITEM('PH_GCS',(#5),#4)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PH_GCS'));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"PH_PS[0]\"",
                "\"name\":\"PH_PS[1]\"",
                "\"name\":\"PH_GCS\"",
                "\"position\":[0.0,0.0,0.0]",
                "\"position\":[1.0,0.0,0.0]");
    }

    @Test
    void shouldExportAnnotationTextOccurrenceWithPointReplicaPmi() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=DIRECTION('DY',(0.0,1.0,0.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=CARTESIAN_POINT('O',(10.0,20.0,30.0));
                #6=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#2,#3,#5,2.0,#4);
                #7=POINT_REPLICA('PR0',#1,#6);
                #8=ANNOTATION_TEXT_OCCURRENCE('NOTE','replica',#7);
                #9=GEOMETRIC_CURVE_SET('LEADER',(#7,#1));
                #10=DRAUGHTING_CALLOUT('CALLOUT',(#8,#9));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE\"",
                "\"text\":\"replica\"",
                "\"position\":[12.0,24.0,36.0]",
                "\"name\":\"CALLOUT\"");
    }

    @Test
    void shouldExportAnnotationPointAndFillAreaOccurrencesWithVertexPointPmi() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=PRESENTATION_STYLE_ASSIGNMENT(());
                #4=(ANNOTATION_POINT_OCCURRENCE('AP0',(#3),#2)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('AP0',(#3),#2));
                #5=POLYLINE('B0',(#1,#1));
                #6=(ANNOTATION_FILL_AREA('FA0',(#5))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                #7=(ANNOTATION_FILL_AREA_OCCURRENCE('FAO0',(#3),#6,#2)
                    STYLED_ITEM('FAO0',(#3),#6)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FAO0'));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"AP0\"",
                "\"name\":\"FAO0\"",
                "\"position\":[1.0,2.0,3.0]");
    }

    @Test
    void shouldExportAnnotationPointLikeOccurrencesWithVertexShellPmi() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=VERTEX_LOOP('VL0',#2);
                #4=VERTEX_SHELL('VS0',#3);
                #5=PRESENTATION_STYLE_ASSIGNMENT(());
                #6=(ANNOTATION_POINT_OCCURRENCE('AP0',(#5),#4)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('AP0',(#5),#4));
                #7=POLYLINE('B0',(#1,#1));
                #8=(ANNOTATION_FILL_AREA('FA0',(#7))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                #9=(ANNOTATION_FILL_AREA_OCCURRENCE('FAO0',(#5),#8,#4)
                    STYLED_ITEM('FAO0',(#5),#8)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FAO0'));
                #10=ANNOTATION_TEXT_OCCURRENCE('NOTE','vertex-shell',#4);
                #11=GEOMETRIC_CURVE_SET('LEADER',(#1));
                #12=DRAUGHTING_CALLOUT('CALLOUT',(#10,#11));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"AP0\"",
                "\"name\":\"FAO0\"",
                "\"name\":\"NOTE\"",
                "\"text\":\"vertex-shell\"",
                "\"position\":[1.0,2.0,3.0]",
                "\"name\":\"CALLOUT\"");
    }

    @Test
    void shouldExportPointLikeAnnotationOccurrenceCarriersPmi() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=PRESENTATION_STYLE_ASSIGNMENT(());
                #3=(ANNOTATION_POINT_OCCURRENCE('AP_BASE',(#2),#1)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('AP_BASE',(#2),#1));
                #4=ANNOTATION_TEXT_OCCURRENCE('NOTE','nested',#3);
                #5=POLYLINE('B0',(#1,#1));
                #6=(ANNOTATION_FILL_AREA('FA0',(#5))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                #7=(ANNOTATION_FILL_AREA_OCCURRENCE('FAO0',(#2),#6,#4)
                    STYLED_ITEM('FAO0',(#2),#6)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FAO0'));
                #8=(ANNOTATION_POINT_OCCURRENCE('AP_NESTED',(#2),#7)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('AP_NESTED',(#2),#7));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"AP_BASE\"",
                "\"name\":\"NOTE\"",
                "\"text\":\"nested\"",
                "\"name\":\"FAO0\"",
                "\"name\":\"AP_NESTED\"",
                "\"position\":[1.0,2.0,3.0]");
    }

    @Test
    void shouldExportDirectAnnotationContentPointCarriersPmi() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','ANN'));
                #2=REPRESENTATION('REP',(),#1);
                #3=CARTESIAN_POINT('O',(0.0,0.0));
                #4=DIRECTION('X',(1.0,0.0));
                #5=AXIS2_PLACEMENT_2D('MAP',#3,#4);
                #6=SYMBOL_REPRESENTATION_MAP(#5,#2);
                #7=CARTESIAN_POINT('P0',(10.0,20.0));
                #8=AXIS2_PLACEMENT_2D('TGT0',#7,#4);
                #9=ANNOTATION_SYMBOL('AS0',#6,#8);
                #10=REPRESENTATION_MAP(#5,#2);
                #11=CARTESIAN_POINT('P1',(30.0,40.0));
                #12=AXIS2_PLACEMENT_2D('TGT1',#11,#4);
                #13=ANNOTATION_TEXT('AT0',#10,#12);
                #14=ANNOTATION_TEXT_CHARACTER('ATC0',#10,#12);
                #15=CARTESIAN_POINT('F0',(0.0,0.0,0.0));
                #16=CARTESIAN_POINT('F1',(1.0,0.0,0.0));
                #17=CARTESIAN_POINT('F2',(1.0,1.0,0.0));
                #18=POLYLINE('B0',(#15,#16,#17));
                #19=(ANNOTATION_FILL_AREA('FA0',(#18))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                #20=PRESENTATION_STYLE_ASSIGNMENT(());
                #21=ANNOTATION_TEXT_OCCURRENCE('NOTE','symbol-pos',#9);
                #22=(ANNOTATION_POINT_OCCURRENCE('AP0',(#20),#13)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('AP0',(#20),#13));
                #23=(ANNOTATION_FILL_AREA_OCCURRENCE('FAO0',(#20),#19,#14)
                    STYLED_ITEM('FAO0',(#20),#19)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FAO0'));
                #24=POINT_SET('PS0',(#9,#19));
                #25=DIRECTION('N',(0.0,0.0,1.0));
                #26=DIRECTION('X3',(1.0,0.0,0.0));
                #27=AXIS2_PLACEMENT_3D('AX',#15,#25,#26);
                #28=PLANE('PL0',#27);
                #29=(ANNOTATION_PLANE((#9,#19))
                    STYLED_ITEM('AP',(#20),#28)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('AP'));
                #30=GEOMETRIC_CURVE_SET('LEADER',(#15));
                #31=DRAUGHTING_CALLOUT('CALLOUT',(#21,#22,#23,#24,#29,#30));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE\"",
                "\"text\":\"symbol-pos\"",
                "\"name\":\"AP0\"",
                "\"name\":\"FAO0\"",
                "\"name\":\"PS0[0]\"",
                "\"name\":\"PS0[1]\"",
                "\"name\":\"AP[0]\"",
                "\"name\":\"AP[1]\"",
                "\"name\":\"CALLOUT\"",
                "\"position\":[10.0,20.0,0.0]",
                "\"position\":[30.0,40.0,0.0]",
                "\"position\":[0.0,0.0,0.0]");
    }

    @Test
    void shouldExportPointSetWithPointLikeAnnotationOccurrenceCarriersPmi() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=PRESENTATION_STYLE_ASSIGNMENT(());
                #3=(ANNOTATION_POINT_OCCURRENCE('AP0',(#2),#1)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('AP0',(#2),#1));
                #4=ANNOTATION_TEXT_OCCURRENCE('NOTE','TXT',#3);
                #5=DIRECTION('N',(0.0,0.0,1.0));
                #6=DIRECTION('X',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX',#1,#5,#6);
                #8=PLANE('PL0',#7);
                #9=(ANNOTATION_PLANE((#4))
                    STYLED_ITEM('AP',(#2),#8)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('AP'));
                #10=POINT_SET('PS0',(#3,#9));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"PS0[0]\"",
                "\"name\":\"PS0[1]\"",
                "\"name\":\"NOTE\"",
                "\"position\":[1.0,2.0,3.0]");
    }

    @Test
    void shouldExportAnnotationPointLikeOccurrencesWithContainerCarriersPmi() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,0.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=POINT_SET('PS0',(#2));
                #4=GEOMETRIC_CURVE_SET('GCS0',(#2));
                #5=GEOMETRIC_SET('GS0',(#3,#4));
                #6=PRESENTATION_STYLE_ASSIGNMENT(());
                #7=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#3);
                #8=(ANNOTATION_POINT_OCCURRENCE('AP0',(#6),#4)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('AP0',(#6),#4));
                #9=POLYLINE('B0',(#1,#1));
                #10=(ANNOTATION_FILL_AREA('FA0',(#9))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                #11=(ANNOTATION_FILL_AREA_OCCURRENCE('FAO0',(#6),#10,#5)
                    STYLED_ITEM('FAO0',(#6),#10)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FAO0'));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE\"",
                "\"name\":\"AP0\"",
                "\"name\":\"FAO0\"",
                "\"position\":[1.0,2.0,0.0]");
    }

    @Test
    void shouldExportNestedPointSetAndAnnotationPlaneOccurrenceElementsPmi() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=POINT_SET('INNER',(#2));
                #4=GEOMETRIC_CURVE_SET('GCS0',(#1));
                #5=GEOMETRIC_SET('GS0',(#3,#4));
                #6=POINT_SET('OUTER',(#3,#5));
                #7=DIRECTION('N',(0.0,0.0,1.0));
                #8=DIRECTION('X',(1.0,0.0,0.0));
                #9=AXIS2_PLACEMENT_3D('AX',#1,#7,#8);
                #10=PLANE('PL0',#9);
                #11=PRESENTATION_STYLE_ASSIGNMENT(());
                #12=ANNOTATION_TEXT_OCCURRENCE('NOTE','TXT',#6);
                #13=(ANNOTATION_POINT_OCCURRENCE('AP0',(#11),#5) ANNOTATION_OCCURRENCE());
                #14=(ANNOTATION_PLANE((#12,#13))
                    STYLED_ITEM('AP',(#11),#10)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('AP'));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"OUTER[0]\"",
                "\"name\":\"OUTER[1]\"",
                "\"name\":\"AP[0]\"",
                "\"name\":\"AP[1]\"",
                "\"position\":[1.0,2.0,3.0]");
    }

    @Test
    void shouldExportAnnotationTextOccurrenceWithVertexPointPmi() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=ANNOTATION_TEXT_OCCURRENCE('NOTE','vertex',#2);
                #4=GEOMETRIC_CURVE_SET('LEADER',(#1));
                #5=DRAUGHTING_CALLOUT('CALLOUT',(#3,#4));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE\"",
                "\"text\":\"vertex\"",
                "\"position\":[1.0,2.0,3.0]",
                "\"name\":\"CALLOUT\"");
    }

    @Test
    void shouldSampleGeometricCurveSetLeaderWithVertexPoint() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=ANNOTATION_TEXT_OCCURRENCE('NOTE','vertex-leader',#2);
                #4=GEOMETRIC_CURVE_SET('LEADER',(#2));
                #5=DRAUGHTING_CALLOUT('CALLOUT',(#3,#4));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE\"",
                "\"position\":[1.0,2.0,3.0]",
                "\"leader\":[[1.0,2.0,3.0]]");
    }

    @Test
    void shouldSamplePointContainersAndPlaneInCalloutLeader() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=ANNOTATION_TEXT_OCCURRENCE('NOTE','container-leader',#1);
                #4=POINT_SET('PS',(#2));
                #5=GEOMETRIC_SET('GS',(#2));
                #6=PRESENTATION_STYLE_ASSIGNMENT(());
                #7=(ANNOTATION_PLACEHOLDER_OCCURRENCE('PH',(#6),#5,.ANNOTATION_TEXT.,1.0)
                    STYLED_ITEM('PH',(#6),#5)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PH'));
                #8=DIRECTION('DZ',(0.0,0.0,1.0));
                #9=DIRECTION('DX',(1.0,0.0,0.0));
                #10=AXIS2_PLACEMENT_3D('AX',#1,#8,#9);
                #11=PLANE('PL0',#10);
                #12=(ANNOTATION_PLANE((#2))
                    STYLED_ITEM('AP',(#6),#11)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('AP'));
                #13=DRAUGHTING_CALLOUT('PS_CALLOUT',(#3,#4));
                #14=DRAUGHTING_CALLOUT('PH_CALLOUT',(#3,#7));
                #15=DRAUGHTING_CALLOUT('AP_CALLOUT',(#3,#12));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"PS_CALLOUT\"",
                "\"name\":\"PH_CALLOUT\"",
                "\"name\":\"AP_CALLOUT\"",
                "\"leader\":[[1.0,0.0,0.0]]");
    }

    @Test
    void shouldSampleGeometricSetInCalloutLeader() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=ANNOTATION_TEXT_OCCURRENCE('NOTE','gs-leader',#1);
                #4=GEOMETRIC_SET('GS',(#2));
                #5=DRAUGHTING_CALLOUT('GS_CALLOUT',(#3,#4));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"GS_CALLOUT\"",
                "\"leader\":[[1.0,0.0,0.0]]");
    }

    @Test
    void shouldSamplePathAndWireContentsInCalloutLeader() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=VECTOR('VX',#3,1.0);
                #5=LINE('L0',#1,#4);
                #6=VERTEX_POINT('V0',#1);
                #7=VERTEX_POINT('V1',#2);
                #8=EDGE_CURVE('E0',#6,#7,#5,.T.);
                #9=ORIENTED_EDGE('OE0',$,$,#8,.T.);
                #10=ORIENTED_EDGE('OE1',$,$,#8,.F.);
                #11=PATH('PTH',(#9));
                #12=CONNECTED_EDGE_SET('CES0',(#8));
                #13=EDGE_LOOP('EL0',(#9,#10));
                #14=WIRE_SHELL('WS0',(#13));
                #15=ANNOTATION_TEXT_OCCURRENCE('NOTE','topology-leader',#1);
                #16=DRAUGHTING_CALLOUT('PATH_CALLOUT',(#15,#11));
                #17=DRAUGHTING_CALLOUT('EDGESET_CALLOUT',(#15,#12));
                #18=DRAUGHTING_CALLOUT('WIRE_CALLOUT',(#15,#14));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"PATH_CALLOUT\"",
                "\"name\":\"EDGESET_CALLOUT\"",
                "\"name\":\"WIRE_CALLOUT\"",
                "[0.0,0.0,0.0]",
                "[1.0,0.0,0.0]");
    }

    @Test
    void shouldSampleExtendedContainerContentsInCalloutLeader() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=VERTEX_POINT('V0',#1);
                #5=VERTEX_POINT('V1',#2);
                #6=VERTEX_POINT('V2',#3);
                #7=DIRECTION('DX',(1.0,0.0,0.0));
                #8=DIRECTION('DY',(0.0,1.0,0.0));
                #9=VECTOR('VX',#7,1.0);
                #10=VECTOR('VY',#8,1.0);
                #11=LINE('L0',#1,#9);
                #12=LINE('L1',#2,#10);
                #13=EDGE_CURVE('E0',#4,#5,#11,.T.);
                #14=EDGE_CURVE('E1',#5,#6,#12,.T.);
                #15=CONNECTED_EDGE_SET('CES0',(#13,#14));
                #16=(EDGE_BASED_WIREFRAME_MODEL('EBWM',(#15)) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('EBWM'));
                #17=POLY_LOOP('PL0',(#1,#2,#3));
                #18=VERTEX_LOOP('VL0',#4);
                #19=VERTEX_SHELL('VS0',#18);
                #20=WIRE_SHELL('WS0',(#17));
                #21=SHELL_BASED_WIREFRAME_MODEL('SBWM',(#20,#19));
                #22=ANNOTATION_TEXT_OCCURRENCE('NOTE','extended-leader',#1);
                #23=DRAUGHTING_CALLOUT('PL_CALLOUT',(#22,#17));
                #24=DRAUGHTING_CALLOUT('VL_CALLOUT',(#22,#18));
                #25=DRAUGHTING_CALLOUT('VS_CALLOUT',(#22,#19));
                #26=DRAUGHTING_CALLOUT('EBWM_CALLOUT',(#22,#16));
                #27=DRAUGHTING_CALLOUT('SBWM_CALLOUT',(#22,#21));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"PL_CALLOUT\"",
                "\"name\":\"VL_CALLOUT\"",
                "\"name\":\"VS_CALLOUT\"",
                "\"name\":\"EBWM_CALLOUT\"",
                "\"name\":\"SBWM_CALLOUT\"",
                "[0.0,0.0,0.0]",
                "[1.0,0.0,0.0]",
                "[1.0,1.0,0.0]");
    }

    @Test
    void shouldSampleDirectGeometryContentsInCalloutLeader() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(2.0,1.0,0.0));
                #5=VERTEX_POINT('V0',#1);
                #6=VERTEX_POINT('V1',#2);
                #7=DIRECTION('DX',(1.0,0.0,0.0));
                #8=DIRECTION('DY',(0.0,1.0,0.0));
                #9=DIRECTION('DZ',(0.0,0.0,1.0));
                #10=VECTOR('VX',#7,1.0);
                #11=LINE('L0',#1,#10);
                #12=POLYLINE('PL0',(#1,#2,#3,#4));
                #13=EDGE_CURVE('E0',#5,#6,#11,.T.);
                #14=ORIENTED_EDGE('OE0',$,$,#13,.T.);
                #15=CARTESIAN_POINT('O',(10.0,0.0,0.0));
                #16=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#7,#8,#15,1.0,#9);
                #17=POINT_REPLICA('PR0',#1,#16);
                #18=ANNOTATION_TEXT_OCCURRENCE('NOTE','direct',#1);
                #19=DRAUGHTING_CALLOUT('DIRECT_CALLOUT',(#18,#1,#5,#17,#11,#12,#13,#14));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"DIRECT_CALLOUT\"",
                "[0.0,0.0,0.0]",
                "[1.0,0.0,0.0]",
                "[1.0,1.0,0.0]",
                "[2.0,1.0,0.0]",
                "[10.0,0.0,0.0]");
    }

    @Test
    void shouldSampleAdvancedCurveContentsInCalloutLeader() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=DIRECTION('DY',(0.0,1.0,0.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=VECTOR('VX',#3,1.0);
                #7=LINE('L0',#1,#6);
                #8=TRIMMED_CURVE('TC0',#7,(#1),(#2),.T.,.CARTESIAN.);
                #9=ORIENTED_CURVE('OC0',#8,.F.);
                #10=CARTESIAN_POINT('T0',(10.0,0.0,0.0));
                #11=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',#3,#4,#10,1.0,#5);
                #12=CURVE_REPLICA('CR0',#8,#11);
                #13=ANNOTATION_TEXT_OCCURRENCE('NOTE','advanced',#1);
                #14=DRAUGHTING_CALLOUT('ADV_CALLOUT',(#13,#8,#9,#12));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"ADV_CALLOUT\"",
                "[0.0,0.0,0.0]",
                "[1.0,0.0,0.0]",
                "[10.0,0.0,0.0]",
                "[11.0,0.0,0.0]");
    }

    @Test
    void shouldSampleFaceShellAndSurfaceModelContentsInCalloutLeader() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #13=PLANE('PL',#12);
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
                #80=ADVANCED_FACE('AF0',(#71),#13,.T.);
                #81=ORIENTED_FACE('OF0',#80,.F.);
                #82=FACE_SURFACE('FS0',(#71),#13,.T.);
                #83=OPEN_SHELL('OS0',(#80));
                #84=SURFACED_OPEN_SHELL('SOS0',(#82));
                #85=ORIENTED_OPEN_SHELL('OOS0',#83,.F.);
                #86=CLOSED_SHELL('CS0',(#80));
                #87=ORIENTED_CLOSED_SHELL('OCS0',#86,.F.);
                #88=CONNECTED_FACE_SET('CFS0',(#80));
                #89=CONNECTED_FACE_SUB_SET('CFSS0',(#80),#88);
                #90=(FACE_BASED_SURFACE_MODEL('FBSM0',(#88,#83))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FBSM0'));
                #91=SHELL_BASED_SURFACE_MODEL('SBSM0',(#83,#84,#85,#86,#87));
                #92=ANNOTATION_TEXT_OCCURRENCE('NOTE','shell-face',#1);
                #93=DRAUGHTING_CALLOUT('AF_CALLOUT',(#92,#80));
                #94=DRAUGHTING_CALLOUT('OF_CALLOUT',(#92,#81));
                #95=DRAUGHTING_CALLOUT('FS_CALLOUT',(#92,#82));
                #96=DRAUGHTING_CALLOUT('OS_CALLOUT',(#92,#83));
                #97=DRAUGHTING_CALLOUT('SOS_CALLOUT',(#92,#84));
                #98=DRAUGHTING_CALLOUT('OOS_CALLOUT',(#92,#85));
                #99=DRAUGHTING_CALLOUT('CS_CALLOUT',(#92,#86));
                #100=DRAUGHTING_CALLOUT('OCS_CALLOUT',(#92,#87));
                #101=DRAUGHTING_CALLOUT('CFS_CALLOUT',(#92,#88));
                #102=DRAUGHTING_CALLOUT('CFSS_CALLOUT',(#92,#89));
                #103=DRAUGHTING_CALLOUT('FBSM_CALLOUT',(#92,#90));
                #104=DRAUGHTING_CALLOUT('SBSM_CALLOUT',(#92,#91));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"AF_CALLOUT\"",
                "\"name\":\"OF_CALLOUT\"",
                "\"name\":\"FS_CALLOUT\"",
                "\"name\":\"OS_CALLOUT\"",
                "\"name\":\"SOS_CALLOUT\"",
                "\"name\":\"OOS_CALLOUT\"",
                "\"name\":\"CS_CALLOUT\"",
                "\"name\":\"OCS_CALLOUT\"",
                "\"name\":\"CFS_CALLOUT\"",
                "\"name\":\"CFSS_CALLOUT\"",
                "\"name\":\"FBSM_CALLOUT\"",
                "\"name\":\"SBSM_CALLOUT\"",
                "\"viaDefinitionType\":\"ADVANCED_FACE\"",
                "\"viaDefinitionId\":80",
                "\"viaDefinitionType\":\"ORIENTED_FACE\"",
                "\"viaDefinitionId\":81",
                "\"viaDefinitionType\":\"FACE_SURFACE\"",
                "\"viaDefinitionId\":82",
                "\"viaDefinitionType\":\"OPEN_SHELL\"",
                "\"viaDefinitionId\":83",
                "\"viaDefinitionType\":\"SURFACED_OPEN_SHELL\"",
                "\"viaDefinitionId\":84",
                "\"viaDefinitionType\":\"ORIENTED_OPEN_SHELL\"",
                "\"viaDefinitionId\":85",
                "\"viaDefinitionType\":\"CLOSED_SHELL\"",
                "\"viaDefinitionId\":86",
                "\"viaDefinitionType\":\"ORIENTED_CLOSED_SHELL\"",
                "\"viaDefinitionId\":87",
                "\"viaDefinitionType\":\"CONNECTED_FACE_SET\"",
                "\"viaDefinitionId\":88",
                "\"viaDefinitionType\":\"CONNECTED_FACE_SUB_SET\"",
                "\"viaDefinitionId\":89",
                "\"viaDefinitionType\":\"FACE_BASED_SURFACE_MODEL\"",
                "\"viaDefinitionId\":90",
                "\"viaDefinitionType\":\"SHELL_BASED_SURFACE_MODEL\"",
                "\"viaDefinitionId\":91",
                "[0.0,0.0,0.0]",
                "[1.0,0.0,0.0]",
                "[1.0,1.0,0.0]",
                "[0.0,1.0,0.0]");
    }

    @Test
    void shouldSampleBrepSolidContentsInCalloutLeader() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #13=PLANE('PL',#12);
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
                #80=ADVANCED_FACE('AF0',(#71),#13,.T.);
                #81=CLOSED_SHELL('CS0',(#80));
                #82=MANIFOLD_SOLID_BREP('MSB0',#81);
                #83=BREP_WITH_VOIDS('BWV0',#81,());
                #84=ANNOTATION_TEXT_OCCURRENCE('NOTE','solid-callout',#1);
                #85=DRAUGHTING_CALLOUT('MSB_CALLOUT',(#84,#82));
                #86=DRAUGHTING_CALLOUT('BWV_CALLOUT',(#84,#83));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"MSB_CALLOUT\"",
                "\"name\":\"BWV_CALLOUT\"",
                "[0.0,0.0,0.0]",
                "[1.0,0.0,0.0]",
                "[1.0,1.0,0.0]",
                "[0.0,1.0,0.0]");
    }

    @Test
    void shouldSampleBuildableSolidFamilyContentsInCalloutLeader() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=BLOCK('BLK',#4,10.0,20.0,30.0);
                #6=CARTESIAN_POINT('PZ',(0.0,0.0,15.0));
                #7=AXIS2_PLACEMENT_3D('PLAX',#6,#2,#3);
                #8=PLANE('PLANE',#7);
                #9=HALF_SPACE_SOLID('HS',#8,.T.);
                #10=(BOOLEAN_RESULT(.DIFFERENCE.,#5,#9) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BOOL0'));
                #11=CSG_SOLID('CSG0',#10);
                #12=(BOOLEAN_CLIPPING_RESULT(.DIFFERENCE.,#5,#9) BOOLEAN_RESULT(.DIFFERENCE.,#5,#9) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BCR0'));
                #20=CARTESIAN_POINT('P2D',(0.0,0.0));
                #21=DIRECTION('DX2',(1.0,0.0));
                #22=AXIS2_PLACEMENT_2D('PROFILE_AX',#20,#21);
                #23=RECTANGLE_PROFILE_DEF(.AREA.,'RPD',#22,4.0,2.0);
                #24=DIRECTION('DIR',(0.0,0.0,1.0));
                #25=EXTRUDED_AREA_SOLID('EX0',#23,#4,#24,5.0);
                #26=CARTESIAN_POINT('T',(10.0,0.0,0.0));
                #27=DIRECTION('DY',(0.0,1.0,0.0));
                #28=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',#3,#27,#26,1.0,#2);
                #29=SOLID_REPLICA('SR0',#25,#28);
                #30=ANNOTATION_TEXT_OCCURRENCE('NOTE','solid-family',#1);
                #31=DRAUGHTING_CALLOUT('PRIM_CALLOUT',(#30,#5));
                #32=DRAUGHTING_CALLOUT('BOOL_CALLOUT',(#30,#10));
                #33=DRAUGHTING_CALLOUT('CSG_CALLOUT',(#30,#11));
                #34=DRAUGHTING_CALLOUT('BCR_CALLOUT',(#30,#12));
                #35=DRAUGHTING_CALLOUT('EX_CALLOUT',(#30,#25));
                #36=DRAUGHTING_CALLOUT('SR_CALLOUT',(#30,#29));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"PRIM_CALLOUT\"",
                "\"name\":\"BOOL_CALLOUT\"",
                "\"name\":\"CSG_CALLOUT\"",
                "\"name\":\"BCR_CALLOUT\"",
                "\"name\":\"EX_CALLOUT\"",
                "\"name\":\"SR_CALLOUT\"",
                "[0.0,0.0,0.0]",
                "[10.0,0.0,0.0]",
                "[10.0,20.0,0.0]",
                "[0.0,20.0,0.0]");
    }

    @Test
    void shouldExportAnnotationPlanePmiThroughNestedPointContainers() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('N',(0.0,0.0,1.0));
                #3=DIRECTION('X',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('AP',#4);
                #6=CARTESIAN_POINT('P0',(1.0,2.0,0.0));
                #7=VERTEX_POINT('VP0',#6);
                #8=POINT_SET('PS0',(#7));
                #9=GEOMETRIC_CURVE_SET('GCS0',(#7));
                #10=PRESENTATION_STYLE_ASSIGNMENT(());
                #11=(ANNOTATION_PLANE((#8,#9))
                    STYLED_ITEM('AP',(#10),#5)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('AP'));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"AP[0]\"",
                "\"name\":\"AP[1]\"",
                "\"position\":[1.0,2.0,0.0]");
    }

    @Test
    void shouldExportAnnotationPlanePmiThroughNestedGeometricSet() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('N',(0.0,0.0,1.0));
                #3=DIRECTION('X',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('AP',#4);
                #6=CARTESIAN_POINT('P0',(1.0,2.0,0.0));
                #7=VERTEX_POINT('VP0',#6);
                #8=POINT_SET('PS0',(#7));
                #9=GEOMETRIC_CURVE_SET('GCS0',(#7));
                #10=GEOMETRIC_SET('GS0',(#8,#9));
                #11=PRESENTATION_STYLE_ASSIGNMENT(());
                #12=(ANNOTATION_PLANE((#10))
                    STYLED_ITEM('AP',(#11),#5)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('AP'));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"PS0\"",
                "\"name\":\"AP\"",
                "\"position\":[1.0,2.0,0.0]");
    }

    @Test
    void shouldExportAnnotationTextOccurrenceWithVertexBackedPointReplicaPmi() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=DIRECTION('DY',(0.0,1.0,0.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=CARTESIAN_POINT('O',(10.0,20.0,30.0));
                #7=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#3,#4,#6,2.0,#5);
                #8=POINT_REPLICA('PR0',#2,#7);
                #9=ANNOTATION_TEXT_OCCURRENCE('NOTE','vertex-replica',#8);
                #10=GEOMETRIC_CURVE_SET('LEADER',(#8,#1));
                #11=DRAUGHTING_CALLOUT('CALLOUT',(#9,#10));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE\"",
                "\"text\":\"vertex-replica\"",
                "\"position\":[12.0,24.0,36.0]",
                "\"name\":\"CALLOUT\"");
    }

    @Test
    void shouldExportPlaceholderPmiThroughNestedPointAndCurveSets() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=POINT_SET('PS0',(#2));
                #4=GEOMETRIC_CURVE_SET('GCS0',(#2));
                #5=GEOMETRIC_SET('GS0',(#3,#4));
                #6=PRESENTATION_STYLE_ASSIGNMENT(());
                #7=(ANNOTATION_PLACEHOLDER_OCCURRENCE('PH0',(#6),#5,.ANNOTATION_TEXT.,1.0)
                    STYLED_ITEM('PH0',(#6),#5)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PH0'));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"PH0[0]\"",
                "\"position\":[1.0,2.0,3.0]");
    }

    @Test
    void shouldExportDirectCurveEntitiesAsStandaloneEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=ORIENTED_CURVE('OC0',#4,.F.);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #11=SHAPE_REPRESENTATION('WIRE',(#4,#5),#10);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"representationCount\":1",
                "\"representations\":[{\"id\":11",
                "\"edges\":[{\"id\":4",
                "\"points\":[[0.0,0.0,0.0],[1.0,0.0,0.0]]",
                "\"points\":[[1.0,0.0,0.0],[0.0,0.0,0.0]]");
    }

    @Test
    void shouldExportConicCurvesAsStandaloneEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=PARABOLA('PAR0',#4,2.0);
                #6=HYPERBOLA('HYP0',#4,4.0,2.0);
                #10=GEOMETRIC_CURVE_SET('WIRE',(#5,#6));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":2",
                "\"unsupportedFaceCount\":0",
                "[0.0,-8.0,0.0]",
                "[0.0,0.0,0.0]",
                "[11.85675323891235,-5.580828732555284,0.0]",
                "[4.0,0.0,0.0]");
    }

    @Test
    void shouldExportGeometricSetTopologyMembersAsStandaloneEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=CYLINDRICAL_SURFACE('CYL',#4,2.0);
                #6=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #7=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #8=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #9=VERTEX_POINT('V0',#6);
                #10=VERTEX_POINT('V1',#7);
                #11=VECTOR('VX',#3,1.0);
                #12=LINE('L0',#6,#11);
                #13=EDGE_CURVE('E0',#9,#10,#12,.T.);
                #14=(SUBEDGE('SE0',#9,#10,#13) EDGE() TOPOLOGICAL_REPRESENTATION_ITEM('subedge'));
                #15=ORIENTED_EDGE('OE0',*,*,#13,.T.);
                #16=PATH('PTH',(#15));
                #17=ORIENTED_PATH('OP0',#16,.T.);
                #18=POLY_LOOP('PL0',(#6,#7,#8));
                #19=GEOMETRIC_SET('GS0',(#5,#14,#17,#18));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":2",
                "\"unsupportedFaceCount\":0",
                "\"id\":13",
                "\"id\":18");
    }

    @Test
    void shouldExportGeometricSetsWithWireAndLoopContainersAsStandaloneEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=VERTEX_POINT('V0',#1);
                #4=VERTEX_POINT('V1',#2);
                #5=DIRECTION('DX',(1.0,0.0,0.0));
                #6=VECTOR('VX',#5,1.0);
                #7=LINE('L0',#1,#6);
                #8=EDGE_CURVE('E0',#3,#4,#7,.T.);
                #9=ORIENTED_EDGE('OE0',*,*,#8,.T.);
                #10=ORIENTED_EDGE('OE1',*,*,#8,.F.);
                #11=CONNECTED_EDGE_SET('CES0',(#8,#9));
                #12=VERTEX_LOOP('VL0',#3);
                #13=EDGE_LOOP('EL0',(#9,#10));
                #14=WIRE_SHELL('WS0',(#13,#12));
                #15=GEOMETRIC_CURVE_SET('GCS0',(#11,#13));
                #16=GEOMETRIC_SET('GS0',(#11,#14,#15));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":1",
                "\"unsupportedFaceCount\":0",
                "\"id\":8");
    }

    @Test
    void shouldExportGeometricCurveSetWithWireContainersAndWireframeModelsAsStandaloneEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=VERTEX_POINT('V0',#1);
                #4=VERTEX_POINT('V1',#2);
                #5=DIRECTION('DX',(1.0,0.0,0.0));
                #6=VECTOR('VX',#5,1.0);
                #7=LINE('L0',#1,#6);
                #8=EDGE_CURVE('E0',#3,#4,#7,.T.);
                #9=ORIENTED_EDGE('OE0',*,*,#8,.T.);
                #10=ORIENTED_EDGE('OE1',*,*,#8,.F.);
                #11=CONNECTED_EDGE_SET('CES0',(#8,#9));
                #12=EDGE_LOOP('EL0',(#9,#10));
                #13=VERTEX_LOOP('VL0',#3);
                #14=VERTEX_SHELL('VS0',#13);
                #15=WIRE_SHELL('WS0',(#12,#13));
                #16=(EDGE_BASED_WIREFRAME_MODEL('EBWM',(#11))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('EBWM'));
                #17=SHELL_BASED_WIREFRAME_MODEL('SBWM',(#15,#14));
                #18=GEOMETRIC_CURVE_SET('GCS0',(#11,#12,#13,#15,#16,#17));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":1",
                "\"unsupportedFaceCount\":0",
                "\"id\":8");
    }

    @Test
    void shouldExportGeometricSetWithShellModelAndSolidMembersPreviewMetadata() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX',#1,#5,#6);
                #8=PLANE('PL0',#7);
                #9=POLY_LOOP('LOOP',(#1,#2,#3,#4));
                #10=FACE_OUTER_BOUND('FOB',#9,.T.);
                #11=ADVANCED_FACE('FACE0',(#10),#8,.T.);
                #12=OPEN_SHELL('OSH',(#11));
                #13=(FACE_BASED_SURFACE_MODEL('FBM',(#12))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FBM'));
                #14=BLOCK('BLK',#7,1.0,1.0,1.0);
                #15=GEOMETRIC_SET('GS0',(#12,#13,#14));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"faceCount\":7",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportNestedGeometricSetMembersThroughCalloutLeader() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=VECTOR('VX',#3,1.0);
                #5=LINE('L0',#1,#4);
                #6=POINT_SET('PS0',(#1));
                #7=GEOMETRIC_CURVE_SET('INNER_GCS',(#5));
                #8=GEOMETRIC_SET('INNER_GS',(#6,#7));
                #9=GEOMETRIC_CURVE_SET('OUTER_GCS',(#6,#8,#7));
                #10=ANNOTATION_TEXT_OCCURRENCE('NOTE','nested',#1);
                #11=DRAUGHTING_CALLOUT('CAL',(#10,#9));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"CAL\"",
                "\"text\":\"nested\"",
                "\"leader\":[",
                "\"position\":[0.0,0.0,0.0]");
    }

    @Test
    void shouldExportEdgeCurveBackedByHyperbola() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=HYPERBOLA('HYP0',#4,4.0,2.0);
                #6=CARTESIAN_POINT('S',(4.0,0.0,0.0));
                #7=CARTESIAN_POINT('E',(11.85675323891235,5.580828732555284,0.0));
                #8=VERTEX_POINT('VS',#6);
                #9=VERTEX_POINT('VE',#7);
                #10=EDGE_CURVE('E0',#8,#9,#5,.T.);
                #11=CONNECTED_EDGE_SET('CES',(#10));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":1",
                "\"id\":10",
                "[4.0,0.0,0.0]",
                "[11.85675323891235,5.580828732555284,0.0]");
    }

    @Test
    void shouldExportConicBackedSurfaceAndCompositeCurves() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=PARABOLA('PAR0',#4,2.0);
                #6=HYPERBOLA('HYP0',#4,4.0,2.0);
                #7=SURFACE_CURVE('SC0',#5,(),.T.);
                #8=COMPOSITE_CURVE_SEGMENT(.CONTINUOUS.,.T.,#6);
                #9=(COMPOSITE_CURVE('CC0',(#8),.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('cc-name'));
                #10=GEOMETRIC_CURVE_SET('WIRE',(#7,#9));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":2",
                "[0.0,0.0,0.0]",
                "[11.85675323891235,5.580828732555284,0.0]");
    }

    @Test
    void shouldExportDegenerateConicAsStandaloneEdge() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=DEGENERATE_CONIC('DC0',#4);
                #10=GEOMETRIC_CURVE_SET('WIRE',(#5));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":1",
                "[1.0,2.0,3.0],[1.0,2.0,3.0]");
    }

    @Test
    void shouldExportConicBackedSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=HYPERBOLA('HYP0',#4,4.0,2.0);
                #6=PLANE('PL0',#4);
                #7=CARTESIAN_POINT('UV0',(0.0,0.0));
                #8=DIRECTION('UX',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('AX2',#7,#8);
                #10=HYPERBOLA('H2A',#9,4.0,2.0);
                #11=HYPERBOLA('H2B',#9,4.0,2.0);
                #12=REPRESENTATION_CONTEXT('PC','PARAMETRIC');
                #13=DEFINITIONAL_REPRESENTATION('DR0',(#10),#12);
                #14=DEFINITIONAL_REPRESENTATION('DR1',(#11),#12);
                #15=PCURVE('PC0',#6,#13);
                #16=PCURVE('PC1',#6,#14);
                #17=SEAM_CURVE('SEAM0',#5,(#15,#16),.PCURVE_S1.);
                #18=GEOMETRIC_CURVE_SET('WIRE',(#17));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":1",
                "[4.0,0.0,0.0]",
                "[11.85675323891235,5.580828732555284,0.0]");
    }

    @Test
    void shouldExportPlanarFaceWithDegeneratePcurveAssociatedGeometry() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=PLANE('PL0',#7);
                #9=VERTEX_POINT('V0',#1);
                #10=VERTEX_POINT('V1',#2);
                #11=VERTEX_POINT('V2',#3);
                #12=VERTEX_POINT('V3',#4);
                #13=VECTOR('VX',#6,1.0);
                #14=LINE('L0',#1,#13);
                #15=DIRECTION('DY',(0.0,1.0,0.0));
                #16=VECTOR('VY',#15,1.0);
                #17=LINE('L1',#2,#16);
                #18=DIRECTION('MX',(-1.0,0.0,0.0));
                #19=VECTOR('VMX',#18,1.0);
                #20=LINE('L2',#3,#19);
                #21=DIRECTION('MY',(0.0,-1.0,0.0));
                #22=VECTOR('VMY',#21,1.0);
                #23=LINE('L3',#4,#22);
                #24=CARTESIAN_POINT('UV0',(0.0,0.0));
                #25=CARTESIAN_POINT('UV1',(1.0,0.0));
                #26=CARTESIAN_POINT('UV2',(1.0,1.0));
                #27=CARTESIAN_POINT('UV3',(0.0,1.0));
                #28=DIRECTION('UX',(1.0,0.0));
                #29=DIRECTION('UY',(0.0,1.0));
                #30=DIRECTION('MUX',(-1.0,0.0));
                #31=DIRECTION('MUY',(0.0,-1.0));
                #32=VECTOR('UVX',#28,1.0);
                #33=VECTOR('UVY',#29,1.0);
                #34=VECTOR('UVMX',#30,1.0);
                #35=VECTOR('UVMY',#31,1.0);
                #36=LINE('UL0',#24,#32);
                #37=LINE('UL1',#25,#33);
                #38=LINE('UL2',#26,#34);
                #39=LINE('UL3',#27,#35);
                #40=REPRESENTATION_CONTEXT('PC','PARAMETRIC');
                #41=DEFINITIONAL_REPRESENTATION('DR0',(#36),#40);
                #42=DEFINITIONAL_REPRESENTATION('DR1',(#37),#40);
                #43=DEFINITIONAL_REPRESENTATION('DR2',(#38),#40);
                #44=DEFINITIONAL_REPRESENTATION('DR3',(#39),#40);
                #45=DEGENERATE_PCURVE('DPC0',#8,#41);
                #46=DEGENERATE_PCURVE('DPC1',#8,#42);
                #47=DEGENERATE_PCURVE('DPC2',#8,#43);
                #48=DEGENERATE_PCURVE('DPC3',#8,#44);
                #49=SURFACE_CURVE('SC0',#14,(#45),.PCURVE_S1.);
                #50=SURFACE_CURVE('SC1',#17,(#46),.PCURVE_S1.);
                #51=SURFACE_CURVE('SC2',#20,(#47),.PCURVE_S1.);
                #52=SURFACE_CURVE('SC3',#23,(#48),.PCURVE_S1.);
                #53=EDGE_CURVE('E0',#9,#10,#49,.T.);
                #54=EDGE_CURVE('E1',#10,#11,#50,.T.);
                #55=EDGE_CURVE('E2',#11,#12,#51,.T.);
                #56=EDGE_CURVE('E3',#12,#9,#52,.T.);
                #57=ORIENTED_EDGE('OE0',$,$,#53,.T.);
                #58=ORIENTED_EDGE('OE1',$,$,#54,.T.);
                #59=ORIENTED_EDGE('OE2',$,$,#55,.T.);
                #60=ORIENTED_EDGE('OE3',$,$,#56,.T.);
                #61=EDGE_LOOP('LOOP0',(#57,#58,#59,#60));
                #62=FACE_OUTER_BOUND('FOB',#61,.T.);
                #63=ADVANCED_FACE('F0',(#62),#8,.T.);
                #64=CLOSED_SHELL('CS0',(#63));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"faceCount\":1",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportOffsetPlanarFaceThroughProjectedFallback() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX0',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #6=OFFSET_SURFACE('OFS0',#5,1.0,.F.);
                #10=CARTESIAN_POINT('Q0',(0.0,0.0,1.0));
                #11=CARTESIAN_POINT('Q1',(1.0,0.0,1.0));
                #12=CARTESIAN_POINT('Q2',(1.0,1.0,1.0));
                #13=CARTESIAN_POINT('Q3',(0.0,1.0,1.0));
                #20=VERTEX_POINT('V0',#10);
                #21=VERTEX_POINT('V1',#11);
                #22=VERTEX_POINT('V2',#12);
                #23=VERTEX_POINT('V3',#13);
                #24=VECTOR('VX',#3,1.0);
                #25=LINE('L0',#10,#24);
                #26=DIRECTION('DY',(0.0,1.0,0.0));
                #27=VECTOR('VY',#26,1.0);
                #28=LINE('L1',#11,#27);
                #29=DIRECTION('MX',(-1.0,0.0,0.0));
                #30=VECTOR('VMX',#29,1.0);
                #31=LINE('L2',#12,#30);
                #32=DIRECTION('MY',(0.0,-1.0,0.0));
                #33=VECTOR('VMY',#32,1.0);
                #34=LINE('L3',#13,#33);
                #40=EDGE_CURVE('E0',#20,#21,#25,.T.);
                #41=EDGE_CURVE('E1',#21,#22,#28,.T.);
                #42=EDGE_CURVE('E2',#22,#23,#31,.T.);
                #43=EDGE_CURVE('E3',#23,#20,#34,.T.);
                #50=ORIENTED_EDGE('OE0',$,$,#40,.T.);
                #51=ORIENTED_EDGE('OE1',$,$,#41,.T.);
                #52=ORIENTED_EDGE('OE2',$,$,#42,.T.);
                #53=ORIENTED_EDGE('OE3',$,$,#43,.T.);
                #60=EDGE_LOOP('LOOP0',(#50,#51,#52,#53));
                #61=FACE_OUTER_BOUND('B0',#60,.T.);
                #62=ADVANCED_FACE('F0',(#61),#6,.T.);
                #63=OPEN_SHELL('OS0',(#62));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"surfaceType\":\"OFFSET_SURFACE\"",
                "\"faceCount\":1",
                "\"edgeCount\":4",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportReplicaPlanarFaceThroughProjectedFallback() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX0',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #6=CARTESIAN_POINT('O2',(0.0,0.0,2.0));
                #7=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#6,1.0,$);
                #8=SURFACE_REPLICA('SR0',#5,#7);
                #10=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #11=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #12=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #20=VECTOR('VX',#3,1.0);
                #21=LINE('L0',#1,#20);
                #22=DIRECTION('DY',(0.0,1.0,0.0));
                #23=VECTOR('VY',#22,1.0);
                #24=LINE('L1',#10,#23);
                #25=DIRECTION('MX',(-1.0,0.0,0.0));
                #26=VECTOR('VMX',#25,1.0);
                #27=LINE('L2',#11,#26);
                #28=DIRECTION('MY',(0.0,-1.0,0.0));
                #29=VECTOR('VMY',#28,1.0);
                #30=LINE('L3',#12,#29);
                #31=CURVE_REPLICA('CR0',#21,#7);
                #32=CURVE_REPLICA('CR1',#24,#7);
                #33=CURVE_REPLICA('CR2',#27,#7);
                #34=CURVE_REPLICA('CR3',#30,#7);
                #40=CARTESIAN_POINT('Q0',(0.0,0.0,2.0));
                #41=CARTESIAN_POINT('Q1',(1.0,0.0,2.0));
                #42=CARTESIAN_POINT('Q2',(1.0,1.0,2.0));
                #43=CARTESIAN_POINT('Q3',(0.0,1.0,2.0));
                #50=VERTEX_POINT('V0',#40);
                #51=VERTEX_POINT('V1',#41);
                #52=VERTEX_POINT('V2',#42);
                #53=VERTEX_POINT('V3',#43);
                #60=EDGE_CURVE('E0',#50,#51,#31,.T.);
                #61=EDGE_CURVE('E1',#51,#52,#32,.T.);
                #62=EDGE_CURVE('E2',#52,#53,#33,.T.);
                #63=EDGE_CURVE('E3',#53,#50,#34,.T.);
                #70=ORIENTED_EDGE('OE0',$,$,#60,.T.);
                #71=ORIENTED_EDGE('OE1',$,$,#61,.T.);
                #72=ORIENTED_EDGE('OE2',$,$,#62,.T.);
                #73=ORIENTED_EDGE('OE3',$,$,#63,.T.);
                #80=EDGE_LOOP('LOOP0',(#70,#71,#72,#73));
                #81=FACE_OUTER_BOUND('B0',#80,.T.);
                #82=ADVANCED_FACE('F0',(#81),#8,.T.);
                #83=OPEN_SHELL('OS0',(#82));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "SURFACE_REPLICA", 4, false);
    }

    @Test
    void shouldBindPmiTargetsFromRepresentationUsageAssociations() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #3=REPRESENTATION('REP_A',(#1),#2);
                #4=REPRESENTATION('REP_B',(#1),#2);
                #5=REPRESENTATION('REP_C',(#1),#2);
                #6=REPRESENTATION_RELATIONSHIP('RR','chain',#4,#5);
                #7=PROPERTY_DEFINITION('PD','',#1);
                #8=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #9=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#8);
                #10=GEOMETRIC_CURVE_SET('LEADER',(#8));
                #11=DRAUGHTING_CALLOUT('CALLOUT',(#9,#10));
                #12=ITEM_IDENTIFIED_REPRESENTATION_USAGE('USAGE','generic',#7,#3,#11);
                #13=PLACED_TARGET('PT','target',#7,#4,#11);
                #14=CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE('CBIIRU','chain',#7,(#4,#5),(#6),#11);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughPropertyAndShapeAspectChains() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA_BASE','base',#7,.T.);
                #9=SPOTFACE_HOLE_OCCURRENCE('SA_OCC','occurrence',#7,.T.,#8);
                #10=PROPERTY_DEFINITION('PD_ROOT','',#9);
                #11=PROPERTY_DEFINITION('PD_TARGET','',#8);
                #12=PROPERTY_DEFINITION_RELATIONSHIP('PDR','link',#10,#11);
                #13=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #14=REPRESENTATION('REP_USED',(),#13);
                #15=REPRESENTATION('REP_PROP',(),#13);
                #16=REPRESENTATION('REP_DATUM',(),#13);
                #17=PROPERTY_DEFINITION_REPRESENTATION(#11,#15);
                #18=PLACED_DATUM_TARGET_FEATURE(#11,#16);
                #19=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #20=ANNOTATION_TEXT_OCCURRENCE('NOTE','semantic',#19);
                #21=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC','semantic link',#12,#14,#20,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE\"",
                "\"id\":14,\"type\":\"representation\",\"name\":\"REP_USED\"",
                "\"id\":15,\"type\":\"representation\",\"name\":\"REP_PROP\"",
                "\"id\":16,\"type\":\"representation\",\"name\":\"REP_DATUM\"",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"PLACED_DATUM_TARGET_FEATURE\"",
                "\"viaDefinitionId\":18");
    }

    @Test
    void shouldBindPmiTargetsThroughPathAndWireSemanticDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=VECTOR('V0',#3,1.0);
                #5=LINE('L0',#1,#4);
                #6=EDGE_CURVE('E0',#2,#2,#5,.T.);
                #7=ORIENTED_EDGE('',*,*,#6,.T.);
                #8=PATH('OP',(#7));
                #9=CONNECTED_EDGE_SET('CES',(#7));
                #10=EDGE_LOOP('EL',(#7));
                #11=WIRE_SHELL('WS',(#10));
                #12=VERTEX_LOOP('VL',#2);
                #13=VERTEX_SHELL('VS',#12);
                #14=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #15=REPRESENTATION('REP_A',(),#14);
                #16=ANNOTATION_TEXT_OCCURRENCE('NOTE','targets',#1);
                #17=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#8,#15,#16,#8);
                #18=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#9,#15,#16,#9);
                #19=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#10,#15,#16,#10);
                #20=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#11,#15,#16,#11);
                #21=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#13,#15,#16,#13);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE\"",
                "\"id\":15,\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"id\":8,\"type\":\"path\",\"name\":\"OP\"",
                "\"id\":9,\"type\":\"edge_set\",\"name\":\"CES\"",
                "\"id\":10,\"type\":\"loop\",\"name\":\"EL\"",
                "\"id\":11,\"type\":\"wire_shell\",\"name\":\"WS\"",
                "\"id\":13,\"type\":\"vertex_shell\",\"name\":\"VS\"");
    }

    @Test
    void shouldBindPmiTargetsThroughShellAndModelSemanticDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,1.0,0.0));
                #4=DIRECTION('N',(0.0,0.0,1.0));
                #5=DIRECTION('X',(1.0,0.0,0.0));
                #6=AXIS2_PLACEMENT_3D('AX',#1,#4,#5);
                #7=PLANE('PL',#6);
                #8=POLY_LOOP('PL0',(#1,#2,#3));
                #9=FACE_BOUND('FB',#8,.T.);
                #10=FACE_SURFACE('AF',(#9),#7,.T.);
                #11=CONNECTED_FACE_SET('CFS',(#10));
                #12=CONNECTED_FACE_SUB_SET('CFSS',(#10),#11);
                #13=OPEN_SHELL('OS',(#10));
                #14=SURFACED_OPEN_SHELL('SOS',(#10));
                #15=ORIENTED_OPEN_SHELL('OOS',#13,.T.);
                #16=CLOSED_SHELL('CS',(#10));
                #17=ORIENTED_CLOSED_SHELL('OCS',#16,.T.);
                #18=FACE_BASED_SURFACE_MODEL('FBSM',(#11));
                #19=SHELL_BASED_SURFACE_MODEL('SBSM',(#13,#16));
                #20=VERTEX_POINT('VP0',#1);
                #21=VERTEX_POINT('VP1',#2);
                #22=DIRECTION('DX',(1.0,0.0,0.0));
                #23=VECTOR('V0',#22,1.0);
                #24=LINE('L0',#1,#23);
                #25=EDGE_CURVE('E0',#20,#21,#24,.T.);
                #26=ORIENTED_EDGE('OE0',$,$,#25,.T.);
                #27=CONNECTED_EDGE_SET('CES',(#26));
                #28=EDGE_BASED_WIREFRAME_MODEL('EBWM',(#27));
                #29=EDGE_LOOP('EL',(#26));
                #30=WIRE_SHELL('WS',(#29));
                #31=VERTEX_LOOP('VL',#20);
                #32=VERTEX_SHELL('VS',#31);
                #33=SHELL_BASED_WIREFRAME_MODEL('SBWM',(#30,#32));
                #34=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #35=REPRESENTATION('REP_A',(),#34);
                #36=ANNOTATION_TEXT_OCCURRENCE('NOTE','containers',#1);
                #37=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#11,#35,#36,#11);
                #38=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#12,#35,#36,#12);
                #39=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#13,#35,#36,#13);
                #40=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#14,#35,#36,#14);
                #41=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#15,#35,#36,#15);
                #42=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#16,#35,#36,#16);
                #43=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#17,#35,#36,#17);
                #44=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#18,#35,#36,#18);
                #45=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#19,#35,#36,#19);
                #46=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#28,#35,#36,#28);
                #47=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#33,#35,#36,#33);
                ENDSEC;
                """);

        assertJsonContains(json,
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
                "\"id\":33,\"type\":\"wireframe_model\",\"name\":\"SBWM\"");
    }

    @Test
    void shouldBindPmiTargetsThroughSolidSemanticDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=BLOCK('BLK',#4,10.0,20.0,30.0);
                #6=CARTESIAN_POINT('PZ',(0.0,0.0,15.0));
                #7=AXIS2_PLACEMENT_3D('PLAX',#6,#2,#3);
                #8=PLANE('PLANE',#7);
                #9=HALF_SPACE_SOLID('HS',#8,.T.);
                #10=(BOOLEAN_RESULT(.DIFFERENCE.,#5,#9) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BOOL0'));
                #11=CSG_SOLID('CSG0',#10);
                #12=(BOOLEAN_CLIPPING_RESULT(.DIFFERENCE.,#5,#9) BOOLEAN_RESULT(.DIFFERENCE.,#5,#9) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BCR0'));
                #20=CARTESIAN_POINT('P2D',(0.0,0.0));
                #21=DIRECTION('DX2',(1.0,0.0));
                #22=AXIS2_PLACEMENT_2D('PROFILE_AX',#20,#21);
                #23=RECTANGLE_PROFILE_DEF(.AREA.,'RPD',#22,4.0,2.0);
                #24=DIRECTION('DIR',(0.0,0.0,1.0));
                #25=EXTRUDED_AREA_SOLID('EX0',#23,#4,#24,5.0);
                #26=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TX',#2,#3,#1,1.0,$);
                #27=SOLID_REPLICA('SR0',#25,#26);
                #30=CARTESIAN_POINT('Q0',(0.0,0.0,0.0));
                #31=CARTESIAN_POINT('Q1',(1.0,0.0,0.0));
                #32=CARTESIAN_POINT('Q2',(1.0,1.0,0.0));
                #33=CARTESIAN_POINT('Q3',(0.0,1.0,0.0));
                #34=DIRECTION('NZ',(0.0,0.0,1.0));
                #35=DIRECTION('NX',(1.0,0.0,0.0));
                #36=AXIS2_PLACEMENT_3D('SAX',#30,#34,#35);
                #37=PLANE('SPL',#36);
                #38=VERTEX_POINT('V0',#30);
                #39=VERTEX_POINT('V1',#31);
                #40=VERTEX_POINT('V2',#32);
                #41=VERTEX_POINT('V3',#33);
                #42=DIRECTION('D1',(1.0,0.0,0.0));
                #43=VECTOR('VE1',#42,1.0);
                #44=LINE('L1',#30,#43);
                #45=DIRECTION('D2',(0.0,1.0,0.0));
                #46=VECTOR('VE2',#45,1.0);
                #47=LINE('L2',#31,#46);
                #48=DIRECTION('D3',(-1.0,0.0,0.0));
                #49=VECTOR('VE3',#48,1.0);
                #50=LINE('L3',#32,#49);
                #51=DIRECTION('D4',(0.0,-1.0,0.0));
                #52=VECTOR('VE4',#51,1.0);
                #53=LINE('L4',#33,#52);
                #54=EDGE_CURVE('E1',#38,#39,#44,.T.);
                #55=EDGE_CURVE('E2',#39,#40,#47,.T.);
                #56=EDGE_CURVE('E3',#40,#41,#50,.T.);
                #57=EDGE_CURVE('E4',#41,#38,#53,.T.);
                #58=ORIENTED_EDGE('OE1',$,$,#54,.T.);
                #59=ORIENTED_EDGE('OE2',$,$,#55,.T.);
                #60=ORIENTED_EDGE('OE3',$,$,#56,.T.);
                #61=ORIENTED_EDGE('OE4',$,$,#57,.T.);
                #62=EDGE_LOOP('LOOP',(#58,#59,#60,#61));
                #63=FACE_OUTER_BOUND('FOB',#62,.T.);
                #64=ADVANCED_FACE('AF0',(#63),#37,.T.);
                #65=CLOSED_SHELL('CS0',(#64));
                #66=MANIFOLD_SOLID_BREP('MSB0',#65);
                #67=BREP_WITH_VOIDS('BWV0',#65,());
                #70=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #71=REPRESENTATION('REP_A',(),#70);
                #72=ANNOTATION_TEXT_OCCURRENCE('NOTE','solids',#1);
                #73=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#66,#71,#72,#66);
                #74=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#67,#71,#72,#67);
                #75=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#25,#71,#72,#25);
                #76=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#27,#71,#72,#27);
                #77=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#5,#71,#72,#5);
                #78=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#11,#71,#72,#11);
                #79=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#10,#71,#72,#10);
                #80=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#12,#71,#72,#12);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE\"",
                "\"id\":71,\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"id\":66,\"type\":\"solid\",\"name\":\"MSB0\"",
                "\"id\":67,\"type\":\"solid\",\"name\":\"BWV0\"",
                "\"id\":25,\"type\":\"solid\",\"name\":\"EX0\"",
                "\"id\":27,\"type\":\"solid\",\"name\":\"SR0\"",
                "\"id\":5,\"type\":\"solid\",\"name\":\"BLK\"",
                "\"id\":11,\"type\":\"solid\",\"name\":\"CSG0\"",
                "\"id\":10,\"type\":\"solid\",\"name\":\"BOOL0\"",
                "\"id\":12,\"type\":\"solid\",\"name\":\"BCR0\"",
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
    void shouldBindPmiTargetsThroughProductAndAssemblyShapeChains() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('ASM','Assembly','Assembly',(#2));
                #4=PRODUCT('COMP','Component','Component',(#2));
                #5=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #6=PRODUCT_DEFINITION_FORMATION('v1','',#4);
                #7=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #8=PRODUCT_DEFINITION('asm_pd','assembly',#5,#7);
                #9=PRODUCT_DEFINITION('comp_pd','component',#6,#7);
                #10=PRODUCT_DEFINITION_SHAPE('asm_shape','',#8);
                #11=PRODUCT_DEFINITION_SHAPE('comp_shape','',#9);
                #12=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ-1','OCC','component usage',#8,#9,'R1');
                #13=PRODUCT_DEFINITION_SHAPE('occ_shape','',#12);
                #20=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #21=REPRESENTATION('REP_COMP',(),#20);
                #22=REPRESENTATION('REP_OCC',(),#20);
                #23=REPRESENTATION('REP_USAGE_OCC',(),#20);
                #24=REPRESENTATION('REP_USAGE_PD',(),#20);
                #25=REPRESENTATION('REP_USAGE_PDS',(),#20);
                #26=SHAPE_DEFINITION_REPRESENTATION(#11,#21);
                #27=REPRESENTATION_RELATIONSHIP('CTX','occ ctx',#21,#22);
                #28=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#27,#13);
                #29=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #30=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #31=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #38=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #32=ANNOTATION_TEXT_OCCURRENCE('NOTE_OCC','occ',#29);
                #33=ANNOTATION_TEXT_OCCURRENCE('NOTE_PD','pd',#30);
                #34=ANNOTATION_TEXT_OCCURRENCE('NOTE_PDS','pds',#31);
                #39=ANNOTATION_TEXT_OCCURRENCE('NOTE_SDR','sdr',#38);
                #35=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_OCC','',#12,#23,#32,#13);
                #36=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_PD','',#9,#24,#33,#11);
                #37=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_PDS','',#11,#25,#34,#11);
                #40=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_SDR','',#26,#25,#39,#11);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_OCC\"",
                "\"id\":23,\"type\":\"representation\",\"name\":\"REP_USAGE_OCC\"",
                "\"id\":21,\"type\":\"representation\",\"name\":\"REP_COMP\"",
                "\"id\":22,\"type\":\"representation\",\"name\":\"REP_OCC\"",
                "\"name\":\"NOTE_PD\"",
                "\"id\":24,\"type\":\"representation\",\"name\":\"REP_USAGE_PD\"",
                "\"name\":\"NOTE_PDS\"",
                "\"id\":25,\"type\":\"representation\",\"name\":\"REP_USAGE_PDS\"",
                "\"name\":\"NOTE_SDR\"",
                "\"viaDefinitionType\":\"NEXT_ASSEMBLY_USAGE_OCCURRENCE\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION\"",
                "\"viaDefinitionId\":9",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_SHAPE\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"SHAPE_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"CONTEXT_DEPENDENT_SHAPE_REPRESENTATION\"",
                "\"viaDefinitionId\":28");
    }

    @Test
    void shouldBindPmiTargetsThroughTransformedAssemblyShapeChains() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('ASM','Assembly','Assembly',(#2));
                #4=PRODUCT('COMP','Component','Component',(#2));
                #5=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #6=PRODUCT_DEFINITION_FORMATION('v1','',#4);
                #7=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #8=PRODUCT_DEFINITION('asm_pd','assembly',#5,#7);
                #9=PRODUCT_DEFINITION('comp_pd','component',#6,#7);
                #10=PRODUCT_DEFINITION_SHAPE('asm_shape','',#8);
                #11=PRODUCT_DEFINITION_SHAPE('comp_shape','',#9);
                #12=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ-1','OCC','component usage',#8,#9,'R1');
                #13=PRODUCT_DEFINITION_SHAPE('occ_shape','',#12);
                #20=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #21=REPRESENTATION('REP_COMP',(),#20);
                #22=REPRESENTATION('REP_OCC',(),#20);
                #23=REPRESENTATION('REP_USAGE_OCC',(),#20);
                #24=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #25=CARTESIAN_POINT('TX1',(5.0,0.0,0.0));
                #26=DIRECTION('DZ',(0.0,0.0,1.0));
                #27=DIRECTION('DX',(1.0,0.0,0.0));
                #28=AXIS2_PLACEMENT_3D('AX0',#24,#26,#27);
                #29=AXIS2_PLACEMENT_3D('AX1',#25,#26,#27);
                #30=ITEM_DEFINED_TRANSFORMATION('T1','',#28,#29);
                #31=(REPRESENTATION_RELATIONSHIP('CTX','occ ctx',#21,#22)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#30));
                #32=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#31,#13);
                #33=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #34=ANNOTATION_TEXT_OCCURRENCE('NOTE_OCC','occ',#33);
                #35=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_OCC','',#12,#23,#34,#13);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_OCC\"",
                "\"id\":23,\"type\":\"representation\",\"name\":\"REP_USAGE_OCC\"",
                "\"id\":21,\"type\":\"representation\",\"name\":\"REP_COMP\"",
                "\"id\":22,\"type\":\"representation\",\"name\":\"REP_OCC\"",
                "\"viaDefinitionType\":\"NEXT_ASSEMBLY_USAGE_OCCURRENCE\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_SHAPE\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"ITEM_DEFINED_TRANSFORMATION\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"CONTEXT_DEPENDENT_SHAPE_REPRESENTATION\"",
                "\"viaDefinitionId\":32");
    }

    @Test
    void shouldBindPmiTargetsThroughProductShapeRepresentationRelationshipChains() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_USAGE',(),#9);
                #11=REPRESENTATION('REP_REL_A',(),#9);
                #12=REPRESENTATION('REP_REL_B',(),#9);
                #13=REPRESENTATION('REP_REL_C',(),#9);
                #14=REPRESENTATION('REP_REL_D',(),#9);
                #15=REPRESENTATION('REP_REL_E',(),#9);
                #16=REPRESENTATION('REP_REL_F',(),#9);
                #17=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #18=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);
                #19=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #20=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #21=DIRECTION('DZ',(0.0,0.0,1.0));
                #22=DIRECTION('DX',(1.0,0.0,0.0));
                #23=AXIS2_PLACEMENT_3D('AX0',#19,#21,#22);
                #24=AXIS2_PLACEMENT_3D('AX1',#20,#21,#22);
                #25=ITEM_DEFINED_TRANSFORMATION('T1','',#23,#24);
                #26=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#12)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#25));
                #27=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#13);
                #30=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #31=ANNOTATION_TEXT_OCCURRENCE('NOTE_PDS','',#30);
                #32=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#7,#10,#31,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_PDS\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_USAGE\"",
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
    void shouldBindPmiTargetsThroughAdditionalPropertyRepresentationLinks() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=PROPERTY_DEFINITION('PD0','',#8);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #11=REPRESENTATION('REP_ACTION',(),#10);
                #12=REPRESENTATION('REP_CONTACT',(),#10);
                #13=REPRESENTATION('REP_KDEF',(),#10);
                #14=REPRESENTATION('REP_KMECH',(),#10);
                #15=REPRESENTATION('REP_KREL',(),#10);
                #16=REPRESENTATION('REP_KTOPO',(),#10);
                #17=REPRESENTATION('REP_RESOURCE',(),#10);
                #18=ACTION_PROPERTY_REPRESENTATION(#9,#11);
                #19=CONTACT_RATIO_REPRESENTATION(#9,#12);
                #20=KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION(#9,#13);
                #21=KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION(#9,#14);
                #22=KINEMATIC_PROPERTY_REPRESENTATION_RELATION(#9,#15);
                #23=KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION(#9,#16);
                #24=RESOURCE_PROPERTY_REPRESENTATION(#9,#17);
                #28=REPRESENTATION_RELATIONSHIP('RR','',#11,#12);
                #29=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #30=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #31=DIRECTION('DZ',(0.0,0.0,1.0));
                #32=DIRECTION('DX',(1.0,0.0,0.0));
                #33=AXIS2_PLACEMENT_3D('AX0',#29,#31,#32);
                #34=AXIS2_PLACEMENT_3D('AX1',#30,#31,#32);
                #35=ITEM_DEFINED_TRANSFORMATION('T1','',#33,#34);
                #36=(REPRESENTATION_RELATIONSHIP('RRT','',#11,#13)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#35));
                #37=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#11,#14);
                #25=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #26=ANNOTATION_TEXT_OCCURRENCE('NOTE','links',#25);
                #27=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC','',#9,#11,#26,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughDirectAdditionalPropertyRepresentationLinkDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=PROPERTY_DEFINITION('PD0','',#8);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #11=REPRESENTATION('REP_ACTION',(),#10);
                #12=REPRESENTATION('REP_CONTACT',(),#10);
                #13=REPRESENTATION('REP_KDEF',(),#10);
                #14=REPRESENTATION('REP_KMECH',(),#10);
                #15=REPRESENTATION('REP_KREL',(),#10);
                #16=REPRESENTATION('REP_KTOPO',(),#10);
                #17=REPRESENTATION('REP_RESOURCE',(),#10);
                #18=ACTION_PROPERTY_REPRESENTATION(#9,#11);
                #19=CONTACT_RATIO_REPRESENTATION(#9,#12);
                #20=KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION(#9,#13);
                #21=KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION(#9,#14);
                #22=KINEMATIC_PROPERTY_REPRESENTATION_RELATION(#9,#15);
                #23=KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION(#9,#16);
                #24=RESOURCE_PROPERTY_REPRESENTATION(#9,#17);
                #25=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #26=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #27=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #28=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #29=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #30=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #31=CARTESIAN_POINT('P6',(6.0,0.0,0.0));
                #32=ANNOTATION_TEXT_OCCURRENCE('NOTE_ACTION','',#25);
                #33=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONTACT','',#26);
                #34=ANNOTATION_TEXT_OCCURRENCE('NOTE_KDEF','',#27);
                #35=ANNOTATION_TEXT_OCCURRENCE('NOTE_KMECH','',#28);
                #36=ANNOTATION_TEXT_OCCURRENCE('NOTE_KREL','',#29);
                #37=ANNOTATION_TEXT_OCCURRENCE('NOTE_KTOPO','',#30);
                #38=ANNOTATION_TEXT_OCCURRENCE('NOTE_RESOURCE','',#31);
                #39=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#18,#11,#32,#7);
                #40=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#19,#12,#33,#7);
                #41=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#20,#13,#34,#7);
                #42=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#21,#14,#35,#7);
                #43=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#22,#15,#36,#7);
                #44=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#23,#16,#37,#7);
                #45=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#24,#17,#38,#7);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughRuleAndDatumPropertyRepresentationRelationshipChains() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA_TARGET','target',#7,.T.);
                #9=PROPERTY_DEFINITION('PD_TARGET','',#8);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #11=REPRESENTATION('REP_RULE',(),#10);
                #28=REPRESENTATION('REP_AUX',(),#10);
                #12=FORWARD_CHAINING_RULE_PREMISE(#9,#11);
                #13=BACK_CHAINING_RULE_BODY(#9,#11);
                #14=PLACED_DATUM_TARGET_FEATURE(#9,#11);
                #15=REPRESENTATION_RELATIONSHIP('RR','',#11,#28);
                #16=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #17=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #18=DIRECTION('DZ',(0.0,0.0,1.0));
                #19=DIRECTION('DX',(1.0,0.0,0.0));
                #20=AXIS2_PLACEMENT_3D('AX0',#16,#18,#19);
                #21=AXIS2_PLACEMENT_3D('AX1',#17,#18,#19);
                #22=ITEM_DEFINED_TRANSFORMATION('T1','',#20,#21);
                #23=(REPRESENTATION_RELATIONSHIP('RRT','',#11,#28)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#22));
                #24=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#11,#28);
                #25=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #26=ANNOTATION_TEXT_OCCURRENCE('NOTE_RULE_DATUM','',#25);
                #27=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC','',#9,#11,#26,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughDirectPropertyAndAttributeRepresentationRelationshipChains() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=PROPERTY_DEFINITION('PD0','',#8);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #11=REPRESENTATION('REP_PROP',(),#10);
                #12=REPRESENTATION('REP_ASSERT',(),#10);
                #13=PROPERTY_DEFINITION_REPRESENTATION(#9,#11);
                #14=ATTRIBUTE_ASSERTION(#9,#12);
                #15=REPRESENTATION_RELATIONSHIP('RR','',#11,#12);
                #16=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #17=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #18=DIRECTION('DZ',(0.0,0.0,1.0));
                #19=DIRECTION('DX',(1.0,0.0,0.0));
                #20=AXIS2_PLACEMENT_3D('AX0',#16,#18,#19);
                #21=AXIS2_PLACEMENT_3D('AX1',#17,#18,#19);
                #22=ITEM_DEFINED_TRANSFORMATION('T1','',#20,#21);
                #23=(REPRESENTATION_RELATIONSHIP('RRT','',#11,#12)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#22));
                #24=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#11,#12);
                #25=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #26=ANNOTATION_TEXT_OCCURRENCE('NOTE','direct property links',#25);
                #27=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC','',#9,#11,#26,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughDirectPropertyAndAttributeLinkDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=PROPERTY_DEFINITION('PD0','',#8);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #11=REPRESENTATION('REP_PROP',(),#10);
                #12=REPRESENTATION('REP_ASSERT',(),#10);
                #13=PROPERTY_DEFINITION_REPRESENTATION(#9,#11);
                #14=ATTRIBUTE_ASSERTION(#9,#12);
                #15=REPRESENTATION_RELATIONSHIP('RR','',#11,#12);
                #16=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #17=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #18=DIRECTION('DZ',(0.0,0.0,1.0));
                #19=DIRECTION('DX',(1.0,0.0,0.0));
                #20=AXIS2_PLACEMENT_3D('AX0',#16,#18,#19);
                #21=AXIS2_PLACEMENT_3D('AX1',#17,#18,#19);
                #22=ITEM_DEFINED_TRANSFORMATION('T1','',#20,#21);
                #23=(REPRESENTATION_RELATIONSHIP('RRT','',#11,#12)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#22));
                #24=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#11,#12);
                #25=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #26=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #27=ANNOTATION_TEXT_OCCURRENCE('NOTE_PDR','direct pdr',#25);
                #28=ANNOTATION_TEXT_OCCURRENCE('NOTE_ASSERT','direct assert',#26);
                #29=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_PDR','',#13,#11,#27,#8);
                #30=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_ASSERT','',#14,#12,#28,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldPropagatePmiTargetsAcrossDraughtingCalloutRelationship() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #3=REPRESENTATION('REP_A',(#1),#2);
                #4=PROPERTY_DEFINITION('PD','',#1);
                #5=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #6=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #7=ANNOTATION_TEXT_OCCURRENCE('NOTE_A','base',#5);
                #8=ANNOTATION_TEXT_OCCURRENCE('NOTE_B','child',#6);
                #9=DRAUGHTING_CALLOUT('CALLOUT_A',(#7));
                #10=DRAUGHTING_CALLOUT('CALLOUT_B',(#8));
                #11=PLACED_TARGET('PT','target',#4,#3,#9);
                #12=DRAUGHTING_CALLOUT_RELATIONSHIP('REL','carry',#9,#10);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"CALLOUT_A\"",
                "\"name\":\"CALLOUT_B\"",
                "\"id\":3,\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"viaRelationshipType\":\"DRAUGHTING_CALLOUT_RELATIONSHIP\"",
                "\"viaRelationshipId\":12");
    }

    @Test
    void shouldPropagatePmiTargetsAcrossAnnotationOccurrenceRelationshipWithTerminatorSymbol() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #3=REPRESENTATION('REP_A',(#1),#2);
                #4=PROPERTY_DEFINITION('PD','',#1);
                #5=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYM'));
                #6=REPRESENTATION('SYM',(),#5);
                #7=CARTESIAN_POINT('O',(0.0,0.0));
                #8=DIRECTION('X',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('MAP',#7,#8);
                #10=SYMBOL_REPRESENTATION_MAP(#9,#6);
                #11=CARTESIAN_POINT('P',(3.0,4.0));
                #12=AXIS2_PLACEMENT_2D('TGT',#11,#8);
                #13=ANNOTATION_SYMBOL('AS0',#10,#12);
                #14=PRESENTATION_STYLE_ASSIGNMENT(());
                #15=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #16=DIRECTION('DIR0',(1.0,0.0,0.0));
                #17=VECTOR('V0',#16,1.0);
                #18=LINE('L0',#15,#17);
                #19=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#14),#18);
                #20=TERMINATOR_SYMBOL('TS0',(#14),#13,#19);
                #21=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#14),#13);
                #22=ANNOTATION_OCCURRENCE_RELATIONSHIP('REL','links symbol to terminator',#21,#20);
                #23=ANNOTATION_TEXT_OCCURRENCE('NOTE','occurrence-links',#15);
                #24=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC','',#4,#3,#23,#22);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE\"",
                "\"id\":3,\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"viaRelationshipType\":\"ANNOTATION_OCCURRENCE_RELATIONSHIP\"",
                "\"viaRelationshipId\":22");
    }

    @Test
    void shouldPropagatePmiTargetsAcrossAnnotationOccurrenceAssociativityWithTerminatorSymbol() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #3=REPRESENTATION('REP_A',(#1),#2);
                #4=PROPERTY_DEFINITION('PD','',#1);
                #5=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYM'));
                #6=REPRESENTATION('SYM',(),#5);
                #7=CARTESIAN_POINT('O',(0.0,0.0));
                #8=DIRECTION('X',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('MAP',#7,#8);
                #10=SYMBOL_REPRESENTATION_MAP(#9,#6);
                #11=CARTESIAN_POINT('P',(3.0,4.0));
                #12=AXIS2_PLACEMENT_2D('TGT',#11,#8);
                #13=ANNOTATION_SYMBOL('AS0',#10,#12);
                #14=PRESENTATION_STYLE_ASSIGNMENT(());
                #15=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#14),#13);
                #16=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #17=DIRECTION('DIR0',(1.0,0.0,0.0));
                #18=VECTOR('V0',#17,1.0);
                #19=LINE('L0',#16,#18);
                #20=PROJECTION_CURVE('PC0',(#14),#19);
                #21=TERMINATOR_SYMBOL('TS0',(#14),#13,#20);
                #22=ANNOTATION_OCCURRENCE_ASSOCIATIVITY('AOA','assoc',#15,#21);
                #23=DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY('DCTPCA','assoc',#21,#20);
                #24=ANNOTATION_TEXT_OCCURRENCE('NOTE_A','assoc',#16);
                #25=ANNOTATION_TEXT_OCCURRENCE('NOTE_B','dim',#16);
                #26=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_A','',#4,#3,#24,#22);
                #27=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC_B','',#4,#3,#25,#23);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_A\"",
                "\"name\":\"NOTE_B\"",
                "\"id\":3,\"type\":\"representation\",\"name\":\"REP_A\"",
                "\"viaRelationshipType\":\"ANNOTATION_OCCURRENCE_ASSOCIATIVITY\"",
                "\"viaRelationshipId\":22",
                "\"viaRelationshipType\":\"DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY\"",
                "\"viaRelationshipId\":23");
    }

    @Test
    void shouldExportAnnotationOccurrenceRelationshipWithWrappedTerminatorItemPmi() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=POINT_SET('PS0',(#1));
                #3=PRESENTATION_STYLE_ASSIGNMENT(());
                #4=(ANNOTATION_PLACEHOLDER_OCCURRENCE('PH0',(#3),#2,.ANNOTATION_TEXT.,1.0)
                    STYLED_ITEM('PH0',(#3),#2)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PH0'));
                #5=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#3),#4);
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=VECTOR('VX',#6,1.0);
                #8=LINE('L0',#1,#7);
                #9=(PROJECTION_CURVE('PC0',(#3),#8)
                    ANNOTATION_CURVE_OCCURRENCE('PC0',(#3),#8)
                    STYLED_ITEM('PC0',(#3),#8)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PC0'));
                #10=TERMINATOR_SYMBOL('TS0',(#3),#5,#9);
                #11=ANNOTATION_OCCURRENCE_RELATIONSHIP('REL','wrapped terminator',#5,#10);
                #20=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #21=SHAPE_REPRESENTATION('ANN',(#11),#20);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"REL\"",
                "\"position\":[1.0,2.0,3.0]");
    }

    @Test
    void shouldBindPmiTargetsThroughMetadataWrapperDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=PROPERTY_DEFINITION('PD_TARGET','',#8);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #11=REPRESENTATION('REP_TARGET',(),#10);
                #12=PROPERTY_DEFINITION_REPRESENTATION(#9,#11);
                #13=NAME_ATTRIBUTE('N0',#8);
                #14=DESCRIPTION_ATTRIBUTE('D0',#8);
                #15=ID_ATTRIBUTE('I0',#8);
                #16=IDENTIFICATION_ROLE('role');
                #17=EXTERNAL_SOURCE('src');
                #18=NAME_ASSIGNMENT('NAME_META');
                #19=IDENTIFICATION_ASSIGNMENT('ID_META',#16);
                #20=EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT_META',#16,#17);
                #21=APPLIED_NAME_ASSIGNMENT('APPLIED_NAME',(#8));
                #22=APPLIED_IDENTIFICATION_ASSIGNMENT('APPLIED_ID',#16,(#8));
                #23=APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT('APPLIED_EXT',#16,#17,(#8));
                #90=EXTERNAL_SOURCE('src-linked');
                #91=EXTERNAL_SOURCE_RELATIONSHIP('SR','',#17,#90);
                #92=EXTERNALLY_DEFINED_ITEM('EXT-APPLIED',#90);
                #24=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #25=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #26=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #27=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #28=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #29=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #30=ANNOTATION_TEXT_OCCURRENCE('NOTE_NAME_ATTR','',#24);
                #31=ANNOTATION_TEXT_OCCURRENCE('NOTE_DESC_ATTR','',#25);
                #32=ANNOTATION_TEXT_OCCURRENCE('NOTE_ID_ATTR','',#26);
                #33=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPLIED_NAME','',#27);
                #34=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPLIED_ID','',#28);
                #35=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPLIED_EXT','',#29);
                #36=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#13,#11,#30,#8);
                #37=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#14,#11,#31,#8);
                #38=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#15,#11,#32,#8);
                #39=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#21,#11,#33,#8);
                #40=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#22,#11,#34,#8);
                #41=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#23,#11,#35,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_NAME_ATTR\"",
                "\"name\":\"NOTE_DESC_ATTR\"",
                "\"name\":\"NOTE_ID_ATTR\"",
                "\"name\":\"NOTE_APPLIED_NAME\"",
                "\"name\":\"NOTE_APPLIED_ID\"",
                "\"name\":\"NOTE_APPLIED_EXT\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_TARGET\"",
                "\"viaDefinitionType\":\"NAME_ATTRIBUTE\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"DESCRIPTION_ATTRIBUTE\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"ID_ATTRIBUTE\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"APPLIED_NAME_ASSIGNMENT\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"APPLIED_IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE_RELATIONSHIP\"",
                "\"viaDefinitionId\":91",
                "\"viaDefinitionType\":\"EXTERNALLY_DEFINED_ITEM\"",
                "\"viaDefinitionId\":92");
    }

    @Test
    void shouldBindPmiTargetsThroughPropertyDefinitionRepresentationLink() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=PROPERTY_DEFINITION('PD0','',#8);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #11=REPRESENTATION('REP_PROP',(),#10);
                #12=PROPERTY_DEFINITION_REPRESENTATION(#9,#11);
                #13=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #14=ANNOTATION_TEXT_OCCURRENCE('NOTE','prop',#13);
                #15=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC','',#9,#11,#14,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_PROP\"",
                "\"viaDefinitionType\":\"PROPERTY_DEFINITION_REPRESENTATION\"",
                "\"viaDefinitionId\":12");
    }

    @Test
    void shouldBindPmiTargetsThroughGeneralPropertyRelationships() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=GENERAL_PROPERTY('GP1','gp1','');
                #9=GENERAL_PROPERTY('GP2','gp2','');
                #10=GENERAL_PROPERTY_RELATIONSHIP('LINK','',#8,#9);
                #11=PROPERTY_DEFINITION('PD_GP2','',#9);
                #12=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #13=REPRESENTATION('REP_GP',(),#12);
                #14=ACTION_PROPERTY_REPRESENTATION(#11,#13);
                #15=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #16=ANNOTATION_TEXT_OCCURRENCE('NOTE_GP','',#15);
                #17=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC','',#8,#13,#16,#7);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_GP\"",
                "\"id\":13,\"type\":\"representation\",\"name\":\"REP_GP\"",
                "\"viaDefinitionType\":\"GENERAL_PROPERTY_RELATIONSHIP\"",
                "\"viaDefinitionId\":10");
    }

    @Test
    void shouldBindPmiTargetsThroughShapeAspectRelationships() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA_BASE','base',#7,.T.);
                #9=SHAPE_ASPECT('SA_TARGET','target',#7,.T.);
                #10=SHAPE_ASPECT_RELATIONSHIP('SAR','',#8,#9);
                #11=PROPERTY_DEFINITION('PD_TARGET','',#9);
                #12=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #13=REPRESENTATION('REP_SA',(),#12);
                #14=PROPERTY_DEFINITION_REPRESENTATION(#11,#13);
                #15=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #16=ANNOTATION_TEXT_OCCURRENCE('NOTE_SA','',#15);
                #17=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC','',#8,#13,#16,#7);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_SA\"",
                "\"id\":13,\"type\":\"representation\",\"name\":\"REP_SA\"",
                "\"viaDefinitionType\":\"SHAPE_ASPECT_RELATIONSHIP\"",
                "\"viaDefinitionId\":10");
    }

    @Test
    void shouldBindPmiTargetsThroughRequirementSemanticDefinitionsAndPreserveOccurrenceSubtypeNames() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA_BASE','base',#7,.T.);
                #9=SPOTFACE_HOLE_OCCURRENCE('SA_OCC','occurrence',#7,.T.,#8);
                #10=PROPERTY_DEFINITION('PD_OCC','',#9);
                #11=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #12=REPRESENTATION('REP_USED',(),#11);
                #13=REPRESENTATION('REP_OCC',(),#11);
                #14=PROPERTY_DEFINITION_REPRESENTATION(#10,#13);
                #15=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #16=ANNOTATION_TEXT_OCCURRENCE('NOTE_REQ','',#15);
                #17=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC','',#8,#12,#16,#9);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_REQ\"",
                "\"id\":12,\"type\":\"representation\",\"name\":\"REP_USED\"",
                "\"id\":13,\"type\":\"representation\",\"name\":\"REP_OCC\"",
                "\"viaDefinitionType\":\"SPOTFACE_HOLE_OCCURRENCE\"",
                "\"viaDefinitionId\":9");
    }

    @Test
    void shouldBindPmiTargetsThroughAdditionalAppliedMetadataAssignments() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=PROPERTY_DEFINITION('PD_TARGET','',#8);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #11=REPRESENTATION('REP_TARGET',(),#10);
                #12=PROPERTY_DEFINITION_REPRESENTATION(#9,#11);
                #13=CALENDAR_DATE(2026,11,4);
                #14=DATE_ROLE('release');
                #15=COORDINATED_UNIVERSAL_TIME_OFFSET(8,0,.AHEAD.);
                #16=LOCAL_TIME(9,15,$,#15);
                #17=DATE_AND_TIME(#13,#16);
                #18=DATE_TIME_ROLE('created');
                #19=APPROVAL_STATUS('approved');
                #20=APPROVAL(#19,'design');
                #21=SECURITY_CLASSIFICATION_LEVEL('unclassified');
                #22=SECURITY_CLASSIFICATION('sec','export control',#21);
                #23=DOCUMENT_TYPE('specification');
                #24=DOCUMENT('DOC-1','Spec','primary spec',#23);
                #25=CONTRACT_TYPE('purchase');
                #26=CONTRACT('C-1','supply',#25);
                #27=CERTIFICATION_TYPE('material');
                #28=CERTIFICATION('CERT-1','compliance',#27);
                #29=PERSON('p-1','Doe','Jane',$,$,$);
                #30=ORGANIZATION('org-1','Acme','engineering');
                #31=PERSON_AND_ORGANIZATION(#29,#30);
                #32=PERSON_AND_ORGANIZATION_ROLE('creator');
                #33=ORGANIZATION_ROLE('owner');
                #34=LANGUAGE('en-US');
                #90=APPROVAL_ROLE('authorizer');
                #91=APPROVAL_PERSON_ORGANIZATION(#31,#20,#90);
                #92=APPROVAL_DATE_TIME(#17,#20);
                #35=APPLIED_DATE_ASSIGNMENT(#13,#14,(#8));
                #36=APPLIED_DATE_AND_TIME_ASSIGNMENT(#17,#18,(#8));
                #37=APPLIED_APPROVAL_ASSIGNMENT(#20,(#8));
                #38=APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT(#22,(#8));
                #39=APPLIED_DOCUMENT_REFERENCE(#24,'internal',(#8));
                #40=APPLIED_CONTRACT_ASSIGNMENT(#26,(#8));
                #41=APPLIED_CERTIFICATION_ASSIGNMENT(#28,(#8));
                #42=APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT(#31,#32,(#8));
                #43=APPLIED_ORGANIZATION_ASSIGNMENT(#30,#33,(#8));
                #44=APPLIED_LANGUAGE_ASSIGNMENT(#34,(#8));
                #45=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #46=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #47=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #48=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #49=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #50=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #51=CARTESIAN_POINT('P6',(6.0,0.0,0.0));
                #52=CARTESIAN_POINT('P7',(7.0,0.0,0.0));
                #53=CARTESIAN_POINT('P8',(8.0,0.0,0.0));
                #54=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE','',#45);
                #55=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_TIME','',#46);
                #56=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL','',#47);
                #57=ANNOTATION_TEXT_OCCURRENCE('NOTE_SECURITY','',#48);
                #58=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOCUMENT','',#49);
                #59=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONTRACT','',#50);
                #60=ANNOTATION_TEXT_OCCURRENCE('NOTE_CERTIFICATION','',#51);
                #61=ANNOTATION_TEXT_OCCURRENCE('NOTE_PERSON_ORG','',#52);
                #62=ANNOTATION_TEXT_OCCURRENCE('NOTE_ORGANIZATION','',#53);
                #63=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#35,#11,#54,#8);
                #64=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#36,#11,#55,#8);
                #65=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#37,#11,#56,#8);
                #66=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#38,#11,#57,#8);
                #67=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#39,#11,#58,#8);
                #68=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#40,#11,#59,#8);
                #69=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#41,#11,#60,#8);
                #70=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#42,#11,#61,#8);
                #71=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#43,#11,#62,#8);
                #72=CARTESIAN_POINT('P9',(9.0,0.0,0.0));
                #73=ANNOTATION_TEXT_OCCURRENCE('NOTE_LANGUAGE','',#72);
                #74=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#44,#11,#73,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_DATE\"",
                "\"name\":\"NOTE_DATE_TIME\"",
                "\"name\":\"NOTE_APPROVAL\"",
                "\"name\":\"NOTE_SECURITY\"",
                "\"name\":\"NOTE_DOCUMENT\"",
                "\"name\":\"NOTE_CONTRACT\"",
                "\"name\":\"NOTE_CERTIFICATION\"",
                "\"name\":\"NOTE_PERSON_ORG\"",
                "\"name\":\"NOTE_ORGANIZATION\"",
                "\"name\":\"NOTE_LANGUAGE\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_TARGET\"",
                "\"viaDefinitionType\":\"APPLIED_DATE_ASSIGNMENT\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"APPLIED_DATE_AND_TIME_ASSIGNMENT\"",
                "\"viaDefinitionId\":36",
                "\"viaDefinitionType\":\"DATE_AND_TIME\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"LOCAL_TIME\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"COORDINATED_UNIVERSAL_TIME_OFFSET\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"APPLIED_APPROVAL_ASSIGNMENT\"",
                "\"viaDefinitionId\":37",
                "\"viaDefinitionType\":\"APPROVAL\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"APPROVAL_PERSON_ORGANIZATION\"",
                "\"viaDefinitionId\":91",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"PERSON\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"ORGANIZATION\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"APPROVAL_ROLE\"",
                "\"viaDefinitionId\":90",
                "\"viaDefinitionType\":\"APPROVAL_DATE_TIME\"",
                "\"viaDefinitionId\":92",
                "\"viaDefinitionType\":\"DATE_AND_TIME\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"LOCAL_TIME\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"COORDINATED_UNIVERSAL_TIME_OFFSET\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":38",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"APPLIED_DOCUMENT_REFERENCE\"",
                "\"viaDefinitionId\":39",
                "\"viaDefinitionType\":\"DOCUMENT\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"APPLIED_CONTRACT_ASSIGNMENT\"",
                "\"viaDefinitionId\":40",
                "\"viaDefinitionType\":\"CONTRACT\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"APPLIED_CERTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":41",
                "\"viaDefinitionType\":\"CERTIFICATION\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":42",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"PERSON\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"APPLIED_ORGANIZATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":43",
                "\"viaDefinitionType\":\"ORGANIZATION\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"APPLIED_LANGUAGE_ASSIGNMENT\"",
                "\"viaDefinitionId\":44",
                "\"viaDefinitionType\":\"DATE_ROLE\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"APPROVAL_STATUS\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION_LEVEL\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"DOCUMENT_TYPE\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"CONTRACT_TYPE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"CERTIFICATION_TYPE\"",
                "\"viaDefinitionId\":27",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION_ROLE\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"ORGANIZATION_ROLE\"",
                "\"viaDefinitionId\":33",
                "\"viaDefinitionType\":\"LANGUAGE\"",
                "\"viaDefinitionId\":34");
    }

    @Test
    void shouldBindPmiTargetsThroughPlainNameAndIdentificationAssignments() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_META_NAME',(),#9);
                #11=NAME_ASSIGNMENT('NAME_META');
                #12=IDENTIFICATION_ROLE('role');
                #13=IDENTIFICATION_ASSIGNMENT('ID_META',#12);
                #14=EXTERNAL_SOURCE('SRC');
                #15=EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT_META',#12,#14);
                #16=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #17=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #18=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #19=ANNOTATION_TEXT_OCCURRENCE('NOTE_NAME_ASSIGN','',#16);
                #20=ANNOTATION_TEXT_OCCURRENCE('NOTE_ID_ASSIGN','',#17);
                #21=ANNOTATION_TEXT_OCCURRENCE('NOTE_EXT_ID_ASSIGN','',#18);
                #22=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#11,#10,#19,#8);
                #23=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#13,#10,#20,#8);
                #24=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#15,#10,#21,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_NAME_ASSIGN\"",
                "\"name\":\"NOTE_ID_ASSIGN\"",
                "\"name\":\"NOTE_EXT_ID_ASSIGN\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_META_NAME\"",
                "\"viaDefinitionType\":\"NAME_ASSIGNMENT\"",
                "\"viaDefinitionId\":11",
                "\"viaDefinitionType\":\"IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"IDENTIFICATION_ROLE\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"EXTERNAL_IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":15");
    }

    @Test
    void shouldPreserveRepresentationSubtypeNameInDefinitionMetadata() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=SHAPE_REPRESENTATION('REP_SHAPE',(),#9);
                #11=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #12=ANNOTATION_TEXT_OCCURRENCE('NOTE_SHAPE_REP','',#11);
                #13=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#10,#10,#12,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_SHAPE_REP\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_SHAPE\"",
                "\"viaDefinitionType\":\"SHAPE_REPRESENTATION\"",
                "\"viaDefinitionId\":10");
    }

    @Test
    void shouldBindPmiTargetsThroughAttributeAssertionDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=PROPERTY_DEFINITION('PD_TARGET','',#8);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #11=REPRESENTATION('REP_ASSERT',(),#10);
                #12=PROPERTY_DEFINITION_REPRESENTATION(#9,#11);
                #13=ATTRIBUTE_ASSERTION(#9,#11);
                #14=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #15=ANNOTATION_TEXT_OCCURRENCE('NOTE_ASSERT','',#14);
                #16=PMI_REQUIREMENT_ITEM_ASSOCIATION('ASSOC','',#13,#11,#15,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_ASSERT\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_ASSERT\"",
                "\"viaDefinitionType\":\"ATTRIBUTE_ASSERTION\"",
                "\"viaDefinitionId\":13");
    }

    @Test
    void shouldBindPmiTargetsThroughProductRelationshipMetadataChains() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PROD_A','Product A','',(#2));
                #4=PRODUCT('PROD_B','Product B','',(#2));
                #5=PRODUCT_DEFINITION_FORMATION('vA','',#3);
                #6=PRODUCT_DEFINITION_FORMATION('vB','',#4);
                #7=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #8=PRODUCT_DEFINITION('pd-a','',#5,#7);
                #9=PRODUCT_DEFINITION('pd-b','',#6,#7);
                #10=PRODUCT_DEFINITION_SHAPE('pds-a','shape a',#8);
                #11=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #12=REPRESENTATION('REP_PROD',(),#11);
                #13=SHAPE_DEFINITION_REPRESENTATION(#10,#12);
                #14=PRODUCT_RELATIONSHIP('PR','contains','',#4,#3);
                #15=PRODUCT_DEFINITION_FORMATION_RELATIONSHIP('PFR','versions','',#6,#5);
                #16=NEXT_ASSEMBLY_USAGE_OCCURRENCE('NAUO','occ','',#9,#8);
                #17=PRODUCT_DEFINITION_RELATIONSHIP('PDR_1','peer-1','',#9,#8);
                #18=PRODUCT_DEFINITION_RELATIONSHIP('PDR_2','peer-2','',#9,#8);
                #19=PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP('PDRR','links','',#17,#18);
                #20=PRODUCT_DEFINITION_EFFECTIVITY('PDE-1','serial usage',#8);
                #21=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #22=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #23=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #24=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #25=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #26=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRODUCT','',#21);
                #27=ANNOTATION_TEXT_OCCURRENCE('NOTE_FORMATION','',#22);
                #28=ANNOTATION_TEXT_OCCURRENCE('NOTE_OCCURRENCE','',#23);
                #29=ANNOTATION_TEXT_OCCURRENCE('NOTE_PDRR','',#24);
                #30=ANNOTATION_TEXT_OCCURRENCE('NOTE_EFFECTIVITY','',#25);
                #31=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#4,#12,#26,#10);
                #32=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#6,#12,#27,#10);
                #33=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#16,#12,#28,#10);
                #34=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#19,#12,#29,#10);
                #35=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#20,#12,#30,#10);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_PRODUCT\"",
                "\"name\":\"NOTE_FORMATION\"",
                "\"name\":\"NOTE_OCCURRENCE\"",
                "\"name\":\"NOTE_PDRR\"",
                "\"name\":\"NOTE_EFFECTIVITY\"",
                "\"id\":12,\"type\":\"representation\",\"name\":\"REP_PROD\"",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_EFFECTIVITY\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"PRODUCT_RELATIONSHIP\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_FORMATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"NEXT_ASSEMBLY_USAGE_OCCURRENCE\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP\"",
                "\"viaDefinitionId\":19");
    }

    @Test
    void shouldBindPmiTargetsThroughDirectRelationshipCarrierDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd-a','',#4,#5);
                #7=PRODUCT_DEFINITION('pd-b','',#4,#5);
                #8=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #9=PROPERTY_DEFINITION('PD_ROOT','',#8);
                #10=PROPERTY_DEFINITION('PD_TARGET','',#8);
                #11=PROPERTY_DEFINITION_RELATIONSHIP('PDR','link',#9,#10);
                #12=GENERAL_PROPERTY('GP1','gp1','');
                #13=GENERAL_PROPERTY('GP2','gp2','');
                #14=GENERAL_PROPERTY_RELATIONSHIP('GPR','',#12,#13);
                #15=SHAPE_ASPECT('SA1','sa1',#8,.T.);
                #16=SHAPE_ASPECT('SA2','sa2',#8,.T.);
                #17=SHAPE_ASPECT_RELATIONSHIP('SAR','',#15,#16);
                #18=PRODUCT_DEFINITION_RELATIONSHIP('PDR_A','peer-a','',#7,#6);
                #19=PRODUCT_DEFINITION_RELATIONSHIP('PDR_B','peer-b','',#7,#6);
                #20=PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP('PDRR','links','',#18,#19);
                #21=PROPERTY_DEFINITION('PD_GP','',#13);
                #22=PROPERTY_DEFINITION('PD_SA','',#16);
                #23=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #24=REPRESENTATION('REP_PROP',(),#23);
                #25=REPRESENTATION('REP_GP',(),#23);
                #26=REPRESENTATION('REP_SA',(),#23);
                #27=REPRESENTATION('REP_PDRR',(),#23);
                #28=PROPERTY_DEFINITION_REPRESENTATION(#10,#24);
                #29=ACTION_PROPERTY_REPRESENTATION(#21,#25);
                #30=PROPERTY_DEFINITION_REPRESENTATION(#22,#26);
                #31=SHAPE_DEFINITION_REPRESENTATION(#8,#27);
                #32=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #33=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #34=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #35=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #36=ANNOTATION_TEXT_OCCURRENCE('NOTE_PDR','',#32);
                #37=ANNOTATION_TEXT_OCCURRENCE('NOTE_GPR','',#33);
                #38=ANNOTATION_TEXT_OCCURRENCE('NOTE_SAR','',#34);
                #39=ANNOTATION_TEXT_OCCURRENCE('NOTE_PDRR','',#35);
                #40=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#11,#24,#36,#8);
                #41=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#14,#25,#37,#8);
                #42=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#17,#26,#38,#8);
                #43=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#20,#27,#39,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughDirectProductAndMetadataRelationshipCarrierDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PROD_A','Product A','',(#2));
                #4=PRODUCT('PROD_B','Product B','',(#2));
                #5=PRODUCT_DEFINITION_FORMATION('vA','',#3);
                #6=PRODUCT_DEFINITION_FORMATION('vB','',#4);
                #7=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #8=PRODUCT_DEFINITION('pd-a','',#5,#7);
                #9=PRODUCT_DEFINITION('pd-b','',#6,#7);
                #10=PRODUCT_DEFINITION_SHAPE('pds-a','shape a',#8);
                #11=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #12=REPRESENTATION('REP_REL',(),#11);
                #13=SHAPE_DEFINITION_REPRESENTATION(#10,#12);
                #14=PRODUCT_RELATIONSHIP('PR','contains','',#4,#3);
                #15=PRODUCT_DEFINITION_FORMATION_RELATIONSHIP('PFR','versions','',#6,#5);
                #16=GROUP('G1','g1');
                #17=GROUP('G2','g2');
                #18=GROUP_RELATIONSHIP('GR','',#16,#17);
                #19=DOCUMENT_TYPE('spec');
                #20=DOCUMENT('DOC-1','Spec A','',#19);
                #21=DOCUMENT('DOC-2','Spec B','',#19);
                #22=DOCUMENT_RELATIONSHIP('DR','',#20,#21);
                #23=ORGANIZATION('ORG-1','Org A','');
                #24=ORGANIZATION('ORG-2','Org B','');
                #25=ORGANIZATION_RELATIONSHIP('OR','',#23,#24);
                #26=PRODUCT_CATEGORY('CAT_A','cat a');
                #27=PRODUCT_CATEGORY('CAT_B','cat b');
                #28=PRODUCT_CATEGORY_RELATIONSHIP('CR','',#26,#27);
                #29=EFFECTIVITY('E-1');
                #30=EFFECTIVITY('E-2');
                #31=EFFECTIVITY_RELATIONSHIP('ER','',#29,#30);
                #32=PROPERTY_DEFINITION('PD_GROUP','',#17);
                #33=PROPERTY_DEFINITION('PD_DOC','',#21);
                #34=PROPERTY_DEFINITION('PD_ORG','',#24);
                #35=PROPERTY_DEFINITION('PD_CAT','',#27);
                #36=PROPERTY_DEFINITION('PD_EFF','',#30);
                #37=PROPERTY_DEFINITION_REPRESENTATION(#32,#12);
                #38=PROPERTY_DEFINITION_REPRESENTATION(#33,#12);
                #39=PROPERTY_DEFINITION_REPRESENTATION(#34,#12);
                #40=PROPERTY_DEFINITION_REPRESENTATION(#35,#12);
                #41=PROPERTY_DEFINITION_REPRESENTATION(#36,#12);
                #42=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #43=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #44=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #45=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #46=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #47=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #48=CARTESIAN_POINT('P6',(6.0,0.0,0.0));
                #49=ANNOTATION_TEXT_OCCURRENCE('NOTE_PR','',#42);
                #50=ANNOTATION_TEXT_OCCURRENCE('NOTE_PFR','',#43);
                #51=ANNOTATION_TEXT_OCCURRENCE('NOTE_GR','',#44);
                #52=ANNOTATION_TEXT_OCCURRENCE('NOTE_DR','',#45);
                #53=ANNOTATION_TEXT_OCCURRENCE('NOTE_OR','',#46);
                #54=ANNOTATION_TEXT_OCCURRENCE('NOTE_CR','',#47);
                #55=ANNOTATION_TEXT_OCCURRENCE('NOTE_ER','',#48);
                #56=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#14,#12,#49,#10);
                #57=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#15,#12,#50,#10);
                #58=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#18,#12,#51,#10);
                #59=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#22,#12,#52,#10);
                #60=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#25,#12,#53,#10);
                #61=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#28,#12,#54,#10);
                #62=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#31,#12,#55,#10);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughRelationshipMetadataFamilies() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #9=REPRESENTATION('REP_META',(),#8);
                #10=GROUP('G1','g1');
                #11=GROUP('G2','g2');
                #12=GROUP_RELATIONSHIP('GR','',#10,#11);
                #13=DOCUMENT_TYPE('spec');
                #14=DOCUMENT('DOC-1','Spec A','',#13);
                #15=DOCUMENT('DOC-2','Spec B','',#13);
                #16=DOCUMENT_RELATIONSHIP('DR','',#14,#15);
                #17=DOCUMENT_USAGE_CONSTRAINT(#14,'scope','assembly only');
                #18=ORGANIZATION('ORG-1','Org A','');
                #19=ORGANIZATION('ORG-2','Org B','');
                #20=ORGANIZATION_RELATIONSHIP('OR','',#18,#19);
                #21=PRODUCT_CATEGORY('CAT_A','cat a');
                #22=PRODUCT_CATEGORY('CAT_B','cat b');
                #23=PRODUCT_CATEGORY_RELATIONSHIP('CR','',#21,#22);
                #24=EFFECTIVITY('E-1');
                #25=EFFECTIVITY('E-2');
                #26=EFFECTIVITY_RELATIONSHIP('ER','',#24,#25);
                #27=PROPERTY_DEFINITION('PD_GROUP','',#11);
                #28=PROPERTY_DEFINITION('PD_DOC','',#15);
                #29=PROPERTY_DEFINITION('PD_ORG','',#19);
                #30=PROPERTY_DEFINITION('PD_CAT','',#22);
                #31=PROPERTY_DEFINITION('PD_EFF','',#25);
                #32=PROPERTY_DEFINITION_REPRESENTATION(#27,#9);
                #33=PROPERTY_DEFINITION_REPRESENTATION(#28,#9);
                #34=PROPERTY_DEFINITION_REPRESENTATION(#29,#9);
                #35=PROPERTY_DEFINITION_REPRESENTATION(#30,#9);
                #36=PROPERTY_DEFINITION_REPRESENTATION(#31,#9);
                #37=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #38=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #39=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #40=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #41=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #42=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #43=ANNOTATION_TEXT_OCCURRENCE('NOTE_GROUP','',#37);
                #44=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOCUMENT','',#38);
                #45=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOC_CONSTRAINT','',#39);
                #46=ANNOTATION_TEXT_OCCURRENCE('NOTE_ORGANIZATION','',#40);
                #47=ANNOTATION_TEXT_OCCURRENCE('NOTE_CATEGORY','',#41);
                #48=ANNOTATION_TEXT_OCCURRENCE('NOTE_EFFECTIVITY','',#42);
                #49=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#10,#9,#43,#7);
                #50=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#14,#9,#44,#7);
                #51=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#17,#9,#45,#7);
                #52=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#18,#9,#46,#7);
                #53=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#21,#9,#47,#7);
                #54=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#24,#9,#48,#7);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_GROUP\"",
                "\"name\":\"NOTE_DOCUMENT\"",
                "\"name\":\"NOTE_DOC_CONSTRAINT\"",
                "\"name\":\"NOTE_ORGANIZATION\"",
                "\"name\":\"NOTE_CATEGORY\"",
                "\"name\":\"NOTE_EFFECTIVITY\"",
                "\"id\":9,\"type\":\"representation\",\"name\":\"REP_META\"",
                "\"viaDefinitionType\":\"GROUP\"",
                "\"viaDefinitionId\":10",
                "\"viaDefinitionType\":\"GROUP_RELATIONSHIP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"DOCUMENT\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"DOCUMENT_TYPE\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"DOCUMENT_RELATIONSHIP\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"DOCUMENT_USAGE_CONSTRAINT\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"ORGANIZATION\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"ORGANIZATION_RELATIONSHIP\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"PRODUCT_CATEGORY\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"PRODUCT_CATEGORY_RELATIONSHIP\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"EFFECTIVITY\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"EFFECTIVITY_RELATIONSHIP\"",
                "\"viaDefinitionId\":26");
    }

    @Test
    void shouldBindPmiTargetsThroughExternalCategoryAndLayerMetadata() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_META_EXT',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=PRODUCT_RELATED_PRODUCT_CATEGORY('CAT_LINK','',(#3));
                #13=DOCUMENT_TYPE('spec');
                #14=DOCUMENT('DOC-1','Spec A','',#13);
                #15=DOCUMENT('DOC-2','Spec B','',#13);
                #16=DOCUMENT_RELATIONSHIP('DR','',#14,#15);
                #17=DOCUMENT_REFERENCE(#14,'internal');
                #18=PROPERTY_DEFINITION('PD_DOC','',#15);
                #19=PROPERTY_DEFINITION_REPRESENTATION(#18,#10);
                #20=EXTERNAL_SOURCE('SRC_A');
                #21=EXTERNAL_SOURCE('SRC_B');
                #22=EXTERNAL_SOURCE_RELATIONSHIP('SR','',#20,#21);
                #23=EXTERNALLY_DEFINED_ITEM('EXT-1',#21);
                #24=PROPERTY_DEFINITION('PD_EXT','',#23);
                #25=PROPERTY_DEFINITION_REPRESENTATION(#24,#10);
                #26=PRESENTATION_LAYER_ASSIGNMENT('LAYER_A','',(#10));
                #27=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #28=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #29=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #30=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #31=ANNOTATION_TEXT_OCCURRENCE('NOTE_CATEGORY','',#27);
                #32=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOCUMENT_REF','',#28);
                #33=ANNOTATION_TEXT_OCCURRENCE('NOTE_EXTERNAL_SOURCE','',#29);
                #34=ANNOTATION_TEXT_OCCURRENCE('NOTE_LAYER','',#30);
                #35=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#12,#10,#31,#8);
                #36=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#17,#10,#32,#8);
                #37=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#20,#10,#33,#8);
                #38=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#26,#10,#34,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_CATEGORY\"",
                "\"name\":\"NOTE_DOCUMENT_REF\"",
                "\"name\":\"NOTE_EXTERNAL_SOURCE\"",
                "\"name\":\"NOTE_LAYER\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_META_EXT\"",
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
    void shouldBindPmiTargetsThroughBareMetadataAndPlainAssignments() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_META_ASSIGN',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=APPROVAL_STATUS('approved');
                #13=APPROVAL(#12,'design');
                #14=APPROVAL_ASSIGNMENT(#13);
                #15=APPLIED_APPROVAL_ASSIGNMENT(#13,(#8));
                #16=SECURITY_CLASSIFICATION_LEVEL('unclassified');
                #17=SECURITY_CLASSIFICATION('sec','export control',#16);
                #18=SECURITY_CLASSIFICATION_ASSIGNMENT(#17);
                #19=APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT(#17,(#8));
                #20=CONTRACT_TYPE('purchase');
                #21=CONTRACT('C-1','supply',#20);
                #22=CONTRACT_ASSIGNMENT(#21);
                #23=APPLIED_CONTRACT_ASSIGNMENT(#21,(#8));
                #24=CERTIFICATION_TYPE('material');
                #25=CERTIFICATION('CERT-1','compliance',#24);
                #26=CERTIFICATION_ASSIGNMENT(#25);
                #27=APPLIED_CERTIFICATION_ASSIGNMENT(#25,(#8));
                #28=PERSON('p-1','Doe','Jane',$,$,$);
                #29=ORGANIZATION('org-1','Acme','engineering');
                #30=PERSON_AND_ORGANIZATION(#28,#29);
                #31=PERSON_AND_ORGANIZATION_ROLE('creator');
                #32=PERSON_AND_ORGANIZATION_ASSIGNMENT(#30,#31);
                #33=APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT(#30,#31,(#8));
                #34=LANGUAGE('en-US');
                #35=LANGUAGE_ASSIGNMENT(#34);
                #36=APPLIED_LANGUAGE_ASSIGNMENT(#34,(#8));
                #90=APPROVAL_ROLE('authorizer');
                #91=APPROVAL_PERSON_ORGANIZATION(#30,#13,#90);
                #93=CALENDAR_DATE(2026,11,4);
                #94=COORDINATED_UNIVERSAL_TIME_OFFSET(8,0,.AHEAD.);
                #95=LOCAL_TIME(9,15,$,#94);
                #96=DATE_AND_TIME(#93,#95);
                #92=APPROVAL_DATE_TIME(#96,#13);
                #37=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #38=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #39=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #40=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #41=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #42=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #43=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL','',#37);
                #44=ANNOTATION_TEXT_OCCURRENCE('NOTE_SECURITY','',#38);
                #45=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONTRACT','',#39);
                #46=ANNOTATION_TEXT_OCCURRENCE('NOTE_CERTIFICATION','',#40);
                #47=ANNOTATION_TEXT_OCCURRENCE('NOTE_PERSON_ORG','',#41);
                #48=ANNOTATION_TEXT_OCCURRENCE('NOTE_LANGUAGE','',#42);
                #49=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#14,#10,#43,#8);
                #50=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#18,#10,#44,#8);
                #51=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#22,#10,#45,#8);
                #52=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#26,#10,#46,#8);
                #53=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#32,#10,#47,#8);
                #54=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#35,#10,#48,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_APPROVAL\"",
                "\"name\":\"NOTE_SECURITY\"",
                "\"name\":\"NOTE_CONTRACT\"",
                "\"name\":\"NOTE_CERTIFICATION\"",
                "\"name\":\"NOTE_PERSON_ORG\"",
                "\"name\":\"NOTE_LANGUAGE\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_META_ASSIGN\"",
                "\"viaDefinitionType\":\"APPROVAL_ASSIGNMENT\"",
                "\"viaDefinitionId\":14",
                "\"viaDefinitionType\":\"APPROVAL\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"APPROVAL_STATUS\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"APPROVAL_PERSON_ORGANIZATION\"",
                "\"viaDefinitionId\":91",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"PERSON\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"ORGANIZATION\"",
                "\"viaDefinitionId\":29",
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
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION\"",
                "\"viaDefinitionId\":17",
                "\"viaDefinitionType\":\"SECURITY_CLASSIFICATION_LEVEL\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"CONTRACT_ASSIGNMENT\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"CONTRACT\"",
                "\"viaDefinitionId\":21",
                "\"viaDefinitionType\":\"CONTRACT_TYPE\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"CERTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"CERTIFICATION\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"CERTIFICATION_TYPE\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":32",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"PERSON\"",
                "\"viaDefinitionId\":28",
                "\"viaDefinitionType\":\"ORGANIZATION\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"PERSON_AND_ORGANIZATION_ROLE\"",
                "\"viaDefinitionId\":31",
                "\"viaDefinitionType\":\"LANGUAGE_ASSIGNMENT\"",
                "\"viaDefinitionId\":35",
                "\"viaDefinitionType\":\"LANGUAGE\"",
                "\"viaDefinitionId\":34");
    }

    @Test
    void shouldBindPmiTargetsThroughRemainingPlainMetadataAssignments() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_META_MISC',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=GROUP('G1','group');
                #13=GROUP_ASSIGNMENT(#12);
                #14=APPLIED_GROUP_ASSIGNMENT(#12,(#8));
                #15=CLASSIFICATION_ROLE('family');
                #16=CLASSIFICATION_ASSIGNMENT(#12,#15);
                #17=APPLIED_CLASSIFICATION_ASSIGNMENT(#12,#15,(#8));
                #18=CALENDAR_DATE(2026,11,4);
                #19=DATE_ROLE('release');
                #20=DATE_ASSIGNMENT(#18,#19);
                #21=APPLIED_DATE_ASSIGNMENT(#18,#19,(#8));
                #22=COORDINATED_UNIVERSAL_TIME_OFFSET(8,0,.AHEAD.);
                #23=LOCAL_TIME(9,15,$,#22);
                #24=DATE_AND_TIME(#18,#23);
                #25=DATE_TIME_ROLE('created');
                #26=DATE_TIME_ASSIGNMENT(#24,#25);
                #27=APPLIED_DATE_AND_TIME_ASSIGNMENT(#24,#25,(#8));
                #28=IDENTIFICATION_ROLE('ext role');
                #29=EXTERNAL_SOURCE('SRC_EXT');
                #30=EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT-42',#28,#29);
                #31=APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT-42',#28,#29,(#8));
                #97=EXTERNAL_SOURCE('SRC_LINK');
                #98=EXTERNAL_SOURCE_RELATIONSHIP('SR','',#29,#97);
                #99=EXTERNALLY_DEFINED_ITEM('EXT-REF',#97);
                #32=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #33=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #34=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #35=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #36=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #37=ANNOTATION_TEXT_OCCURRENCE('NOTE_GROUP_ASSIGN','',#32);
                #38=ANNOTATION_TEXT_OCCURRENCE('NOTE_CLASS_ASSIGN','',#33);
                #39=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_ASSIGN','',#34);
                #40=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_TIME_ASSIGN','',#35);
                #41=ANNOTATION_TEXT_OCCURRENCE('NOTE_EXTERNAL_ID','',#36);
                #42=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#13,#10,#37,#8);
                #43=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#16,#10,#38,#8);
                #44=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#20,#10,#39,#8);
                #45=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#26,#10,#40,#8);
                #46=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#30,#10,#41,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_GROUP_ASSIGN\"",
                "\"name\":\"NOTE_CLASS_ASSIGN\"",
                "\"name\":\"NOTE_DATE_ASSIGN\"",
                "\"name\":\"NOTE_DATE_TIME_ASSIGN\"",
                "\"name\":\"NOTE_EXTERNAL_ID\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_META_MISC\"",
                "\"viaDefinitionType\":\"GROUP_ASSIGNMENT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"GROUP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"CLASSIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":16",
                "\"viaDefinitionType\":\"GROUP\"",
                "\"viaDefinitionId\":12",
                "\"viaDefinitionType\":\"CLASSIFICATION_ROLE\"",
                "\"viaDefinitionId\":15",
                "\"viaDefinitionType\":\"DATE_ASSIGNMENT\"",
                "\"viaDefinitionId\":20",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"DATE_ROLE\"",
                "\"viaDefinitionId\":19",
                "\"viaDefinitionType\":\"DATE_TIME_ASSIGNMENT\"",
                "\"viaDefinitionId\":26",
                "\"viaDefinitionType\":\"DATE_AND_TIME\"",
                "\"viaDefinitionId\":24",
                "\"viaDefinitionType\":\"CALENDAR_DATE\"",
                "\"viaDefinitionId\":18",
                "\"viaDefinitionType\":\"LOCAL_TIME\"",
                "\"viaDefinitionId\":23",
                "\"viaDefinitionType\":\"COORDINATED_UNIVERSAL_TIME_OFFSET\"",
                "\"viaDefinitionId\":22",
                "\"viaDefinitionType\":\"DATE_TIME_ROLE\"",
                "\"viaDefinitionId\":25",
                "\"viaDefinitionType\":\"EXTERNAL_IDENTIFICATION_ASSIGNMENT\"",
                "\"viaDefinitionId\":30",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE\"",
                "\"viaDefinitionId\":29",
                "\"viaDefinitionType\":\"EXTERNAL_SOURCE_RELATIONSHIP\"",
                "\"viaDefinitionId\":98",
                "\"viaDefinitionType\":\"EXTERNALLY_DEFINED_ITEM\"",
                "\"viaDefinitionId\":99",
                "\"viaDefinitionType\":\"IDENTIFICATION_ROLE\"",
                "\"viaDefinitionId\":28");
    }

    @Test
    void shouldBindPmiTargetsThroughAppliedGroupAssignment() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_META_GROUP',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=GROUP('G1','group');
                #13=APPLIED_GROUP_ASSIGNMENT(#12,(#8));
                #14=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #15=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPLIED_GROUP','',#14);
                #16=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#13,#10,#15,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_APPLIED_GROUP\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_META_GROUP\"",
                "\"viaDefinitionType\":\"APPLIED_GROUP_ASSIGNMENT\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"GROUP\"",
                "\"viaDefinitionId\":12");
    }

    @Test
    void shouldBindPmiTargetsThroughMetadataRolesStatusesAndTypes() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_META_ROLES',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=APPROVAL_STATUS('released');
                #13=APPROVAL(#12,'design');
                #14=APPLIED_APPROVAL_ASSIGNMENT(#13,(#8));
                #15=SECURITY_CLASSIFICATION_LEVEL('controlled');
                #16=SECURITY_CLASSIFICATION('sec','purpose',#15);
                #17=APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT(#16,(#8));
                #18=CONTRACT_TYPE('purchase');
                #19=CONTRACT('C-1','supply',#18);
                #20=APPLIED_CONTRACT_ASSIGNMENT(#19,(#8));
                #21=CERTIFICATION_TYPE('material');
                #22=CERTIFICATION('CERT-1','compliance',#21);
                #23=APPLIED_CERTIFICATION_ASSIGNMENT(#22,(#8));
                #24=PERSON('p-1','Doe','Jane',$,$,$);
                #25=ORGANIZATION('org-1','Acme','engineering');
                #26=PERSON_AND_ORGANIZATION(#24,#25);
                #27=APPROVAL_ROLE('authorizer');
                #28=APPROVAL_PERSON_ORGANIZATION(#26,#13,#27);
                #108=APPROVAL_DATE_TIME(#41,#13);
                #29=PERSON_AND_ORGANIZATION_ROLE('creator');
                #30=APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT(#26,#29,(#8));
                #31=ORGANIZATION_ROLE('owner');
                #32=APPLIED_ORGANIZATION_ASSIGNMENT(#25,#31,(#8));
                #33=GROUP('G1','group');
                #34=CLASSIFICATION_ROLE('family');
                #35=APPLIED_CLASSIFICATION_ASSIGNMENT(#33,#34,(#8));
                #36=CALENDAR_DATE(2026,11,4);
                #37=DATE_ROLE('release');
                #38=APPLIED_DATE_ASSIGNMENT(#36,#37,(#8));
                #39=COORDINATED_UNIVERSAL_TIME_OFFSET(8,0,.AHEAD.);
                #40=LOCAL_TIME(9,15,$,#39);
                #41=DATE_AND_TIME(#36,#40);
                #42=DATE_TIME_ROLE('created');
                #43=APPLIED_DATE_AND_TIME_ASSIGNMENT(#41,#42,(#8));
                #44=IDENTIFICATION_ROLE('identifier');
                #45=EXTERNAL_SOURCE('SRC_EXT');
                #46=APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT-42',#44,#45,(#8));
                #47=DOCUMENT_TYPE('spec');
                #48=DOCUMENT('DOC-1','Spec A','',#47);
                #49=PROPERTY_DEFINITION('PD_DOC','',#48);
                #50=PROPERTY_DEFINITION_REPRESENTATION(#49,#10);
                #51=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #52=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #53=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #54=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #55=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #56=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #57=CARTESIAN_POINT('P6',(6.0,0.0,0.0));
                #58=CARTESIAN_POINT('P7',(7.0,0.0,0.0));
                #59=CARTESIAN_POINT('P8',(8.0,0.0,0.0));
                #60=CARTESIAN_POINT('P9',(9.0,0.0,0.0));
                #61=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL_STATUS','',#51);
                #62=ANNOTATION_TEXT_OCCURRENCE('NOTE_SECURITY_LEVEL','',#52);
                #63=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONTRACT_TYPE','',#53);
                #64=ANNOTATION_TEXT_OCCURRENCE('NOTE_CERTIFICATION_TYPE','',#54);
                #65=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL_ROLE','',#55);
                #66=ANNOTATION_TEXT_OCCURRENCE('NOTE_PERSON_ORG_ROLE','',#56);
                #67=ANNOTATION_TEXT_OCCURRENCE('NOTE_ORG_ROLE','',#57);
                #68=ANNOTATION_TEXT_OCCURRENCE('NOTE_CLASS_ROLE','',#58);
                #69=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_ROLE','',#59);
                #70=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_TIME_ROLE','',#60);
                #71=CARTESIAN_POINT('P10',(10.0,0.0,0.0));
                #72=CARTESIAN_POINT('P11',(11.0,0.0,0.0));
                #73=ANNOTATION_TEXT_OCCURRENCE('NOTE_ID_ROLE','',#71);
                #74=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOCUMENT_TYPE','',#72);
                #75=CARTESIAN_POINT('P12',(12.0,0.0,0.0));
                #76=CARTESIAN_POINT('P13',(13.0,0.0,0.0));
                #77=CARTESIAN_POINT('P14',(14.0,0.0,0.0));
                #78=CARTESIAN_POINT('P15',(15.0,0.0,0.0));
                #79=CARTESIAN_POINT('P16',(16.0,0.0,0.0));
                #80=CARTESIAN_POINT('P17',(17.0,0.0,0.0));
                #81=CARTESIAN_POINT('P18',(18.0,0.0,0.0));
                #82=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL','',#75);
                #83=ANNOTATION_TEXT_OCCURRENCE('NOTE_SECURITY','',#76);
                #84=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONTRACT','',#77);
                #85=ANNOTATION_TEXT_OCCURRENCE('NOTE_CERTIFICATION','',#78);
                #86=ANNOTATION_TEXT_OCCURRENCE('NOTE_PERSON_ORG','',#79);
                #87=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL_PERSON_ORG','',#80);
                #88=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL_DATE_TIME','',#81);
                #89=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#12,#10,#61,#8);
                #90=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#15,#10,#62,#8);
                #91=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#18,#10,#63,#8);
                #92=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#21,#10,#64,#8);
                #93=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#27,#10,#65,#8);
                #94=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#29,#10,#66,#8);
                #95=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#31,#10,#67,#8);
                #96=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#34,#10,#68,#8);
                #97=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#37,#10,#69,#8);
                #98=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#42,#10,#70,#8);
                #99=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#44,#10,#73,#8);
                #100=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#47,#10,#74,#8);
                #101=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#13,#10,#82,#8);
                #102=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#16,#10,#83,#8);
                #103=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#19,#10,#84,#8);
                #104=PMI_REQUIREMENT_ITEM_ASSOCIATION('A15','',#22,#10,#85,#8);
                #105=PMI_REQUIREMENT_ITEM_ASSOCIATION('A16','',#26,#10,#86,#8);
                #106=PMI_REQUIREMENT_ITEM_ASSOCIATION('A17','',#28,#10,#87,#8);
                #107=PMI_REQUIREMENT_ITEM_ASSOCIATION('A18','',#108,#10,#88,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_APPROVAL_STATUS\"",
                "\"name\":\"NOTE_SECURITY_LEVEL\"",
                "\"name\":\"NOTE_CONTRACT_TYPE\"",
                "\"name\":\"NOTE_CERTIFICATION_TYPE\"",
                "\"name\":\"NOTE_APPROVAL_ROLE\"",
                "\"name\":\"NOTE_PERSON_ORG_ROLE\"",
                "\"name\":\"NOTE_ORG_ROLE\"",
                "\"name\":\"NOTE_CLASS_ROLE\"",
                "\"name\":\"NOTE_DATE_ROLE\"",
                "\"name\":\"NOTE_DATE_TIME_ROLE\"",
                "\"name\":\"NOTE_ID_ROLE\"",
                "\"name\":\"NOTE_DOCUMENT_TYPE\"",
                "\"name\":\"NOTE_APPROVAL\"",
                "\"name\":\"NOTE_SECURITY\"",
                "\"name\":\"NOTE_CONTRACT\"",
                "\"name\":\"NOTE_CERTIFICATION\"",
                "\"name\":\"NOTE_PERSON_ORG\"",
                "\"name\":\"NOTE_APPROVAL_PERSON_ORG\"",
                "\"name\":\"NOTE_APPROVAL_DATE_TIME\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_META_ROLES\"",
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
    void shouldBindPmiTargetsThroughDirectMetadataLeafAndEndpointLinkDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_DIRECT_META',(),#9);
                #11=REPRESENTATION('REP_AUX',(),#9);
                #12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);
                #13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #15=DIRECTION('DZ',(0.0,0.0,1.0));
                #16=DIRECTION('DX',(1.0,0.0,0.0));
                #17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);
                #18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);
                #19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);
                #20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));
                #21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);
                #22=CALENDAR_DATE(2026,11,4);
                #23=COORDINATED_UNIVERSAL_TIME_OFFSET(8,0,.AHEAD.);
                #24=LOCAL_TIME(9,15,$,#23);
                #25=DATE_AND_TIME(#22,#24);
                #26=APPROVAL_STATUS('released');
                #27=APPROVAL(#26,'design');
                #28=APPROVAL_ROLE('authorizer');
                #29=PERSON('p-1','Doe','Jane',$,$,$);
                #30=ORGANIZATION('org-1','Acme','engineering');
                #31=PERSON_AND_ORGANIZATION(#29,#30);
                #32=APPROVAL_PERSON_ORGANIZATION(#31,#27,#28);
                #33=APPROVAL_DATE_TIME(#25,#27);
                #34=SECURITY_CLASSIFICATION_LEVEL('controlled');
                #35=SECURITY_CLASSIFICATION('sec','purpose',#34);
                #36=CONTRACT_TYPE('purchase');
                #37=CONTRACT('C-1','supply',#36);
                #38=CERTIFICATION_TYPE('material');
                #39=CERTIFICATION('CERT-1','compliance',#38);
                #40=PERSON_AND_ORGANIZATION_ROLE('creator');
                #41=ORGANIZATION_ROLE('owner');
                #42=CLASSIFICATION_ROLE('family');
                #43=DATE_ROLE('release');
                #44=DATE_TIME_ROLE('created');
                #45=IDENTIFICATION_ROLE('identifier');
                #46=DOCUMENT_TYPE('spec');
                #47=LANGUAGE('en-US');
                #48=PROPERTY_DEFINITION('PD_CAL_DATE','',#22);
                #49=PROPERTY_DEFINITION_REPRESENTATION(#48,#10);
                #50=PROPERTY_DEFINITION('PD_ZONE','',#23);
                #51=PROPERTY_DEFINITION_REPRESENTATION(#50,#10);
                #52=PROPERTY_DEFINITION('PD_LOCAL_TIME','',#24);
                #53=PROPERTY_DEFINITION_REPRESENTATION(#52,#10);
                #54=PROPERTY_DEFINITION('PD_DATE_TIME','',#25);
                #55=PROPERTY_DEFINITION_REPRESENTATION(#54,#10);
                #56=PROPERTY_DEFINITION('PD_APPROVAL_STATUS','',#26);
                #57=PROPERTY_DEFINITION_REPRESENTATION(#56,#10);
                #58=PROPERTY_DEFINITION('PD_APPROVAL','',#27);
                #59=PROPERTY_DEFINITION_REPRESENTATION(#58,#10);
                #60=PROPERTY_DEFINITION('PD_SECURITY_LEVEL','',#34);
                #61=PROPERTY_DEFINITION_REPRESENTATION(#60,#10);
                #62=PROPERTY_DEFINITION('PD_SECURITY','',#35);
                #63=PROPERTY_DEFINITION_REPRESENTATION(#62,#10);
                #64=PROPERTY_DEFINITION('PD_CONTRACT_TYPE','',#36);
                #65=PROPERTY_DEFINITION_REPRESENTATION(#64,#10);
                #66=PROPERTY_DEFINITION('PD_CONTRACT','',#37);
                #67=PROPERTY_DEFINITION_REPRESENTATION(#66,#10);
                #68=PROPERTY_DEFINITION('PD_CERT_TYPE','',#38);
                #69=PROPERTY_DEFINITION_REPRESENTATION(#68,#10);
                #70=PROPERTY_DEFINITION('PD_CERT','',#39);
                #71=PROPERTY_DEFINITION_REPRESENTATION(#70,#10);
                #72=PROPERTY_DEFINITION('PD_PERSON','',#29);
                #73=PROPERTY_DEFINITION_REPRESENTATION(#72,#10);
                #74=PROPERTY_DEFINITION('PD_PERSON_ORG','',#31);
                #75=PROPERTY_DEFINITION_REPRESENTATION(#74,#10);
                #76=PROPERTY_DEFINITION('PD_APPROVAL_ROLE','',#28);
                #77=PROPERTY_DEFINITION_REPRESENTATION(#76,#10);
                #78=PROPERTY_DEFINITION('PD_PERSON_ORG_ROLE','',#40);
                #79=PROPERTY_DEFINITION_REPRESENTATION(#78,#10);
                #80=PROPERTY_DEFINITION('PD_ORG_ROLE','',#41);
                #81=PROPERTY_DEFINITION_REPRESENTATION(#80,#10);
                #82=PROPERTY_DEFINITION('PD_CLASS_ROLE','',#42);
                #83=PROPERTY_DEFINITION_REPRESENTATION(#82,#10);
                #84=PROPERTY_DEFINITION('PD_DATE_ROLE','',#43);
                #85=PROPERTY_DEFINITION_REPRESENTATION(#84,#10);
                #86=PROPERTY_DEFINITION('PD_DATE_TIME_ROLE','',#44);
                #87=PROPERTY_DEFINITION_REPRESENTATION(#86,#10);
                #88=PROPERTY_DEFINITION('PD_ID_ROLE','',#45);
                #89=PROPERTY_DEFINITION_REPRESENTATION(#88,#10);
                #90=PROPERTY_DEFINITION('PD_DOC_TYPE','',#46);
                #91=PROPERTY_DEFINITION_REPRESENTATION(#90,#10);
                #92=PROPERTY_DEFINITION('PD_LANGUAGE','',#47);
                #93=PROPERTY_DEFINITION_REPRESENTATION(#92,#10);
                #100=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #101=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #102=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #103=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #104=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #105=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #106=CARTESIAN_POINT('P6',(6.0,0.0,0.0));
                #107=CARTESIAN_POINT('P7',(7.0,0.0,0.0));
                #108=CARTESIAN_POINT('P8',(8.0,0.0,0.0));
                #109=CARTESIAN_POINT('P9',(9.0,0.0,0.0));
                #110=CARTESIAN_POINT('P10',(10.0,0.0,0.0));
                #111=CARTESIAN_POINT('P11',(11.0,0.0,0.0));
                #112=CARTESIAN_POINT('P12',(12.0,0.0,0.0));
                #113=CARTESIAN_POINT('P13',(13.0,0.0,0.0));
                #114=CARTESIAN_POINT('P14',(14.0,0.0,0.0));
                #115=CARTESIAN_POINT('P15',(15.0,0.0,0.0));
                #116=CARTESIAN_POINT('P16',(16.0,0.0,0.0));
                #117=ANNOTATION_TEXT_OCCURRENCE('NOTE_CAL_DATE','',#100);
                #118=ANNOTATION_TEXT_OCCURRENCE('NOTE_ZONE','',#101);
                #119=ANNOTATION_TEXT_OCCURRENCE('NOTE_LOCAL_TIME','',#102);
                #120=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_TIME','',#103);
                #121=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL_STATUS','',#104);
                #122=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL','',#105);
                #123=ANNOTATION_TEXT_OCCURRENCE('NOTE_SECURITY_LEVEL','',#106);
                #124=ANNOTATION_TEXT_OCCURRENCE('NOTE_SECURITY','',#107);
                #125=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONTRACT_TYPE','',#108);
                #126=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONTRACT','',#109);
                #127=ANNOTATION_TEXT_OCCURRENCE('NOTE_CERT_TYPE','',#110);
                #128=ANNOTATION_TEXT_OCCURRENCE('NOTE_CERT','',#111);
                #129=ANNOTATION_TEXT_OCCURRENCE('NOTE_PERSON','',#112);
                #130=ANNOTATION_TEXT_OCCURRENCE('NOTE_PERSON_ORG','',#113);
                #131=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL_ROLE','',#114);
                #132=ANNOTATION_TEXT_OCCURRENCE('NOTE_PERSON_ORG_ROLE','',#115);
                #133=ANNOTATION_TEXT_OCCURRENCE('NOTE_ORG_ROLE','',#116);
                #134=CARTESIAN_POINT('P17',(17.0,0.0,0.0));
                #135=CARTESIAN_POINT('P18',(18.0,0.0,0.0));
                #136=CARTESIAN_POINT('P19',(19.0,0.0,0.0));
                #137=CARTESIAN_POINT('P20',(20.0,0.0,0.0));
                #138=CARTESIAN_POINT('P21',(21.0,0.0,0.0));
                #139=ANNOTATION_TEXT_OCCURRENCE('NOTE_CLASS_ROLE','',#134);
                #140=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_ROLE','',#135);
                #141=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_TIME_ROLE','',#136);
                #142=ANNOTATION_TEXT_OCCURRENCE('NOTE_ID_ROLE','',#137);
                #143=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOC_TYPE','',#138);
                #144=ANNOTATION_TEXT_OCCURRENCE('NOTE_LANGUAGE','',#139);
                #145=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#22,#10,#117,#8);
                #146=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#23,#10,#118,#8);
                #147=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#24,#10,#119,#8);
                #148=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#25,#10,#120,#8);
                #149=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#26,#10,#121,#8);
                #150=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#27,#10,#122,#8);
                #151=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#34,#10,#123,#8);
                #152=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#35,#10,#124,#8);
                #153=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#36,#10,#125,#8);
                #154=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#37,#10,#126,#8);
                #155=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#38,#10,#127,#8);
                #156=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#39,#10,#128,#8);
                #157=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#29,#10,#129,#8);
                #158=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#31,#10,#130,#8);
                #159=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#28,#10,#131,#8);
                #160=PMI_REQUIREMENT_ITEM_ASSOCIATION('A15','',#40,#10,#132,#8);
                #161=PMI_REQUIREMENT_ITEM_ASSOCIATION('A16','',#41,#10,#133,#8);
                #162=PMI_REQUIREMENT_ITEM_ASSOCIATION('A17','',#42,#10,#139,#8);
                #163=PMI_REQUIREMENT_ITEM_ASSOCIATION('A18','',#43,#10,#140,#8);
                #164=PMI_REQUIREMENT_ITEM_ASSOCIATION('A19','',#44,#10,#141,#8);
                #165=PMI_REQUIREMENT_ITEM_ASSOCIATION('A20','',#45,#10,#142,#8);
                #166=PMI_REQUIREMENT_ITEM_ASSOCIATION('A21','',#46,#10,#143,#8);
                #167=PMI_REQUIREMENT_ITEM_ASSOCIATION('A22','',#47,#10,#144,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_DIRECT_META\"",
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
    void shouldBindPmiTargetsThroughDirectMetadataWrapperLinkDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_META_WRAP',(),#9);
                #11=REPRESENTATION('REP_AUX',(),#9);
                #12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);
                #13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #15=DIRECTION('DZ',(0.0,0.0,1.0));
                #16=DIRECTION('DX',(1.0,0.0,0.0));
                #17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);
                #18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);
                #19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);
                #20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));
                #21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);
                #22=NAME_ATTRIBUTE('N0',#8);
                #23=DESCRIPTION_ATTRIBUTE('D0',#8);
                #24=ID_ATTRIBUTE('I0',#8);
                #25=NAME_ASSIGNMENT('NAME_META');
                #26=IDENTIFICATION_ROLE('role');
                #27=IDENTIFICATION_ASSIGNMENT('ID_META',#26);
                #28=EXTERNAL_SOURCE('SRC');
                #29=EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT_META',#26,#28);
                #30=APPLIED_NAME_ASSIGNMENT('APPLIED_NAME',(#8));
                #31=APPLIED_IDENTIFICATION_ASSIGNMENT('APPLIED_ID',#26,(#8));
                #32=APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT('APPLIED_EXT',#26,#28,(#8));
                #33=EXTERNAL_SOURCE('SRC_LINK');
                #34=EXTERNAL_SOURCE_RELATIONSHIP('SR','',#28,#33);
                #35=EXTERNALLY_DEFINED_ITEM('EXT-LINK',#33);
                #36=PROPERTY_DEFINITION('PD_NAME_ATTR','',#22);
                #37=PROPERTY_DEFINITION_REPRESENTATION(#36,#10);
                #38=PROPERTY_DEFINITION('PD_DESC_ATTR','',#23);
                #39=PROPERTY_DEFINITION_REPRESENTATION(#38,#10);
                #40=PROPERTY_DEFINITION('PD_ID_ATTR','',#24);
                #41=PROPERTY_DEFINITION_REPRESENTATION(#40,#10);
                #42=PROPERTY_DEFINITION('PD_NAME_ASSIGN','',#25);
                #43=PROPERTY_DEFINITION_REPRESENTATION(#42,#10);
                #44=PROPERTY_DEFINITION('PD_ID_ASSIGN','',#27);
                #45=PROPERTY_DEFINITION_REPRESENTATION(#44,#10);
                #46=PROPERTY_DEFINITION('PD_EXT_ASSIGN','',#29);
                #47=PROPERTY_DEFINITION_REPRESENTATION(#46,#10);
                #48=PROPERTY_DEFINITION('PD_APPLIED_NAME','',#30);
                #49=PROPERTY_DEFINITION_REPRESENTATION(#48,#10);
                #50=PROPERTY_DEFINITION('PD_APPLIED_ID','',#31);
                #51=PROPERTY_DEFINITION_REPRESENTATION(#50,#10);
                #52=PROPERTY_DEFINITION('PD_APPLIED_EXT','',#32);
                #53=PROPERTY_DEFINITION_REPRESENTATION(#52,#10);
                #60=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #61=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #62=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #63=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #64=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #65=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #66=CARTESIAN_POINT('P6',(6.0,0.0,0.0));
                #67=CARTESIAN_POINT('P7',(7.0,0.0,0.0));
                #68=CARTESIAN_POINT('P8',(8.0,0.0,0.0));
                #69=ANNOTATION_TEXT_OCCURRENCE('NOTE_NAME_ATTR','',#60);
                #70=ANNOTATION_TEXT_OCCURRENCE('NOTE_DESC_ATTR','',#61);
                #71=ANNOTATION_TEXT_OCCURRENCE('NOTE_ID_ATTR','',#62);
                #72=ANNOTATION_TEXT_OCCURRENCE('NOTE_NAME_ASSIGN','',#63);
                #73=ANNOTATION_TEXT_OCCURRENCE('NOTE_ID_ASSIGN','',#64);
                #74=ANNOTATION_TEXT_OCCURRENCE('NOTE_EXT_ASSIGN','',#65);
                #75=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPLIED_NAME','',#66);
                #76=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPLIED_ID','',#67);
                #77=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPLIED_EXT','',#68);
                #78=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#22,#10,#69,#8);
                #79=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#23,#10,#70,#8);
                #80=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#24,#10,#71,#8);
                #81=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#25,#10,#72,#8);
                #82=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#27,#10,#73,#8);
                #83=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#29,#10,#74,#8);
                #84=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#30,#10,#75,#8);
                #85=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#31,#10,#76,#8);
                #86=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#32,#10,#77,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_NAME_ATTR\"",
                "\"name\":\"NOTE_DESC_ATTR\"",
                "\"name\":\"NOTE_ID_ATTR\"",
                "\"name\":\"NOTE_NAME_ASSIGN\"",
                "\"name\":\"NOTE_ID_ASSIGN\"",
                "\"name\":\"NOTE_EXT_ASSIGN\"",
                "\"name\":\"NOTE_APPLIED_NAME\"",
                "\"name\":\"NOTE_APPLIED_ID\"",
                "\"name\":\"NOTE_APPLIED_EXT\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_META_WRAP\"",
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
    void shouldBindPmiTargetsThroughDirectDocumentGroupAndExternalSourceLinkDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_DOC_META',(),#9);
                #11=REPRESENTATION('REP_AUX',(),#9);
                #12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);
                #13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #15=DIRECTION('DZ',(0.0,0.0,1.0));
                #16=DIRECTION('DX',(1.0,0.0,0.0));
                #17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);
                #18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);
                #19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);
                #20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));
                #21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);
                #22=GROUP('G1','group');
                #23=DOCUMENT_TYPE('spec');
                #24=DOCUMENT('DOC-1','Spec A','',#23);
                #25=DOCUMENT_REFERENCE(#24,'internal');
                #26=APPLIED_DOCUMENT_REFERENCE(#24,'applied',(#8));
                #27=EXTERNAL_SOURCE('SRC');
                #28=EXTERNALLY_DEFINED_ITEM('EXT-1',#27);
                #29=EXTERNAL_SOURCE('SRC_LINK');
                #30=EXTERNAL_SOURCE_RELATIONSHIP('SR','',#27,#29);
                #31=DOCUMENT_USAGE_CONSTRAINT(#24,'SECTION','7.1');
                #32=PROPERTY_DEFINITION('PD_GROUP','',#22);
                #33=PROPERTY_DEFINITION_REPRESENTATION(#32,#10);
                #34=PROPERTY_DEFINITION('PD_DOCUMENT','',#24);
                #35=PROPERTY_DEFINITION_REPRESENTATION(#34,#10);
                #36=PROPERTY_DEFINITION('PD_DOC_REF','',#25);
                #37=PROPERTY_DEFINITION_REPRESENTATION(#36,#10);
                #38=PROPERTY_DEFINITION('PD_APPLIED_DOC_REF','',#26);
                #39=PROPERTY_DEFINITION_REPRESENTATION(#38,#10);
                #40=PROPERTY_DEFINITION('PD_EXT_SOURCE','',#27);
                #41=PROPERTY_DEFINITION_REPRESENTATION(#40,#10);
                #42=PROPERTY_DEFINITION('PD_EXT_ITEM','',#28);
                #43=PROPERTY_DEFINITION_REPRESENTATION(#42,#10);
                #44=PROPERTY_DEFINITION('PD_DOC_USAGE','',#31);
                #45=PROPERTY_DEFINITION_REPRESENTATION(#44,#10);
                #50=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #51=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #52=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #53=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #54=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #55=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #56=CARTESIAN_POINT('P6',(6.0,0.0,0.0));
                #57=ANNOTATION_TEXT_OCCURRENCE('NOTE_GROUP','',#50);
                #58=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOCUMENT','',#51);
                #59=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOC_REF','',#52);
                #60=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPLIED_DOC_REF','',#53);
                #61=ANNOTATION_TEXT_OCCURRENCE('NOTE_EXT_SOURCE','',#54);
                #62=ANNOTATION_TEXT_OCCURRENCE('NOTE_EXT_ITEM','',#55);
                #63=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOC_USAGE','',#56);
                #64=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#22,#10,#57,#8);
                #65=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#24,#10,#58,#8);
                #66=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#25,#10,#59,#8);
                #67=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#26,#10,#60,#8);
                #68=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#27,#10,#61,#8);
                #69=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#28,#10,#62,#8);
                #70=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#31,#10,#63,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_GROUP\"",
                "\"name\":\"NOTE_DOCUMENT\"",
                "\"name\":\"NOTE_DOC_REF\"",
                "\"name\":\"NOTE_APPLIED_DOC_REF\"",
                "\"name\":\"NOTE_EXT_SOURCE\"",
                "\"name\":\"NOTE_EXT_ITEM\"",
                "\"name\":\"NOTE_DOC_USAGE\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_DOC_META\"",
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
    void shouldBindPmiTargetsThroughDirectAssignmentCarrierLinkDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_ASSIGN',(),#9);
                #11=REPRESENTATION('REP_AUX',(),#9);
                #12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);
                #13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #15=DIRECTION('DZ',(0.0,0.0,1.0));
                #16=DIRECTION('DX',(1.0,0.0,0.0));
                #17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);
                #18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);
                #19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);
                #20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));
                #21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);
                #22=APPROVAL_STATUS('approved');
                #23=APPROVAL(#22,'design');
                #24=APPROVAL_ASSIGNMENT(#23);
                #25=SECURITY_CLASSIFICATION_LEVEL('controlled');
                #26=SECURITY_CLASSIFICATION('sec','purpose',#25);
                #27=SECURITY_CLASSIFICATION_ASSIGNMENT(#26);
                #28=DATE_ROLE('release');
                #29=CALENDAR_DATE(2026,11,4);
                #30=DATE_ASSIGNMENT(#29,#28);
                #31=LANGUAGE('en-US');
                #32=LANGUAGE_ASSIGNMENT(#31);
                #33=GROUP('G1','group');
                #34=GROUP_ASSIGNMENT(#33);
                #35=PERSON('p-1','Doe','Jane',$,$,$);
                #36=ORGANIZATION('org-1','Acme','engineering');
                #37=PERSON_AND_ORGANIZATION(#35,#36);
                #38=PERSON_AND_ORGANIZATION_ROLE('creator');
                #39=APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT(#37,#38,(#8));
                #40=PROPERTY_DEFINITION('PD_APPROVAL_ASSIGN','',#24);
                #41=PROPERTY_DEFINITION_REPRESENTATION(#40,#10);
                #42=PROPERTY_DEFINITION('PD_SECURITY_ASSIGN','',#27);
                #43=PROPERTY_DEFINITION_REPRESENTATION(#42,#10);
                #44=PROPERTY_DEFINITION('PD_DATE_ASSIGN','',#30);
                #45=PROPERTY_DEFINITION_REPRESENTATION(#44,#10);
                #46=PROPERTY_DEFINITION('PD_LANG_ASSIGN','',#32);
                #47=PROPERTY_DEFINITION_REPRESENTATION(#46,#10);
                #48=PROPERTY_DEFINITION('PD_GROUP_ASSIGN','',#34);
                #49=PROPERTY_DEFINITION_REPRESENTATION(#48,#10);
                #50=PROPERTY_DEFINITION('PD_APPLIED_PERSON_ORG_ASSIGN','',#39);
                #51=PROPERTY_DEFINITION_REPRESENTATION(#50,#10);
                #60=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #61=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #62=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #63=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #64=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #65=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #66=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL_ASSIGN','',#60);
                #67=ANNOTATION_TEXT_OCCURRENCE('NOTE_SECURITY_ASSIGN','',#61);
                #68=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_ASSIGN','',#62);
                #69=ANNOTATION_TEXT_OCCURRENCE('NOTE_LANG_ASSIGN','',#63);
                #70=ANNOTATION_TEXT_OCCURRENCE('NOTE_GROUP_ASSIGN','',#64);
                #71=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPLIED_PERSON_ORG_ASSIGN','',#65);
                #72=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#24,#10,#66,#8);
                #73=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#27,#10,#67,#8);
                #74=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#30,#10,#68,#8);
                #75=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#32,#10,#69,#8);
                #76=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#34,#10,#70,#8);
                #77=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#39,#10,#71,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_APPROVAL_ASSIGN\"",
                "\"name\":\"NOTE_SECURITY_ASSIGN\"",
                "\"name\":\"NOTE_DATE_ASSIGN\"",
                "\"name\":\"NOTE_LANG_ASSIGN\"",
                "\"name\":\"NOTE_GROUP_ASSIGN\"",
                "\"name\":\"NOTE_APPLIED_PERSON_ORG_ASSIGN\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_ASSIGN\"",
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
    void shouldBindPmiTargetsThroughContextTimeAndVariableEntities() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=APPLICATION_PROTOCOL_DEFINITION('draft','AP203',2026,#1);
                #3=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #4=PRODUCT('PRT','Part','Part',(#3));
                #5=PRODUCT_DEFINITION_FORMATION('v1','',#4);
                #6=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #7=PRODUCT_DEFINITION('pd','part def',#5,#6);
                #8=PRODUCT_DEFINITION_SHAPE('pds','shape',#7);
                #9=SHAPE_ASPECT('SA0','base',#8,.T.);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #11=REPRESENTATION('REP_META_CTX',(),#10);
                #82=REPRESENTATION('REP_META_AUX0',(),#10);
                #83=REPRESENTATION('REP_META_AUX1',(),#10);
                #84=REPRESENTATION_RELATIONSHIP('RR_VAR','',#11,#82);
                #85=CARTESIAN_POINT('TX0',(20.0,0.0,0.0));
                #86=CARTESIAN_POINT('TX1',(21.0,0.0,0.0));
                #87=DIRECTION('DZV',(0.0,0.0,1.0));
                #88=DIRECTION('DXV',(1.0,0.0,0.0));
                #89=AXIS2_PLACEMENT_3D('AXV0',#85,#87,#88);
                #90=AXIS2_PLACEMENT_3D('AXV1',#86,#87,#88);
                #91=ITEM_DEFINED_TRANSFORMATION('TV','',#89,#90);
                #92=(REPRESENTATION_RELATIONSHIP('RRT_VAR','',#11,#83)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#91));
                #93=SHAPE_REPRESENTATION_RELATIONSHIP('SRR_VAR','',#11,#82);
                #12=SHAPE_DEFINITION_REPRESENTATION(#8,#11);
                #13=CHARACTERIZED_OBJECT('CO','characterized object');
                #14=PROPERTY_DEFINITION('PD_CO','',#13);
                #15=ABSTRACT_VARIABLE(#14,#11);
                #16=ROW_VARIABLE(#14,#11);
                #17=SCALAR_VARIABLE(#14,#11);
                #18=FORWARD_CHAINING_RULE_PREMISE(#14,#11);
                #19=BACK_CHAINING_RULE_BODY(#14,#11);
                #20=CALENDAR_DATE(2026,11,4);
                #21=DATE_ROLE('release');
                #22=APPLIED_DATE_ASSIGNMENT(#20,#21,(#9));
                #23=COORDINATED_UNIVERSAL_TIME_OFFSET(8,0,.AHEAD.);
                #24=LOCAL_TIME(9,15,$,#23);
                #25=DATE_AND_TIME(#20,#24);
                #26=DATE_TIME_ROLE('created');
                #27=APPLIED_DATE_AND_TIME_ASSIGNMENT(#25,#26,(#9));
                #28=PERSON('p-1','Doe','Jane',$,$,$);
                #29=ORGANIZATION('org-1','Acme','engineering');
                #30=PERSON_AND_ORGANIZATION(#28,#29);
                #31=PERSON_AND_ORGANIZATION_ROLE('creator');
                #32=APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT(#30,#31,(#9));
                #40=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #41=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #42=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #43=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #44=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #45=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #46=CARTESIAN_POINT('P6',(6.0,0.0,0.0));
                #47=CARTESIAN_POINT('P7',(7.0,0.0,0.0));
                #48=CARTESIAN_POINT('P8',(8.0,0.0,0.0));
                #49=CARTESIAN_POINT('P9',(9.0,0.0,0.0));
                #50=CARTESIAN_POINT('P10',(10.0,0.0,0.0));
                #51=CARTESIAN_POINT('P11',(11.0,0.0,0.0));
                #52=CARTESIAN_POINT('P12',(12.0,0.0,0.0));
                #53=ANNOTATION_TEXT_OCCURRENCE('NOTE_APP_PROTOCOL','',#40);
                #54=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRODUCT_CONTEXT','',#41);
                #55=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRODUCT_DEF_CONTEXT','',#42);
                #56=ANNOTATION_TEXT_OCCURRENCE('NOTE_CHARACTERIZED_OBJECT','',#43);
                #57=ANNOTATION_TEXT_OCCURRENCE('NOTE_ABSTRACT_VARIABLE','',#44);
                #58=ANNOTATION_TEXT_OCCURRENCE('NOTE_ROW_VARIABLE','',#45);
                #59=ANNOTATION_TEXT_OCCURRENCE('NOTE_SCALAR_VARIABLE','',#46);
                #60=ANNOTATION_TEXT_OCCURRENCE('NOTE_FORWARD_RULE','',#47);
                #61=ANNOTATION_TEXT_OCCURRENCE('NOTE_BACK_RULE','',#48);
                #62=ANNOTATION_TEXT_OCCURRENCE('NOTE_CALENDAR_DATE','',#49);
                #63=ANNOTATION_TEXT_OCCURRENCE('NOTE_DATE_AND_TIME','',#50);
                #64=ANNOTATION_TEXT_OCCURRENCE('NOTE_LOCAL_TIME','',#51);
                #65=ANNOTATION_TEXT_OCCURRENCE('NOTE_UTC_OFFSET','',#52);
                #66=CARTESIAN_POINT('P13',(13.0,0.0,0.0));
                #67=ANNOTATION_TEXT_OCCURRENCE('NOTE_PERSON','',#66);
                #68=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#2,#11,#53,#9);
                #69=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#3,#11,#54,#9);
                #70=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#6,#11,#55,#9);
                #71=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#13,#11,#56,#9);
                #72=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#15,#11,#57,#9);
                #73=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#16,#11,#58,#9);
                #74=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#17,#11,#59,#9);
                #75=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#18,#11,#60,#9);
                #76=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#19,#11,#61,#9);
                #77=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#20,#11,#62,#9);
                #78=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#25,#11,#63,#9);
                #79=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#24,#11,#64,#9);
                #80=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#23,#11,#65,#9);
                #81=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#28,#11,#67,#9);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_APP_PROTOCOL\"",
                "\"name\":\"NOTE_PRODUCT_CONTEXT\"",
                "\"name\":\"NOTE_PRODUCT_DEF_CONTEXT\"",
                "\"name\":\"NOTE_CHARACTERIZED_OBJECT\"",
                "\"name\":\"NOTE_ABSTRACT_VARIABLE\"",
                "\"name\":\"NOTE_ROW_VARIABLE\"",
                "\"name\":\"NOTE_SCALAR_VARIABLE\"",
                "\"name\":\"NOTE_FORWARD_RULE\"",
                "\"name\":\"NOTE_BACK_RULE\"",
                "\"name\":\"NOTE_CALENDAR_DATE\"",
                "\"name\":\"NOTE_DATE_AND_TIME\"",
                "\"name\":\"NOTE_LOCAL_TIME\"",
                "\"name\":\"NOTE_UTC_OFFSET\"",
                "\"name\":\"NOTE_PERSON\"",
                "\"id\":11,\"type\":\"representation\",\"name\":\"REP_META_CTX\"",
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
    void shouldBindPmiTargetsThroughApprovalPersonAndDateLeaves() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_APPROVAL_CHAIN',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=APPROVAL_STATUS('released');
                #13=APPROVAL(#12,'design');
                #14=PERSON('p-1','Doe','Jane',$,$,$);
                #15=ORGANIZATION('org-1','Acme','engineering');
                #16=PERSON_AND_ORGANIZATION(#14,#15);
                #17=APPROVAL_ROLE('authorizer');
                #18=APPROVAL_PERSON_ORGANIZATION(#16,#13,#17);
                #19=CALENDAR_DATE(2026,11,4);
                #20=COORDINATED_UNIVERSAL_TIME_OFFSET(8,0,.AHEAD.);
                #21=LOCAL_TIME(9,15,$,#20);
                #22=DATE_AND_TIME(#19,#21);
                #23=APPROVAL_DATE_TIME(#22,#13);
                #24=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #25=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #26=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL_PERSON','',#24);
                #27=ANNOTATION_TEXT_OCCURRENCE('NOTE_APPROVAL_DATE','',#25);
                #28=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#18,#10,#26,#8);
                #29=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#23,#10,#27,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_APPROVAL_PERSON\"",
                "\"name\":\"NOTE_APPROVAL_DATE\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_APPROVAL_CHAIN\"",
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
    void shouldBindPmiTargetsThroughRepresentationRelationshipDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_USAGE',(),#9);
                #11=REPRESENTATION('REP_REL_A',(),#9);
                #12=REPRESENTATION('REP_REL_B',(),#9);
                #13=REPRESENTATION('REP_REL_C',(),#9);
                #14=REPRESENTATION('REP_REL_D',(),#9);
                #15=REPRESENTATION('REP_REL_E',(),#9);
                #16=REPRESENTATION('REP_REL_F',(),#9);
                #17=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #18=REPRESENTATION_RELATIONSHIP('RR','',#11,#12);
                #19=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #20=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #21=DIRECTION('DZ',(0.0,0.0,1.0));
                #22=DIRECTION('DX',(1.0,0.0,0.0));
                #23=AXIS2_PLACEMENT_3D('AX0',#19,#21,#22);
                #24=AXIS2_PLACEMENT_3D('AX1',#20,#21,#22);
                #25=ITEM_DEFINED_TRANSFORMATION('T1','',#23,#24);
                #26=(REPRESENTATION_RELATIONSHIP('RRT','',#13,#14)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#25));
                #27=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#15,#16);
                #30=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #31=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #32=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #33=ANNOTATION_TEXT_OCCURRENCE('NOTE_REP_REL','',#30);
                #34=ANNOTATION_TEXT_OCCURRENCE('NOTE_REP_REL_XFORM','',#31);
                #35=ANNOTATION_TEXT_OCCURRENCE('NOTE_SHAPE_REP_REL','',#32);
                #36=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#18,#10,#33,#8);
                #37=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#26,#10,#34,#8);
                #38=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#27,#10,#35,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_REP_REL\"",
                "\"name\":\"NOTE_REP_REL_XFORM\"",
                "\"name\":\"NOTE_SHAPE_REP_REL\"",
                "\"name\":\"REP_REL_A\"",
                "\"name\":\"REP_REL_B\"",
                "\"name\":\"REP_REL_C\"",
                "\"name\":\"REP_REL_D\"",
                "\"name\":\"REP_REL_E\"",
                "\"name\":\"REP_REL_F\"",
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
    void shouldBindPmiTargetsThroughNestedUsageDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_BASE',(),#9);
                #11=REPRESENTATION('REP_USAGE_A',(),#9);
                #12=REPRESENTATION('REP_USAGE_B',(),#9);
                #13=REPRESENTATION('REP_USAGE_C',(),#9);
                #14=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #15=ITEM_IDENTIFIED_REPRESENTATION_USAGE('IU','',#8,#11,#8);
                #16=CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE('CIU','',#15,(#11,#12),(#17),#8);
                #17=REPRESENTATION_RELATIONSHIP('RR','',#11,#12);
                #18=ANNOTATION_TEXT_OCCURRENCE('NOTE_USAGE_A','',#40);
                #19=DRAUGHTING_CALLOUT('CALLOUT0',(#18));
                #20=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU','',#18,#11);
                #21=CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE('CGU','',#19,(#12,#13),(#22),#12);
                #22=REPRESENTATION_RELATIONSHIP('RR2','',#12,#13);
                #23=DRAUGHTING_MODEL_ITEM_ASSOCIATION('DMA','',#16,#12,#19);
                #24=PMI_REQUIREMENT_ITEM_ASSOCIATION('PMI0','',#21,#10,#31,#8);
                #25=MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION('MDR0','',#23,#10,#32,#8);
                #26=PLACED_TARGET('PT0','',#24,#13,#8);
                #30=GEOMETRIC_SET('GS',());
                #31=ANNOTATION_TEXT_OCCURRENCE('NOTE_PMI_NESTED','',#40);
                #32=ANNOTATION_TEXT_OCCURRENCE('NOTE_MDR_NESTED','',#41);
                #33=ANNOTATION_TEXT_OCCURRENCE('NOTE_PLACED_TARGET_NESTED','',#42);
                #34=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#26,#10,#33,#8);
                #40=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #41=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #42=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_PMI_NESTED\"",
                "\"name\":\"NOTE_MDR_NESTED\"",
                "\"name\":\"NOTE_PLACED_TARGET_NESTED\"",
                "\"name\":\"CALLOUT0\"",
                "\"name\":\"REP_USAGE_A\"",
                "\"name\":\"REP_USAGE_B\"",
                "\"name\":\"REP_USAGE_C\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_BASE\"",
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
    void shouldBindPmiTargetsThroughExtendedAnnotationUsageFamily() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #3=DRAUGHTING_MODEL('DM',(#1),#2);
                #4=PROPERTY_DEFINITION('PD','',#1);
                #5=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #6=CARTESIAN_POINT('P1',(1.0,1.0,0.0));
                #7=ANNOTATION_POINT_OCCURRENCE('APO',(),#5);
                #8=GEOMETRIC_SET('PHSET',(#6));
                #9=ANNOTATION_PLACEHOLDER_OCCURRENCE('PH',(),#8,.TITLE.,1.0);
                #10=ANNOTATION_SYMBOL_OCCURRENCE('ASO',(),#9);
                #11=REPRESENTATION('REP_A',(),#2);
                #12=REPRESENTATION('REP_B',(),#2);
                #13=REPRESENTATION('REP_C',(),#2);
                #14=REPRESENTATION_RELATIONSHIP('RR','chain',#12,#13);
                #15=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU','',#7,#11);
                #16=CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE('CGU','',#10,(#12,#13),(#14),#11);
                #17=DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER('DMIAP','assocph',#4,#3,#7,#9);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"pmi\":[",
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
    void shouldBindPmiTargetsThroughDirectAnnotationContentUsageFamily() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','ANN'));
                #2=REPRESENTATION('REP_USED',(),#1);
                #3=REPRESENTATION('REP_A',(),#1);
                #4=REPRESENTATION('REP_B',(),#1);
                #5=REPRESENTATION_RELATIONSHIP('RR','chain',#3,#4);
                #34=REPRESENTATION_RELATIONSHIP('RR_USED','used chain',#2,#3);
                #35=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #36=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #37=DIRECTION('DZ3',(0.0,0.0,1.0));
                #38=DIRECTION('DX3',(1.0,0.0,0.0));
                #39=AXIS2_PLACEMENT_3D('AX0',#35,#37,#38);
                #40=AXIS2_PLACEMENT_3D('AX1',#36,#37,#38);
                #41=ITEM_DEFINED_TRANSFORMATION('T1','',#39,#40);
                #42=(REPRESENTATION_RELATIONSHIP('RRT_USED','',#2,#4)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#41));
                #43=SHAPE_REPRESENTATION_RELATIONSHIP('SRR_USED','',#2,#3);
                #6=PROPERTY_DEFINITION('PD','',#2);
                #7=CARTESIAN_POINT('O',(0.0,0.0));
                #8=DIRECTION('X',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('MAP',#7,#8);
                #10=REPRESENTATION('SYMREP',(),#1);
                #11=SYMBOL_REPRESENTATION_MAP(#9,#10);
                #12=CARTESIAN_POINT('P0',(10.0,20.0));
                #13=AXIS2_PLACEMENT_2D('TGT0',#12,#8);
                #14=ANNOTATION_SYMBOL('AS0',#11,#13);
                #15=REPRESENTATION_MAP(#9,#10);
                #16=CARTESIAN_POINT('P1',(30.0,40.0));
                #17=AXIS2_PLACEMENT_2D('TGT1',#16,#8);
                #18=ANNOTATION_TEXT('AT0',#15,#17);
                #19=ANNOTATION_TEXT_CHARACTER('ATC0',#15,#17);
                #20=CARTESIAN_POINT('F0',(0.0,0.0,0.0));
                #21=CARTESIAN_POINT('F1',(1.0,0.0,0.0));
                #22=CARTESIAN_POINT('F2',(1.0,1.0,0.0));
                #23=POLYLINE('PL0',(#20,#21,#22));
                #24=(ANNOTATION_FILL_AREA('FA0',(#23))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                #25=ANNOTATION_TEXT_OCCURRENCE('NOTE','',#20);
                #26=GEOMETRIC_ITEM_SPECIFIC_USAGE('G0','',#25,#14);
                #27=GEOMETRIC_ITEM_SPECIFIC_USAGE('G1','',#25,#18);
                #28=GEOMETRIC_ITEM_SPECIFIC_USAGE('G2','',#25,#19);
                #29=GEOMETRIC_ITEM_SPECIFIC_USAGE('G3','',#25,#24);
                #30=ITEM_IDENTIFIED_REPRESENTATION_USAGE('IU','',#6,#2,#14);
                #31=CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE('CIU','',#6,(#3,#4),(#5),#18);
                #32=DRAUGHTING_MODEL_ITEM_ASSOCIATION('DMA','',#6,#2,#19);
                #33=PMI_REQUIREMENT_ITEM_ASSOCIATION('PMI','',#6,#2,#24,#6);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughPathAndWireUsageTargets() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=VECTOR('V0',#3,1.0);
                #5=LINE('L0',#1,#4);
                #6=VERTEX_POINT('VP0',#1);
                #7=VERTEX_POINT('VP1',#2);
                #8=EDGE_CURVE('E0',#6,#7,#5,.T.);
                #9=ORIENTED_EDGE('OE0',$,$,#8,.T.);
                #10=OPEN_PATH('OP',(#9));
                #11=CONNECTED_EDGE_SET('CES',(#9));
                #12=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #13=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #14=REPRESENTATION('REP_A',(),#13);
                #15=REPRESENTATION('REP_B',(),#13);
                #16=REPRESENTATION_RELATIONSHIP('RR','chain',#14,#15);
                #17=ANNOTATION_TEXT_OCCURRENCE('NOTE','',#1);
                #18=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU','',#17,#10);
                #19=CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE('CGU','',#17,(#14,#15),(#16),#11);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE\"",
                "\"id\":10,\"type\":\"path\",\"name\":\"OP\"",
                "\"id\":11,\"type\":\"edge_set\",\"name\":\"CES\"");
    }

    @Test
    void shouldBindPmiTargetsThroughShellModelAndSolidUsageTargets() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX',#1,#5,#6);
                #8=PLANE('PL0',#7);
                #13=POLY_LOOP('LOOP',(#1,#2,#3,#4));
                #14=FACE_OUTER_BOUND('FOB',#13,.T.);
                #15=ADVANCED_FACE('FACE0',(#14),#8,.T.);
                #16=OPEN_SHELL('OSH',(#15));
                #17=FACE_BASED_SURFACE_MODEL('FBM',(#16));
                #18=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #19=REPRESENTATION('REP_A',(),#18);
                #20=REPRESENTATION('REP_B',(),#18);
                #21=REPRESENTATION_RELATIONSHIP('RR','chain',#19,#20);
                #22=ANNOTATION_TEXT_OCCURRENCE('NOTE_SHELL','',#1);
                #23=ANNOTATION_TEXT_OCCURRENCE('NOTE_MODEL','',#2);
                #24=ANNOTATION_TEXT_OCCURRENCE('NOTE_SOLID','',#3);
                #25=BLOCK('BLK',#7,1.0,1.0,1.0);
                #26=POINT_SET('PS',(#1,#2));
                #27=GEOMETRIC_CURVE_SET('GCS',(#26));
                #28=GEOMETRIC_SET('GS',(#27));
                #29=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU_SHELL','',#22,#16);
                #30=CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE('CGU_MODEL','',#23,(#19,#20),(#21),#17);
                #31=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU_SOLID','',#24,#25);
                #32=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU_SET','',#24,#28);
                #33=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#29,#19,#22,#22);
                #34=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#30,#19,#23,#23);
                #35=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#31,#19,#24,#24);
                #36=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#32,#19,#24,#24);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughVertexLoopUsageTargets() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #8=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #9=VERTEX_POINT('VP0',#8);
                #10=VERTEX_LOOP('VLOOP',#9);
                #11=ANNOTATION_TEXT_OCCURRENCE('NOTE','',#8);
                #12=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #13=REPRESENTATION('REP_A',(),#12);
                #14=REPRESENTATION('REP_B',(),#12);
                #15=REPRESENTATION_RELATIONSHIP('RR','chain',#13,#14);
                #16=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU','',#11,#10);
                #17=CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE('CGU','',#11,(#13,#14),(#15),#10);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE\"",
                "\"id\":10,\"type\":\"loop\",\"name\":\"VLOOP\"");
    }

    @Test
    void shouldBindPmiTargetsThroughAnnotationAndMapDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_BASE',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=CARTESIAN_POINT('M0',(0.0,0.0));
                #13=DIRECTION('DX0',(1.0,0.0));
                #14=AXIS2_PLACEMENT_2D('MAP0',#12,#13);
                #15=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYM'));
                #16=REPRESENTATION('REP_SYM',(),#15);
                #17=SYMBOL_REPRESENTATION_MAP(#14,#16);
                #18=CARTESIAN_POINT('T0',(3.0,4.0));
                #19=AXIS2_PLACEMENT_2D('TGT0',#18,#13);
                #20=ANNOTATION_SYMBOL('AS0',#17,#19);
                #21=PRESENTATION_STYLE_ASSIGNMENT(());
                #22=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#21),#20);
                #23=CARTESIAN_POINT('M1',(0.0,0.0));
                #24=AXIS2_PLACEMENT_2D('MAP1',#23,#13);
                #25=REPRESENTATION('REP_TXT',(),#15);
                #26=REPRESENTATION_MAP(#24,#25);
                #27=CARTESIAN_POINT('T1',(6.0,7.0));
                #28=AXIS2_PLACEMENT_2D('TGT1',#27,#13);
                #29=ANNOTATION_TEXT('AT0',#26,#28);
                #30=ANNOTATION_TEXT_CHARACTER('ATC0',#26,#28);
                #31=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #32=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #33=ANNOTATION_TEXT_OCCURRENCE('NOTE_A','base',#31);
                #34=ANNOTATION_TEXT_OCCURRENCE('NOTE_B','child',#32);
                #35=DRAUGHTING_CALLOUT('CALLOUT_A',(#33,#22));
                #36=DRAUGHTING_CALLOUT('CALLOUT_B',(#34,#22));
                #37=DRAUGHTING_CALLOUT_RELATIONSHIP('REL','carry',#35,#36);
                #38=ANNOTATION_OCCURRENCE_RELATIONSHIP('AOR','link',#22,#34);
                #39=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#17,#10,#33,#8);
                #40=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#20,#10,#34,#8);
                #41=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#26,#10,#33,#8);
                #42=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#29,#10,#34,#8);
                #43=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#30,#10,#33,#8);
                #44=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#35,#10,#34,#8);
                #45=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#37,#10,#33,#8);
                #46=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#38,#10,#34,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"REP_SYM\"",
                "\"name\":\"REP_TXT\"",
                "\"name\":\"NOTE_A\"",
                "\"name\":\"NOTE_B\"",
                "\"name\":\"CALLOUT_A\"",
                "\"name\":\"CALLOUT_B\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_BASE\"",
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
    void shouldBindPmiTargetsThroughUserDefinedMapsAndLeafDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_META_LEAF',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #13=VALUE_REPRESENTATION_ITEM('roughness',DESCRIPTIVE_MEASURE('Ra 3.2'));
                #14=ADDRESS('HQ','42','Market St',$,'Shanghai','Shanghai','200000','CN',$,'+86','cad@example.com',$);
                #15=PROPERTY_DEFINITION('PD_DESC','',#12);
                #16=PROPERTY_DEFINITION('PD_VAL','',#13);
                #17=PROPERTY_DEFINITION('PD_ADDR','',#14);
                #18=PROPERTY_DEFINITION_REPRESENTATION(#15,#10);
                #19=PROPERTY_DEFINITION_REPRESENTATION(#16,#10);
                #20=PROPERTY_DEFINITION_REPRESENTATION(#17,#10);
                #21=CARTESIAN_POINT('M0',(0.0,0.0));
                #22=DIRECTION('DX0',(1.0,0.0));
                #23=AXIS2_PLACEMENT_2D('MAP0',#21,#22);
                #24=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYM'));
                #25=REPRESENTATION('REP_UDEF',(),#24);
                #26=REPRESENTATION_MAP(#23,#25);
                #27=CARTESIAN_POINT('T0',(3.0,4.0));
                #28=AXIS2_PLACEMENT_2D('TGT0',#27,#22);
                #29=USER_DEFINED_CURVE_FONT('UCF0',#26,#28);
                #30=USER_DEFINED_MARKER('UDM0',#26,#28);
                #31=USER_DEFINED_TERMINATOR_SYMBOL('UDT0',#26,#28);
                #32=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #33=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #34=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #35=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #36=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #37=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #38=ANNOTATION_TEXT_OCCURRENCE('NOTE_DESC_LEAF','',#32);
                #39=ANNOTATION_TEXT_OCCURRENCE('NOTE_VALUE_LEAF','',#33);
                #40=ANNOTATION_TEXT_OCCURRENCE('NOTE_ADDRESS_LEAF','',#34);
                #41=ANNOTATION_TEXT_OCCURRENCE('NOTE_USER_CURVE_FONT','',#35);
                #42=ANNOTATION_TEXT_OCCURRENCE('NOTE_USER_MARKER','',#36);
                #43=ANNOTATION_TEXT_OCCURRENCE('NOTE_USER_TERMINATOR','',#37);
                #44=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#12,#10,#38,#8);
                #45=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#13,#10,#39,#8);
                #46=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#14,#10,#40,#8);
                #47=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#29,#10,#41,#8);
                #48=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#30,#10,#42,#8);
                #49=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#31,#10,#43,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_DESC_LEAF\"",
                "\"name\":\"NOTE_VALUE_LEAF\"",
                "\"name\":\"NOTE_ADDRESS_LEAF\"",
                "\"name\":\"NOTE_USER_CURVE_FONT\"",
                "\"name\":\"NOTE_USER_MARKER\"",
                "\"name\":\"NOTE_USER_TERMINATOR\"",
                "\"name\":\"REP_UDEF\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_META_LEAF\"",
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
    void shouldBindPmiTargetsThroughPresentationStyleDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_BASE_STYLE',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=REPRESENTATION('REP_STYLE_LEAF',(),#9);
                #13=CARTESIAN_POINT('M0',(0.0,0.0));
                #14=DIRECTION('DX0',(1.0,0.0));
                #15=AXIS2_PLACEMENT_2D('MAP0',#13,#14);
                #16=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYM'));
                #17=REPRESENTATION('REP_STYLE_MAP',(),#16);
                #18=REPRESENTATION_MAP(#15,#17);
                #19=CARTESIAN_POINT('T0',(3.0,4.0));
                #20=AXIS2_PLACEMENT_2D('TGT0',#19,#14);
                #21=USER_DEFINED_CURVE_FONT('UCF0',#18,#20);
                #22=PRE_DEFINED_COLOUR('yellow');
                #23=FILL_AREA_STYLE_COLOUR('',#22);
                #24=FILL_AREA_STYLE('',(#23));
                #25=SURFACE_STYLE_FILL_AREA(#24);
                #26=CURVE_STYLE('CS0',#21,0.25,#22);
                #27=SURFACE_STYLE_BOUNDARY(#26);
                #28=SURFACE_STYLE_PARAMETER_LINE(#26);
                #29=SURFACE_STYLE_CONTROL_GRID(#26);
                #30=SURFACE_STYLE_SEGMENTATION_CURVE(#26);
                #31=SURFACE_STYLE_SILHOUETTE(#26);
                #32=SURFACE_SIDE_STYLE('',(#25,#27,#28,#29,#30,#31));
                #33=SURFACE_STYLE_USAGE(.BOTH.,#32);
                #34=PRESENTATION_STYLE_ASSIGNMENT((#33));
                #35=CHARACTER_GLYPH_STYLE_STROKE(#26);
                #36=CHARACTER_GLYPH_STYLE_OUTLINE(#26);
                #37=CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS(#26,#24);
                #38=TEXT_STYLE_FOR_DEFINED_FONT(#22);
                #39=TEXT_STYLE('TS0',#38);
                #40=TEXT_STYLE_WITH_SPACING('TS1',#38,0.15);
                #41=TEXT_STYLE_WITH_BOX_CHARACTERISTICS('TS2',#38,(BOX_HEIGHT(1.2)));
                #42=PRE_DEFINED_CURVE_FONT('solid');
                #43=CURVE_STYLE('CS1',#42,0.2,#22);
                #44=PROPERTY_DEFINITION('PD_CURVE_FONT','',#43);
                #45=PROPERTY_DEFINITION_REPRESENTATION(#44,#12);
                #46=PRE_DEFINED_MARKER('dot');
                #47=POINT_STYLE('PS0',#46,2.5,#22);
                #48=PROPERTY_DEFINITION('PD_MARKER','',#47);
                #49=PROPERTY_DEFINITION_REPRESENTATION(#48,#12);
                #50=TEXT_STYLE_WITH_JUSTIFICATION('TS3',#38,.LEFT.);
                #51=PROPERTY_DEFINITION('PD_COLOUR','',#50);
                #52=PROPERTY_DEFINITION_REPRESENTATION(#51,#12);
                #60=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #61=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #62=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #63=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #64=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #65=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #66=CARTESIAN_POINT('P6',(6.0,0.0,0.0));
                #67=CARTESIAN_POINT('P7',(7.0,0.0,0.0));
                #68=CARTESIAN_POINT('P8',(8.0,0.0,0.0));
                #69=CARTESIAN_POINT('P9',(9.0,0.0,0.0));
                #70=CARTESIAN_POINT('P10',(10.0,0.0,0.0));
                #71=CARTESIAN_POINT('P11',(11.0,0.0,0.0));
                #72=ANNOTATION_TEXT_OCCURRENCE('NOTE_CURVE_STYLE','',#60);
                #73=ANNOTATION_TEXT_OCCURRENCE('NOTE_SURFACE_USAGE','',#61);
                #74=ANNOTATION_TEXT_OCCURRENCE('NOTE_STYLE_ASSIGNMENT','',#62);
                #75=ANNOTATION_TEXT_OCCURRENCE('NOTE_GLYPH_STROKE','',#63);
                #76=ANNOTATION_TEXT_OCCURRENCE('NOTE_GLYPH_OUTLINE','',#64);
                #77=ANNOTATION_TEXT_OCCURRENCE('NOTE_GLYPH_OUTLINE_FILL','',#65);
                #78=ANNOTATION_TEXT_OCCURRENCE('NOTE_TEXT_STYLE','',#66);
                #79=ANNOTATION_TEXT_OCCURRENCE('NOTE_TEXT_STYLE_SPACING','',#67);
                #80=ANNOTATION_TEXT_OCCURRENCE('NOTE_TEXT_STYLE_BOX','',#68);
                #81=ANNOTATION_TEXT_OCCURRENCE('NOTE_LEAF_CURVE_FONT','',#69);
                #82=ANNOTATION_TEXT_OCCURRENCE('NOTE_LEAF_MARKER','',#70);
                #83=ANNOTATION_TEXT_OCCURRENCE('NOTE_LEAF_COLOUR','',#71);
                #84=ANNOTATION_TEXT_OCCURRENCE('NOTE_TEXT_STYLE_JUST','',#71);
                #90=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#26,#10,#72,#8);
                #91=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#33,#10,#73,#8);
                #92=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#34,#10,#74,#8);
                #93=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#35,#10,#75,#8);
                #94=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#36,#10,#76,#8);
                #95=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#37,#10,#77,#8);
                #96=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#39,#10,#78,#8);
                #97=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#40,#10,#79,#8);
                #98=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#41,#10,#80,#8);
                #99=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#42,#10,#81,#8);
                #100=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#46,#10,#82,#8);
                #101=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#22,#10,#83,#8);
                #102=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#50,#10,#84,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_CURVE_STYLE\"",
                "\"name\":\"NOTE_SURFACE_USAGE\"",
                "\"name\":\"NOTE_STYLE_ASSIGNMENT\"",
                "\"name\":\"NOTE_GLYPH_STROKE\"",
                "\"name\":\"NOTE_GLYPH_OUTLINE\"",
                "\"name\":\"NOTE_GLYPH_OUTLINE_FILL\"",
                "\"name\":\"NOTE_TEXT_STYLE\"",
                "\"name\":\"NOTE_TEXT_STYLE_SPACING\"",
                "\"name\":\"NOTE_TEXT_STYLE_BOX\"",
                "\"name\":\"NOTE_TEXT_STYLE_JUST\"",
                "\"name\":\"NOTE_LEAF_CURVE_FONT\"",
                "\"name\":\"NOTE_LEAF_MARKER\"",
                "\"name\":\"NOTE_LEAF_COLOUR\"",
                "\"name\":\"REP_STYLE_MAP\"",
                "\"name\":\"REP_STYLE_LEAF\"",
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
    void shouldBindPmiTargetsThroughColourAndSurfaceStyleLeaves() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_SURFACE_STYLE_LEAF',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=COLOUR_RGB('Amber',1.0,0.75,0.0);
                #13=COLOUR_SPECIFICATION('amber-spec');
                #14=COLOUR();
                #15=FILL_AREA_STYLE_COLOUR('',#12);
                #16=FILL_AREA_STYLE('',(#15));
                #17=SURFACE_STYLE_FILL_AREA(#16);
                #18=SURFACE_STYLE_TRANSPARENT(0.35);
                #19=SURFACE_STYLE_REFLECTANCE_AMBIENT(0.2);
                #20=SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE(0.2,0.6);
                #21=SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR(0.2,0.6,0.4,32.0,#13);
                #22=SURFACE_SIDE_STYLE('',(#17,#18,#19,#20,#21));
                #23=SURFACE_STYLE_USAGE(.BOTH.,#22);
                #24=PRESENTATION_STYLE_ASSIGNMENT((#23));
                #25=PRE_DEFINED_SURFACE_SIDE_STYLE('both');
                #26=PROPERTY_DEFINITION('PD_RGB','',#12);
                #27=PROPERTY_DEFINITION('PD_SPEC','',#13);
                #28=PROPERTY_DEFINITION('PD_COLOUR','',#14);
                #29=PROPERTY_DEFINITION('PD_PRE_SIDE','',#25);
                #30=PROPERTY_DEFINITION_REPRESENTATION(#26,#10);
                #31=PROPERTY_DEFINITION_REPRESENTATION(#27,#10);
                #32=PROPERTY_DEFINITION_REPRESENTATION(#28,#10);
                #33=PROPERTY_DEFINITION_REPRESENTATION(#29,#10);
                #40=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #41=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #42=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #43=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #44=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #45=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #46=CARTESIAN_POINT('P6',(6.0,0.0,0.0));
                #47=ANNOTATION_TEXT_OCCURRENCE('NOTE_RGB','',#40);
                #48=ANNOTATION_TEXT_OCCURRENCE('NOTE_SPEC','',#41);
                #49=ANNOTATION_TEXT_OCCURRENCE('NOTE_COLOUR','',#42);
                #50=ANNOTATION_TEXT_OCCURRENCE('NOTE_TRANSPARENT','',#43);
                #51=ANNOTATION_TEXT_OCCURRENCE('NOTE_AMBIENT','',#44);
                #52=ANNOTATION_TEXT_OCCURRENCE('NOTE_AMBIENT_DIFFUSE','',#45);
                #53=ANNOTATION_TEXT_OCCURRENCE('NOTE_SPECULAR','',#46);
                #54=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRE_SIDE','',#46);
                #55=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#12,#10,#47,#8);
                #56=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#13,#10,#48,#8);
                #57=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#14,#10,#49,#8);
                #58=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#18,#10,#50,#8);
                #59=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#19,#10,#51,#8);
                #60=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#20,#10,#52,#8);
                #61=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#21,#10,#53,#8);
                #62=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#25,#10,#54,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_RGB\"",
                "\"name\":\"NOTE_SPEC\"",
                "\"name\":\"NOTE_COLOUR\"",
                "\"name\":\"NOTE_TRANSPARENT\"",
                "\"name\":\"NOTE_AMBIENT\"",
                "\"name\":\"NOTE_AMBIENT_DIFFUSE\"",
                "\"name\":\"NOTE_SPECULAR\"",
                "\"name\":\"NOTE_PRE_SIDE\"",
                "\"name\":\"REP_SURFACE_STYLE_LEAF\"",
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
    void shouldBindPmiTargetsThroughStyledAndLayerCarriers() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_STYLE_WRAP',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #13=DIRECTION('N',(0.0,0.0,1.0));
                #14=DIRECTION('X',(1.0,0.0,0.0));
                #15=AXIS2_PLACEMENT_3D('AX',#12,#13,#14);
                #16=PLANE('PL0',#15);
                #17=PRE_DEFINED_COLOUR('yellow');
                #18=PRE_DEFINED_CURVE_FONT('solid');
                #19=CURVE_STYLE('CS0',#18,0.2,#17);
                #20=PRESENTATION_STYLE_ASSIGNMENT((#19));
                #21=STYLED_ITEM('S0',(#20),#16);
                #22=OVER_RIDING_STYLED_ITEM('OS0',(#20),#16,#21);
                #23=PRESENTATION_LAYER_ASSIGNMENT('L1','layer one',(#16,#21,#22));
                #30=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #31=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #32=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #33=ANNOTATION_TEXT_OCCURRENCE('NOTE_STYLED','',#30);
                #34=ANNOTATION_TEXT_OCCURRENCE('NOTE_OVERRIDE','',#31);
                #35=ANNOTATION_TEXT_OCCURRENCE('NOTE_LAYER','',#32);
                #40=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#21,#10,#33,#8);
                #41=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#22,#10,#34,#8);
                #42=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#23,#10,#35,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_STYLED\"",
                "\"name\":\"NOTE_OVERRIDE\"",
                "\"name\":\"NOTE_LAYER\"",
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
    void shouldBindPmiTargetsThroughMappedTransformationAndPlacementCarriers() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_MAP_WRAP',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #13=DIRECTION('DZ',(0.0,0.0,1.0));
                #14=DIRECTION('DX',(1.0,0.0,0.0));
                #15=DIRECTION('DY',(0.0,1.0,0.0));
                #16=AXIS1_PLACEMENT('AX1',#12,#13);
                #17=AXIS2_PLACEMENT_3D('AX0',#12,#13,#14);
                #18=CARTESIAN_POINT('T1',(5.0,0.0,0.0));
                #19=AXIS2_PLACEMENT_3D('AX1T',#18,#13,#14);
                #20=ITEM_DEFINED_TRANSFORMATION('MOVE','translate x',#17,#19);
                #21=CARTESIAN_POINT('ORIG',(2.0,3.0,4.0));
                #22=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',#14,#15,#21,1.5,#13);
                #23=CARTESIAN_POINT('M0',(0.0,0.0));
                #24=DIRECTION('DX2',(1.0,0.0));
                #25=AXIS2_PLACEMENT_2D('MAP0',#23,#24);
                #26=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','MAP'));
                #27=REPRESENTATION('REP_MAPPED',(),#26);
                #28=REPRESENTATION_MAP(#25,#27);
                #29=MAPPED_ITEM(#28,#22);
                #30=POINT_REPLICA('PR0',#12,#22);
                #40=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #41=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #42=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #43=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #44=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #45=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #46=ANNOTATION_TEXT_OCCURRENCE('NOTE_MAPPED','',#40);
                #47=ANNOTATION_TEXT_OCCURRENCE('NOTE_REPLICA','',#41);
                #48=ANNOTATION_TEXT_OCCURRENCE('NOTE_ITEM_TRANSFORM','',#42);
                #49=ANNOTATION_TEXT_OCCURRENCE('NOTE_CART_TRANSFORM','',#43);
                #50=ANNOTATION_TEXT_OCCURRENCE('NOTE_AXIS1','',#44);
                #51=ANNOTATION_TEXT_OCCURRENCE('NOTE_AXIS2','',#45);
                #60=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#29,#10,#46,#8);
                #61=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#30,#10,#47,#8);
                #62=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#20,#10,#48,#8);
                #63=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#22,#10,#49,#8);
                #64=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#16,#10,#50,#8);
                #65=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#17,#10,#51,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_MAPPED\"",
                "\"name\":\"NOTE_REPLICA\"",
                "\"name\":\"NOTE_ITEM_TRANSFORM\"",
                "\"name\":\"NOTE_CART_TRANSFORM\"",
                "\"name\":\"NOTE_AXIS1\"",
                "\"name\":\"NOTE_AXIS2\"",
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
    void shouldBindPmiTargetsThroughTopologyAndContainerCarriers() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_TOPO_WRAP',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #13=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #14=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #15=DIRECTION('DX',(1.0,0.0,0.0));
                #16=VECTOR('VX',#15,1.0);
                #17=LINE('L0',#12,#16);
                #18=VERTEX_POINT('V0',#12);
                #19=VERTEX_POINT('V1',#13);
                #20=EDGE_CURVE('E0',#18,#19,#17,.T.);
                #21=ORIENTED_EDGE('OE0',$,$,#20,.T.);
                #22=PATH('PTH',(#21));
                #23=OPEN_PATH('OP0',(#21));
                #24=ORIENTED_PATH('OP1',#22,.F.);
                #25=EDGE_LOOP('EL0',(#21));
                #26=POLY_LOOP('PL0',(#12,#13,#14));
                #27=CONNECTED_EDGE_SET('CES0',(#20));
                #28=EDGE_BASED_WIREFRAME_MODEL('EBWM',(#27));
                #29=WIRE_SHELL('WS0',(#25));
                #30=SHELL_BASED_WIREFRAME_MODEL('SBWM',(#29));
                #31=POINT_SET('PS0',(#12,#13));
                #32=GEOMETRIC_CURVE_SET('GCS0',(#17));
                #33=GEOMETRIC_SET('GS0',(#31,#32,#24,#26,#28,#30));
                #40=CARTESIAN_POINT('N0',(0.0,0.0,0.0));
                #41=CARTESIAN_POINT('N1',(1.0,0.0,0.0));
                #42=CARTESIAN_POINT('N2',(2.0,0.0,0.0));
                #43=CARTESIAN_POINT('N3',(3.0,0.0,0.0));
                #44=CARTESIAN_POINT('N4',(4.0,0.0,0.0));
                #45=CARTESIAN_POINT('N5',(5.0,0.0,0.0));
                #46=ANNOTATION_TEXT_OCCURRENCE('NOTE_PATH','',#40);
                #47=ANNOTATION_TEXT_OCCURRENCE('NOTE_LOOP','',#41);
                #48=ANNOTATION_TEXT_OCCURRENCE('NOTE_WIREFRAME','',#42);
                #49=ANNOTATION_TEXT_OCCURRENCE('NOTE_SHELL_WIREFRAME','',#43);
                #50=ANNOTATION_TEXT_OCCURRENCE('NOTE_POINT_SET','',#44);
                #51=ANNOTATION_TEXT_OCCURRENCE('NOTE_GEOMETRIC_SET','',#45);
                #60=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#24,#10,#46,#8);
                #61=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#25,#10,#47,#8);
                #62=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#28,#10,#48,#8);
                #63=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#30,#10,#49,#8);
                #64=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#31,#10,#50,#8);
                #65=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#33,#10,#51,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_PATH\"",
                "\"name\":\"NOTE_LOOP\"",
                "\"name\":\"NOTE_WIREFRAME\"",
                "\"name\":\"NOTE_SHELL_WIREFRAME\"",
                "\"name\":\"NOTE_POINT_SET\"",
                "\"name\":\"NOTE_GEOMETRIC_SET\"",
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
    void shouldBindPmiTargetsThroughSurfaceContainerCarriers() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_SURF_WRAP',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #13=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #14=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #15=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #16=DIRECTION('DZ',(0.0,0.0,1.0));
                #17=DIRECTION('DX',(1.0,0.0,0.0));
                #18=AXIS2_PLACEMENT_3D('AX',#12,#16,#17);
                #19=PLANE('PL',#18);
                #20=POLY_LOOP('PL0',(#12,#13,#14,#15));
                #21=FACE_BOUND('FB',#20,.T.);
                #22=ADVANCED_FACE('AF0',(#21),#19,.T.);
                #23=ORIENTED_FACE('OF0',#22,.F.);
                #24=FACE_SURFACE('FS0',(#21),#19,.T.);
                #25=OPEN_SHELL('OS0',(#22));
                #26=SURFACED_OPEN_SHELL('SOS0',(#24));
                #27=ORIENTED_OPEN_SHELL('OOS0',#25,.F.);
                #28=CLOSED_SHELL('CS0',(#22));
                #29=ORIENTED_CLOSED_SHELL('OCS0',#28,.F.);
                #30=CONNECTED_FACE_SET('CFS0',(#22));
                #31=CONNECTED_FACE_SUB_SET('CFSS0',(#22),#30);
                #32=FACE_BASED_SURFACE_MODEL('FBSM0',(#30,#25));
                #33=SHELL_BASED_SURFACE_MODEL('SBSM0',(#25,#26,#27,#28,#29));
                #40=CARTESIAN_POINT('N0',(0.0,0.0,0.0));
                #41=CARTESIAN_POINT('N1',(1.0,0.0,0.0));
                #42=CARTESIAN_POINT('N2',(2.0,0.0,0.0));
                #43=CARTESIAN_POINT('N3',(3.0,0.0,0.0));
                #44=CARTESIAN_POINT('N4',(4.0,0.0,0.0));
                #45=CARTESIAN_POINT('N5',(5.0,0.0,0.0));
                #52=CARTESIAN_POINT('N6',(6.0,0.0,0.0));
                #53=CARTESIAN_POINT('N7',(7.0,0.0,0.0));
                #46=ANNOTATION_TEXT_OCCURRENCE('NOTE_AF','',#40);
                #47=ANNOTATION_TEXT_OCCURRENCE('NOTE_OF','',#41);
                #48=ANNOTATION_TEXT_OCCURRENCE('NOTE_FS','',#42);
                #49=ANNOTATION_TEXT_OCCURRENCE('NOTE_OS','',#43);
                #50=ANNOTATION_TEXT_OCCURRENCE('NOTE_CFS','',#44);
                #51=ANNOTATION_TEXT_OCCURRENCE('NOTE_MODEL','',#45);
                #54=ANNOTATION_TEXT_OCCURRENCE('NOTE_CFSS','',#52);
                #55=ANNOTATION_TEXT_OCCURRENCE('NOTE_FBSM','',#53);
                #60=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#22,#10,#46,#8);
                #61=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#23,#10,#47,#8);
                #62=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#24,#10,#48,#8);
                #63=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#25,#10,#49,#8);
                #64=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#30,#10,#50,#8);
                #65=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#33,#10,#51,#8);
                #66=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#31,#10,#54,#8);
                #67=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#32,#10,#55,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughMeasureRepresentationItems() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_MEASURE',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=(NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));
                #13=MEASURE_REPRESENTATION_ITEM('thickness',LENGTH_MEASURE(2.5),#12);
                #14=PROPERTY_DEFINITION('PD_MEASURE','',#13);
                #15=PROPERTY_DEFINITION_REPRESENTATION(#14,#10);
                #16=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #17=ANNOTATION_TEXT_OCCURRENCE('NOTE_MEASURE','',#16);
                #18=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#13,#10,#17,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_MEASURE\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_MEASURE\"",
                "\"viaDefinitionType\":\"MEASURE_REPRESENTATION_ITEM\"",
                "\"viaDefinitionId\":13",
                "\"viaDefinitionType\":\"SI_UNIT\"",
                "\"viaDefinitionId\":12");
    }

    @Test
    void shouldBindPmiTargetsThroughDirectRepresentationItemLinkDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_USED',(),#9);
                #11=REPRESENTATION('REP_AUX',(),#9);
                #12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);
                #13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #15=DIRECTION('DZ',(0.0,0.0,1.0));
                #16=DIRECTION('DX',(1.0,0.0,0.0));
                #17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);
                #18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);
                #19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);
                #20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));
                #21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);
                #22=REPRESENTATION_ITEM('REP_ITEM_ONLY');
                #23=PROPERTY_DEFINITION('PD_REP_ITEM','',#22);
                #24=PROPERTY_DEFINITION_REPRESENTATION(#23,#10);
                #25=(GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('GEOM_ITEM_ONLY'));
                #26=PROPERTY_DEFINITION('PD_GEOM_ITEM','',#25);
                #27=PROPERTY_DEFINITION_REPRESENTATION(#26,#10);
                #28=(TOPOLOGICAL_REPRESENTATION_ITEM('TOPO_ITEM_ONLY'));
                #29=PROPERTY_DEFINITION('PD_TOPO_ITEM','',#28);
                #30=PROPERTY_DEFINITION_REPRESENTATION(#29,#10);
                #31=(NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));
                #32=MEASURE_REPRESENTATION_ITEM('thickness',LENGTH_MEASURE(2.5),#31);
                #33=PROPERTY_DEFINITION('PD_MEASURE','',#32);
                #34=PROPERTY_DEFINITION_REPRESENTATION(#33,#10);
                #35=DESCRIPTIVE_REPRESENTATION_ITEM('DESC_ITEM','descriptive');
                #36=PROPERTY_DEFINITION('PD_DESC_ITEM','',#35);
                #37=PROPERTY_DEFINITION_REPRESENTATION(#36,#10);
                #38=VALUE_REPRESENTATION_ITEM('VALUE_ITEM',INTEGER_REPRESENTATION_ITEM(7));
                #39=PROPERTY_DEFINITION('PD_VALUE_ITEM','',#38);
                #40=PROPERTY_DEFINITION_REPRESENTATION(#39,#10);
                #41=MEASURE_WITH_UNIT(LENGTH_MEASURE(9.5),#31);
                #42=PROPERTY_DEFINITION('PD_MEASURE_WITH_UNIT','',#41);
                #43=PROPERTY_DEFINITION_REPRESENTATION(#42,#10);
                #44=ADDRESS('HQ','','Main St','','Shanghai','Shanghai','200000','CN','','','','');
                #45=PROPERTY_DEFINITION('PD_ADDRESS','',#44);
                #46=PROPERTY_DEFINITION_REPRESENTATION(#45,#10);
                #47=CHARACTERIZED_OBJECT('CHAR_OBJ','characterized');
                #48=PROPERTY_DEFINITION('PD_CHAR_OBJ','',#47);
                #49=PROPERTY_DEFINITION_REPRESENTATION(#48,#10);
                #50=DIMENSIONAL_EXPONENTS(1.0,0.0,0.0,0.0,0.0,0.0,0.0);
                #51=PROPERTY_DEFINITION('PD_DIM_EXP','',#50);
                #52=PROPERTY_DEFINITION_REPRESENTATION(#51,#10);
                #53=(VERTEX() TOPOLOGICAL_REPRESENTATION_ITEM('VERT_MARK'));
                #54=PROPERTY_DEFINITION('PD_VERTEX','',#53);
                #55=PROPERTY_DEFINITION_REPRESENTATION(#54,#10);
                #56=(EDGE() TOPOLOGICAL_REPRESENTATION_ITEM('EDGE_MARK'));
                #57=PROPERTY_DEFINITION('PD_EDGE','',#56);
                #58=PROPERTY_DEFINITION_REPRESENTATION(#57,#10);
                #60=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #61=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #62=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #63=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #64=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #65=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #66=CARTESIAN_POINT('P6',(6.0,0.0,0.0));
                #67=CARTESIAN_POINT('P7',(7.0,0.0,0.0));
                #68=CARTESIAN_POINT('P8',(8.0,0.0,0.0));
                #69=CARTESIAN_POINT('P9',(9.0,0.0,0.0));
                #70=CARTESIAN_POINT('P10',(10.0,0.0,0.0));
                #71=ANNOTATION_TEXT_OCCURRENCE('NOTE_REP_ITEM','',#60);
                #72=ANNOTATION_TEXT_OCCURRENCE('NOTE_GEOM_ITEM','',#61);
                #73=ANNOTATION_TEXT_OCCURRENCE('NOTE_TOPO_ITEM','',#62);
                #74=ANNOTATION_TEXT_OCCURRENCE('NOTE_MEASURE_ITEM','',#63);
                #75=ANNOTATION_TEXT_OCCURRENCE('NOTE_DESC_ITEM','',#64);
                #76=ANNOTATION_TEXT_OCCURRENCE('NOTE_VALUE_ITEM','',#65);
                #77=ANNOTATION_TEXT_OCCURRENCE('NOTE_MEASURE_WITH_UNIT','',#66);
                #78=ANNOTATION_TEXT_OCCURRENCE('NOTE_ADDRESS','',#67);
                #79=ANNOTATION_TEXT_OCCURRENCE('NOTE_CHAR_OBJ','',#68);
                #80=ANNOTATION_TEXT_OCCURRENCE('NOTE_DIM_EXP','',#69);
                #81=ANNOTATION_TEXT_OCCURRENCE('NOTE_VERTEX','',#70);
                #82=ANNOTATION_TEXT_OCCURRENCE('NOTE_EDGE','',#71);
                #83=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#22,#10,#71,#8);
                #84=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#25,#10,#72,#8);
                #85=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#28,#10,#73,#8);
                #86=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#32,#10,#74,#8);
                #87=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#35,#10,#75,#8);
                #88=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#38,#10,#76,#8);
                #89=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#41,#10,#77,#8);
                #90=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#44,#10,#78,#8);
                #91=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#47,#10,#79,#8);
                #92=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#50,#10,#80,#8);
                #93=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#53,#10,#81,#8);
                #94=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#56,#10,#82,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughDirectMapTransformAndPointReplicaLinkDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_USED',(),#9);
                #11=REPRESENTATION('REP_AUX',(),#9);
                #12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);
                #13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #15=DIRECTION('DZ',(0.0,0.0,1.0));
                #16=DIRECTION('DX',(1.0,0.0,0.0));
                #17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);
                #18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);
                #19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);
                #20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));
                #21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);
                #22=CARTESIAN_POINT('MAP_P0',(2.0,0.0));
                #23=DIRECTION('MAP_D0',(1.0,0.0));
                #24=AXIS2_PLACEMENT_2D('MAP0',#22,#23);
                #25=REPRESENTATION('MAP_REP',(),#9);
                #26=REPRESENTATION_MAP(#24,#25);
                #27=SYMBOL_REPRESENTATION_MAP(#24,#25);
                #28=CARTESIAN_POINT('MAP_P1',(3.0,0.0));
                #29=DIRECTION('MAP_D1',(0.0,1.0));
                #30=AXIS2_PLACEMENT_2D('TGT0',#28,#29);
                #31=MAPPED_ITEM(#26,#30);
                #32=CARTESIAN_POINT('TR0',(4.0,0.0,0.0));
                #33=DIRECTION('DY',(0.0,1.0,0.0));
                #34=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',#16,#33,#32,1.0,#15);
                #35=CARTESIAN_POINT('PR_P',(5.0,0.0,0.0));
                #36=POINT_REPLICA('PR0',#35,#34);
                #40=PROPERTY_DEFINITION('PD_REP_MAP','',#26);
                #41=PROPERTY_DEFINITION('PD_SYM_MAP','',#27);
                #42=PROPERTY_DEFINITION('PD_MAPPED','',#31);
                #43=PROPERTY_DEFINITION('PD_ITEM_XF','',#19);
                #44=PROPERTY_DEFINITION('PD_CART_XF','',#34);
                #45=PROPERTY_DEFINITION('PD_POINT_REPLICA','',#36);
                #50=PROPERTY_DEFINITION_REPRESENTATION(#40,#10);
                #51=PROPERTY_DEFINITION_REPRESENTATION(#41,#10);
                #52=PROPERTY_DEFINITION_REPRESENTATION(#42,#10);
                #53=PROPERTY_DEFINITION_REPRESENTATION(#43,#10);
                #54=PROPERTY_DEFINITION_REPRESENTATION(#44,#10);
                #55=PROPERTY_DEFINITION_REPRESENTATION(#45,#10);
                #60=ANNOTATION_TEXT_OCCURRENCE('NOTE_REP_MAP','',#13);
                #61=ANNOTATION_TEXT_OCCURRENCE('NOTE_SYM_MAP','',#14);
                #62=ANNOTATION_TEXT_OCCURRENCE('NOTE_MAPPED_ITEM','',#32);
                #63=ANNOTATION_TEXT_OCCURRENCE('NOTE_ITEM_XF','',#35);
                #64=ANNOTATION_TEXT_OCCURRENCE('NOTE_CART_XF','',#13);
                #65=ANNOTATION_TEXT_OCCURRENCE('NOTE_POINT_REPLICA','',#14);
                #70=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#26,#10,#60,#8);
                #71=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#27,#10,#61,#8);
                #72=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#31,#10,#62,#8);
                #73=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#19,#10,#63,#8);
                #74=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#34,#10,#64,#8);
                #75=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#36,#10,#65,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughDirectGenericPointAndCurveLinkDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_USED',(),#9);
                #11=REPRESENTATION('REP_AUX',(),#9);
                #12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);
                #13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #15=DIRECTION('DZ',(0.0,0.0,1.0));
                #16=DIRECTION('DX',(1.0,0.0,0.0));
                #17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);
                #18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);
                #19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);
                #20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));
                #21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);
                #22=(POINT() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('PT0'));
                #23=(CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('CV0'));
                #24=PROPERTY_DEFINITION('PD_POINT','',#22);
                #25=PROPERTY_DEFINITION('PD_CURVE','',#23);
                #26=PROPERTY_DEFINITION_REPRESENTATION(#24,#10);
                #27=PROPERTY_DEFINITION_REPRESENTATION(#25,#10);
                #28=CARTESIAN_POINT('N0',(2.0,0.0,0.0));
                #29=CARTESIAN_POINT('N1',(3.0,0.0,0.0));
                #30=ANNOTATION_TEXT_OCCURRENCE('NOTE_POINT_GENERIC','',#28);
                #31=ANNOTATION_TEXT_OCCURRENCE('NOTE_CURVE_GENERIC','',#29);
                #32=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#22,#10,#30,#8);
                #33=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#23,#10,#31,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughDirectRepresentationAndShapeAspectOccurrenceDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_DIRECT',(),#9);
                #11=REPRESENTATION('REP_AUX',(),#9);
                #12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);
                #13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #15=DIRECTION('DZ',(0.0,0.0,1.0));
                #16=DIRECTION('DX',(1.0,0.0,0.0));
                #17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);
                #18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);
                #19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);
                #20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));
                #21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);
                #22=SHAPE_ASPECT_OCCURRENCE('SAO0','occ',#7,.T.,#8);
                #23=CARTESIAN_POINT('N0',(2.0,0.0,0.0));
                #24=CARTESIAN_POINT('N1',(3.0,0.0,0.0));
                #25=ANNOTATION_TEXT_OCCURRENCE('NOTE_REP_DIRECT','',#23);
                #26=ANNOTATION_TEXT_OCCURRENCE('NOTE_SHAPE_OCC','',#24);
                #27=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#10,#10,#25,#8);
                #28=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#22,#10,#26,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughDirectPredefinedAndColourLeafDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_PREDEF',(),#9);
                #11=REPRESENTATION('REP_AUX',(),#9);
                #12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);
                #13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #15=DIRECTION('DZ',(0.0,0.0,1.0));
                #16=DIRECTION('DX',(1.0,0.0,0.0));
                #17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);
                #18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);
                #19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);
                #20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));
                #21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);
                #22=PRE_DEFINED_COLOUR('yellow');
                #23=DRAUGHTING_PRE_DEFINED_COLOUR('red');
                #24=COLOUR_RGB('Amber',1.0,0.75,0.0);
                #25=COLOUR_SPECIFICATION('amber-spec');
                #26=COLOUR();
                #27=PRE_DEFINED_CURVE_FONT('solid');
                #28=DRAUGHTING_PRE_DEFINED_CURVE_FONT('chain');
                #29=PRE_DEFINED_TEXT_FONT('iso');
                #30=DRAUGHTING_PRE_DEFINED_TEXT_FONT('cadfont');
                #31=PRE_DEFINED_TERMINATOR_SYMBOL('arrow');
                #32=PRE_DEFINED_SYMBOL('sym');
                #33=PRE_DEFINED_DIMENSION_SYMBOL('dim');
                #34=PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL('tol');
                #35=PRE_DEFINED_ITEM('pdi');
                #36=PRE_DEFINED_MARKER('dot');
                #40=PROPERTY_DEFINITION('PD0','',#22);
                #41=PROPERTY_DEFINITION('PD1','',#23);
                #42=PROPERTY_DEFINITION('PD2','',#24);
                #43=PROPERTY_DEFINITION('PD3','',#25);
                #44=PROPERTY_DEFINITION('PD4','',#26);
                #45=PROPERTY_DEFINITION('PD5','',#27);
                #46=PROPERTY_DEFINITION('PD6','',#28);
                #47=PROPERTY_DEFINITION('PD7','',#29);
                #48=PROPERTY_DEFINITION('PD8','',#30);
                #49=PROPERTY_DEFINITION('PD9','',#31);
                #50=PROPERTY_DEFINITION('PD10','',#32);
                #51=PROPERTY_DEFINITION('PD11','',#33);
                #52=PROPERTY_DEFINITION('PD12','',#34);
                #53=PROPERTY_DEFINITION('PD13','',#35);
                #54=PROPERTY_DEFINITION('PD14','',#36);
                #60=PROPERTY_DEFINITION_REPRESENTATION(#40,#10);
                #61=PROPERTY_DEFINITION_REPRESENTATION(#41,#10);
                #62=PROPERTY_DEFINITION_REPRESENTATION(#42,#10);
                #63=PROPERTY_DEFINITION_REPRESENTATION(#43,#10);
                #64=PROPERTY_DEFINITION_REPRESENTATION(#44,#10);
                #65=PROPERTY_DEFINITION_REPRESENTATION(#45,#10);
                #66=PROPERTY_DEFINITION_REPRESENTATION(#46,#10);
                #67=PROPERTY_DEFINITION_REPRESENTATION(#47,#10);
                #68=PROPERTY_DEFINITION_REPRESENTATION(#48,#10);
                #69=PROPERTY_DEFINITION_REPRESENTATION(#49,#10);
                #70=PROPERTY_DEFINITION_REPRESENTATION(#50,#10);
                #71=PROPERTY_DEFINITION_REPRESENTATION(#51,#10);
                #72=PROPERTY_DEFINITION_REPRESENTATION(#52,#10);
                #73=PROPERTY_DEFINITION_REPRESENTATION(#53,#10);
                #74=PROPERTY_DEFINITION_REPRESENTATION(#54,#10);
                #80=ANNOTATION_TEXT_OCCURRENCE('NOTE_PREDEF_COLOUR','',#13);
                #81=ANNOTATION_TEXT_OCCURRENCE('NOTE_DRAUGHT_COLOUR','',#14);
                #82=ANNOTATION_TEXT_OCCURRENCE('NOTE_RGB','',#13);
                #83=ANNOTATION_TEXT_OCCURRENCE('NOTE_SPEC','',#14);
                #84=ANNOTATION_TEXT_OCCURRENCE('NOTE_COLOUR','',#13);
                #85=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRE_FONT','',#14);
                #86=ANNOTATION_TEXT_OCCURRENCE('NOTE_DRAUGHT_FONT','',#13);
                #87=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRE_TEXT_FONT','',#14);
                #88=ANNOTATION_TEXT_OCCURRENCE('NOTE_DRAUGHT_TEXT_FONT','',#13);
                #89=ANNOTATION_TEXT_OCCURRENCE('NOTE_TERM','',#14);
                #90=ANNOTATION_TEXT_OCCURRENCE('NOTE_SYMBOL','',#13);
                #91=ANNOTATION_TEXT_OCCURRENCE('NOTE_DIM_SYMBOL','',#14);
                #92=ANNOTATION_TEXT_OCCURRENCE('NOTE_TOL_SYMBOL','',#13);
                #93=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRE_ITEM','',#14);
                #94=ANNOTATION_TEXT_OCCURRENCE('NOTE_MARKER','',#13);
                #100=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#22,#10,#80,#8);
                #101=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#23,#10,#81,#8);
                #102=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#24,#10,#82,#8);
                #103=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#25,#10,#83,#8);
                #104=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#26,#10,#84,#8);
                #105=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#27,#10,#85,#8);
                #106=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#28,#10,#86,#8);
                #107=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#29,#10,#87,#8);
                #108=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#30,#10,#88,#8);
                #109=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#31,#10,#89,#8);
                #110=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#32,#10,#90,#8);
                #111=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#33,#10,#91,#8);
                #112=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#34,#10,#92,#8);
                #113=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#35,#10,#93,#8);
                #114=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#36,#10,#94,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughDirectProductCategoryAndEffectivityDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=PRODUCT_CATEGORY('CAT_A','cat a');
                #9=PRODUCT_RELATED_PRODUCT_CATEGORY('CAT_LINK','',(#3));
                #10=GENERAL_PROPERTY('GP-1','gp','general property');
                #11=EFFECTIVITY('EFF-1');
                #12=PRODUCT_DEFINITION_EFFECTIVITY('PDE-1','serial usage',#6);
                #13=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #14=REPRESENTATION('REP_UPSTREAM',(),#13);
                #20=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #21=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #22=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #23=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #24=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #25=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #26=CARTESIAN_POINT('P6',(6.0,0.0,0.0));
                #27=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRODUCT','',#20);
                #28=ANNOTATION_TEXT_OCCURRENCE('NOTE_FORMATION','',#21);
                #29=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRODUCT_DEF','',#22);
                #30=ANNOTATION_TEXT_OCCURRENCE('NOTE_PDS','',#23);
                #31=ANNOTATION_TEXT_OCCURRENCE('NOTE_CATEGORY','',#24);
                #32=ANNOTATION_TEXT_OCCURRENCE('NOTE_RELATED_CATEGORY','',#25);
                #33=ANNOTATION_TEXT_OCCURRENCE('NOTE_GENERAL_PROPERTY','',#26);
                #34=ANNOTATION_TEXT_OCCURRENCE('NOTE_EFFECTIVITY','',#20);
                #35=ANNOTATION_TEXT_OCCURRENCE('NOTE_PDE','',#21);
                #40=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#3,#14,#27,#7);
                #41=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#4,#14,#28,#7);
                #42=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#6,#14,#29,#7);
                #43=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#7,#14,#30,#7);
                #44=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#8,#14,#31,#7);
                #45=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#9,#14,#32,#7);
                #46=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#10,#14,#33,#7);
                #47=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#11,#14,#34,#7);
                #48=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#12,#14,#35,#7);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_PRODUCT\"",
                "\"name\":\"NOTE_FORMATION\"",
                "\"name\":\"NOTE_PRODUCT_DEF\"",
                "\"name\":\"NOTE_PDS\"",
                "\"name\":\"NOTE_CATEGORY\"",
                "\"name\":\"NOTE_RELATED_CATEGORY\"",
                "\"name\":\"NOTE_GENERAL_PROPERTY\"",
                "\"name\":\"NOTE_EFFECTIVITY\"",
                "\"name\":\"NOTE_PDE\"",
                "\"id\":14,\"type\":\"representation\",\"name\":\"REP_UPSTREAM\"",
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
    void shouldBindPmiTargetsThroughDirectGeometricLeafLinkDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_USED',(),#9);
                #11=REPRESENTATION('REP_AUX',(),#9);
                #12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);
                #13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #15=DIRECTION('DZ',(0.0,0.0,1.0));
                #16=DIRECTION('DX',(1.0,0.0,0.0));
                #17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);
                #18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);
                #19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);
                #20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));
                #21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);
                #22=CARTESIAN_POINT('DP0',(2.0,0.0,0.0));
                #23=PROPERTY_DEFINITION('PD_POINT','',#22);
                #24=PROPERTY_DEFINITION_REPRESENTATION(#23,#10);
                #25=DIRECTION('DD0',(0.0,1.0,0.0));
                #26=PROPERTY_DEFINITION('PD_DIRECTION','',#25);
                #27=PROPERTY_DEFINITION_REPRESENTATION(#26,#10);
                #28=VECTOR('V0',#25,2.5);
                #29=PROPERTY_DEFINITION('PD_VECTOR','',#28);
                #30=PROPERTY_DEFINITION_REPRESENTATION(#29,#10);
                #31=AXIS2_PLACEMENT_3D('AX2',#22,#15,#16);
                #32=PROPERTY_DEFINITION('PD_AXIS','',#31);
                #33=PROPERTY_DEFINITION_REPRESENTATION(#32,#10);
                #34=PLANE('PL0',#31);
                #35=PROPERTY_DEFINITION('PD_PLANE','',#34);
                #36=PROPERTY_DEFINITION_REPRESENTATION(#35,#10);
                #40=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #41=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #42=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #43=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #44=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #45=ANNOTATION_TEXT_OCCURRENCE('NOTE_POINT','',#40);
                #46=ANNOTATION_TEXT_OCCURRENCE('NOTE_DIRECTION','',#41);
                #47=ANNOTATION_TEXT_OCCURRENCE('NOTE_VECTOR','',#42);
                #48=ANNOTATION_TEXT_OCCURRENCE('NOTE_AXIS','',#43);
                #49=ANNOTATION_TEXT_OCCURRENCE('NOTE_PLANE','',#44);
                #50=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#22,#10,#45,#8);
                #51=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#25,#10,#46,#8);
                #52=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#28,#10,#47,#8);
                #53=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#31,#10,#48,#8);
                #54=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#34,#10,#49,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughDirectCurveAndSurfaceLinkDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_USED',(),#9);
                #11=REPRESENTATION('REP_AUX',(),#9);
                #12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);
                #13=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #14=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #15=DIRECTION('DZ',(0.0,0.0,1.0));
                #16=DIRECTION('DX',(1.0,0.0,0.0));
                #17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);
                #18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);
                #19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);
                #20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));
                #21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);
                #22=CARTESIAN_POINT('P0',(2.0,0.0,0.0));
                #23=DIRECTION('DY',(0.0,1.0,0.0));
                #24=VECTOR('V0',#23,2.5);
                #25=LINE('L0',#22,#24);
                #26=PROPERTY_DEFINITION('PD_LINE','',#25);
                #27=PROPERTY_DEFINITION_REPRESENTATION(#26,#10);
                #28=CIRCLE('C0',#17,5.0);
                #29=PROPERTY_DEFINITION('PD_CIRCLE','',#28);
                #30=PROPERTY_DEFINITION_REPRESENTATION(#29,#10);
                #31=ELLIPSE('E0',#17,6.0,3.0);
                #32=PROPERTY_DEFINITION('PD_ELLIPSE','',#31);
                #33=PROPERTY_DEFINITION_REPRESENTATION(#32,#10);
                #34=POLYLINE('PL0',(#13,#14,#22));
                #35=PROPERTY_DEFINITION('PD_POLYLINE','',#34);
                #36=PROPERTY_DEFINITION_REPRESENTATION(#35,#10);
                #37=TRIMMED_CURVE('TC0',#25,(#22),(#14),.T.,.PARAMETER.);
                #38=PROPERTY_DEFINITION('PD_TRIM','',#37);
                #39=PROPERTY_DEFINITION_REPRESENTATION(#38,#10);
                #40=CYLINDRICAL_SURFACE('CYL0',#17,2.0);
                #41=PROPERTY_DEFINITION('PD_CYL','',#40);
                #42=PROPERTY_DEFINITION_REPRESENTATION(#41,#10);
                #43=SURFACE_OF_LINEAR_EXTRUSION('SOLE0',#25,#24);
                #44=PROPERTY_DEFINITION('PD_SOLE','',#43);
                #45=PROPERTY_DEFINITION_REPRESENTATION(#44,#10);
                #46=AXIS1_PLACEMENT('AXIS1',#22,#15);
                #47=SURFACE_OF_REVOLUTION('SOR0',#25,#46);
                #48=PROPERTY_DEFINITION('PD_SOR','',#47);
                #49=PROPERTY_DEFINITION_REPRESENTATION(#48,#10);
                #60=CARTESIAN_POINT('N0',(0.0,0.0,0.0));
                #61=CARTESIAN_POINT('N1',(1.0,0.0,0.0));
                #62=CARTESIAN_POINT('N2',(2.0,0.0,0.0));
                #63=CARTESIAN_POINT('N3',(3.0,0.0,0.0));
                #64=CARTESIAN_POINT('N4',(4.0,0.0,0.0));
                #65=CARTESIAN_POINT('N5',(5.0,0.0,0.0));
                #66=CARTESIAN_POINT('N6',(6.0,0.0,0.0));
                #67=ANNOTATION_TEXT_OCCURRENCE('NOTE_LINE','',#60);
                #68=ANNOTATION_TEXT_OCCURRENCE('NOTE_CIRCLE','',#61);
                #69=ANNOTATION_TEXT_OCCURRENCE('NOTE_ELLIPSE','',#62);
                #70=ANNOTATION_TEXT_OCCURRENCE('NOTE_POLYLINE','',#63);
                #71=ANNOTATION_TEXT_OCCURRENCE('NOTE_TRIM','',#64);
                #72=ANNOTATION_TEXT_OCCURRENCE('NOTE_CYL','',#65);
                #73=ANNOTATION_TEXT_OCCURRENCE('NOTE_SOLE','',#66);
                #74=ANNOTATION_TEXT_OCCURRENCE('NOTE_SOR','',#67);
                #75=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#25,#10,#67,#8);
                #76=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#28,#10,#68,#8);
                #77=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#31,#10,#69,#8);
                #78=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#34,#10,#70,#8);
                #79=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#37,#10,#71,#8);
                #80=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#40,#10,#72,#8);
                #81=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#43,#10,#73,#8);
                #82=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#47,#10,#74,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughDirectConicCurveLinkDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_A',(),#9);
                #11=REPRESENTATION('REP_B',(),#9);
                #12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);
                #13=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #14=DIRECTION('DZ',(0.0,0.0,1.0));
                #15=DIRECTION('DX',(1.0,0.0,0.0));
                #16=AXIS2_PLACEMENT_3D('AX3',#13,#14,#15);
                #17=PARABOLA('PAR0',#16,2.0);
                #18=HYPERBOLA('HYP0',#16,4.0,2.0);
                #19=DEGENERATE_CONIC('DC0',#16);
                #20=PROPERTY_DEFINITION('PD_PAR','',#17);
                #21=PROPERTY_DEFINITION('PD_HYP','',#18);
                #22=PROPERTY_DEFINITION('PD_DC','',#19);
                #23=PROPERTY_DEFINITION_REPRESENTATION(#20,#10);
                #24=PROPERTY_DEFINITION_REPRESENTATION(#21,#10);
                #25=PROPERTY_DEFINITION_REPRESENTATION(#22,#10);
                #26=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #27=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #28=DIRECTION('TZ',(0.0,0.0,1.0));
                #29=DIRECTION('TX',(1.0,0.0,0.0));
                #30=AXIS2_PLACEMENT_3D('AX0',#26,#28,#29);
                #31=AXIS2_PLACEMENT_3D('AX1',#27,#28,#29);
                #32=ITEM_DEFINED_TRANSFORMATION('T1','',#30,#31);
                #33=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#32));
                #34=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);
                #35=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONIC','',#13);
                #36=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#17,#10,#35,#8);
                #37=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#18,#10,#35,#8);
                #38=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#19,#10,#35,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughDirectSplineCurveAndSurfaceLinkDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_A',(),#9);
                #11=REPRESENTATION('REP_B',(),#9);
                #12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);
                #13=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #14=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #15=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #16=CARTESIAN_POINT('P3',(0.0,1.0,1.0));
                #17=DIRECTION('DZ',(0.0,0.0,1.0));
                #18=DIRECTION('DX',(1.0,0.0,0.0));
                #19=AXIS2_PLACEMENT_3D('AX0',#13,#17,#18);
                #20=ITEM_DEFINED_TRANSFORMATION('T1','',#19,#19);
                #21=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#20));
                #22=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);
                #30=(B_SPLINE_CURVE('BSC0',2,(#13,#14,#15),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BSC0'));
                #31=(B_SPLINE_CURVE('BSK0',2,(#13,#14,#15),.UNSPECIFIED.,.F.,.F.)
                     B_SPLINE_CURVE_WITH_KNOTS((3,3),(0.0,1.0),.UNSPECIFIED.)
                     BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BSK0'));
                #32=(B_SPLINE_CURVE('RBC0',2,(#13,#14,#15),.UNSPECIFIED.,.F.,.F.)
                     B_SPLINE_CURVE_WITH_KNOTS((3,3),(0.0,1.0),.UNSPECIFIED.)
                     RATIONAL_B_SPLINE_CURVE((1.0,0.5,1.0))
                     BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('RBC0'));
                #33=(BEZIER_CURVE() B_SPLINE_CURVE(2,(#13,#14,#15),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BZ0'));
                #34=(UNIFORM_CURVE() B_SPLINE_CURVE(2,(#13,#14,#15),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('UC0'));
                #35=(QUASI_UNIFORM_CURVE() B_SPLINE_CURVE(2,(#13,#14,#15),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('QUC0'));
                #36=(PIECEWISE_BEZIER_CURVE() BEZIER_CURVE() B_SPLINE_CURVE(2,(#13,#14,#15),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('PBC0'));
                #40=(B_SPLINE_SURFACE(1,1,((#13,#14),(#15,#16)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BSS0'));
                #41=(B_SPLINE_SURFACE(1,1,((#13,#14),(#15,#16)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.)
                     BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BSK0'));
                #42=(B_SPLINE_SURFACE(1,1,((#13,#14),(#15,#16)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.)
                     RATIONAL_B_SPLINE_SURFACE(((1.0,1.0),(1.0,1.0)))
                     BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('RBS0'));
                #43=(BEZIER_SURFACE() B_SPLINE_SURFACE(1,1,((#13,#14),(#15,#16)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BZS0'));
                #44=(UNIFORM_SURFACE() B_SPLINE_SURFACE(1,1,((#13,#14),(#15,#16)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('US0'));
                #45=(QUASI_UNIFORM_SURFACE() B_SPLINE_SURFACE(1,1,((#13,#14),(#15,#16)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('QUS0'));
                #46=(PIECEWISE_BEZIER_SURFACE() BEZIER_SURFACE() B_SPLINE_SURFACE(1,1,((#13,#14),(#15,#16)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('PBS0'));
                #130=PROPERTY_DEFINITION('PD_BSC','',#30);
                #131=PROPERTY_DEFINITION('PD_BSK','',#31);
                #132=PROPERTY_DEFINITION('PD_RBC','',#32);
                #133=PROPERTY_DEFINITION('PD_BZ','',#33);
                #134=PROPERTY_DEFINITION('PD_UC','',#34);
                #135=PROPERTY_DEFINITION('PD_QUC','',#35);
                #136=PROPERTY_DEFINITION('PD_PBC','',#36);
                #137=PROPERTY_DEFINITION('PD_BSS','',#40);
                #138=PROPERTY_DEFINITION('PD_BSKS','',#41);
                #139=PROPERTY_DEFINITION('PD_RBS','',#42);
                #140=PROPERTY_DEFINITION('PD_BZS','',#43);
                #141=PROPERTY_DEFINITION('PD_US','',#44);
                #142=PROPERTY_DEFINITION('PD_QUS','',#45);
                #143=PROPERTY_DEFINITION('PD_PBS','',#46);
                #160=PROPERTY_DEFINITION_REPRESENTATION(#130,#10);
                #161=PROPERTY_DEFINITION_REPRESENTATION(#131,#10);
                #162=PROPERTY_DEFINITION_REPRESENTATION(#132,#10);
                #163=PROPERTY_DEFINITION_REPRESENTATION(#133,#10);
                #164=PROPERTY_DEFINITION_REPRESENTATION(#134,#10);
                #165=PROPERTY_DEFINITION_REPRESENTATION(#135,#10);
                #166=PROPERTY_DEFINITION_REPRESENTATION(#136,#10);
                #167=PROPERTY_DEFINITION_REPRESENTATION(#137,#10);
                #168=PROPERTY_DEFINITION_REPRESENTATION(#138,#10);
                #169=PROPERTY_DEFINITION_REPRESENTATION(#139,#10);
                #170=PROPERTY_DEFINITION_REPRESENTATION(#140,#10);
                #171=PROPERTY_DEFINITION_REPRESENTATION(#141,#10);
                #172=PROPERTY_DEFINITION_REPRESENTATION(#142,#10);
                #173=PROPERTY_DEFINITION_REPRESENTATION(#143,#10);
                #200=ANNOTATION_TEXT_OCCURRENCE('NOTE_SPLINE','',#13);
                #201=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#30,#10,#200,#8);
                #202=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#31,#10,#200,#8);
                #203=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#32,#10,#200,#8);
                #204=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#33,#10,#200,#8);
                #205=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#34,#10,#200,#8);
                #206=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#35,#10,#200,#8);
                #207=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#36,#10,#200,#8);
                #208=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#40,#10,#200,#8);
                #209=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#41,#10,#200,#8);
                #210=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#42,#10,#200,#8);
                #211=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#43,#10,#200,#8);
                #212=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#44,#10,#200,#8);
                #213=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#45,#10,#200,#8);
                #214=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#46,#10,#200,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughDirectWrapperCurveAndPrimitiveSurfaceLinkDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_USED',(),#9);
                #11=REPRESENTATION('REP_AUX',(),#9);
                #12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);
                #13=CARTESIAN_POINT('O0',(0.0,0.0,0.0));
                #14=CARTESIAN_POINT('O1',(1.0,0.0,0.0));
                #15=DIRECTION('DZ',(0.0,0.0,1.0));
                #16=DIRECTION('DX',(1.0,0.0,0.0));
                #17=DIRECTION('DY',(0.0,1.0,0.0));
                #18=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);
                #19=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);
                #20=ITEM_DEFINED_TRANSFORMATION('T1','',#18,#19);
                #21=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#20));
                #22=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);
                #23=VECTOR('V0',#17,2.5);
                #24=LINE('L0',#13,#23);
                #25=PLANE('PL_REF',#18);
                #26=CARTESIAN_POINT('UV0',(0.0,0.0));
                #27=DIRECTION('DUV',(0.0,1.0));
                #28=VECTOR('VUV',#27,1.0);
                #29=LINE('UVL0',#26,#28);
                #30=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #31=DEFINITIONAL_REPRESENTATION('DEF0',(#29),#30);
                #32=PCURVE('PC0',#25,#31);
                #33=SURFACE_CURVE('SC0',#24,(#32),.PCURVE_S1.);
                #34=PROPERTY_DEFINITION('PD_SC','',#33);
                #35=PROPERTY_DEFINITION_REPRESENTATION(#34,#10);
                #36=SEAM_CURVE('SM0',#24,(#32,#32),.PCURVE_S1.);
                #37=PROPERTY_DEFINITION('PD_SM','',#36);
                #38=PROPERTY_DEFINITION_REPRESENTATION(#37,#10);
                #39=OFFSET_CURVE_2D('OC2D',#32,1.0,.F.);
                #40=PROPERTY_DEFINITION('PD_OC2D','',#39);
                #41=PROPERTY_DEFINITION_REPRESENTATION(#40,#10);
                #42=OFFSET_CURVE_3D('OC3D',#24,2.0,.F.,#17);
                #43=PROPERTY_DEFINITION('PD_OC3D','',#42);
                #44=PROPERTY_DEFINITION_REPRESENTATION(#43,#10);
                #45=CONICAL_SURFACE('CN0',#18,4.0,0.5);
                #46=PROPERTY_DEFINITION('PD_CONE','',#45);
                #47=PROPERTY_DEFINITION_REPRESENTATION(#46,#10);
                #48=SPHERICAL_SURFACE('SP0',#18,6.0);
                #49=PROPERTY_DEFINITION('PD_SPH','',#48);
                #50=PROPERTY_DEFINITION_REPRESENTATION(#49,#10);
                #51=TOROIDAL_SURFACE('TO0',#18,8.0,2.0);
                #52=PROPERTY_DEFINITION('PD_TOR','',#51);
                #53=PROPERTY_DEFINITION_REPRESENTATION(#52,#10);
                #70=CARTESIAN_POINT('N0',(0.0,0.0,0.0));
                #71=CARTESIAN_POINT('N1',(1.0,0.0,0.0));
                #72=CARTESIAN_POINT('N2',(2.0,0.0,0.0));
                #73=CARTESIAN_POINT('N3',(3.0,0.0,0.0));
                #74=CARTESIAN_POINT('N4',(4.0,0.0,0.0));
                #75=CARTESIAN_POINT('N5',(5.0,0.0,0.0));
                #76=ANNOTATION_TEXT_OCCURRENCE('NOTE_SC','',#70);
                #77=ANNOTATION_TEXT_OCCURRENCE('NOTE_SM','',#71);
                #78=ANNOTATION_TEXT_OCCURRENCE('NOTE_OC2D','',#72);
                #79=ANNOTATION_TEXT_OCCURRENCE('NOTE_OC3D','',#73);
                #80=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONE','',#74);
                #81=ANNOTATION_TEXT_OCCURRENCE('NOTE_SPH','',#75);
                #82=ANNOTATION_TEXT_OCCURRENCE('NOTE_TOR','',#75);
                #83=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#33,#10,#76,#8);
                #84=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#36,#10,#77,#8);
                #85=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#39,#10,#78,#8);
                #86=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#42,#10,#79,#8);
                #87=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#45,#10,#80,#8);
                #88=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#48,#10,#81,#8);
                #89=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#51,#10,#82,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughDirectCompositeReplicaAndWrapperSurfaceLinkDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_USED',(),#9);
                #11=REPRESENTATION('REP_AUX',(),#9);
                #12=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);
                #13=CARTESIAN_POINT('O0',(0.0,0.0,0.0));
                #14=CARTESIAN_POINT('O1',(1.0,0.0,0.0));
                #15=DIRECTION('DZ',(0.0,0.0,1.0));
                #16=DIRECTION('DX',(1.0,0.0,0.0));
                #17=AXIS2_PLACEMENT_3D('AX0',#13,#15,#16);
                #18=AXIS2_PLACEMENT_3D('AX1',#14,#15,#16);
                #19=ITEM_DEFINED_TRANSFORMATION('T1','',#17,#18);
                #20=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#19));
                #21=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);
                #22=VECTOR('VX',#16,1.0);
                #23=LINE('L0',#13,#22);
                #24=COMPOSITE_CURVE_SEGMENT(.CONTINUOUS.,.T.,#23);
                #25=(COMPOSITE_CURVE('CC0',(#24),.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('cc-name'));
                #26=(COMPOSITE_CURVE_ON_SURFACE('CCS0',(#24),.F.) COMPOSITE_CURVE('CCS0',(#24),.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('ccs-name'));
                #27=PLANE('PL0',#17);
                #28=CARTESIAN_POINT('UV0',(0.0,0.0));
                #29=DIRECTION('DUV',(1.0,0.0));
                #30=VECTOR('VUV',#29,1.0);
                #31=LINE('UL0',#28,#30);
                #32=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #33=DEFINITIONAL_REPRESENTATION('DEF0',(#31),#32);
                #34=DEGENERATE_PCURVE('DPC0',#27,#33);
                #35=RECTANGULAR_TRIMMED_SURFACE('RTS0',#27,0.0,1.0,0.0,1.0,.T.,.T.);
                #36=CURVE_BOUNDED_SURFACE('CBS0',#27,(#25),.T.);
                #37=ORIENTED_SURFACE('OS0',#35,.T.);
                #38=OFFSET_SURFACE('OFS0',#27,1.0,.F.);
                #39=CURVE_REPLICA('CR0',#25,#81);
                #40=SURFACE_REPLICA('SR0',#35,#81);
                #50=PROPERTY_DEFINITION('PD_CCS','',#26);
                #51=PROPERTY_DEFINITION('PD_CC','',#25);
                #52=PROPERTY_DEFINITION('PD_SEG','',#24);
                #53=PROPERTY_DEFINITION('PD_DPC','',#34);
                #54=PROPERTY_DEFINITION('PD_RTS','',#35);
                #55=PROPERTY_DEFINITION('PD_CBS','',#36);
                #56=PROPERTY_DEFINITION('PD_OS','',#37);
                #57=PROPERTY_DEFINITION('PD_OFS','',#38);
                #58=PROPERTY_DEFINITION('PD_CR','',#39);
                #59=PROPERTY_DEFINITION('PD_SR','',#40);
                #60=PROPERTY_DEFINITION_REPRESENTATION(#50,#10);
                #61=PROPERTY_DEFINITION_REPRESENTATION(#51,#10);
                #62=PROPERTY_DEFINITION_REPRESENTATION(#52,#10);
                #63=PROPERTY_DEFINITION_REPRESENTATION(#53,#10);
                #64=PROPERTY_DEFINITION_REPRESENTATION(#54,#10);
                #65=PROPERTY_DEFINITION_REPRESENTATION(#55,#10);
                #66=PROPERTY_DEFINITION_REPRESENTATION(#56,#10);
                #67=PROPERTY_DEFINITION_REPRESENTATION(#57,#10);
                #68=PROPERTY_DEFINITION_REPRESENTATION(#58,#10);
                #69=PROPERTY_DEFINITION_REPRESENTATION(#59,#10);
                #70=ANNOTATION_TEXT_OCCURRENCE('NOTE_WRAP','',#13);
                #71=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#24,#10,#70,#8);
                #72=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#25,#10,#70,#8);
                #73=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#26,#10,#70,#8);
                #74=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#34,#10,#70,#8);
                #75=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#35,#10,#70,#8);
                #76=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#36,#10,#70,#8);
                #77=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#37,#10,#70,#8);
                #78=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#38,#10,#70,#8);
                #79=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#39,#10,#70,#8);
                #80=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#40,#10,#70,#8);
                #81=CARTESIAN_TRANSFORMATION_OPERATOR_3D('CTR0',#16,#15,#13,1.0,#16);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughDirectTopologyAndSurfaceContainerLinkDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=SHAPE_REPRESENTATION('REP_A',(),#9);
                #11=SHAPE_REPRESENTATION('REP_B',(),#9);
                #12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #13=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #14=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #15=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #16=DIRECTION('DX',(1.0,0.0,0.0));
                #17=VECTOR('VX',#16,1.0);
                #18=LINE('L0',#12,#17);
                #19=VERTEX_POINT('V0',#12);
                #20=VERTEX_POINT('V1',#13);
                #21=EDGE_CURVE('E0',#19,#20,#18,.T.);
                #22=ORIENTED_EDGE('OE0',$,$,#21,.T.);
                #23=PATH('PTH',(#22));
                #24=OPEN_PATH('OP0',(#22));
                #25=ORIENTED_PATH('OP1',#23,.F.);
                #26=EDGE_LOOP('EL0',(#22));
                #27=POLY_LOOP('PL0',(#12,#13,#14,#15));
                #28=CONNECTED_EDGE_SET('CES0',(#21));
                #29=EDGE_BASED_WIREFRAME_MODEL('EBWM',(#28));
                #30=WIRE_SHELL('WS0',(#26));
                #31=VERTEX_LOOP('VL0',#19);
                #32=VERTEX_SHELL('VS0',#31);
                #33=SHELL_BASED_WIREFRAME_MODEL('SBWM',(#30,#32));
                #34=POINT_SET('PS0',(#12,#13));
                #35=GEOMETRIC_CURVE_SET('GCS0',(#18));
                #36=GEOMETRIC_SET('GS0',(#34,#35,#25,#27,#29,#33));
                #37=DIRECTION('DZ',(0.0,0.0,1.0));
                #38=AXIS2_PLACEMENT_3D('AX',#12,#37,#16);
                #39=PLANE('PL',#38);
                #40=FACE_BOUND('FB',#27,.T.);
                #41=ADVANCED_FACE('AF0',(#40),#39,.T.);
                #42=ORIENTED_FACE('OF0',#41,.F.);
                #43=FACE_SURFACE('FS0',(#40),#39,.T.);
                #44=OPEN_SHELL('OS0',(#41));
                #45=SURFACED_OPEN_SHELL('SOS0',(#43));
                #46=ORIENTED_OPEN_SHELL('OOS0',#44,.F.);
                #47=CLOSED_SHELL('CS0',(#41));
                #48=ORIENTED_CLOSED_SHELL('OCS0',#47,.F.);
                #49=CONNECTED_FACE_SET('CFS0',(#41));
                #50=CONNECTED_FACE_SUB_SET('CFSS0',(#41),#49);
                #51=FACE_BASED_SURFACE_MODEL('FBSM0',(#49,#44));
                #52=SHELL_BASED_SURFACE_MODEL('SBSM0',(#44,#45,#46,#47,#48));
                #53=SUBPATH('SP0',(#22),#23);
                #54=(FACE() TOPOLOGICAL_REPRESENTATION_ITEM('F0') REPRESENTATION_ITEM('F0'));
                #55=(SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('S0'));
                #56=(BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BC0'));
                #57=(BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BS0'));
                #60=PROPERTY_DEFINITION('PD_VP','',#19);
                #61=PROPERTY_DEFINITION('PD_OP','',#25);
                #62=PROPERTY_DEFINITION('PD_EL','',#26);
                #63=PROPERTY_DEFINITION('PD_CES','',#28);
                #64=PROPERTY_DEFINITION('PD_EBWM','',#29);
                #65=PROPERTY_DEFINITION('PD_WS','',#30);
                #66=PROPERTY_DEFINITION('PD_SBWM','',#33);
                #67=PROPERTY_DEFINITION('PD_PS','',#34);
                #68=PROPERTY_DEFINITION('PD_GCS','',#35);
                #69=PROPERTY_DEFINITION('PD_GS','',#36);
                #70=PROPERTY_DEFINITION('PD_AF','',#41);
                #71=PROPERTY_DEFINITION('PD_FS','',#43);
                #72=PROPERTY_DEFINITION('PD_OS','',#44);
                #73=PROPERTY_DEFINITION('PD_SOS','',#45);
                #74=PROPERTY_DEFINITION('PD_OOS','',#46);
                #75=PROPERTY_DEFINITION('PD_CS','',#47);
                #76=PROPERTY_DEFINITION('PD_OCS','',#48);
                #77=PROPERTY_DEFINITION('PD_CFS','',#49);
                #78=PROPERTY_DEFINITION('PD_CFSS','',#50);
                #79=PROPERTY_DEFINITION('PD_FBSM','',#51);
                #80=PROPERTY_DEFINITION('PD_SBSM','',#52);
                #81=PROPERTY_DEFINITION_REPRESENTATION(#60,#10);
                #82=PROPERTY_DEFINITION_REPRESENTATION(#61,#10);
                #83=PROPERTY_DEFINITION_REPRESENTATION(#62,#10);
                #84=PROPERTY_DEFINITION_REPRESENTATION(#63,#10);
                #85=PROPERTY_DEFINITION_REPRESENTATION(#64,#10);
                #86=PROPERTY_DEFINITION_REPRESENTATION(#65,#10);
                #87=PROPERTY_DEFINITION_REPRESENTATION(#66,#10);
                #88=PROPERTY_DEFINITION_REPRESENTATION(#67,#10);
                #89=PROPERTY_DEFINITION_REPRESENTATION(#68,#10);
                #90=PROPERTY_DEFINITION_REPRESENTATION(#69,#10);
                #91=PROPERTY_DEFINITION_REPRESENTATION(#70,#10);
                #92=PROPERTY_DEFINITION_REPRESENTATION(#71,#10);
                #93=PROPERTY_DEFINITION_REPRESENTATION(#72,#10);
                #94=PROPERTY_DEFINITION_REPRESENTATION(#73,#10);
                #95=PROPERTY_DEFINITION_REPRESENTATION(#74,#10);
                #96=PROPERTY_DEFINITION_REPRESENTATION(#75,#10);
                #97=PROPERTY_DEFINITION_REPRESENTATION(#76,#10);
                #98=PROPERTY_DEFINITION_REPRESENTATION(#77,#10);
                #99=PROPERTY_DEFINITION_REPRESENTATION(#78,#10);
                #100=PROPERTY_DEFINITION_REPRESENTATION(#79,#10);
                #101=PROPERTY_DEFINITION_REPRESENTATION(#80,#10);
                #141=PROPERTY_DEFINITION('PD_SP','',#53);
                #142=PROPERTY_DEFINITION('PD_F','',#54);
                #143=PROPERTY_DEFINITION('PD_S','',#55);
                #144=PROPERTY_DEFINITION('PD_BC','',#56);
                #145=PROPERTY_DEFINITION('PD_BS','',#57);
                #146=PROPERTY_DEFINITION_REPRESENTATION(#141,#10);
                #147=PROPERTY_DEFINITION_REPRESENTATION(#142,#10);
                #148=PROPERTY_DEFINITION_REPRESENTATION(#143,#10);
                #149=PROPERTY_DEFINITION_REPRESENTATION(#144,#10);
                #150=PROPERTY_DEFINITION_REPRESENTATION(#145,#10);
                #102=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);
                #103=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #104=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #105=DIRECTION('TZ',(0.0,0.0,1.0));
                #106=DIRECTION('TX',(1.0,0.0,0.0));
                #107=AXIS2_PLACEMENT_3D('AX0',#103,#105,#106);
                #108=AXIS2_PLACEMENT_3D('AX1',#104,#105,#106);
                #109=ITEM_DEFINED_TRANSFORMATION('T1','',#107,#108);
                #110=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#109));
                #111=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);
                #112=CARTESIAN_POINT('NOTE_P',(0.0,0.0,0.0));
                #113=ANNOTATION_TEXT_OCCURRENCE('NOTE_TOPO','direct topology link',#112);
                #120=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#19,#10,#113,#8);
                #121=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#25,#10,#113,#8);
                #122=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#26,#10,#113,#8);
                #123=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#28,#10,#113,#8);
                #124=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#29,#10,#113,#8);
                #125=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#30,#10,#113,#8);
                #126=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#33,#10,#113,#8);
                #127=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#34,#10,#113,#8);
                #128=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#35,#10,#113,#8);
                #129=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#36,#10,#113,#8);
                #130=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#41,#10,#113,#8);
                #131=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#43,#10,#113,#8);
                #132=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#44,#10,#113,#8);
                #133=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#45,#10,#113,#8);
                #134=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#46,#10,#113,#8);
                #135=PMI_REQUIREMENT_ITEM_ASSOCIATION('A15','',#47,#10,#113,#8);
                #136=PMI_REQUIREMENT_ITEM_ASSOCIATION('A16','',#48,#10,#113,#8);
                #137=PMI_REQUIREMENT_ITEM_ASSOCIATION('A17','',#49,#10,#113,#8);
                #138=PMI_REQUIREMENT_ITEM_ASSOCIATION('A18','',#50,#10,#113,#8);
                #139=PMI_REQUIREMENT_ITEM_ASSOCIATION('A19','',#51,#10,#113,#8);
                #140=PMI_REQUIREMENT_ITEM_ASSOCIATION('A20','',#52,#10,#113,#8);
                #151=PMI_REQUIREMENT_ITEM_ASSOCIATION('A21','',#53,#10,#113,#8);
                #152=PMI_REQUIREMENT_ITEM_ASSOCIATION('A22','',#54,#10,#113,#8);
                #153=PMI_REQUIREMENT_ITEM_ASSOCIATION('A23','',#55,#10,#113,#8);
                #154=PMI_REQUIREMENT_ITEM_ASSOCIATION('A24','',#56,#10,#113,#8);
                #155=PMI_REQUIREMENT_ITEM_ASSOCIATION('A25','',#57,#10,#113,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughDirectSolidAndProfileLinkDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=SHAPE_REPRESENTATION('REP_A',(),#9);
                #11=SHAPE_REPRESENTATION('REP_B',(),#9);
                #12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #13=DIRECTION('DZ',(0.0,0.0,1.0));
                #14=DIRECTION('DX',(1.0,0.0,0.0));
                #15=AXIS2_PLACEMENT_3D('AX3',#12,#13,#14);
                #16=AXIS2_PLACEMENT_2D('AX2',#12,#14);
                #17=BLOCK('BLK0',#15,1.0,2.0,3.0);
                #18=CLOSED_SHELL('CS0',());
                #19=MANIFOLD_SOLID_BREP('MSB0',#18);
                #20=BREP_WITH_VOIDS('BV0',#18,());
                #21=RECTANGLE_PROFILE_DEF(.AREA.,'RPD',#16,1.0,2.0);
                #22=EXTRUDED_AREA_SOLID('EAS0',#21,#15,#13,4.0);
                #23=AXIS1_PLACEMENT('AX1',#12,#13);
                #24=REVOLVED_AREA_SOLID('RAS0',#21,#15,#23,1.57079632679);
                #25=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T3',#14,#13,#12,1.0,#14);
                #26=SOLID_REPLICA('SR0',#19,#25);
                #27=HALF_SPACE_SOLID('HS0',#28,.F.);
                #28=PLANE('PL0',#15);
                #29=CSG_SOLID('CSG0',#17);
                #30=(BOOLEAN_RESULT(.UNION.,#19,#29) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BR0'));
                #31=(BOOLEAN_CLIPPING_RESULT(.DIFFERENCE.,#29,#19) BOOLEAN_RESULT(.DIFFERENCE.,#29,#19) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BCR0'));
                #130=SPHERE('SP0',#15,2.0);
                #131=RIGHT_CIRCULAR_CYLINDER('RCY0',#23,5.0,2.0);
                #132=TORUS('TOR0',#23,5.0,1.0);
                #133=RIGHT_ANGULAR_WEDGE('WED0',#15,4.0,3.0,2.0,2.5);
                #134=CIRCLE_PROFILE_DEF(.AREA.,'C',#16,2.0);
                #135=CENTERED_RECTANGLE_PROFILE_DEF(.AREA.,'CR',#16,3.0,5.0);
                #136=ELLIPSE_PROFILE_DEF(.AREA.,'E',#16,3.0,1.5);
                #137=ROUNDED_RECTANGLE_PROFILE_DEF(.AREA.,'RR',#16,6.0,4.0,0.5);
                #138=CIRCULAR_HOLLOW_PROFILE_DEF(.AREA.,'CH',#16,3.0,0.5);
                #139=POLYLINE('PLC',(#12,#12,#12,#12));
                #140=POLYLINE('PLO',(#12,#12,#12));
                #141=ARBITRARY_CLOSED_PROFILE_DEF(.AREA.,'ACP',#139);
                #142=ARBITRARY_PROFILE_DEF(.AREA.,'AP',#139);
                #143=ARBITRARY_OPEN_PROFILE_DEF(.CURVE.,'AOP',#140);
                #230=PROPERTY_DEFINITION('PD_BLK','',#17);
                #231=PROPERTY_DEFINITION('PD_MSB','',#19);
                #232=PROPERTY_DEFINITION('PD_BV','',#20);
                #233=PROPERTY_DEFINITION('PD_RPD','',#21);
                #234=PROPERTY_DEFINITION('PD_EAS','',#22);
                #235=PROPERTY_DEFINITION('PD_RAS','',#24);
                #236=PROPERTY_DEFINITION('PD_SR','',#26);
                #237=PROPERTY_DEFINITION('PD_HS','',#27);
                #238=PROPERTY_DEFINITION('PD_CSG','',#29);
                #239=PROPERTY_DEFINITION('PD_BR','',#30);
                #240=PROPERTY_DEFINITION('PD_BCR','',#31);
                #145=PROPERTY_DEFINITION('PD_SPH','',#130);
                #146=PROPERTY_DEFINITION('PD_RCY','',#131);
                #147=PROPERTY_DEFINITION('PD_TOR','',#132);
                #148=PROPERTY_DEFINITION('PD_WED','',#133);
                #149=PROPERTY_DEFINITION('PD_C','',#134);
                #150=PROPERTY_DEFINITION('PD_CR','',#135);
                #151=PROPERTY_DEFINITION('PD_E','',#136);
                #152=PROPERTY_DEFINITION('PD_RR','',#137);
                #153=PROPERTY_DEFINITION('PD_CH','',#138);
                #154=PROPERTY_DEFINITION('PD_ACP','',#141);
                #155=PROPERTY_DEFINITION('PD_AP','',#142);
                #156=PROPERTY_DEFINITION('PD_AOP','',#143);
                #241=PROPERTY_DEFINITION_REPRESENTATION(#230,#10);
                #242=PROPERTY_DEFINITION_REPRESENTATION(#231,#10);
                #243=PROPERTY_DEFINITION_REPRESENTATION(#232,#10);
                #244=PROPERTY_DEFINITION_REPRESENTATION(#233,#10);
                #245=PROPERTY_DEFINITION_REPRESENTATION(#234,#10);
                #246=PROPERTY_DEFINITION_REPRESENTATION(#235,#10);
                #247=PROPERTY_DEFINITION_REPRESENTATION(#236,#10);
                #248=PROPERTY_DEFINITION_REPRESENTATION(#237,#10);
                #249=PROPERTY_DEFINITION_REPRESENTATION(#238,#10);
                #250=PROPERTY_DEFINITION_REPRESENTATION(#239,#10);
                #251=PROPERTY_DEFINITION_REPRESENTATION(#240,#10);
                #157=PROPERTY_DEFINITION_REPRESENTATION(#145,#10);
                #158=PROPERTY_DEFINITION_REPRESENTATION(#146,#10);
                #159=PROPERTY_DEFINITION_REPRESENTATION(#147,#10);
                #160=PROPERTY_DEFINITION_REPRESENTATION(#148,#10);
                #161=PROPERTY_DEFINITION_REPRESENTATION(#149,#10);
                #162=PROPERTY_DEFINITION_REPRESENTATION(#150,#10);
                #163=PROPERTY_DEFINITION_REPRESENTATION(#151,#10);
                #164=PROPERTY_DEFINITION_REPRESENTATION(#152,#10);
                #165=PROPERTY_DEFINITION_REPRESENTATION(#153,#10);
                #166=PROPERTY_DEFINITION_REPRESENTATION(#154,#10);
                #167=PROPERTY_DEFINITION_REPRESENTATION(#155,#10);
                #168=PROPERTY_DEFINITION_REPRESENTATION(#156,#10);
                #51=REPRESENTATION_RELATIONSHIP('RR','',#10,#11);
                #52=CARTESIAN_POINT('TX0',(0.0,0.0,0.0));
                #53=CARTESIAN_POINT('TX1',(1.0,0.0,0.0));
                #54=DIRECTION('TZ',(0.0,0.0,1.0));
                #55=DIRECTION('TX',(1.0,0.0,0.0));
                #56=AXIS2_PLACEMENT_3D('AX0',#52,#54,#55);
                #57=AXIS2_PLACEMENT_3D('AX1',#53,#54,#55);
                #58=ITEM_DEFINED_TRANSFORMATION('T1','',#56,#57);
                #59=(REPRESENTATION_RELATIONSHIP('RRT','',#10,#11)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#58));
                #60=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','',#10,#11);
                #61=ANNOTATION_TEXT_OCCURRENCE('NOTE_SOLID','',#12);
                #62=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#17,#10,#61,#8);
                #63=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#19,#10,#61,#8);
                #64=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#20,#10,#61,#8);
                #65=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#21,#10,#61,#8);
                #66=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#22,#10,#61,#8);
                #67=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#24,#10,#61,#8);
                #68=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#26,#10,#61,#8);
                #69=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#27,#10,#61,#8);
                #70=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#29,#10,#61,#8);
                #71=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#30,#10,#61,#8);
                #169=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#130,#10,#61,#8);
                #170=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#131,#10,#61,#8);
                #171=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#132,#10,#61,#8);
                #172=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#133,#10,#61,#8);
                #173=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#134,#10,#61,#8);
                #174=PMI_REQUIREMENT_ITEM_ASSOCIATION('A15','',#135,#10,#61,#8);
                #175=PMI_REQUIREMENT_ITEM_ASSOCIATION('A16','',#136,#10,#61,#8);
                #176=PMI_REQUIREMENT_ITEM_ASSOCIATION('A17','',#137,#10,#61,#8);
                #177=PMI_REQUIREMENT_ITEM_ASSOCIATION('A18','',#138,#10,#61,#8);
                #178=PMI_REQUIREMENT_ITEM_ASSOCIATION('A19','',#141,#10,#61,#8);
                #179=PMI_REQUIREMENT_ITEM_ASSOCIATION('A20','',#142,#10,#61,#8);
                #180=PMI_REQUIREMENT_ITEM_ASSOCIATION('A21','',#143,#10,#61,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
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
    void shouldBindPmiTargetsThroughMeasureAndUnitEntities() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=REPRESENTATION('REP_UNIT_CHAIN',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));
                #13=MEASURE_WITH_UNIT(LENGTH_MEASURE(12.5),#12);
                #14=(CONVERSION_BASED_UNIT('DEGREE',#13) NAMED_UNIT(*) PLANE_ANGLE_UNIT());
                #15=(THERMODYNAMIC_TEMPERATURE_UNIT() NAMED_UNIT(*) SI_UNIT($,.KELVIN.));
                #16=(CONVERSION_BASED_UNIT_WITH_OFFSET(THERMODYNAMIC_TEMPERATURE_MEASURE(273.15))
                    CONVERSION_BASED_UNIT('DEG_C',#13)
                    NAMED_UNIT(*)
                    THERMODYNAMIC_TEMPERATURE_UNIT());
                #17=(CONTEXT_DEPENDENT_UNIT('BOX') NAMED_UNIT(*));
                #18=(FORCE_UNIT() NAMED_UNIT(*) SI_UNIT($,.NEWTON.));
                #19=DERIVED_UNIT_ELEMENT(#18,1.0);
                #20=DERIVED_UNIT((#19));
                #21=UNCERTAINTY_MEASURE_WITH_UNIT(LENGTH_MEASURE(0.01),#12,'distance_accuracy_value','confusion');
                #22=LENGTH_MEASURE_WITH_UNIT(LENGTH_MEASURE(3.5),#12);
                #23=PLANE_ANGLE_MEASURE_WITH_UNIT(PLANE_ANGLE_MEASURE(0.25),#14);
                #24=PROPERTY_DEFINITION('PD_MWU','',#13);
                #25=PROPERTY_DEFINITION('PD_TYPED','',#14);
                #26=PROPERTY_DEFINITION('PD_OFFSET','',#16);
                #27=PROPERTY_DEFINITION('PD_CTX_UNIT','',#17);
                #28=PROPERTY_DEFINITION('PD_DERIVED','',#20);
                #29=PROPERTY_DEFINITION('PD_UNCERTAINTY','',#21);
                #30=PROPERTY_DEFINITION('PD_LEN_TYPED','',#22);
                #31=PROPERTY_DEFINITION('PD_ANGLE_TYPED','',#23);
                #32=PROPERTY_DEFINITION_REPRESENTATION(#24,#10);
                #33=PROPERTY_DEFINITION_REPRESENTATION(#25,#10);
                #34=PROPERTY_DEFINITION_REPRESENTATION(#26,#10);
                #35=PROPERTY_DEFINITION_REPRESENTATION(#27,#10);
                #36=PROPERTY_DEFINITION_REPRESENTATION(#28,#10);
                #37=PROPERTY_DEFINITION_REPRESENTATION(#29,#10);
                #38=PROPERTY_DEFINITION_REPRESENTATION(#30,#10);
                #39=PROPERTY_DEFINITION_REPRESENTATION(#31,#10);
                #70=MASS_UNIT();
                #71=TIME_UNIT();
                #72=AREA_UNIT();
                #73=VOLUME_UNIT();
                #74=(SOLID_ANGLE_UNIT() NAMED_UNIT(*) SI_UNIT($,.STERADIAN.));
                #75=RATIO_UNIT();
                #76=MASS_MEASURE_WITH_UNIT(MASS_MEASURE(3.0),#70);
                #77=TIME_MEASURE_WITH_UNIT(TIME_MEASURE(2.0),#71);
                #78=AREA_MEASURE_WITH_UNIT(AREA_MEASURE(6.0),#72);
                #79=VOLUME_MEASURE_WITH_UNIT(VOLUME_MEASURE(7.0),#73);
                #80=SOLID_ANGLE_MEASURE_WITH_UNIT(SOLID_ANGLE_MEASURE(1.5),#74);
                #81=RATIO_MEASURE_WITH_UNIT(RATIO_MEASURE(0.25),#75);
                #82=PROPERTY_DEFINITION('PD_MASS_TYPED','',#76);
                #83=PROPERTY_DEFINITION('PD_TIME_TYPED','',#77);
                #84=PROPERTY_DEFINITION('PD_AREA_TYPED','',#78);
                #85=PROPERTY_DEFINITION('PD_VOLUME_TYPED','',#79);
                #86=PROPERTY_DEFINITION('PD_SOLID_ANGLE_TYPED','',#80);
                #87=PROPERTY_DEFINITION('PD_RATIO_TYPED','',#81);
                #88=PROPERTY_DEFINITION_REPRESENTATION(#82,#10);
                #89=PROPERTY_DEFINITION_REPRESENTATION(#83,#10);
                #90=PROPERTY_DEFINITION_REPRESENTATION(#84,#10);
                #91=PROPERTY_DEFINITION_REPRESENTATION(#85,#10);
                #92=PROPERTY_DEFINITION_REPRESENTATION(#86,#10);
                #93=PROPERTY_DEFINITION_REPRESENTATION(#87,#10);
                #112=FREQUENCY_UNIT();
                #113=FORCE_UNIT();
                #114=PRESSURE_UNIT();
                #115=ENERGY_UNIT();
                #116=POWER_UNIT();
                #117=ELECTRIC_POTENTIAL_UNIT();
                #118=RESISTANCE_UNIT();
                #119=CONDUCTANCE_UNIT();
                #120=MAGNETIC_FLUX_UNIT();
                #121=ILLUMINANCE_UNIT();
                #122=LUMINOUS_FLUX_UNIT();
                #123=LUMINOUS_INTENSITY_UNIT();
                #124=FREQUENCY_MEASURE_WITH_UNIT(FREQUENCY_MEASURE(50.0),#112);
                #125=FORCE_MEASURE_WITH_UNIT(FORCE_MEASURE(100.0),#113);
                #126=PRESSURE_MEASURE_WITH_UNIT(PRESSURE_MEASURE(1.5),#114);
                #127=ENERGY_MEASURE_WITH_UNIT(ENERGY_MEASURE(42.0),#115);
                #128=POWER_MEASURE_WITH_UNIT(POWER_MEASURE(3.5),#116);
                #129=ELECTRIC_POTENTIAL_MEASURE_WITH_UNIT(ELECTRIC_POTENTIAL_MEASURE(220.0),#117);
                #130=RESISTANCE_MEASURE_WITH_UNIT(RESISTANCE_MEASURE(10.0),#118);
                #131=CONDUCTANCE_MEASURE_WITH_UNIT(CONDUCTANCE_MEASURE(0.1),#119);
                #132=MAGNETIC_FLUX_MEASURE_WITH_UNIT(MAGNETIC_FLUX_MEASURE(0.02),#120);
                #133=ILLUMINANCE_MEASURE_WITH_UNIT(ILLUMINANCE_MEASURE(500.0),#121);
                #134=LUMINOUS_FLUX_MEASURE_WITH_UNIT(LUMINOUS_FLUX_MEASURE(800.0),#122);
                #135=LUMINOUS_INTENSITY_MEASURE_WITH_UNIT(LUMINOUS_INTENSITY_MEASURE(120.0),#123);
                #136=PROPERTY_DEFINITION('PD_FREQ_TYPED','',#124);
                #137=PROPERTY_DEFINITION('PD_FORCE_TYPED','',#125);
                #138=PROPERTY_DEFINITION('PD_PRESSURE_TYPED','',#126);
                #139=PROPERTY_DEFINITION('PD_ENERGY_TYPED','',#127);
                #140=PROPERTY_DEFINITION('PD_POWER_TYPED','',#128);
                #141=PROPERTY_DEFINITION('PD_ELECTRIC_POTENTIAL_TYPED','',#129);
                #142=PROPERTY_DEFINITION('PD_RESISTANCE_TYPED','',#130);
                #143=PROPERTY_DEFINITION('PD_CONDUCTANCE_TYPED','',#131);
                #144=PROPERTY_DEFINITION('PD_MAGNETIC_FLUX_TYPED','',#132);
                #145=PROPERTY_DEFINITION('PD_ILLUMINANCE_TYPED','',#133);
                #146=PROPERTY_DEFINITION('PD_LUMINOUS_FLUX_TYPED','',#134);
                #147=PROPERTY_DEFINITION('PD_LUMINOUS_INTENSITY_TYPED','',#135);
                #148=PROPERTY_DEFINITION_REPRESENTATION(#136,#10);
                #149=PROPERTY_DEFINITION_REPRESENTATION(#137,#10);
                #150=PROPERTY_DEFINITION_REPRESENTATION(#138,#10);
                #151=PROPERTY_DEFINITION_REPRESENTATION(#139,#10);
                #152=PROPERTY_DEFINITION_REPRESENTATION(#140,#10);
                #153=PROPERTY_DEFINITION_REPRESENTATION(#141,#10);
                #154=PROPERTY_DEFINITION_REPRESENTATION(#142,#10);
                #155=PROPERTY_DEFINITION_REPRESENTATION(#143,#10);
                #156=PROPERTY_DEFINITION_REPRESENTATION(#144,#10);
                #157=PROPERTY_DEFINITION_REPRESENTATION(#145,#10);
                #158=PROPERTY_DEFINITION_REPRESENTATION(#146,#10);
                #159=PROPERTY_DEFINITION_REPRESENTATION(#147,#10);
                #196=AMOUNT_OF_SUBSTANCE_UNIT();
                #197=ELECTRIC_CHARGE_UNIT();
                #198=CAPACITANCE_UNIT();
                #199=MAGNETIC_FLUX_DENSITY_UNIT();
                #200=INDUCTANCE_UNIT();
                #201=RADIOACTIVITY_UNIT();
                #202=ABSORBED_DOSE_UNIT();
                #203=DOSE_EQUIVALENT_UNIT();
                #204=ACCELERATION_UNIT();
                #205=VELOCITY_UNIT();
                #206=THERMAL_RESISTANCE_UNIT();
                #207=AMOUNT_OF_SUBSTANCE_MEASURE_WITH_UNIT(AMOUNT_OF_SUBSTANCE_MEASURE(2.5),#196);
                #208=ELECTRIC_CHARGE_MEASURE_WITH_UNIT(ELECTRIC_CHARGE_MEASURE(1.6),#197);
                #209=CAPACITANCE_MEASURE_WITH_UNIT(CAPACITANCE_MEASURE(0.047),#198);
                #210=MAGNETIC_FLUX_DENSITY_MEASURE_WITH_UNIT(MAGNETIC_FLUX_DENSITY_MEASURE(0.12),#199);
                #211=INDUCTANCE_MEASURE_WITH_UNIT(INDUCTANCE_MEASURE(0.008),#200);
                #212=RADIOACTIVITY_MEASURE_WITH_UNIT(RADIOACTIVITY_MEASURE(3.0),#201);
                #213=ABSORBED_DOSE_MEASURE_WITH_UNIT(ABSORBED_DOSE_MEASURE(0.4),#202);
                #214=DOSE_EQUIVALENT_MEASURE_WITH_UNIT(DOSE_EQUIVALENT_MEASURE(0.6),#203);
                #215=ACCELERATION_MEASURE_WITH_UNIT(ACCELERATION_MEASURE(9.81),#204);
                #216=VELOCITY_MEASURE_WITH_UNIT(VELOCITY_MEASURE(12.0),#205);
                #217=THERMAL_RESISTANCE_MEASURE_WITH_UNIT(THERMAL_RESISTANCE_MEASURE(0.15),#206);
                #218=PROPERTY_DEFINITION('PD_AMOUNT_TYPED','',#207);
                #219=PROPERTY_DEFINITION('PD_CHARGE_TYPED','',#208);
                #220=PROPERTY_DEFINITION('PD_CAPACITANCE_TYPED','',#209);
                #221=PROPERTY_DEFINITION('PD_FLUX_DENSITY_TYPED','',#210);
                #222=PROPERTY_DEFINITION('PD_INDUCTANCE_TYPED','',#211);
                #223=PROPERTY_DEFINITION('PD_RADIOACTIVITY_TYPED','',#212);
                #224=PROPERTY_DEFINITION('PD_ABSORBED_DOSE_TYPED','',#213);
                #225=PROPERTY_DEFINITION('PD_DOSE_EQUIVALENT_TYPED','',#214);
                #226=PROPERTY_DEFINITION('PD_ACCELERATION_TYPED','',#215);
                #227=PROPERTY_DEFINITION('PD_VELOCITY_TYPED','',#216);
                #228=PROPERTY_DEFINITION('PD_THERMAL_RESISTANCE_TYPED','',#217);
                #229=PROPERTY_DEFINITION_REPRESENTATION(#218,#10);
                #230=PROPERTY_DEFINITION_REPRESENTATION(#219,#10);
                #231=PROPERTY_DEFINITION_REPRESENTATION(#220,#10);
                #232=PROPERTY_DEFINITION_REPRESENTATION(#221,#10);
                #233=PROPERTY_DEFINITION_REPRESENTATION(#222,#10);
                #234=PROPERTY_DEFINITION_REPRESENTATION(#223,#10);
                #235=PROPERTY_DEFINITION_REPRESENTATION(#224,#10);
                #236=PROPERTY_DEFINITION_REPRESENTATION(#225,#10);
                #237=PROPERTY_DEFINITION_REPRESENTATION(#226,#10);
                #238=PROPERTY_DEFINITION_REPRESENTATION(#227,#10);
                #239=PROPERTY_DEFINITION_REPRESENTATION(#228,#10);
                #40=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #41=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #42=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #43=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #44=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #45=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #46=CARTESIAN_POINT('P6',(6.0,0.0,0.0));
                #47=CARTESIAN_POINT('P7',(7.0,0.0,0.0));
                #48=ANNOTATION_TEXT_OCCURRENCE('NOTE_MWU','',#40);
                #49=ANNOTATION_TEXT_OCCURRENCE('NOTE_TYPED_UNIT','',#41);
                #50=ANNOTATION_TEXT_OCCURRENCE('NOTE_OFFSET_UNIT','',#42);
                #51=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONTEXT_UNIT','',#43);
                #52=ANNOTATION_TEXT_OCCURRENCE('NOTE_DERIVED_UNIT','',#44);
                #53=ANNOTATION_TEXT_OCCURRENCE('NOTE_UNCERTAINTY','',#45);
                #54=ANNOTATION_TEXT_OCCURRENCE('NOTE_LENGTH_TYPED','',#46);
                #55=ANNOTATION_TEXT_OCCURRENCE('NOTE_ANGLE_TYPED','',#47);
                #94=CARTESIAN_POINT('P8',(8.0,0.0,0.0));
                #95=CARTESIAN_POINT('P9',(9.0,0.0,0.0));
                #96=CARTESIAN_POINT('P10',(10.0,0.0,0.0));
                #97=CARTESIAN_POINT('P11',(11.0,0.0,0.0));
                #98=CARTESIAN_POINT('P12',(12.0,0.0,0.0));
                #99=CARTESIAN_POINT('P13',(13.0,0.0,0.0));
                #100=ANNOTATION_TEXT_OCCURRENCE('NOTE_MASS_TYPED','',#94);
                #101=ANNOTATION_TEXT_OCCURRENCE('NOTE_TIME_TYPED','',#95);
                #102=ANNOTATION_TEXT_OCCURRENCE('NOTE_AREA_TYPED','',#96);
                #103=ANNOTATION_TEXT_OCCURRENCE('NOTE_VOLUME_TYPED','',#97);
                #104=ANNOTATION_TEXT_OCCURRENCE('NOTE_SOLID_ANGLE_TYPED','',#98);
                #105=ANNOTATION_TEXT_OCCURRENCE('NOTE_RATIO_TYPED','',#99);
                #160=CARTESIAN_POINT('P14',(14.0,0.0,0.0));
                #161=CARTESIAN_POINT('P15',(15.0,0.0,0.0));
                #162=CARTESIAN_POINT('P16',(16.0,0.0,0.0));
                #163=CARTESIAN_POINT('P17',(17.0,0.0,0.0));
                #164=CARTESIAN_POINT('P18',(18.0,0.0,0.0));
                #165=CARTESIAN_POINT('P19',(19.0,0.0,0.0));
                #166=CARTESIAN_POINT('P20',(20.0,0.0,0.0));
                #167=CARTESIAN_POINT('P21',(21.0,0.0,0.0));
                #168=CARTESIAN_POINT('P22',(22.0,0.0,0.0));
                #169=CARTESIAN_POINT('P23',(23.0,0.0,0.0));
                #170=CARTESIAN_POINT('P24',(24.0,0.0,0.0));
                #171=CARTESIAN_POINT('P25',(25.0,0.0,0.0));
                #172=ANNOTATION_TEXT_OCCURRENCE('NOTE_FREQUENCY_TYPED','',#160);
                #173=ANNOTATION_TEXT_OCCURRENCE('NOTE_FORCE_TYPED','',#161);
                #174=ANNOTATION_TEXT_OCCURRENCE('NOTE_PRESSURE_TYPED','',#162);
                #175=ANNOTATION_TEXT_OCCURRENCE('NOTE_ENERGY_TYPED','',#163);
                #176=ANNOTATION_TEXT_OCCURRENCE('NOTE_POWER_TYPED','',#164);
                #177=ANNOTATION_TEXT_OCCURRENCE('NOTE_ELECTRIC_POTENTIAL_TYPED','',#165);
                #178=ANNOTATION_TEXT_OCCURRENCE('NOTE_RESISTANCE_TYPED','',#166);
                #179=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONDUCTANCE_TYPED','',#167);
                #180=ANNOTATION_TEXT_OCCURRENCE('NOTE_MAGNETIC_FLUX_TYPED','',#168);
                #181=ANNOTATION_TEXT_OCCURRENCE('NOTE_ILLUMINANCE_TYPED','',#169);
                #182=ANNOTATION_TEXT_OCCURRENCE('NOTE_LUMINOUS_FLUX_TYPED','',#170);
                #183=ANNOTATION_TEXT_OCCURRENCE('NOTE_LUMINOUS_INTENSITY_TYPED','',#171);
                #240=CARTESIAN_POINT('P26',(26.0,0.0,0.0));
                #241=CARTESIAN_POINT('P27',(27.0,0.0,0.0));
                #242=CARTESIAN_POINT('P28',(28.0,0.0,0.0));
                #243=CARTESIAN_POINT('P29',(29.0,0.0,0.0));
                #244=CARTESIAN_POINT('P30',(30.0,0.0,0.0));
                #245=CARTESIAN_POINT('P31',(31.0,0.0,0.0));
                #246=CARTESIAN_POINT('P32',(32.0,0.0,0.0));
                #247=CARTESIAN_POINT('P33',(33.0,0.0,0.0));
                #248=CARTESIAN_POINT('P34',(34.0,0.0,0.0));
                #249=CARTESIAN_POINT('P35',(35.0,0.0,0.0));
                #250=CARTESIAN_POINT('P36',(36.0,0.0,0.0));
                #251=ANNOTATION_TEXT_OCCURRENCE('NOTE_AMOUNT_TYPED','',#240);
                #252=ANNOTATION_TEXT_OCCURRENCE('NOTE_CHARGE_TYPED','',#241);
                #253=ANNOTATION_TEXT_OCCURRENCE('NOTE_CAPACITANCE_TYPED','',#242);
                #254=ANNOTATION_TEXT_OCCURRENCE('NOTE_FLUX_DENSITY_TYPED','',#243);
                #255=ANNOTATION_TEXT_OCCURRENCE('NOTE_INDUCTANCE_TYPED','',#244);
                #256=ANNOTATION_TEXT_OCCURRENCE('NOTE_RADIOACTIVITY_TYPED','',#245);
                #257=ANNOTATION_TEXT_OCCURRENCE('NOTE_ABSORBED_DOSE_TYPED','',#246);
                #258=ANNOTATION_TEXT_OCCURRENCE('NOTE_DOSE_EQUIVALENT_TYPED','',#247);
                #259=ANNOTATION_TEXT_OCCURRENCE('NOTE_ACCELERATION_TYPED','',#248);
                #260=ANNOTATION_TEXT_OCCURRENCE('NOTE_VELOCITY_TYPED','',#249);
                #261=ANNOTATION_TEXT_OCCURRENCE('NOTE_THERMAL_RESISTANCE_TYPED','',#250);
                #56=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#13,#10,#48,#8);
                #57=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#14,#10,#49,#8);
                #58=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#16,#10,#50,#8);
                #59=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#17,#10,#51,#8);
                #60=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#20,#10,#52,#8);
                #61=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#21,#10,#53,#8);
                #62=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#22,#10,#54,#8);
                #63=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#23,#10,#55,#8);
                #106=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#76,#10,#100,#8);
                #107=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#77,#10,#101,#8);
                #108=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#78,#10,#102,#8);
                #109=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#79,#10,#103,#8);
                #110=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#80,#10,#104,#8);
                #111=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#81,#10,#105,#8);
                #184=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#124,#10,#172,#8);
                #185=PMI_REQUIREMENT_ITEM_ASSOCIATION('A15','',#125,#10,#173,#8);
                #186=PMI_REQUIREMENT_ITEM_ASSOCIATION('A16','',#126,#10,#174,#8);
                #187=PMI_REQUIREMENT_ITEM_ASSOCIATION('A17','',#127,#10,#175,#8);
                #188=PMI_REQUIREMENT_ITEM_ASSOCIATION('A18','',#128,#10,#176,#8);
                #189=PMI_REQUIREMENT_ITEM_ASSOCIATION('A19','',#129,#10,#177,#8);
                #190=PMI_REQUIREMENT_ITEM_ASSOCIATION('A20','',#130,#10,#178,#8);
                #191=PMI_REQUIREMENT_ITEM_ASSOCIATION('A21','',#131,#10,#179,#8);
                #192=PMI_REQUIREMENT_ITEM_ASSOCIATION('A22','',#132,#10,#180,#8);
                #193=PMI_REQUIREMENT_ITEM_ASSOCIATION('A23','',#133,#10,#181,#8);
                #194=PMI_REQUIREMENT_ITEM_ASSOCIATION('A24','',#134,#10,#182,#8);
                #195=PMI_REQUIREMENT_ITEM_ASSOCIATION('A25','',#135,#10,#183,#8);
                #262=PMI_REQUIREMENT_ITEM_ASSOCIATION('A26','',#207,#10,#251,#8);
                #263=PMI_REQUIREMENT_ITEM_ASSOCIATION('A27','',#208,#10,#252,#8);
                #264=PMI_REQUIREMENT_ITEM_ASSOCIATION('A28','',#209,#10,#253,#8);
                #265=PMI_REQUIREMENT_ITEM_ASSOCIATION('A29','',#210,#10,#254,#8);
                #266=PMI_REQUIREMENT_ITEM_ASSOCIATION('A30','',#211,#10,#255,#8);
                #267=PMI_REQUIREMENT_ITEM_ASSOCIATION('A31','',#212,#10,#256,#8);
                #268=PMI_REQUIREMENT_ITEM_ASSOCIATION('A32','',#213,#10,#257,#8);
                #269=PMI_REQUIREMENT_ITEM_ASSOCIATION('A33','',#214,#10,#258,#8);
                #270=PMI_REQUIREMENT_ITEM_ASSOCIATION('A34','',#215,#10,#259,#8);
                #271=PMI_REQUIREMENT_ITEM_ASSOCIATION('A35','',#216,#10,#260,#8);
                #272=PMI_REQUIREMENT_ITEM_ASSOCIATION('A36','',#217,#10,#261,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_MWU\"",
                "\"name\":\"NOTE_TYPED_UNIT\"",
                "\"name\":\"NOTE_OFFSET_UNIT\"",
                "\"name\":\"NOTE_CONTEXT_UNIT\"",
                "\"name\":\"NOTE_DERIVED_UNIT\"",
                "\"name\":\"NOTE_UNCERTAINTY\"",
                "\"name\":\"NOTE_LENGTH_TYPED\"",
                "\"name\":\"NOTE_ANGLE_TYPED\"",
                "\"name\":\"NOTE_MASS_TYPED\"",
                "\"name\":\"NOTE_TIME_TYPED\"",
                "\"name\":\"NOTE_AREA_TYPED\"",
                "\"name\":\"NOTE_VOLUME_TYPED\"",
                "\"name\":\"NOTE_SOLID_ANGLE_TYPED\"",
                "\"name\":\"NOTE_RATIO_TYPED\"",
                "\"name\":\"NOTE_FREQUENCY_TYPED\"",
                "\"name\":\"NOTE_FORCE_TYPED\"",
                "\"name\":\"NOTE_PRESSURE_TYPED\"",
                "\"name\":\"NOTE_ENERGY_TYPED\"",
                "\"name\":\"NOTE_POWER_TYPED\"",
                "\"name\":\"NOTE_ELECTRIC_POTENTIAL_TYPED\"",
                "\"name\":\"NOTE_RESISTANCE_TYPED\"",
                "\"name\":\"NOTE_CONDUCTANCE_TYPED\"",
                "\"name\":\"NOTE_MAGNETIC_FLUX_TYPED\"",
                "\"name\":\"NOTE_ILLUMINANCE_TYPED\"",
                "\"name\":\"NOTE_LUMINOUS_FLUX_TYPED\"",
                "\"name\":\"NOTE_LUMINOUS_INTENSITY_TYPED\"",
                "\"name\":\"NOTE_AMOUNT_TYPED\"",
                "\"name\":\"NOTE_CHARGE_TYPED\"",
                "\"name\":\"NOTE_CAPACITANCE_TYPED\"",
                "\"name\":\"NOTE_FLUX_DENSITY_TYPED\"",
                "\"name\":\"NOTE_INDUCTANCE_TYPED\"",
                "\"name\":\"NOTE_RADIOACTIVITY_TYPED\"",
                "\"name\":\"NOTE_ABSORBED_DOSE_TYPED\"",
                "\"name\":\"NOTE_DOSE_EQUIVALENT_TYPED\"",
                "\"name\":\"NOTE_ACCELERATION_TYPED\"",
                "\"name\":\"NOTE_VELOCITY_TYPED\"",
                "\"name\":\"NOTE_THERMAL_RESISTANCE_TYPED\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_UNIT_CHAIN\"",
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
    void shouldBindPmiTargetsThroughRepresentationContextsTransformationsAndMarkerItems() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=REPRESENTATION_CONTEXT('GEN','GENERAL');
                #10=REPRESENTATION('REP_CONTEXT_ONLY',(),#9);
                #11=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));
                #12=UNCERTAINTY_MEASURE_WITH_UNIT(LENGTH_MEASURE(0.01),#11,'distance_accuracy_value','confusion');
                #13=(GEOMETRIC_REPRESENTATION_CONTEXT(3)
                    GLOBAL_UNIT_ASSIGNED_CONTEXT((#11))
                    GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT((#12))
                    REPRESENTATION_CONTEXT('GEO','MODEL'));
                #14=REPRESENTATION('REP_GEOM_CONTEXT',(),#13);
                #15=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #16=SHAPE_DEFINITION_REPRESENTATION(#7,#14);
                #17=CARTESIAN_POINT('O0',(0.0,0.0,0.0));
                #18=DIRECTION('X0',(1.0,0.0,0.0));
                #19=DIRECTION('Y0',(0.0,1.0,0.0));
                #20=AXIS2_PLACEMENT_3D('A0',#17,#18,#19);
                #21=CARTESIAN_POINT('O1',(5.0,0.0,0.0));
                #22=AXIS2_PLACEMENT_3D('A1',#21,#18,#19);
                #23=ITEM_DEFINED_TRANSFORMATION('MOVE','translate x',#20,#22);
                #24=(REPRESENTATION_RELATIONSHIP('RWT','',#10,#14)
                    REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#23));
                #25=REPRESENTATION_ITEM('REP_ITEM_ONLY');
                #26=PROPERTY_DEFINITION('PD_REP_ITEM','',#25);
                #27=PROPERTY_DEFINITION_REPRESENTATION(#26,#10);
                #28=(GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('GEOM_ITEM_ONLY'));
                #29=PROPERTY_DEFINITION('PD_GEOM_ITEM','',#28);
                #30=PROPERTY_DEFINITION_REPRESENTATION(#29,#14);
                #31=(TOPOLOGICAL_REPRESENTATION_ITEM('TOPO_ITEM_ONLY'));
                #32=PROPERTY_DEFINITION('PD_TOPO_ITEM','',#31);
                #33=PROPERTY_DEFINITION_REPRESENTATION(#32,#10);
                #40=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #41=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #42=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #43=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #44=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #45=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #46=CARTESIAN_POINT('P6',(6.0,0.0,0.0));
                #47=CARTESIAN_POINT('P7',(7.0,0.0,0.0));
                #48=ANNOTATION_TEXT_OCCURRENCE('NOTE_REP_CONTEXT','',#40);
                #49=ANNOTATION_TEXT_OCCURRENCE('NOTE_GEOM_CONTEXT','',#41);
                #50=ANNOTATION_TEXT_OCCURRENCE('NOTE_SI_UNIT','',#42);
                #51=ANNOTATION_TEXT_OCCURRENCE('NOTE_UNCERTAINTY_CTX','',#43);
                #52=ANNOTATION_TEXT_OCCURRENCE('NOTE_TRANSFORM','',#44);
                #53=ANNOTATION_TEXT_OCCURRENCE('NOTE_REP_ITEM','',#45);
                #54=ANNOTATION_TEXT_OCCURRENCE('NOTE_GEOM_ITEM','',#46);
                #55=ANNOTATION_TEXT_OCCURRENCE('NOTE_TOPO_ITEM','',#47);
                #56=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#9,#10,#48,#8);
                #57=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#13,#14,#49,#8);
                #58=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#11,#14,#50,#8);
                #59=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#12,#14,#51,#8);
                #60=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#23,#10,#52,#8);
                #61=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#25,#10,#53,#8);
                #62=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#28,#14,#54,#8);
                #63=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#31,#10,#55,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_REP_CONTEXT\"",
                "\"name\":\"NOTE_GEOM_CONTEXT\"",
                "\"name\":\"NOTE_SI_UNIT\"",
                "\"name\":\"NOTE_UNCERTAINTY_CTX\"",
                "\"name\":\"NOTE_TRANSFORM\"",
                "\"name\":\"NOTE_REP_ITEM\"",
                "\"name\":\"NOTE_GEOM_ITEM\"",
                "\"name\":\"NOTE_TOPO_ITEM\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_CONTEXT_ONLY\"",
                "\"id\":14,\"type\":\"representation\",\"name\":\"REP_GEOM_CONTEXT\"",
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
    void shouldBindPmiTargetsThroughGeometricLeafAndHalfSpaceDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #10=REPRESENTATION('REP_GEOM_LEAF',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #13=DIRECTION('DZ',(0.0,0.0,1.0));
                #14=DIRECTION('DX',(1.0,0.0,0.0));
                #15=VECTOR('VZ',#13,10.0);
                #16=AXIS1_PLACEMENT('AX1',#12,#13);
                #17=AXIS2_PLACEMENT_3D('AX2',#12,#13,#14);
                #18=(POINT() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('PT0'));
                #19=(CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('CV0'));
                #20=(SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('SF0'));
                #21=(SURFACE_MODEL() REPRESENTATION_ITEM('SM0'));
                #22=(SOLID_MODEL() REPRESENTATION_ITEM('SO0'));
                #23=DIMENSIONAL_EXPONENTS(1.0,0.0,0.0,0.0,0.0,0.0,0.0);
                #24=BOX_DOMAIN(#12,2.0,3.0,4.0);
                #25=PLANE('PLN',#17);
                #26=HALF_SPACE_SOLID('HS',#25,.T.);
                #27=BOXED_HALF_SPACE('BHS',#25,.F.,#24);
                #40=PROPERTY_DEFINITION('PD_P0','',#12);
                #41=PROPERTY_DEFINITION('PD_D0','',#13);
                #42=PROPERTY_DEFINITION('PD_V0','',#15);
                #43=PROPERTY_DEFINITION('PD_AX1','',#16);
                #44=PROPERTY_DEFINITION('PD_AX2','',#17);
                #45=PROPERTY_DEFINITION('PD_POINT','',#18);
                #46=PROPERTY_DEFINITION('PD_CURVE','',#19);
                #47=PROPERTY_DEFINITION('PD_SURFACE','',#20);
                #48=PROPERTY_DEFINITION('PD_SURFACE_MODEL','',#21);
                #49=PROPERTY_DEFINITION('PD_SOLID_MODEL','',#22);
                #50=PROPERTY_DEFINITION('PD_EXP','',#23);
                #51=PROPERTY_DEFINITION('PD_BOX','',#24);
                #52=PROPERTY_DEFINITION('PD_HALFSPACE','',#26);
                #53=PROPERTY_DEFINITION('PD_BOXED_HALFSPACE','',#27);
                #54=PROPERTY_DEFINITION_REPRESENTATION(#40,#10);
                #55=PROPERTY_DEFINITION_REPRESENTATION(#41,#10);
                #56=PROPERTY_DEFINITION_REPRESENTATION(#42,#10);
                #57=PROPERTY_DEFINITION_REPRESENTATION(#43,#10);
                #58=PROPERTY_DEFINITION_REPRESENTATION(#44,#10);
                #59=PROPERTY_DEFINITION_REPRESENTATION(#45,#10);
                #60=PROPERTY_DEFINITION_REPRESENTATION(#46,#10);
                #61=PROPERTY_DEFINITION_REPRESENTATION(#47,#10);
                #62=PROPERTY_DEFINITION_REPRESENTATION(#48,#10);
                #63=PROPERTY_DEFINITION_REPRESENTATION(#49,#10);
                #64=PROPERTY_DEFINITION_REPRESENTATION(#50,#10);
                #65=PROPERTY_DEFINITION_REPRESENTATION(#51,#10);
                #66=PROPERTY_DEFINITION_REPRESENTATION(#52,#10);
                #67=PROPERTY_DEFINITION_REPRESENTATION(#53,#10);
                #70=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #71=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #72=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #73=CARTESIAN_POINT('P3',(3.0,0.0,0.0));
                #74=CARTESIAN_POINT('P4',(4.0,0.0,0.0));
                #75=CARTESIAN_POINT('P5',(5.0,0.0,0.0));
                #76=CARTESIAN_POINT('P6',(6.0,0.0,0.0));
                #77=CARTESIAN_POINT('P7',(7.0,0.0,0.0));
                #78=CARTESIAN_POINT('P8',(8.0,0.0,0.0));
                #79=CARTESIAN_POINT('P9',(9.0,0.0,0.0));
                #80=CARTESIAN_POINT('P10',(10.0,0.0,0.0));
                #81=CARTESIAN_POINT('P11',(11.0,0.0,0.0));
                #82=CARTESIAN_POINT('P12',(12.0,0.0,0.0));
                #83=CARTESIAN_POINT('P13',(13.0,0.0,0.0));
                #84=ANNOTATION_TEXT_OCCURRENCE('NOTE_CARTESIAN','',#70);
                #85=ANNOTATION_TEXT_OCCURRENCE('NOTE_DIRECTION','',#71);
                #86=ANNOTATION_TEXT_OCCURRENCE('NOTE_VECTOR','',#72);
                #87=ANNOTATION_TEXT_OCCURRENCE('NOTE_AXIS1','',#73);
                #88=ANNOTATION_TEXT_OCCURRENCE('NOTE_AXIS2','',#74);
                #89=ANNOTATION_TEXT_OCCURRENCE('NOTE_POINT_MARKER','',#75);
                #90=ANNOTATION_TEXT_OCCURRENCE('NOTE_CURVE_MARKER','',#76);
                #91=ANNOTATION_TEXT_OCCURRENCE('NOTE_SURFACE_MARKER','',#77);
                #92=ANNOTATION_TEXT_OCCURRENCE('NOTE_SURFACE_MODEL','',#78);
                #93=ANNOTATION_TEXT_OCCURRENCE('NOTE_SOLID_MODEL','',#79);
                #94=ANNOTATION_TEXT_OCCURRENCE('NOTE_EXPONENTS','',#80);
                #95=ANNOTATION_TEXT_OCCURRENCE('NOTE_BOX_DOMAIN','',#81);
                #96=ANNOTATION_TEXT_OCCURRENCE('NOTE_HALF_SPACE','',#82);
                #97=ANNOTATION_TEXT_OCCURRENCE('NOTE_BOXED_HALF_SPACE','',#83);
                #98=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#12,#10,#84,#8);
                #99=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#13,#10,#85,#8);
                #100=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#15,#10,#86,#8);
                #101=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#16,#10,#87,#8);
                #102=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#17,#10,#88,#8);
                #103=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#18,#10,#89,#8);
                #104=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#19,#10,#90,#8);
                #105=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#20,#10,#91,#8);
                #106=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#21,#10,#92,#8);
                #107=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#22,#10,#93,#8);
                #108=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#23,#10,#94,#8);
                #109=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#24,#10,#95,#8);
                #110=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#26,#10,#96,#8);
                #111=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#27,#10,#97,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_CARTESIAN\"",
                "\"name\":\"NOTE_DIRECTION\"",
                "\"name\":\"NOTE_VECTOR\"",
                "\"name\":\"NOTE_AXIS1\"",
                "\"name\":\"NOTE_AXIS2\"",
                "\"name\":\"NOTE_POINT_MARKER\"",
                "\"name\":\"NOTE_CURVE_MARKER\"",
                "\"name\":\"NOTE_SURFACE_MARKER\"",
                "\"name\":\"NOTE_SURFACE_MODEL\"",
                "\"name\":\"NOTE_SOLID_MODEL\"",
                "\"name\":\"NOTE_EXPONENTS\"",
                "\"name\":\"NOTE_BOX_DOMAIN\"",
                "\"name\":\"NOTE_HALF_SPACE\"",
                "\"name\":\"NOTE_BOXED_HALF_SPACE\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_GEOM_LEAF\"",
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
    void shouldBindPmiTargetsThroughCurveSurfaceWrapperAndTopologyLeafDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #10=REPRESENTATION('REP_WRAPPER_LEAF',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #13=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #14=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #15=DIRECTION('DZ',(0.0,0.0,1.0));
                #16=DIRECTION('DX',(1.0,0.0,0.0));
                #17=VECTOR('VX',#16,1.0);
                #18=AXIS2_PLACEMENT_3D('AX3',#12,#15,#16);
                #19=LINE('L0',#12,#17);
                #20=(BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BC0'));
                #21=(UNIFORM_CURVE() BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('UC0'));
                #22=(BEZIER_CURVE() BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BZ0'));
                #23=(PIECEWISE_BEZIER_CURVE() BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('PBC0'));
                #24=(QUASI_UNIFORM_CURVE() BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('QUC0'));
                #25=PARABOLA('PAR0',#18,2.0);
                #26=COMPOSITE_CURVE_SEGMENT(.CONTINUOUS.,.T.,#19);
                #27=(BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BS0'));
                #28=(UNIFORM_SURFACE() BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('US0'));
                #29=(BEZIER_SURFACE() BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BZS0'));
                #30=(PIECEWISE_BEZIER_SURFACE() BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('PBS0'));
                #31=(QUASI_UNIFORM_SURFACE() BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('QUS0'));
                #32=PLANE('PL0',#18);
                #33=RECTANGULAR_TRIMMED_SURFACE('RTS',#32,0.0,1.0,0.0,1.0,.T.,.T.);
                #34=POLYLINE('PL1',(#12,#13,#14));
                #35=CURVE_BOUNDED_SURFACE('CBS',#32,(#34),.F.);
                #36=ORIENTED_SURFACE('OS',#32,.T.);
                #37=OFFSET_SURFACE('OFS',#32,1.0,.F.);
                #38=SPHERICAL_SURFACE('SPH',#18,2.0);
                #39=DEGENERATE_TOROIDAL_SURFACE('DTS',#18,5.0,1.0,.T.);
                #40=CARTESIAN_POINT('UV0',(0.0,0.0));
                #41=DIRECTION('UX',(1.0,0.0));
                #42=VECTOR('UVV',#41,1.0);
                #43=LINE('UVL',#40,#42);
                #44=REPRESENTATION_CONTEXT('PC','PARAMETRIC');
                #45=DEFINITIONAL_REPRESENTATION('DPR',(#43),#44);
                #46=DEGENERATE_PCURVE('DPC',#32,#45);
                #47=(VERTEX() TOPOLOGICAL_REPRESENTATION_ITEM('VTX0'));
                #48=VERTEX_POINT('VP0',#12);
                #49=VERTEX_POINT('VP1',#13);
                #50=EDGE_CURVE('EC0',#48,#49,#19,.T.);
                #51=(EDGE() TOPOLOGICAL_REPRESENTATION_ITEM('ED0'));
                #52=SUBEDGE('SE0',#48,#49,#50);
                #53=POLY_LOOP('LOOP0',(#12,#13,#14));
                #54=(FACE() TOPOLOGICAL_REPRESENTATION_ITEM('FC0'));
                #60=PROPERTY_DEFINITION('PD_BC','',#20);
                #61=PROPERTY_DEFINITION('PD_UC','',#21);
                #62=PROPERTY_DEFINITION('PD_BZ','',#22);
                #63=PROPERTY_DEFINITION('PD_PBC','',#23);
                #64=PROPERTY_DEFINITION('PD_QUC','',#24);
                #65=PROPERTY_DEFINITION('PD_PAR','',#25);
                #66=PROPERTY_DEFINITION('PD_CCS','',#26);
                #67=PROPERTY_DEFINITION('PD_BS','',#27);
                #68=PROPERTY_DEFINITION('PD_US','',#28);
                #69=PROPERTY_DEFINITION('PD_BZS','',#29);
                #70=PROPERTY_DEFINITION('PD_PBS','',#30);
                #71=PROPERTY_DEFINITION('PD_QUS','',#31);
                #72=PROPERTY_DEFINITION('PD_RTS','',#33);
                #73=PROPERTY_DEFINITION('PD_CBS','',#35);
                #74=PROPERTY_DEFINITION('PD_OS','',#36);
                #75=PROPERTY_DEFINITION('PD_OFS','',#37);
                #76=PROPERTY_DEFINITION('PD_SPH','',#38);
                #77=PROPERTY_DEFINITION('PD_DTS','',#39);
                #78=PROPERTY_DEFINITION('PD_DPC','',#46);
                #79=PROPERTY_DEFINITION('PD_VTX','',#47);
                #80=PROPERTY_DEFINITION('PD_EDGE','',#51);
                #81=PROPERTY_DEFINITION('PD_SUBEDGE','',#52);
                #82=PROPERTY_DEFINITION('PD_LOOP','',#53);
                #83=PROPERTY_DEFINITION('PD_FACE','',#54);
                #84=PROPERTY_DEFINITION_REPRESENTATION(#60,#10);
                #85=PROPERTY_DEFINITION_REPRESENTATION(#61,#10);
                #86=PROPERTY_DEFINITION_REPRESENTATION(#62,#10);
                #87=PROPERTY_DEFINITION_REPRESENTATION(#63,#10);
                #88=PROPERTY_DEFINITION_REPRESENTATION(#64,#10);
                #89=PROPERTY_DEFINITION_REPRESENTATION(#65,#10);
                #90=PROPERTY_DEFINITION_REPRESENTATION(#66,#10);
                #91=PROPERTY_DEFINITION_REPRESENTATION(#67,#10);
                #92=PROPERTY_DEFINITION_REPRESENTATION(#68,#10);
                #93=PROPERTY_DEFINITION_REPRESENTATION(#69,#10);
                #94=PROPERTY_DEFINITION_REPRESENTATION(#70,#10);
                #95=PROPERTY_DEFINITION_REPRESENTATION(#71,#10);
                #96=PROPERTY_DEFINITION_REPRESENTATION(#72,#10);
                #97=PROPERTY_DEFINITION_REPRESENTATION(#73,#10);
                #98=PROPERTY_DEFINITION_REPRESENTATION(#74,#10);
                #99=PROPERTY_DEFINITION_REPRESENTATION(#75,#10);
                #100=PROPERTY_DEFINITION_REPRESENTATION(#76,#10);
                #101=PROPERTY_DEFINITION_REPRESENTATION(#77,#10);
                #102=PROPERTY_DEFINITION_REPRESENTATION(#78,#10);
                #103=PROPERTY_DEFINITION_REPRESENTATION(#79,#10);
                #104=PROPERTY_DEFINITION_REPRESENTATION(#80,#10);
                #105=PROPERTY_DEFINITION_REPRESENTATION(#81,#10);
                #106=PROPERTY_DEFINITION_REPRESENTATION(#82,#10);
                #107=PROPERTY_DEFINITION_REPRESENTATION(#83,#10);
                #110=CARTESIAN_POINT('N0',(0.0,0.0,0.0));
                #111=CARTESIAN_POINT('N1',(1.0,0.0,0.0));
                #112=CARTESIAN_POINT('N2',(2.0,0.0,0.0));
                #113=CARTESIAN_POINT('N3',(3.0,0.0,0.0));
                #114=CARTESIAN_POINT('N4',(4.0,0.0,0.0));
                #115=CARTESIAN_POINT('N5',(5.0,0.0,0.0));
                #116=CARTESIAN_POINT('N6',(6.0,0.0,0.0));
                #117=CARTESIAN_POINT('N7',(7.0,0.0,0.0));
                #118=CARTESIAN_POINT('N8',(8.0,0.0,0.0));
                #119=CARTESIAN_POINT('N9',(9.0,0.0,0.0));
                #120=CARTESIAN_POINT('N10',(10.0,0.0,0.0));
                #121=CARTESIAN_POINT('N11',(11.0,0.0,0.0));
                #122=CARTESIAN_POINT('N12',(12.0,0.0,0.0));
                #123=CARTESIAN_POINT('N13',(13.0,0.0,0.0));
                #124=CARTESIAN_POINT('N14',(14.0,0.0,0.0));
                #125=CARTESIAN_POINT('N15',(15.0,0.0,0.0));
                #126=CARTESIAN_POINT('N16',(16.0,0.0,0.0));
                #127=CARTESIAN_POINT('N17',(17.0,0.0,0.0));
                #128=CARTESIAN_POINT('N18',(18.0,0.0,0.0));
                #129=CARTESIAN_POINT('N19',(19.0,0.0,0.0));
                #130=CARTESIAN_POINT('N20',(20.0,0.0,0.0));
                #131=CARTESIAN_POINT('N21',(21.0,0.0,0.0));
                #132=CARTESIAN_POINT('N22',(22.0,0.0,0.0));
                #133=CARTESIAN_POINT('N23',(23.0,0.0,0.0));
                #134=ANNOTATION_TEXT_OCCURRENCE('NOTE_BC','',#110);
                #135=ANNOTATION_TEXT_OCCURRENCE('NOTE_UC','',#111);
                #136=ANNOTATION_TEXT_OCCURRENCE('NOTE_BZ','',#112);
                #137=ANNOTATION_TEXT_OCCURRENCE('NOTE_PBC','',#113);
                #138=ANNOTATION_TEXT_OCCURRENCE('NOTE_QUC','',#114);
                #139=ANNOTATION_TEXT_OCCURRENCE('NOTE_PAR','',#115);
                #140=ANNOTATION_TEXT_OCCURRENCE('NOTE_CCS','',#116);
                #141=ANNOTATION_TEXT_OCCURRENCE('NOTE_BS','',#117);
                #142=ANNOTATION_TEXT_OCCURRENCE('NOTE_US','',#118);
                #143=ANNOTATION_TEXT_OCCURRENCE('NOTE_BZS','',#119);
                #144=ANNOTATION_TEXT_OCCURRENCE('NOTE_PBS','',#120);
                #145=ANNOTATION_TEXT_OCCURRENCE('NOTE_QUS','',#121);
                #146=ANNOTATION_TEXT_OCCURRENCE('NOTE_RTS','',#122);
                #147=ANNOTATION_TEXT_OCCURRENCE('NOTE_CBS','',#123);
                #148=ANNOTATION_TEXT_OCCURRENCE('NOTE_OS','',#124);
                #149=ANNOTATION_TEXT_OCCURRENCE('NOTE_OFS','',#125);
                #150=ANNOTATION_TEXT_OCCURRENCE('NOTE_SPH','',#126);
                #151=ANNOTATION_TEXT_OCCURRENCE('NOTE_DTS','',#127);
                #152=ANNOTATION_TEXT_OCCURRENCE('NOTE_DPC','',#128);
                #153=ANNOTATION_TEXT_OCCURRENCE('NOTE_VERTEX','',#129);
                #154=ANNOTATION_TEXT_OCCURRENCE('NOTE_EDGE','',#130);
                #155=ANNOTATION_TEXT_OCCURRENCE('NOTE_SUBEDGE','',#131);
                #156=ANNOTATION_TEXT_OCCURRENCE('NOTE_LOOP','',#132);
                #157=ANNOTATION_TEXT_OCCURRENCE('NOTE_FACE','',#133);
                #158=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#20,#10,#134,#8);
                #159=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#21,#10,#135,#8);
                #160=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#22,#10,#136,#8);
                #161=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#23,#10,#137,#8);
                #162=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#24,#10,#138,#8);
                #163=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#25,#10,#139,#8);
                #164=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#26,#10,#140,#8);
                #165=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#27,#10,#141,#8);
                #166=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#28,#10,#142,#8);
                #167=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#29,#10,#143,#8);
                #168=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#30,#10,#144,#8);
                #169=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#31,#10,#145,#8);
                #170=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#33,#10,#146,#8);
                #171=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#35,#10,#147,#8);
                #172=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#36,#10,#148,#8);
                #173=PMI_REQUIREMENT_ITEM_ASSOCIATION('A15','',#37,#10,#149,#8);
                #174=PMI_REQUIREMENT_ITEM_ASSOCIATION('A16','',#38,#10,#150,#8);
                #175=PMI_REQUIREMENT_ITEM_ASSOCIATION('A17','',#39,#10,#151,#8);
                #176=PMI_REQUIREMENT_ITEM_ASSOCIATION('A18','',#46,#10,#152,#8);
                #177=PMI_REQUIREMENT_ITEM_ASSOCIATION('A19','',#47,#10,#153,#8);
                #178=PMI_REQUIREMENT_ITEM_ASSOCIATION('A20','',#51,#10,#154,#8);
                #179=PMI_REQUIREMENT_ITEM_ASSOCIATION('A21','',#52,#10,#155,#8);
                #180=PMI_REQUIREMENT_ITEM_ASSOCIATION('A22','',#53,#10,#156,#8);
                #181=PMI_REQUIREMENT_ITEM_ASSOCIATION('A23','',#54,#10,#157,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_BC\"",
                "\"name\":\"NOTE_UC\"",
                "\"name\":\"NOTE_BZ\"",
                "\"name\":\"NOTE_PBC\"",
                "\"name\":\"NOTE_QUC\"",
                "\"name\":\"NOTE_PAR\"",
                "\"name\":\"NOTE_CCS\"",
                "\"name\":\"NOTE_BS\"",
                "\"name\":\"NOTE_US\"",
                "\"name\":\"NOTE_BZS\"",
                "\"name\":\"NOTE_PBS\"",
                "\"name\":\"NOTE_QUS\"",
                "\"name\":\"NOTE_RTS\"",
                "\"name\":\"NOTE_CBS\"",
                "\"name\":\"NOTE_OS\"",
                "\"name\":\"NOTE_OFS\"",
                "\"name\":\"NOTE_SPH\"",
                "\"name\":\"NOTE_DTS\"",
                "\"name\":\"NOTE_DPC\"",
                "\"name\":\"NOTE_VERTEX\"",
                "\"name\":\"NOTE_EDGE\"",
                "\"name\":\"NOTE_SUBEDGE\"",
                "\"name\":\"NOTE_LOOP\"",
                "\"name\":\"NOTE_FACE\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_WRAPPER_LEAF\"",
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
    void shouldBindPmiTargetsThroughReplicaShellAndCurveWrapperDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #10=REPRESENTATION('REP_WRAPPER_SHELL',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #13=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #14=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #15=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #16=DIRECTION('DZ',(0.0,0.0,1.0));
                #17=DIRECTION('DX',(1.0,0.0,0.0));
                #18=DIRECTION('DY',(0.0,1.0,0.0));
                #19=VECTOR('VX',#17,2.0);
                #20=AXIS2_PLACEMENT_3D('AX3',#12,#16,#17);
                #21=LINE('L0',#12,#19);
                #22=TRIMMED_CURVE('TC0',#21,(#12),(#13),.T.,.CARTESIAN.);
                #23=REPRESENTATION_CONTEXT('UV','PARAMETRIC');
                #24=DEFINITIONAL_REPRESENTATION('DR0',(#21),#23);
                #25=PCURVE('PC0',#32,#24);
                #26=SURFACE_CURVE('SC0',#21,(#25),.PCURVE_S1.);
                #27=PCURVE('PC1',#32,#24);
                #28=SEAM_CURVE('SEAM0',#21,(#25,#27),.PCURVE_S1.);
                #29=COMPOSITE_CURVE_SEGMENT(.CONTINUOUS.,.T.,#21);
                #30=(COMPOSITE_CURVE('CC0',(#29),.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('cc-name'));
                #31=(COMPOSITE_CURVE_ON_SURFACE('CCS0',(#29),.F.) COMPOSITE_CURVE('CCS0',(#29),.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('ccs-name'));
                #32=PLANE('PL0',#20);
                #33=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T3',#17,#18,#12,1.0,#16);
                #34=CURVE_REPLICA('CR0',#21,#33);
                #35=SURFACE_REPLICA('SR0',#32,#33);
                #36=VERTEX_POINT('V0',#12);
                #37=VERTEX_POINT('V1',#13);
                #38=VERTEX_POINT('V2',#14);
                #39=VERTEX_POINT('V3',#15);
                #40=EDGE_CURVE('E0',#36,#37,#21,.T.);
                #41=EDGE_CURVE('E1',#37,#38,#21,.T.);
                #42=EDGE_CURVE('E2',#38,#39,#21,.T.);
                #43=EDGE_CURVE('E3',#39,#36,#21,.T.);
                #44=ORIENTED_EDGE('OE0',$,$,#40,.T.);
                #45=ORIENTED_EDGE('OE1',$,$,#41,.T.);
                #46=ORIENTED_EDGE('OE2',$,$,#42,.T.);
                #47=ORIENTED_EDGE('OE3',$,$,#43,.T.);
                #48=EDGE_LOOP('EL0',(#44,#45,#46,#47));
                #49=VERTEX_LOOP('VL0',#36);
                #50=FACE_OUTER_BOUND('FOB0',#48,.T.);
                #51=ADVANCED_FACE('F0',(#50),#32,.T.);
                #52=OPEN_SHELL('OS0',(#51));
                #53=ORIENTED_OPEN_SHELL('OOS0',#52,.F.);
                #54=CLOSED_SHELL('CS0',(#51));
                #55=ORIENTED_CLOSED_SHELL('OCS0',#54,.F.);
                #56=CONNECTED_EDGE_SET('CES0',(#40,#41,#42,#43));
                #57=CONNECTED_FACE_SET('CFS0',(#51));
                #58=(CONNECTED_FACE_SUB_SET('CFSS0',(#51),#57) CONNECTED_FACE_SET('CFSS0',(#51)));
                #60=PROPERTY_DEFINITION('PD_TC','',#22);
                #61=PROPERTY_DEFINITION('PD_PC','',#25);
                #62=PROPERTY_DEFINITION('PD_SC','',#26);
                #63=PROPERTY_DEFINITION('PD_SEAM','',#28);
                #64=PROPERTY_DEFINITION('PD_CC','',#30);
                #65=PROPERTY_DEFINITION('PD_CCS','',#31);
                #66=PROPERTY_DEFINITION('PD_XF','',#33);
                #67=PROPERTY_DEFINITION('PD_CR','',#34);
                #68=PROPERTY_DEFINITION('PD_SR','',#35);
                #69=PROPERTY_DEFINITION('PD_VP','',#36);
                #70=PROPERTY_DEFINITION('PD_EL','',#48);
                #71=PROPERTY_DEFINITION('PD_VL','',#49);
                #72=PROPERTY_DEFINITION('PD_FB','',#50);
                #73=PROPERTY_DEFINITION('PD_OS','',#52);
                #74=PROPERTY_DEFINITION('PD_OOS','',#53);
                #75=PROPERTY_DEFINITION('PD_CS','',#54);
                #76=PROPERTY_DEFINITION('PD_OCS','',#55);
                #77=PROPERTY_DEFINITION('PD_CES','',#56);
                #78=PROPERTY_DEFINITION('PD_CFS','',#57);
                #79=PROPERTY_DEFINITION('PD_CFSS','',#58);
                #80=PROPERTY_DEFINITION_REPRESENTATION(#60,#10);
                #81=PROPERTY_DEFINITION_REPRESENTATION(#61,#10);
                #82=PROPERTY_DEFINITION_REPRESENTATION(#62,#10);
                #83=PROPERTY_DEFINITION_REPRESENTATION(#63,#10);
                #84=PROPERTY_DEFINITION_REPRESENTATION(#64,#10);
                #85=PROPERTY_DEFINITION_REPRESENTATION(#65,#10);
                #86=PROPERTY_DEFINITION_REPRESENTATION(#66,#10);
                #87=PROPERTY_DEFINITION_REPRESENTATION(#67,#10);
                #88=PROPERTY_DEFINITION_REPRESENTATION(#68,#10);
                #89=PROPERTY_DEFINITION_REPRESENTATION(#69,#10);
                #90=PROPERTY_DEFINITION_REPRESENTATION(#70,#10);
                #91=PROPERTY_DEFINITION_REPRESENTATION(#71,#10);
                #92=PROPERTY_DEFINITION_REPRESENTATION(#72,#10);
                #93=PROPERTY_DEFINITION_REPRESENTATION(#73,#10);
                #94=PROPERTY_DEFINITION_REPRESENTATION(#74,#10);
                #95=PROPERTY_DEFINITION_REPRESENTATION(#75,#10);
                #96=PROPERTY_DEFINITION_REPRESENTATION(#76,#10);
                #97=PROPERTY_DEFINITION_REPRESENTATION(#77,#10);
                #98=PROPERTY_DEFINITION_REPRESENTATION(#78,#10);
                #99=PROPERTY_DEFINITION_REPRESENTATION(#79,#10);
                #100=CARTESIAN_POINT('N0',(0.0,0.0,0.0));
                #101=CARTESIAN_POINT('N1',(1.0,0.0,0.0));
                #102=CARTESIAN_POINT('N2',(2.0,0.0,0.0));
                #103=CARTESIAN_POINT('N3',(3.0,0.0,0.0));
                #104=CARTESIAN_POINT('N4',(4.0,0.0,0.0));
                #105=CARTESIAN_POINT('N5',(5.0,0.0,0.0));
                #106=CARTESIAN_POINT('N6',(6.0,0.0,0.0));
                #107=CARTESIAN_POINT('N7',(7.0,0.0,0.0));
                #108=CARTESIAN_POINT('N8',(8.0,0.0,0.0));
                #109=CARTESIAN_POINT('N9',(9.0,0.0,0.0));
                #110=CARTESIAN_POINT('N10',(10.0,0.0,0.0));
                #111=CARTESIAN_POINT('N11',(11.0,0.0,0.0));
                #112=CARTESIAN_POINT('N12',(12.0,0.0,0.0));
                #113=CARTESIAN_POINT('N13',(13.0,0.0,0.0));
                #114=CARTESIAN_POINT('N14',(14.0,0.0,0.0));
                #115=CARTESIAN_POINT('N15',(15.0,0.0,0.0));
                #116=CARTESIAN_POINT('N16',(16.0,0.0,0.0));
                #117=CARTESIAN_POINT('N17',(17.0,0.0,0.0));
                #118=CARTESIAN_POINT('N18',(18.0,0.0,0.0));
                #119=CARTESIAN_POINT('N19',(19.0,0.0,0.0));
                #120=ANNOTATION_TEXT_OCCURRENCE('NOTE_TC','',#100);
                #121=ANNOTATION_TEXT_OCCURRENCE('NOTE_PC','',#101);
                #122=ANNOTATION_TEXT_OCCURRENCE('NOTE_SC','',#102);
                #123=ANNOTATION_TEXT_OCCURRENCE('NOTE_SEAM','',#103);
                #124=ANNOTATION_TEXT_OCCURRENCE('NOTE_CC','',#104);
                #125=ANNOTATION_TEXT_OCCURRENCE('NOTE_CCS','',#105);
                #126=ANNOTATION_TEXT_OCCURRENCE('NOTE_XF','',#106);
                #127=ANNOTATION_TEXT_OCCURRENCE('NOTE_CR','',#107);
                #128=ANNOTATION_TEXT_OCCURRENCE('NOTE_SR','',#108);
                #129=ANNOTATION_TEXT_OCCURRENCE('NOTE_VP','',#109);
                #130=ANNOTATION_TEXT_OCCURRENCE('NOTE_EL','',#110);
                #131=ANNOTATION_TEXT_OCCURRENCE('NOTE_VL','',#111);
                #132=ANNOTATION_TEXT_OCCURRENCE('NOTE_FB','',#112);
                #133=ANNOTATION_TEXT_OCCURRENCE('NOTE_OS','',#113);
                #134=ANNOTATION_TEXT_OCCURRENCE('NOTE_OOS','',#114);
                #135=ANNOTATION_TEXT_OCCURRENCE('NOTE_CS','',#115);
                #136=ANNOTATION_TEXT_OCCURRENCE('NOTE_OCS','',#116);
                #137=ANNOTATION_TEXT_OCCURRENCE('NOTE_CES','',#117);
                #138=ANNOTATION_TEXT_OCCURRENCE('NOTE_CFS','',#118);
                #139=ANNOTATION_TEXT_OCCURRENCE('NOTE_CFSS','',#119);
                #140=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#22,#10,#120,#8);
                #141=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#25,#10,#121,#8);
                #142=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#26,#10,#122,#8);
                #143=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#28,#10,#123,#8);
                #144=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#30,#10,#124,#8);
                #145=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#31,#10,#125,#8);
                #146=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#33,#10,#126,#8);
                #147=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#34,#10,#127,#8);
                #148=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#35,#10,#128,#8);
                #149=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#36,#10,#129,#8);
                #150=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#48,#10,#130,#8);
                #151=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#49,#10,#131,#8);
                #152=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#50,#10,#132,#8);
                #153=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#52,#10,#133,#8);
                #154=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#53,#10,#134,#8);
                #155=PMI_REQUIREMENT_ITEM_ASSOCIATION('A15','',#54,#10,#135,#8);
                #156=PMI_REQUIREMENT_ITEM_ASSOCIATION('A16','',#55,#10,#136,#8);
                #157=PMI_REQUIREMENT_ITEM_ASSOCIATION('A17','',#56,#10,#137,#8);
                #158=PMI_REQUIREMENT_ITEM_ASSOCIATION('A18','',#57,#10,#138,#8);
                #159=PMI_REQUIREMENT_ITEM_ASSOCIATION('A19','',#58,#10,#139,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_TC\"",
                "\"name\":\"NOTE_PC\"",
                "\"name\":\"NOTE_SC\"",
                "\"name\":\"NOTE_SEAM\"",
                "\"name\":\"NOTE_CC\"",
                "\"name\":\"NOTE_CCS\"",
                "\"name\":\"NOTE_XF\"",
                "\"name\":\"NOTE_CR\"",
                "\"name\":\"NOTE_SR\"",
                "\"name\":\"NOTE_VP\"",
                "\"name\":\"NOTE_EL\"",
                "\"name\":\"NOTE_VL\"",
                "\"name\":\"NOTE_FB\"",
                "\"name\":\"NOTE_OS\"",
                "\"name\":\"NOTE_OOS\"",
                "\"name\":\"NOTE_CS\"",
                "\"name\":\"NOTE_OCS\"",
                "\"name\":\"NOTE_CES\"",
                "\"name\":\"NOTE_CFS\"",
                "\"name\":\"NOTE_CFSS\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_WRAPPER_SHELL\"",
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
    void shouldBindPmiTargetsThroughPrimitiveSurfaceFaceAndSolidDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #10=REPRESENTATION('REP_PRIMITIVE_SOLID',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #13=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #14=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #15=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #16=DIRECTION('DZ',(0.0,0.0,1.0));
                #17=DIRECTION('DX',(1.0,0.0,0.0));
                #18=VECTOR('VX',#17,1.0);
                #19=AXIS1_PLACEMENT('AX1',#12,#16);
                #20=AXIS2_PLACEMENT_3D('AX2',#12,#16,#17);
                #21=AXIS2_PLACEMENT_2D('AX2D',#12,#17);
                #22=LINE('L0',#12,#18);
                #23=CIRCLE('C0',#20,1.0);
                #24=ELLIPSE('E0',#20,2.0,1.0);
                #25=PLANE('PL0',#20);
                #26=CYLINDRICAL_SURFACE('CY0',#20,1.0);
                #27=CONICAL_SURFACE('CN0',#20,1.0,0.5);
                #28=TOROIDAL_SURFACE('TO0',#20,5.0,1.0);
                #29=SURFACE_OF_LINEAR_EXTRUSION('SLE0',#22,#18);
                #30=SURFACE_OF_REVOLUTION('SOR0',#22,#19);
                #31=POINT_SET('PS0',(#12,#13));
                #32=VERTEX_POINT('V0',#12);
                #33=VERTEX_POINT('V1',#13);
                #34=VERTEX_POINT('V2',#14);
                #35=VERTEX_POINT('V3',#15);
                #36=EDGE_CURVE('E0',#32,#33,#22,.T.);
                #37=EDGE_CURVE('E1',#33,#34,#22,.T.);
                #38=EDGE_CURVE('E2',#34,#35,#22,.T.);
                #39=EDGE_CURVE('E3',#35,#32,#22,.T.);
                #40=ORIENTED_EDGE('OE0',$,$,#36,.T.);
                #41=ORIENTED_EDGE('OE1',$,$,#37,.T.);
                #42=ORIENTED_EDGE('OE2',$,$,#38,.T.);
                #43=ORIENTED_EDGE('OE3',$,$,#39,.T.);
                #44=EDGE_LOOP('EL0',(#40,#41,#42,#43));
                #45=FACE_OUTER_BOUND('FOB0',#44,.T.);
                #46=ADVANCED_FACE('AF0',(#45),#25,.T.);
                #47=ORIENTED_FACE('OF0',#46,.F.);
                #48=CLOSED_SHELL('CS0',(#46));
                #49=MANIFOLD_SOLID_BREP('MSB0',#48);
                #50=BREP_WITH_VOIDS('BV0',#48,());
                #51=RECTANGLE_PROFILE_DEF(.AREA.,'RPD',#21,1.0,1.0);
                #52=EXTRUDED_AREA_SOLID('EAS0',#51,#20,#16,2.0);
                #53=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T3',#17,#16,#12,1.0,#17);
                #54=SOLID_REPLICA('SR0',#49,#53);
                #55=BLOCK('BLK0',#20,1.0,2.0,3.0);
                #56=CSG_SOLID('CSG0',#55);
                #57=(BOOLEAN_RESULT(.UNION.,#49,#56) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BR0'));
                #58=(BOOLEAN_CLIPPING_RESULT(.DIFFERENCE.,#56,#49) BOOLEAN_RESULT(.DIFFERENCE.,#56,#49) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BCR0'));
                #60=PROPERTY_DEFINITION('PD_LINE','',#21);
                #61=PROPERTY_DEFINITION('PD_CIRCLE','',#22);
                #62=PROPERTY_DEFINITION('PD_ELLIPSE','',#23);
                #63=PROPERTY_DEFINITION('PD_PLANE','',#24);
                #64=PROPERTY_DEFINITION('PD_CYL','',#25);
                #65=PROPERTY_DEFINITION('PD_CONE','',#26);
                #66=PROPERTY_DEFINITION('PD_TORUS','',#27);
                #67=PROPERTY_DEFINITION('PD_SLE','',#28);
                #68=PROPERTY_DEFINITION('PD_SOR','',#29);
                #69=PROPERTY_DEFINITION('PD_POINTSET','',#31);
                #70=PROPERTY_DEFINITION('PD_AF','',#46);
                #71=PROPERTY_DEFINITION('PD_OF','',#47);
                #72=PROPERTY_DEFINITION('PD_MSB','',#49);
                #73=PROPERTY_DEFINITION('PD_BV','',#50);
                #74=PROPERTY_DEFINITION('PD_EAS','',#52);
                #75=PROPERTY_DEFINITION('PD_SR','',#54);
                #76=PROPERTY_DEFINITION('PD_CSG','',#56);
                #77=PROPERTY_DEFINITION('PD_BR','',#57);
                #78=PROPERTY_DEFINITION('PD_BCR','',#58);
                #80=PROPERTY_DEFINITION_REPRESENTATION(#60,#10);
                #81=PROPERTY_DEFINITION_REPRESENTATION(#61,#10);
                #82=PROPERTY_DEFINITION_REPRESENTATION(#62,#10);
                #83=PROPERTY_DEFINITION_REPRESENTATION(#63,#10);
                #84=PROPERTY_DEFINITION_REPRESENTATION(#64,#10);
                #85=PROPERTY_DEFINITION_REPRESENTATION(#65,#10);
                #86=PROPERTY_DEFINITION_REPRESENTATION(#66,#10);
                #87=PROPERTY_DEFINITION_REPRESENTATION(#67,#10);
                #88=PROPERTY_DEFINITION_REPRESENTATION(#68,#10);
                #89=PROPERTY_DEFINITION_REPRESENTATION(#69,#10);
                #90=PROPERTY_DEFINITION_REPRESENTATION(#70,#10);
                #91=PROPERTY_DEFINITION_REPRESENTATION(#71,#10);
                #92=PROPERTY_DEFINITION_REPRESENTATION(#72,#10);
                #93=PROPERTY_DEFINITION_REPRESENTATION(#73,#10);
                #94=PROPERTY_DEFINITION_REPRESENTATION(#74,#10);
                #95=PROPERTY_DEFINITION_REPRESENTATION(#75,#10);
                #96=PROPERTY_DEFINITION_REPRESENTATION(#76,#10);
                #97=PROPERTY_DEFINITION_REPRESENTATION(#77,#10);
                #98=PROPERTY_DEFINITION_REPRESENTATION(#78,#10);
                #100=CARTESIAN_POINT('N0',(0.0,0.0,0.0));
                #101=CARTESIAN_POINT('N1',(1.0,0.0,0.0));
                #102=CARTESIAN_POINT('N2',(2.0,0.0,0.0));
                #103=CARTESIAN_POINT('N3',(3.0,0.0,0.0));
                #104=CARTESIAN_POINT('N4',(4.0,0.0,0.0));
                #105=CARTESIAN_POINT('N5',(5.0,0.0,0.0));
                #106=CARTESIAN_POINT('N6',(6.0,0.0,0.0));
                #107=CARTESIAN_POINT('N7',(7.0,0.0,0.0));
                #108=CARTESIAN_POINT('N8',(8.0,0.0,0.0));
                #109=CARTESIAN_POINT('N9',(9.0,0.0,0.0));
                #110=CARTESIAN_POINT('N10',(10.0,0.0,0.0));
                #111=CARTESIAN_POINT('N11',(11.0,0.0,0.0));
                #112=CARTESIAN_POINT('N12',(12.0,0.0,0.0));
                #113=CARTESIAN_POINT('N13',(13.0,0.0,0.0));
                #114=CARTESIAN_POINT('N14',(14.0,0.0,0.0));
                #115=CARTESIAN_POINT('N15',(15.0,0.0,0.0));
                #116=CARTESIAN_POINT('N16',(16.0,0.0,0.0));
                #117=CARTESIAN_POINT('N17',(17.0,0.0,0.0));
                #118=ANNOTATION_TEXT_OCCURRENCE('NOTE_LINE','',#100);
                #119=ANNOTATION_TEXT_OCCURRENCE('NOTE_CIRCLE','',#101);
                #120=ANNOTATION_TEXT_OCCURRENCE('NOTE_ELLIPSE','',#102);
                #121=ANNOTATION_TEXT_OCCURRENCE('NOTE_PLANE','',#103);
                #122=ANNOTATION_TEXT_OCCURRENCE('NOTE_CYL','',#104);
                #123=ANNOTATION_TEXT_OCCURRENCE('NOTE_CONE','',#105);
                #124=ANNOTATION_TEXT_OCCURRENCE('NOTE_TORUS','',#106);
                #125=ANNOTATION_TEXT_OCCURRENCE('NOTE_SLE','',#107);
                #126=ANNOTATION_TEXT_OCCURRENCE('NOTE_SOR','',#108);
                #127=ANNOTATION_TEXT_OCCURRENCE('NOTE_POINTSET','',#109);
                #128=ANNOTATION_TEXT_OCCURRENCE('NOTE_AF','',#110);
                #129=ANNOTATION_TEXT_OCCURRENCE('NOTE_OF','',#111);
                #130=ANNOTATION_TEXT_OCCURRENCE('NOTE_MSB','',#112);
                #131=ANNOTATION_TEXT_OCCURRENCE('NOTE_BV','',#113);
                #132=ANNOTATION_TEXT_OCCURRENCE('NOTE_EAS','',#114);
                #133=ANNOTATION_TEXT_OCCURRENCE('NOTE_SR','',#115);
                #134=ANNOTATION_TEXT_OCCURRENCE('NOTE_CSG','',#116);
                #135=ANNOTATION_TEXT_OCCURRENCE('NOTE_BR','',#117);
                #136=ANNOTATION_TEXT_OCCURRENCE('NOTE_BCR','',#117);
                #159=CARTESIAN_POINT('N18',(18.0,0.0,0.0));
                #160=ANNOTATION_TEXT_OCCURRENCE('NOTE_SOR_DIRECT','',#159);
                #140=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#21,#10,#118,#8);
                #141=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#22,#10,#119,#8);
                #142=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#23,#10,#120,#8);
                #143=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#24,#10,#121,#8);
                #144=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#25,#10,#122,#8);
                #145=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#26,#10,#123,#8);
                #146=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#27,#10,#124,#8);
                #147=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#28,#10,#125,#8);
                #148=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#29,#10,#126,#8);
                #149=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#31,#10,#127,#8);
                #150=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#46,#10,#128,#8);
                #151=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#47,#10,#129,#8);
                #152=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#49,#10,#130,#8);
                #153=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#50,#10,#131,#8);
                #154=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#52,#10,#132,#8);
                #155=PMI_REQUIREMENT_ITEM_ASSOCIATION('A15','',#54,#10,#133,#8);
                #156=PMI_REQUIREMENT_ITEM_ASSOCIATION('A16','',#56,#10,#134,#8);
                #157=PMI_REQUIREMENT_ITEM_ASSOCIATION('A17','',#57,#10,#135,#8);
                #158=PMI_REQUIREMENT_ITEM_ASSOCIATION('A18','',#58,#10,#136,#8);
                #161=PMI_REQUIREMENT_ITEM_ASSOCIATION('A19','',#30,#10,#160,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_LINE\"",
                "\"name\":\"NOTE_CIRCLE\"",
                "\"name\":\"NOTE_ELLIPSE\"",
                "\"name\":\"NOTE_PLANE\"",
                "\"name\":\"NOTE_CYL\"",
                "\"name\":\"NOTE_CONE\"",
                "\"name\":\"NOTE_TORUS\"",
                "\"name\":\"NOTE_SLE\"",
                "\"name\":\"NOTE_SOR\"",
                "\"name\":\"NOTE_SOR_DIRECT\"",
                "\"name\":\"NOTE_POINTSET\"",
                "\"name\":\"NOTE_AF\"",
                "\"name\":\"NOTE_OF\"",
                "\"name\":\"NOTE_MSB\"",
                "\"name\":\"NOTE_BV\"",
                "\"name\":\"NOTE_EAS\"",
                "\"name\":\"NOTE_SR\"",
                "\"name\":\"NOTE_CSG\"",
                "\"name\":\"NOTE_BR\"",
                "\"name\":\"NOTE_BCR\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_PRIMITIVE_SOLID\"",
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
    void shouldBindPmiTargetsThroughAnnotationAndOffsetWrapperDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #10=REPRESENTATION('REP_ANNOTATION_OFFSET',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #13=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #14=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #15=DIRECTION('DX',(1.0,0.0,0.0));
                #16=DIRECTION('DY',(0.0,1.0,0.0));
                #17=DIRECTION('DZ',(0.0,0.0,1.0));
                #18=VECTOR('VX',#15,1.0);
                #19=AXIS2_PLACEMENT_3D('AX3',#12,#17,#15);
                #20=LINE('L0',#12,#18);
                #21=OFFSET_CURVE_3D('OC3',#20,0.5,.F.,#16);
                #22=ORIENTED_CURVE('ORC0',#21,.F.);
                #23=B_SPLINE_CURVE_WITH_KNOTS('BC0',1,(#12,#13),.UNSPECIFIED.,.F.,.F.,(2,2),(0.0,1.0),.PIECEWISE_BEZIER_KNOTS.);
                #24=(B_SPLINE_SURFACE(1,1,((#12,#13),(#14,#12)),.UNSPECIFIED.,.F.,.F.,.F.)
                     RATIONAL_B_SPLINE_SURFACE(((1.0,1.0),(1.0,1.0))));
                #25=POLYLINE('PL0',(#12,#13,#14));
                #26=PRESENTATION_STYLE_ASSIGNMENT(());
                #27=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#26),#22);
                #28=ANNOTATION_FILL_AREA('AFA0',(#25));
                #29=ANNOTATION_FILL_AREA_OCCURRENCE('AFAO0',(#26),#28,#12);
                #30=GEOMETRIC_SET('GS0',(#12,#13));
                #31=ANNOTATION_PLACEHOLDER_OCCURRENCE('APO0',(#26),#30,.END.,1.0);
                #32=(ANNOTATION_POINT_OCCURRENCE('AP0',(#26),#12) DRAUGHTING_ANNOTATION_OCCURRENCE('AP0',(#26),#12));
                #33=ANNOTATION_TEXT_OCCURRENCE('AT0','note',#13);
                #34=ANNOTATION_CURVE_OCCURRENCE('LC0',(#26),#20);
                #35=DIMENSION_CURVE('DC0',(#26),#20);
                #36=LEADER_CURVE('LD0',(#26),#20);
                #37=PROJECTION_CURVE('PC0',(#26),#20);
                #38=SYMBOL_REPRESENTATION_MAP(#19,#10);
                #39=ANNOTATION_SYMBOL('AS0',#38,#19);
                #40=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#26),#39);
                #41=ANNOTATION_SUBFIGURE_OCCURRENCE('SUB0',(#26),#39);
                #42=TERMINATOR_SYMBOL('TS0',(#26),#39,#34);
                #60=PROPERTY_DEFINITION('PD_OC3','',#21);
                #61=PROPERTY_DEFINITION('PD_ORC','',#22);
                #62=PROPERTY_DEFINITION('PD_BSC','',#23);
                #63=PROPERTY_DEFINITION('PD_RBS','',#24);
                #64=PROPERTY_DEFINITION('PD_ACO','',#27);
                #65=PROPERTY_DEFINITION('PD_AFA','',#28);
                #66=PROPERTY_DEFINITION('PD_AFAO','',#29);
                #67=PROPERTY_DEFINITION('PD_APO','',#31);
                #68=PROPERTY_DEFINITION('PD_AP','',#32);
                #69=PROPERTY_DEFINITION('PD_AT','',#33);
                #70=PROPERTY_DEFINITION('PD_DC','',#35);
                #71=PROPERTY_DEFINITION('PD_LD','',#36);
                #72=PROPERTY_DEFINITION('PD_PC','',#37);
                #73=PROPERTY_DEFINITION('PD_ASO','',#40);
                #74=PROPERTY_DEFINITION('PD_SUB','',#41);
                #75=PROPERTY_DEFINITION('PD_TS','',#42);
                #80=PROPERTY_DEFINITION_REPRESENTATION(#60,#10);
                #81=PROPERTY_DEFINITION_REPRESENTATION(#61,#10);
                #82=PROPERTY_DEFINITION_REPRESENTATION(#62,#10);
                #83=PROPERTY_DEFINITION_REPRESENTATION(#63,#10);
                #84=PROPERTY_DEFINITION_REPRESENTATION(#64,#10);
                #85=PROPERTY_DEFINITION_REPRESENTATION(#65,#10);
                #86=PROPERTY_DEFINITION_REPRESENTATION(#66,#10);
                #87=PROPERTY_DEFINITION_REPRESENTATION(#67,#10);
                #88=PROPERTY_DEFINITION_REPRESENTATION(#68,#10);
                #89=PROPERTY_DEFINITION_REPRESENTATION(#69,#10);
                #90=PROPERTY_DEFINITION_REPRESENTATION(#70,#10);
                #91=PROPERTY_DEFINITION_REPRESENTATION(#71,#10);
                #92=PROPERTY_DEFINITION_REPRESENTATION(#72,#10);
                #93=PROPERTY_DEFINITION_REPRESENTATION(#73,#10);
                #94=PROPERTY_DEFINITION_REPRESENTATION(#74,#10);
                #95=PROPERTY_DEFINITION_REPRESENTATION(#75,#10);
                #100=CARTESIAN_POINT('N0',(0.0,0.0,0.0));
                #101=CARTESIAN_POINT('N1',(1.0,0.0,0.0));
                #102=CARTESIAN_POINT('N2',(2.0,0.0,0.0));
                #103=CARTESIAN_POINT('N3',(3.0,0.0,0.0));
                #104=CARTESIAN_POINT('N4',(4.0,0.0,0.0));
                #105=CARTESIAN_POINT('N5',(5.0,0.0,0.0));
                #106=CARTESIAN_POINT('N6',(6.0,0.0,0.0));
                #107=CARTESIAN_POINT('N7',(7.0,0.0,0.0));
                #108=CARTESIAN_POINT('N8',(8.0,0.0,0.0));
                #109=CARTESIAN_POINT('N9',(9.0,0.0,0.0));
                #110=CARTESIAN_POINT('N10',(10.0,0.0,0.0));
                #111=CARTESIAN_POINT('N11',(11.0,0.0,0.0));
                #112=CARTESIAN_POINT('N12',(12.0,0.0,0.0));
                #113=CARTESIAN_POINT('N13',(13.0,0.0,0.0));
                #114=CARTESIAN_POINT('N14',(14.0,0.0,0.0));
                #115=CARTESIAN_POINT('N15',(15.0,0.0,0.0));
                #116=ANNOTATION_TEXT_OCCURRENCE('NOTE_OC3','',#100);
                #117=ANNOTATION_TEXT_OCCURRENCE('NOTE_ORC','',#101);
                #118=ANNOTATION_TEXT_OCCURRENCE('NOTE_BSC','',#102);
                #119=ANNOTATION_TEXT_OCCURRENCE('NOTE_RBS','',#103);
                #120=ANNOTATION_TEXT_OCCURRENCE('NOTE_ACO','',#104);
                #121=ANNOTATION_TEXT_OCCURRENCE('NOTE_AFA','',#105);
                #122=ANNOTATION_TEXT_OCCURRENCE('NOTE_AFAO','',#106);
                #123=ANNOTATION_TEXT_OCCURRENCE('NOTE_APO','',#107);
                #124=ANNOTATION_TEXT_OCCURRENCE('NOTE_AP','',#108);
                #125=ANNOTATION_TEXT_OCCURRENCE('NOTE_AT','',#109);
                #126=ANNOTATION_TEXT_OCCURRENCE('NOTE_DC','',#110);
                #127=ANNOTATION_TEXT_OCCURRENCE('NOTE_LD','',#111);
                #128=ANNOTATION_TEXT_OCCURRENCE('NOTE_PC','',#112);
                #129=ANNOTATION_TEXT_OCCURRENCE('NOTE_ASO','',#113);
                #130=ANNOTATION_TEXT_OCCURRENCE('NOTE_SUB','',#114);
                #131=ANNOTATION_TEXT_OCCURRENCE('NOTE_TS','',#115);
                #140=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#21,#10,#116,#8);
                #141=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#22,#10,#117,#8);
                #142=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#23,#10,#118,#8);
                #143=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#24,#10,#119,#8);
                #144=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#27,#10,#120,#8);
                #145=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#28,#10,#121,#8);
                #146=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#29,#10,#122,#8);
                #147=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#31,#10,#123,#8);
                #148=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#32,#10,#124,#8);
                #149=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#33,#10,#125,#8);
                #150=PMI_REQUIREMENT_ITEM_ASSOCIATION('A10','',#35,#10,#126,#8);
                #151=PMI_REQUIREMENT_ITEM_ASSOCIATION('A11','',#36,#10,#127,#8);
                #152=PMI_REQUIREMENT_ITEM_ASSOCIATION('A12','',#37,#10,#128,#8);
                #153=PMI_REQUIREMENT_ITEM_ASSOCIATION('A13','',#40,#10,#129,#8);
                #154=PMI_REQUIREMENT_ITEM_ASSOCIATION('A14','',#41,#10,#130,#8);
                #155=PMI_REQUIREMENT_ITEM_ASSOCIATION('A15','',#42,#10,#131,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_OC3\"",
                "\"name\":\"NOTE_ORC\"",
                "\"name\":\"NOTE_BSC\"",
                "\"name\":\"NOTE_RBS\"",
                "\"name\":\"NOTE_ACO\"",
                "\"name\":\"NOTE_AFA\"",
                "\"name\":\"NOTE_AFAO\"",
                "\"name\":\"NOTE_APO\"",
                "\"name\":\"NOTE_AP\"",
                "\"name\":\"NOTE_AT\"",
                "\"name\":\"NOTE_DC\"",
                "\"name\":\"NOTE_LD\"",
                "\"name\":\"NOTE_PC\"",
                "\"name\":\"NOTE_ASO\"",
                "\"name\":\"NOTE_SUB\"",
                "\"name\":\"NOTE_TS\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_ANNOTATION_OFFSET\"",
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
                "\"viaDefinitionId\":39",
                "\"viaDefinitionType\":\"SYMBOL_REPRESENTATION_MAP\"",
                "\"viaDefinitionId\":38");
    }

    @Test
    void shouldBindPmiTargetsThroughRationalCurveAndProfileDefinitions() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #10=REPRESENTATION('REP_PROFILE_CURVE',(),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #13=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #14=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #15=DIRECTION('DX',(1.0,0.0,0.0));
                #16=AXIS2_PLACEMENT_2D('AX2',#12,#15);
                #17=POLYLINE('PLC',(#12,#13,#14,#12));
                #18=POLYLINE('PLO',(#12,#13,#14));
                #19=(B_SPLINE_CURVE('RBC0',2,(#12,#13,#14),.UNSPECIFIED.,.F.,.F.)
                     B_SPLINE_CURVE_WITH_KNOTS((3,3),(0.0,1.0),.UNSPECIFIED.)
                     RATIONAL_B_SPLINE_CURVE((1.0,0.5,1.0)));
                #20=CIRCLE_PROFILE_DEF(.AREA.,'C',#16,2.0);
                #21=RECTANGLE_PROFILE_DEF(.AREA.,'R',#16,4.0,2.0);
                #22=CENTERED_RECTANGLE_PROFILE_DEF(.AREA.,'CR',#16,3.0,5.0);
                #23=ELLIPSE_PROFILE_DEF(.AREA.,'E',#16,3.0,1.5);
                #24=ROUNDED_RECTANGLE_PROFILE_DEF(.AREA.,'RR',#16,6.0,4.0,0.5);
                #25=CIRCULAR_HOLLOW_PROFILE_DEF(.AREA.,'CH',#16,3.0,0.5);
                #26=ARBITRARY_CLOSED_PROFILE_DEF(.AREA.,'ACP',#17);
                #27=ARBITRARY_PROFILE_DEF(.AREA.,'AP',#17);
                #28=ARBITRARY_OPEN_PROFILE_DEF(.CURVE.,'AOP',#18);
                #60=PROPERTY_DEFINITION('PD_RBC','',#19);
                #61=PROPERTY_DEFINITION('PD_C','',#20);
                #62=PROPERTY_DEFINITION('PD_R','',#21);
                #63=PROPERTY_DEFINITION('PD_CR','',#22);
                #64=PROPERTY_DEFINITION('PD_E','',#23);
                #65=PROPERTY_DEFINITION('PD_RR','',#24);
                #66=PROPERTY_DEFINITION('PD_CH','',#25);
                #67=PROPERTY_DEFINITION('PD_ACP','',#26);
                #68=PROPERTY_DEFINITION('PD_AP','',#27);
                #69=PROPERTY_DEFINITION('PD_AOP','',#28);
                #80=PROPERTY_DEFINITION_REPRESENTATION(#60,#10);
                #81=PROPERTY_DEFINITION_REPRESENTATION(#61,#10);
                #82=PROPERTY_DEFINITION_REPRESENTATION(#62,#10);
                #83=PROPERTY_DEFINITION_REPRESENTATION(#63,#10);
                #84=PROPERTY_DEFINITION_REPRESENTATION(#64,#10);
                #85=PROPERTY_DEFINITION_REPRESENTATION(#65,#10);
                #86=PROPERTY_DEFINITION_REPRESENTATION(#66,#10);
                #87=PROPERTY_DEFINITION_REPRESENTATION(#67,#10);
                #88=PROPERTY_DEFINITION_REPRESENTATION(#68,#10);
                #89=PROPERTY_DEFINITION_REPRESENTATION(#69,#10);
                #100=CARTESIAN_POINT('N0',(0.0,0.0,0.0));
                #101=CARTESIAN_POINT('N1',(1.0,0.0,0.0));
                #102=CARTESIAN_POINT('N2',(2.0,0.0,0.0));
                #103=CARTESIAN_POINT('N3',(3.0,0.0,0.0));
                #104=CARTESIAN_POINT('N4',(4.0,0.0,0.0));
                #105=CARTESIAN_POINT('N5',(5.0,0.0,0.0));
                #106=CARTESIAN_POINT('N6',(6.0,0.0,0.0));
                #107=CARTESIAN_POINT('N7',(7.0,0.0,0.0));
                #108=CARTESIAN_POINT('N8',(8.0,0.0,0.0));
                #109=CARTESIAN_POINT('N9',(9.0,0.0,0.0));
                #110=ANNOTATION_TEXT_OCCURRENCE('NOTE_RBC','',#100);
                #111=ANNOTATION_TEXT_OCCURRENCE('NOTE_C','',#101);
                #112=ANNOTATION_TEXT_OCCURRENCE('NOTE_R','',#102);
                #113=ANNOTATION_TEXT_OCCURRENCE('NOTE_CR','',#103);
                #114=ANNOTATION_TEXT_OCCURRENCE('NOTE_E','',#104);
                #115=ANNOTATION_TEXT_OCCURRENCE('NOTE_RR','',#105);
                #116=ANNOTATION_TEXT_OCCURRENCE('NOTE_CH','',#106);
                #117=ANNOTATION_TEXT_OCCURRENCE('NOTE_ACP','',#107);
                #118=ANNOTATION_TEXT_OCCURRENCE('NOTE_AP','',#108);
                #119=ANNOTATION_TEXT_OCCURRENCE('NOTE_AOP','',#109);
                #130=PMI_REQUIREMENT_ITEM_ASSOCIATION('A0','',#19,#10,#110,#8);
                #131=PMI_REQUIREMENT_ITEM_ASSOCIATION('A1','',#20,#10,#111,#8);
                #132=PMI_REQUIREMENT_ITEM_ASSOCIATION('A2','',#21,#10,#112,#8);
                #133=PMI_REQUIREMENT_ITEM_ASSOCIATION('A3','',#22,#10,#113,#8);
                #134=PMI_REQUIREMENT_ITEM_ASSOCIATION('A4','',#23,#10,#114,#8);
                #135=PMI_REQUIREMENT_ITEM_ASSOCIATION('A5','',#24,#10,#115,#8);
                #136=PMI_REQUIREMENT_ITEM_ASSOCIATION('A6','',#25,#10,#116,#8);
                #137=PMI_REQUIREMENT_ITEM_ASSOCIATION('A7','',#26,#10,#117,#8);
                #138=PMI_REQUIREMENT_ITEM_ASSOCIATION('A8','',#27,#10,#118,#8);
                #139=PMI_REQUIREMENT_ITEM_ASSOCIATION('A9','',#28,#10,#119,#8);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"name\":\"NOTE_RBC\"",
                "\"name\":\"NOTE_C\"",
                "\"name\":\"NOTE_R\"",
                "\"name\":\"NOTE_CR\"",
                "\"name\":\"NOTE_E\"",
                "\"name\":\"NOTE_RR\"",
                "\"name\":\"NOTE_CH\"",
                "\"name\":\"NOTE_ACP\"",
                "\"name\":\"NOTE_AP\"",
                "\"name\":\"NOTE_AOP\"",
                "\"id\":10,\"type\":\"representation\",\"name\":\"REP_PROFILE_CURVE\"",
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
    void shouldExportStyledItemWrappedGeometryInRepresentation() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=PRESENTATION_STYLE_ASSIGNMENT(());
                #6=STYLED_ITEM('S0',(#5),#4);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #11=SHAPE_REPRESENTATION('WIRE',(#6),#10);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"representationCount\":1",
                "\"representations\":[{\"id\":11",
                "\"edges\":[{\"id\":4",
                "\"points\":[[0.0,0.0,0.0],[1.0,0.0,0.0]]");
    }

    @Test
    void shouldExportStyledItemWrappedSolidInRepresentation() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O2',(0.0,0.0));
                #2=DIRECTION('DX2',(1.0,0.0));
                #3=AXIS2_PLACEMENT_2D('P2',#1,#2);
                #4=CARTESIAN_POINT('O3',(0.0,0.0,0.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX',#4,#5,#6);
                #8=RECTANGLE_PROFILE_DEF(.AREA.,'R0',#3,2.0,1.0);
                #9=EXTRUDED_AREA_SOLID('SOL0',#8,#7,#5,1.0);
                #10=PRESENTATION_STYLE_ASSIGNMENT(());
                #11=STYLED_ITEM('SS0',(#10),#9);
                #12=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #13=SHAPE_REPRESENTATION('SOLID',(#11),#12);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"representationCount\":1",
                "\"solidCount\":1",
                "\"representations\":[{\"id\":13",
                "\"faces\":[");
    }

    @Test
    void shouldExportInnerLoopForRoundHoleExample() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/plate-with-round-hole.step")));

        assertJsonContains(json,
                "\"faceCount\":1",
                "\"edgeCount\":8",
                "\"outer\":false");
    }

    @Test
    void shouldExportPreviewJsonForFanExample() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/fan.stp")));

        assertJsonContains(json,
                "\"representationCount\":1",
                "\"instanceCount\":1",
                "\"unsupportedFaceCount\":1",
                "\"surfaceType\":\"MANIFOLD_SOLID_BREP\"",
                "\"reason\":\"face must contain an outer bound\"",
                "\"edges\":[],\"faces\":[],\"representations\":[");
        assertTrue(json.length() < 250_000_000);
    }

    @Test
    void shouldOmitDuplicateLegacyGeometryForAssemblyPayloads() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/two-instance-assembly.step")));

        assertJsonContains(json,
                "\"representationCount\":2",
                "\"instanceCount\":3",
                "\"unsupportedFaces\":[],\"edges\":[],\"faces\":[],\"representations\":[");
    }

    @Test
    void shouldExportPolylineRepresentationEdgePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=POLYLINE('PL0',(#1,#2,#3));
                #5=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #6=SHAPE_REPRESENTATION('WIRE',(#4),#5);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"representationCount\":1",
                "\"faceCount\":0",
                "\"edgeCount\":1",
                "\"representations\":[{\"id\":6",
                "\"edges\":[{\"id\":4",
                "\"points\":[[0.0,0.0,0.0],[1.0,0.0,0.0],[1.0,1.0,0.0]]");
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

        assertJsonContains(json,
                "\"faceCount\":0",
                "\"edgeCount\":0",
                "\"unsupportedFaceCount\":1",
                "\"unsupportedFaces\":[{\"id\":70",
                "\"surfaceType\":\"CYLINDRICAL_SURFACE\"",
                "\"reason\":");
    }

    @Test
    void shouldSkipCylindricalFaceWithVertexLoopOuterBound() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #13=CYLINDRICAL_SURFACE('CY0',#12,1.0);
                #20=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #21=VERTEX_POINT('V0',#20);
                #22=VERTEX_LOOP('VL0',#21);
                #23=FACE_OUTER_BOUND('B0',#22,.T.);
                #24=ADVANCED_FACE('F0',(#23),#13,.T.);
                #25=OPEN_SHELL('OS',(#24));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"faceCount\":0",
                "\"unsupportedFaceCount\":1");
    }

    @Test
    void shouldReportUnderlyingUnsupportedSurfaceTypeThroughWrappers() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #8=(BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BS0'));
                #9=RECTANGULAR_TRIMMED_SURFACE('RTS0',#8,0.0,1.0,0.0,1.0,.T.,.T.);
                #10=ORIENTED_SURFACE('OS0',#9,.T.);
                #11=OFFSET_SURFACE('OFS0',#10,1.0,.F.);
                #12=CARTESIAN_TRANSFORMATION_OPERATOR_3D('X0',#3,$,#1,1.0,#2);
                #13=SURFACE_REPLICA('SR0',#11,#12);
                #14=POLY_LOOP('LOOP0',(#1,#1,#1));
                #15=FACE_OUTER_BOUND('B0',#14,.T.);
                #16=ADVANCED_FACE('F0',(#15),#13,.T.);
                #17=OPEN_SHELL('OSHELL',(#16));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"unsupportedFaceCount\":1",
                "\"unsupportedFaces\":[{\"id\":16",
                "\"surfaceType\":\"SURFACE_REPLICA\"",
                "\"reason\":\"BOUNDED_SURFACE preview is unsupported\"");
    }

    @Test
    void shouldExportSphericalAndDegenerateToroidalFaces() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,2.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,0.0,2.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=SPHERICAL_SURFACE('SPH0',#7,2.0);
                #9=DEGENERATE_TOROIDAL_SURFACE('DTS0',#7,5.0,1.0,.T.);
                #10=POLY_LOOP('L0',(#2,#3,#4));
                #11=FACE_OUTER_BOUND('B0',#10,.T.);
                #12=ADVANCED_FACE('F0',(#11),#8,.T.);
                #13=ADVANCED_FACE('F1',(#11),#9,.T.);
                #14=OPEN_SHELL('OS',(#12,#13));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"faceCount\":2",
                "\"unsupportedFaceCount\":0",
                "\"surfaceType\":\"SPHERICAL_SURFACE\"",
                "\"surfaceType\":\"DEGENERATE_TOROIDAL_SURFACE\"",
                "\"triangles\":[[");
    }

    @Test
    void shouldExportOffsetSphericalFacePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(3.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,3.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,0.0,3.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=SPHERICAL_SURFACE('SPH0',#7,2.0);
                #9=OFFSET_SURFACE('OFS0',#8,1.0,.F.);
                #10=POLY_LOOP('L0',(#2,#3,#4));
                #11=FACE_OUTER_BOUND('B0',#10,.T.);
                #12=ADVANCED_FACE('F0',(#11),#9,.T.);
                #13=OPEN_SHELL('OS',(#12));
                ENDSEC;
                """);

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "OFFSET_SURFACE");
    }

    @Test
    void shouldExportReplicaSphericalFacePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #3=CARTESIAN_POINT('P1',(2.0,0.0,2.0));
                #4=CARTESIAN_POINT('P2',(0.0,2.0,2.0));
                #5=CARTESIAN_POINT('P3',(0.0,0.0,4.0));
                #6=DIRECTION('DZ',(0.0,0.0,1.0));
                #7=DIRECTION('DX',(1.0,0.0,0.0));
                #8=AXIS2_PLACEMENT_3D('AX3',#1,#6,#7);
                #9=SPHERICAL_SURFACE('SPH0',#8,2.0);
                #10=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#2,1.0,$);
                #11=SURFACE_REPLICA('SR0',#9,#10);
                #12=POLY_LOOP('L0',(#3,#4,#5));
                #13=FACE_OUTER_BOUND('B0',#12,.T.);
                #14=ADVANCED_FACE('F0',(#13),#11,.T.);
                #15=OPEN_SHELL('OS',(#14));
                ENDSEC;
                """);

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "SURFACE_REPLICA");
    }

    @Test
    void shouldExportUniformScaledReplicaSphericalFacePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #3=CARTESIAN_POINT('P1',(4.0,0.0,2.0));
                #4=CARTESIAN_POINT('P2',(0.0,4.0,2.0));
                #5=CARTESIAN_POINT('P3',(0.0,0.0,6.0));
                #6=DIRECTION('DZ',(0.0,0.0,1.0));
                #7=DIRECTION('DX',(1.0,0.0,0.0));
                #8=AXIS2_PLACEMENT_3D('AX3',#1,#6,#7);
                #9=SPHERICAL_SURFACE('SPH0',#8,2.0);
                #10=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#2,2.0,$);
                #11=SURFACE_REPLICA('SR0',#9,#10);
                #12=POLY_LOOP('L0',(#3,#4,#5));
                #13=FACE_OUTER_BOUND('B0',#12,.T.);
                #14=ADVANCED_FACE('F0',(#13),#11,.T.);
                #15=OPEN_SHELL('OS',(#14));
                ENDSEC;
                """);

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "SURFACE_REPLICA");
    }

    @Test
    void shouldExportOffsetSphericalFaceUsingTerminatorWrappedEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(3.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,3.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,0.0,3.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=SPHERICAL_SURFACE('SPH0',#7,2.0);
                #9=OFFSET_SURFACE('OFS0',#8,1.0,.F.);
                #10=VERTEX_POINT('V0',#2);
                #11=VERTEX_POINT('V1',#3);
                #12=VERTEX_POINT('V2',#4);
                #13=DIRECTION('DA',(-1.0,1.0,0.0));
                #14=VECTOR('VA',#13,1.0);
                #15=LINE('L0',#2,#14);
                #16=DIRECTION('DB',(0.0,-1.0,1.0));
                #17=VECTOR('VB',#16,1.0);
                #18=LINE('L1',#3,#17);
                #19=DIRECTION('DC',(-1.0,0.0,1.0));
                #20=VECTOR('VC',#19,1.0);
                #21=LINE('L2',#2,#20);
                #22=PRESENTATION_STYLE_ASSIGNMENT(());
                #23=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #24=DIRECTION('SD0',(1.0,0.0));
                #25=AXIS2_PLACEMENT_2D('MAP',#23,#24);
                #26=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #27=REPRESENTATION('SYMREP',(),#26);
                #28=SYMBOL_REPRESENTATION_MAP(#25,#27);
                #29=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #30=DIRECTION('SD1',(1.0,0.0));
                #31=AXIS2_PLACEMENT_2D('TGT',#29,#30);
                #32=ANNOTATION_SYMBOL('AS0',#28,#31);
                #33=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#22),#15);
                #34=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#22),#21);
                #35=TERMINATOR_SYMBOL('TS0',(#22),#32,#33);
                #36=TERMINATOR_SYMBOL('TS1',(#22),#32,#34);
                #37=EDGE_CURVE('E0',#10,#11,#35,.T.);
                #38=EDGE_CURVE('E1',#11,#12,#18,.T.);
                #39=EDGE_CURVE('E2',#10,#12,#36,.T.);
                #40=ORIENTED_EDGE('OE0',$,$,#37,.T.);
                #41=ORIENTED_EDGE('OE1',$,$,#38,.T.);
                #42=ORIENTED_EDGE('OE2',$,$,#39,.F.);
                #43=EDGE_LOOP('L0',(#40,#41,#42));
                #44=FACE_OUTER_BOUND('B0',#43,.T.);
                #45=ADVANCED_FACE('F0',(#44),#9,.T.);
                #46=OPEN_SHELL('OS',(#45));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "OFFSET_SURFACE", 5, true);
    }

    @Test
    void shouldExportOrientedSphericalFaceUsingTerminatorWrappedEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,2.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,0.0,2.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=SPHERICAL_SURFACE('SPH0',#7,2.0);
                #9=ORIENTED_SURFACE('OS0',#8,.T.);
                #10=VERTEX_POINT('V0',#2);
                #11=VERTEX_POINT('V1',#3);
                #12=VERTEX_POINT('V2',#4);
                #13=DIRECTION('DA',(-1.0,1.0,0.0));
                #14=VECTOR('VA',#13,1.0);
                #15=LINE('L0',#2,#14);
                #16=DIRECTION('DB',(0.0,-1.0,1.0));
                #17=VECTOR('VB',#16,1.0);
                #18=LINE('L1',#3,#17);
                #19=DIRECTION('DC',(-1.0,0.0,1.0));
                #20=VECTOR('VC',#19,1.0);
                #21=LINE('L2',#2,#20);
                #22=PRESENTATION_STYLE_ASSIGNMENT(());
                #23=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #24=DIRECTION('SD0',(1.0,0.0));
                #25=AXIS2_PLACEMENT_2D('MAP',#23,#24);
                #26=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #27=REPRESENTATION('SYMREP',(),#26);
                #28=SYMBOL_REPRESENTATION_MAP(#25,#27);
                #29=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #30=DIRECTION('SD1',(1.0,0.0));
                #31=AXIS2_PLACEMENT_2D('TGT',#29,#30);
                #32=ANNOTATION_SYMBOL('AS0',#28,#31);
                #33=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#22),#15);
                #34=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#22),#21);
                #35=TERMINATOR_SYMBOL('TS0',(#22),#32,#33);
                #36=TERMINATOR_SYMBOL('TS1',(#22),#32,#34);
                #37=EDGE_CURVE('E0',#10,#11,#35,.T.);
                #38=EDGE_CURVE('E1',#11,#12,#18,.T.);
                #39=EDGE_CURVE('E2',#10,#12,#36,.T.);
                #40=ORIENTED_EDGE('OE0',$,$,#37,.T.);
                #41=ORIENTED_EDGE('OE1',$,$,#38,.T.);
                #42=ORIENTED_EDGE('OE2',$,$,#39,.F.);
                #43=EDGE_LOOP('L0',(#40,#41,#42));
                #44=FACE_OUTER_BOUND('B0',#43,.T.);
                #45=ADVANCED_FACE('F0',(#44),#9,.T.);
                #46=OPEN_SHELL('OS',(#45));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "ORIENTED_SURFACE", 5, true);
    }

    @Test
    void shouldExportOrientedToroidalBandUsingTerminatorWrappedEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P00',(5.707106781186548,0.0,-0.7071067811865475));
                #3=CARTESIAN_POINT('P01',(5.707106781186548,0.0,0.7071067811865475));
                #4=CARTESIAN_POINT('P10',(0.0,5.707106781186548,-0.7071067811865475));
                #5=CARTESIAN_POINT('P11',(0.0,5.707106781186548,0.7071067811865475));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=DIRECTION('DY',(0.0,1.0,0.0));
                #13=DIRECTION('NX',(-1.0,0.0,0.0));
                #14=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #15=TOROIDAL_SURFACE('TO0',#14,5.0,1.0);
                #16=ORIENTED_SURFACE('OS0',#15,.T.);
                #17=CARTESIAN_POINT('CL',(0.0,0.0,-0.7071067811865475));
                #18=CARTESIAN_POINT('CU',(0.0,0.0,0.7071067811865475));
                #19=CARTESIAN_POINT('CR',(5.0,0.0,0.0));
                #20=CARTESIAN_POINT('CT',(0.0,5.0,0.0));
                #21=AXIS2_PLACEMENT_3D('AL',#17,#10,#11);
                #22=AXIS2_PLACEMENT_3D('AU',#18,#10,#11);
                #23=AXIS2_PLACEMENT_3D('AR',#19,#12,#11);
                #24=AXIS2_PLACEMENT_3D('AT',#20,#13,#12);
                #25=CIRCLE('C0',#21,5.707106781186548);
                #26=CIRCLE('C1',#24,1.0);
                #27=CIRCLE('C2',#22,5.707106781186548);
                #28=CIRCLE('C3',#23,1.0);
                #29=PRESENTATION_STYLE_ASSIGNMENT(());
                #30=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #31=DIRECTION('SD0',(1.0,0.0));
                #32=AXIS2_PLACEMENT_2D('MAP',#30,#31);
                #33=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #34=REPRESENTATION('SYMREP',(),#33);
                #35=SYMBOL_REPRESENTATION_MAP(#32,#34);
                #36=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #37=DIRECTION('SD1',(1.0,0.0));
                #38=AXIS2_PLACEMENT_2D('TGT',#36,#37);
                #39=ANNOTATION_SYMBOL('AS0',#35,#38);
                #40=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#29),#25);
                #41=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#29),#28);
                #42=TERMINATOR_SYMBOL('TS0',(#29),#39,#40);
                #43=TERMINATOR_SYMBOL('TS1',(#29),#39,#41);
                #50=VERTEX_POINT('V0',#2);
                #51=VERTEX_POINT('V1',#4);
                #52=VERTEX_POINT('V2',#5);
                #53=VERTEX_POINT('V3',#3);
                #60=EDGE_CURVE('E0',#50,#51,#42,.T.);
                #61=EDGE_CURVE('E1',#51,#52,#26,.T.);
                #62=EDGE_CURVE('E2',#53,#52,#27,.T.);
                #63=EDGE_CURVE('E3',#50,#53,#43,.T.);
                #70=ORIENTED_EDGE('OE0',$,$,#60,.T.);
                #71=ORIENTED_EDGE('OE1',$,$,#61,.T.);
                #72=ORIENTED_EDGE('OE2',$,$,#62,.F.);
                #73=ORIENTED_EDGE('OE3',$,$,#63,.F.);
                #80=EDGE_LOOP('L0',(#70,#71,#72,#73));
                #81=FACE_OUTER_BOUND('B0',#80,.T.);
                #90=ADVANCED_FACE('F0',(#81),#16,.T.);
                #91=OPEN_SHELL('OS',(#90));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "ORIENTED_SURFACE", 6, true);
    }

    @Test
    void shouldExportReplicaSphericalFaceUsingTerminatorWrappedReplicaEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #3=CARTESIAN_POINT('P1',(2.0,0.0,2.0));
                #4=CARTESIAN_POINT('P2',(0.0,2.0,2.0));
                #5=CARTESIAN_POINT('P3',(0.0,0.0,4.0));
                #6=DIRECTION('DZ',(0.0,0.0,1.0));
                #7=DIRECTION('DX',(1.0,0.0,0.0));
                #8=AXIS2_PLACEMENT_3D('AX3',#1,#6,#7);
                #9=SPHERICAL_SURFACE('SPH0',#8,2.0);
                #10=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#2,1.0,$);
                #11=SURFACE_REPLICA('SR0',#9,#10);
                #12=CARTESIAN_POINT('Q1',(2.0,0.0,2.0));
                #13=CARTESIAN_POINT('Q2',(0.0,2.0,2.0));
                #14=CARTESIAN_POINT('Q3',(0.0,0.0,4.0));
                #15=VERTEX_POINT('V0',#12);
                #16=VERTEX_POINT('V1',#13);
                #17=VERTEX_POINT('V2',#14);
                #18=DIRECTION('DA',(-1.0,1.0,0.0));
                #19=VECTOR('VA',#18,1.0);
                #20=LINE('L0',#3,#19);
                #21=DIRECTION('DB',(0.0,-1.0,1.0));
                #22=VECTOR('VB',#21,1.0);
                #23=LINE('L1',#4,#22);
                #24=DIRECTION('DC',(-1.0,0.0,1.0));
                #25=VECTOR('VC',#24,1.0);
                #26=LINE('L2',#3,#25);
                #27=CURVE_REPLICA('CR0',#20,#10);
                #28=CURVE_REPLICA('CR1',#23,#10);
                #29=CURVE_REPLICA('CR2',#26,#10);
                #30=PRESENTATION_STYLE_ASSIGNMENT(());
                #31=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #32=DIRECTION('SD0',(1.0,0.0));
                #33=AXIS2_PLACEMENT_2D('MAP',#31,#32);
                #34=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #35=REPRESENTATION('SYMREP',(),#34);
                #36=SYMBOL_REPRESENTATION_MAP(#33,#35);
                #37=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #38=DIRECTION('SD1',(1.0,0.0));
                #39=AXIS2_PLACEMENT_2D('TGT',#37,#38);
                #40=ANNOTATION_SYMBOL('AS0',#36,#39);
                #41=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#30),#27);
                #42=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#30),#29);
                #43=TERMINATOR_SYMBOL('TS0',(#30),#40,#41);
                #44=TERMINATOR_SYMBOL('TS1',(#30),#40,#42);
                #45=EDGE_CURVE('E0',#15,#16,#43,.T.);
                #46=EDGE_CURVE('E1',#16,#17,#28,.T.);
                #47=EDGE_CURVE('E2',#15,#17,#44,.T.);
                #48=ORIENTED_EDGE('OE0',$,$,#45,.T.);
                #49=ORIENTED_EDGE('OE1',$,$,#46,.T.);
                #50=ORIENTED_EDGE('OE2',$,$,#47,.F.);
                #51=EDGE_LOOP('L0',(#48,#49,#50));
                #52=FACE_OUTER_BOUND('B0',#51,.T.);
                #53=ADVANCED_FACE('F0',(#52),#11,.T.);
                #54=OPEN_SHELL('OS',(#53));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "SURFACE_REPLICA", 5, true);
    }

    @Test
    void shouldExportOrientedDegenerateToroidalFacePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(6.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,6.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,0.0,1.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=DEGENERATE_TOROIDAL_SURFACE('DTS0',#7,5.0,1.0,.T.);
                #9=ORIENTED_SURFACE('OS0',#8,.T.);
                #10=POLY_LOOP('L0',(#2,#3,#4));
                #11=FACE_OUTER_BOUND('B0',#10,.T.);
                #12=ADVANCED_FACE('F0',(#11),#9,.T.);
                #13=OPEN_SHELL('OS',(#12));
                ENDSEC;
                """);

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "ORIENTED_SURFACE");
    }

    @Test
    void shouldExportOffsetDegenerateToroidalFacePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(7.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,7.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,0.0,2.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=DEGENERATE_TOROIDAL_SURFACE('DTS0',#7,5.0,1.0,.T.);
                #9=OFFSET_SURFACE('OFS0',#8,1.0,.F.);
                #10=POLY_LOOP('L0',(#2,#3,#4));
                #11=FACE_OUTER_BOUND('B0',#10,.T.);
                #12=ADVANCED_FACE('F0',(#11),#9,.T.);
                #13=OPEN_SHELL('OS',(#12));
                ENDSEC;
                """);

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "OFFSET_SURFACE");
    }

    @Test
    void shouldExportOffsetDegenerateToroidalFaceUsingTerminatorWrappedEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(7.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,7.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,0.0,2.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=DEGENERATE_TOROIDAL_SURFACE('DTS0',#7,5.0,1.0,.T.);
                #9=OFFSET_SURFACE('OFS0',#8,1.0,.F.);
                #10=VERTEX_POINT('V0',#2);
                #11=VERTEX_POINT('V1',#3);
                #12=VERTEX_POINT('V2',#4);
                #13=DIRECTION('DA',(-1.0,1.0,0.0));
                #14=VECTOR('VA',#13,1.0);
                #15=LINE('L0',#2,#14);
                #16=DIRECTION('DB',(0.0,-1.0,1.0));
                #17=VECTOR('VB',#16,1.0);
                #18=LINE('L1',#3,#17);
                #19=DIRECTION('DC',(-1.0,0.0,1.0));
                #20=VECTOR('VC',#19,1.0);
                #21=LINE('L2',#2,#20);
                #22=PRESENTATION_STYLE_ASSIGNMENT(());
                #23=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #24=DIRECTION('SD0',(1.0,0.0));
                #25=AXIS2_PLACEMENT_2D('MAP',#23,#24);
                #26=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #27=REPRESENTATION('SYMREP',(),#26);
                #28=SYMBOL_REPRESENTATION_MAP(#25,#27);
                #29=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #30=DIRECTION('SD1',(1.0,0.0));
                #31=AXIS2_PLACEMENT_2D('TGT',#29,#30);
                #32=ANNOTATION_SYMBOL('AS0',#28,#31);
                #33=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#22),#15);
                #34=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#22),#21);
                #35=TERMINATOR_SYMBOL('TS0',(#22),#32,#33);
                #36=TERMINATOR_SYMBOL('TS1',(#22),#32,#34);
                #37=EDGE_CURVE('E0',#10,#11,#35,.T.);
                #38=EDGE_CURVE('E1',#11,#12,#18,.T.);
                #39=EDGE_CURVE('E2',#10,#12,#36,.T.);
                #40=ORIENTED_EDGE('OE0',$,$,#37,.T.);
                #41=ORIENTED_EDGE('OE1',$,$,#38,.T.);
                #42=ORIENTED_EDGE('OE2',$,$,#39,.F.);
                #43=EDGE_LOOP('L0',(#40,#41,#42));
                #44=FACE_OUTER_BOUND('B0',#43,.T.);
                #45=ADVANCED_FACE('F0',(#44),#9,.T.);
                #46=OPEN_SHELL('OS',(#45));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"OFFSET_SURFACE\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":5"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldExportReplicaDegenerateToroidalFaceUsingTerminatorWrappedReplicaEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #3=CARTESIAN_POINT('P1',(6.0,0.0,2.0));
                #4=CARTESIAN_POINT('P2',(0.0,6.0,2.0));
                #5=CARTESIAN_POINT('P3',(0.0,0.0,3.0));
                #6=DIRECTION('DZ',(0.0,0.0,1.0));
                #7=DIRECTION('DX',(1.0,0.0,0.0));
                #8=AXIS2_PLACEMENT_3D('AX3',#1,#6,#7);
                #9=DEGENERATE_TOROIDAL_SURFACE('DTS0',#8,5.0,1.0,.T.);
                #10=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#2,1.0,$);
                #11=SURFACE_REPLICA('SR0',#9,#10);
                #12=VERTEX_POINT('V0',#3);
                #13=VERTEX_POINT('V1',#4);
                #14=VERTEX_POINT('V2',#5);
                #15=DIRECTION('DA',(-1.0,1.0,0.0));
                #16=VECTOR('VA',#15,1.0);
                #17=LINE('L0',#3,#16);
                #18=DIRECTION('DB',(0.0,-1.0,1.0));
                #19=VECTOR('VB',#18,1.0);
                #20=LINE('L1',#4,#19);
                #21=DIRECTION('DC',(-1.0,0.0,1.0));
                #22=VECTOR('VC',#21,1.0);
                #23=LINE('L2',#3,#22);
                #24=CURVE_REPLICA('CR0',#17,#10);
                #25=CURVE_REPLICA('CR1',#20,#10);
                #26=CURVE_REPLICA('CR2',#23,#10);
                #27=PRESENTATION_STYLE_ASSIGNMENT(());
                #28=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #29=DIRECTION('SD0',(1.0,0.0));
                #30=AXIS2_PLACEMENT_2D('MAP',#28,#29);
                #31=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #32=REPRESENTATION('SYMREP',(),#31);
                #33=SYMBOL_REPRESENTATION_MAP(#30,#32);
                #34=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #35=DIRECTION('SD1',(1.0,0.0));
                #36=AXIS2_PLACEMENT_2D('TGT',#34,#35);
                #37=ANNOTATION_SYMBOL('AS0',#33,#36);
                #38=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#27),#24);
                #39=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#27),#26);
                #40=TERMINATOR_SYMBOL('TS0',(#27),#37,#38);
                #41=TERMINATOR_SYMBOL('TS1',(#27),#37,#39);
                #42=EDGE_CURVE('E0',#12,#13,#40,.T.);
                #43=EDGE_CURVE('E1',#13,#14,#25,.T.);
                #44=EDGE_CURVE('E2',#12,#14,#41,.T.);
                #45=ORIENTED_EDGE('OE0',$,$,#42,.T.);
                #46=ORIENTED_EDGE('OE1',$,$,#43,.T.);
                #47=ORIENTED_EDGE('OE2',$,$,#44,.F.);
                #48=EDGE_LOOP('L0',(#45,#46,#47));
                #49=FACE_OUTER_BOUND('B0',#48,.T.);
                #50=ADVANCED_FACE('F0',(#49),#11,.T.);
                #51=OPEN_SHELL('OS',(#50));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "SURFACE_REPLICA", 5, true);
    }

    @Test
    void shouldExportReplicaDegenerateToroidalFacePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #3=CARTESIAN_POINT('P1',(6.0,0.0,2.0));
                #4=CARTESIAN_POINT('P2',(0.0,6.0,2.0));
                #5=CARTESIAN_POINT('P3',(0.0,0.0,3.0));
                #6=DIRECTION('DZ',(0.0,0.0,1.0));
                #7=DIRECTION('DX',(1.0,0.0,0.0));
                #8=AXIS2_PLACEMENT_3D('AX3',#1,#6,#7);
                #9=DEGENERATE_TOROIDAL_SURFACE('DTS0',#8,5.0,1.0,.T.);
                #10=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#2,1.0,$);
                #11=SURFACE_REPLICA('SR0',#9,#10);
                #12=POLY_LOOP('L0',(#3,#4,#5));
                #13=FACE_OUTER_BOUND('B0',#12,.T.);
                #14=ADVANCED_FACE('F0',(#13),#11,.T.);
                #15=OPEN_SHELL('OS',(#14));
                ENDSEC;
                """);

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "SURFACE_REPLICA");
    }

    @Test
    void shouldExportUniformScaledReplicaDegenerateToroidalFacePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #3=CARTESIAN_POINT('P1',(12.0,0.0,2.0));
                #4=CARTESIAN_POINT('P2',(0.0,12.0,2.0));
                #5=CARTESIAN_POINT('P3',(0.0,0.0,4.0));
                #6=DIRECTION('DZ',(0.0,0.0,1.0));
                #7=DIRECTION('DX',(1.0,0.0,0.0));
                #8=AXIS2_PLACEMENT_3D('AX3',#1,#6,#7);
                #9=DEGENERATE_TOROIDAL_SURFACE('DTS0',#8,5.0,1.0,.T.);
                #10=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#2,2.0,$);
                #11=SURFACE_REPLICA('SR0',#9,#10);
                #12=POLY_LOOP('L0',(#3,#4,#5));
                #13=FACE_OUTER_BOUND('B0',#12,.T.);
                #14=ADVANCED_FACE('F0',(#13),#11,.T.);
                #15=OPEN_SHELL('OS',(#14));
                ENDSEC;
                """);

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "SURFACE_REPLICA");
    }

    @Test
    void shouldExportRectangularTrimmedDegenerateToroidalFacePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(6.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,6.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,0.0,1.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=DEGENERATE_TOROIDAL_SURFACE('DTS0',#7,5.0,1.0,.T.);
                #9=RECTANGULAR_TRIMMED_SURFACE('RTS0',#8,0.0,1.5707963267948966,0.0,1.5707963267948966,.T.,.T.);
                #10=POLY_LOOP('L0',(#2,#3,#4));
                #11=FACE_OUTER_BOUND('B0',#10,.T.);
                #12=ADVANCED_FACE('F0',(#11),#9,.T.);
                #13=OPEN_SHELL('OS',(#12));
                ENDSEC;
                """);

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "RECTANGULAR_TRIMMED_SURFACE");
    }

    @Test
    void shouldExportRectangularTrimmedDegenerateToroidalFaceUsingTerminatorWrappedEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(6.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,6.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,0.0,1.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=DEGENERATE_TOROIDAL_SURFACE('DTS0',#7,5.0,1.0,.T.);
                #9=RECTANGULAR_TRIMMED_SURFACE('RT0',#8,0.0,1.5707963267948966,0.0,1.0,.T.,.T.);
                #10=VERTEX_POINT('V0',#2);
                #11=VERTEX_POINT('V1',#3);
                #12=VERTEX_POINT('V2',#4);
                #13=DIRECTION('DA',(-1.0,1.0,0.0));
                #14=VECTOR('VA',#13,1.0);
                #15=LINE('L0',#2,#14);
                #16=DIRECTION('DB',(0.0,-1.0,1.0));
                #17=VECTOR('VB',#16,1.0);
                #18=LINE('L1',#3,#17);
                #19=DIRECTION('DC',(-1.0,0.0,1.0));
                #20=VECTOR('VC',#19,1.0);
                #21=LINE('L2',#2,#20);
                #22=PRESENTATION_STYLE_ASSIGNMENT(());
                #23=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #24=DIRECTION('SD0',(1.0,0.0));
                #25=AXIS2_PLACEMENT_2D('MAP',#23,#24);
                #26=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #27=REPRESENTATION('SYMREP',(),#26);
                #28=SYMBOL_REPRESENTATION_MAP(#25,#27);
                #29=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #30=DIRECTION('SD1',(1.0,0.0));
                #31=AXIS2_PLACEMENT_2D('TGT',#29,#30);
                #32=ANNOTATION_SYMBOL('AS0',#28,#31);
                #33=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#22),#15);
                #34=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#22),#21);
                #35=TERMINATOR_SYMBOL('TS0',(#22),#32,#33);
                #36=TERMINATOR_SYMBOL('TS1',(#22),#32,#34);
                #37=EDGE_CURVE('E0',#10,#11,#35,.T.);
                #38=EDGE_CURVE('E1',#11,#12,#18,.T.);
                #39=EDGE_CURVE('E2',#10,#12,#36,.T.);
                #40=ORIENTED_EDGE('OE0',$,$,#37,.T.);
                #41=ORIENTED_EDGE('OE1',$,$,#38,.T.);
                #42=ORIENTED_EDGE('OE2',$,$,#39,.F.);
                #43=EDGE_LOOP('L0',(#40,#41,#42));
                #44=FACE_OUTER_BOUND('B0',#43,.T.);
                #45=ADVANCED_FACE('F0',(#44),#9,.T.);
                #46=OPEN_SHELL('OS',(#45));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "RECTANGULAR_TRIMMED_SURFACE", 5, true);
    }

    @Test
    void shouldExportCurveBoundedDegenerateToroidalFacePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(6.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,6.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,0.0,1.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=DEGENERATE_TOROIDAL_SURFACE('DTS0',#7,5.0,1.0,.T.);
                #9=POLYLINE('PL0',(#2,#3,#4,#2));
                #10=CURVE_BOUNDED_SURFACE('CBS0',#8,(#9),.T.);
                #11=POLY_LOOP('L0',(#2,#3,#4));
                #12=FACE_OUTER_BOUND('B0',#11,.T.);
                #13=ADVANCED_FACE('F0',(#12),#10,.T.);
                #14=OPEN_SHELL('OS',(#13));
                ENDSEC;
                """);

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "CURVE_BOUNDED_SURFACE");
    }

    @Test
    void shouldExportCurveBoundedDegenerateToroidalFaceUsingTerminatorWrappedEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(6.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,6.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,0.0,1.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=DEGENERATE_TOROIDAL_SURFACE('DTS0',#7,5.0,1.0,.T.);
                #9=POLYLINE('PL0',(#2,#3,#4,#2));
                #10=CURVE_BOUNDED_SURFACE('CBS0',#8,(#9),.T.);
                #11=VERTEX_POINT('V0',#2);
                #12=VERTEX_POINT('V1',#3);
                #13=VERTEX_POINT('V2',#4);
                #14=DIRECTION('DA',(-1.0,1.0,0.0));
                #15=VECTOR('VA',#14,1.0);
                #16=LINE('L0',#2,#15);
                #17=DIRECTION('DB',(0.0,-1.0,1.0));
                #18=VECTOR('VB',#17,1.0);
                #19=LINE('L1',#3,#18);
                #20=DIRECTION('DC',(-1.0,0.0,1.0));
                #21=VECTOR('VC',#20,1.0);
                #22=LINE('L2',#2,#21);
                #23=PRESENTATION_STYLE_ASSIGNMENT(());
                #24=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #25=DIRECTION('SD0',(1.0,0.0));
                #26=AXIS2_PLACEMENT_2D('MAP',#24,#25);
                #27=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #28=REPRESENTATION('SYMREP',(),#27);
                #29=SYMBOL_REPRESENTATION_MAP(#26,#28);
                #30=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #31=DIRECTION('SD1',(1.0,0.0));
                #32=AXIS2_PLACEMENT_2D('TGT',#30,#31);
                #33=ANNOTATION_SYMBOL('AS0',#29,#32);
                #34=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#23),#16);
                #35=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#23),#22);
                #36=TERMINATOR_SYMBOL('TS0',(#23),#33,#34);
                #37=TERMINATOR_SYMBOL('TS1',(#23),#33,#35);
                #38=EDGE_CURVE('E0',#11,#12,#36,.T.);
                #39=EDGE_CURVE('E1',#12,#13,#19,.T.);
                #40=EDGE_CURVE('E2',#11,#13,#37,.T.);
                #41=ORIENTED_EDGE('OE0',$,$,#38,.T.);
                #42=ORIENTED_EDGE('OE1',$,$,#39,.T.);
                #43=ORIENTED_EDGE('OE2',$,$,#40,.F.);
                #44=EDGE_LOOP('L0',(#41,#42,#43));
                #45=FACE_OUTER_BOUND('B0',#44,.T.);
                #46=ADVANCED_FACE('F0',(#45),#10,.T.);
                #47=OPEN_SHELL('OS',(#46));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "CURVE_BOUNDED_SURFACE", 6, true);
    }

    @Test
    void shouldExportOrientedSphericalFacePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,2.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,0.0,2.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=SPHERICAL_SURFACE('SPH0',#7,2.0);
                #9=ORIENTED_SURFACE('OS0',#8,.T.);
                #10=POLY_LOOP('L0',(#2,#3,#4));
                #11=FACE_OUTER_BOUND('B0',#10,.T.);
                #12=ADVANCED_FACE('F0',(#11),#9,.T.);
                #13=OPEN_SHELL('OS',(#12));
                ENDSEC;
                """);

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "ORIENTED_SURFACE");
    }

    @Test
    void shouldExportRectangularTrimmedSphericalFaceUsingTerminatorWrappedEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,2.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,0.0,2.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=SPHERICAL_SURFACE('SPH0',#7,2.0);
                #9=RECTANGULAR_TRIMMED_SURFACE('RTS0',#8,0.0,1.5707963267948966,0.0,1.5707963267948966,.T.,.T.);
                #10=VERTEX_POINT('V0',#2);
                #11=VERTEX_POINT('V1',#3);
                #12=VERTEX_POINT('V2',#4);
                #13=DIRECTION('DA',(-1.0,1.0,0.0));
                #14=VECTOR('VA',#13,1.0);
                #15=LINE('L0',#2,#14);
                #16=DIRECTION('DB',(0.0,-1.0,1.0));
                #17=VECTOR('VB',#16,1.0);
                #18=LINE('L1',#3,#17);
                #19=DIRECTION('DC',(-1.0,0.0,1.0));
                #20=VECTOR('VC',#19,1.0);
                #21=LINE('L2',#2,#20);
                #22=PRESENTATION_STYLE_ASSIGNMENT(());
                #23=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #24=DIRECTION('SD0',(1.0,0.0));
                #25=AXIS2_PLACEMENT_2D('MAP',#23,#24);
                #26=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #27=REPRESENTATION('SYMREP',(),#26);
                #28=SYMBOL_REPRESENTATION_MAP(#25,#27);
                #29=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #30=DIRECTION('SD1',(1.0,0.0));
                #31=AXIS2_PLACEMENT_2D('TGT',#29,#30);
                #32=ANNOTATION_SYMBOL('AS0',#28,#31);
                #33=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#22),#15);
                #34=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#22),#21);
                #35=TERMINATOR_SYMBOL('TS0',(#22),#32,#33);
                #36=TERMINATOR_SYMBOL('TS1',(#22),#32,#34);
                #37=EDGE_CURVE('E0',#10,#11,#35,.T.);
                #38=EDGE_CURVE('E1',#11,#12,#18,.T.);
                #39=EDGE_CURVE('E2',#10,#12,#36,.T.);
                #40=ORIENTED_EDGE('OE0',$,$,#37,.T.);
                #41=ORIENTED_EDGE('OE1',$,$,#38,.T.);
                #42=ORIENTED_EDGE('OE2',$,$,#39,.F.);
                #43=EDGE_LOOP('L0',(#40,#41,#42));
                #44=FACE_OUTER_BOUND('B0',#43,.T.);
                #45=ADVANCED_FACE('F0',(#44),#9,.T.);
                #46=OPEN_SHELL('OS',(#45));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "RECTANGULAR_TRIMMED_SURFACE", 5, true);
    }

    @Test
    void shouldExportRectangularTrimmedSphericalFacePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,2.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,0.0,2.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=SPHERICAL_SURFACE('SPH0',#7,2.0);
                #9=RECTANGULAR_TRIMMED_SURFACE('RTS0',#8,0.0,1.5707963267948966,0.0,1.5707963267948966,.T.,.T.);
                #10=POLY_LOOP('L0',(#2,#3,#4));
                #11=FACE_OUTER_BOUND('B0',#10,.T.);
                #12=ADVANCED_FACE('F0',(#11),#9,.T.);
                #13=OPEN_SHELL('OS',(#12));
                ENDSEC;
                """);

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "RECTANGULAR_TRIMMED_SURFACE");
    }

    @Test
    void shouldExportCurveBoundedSphericalFaceUsingTerminatorWrappedEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,2.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,0.0,2.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=SPHERICAL_SURFACE('SPH0',#7,2.0);
                #9=POLYLINE('PL0',(#2,#3,#4,#2));
                #10=CURVE_BOUNDED_SURFACE('CBS0',#8,(#9),.T.);
                #11=VERTEX_POINT('V0',#2);
                #12=VERTEX_POINT('V1',#3);
                #13=VERTEX_POINT('V2',#4);
                #14=DIRECTION('DA',(-1.0,1.0,0.0));
                #15=VECTOR('VA',#14,1.0);
                #16=LINE('L0',#2,#15);
                #17=DIRECTION('DB',(0.0,-1.0,1.0));
                #18=VECTOR('VB',#17,1.0);
                #19=LINE('L1',#3,#18);
                #20=DIRECTION('DC',(-1.0,0.0,1.0));
                #21=VECTOR('VC',#20,1.0);
                #22=LINE('L2',#2,#21);
                #23=PRESENTATION_STYLE_ASSIGNMENT(());
                #24=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #25=DIRECTION('SD0',(1.0,0.0));
                #26=AXIS2_PLACEMENT_2D('MAP',#24,#25);
                #27=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #28=REPRESENTATION('SYMREP',(),#27);
                #29=SYMBOL_REPRESENTATION_MAP(#26,#28);
                #30=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #31=DIRECTION('SD1',(1.0,0.0));
                #32=AXIS2_PLACEMENT_2D('TGT',#30,#31);
                #33=ANNOTATION_SYMBOL('AS0',#29,#32);
                #34=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#23),#16);
                #35=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#23),#22);
                #36=TERMINATOR_SYMBOL('TS0',(#23),#33,#34);
                #37=TERMINATOR_SYMBOL('TS1',(#23),#33,#35);
                #38=EDGE_CURVE('E0',#11,#12,#36,.T.);
                #39=EDGE_CURVE('E1',#12,#13,#19,.T.);
                #40=EDGE_CURVE('E2',#11,#13,#37,.T.);
                #41=ORIENTED_EDGE('OE0',$,$,#38,.T.);
                #42=ORIENTED_EDGE('OE1',$,$,#39,.T.);
                #43=ORIENTED_EDGE('OE2',$,$,#40,.F.);
                #44=EDGE_LOOP('L0',(#41,#42,#43));
                #45=FACE_OUTER_BOUND('B0',#44,.T.);
                #46=ADVANCED_FACE('F0',(#45),#10,.T.);
                #47=OPEN_SHELL('OS',(#46));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "CURVE_BOUNDED_SURFACE", 6, true);
    }

    @Test
    void shouldExportCurveBoundedSphericalFacePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,2.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,0.0,2.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX3',#1,#5,#6);
                #8=SPHERICAL_SURFACE('SPH0',#7,2.0);
                #9=POLYLINE('PL0',(#2,#3,#4,#2));
                #10=CURVE_BOUNDED_SURFACE('CBS0',#8,(#9),.T.);
                #11=POLY_LOOP('L0',(#2,#3,#4));
                #12=FACE_OUTER_BOUND('B0',#11,.T.);
                #13=ADVANCED_FACE('F0',(#12),#10,.T.);
                #14=OPEN_SHELL('OS',(#13));
                ENDSEC;
                """);

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "CURVE_BOUNDED_SURFACE");
    }

    @Test
    void shouldReportUnsupportedMarkerSurfaceTypesByStepName() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,1.0,0.0));
                #30=CARTESIAN_POINT('P3',(0.0,0.0,1.0));
                #4=POLY_LOOP('L0',(#1,#2,#3));
                #5=FACE_OUTER_BOUND('B0',#4,.T.);
                #10=(SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('S0'));
                #11=(BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BS0'));
                #12=(B_SPLINE_SURFACE(1,1,((#1,#2),(#3,#30)),.UNSPECIFIED.,.F.,.F.,.F.)
                     BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BSS0'));
                #13=(BEZIER_SURFACE() BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BZS0'));
                #14=(UNIFORM_SURFACE() BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('US0'));
                #15=(QUASI_UNIFORM_SURFACE() BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('QUS0'));
                #16=(PIECEWISE_BEZIER_SURFACE() BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('PBS0'));
                #20=ADVANCED_FACE('F0',(#5),#10,.T.);
                #21=ADVANCED_FACE('F1',(#5),#11,.T.);
                #22=ADVANCED_FACE('F2',(#5),#12,.T.);
                #23=ADVANCED_FACE('F3',(#5),#13,.T.);
                #24=ADVANCED_FACE('F4',(#5),#14,.T.);
                #25=ADVANCED_FACE('F5',(#5),#15,.T.);
                #26=ADVANCED_FACE('F6',(#5),#16,.T.);
                #27=OPEN_SHELL('OS',(#20,#21,#22,#23,#24,#25,#26));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"unsupportedFaceCount\":7",
                "\"surfaceType\":\"SURFACE\"",
                "\"surfaceType\":\"BOUNDED_SURFACE\"",
                "\"surfaceType\":\"B_SPLINE_SURFACE\"",
                "\"surfaceType\":\"BEZIER_SURFACE\"",
                "\"surfaceType\":\"UNIFORM_SURFACE\"",
                "\"surfaceType\":\"QUASI_UNIFORM_SURFACE\"",
                "\"surfaceType\":\"PIECEWISE_BEZIER_SURFACE\"");
    }

    @Test
    void shouldPreviewImplicitBsplineSurfaceSubtypeMarkers() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(1.0,1.0,0.0));
                #5=POLY_LOOP('L0',(#1,#2,#4,#3));
                #6=FACE_OUTER_BOUND('B0',#5,.T.);
                #10=(BEZIER_SURFACE() B_SPLINE_SURFACE(1,1,((#1,#2),(#3,#4)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BZS0'));
                #11=(UNIFORM_SURFACE() B_SPLINE_SURFACE(1,1,((#1,#2),(#3,#4)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('US0'));
                #12=(QUASI_UNIFORM_SURFACE() B_SPLINE_SURFACE(1,1,((#1,#2),(#3,#4)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('QUS0'));
                #13=(PIECEWISE_BEZIER_SURFACE() BEZIER_SURFACE() B_SPLINE_SURFACE(1,1,((#1,#2),(#3,#4)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('PBS0'));
                #20=ADVANCED_FACE('F0',(#6),#10,.T.);
                #21=ADVANCED_FACE('F1',(#6),#11,.T.);
                #22=ADVANCED_FACE('F2',(#6),#12,.T.);
                #23=ADVANCED_FACE('F3',(#6),#13,.T.);
                #24=OPEN_SHELL('OS',(#20,#21,#22,#23));
                ENDSEC;
                """);

        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"surfaceType\":\"BEZIER_SURFACE\""), json);
        assertTrue(json.contains("\"surfaceType\":\"UNIFORM_SURFACE\""), json);
        assertTrue(json.contains("\"surfaceType\":\"QUASI_UNIFORM_SURFACE\""), json);
        assertTrue(json.contains("\"surfaceType\":\"PIECEWISE_BEZIER_SURFACE\""), json);
        assertTrue(json.contains("\"surface\":{\"type\":\"bspline_surface\""), json);
        assertTrue(json.contains("\"surfaceUvLoops\":[") || json.contains("\"uvLoops\":[") || json.contains("\"surface\":{\"type\":\"plane_face\""), json);
    }

    @Test
    void shouldEmbedParametricSurfaceMetadataInJsonPreviewForBsplineFaces() throws Exception {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/bspline-patch.step")));

        assertTrue(json.contains("\"surfaceType\":\"B_SPLINE_SURFACE_WITH_KNOTS\""), json);
        assertTrue(json.contains("\"surface\":{\"type\":\"bspline_surface\""), json);
        assertTrue(json.contains("\"uDegree\":"), json);
        assertTrue(json.contains("\"vDegree\":"), json);
        assertTrue(json.contains("\"controlPoints\":"), json);
        assertTrue(json.contains("\"surfaceUvLoops\":"), json);
    }

    @Test
    void shouldEmbedParametricSurfaceMetadataInJsonPreviewForCylindricalFaces() throws Exception {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/cylindrical-band.step")));

        assertTrue(json.contains("\"surfaceType\":\"CYLINDRICAL_SURFACE\""), json);
        assertTrue(json.contains("\"surface\":{\"type\":\"cylindrical_strip\""), json);
        assertTrue(json.contains("\"radius\":"), json);
        assertTrue(json.contains("\"lowerHeight\":"), json);
        assertTrue(json.contains("\"upperHeight\":"), json);
    }

    @Test
    void shouldEmbedSurfaceWrapperMetadataInJsonPreviewForOffsetSurface() {
        String json = StepPreviewJsonExporter.export(surfaceOfRevolutionFaceStep(
                "#11=OFFSET_SURFACE('OFS0',#10,1.0,.F.);",
                "#11",
                "2.0,0.0,0.0",
                "-2.0,0.0,0.0",
                "-2.0,0.0,1.0",
                "2.0,0.0,1.0"
        ));

        assertTrue(json.contains("\"surfaceType\":\"OFFSET_SURFACE\""), json);
        assertTrue(json.contains("\"surface\":{\"type\":\"surface_of_revolution\"") || json.contains("\"surface\":{\"type\":\"plane_face\"") || json.contains("\"surface\":{\"type\":\"bspline_surface\""), json);
        assertTrue(json.contains("\"sourceType\":\"OFFSET_SURFACE\""), json);
        assertTrue(json.contains("\"sourceStepId\":11"), json);
        assertTrue(json.contains("\"basisType\":\"SURFACE_OF_REVOLUTION\""), json);
        assertTrue(json.contains("\"basisStepId\":10"), json);
        assertTrue(json.contains("\"offsetDistance\":1.0"), json);
    }

    @Test
    void shouldEmbedSurfaceWrapperMetadataInJsonPreviewForSurfaceReplica() {
        String json = StepPreviewJsonExporter.export(surfaceOfRevolutionFaceStep(
                """
                #100=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #101=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#100,1.0,$);
                #102=SURFACE_REPLICA('SR0',#10,#101);
                """,
                "#102",
                "1.0,0.0,2.0",
                "-1.0,0.0,2.0",
                "-1.0,0.0,3.0",
                "1.0,0.0,3.0"
        ));

        assertTrue(json.contains("\"surfaceType\":\"SURFACE_REPLICA\""), json);
        assertTrue(json.contains("\"sourceType\":\"SURFACE_REPLICA\""), json);
        assertTrue(json.contains("\"sourceStepId\":102"), json);
        assertTrue(json.contains("\"basisType\":\"SURFACE_OF_REVOLUTION\""), json);
        assertTrue(json.contains("\"basisStepId\":10"), json);
        assertTrue(json.contains("\"transformScale\":1.0"), json);
    }

    @Test
    void shouldPreviewImplicitBsplineCurveSubtypeMarkersInCurveSet() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(2.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(3.0,1.0,0.0));
                #10=(BEZIER_CURVE() B_SPLINE_CURVE(3,(#1,#2,#3,#4),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BZ0'));
                #11=(UNIFORM_CURVE() B_SPLINE_CURVE(2,(#1,#2,#3,#4),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('UC0'));
                #12=(QUASI_UNIFORM_CURVE() B_SPLINE_CURVE(2,(#1,#2,#3,#4),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('QUC0'));
                #13=(PIECEWISE_BEZIER_CURVE() BEZIER_CURVE() B_SPLINE_CURVE(1,(#1,#2,#3,#4),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('PBC0'));
                #20=GEOMETRIC_CURVE_SET('GCS0',(#10,#11,#12,#13));
                ENDSEC;
                """);

        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"edgeCount\":4"), json);
        assertTrue(json.contains("\"id\":10"), json);
        assertTrue(json.contains("\"id\":11"), json);
        assertTrue(json.contains("\"id\":12"), json);
        assertTrue(json.contains("\"id\":13"), json);
        assertTrue(json.contains("\"curve\":{\"stepId\":10,\"type\":\"BEZIER_CURVE\""), json);
        assertTrue(json.contains("\"curve\":{\"stepId\":11,\"type\":\"UNIFORM_CURVE\""), json);
        assertTrue(json.contains("\"curve\":{\"stepId\":12,\"type\":\"QUASI_UNIFORM_CURVE\""), json);
        assertTrue(json.contains("\"curve\":{\"stepId\":13,\"type\":\"PIECEWISE_BEZIER_CURVE\""), json);
    }

    @Test
    void shouldExportStandaloneCurveMetadataForWrapperAndReplicaChains() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('SHIFT',(2.0,0.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=DIRECTION('DY',(0.0,1.0,0.0));
                #6=DIRECTION('DZ',(0.0,0.0,1.0));
                #7=VECTOR('VX',#4,1.0);
                #8=LINE('L0',#1,#7);
                #9=TRIMMED_CURVE('TC0',#8,(#1),(#2),.T.,.CARTESIAN.);
                #10=ORIENTED_CURVE('OC0',#9,.F.);
                #11=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#4,#5,#3,1.0,#6);
                #12=CURVE_REPLICA('CR0',#10,#11);
                #13=GEOMETRIC_CURVE_SET('GCS0',(#9,#10,#12));
                ENDSEC;
                """);

        assertTrue(json.contains("\"edgeCount\":3"), json);
        assertTrue(json.contains("\"curve\":{\"stepId\":9,\"type\":\"TRIMMED_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":8,\"senseAgreement\":true"), json);
        assertTrue(json.contains("\"curve\":{\"stepId\":10,\"type\":\"ORIENTED_CURVE\",\"basisType\":\"TRIMMED_CURVE\",\"basisStepId\":9,\"orientation\":false"), json);
        assertTrue(json.contains("\"curve\":{\"stepId\":12,\"type\":\"CURVE_REPLICA\",\"basisType\":\"ORIENTED_CURVE\",\"basisStepId\":10,\"transformScale\":1.0"), json);
    }

    @Test
    void shouldExportTopologicalEdgeCurveMetadataForSupportedNonArcCurves() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(2.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(3.0,1.0,0.0));
                #5=VERTEX_POINT('V0',#1);
                #6=VERTEX_POINT('V1',#4);
                #10=(BEZIER_CURVE() B_SPLINE_CURVE(3,(#1,#2,#3,#4),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BZ0'));
                #11=EDGE_CURVE('E0',#5,#6,#10,.T.);
                #12=CONNECTED_EDGE_SET('CES',(#11));
                ENDSEC;
                """);

        assertTrue(json.contains("\"edgeCount\":1"), json);
        assertTrue(json.contains("\"id\":11"), json);
        assertTrue(json.contains("\"curve\":{\"stepId\":10,\"type\":\"BEZIER_CURVE\",\"startAngle\":0.0,\"sweepAngle\":0.0}"), json);
    }

    @Test
    void shouldExportTopologicalEdgeCurveMetadataForWrapperChains() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=VECTOR('VX',#3,1.0);
                #5=LINE('L0',#1,#4);
                #6=TRIMMED_CURVE('TC0',#5,(#1),(#2),.T.,.CARTESIAN.);
                #7=ORIENTED_CURVE('OC0',#6,.F.);
                #8=VERTEX_POINT('V0',#1);
                #9=VERTEX_POINT('V1',#2);
                #10=EDGE_CURVE('E0',#8,#9,#7,.T.);
                #11=CONNECTED_EDGE_SET('CES',(#10));
                ENDSEC;
                """);

        assertTrue(json.contains("\"edgeCount\":1"), json);
        assertTrue(json.contains("\"curve\":{\"stepId\":7,\"type\":\"ORIENTED_CURVE\",\"basisType\":\"TRIMMED_CURVE\",\"basisStepId\":6,\"orientation\":false,\"startAngle\":0.0,\"sweepAngle\":0.0}"), json);
    }

    @Test
    void shouldReportUnsupportedBooleanResultsInPreviewMetadata() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=VERTEX_POINT('V0',#1);
                #6=EDGE_CURVE('E0',#5,#5,#4,.T.);
                #7=ORIENTED_EDGE('OE0',$,$,#6,.T.);
                #8=EDGE_LOOP('LOOP',(#7));
                #9=PLANE('PL',#10);
                #10=AXIS2_PLACEMENT_3D('AX',#1,#11,#12);
                #11=DIRECTION('DZ',(0.0,0.0,1.0));
                #12=DIRECTION('DX2',(1.0,0.0,0.0));
                #13=FACE_SURFACE('F',(#14),#9,.T.);
                #14=FACE_BOUND('B',#8,.T.);
                #15=CLOSED_SHELL('CS0',(#13));
                #16=CLOSED_SHELL('CS1',(#13));
                #17=FACETED_BREP('FB0',#15);
                #18=FACETED_BREP('FB1',#16);
                #19=(BOOLEAN_RESULT(.UNION.,#17,#18) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BOOL0'));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"unsupportedBooleanCount\":1",
                "\"unsupportedBooleans\":[{\"id\":19",
                "\"type\":\"BOOLEAN_RESULT\"",
                "\"reason\":\"BOOLEAN_RESULT operator UNION is unsupported\"",
                "\"id\":19",
                "\"surfaceType\":\"BOOLEAN_RESULT\"");
    }

    @Test
    void shouldExportBrepWithVoidsPreviewMetadata() {
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
                #90=CLOSED_SHELL('OUTER',(#80));
                #91=CLOSED_SHELL('VOID0',(#80));
                #100=BREP_WITH_VOIDS('BWV',#90,(#91));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"solidCount\":1",
                "\"shellCount\":2",
                "\"faceCount\":2",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportExtrudedAreaSolidPreviewMetadata() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=CARTESIAN_POINT('P2',(0.0,0.0));
                #6=DIRECTION('DX2',(1.0,0.0));
                #7=AXIS2_PLACEMENT_2D('AX2',#5,#6);
                #8=RECTANGLE_PROFILE_DEF(.AREA.,'R',#7,4.0,2.0);
                #9=EXTRUDED_AREA_SOLID('EX',#8,#4,#2,5.0);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"solidCount\":1",
                "\"faceCount\":6",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportExtendedSweptProfilePreviewMetadata() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=CARTESIAN_POINT('P2',(0.0,0.0));
                #6=DIRECTION('DX2',(1.0,0.0));
                #7=AXIS2_PLACEMENT_2D('AX2',#5,#6);
                #8=CIRCLE_PROFILE_DEF(.AREA.,'C',#7,2.0);
                #9=ELLIPSE_PROFILE_DEF(.AREA.,'E',#7,3.0,1.5);
                #10=ROUNDED_RECTANGLE_PROFILE_DEF(.AREA.,'RR',#7,6.0,4.0,0.5);
                #11=CIRCULAR_HOLLOW_PROFILE_DEF(.AREA.,'CH',#7,3.0,0.5);
                #12=EXTRUDED_AREA_SOLID('EX0',#8,#4,#2,5.0);
                #13=EXTRUDED_AREA_SOLID('EX1',#9,#4,#2,5.0);
                #14=EXTRUDED_AREA_SOLID('EX2',#10,#4,#2,5.0);
                #15=EXTRUDED_AREA_SOLID('EX3',#11,#4,#2,5.0);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"solidCount\":4",
                "\"faceCount\":345",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportRevolvedAreaSolidPreviewMetadata() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=DIRECTION('DY',(0.0,1.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX3',#1,#4,#3);
                #6=AXIS1_PLACEMENT('AX1',#1,#2);
                #7=CARTESIAN_POINT('P2',(3.0,0.0));
                #8=DIRECTION('DX2',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('AX2',#7,#8);
                #10=RECTANGLE_PROFILE_DEF(.AREA.,'R',#9,2.0,4.0);
                #11=REVOLVED_AREA_SOLID('RV',#10,#5,#6,1.57079632679);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"solidCount\":1",
                "\"faceCount\":34",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportRevolvedAreaSolidPreviewMetadataForHollowProfile() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=DIRECTION('DY',(0.0,1.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX3',#1,#4,#3);
                #6=AXIS1_PLACEMENT('AX1',#1,#2);
                #7=CARTESIAN_POINT('P2',(4.0,0.0));
                #8=DIRECTION('DX2',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('AX2',#7,#8);
                #10=CIRCULAR_HOLLOW_PROFILE_DEF(.AREA.,'CH',#9,2.0,0.5);
                #11=REVOLVED_AREA_SOLID('RVH',#10,#5,#6,0.19634954084936207);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"solidCount\":1",
                "\"faceCount\":146",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportExtrudedAreaSolidPreviewMetadataForArbitraryProfileWithVoids() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=CARTESIAN_POINT('P0',(0.0,0.0));
                #6=CARTESIAN_POINT('P1',(4.0,0.0));
                #7=CARTESIAN_POINT('P2',(4.0,4.0));
                #8=CARTESIAN_POINT('P3',(0.0,4.0));
                #9=CARTESIAN_POINT('P4',(1.0,1.0));
                #10=CARTESIAN_POINT('P5',(3.0,1.0));
                #11=CARTESIAN_POINT('P6',(3.0,3.0));
                #12=CARTESIAN_POINT('P7',(1.0,3.0));
                #13=POLYLINE('OUTER',(#5,#6,#7,#8,#5));
                #14=POLYLINE('INNER',(#9,#10,#11,#12,#9));
                #15=ARBITRARY_PROFILE_DEF_WITH_VOIDS(.AREA.,'APV',#13,(#14));
                #16=EXTRUDED_AREA_SOLID('EXV',#15,#4,#2,5.0);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"solidCount\":1",
                "\"faceCount\":10",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportRevolvedAreaSolidPreviewMetadataForArbitraryProfileWithVoids() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=DIRECTION('DY',(0.0,1.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX3',#1,#4,#3);
                #6=AXIS1_PLACEMENT('AX1',#1,#2);
                #7=CARTESIAN_POINT('P0',(2.0,-2.0));
                #8=CARTESIAN_POINT('P1',(6.0,-2.0));
                #9=CARTESIAN_POINT('P2',(6.0,2.0));
                #10=CARTESIAN_POINT('P3',(2.0,2.0));
                #11=CARTESIAN_POINT('P4',(3.0,-1.0));
                #12=CARTESIAN_POINT('P5',(5.0,-1.0));
                #13=CARTESIAN_POINT('P6',(5.0,1.0));
                #14=CARTESIAN_POINT('P7',(3.0,1.0));
                #15=POLYLINE('OUTER',(#7,#8,#9,#10,#7));
                #16=POLYLINE('INNER',(#11,#12,#13,#14,#11));
                #17=ARBITRARY_PROFILE_DEF_WITH_VOIDS(.AREA.,'APV',#15,(#16));
                #18=REVOLVED_AREA_SOLID('RVV',#17,#5,#6,1.57079632679);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"solidCount\":1",
                "\"faceCount\":66",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportSolidReplicaPreviewMetadata() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=CARTESIAN_POINT('P2',(0.0,0.0));
                #6=DIRECTION('DX2',(1.0,0.0));
                #7=AXIS2_PLACEMENT_2D('AX2',#5,#6);
                #8=RECTANGLE_PROFILE_DEF(.AREA.,'R',#7,4.0,2.0);
                #9=EXTRUDED_AREA_SOLID('EX',#8,#4,#2,5.0);
                #10=CARTESIAN_POINT('T',(10.0,0.0,0.0));
                #11=DIRECTION('DY',(0.0,1.0,0.0));
                #12=CARTESIAN_TRANSFORMATION_OPERATOR_3D('X',#3,#11,#10,1.0,#2);
                #13=SOLID_REPLICA('SR',#9,#12);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"solidCount\":2",
                "\"faceCount\":12",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportCsgSolidPreviewMetadata() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=BLOCK('BLK',#4,10.0,20.0,30.0);
                #6=CARTESIAN_POINT('PZ',(0.0,0.0,15.0));
                #7=AXIS2_PLACEMENT_3D('PLAX',#6,#2,#3);
                #8=PLANE('PLANE',#7);
                #9=HALF_SPACE_SOLID('HS',#8,.T.);
                #10=(BOOLEAN_RESULT(.DIFFERENCE.,#5,#9) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BOOL0'));
                #11=CSG_SOLID('CSG0',#10);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"solidCount\":3",
                "\"faceCount\":18",
                "\"unsupportedBooleanCount\":0",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportAdditionalCsgPrimitivePreviewMetadata() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=SPHERE('SP',#4,2.0);
                #6=ELLIPSOID('EL',#4,3.0,2.0,1.0);
                #7=AXIS1_PLACEMENT('AX1',#1,#2);
                #8=RIGHT_CIRCULAR_CYLINDER('CY',#7,5.0,2.0);
                #9=TORUS('TO',#7,5.0,1.0);
                #10=RIGHT_ANGULAR_WEDGE('WG',#4,4.0,3.0,2.0,2.5);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"solidCount\":5",
                "\"faceCount\":2137",
                "\"unsupportedBooleanCount\":0",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportBooleanClippingResultPreviewMetadata() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=BLOCK('BLK',#4,10.0,20.0,30.0);
                #6=CARTESIAN_POINT('PZ',(0.0,0.0,15.0));
                #7=AXIS2_PLACEMENT_3D('PLAX',#6,#2,#3);
                #8=PLANE('PLANE',#7);
                #9=HALF_SPACE_SOLID('HS',#8,.T.);
                #10=(BOOLEAN_CLIPPING_RESULT(.DIFFERENCE.,#5,#9) BOOLEAN_RESULT(.DIFFERENCE.,#5,#9) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BCR0'));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"solidCount\":2",
                "\"faceCount\":12",
                "\"unsupportedBooleanCount\":0",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportBooleanDifferenceAgainstBoxedHalfSpacePreviewMetadata() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=BLOCK('BLK',#4,10.0,20.0,30.0);
                #6=CARTESIAN_POINT('PZ',(0.0,0.0,15.0));
                #7=AXIS2_PLACEMENT_3D('PLAX',#6,#2,#3);
                #8=PLANE('PLANE',#7);
                #9=BOX_DOMAIN(#1,5.0,20.0,30.0);
                #10=BOXED_HALF_SPACE('BHS',#8,.T.,#9);
                #11=(BOOLEAN_RESULT(.DIFFERENCE.,#5,#10) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BOOL0'));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"solidCount\":2",
                "\"faceCount\":12",
                "\"unsupportedBooleanCount\":0",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportBooleanIntersectionAgainstBoxedHalfSpacePreviewMetadata() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=BLOCK('BLK',#4,10.0,20.0,30.0);
                #6=CARTESIAN_POINT('PZ',(0.0,0.0,15.0));
                #7=AXIS2_PLACEMENT_3D('PLAX',#6,#2,#3);
                #8=PLANE('PLANE',#7);
                #9=BOX_DOMAIN(#1,5.0,20.0,30.0);
                #10=BOXED_HALF_SPACE('BHS',#8,.T.,#9);
                #11=(BOOLEAN_RESULT(.INTERSECTION.,#5,#10) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BOOL0'));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"solidCount\":2",
                "\"faceCount\":12",
                "\"unsupportedBooleanCount\":0",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportBooleanClippingResultAgainstBoxedHalfSpacePreviewMetadata() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=BLOCK('BLK',#4,10.0,20.0,30.0);
                #6=CARTESIAN_POINT('PZ',(0.0,0.0,15.0));
                #7=AXIS2_PLACEMENT_3D('PLAX',#6,#2,#3);
                #8=PLANE('PLANE',#7);
                #9=BOX_DOMAIN(#1,5.0,20.0,30.0);
                #10=BOXED_HALF_SPACE('BHS',#8,.T.,#9);
                #11=(BOOLEAN_CLIPPING_RESULT(.DIFFERENCE.,#5,#10) BOOLEAN_RESULT(.DIFFERENCE.,#5,#10) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BCR0'));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"solidCount\":2",
                "\"faceCount\":12",
                "\"unsupportedBooleanCount\":0",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldRenderSingleCylindricalFaceBoundAsOuterLoop() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #4=CARTESIAN_POINT('B0',(0.7648421872844885,0.644217687237691,0.0));
                #5=CARTESIAN_POINT('A1',(1.0,0.0,1.0));
                #6=CARTESIAN_POINT('B1',(0.7648421872844885,0.644217687237691,1.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX0',#1,#10,#11);
                #13=AXIS2_PLACEMENT_3D('AX1',#2,#10,#11);
                #14=CYLINDRICAL_SURFACE('CY0',#12,1.0);
                #20=VERTEX_POINT('V0',#3);
                #21=VERTEX_POINT('V1',#4);
                #22=VERTEX_POINT('V2',#6);
                #23=VERTEX_POINT('V3',#5);
                #30=CIRCLE('C0',#12,1.0);
                #31=CIRCLE('C1',#13,1.0);
                #32=DIRECTION('DU',(1.0,0.0));
                #33=DIRECTION('DV',(0.0,1.0));
                #34=VECTOR('VU',#32,0.7);
                #35=VECTOR('VV',#33,1.0);
                #36=DIRECTION('UP3',(0.0,0.0,1.0));
                #37=VECTOR('VUP3',#36,1.0);
                #40=CARTESIAN_POINT('UV00',(0.0,0.0));
                #41=LINE('UVL0',#40,#34);
                #42=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #43=DEFINITIONAL_REPRESENTATION('DEF0',(#41),#42);
                #44=PCURVE('PC0',#14,#43);
                #45=SURFACE_CURVE('SC0',#30,(#44),.PCURVE_S1.);
                #46=CARTESIAN_POINT('UV10',(0.0,1.0));
                #47=LINE('UVL1',#46,#34);
                #48=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #49=DEFINITIONAL_REPRESENTATION('DEF1',(#47),#48);
                #50=PCURVE('PC1',#14,#49);
                #51=SURFACE_CURVE('SC1',#31,(#50),.PCURVE_S1.);
                #52=CARTESIAN_POINT('UV20',(0.9,1.0));
                #53=LINE('UVL2',#52,#35);
                #54=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #55=DEFINITIONAL_REPRESENTATION('DEF2',(#53),#54);
                #56=PCURVE('PC2',#14,#55);
                #57=LINE('L0',#4,#37);
                #58=SURFACE_CURVE('SC2',#57,(#56),.PCURVE_S1.);
                #59=CARTESIAN_POINT('UV30',(0.0,0.0));
                #60=LINE('UVL3',#59,#35);
                #61=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #62=DEFINITIONAL_REPRESENTATION('DEF3',(#60),#61);
                #63=PCURVE('PC3',#14,#62);
                #64=LINE('L1',#3,#37);
                #65=SURFACE_CURVE('SC3',#64,(#63),.PCURVE_S1.);
                #66=EDGE_CURVE('E0',#20,#21,#45,.T.);
                #67=EDGE_CURVE('E1',#21,#22,#58,.T.);
                #68=EDGE_CURVE('E2',#22,#23,#51,.F.);
                #69=EDGE_CURVE('E3',#23,#20,#65,.F.);
                #70=ORIENTED_EDGE('OE0',$,$,#66,.T.);
                #71=ORIENTED_EDGE('OE1',$,$,#67,.T.);
                #72=ORIENTED_EDGE('OE2',$,$,#68,.T.);
                #73=ORIENTED_EDGE('OE3',$,$,#69,.T.);
                #80=EDGE_LOOP('L0',(#70,#71,#72,#73));
                #81=FACE_BOUND('B0',#80,.F.);
                #82=ADVANCED_FACE('F0',(#81),#14,.T.);
                #83=OPEN_SHELL('OS',(#82));
                ENDSEC;
                """);

        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":4"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"surfaceType\":\"CYLINDRICAL_SURFACE\""));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldSkipCylindricalFaceWithVertexLoopHole() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=CYLINDRICAL_SURFACE('CY0',#4,1.0);
                #10=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(1.0,0.0,1.0));
                #12=CARTESIAN_POINT('P2',(0.6967067093471654,0.7173560908995228,1.0));
                #13=CARTESIAN_POINT('P3',(0.6967067093471654,0.7173560908995228,0.0));
                #14=CARTESIAN_POINT('Q0',(0.9393727128473789,0.34289780745545134,0.5));
                #20=VERTEX_POINT('V0',#10);
                #21=VERTEX_POINT('V1',#11);
                #22=VERTEX_POINT('V2',#12);
                #23=VERTEX_POINT('V3',#13);
                #24=VERTEX_POINT('V4',#14);
                #30=DIRECTION('DV0',(0.0,0.0,1.0));
                #31=VECTOR('VV0',#30,1.0);
                #32=LINE('L0',#10,#31);
                #33=DIRECTION('DH0',(-0.3032932906528346,0.7173560908995228,0.0));
                #34=VECTOR('VU0',#33,1.0);
                #35=LINE('L1',#11,#34);
                #36=DIRECTION('DV1',(0.0,0.0,-1.0));
                #37=VECTOR('VV1',#36,1.0);
                #38=LINE('L2',#12,#37);
                #39=DIRECTION('DH1',(0.3032932906528346,-0.7173560908995228,0.0));
                #40=VECTOR('VU1',#39,1.0);
                #41=LINE('L3',#13,#40);
                #60=CARTESIAN_POINT('E0C',(0.0,0.5));
                #61=DIRECTION('E0X',(1.0,0.0));
                #62=AXIS2_PLACEMENT_2D('E0A',#60,#61);
                #63=ELLIPSE('E0',#62,0.3,0.5);
                #64=CARTESIAN_POINT('E0T0',(0.0,0.0));
                #65=CARTESIAN_POINT('E0T1',(0.0,1.0));
                #66=TRIMMED_CURVE('E0TC',#63,(#64),(#65),.T.,.CARTESIAN.);
                #67=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #68=DEFINITIONAL_REPRESENTATION('DEF0',(#66),#67);
                #69=PCURVE('PC0',#5,#68);
                #70=SURFACE_CURVE('SC0',#32,(#69),.PCURVE_S1.);
                #71=CARTESIAN_POINT('UV10',(0.0,1.0));
                #72=DIRECTION('U1',(1.0,0.0));
                #73=VECTOR('VU4',#72,0.8);
                #74=LINE('UVL1',#71,#73);
                #75=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #76=DEFINITIONAL_REPRESENTATION('DEF1',(#74),#75);
                #77=PCURVE('PC1',#5,#76);
                #78=SURFACE_CURVE('SC1',#35,(#77),.PCURVE_S1.);
                #79=CARTESIAN_POINT('C1C',(0.8,0.5));
                #80=DIRECTION('C1X',(1.0,0.0));
                #81=AXIS2_PLACEMENT_2D('C1A',#79,#80);
                #82=CIRCLE('C1',#81,0.5);
                #83=CARTESIAN_POINT('C1T0',(0.8,1.0));
                #84=CARTESIAN_POINT('C1T1',(0.8,0.0));
                #85=TRIMMED_CURVE('C1TC',#82,(#83),(#84),.T.,.CARTESIAN.);
                #86=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #87=DEFINITIONAL_REPRESENTATION('DEF2',(#85),#86);
                #88=PCURVE('PC2',#5,#87);
                #89=SURFACE_CURVE('SC2',#38,(#88),.PCURVE_S1.);
                #90=CARTESIAN_POINT('UV30',(0.8,0.0));
                #91=DIRECTION('U0',(-1.0,0.0));
                #92=VECTOR('VU5',#91,0.8);
                #93=LINE('UVL3',#90,#92);
                #94=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #95=DEFINITIONAL_REPRESENTATION('DEF3',(#93),#94);
                #96=PCURVE('PC3',#5,#95);
                #97=SURFACE_CURVE('SC3',#41,(#96),.PCURVE_S1.);
                #140=EDGE_CURVE('E0',#20,#21,#70,.T.);
                #141=EDGE_CURVE('E1',#21,#22,#78,.T.);
                #142=EDGE_CURVE('E2',#22,#23,#89,.T.);
                #143=EDGE_CURVE('E3',#23,#20,#97,.T.);
                #150=ORIENTED_EDGE('OE0',$,$,#140,.T.);
                #151=ORIENTED_EDGE('OE1',$,$,#141,.T.);
                #152=ORIENTED_EDGE('OE2',$,$,#142,.T.);
                #153=ORIENTED_EDGE('OE3',$,$,#143,.T.);
                #160=EDGE_LOOP('LOUT',(#150,#151,#152,#153));
                #161=FACE_OUTER_BOUND('B0',#160,.T.);
                #162=VERTEX_LOOP('LIN',#24);
                #163=FACE_BOUND('B1',#162,.F.);
                #164=ADVANCED_FACE('F0',(#161,#163),#5,.T.);
                #165=OPEN_SHELL('OS',(#164));
                ENDSEC;
                """);

        assertTrue(json.contains("\"faceCount\":0"));
        assertTrue(json.contains("\"unsupportedFaceCount\":1"));
    }

    @Test
    void shouldExportCylindricalBandPreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/cylindrical-band.step")));

        assertTrue(json.contains("\"surfaceType\":\"CYLINDRICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":4"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[[2.0,0.0,0.0]"));
    }

    @Test
    void shouldExportCylindricalFaceExamplePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/cylindrical-face.step")));

        assertTrue(json.contains("\"surfaceType\":\"CYLINDRICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":3"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
        assertTrue(countOccurrences(json, "],[") > 1200);
    }

    @Test
    void shouldExportFaceSurfaceAndOrientedFacePreview() {
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
                #80=FACE_SURFACE('FS0',(#71),#13,.T.);
                #81=ORIENTED_FACE('OF0',#80,.F.);
                #90=OPEN_SHELL('OS',(#81));
                ENDSEC;
                """);

        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":4"));
        assertTrue(json.contains("\"surfaceType\":\"PLANE\""));
        assertTrue(json.contains("\"sameSense\":false"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
    }

    @Test
    void shouldExportConicalBandPreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/conical-band.step")));

        assertTrue(json.contains("\"surfaceType\":\"CONICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":4"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[[2.0,0.0,0.0]"));
    }

    @Test
    void shouldExportConicalHolePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/conical-hole.step")));

        assertTrue(json.contains("\"surfaceType\":\"CONICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":8"));
        assertTrue(json.contains("\"outer\":false"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldExportVertexLoopFacePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #10=VERTEX_POINT('V0',#1);
                #11=VERTEX_LOOP('VL0',#10);
                #12=FACE_OUTER_BOUND('B0',#11,.T.);
                #13=FACE_SURFACE('FS0',(#12),#5,.T.);
                #14=OPEN_SHELL('OS',(#13));
                ENDSEC;
                """);

        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":0"));
        assertTrue(json.contains("\"points\":[[0.0,0.0,0.0]]"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
    }

    @Test
    void shouldExportPolyLoopFaceInPreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#5,#6);
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=PLANE('PL0',#4);
                #8=POLY_LOOP('PL0',(#1,#2,#3));
                #9=FACE_OUTER_BOUND('B0',#8,.T.);
                #10=FACE_SURFACE('FS0',(#9),#7,.T.);
                #11=OPEN_SHELL('OS',(#10));
                ENDSEC;
                """);

        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"surfaceType\":\"PLANE\""));
    }

    @Test
    void shouldExportRationalBsplineSurfacePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P10',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P01',(0.0,1.0,0.0));
                #4=CARTESIAN_POINT('P11',(1.0,1.0,1.0));
                #10=VERTEX_POINT('V0',#1);
                #11=VERTEX_POINT('V1',#2);
                #12=VERTEX_POINT('V2',#4);
                #13=VERTEX_POINT('V3',#3);
                #20=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.)
                     RATIONAL_B_SPLINE_SURFACE(((1.0,1.0),(1.0,1.0))));
                #30=LINE('E0',#1,#40);
                #31=LINE('E1',#2,#41);
                #32=LINE('E2',#4,#42);
                #33=LINE('E3',#3,#43);
                #40=VECTOR('VX',#50,1.0);
                #41=VECTOR('VY',#51,1.0);
                #42=VECTOR('VMX',#52,1.0);
                #43=VECTOR('VMY',#53,1.0);
                #50=DIRECTION('DX',(1.0,0.0,0.0));
                #51=DIRECTION('DY',(0.0,1.0,0.0));
                #52=DIRECTION('MX',(-1.0,0.0,0.0));
                #53=DIRECTION('MY',(0.0,-1.0,0.0));
                #60=EDGE_CURVE('EC0',#10,#11,#30,.T.);
                #61=EDGE_CURVE('EC1',#11,#12,#31,.T.);
                #62=EDGE_CURVE('EC2',#12,#13,#32,.T.);
                #63=EDGE_CURVE('EC3',#13,#10,#33,.T.);
                #70=ORIENTED_EDGE('OE0',$,$,#60,.T.);
                #71=ORIENTED_EDGE('OE1',$,$,#61,.T.);
                #72=ORIENTED_EDGE('OE2',$,$,#62,.T.);
                #73=ORIENTED_EDGE('OE3',$,$,#63,.T.);
                #80=EDGE_LOOP('L0',(#70,#71,#72,#73));
                #81=FACE_OUTER_BOUND('B0',#80,.T.);
                #82=ADVANCED_FACE('F0',(#81),#20,.T.);
                #83=OPEN_SHELL('OS',(#82));
                ENDSEC;
                """);

        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"surfaceType\":\"RATIONAL_B_SPLINE_SURFACE\""));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
    }

    @Test
    void shouldExportOrientedBsplineSurfacePreview() {
        String json = StepPreviewJsonExporter.export(bsplinePatchFaceStep(
                false,
                "#20=ORIENTED_SURFACE('OS0',#10,.T.);",
                "#20",
                "0.0,0.0,0.0",
                "1.0,0.0,0.0",
                "1.0,1.0,1.0",
                "0.0,1.0,0.0"
        ));

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "ORIENTED_SURFACE");
    }

    @Test
    void shouldExportRectangularTrimmedBsplineSurfacePreview() {
        String json = StepPreviewJsonExporter.export(bsplinePatchFaceStep(
                false,
                "#20=RECTANGULAR_TRIMMED_SURFACE('RTS0',#10,0.0,1.0,0.0,1.0,.T.,.T.);",
                "#20",
                "0.0,0.0,0.0",
                "1.0,0.0,0.0",
                "1.0,1.0,1.0",
                "0.0,1.0,0.0"
        ));

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "RECTANGULAR_TRIMMED_SURFACE");
    }

    @Test
    void shouldExportCurveBoundedBsplineSurfacePreview() {
        String json = StepPreviewJsonExporter.export(bsplinePatchFaceStep(
                false,
                "#20=CURVE_BOUNDED_SURFACE('CBS0',#10,(#30),.T.);",
                "#20",
                "0.0,0.0,0.0",
                "1.0,0.0,0.0",
                "1.0,1.0,1.0",
                "0.0,1.0,0.0"
        ));

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "CURVE_BOUNDED_SURFACE");
    }

    @Test
    void shouldExportOffsetBsplineSurfacePreview() {
        String json = StepPreviewJsonExporter.export(bsplinePatchFaceStep(
                false,
                "#20=OFFSET_SURFACE('OFS0',#10,1.0,.F.);",
                "#20",
                "0.0,0.0,-1.0",
                "1.0,0.0,-1.0",
                "1.0,1.7071067811865475,0.29289321881345254",
                "0.0,1.7071067811865475,-0.7071067811865475"
        ));

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "OFFSET_SURFACE");
    }

    @Test
    void shouldExportReplicaBsplineSurfacePreview() {
        String json = StepPreviewJsonExporter.export(bsplinePatchFaceStep(
                false,
                """
                #100=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #101=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#100,1.0,$);
                #102=SURFACE_REPLICA('SR0',#10,#101);
                """,
                "#102",
                "0.0,0.0,2.0",
                "1.0,0.0,2.0",
                "1.0,1.0,3.0",
                "0.0,1.0,2.0"
        ));

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "SURFACE_REPLICA");
    }

    @Test
    void shouldExportUniformScaledReplicaBsplineSurfacePreview() {
        String json = StepPreviewJsonExporter.export(bsplinePatchFaceStep(
                false,
                """
                #100=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #101=DIRECTION('SX',(2.0,0.0,0.0));
                #102=DIRECTION('SZ',(0.0,0.0,2.0));
                #103=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',#101,#102,#100,2.0,$);
                #104=SURFACE_REPLICA('SR0',#10,#103);
                """,
                "#104",
                "0.0,0.0,2.0",
                "2.0,0.0,2.0",
                "2.0,2.0,4.0",
                "0.0,2.0,2.0"
        ));

        assertTrue(json.contains("\"surfaceType\":\"SURFACE_REPLICA\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldReportZeroScaleReplicaBsplineSurfaceAsUnsupportedPreview() {
        String json = StepPreviewJsonExporter.export(bsplinePatchFaceStep(
                false,
                """
                #100=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #101=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#100,0.0,$);
                #102=SURFACE_REPLICA('SR0',#10,#101);
                """,
                "#102",
                "0.0,0.0,2.0",
                "0.0,0.0,2.0",
                "0.0,0.0,2.0",
                "0.0,0.0,2.0"
        ));

        assertTrue(json.contains("\"surfaceType\":\"SURFACE_REPLICA\""), json);
        assertTrue(json.contains("\"faceCount\":0"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":1"), json);
        assertTrue(json.contains("\"reason\":\"SURFACE_REPLICA zero scale preview is unsupported\""), json);
    }

    @Test
    void shouldReportNonUniformScaleReplicaBsplineSurfaceAsUnsupportedPreview() {
        String json = StepPreviewJsonExporter.export(bsplinePatchFaceStep(
                false,
                """
                #100=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #101=DIRECTION('SX',(1.0,0.0,0.0));
                #102=DIRECTION('SY',(1.0,1.0,0.0));
                #103=DIRECTION('SZ',(0.0,0.0,1.0));
                #104=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',#101,#102,#100,1.0,#103);
                #105=SURFACE_REPLICA('SR0',#10,#104);
                """,
                "#105",
                "0.0,0.0,2.0",
                "2.0,0.0,2.0",
                "2.0,1.0,3.0",
                "0.0,1.0,2.0"
        ));

        assertTrue(json.contains("\"surfaceType\":\"SURFACE_REPLICA\""), json);
        assertTrue(json.contains("\"faceCount\":0"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":1"), json);
        assertTrue(json.contains("\"reason\":\"SURFACE_REPLICA non-uniform scale preview is unsupported\""), json);
    }

    @Test
    void shouldExportOrientedRationalBsplineSurfacePreview() {
        String json = StepPreviewJsonExporter.export(bsplinePatchFaceStep(
                true,
                "#20=ORIENTED_SURFACE('OS0',#10,.T.);",
                "#20",
                "0.0,0.0,0.0",
                "1.0,0.0,0.0",
                "1.0,1.0,1.0",
                "0.0,1.0,0.0"
        ));

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "ORIENTED_SURFACE");
    }

    @Test
    void shouldExportRectangularTrimmedRationalBsplineSurfacePreview() {
        String json = StepPreviewJsonExporter.export(bsplinePatchFaceStep(
                true,
                "#20=RECTANGULAR_TRIMMED_SURFACE('RTS0',#10,0.0,1.0,0.0,1.0,.T.,.T.);",
                "#20",
                "0.0,0.0,0.0",
                "1.0,0.0,0.0",
                "1.0,1.0,1.0",
                "0.0,1.0,0.0"
        ));

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "RECTANGULAR_TRIMMED_SURFACE");
    }

    @Test
    void shouldExportCurveBoundedRationalBsplineSurfacePreview() {
        String json = StepPreviewJsonExporter.export(bsplinePatchFaceStep(
                true,
                "#20=CURVE_BOUNDED_SURFACE('CBS0',#10,(#30),.T.);",
                "#20",
                "0.0,0.0,0.0",
                "1.0,0.0,0.0",
                "1.0,1.0,1.0",
                "0.0,1.0,0.0"
        ));

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "CURVE_BOUNDED_SURFACE");
    }

    @Test
    void shouldExportOffsetRationalBsplineSurfacePreview() {
        String json = StepPreviewJsonExporter.export(bsplinePatchFaceStep(
                true,
                "#20=OFFSET_SURFACE('OFS0',#10,1.0,.F.);",
                "#20",
                "0.0,0.0,-1.0",
                "1.0,0.0,-1.0",
                "1.0,1.7071067811865475,0.29289321881345254",
                "0.0,1.7071067811865475,-0.7071067811865475"
        ));

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "OFFSET_SURFACE");
    }

    @Test
    void shouldExportReplicaRationalBsplineSurfacePreview() {
        String json = StepPreviewJsonExporter.export(bsplinePatchFaceStep(
                true,
                """
                #100=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #101=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#100,1.0,$);
                #102=SURFACE_REPLICA('SR0',#10,#101);
                """,
                "#102",
                "0.0,0.0,2.0",
                "1.0,0.0,2.0",
                "1.0,1.0,3.0",
                "0.0,1.0,2.0"
        ));

        assertSingleSupportedFacePreviewWithoutEdgeCount(json, "SURFACE_REPLICA");
    }

    @Test
    void shouldExportUniformScaledReplicaRationalBsplineSurfacePreview() {
        String json = StepPreviewJsonExporter.export(bsplinePatchFaceStep(
                true,
                """
                #100=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #101=DIRECTION('SX',(2.0,0.0,0.0));
                #102=DIRECTION('SZ',(0.0,0.0,2.0));
                #103=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',#101,#102,#100,2.0,$);
                #104=SURFACE_REPLICA('SR0',#10,#103);
                """,
                "#104",
                "0.0,0.0,2.0",
                "2.0,0.0,2.0",
                "2.0,2.0,4.0",
                "0.0,2.0,2.0"
        ));

        assertTrue(json.contains("\"surfaceType\":\"SURFACE_REPLICA\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldReportZeroScaleReplicaRationalBsplineSurfaceAsUnsupportedPreview() {
        String json = StepPreviewJsonExporter.export(bsplinePatchFaceStep(
                true,
                """
                #100=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #101=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#100,0.0,$);
                #102=SURFACE_REPLICA('SR0',#10,#101);
                """,
                "#102",
                "0.0,0.0,2.0",
                "0.0,0.0,2.0",
                "0.0,0.0,2.0",
                "0.0,0.0,2.0"
        ));

        assertTrue(json.contains("\"surfaceType\":\"SURFACE_REPLICA\""), json);
        assertTrue(json.contains("\"faceCount\":0"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":1"), json);
        assertTrue(json.contains("\"reason\":\"SURFACE_REPLICA zero scale preview is unsupported\""), json);
    }

    @Test
    void shouldReportNonUniformScaleReplicaRationalBsplineSurfaceAsUnsupportedPreview() {
        String json = StepPreviewJsonExporter.export(bsplinePatchFaceStep(
                true,
                """
                #100=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #101=DIRECTION('SX',(1.0,0.0,0.0));
                #102=DIRECTION('SY',(1.0,1.0,0.0));
                #103=DIRECTION('SZ',(0.0,0.0,1.0));
                #104=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',#101,#102,#100,1.0,#103);
                #105=SURFACE_REPLICA('SR0',#10,#104);
                """,
                "#105",
                "0.0,0.0,2.0",
                "2.0,0.0,2.0",
                "2.0,1.0,3.0",
                "0.0,1.0,2.0"
        ));

        assertTrue(json.contains("\"surfaceType\":\"SURFACE_REPLICA\""), json);
        assertTrue(json.contains("\"faceCount\":0"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":1"), json);
        assertTrue(json.contains("\"reason\":\"SURFACE_REPLICA non-uniform scale preview is unsupported\""), json);
    }

    @Test
    void shouldExportTrimmedCurveEdgePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P0',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('P1',(0.0,2.0,0.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=DIRECTION('DX',(1.0,0.0,0.0));
                #6=AXIS2_PLACEMENT_3D('AX',#1,#4,#5);
                #7=PLANE('PL0',#6);
                #8=CIRCLE('C0',#6,2.0);
                #9=TRIMMED_CURVE('TC0',#8,(#2),(#3),.T.,.CARTESIAN.);
                #10=VERTEX_POINT('V0',#2);
                #11=VERTEX_POINT('V1',#3);
                #12=EDGE_CURVE('E0',#10,#11,#9,.T.);
                #13=ORIENTED_EDGE('OE0',$,$,#12,.T.);
                #14=TRIMMED_CURVE('TC1',#8,(#3),(#2),.T.,.CARTESIAN.);
                #15=EDGE_CURVE('E1',#11,#10,#14,.T.);
                #16=ORIENTED_EDGE('OE1',$,$,#15,.T.);
                #17=EDGE_LOOP('L0',(#13,#16));
                #18=FACE_OUTER_BOUND('B0',#17,.T.);
                #19=ADVANCED_FACE('F0',(#18),#7,.T.);
                #20=OPEN_SHELL('OS',(#19));
                ENDSEC;
                """);

        assertTrue(json.contains("\"edgeCount\":2"));
        assertTrue(json.contains("[2.0,0.0,0.0]"));
        assertTrue(json.contains("[0.0,2.0,0.0]"));
    }

    @Test
    void shouldExportStandaloneTrimmedCurveWithoutSamplingFullCircle() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P0',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('P1',(0.0,2.0,0.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=DIRECTION('DX',(1.0,0.0,0.0));
                #6=AXIS2_PLACEMENT_3D('AX',#1,#4,#5);
                #7=CIRCLE('C0',#6,2.0);
                #8=TRIMMED_CURVE('TC0',#7,(#2),(#3),.T.,.CARTESIAN.);
                #9=VERTEX_POINT('V0',#2);
                #10=VERTEX_POINT('V1',#3);
                #11=EDGE_CURVE('E0',#9,#10,#8,.T.);
                #12=ORIENTED_EDGE('OE0',$,$,#11,.T.);
                #13=PATH('P0',(#12));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":1",
                "\"id\":11",
                "[2.0,0.0,0.0]",
                "[0.0,2.0,0.0]");
        assertFalse(json.contains("[-2.0,0.0,0.0]"), json);
        assertFalse(json.contains("[0.0,-2.0,0.0]"), json);
    }

    @Test
    void shouldExportStandaloneTrimmedPcurveWithoutLiftingFullCircle() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O2',(0.0,0.0));
                #2=CARTESIAN_POINT('P0',(2.0,0.0));
                #3=CARTESIAN_POINT('P1',(0.0,2.0));
                #4=DIRECTION('DX2',(1.0,0.0));
                #5=AXIS2_PLACEMENT_2D('AX2',#1,#4);
                #6=CIRCLE('C2',#5,2.0);
                #7=TRIMMED_CURVE('TC2',#6,(#2),(#3),.T.,.CARTESIAN.);
                #8=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID2','PARAM'));
                #9=REPRESENTATION('R2',(#7),#8);
                #10=CARTESIAN_POINT('O3',(0.0,0.0,0.0));
                #11=DIRECTION('DZ',(0.0,0.0,1.0));
                #12=DIRECTION('DX3',(1.0,0.0,0.0));
                #13=AXIS2_PLACEMENT_3D('PL',#10,#11,#12);
                #14=PLANE('PL0',#13);
                #15=PCURVE('PC0',#14,#9);
                #16=GEOMETRIC_SET('GS0',(#15));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":1",
                "\"id\":15",
                "[2.0,0.0,0.0]",
                "\"max\":[2.0,2.0,0.0]");
        assertFalse(json.contains("[-2.0,0.0,0.0]"), json);
        assertFalse(json.contains("[0.0,-2.0,0.0]"), json);
    }

    @Test
    void shouldExportEllipsePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/ellipse-face.step")));

        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":2"));
        assertTrue(json.contains("[3.0,0.0,0.0]"));
        assertTrue(json.contains("[0.0,2.0,0.0]"));
    }

    @Test
    void shouldExportMetadataAndValidationSummary() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/metadata-square.step")));

        assertTrue(json.contains("\"name\":\"FRONT_FACE\""));
        assertTrue(json.contains("\"color\":[204,102,51]"));
        assertTrue(json.contains("\"layers\":[\"Inspection\"]"));
        assertTrue(json.contains("\"validation\":{"));
        assertTrue(json.contains("\"approxSurfaceArea\":2.0"));
        assertTrue(json.contains("\"approxEdgeLength\":6.0"));
        assertTrue(json.contains("\"center\":[1.0,0.5,0.0]"));
    }

    @Test
    void shouldExportPreviewForExamplesTestStep() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/test.step")));

        assertTrue(json.contains("\"solidCount\":1"));
        assertTrue(json.contains("\"faceCount\":15"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"surfaceType\":\"CYLINDRICAL_SURFACE\""));
        assertTrue(containsNoRawControlCharacters(json));
        assertTrue(json.contains("\"instances\":[{\"id\":\"pd-527\""));
        assertTrue(json.contains("\"representationIds\":[525]"));
        assertTrue(json.contains("\"representations\":[{\"id\":525"));
        assertTrue(json.contains("\"representations\":[{\"id\":525,\"name\":\"\",\"layers\":[],\"color\":null,\"edges\":[{\"id\":157"));
        assertTrue(json.contains("\"faces\":[{\"id\":283"));
    }

    @Test
    void shouldExportPmiAndNativeValidationChecks() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/pmi-validation-square.step")));

        assertTrue(json.contains("\"pmi\":["));
        assertTrue(json.contains("\"text\":\"A=2.0 / L=6.0\""));
        assertTrue(json.contains("\"leader\":[[1.0,1.0,0.0],[1.35,1.45,0.0]]"));
        assertTrue(json.contains("\"targetIds\":[80]"));
        assertTrue(json.contains("\"targets\":[{\"id\":80,\"type\":\"face\",\"name\":\"FACE_A\",\"instanceIds\":[]}]"));
        assertTrue(json.contains("\"report\":{\"status\":\"ok\""));
        assertTrue(json.contains("\"okCount\":4"));
        assertTrue(json.contains("\"warnCount\":0"));
        assertTrue(json.contains("\"nativeChecks\":["));
        assertTrue(json.contains("\"name\":\"surface area\""));
        assertTrue(json.contains("\"propertyId\":\"surface_area\""));
        assertTrue(json.contains("\"name\":\"edge length\""));
        assertTrue(json.contains("\"name\":\"center x\""));
        assertTrue(json.contains("\"name\":\"bbox y\""));
        assertTrue(json.contains("\"status\":\"ok\""));
        assertTrue(json.contains("\"matches\":true"));
    }

    @Test
    void shouldExportSemanticPmiTargetsWithAssemblyInstanceIds() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('ASM-001','Assembly','Assembly root',(#2));
                #4=PRODUCT('PRT-001','Component','Component part',(#2));
                #5=PRODUCT_DEFINITION_FORMATION('asm-v1','first',#3);
                #6=PRODUCT_DEFINITION_FORMATION('prt-v1','first',#4);
                #7=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #8=PRODUCT_DEFINITION('asm-def','assembly def',#5,#7);
                #9=PRODUCT_DEFINITION('prt-def','part def',#6,#7);
                #10=PRODUCT_DEFINITION_SHAPE('asm-shape','assembly shape',#8);
                #11=PRODUCT_DEFINITION_SHAPE('part-shape','part shape',#9);
                #12=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #13=CARTESIAN_POINT('T',(8.0,0.0,0.0));
                #14=DIRECTION('DZ',(0.0,0.0,1.0));
                #15=DIRECTION('DX',(1.0,0.0,0.0));
                #16=AXIS2_PLACEMENT_3D('AX0',#12,#14,#15);
                #17=AXIS2_PLACEMENT_3D('AX1',#13,#14,#15);
                #18=ITEM_DEFINED_TRANSFORMATION('move','translate x',#16,#17);
                #19=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #20=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #21=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #22=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #23=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #24=VERTEX_POINT('V0',#20);
                #25=VERTEX_POINT('V1',#21);
                #26=VERTEX_POINT('V2',#22);
                #27=VERTEX_POINT('V3',#23);
                #28=DIRECTION('D1',(1.0,0.0,0.0));
                #29=VECTOR('VE1',#28,1.0);
                #30=LINE('L1',#20,#29);
                #31=DIRECTION('D2',(0.0,1.0,0.0));
                #32=VECTOR('VE2',#31,1.0);
                #33=LINE('L2',#21,#32);
                #34=DIRECTION('D3',(-1.0,0.0,0.0));
                #35=VECTOR('VE3',#34,1.0);
                #36=LINE('L3',#22,#35);
                #37=DIRECTION('D4',(0.0,-1.0,0.0));
                #38=VECTOR('VE4',#37,1.0);
                #39=LINE('L4',#23,#38);
                #40=EDGE_CURVE('E1',#24,#25,#30,.T.);
                #41=EDGE_CURVE('E2',#25,#26,#33,.T.);
                #42=EDGE_CURVE('E3',#26,#27,#36,.T.);
                #43=EDGE_CURVE('E4',#27,#24,#39,.T.);
                #44=ORIENTED_EDGE('OE1',$,$,#40,.T.);
                #45=ORIENTED_EDGE('OE2',$,$,#41,.T.);
                #46=ORIENTED_EDGE('OE3',$,$,#42,.T.);
                #47=ORIENTED_EDGE('OE4',$,$,#43,.T.);
                #48=EDGE_LOOP('LOOP',(#44,#45,#46,#47));
                #49=FACE_OUTER_BOUND('FOB',#48,.T.);
                #50=PLANE('PL',#16);
                #51=ADVANCED_FACE('F0',(#49),#50,.T.);
                #52=CLOSED_SHELL('CS',(#51));
                #53=MANIFOLD_SOLID_BREP('BODY',#52);
                #54=SHAPE_REPRESENTATION('ASM_REP',(),#19);
                #55=SHAPE_REPRESENTATION('PART_REP',(#53),#19);
                #56=SHAPE_DEFINITION_REPRESENTATION(#10,#54);
                #57=SHAPE_DEFINITION_REPRESENTATION(#11,#55);
                #58=(REPRESENTATION_RELATIONSHIP('rr','with transform',#54,#55)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#18));
                #59=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ-1','component 1','mounted',#8,#9);
                #60=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#58,#59);
                #70=CARTESIAN_POINT('TXT',(0.5,1.3,0.0));
                #71=ANNOTATION_TEXT_OCCURRENCE('NOTE','instance note',#70);
                #72=DRAUGHTING_CALLOUT('CALLOUT',(#71));
                #73=GEOMETRIC_ITEM_SPECIFIC_USAGE('callout->face','face semantic link',#72,#51);
                ENDSEC;
                """);

        assertTrue(json.contains("\"targetIds\":[51]"));
        assertTrue(json.contains("\"type\":\"face\""));
        assertTrue(json.contains("\"name\":\"F0\""));
        assertTrue(json.contains("\"instanceIds\":[]"));
    }

    @Test
    void shouldExportValidationWarningsForMismatchedMeasures() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/pmi-validation-mismatch.step")));

        assertTrue(json.contains("\"report\":{\"status\":\"warn\""));
        assertTrue(json.contains("\"okCount\":1"));
        assertTrue(json.contains("\"warnCount\":2"));
        assertTrue(json.contains("\"name\":\"surface area\""));
        assertTrue(json.contains("\"name\":\"edge length\""));
        assertTrue(json.contains("\"status\":\"warn\""));
        assertTrue(json.contains("\"matches\":false"));
    }

    @Test
    void shouldExportBsplineTrimmedSurfaceCurvePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #10=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #12=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #13=(B_SPLINE_CURVE('B0',2,(#10,#11,#12),.UNSPECIFIED.,.F.,.F.)
                     B_SPLINE_CURVE_WITH_KNOTS((3,3),(0.0,1.0),.UNSPECIFIED.));
                #14=SURFACE_CURVE('SC0',#13,(),.T.);
                #15=TRIMMED_CURVE('TC0',#14,(#10),(#12),.T.,.CARTESIAN.);
                #16=VERTEX_POINT('V0',#10);
                #17=VERTEX_POINT('V1',#12);
                #18=EDGE_CURVE('E0',#16,#17,#15,.T.);
                #19=EDGE_CURVE('E1',#17,#16,#15,.T.);
                #20=ORIENTED_EDGE('OE0',$,$,#18,.T.);
                #21=ORIENTED_EDGE('OE1',$,$,#19,.T.);
                #22=EDGE_LOOP('L0',(#20,#21));
                #23=FACE_OUTER_BOUND('B0',#22,.T.);
                #24=ADVANCED_FACE('F0',(#23),#5,.T.);
                #25=OPEN_SHELL('OS',(#24));
                ENDSEC;
                """);

        assertTrue(json.contains("\"edgeCount\":2"));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("[0.0,0.0,0.0]"));
        assertTrue(json.contains("[1.0,1.0,0.0]"));
    }

    @Test
    void shouldExportToroidalBandPreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/toroidal-band.step")));

        assertTrue(json.contains("\"surfaceType\":\"TOROIDAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":4"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
        assertTrue(json.contains("[5.707106781186548,0.0"));
    }

    @Test
    void shouldExportToroidalHolePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/toroidal-hole.step")));

        assertTrue(json.contains("\"surfaceType\":\"TOROIDAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":8"));
        assertTrue(json.contains("\"outer\":false"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldExportTrimmedCirclePcurveExamplePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/cylindrical-trimmed-circle-pcurve.step")));

        assertTrue(json.contains("\"surfaceType\":\"CYLINDRICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":3"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldExportTrimmedBsplinePcurveExamplePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/cylindrical-trimmed-bspline-pcurve.step")));

        assertTrue(json.contains("\"surfaceType\":\"CYLINDRICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":3"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldExportTrimmedEllipsePcurveExamplePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/cylindrical-trimmed-ellipse-pcurve.step")));

        assertTrue(json.contains("\"surfaceType\":\"CYLINDRICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":3"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldExportCylindricalHoleWithEllipsePcurveExamplePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/cylindrical-hole-ellipse-pcurve.step")));

        assertSingleSupportedFacePreviewWithInnerLoop(json, "CYLINDRICAL_SURFACE", 8);
    }

    @Test
    void shouldExportCylindricalTwoHolesExamplePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/cylindrical-two-holes.step")));

        assertTrue(json.contains("\"surfaceType\":\"CYLINDRICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":12"));
        assertEquals(2, countOccurrences(json, "\"outer\":false"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldExportCylindricalSeamTwoHolesExamplePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/cylindrical-seam-two-holes.step")));

        assertTrue(json.contains("\"surfaceType\":\"CYLINDRICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":12"));
        assertEquals(2, countOccurrences(json, "\"outer\":false"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldExportCylindricalMultiTrimmedLoopExamplePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/cylindrical-multi-trimmed-loop.step")));

        assertTrue(json.contains("\"surfaceType\":\"CYLINDRICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":4"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldExportCylindricalTrimmedLoopsWithHoleExamplePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/cylindrical-trimmed-loops-with-hole.step")));

        assertTrue(json.contains("\"surfaceType\":\"CYLINDRICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":8"));
        assertTrue(json.contains("\"outer\":false"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldExportConicalTrimmedEllipsePcurveExamplePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/conical-trimmed-ellipse-pcurve.step")));

        assertTrue(json.contains("\"surfaceType\":\"CONICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":3"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldExportToroidalTrimmedCirclePcurveExamplePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/toroidal-trimmed-circle-pcurve.step")));

        assertTrue(json.contains("\"surfaceType\":\"TOROIDAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":3"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldExportConicalTrimmedLoopsWithHoleExamplePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/conical-trimmed-loops-with-hole.step")));

        assertSingleSupportedFacePreviewWithInnerLoop(json, "CONICAL_SURFACE", 8);
    }

    @Test
    void shouldExportConicalTwoHolesExamplePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/conical-two-holes.step")));

        assertTrue(json.contains("\"surfaceType\":\"CONICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":12"));
        assertEquals(2, countOccurrences(json, "\"outer\":false"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldExportConicalSeamTwoHolesExamplePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/conical-seam-two-holes.step")));

        assertTrue(json.contains("\"surfaceType\":\"CONICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":12"));
        assertEquals(2, countOccurrences(json, "\"outer\":false"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldRenderConicalSeamFaceWithSingleFaceBound() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/conical-seam-missing-outer.step")));

        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":4"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"surfaceType\":\"CONICAL_SURFACE\""));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldMarkConicalSeamVertexLoopOuterBoundUnsupported() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/conical-seam-vertex-loop.step")));

        assertTrue(json.contains("\"faceCount\":0"));
        assertTrue(json.contains("\"unsupportedFaceCount\":1"));
        assertTrue(json.contains("\"unsupportedFaces\":[{\"id\":14"));
        assertTrue(json.contains("\"surfaceType\":\"CONICAL_SURFACE\""));
        assertTrue(json.contains("\"reason\":\"failed to build parametric loops\""));
    }

    @Test
    void shouldMarkConicalSeamVertexLoopHoleUnsupported() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/conical-seam-hole-vertex-loop.step")));

        assertTrue(json.contains("\"faceCount\":0"));
        assertTrue(json.contains("\"unsupportedFaceCount\":1"));
        assertTrue(json.contains("\"unsupportedFaces\":[{\"id\":186"));
        assertTrue(json.contains("\"surfaceType\":\"CONICAL_SURFACE\""));
        assertTrue(json.contains("\"reason\":\""));
    }

    @Test
    void shouldExportOrientedConicalFaceUsingPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('B0',(-1.0,0.0,0.0));
                #4=CARTESIAN_POINT('A1',(1.5,0.0,1.0));
                #5=CARTESIAN_POINT('B1',(-1.5,0.0,1.0));
                #20=DIRECTION('DZ',(0.0,0.0,1.0));
                #21=DIRECTION('DX',(1.0,0.0,0.0));
                #22=DIRECTION('DU',(1.0,0.0));
                #23=DIRECTION('DV',(0.0,1.0));
                #24=AXIS2_PLACEMENT_3D('AX0',#1,#20,#21);
                #25=AXIS2_PLACEMENT_3D('AX1',#4,#20,#21);
                #28=CONICAL_SURFACE('CN0',#24,1.0,0.4636476090008061);
                #29=ORIENTED_SURFACE('OS0',#28,.T.);
                #30=CIRCLE('C0',#24,1.0);
                #31=CIRCLE('C1',#25,1.5);
                #33=VECTOR('VU',#22,1.0);
                #34=VECTOR('VV',#23,1.0);
                #35=DIRECTION('GA',(0.5,0.0,1.0));
                #36=DIRECTION('GB',(-0.5,0.0,1.0));
                #39=VECTOR('VGA',#35,1.0);
                #40=VECTOR('VGB',#36,1.0);
                #41=VERTEX_POINT('V0',#2);
                #42=VERTEX_POINT('V1',#3);
                #43=VERTEX_POINT('V2',#5);
                #44=VERTEX_POINT('V3',#4);
                #50=CARTESIAN_POINT('UV00',(0.0,0.0));
                #51=LINE('UVL0',#50,#33);
                #52=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #53=DEFINITIONAL_REPRESENTATION('DEF0',(#51),#52);
                #54=PCURVE('PC0',#28,#53);
                #55=SURFACE_CURVE('SC0',#30,(#54),.PCURVE_S1.);
                #56=CARTESIAN_POINT('UV10',(3.141592653589793,0.0));
                #57=LINE('UVL1',#56,#34);
                #58=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #59=DEFINITIONAL_REPRESENTATION('DEF1',(#57),#58);
                #60=PCURVE('PC1',#28,#59);
                #61=LINE('L0',#3,#40);
                #62=SURFACE_CURVE('SC1',#61,(#60),.PCURVE_S1.);
                #63=CARTESIAN_POINT('UV20',(0.0,1.0));
                #64=LINE('UVL2',#63,#33);
                #65=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #66=DEFINITIONAL_REPRESENTATION('DEF2',(#64),#65);
                #67=PCURVE('PC2',#28,#66);
                #68=SURFACE_CURVE('SC2',#31,(#67),.PCURVE_S1.);
                #69=CARTESIAN_POINT('UV30',(0.0,0.0));
                #70=LINE('UVL3',#69,#34);
                #71=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #72=DEFINITIONAL_REPRESENTATION('DEF3',(#70),#71);
                #73=PCURVE('PC3',#28,#72);
                #74=CARTESIAN_POINT('UV31',(6.283185307179586,0.0));
                #75=LINE('UVL4',#74,#34);
                #76=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #77=DEFINITIONAL_REPRESENTATION('DEF4',(#75),#76);
                #78=PCURVE('PC4',#28,#77);
                #79=LINE('L1',#2,#39);
                #80=SEAM_CURVE('SEAM0',#79,(#73,#78),.PCURVE_S1.);
                #95=EDGE_CURVE('E0',#41,#42,#55,.T.);
                #96=EDGE_CURVE('E1',#42,#43,#62,.T.);
                #97=EDGE_CURVE('E2',#43,#44,#68,.F.);
                #98=EDGE_CURVE('E3',#44,#41,#80,.F.);
                #99=ORIENTED_EDGE('OE0',$,$,#95,.T.);
                #100=ORIENTED_EDGE('OE1',$,$,#96,.T.);
                #101=ORIENTED_EDGE('OE2',$,$,#97,.T.);
                #102=ORIENTED_EDGE('OE3',$,$,#98,.T.);
                #103=EDGE_LOOP('LOUT',(#99,#100,#101,#102));
                #104=FACE_OUTER_BOUND('BOUT',#103,.T.);
                #105=ADVANCED_FACE('F0',(#104),#29,.T.);
                #106=OPEN_SHELL('OS',(#105));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"ORIENTED_SURFACE\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":4"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldExportRectangularTrimmedConicalFaceUsingPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('B0',(-1.0,0.0,0.0));
                #4=CARTESIAN_POINT('A1',(1.5,0.0,1.0));
                #5=CARTESIAN_POINT('B1',(-1.5,0.0,1.0));
                #20=DIRECTION('DZ',(0.0,0.0,1.0));
                #21=DIRECTION('DX',(1.0,0.0,0.0));
                #22=DIRECTION('DU',(1.0,0.0));
                #23=DIRECTION('DV',(0.0,1.0));
                #24=AXIS2_PLACEMENT_3D('AX0',#1,#20,#21);
                #25=AXIS2_PLACEMENT_3D('AX1',#4,#20,#21);
                #28=CONICAL_SURFACE('CN0',#24,1.0,0.4636476090008061);
                #29=RECTANGULAR_TRIMMED_SURFACE('RT0',#28,0.0,3.141592653589793,0.0,1.0,.T.,.T.);
                #30=CIRCLE('C0',#24,1.0);
                #31=CIRCLE('C1',#25,1.5);
                #33=VECTOR('VU',#22,1.0);
                #34=VECTOR('VV',#23,1.0);
                #35=DIRECTION('GA',(0.5,0.0,1.0));
                #36=DIRECTION('GB',(-0.5,0.0,1.0));
                #39=VECTOR('VGA',#35,1.0);
                #40=VECTOR('VGB',#36,1.0);
                #41=VERTEX_POINT('V0',#2);
                #42=VERTEX_POINT('V1',#3);
                #43=VERTEX_POINT('V2',#5);
                #44=VERTEX_POINT('V3',#4);
                #50=CARTESIAN_POINT('UV00',(0.0,0.0));
                #51=LINE('UVL0',#50,#33);
                #52=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #53=DEFINITIONAL_REPRESENTATION('DEF0',(#51),#52);
                #54=PCURVE('PC0',#28,#53);
                #55=SURFACE_CURVE('SC0',#30,(#54),.PCURVE_S1.);
                #56=CARTESIAN_POINT('UV10',(3.141592653589793,0.0));
                #57=LINE('UVL1',#56,#34);
                #58=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #59=DEFINITIONAL_REPRESENTATION('DEF1',(#57),#58);
                #60=PCURVE('PC1',#28,#59);
                #61=LINE('L0',#3,#40);
                #62=SURFACE_CURVE('SC1',#61,(#60),.PCURVE_S1.);
                #63=CARTESIAN_POINT('UV20',(0.0,1.0));
                #64=LINE('UVL2',#63,#33);
                #65=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #66=DEFINITIONAL_REPRESENTATION('DEF2',(#64),#65);
                #67=PCURVE('PC2',#28,#66);
                #68=SURFACE_CURVE('SC2',#31,(#67),.PCURVE_S1.);
                #69=CARTESIAN_POINT('UV30',(0.0,0.0));
                #70=LINE('UVL3',#69,#34);
                #71=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #72=DEFINITIONAL_REPRESENTATION('DEF3',(#70),#71);
                #73=PCURVE('PC3',#28,#72);
                #74=CARTESIAN_POINT('UV31',(6.283185307179586,0.0));
                #75=LINE('UVL4',#74,#34);
                #76=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #77=DEFINITIONAL_REPRESENTATION('DEF4',(#75),#76);
                #78=PCURVE('PC4',#28,#77);
                #79=LINE('L1',#2,#39);
                #80=SEAM_CURVE('SEAM0',#79,(#73,#78),.PCURVE_S1.);
                #95=EDGE_CURVE('E0',#41,#42,#55,.T.);
                #96=EDGE_CURVE('E1',#42,#43,#62,.T.);
                #97=EDGE_CURVE('E2',#43,#44,#68,.F.);
                #98=EDGE_CURVE('E3',#44,#41,#80,.F.);
                #99=ORIENTED_EDGE('OE0',$,$,#95,.T.);
                #100=ORIENTED_EDGE('OE1',$,$,#96,.T.);
                #101=ORIENTED_EDGE('OE2',$,$,#97,.T.);
                #102=ORIENTED_EDGE('OE3',$,$,#98,.T.);
                #103=EDGE_LOOP('LOUT',(#99,#100,#101,#102));
                #104=FACE_OUTER_BOUND('BOUT',#103,.T.);
                #105=ADVANCED_FACE('F0',(#104),#29,.T.);
                #106=OPEN_SHELL('OS',(#105));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"RECTANGULAR_TRIMMED_SURFACE\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":4"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldExportCurveBoundedConicalFaceUsingPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('B0',(-1.0,0.0,0.0));
                #4=CARTESIAN_POINT('A1',(1.5,0.0,1.0));
                #5=CARTESIAN_POINT('B1',(-1.5,0.0,1.0));
                #20=DIRECTION('DZ',(0.0,0.0,1.0));
                #21=DIRECTION('DX',(1.0,0.0,0.0));
                #22=DIRECTION('DU',(1.0,0.0));
                #23=DIRECTION('DV',(0.0,1.0));
                #24=AXIS2_PLACEMENT_3D('AX0',#1,#20,#21);
                #25=AXIS2_PLACEMENT_3D('AX1',#4,#20,#21);
                #28=CONICAL_SURFACE('CN0',#24,1.0,0.4636476090008061);
                #29=CIRCLE('C0',#24,1.0);
                #30=CIRCLE('C1',#25,1.5);
                #31=CURVE_BOUNDED_SURFACE('CBS0',#28,(#29,#30),.T.);
                #33=VECTOR('VU',#22,1.0);
                #34=VECTOR('VV',#23,1.0);
                #35=DIRECTION('GA',(0.5,0.0,1.0));
                #36=DIRECTION('GB',(-0.5,0.0,1.0));
                #39=VECTOR('VGA',#35,1.0);
                #40=VECTOR('VGB',#36,1.0);
                #41=VERTEX_POINT('V0',#2);
                #42=VERTEX_POINT('V1',#3);
                #43=VERTEX_POINT('V2',#5);
                #44=VERTEX_POINT('V3',#4);
                #50=CARTESIAN_POINT('UV00',(0.0,0.0));
                #51=LINE('UVL0',#50,#33);
                #52=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #53=DEFINITIONAL_REPRESENTATION('DEF0',(#51),#52);
                #54=PCURVE('PC0',#28,#53);
                #55=SURFACE_CURVE('SC0',#29,(#54),.PCURVE_S1.);
                #56=CARTESIAN_POINT('UV10',(3.141592653589793,0.0));
                #57=LINE('UVL1',#56,#34);
                #58=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #59=DEFINITIONAL_REPRESENTATION('DEF1',(#57),#58);
                #60=PCURVE('PC1',#28,#59);
                #61=LINE('L0',#3,#40);
                #62=SURFACE_CURVE('SC1',#61,(#60),.PCURVE_S1.);
                #63=CARTESIAN_POINT('UV20',(0.0,1.0));
                #64=LINE('UVL2',#63,#33);
                #65=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #66=DEFINITIONAL_REPRESENTATION('DEF2',(#64),#65);
                #67=PCURVE('PC2',#28,#66);
                #68=SURFACE_CURVE('SC2',#30,(#67),.PCURVE_S1.);
                #69=CARTESIAN_POINT('UV30',(0.0,0.0));
                #70=LINE('UVL3',#69,#34);
                #71=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #72=DEFINITIONAL_REPRESENTATION('DEF3',(#70),#71);
                #73=PCURVE('PC3',#28,#72);
                #74=CARTESIAN_POINT('UV31',(6.283185307179586,0.0));
                #75=LINE('UVL4',#74,#34);
                #76=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #77=DEFINITIONAL_REPRESENTATION('DEF4',(#75),#76);
                #78=PCURVE('PC4',#28,#77);
                #79=LINE('L1',#2,#39);
                #80=SEAM_CURVE('SEAM0',#79,(#73,#78),.PCURVE_S1.);
                #95=EDGE_CURVE('E0',#41,#42,#55,.T.);
                #96=EDGE_CURVE('E1',#42,#43,#62,.T.);
                #97=EDGE_CURVE('E2',#43,#44,#68,.F.);
                #98=EDGE_CURVE('E3',#44,#41,#80,.F.);
                #99=ORIENTED_EDGE('OE0',$,$,#95,.T.);
                #100=ORIENTED_EDGE('OE1',$,$,#96,.T.);
                #101=ORIENTED_EDGE('OE2',$,$,#97,.T.);
                #102=ORIENTED_EDGE('OE3',$,$,#98,.T.);
                #103=EDGE_LOOP('LOUT',(#99,#100,#101,#102));
                #104=FACE_OUTER_BOUND('BOUT',#103,.T.);
                #105=ADVANCED_FACE('F0',(#104),#31,.T.);
                #106=OPEN_SHELL('OS',(#105));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"CURVE_BOUNDED_SURFACE\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":4"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldExportToroidalTrimmedLoopsWithHoleExamplePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/toroidal-trimmed-loops-with-hole.step")));

        assertTrue(json.contains("\"surfaceType\":\"TOROIDAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":8"));
        assertTrue(json.contains("\"outer\":false"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldExportToroidalTwoHolesExamplePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/toroidal-two-holes.step")));

        assertTrue(json.contains("\"surfaceType\":\"TOROIDAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":12"));
        assertEquals(2, countOccurrences(json, "\"outer\":false"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldExportToroidalSeamTwoHolesExamplePreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/toroidal-seam-two-holes.step")));

        assertTrue(json.contains("\"surfaceType\":\"TOROIDAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":12"));
        assertEquals(2, countOccurrences(json, "\"outer\":false"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldRenderToroidalSeamFaceWithSingleFaceBound() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/toroidal-seam-missing-outer.step")));

        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":4"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"surfaceType\":\"TOROIDAL_SURFACE\""));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldMarkToroidalSeamVertexLoopOuterBoundUnsupported() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/toroidal-seam-vertex-loop.step")));

        assertTrue(json.contains("\"faceCount\":0"));
        assertTrue(json.contains("\"unsupportedFaceCount\":1"));
        assertTrue(json.contains("\"unsupportedFaces\":[{\"id\":14"));
        assertTrue(json.contains("\"surfaceType\":\"TOROIDAL_SURFACE\""));
        assertTrue(json.contains("\"reason\":\"failed to build parametric loops\""));
    }

    @Test
    void shouldMarkToroidalSeamVertexLoopHoleUnsupported() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/toroidal-seam-hole-vertex-loop.step")));

        assertTrue(json.contains("\"faceCount\":0"));
        assertTrue(json.contains("\"unsupportedFaceCount\":1"));
        assertTrue(json.contains("\"unsupportedFaces\":[{\"id\":186"));
        assertTrue(json.contains("\"surfaceType\":\"TOROIDAL_SURFACE\""));
        assertTrue(json.contains("\"reason\":\""));
    }

    @Test
    void shouldExportToroidalFaceWithInnerLoopHole() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P00',(5.707106781186548,0.0,-0.7071067811865475));
                #3=CARTESIAN_POINT('P01',(5.707106781186548,0.0,0.7071067811865475));
                #4=CARTESIAN_POINT('P10',(0.0,5.707106781186548,-0.7071067811865475));
                #5=CARTESIAN_POINT('P11',(0.0,5.707106781186548,0.7071067811865475));
                #6=CARTESIAN_POINT('I00',(5.5080060663898855,2.328747613638301,-0.19866933079506122));
                #7=CARTESIAN_POINT('I10',(3.2310437612526184,5.032052512472857,-0.19866933079506122));
                #8=CARTESIAN_POINT('I11',(3.2310437612526184,5.032052512472857,0.19866933079506122));
                #9=CARTESIAN_POINT('I01',(5.5080060663898855,2.328747613638301,0.19866933079506122));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=DIRECTION('DY',(0.0,1.0,0.0));
                #13=DIRECTION('NX',(-1.0,0.0,0.0));
                #14=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #15=TOROIDAL_SURFACE('TO0',#14,5.0,1.0);
                #16=CARTESIAN_POINT('CL',(0.0,0.0,-0.7071067811865475));
                #17=CARTESIAN_POINT('CU',(0.0,0.0,0.7071067811865475));
                #18=CARTESIAN_POINT('CR',(5.0,0.0,0.0));
                #19=CARTESIAN_POINT('CT',(0.0,5.0,0.0));
                #20=AXIS2_PLACEMENT_3D('AL',#16,#10,#11);
                #21=AXIS2_PLACEMENT_3D('AU',#17,#10,#11);
                #22=AXIS2_PLACEMENT_3D('AR',#18,#12,#11);
                #23=AXIS2_PLACEMENT_3D('AT',#19,#13,#12);
                #24=CIRCLE('C0',#20,5.707106781186548);
                #25=CIRCLE('C1',#23,1.0);
                #26=CIRCLE('C2',#21,5.707106781186548);
                #27=CIRCLE('C3',#22,1.0);
                #28=CARTESIAN_POINT('ICL',(0.0,0.0,-0.19866933079506122));
                #29=CARTESIAN_POINT('ICU',(0.0,0.0,0.19866933079506122));
                #30=CARTESIAN_POINT('IC0',(4.6053049700144255,1.9470917115432527,0.0));
                #31=CARTESIAN_POINT('IC1',(2.701511529340699,4.207354924039483,0.0));
                #32=DIRECTION('TU0',(-0.3894183423086505,0.9210609940028851,0.0));
                #33=DIRECTION('RU0',(0.9210609940028851,0.3894183423086505,0.0));
                #34=DIRECTION('TU1',(-0.8414709848078965,0.5403023058681398,0.0));
                #35=DIRECTION('RU1',(0.5403023058681398,0.8414709848078965,0.0));
                #36=AXIS2_PLACEMENT_3D('AIL',#28,#10,#11);
                #37=AXIS2_PLACEMENT_3D('AIU',#29,#10,#11);
                #38=AXIS2_PLACEMENT_3D('AI0',#30,#32,#33);
                #39=AXIS2_PLACEMENT_3D('AI1',#31,#34,#35);
                #40=CIRCLE('CI0',#36,5.980066577841241);
                #41=CIRCLE('CI1',#39,1.0);
                #42=CIRCLE('CI2',#37,5.980066577841241);
                #43=CIRCLE('CI3',#38,1.0);
                #50=VERTEX_POINT('V0',#2);
                #51=VERTEX_POINT('V1',#4);
                #52=VERTEX_POINT('V2',#5);
                #53=VERTEX_POINT('V3',#3);
                #54=VERTEX_POINT('V4',#6);
                #55=VERTEX_POINT('V5',#7);
                #56=VERTEX_POINT('V6',#8);
                #57=VERTEX_POINT('V7',#9);
                #60=EDGE_CURVE('E0',#50,#51,#24,.T.);
                #61=EDGE_CURVE('E1',#51,#52,#25,.T.);
                #62=EDGE_CURVE('E2',#53,#52,#26,.T.);
                #63=EDGE_CURVE('E3',#50,#53,#27,.T.);
                #64=EDGE_CURVE('E4',#54,#55,#40,.T.);
                #65=EDGE_CURVE('E5',#55,#56,#41,.T.);
                #66=EDGE_CURVE('E6',#57,#56,#42,.T.);
                #67=EDGE_CURVE('E7',#54,#57,#43,.T.);
                #70=ORIENTED_EDGE('OE0',$,$,#60,.T.);
                #71=ORIENTED_EDGE('OE1',$,$,#61,.T.);
                #72=ORIENTED_EDGE('OE2',$,$,#62,.F.);
                #73=ORIENTED_EDGE('OE3',$,$,#63,.F.);
                #74=ORIENTED_EDGE('OE4',$,$,#64,.T.);
                #75=ORIENTED_EDGE('OE5',$,$,#65,.T.);
                #76=ORIENTED_EDGE('OE6',$,$,#66,.F.);
                #77=ORIENTED_EDGE('OE7',$,$,#67,.F.);
                #80=EDGE_LOOP('L0',(#70,#71,#72,#73));
                #81=FACE_OUTER_BOUND('B0',#80,.T.);
                #82=EDGE_LOOP('L1',(#74,#75,#76,#77));
                #83=FACE_BOUND('B1',#82,.T.);
                #90=ADVANCED_FACE('F0',(#81,#83),#15,.T.);
                #91=OPEN_SHELL('OS',(#90));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"TOROIDAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":8"));
        assertTrue(json.contains("\"outer\":false"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldSampleTrimmedCircularPcurveInPreviewExporter() throws Exception {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=CYLINDRICAL_SURFACE('CY0',#4,1.0);
                #10=CARTESIAN_POINT('P0',(0.5403023058681398,0.8414709848078965,0.0));
                #11=CARTESIAN_POINT('P1',(1.0,0.0,1.0));
                #12=VERTEX_POINT('V0',#10);
                #13=VERTEX_POINT('V1',#11);
                #14=DIRECTION('D3',(0.45969769413186023,-0.8414709848078965,1.0));
                #15=VECTOR('V3',#14,1.0);
                #16=LINE('L0',#10,#15);
                #17=CARTESIAN_POINT('UVC',(0.0,0.0));
                #18=DIRECTION('DUV',(1.0,0.0));
                #19=AXIS2_PLACEMENT_2D('A2',#17,#18);
                #20=CIRCLE('PC',#19,1.0);
                #21=CARTESIAN_POINT('T0',(1.0,0.0));
                #22=CARTESIAN_POINT('T1',(0.0,1.0));
                #23=TRIMMED_CURVE('TC0',#20,(#21),(#22),.T.,.CARTESIAN.);
                #24=REPRESENTATION_CONTEXT('PC','PARAMETRIC');
                #25=DEFINITIONAL_REPRESENTATION('DEF',(#23),#24);
                #26=PCURVE('PC0',#5,#25);
                #27=SURFACE_CURVE('SC0',#16,(#26),.PCURVE_S1.);
                #28=EDGE_CURVE('E0',#12,#13,#27,.T.);
                #29=ORIENTED_EDGE('OE0',$,$,#28,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        Method mapperForSurface = StepPreviewJsonExporter.class.getDeclaredMethod(
                "mapperForSurface",
                StepEntity.class,
                StepCadBuilder.class
        );
        mapperForSurface.setAccessible(true);
        Object mapper = mapperForSurface.invoke(null, resolved.get(5), builder);
        assertNotNull(mapper);

        Method sampleParametricOrientedEdge = StepPreviewJsonExporter.class.getDeclaredMethod(
                "sampleParametricOrientedEdge",
                StepOrientedEdge.class,
                StepEntity.class,
                Class.forName("com.minicad.app.StepPreviewJsonExporter$ParametricSurfaceMapper"),
                StepCadBuilder.class
        );
        sampleParametricOrientedEdge.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Object> points = (List<Object>) sampleParametricOrientedEdge.invoke(null, resolved.get(29), resolved.get(5), mapper, builder);
        assertTrue(points.size() >= 12);

        Method u = points.getFirst().getClass().getDeclaredMethod("u");
        Method v = points.getFirst().getClass().getDeclaredMethod("v");
        double startU = (double) u.invoke(points.getFirst());
        double startV = (double) v.invoke(points.getFirst());
        double endU = (double) u.invoke(points.getLast());
        double endV = (double) v.invoke(points.getLast());
        double midU = (double) u.invoke(points.get(points.size() / 2));
        double midV = (double) v.invoke(points.get(points.size() / 2));

        assertEquals(1.0, startU, 1.0e-9);
        assertEquals(0.0, startV, 1.0e-9);
        assertEquals(0.0, endU, 1.0e-9);
        assertEquals(1.0, endV, 1.0e-9);
        assertTrue(midU > 0.6 && midU < 0.8);
        assertTrue(midV > 0.6 && midV < 0.8);
    }

    @Test
    void shouldRespectReversedFaceBoundOrientationWhenSamplingLoop() throws Exception {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse("""
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
                #71=FACE_OUTER_BOUND('FOB0',#70,.T.);
                #72=FACE_OUTER_BOUND('FOB1',#70,.F.);
                #80=ADVANCED_FACE('F0',(#71),#13,.T.);
                ENDSEC;
                """));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        FaceBound forwardBound = builder.buildFaceBound(71);
        FaceBound reversedBound = builder.buildFaceBound(72);
        Method sampleLoop = StepPreviewJsonExporter.class.getDeclaredMethod("sampleLoop", FaceBound.class);
        sampleLoop.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Object> forward = (List<Object>) sampleLoop.invoke(null, forwardBound);
        @SuppressWarnings("unchecked")
        List<Object> reversed = (List<Object>) sampleLoop.invoke(null, reversedBound);

        Method x = forward.getFirst().getClass().getDeclaredMethod("x");
        Method y = forward.getFirst().getClass().getDeclaredMethod("y");

        assertEquals(forward.size(), reversed.size());
        assertTrue(signedArea(forward, x, y) > 0.0);
        assertTrue(signedArea(reversed, x, y) < 0.0);
    }

    @Test
    void shouldNormalizePeriodicUvLoopAcrossSeam() throws Exception {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=CYLINDRICAL_SURFACE('CY0',#4,1.0);
                ENDSEC;
                """));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        Method mapperForSurface = StepPreviewJsonExporter.class.getDeclaredMethod(
                "mapperForSurface",
                StepEntity.class,
                StepCadBuilder.class
        );
        mapperForSurface.setAccessible(true);
        Object mapper = mapperForSurface.invoke(null, resolved.get(5), builder);

        Class<?> uvPointClass = Class.forName("com.minicad.app.StepPreviewJsonExporter$UvPoint");
        var constructor = uvPointClass.getDeclaredConstructor(double.class, double.class);
        constructor.setAccessible(true);
        Object p0 = constructor.newInstance(6.20, 0.0);
        Object p1 = constructor.newInstance(0.05, 0.5);
        Object p2 = constructor.newInstance(6.24, 1.0);

        Method normalizePeriodicLoop = StepPreviewJsonExporter.class.getDeclaredMethod(
                "normalizePeriodicLoop",
                List.class,
                Class.forName("com.minicad.app.StepPreviewJsonExporter$ParametricSurfaceMapper")
        );
        normalizePeriodicLoop.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Object> normalized = (List<Object>) normalizePeriodicLoop.invoke(null, List.of(p0, p1, p2), mapper);
        Method u = uvPointClass.getDeclaredMethod("u");

        double u0 = (double) u.invoke(normalized.get(0));
        double u1 = (double) u.invoke(normalized.get(1));
        double u2 = (double) u.invoke(normalized.get(2));

        assertTrue(Math.abs(u1 - u0) < Math.PI);
        assertTrue(Math.abs(u2 - u1) < Math.PI);
        assertTrue(Math.abs(u2 - u0) < Math.PI);
    }

    @Test
    void shouldTreatPointOnParametricBoundaryAsInside() throws Exception {
        Class<?> uvPointClass = Class.forName("com.minicad.app.StepPreviewJsonExporter$UvPoint");
        var constructor = uvPointClass.getDeclaredConstructor(double.class, double.class);
        constructor.setAccessible(true);

        List<Object> polygon = List.of(
                constructor.newInstance(0.0, 0.0),
                constructor.newInstance(1.0, 0.0),
                constructor.newInstance(1.0, 1.0),
                constructor.newInstance(0.0, 1.0),
                constructor.newInstance(0.0, 0.0)
        );
        Object point = constructor.newInstance(0.5, 0.0);

        Method contains = StepPreviewJsonExporter.class.getDeclaredMethod("contains", List.class, uvPointClass);
        contains.setAccessible(true);

        assertTrue((boolean) contains.invoke(null, polygon, point));
    }

    @Test
    void shouldDetectPointOnParametricHoleBoundary() throws Exception {
        Class<?> uvPointClass = Class.forName("com.minicad.app.StepPreviewJsonExporter$UvPoint");
        var constructor = uvPointClass.getDeclaredConstructor(double.class, double.class);
        constructor.setAccessible(true);

        List<Object> polygon = List.of(
                constructor.newInstance(0.2, 0.2),
                constructor.newInstance(0.8, 0.2),
                constructor.newInstance(0.8, 0.8),
                constructor.newInstance(0.2, 0.8),
                constructor.newInstance(0.2, 0.2)
        );
        Object point = constructor.newInstance(0.8, 0.5);

        Method isOnPolygonBoundary = StepPreviewJsonExporter.class.getDeclaredMethod("isOnPolygonBoundary", List.class, uvPointClass);
        isOnPolygonBoundary.setAccessible(true);

        assertTrue((boolean) isOnPolygonBoundary.invoke(null, polygon, point));
    }

    private static double signedArea(List<Object> points, Method x, Method y) throws Exception {
        double area = 0.0;
        for (int index = 0; index + 1 < points.size(); index++) {
            double x0 = (double) x.invoke(points.get(index));
            double y0 = (double) y.invoke(points.get(index));
            double x1 = (double) x.invoke(points.get(index + 1));
            double y1 = (double) y.invoke(points.get(index + 1));
            area += (x0 * y1) - (x1 * y0);
        }
        return area * 0.5;
    }

    @Test
    void shouldRespectReverseSenseOnTrimmedCircularPcurveInPreviewExporter() throws Exception {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=CYLINDRICAL_SURFACE('CY0',#4,1.0);
                #10=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(0.5403023058681398,0.8414709848078965,1.0));
                #12=VERTEX_POINT('V0',#10);
                #13=VERTEX_POINT('V1',#11);
                #14=DIRECTION('D3',(-0.45969769413186023,0.8414709848078965,1.0));
                #15=VECTOR('V3',#14,1.0);
                #16=LINE('L0',#10,#15);
                #17=CARTESIAN_POINT('UVC',(0.0,0.0));
                #18=DIRECTION('DUV',(1.0,0.0));
                #19=AXIS2_PLACEMENT_2D('A2',#17,#18);
                #20=CIRCLE('PC',#19,1.0);
                #21=CARTESIAN_POINT('T0',(1.0,0.0));
                #22=CARTESIAN_POINT('T1',(0.0,1.0));
                #23=TRIMMED_CURVE('TC0',#20,(#21),(#22),.F.,.CARTESIAN.);
                #24=REPRESENTATION_CONTEXT('PC','PARAMETRIC');
                #25=DEFINITIONAL_REPRESENTATION('DEF',(#23),#24);
                #26=PCURVE('PC0',#5,#25);
                #27=SURFACE_CURVE('SC0',#16,(#26),.PCURVE_S1.);
                #28=EDGE_CURVE('E0',#12,#13,#27,.T.);
                #29=ORIENTED_EDGE('OE0',$,$,#28,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        Method mapperForSurface = StepPreviewJsonExporter.class.getDeclaredMethod(
                "mapperForSurface",
                StepEntity.class,
                StepCadBuilder.class
        );
        mapperForSurface.setAccessible(true);
        Object mapper = mapperForSurface.invoke(null, resolved.get(5), builder);
        assertNotNull(mapper);

        Method sampleParametricOrientedEdge = StepPreviewJsonExporter.class.getDeclaredMethod(
                "sampleParametricOrientedEdge",
                StepOrientedEdge.class,
                StepEntity.class,
                Class.forName("com.minicad.app.StepPreviewJsonExporter$ParametricSurfaceMapper"),
                StepCadBuilder.class
        );
        sampleParametricOrientedEdge.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Object> points = (List<Object>) sampleParametricOrientedEdge.invoke(null, resolved.get(29), resolved.get(5), mapper, builder);
        assertTrue(points.size() >= 12);

        Method u = points.getFirst().getClass().getDeclaredMethod("u");
        Method v = points.getFirst().getClass().getDeclaredMethod("v");
        double startU = (double) u.invoke(points.getFirst());
        double startV = (double) v.invoke(points.getFirst());
        double endU = (double) u.invoke(points.getLast());
        double endV = (double) v.invoke(points.getLast());
        double midU = (double) u.invoke(points.get(points.size() / 2));
        double midV = (double) v.invoke(points.get(points.size() / 2));

        assertEquals(0.0, startU, 1.0e-9);
        assertEquals(0.0, startV, 1.0e-9);
        assertEquals(1.0, endU, 1.0e-9);
        assertEquals(1.0, endV, 1.0e-9);
        assertTrue(midU > 0.6 && midU < 0.8);
        assertTrue(midV > 0.6 && midV < 0.8);
    }

    @Test
    void shouldSampleTrimmedBsplinePcurveInPreviewExporter() throws Exception {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=CYLINDRICAL_SURFACE('CY0',#4,1.0);
                #10=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(1.0,0.0,1.0));
                #12=VERTEX_POINT('V0',#10);
                #13=VERTEX_POINT('V1',#11);
                #14=DIRECTION('D3',(0.0,0.0,1.0));
                #15=VECTOR('V3',#14,1.0);
                #16=LINE('L0',#10,#15);
                #20=CARTESIAN_POINT('UV0',(0.0,0.0));
                #21=CARTESIAN_POINT('UV1',(0.4,0.5));
                #22=CARTESIAN_POINT('UV2',(0.0,1.0));
                #23=(B_SPLINE_CURVE('B2D',2,(#20,#21,#22),.UNSPECIFIED.,.F.,.F.)
                     B_SPLINE_CURVE_WITH_KNOTS((3,3),(0.0,1.0),.UNSPECIFIED.));
                #24=CARTESIAN_POINT('T0',(0.0,0.0));
                #25=CARTESIAN_POINT('T1',(0.0,1.0));
                #26=TRIMMED_CURVE('TC0',#23,(#24),(#25),.T.,.CARTESIAN.);
                #27=REPRESENTATION_CONTEXT('PC','PARAMETRIC');
                #28=DEFINITIONAL_REPRESENTATION('DEF',(#26),#27);
                #29=PCURVE('PC0',#5,#28);
                #30=SURFACE_CURVE('SC0',#16,(#29),.PCURVE_S1.);
                #31=EDGE_CURVE('E0',#12,#13,#30,.T.);
                #32=ORIENTED_EDGE('OE0',$,$,#31,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        Method mapperForSurface = StepPreviewJsonExporter.class.getDeclaredMethod(
                "mapperForSurface",
                StepEntity.class,
                StepCadBuilder.class
        );
        mapperForSurface.setAccessible(true);
        Object mapper = mapperForSurface.invoke(null, resolved.get(5), builder);
        assertNotNull(mapper);

        Method sampleParametricOrientedEdge = StepPreviewJsonExporter.class.getDeclaredMethod(
                "sampleParametricOrientedEdge",
                StepOrientedEdge.class,
                StepEntity.class,
                Class.forName("com.minicad.app.StepPreviewJsonExporter$ParametricSurfaceMapper"),
                StepCadBuilder.class
        );
        sampleParametricOrientedEdge.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Object> points = (List<Object>) sampleParametricOrientedEdge.invoke(null, resolved.get(32), resolved.get(5), mapper, builder);
        assertTrue(points.size() >= 8);

        Method u = points.getFirst().getClass().getDeclaredMethod("u");
        Method v = points.getFirst().getClass().getDeclaredMethod("v");
        double startU = (double) u.invoke(points.getFirst());
        double startV = (double) v.invoke(points.getFirst());
        double endU = (double) u.invoke(points.getLast());
        double endV = (double) v.invoke(points.getLast());
        double midU = (double) u.invoke(points.get(points.size() / 2));
        double midV = (double) v.invoke(points.get(points.size() / 2));

        assertEquals(0.0, startU, 1.0e-9);
        assertEquals(0.0, startV, 1.0e-9);
        assertEquals(0.0, endU, 1.0e-9);
        assertEquals(1.0, endV, 1.0e-9);
        assertTrue(midU > 0.15 && midU < 0.35);
        assertTrue(midV > 0.35 && midV < 0.65);
    }

    @Test
    void shouldSampleEllipsePcurveInPreviewExporter() throws Exception {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=CYLINDRICAL_SURFACE('CY0',#4,1.0);
                #10=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(1.0,0.0,1.0));
                #12=VERTEX_POINT('V0',#10);
                #13=VERTEX_POINT('V1',#11);
                #14=DIRECTION('D3',(0.0,0.0,1.0));
                #15=VECTOR('V3',#14,1.0);
                #16=LINE('L0',#10,#15);
                #20=CARTESIAN_POINT('UV0',(0.0,0.5));
                #21=DIRECTION('DUV',(1.0,0.0));
                #22=AXIS2_PLACEMENT_2D('A2',#20,#21);
                #23=ELLIPSE('PE',#22,0.25,0.5);
                #24=REPRESENTATION_CONTEXT('PC','PARAMETRIC');
                #25=DEFINITIONAL_REPRESENTATION('DEF',(#23),#24);
                #26=PCURVE('PC0',#5,#25);
                #27=SURFACE_CURVE('SC0',#16,(#26),.PCURVE_S1.);
                #28=EDGE_CURVE('E0',#12,#13,#27,.T.);
                #29=ORIENTED_EDGE('OE0',$,$,#28,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        Method mapperForSurface = StepPreviewJsonExporter.class.getDeclaredMethod(
                "mapperForSurface",
                StepEntity.class,
                StepCadBuilder.class
        );
        mapperForSurface.setAccessible(true);
        Object mapper = mapperForSurface.invoke(null, resolved.get(5), builder);
        assertNotNull(mapper);

        Method sampleParametricOrientedEdge = StepPreviewJsonExporter.class.getDeclaredMethod(
                "sampleParametricOrientedEdge",
                StepOrientedEdge.class,
                StepEntity.class,
                Class.forName("com.minicad.app.StepPreviewJsonExporter$ParametricSurfaceMapper"),
                StepCadBuilder.class
        );
        sampleParametricOrientedEdge.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Object> points = (List<Object>) sampleParametricOrientedEdge.invoke(null, resolved.get(29), resolved.get(5), mapper, builder);
        assertTrue(points.size() >= 12);

        Method u = points.getFirst().getClass().getDeclaredMethod("u");
        Method v = points.getFirst().getClass().getDeclaredMethod("v");
        double startU = (double) u.invoke(points.getFirst());
        double startV = (double) v.invoke(points.getFirst());
        double endU = (double) u.invoke(points.getLast());
        double endV = (double) v.invoke(points.getLast());
        double midU = (double) u.invoke(points.get(points.size() / 2));
        double midV = (double) v.invoke(points.get(points.size() / 2));

        assertEquals(0.0, startU, 1.0e-9);
        assertEquals(0.0, startV, 1.0e-9);
        assertEquals(0.0, endU, 1.0e-9);
        assertEquals(1.0, endV, 1.0e-9);
        assertTrue(midU > -0.26 && midU < -0.15);
        assertTrue(midV > 0.35 && midV < 0.65);
    }

    @Test
    void shouldIgnorePcurveBoundToDifferentSurface() throws Exception {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=DIRECTION('DZ',(0.0,0.0,1.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX0',#1,#3,#4);
                #6=AXIS2_PLACEMENT_3D('AX1',#2,#3,#4);
                #7=CYLINDRICAL_SURFACE('CY0',#5,1.0);
                #8=CYLINDRICAL_SURFACE('CY1',#6,1.0);
                #10=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(1.0,0.0,1.0));
                #12=VERTEX_POINT('V0',#10);
                #13=VERTEX_POINT('V1',#11);
                #14=DIRECTION('UP',(0.0,0.0,1.0));
                #15=VECTOR('VUP',#14,1.0);
                #16=LINE('L0',#10,#15);
                #20=CARTESIAN_POINT('UV0',(0.0,0.0));
                #21=DIRECTION('DU',(0.0,1.0));
                #22=VECTOR('VV',#21,1.0);
                #23=LINE('BAD',#20,#22);
                #24=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #25=DEFINITIONAL_REPRESENTATION('DEF0',(#23),#24);
                #26=PCURVE('PC0',#8,#25);
                #27=SURFACE_CURVE('SC0',#16,(#26),.PCURVE_S1.);
                #28=EDGE_CURVE('E0',#12,#13,#27,.T.);
                #29=ORIENTED_EDGE('OE0',$,$,#28,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        Method mapperForSurface = StepPreviewJsonExporter.class.getDeclaredMethod(
                "mapperForSurface",
                StepEntity.class,
                StepCadBuilder.class
        );
        mapperForSurface.setAccessible(true);
        Object mapper = mapperForSurface.invoke(null, resolved.get(7), builder);
        assertNotNull(mapper);

        Method sampleParametricOrientedEdge = StepPreviewJsonExporter.class.getDeclaredMethod(
                "sampleParametricOrientedEdge",
                StepOrientedEdge.class,
                StepEntity.class,
                Class.forName("com.minicad.app.StepPreviewJsonExporter$ParametricSurfaceMapper"),
                StepCadBuilder.class
        );
        sampleParametricOrientedEdge.setAccessible(true);

        Object points = sampleParametricOrientedEdge.invoke(null, resolved.get(29), resolved.get(7), mapper, builder);
        assertEquals(null, points);
    }

    @Test
    void shouldPickMatchingSeamPcurveWhenOtherSeamPcurveTargetsDifferentSurface() throws Exception {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=DIRECTION('DZ',(0.0,0.0,1.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX0',#1,#3,#4);
                #6=AXIS2_PLACEMENT_3D('AX1',#2,#3,#4);
                #7=CYLINDRICAL_SURFACE('CY0',#5,1.0);
                #8=CYLINDRICAL_SURFACE('CY1',#6,1.0);
                #10=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(1.0,0.0,1.0));
                #12=VERTEX_POINT('V0',#10);
                #13=VERTEX_POINT('V1',#11);
                #14=DIRECTION('UP',(0.0,0.0,1.0));
                #15=VECTOR('VUP',#14,1.0);
                #16=LINE('L0',#10,#15);
                #20=CARTESIAN_POINT('UV0',(0.0,0.0));
                #21=DIRECTION('DU0',(0.0,1.0));
                #22=VECTOR('VV0',#21,1.0);
                #23=LINE('GOOD',#20,#22);
                #24=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #25=DEFINITIONAL_REPRESENTATION('DEF0',(#23),#24);
                #26=PCURVE('PC0',#7,#25);
                #27=CARTESIAN_POINT('UV1',(3.141592653589793,0.0));
                #28=DIRECTION('DU1',(0.0,1.0));
                #29=VECTOR('VV1',#28,1.0);
                #30=LINE('BAD',#27,#29);
                #31=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #32=DEFINITIONAL_REPRESENTATION('DEF1',(#30),#31);
                #33=PCURVE('PC1',#8,#32);
                #34=SEAM_CURVE('SEAM0',#16,(#33,#26),.PCURVE_S1.);
                #35=EDGE_CURVE('E0',#12,#13,#34,.T.);
                #36=ORIENTED_EDGE('OE0',$,$,#35,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        Method mapperForSurface = StepPreviewJsonExporter.class.getDeclaredMethod(
                "mapperForSurface",
                StepEntity.class,
                StepCadBuilder.class
        );
        mapperForSurface.setAccessible(true);
        Object mapper = mapperForSurface.invoke(null, resolved.get(7), builder);
        assertNotNull(mapper);

        Method sampleParametricOrientedEdge = StepPreviewJsonExporter.class.getDeclaredMethod(
                "sampleParametricOrientedEdge",
                StepOrientedEdge.class,
                StepEntity.class,
                Class.forName("com.minicad.app.StepPreviewJsonExporter$ParametricSurfaceMapper"),
                StepCadBuilder.class
        );
        sampleParametricOrientedEdge.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Object> points = (List<Object>) sampleParametricOrientedEdge.invoke(null, resolved.get(36), resolved.get(7), mapper, builder);
        assertNotNull(points);
        assertTrue(points.size() >= 8);

        Method u = points.getFirst().getClass().getDeclaredMethod("u");
        Method v = points.getFirst().getClass().getDeclaredMethod("v");
        double startU = (double) u.invoke(points.getFirst());
        double startV = (double) v.invoke(points.getFirst());
        double endU = (double) u.invoke(points.getLast());
        double endV = (double) v.invoke(points.getLast());

        assertEquals(0.0, startU, 1.0e-9);
        assertEquals(0.0, startV, 1.0e-9);
        assertEquals(0.0, endU, 1.0e-9);
        assertEquals(1.0, endV, 1.0e-9);
    }

    @Test
    void shouldIgnoreConicalPcurveBoundToDifferentSurface() throws Exception {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=DIRECTION('DZ',(0.0,0.0,1.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX0',#1,#3,#4);
                #6=AXIS2_PLACEMENT_3D('AX1',#2,#3,#4);
                #7=CONICAL_SURFACE('CN0',#5,1.0,0.4636476090008061);
                #8=CONICAL_SURFACE('CN1',#6,1.0,0.4636476090008061);
                #10=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(2.0,0.0,1.0));
                #12=VERTEX_POINT('V0',#10);
                #13=VERTEX_POINT('V1',#11);
                #14=DIRECTION('DU',(1.0,0.0,1.0));
                #15=VECTOR('VU',#14,1.0);
                #16=LINE('L0',#10,#15);
                #20=CARTESIAN_POINT('UV0',(0.0,0.0));
                #21=DIRECTION('DV',(0.0,1.0));
                #22=VECTOR('VV',#21,1.0);
                #23=LINE('BAD',#20,#22);
                #24=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #25=DEFINITIONAL_REPRESENTATION('DEF0',(#23),#24);
                #26=PCURVE('PC0',#8,#25);
                #27=SURFACE_CURVE('SC0',#16,(#26),.PCURVE_S1.);
                #28=EDGE_CURVE('E0',#12,#13,#27,.T.);
                #29=ORIENTED_EDGE('OE0',$,$,#28,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        Method mapperForSurface = StepPreviewJsonExporter.class.getDeclaredMethod(
                "mapperForSurface",
                StepEntity.class,
                StepCadBuilder.class
        );
        mapperForSurface.setAccessible(true);
        Object mapper = mapperForSurface.invoke(null, resolved.get(7), builder);
        assertNotNull(mapper);

        Method sampleParametricOrientedEdge = StepPreviewJsonExporter.class.getDeclaredMethod(
                "sampleParametricOrientedEdge",
                StepOrientedEdge.class,
                StepEntity.class,
                Class.forName("com.minicad.app.StepPreviewJsonExporter$ParametricSurfaceMapper"),
                StepCadBuilder.class
        );
        sampleParametricOrientedEdge.setAccessible(true);

        Object points = sampleParametricOrientedEdge.invoke(null, resolved.get(29), resolved.get(7), mapper, builder);
        assertEquals(null, points);
    }

    @Test
    void shouldPickMatchingConicalSeamPcurveWhenOtherTargetsDifferentSurface() throws Exception {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=DIRECTION('DZ',(0.0,0.0,1.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX0',#1,#3,#4);
                #6=AXIS2_PLACEMENT_3D('AX1',#2,#3,#4);
                #7=CONICAL_SURFACE('CN0',#5,1.0,0.4636476090008061);
                #8=CONICAL_SURFACE('CN1',#6,1.0,0.4636476090008061);
                #10=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(2.0,0.0,1.0));
                #12=VERTEX_POINT('V0',#10);
                #13=VERTEX_POINT('V1',#11);
                #14=DIRECTION('DU',(1.0,0.0,1.0));
                #15=VECTOR('VU',#14,1.0);
                #16=LINE('L0',#10,#15);
                #20=CARTESIAN_POINT('UV0',(0.0,0.0));
                #21=DIRECTION('DV0',(0.0,1.0));
                #22=VECTOR('VV0',#21,1.0);
                #23=LINE('GOOD',#20,#22);
                #24=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #25=DEFINITIONAL_REPRESENTATION('DEF0',(#23),#24);
                #26=PCURVE('PC0',#7,#25);
                #27=CARTESIAN_POINT('UV1',(3.141592653589793,0.0));
                #28=DIRECTION('DV1',(0.0,1.0));
                #29=VECTOR('VV1',#28,1.0);
                #30=LINE('BAD',#27,#29);
                #31=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #32=DEFINITIONAL_REPRESENTATION('DEF1',(#30),#31);
                #33=PCURVE('PC1',#8,#32);
                #34=SEAM_CURVE('SEAM0',#16,(#33,#26),.PCURVE_S1.);
                #35=EDGE_CURVE('E0',#12,#13,#34,.T.);
                #36=ORIENTED_EDGE('OE0',$,$,#35,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        Method mapperForSurface = StepPreviewJsonExporter.class.getDeclaredMethod(
                "mapperForSurface",
                StepEntity.class,
                StepCadBuilder.class
        );
        mapperForSurface.setAccessible(true);
        Object mapper = mapperForSurface.invoke(null, resolved.get(7), builder);
        assertNotNull(mapper);

        Method sampleParametricOrientedEdge = StepPreviewJsonExporter.class.getDeclaredMethod(
                "sampleParametricOrientedEdge",
                StepOrientedEdge.class,
                StepEntity.class,
                Class.forName("com.minicad.app.StepPreviewJsonExporter$ParametricSurfaceMapper"),
                StepCadBuilder.class
        );
        sampleParametricOrientedEdge.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Object> points = (List<Object>) sampleParametricOrientedEdge.invoke(null, resolved.get(36), resolved.get(7), mapper, builder);
        assertNotNull(points);
        assertTrue(points.size() >= 8);

        Method u = points.getFirst().getClass().getDeclaredMethod("u");
        Method v = points.getFirst().getClass().getDeclaredMethod("v");
        assertEquals(0.0, (double) u.invoke(points.getFirst()), 1.0e-9);
        assertEquals(0.0, (double) v.invoke(points.getFirst()), 1.0e-9);
        assertEquals(0.0, (double) u.invoke(points.getLast()), 1.0e-9);
        assertEquals(1.0, (double) v.invoke(points.getLast()), 1.0e-9);
    }

    @Test
    void shouldIgnoreToroidalPcurveBoundToDifferentSurface() throws Exception {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=DIRECTION('DZ',(0.0,0.0,1.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX0',#1,#3,#4);
                #6=AXIS2_PLACEMENT_3D('AX1',#2,#3,#4);
                #7=TOROIDAL_SURFACE('TO0',#5,5.0,1.0);
                #8=TOROIDAL_SURFACE('TO1',#6,5.0,1.0);
                #10=CARTESIAN_POINT('P0',(6.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(5.0,0.0,1.0));
                #12=VERTEX_POINT('V0',#10);
                #13=VERTEX_POINT('V1',#11);
                #14=DIRECTION('DU',(-1.0,0.0,1.0));
                #15=VECTOR('VU',#14,1.0);
                #16=LINE('L0',#10,#15);
                #20=CARTESIAN_POINT('UV0',(0.0,0.0));
                #21=DIRECTION('DV',(0.0,1.0));
                #22=VECTOR('VV',#21,1.0);
                #23=LINE('BAD',#20,#22);
                #24=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #25=DEFINITIONAL_REPRESENTATION('DEF0',(#23),#24);
                #26=PCURVE('PC0',#8,#25);
                #27=SURFACE_CURVE('SC0',#16,(#26),.PCURVE_S1.);
                #28=EDGE_CURVE('E0',#12,#13,#27,.T.);
                #29=ORIENTED_EDGE('OE0',$,$,#28,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        Method mapperForSurface = StepPreviewJsonExporter.class.getDeclaredMethod(
                "mapperForSurface",
                StepEntity.class,
                StepCadBuilder.class
        );
        mapperForSurface.setAccessible(true);
        Object mapper = mapperForSurface.invoke(null, resolved.get(7), builder);
        assertNotNull(mapper);

        Method sampleParametricOrientedEdge = StepPreviewJsonExporter.class.getDeclaredMethod(
                "sampleParametricOrientedEdge",
                StepOrientedEdge.class,
                StepEntity.class,
                Class.forName("com.minicad.app.StepPreviewJsonExporter$ParametricSurfaceMapper"),
                StepCadBuilder.class
        );
        sampleParametricOrientedEdge.setAccessible(true);

        Object points = sampleParametricOrientedEdge.invoke(null, resolved.get(29), resolved.get(7), mapper, builder);
        assertEquals(null, points);
    }

    @Test
    void shouldPickMatchingToroidalSeamPcurveWhenOtherTargetsDifferentSurface() throws Exception {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=DIRECTION('DZ',(0.0,0.0,1.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX0',#1,#3,#4);
                #6=AXIS2_PLACEMENT_3D('AX1',#2,#3,#4);
                #7=TOROIDAL_SURFACE('TO0',#5,5.0,1.0);
                #8=TOROIDAL_SURFACE('TO1',#6,5.0,1.0);
                #10=CARTESIAN_POINT('P0',(6.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(5.0,0.0,1.0));
                #12=VERTEX_POINT('V0',#10);
                #13=VERTEX_POINT('V1',#11);
                #14=DIRECTION('DU',(-1.0,0.0,1.0));
                #15=VECTOR('VU',#14,1.0);
                #16=LINE('L0',#10,#15);
                #20=CARTESIAN_POINT('UV0',(0.0,0.0));
                #21=DIRECTION('DV0',(0.0,1.0));
                #22=VECTOR('VV0',#21,1.0);
                #23=LINE('GOOD',#20,#22);
                #24=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #25=DEFINITIONAL_REPRESENTATION('DEF0',(#23),#24);
                #26=PCURVE('PC0',#7,#25);
                #27=CARTESIAN_POINT('UV1',(3.141592653589793,0.0));
                #28=DIRECTION('DV1',(0.0,1.0));
                #29=VECTOR('VV1',#28,1.0);
                #30=LINE('BAD',#27,#29);
                #31=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #32=DEFINITIONAL_REPRESENTATION('DEF1',(#30),#31);
                #33=PCURVE('PC1',#8,#32);
                #34=SEAM_CURVE('SEAM0',#16,(#33,#26),.PCURVE_S1.);
                #35=EDGE_CURVE('E0',#12,#13,#34,.T.);
                #36=ORIENTED_EDGE('OE0',$,$,#35,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        Method mapperForSurface = StepPreviewJsonExporter.class.getDeclaredMethod(
                "mapperForSurface",
                StepEntity.class,
                StepCadBuilder.class
        );
        mapperForSurface.setAccessible(true);
        Object mapper = mapperForSurface.invoke(null, resolved.get(7), builder);
        assertNotNull(mapper);

        Method sampleParametricOrientedEdge = StepPreviewJsonExporter.class.getDeclaredMethod(
                "sampleParametricOrientedEdge",
                StepOrientedEdge.class,
                StepEntity.class,
                Class.forName("com.minicad.app.StepPreviewJsonExporter$ParametricSurfaceMapper"),
                StepCadBuilder.class
        );
        sampleParametricOrientedEdge.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Object> points = (List<Object>) sampleParametricOrientedEdge.invoke(null, resolved.get(36), resolved.get(7), mapper, builder);
        assertNotNull(points);
        assertTrue(points.size() >= 8);

        Method u = points.getFirst().getClass().getDeclaredMethod("u");
        Method v = points.getFirst().getClass().getDeclaredMethod("v");
        assertEquals(0.0, (double) u.invoke(points.getFirst()), 1.0e-9);
        assertEquals(0.0, (double) v.invoke(points.getFirst()), 1.0e-9);
        assertEquals(0.0, (double) u.invoke(points.getLast()), 1.0e-9);
        assertEquals(Math.PI / 2.0, (double) v.invoke(points.getLast()), 1.0e-9);
    }

    @Test
    void shouldExportCylindricalFaceUsingPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #4=CARTESIAN_POINT('B0',(-1.0,0.0,0.0));
                #5=CARTESIAN_POINT('A1',(1.0,0.0,1.0));
                #6=CARTESIAN_POINT('B1',(-1.0,0.0,1.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX0',#1,#10,#11);
                #13=AXIS2_PLACEMENT_3D('AX1',#2,#10,#11);
                #14=CYLINDRICAL_SURFACE('CY0',#12,1.0);
                #20=VERTEX_POINT('V0',#3);
                #21=VERTEX_POINT('V1',#4);
                #22=VERTEX_POINT('V2',#6);
                #23=VERTEX_POINT('V3',#5);
                #30=CIRCLE('C0',#12,1.0);
                #31=CIRCLE('C1',#13,1.0);
                #32=DIRECTION('DU',(1.0,0.0));
                #33=DIRECTION('DV',(0.0,1.0));
                #34=VECTOR('VU',#32,1.0);
                #35=VECTOR('VV',#33,1.0);
                #36=DIRECTION('UP3',(0.0,0.0,1.0));
                #37=VECTOR('VUP3',#36,1.0);
                #40=CARTESIAN_POINT('UV00',(0.0,0.0));
                #41=LINE('UVL0',#40,#34);
                #42=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #43=DEFINITIONAL_REPRESENTATION('DEF0',(#41),#42);
                #44=PCURVE('PC0',#14,#43);
                #45=SURFACE_CURVE('SC0',#30,(#44),.PCURVE_S1.);
                #46=CARTESIAN_POINT('UV10',(0.0,1.0));
                #47=LINE('UVL1',#46,#34);
                #48=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #49=DEFINITIONAL_REPRESENTATION('DEF1',(#47),#48);
                #50=PCURVE('PC1',#14,#49);
                #51=SURFACE_CURVE('SC1',#31,(#50),.PCURVE_S1.);
                #52=CARTESIAN_POINT('UV20',(3.141592653589793,0.0));
                #53=LINE('UVL2',#52,#35);
                #54=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #55=DEFINITIONAL_REPRESENTATION('DEF2',(#53),#54);
                #56=PCURVE('PC2',#14,#55);
                #57=LINE('L0',#4,#37);
                #58=SURFACE_CURVE('SC2',#57,(#56),.PCURVE_S1.);
                #59=CARTESIAN_POINT('UV30',(0.0,0.0));
                #60=LINE('UVL3',#59,#35);
                #61=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #62=DEFINITIONAL_REPRESENTATION('DEF3',(#60),#61);
                #63=PCURVE('PC3',#14,#62);
                #64=CARTESIAN_POINT('UV40',(6.283185307179586,0.0));
                #65=LINE('UVL4',#64,#35);
                #66=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #67=DEFINITIONAL_REPRESENTATION('DEF4',(#65),#66);
                #68=PCURVE('PC4',#14,#67);
                #69=LINE('L1',#3,#37);
                #70=SEAM_CURVE('SEAM0',#69,(#63,#68),.PCURVE_S1.);
                #71=EDGE_CURVE('E0',#20,#21,#45,.T.);
                #72=EDGE_CURVE('E1',#21,#22,#58,.T.);
                #73=EDGE_CURVE('E2',#22,#23,#51,.F.);
                #74=EDGE_CURVE('E3',#23,#20,#70,.F.);
                #80=ORIENTED_EDGE('OE0',$,$,#71,.T.);
                #81=ORIENTED_EDGE('OE1',$,$,#72,.T.);
                #82=ORIENTED_EDGE('OE2',$,$,#73,.T.);
                #83=ORIENTED_EDGE('OE3',$,$,#74,.T.);
                #90=EDGE_LOOP('L0',(#80,#81,#82,#83));
                #91=FACE_OUTER_BOUND('B0',#90,.T.);
                #92=ADVANCED_FACE('F0',(#91),#14,.T.);
                #93=OPEN_SHELL('OS',(#92));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"CYLINDRICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":4"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldExportCylindricalFaceUsingTerminatorWrappedPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #4=CARTESIAN_POINT('B0',(-1.0,0.0,0.0));
                #5=CARTESIAN_POINT('A1',(1.0,0.0,1.0));
                #6=CARTESIAN_POINT('B1',(-1.0,0.0,1.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX0',#1,#10,#11);
                #13=AXIS2_PLACEMENT_3D('AX1',#2,#10,#11);
                #14=CYLINDRICAL_SURFACE('CY0',#12,1.0);
                #20=VERTEX_POINT('V0',#3);
                #21=VERTEX_POINT('V1',#4);
                #22=VERTEX_POINT('V2',#6);
                #23=VERTEX_POINT('V3',#5);
                #30=CIRCLE('C0',#12,1.0);
                #31=CIRCLE('C1',#13,1.0);
                #32=DIRECTION('DU',(1.0,0.0));
                #33=DIRECTION('DV',(0.0,1.0));
                #34=VECTOR('VU',#32,1.0);
                #35=VECTOR('VV',#33,1.0);
                #36=DIRECTION('UP3',(0.0,0.0,1.0));
                #37=VECTOR('VUP3',#36,1.0);
                #40=CARTESIAN_POINT('UV00',(0.0,0.0));
                #41=LINE('UVL0',#40,#34);
                #42=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #43=DEFINITIONAL_REPRESENTATION('DEF0',(#41),#42);
                #44=PCURVE('PC0',#14,#43);
                #45=SURFACE_CURVE('SC0',#30,(#44),.PCURVE_S1.);
                #46=CARTESIAN_POINT('UV10',(0.0,1.0));
                #47=LINE('UVL1',#46,#34);
                #48=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #49=DEFINITIONAL_REPRESENTATION('DEF1',(#47),#48);
                #50=PCURVE('PC1',#14,#49);
                #51=SURFACE_CURVE('SC1',#31,(#50),.PCURVE_S1.);
                #52=CARTESIAN_POINT('UV20',(3.141592653589793,0.0));
                #53=LINE('UVL2',#52,#35);
                #54=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #55=DEFINITIONAL_REPRESENTATION('DEF2',(#53),#54);
                #56=PCURVE('PC2',#14,#55);
                #57=LINE('L0',#4,#37);
                #58=SURFACE_CURVE('SC2',#57,(#56),.PCURVE_S1.);
                #59=CARTESIAN_POINT('UV30',(0.0,0.0));
                #60=LINE('UVL3',#59,#35);
                #61=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #62=DEFINITIONAL_REPRESENTATION('DEF3',(#60),#61);
                #63=PCURVE('PC3',#14,#62);
                #64=CARTESIAN_POINT('UV40',(6.283185307179586,0.0));
                #65=LINE('UVL4',#64,#35);
                #66=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #67=DEFINITIONAL_REPRESENTATION('DEF4',(#65),#66);
                #68=PCURVE('PC4',#14,#67);
                #69=LINE('L1',#3,#37);
                #70=SEAM_CURVE('SEAM0',#69,(#63,#68),.PCURVE_S1.);
                #71=PRESENTATION_STYLE_ASSIGNMENT(());
                #72=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #73=DIRECTION('SD0',(1.0,0.0));
                #74=AXIS2_PLACEMENT_2D('MAP',#72,#73);
                #75=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #76=REPRESENTATION('SYMREP',(),#75);
                #77=SYMBOL_REPRESENTATION_MAP(#74,#76);
                #78=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #79=DIRECTION('SD1',(1.0,0.0));
                #80=AXIS2_PLACEMENT_2D('TGT',#78,#79);
                #81=ANNOTATION_SYMBOL('AS0',#77,#80);
                #82=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#71),#45);
                #83=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#71),#70);
                #84=TERMINATOR_SYMBOL('TS0',(#71),#81,#82);
                #85=TERMINATOR_SYMBOL('TS1',(#71),#81,#83);
                #86=EDGE_CURVE('E0',#20,#21,#84,.T.);
                #87=EDGE_CURVE('E1',#21,#22,#58,.T.);
                #88=EDGE_CURVE('E2',#22,#23,#51,.F.);
                #89=EDGE_CURVE('E3',#23,#20,#85,.F.);
                #90=ORIENTED_EDGE('OE0',$,$,#86,.T.);
                #91=ORIENTED_EDGE('OE1',$,$,#87,.T.);
                #92=ORIENTED_EDGE('OE2',$,$,#88,.T.);
                #93=ORIENTED_EDGE('OE3',$,$,#89,.T.);
                #94=EDGE_LOOP('L0',(#90,#91,#92,#93));
                #95=FACE_OUTER_BOUND('B0',#94,.T.);
                #96=ADVANCED_FACE('F0',(#95),#14,.T.);
                #97=OPEN_SHELL('OS',(#96));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"CYLINDRICAL_SURFACE\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":8"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldExportRectangularTrimmedToroidalBandUsingTerminatorWrappedEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P00',(5.707106781186548,0.0,-0.7071067811865475));
                #3=CARTESIAN_POINT('P01',(5.707106781186548,0.0,0.7071067811865475));
                #4=CARTESIAN_POINT('P10',(0.0,5.707106781186548,-0.7071067811865475));
                #5=CARTESIAN_POINT('P11',(0.0,5.707106781186548,0.7071067811865475));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=DIRECTION('DY',(0.0,1.0,0.0));
                #13=DIRECTION('NX',(-1.0,0.0,0.0));
                #14=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #15=TOROIDAL_SURFACE('TO0',#14,5.0,1.0);
                #16=RECTANGULAR_TRIMMED_SURFACE('RT0',#15,0.0,1.5707963267948966,-0.7853981633974483,0.7853981633974483,.T.,.T.);
                #17=CARTESIAN_POINT('CL',(0.0,0.0,-0.7071067811865475));
                #18=CARTESIAN_POINT('CU',(0.0,0.0,0.7071067811865475));
                #19=CARTESIAN_POINT('CR',(5.0,0.0,0.0));
                #20=CARTESIAN_POINT('CT',(0.0,5.0,0.0));
                #21=AXIS2_PLACEMENT_3D('AL',#17,#10,#11);
                #22=AXIS2_PLACEMENT_3D('AU',#18,#10,#11);
                #23=AXIS2_PLACEMENT_3D('AR',#19,#12,#11);
                #24=AXIS2_PLACEMENT_3D('AT',#20,#13,#12);
                #25=CIRCLE('C0',#21,5.707106781186548);
                #26=CIRCLE('C1',#24,1.0);
                #27=CIRCLE('C2',#22,5.707106781186548);
                #28=CIRCLE('C3',#23,1.0);
                #29=PRESENTATION_STYLE_ASSIGNMENT(());
                #30=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #31=DIRECTION('SD0',(1.0,0.0));
                #32=AXIS2_PLACEMENT_2D('MAP',#30,#31);
                #33=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #34=REPRESENTATION('SYMREP',(),#33);
                #35=SYMBOL_REPRESENTATION_MAP(#32,#34);
                #36=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #37=DIRECTION('SD1',(1.0,0.0));
                #38=AXIS2_PLACEMENT_2D('TGT',#36,#37);
                #39=ANNOTATION_SYMBOL('AS0',#35,#38);
                #40=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#29),#25);
                #41=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#29),#28);
                #42=TERMINATOR_SYMBOL('TS0',(#29),#39,#40);
                #43=TERMINATOR_SYMBOL('TS1',(#29),#39,#41);
                #50=VERTEX_POINT('V0',#2);
                #51=VERTEX_POINT('V1',#4);
                #52=VERTEX_POINT('V2',#5);
                #53=VERTEX_POINT('V3',#3);
                #60=EDGE_CURVE('E0',#50,#51,#42,.T.);
                #61=EDGE_CURVE('E1',#51,#52,#26,.T.);
                #62=EDGE_CURVE('E2',#53,#52,#27,.T.);
                #63=EDGE_CURVE('E3',#50,#53,#43,.T.);
                #70=ORIENTED_EDGE('OE0',$,$,#60,.T.);
                #71=ORIENTED_EDGE('OE1',$,$,#61,.T.);
                #72=ORIENTED_EDGE('OE2',$,$,#62,.F.);
                #73=ORIENTED_EDGE('OE3',$,$,#63,.F.);
                #80=EDGE_LOOP('L0',(#70,#71,#72,#73));
                #81=FACE_OUTER_BOUND('B0',#80,.T.);
                #90=ADVANCED_FACE('F0',(#81),#16,.T.);
                #91=OPEN_SHELL('OS',(#90));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"RECTANGULAR_TRIMMED_SURFACE\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":6"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldExportCurveBoundedToroidalBandUsingTerminatorWrappedEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P00',(5.707106781186548,0.0,-0.7071067811865475));
                #3=CARTESIAN_POINT('P01',(5.707106781186548,0.0,0.7071067811865475));
                #4=CARTESIAN_POINT('P10',(0.0,5.707106781186548,-0.7071067811865475));
                #5=CARTESIAN_POINT('P11',(0.0,5.707106781186548,0.7071067811865475));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=DIRECTION('DY',(0.0,1.0,0.0));
                #13=DIRECTION('NX',(-1.0,0.0,0.0));
                #14=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #15=TOROIDAL_SURFACE('TO0',#14,5.0,1.0);
                #16=CARTESIAN_POINT('CL',(0.0,0.0,-0.7071067811865475));
                #17=CARTESIAN_POINT('CU',(0.0,0.0,0.7071067811865475));
                #18=CARTESIAN_POINT('CR',(5.0,0.0,0.0));
                #19=CARTESIAN_POINT('CT',(0.0,5.0,0.0));
                #20=AXIS2_PLACEMENT_3D('AL',#16,#10,#11);
                #21=AXIS2_PLACEMENT_3D('AU',#17,#10,#11);
                #22=AXIS2_PLACEMENT_3D('AR',#18,#12,#11);
                #23=AXIS2_PLACEMENT_3D('AT',#19,#13,#12);
                #24=CIRCLE('C0',#20,5.707106781186548);
                #25=CIRCLE('C1',#23,1.0);
                #26=CIRCLE('C2',#21,5.707106781186548);
                #27=CIRCLE('C3',#22,1.0);
                #28=CURVE_BOUNDED_SURFACE('CBS0',#15,(#24,#25,#26,#27),.T.);
                #29=PRESENTATION_STYLE_ASSIGNMENT(());
                #30=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #31=DIRECTION('SD0',(1.0,0.0));
                #32=AXIS2_PLACEMENT_2D('MAP',#30,#31);
                #33=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #34=REPRESENTATION('SYMREP',(),#33);
                #35=SYMBOL_REPRESENTATION_MAP(#32,#34);
                #36=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #37=DIRECTION('SD1',(1.0,0.0));
                #38=AXIS2_PLACEMENT_2D('TGT',#36,#37);
                #39=ANNOTATION_SYMBOL('AS0',#35,#38);
                #40=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#29),#24);
                #41=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#29),#27);
                #42=TERMINATOR_SYMBOL('TS0',(#29),#39,#40);
                #43=TERMINATOR_SYMBOL('TS1',(#29),#39,#41);
                #50=VERTEX_POINT('V0',#2);
                #51=VERTEX_POINT('V1',#4);
                #52=VERTEX_POINT('V2',#5);
                #53=VERTEX_POINT('V3',#3);
                #60=EDGE_CURVE('E0',#50,#51,#42,.T.);
                #61=EDGE_CURVE('E1',#51,#52,#25,.T.);
                #62=EDGE_CURVE('E2',#53,#52,#26,.T.);
                #63=EDGE_CURVE('E3',#50,#53,#43,.T.);
                #70=ORIENTED_EDGE('OE0',$,$,#60,.T.);
                #71=ORIENTED_EDGE('OE1',$,$,#61,.T.);
                #72=ORIENTED_EDGE('OE2',$,$,#62,.F.);
                #73=ORIENTED_EDGE('OE3',$,$,#63,.F.);
                #80=EDGE_LOOP('L0',(#70,#71,#72,#73));
                #81=FACE_OUTER_BOUND('B0',#80,.T.);
                #90=ADVANCED_FACE('F0',(#81),#28,.T.);
                #91=OPEN_SHELL('OS',(#90));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"CURVE_BOUNDED_SURFACE\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":6"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldExportOrientedConicalFaceUsingTerminatorWrappedPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('B0',(-1.0,0.0,0.0));
                #4=CARTESIAN_POINT('A1',(1.5,0.0,1.0));
                #5=CARTESIAN_POINT('B1',(-1.5,0.0,1.0));
                #20=DIRECTION('DZ',(0.0,0.0,1.0));
                #21=DIRECTION('DX',(1.0,0.0,0.0));
                #22=DIRECTION('DU',(1.0,0.0));
                #23=DIRECTION('DV',(0.0,1.0));
                #24=AXIS2_PLACEMENT_3D('AX0',#1,#20,#21);
                #25=AXIS2_PLACEMENT_3D('AX1',#4,#20,#21);
                #28=CONICAL_SURFACE('CN0',#24,1.0,0.4636476090008061);
                #29=ORIENTED_SURFACE('OS0',#28,.T.);
                #30=CIRCLE('C0',#24,1.0);
                #31=CIRCLE('C1',#25,1.5);
                #33=VECTOR('VU',#22,1.0);
                #34=VECTOR('VV',#23,1.0);
                #35=DIRECTION('GA',(0.5,0.0,1.0));
                #36=DIRECTION('GB',(-0.5,0.0,1.0));
                #39=VECTOR('VGA',#35,1.0);
                #40=VECTOR('VGB',#36,1.0);
                #41=VERTEX_POINT('V0',#2);
                #42=VERTEX_POINT('V1',#3);
                #43=VERTEX_POINT('V2',#5);
                #44=VERTEX_POINT('V3',#4);
                #50=CARTESIAN_POINT('UV00',(0.0,0.0));
                #51=LINE('UVL0',#50,#33);
                #52=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #53=DEFINITIONAL_REPRESENTATION('DEF0',(#51),#52);
                #54=PCURVE('PC0',#28,#53);
                #55=SURFACE_CURVE('SC0',#30,(#54),.PCURVE_S1.);
                #56=CARTESIAN_POINT('UV10',(3.141592653589793,0.0));
                #57=LINE('UVL1',#56,#34);
                #58=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #59=DEFINITIONAL_REPRESENTATION('DEF1',(#57),#58);
                #60=PCURVE('PC1',#28,#59);
                #61=LINE('L0',#3,#40);
                #62=SURFACE_CURVE('SC1',#61,(#60),.PCURVE_S1.);
                #63=CARTESIAN_POINT('UV20',(0.0,1.0));
                #64=LINE('UVL2',#63,#33);
                #65=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #66=DEFINITIONAL_REPRESENTATION('DEF2',(#64),#65);
                #67=PCURVE('PC2',#28,#66);
                #68=SURFACE_CURVE('SC2',#31,(#67),.PCURVE_S1.);
                #69=CARTESIAN_POINT('UV30',(0.0,0.0));
                #70=LINE('UVL3',#69,#34);
                #71=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #72=DEFINITIONAL_REPRESENTATION('DEF3',(#70),#71);
                #73=PCURVE('PC3',#28,#72);
                #74=CARTESIAN_POINT('UV31',(6.283185307179586,0.0));
                #75=LINE('UVL4',#74,#34);
                #76=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #77=DEFINITIONAL_REPRESENTATION('DEF4',(#75),#76);
                #78=PCURVE('PC4',#28,#77);
                #79=LINE('L1',#2,#39);
                #80=SEAM_CURVE('SEAM0',#79,(#73,#78),.PCURVE_S1.);
                #81=PRESENTATION_STYLE_ASSIGNMENT(());
                #82=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #83=DIRECTION('SD0',(1.0,0.0));
                #84=AXIS2_PLACEMENT_2D('MAP',#82,#83);
                #85=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #86=REPRESENTATION('SYMREP',(),#85);
                #87=SYMBOL_REPRESENTATION_MAP(#84,#86);
                #88=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #89=DIRECTION('SD1',(1.0,0.0));
                #90=AXIS2_PLACEMENT_2D('TGT',#88,#89);
                #91=ANNOTATION_SYMBOL('AS0',#87,#90);
                #92=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#81),#55);
                #93=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#81),#80);
                #94=TERMINATOR_SYMBOL('TS0',(#81),#91,#92);
                #95=TERMINATOR_SYMBOL('TS1',(#81),#91,#93);
                #96=EDGE_CURVE('E0',#41,#42,#94,.T.);
                #97=EDGE_CURVE('E1',#42,#43,#62,.T.);
                #98=EDGE_CURVE('E2',#43,#44,#68,.F.);
                #99=EDGE_CURVE('E3',#44,#41,#95,.F.);
                #100=ORIENTED_EDGE('OE0',$,$,#96,.T.);
                #101=ORIENTED_EDGE('OE1',$,$,#97,.T.);
                #102=ORIENTED_EDGE('OE2',$,$,#98,.T.);
                #103=ORIENTED_EDGE('OE3',$,$,#99,.T.);
                #104=EDGE_LOOP('LOUT',(#100,#101,#102,#103));
                #105=FACE_OUTER_BOUND('BOUT',#104,.T.);
                #106=ADVANCED_FACE('F0',(#105),#29,.T.);
                #107=OPEN_SHELL('OS',(#106));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "ORIENTED_SURFACE", 6, true);
    }

    @Test
    void shouldExportOffsetCylindricalFaceUsingTerminatorWrappedPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=CARTESIAN_POINT('A0',(2.0,0.0,0.0));
                #4=CARTESIAN_POINT('B0',(-2.0,0.0,0.0));
                #5=CARTESIAN_POINT('A1',(2.0,0.0,1.0));
                #6=CARTESIAN_POINT('B1',(-2.0,0.0,1.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX0',#1,#10,#11);
                #13=AXIS2_PLACEMENT_3D('AX1',#2,#10,#11);
                #14=CYLINDRICAL_SURFACE('CY0',#12,1.0);
                #15=OFFSET_SURFACE('OFS0',#14,1.0,.F.);
                #20=VERTEX_POINT('V0',#3);
                #21=VERTEX_POINT('V1',#4);
                #22=VERTEX_POINT('V2',#6);
                #23=VERTEX_POINT('V3',#5);
                #30=CIRCLE('C0',#12,2.0);
                #31=CIRCLE('C1',#13,2.0);
                #32=DIRECTION('DU',(1.0,0.0));
                #33=DIRECTION('DV',(0.0,1.0));
                #34=VECTOR('VU',#32,1.0);
                #35=VECTOR('VV',#33,1.0);
                #36=DIRECTION('UP3',(0.0,0.0,1.0));
                #37=VECTOR('VUP3',#36,1.0);
                #40=CARTESIAN_POINT('UV00',(0.0,0.0));
                #41=LINE('UVL0',#40,#34);
                #42=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #43=DEFINITIONAL_REPRESENTATION('DEF0',(#41),#42);
                #44=PCURVE('PC0',#14,#43);
                #45=SURFACE_CURVE('SC0',#30,(#44),.PCURVE_S1.);
                #46=CARTESIAN_POINT('UV10',(0.0,1.0));
                #47=LINE('UVL1',#46,#34);
                #48=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #49=DEFINITIONAL_REPRESENTATION('DEF1',(#47),#48);
                #50=PCURVE('PC1',#14,#49);
                #51=SURFACE_CURVE('SC1',#31,(#50),.PCURVE_S1.);
                #52=CARTESIAN_POINT('UV20',(3.141592653589793,0.0));
                #53=LINE('UVL2',#52,#35);
                #54=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #55=DEFINITIONAL_REPRESENTATION('DEF2',(#53),#54);
                #56=PCURVE('PC2',#14,#55);
                #57=LINE('L0',#4,#37);
                #58=SURFACE_CURVE('SC2',#57,(#56),.PCURVE_S1.);
                #59=CARTESIAN_POINT('UV30',(0.0,0.0));
                #60=LINE('UVL3',#59,#35);
                #61=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #62=DEFINITIONAL_REPRESENTATION('DEF3',(#60),#61);
                #63=PCURVE('PC3',#14,#62);
                #64=CARTESIAN_POINT('UV40',(6.283185307179586,0.0));
                #65=LINE('UVL4',#64,#35);
                #66=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #67=DEFINITIONAL_REPRESENTATION('DEF4',(#65),#66);
                #68=PCURVE('PC4',#14,#67);
                #69=LINE('L1',#3,#37);
                #70=SEAM_CURVE('SEAM0',#69,(#63,#68),.PCURVE_S1.);
                #71=PRESENTATION_STYLE_ASSIGNMENT(());
                #72=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #73=DIRECTION('SD0',(1.0,0.0));
                #74=AXIS2_PLACEMENT_2D('MAP',#72,#73);
                #75=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #76=REPRESENTATION('SYMREP',(),#75);
                #77=SYMBOL_REPRESENTATION_MAP(#74,#76);
                #78=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #79=DIRECTION('SD1',(1.0,0.0));
                #80=AXIS2_PLACEMENT_2D('TGT',#78,#79);
                #81=ANNOTATION_SYMBOL('AS0',#77,#80);
                #82=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#71),#45);
                #83=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#71),#70);
                #84=TERMINATOR_SYMBOL('TS0',(#71),#81,#82);
                #85=TERMINATOR_SYMBOL('TS1',(#71),#81,#83);
                #86=EDGE_CURVE('E0',#20,#21,#84,.T.);
                #87=EDGE_CURVE('E1',#21,#22,#58,.T.);
                #88=EDGE_CURVE('E2',#22,#23,#51,.F.);
                #89=EDGE_CURVE('E3',#23,#20,#85,.F.);
                #90=ORIENTED_EDGE('OE0',$,$,#86,.T.);
                #91=ORIENTED_EDGE('OE1',$,$,#87,.T.);
                #92=ORIENTED_EDGE('OE2',$,$,#88,.T.);
                #93=ORIENTED_EDGE('OE3',$,$,#89,.T.);
                #94=EDGE_LOOP('L0',(#90,#91,#92,#93));
                #95=FACE_OUTER_BOUND('B0',#94,.T.);
                #96=ADVANCED_FACE('F0',(#95),#15,.T.);
                #97=OPEN_SHELL('OS',(#96));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"OFFSET_SURFACE\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":6"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldExportReplicaCylindricalFaceUsingTerminatorWrappedReplicaPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #4=CARTESIAN_POINT('B0',(-1.0,0.0,0.0));
                #5=CARTESIAN_POINT('A1',(1.0,0.0,1.0));
                #6=CARTESIAN_POINT('B1',(-1.0,0.0,1.0));
                #7=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX0',#1,#10,#11);
                #13=AXIS2_PLACEMENT_3D('AX1',#2,#10,#11);
                #14=CYLINDRICAL_SURFACE('CY0',#12,1.0);
                #15=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#7,1.0,$);
                #16=SURFACE_REPLICA('SR0',#14,#15);
                #20=CARTESIAN_POINT('TA0',(1.0,0.0,2.0));
                #21=CARTESIAN_POINT('TB0',(-1.0,0.0,2.0));
                #22=CARTESIAN_POINT('TB1',(-1.0,0.0,3.0));
                #23=CARTESIAN_POINT('TA1',(1.0,0.0,3.0));
                #24=VERTEX_POINT('V0',#20);
                #25=VERTEX_POINT('V1',#21);
                #26=VERTEX_POINT('V2',#22);
                #27=VERTEX_POINT('V3',#23);
                #30=CIRCLE('C0',#12,1.0);
                #31=CIRCLE('C1',#13,1.0);
                #32=DIRECTION('DU',(1.0,0.0));
                #33=DIRECTION('DV',(0.0,1.0));
                #34=VECTOR('VU',#32,1.0);
                #35=VECTOR('VV',#33,1.0);
                #36=DIRECTION('UP3',(0.0,0.0,1.0));
                #37=VECTOR('VUP3',#36,1.0);
                #40=CARTESIAN_POINT('UV00',(0.0,0.0));
                #41=LINE('UVL0',#40,#34);
                #42=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #43=DEFINITIONAL_REPRESENTATION('DEF0',(#41),#42);
                #44=PCURVE('PC0',#14,#43);
                #45=SURFACE_CURVE('SC0',#30,(#44),.PCURVE_S1.);
                #46=CARTESIAN_POINT('UV10',(0.0,1.0));
                #47=LINE('UVL1',#46,#34);
                #48=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #49=DEFINITIONAL_REPRESENTATION('DEF1',(#47),#48);
                #50=PCURVE('PC1',#14,#49);
                #51=SURFACE_CURVE('SC1',#31,(#50),.PCURVE_S1.);
                #52=CARTESIAN_POINT('UV20',(3.141592653589793,0.0));
                #53=LINE('UVL2',#52,#35);
                #54=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #55=DEFINITIONAL_REPRESENTATION('DEF2',(#53),#54);
                #56=PCURVE('PC2',#14,#55);
                #57=LINE('L0',#4,#37);
                #58=SURFACE_CURVE('SC2',#57,(#56),.PCURVE_S1.);
                #59=CARTESIAN_POINT('UV30',(0.0,0.0));
                #60=LINE('UVL3',#59,#35);
                #61=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #62=DEFINITIONAL_REPRESENTATION('DEF3',(#60),#61);
                #63=PCURVE('PC3',#14,#62);
                #64=CARTESIAN_POINT('UV40',(6.283185307179586,0.0));
                #65=LINE('UVL4',#64,#35);
                #66=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #67=DEFINITIONAL_REPRESENTATION('DEF4',(#65),#66);
                #68=PCURVE('PC4',#14,#67);
                #69=LINE('L1',#3,#37);
                #70=SEAM_CURVE('SEAM0',#69,(#63,#68),.PCURVE_S1.);
                #71=CURVE_REPLICA('CR0',#45,#15);
                #72=CURVE_REPLICA('CR1',#58,#15);
                #73=CURVE_REPLICA('CR2',#51,#15);
                #74=CURVE_REPLICA('CR3',#70,#15);
                #75=PRESENTATION_STYLE_ASSIGNMENT(());
                #76=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #77=DIRECTION('SD0',(1.0,0.0));
                #78=AXIS2_PLACEMENT_2D('MAP',#76,#77);
                #79=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #80=REPRESENTATION('SYMREP',(),#79);
                #81=SYMBOL_REPRESENTATION_MAP(#78,#80);
                #82=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #83=DIRECTION('SD1',(1.0,0.0));
                #84=AXIS2_PLACEMENT_2D('TGT',#82,#83);
                #85=ANNOTATION_SYMBOL('AS0',#81,#84);
                #86=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#75),#71);
                #87=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#75),#74);
                #88=TERMINATOR_SYMBOL('TS0',(#75),#85,#86);
                #89=TERMINATOR_SYMBOL('TS1',(#75),#85,#87);
                #90=EDGE_CURVE('E0',#24,#25,#88,.T.);
                #91=EDGE_CURVE('E1',#25,#26,#72,.T.);
                #92=EDGE_CURVE('E2',#26,#27,#73,.F.);
                #93=EDGE_CURVE('E3',#27,#24,#89,.F.);
                #94=ORIENTED_EDGE('OE0',$,$,#90,.T.);
                #95=ORIENTED_EDGE('OE1',$,$,#91,.T.);
                #96=ORIENTED_EDGE('OE2',$,$,#92,.T.);
                #97=ORIENTED_EDGE('OE3',$,$,#93,.T.);
                #98=EDGE_LOOP('L0',(#94,#95,#96,#97));
                #99=FACE_OUTER_BOUND('B0',#98,.T.);
                #100=ADVANCED_FACE('F0',(#99),#16,.T.);
                #101=OPEN_SHELL('OS',(#100));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "SURFACE_REPLICA", 6, true);
    }

    @Test
    void shouldExportOrientedCylindricalFaceUsingPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #4=CARTESIAN_POINT('B0',(-1.0,0.0,0.0));
                #5=CARTESIAN_POINT('A1',(1.0,0.0,1.0));
                #6=CARTESIAN_POINT('B1',(-1.0,0.0,1.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX0',#1,#10,#11);
                #13=AXIS2_PLACEMENT_3D('AX1',#2,#10,#11);
                #14=CYLINDRICAL_SURFACE('CY0',#12,1.0);
                #15=ORIENTED_SURFACE('OS0',#14,.F.);
                #20=VERTEX_POINT('V0',#3);
                #21=VERTEX_POINT('V1',#4);
                #22=VERTEX_POINT('V2',#6);
                #23=VERTEX_POINT('V3',#5);
                #30=CIRCLE('C0',#12,1.0);
                #31=CIRCLE('C1',#13,1.0);
                #32=DIRECTION('DU',(1.0,0.0));
                #33=DIRECTION('DV',(0.0,1.0));
                #34=VECTOR('VU',#32,1.0);
                #35=VECTOR('VV',#33,1.0);
                #36=DIRECTION('UP3',(0.0,0.0,1.0));
                #37=VECTOR('VUP3',#36,1.0);
                #40=CARTESIAN_POINT('UV00',(0.0,0.0));
                #41=LINE('UVL0',#40,#34);
                #42=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #43=DEFINITIONAL_REPRESENTATION('DEF0',(#41),#42);
                #44=PCURVE('PC0',#14,#43);
                #45=SURFACE_CURVE('SC0',#30,(#44),.PCURVE_S1.);
                #46=CARTESIAN_POINT('UV10',(0.0,1.0));
                #47=LINE('UVL1',#46,#34);
                #48=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #49=DEFINITIONAL_REPRESENTATION('DEF1',(#47),#48);
                #50=PCURVE('PC1',#14,#49);
                #51=SURFACE_CURVE('SC1',#31,(#50),.PCURVE_S1.);
                #52=CARTESIAN_POINT('UV20',(3.141592653589793,0.0));
                #53=LINE('UVL2',#52,#35);
                #54=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #55=DEFINITIONAL_REPRESENTATION('DEF2',(#53),#54);
                #56=PCURVE('PC2',#14,#55);
                #57=LINE('L0',#4,#37);
                #58=SURFACE_CURVE('SC2',#57,(#56),.PCURVE_S1.);
                #59=CARTESIAN_POINT('UV30',(0.0,0.0));
                #60=LINE('UVL3',#59,#35);
                #61=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #62=DEFINITIONAL_REPRESENTATION('DEF3',(#60),#61);
                #63=PCURVE('PC3',#14,#62);
                #64=CARTESIAN_POINT('UV40',(6.283185307179586,0.0));
                #65=LINE('UVL4',#64,#35);
                #66=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #67=DEFINITIONAL_REPRESENTATION('DEF4',(#65),#66);
                #68=PCURVE('PC4',#14,#67);
                #69=LINE('L1',#3,#37);
                #70=SEAM_CURVE('SEAM0',#69,(#63,#68),.PCURVE_S1.);
                #71=EDGE_CURVE('E0',#20,#21,#45,.T.);
                #72=EDGE_CURVE('E1',#21,#22,#58,.T.);
                #73=EDGE_CURVE('E2',#22,#23,#51,.F.);
                #74=EDGE_CURVE('E3',#23,#20,#70,.F.);
                #80=ORIENTED_EDGE('OE0',$,$,#71,.T.);
                #81=ORIENTED_EDGE('OE1',$,$,#72,.T.);
                #82=ORIENTED_EDGE('OE2',$,$,#73,.T.);
                #83=ORIENTED_EDGE('OE3',$,$,#74,.T.);
                #90=EDGE_LOOP('L0',(#80,#81,#82,#83));
                #91=FACE_OUTER_BOUND('B0',#90,.T.);
                #92=ADVANCED_FACE('F0',(#91),#15,.T.);
                #93=OPEN_SHELL('OS',(#92));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "ORIENTED_SURFACE", 4, true);
    }

    @Test
    void shouldExportRectangularTrimmedCylindricalFaceUsingPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #4=CARTESIAN_POINT('B0',(-1.0,0.0,0.0));
                #5=CARTESIAN_POINT('A1',(1.0,0.0,1.0));
                #6=CARTESIAN_POINT('B1',(-1.0,0.0,1.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX0',#1,#10,#11);
                #13=AXIS2_PLACEMENT_3D('AX1',#2,#10,#11);
                #14=CYLINDRICAL_SURFACE('CY0',#12,1.0);
                #15=RECTANGULAR_TRIMMED_SURFACE('RT0',#14,0.0,3.141592653589793,0.0,1.0,.T.,.T.);
                #20=VERTEX_POINT('V0',#3);
                #21=VERTEX_POINT('V1',#4);
                #22=VERTEX_POINT('V2',#6);
                #23=VERTEX_POINT('V3',#5);
                #30=CIRCLE('C0',#12,1.0);
                #31=CIRCLE('C1',#13,1.0);
                #32=DIRECTION('DU',(1.0,0.0));
                #33=DIRECTION('DV',(0.0,1.0));
                #34=VECTOR('VU',#32,1.0);
                #35=VECTOR('VV',#33,1.0);
                #36=DIRECTION('UP3',(0.0,0.0,1.0));
                #37=VECTOR('VUP3',#36,1.0);
                #40=CARTESIAN_POINT('UV00',(0.0,0.0));
                #41=LINE('UVL0',#40,#34);
                #42=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #43=DEFINITIONAL_REPRESENTATION('DEF0',(#41),#42);
                #44=PCURVE('PC0',#14,#43);
                #45=SURFACE_CURVE('SC0',#30,(#44),.PCURVE_S1.);
                #46=CARTESIAN_POINT('UV10',(0.0,1.0));
                #47=LINE('UVL1',#46,#34);
                #48=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #49=DEFINITIONAL_REPRESENTATION('DEF1',(#47),#48);
                #50=PCURVE('PC1',#14,#49);
                #51=SURFACE_CURVE('SC1',#31,(#50),.PCURVE_S1.);
                #52=CARTESIAN_POINT('UV20',(3.141592653589793,0.0));
                #53=LINE('UVL2',#52,#35);
                #54=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #55=DEFINITIONAL_REPRESENTATION('DEF2',(#53),#54);
                #56=PCURVE('PC2',#14,#55);
                #57=LINE('L0',#4,#37);
                #58=SURFACE_CURVE('SC2',#57,(#56),.PCURVE_S1.);
                #59=CARTESIAN_POINT('UV30',(0.0,0.0));
                #60=LINE('UVL3',#59,#35);
                #61=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #62=DEFINITIONAL_REPRESENTATION('DEF3',(#60),#61);
                #63=PCURVE('PC3',#14,#62);
                #64=CARTESIAN_POINT('UV40',(6.283185307179586,0.0));
                #65=LINE('UVL4',#64,#35);
                #66=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #67=DEFINITIONAL_REPRESENTATION('DEF4',(#65),#66);
                #68=PCURVE('PC4',#14,#67);
                #69=LINE('L1',#3,#37);
                #70=SEAM_CURVE('SEAM0',#69,(#63,#68),.PCURVE_S1.);
                #71=EDGE_CURVE('E0',#20,#21,#45,.T.);
                #72=EDGE_CURVE('E1',#21,#22,#58,.T.);
                #73=EDGE_CURVE('E2',#22,#23,#51,.F.);
                #74=EDGE_CURVE('E3',#23,#20,#70,.F.);
                #80=ORIENTED_EDGE('OE0',$,$,#71,.T.);
                #81=ORIENTED_EDGE('OE1',$,$,#72,.T.);
                #82=ORIENTED_EDGE('OE2',$,$,#73,.T.);
                #83=ORIENTED_EDGE('OE3',$,$,#74,.T.);
                #90=EDGE_LOOP('L0',(#80,#81,#82,#83));
                #91=FACE_OUTER_BOUND('B0',#90,.T.);
                #92=ADVANCED_FACE('F0',(#91),#15,.T.);
                #93=OPEN_SHELL('OS',(#92));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "RECTANGULAR_TRIMMED_SURFACE", 4, true);
    }

    @Test
    void shouldExportCurveBoundedCylindricalFaceUsingPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #4=CARTESIAN_POINT('B0',(-1.0,0.0,0.0));
                #5=CARTESIAN_POINT('A1',(1.0,0.0,1.0));
                #6=CARTESIAN_POINT('B1',(-1.0,0.0,1.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX0',#1,#10,#11);
                #13=AXIS2_PLACEMENT_3D('AX1',#2,#10,#11);
                #14=CYLINDRICAL_SURFACE('CY0',#12,1.0);
                #20=VERTEX_POINT('V0',#3);
                #21=VERTEX_POINT('V1',#4);
                #22=VERTEX_POINT('V2',#6);
                #23=VERTEX_POINT('V3',#5);
                #30=CIRCLE('C0',#12,1.0);
                #31=CIRCLE('C1',#13,1.0);
                #32=DIRECTION('DU',(1.0,0.0));
                #33=DIRECTION('DV',(0.0,1.0));
                #34=VECTOR('VU',#32,1.0);
                #35=VECTOR('VV',#33,1.0);
                #36=DIRECTION('UP3',(0.0,0.0,1.0));
                #37=VECTOR('VUP3',#36,1.0);
                #40=CARTESIAN_POINT('UV00',(0.0,0.0));
                #41=LINE('UVL0',#40,#34);
                #42=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #43=DEFINITIONAL_REPRESENTATION('DEF0',(#41),#42);
                #44=PCURVE('PC0',#14,#43);
                #45=SURFACE_CURVE('SC0',#30,(#44),.PCURVE_S1.);
                #46=CARTESIAN_POINT('UV10',(0.0,1.0));
                #47=LINE('UVL1',#46,#34);
                #48=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #49=DEFINITIONAL_REPRESENTATION('DEF1',(#47),#48);
                #50=PCURVE('PC1',#14,#49);
                #51=SURFACE_CURVE('SC1',#31,(#50),.PCURVE_S1.);
                #52=CARTESIAN_POINT('UV20',(3.141592653589793,0.0));
                #53=LINE('UVL2',#52,#35);
                #54=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #55=DEFINITIONAL_REPRESENTATION('DEF2',(#53),#54);
                #56=PCURVE('PC2',#14,#55);
                #57=LINE('L0',#4,#37);
                #58=SURFACE_CURVE('SC2',#57,(#56),.PCURVE_S1.);
                #59=CARTESIAN_POINT('UV30',(0.0,0.0));
                #60=LINE('UVL3',#59,#35);
                #61=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #62=DEFINITIONAL_REPRESENTATION('DEF3',(#60),#61);
                #63=PCURVE('PC3',#14,#62);
                #64=CARTESIAN_POINT('UV40',(6.283185307179586,0.0));
                #65=LINE('UVL4',#64,#35);
                #66=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #67=DEFINITIONAL_REPRESENTATION('DEF4',(#65),#66);
                #68=PCURVE('PC4',#14,#67);
                #69=LINE('L1',#3,#37);
                #70=SEAM_CURVE('SEAM0',#69,(#63,#68),.PCURVE_S1.);
                #75=CURVE_BOUNDED_SURFACE('CBS0',#14,(#45,#58,#51,#70),.T.);
                #71=EDGE_CURVE('E0',#20,#21,#45,.T.);
                #72=EDGE_CURVE('E1',#21,#22,#58,.T.);
                #73=EDGE_CURVE('E2',#22,#23,#51,.F.);
                #74=EDGE_CURVE('E3',#23,#20,#70,.F.);
                #80=ORIENTED_EDGE('OE0',$,$,#71,.T.);
                #81=ORIENTED_EDGE('OE1',$,$,#72,.T.);
                #82=ORIENTED_EDGE('OE2',$,$,#73,.T.);
                #83=ORIENTED_EDGE('OE3',$,$,#74,.T.);
                #90=EDGE_LOOP('L0',(#80,#81,#82,#83));
                #91=FACE_OUTER_BOUND('B0',#90,.T.);
                #92=ADVANCED_FACE('F0',(#91),#75,.T.);
                #93=OPEN_SHELL('OS',(#92));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "CURVE_BOUNDED_SURFACE", 4, true);
    }

    @Test
    void shouldExportOffsetCylindricalFaceUsingPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=CARTESIAN_POINT('A0',(2.0,0.0,0.0));
                #4=CARTESIAN_POINT('B0',(-2.0,0.0,0.0));
                #5=CARTESIAN_POINT('A1',(2.0,0.0,1.0));
                #6=CARTESIAN_POINT('B1',(-2.0,0.0,1.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX0',#1,#10,#11);
                #13=AXIS2_PLACEMENT_3D('AX1',#2,#10,#11);
                #14=CYLINDRICAL_SURFACE('CY0',#12,1.0);
                #15=OFFSET_SURFACE('OFS0',#14,1.0,.F.);
                #20=VERTEX_POINT('V0',#3);
                #21=VERTEX_POINT('V1',#4);
                #22=VERTEX_POINT('V2',#6);
                #23=VERTEX_POINT('V3',#5);
                #30=CIRCLE('C0',#12,2.0);
                #31=CIRCLE('C1',#13,2.0);
                #32=DIRECTION('DU',(1.0,0.0));
                #33=DIRECTION('DV',(0.0,1.0));
                #34=VECTOR('VU',#32,1.0);
                #35=VECTOR('VV',#33,1.0);
                #36=DIRECTION('UP3',(0.0,0.0,1.0));
                #37=VECTOR('VUP3',#36,1.0);
                #40=CARTESIAN_POINT('UV00',(0.0,0.0));
                #41=LINE('UVL0',#40,#34);
                #42=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #43=DEFINITIONAL_REPRESENTATION('DEF0',(#41),#42);
                #44=PCURVE('PC0',#14,#43);
                #45=SURFACE_CURVE('SC0',#30,(#44),.PCURVE_S1.);
                #46=CARTESIAN_POINT('UV10',(0.0,1.0));
                #47=LINE('UVL1',#46,#34);
                #48=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #49=DEFINITIONAL_REPRESENTATION('DEF1',(#47),#48);
                #50=PCURVE('PC1',#14,#49);
                #51=SURFACE_CURVE('SC1',#31,(#50),.PCURVE_S1.);
                #52=CARTESIAN_POINT('UV20',(3.141592653589793,0.0));
                #53=LINE('UVL2',#52,#35);
                #54=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #55=DEFINITIONAL_REPRESENTATION('DEF2',(#53),#54);
                #56=PCURVE('PC2',#14,#55);
                #57=LINE('L0',#4,#37);
                #58=SURFACE_CURVE('SC2',#57,(#56),.PCURVE_S1.);
                #59=CARTESIAN_POINT('UV30',(0.0,0.0));
                #60=LINE('UVL3',#59,#35);
                #61=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #62=DEFINITIONAL_REPRESENTATION('DEF3',(#60),#61);
                #63=PCURVE('PC3',#14,#62);
                #64=CARTESIAN_POINT('UV40',(6.283185307179586,0.0));
                #65=LINE('UVL4',#64,#35);
                #66=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #67=DEFINITIONAL_REPRESENTATION('DEF4',(#65),#66);
                #68=PCURVE('PC4',#14,#67);
                #69=LINE('L1',#3,#37);
                #70=SEAM_CURVE('SEAM0',#69,(#63,#68),.PCURVE_S1.);
                #71=EDGE_CURVE('E0',#20,#21,#45,.T.);
                #72=EDGE_CURVE('E1',#21,#22,#58,.T.);
                #73=EDGE_CURVE('E2',#22,#23,#51,.F.);
                #74=EDGE_CURVE('E3',#23,#20,#70,.F.);
                #80=ORIENTED_EDGE('OE0',$,$,#71,.T.);
                #81=ORIENTED_EDGE('OE1',$,$,#72,.T.);
                #82=ORIENTED_EDGE('OE2',$,$,#73,.T.);
                #83=ORIENTED_EDGE('OE3',$,$,#74,.T.);
                #90=EDGE_LOOP('L0',(#80,#81,#82,#83));
                #91=FACE_OUTER_BOUND('B0',#90,.T.);
                #92=ADVANCED_FACE('F0',(#91),#15,.T.);
                #93=OPEN_SHELL('OS',(#92));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "OFFSET_SURFACE", 4, true);
    }

    @Test
    void shouldExportReplicaCylindricalFaceUsingReplicaPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #4=CARTESIAN_POINT('B0',(-1.0,0.0,0.0));
                #5=CARTESIAN_POINT('A1',(1.0,0.0,1.0));
                #6=CARTESIAN_POINT('B1',(-1.0,0.0,1.0));
                #7=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX0',#1,#10,#11);
                #13=AXIS2_PLACEMENT_3D('AX1',#2,#10,#11);
                #14=CYLINDRICAL_SURFACE('CY0',#12,1.0);
                #15=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#7,1.0,$);
                #16=SURFACE_REPLICA('SR0',#14,#15);
                #20=CARTESIAN_POINT('TA0',(1.0,0.0,2.0));
                #21=CARTESIAN_POINT('TB0',(-1.0,0.0,2.0));
                #22=CARTESIAN_POINT('TB1',(-1.0,0.0,3.0));
                #23=CARTESIAN_POINT('TA1',(1.0,0.0,3.0));
                #24=VERTEX_POINT('V0',#20);
                #25=VERTEX_POINT('V1',#21);
                #26=VERTEX_POINT('V2',#22);
                #27=VERTEX_POINT('V3',#23);
                #30=CIRCLE('C0',#12,1.0);
                #31=CIRCLE('C1',#13,1.0);
                #32=DIRECTION('DU',(1.0,0.0));
                #33=DIRECTION('DV',(0.0,1.0));
                #34=VECTOR('VU',#32,1.0);
                #35=VECTOR('VV',#33,1.0);
                #36=DIRECTION('UP3',(0.0,0.0,1.0));
                #37=VECTOR('VUP3',#36,1.0);
                #40=CARTESIAN_POINT('UV00',(0.0,0.0));
                #41=LINE('UVL0',#40,#34);
                #42=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #43=DEFINITIONAL_REPRESENTATION('DEF0',(#41),#42);
                #44=PCURVE('PC0',#14,#43);
                #45=SURFACE_CURVE('SC0',#30,(#44),.PCURVE_S1.);
                #46=CARTESIAN_POINT('UV10',(0.0,1.0));
                #47=LINE('UVL1',#46,#34);
                #48=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #49=DEFINITIONAL_REPRESENTATION('DEF1',(#47),#48);
                #50=PCURVE('PC1',#14,#49);
                #51=SURFACE_CURVE('SC1',#31,(#50),.PCURVE_S1.);
                #52=CARTESIAN_POINT('UV20',(3.141592653589793,0.0));
                #53=LINE('UVL2',#52,#35);
                #54=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #55=DEFINITIONAL_REPRESENTATION('DEF2',(#53),#54);
                #56=PCURVE('PC2',#14,#55);
                #57=LINE('L0',#4,#37);
                #58=SURFACE_CURVE('SC2',#57,(#56),.PCURVE_S1.);
                #59=CARTESIAN_POINT('UV30',(0.0,0.0));
                #60=LINE('UVL3',#59,#35);
                #61=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #62=DEFINITIONAL_REPRESENTATION('DEF3',(#60),#61);
                #63=PCURVE('PC3',#14,#62);
                #64=CARTESIAN_POINT('UV40',(6.283185307179586,0.0));
                #65=LINE('UVL4',#64,#35);
                #66=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #67=DEFINITIONAL_REPRESENTATION('DEF4',(#65),#66);
                #68=PCURVE('PC4',#14,#67);
                #69=LINE('L1',#3,#37);
                #70=SEAM_CURVE('SEAM0',#69,(#63,#68),.PCURVE_S1.);
                #71=CURVE_REPLICA('CR0',#45,#15);
                #72=CURVE_REPLICA('CR1',#58,#15);
                #73=CURVE_REPLICA('CR2',#51,#15);
                #74=CURVE_REPLICA('CR3',#70,#15);
                #80=EDGE_CURVE('E0',#24,#25,#71,.T.);
                #81=EDGE_CURVE('E1',#25,#26,#72,.T.);
                #82=EDGE_CURVE('E2',#26,#27,#73,.F.);
                #83=EDGE_CURVE('E3',#27,#24,#74,.F.);
                #90=ORIENTED_EDGE('OE0',$,$,#80,.T.);
                #91=ORIENTED_EDGE('OE1',$,$,#81,.T.);
                #92=ORIENTED_EDGE('OE2',$,$,#82,.T.);
                #93=ORIENTED_EDGE('OE3',$,$,#83,.T.);
                #94=EDGE_LOOP('L0',(#90,#91,#92,#93));
                #95=FACE_OUTER_BOUND('B0',#94,.T.);
                #96=ADVANCED_FACE('F0',(#95),#16,.T.);
                #97=OPEN_SHELL('OS',(#96));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "SURFACE_REPLICA", 4, true);
    }

    @Test
    void shouldExportUniformScaledReplicaCylindricalFaceUsingReplicaPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #4=CARTESIAN_POINT('B0',(-1.0,0.0,0.0));
                #5=CARTESIAN_POINT('A1',(1.0,0.0,1.0));
                #6=CARTESIAN_POINT('B1',(-1.0,0.0,1.0));
                #7=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX0',#1,#10,#11);
                #13=AXIS2_PLACEMENT_3D('AX1',#2,#10,#11);
                #14=CYLINDRICAL_SURFACE('CY0',#12,1.0);
                #15=DIRECTION('SX',(2.0,0.0,0.0));
                #16=DIRECTION('SZ',(0.0,0.0,2.0));
                #17=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',#15,#16,#7,2.0,$);
                #18=SURFACE_REPLICA('SR0',#14,#17);
                #20=CARTESIAN_POINT('TA0',(2.0,0.0,2.0));
                #21=CARTESIAN_POINT('TB0',(-2.0,0.0,2.0));
                #22=CARTESIAN_POINT('TB1',(-2.0,0.0,4.0));
                #23=CARTESIAN_POINT('TA1',(2.0,0.0,4.0));
                #24=VERTEX_POINT('V0',#20);
                #25=VERTEX_POINT('V1',#21);
                #26=VERTEX_POINT('V2',#22);
                #27=VERTEX_POINT('V3',#23);
                #30=CIRCLE('C0',#12,1.0);
                #31=CIRCLE('C1',#13,1.0);
                #32=DIRECTION('DU',(1.0,0.0));
                #33=DIRECTION('DV',(0.0,1.0));
                #34=VECTOR('VU',#32,1.0);
                #35=VECTOR('VV',#33,1.0);
                #36=DIRECTION('UP3',(0.0,0.0,1.0));
                #37=VECTOR('VUP3',#36,1.0);
                #40=CARTESIAN_POINT('UV00',(0.0,0.0));
                #41=LINE('UVL0',#40,#34);
                #42=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #43=DEFINITIONAL_REPRESENTATION('DEF0',(#41),#42);
                #44=PCURVE('PC0',#14,#43);
                #45=SURFACE_CURVE('SC0',#30,(#44),.PCURVE_S1.);
                #46=CARTESIAN_POINT('UV10',(0.0,1.0));
                #47=LINE('UVL1',#46,#34);
                #48=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #49=DEFINITIONAL_REPRESENTATION('DEF1',(#47),#48);
                #50=PCURVE('PC1',#14,#49);
                #51=SURFACE_CURVE('SC1',#31,(#50),.PCURVE_S1.);
                #52=CARTESIAN_POINT('UV20',(3.141592653589793,0.0));
                #53=LINE('UVL2',#52,#35);
                #54=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #55=DEFINITIONAL_REPRESENTATION('DEF2',(#53),#54);
                #56=PCURVE('PC2',#14,#55);
                #57=LINE('L0',#4,#37);
                #58=SURFACE_CURVE('SC2',#57,(#56),.PCURVE_S1.);
                #59=CARTESIAN_POINT('UV30',(0.0,0.0));
                #60=LINE('UVL3',#59,#35);
                #61=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #62=DEFINITIONAL_REPRESENTATION('DEF3',(#60),#61);
                #63=PCURVE('PC3',#14,#62);
                #64=CARTESIAN_POINT('UV40',(6.283185307179586,0.0));
                #65=LINE('UVL4',#64,#35);
                #66=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #67=DEFINITIONAL_REPRESENTATION('DEF4',(#65),#66);
                #68=PCURVE('PC4',#14,#67);
                #69=LINE('L1',#3,#37);
                #70=SEAM_CURVE('SEAM0',#69,(#63,#68),.PCURVE_S1.);
                #71=CURVE_REPLICA('CR0',#45,#17);
                #72=CURVE_REPLICA('CR1',#58,#17);
                #73=CURVE_REPLICA('CR2',#51,#17);
                #74=CURVE_REPLICA('CR3',#70,#17);
                #80=EDGE_CURVE('E0',#24,#25,#71,.T.);
                #81=EDGE_CURVE('E1',#25,#26,#72,.T.);
                #82=EDGE_CURVE('E2',#26,#27,#73,.F.);
                #83=EDGE_CURVE('E3',#27,#24,#74,.F.);
                #90=ORIENTED_EDGE('OE0',$,$,#80,.T.);
                #91=ORIENTED_EDGE('OE1',$,$,#81,.T.);
                #92=ORIENTED_EDGE('OE2',$,$,#82,.T.);
                #93=ORIENTED_EDGE('OE3',$,$,#83,.T.);
                #94=EDGE_LOOP('L0',(#90,#91,#92,#93));
                #95=FACE_OUTER_BOUND('B0',#94,.T.);
                #96=ADVANCED_FACE('F0',(#95),#18,.T.);
                #97=OPEN_SHELL('OS',(#96));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "SURFACE_REPLICA", 4, true);
    }

    @Test
    void shouldExportReplicaConicalFaceUsingReplicaPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('B0',(-1.0,0.0,0.0));
                #4=CARTESIAN_POINT('A1',(1.5,0.0,1.0));
                #5=CARTESIAN_POINT('B1',(-1.5,0.0,1.0));
                #6=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #20=DIRECTION('DZ',(0.0,0.0,1.0));
                #21=DIRECTION('DX',(1.0,0.0,0.0));
                #22=DIRECTION('DU',(1.0,0.0));
                #23=DIRECTION('DV',(0.0,1.0));
                #24=AXIS2_PLACEMENT_3D('AX0',#1,#20,#21);
                #25=AXIS2_PLACEMENT_3D('AX1',#4,#20,#21);
                #28=CONICAL_SURFACE('CN0',#24,1.0,0.4636476090008061);
                #29=CIRCLE('C0',#24,1.0);
                #30=CIRCLE('C1',#25,1.5);
                #33=VECTOR('VU',#22,1.0);
                #34=VECTOR('VV',#23,1.0);
                #35=DIRECTION('GA',(0.5,0.0,1.0));
                #36=DIRECTION('GB',(-0.5,0.0,1.0));
                #39=VECTOR('VGA',#35,1.0);
                #40=VECTOR('VGB',#36,1.0);
                #41=VERTEX_POINT('V0',#2);
                #42=VERTEX_POINT('V1',#3);
                #43=VERTEX_POINT('V2',#5);
                #44=VERTEX_POINT('V3',#4);
                #50=CARTESIAN_POINT('UV00',(0.0,0.0));
                #51=LINE('UVL0',#50,#33);
                #52=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #53=DEFINITIONAL_REPRESENTATION('DEF0',(#51),#52);
                #54=PCURVE('PC0',#28,#53);
                #55=SURFACE_CURVE('SC0',#29,(#54),.PCURVE_S1.);
                #56=CARTESIAN_POINT('UV10',(3.141592653589793,0.0));
                #57=LINE('UVL1',#56,#34);
                #58=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #59=DEFINITIONAL_REPRESENTATION('DEF1',(#57),#58);
                #60=PCURVE('PC1',#28,#59);
                #61=LINE('L0',#3,#40);
                #62=SURFACE_CURVE('SC1',#61,(#60),.PCURVE_S1.);
                #63=CARTESIAN_POINT('UV20',(0.0,1.0));
                #64=LINE('UVL2',#63,#33);
                #65=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #66=DEFINITIONAL_REPRESENTATION('DEF2',(#64),#65);
                #67=PCURVE('PC2',#28,#66);
                #68=SURFACE_CURVE('SC2',#30,(#67),.PCURVE_S1.);
                #69=CARTESIAN_POINT('UV30',(0.0,0.0));
                #70=LINE('UVL3',#69,#34);
                #71=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #72=DEFINITIONAL_REPRESENTATION('DEF3',(#70),#71);
                #73=PCURVE('PC3',#28,#72);
                #74=CARTESIAN_POINT('UV31',(6.283185307179586,0.0));
                #75=LINE('UVL4',#74,#34);
                #76=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #77=DEFINITIONAL_REPRESENTATION('DEF4',(#75),#76);
                #78=PCURVE('PC4',#28,#77);
                #79=LINE('L1',#2,#39);
                #80=SEAM_CURVE('SEAM0',#79,(#73,#78),.PCURVE_S1.);
                #81=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#6,1.0,$);
                #82=SURFACE_REPLICA('SR0',#28,#81);
                #83=CURVE_REPLICA('CR0',#55,#81);
                #84=CURVE_REPLICA('CR1',#62,#81);
                #85=CURVE_REPLICA('CR2',#68,#81);
                #86=CURVE_REPLICA('CR3',#80,#81);
                #87=CARTESIAN_POINT('TA0',(1.0,0.0,2.0));
                #88=CARTESIAN_POINT('TB0',(-1.0,0.0,2.0));
                #89=CARTESIAN_POINT('TB1',(-1.5,0.0,3.0));
                #90=CARTESIAN_POINT('TA1',(1.5,0.0,3.0));
                #91=VERTEX_POINT('TV0',#87);
                #92=VERTEX_POINT('TV1',#88);
                #93=VERTEX_POINT('TV2',#89);
                #94=VERTEX_POINT('TV3',#90);
                #95=EDGE_CURVE('E0',#91,#92,#83,.T.);
                #96=EDGE_CURVE('E1',#92,#93,#84,.T.);
                #97=EDGE_CURVE('E2',#93,#94,#85,.F.);
                #98=EDGE_CURVE('E3',#94,#91,#86,.F.);
                #99=ORIENTED_EDGE('OE0',$,$,#95,.T.);
                #100=ORIENTED_EDGE('OE1',$,$,#96,.T.);
                #101=ORIENTED_EDGE('OE2',$,$,#97,.T.);
                #102=ORIENTED_EDGE('OE3',$,$,#98,.T.);
                #103=EDGE_LOOP('LOUT',(#99,#100,#101,#102));
                #104=FACE_OUTER_BOUND('BOUT',#103,.T.);
                #105=ADVANCED_FACE('F0',(#104),#82,.T.);
                #106=OPEN_SHELL('OS',(#105));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"SURFACE_REPLICA\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":4"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldExportReplicaConicalFaceUsingTerminatorWrappedReplicaPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('B0',(-1.0,0.0,0.0));
                #4=CARTESIAN_POINT('A1',(1.5,0.0,1.0));
                #5=CARTESIAN_POINT('B1',(-1.5,0.0,1.0));
                #6=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #20=DIRECTION('DZ',(0.0,0.0,1.0));
                #21=DIRECTION('DX',(1.0,0.0,0.0));
                #22=DIRECTION('DU',(1.0,0.0));
                #23=DIRECTION('DV',(0.0,1.0));
                #24=AXIS2_PLACEMENT_3D('AX0',#1,#20,#21);
                #25=AXIS2_PLACEMENT_3D('AX1',#4,#20,#21);
                #28=CONICAL_SURFACE('CN0',#24,1.0,0.4636476090008061);
                #29=CIRCLE('C0',#24,1.0);
                #30=CIRCLE('C1',#25,1.5);
                #33=VECTOR('VU',#22,1.0);
                #34=VECTOR('VV',#23,1.0);
                #35=DIRECTION('GA',(0.5,0.0,1.0));
                #36=DIRECTION('GB',(-0.5,0.0,1.0));
                #39=VECTOR('VGA',#35,1.0);
                #40=VECTOR('VGB',#36,1.0);
                #41=VERTEX_POINT('V0',#2);
                #42=VERTEX_POINT('V1',#3);
                #43=VERTEX_POINT('V2',#5);
                #44=VERTEX_POINT('V3',#4);
                #50=CARTESIAN_POINT('UV00',(0.0,0.0));
                #51=LINE('UVL0',#50,#33);
                #52=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #53=DEFINITIONAL_REPRESENTATION('DEF0',(#51),#52);
                #54=PCURVE('PC0',#28,#53);
                #55=SURFACE_CURVE('SC0',#29,(#54),.PCURVE_S1.);
                #56=CARTESIAN_POINT('UV10',(3.141592653589793,0.0));
                #57=LINE('UVL1',#56,#34);
                #58=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #59=DEFINITIONAL_REPRESENTATION('DEF1',(#57),#58);
                #60=PCURVE('PC1',#28,#59);
                #61=LINE('L0',#3,#40);
                #62=SURFACE_CURVE('SC1',#61,(#60),.PCURVE_S1.);
                #63=CARTESIAN_POINT('UV20',(0.0,1.0));
                #64=LINE('UVL2',#63,#33);
                #65=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #66=DEFINITIONAL_REPRESENTATION('DEF2',(#64),#65);
                #67=PCURVE('PC2',#28,#66);
                #68=SURFACE_CURVE('SC2',#30,(#67),.PCURVE_S1.);
                #69=CARTESIAN_POINT('UV30',(0.0,0.0));
                #70=LINE('UVL3',#69,#34);
                #71=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #72=DEFINITIONAL_REPRESENTATION('DEF3',(#70),#71);
                #73=PCURVE('PC3',#28,#72);
                #74=CARTESIAN_POINT('UV31',(6.283185307179586,0.0));
                #75=LINE('UVL4',#74,#34);
                #76=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #77=DEFINITIONAL_REPRESENTATION('DEF4',(#75),#76);
                #78=PCURVE('PC4',#28,#77);
                #79=LINE('L1',#2,#39);
                #80=SEAM_CURVE('SEAM0',#79,(#73,#78),.PCURVE_S1.);
                #81=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#6,1.0,$);
                #82=SURFACE_REPLICA('SR0',#28,#81);
                #83=CURVE_REPLICA('CR0',#55,#81);
                #84=CURVE_REPLICA('CR1',#62,#81);
                #85=CURVE_REPLICA('CR2',#68,#81);
                #86=CURVE_REPLICA('CR3',#80,#81);
                #87=CARTESIAN_POINT('TA0',(1.0,0.0,2.0));
                #88=CARTESIAN_POINT('TB0',(-1.0,0.0,2.0));
                #89=CARTESIAN_POINT('TB1',(-1.5,0.0,3.0));
                #90=CARTESIAN_POINT('TA1',(1.5,0.0,3.0));
                #91=VERTEX_POINT('TV0',#87);
                #92=VERTEX_POINT('TV1',#88);
                #93=VERTEX_POINT('TV2',#89);
                #94=VERTEX_POINT('TV3',#90);
                #95=PRESENTATION_STYLE_ASSIGNMENT(());
                #96=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #97=DIRECTION('SD0',(1.0,0.0));
                #98=AXIS2_PLACEMENT_2D('MAP',#96,#97);
                #99=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #100=REPRESENTATION('SYMREP',(),#99);
                #101=SYMBOL_REPRESENTATION_MAP(#98,#100);
                #102=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #103=DIRECTION('SD1',(1.0,0.0));
                #104=AXIS2_PLACEMENT_2D('TGT',#102,#103);
                #105=ANNOTATION_SYMBOL('AS0',#101,#104);
                #106=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#95),#83);
                #107=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#95),#86);
                #108=TERMINATOR_SYMBOL('TS0',(#95),#105,#106);
                #109=TERMINATOR_SYMBOL('TS1',(#95),#105,#107);
                #110=EDGE_CURVE('E0',#91,#92,#108,.T.);
                #111=EDGE_CURVE('E1',#92,#93,#84,.T.);
                #112=EDGE_CURVE('E2',#93,#94,#85,.F.);
                #113=EDGE_CURVE('E3',#94,#91,#109,.F.);
                #114=ORIENTED_EDGE('OE0',$,$,#110,.T.);
                #115=ORIENTED_EDGE('OE1',$,$,#111,.T.);
                #116=ORIENTED_EDGE('OE2',$,$,#112,.T.);
                #117=ORIENTED_EDGE('OE3',$,$,#113,.T.);
                #118=EDGE_LOOP('LOUT',(#114,#115,#116,#117));
                #119=FACE_OUTER_BOUND('BOUT',#118,.T.);
                #120=ADVANCED_FACE('F0',(#119),#82,.T.);
                #121=OPEN_SHELL('OS',(#120));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "SURFACE_REPLICA", 6, true);
    }

    @Test
    void shouldExportUniformScaledReplicaConicalFaceUsingReplicaPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('B0',(-1.0,0.0,0.0));
                #4=CARTESIAN_POINT('A1',(1.5,0.0,1.0));
                #5=CARTESIAN_POINT('B1',(-1.5,0.0,1.0));
                #6=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #20=DIRECTION('DZ',(0.0,0.0,1.0));
                #21=DIRECTION('DX',(1.0,0.0,0.0));
                #22=DIRECTION('DU',(1.0,0.0));
                #23=DIRECTION('DV',(0.0,1.0));
                #24=AXIS2_PLACEMENT_3D('AX0',#1,#20,#21);
                #25=AXIS2_PLACEMENT_3D('AX1',#4,#20,#21);
                #28=CONICAL_SURFACE('CN0',#24,1.0,0.4636476090008061);
                #29=CIRCLE('C0',#24,1.0);
                #30=CIRCLE('C1',#25,1.5);
                #33=VECTOR('VU',#22,1.0);
                #34=VECTOR('VV',#23,1.0);
                #35=DIRECTION('GA',(0.5,0.0,1.0));
                #36=DIRECTION('GB',(-0.5,0.0,1.0));
                #39=VECTOR('VGA',#35,1.0);
                #40=VECTOR('VGB',#36,1.0);
                #41=VERTEX_POINT('V0',#2);
                #42=VERTEX_POINT('V1',#3);
                #43=VERTEX_POINT('V2',#5);
                #44=VERTEX_POINT('V3',#4);
                #50=CARTESIAN_POINT('UV00',(0.0,0.0));
                #51=LINE('UVL0',#50,#33);
                #52=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #53=DEFINITIONAL_REPRESENTATION('DEF0',(#51),#52);
                #54=PCURVE('PC0',#28,#53);
                #55=SURFACE_CURVE('SC0',#29,(#54),.PCURVE_S1.);
                #56=CARTESIAN_POINT('UV10',(3.141592653589793,0.0));
                #57=LINE('UVL1',#56,#34);
                #58=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #59=DEFINITIONAL_REPRESENTATION('DEF1',(#57),#58);
                #60=PCURVE('PC1',#28,#59);
                #61=LINE('L0',#3,#40);
                #62=SURFACE_CURVE('SC1',#61,(#60),.PCURVE_S1.);
                #63=CARTESIAN_POINT('UV20',(0.0,1.0));
                #64=LINE('UVL2',#63,#33);
                #65=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #66=DEFINITIONAL_REPRESENTATION('DEF2',(#64),#65);
                #67=PCURVE('PC2',#28,#66);
                #68=SURFACE_CURVE('SC2',#30,(#67),.PCURVE_S1.);
                #69=CARTESIAN_POINT('UV30',(0.0,0.0));
                #70=LINE('UVL3',#69,#34);
                #71=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #72=DEFINITIONAL_REPRESENTATION('DEF3',(#70),#71);
                #73=PCURVE('PC3',#28,#72);
                #74=CARTESIAN_POINT('UV31',(6.283185307179586,0.0));
                #75=LINE('UVL4',#74,#34);
                #76=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #77=DEFINITIONAL_REPRESENTATION('DEF4',(#75),#76);
                #78=PCURVE('PC4',#28,#77);
                #79=LINE('L1',#2,#39);
                #80=SEAM_CURVE('SEAM0',#79,(#73,#78),.PCURVE_S1.);
                #81=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#6,2.0,$);
                #82=SURFACE_REPLICA('SR0',#28,#81);
                #83=CURVE_REPLICA('CR0',#55,#81);
                #84=CURVE_REPLICA('CR1',#62,#81);
                #85=CURVE_REPLICA('CR2',#68,#81);
                #86=CURVE_REPLICA('CR3',#80,#81);
                #87=CARTESIAN_POINT('TA0',(2.0,0.0,2.0));
                #88=CARTESIAN_POINT('TB0',(-2.0,0.0,2.0));
                #89=CARTESIAN_POINT('TB1',(-3.0,0.0,4.0));
                #90=CARTESIAN_POINT('TA1',(3.0,0.0,4.0));
                #91=VERTEX_POINT('TV0',#87);
                #92=VERTEX_POINT('TV1',#88);
                #93=VERTEX_POINT('TV2',#89);
                #94=VERTEX_POINT('TV3',#90);
                #95=EDGE_CURVE('E0',#91,#92,#83,.T.);
                #96=EDGE_CURVE('E1',#92,#93,#84,.T.);
                #97=EDGE_CURVE('E2',#93,#94,#85,.F.);
                #98=EDGE_CURVE('E3',#94,#91,#86,.F.);
                #99=ORIENTED_EDGE('OE0',$,$,#95,.T.);
                #100=ORIENTED_EDGE('OE1',$,$,#96,.T.);
                #101=ORIENTED_EDGE('OE2',$,$,#97,.T.);
                #102=ORIENTED_EDGE('OE3',$,$,#98,.T.);
                #103=EDGE_LOOP('L0',(#99,#100,#101,#102));
                #104=FACE_OUTER_BOUND('B0',#103,.T.);
                #105=ADVANCED_FACE('F0',(#104),#82,.T.);
                #106=OPEN_SHELL('OS',(#105));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "SURFACE_REPLICA", 4, true);
    }

    @Test
    void shouldExportOffsetConicalFaceUsingPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('A0',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('B0',(-2.0,0.0,0.0));
                #4=CARTESIAN_POINT('A1',(2.5,0.0,1.0));
                #5=CARTESIAN_POINT('B1',(-2.5,0.0,1.0));
                #20=DIRECTION('DZ',(0.0,0.0,1.0));
                #21=DIRECTION('DX',(1.0,0.0,0.0));
                #22=DIRECTION('DU',(1.0,0.0));
                #23=DIRECTION('DV',(0.0,1.0));
                #24=AXIS2_PLACEMENT_3D('AX0',#1,#20,#21);
                #25=AXIS2_PLACEMENT_3D('AX1',#4,#20,#21);
                #28=CONICAL_SURFACE('CN0',#24,1.0,0.4636476090008061);
                #29=OFFSET_SURFACE('OFS0',#28,1.0,.F.);
                #30=CIRCLE('C0',#24,2.0);
                #31=CIRCLE('C1',#25,2.5);
                #33=VECTOR('VU',#22,1.0);
                #34=VECTOR('VV',#23,1.0);
                #35=DIRECTION('GA',(0.5,0.0,1.0));
                #36=DIRECTION('GB',(-0.5,0.0,1.0));
                #39=VECTOR('VGA',#35,1.0);
                #40=VECTOR('VGB',#36,1.0);
                #41=VERTEX_POINT('V0',#2);
                #42=VERTEX_POINT('V1',#3);
                #43=VERTEX_POINT('V2',#5);
                #44=VERTEX_POINT('V3',#4);
                #50=CARTESIAN_POINT('UV00',(0.0,0.0));
                #51=LINE('UVL0',#50,#33);
                #52=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #53=DEFINITIONAL_REPRESENTATION('DEF0',(#51),#52);
                #54=PCURVE('PC0',#28,#53);
                #55=SURFACE_CURVE('SC0',#30,(#54),.PCURVE_S1.);
                #56=CARTESIAN_POINT('UV10',(3.141592653589793,0.0));
                #57=LINE('UVL1',#56,#34);
                #58=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #59=DEFINITIONAL_REPRESENTATION('DEF1',(#57),#58);
                #60=PCURVE('PC1',#28,#59);
                #61=LINE('L0',#3,#40);
                #62=SURFACE_CURVE('SC1',#61,(#60),.PCURVE_S1.);
                #63=CARTESIAN_POINT('UV20',(0.0,1.0));
                #64=LINE('UVL2',#63,#33);
                #65=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #66=DEFINITIONAL_REPRESENTATION('DEF2',(#64),#65);
                #67=PCURVE('PC2',#28,#66);
                #68=SURFACE_CURVE('SC2',#31,(#67),.PCURVE_S1.);
                #69=CARTESIAN_POINT('UV30',(0.0,0.0));
                #70=LINE('UVL3',#69,#34);
                #71=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #72=DEFINITIONAL_REPRESENTATION('DEF3',(#70),#71);
                #73=PCURVE('PC3',#28,#72);
                #74=CARTESIAN_POINT('UV31',(6.283185307179586,0.0));
                #75=LINE('UVL4',#74,#34);
                #76=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #77=DEFINITIONAL_REPRESENTATION('DEF4',(#75),#76);
                #78=PCURVE('PC4',#28,#77);
                #79=LINE('L1',#2,#39);
                #80=SEAM_CURVE('SEAM0',#79,(#73,#78),.PCURVE_S1.);
                #95=EDGE_CURVE('E0',#41,#42,#55,.T.);
                #96=EDGE_CURVE('E1',#42,#43,#62,.T.);
                #97=EDGE_CURVE('E2',#43,#44,#68,.F.);
                #98=EDGE_CURVE('E3',#44,#41,#80,.F.);
                #99=ORIENTED_EDGE('OE0',$,$,#95,.T.);
                #100=ORIENTED_EDGE('OE1',$,$,#96,.T.);
                #101=ORIENTED_EDGE('OE2',$,$,#97,.T.);
                #102=ORIENTED_EDGE('OE3',$,$,#98,.T.);
                #103=EDGE_LOOP('LOUT',(#99,#100,#101,#102));
                #104=FACE_OUTER_BOUND('BOUT',#103,.T.);
                #105=ADVANCED_FACE('F0',(#104),#29,.T.);
                #106=OPEN_SHELL('OS',(#105));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "OFFSET_SURFACE", 4, true);
    }

    @Test
    void shouldExportOffsetConicalFaceUsingTerminatorWrappedPcurvesAndSeamCurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('A0',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('B0',(-2.0,0.0,0.0));
                #4=CARTESIAN_POINT('A1',(2.5,0.0,1.0));
                #5=CARTESIAN_POINT('B1',(-2.5,0.0,1.0));
                #20=DIRECTION('DZ',(0.0,0.0,1.0));
                #21=DIRECTION('DX',(1.0,0.0,0.0));
                #22=DIRECTION('DU',(1.0,0.0));
                #23=DIRECTION('DV',(0.0,1.0));
                #24=AXIS2_PLACEMENT_3D('AX0',#1,#20,#21);
                #25=AXIS2_PLACEMENT_3D('AX1',#4,#20,#21);
                #28=CONICAL_SURFACE('CN0',#24,1.0,0.4636476090008061);
                #29=OFFSET_SURFACE('OS0',#28,1.0,.F.);
                #30=CIRCLE('C0',#24,2.0);
                #31=CIRCLE('C1',#25,2.5);
                #33=VECTOR('VU',#22,1.0);
                #34=VECTOR('VV',#23,1.0);
                #35=DIRECTION('GA',(0.5,0.0,1.0));
                #36=DIRECTION('GB',(-0.5,0.0,1.0));
                #39=VECTOR('VGA',#35,1.0);
                #40=VECTOR('VGB',#36,1.0);
                #41=VERTEX_POINT('V0',#2);
                #42=VERTEX_POINT('V1',#3);
                #43=VERTEX_POINT('V2',#5);
                #44=VERTEX_POINT('V3',#4);
                #50=CARTESIAN_POINT('UV00',(0.0,0.0));
                #51=LINE('UVL0',#50,#33);
                #52=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #53=DEFINITIONAL_REPRESENTATION('DEF0',(#51),#52);
                #54=PCURVE('PC0',#28,#53);
                #55=SURFACE_CURVE('SC0',#30,(#54),.PCURVE_S1.);
                #56=CARTESIAN_POINT('UV10',(3.141592653589793,0.0));
                #57=LINE('UVL1',#56,#34);
                #58=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #59=DEFINITIONAL_REPRESENTATION('DEF1',(#57),#58);
                #60=PCURVE('PC1',#28,#59);
                #61=LINE('L0',#3,#40);
                #62=SURFACE_CURVE('SC1',#61,(#60),.PCURVE_S1.);
                #63=CARTESIAN_POINT('UV20',(0.0,1.0));
                #64=LINE('UVL2',#63,#33);
                #65=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #66=DEFINITIONAL_REPRESENTATION('DEF2',(#64),#65);
                #67=PCURVE('PC2',#28,#66);
                #68=SURFACE_CURVE('SC2',#31,(#67),.PCURVE_S1.);
                #69=CARTESIAN_POINT('UV30',(0.0,0.0));
                #70=LINE('UVL3',#69,#34);
                #71=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #72=DEFINITIONAL_REPRESENTATION('DEF3',(#70),#71);
                #73=PCURVE('PC3',#28,#72);
                #74=CARTESIAN_POINT('UV31',(6.283185307179586,0.0));
                #75=LINE('UVL4',#74,#34);
                #76=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #77=DEFINITIONAL_REPRESENTATION('DEF4',(#75),#76);
                #78=PCURVE('PC4',#28,#77);
                #79=LINE('L1',#2,#39);
                #80=SEAM_CURVE('SEAM0',#79,(#73,#78),.PCURVE_S1.);
                #81=PRESENTATION_STYLE_ASSIGNMENT(());
                #82=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #83=DIRECTION('SD0',(1.0,0.0));
                #84=AXIS2_PLACEMENT_2D('MAP',#82,#83);
                #85=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #86=REPRESENTATION('SYMREP',(),#85);
                #87=SYMBOL_REPRESENTATION_MAP(#84,#86);
                #88=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #89=DIRECTION('SD1',(1.0,0.0));
                #90=AXIS2_PLACEMENT_2D('TGT',#88,#89);
                #91=ANNOTATION_SYMBOL('AS0',#87,#90);
                #92=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#81),#55);
                #93=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#81),#80);
                #94=TERMINATOR_SYMBOL('TS0',(#81),#91,#92);
                #95=TERMINATOR_SYMBOL('TS1',(#81),#91,#93);
                #96=EDGE_CURVE('E0',#41,#42,#94,.T.);
                #97=EDGE_CURVE('E1',#42,#43,#62,.T.);
                #98=EDGE_CURVE('E2',#43,#44,#68,.F.);
                #99=EDGE_CURVE('E3',#44,#41,#95,.F.);
                #100=ORIENTED_EDGE('OE0',$,$,#96,.T.);
                #101=ORIENTED_EDGE('OE1',$,$,#97,.T.);
                #102=ORIENTED_EDGE('OE2',$,$,#98,.T.);
                #103=ORIENTED_EDGE('OE3',$,$,#99,.T.);
                #104=EDGE_LOOP('LOUT',(#100,#101,#102,#103));
                #105=FACE_OUTER_BOUND('BOUT',#104,.T.);
                #106=ADVANCED_FACE('F0',(#105),#29,.T.);
                #107=OPEN_SHELL('OS',(#106));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "OFFSET_SURFACE", 6, true);
    }

    @Test
    void shouldExportOffsetToroidalBandPreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P00',(6.060660171779821,0.0,-1.0606601717798212));
                #3=CARTESIAN_POINT('P01',(6.060660171779821,0.0,1.0606601717798212));
                #4=CARTESIAN_POINT('P10',(0.0,6.060660171779821,-1.0606601717798212));
                #5=CARTESIAN_POINT('P11',(0.0,6.060660171779821,1.0606601717798212));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=DIRECTION('DY',(0.0,1.0,0.0));
                #13=DIRECTION('NX',(-1.0,0.0,0.0));
                #14=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #15=TOROIDAL_SURFACE('TO0',#14,5.0,1.0);
                #16=OFFSET_SURFACE('OFS0',#15,0.5,.F.);
                #17=CARTESIAN_POINT('CL',(0.0,0.0,-1.0606601717798212));
                #18=CARTESIAN_POINT('CU',(0.0,0.0,1.0606601717798212));
                #19=CARTESIAN_POINT('CR',(5.0,0.0,0.0));
                #20=CARTESIAN_POINT('CT',(0.0,5.0,0.0));
                #21=AXIS2_PLACEMENT_3D('AL',#17,#10,#11);
                #22=AXIS2_PLACEMENT_3D('AU',#18,#10,#11);
                #23=AXIS2_PLACEMENT_3D('AR',#19,#12,#11);
                #24=AXIS2_PLACEMENT_3D('AT',#20,#13,#12);
                #25=CIRCLE('C0',#21,6.060660171779821);
                #26=CIRCLE('C1',#24,1.5);
                #27=CIRCLE('C2',#22,6.060660171779821);
                #28=CIRCLE('C3',#23,1.5);
                #30=VERTEX_POINT('V0',#2);
                #31=VERTEX_POINT('V1',#4);
                #32=VERTEX_POINT('V2',#5);
                #33=VERTEX_POINT('V3',#3);
                #40=EDGE_CURVE('E0',#30,#31,#25,.T.);
                #41=EDGE_CURVE('E1',#31,#32,#26,.T.);
                #42=EDGE_CURVE('E2',#33,#32,#27,.T.);
                #43=EDGE_CURVE('E3',#30,#33,#28,.T.);
                #50=ORIENTED_EDGE('OE0',$,$,#40,.T.);
                #51=ORIENTED_EDGE('OE1',$,$,#41,.T.);
                #52=ORIENTED_EDGE('OE2',$,$,#42,.F.);
                #53=ORIENTED_EDGE('OE3',$,$,#43,.F.);
                #60=EDGE_LOOP('L0',(#50,#51,#52,#53));
                #61=FACE_OUTER_BOUND('B0',#60,.T.);
                #70=ADVANCED_FACE('F0',(#61),#16,.T.);
                #80=OPEN_SHELL('OS',(#70));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"OFFSET_SURFACE\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":4"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldExportOrientedToroidalBandPreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P00',(5.707106781186548,0.0,-0.7071067811865475));
                #3=CARTESIAN_POINT('P01',(5.707106781186548,0.0,0.7071067811865475));
                #4=CARTESIAN_POINT('P10',(0.0,5.707106781186548,-0.7071067811865475));
                #5=CARTESIAN_POINT('P11',(0.0,5.707106781186548,0.7071067811865475));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=DIRECTION('DY',(0.0,1.0,0.0));
                #13=DIRECTION('NX',(-1.0,0.0,0.0));
                #14=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #15=TOROIDAL_SURFACE('TO0',#14,5.0,1.0);
                #16=ORIENTED_SURFACE('OS0',#15,.T.);
                #17=CARTESIAN_POINT('CL',(0.0,0.0,-0.7071067811865475));
                #18=CARTESIAN_POINT('CU',(0.0,0.0,0.7071067811865475));
                #19=CARTESIAN_POINT('CR',(5.0,0.0,0.0));
                #20=CARTESIAN_POINT('CT',(0.0,5.0,0.0));
                #21=AXIS2_PLACEMENT_3D('AL',#17,#10,#11);
                #22=AXIS2_PLACEMENT_3D('AU',#18,#10,#11);
                #23=AXIS2_PLACEMENT_3D('AR',#19,#12,#11);
                #24=AXIS2_PLACEMENT_3D('AT',#20,#13,#12);
                #25=CIRCLE('C0',#21,5.707106781186548);
                #26=CIRCLE('C1',#24,1.0);
                #27=CIRCLE('C2',#22,5.707106781186548);
                #28=CIRCLE('C3',#23,1.0);
                #30=VERTEX_POINT('V0',#2);
                #31=VERTEX_POINT('V1',#4);
                #32=VERTEX_POINT('V2',#5);
                #33=VERTEX_POINT('V3',#3);
                #40=EDGE_CURVE('E0',#30,#31,#25,.T.);
                #41=EDGE_CURVE('E1',#31,#32,#26,.T.);
                #42=EDGE_CURVE('E2',#33,#32,#27,.T.);
                #43=EDGE_CURVE('E3',#30,#33,#28,.T.);
                #50=ORIENTED_EDGE('OE0',$,$,#40,.T.);
                #51=ORIENTED_EDGE('OE1',$,$,#41,.T.);
                #52=ORIENTED_EDGE('OE2',$,$,#42,.F.);
                #53=ORIENTED_EDGE('OE3',$,$,#43,.F.);
                #60=EDGE_LOOP('L0',(#50,#51,#52,#53));
                #61=FACE_OUTER_BOUND('B0',#60,.T.);
                #70=ADVANCED_FACE('F0',(#61),#16,.T.);
                #80=OPEN_SHELL('OS',(#70));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "ORIENTED_SURFACE", 4, true);
    }

    @Test
    void shouldExportRectangularTrimmedToroidalBandPreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P00',(5.707106781186548,0.0,-0.7071067811865475));
                #3=CARTESIAN_POINT('P01',(5.707106781186548,0.0,0.7071067811865475));
                #4=CARTESIAN_POINT('P10',(0.0,5.707106781186548,-0.7071067811865475));
                #5=CARTESIAN_POINT('P11',(0.0,5.707106781186548,0.7071067811865475));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=DIRECTION('DY',(0.0,1.0,0.0));
                #13=DIRECTION('NX',(-1.0,0.0,0.0));
                #14=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #15=TOROIDAL_SURFACE('TO0',#14,5.0,1.0);
                #16=RECTANGULAR_TRIMMED_SURFACE('RT0',#15,0.0,1.5707963267948966,-0.7853981633974483,0.7853981633974483,.T.,.T.);
                #17=CARTESIAN_POINT('CL',(0.0,0.0,-0.7071067811865475));
                #18=CARTESIAN_POINT('CU',(0.0,0.0,0.7071067811865475));
                #19=CARTESIAN_POINT('CR',(5.0,0.0,0.0));
                #20=CARTESIAN_POINT('CT',(0.0,5.0,0.0));
                #21=AXIS2_PLACEMENT_3D('AL',#17,#10,#11);
                #22=AXIS2_PLACEMENT_3D('AU',#18,#10,#11);
                #23=AXIS2_PLACEMENT_3D('AR',#19,#12,#11);
                #24=AXIS2_PLACEMENT_3D('AT',#20,#13,#12);
                #25=CIRCLE('C0',#21,5.707106781186548);
                #26=CIRCLE('C1',#24,1.0);
                #27=CIRCLE('C2',#22,5.707106781186548);
                #28=CIRCLE('C3',#23,1.0);
                #30=VERTEX_POINT('V0',#2);
                #31=VERTEX_POINT('V1',#4);
                #32=VERTEX_POINT('V2',#5);
                #33=VERTEX_POINT('V3',#3);
                #40=EDGE_CURVE('E0',#30,#31,#25,.T.);
                #41=EDGE_CURVE('E1',#31,#32,#26,.T.);
                #42=EDGE_CURVE('E2',#33,#32,#27,.T.);
                #43=EDGE_CURVE('E3',#30,#33,#28,.T.);
                #50=ORIENTED_EDGE('OE0',$,$,#40,.T.);
                #51=ORIENTED_EDGE('OE1',$,$,#41,.T.);
                #52=ORIENTED_EDGE('OE2',$,$,#42,.F.);
                #53=ORIENTED_EDGE('OE3',$,$,#43,.F.);
                #60=EDGE_LOOP('L0',(#50,#51,#52,#53));
                #61=FACE_OUTER_BOUND('B0',#60,.T.);
                #70=ADVANCED_FACE('F0',(#61),#16,.T.);
                #80=OPEN_SHELL('OS',(#70));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "RECTANGULAR_TRIMMED_SURFACE", 4, true);
    }

    @Test
    void shouldExportCurveBoundedToroidalBandPreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P00',(5.707106781186548,0.0,-0.7071067811865475));
                #3=CARTESIAN_POINT('P01',(5.707106781186548,0.0,0.7071067811865475));
                #4=CARTESIAN_POINT('P10',(0.0,5.707106781186548,-0.7071067811865475));
                #5=CARTESIAN_POINT('P11',(0.0,5.707106781186548,0.7071067811865475));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=DIRECTION('DY',(0.0,1.0,0.0));
                #13=DIRECTION('NX',(-1.0,0.0,0.0));
                #14=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #15=TOROIDAL_SURFACE('TO0',#14,5.0,1.0);
                #16=CARTESIAN_POINT('CL',(0.0,0.0,-0.7071067811865475));
                #17=CARTESIAN_POINT('CU',(0.0,0.0,0.7071067811865475));
                #18=CARTESIAN_POINT('CR',(5.0,0.0,0.0));
                #19=CARTESIAN_POINT('CT',(0.0,5.0,0.0));
                #20=AXIS2_PLACEMENT_3D('AL',#16,#10,#11);
                #21=AXIS2_PLACEMENT_3D('AU',#17,#10,#11);
                #22=AXIS2_PLACEMENT_3D('AR',#18,#12,#11);
                #23=AXIS2_PLACEMENT_3D('AT',#19,#13,#12);
                #24=CIRCLE('C0',#20,5.707106781186548);
                #25=CIRCLE('C1',#23,1.0);
                #26=CIRCLE('C2',#21,5.707106781186548);
                #27=CIRCLE('C3',#22,1.0);
                #28=CURVE_BOUNDED_SURFACE('CBS0',#15,(#24,#25,#26,#27),.T.);
                #30=VERTEX_POINT('V0',#2);
                #31=VERTEX_POINT('V1',#4);
                #32=VERTEX_POINT('V2',#5);
                #33=VERTEX_POINT('V3',#3);
                #40=EDGE_CURVE('E0',#30,#31,#24,.T.);
                #41=EDGE_CURVE('E1',#31,#32,#25,.T.);
                #42=EDGE_CURVE('E2',#33,#32,#26,.T.);
                #43=EDGE_CURVE('E3',#30,#33,#27,.T.);
                #50=ORIENTED_EDGE('OE0',$,$,#40,.T.);
                #51=ORIENTED_EDGE('OE1',$,$,#41,.T.);
                #52=ORIENTED_EDGE('OE2',$,$,#42,.F.);
                #53=ORIENTED_EDGE('OE3',$,$,#43,.F.);
                #60=EDGE_LOOP('L0',(#50,#51,#52,#53));
                #61=FACE_OUTER_BOUND('B0',#60,.T.);
                #70=ADVANCED_FACE('F0',(#61),#28,.T.);
                #80=OPEN_SHELL('OS',(#70));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "CURVE_BOUNDED_SURFACE", 4, true);
    }

    @Test
    void shouldExportReplicaToroidalBandPreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P00',(5.707106781186548,0.0,-0.7071067811865475));
                #3=CARTESIAN_POINT('P01',(5.707106781186548,0.0,0.7071067811865475));
                #4=CARTESIAN_POINT('P10',(0.0,5.707106781186548,-0.7071067811865475));
                #5=CARTESIAN_POINT('P11',(0.0,5.707106781186548,0.7071067811865475));
                #6=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=DIRECTION('DY',(0.0,1.0,0.0));
                #13=DIRECTION('NX',(-1.0,0.0,0.0));
                #14=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #15=TOROIDAL_SURFACE('TO0',#14,5.0,1.0);
                #16=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#6,1.0,$);
                #17=SURFACE_REPLICA('SR0',#15,#16);
                #18=CARTESIAN_POINT('CL',(0.0,0.0,-0.7071067811865475));
                #19=CARTESIAN_POINT('CU',(0.0,0.0,0.7071067811865475));
                #20=CARTESIAN_POINT('CR',(5.0,0.0,0.0));
                #21=CARTESIAN_POINT('CT',(0.0,5.0,0.0));
                #22=AXIS2_PLACEMENT_3D('AL',#18,#10,#11);
                #23=AXIS2_PLACEMENT_3D('AU',#19,#10,#11);
                #24=AXIS2_PLACEMENT_3D('AR',#20,#12,#11);
                #25=AXIS2_PLACEMENT_3D('AT',#21,#13,#12);
                #26=CIRCLE('C0',#22,5.707106781186548);
                #27=CIRCLE('C1',#25,1.0);
                #28=CIRCLE('C2',#23,5.707106781186548);
                #29=CIRCLE('C3',#24,1.0);
                #30=CURVE_REPLICA('CR0',#26,#16);
                #31=CURVE_REPLICA('CR1',#27,#16);
                #32=CURVE_REPLICA('CR2',#28,#16);
                #33=CURVE_REPLICA('CR3',#29,#16);
                #34=CARTESIAN_POINT('TP00',(5.707106781186548,0.0,1.2928932188134525));
                #35=CARTESIAN_POINT('TP01',(5.707106781186548,0.0,2.7071067811865475));
                #36=CARTESIAN_POINT('TP10',(0.0,5.707106781186548,1.2928932188134525));
                #37=CARTESIAN_POINT('TP11',(0.0,5.707106781186548,2.7071067811865475));
                #40=VERTEX_POINT('V0',#34);
                #41=VERTEX_POINT('V1',#36);
                #42=VERTEX_POINT('V2',#37);
                #43=VERTEX_POINT('V3',#35);
                #50=EDGE_CURVE('E0',#40,#41,#30,.T.);
                #51=EDGE_CURVE('E1',#41,#42,#31,.T.);
                #52=EDGE_CURVE('E2',#43,#42,#32,.T.);
                #53=EDGE_CURVE('E3',#40,#43,#33,.T.);
                #60=ORIENTED_EDGE('OE0',$,$,#50,.T.);
                #61=ORIENTED_EDGE('OE1',$,$,#51,.T.);
                #62=ORIENTED_EDGE('OE2',$,$,#52,.F.);
                #63=ORIENTED_EDGE('OE3',$,$,#53,.F.);
                #70=EDGE_LOOP('L0',(#60,#61,#62,#63));
                #71=FACE_OUTER_BOUND('B0',#70,.T.);
                #80=ADVANCED_FACE('F0',(#71),#17,.T.);
                #81=OPEN_SHELL('OS',(#80));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "SURFACE_REPLICA", 4, true);
    }

    @Test
    void shouldExportOffsetToroidalBandUsingTerminatorWrappedEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P00',(6.060660171779821,0.0,-1.0606601717798212));
                #3=CARTESIAN_POINT('P01',(6.060660171779821,0.0,1.0606601717798212));
                #4=CARTESIAN_POINT('P10',(0.0,6.060660171779821,-1.0606601717798212));
                #5=CARTESIAN_POINT('P11',(0.0,6.060660171779821,1.0606601717798212));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=DIRECTION('DY',(0.0,1.0,0.0));
                #13=DIRECTION('NX',(-1.0,0.0,0.0));
                #14=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #15=TOROIDAL_SURFACE('TO0',#14,5.0,1.0);
                #16=OFFSET_SURFACE('OFS0',#15,0.5,.F.);
                #17=CARTESIAN_POINT('CL',(0.0,0.0,-1.0606601717798212));
                #18=CARTESIAN_POINT('CU',(0.0,0.0,1.0606601717798212));
                #19=CARTESIAN_POINT('CR',(5.0,0.0,0.0));
                #20=CARTESIAN_POINT('CT',(0.0,5.0,0.0));
                #21=AXIS2_PLACEMENT_3D('AL',#17,#10,#11);
                #22=AXIS2_PLACEMENT_3D('AU',#18,#10,#11);
                #23=AXIS2_PLACEMENT_3D('AR',#19,#12,#11);
                #24=AXIS2_PLACEMENT_3D('AT',#20,#13,#12);
                #25=CIRCLE('C0',#21,6.060660171779821);
                #26=CIRCLE('C1',#24,1.5);
                #27=CIRCLE('C2',#22,6.060660171779821);
                #28=CIRCLE('C3',#23,1.5);
                #29=PRESENTATION_STYLE_ASSIGNMENT(());
                #30=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #31=DIRECTION('SD0',(1.0,0.0));
                #32=AXIS2_PLACEMENT_2D('MAP',#30,#31);
                #33=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #34=REPRESENTATION('SYMREP',(),#33);
                #35=SYMBOL_REPRESENTATION_MAP(#32,#34);
                #36=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #37=DIRECTION('SD1',(1.0,0.0));
                #38=AXIS2_PLACEMENT_2D('TGT',#36,#37);
                #39=ANNOTATION_SYMBOL('AS0',#35,#38);
                #40=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#29),#25);
                #41=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#29),#28);
                #42=TERMINATOR_SYMBOL('TS0',(#29),#39,#40);
                #43=TERMINATOR_SYMBOL('TS1',(#29),#39,#41);
                #50=VERTEX_POINT('V0',#2);
                #51=VERTEX_POINT('V1',#4);
                #52=VERTEX_POINT('V2',#5);
                #53=VERTEX_POINT('V3',#3);
                #60=EDGE_CURVE('E0',#50,#51,#42,.T.);
                #61=EDGE_CURVE('E1',#51,#52,#26,.T.);
                #62=EDGE_CURVE('E2',#53,#52,#27,.T.);
                #63=EDGE_CURVE('E3',#50,#53,#43,.T.);
                #70=ORIENTED_EDGE('OE0',$,$,#60,.T.);
                #71=ORIENTED_EDGE('OE1',$,$,#61,.T.);
                #72=ORIENTED_EDGE('OE2',$,$,#62,.F.);
                #73=ORIENTED_EDGE('OE3',$,$,#63,.F.);
                #80=EDGE_LOOP('L0',(#70,#71,#72,#73));
                #81=FACE_OUTER_BOUND('B0',#80,.T.);
                #90=ADVANCED_FACE('F0',(#81),#16,.T.);
                #91=OPEN_SHELL('OS',(#90));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "OFFSET_SURFACE", 6, true);
    }

    @Test
    void shouldExportReplicaToroidalBandUsingTerminatorWrappedReplicaEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P00',(5.707106781186548,0.0,-0.7071067811865475));
                #3=CARTESIAN_POINT('P01',(5.707106781186548,0.0,0.7071067811865475));
                #4=CARTESIAN_POINT('P10',(0.0,5.707106781186548,-0.7071067811865475));
                #5=CARTESIAN_POINT('P11',(0.0,5.707106781186548,0.7071067811865475));
                #6=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=DIRECTION('DY',(0.0,1.0,0.0));
                #13=DIRECTION('NX',(-1.0,0.0,0.0));
                #14=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #15=TOROIDAL_SURFACE('TO0',#14,5.0,1.0);
                #16=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#6,1.0,$);
                #17=SURFACE_REPLICA('SR0',#15,#16);
                #18=CARTESIAN_POINT('CL',(0.0,0.0,-0.7071067811865475));
                #19=CARTESIAN_POINT('CU',(0.0,0.0,0.7071067811865475));
                #20=CARTESIAN_POINT('CR',(5.0,0.0,0.0));
                #21=CARTESIAN_POINT('CT',(0.0,5.0,0.0));
                #22=AXIS2_PLACEMENT_3D('AL',#18,#10,#11);
                #23=AXIS2_PLACEMENT_3D('AU',#19,#10,#11);
                #24=AXIS2_PLACEMENT_3D('AR',#20,#12,#11);
                #25=AXIS2_PLACEMENT_3D('AT',#21,#13,#12);
                #26=CIRCLE('C0',#22,5.707106781186548);
                #27=CIRCLE('C1',#25,1.0);
                #28=CIRCLE('C2',#23,5.707106781186548);
                #29=CIRCLE('C3',#24,1.0);
                #30=CURVE_REPLICA('CR0',#26,#16);
                #31=CURVE_REPLICA('CR1',#27,#16);
                #32=CURVE_REPLICA('CR2',#28,#16);
                #33=CURVE_REPLICA('CR3',#29,#16);
                #34=PRESENTATION_STYLE_ASSIGNMENT(());
                #35=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #36=DIRECTION('SD0',(1.0,0.0));
                #37=AXIS2_PLACEMENT_2D('MAP',#35,#36);
                #38=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #39=REPRESENTATION('SYMREP',(),#38);
                #40=SYMBOL_REPRESENTATION_MAP(#37,#39);
                #41=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #42=DIRECTION('SD1',(1.0,0.0));
                #43=AXIS2_PLACEMENT_2D('TGT',#41,#42);
                #44=ANNOTATION_SYMBOL('AS0',#40,#43);
                #45=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#34),#30);
                #46=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#34),#33);
                #47=TERMINATOR_SYMBOL('TS0',(#34),#44,#45);
                #48=TERMINATOR_SYMBOL('TS1',(#34),#44,#46);
                #49=CARTESIAN_POINT('TP00',(5.707106781186548,0.0,1.2928932188134525));
                #50=CARTESIAN_POINT('TP01',(5.707106781186548,0.0,2.7071067811865475));
                #51=CARTESIAN_POINT('TP10',(0.0,5.707106781186548,1.2928932188134525));
                #52=CARTESIAN_POINT('TP11',(0.0,5.707106781186548,2.7071067811865475));
                #53=VERTEX_POINT('V0',#49);
                #54=VERTEX_POINT('V1',#51);
                #55=VERTEX_POINT('V2',#52);
                #56=VERTEX_POINT('V3',#50);
                #60=EDGE_CURVE('E0',#53,#54,#47,.T.);
                #61=EDGE_CURVE('E1',#54,#55,#31,.T.);
                #62=EDGE_CURVE('E2',#56,#55,#32,.T.);
                #63=EDGE_CURVE('E3',#53,#56,#48,.T.);
                #70=ORIENTED_EDGE('OE0',$,$,#60,.T.);
                #71=ORIENTED_EDGE('OE1',$,$,#61,.T.);
                #72=ORIENTED_EDGE('OE2',$,$,#62,.F.);
                #73=ORIENTED_EDGE('OE3',$,$,#63,.F.);
                #80=EDGE_LOOP('L0',(#70,#71,#72,#73));
                #81=FACE_OUTER_BOUND('B0',#80,.T.);
                #90=ADVANCED_FACE('F0',(#81),#17,.T.);
                #91=OPEN_SHELL('OS',(#90));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "SURFACE_REPLICA", 6, true);
    }

    @Test
    void shouldExportUniformScaledReplicaToroidalBandPreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P00',(11.414213562373096,0.0,-1.414213562373095));
                #3=CARTESIAN_POINT('P01',(11.414213562373096,0.0,1.414213562373095));
                #4=CARTESIAN_POINT('P10',(0.0,11.414213562373096,-1.414213562373095));
                #5=CARTESIAN_POINT('P11',(0.0,11.414213562373096,1.414213562373095));
                #6=DIRECTION('DZ',(0.0,0.0,1.0));
                #7=DIRECTION('DX',(1.0,0.0,0.0));
                #8=DIRECTION('DY',(0.0,1.0,0.0));
                #9=DIRECTION('NX',(-1.0,0.0,0.0));
                #10=AXIS2_PLACEMENT_3D('AX',#1,#6,#7);
                #11=TOROIDAL_SURFACE('TO0',#10,5.0,1.0);
                #12=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#1,2.0,$);
                #13=SURFACE_REPLICA('SR0',#11,#12);
                #14=CARTESIAN_POINT('CL',(0.0,0.0,-1.414213562373095));
                #15=CARTESIAN_POINT('CU',(0.0,0.0,1.414213562373095));
                #16=CARTESIAN_POINT('CR',(10.0,0.0,0.0));
                #17=CARTESIAN_POINT('CT',(0.0,10.0,0.0));
                #18=AXIS2_PLACEMENT_3D('AL',#14,#6,#7);
                #19=AXIS2_PLACEMENT_3D('AU',#15,#6,#7);
                #20=AXIS2_PLACEMENT_3D('AR',#16,#8,#7);
                #21=AXIS2_PLACEMENT_3D('AT',#17,#9,#8);
                #22=CIRCLE('C0',#18,11.414213562373096);
                #23=CIRCLE('C1',#21,2.0);
                #24=CIRCLE('C2',#19,11.414213562373096);
                #25=CIRCLE('C3',#20,2.0);
                #26=CURVE_REPLICA('CR0',#22,#12);
                #27=CURVE_REPLICA('CR1',#23,#12);
                #28=CURVE_REPLICA('CR2',#24,#12);
                #29=CURVE_REPLICA('CR3',#25,#12);
                #30=VERTEX_POINT('V0',#2);
                #31=VERTEX_POINT('V1',#4);
                #32=VERTEX_POINT('V2',#5);
                #33=VERTEX_POINT('V3',#3);
                #40=EDGE_CURVE('E0',#30,#31,#26,.T.);
                #41=EDGE_CURVE('E1',#31,#32,#27,.T.);
                #42=EDGE_CURVE('E2',#33,#32,#28,.T.);
                #43=EDGE_CURVE('E3',#30,#33,#29,.T.);
                #50=ORIENTED_EDGE('OE0',$,$,#40,.T.);
                #51=ORIENTED_EDGE('OE1',$,$,#41,.T.);
                #52=ORIENTED_EDGE('OE2',$,$,#42,.F.);
                #53=ORIENTED_EDGE('OE3',$,$,#43,.F.);
                #60=EDGE_LOOP('L0',(#50,#51,#52,#53));
                #61=FACE_OUTER_BOUND('B0',#60,.T.);
                #62=ADVANCED_FACE('F0',(#61),#13,.T.);
                #63=OPEN_SHELL('OS',(#62));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"SURFACE_REPLICA\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":4"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldExportSurfaceReplicaWithUniformScalePreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX0',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #6=CARTESIAN_POINT('O2',(0.0,0.0,2.0));
                #7=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#6,2.0,$);
                #8=SURFACE_REPLICA('SR0',#5,#7);
                #9=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #10=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #11=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #12=VECTOR('VX',#3,1.0);
                #13=LINE('L0',#1,#12);
                #14=DIRECTION('DY',(0.0,1.0,0.0));
                #15=VECTOR('VY',#14,1.0);
                #16=LINE('L1',#9,#15);
                #17=DIRECTION('MX',(-1.0,0.0,0.0));
                #18=VECTOR('VMX',#17,1.0);
                #19=LINE('L2',#10,#18);
                #20=DIRECTION('MY',(0.0,-1.0,0.0));
                #21=VECTOR('VMY',#20,1.0);
                #22=LINE('L3',#11,#21);
                #23=CURVE_REPLICA('CR0',#13,#7);
                #24=CURVE_REPLICA('CR1',#16,#7);
                #25=CURVE_REPLICA('CR2',#19,#7);
                #26=CURVE_REPLICA('CR3',#22,#7);
                #27=CARTESIAN_POINT('Q0',(0.0,0.0,2.0));
                #28=CARTESIAN_POINT('Q1',(2.0,0.0,2.0));
                #29=CARTESIAN_POINT('Q2',(2.0,2.0,2.0));
                #30=CARTESIAN_POINT('Q3',(0.0,2.0,2.0));
                #31=VERTEX_POINT('V0',#27);
                #32=VERTEX_POINT('V1',#28);
                #33=VERTEX_POINT('V2',#29);
                #34=VERTEX_POINT('V3',#30);
                #35=EDGE_CURVE('E0',#31,#32,#23,.T.);
                #36=EDGE_CURVE('E1',#32,#33,#24,.T.);
                #37=EDGE_CURVE('E2',#33,#34,#25,.T.);
                #38=EDGE_CURVE('E3',#34,#31,#26,.T.);
                #39=ORIENTED_EDGE('OE0',$,$,#35,.T.);
                #40=ORIENTED_EDGE('OE1',$,$,#36,.T.);
                #41=ORIENTED_EDGE('OE2',$,$,#37,.T.);
                #42=ORIENTED_EDGE('OE3',$,$,#38,.T.);
                #43=EDGE_LOOP('LOOP0',(#39,#40,#41,#42));
                #44=FACE_OUTER_BOUND('B0',#43,.T.);
                #45=ADVANCED_FACE('F0',(#44),#8,.T.);
                #46=OPEN_SHELL('OS0',(#45));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "SURFACE_REPLICA", 4, false);
        assertTrue(json.contains("[2.0,2.0,2.0]"), json);
    }

    @Test
    void shouldReportSurfaceReplicaZeroScaleAsUnsupportedPreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX0',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #6=CARTESIAN_POINT('O2',(0.0,0.0,2.0));
                #7=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#6,0.0,$);
                #8=SURFACE_REPLICA('SR0',#5,#7);
                #10=POLY_LOOP('LOOP0',(#1,#1,#1));
                #11=FACE_OUTER_BOUND('B0',#10,.T.);
                #12=ADVANCED_FACE('F0',(#11),#8,.T.);
                #13=OPEN_SHELL('OS0',(#12));
                ENDSEC;
                """);

        assertTrue(json.contains("\"unsupportedFaceCount\":1"), json);
        assertTrue(json.contains("\"surfaceType\":\"SURFACE_REPLICA\""), json);
        assertTrue(json.contains("\"reason\":\"SURFACE_REPLICA zero scale preview is unsupported\""), json);
    }

    @Test
    void shouldReportSurfaceReplicaNonUniformScaleAsUnsupportedPreview() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX0',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #6=CARTESIAN_POINT('O2',(0.0,0.0,2.0));
                #7=DIRECTION('SX',(1.0,0.0,0.0));
                #8=DIRECTION('SY',(1.0,1.0,0.0));
                #9=DIRECTION('SZ',(0.0,0.0,1.0));
                #10=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',#7,#8,#6,1.0,#9);
                #11=SURFACE_REPLICA('SR0',#5,#10);
                #12=POLY_LOOP('LOOP0',(#1,#1,#1));
                #13=FACE_OUTER_BOUND('B0',#12,.T.);
                #14=ADVANCED_FACE('F0',(#13),#11,.T.);
                #15=OPEN_SHELL('OS0',(#14));
                ENDSEC;
                """);

        assertTrue(json.contains("\"unsupportedFaceCount\":1"), json);
        assertTrue(json.contains("\"surfaceType\":\"SURFACE_REPLICA\""), json);
        assertTrue(json.contains("\"reason\":\"SURFACE_REPLICA non-uniform scale preview is unsupported\""), json);
    }

    @Test
    void shouldMatchPcurvesThroughWrappedSurfaceCurveGeometry() throws Exception {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #4=CARTESIAN_POINT('A1',(1.0,0.0,1.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX0',#1,#10,#11);
                #13=CYLINDRICAL_SURFACE('CY0',#12,1.0);
                #20=VERTEX_POINT('V0',#3);
                #21=VERTEX_POINT('V1',#4);
                #30=DIRECTION('DU',(0.0,0.0,1.0));
                #31=VECTOR('VU',#30,1.0);
                #32=LINE('L0',#3,#31);
                #40=CARTESIAN_POINT('UV0',(0.0,0.0));
                #41=DIRECTION('DV',(0.0,1.0));
                #42=VECTOR('VV',#41,1.0);
                #43=LINE('UVL0',#40,#42);
                #44=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #45=DEFINITIONAL_REPRESENTATION('DEF0',(#43),#44);
                #46=PCURVE('PC0',#13,#45);
                #47=SURFACE_CURVE('SC0',#32,(#46),.PCURVE_S1.);
                #48=PRESENTATION_STYLE_ASSIGNMENT(());
                #49=(PROJECTION_CURVE('PCW0',(#48),#47)
                    ANNOTATION_CURVE_OCCURRENCE('PCW0',(#48),#47)
                    STYLED_ITEM('PCW0',(#48),#47)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PCW0'));
                #50=ORIENTED_CURVE('OC0',#49,.F.);
                #51=EDGE_CURVE('E0',#20,#21,#50,.T.);
                #52=ORIENTED_EDGE('OE0',$,$,#51,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        Method mapperForSurface = StepPreviewJsonExporter.class.getDeclaredMethod(
                "mapperForSurface",
                StepEntity.class,
                StepCadBuilder.class
        );
        mapperForSurface.setAccessible(true);
        Object mapper = mapperForSurface.invoke(null, resolved.get(13), builder);
        assertNotNull(mapper);

        Method sampleParametricOrientedEdge = StepPreviewJsonExporter.class.getDeclaredMethod(
                "sampleParametricOrientedEdge",
                StepOrientedEdge.class,
                StepEntity.class,
                Class.forName("com.minicad.app.StepPreviewJsonExporter$ParametricSurfaceMapper"),
                StepCadBuilder.class
        );
        sampleParametricOrientedEdge.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Object> points = (List<Object>) sampleParametricOrientedEdge.invoke(null, resolved.get(52), resolved.get(13), mapper, builder);
        assertNotNull(points);
        assertTrue(points.size() >= 8);

        Method u = points.getFirst().getClass().getDeclaredMethod("u");
        Method v = points.getFirst().getClass().getDeclaredMethod("v");
        assertEquals(0.0, (double) u.invoke(points.getFirst()), 1.0e-9);
        assertEquals(0.0, (double) v.invoke(points.getFirst()), 1.0e-9);
        assertEquals(0.0, (double) u.invoke(points.getLast()), 1.0e-9);
        assertEquals(1.0, (double) v.invoke(points.getLast()), 1.0e-9);
    }

    @Test
    void shouldPickMatchingPcurveThroughWrappedSeamCurveGeometry() throws Exception {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=DIRECTION('DZ',(0.0,0.0,1.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX0',#1,#3,#4);
                #6=AXIS2_PLACEMENT_3D('AX1',#2,#3,#4);
                #7=CONICAL_SURFACE('CN0',#5,1.0,0.4636476090008061);
                #8=CONICAL_SURFACE('CN1',#6,1.0,0.4636476090008061);
                #10=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(2.0,0.0,1.0));
                #12=VERTEX_POINT('V0',#10);
                #13=VERTEX_POINT('V1',#11);
                #14=DIRECTION('DU',(1.0,0.0,1.0));
                #15=VECTOR('VU',#14,1.0);
                #16=LINE('L0',#10,#15);
                #20=CARTESIAN_POINT('UV0',(0.0,0.0));
                #21=DIRECTION('DV0',(0.0,1.0));
                #22=VECTOR('VV0',#21,1.0);
                #23=LINE('GOOD',#20,#22);
                #24=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #25=DEFINITIONAL_REPRESENTATION('DEF0',(#23),#24);
                #26=PCURVE('PC0',#7,#25);
                #27=CARTESIAN_POINT('UV1',(3.141592653589793,0.0));
                #28=DIRECTION('DV1',(0.0,1.0));
                #29=VECTOR('VV1',#28,1.0);
                #30=LINE('BAD',#27,#29);
                #31=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #32=DEFINITIONAL_REPRESENTATION('DEF1',(#30),#31);
                #33=PCURVE('PC1',#8,#32);
                #34=SEAM_CURVE('SEAM0',#16,(#33,#26),.PCURVE_S1.);
                #35=PRESENTATION_STYLE_ASSIGNMENT(());
                #36=(PROJECTION_CURVE('PCW0',(#35),#34)
                    ANNOTATION_CURVE_OCCURRENCE('PCW0',(#35),#34)
                    STYLED_ITEM('PCW0',(#35),#34)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PCW0'));
                #37=ORIENTED_CURVE('OC0',#36,.F.);
                #38=EDGE_CURVE('E0',#12,#13,#37,.T.);
                #39=ORIENTED_EDGE('OE0',$,$,#38,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        Method mapperForSurface = StepPreviewJsonExporter.class.getDeclaredMethod(
                "mapperForSurface",
                StepEntity.class,
                StepCadBuilder.class
        );
        mapperForSurface.setAccessible(true);
        Object mapper = mapperForSurface.invoke(null, resolved.get(7), builder);
        assertNotNull(mapper);

        Method sampleParametricOrientedEdge = StepPreviewJsonExporter.class.getDeclaredMethod(
                "sampleParametricOrientedEdge",
                StepOrientedEdge.class,
                StepEntity.class,
                Class.forName("com.minicad.app.StepPreviewJsonExporter$ParametricSurfaceMapper"),
                StepCadBuilder.class
        );
        sampleParametricOrientedEdge.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Object> points = (List<Object>) sampleParametricOrientedEdge.invoke(null, resolved.get(39), resolved.get(7), mapper, builder);
        assertNotNull(points);
        assertTrue(points.size() >= 8);

        Method u = points.getFirst().getClass().getDeclaredMethod("u");
        Method v = points.getFirst().getClass().getDeclaredMethod("v");
        assertEquals(0.0, (double) u.invoke(points.getFirst()), 1.0e-9);
        assertEquals(0.0, (double) v.invoke(points.getFirst()), 1.0e-9);
        assertEquals(0.0, (double) u.invoke(points.getLast()), 1.0e-9);
        assertEquals(1.0, (double) v.invoke(points.getLast()), 1.0e-9);
    }

    @Test
    void shouldMatchPcurvesThroughReplicaWrappedSurfaceCurveGeometry() throws Exception {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=DIRECTION('DZ',(0.0,0.0,1.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX0',#1,#3,#4);
                #6=CYLINDRICAL_SURFACE('CY0',#5,1.0);
                #10=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(1.0,0.0,1.0));
                #12=VERTEX_POINT('V0',#10);
                #13=VERTEX_POINT('V1',#11);
                #14=DIRECTION('DU',(0.0,0.0,1.0));
                #15=VECTOR('VU',#14,1.0);
                #16=LINE('L0',#10,#15);
                #20=CARTESIAN_POINT('UV0',(0.0,0.0));
                #21=DIRECTION('DV',(0.0,1.0));
                #22=VECTOR('VV',#21,1.0);
                #23=LINE('UVL0',#20,#22);
                #24=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #25=DEFINITIONAL_REPRESENTATION('DEF0',(#23),#24);
                #26=PCURVE('PC0',#6,#25);
                #27=SURFACE_CURVE('SC0',#16,(#26),.PCURVE_S1.);
                #28=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#1,1.0,$);
                #29=CURVE_REPLICA('CR0',#27,#28);
                #30=EDGE_CURVE('E0',#12,#13,#29,.T.);
                #31=ORIENTED_EDGE('OE0',$,$,#30,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        Method mapperForSurface = StepPreviewJsonExporter.class.getDeclaredMethod(
                "mapperForSurface",
                StepEntity.class,
                StepCadBuilder.class
        );
        mapperForSurface.setAccessible(true);
        Object mapper = mapperForSurface.invoke(null, resolved.get(6), builder);
        assertNotNull(mapper);

        Method sampleParametricOrientedEdge = StepPreviewJsonExporter.class.getDeclaredMethod(
                "sampleParametricOrientedEdge",
                StepOrientedEdge.class,
                StepEntity.class,
                Class.forName("com.minicad.app.StepPreviewJsonExporter$ParametricSurfaceMapper"),
                StepCadBuilder.class
        );
        sampleParametricOrientedEdge.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Object> points = (List<Object>) sampleParametricOrientedEdge.invoke(null, resolved.get(31), resolved.get(6), mapper, builder);
        assertNotNull(points);
        assertTrue(points.size() >= 8);

        Method u = points.getFirst().getClass().getDeclaredMethod("u");
        Method v = points.getFirst().getClass().getDeclaredMethod("v");
        assertEquals(0.0, (double) u.invoke(points.getFirst()), 1.0e-9);
        assertEquals(0.0, (double) v.invoke(points.getFirst()), 1.0e-9);
        assertEquals(0.0, (double) u.invoke(points.getLast()), 1.0e-9);
        assertEquals(1.0, (double) v.invoke(points.getLast()), 1.0e-9);
    }

    @Test
    void shouldMatchPcurvesThroughTerminatorWrappedSurfaceCurveGeometry() throws Exception {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=DIRECTION('DZ',(0.0,0.0,1.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX0',#1,#3,#4);
                #6=CYLINDRICAL_SURFACE('CY0',#5,1.0);
                #10=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(1.0,0.0,1.0));
                #12=VERTEX_POINT('V0',#10);
                #13=VERTEX_POINT('V1',#11);
                #14=DIRECTION('DU',(0.0,0.0,1.0));
                #15=VECTOR('VU',#14,1.0);
                #16=LINE('L0',#10,#15);
                #20=CARTESIAN_POINT('UV0',(0.0,0.0));
                #21=DIRECTION('DV',(0.0,1.0));
                #22=VECTOR('VV',#21,1.0);
                #23=LINE('UVL0',#20,#22);
                #24=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #25=DEFINITIONAL_REPRESENTATION('DEF0',(#23),#24);
                #26=PCURVE('PC0',#6,#25);
                #27=SURFACE_CURVE('SC0',#16,(#26),.PCURVE_S1.);
                #28=PRESENTATION_STYLE_ASSIGNMENT(());
                #29=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #30=DIRECTION('SD0',(1.0,0.0));
                #31=AXIS2_PLACEMENT_2D('MAP',#29,#30);
                #32=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #33=REPRESENTATION('SYMREP',(),#32);
                #34=SYMBOL_REPRESENTATION_MAP(#31,#33);
                #35=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #36=DIRECTION('SD1',(1.0,0.0));
                #37=AXIS2_PLACEMENT_2D('TGT',#35,#36);
                #38=ANNOTATION_SYMBOL('AS0',#34,#37);
                #39=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#28),#27);
                #40=TERMINATOR_SYMBOL('TS0',(#28),#38,#39);
                #41=EDGE_CURVE('E0',#12,#13,#40,.T.);
                #42=ORIENTED_EDGE('OE0',$,$,#41,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        Method mapperForSurface = StepPreviewJsonExporter.class.getDeclaredMethod(
                "mapperForSurface",
                StepEntity.class,
                StepCadBuilder.class
        );
        mapperForSurface.setAccessible(true);
        Object mapper = mapperForSurface.invoke(null, resolved.get(6), builder);
        assertNotNull(mapper);

        Method sampleParametricOrientedEdge = StepPreviewJsonExporter.class.getDeclaredMethod(
                "sampleParametricOrientedEdge",
                StepOrientedEdge.class,
                StepEntity.class,
                Class.forName("com.minicad.app.StepPreviewJsonExporter$ParametricSurfaceMapper"),
                StepCadBuilder.class
        );
        sampleParametricOrientedEdge.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Object> points = (List<Object>) sampleParametricOrientedEdge.invoke(null, resolved.get(42), resolved.get(6), mapper, builder);
        assertNotNull(points);
        assertTrue(points.size() >= 8);

        Method u = points.getFirst().getClass().getDeclaredMethod("u");
        Method v = points.getFirst().getClass().getDeclaredMethod("v");
        assertEquals(0.0, (double) u.invoke(points.getFirst()), 1.0e-9);
        assertEquals(0.0, (double) v.invoke(points.getFirst()), 1.0e-9);
        assertEquals(0.0, (double) u.invoke(points.getLast()), 1.0e-9);
        assertEquals(1.0, (double) v.invoke(points.getLast()), 1.0e-9);
    }

    @Test
    void shouldPickMatchingPcurveThroughReplicaWrappedSeamCurveGeometry() throws Exception {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=DIRECTION('DZ',(0.0,0.0,1.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX0',#1,#3,#4);
                #6=AXIS2_PLACEMENT_3D('AX1',#2,#3,#4);
                #7=CONICAL_SURFACE('CN0',#5,1.0,0.4636476090008061);
                #8=CONICAL_SURFACE('CN1',#6,1.0,0.4636476090008061);
                #10=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(2.0,0.0,1.0));
                #12=VERTEX_POINT('V0',#10);
                #13=VERTEX_POINT('V1',#11);
                #14=DIRECTION('DU',(1.0,0.0,1.0));
                #15=VECTOR('VU',#14,1.0);
                #16=LINE('L0',#10,#15);
                #20=CARTESIAN_POINT('UV0',(0.0,0.0));
                #21=DIRECTION('DV0',(0.0,1.0));
                #22=VECTOR('VV0',#21,1.0);
                #23=LINE('GOOD',#20,#22);
                #24=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #25=DEFINITIONAL_REPRESENTATION('DEF0',(#23),#24);
                #26=PCURVE('PC0',#7,#25);
                #27=CARTESIAN_POINT('UV1',(3.141592653589793,0.0));
                #28=DIRECTION('DV1',(0.0,1.0));
                #29=VECTOR('VV1',#28,1.0);
                #30=LINE('BAD',#27,#29);
                #31=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #32=DEFINITIONAL_REPRESENTATION('DEF1',(#30),#31);
                #33=PCURVE('PC1',#8,#32);
                #34=SEAM_CURVE('SEAM0',#16,(#33,#26),.PCURVE_S1.);
                #35=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#1,1.0,$);
                #36=CURVE_REPLICA('CR0',#34,#35);
                #37=EDGE_CURVE('E0',#12,#13,#36,.T.);
                #38=ORIENTED_EDGE('OE0',$,$,#37,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        Method mapperForSurface = StepPreviewJsonExporter.class.getDeclaredMethod(
                "mapperForSurface",
                StepEntity.class,
                StepCadBuilder.class
        );
        mapperForSurface.setAccessible(true);
        Object mapper = mapperForSurface.invoke(null, resolved.get(7), builder);
        assertNotNull(mapper);

        Method sampleParametricOrientedEdge = StepPreviewJsonExporter.class.getDeclaredMethod(
                "sampleParametricOrientedEdge",
                StepOrientedEdge.class,
                StepEntity.class,
                Class.forName("com.minicad.app.StepPreviewJsonExporter$ParametricSurfaceMapper"),
                StepCadBuilder.class
        );
        sampleParametricOrientedEdge.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Object> points = (List<Object>) sampleParametricOrientedEdge.invoke(null, resolved.get(38), resolved.get(7), mapper, builder);
        assertNotNull(points);
        assertTrue(points.size() >= 8);

        Method u = points.getFirst().getClass().getDeclaredMethod("u");
        Method v = points.getFirst().getClass().getDeclaredMethod("v");
        assertEquals(0.0, (double) u.invoke(points.getFirst()), 1.0e-9);
        assertEquals(0.0, (double) v.invoke(points.getFirst()), 1.0e-9);
        assertEquals(0.0, (double) u.invoke(points.getLast()), 1.0e-9);
        assertEquals(1.0, (double) v.invoke(points.getLast()), 1.0e-9);
    }

    @Test
    void shouldPickMatchingPcurveThroughTerminatorWrappedSeamCurveGeometry() throws Exception {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=DIRECTION('DZ',(0.0,0.0,1.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX0',#1,#3,#4);
                #6=AXIS2_PLACEMENT_3D('AX1',#2,#3,#4);
                #7=CONICAL_SURFACE('CN0',#5,1.0,0.4636476090008061);
                #8=CONICAL_SURFACE('CN1',#6,1.0,0.4636476090008061);
                #10=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(2.0,0.0,1.0));
                #12=VERTEX_POINT('V0',#10);
                #13=VERTEX_POINT('V1',#11);
                #14=DIRECTION('DU',(1.0,0.0,1.0));
                #15=VECTOR('VU',#14,1.0);
                #16=LINE('L0',#10,#15);
                #20=CARTESIAN_POINT('UV0',(0.0,0.0));
                #21=DIRECTION('DV0',(0.0,1.0));
                #22=VECTOR('VV0',#21,1.0);
                #23=LINE('GOOD',#20,#22);
                #24=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #25=DEFINITIONAL_REPRESENTATION('DEF0',(#23),#24);
                #26=PCURVE('PC0',#7,#25);
                #27=CARTESIAN_POINT('UV1',(3.141592653589793,0.0));
                #28=DIRECTION('DV1',(0.0,1.0));
                #29=VECTOR('VV1',#28,1.0);
                #30=LINE('BAD',#27,#29);
                #31=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #32=DEFINITIONAL_REPRESENTATION('DEF1',(#30),#31);
                #33=PCURVE('PC1',#8,#32);
                #34=SEAM_CURVE('SEAM0',#16,(#33,#26),.PCURVE_S1.);
                #35=PRESENTATION_STYLE_ASSIGNMENT(());
                #36=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #37=DIRECTION('SD0',(1.0,0.0));
                #38=AXIS2_PLACEMENT_2D('MAP',#36,#37);
                #39=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #40=REPRESENTATION('SYMREP',(),#39);
                #41=SYMBOL_REPRESENTATION_MAP(#38,#40);
                #42=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #43=DIRECTION('SD1',(1.0,0.0));
                #44=AXIS2_PLACEMENT_2D('TGT',#42,#43);
                #45=ANNOTATION_SYMBOL('AS0',#41,#44);
                #46=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#35),#34);
                #47=TERMINATOR_SYMBOL('TS0',(#35),#45,#46);
                #48=EDGE_CURVE('E0',#12,#13,#47,.T.);
                #49=ORIENTED_EDGE('OE0',$,$,#48,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        Method mapperForSurface = StepPreviewJsonExporter.class.getDeclaredMethod(
                "mapperForSurface",
                StepEntity.class,
                StepCadBuilder.class
        );
        mapperForSurface.setAccessible(true);
        Object mapper = mapperForSurface.invoke(null, resolved.get(7), builder);
        assertNotNull(mapper);

        Method sampleParametricOrientedEdge = StepPreviewJsonExporter.class.getDeclaredMethod(
                "sampleParametricOrientedEdge",
                StepOrientedEdge.class,
                StepEntity.class,
                Class.forName("com.minicad.app.StepPreviewJsonExporter$ParametricSurfaceMapper"),
                StepCadBuilder.class
        );
        sampleParametricOrientedEdge.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Object> points = (List<Object>) sampleParametricOrientedEdge.invoke(null, resolved.get(49), resolved.get(7), mapper, builder);
        assertNotNull(points);
        assertTrue(points.size() >= 8);

        Method u = points.getFirst().getClass().getDeclaredMethod("u");
        Method v = points.getFirst().getClass().getDeclaredMethod("v");
        assertEquals(0.0, (double) u.invoke(points.getFirst()), 1.0e-9);
        assertEquals(0.0, (double) v.invoke(points.getFirst()), 1.0e-9);
        assertEquals(0.0, (double) u.invoke(points.getLast()), 1.0e-9);
        assertEquals(1.0, (double) v.invoke(points.getLast()), 1.0e-9);
    }

    @Test
    void shouldExportCylindricalFaceUsingBsplinePcurve() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #4=CARTESIAN_POINT('B0',(-1.0,0.0,0.0));
                #5=CARTESIAN_POINT('A1',(1.0,0.0,1.0));
                #6=CARTESIAN_POINT('B1',(-1.0,0.0,1.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX0',#1,#10,#11);
                #13=AXIS2_PLACEMENT_3D('AX1',#2,#10,#11);
                #14=CYLINDRICAL_SURFACE('CY0',#12,1.0);
                #20=VERTEX_POINT('V0',#3);
                #21=VERTEX_POINT('V1',#4);
                #22=VERTEX_POINT('V2',#6);
                #23=VERTEX_POINT('V3',#5);
                #30=CIRCLE('C0',#12,1.0);
                #31=CIRCLE('C1',#13,1.0);
                #32=DIRECTION('DU',(1.0,0.0));
                #33=DIRECTION('DV',(0.0,1.0));
                #34=VECTOR('VU',#32,1.0);
                #35=VECTOR('VV',#33,1.0);
                #36=DIRECTION('UP3',(0.0,0.0,1.0));
                #37=VECTOR('VUP3',#36,1.0);
                #40=CARTESIAN_POINT('UV00',(0.0,0.0));
                #41=LINE('UVL0',#40,#34);
                #42=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #43=DEFINITIONAL_REPRESENTATION('DEF0',(#41),#42);
                #44=PCURVE('PC0',#14,#43);
                #45=SURFACE_CURVE('SC0',#30,(#44),.PCURVE_S1.);
                #46=CARTESIAN_POINT('UV10',(0.0,0.0));
                #47=CARTESIAN_POINT('UV11',(0.35,0.45));
                #48=CARTESIAN_POINT('UV12',(3.141592653589793,1.0));
                #49=(B_SPLINE_CURVE('B2D',2,(#46,#47,#48),.UNSPECIFIED.,.F.,.F.)
                     B_SPLINE_CURVE_WITH_KNOTS((3,3),(0.0,1.0),.UNSPECIFIED.));
                #50=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #51=DEFINITIONAL_REPRESENTATION('DEF1',(#49),#50);
                #52=PCURVE('PC1',#14,#51);
                #53=LINE('L0',#4,#37);
                #54=SURFACE_CURVE('SC1',#53,(#52),.PCURVE_S1.);
                #55=CARTESIAN_POINT('UV20',(0.0,1.0));
                #56=LINE('UVL2',#55,#34);
                #57=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #58=DEFINITIONAL_REPRESENTATION('DEF2',(#56),#57);
                #59=PCURVE('PC2',#14,#58);
                #60=SURFACE_CURVE('SC2',#31,(#59),.PCURVE_S1.);
                #61=CARTESIAN_POINT('UV30',(0.0,0.0));
                #62=LINE('UVL3',#61,#35);
                #63=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #64=DEFINITIONAL_REPRESENTATION('DEF3',(#62),#63);
                #65=PCURVE('PC3',#14,#64);
                #66=CARTESIAN_POINT('UV40',(6.283185307179586,0.0));
                #67=LINE('UVL4',#66,#35);
                #68=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #69=DEFINITIONAL_REPRESENTATION('DEF4',(#67),#68);
                #70=PCURVE('PC4',#14,#69);
                #71=LINE('L1',#3,#37);
                #72=SEAM_CURVE('SEAM0',#71,(#65,#70),.PCURVE_S1.);
                #73=EDGE_CURVE('E0',#20,#21,#45,.T.);
                #74=EDGE_CURVE('E1',#21,#22,#54,.T.);
                #75=EDGE_CURVE('E2',#22,#23,#60,.F.);
                #76=EDGE_CURVE('E3',#23,#20,#72,.F.);
                #80=ORIENTED_EDGE('OE0',$,$,#73,.T.);
                #81=ORIENTED_EDGE('OE1',$,$,#74,.T.);
                #82=ORIENTED_EDGE('OE2',$,$,#75,.T.);
                #83=ORIENTED_EDGE('OE3',$,$,#76,.T.);
                #90=EDGE_LOOP('L0',(#80,#81,#82,#83));
                #91=FACE_OUTER_BOUND('B0',#90,.T.);
                #92=ADVANCED_FACE('F0',(#91),#14,.T.);
                #93=OPEN_SHELL('OS',(#92));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"CYLINDRICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":4"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldExportCylindricalFaceWithInnerLoopHole() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=CARTESIAN_POINT('O03',(0.0,0.0,0.3));
                #4=CARTESIAN_POINT('O07',(0.0,0.0,0.7));
                #5=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #6=CARTESIAN_POINT('B0',(-1.0,0.0,0.0));
                #7=CARTESIAN_POINT('A1',(1.0,0.0,1.0));
                #8=CARTESIAN_POINT('B1',(-1.0,0.0,1.0));
                #9=CARTESIAN_POINT('I0',(0.5403023058681398,0.8414709848078965,0.3));
                #10=CARTESIAN_POINT('I1',(-0.4161468365471424,0.9092974268256817,0.3));
                #11=CARTESIAN_POINT('I2',(-0.4161468365471424,0.9092974268256817,0.7));
                #12=CARTESIAN_POINT('I3',(0.5403023058681398,0.8414709848078965,0.7));
                #20=DIRECTION('DZ',(0.0,0.0,1.0));
                #21=DIRECTION('DX',(1.0,0.0,0.0));
                #22=DIRECTION('DU',(1.0,0.0));
                #23=DIRECTION('DV',(0.0,1.0));
                #24=DIRECTION('UP3',(0.0,0.0,1.0));
                #25=AXIS2_PLACEMENT_3D('AX0',#1,#20,#21);
                #26=AXIS2_PLACEMENT_3D('AX1',#2,#20,#21);
                #27=AXIS2_PLACEMENT_3D('AX03',#3,#20,#21);
                #28=AXIS2_PLACEMENT_3D('AX07',#4,#20,#21);
                #29=CYLINDRICAL_SURFACE('CY0',#25,1.0);
                #30=CIRCLE('C0',#25,1.0);
                #31=CIRCLE('C1',#26,1.0);
                #32=CIRCLE('C03',#27,1.0);
                #33=CIRCLE('C07',#28,1.0);
                #34=VECTOR('VU',#22,1.0);
                #35=VECTOR('VV',#23,1.0);
                #36=VECTOR('VUP3',#24,1.0);
                #40=VERTEX_POINT('V0',#5);
                #41=VERTEX_POINT('V1',#6);
                #42=VERTEX_POINT('V2',#8);
                #43=VERTEX_POINT('V3',#7);
                #44=VERTEX_POINT('V4',#9);
                #45=VERTEX_POINT('V5',#10);
                #46=VERTEX_POINT('V6',#11);
                #47=VERTEX_POINT('V7',#12);
                #50=CARTESIAN_POINT('UV00',(0.0,0.0));
                #51=LINE('UVL0',#50,#34);
                #52=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #53=DEFINITIONAL_REPRESENTATION('DEF0',(#51),#52);
                #54=PCURVE('PC0',#29,#53);
                #55=SURFACE_CURVE('SC0',#30,(#54),.PCURVE_S1.);
                #56=CARTESIAN_POINT('UV10',(3.141592653589793,0.0));
                #57=LINE('UVL1',#56,#35);
                #58=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #59=DEFINITIONAL_REPRESENTATION('DEF1',(#57),#58);
                #60=PCURVE('PC1',#29,#59);
                #61=LINE('L0',#6,#36);
                #62=SURFACE_CURVE('SC1',#61,(#60),.PCURVE_S1.);
                #63=CARTESIAN_POINT('UV20',(0.0,1.0));
                #64=LINE('UVL2',#63,#34);
                #65=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #66=DEFINITIONAL_REPRESENTATION('DEF2',(#64),#65);
                #67=PCURVE('PC2',#29,#66);
                #68=SURFACE_CURVE('SC2',#31,(#67),.PCURVE_S1.);
                #69=CARTESIAN_POINT('UV30',(0.0,0.0));
                #70=LINE('UVL3',#69,#35);
                #71=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #72=DEFINITIONAL_REPRESENTATION('DEF3',(#70),#71);
                #73=PCURVE('PC3',#29,#72);
                #74=CARTESIAN_POINT('UV31',(6.283185307179586,0.0));
                #75=LINE('UVL4',#74,#35);
                #76=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #77=DEFINITIONAL_REPRESENTATION('DEF4',(#75),#76);
                #78=PCURVE('PC4',#29,#77);
                #79=LINE('L1',#5,#36);
                #80=SEAM_CURVE('SEAM0',#79,(#73,#78),.PCURVE_S1.);
                #81=EDGE_CURVE('E0',#40,#41,#55,.T.);
                #82=EDGE_CURVE('E1',#41,#42,#62,.T.);
                #83=EDGE_CURVE('E2',#42,#43,#68,.F.);
                #84=EDGE_CURVE('E3',#43,#40,#80,.F.);
                #85=ORIENTED_EDGE('OE0',$,$,#81,.T.);
                #86=ORIENTED_EDGE('OE1',$,$,#82,.T.);
                #87=ORIENTED_EDGE('OE2',$,$,#83,.T.);
                #88=ORIENTED_EDGE('OE3',$,$,#84,.T.);
                #89=EDGE_LOOP('LOUT',(#85,#86,#87,#88));
                #90=FACE_OUTER_BOUND('BOUT',#89,.T.);
                #91=CARTESIAN_POINT('UV40',(1.0,0.3));
                #92=LINE('UVL5',#91,#34);
                #93=REPRESENTATION_CONTEXT('PC5','PARAMETRIC');
                #94=DEFINITIONAL_REPRESENTATION('DEF5',(#92),#93);
                #95=PCURVE('PC5',#29,#94);
                #96=SURFACE_CURVE('SC3',#32,(#95),.PCURVE_S1.);
                #97=CARTESIAN_POINT('UV50',(2.0,0.3));
                #98=LINE('UVL6',#97,#35);
                #99=REPRESENTATION_CONTEXT('PC6','PARAMETRIC');
                #100=DEFINITIONAL_REPRESENTATION('DEF6',(#98),#99);
                #101=PCURVE('PC6',#29,#100);
                #102=LINE('L2',#10,#36);
                #103=SURFACE_CURVE('SC4',#102,(#101),.PCURVE_S1.);
                #104=CARTESIAN_POINT('UV60',(1.0,0.7));
                #105=LINE('UVL7',#104,#34);
                #106=REPRESENTATION_CONTEXT('PC7','PARAMETRIC');
                #107=DEFINITIONAL_REPRESENTATION('DEF7',(#105),#106);
                #108=PCURVE('PC7',#29,#107);
                #109=SURFACE_CURVE('SC5',#33,(#108),.PCURVE_S1.);
                #110=CARTESIAN_POINT('UV70',(1.0,0.3));
                #111=LINE('UVL8',#110,#35);
                #112=REPRESENTATION_CONTEXT('PC8','PARAMETRIC');
                #113=DEFINITIONAL_REPRESENTATION('DEF8',(#111),#112);
                #114=PCURVE('PC8',#29,#113);
                #115=LINE('L3',#9,#36);
                #116=SURFACE_CURVE('SC6',#115,(#114),.PCURVE_S1.);
                #117=EDGE_CURVE('E4',#44,#45,#96,.T.);
                #118=EDGE_CURVE('E5',#45,#46,#103,.T.);
                #119=EDGE_CURVE('E6',#46,#47,#109,.F.);
                #120=EDGE_CURVE('E7',#47,#44,#116,.F.);
                #121=ORIENTED_EDGE('OE4',$,$,#117,.T.);
                #122=ORIENTED_EDGE('OE5',$,$,#118,.T.);
                #123=ORIENTED_EDGE('OE6',$,$,#119,.T.);
                #124=ORIENTED_EDGE('OE7',$,$,#120,.T.);
                #125=EDGE_LOOP('LIN',(#121,#122,#123,#124));
                #126=FACE_BOUND('BIN',#125,.T.);
                #127=ADVANCED_FACE('F0',(#90,#126),#29,.T.);
                #128=OPEN_SHELL('OS',(#127));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"CYLINDRICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":8"));
        assertTrue(json.contains("\"outer\":false"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldExportConicalFaceWithInnerLoopHole() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('A0',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('B0',(-1.0,0.0,0.0));
                #4=CARTESIAN_POINT('A1',(1.5,0.0,1.0));
                #5=CARTESIAN_POINT('B1',(-1.5,0.0,1.0));
                #6=CARTESIAN_POINT('O03',(0.0,0.0,0.3));
                #7=CARTESIAN_POINT('O07',(0.0,0.0,0.7));
                #8=CARTESIAN_POINT('I0',(0.6213476517483607,0.967691632529081,0.3));
                #9=CARTESIAN_POINT('I1',(-0.47856886102921373,1.045691040849534,0.3));
                #10=CARTESIAN_POINT('I2',(-0.5617982293386423,1.2275515262146704,0.7));
                #11=CARTESIAN_POINT('I3',(0.7294081129219887,1.1359858294906603,0.7));
                #20=DIRECTION('DZ',(0.0,0.0,1.0));
                #21=DIRECTION('DX',(1.0,0.0,0.0));
                #22=DIRECTION('DU',(1.0,0.0));
                #23=DIRECTION('DV',(0.0,1.0));
                #24=AXIS2_PLACEMENT_3D('AX0',#1,#20,#21);
                #25=AXIS2_PLACEMENT_3D('AX1',#4,#20,#21);
                #26=AXIS2_PLACEMENT_3D('AX03',#6,#20,#21);
                #27=AXIS2_PLACEMENT_3D('AX07',#7,#20,#21);
                #28=CONICAL_SURFACE('CN0',#24,1.0,0.4636476090008061);
                #29=CIRCLE('C0',#24,1.0);
                #30=CIRCLE('C1',#25,1.5);
                #31=CIRCLE('C03',#26,1.15);
                #32=CIRCLE('C07',#27,1.35);
                #33=VECTOR('VU',#22,1.0);
                #34=VECTOR('VV',#23,1.0);
                #35=DIRECTION('GA',(0.5,0.0,1.0));
                #36=DIRECTION('GB',(-0.5,0.0,1.0));
                #37=DIRECTION('GI0',(0.2701511529340699,0.42073549240394825,1.0));
                #38=DIRECTION('GI1',(-0.2080734182735712,0.45464871341284085,1.0));
                #39=VECTOR('VGA',#35,1.0);
                #40=VECTOR('VGB',#36,1.0);
                #41=VECTOR('VGI0',#37,0.8);
                #42=VECTOR('VGI1',#38,0.8);
                #50=VERTEX_POINT('V0',#2);
                #51=VERTEX_POINT('V1',#3);
                #52=VERTEX_POINT('V2',#5);
                #53=VERTEX_POINT('V3',#4);
                #54=VERTEX_POINT('V4',#8);
                #55=VERTEX_POINT('V5',#9);
                #56=VERTEX_POINT('V6',#10);
                #57=VERTEX_POINT('V7',#11);
                #60=CARTESIAN_POINT('UV00',(0.0,0.0));
                #61=LINE('UVL0',#60,#33);
                #62=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #63=DEFINITIONAL_REPRESENTATION('DEF0',(#61),#62);
                #64=PCURVE('PC0',#28,#63);
                #65=SURFACE_CURVE('SC0',#29,(#64),.PCURVE_S1.);
                #66=CARTESIAN_POINT('UV10',(3.141592653589793,0.0));
                #67=LINE('UVL1',#66,#34);
                #68=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #69=DEFINITIONAL_REPRESENTATION('DEF1',(#67),#68);
                #70=PCURVE('PC1',#28,#69);
                #71=LINE('L0',#3,#40);
                #72=SURFACE_CURVE('SC1',#71,(#70),.PCURVE_S1.);
                #73=CARTESIAN_POINT('UV20',(0.0,1.0));
                #74=LINE('UVL2',#73,#33);
                #75=REPRESENTATION_CONTEXT('PC2','PARAMETRIC');
                #76=DEFINITIONAL_REPRESENTATION('DEF2',(#74),#75);
                #77=PCURVE('PC2',#28,#76);
                #78=SURFACE_CURVE('SC2',#30,(#77),.PCURVE_S1.);
                #79=CARTESIAN_POINT('UV30',(0.0,0.0));
                #80=LINE('UVL3',#79,#34);
                #81=REPRESENTATION_CONTEXT('PC3','PARAMETRIC');
                #82=DEFINITIONAL_REPRESENTATION('DEF3',(#80),#81);
                #83=PCURVE('PC3',#28,#82);
                #84=CARTESIAN_POINT('UV31',(6.283185307179586,0.0));
                #85=LINE('UVL4',#84,#34);
                #86=REPRESENTATION_CONTEXT('PC4','PARAMETRIC');
                #87=DEFINITIONAL_REPRESENTATION('DEF4',(#85),#86);
                #88=PCURVE('PC4',#28,#87);
                #89=LINE('L1',#2,#39);
                #90=SEAM_CURVE('SEAM0',#89,(#83,#88),.PCURVE_S1.);
                #91=EDGE_CURVE('E0',#50,#51,#65,.T.);
                #92=EDGE_CURVE('E1',#51,#52,#72,.T.);
                #93=EDGE_CURVE('E2',#52,#53,#78,.F.);
                #94=EDGE_CURVE('E3',#53,#50,#90,.F.);
                #95=ORIENTED_EDGE('OE0',$,$,#91,.T.);
                #96=ORIENTED_EDGE('OE1',$,$,#92,.T.);
                #97=ORIENTED_EDGE('OE2',$,$,#93,.T.);
                #98=ORIENTED_EDGE('OE3',$,$,#94,.T.);
                #99=EDGE_LOOP('LOUT',(#95,#96,#97,#98));
                #100=FACE_OUTER_BOUND('BOUT',#99,.T.);
                #101=CARTESIAN_POINT('UV40',(1.0,0.3));
                #102=LINE('UVL5',#101,#33);
                #103=REPRESENTATION_CONTEXT('PC5','PARAMETRIC');
                #104=DEFINITIONAL_REPRESENTATION('DEF5',(#102),#103);
                #105=PCURVE('PC5',#28,#104);
                #106=SURFACE_CURVE('SC3',#31,(#105),.PCURVE_S1.);
                #107=CARTESIAN_POINT('UV50',(2.0,0.3));
                #108=LINE('UVL6',#107,#34);
                #109=REPRESENTATION_CONTEXT('PC6','PARAMETRIC');
                #110=DEFINITIONAL_REPRESENTATION('DEF6',(#108),#109);
                #111=PCURVE('PC6',#28,#110);
                #112=LINE('L2',#9,#42);
                #113=SURFACE_CURVE('SC4',#112,(#111),.PCURVE_S1.);
                #114=CARTESIAN_POINT('UV60',(1.0,0.7));
                #115=LINE('UVL7',#114,#33);
                #116=REPRESENTATION_CONTEXT('PC7','PARAMETRIC');
                #117=DEFINITIONAL_REPRESENTATION('DEF7',(#115),#116);
                #118=PCURVE('PC7',#28,#117);
                #119=SURFACE_CURVE('SC5',#32,(#118),.PCURVE_S1.);
                #120=CARTESIAN_POINT('UV70',(1.0,0.3));
                #121=LINE('UVL8',#120,#34);
                #122=REPRESENTATION_CONTEXT('PC8','PARAMETRIC');
                #123=DEFINITIONAL_REPRESENTATION('DEF8',(#121),#122);
                #124=PCURVE('PC8',#28,#123);
                #125=LINE('L3',#8,#41);
                #126=SURFACE_CURVE('SC6',#125,(#124),.PCURVE_S1.);
                #127=EDGE_CURVE('E4',#54,#55,#106,.T.);
                #128=EDGE_CURVE('E5',#55,#56,#113,.T.);
                #129=EDGE_CURVE('E6',#56,#57,#119,.F.);
                #130=EDGE_CURVE('E7',#57,#54,#126,.F.);
                #131=ORIENTED_EDGE('OE4',$,$,#127,.T.);
                #132=ORIENTED_EDGE('OE5',$,$,#128,.T.);
                #133=ORIENTED_EDGE('OE6',$,$,#129,.T.);
                #134=ORIENTED_EDGE('OE7',$,$,#130,.T.);
                #135=EDGE_LOOP('LIN',(#131,#132,#133,#134));
                #136=FACE_BOUND('BIN',#135,.T.);
                #137=ADVANCED_FACE('F0',(#100,#136),#28,.T.);
                #138=OPEN_SHELL('OS',(#137));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"CONICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":8"));
        assertTrue(json.contains("\"outer\":false"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
    }

    @Test
    void shouldExportBSplinePatchPreview() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/bspline-patch.step")));

        assertSingleSupportedFacePreview(json, "B_SPLINE_SURFACE_WITH_KNOTS", 4, true);
        assertTrue(json.contains("[2.0,2.0,1.0]"));
    }

    @Test
    void shouldExportAssemblyInstancesAndTransforms() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('ASM-001','Assembly','Assembly root',(#2));
                #4=PRODUCT('PRT-001','Component','Component part',(#2));
                #5=PRODUCT_DEFINITION_FORMATION('asm-v1','first',#3);
                #6=PRODUCT_DEFINITION_FORMATION('prt-v1','first',#4);
                #7=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #8=PRODUCT_DEFINITION('asm-def','assembly def',#5,#7);
                #9=PRODUCT_DEFINITION('prt-def','part def',#6,#7);
                #10=PRODUCT_DEFINITION_SHAPE('asm-shape','assembly shape',#8);
                #11=PRODUCT_DEFINITION_SHAPE('part-shape','part shape',#9);
                #12=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #13=CARTESIAN_POINT('T',(10.0,0.0,0.0));
                #14=DIRECTION('DZ',(0.0,0.0,1.0));
                #15=DIRECTION('DX',(1.0,0.0,0.0));
                #16=AXIS2_PLACEMENT_3D('AX0',#12,#14,#15);
                #17=AXIS2_PLACEMENT_3D('AX1',#13,#14,#15);
                #18=ITEM_DEFINED_TRANSFORMATION('move','translate x',#16,#17);
                #19=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #20=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #21=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #22=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #23=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #24=VERTEX_POINT('V0',#20);
                #25=VERTEX_POINT('V1',#21);
                #26=VERTEX_POINT('V2',#22);
                #27=VERTEX_POINT('V3',#23);
                #28=DIRECTION('D1',(1.0,0.0,0.0));
                #29=VECTOR('VE1',#28,1.0);
                #30=LINE('L1',#20,#29);
                #31=DIRECTION('D2',(0.0,1.0,0.0));
                #32=VECTOR('VE2',#31,1.0);
                #33=LINE('L2',#21,#32);
                #34=DIRECTION('D3',(-1.0,0.0,0.0));
                #35=VECTOR('VE3',#34,1.0);
                #36=LINE('L3',#22,#35);
                #37=DIRECTION('D4',(0.0,-1.0,0.0));
                #38=VECTOR('VE4',#37,1.0);
                #39=LINE('L4',#23,#38);
                #40=EDGE_CURVE('E1',#24,#25,#30,.T.);
                #41=EDGE_CURVE('E2',#25,#26,#33,.T.);
                #42=EDGE_CURVE('E3',#26,#27,#36,.T.);
                #43=EDGE_CURVE('E4',#27,#24,#39,.T.);
                #44=ORIENTED_EDGE('OE1',$,$,#40,.T.);
                #45=ORIENTED_EDGE('OE2',$,$,#41,.T.);
                #46=ORIENTED_EDGE('OE3',$,$,#42,.T.);
                #47=ORIENTED_EDGE('OE4',$,$,#43,.T.);
                #48=EDGE_LOOP('LOOP',(#44,#45,#46,#47));
                #49=FACE_OUTER_BOUND('FOB',#48,.T.);
                #50=PLANE('PL',#16);
                #51=ADVANCED_FACE('F0',(#49),#50,.T.);
                #52=CLOSED_SHELL('CS',(#51));
                #53=MANIFOLD_SOLID_BREP('BODY',#52);
                #54=SHAPE_REPRESENTATION('ASM_REP',(),#19);
                #55=SHAPE_REPRESENTATION('PART_REP',(#53),#19);
                #56=SHAPE_DEFINITION_REPRESENTATION(#10,#54);
                #57=SHAPE_DEFINITION_REPRESENTATION(#11,#55);
                #58=(REPRESENTATION_RELATIONSHIP('rr','with transform',#54,#55)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#18));
                #59=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ-1','component 1','mounted',#8,#9);
                #60=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#58,#59);
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"representations\":[",
                "\"instances\":[",
                "\"representationId\":55",
                "\"occurrenceId\":59",
                "\"matrix\":[1.0,0.0,0.0,10.0");
    }

    @Test
    void shouldExportTranslatedPartAssemblyExample() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/translated-part-assembly.step")));

        assertJsonContains(json,
                "\"representations\":[",
                "\"instances\":[",
                "\"occurrenceId\":59",
                "\"matrix\":[1.0,0.0,0.0,12.0");
    }

    @Test
    void shouldExportTwoInstanceAssemblyExample() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/two-instance-assembly.step")));

        assertJsonContains(json,
                "\"instances\":[",
                "\"occurrenceId\":63",
                "\"occurrenceId\":64",
                "\"matrix\":[1.0,0.0,0.0,6.0",
                "\"matrix\":[1.0,0.0,0.0,0.0,0.0,1.0,0.0,5.0");
    }

    @Test
    void shouldExportNestedAssemblyExample() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/nested-assembly.step")));

        assertJsonContains(json,
                "\"instances\":[",
                "\"id\":\"pd-10\"",
                "\"occurrenceId\":69",
                "\"occurrenceId\":70",
                "\"parentId\":\"pd-10\"",
                "\"representationIds\":[63]",
                "\"localMatrix\":[1.0,0.0,0.0,0.0,0.0,1.0,0.0,4.0",
                "\"matrix\":[1.0,0.0,0.0,10.0,0.0,1.0,0.0,4.0");
    }

    @Test
    void shouldExportFaceBasedSurfaceModelPreviewMetadata() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #13=PLANE('PL',#12);
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
                #81=CONNECTED_FACE_SET('CFS',(#80));
                #82=(FACE_BASED_SURFACE_MODEL('FBSM',(#81)) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('fbsm-item'));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"shellCount\":1",
                "\"faceCount\":1",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportShellBackedFaceBasedSurfaceModelPreviewMetadata() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #13=PLANE('PL',#12);
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
                #81=OPEN_SHELL('OS0',(#80));
                #82=(FACE_BASED_SURFACE_MODEL('FBSM',(#81)) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('fbsm-item'));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"shellCount\":1",
                "\"faceCount\":1",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportWireframeAndGeometricCurveSetEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #4=VERTEX_POINT('V0',#1);
                #5=VERTEX_POINT('V1',#2);
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=VECTOR('VX',#6,1.0);
                #8=LINE('L0',#1,#7);
                #9=EDGE_CURVE('E0',#4,#5,#8,.T.);
                #10=CONNECTED_EDGE_SET('CES',(#9));
                #11=(EDGE_BASED_WIREFRAME_MODEL('WBM',(#10)) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('wire'));
                #12=POLYLINE('PL0',(#1,#2,#3));
                #13=GEOMETRIC_CURVE_SET('GCS',(#8,#12));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":3",
                "\"faceCount\":0",
                "\"unsupportedFaceCount\":0");
    }

    @Test
    void shouldExportOffsetCurvesAsStandaloneEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0));
                #2=DIRECTION('DX2',(1.0,0.0));
                #3=VECTOR('VX2',#2,1.0);
                #4=LINE('L2',#1,#3);
                #5=OFFSET_CURVE_2D('OC2',#4,0.5,.F.);
                #10=CARTESIAN_POINT('A',(0.0,0.0,0.0));
                #11=DIRECTION('DX3',(1.0,0.0,0.0));
                #12=VECTOR('VX3',#11,1.0);
                #13=LINE('L3',#10,#12);
                #14=DIRECTION('DZ',(0.0,0.0,1.0));
                #15=OFFSET_CURVE_3D('OC3',#13,0.5,.F.,#14);
                #20=GEOMETRIC_SET('GS',(#5,#15));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":2",
                "\"curve\":{\"stepId\":5,\"type\":\"OFFSET_CURVE_2D\",\"basisType\":\"LINE\",\"basisStepId\":4,\"offsetDistance\":0.5,\"selfIntersect\":false",
                "\"curve\":{\"stepId\":15,\"type\":\"OFFSET_CURVE_3D\",\"basisType\":\"LINE\",\"basisStepId\":13,\"offsetDistance\":0.5,\"selfIntersect\":false,\"refDirection\":[0.0,0.0,1.0]",
                "[0.0,0.5,0.0]",
                "[1.0,0.5,0.0]",
                "[0.0,-0.5,0.0]",
                "[1.0,-0.5,0.0]");
    }

    @Test
    void shouldExportSurfaceCurveAssociationMetadata() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX0',#1,#2,#3);
                #5=CYLINDRICAL_SURFACE('CY0',#4,1.0);
                #6=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #7=DIRECTION('DU',(0.0,0.0,1.0));
                #8=VECTOR('VU',#7,1.0);
                #9=LINE('L0',#6,#8);
                #10=CARTESIAN_POINT('UV0',(0.0,0.0));
                #11=DIRECTION('DV',(0.0,1.0));
                #12=VECTOR('VV',#11,1.0);
                #13=LINE('UVL0',#10,#12);
                #14=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #15=DEFINITIONAL_REPRESENTATION('DEF0',(#13),#14);
                #16=PCURVE('PC0',#5,#15);
                #17=SURFACE_CURVE('SC0',#9,(#16),.PCURVE_S1.);
                #18=GEOMETRIC_CURVE_SET('GCS0',(#17));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"curve\":{\"stepId\":17,\"type\":\"SURFACE_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":9",
                "\"masterRepresentation\":\"PCURVE_S1\"",
                "\"associatedSurfaceTypes\":[\"CYLINDRICAL_SURFACE\"]",
                "\"associatedSurfaceStepIds\":[5]");
    }

    @Test
    void shouldExportSeamCurveAssociationMetadata() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=DIRECTION('DZ',(0.0,0.0,1.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX0',#1,#3,#4);
                #6=AXIS2_PLACEMENT_3D('AX1',#2,#3,#4);
                #7=CONICAL_SURFACE('CN0',#5,1.0,0.4636476090008061);
                #8=CONICAL_SURFACE('CN1',#6,1.0,0.4636476090008061);
                #10=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #11=DIRECTION('DU',(1.0,0.0,1.0));
                #12=VECTOR('VU',#11,1.0);
                #13=LINE('L0',#10,#12);
                #20=CARTESIAN_POINT('UV0',(0.0,0.0));
                #21=DIRECTION('DV0',(0.0,1.0));
                #22=VECTOR('VV0',#21,1.0);
                #23=LINE('GOOD',#20,#22);
                #24=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #25=DEFINITIONAL_REPRESENTATION('DEF0',(#23),#24);
                #26=PCURVE('PC0',#7,#25);
                #27=CARTESIAN_POINT('UV1',(3.141592653589793,0.0));
                #28=DIRECTION('DV1',(0.0,1.0));
                #29=VECTOR('VV1',#28,1.0);
                #30=LINE('BAD',#27,#29);
                #31=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #32=DEFINITIONAL_REPRESENTATION('DEF1',(#30),#31);
                #33=PCURVE('PC1',#8,#32);
                #34=SEAM_CURVE('SEAM0',#13,(#33,#26),.PCURVE_S1.);
                #35=GEOMETRIC_CURVE_SET('GCS0',(#34));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"curve\":{\"stepId\":34,\"type\":\"SEAM_CURVE\",\"basisType\":\"LINE\",\"basisStepId\":13",
                "\"masterRepresentation\":\"PCURVE_S1\"",
                "\"associatedSurfaceTypes\":[\"CONICAL_SURFACE\",\"CONICAL_SURFACE\"]",
                "\"associatedSurfaceStepIds\":[8,7]");
    }

    @Test
    void shouldPropagateSurfaceCurveAssociationMetadataThroughProjectionWrapper() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX0',#1,#2,#3);
                #5=CYLINDRICAL_SURFACE('CY0',#4,1.0);
                #6=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #7=DIRECTION('DU',(0.0,0.0,1.0));
                #8=VECTOR('VU',#7,1.0);
                #9=LINE('L0',#6,#8);
                #10=CARTESIAN_POINT('UV0',(0.0,0.0));
                #11=DIRECTION('DV',(0.0,1.0));
                #12=VECTOR('VV',#11,1.0);
                #13=LINE('UVL0',#10,#12);
                #14=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #15=DEFINITIONAL_REPRESENTATION('DEF0',(#13),#14);
                #16=PCURVE('PC0',#5,#15);
                #17=SURFACE_CURVE('SC0',#9,(#16),.PCURVE_S1.);
                #18=PRESENTATION_STYLE_ASSIGNMENT(());
                #19=(PROJECTION_CURVE('PCW0',(#18),#17)
                    ANNOTATION_CURVE_OCCURRENCE('PCW0',(#18),#17)
                    STYLED_ITEM('PCW0',(#18),#17)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PCW0'));
                #20=GEOMETRIC_CURVE_SET('GCS0',(#19));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"curve\":{\"stepId\":19,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"SURFACE_CURVE\",\"basisStepId\":17",
                "\"masterRepresentation\":\"PCURVE_S1\"",
                "\"associatedSurfaceTypes\":[\"CYLINDRICAL_SURFACE\"]",
                "\"associatedSurfaceStepIds\":[5]");
    }

    @Test
    void shouldPropagateSeamCurveAssociationMetadataThroughProjectionWrapper() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('O1',(0.0,0.0,1.0));
                #3=DIRECTION('DZ',(0.0,0.0,1.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX0',#1,#3,#4);
                #6=AXIS2_PLACEMENT_3D('AX1',#2,#3,#4);
                #7=CONICAL_SURFACE('CN0',#5,1.0,0.4636476090008061);
                #8=CONICAL_SURFACE('CN1',#6,1.0,0.4636476090008061);
                #10=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #11=DIRECTION('DU',(1.0,0.0,1.0));
                #12=VECTOR('VU',#11,1.0);
                #13=LINE('L0',#10,#12);
                #20=CARTESIAN_POINT('UV0',(0.0,0.0));
                #21=DIRECTION('DV0',(0.0,1.0));
                #22=VECTOR('VV0',#21,1.0);
                #23=LINE('GOOD',#20,#22);
                #24=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #25=DEFINITIONAL_REPRESENTATION('DEF0',(#23),#24);
                #26=PCURVE('PC0',#7,#25);
                #27=CARTESIAN_POINT('UV1',(3.141592653589793,0.0));
                #28=DIRECTION('DV1',(0.0,1.0));
                #29=VECTOR('VV1',#28,1.0);
                #30=LINE('BAD',#27,#29);
                #31=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #32=DEFINITIONAL_REPRESENTATION('DEF1',(#30),#31);
                #33=PCURVE('PC1',#8,#32);
                #34=SEAM_CURVE('SEAM0',#13,(#33,#26),.PCURVE_S1.);
                #35=PRESENTATION_STYLE_ASSIGNMENT(());
                #36=(PROJECTION_CURVE('PCW0',(#35),#34)
                    ANNOTATION_CURVE_OCCURRENCE('PCW0',(#35),#34)
                    STYLED_ITEM('PCW0',(#35),#34)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PCW0'));
                #37=GEOMETRIC_CURVE_SET('GCS0',(#36));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"curve\":{\"stepId\":36,\"type\":\"PROJECTION_CURVE\",\"basisType\":\"SEAM_CURVE\",\"basisStepId\":34",
                "\"masterRepresentation\":\"PCURVE_S1\"",
                "\"associatedSurfaceTypes\":[\"CONICAL_SURFACE\",\"CONICAL_SURFACE\"]",
                "\"associatedSurfaceStepIds\":[8,7]");
    }

    @Test
    void shouldExportLinearExtrusionFaceThroughProjectedFallback() {
        String json = StepPreviewJsonExporter.export(linearExtrusionFaceStep(
                "",
                "#7",
                "0.0,0.0,0.0",
                "1.0,0.0,0.0",
                "1.0,0.0,1.0",
                "0.0,0.0,1.0"
        ));

        assertSingleSupportedFacePreview(json, "SURFACE_OF_LINEAR_EXTRUSION", 4, true);
    }

    @Test
    void shouldExportOrientedLinearExtrusionFaceThroughProjectedFallback() {
        String json = StepPreviewJsonExporter.export(linearExtrusionFaceStep(
                "#8=ORIENTED_SURFACE('OS0',#7,.T.);",
                "#8",
                "0.0,0.0,0.0",
                "1.0,0.0,0.0",
                "1.0,0.0,1.0",
                "0.0,0.0,1.0"
        ));

        assertSingleSupportedFacePreview(json, "ORIENTED_SURFACE", 4, true);
    }

    @Test
    void shouldExportCurveBoundedLinearExtrusionFaceThroughProjectedFallback() {
        String json = StepPreviewJsonExporter.export(linearExtrusionFaceStep(
                "#8=CURVE_BOUNDED_SURFACE('CBS0',#7,(#20,#21,#22,#23),.T.);",
                "#8",
                "0.0,0.0,0.0",
                "1.0,0.0,0.0",
                "1.0,0.0,1.0",
                "0.0,0.0,1.0"
        ));

        assertSingleSupportedFacePreview(json, "CURVE_BOUNDED_SURFACE", 4, true);
    }

    @Test
    void shouldExportRectangularTrimmedLinearExtrusionFaceThroughProjectedFallback() {
        String json = StepPreviewJsonExporter.export(linearExtrusionFaceStep(
                "#8=RECTANGULAR_TRIMMED_SURFACE('RTS0',#7,0.0,1.0,0.0,1.0,.T.,.T.);",
                "#8",
                "0.0,0.0,0.0",
                "1.0,0.0,0.0",
                "1.0,0.0,1.0",
                "0.0,0.0,1.0"
        ));

        assertSingleSupportedFacePreview(json, "RECTANGULAR_TRIMMED_SURFACE", 4, true);
    }

    @Test
    void shouldExportOffsetLinearExtrusionFaceThroughProjectedFallback() {
        String json = StepPreviewJsonExporter.export(linearExtrusionFaceStep(
                "#8=OFFSET_SURFACE('OFS0',#7,1.0,.F.);",
                "#8",
                "0.0,-1.0,0.0",
                "1.0,-1.0,0.0",
                "1.0,-1.0,1.0",
                "0.0,-1.0,1.0"
        ));

        assertSingleSupportedFacePreview(json, "OFFSET_SURFACE", 4, true);
    }

    @Test
    void shouldExportReplicaLinearExtrusionFaceThroughProjectedFallback() {
        String json = StepPreviewJsonExporter.export(linearExtrusionFaceStep(
                """
                #100=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #101=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#100,1.0,$);
                #102=SURFACE_REPLICA('SR0',#7,#101);
                """,
                "#102",
                "0.0,0.0,2.0",
                "1.0,0.0,2.0",
                "1.0,0.0,3.0",
                "0.0,0.0,3.0"
        ));

        assertSingleSupportedFacePreview(json, "SURFACE_REPLICA", 4, true);
    }

    @Test
    void shouldExportUniformScaledReplicaLinearExtrusionFaceThroughProjectedFallback() {
        String json = StepPreviewJsonExporter.export(linearExtrusionFaceStep(
                """
                #100=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #101=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#100,2.0,$);
                #102=SURFACE_REPLICA('SR0',#7,#101);
                """,
                "#102",
                "0.0,0.0,2.0",
                "2.0,0.0,2.0",
                "2.0,0.0,4.0",
                "0.0,0.0,4.0"
        ));

        assertSingleSupportedFacePreview(json, "SURFACE_REPLICA", 4, true);
    }

    @Test
    void shouldExportOffsetLinearExtrusionFaceUsingTerminatorWrappedEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=VECTOR('VZ',#4,1.0);
                #6=LINE('BASE',#1,#3);
                #7=SURFACE_OF_LINEAR_EXTRUSION('SLE0',#6,#5);
                #8=OFFSET_SURFACE('OFS0',#7,1.0,.F.);
                #10=CARTESIAN_POINT('P00',(0.0,-1.0,0.0));
                #11=CARTESIAN_POINT('P10',(1.0,-1.0,0.0));
                #12=CARTESIAN_POINT('P11',(1.0,-1.0,1.0));
                #13=CARTESIAN_POINT('P01',(0.0,-1.0,1.0));
                #14=VERTEX_POINT('V0',#10);
                #15=VERTEX_POINT('V1',#11);
                #16=VERTEX_POINT('V2',#12);
                #17=VERTEX_POINT('V3',#13);
                #20=LINE('L0',#10,#3);
                #21=LINE('L1',#11,#5);
                #22=LINE('L2',#13,#3);
                #23=LINE('L3',#10,#5);
                #24=PRESENTATION_STYLE_ASSIGNMENT(());
                #25=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #26=DIRECTION('SD0',(1.0,0.0));
                #27=AXIS2_PLACEMENT_2D('MAP',#25,#26);
                #28=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #29=REPRESENTATION('SYMREP',(),#28);
                #30=SYMBOL_REPRESENTATION_MAP(#27,#29);
                #31=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #32=DIRECTION('SD1',(1.0,0.0));
                #33=AXIS2_PLACEMENT_2D('TGT',#31,#32);
                #34=ANNOTATION_SYMBOL('AS0',#30,#33);
                #35=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#24),#20);
                #36=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#24),#23);
                #37=TERMINATOR_SYMBOL('TS0',(#24),#34,#35);
                #38=TERMINATOR_SYMBOL('TS1',(#24),#34,#36);
                #39=EDGE_CURVE('E0',#14,#15,#37,.T.);
                #40=EDGE_CURVE('E1',#15,#16,#21,.T.);
                #41=EDGE_CURVE('E2',#17,#16,#22,.T.);
                #42=EDGE_CURVE('E3',#14,#17,#38,.T.);
                #43=ORIENTED_EDGE('OE0',$,$,#39,.T.);
                #44=ORIENTED_EDGE('OE1',$,$,#40,.T.);
                #45=ORIENTED_EDGE('OE2',$,$,#41,.F.);
                #46=ORIENTED_EDGE('OE3',$,$,#42,.F.);
                #47=EDGE_LOOP('L0',(#43,#44,#45,#46));
                #48=FACE_OUTER_BOUND('B0',#47,.T.);
                #49=ADVANCED_FACE('F0',(#48),#8,.T.);
                #50=OPEN_SHELL('OS',(#49));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"OFFSET_SURFACE\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":6"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldExportReplicaLinearExtrusionFaceUsingTerminatorWrappedReplicaEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=VECTOR('VZ',#4,1.0);
                #6=LINE('BASE',#1,#3);
                #7=SURFACE_OF_LINEAR_EXTRUSION('SLE0',#6,#5);
                #8=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #9=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#8,1.0,$);
                #10=SURFACE_REPLICA('SR0',#7,#9);
                #11=CARTESIAN_POINT('P00',(0.0,0.0,2.0));
                #12=CARTESIAN_POINT('P10',(1.0,0.0,2.0));
                #13=CARTESIAN_POINT('P11',(1.0,0.0,3.0));
                #14=CARTESIAN_POINT('P01',(0.0,0.0,3.0));
                #15=VERTEX_POINT('V0',#11);
                #16=VERTEX_POINT('V1',#12);
                #17=VERTEX_POINT('V2',#13);
                #18=VERTEX_POINT('V3',#14);
                #20=LINE('L0',#1,#3);
                #21=LINE('L1',#12,#5);
                #22=LINE('L2',#1,#5);
                #23=CURVE_REPLICA('CR0',#20,#9);
                #24=CURVE_REPLICA('CR1',#22,#9);
                #25=PRESENTATION_STYLE_ASSIGNMENT(());
                #26=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #27=DIRECTION('SD0',(1.0,0.0));
                #28=AXIS2_PLACEMENT_2D('MAP',#26,#27);
                #29=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #30=REPRESENTATION('SYMREP',(),#29);
                #31=SYMBOL_REPRESENTATION_MAP(#28,#30);
                #32=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #33=DIRECTION('SD1',(1.0,0.0));
                #34=AXIS2_PLACEMENT_2D('TGT',#32,#33);
                #35=ANNOTATION_SYMBOL('AS0',#31,#34);
                #36=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#25),#23);
                #37=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#25),#24);
                #38=TERMINATOR_SYMBOL('TS0',(#25),#35,#36);
                #39=TERMINATOR_SYMBOL('TS1',(#25),#35,#37);
                #40=EDGE_CURVE('E0',#15,#16,#38,.T.);
                #41=EDGE_CURVE('E1',#16,#17,#21,.T.);
                #42=EDGE_CURVE('E2',#18,#17,#23,.T.);
                #43=EDGE_CURVE('E3',#15,#18,#39,.T.);
                #44=ORIENTED_EDGE('OE0',$,$,#40,.T.);
                #45=ORIENTED_EDGE('OE1',$,$,#41,.T.);
                #46=ORIENTED_EDGE('OE2',$,$,#42,.F.);
                #47=ORIENTED_EDGE('OE3',$,$,#43,.F.);
                #48=EDGE_LOOP('L0',(#44,#45,#46,#47));
                #49=FACE_OUTER_BOUND('B0',#48,.T.);
                #50=ADVANCED_FACE('F0',(#49),#10,.T.);
                #51=OPEN_SHELL('OS',(#50));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "SURFACE_REPLICA", 6, true);
    }

    @Test
    void shouldExportSurfaceOfRevolutionFaceThroughProjectedFallback() {
        String json = StepPreviewJsonExporter.export(surfaceOfRevolutionFaceStep(
                "",
                "#10",
                "1.0,0.0,0.0",
                "-1.0,0.0,0.0",
                "-1.0,0.0,1.0",
                "1.0,0.0,1.0"
        ));

        assertSingleSupportedFacePreview(json, "SURFACE_OF_REVOLUTION", 4, true);
    }

    @Test
    void shouldExportOrientedSurfaceOfRevolutionFaceThroughProjectedFallback() {
        String json = StepPreviewJsonExporter.export(surfaceOfRevolutionFaceStep(
                "#11=ORIENTED_SURFACE('OS0',#10,.T.);",
                "#11",
                "1.0,0.0,0.0",
                "-1.0,0.0,0.0",
                "-1.0,0.0,1.0",
                "1.0,0.0,1.0"
        ));

        assertSingleSupportedFacePreview(json, "ORIENTED_SURFACE", 4, true);
    }

    @Test
    void shouldExportCurveBoundedSurfaceOfRevolutionFaceThroughProjectedFallback() {
        String json = StepPreviewJsonExporter.export(surfaceOfRevolutionFaceStep(
                "#11=CURVE_BOUNDED_SURFACE('CBS0',#10,(#30,#31,#32,#33),.T.);",
                "#11",
                "1.0,0.0,0.0",
                "-1.0,0.0,0.0",
                "-1.0,0.0,1.0",
                "1.0,0.0,1.0"
        ));

        assertSingleSupportedFacePreview(json, "CURVE_BOUNDED_SURFACE", 4, true);
    }

    @Test
    void shouldExportRectangularTrimmedSurfaceOfRevolutionFaceThroughProjectedFallback() {
        String json = StepPreviewJsonExporter.export(surfaceOfRevolutionFaceStep(
                "#11=RECTANGULAR_TRIMMED_SURFACE('RTS0',#10,0.0,3.141592653589793,0.0,1.0,.T.,.T.);",
                "#11",
                "1.0,0.0,0.0",
                "-1.0,0.0,0.0",
                "-1.0,0.0,1.0",
                "1.0,0.0,1.0"
        ));

        assertSingleSupportedFacePreview(json, "RECTANGULAR_TRIMMED_SURFACE", 4, true);
    }

    @Test
    void shouldExportOffsetSurfaceOfRevolutionFaceThroughProjectedFallback() {
        String json = StepPreviewJsonExporter.export(surfaceOfRevolutionFaceStep(
                "#11=OFFSET_SURFACE('OFS0',#10,1.0,.F.);",
                "#11",
                "2.0,0.0,0.0",
                "-2.0,0.0,0.0",
                "-2.0,0.0,1.0",
                "2.0,0.0,1.0"
        ));

        assertSingleSupportedFacePreview(json, "OFFSET_SURFACE", 4, true);
    }

    @Test
    void shouldExportReplicaSurfaceOfRevolutionFaceThroughProjectedFallback() {
        String json = StepPreviewJsonExporter.export(surfaceOfRevolutionFaceStep(
                """
                #100=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #101=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#100,1.0,$);
                #102=SURFACE_REPLICA('SR0',#10,#101);
                """,
                "#102",
                "1.0,0.0,2.0",
                "-1.0,0.0,2.0",
                "-1.0,0.0,3.0",
                "1.0,0.0,3.0"
        ));

        assertSingleSupportedFacePreview(json, "SURFACE_REPLICA", 4, true);
    }

    @Test
    void shouldExportUniformScaledReplicaSurfaceOfRevolutionFaceThroughProjectedFallback() {
        String json = StepPreviewJsonExporter.export(surfaceOfRevolutionFaceStep(
                """
                #100=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #101=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#100,2.0,$);
                #102=SURFACE_REPLICA('SR0',#10,#101);
                """,
                "#102",
                "2.0,0.0,2.0",
                "-2.0,0.0,2.0",
                "-2.0,0.0,4.0",
                "2.0,0.0,4.0"
        ));

        assertSingleSupportedFacePreview(json, "SURFACE_REPLICA", 4, true);
    }

    @Test
    void shouldExportOffsetSurfaceOfRevolutionFaceUsingTerminatorWrappedEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('B0',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('B1',(1.0,0.0,1.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=DIRECTION('DX',(1.0,0.0,0.0));
                #6=AXIS1_PLACEMENT('AX1',#1,#4);
                #7=VECTOR('VZ',#4,1.0);
                #8=LINE('GEN',#2,#7);
                #9=AXIS2_PLACEMENT_3D('AC0',#1,#4,#5);
                #10=SURFACE_OF_REVOLUTION('SOR0',#8,#6);
                #11=OFFSET_SURFACE('OFS0',#10,1.0,.F.);
                #12=CARTESIAN_POINT('P00',(2.0,0.0,0.0));
                #13=CARTESIAN_POINT('P10',(-2.0,0.0,0.0));
                #14=CARTESIAN_POINT('P11',(-2.0,0.0,1.0));
                #15=CARTESIAN_POINT('P01',(2.0,0.0,1.0));
                #16=VERTEX_POINT('V0',#12);
                #17=VERTEX_POINT('V1',#13);
                #18=VERTEX_POINT('V2',#14);
                #19=VERTEX_POINT('V3',#15);
                #20=AXIS2_PLACEMENT_3D('AC1',#3,#4,#5);
                #30=CIRCLE('C0',#9,2.0);
                #31=LINE('L0',#13,#7);
                #32=CIRCLE('C1',#20,2.0);
                #33=LINE('L1',#12,#7);
                #34=PRESENTATION_STYLE_ASSIGNMENT(());
                #35=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #36=DIRECTION('SD0',(1.0,0.0));
                #37=AXIS2_PLACEMENT_2D('MAP',#35,#36);
                #38=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #39=REPRESENTATION('SYMREP',(),#38);
                #40=SYMBOL_REPRESENTATION_MAP(#37,#39);
                #41=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #42=DIRECTION('SD1',(1.0,0.0));
                #43=AXIS2_PLACEMENT_2D('TGT',#41,#42);
                #44=ANNOTATION_SYMBOL('AS0',#40,#43);
                #45=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#34),#30);
                #46=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#34),#33);
                #47=TERMINATOR_SYMBOL('TS0',(#34),#44,#45);
                #48=TERMINATOR_SYMBOL('TS1',(#34),#44,#46);
                #49=EDGE_CURVE('E0',#16,#17,#47,.T.);
                #50=EDGE_CURVE('E1',#17,#18,#31,.T.);
                #51=EDGE_CURVE('E2',#19,#18,#32,.T.);
                #52=EDGE_CURVE('E3',#16,#19,#48,.T.);
                #53=ORIENTED_EDGE('OE0',$,$,#49,.T.);
                #54=ORIENTED_EDGE('OE1',$,$,#50,.T.);
                #55=ORIENTED_EDGE('OE2',$,$,#51,.F.);
                #56=ORIENTED_EDGE('OE3',$,$,#52,.F.);
                #57=EDGE_LOOP('L0',(#53,#54,#55,#56));
                #58=FACE_OUTER_BOUND('B0',#57,.T.);
                #59=ADVANCED_FACE('F0',(#58),#11,.T.);
                #60=OPEN_SHELL('OS',(#59));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"OFFSET_SURFACE\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":6"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldExportReplicaSurfaceOfRevolutionFaceUsingTerminatorWrappedReplicaEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('B0',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('B1',(1.0,0.0,1.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=DIRECTION('DX',(1.0,0.0,0.0));
                #6=AXIS1_PLACEMENT('AX1',#1,#4);
                #7=VECTOR('VZ',#4,1.0);
                #8=LINE('GEN',#2,#7);
                #9=AXIS2_PLACEMENT_3D('AC0',#1,#4,#5);
                #10=SURFACE_OF_REVOLUTION('SOR0',#8,#6);
                #11=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #12=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#11,1.0,$);
                #13=SURFACE_REPLICA('SR0',#10,#12);
                #14=CARTESIAN_POINT('P00',(1.0,0.0,2.0));
                #15=CARTESIAN_POINT('P10',(-1.0,0.0,2.0));
                #16=CARTESIAN_POINT('P11',(-1.0,0.0,3.0));
                #17=CARTESIAN_POINT('P01',(1.0,0.0,3.0));
                #18=VERTEX_POINT('V0',#14);
                #19=VERTEX_POINT('V1',#15);
                #20=VERTEX_POINT('V2',#16);
                #21=VERTEX_POINT('V3',#17);
                #22=AXIS2_PLACEMENT_3D('AC1',#3,#4,#5);
                #30=CIRCLE('C0',#9,1.0);
                #31=LINE('L0',#15,#7);
                #32=CIRCLE('C1',#22,1.0);
                #33=LINE('L1',#14,#7);
                #34=CURVE_REPLICA('CR0',#30,#12);
                #35=CURVE_REPLICA('CR1',#32,#12);
                #36=CURVE_REPLICA('CR2',#33,#12);
                #37=PRESENTATION_STYLE_ASSIGNMENT(());
                #38=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #39=DIRECTION('SD0',(1.0,0.0));
                #40=AXIS2_PLACEMENT_2D('MAP',#38,#39);
                #41=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #42=REPRESENTATION('SYMREP',(),#41);
                #43=SYMBOL_REPRESENTATION_MAP(#40,#42);
                #44=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #45=DIRECTION('SD1',(1.0,0.0));
                #46=AXIS2_PLACEMENT_2D('TGT',#44,#45);
                #47=ANNOTATION_SYMBOL('AS0',#43,#46);
                #48=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#37),#34);
                #49=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#37),#36);
                #50=TERMINATOR_SYMBOL('TS0',(#37),#47,#48);
                #51=TERMINATOR_SYMBOL('TS1',(#37),#47,#49);
                #52=EDGE_CURVE('E0',#18,#19,#50,.T.);
                #53=EDGE_CURVE('E1',#19,#20,#31,.T.);
                #54=EDGE_CURVE('E2',#21,#20,#35,.T.);
                #55=EDGE_CURVE('E3',#18,#21,#51,.T.);
                #56=ORIENTED_EDGE('OE0',$,$,#52,.T.);
                #57=ORIENTED_EDGE('OE1',$,$,#53,.T.);
                #58=ORIENTED_EDGE('OE2',$,$,#54,.F.);
                #59=ORIENTED_EDGE('OE3',$,$,#55,.F.);
                #60=EDGE_LOOP('L0',(#56,#57,#58,#59));
                #61=FACE_OUTER_BOUND('B0',#60,.T.);
                #62=ADVANCED_FACE('F0',(#61),#13,.T.);
                #63=OPEN_SHELL('OS',(#62));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"SURFACE_REPLICA\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":8"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldExportOrientedBsplineSurfaceUsingTerminatorWrappedEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P10',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P01',(0.0,1.0,0.0));
                #4=CARTESIAN_POINT('P11',(1.0,1.0,1.0));
                #10=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.));
                #11=ORIENTED_SURFACE('OS0',#10,.T.);
                #20=VERTEX_POINT('V0',#1);
                #21=VERTEX_POINT('V1',#2);
                #22=VERTEX_POINT('V2',#4);
                #23=VERTEX_POINT('V3',#3);
                #30=DIRECTION('DX',(1.0,0.0,0.0));
                #31=VECTOR('VX',#30,1.0);
                #32=LINE('L0',#1,#31);
                #33=DIRECTION('DYZ',(0.0,1.0,1.0));
                #34=VECTOR('VYZ',#33,1.0);
                #35=LINE('L1',#2,#34);
                #36=DIRECTION('NDX',(-1.0,0.0,0.0));
                #37=VECTOR('NVX',#36,1.0);
                #38=LINE('L2',#4,#37);
                #39=DIRECTION('NDY',(0.0,-1.0,0.0));
                #40=VECTOR('NVY',#39,1.0);
                #41=LINE('L3',#3,#40);
                #42=PRESENTATION_STYLE_ASSIGNMENT(());
                #43=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #44=DIRECTION('SD0',(1.0,0.0));
                #45=AXIS2_PLACEMENT_2D('MAP',#43,#44);
                #46=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #47=REPRESENTATION('SYMREP',(),#46);
                #48=SYMBOL_REPRESENTATION_MAP(#45,#47);
                #49=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #50=DIRECTION('SD1',(1.0,0.0));
                #51=AXIS2_PLACEMENT_2D('TGT',#49,#50);
                #52=ANNOTATION_SYMBOL('AS0',#48,#51);
                #53=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#42),#32);
                #54=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#42),#41);
                #55=TERMINATOR_SYMBOL('TS0',(#42),#52,#53);
                #56=TERMINATOR_SYMBOL('TS1',(#42),#52,#54);
                #57=EDGE_CURVE('E0',#20,#21,#55,.T.);
                #58=EDGE_CURVE('E1',#21,#22,#35,.T.);
                #59=EDGE_CURVE('E2',#23,#22,#38,.T.);
                #60=EDGE_CURVE('E3',#20,#23,#56,.T.);
                #61=ORIENTED_EDGE('OE0',$,$,#57,.T.);
                #62=ORIENTED_EDGE('OE1',$,$,#58,.T.);
                #63=ORIENTED_EDGE('OE2',$,$,#59,.F.);
                #64=ORIENTED_EDGE('OE3',$,$,#60,.F.);
                #65=EDGE_LOOP('L0',(#61,#62,#63,#64));
                #66=FACE_OUTER_BOUND('B0',#65,.T.);
                #67=ADVANCED_FACE('F0',(#66),#11,.T.);
                #68=OPEN_SHELL('OS',(#67));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"ORIENTED_SURFACE\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":6"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldExportReplicaRationalBsplineSurfaceUsingTerminatorWrappedReplicaEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P10',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P01',(0.0,1.0,0.0));
                #4=CARTESIAN_POINT('P11',(1.0,1.0,1.0));
                #10=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.)
                     RATIONAL_B_SPLINE_SURFACE(((1.0,1.0),(1.0,1.0))));
                #11=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #12=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#11,1.0,$);
                #13=SURFACE_REPLICA('SR0',#10,#12);
                #20=CARTESIAN_POINT('Q00',(0.0,0.0,2.0));
                #21=CARTESIAN_POINT('Q10',(1.0,0.0,2.0));
                #22=CARTESIAN_POINT('Q11',(1.0,1.0,3.0));
                #23=CARTESIAN_POINT('Q01',(0.0,1.0,2.0));
                #24=VERTEX_POINT('V0',#20);
                #25=VERTEX_POINT('V1',#21);
                #26=VERTEX_POINT('V2',#22);
                #27=VERTEX_POINT('V3',#23);
                #30=DIRECTION('DX',(1.0,0.0,0.0));
                #31=VECTOR('VX',#30,1.0);
                #32=LINE('L0',#1,#31);
                #33=DIRECTION('DYZ',(0.0,1.0,1.0));
                #34=VECTOR('VYZ',#33,1.0);
                #35=LINE('L1',#2,#34);
                #36=DIRECTION('NDX',(-1.0,0.0,0.0));
                #37=VECTOR('NVX',#36,1.0);
                #38=LINE('L2',#4,#37);
                #39=DIRECTION('NDY',(0.0,-1.0,0.0));
                #40=VECTOR('NVY',#39,1.0);
                #41=LINE('L3',#3,#40);
                #42=CURVE_REPLICA('CR0',#32,#12);
                #43=CURVE_REPLICA('CR1',#35,#12);
                #44=CURVE_REPLICA('CR2',#38,#12);
                #45=CURVE_REPLICA('CR3',#41,#12);
                #46=PRESENTATION_STYLE_ASSIGNMENT(());
                #47=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #48=DIRECTION('SD0',(1.0,0.0));
                #49=AXIS2_PLACEMENT_2D('MAP',#47,#48);
                #50=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #51=REPRESENTATION('SYMREP',(),#50);
                #52=SYMBOL_REPRESENTATION_MAP(#49,#51);
                #53=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #54=DIRECTION('SD1',(1.0,0.0));
                #55=AXIS2_PLACEMENT_2D('TGT',#53,#54);
                #56=ANNOTATION_SYMBOL('AS0',#52,#55);
                #57=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#46),#42);
                #58=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#46),#45);
                #59=TERMINATOR_SYMBOL('TS0',(#46),#56,#57);
                #60=TERMINATOR_SYMBOL('TS1',(#46),#56,#58);
                #61=EDGE_CURVE('E0',#24,#25,#59,.T.);
                #62=EDGE_CURVE('E1',#25,#26,#43,.T.);
                #63=EDGE_CURVE('E2',#27,#26,#44,.T.);
                #64=EDGE_CURVE('E3',#24,#27,#60,.T.);
                #65=ORIENTED_EDGE('OE0',$,$,#61,.T.);
                #66=ORIENTED_EDGE('OE1',$,$,#62,.T.);
                #67=ORIENTED_EDGE('OE2',$,$,#63,.F.);
                #68=ORIENTED_EDGE('OE3',$,$,#64,.F.);
                #69=EDGE_LOOP('L0',(#65,#66,#67,#68));
                #70=FACE_OUTER_BOUND('B0',#69,.T.);
                #71=ADVANCED_FACE('F0',(#70),#13,.T.);
                #72=OPEN_SHELL('OS',(#71));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"SURFACE_REPLICA\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":6"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldExportOffsetBsplineSurfaceUsingTerminatorWrappedEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P10',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P01',(0.0,1.0,0.0));
                #4=CARTESIAN_POINT('P11',(1.0,1.0,1.0));
                #10=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.));
                #11=OFFSET_SURFACE('OFS0',#10,1.0,.F.);
                #20=CARTESIAN_POINT('Q00',(0.0,0.0,-1.0));
                #21=CARTESIAN_POINT('Q10',(1.0,0.0,-1.0));
                #22=CARTESIAN_POINT('Q11',(1.0,1.7071067811865475,0.29289321881345254));
                #23=CARTESIAN_POINT('Q01',(0.0,1.7071067811865475,-0.7071067811865475));
                #24=VERTEX_POINT('V0',#20);
                #25=VERTEX_POINT('V1',#21);
                #26=VERTEX_POINT('V2',#22);
                #27=VERTEX_POINT('V3',#23);
                #30=DIRECTION('DX',(1.0,0.0,0.0));
                #31=VECTOR('VX',#30,1.0);
                #32=LINE('L0',#20,#31);
                #33=DIRECTION('DYZ',(0.0,1.0,1.0));
                #34=VECTOR('VYZ',#33,1.0);
                #35=LINE('L1',#21,#34);
                #36=DIRECTION('NDX',(-1.0,0.0,0.0));
                #37=VECTOR('NVX',#36,1.0);
                #38=LINE('L2',#22,#37);
                #39=DIRECTION('NBACK',(0.0,-0.9238795325112867,-0.3826834323650898));
                #40=VECTOR('NVY',#39,1.0);
                #41=LINE('L3',#23,#40);
                #42=PRESENTATION_STYLE_ASSIGNMENT(());
                #43=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #44=DIRECTION('SD0',(1.0,0.0));
                #45=AXIS2_PLACEMENT_2D('MAP',#43,#44);
                #46=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #47=REPRESENTATION('SYMREP',(),#46);
                #48=SYMBOL_REPRESENTATION_MAP(#45,#47);
                #49=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #50=DIRECTION('SD1',(1.0,0.0));
                #51=AXIS2_PLACEMENT_2D('TGT',#49,#50);
                #52=ANNOTATION_SYMBOL('AS0',#48,#51);
                #53=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#42),#32);
                #54=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#42),#41);
                #55=TERMINATOR_SYMBOL('TS0',(#42),#52,#53);
                #56=TERMINATOR_SYMBOL('TS1',(#42),#52,#54);
                #57=EDGE_CURVE('E0',#24,#25,#55,.T.);
                #58=EDGE_CURVE('E1',#25,#26,#35,.T.);
                #59=EDGE_CURVE('E2',#27,#26,#38,.T.);
                #60=EDGE_CURVE('E3',#24,#27,#56,.T.);
                #61=ORIENTED_EDGE('OE0',$,$,#57,.T.);
                #62=ORIENTED_EDGE('OE1',$,$,#58,.T.);
                #63=ORIENTED_EDGE('OE2',$,$,#59,.F.);
                #64=ORIENTED_EDGE('OE3',$,$,#60,.F.);
                #65=EDGE_LOOP('L0',(#61,#62,#63,#64));
                #66=FACE_OUTER_BOUND('B0',#65,.T.);
                #67=ADVANCED_FACE('F0',(#66),#11,.T.);
                #68=OPEN_SHELL('OS',(#67));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"OFFSET_SURFACE\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":6"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldExportOffsetRationalBsplineSurfaceUsingTerminatorWrappedEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P10',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P01',(0.0,1.0,0.0));
                #4=CARTESIAN_POINT('P11',(1.0,1.0,1.0));
                #10=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.)
                     RATIONAL_B_SPLINE_SURFACE(((1.0,1.0),(1.0,1.0))));
                #11=OFFSET_SURFACE('OFS0',#10,1.0,.F.);
                #20=CARTESIAN_POINT('Q00',(0.0,0.0,-1.0));
                #21=CARTESIAN_POINT('Q10',(1.0,0.0,-1.0));
                #22=CARTESIAN_POINT('Q11',(1.0,1.7071067811865475,0.29289321881345254));
                #23=CARTESIAN_POINT('Q01',(0.0,1.7071067811865475,-0.7071067811865475));
                #24=VERTEX_POINT('V0',#20);
                #25=VERTEX_POINT('V1',#21);
                #26=VERTEX_POINT('V2',#22);
                #27=VERTEX_POINT('V3',#23);
                #30=DIRECTION('DX',(1.0,0.0,0.0));
                #31=VECTOR('VX',#30,1.0);
                #32=LINE('L0',#20,#31);
                #33=DIRECTION('DYZ',(0.0,1.0,1.0));
                #34=VECTOR('VYZ',#33,1.0);
                #35=LINE('L1',#21,#34);
                #36=DIRECTION('NDX',(-1.0,0.0,0.0));
                #37=VECTOR('NVX',#36,1.0);
                #38=LINE('L2',#22,#37);
                #39=DIRECTION('NBACK',(0.0,-0.9238795325112867,-0.3826834323650898));
                #40=VECTOR('NVY',#39,1.0);
                #41=LINE('L3',#23,#40);
                #42=PRESENTATION_STYLE_ASSIGNMENT(());
                #43=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #44=DIRECTION('SD0',(1.0,0.0));
                #45=AXIS2_PLACEMENT_2D('MAP',#43,#44);
                #46=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #47=REPRESENTATION('SYMREP',(),#46);
                #48=SYMBOL_REPRESENTATION_MAP(#45,#47);
                #49=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #50=DIRECTION('SD1',(1.0,0.0));
                #51=AXIS2_PLACEMENT_2D('TGT',#49,#50);
                #52=ANNOTATION_SYMBOL('AS0',#48,#51);
                #53=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#42),#32);
                #54=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#42),#41);
                #55=TERMINATOR_SYMBOL('TS0',(#42),#52,#53);
                #56=TERMINATOR_SYMBOL('TS1',(#42),#52,#54);
                #57=EDGE_CURVE('E0',#24,#25,#55,.T.);
                #58=EDGE_CURVE('E1',#25,#26,#35,.T.);
                #59=EDGE_CURVE('E2',#27,#26,#38,.T.);
                #60=EDGE_CURVE('E3',#24,#27,#56,.T.);
                #61=ORIENTED_EDGE('OE0',$,$,#57,.T.);
                #62=ORIENTED_EDGE('OE1',$,$,#58,.T.);
                #63=ORIENTED_EDGE('OE2',$,$,#59,.F.);
                #64=ORIENTED_EDGE('OE3',$,$,#60,.F.);
                #65=EDGE_LOOP('L0',(#61,#62,#63,#64));
                #66=FACE_OUTER_BOUND('B0',#65,.T.);
                #67=ADVANCED_FACE('F0',(#66),#11,.T.);
                #68=OPEN_SHELL('OS',(#67));
                ENDSEC;
                """);

        assertTrue(json.contains("\"surfaceType\":\"OFFSET_SURFACE\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":6"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    @Test
    void shouldExportRectangularTrimmedBsplineSurfaceUsingTerminatorWrappedEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P10',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P01',(0.0,1.0,0.0));
                #4=CARTESIAN_POINT('P11',(1.0,1.0,1.0));
                #10=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.));
                #11=RECTANGULAR_TRIMMED_SURFACE('RTS0',#10,0.0,1.0,0.0,1.0,.T.,.T.);
                #20=VERTEX_POINT('V0',#1);
                #21=VERTEX_POINT('V1',#2);
                #22=VERTEX_POINT('V2',#4);
                #23=VERTEX_POINT('V3',#3);
                #30=DIRECTION('DX',(1.0,0.0,0.0));
                #31=VECTOR('VX',#30,1.0);
                #32=LINE('L0',#1,#31);
                #33=DIRECTION('DYZ',(0.0,1.0,1.0));
                #34=VECTOR('VYZ',#33,1.0);
                #35=LINE('L1',#2,#34);
                #36=DIRECTION('NDX',(-1.0,0.0,0.0));
                #37=VECTOR('NVX',#36,1.0);
                #38=LINE('L2',#4,#37);
                #39=DIRECTION('NDY',(0.0,-1.0,0.0));
                #40=VECTOR('NVY',#39,1.0);
                #41=LINE('L3',#3,#40);
                #42=PRESENTATION_STYLE_ASSIGNMENT(());
                #43=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #44=DIRECTION('SD0',(1.0,0.0));
                #45=AXIS2_PLACEMENT_2D('MAP',#43,#44);
                #46=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #47=REPRESENTATION('SYMREP',(),#46);
                #48=SYMBOL_REPRESENTATION_MAP(#45,#47);
                #49=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #50=DIRECTION('SD1',(1.0,0.0));
                #51=AXIS2_PLACEMENT_2D('TGT',#49,#50);
                #52=ANNOTATION_SYMBOL('AS0',#48,#51);
                #53=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#42),#32);
                #54=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#42),#41);
                #55=TERMINATOR_SYMBOL('TS0',(#42),#52,#53);
                #56=TERMINATOR_SYMBOL('TS1',(#42),#52,#54);
                #57=EDGE_CURVE('E0',#20,#21,#55,.T.);
                #58=EDGE_CURVE('E1',#21,#22,#35,.T.);
                #59=EDGE_CURVE('E2',#23,#22,#38,.T.);
                #60=EDGE_CURVE('E3',#20,#23,#56,.T.);
                #61=ORIENTED_EDGE('OE0',$,$,#57,.T.);
                #62=ORIENTED_EDGE('OE1',$,$,#58,.T.);
                #63=ORIENTED_EDGE('OE2',$,$,#59,.F.);
                #64=ORIENTED_EDGE('OE3',$,$,#60,.F.);
                #65=EDGE_LOOP('L0',(#61,#62,#63,#64));
                #66=FACE_OUTER_BOUND('B0',#65,.T.);
                #67=ADVANCED_FACE('F0',(#66),#11,.T.);
                #68=OPEN_SHELL('OS',(#67));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "RECTANGULAR_TRIMMED_SURFACE", 6, true);
    }

    @Test
    void shouldExportRectangularTrimmedRationalBsplineSurfaceUsingTerminatorWrappedEdges() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P10',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P01',(0.0,1.0,0.0));
                #4=CARTESIAN_POINT('P11',(1.0,1.0,1.0));
                #10=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.)
                     RATIONAL_B_SPLINE_SURFACE(((1.0,1.0),(1.0,1.0))));
                #11=RECTANGULAR_TRIMMED_SURFACE('RTS0',#10,0.0,1.0,0.0,1.0,.T.,.T.);
                #20=VERTEX_POINT('V0',#1);
                #21=VERTEX_POINT('V1',#2);
                #22=VERTEX_POINT('V2',#4);
                #23=VERTEX_POINT('V3',#3);
                #30=DIRECTION('DX',(1.0,0.0,0.0));
                #31=VECTOR('VX',#30,1.0);
                #32=LINE('L0',#1,#31);
                #33=DIRECTION('DYZ',(0.0,1.0,1.0));
                #34=VECTOR('VYZ',#33,1.0);
                #35=LINE('L1',#2,#34);
                #36=DIRECTION('NDX',(-1.0,0.0,0.0));
                #37=VECTOR('NVX',#36,1.0);
                #38=LINE('L2',#4,#37);
                #39=DIRECTION('NDY',(0.0,-1.0,0.0));
                #40=VECTOR('NVY',#39,1.0);
                #41=LINE('L3',#3,#40);
                #42=PRESENTATION_STYLE_ASSIGNMENT(());
                #43=CARTESIAN_POINT('SYM0',(0.0,0.0));
                #44=DIRECTION('SD0',(1.0,0.0));
                #45=AXIS2_PLACEMENT_2D('MAP',#43,#44);
                #46=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #47=REPRESENTATION('SYMREP',(),#46);
                #48=SYMBOL_REPRESENTATION_MAP(#45,#47);
                #49=CARTESIAN_POINT('SYM1',(0.0,0.0));
                #50=DIRECTION('SD1',(1.0,0.0));
                #51=AXIS2_PLACEMENT_2D('TGT',#49,#50);
                #52=ANNOTATION_SYMBOL('AS0',#48,#51);
                #53=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#42),#32);
                #54=ANNOTATION_CURVE_OCCURRENCE('ACO1',(#42),#41);
                #55=TERMINATOR_SYMBOL('TS0',(#42),#52,#53);
                #56=TERMINATOR_SYMBOL('TS1',(#42),#52,#54);
                #57=EDGE_CURVE('E0',#20,#21,#55,.T.);
                #58=EDGE_CURVE('E1',#21,#22,#35,.T.);
                #59=EDGE_CURVE('E2',#23,#22,#38,.T.);
                #60=EDGE_CURVE('E3',#20,#23,#56,.T.);
                #61=ORIENTED_EDGE('OE0',$,$,#57,.T.);
                #62=ORIENTED_EDGE('OE1',$,$,#58,.T.);
                #63=ORIENTED_EDGE('OE2',$,$,#59,.F.);
                #64=ORIENTED_EDGE('OE3',$,$,#60,.F.);
                #65=EDGE_LOOP('L0',(#61,#62,#63,#64));
                #66=FACE_OUTER_BOUND('B0',#65,.T.);
                #67=ADVANCED_FACE('F0',(#66),#11,.T.);
                #68=OPEN_SHELL('OS',(#67));
                ENDSEC;
                """);

        assertSingleSupportedFacePreview(json, "RECTANGULAR_TRIMMED_SURFACE", 6, true);
    }

    @Test
    void shouldExportEdgeCurveBackedByOffsetCurve3d() {
        String json = StepPreviewJsonExporter.export("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=OFFSET_CURVE_3D('OC3',#4,0.5,.F.,#5);
                #7=CARTESIAN_POINT('S',(0.0,-0.5,0.0));
                #8=CARTESIAN_POINT('E',(1.0,-0.5,0.0));
                #9=VERTEX_POINT('VS',#7);
                #10=VERTEX_POINT('VE',#8);
                #11=EDGE_CURVE('E0',#9,#10,#6,.T.);
                #12=CONNECTED_EDGE_SET('CES',(#11));
                ENDSEC;
                """);

        assertJsonContains(json,
                "\"edgeCount\":1",
                "\"id\":11",
                "[0.0,-0.5,0.0]",
                "[1.0,-0.5,0.0]");
    }

    private static String linearExtrusionFaceStep(
            String surfaceDeclarations,
            String faceGeometryRef,
            String p00,
            String p10,
            String p11,
            String p01
    ) {
        return """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=VECTOR('VZ',#4,1.0);
                #6=LINE('BASE',#1,#3);
                #7=SURFACE_OF_LINEAR_EXTRUSION('SLE0',#6,#5);
                %s
                #10=CARTESIAN_POINT('P00',(%s));
                #11=CARTESIAN_POINT('P10',(%s));
                #12=CARTESIAN_POINT('P11',(%s));
                #13=CARTESIAN_POINT('P01',(%s));
                #14=VERTEX_POINT('V0',#10);
                #15=VERTEX_POINT('V1',#11);
                #16=VERTEX_POINT('V2',#12);
                #17=VERTEX_POINT('V3',#13);
                #20=LINE('L0',#10,#3);
                #21=LINE('L1',#11,#5);
                #22=LINE('L2',#13,#3);
                #23=LINE('L3',#10,#5);
                #30=EDGE_CURVE('E0',#14,#15,#20,.T.);
                #31=EDGE_CURVE('E1',#15,#16,#21,.T.);
                #32=EDGE_CURVE('E2',#17,#16,#22,.T.);
                #33=EDGE_CURVE('E3',#14,#17,#23,.T.);
                #34=ORIENTED_EDGE('OE0',$,$,#30,.T.);
                #35=ORIENTED_EDGE('OE1',$,$,#31,.T.);
                #36=ORIENTED_EDGE('OE2',$,$,#32,.F.);
                #37=ORIENTED_EDGE('OE3',$,$,#33,.F.);
                #38=EDGE_LOOP('L0',(#34,#35,#36,#37));
                #39=FACE_OUTER_BOUND('B0',#38,.T.);
                #40=ADVANCED_FACE('F0',(#39),%s,.T.);
                #41=OPEN_SHELL('OS',(#40));
                ENDSEC;
                """.formatted(surfaceDeclarations, p00, p10, p11, p01, faceGeometryRef);
    }

    private static String surfaceOfRevolutionFaceStep(
            String surfaceDeclarations,
            String faceGeometryRef,
            String p00,
            String p10,
            String p11,
            String p01
    ) {
        return """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('B0',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('B1',(1.0,0.0,1.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=DIRECTION('DX',(1.0,0.0,0.0));
                #6=AXIS1_PLACEMENT('AX1',#1,#4);
                #7=VECTOR('VZ',#4,1.0);
                #8=LINE('GEN',#2,#7);
                #9=AXIS2_PLACEMENT_3D('AC0',#1,#4,#5);
                #10=SURFACE_OF_REVOLUTION('SOR0',#8,#6);
                %s
                #12=CARTESIAN_POINT('P00',(%s));
                #13=CARTESIAN_POINT('P10',(%s));
                #14=CARTESIAN_POINT('P11',(%s));
                #15=CARTESIAN_POINT('P01',(%s));
                #16=VERTEX_POINT('V0',#12);
                #17=VERTEX_POINT('V1',#13);
                #18=VERTEX_POINT('V2',#14);
                #19=VERTEX_POINT('V3',#15);
                #20=AXIS2_PLACEMENT_3D('AC1',#3,#4,#5);
                #30=CIRCLE('C0',#9,1.0);
                #31=LINE('L0',#13,#7);
                #32=CIRCLE('C1',#20,1.0);
                #33=LINE('L1',#12,#7);
                #40=EDGE_CURVE('E0',#16,#17,#30,.T.);
                #41=EDGE_CURVE('E1',#17,#18,#31,.T.);
                #42=EDGE_CURVE('E2',#19,#18,#32,.T.);
                #43=EDGE_CURVE('E3',#16,#19,#33,.T.);
                #44=ORIENTED_EDGE('OE0',$,$,#40,.T.);
                #45=ORIENTED_EDGE('OE1',$,$,#41,.T.);
                #46=ORIENTED_EDGE('OE2',$,$,#42,.F.);
                #47=ORIENTED_EDGE('OE3',$,$,#43,.F.);
                #48=EDGE_LOOP('L0',(#44,#45,#46,#47));
                #49=FACE_OUTER_BOUND('B0',#48,.T.);
                #50=ADVANCED_FACE('F0',(#49),%s,.T.);
                #51=OPEN_SHELL('OS',(#50));
                ENDSEC;
                """.formatted(surfaceDeclarations, p00, p10, p11, p01, faceGeometryRef);
    }

    private static String bsplinePatchFaceStep(
            boolean rational,
            String surfaceDeclarations,
            String faceGeometryRef,
            String p00,
            String p10,
            String p11,
            String p01
    ) {
        String surface = rational
                ? """
                #10=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.)
                     RATIONAL_B_SPLINE_SURFACE(((1.0,1.0),(1.0,1.0))));
                """
                : """
                #10=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.));
                """;
        return """
                DATA;
                #1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P10',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P01',(0.0,1.0,0.0));
                #4=CARTESIAN_POINT('P11',(1.0,1.0,1.0));
                %s
                %s
                #21=CARTESIAN_POINT('Q00',(%s));
                #22=CARTESIAN_POINT('Q10',(%s));
                #23=CARTESIAN_POINT('Q11',(%s));
                #24=CARTESIAN_POINT('Q01',(%s));
                #30=POLYLINE('PL0',(#21,#22,#23,#24,#21));
                #40=POLY_LOOP('L0',(#21,#22,#23,#24));
                #41=FACE_OUTER_BOUND('B0',#40,.T.);
                #42=ADVANCED_FACE('F0',(#41),%s,.T.);
                #43=OPEN_SHELL('OS',(#42));
                ENDSEC;
                """.formatted(surface, surfaceDeclarations, p00, p10, p11, p01, faceGeometryRef);
    }

    private static void assertSingleSupportedFacePreview(String json, String surfaceType, int edgeCount, boolean requireTriangles) {
        assertTrue(json.contains("\"surfaceType\":\"" + surfaceType + "\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"edgeCount\":" + edgeCount), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        if (requireTriangles) {
            assertTrue(json.contains("\"triangles\":[["), json);
        }
    }

    private static void assertSingleSupportedFacePreviewWithoutEdgeCount(String json, String surfaceType) {
        assertTrue(json.contains("\"surfaceType\":\"" + surfaceType + "\""), json);
        assertTrue(json.contains("\"faceCount\":1"), json);
        assertTrue(json.contains("\"unsupportedFaceCount\":0"), json);
        assertTrue(json.contains("\"triangles\":[["), json);
    }

    private static void assertSingleSupportedFacePreviewWithInnerLoop(String json, String surfaceType, int edgeCount) {
        assertSingleSupportedFacePreview(json, surfaceType, edgeCount, true);
        assertTrue(json.contains("\"outer\":false"), json);
    }

    private static void assertJsonContains(String json, String... fragments) {
        for (String fragment : fragments) {
            assertTrue(json.contains(fragment), json);
        }
    }

    private static int countOccurrences(String text, String needle) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(needle, index)) >= 0) {
            count++;
            index += needle.length();
        }
        return count;
    }

    private static boolean containsNoRawControlCharacters(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) < 0x20) {
                return false;
            }
        }
        return true;
    }
}
