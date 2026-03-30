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
    void shouldExportPreviewJsonForFanExample() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/fan.stp")));

        assertTrue(json.contains("\"representationCount\":1"));
        assertTrue(json.contains("\"instanceCount\":1"));
        assertTrue(json.contains("\"unsupportedFaceCount\":3"));
        assertTrue(json.contains("\"B_SPLINE_SURFACE_WITH_KNOTS\""));
        assertFalse(json.contains("\"reason\":\"b-spline surface preview failed\""));
        assertTrue(json.contains("\"edges\":[],\"faces\":[],\"representations\":["));
        assertTrue(json.length() < 250_000_000);
    }

    @Test
    void shouldOmitDuplicateLegacyGeometryForAssemblyPayloads() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/two-instance-assembly.step")));

        assertTrue(json.contains("\"representationCount\":2"));
        assertTrue(json.contains("\"instanceCount\":3"));
        assertTrue(json.contains("\"unsupportedFaces\":[],\"edges\":[],\"faces\":[],\"representations\":["));
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
        assertTrue(json.contains("\"unsupportedFaces\":[{\"id\":70"));
        assertTrue(json.contains("\"surfaceType\":\"CYLINDRICAL_SURFACE\""));
        assertTrue(json.contains("\"reason\":"));
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

        assertTrue(json.contains("\"faceCount\":0"));
        assertTrue(json.contains("\"unsupportedFaceCount\":1"));
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
        assertTrue(json.contains("\"instanceIds\":[\"pd-8/occ-59-pd-9\"]"));
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

        assertTrue(json.contains("\"surfaceType\":\"CYLINDRICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":8"));
        assertTrue(json.contains("\"outer\":false"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
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

        assertTrue(json.contains("\"surfaceType\":\"CONICAL_SURFACE\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":8"));
        assertTrue(json.contains("\"outer\":false"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
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

        assertTrue(json.contains("\"surfaceType\":\"B_SPLINE_SURFACE_WITH_KNOTS\""));
        assertTrue(json.contains("\"faceCount\":1"));
        assertTrue(json.contains("\"edgeCount\":4"));
        assertTrue(json.contains("\"unsupportedFaceCount\":0"));
        assertTrue(json.contains("\"triangles\":[["));
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

        assertTrue(json.contains("\"representations\":["));
        assertTrue(json.contains("\"instances\":["));
        assertTrue(json.contains("\"representationId\":55"));
        assertTrue(json.contains("\"occurrenceId\":59"));
        assertTrue(json.contains("\"matrix\":[1.0,0.0,0.0,10.0"));
    }

    @Test
    void shouldExportTranslatedPartAssemblyExample() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/translated-part-assembly.step")));

        assertTrue(json.contains("\"representations\":["));
        assertTrue(json.contains("\"instances\":["));
        assertTrue(json.contains("\"occurrenceId\":59"));
        assertTrue(json.contains("\"matrix\":[1.0,0.0,0.0,12.0"));
    }

    @Test
    void shouldExportTwoInstanceAssemblyExample() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/two-instance-assembly.step")));

        assertTrue(json.contains("\"instances\":["));
        assertTrue(json.contains("\"occurrenceId\":63"));
        assertTrue(json.contains("\"occurrenceId\":64"));
        assertTrue(json.contains("\"matrix\":[1.0,0.0,0.0,6.0"));
        assertTrue(json.contains("\"matrix\":[1.0,0.0,0.0,0.0,0.0,1.0,0.0,5.0"));
    }

    @Test
    void shouldExportNestedAssemblyExample() throws IOException {
        String json = StepPreviewJsonExporter.export(Files.readString(Path.of("examples/nested-assembly.step")));

        assertTrue(json.contains("\"instances\":["));
        assertTrue(json.contains("\"id\":\"pd-10\""));
        assertTrue(json.contains("\"occurrenceId\":69"));
        assertTrue(json.contains("\"occurrenceId\":70"));
        assertTrue(json.contains("\"parentId\":\"pd-10\""));
        assertTrue(json.contains("\"representationIds\":[63]"));
        assertTrue(json.contains("\"localMatrix\":[1.0,0.0,0.0,0.0,0.0,1.0,0.0,4.0"));
        assertTrue(json.contains("\"matrix\":[1.0,0.0,0.0,10.0,0.0,1.0,0.0,4.0"));
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
