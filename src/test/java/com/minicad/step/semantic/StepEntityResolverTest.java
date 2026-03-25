package com.minicad.step.semantic;

import com.minicad.common.StepParseException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepPlane;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StepEntityResolverTest {

    @Test
    void shouldResolveSupportedEntitiesWithForwardReferences() {
        String step = """
                DATA;
                #20=EDGE_CURVE('E0',#10,#11,#30,.T.);
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #10=VERTEX_POINT('V0',#1);
                #11=VERTEX_POINT('V1',#2);
                #3=DIRECTION('D0',(1.0,0.0,0.0));
                #4=VECTOR('VEC0',#3,1.0);
                #30=LINE('L0',#1,#4);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEdgeCurve edgeCurve = assertInstanceOf(StepEdgeCurve.class, resolved.get(20));
        assertEquals(10, edgeCurve.start().id());
        assertEquals(30, edgeCurve.edgeGeometry().id());
    }

    @Test
    void shouldResolveMinimalSolidSemanticGraph() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepPlane plane = assertInstanceOf(StepPlane.class, resolved.get(13));
        assertEquals(12, plane.position().id());
        StepClosedShell shell = assertInstanceOf(StepClosedShell.class, resolved.get(90));
        assertEquals(1, shell.faces().size());
        StepManifoldSolidBrep solid = assertInstanceOf(StepManifoldSolidBrep.class, resolved.get(100));
        assertEquals(90, solid.outer().id());
    }

    @Test
    void shouldRejectMissingReference() {
        String step = """
                DATA;
                #1=VERTEX_POINT('V0',#99);
                ENDSEC;
                """;

        StepResolutionException exception = assertThrows(
                StepResolutionException.class,
                () -> StepEntityResolver.resolveAll(StepParser.parse(step))
        );

        assertEquals("missing referenced entity #99", exception.getMessage());
    }

    @Test
    void shouldRejectUnsupportedEntity() {
        String step = """
                DATA;
                #1=B_SPLINE_CURVE('C0');
                ENDSEC;
                """;

        UnsupportedStepEntityException exception = assertThrows(
                UnsupportedStepEntityException.class,
                () -> StepEntityResolver.resolveAll(StepParser.parse(step))
        );

        assertEquals("unsupported STEP entity B_SPLINE_CURVE", exception.getMessage());
    }

    @Test
    void shouldRejectUnsupportedAdvancedFaceGeometry() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=CIRCLE('C0',#4,2.0);
                #6=EDGE_LOOP('L0',());
                #7=FACE_OUTER_BOUND('B0',#6,.T.);
                #8=ADVANCED_FACE('F0',(#7),#5,.T.);
                ENDSEC;
                """;

        UnsupportedStepEntityException exception = assertThrows(
                UnsupportedStepEntityException.class,
                () -> StepEntityResolver.resolveAll(StepParser.parse(step))
        );

        assertEquals("ADVANCED_FACE geometry must be PLANE", exception.getMessage());
    }

    @Test
    void shouldRejectAxisPlacementWithoutExplicitDirections() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=AXIS2_PLACEMENT_3D('AX',#1,$,$);
                ENDSEC;
                """;

        UnsupportedStepEntityException exception = assertThrows(
                UnsupportedStepEntityException.class,
                () -> StepEntityResolver.resolveAll(StepParser.parse(step))
        );

        assertEquals("AXIS2_PLACEMENT_3D requires explicit axis and ref direction", exception.getMessage());
    }

    @Test
    void shouldRejectDuplicateIdsAtSyntaxLayer() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #1=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                ENDSEC;
                """;

        StepParseException exception = assertThrows(StepParseException.class, () -> {
            StepFile file = StepParser.parse(step);
            file.entitiesById();
        });

        assertEquals("duplicate entity id #1", exception.getMessage());
    }

    @Test
    void shouldResolveCartesianPointCoordinates() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepCartesianPoint point = assertInstanceOf(StepCartesianPoint.class, resolved.get(1));
        assertEquals(3, point.coordinates().size());
        assertEquals(2.0, point.coordinates().get(1));
    }
}
