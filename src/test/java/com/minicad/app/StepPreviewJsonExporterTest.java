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
}
