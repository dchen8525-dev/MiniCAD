package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.BSplineSurface3;
import com.minicad.geometry.Circle;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.SurfaceCurve3;
import com.minicad.geometry.ToroidalSurface;
import com.minicad.geometry.TrimmedCurve3;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.BSplineCurve2;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

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
                #7=CYLINDRICAL_SURFACE('CY0',#4,3.0);
                #8=VECTOR('V0',#3,5.0);
                #9=LINE('L0',#1,#8);
                ENDSEC;
                """);

        Axis2Placement3D placement = builder.buildPlacement(4);
        Plane plane = builder.buildPlane(5);
        Circle circle = builder.buildCircle(6);
        CylindricalSurface cylindricalSurface = builder.buildCylindricalSurface(7);
        Line3 line = builder.buildLine(9);

        assertEquals(0.0, placement.location().x());
        assertEquals(2.0, circle.radius());
        assertEquals(3.0, cylindricalSurface.radius());
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
    void shouldBuildCircularEdgeTopology() {
        StepCadBuilder builder = builder("""
                DATA;
                #1=CARTESIAN_POINT('CENTER',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P0',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P1',(0.0,1.0,0.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #13=CIRCLE('C0',#12,1.0);
                #20=VERTEX_POINT('V0',#2);
                #21=VERTEX_POINT('V1',#3);
                #30=EDGE_CURVE('E0',#20,#21,#13,.T.);
                ENDSEC;
                """);

        Edge edge = builder.buildEdge(30);

        assertInstanceOf(Circle.class, edge.curve());
        assertTrue(edge.curve().contains(edge.start().point()));
        assertTrue(edge.curve().contains(edge.end().point()));
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

    @Test
    void shouldRejectCylindricalAdvancedFaceConstruction() {
        StepCadBuilder builder = builder("""
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
                ENDSEC;
                """);

        UnsupportedGeometryException exception = assertThrows(
                UnsupportedGeometryException.class,
                () -> builder.buildFace(70)
        );

        assertEquals("ADVANCED_FACE construction for CYLINDRICAL_SURFACE is unsupported", exception.getMessage());
    }

    @Test
    void shouldBuildPlanarFaceSurfaceAndOrientedFace() {
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
                #80=FACE_SURFACE('FS0',(#71),#13,.T.);
                #81=ORIENTED_FACE('OF0',#80,.F.);
                #90=OPEN_SHELL('OS',(#81));
                ENDSEC;
                """);

        Face face = builder.buildFace(80);
        Face orientedFace = builder.buildFace(81);
        Shell openShell = builder.buildShell(90);

        assertEquals(true, face.sameSense());
        assertEquals(false, orientedFace.sameSense());
        assertEquals(1, openShell.faces().size());
    }

    @Test
    void shouldBuildVertexLoopFaceConstruction() {
        StepCadBuilder builder = builder("""
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
                ENDSEC;
                """);

        Face face = builder.buildFace(13);

        assertEquals(1, face.bounds().size());
        assertEquals(true, face.bounds().getFirst().loop() instanceof com.minicad.topology.VertexLoop);
    }

    @Test
    void shouldBuildConicalSurfaceAndTrimmedCurveEdge() {
        StepCadBuilder builder = builder("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=CONICAL_SURFACE('CN0',#4,2.0,0.2);
                #6=CARTESIAN_POINT('P0',(2.0,0.0,0.0));
                #7=CARTESIAN_POINT('P1',(0.0,2.0,0.0));
                #8=VERTEX_POINT('V0',#6);
                #9=VERTEX_POINT('V1',#7);
                #10=CIRCLE('C0',#4,2.0);
                #11=TRIMMED_CURVE('TC0',#10,(#6),(#7),.T.,.CARTESIAN.);
                #12=EDGE_CURVE('E0',#8,#9,#11,.T.);
                ENDSEC;
                """);

        ConicalSurface conicalSurface = builder.buildConicalSurface(5);
        Edge edge = builder.buildEdge(12);

        assertEquals(2.0, conicalSurface.radius());
        assertInstanceOf(TrimmedCurve3.class, edge.curve());
    }

    @Test
    void shouldRejectConicalFaceConstruction() {
        StepCadBuilder builder = builder("""
                DATA;
                #1=CARTESIAN_POINT('O0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('A0',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('B0',(0.0,2.0,0.0));
                #4=CARTESIAN_POINT('O1',(0.0,0.0,2.0));
                #5=CARTESIAN_POINT('A1',(2.4,0.0,2.0));
                #6=CARTESIAN_POINT('B1',(0.0,2.4,2.0));
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=AXIS2_PLACEMENT_3D('AX0',#1,#10,#11);
                #13=AXIS2_PLACEMENT_3D('AX1',#4,#10,#11);
                #14=CONICAL_SURFACE('CN0',#12,2.0,0.19739555984988078);
                #15=CIRCLE('C0',#12,2.0);
                #16=CIRCLE('C1',#13,2.4);
                #20=VERTEX_POINT('V0',#2);
                #21=VERTEX_POINT('V1',#3);
                #22=VERTEX_POINT('V2',#6);
                #23=VERTEX_POINT('V3',#5);
                #30=DIRECTION('DB',(0.0,0.4,2.0));
                #31=VECTOR('VB',#30,1.0);
                #32=LINE('L0',#3,#31);
                #33=DIRECTION('DA',(0.4,0.0,2.0));
                #34=VECTOR('VA',#33,1.0);
                #35=LINE('L1',#2,#34);
                #40=EDGE_CURVE('E0',#20,#21,#15,.T.);
                #41=EDGE_CURVE('E1',#21,#22,#32,.T.);
                #42=EDGE_CURVE('E2',#23,#22,#16,.T.);
                #43=EDGE_CURVE('E3',#20,#23,#35,.T.);
                #50=ORIENTED_EDGE('OE0',$,$,#40,.T.);
                #51=ORIENTED_EDGE('OE1',$,$,#41,.T.);
                #52=ORIENTED_EDGE('OE2',$,$,#42,.F.);
                #53=ORIENTED_EDGE('OE3',$,$,#43,.F.);
                #60=EDGE_LOOP('L0',(#50,#51,#52,#53));
                #61=FACE_OUTER_BOUND('B0',#60,.T.);
                #70=ADVANCED_FACE('F0',(#61),#14,.T.);
                ENDSEC;
                """);

        UnsupportedGeometryException exception = assertThrows(
                UnsupportedGeometryException.class,
                () -> builder.buildFace(70)
        );

        assertEquals("ADVANCED_FACE construction for CONICAL_SURFACE is unsupported", exception.getMessage());
    }

    @Test
    void shouldBuildEllipseAndSurfaceCurve() {
        StepCadBuilder builder = builder("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P0',(3.0,0.0,0.0));
                #3=CARTESIAN_POINT('P1',(0.0,2.0,0.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=DIRECTION('DX',(1.0,0.0,0.0));
                #6=AXIS2_PLACEMENT_3D('AX',#1,#4,#5);
                #7=ELLIPSE('E0',#6,3.0,2.0);
                #8=SURFACE_CURVE('SC0',#7,(),.T.);
                #9=VERTEX_POINT('V0',#2);
                #10=VERTEX_POINT('V1',#3);
                #11=EDGE_CURVE('E1',#9,#10,#8,.T.);
                ENDSEC;
                """);

        Ellipse3 ellipse = builder.buildEllipse(7);
        SurfaceCurve3 surfaceCurve = builder.buildSurfaceCurve(8);
        Edge edge = builder.buildEdge(11);

        assertEquals(3.0, ellipse.semiAxis1());
        assertInstanceOf(SurfaceCurve3.class, surfaceCurve);
        assertInstanceOf(SurfaceCurve3.class, edge.curve());
    }

    @Test
    void shouldBuildToroidalSurfaceSplineAndTrimmedSurfaceCurveEdge() {
        StepCadBuilder builder = builder("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=TOROIDAL_SURFACE('T0',#4,5.0,1.0);
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
                ENDSEC;
                """);

        ToroidalSurface torus = builder.buildToroidalSurface(5);
        BSplineCurve3 spline = builder.buildBSplineCurve(13);
        SurfaceCurve3 surfaceCurve = builder.buildSurfaceCurve(14);
        TrimmedCurve3 trimmedCurve = builder.buildTrimmedCurve(15);
        Edge edge = builder.buildEdge(18);

        assertEquals(5.0, torus.majorRadius());
        assertEquals(1.0, torus.minorRadius());
        assertEquals(2, spline.degree());
        assertEquals(9, spline.sample(8).size());
        assertInstanceOf(SurfaceCurve3.class, surfaceCurve);
        assertInstanceOf(TrimmedCurve3.class, trimmedCurve);
        assertInstanceOf(TrimmedCurve3.class, edge.curve());
    }

    @Test
    void shouldBuildBSplineSurfaceGeometry() {
        StepCadBuilder builder = builder("""
                DATA;
                #1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P10',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('P01',(0.0,2.0,0.0));
                #4=CARTESIAN_POINT('P11',(2.0,2.0,1.0));
                #10=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.));
                ENDSEC;
                """);

        BSplineSurface3 surface = builder.buildBSplineSurface(10);

        assertEquals(1, surface.uDegree());
        assertEquals(1, surface.vDegree());
        assertEquals(0.25, surface.pointAt(0.5, 0.5).z(), 1.0e-9);
    }

    @Test
    void shouldBuild2dPcurveLine() {
        StepCadBuilder builder = builder("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #10=CARTESIAN_POINT('UV0',(0.0,0.0));
                #11=DIRECTION('DU',(1.0,0.0));
                #12=VECTOR('VU',#11,1.0);
                #13=LINE('L2D',#10,#12);
                #14=REPRESENTATION_CONTEXT('PCURVE','PARAMETRIC');
                #15=DEFINITIONAL_REPRESENTATION('DEF',(#13),#14);
                #16=PCURVE('PC0',#5,#15);
                ENDSEC;
                """);

        Line2 line = assertInstanceOf(Line2.class, builder.buildPcurve2(16));

        assertTrue(line.contains(new Point2(2.0, 0.0)));
        assertEquals(new Point2(3.0, 0.0), line.pointAt(3.0));
    }

    @Test
    void shouldBuildSeamCurveAsSurfaceCurve3() {
        StepCadBuilder builder = builder("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=CYLINDRICAL_SURFACE('CY0',#4,1.0);
                #6=CIRCLE('C0',#4,1.0);
                #10=CARTESIAN_POINT('UV0',(0.0,0.0));
                #11=DIRECTION('DU',(1.0,0.0));
                #12=VECTOR('VU',#11,1.0);
                #13=LINE('L2D0',#10,#12);
                #14=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');
                #15=DEFINITIONAL_REPRESENTATION('DEF0',(#13),#14);
                #16=PCURVE('PC0',#5,#15);
                #17=CARTESIAN_POINT('UV1',(0.0,6.283185307179586));
                #18=LINE('L2D1',#17,#12);
                #19=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');
                #20=DEFINITIONAL_REPRESENTATION('DEF1',(#18),#19);
                #21=PCURVE('PC1',#5,#20);
                #22=SEAM_CURVE('SEAM0',#6,(#16,#21),.PCURVE_S1.);
                ENDSEC;
                """);

        SurfaceCurve3 seam = builder.buildSeamCurve(22);

        assertInstanceOf(SurfaceCurve3.class, seam);
        assertTrue(seam.contains(new com.minicad.geometry.Circle(new Axis2Placement3D(
                new com.minicad.geometry.CartesianPoint(0.0, 0.0, 0.0),
                com.minicad.geometry.Direction3.from(new com.minicad.geometry.Vector3(0.0, 0.0, 1.0)),
                com.minicad.geometry.Direction3.from(new com.minicad.geometry.Vector3(1.0, 0.0, 0.0))
        ), 1.0).pointAt(0.0)));
    }

    @Test
    void shouldBuild2dBsplinePcurve() {
        StepCadBuilder builder = builder("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=CYLINDRICAL_SURFACE('CY0',#4,1.0);
                #10=CARTESIAN_POINT('UV0',(0.0,0.0));
                #11=CARTESIAN_POINT('UV1',(0.2,0.45));
                #12=CARTESIAN_POINT('UV2',(0.0,1.0));
                #13=(B_SPLINE_CURVE('B2D',2,(#10,#11,#12),.UNSPECIFIED.,.F.,.F.)
                     B_SPLINE_CURVE_WITH_KNOTS((3,3),(0.0,1.0),.UNSPECIFIED.));
                #14=REPRESENTATION_CONTEXT('PC','PARAMETRIC');
                #15=DEFINITIONAL_REPRESENTATION('DEF',(#13),#14);
                #16=PCURVE('PC0',#5,#15);
                ENDSEC;
                """);

        BSplineCurve2 spline = assertInstanceOf(BSplineCurve2.class, builder.buildPcurve2(16));

        assertEquals(new Point2(0.0, 0.0), spline.pointAt(spline.startParameter()));
        assertEquals(new Point2(0.0, 1.0), spline.pointAt(spline.endParameter()));
        assertTrue(spline.sample(8).size() >= 9);
    }

    @Test
    void shouldRejectBSplineSurfaceFaceConstruction() {
        StepCadBuilder builder = builder("""
                DATA;
                #1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P10',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('P01',(0.0,2.0,0.0));
                #4=CARTESIAN_POINT('P11',(2.0,2.0,1.0));
                #20=VERTEX_POINT('V0',#1);
                #21=VERTEX_POINT('V1',#2);
                #22=VERTEX_POINT('V2',#4);
                #23=VERTEX_POINT('V3',#3);
                #30=DIRECTION('D1',(1.0,0.0,0.0));
                #31=VECTOR('VE1',#30,2.0);
                #32=LINE('L1',#1,#31);
                #33=DIRECTION('D2',(0.0,2.0,1.0));
                #34=VECTOR('VE2',#33,1.0);
                #35=LINE('L2',#2,#34);
                #36=DIRECTION('D3',(-2.0,0.0,-1.0));
                #37=VECTOR('VE3',#36,1.0);
                #38=LINE('L3',#4,#37);
                #39=DIRECTION('D4',(0.0,-2.0,0.0));
                #40=VECTOR('VE4',#39,1.0);
                #41=LINE('L4',#3,#40);
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
                #80=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.));
                #81=ADVANCED_FACE('BS_PATCH',(#71),#80,.T.);
                ENDSEC;
                """);

        UnsupportedGeometryException exception = assertThrows(
                UnsupportedGeometryException.class,
                () -> builder.buildFace(81)
        );

        assertEquals("ADVANCED_FACE construction for B_SPLINE_SURFACE_WITH_KNOTS is unsupported", exception.getMessage());
    }

    @Test
    void shouldRejectToroidalFaceConstruction() {
        StepCadBuilder builder = builder("""
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=TOROIDAL_SURFACE('T0',#4,5.0,1.0);
                #10=EDGE_LOOP('L0',());
                #11=FACE_OUTER_BOUND('B0',#10,.T.);
                #12=ADVANCED_FACE('F0',(#11),#5,.T.);
                ENDSEC;
                """);

        UnsupportedGeometryException exception = assertThrows(
                UnsupportedGeometryException.class,
                () -> builder.buildFace(12)
        );

        assertEquals("ADVANCED_FACE construction for TOROIDAL_SURFACE is unsupported", exception.getMessage());
    }

    private static StepCadBuilder builder(String step) {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        return StepCadBuilder.fromResolved(resolved);
    }
}
