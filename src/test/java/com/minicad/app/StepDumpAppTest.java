package com.minicad.app;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StepDumpAppTest {

    @Test
    void shouldPrintSummaryForMinimalSolid() throws IOException {
        Path file = Files.createTempFile("minicad-solid", ".step");
        Files.writeString(file, """
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

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        int exitCode = StepDumpApp.run(
                new String[]{file.toString()},
                sink(stdout),
                sink(stderr)
        );

        String output = stdout.toString();
        assertEquals(0, exitCode);
        assertTrue(output.contains("Syntax Summary"));
        assertTrue(output.contains("Semantic Summary"));
        assertTrue(output.contains("Build Summary"));
        assertTrue(output.contains("solid #100: shellFaces=1"));
    }

    @Test
    void shouldTreatInvalidTopologyBuildAsUnsupportedFace() throws IOException {
        Path file = Files.createTempFile("minicad-circle-edge", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=DIRECTION('DZ',(0.0,0.0,1.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX',#1,#3,#4);
                #6=CIRCLE('C0',#5,2.0);
                #10=VERTEX_POINT('V0',#1);
                #11=VERTEX_POINT('V1',#2);
                #20=EDGE_CURVE('E0',#10,#11,#6,.T.);
                #30=ORIENTED_EDGE('OE0',$,$,#20,.T.);
                #40=EDGE_LOOP('L0',(#30));
                #41=FACE_OUTER_BOUND('B0',#40,.T.);
                #42=PLANE('PL0',#5);
                #50=ADVANCED_FACE('F0',(#41),#42,.T.);
                #60=OPEN_SHELL('OS',(#50));
                ENDSEC;
                """);

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        int exitCode = StepDumpApp.run(
                new String[]{file.toString()},
                sink(stdout),
                sink(stderr)
        );

        String output = stdout.toString();
        assertEquals(0, exitCode);
        assertTrue(output.contains("openShell #60: faces=0, unsupportedFaces=1"));
        assertTrue(output.contains("unsupportedReasons:"));
        assertTrue(output.contains("lie on edge curve"));
        assertTrue(output.contains("unsupportedReasonCodes: topology.edge_vertex_off_curve:1"));
        assertTrue(output.contains("unsupportedFaces=1"));
    }

    @Test
    void shouldReportUnsupportedCylindricalFacesWithoutFailingWholeDump() throws IOException {
        Path file = Files.createTempFile("minicad-cylinder-face", ".step");
        Files.writeString(file, """
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

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        int exitCode = StepDumpApp.run(
                new String[]{file.toString()},
                sink(stdout),
                sink(stderr)
        );

        String output = stdout.toString();
        assertEquals(0, exitCode);
        assertTrue(output.contains("openShell #80: faces=0, unsupportedFaces=1"));
        assertTrue(output.contains("unsupportedReasons: ADVANCED_FACE construction for CYLINDRICAL_SURFACE is unsupported:1"));
        assertTrue(output.contains("unsupportedReasonCodes: unsupported_surface.cylindrical:1"));
        assertTrue(output.contains("unsupportedFaces=1"));
    }

    @Test
    void shouldReportUnsupportedToroidalFacesWithoutFailingWholeDump() throws IOException {
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        int exitCode = StepDumpApp.run(
                new String[]{"examples/toroidal-trimmed-loops-with-hole.step"},
                sink(stdout),
                sink(stderr)
        );

        String output = stdout.toString();
        assertEquals(0, exitCode);
        assertTrue(output.contains("openShell #155: faces=0, unsupportedFaces=1"));
        assertTrue(output.contains("unsupportedReasons: ADVANCED_FACE construction for TOROIDAL_SURFACE is unsupported:1"));
        assertTrue(output.contains("unsupportedReasonCodes: unsupported_surface.toroidal:1"));
        assertTrue(output.contains("unsupportedFaces=1"));
    }

    @Test
    void shouldTreatInvalidPlanarFaceAsUnsupportedDuringSummary() throws IOException {
        Path file = Files.createTempFile("minicad-invalid-planar-face", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,1.0,1.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AXIS',#1,#10,#11);
                #13=PLANE('PL0',#12);
                #20=VERTEX_POINT('V0',#1);
                #21=VERTEX_POINT('V1',#2);
                #22=VERTEX_POINT('V2',#3);
                #30=DIRECTION('D1',(1.0,0.0,0.0));
                #31=VECTOR('VE1',#30,1.0);
                #32=LINE('L1',#1,#31);
                #33=DIRECTION('D2',(-1.0,1.0,1.0));
                #34=VECTOR('VE2',#33,1.0);
                #35=LINE('L2',#2,#34);
                #36=DIRECTION('D3',(0.0,-1.0,-1.0));
                #37=VECTOR('VE3',#36,1.0);
                #38=LINE('L3',#3,#37);
                #40=EDGE_CURVE('E1',#20,#21,#32,.T.);
                #41=EDGE_CURVE('E2',#21,#22,#35,.T.);
                #42=EDGE_CURVE('E3',#22,#20,#38,.T.);
                #50=ORIENTED_EDGE('OE1',$,$,#40,.T.);
                #51=ORIENTED_EDGE('OE2',$,$,#41,.T.);
                #52=ORIENTED_EDGE('OE3',$,$,#42,.T.);
                #60=EDGE_LOOP('LOOP',(#50,#51,#52));
                #61=FACE_OUTER_BOUND('FOB',#60,.T.);
                #70=ADVANCED_FACE('F0',(#61),#13,.T.);
                #80=OPEN_SHELL('OS',(#70));
                ENDSEC;
                """);

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        int exitCode = StepDumpApp.run(
                new String[]{file.toString()},
                sink(stdout),
                sink(stderr)
        );

        String output = stdout.toString();
        assertEquals(0, exitCode);
        assertTrue(output.contains("openShell #80: faces=0, unsupportedFaces=1"));
        assertTrue(output.contains("unsupportedReasons: all face vertices must lie on the plane:1"));
        assertTrue(output.contains("unsupportedReasonCodes: topology.face_vertex_off_plane:1"));
        assertTrue(output.contains("unsupportedFaces=1"));
    }

    @Test
    void shouldReportPolyLoopFaceAsUnsupportedDuringSummary() throws IOException {
        Path file = Files.createTempFile("minicad-poly-loop-face", ".step");
        Files.writeString(file, """
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

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        int exitCode = StepDumpApp.run(
                new String[]{file.toString()},
                sink(stdout),
                sink(stderr)
        );

        String output = stdout.toString();
        assertEquals(0, exitCode);
        assertTrue(output.contains("openShell #11: faces=0, unsupportedFaces=1"));
        assertTrue(output.contains("unsupportedReasons: FACE_BOUND construction for POLY_LOOP is unsupported:1"));
        assertTrue(output.contains("unsupportedReasonCodes: unsupported_loop.poly:1"));
    }

    @Test
    void shouldReportBooleanResultAsUnsupportedBuildItem() throws IOException {
        Path file = Files.createTempFile("minicad-boolean-result", ".step");
        Files.writeString(file, """
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

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        int exitCode = StepDumpApp.run(
                new String[]{file.toString()},
                sink(stdout),
                sink(stderr)
        );

        String output = stdout.toString();
        assertEquals(0, exitCode);
        assertTrue(output.contains("booleanResult #19: faces=0, unsupportedFaces=1"));
        assertTrue(output.contains("unsupportedReasons: BOOLEAN_RESULT construction is unsupported:1"));
        assertTrue(output.contains("unsupportedReasonCodes: unsupported_boolean.result:1"));
        assertTrue(output.contains("booleanResults=1"));
    }

    @Test
    void shouldReportBooleanClippingResultAsUnsupportedBuildItem() throws IOException {
        Path file = Files.createTempFile("minicad-boolean-clipping-result", ".step");
        Files.writeString(file, """
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
                #19=(BOOLEAN_CLIPPING_RESULT(.DIFFERENCE.,#17,#18) BOOLEAN_RESULT(.DIFFERENCE.,#17,#18) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BCR0'));
                ENDSEC;
                """);

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        int exitCode = StepDumpApp.run(
                new String[]{file.toString()},
                sink(stdout),
                sink(stderr)
        );

        String output = stdout.toString();
        assertEquals(0, exitCode);
        assertTrue(output.contains("booleanClippingResult #19: faces=0, unsupportedFaces=1"));
        assertTrue(output.contains("unsupportedReasons: BOOLEAN_CLIPPING_RESULT construction is unsupported:1"));
        assertTrue(output.contains("unsupportedReasonCodes: unsupported_boolean.clipping_result:1"));
        assertTrue(output.contains("booleanResults=1"));
    }

    private static Consumer<String> sink(ByteArrayOutputStream output) {
        return line -> {
            try {
                output.write((line + System.lineSeparator()).getBytes());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
