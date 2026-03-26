package com.minicad.app;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

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
                new PrintStream(stdout),
                new PrintStream(stderr)
        );

        String output = stdout.toString();
        assertEquals(0, exitCode);
        assertTrue(output.contains("Syntax Summary"));
        assertTrue(output.contains("Semantic Summary"));
        assertTrue(output.contains("Build Summary"));
        assertTrue(output.contains("solid #100: shellFaces=1"));
    }

    @Test
    void shouldFailClearlyOnInvalidTopologyBuild() throws IOException {
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
                new PrintStream(stdout),
                new PrintStream(stderr)
        );

        assertEquals(1, exitCode);
        assertTrue(stderr.toString().contains("start vertex must lie on edge curve"));
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
                new PrintStream(stdout),
                new PrintStream(stderr)
        );

        String output = stdout.toString();
        assertEquals(0, exitCode);
        assertTrue(output.contains("openShell #80: faces=0, unsupportedFaces=1"));
        assertTrue(output.contains("unsupportedFaces=1"));
    }
}
