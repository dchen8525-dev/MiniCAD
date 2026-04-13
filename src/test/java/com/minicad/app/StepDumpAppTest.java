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

        assertDumpContains(file,
                "Syntax Summary",
                "Semantic Summary",
                "Build Summary",
                "  MANIFOLD_SOLID_BREP: 1",
                "  VERTEX_POINT: 4",
                "  FACE_OUTER_BOUND: 1",
                "MANIFOLD_SOLID_BREP #100: shellFaces=1");
    }

    @Test
    void shouldPreserveSolidAndSurfaceModelContainerNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-solid-surface-model-containers", ".step");
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
                #81=CLOSED_SHELL('CS',(#80));
                #82=MANIFOLD_SOLID_BREP('S0',#81);
                #83=OPEN_SHELL('OS0',(#80));
                #84=SHELL_BASED_SURFACE_MODEL('SBSM',(#83));
                ENDSEC;
                """);

        assertDumpContains(file,
                "MANIFOLD_SOLID_BREP #82:",
                "SHELL_BASED_SURFACE_MODEL #84:");
    }

    @Test
    void shouldPreserveWireAndSurfaceContainerNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-wire-surface-container-names", ".step");
        Files.writeString(file, """
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
                #83=(FACE_BASED_SURFACE_MODEL('FBSM',(#81)) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('fbsm-item'));
                #84=SHELL_BASED_SURFACE_MODEL('SBSM',(#94));
                #85=CONNECTED_EDGE_SET('CES',(#50,#51));
                #86=(EDGE_BASED_WIREFRAME_MODEL('WBM',(#85)) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('wire'));
                #87=POLY_LOOP('PL0',(#1,#2,#3));
                #88=WIRE_SHELL('WS0',(#87));
                #89=VERTEX_LOOP('VL0',#20);
                #90=VERTEX_SHELL('VS0',#89);
                #91=SHELL_BASED_WIREFRAME_MODEL('SBWM',(#88,#90));
                #94=OPEN_SHELL('OS0',(#80));
                ENDSEC;
                """);

        assertDumpContains(file,
                "FACE_BASED_SURFACE_MODEL #83:",
                "SHELL_BASED_SURFACE_MODEL #84:",
                "CONNECTED_EDGE_SET #85:",
                "EDGE_BASED_WIREFRAME_MODEL #86:",
                "WIRE_SHELL #88:",
                "VERTEX_SHELL #90:",
                "SHELL_BASED_WIREFRAME_MODEL #91:");
    }

    @Test
    void shouldReportWireShellWithMixedLoopFamiliesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-wire-shell-mixed-loops", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=VECTOR('VX',#4,1.0);
                #6=LINE('L0',#1,#5);
                #7=DIRECTION('DY',(0.0,1.0,0.0));
                #8=VECTOR('VY',#7,1.0);
                #9=LINE('L1',#2,#8);
                #10=DIRECTION('DM',(-1.0,-1.0,0.0));
                #11=VECTOR('VM',#10,1.4142135623730951);
                #12=LINE('L2',#3,#11);
                #13=VERTEX_POINT('V0',#1);
                #14=VERTEX_POINT('V1',#2);
                #15=VERTEX_POINT('V2',#3);
                #16=EDGE_CURVE('E0',#13,#14,#6,.T.);
                #17=EDGE_CURVE('E1',#14,#15,#9,.T.);
                #18=EDGE_CURVE('E2',#15,#13,#12,.T.);
                #19=ORIENTED_EDGE('OE0',$,$,#16,.T.);
                #20=ORIENTED_EDGE('OE1',$,$,#17,.T.);
                #21=ORIENTED_EDGE('OE2',$,$,#18,.T.);
                #22=EDGE_LOOP('EL0',(#19,#20,#21));
                #23=POLY_LOOP('PL0',(#1,#2,#3));
                #24=WIRE_SHELL('WS0',(#22,#23));
                ENDSEC;
                """);

        assertDumpContains(file,
                "EDGE_LOOP #22: built=true, unsupportedFaces=0",
                "POLY_LOOP #23: built=true, unsupportedFaces=0",
                "WIRE_SHELL #24: builtLoops=2, unsupportedFaces=0");
    }

    @Test
    void shouldPrintStepEntityNamesForConicsInSemanticSummary() throws IOException {
        Path file = Files.createTempFile("minicad-parabola-summary", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PARABOLA('PAR0',#4,2.0);
                ENDSEC;
                """);

        assertDumpContains(file,
                "Semantic Summary",
                "  PARABOLA: 1");
    }

    @Test
    void shouldReportStandaloneFaceEntitiesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-standalone-face", ".step");
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
                #81=ORIENTED_FACE('OF0',#80,.F.);
                ENDSEC;
                """);

        assertDumpContains(file,
                "ADVANCED_FACE #80: built=true, unsupportedFaces=0",
                "ORIENTED_FACE #81: built=true, unsupportedFaces=0",
                "standaloneFaceEntities=2");
    }

    @Test
    void shouldReportStandaloneEdgeEntitiesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-standalone-edge", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,1.0,0.0));
                #10=DIRECTION('DX',(1.0,0.0,0.0));
                #11=DIRECTION('DY',(0.0,1.0,0.0));
                #12=VECTOR('VX',#10,1.0);
                #13=VECTOR('VY',#11,1.0);
                #20=LINE('L0',#1,#12);
                #21=LINE('L1',#1,#13);
                #30=VERTEX_POINT('V0',#1);
                #31=VERTEX_POINT('V1',#2);
                #32=VERTEX_POINT('V2',#3);
                #50=EDGE_CURVE('E0',#30,#31,#20,.T.);
                #51=EDGE_CURVE('E1',#30,#32,#21,.T.);
                #61=ORIENTED_EDGE('OE1',$,$,#51,.F.);
                ENDSEC;
                """);

        assertDumpContains(file,
                "EDGE_CURVE #50: built=true, unsupportedFaces=0",
                "ORIENTED_EDGE #61: built=true, unsupportedFaces=0",
                "standaloneEdgeEntities=2");
    }

    @Test
    void shouldReportStandaloneLoopAndPathEntitiesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-standalone-loop-path", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=DIRECTION('DY',(0.0,1.0,0.0));
                #6=DIRECTION('D20',(-0.7071067811865475,-0.7071067811865475,0.0));
                #7=VECTOR('VX',#4,1.0);
                #8=VECTOR('VY',#5,1.0);
                #9=VECTOR('V20',#6,1.4142135623730951);
                #13=LINE('L0',#1,#7);
                #14=LINE('L1',#2,#8);
                #15=LINE('L2',#3,#9);
                #16=VERTEX_POINT('V0',#1);
                #17=VERTEX_POINT('V1',#2);
                #18=VERTEX_POINT('V2',#3);
                #20=EDGE_CURVE('E0',#16,#17,#13,.T.);
                #21=EDGE_CURVE('E1',#17,#18,#14,.T.);
                #22=EDGE_CURVE('E2',#18,#16,#15,.T.);
                #30=ORIENTED_EDGE('OE0',$,$,#20,.T.);
                #31=ORIENTED_EDGE('OE1',$,$,#21,.T.);
                #32=ORIENTED_EDGE('OE2',$,$,#22,.T.);
                #40=EDGE_LOOP('EL0',(#30,#31,#32));
                #41=VERTEX_LOOP('VL0',#16);
                #42=POLY_LOOP('PL0',(#1,#2,#3));
                #50=PATH('PTH',(#30,#31,#32));
                #51=OPEN_PATH('OP',(#30));
                #52=SUBPATH('SP',(#31),#50);
                #53=ORIENTED_PATH('OPTH',#50,.F.);
                ENDSEC;
                """);

        assertDumpContains(file,
                "EDGE_LOOP #40: built=true, unsupportedFaces=0",
                "VERTEX_LOOP #41: built=true, unsupportedFaces=0",
                "POLY_LOOP #42: built=true, unsupportedFaces=0",
                "PATH #50: built=true, unsupportedFaces=0",
                "OPEN_PATH #51: built=true, unsupportedFaces=0",
                "SUBPATH #52: built=true, unsupportedFaces=0",
                "ORIENTED_PATH #53: built=true, unsupportedFaces=0",
                "standaloneLoopEntities=3",
                "standalonePathEntities=4");
    }

    @Test
    void shouldReportNestedOrientedAndReversedOpenPathsInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-nested-oriented-paths", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=VECTOR('VX',#4,1.0);
                #6=LINE('L0',#1,#5);
                #7=LINE('L1',#3,#5);
                #8=VERTEX_POINT('V0',#1);
                #9=VERTEX_POINT('V1',#2);
                #10=VERTEX_POINT('V2',#3);
                #11=EDGE_CURVE('E0',#8,#9,#6,.T.);
                #12=EDGE_CURVE('E1',#10,#8,#7,.T.);
                #13=ORIENTED_EDGE('OE0',$,$,#11,.F.);
                #14=ORIENTED_EDGE('OE1',$,$,#12,.F.);
                #15=OPEN_PATH('OP0',(#13,#14));
                #16=ORIENTED_PATH('OP1',#15,.F.);
                #17=ORIENTED_PATH('OP2',#16,.F.);
                ENDSEC;
                """);

        assertDumpContains(file,
                "OPEN_PATH #15: built=true, unsupportedFaces=0",
                "ORIENTED_PATH #16: built=true, unsupportedFaces=0",
                "ORIENTED_PATH #17: built=true, unsupportedFaces=0");
    }

    @Test
    void shouldReportStandaloneWireAndSurfaceContainersInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-standalone-containers", ".step");
        Files.writeString(file, """
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
                #83=(FACE_BASED_SURFACE_MODEL('FBSM',(#81)) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('fbsm-item'));
                #84=SHELL_BASED_SURFACE_MODEL('SBSM',(#94));
                #85=CONNECTED_EDGE_SET('CES',(#50,#51));
                #86=(EDGE_BASED_WIREFRAME_MODEL('WBM',(#85)) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('wire'));
                #87=POLY_LOOP('PL0',(#1,#2,#3));
                #88=WIRE_SHELL('WS0',(#87));
                #89=VERTEX_LOOP('VL0',#20);
                #90=VERTEX_SHELL('VS0',#89);
                #91=SHELL_BASED_WIREFRAME_MODEL('SBWM',(#88,#90));
                #92=PATH('PTH',(#60,#61));
                #93=POLYLINE('PL0',(#1,#2,#3));
                #95=GEOMETRIC_CURVE_SET('GCS',(#32,#93,#1));
                #94=OPEN_SHELL('OS0',(#80));
                ENDSEC;
                """);

        assertDumpContains(file,
                "CONNECTED_EDGE_SET #85: builtEdges=2, unsupportedFaces=0",
                "WIRE_SHELL #88: builtLoops=1, unsupportedFaces=0",
                "VERTEX_SHELL #90: builtVertices=1, unsupportedFaces=0",
                "EDGE_BASED_WIREFRAME_MODEL #86: builtEdges=2, unsupportedFaces=0",
                "SHELL_BASED_WIREFRAME_MODEL #91: builtBoundaries=2, unsupportedFaces=0",
                "FACE_BASED_SURFACE_MODEL #83: faces=1, unsupportedFaces=0",
                "SHELL_BASED_SURFACE_MODEL #84: faces=1, unsupportedFaces=0",
                "GEOMETRIC_CURVE_SET #95: builtMembers=3, unsupportedFaces=0",
                "standaloneContainerEntities=44");
    }

    @Test
    void shouldReportShellBackedFaceBasedSurfaceModelInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-face-based-surface-model-shell", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "OPEN_SHELL #81: faces=1, unsupportedFaces=0",
                "FACE_BASED_SURFACE_MODEL #82: faces=1, unsupportedFaces=0");
    }

    @Test
    void shouldReportStandalonePointAndGeometricSetsInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-standalone-geom-sets", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=VECTOR('VX',#4,1.0);
                #6=LINE('L0',#1,#5);
                #7=DIRECTION('DZ',(0.0,0.0,1.0));
                #8=AXIS2_PLACEMENT_3D('AX',#1,#7,#4);
                #9=PLANE('PL0',#8);
                #10=POINT_SET('PS0',(#1,#2));
                #11=POLYLINE('PL0',(#1,#2,#3));
                #12=GEOMETRIC_SET('GS0',(#1,#6,#9,#11));
                ENDSEC;
                """);

        assertDumpContains(file,
                "POINT_SET #10: builtMembers=2, unsupportedFaces=0",
                "GEOMETRIC_SET #12: builtMembers=4, unsupportedFaces=0",
                "standaloneContainerEntities=12");
    }

    @Test
    void shouldPreservePointAndGeometricSetContainerNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-point-geometric-set-container-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=VECTOR('VX',#4,1.0);
                #6=LINE('L0',#1,#5);
                #7=DIRECTION('DZ',(0.0,0.0,1.0));
                #8=AXIS2_PLACEMENT_3D('AX',#1,#7,#4);
                #9=PLANE('PL0',#8);
                #10=POINT_SET('PS0',(#1,#2));
                #11=POLYLINE('PL0',(#1,#2,#3));
                #12=GEOMETRIC_SET('GS0',(#1,#6,#9,#10));
                #13=GEOMETRIC_CURVE_SET('GCS0',(#1,#6,#11));
                ENDSEC;
                """);

        assertDumpContains(file,
                "POINT_SET #10:",
                "GEOMETRIC_SET #12:",
                "GEOMETRIC_CURVE_SET #13:");
    }

    @Test
    void shouldPreservePointAndGeometricSetContainerEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-point-geometric-set-container-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=VECTOR('VX',#4,1.0);
                #6=LINE('L0',#1,#5);
                #7=DIRECTION('DZ',(0.0,0.0,1.0));
                #8=AXIS2_PLACEMENT_3D('AX',#1,#7,#4);
                #9=PLANE('PL0',#8);
                #10=POINT_SET('PS0',(#1,#2));
                #11=POLYLINE('PL0',(#1,#2,#3));
                #12=GEOMETRIC_SET('GS0',(#1,#6,#9,#10));
                #13=GEOMETRIC_CURVE_SET('GCS0',(#1,#6,#11));
                ENDSEC;
                """);

        assertDumpContains(file,
                "POINT_SET #10:",
                "GEOMETRIC_SET #12:",
                "GEOMETRIC_CURVE_SET #13:");
    }

    @Test
    void shouldPreserveAnnotationContainerNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-container-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #5=POLYLINE('B0',(#1,#2,#3,#4,#1));
                #6=(ANNOTATION_FILL_AREA('FA0',(#5))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                #7=PRESENTATION_STYLE_ASSIGNMENT(());
                #8=GEOMETRIC_SET('GS0',(#1,#2));
                #9=(ANNOTATION_PLACEHOLDER_OCCURRENCE('PH0',(#7),#8,.ANNOTATION_TEXT.,1.0)
                    STYLED_ITEM('PH0',(#7),#8)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PH0'));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #13=PLANE('PL0',#12);
                #14=(ANNOTATION_PLANE((#2))
                    STYLED_ITEM('AP',(#7),#13)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('AP'));
                #15=(ANNOTATION_POINT_OCCURRENCE('APO0',(#7),#1)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('APO0',(#7),#1));
                #16=ANNOTATION_TEXT_OCCURRENCE('NOTE','note',#2);
                #17=(ANNOTATION_FILL_AREA_OCCURRENCE('FAO0',(#7),#6,#1)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('FAO0',(#7),#6)
                    STYLED_ITEM('FAO0',(#7),#6)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FAO0'));
                #18=GEOMETRIC_CURVE_SET('LEADER',(#1,#2));
                #19=DRAUGHTING_CALLOUT('CALLOUT',(#16,#18));
                ENDSEC;
                """);

        assertDumpContains(file,
                "ANNOTATION_PLACEHOLDER_OCCURRENCE #9:",
                "ANNOTATION_PLANE #14:",
                "ANNOTATION_POINT_OCCURRENCE #15:",
                "ANNOTATION_TEXT_OCCURRENCE #16:",
                "ANNOTATION_FILL_AREA_OCCURRENCE #17:",
                "DRAUGHTING_CALLOUT #19:");
    }

    @Test
    void shouldPreserveAnnotationContainerEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-container-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #5=POLYLINE('B0',(#1,#2,#3,#4,#1));
                #6=(ANNOTATION_FILL_AREA('FA0',(#5))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                #7=PRESENTATION_STYLE_ASSIGNMENT(());
                #8=GEOMETRIC_SET('GS0',(#1,#2));
                #9=(ANNOTATION_PLACEHOLDER_OCCURRENCE('PH0',(#7),#8,.ANNOTATION_TEXT.,1.0)
                    STYLED_ITEM('PH0',(#7),#8)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PH0'));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #13=PLANE('PL0',#12);
                #14=(ANNOTATION_PLANE((#2))
                    STYLED_ITEM('AP',(#7),#13)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('AP'));
                #15=(ANNOTATION_POINT_OCCURRENCE('APO0',(#7),#1)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('APO0',(#7),#1));
                #16=ANNOTATION_TEXT_OCCURRENCE('NOTE','note',#2);
                #17=(ANNOTATION_FILL_AREA_OCCURRENCE('FAO0',(#7),#6,#1)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('FAO0',(#7),#6)
                    STYLED_ITEM('FAO0',(#7),#6)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FAO0'));
                #18=GEOMETRIC_CURVE_SET('LEADER',(#1,#2));
                #19=DRAUGHTING_CALLOUT('CALLOUT',(#16,#18));
                ENDSEC;
                """);

        assertDumpContains(file,
                "ANNOTATION_PLACEHOLDER_OCCURRENCE #9:",
                "ANNOTATION_PLANE #14:",
                "ANNOTATION_POINT_OCCURRENCE #15:",
                "ANNOTATION_TEXT_OCCURRENCE #16:",
                "ANNOTATION_FILL_AREA_OCCURRENCE #17:",
                "DRAUGHTING_CALLOUT #19:");
    }

    @Test
    void shouldReportPointReplicaInPointAndGeometricSetsInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-point-replica-sets", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=DIRECTION('DY',(0.0,1.0,0.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=CARTESIAN_POINT('O',(10.0,20.0,30.0));
                #6=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#2,#3,#5,2.0,#4);
                #7=POINT_REPLICA('PR0',#1,#6);
                #8=POINT_SET('PS0',(#1,#7));
                #9=GEOMETRIC_SET('GS0',(#7,#1));
                ENDSEC;
                """);

        assertDumpContains(file,
                "POINT_REPLICA #7: builtItems=2, unsupportedFaces=0",
                "POINT_SET #8: builtMembers=2, unsupportedFaces=0",
                "GEOMETRIC_SET #9: builtMembers=2, unsupportedFaces=0");
    }

    @Test
    void shouldReportPointReplicaInGeometricCurveSetInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-point-replica-curve-set", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=DIRECTION('DY',(0.0,1.0,0.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=CARTESIAN_POINT('O',(10.0,20.0,30.0));
                #6=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#2,#3,#5,2.0,#4);
                #7=POINT_REPLICA('PR0',#1,#6);
                #8=VECTOR('VX',#2,1.0);
                #9=LINE('L0',#1,#8);
                #10=GEOMETRIC_CURVE_SET('GCS0',(#7,#9));
                ENDSEC;
                """);

        assertDumpContains(file,
                "POINT_REPLICA #7: builtItems=2, unsupportedFaces=0",
                "GEOMETRIC_CURVE_SET #10: builtMembers=2, unsupportedFaces=0");
    }

    @Test
    void shouldReportGeometricSetWithNestedPointAndCurveSetsInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-nested-geom-sets", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=POINT_SET('PS0',(#2));
                #4=GEOMETRIC_CURVE_SET('GCS0',(#2));
                #5=GEOMETRIC_SET('GS0',(#3,#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "POINT_SET #3: builtMembers=1, unsupportedFaces=0",
                "GEOMETRIC_CURVE_SET #4: builtMembers=1, unsupportedFaces=0",
                "GEOMETRIC_SET #5: builtMembers=2, unsupportedFaces=0");
    }

    @Test
    void shouldReportGeometricSetWithSurfaceAndTopologyMembersInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-surface-topology-geom-sets", ".step");
        Files.writeString(file, """
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
                #19=GEOMETRIC_CURVE_SET('GCS0',(#14,#17,#18));
                #20=GEOMETRIC_SET('GS0',(#5,#14,#17,#18,#19));
                ENDSEC;
                """);

        assertDumpContains(file,
                "CYLINDRICAL_SURFACE #5: builtItems=1, unsupportedFaces=0",
                "GEOMETRIC_CURVE_SET #19: builtMembers=3, unsupportedFaces=0",
                "GEOMETRIC_SET #20: builtMembers=5, unsupportedFaces=0");
    }

    @Test
    void shouldReportGeometricSetsWithWireAndLoopContainersInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-wire-loop-geom-sets", ".step");
        Files.writeString(file, """
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
                #16=GEOMETRIC_SET('GS0',(#11,#13,#12,#14));
                ENDSEC;
                """);

        assertDumpContains(file,
                "CONNECTED_EDGE_SET #11: builtEdges=2, unsupportedFaces=0",
                "EDGE_LOOP #13: built=true, unsupportedFaces=0",
                "VERTEX_LOOP #12: built=true, unsupportedFaces=0",
                "WIRE_SHELL #14: builtLoops=2, unsupportedFaces=0",
                "GEOMETRIC_CURVE_SET #15: builtMembers=2, unsupportedFaces=0",
                "GEOMETRIC_SET #16: builtMembers=4, unsupportedFaces=0");
    }

    @Test
    void shouldReportGeometricCurveSetWithWireContainersAndWireframeModelsInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-wire-model-curve-set", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "CONNECTED_EDGE_SET #11: builtEdges=2, unsupportedFaces=0",
                "EDGE_LOOP #12: built=true, unsupportedFaces=0",
                "VERTEX_LOOP #13: built=true, unsupportedFaces=0",
                "WIRE_SHELL #15: builtLoops=2, unsupportedFaces=0",
                "EDGE_BASED_WIREFRAME_MODEL #16: builtEdges=2, unsupportedFaces=0",
                "SHELL_BASED_WIREFRAME_MODEL #17: builtBoundaries=2, unsupportedFaces=0",
                "GEOMETRIC_CURVE_SET #18: builtMembers=6, unsupportedFaces=0");
    }

    @Test
    void shouldReportGeometricSetWithShellModelAndSolidMembersInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-shell-model-solid-geom-set", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "OPEN_SHELL #12: faces=1, unsupportedFaces=0",
                "FACE_BASED_SURFACE_MODEL #13: faces=1, unsupportedFaces=0",
                "BLOCK #14: shellFaces=6, unsupportedFaces=0",
                "GEOMETRIC_SET #15: builtMembers=3, unsupportedFaces=0");
    }

    @Test
    void shouldReportGeometricSetsWithNestedSetMembersInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-nested-set-geom-sets", ".step");
        Files.writeString(file, """
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
                #10=GEOMETRIC_SET('OUTER_GS',(#8,#9));
                ENDSEC;
                """);

        assertDumpContains(file,
                "POINT_SET #6: builtMembers=1, unsupportedFaces=0",
                "GEOMETRIC_CURVE_SET #7: builtMembers=1, unsupportedFaces=0",
                "GEOMETRIC_SET #8: builtMembers=2, unsupportedFaces=0",
                "GEOMETRIC_CURVE_SET #9: builtMembers=3, unsupportedFaces=0",
                "GEOMETRIC_SET #10: builtMembers=2, unsupportedFaces=0");
    }

    @Test
    void shouldReportPointReplicaThroughRepresentationWrappersInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-point-replica-repr", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=DIRECTION('DY',(0.0,1.0,0.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=CARTESIAN_POINT('O',(10.0,20.0,30.0));
                #6=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#2,#3,#5,2.0,#4);
                #7=POINT_REPLICA('PR0',#1,#6);
                #8=AXIS2_PLACEMENT_3D('AX3',#5,#4,#2);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #10=SHAPE_REPRESENTATION('BASE',(#7),#9);
                #11=REPRESENTATION_MAP(#8,#10);
                #12=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T1',#2,#3,#1,1.0,#4);
                #13=MAPPED_ITEM(#11,#12);
                #14=SHAPE_REPRESENTATION('TOP',(#13),#9);
                ENDSEC;
                """);

        assertDumpContains(file,
                "POINT_REPLICA #7: builtItems=2, unsupportedFaces=0",
                "SHAPE_REPRESENTATION #10: builtItems=2, unsupportedFaces=0",
                "REPRESENTATION_MAP #11: builtItems=2, unsupportedFaces=0",
                "MAPPED_ITEM #13: builtItems=2, unsupportedFaces=0",
                "SHAPE_REPRESENTATION #14: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldReportRepresentationAndWrapperEntitiesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-standalone-representation", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=DIRECTION('DY',(0.0,1.0,0.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=VECTOR('VX',#2,1.0);
                #6=LINE('L0',#1,#5);
                #7=PRESENTATION_STYLE_ASSIGNMENT(());
                #8=STYLED_ITEM('S0',(#7),#6);
                #9=AXIS2_PLACEMENT_3D('AX3',#1,#4,#2);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #11=SHAPE_REPRESENTATION('BASE',(#8),#10);
                #12=REPRESENTATION_MAP(#9,#11);
                #13=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T3',#2,#3,#1,1.0,#4);
                #14=MAPPED_ITEM(#12,#13);
                #15=SHAPE_REPRESENTATION('TOP',(#14),#10);
                ENDSEC;
                """);

        assertDumpContains(file,
                "STYLED_ITEM #8: builtItems=1, unsupportedFaces=0",
                "SHAPE_REPRESENTATION #11: builtItems=1, unsupportedFaces=0",
                "REPRESENTATION_MAP #12: builtItems=1, unsupportedFaces=0",
                "MAPPED_ITEM #14: builtItems=1, unsupportedFaces=0",
                "SHAPE_REPRESENTATION #15: builtItems=1, unsupportedFaces=0",
                "PRESENTATION_STYLE_ASSIGNMENT #7: builtItems=0, unsupportedFaces=0",
                "standaloneContainerEntities=15");
    }

    @Test
    void shouldPreserveRepresentationWrapperNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-representation-wrapper-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=DIRECTION('DY',(0.0,1.0,0.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=VECTOR('VX',#2,1.0);
                #6=LINE('L0',#1,#5);
                #7=PRESENTATION_STYLE_ASSIGNMENT(());
                #8=STYLED_ITEM('S0',(#7),#6);
                #9=AXIS2_PLACEMENT_3D('AX3',#1,#4,#2);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #11=SHAPE_REPRESENTATION('BASE',(#8),#10);
                #12=REPRESENTATION_MAP(#9,#11);
                #13=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T3',#2,#3,#1,1.0,#4);
                #14=MAPPED_ITEM(#12,#13);
                #15=SHAPE_REPRESENTATION('TOP',(#14),#10);
                ENDSEC;
                """);

        assertDumpContains(file,
                "STYLED_ITEM #8:",
                "SHAPE_REPRESENTATION #11:",
                "REPRESENTATION_MAP #12:",
                "MAPPED_ITEM #14:",
                "SHAPE_REPRESENTATION #15:",
                "PRESENTATION_STYLE_ASSIGNMENT #7:");
    }

    @Test
    void shouldReportRepresentationRelationshipEntitiesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-representation-relationships", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=DIRECTION('DY',(0.0,1.0,0.0));
                #6=DIRECTION('DZ',(0.0,0.0,1.0));
                #7=VECTOR('VX',#4,1.0);
                #8=LINE('L0',#1,#7);
                #9=POLYLINE('PL0',(#1,#2,#3));
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #11=SHAPE_REPRESENTATION('REP_A',(#8),#10);
                #12=SHAPE_REPRESENTATION('REP_B',(#9),#10);
                #13=REPRESENTATION_RELATIONSHIP('RR','plain',#11,#12);
                #14=CARTESIAN_POINT('P3',(10.0,0.0,0.0));
                #15=AXIS2_PLACEMENT_3D('AX0',#1,#6,#4);
                #16=AXIS2_PLACEMENT_3D('AX1',#14,#6,#4);
                #17=ITEM_DEFINED_TRANSFORMATION('MOVE','translate x',#15,#16);
                #18=(REPRESENTATION_RELATIONSHIP('RRT','xform',#11,#12)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#17));
                #19=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','shape',#11,#12);
                ENDSEC;
                """);

        assertDumpContains(file,
                "REPRESENTATION_RELATIONSHIP #13: builtItems=2, unsupportedFaces=0",
                "ITEM_DEFINED_TRANSFORMATION #17: builtItems=1, unsupportedFaces=0",
                "REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION #18: builtItems=2, unsupportedFaces=0",
                "SHAPE_REPRESENTATION_RELATIONSHIP #19: builtItems=2, unsupportedFaces=0",
                "standaloneContainerEntities=");
    }

    @Test
    void shouldPreserveBasicRepresentationRelationshipNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-basic-representation-relationship-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=DIRECTION('DY',(0.0,1.0,0.0));
                #6=DIRECTION('DZ',(0.0,0.0,1.0));
                #7=VECTOR('VX',#4,1.0);
                #8=LINE('L0',#1,#7);
                #9=POLYLINE('PL0',(#1,#2,#3));
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #11=SHAPE_REPRESENTATION('REP_A',(#8),#10);
                #12=SHAPE_REPRESENTATION('REP_B',(#9),#10);
                #13=REPRESENTATION_RELATIONSHIP('RR','plain',#11,#12);
                #14=CARTESIAN_POINT('P3',(10.0,0.0,0.0));
                #15=AXIS2_PLACEMENT_3D('AX0',#1,#6,#4);
                #16=AXIS2_PLACEMENT_3D('AX1',#14,#6,#4);
                #17=ITEM_DEFINED_TRANSFORMATION('MOVE','translate x',#15,#16);
                #18=(REPRESENTATION_RELATIONSHIP('RRT','xform',#11,#12)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#17));
                #19=SHAPE_REPRESENTATION_RELATIONSHIP('SRR','shape',#11,#12);
                ENDSEC;
                """);

        assertDumpContains(file,
                "REPRESENTATION_RELATIONSHIP #13:",
                "REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION #18:",
                "SHAPE_REPRESENTATION_RELATIONSHIP #19:");
    }

    @Test
    void shouldReportAnnotationWrapperEntitiesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-wrappers", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=DIRECTION('DY',(0.0,1.0,0.0));
                #6=DIRECTION('DZ',(0.0,0.0,1.0));
                #7=VECTOR('VX',#4,1.0);
                #8=LINE('L0',#1,#7);
                #9=POLYLINE('PL0',(#1,#2,#3));
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #11=SHAPE_REPRESENTATION('SYMREP',(#8),#10);
                #12=AXIS2_PLACEMENT_3D('AX3',#1,#6,#4);
                #13=CARTESIAN_POINT('MAP0',(0.0,0.0));
                #14=DIRECTION('TDX',(1.0,0.0));
                #15=AXIS2_PLACEMENT_2D('MAPAX',#13,#14);
                #16=SYMBOL_REPRESENTATION_MAP(#15,#11);
                #17=REPRESENTATION_MAP(#15,#11);
                #18=CARTESIAN_POINT('TXT0',(1.0,0.0));
                #19=AXIS2_PLACEMENT_2D('TXT',#18,#14);
                #20=PRESENTATION_STYLE_ASSIGNMENT(());
                #21=GEOMETRIC_SET('GS0',(#1,#2));
                #22=(ANNOTATION_PLACEHOLDER_OCCURRENCE('PH0',(#20),#21,.ANNOTATION_TEXT.,1.0)
                    STYLED_ITEM('PH0',(#20),#21)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PH0'));
                #23=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#20),#8);
                #24=(LEADER_CURVE('LC0',(#20),#8)
                    ANNOTATION_CURVE_OCCURRENCE('LC0',(#20),#8));
                #25=(DIMENSION_CURVE('DC0',(#20),#8)
                    ANNOTATION_CURVE_OCCURRENCE('DC0',(#20),#8));
                #26=(PROJECTION_CURVE('PC0',(#20),#8)
                    ANNOTATION_CURVE_OCCURRENCE('PC0',(#20),#8));
                #27=ANNOTATION_FILL_AREA('FA0',(#9));
                #28=(ANNOTATION_FILL_AREA_OCCURRENCE('FAO0',(#20),#27,#1)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('FAO0',(#20),#27)
                    STYLED_ITEM('FAO0',(#20),#27)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FAO0'));
                #29=(ANNOTATION_POINT_OCCURRENCE('AP0',(#20),#1)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('AP0',(#20),#1));
                #30=ANNOTATION_TEXT_OCCURRENCE('ATO0','note',#2);
                #31=ANNOTATION_SYMBOL('AS0',#16,#12);
                #32=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#20),#31);
                #33=ANNOTATION_SUBFIGURE_OCCURRENCE('SUB0',(#20),#31);
                #34=TERMINATOR_SYMBOL('TS0',(#20),#31,#24);
                #35=ANNOTATION_TEXT('AT0',#17,#19);
                #36=ANNOTATION_TEXT_CHARACTER('ATC0',#17,#19);
                #37=(ANNOTATION_PLANE((#3))
                    STYLED_ITEM('APLANE',(#20),#38)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('APLANE'));
                #38=PLANE('PL0',#12);
                #39=GEOMETRIC_CURVE_SET('LEADER',(#1,#2));
                #40=DRAUGHTING_CALLOUT('CALLOUT_A',(#30,#39));
                #41=DRAUGHTING_CALLOUT('CALLOUT_B',(#30,#39));
                #42=DRAUGHTING_CALLOUT_RELATIONSHIP('REL','carry',#40,#41);
                #43=ANNOTATION_OCCURRENCE_RELATIONSHIP('AOR','link',#22,#37);
                ENDSEC;
                """);

        assertDumpContains(file,
                "ANNOTATION_CURVE_OCCURRENCE #23: builtItems=1, unsupportedFaces=0",
                "LEADER_CURVE #24: builtItems=1, unsupportedFaces=0",
                "DIMENSION_CURVE #25: builtItems=1, unsupportedFaces=0",
                "PROJECTION_CURVE #26: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_FILL_AREA #27: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_FILL_AREA_OCCURRENCE #28: builtItems=2, unsupportedFaces=0",
                "ANNOTATION_POINT_OCCURRENCE #29: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_TEXT_OCCURRENCE #30: builtItems=1, unsupportedFaces=0",
                "SYMBOL_REPRESENTATION_MAP #16: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_SYMBOL #31: builtItems=2, unsupportedFaces=0",
                "ANNOTATION_SYMBOL_OCCURRENCE #32: builtItems=2, unsupportedFaces=0",
                "ANNOTATION_SUBFIGURE_OCCURRENCE #33: builtItems=2, unsupportedFaces=0",
                "TERMINATOR_SYMBOL #34: builtItems=3, unsupportedFaces=0",
                "ANNOTATION_TEXT #35: builtItems=2, unsupportedFaces=0",
                "ANNOTATION_TEXT_CHARACTER #36: builtItems=2, unsupportedFaces=0",
                "ANNOTATION_PLANE #37: builtItems=2, unsupportedFaces=0",
                "GEOMETRIC_CURVE_SET #39: builtMembers=2, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #40: builtItems=3, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT_RELATIONSHIP #42: builtItems=6, unsupportedFaces=0",
                "ANNOTATION_OCCURRENCE_RELATIONSHIP #43: builtItems=4, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveAnnotationRelationshipNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-relationship-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYM'));
                #2=REPRESENTATION('SYM',(),#1);
                #3=CARTESIAN_POINT('O',(0.0,0.0));
                #4=DIRECTION('X',(1.0,0.0));
                #5=AXIS2_PLACEMENT_2D('MAP',#3,#4);
                #6=SYMBOL_REPRESENTATION_MAP(#5,#2);
                #7=CARTESIAN_POINT('P',(10.0,20.0));
                #8=DIRECTION('DX',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('TGT',#7,#8);
                #10=ANNOTATION_SYMBOL('AS0',#6,#9);
                #11=PRESENTATION_STYLE_ASSIGNMENT(());
                #12=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#11),#10);
                #13=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #14=DIRECTION('DIR0',(1.0,0.0,0.0));
                #15=VECTOR('V0',#14,1.0);
                #16=LINE('L0',#13,#15);
                #17=PROJECTION_CURVE('PC0',(#11),#16);
                #18=TERMINATOR_SYMBOL('TS0',(#11),#10,#17);
                #19=ANNOTATION_OCCURRENCE_RELATIONSHIP('AOR','assoc',#12,#18);
                #20=ANNOTATION_OCCURRENCE_ASSOCIATIVITY('AOA','assoc',#12,#18);
                #21=DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY('DCTPCA','assoc',#18,#17);
                #22=ANNOTATION_TEXT_OCCURRENCE('NOTE','note',#13);
                #23=GEOMETRIC_CURVE_SET('LEADER',(#13));
                #24=DRAUGHTING_CALLOUT('CALLOUT_A',(#22,#23));
                #25=DRAUGHTING_CALLOUT('CALLOUT_B',(#22,#23));
                #26=DRAUGHTING_CALLOUT_RELATIONSHIP('REL','carry',#24,#25);
                ENDSEC;
                """);

        assertDumpContains(file,
                "ANNOTATION_OCCURRENCE_RELATIONSHIP #19:",
                "ANNOTATION_OCCURRENCE_ASSOCIATIVITY #20:",
                "DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY #21:",
                "DRAUGHTING_CALLOUT_RELATIONSHIP #26:");
    }

    @Test
    void shouldPreserveAnnotationRelationshipEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-relationship-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYM'));
                #2=REPRESENTATION('SYM',(),#1);
                #3=CARTESIAN_POINT('O',(0.0,0.0));
                #4=DIRECTION('X',(1.0,0.0));
                #5=AXIS2_PLACEMENT_2D('MAP',#3,#4);
                #6=SYMBOL_REPRESENTATION_MAP(#5,#2);
                #7=CARTESIAN_POINT('P',(10.0,20.0));
                #8=DIRECTION('DX',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('TGT',#7,#8);
                #10=ANNOTATION_SYMBOL('AS0',#6,#9);
                #11=PRESENTATION_STYLE_ASSIGNMENT(());
                #12=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#11),#10);
                #13=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #14=DIRECTION('DIR0',(1.0,0.0,0.0));
                #15=VECTOR('V0',#14,1.0);
                #16=LINE('L0',#13,#15);
                #17=PROJECTION_CURVE('PC0',(#11),#16);
                #18=TERMINATOR_SYMBOL('TS0',(#11),#10,#17);
                #19=ANNOTATION_OCCURRENCE_RELATIONSHIP('AOR','assoc',#12,#18);
                #20=ANNOTATION_OCCURRENCE_ASSOCIATIVITY('AOA','assoc',#12,#18);
                #21=DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY('DCTPCA','assoc',#18,#17);
                #22=ANNOTATION_TEXT_OCCURRENCE('NOTE','note',#13);
                #23=GEOMETRIC_CURVE_SET('LEADER',(#13));
                #24=DRAUGHTING_CALLOUT('CALLOUT_A',(#22,#23));
                #25=DRAUGHTING_CALLOUT('CALLOUT_B',(#22,#23));
                #26=DRAUGHTING_CALLOUT_RELATIONSHIP('REL','carry',#24,#25);
                ENDSEC;
                """);

        assertDumpContains(file,
                "ANNOTATION_OCCURRENCE_RELATIONSHIP #19:",
                "ANNOTATION_OCCURRENCE_ASSOCIATIVITY #20:",
                "DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY #21:",
                "DRAUGHTING_CALLOUT_RELATIONSHIP #26:");
    }

    @Test
    void shouldReportPointReplicaInAnnotationContainersInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-point-replica", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=DIRECTION('DY',(0.0,1.0,0.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=CARTESIAN_POINT('O',(10.0,20.0,30.0));
                #6=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#2,#3,#5,2.0,#4);
                #7=POINT_REPLICA('PR0',#1,#6);
                #8=PRESENTATION_STYLE_ASSIGNMENT(());
                #9=GEOMETRIC_SET('GS0',(#7,#1));
                #10=(ANNOTATION_PLACEHOLDER_OCCURRENCE('PH0',(#8),#9,.ANNOTATION_TEXT.,1.0)
                    STYLED_ITEM('PH0',(#8),#9)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PH0'));
                #11=AXIS2_PLACEMENT_3D('AX',#5,#4,#2);
                #12=(ANNOTATION_PLANE((#7,#1))
                    STYLED_ITEM('APLANE',(#8),#13)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('APLANE'));
                #13=PLANE('PL0',#11);
                ENDSEC;
                """);

        assertDumpContains(file,
                "POINT_REPLICA #7: builtItems=2, unsupportedFaces=0",
                "GEOMETRIC_SET #9: builtMembers=2, unsupportedFaces=0",
                "ANNOTATION_PLACEHOLDER_OCCURRENCE #10: builtItems=2, unsupportedFaces=0",
                "ANNOTATION_PLANE #12: builtItems=4, unsupportedFaces=0");
    }

    @Test
    void shouldReportPointReplicaInAnnotationPointOccurrenceAndCalloutInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-point-callout-replica", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=DIRECTION('DY',(0.0,1.0,0.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=CARTESIAN_POINT('O',(10.0,20.0,30.0));
                #6=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#2,#3,#5,2.0,#4);
                #7=POINT_REPLICA('PR0',#1,#6);
                #8=PRESENTATION_STYLE_ASSIGNMENT(());
                #9=(ANNOTATION_POINT_OCCURRENCE('AP0',(#8),#7)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('AP0',(#8),#7));
                #10=GEOMETRIC_CURVE_SET('LEADER',(#7,#1));
                #11=DRAUGHTING_CALLOUT('CALLOUT',(#9,#10));
                ENDSEC;
                """);

        assertDumpContains(file,
                "POINT_REPLICA #7: builtItems=2, unsupportedFaces=0",
                "ANNOTATION_POINT_OCCURRENCE #9: builtItems=2, unsupportedFaces=0",
                "GEOMETRIC_CURVE_SET #10: builtMembers=2, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #11: builtItems=4, unsupportedFaces=0");
    }

    @Test
    void shouldReportAnnotationOccurrenceRelationshipWithTerminatorSymbolInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-aor-terminator", ".step");
        Files.writeString(file, """
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYM'));
                #2=REPRESENTATION('SYM',(),#1);
                #3=CARTESIAN_POINT('O',(0.0,0.0));
                #4=DIRECTION('X',(1.0,0.0));
                #5=AXIS2_PLACEMENT_2D('MAP',#3,#4);
                #6=SYMBOL_REPRESENTATION_MAP(#5,#2);
                #7=CARTESIAN_POINT('P',(30.0,40.0));
                #8=DIRECTION('DX',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('TGT',#7,#8);
                #10=ANNOTATION_SYMBOL('AS0',#6,#9);
                #11=PRESENTATION_STYLE_ASSIGNMENT(());
                #12=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #13=DIRECTION('DIR0',(1.0,0.0,0.0));
                #14=VECTOR('V0',#13,1.0);
                #15=LINE('L0',#12,#14);
                #16=ANNOTATION_CURVE_OCCURRENCE('ACO0',(#11),#15);
                #17=TERMINATOR_SYMBOL('TS0',(#11),#10,#16);
                #18=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#11),#10);
                #19=ANNOTATION_OCCURRENCE_RELATIONSHIP('REL','links symbol to terminator',#18,#17);
                ENDSEC;
                """);

        assertDumpContains(file,
                "ANNOTATION_SYMBOL_OCCURRENCE #18: builtItems=1, unsupportedFaces=0",
                "TERMINATOR_SYMBOL #17: builtItems=2, unsupportedFaces=0",
                "ANNOTATION_OCCURRENCE_RELATIONSHIP #19: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldReportAnnotationOccurrenceAssociativityWithTerminatorSymbolInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-aor-assoc-terminator", ".step");
        Files.writeString(file, """
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYM'));
                #2=REPRESENTATION('SYM',(),#1);
                #3=CARTESIAN_POINT('O',(0.0,0.0));
                #4=DIRECTION('X',(1.0,0.0));
                #5=AXIS2_PLACEMENT_2D('MAP',#3,#4);
                #6=SYMBOL_REPRESENTATION_MAP(#5,#2);
                #7=CARTESIAN_POINT('P',(10.0,20.0));
                #8=DIRECTION('DX',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('TGT',#7,#8);
                #10=ANNOTATION_SYMBOL('AS0',#6,#9);
                #11=PRESENTATION_STYLE_ASSIGNMENT(());
                #12=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#11),#10);
                #13=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #14=DIRECTION('DIR0',(1.0,0.0,0.0));
                #15=VECTOR('V0',#14,1.0);
                #16=LINE('L0',#13,#15);
                #17=PROJECTION_CURVE('PC0',(#11),#16);
                #18=TERMINATOR_SYMBOL('TS0',(#11),#10,#17);
                #19=ANNOTATION_OCCURRENCE_ASSOCIATIVITY('AOA','assoc',#12,#18);
                #20=DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY('DCTPCA','assoc',#18,#17);
                ENDSEC;
                """);

        assertDumpContains(file,
                "ANNOTATION_OCCURRENCE_ASSOCIATIVITY #19: builtItems=3, unsupportedFaces=0",
                "DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY #20: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldReportTerminatorSymbolWithWrappedAnnotationItemInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-terminator-wrapped-item", ".step");
        Files.writeString(file, """
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
                ENDSEC;
                """);

        assertDumpContains(file,
                "ANNOTATION_PLACEHOLDER_OCCURRENCE #4: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_SYMBOL_OCCURRENCE #5: builtItems=1, unsupportedFaces=0",
                "PROJECTION_CURVE #9: builtItems=1, unsupportedFaces=0",
                "TERMINATOR_SYMBOL #10: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldReportPointContainersAndPlaneInDraughtingCalloutBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-callout-point-containers", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#1);
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

        assertDumpContains(file,
                "POINT_SET #4: builtMembers=1, unsupportedFaces=0",
                "ANNOTATION_PLACEHOLDER_OCCURRENCE #7: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_PLANE #12: builtItems=2, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #13: builtItems=2, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #14: builtItems=2, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #15: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldReportAnnotationPlaceholderOccurrenceWithPointContainersInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-placeholder-point-containers", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=POINT_SET('PS0',(#1));
                #3=GEOMETRIC_CURVE_SET('GCS0',(#1));
                #4=PRESENTATION_STYLE_ASSIGNMENT(());
                #5=(ANNOTATION_PLACEHOLDER_OCCURRENCE('PH_PS',(#4),#2,.ANNOTATION_TEXT.,1.0)
                    STYLED_ITEM('PH_PS',(#4),#2)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PH_PS'));
                #6=(ANNOTATION_PLACEHOLDER_OCCURRENCE('PH_GCS',(#4),#3,.ANNOTATION_TEXT.,1.0)
                    STYLED_ITEM('PH_GCS',(#4),#3)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PH_GCS'));
                ENDSEC;
                """);

        assertDumpContains(file,
                "POINT_SET #2: builtMembers=1, unsupportedFaces=0",
                "GEOMETRIC_CURVE_SET #3: builtMembers=1, unsupportedFaces=0",
                "ANNOTATION_PLACEHOLDER_OCCURRENCE #5: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_PLACEHOLDER_OCCURRENCE #6: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldReportAnnotationCurveFamilyWithPathAndWireCarriersInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-curve-path-wire", ".step");
        Files.writeString(file, """
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
                ENDSEC;
                """);

        assertDumpContains(file,
                "ANNOTATION_CURVE_OCCURRENCE #23: builtItems=1, unsupportedFaces=0",
                "LEADER_CURVE #24: builtItems=1, unsupportedFaces=0",
                "DIMENSION_CURVE #25: builtItems=2, unsupportedFaces=0",
                "PROJECTION_CURVE #26: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldReportAnnotationFillAreaWithPathAndWireBoundariesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-fill-area-path-wire", ".step");
        Files.writeString(file, """
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
                ENDSEC;
                """);

        assertDumpContains(file,
                "ANNOTATION_FILL_AREA #18: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldReportAnnotationCurveAndFillAreaWithWireframeModelCarriersInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-wireframe-carriers", ".step");
        Files.writeString(file, """
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
                ENDSEC;
                """);

        assertDumpContains(file,
                "ANNOTATION_CURVE_OCCURRENCE #21: builtItems=2, unsupportedFaces=0",
                "PROJECTION_CURVE #22: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_FILL_AREA #23: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveDraughtingCalloutPointContainerContentNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-callout-point-container-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#1);
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

        assertDumpContains(file,
                "POINT_SET #4:",
                "ANNOTATION_PLACEHOLDER_OCCURRENCE #7:",
                "ANNOTATION_PLANE #12:",
                "DRAUGHTING_CALLOUT #13:",
                "DRAUGHTING_CALLOUT #14:",
                "DRAUGHTING_CALLOUT #15:");
    }

    @Test
    void shouldPreserveDraughtingCalloutPointContainerContentEntityNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-callout-point-container-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#1);
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

        assertDumpContains(file,
                "POINT_SET #4:",
                "ANNOTATION_PLACEHOLDER_OCCURRENCE #7:",
                "ANNOTATION_PLANE #12:",
                "DRAUGHTING_CALLOUT #13:",
                "DRAUGHTING_CALLOUT #14:",
                "DRAUGHTING_CALLOUT #15:");
    }

    @Test
    void shouldReportGeometricSetInDraughtingCalloutBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-callout-geometric-set", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#1);
                #4=GEOMETRIC_SET('GS',(#2));
                #5=DRAUGHTING_CALLOUT('GS_CALLOUT',(#3,#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "GEOMETRIC_SET #4: builtMembers=1, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #5: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveDraughtingCalloutGeometricSetContentNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-callout-geometric-set-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#1);
                #4=GEOMETRIC_SET('GS',(#2));
                #5=DRAUGHTING_CALLOUT('GS_CALLOUT',(#3,#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "GEOMETRIC_SET #4:",
                "DRAUGHTING_CALLOUT #5:");
    }

    @Test
    void shouldPreserveDraughtingCalloutGeometricSetContentEntityNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-callout-geometric-set-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#1);
                #4=GEOMETRIC_SET('GS',(#2));
                #5=DRAUGHTING_CALLOUT('GS_CALLOUT',(#3,#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "GEOMETRIC_SET #4:",
                "DRAUGHTING_CALLOUT #5:");
    }

    @Test
    void shouldReportPathAndWireContentsInDraughtingCalloutBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-callout-path-wire", ".step");
        Files.writeString(file, """
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
                #15=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#1);
                #16=DRAUGHTING_CALLOUT('PATH_CALLOUT',(#15,#11));
                #17=DRAUGHTING_CALLOUT('EDGESET_CALLOUT',(#15,#12));
                #18=DRAUGHTING_CALLOUT('WIRE_CALLOUT',(#15,#14));
                ENDSEC;
                """);

        assertDumpContains(file,
                "PATH #11: built=true, unsupportedFaces=0",
                "CONNECTED_EDGE_SET #12: builtEdges=1, unsupportedFaces=0",
                "WIRE_SHELL #14: builtLoops=1, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #16: builtItems=2, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #17: builtItems=2, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #18: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldReportDirectGeometryContentsInDraughtingCalloutBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-callout-direct-geometry", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "POINT_REPLICA #17: builtItems=2, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #19: builtItems=9, unsupportedFaces=0");
    }

    @Test
    void shouldReportAdvancedCurveContentsInDraughtingCalloutBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-callout-advanced-curve", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "TRIMMED_CURVE #8: builtItems=1, unsupportedFaces=0",
                "ORIENTED_CURVE #9: builtItems=1, unsupportedFaces=0",
                "CURVE_REPLICA #12: builtItems=2, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #14: builtItems=5, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveDraughtingCalloutPathAndWireContentNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-callout-path-wire-names", ".step");
        Files.writeString(file, """
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
                #15=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#1);
                #16=DRAUGHTING_CALLOUT('PATH_CALLOUT',(#15,#11));
                #17=DRAUGHTING_CALLOUT('EDGESET_CALLOUT',(#15,#12));
                #18=DRAUGHTING_CALLOUT('WIRE_CALLOUT',(#15,#14));
                ENDSEC;
                """);

        assertDumpContains(file,
                "PATH #11:",
                "CONNECTED_EDGE_SET #12:",
                "WIRE_SHELL #14:",
                "DRAUGHTING_CALLOUT #16:",
                "DRAUGHTING_CALLOUT #17:",
                "DRAUGHTING_CALLOUT #18:");
    }

    @Test
    void shouldReportExtendedContainerContentsInDraughtingCalloutBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-callout-extended-containers", ".step");
        Files.writeString(file, """
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
                #22=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#1);
                #23=DRAUGHTING_CALLOUT('PL_CALLOUT',(#22,#17));
                #24=DRAUGHTING_CALLOUT('VL_CALLOUT',(#22,#18));
                #25=DRAUGHTING_CALLOUT('VS_CALLOUT',(#22,#19));
                #26=DRAUGHTING_CALLOUT('EBWM_CALLOUT',(#22,#16));
                #27=DRAUGHTING_CALLOUT('SBWM_CALLOUT',(#22,#21));
                ENDSEC;
                """);

        assertDumpContains(file,
                "POLY_LOOP #17: built=true, unsupportedFaces=0",
                "VERTEX_LOOP #18: built=true, unsupportedFaces=0",
                "VERTEX_SHELL #19: builtVertices=1, unsupportedFaces=0",
                "EDGE_BASED_WIREFRAME_MODEL #16: builtEdges=2, unsupportedFaces=0",
                "SHELL_BASED_WIREFRAME_MODEL #21: builtBoundaries=2, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #23: builtItems=2, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #24: builtItems=2, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #25: builtItems=2, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #26: builtItems=3, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #27: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldReportFaceShellAndSurfaceModelContentsInDraughtingCalloutBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-callout-shell-face-models", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "ADVANCED_FACE #80: builtItems=1, unsupportedFaces=0",
                "ORIENTED_FACE #81: built=true, unsupportedFaces=0",
                "FACE_SURFACE #82: builtItems=1, unsupportedFaces=0",
                "OPEN_SHELL #83: faces=1, unsupportedFaces=0",
                "SURFACED_OPEN_SHELL #84: faces=1, unsupportedFaces=0",
                "ORIENTED_OPEN_SHELL #85: faces=1, unsupportedFaces=0",
                "CLOSED_SHELL #86: faces=1, unsupportedFaces=0",
                "ORIENTED_CLOSED_SHELL #87: faces=1, unsupportedFaces=0",
                "CONNECTED_FACE_SET #88: builtItems=1, unsupportedFaces=0",
                "CONNECTED_FACE_SUB_SET #89: builtItems=1, unsupportedFaces=0",
                "FACE_BASED_SURFACE_MODEL #90: faces=2, unsupportedFaces=0",
                "SHELL_BASED_SURFACE_MODEL #91: faces=5, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #93:",
                "DRAUGHTING_CALLOUT #104:");
    }

    @Test
    void shouldReportBrepSolidContentsInDraughtingCalloutBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-callout-brep-solids", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "MANIFOLD_SOLID_BREP #82: shellFaces=1",
                "BREP_WITH_VOIDS #83: shellFaces=1",
                "DRAUGHTING_CALLOUT #85: builtItems=2, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #86: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldReportBuildableSolidFamilyContentsInDraughtingCalloutBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-callout-solid-family", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "BLOCK #5: shellFaces=6, unsupportedFaces=0",
                "BOOLEAN_RESULT #10: faces=6, unsupportedFaces=0",
                "CSG_SOLID #11: shellFaces=6, unsupportedFaces=0",
                "BOOLEAN_CLIPPING_RESULT #12: faces=6, unsupportedFaces=0",
                "EXTRUDED_AREA_SOLID #25: shellFaces=6, unsupportedFaces=0",
                "SOLID_REPLICA #29: shellFaces=6, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #31: builtItems=7, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #32: builtItems=7, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #33: builtItems=7, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #34: builtItems=7, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #35: builtItems=7, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #36: builtItems=7, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveDraughtingCalloutPathAndWireContentEntityNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-callout-path-wire-entity-names", ".step");
        Files.writeString(file, """
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
                #15=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#1);
                #16=DRAUGHTING_CALLOUT('PATH_CALLOUT',(#15,#11));
                #17=DRAUGHTING_CALLOUT('EDGESET_CALLOUT',(#15,#12));
                #18=DRAUGHTING_CALLOUT('WIRE_CALLOUT',(#15,#14));
                ENDSEC;
                """);

        assertDumpContains(file,
                "PATH #11:",
                "CONNECTED_EDGE_SET #12:",
                "WIRE_SHELL #14:",
                "DRAUGHTING_CALLOUT #16:",
                "DRAUGHTING_CALLOUT #17:",
                "DRAUGHTING_CALLOUT #18:");
    }

    @Test
    void shouldReportDirectAnnotationContentInDraughtingCalloutBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-callout-annotation-content", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "ANNOTATION_SYMBOL #12: builtItems=2, unsupportedFaces=0",
                "ANNOTATION_TEXT #15: builtItems=2, unsupportedFaces=0",
                "ANNOTATION_TEXT_CHARACTER #16: builtItems=2, unsupportedFaces=0",
                "ANNOTATION_FILL_AREA #17: builtItems=1, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #20: builtItems=3, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #21: builtItems=3, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #22: builtItems=3, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #23: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveDirectAnnotationContentNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-direct-annotation-content-names", ".step");
        Files.writeString(file, """
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
                ENDSEC;
                """);

        assertDumpContains(file,
                "ANNOTATION_SYMBOL #12:",
                "ANNOTATION_TEXT #15:",
                "ANNOTATION_TEXT_CHARACTER #16:",
                "ANNOTATION_FILL_AREA #17:");
    }

    @Test
    void shouldPreserveDirectAnnotationContentEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-direct-annotation-content-entity-names", ".step");
        Files.writeString(file, """
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
                ENDSEC;
                """);

        assertDumpContains(file,
                "ANNOTATION_SYMBOL #12:",
                "ANNOTATION_TEXT #15:",
                "ANNOTATION_TEXT_CHARACTER #16:",
                "ANNOTATION_FILL_AREA #17:");
    }

    @Test
    void shouldReportOccurrenceWrappersInDraughtingCalloutBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-callout-occurrence-wrappers", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "ANNOTATION_SUBFIGURE_OCCURRENCE #13: builtItems=2, unsupportedFaces=0",
                "DRAUGHTING_ANNOTATION_OCCURRENCE #14: builtItems=2, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #17: builtItems=3, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #18: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldReportAnnotationWrapperOccurrencesWithAdditionalItemsInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-wrapper-additional-items", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "ANNOTATION_PLACEHOLDER_OCCURRENCE #4: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_POINT_OCCURRENCE #5: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_SYMBOL_OCCURRENCE #6: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_SUBFIGURE_OCCURRENCE #7: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveAnnotationOccurrenceWrapperNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-occurrence-wrapper-names", ".step");
        Files.writeString(file, """
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
                ENDSEC;
                """);

        assertDumpContains(file,
                "ANNOTATION_SUBFIGURE_OCCURRENCE #13:",
                "DRAUGHTING_ANNOTATION_OCCURRENCE #14:");
    }

    @Test
    void shouldPreserveAnnotationOccurrenceWrapperEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-occurrence-wrapper-entity-names", ".step");
        Files.writeString(file, """
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
                ENDSEC;
                """);

        assertDumpContains(file,
                "ANNOTATION_SUBFIGURE_OCCURRENCE #13:",
                "DRAUGHTING_ANNOTATION_OCCURRENCE #14:");
    }

    @Test
    void shouldPreserveDraughtingCalloutAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-callout-alias-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#1);
                #3=GEOMETRIC_CURVE_SET('LEADER',(#1));
                #4=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #5=REPRESENTATION('SYMREP',(),#4);
                #6=CARTESIAN_POINT('O',(0.0,0.0));
                #7=DIRECTION('X',(1.0,0.0));
                #8=AXIS2_PLACEMENT_2D('MAP',#6,#7);
                #9=SYMBOL_REPRESENTATION_MAP(#8,#5);
                #10=CARTESIAN_POINT('P',(10.0,20.0));
                #11=DIRECTION('DX',(1.0,0.0));
                #12=AXIS2_PLACEMENT_2D('TGT',#10,#11);
                #13=ANNOTATION_SYMBOL('SYM',#9,#12);
                #14=ANNOTATION_SYMBOL_OCCURRENCE('SO',(),#13);
                #15=LEADER_DIRECTED_CALLOUT('LDC',(#2,#3));
                #16=PROJECTION_DIRECTED_CALLOUT('PDC',(#2,#3));
                #17=DIMENSION_CURVE_DIRECTED_CALLOUT('DCDC',(#2,#3));
                #18=DIMENSION_CALLOUT('DC',(#2,#14));
                #19=STRUCTURED_DIMENSION_CALLOUT('SDC',(#2,#14));
                #20=SURFACE_CONDITION_CALLOUT('SCC',(#2,#3));
                #21=DATUM_FEATURE_CALLOUT('DFC',(#2,#14));
                #22=DATUM_TARGET_CALLOUT('DTC',(#2,#14));
                #23=GEOMETRICAL_TOLERANCE_CALLOUT('GTC',(#2,#14));
                #24=ROUGHNESS_CALLOUT('RC',(#2,#3));
                ENDSEC;
                """);

        assertDumpContains(file,
                "LEADER_DIRECTED_CALLOUT #15: builtItems=2, unsupportedFaces=0",
                "PROJECTION_DIRECTED_CALLOUT #16: builtItems=2, unsupportedFaces=0",
                "DIMENSION_CURVE_DIRECTED_CALLOUT #17: builtItems=2, unsupportedFaces=0",
                "DIMENSION_CALLOUT #18: builtItems=2, unsupportedFaces=0",
                "STRUCTURED_DIMENSION_CALLOUT #19: builtItems=2, unsupportedFaces=0",
                "SURFACE_CONDITION_CALLOUT #20: builtItems=2, unsupportedFaces=0",
                "DATUM_FEATURE_CALLOUT #21: builtItems=2, unsupportedFaces=0",
                "DATUM_TARGET_CALLOUT #22: builtItems=2, unsupportedFaces=0",
                "GEOMETRICAL_TOLERANCE_CALLOUT #23: builtItems=2, unsupportedFaces=0",
                "ROUGHNESS_CALLOUT #24: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveDraughtingCalloutAliasFamilyNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-callout-alias-family-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#1);
                #3=GEOMETRIC_CURVE_SET('LEADER',(#1));
                #4=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #5=REPRESENTATION('SYMREP',(),#4);
                #6=CARTESIAN_POINT('O',(0.0,0.0));
                #7=DIRECTION('X',(1.0,0.0));
                #8=AXIS2_PLACEMENT_2D('MAP',#6,#7);
                #9=SYMBOL_REPRESENTATION_MAP(#8,#5);
                #10=CARTESIAN_POINT('P',(10.0,20.0));
                #11=DIRECTION('DX',(1.0,0.0));
                #12=AXIS2_PLACEMENT_2D('TGT',#10,#11);
                #13=ANNOTATION_SYMBOL('SYM',#9,#12);
                #14=ANNOTATION_SYMBOL_OCCURRENCE('SO',(),#13);
                #15=LEADER_DIRECTED_CALLOUT('LDC',(#2,#3));
                #16=PROJECTION_DIRECTED_CALLOUT('PDC',(#2,#3));
                #17=DIMENSION_CURVE_DIRECTED_CALLOUT('DCDC',(#2,#3));
                #18=DIMENSION_CALLOUT('DC',(#2,#14));
                #19=STRUCTURED_DIMENSION_CALLOUT('SDC',(#2,#14));
                #20=SURFACE_CONDITION_CALLOUT('SCC',(#2,#3));
                #21=DATUM_FEATURE_CALLOUT('DFC',(#2,#14));
                #22=DATUM_TARGET_CALLOUT('DTC',(#2,#14));
                #23=GEOMETRICAL_TOLERANCE_CALLOUT('GTC',(#2,#14));
                #24=ROUGHNESS_CALLOUT('RC',(#2,#3));
                ENDSEC;
                """);

        assertDumpContains(file,
                "LEADER_DIRECTED_CALLOUT #15:",
                "PROJECTION_DIRECTED_CALLOUT #16:",
                "DIMENSION_CURVE_DIRECTED_CALLOUT #17:",
                "DIMENSION_CALLOUT #18:",
                "STRUCTURED_DIMENSION_CALLOUT #19:",
                "SURFACE_CONDITION_CALLOUT #20:",
                "DATUM_FEATURE_CALLOUT #21:",
                "DATUM_TARGET_CALLOUT #22:",
                "GEOMETRICAL_TOLERANCE_CALLOUT #23:",
                "ROUGHNESS_CALLOUT #24:");
    }

    @Test
    void shouldReportAnnotationPlaneWithNestedPointContainersInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-plane-nested-points", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "POINT_SET #8: builtMembers=1, unsupportedFaces=0",
                "GEOMETRIC_CURVE_SET #9: builtMembers=1, unsupportedFaces=0",
                "ANNOTATION_PLANE #11: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldReportAnnotationPlaneWithNestedGeometricSetInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-plane-nested-geometric-set", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "POINT_SET #8: builtMembers=1, unsupportedFaces=0",
                "GEOMETRIC_CURVE_SET #9: builtMembers=1, unsupportedFaces=0",
                "GEOMETRIC_SET #10: builtMembers=2, unsupportedFaces=0",
                "ANNOTATION_PLANE #12: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldReportPointReplicaInAnnotationFillAreaOccurrenceInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-fill-area-replica", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=DIRECTION('DY',(0.0,1.0,0.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=CARTESIAN_POINT('O',(10.0,20.0,30.0));
                #6=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#2,#3,#5,2.0,#4);
                #7=POINT_REPLICA('PR0',#1,#6);
                #8=CARTESIAN_POINT('P1',(0.0,0.0,0.0));
                #9=CARTESIAN_POINT('P2',(1.0,0.0,0.0));
                #10=CARTESIAN_POINT('P3',(1.0,1.0,0.0));
                #11=CARTESIAN_POINT('P4',(0.0,1.0,0.0));
                #12=POLYLINE('B0',(#8,#9,#10,#11,#8));
                #13=(ANNOTATION_FILL_AREA('FA0',(#12))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                #14=PRESENTATION_STYLE_ASSIGNMENT(());
                #15=(ANNOTATION_FILL_AREA_OCCURRENCE('FAO0',(#14),#13,#7)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('FAO0',(#14),#13)
                    STYLED_ITEM('FAO0',(#14),#13)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FAO0'));
                ENDSEC;
                """);

        assertDumpContains(file,
                "POINT_REPLICA #7: builtItems=2, unsupportedFaces=0",
                "ANNOTATION_FILL_AREA #13: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_FILL_AREA_OCCURRENCE #15: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldReportPointReplicaInAnnotationTextOccurrenceInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-text-replica", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=DIRECTION('DY',(0.0,1.0,0.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=CARTESIAN_POINT('O',(10.0,20.0,30.0));
                #6=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#2,#3,#5,2.0,#4);
                #7=POINT_REPLICA('PR0',#1,#6);
                #8=ANNOTATION_TEXT_OCCURRENCE('NOTE','note',#7);
                #9=GEOMETRIC_CURVE_SET('LEADER',(#7,#1));
                #10=DRAUGHTING_CALLOUT('CALLOUT',(#8,#9));
                ENDSEC;
                """);

        assertDumpContains(file,
                "POINT_REPLICA #7: builtItems=2, unsupportedFaces=0",
                "ANNOTATION_TEXT_OCCURRENCE #8: builtItems=1, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #10: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldReportAnnotationPointLikeOccurrencesWithContainerCarriersInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-pointlike-containers", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "ANNOTATION_TEXT_OCCURRENCE #7: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_POINT_OCCURRENCE #8: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_FILL_AREA_OCCURRENCE #11: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldReportNestedPointSetAndAnnotationPlaneOccurrenceElementsInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-plane-point-carriers", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "POINT_SET #3: builtMembers=1, unsupportedFaces=0",
                "GEOMETRIC_SET #5: builtMembers=2, unsupportedFaces=0",
                "POINT_SET #6: builtMembers=2, unsupportedFaces=0",
                "ANNOTATION_TEXT_OCCURRENCE #12: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_POINT_OCCURRENCE #13: builtItems=2, unsupportedFaces=0",
                "ANNOTATION_PLANE #14: builtItems=4, unsupportedFaces=0");
    }

    @Test
    void shouldReportVertexPointInAnnotationPointAndFillAreaOccurrencesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-vertex-point", ".step");
        Files.writeString(file, """
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
                    DRAUGHTING_ANNOTATION_OCCURRENCE('FAO0',(#3),#6)
                    STYLED_ITEM('FAO0',(#3),#6)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FAO0'));
                ENDSEC;
                """);

        assertDumpContains(file,
                "VERTEX_POINT #2: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_POINT_OCCURRENCE #4: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_FILL_AREA_OCCURRENCE #7: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldReportVertexPointInAnnotationTextOccurrenceInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-text-vertex-point", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=ANNOTATION_TEXT_OCCURRENCE('NOTE','vertex',#2);
                #4=GEOMETRIC_CURVE_SET('LEADER',(#1));
                #5=DRAUGHTING_CALLOUT('CALLOUT',(#3,#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "VERTEX_POINT #2: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_TEXT_OCCURRENCE #3: builtItems=1, unsupportedFaces=0",
                "DRAUGHTING_CALLOUT #5: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldReportVertexShellInAnnotationPointLikeOccurrencesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-vertex-shell", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=VERTEX_LOOP('VL0',#2);
                #4=VERTEX_SHELL('VS0',#3);
                #5=PRESENTATION_STYLE_ASSIGNMENT(());
                #6=ANNOTATION_TEXT_OCCURRENCE('NOTE','vertex-shell',#4);
                #7=(ANNOTATION_POINT_OCCURRENCE('AP0',(#5),#4)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('AP0',(#5),#4));
                #8=POLYLINE('B0',(#1,#1));
                #9=(ANNOTATION_FILL_AREA('FA0',(#8))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                #10=(ANNOTATION_FILL_AREA_OCCURRENCE('FAO0',(#5),#9,#4)
                    STYLED_ITEM('FAO0',(#5),#9)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FAO0'));
                ENDSEC;
                """);

        assertDumpContains(file,
                "VERTEX_SHELL #4: builtVertices=1, unsupportedFaces=0",
                "ANNOTATION_TEXT_OCCURRENCE #6: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_POINT_OCCURRENCE #7: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_FILL_AREA_OCCURRENCE #10: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldReportPointLikeAnnotationOccurrenceCarriersInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-annotation-point-like-carriers", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "ANNOTATION_POINT_OCCURRENCE #3: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_TEXT_OCCURRENCE #4: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_FILL_AREA_OCCURRENCE #7: builtItems=2, unsupportedFaces=0",
                "ANNOTATION_POINT_OCCURRENCE #8: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldReportDirectAnnotationContentPointCarriersInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-direct-annotation-content-point-carriers", ".step");
        Files.writeString(file, """
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
                ENDSEC;
                """);

        assertDumpContains(file,
                "ANNOTATION_SYMBOL #9: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_TEXT #13: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_TEXT_CHARACTER #14: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_FILL_AREA #19: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_TEXT_OCCURRENCE #21: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_POINT_OCCURRENCE #22: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_FILL_AREA_OCCURRENCE #23: builtItems=2, unsupportedFaces=0",
                "POINT_SET #24: builtMembers=2, unsupportedFaces=0",
                "ANNOTATION_PLANE #29: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldReportPointSetWithPointLikeAnnotationOccurrenceCarriersInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-point-set-annotation-point-like-carriers", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "ANNOTATION_POINT_OCCURRENCE #3: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_TEXT_OCCURRENCE #4: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_PLANE #9: builtItems=2, unsupportedFaces=0",
                "POINT_SET #10: builtMembers=2, unsupportedFaces=0");
    }

    @Test
    void shouldReportVertexPointInPointAndGeometricCurveContainersInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-vertex-point-containers", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=POINT_SET('PS0',(#2));
                #4=GEOMETRIC_SET('GS0',(#2));
                #5=GEOMETRIC_CURVE_SET('GCS0',(#2));
                ENDSEC;
                """);

        assertDumpContains(file,
                "POINT_SET #3: builtMembers=1, unsupportedFaces=0",
                "GEOMETRIC_SET #4: builtMembers=1, unsupportedFaces=0",
                "GEOMETRIC_CURVE_SET #5: builtMembers=1, unsupportedFaces=0");
    }

    @Test
    void shouldReportPointReplicaWithVertexPointParentInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-point-replica-vertex-parent", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=DIRECTION('DY',(0.0,1.0,0.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=CARTESIAN_POINT('O',(10.0,20.0,30.0));
                #7=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#3,#4,#6,2.0,#5);
                #8=POINT_REPLICA('PR0',#2,#7);
                #9=POINT_SET('PS0',(#8));
                ENDSEC;
                """);

        assertDumpContains(file,
                "POINT_REPLICA #8: builtItems=2, unsupportedFaces=0",
                "POINT_SET #9: builtMembers=1, unsupportedFaces=0");
    }

    @Test
    void shouldReportPresentationStyleEntitiesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-presentation-styles", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0));
                #3=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','STYLECTX'));
                #4=REPRESENTATION('STYLE_REP',(),#3);
                #5=AXIS2_PLACEMENT_2D('MAP',#1,#2);
                #6=REPRESENTATION_MAP(#5,#4);
                #7=USER_DEFINED_CURVE_FONT('UCF0',#6,#5);
                #8=PRE_DEFINED_COLOUR('yellow');
                #9=FILL_AREA_STYLE_COLOUR('',#8);
                #10=FILL_AREA_STYLE('',(#9));
                #11=SURFACE_STYLE_FILL_AREA(#10);
                #12=CURVE_STYLE('CS0',#7,0.25,#8);
                #13=SURFACE_STYLE_BOUNDARY(#12);
                #14=SURFACE_STYLE_PARAMETER_LINE(#12);
                #15=SURFACE_STYLE_CONTROL_GRID(#12);
                #16=SURFACE_STYLE_SEGMENTATION_CURVE(#12);
                #17=SURFACE_STYLE_SILHOUETTE(#12);
                #18=SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR(0.2,0.6,0.4,32.0,#8);
                #19=SURFACE_SIDE_STYLE('SS0',(#11,#13,#14,#15,#16,#17,#18));
                #20=SURFACE_STYLE_USAGE(.BOTH.,#19);
                #21=PRESENTATION_STYLE_ASSIGNMENT((#20));
                #22=TEXT_STYLE_FOR_DEFINED_FONT(#8);
                #23=TEXT_STYLE('TS0',#22);
                #24=TEXT_STYLE_WITH_SPACING('TS1',#22,0.15);
                #25=TEXT_STYLE_WITH_JUSTIFICATION('TS2',#22,.LEFT.);
                #26=TEXT_STYLE_WITH_MIRROR('TS3',#22,#5);
                #27=TEXT_STYLE_WITH_BOX_CHARACTERISTICS('TS4',#22,(BOX_HEIGHT(1.2)));
                #28=SYMBOL_COLOUR(#8);
                ENDSEC;
                """);

        assertDumpContains(file,
                "REPRESENTATION_MAP #6: builtItems=0, unsupportedFaces=0",
                "USER_DEFINED_CURVE_FONT #7: builtItems=1, unsupportedFaces=0",
                "FILL_AREA_STYLE_COLOUR #9: builtItems=1, unsupportedFaces=0",
                "FILL_AREA_STYLE #10: builtItems=1, unsupportedFaces=0",
                "SURFACE_STYLE_FILL_AREA #11: builtItems=1, unsupportedFaces=0",
                "CURVE_STYLE #12: builtItems=2, unsupportedFaces=0",
                "SURFACE_STYLE_BOUNDARY #13: builtItems=2, unsupportedFaces=0",
                "SURFACE_STYLE_PARAMETER_LINE #14: builtItems=2, unsupportedFaces=0",
                "SURFACE_STYLE_CONTROL_GRID #15: builtItems=2, unsupportedFaces=0",
                "SURFACE_STYLE_SEGMENTATION_CURVE #16: builtItems=2, unsupportedFaces=0",
                "SURFACE_STYLE_SILHOUETTE #17: builtItems=2, unsupportedFaces=0",
                "SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR #18: builtItems=2, unsupportedFaces=0",
                "SURFACE_SIDE_STYLE #19: builtItems=13, unsupportedFaces=0",
                "SURFACE_STYLE_USAGE #20: builtItems=13, unsupportedFaces=0",
                "PRESENTATION_STYLE_ASSIGNMENT #21: builtItems=13, unsupportedFaces=0",
                "TEXT_STYLE_FOR_DEFINED_FONT #22: builtItems=1, unsupportedFaces=0",
                "TEXT_STYLE #23: builtItems=1, unsupportedFaces=0",
                "TEXT_STYLE_WITH_SPACING #24: builtItems=1, unsupportedFaces=0",
                "TEXT_STYLE_WITH_JUSTIFICATION #25: builtItems=1, unsupportedFaces=0",
                "TEXT_STYLE_WITH_MIRROR #26: builtItems=2, unsupportedFaces=0",
                "TEXT_STYLE_WITH_BOX_CHARACTERISTICS #27: builtItems=1, unsupportedFaces=0",
                "SYMBOL_COLOUR #28: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreservePresentationStyleNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-presentation-style-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0));
                #3=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','STYLECTX'));
                #4=REPRESENTATION('STYLE_REP',(),#3);
                #5=AXIS2_PLACEMENT_2D('MAP',#1,#2);
                #6=REPRESENTATION_MAP(#5,#4);
                #7=USER_DEFINED_CURVE_FONT('UCF0',#6,#5);
                #8=PRE_DEFINED_COLOUR('yellow');
                #9=FILL_AREA_STYLE_COLOUR('',#8);
                #10=FILL_AREA_STYLE('',(#9));
                #11=SURFACE_STYLE_FILL_AREA(#10);
                #12=CURVE_STYLE('CS0',#7,0.25,#8);
                #13=SURFACE_STYLE_BOUNDARY(#12);
                #14=SURFACE_STYLE_PARAMETER_LINE(#12);
                #15=SURFACE_STYLE_CONTROL_GRID(#12);
                #16=SURFACE_STYLE_SEGMENTATION_CURVE(#12);
                #17=SURFACE_STYLE_SILHOUETTE(#12);
                #18=SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR(0.2,0.6,0.4,32.0,#8);
                #19=SURFACE_SIDE_STYLE('SS0',(#11,#13,#14,#15,#16,#17,#18));
                #20=SURFACE_STYLE_USAGE(.BOTH.,#19);
                #21=PRESENTATION_STYLE_ASSIGNMENT((#20));
                #22=TEXT_STYLE_FOR_DEFINED_FONT(#8);
                #23=TEXT_STYLE('TS0',#22);
                #24=TEXT_STYLE_WITH_SPACING('TS1',#22,0.15);
                #25=TEXT_STYLE_WITH_JUSTIFICATION('TS2',#22,.LEFT.);
                #26=TEXT_STYLE_WITH_MIRROR('TS3',#22,#5);
                #27=TEXT_STYLE_WITH_BOX_CHARACTERISTICS('TS4',#22,(BOX_HEIGHT(1.2)));
                #28=SYMBOL_COLOUR(#8);
                ENDSEC;
                """);

        assertDumpContains(file,
                "USER_DEFINED_CURVE_FONT #7:",
                "FILL_AREA_STYLE_COLOUR #9:",
                "FILL_AREA_STYLE #10:",
                "SURFACE_STYLE_FILL_AREA #11:",
                "CURVE_STYLE #12:",
                "SURFACE_STYLE_BOUNDARY #13:",
                "SURFACE_STYLE_PARAMETER_LINE #14:",
                "SURFACE_STYLE_CONTROL_GRID #15:",
                "SURFACE_STYLE_SEGMENTATION_CURVE #16:",
                "SURFACE_STYLE_SILHOUETTE #17:",
                "SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR #18:",
                "SURFACE_SIDE_STYLE #19:",
                "SURFACE_STYLE_USAGE #20:",
                "PRESENTATION_STYLE_ASSIGNMENT #21:",
                "TEXT_STYLE_FOR_DEFINED_FONT #22:",
                "TEXT_STYLE #23:",
                "TEXT_STYLE_WITH_SPACING #24:",
                "TEXT_STYLE_WITH_JUSTIFICATION #25:",
                "TEXT_STYLE_WITH_MIRROR #26:",
                "TEXT_STYLE_WITH_BOX_CHARACTERISTICS #27:",
                "SYMBOL_COLOUR #28:");
    }

    @Test
    void shouldReportPredefinedAndExternallyDefinedStyleLeavesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-predefined-external-style-leaves", ".step");
        Files.writeString(file, """
                DATA;
                #1=EXTERNAL_SOURCE('supplier-catalog');
                #2=EXTERNALLY_DEFINED_ITEM('item-1',#1);
                #3=EXTERNALLY_DEFINED_CURVE_FONT('font-1',#1);
                #4=EXTERNALLY_DEFINED_MARKER('marker-1',#1);
                #5=EXTERNALLY_DEFINED_SYMBOL('symbol-1',#1);
                #6=EXTERNALLY_DEFINED_TEXT_FONT('text-font-1',#1);
                #7=EXTERNALLY_DEFINED_TERMINATOR_SYMBOL('terminator-1',#1);
                #8=EXTERNALLY_DEFINED_TEXT_STYLE('text-style-1',#1);
                #9=EXTERNALLY_DEFINED_TILE('tile-1',#1);
                #10=PRE_DEFINED_ITEM('generic-item');
                #11=PRE_DEFINED_SYMBOL('diameter');
                #12=PRE_DEFINED_POINT_MARKER_SYMBOL('dot');
                #13=PRE_DEFINED_DIMENSION_SYMBOL('diameter');
                #14=PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL('position');
                #15=PRE_DEFINED_TERMINATOR_SYMBOL('filled_arrow');
                #16=PRE_DEFINED_SURFACE_SIDE_STYLE('both');
                ENDSEC;
                """);

        assertDumpContains(file,
                "EXTERNAL_SOURCE #1: builtItems=1, unsupportedFaces=0",
                "EXTERNALLY_DEFINED_ITEM #2: builtItems=1, unsupportedFaces=0",
                "EXTERNALLY_DEFINED_CURVE_FONT #3: builtItems=1, unsupportedFaces=0",
                "EXTERNALLY_DEFINED_MARKER #4: builtItems=1, unsupportedFaces=0",
                "EXTERNALLY_DEFINED_SYMBOL #5: builtItems=1, unsupportedFaces=0",
                "EXTERNALLY_DEFINED_TEXT_FONT #6: builtItems=1, unsupportedFaces=0",
                "EXTERNALLY_DEFINED_TERMINATOR_SYMBOL #7: builtItems=1, unsupportedFaces=0",
                "EXTERNALLY_DEFINED_TEXT_STYLE #8: builtItems=1, unsupportedFaces=0",
                "EXTERNALLY_DEFINED_TILE #9: builtItems=1, unsupportedFaces=0",
                "PRE_DEFINED_ITEM #10: builtItems=1, unsupportedFaces=0",
                "PRE_DEFINED_SYMBOL #11: builtItems=1, unsupportedFaces=0",
                "PRE_DEFINED_POINT_MARKER_SYMBOL #12: builtItems=1, unsupportedFaces=0",
                "PRE_DEFINED_DIMENSION_SYMBOL #13: builtItems=1, unsupportedFaces=0",
                "PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL #14: builtItems=1, unsupportedFaces=0",
                "PRE_DEFINED_TERMINATOR_SYMBOL #15: builtItems=1, unsupportedFaces=0",
                "PRE_DEFINED_SURFACE_SIDE_STYLE #16: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreservePredefinedAndExternallyDefinedStyleLeafNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-predefined-external-style-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=EXTERNAL_SOURCE('supplier-catalog');
                #2=EXTERNALLY_DEFINED_ITEM('item-1',#1);
                #3=EXTERNALLY_DEFINED_CURVE_FONT('font-1',#1);
                #4=EXTERNALLY_DEFINED_MARKER('marker-1',#1);
                #5=EXTERNALLY_DEFINED_SYMBOL('symbol-1',#1);
                #6=EXTERNALLY_DEFINED_TEXT_FONT('text-font-1',#1);
                #7=EXTERNALLY_DEFINED_TERMINATOR_SYMBOL('terminator-1',#1);
                #8=EXTERNALLY_DEFINED_TEXT_STYLE('text-style-1',#1);
                #9=EXTERNALLY_DEFINED_TILE('tile-1',#1);
                #10=PRE_DEFINED_ITEM('generic-item');
                #11=PRE_DEFINED_SYMBOL('diameter');
                #12=PRE_DEFINED_POINT_MARKER_SYMBOL('dot');
                #13=PRE_DEFINED_DIMENSION_SYMBOL('diameter');
                #14=PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL('position');
                #15=PRE_DEFINED_TERMINATOR_SYMBOL('filled_arrow');
                #16=PRE_DEFINED_SURFACE_SIDE_STYLE('both');
                ENDSEC;
                """);

        assertDumpContains(file,
                "EXTERNALLY_DEFINED_ITEM #2:",
                "EXTERNALLY_DEFINED_CURVE_FONT #3:",
                "EXTERNALLY_DEFINED_MARKER #4:",
                "EXTERNALLY_DEFINED_SYMBOL #5:",
                "EXTERNALLY_DEFINED_TEXT_FONT #6:",
                "EXTERNALLY_DEFINED_TERMINATOR_SYMBOL #7:",
                "EXTERNALLY_DEFINED_TEXT_STYLE #8:",
                "EXTERNALLY_DEFINED_TILE #9:",
                "PRE_DEFINED_ITEM #10:",
                "PRE_DEFINED_SYMBOL #11:",
                "PRE_DEFINED_POINT_MARKER_SYMBOL #12:",
                "PRE_DEFINED_DIMENSION_SYMBOL #13:",
                "PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL #14:",
                "PRE_DEFINED_TERMINATOR_SYMBOL #15:",
                "PRE_DEFINED_SURFACE_SIDE_STYLE #16:");
    }

    @Test
    void shouldReportMetadataRelationshipLeavesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-metadata-leaves", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #10=EXTERNAL_SOURCE('supplier-catalog');
                #11=EXTERNAL_SOURCE('erp');
                #12=EXTERNAL_SOURCE_RELATIONSHIP('SR','catalog to erp',#10,#11);
                #13=GENERAL_PROPERTY('gp-1','material','material property');
                #14=GENERAL_PROPERTY('gp-2','finish','finish property');
                #15=GENERAL_PROPERTY_RELATIONSHIP('GPR','linked property',#13,#14);
                #16=PRODUCT_CATEGORY('hardware','hardware parts');
                #17=PRODUCT_CATEGORY('fastener','fastener parts');
                #18=PRODUCT_CATEGORY_RELATIONSHIP('PCR','parent child',#16,#17);
                #19=DOCUMENT_TYPE('spec');
                #20=DOCUMENT('DOC-1','datasheet','supplier document',#19);
                #21=DOCUMENT_USAGE_CONSTRAINT(#20,'scope','assembly only');
                #22=EFFECTIVITY('E-2');
                #23=EFFECTIVITY('E-3');
                #24=EFFECTIVITY_RELATIONSHIP('ER','range chain',#22,#23);
                #25=LANGUAGE('en-US');
                #26=LANGUAGE_ASSIGNMENT(#25);
                #27=APPLIED_LANGUAGE_ASSIGNMENT(#25,(#4));
                #28=IDENTIFICATION_ROLE('erp id');
                #29=EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT-42',#28,#11);
                #30=APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT-42',#28,#11,(#4));
                #31=ADDRESS('HQ','42','Market St',$,'Shanghai','Shanghai','200000','CN',$,'+86','cad@example.com',$);
                #32=DESCRIPTION_ATTRIBUTE('attribute description',#4);
                #33=NAME_ATTRIBUTE('attribute name',#4);
                #34=ID_ATTRIBUTE('attribute-id',#4);
                ENDSEC;
                """);

        assertDumpContains(file,
                "EXTERNAL_SOURCE_RELATIONSHIP #12: builtItems=2, unsupportedFaces=0",
                "GENERAL_PROPERTY_RELATIONSHIP #15: builtItems=2, unsupportedFaces=0",
                "PRODUCT_CATEGORY_RELATIONSHIP #18: builtItems=2, unsupportedFaces=0",
                "DOCUMENT #20: builtItems=2, unsupportedFaces=0",
                "DOCUMENT_USAGE_CONSTRAINT #21: builtItems=2, unsupportedFaces=0",
                "EFFECTIVITY_RELATIONSHIP #24: builtItems=2, unsupportedFaces=0",
                "LANGUAGE_ASSIGNMENT #26: builtItems=1, unsupportedFaces=0",
                "APPLIED_LANGUAGE_ASSIGNMENT #27: builtItems=2, unsupportedFaces=0",
                "EXTERNAL_IDENTIFICATION_ASSIGNMENT #29: builtItems=2, unsupportedFaces=0",
                "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT #30: builtItems=3, unsupportedFaces=0",
                "ADDRESS #31: builtItems=1, unsupportedFaces=0",
                "DESCRIPTION_ATTRIBUTE #32: builtItems=1, unsupportedFaces=0",
                "NAME_ATTRIBUTE #33: builtItems=1, unsupportedFaces=0",
                "ID_ATTRIBUTE #34: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldReportAssignmentMetadataFamiliesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-assignment-metadata", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #10=GROUP('fasteners','hardware classification');
                #11=CLASSIFICATION_ROLE('part family');
                #12=GROUP_ASSIGNMENT(#10);
                #13=APPLIED_GROUP_ASSIGNMENT(#10,(#4));
                #14=CLASSIFICATION_ASSIGNMENT(#10,#11);
                #15=APPLIED_CLASSIFICATION_ASSIGNMENT(#10,#11,(#4));
                #16=ORGANIZATION('org-1','Acme','supplier');
                #17=ORGANIZATION_ROLE('supplier');
                #18=ORGANIZATION_ASSIGNMENT(#16,#17);
                #19=APPLIED_ORGANIZATION_ASSIGNMENT(#16,#17,(#4));
                #20=NAME_ASSIGNMENT('display name');
                #21=APPLIED_NAME_ASSIGNMENT('display name',(#4));
                #22=APPROVAL_STATUS('approved');
                #23=APPROVAL(#22,'release');
                #24=APPROVAL_ASSIGNMENT(#23);
                #25=APPLIED_APPROVAL_ASSIGNMENT(#23,(#4));
                #26=CONTRACT_TYPE('supply');
                #27=CONTRACT('C-1','supply contract',#26);
                #28=CONTRACT_ASSIGNMENT(#27);
                #29=APPLIED_CONTRACT_ASSIGNMENT(#27,(#4));
                #30=CERTIFICATION_TYPE('iso');
                #31=CERTIFICATION('ISO-1','quality',#30);
                #32=CERTIFICATION_ASSIGNMENT(#31);
                #33=APPLIED_CERTIFICATION_ASSIGNMENT(#31,(#4));
                #34=SECURITY_CLASSIFICATION_LEVEL('internal');
                #35=SECURITY_CLASSIFICATION('SEC-1','internal use',#34);
                #36=SECURITY_CLASSIFICATION_ASSIGNMENT(#35);
                #37=APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT(#35,(#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "GROUP_ASSIGNMENT #12: builtItems=1, unsupportedFaces=0",
                "APPLIED_GROUP_ASSIGNMENT #13: builtItems=2, unsupportedFaces=0",
                "CLASSIFICATION_ASSIGNMENT #14: builtItems=2, unsupportedFaces=0",
                "APPLIED_CLASSIFICATION_ASSIGNMENT #15: builtItems=3, unsupportedFaces=0",
                "ORGANIZATION_ASSIGNMENT #18: builtItems=2, unsupportedFaces=0",
                "APPLIED_ORGANIZATION_ASSIGNMENT #19: builtItems=3, unsupportedFaces=0",
                "NAME_ASSIGNMENT #20: builtItems=1, unsupportedFaces=0",
                "APPLIED_NAME_ASSIGNMENT #21: builtItems=1, unsupportedFaces=0",
                "APPROVAL_ASSIGNMENT #24: builtItems=2, unsupportedFaces=0",
                "APPLIED_APPROVAL_ASSIGNMENT #25: builtItems=3, unsupportedFaces=0",
                "CONTRACT_ASSIGNMENT #28: builtItems=2, unsupportedFaces=0",
                "APPLIED_CONTRACT_ASSIGNMENT #29: builtItems=3, unsupportedFaces=0",
                "CERTIFICATION_ASSIGNMENT #32: builtItems=2, unsupportedFaces=0",
                "APPLIED_CERTIFICATION_ASSIGNMENT #33: builtItems=3, unsupportedFaces=0",
                "SECURITY_CLASSIFICATION_ASSIGNMENT #36: builtItems=2, unsupportedFaces=0",
                "APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT #37: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldReportPersonDateAndRelationshipMetadataInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-person-date-relationships", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #10=PERSON('p-1','Doe','Jane',$,$,$);
                #11=ORGANIZATION('org-1','Acme','engineering');
                #12=PERSON_AND_ORGANIZATION(#10,#11);
                #13=PERSON_AND_ORGANIZATION_ROLE('creator');
                #14=PERSON_AND_ORGANIZATION_ASSIGNMENT(#12,#13);
                #15=APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT(#12,#13,(#4));
                #16=CALENDAR_DATE(2026,11,4);
                #17=COORDINATED_UNIVERSAL_TIME_OFFSET(8,30,.AHEAD.);
                #18=LOCAL_TIME(9,15,$,#17);
                #19=DATE_AND_TIME(#16,#18);
                #20=DATE_ROLE('release');
                #21=DATE_TIME_ROLE('created');
                #22=DATE_ASSIGNMENT(#16,#20);
                #23=APPLIED_DATE_ASSIGNMENT(#16,#20,(#4));
                #24=DATE_TIME_ASSIGNMENT(#19,#21);
                #25=APPLIED_DATE_TIME_ASSIGNMENT(#19,#21,(#4));
                #26=GROUP('fasteners','hardware classification');
                #27=GROUP('hardware','top level');
                #28=GROUP_RELATIONSHIP('GR','relates groups',#26,#27);
                #29=ORGANIZATION('org-2','Supplier','partner');
                #30=ORGANIZATION_RELATIONSHIP('OR','supplier link',#11,#29);
                #31=PRODUCT('ASM','assembly','',());
                #32=PRODUCT('PRT','part','',());
                #33=PRODUCT_RELATIONSHIP('PR','contains','assembly contains part',#31,#32);
                #34=PRODUCT_DEFINITION_FORMATION('v1','first',#31);
                #35=PRODUCT_DEFINITION_FORMATION('v2','second',#32);
                #36=PRODUCT_DEFINITION('def-a','assembly def',#34,#37);
                #37=PRODUCT_DEFINITION_CONTEXT('design',#38,'design');
                #38=APPLICATION_CONTEXT('mechanical');
                #39=PRODUCT_DEFINITION('def-b','part def',#35,#37);
                #40=PRODUCT_DEFINITION_RELATIONSHIP('PDR1','base','base relationship',#36,#39);
                #41=PRODUCT_DEFINITION_RELATIONSHIP('PDR2','peer','peer relationship',#39,#36);
                #42=PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP('PDRR','links','links pdrs',#40,#41);
                #43=PRODUCT_DEFINITION_FORMATION_RELATIONSHIP('PDFR','versions','links formations',#34,#35);
                ENDSEC;
                """);

        assertDumpContains(file,
                "PERSON_AND_ORGANIZATION_ASSIGNMENT #14: builtItems=3, unsupportedFaces=0",
                "APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT #15: builtItems=4, unsupportedFaces=0",
                "DATE_ASSIGNMENT #22: builtItems=2, unsupportedFaces=0",
                "APPLIED_DATE_ASSIGNMENT #23: builtItems=3, unsupportedFaces=0",
                "DATE_TIME_ASSIGNMENT #24: builtItems=4, unsupportedFaces=0",
                "APPLIED_DATE_TIME_ASSIGNMENT #25: builtItems=5, unsupportedFaces=0",
                "GROUP_RELATIONSHIP #28: builtItems=2, unsupportedFaces=0",
                "ORGANIZATION_RELATIONSHIP #30: builtItems=2, unsupportedFaces=0",
                "PRODUCT_RELATIONSHIP #33: builtItems=2, unsupportedFaces=0",
                "PRODUCT_DEFINITION_RELATIONSHIP #40: builtItems=4, unsupportedFaces=0",
                "PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP #42: builtItems=8, unsupportedFaces=0",
                "PRODUCT_DEFINITION_FORMATION_RELATIONSHIP #43: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveAppliedDateTimeAssignmentAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-date-time-assignment-aliases", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #10=CALENDAR_DATE(2026,11,4);
                #11=COORDINATED_UNIVERSAL_TIME_OFFSET(8,30,.AHEAD.);
                #12=LOCAL_TIME(9,15,$,#11);
                #13=DATE_AND_TIME(#10,#12);
                #14=DATE_TIME_ROLE('created');
                #15=APPLIED_DATE_AND_TIME_ASSIGNMENT(#13,#14,(#4));
                #16=APPLIED_DATE_TIME_ASSIGNMENT(#13,#14,(#4));
                #17=CC_DESIGN_DATE_AND_TIME_ASSIGNMENT(#13,#14,(#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "APPLIED_DATE_AND_TIME_ASSIGNMENT #15: builtItems=5, unsupportedFaces=0",
                "APPLIED_DATE_TIME_ASSIGNMENT #16: builtItems=5, unsupportedFaces=0",
                "CC_DESIGN_DATE_AND_TIME_ASSIGNMENT #17: builtItems=5, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveAppliedDateTimeAssignmentAliasEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-date-time-assignment-alias-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #10=CALENDAR_DATE(2026,11,4);
                #11=COORDINATED_UNIVERSAL_TIME_OFFSET(8,30,.AHEAD.);
                #12=LOCAL_TIME(9,15,$,#11);
                #13=DATE_AND_TIME(#10,#12);
                #14=DATE_TIME_ROLE('created');
                #15=APPLIED_DATE_AND_TIME_ASSIGNMENT(#13,#14,(#4));
                #16=APPLIED_DATE_TIME_ASSIGNMENT(#13,#14,(#4));
                #17=CC_DESIGN_DATE_AND_TIME_ASSIGNMENT(#13,#14,(#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "APPLIED_DATE_AND_TIME_ASSIGNMENT #15:",
                "APPLIED_DATE_TIME_ASSIGNMENT #16:",
                "CC_DESIGN_DATE_AND_TIME_ASSIGNMENT #17:");
    }

    @Test
    void shouldPreserveBasicMetadataRelationshipNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-basic-metadata-relationships", ".step");
        Files.writeString(file, """
                DATA;
                #1=GROUP('inspection group','group description');
                #2=GROUP('child group',$);
                #3=GROUP_RELATIONSHIP('group rel','relates groups',#1,#2);
                #4=APPLICATION_CONTEXT('mechanical');
                #5=PRODUCT_CONTEXT('part','',#4);
                #6=PRODUCT('ASM','assembly','',(#5));
                #7=PRODUCT('PRT','part','machined part',(#5));
                #8=PRODUCT_RELATIONSHIP('PR','contains','assembly contains part',#6,#7);
                #9=PRODUCT_DEFINITION_FORMATION('a1','assembly version',#6);
                #10=PRODUCT_DEFINITION_FORMATION('p1','part version',#7);
                #11=PRODUCT_DEFINITION_CONTEXT('design','released',#4);
                #12=PRODUCT_DEFINITION('asm def','assembly definition',#9,#11);
                #13=PRODUCT_DEFINITION('part def','part definition',#10,#11);
                #14=PROPERTY_DEFINITION('prop-a','property a',#12);
                #15=PROPERTY_DEFINITION('prop-b','property b',#13);
                #16=PROPERTY_DEFINITION_RELATIONSHIP('PDRP','property relation',#14,#15);
                ENDSEC;
                """);

        assertDumpContains(file,
                "GROUP_RELATIONSHIP #3: builtItems=2, unsupportedFaces=0",
                "PRODUCT_RELATIONSHIP #8: builtItems=2, unsupportedFaces=0",
                "PROPERTY_DEFINITION_RELATIONSHIP #16: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveBasicMetadataRelationshipEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-basic-metadata-relationship-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=GROUP('inspection group','group description');
                #2=GROUP('child group',$);
                #3=GROUP_RELATIONSHIP('group rel','relates groups',#1,#2);
                #4=APPLICATION_CONTEXT('mechanical');
                #5=PRODUCT_CONTEXT('part','',#4);
                #6=PRODUCT('ASM','assembly','',(#5));
                #7=PRODUCT('PRT','part','machined part',(#5));
                #8=PRODUCT_RELATIONSHIP('PR','contains','assembly contains part',#6,#7);
                #9=PRODUCT_DEFINITION_FORMATION('a1','assembly version',#6);
                #10=PRODUCT_DEFINITION_FORMATION('p1','part version',#7);
                #11=PRODUCT_DEFINITION_CONTEXT('design','released',#4);
                #12=PRODUCT_DEFINITION('asm def','assembly definition',#9,#11);
                #13=PRODUCT_DEFINITION('part def','part definition',#10,#11);
                #14=PROPERTY_DEFINITION('prop-a','property a',#12);
                #15=PROPERTY_DEFINITION('prop-b','property b',#13);
                #16=PROPERTY_DEFINITION_RELATIONSHIP('PDRP','property relation',#14,#15);
                ENDSEC;
                """);

        assertDumpContains(file,
                "GROUP_RELATIONSHIP #3:",
                "PRODUCT_RELATIONSHIP #8:",
                "PROPERTY_DEFINITION_RELATIONSHIP #16:");
    }

    @Test
    void shouldPreserveAdditionalMetadataRelationshipNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-additional-metadata-relationships", ".step");
        Files.writeString(file, """
                DATA;
                #1=EXTERNAL_SOURCE('supplier-catalog');
                #2=EXTERNAL_SOURCE('erp');
                #3=EXTERNAL_SOURCE_RELATIONSHIP('source rel','catalog to erp',#1,#2);
                #4=GENERAL_PROPERTY('gp-1','material','material property');
                #5=GENERAL_PROPERTY('gp-2','finish','finish property');
                #6=GENERAL_PROPERTY_RELATIONSHIP('property rel','linked property',#4,#5);
                #7=PRODUCT_CATEGORY('hardware','hardware parts');
                #8=PRODUCT_CATEGORY('fastener','fastener parts');
                #9=PRODUCT_CATEGORY_RELATIONSHIP('category rel','parent child',#7,#8);
                #10=EFFECTIVITY('E-2');
                #11=EFFECTIVITY('E-3');
                #12=EFFECTIVITY_RELATIONSHIP('effectivity rel','range chain',#10,#11);
                ENDSEC;
                """);

        assertDumpContains(file,
                "EXTERNAL_SOURCE_RELATIONSHIP #3: builtItems=2, unsupportedFaces=0",
                "GENERAL_PROPERTY_RELATIONSHIP #6: builtItems=2, unsupportedFaces=0",
                "PRODUCT_CATEGORY_RELATIONSHIP #9: builtItems=2, unsupportedFaces=0",
                "EFFECTIVITY_RELATIONSHIP #12: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveAdditionalMetadataRelationshipEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-additional-metadata-relationship-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=EXTERNAL_SOURCE('supplier-catalog');
                #2=EXTERNAL_SOURCE('erp');
                #3=EXTERNAL_SOURCE_RELATIONSHIP('source rel','catalog to erp',#1,#2);
                #4=GENERAL_PROPERTY('gp-1','material','material property');
                #5=GENERAL_PROPERTY('gp-2','finish','finish property');
                #6=GENERAL_PROPERTY_RELATIONSHIP('property rel','linked property',#4,#5);
                #7=PRODUCT_CATEGORY('hardware','hardware parts');
                #8=PRODUCT_CATEGORY('fastener','fastener parts');
                #9=PRODUCT_CATEGORY_RELATIONSHIP('category rel','parent child',#7,#8);
                #10=EFFECTIVITY('E-2');
                #11=EFFECTIVITY('E-3');
                #12=EFFECTIVITY_RELATIONSHIP('effectivity rel','range chain',#10,#11);
                ENDSEC;
                """);

        assertDumpContains(file,
                "EXTERNAL_SOURCE_RELATIONSHIP #3:",
                "GENERAL_PROPERTY_RELATIONSHIP #6:",
                "PRODUCT_CATEGORY_RELATIONSHIP #9:",
                "EFFECTIVITY_RELATIONSHIP #12:");
    }

    @Test
    void shouldPreserveDocumentAndExternalIdentificationNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-document-external-identification", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=IDENTIFICATION_ROLE('erp id');
                #6=EXTERNAL_SOURCE('erp');
                #7=EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT-42',#5,#6);
                #8=APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT-42',#5,#6,(#4));
                #9=DOCUMENT_TYPE('spec');
                #10=DOCUMENT('DOC-1','datasheet','supplier document',#9);
                #11=DOCUMENT_USAGE_CONSTRAINT(#10,'scope','assembly only');
                ENDSEC;
                """);

        assertDumpContains(file,
                "EXTERNAL_IDENTIFICATION_ASSIGNMENT #7: builtItems=2, unsupportedFaces=0",
                "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT #8: builtItems=3, unsupportedFaces=0",
                "DOCUMENT #10: builtItems=2, unsupportedFaces=0",
                "DOCUMENT_USAGE_CONSTRAINT #11: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveDocumentAndExternalIdentificationEntityNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-document-external-identification-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=IDENTIFICATION_ROLE('erp id');
                #6=EXTERNAL_SOURCE('erp');
                #7=EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT-42',#5,#6);
                #8=APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT-42',#5,#6,(#4));
                #9=DOCUMENT_TYPE('spec');
                #10=DOCUMENT('DOC-1','datasheet','supplier document',#9);
                #11=DOCUMENT_USAGE_CONSTRAINT(#10,'scope','assembly only');
                ENDSEC;
                """);

        assertDumpContains(file,
                "EXTERNAL_IDENTIFICATION_ASSIGNMENT #7:",
                "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT #8:",
                "DOCUMENT #10:",
                "DOCUMENT_USAGE_CONSTRAINT #11:");
    }

    @Test
    void shouldPreserveLanguageAndAttributeNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-language-attributes", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=LANGUAGE('en-US');
                #6=LANGUAGE_ASSIGNMENT(#5);
                #7=APPLIED_LANGUAGE_ASSIGNMENT(#5,(#4));
                #8=ADDRESS('HQ','42','Market St',$,'Shanghai','Shanghai','200000','CN',$,'+86','cad@example.com',$);
                #9=DESCRIPTION_ATTRIBUTE('attribute description',#4);
                #10=NAME_ATTRIBUTE('attribute name',#4);
                #11=ID_ATTRIBUTE('attribute-id',#4);
                ENDSEC;
                """);

        assertDumpContains(file,
                "LANGUAGE_ASSIGNMENT #6: builtItems=1, unsupportedFaces=0",
                "APPLIED_LANGUAGE_ASSIGNMENT #7: builtItems=2, unsupportedFaces=0",
                "ADDRESS #8: builtItems=1, unsupportedFaces=0",
                "DESCRIPTION_ATTRIBUTE #9: builtItems=1, unsupportedFaces=0",
                "NAME_ATTRIBUTE #10: builtItems=1, unsupportedFaces=0",
                "ID_ATTRIBUTE #11: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveLanguageAndAttributeEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-language-attribute-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=LANGUAGE('en-US');
                #6=LANGUAGE_ASSIGNMENT(#5);
                #7=APPLIED_LANGUAGE_ASSIGNMENT(#5,(#4));
                #8=ADDRESS('HQ','42','Market St',$,'Shanghai','Shanghai','200000','CN',$,'+86','cad@example.com',$);
                #9=DESCRIPTION_ATTRIBUTE('attribute description',#4);
                #10=NAME_ATTRIBUTE('attribute name',#4);
                #11=ID_ATTRIBUTE('attribute-id',#4);
                ENDSEC;
                """);

        assertDumpContains(file,
                "LANGUAGE_ASSIGNMENT #6:",
                "APPLIED_LANGUAGE_ASSIGNMENT #7:",
                "ADDRESS #8:",
                "DESCRIPTION_ATTRIBUTE #9:",
                "NAME_ATTRIBUTE #10:",
                "ID_ATTRIBUTE #11:");
    }

    @Test
    void shouldPreserveClassificationIdentificationAndNameAssignmentsInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-classification-identification-name", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=GROUP('fasteners','hardware classification');
                #5=CLASSIFICATION_ROLE('part family');
                #6=CLASSIFICATION_ASSIGNMENT(#4,#5);
                #7=APPLIED_CLASSIFICATION_ASSIGNMENT(#4,#5,(#3));
                #8=IDENTIFICATION_ROLE('erp id');
                #9=IDENTIFICATION_ASSIGNMENT('ERP-42',#8);
                #10=APPLIED_IDENTIFICATION_ASSIGNMENT('ERP-42',#8,(#3));
                #11=NAME_ASSIGNMENT('display name');
                #12=APPLIED_NAME_ASSIGNMENT('display name',(#3));
                ENDSEC;
                """);

        assertDumpContains(file,
                "CLASSIFICATION_ASSIGNMENT #6: builtItems=2, unsupportedFaces=0",
                "APPLIED_CLASSIFICATION_ASSIGNMENT #7: builtItems=3, unsupportedFaces=0",
                "IDENTIFICATION_ASSIGNMENT #9: builtItems=1, unsupportedFaces=0",
                "APPLIED_IDENTIFICATION_ASSIGNMENT #10: builtItems=2, unsupportedFaces=0",
                "NAME_ASSIGNMENT #11: builtItems=1, unsupportedFaces=0",
                "APPLIED_NAME_ASSIGNMENT #12: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveClassificationIdentificationAndNameAssignmentEntityNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-classification-identification-name-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=GROUP('fasteners','hardware classification');
                #5=CLASSIFICATION_ROLE('part family');
                #6=CLASSIFICATION_ASSIGNMENT(#4,#5);
                #7=APPLIED_CLASSIFICATION_ASSIGNMENT(#4,#5,(#3));
                #8=IDENTIFICATION_ROLE('erp id');
                #9=IDENTIFICATION_ASSIGNMENT('ERP-42',#8);
                #10=APPLIED_IDENTIFICATION_ASSIGNMENT('ERP-42',#8,(#3));
                #11=NAME_ASSIGNMENT('display name');
                #12=APPLIED_NAME_ASSIGNMENT('display name',(#3));
                ENDSEC;
                """);

        assertDumpContains(file,
                "CLASSIFICATION_ASSIGNMENT #6:",
                "APPLIED_CLASSIFICATION_ASSIGNMENT #7:",
                "IDENTIFICATION_ASSIGNMENT #9:",
                "APPLIED_IDENTIFICATION_ASSIGNMENT #10:",
                "NAME_ASSIGNMENT #11:",
                "APPLIED_NAME_ASSIGNMENT #12:");
    }

    @Test
    void shouldPreserveClassificationAndExternalIdentificationNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-classification-external-identification", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=GROUP('fasteners','hardware classification');
                #5=CLASSIFICATION_ROLE('part family');
                #6=CLASSIFICATION_ASSIGNMENT(#4,#5);
                #7=APPLIED_CLASSIFICATION_ASSIGNMENT(#4,#5,(#3));
                #8=IDENTIFICATION_ROLE('erp id');
                #9=EXTERNAL_SOURCE('erp');
                #10=EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT-42',#8,#9);
                #11=APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT-42',#8,#9,(#3));
                ENDSEC;
                """);

        assertDumpContains(file,
                "CLASSIFICATION_ROLE #5: builtItems=1, unsupportedFaces=0",
                "CLASSIFICATION_ASSIGNMENT #6: builtItems=2, unsupportedFaces=0",
                "APPLIED_CLASSIFICATION_ASSIGNMENT #7: builtItems=3, unsupportedFaces=0",
                "IDENTIFICATION_ROLE #8: builtItems=1, unsupportedFaces=0",
                "EXTERNAL_SOURCE #9: builtItems=1, unsupportedFaces=0",
                "EXTERNAL_IDENTIFICATION_ASSIGNMENT #10: builtItems=2, unsupportedFaces=0",
                "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT #11: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveClassificationAndExternalIdentificationEntityNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-classification-external-identification-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=GROUP('fasteners','hardware classification');
                #5=CLASSIFICATION_ROLE('part family');
                #6=CLASSIFICATION_ASSIGNMENT(#4,#5);
                #7=APPLIED_CLASSIFICATION_ASSIGNMENT(#4,#5,(#3));
                #8=IDENTIFICATION_ROLE('erp id');
                #9=EXTERNAL_SOURCE('erp');
                #10=EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT-42',#8,#9);
                #11=APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT-42',#8,#9,(#3));
                ENDSEC;
                """);

        assertDumpContains(file,
                "CLASSIFICATION_ASSIGNMENT #6:",
                "APPLIED_CLASSIFICATION_ASSIGNMENT #7:",
                "EXTERNAL_IDENTIFICATION_ASSIGNMENT #10:",
                "APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT #11:");
    }

    @Test
    void shouldPreserveNameAssignmentAndAttributeNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-name-attribute-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=NAME_ASSIGNMENT('display name');
                #5=APPLIED_NAME_ASSIGNMENT('display name',(#3));
                #6=DESCRIPTION_ATTRIBUTE('attribute description',#3);
                #7=NAME_ATTRIBUTE('attribute name',#3);
                #8=ID_ATTRIBUTE('attribute-id',#3);
                ENDSEC;
                """);

        assertDumpContains(file,
                "NAME_ASSIGNMENT #4: builtItems=1, unsupportedFaces=0",
                "APPLIED_NAME_ASSIGNMENT #5: builtItems=1, unsupportedFaces=0",
                "DESCRIPTION_ATTRIBUTE #6: builtItems=1, unsupportedFaces=0",
                "NAME_ATTRIBUTE #7: builtItems=1, unsupportedFaces=0",
                "ID_ATTRIBUTE #8: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveNameAssignmentAndAttributeEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-name-attribute-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=NAME_ASSIGNMENT('display name');
                #5=APPLIED_NAME_ASSIGNMENT('display name',(#3));
                #6=DESCRIPTION_ATTRIBUTE('attribute description',#3);
                #7=NAME_ATTRIBUTE('attribute name',#3);
                #8=ID_ATTRIBUTE('attribute-id',#3);
                ENDSEC;
                """);

        assertDumpContains(file,
                "NAME_ASSIGNMENT #4:",
                "APPLIED_NAME_ASSIGNMENT #5:",
                "DESCRIPTION_ATTRIBUTE #6:",
                "NAME_ATTRIBUTE #7:",
                "ID_ATTRIBUTE #8:");
    }

    @Test
    void shouldPreservePropertyCategoryAndEffectivityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-property-category-effectivity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=GENERAL_PROPERTY('gp-1','material','material property');
                #2=GENERAL_PROPERTY('gp-2','finish','finish property');
                #3=GENERAL_PROPERTY_RELATIONSHIP('property rel','linked property',#1,#2);
                #4=PRODUCT_CATEGORY('hardware','hardware parts');
                #5=PRODUCT_CATEGORY('fastener','fastener parts');
                #6=PRODUCT_CATEGORY_RELATIONSHIP('category rel','parent child',#4,#5);
                #7=EFFECTIVITY('E-2');
                #8=EFFECTIVITY('E-3');
                #9=EFFECTIVITY_RELATIONSHIP('effectivity rel','supersedes',#7,#8);
                ENDSEC;
                """);

        assertDumpContains(file,
                "GENERAL_PROPERTY #1: builtItems=1, unsupportedFaces=0",
                "GENERAL_PROPERTY #2: builtItems=1, unsupportedFaces=0",
                "GENERAL_PROPERTY_RELATIONSHIP #3: builtItems=2, unsupportedFaces=0",
                "PRODUCT_CATEGORY #4: builtItems=1, unsupportedFaces=0",
                "PRODUCT_CATEGORY #5: builtItems=1, unsupportedFaces=0",
                "PRODUCT_CATEGORY_RELATIONSHIP #6: builtItems=2, unsupportedFaces=0",
                "EFFECTIVITY #7: builtItems=1, unsupportedFaces=0",
                "EFFECTIVITY #8: builtItems=1, unsupportedFaces=0",
                "EFFECTIVITY_RELATIONSHIP #9: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreservePropertyCategoryAndEffectivityEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-property-category-effectivity-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=GENERAL_PROPERTY('gp-1','material','material property');
                #2=GENERAL_PROPERTY('gp-2','finish','finish property');
                #3=GENERAL_PROPERTY_RELATIONSHIP('property rel','linked property',#1,#2);
                #4=PRODUCT_CATEGORY('hardware','hardware parts');
                #5=PRODUCT_CATEGORY('fastener','fastener parts');
                #6=PRODUCT_CATEGORY_RELATIONSHIP('category rel','parent child',#4,#5);
                #7=EFFECTIVITY('E-2');
                #8=EFFECTIVITY('E-3');
                #9=EFFECTIVITY_RELATIONSHIP('effectivity rel','supersedes',#7,#8);
                ENDSEC;
                """);

        assertDumpContains(file,
                "GENERAL_PROPERTY #1:",
                "GENERAL_PROPERTY #2:",
                "GENERAL_PROPERTY_RELATIONSHIP #3:",
                "PRODUCT_CATEGORY #4:",
                "PRODUCT_CATEGORY #5:",
                "PRODUCT_CATEGORY_RELATIONSHIP #6:",
                "EFFECTIVITY #7:",
                "EFFECTIVITY #8:",
                "EFFECTIVITY_RELATIONSHIP #9:");
    }

    @Test
    void shouldPreserveGroupAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-group-alias-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=GROUP('plain group','plain grouping');
                #2=CLASS('class group','classified grouping');
                #3=CLASS_SYSTEM('class system','classification root');
                #4=GROUP_RELATIONSHIP('group rel','relates groups',#2,#3);
                ENDSEC;
                """);

        assertDumpContains(file,
                "GROUP #1: builtItems=1, unsupportedFaces=0",
                "CLASS #2: builtItems=1, unsupportedFaces=0",
                "CLASS_SYSTEM #3: builtItems=1, unsupportedFaces=0",
                "GROUP_RELATIONSHIP #4: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveGroupAliasEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-group-alias-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=GROUP('plain group','plain grouping');
                #2=CLASS('class group','classified grouping');
                #3=CLASS_SYSTEM('class system','classification root');
                #4=GROUP_RELATIONSHIP('group rel','relates groups',#2,#3);
                ENDSEC;
                """);

        assertDumpContains(file,
                "GROUP #1:",
                "CLASS #2:",
                "CLASS_SYSTEM #3:",
                "GROUP_RELATIONSHIP #4:");
    }

    @Test
    void shouldPreserveProductRelationshipNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-product-relationship-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('ASM','assembly','',(#2));
                #4=PRODUCT('PRT','part','machined part',(#2));
                #5=PRODUCT_RELATIONSHIP('PR','contains','assembly contains part',#3,#4);
                ENDSEC;
                """);

        assertDumpContains(file,
                "PRODUCT #3: builtItems=1, unsupportedFaces=0",
                "PRODUCT #4: builtItems=1, unsupportedFaces=0",
                "PRODUCT_RELATIONSHIP #5: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveProductRelationshipEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-product-relationship-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('ASM','assembly','',(#2));
                #4=PRODUCT('PRT','part','machined part',(#2));
                #5=PRODUCT_RELATIONSHIP('PR','contains','assembly contains part',#3,#4);
                ENDSEC;
                """);

        assertDumpContains(file,
                "PRODUCT #3:",
                "PRODUCT #4:",
                "PRODUCT_RELATIONSHIP #5:");
    }

    @Test
    void shouldPreserveAppliedAssignmentFamiliesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-applied-assignment-families", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=GROUP('fasteners','hardware classification');
                #6=GROUP_ASSIGNMENT(#5);
                #7=APPLIED_GROUP_ASSIGNMENT(#5,(#4));
                #8=ORGANIZATION('org-1','Acme','supplier');
                #9=ORGANIZATION_ROLE('supplier');
                #10=ORGANIZATION_ASSIGNMENT(#8,#9);
                #11=APPLIED_ORGANIZATION_ASSIGNMENT(#8,#9,(#4));
                #12=APPROVAL_STATUS('approved');
                #13=APPROVAL(#12,'design');
                #14=APPROVAL_ASSIGNMENT(#13);
                #15=APPLIED_APPROVAL_ASSIGNMENT(#13,(#4));
                #16=SECURITY_CLASSIFICATION_LEVEL('unclassified');
                #17=SECURITY_CLASSIFICATION('sec','export',#16);
                #18=SECURITY_CLASSIFICATION_ASSIGNMENT(#17);
                #19=APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT(#17,(#4));
                #20=CONTRACT_TYPE('purchase');
                #21=CONTRACT('C-1','supply',#20);
                #22=CONTRACT_ASSIGNMENT(#21);
                #23=APPLIED_CONTRACT_ASSIGNMENT(#21,(#4));
                #24=CERTIFICATION_TYPE('material');
                #25=CERTIFICATION('CERT-1','compliance',#24);
                #26=CERTIFICATION_ASSIGNMENT(#25);
                #27=APPLIED_CERTIFICATION_ASSIGNMENT(#25,(#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "GROUP_ASSIGNMENT #6: builtItems=1, unsupportedFaces=0",
                "APPLIED_GROUP_ASSIGNMENT #7: builtItems=2, unsupportedFaces=0",
                "ORGANIZATION_ASSIGNMENT #10: builtItems=2, unsupportedFaces=0",
                "APPLIED_ORGANIZATION_ASSIGNMENT #11: builtItems=3, unsupportedFaces=0",
                "APPROVAL_ASSIGNMENT #14: builtItems=2, unsupportedFaces=0",
                "APPLIED_APPROVAL_ASSIGNMENT #15: builtItems=3, unsupportedFaces=0",
                "SECURITY_CLASSIFICATION_ASSIGNMENT #18: builtItems=2, unsupportedFaces=0",
                "APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT #19: builtItems=3, unsupportedFaces=0",
                "CONTRACT_ASSIGNMENT #22: builtItems=2, unsupportedFaces=0",
                "APPLIED_CONTRACT_ASSIGNMENT #23: builtItems=3, unsupportedFaces=0",
                "CERTIFICATION_ASSIGNMENT #26: builtItems=2, unsupportedFaces=0",
                "APPLIED_CERTIFICATION_ASSIGNMENT #27: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveAppliedAssignmentFamilyEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-applied-assignment-family-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=GROUP('fasteners','hardware classification');
                #6=GROUP_ASSIGNMENT(#5);
                #7=APPLIED_GROUP_ASSIGNMENT(#5,(#4));
                #8=ORGANIZATION('org-1','Acme','supplier');
                #9=ORGANIZATION_ROLE('supplier');
                #10=ORGANIZATION_ASSIGNMENT(#8,#9);
                #11=APPLIED_ORGANIZATION_ASSIGNMENT(#8,#9,(#4));
                #12=APPROVAL_STATUS('approved');
                #13=APPROVAL(#12,'design');
                #14=APPROVAL_ASSIGNMENT(#13);
                #15=APPLIED_APPROVAL_ASSIGNMENT(#13,(#4));
                #16=SECURITY_CLASSIFICATION_LEVEL('unclassified');
                #17=SECURITY_CLASSIFICATION('sec','export',#16);
                #18=SECURITY_CLASSIFICATION_ASSIGNMENT(#17);
                #19=APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT(#17,(#4));
                #20=CONTRACT_TYPE('purchase');
                #21=CONTRACT('C-1','supply',#20);
                #22=CONTRACT_ASSIGNMENT(#21);
                #23=APPLIED_CONTRACT_ASSIGNMENT(#21,(#4));
                #24=CERTIFICATION_TYPE('material');
                #25=CERTIFICATION('CERT-1','compliance',#24);
                #26=CERTIFICATION_ASSIGNMENT(#25);
                #27=APPLIED_CERTIFICATION_ASSIGNMENT(#25,(#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "GROUP_ASSIGNMENT #6:",
                "APPLIED_GROUP_ASSIGNMENT #7:",
                "ORGANIZATION_ASSIGNMENT #10:",
                "APPLIED_ORGANIZATION_ASSIGNMENT #11:",
                "APPROVAL_ASSIGNMENT #14:",
                "APPLIED_APPROVAL_ASSIGNMENT #15:",
                "SECURITY_CLASSIFICATION_ASSIGNMENT #18:",
                "APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT #19:",
                "CONTRACT_ASSIGNMENT #22:",
                "APPLIED_CONTRACT_ASSIGNMENT #23:",
                "CERTIFICATION_ASSIGNMENT #26:",
                "APPLIED_CERTIFICATION_ASSIGNMENT #27:");
    }

    @Test
    void shouldPreservePersonAndDateAssignmentNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-person-date-assignments", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=PERSON('p-1','Doe','Jane',$,$,$);
                #6=ORGANIZATION('org-1','Acme','engineering');
                #7=PERSON_AND_ORGANIZATION(#5,#6);
                #8=PERSON_AND_ORGANIZATION_ROLE('creator');
                #9=PERSON_AND_ORGANIZATION_ASSIGNMENT(#7,#8);
                #10=APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT(#7,#8,(#4));
                #11=CALENDAR_DATE(2026,11,4);
                #12=DATE_ROLE('released');
                #13=DATE_ASSIGNMENT(#11,#12);
                #14=APPLIED_DATE_ASSIGNMENT(#11,#12,(#4));
                #15=COORDINATED_UNIVERSAL_TIME_OFFSET(8,30,.AHEAD.);
                #16=LOCAL_TIME(9,15,$,#15);
                #17=DATE_AND_TIME(#11,#16);
                #18=DATE_TIME_ROLE('created');
                #19=DATE_TIME_ASSIGNMENT(#17,#18);
                #20=APPLIED_DATE_TIME_ASSIGNMENT(#17,#18,(#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "PERSON_AND_ORGANIZATION_ASSIGNMENT #9: builtItems=3, unsupportedFaces=0",
                "APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT #10: builtItems=4, unsupportedFaces=0",
                "DATE_ASSIGNMENT #13: builtItems=2, unsupportedFaces=0",
                "APPLIED_DATE_ASSIGNMENT #14: builtItems=3, unsupportedFaces=0",
                "DATE_TIME_ASSIGNMENT #19: builtItems=4, unsupportedFaces=0",
                "APPLIED_DATE_TIME_ASSIGNMENT #20: builtItems=5, unsupportedFaces=0");
    }

    @Test
    void shouldPreservePersonAndDateAssignmentEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-person-date-assignment-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=PERSON('p-1','Doe','Jane',$,$,$);
                #6=ORGANIZATION('org-1','Acme','engineering');
                #7=PERSON_AND_ORGANIZATION(#5,#6);
                #8=PERSON_AND_ORGANIZATION_ROLE('creator');
                #9=PERSON_AND_ORGANIZATION_ASSIGNMENT(#7,#8);
                #10=APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT(#7,#8,(#4));
                #11=CALENDAR_DATE(2026,11,4);
                #12=DATE_ROLE('released');
                #13=DATE_ASSIGNMENT(#11,#12);
                #14=APPLIED_DATE_ASSIGNMENT(#11,#12,(#4));
                #15=COORDINATED_UNIVERSAL_TIME_OFFSET(8,30,.AHEAD.);
                #16=LOCAL_TIME(9,15,$,#15);
                #17=DATE_AND_TIME(#11,#16);
                #18=DATE_TIME_ROLE('created');
                #19=DATE_TIME_ASSIGNMENT(#17,#18);
                #20=APPLIED_DATE_TIME_ASSIGNMENT(#17,#18,(#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "PERSON_AND_ORGANIZATION_ASSIGNMENT #9:",
                "APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT #10:",
                "DATE_ASSIGNMENT #13:",
                "APPLIED_DATE_ASSIGNMENT #14:",
                "DATE_TIME_ASSIGNMENT #19:",
                "APPLIED_DATE_TIME_ASSIGNMENT #20:");
    }

    @Test
    void shouldPreserveDocumentAndApprovalRelationshipNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-document-approval-relationships", ".step");
        Files.writeString(file, """
                DATA;
                #1=DOCUMENT_TYPE('drawing');
                #2=DOCUMENT('DOC-1','Spec','primary spec',#1);
                #3=DOCUMENT('DOC-2','Spec child',$,#1);
                #4=DOCUMENT_RELATIONSHIP('doc rel','revision link',#2,#3);
                #5=PERSON('p-1','Doe','Jane',('Q'),('Dr.'),('PE'));
                #6=ORGANIZATION('org-1','Acme','engineering');
                #7=PERSON_AND_ORGANIZATION(#5,#6);
                #8=APPROVAL_ROLE('creator');
                #9=APPROVAL_STATUS('approved');
                #10=APPROVAL(#9,'design');
                #11=APPROVAL_PERSON_ORGANIZATION(#7,#10,#8);
                #12=CALENDAR_DATE(2026,11,4);
                #13=COORDINATED_UNIVERSAL_TIME_OFFSET(8,30,.AHEAD.);
                #14=LOCAL_TIME(9,15,$,#13);
                #15=DATE_AND_TIME(#12,#14);
                #16=APPROVAL_DATE_TIME(#15,#10);
                ENDSEC;
                """);

        assertDumpContains(file,
                "DOCUMENT_RELATIONSHIP #4: builtItems=4, unsupportedFaces=0",
                "APPROVAL_PERSON_ORGANIZATION #11: builtItems=5, unsupportedFaces=0",
                "APPROVAL_DATE_TIME #16: builtItems=5, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveDocumentAndApprovalRelationshipEntityNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-document-approval-relationship-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=DOCUMENT_TYPE('drawing');
                #2=DOCUMENT('DOC-1','Spec','primary spec',#1);
                #3=DOCUMENT('DOC-2','Spec child',$,#1);
                #4=DOCUMENT_RELATIONSHIP('doc rel','revision link',#2,#3);
                #5=PERSON('p-1','Doe','Jane',('Q'),('Dr.'),('PE'));
                #6=ORGANIZATION('org-1','Acme','engineering');
                #7=PERSON_AND_ORGANIZATION(#5,#6);
                #8=APPROVAL_ROLE('creator');
                #9=APPROVAL_STATUS('approved');
                #10=APPROVAL(#9,'design');
                #11=APPROVAL_PERSON_ORGANIZATION(#7,#10,#8);
                #12=CALENDAR_DATE(2026,11,4);
                #13=COORDINATED_UNIVERSAL_TIME_OFFSET(8,30,.AHEAD.);
                #14=LOCAL_TIME(9,15,$,#13);
                #15=DATE_AND_TIME(#12,#14);
                #16=APPROVAL_DATE_TIME(#15,#10);
                ENDSEC;
                """);

        assertDumpContains(file,
                "DOCUMENT_RELATIONSHIP #4:",
                "APPROVAL_PERSON_ORGANIZATION #11:",
                "APPROVAL_DATE_TIME #16:");
    }

    @Test
    void shouldPreserveDocumentReferenceAndApprovalAssignmentNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-document-reference-approval-assignment", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=DOCUMENT_TYPE('spec');
                #6=DOCUMENT('DOC-1','Spec','primary spec',#5);
                #7=DOCUMENT_REFERENCE(#6,'internal');
                #8=APPLIED_DOCUMENT_REFERENCE(#6,'internal',(#4));
                #9=APPROVAL_STATUS('approved');
                #10=APPROVAL(#9,'design');
                #11=APPROVAL_ASSIGNMENT(#10);
                #12=APPLIED_APPROVAL_ASSIGNMENT(#10,(#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "DOCUMENT_REFERENCE #7: builtItems=2, unsupportedFaces=0",
                "APPLIED_DOCUMENT_REFERENCE #8: builtItems=3, unsupportedFaces=0",
                "APPROVAL_ASSIGNMENT #11: builtItems=2, unsupportedFaces=0",
                "APPLIED_APPROVAL_ASSIGNMENT #12: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveDocumentReferenceAndApprovalAssignmentEntityNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-document-reference-approval-assignment-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=DOCUMENT_TYPE('spec');
                #6=DOCUMENT('DOC-1','Spec','primary spec',#5);
                #7=DOCUMENT_REFERENCE(#6,'internal');
                #8=APPLIED_DOCUMENT_REFERENCE(#6,'internal',(#4));
                #9=APPROVAL_STATUS('approved');
                #10=APPROVAL(#9,'design');
                #11=APPROVAL_ASSIGNMENT(#10);
                #12=APPLIED_APPROVAL_ASSIGNMENT(#10,(#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "DOCUMENT_REFERENCE #7:",
                "APPLIED_DOCUMENT_REFERENCE #8:",
                "APPROVAL_ASSIGNMENT #11:",
                "APPLIED_APPROVAL_ASSIGNMENT #12:");
    }

    @Test
    void shouldPreservePersonOrganizationAndDateLeafNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-person-organization-date-leaves", ".step");
        Files.writeString(file, """
                DATA;
                #1=PERSON('p-1','Doe','Jane',('Q'),('Dr.'),('PE'));
                #2=ORGANIZATION('org-1','Acme','engineering');
                #3=PERSON_AND_ORGANIZATION(#1,#2);
                #4=PERSON_AND_ORGANIZATION_ROLE('creator');
                #5=ORGANIZATION_ROLE('supplier');
                #6=CALENDAR_DATE(2026,11,4);
                #7=COORDINATED_UNIVERSAL_TIME_OFFSET(8,30,.AHEAD.);
                #8=LOCAL_TIME(9,15,$,#7);
                #9=DATE_AND_TIME(#6,#8);
                #10=DATE_ROLE('released');
                #11=DATE_TIME_ROLE('created');
                ENDSEC;
                """);

        assertDumpContains(file,
                "PERSON #1: builtItems=1, unsupportedFaces=0",
                "ORGANIZATION #2: builtItems=1, unsupportedFaces=0",
                "PERSON_AND_ORGANIZATION #3: builtItems=2, unsupportedFaces=0",
                "PERSON_AND_ORGANIZATION_ROLE #4: builtItems=1, unsupportedFaces=0",
                "ORGANIZATION_ROLE #5: builtItems=1, unsupportedFaces=0",
                "CALENDAR_DATE #6: builtItems=1, unsupportedFaces=0",
                "COORDINATED_UNIVERSAL_TIME_OFFSET #7: builtItems=1, unsupportedFaces=0",
                "LOCAL_TIME #8: builtItems=2, unsupportedFaces=0",
                "DATE_AND_TIME #9: builtItems=3, unsupportedFaces=0",
                "DATE_ROLE #10: builtItems=1, unsupportedFaces=0",
                "DATE_TIME_ROLE #11: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreservePersonOrganizationAndDateLeafEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-person-organization-date-leaf-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=PERSON('p-1','Doe','Jane',('Q'),('Dr.'),('PE'));
                #2=ORGANIZATION('org-1','Acme','engineering');
                #3=PERSON_AND_ORGANIZATION(#1,#2);
                #4=PERSON_AND_ORGANIZATION_ROLE('creator');
                #5=ORGANIZATION_ROLE('supplier');
                #6=CALENDAR_DATE(2026,11,4);
                #7=COORDINATED_UNIVERSAL_TIME_OFFSET(8,30,.AHEAD.);
                #8=LOCAL_TIME(9,15,$,#7);
                #9=DATE_AND_TIME(#6,#8);
                #10=DATE_ROLE('released');
                #11=DATE_TIME_ROLE('created');
                ENDSEC;
                """);

        assertDumpContains(file,
                "PERSON #1:",
                "ORGANIZATION #2:",
                "PERSON_AND_ORGANIZATION #3:",
                "PERSON_AND_ORGANIZATION_ROLE #4:",
                "ORGANIZATION_ROLE #5:",
                "CALENDAR_DATE #6:",
                "COORDINATED_UNIVERSAL_TIME_OFFSET #7:",
                "LOCAL_TIME #8:",
                "DATE_AND_TIME #9:",
                "DATE_ROLE #10:",
                "DATE_TIME_ROLE #11:");
    }

    @Test
    void shouldPreserveOrganizationLanguageAndExternalSourceNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-organization-language-external-source", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=ORGANIZATION('org-1','Acme','engineering');
                #6=ORGANIZATION('org-2','Supplier','vendor');
                #7=ORGANIZATION_RELATIONSHIP('org rel','supplier link',#5,#6);
                #8=LANGUAGE('en-US');
                #9=LANGUAGE_ASSIGNMENT(#8);
                #10=APPLIED_LANGUAGE_ASSIGNMENT(#8,(#4));
                #11=EXTERNAL_SOURCE('supplier-catalog');
                #12=EXTERNAL_SOURCE('erp');
                #13=EXTERNAL_SOURCE_RELATIONSHIP('source rel','catalog to erp',#11,#12);
                ENDSEC;
                """);

        assertDumpContains(file,
                "ORGANIZATION #5: builtItems=1, unsupportedFaces=0",
                "ORGANIZATION #6: builtItems=1, unsupportedFaces=0",
                "ORGANIZATION_RELATIONSHIP #7: builtItems=2, unsupportedFaces=0",
                "LANGUAGE #8: builtItems=1, unsupportedFaces=0",
                "LANGUAGE_ASSIGNMENT #9: builtItems=1, unsupportedFaces=0",
                "APPLIED_LANGUAGE_ASSIGNMENT #10: builtItems=2, unsupportedFaces=0",
                "EXTERNAL_SOURCE #11: builtItems=1, unsupportedFaces=0",
                "EXTERNAL_SOURCE #12: builtItems=1, unsupportedFaces=0",
                "EXTERNAL_SOURCE_RELATIONSHIP #13: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveOrganizationLanguageAndExternalSourceEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-organization-language-external-source-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=ORGANIZATION('org-1','Acme','engineering');
                #6=ORGANIZATION('org-2','Supplier','vendor');
                #7=ORGANIZATION_RELATIONSHIP('org rel','supplier link',#5,#6);
                #8=LANGUAGE('en-US');
                #9=LANGUAGE_ASSIGNMENT(#8);
                #10=APPLIED_LANGUAGE_ASSIGNMENT(#8,(#4));
                #11=EXTERNAL_SOURCE('supplier-catalog');
                #12=EXTERNAL_SOURCE('erp');
                #13=EXTERNAL_SOURCE_RELATIONSHIP('source rel','catalog to erp',#11,#12);
                ENDSEC;
                """);

        assertDumpContains(file,
                "ORGANIZATION #5:",
                "ORGANIZATION #6:",
                "ORGANIZATION_RELATIONSHIP #7:",
                "LANGUAGE #8:",
                "LANGUAGE_ASSIGNMENT #9:",
                "APPLIED_LANGUAGE_ASSIGNMENT #10:",
                "EXTERNAL_SOURCE #11:",
                "EXTERNAL_SOURCE #12:",
                "EXTERNAL_SOURCE_RELATIONSHIP #13:");
    }

    @Test
    void shouldPreserveAssignmentRoleAndTypeLeafNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-assignment-role-type-leaves", ".step");
        Files.writeString(file, """
                DATA;
                #1=CLASSIFICATION_ROLE('part family');
                #2=IDENTIFICATION_ROLE('erp id');
                #3=APPROVAL_STATUS('approved');
                #4=APPROVAL_ROLE('signoff');
                #5=CONTRACT_TYPE('supply');
                #6=CERTIFICATION_TYPE('iso');
                #7=SECURITY_CLASSIFICATION_LEVEL('internal');
                ENDSEC;
                """);

        assertDumpContains(file,
                "CLASSIFICATION_ROLE #1: builtItems=1, unsupportedFaces=0",
                "IDENTIFICATION_ROLE #2: builtItems=1, unsupportedFaces=0",
                "APPROVAL_STATUS #3: builtItems=1, unsupportedFaces=0",
                "APPROVAL_ROLE #4: builtItems=1, unsupportedFaces=0",
                "CONTRACT_TYPE #5: builtItems=1, unsupportedFaces=0",
                "CERTIFICATION_TYPE #6: builtItems=1, unsupportedFaces=0",
                "SECURITY_CLASSIFICATION_LEVEL #7: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveAssignmentRoleAndTypeEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-assignment-role-type-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CLASSIFICATION_ROLE('part family');
                #2=IDENTIFICATION_ROLE('erp id');
                #3=APPROVAL_STATUS('approved');
                #4=APPROVAL_ROLE('signoff');
                #5=CONTRACT_TYPE('supply');
                #6=CERTIFICATION_TYPE('iso');
                #7=SECURITY_CLASSIFICATION_LEVEL('internal');
                ENDSEC;
                """);

        assertDumpContains(file,
                "CLASSIFICATION_ROLE #1:",
                "IDENTIFICATION_ROLE #2:",
                "APPROVAL_STATUS #3:",
                "APPROVAL_ROLE #4:",
                "CONTRACT_TYPE #5:",
                "CERTIFICATION_TYPE #6:",
                "SECURITY_CLASSIFICATION_LEVEL #7:");
    }

    @Test
    void shouldPreserveBasicMetadataLeafNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-basic-metadata-leaves", ".step");
        Files.writeString(file, """
                DATA;
                #1=DOCUMENT_TYPE('spec');
                #2=EXTERNAL_SOURCE('supplier-catalog');
                #3=EFFECTIVITY('E-2');
                #4=PRODUCT_CATEGORY('hardware','hardware parts');
                #5=GENERAL_PROPERTY('gp-1','material','material property');
                ENDSEC;
                """);

        assertDumpContains(file,
                "DOCUMENT_TYPE #1: builtItems=1, unsupportedFaces=0",
                "EXTERNAL_SOURCE #2: builtItems=1, unsupportedFaces=0",
                "EFFECTIVITY #3: builtItems=1, unsupportedFaces=0",
                "PRODUCT_CATEGORY #4: builtItems=1, unsupportedFaces=0",
                "GENERAL_PROPERTY #5: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveBasicMetadataLeafEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-basic-metadata-leaf-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=DOCUMENT_TYPE('spec');
                #2=EXTERNAL_SOURCE('supplier-catalog');
                #3=EFFECTIVITY('E-2');
                #4=PRODUCT_CATEGORY('hardware','hardware parts');
                #5=GENERAL_PROPERTY('gp-1','material','material property');
                ENDSEC;
                """);

        assertDumpContains(file,
                "DOCUMENT_TYPE #1:",
                "EXTERNAL_SOURCE #2:",
                "EFFECTIVITY #3:",
                "PRODUCT_CATEGORY #4:",
                "GENERAL_PROPERTY #5:");
    }

    @Test
    void shouldPreserveDocumentApprovalAndClassificationLeafNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-document-approval-classification-leaves", ".step");
        Files.writeString(file, """
                DATA;
                #1=DOCUMENT_TYPE('drawing');
                #2=DOCUMENT('DOC-1','Spec','primary spec',#1);
                #3=APPROVAL_STATUS('approved');
                #4=APPROVAL(#3,'design');
                #5=CONTRACT_TYPE('purchase');
                #6=CONTRACT('C-1','supply',#5);
                #7=CERTIFICATION_TYPE('material');
                #8=CERTIFICATION('CERT-1','compliance',#7);
                #9=SECURITY_CLASSIFICATION_LEVEL('internal');
                #10=SECURITY_CLASSIFICATION('SEC-1','internal use',#9);
                ENDSEC;
                """);

        assertDumpContains(file,
                "DOCUMENT #2: builtItems=2, unsupportedFaces=0",
                "APPROVAL #4: builtItems=2, unsupportedFaces=0",
                "CONTRACT #6: builtItems=2, unsupportedFaces=0",
                "CERTIFICATION #8: builtItems=2, unsupportedFaces=0",
                "SECURITY_CLASSIFICATION #10: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveDocumentApprovalAndClassificationEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-document-approval-classification-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=DOCUMENT_TYPE('drawing');
                #2=DOCUMENT('DOC-1','Spec','primary spec',#1);
                #3=APPROVAL_STATUS('approved');
                #4=APPROVAL(#3,'design');
                #5=CONTRACT_TYPE('purchase');
                #6=CONTRACT('C-1','supply',#5);
                #7=CERTIFICATION_TYPE('material');
                #8=CERTIFICATION('CERT-1','compliance',#7);
                #9=SECURITY_CLASSIFICATION_LEVEL('internal');
                #10=SECURITY_CLASSIFICATION('SEC-1','internal use',#9);
                ENDSEC;
                """);

        assertDumpContains(file,
                "DOCUMENT #2:",
                "APPROVAL #4:",
                "CONTRACT #6:",
                "CERTIFICATION #8:",
                "SECURITY_CLASSIFICATION #10:");
    }

    @Test
    void shouldPreserveDocumentLeafAndConstraintNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-document-leaf-constraint-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=DOCUMENT_TYPE('spec');
                #2=DOCUMENT('DOC-1','Spec','primary spec',#1);
                #3=DOCUMENT_USAGE_CONSTRAINT(#2,'scope','assembly only');
                ENDSEC;
                """);

        assertDumpContains(file,
                "DOCUMENT_TYPE #1: builtItems=1, unsupportedFaces=0",
                "DOCUMENT #2: builtItems=2, unsupportedFaces=0",
                "DOCUMENT_USAGE_CONSTRAINT #3: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveDocumentLeafAndConstraintEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-document-leaf-constraint-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=DOCUMENT_TYPE('spec');
                #2=DOCUMENT('DOC-1','Spec','primary spec',#1);
                #3=DOCUMENT_USAGE_CONSTRAINT(#2,'scope','assembly only');
                ENDSEC;
                """);

        assertDumpContains(file,
                "DOCUMENT_TYPE #1:",
                "DOCUMENT #2:",
                "DOCUMENT_USAGE_CONSTRAINT #3:");
    }

    @Test
    void shouldPreserveApprovalLeafAndRelationshipNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-approval-leaf-relationship-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=PERSON('p-1','Doe','Jane',('Q'),('Dr.'),('PE'));
                #2=ORGANIZATION('org-1','Acme','engineering');
                #3=PERSON_AND_ORGANIZATION(#1,#2);
                #4=APPROVAL_ROLE('creator');
                #5=APPROVAL_STATUS('approved');
                #6=APPROVAL(#5,'design');
                #7=APPROVAL_PERSON_ORGANIZATION(#3,#6,#4);
                #8=CALENDAR_DATE(2026,11,4);
                #9=COORDINATED_UNIVERSAL_TIME_OFFSET(8,30,.AHEAD.);
                #10=LOCAL_TIME(9,15,$,#9);
                #11=DATE_AND_TIME(#8,#10);
                #12=APPROVAL_DATE_TIME(#11,#6);
                ENDSEC;
                """);

        assertDumpContains(file,
                "APPROVAL_ROLE #4: builtItems=1, unsupportedFaces=0",
                "APPROVAL_STATUS #5: builtItems=1, unsupportedFaces=0",
                "APPROVAL #6: builtItems=2, unsupportedFaces=0",
                "APPROVAL_PERSON_ORGANIZATION #7: builtItems=5, unsupportedFaces=0",
                "APPROVAL_DATE_TIME #12: builtItems=5, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveApprovalLeafAndRelationshipEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-approval-leaf-relationship-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=PERSON('p-1','Doe','Jane',('Q'),('Dr.'),('PE'));
                #2=ORGANIZATION('org-1','Acme','engineering');
                #3=PERSON_AND_ORGANIZATION(#1,#2);
                #4=APPROVAL_ROLE('creator');
                #5=APPROVAL_STATUS('approved');
                #6=APPROVAL(#5,'design');
                #7=APPROVAL_PERSON_ORGANIZATION(#3,#6,#4);
                #8=CALENDAR_DATE(2026,11,4);
                #9=COORDINATED_UNIVERSAL_TIME_OFFSET(8,30,.AHEAD.);
                #10=LOCAL_TIME(9,15,$,#9);
                #11=DATE_AND_TIME(#8,#10);
                #12=APPROVAL_DATE_TIME(#11,#6);
                ENDSEC;
                """);

        assertDumpContains(file,
                "APPROVAL_ROLE #4:",
                "APPROVAL_STATUS #5:",
                "APPROVAL #6:",
                "APPROVAL_PERSON_ORGANIZATION #7:",
                "APPROVAL_DATE_TIME #12:");
    }

    @Test
    void shouldPreservePersonAndOrganizationNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-person-organization-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=PERSON('p-1','Doe','Jane',('Q'),('Dr.'),('PE'));
                #2=ORGANIZATION('org-1','Acme','engineering');
                #3=PERSON_AND_ORGANIZATION(#1,#2);
                #4=PERSON_AND_ORGANIZATION_ROLE('creator');
                #5=ORGANIZATION_ROLE('supplier');
                #6=ORGANIZATION('org-2','Vendor','supply');
                #7=ORGANIZATION_RELATIONSHIP('org rel','supplier link',#2,#6);
                ENDSEC;
                """);

        assertDumpContains(file,
                "PERSON #1: builtItems=1, unsupportedFaces=0",
                "ORGANIZATION #2: builtItems=1, unsupportedFaces=0",
                "PERSON_AND_ORGANIZATION #3: builtItems=2, unsupportedFaces=0",
                "PERSON_AND_ORGANIZATION_ROLE #4: builtItems=1, unsupportedFaces=0",
                "ORGANIZATION_ROLE #5: builtItems=1, unsupportedFaces=0",
                "ORGANIZATION #6: builtItems=1, unsupportedFaces=0",
                "ORGANIZATION_RELATIONSHIP #7: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreservePersonAndOrganizationEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-person-organization-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=PERSON('p-1','Doe','Jane',('Q'),('Dr.'),('PE'));
                #2=ORGANIZATION('org-1','Acme','engineering');
                #3=PERSON_AND_ORGANIZATION(#1,#2);
                #4=PERSON_AND_ORGANIZATION_ROLE('creator');
                #5=ORGANIZATION_ROLE('supplier');
                #6=ORGANIZATION('org-2','Vendor','supply');
                #7=ORGANIZATION_RELATIONSHIP('org rel','supplier link',#2,#6);
                ENDSEC;
                """);

        assertDumpContains(file,
                "PERSON #1:",
                "ORGANIZATION #2:",
                "PERSON_AND_ORGANIZATION #3:",
                "PERSON_AND_ORGANIZATION_ROLE #4:",
                "ORGANIZATION_ROLE #5:",
                "ORGANIZATION #6:",
                "ORGANIZATION_RELATIONSHIP #7:");
    }

    @Test
    void shouldPreserveApplicationProductAndContextNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-application-product-context", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','first release',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('def','part definition',#4,#5);
                ENDSEC;
                """);

        assertDumpContains(file,
                "APPLICATION_CONTEXT #1: builtItems=1, unsupportedFaces=0",
                "PRODUCT_CONTEXT #2: builtItems=1, unsupportedFaces=0",
                "PRODUCT #3: builtItems=1, unsupportedFaces=0",
                "PRODUCT_DEFINITION_FORMATION #4: builtItems=1, unsupportedFaces=0",
                "PRODUCT_DEFINITION_CONTEXT #5: builtItems=1, unsupportedFaces=0",
                "PRODUCT_DEFINITION #6: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveApplicationProductAndContextEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-application-product-context-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','first release',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('def','part definition',#4,#5);
                ENDSEC;
                """);

        assertDumpContains(file,
                "APPLICATION_CONTEXT #1:",
                "PRODUCT_CONTEXT #2:",
                "PRODUCT #3:",
                "PRODUCT_DEFINITION_FORMATION #4:",
                "PRODUCT_DEFINITION_CONTEXT #5:",
                "PRODUCT_DEFINITION #6:");
    }

    @Test
    void shouldPreserveShapeUsageMetadataNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-shape-usage-metadata", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','first release',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('def','part definition',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape def',#6);
                #8=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','SHAPE');
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=SHAPE_REPRESENTATION('SR',(#8),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=REPRESENTATION_RELATIONSHIP('RR','rep rel',#10,#10);
                #13=NEXT_ASSEMBLY_USAGE_OCCURRENCE('NAUO','next usage','',#6,#6);
                #14=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#12,#13);
                ENDSEC;
                """);

        assertDumpContains(file,
                "PRODUCT_DEFINITION_SHAPE #7: builtItems=2, unsupportedFaces=0",
                "SHAPE_DEFINITION_REPRESENTATION #11: builtItems=3, unsupportedFaces=0",
                "CONTEXT_DEPENDENT_SHAPE_REPRESENTATION #14: builtItems=6, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveShapeUsageEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-shape-usage-metadata-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','first release',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('def','part definition',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape def',#6);
                #8=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','SHAPE');
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #10=SHAPE_REPRESENTATION('SR',(#8),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                #12=REPRESENTATION_RELATIONSHIP('RR','rep rel',#10,#10);
                #13=NEXT_ASSEMBLY_USAGE_OCCURRENCE('NAUO','next usage','',#6,#6);
                #14=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#12,#13);
                ENDSEC;
                """);

        assertDumpContains(file,
                "PRODUCT_DEFINITION_SHAPE #7:",
                "SHAPE_DEFINITION_REPRESENTATION #11:",
                "CONTEXT_DEPENDENT_SHAPE_REPRESENTATION #14:");
    }

    @Test
    void shouldPreserveRepresentationUsageNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-representation-usage", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','first release',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('def','part definition',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('shape','primary shape',#6);
                #8=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','NODE_A');
                #9=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','NODE_B');
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #11=SHAPE_REPRESENTATION('SR',(#8),#10);
                #12=REPRESENTATION('R2',(#9),#10);
                #13=REPRESENTATION_RELATIONSHIP('RR','rep rel',#11,#12);
                #14=ITEM_IDENTIFIED_REPRESENTATION_USAGE('IU','generic',#7,#11,#6);
                #15=CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE('CIU','chain',#7,(#11,#12),(#13),#6);
                ENDSEC;
                """);

        assertDumpContains(file,
                "ITEM_IDENTIFIED_REPRESENTATION_USAGE #14: builtItems=5, unsupportedFaces=0",
                "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE #15: builtItems=8, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveRepresentationUsageEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-representation-usage-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','first release',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('def','part definition',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('shape','primary shape',#6);
                #8=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','NODE_A');
                #9=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','NODE_B');
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #11=SHAPE_REPRESENTATION('SR',(#8),#10);
                #12=REPRESENTATION('R2',(#9),#10);
                #13=REPRESENTATION_RELATIONSHIP('RR','rep rel',#11,#12);
                #14=ITEM_IDENTIFIED_REPRESENTATION_USAGE('IU','generic',#7,#11,#6);
                #15=CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE('CIU','chain',#7,(#11,#12),(#13),#6);
                ENDSEC;
                """);

        assertDumpContains(file,
                "ITEM_IDENTIFIED_REPRESENTATION_USAGE #14:",
                "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE #15:");
    }

    @Test
    void shouldPreserveGeometricItemSpecificUsageNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-geometric-item-specific-usage", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('COMP','Component','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('comp_pd','',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('comp_shape','',#6);
                #8=SHAPE_ASPECT('SA','base',#7,.T.);
                #9=PROPERTY_DEFINITION('PD','',#8);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #11=REPRESENTATION('REP_A',(),#10);
                #12=REPRESENTATION('REP_B',(),#10);
                #13=REPRESENTATION('REP_C',(),#10);
                #14=REPRESENTATION_RELATIONSHIP('RR','chain',#12,#13);
                #15=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #16=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#15);
                #17=GEOMETRIC_CURVE_SET('LEADER',(#15));
                #18=DRAUGHTING_CALLOUT('CALLOUT',(#16,#17));
                #19=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU','',#18,#11);
                #20=CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE('CGU','',#18,(#12,#13),(#14),#11);
                ENDSEC;
                """);

        assertDumpContains(file,
                "GEOMETRIC_ITEM_SPECIFIC_USAGE #19: builtItems=2, unsupportedFaces=0",
                "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE #20: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveGeometricItemSpecificUsageEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-geometric-item-specific-usage-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('COMP','Component','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('comp_pd','',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('comp_shape','',#6);
                #8=SHAPE_ASPECT('SA','base',#7,.T.);
                #9=PROPERTY_DEFINITION('PD','',#8);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #11=REPRESENTATION('REP_A',(),#10);
                #12=REPRESENTATION('REP_B',(),#10);
                #13=REPRESENTATION('REP_C',(),#10);
                #14=REPRESENTATION_RELATIONSHIP('RR','chain',#12,#13);
                #15=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #16=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#15);
                #17=GEOMETRIC_CURVE_SET('LEADER',(#15));
                #18=DRAUGHTING_CALLOUT('CALLOUT',(#16,#17));
                #19=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU','',#18,#11);
                #20=CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE('CGU','',#18,(#12,#13),(#14),#11);
                ENDSEC;
                """);

        assertDumpContains(file,
                "GEOMETRIC_ITEM_SPECIFIC_USAGE #19:",
                "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE #20:");
    }

    @Test
    void shouldPreserveAssociationUsageNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-association-usage", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('COMP','Component','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('comp_pd','',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('comp_shape','',#6);
                #8=SHAPE_ASPECT('SA','base',#7,.T.);
                #9=PROPERTY_DEFINITION('PD','',#8);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #11=REPRESENTATION('REP_A',(),#10);
                #12=REPRESENTATION('REP_B',(),#10);
                #13=REPRESENTATION('REP_C',(),#10);
                #14=REPRESENTATION_RELATIONSHIP('RR','chain',#12,#13);
                #15=ITEM_IDENTIFIED_REPRESENTATION_USAGE('IU','',#9,#11,#8);
                #16=CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE('CIU','',#9,(#12,#13),(#14),#8);
                #17=DRAUGHTING_MODEL_ITEM_ASSOCIATION('DMA','',#15,#11,#8);
                #18=PMI_REQUIREMENT_ITEM_ASSOCIATION('PMI','',#16,#11,#8,#8);
                #19=MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION('MDR','',#17,#11,#8,#8);
                #20=PLACED_TARGET('PT','',#18,#12,#8);
                ENDSEC;
                """);

        assertDumpContains(file,
                "DRAUGHTING_MODEL_ITEM_ASSOCIATION #17: builtItems=6, unsupportedFaces=0",
                "PMI_REQUIREMENT_ITEM_ASSOCIATION #18: builtItems=8, unsupportedFaces=0",
                "MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION #19: builtItems=10, unsupportedFaces=0",
                "PLACED_TARGET #20: builtItems=10, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveAssociationUsageEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-association-usage-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('COMP','Component','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('comp_pd','',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('comp_shape','',#6);
                #8=SHAPE_ASPECT('SA','base',#7,.T.);
                #9=PROPERTY_DEFINITION('PD','',#8);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #11=REPRESENTATION('REP_A',(),#10);
                #12=REPRESENTATION('REP_B',(),#10);
                #13=REPRESENTATION('REP_C',(),#10);
                #14=REPRESENTATION_RELATIONSHIP('RR','chain',#12,#13);
                #15=ITEM_IDENTIFIED_REPRESENTATION_USAGE('IU','',#9,#11,#8);
                #16=CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE('CIU','',#9,(#12,#13),(#14),#8);
                #17=DRAUGHTING_MODEL_ITEM_ASSOCIATION('DMA','',#15,#11,#8);
                #18=PMI_REQUIREMENT_ITEM_ASSOCIATION('PMI','',#16,#11,#8,#8);
                #19=MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION('MDR','',#17,#11,#8,#8);
                #20=PLACED_TARGET('PT','',#18,#12,#8);
                ENDSEC;
                """);

        assertDumpContains(file,
                "DRAUGHTING_MODEL_ITEM_ASSOCIATION #17:",
                "PMI_REQUIREMENT_ITEM_ASSOCIATION #18:",
                "MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION #19:",
                "PLACED_TARGET #20:");
    }

    @Test
    void shouldReportUsageAndAssociationFamilyWithVertexLoopTargetInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-association-usage-vertex-loop", ".step");
        Files.writeString(file, """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #3=REPRESENTATION('REP_A',(#1),#2);
                #4=REPRESENTATION('REP_B',(#1),#2);
                #5=REPRESENTATION('REP_C',(#1),#2);
                #6=REPRESENTATION_RELATIONSHIP('RR','chain',#4,#5);
                #7=PROPERTY_DEFINITION('PD','',#1);
                #8=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #9=VERTEX_POINT('VP0',#8);
                #10=VERTEX_LOOP('VLOOP',#9);
                #11=ITEM_IDENTIFIED_REPRESENTATION_USAGE('USAGE','generic',#7,#3,#10);
                #12=CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE('CBIIRU','chain',#7,(#4,#5),(#6),#10);
                #13=DRAUGHTING_MODEL_ITEM_ASSOCIATION('DMA','assoc',#7,#3,#10);
                #14=PMI_REQUIREMENT_ITEM_ASSOCIATION('PRIA','req',#7,#3,#10,#7);
                #15=MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION('MDRIA','mdreq',#7,#3,#10,#7);
                #16=PLACED_TARGET('PT','target',#7,#3,#10);
                ENDSEC;
                """);

        assertDumpContains(file,
                "VERTEX_LOOP #10: built=true, unsupportedFaces=0",
                "ITEM_IDENTIFIED_REPRESENTATION_USAGE #11:",
                "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE #12:",
                "DRAUGHTING_MODEL_ITEM_ASSOCIATION #13:",
                "PMI_REQUIREMENT_ITEM_ASSOCIATION #14:",
                "MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION #15:",
                "PLACED_TARGET #16:");
    }

    @Test
    void shouldPreserveAssociationWithPlaceholderNameInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-association-with-placeholder", ".step");
        Files.writeString(file, """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PMICTX'));
                #3=DRAUGHTING_MODEL('DM',(#1),#2);
                #4=PROPERTY_DEFINITION('PD','',#1);
                #5=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #6=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#5);
                #7=GEOMETRIC_CURVE_SET('LEADER',(#5));
                #8=GEOMETRIC_SET('PHSET',(#5));
                #9=DRAUGHTING_CALLOUT('CALLOUT',(#6,#7));
                #10=ANNOTATION_PLACEHOLDER_OCCURRENCE('PH',(),#8,.TITLE.,1.0);
                #11=DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER('DMIAP','assocph',#4,#3,#9,#10);
                ENDSEC;
                """);

        assertDumpContains(file,
                "DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER #11: builtItems=5, unsupportedFaces=0");
    }

    @Test
    void shouldReportExtendedAnnotationUsageFamilyInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-extended-annotation-usage-family", ".step");
        Files.writeString(file, """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PMICTX'));
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

        assertDumpContains(file,
                "ANNOTATION_POINT_OCCURRENCE #7: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_PLACEHOLDER_OCCURRENCE #9: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_SYMBOL_OCCURRENCE #10: builtItems=1, unsupportedFaces=0",
                "GEOMETRIC_ITEM_SPECIFIC_USAGE #15: builtItems=1, unsupportedFaces=0",
                "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE #16: builtItems=1, unsupportedFaces=0",
                "DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER #17:");
    }

    @Test
    void shouldReportDirectAnnotationContentUsageFamiliesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-direct-annotation-content-usage-family", ".step");
        Files.writeString(file, """
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','ANN'));
                #2=REPRESENTATION('REP_USED',(),#1);
                #3=REPRESENTATION('REP_A',(),#1);
                #4=REPRESENTATION('REP_B',(),#1);
                #5=REPRESENTATION_RELATIONSHIP('RR','chain',#3,#4);
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
                #25=POINT_SET('PS0',(#20));
                #26=ANNOTATION_PLACEHOLDER_OCCURRENCE('PH',(),#25,.TITLE.,1.0);
                #27=ITEM_IDENTIFIED_REPRESENTATION_USAGE('IU','',#6,#2,#14);
                #28=CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE('CIU','',#6,(#3,#4),(#5),#18);
                #29=DRAUGHTING_MODEL_ITEM_ASSOCIATION('DMA','',#6,#2,#19);
                #30=PMI_REQUIREMENT_ITEM_ASSOCIATION('PMI','',#6,#2,#24,#6);
                #31=MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION('MDRIA','',#6,#2,#14,#6);
                #32=PLACED_TARGET('PT','',#6,#2,#18);
                #33=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU','',#24,#2);
                #34=CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE('CGIU','',#14,(#3,#4),(#5),#2);
                #35=DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER('DMIAP','',#6,#2,#19,#26);
                ENDSEC;
                """);

        assertDumpContains(file,
                "ANNOTATION_SYMBOL #14: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_TEXT #18: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_TEXT_CHARACTER #19: builtItems=1, unsupportedFaces=0",
                "ANNOTATION_FILL_AREA #24: builtItems=1, unsupportedFaces=0",
                "ITEM_IDENTIFIED_REPRESENTATION_USAGE #27:",
                "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE #28:",
                "DRAUGHTING_MODEL_ITEM_ASSOCIATION #29:",
                "PMI_REQUIREMENT_ITEM_ASSOCIATION #30:",
                "MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION #31:",
                "PLACED_TARGET #32:",
                "GEOMETRIC_ITEM_SPECIFIC_USAGE #33:",
                "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE #34:",
                "DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER #35:");
    }

    @Test
    void shouldReportGeometricItemSpecificUsageWithPathAndWireTargetsInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-usage-path-wire-targets", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "OPEN_PATH #10: built=true, unsupportedFaces=0",
                "CONNECTED_EDGE_SET #11: builtEdges=1, unsupportedFaces=0",
                "GEOMETRIC_ITEM_SPECIFIC_USAGE #18:",
                "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE #19:");
    }

    @Test
    void shouldReportGeometricItemSpecificUsageWithShellModelAndSolidTargetsInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-usage-shell-model-solid-targets", ".step");
        Files.writeString(file, """
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
                #22=ANNOTATION_TEXT_OCCURRENCE('NOTE_A','',#1);
                #23=ANNOTATION_TEXT_OCCURRENCE('NOTE_B','',#2);
                #24=BLOCK('BLK',#7,1.0,1.0,1.0);
                #25=POINT_SET('PS',(#1,#2));
                #26=GEOMETRIC_CURVE_SET('GCS',(#25));
                #27=GEOMETRIC_SET('GS',(#26));
                #28=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU','',#22,#16);
                #29=CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE('CGU','',#23,(#19,#20),(#21),#17);
                #30=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU_SOLID','',#22,#24);
                #31=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU_SET','',#22,#27);
                ENDSEC;
                """);

        assertDumpContains(file,
                "OPEN_SHELL #16: faces=1, unsupportedFaces=0",
                "FACE_BASED_SURFACE_MODEL #17: faces=1, unsupportedFaces=0",
                "BLOCK #24: shellFaces=",
                "POINT_SET #25: builtMembers=2, unsupportedFaces=0",
                "GEOMETRIC_CURVE_SET #26: builtMembers=1, unsupportedFaces=0",
                "GEOMETRIC_SET #27: builtMembers=1, unsupportedFaces=0",
                "GEOMETRIC_ITEM_SPECIFIC_USAGE #28: builtItems=",
                "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE #29: builtItems=",
                "GEOMETRIC_ITEM_SPECIFIC_USAGE #30: builtItems=",
                "GEOMETRIC_ITEM_SPECIFIC_USAGE #31: builtItems=");
    }

    @Test
    void shouldPreservePlacedDatumTargetFeatureAndProductDefinitionEffectivityNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-placed-datum-effectivity", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('COMP','Component','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('comp_pd','',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('comp_shape','',#6);
                #8=SHAPE_ASPECT('SA','base',#7,.T.);
                #9=PROPERTY_DEFINITION('PD','',#8);
                #10=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #11=REPRESENTATION('REP_A',(),#10);
                #12=PLACED_DATUM_TARGET_FEATURE(#9,#11);
                #13=PRODUCT_DEFINITION_EFFECTIVITY('PDE-1','serial usage',#6);
                ENDSEC;
                """);

        assertDumpContains(file,
                "PLACED_DATUM_TARGET_FEATURE #12: builtItems=0, unsupportedFaces=0",
                "PRODUCT_DEFINITION_EFFECTIVITY #13: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveAppliedAssignmentAndReferenceAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-applied-alias-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=PERSON('p-1','Doe','Jane',$,$,$);
                #6=ORGANIZATION('org-1','Acme','engineering');
                #7=PERSON_AND_ORGANIZATION(#5,#6);
                #8=PERSON_AND_ORGANIZATION_ROLE('creator');
                #9=CC_DESIGN_PERSON_AND_ORGANIZATION_ASSIGNMENT(#7,#8,(#4));
                #10=ORGANIZATION_ROLE('supplier');
                #11=CC_DESIGN_ORGANIZATION_ASSIGNMENT(#6,#10,(#4));
                #12=APPROVAL_STATUS('approved');
                #13=APPROVAL(#12,'design');
                #14=CC_DESIGN_APPROVAL(#13,(#4));
                #15=SECURITY_CLASSIFICATION_LEVEL('unclassified');
                #16=SECURITY_CLASSIFICATION('sec','export',#15);
                #17=CC_DESIGN_SECURITY_CLASSIFICATION(#16,(#4));
                #18=CONTRACT_TYPE('purchase');
                #19=CONTRACT('C-1','supply',#18);
                #20=CC_DESIGN_CONTRACT(#19,(#4));
                #21=CERTIFICATION_TYPE('material');
                #22=CERTIFICATION('CERT-1','compliance',#21);
                #23=CC_DESIGN_CERTIFICATION(#22,(#4));
                #24=DOCUMENT_TYPE('specification');
                #25=DOCUMENT('DOC-1','Spec','primary spec',#24);
                #26=CC_DESIGN_SPECIFICATION_REFERENCE(#25,'internal',(#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "CC_DESIGN_PERSON_AND_ORGANIZATION_ASSIGNMENT #9: builtItems=4, unsupportedFaces=0",
                "CC_DESIGN_ORGANIZATION_ASSIGNMENT #11: builtItems=3, unsupportedFaces=0",
                "CC_DESIGN_APPROVAL #14: builtItems=3, unsupportedFaces=0",
                "CC_DESIGN_SECURITY_CLASSIFICATION #17: builtItems=3, unsupportedFaces=0",
                "CC_DESIGN_CONTRACT #20: builtItems=3, unsupportedFaces=0",
                "CC_DESIGN_CERTIFICATION #23: builtItems=3, unsupportedFaces=0",
                "CC_DESIGN_SPECIFICATION_REFERENCE #26: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveCommonControlApprovalAndReferenceAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-cc-design-approval-reference-aliases", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=APPROVAL_STATUS('approved');
                #6=APPROVAL(#5,'design');
                #7=CC_DESIGN_APPROVAL(#6,(#4));
                #8=SECURITY_CLASSIFICATION_LEVEL('unclassified');
                #9=SECURITY_CLASSIFICATION('sec','export',#8);
                #10=CC_DESIGN_SECURITY_CLASSIFICATION(#9,(#4));
                #11=CONTRACT_TYPE('purchase');
                #12=CONTRACT('C-1','supply',#11);
                #13=CC_DESIGN_CONTRACT(#12,(#4));
                #14=CERTIFICATION_TYPE('material');
                #15=CERTIFICATION('CERT-1','compliance',#14);
                #16=CC_DESIGN_CERTIFICATION(#15,(#4));
                #17=DOCUMENT_TYPE('specification');
                #18=DOCUMENT('DOC-1','Spec','primary spec',#17);
                #19=CC_DESIGN_SPECIFICATION_REFERENCE(#18,'internal',(#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "CC_DESIGN_APPROVAL #7: builtItems=3, unsupportedFaces=0",
                "CC_DESIGN_SECURITY_CLASSIFICATION #10: builtItems=3, unsupportedFaces=0",
                "CC_DESIGN_CONTRACT #13: builtItems=3, unsupportedFaces=0",
                "CC_DESIGN_CERTIFICATION #16: builtItems=3, unsupportedFaces=0",
                "CC_DESIGN_SPECIFICATION_REFERENCE #19: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveCommonControlApprovalAndReferenceAliasEntityNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-cc-design-approval-reference-alias-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=APPROVAL_STATUS('approved');
                #6=APPROVAL(#5,'design');
                #7=CC_DESIGN_APPROVAL(#6,(#4));
                #8=SECURITY_CLASSIFICATION_LEVEL('unclassified');
                #9=SECURITY_CLASSIFICATION('sec','export',#8);
                #10=CC_DESIGN_SECURITY_CLASSIFICATION(#9,(#4));
                #11=CONTRACT_TYPE('purchase');
                #12=CONTRACT('C-1','supply',#11);
                #13=CC_DESIGN_CONTRACT(#12,(#4));
                #14=CERTIFICATION_TYPE('material');
                #15=CERTIFICATION('CERT-1','compliance',#14);
                #16=CC_DESIGN_CERTIFICATION(#15,(#4));
                #17=DOCUMENT_TYPE('specification');
                #18=DOCUMENT('DOC-1','Spec','primary spec',#17);
                #19=CC_DESIGN_SPECIFICATION_REFERENCE(#18,'internal',(#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "CC_DESIGN_APPROVAL #7:",
                "CC_DESIGN_SECURITY_CLASSIFICATION #10:",
                "CC_DESIGN_CONTRACT #13:",
                "CC_DESIGN_CERTIFICATION #16:",
                "CC_DESIGN_SPECIFICATION_REFERENCE #19:");
    }

    @Test
    void shouldPreserveCommonControlPersonAndOrganizationAliasNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-cc-design-person-org-aliases", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=PERSON('p-1','Doe','Jane',$,$,$);
                #6=ORGANIZATION('org-1','Acme','engineering');
                #7=PERSON_AND_ORGANIZATION(#5,#6);
                #8=PERSON_AND_ORGANIZATION_ROLE('creator');
                #9=CC_DESIGN_PERSON_AND_ORGANIZATION_ASSIGNMENT(#7,#8,(#4));
                #10=ORGANIZATION_ROLE('supplier');
                #11=CC_DESIGN_ORGANIZATION_ASSIGNMENT(#6,#10,(#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "CC_DESIGN_PERSON_AND_ORGANIZATION_ASSIGNMENT #9: builtItems=4, unsupportedFaces=0",
                "CC_DESIGN_ORGANIZATION_ASSIGNMENT #11: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveCommonControlPersonAndOrganizationAliasEntityNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-cc-design-person-org-alias-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=PERSON('p-1','Doe','Jane',$,$,$);
                #6=ORGANIZATION('org-1','Acme','engineering');
                #7=PERSON_AND_ORGANIZATION(#5,#6);
                #8=PERSON_AND_ORGANIZATION_ROLE('creator');
                #9=CC_DESIGN_PERSON_AND_ORGANIZATION_ASSIGNMENT(#7,#8,(#4));
                #10=ORGANIZATION_ROLE('supplier');
                #11=CC_DESIGN_ORGANIZATION_ASSIGNMENT(#6,#10,(#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "CC_DESIGN_PERSON_AND_ORGANIZATION_ASSIGNMENT #9:",
                "CC_DESIGN_ORGANIZATION_ASSIGNMENT #11:");
    }

    @Test
    void shouldPreserveCommonControlDateAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-cc-design-date-aliases", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=CALENDAR_DATE(2026,11,4);
                #6=DATE_ROLE('released');
                #7=CC_DESIGN_DATE_ASSIGNMENT(#5,#6,(#4));
                #8=COORDINATED_UNIVERSAL_TIME_OFFSET(8,$,.AHEAD.);
                #9=LOCAL_TIME(9,15,30.0,#8);
                #10=DATE_AND_TIME(#5,#9);
                #11=DATE_TIME_ROLE('created');
                #12=CC_DESIGN_DATE_AND_TIME_ASSIGNMENT(#10,#11,(#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "CC_DESIGN_DATE_ASSIGNMENT #7: builtItems=3, unsupportedFaces=0",
                "CC_DESIGN_DATE_AND_TIME_ASSIGNMENT #12: builtItems=5, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveCommonControlDateAliasEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-cc-design-date-alias-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=CALENDAR_DATE(2026,11,4);
                #6=DATE_ROLE('released');
                #7=CC_DESIGN_DATE_ASSIGNMENT(#5,#6,(#4));
                #8=COORDINATED_UNIVERSAL_TIME_OFFSET(8,$,.AHEAD.);
                #9=LOCAL_TIME(9,15,30.0,#8);
                #10=DATE_AND_TIME(#5,#9);
                #11=DATE_TIME_ROLE('created');
                #12=CC_DESIGN_DATE_AND_TIME_ASSIGNMENT(#10,#11,(#4));
                ENDSEC;
                """);

        assertDumpContains(file,
                "CC_DESIGN_DATE_ASSIGNMENT #7:",
                "CC_DESIGN_DATE_AND_TIME_ASSIGNMENT #12:");
    }

    @Test
    void shouldPreserveCommonControlDesignRelationshipAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-cc-design-relationship-aliases", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=MECHANICAL_CONTEXT('part',#1,'mechanical');
                #3=PRODUCT('PRT','part','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','first',#3);
                #5=DESIGN_CONTEXT('design',#1,'design');
                #6=PRODUCT_DEFINITION('def','part def',#4,#5);
                #7=CLASS_SYSTEM('classification system','class system description');
                #8=CLASS('fixture','classification');
                #9=ASSEMBLY_COMPONENT_USAGE('acu','assembly usage','',#6,#6);
                #10=PROMISSORY_USAGE_OCCURRENCE('puo','promised usage','',#6,#6);
                ENDSEC;
                """);

        assertDumpContains(file,
                "CLASS_SYSTEM #7: builtItems=1, unsupportedFaces=0",
                "CLASS #8: builtItems=1, unsupportedFaces=0",
                "ASSEMBLY_COMPONENT_USAGE #9: builtItems=4, unsupportedFaces=0",
                "PROMISSORY_USAGE_OCCURRENCE #10: builtItems=4, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveCommonControlDesignRelationshipAliasEntityNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-cc-design-relationship-alias-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=MECHANICAL_CONTEXT('part',#1,'mechanical');
                #3=PRODUCT('PRT','part','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','first',#3);
                #5=DESIGN_CONTEXT('design',#1,'design');
                #6=PRODUCT_DEFINITION('def','part def',#4,#5);
                #7=CLASS_SYSTEM('classification system','class system description');
                #8=CLASS('fixture','classification');
                #9=ASSEMBLY_COMPONENT_USAGE('acu','assembly usage','',#6,#6);
                #10=PROMISSORY_USAGE_OCCURRENCE('puo','promised usage','',#6,#6);
                ENDSEC;
                """);

        assertDumpContains(file,
                "MECHANICAL_CONTEXT #2:",
                "DESIGN_CONTEXT #5:",
                "CLASS_SYSTEM #7:",
                "CLASS #8:",
                "ASSEMBLY_COMPONENT_USAGE #9:",
                "PROMISSORY_USAGE_OCCURRENCE #10:");
    }

    @Test
    void shouldPreserveContextAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-context-alias-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=MECHANICAL_CONTEXT('part',#1,'mechanical');
                #3=PRODUCT('PRT','part','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','first',#3);
                #5=DESIGN_CONTEXT('design',#1,'design');
                #6=PRODUCT_DEFINITION('def','part def',#4,#5);
                ENDSEC;
                """);

        assertDumpContains(file,
                "MECHANICAL_CONTEXT #2: builtItems=1, unsupportedFaces=0",
                "DESIGN_CONTEXT #5: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveContextAliasEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-context-alias-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=MECHANICAL_CONTEXT('part',#1,'mechanical');
                #3=PRODUCT('PRT','part','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','first',#3);
                #5=DESIGN_CONTEXT('design',#1,'design');
                #6=PRODUCT_DEFINITION('def','part def',#4,#5);
                ENDSEC;
                """);

        assertDumpContains(file,
                "MECHANICAL_CONTEXT #2:",
                "DESIGN_CONTEXT #5:");
    }

    @Test
    void shouldPreserveRepresentationAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-representation-alias-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','GEN');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','GENERAL'));
                #3=ANALYSIS_MODEL('AM',(#1),#2);
                #4=LANGUAGE_ASSIGNMENT('LANG',(#1),#2);
                #5=MESSAGE_CONTENTS_ASSIGNMENT('MSG',(#1),#2);
                #6=MACHINING_TOOL_DIRECTION_REPRESENTATION('MTDR',(#1),#2);
                ENDSEC;
                """);

        assertDumpContains(file,
                "ANALYSIS_MODEL #3: builtItems=1, unsupportedFaces=0",
                "LANGUAGE_ASSIGNMENT #4: builtItems=1, unsupportedFaces=0",
                "MESSAGE_CONTENTS_ASSIGNMENT #5: builtItems=1, unsupportedFaces=0",
                "MACHINING_TOOL_DIRECTION_REPRESENTATION #6: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveFoundedHoleAndMachiningRepresentationAliasNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-founded-machining-alias-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','GEN');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','GENERAL'));
                #3=FOUNDED_KINEMATIC_PATH('FKP',(#1),#2);
                #4=SIMPLIFIED_COUNTERBORE_HOLE_DEFINITION('SCBH',(#1),#2);
                #5=SIMPLIFIED_COUNTERDRILL_HOLE_DEFINITION('SCDH',(#1),#2);
                #6=SIMPLIFIED_COUNTERSINK_HOLE_DEFINITION('SCSH',(#1),#2);
                #7=MACHINING_SPINDLE_SPEED_REPRESENTATION('MSSR',(#1),#2);
                #8=MACHINING_TOOL_BODY_REPRESENTATION('MTBR',(#1),#2);
                #9=MACHINING_TOOL_DIMENSION_REPRESENTATION('MTDR2',(#1),#2);
                #10=MACHINING_TOOLPATH_SPEED_PROFILE_REPRESENTATION('MTSPR',(#1),#2);
                ENDSEC;
                """);

        assertDumpContains(file,
                "FOUNDED_KINEMATIC_PATH #3: builtItems=1, unsupportedFaces=0",
                "SIMPLIFIED_COUNTERBORE_HOLE_DEFINITION #4: builtItems=1, unsupportedFaces=0",
                "SIMPLIFIED_COUNTERDRILL_HOLE_DEFINITION #5: builtItems=1, unsupportedFaces=0",
                "SIMPLIFIED_COUNTERSINK_HOLE_DEFINITION #6: builtItems=1, unsupportedFaces=0",
                "MACHINING_SPINDLE_SPEED_REPRESENTATION #7: builtItems=1, unsupportedFaces=0",
                "MACHINING_TOOL_BODY_REPRESENTATION #8: builtItems=1, unsupportedFaces=0",
                "MACHINING_TOOL_DIMENSION_REPRESENTATION #9: builtItems=1, unsupportedFaces=0",
                "MACHINING_TOOLPATH_SPEED_PROFILE_REPRESENTATION #10: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveToleranceAndTableRepresentationAliasNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-tolerance-table-alias-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','TABLE');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','TOL'));
                #3=FREEFORM_MILLING_TOLERANCE_REPRESENTATION('FMTR',(#1),#2);
                #4=HARDNESS_REPRESENTATION('HR',(#1),#2);
                #5=DEFAULT_TOLERANCE_TABLE('DTT',(#1),#2);
                #6=OTHER_LIST_TABLE_REPRESENTATION('OLTR',(#1),#2);
                ENDSEC;
                """);

        assertDumpContains(file,
                "FREEFORM_MILLING_TOLERANCE_REPRESENTATION #3: builtItems=1, unsupportedFaces=0",
                "HARDNESS_REPRESENTATION #4: builtItems=1, unsupportedFaces=0",
                "DEFAULT_TOLERANCE_TABLE #5: builtItems=1, unsupportedFaces=0",
                "OTHER_LIST_TABLE_REPRESENTATION #6: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveCharacterizedAndEvaluatedRepresentationAliasNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-characterized-evaluated-alias-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','CHAR');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CHARREP'));
                #3=CHARACTERIZED_REPRESENTATION('CR',(#1),#2);
                #4=CHARACTERIZED_ITEM_WITHIN_REPRESENTATION('CIWR',(#1),#2);
                #5=CHARACTERIZED_CHAIN_BASED_ITEM_WITHIN_REPRESENTATION('CCBIWR',(#1),#2);
                #6=EVALUATED_CHARACTERISTIC_OF_PRODUCT_AS_INDIVIDUAL_TEST_RESULT('ECPITR',(#1),#2);
                ENDSEC;
                """);

        assertDumpContains(file,
                "CHARACTERIZED_REPRESENTATION #3: builtItems=1, unsupportedFaces=0",
                "CHARACTERIZED_ITEM_WITHIN_REPRESENTATION #4: builtItems=1, unsupportedFaces=0",
                "CHARACTERIZED_CHAIN_BASED_ITEM_WITHIN_REPRESENTATION #5: builtItems=1, unsupportedFaces=0",
                "EVALUATED_CHARACTERISTIC_OF_PRODUCT_AS_INDIVIDUAL_TEST_RESULT #6: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveShapeRepresentationAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-shape-representation-alias-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','SHAPE');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','SHAPECTX'));
                #3=GEOMETRICALLY_BOUNDED_SURFACE_SHAPE_REPRESENTATION('GBSSR',(#1),#2);
                #4=GEOMETRICALLY_BOUNDED_WIREFRAME_SHAPE_REPRESENTATION('GBWSR',(#1),#2);
                #5=GEOMETRIC_SET_SHAPE_REPRESENTATION('GSSR',(#1),#2);
                #6=MANIFOLD_SURFACE_SHAPE_REPRESENTATION('MSSR',(#1),#2);
                #7=SHELL_BASED_SURFACE_MODEL_SHAPE_REPRESENTATION('SBSMSR',(#1),#2);
                #8=SURFACE_MODEL_SHAPE_REPRESENTATION('SMSR',(#1),#2);
                ENDSEC;
                """);

        assertDumpContains(file,
                "GEOMETRICALLY_BOUNDED_SURFACE_SHAPE_REPRESENTATION #3: builtItems=1, unsupportedFaces=0",
                "GEOMETRICALLY_BOUNDED_WIREFRAME_SHAPE_REPRESENTATION #4: builtItems=1, unsupportedFaces=0",
                "GEOMETRIC_SET_SHAPE_REPRESENTATION #5: builtItems=1, unsupportedFaces=0",
                "MANIFOLD_SURFACE_SHAPE_REPRESENTATION #6: builtItems=1, unsupportedFaces=0",
                "SHELL_BASED_SURFACE_MODEL_SHAPE_REPRESENTATION #7: builtItems=1, unsupportedFaces=0",
                "SURFACE_MODEL_SHAPE_REPRESENTATION #8: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveRepresentationRelationshipAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-representation-relationship-alias-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','R');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=REPRESENTATION('REP_A',(#1),#2);
                #4=REPRESENTATION('REP_B',(#1),#2);
                #5=DEFINITIONAL_REPRESENTATION_RELATIONSHIP('DRR','def link',#3,#4);
                #6=TOPOLOGY_TO_GEOMETRY_MODEL_ASSOCIATION('TGMA','topo geom',#3,#4);
                #7=KINEMATIC_FRAME_REPRESENTATION_RELATIONSHIP('KFRR','frame link',#3,#4);
                #8=COAXIAL_ASSEMBLY_CONSTRAINT('CAC','coaxial',#3,#4);
                ENDSEC;
                """);

        assertDumpContains(file,
                "DEFINITIONAL_REPRESENTATION_RELATIONSHIP #5: builtItems=2, unsupportedFaces=0",
                "TOPOLOGY_TO_GEOMETRY_MODEL_ASSOCIATION #6: builtItems=2, unsupportedFaces=0",
                "KINEMATIC_FRAME_REPRESENTATION_RELATIONSHIP #7: builtItems=2, unsupportedFaces=0",
                "COAXIAL_ASSEMBLY_CONSTRAINT #8: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveAdditionalRepresentationRelationshipAliasNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-representation-relationship-more-alias-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','REL');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','RELCTX'));
                #3=REPRESENTATION('REP_A',(#1),#2);
                #4=REPRESENTATION('REP_B',(#1),#2);
                #5=SHAPE_REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION('SRRWT','shapex',#3,#4);
                #6=REPRESENTATION_RELATIONSHIP_WITH_SAME_CONTEXT('RRSC','samectx',#3,#4);
                #7=KINEMATIC_FRAME_BACKGROUND_REPRESENTATION_RELATIONSHIP('KFBRR','framebg',#3,#4);
                #8=MECHANISM_STATE_REPRESENTATION_RELATIONSHIP('MSRR','state',#3,#4);
                ENDSEC;
                """);

        assertDumpContains(file,
                "SHAPE_REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION #5: builtItems=2, unsupportedFaces=0",
                "REPRESENTATION_RELATIONSHIP_WITH_SAME_CONTEXT #6: builtItems=2, unsupportedFaces=0",
                "KINEMATIC_FRAME_BACKGROUND_REPRESENTATION_RELATIONSHIP #7: builtItems=2, unsupportedFaces=0",
                "MECHANISM_STATE_REPRESENTATION_RELATIONSHIP #8: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveProductDefinitionRelationshipAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-product-definition-relationship-aliases", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('ASM','assembly','',(#2));
                #4=PRODUCT('PRT','part','',(#2));
                #5=PRODUCT_DEFINITION_FORMATION('a1','assembly version',#3);
                #6=PRODUCT_DEFINITION_FORMATION('p1','part version',#4);
                #7=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #8=PRODUCT_DEFINITION('asm def','assembly definition',#5,#7);
                #9=PRODUCT_DEFINITION('part def','part definition',#6,#7);
                #10=PRODUCT_DEFINITION_RELATIONSHIP('PDR','base','base relationship',#8,#9);
                #11=PRODUCT_DEFINITION_USAGE('PDU','usage','usage relationship',#8,#9);
                #12=BREAKDOWN_CONTEXT('BC','breakdown context','context',#8,#9);
                #13=BREAKDOWN_ELEMENT_USAGE('BEU','breakdown usage','usage',#8,#9);
                #14=BREAKDOWN_OF('BO','breakdown of','breakdown',#8,#9);
                #15=SUPPLIED_PART_RELATIONSHIP('SPR','supplied','supplied relation',#8,#9);
                #16=PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP('PDRR','relationship relation','links pdrs',#10,#11);
                #17=PRODUCT_DEFINITION_USAGE_RELATIONSHIP('PDUR','usage relation','links usages',#11,#13);
                #18=PRODUCT_DEFINITION_FORMATION_RELATIONSHIP('PDFR','formation relation','links formations',#5,#6);
                #19=PROPERTY_DEFINITION('prop-a','property a',#8);
                #20=PROPERTY_DEFINITION('prop-b','property b',#9);
                #21=PROPERTY_DEFINITION_RELATIONSHIP('PDRP','property relation',#19,#20);
                #22=ASSEMBLY_COMPONENT_USAGE_SUBSTITUTE('ACUS','usage substitute','alternate usage',#10,#11);
                #23=PRODUCT_DEFINITION_SUBSTITUTE('PDS','definition substitute','alternate definition',#10,#11);
                ENDSEC;
                """);

        assertDumpContains(file,
                "PRODUCT_DEFINITION_RELATIONSHIP #10: builtItems=4, unsupportedFaces=0",
                "PRODUCT_DEFINITION_USAGE #11: builtItems=4, unsupportedFaces=0",
                "BREAKDOWN_CONTEXT #12: builtItems=4, unsupportedFaces=0",
                "BREAKDOWN_ELEMENT_USAGE #13: builtItems=4, unsupportedFaces=0",
                "BREAKDOWN_OF #14: builtItems=4, unsupportedFaces=0",
                "SUPPLIED_PART_RELATIONSHIP #15: builtItems=4, unsupportedFaces=0",
                "PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP #16: builtItems=8, unsupportedFaces=0",
                "PRODUCT_DEFINITION_USAGE_RELATIONSHIP #17: builtItems=8, unsupportedFaces=0",
                "PRODUCT_DEFINITION_FORMATION_RELATIONSHIP #18: builtItems=2, unsupportedFaces=0",
                "PROPERTY_DEFINITION_RELATIONSHIP #21: builtItems=2, unsupportedFaces=0",
                "ASSEMBLY_COMPONENT_USAGE_SUBSTITUTE #22: builtItems=8, unsupportedFaces=0",
                "PRODUCT_DEFINITION_SUBSTITUTE #23: builtItems=8, unsupportedFaces=0");
    }

    @Test
    void shouldReportDocumentReferenceAndApprovalMetadataInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-document-approval-metadata", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=REPRESENTATION('R0',(#4),#6);
                #6=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #7=PROPERTY_DEFINITION('PD-A','',#4);
                #8=PROPERTY_DEFINITION('PD-B','',#5);
                #9=PROPERTY_DEFINITION_RELATIONSHIP('PDR','links properties',#7,#8);
                #10=ATTRIBUTE_ASSERTION(#7,#5);
                #11=IDENTIFICATION_ROLE('erp');
                #12=IDENTIFICATION_ASSIGNMENT('ERP-42',#11);
                #13=APPLIED_IDENTIFICATION_ASSIGNMENT('ERP-42',#11,(#4));
                #14=DOCUMENT_TYPE('spec');
                #15=DOCUMENT('DOC-A','spec a','',#14);
                #16=DOCUMENT('DOC-B','spec b','',#14);
                #17=DOCUMENT_REFERENCE(#15,'internal');
                #18=APPLIED_DOCUMENT_REFERENCE(#15,'internal',(#4));
                #19=DOCUMENT_RELATIONSHIP('DOCREL','pairs docs',#15,#16);
                #20=APPROVAL_STATUS('approved');
                #21=APPROVAL(#20,'design');
                #22=APPROVAL_ROLE('signoff');
                #23=PERSON('p-1','Doe','Jane',$,$,$);
                #24=ORGANIZATION('org-1','Acme','engineering');
                #25=PERSON_AND_ORGANIZATION(#23,#24);
                #26=APPROVAL_PERSON_ORGANIZATION(#25,#21,#22);
                #27=CALENDAR_DATE(2026,11,4);
                #28=COORDINATED_UNIVERSAL_TIME_OFFSET(8,0,.AHEAD.);
                #29=LOCAL_TIME(9,15,$,#28);
                #30=DATE_AND_TIME(#27,#29);
                #31=APPROVAL_DATE_TIME(#30,#21);
                ENDSEC;
                """);

        assertDumpContains(file,
                "PROPERTY_DEFINITION_RELATIONSHIP #9: builtItems=2, unsupportedFaces=0",
                "ATTRIBUTE_ASSERTION #10: builtItems=1, unsupportedFaces=0",
                "IDENTIFICATION_ASSIGNMENT #12: builtItems=1, unsupportedFaces=0",
                "APPLIED_IDENTIFICATION_ASSIGNMENT #13: builtItems=2, unsupportedFaces=0",
                "DOCUMENT_REFERENCE #17: builtItems=2, unsupportedFaces=0",
                "APPLIED_DOCUMENT_REFERENCE #18: builtItems=3, unsupportedFaces=0",
                "DOCUMENT_RELATIONSHIP #19: builtItems=4, unsupportedFaces=0",
                "APPROVAL_PERSON_ORGANIZATION #26: builtItems=5, unsupportedFaces=0",
                "APPROVAL_DATE_TIME #31: builtItems=5, unsupportedFaces=0");
    }

    @Test
    void shouldReportProductShapeAndUsageMetadataInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-product-shape-usage-metadata", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('ASM','Assembly','',(#2));
                #4=PRODUCT('COMP','Component','',(#2));
                #5=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #6=PRODUCT_DEFINITION_FORMATION('v1','',#4);
                #7=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #8=PRODUCT_DEFINITION('asm_pd','',#5,#7);
                #9=PRODUCT_DEFINITION('comp_pd','',#6,#7);
                #10=PRODUCT_DEFINITION_SHAPE('asm_shape','',#8);
                #11=PRODUCT_DEFINITION_SHAPE('comp_shape','',#9);
                #12=SHAPE_ASPECT('SA','base',#11,.T.);
                #13=PROPERTY_DEFINITION('PD','',#12);
                #14=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #15=REPRESENTATION('REP_A',(),#14);
                #16=REPRESENTATION('REP_B',(),#14);
                #17=REPRESENTATION('REP_C',(),#14);
                #18=SHAPE_DEFINITION_REPRESENTATION(#11,#15);
                #19=REPRESENTATION_RELATIONSHIP('RR','chain',#16,#17);
                #20=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ-1','OCC','component usage',#8,#9,'R1');
                #21=PRODUCT_DEFINITION_SHAPE('occ_shape','',#20);
                #22=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#19,#21);
                #23=PLACED_DATUM_TARGET_FEATURE(#13,#16);
                #24=ITEM_IDENTIFIED_REPRESENTATION_USAGE('IU','',#13,#15,#12);
                #25=CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE('CIU','',#13,(#16,#17),(#19),#12);
                #26=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU','',#34,#15);
                #27=CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE('CGU','',#34,(#16,#17),(#19),#15);
                #28=DRAUGHTING_MODEL_ITEM_ASSOCIATION('DMA','',#24,#15,#12);
                #29=PMI_REQUIREMENT_ITEM_ASSOCIATION('PMI','',#25,#15,#12,#12);
                #30=MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION('MDR','',#28,#15,#12,#12);
                #31=PLACED_TARGET('PT','',#29,#16,#12);
                #32=PRODUCT_DEFINITION_EFFECTIVITY('PDE-1','serial usage',#9);
                #33=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #34=ANNOTATION_TEXT_OCCURRENCE('NOTE','',#33);
                ENDSEC;
                """);

        assertDumpContains(file,
                "PRODUCT_DEFINITION_SHAPE #11:",
                "SHAPE_DEFINITION_REPRESENTATION #18:",
                "CONTEXT_DEPENDENT_SHAPE_REPRESENTATION #22:",
                "PLACED_DATUM_TARGET_FEATURE #23:",
                "ITEM_IDENTIFIED_REPRESENTATION_USAGE #24:",
                "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE #25:",
                "GEOMETRIC_ITEM_SPECIFIC_USAGE #26:",
                "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE #27:",
                "DRAUGHTING_MODEL_ITEM_ASSOCIATION #28:",
                "PMI_REQUIREMENT_ITEM_ASSOCIATION #29:",
                "MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION #30:",
                "PLACED_TARGET #31:",
                "PRODUCT_DEFINITION_EFFECTIVITY #32:");
    }

    @Test
    void shouldPreserveShapeAspectRelationshipAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-shape-aspect-relationship-alias-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','first',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('def','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('shape','primary shape',#6);
                #8=SHAPE_ASPECT('SA','base',#7,.T.);
                #9=APEX('AP','apex',#7,.F.);
                #10=CENTRE_OF_SYMMETRY('COS','centre',#7,.U.);
                #11=COMPONENT_FEATURE('CF','component',#7,.T.);
                #12=COMPOSITE_SHAPE_ASPECT('CSA','composite',#7,.T.);
                #13=DATUM('D','datum',#7,.T.);
                #14=DATUM_FEATURE('DF','datum feature',#7,.T.);
                #15=DATUM_TARGET('DT','datum target',#7,.T.);
                #16=GEOMETRIC_ALIGNMENT('GA','align',#7,.T.);
                #17=GEOMETRIC_CONTACT('GC','contact',#7,.T.);
                #18=GROUP_SHAPE_ASPECT('GSA','group',#7,.T.);
                #22=SYMMETRIC_SHAPE_ASPECT('SYSA','symmetric',#7,.T.);
                #37=SHAPE_ASPECT_RELATIONSHIP('SAR','base rel',#8,#9);
                #39=DIMENSIONAL_LOCATION('DL','location',#8,#10);
                #45=SHAPE_ASPECT_DERIVING_RELATIONSHIP('SADR','derive',#22,#8);
                #54=SHAPE_FEATURE_FIT_RELATIONSHIP('SFFR','fit',#11,#12);
                ENDSEC;
                """);

        assertDumpContains(file,
                "SHAPE_ASPECT_RELATIONSHIP #37: builtItems=4, unsupportedFaces=0",
                "DIMENSIONAL_LOCATION #39: builtItems=4, unsupportedFaces=0",
                "SHAPE_ASPECT_DERIVING_RELATIONSHIP #45: builtItems=4, unsupportedFaces=0",
                "SHAPE_FEATURE_FIT_RELATIONSHIP #54: builtItems=4, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveAdditionalShapeAspectRelationshipAliasNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-shape-aspect-relationship-more-alias-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','first',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('def','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('shape','primary shape',#6);
                #8=APPLIED_AREA('AA','applied area',#7,.T.);
                #10=BEAD_END('BE','bead end',#7,.T.);
                #13=COMPONENT_TERMINAL('CT','component terminal',#7,.T.);
                #15=CONTACTING_FEATURE('CF2','contacting',#7,.T.);
                #19=DATUM_REFERENCE_ELEMENT('DRE','datum ref',#7,.T.);
                #28=PATH_FEATURE_COMPONENT('PFC','path feature',#7,.T.);
                #29=PHYSICAL_COMPONENT_FEATURE('PCF','physical feature',#7,.T.);
                #37=ASSEMBLY_SHAPE_CONSTRAINT_ITEM_RELATIONSHIP('ASCIR','constraint item',#8,#10);
                #42=COMPONENT_PATH_SHAPE_ASPECT_RELATIONSHIP('CPSAR','component path',#28,#29);
                #45=CONTACT_FEATURE_FIT_RELATIONSHIP('CFFR','contact fit',#15,#29);
                #46=DIMENSIONAL_LOCATION_WITH_DATUM_FEATURE('DLWDF','datum location',#19,#8);
                #47=DIMENSIONAL_LOCATION_WITH_PATH('DLWP','path location',#8,#28);
                #49=SHAPE_FEATURE_DEFINITION_ELEMENT_RELATIONSHIP('SFDERR','definition element',#8,#10);
                ENDSEC;
                """);

        assertDumpContains(file,
                "ASSEMBLY_SHAPE_CONSTRAINT_ITEM_RELATIONSHIP #37: builtItems=4, unsupportedFaces=0",
                "COMPONENT_PATH_SHAPE_ASPECT_RELATIONSHIP #42: builtItems=4, unsupportedFaces=0",
                "CONTACT_FEATURE_FIT_RELATIONSHIP #45: builtItems=4, unsupportedFaces=0",
                "DIMENSIONAL_LOCATION_WITH_DATUM_FEATURE #46: builtItems=4, unsupportedFaces=0",
                "DIMENSIONAL_LOCATION_WITH_PATH #47: builtItems=4, unsupportedFaces=0",
                "SHAPE_FEATURE_DEFINITION_ELEMENT_RELATIONSHIP #49: builtItems=4, unsupportedFaces=0");
    }

    @Test
    void shouldReportPropertyRepresentationFamiliesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-property-representation-families", ".step");
        Files.writeString(file, """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PDR');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PDRCTX'));
                #3=REPRESENTATION('R',(#1),#2);
                #4=PROPERTY_DEFINITION('PD','',#1);
                #5=PROPERTY_DEFINITION_REPRESENTATION(#4,#3);
                #6=ACTION_PROPERTY_REPRESENTATION(#4,#3);
                #7=CONTACT_RATIO_REPRESENTATION(#4,#3);
                #8=KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION(#4,#3);
                #9=KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION(#4,#3);
                #10=KINEMATIC_PROPERTY_REPRESENTATION_RELATION(#4,#3);
                #11=KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION(#4,#3);
                #12=RESOURCE_PROPERTY_REPRESENTATION(#4,#3);
                ENDSEC;
                """);

        assertDumpContains(file,
                "PROPERTY_DEFINITION_REPRESENTATION #5: builtItems=2, unsupportedFaces=0",
                "ACTION_PROPERTY_REPRESENTATION #6: builtItems=2, unsupportedFaces=0",
                "CONTACT_RATIO_REPRESENTATION #7: builtItems=2, unsupportedFaces=0",
                "KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION #8: builtItems=2, unsupportedFaces=0",
                "KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION #9: builtItems=2, unsupportedFaces=0",
                "KINEMATIC_PROPERTY_REPRESENTATION_RELATION #10: builtItems=2, unsupportedFaces=0",
                "KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION #11: builtItems=2, unsupportedFaces=0",
                "RESOURCE_PROPERTY_REPRESENTATION #12: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveBasicPropertyRepresentationNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-basic-property-representation-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PDR');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PDRCTX'));
                #3=REPRESENTATION('R',(#1),#2);
                #4=PROPERTY_DEFINITION('PD','',#1);
                #5=PROPERTY_DEFINITION_REPRESENTATION(#4,#3);
                #6=ACTION_PROPERTY_REPRESENTATION(#4,#3);
                #7=CONTACT_RATIO_REPRESENTATION(#4,#3);
                ENDSEC;
                """);

        assertDumpContains(file,
                "PROPERTY_DEFINITION_REPRESENTATION #5: builtItems=2, unsupportedFaces=0",
                "ACTION_PROPERTY_REPRESENTATION #6: builtItems=2, unsupportedFaces=0",
                "CONTACT_RATIO_REPRESENTATION #7: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveBasicPropertyRepresentationEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-basic-property-representation-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PDR');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PDRCTX'));
                #3=REPRESENTATION('R',(#1),#2);
                #4=PROPERTY_DEFINITION('PD','',#1);
                #5=PROPERTY_DEFINITION_REPRESENTATION(#4,#3);
                #6=ACTION_PROPERTY_REPRESENTATION(#4,#3);
                #7=CONTACT_RATIO_REPRESENTATION(#4,#3);
                ENDSEC;
                """);

        assertDumpContains(file,
                "PROPERTY_DEFINITION_REPRESENTATION #5:",
                "ACTION_PROPERTY_REPRESENTATION #6:",
                "CONTACT_RATIO_REPRESENTATION #7:");
    }

    @Test
    void shouldPreserveKinematicAndResourcePropertyRepresentationNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-kinematic-property-representation-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PDR');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PDRCTX'));
                #3=REPRESENTATION('R',(#1),#2);
                #4=PROPERTY_DEFINITION('PD','',#1);
                #8=KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION(#4,#3);
                #9=KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION(#4,#3);
                #10=KINEMATIC_PROPERTY_REPRESENTATION_RELATION(#4,#3);
                #11=KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION(#4,#3);
                #12=RESOURCE_PROPERTY_REPRESENTATION(#4,#3);
                ENDSEC;
                """);

        assertDumpContains(file,
                "KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION #8: builtItems=2, unsupportedFaces=0",
                "KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION #9: builtItems=2, unsupportedFaces=0",
                "KINEMATIC_PROPERTY_REPRESENTATION_RELATION #10: builtItems=2, unsupportedFaces=0",
                "KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION #11: builtItems=2, unsupportedFaces=0",
                "RESOURCE_PROPERTY_REPRESENTATION #12: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveKinematicAndResourcePropertyRepresentationEntityNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-kinematic-property-representation-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PDR');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PDRCTX'));
                #3=REPRESENTATION('R',(#1),#2);
                #4=PROPERTY_DEFINITION('PD','',#1);
                #8=KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION(#4,#3);
                #9=KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION(#4,#3);
                #10=KINEMATIC_PROPERTY_REPRESENTATION_RELATION(#4,#3);
                #11=KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION(#4,#3);
                #12=RESOURCE_PROPERTY_REPRESENTATION(#4,#3);
                ENDSEC;
                """);

        assertDumpContains(file,
                "KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION #8:",
                "KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION #9:",
                "KINEMATIC_PROPERTY_REPRESENTATION_RELATION #10:",
                "KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION #11:",
                "RESOURCE_PROPERTY_REPRESENTATION #12:");
    }

    @Test
    void shouldReportMeasureUnitAndContextFamiliesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-measure-unit-context", ".step");
        Files.writeString(file, """
                DATA;
                #1=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));
                #2=MEASURE_WITH_UNIT(LENGTH_MEASURE(12.5),#1);
                #3=(PLANE_ANGLE_UNIT() NAMED_UNIT(*) SI_UNIT($,.RADIAN.));
                #4=(SOLID_ANGLE_UNIT() NAMED_UNIT(*) SI_UNIT($,.STERADIAN.));
                #5=(CONVERSION_BASED_UNIT('DEGREE',#2) NAMED_UNIT(*) PLANE_ANGLE_UNIT());
                #6=(CONTEXT_DEPENDENT_UNIT('BOX') NAMED_UNIT(*));
                #7=LENGTH_MEASURE_WITH_UNIT(LENGTH_MEASURE(2.5),#1);
                #8=UNCERTAINTY_MEASURE_WITH_UNIT(LENGTH_MEASURE(0.01),#1,'distance_accuracy_value','confusion');
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3)
                    GLOBAL_UNIT_ASSIGNED_CONTEXT((#1,#3,#4))
                    GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT((#8))
                    REPRESENTATION_CONTEXT('ID','MODEL'));
                #10=REPRESENTATION_ITEM('REP_ITEM_ONLY');
                #11=(GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('GEOM_ITEM_ONLY'));
                #12=(TOPOLOGICAL_REPRESENTATION_ITEM('TOPO_ITEM_ONLY'));
                #13=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #14=VALUE_REPRESENTATION_ITEM('roughness',DESCRIPTIVE_MEASURE('Ra 3.2'));
                #15=MEASURE_REPRESENTATION_ITEM('thickness',LENGTH_MEASURE(2.5),#1);
                #16=REPRESENTATION('R',(#10,#11,#12,#13,#14,#15),#9);
                #17=MEASURE_WITH_UNIT(LENGTH_MEASURE(6.0),#1);
                #18=LENGTH_MEASURE_WITH_UNIT(LENGTH_MEASURE(3.5),#1);
                #19=UNCERTAINTY_MEASURE_WITH_UNIT(LENGTH_MEASURE(0.02),#1,'local_accuracy','standalone');
                #20=(CONTEXT_DEPENDENT_UNIT('CRATE') NAMED_UNIT(*));
                #21=(GEOMETRIC_REPRESENTATION_CONTEXT(3)
                    GLOBAL_UNIT_ASSIGNED_CONTEXT((#1,#3,#4))
                    GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT((#19))
                    REPRESENTATION_CONTEXT('ID2','MODEL2'));
                #22=PROPERTY_DEFINITION('PD_MWU','',#17);
                #23=PROPERTY_DEFINITION('PD_TYPED','',#18);
                #24=PROPERTY_DEFINITION('PD_UNC','',#19);
                #25=PROPERTY_DEFINITION('PD_UNIT','',#20);
                #26=PROPERTY_DEFINITION('PD_CTX','',#21);
                ENDSEC;
                """);

        assertDumpContains(file,
                "PROPERTY_DEFINITION #22:",
                "PROPERTY_DEFINITION #23:",
                "PROPERTY_DEFINITION #24:",
                "PROPERTY_DEFINITION #25:",
                "PROPERTY_DEFINITION #26:",
                "REPRESENTATION #16:");
    }

    @Test
    void shouldPreserveUnitAndContextNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-unit-context-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));
                #2=MEASURE_WITH_UNIT(LENGTH_MEASURE(12.5),#1);
                #3=(PLANE_ANGLE_UNIT() NAMED_UNIT(*) SI_UNIT($,.RADIAN.));
                #4=(SOLID_ANGLE_UNIT() NAMED_UNIT(*) SI_UNIT($,.STERADIAN.));
                #5=(CONVERSION_BASED_UNIT('DEGREE',#2) NAMED_UNIT(*) PLANE_ANGLE_UNIT());
                #6=(CONTEXT_DEPENDENT_UNIT('BOX') NAMED_UNIT(*));
                #7=UNCERTAINTY_MEASURE_WITH_UNIT(LENGTH_MEASURE(0.01),#1,'distance_accuracy_value','confusion');
                #8=(GEOMETRIC_REPRESENTATION_CONTEXT(3)
                    GLOBAL_UNIT_ASSIGNED_CONTEXT((#1,#3,#4))
                    GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT((#7))
                    REPRESENTATION_CONTEXT('ID','MODEL'));
                ENDSEC;
                """);

        assertDumpContains(file,
                "SI_UNIT #1: builtItems=1, unsupportedFaces=0",
                "SI_UNIT #3: builtItems=1, unsupportedFaces=0",
                "SI_UNIT #4: builtItems=1, unsupportedFaces=0",
                "MEASURE_WITH_UNIT #2: builtItems=1, unsupportedFaces=0",
                "CONVERSION_BASED_UNIT #5: builtItems=1, unsupportedFaces=0",
                "CONTEXT_DEPENDENT_UNIT #6: builtItems=1, unsupportedFaces=0",
                "UNCERTAINTY_MEASURE_WITH_UNIT #7: builtItems=1, unsupportedFaces=0",
                "GEOMETRIC_REPRESENTATION_CONTEXT #8: builtItems=5, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveUnitAndContextEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-unit-context-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));
                #2=MEASURE_WITH_UNIT(LENGTH_MEASURE(12.5),#1);
                #3=(PLANE_ANGLE_UNIT() NAMED_UNIT(*) SI_UNIT($,.RADIAN.));
                #4=(SOLID_ANGLE_UNIT() NAMED_UNIT(*) SI_UNIT($,.STERADIAN.));
                #5=(CONVERSION_BASED_UNIT('DEGREE',#2) NAMED_UNIT(*) PLANE_ANGLE_UNIT());
                #6=(CONTEXT_DEPENDENT_UNIT('BOX') NAMED_UNIT(*));
                #7=UNCERTAINTY_MEASURE_WITH_UNIT(LENGTH_MEASURE(0.01),#1,'distance_accuracy_value','confusion');
                #8=(GEOMETRIC_REPRESENTATION_CONTEXT(3)
                    GLOBAL_UNIT_ASSIGNED_CONTEXT((#1,#3,#4))
                    GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT((#7))
                    REPRESENTATION_CONTEXT('ID','MODEL'));
                ENDSEC;
                """);

        assertDumpContains(file,
                "SI_UNIT #1:",
                "SI_UNIT #3:",
                "SI_UNIT #4:",
                "MEASURE_WITH_UNIT #2:",
                "CONVERSION_BASED_UNIT #5:",
                "CONTEXT_DEPENDENT_UNIT #6:",
                "UNCERTAINTY_MEASURE_WITH_UNIT #7:",
                "GEOMETRIC_REPRESENTATION_CONTEXT #8:");
    }

    @Test
    void shouldPreserveTypedMeasureWithUnitAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-typed-measure-alias-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=MASS_DENSITY_UNIT();
                #2=DYNAMIC_VISCOSITY_UNIT();
                #3=KINEMATIC_VISCOSITY_UNIT();
                #4=MOMENT_OF_INERTIA_UNIT();
                #5=THERMAL_CONDUCTIVITY_UNIT();
                #6=HEAT_FLUX_DENSITY_UNIT();
                #7=SPECIFIC_HEAT_CAPACITY_UNIT();
                #8=AREA_DENSITY_UNIT();
                #9=VOLUMETRIC_FLOW_RATE_UNIT();
                #10=MASS_FLOW_RATE_UNIT();
                #11=ROTATIONAL_FREQUENCY_UNIT();
                #12=ANGULAR_VELOCITY_UNIT();
                #13=ANGULAR_ACCELERATION_UNIT();
                #14=TORQUE_UNIT();
                #15=LINEAR_FORCE_UNIT();
                #16=LINEAR_STIFFNESS_UNIT();
                #17=ROTATIONAL_STIFFNESS_UNIT();
                #18=LINEAR_MOMENT_UNIT();
                #21=MASS_DENSITY_MEASURE_WITH_UNIT(MASS_DENSITY_MEASURE(7.85),#1);
                #22=DYNAMIC_VISCOSITY_MEASURE_WITH_UNIT(DYNAMIC_VISCOSITY_MEASURE(0.01),#2);
                #23=KINEMATIC_VISCOSITY_MEASURE_WITH_UNIT(KINEMATIC_VISCOSITY_MEASURE(0.02),#3);
                #24=MOMENT_OF_INERTIA_MEASURE_WITH_UNIT(MOMENT_OF_INERTIA_MEASURE(3.0),#4);
                #25=THERMAL_CONDUCTIVITY_MEASURE_WITH_UNIT(THERMAL_CONDUCTIVITY_MEASURE(205.0),#5);
                #26=HEAT_FLUX_DENSITY_MEASURE_WITH_UNIT(HEAT_FLUX_DENSITY_MEASURE(12.0),#6);
                #27=SPECIFIC_HEAT_CAPACITY_MEASURE_WITH_UNIT(SPECIFIC_HEAT_CAPACITY_MEASURE(900.0),#7);
                #28=AREA_DENSITY_MEASURE_WITH_UNIT(AREA_DENSITY_MEASURE(2.7),#8);
                #29=VOLUMETRIC_FLOW_RATE_MEASURE_WITH_UNIT(VOLUMETRIC_FLOW_RATE_MEASURE(1.5),#9);
                #30=MASS_FLOW_RATE_MEASURE_WITH_UNIT(MASS_FLOW_RATE_MEASURE(0.8),#10);
                #31=ROTATIONAL_FREQUENCY_MEASURE_WITH_UNIT(ROTATIONAL_FREQUENCY_MEASURE(60.0),#11);
                #32=ANGULAR_VELOCITY_MEASURE_WITH_UNIT(ANGULAR_VELOCITY_MEASURE(3.14),#12);
                #33=ANGULAR_ACCELERATION_MEASURE_WITH_UNIT(ANGULAR_ACCELERATION_MEASURE(1.5),#13);
                #34=TORQUE_MEASURE_WITH_UNIT(TORQUE_MEASURE(20.0),#14);
                #35=LINEAR_FORCE_MEASURE_WITH_UNIT(LINEAR_FORCE_MEASURE(4.0),#15);
                #36=LINEAR_STIFFNESS_MEASURE_WITH_UNIT(LINEAR_STIFFNESS_MEASURE(1000.0),#16);
                #37=ROTATIONAL_STIFFNESS_MEASURE_WITH_UNIT(ROTATIONAL_STIFFNESS_MEASURE(250.0),#17);
                #38=LINEAR_MOMENT_MEASURE_WITH_UNIT(LINEAR_MOMENT_MEASURE(6.0),#18);
                ENDSEC;
                """);

        assertDumpContains(file,
                "MASS_DENSITY_MEASURE_WITH_UNIT #21: builtItems=0, unsupportedFaces=0",
                "DYNAMIC_VISCOSITY_MEASURE_WITH_UNIT #22: builtItems=0, unsupportedFaces=0",
                "KINEMATIC_VISCOSITY_MEASURE_WITH_UNIT #23: builtItems=0, unsupportedFaces=0",
                "MOMENT_OF_INERTIA_MEASURE_WITH_UNIT #24: builtItems=0, unsupportedFaces=0",
                "THERMAL_CONDUCTIVITY_MEASURE_WITH_UNIT #25: builtItems=0, unsupportedFaces=0",
                "HEAT_FLUX_DENSITY_MEASURE_WITH_UNIT #26: builtItems=0, unsupportedFaces=0",
                "SPECIFIC_HEAT_CAPACITY_MEASURE_WITH_UNIT #27: builtItems=0, unsupportedFaces=0",
                "AREA_DENSITY_MEASURE_WITH_UNIT #28: builtItems=0, unsupportedFaces=0",
                "VOLUMETRIC_FLOW_RATE_MEASURE_WITH_UNIT #29: builtItems=0, unsupportedFaces=0",
                "MASS_FLOW_RATE_MEASURE_WITH_UNIT #30: builtItems=0, unsupportedFaces=0",
                "ROTATIONAL_FREQUENCY_MEASURE_WITH_UNIT #31: builtItems=0, unsupportedFaces=0",
                "ANGULAR_VELOCITY_MEASURE_WITH_UNIT #32: builtItems=0, unsupportedFaces=0",
                "ANGULAR_ACCELERATION_MEASURE_WITH_UNIT #33: builtItems=0, unsupportedFaces=0",
                "TORQUE_MEASURE_WITH_UNIT #34: builtItems=0, unsupportedFaces=0",
                "LINEAR_FORCE_MEASURE_WITH_UNIT #35: builtItems=0, unsupportedFaces=0",
                "LINEAR_STIFFNESS_MEASURE_WITH_UNIT #36: builtItems=0, unsupportedFaces=0",
                "ROTATIONAL_STIFFNESS_MEASURE_WITH_UNIT #37: builtItems=0, unsupportedFaces=0",
                "LINEAR_MOMENT_MEASURE_WITH_UNIT #38: builtItems=0, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveTypedMeasureWithUnitEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-typed-measure-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=MASS_DENSITY_UNIT();
                #2=DYNAMIC_VISCOSITY_UNIT();
                #3=KINEMATIC_VISCOSITY_UNIT();
                #4=MOMENT_OF_INERTIA_UNIT();
                #5=THERMAL_CONDUCTIVITY_UNIT();
                #6=HEAT_FLUX_DENSITY_UNIT();
                #7=SPECIFIC_HEAT_CAPACITY_UNIT();
                #8=AREA_DENSITY_UNIT();
                #9=VOLUMETRIC_FLOW_RATE_UNIT();
                #10=MASS_FLOW_RATE_UNIT();
                #11=ROTATIONAL_FREQUENCY_UNIT();
                #12=ANGULAR_VELOCITY_UNIT();
                #13=ANGULAR_ACCELERATION_UNIT();
                #14=TORQUE_UNIT();
                #15=LINEAR_FORCE_UNIT();
                #16=LINEAR_STIFFNESS_UNIT();
                #17=ROTATIONAL_STIFFNESS_UNIT();
                #18=LINEAR_MOMENT_UNIT();
                #21=MASS_DENSITY_MEASURE_WITH_UNIT(MASS_DENSITY_MEASURE(7.85),#1);
                #22=DYNAMIC_VISCOSITY_MEASURE_WITH_UNIT(DYNAMIC_VISCOSITY_MEASURE(0.01),#2);
                #23=KINEMATIC_VISCOSITY_MEASURE_WITH_UNIT(KINEMATIC_VISCOSITY_MEASURE(0.02),#3);
                #24=MOMENT_OF_INERTIA_MEASURE_WITH_UNIT(MOMENT_OF_INERTIA_MEASURE(3.0),#4);
                #25=THERMAL_CONDUCTIVITY_MEASURE_WITH_UNIT(THERMAL_CONDUCTIVITY_MEASURE(205.0),#5);
                #26=HEAT_FLUX_DENSITY_MEASURE_WITH_UNIT(HEAT_FLUX_DENSITY_MEASURE(12.0),#6);
                #27=SPECIFIC_HEAT_CAPACITY_MEASURE_WITH_UNIT(SPECIFIC_HEAT_CAPACITY_MEASURE(900.0),#7);
                #28=AREA_DENSITY_MEASURE_WITH_UNIT(AREA_DENSITY_MEASURE(2.7),#8);
                #29=VOLUMETRIC_FLOW_RATE_MEASURE_WITH_UNIT(VOLUMETRIC_FLOW_RATE_MEASURE(1.5),#9);
                #30=MASS_FLOW_RATE_MEASURE_WITH_UNIT(MASS_FLOW_RATE_MEASURE(0.8),#10);
                #31=ROTATIONAL_FREQUENCY_MEASURE_WITH_UNIT(ROTATIONAL_FREQUENCY_MEASURE(60.0),#11);
                #32=ANGULAR_VELOCITY_MEASURE_WITH_UNIT(ANGULAR_VELOCITY_MEASURE(3.14),#12);
                #33=ANGULAR_ACCELERATION_MEASURE_WITH_UNIT(ANGULAR_ACCELERATION_MEASURE(1.5),#13);
                #34=TORQUE_MEASURE_WITH_UNIT(TORQUE_MEASURE(20.0),#14);
                #35=LINEAR_FORCE_MEASURE_WITH_UNIT(LINEAR_FORCE_MEASURE(4.0),#15);
                #36=LINEAR_STIFFNESS_MEASURE_WITH_UNIT(LINEAR_STIFFNESS_MEASURE(1000.0),#16);
                #37=ROTATIONAL_STIFFNESS_MEASURE_WITH_UNIT(ROTATIONAL_STIFFNESS_MEASURE(250.0),#17);
                #38=LINEAR_MOMENT_MEASURE_WITH_UNIT(LINEAR_MOMENT_MEASURE(6.0),#18);
                ENDSEC;
                """);

        assertDumpContains(file,
                "MASS_DENSITY_MEASURE_WITH_UNIT #21:",
                "DYNAMIC_VISCOSITY_MEASURE_WITH_UNIT #22:",
                "KINEMATIC_VISCOSITY_MEASURE_WITH_UNIT #23:",
                "MOMENT_OF_INERTIA_MEASURE_WITH_UNIT #24:",
                "THERMAL_CONDUCTIVITY_MEASURE_WITH_UNIT #25:",
                "HEAT_FLUX_DENSITY_MEASURE_WITH_UNIT #26:",
                "SPECIFIC_HEAT_CAPACITY_MEASURE_WITH_UNIT #27:",
                "AREA_DENSITY_MEASURE_WITH_UNIT #28:",
                "VOLUMETRIC_FLOW_RATE_MEASURE_WITH_UNIT #29:",
                "MASS_FLOW_RATE_MEASURE_WITH_UNIT #30:",
                "ROTATIONAL_FREQUENCY_MEASURE_WITH_UNIT #31:",
                "ANGULAR_VELOCITY_MEASURE_WITH_UNIT #32:",
                "ANGULAR_ACCELERATION_MEASURE_WITH_UNIT #33:",
                "TORQUE_MEASURE_WITH_UNIT #34:",
                "LINEAR_FORCE_MEASURE_WITH_UNIT #35:",
                "LINEAR_STIFFNESS_MEASURE_WITH_UNIT #36:",
                "ROTATIONAL_STIFFNESS_MEASURE_WITH_UNIT #37:",
                "LINEAR_MOMENT_MEASURE_WITH_UNIT #38:");
    }

    @Test
    void shouldPreserveDerivedAndOffsetUnitNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-derived-offset-unit-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT($,.METRE.));
                #2=(TIME_UNIT() NAMED_UNIT(*) SI_UNIT($,.SECOND.));
                #3=DERIVED_UNIT_ELEMENT(#1,1.0);
                #4=DERIVED_UNIT_ELEMENT(#2,-1.0);
                #5=DERIVED_UNIT((#3,#4));
                #6=(THERMODYNAMIC_TEMPERATURE_UNIT() NAMED_UNIT(*) SI_UNIT($,.KELVIN.));
                #7=MEASURE_WITH_UNIT(THERMODYNAMIC_TEMPERATURE_MEASURE(1.0),#6);
                #8=(CONVERSION_BASED_UNIT_WITH_OFFSET(THERMODYNAMIC_TEMPERATURE_MEASURE(273.15))
                    CONVERSION_BASED_UNIT('DEG_C',#7)
                    NAMED_UNIT(*)
                    THERMODYNAMIC_TEMPERATURE_UNIT());
                ENDSEC;
                """);

        assertDumpContains(file,
                "DERIVED_UNIT_ELEMENT #3: builtItems=1, unsupportedFaces=0",
                "DERIVED_UNIT_ELEMENT #4: builtItems=1, unsupportedFaces=0",
                "DERIVED_UNIT #5: builtItems=2, unsupportedFaces=0",
                "MEASURE_WITH_UNIT #7: builtItems=1, unsupportedFaces=0",
                "CONVERSION_BASED_UNIT_WITH_OFFSET #8: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveDerivedAndOffsetUnitEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-derived-offset-unit-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT($,.METRE.));
                #2=(TIME_UNIT() NAMED_UNIT(*) SI_UNIT($,.SECOND.));
                #3=DERIVED_UNIT_ELEMENT(#1,1.0);
                #4=DERIVED_UNIT_ELEMENT(#2,-1.0);
                #5=DERIVED_UNIT((#3,#4));
                #6=(THERMODYNAMIC_TEMPERATURE_UNIT() NAMED_UNIT(*) SI_UNIT($,.KELVIN.));
                #7=MEASURE_WITH_UNIT(THERMODYNAMIC_TEMPERATURE_MEASURE(1.0),#6);
                #8=(CONVERSION_BASED_UNIT_WITH_OFFSET(THERMODYNAMIC_TEMPERATURE_MEASURE(273.15))
                    CONVERSION_BASED_UNIT('DEG_C',#7)
                    NAMED_UNIT(*)
                    THERMODYNAMIC_TEMPERATURE_UNIT());
                ENDSEC;
                """);

        assertDumpContains(file,
                "DERIVED_UNIT_ELEMENT #3:",
                "DERIVED_UNIT_ELEMENT #4:",
                "DERIVED_UNIT #5:",
                "MEASURE_WITH_UNIT #7:",
                "CONVERSION_BASED_UNIT_WITH_OFFSET #8:");
    }

    @Test
    void shouldPreserveRepresentationItemLeafNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-representation-item-leaf-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=REPRESENTATION_ITEM('REP_ITEM_ONLY');
                #2=(GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('GEOM_ITEM_ONLY'));
                #3=(TOPOLOGICAL_REPRESENTATION_ITEM('TOPO_ITEM_ONLY'));
                #4=DIMENSIONAL_EXPONENTS(1.0,0.0,0.0,0.0,0.0,0.0,0.0);
                ENDSEC;
                """);

        assertDumpContains(file,
                "REPRESENTATION_ITEM #1: builtItems=1, unsupportedFaces=0",
                "GEOMETRIC_REPRESENTATION_ITEM #2: builtItems=1, unsupportedFaces=0",
                "TOPOLOGICAL_REPRESENTATION_ITEM #3: builtItems=1, unsupportedFaces=0",
                "DIMENSIONAL_EXPONENTS #4: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveRepresentationItemEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-representation-item-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=REPRESENTATION_ITEM('REP_ITEM_ONLY');
                #2=(GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('GEOM_ITEM_ONLY'));
                #3=(TOPOLOGICAL_REPRESENTATION_ITEM('TOPO_ITEM_ONLY'));
                #4=DIMENSIONAL_EXPONENTS(1.0,0.0,0.0,0.0,0.0,0.0,0.0);
                ENDSEC;
                """);

        assertDumpContains(file,
                "REPRESENTATION_ITEM #1:",
                "GEOMETRIC_REPRESENTATION_ITEM #2:",
                "TOPOLOGICAL_REPRESENTATION_ITEM #3:",
                "DIMENSIONAL_EXPONENTS #4:");
    }

    @Test
    void shouldPreserveSpecificRepresentationItemLeafNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-specific-representation-item-leaf-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));
                #2=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #3=VALUE_REPRESENTATION_ITEM('roughness',DESCRIPTIVE_MEASURE('Ra 3.2'));
                #4=MEASURE_REPRESENTATION_ITEM('thickness',LENGTH_MEASURE(2.5),#1);
                ENDSEC;
                """);

        assertDumpContains(file,
                "DESCRIPTIVE_REPRESENTATION_ITEM #2: builtItems=1, unsupportedFaces=0",
                "VALUE_REPRESENTATION_ITEM #3: builtItems=1, unsupportedFaces=0",
                "MEASURE_REPRESENTATION_ITEM #4: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveSpecificRepresentationItemEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-specific-representation-item-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));
                #2=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #3=VALUE_REPRESENTATION_ITEM('roughness',DESCRIPTIVE_MEASURE('Ra 3.2'));
                #4=MEASURE_REPRESENTATION_ITEM('thickness',LENGTH_MEASURE(2.5),#1);
                ENDSEC;
                """);

        assertDumpContains(file,
                "DESCRIPTIVE_REPRESENTATION_ITEM #2:",
                "VALUE_REPRESENTATION_ITEM #3:",
                "MEASURE_REPRESENTATION_ITEM #4:");
    }

    @Test
    void shouldReportMarkerBaseEntitiesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-marker-base-entities", ".step");
        Files.writeString(file, """
                DATA;
                #1=(POINT() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('P'));
                #2=(CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('C'));
                #3=(SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('S'));
                #4=(SURFACE_MODEL() REPRESENTATION_ITEM('SM'));
                #5=(SOLID_MODEL() REPRESENTATION_ITEM('SO'));
                #6=(VERTEX() TOPOLOGICAL_REPRESENTATION_ITEM('V'));
                #7=(EDGE() TOPOLOGICAL_REPRESENTATION_ITEM('E'));
                #8=(FACE() TOPOLOGICAL_REPRESENTATION_ITEM('F'));
                ENDSEC;
                """);

        assertDumpContains(file,
                "POINT #1: builtItems=1, unsupportedFaces=0",
                "CURVE #2: builtItems=1, unsupportedFaces=0",
                "SURFACE #3: builtItems=1, unsupportedFaces=0",
                "SURFACE_MODEL #4: builtItems=1, unsupportedFaces=0",
                "SOLID_MODEL #5: builtItems=1, unsupportedFaces=0",
                "VERTEX #6: builtItems=1, unsupportedFaces=0",
                "EDGE #7: builtItems=1, unsupportedFaces=0",
                "FACE #8: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveMarkerBaseEntityNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-marker-base-entity-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=(POINT() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('P'));
                #2=(CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('C'));
                #3=(SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('S'));
                #4=(SURFACE_MODEL() REPRESENTATION_ITEM('SM'));
                #5=(SOLID_MODEL() REPRESENTATION_ITEM('SO'));
                #6=(VERTEX() TOPOLOGICAL_REPRESENTATION_ITEM('V'));
                #7=(EDGE() TOPOLOGICAL_REPRESENTATION_ITEM('E'));
                #8=(FACE() TOPOLOGICAL_REPRESENTATION_ITEM('F'));
                ENDSEC;
                """);

        assertDumpContains(file,
                "POINT #1:",
                "CURVE #2:",
                "SURFACE #3:",
                "SURFACE_MODEL #4:",
                "SOLID_MODEL #5:",
                "VERTEX #6:",
                "EDGE #7:",
                "FACE #8:");
    }

    @Test
    void shouldReportStandaloneGeometryAndPlacementLeavesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-geometry-placement-leaves", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=VECTOR('VZ',#2,2.0);
                #5=AXIS1_PLACEMENT('A1',#1,#2);
                #6=AXIS2_PLACEMENT_3D('A3',#1,#2,#3);
                #7=CARTESIAN_POINT('P2',(1.0,2.0));
                #8=DIRECTION('D2',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('A2',#7,#8);
                #16=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #10=LINE('L0',#1,#4);
                #11=CIRCLE('C0',#6,3.0);
                #12=ELLIPSE('E0',#6,4.0,2.0);
                #13=POLYLINE('PL0',(#1,#16));
                #14=PLANE('PLN',#6);
                #15=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#3,#2,#1,1.0,#2);
                ENDSEC;
                """);

        assertDumpContains(file,
                "CARTESIAN_POINT #1: builtItems=1, unsupportedFaces=0",
                "DIRECTION #2: builtItems=1, unsupportedFaces=0",
                "DIRECTION #3: builtItems=1, unsupportedFaces=0",
                "VECTOR #4: builtItems=1, unsupportedFaces=0",
                "AXIS1_PLACEMENT #5: builtItems=1, unsupportedFaces=0",
                "AXIS2_PLACEMENT_3D #6: builtItems=1, unsupportedFaces=0",
                "CARTESIAN_POINT #7: builtItems=1, unsupportedFaces=0",
                "DIRECTION #8: builtItems=1, unsupportedFaces=0",
                "AXIS2_PLACEMENT_2D #9: builtItems=1, unsupportedFaces=0",
                "LINE #10: builtItems=1, unsupportedFaces=0",
                "CIRCLE #11: builtItems=1, unsupportedFaces=0",
                "ELLIPSE #12: builtItems=1, unsupportedFaces=0",
                "POLYLINE #13: builtItems=1, unsupportedFaces=0",
                "PLANE #14: builtItems=1, unsupportedFaces=0",
                "CARTESIAN_TRANSFORMATION_OPERATOR_3D #15: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveStandaloneGeometryAndPlacementLeafNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-geometry-placement-leaf-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=VECTOR('VZ',#2,2.0);
                #5=AXIS1_PLACEMENT('A1',#1,#2);
                #6=AXIS2_PLACEMENT_3D('A3',#1,#2,#3);
                #7=CARTESIAN_POINT('P2',(1.0,2.0));
                #8=DIRECTION('D2',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('A2',#7,#8);
                #16=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #10=LINE('L0',#1,#4);
                #11=CIRCLE('C0',#6,3.0);
                #12=ELLIPSE('E0',#6,4.0,2.0);
                #13=POLYLINE('PL0',(#1,#16));
                #14=PLANE('PLN',#6);
                #15=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#3,#2,#1,1.0,#2);
                ENDSEC;
                """);

        assertDumpContains(file,
                "CARTESIAN_POINT #1:",
                "DIRECTION #2:",
                "DIRECTION #3:",
                "VECTOR #4:",
                "AXIS1_PLACEMENT #5:",
                "AXIS2_PLACEMENT_3D #6:",
                "CARTESIAN_POINT #7:",
                "DIRECTION #8:",
                "AXIS2_PLACEMENT_2D #9:",
                "LINE #10:",
                "CIRCLE #11:",
                "ELLIPSE #12:",
                "POLYLINE #13:",
                "PLANE #14:",
                "CARTESIAN_TRANSFORMATION_OPERATOR_3D #15:");
    }

    @Test
    void shouldReportCurveAndSurfaceWrapperEntitiesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-curve-surface-wrappers", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #4=CARTESIAN_POINT('SHIFT',(10.0,0.0,0.0));
                #5=DIRECTION('DX',(1.0,0.0,0.0));
                #6=DIRECTION('DZ',(0.0,0.0,1.0));
                #7=VECTOR('V0',#5,1.0);
                #8=LINE('L0',#1,#7);
                #9=ORIENTED_CURVE('OC0',#8,.F.);
                #10=COMPOSITE_CURVE_SEGMENT(.CONTINUOUS.,.T.,#9);
                #11=(COMPOSITE_CURVE('CC0',(#10),.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('cc-name'));
                #12=(COMPOSITE_CURVE_ON_SURFACE('CCS0',(#10),.F.) COMPOSITE_CURVE('CCS0',(#10),.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('ccs-name'));
                #13=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',$,$,#4,1.0,$);
                #14=CURVE_REPLICA('CR0',#8,#13);
                #15=AXIS2_PLACEMENT_3D('AX',#1,#6,#5);
                #16=PLANE('PL0',#15);
                #17=SURFACE_REPLICA('SR0',#16,#13);
                #18=RECTANGULAR_TRIMMED_SURFACE('RTS0',#17,0.0,1.0,0.0,1.0,.T.,.T.);
                #19=ORIENTED_SURFACE('OS0',#18,.F.);
                #20=CURVE_BOUNDED_SURFACE('CBS0',#19,(#14),.T.);
                #21=CARTESIAN_POINT('Q0',(0.0,0.0));
                #22=CARTESIAN_POINT('Q1',(1.0,0.0));
                #23=POLYLINE('PL2',(#21,#22));
                #24=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID2','PARAM'));
                #25=REPRESENTATION('R2',(#23),#24);
                #26=PCURVE('PC0',#20,#25);
                #27=TRIMMED_CURVE('TC0',#8,(#1),(#2),.T.,.CARTESIAN.);
                #28=OFFSET_CURVE_2D('OC2',#23,0.25,.F.);
                #29=DIRECTION('DY',(0.0,1.0,0.0));
                #30=OFFSET_CURVE_3D('OC3',#8,0.5,.F.,#29);
                ENDSEC;
                """);

        assertDumpContains(file,
                "ORIENTED_CURVE #9: builtItems=1, unsupportedFaces=0",
                "COMPOSITE_CURVE #11: builtItems=1, unsupportedFaces=0",
                "COMPOSITE_CURVE_ON_SURFACE #12: builtItems=1, unsupportedFaces=0",
                "CURVE_REPLICA #14: builtItems=2, unsupportedFaces=0",
                "SURFACE_REPLICA #17: builtItems=2, unsupportedFaces=0",
                "RECTANGULAR_TRIMMED_SURFACE #18: builtItems=2, unsupportedFaces=0",
                "ORIENTED_SURFACE #19: builtItems=2, unsupportedFaces=0",
                "CURVE_BOUNDED_SURFACE #20: builtItems=4, unsupportedFaces=0",
                "PCURVE #26: builtItems=1, unsupportedFaces=0",
                "TRIMMED_CURVE #27: builtItems=1, unsupportedFaces=0",
                "OFFSET_CURVE_2D #28: builtItems=1, unsupportedFaces=0",
                "OFFSET_CURVE_3D #30: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveReplicaAndTransformationAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-replica-transformation-alias", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=DIRECTION('DZ',(0.0,0.0,1.0));
                #4=CARTESIAN_POINT('SHIFT',(10.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX',#1,#3,#2);
                #6=VECTOR('VX',#2,1.0);
                #7=LINE('L0',#1,#6);
                #8=PLANE('PL0',#5);
                #9=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T3',$,$,#4,1.0,$);
                #10=POINT_REPLICA('PR0',#1,#9);
                #11=CURVE_REPLICA('CR0',#7,#9);
                #12=SURFACE_REPLICA('SR0',#8,#9);
                #13=CARTESIAN_POINT('P2',(1.0,2.0));
                #14=DIRECTION('D2',(1.0,0.0));
                #15=CARTESIAN_TRANSFORMATION_OPERATOR_2D('T2',$,#14,#13,$);
                ENDSEC;
                """);

        assertDumpContains(file,
                "CARTESIAN_TRANSFORMATION_OPERATOR_3D #9: builtItems=1, unsupportedFaces=0",
                "POINT_REPLICA #10: builtItems=2, unsupportedFaces=0",
                "CURVE_REPLICA #11: builtItems=2, unsupportedFaces=0",
                "SURFACE_REPLICA #12: builtItems=2, unsupportedFaces=0",
                "CARTESIAN_TRANSFORMATION_OPERATOR_2D #15: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveSweptAreaSolidAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-swept-area-solid-alias", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=AXIS1_PLACEMENT('AX1',#1,#2);
                #6=CARTESIAN_POINT('P2',(0.0,0.0));
                #7=DIRECTION('D2',(1.0,0.0));
                #8=AXIS2_PLACEMENT_2D('PRF',#6,#7);
                #9=CIRCLE_PROFILE_DEF(.AREA.,'C',#8,2.0);
                #10=RECTANGLE_PROFILE_DEF(.AREA.,'R',#8,4.0,2.0);
                #11=EXTRUDED_AREA_SOLID('EX',#9,#4,#2,5.0);
                #12=REVOLVED_AREA_SOLID('RV',#10,#4,#5,1.57079632679);
                ENDSEC;
                """);

        assertDumpContains(file,
                "EXTRUDED_AREA_SOLID #11:",
                "REVOLVED_AREA_SOLID #12:");
    }

    @Test
    void shouldPreserveCsgPrimitiveAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-csg-primitive-alias", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('A3',#1,#2,#3);
                #5=AXIS1_PLACEMENT('A1',#1,#2);
                #6=BLOCK('BLK',#4,10.0,20.0,30.0);
                #7=SPHERE('SPH',#4,5.0);
                #8=RIGHT_CIRCULAR_CYLINDER('CYL',#5,12.0,3.0);
                ENDSEC;
                """);

        assertDumpContains(file,
                "BLOCK #6:",
                "SPHERE #7:",
                "RIGHT_CIRCULAR_CYLINDER #8:");
    }

    @Test
    void shouldPreserveAdditionalCsgPrimitiveAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-additional-csg-primitive-alias", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=AXIS1_PLACEMENT('AX1',#1,#2);
                #6=ELLIPSOID('EL',#4,3.0,2.0,1.0);
                #7=TORUS('TO',#5,5.0,1.0);
                #8=RIGHT_ANGULAR_WEDGE('WG',#4,4.0,3.0,2.0,2.5);
                ENDSEC;
                """);

        assertDumpContains(file,
                "ELLIPSOID #6:",
                "TORUS #7:",
                "RIGHT_ANGULAR_WEDGE #8:");
    }

    @Test
    void shouldPreserveHalfSpaceAndBoxDomainAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-halfspace-boxdomain-alias", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('PLN',#4);
                #6=BOX_DOMAIN(#1,10.0,20.0,30.0);
                #7=HALF_SPACE_SOLID('HS',#5,.T.);
                #8=BOXED_HALF_SPACE('BHS',#5,.F.,#6);
                ENDSEC;
                """);

        assertDumpContains(file,
                "BOX_DOMAIN #6: builtItems=1, unsupportedFaces=0",
                "HALF_SPACE_SOLID #7: builtItems=1, unsupportedFaces=0",
                "BOXED_HALF_SPACE #8: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveAdditionalProfileDefinitionAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-additional-profile-def-alias", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P2',(0.0,0.0));
                #2=DIRECTION('D2',(1.0,0.0));
                #3=AXIS2_PLACEMENT_2D('PRF',#1,#2);
                #4=ELLIPSE_PROFILE_DEF(.AREA.,'ellipse profile',#3,5.0,2.0);
                #5=ROUNDED_RECTANGLE_PROFILE_DEF(.AREA.,'rounded rectangle',#3,8.0,4.0,0.5);
                #6=CENTERED_RECTANGLE_PROFILE_DEF(.AREA.,'centered rectangle',#3,3.0,7.0);
                #7=CIRCULAR_HOLLOW_PROFILE_DEF(.AREA.,'hollow circle',#3,6.0,0.5);
                ENDSEC;
                """);

        assertDumpContains(file,
                "ELLIPSE_PROFILE_DEF #4: builtItems=1, unsupportedFaces=0",
                "ROUNDED_RECTANGLE_PROFILE_DEF #5: builtItems=1, unsupportedFaces=0",
                "CENTERED_RECTANGLE_PROFILE_DEF #6: builtItems=1, unsupportedFaces=0",
                "CIRCULAR_HOLLOW_PROFILE_DEF #7: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveBasicProfileDefinitionAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-basic-profile-def-alias", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P2',(0.0,0.0));
                #2=DIRECTION('D2',(1.0,0.0));
                #3=AXIS2_PLACEMENT_2D('PRF',#1,#2);
                #4=CARTESIAN_POINT('Q0',(0.0,0.0));
                #5=CARTESIAN_POINT('Q1',(1.0,0.0));
                #6=POLYLINE('PL2',(#4,#5));
                #7=COMPOSITE_CURVE_SEGMENT(.CONTINUOUS.,.T.,#6);
                #8=(COMPOSITE_CURVE('CC0',(#7),.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('cc-name'));
                #9=CIRCLE_PROFILE_DEF(.AREA.,'C',#3,2.0);
                #10=RECTANGLE_PROFILE_DEF(.AREA.,'R',#3,4.0,2.0);
                #11=ARBITRARY_CLOSED_PROFILE_DEF(.AREA.,'ACP',#8);
                #12=ARBITRARY_PROFILE_DEF(.AREA.,'AP',#8);
                #13=ARBITRARY_OPEN_PROFILE_DEF(.CURVE.,'AOP',#8);
                ENDSEC;
                """);

        assertDumpContains(file,
                "CIRCLE_PROFILE_DEF #9: builtItems=1, unsupportedFaces=0",
                "RECTANGLE_PROFILE_DEF #10: builtItems=1, unsupportedFaces=0",
                "ARBITRARY_CLOSED_PROFILE_DEF #11: builtItems=1, unsupportedFaces=0",
                "ARBITRARY_PROFILE_DEF #12: builtItems=1, unsupportedFaces=0",
                "ARBITRARY_OPEN_PROFILE_DEF #13: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveSurfaceCurveAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-surface-curve-alias", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=VECTOR('V0',#3,1.0);
                #6=LINE('L0',#1,#5);
                #7=AXIS2_PLACEMENT_3D('AX',#1,#4,#3);
                #8=PLANE('PL0',#7);
                #9=CARTESIAN_POINT('Q0',(0.0,0.0));
                #10=CARTESIAN_POINT('Q1',(1.0,0.0));
                #11=POLYLINE('PL2',(#9,#10));
                #12=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID2','PARAM'));
                #13=REPRESENTATION('R2',(#11),#12);
                #14=PCURVE('PC0',#8,#13);
                #15=DEGENERATE_PCURVE('DPC0',#8,#13);
                #16=SURFACE_CURVE('SC0',#6,(#14),.CURVE_3D.);
                #17=SEAM_CURVE('SM0',#6,(#14,#15),.CURVE_3D.);
                ENDSEC;
                """);

        assertDumpContains(file,
                "PCURVE #14: builtItems=1, unsupportedFaces=0",
                "DEGENERATE_PCURVE #15: builtItems=2, unsupportedFaces=0",
                "SURFACE_CURVE #16: builtItems=1, unsupportedFaces=0",
                "SEAM_CURVE #17: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveConicCurveAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-conic-curve-alias", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PARABOLA('P0',#4,2.0);
                #6=HYPERBOLA('H0',#4,4.0,2.0);
                #7=DEGENERATE_CONIC('DC0',#4);
                ENDSEC;
                """);

        assertDumpContains(file,
                "PARABOLA #5: builtItems=1, unsupportedFaces=0",
                "HYPERBOLA #6: builtItems=1, unsupportedFaces=0",
                "DEGENERATE_CONIC #7: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveSurfaceAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-surface-alias", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=CYLINDRICAL_SURFACE('CY0',#4,2.0);
                #6=CONICAL_SURFACE('CO0',#4,2.0,0.5);
                #7=TOROIDAL_SURFACE('TO0',#4,5.0,1.0);
                #8=SPHERICAL_SURFACE('SPH0',#4,2.0);
                #9=DEGENERATE_TOROIDAL_SURFACE('DTS0',#4,5.0,1.0,.T.);
                ENDSEC;
                """);

        assertDumpContains(file,
                "CYLINDRICAL_SURFACE #5: builtItems=1, unsupportedFaces=0",
                "CONICAL_SURFACE #6: builtItems=1, unsupportedFaces=0",
                "TOROIDAL_SURFACE #7: builtItems=1, unsupportedFaces=0",
                "SPHERICAL_SURFACE #8: builtItems=1, unsupportedFaces=0",
                "DEGENERATE_TOROIDAL_SURFACE #9: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldReportSplineSweptSurfaceAndProfileEntitiesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-spline-swept-profile", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P10',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P01',(0.0,1.0,0.0));
                #4=CARTESIAN_POINT('P11',(1.0,1.0,1.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=VECTOR('VZ',#5,1.0);
                #7=AXIS1_PLACEMENT('A1',#1,#5);
                #8=(B_SPLINE_CURVE('RBC0',2,(#1,#2,#4),.UNSPECIFIED.,.F.,.F.)
                    B_SPLINE_CURVE_WITH_KNOTS((3,3),(0.0,1.0),.UNSPECIFIED.)
                    RATIONAL_B_SPLINE_CURVE((1.0,0.5,1.0)));
                #9=SURFACE_OF_LINEAR_EXTRUSION('SLE0',#8,#6);
                #10=SURFACE_OF_REVOLUTION('SOR0',#8,#7);
                #11=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.));
                #12=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.)
                     RATIONAL_B_SPLINE_SURFACE(((1.0,1.0),(1.0,0.5))));
                #20=CARTESIAN_POINT('Q0',(0.0,0.0));
                #21=CARTESIAN_POINT('Q1',(1.0,0.0));
                #22=CARTESIAN_POINT('Q2',(1.0,1.0));
                #23=DIRECTION('DX2',(1.0,0.0));
                #24=AXIS2_PLACEMENT_2D('AX2',#20,#23);
                #25=POLYLINE('PLC',(#20,#21,#22,#20));
                #26=POLYLINE('PLO',(#20,#21,#22));
                #27=CIRCLE_PROFILE_DEF(.AREA.,'C',#24,2.0);
                #28=RECTANGLE_PROFILE_DEF(.AREA.,'R',#24,4.0,2.0);
                #29=ARBITRARY_CLOSED_PROFILE_DEF(.AREA.,'ACP',#25);
                #30=ARBITRARY_PROFILE_DEF(.AREA.,'AP',#25);
                #31=ARBITRARY_OPEN_PROFILE_DEF(.CURVE.,'AOP',#26);
                ENDSEC;
                """);

        assertDumpContains(file,
                "RATIONAL_B_SPLINE_CURVE #8: builtItems=1, unsupportedFaces=0",
                "SURFACE_OF_LINEAR_EXTRUSION #9: builtItems=2, unsupportedFaces=0",
                "SURFACE_OF_REVOLUTION #10: builtItems=2, unsupportedFaces=0",
                "B_SPLINE_SURFACE_WITH_KNOTS #11: builtItems=1, unsupportedFaces=0",
                "RATIONAL_B_SPLINE_SURFACE #12: builtItems=1, unsupportedFaces=0",
                "CIRCLE_PROFILE_DEF #27: builtItems=1, unsupportedFaces=0",
                "RECTANGLE_PROFILE_DEF #28: builtItems=1, unsupportedFaces=0",
                "ARBITRARY_CLOSED_PROFILE_DEF #29: builtItems=1, unsupportedFaces=0",
                "ARBITRARY_PROFILE_DEF #30: builtItems=1, unsupportedFaces=0",
                "ARBITRARY_OPEN_PROFILE_DEF #31: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldBuildOffsetSurfaceFaceFamiliesInShellSummary() throws IOException {
        Path file = Files.createTempFile("minicad-offset-surface-face-families", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=AXIS1_PLACEMENT('A1',#1,#2);
                #10=CONICAL_SURFACE('CN0',#4,2.0,0.5);
                #11=OFFSET_SURFACE('OCN0',#10,0.5,.F.);
                #12=CARTESIAN_POINT('CN-P0',(2.4387912809451864,0.0,-0.2397127693021015));
                #13=CARTESIAN_POINT('CN-P1',(0.0,2.4387912809451864,-0.2397127693021015));
                #14=CARTESIAN_POINT('CN-P2',(0.0,2.985093611541959,-1.2397127693021015));
                #15=POLY_LOOP('CN-L0',(#12,#13,#14));
                #16=FACE_OUTER_BOUND('CN-B0',#15,.T.);
                #17=ADVANCED_FACE('CN-F0',(#16),#11,.T.);
                #20=TOROIDAL_SURFACE('TO0',#4,5.0,1.0);
                #21=OFFSET_SURFACE('OTO0',#20,0.5,.F.);
                #22=CARTESIAN_POINT('TO-P0',(6.5,0.0,0.0));
                #23=CARTESIAN_POINT('TO-P1',(0.0,6.5,0.0));
                #24=CARTESIAN_POINT('TO-P2',(0.0,5.0,1.5));
                #25=POLY_LOOP('TO-L0',(#22,#23,#24));
                #26=FACE_OUTER_BOUND('TO-B0',#25,.T.);
                #27=ADVANCED_FACE('TO-F0',(#26),#21,.T.);
                #30=VECTOR('VZ',#2,1.0);
                #31=LINE('GEN0',#1,#30);
                #32=SURFACE_OF_LINEAR_EXTRUSION('SLE0',#31,#30);
                #33=OFFSET_SURFACE('OSLE0',#32,1.0,.F.);
                #34=CARTESIAN_POINT('EX-P0',(1.0,0.0,0.0));
                #35=CARTESIAN_POINT('EX-P1',(1.0,0.0,1.0));
                #36=CARTESIAN_POINT('EX-P2',(1.0,0.0,2.0));
                #37=POLY_LOOP('EX-L0',(#34,#35,#36));
                #38=FACE_OUTER_BOUND('EX-B0',#37,.T.);
                #39=ADVANCED_FACE('EX-F0',(#38),#33,.T.);
                #40=CARTESIAN_POINT('R0',(1.0,0.0,0.0));
                #41=LINE('GEN1',#40,#30);
                #42=SURFACE_OF_REVOLUTION('SOR0',#41,#5);
                #43=OFFSET_SURFACE('OSOR0',#42,1.0,.F.);
                #44=CARTESIAN_POINT('RV-P0',(2.0,0.0,0.0));
                #45=CARTESIAN_POINT('RV-P1',(2.0,0.0,1.0));
                #46=CARTESIAN_POINT('RV-P2',(2.0,0.0,2.0));
                #47=POLY_LOOP('RV-L0',(#44,#45,#46));
                #48=FACE_OUTER_BOUND('RV-B0',#47,.T.);
                #49=ADVANCED_FACE('RV-F0',(#48),#43,.T.);
                #50=CARTESIAN_POINT('P00',(0.0,0.0,0.0));
                #51=CARTESIAN_POINT('P10',(1.0,0.0,0.0));
                #52=CARTESIAN_POINT('P01',(0.0,1.0,0.0));
                #53=CARTESIAN_POINT('P11',(1.0,1.0,1.0));
                #54=(B_SPLINE_SURFACE(1,1,((#50,#52),(#51,#53)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.));
                #55=OFFSET_SURFACE('OBS0',#54,0.25,.F.);
                #56=CARTESIAN_POINT('BS-P0',(0.0,0.0,0.25));
                #57=CARTESIAN_POINT('BS-P1',(1.0,0.0,0.25));
                #58=CARTESIAN_POINT('BS-P2',(1.0,1.0,1.25));
                #59=POLY_LOOP('BS-L0',(#56,#57,#58));
                #60=FACE_OUTER_BOUND('BS-B0',#59,.T.);
                #61=ADVANCED_FACE('BS-F0',(#60),#55,.T.);
                #90=OPEN_SHELL('OS',(#17,#27,#39,#49,#61));
                ENDSEC;
                """);

        assertDumpContains(file,
                "OFFSET_SURFACE #11:",
                "OFFSET_SURFACE #21:",
                "OFFSET_SURFACE #33:",
                "OFFSET_SURFACE #43:",
                "OFFSET_SURFACE #55:",
                "OPEN_SHELL #90: faces=5, unsupportedFaces=0");
    }

    @Test
    void shouldReportConicSurfaceAndHalfSpaceEntitiesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-conic-surface-halfspace", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PARABOLA('P0',#4,2.0);
                #6=HYPERBOLA('H0',#4,4.0,2.0);
                #7=DEGENERATE_CONIC('DC0',#4);
                #8=CYLINDRICAL_SURFACE('CY0',#4,2.0);
                #9=CONICAL_SURFACE('CO0',#4,2.0,0.5);
                #10=TOROIDAL_SURFACE('TO0',#4,5.0,1.0);
                #11=SPHERICAL_SURFACE('SPH0',#4,2.0);
                #12=DEGENERATE_TOROIDAL_SURFACE('DTS0',#4,5.0,1.0,.T.);
                #13=BOX_DOMAIN(#1,10.0,20.0,30.0);
                #14=PLANE('PL0',#4);
                #15=HALF_SPACE_SOLID('HS',#14,.T.);
                #16=BOXED_HALF_SPACE('BHS',#14,.F.,#13);
                ENDSEC;
                """);

        assertDumpContains(file,
                "PARABOLA #5: builtItems=1, unsupportedFaces=0",
                "HYPERBOLA #6: builtItems=1, unsupportedFaces=0",
                "DEGENERATE_CONIC #7: builtItems=1, unsupportedFaces=0",
                "CYLINDRICAL_SURFACE #8: builtItems=1, unsupportedFaces=0",
                "CONICAL_SURFACE #9: builtItems=1, unsupportedFaces=0",
                "TOROIDAL_SURFACE #10: builtItems=1, unsupportedFaces=0",
                "SPHERICAL_SURFACE #11: builtItems=1, unsupportedFaces=0",
                "DEGENERATE_TOROIDAL_SURFACE #12: builtItems=1, unsupportedFaces=0",
                "BOX_DOMAIN #13: builtItems=1, unsupportedFaces=0",
                "HALF_SPACE_SOLID #15: builtItems=1, unsupportedFaces=0",
                "BOXED_HALF_SPACE #16: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldReportLayerStyleVariableAndDegeneratePcurveEntitiesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-layer-style-variable", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #6=CARTESIAN_POINT('Q0',(0.0,0.0));
                #7=CARTESIAN_POINT('Q1',(1.0,0.0));
                #8=POLYLINE('PL2',(#6,#7));
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID2','PARAM'));
                #10=REPRESENTATION('R2',(#8),#9);
                #11=DEGENERATE_PCURVE('DPC0',#5,#10);
                #12=PRE_DEFINED_MARKER('DOT');
                #13=COLOUR_RGB('red',1.0,0.0,0.0);
                #14=POINT_STYLE('PS0',#12,2.0,#13);
                #15=SYMBOL_COLOUR(#13);
                #38=SYMBOL_STYLE('SS0',#15);
                #16=PRESENTATION_LAYER_ASSIGNMENT('L1','layer one',(#14,#38,#11));
                #17=PROPERTY_DEFINITION('PD0','',#1);
                #18=REPRESENTATION('R3',(#1),#9);
                #19=ABSTRACT_VARIABLE(#17,#18);
                #20=ROW_VARIABLE(#17,#18);
                #21=SCALAR_VARIABLE(#17,#18);
                #22=FORWARD_CHAINING_RULE_PREMISE(#17,#18);
                #23=BACK_CHAINING_RULE_BODY(#17,#18);
                #24=PRODUCT('PRD','prod','',());
                #25=PRODUCT_RELATED_PRODUCT_CATEGORY('CAT','category',(#24));
                #26=COLOUR_SPECIFICATION('generic-colour');
                #27=DRAUGHTING_PRE_DEFINED_TEXT_FONT('ISO');
                ENDSEC;
                """);

        assertDumpContains(file,
                "DEGENERATE_PCURVE #11: builtItems=2, unsupportedFaces=0",
                "POINT_STYLE #14: builtItems=2, unsupportedFaces=0",
                "SYMBOL_STYLE #38: builtItems=1, unsupportedFaces=0",
                "PRESENTATION_LAYER_ASSIGNMENT #16: builtItems=5, unsupportedFaces=0",
                "ABSTRACT_VARIABLE #19: builtItems=2, unsupportedFaces=0",
                "ROW_VARIABLE #20: builtItems=2, unsupportedFaces=0",
                "SCALAR_VARIABLE #21: builtItems=2, unsupportedFaces=0",
                "FORWARD_CHAINING_RULE_PREMISE #22: builtItems=2, unsupportedFaces=0",
                "BACK_CHAINING_RULE_BODY #23: builtItems=2, unsupportedFaces=0",
                "PRODUCT_RELATED_PRODUCT_CATEGORY #25: builtItems=1, unsupportedFaces=0",
                "COLOUR_SPECIFICATION #26: builtItems=1, unsupportedFaces=0",
                "DRAUGHTING_PRE_DEFINED_TEXT_FONT #27: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldReportCharacterizedObjectAndCompositeCurveSegmentInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-characterized-segment", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('V0',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=COMPOSITE_CURVE_SEGMENT(.CONTINUOUS.,.T.,#4);
                #6=FEATURE_DEFINITION('FD0','feature');
                ENDSEC;
                """);

        assertDumpContains(file,
                "COMPOSITE_CURVE_SEGMENT #5: builtItems=1, unsupportedFaces=0",
                "FEATURE_DEFINITION #6: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveFeatureDefinitionAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-feature-definition-alias-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=CHARACTERIZED_OBJECT('CO','base characterized object');
                #2=FEATURE_DEFINITION('FD','feature definition');
                #3=ADDITIVE_MANUFACTURING_FEATURE('AMF','additive feature');
                #4=CONTACT_FEATURE_DEFINITION('CFD','contact feature');
                #5=EXTERNALLY_DEFINED_FEATURE_DEFINITION('EDFD','external feature');
                #6=FEATURE_DEFINITION_WITH_CONNECTION_AREA('FDWCA','connection area');
                #7=SHAPE_FEATURE_DEFINITION('SFD','shape feature');
                #8=BASIC_ROUND_HOLE('BRH','round hole');
                #9=COUNTERBORE_HOLE_DEFINITION('CBHD','counterbore');
                #10=SPOTFACE_HOLE_DEFINITION('SHD','spotface hole');
                #11=TURNED_KNURL('TK','turned knurl');
                ENDSEC;
                """);

        assertDumpContains(file,
                "CHARACTERIZED_OBJECT #1: builtItems=1, unsupportedFaces=0",
                "FEATURE_DEFINITION #2: builtItems=1, unsupportedFaces=0",
                "ADDITIVE_MANUFACTURING_FEATURE #3: builtItems=1, unsupportedFaces=0",
                "CONTACT_FEATURE_DEFINITION #4: builtItems=1, unsupportedFaces=0",
                "EXTERNALLY_DEFINED_FEATURE_DEFINITION #5: builtItems=1, unsupportedFaces=0",
                "FEATURE_DEFINITION_WITH_CONNECTION_AREA #6: builtItems=1, unsupportedFaces=0",
                "SHAPE_FEATURE_DEFINITION #7: builtItems=1, unsupportedFaces=0",
                "BASIC_ROUND_HOLE #8: builtItems=1, unsupportedFaces=0",
                "COUNTERBORE_HOLE_DEFINITION #9: builtItems=1, unsupportedFaces=0",
                "SPOTFACE_HOLE_DEFINITION #10: builtItems=1, unsupportedFaces=0",
                "TURNED_KNURL #11: builtItems=1, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveShapeAspectOccurrenceAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-shape-aspect-occurrence-alias-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','first',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('def','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('shape','primary shape',#6);
                #8=APPLIED_AREA('AA','applied area',#7,.T.);
                #9=BASIC_ROUND_HOLE_OCCURRENCE('BRHO','basic round hole',#7,.T.,#50);
                #16=COUNTERBORE_HOLE_OCCURRENCE('CBHO','counterbore',#7,.T.,#51);
                #17=COUNTERDRILL_HOLE_OCCURRENCE('CDHO','counterdrill',#7,.T.,#52);
                #18=COUNTERSINK_HOLE_OCCURRENCE('CSHO','countersink',#7,.T.,#53);
                #50=BASIC_ROUND_HOLE('BRH','round hole definition');
                #51=COUNTERBORE_HOLE_DEFINITION('CBHD','counterbore definition');
                #52=COUNTERDRILL_HOLE_DEFINITION('CDHD','counterdrill definition');
                #53=COUNTERSINK_HOLE_DEFINITION('CSHD','countersink definition');
                ENDSEC;
                """);

        assertDumpContains(file,
                "BASIC_ROUND_HOLE_OCCURRENCE #9: builtItems=3, unsupportedFaces=0",
                "COUNTERBORE_HOLE_OCCURRENCE #16: builtItems=3, unsupportedFaces=0",
                "COUNTERDRILL_HOLE_OCCURRENCE #17: builtItems=3, unsupportedFaces=0",
                "COUNTERSINK_HOLE_OCCURRENCE #18: builtItems=3, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveShapeAspectAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-shape-aspect-alias-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','first',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('def','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('shape','primary shape',#6);
                #8=APPLIED_AREA('AA','applied area',#7,.T.);
                #10=BEAD_END('BE','bead end',#7,.T.);
                #15=CONTACTING_FEATURE('CF2','contacting',#7,.T.);
                #23=GEOMETRIC_TOLERANCE_WITH_MODIFIERS('GTWM','tolerance modifiers',#7,.T.);
                #28=PATH_FEATURE_COMPONENT('PFC','path feature',#7,.T.);
                #36=TOLERANCE_ZONE_DEFINITION('TZD','tolerance zone',#7,.T.);
                ENDSEC;
                """);

        assertDumpContains(file,
                "APPLIED_AREA #8: builtItems=2, unsupportedFaces=0",
                "BEAD_END #10: builtItems=2, unsupportedFaces=0",
                "CONTACTING_FEATURE #15: builtItems=2, unsupportedFaces=0",
                "GEOMETRIC_TOLERANCE_WITH_MODIFIERS #23: builtItems=2, unsupportedFaces=0",
                "PATH_FEATURE_COMPONENT #28: builtItems=2, unsupportedFaces=0",
                "TOLERANCE_ZONE_DEFINITION #36: builtItems=2, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveAdditionalExternallyDefinedItemAliasNamesInBuildSummary()
            throws IOException {
        Path file = Files.createTempFile("minicad-externally-defined-item-more-alias-names", ".step");
        Files.writeString(file, """
                DATA;
                #1=EXTERNAL_SOURCE('supplier-catalog');
                #2=EXTERNALLY_DEFINED_CLASS('class-1',#1);
                #3=EXTERNALLY_DEFINED_GENERAL_PROPERTY('property-1',#1);
                #4=EXTERNALLY_DEFINED_HATCH_STYLE('hatch-1',#1);
                #5=EXTERNALLY_DEFINED_CHARACTER_GLYPH('glyph-1',#1);
                #6=EXTERNALLY_DEFINED_DIMENSION_DEFINITION('dimension-1',#1);
                #7=EXTERNALLY_DEFINED_PICTURE_REPRESENTATION_ITEM('picture-1',#1);
                #8=EXTERNALLY_DEFINED_STYLE('style-1',#1);
                ENDSEC;
                """);

        assertDumpContains(file,
                "EXTERNALLY_DEFINED_CLASS #2: builtItems=1, unsupportedFaces=0",
                "EXTERNALLY_DEFINED_GENERAL_PROPERTY #3: builtItems=1, unsupportedFaces=0",
                "EXTERNALLY_DEFINED_HATCH_STYLE #4: builtItems=1, unsupportedFaces=0",
                "EXTERNALLY_DEFINED_CHARACTER_GLYPH #5: builtItems=1, unsupportedFaces=0",
                "EXTERNALLY_DEFINED_DIMENSION_DEFINITION #6: builtItems=1, unsupportedFaces=0",
                "EXTERNALLY_DEFINED_PICTURE_REPRESENTATION_ITEM #7: builtItems=1, unsupportedFaces=0",
                "EXTERNALLY_DEFINED_STYLE #8: builtItems=1, unsupportedFaces=0");
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

        assertDumpContains(file,
                "OPEN_SHELL #60: faces=0, unsupportedFaces=1",
                "unsupportedReasons:",
                "lie on edge curve",
                "unsupportedReasonCodes: topology.edge_vertex_off_curve:1",
                "unsupportedFaces=1");
    }

    @Test
    void shouldReportUnsupportedCylindricalFacesWithoutFailingWholeDump() throws IOException {
        Path file = Files.createTempFile("minicad-cylinder-face", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #13=CYLINDRICAL_SURFACE('CY0',#12,2.0);
                #20=CARTESIAN_POINT('P0',(2.0,0.0,0.0));
                #21=CARTESIAN_POINT('P1',(0.0,2.0,0.0));
                #22=CARTESIAN_POINT('P2',(0.0,2.0,1.0));
                #60=POLY_LOOP('L0',(#20,#21,#22));
                #61=FACE_OUTER_BOUND('B0',#60,.T.);
                #70=ADVANCED_FACE('F0',(#61),#13,.T.);
                #80=OPEN_SHELL('OS',(#70));
                ENDSEC;
                """);

        assertDumpContains(file,
                "CYLINDRICAL_SURFACE #13: builtItems=1, unsupportedFaces=0",
                "ADVANCED_FACE #70: builtItems=1, unsupportedFaces=0",
                "OPEN_SHELL #80: faces=1, unsupportedFaces=0");
    }

    @Test
    void shouldClassifyWrappedUnsupportedSurfaceByUnderlyingType() throws IOException {
        Path file = Files.createTempFile("minicad-wrapped-surface", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=AXIS1_PLACEMENT('A1',#1,#2);
                #6=CARTESIAN_POINT('P0',(2.0,0.0,0.0));
                #7=DIRECTION('DY',(0.0,1.0,0.0));
                #8=VECTOR('V0',#7,1.0);
                #9=LINE('L0',#6,#8);
                #10=SURFACE_OF_REVOLUTION('SR',#9,#5);
                #11=RECTANGULAR_TRIMMED_SURFACE('RTS',#10,0.0,1.0,0.0,1.0,.T.,.T.);
                #20=VERTEX_POINT('V0',#6);
                #21=CARTESIAN_POINT('P1',(2.0,1.0,0.0));
                #22=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #23=CARTESIAN_POINT('P3',(1.0,0.0,0.0));
                #24=VERTEX_POINT('V1',#21);
                #25=VERTEX_POINT('V2',#22);
                #26=VERTEX_POINT('V3',#23);
                #30=EDGE_CURVE('E0',#20,#24,#9,.T.);
                #31=DIRECTION('MX',(-1.0,0.0,0.0));
                #32=VECTOR('VMX',#31,1.0);
                #33=LINE('L1',#21,#32);
                #34=EDGE_CURVE('E1',#24,#25,#33,.T.);
                #35=DIRECTION('MY',(0.0,-1.0,0.0));
                #36=VECTOR('VMY',#35,1.0);
                #37=LINE('L2',#22,#36);
                #38=EDGE_CURVE('E2',#25,#26,#37,.T.);
                #39=DIRECTION('PX',(1.0,0.0,0.0));
                #40=VECTOR('VPX',#39,1.0);
                #41=LINE('L3',#23,#40);
                #42=EDGE_CURVE('E3',#26,#20,#41,.T.);
                #50=ORIENTED_EDGE('OE0',$,$,#30,.T.);
                #51=ORIENTED_EDGE('OE1',$,$,#34,.T.);
                #52=ORIENTED_EDGE('OE2',$,$,#38,.T.);
                #53=ORIENTED_EDGE('OE3',$,$,#42,.T.);
                #60=EDGE_LOOP('L0',(#50,#51,#52,#53));
                #61=FACE_OUTER_BOUND('B0',#60,.T.);
                #70=ADVANCED_FACE('F0',(#61),#11,.T.);
                #80=OPEN_SHELL('OS',(#70));
                ENDSEC;
                """);

        assertDumpContains(file,
                "SURFACE_OF_REVOLUTION #10: builtItems=2, unsupportedFaces=0",
                "ADVANCED_FACE #70: builtItems=1, unsupportedFaces=0",
                "OPEN_SHELL #80: faces=1, unsupportedFaces=0");
    }

    @Test
    void shouldReportUnsupportedToroidalFacesWithoutFailingWholeDump() throws IOException {
        Path file = Files.createTempFile("minicad-toroidal-face", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=TOROIDAL_SURFACE('T0',#4,5.0,1.0);
                #6=CARTESIAN_POINT('P0',(6.0,0.0,0.0));
                #7=CARTESIAN_POINT('P1',(5.0,1.0,0.0));
                #8=CARTESIAN_POINT('P2',(4.0,0.0,0.0));
                #9=POLY_LOOP('L0',(#6,#7,#8));
                #10=FACE_OUTER_BOUND('B0',#9,.T.);
                #11=ADVANCED_FACE('F0',(#10),#5,.T.);
                #12=OPEN_SHELL('OS',(#11));
                ENDSEC;
                """);

        assertDumpContains(file,
                "TOROIDAL_SURFACE #5: builtItems=1, unsupportedFaces=0",
                "ADVANCED_FACE #11: builtItems=1, unsupportedFaces=0",
                "OPEN_SHELL #12: faces=1, unsupportedFaces=0");
    }

    @Test
    void shouldReportUnsupportedDegenerateToroidalFacesWithoutFailingWholeDump() throws IOException {
        Path file = Files.createTempFile("minicad-degenerate-toroidal-face", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=DEGENERATE_TOROIDAL_SURFACE('DT0',#4,5.0,1.0,.T.);
                #6=CARTESIAN_POINT('P0',(6.0,0.0,0.0));
                #7=CARTESIAN_POINT('P1',(5.0,1.0,0.0));
                #8=CARTESIAN_POINT('P2',(4.0,0.0,0.0));
                #10=POLY_LOOP('L0',(#6,#7,#8));
                #11=FACE_OUTER_BOUND('B0',#10,.T.);
                #12=ADVANCED_FACE('F0',(#11),#5,.T.);
                #13=OPEN_SHELL('OS',(#12));
                ENDSEC;
                """);

        assertDumpContains(file,
                "DEGENERATE_TOROIDAL_SURFACE #5: builtItems=1, unsupportedFaces=0",
                "ADVANCED_FACE #12: builtItems=1, unsupportedFaces=0",
                "OPEN_SHELL #13: faces=1, unsupportedFaces=0");
    }

    @Test
    void shouldReportUnsupportedSphericalFacesWithoutFailingWholeDump() throws IOException {
        Path file = Files.createTempFile("minicad-spherical-face", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=SPHERICAL_SURFACE('SP0',#4,2.0);
                #6=CARTESIAN_POINT('P0',(2.0,0.0,0.0));
                #7=CARTESIAN_POINT('P1',(0.0,2.0,0.0));
                #8=CARTESIAN_POINT('P2',(0.0,0.0,2.0));
                #10=POLY_LOOP('L0',(#6,#7,#8));
                #11=FACE_OUTER_BOUND('B0',#10,.T.);
                #12=ADVANCED_FACE('F0',(#11),#5,.T.);
                #13=OPEN_SHELL('OS',(#12));
                ENDSEC;
                """);

        assertDumpContains(file,
                "SPHERICAL_SURFACE #5: builtItems=1, unsupportedFaces=0",
                "ADVANCED_FACE #12: builtItems=1, unsupportedFaces=0",
                "OPEN_SHELL #13: faces=1, unsupportedFaces=0");
    }

    @Test
    void shouldReportZeroScaleSurfaceReplicaFacesWithoutFailingWholeDump() throws IOException {
        Path file = Files.createTempFile("minicad-zero-scale-surface-replica", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #13=PLANE('PL0',#12);
                #14=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',$,$,#1,0.0,$);
                #15=SURFACE_REPLICA('SR0',#13,#14);
                #30=VERTEX_POINT('V0',#1);
                #31=VERTEX_POINT('V1',#2);
                #32=VERTEX_POINT('V2',#3);
                #33=VERTEX_POINT('V3',#4);
                #40=DIRECTION('D1',(1.0,0.0,0.0));
                #41=VECTOR('VE1',#40,1.0);
                #42=LINE('E1',#1,#41);
                #43=DIRECTION('D2',(0.0,1.0,0.0));
                #44=VECTOR('VE2',#43,1.0);
                #45=LINE('E2',#2,#44);
                #46=DIRECTION('D3',(-1.0,0.0,0.0));
                #47=VECTOR('VE3',#46,1.0);
                #48=LINE('E3',#3,#47);
                #49=DIRECTION('D4',(0.0,-1.0,0.0));
                #50=VECTOR('VE4',#49,1.0);
                #51=LINE('E4',#4,#50);
                #60=EDGE_CURVE('EC1',#30,#31,#42,.T.);
                #61=EDGE_CURVE('EC2',#31,#32,#45,.T.);
                #62=EDGE_CURVE('EC3',#32,#33,#48,.T.);
                #63=EDGE_CURVE('EC4',#33,#30,#51,.T.);
                #70=ORIENTED_EDGE('OE1',$,$,#60,.T.);
                #71=ORIENTED_EDGE('OE2',$,$,#61,.T.);
                #72=ORIENTED_EDGE('OE3',$,$,#62,.T.);
                #73=ORIENTED_EDGE('OE4',$,$,#63,.T.);
                #80=EDGE_LOOP('LOOP',(#70,#71,#72,#73));
                #81=FACE_OUTER_BOUND('FOB',#80,.T.);
                #82=ADVANCED_FACE('F0',(#81),#15,.T.);
                #83=OPEN_SHELL('OS',(#82));
                ENDSEC;
                """);

        assertDumpContains(file,
                "OPEN_SHELL #83: faces=0, unsupportedFaces=1",
                "unsupportedReasons: SURFACE_REPLICA zero scale is unsupported:1",
                "unsupportedReasonCodes: unsupported_surface.replica_zero_scale:1",
                "unsupportedFaces=1");
    }

    @Test
    void shouldTreatSurfaceReplicaNonUniformScaleAsUnsupportedDuringSummary() throws IOException {
        Path file = Files.createTempFile("minicad-surface-replica-non-uniform-scale", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #13=PLANE('PL0',#12);
                #14=CARTESIAN_POINT('T0',(0.0,0.0,2.0));
                #15=DIRECTION('SX',(1.0,0.0,0.0));
                #16=DIRECTION('SY',(1.0,1.0,0.0));
                #17=DIRECTION('SZ',(0.0,0.0,1.0));
                #18=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#15,#16,#14,1.0,#17);
                #19=SURFACE_REPLICA('SR0',#13,#18);
                #30=VERTEX_POINT('V0',#1);
                #31=VERTEX_POINT('V1',#2);
                #32=VERTEX_POINT('V2',#3);
                #33=VERTEX_POINT('V3',#4);
                #40=DIRECTION('D1',(1.0,0.0,0.0));
                #41=VECTOR('VE1',#40,1.0);
                #42=LINE('E1',#1,#41);
                #43=DIRECTION('D2',(0.0,1.0,0.0));
                #44=VECTOR('VE2',#43,1.0);
                #45=LINE('E2',#2,#44);
                #46=DIRECTION('D3',(-1.0,0.0,0.0));
                #47=VECTOR('VE3',#46,1.0);
                #48=LINE('E3',#3,#47);
                #49=DIRECTION('D4',(0.0,-1.0,0.0));
                #50=VECTOR('VE4',#49,1.0);
                #51=LINE('E4',#4,#50);
                #60=EDGE_CURVE('EC1',#30,#31,#42,.T.);
                #61=EDGE_CURVE('EC2',#31,#32,#45,.T.);
                #62=EDGE_CURVE('EC3',#32,#33,#48,.T.);
                #63=EDGE_CURVE('EC4',#33,#30,#51,.T.);
                #70=ORIENTED_EDGE('OE1',$,$,#60,.T.);
                #71=ORIENTED_EDGE('OE2',$,$,#61,.T.);
                #72=ORIENTED_EDGE('OE3',$,$,#62,.T.);
                #73=ORIENTED_EDGE('OE4',$,$,#63,.T.);
                #80=EDGE_LOOP('LOOP',(#70,#71,#72,#73));
                #81=FACE_OUTER_BOUND('FOB',#80,.T.);
                #82=ADVANCED_FACE('F0',(#81),#19,.T.);
                #83=OPEN_SHELL('OS',(#82));
                ENDSEC;
                """);

        assertDumpContains(file,
                "OPEN_SHELL #83: faces=0, unsupportedFaces=1",
                "unsupportedReasons: SURFACE_REPLICA non-uniform scale is unsupported:1",
                "unsupportedReasonCodes: unsupported_surface.replica_non_uniform_scale:1",
                "unsupportedFaces=1");
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

        assertDumpContains(file,
                "OPEN_SHELL #80: faces=0, unsupportedFaces=1",
                "unsupportedReasons: all face vertices must lie on the plane:1",
                "unsupportedReasonCodes: topology.face_vertex_off_plane:1",
                "unsupportedFaces=1");
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

        assertDumpContains(file,
                "OPEN_SHELL #11: faces=1, unsupportedFaces=0");
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

        assertDumpContains(file,
                "BOOLEAN_RESULT #19: faces=0, unsupportedFaces=1",
                "unsupportedReasons: BOOLEAN_RESULT operator UNION is unsupported:1",
                "unsupportedReasonCodes: unsupported_boolean.result:1",
                "booleanResults=1");
    }

    @Test
    void shouldReportExtrudedAreaSolidAsBuildableItem() throws IOException {
        Path file = Files.createTempFile("minicad-extruded-solid", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file, "EXTRUDED_AREA_SOLID #9: shellFaces=6, unsupportedFaces=0");
    }

    @Test
    void shouldReportRevolvedAreaSolidAsBuildableItem() throws IOException {
        Path file = Files.createTempFile("minicad-revolved-solid", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file, "REVOLVED_AREA_SOLID #11: shellFaces=34, unsupportedFaces=0");
    }

    @Test
    void shouldReportExtendedExtrudedAreaSolidShellFaceCounts() throws IOException {
        Path file = Files.createTempFile("minicad-extended-extruded-solids", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=CARTESIAN_POINT('P2',(0.0,0.0));
                #6=DIRECTION('DX2',(1.0,0.0));
                #7=AXIS2_PLACEMENT_2D('AX2',#5,#6);
                #8=CIRCLE_PROFILE_DEF(.AREA.,'C',#7,2.0);
                #9=CIRCULAR_HOLLOW_PROFILE_DEF(.AREA.,'CH',#7,3.0,0.5);
                #10=EXTRUDED_AREA_SOLID('EX0',#8,#4,#2,5.0);
                #11=EXTRUDED_AREA_SOLID('EX1',#9,#4,#2,5.0);
                ENDSEC;
                """);

        assertDumpContains(file,
                "EXTRUDED_AREA_SOLID #10: shellFaces=74, unsupportedFaces=0",
                "EXTRUDED_AREA_SOLID #11: shellFaces=146, unsupportedFaces=0");
    }

    @Test
    void shouldReportSolidReplicaAsBuildableItem() throws IOException {
        Path file = Files.createTempFile("minicad-solid-replica", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file, "SOLID_REPLICA #13: shellFaces=6, unsupportedFaces=0");
    }

    @Test
    void shouldPreserveSolidReplicaAliasNameInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-solid-replica-alias", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file, "SOLID_REPLICA #13:");
    }

    @Test
    void shouldPreserveCsgSolidAliasNameInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-csg-solid-alias", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file, "CSG_SOLID #11:");
    }

    @Test
    void shouldPreserveBooleanAliasNamesInBuildSummary() throws IOException {
        Path file = Files.createTempFile("minicad-boolean-alias", ".step");
        Files.writeString(file, """
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
                #11=(BOOLEAN_CLIPPING_RESULT(.DIFFERENCE.,#5,#9) BOOLEAN_RESULT(.DIFFERENCE.,#5,#9) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BCR0'));
                ENDSEC;
                """);

        assertDumpContains(file,
                "BOOLEAN_RESULT #10:",
                "BOOLEAN_CLIPPING_RESULT #11:");
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

        assertDumpContains(file,
                "BOOLEAN_CLIPPING_RESULT #19: faces=0, unsupportedFaces=1",
                "unsupportedReasons: BOOLEAN_RESULT difference requires HALF_SPACE_SOLID or BOXED_HALF_SPACE second operand:1",
                "unsupportedReasonCodes: unsupported_boolean.clipping_result:1",
                "booleanResults=1");
    }

    @Test
    void shouldReportCsgSolidAndBooleanDifferenceAsBuildableItems() throws IOException {
        Path file = Files.createTempFile("minicad-csg-solid", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file,
                "BLOCK #5: shellFaces=6, unsupportedFaces=0",
                "BOOLEAN_RESULT #10: faces=6, unsupportedFaces=0",
                "CSG_SOLID #11: shellFaces=6, unsupportedFaces=0");
    }

    @Test
    void shouldReportBasicCsgPrimitiveShellFaceCounts() throws IOException {
        Path file = Files.createTempFile("minicad-basic-csg-primitives", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=AXIS1_PLACEMENT('AX1',#1,#2);
                #6=BLOCK('BLK',#4,10.0,20.0,30.0);
                #7=SPHERE('SP',#4,2.0);
                #8=RIGHT_CIRCULAR_CYLINDER('CY',#5,5.0,2.0);
                ENDSEC;
                """);

        assertDumpContains(file,
                "BLOCK #6: shellFaces=6, unsupportedFaces=0",
                "SPHERE #7: shellFaces=528, unsupportedFaces=0",
                "RIGHT_CIRCULAR_CYLINDER #8: shellFaces=50, unsupportedFaces=0");
    }

    @Test
    void shouldReportAdditionalCsgPrimitiveShellFaceCounts() throws IOException {
        Path file = Files.createTempFile("minicad-additional-csg-primitives", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=AXIS1_PLACEMENT('AX1',#1,#2);
                #6=ELLIPSOID('EL',#4,3.0,2.0,1.0);
                #7=TORUS('TO',#5,5.0,1.0);
                #8=RIGHT_ANGULAR_WEDGE('WG',#4,4.0,3.0,2.0,2.5);
                ENDSEC;
                """);

        assertDumpContains(file,
                "ELLIPSOID #6: shellFaces=528, unsupportedFaces=0",
                "TORUS #7: shellFaces=1024, unsupportedFaces=0",
                "RIGHT_ANGULAR_WEDGE #8: shellFaces=7, unsupportedFaces=0");
    }

    @Test
    void shouldReportBooleanClippingResultAsBuildableItem() throws IOException {
        Path file = Files.createTempFile("minicad-boolean-clipping-supported", ".step");
        Files.writeString(file, """
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

        assertDumpContains(file, "BOOLEAN_CLIPPING_RESULT #10: faces=6, unsupportedFaces=0");
    }

    @Test
    void shouldReportExtendedSweptProfilesAndCsgPrimitivesAsBuildableItems() throws IOException {
        Path file = Files.createTempFile("minicad-extended-solid-support", ".step");
        Files.writeString(file, """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=CARTESIAN_POINT('P2',(0.0,0.0));
                #6=DIRECTION('DX2',(1.0,0.0));
                #7=AXIS2_PLACEMENT_2D('AX2',#5,#6);
                #8=CIRCLE_PROFILE_DEF(.AREA.,'C',#7,2.0);
                #9=CIRCULAR_HOLLOW_PROFILE_DEF(.AREA.,'CH',#7,3.0,0.5);
                #10=EXTRUDED_AREA_SOLID('EX0',#8,#4,#2,5.0);
                #11=EXTRUDED_AREA_SOLID('EX1',#9,#4,#2,5.0);
                #12=SPHERE('SP',#4,2.0);
                #13=ELLIPSOID('EL',#4,3.0,2.0,1.0);
                #14=AXIS1_PLACEMENT('AX1',#1,#2);
                #15=RIGHT_CIRCULAR_CYLINDER('CY',#14,5.0,2.0);
                #16=TORUS('TO',#14,5.0,1.0);
                #17=RIGHT_ANGULAR_WEDGE('WG',#4,4.0,3.0,2.0,2.5);
                ENDSEC;
                """);

        assertDumpContains(file,
                "EXTRUDED_AREA_SOLID #10: shellFaces=74, unsupportedFaces=0",
                "EXTRUDED_AREA_SOLID #11: shellFaces=146, unsupportedFaces=0",
                "SPHERE #12: shellFaces=528, unsupportedFaces=0",
                "ELLIPSOID #13: shellFaces=528, unsupportedFaces=0",
                "RIGHT_CIRCULAR_CYLINDER #15: shellFaces=50, unsupportedFaces=0",
                "TORUS #16: shellFaces=1024, unsupportedFaces=0",
                "RIGHT_ANGULAR_WEDGE #17: shellFaces=7, unsupportedFaces=0");
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

    private static String runDump(Path file) throws IOException {
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();

        int exitCode = StepDumpApp.run(
                new String[]{file.toString()},
                sink(stdout),
                sink(stderr)
        );

        String output = stdout.toString();
        assertEquals(0, exitCode, output + "\nSTDERR:\n" + stderr);
        return output;
    }

    private static void assertDumpContains(Path file, String... expectedFragments) throws IOException {
        String output = runDump(file);
        for (String fragment : expectedFragments) {
            assertTrue(output.contains(fragment), output);
        }
    }
}
