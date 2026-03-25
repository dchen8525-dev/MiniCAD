package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.Circle;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Plane;
import com.minicad.step.model.StepEntity;
import com.minicad.step.syntax.StepParser;
import com.minicad.topology.Edge;
import com.minicad.topology.Face;
import com.minicad.topology.Shell;
import com.minicad.topology.Solid;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StepCadBuilderTest {

    @Test
    void shouldBuildGeometryObjectsFromResolvedModel() {
        StepCadBuilder builder = builder("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #6=CIRCLE('C0',#4,2.0);
                #7=VECTOR('V0',#3,5.0);
                #8=LINE('L0',#1,#7);
                ENDSEC;
                """);

        Axis2Placement3D placement = builder.buildPlacement(4);
        Plane plane = builder.buildPlane(5);
        Circle circle = builder.buildCircle(6);
        Line3 line = builder.buildLine(8);

        assertEquals(0.0, placement.location().x());
        assertEquals(2.0, circle.radius());
        assertEquals(1.0, plane.normal().z(), 1.0e-12);
        assertEquals(1.0, line.direction().x(), 1.0e-12);
    }

    @Test
    void shouldBuildPlanarFaceOpenShellAndSolid() {
        StepCadBuilder builder = builder("""
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
                #90=OPEN_SHELL('OS',(#80));
                #91=CLOSED_SHELL('CS',(#80));
                #100=MANIFOLD_SOLID_BREP('S0',#91);
                ENDSEC;
                """);

        Edge edge = builder.buildEdge(50);
        Face face = builder.buildFace(80);
        Shell openShell = builder.buildShell(90);
        Shell closedShell = builder.buildShell(91);
        Solid solid = builder.buildSolid(100);

        assertInstanceOf(Edge.class, edge);
        assertEquals(1, face.bounds().size());
        assertEquals(false, openShell.closed());
        assertEquals(true, closedShell.closed());
        assertEquals(true, solid.outerShell().closed());
    }

    @Test
    void shouldRejectCircularEdgeTopologyConstruction() {
        StepCadBuilder builder = builder("""
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
                ENDSEC;
                """);

        UnsupportedGeometryException exception = assertThrows(
                UnsupportedGeometryException.class,
                () -> builder.buildEdge(20)
        );

        assertEquals("circular EDGE_CURVE topology is unsupported", exception.getMessage());
    }

    @Test
    void shouldRejectWrongTopLevelEntityTypeForSolidBuild() {
        StepCadBuilder builder = builder("""
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                ENDSEC;
                """);

        StepResolutionException exception = assertThrows(
                StepResolutionException.class,
                () -> builder.buildSolid(1)
        );

        assertEquals("entity #1 is not a MANIFOLD_SOLID_BREP", exception.getMessage());
    }

    private static StepCadBuilder builder(String step) {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        return StepCadBuilder.fromResolved(resolved);
    }
}
