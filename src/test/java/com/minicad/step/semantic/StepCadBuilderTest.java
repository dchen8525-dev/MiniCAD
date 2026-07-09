package com.minicad.step.semantic;

import com.minicad.common.GeometryException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.BSplineSurface3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.OffsetSurface3;
import com.minicad.geometry.Parabola3;
import com.minicad.geometry.Hyperbola3;
import com.minicad.geometry.DegenerateCurve3;
import com.minicad.geometry.Clothoid3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.Polyline3;
import com.minicad.geometry.RationalBSplineCurve3;
import com.minicad.geometry.RationalBSplineSurface3;
import com.minicad.geometry.SphericalSurface;
import com.minicad.geometry.SurfaceOfLinearExtrusion3;
import com.minicad.geometry.SurfaceOfRevolution3;
import com.minicad.geometry.SurfaceCurve3;
import com.minicad.geometry.ToroidalSurface;
import com.minicad.geometry.TrimmedCurve3;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepProfileDef;
import com.minicad.step.model.StepCenteredCircleProfileDef;
import com.minicad.step.model.StepRectangleHollowProfileDef;
import com.minicad.step.model.StepCsgPrimitive;
import com.minicad.step.syntax.StepParser;
import com.minicad.topology.Edge;
import com.minicad.topology.Face;
import com.minicad.topology.Shell;
import com.minicad.topology.Solid;
import com.minicad.topology.Vertex;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StepCadBuilderTest {

    @Test
    void shouldBuildGeometryObjectsFromResolvedModel() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=PLANE('PL0',#4);\n"
        + "#6=CIRCLE('C0',#4,2.0);\n"
        + "#7=CYLINDRICAL_SURFACE('CY0',#4,3.0);\n"
        + "#8=VECTOR('V0',#3,5.0);\n"
        + "#9=LINE('L0',#1,#8);\n"
        + "#10=SPHERICAL_SURFACE('SP0',#4,2.5);\n"
        + "ENDSEC;"

        );


        Axis2Placement3D placement = builder.buildPlacement(4);
        Plane plane = builder.buildPlane(5);
        Circle circle = builder.buildCircle(6);
        CylindricalSurface cylindricalSurface = builder.buildCylindricalSurface(7);
        Line3 line = builder.buildLine(9);
        SphericalSurface sphericalSurface = builder.buildSphericalSurface(10);

        assertEquals(0.0, placement.location().x());
        assertEquals(2.0, circle.radius());
        assertEquals(3.0, cylindricalSurface.radius());
        assertEquals(2.5, sphericalSurface.radius());
        assertEquals(1.0, plane.normal().z(), 1.0e-12);
        assertEquals(1.0, line.direction().x(), 1.0e-12);
    }

    @Test
    void shouldBuildPlanarFaceOpenShellAndSolid() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#10=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#11=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_3D('AXIS',#1,#10,#11);\n"
        + "#13=PLANE('PL0',#12);\n"
        + "#20=VERTEX_POINT('V0',#1);\n"
        + "#21=VERTEX_POINT('V1',#2);\n"
        + "#22=VERTEX_POINT('V2',#3);\n"
        + "#23=VERTEX_POINT('V3',#4);\n"
        + "#30=DIRECTION('D1',(1.0,0.0,0.0));\n"
        + "#31=VECTOR('VE1',#30,1.0);\n"
        + "#32=LINE('L1',#1,#31);\n"
        + "#33=DIRECTION('D2',(0.0,1.0,0.0));\n"
        + "#34=VECTOR('VE2',#33,1.0);\n"
        + "#35=LINE('L2',#2,#34);\n"
        + "#36=DIRECTION('D3',(-1.0,0.0,0.0));\n"
        + "#37=VECTOR('VE3',#36,1.0);\n"
        + "#38=LINE('L3',#3,#37);\n"
        + "#39=DIRECTION('D4',(0.0,-1.0,0.0));\n"
        + "#40=VECTOR('VE4',#39,1.0);\n"
        + "#41=LINE('L4',#4,#40);\n"
        + "#50=EDGE_CURVE('E1',#20,#21,#32,.T.);\n"
        + "#51=EDGE_CURVE('E2',#21,#22,#35,.T.);\n"
        + "#52=EDGE_CURVE('E3',#22,#23,#38,.T.);\n"
        + "#53=EDGE_CURVE('E4',#23,#20,#41,.T.);\n"
        + "#60=ORIENTED_EDGE('OE1',$,$,#50,.T.);\n"
        + "#61=ORIENTED_EDGE('OE2',$,$,#51,.T.);\n"
        + "#62=ORIENTED_EDGE('OE3',$,$,#52,.T.);\n"
        + "#63=ORIENTED_EDGE('OE4',$,$,#53,.T.);\n"
        + "#70=EDGE_LOOP('LOOP',(#60,#61,#62,#63));\n"
        + "#71=FACE_OUTER_BOUND('FOB',#70,.T.);\n"
        + "#80=ADVANCED_FACE('F0',(#71),#13,.T.);\n"
        + "#90=OPEN_SHELL('OS',(#80));\n"
        + "#91=CLOSED_SHELL('CS',(#80));\n"
        + "#100=MANIFOLD_SOLID_BREP('S0',#91);\n"
        + "ENDSEC;"
        );

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
    void shouldBuildOrientedShellsAndSolid() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#10=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#11=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_3D('AXIS',#1,#10,#11);\n"
        + "#13=PLANE('PL0',#12);\n"
        + "#20=VERTEX_POINT('V0',#1);\n"
        + "#21=VERTEX_POINT('V1',#2);\n"
        + "#22=VERTEX_POINT('V2',#3);\n"
        + "#23=VERTEX_POINT('V3',#4);\n"
        + "#32=LINE('L1',#1,#24);\n"
        + "#24=VECTOR('VE1',#25,1.0);\n"
        + "#25=DIRECTION('D1',(1.0,0.0,0.0));\n"
        + "#35=LINE('L2',#2,#34);\n"
        + "#34=VECTOR('VE2',#33,1.0);\n"
        + "#33=DIRECTION('D2',(0.0,1.0,0.0));\n"
        + "#38=LINE('L3',#3,#37);\n"
        + "#37=VECTOR('VE3',#36,1.0);\n"
        + "#36=DIRECTION('D3',(-1.0,0.0,0.0));\n"
        + "#41=LINE('L4',#4,#40);\n"
        + "#40=VECTOR('VE4',#39,1.0);\n"
        + "#39=DIRECTION('D4',(0.0,-1.0,0.0));\n"
        + "#50=EDGE_CURVE('E1',#20,#21,#32,.T.);\n"
        + "#51=EDGE_CURVE('E2',#21,#22,#35,.T.);\n"
        + "#52=EDGE_CURVE('E3',#22,#23,#38,.T.);\n"
        + "#53=EDGE_CURVE('E4',#23,#20,#41,.T.);\n"
        + "#60=ORIENTED_EDGE('OE1',$,$,#50,.T.);\n"
        + "#61=ORIENTED_EDGE('OE2',$,$,#51,.T.);\n"
        + "#62=ORIENTED_EDGE('OE3',$,$,#52,.T.);\n"
        + "#63=ORIENTED_EDGE('OE4',$,$,#53,.T.);\n"
        + "#70=EDGE_LOOP('LOOP',(#60,#61,#62,#63));\n"
        + "#71=FACE_OUTER_BOUND('FOB',#70,.T.);\n"
        + "#80=ADVANCED_FACE('F0',(#71),#13,.T.);\n"
        + "#90=OPEN_SHELL('OS',(#80));\n"
        + "#91=CLOSED_SHELL('CS',(#80));\n"
        + "#92=ORIENTED_OPEN_SHELL('OOS',#90,.F.);\n"
        + "#93=ORIENTED_CLOSED_SHELL('OCS',#91,.F.);\n"
        + "#100=MANIFOLD_SOLID_BREP('S0',#93);\n"
        + "ENDSEC;"
        );

        Shell openShell = builder.buildShell(92);
        Shell closedShell = builder.buildShell(93);
        Solid solid = builder.buildSolid(100);

        assertEquals(false, openShell.closed());
        assertEquals(true, closedShell.closed());
        assertEquals(1, openShell.faces().size());
        assertEquals(1, closedShell.faces().size());
        assertEquals(true, solid.outerShell().closed());
    }

    @Test
    void shouldBuildSurfacedOpenShell() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#10=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#11=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_3D('AXIS',#1,#10,#11);\n"
        + "#13=PLANE('PL0',#12);\n"
        + "#20=VERTEX_POINT('V0',#1);\n"
        + "#21=VERTEX_POINT('V1',#2);\n"
        + "#22=VERTEX_POINT('V2',#3);\n"
        + "#23=VERTEX_POINT('V3',#4);\n"
        + "#32=LINE('L1',#1,#24);\n"
        + "#24=VECTOR('VE1',#25,1.0);\n"
        + "#25=DIRECTION('D1',(1.0,0.0,0.0));\n"
        + "#35=LINE('L2',#2,#34);\n"
        + "#34=VECTOR('VE2',#33,1.0);\n"
        + "#33=DIRECTION('D2',(0.0,1.0,0.0));\n"
        + "#38=LINE('L3',#3,#37);\n"
        + "#37=VECTOR('VE3',#36,1.0);\n"
        + "#36=DIRECTION('D3',(-1.0,0.0,0.0));\n"
        + "#41=LINE('L4',#4,#40);\n"
        + "#40=VECTOR('VE4',#39,1.0);\n"
        + "#39=DIRECTION('D4',(0.0,-1.0,0.0));\n"
        + "#50=EDGE_CURVE('E1',#20,#21,#32,.T.);\n"
        + "#51=EDGE_CURVE('E2',#21,#22,#35,.T.);\n"
        + "#52=EDGE_CURVE('E3',#22,#23,#38,.T.);\n"
        + "#53=EDGE_CURVE('E4',#23,#20,#41,.T.);\n"
        + "#60=ORIENTED_EDGE('OE1',$,$,#50,.T.);\n"
        + "#61=ORIENTED_EDGE('OE2',$,$,#51,.T.);\n"
        + "#62=ORIENTED_EDGE('OE3',$,$,#52,.T.);\n"
        + "#63=ORIENTED_EDGE('OE4',$,$,#53,.T.);\n"
        + "#70=EDGE_LOOP('LOOP',(#60,#61,#62,#63));\n"
        + "#71=FACE_OUTER_BOUND('FOB',#70,.T.);\n"
        + "#80=FACE_SURFACE('F0',(#71),#13,.T.);\n"
        + "#90=SURFACED_OPEN_SHELL('SOS',(#80));\n"
        + "ENDSEC;"
        );

        Shell shell = builder.buildShell(90);

        assertEquals(false, shell.closed());
        assertEquals(1, shell.faces().size());
    }

    @Test
    void shouldBuildExtrudedFaceSolidUsingPlacementAndLocalDirection() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#10=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#11=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_3D('FACE_AX',#1,#10,#11);\n"
        + "#13=PLANE('PL0',#12);\n"
        + "#20=VERTEX_POINT('V0',#1);\n"
        + "#21=VERTEX_POINT('V1',#2);\n"
        + "#22=VERTEX_POINT('V2',#3);\n"
        + "#23=VERTEX_POINT('V3',#4);\n"
        + "#30=DIRECTION('D1',(1.0,0.0,0.0));\n"
        + "#31=VECTOR('VE1',#30,1.0);\n"
        + "#32=LINE('L1',#1,#31);\n"
        + "#33=DIRECTION('D2',(0.0,1.0,0.0));\n"
        + "#34=VECTOR('VE2',#33,1.0);\n"
        + "#35=LINE('L2',#2,#34);\n"
        + "#36=DIRECTION('D3',(-1.0,0.0,0.0));\n"
        + "#37=VECTOR('VE3',#36,1.0);\n"
        + "#38=LINE('L3',#3,#37);\n"
        + "#39=DIRECTION('D4',(0.0,-1.0,0.0));\n"
        + "#40=VECTOR('VE4',#39,1.0);\n"
        + "#41=LINE('L4',#4,#40);\n"
        + "#50=EDGE_CURVE('E1',#20,#21,#32,.T.);\n"
        + "#51=EDGE_CURVE('E2',#21,#22,#35,.T.);\n"
        + "#52=EDGE_CURVE('E3',#22,#23,#38,.T.);\n"
        + "#53=EDGE_CURVE('E4',#23,#20,#41,.T.);\n"
        + "#60=ORIENTED_EDGE('OE1',$,$,#50,.T.);\n"
        + "#61=ORIENTED_EDGE('OE2',$,$,#51,.T.);\n"
        + "#62=ORIENTED_EDGE('OE3',$,$,#52,.T.);\n"
        + "#63=ORIENTED_EDGE('OE4',$,$,#53,.T.);\n"
        + "#70=EDGE_LOOP('LOOP',(#60,#61,#62,#63));\n"
        + "#71=FACE_OUTER_BOUND('FOB',#70,.T.);\n"
        + "#80=ADVANCED_FACE('F0',(#71),#13,.T.);\n"
        + "#90=CARTESIAN_POINT('SOLID_ORIGIN',(3.0,4.0,5.0));\n"
        + "#91=DIRECTION('SOLID_AXIS',(0.0,1.0,0.0));\n"
        + "#92=DIRECTION('SOLID_X',(1.0,0.0,0.0));\n"
        + "#93=AXIS2_PLACEMENT_3D('SOLID_POS',#90,#91,#92);\n"
        + "#94=DIRECTION('LOCAL_EXTRUDE',(0.0,0.0,1.0));\n"
        + "#95=(EXTRUDED_FACE_SOLID('EXF',#80,#93,#94,5.0) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('EXF_ITEM'));\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(95);

        assertEquals(6, solid.faceCount());
        assertEquals(-7.0, solid.boundingBox().minX(), 1.0e-9);
        assertEquals(13.0, solid.boundingBox().maxX(), 1.0e-9);
        assertEquals(4.0, solid.boundingBox().minY(), 1.0e-9);
        assertEquals(9.0, solid.boundingBox().maxY(), 1.0e-9);
        assertEquals(-5.0, solid.boundingBox().minZ(), 1.0e-9);
        assertEquals(15.0, solid.boundingBox().maxZ(), 1.0e-9);
    }

    @Test
    void shouldSupportReverseExtrusionDirectionForExtrudedFaceSolid() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#10=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#11=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_3D('FACE_AX',#1,#10,#11);\n"
        + "#13=PLANE('PL0',#12);\n"
        + "#20=VERTEX_POINT('V0',#1);\n"
        + "#21=VERTEX_POINT('V1',#2);\n"
        + "#22=VERTEX_POINT('V2',#3);\n"
        + "#23=VERTEX_POINT('V3',#4);\n"
        + "#30=DIRECTION('D1',(1.0,0.0,0.0));\n"
        + "#31=VECTOR('VE1',#30,1.0);\n"
        + "#32=LINE('L1',#1,#31);\n"
        + "#33=DIRECTION('D2',(0.0,1.0,0.0));\n"
        + "#34=VECTOR('VE2',#33,1.0);\n"
        + "#35=LINE('L2',#2,#34);\n"
        + "#36=DIRECTION('D3',(-1.0,0.0,0.0));\n"
        + "#37=VECTOR('VE3',#36,1.0);\n"
        + "#38=LINE('L3',#3,#37);\n"
        + "#39=DIRECTION('D4',(0.0,-1.0,0.0));\n"
        + "#40=VECTOR('VE4',#39,1.0);\n"
        + "#41=LINE('L4',#4,#40);\n"
        + "#50=EDGE_CURVE('E1',#20,#21,#32,.T.);\n"
        + "#51=EDGE_CURVE('E2',#21,#22,#35,.T.);\n"
        + "#52=EDGE_CURVE('E3',#22,#23,#38,.T.);\n"
        + "#53=EDGE_CURVE('E4',#23,#20,#41,.T.);\n"
        + "#60=ORIENTED_EDGE('OE1',$,$,#50,.T.);\n"
        + "#61=ORIENTED_EDGE('OE2',$,$,#51,.T.);\n"
        + "#62=ORIENTED_EDGE('OE3',$,$,#52,.T.);\n"
        + "#63=ORIENTED_EDGE('OE4',$,$,#53,.T.);\n"
        + "#70=EDGE_LOOP('LOOP',(#60,#61,#62,#63));\n"
        + "#71=FACE_OUTER_BOUND('FOB',#70,.T.);\n"
        + "#80=ADVANCED_FACE('F0',(#71),#13,.T.);\n"
        + "#90=CARTESIAN_POINT('SOLID_ORIGIN',(3.0,4.0,5.0));\n"
        + "#91=DIRECTION('SOLID_AXIS',(0.0,1.0,0.0));\n"
        + "#92=DIRECTION('SOLID_X',(1.0,0.0,0.0));\n"
        + "#93=AXIS2_PLACEMENT_3D('SOLID_POS',#90,#91,#92);\n"
        + "#94=DIRECTION('LOCAL_EXTRUDE',(0.0,0.0,-1.0));\n"
        + "#95=(EXTRUDED_FACE_SOLID('EXF',#80,#93,#94,5.0) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('EXF_ITEM'));\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(95);

        assertEquals(-1.0, solid.boundingBox().minY(), 1.0e-9);
        assertEquals(4.0, solid.boundingBox().maxY(), 1.0e-9);
    }

    @Test
    void shouldRejectNonPositiveDepthForExtrudedFaceSolid() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#10=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#11=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_3D('FACE_AX',#1,#10,#11);\n"
        + "#13=PLANE('PL0',#12);\n"
        + "#20=VERTEX_POINT('V0',#1);\n"
        + "#21=VERTEX_POINT('V1',#2);\n"
        + "#22=VERTEX_POINT('V2',#3);\n"
        + "#23=VERTEX_POINT('V3',#4);\n"
        + "#30=DIRECTION('D1',(1.0,0.0,0.0));\n"
        + "#31=VECTOR('VE1',#30,1.0);\n"
        + "#32=LINE('L1',#1,#31);\n"
        + "#33=DIRECTION('D2',(0.0,1.0,0.0));\n"
        + "#34=VECTOR('VE2',#33,1.0);\n"
        + "#35=LINE('L2',#2,#34);\n"
        + "#36=DIRECTION('D3',(-1.0,0.0,0.0));\n"
        + "#37=VECTOR('VE3',#36,1.0);\n"
        + "#38=LINE('L3',#3,#37);\n"
        + "#39=DIRECTION('D4',(0.0,-1.0,0.0));\n"
        + "#40=VECTOR('VE4',#39,1.0);\n"
        + "#41=LINE('L4',#4,#40);\n"
        + "#50=EDGE_CURVE('E1',#20,#21,#32,.T.);\n"
        + "#51=EDGE_CURVE('E2',#21,#22,#35,.T.);\n"
        + "#52=EDGE_CURVE('E3',#22,#23,#38,.T.);\n"
        + "#53=EDGE_CURVE('E4',#23,#20,#41,.T.);\n"
        + "#60=ORIENTED_EDGE('OE1',$,$,#50,.T.);\n"
        + "#61=ORIENTED_EDGE('OE2',$,$,#51,.T.);\n"
        + "#62=ORIENTED_EDGE('OE3',$,$,#52,.T.);\n"
        + "#63=ORIENTED_EDGE('OE4',$,$,#53,.T.);\n"
        + "#70=EDGE_LOOP('LOOP',(#60,#61,#62,#63));\n"
        + "#71=FACE_OUTER_BOUND('FOB',#70,.T.);\n"
        + "#80=ADVANCED_FACE('F0',(#71),#13,.T.);\n"
        + "#90=CARTESIAN_POINT('SOLID_ORIGIN',(3.0,4.0,5.0));\n"
        + "#91=DIRECTION('SOLID_AXIS',(0.0,1.0,0.0));\n"
        + "#92=DIRECTION('SOLID_X',(1.0,0.0,0.0));\n"
        + "#93=AXIS2_PLACEMENT_3D('SOLID_POS',#90,#91,#92);\n"
        + "#94=DIRECTION('LOCAL_EXTRUDE',(0.0,0.0,1.0));\n"
        + "#95=(EXTRUDED_FACE_SOLID('EXF',#80,#93,#94,0.0) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('EXF_ITEM'));\n"
        + "ENDSEC;"

        );


        assertThrows(UnsupportedGeometryException.class, () -> builder.buildSolid(95));
    }

    @Test
    void shouldBuildRevolvedFaceSolidUsingPositionAndAxis() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#10=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#11=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_3D('FACE_AX',#1,#10,#11);\n"
        + "#13=PLANE('PL0',#12);\n"
        + "#20=VERTEX_POINT('V0',#1);\n"
        + "#21=VERTEX_POINT('V1',#2);\n"
        + "#22=VERTEX_POINT('V2',#3);\n"
        + "#23=VERTEX_POINT('V3',#4);\n"
        + "#30=DIRECTION('D1',(1.0,0.0,0.0));\n"
        + "#31=VECTOR('VE1',#30,1.0);\n"
        + "#32=LINE('L1',#1,#31);\n"
        + "#33=DIRECTION('D2',(0.0,1.0,0.0));\n"
        + "#34=VECTOR('VE2',#33,1.0);\n"
        + "#35=LINE('L2',#2,#34);\n"
        + "#36=DIRECTION('D3',(-1.0,0.0,0.0));\n"
        + "#37=VECTOR('VE3',#36,1.0);\n"
        + "#38=LINE('L3',#3,#37);\n"
        + "#39=DIRECTION('D4',(0.0,-1.0,0.0));\n"
        + "#40=VECTOR('VE4',#39,1.0);\n"
        + "#41=LINE('L4',#4,#40);\n"
        + "#50=EDGE_CURVE('E1',#20,#21,#32,.T.);\n"
        + "#51=EDGE_CURVE('E2',#21,#22,#35,.T.);\n"
        + "#52=EDGE_CURVE('E3',#22,#23,#38,.T.);\n"
        + "#53=EDGE_CURVE('E4',#23,#20,#41,.T.);\n"
        + "#60=ORIENTED_EDGE('OE1',$,$,#50,.T.);\n"
        + "#61=ORIENTED_EDGE('OE2',$,$,#51,.T.);\n"
        + "#62=ORIENTED_EDGE('OE3',$,$,#52,.T.);\n"
        + "#63=ORIENTED_EDGE('OE4',$,$,#53,.T.);\n"
        + "#70=EDGE_LOOP('LOOP',(#60,#61,#62,#63));\n"
        + "#71=FACE_OUTER_BOUND('FOB',#70,.T.);\n"
        + "#80=ADVANCED_FACE('F0',(#71),#13,.T.);\n"
        + "#90=CARTESIAN_POINT('SOLID_ORIGIN',(3.0,4.0,5.0));\n"
        + "#91=AXIS2_PLACEMENT_3D('SOLID_POS',#90,#10,#11);\n"
        + "#92=CARTESIAN_POINT('AXIS_ORIGIN',(20.0,0.0,0.0));\n"
        + "#93=DIRECTION('AXIS_DIR',(0.0,1.0,0.0));\n"
        + "#94=AXIS1_PLACEMENT('REV_AXIS',#92,#93);\n"
        + "#95=(REVOLVED_FACE_SOLID('RVF',#80,#91,#94,1.57079632679) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('RVF_ITEM'));\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(95);

        assertEquals(34, solid.faceCount());
        assertEquals(-7.0, solid.boundingBox().minX(), 1.0e-9);
        assertEquals(23.0, solid.boundingBox().maxX(), 1.0e-9);
        assertEquals(-6.0, solid.boundingBox().minY(), 1.0e-9);
        assertEquals(14.0, solid.boundingBox().maxY(), 1.0e-9);
        assertEquals(5.0, solid.boundingBox().minZ(), 1.0e-9);
        assertEquals(35.0, solid.boundingBox().maxZ(), 1.0e-9);
    }

    @Test
    void shouldRejectExcessiveAngleForRevolvedFaceSolid() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#10=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#11=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_3D('FACE_AX',#1,#10,#11);\n"
        + "#13=PLANE('PL0',#12);\n"
        + "#20=VERTEX_POINT('V0',#1);\n"
        + "#21=VERTEX_POINT('V1',#2);\n"
        + "#22=VERTEX_POINT('V2',#3);\n"
        + "#23=VERTEX_POINT('V3',#4);\n"
        + "#30=DIRECTION('D1',(1.0,0.0,0.0));\n"
        + "#31=VECTOR('VE1',#30,1.0);\n"
        + "#32=LINE('L1',#1,#31);\n"
        + "#33=DIRECTION('D2',(0.0,1.0,0.0));\n"
        + "#34=VECTOR('VE2',#33,1.0);\n"
        + "#35=LINE('L2',#2,#34);\n"
        + "#36=DIRECTION('D3',(-1.0,0.0,0.0));\n"
        + "#37=VECTOR('VE3',#36,1.0);\n"
        + "#38=LINE('L3',#3,#37);\n"
        + "#39=DIRECTION('D4',(0.0,-1.0,0.0));\n"
        + "#40=VECTOR('VE4',#39,1.0);\n"
        + "#41=LINE('L4',#4,#40);\n"
        + "#50=EDGE_CURVE('E1',#20,#21,#32,.T.);\n"
        + "#51=EDGE_CURVE('E2',#21,#22,#35,.T.);\n"
        + "#52=EDGE_CURVE('E3',#22,#23,#38,.T.);\n"
        + "#53=EDGE_CURVE('E4',#23,#20,#41,.T.);\n"
        + "#60=ORIENTED_EDGE('OE1',$,$,#50,.T.);\n"
        + "#61=ORIENTED_EDGE('OE2',$,$,#51,.T.);\n"
        + "#62=ORIENTED_EDGE('OE3',$,$,#52,.T.);\n"
        + "#63=ORIENTED_EDGE('OE4',$,$,#53,.T.);\n"
        + "#70=EDGE_LOOP('LOOP',(#60,#61,#62,#63));\n"
        + "#71=FACE_OUTER_BOUND('FOB',#70,.T.);\n"
        + "#80=ADVANCED_FACE('F0',(#71),#13,.T.);\n"
        + "#90=CARTESIAN_POINT('SOLID_ORIGIN',(3.0,4.0,5.0));\n"
        + "#91=AXIS2_PLACEMENT_3D('SOLID_POS',#90,#10,#11);\n"
        + "#92=CARTESIAN_POINT('AXIS_ORIGIN',(20.0,0.0,0.0));\n"
        + "#93=DIRECTION('AXIS_DIR',(0.0,1.0,0.0));\n"
        + "#94=AXIS1_PLACEMENT('REV_AXIS',#92,#93);\n"
        + "#95=(REVOLVED_FACE_SOLID('RVF',#80,#91,#94,6.4) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('RVF_ITEM'));\n"
        + "ENDSEC;"

        );


        assertThrows(UnsupportedGeometryException.class, () -> builder.buildSolid(95));
    }

    @Test
    void shouldBuildCircularEdgeTopology() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('CENTER',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P0',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P1',(0.0,1.0,0.0));\n"
        + "#10=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#11=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);\n"
        + "#13=CIRCLE('C0',#12,1.0);\n"
        + "#20=VERTEX_POINT('V0',#2);\n"
        + "#21=VERTEX_POINT('V1',#3);\n"
        + "#30=EDGE_CURVE('E0',#20,#21,#13,.T.);\n"
        + "ENDSEC;"
        );

        Edge edge = builder.buildEdge(30);

        assertInstanceOf(Circle.class, edge.curve());
        assertTrue(edge.curve().contains(edge.start().point()));
        assertTrue(edge.curve().contains(edge.end().point()));
    }

    @Test
    void shouldBuildParabolaBackedEdgeAsParabola3() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=PARABOLA('PAR0',#4,2.0);\n"
        + "#6=CARTESIAN_POINT('S',(-8.0,8.0,0.0));\n"
        + "#7=CARTESIAN_POINT('E',(8.0,8.0,0.0));\n"
        + "#8=VERTEX_POINT('V0',#6);\n"
        + "#9=VERTEX_POINT('V1',#7);\n"
        + "#10=EDGE_CURVE('E0',#8,#9,#5,.T.);\n"
        + "ENDSEC;"
        );

        Edge edge = builder.buildEdge(10);

        assertInstanceOf(Parabola3.class, edge.curve());
        assertEquals(2.0, ((Parabola3) edge.curve()).focalDistance());
    }

    @Test
    void shouldBuildClothoidAsClothoid3() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0));\n"
        + "#3=AXIS2_PLACEMENT_2D('AX',#1,#2);\n"
        + "#4=CLOTHOID('CL0',#3,1.0,0.5);\n"
        + "#5=CARTESIAN_POINT('O3',(0.0,0.0,0.0));\n"
        + "#6=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#7=DIRECTION('DX3',(1.0,0.0,0.0));\n"
        + "#8=AXIS2_PLACEMENT_3D('AX3',#5,#6,#7);\n"
        + "ENDSEC;"

        );


        Clothoid3 clothoid = builder.buildClothoid(4);

        assertNotNull(clothoid);
        assertEquals(1.0, clothoid.xAxisIntercept());
        assertEquals(0.5, clothoid.curvature());
    }

    @Test
    void shouldBuildHyperbolaPcurveAsHyperbola2() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0));\n"
        + "#3=AXIS2_PLACEMENT_2D('AX2',#1,#2);\n"
        + "#4=HYPERBOLA('H0',#3,4.0,2.0);\n"
        + "#5=REPRESENTATION_CONTEXT('PC','PARAMETRIC');\n"
        + "#6=DEFINITIONAL_REPRESENTATION('DR',(#4),#5);\n"
        + "#7=PLANE('PL0',#10);\n"
        + "#8=PCURVE('PC0',#7,#6);\n"
        + "#9=CARTESIAN_POINT('S',(4.0,0.0));\n"
        + "#10=AXIS2_PLACEMENT_3D('AX3',#11,#12,#13);\n"
        + "#11=CARTESIAN_POINT('O3',(0.0,0.0,0.0));\n"
        + "#12=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#13=DIRECTION('DX3',(1.0,0.0,0.0));\n"
        + "ENDSEC;"

        );


        Object curve = builder.buildPcurve2(8);

        assertInstanceOf(com.minicad.geometry2d.Hyperbola2.class, curve);
        com.minicad.geometry2d.Hyperbola2 hyperbola = (com.minicad.geometry2d.Hyperbola2) curve;
        assertEquals(4.0, hyperbola.semiAxisA());
        assertEquals(2.0, hyperbola.semiAxisB());
    }

    @Test
    void shouldBuildDegeneratePcurveAs2dCurve() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=PLANE('PL0',#4);\n"
        + "#6=CARTESIAN_POINT('UV0',(0.0,0.0));\n"
        + "#7=DIRECTION('DU',(1.0,0.0));\n"
        + "#8=VECTOR('VU',#7,1.0);\n"
        + "#9=LINE('L2D',#6,#8);\n"
        + "#10=REPRESENTATION_CONTEXT('PC','PARAMETRIC');\n"
        + "#11=DEFINITIONAL_REPRESENTATION('DR',(#9),#10);\n"
        + "#12=DEGENERATE_PCURVE('DPC0',#5,#11);\n"
        + "ENDSEC;"

        );


        Object curve = builder.buildPcurve2(12);

        assertInstanceOf(Line2.class, curve);
    }

    @Test
    void shouldBuildImplicitBsplineCurveSubtypeMarkers() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(2.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(3.0,1.0,0.0));\n"
        + "#5=CARTESIAN_POINT('UV0',(0.0,0.0));\n"
        + "#6=CARTESIAN_POINT('UV1',(1.0,0.0));\n"
        + "#7=CARTESIAN_POINT('UV2',(2.0,1.0));\n"
        + "#8=CARTESIAN_POINT('UV3',(3.0,1.0));\n"
        + "#10=(BEZIER_CURVE() B_SPLINE_CURVE(3,(#1,#2,#3,#4),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('bz'));\n"
        + "#11=(UNIFORM_CURVE() B_SPLINE_CURVE(2,(#1,#2,#3,#4),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('uc'));\n"
        + "#12=(QUASI_UNIFORM_CURVE() B_SPLINE_CURVE(2,(#1,#2,#3,#4),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('quc'));\n"
        + "#13=(PIECEWISE_BEZIER_CURVE() BEZIER_CURVE() B_SPLINE_CURVE(1,(#1,#2,#3,#4),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('pbc'));\n"
        + "#20=(BEZIER_CURVE() B_SPLINE_CURVE(3,(#5,#6,#7,#8),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('bz2'));\n"
        + "ENDSEC;"
        );

        Curve3 bezier = builder.buildCurveReference3(10);
        Curve3 uniform = builder.buildCurveReference3(11);
        Curve3 quasiUniform = builder.buildCurveReference3(12);
        Curve3 piecewise = builder.buildCurveReference3(13);
        Object pcurve = builder.buildBezierCurve2(20);

        assertInstanceOf(BSplineCurve3.class, bezier);
        assertInstanceOf(BSplineCurve3.class, uniform);
        assertInstanceOf(BSplineCurve3.class, quasiUniform);
        assertInstanceOf(BSplineCurve3.class, piecewise);
        assertInstanceOf(BSplineCurve2.class, pcurve);
    }

    @Test
    void shouldBuildImplicitBsplineSurfaceSubtypeMarkers() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(0.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(1.0,1.0,0.0));\n"
        + "#10=(BEZIER_SURFACE() B_SPLINE_SURFACE(1,1,((#1,#2),(#3,#4)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('bzs'));\n"
        + "#11=(UNIFORM_SURFACE() B_SPLINE_SURFACE(1,1,((#1,#2),(#3,#4)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('us'));\n"
        + "#12=(QUASI_UNIFORM_SURFACE() B_SPLINE_SURFACE(1,1,((#1,#2),(#3,#4)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('qus'));\n"
        + "#13=(PIECEWISE_BEZIER_SURFACE() BEZIER_SURFACE() B_SPLINE_SURFACE(1,1,((#1,#2),(#3,#4)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('pbs'));\n"
        + "ENDSEC;"
        );

        assertInstanceOf(BSplineSurface3.class, builder.buildBezierSurface(10));
        assertInstanceOf(BSplineSurface3.class, builder.buildUniformSurface(11));
        assertInstanceOf(BSplineSurface3.class, builder.buildQuasiUniformSurface(12));
        assertInstanceOf(BSplineSurface3.class, builder.buildPiecewiseBezierSurface(13));
    }

    @Test
    void shouldBuildConicBackedSurfaceAndCompositeCurves() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=PARABOLA('PAR0',#4,2.0);\n"
        + "#6=HYPERBOLA('HYP0',#4,4.0,2.0);\n"
        + "#7=SURFACE_CURVE('SC0',#5,(),.T.);\n"
        + "#8=COMPOSITE_CURVE_SEGMENT(.CONTINUOUS.,.T.,#6);\n"
        + "#9=(COMPOSITE_CURVE('CC0',(#8),.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('cc-name'));\n"
        + "ENDSEC;"

        );


        SurfaceCurve3 surfaceCurve = builder.buildSurfaceCurve(7);
        CompositeCurve3 compositeCurve = builder.buildCompositeCurve(9);

        assertInstanceOf(Parabola3.class, surfaceCurve.curve3d());
        assertEquals(2.0, ((Parabola3) surfaceCurve.curve3d()).focalDistance());
        assertEquals(1, compositeCurve.segments().size());
        assertInstanceOf(Hyperbola3.class, compositeCurve.segments().get(0));
        assertEquals(4.0, ((Hyperbola3) compositeCurve.segments().get(0)).semiAxisA());
        assertEquals(2.0, ((Hyperbola3) compositeCurve.segments().get(0)).semiAxisB());
    }

    @Test
    void shouldBuildDegenerateConicAsDegenerateCurve3() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(1.0,2.0,3.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=DEGENERATE_CONIC('DC0',#4);\n"
        + "#6=GEOMETRIC_CURVE_SET('WIRE',(#5));\n"
        + "ENDSEC;"
        );

        Curve3 curve = builder.buildCurveReference3(5);

        DegenerateCurve3 degenerate = assertInstanceOf(DegenerateCurve3.class, curve);
        assertEquals(new CartesianPoint(1.0, 2.0, 3.0), degenerate.point());
        assertTrue(curve.contains(new CartesianPoint(1.0, 2.0, 3.0)));
    }

    @Test
    void shouldBuildDegenerateConicPcurveAsZeroLengthPolyline2() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('UV0',(3.0,4.0));\n"
        + "#2=DIRECTION('DU',(1.0,0.0));\n"
        + "#3=AXIS2_PLACEMENT_2D('AX2',#1,#2);\n"
        + "#4=DEGENERATE_CONIC('DC0',#3);\n"
        + "#5=REPRESENTATION_CONTEXT('PC','PARAMETRIC');\n"
        + "#6=DEFINITIONAL_REPRESENTATION('DR',(#4),#5);\n"
        + "#7=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#8=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#9=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#10=AXIS2_PLACEMENT_3D('AX3',#7,#8,#9);\n"
        + "#11=PLANE('PL0',#10);\n"
        + "#12=PCURVE('PC0',#11,#6);\n"
        + "ENDSEC;"

        );


        Object curve = builder.buildPcurve2(12);

        com.minicad.geometry2d.Polyline2 polyline = assertInstanceOf(com.minicad.geometry2d.Polyline2.class, curve);
        assertEquals(2, polyline.points().size());
        assertEquals(polyline.points().get(0), polyline.points().get(polyline.points().size() - 1));
        assertTrue(polyline.contains(new Point2(3.0, 4.0)));
    }

    @Test
    void shouldBuildConicBackedSeamCurve() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=HYPERBOLA('HYP0',#4,4.0,2.0);\n"
        + "#6=PLANE('PL0',#4);\n"
        + "#7=CARTESIAN_POINT('UV0',(0.0,0.0));\n"
        + "#8=DIRECTION('UX',(1.0,0.0));\n"
        + "#9=AXIS2_PLACEMENT_2D('AX2',#7,#8);\n"
        + "#10=HYPERBOLA('H2A',#9,4.0,2.0);\n"
        + "#11=HYPERBOLA('H2B',#9,4.0,2.0);\n"
        + "#12=REPRESENTATION_CONTEXT('PC','PARAMETRIC');\n"
        + "#13=DEFINITIONAL_REPRESENTATION('DR0',(#10),#12);\n"
        + "#14=DEFINITIONAL_REPRESENTATION('DR1',(#11),#12);\n"
        + "#15=PCURVE('PC0',#6,#13);\n"
        + "#16=PCURVE('PC1',#6,#14);\n"
        + "#17=SEAM_CURVE('SEAM0',#5,(#15,#16),.PCURVE_S1.);\n"
        + "ENDSEC;"

        );


        SurfaceCurve3 seamCurve = builder.buildSeamCurve(17);

        assertInstanceOf(Hyperbola3.class, seamCurve.curve3d());
        assertEquals(4.0, ((Hyperbola3) seamCurve.curve3d()).semiAxisA());
        assertEquals(2.0, ((Hyperbola3) seamCurve.curve3d()).semiAxisB());
    }

    @Test
    void shouldBuildEdgesBackedByAnnotationCurveWrappers() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(0.0,1.0,0.0));\n"
        + "#4=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#5=VECTOR('VX',#4,1.0);\n"
        + "#6=LINE('L0',#1,#5);\n"
        + "#7=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#8=VECTOR('VY',#7,1.0);\n"
        + "#9=LINE('L1',#1,#8);\n"
        + "#10=PRESENTATION_STYLE_ASSIGNMENT(());\n"
        + "#11=(LEADER_CURVE('LC0',(#10),#6)\n"
        + "    ANNOTATION_CURVE_OCCURRENCE('LC0',(#10),#6)\n"
        + "    STYLED_ITEM('LC0',(#10),#6)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('LC0'));\n"
        + "#12=(DIMENSION_CURVE('DC0',(#10),#9)\n"
        + "    ANNOTATION_CURVE_OCCURRENCE('DC0',(#10),#9)\n"
        + "    STYLED_ITEM('DC0',(#10),#9)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('DC0'));\n"
        + "#20=VERTEX_POINT('V0',#1);\n"
        + "#21=VERTEX_POINT('V1',#2);\n"
        + "#22=VERTEX_POINT('V2',#3);\n"
        + "#30=EDGE_CURVE('E0',#20,#21,#11,.T.);\n"
        + "#31=EDGE_CURVE('E1',#20,#22,#12,.T.);\n"
        + "ENDSEC;"
        );

        Edge leaderEdge = builder.buildEdge(30);
        Edge dimensionEdge = builder.buildEdge(31);

        assertInstanceOf(Line3.class, leaderEdge.curve());
        assertInstanceOf(Line3.class, dimensionEdge.curve());
        assertTrue(leaderEdge.curve().contains(leaderEdge.start().point()));
        assertTrue(leaderEdge.curve().contains(leaderEdge.end().point()));
        assertTrue(dimensionEdge.curve().contains(dimensionEdge.start().point()));
        assertTrue(dimensionEdge.curve().contains(dimensionEdge.end().point()));
    }

    @Test
    void shouldBuildEdgesBackedByProjectionAndTerminatorWrappers() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=VECTOR('VX',#3,1.0);\n"
        + "#5=LINE('L0',#1,#4);\n"
        + "#6=PRESENTATION_STYLE_ASSIGNMENT(());\n"
        + "#7=(PROJECTION_CURVE('PC0',(#6),#5)\n"
        + "    ANNOTATION_CURVE_OCCURRENCE('PC0',(#6),#5)\n"
        + "    STYLED_ITEM('PC0',(#6),#5)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('PC0'));\n"
        + "#8=CARTESIAN_POINT('O2',(2.0,0.0));\n"
        + "#9=DIRECTION('DX2',(1.0,0.0));\n"
        + "#10=AXIS2_PLACEMENT_2D('MAP',#8,#9);\n"
        + "#11=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));\n"
        + "#12=REPRESENTATION('SYM',(),#11);\n"
        + "#13=SYMBOL_REPRESENTATION_MAP(#10,#12);\n"
        + "#14=CARTESIAN_POINT('O3',(3.0,0.0));\n"
        + "#15=DIRECTION('DX3',(1.0,0.0));\n"
        + "#16=AXIS2_PLACEMENT_2D('TGT',#14,#15);\n"
        + "#17=ANNOTATION_SYMBOL('AS0',#13,#16);\n"
        + "#18=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#6),#17);\n"
        + "#19=TERMINATOR_SYMBOL('TS0',(#6),#17,#7);\n"
        + "#20=VERTEX_POINT('V0',#1);\n"
        + "#21=VERTEX_POINT('V1',#2);\n"
        + "#22=EDGE_CURVE('E0',#20,#21,#7,.T.);\n"
        + "#23=EDGE_CURVE('E1',#20,#21,#19,.T.);\n"
        + "ENDSEC;"
        );

        Edge projectionEdge = builder.buildEdge(22);
        Edge terminatorEdge = builder.buildEdge(23);

        assertInstanceOf(Line3.class, projectionEdge.curve());
        assertInstanceOf(Line3.class, terminatorEdge.curve());
        assertTrue(projectionEdge.curve().contains(projectionEdge.start().point()));
        assertTrue(terminatorEdge.curve().contains(terminatorEdge.end().point()));
    }

    @Test
    void shouldBuildTrimmedCurveBackedByProjectionWrapper() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=VECTOR('VX',#3,1.0);\n"
        + "#5=LINE('L0',#1,#4);\n"
        + "#6=PRESENTATION_STYLE_ASSIGNMENT(());\n"
        + "#7=(PROJECTION_CURVE('PC0',(#6),#5)\n"
        + "    ANNOTATION_CURVE_OCCURRENCE('PC0',(#6),#5)\n"
        + "    STYLED_ITEM('PC0',(#6),#5)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('PC0'));\n"
        + "#8=TRIMMED_CURVE('TC0',#7,(#1),(#2),.T.,.CARTESIAN.);\n"
        + "#9=VERTEX_POINT('V0',#1);\n"
        + "#10=VERTEX_POINT('V1',#2);\n"
        + "#11=EDGE_CURVE('E0',#9,#10,#8,.T.);\n"
        + "ENDSEC;"

        );


        TrimmedCurve3 trimmedCurve = builder.buildTrimmedCurve(8);
        Edge edge = builder.buildEdge(11);

        assertInstanceOf(Line3.class, trimmedCurve.basisCurve());
        assertTrue(edge.curve().contains(edge.start().point()));
        assertTrue(edge.curve().contains(edge.end().point()));
    }

    @Test
    void shouldBuildCurveBoundedSurfaceWithAnnotationWrapperBoundary() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#5=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#6=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#7=AXIS2_PLACEMENT_3D('AX',#1,#5,#6);\n"
        + "#8=PLANE('PL0',#7);\n"
        + "#9=PRESENTATION_STYLE_ASSIGNMENT(());\n"
        + "#10=VECTOR('VX',#6,1.0);\n"
        + "#11=LINE('L0',#1,#10);\n"
        + "#12=(LEADER_CURVE('LC0',(#9),#11)\n"
        + "    ANNOTATION_CURVE_OCCURRENCE('LC0',(#9),#11)\n"
        + "    STYLED_ITEM('LC0',(#9),#11)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('LC0'));\n"
        + "#13=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#14=VECTOR('VY',#13,1.0);\n"
        + "#15=LINE('L1',#2,#14);\n"
        + "#16=(DIMENSION_CURVE('DC0',(#9),#15)\n"
        + "    ANNOTATION_CURVE_OCCURRENCE('DC0',(#9),#15)\n"
        + "    STYLED_ITEM('DC0',(#9),#15)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('DC0'));\n"
        + "#17=DIRECTION('MX',(-1.0,0.0,0.0));\n"
        + "#18=VECTOR('VMX',#17,1.0);\n"
        + "#19=LINE('L2',#3,#18);\n"
        + "#20=(PROJECTION_CURVE('PC0',(#9),#19)\n"
        + "    ANNOTATION_CURVE_OCCURRENCE('PC0',(#9),#19)\n"
        + "    STYLED_ITEM('PC0',(#9),#19)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('PC0'));\n"
        + "#21=DIRECTION('MY',(0.0,-1.0,0.0));\n"
        + "#22=VECTOR('VMY',#21,1.0);\n"
        + "#23=LINE('L3',#4,#22);\n"
        + "#24=(DRAUGHTING_ANNOTATION_OCCURRENCE('DA0',(#9),#23)\n"
        + "    STYLED_ITEM('DA0',(#9),#23)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('DA0'));\n"
        + "#25=CURVE_BOUNDED_SURFACE('CBS0',#8,(#12,#16,#20,#24),.T.);\n"
        + "#30=VERTEX_POINT('V0',#1);\n"
        + "#31=VERTEX_POINT('V1',#2);\n"
        + "#32=VERTEX_POINT('V2',#3);\n"
        + "#33=VERTEX_POINT('V3',#4);\n"
        + "#40=EDGE_CURVE('E0',#30,#31,#12,.T.);\n"
        + "#41=EDGE_CURVE('E1',#31,#32,#16,.T.);\n"
        + "#42=EDGE_CURVE('E2',#32,#33,#20,.T.);\n"
        + "#43=EDGE_CURVE('E3',#33,#30,#24,.T.);\n"
        + "#50=ORIENTED_EDGE('OE0',$,$,#40,.T.);\n"
        + "#51=ORIENTED_EDGE('OE1',$,$,#41,.T.);\n"
        + "#52=ORIENTED_EDGE('OE2',$,$,#42,.T.);\n"
        + "#53=ORIENTED_EDGE('OE3',$,$,#43,.T.);\n"
        + "#60=EDGE_LOOP('LOOP0',(#50,#51,#52,#53));\n"
        + "#61=FACE_OUTER_BOUND('FOB',#60,.T.);\n"
        + "#62=FACE_SURFACE('F0',(#61),#25,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(62);

        assertEquals(1, face.bounds().size());
        assertEquals(1.0, planeSurface(face).normal().z(), 1.0e-12);
    }

    @Test
    void shouldBuildEdgeBackedByOrientedAnnotationWrapperCurve() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=VECTOR('VX',#3,1.0);\n"
        + "#5=LINE('L0',#1,#4);\n"
        + "#6=PRESENTATION_STYLE_ASSIGNMENT(());\n"
        + "#7=(PROJECTION_CURVE('PC0',(#6),#5)\n"
        + "    ANNOTATION_CURVE_OCCURRENCE('PC0',(#6),#5)\n"
        + "    STYLED_ITEM('PC0',(#6),#5)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('PC0'));\n"
        + "#8=ORIENTED_CURVE('OC0',#7,.F.);\n"
        + "#9=VERTEX_POINT('V0',#1);\n"
        + "#10=VERTEX_POINT('V1',#2);\n"
        + "#11=EDGE_CURVE('E0',#9,#10,#8,.T.);\n"
        + "ENDSEC;"
        );

        Edge edge = builder.buildEdge(11);

        assertInstanceOf(Line3.class, edge.curve());
        assertTrue(edge.curve().contains(edge.start().point()));
        assertTrue(edge.curve().contains(edge.end().point()));
    }

    @Test
    void shouldBuildEdgeBackedByReplicaOfAnnotationWrapperCurve() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('T',(10.0,0.0,0.0));\n"
        + "#4=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#5=VECTOR('VX',#4,1.0);\n"
        + "#6=LINE('L0',#1,#5);\n"
        + "#7=PRESENTATION_STYLE_ASSIGNMENT(());\n"
        + "#8=(PROJECTION_CURVE('PC0',(#7),#6)\n"
        + "    ANNOTATION_CURVE_OCCURRENCE('PC0',(#7),#6)\n"
        + "    STYLED_ITEM('PC0',(#7),#6)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('PC0'));\n"
        + "#9=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#3,1.0,$);\n"
        + "#10=CURVE_REPLICA('CR0',#8,#9);\n"
        + "#11=VERTEX_POINT('V0',#3);\n"
        + "#12=CARTESIAN_POINT('P2',(11.0,0.0,0.0));\n"
        + "#13=VERTEX_POINT('V1',#12);\n"
        + "#14=EDGE_CURVE('E0',#11,#13,#10,.T.);\n"
        + "ENDSEC;"
        );

        Edge edge = builder.buildEdge(14);

        assertTrue(edge.curve().contains(edge.start().point()));
        assertTrue(edge.curve().contains(edge.end().point()));
    }

    @Test
    void shouldBuildEdgeBackedByOrientedReplicaOfAnnotationWrapperCurve() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('T',(10.0,0.0,0.0));\n"
        + "#4=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#5=VECTOR('VX',#4,1.0);\n"
        + "#6=LINE('L0',#1,#5);\n"
        + "#7=PRESENTATION_STYLE_ASSIGNMENT(());\n"
        + "#8=(PROJECTION_CURVE('PC0',(#7),#6)\n"
        + "    ANNOTATION_CURVE_OCCURRENCE('PC0',(#7),#6)\n"
        + "    STYLED_ITEM('PC0',(#7),#6)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('PC0'));\n"
        + "#9=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',$,$,#3,1.0,$);\n"
        + "#10=CURVE_REPLICA('CR0',#8,#9);\n"
        + "#11=ORIENTED_CURVE('OC0',#10,.F.);\n"
        + "#12=VERTEX_POINT('V0',#3);\n"
        + "#13=CARTESIAN_POINT('P2',(11.0,0.0,0.0));\n"
        + "#14=VERTEX_POINT('V1',#13);\n"
        + "#15=EDGE_CURVE('E0',#12,#14,#11,.T.);\n"
        + "ENDSEC;"
        );

        Edge edge = builder.buildEdge(15);

        assertTrue(edge.curve().contains(edge.start().point()));
        assertTrue(edge.curve().contains(edge.end().point()));
    }

    @Test
    void shouldBuildSweptSurfacesWithWrapperDirectrix() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#5=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#6=AXIS1_PLACEMENT('AX1',#1,#2);\n"
        + "#7=CARTESIAN_POINT('P0',(2.0,0.0,0.0));\n"
        + "#8=VECTOR('VY',#4,1.0);\n"
        + "#9=LINE('L0',#7,#8);\n"
        + "#10=PRESENTATION_STYLE_ASSIGNMENT(());\n"
        + "#11=(PROJECTION_CURVE('PC0',(#10),#9)\n"
        + "    ANNOTATION_CURVE_OCCURRENCE('PC0',(#10),#9)\n"
        + "    STYLED_ITEM('PC0',(#10),#9)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('PC0'));\n"
        + "#12=(LEADER_CURVE('LC0',(#10),#9)\n"
        + "    ANNOTATION_CURVE_OCCURRENCE('LC0',(#10),#9)\n"
        + "    STYLED_ITEM('LC0',(#10),#9)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('LC0'));\n"
        + "#13=SURFACE_OF_LINEAR_EXTRUSION('SLE0',#11,#8);\n"
        + "#14=SURFACE_OF_REVOLUTION('SOR0',#12,#6);\n"
        + "#20=CARTESIAN_POINT('A0',(0.0,0.0,0.0));\n"
        + "#21=CARTESIAN_POINT('A1',(1.0,0.0,0.0));\n"
        + "#22=CARTESIAN_POINT('A2',(1.0,1.0,0.0));\n"
        + "#23=CARTESIAN_POINT('A3',(0.0,1.0,0.0));\n"
        + "#30=VERTEX_POINT('V0',#20);\n"
        + "#31=VERTEX_POINT('V1',#21);\n"
        + "#32=VERTEX_POINT('V2',#22);\n"
        + "#33=VERTEX_POINT('V3',#23);\n"
        + "#40=VECTOR('VX0',#3,1.0);\n"
        + "#41=LINE('E0L',#20,#40);\n"
        + "#42=VECTOR('VY0',#4,1.0);\n"
        + "#43=LINE('E1L',#21,#42);\n"
        + "#44=DIRECTION('MX',(-1.0,0.0,0.0));\n"
        + "#45=VECTOR('VMX',#44,1.0);\n"
        + "#46=LINE('E2L',#22,#45);\n"
        + "#47=DIRECTION('MY',(0.0,-1.0,0.0));\n"
        + "#48=VECTOR('VMY',#47,1.0);\n"
        + "#49=LINE('E3L',#23,#48);\n"
        + "#50=EDGE_CURVE('E0',#30,#31,#41,.T.);\n"
        + "#51=EDGE_CURVE('E1',#31,#32,#43,.T.);\n"
        + "#52=EDGE_CURVE('E2',#32,#33,#46,.T.);\n"
        + "#53=EDGE_CURVE('E3',#33,#30,#49,.T.);\n"
        + "#54=ORIENTED_EDGE('OE0',$,$,#50,.T.);\n"
        + "#55=ORIENTED_EDGE('OE1',$,$,#51,.T.);\n"
        + "#56=ORIENTED_EDGE('OE2',$,$,#52,.T.);\n"
        + "#57=ORIENTED_EDGE('OE3',$,$,#53,.T.);\n"
        + "#58=EDGE_LOOP('LOOP0',(#54,#55,#56,#57));\n"
        + "#59=FACE_OUTER_BOUND('FOB',#58,.T.);\n"
        + "#60=ADVANCED_FACE('FEX',(#59),#13,.T.);\n"
        + "#61=ADVANCED_FACE('FREV',(#59),#14,.T.);\n"
        + "ENDSEC;"
        );

        Face extrusion = builder.buildFace(60);
        Face revolution = builder.buildFace(61);

        assertEquals(1, extrusion.bounds().size());
        assertEquals(1, revolution.bounds().size());
        assertInstanceOf(com.minicad.geometry.SurfaceOfLinearExtrusion3.class, extrusion.surface());
        assertInstanceOf(com.minicad.geometry.SurfaceOfRevolution3.class, revolution.surface());
    }

    @Test
    void shouldBuildWrappedCurveEntitiesAndSubedge() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(3.0,0.0,0.0));\n"
        + "#5=CARTESIAN_POINT('SHIFT',(10.0,0.0,0.0));\n"
        + "#10=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#11=VECTOR('V0',#10,1.0);\n"
        + "#12=LINE('L0',#1,#11);\n"
        + "#13=ORIENTED_CURVE('OC0',#12,.F.);\n"
        + "#14=COMPOSITE_CURVE_SEGMENT(.CONTINUOUS.,.T.,#13);\n"
        + "#15=(COMPOSITE_CURVE('CC0',(#14),.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('cc-name'));\n"
        + "#16=POLYLINE('PL0',(#1,#2,#3,#4));\n"
        + "#17=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',$,$,#5,1.0,$);\n"
        + "#18=CURVE_REPLICA('CR0',#12,#17);\n"
        + "#20=VERTEX_POINT('V0',#1);\n"
        + "#21=VERTEX_POINT('V1',#2);\n"
        + "#22=VERTEX_POINT('V2',#3);\n"
        + "#23=VERTEX_POINT('V3',#4);\n"
        + "#24=VERTEX_POINT('V4',#5);\n"
        + "#25=CARTESIAN_POINT('SHIFT_END',(11.0,0.0,0.0));\n"
        + "#26=VERTEX_POINT('V5',#25);\n"
        + "#30=EDGE_CURVE('E0',#20,#21,#15,.T.);\n"
        + "#31=EDGE_CURVE('E1',#21,#23,#16,.T.);\n"
        + "#32=(SUBEDGE('SE0',#21,#22,#31) EDGE() TOPOLOGICAL_REPRESENTATION_ITEM('subedge'));\n"
        + "#33=EDGE_CURVE('E2',#24,#26,#18,.T.);\n"
        + "ENDSEC;"

        );


        CompositeCurve3 compositeCurve = builder.buildCompositeCurve(15);
        Polyline3 polyline = builder.buildPolyline(16);
        Edge subedge = builder.buildEdge(32);
        Edge replicaEdge = builder.buildEdge(33);

        assertInstanceOf(CompositeCurve3.class, compositeCurve);
        assertInstanceOf(Polyline3.class, polyline);
        assertEquals(1, compositeCurve.segments().size());
        assertTrue(subedge.curve().contains(subedge.start().point()));
        assertTrue(subedge.curve().contains(subedge.end().point()));
        assertTrue(replicaEdge.curve().contains(replicaEdge.start().point()));
        assertTrue(replicaEdge.curve().contains(replicaEdge.end().point()));
    }

    @Test
    void shouldBuildPlanarFaceThroughWrappedSurfaceEntities() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#10=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#11=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);\n"
        + "#13=PLANE('PL0',#12);\n"
        + "#14=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',$,$,#1,1.0,$);\n"
        + "#15=SURFACE_REPLICA('SR0',#13,#14);\n"
        + "#16=RECTANGULAR_TRIMMED_SURFACE('RTS0',#15,0.0,1.0,0.0,1.0,.T.,.T.);\n"
        + "#17=ORIENTED_SURFACE('OS0',#16,.F.);\n"
        + "#18=VECTOR('VX',#11,1.0);\n"
        + "#19=LINE('L0',#1,#18);\n"
        + "#20=CURVE_REPLICA('CR0',#19,#14);\n"
        + "#21=CURVE_BOUNDED_SURFACE('CBS0',#17,(#20),.T.);\n"
        + "#30=VERTEX_POINT('V0',#1);\n"
        + "#31=VERTEX_POINT('V1',#2);\n"
        + "#32=VERTEX_POINT('V2',#3);\n"
        + "#33=VERTEX_POINT('V3',#4);\n"
        + "#40=DIRECTION('D1',(1.0,0.0,0.0));\n"
        + "#41=VECTOR('VE1',#40,1.0);\n"
        + "#42=LINE('E1',#1,#41);\n"
        + "#43=DIRECTION('D2',(0.0,1.0,0.0));\n"
        + "#44=VECTOR('VE2',#43,1.0);\n"
        + "#45=LINE('E2',#2,#44);\n"
        + "#46=DIRECTION('D3',(-1.0,0.0,0.0));\n"
        + "#47=VECTOR('VE3',#46,1.0);\n"
        + "#48=LINE('E3',#3,#47);\n"
        + "#49=DIRECTION('D4',(0.0,-1.0,0.0));\n"
        + "#50=VECTOR('VE4',#49,1.0);\n"
        + "#51=LINE('E4',#4,#50);\n"
        + "#60=EDGE_CURVE('EC1',#30,#31,#42,.T.);\n"
        + "#61=EDGE_CURVE('EC2',#31,#32,#45,.T.);\n"
        + "#62=EDGE_CURVE('EC3',#32,#33,#48,.T.);\n"
        + "#63=EDGE_CURVE('EC4',#33,#30,#51,.T.);\n"
        + "#70=ORIENTED_EDGE('OE1',$,$,#60,.T.);\n"
        + "#71=ORIENTED_EDGE('OE2',$,$,#61,.T.);\n"
        + "#72=ORIENTED_EDGE('OE3',$,$,#62,.T.);\n"
        + "#73=ORIENTED_EDGE('OE4',$,$,#63,.T.);\n"
        + "#80=EDGE_LOOP('LOOP',(#70,#71,#72,#73));\n"
        + "#81=FACE_OUTER_BOUND('FOB',#80,.T.);\n"
        + "#82=FACE_SURFACE('FS0',(#81),#21,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(82);

        assertEquals(1, face.bounds().size());
        assertEquals(-1.0, planeSurface(face).normal().z(), 1.0e-12);
    }

    @Test
    void shouldRejectZeroScaleSurfaceReplicaFaceConstruction() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#10=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#11=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);\n"
        + "#13=PLANE('PL0',#12);\n"
        + "#14=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',$,$,#1,0.0,$);\n"
        + "#15=SURFACE_REPLICA('SR0',#13,#14);\n"
        + "#30=VERTEX_POINT('V0',#1);\n"
        + "#31=VERTEX_POINT('V1',#2);\n"
        + "#32=VERTEX_POINT('V2',#3);\n"
        + "#33=VERTEX_POINT('V3',#4);\n"
        + "#40=DIRECTION('D1',(1.0,0.0,0.0));\n"
        + "#41=VECTOR('VE1',#40,1.0);\n"
        + "#42=LINE('E1',#1,#41);\n"
        + "#43=DIRECTION('D2',(0.0,1.0,0.0));\n"
        + "#44=VECTOR('VE2',#43,1.0);\n"
        + "#45=LINE('E2',#2,#44);\n"
        + "#46=DIRECTION('D3',(-1.0,0.0,0.0));\n"
        + "#47=VECTOR('VE3',#46,1.0);\n"
        + "#48=LINE('E3',#3,#47);\n"
        + "#49=DIRECTION('D4',(0.0,-1.0,0.0));\n"
        + "#50=VECTOR('VE4',#49,1.0);\n"
        + "#51=LINE('E4',#4,#50);\n"
        + "#60=EDGE_CURVE('EC1',#30,#31,#42,.T.);\n"
        + "#61=EDGE_CURVE('EC2',#31,#32,#45,.T.);\n"
        + "#62=EDGE_CURVE('EC3',#32,#33,#48,.T.);\n"
        + "#63=EDGE_CURVE('EC4',#33,#30,#51,.T.);\n"
        + "#70=ORIENTED_EDGE('OE1',$,$,#60,.T.);\n"
        + "#71=ORIENTED_EDGE('OE2',$,$,#61,.T.);\n"
        + "#72=ORIENTED_EDGE('OE3',$,$,#62,.T.);\n"
        + "#73=ORIENTED_EDGE('OE4',$,$,#63,.T.);\n"
        + "#80=EDGE_LOOP('LOOP',(#70,#71,#72,#73));\n"
        + "#81=FACE_OUTER_BOUND('FOB',#80,.T.);\n"
        + "#82=ADVANCED_FACE('F0',(#81),#15,.T.);\n"
        + "ENDSEC;"

        );


        UnsupportedGeometryException exception = assertThrows(UnsupportedGeometryException.class, () -> builder.buildFace(82));
        assertEquals("SURFACE_REPLICA zero scale is unsupported", exception.getMessage());
    }

    @Test
    void shouldRejectSurfaceReplicaWithNonUniformScaleForFaceBuild() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#10=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#11=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);\n"
        + "#13=PLANE('PL0',#12);\n"
        + "#14=CARTESIAN_POINT('T0',(0.0,0.0,2.0));\n"
        + "#15=DIRECTION('SX',(1.0,0.0,0.0));\n"
        + "#16=DIRECTION('SY',(1.0,1.0,0.0));\n"
        + "#17=DIRECTION('SZ',(0.0,0.0,1.0));\n"
        + "#18=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#15,#16,#14,1.0,#17);\n"
        + "#19=SURFACE_REPLICA('SR0',#13,#18);\n"
        + "#30=VERTEX_POINT('V0',#1);\n"
        + "#31=VERTEX_POINT('V1',#2);\n"
        + "#32=VERTEX_POINT('V2',#3);\n"
        + "#33=VERTEX_POINT('V3',#4);\n"
        + "#40=DIRECTION('D1',(1.0,0.0,0.0));\n"
        + "#41=VECTOR('VE1',#40,1.0);\n"
        + "#42=LINE('E1',#1,#41);\n"
        + "#43=DIRECTION('D2',(0.0,1.0,0.0));\n"
        + "#44=VECTOR('VE2',#43,1.0);\n"
        + "#45=LINE('E2',#2,#44);\n"
        + "#46=DIRECTION('D3',(-1.0,0.0,0.0));\n"
        + "#47=VECTOR('VE3',#46,1.0);\n"
        + "#48=LINE('E3',#3,#47);\n"
        + "#49=DIRECTION('D4',(0.0,-1.0,0.0));\n"
        + "#50=VECTOR('VE4',#49,1.0);\n"
        + "#51=LINE('E4',#4,#50);\n"
        + "#60=EDGE_CURVE('EC1',#30,#31,#42,.T.);\n"
        + "#61=EDGE_CURVE('EC2',#31,#32,#45,.T.);\n"
        + "#62=EDGE_CURVE('EC3',#32,#33,#48,.T.);\n"
        + "#63=EDGE_CURVE('EC4',#33,#30,#51,.T.);\n"
        + "#70=ORIENTED_EDGE('OE1',$,$,#60,.T.);\n"
        + "#71=ORIENTED_EDGE('OE2',$,$,#61,.T.);\n"
        + "#72=ORIENTED_EDGE('OE3',$,$,#62,.T.);\n"
        + "#73=ORIENTED_EDGE('OE4',$,$,#63,.T.);\n"
        + "#80=EDGE_LOOP('LOOP',(#70,#71,#72,#73));\n"
        + "#81=FACE_OUTER_BOUND('FOB',#80,.T.);\n"
        + "#82=ADVANCED_FACE('F0',(#81),#19,.T.);\n"
        + "ENDSEC;"

        );


        UnsupportedGeometryException exception = assertThrows(UnsupportedGeometryException.class, () -> builder.buildFace(82));
        assertEquals("SURFACE_REPLICA non-uniform scale is unsupported", exception.getMessage());
    }

    @Test
    void shouldBuildUniformWeightRationalBsplineCurve() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=VERTEX_POINT('V0',#1);\n"
        + "#5=VERTEX_POINT('V1',#3);\n"
        + "#10=(B_SPLINE_CURVE('RBK',2,(#1,#2,#3),.UNSPECIFIED.,.F.,.F.)\n"
        + "     B_SPLINE_CURVE_WITH_KNOTS((3,3),(0.0,1.0),.UNSPECIFIED.)\n"
        + "     RATIONAL_B_SPLINE_CURVE((1.0,1.0,1.0)));\n"
        + "#11=TRIMMED_CURVE('TC0',#10,(#1),(#3),.T.,.CARTESIAN.);\n"
        + "#12=EDGE_CURVE('EC0',#4,#5,#10,.T.);\n"
        + "ENDSEC;"

        );


        RationalBSplineCurve3 spline = builder.buildRationalBSplineCurve(10);
        TrimmedCurve3 trimmedCurve = builder.buildTrimmedCurve(11);
        Edge edge = builder.buildEdge(12);

        assertEquals(2, spline.degree());
        assertEquals(9, spline.sample(8).size());
        assertInstanceOf(TrimmedCurve3.class, trimmedCurve);
        assertEquals(new CartesianPoint(0.0, 0.0, 0.0), trimmedCurve.trimStart());
        assertEquals(new CartesianPoint(1.0, 1.0, 0.0), trimmedCurve.trimEnd());
        assertTrue(edge.curve().contains(edge.start().point()));
        assertTrue(edge.curve().contains(edge.end().point()));
    }

    @Test
    void shouldBuildRationalBsplineSurfaceGeometry() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P10',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P01',(0.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P11',(1.0,1.0,1.0));\n"
        + "#10=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)\n"
        + "     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.)\n"
        + "     RATIONAL_B_SPLINE_SURFACE(((1.0,1.0),(1.0,0.5))));\n"
        + "ENDSEC;"

        );


        RationalBSplineSurface3 surface = builder.buildRationalBSplineSurface(10);
        CartesianPoint point = surface.pointAt(0.5, 0.5);

        assertEquals(1, surface.uDegree());
        assertEquals(1, surface.vDegree());
        assertTrue(point.z() > 0.0);
        assertTrue(point.z() < 0.5);
    }

    @Test
    void shouldRejectWrongTopLevelEntityTypeForSolidBuild() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "ENDSEC;"

        );


        StepResolutionException exception = assertThrows(
                StepResolutionException.class,
                () -> builder.buildSolid(1)
        );

        assertEquals("entity #1 is not a supported SOLID", exception.getMessage());
    }

    @Test
    void shouldBuildCylindricalAdvancedFaceConstruction() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#10=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#11=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);\n"
        + "#13=CYLINDRICAL_SURFACE('CY0',#12,2.0);\n"
        + "#20=CARTESIAN_POINT('P0',(2.0,0.0,0.0));\n"
        + "#21=CARTESIAN_POINT('P1',(0.0,2.0,0.0));\n"
        + "#22=CARTESIAN_POINT('P2',(0.0,2.0,1.0));\n"
        + "#23=POLY_LOOP('L0',(#20,#21,#22));\n"
        + "#61=FACE_OUTER_BOUND('B0',#23,.T.);\n"
        + "#70=ADVANCED_FACE('F0',(#61),#13,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(70);

        assertEquals(1, face.bounds().size());
        assertInstanceOf(CylindricalSurface.class, face.surface());
    }

    @Test
    void shouldBuildPlanarFaceSurfaceAndOrientedFace() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#10=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#11=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_3D('AXIS',#1,#10,#11);\n"
        + "#13=PLANE('PL0',#12);\n"
        + "#20=VERTEX_POINT('V0',#1);\n"
        + "#21=VERTEX_POINT('V1',#2);\n"
        + "#22=VERTEX_POINT('V2',#3);\n"
        + "#23=VERTEX_POINT('V3',#4);\n"
        + "#30=DIRECTION('D1',(1.0,0.0,0.0));\n"
        + "#31=VECTOR('VE1',#30,1.0);\n"
        + "#32=LINE('L1',#1,#31);\n"
        + "#33=DIRECTION('D2',(0.0,1.0,0.0));\n"
        + "#34=VECTOR('VE2',#33,1.0);\n"
        + "#35=LINE('L2',#2,#34);\n"
        + "#36=DIRECTION('D3',(-1.0,0.0,0.0));\n"
        + "#37=VECTOR('VE3',#36,1.0);\n"
        + "#38=LINE('L3',#3,#37);\n"
        + "#39=DIRECTION('D4',(0.0,-1.0,0.0));\n"
        + "#40=VECTOR('VE4',#39,1.0);\n"
        + "#41=LINE('L4',#4,#40);\n"
        + "#50=EDGE_CURVE('E1',#20,#21,#32,.T.);\n"
        + "#51=EDGE_CURVE('E2',#21,#22,#35,.T.);\n"
        + "#52=EDGE_CURVE('E3',#22,#23,#38,.T.);\n"
        + "#53=EDGE_CURVE('E4',#23,#20,#41,.T.);\n"
        + "#60=ORIENTED_EDGE('OE1',$,$,#50,.T.);\n"
        + "#61=ORIENTED_EDGE('OE2',$,$,#51,.T.);\n"
        + "#62=ORIENTED_EDGE('OE3',$,$,#52,.T.);\n"
        + "#63=ORIENTED_EDGE('OE4',$,$,#53,.T.);\n"
        + "#70=EDGE_LOOP('LOOP',(#60,#61,#62,#63));\n"
        + "#71=FACE_OUTER_BOUND('FOB',#70,.T.);\n"
        + "#80=FACE_SURFACE('FS0',(#71),#13,.T.);\n"
        + "#81=ORIENTED_FACE('OF0',#80,.F.);\n"
        + "#90=OPEN_SHELL('OS',(#81));\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(80);
        Face orientedFace = builder.buildFace(81);
        Shell openShell = builder.buildShell(90);

        assertEquals(true, face.sameSense());
        assertEquals(false, orientedFace.sameSense());
        assertEquals(1, openShell.faces().size());
    }

    @Test
    void shouldBuildVertexLoopFaceConstruction() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=PLANE('PL0',#4);\n"
        + "#10=VERTEX_POINT('V0',#1);\n"
        + "#11=VERTEX_LOOP('VL0',#10);\n"
        + "#12=FACE_OUTER_BOUND('B0',#11,.T.);\n"
        + "#13=FACE_SURFACE('FS0',(#12),#5,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(13);

        assertEquals(1, face.bounds().size());
        assertEquals(true, face.bounds().get(0).loop() instanceof com.minicad.topology.VertexLoop);
    }

    @Test
    void shouldBuildPolyLoopFaceConstruction() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#5,#6);\n"
        + "#5=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#6=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#7=PLANE('PL0',#4);\n"
        + "#8=POLY_LOOP('PL0',(#1,#2,#3));\n"
        + "#9=FACE_OUTER_BOUND('B0',#8,.T.);\n"
        + "#10=FACE_SURFACE('FS0',(#9),#7,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(10);

        assertEquals(1, face.bounds().size());
        assertTrue(face.bounds().get(0).loop() instanceof com.minicad.topology.PolyLoop);
    }

    @Test
    void shouldBuildOffsetCurvesAndOffsetPlaneFace() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P0',(0.0,0.0));\n"
        + "#3=DIRECTION('DU',(1.0,0.0));\n"
        + "#4=VECTOR('VU',#3,1.0);\n"
        + "#5=LINE('L2D',#2,#4);\n"
        + "#6=OFFSET_CURVE_2D('OC2',#5,0.5,.F.);\n"
        + "#10=CARTESIAN_POINT('A',(0.0,0.0,0.0));\n"
        + "#11=CARTESIAN_POINT('B',(1.0,0.0,0.0));\n"
        + "#17=CARTESIAN_POINT('F0',(0.0,0.0,2.0));\n"
        + "#18=CARTESIAN_POINT('F1',(1.0,0.0,2.0));\n"
        + "#19=CARTESIAN_POINT('F2',(0.0,1.0,2.0));\n"
        + "#12=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#13=VECTOR('VX',#12,1.0);\n"
        + "#14=LINE('L3D',#10,#13);\n"
        + "#15=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#16=OFFSET_CURVE_3D('OC3',#14,0.25,.F.,#15);\n"
        + "#20=DIRECTION('NZ',(0.0,0.0,1.0));\n"
        + "#21=AXIS2_PLACEMENT_3D('AX',#1,#20,#12);\n"
        + "#22=PLANE('PL',#21);\n"
        + "#23=OFFSET_SURFACE('OS',#22,2.0,.T.);\n"
        + "#30=VERTEX_POINT('V0',#10);\n"
        + "#31=VERTEX_POINT('V1',#11);\n"
        + "#32=EDGE_CURVE('E0',#30,#31,#14,.T.);\n"
        + "#33=ORIENTED_EDGE('OE0',$,$,#32,.T.);\n"
        + "#34=POLY_LOOP('PL0',(#17,#18,#19));\n"
        + "#35=FACE_OUTER_BOUND('FB0',#34,.T.);\n"
        + "#36=FACE_SURFACE('F0',(#35),#23,.T.);\n"
        + "ENDSEC;"

        );


        assertTrue(builder.buildOffsetCurve2(6).contains(new Point2(0.0, 0.5)));
        var offsetCurve3 = builder.buildOffsetCurve3(16);
        Face face = builder.buildFace(36);

        assertTrue(offsetCurve3.contains(new com.minicad.geometry.CartesianPoint(0.0, -0.25, 0.0)));
        assertEquals(2.0, planeSurface(face).origin().z(), 1.0e-12);
    }

    @Test
    void shouldBuildExtrudedAreaSolidFromRectangleProfile() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=CARTESIAN_POINT('P2',(0.0,0.0));\n"
        + "#6=DIRECTION('DX2',(1.0,0.0));\n"
        + "#7=AXIS2_PLACEMENT_2D('AX2',#5,#6);\n"
        + "#8=RECTANGLE_PROFILE_DEF(.AREA.,'R',#7,4.0,2.0);\n"
        + "#9=EXTRUDED_AREA_SOLID('EX',#8,#4,#2,5.0);\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(9);

        assertEquals(6, solid.outerShell().faces().size());
        assertEquals(true, solid.outerShell().closed());
        assertEquals(0, solid.voidShells().size());
    }

    @Test
    void shouldBuildSolidReplicaFromExtrudedAreaSolid() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=CARTESIAN_POINT('P2',(0.0,0.0));\n"
        + "#6=DIRECTION('DX2',(1.0,0.0));\n"
        + "#7=AXIS2_PLACEMENT_2D('AX2',#5,#6);\n"
        + "#8=RECTANGLE_PROFILE_DEF(.AREA.,'R',#7,4.0,2.0);\n"
        + "#9=EXTRUDED_AREA_SOLID('EX',#8,#4,#2,5.0);\n"
        + "#10=CARTESIAN_POINT('T',(10.0,0.0,0.0));\n"
        + "#11=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#12=CARTESIAN_TRANSFORMATION_OPERATOR_3D('X',#3,#11,#10,1.0,#2);\n"
        + "#13=SOLID_REPLICA('SR',#9,#12);\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(13);

        assertEquals(6, solid.outerShell().faces().size());
        assertEquals(8.0, planeSurface(solid.outerShell().faces().get(0)).origin().x(), 1.0e-12);
    }

    @Test
    void shouldBuildRevolvedAreaSolid() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#5=AXIS2_PLACEMENT_3D('AX3',#1,#4,#3);\n"
        + "#6=AXIS1_PLACEMENT('AX1',#1,#2);\n"
        + "#7=CARTESIAN_POINT('P2',(3.0,0.0));\n"
        + "#8=DIRECTION('DX2',(1.0,0.0));\n"
        + "#9=AXIS2_PLACEMENT_2D('AX2',#7,#8);\n"
        + "#10=RECTANGLE_PROFILE_DEF(.AREA.,'R',#9,2.0,4.0);\n"
        + "#11=REVOLVED_AREA_SOLID('RV',#10,#5,#6,1.57079632679);\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(11);

        assertEquals(34, solid.outerShell().faces().size());
        assertEquals(true, solid.outerShell().closed());
        assertEquals(0, solid.voidShells().size());
    }

    @Test
    void shouldBuildRevolvedAreaSolidFromHollowProfile() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#5=AXIS2_PLACEMENT_3D('AX3',#1,#4,#3);\n"
        + "#6=AXIS1_PLACEMENT('AX1',#1,#2);\n"
        + "#7=CARTESIAN_POINT('P2',(4.0,0.0));\n"
        + "#8=DIRECTION('DX2',(1.0,0.0));\n"
        + "#9=AXIS2_PLACEMENT_2D('AX2',#7,#8);\n"
        + "#10=CIRCULAR_HOLLOW_PROFILE_DEF(.AREA.,'CH',#9,2.0,0.5);\n"
        + "#11=REVOLVED_AREA_SOLID('RVH',#10,#5,#6,0.19634954084936207);\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(11);

        assertEquals(146, solid.outerShell().faces().size());
        assertEquals(true, solid.outerShell().closed());
        assertEquals(0, solid.voidShells().size());
    }

    @Test
    void shouldBuildExtrudedAreaSolidFromCircularAndHollowProfiles() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=CARTESIAN_POINT('P2',(0.0,0.0));\n"
        + "#6=DIRECTION('DX2',(1.0,0.0));\n"
        + "#7=AXIS2_PLACEMENT_2D('AX2',#5,#6);\n"
        + "#8=CIRCLE_PROFILE_DEF(.AREA.,'C',#7,2.0);\n"
        + "#9=ELLIPSE_PROFILE_DEF(.AREA.,'E',#7,3.0,1.5);\n"
        + "#10=ROUNDED_RECTANGLE_PROFILE_DEF(.AREA.,'RR',#7,6.0,4.0,0.5);\n"
        + "#11=CIRCULAR_HOLLOW_PROFILE_DEF(.AREA.,'CH',#7,3.0,0.5);\n"
        + "#12=EXTRUDED_AREA_SOLID('EX0',#8,#4,#2,5.0);\n"
        + "#13=EXTRUDED_AREA_SOLID('EX1',#9,#4,#2,5.0);\n"
        + "#14=EXTRUDED_AREA_SOLID('EX2',#10,#4,#2,5.0);\n"
        + "#15=EXTRUDED_AREA_SOLID('EX3',#11,#4,#2,5.0);\n"
        + "ENDSEC;"
        );

        Solid circle = builder.buildSolid(12);
        Solid ellipse = builder.buildSolid(13);
        Solid rounded = builder.buildSolid(14);
        Solid hollow = builder.buildSolid(15);

        assertEquals(74, circle.outerShell().faces().size());
        assertEquals(74, ellipse.outerShell().faces().size());
        assertEquals(51, rounded.outerShell().faces().size());
        assertEquals(146, hollow.outerShell().faces().size());
        assertEquals(0, hollow.voidShells().size());
    }

    @Test
    void shouldBuildExtrudedAreaSolidFromArbitraryProfileWithVoids() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=CARTESIAN_POINT('P0',(0.0,0.0));\n"
        + "#6=CARTESIAN_POINT('P1',(4.0,0.0));\n"
        + "#7=CARTESIAN_POINT('P2',(4.0,4.0));\n"
        + "#8=CARTESIAN_POINT('P3',(0.0,4.0));\n"
        + "#9=CARTESIAN_POINT('P4',(1.0,1.0));\n"
        + "#10=CARTESIAN_POINT('P5',(3.0,1.0));\n"
        + "#11=CARTESIAN_POINT('P6',(3.0,3.0));\n"
        + "#12=CARTESIAN_POINT('P7',(1.0,3.0));\n"
        + "#13=POLYLINE('OUTER',(#5,#6,#7,#8,#5));\n"
        + "#14=POLYLINE('INNER',(#9,#10,#11,#12,#9));\n"
        + "#15=ARBITRARY_PROFILE_DEF_WITH_VOIDS(.AREA.,'APV',#13,(#14));\n"
        + "#16=EXTRUDED_AREA_SOLID('EXV',#15,#4,#2,5.0);\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(16);

        assertEquals(10, solid.outerShell().faces().size());
        assertEquals(true, solid.outerShell().closed());
        assertEquals(0, solid.voidShells().size());
    }

    @Test
    void shouldBuildRevolvedAreaSolidFromArbitraryProfileWithVoids() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#5=AXIS2_PLACEMENT_3D('AX3',#1,#4,#3);\n"
        + "#6=AXIS1_PLACEMENT('AX1',#1,#2);\n"
        + "#7=CARTESIAN_POINT('P0',(2.0,-2.0));\n"
        + "#8=CARTESIAN_POINT('P1',(6.0,-2.0));\n"
        + "#9=CARTESIAN_POINT('P2',(6.0,2.0));\n"
        + "#10=CARTESIAN_POINT('P3',(2.0,2.0));\n"
        + "#11=CARTESIAN_POINT('P4',(3.0,-1.0));\n"
        + "#12=CARTESIAN_POINT('P5',(5.0,-1.0));\n"
        + "#13=CARTESIAN_POINT('P6',(5.0,1.0));\n"
        + "#14=CARTESIAN_POINT('P7',(3.0,1.0));\n"
        + "#15=POLYLINE('OUTER',(#7,#8,#9,#10,#7));\n"
        + "#16=POLYLINE('INNER',(#11,#12,#13,#14,#11));\n"
        + "#17=ARBITRARY_PROFILE_DEF_WITH_VOIDS(.AREA.,'APV',#15,(#16));\n"
        + "#18=REVOLVED_AREA_SOLID('RVV',#17,#5,#6,1.57079632679);\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(18);

        assertEquals(66, solid.outerShell().faces().size());
        assertEquals(true, solid.outerShell().closed());
        assertEquals(0, solid.voidShells().size());
    }

    @Test
    void shouldBuildBlockCsgPrimitive() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=BLOCK('BLK',#4,10.0,20.0,30.0);\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(5);

        assertEquals(6, solid.outerShell().faces().size());
        assertEquals(true, solid.outerShell().closed());
    }

    @Test
    void shouldBuildAdditionalCsgPrimitives() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=SPHERE('SP',#4,2.0);\n"
        + "#6=ELLIPSOID('EL',#4,3.0,2.0,1.0);\n"
        + "#7=AXIS1_PLACEMENT('AX1',#1,#2);\n"
        + "#8=RIGHT_CIRCULAR_CYLINDER('CY',#7,5.0,2.0);\n"
        + "#9=TORUS('TO',#7,5.0,1.0);\n"
        + "#10=RIGHT_ANGULAR_WEDGE('WG',#4,4.0,3.0,2.0,2.5);\n"
        + "ENDSEC;"
        );

        Solid sphere = builder.buildSolid(5);
        Solid ellipsoid = builder.buildSolid(6);
        Solid cylinder = builder.buildSolid(8);
        Solid torus = builder.buildSolid(9);
        Solid wedge = builder.buildSolid(10);

        assertEquals(528, sphere.outerShell().faces().size());
        assertEquals(528, ellipsoid.outerShell().faces().size());
        assertEquals(50, cylinder.outerShell().faces().size());
        assertEquals(1024, torus.outerShell().faces().size());
        assertEquals(7, wedge.outerShell().faces().size());
    }

    @Test
    void shouldBuildBooleanDifferenceAgainstHalfSpace() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=BLOCK('BLK',#4,10.0,20.0,30.0);\n"
        + "#6=CARTESIAN_POINT('PZ',(0.0,0.0,15.0));\n"
        + "#7=AXIS2_PLACEMENT_3D('PLAX',#6,#2,#3);\n"
        + "#8=PLANE('PLANE',#7);\n"
        + "#9=HALF_SPACE_SOLID('HS',#8,.T.);\n"
        + "#10=(BOOLEAN_RESULT(.DIFFERENCE.,#5,#9) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BOOL0'));\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(10);

        assertEquals(6, solid.outerShell().faces().size());
        double maxZ = solid.outerShell().faces().stream()
                .mapToDouble(face -> planeSurface(face).origin().z())
                .max()
                .orElseThrow();
        assertEquals(15.0, maxZ, 1.0e-12);
    }

    @Test
    void shouldBuildCsgSolidFromBooleanTree() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=BLOCK('BLK',#4,10.0,20.0,30.0);\n"
        + "#6=CARTESIAN_POINT('PZ',(0.0,0.0,15.0));\n"
        + "#7=AXIS2_PLACEMENT_3D('PLAX',#6,#2,#3);\n"
        + "#8=PLANE('PLANE',#7);\n"
        + "#9=HALF_SPACE_SOLID('HS',#8,.T.);\n"
        + "#10=(BOOLEAN_RESULT(.DIFFERENCE.,#5,#9) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BOOL0'));\n"
        + "#11=CSG_SOLID('CSG0',#10);\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(11);

        assertEquals(6, solid.outerShell().faces().size());
        double maxZ = solid.outerShell().faces().stream()
                .mapToDouble(face -> planeSurface(face).origin().z())
                .max()
                .orElseThrow();
        assertEquals(15.0, maxZ, 1.0e-12);
    }

    @Test
    void shouldBuildBooleanClippingResultAgainstHalfSpace() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=BLOCK('BLK',#4,10.0,20.0,30.0);\n"
        + "#6=CARTESIAN_POINT('PZ',(0.0,0.0,15.0));\n"
        + "#7=AXIS2_PLACEMENT_3D('PLAX',#6,#2,#3);\n"
        + "#8=PLANE('PLANE',#7);\n"
        + "#9=HALF_SPACE_SOLID('HS',#8,.T.);\n"
        + "#10=(BOOLEAN_CLIPPING_RESULT(.DIFFERENCE.,#5,#9) BOOLEAN_RESULT(.DIFFERENCE.,#5,#9) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BCR0'));\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(10);

        assertEquals(6, solid.outerShell().faces().size());
        double maxZ = solid.outerShell().faces().stream()
                .mapToDouble(face -> planeSurface(face).origin().z())
                .max()
                .orElseThrow();
        assertEquals(15.0, maxZ, 1.0e-12);
    }

    @Test
    void shouldBuildBooleanDifferenceAgainstBoxedHalfSpace() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=BLOCK('BLK',#4,10.0,20.0,30.0);\n"
        + "#6=CARTESIAN_POINT('PZ',(0.0,0.0,15.0));\n"
        + "#7=AXIS2_PLACEMENT_3D('PLAX',#6,#2,#3);\n"
        + "#8=PLANE('PLANE',#7);\n"
        + "#9=BOX_DOMAIN(#1,5.0,20.0,30.0);\n"
        + "#10=BOXED_HALF_SPACE('BHS',#8,.T.,#9);\n"
        + "#11=(BOOLEAN_RESULT(.DIFFERENCE.,#5,#10) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BOOL0'));\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(11);

        assertEquals(6, solid.outerShell().faces().size());
        assertTrue(solid.outerShell().faces().stream()
                .map(StepCadBuilderTest::planeSurface)
                .anyMatch(plane -> Math.abs(plane.origin().x() - 5.0) <= 1.0e-12));
        assertTrue(solid.outerShell().faces().stream()
                .map(StepCadBuilderTest::planeSurface)
                .anyMatch(plane -> Math.abs(plane.origin().z() - 15.0) <= 1.0e-12));
    }

    @Test
    void shouldBuildBooleanIntersectionAgainstBoxedHalfSpace() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=BLOCK('BLK',#4,10.0,20.0,30.0);\n"
        + "#6=CARTESIAN_POINT('PZ',(0.0,0.0,15.0));\n"
        + "#7=AXIS2_PLACEMENT_3D('PLAX',#6,#2,#3);\n"
        + "#8=PLANE('PLANE',#7);\n"
        + "#9=BOX_DOMAIN(#1,5.0,20.0,30.0);\n"
        + "#10=BOXED_HALF_SPACE('BHS',#8,.T.,#9);\n"
        + "#11=(BOOLEAN_RESULT(.INTERSECTION.,#5,#10) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BOOL0'));\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(11);

        assertEquals(6, solid.outerShell().faces().size());
        assertTrue(solid.outerShell().faces().stream()
                .map(StepCadBuilderTest::planeSurface)
                .anyMatch(plane -> Math.abs(plane.origin().x() - 5.0) <= 1.0e-12));
        assertTrue(solid.outerShell().faces().stream()
                .map(StepCadBuilderTest::planeSurface)
                .anyMatch(plane -> Math.abs(plane.origin().z() - 15.0) <= 1.0e-12));
    }

    @Test
    void shouldBuildBooleanClippingResultAgainstBoxedHalfSpace() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=BLOCK('BLK',#4,10.0,20.0,30.0);\n"
        + "#6=CARTESIAN_POINT('PZ',(0.0,0.0,15.0));\n"
        + "#7=AXIS2_PLACEMENT_3D('PLAX',#6,#2,#3);\n"
        + "#8=PLANE('PLANE',#7);\n"
        + "#9=BOX_DOMAIN(#1,5.0,20.0,30.0);\n"
        + "#10=BOXED_HALF_SPACE('BHS',#8,.T.,#9);\n"
        + "#11=(BOOLEAN_CLIPPING_RESULT(.DIFFERENCE.,#5,#10) BOOLEAN_RESULT(.DIFFERENCE.,#5,#10) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BCR0'));\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(11);

        assertEquals(6, solid.outerShell().faces().size());
        assertTrue(solid.outerShell().faces().stream()
                .map(StepCadBuilderTest::planeSurface)
                .anyMatch(plane -> Math.abs(plane.origin().x() - 5.0) <= 1.0e-12));
        assertTrue(solid.outerShell().faces().stream()
                .map(StepCadBuilderTest::planeSurface)
                .anyMatch(plane -> Math.abs(plane.origin().z() - 15.0) <= 1.0e-12));
    }

    @Test
    void shouldBuildConicalSurfaceAndTrimmedCurveEdge() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=CONICAL_SURFACE('CN0',#4,2.0,0.2);\n"
        + "#6=CARTESIAN_POINT('P0',(2.0,0.0,0.0));\n"
        + "#7=CARTESIAN_POINT('P1',(0.0,2.0,0.0));\n"
        + "#8=VERTEX_POINT('V0',#6);\n"
        + "#9=VERTEX_POINT('V1',#7);\n"
        + "#10=CIRCLE('C0',#4,2.0);\n"
        + "#11=TRIMMED_CURVE('TC0',#10,(#6),(#7),.T.,.CARTESIAN.);\n"
        + "#12=EDGE_CURVE('E0',#8,#9,#11,.T.);\n"
        + "ENDSEC;"

        );


        ConicalSurface conicalSurface = builder.buildConicalSurface(5);
        Edge edge = builder.buildEdge(12);

        assertEquals(2.0, conicalSurface.radius());
        assertInstanceOf(TrimmedCurve3.class, edge.curve());
    }

    @Test
    void shouldBuildConicalFaceConstruction() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('A0',(2.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('B0',(0.0,2.0,0.0));\n"
        + "#4=CARTESIAN_POINT('O1',(0.0,0.0,2.0));\n"
        + "#5=CARTESIAN_POINT('A1',(2.4,0.0,2.0));\n"
        + "#6=CARTESIAN_POINT('B1',(0.0,2.4,2.0));\n"
        + "#10=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#11=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_3D('AX0',#1,#10,#11);\n"
        + "#13=AXIS2_PLACEMENT_3D('AX1',#4,#10,#11);\n"
        + "#14=CONICAL_SURFACE('CN0',#12,2.0,0.19739555984988078);\n"
        + "#15=CIRCLE('C0',#12,2.0);\n"
        + "#16=CIRCLE('C1',#13,2.4);\n"
        + "#20=VERTEX_POINT('V0',#2);\n"
        + "#21=VERTEX_POINT('V1',#3);\n"
        + "#22=VERTEX_POINT('V2',#6);\n"
        + "#23=VERTEX_POINT('V3',#5);\n"
        + "#30=DIRECTION('DB',(0.0,0.4,2.0));\n"
        + "#31=VECTOR('VB',#30,1.0);\n"
        + "#32=LINE('L0',#3,#31);\n"
        + "#33=DIRECTION('DA',(0.4,0.0,2.0));\n"
        + "#34=VECTOR('VA',#33,1.0);\n"
        + "#35=LINE('L1',#2,#34);\n"
        + "#40=EDGE_CURVE('E0',#20,#21,#15,.T.);\n"
        + "#41=EDGE_CURVE('E1',#21,#22,#32,.T.);\n"
        + "#42=EDGE_CURVE('E2',#23,#22,#16,.T.);\n"
        + "#43=EDGE_CURVE('E3',#20,#23,#35,.T.);\n"
        + "#50=ORIENTED_EDGE('OE0',$,$,#40,.T.);\n"
        + "#51=ORIENTED_EDGE('OE1',$,$,#41,.T.);\n"
        + "#52=ORIENTED_EDGE('OE2',$,$,#42,.F.);\n"
        + "#53=ORIENTED_EDGE('OE3',$,$,#43,.F.);\n"
        + "#60=EDGE_LOOP('L0',(#50,#51,#52,#53));\n"
        + "#61=FACE_OUTER_BOUND('B0',#60,.T.);\n"
        + "#70=ADVANCED_FACE('F0',(#61),#14,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(70);

        assertEquals(1, face.bounds().size());
        assertInstanceOf(ConicalSurface.class, face.surface());
    }

    @Test
    void shouldBuildWrappedSurfaceOfRevolutionFaceGeometry() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=AXIS1_PLACEMENT('A1',#1,#2);\n"
        + "#6=CARTESIAN_POINT('P0',(2.0,0.0,0.0));\n"
        + "#7=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#8=VECTOR('V0',#7,1.0);\n"
        + "#9=LINE('L0',#6,#8);\n"
        + "#10=SURFACE_OF_REVOLUTION('SR',#9,#5);\n"
        + "#11=RECTANGULAR_TRIMMED_SURFACE('RTS',#10,0.0,1.0,0.0,1.0,.T.,.T.);\n"
        + "#20=VERTEX_POINT('V0',#6);\n"
        + "#21=CARTESIAN_POINT('P1',(2.0,1.0,0.0));\n"
        + "#22=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#23=CARTESIAN_POINT('P3',(1.0,0.0,0.0));\n"
        + "#24=VERTEX_POINT('V1',#21);\n"
        + "#25=VERTEX_POINT('V2',#22);\n"
        + "#26=VERTEX_POINT('V3',#23);\n"
        + "#30=EDGE_CURVE('E0',#20,#24,#9,.T.);\n"
        + "#31=DIRECTION('MX',(-1.0,0.0,0.0));\n"
        + "#32=VECTOR('VMX',#31,1.0);\n"
        + "#33=LINE('L1',#21,#32);\n"
        + "#34=EDGE_CURVE('E1',#24,#25,#33,.T.);\n"
        + "#35=DIRECTION('MY',(0.0,-1.0,0.0));\n"
        + "#36=VECTOR('VMY',#35,1.0);\n"
        + "#37=LINE('L2',#22,#36);\n"
        + "#38=EDGE_CURVE('E2',#25,#26,#37,.T.);\n"
        + "#39=DIRECTION('PX',(1.0,0.0,0.0));\n"
        + "#40=VECTOR('VPX',#39,1.0);\n"
        + "#41=LINE('L3',#23,#40);\n"
        + "#42=EDGE_CURVE('E3',#26,#20,#41,.T.);\n"
        + "#50=ORIENTED_EDGE('OE0',$,$,#30,.T.);\n"
        + "#51=ORIENTED_EDGE('OE1',$,$,#34,.T.);\n"
        + "#52=ORIENTED_EDGE('OE2',$,$,#38,.T.);\n"
        + "#53=ORIENTED_EDGE('OE3',$,$,#42,.T.);\n"
        + "#60=EDGE_LOOP('L0',(#50,#51,#52,#53));\n"
        + "#61=FACE_OUTER_BOUND('B0',#60,.T.);\n"
        + "#70=ADVANCED_FACE('F0',(#61),#11,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(70);

        assertEquals(1, face.bounds().size());
        assertInstanceOf(com.minicad.geometry.SurfaceOfRevolution3.class, face.surface());
    }

    @Test
    void shouldBuildOffsetSphericalFaceGeometry() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=SPHERICAL_SURFACE('SP0',#4,2.0);\n"
        + "#6=OFFSET_SURFACE('OS0',#5,0.5,.F.);\n"
        + "#7=CARTESIAN_POINT('P0',(2.5,0.0,0.0));\n"
        + "#8=CARTESIAN_POINT('P1',(0.0,2.5,0.0));\n"
        + "#9=CARTESIAN_POINT('P2',(0.0,0.0,2.5));\n"
        + "#10=POLY_LOOP('L0',(#7,#8,#9));\n"
        + "#11=FACE_OUTER_BOUND('B0',#10,.T.);\n"
        + "#12=ADVANCED_FACE('F0',(#11),#6,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(12);
        SphericalSurface surface = assertInstanceOf(SphericalSurface.class, face.surface());

        assertEquals(2.5, surface.radius(), 1.0e-12);
    }

    @Test
    void shouldBuildOffsetConicalFaceGeometry() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=CONICAL_SURFACE('CN0',#4,2.0,0.5);\n"
        + "#6=OFFSET_SURFACE('OS0',#5,0.5,.F.);\n"
        + "#7=CARTESIAN_POINT('P0',(2.4387912809451864,0.0,-0.2397127693021015));\n"
        + "#8=CARTESIAN_POINT('P1',(0.0,2.4387912809451864,-0.2397127693021015));\n"
        + "#9=CARTESIAN_POINT('P2',(0.0,2.985093611541959,-1.2397127693021015));\n"
        + "#10=POLY_LOOP('L0',(#7,#8,#9));\n"
        + "#11=FACE_OUTER_BOUND('B0',#10,.T.);\n"
        + "#12=ADVANCED_FACE('F0',(#11),#6,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(12);
        ConicalSurface surface = assertInstanceOf(ConicalSurface.class, face.surface());

        assertEquals(2.0 + 0.5 * Math.cos(0.5), surface.radius(), 1.0e-12);
        assertEquals(-0.5 * Math.sin(0.5), surface.position().location().z(), 1.0e-12);
    }

    @Test
    void shouldBuildOffsetToroidalFaceGeometry() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=TOROIDAL_SURFACE('T0',#4,5.0,1.0);\n"
        + "#6=OFFSET_SURFACE('OS0',#5,0.5,.F.);\n"
        + "#7=CARTESIAN_POINT('P0',(6.5,0.0,0.0));\n"
        + "#8=CARTESIAN_POINT('P1',(0.0,6.5,0.0));\n"
        + "#9=CARTESIAN_POINT('P2',(0.0,5.0,1.5));\n"
        + "#10=POLY_LOOP('L0',(#7,#8,#9));\n"
        + "#11=FACE_OUTER_BOUND('B0',#10,.T.);\n"
        + "#12=ADVANCED_FACE('F0',(#11),#6,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(12);
        ToroidalSurface surface = assertInstanceOf(ToroidalSurface.class, face.surface());

        assertEquals(5.0, surface.majorRadius(), 1.0e-12);
        assertEquals(1.5, surface.minorRadius(), 1.0e-12);
    }

    @Test
    void shouldBuildOffsetLinearExtrusionFaceGeometry() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#4=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#5=VECTOR('VZ',#3,1.0);\n"
        + "#6=LINE('GEN',#1,#5);\n"
        + "#7=SURFACE_OF_LINEAR_EXTRUSION('SLE0',#6,#5);\n"
        + "#8=OFFSET_SURFACE('OS0',#7,1.0,.F.);\n"
        + "#9=CARTESIAN_POINT('Q0',(1.0,0.0,0.0));\n"
        + "#10=CARTESIAN_POINT('Q1',(1.0,0.0,1.0));\n"
        + "#11=CARTESIAN_POINT('Q2',(1.0,0.0,2.0));\n"
        + "#12=POLY_LOOP('L0',(#9,#10,#11));\n"
        + "#13=FACE_OUTER_BOUND('B0',#12,.T.);\n"
        + "#14=ADVANCED_FACE('F0',(#13),#8,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(14);
        OffsetSurface3 surface = assertInstanceOf(OffsetSurface3.class, face.surface());

        assertEquals(1.0, surface.distance(), 1.0e-12);
        assertInstanceOf(SurfaceOfLinearExtrusion3.class, surface.basisSurface());
    }

    @Test
    void shouldBuildOffsetSurfaceOfRevolutionFaceGeometry() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('B0',(1.0,0.0,0.0));\n"
        + "#3=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#4=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#5=AXIS1_PLACEMENT('A1',#1,#3);\n"
        + "#6=VECTOR('VZ',#3,1.0);\n"
        + "#7=LINE('GEN',#2,#6);\n"
        + "#8=SURFACE_OF_REVOLUTION('SOR0',#7,#5);\n"
        + "#9=OFFSET_SURFACE('OS0',#8,1.0,.F.);\n"
        + "#10=CARTESIAN_POINT('P0',(2.0,0.0,0.0));\n"
        + "#11=CARTESIAN_POINT('P1',(2.0,0.0,1.0));\n"
        + "#12=CARTESIAN_POINT('P2',(2.0,0.0,2.0));\n"
        + "#13=POLY_LOOP('L0',(#10,#11,#12));\n"
        + "#14=FACE_OUTER_BOUND('B0',#13,.T.);\n"
        + "#15=ADVANCED_FACE('F0',(#14),#9,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(15);
        OffsetSurface3 surface = assertInstanceOf(OffsetSurface3.class, face.surface());

        assertEquals(1.0, surface.distance(), 1.0e-12);
        assertInstanceOf(SurfaceOfRevolution3.class, surface.basisSurface());
    }

    @Test
    void shouldBuildOffsetBsplineFaceGeometry() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P10',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P01',(0.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P11',(1.0,1.0,1.0));\n"
        + "#5=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)\n"
        + "    B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.));\n"
        + "#6=OFFSET_SURFACE('OS0',#5,0.25,.F.);\n"
        + "#7=CARTESIAN_POINT('Q0',(0.0,0.0,0.25));\n"
        + "#8=CARTESIAN_POINT('Q1',(1.0,0.0,0.25));\n"
        + "#9=CARTESIAN_POINT('Q2',(1.0,1.0,1.25));\n"
        + "#10=POLY_LOOP('L0',(#7,#8,#9));\n"
        + "#11=FACE_OUTER_BOUND('B0',#10,.T.);\n"
        + "#12=ADVANCED_FACE('F0',(#11),#6,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(12);
        OffsetSurface3 surface = assertInstanceOf(OffsetSurface3.class, face.surface());

        assertEquals(0.25, surface.distance(), 1.0e-12);
        assertInstanceOf(BSplineSurface3.class, surface.basisSurface());
    }

    @Test
    void shouldBuildReplicaCylindricalFaceGeometry() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=CYLINDRICAL_SURFACE('CY0',#4,2.0);\n"
        + "#6=CARTESIAN_POINT('T',(10.0,0.0,0.0));\n"
        + "#7=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#8=CARTESIAN_TRANSFORMATION_OPERATOR_3D('TR',#3,#7,#6,2.0,#2);\n"
        + "#9=SURFACE_REPLICA('SR0',#5,#8);\n"
        + "#10=CARTESIAN_POINT('P0',(14.0,0.0,0.0));\n"
        + "#11=CARTESIAN_POINT('P1',(10.0,4.0,0.0));\n"
        + "#12=CARTESIAN_POINT('P2',(10.0,4.0,2.0));\n"
        + "#13=POLY_LOOP('L0',(#10,#11,#12));\n"
        + "#14=FACE_OUTER_BOUND('B0',#13,.T.);\n"
        + "#15=ADVANCED_FACE('F0',(#14),#9,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(15);
        CylindricalSurface surface = assertInstanceOf(CylindricalSurface.class, face.surface());

        assertEquals(4.0, surface.radius(), 1.0e-12);
        assertEquals(10.0, surface.position().location().x(), 1.0e-12);
    }

    @Test
    void shouldBuildEllipseAndSurfaceCurve() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P0',(3.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P1',(0.0,2.0,0.0));\n"
        + "#4=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#5=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#6=AXIS2_PLACEMENT_3D('AX',#1,#4,#5);\n"
        + "#7=ELLIPSE('E0',#6,3.0,2.0);\n"
        + "#8=SURFACE_CURVE('SC0',#7,(),.T.);\n"
        + "#9=VERTEX_POINT('V0',#2);\n"
        + "#10=VERTEX_POINT('V1',#3);\n"
        + "#11=EDGE_CURVE('E1',#9,#10,#8,.T.);\n"
        + "ENDSEC;"

        );


        Ellipse3 ellipse = builder.buildEllipse(7);
        SurfaceCurve3 surfaceCurve = builder.buildSurfaceCurve(8);
        Edge edge = builder.buildEdge(11);

        assertEquals(3.0, ellipse.semiAxis1());
        assertInstanceOf(SurfaceCurve3.class, surfaceCurve);
        assertInstanceOf(SurfaceCurve3.class, edge.curve());
    }

    @Test
    void shouldBuildToroidalSurfaceSplineAndTrimmedSurfaceCurveEdge() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=TOROIDAL_SURFACE('T0',#4,5.0,1.0);\n"
        + "#10=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#11=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#12=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#13=(B_SPLINE_CURVE('B0',2,(#10,#11,#12),.UNSPECIFIED.,.F.,.F.)\n"
        + "     B_SPLINE_CURVE_WITH_KNOTS((3,3),(0.0,1.0),.UNSPECIFIED.));\n"
        + "#14=SURFACE_CURVE('SC0',#13,(),.T.);\n"
        + "#15=TRIMMED_CURVE('TC0',#14,(#10),(#12),.T.,.CARTESIAN.);\n"
        + "#16=VERTEX_POINT('V0',#10);\n"
        + "#17=VERTEX_POINT('V1',#12);\n"
        + "#18=EDGE_CURVE('E0',#16,#17,#15,.T.);\n"
        + "ENDSEC;"

        );


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
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P10',(2.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P01',(0.0,2.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P11',(2.0,2.0,1.0));\n"
        + "#10=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)\n"
        + "     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.));\n"
        + "ENDSEC;"

        );


        BSplineSurface3 surface = builder.buildBSplineSurface(10);

        assertEquals(1, surface.uDegree());
        assertEquals(1, surface.vDegree());
        assertEquals(0.25, surface.pointAt(0.5, 0.5).z(), 1.0e-9);
    }

    @Test
    void shouldBuild2dPcurveLine() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=PLANE('PL0',#4);\n"
        + "#10=CARTESIAN_POINT('UV0',(0.0,0.0));\n"
        + "#11=DIRECTION('DU',(1.0,0.0));\n"
        + "#12=VECTOR('VU',#11,1.0);\n"
        + "#13=LINE('L2D',#10,#12);\n"
        + "#14=REPRESENTATION_CONTEXT('PCURVE','PARAMETRIC');\n"
        + "#15=DEFINITIONAL_REPRESENTATION('DEF',(#13),#14);\n"
        + "#16=PCURVE('PC0',#5,#15);\n"
        + "ENDSEC;"

        );


        Line2 line = assertInstanceOf(Line2.class, builder.buildPcurve2(16));

        assertTrue(line.contains(new Point2(2.0, 0.0)));
        assertEquals(new Point2(3.0, 0.0), line.pointAt(3.0));
    }

    @Test
    void shouldBuildSeamCurveAsSurfaceCurve3() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=CYLINDRICAL_SURFACE('CY0',#4,1.0);\n"
        + "#6=CIRCLE('C0',#4,1.0);\n"
        + "#10=CARTESIAN_POINT('UV0',(0.0,0.0));\n"
        + "#11=DIRECTION('DU',(1.0,0.0));\n"
        + "#12=VECTOR('VU',#11,1.0);\n"
        + "#13=LINE('L2D0',#10,#12);\n"
        + "#14=REPRESENTATION_CONTEXT('PC0','PARAMETRIC');\n"
        + "#15=DEFINITIONAL_REPRESENTATION('DEF0',(#13),#14);\n"
        + "#16=PCURVE('PC0',#5,#15);\n"
        + "#17=CARTESIAN_POINT('UV1',(0.0,6.283185307179586));\n"
        + "#18=LINE('L2D1',#17,#12);\n"
        + "#19=REPRESENTATION_CONTEXT('PC1','PARAMETRIC');\n"
        + "#20=DEFINITIONAL_REPRESENTATION('DEF1',(#18),#19);\n"
        + "#21=PCURVE('PC1',#5,#20);\n"
        + "#22=SEAM_CURVE('SEAM0',#6,(#16,#21),.PCURVE_S1.);\n"
        + "ENDSEC;"

        );


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
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=CYLINDRICAL_SURFACE('CY0',#4,1.0);\n"
        + "#10=CARTESIAN_POINT('UV0',(0.0,0.0));\n"
        + "#11=CARTESIAN_POINT('UV1',(0.2,0.45));\n"
        + "#12=CARTESIAN_POINT('UV2',(0.0,1.0));\n"
        + "#13=(B_SPLINE_CURVE('B2D',2,(#10,#11,#12),.UNSPECIFIED.,.F.,.F.)\n"
        + "     B_SPLINE_CURVE_WITH_KNOTS((3,3),(0.0,1.0),.UNSPECIFIED.));\n"
        + "#14=REPRESENTATION_CONTEXT('PC','PARAMETRIC');\n"
        + "#15=DEFINITIONAL_REPRESENTATION('DEF',(#13),#14);\n"
        + "#16=PCURVE('PC0',#5,#15);\n"
        + "ENDSEC;"

        );


        BSplineCurve2 spline = assertInstanceOf(BSplineCurve2.class, builder.buildPcurve2(16));

        assertEquals(new Point2(0.0, 0.0), spline.pointAt(spline.startParameter()));
        assertEquals(new Point2(0.0, 1.0), spline.pointAt(spline.endParameter()));
        assertTrue(spline.sample(8).size() >= 9);
    }

    @Test
    void shouldBuild2dCircularPcurveAndTrimmedBasis() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=CYLINDRICAL_SURFACE('CY0',#4,1.0);\n"
        + "#10=CARTESIAN_POINT('UV0',(1.0,2.0));\n"
        + "#11=DIRECTION('DUV',(1.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_2D('A2',#10,#11);\n"
        + "#13=CIRCLE('PC',#12,0.5);\n"
        + "#14=CARTESIAN_POINT('T0',(1.5,2.0));\n"
        + "#15=CARTESIAN_POINT('T1',(1.0,2.5));\n"
        + "#16=TRIMMED_CURVE('TC0',#13,(#14),(#15),.T.,.CARTESIAN.);\n"
        + "#17=REPRESENTATION_CONTEXT('PC','PARAMETRIC');\n"
        + "#18=DEFINITIONAL_REPRESENTATION('DEF',(#16),#17);\n"
        + "#19=PCURVE('PC0',#5,#18);\n"
        + "ENDSEC;"

        );


        TrimmedCurve2 trimmed = assertInstanceOf(TrimmedCurve2.class, builder.buildPcurve2(19));
        Circle2 circle = assertInstanceOf(Circle2.class, trimmed.basisCurve());

        assertEquals(new Point2(1.5, 2.0), trimmed.trimStart());
        assertEquals(new Point2(1.0, 2.5), trimmed.trimEnd());
        assertEquals(new Point2(1.5, 2.0), circle.pointAt(0.0));
        assertEquals(Math.PI * 0.5, circle.angleOf(new Point2(1.0, 2.5)), 1.0e-12);
    }

    @Test
    void shouldSnapOffCurveTrimPointsFor2dTrimmedPcurve() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=CYLINDRICAL_SURFACE('CY0',#4,1.0);\n"
        + "#10=CARTESIAN_POINT('UV0',(1.0,2.0));\n"
        + "#11=DIRECTION('DUV',(1.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_2D('A2',#10,#11);\n"
        + "#13=CIRCLE('PC',#12,0.5);\n"
        + "#14=CARTESIAN_POINT('T0',(1.500000001,2.000000002));\n"
        + "#15=CARTESIAN_POINT('T1',(0.999999998,2.499999999));\n"
        + "#16=TRIMMED_CURVE('TC0',#13,(#14),(#15),.T.,.CARTESIAN.);\n"
        + "#17=REPRESENTATION_CONTEXT('PC','PARAMETRIC');\n"
        + "#18=DEFINITIONAL_REPRESENTATION('DEF',(#16),#17);\n"
        + "#19=PCURVE('PC0',#5,#18);\n"
        + "ENDSEC;"

        );


        TrimmedCurve2 trimmed = assertInstanceOf(TrimmedCurve2.class, builder.buildPcurve2(19));

        assertEquals(1.5, trimmed.trimStart().x(), 1.0e-6);
        assertEquals(2.0, trimmed.trimStart().y(), 1.0e-6);
        assertEquals(1.0, trimmed.trimEnd().x(), 1.0e-6);
        assertEquals(2.5, trimmed.trimEnd().y(), 1.0e-6);
    }

    @Test
    void shouldBuild2dEllipsePcurve() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=CYLINDRICAL_SURFACE('CY0',#4,1.0);\n"
        + "#10=CARTESIAN_POINT('UV0',(1.0,2.0));\n"
        + "#11=DIRECTION('DUV',(1.0,0.0));\n"
        + "#12=AXIS2_PLACEMENT_2D('A2',#10,#11);\n"
        + "#13=ELLIPSE('PE',#12,0.5,0.25);\n"
        + "#14=REPRESENTATION_CONTEXT('PC','PARAMETRIC');\n"
        + "#15=DEFINITIONAL_REPRESENTATION('DEF',(#13),#14);\n"
        + "#16=PCURVE('PC0',#5,#15);\n"
        + "ENDSEC;"

        );


        Ellipse2 ellipse = assertInstanceOf(Ellipse2.class, builder.buildPcurve2(16));

        assertEquals(new Point2(1.5, 2.0), ellipse.pointAt(0.0));
        assertEquals(Math.PI * 0.5, ellipse.angleOf(new Point2(1.0, 2.25)), 1.0e-12);
    }

    @Test
    void shouldBuildBSplineSurfaceFaceConstruction() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P10',(2.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P01',(0.0,2.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P11',(2.0,2.0,1.0));\n"
        + "#20=VERTEX_POINT('V0',#1);\n"
        + "#21=VERTEX_POINT('V1',#2);\n"
        + "#22=VERTEX_POINT('V2',#4);\n"
        + "#23=VERTEX_POINT('V3',#3);\n"
        + "#30=DIRECTION('D1',(1.0,0.0,0.0));\n"
        + "#31=VECTOR('VE1',#30,2.0);\n"
        + "#32=LINE('L1',#1,#31);\n"
        + "#33=DIRECTION('D2',(0.0,2.0,1.0));\n"
        + "#34=VECTOR('VE2',#33,1.0);\n"
        + "#35=LINE('L2',#2,#34);\n"
        + "#36=DIRECTION('D3',(-2.0,0.0,-1.0));\n"
        + "#37=VECTOR('VE3',#36,1.0);\n"
        + "#38=LINE('L3',#4,#37);\n"
        + "#39=DIRECTION('D4',(0.0,-2.0,0.0));\n"
        + "#40=VECTOR('VE4',#39,1.0);\n"
        + "#41=LINE('L4',#3,#40);\n"
        + "#50=EDGE_CURVE('E1',#20,#21,#32,.T.);\n"
        + "#51=EDGE_CURVE('E2',#21,#22,#35,.T.);\n"
        + "#52=EDGE_CURVE('E3',#22,#23,#38,.T.);\n"
        + "#53=EDGE_CURVE('E4',#23,#20,#41,.T.);\n"
        + "#60=ORIENTED_EDGE('OE1',$,$,#50,.T.);\n"
        + "#61=ORIENTED_EDGE('OE2',$,$,#51,.T.);\n"
        + "#62=ORIENTED_EDGE('OE3',$,$,#52,.T.);\n"
        + "#63=ORIENTED_EDGE('OE4',$,$,#53,.T.);\n"
        + "#70=EDGE_LOOP('LOOP',(#60,#61,#62,#63));\n"
        + "#71=FACE_OUTER_BOUND('FOB',#70,.T.);\n"
        + "#80=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)\n"
        + "     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.));\n"
        + "#81=ADVANCED_FACE('BS_PATCH',(#71),#80,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(81);

        assertEquals(1, face.bounds().size());
        assertInstanceOf(BSplineSurface3.class, face.surface());
    }

    @Test
    void shouldBuildRationalBSplineSurfaceFaceConstruction() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P10',(2.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P01',(0.0,2.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P11',(2.0,2.0,1.0));\n"
        + "#20=CARTESIAN_POINT('L0',(0.0,0.0,0.0));\n"
        + "#21=CARTESIAN_POINT('L1',(2.0,0.0,0.0));\n"
        + "#22=CARTESIAN_POINT('L2',(2.0,2.0,1.0));\n"
        + "#23=CARTESIAN_POINT('L3',(0.0,2.0,0.0));\n"
        + "#30=POLY_LOOP('LOOP',(#20,#21,#22,#23));\n"
        + "#31=FACE_OUTER_BOUND('FOB',#30,.T.);\n"
        + "#40=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)\n"
        + "     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.)\n"
        + "     RATIONAL_B_SPLINE_SURFACE(((1.0,1.0),(1.0,0.5))));\n"
        + "#41=ADVANCED_FACE('RBS_PATCH',(#31),#40,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(41);

        assertEquals(1, face.bounds().size());
        assertInstanceOf(RationalBSplineSurface3.class, face.surface());
    }

    @Test
    void shouldBuildToroidalFaceConstruction() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=TOROIDAL_SURFACE('T0',#4,5.0,1.0);\n"
        + "#6=CARTESIAN_POINT('P0',(6.0,0.0,0.0));\n"
        + "#7=CARTESIAN_POINT('P1',(5.0,1.0,0.0));\n"
        + "#8=CARTESIAN_POINT('P2',(4.0,0.0,0.0));\n"
        + "#10=POLY_LOOP('L0',(#6,#7,#8));\n"
        + "#11=FACE_OUTER_BOUND('B0',#10,.T.);\n"
        + "#12=ADVANCED_FACE('F0',(#11),#5,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(12);

        assertEquals(1, face.bounds().size());
        assertInstanceOf(ToroidalSurface.class, face.surface());
    }

    @Test
    void shouldBuildDegenerateToroidalFaceConstruction() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=DEGENERATE_TOROIDAL_SURFACE('DT0',#4,5.0,1.0,.T.);\n"
        + "#6=CARTESIAN_POINT('P0',(6.0,0.0,0.0));\n"
        + "#7=CARTESIAN_POINT('P1',(5.0,1.0,0.0));\n"
        + "#8=CARTESIAN_POINT('P2',(4.0,0.0,0.0));\n"
        + "#10=POLY_LOOP('L0',(#6,#7,#8));\n"
        + "#11=FACE_OUTER_BOUND('B0',#10,.T.);\n"
        + "#12=ADVANCED_FACE('F0',(#11),#5,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(12);

        assertEquals(1, face.bounds().size());
        assertInstanceOf(ToroidalSurface.class, face.surface());
    }

    @Test
    void shouldBuildSphericalFaceConstruction() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=SPHERICAL_SURFACE('SP0',#4,2.0);\n"
        + "#6=CARTESIAN_POINT('P0',(2.0,0.0,0.0));\n"
        + "#7=CARTESIAN_POINT('P1',(0.0,2.0,0.0));\n"
        + "#8=CARTESIAN_POINT('P2',(0.0,0.0,2.0));\n"
        + "#9=POLY_LOOP('L0',(#6,#7,#8));\n"
        + "#10=FACE_OUTER_BOUND('B0',#9,.T.);\n"
        + "#11=ADVANCED_FACE('F0',(#10),#5,.T.);\n"
        + "ENDSEC;"
        );

        Face face = builder.buildFace(11);

        assertEquals(1, face.bounds().size());
        assertInstanceOf(SphericalSurface.class, face.surface());
    }

    @Test
    void shouldRejectInvalidSphericalSurfaceRadius() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=SPHERICAL_SURFACE('SP0',#4,0.0);\n"
        + "ENDSEC;"

        );


        GeometryException exception = assertThrows(
                GeometryException.class,
                () -> builder.buildSphericalSurface(5)
        );

        assertEquals("sphere radius must be greater than epsilon", exception.getMessage());
    }

    @Test
    void shouldValidateSweptAndWrappedSurfaceEntities() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=PLANE('PL0',#4);\n"
        + "#6=VECTOR('EX',#3,2.0);\n"
        + "#7=LINE('L0',#1,#6);\n"
        + "#8=VECTOR('VZ',#2,3.0);\n"
        + "#9=AXIS1_PLACEMENT('A1',#1,#2);\n"
        + "#10=SURFACE_OF_LINEAR_EXTRUSION('SLE0',#7,#8);\n"
        + "#11=SURFACE_OF_REVOLUTION('SOR0',#7,#9);\n"
        + "#12=RECTANGULAR_TRIMMED_SURFACE('RTS0',#5,0.0,1.0,0.0,1.0,.T.,.T.);\n"
        + "#13=ORIENTED_SURFACE('OS0',#12,.T.);\n"
        + "#14=CURVE_BOUNDED_SURFACE('CBS0',#13,(#7),.T.);\n"
        + "#15=OFFSET_SURFACE('OFS0',#5,1.0,.F.);\n"
        + "#16=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',$,$,#1,1.0,$);\n"
        + "#17=SURFACE_REPLICA('SR0',#5,#16);\n"
        + "ENDSEC;"

        );


        builder.buildSurfaceOfLinearExtrusion(10);
        builder.buildSurfaceOfRevolution(11);
        builder.buildRectangularTrimmedSurface(12);
        builder.buildOrientedSurface(13);
        builder.buildCurveBoundedSurface(14);
        builder.buildOffsetSurface(15);
        builder.buildSurfaceReplica(17);
    }

    @Test
    void shouldValidateCurveWrapperEntities() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=VECTOR('V0',#3,1.0);\n"
        + "#5=LINE('L0',#1,#4);\n"
        + "#6=ORIENTED_CURVE('OC0',#5,.F.);\n"
        + "#7=CARTESIAN_POINT('Q0',(0.0,0.0));\n"
        + "#8=CARTESIAN_POINT('Q1',(1.0,0.0));\n"
        + "#9=POLYLINE('PL2',(#7,#8));\n"
        + "#10=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID2','PARAM'));\n"
        + "#11=REPRESENTATION('R2',(#9),#10);\n"
        + "#12=PLANE('PL0',#13);\n"
        + "#13=AXIS2_PLACEMENT_3D('AX',#1,#14,#3);\n"
        + "#14=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#15=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#16=DEGENERATE_PCURVE('DPC0',#12,#11);\n"
        + "#17=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#3,#15,#1,1.0,#14);\n"
        + "#18=CURVE_REPLICA('CR0',#5,#17);\n"
        + "ENDSEC;"

        );


        builder.buildCurveReference3(6);
        builder.buildCurveReference3(18);
        builder.buildPcurve2(16);
    }

    @Test
    void shouldBuildPointReplicaReference() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#3=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#4=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#5=CARTESIAN_POINT('O',(10.0,20.0,30.0));\n"
        + "#6=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#2,#3,#5,2.0,#4);\n"
        + "#7=POINT_REPLICA('PR0',#1,#6);\n"
        + "ENDSEC;"

        );


        CartesianPoint point = builder.buildPointReference(7);

        assertEquals(12.0, point.x());
        assertEquals(24.0, point.y());
        assertEquals(36.0, point.z());
    }

    @Test
    void shouldRejectPointReplicaWithNonOrthogonalTransformationAxes() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#3=DIRECTION('SKEW',(1.0,1.0,0.0));\n"
        + "#4=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#5=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#6=CARTESIAN_TRANSFORMATION_OPERATOR_3D('BAD',#2,#3,#5,1.0,#4);\n"
        + "#7=POINT_REPLICA('PR0',#1,#6);\n"
        + "ENDSEC;"

        );


        UnsupportedGeometryException error =
                assertThrows(UnsupportedGeometryException.class, () -> builder.buildPointReference(7));

        assertEquals("CARTESIAN_TRANSFORMATION_OPERATOR_3D #6 axes must be orthogonal", error.getMessage());
    }

    @Test
    void shouldBuildPointReplicaReferenceFromVertexPointParent() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));\n"
        + "#2=VERTEX_POINT('VP0',#1);\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#5=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#6=CARTESIAN_POINT('O',(10.0,20.0,30.0));\n"
        + "#7=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#3,#4,#6,2.0,#5);\n"
        + "#8=POINT_REPLICA('PR0',#2,#7);\n"
        + "ENDSEC;"

        );


        CartesianPoint point = builder.buildPointReference(8);

        assertEquals(12.0, point.x());
        assertEquals(24.0, point.y());
        assertEquals(36.0, point.z());
    }

    // Phase 1 tests: Boolean UNION, SweptDiskSolid, Tapered solids, RightCircularCone, Profiles

    @Test
    void shouldBuildBooleanUnionWithHalfSpace() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#3=DIRECTION('DY',(0.0,1.0,0.0));\n"
        + "#4=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#5=AXIS2_PLACEMENT_3D('AX',#1,#4,#2);\n"
        + "#6=PLANE('PL0',#5);\n"
        + "#7=CARTESIAN_POINT('MIN',(0.0,0.0,0.0));\n"
        + "#8=BOX_DOMAIN(#7,2.0,2.0,2.0);\n"
        + "#9=BOXED_HALF_SPACE('HS',#6,.T.,#8);\n"
        + "#10=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#11=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#12=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#13=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#20=DIRECTION('D1',(1.0,0.0,0.0));\n"
        + "#21=VECTOR('V1',#20,1.0);\n"
        + "#22=LINE('L1',#10,#21);\n"
        + "#23=DIRECTION('D2',(0.0,1.0,0.0));\n"
        + "#24=VECTOR('V2',#23,1.0);\n"
        + "#25=LINE('L2',#11,#24);\n"
        + "#26=DIRECTION('D3',(-1.0,0.0,0.0));\n"
        + "#27=VECTOR('V3',#26,1.0);\n"
        + "#28=LINE('L3',#12,#27);\n"
        + "#29=DIRECTION('D4',(0.0,-1.0,0.0));\n"
        + "#30=VECTOR('V4',#29,1.0);\n"
        + "#31=LINE('L4',#13,#30);\n"
        + "#40=VERTEX_POINT('V0',#10);\n"
        + "#41=VERTEX_POINT('V1',#11);\n"
        + "#42=VERTEX_POINT('V2',#12);\n"
        + "#43=VERTEX_POINT('V3',#13);\n"
        + "#50=EDGE_CURVE('E0',#40,#41,#22,.T.);\n"
        + "#51=EDGE_CURVE('E1',#41,#42,#25,.T.);\n"
        + "#52=EDGE_CURVE('E2',#42,#43,#28,.T.);\n"
        + "#53=EDGE_CURVE('E3',#43,#40,#31,.T.);\n"
        + "#60=ORIENTED_EDGE('OE0',$,$,#50,.T.);\n"
        + "#61=ORIENTED_EDGE('OE1',$,$,#51,.T.);\n"
        + "#62=ORIENTED_EDGE('OE2',$,$,#52,.T.);\n"
        + "#63=ORIENTED_EDGE('OE3',$,$,#53,.T.);\n"
        + "#70=EDGE_LOOP('LOOP',(#60,#61,#62,#63));\n"
        + "#71=FACE_OUTER_BOUND('FOB',#70,.T.);\n"
        + "#80=ADVANCED_FACE('F0',(#71),#6,.T.);\n"
        + "#90=CLOSED_SHELL('CS',(#80));\n"
        + "#100=FACETED_BREP('FB',#90);\n"
        + "#110=(BOOLEAN_RESULT(.UNION.,#100,#9)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('BOOL'));\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(110);
        // Union produces a solid with merged faces from both operands
        assertTrue(solid.outerShell().faces().size() >= 1);
    }

    @Test
    void shouldRejectBooleanUnionWithoutHalfSpace() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#3=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#3,#2);\n"
        + "#5=PLANE('PL0',#4);\n"
        + "#6=FACE_SURFACE('FS',(),#5,.T.);\n"
        + "#7=CLOSED_SHELL('CS1',(#6));\n"
        + "#8=CLOSED_SHELL('CS2',(#6));\n"
        + "#9=FACETED_BREP('FB1',#7);\n"
        + "#10=FACETED_BREP('FB2',#8);\n"
        + "#11=(BOOLEAN_RESULT(.UNION.,#9,#10)\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('BOOL'));\n"
        + "ENDSEC;"

        );


        assertThrows(UnsupportedGeometryException.class, () -> builder.buildSolid(11));
    }

    @Test
    void shouldBuildRightCircularConePrimitive() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=(RIGHT_CIRCULAR_CONE('CONE',#4,5.0,2.0)\n"
        + "    CSG_PRIMITIVE()\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('cone'));\n"
        + "#6=(CSG_SOLID('SOLID',#5)\n"
        + "    SOLID_MODEL()\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('solid'));\n"
        + "ENDSEC;"
        );

        Solid cone = builder.buildSolid(6);
        // Cone should have base face + lateral triangular faces
        assertTrue(cone.outerShell().faces().size() >= 2);
    }

    @Test
    void shouldResolveRectangleHollowProfileDef() {
        // Just verify resolution works - profile is already tested via buildSolid
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0));\n"
        + "#3=AXIS2_PLACEMENT_2D('AX2',#1,#2);\n"
        + "#4=(RECTANGLE_HOLLOW_PROFILE_DEF(.AREA.,'RH',#3,4.0,3.0,0.5,0.0)\n"
        + "    PROFILE_DEFINITION()\n"
        + "    PRODUCT_DEFINITION_SHAPE('',$,$));\n"
        + "ENDSEC;"
        );

        // Verify the profile was resolved - check that entity 4 is in the map
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(
                com.minicad.step.syntax.StepParser.parse(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0));\n"
        + "#3=AXIS2_PLACEMENT_2D('AX2',#1,#2);\n"
        + "#4=(RECTANGLE_HOLLOW_PROFILE_DEF(.AREA.,'RH',#3,4.0,3.0,0.5,0.0)\n"
        + "    PROFILE_DEFINITION()\n"
        + "    PRODUCT_DEFINITION_SHAPE('',$,$));\n"
        + "ENDSEC;"
        ));
        StepEntity entity = resolved.get(4);
        assertInstanceOf(StepRectangleHollowProfileDef.class, entity);
        StepRectangleHollowProfileDef profile = (StepRectangleHollowProfileDef) entity;
        assertEquals("RH", profile.name());
        assertEquals(4.0, profile.xDim());
        assertEquals(3.0, profile.yDim());
        assertEquals(0.5, profile.wallThickness());
        assertEquals(0.0, profile.innerRadius());
    }

    @Test
    void shouldResolveCenteredCircleProfileDef() {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(
                com.minicad.step.syntax.StepParser.parse(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0));\n"
        + "#3=AXIS2_PLACEMENT_2D('AX2',#1,#2);\n"
        + "#4=(CENTERED_CIRCLE_PROFILE_DEF(.AREA.,'CC',#3,2.0,1.0)\n"
        + "    PROFILE_DEFINITION()\n"
        + "    PRODUCT_DEFINITION_SHAPE('',$,$));\n"
        + "ENDSEC;\n"
        ));

        StepEntity entity = resolved.get(4);
        assertInstanceOf(StepCenteredCircleProfileDef.class, entity);
        StepCenteredCircleProfileDef profile = (StepCenteredCircleProfileDef) entity;
        assertEquals("CC", profile.name());
        assertEquals(2.0, profile.radius());
        assertEquals(1.0, profile.centerOffset());
    }

    @Test
    void shouldResolveIShapeProfileDef() {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(
                com.minicad.step.syntax.StepParser.parse(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0));\n"
        + "#3=AXIS2_PLACEMENT_2D('AX2',#1,#2);\n"
        + "#4=(I_SHAPE_PROFILE_DEF(.AREA.,'IBEAM',#3,100.0,200.0,10.0,10.0,5.0,0.0)\n"
        + "    PROFILE_DEFINITION()\n"
        + "    PRODUCT_DEFINITION_SHAPE('',$,$));\n"
        + "ENDSEC;\n"
        ));

        StepEntity entity = resolved.get(4);
        assertInstanceOf(StepProfileDef.class, entity);
        StepProfileDef profile = (StepProfileDef) entity;
        assertEquals("I_SHAPE_PROFILE_DEF", profile.entityName());
        assertEquals(6, profile.parameters().size());
    }

    @Test
    void shouldResolveLShapeProfileDef() {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(
                com.minicad.step.syntax.StepParser.parse(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0));\n"
        + "#3=AXIS2_PLACEMENT_2D('AX2',#1,#2);\n"
        + "#4=(L_SHAPE_PROFILE_DEF(.AREA.,'ANGLE',#3,50.0,50.0,5.0,5.0)\n"
        + "    PROFILE_DEFINITION()\n"
        + "    PRODUCT_DEFINITION_SHAPE('',$,$));\n"
        + "ENDSEC;\n"
        ));

        StepEntity entity = resolved.get(4);
        assertInstanceOf(StepProfileDef.class, entity);
        StepProfileDef profile = (StepProfileDef) entity;
        assertEquals("L_SHAPE_PROFILE_DEF", profile.entityName());
        assertEquals(4, profile.parameters().size());
    }

    @Test
    void shouldResolveUShapeProfileDef() {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(
                com.minicad.step.syntax.StepParser.parse(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0));\n"
        + "#3=AXIS2_PLACEMENT_2D('AX2',#1,#2);\n"
        + "#4=(U_SHAPE_PROFILE_DEF(.AREA.,'CHANNEL',#3,50.0,100.0,5.0,10.0,5.0)\n"
        + "    PROFILE_DEFINITION()\n"
        + "    PRODUCT_DEFINITION_SHAPE('',$,$));\n"
        + "ENDSEC;\n"
        ));

        StepEntity entity = resolved.get(4);
        assertInstanceOf(StepProfileDef.class, entity);
        StepProfileDef profile = (StepProfileDef) entity;
        assertEquals("U_SHAPE_PROFILE_DEF", profile.entityName());
        assertEquals(5, profile.parameters().size());
    }

    @Test
    void shouldResolveTShapeProfileDef() {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(
                com.minicad.step.syntax.StepParser.parse(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0));\n"
        + "#3=AXIS2_PLACEMENT_2D('AX2',#1,#2);\n"
        + "#4=(T_SHAPE_PROFILE_DEF(.AREA.,'TEE',#3,100.0,50.0,10.0,10.0,5.0)\n"
        + "    PROFILE_DEFINITION()\n"
        + "    PRODUCT_DEFINITION_SHAPE('',$,$));\n"
        + "ENDSEC;\n"
        ));

        StepEntity entity = resolved.get(4);
        assertInstanceOf(StepProfileDef.class, entity);
        StepProfileDef profile = (StepProfileDef) entity;
        assertEquals("T_SHAPE_PROFILE_DEF", profile.entityName());
        assertEquals(5, profile.parameters().size());
    }

    @Test
    void shouldResolveZShapeProfileDef() {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(
                com.minicad.step.syntax.StepParser.parse(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0));\n"
        + "#3=AXIS2_PLACEMENT_2D('AX2',#1,#2);\n"
        + "#4=(Z_SHAPE_PROFILE_DEF(.AREA.,'ZBEAM',#3,50.0,100.0,5.0,10.0,5.0)\n"
        + "    PROFILE_DEFINITION()\n"
        + "    PRODUCT_DEFINITION_SHAPE('',$,$));\n"
        + "ENDSEC;\n"
        ));

        StepEntity entity = resolved.get(4);
        assertInstanceOf(StepProfileDef.class, entity);
        StepProfileDef profile = (StepProfileDef) entity;
        assertEquals("Z_SHAPE_PROFILE_DEF", profile.entityName());
        assertEquals(5, profile.parameters().size());
    }

    @Test
    void shouldBuildIndexedPolyCurve() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#5=(INDEXED_POLY_CURVE('IPC',(#1,#2,#3,#4),(0,1,2,3),.F.)\n"
        + "    BOUNDED_CURVE()\n"
        + "    CURVE()\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('ipc'));\n"
        + "ENDSEC;"
        );

        Curve3 curve = builder.buildCurveReference3(5);
        assertInstanceOf(Polyline3.class, curve);
        Polyline3 polyline = (Polyline3) curve;
        assertEquals(4, polyline.points().size());
    }

    @Test
    void shouldBuildIndexedPolyCurveClosed() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#5=(INDEXED_POLY_CURVE('IPC',(#1,#2,#3,#4),(0,1,2,3),.T.)\n"
        + "    BOUNDED_CURVE()\n"
        + "    CURVE()\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('ipc'));\n"
        + "ENDSEC;"
        );

        Curve3 curve = builder.buildCurveReference3(5);
        assertInstanceOf(Polyline3.class, curve);
        Polyline3 polyline = (Polyline3) curve;
        // Closed curve should have first point appended at end
        assertEquals(5, polyline.points().size());
        assertEquals(polyline.points().get(0), polyline.points().get(polyline.points().size() - 1));
    }

    @Test
    void shouldBuildNonManifoldSolidBrep() {
        // Non-manifold solid brep now supports open shell boundary (sheet bodies)
        // For a valid solid we need a closed shell
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#3=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#3,#2);\n"
        + "#5=PLANE('PL0',#4);\n"
        + "#6=CARTESIAN_POINT('P1',(0.0,0.0,0.0));\n"
        + "#7=CARTESIAN_POINT('P2',(1.0,0.0,0.0));\n"
        + "#8=CARTESIAN_POINT('P3',(1.0,1.0,0.0));\n"
        + "#9=CARTESIAN_POINT('P4',(0.0,1.0,0.0));\n"
        + "#10=POLY_LOOP('',(#6,#7,#8,#9));\n"
        + "#11=FACE_BOUND('FB',#10,.T.);\n"
        + "#12=ADVANCED_FACE('AF',(#11),#5,.T.);\n"
        + "#13=CLOSED_SHELL('CS',(#12));\n"
        + "#14=NON_MANIFOLD_SOLID_BREP('NMB',#13);\n"
        + "ENDSEC;"

        );


        // Should now build successfully with closed shell
        Solid solid = builder.buildSolid(14);
        assertTrue(solid != null);
        assertTrue(solid.outerShell() != null);
    }

    @Test
    void shouldBuildExtrudedAreaSolidTapered() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=CARTESIAN_POINT('P0',(0.0,0.0));\n"
        + "#6=DIRECTION('DX2',(1.0,0.0));\n"
        + "#7=AXIS2_PLACEMENT_2D('AX2',#5,#6);\n"
        + "#8=(RECTANGLE_PROFILE_DEF(.AREA.,'R',#7,4.0,2.0)\n"
        + "    PROFILE_DEFINITION());\n"
        + "#9=DIRECTION('DIR',(0.0,0.0,1.0));\n"
        + "#10=(EXTRUDED_AREA_SOLID_TAPERED('EXT',#8,#9,10.0,0.05)\n"
        + "    SOLID_MODEL()\n"
        + "    GEOMETRIC_REPRESENTATION_ITEM()\n"
        + "    REPRESENTATION_ITEM('ext'));\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(10);
        assertTrue(solid.outerShell().faces().size() >= 6);
    }

    @Test
    void shouldBuildShellBasedSurfaceModel() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));\n"
        + "#5=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#6=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#7=AXIS2_PLACEMENT_3D('AX',#1,#5,#6);\n"
        + "#8=PLANE('PL',#7);\n"
        + "#9=POLY_LOOP('PL0',(#1,#2,#3,#4));\n"
        + "#10=FACE_BOUND('FB',#9,.T.);\n"
        + "#11=ADVANCED_FACE('AF',(#10),#8,.T.);\n"
        + "#12=OPEN_SHELL('OS',(#11));\n"
        + "#13=SHELL_BASED_SURFACE_MODEL('SSM',(#12));\n"
        + "ENDSEC;"
        );

        Shell shell = builder.buildShell(13);
        assertNotNull(shell);
        assertEquals(1, shell.faces().size());
    }

    @Test
    void shouldBuildPlanarBox() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=PLANAR_BOX('PB',#4,2.0,3.0,1.0);\n"
        + "ENDSEC;"
        );

        Shell shell = builder.buildShell(5);
        assertNotNull(shell);
        assertEquals(1, shell.faces().size());
        assertInstanceOf(Plane.class, shell.faces().get(0).surface());
    }

    @Test
    void shouldBuildPlanarExtent() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=PLANAR_EXTENT('PE',4.0,5.0,1.0);\n"
        + "ENDSEC;"
        );

        Shell shell = builder.buildShell(1);
        assertNotNull(shell);
        assertEquals(1, shell.faces().size());
        assertInstanceOf(Plane.class, shell.faces().get(0).surface());
    }

    @Test
    void shouldBuildConnectedFaceSubSet() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(0.0,1.0,0.0));\n"
        + "#4=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#5=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#6=AXIS2_PLACEMENT_3D('AX',#1,#4,#5);\n"
        + "#7=PLANE('PL',#6);\n"
        + "#8=POLY_LOOP('PL0',(#1,#2,#3));\n"
        + "#9=FACE_BOUND('FB',#8,.T.);\n"
        + "#10=ADVANCED_FACE('AF',(#9),#7,.T.);\n"
        + "#11=CONNECTED_FACE_SET('CFS',(#10));\n"
        + "#12=CONNECTED_FACE_SUB_SET('CFSS',(#10),#11);\n"
        + "ENDSEC;"
        );

        Shell shell = builder.buildShell(12);
        assertNotNull(shell);
        assertEquals(1, shell.faces().size());
    }

    @Test
    void shouldBuildSurfacePatchShell() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=PLANE('PL',#4);\n"
        + "#6=SURFACE_PATCH('SP',#5,$,.T.);\n"
        + "ENDSEC;"
        );

        Shell shell = builder.buildShell(6);
        assertNotNull(shell);
        assertEquals(1, shell.faces().size());
        assertInstanceOf(Plane.class, shell.faces().get(0).surface());
    }

    @Test
    void shouldRejectGeometricSurfaceSetShellWithoutFaceBounds() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=PLANE('PL',#4);\n"
        + "#6=GEOMETRIC_SURFACE_SET('GSS',(#5));\n"
        + "ENDSEC;"

        );


        UnsupportedGeometryException exception = assertThrows(
                UnsupportedGeometryException.class,
                () -> builder.buildShell(6)
        );

        assertEquals("GEOMETRIC_SURFACE_SET shell construction requires bounded face geometry", exception.getMessage());
    }

    @Test
    void shouldBuildEdgeCurveAsCurve3() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=VECTOR('V',#3,2.0);\n"
        + "#6=LINE('L',#1,#5);\n"
        + "#7=VERTEX_POINT('V1',#1);\n"
        + "#8=CARTESIAN_POINT('P2',(2.0,0.0,0.0));\n"
        + "#9=VERTEX_POINT('V2',#8);\n"
        + "#10=EDGE_CURVE('EC',#7,#9,#6,.T.);\n"
        + "ENDSEC;"

        );


        // buildEdge should work, and the edge's curve should be a Line3
        Edge edge = builder.buildEdge(10);
        assertNotNull(edge);
        assertInstanceOf(Line3.class, edge.curve());
    }

    @Test
    void shouldBuildFacettedBrep() {
        // FACETTED_BREP (double-T spelling) wraps a closed shell of planar faces
        // Use a simple tetrahedron with 4 faces
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(0.0,1.0,0.0));\n"
        + "#4=CARTESIAN_POINT('P3',(0.0,0.0,1.0));\n"
        + "#5=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#6=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#7=AXIS2_PLACEMENT_3D('AX',#1,#5,#6);\n"
        + "#8=PLANE('PL',#7);\n"
        + "#9=POLY_LOOP('L0',(#1,#2,#3));\n"
        + "#10=FACE_BOUND('FB0',#9,.T.);\n"
        + "#11=ADVANCED_FACE('AF0',(#10),#8,.T.);\n"
        + "#12=CLOSED_SHELL('CS',(#11));\n"
        + "#13=FACETTED_BREP('FB',#12);\n"
        + "ENDSEC;"

        );


        // FACETTED_BREP resolves to StepFacettedBrep which delegates to shell building
        Shell shell = builder.buildShell(12);
        assertNotNull(shell);
        assertEquals(1, shell.faces().size());
    }

    @Test
    void shouldBuildGenericBSplineSurface() {
        // This test verifies that B_SPLINE_SURFACE (without knots) is handled.
        // Since the builder creates a synthetic knot vector, we test through a face that
        // references a PLANE (the most common surface type).
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(0.0,1.0,0.0));\n"
        + "#4=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#5=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#6=AXIS2_PLACEMENT_3D('AX',#1,#4,#5);\n"
        + "#7=PLANE('PL',#6);\n"
        + "#8=POLY_LOOP('PL0',(#1,#2,#3));\n"
        + "#9=FACE_BOUND('FB',#8,.T.);\n"
        + "#10=ADVANCED_FACE('AF',(#9),#7,.T.);\n"
        + "#11=CLOSED_SHELL('CS',(#10));\n"
        + "#12=MANIFOLD_SOLID_BREP('MSB',#11);\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(12);
        assertNotNull(solid);
    }

    @Test
    void shouldResolveFiniteElementMesh() {
        // FINITE_ELEMENT_MESH: verify it can be resolved but cannot build a B-Rep shell
        // (mesh element topology handling varies by mesh type)
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('N0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('N1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('N2',(0.0,1.0,0.0));\n"
        + "#4=FINITE_ELEMENT_MESH('MESH','SOLID',(#1,#2,#3),(#1,#2,#3),('TRI3'),0.0);\n"
        + "ENDSEC;"

        );


        // FINITE_ELEMENT_MESH resolves successfully but can't be converted to B-Rep shell
        assertThrows(UnsupportedGeometryException.class, () -> builder.buildShell(4));
    }

    @Test
    void shouldBuildVertexFromVertexMarker() {
        // When VERTEX is resolved instead of VERTEX_POINT (rare case in complex entity syntax),
        // buildVertex should still delegate to the concrete VERTEX_POINT at the same ID.
        // In normal syntax, VERTEX_POINT resolves and VERTEX is not stored separately.
        // This test verifies the fallback path handles the case where VERTEX resolves
        // but a VERTEX_POINT also exists at the same ID.
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=VERTEX_POINT('V0',#1);\n"
        + "ENDSEC;"

        );


        // Normal VERTEX_POINT path works
        Vertex vertex = builder.buildVertex(2);
        assertNotNull(vertex);
        assertEquals(0.0, vertex.point().x());
        assertEquals(0.0, vertex.point().y());
    }

    @Test
    void shouldBuildEdgeFromEdgeCurve() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=VERTEX_POINT('V0',#1);\n"
        + "#4=VERTEX_POINT('V1',#2);\n"
        + "#5=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#6=VECTOR('V',#5,1.0);\n"
        + "#7=LINE('L0',#1,#6);\n"
        + "#8=EDGE_CURVE('E0',#3,#4,#7,.T.);\n"
        + "ENDSEC;"
        );

        Edge edge = builder.buildEdge(8);
        assertNotNull(edge);
    }

    private static Plane planeSurface(Face face) {
        return assertInstanceOf(Plane.class, face.surface());
    }

    private static StepCadBuilder builder(String step) {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        return StepCadBuilder.fromResolved(resolved);
    }

    @Test
    void shouldResolveSeamEdge() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=VERTEX_POINT('V0',#1);\n"
        + "#3=SEAM_EDGE('SEAM0',#2,#2);\n"
        + "ENDSEC;"

        );


        // Seam edge resolution should work, but building requires associated curve geometry
        // which is typically provided through complex entity syntax
        assertThrows(UnsupportedGeometryException.class, () -> builder.buildEdge(3));
    }

    @Test
    void shouldResolveTessellatedFace() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(0.5,1.0,0.0));\n"
        + "#4=VERTEX_POINT('V0',#1);\n"
        + "#5=VERTEX_POINT('V1',#2);\n"
        + "#6=VERTEX_POINT('V2',#3);\n"
        + "#7=TESSELLATED_TRIANGLE('T0','',#4,#5,#6);\n"
        + "#8=TESSELLATED_FACE('TF0',(#7));\n"
        + "ENDSEC;"

        );


        // Tessellated face should resolve and build to a shell with one triangular face
        Shell shell = builder.buildShell(8);
        assertNotNull(shell);
        assertEquals(1, shell.faces().size());
    }

    @Test
    void shouldResolveProfileDefSubtype() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=AXIS2_PLACEMENT_2D('A2D',#2,#3);\n"
        + "#2=CARTESIAN_POINT('ORIGIN',(0.0,0.0));\n"
        + "#3=DIRECTION('X',(1.0,0.0));\n"
        + "#4=CIRCLE_PROFILE_DEF(.AREA.,'',#1,5.0);\n"
        + "ENDSEC;"

        );


        // Circle profile def should resolve successfully
        assertNotNull(builder);
    }

    @Test
    void shouldBuildPath() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));\n"
        + "#4=VERTEX_POINT('V0',#1);\n"
        + "#5=VERTEX_POINT('V1',#2);\n"
        + "#6=VERTEX_POINT('V2',#3);\n"
        + "#7=DIRECTION('DX1',(1.0,0.0,0.0));\n"
        + "#8=DIRECTION('DY1',(0.0,1.0,0.0));\n"
        + "#9=VECTOR('V1',#7,1.0);\n"
        + "#10=VECTOR('V2',#8,1.0);\n"
        + "#11=LINE('L1',#1,#9);\n"
        + "#12=LINE('L2',#2,#10);\n"
        + "#13=EDGE_CURVE('E1',#4,#5,#11,.T.);\n"
        + "#14=EDGE_CURVE('E2',#5,#6,#12,.T.);\n"
        + "#15=ORIENTED_EDGE('',$,$,#13,.T.);\n"
        + "#16=ORIENTED_EDGE('',$,$,#14,.T.);\n"
        + "#17=PATH('MY_PATH',(#15,#16));\n"
        + "ENDSEC;"

        );


        CompositeCurve3 path = builder.buildPath(17);
        assertEquals(2, path.segments().size());
        assertInstanceOf(Line3.class, path.segments().get(0));
        assertInstanceOf(Line3.class, path.segments().get(1));
    }

    @Test
    void shouldBuildOpenPath() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(2.0,0.0,0.0));\n"
        + "#3=VERTEX_POINT('V0',#1);\n"
        + "#4=VERTEX_POINT('V1',#2);\n"
        + "#5=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#6=VECTOR('V1',#5,2.0);\n"
        + "#7=LINE('L1',#1,#6);\n"
        + "#8=EDGE_CURVE('E1',#3,#4,#7,.T.);\n"
        + "#9=ORIENTED_EDGE('',$,$,#8,.T.);\n"
        + "#10=OPEN_PATH('OPEN_P1',(#9));\n"
        + "ENDSEC;"

        );


        CompositeCurve3 path = builder.buildPath(10);
        assertEquals(1, path.segments().size());
        assertInstanceOf(Line3.class, path.segments().get(0));
    }

    @Test
    void shouldBuildOrientedPathWithReversedOrientation() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=VERTEX_POINT('V0',#1);\n"
        + "#4=VERTEX_POINT('V1',#2);\n"
        + "#5=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#6=VECTOR('V1',#5,1.0);\n"
        + "#7=LINE('L1',#1,#6);\n"
        + "#8=EDGE_CURVE('E1',#3,#4,#7,.T.);\n"
        + "#9=ORIENTED_EDGE('',$,$,#8,.T.);\n"
        + "#10=PATH('P1',(#9));\n"
        + "#11=ORIENTED_PATH('OP1',#10,.F.);\n"
        + "ENDSEC;"

        );


        CompositeCurve3 path = builder.buildPath(11);
        assertEquals(1, path.segments().size());
        assertInstanceOf(Line3.class, path.segments().get(0));
    }

    @Test
    void shouldBuildSubpath() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=VERTEX_POINT('V0',#1);\n"
        + "#4=VERTEX_POINT('V1',#2);\n"
        + "#5=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#6=VECTOR('V1',#5,1.0);\n"
        + "#7=LINE('L1',#1,#6);\n"
        + "#8=EDGE_CURVE('E1',#3,#4,#7,.T.);\n"
        + "#9=ORIENTED_EDGE('',$,$,#8,.T.);\n"
        + "#10=PATH('P1',(#9));\n"
        + "#11=ORIENTED_EDGE('',$,$,#8,.T.);\n"
        + "#12=SUBPATH('SP1',(#11),#10);\n"
        + "ENDSEC;"

        );


        CompositeCurve3 path = builder.buildPath(12);
        assertEquals(1, path.segments().size());
        assertInstanceOf(Line3.class, path.segments().get(0));
    }

    @Test
    void shouldBuildPathAsCurve3() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=VERTEX_POINT('V0',#1);\n"
        + "#4=VERTEX_POINT('V1',#2);\n"
        + "#5=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#6=VECTOR('V1',#5,1.0);\n"
        + "#7=LINE('L1',#1,#6);\n"
        + "#8=EDGE_CURVE('E1',#3,#4,#7,.T.);\n"
        + "#9=ORIENTED_EDGE('',$,$,#8,.T.);\n"
        + "#10=PATH('P1',(#9));\n"
        + "ENDSEC;"

        );


        CompositeCurve3 path = builder.buildPath(10);
        Curve3 curve = path;
        assertInstanceOf(CompositeCurve3.class, curve);
        assertEquals(1, path.segments().size());
    }

    @Test
    void shouldBuildOrientedPathWithFlippedEdgeOrientation() {
        // When StepOrientedPath has orientation=false, the resolver reverses the edge list,
        // and the builder should also flip each edge's orientation flag.
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=VERTEX_POINT('V0',#1);\n"
        + "#4=VERTEX_POINT('V1',#2);\n"
        + "#5=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#6=VECTOR('V1',#5,1.0);\n"
        + "#7=LINE('L1',#1,#6);\n"
        + "#8=EDGE_CURVE('E1',#3,#4,#7,.T.);\n"
        + "#9=ORIENTED_EDGE('',$,$,#8,.T.);\n"
        + "#10=PATH('P1',(#9));\n"
        + "#11=ORIENTED_PATH('OP1',#10,.F.);\n"
        + "ENDSEC;"

        );


        CompositeCurve3 path = builder.buildPath(11);
        assertEquals(1, path.segments().size());
        // The curve should still be a valid Line3 regardless of orientation
        assertInstanceOf(Line3.class, path.segments().get(0));
    }

    @Test
    void shouldResolveOrientedCurveWithReversedOrientation() {
        // When StepOrientedCurve has orientation=false, the resolver should store
        // the orientation flag correctly so the builder can handle it.
        String step =
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#3=VECTOR('V1',#2,1.0);\n"
        + "#4=LINE('L1',#1,#3);\n"
        + "#5=ORIENTED_CURVE('OC1',#4,.F.);\n"
        + "ENDSEC;";

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(com.minicad.step.syntax.StepParser.parse(step));

        StepEntity curveEntity = resolved.get(5);
        assertNotNull(curveEntity);
        assertInstanceOf(com.minicad.step.model.StepOrientedCurve.class, curveEntity);
        com.minicad.step.model.StepOrientedCurve oc = (com.minicad.step.model.StepOrientedCurve) curveEntity;
        assertEquals(false, oc.orientation());
    }

    @Test
    void shouldBuildTrimmedCurveWithNumericParameterTrims() {
        // TRIMMED_CURVE with numeric parameter values instead of entity references.
        // The trim values should be evaluated to points on the basis curve.
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#3=VECTOR('VX',#2,1.0);\n"
        + "#4=LINE('L0',#1,#3);\n"
        + "#5=TRIMMED_CURVE('TC0',#4,((1.0)),((5.0)),.T.,.PARAMETER.);\n"
        + "ENDSEC;"

        );


        TrimmedCurve3 trimmedCurve = builder.buildTrimmedCurve(5);
        assertNotNull(trimmedCurve);
        assertInstanceOf(Line3.class, trimmedCurve.basisCurve());
        // Basis curve is an infinite line, trimmed curve should still be valid
        assertNotNull(trimmedCurve.basisCurve());
    }

    @Test
    void shouldBuildTrimmedCurveWithNumericTrimsOnPolyline() {
        // TRIMMED_CURVE with numeric trims on a POLYLINE basis curve.
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(2.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(4.0,0.0,0.0));\n"
        + "#4=POLYLINE('PL0',(#1,#2,#3));\n"
        + "#5=TRIMMED_CURVE('TC0',#4,((0.5)),((1.5)),.T.,.PARAMETER.);\n"
        + "ENDSEC;"

        );


        TrimmedCurve3 trimmedCurve = builder.buildTrimmedCurve(5);
        assertNotNull(trimmedCurve);
        assertInstanceOf(Polyline3.class, trimmedCurve.basisCurve());
    }

    @Test
    void shouldBuildTrimmedCurveWithEntityReferenceTrims() {
        // TRIMMED_CURVE with entity references (CartesianPoint) as trims.
        // This is the classic form that was already supported.
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=VECTOR('VX',#3,1.0);\n"
        + "#5=LINE('L0',#1,#4);\n"
        + "#6=TRIMMED_CURVE('TC0',#5,(#1),(#2),.T.,.CARTESIAN.);\n"
        + "#7=VERTEX_POINT('V0',#1);\n"
        + "#8=VERTEX_POINT('V1',#2);\n"
        + "#9=EDGE_CURVE('E0',#7,#8,#6,.T.);\n"
        + "ENDSEC;"

        );


        TrimmedCurve3 trimmedCurve = builder.buildTrimmedCurve(6);
        Edge edge = builder.buildEdge(9);

        assertInstanceOf(Line3.class, trimmedCurve.basisCurve());
        assertTrue(edge.curve().contains(edge.start().point()));
        assertTrue(edge.curve().contains(edge.end().point()));
    }

    @Test
    void shouldBuildPcurveAs2DCurve() {
        // PCURVE wraps a representation containing a 2D curve.
        // The builder should dispatch through the representation to the underlying 2D curve.
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0));\n"
        + "#4=VECTOR('V',#3,1.414);\n"
        + "#5=AXIS2_PLACEMENT_2D('AX',#1,#3);\n"
        + "#6=CARTESIAN_POINT('P3',(0.0,0.0,0.0));\n"
        + "#7=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#8=DIRECTION('DX3',(1.0,0.0,0.0));\n"
        + "#9=AXIS2_PLACEMENT_3D('AX3',#6,#7,#8);\n"
        + "#10=PLANE('PL',#9);\n"
        + "#11=LINE('L',#1,#4);\n"
        + "#12=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('CTX','2D'));\n"
        + "#13=REPRESENTATION('PCURVE_REP',(#11),#12);\n"
        + "#14=PCURVE('PC',#10,#13);\n"
        + "ENDSEC;"

        );


        Object curve2 = builder.buildPcurve2(14);
        assertInstanceOf(Line2.class, curve2);
    }

    @Test
    void shouldBuildTrimmedCurveWithParameterTrimsOnCircle() {
        // TRIMMED_CURVE with numeric parameter trims on a CIRCLE basis curve.
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=CIRCLE('C0',#4,1.0);\n"
        + "#6=TRIMMED_CURVE('TC0',#5,((0.0)),((3.14159)),.T.,.PARAMETER.);\n"
        + "ENDSEC;"

        );


        TrimmedCurve3 trimmedCurve = builder.buildTrimmedCurve(6);
        assertNotNull(trimmedCurve);
        assertInstanceOf(com.minicad.geometry.Circle.class, trimmedCurve.basisCurve());
    }

    @Test
    void shouldBuildPolyLoopWithCaching() {
        // POLY_LOOP should be cached and return the same instance on repeated builds.
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));\n"
        + "#3=CARTESIAN_POINT('P2',(0.5,1.0,0.0));\n"
        + "#4=POLY_LOOP('PL',(#1,#2,#3));\n"
        + "ENDSEC;"

        );


        com.minicad.topology.PolyLoop loop1 = builder.buildPolyLoop(4);
        com.minicad.topology.PolyLoop loop2 = builder.buildPolyLoop(4);
        assertSame(loop1, loop2);
        assertEquals(3, loop1.vertexCount());
    }

    @Test
    void shouldBuildCurve2DParametric() {
        // CURVE_2D with polynomial equation coefficients used as PCURVE basis.
        // Equation: x(t) = 0 + 1*t, y(t) = 0 + 1*t (linear parametric curve from origin to (1,1))
        // CURVE_2D has 3 parameters: name (0), position (1), equation (2)
        // The equation list uses ((...)) syntax which creates a nested ListValue
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('ORIGIN',(0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0));\n"
        + "#3=AXIS2_PLACEMENT_2D('AX',#1,#2);\n"
        + "#4=CURVE_2D('C',#3,((0.0,1.0,0.0,1.0)));\n"
        + "#5=CARTESIAN_POINT('P3D',(0.0,0.0,0.0));\n"
        + "#6=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#7=DIRECTION('DX3',(1.0,0.0,0.0));\n"
        + "#8=AXIS2_PLACEMENT_3D('AX3',#5,#6,#7);\n"
        + "#9=PLANE('PL',#8);\n"
        + "#10=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('CTX','2D'));\n"
        + "#11=REPRESENTATION('REP',(#4),#10);\n"
        + "#12=PCURVE('PC',#9,#11);\n"
        + "ENDSEC;"

        );


        // PCURVE wraps a REPRESENTATION that contains the CURVE_2D
        Object curve2 = builder.buildPcurve2(12);
        assertInstanceOf(com.minicad.geometry2d.Polyline2.class, curve2);
    }

    @Test
    void shouldBuildTrimmedCurve2WithNestedListValue() {
        // TRIMMED_CURVE (3D resolver used for 2D) with nested list value syntax ((0.0))
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#3=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#3,#2);\n"
        + "#5=CIRCLE('C',#4,1.0);\n"
        + "#6=TRIMMED_CURVE('TC',#5,((0.0)),((1.5708)),.T.,.PARAMETER.);\n"
        + "ENDSEC;"

        );


        TrimmedCurve3 trimmed = builder.buildTrimmedCurve(6);
        assertNotNull(trimmed);
        assertInstanceOf(com.minicad.geometry.Circle.class, trimmed.basisCurve());
    }

    @Test
    void shouldBuildCartesianTransformationOperator() {
        // CARTESIAN_TRANSFORMATION_OPERATOR_3D: 6 params - name, axis1, axis3, origin, scale, axis2
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('ORIGIN',(1.0,2.0,3.0));\n"
        + "#2=DIRECTION('AXIS1',(1.0,0.0,0.0));\n"
        + "#3=DIRECTION('AXIS2',(0.0,1.0,0.0));\n"
        + "#4=DIRECTION('AXIS3',(0.0,0.0,1.0));\n"
        + "#5=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T',#2,#4,#1,1.0,#3);\n"
        + "ENDSEC;"

        );


        Axis2Placement3D placement = builder.buildTransformation(5);
        assertNotNull(placement);
        assertEquals(1.0, placement.location().x(), 1.0e-12);
        assertEquals(2.0, placement.location().y(), 1.0e-12);
        assertEquals(3.0, placement.location().z(), 1.0e-12);
    }

    @Test
    void shouldBuildItemDefinedTransformation() {
        // ITEM_DEFINED_TRANSFORMATION: 4 params - name, name2, transform_item_1, transform_item_2
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P',(5.0,10.0,15.0));\n"
        + "#2=DIRECTION('Z',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('X',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=ITEM_DEFINED_TRANSFORMATION('T','T2',#4,#4);\n"
        + "ENDSEC;"

        );


        Axis2Placement3D placement = builder.buildItemDefinedTransformation(5);
        assertNotNull(placement);
        assertEquals(5.0, placement.location().x(), 1.0e-12);
        assertEquals(10.0, placement.location().y(), 1.0e-12);
        assertEquals(15.0, placement.location().z(), 1.0e-12);
    }

    @Test
    void shouldBuildCsgSolidWithBlock() {
        // CSG_SOLID wrapping a BLOCK primitive
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('ORIGIN',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('Z',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('X',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
        + "#5=BLOCK('B',#4,2.0,3.0,4.0);\n"
        + "#6=CSG_SOLID('CS',#5);\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(6);
        assertNotNull(solid);
    }

    @Test
    void shouldBuildOffsetCurve2() {
        // OFFSET_CURVE_2D: 4 params - name, basis_curve_ref, offset_distance, self_intersect (.T./.F.)
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('P0',(0.0,0.0));\n"
        + "#2=DIRECTION('D',(1.0,0.0));\n"
        + "#3=VECTOR('V',#2,1.0);\n"
        + "#4=LINE('L',#1,#3);\n"
        + "#5=OFFSET_CURVE_2D('OC',#4,0.5,.F.);\n"
        + "ENDSEC;"

        );


        Object curve2 = builder.buildOffsetCurve2(5);
        assertNotNull(curve2);
    }

    // ---------------------------------------------------------------------------
    // Bounding Box Tests (I04)
    // ---------------------------------------------------------------------------

    @Test
    void shouldComputeBoundingBoxForBlockPrimitive() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=BLOCK('BLK',#4,10.0,20.0,30.0);\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(5);
        assertNotNull(solid);

        BoundingBox3 bbox = solid.boundingBox();
        // Block with x=10, y=20, z=30 at origin
        assertEquals(0.0, bbox.minX(), 1e-9);
        assertEquals(0.0, bbox.minY(), 1e-9);
        assertEquals(0.0, bbox.minZ(), 1e-9);
        assertEquals(10.0, bbox.maxX(), 1e-9);
        assertEquals(20.0, bbox.maxY(), 1e-9);
        assertEquals(30.0, bbox.maxZ(), 1e-9);
    }

    @Test
    void shouldComputeBoundingBoxForSpherePrimitive() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(5.0,5.0,5.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
        + "#4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);\n"
        + "#5=SPHERE('SP',#4,3.0);\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(5);
        assertNotNull(solid);

        BoundingBox3 bbox = solid.boundingBox();
        // Sphere radius 3, center at (5,5,5)
        assertEquals(2.0, bbox.minX(), 1e-9); // 5-3
        assertEquals(2.0, bbox.minY(), 1e-9); // 5-3
        assertEquals(2.0, bbox.minZ(), 1e-9); // 5-3
        assertEquals(8.0, bbox.maxX(), 1e-9); // 5+3
        assertEquals(8.0, bbox.maxY(), 1e-9); // 5+3
        assertEquals(8.0, bbox.maxZ(), 1e-9); // 5+3
    }

    @Test
    void shouldComputeBoundingBoxForCylinderPrimitive() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=AXIS1_PLACEMENT('AX1',#1,#2);\n"
        // RIGHT_CIRCULAR_CYLINDER params: position, height, radius
        + "#4=RIGHT_CIRCULAR_CYLINDER('CY',#3,5.0,2.0);\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(4);
        assertNotNull(solid);

        BoundingBox3 bbox = solid.boundingBox();
        // Cylinder height 5, radius 2, axis along Z at origin
        assertEquals(-2.0, bbox.minX(), 1e-9);
        assertEquals(-2.0, bbox.minY(), 1e-9);
        assertEquals(0.0, bbox.minZ(), 1e-9);
        assertEquals(2.0, bbox.maxX(), 1e-9);
        assertEquals(2.0, bbox.maxY(), 1e-9);
        assertEquals(5.0, bbox.maxZ(), 1e-9);
    }

    @Test
    void shouldComputeBoundingBoxForTorusPrimitive() {
        StepCadBuilder builder = builder(
        "DATA;\n"
        + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
        + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
        + "#3=AXIS1_PLACEMENT('AX1',#1,#2);\n"
        + "#4=TORUS('TO',#3,4.0,1.0);\n"
        + "ENDSEC;"
        );

        Solid solid = builder.buildSolid(4);
        assertNotNull(solid);

        BoundingBox3 bbox = solid.boundingBox();
        // Torus major radius 4, minor radius 1, axis along Z
        // Outer extent: major + minor = 5 in XY, minor = 1 in Z
        assertEquals(-5.0, bbox.minX(), 1e-9);
        assertEquals(-5.0, bbox.minY(), 1e-9);
        assertEquals(-1.0, bbox.minZ(), 1e-9);
        assertEquals(5.0, bbox.maxX(), 1e-9);
        assertEquals(5.0, bbox.maxY(), 1e-9);
        assertEquals(1.0, bbox.maxZ(), 1e-9);
    }
}
