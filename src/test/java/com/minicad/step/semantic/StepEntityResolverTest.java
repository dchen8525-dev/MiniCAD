package com.minicad.step.semantic;

import com.minicad.common.StepParseException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepConicalSurface;
import com.minicad.step.model.StepColourRgb;
import com.minicad.step.model.StepConnectedEdgeSet;
import com.minicad.step.model.StepConnectedFaceSet;
import com.minicad.step.model.StepConnectedFaceSubSet;
import com.minicad.step.model.StepCompositeCurve;
import com.minicad.step.model.StepCompositeCurveOnSurface;
import com.minicad.step.model.StepCompositeCurveSegment;
import com.minicad.step.model.StepCylindricalSurface;
import com.minicad.step.model.StepConversionBasedUnit;
import com.minicad.step.model.StepConversionBasedUnitWithOffset;
import com.minicad.step.model.StepContextDependentUnit;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEdgeBasedWireframeModel;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepBSplineCurve;
import com.minicad.step.model.StepBSplineCurveWithKnots;
import com.minicad.step.model.StepBSplineSurface;
import com.minicad.step.model.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.StepBoundedCurve;
import com.minicad.step.model.StepBoundedSurface;
import com.minicad.step.model.StepBezierCurve;
import com.minicad.step.model.StepBezierSurface;
import com.minicad.step.model.StepBooleanClippingResult;
import com.minicad.step.model.StepBooleanResult;
import com.minicad.step.model.StepBrepWithVoids;
import com.minicad.step.model.StepPiecewiseBezierCurve;
import com.minicad.step.model.StepPiecewiseBezierSurface;
import com.minicad.step.model.StepRationalBSplineCurve;
import com.minicad.step.model.StepRationalBSplineSurface;
import com.minicad.step.model.StepUniformCurve;
import com.minicad.step.model.StepQuasiUniformCurve;
import com.minicad.step.model.StepUniformSurface;
import com.minicad.step.model.StepQuasiUniformSurface;
import com.minicad.step.model.StepEllipse;
import com.minicad.step.model.StepFaceBound;
import com.minicad.step.model.StepFaceBasedSurfaceModel;
import com.minicad.step.model.StepFaceSurface;
import com.minicad.step.model.StepPresentationLayerAssignment;
import com.minicad.step.model.StepStyledItem;
import com.minicad.step.model.StepSubedge;
import com.minicad.step.model.StepAnnotationTextOccurrence;
import com.minicad.step.model.StepApplicationProtocolDefinition;
import com.minicad.step.model.StepAdvancedFace;
import com.minicad.step.model.StepAxis1Placement;
import com.minicad.step.model.StepAxis2Placement2D;
import com.minicad.step.model.StepDescriptiveRepresentationItem;
import com.minicad.step.model.StepDegeneratePcurve;
import com.minicad.step.model.StepDerivedUnit;
import com.minicad.step.model.StepDraughtingCallout;
import com.minicad.step.model.StepGeometricCurveSet;
import com.minicad.step.model.StepGeometricSet;
import com.minicad.step.model.StepGeometricItemSpecificUsage;
import com.minicad.step.model.StepMeasureRepresentationItem;
import com.minicad.step.model.StepGlobalUncertaintyAssignedContext;
import com.minicad.step.model.StepGlobalUnitAssignedContext;
import com.minicad.step.model.StepItemDefinedTransformation;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepMeasureWithUnit;
import com.minicad.step.model.StepNamedUnit;
import com.minicad.step.model.StepOrientedFace;
import com.minicad.step.model.StepOrientedClosedShell;
import com.minicad.step.model.StepOrientedOpenShell;
import com.minicad.step.model.StepOrientedPath;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepPolyLoop;
import com.minicad.step.model.StepPolyline;
import com.minicad.step.model.StepPcurve;
import com.minicad.step.model.StepPoint;
import com.minicad.step.model.StepPointSet;
import com.minicad.step.model.StepProduct;
import com.minicad.step.model.StepProductDefinition;
import com.minicad.step.model.StepProductDefinitionFormation;
import com.minicad.step.model.StepProductDefinitionShape;
import com.minicad.step.model.StepProductRelatedProductCategory;
import com.minicad.step.model.StepPropertyDefinition;
import com.minicad.step.model.StepPropertyDefinitionRepresentation;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.model.StepRepresentationItem;
import com.minicad.step.model.StepRepresentationRelationship;
import com.minicad.step.model.StepCurve;
import com.minicad.step.model.StepGeometricRepresentationContext;
import com.minicad.step.model.StepGeometricRepresentationItem;
import com.minicad.step.model.StepRepresentationRelationshipWithTransformation;
import com.minicad.step.model.StepSeamCurve;
import com.minicad.step.model.StepShapeRepresentationRelationship;
import com.minicad.step.model.StepShapeDefinitionRepresentation;
import com.minicad.step.model.StepShellBasedSurfaceModel;
import com.minicad.step.model.StepSiUnit;
import com.minicad.step.model.StepSolidModel;
import com.minicad.step.model.StepSphericalSurface;
import com.minicad.step.model.StepSurface;
import com.minicad.step.model.StepSurfaceCurve;
import com.minicad.step.model.StepSurfaceModel;
import com.minicad.step.model.StepSurfaceOfLinearExtrusion;
import com.minicad.step.model.StepSurfaceOfRevolution;
import com.minicad.step.model.StepSurfacedOpenShell;
import com.minicad.step.model.StepTopologicalRepresentationItem;
import com.minicad.step.model.StepToroidalSurface;
import com.minicad.step.model.StepTrimmedCurve;
import com.minicad.step.model.StepTypedMeasureWithUnit;
import com.minicad.step.model.StepNextAssemblyUsageOccurrence;
import com.minicad.step.model.StepOffsetCurve3D;
import com.minicad.step.model.StepOffsetSurface;
import com.minicad.step.model.StepOpenPath;
import com.minicad.step.model.StepPath;
import com.minicad.step.model.StepSubpath;
import com.minicad.step.model.StepContextDependentShapeRepresentation;
import com.minicad.step.model.StepValueRepresentationItem;
import com.minicad.step.model.StepCircle;
import com.minicad.step.model.StepUncertaintyMeasureWithUnit;
import com.minicad.step.model.StepVertex;
import com.minicad.step.model.StepEdge;
import com.minicad.step.model.StepFace;
import com.minicad.step.model.StepVertexLoop;
import com.minicad.step.model.StepVertexShell;
import com.minicad.step.model.StepWireShell;
import com.minicad.step.model.StepShellBasedWireframeModel;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;

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
    void shouldResolveExamplesTestStepSemanticGraph() throws IOException {
        String step = Files.readString(Path.of("examples/test.step"));

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation presentation = assertInstanceOf(StepRepresentation.class, resolved.get(10));
        assertEquals(false, presentation.shapeRepresentation());
        assertEquals(1, presentation.items().size());

        StepRepresentation brepRepresentation = assertInstanceOf(StepRepresentation.class, resolved.get(12));
        assertEquals(true, brepRepresentation.shapeRepresentation());
        assertEquals(1, brepRepresentation.items().size());

        StepDerivedUnit derivedUnit = assertInstanceOf(StepDerivedUnit.class, resolved.get(302));
        assertEquals(2, derivedUnit.elements().size());
        assertEquals("DERIVED_UNIT", derivedUnit.unitKind());

        StepDescriptiveRepresentationItem item = assertInstanceOf(StepDescriptiveRepresentationItem.class, resolved.get(308));
        assertEquals("\\X2\\94A2\\X0\\", item.name());

        StepPropertyDefinition propertyDefinition = assertInstanceOf(StepPropertyDefinition.class, resolved.get(309));
        assertEquals(527, propertyDefinition.definition().id());

        StepPropertyDefinitionRepresentation propertyRepresentation = assertInstanceOf(
                StepPropertyDefinitionRepresentation.class,
                resolved.get(304)
        );
        assertEquals(309, propertyRepresentation.definition().id());
        assertEquals(306, propertyRepresentation.usedRepresentation().id());

        StepProductRelatedProductCategory category = assertInstanceOf(StepProductRelatedProductCategory.class, resolved.get(529));
        assertEquals(1, category.products().size());
        assertEquals(533, category.products().getFirst().id());

        StepApplicationProtocolDefinition protocol = assertInstanceOf(StepApplicationProtocolDefinition.class, resolved.get(530));
        assertEquals(2009, protocol.year());
        assertEquals(531, protocol.application().id());
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
    void shouldResolveStandaloneBSplineCurve() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=B_SPLINE_CURVE('C0',2,(#1,#2,#3),.UNSPECIFIED.,.F.,.F.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepBSplineCurve spline = assertInstanceOf(StepBSplineCurve.class, resolved.get(4));
        assertEquals("C0", spline.name());
        assertEquals(2, spline.degree());
        assertEquals(3, spline.controlPoints().size());
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

        assertEquals(
                "ADVANCED_FACE geometry must be PLANE, CYLINDRICAL_SURFACE, CONICAL_SURFACE, SURFACE_OF_LINEAR_EXTRUSION, SURFACE_OF_REVOLUTION, B_SPLINE_SURFACE_WITH_KNOTS or TOROIDAL_SURFACE",
                exception.getMessage()
        );
    }

    @Test
    void shouldResolveCylindricalSurfaceAdvancedFaceGeometry() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=CYLINDRICAL_SURFACE('CY0',#4,2.0);
                #10=EDGE_LOOP('L0',());
                #11=FACE_OUTER_BOUND('B0',#10,.T.);
                #12=ADVANCED_FACE('F0',(#11),#5,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepCylindricalSurface surface = assertInstanceOf(StepCylindricalSurface.class, resolved.get(5));
        assertEquals(2.0, surface.radius());
    }

    @Test
    void shouldResolveExtrusionAndRevolutionSurfaces() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(4.0,1.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=DIRECTION('DY',(0.0,1.0,0.0));
                #6=VECTOR('VY',#5,3.0);
                #7=B_SPLINE_CURVE_WITH_KNOTS('',2,(#1,#2,#3),.UNSPECIFIED.,.F.,.F.,(3,3),(0.0,1.0),.PIECEWISE_BEZIER_KNOTS.);
                #8=AXIS1_PLACEMENT('',#1,#5);
                #9=SURFACE_OF_LINEAR_EXTRUSION('',#7,#6);
                #10=SURFACE_OF_REVOLUTION('',#7,#8);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAxis1Placement axis = assertInstanceOf(StepAxis1Placement.class, resolved.get(8));
        assertEquals(5, axis.axis().id());
        StepSurfaceOfLinearExtrusion extrusion = assertInstanceOf(StepSurfaceOfLinearExtrusion.class, resolved.get(9));
        assertEquals(7, extrusion.sweptCurve().id());
        StepSurfaceOfRevolution revolution = assertInstanceOf(StepSurfaceOfRevolution.class, resolved.get(10));
        assertEquals(8, revolution.axisPosition().id());
    }

    @Test
    void shouldResolveExamplesFanStepSemanticGraph() throws IOException {
        String step = Files.readString(Path.of("examples/fan.stp"));

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertInstanceOf(StepSurfaceOfLinearExtrusion.class, resolved.get(71));
        assertInstanceOf(StepSurfaceOfRevolution.class, resolved.get(6338));
        assertInstanceOf(StepAxis1Placement.class, resolved.get(6349));
    }

    @Test
    void shouldResolveFaceSurfaceAndOrientedFace() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #10=EDGE_LOOP('L0',());
                #11=FACE_OUTER_BOUND('B0',#10,.T.);
                #12=FACE_SURFACE('FS0',(#11),#5,.T.);
                #13=ORIENTED_FACE('OF0',#12,.F.);
                #14=OPEN_SHELL('OS',(#13));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepFaceSurface faceSurface = assertInstanceOf(StepFaceSurface.class, resolved.get(12));
        StepOrientedFace orientedFace = assertInstanceOf(StepOrientedFace.class, resolved.get(13));
        assertEquals(5, faceSurface.faceGeometry().id());
        assertEquals(12, orientedFace.faceElement().id());
        assertEquals(false, orientedFace.orientation());
    }

    @Test
    void shouldResolveOrientedShells() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #6=EDGE_LOOP('L0',());
                #7=FACE_OUTER_BOUND('B0',#6,.T.);
                #8=ADVANCED_FACE('FACE0',(#7),#5,.T.);
                #9=OPEN_SHELL('OS',(#8));
                #10=CLOSED_SHELL('CS',(#8));
                #11=ORIENTED_OPEN_SHELL('OOS',#9,.F.);
                #12=ORIENTED_CLOSED_SHELL('OCS',#10,.F.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepOrientedOpenShell orientedOpenShell = assertInstanceOf(StepOrientedOpenShell.class, resolved.get(11));
        StepOrientedClosedShell orientedClosedShell = assertInstanceOf(StepOrientedClosedShell.class, resolved.get(12));
        assertEquals(9, orientedOpenShell.openShellElement().id());
        assertEquals(10, orientedClosedShell.closedShellElement().id());
        assertEquals(false, orientedOpenShell.orientation());
        assertEquals(false, orientedClosedShell.orientation());
        assertEquals(1, orientedOpenShell.faces().size());
        assertEquals(1, orientedClosedShell.faces().size());
    }

    @Test
    void shouldResolveSurfacedOpenShell() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #6=EDGE_LOOP('L0',());
                #7=FACE_OUTER_BOUND('B0',#6,.T.);
                #8=FACE_SURFACE('FS0',(#7),#5,.T.);
                #9=SURFACED_OPEN_SHELL('SOS',(#8));
                #10=ORIENTED_OPEN_SHELL('OOS',#9,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepSurfacedOpenShell surfacedOpenShell = assertInstanceOf(StepSurfacedOpenShell.class, resolved.get(9));
        StepOrientedOpenShell orientedOpenShell = assertInstanceOf(StepOrientedOpenShell.class, resolved.get(10));
        assertEquals("SOS", surfacedOpenShell.name());
        assertEquals(1, surfacedOpenShell.faces().size());
        assertEquals(8, surfacedOpenShell.faces().getFirst().id());
        assertEquals(9, orientedOpenShell.openShellElement().id());
    }

    @Test
    void shouldResolveVertexLoop() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=VERTEX_POINT('V0',#1);
                #3=VERTEX_LOOP('VL0',#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepVertexLoop vertexLoop = assertInstanceOf(StepVertexLoop.class, resolved.get(3));
        assertEquals(2, vertexLoop.loopVertex().id());
    }

    @Test
    void shouldResolvePolyLoop() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=POLY_LOOP('PL0',(#1,#2,#3));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepPolyLoop polyLoop = assertInstanceOf(StepPolyLoop.class, resolved.get(4));
        assertEquals("PL0", polyLoop.name());
        assertEquals(3, polyLoop.polygon().size());
        assertEquals(1, polyLoop.polygon().getFirst().id());
    }

    @Test
    void shouldResolveVertexShell() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=VERTEX_POINT('V0',#1);
                #3=VERTEX_LOOP('VL0',#2);
                #4=VERTEX_SHELL('VS0',#3);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepVertexShell shell = assertInstanceOf(StepVertexShell.class, resolved.get(4));
        assertEquals("VS0", shell.name());
        assertEquals(3, shell.extent().id());
    }

    @Test
    void shouldResolveConnectedFaceSet() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #6=EDGE_LOOP('L0',());
                #7=FACE_OUTER_BOUND('B0',#6,.T.);
                #8=ADVANCED_FACE('FACE0',(#7),#5,.T.);
                #9=CONNECTED_FACE_SET('CFS0',(#8));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepConnectedFaceSet faceSet = assertInstanceOf(StepConnectedFaceSet.class, resolved.get(9));
        assertEquals("CFS0", faceSet.name());
        assertEquals(1, faceSet.faces().size());
        assertEquals(8, faceSet.faces().getFirst().id());
    }

    @Test
    void shouldResolveConnectedFaceSubSet() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #6=EDGE_LOOP('L0',());
                #7=FACE_OUTER_BOUND('B0',#6,.T.);
                #8=ADVANCED_FACE('FACE0',(#7),#5,.T.);
                #9=CONNECTED_FACE_SET('CFS0',(#8));
                #10=(CONNECTED_FACE_SUB_SET('CFSS0',(#8),#9) CONNECTED_FACE_SET('CFSS0',(#8)));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(10);
        assertSame(StepConnectedFaceSubSet.class, entity.getClass());
        StepConnectedFaceSubSet faceSubSet = assertInstanceOf(StepConnectedFaceSubSet.class, entity);
        assertEquals("CFSS0", faceSubSet.name());
        assertEquals(1, faceSubSet.faces().size());
        assertEquals(8, faceSubSet.faces().getFirst().id());
        assertEquals(9, faceSubSet.parentFaceSet().id());
    }

    @Test
    void shouldResolvePath() {
        String step = """
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
                #10=PATH('PTH',(#9));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepPath path = assertInstanceOf(StepPath.class, resolved.get(10));
        assertEquals("PTH", path.name());
        assertEquals(1, path.edges().size());
        assertEquals(9, path.edges().getFirst().id());
    }

    @Test
    void shouldResolveOpenPath() {
        String step = """
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
                #10=OPEN_PATH('OP',(#9));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepOpenPath path = assertInstanceOf(StepOpenPath.class, resolved.get(10));
        assertEquals("OP", path.name());
        assertEquals(1, path.edges().size());
        assertEquals(9, path.edges().getFirst().id());
    }

    @Test
    void shouldResolveSubpath() {
        String step = """
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
                #10=PATH('PTH',(#9));
                #11=SUBPATH('SP0',(#9),#10);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepSubpath subpath = assertInstanceOf(StepSubpath.class, resolved.get(11));
        assertEquals("SP0", subpath.name());
        assertEquals(1, subpath.edges().size());
        assertEquals(10, subpath.parentPath().id());
    }

    @Test
    void shouldResolveWireShell() {
        String step = """
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
                #10=EDGE_LOOP('EL0',(#9));
                #11=WIRE_SHELL('WS0',(#10));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepWireShell shell = assertInstanceOf(StepWireShell.class, resolved.get(11));
        assertEquals("WS0", shell.name());
        assertEquals(1, shell.loops().size());
        assertEquals(10, shell.loops().getFirst().id());
    }

    @Test
    void shouldResolveWireShellWithPolyLoop() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=POLY_LOOP('PL0',(#1,#2,#3));
                #5=WIRE_SHELL('WS0',(#4));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepWireShell shell = assertInstanceOf(StepWireShell.class, resolved.get(5));
        assertEquals("WS0", shell.name());
        assertEquals(1, shell.loops().size());
        assertSame(StepPolyLoop.class, shell.loops().getFirst().getClass());
    }

    @Test
    void shouldResolveOrientedPath() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=VECTOR('VX',#4,1.0);
                #6=LINE('L0',#1,#5);
                #7=LINE('L1',#2,#5);
                #8=VERTEX_POINT('V0',#1);
                #9=VERTEX_POINT('V1',#2);
                #10=VERTEX_POINT('V2',#3);
                #11=EDGE_CURVE('E0',#8,#9,#6,.T.);
                #12=EDGE_CURVE('E1',#9,#10,#7,.T.);
                #13=ORIENTED_EDGE('OE0',$,$,#11,.T.);
                #14=ORIENTED_EDGE('OE1',$,$,#12,.T.);
                #15=PATH('PTH',(#13,#14));
                #16=ORIENTED_PATH('OPTH',#15,.F.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepOrientedPath path = assertInstanceOf(StepOrientedPath.class, resolved.get(16));
        assertEquals("OPTH", path.name());
        assertEquals(15, path.pathElement().id());
        assertEquals(false, path.orientation());
        assertEquals(2, path.edges().size());
        assertEquals(14, path.edges().getFirst().id());
        assertEquals(13, path.edges().get(1).id());
    }

    @Test
    void shouldResolveOrientedPathAgainstOpenPathSubtype() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(2.0,0.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=VECTOR('VX',#4,1.0);
                #6=LINE('L0',#1,#5);
                #7=LINE('L1',#2,#5);
                #8=VERTEX_POINT('V0',#1);
                #9=VERTEX_POINT('V1',#2);
                #10=VERTEX_POINT('V2',#3);
                #11=EDGE_CURVE('E0',#8,#9,#6,.T.);
                #12=EDGE_CURVE('E1',#9,#10,#7,.T.);
                #13=ORIENTED_EDGE('OE0',$,$,#11,.T.);
                #14=ORIENTED_EDGE('OE1',$,$,#12,.T.);
                #15=OPEN_PATH('OP0',(#13,#14));
                #16=ORIENTED_PATH('OOP',#15,.F.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepOrientedPath path = assertInstanceOf(StepOrientedPath.class, resolved.get(16));
        assertEquals("OOP", path.name());
        assertEquals(15, path.pathElement().id());
        assertEquals(false, path.orientation());
        assertEquals(2, path.edges().size());
        assertEquals(14, path.edges().getFirst().id());
        assertEquals(13, path.edges().get(1).id());
    }

    @Test
    void shouldResolveShellBasedWireframeModel() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=VERTEX_POINT('V0',#1);
                #3=VERTEX_LOOP('VL0',#2);
                #4=VERTEX_SHELL('VS0',#3);
                #5=SHELL_BASED_WIREFRAME_MODEL('SBWM',(#4));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepShellBasedWireframeModel model = assertInstanceOf(
                StepShellBasedWireframeModel.class,
                resolved.get(5)
        );
        assertEquals("SBWM", model.name());
        assertEquals(1, model.boundaries().size());
        assertEquals(4, model.boundaries().getFirst().id());
    }

    @Test
    void shouldResolveFaceBasedSurfaceModel() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #6=EDGE_LOOP('L0',());
                #7=FACE_OUTER_BOUND('B0',#6,.T.);
                #8=ADVANCED_FACE('FACE0',(#7),#5,.T.);
                #9=CONNECTED_FACE_SET('CFS0',(#8));
                #10=(FACE_BASED_SURFACE_MODEL('FBSM',(#9)) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('fbsm-item'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(10);
        assertSame(StepFaceBasedSurfaceModel.class, entity.getClass());
        StepFaceBasedSurfaceModel model = assertInstanceOf(StepFaceBasedSurfaceModel.class, entity);
        assertEquals("FBSM", model.name());
        assertEquals(1, model.faceSets().size());
        assertEquals(9, model.faceSets().getFirst().id());
    }

    @Test
    void shouldResolveFaceBasedSurfaceModelWithConnectedFaceSubSet() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #6=EDGE_LOOP('L0',());
                #7=FACE_OUTER_BOUND('B0',#6,.T.);
                #8=ADVANCED_FACE('FACE0',(#7),#5,.T.);
                #9=CONNECTED_FACE_SET('CFS0',(#8));
                #10=(CONNECTED_FACE_SUB_SET('CFSS0',(#8),#9) CONNECTED_FACE_SET('CFSS0',(#8)));
                #11=(FACE_BASED_SURFACE_MODEL('FBSM',(#10)) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('fbsm-item'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(11);
        assertSame(StepFaceBasedSurfaceModel.class, entity.getClass());
        StepFaceBasedSurfaceModel model = assertInstanceOf(StepFaceBasedSurfaceModel.class, entity);
        assertEquals("FBSM", model.name());
        assertEquals(1, model.faceSets().size());
        assertEquals(10, model.faceSets().getFirst().id());
    }

    @Test
    void shouldResolveConicalSurfaceAndTrimmedCurve() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=CONICAL_SURFACE('CN0',#4,2.0,0.2);
                #6=CARTESIAN_POINT('P0',(2.0,0.0,0.0));
                #7=CARTESIAN_POINT('P1',(0.0,2.0,0.0));
                #8=CIRCLE('C0',#4,2.0);
                #9=TRIMMED_CURVE('TC0',#8,(#6),(#7),.T.,.CARTESIAN.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepConicalSurface conicalSurface = assertInstanceOf(StepConicalSurface.class, resolved.get(5));
        StepTrimmedCurve trimmedCurve = assertInstanceOf(StepTrimmedCurve.class, resolved.get(9));
        assertEquals(2.0, conicalSurface.radius());
        assertEquals(8, trimmedCurve.basisCurve().id());
    }

    @Test
    void shouldResolveEllipseSurfaceCurveAndBSplineCurveWithKnots() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=ELLIPSE('E0',#4,3.0,2.0);
                #6=SURFACE_CURVE('SC0',#5,(),.T.);
                #10=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #12=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #13=(B_SPLINE_CURVE('B0',2,(#10,#11,#12),.UNSPECIFIED.,.F.,.F.)
                     B_SPLINE_CURVE_WITH_KNOTS((3,3),(0.0,1.0),.UNSPECIFIED.));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEllipse ellipse = assertInstanceOf(StepEllipse.class, resolved.get(5));
        StepSurfaceCurve surfaceCurve = assertInstanceOf(StepSurfaceCurve.class, resolved.get(6));
        StepBSplineCurveWithKnots spline = assertInstanceOf(StepBSplineCurveWithKnots.class, resolved.get(13));
        assertEquals(3.0, ellipse.semiAxis1());
        assertEquals(5, surfaceCurve.curve3d().id());
        assertEquals(2, spline.degree());
    }

    @Test
    void shouldResolveComplexBsplineCurveWithoutNameParameter() {
        String step = """
                DATA;
                #10=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #12=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #13=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #20=(BOUNDED_CURVE()
                     B_SPLINE_CURVE(3,(#10,#11,#12,#13),.UNSPECIFIED.,.F.,.F.)
                     B_SPLINE_CURVE_WITH_KNOTS((4,4),(0.0,1.0),.UNSPECIFIED.));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepBSplineCurveWithKnots spline = assertInstanceOf(StepBSplineCurveWithKnots.class, resolved.get(20));
        assertEquals("", spline.name());
        assertEquals(3, spline.degree());
        assertEquals(4, spline.controlPoints().size());
    }

    @Test
    void shouldResolveBoundedCurveMarker() {
        String step = """
                DATA;
                #1=(BOUNDED_CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('bc'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepBoundedCurve.class, entity.getClass());
        StepBoundedCurve boundedCurve = assertInstanceOf(StepBoundedCurve.class, entity);
        assertEquals("bc", boundedCurve.name());
    }

    @Test
    void shouldResolveRationalBsplineCurve() {
        String step = """
                DATA;
                #10=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #12=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #20=(B_SPLINE_CURVE('RB',2,(#10,#11,#12),.UNSPECIFIED.,.F.,.F.)
                     RATIONAL_B_SPLINE_CURVE((1.0,0.5,1.0)));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRationalBSplineCurve curve = assertInstanceOf(StepRationalBSplineCurve.class, resolved.get(20));
        assertEquals("RB", curve.name());
        assertEquals(3, curve.weightsData().size());
        assertEquals(0, curve.knots().size());
    }

    @Test
    void shouldResolveRationalBsplineCurveWithKnots() {
        String step = """
                DATA;
                #10=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #11=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #12=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #20=(B_SPLINE_CURVE('RBK',2,(#10,#11,#12),.UNSPECIFIED.,.F.,.F.)
                     B_SPLINE_CURVE_WITH_KNOTS((3,3),(0.0,1.0),.UNSPECIFIED.)
                     RATIONAL_B_SPLINE_CURVE((1.0,0.5,1.0)));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRationalBSplineCurve curve = assertInstanceOf(StepRationalBSplineCurve.class, resolved.get(20));
        assertEquals(2, curve.knots().size());
        assertEquals(2, curve.knotMultiplicities().size());
    }

    @Test
    void shouldResolveUniformCurveMarker() {
        String step = """
                DATA;
                #1=(UNIFORM_CURVE() BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('uc'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepUniformCurve.class, entity.getClass());
        StepUniformCurve curve = assertInstanceOf(StepUniformCurve.class, entity);
        assertEquals("uc", curve.name());
    }

    @Test
    void shouldResolveQuasiUniformCurveMarker() {
        String step = """
                DATA;
                #1=(QUASI_UNIFORM_CURVE() BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('quc'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepQuasiUniformCurve.class, entity.getClass());
        StepQuasiUniformCurve curve = assertInstanceOf(StepQuasiUniformCurve.class, entity);
        assertEquals("quc", curve.name());
    }

    @Test
    void shouldResolveBezierCurveMarker() {
        String step = """
                DATA;
                #1=(BEZIER_CURVE() BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('bc'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepBezierCurve.class, entity.getClass());
        StepBezierCurve curve = assertInstanceOf(StepBezierCurve.class, entity);
        assertEquals("bc", curve.name());
    }

    @Test
    void shouldResolvePiecewiseBezierCurveMarker() {
        String step = """
                DATA;
                #1=(PIECEWISE_BEZIER_CURVE() BEZIER_CURVE() BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('pbc'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepPiecewiseBezierCurve.class, entity.getClass());
        StepPiecewiseBezierCurve curve = assertInstanceOf(StepPiecewiseBezierCurve.class, entity);
        assertEquals("pbc", curve.name());
    }

    @Test
    void shouldResolvePcurveAndDefinitionalRepresentation() {
        String step = """
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
                #17=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #18=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #19=DIRECTION('D3',(1.0,0.0,0.0));
                #20=VECTOR('V3',#19,1.0);
                #21=LINE('L3D',#17,#20);
                #22=SURFACE_CURVE('SC0',#21,(#16),.PCURVE_S1.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(15));
        StepPcurve pcurve = assertInstanceOf(StepPcurve.class, resolved.get(16));
        StepSurfaceCurve surfaceCurve = assertInstanceOf(StepSurfaceCurve.class, resolved.get(22));
        assertEquals(false, representation.shapeRepresentation());
        assertEquals(15, pcurve.referenceToCurve().id());
        assertEquals(1, surfaceCurve.associatedGeometry().size());
        assertEquals(16, surfaceCurve.associatedGeometry().getFirst().id());
        assertEquals("PCURVE_S1", surfaceCurve.masterRepresentation());
    }

    @Test
    void shouldResolveDegeneratePcurveAndSurfaceCurve() {
        String step = """
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
                #16=DEGENERATE_PCURVE('DPC0',#5,#15);
                #17=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #18=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #19=DIRECTION('D3',(1.0,0.0,0.0));
                #20=VECTOR('V3',#19,1.0);
                #21=LINE('L3D',#17,#20);
                #22=SURFACE_CURVE('SC0',#21,(#16),.PCURVE_S1.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepDegeneratePcurve pcurve = assertInstanceOf(StepDegeneratePcurve.class, resolved.get(16));
        StepSurfaceCurve surfaceCurve = assertInstanceOf(StepSurfaceCurve.class, resolved.get(22));
        assertEquals(15, pcurve.referenceToCurve().id());
        assertEquals(1, surfaceCurve.associatedGeometry().size());
        assertEquals(16, surfaceCurve.associatedGeometry().getFirst().id());
    }

    @Test
    void shouldResolve2dAxisPlacementAndCircle() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('UV0',(1.0,2.0));
                #2=DIRECTION('DUV',(1.0,0.0));
                #3=AXIS2_PLACEMENT_2D('A2',#1,#2);
                #4=CIRCLE('PC',#3,0.5);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAxis2Placement2D placement = assertInstanceOf(StepAxis2Placement2D.class, resolved.get(3));
        StepCircle circle = assertInstanceOf(StepCircle.class, resolved.get(4));
        assertEquals(1, placement.location().id());
        assertEquals(3, circle.position().id());
        assertEquals(0.5, circle.radius());
    }

    @Test
    void shouldResolveSeamCurveWithTwoPcurves() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepSeamCurve seamCurve = assertInstanceOf(StepSeamCurve.class, resolved.get(22));
        assertEquals(2, seamCurve.associatedGeometry().size());
        assertEquals("PCURVE_S1", seamCurve.masterRepresentation());
    }

    @Test
    void shouldResolveBSplineSurfaceWithKnots() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P10',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('P01',(0.0,2.0,0.0));
                #4=CARTESIAN_POINT('P11',(2.0,2.0,1.0));
                #10=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepBSplineSurfaceWithKnots surface = assertInstanceOf(StepBSplineSurfaceWithKnots.class, resolved.get(10));
        assertEquals(1, surface.uDegree());
        assertEquals(1, surface.vDegree());
        assertEquals(2, surface.controlPoints().size());
        assertEquals(2, surface.controlPoints().getFirst().size());
    }

    @Test
    void shouldResolveStandaloneBSplineSurface() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P10',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('P01',(0.0,2.0,0.0));
                #4=CARTESIAN_POINT('P11',(2.0,2.0,1.0));
                #10=B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepBSplineSurface surface = assertInstanceOf(StepBSplineSurface.class, resolved.get(10));
        assertEquals(1, surface.uDegree());
        assertEquals(1, surface.vDegree());
        assertEquals(2, surface.controlPoints().size());
        assertEquals(2, surface.controlPoints().getFirst().size());
    }

    @Test
    void shouldResolveBoundedSurfaceMarker() {
        String step = """
                DATA;
                #1=(BOUNDED_SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('bs'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepBoundedSurface.class, entity.getClass());
        StepBoundedSurface boundedSurface = assertInstanceOf(StepBoundedSurface.class, entity);
        assertEquals("bs", boundedSurface.name());
    }

    @Test
    void shouldResolveRationalBsplineSurface() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P10',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('P01',(0.0,2.0,0.0));
                #4=CARTESIAN_POINT('P11',(2.0,2.0,1.0));
                #10=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)
                     RATIONAL_B_SPLINE_SURFACE(((1.0,1.0),(1.0,0.5))));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRationalBSplineSurface surface = assertInstanceOf(
                StepRationalBSplineSurface.class,
                resolved.get(10)
        );
        assertEquals(2, surface.weightsData().size());
        assertEquals(2, surface.weightsData().getFirst().size());
        assertEquals(0, surface.uKnots().size());
    }

    @Test
    void shouldResolveRationalBsplineSurfaceWithKnots() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P10',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('P01',(0.0,2.0,0.0));
                #4=CARTESIAN_POINT('P11',(2.0,2.0,1.0));
                #10=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.)
                     RATIONAL_B_SPLINE_SURFACE(((1.0,1.0),(1.0,0.5))));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRationalBSplineSurface surface = assertInstanceOf(
                StepRationalBSplineSurface.class,
                resolved.get(10)
        );
        assertEquals(2, surface.uKnots().size());
        assertEquals(2, surface.vKnots().size());
    }

    @Test
    void shouldResolveUniformSurfaceMarker() {
        String step = """
                DATA;
                #1=(UNIFORM_SURFACE() BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('us'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepUniformSurface.class, entity.getClass());
        StepUniformSurface surface = assertInstanceOf(StepUniformSurface.class, entity);
        assertEquals("us", surface.name());
    }

    @Test
    void shouldResolveQuasiUniformSurfaceMarker() {
        String step = """
                DATA;
                #1=(QUASI_UNIFORM_SURFACE() BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('qus'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepQuasiUniformSurface.class, entity.getClass());
        StepQuasiUniformSurface surface = assertInstanceOf(StepQuasiUniformSurface.class, entity);
        assertEquals("qus", surface.name());
    }

    @Test
    void shouldResolveBezierSurfaceMarker() {
        String step = """
                DATA;
                #1=(BEZIER_SURFACE() BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('bsz'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepBezierSurface.class, entity.getClass());
        StepBezierSurface surface = assertInstanceOf(StepBezierSurface.class, entity);
        assertEquals("bsz", surface.name());
    }

    @Test
    void shouldResolvePiecewiseBezierSurfaceMarker() {
        String step = """
                DATA;
                #1=(PIECEWISE_BEZIER_SURFACE() BEZIER_SURFACE() BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('pbs'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepPiecewiseBezierSurface.class, entity.getClass());
        StepPiecewiseBezierSurface surface = assertInstanceOf(StepPiecewiseBezierSurface.class, entity);
        assertEquals("pbs", surface.name());
    }

    @Test
    void shouldResolveOffsetCurve3d() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=OFFSET_CURVE_3D('OC3',#4,2.5,.F.,#5);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepOffsetCurve3D offsetCurve = assertInstanceOf(StepOffsetCurve3D.class, resolved.get(6));
        assertEquals("OC3", offsetCurve.name());
        assertEquals(4, offsetCurve.basisCurve().id());
        assertEquals(2.5, offsetCurve.distance());
        assertEquals(false, offsetCurve.selfIntersect());
        assertEquals(5, offsetCurve.refDirection().id());
    }

    @Test
    void shouldResolveOffsetSurface() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #6=OFFSET_SURFACE('OS0',#5,1.5,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepOffsetSurface offsetSurface = assertInstanceOf(StepOffsetSurface.class, resolved.get(6));
        assertEquals("OS0", offsetSurface.name());
        assertEquals(5, offsetSurface.basisSurface().id());
        assertEquals(1.5, offsetSurface.distance());
        assertEquals(true, offsetSurface.selfIntersect());
    }

    @Test
    void shouldResolveCompositeCurveSegment() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=COMPOSITE_CURVE_SEGMENT(.CONTINUOUS.,.T.,#4);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepCompositeCurveSegment segment = assertInstanceOf(StepCompositeCurveSegment.class, resolved.get(5));
        assertEquals("CONTINUOUS", segment.transition());
        assertEquals(true, segment.sameSense());
        assertEquals(4, segment.parentCurve().id());
    }

    @Test
    void shouldResolveCompositeCurve() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=COMPOSITE_CURVE_SEGMENT(.CONTINUOUS.,.T.,#4);
                #6=(COMPOSITE_CURVE('CC0',(#5),.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('cc-name'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(6);
        assertSame(StepCompositeCurve.class, entity.getClass());
        StepCompositeCurve curve = assertInstanceOf(StepCompositeCurve.class, entity);
        assertEquals("CC0", curve.name());
        assertEquals(1, curve.segments().size());
        assertEquals(5, curve.segments().getFirst().id());
        assertEquals(false, curve.selfIntersect());
    }

    @Test
    void shouldResolveCompositeCurveOnSurface() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #6=LINE('L2D',#1,#7);
                #7=VECTOR('VX',#3,1.0);
                #8=REPRESENTATION('R2D',(#6),#9);
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('UV','PCURVE'));
                #10=PCURVE('PC0',#5,#8);
                #11=COMPOSITE_CURVE_SEGMENT(.CONTINUOUS.,.T.,#10);
                #12=(COMPOSITE_CURVE_ON_SURFACE('CCS0',(#11),.F.) COMPOSITE_CURVE('CCS0',(#11),.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('ccs-name'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(12);
        assertSame(StepCompositeCurveOnSurface.class, entity.getClass());
        StepCompositeCurveOnSurface curve = assertInstanceOf(StepCompositeCurveOnSurface.class, entity);
        assertEquals("CCS0", curve.name());
        assertEquals(1, curve.segments().size());
        assertEquals(11, curve.segments().getFirst().id());
        assertEquals(false, curve.selfIntersect());
    }

    @Test
    void shouldResolveToroidalSurfaceAndSplineTrimmedSurfaceCurve() {
        String step = """
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
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepToroidalSurface torus = assertInstanceOf(StepToroidalSurface.class, resolved.get(5));
        StepSurfaceCurve surfaceCurve = assertInstanceOf(StepSurfaceCurve.class, resolved.get(14));
        StepTrimmedCurve trimmedCurve = assertInstanceOf(StepTrimmedCurve.class, resolved.get(15));
        assertEquals(5.0, torus.majorRadius());
        assertEquals(1.0, torus.minorRadius());
        assertEquals(13, surfaceCurve.curve3d().id());
        assertEquals(14, trimmedCurve.basisCurve().id());
    }

    @Test
    void shouldResolveSphericalSurfaceAdvancedFaceGeometry() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=SPHERICAL_SURFACE('SPH',#4,2.0);
                #6=EDGE_LOOP('L0',());
                #7=FACE_OUTER_BOUND('B0',#6,.T.);
                #8=ADVANCED_FACE('F0',(#7),#5,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepSphericalSurface sphere = assertInstanceOf(StepSphericalSurface.class, resolved.get(5));
        StepAdvancedFace face = assertInstanceOf(StepAdvancedFace.class, resolved.get(8));
        assertEquals(2.0, sphere.radius());
        assertEquals(5, face.faceGeometry().id());
    }

    @Test
    void shouldResolvePresentationStyleAndLayerAssignments() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #6=EDGE_LOOP('L0',());
                #7=FACE_OUTER_BOUND('B0',#6,.T.);
                #8=ADVANCED_FACE('FACE0',(#7),#5,.T.);
                #20=COLOUR_RGB('Terracotta',0.8,0.4,0.2);
                #21=FILL_AREA_STYLE_COLOUR('',#20);
                #22=FILL_AREA_STYLE('',(#21));
                #23=SURFACE_STYLE_FILL_AREA(#22);
                #24=SURFACE_SIDE_STYLE('',(#23));
                #25=SURFACE_STYLE_USAGE(.BOTH.,#24);
                #26=PRESENTATION_STYLE_ASSIGNMENT((#25));
                #27=STYLED_ITEM('FACE_STYLE',(#26),#8);
                #28=PRESENTATION_LAYER_ASSIGNMENT('Inspection','Layer for QA',(#8));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepColourRgb colour = assertInstanceOf(StepColourRgb.class, resolved.get(20));
        StepStyledItem styledItem = assertInstanceOf(StepStyledItem.class, resolved.get(27));
        StepPresentationLayerAssignment layer = assertInstanceOf(StepPresentationLayerAssignment.class, resolved.get(28));
        assertEquals("Terracotta", colour.name());
        assertEquals(8, styledItem.item().id());
        assertEquals("Inspection", layer.name());
        assertEquals(1, layer.assignedItems().size());
    }

    @Test
    void shouldResolvePmiAndMeasureRepresentationItems() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,1.0,0.0));
                #3=ANNOTATION_TEXT_OCCURRENCE('PMI_NOTE','A=2.0',#2);
                #4=GEOMETRIC_CURVE_SET('PMI_LEADER',(#1,#2));
                #5=DRAUGHTING_CALLOUT('AREA_NOTE',(#3,#4));
                #6=ADVANCED_FACE('FACE0',(),#8,.T.);
                #7=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT($,.METRE.));
                #8=PLANE('PL0',#9);
                #9=AXIS2_PLACEMENT_3D('AX',#1,#10,#11);
                #10=DIRECTION('DZ',(0.0,0.0,1.0));
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=MEASURE_REPRESENTATION_ITEM('surface area',AREA_MEASURE(2.0),#7);
                #13=GEOMETRIC_ITEM_SPECIFIC_USAGE('callout->face','semantic PMI link',#5,#6);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationTextOccurrence text = assertInstanceOf(StepAnnotationTextOccurrence.class, resolved.get(3));
        StepGeometricCurveSet curveSet = assertInstanceOf(StepGeometricCurveSet.class, resolved.get(4));
        StepDraughtingCallout callout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(5));
        StepMeasureRepresentationItem measure = assertInstanceOf(StepMeasureRepresentationItem.class, resolved.get(12));
        StepGeometricItemSpecificUsage usage = assertInstanceOf(StepGeometricItemSpecificUsage.class, resolved.get(13));
        assertEquals("A=2.0", text.text());
        assertEquals(2, curveSet.elements().size());
        assertEquals(2, callout.contents().size());
        assertEquals("AREA_MEASURE", measure.measureType());
        assertEquals(2.0, measure.value());
        assertEquals(5, usage.usage().id());
        assertEquals(6, usage.identifiedItem().id());
    }

    @Test
    void shouldResolveGeometricSet() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #9=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #10=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=AXIS2_PLACEMENT_3D('AX',#1,#6,#2);
                #6=DIRECTION('DZ',(0.0,0.0,1.0));
                #7=PLANE('PL0',#5);
                #11=POLYLINE('PL0',(#1,#9,#10));
                #8=GEOMETRIC_SET('GS0',(#1,#4,#7,#11));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepGeometricSet set = assertInstanceOf(StepGeometricSet.class, resolved.get(8));
        assertEquals("GS0", set.name());
        assertEquals(4, set.elements().size());
    }

    @Test
    void shouldResolvePolyline() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=POLYLINE('PL0',(#1,#2,#3));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepPolyline polyline = assertInstanceOf(StepPolyline.class, resolved.get(4));
        assertEquals("PL0", polyline.name());
        assertEquals(3, polyline.points().size());
        assertEquals(1, polyline.points().getFirst().id());
    }

    @Test
    void shouldResolveGeometricCurveSetWithPolyline() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=POLYLINE('PL0',(#1,#2,#3));
                #5=GEOMETRIC_CURVE_SET('GC0',(#4,#1));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepGeometricCurveSet curveSet = assertInstanceOf(StepGeometricCurveSet.class, resolved.get(5));
        assertEquals("GC0", curveSet.name());
        assertEquals(2, curveSet.elements().size());
        assertSame(StepPolyline.class, curveSet.elements().getFirst().getClass());
    }

    @Test
    void shouldResolvePointSet() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,1.0,0.0));
                #3=POINT_SET('PS0',(#1,#2));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepPointSet set = assertInstanceOf(StepPointSet.class, resolved.get(3));
        assertEquals("PS0", set.name());
        assertEquals(2, set.points().size());
    }

    @Test
    void shouldResolveSurfaceModelMarker() {
        String step = """
                DATA;
                #1=(SURFACE_MODEL() REPRESENTATION_ITEM('sm0'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepSurfaceModel.class, entity.getClass());
        StepSurfaceModel surfaceModel = assertInstanceOf(StepSurfaceModel.class, entity);
        assertEquals("sm0", surfaceModel.name());
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

    @Test
    void shouldResolveComplexGeometricRepresentationContext() {
        String step = """
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepGeometricRepresentationContext context = assertInstanceOf(
                StepGeometricRepresentationContext.class,
                resolved.get(1)
        );
        assertEquals(3, context.coordinateSpaceDimension());
        assertEquals("ID", context.contextIdentifier());
    }

    @Test
    void shouldPreferGeometricRepresentationContextOverRepresentationContextForComplexEntity() {
        String step = """
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepGeometricRepresentationContext.class, entity.getClass());
        StepGeometricRepresentationContext context = assertInstanceOf(StepGeometricRepresentationContext.class, entity);
        assertEquals(3, context.coordinateSpaceDimension());
        assertEquals("MODEL", context.contextType());
    }

    @Test
    void shouldResolveShapeRepresentationAgainstGeometricContext() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=SHAPE_REPRESENTATION('POINT_REP',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        assertEquals("POINT_REP", representation.name());
        assertEquals(1, representation.items().size());
        assertEquals(true, representation.shapeRepresentation());
    }

    @Test
    void shouldResolveSiUnitComplexEntity() {
        String step = """
                DATA;
                #1=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepSiUnit unit = assertInstanceOf(StepSiUnit.class, resolved.get(1));
        assertEquals("LENGTH_UNIT", unit.unitKind());
        assertEquals("MILLI", unit.prefix());
        assertEquals("METRE", unit.unitName());
    }

    @Test
    void shouldResolveStandaloneLengthUnit() {
        String step = """
                DATA;
                #1=LENGTH_UNIT();
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepNamedUnit unit = assertInstanceOf(StepNamedUnit.class, resolved.get(1));
        assertEquals("LENGTH_UNIT", unit.unitKind());
    }

    @Test
    void shouldResolveStandaloneAdditionalUnits() {
        String step = """
                DATA;
                #1=AREA_UNIT();
                #2=VOLUME_UNIT();
                #3=TIME_UNIT();
                #4=THERMODYNAMIC_TEMPERATURE_UNIT();
                #5=ELECTRIC_CURRENT_UNIT();
                #6=AMOUNT_OF_SUBSTANCE_UNIT();
                #7=LUMINOUS_FLUX_UNIT();
                #8=LUMINOUS_INTENSITY_UNIT();
                #9=RATIO_UNIT();
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepNamedUnit area = assertInstanceOf(StepNamedUnit.class, resolved.get(1));
        StepNamedUnit volume = assertInstanceOf(StepNamedUnit.class, resolved.get(2));
        StepNamedUnit time = assertInstanceOf(StepNamedUnit.class, resolved.get(3));
        StepNamedUnit temperature = assertInstanceOf(StepNamedUnit.class, resolved.get(4));
        StepNamedUnit electricCurrent = assertInstanceOf(StepNamedUnit.class, resolved.get(5));
        StepNamedUnit amountOfSubstance = assertInstanceOf(StepNamedUnit.class, resolved.get(6));
        StepNamedUnit luminousFlux = assertInstanceOf(StepNamedUnit.class, resolved.get(7));
        StepNamedUnit luminousIntensity = assertInstanceOf(StepNamedUnit.class, resolved.get(8));
        StepNamedUnit ratio = assertInstanceOf(StepNamedUnit.class, resolved.get(9));
        assertEquals("AREA_UNIT", area.unitKind());
        assertEquals("VOLUME_UNIT", volume.unitKind());
        assertEquals("TIME_UNIT", time.unitKind());
        assertEquals("THERMODYNAMIC_TEMPERATURE_UNIT", temperature.unitKind());
        assertEquals("ELECTRIC_CURRENT_UNIT", electricCurrent.unitKind());
        assertEquals("AMOUNT_OF_SUBSTANCE_UNIT", amountOfSubstance.unitKind());
        assertEquals("LUMINOUS_FLUX_UNIT", luminousFlux.unitKind());
        assertEquals("LUMINOUS_INTENSITY_UNIT", luminousIntensity.unitKind());
        assertEquals("RATIO_UNIT", ratio.unitKind());
    }

    @Test
    void shouldResolveStandaloneAdditionalDerivedUnits() {
        String step = """
                DATA;
                #1=FREQUENCY_UNIT();
                #2=FORCE_UNIT();
                #3=PRESSURE_UNIT();
                #4=ENERGY_UNIT();
                #5=POWER_UNIT();
                #6=ELECTRIC_CHARGE_UNIT();
                #7=ELECTRIC_POTENTIAL_UNIT();
                #8=CAPACITANCE_UNIT();
                #9=RESISTANCE_UNIT();
                #10=CONDUCTANCE_UNIT();
                #11=MAGNETIC_FLUX_UNIT();
                #12=MAGNETIC_FLUX_DENSITY_UNIT();
                #13=INDUCTANCE_UNIT();
                #14=ILLUMINANCE_UNIT();
                #15=RADIOACTIVITY_UNIT();
                #16=ABSORBED_DOSE_UNIT();
                #17=DOSE_EQUIVALENT_UNIT();
                #18=ACCELERATION_UNIT();
                #19=VELOCITY_UNIT();
                #20=THERMAL_RESISTANCE_UNIT();
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertEquals("FREQUENCY_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(1)).unitKind());
        assertEquals("FORCE_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(2)).unitKind());
        assertEquals("PRESSURE_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(3)).unitKind());
        assertEquals("ENERGY_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(4)).unitKind());
        assertEquals("POWER_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(5)).unitKind());
        assertEquals("ELECTRIC_CHARGE_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(6)).unitKind());
        assertEquals("ELECTRIC_POTENTIAL_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(7)).unitKind());
        assertEquals("CAPACITANCE_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(8)).unitKind());
        assertEquals("RESISTANCE_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(9)).unitKind());
        assertEquals("CONDUCTANCE_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(10)).unitKind());
        assertEquals("MAGNETIC_FLUX_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(11)).unitKind());
        assertEquals("MAGNETIC_FLUX_DENSITY_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(12)).unitKind());
        assertEquals("INDUCTANCE_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(13)).unitKind());
        assertEquals("ILLUMINANCE_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(14)).unitKind());
        assertEquals("RADIOACTIVITY_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(15)).unitKind());
        assertEquals("ABSORBED_DOSE_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(16)).unitKind());
        assertEquals("DOSE_EQUIVALENT_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(17)).unitKind());
        assertEquals("ACCELERATION_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(18)).unitKind());
        assertEquals("VELOCITY_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(19)).unitKind());
        assertEquals("THERMAL_RESISTANCE_UNIT", assertInstanceOf(StepDerivedUnit.class, resolved.get(20)).unitKind());
    }

    @Test
    void shouldResolveSiForceUnitAsSpecificUnitKind() {
        String step = """
                DATA;
                #1=(FORCE_UNIT() NAMED_UNIT(*) SI_UNIT($,.NEWTON.));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepSiUnit unit = assertInstanceOf(StepSiUnit.class, resolved.get(1));
        assertEquals("FORCE_UNIT", unit.unitKind());
        assertEquals("NEWTON", unit.unitName());
    }

    @Test
    void shouldResolveConversionBasedPlaneAngleUnit() {
        String step = """
                DATA;
                #1=(PLANE_ANGLE_UNIT() NAMED_UNIT(*) SI_UNIT($,.RADIAN.));
                #2=MEASURE_WITH_UNIT(PLANE_ANGLE_MEASURE(0.0174532925199433),#1);
                #3=(CONVERSION_BASED_UNIT('DEGREE',#2) NAMED_UNIT(*) PLANE_ANGLE_UNIT());
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(3);
        assertSame(StepConversionBasedUnit.class, entity.getClass());
        StepConversionBasedUnit unit = assertInstanceOf(StepConversionBasedUnit.class, entity);
        assertEquals("DEGREE", unit.name());
        assertEquals("PLANE_ANGLE_UNIT", unit.unitKind());
        assertEquals(2, unit.conversionFactor().id());
    }

    @Test
    void shouldResolveContextDependentUnit() {
        String step = """
                DATA;
                #1=(CONTEXT_DEPENDENT_UNIT('BOX') NAMED_UNIT(*));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepContextDependentUnit.class, entity.getClass());
        StepContextDependentUnit unit = assertInstanceOf(StepContextDependentUnit.class, entity);
        assertEquals("BOX", unit.name());
        assertEquals("NAMED_UNIT", unit.unitKind());
    }

    @Test
    void shouldResolveConversionBasedUnitWithOffset() {
        String step = """
                DATA;
                #1=(THERMODYNAMIC_TEMPERATURE_UNIT() NAMED_UNIT(*) SI_UNIT($,.KELVIN.));
                #2=MEASURE_WITH_UNIT(THERMODYNAMIC_TEMPERATURE_MEASURE(1.0),#1);
                #3=(CONVERSION_BASED_UNIT_WITH_OFFSET(THERMODYNAMIC_TEMPERATURE_MEASURE(273.15))
                    CONVERSION_BASED_UNIT('DEG_C',#2)
                    NAMED_UNIT(*)
                    THERMODYNAMIC_TEMPERATURE_UNIT());
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(3);
        assertSame(StepConversionBasedUnitWithOffset.class, entity.getClass());
        StepConversionBasedUnitWithOffset unit =
            assertInstanceOf(StepConversionBasedUnitWithOffset.class, entity);
        assertEquals("DEG_C", unit.name());
        assertEquals("THERMODYNAMIC_TEMPERATURE_UNIT", unit.unitKind());
        assertEquals(2, unit.conversionFactor().id());
        assertEquals(273.15, unit.conversionOffset());
    }

    @Test
    void shouldResolveTypedMeasureWithUnitSubtypes() {
        String step = """
                DATA;
                #1=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));
                #2=MASS_UNIT();
                #3=TIME_UNIT();
                #4=AREA_UNIT();
                #5=VOLUME_UNIT();
                #6=(PLANE_ANGLE_UNIT() NAMED_UNIT(*) SI_UNIT($,.RADIAN.));
                #7=(SOLID_ANGLE_UNIT() NAMED_UNIT(*) SI_UNIT($,.STERADIAN.));
                #8=RATIO_UNIT();
                #9=(THERMODYNAMIC_TEMPERATURE_UNIT() NAMED_UNIT(*) SI_UNIT($,.KELVIN.));
                #10=ELECTRIC_CURRENT_UNIT();
                #11=LENGTH_MEASURE_WITH_UNIT(LENGTH_MEASURE(12.5),#1);
                #12=MASS_MEASURE_WITH_UNIT(MASS_MEASURE(3.0),#2);
                #13=TIME_MEASURE_WITH_UNIT(TIME_MEASURE(2.0),#3);
                #14=AREA_MEASURE_WITH_UNIT(AREA_MEASURE(6.0),#4);
                #15=VOLUME_MEASURE_WITH_UNIT(VOLUME_MEASURE(7.0),#5);
                #16=PLANE_ANGLE_MEASURE_WITH_UNIT(PLANE_ANGLE_MEASURE(0.5),#6);
                #17=SOLID_ANGLE_MEASURE_WITH_UNIT(SOLID_ANGLE_MEASURE(1.5),#7);
                #18=RATIO_MEASURE_WITH_UNIT(RATIO_MEASURE(0.25),#8);
                #19=THERMODYNAMIC_TEMPERATURE_MEASURE_WITH_UNIT(THERMODYNAMIC_TEMPERATURE_MEASURE(300.0),#9);
                #20=ELECTRIC_CURRENT_MEASURE_WITH_UNIT(ELECTRIC_CURRENT_MEASURE(1.2),#10);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertEquals("LENGTH_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(11)).entityName());
        assertEquals("MASS_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(12)).entityName());
        assertEquals("TIME_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(13)).entityName());
        assertEquals("AREA_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(14)).entityName());
        assertEquals("VOLUME_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(15)).entityName());
        assertEquals("PLANE_ANGLE_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(16)).entityName());
        assertEquals("SOLID_ANGLE_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(17)).entityName());
        assertEquals("RATIO_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(18)).entityName());
        assertEquals("THERMODYNAMIC_TEMPERATURE_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(19)).entityName());
        assertEquals("ELECTRIC_CURRENT_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(20)).entityName());
    }

    @Test
    void shouldResolveAdditionalTypedMeasureWithUnitSubtypes() {
        String step = """
                DATA;
                #1=FREQUENCY_UNIT();
                #2=FORCE_UNIT();
                #3=PRESSURE_UNIT();
                #4=ENERGY_UNIT();
                #5=POWER_UNIT();
                #6=ELECTRIC_POTENTIAL_UNIT();
                #7=RESISTANCE_UNIT();
                #8=CONDUCTANCE_UNIT();
                #9=MAGNETIC_FLUX_UNIT();
                #10=ILLUMINANCE_UNIT();
                #11=LUMINOUS_FLUX_UNIT();
                #12=LUMINOUS_INTENSITY_UNIT();
                #21=FREQUENCY_MEASURE_WITH_UNIT(FREQUENCY_MEASURE(50.0),#1);
                #22=FORCE_MEASURE_WITH_UNIT(FORCE_MEASURE(100.0),#2);
                #23=PRESSURE_MEASURE_WITH_UNIT(PRESSURE_MEASURE(1.5),#3);
                #24=ENERGY_MEASURE_WITH_UNIT(ENERGY_MEASURE(42.0),#4);
                #25=POWER_MEASURE_WITH_UNIT(POWER_MEASURE(3.5),#5);
                #26=ELECTRIC_POTENTIAL_MEASURE_WITH_UNIT(ELECTRIC_POTENTIAL_MEASURE(220.0),#6);
                #27=RESISTANCE_MEASURE_WITH_UNIT(RESISTANCE_MEASURE(10.0),#7);
                #28=CONDUCTANCE_MEASURE_WITH_UNIT(CONDUCTANCE_MEASURE(0.1),#8);
                #29=MAGNETIC_FLUX_MEASURE_WITH_UNIT(MAGNETIC_FLUX_MEASURE(0.02),#9);
                #30=ILLUMINANCE_MEASURE_WITH_UNIT(ILLUMINANCE_MEASURE(500.0),#10);
                #31=LUMINOUS_FLUX_MEASURE_WITH_UNIT(LUMINOUS_FLUX_MEASURE(800.0),#11);
                #32=LUMINOUS_INTENSITY_MEASURE_WITH_UNIT(LUMINOUS_INTENSITY_MEASURE(120.0),#12);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertEquals("FREQUENCY_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(21)).entityName());
        assertEquals("FORCE_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(22)).entityName());
        assertEquals("PRESSURE_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(23)).entityName());
        assertEquals("ENERGY_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(24)).entityName());
        assertEquals("POWER_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(25)).entityName());
        assertEquals("ELECTRIC_POTENTIAL_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(26)).entityName());
        assertEquals("RESISTANCE_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(27)).entityName());
        assertEquals("CONDUCTANCE_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(28)).entityName());
        assertEquals("MAGNETIC_FLUX_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(29)).entityName());
        assertEquals("ILLUMINANCE_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(30)).entityName());
        assertEquals("LUMINOUS_FLUX_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(31)).entityName());
        assertEquals("LUMINOUS_INTENSITY_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(32)).entityName());
    }

    @Test
    void shouldResolveRemainingTypedMeasureWithUnitSubtypes() {
        String step = """
                DATA;
                #1=AMOUNT_OF_SUBSTANCE_UNIT();
                #2=ELECTRIC_CHARGE_UNIT();
                #3=CAPACITANCE_UNIT();
                #4=MAGNETIC_FLUX_DENSITY_UNIT();
                #5=INDUCTANCE_UNIT();
                #6=RADIOACTIVITY_UNIT();
                #7=ABSORBED_DOSE_UNIT();
                #8=DOSE_EQUIVALENT_UNIT();
                #9=ACCELERATION_UNIT();
                #10=VELOCITY_UNIT();
                #11=THERMAL_RESISTANCE_UNIT();
                #21=AMOUNT_OF_SUBSTANCE_MEASURE_WITH_UNIT(AMOUNT_OF_SUBSTANCE_MEASURE(2.5),#1);
                #22=ELECTRIC_CHARGE_MEASURE_WITH_UNIT(ELECTRIC_CHARGE_MEASURE(1.6),#2);
                #23=CAPACITANCE_MEASURE_WITH_UNIT(CAPACITANCE_MEASURE(0.047),#3);
                #24=MAGNETIC_FLUX_DENSITY_MEASURE_WITH_UNIT(MAGNETIC_FLUX_DENSITY_MEASURE(0.12),#4);
                #25=INDUCTANCE_MEASURE_WITH_UNIT(INDUCTANCE_MEASURE(0.008),#5);
                #26=RADIOACTIVITY_MEASURE_WITH_UNIT(RADIOACTIVITY_MEASURE(3.0),#6);
                #27=ABSORBED_DOSE_MEASURE_WITH_UNIT(ABSORBED_DOSE_MEASURE(0.4),#7);
                #28=DOSE_EQUIVALENT_MEASURE_WITH_UNIT(DOSE_EQUIVALENT_MEASURE(0.6),#8);
                #29=ACCELERATION_MEASURE_WITH_UNIT(ACCELERATION_MEASURE(9.81),#9);
                #30=VELOCITY_MEASURE_WITH_UNIT(VELOCITY_MEASURE(12.0),#10);
                #31=THERMAL_RESISTANCE_MEASURE_WITH_UNIT(THERMAL_RESISTANCE_MEASURE(0.15),#11);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertEquals("AMOUNT_OF_SUBSTANCE_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(21)).entityName());
        assertEquals("ELECTRIC_CHARGE_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(22)).entityName());
        assertEquals("CAPACITANCE_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(23)).entityName());
        assertEquals("MAGNETIC_FLUX_DENSITY_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(24)).entityName());
        assertEquals("INDUCTANCE_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(25)).entityName());
        assertEquals("RADIOACTIVITY_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(26)).entityName());
        assertEquals("ABSORBED_DOSE_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(27)).entityName());
        assertEquals("DOSE_EQUIVALENT_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(28)).entityName());
        assertEquals("ACCELERATION_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(29)).entityName());
        assertEquals("VELOCITY_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(30)).entityName());
        assertEquals("THERMAL_RESISTANCE_MEASURE_WITH_UNIT", assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(31)).entityName());
    }

    @Test
    void shouldResolveSolidModelMarkerWithoutStealingManifoldSolidBrep() {
        String markerStep = """
                DATA;
                #1=(SOLID_MODEL() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('solid-item'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(markerStep));

        StepSolidModel solidModel = assertInstanceOf(StepSolidModel.class, resolved.get(1));
        assertEquals("solid-item", solidModel.name());

        String brepStep = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX0',#1,#2,#3);
                #5=PLANE('PL0',#4);
                #6=VERTEX_POINT('V0',#1);
                #7=VERTEX_LOOP('VL0',#6);
                #8=FACE_OUTER_BOUND('B0',#7,.T.);
                #9=ADVANCED_FACE('F0',(#8),#5,.T.);
                #10=CLOSED_SHELL('CS0',(#9));
                #11=(MANIFOLD_SOLID_BREP('MSB0',#10) SOLID_MODEL() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('solid-item'));
                ENDSEC;
                """;

        resolved = StepEntityResolver.resolveAll(StepParser.parse(brepStep));

        assertSame(StepManifoldSolidBrep.class, resolved.get(11).getClass());
    }

    @Test
    void shouldResolveRepresentationItem() {
        String step = """
                DATA;
                #1=REPRESENTATION_ITEM('item-1');
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentationItem item = assertInstanceOf(StepRepresentationItem.class, resolved.get(1));
        assertEquals("item-1", item.name());
    }

    @Test
    void shouldResolveValueRepresentationItem() {
        String step = """
                DATA;
                #1=VALUE_REPRESENTATION_ITEM('roughness',DESCRIPTIVE_MEASURE('Ra 3.2'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepValueRepresentationItem item =
                assertInstanceOf(StepValueRepresentationItem.class, resolved.get(1));
        assertEquals("roughness", item.name());
        assertEquals("DESCRIPTIVE_MEASURE", item.valueType());
        assertEquals("Ra 3.2", item.valueText());
    }

    @Test
    void shouldPreferGeometricRepresentationItemOverRepresentationItemForComplexEntity() {
        String step = """
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('geom-item'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepGeometricRepresentationItem.class, entity.getClass());
        StepGeometricRepresentationItem item = assertInstanceOf(StepGeometricRepresentationItem.class, entity);
        assertEquals("geom-item", item.name());
    }

    @Test
    void shouldResolvePointMarker() {
        String step = """
                DATA;
                #1=(POINT() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('p'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepPoint.class, entity.getClass());
        StepPoint point = assertInstanceOf(StepPoint.class, entity);
        assertEquals("p", point.name());
    }

    @Test
    void shouldResolveCurveMarker() {
        String step = """
                DATA;
                #1=(CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('c'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepCurve.class, entity.getClass());
        StepCurve curve = assertInstanceOf(StepCurve.class, entity);
        assertEquals("c", curve.name());
    }

    @Test
    void shouldResolveSurfaceMarker() {
        String step = """
                DATA;
                #1=(SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('s'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepSurface.class, entity.getClass());
        StepSurface surface = assertInstanceOf(StepSurface.class, entity);
        assertEquals("s", surface.name());
    }

    @Test
    void shouldResolveTopologicalRepresentationItem() {
        String step = """
                DATA;
                #1=TOPOLOGICAL_REPRESENTATION_ITEM('topo');
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepTopologicalRepresentationItem item = assertInstanceOf(
                StepTopologicalRepresentationItem.class,
                resolved.get(1)
        );
        assertEquals("topo", item.name());
    }

    @Test
    void shouldResolveVertexMarker() {
        String step = """
                DATA;
                #1=(VERTEX() TOPOLOGICAL_REPRESENTATION_ITEM('v'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepVertex.class, entity.getClass());
        StepVertex vertex = assertInstanceOf(StepVertex.class, entity);
        assertEquals("v", vertex.name());
    }

    @Test
    void shouldResolveEdgeMarker() {
        String step = """
                DATA;
                #1=(EDGE() TOPOLOGICAL_REPRESENTATION_ITEM('e'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepEdge.class, entity.getClass());
        StepEdge edge = assertInstanceOf(StepEdge.class, entity);
        assertEquals("e", edge.name());
    }

    @Test
    void shouldResolveFaceMarker() {
        String step = """
                DATA;
                #1=(FACE() TOPOLOGICAL_REPRESENTATION_ITEM('f'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepFace.class, entity.getClass());
        StepFace face = assertInstanceOf(StepFace.class, entity);
        assertEquals("f", face.name());
    }

    @Test
    void shouldResolveSubedge() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=VECTOR('VX',#3,1.0);
                #5=LINE('L0',#1,#4);
                #6=VERTEX_POINT('V0',#1);
                #7=VERTEX_POINT('V1',#2);
                #8=EDGE_CURVE('E0',#6,#7,#5,.T.);
                #9=(SUBEDGE('SE0',#6,#7,#8) EDGE() TOPOLOGICAL_REPRESENTATION_ITEM('subedge'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(9);
        assertSame(StepSubedge.class, entity.getClass());
        StepSubedge subedge = assertInstanceOf(StepSubedge.class, entity);
        assertEquals("SE0", subedge.name());
        assertEquals(6, subedge.start().id());
        assertEquals(7, subedge.end().id());
        assertEquals(8, subedge.parentEdge().id());
    }

    @Test
    void shouldResolveConnectedEdgeSet() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=VECTOR('VX',#3,1.0);
                #5=LINE('L0',#1,#4);
                #6=VERTEX_POINT('V0',#1);
                #7=VERTEX_POINT('V1',#2);
                #8=EDGE_CURVE('E0',#6,#7,#5,.T.);
                #9=CONNECTED_EDGE_SET('CES0',(#8));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepConnectedEdgeSet edgeSet = assertInstanceOf(StepConnectedEdgeSet.class, resolved.get(9));
        assertEquals("CES0", edgeSet.name());
        assertEquals(1, edgeSet.edges().size());
        assertEquals(8, edgeSet.edges().getFirst().id());
    }

    @Test
    void shouldResolveEdgeBasedWireframeModel() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=VECTOR('VX',#3,1.0);
                #5=LINE('L0',#1,#4);
                #6=VERTEX_POINT('V0',#1);
                #7=VERTEX_POINT('V1',#2);
                #8=EDGE_CURVE('E0',#6,#7,#5,.T.);
                #9=CONNECTED_EDGE_SET('CES0',(#8));
                #10=(EDGE_BASED_WIREFRAME_MODEL('WBM',(#9)) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('wire'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(10);
        assertSame(StepEdgeBasedWireframeModel.class, entity.getClass());
        StepEdgeBasedWireframeModel model = assertInstanceOf(StepEdgeBasedWireframeModel.class, entity);
        assertEquals("WBM", model.name());
        assertEquals(1, model.boundaries().size());
        assertEquals(9, model.boundaries().getFirst().id());
    }

    @Test
    void shouldPreferSiUnitOverNamedUnitForComplexEntity() {
        String step = """
                DATA;
                #1=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(1);
        assertSame(StepSiUnit.class, entity.getClass());
        StepSiUnit unit = assertInstanceOf(StepSiUnit.class, entity);
        assertEquals("LENGTH_UNIT", unit.unitKind());
    }

    @Test
    void shouldPreferFaceOuterBoundOverFaceBoundForComplexEntity() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('VX',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=VERTEX_POINT('V0',#1);
                #6=EDGE_CURVE('E0',#5,#5,#4,.T.);
                #7=ORIENTED_EDGE('OE0',$,$,#6,.T.);
                #8=EDGE_LOOP('LOOP',(#7));
                #9=(FACE_OUTER_BOUND('B0',#8,.T.) FACE_BOUND());
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(9);
        assertSame(StepFaceBound.class, entity.getClass());
        StepFaceBound bound = assertInstanceOf(StepFaceBound.class, entity);
        assertEquals(true, bound.outer());
        assertEquals(8, bound.loop().id());
    }

    @Test
    void shouldResolveMeasureWithUnitUsingTypedMeasureValue() {
        String step = """
                DATA;
                #1=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));
                #2=MEASURE_WITH_UNIT(LENGTH_MEASURE(12.5),#1);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepMeasureWithUnit measure = assertInstanceOf(StepMeasureWithUnit.class, resolved.get(2));
        assertEquals(12.5, measure.valueComponent());
        assertEquals(1, measure.unitComponent().id());
    }

    @Test
    void shouldResolveProductDefinitionLinkedToShapeRepresentation() {
        String step = """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('P-001','Bracket','Demo part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','first release',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('def-1','main definition',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('shape','primary shape',#6);
                #8=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #9=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #10=SHAPE_REPRESENTATION('BODY',(#8),#9);
                #11=SHAPE_DEFINITION_REPRESENTATION(#7,#10);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepProduct product = assertInstanceOf(StepProduct.class, resolved.get(3));
        assertEquals("P-001", product.identifier());
        assertEquals(1, product.frameOfReference().size());

        StepProductDefinition definition = assertInstanceOf(StepProductDefinition.class, resolved.get(6));
        assertEquals(4, definition.formation().id());

        StepProductDefinitionShape shape = assertInstanceOf(StepProductDefinitionShape.class, resolved.get(7));
        assertEquals(6, shape.definition().id());

        StepShapeDefinitionRepresentation link = assertInstanceOf(
                StepShapeDefinitionRepresentation.class,
                resolved.get(11)
        );
        assertEquals(7, link.definition().id());
        assertEquals(10, link.usedRepresentation().id());
    }

    @Test
    void shouldResolveEdgeBasedWireframeShapeRepresentation() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=EDGE_BASED_WIREFRAME_SHAPE_REPRESENTATION('WIRE',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        assertEquals("WIRE", representation.name());
        assertEquals(true, representation.shapeRepresentation());
        assertEquals(1, representation.items().size());
        assertEquals(1, representation.items().getFirst().id());
    }

    @Test
    void shouldResolveGeometricallyBoundedWireframeShapeRepresentation() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=GEOMETRICALLY_BOUNDED_WIREFRAME_SHAPE_REPRESENTATION('GBW',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        assertEquals("GBW", representation.name());
        assertEquals(true, representation.shapeRepresentation());
    }

    @Test
    void shouldResolveGeometricallyBounded2dWireframeRepresentation() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','PLAN'));
                #3=GEOMETRICALLY_BOUNDED_2D_WIREFRAME_REPRESENTATION('GB2D',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        assertEquals("GB2D", representation.name());
        assertEquals(true, representation.shapeRepresentation());
    }

    @Test
    void shouldResolveShellBasedWireframeShapeRepresentation() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=SHELL_BASED_WIREFRAME_SHAPE_REPRESENTATION('SBW',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        assertEquals("SBW", representation.name());
        assertEquals(true, representation.shapeRepresentation());
    }

    @Test
    void shouldResolveManifoldSurfaceShapeRepresentation() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=MANIFOLD_SURFACE_SHAPE_REPRESENTATION('SURF',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        assertEquals("SURF", representation.name());
        assertEquals(true, representation.shapeRepresentation());
    }

    @Test
    void shouldResolveSurfaceShapeRepresentation() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=SURFACE_SHAPE_REPRESENTATION('SSR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        assertEquals("SSR", representation.name());
        assertEquals(true, representation.shapeRepresentation());
    }

    @Test
    void shouldResolveGeometricallyBoundedSurfaceShapeRepresentation() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=GEOMETRICALLY_BOUNDED_SURFACE_SHAPE_REPRESENTATION('GBSR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        assertEquals("GBSR", representation.name());
        assertEquals(true, representation.shapeRepresentation());
    }

    @Test
    void shouldResolvePathShapeRepresentation() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=PATH_SHAPE_REPRESENTATION('PSR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        assertEquals("PSR", representation.name());
        assertEquals(true, representation.shapeRepresentation());
    }

    @Test
    void shouldResolveWireframeShapeRepresentation() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=WIREFRAME_SHAPE_REPRESENTATION('WSR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        assertEquals("WSR", representation.name());
        assertEquals(true, representation.shapeRepresentation());
    }

    @Test
    void shouldResolveFaceShapeRepresentation() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=FACE_SHAPE_REPRESENTATION('FSR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        assertEquals("FSR", representation.name());
        assertEquals(true, representation.shapeRepresentation());
    }

    @Test
    void shouldResolveFacetedBrepShapeRepresentation() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=FACETED_BREP_SHAPE_REPRESENTATION('FBSR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        assertEquals("FBSR", representation.name());
        assertEquals(true, representation.shapeRepresentation());
    }

    @Test
    void shouldResolveElementaryBrepShapeRepresentation() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=ELEMENTARY_BREP_SHAPE_REPRESENTATION('EBSR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        assertEquals("EBSR", representation.name());
        assertEquals(true, representation.shapeRepresentation());
    }

    @Test
    void shouldResolveCsgShapeRepresentation() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=CSG_SHAPE_REPRESENTATION('CSG',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        assertEquals("CSG", representation.name());
        assertEquals(true, representation.shapeRepresentation());
    }

    @Test
    void shouldResolveBooleanResult() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(19);
        assertSame(StepBooleanResult.class, entity.getClass());
        StepBooleanResult result = assertInstanceOf(StepBooleanResult.class, entity);
        assertEquals("BOOL0", result.name());
        assertEquals("UNION", result.operator());
        assertEquals(17, result.firstOperand().id());
        assertEquals(18, result.secondOperand().id());
    }

    @Test
    void shouldResolveBooleanClippingResult() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEntity entity = resolved.get(19);
        assertSame(StepBooleanClippingResult.class, entity.getClass());
        StepBooleanClippingResult result = assertInstanceOf(StepBooleanClippingResult.class, entity);
        assertEquals("BCR0", result.name());
        assertEquals("DIFFERENCE", result.operator());
        assertEquals(17, result.firstOperand().id());
        assertEquals(18, result.secondOperand().id());
    }

    @Test
    void shouldResolveNonManifoldSurfaceShapeRepresentation() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=NON_MANIFOLD_SURFACE_SHAPE_REPRESENTATION('NMSR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        assertEquals("NMSR", representation.name());
        assertEquals(true, representation.shapeRepresentation());
    }

    @Test
    void shouldResolveFacetedBrep() {
        String step = """
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
                #15=CLOSED_SHELL('CS',(#13));
                #16=FACETED_BREP('FB',#15);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepManifoldSolidBrep brep = assertInstanceOf(StepManifoldSolidBrep.class, resolved.get(16));
        assertEquals("FB", brep.name());
        assertEquals(15, brep.outer().id());
    }

    @Test
    void shouldResolveShellBasedSurfaceModel() {
        String step = """
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
                #15=OPEN_SHELL('OS',(#13));
                #16=SHELL_BASED_SURFACE_MODEL('SBSM',(#15));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepShellBasedSurfaceModel model = assertInstanceOf(
                StepShellBasedSurfaceModel.class,
                resolved.get(16)
        );
        assertEquals("SBSM", model.name());
        assertEquals(1, model.shells().size());
        assertEquals(15, model.shells().getFirst().id());
    }

    @Test
    void shouldResolveBrepWithVoids() {
        String step = """
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
                #15=CLOSED_SHELL('OUTER',(#13));
                #16=CLOSED_SHELL('VOID0',(#13));
                #17=BREP_WITH_VOIDS('BWV',#15,(#16));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepBrepWithVoids brep = assertInstanceOf(StepBrepWithVoids.class, resolved.get(17));
        assertEquals("BWV", brep.name());
        assertEquals(15, brep.outer().id());
        assertEquals(1, brep.voids().size());
        assertEquals(16, brep.voids().getFirst().id());
    }

    @Test
    void shouldResolveProductDefinitionShapeLinkedToAssemblyOccurrence() {
        String step = """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('ASM','assembly','',(#2));
                #4=PRODUCT('PRT','part','',(#2));
                #5=PRODUCT_DEFINITION_FORMATION('asm-v1','first',#3);
                #6=PRODUCT_DEFINITION_FORMATION('prt-v1','first',#4);
                #7=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #8=PRODUCT_DEFINITION('asm-def','assembly def',#5,#7);
                #9=PRODUCT_DEFINITION('prt-def','part def',#6,#7);
                #10=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ-1','component 1','mounted',#8,#9);
                #11=PRODUCT_DEFINITION_SHAPE('shape','occurrence shape',#10);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepProductDefinitionShape shape = assertInstanceOf(StepProductDefinitionShape.class, resolved.get(11));
        StepNextAssemblyUsageOccurrence occurrence = assertInstanceOf(
                StepNextAssemblyUsageOccurrence.class,
                shape.definition()
        );
        assertEquals(10, occurrence.id());
    }

    @Test
    void shouldResolveShapeRepresentationRelationship() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #4=SHAPE_REPRESENTATION('REP_A',(#1),#3);
                #5=SHAPE_REPRESENTATION('REP_B',(#2),#3);
                #6=SHAPE_REPRESENTATION_RELATIONSHIP('map','assembly link',#4,#5);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepShapeRepresentationRelationship relationship = assertInstanceOf(
                StepShapeRepresentationRelationship.class,
                resolved.get(6)
        );
        assertEquals(4, relationship.rep1().id());
        assertEquals(5, relationship.rep2().id());
    }

    @Test
    void shouldResolveContextDependentShapeRepresentationForOccurrence() {
        String step = """
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
                #10=CARTESIAN_POINT('PA',(0.0,0.0,0.0));
                #11=CARTESIAN_POINT('PB',(1.0,0.0,0.0));
                #12=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #13=SHAPE_REPRESENTATION('ASM_SHAPE',(#10),#12);
                #14=SHAPE_REPRESENTATION('PART_SHAPE',(#11),#12);
                #15=SHAPE_REPRESENTATION_RELATIONSHIP('placement','occurrence shape',#13,#14);
                #16=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ-1','component 1','mounted',#8,#9);
                #17=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#15,#16);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepNextAssemblyUsageOccurrence occurrence = assertInstanceOf(
                StepNextAssemblyUsageOccurrence.class,
                resolved.get(16)
        );
        assertEquals(8, occurrence.relatingProductDefinition().id());
        assertEquals(9, occurrence.relatedProductDefinition().id());
        assertEquals(null, occurrence.referenceDesignator());

        StepContextDependentShapeRepresentation contextRepresentation = assertInstanceOf(
                StepContextDependentShapeRepresentation.class,
                resolved.get(17)
        );
        assertEquals(15, contextRepresentation.representationRelationship().id());
        assertEquals(16, contextRepresentation.representedProductRelation().id());
    }

    @Test
    void shouldResolveContextDependentShapeRepresentationForPlainRepresentationRelationship() {
        String step = """
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
                #10=CARTESIAN_POINT('PA',(0.0,0.0,0.0));
                #11=CARTESIAN_POINT('PB',(1.0,0.0,0.0));
                #12=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #13=REPRESENTATION('ASM_REP',(#10),#12);
                #14=REPRESENTATION('PART_REP',(#11),#12);
                #15=REPRESENTATION_RELATIONSHIP('placement','occurrence shape',#13,#14);
                #16=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ-1','component 1','mounted',#8,#9);
                #17=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#15,#16);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepContextDependentShapeRepresentation contextRepresentation = assertInstanceOf(
                StepContextDependentShapeRepresentation.class,
                resolved.get(17)
        );
        StepRepresentationRelationship relationship = assertInstanceOf(
                StepRepresentationRelationship.class,
                contextRepresentation.representationRelationship()
        );
        assertEquals(13, relationship.rep1().id());
        assertEquals(14, relationship.rep2().id());
    }

    @Test
    void shouldResolveNextAssemblyUsageOccurrenceWithReferenceDesignator() {
        String step = """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('ASM','assembly','',(#2));
                #4=PRODUCT('PRT','part','',(#2));
                #5=PRODUCT_DEFINITION_FORMATION('asm-v1','first',#3);
                #6=PRODUCT_DEFINITION_FORMATION('prt-v1','first',#4);
                #7=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #8=PRODUCT_DEFINITION('asm-def','assembly def',#5,#7);
                #9=PRODUCT_DEFINITION('prt-def','part def',#6,#7);
                #10=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ-1','component 1','mounted',#8,#9,'A-01');
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepNextAssemblyUsageOccurrence occurrence = assertInstanceOf(
                StepNextAssemblyUsageOccurrence.class,
                resolved.get(10)
        );
        assertEquals("A-01", occurrence.referenceDesignator());
    }

    @Test
    void shouldResolveContextDependentShapeRepresentationViaProductDefinitionShape() {
        String step = """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('ASM','assembly','',(#2));
                #4=PRODUCT('PRT','part','',(#2));
                #5=PRODUCT_DEFINITION_FORMATION('asm-v1','first',#3);
                #6=PRODUCT_DEFINITION_FORMATION('prt-v1','first',#4);
                #7=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #8=PRODUCT_DEFINITION('asm-def','assembly def',#5,#7);
                #9=PRODUCT_DEFINITION('prt-def','part def',#6,#7);
                #10=NEXT_ASSEMBLY_USAGE_OCCURRENCE('occ-1','component 1','mounted',#8,#9);
                #11=PRODUCT_DEFINITION_SHAPE('shape','occurrence shape',#10);
                #12=SHAPE_REPRESENTATION_RELATIONSHIP('rr','shape link',#13,#14);
                #13=SHAPE_REPRESENTATION('ASM',(),#15);
                #14=SHAPE_REPRESENTATION('PRT',(),#15);
                #15=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #16=CONTEXT_DEPENDENT_SHAPE_REPRESENTATION(#12,#11);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepContextDependentShapeRepresentation representation = assertInstanceOf(
                StepContextDependentShapeRepresentation.class,
                resolved.get(16)
        );
        StepProductDefinitionShape shape = assertInstanceOf(
                StepProductDefinitionShape.class,
                representation.representedProductRelation()
        );
        assertEquals(11, shape.id());
    }

    @Test
    void shouldResolveGlobalUnitAndUncertaintyAssignedContexts() {
        String step = """
                DATA;
                #1=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));
                #2=(PLANE_ANGLE_UNIT() NAMED_UNIT(*) SI_UNIT($,.RADIAN.));
                #3=(SOLID_ANGLE_UNIT() NAMED_UNIT(*) SI_UNIT($,.STERADIAN.));
                #4=UNCERTAINTY_MEASURE_WITH_UNIT(LENGTH_MEASURE(0.01),#1,'distance_accuracy_value','confusion');
                #5=(GEOMETRIC_REPRESENTATION_CONTEXT(3)
                    GLOBAL_UNIT_ASSIGNED_CONTEXT((#1,#2,#3))
                    GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT((#4))
                    REPRESENTATION_CONTEXT('ID','MODEL'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepUncertaintyMeasureWithUnit uncertainty = assertInstanceOf(StepUncertaintyMeasureWithUnit.class, resolved.get(4));
        assertEquals(0.01, uncertainty.valueComponent());

        StepGeometricRepresentationContext context = assertInstanceOf(
                StepGeometricRepresentationContext.class,
                resolved.get(5)
        );
        assertEquals(3, context.coordinateSpaceDimension());

        StepGlobalUnitAssignedContext units = context.globalUnitAssignedContext();
        assertInstanceOf(StepGlobalUnitAssignedContext.class, units);
        assertEquals(3, units.units().size());

        StepGlobalUncertaintyAssignedContext uncertainties = context.globalUncertaintyAssignedContext();
        assertInstanceOf(StepGlobalUncertaintyAssignedContext.class, uncertainties);
        assertEquals(1, uncertainties.uncertainties().size());
    }

    @Test
    void shouldResolveProductDefinitionFormationWithSpecifiedSource() {
        String step = """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION_WITH_SPECIFIED_SOURCE('v1','first release',#3,.NOT_KNOWN.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepProductDefinitionFormation formation = assertInstanceOf(StepProductDefinitionFormation.class, resolved.get(4));
        assertEquals("v1", formation.name());
        assertEquals(3, formation.ofProduct().id());
    }

    @Test
    void shouldResolveRepresentationRelationshipWithTransformation() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P',(10.0,0.0,0.0));
                #3=DIRECTION('DZ',(0.0,0.0,1.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX0',#1,#3,#4);
                #6=AXIS2_PLACEMENT_3D('AX1',#2,#3,#4);
                #7=ITEM_DEFINED_TRANSFORMATION('move','translate x',#5,#6);
                #8=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #9=CARTESIAN_POINT('PA',(0.0,0.0,0.0));
                #10=CARTESIAN_POINT('PB',(1.0,0.0,0.0));
                #11=SHAPE_REPRESENTATION('REP_A',(#9),#8);
                #12=SHAPE_REPRESENTATION('REP_B',(#10),#8);
                #13=(REPRESENTATION_RELATIONSHIP('rr','with transform',#11,#12)
                     REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION(#7));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepItemDefinedTransformation transformation = assertInstanceOf(StepItemDefinedTransformation.class, resolved.get(7));
        assertEquals(5, transformation.transformItem1().id());
        assertEquals(6, transformation.transformItem2().id());

        StepRepresentationRelationshipWithTransformation relationship = assertInstanceOf(
                StepRepresentationRelationshipWithTransformation.class,
                resolved.get(13)
        );
        assertEquals(11, relationship.rep1().id());
        assertEquals(12, relationship.rep2().id());
        assertEquals(7, relationship.transformationOperator().id());
    }

    @Test
    void shouldResolveRepresentationRelationship() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #4=REPRESENTATION('REP_A',(#1),#3);
                #5=REPRESENTATION('REP_B',(#2),#3);
                #6=REPRESENTATION_RELATIONSHIP('rr','plain relation',#4,#5);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentationRelationship relationship = assertInstanceOf(
                StepRepresentationRelationship.class,
                resolved.get(6)
        );
        assertEquals("rr", relationship.name());
        assertEquals(4, relationship.rep1().id());
        assertEquals(5, relationship.rep2().id());
    }
}
