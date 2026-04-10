package com.minicad.step.semantic;

import com.minicad.common.StepParseException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepCharacterGlyphStyleOutline;
import com.minicad.step.model.StepCharacterGlyphStyleOutlineWithCharacteristics;
import com.minicad.step.model.StepCharacterGlyphStyleStroke;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepColour;
import com.minicad.step.model.StepColourSpecification;
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
import com.minicad.step.model.StepAnnotationCurveOccurrence;
import com.minicad.step.model.StepAnnotationFillArea;
import com.minicad.step.model.StepAnnotationFillAreaOccurrence;
import com.minicad.step.model.StepAnnotationPlane;
import com.minicad.step.model.StepAnnotationPlaceholderOccurrence;
import com.minicad.step.model.StepAnnotationPointOccurrence;
import com.minicad.step.model.StepAnnotationOccurrenceRelationship;
import com.minicad.step.model.StepAnnotationSymbol;
import com.minicad.step.model.StepAnnotationSymbolOccurrence;
import com.minicad.step.model.StepTerminatorSymbol;
import com.minicad.step.model.StepAnnotationText;
import com.minicad.step.model.StepAnnotationTextCharacter;
import com.minicad.step.model.StepApplicationProtocolDefinition;
import com.minicad.step.model.StepAdvancedFace;
import com.minicad.step.model.StepAxis1Placement;
import com.minicad.step.model.StepAxis2Placement2D;
import com.minicad.step.model.StepDescriptiveRepresentationItem;
import com.minicad.step.model.StepDegeneratePcurve;
import com.minicad.step.model.StepDimensionCurve;
import com.minicad.step.model.StepDerivedUnit;
import com.minicad.step.model.StepDraughtingCallout;
import com.minicad.step.model.StepDraughtingPreDefinedColour;
import com.minicad.step.model.StepDraughtingPreDefinedCurveFont;
import com.minicad.step.model.StepDraughtingPreDefinedTextFont;
import com.minicad.step.model.StepCurveStyle;
import com.minicad.step.model.StepFillAreaStyleColour;
import com.minicad.step.model.StepGeometricCurveSet;
import com.minicad.step.model.StepGeometricSet;
import com.minicad.step.model.StepGeometricItemSpecificUsage;
import com.minicad.step.model.StepMeasureRepresentationItem;
import com.minicad.step.model.StepGlobalUncertaintyAssignedContext;
import com.minicad.step.model.StepGlobalUnitAssignedContext;
import com.minicad.step.model.StepItemDefinedTransformation;
import com.minicad.step.model.StepLeaderCurve;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepMeasureWithUnit;
import com.minicad.step.model.StepNamedUnit;
import com.minicad.step.model.StepOrientedFace;
import com.minicad.step.model.StepOrientedClosedShell;
import com.minicad.step.model.StepOrientedOpenShell;
import com.minicad.step.model.StepOrientedPath;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepPreDefinedColour;
import com.minicad.step.model.StepPreDefinedCurveFont;
import com.minicad.step.model.StepPreDefinedDimensionSymbol;
import com.minicad.step.model.StepPreDefinedGeometricalToleranceSymbol;
import com.minicad.step.model.StepPreDefinedItem;
import com.minicad.step.model.StepPreDefinedMarker;
import com.minicad.step.model.StepPreDefinedPointMarkerSymbol;
import com.minicad.step.model.StepPreDefinedSurfaceSideStyle;
import com.minicad.step.model.StepPreDefinedSymbol;
import com.minicad.step.model.StepPreDefinedTerminatorSymbol;
import com.minicad.step.model.StepPreDefinedTextFont;
import com.minicad.step.model.StepPolyLoop;
import com.minicad.step.model.StepPolyline;
import com.minicad.step.model.StepPcurve;
import com.minicad.step.model.StepPoint;
import com.minicad.step.model.StepPointSet;
import com.minicad.step.model.StepPointStyle;
import com.minicad.step.model.StepProduct;
import com.minicad.step.model.StepProjectionCurve;
import com.minicad.step.model.StepProductDefinition;
import com.minicad.step.model.StepProductDefinitionFormation;
import com.minicad.step.model.StepProductDefinitionShape;
import com.minicad.step.model.StepProductRelatedProductCategory;
import com.minicad.step.model.StepPropertyDefinition;
import com.minicad.step.model.StepPropertyDefinitionRepresentation;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.model.StepRepresentationMap;
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
import com.minicad.step.model.StepSurfaceStyleReflectanceAmbient;
import com.minicad.step.model.StepSurfaceStyleBoundary;
import com.minicad.step.model.StepSurfaceStyleControlGrid;
import com.minicad.step.model.StepSurfaceStyleParameterLine;
import com.minicad.step.model.StepSurfaceStyleSegmentationCurve;
import com.minicad.step.model.StepSurfaceStyleSilhouette;
import com.minicad.step.model.StepSurfaceSideStyle;
import com.minicad.step.model.StepSurfaceStyleFillArea;
import com.minicad.step.model.StepSurfaceStyleReflectanceAmbientDiffuse;
import com.minicad.step.model.StepSurfaceStyleReflectanceAmbientDiffuseSpecular;
import com.minicad.step.model.StepSurfaceStyleTransparent;
import com.minicad.step.model.StepSurfaceStyleUsage;
import com.minicad.step.model.StepSymbolColour;
import com.minicad.step.model.StepSymbolRepresentationMap;
import com.minicad.step.model.StepSymbolStyle;
import com.minicad.step.model.StepSurfacedOpenShell;
import com.minicad.step.model.StepTextStyle;
import com.minicad.step.model.StepTextStyleWithBoxCharacteristics;
import com.minicad.step.model.StepTextStyleForDefinedFont;
import com.minicad.step.model.StepTextStyleWithJustification;
import com.minicad.step.model.StepTextStyleWithMirror;
import com.minicad.step.model.StepTextStyleWithSpacing;
import com.minicad.step.model.StepTopologicalRepresentationItem;
import com.minicad.step.model.StepToroidalSurface;
import com.minicad.step.model.StepTrimmedCurve;
import com.minicad.step.model.StepTypedMeasureWithUnit;
import com.minicad.step.model.StepUserDefinedCurveFont;
import com.minicad.step.model.StepUserDefinedMarker;
import com.minicad.step.model.StepUserDefinedTerminatorSymbol;
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
import java.util.List;
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
    void shouldResolveSurfaceStyleTransparentWithinSurfaceSideStyle() {
        String step = """
                DATA;
                #1=COLOUR_RGB('Terracotta',0.8,0.4,0.2);
                #2=FILL_AREA_STYLE_COLOUR('',#1);
                #3=FILL_AREA_STYLE('',(#2));
                #4=SURFACE_STYLE_FILL_AREA(#3);
                #5=SURFACE_STYLE_TRANSPARENT(0.35);
                #6=SURFACE_SIDE_STYLE('',(#4,#5));
                #7=SURFACE_STYLE_USAGE(.BOTH.,#6);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepSurfaceStyleTransparent transparent =
                assertInstanceOf(StepSurfaceStyleTransparent.class, resolved.get(5));
        StepSurfaceSideStyle sideStyle = assertInstanceOf(StepSurfaceSideStyle.class, resolved.get(6));
        StepSurfaceStyleUsage usage = assertInstanceOf(StepSurfaceStyleUsage.class, resolved.get(7));
        assertEquals(0.35, transparent.transparency());
        assertEquals(2, sideStyle.styles().size());
        assertInstanceOf(StepSurfaceStyleFillArea.class, sideStyle.styles().get(0));
        assertInstanceOf(StepSurfaceStyleTransparent.class, sideStyle.styles().get(1));
        assertEquals(6, usage.style().id());
    }

    @Test
    void shouldResolveSurfaceStyleReflectanceAmbientWithinSurfaceSideStyle() {
        String step = """
                DATA;
                #1=SURFACE_STYLE_REFLECTANCE_AMBIENT(0.2);
                #2=SURFACE_STYLE_TRANSPARENT(0.35);
                #3=SURFACE_SIDE_STYLE('',(#1,#2));
                #4=SURFACE_STYLE_USAGE(.BOTH.,#3);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepSurfaceStyleReflectanceAmbient ambient =
                assertInstanceOf(StepSurfaceStyleReflectanceAmbient.class, resolved.get(1));
        StepSurfaceStyleTransparent transparent =
                assertInstanceOf(StepSurfaceStyleTransparent.class, resolved.get(2));
        StepSurfaceSideStyle sideStyle = assertInstanceOf(StepSurfaceSideStyle.class, resolved.get(3));
        StepSurfaceStyleUsage usage = assertInstanceOf(StepSurfaceStyleUsage.class, resolved.get(4));
        assertEquals(0.2, ambient.ambientReflectance());
        assertEquals(0.35, transparent.transparency());
        assertEquals(2, sideStyle.styles().size());
        assertInstanceOf(StepSurfaceStyleReflectanceAmbient.class, sideStyle.styles().get(0));
        assertInstanceOf(StepSurfaceStyleTransparent.class, sideStyle.styles().get(1));
        assertEquals(3, usage.style().id());
    }

    @Test
    void shouldResolveSurfaceStyleParameterLineWithinSurfaceSideStyle() {
        String step = """
                DATA;
                #1=PRE_DEFINED_CURVE_FONT('solid');
                #2=PRE_DEFINED_COLOUR('black');
                #3=CURVE_STYLE('C0',#1,0.25,#2);
                #4=SURFACE_STYLE_PARAMETER_LINE(#3);
                #5=SURFACE_SIDE_STYLE('',(#4));
                #6=SURFACE_STYLE_USAGE(.BOTH.,#5);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepCurveStyle curveStyle = assertInstanceOf(StepCurveStyle.class, resolved.get(3));
        StepSurfaceStyleParameterLine parameterLine =
                assertInstanceOf(StepSurfaceStyleParameterLine.class, resolved.get(4));
        StepSurfaceSideStyle sideStyle = assertInstanceOf(StepSurfaceSideStyle.class, resolved.get(5));
        StepSurfaceStyleUsage usage = assertInstanceOf(StepSurfaceStyleUsage.class, resolved.get(6));
        assertEquals(3, parameterLine.style().id());
        assertEquals(0.25, curveStyle.curveWidth());
        assertEquals(1, sideStyle.styles().size());
        assertInstanceOf(StepSurfaceStyleParameterLine.class, sideStyle.styles().get(0));
        assertEquals(5, usage.style().id());
    }

    @Test
    void shouldResolveAdditionalSurfaceCurveStylesWithinSurfaceSideStyle() {
        String step = """
                DATA;
                #1=PRE_DEFINED_CURVE_FONT('solid');
                #2=PRE_DEFINED_COLOUR('black');
                #3=CURVE_STYLE('C0',#1,0.25,#2);
                #4=SURFACE_STYLE_BOUNDARY(#3);
                #5=SURFACE_STYLE_CONTROL_GRID(#3);
                #6=SURFACE_STYLE_SEGMENTATION_CURVE(#3);
                #7=SURFACE_STYLE_SILHOUETTE(#3);
                #8=SURFACE_SIDE_STYLE('',(#4,#5,#6,#7));
                #9=SURFACE_STYLE_USAGE(.BOTH.,#8);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertInstanceOf(StepSurfaceStyleBoundary.class, resolved.get(4));
        assertInstanceOf(StepSurfaceStyleControlGrid.class, resolved.get(5));
        assertInstanceOf(StepSurfaceStyleSegmentationCurve.class, resolved.get(6));
        assertInstanceOf(StepSurfaceStyleSilhouette.class, resolved.get(7));
        StepSurfaceSideStyle sideStyle = assertInstanceOf(StepSurfaceSideStyle.class, resolved.get(8));
        StepSurfaceStyleUsage usage = assertInstanceOf(StepSurfaceStyleUsage.class, resolved.get(9));
        assertEquals(4, sideStyle.styles().size());
        assertInstanceOf(StepSurfaceStyleBoundary.class, sideStyle.styles().get(0));
        assertInstanceOf(StepSurfaceStyleControlGrid.class, sideStyle.styles().get(1));
        assertInstanceOf(StepSurfaceStyleSegmentationCurve.class, sideStyle.styles().get(2));
        assertInstanceOf(StepSurfaceStyleSilhouette.class, sideStyle.styles().get(3));
        assertEquals(8, usage.style().id());
    }

    @Test
    void shouldResolveSurfaceReflectanceVariantsWithinSurfaceSideStyle() {
        String step = """
                DATA;
                #1=SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE(0.2,0.6);
                #2=COLOUR_RGB('Specular',1.0,1.0,1.0);
                #3=SURFACE_STYLE_REFLECTANCE_AMBIENT_DIFFUSE_SPECULAR(0.2,0.6,0.4,32.0,#2);
                #4=SURFACE_SIDE_STYLE('',(#1,#3));
                #5=SURFACE_STYLE_USAGE(.BOTH.,#4);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepSurfaceStyleReflectanceAmbientDiffuse diffuse =
                assertInstanceOf(StepSurfaceStyleReflectanceAmbientDiffuse.class, resolved.get(1));
        StepSurfaceStyleReflectanceAmbientDiffuseSpecular specular =
                assertInstanceOf(StepSurfaceStyleReflectanceAmbientDiffuseSpecular.class, resolved.get(3));
        StepSurfaceSideStyle sideStyle = assertInstanceOf(StepSurfaceSideStyle.class, resolved.get(4));
        StepSurfaceStyleUsage usage = assertInstanceOf(StepSurfaceStyleUsage.class, resolved.get(5));
        assertEquals(0.2, diffuse.ambientReflectance());
        assertEquals(0.6, diffuse.diffuseReflectance());
        assertEquals(0.4, specular.specularReflectance());
        assertEquals(32.0, specular.specularExponent());
        assertEquals(2, specular.specularColour().id());
        assertEquals(2, sideStyle.styles().size());
        assertInstanceOf(StepSurfaceStyleReflectanceAmbientDiffuse.class, sideStyle.styles().get(0));
        assertInstanceOf(
                StepSurfaceStyleReflectanceAmbientDiffuseSpecular.class,
                sideStyle.styles().get(1));
        assertEquals(4, usage.style().id());
    }

    @Test
    void shouldResolvePointStyle() {
        String step = """
                DATA;
                #1=PRE_DEFINED_POINT_MARKER_SYMBOL('dot');
                #2=COLOUR_RGB('Red',1.0,0.0,0.0);
                #3=POINT_STYLE('Pts',#1,2.5,#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepPointStyle pointStyle = assertInstanceOf(StepPointStyle.class, resolved.get(3));
        assertEquals("Pts", pointStyle.name());
        assertEquals(1, pointStyle.marker().id());
        assertEquals(2.5, pointStyle.markerSize());
        assertEquals(2, pointStyle.colour().id());
    }

    @Test
    void shouldResolveTextStyleForDefinedFontAndTextStyle() {
        String step = """
                DATA;
                #1=COLOUR_RGB('Black',0.0,0.0,0.0);
                #2=TEXT_STYLE_FOR_DEFINED_FONT(#1);
                #3=TEXT_STYLE('TS0',#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepTextStyleForDefinedFont definedFont =
                assertInstanceOf(StepTextStyleForDefinedFont.class, resolved.get(2));
        StepTextStyle textStyle = assertInstanceOf(StepTextStyle.class, resolved.get(3));
        assertEquals(1, definedFont.textColour().id());
        assertEquals("TS0", textStyle.name());
        assertEquals(2, textStyle.characterAppearance().id());
    }

    @Test
    void shouldResolveTextStyleWithSpacing() {
        String step = """
                DATA;
                #1=COLOUR_RGB('Black',0.0,0.0,0.0);
                #2=TEXT_STYLE_FOR_DEFINED_FONT(#1);
                #3=TEXT_STYLE_WITH_SPACING('TS1',#2,0.15);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepTextStyleWithSpacing textStyle =
                assertInstanceOf(StepTextStyleWithSpacing.class, resolved.get(3));
        assertEquals("TS1", textStyle.name());
        assertEquals(2, textStyle.characterAppearance().id());
        assertEquals(0.15, textStyle.characterSpacing());
    }

    @Test
    void shouldResolveTextStyleWithJustification() {
        String step = """
                DATA;
                #1=COLOUR_RGB('Black',0.0,0.0,0.0);
                #2=TEXT_STYLE_FOR_DEFINED_FONT(#1);
                #3=TEXT_STYLE_WITH_JUSTIFICATION('TS2',#2,.LEFT.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepTextStyleWithJustification textStyle =
                assertInstanceOf(StepTextStyleWithJustification.class, resolved.get(3));
        assertEquals("TS2", textStyle.name());
        assertEquals(2, textStyle.characterAppearance().id());
        assertEquals("LEFT", textStyle.justification());
    }

    @Test
    void shouldResolveTextStyleWithMirror() {
        String step = """
                DATA;
                #1=COLOUR_RGB('Black',0.0,0.0,0.0);
                #2=TEXT_STYLE_FOR_DEFINED_FONT(#1);
                #3=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #4=DIRECTION('X',(1.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_2D('M',#3,#4);
                #6=TEXT_STYLE_WITH_MIRROR('TS3',#2,#5);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepTextStyleWithMirror textStyle =
                assertInstanceOf(StepTextStyleWithMirror.class, resolved.get(6));
        assertEquals("TS3", textStyle.name());
        assertEquals(2, textStyle.characterAppearance().id());
        assertEquals(5, textStyle.mirrorPlacement().id());
    }

    @Test
    void shouldResolveTextStyleWithBoxCharacteristics() {
        String step = """
                DATA;
                #1=COLOUR_RGB('Black',0.0,0.0,0.0);
                #2=TEXT_STYLE_FOR_DEFINED_FONT(#1);
                #3=TEXT_STYLE_WITH_BOX_CHARACTERISTICS('TS4',#2,(BOX_HEIGHT(1.2),BOX_WIDTH(0.8)));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepTextStyleWithBoxCharacteristics textStyle =
                assertInstanceOf(StepTextStyleWithBoxCharacteristics.class, resolved.get(3));
        assertEquals("TS4", textStyle.name());
        assertEquals(2, textStyle.characterAppearance().id());
        assertEquals(List.of("BOX_HEIGHT(1.2)", "BOX_WIDTH(0.8)"), textStyle.boxCharacteristics());
    }

    @Test
    void shouldResolveCharacterGlyphStyles() {
        String step = """
                DATA;
                #1=DRAUGHTING_PRE_DEFINED_CURVE_FONT('continuous');
                #2=COLOUR_RGB('Black',0.0,0.0,0.0);
                #3=CURVE_STYLE('GlyphStroke',#1,0.2,#2);
                #4=FILL_AREA_STYLE_COLOUR('',#2);
                #5=FILL_AREA_STYLE('',(#4));
                #6=CHARACTER_GLYPH_STYLE_STROKE(#3);
                #7=CHARACTER_GLYPH_STYLE_OUTLINE(#3);
                #8=CHARACTER_GLYPH_STYLE_OUTLINE_WITH_CHARACTERISTICS(#3,#5);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepCharacterGlyphStyleStroke stroke =
                assertInstanceOf(StepCharacterGlyphStyleStroke.class, resolved.get(6));
        StepCharacterGlyphStyleOutline outline =
                assertInstanceOf(StepCharacterGlyphStyleOutline.class, resolved.get(7));
        StepCharacterGlyphStyleOutlineWithCharacteristics outlineWithCharacteristics =
                assertInstanceOf(StepCharacterGlyphStyleOutlineWithCharacteristics.class, resolved.get(8));
        assertEquals(3, stroke.strokeStyle().id());
        assertEquals(3, outline.outlineStyle().id());
        assertEquals(3, outlineWithCharacteristics.outlineStyle().id());
        assertEquals(5, outlineWithCharacteristics.characteristics().id());
    }

    @Test
    void shouldResolveSymbolColourAndSymbolStyle() {
        String step = """
                DATA;
                #1=PRE_DEFINED_COLOUR('yellow');
                #2=SYMBOL_COLOUR(#1);
                #3=SYMBOL_STYLE('SS0',#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepSymbolColour symbolColour = assertInstanceOf(StepSymbolColour.class, resolved.get(2));
        StepSymbolStyle symbolStyle = assertInstanceOf(StepSymbolStyle.class, resolved.get(3));
        assertEquals(1, symbolColour.colour().id());
        assertEquals("SS0", symbolStyle.name());
        assertEquals(2, symbolStyle.styleOfSymbol().id());
    }

    @Test
    void shouldResolveColourAndColourSpecification() {
        String step = """
                DATA;
                #1=COLOUR();
                #2=COLOUR_SPECIFICATION('amber');
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertInstanceOf(StepColour.class, resolved.get(1));
        StepColourSpecification specification =
                assertInstanceOf(StepColourSpecification.class, resolved.get(2));
        assertEquals("amber", specification.name());
    }

    @Test
    void shouldPreferColourRgbOverColourAndColourSpecification() {
        String step = """
                DATA;
                #1=(COLOUR_RGB('Amber',1.0,0.75,0.0)
                    COLOUR_SPECIFICATION('Amber')
                    COLOUR());
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertSame(StepColourRgb.class, resolved.get(1).getClass());
    }

    @Test
    void shouldResolveCurveStyleWithColourSpecification() {
        String step = """
                DATA;
                #1=PRE_DEFINED_CURVE_FONT('solid');
                #2=COLOUR_SPECIFICATION('amber');
                #3=CURVE_STYLE('C0',#1,0.25,#2);
                #4=FILL_AREA_STYLE_COLOUR('',#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepCurveStyle style = assertInstanceOf(StepCurveStyle.class, resolved.get(3));
        StepFillAreaStyleColour fill = assertInstanceOf(StepFillAreaStyleColour.class, resolved.get(4));
        assertEquals(2, style.colour().id());
        assertEquals(2, fill.colour().id());
    }

    @Test
    void shouldResolvePreDefinedColourAndCurveFont() {
        String step = """
                DATA;
                #1=PRE_DEFINED_COLOUR('black');
                #2=PRE_DEFINED_CURVE_FONT('solid');
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepPreDefinedColour colour = assertInstanceOf(StepPreDefinedColour.class, resolved.get(1));
        StepPreDefinedCurveFont font = assertInstanceOf(StepPreDefinedCurveFont.class, resolved.get(2));
        assertEquals("black", colour.name());
        assertEquals("solid", font.name());
    }

    @Test
    void shouldPreferPreDefinedColourOverColourSpecificationAndColour() {
        String step = """
                DATA;
                #1=(PRE_DEFINED_COLOUR('black')
                    COLOUR_SPECIFICATION('black')
                    COLOUR());
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertSame(StepPreDefinedColour.class, resolved.get(1).getClass());
    }

    @Test
    void shouldPreferDraughtingPreDefinedSubtypeOverGenericBase() {
        String step = """
                DATA;
                #1=(DRAUGHTING_PRE_DEFINED_COLOUR('black') PRE_DEFINED_COLOUR('black'));
                #2=(DRAUGHTING_PRE_DEFINED_CURVE_FONT('solid') PRE_DEFINED_CURVE_FONT('solid'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertSame(StepDraughtingPreDefinedColour.class, resolved.get(1).getClass());
        assertSame(StepDraughtingPreDefinedCurveFont.class, resolved.get(2).getClass());
    }

    @Test
    void shouldResolveCurveStyleWithGenericPreDefinedEntities() {
        String step = """
                DATA;
                #1=PRE_DEFINED_CURVE_FONT('solid');
                #2=PRE_DEFINED_COLOUR('black');
                #3=CURVE_STYLE('C0',#1,0.25,#2);
                #4=FILL_AREA_STYLE_COLOUR('',#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepCurveStyle style = assertInstanceOf(StepCurveStyle.class, resolved.get(3));
        StepFillAreaStyleColour fill = assertInstanceOf(StepFillAreaStyleColour.class, resolved.get(4));
        assertEquals("C0", style.name());
        assertEquals(1, style.curveFont().id());
        assertEquals(2, style.colour().id());
        assertEquals(2, fill.colour().id());
    }

    @Test
    void shouldResolvePreDefinedTextFont() {
        String step = """
                DATA;
                #1=PRE_DEFINED_TEXT_FONT('iso_3098');
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepPreDefinedTextFont font = assertInstanceOf(StepPreDefinedTextFont.class, resolved.get(1));
        assertEquals("iso_3098", font.name());
    }

    @Test
    void shouldPreferDraughtingPreDefinedTextFontSubtypeOverGenericBase() {
        String step = """
                DATA;
                #1=(DRAUGHTING_PRE_DEFINED_TEXT_FONT('iso_3098') PRE_DEFINED_TEXT_FONT('iso_3098'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertSame(StepDraughtingPreDefinedTextFont.class, resolved.get(1).getClass());
    }

    @Test
    void shouldResolvePreDefinedSymbolAndMarkerFamily() {
        String step = """
                DATA;
                #1=PRE_DEFINED_MARKER('dot');
                #2=PRE_DEFINED_SYMBOL('diameter');
                #3=PRE_DEFINED_SURFACE_SIDE_STYLE('both');
                #4=PRE_DEFINED_DIMENSION_SYMBOL('diameter');
                #5=PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL('position');
                #6=PRE_DEFINED_TERMINATOR_SYMBOL('filled_arrow');
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertEquals("dot", assertInstanceOf(StepPreDefinedMarker.class, resolved.get(1)).name());
        assertEquals("diameter", assertInstanceOf(StepPreDefinedSymbol.class, resolved.get(2)).name());
        assertEquals("both", assertInstanceOf(StepPreDefinedSurfaceSideStyle.class, resolved.get(3)).name());
        assertEquals("diameter", assertInstanceOf(StepPreDefinedDimensionSymbol.class, resolved.get(4)).name());
        assertEquals("position", assertInstanceOf(StepPreDefinedGeometricalToleranceSymbol.class, resolved.get(5)).name());
        assertEquals("filled_arrow", assertInstanceOf(StepPreDefinedTerminatorSymbol.class, resolved.get(6)).name());
    }

    @Test
    void shouldPreferPreDefinedPointMarkerSymbolOverBaseMarkerAndSymbol() {
        String step = """
                DATA;
                #1=(PRE_DEFINED_POINT_MARKER_SYMBOL('dot')
                    PRE_DEFINED_MARKER('dot')
                    PRE_DEFINED_SYMBOL('dot'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepPreDefinedPointMarkerSymbol symbol =
                assertInstanceOf(StepPreDefinedPointMarkerSymbol.class, resolved.get(1));
        assertEquals("dot", symbol.name());
    }

    @Test
    void shouldResolvePreDefinedItem() {
        String step = """
                DATA;
                #1=PRE_DEFINED_ITEM('generic-item');
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepPreDefinedItem item = assertInstanceOf(StepPreDefinedItem.class, resolved.get(1));
        assertEquals("generic-item", item.name());
    }

    @Test
    void shouldPreferSpecificPreDefinedSubtypeOverPreDefinedItem() {
        String step = """
                DATA;
                #1=(PRE_DEFINED_POINT_MARKER_SYMBOL('dot')
                    PRE_DEFINED_MARKER('dot')
                    PRE_DEFINED_SYMBOL('dot')
                    PRE_DEFINED_ITEM('dot'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertSame(StepPreDefinedPointMarkerSymbol.class, resolved.get(1).getClass());
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
    void shouldResolveRepresentationMapAndAnnotationTextFamily() {
        String step = """
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','TEXT'));
                #2=REPRESENTATION('TXT',(),#1);
                #3=CARTESIAN_POINT('O',(0.0,0.0));
                #4=DIRECTION('X',(1.0,0.0));
                #5=AXIS2_PLACEMENT_2D('MAP',#3,#4);
                #6=REPRESENTATION_MAP(#5,#2);
                #7=CARTESIAN_POINT('P',(10.0,20.0));
                #8=DIRECTION('DX',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('TGT',#7,#8);
                #10=ANNOTATION_TEXT('AT0',#6,#9);
                #11=ANNOTATION_TEXT_CHARACTER('ATC0',#6,#9);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentationMap representationMap =
                assertInstanceOf(StepRepresentationMap.class, resolved.get(6));
        StepAnnotationText annotationText =
                assertInstanceOf(StepAnnotationText.class, resolved.get(10));
        StepAnnotationTextCharacter annotationTextCharacter =
                assertInstanceOf(StepAnnotationTextCharacter.class, resolved.get(11));
        assertEquals(5, representationMap.mappedOrigin().id());
        assertEquals(2, representationMap.mappedRepresentation().id());
        assertEquals("AT0", annotationText.name());
        assertEquals(6, annotationText.mappingSource().id());
        assertEquals(9, annotationText.mappingTarget().id());
        assertEquals("ATC0", annotationTextCharacter.name());
        assertEquals(6, annotationTextCharacter.mappingSource().id());
        assertEquals(9, annotationTextCharacter.mappingTarget().id());
    }

    @Test
    void shouldResolveSymbolRepresentationMapAndAnnotationSymbol() {
        String step = """
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #2=REPRESENTATION('SYM',(),#1);
                #3=CARTESIAN_POINT('O',(0.0,0.0));
                #4=DIRECTION('X',(1.0,0.0));
                #5=AXIS2_PLACEMENT_2D('MAP',#3,#4);
                #6=SYMBOL_REPRESENTATION_MAP(#5,#2);
                #7=CARTESIAN_POINT('P',(30.0,40.0));
                #8=DIRECTION('DX',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('TGT',#7,#8);
                #10=ANNOTATION_SYMBOL('AS0',#6,#9);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepSymbolRepresentationMap symbolRepresentationMap =
                assertInstanceOf(StepSymbolRepresentationMap.class, resolved.get(6));
        StepAnnotationSymbol annotationSymbol =
                assertInstanceOf(StepAnnotationSymbol.class, resolved.get(10));
        assertEquals(5, symbolRepresentationMap.mappedOrigin().id());
        assertEquals(2, symbolRepresentationMap.mappedRepresentation().id());
        assertEquals("AS0", annotationSymbol.name());
        assertEquals(6, annotationSymbol.mappingSource().id());
        assertEquals(9, annotationSymbol.mappingTarget().id());
    }

    @Test
    void shouldResolveAnnotationSymbolOccurrence() {
        String step = """
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
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
                #12=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#11),#10);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationSymbolOccurrence occurrence =
                assertInstanceOf(StepAnnotationSymbolOccurrence.class, resolved.get(12));
        assertEquals("ASO0", occurrence.name());
        assertEquals(1, occurrence.styles().size());
        assertEquals(10, occurrence.item().id());
    }

    @Test
    void shouldResolveTerminatorSymbol() {
        String step = """
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
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
                #12=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#11),#10);
                #13=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #14=DIRECTION('DX3',(1.0,0.0,0.0));
                #15=VECTOR('V0',#14,1.0);
                #16=LINE('L0',#13,#15);
                #17=PRESENTATION_STYLE_ASSIGNMENT(());
                #18=(LEADER_CURVE('LC0',(#17),#16)
                    ANNOTATION_CURVE_OCCURRENCE('LC0',(#17),#16)
                    STYLED_ITEM('LC0',(#17),#16)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('LC0'));
                #19=TERMINATOR_SYMBOL('TS0',(#11),#10,#18);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepTerminatorSymbol terminatorSymbol =
                assertInstanceOf(StepTerminatorSymbol.class, resolved.get(19));
        assertEquals("TS0", terminatorSymbol.name());
        assertEquals(1, terminatorSymbol.styles().size());
        assertEquals(10, terminatorSymbol.item().id());
        assertEquals(18, terminatorSymbol.annotatedCurve().id());
    }

    @Test
    void shouldResolveAnnotationOccurrenceRelationship() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=PRESENTATION_STYLE_ASSIGNMENT(());
                #3=(ANNOTATION_POINT_OCCURRENCE('PO0',(#2),#1)
                    STYLED_ITEM('PO0',(#2),#1)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PO0'));
                #4=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #5=REPRESENTATION('SYM',(),#4);
                #6=CARTESIAN_POINT('O',(0.0,0.0));
                #7=DIRECTION('X',(1.0,0.0));
                #8=AXIS2_PLACEMENT_2D('MAP',#6,#7);
                #9=SYMBOL_REPRESENTATION_MAP(#8,#5);
                #10=CARTESIAN_POINT('P',(30.0,40.0));
                #11=DIRECTION('DX',(1.0,0.0));
                #12=AXIS2_PLACEMENT_2D('TGT',#10,#11);
                #13=ANNOTATION_SYMBOL('AS0',#9,#12);
                #14=PRESENTATION_STYLE_ASSIGNMENT(());
                #15=ANNOTATION_SYMBOL_OCCURRENCE('ASO0',(#14),#13);
                #16=ANNOTATION_OCCURRENCE_RELATIONSHIP('rel','links point to symbol',#3,#15);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationOccurrenceRelationship relationship =
                assertInstanceOf(StepAnnotationOccurrenceRelationship.class, resolved.get(16));
        assertEquals("rel", relationship.name());
        assertEquals("links point to symbol", relationship.description());
        assertEquals(3, relationship.relatingAnnotationOccurrence().id());
        assertEquals(15, relationship.relatedAnnotationOccurrence().id());
    }

    @Test
    void shouldResolveUserDefinedMarkerAndCurveFont() {
        String step = """
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','MAP'));
                #2=REPRESENTATION('MAP_REP',(),#1);
                #3=CARTESIAN_POINT('O',(0.0,0.0));
                #4=DIRECTION('X',(1.0,0.0));
                #5=AXIS2_PLACEMENT_2D('MAP',#3,#4);
                #6=REPRESENTATION_MAP(#5,#2);
                #7=CARTESIAN_POINT('P',(1.0,1.0));
                #8=DIRECTION('DX',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('TGT',#7,#8);
                #10=USER_DEFINED_MARKER('UM0',#6,#9);
                #11=USER_DEFINED_CURVE_FONT('UF0',#6,#9);
                #12=COLOUR_RGB('Black',0.0,0.0,0.0);
                #13=POINT_STYLE('PS0',#10,1.5,#12);
                #14=CURVE_STYLE('CS0',#11,0.25,#12);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepUserDefinedMarker marker = assertInstanceOf(StepUserDefinedMarker.class, resolved.get(10));
        StepUserDefinedCurveFont font = assertInstanceOf(StepUserDefinedCurveFont.class, resolved.get(11));
        StepPointStyle pointStyle = assertInstanceOf(StepPointStyle.class, resolved.get(13));
        StepCurveStyle curveStyle = assertInstanceOf(StepCurveStyle.class, resolved.get(14));
        assertEquals(6, marker.mappingSource().id());
        assertEquals(9, marker.mappingTarget().id());
        assertEquals(6, font.mappingSource().id());
        assertEquals(9, font.mappingTarget().id());
        assertEquals(10, pointStyle.marker().id());
        assertEquals(11, curveStyle.curveFont().id());
    }

    @Test
    void shouldResolveUserDefinedTerminatorSymbol() {
        String step = """
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','MAP'));
                #2=REPRESENTATION('MAP_REP',(),#1);
                #3=CARTESIAN_POINT('O',(0.0,0.0));
                #4=DIRECTION('X',(1.0,0.0));
                #5=AXIS2_PLACEMENT_2D('MAP',#3,#4);
                #6=REPRESENTATION_MAP(#5,#2);
                #7=CARTESIAN_POINT('P',(5.0,6.0));
                #8=DIRECTION('DX',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('TGT',#7,#8);
                #10=USER_DEFINED_TERMINATOR_SYMBOL('UT0',#6,#9);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepUserDefinedTerminatorSymbol symbol =
                assertInstanceOf(StepUserDefinedTerminatorSymbol.class, resolved.get(10));
        assertEquals("UT0", symbol.name());
        assertEquals(6, symbol.mappingSource().id());
        assertEquals(9, symbol.mappingTarget().id());
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
    void shouldResolveAnnotationPointOccurrence() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,0.0));
                #2=PRESENTATION_STYLE_ASSIGNMENT(());
                #3=(ANNOTATION_POINT_OCCURRENCE('AP0',(#2),#1)
                    STYLED_ITEM('AP0',(#2),#1)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('AP0'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationPointOccurrence occurrence =
                assertInstanceOf(StepAnnotationPointOccurrence.class, resolved.get(3));
        assertEquals("AP0", occurrence.name());
        assertEquals(1, occurrence.styles().size());
        assertEquals(1, occurrence.item().id());
    }

    @Test
    void shouldResolveAnnotationCurveOccurrence() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('V0',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=PRESENTATION_STYLE_ASSIGNMENT(());
                #6=(ANNOTATION_CURVE_OCCURRENCE('AC0',(#5),#4)
                    STYLED_ITEM('AC0',(#5),#4)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('AC0'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationCurveOccurrence occurrence =
                assertInstanceOf(StepAnnotationCurveOccurrence.class, resolved.get(6));
        assertEquals("AC0", occurrence.name());
        assertEquals(1, occurrence.styles().size());
        assertEquals(4, occurrence.item().id());
    }

    @Test
    void shouldResolveLeaderCurve() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('V0',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=PRESENTATION_STYLE_ASSIGNMENT(());
                #6=(LEADER_CURVE('LC0',(#5),#4)
                    ANNOTATION_CURVE_OCCURRENCE('LC0',(#5),#4)
                    STYLED_ITEM('LC0',(#5),#4)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('LC0'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepLeaderCurve leaderCurve = assertInstanceOf(StepLeaderCurve.class, resolved.get(6));
        assertEquals("LC0", leaderCurve.name());
        assertEquals(1, leaderCurve.styles().size());
        assertEquals(4, leaderCurve.item().id());
    }

    @Test
    void shouldResolveAnnotationFillArea() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #5=POLYLINE('B0',(#1,#2,#3,#4,#1));
                #6=(ANNOTATION_FILL_AREA('FA0',(#5))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationFillArea fillArea = assertInstanceOf(StepAnnotationFillArea.class, resolved.get(6));
        assertEquals("FA0", fillArea.name());
        assertEquals(1, fillArea.boundaries().size());
        assertEquals(5, fillArea.boundaries().get(0).id());
    }

    @Test
    void shouldResolveAnnotationFillAreaOccurrence() {
        String step = """
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
                #8=(ANNOTATION_FILL_AREA_OCCURRENCE('FAO0',(#7),#6,#1)
                    STYLED_ITEM('FAO0',(#7),#6)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FAO0'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationFillAreaOccurrence occurrence =
                assertInstanceOf(StepAnnotationFillAreaOccurrence.class, resolved.get(8));
        assertEquals("FAO0", occurrence.name());
        assertEquals(1, occurrence.styles().size());
        assertEquals(6, occurrence.item().id());
        assertEquals(1, occurrence.fillStyleTarget().id());
    }

    @Test
    void shouldResolveAnnotationPlaceholderOccurrence() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=GEOMETRIC_SET('GS0',(#1));
                #3=PRESENTATION_STYLE_ASSIGNMENT(());
                #4=(ANNOTATION_PLACEHOLDER_OCCURRENCE('PH0',(#3),#2,.ANNOTATION_TEXT.,2.5)
                    STYLED_ITEM('PH0',(#3),#2)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PH0'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationPlaceholderOccurrence occurrence =
                assertInstanceOf(StepAnnotationPlaceholderOccurrence.class, resolved.get(4));
        assertEquals("PH0", occurrence.name());
        assertEquals(1, occurrence.styles().size());
        assertEquals(2, occurrence.item().id());
        assertEquals("ANNOTATION_TEXT", occurrence.role());
        assertEquals(2.5, occurrence.lineSpacing());
    }

    @Test
    void shouldResolveAnnotationPlane() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('N',(0.0,0.0,1.0));
                #3=DIRECTION('X',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('AP',#4);
                #6=CARTESIAN_POINT('P0',(1.0,2.0,0.0));
                #7=PRESENTATION_STYLE_ASSIGNMENT(());
                #8=(ANNOTATION_PLANE((#6))
                    STYLED_ITEM('AP',(#7),#5)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('AP'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationPlane annotationPlane =
                assertInstanceOf(StepAnnotationPlane.class, resolved.get(8));
        assertEquals("AP", annotationPlane.name());
        assertEquals(1, annotationPlane.styles().size());
        assertEquals(5, annotationPlane.item().id());
        assertEquals(1, annotationPlane.elements().size());
        assertEquals(6, annotationPlane.elements().get(0).id());
    }

    @Test
    void shouldResolveProjectionCurve() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('V0',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=PRESENTATION_STYLE_ASSIGNMENT(());
                #6=(PROJECTION_CURVE('PC0',(#5),#4)
                    ANNOTATION_CURVE_OCCURRENCE('PC0',(#5),#4)
                    STYLED_ITEM('PC0',(#5),#4)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('PC0'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepProjectionCurve projectionCurve =
                assertInstanceOf(StepProjectionCurve.class, resolved.get(6));
        assertEquals("PC0", projectionCurve.name());
        assertEquals(1, projectionCurve.styles().size());
        assertEquals(4, projectionCurve.item().id());
    }

    @Test
    void shouldResolveDimensionCurve() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=VECTOR('V0',#2,1.0);
                #4=LINE('L0',#1,#3);
                #5=PRESENTATION_STYLE_ASSIGNMENT(());
                #6=(DIMENSION_CURVE('DC0',(#5),#4)
                    ANNOTATION_CURVE_OCCURRENCE('DC0',(#5),#4)
                    STYLED_ITEM('DC0',(#5),#4)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('DC0'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepDimensionCurve dimensionCurve =
                assertInstanceOf(StepDimensionCurve.class, resolved.get(6));
        assertEquals("DC0", dimensionCurve.name());
        assertEquals(1, dimensionCurve.styles().size());
        assertEquals(4, dimensionCurve.item().id());
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
    void shouldResolveAdditionalShapeRepresentationSubtypes() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=COMPOUND_SHAPE_REPRESENTATION('CSR',(#1),#2);
                #4=PLANAR_SHAPE_REPRESENTATION('PLSR',(#1),#2);
                #5=POINT_PLACEMENT_SHAPE_REPRESENTATION('PPSR',(#1),#2);
                #6=SHAPE_DIMENSION_REPRESENTATION('SDR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation compound = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation planar = assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation pointPlacement = assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation shapeDimension = assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("CSR", compound.name());
        assertEquals("PLSR", planar.name());
        assertEquals("PPSR", pointPlacement.name());
        assertEquals("SDR", shapeDimension.name());
        assertEquals(true, compound.shapeRepresentation());
        assertEquals(true, planar.shapeRepresentation());
        assertEquals(true, pointPlacement.shapeRepresentation());
        assertEquals(true, shapeDimension.shapeRepresentation());
    }

    @Test
    void shouldResolveMoreShapeRepresentationSubtypes() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=LOCATION_SHAPE_REPRESENTATION('LSR',(#1),#2);
                #4=REPRESENTATIVE_SHAPE_REPRESENTATION('RSR',(#1),#2);
                #5=NEUTRAL_SKETCH_REPRESENTATION('NSR',(#1),#2);
                #6=PROCEDURAL_SHAPE_REPRESENTATION('PSR2',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation location = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation representative = assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation neutralSketch = assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation procedural = assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("LSR", location.name());
        assertEquals("RSR", representative.name());
        assertEquals("NSR", neutralSketch.name());
        assertEquals("PSR2", procedural.name());
        assertEquals(true, location.shapeRepresentation());
        assertEquals(true, representative.shapeRepresentation());
        assertEquals(true, neutralSketch.shapeRepresentation());
        assertEquals(true, procedural.shapeRepresentation());
    }

    @Test
    void shouldResolveEvenMoreShapeRepresentationSubtypes() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=BLOCK_SHAPE_REPRESENTATION('BSR',(#1),#2);
                #4=CYLINDRICAL_SHAPE_REPRESENTATION('CSR2',(#1),#2);
                #5=DIRECTION_SHAPE_REPRESENTATION('DSR',(#1),#2);
                #6=TESSELLATED_SHAPE_REPRESENTATION('TSR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation block = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation cylindrical = assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation direction = assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation tessellated = assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("BSR", block.name());
        assertEquals("CSR2", cylindrical.name());
        assertEquals("DSR", direction.name());
        assertEquals("TSR", tessellated.name());
        assertEquals(true, block.shapeRepresentation());
        assertEquals(true, cylindrical.shapeRepresentation());
        assertEquals(true, direction.shapeRepresentation());
        assertEquals(true, tessellated.shapeRepresentation());
    }

    @Test
    void shouldResolveCsg2dAndNgonShapeRepresentationSubtypes() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=CSG_2D_SHAPE_REPRESENTATION('C2D',(#1),#2);
                #4=SINGLE_AREA_CSG_2D_SHAPE_REPRESENTATION('SA2D',(#1),#2);
                #5=SINGLE_BOUNDARY_CSG_2D_SHAPE_REPRESENTATION('SB2D',(#1),#2);
                #6=NGON_SHAPE_REPRESENTATION('NGON',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation csg2d = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation singleArea = assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation singleBoundary = assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation ngon = assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("C2D", csg2d.name());
        assertEquals("SA2D", singleArea.name());
        assertEquals("SB2D", singleBoundary.name());
        assertEquals("NGON", ngon.name());
        assertEquals(true, csg2d.shapeRepresentation());
        assertEquals(true, singleArea.shapeRepresentation());
        assertEquals(true, singleBoundary.shapeRepresentation());
        assertEquals(true, ngon.shapeRepresentation());
    }

    @Test
    void shouldResolveAdvancedShapeRepresentationSubtypes() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=CURVE_SWEPT_SOLID_SHAPE_REPRESENTATION('CSSR',(#1),#2);
                #4=MANIFOLD_SUBSURFACE_SHAPE_REPRESENTATION('MSSR',(#1),#2);
                #5=SCAN_DATA_SHAPE_REPRESENTATION('SDSR',(#1),#2);
                #6=TESSELLATED_SHAPE_REPRESENTATION_WITH_ACCURACY_PARAMETERS('TSAP',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation curveSwept = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation manifoldSubsurface =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation scanData = assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation tessellatedAcc =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("CSSR", curveSwept.name());
        assertEquals("MSSR", manifoldSubsurface.name());
        assertEquals("SDSR", scanData.name());
        assertEquals("TSAP", tessellatedAcc.name());
        assertEquals(true, curveSwept.shapeRepresentation());
        assertEquals(true, manifoldSubsurface.shapeRepresentation());
        assertEquals(true, scanData.shapeRepresentation());
        assertEquals(true, tessellatedAcc.shapeRepresentation());
    }

    @Test
    void shouldResolveSheetAndParameterizedShapeRepresentationSubtypes() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #3=BEVELED_SHEET_REPRESENTATION('BSHEET',(#1),#2);
                #4=COMPOSITE_SHEET_REPRESENTATION('CSHEET',(#1),#2);
                #5=SHAPE_REPRESENTATION_WITH_PARAMETERS('SRWP',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation beveled = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation composite = assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation parameterized = assertInstanceOf(StepRepresentation.class, resolved.get(5));
        assertEquals("BSHEET", beveled.name());
        assertEquals("CSHEET", composite.name());
        assertEquals("SRWP", parameterized.name());
        assertEquals(true, beveled.shapeRepresentation());
        assertEquals(true, composite.shapeRepresentation());
        assertEquals(true, parameterized.shapeRepresentation());
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
    void shouldResolvePresentationRepresentation() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PRESENTATION'));
                #3=PRESENTATION_REPRESENTATION('PR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        assertEquals("PR", representation.name());
        assertEquals(false, representation.shapeRepresentation());
    }

    @Test
    void shouldResolveDraughtingModel() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','DRAWING'));
                #3=DRAUGHTING_MODEL('DM',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(3));
        assertEquals("DM", representation.name());
        assertEquals(false, representation.shapeRepresentation());
    }

    @Test
    void shouldResolveAdditionalRepresentationSubtypes() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PRESENTATION'));
                #3=DRAUGHTING_SUBFIGURE_REPRESENTATION('DSF',(#1),#2);
                #4=DRAUGHTING_SYMBOL_REPRESENTATION('DSR',(#1),#2);
                #5=MECHANICAL_DESIGN_SHADED_PRESENTATION_REPRESENTATION('MSPR',(#1),#2);
                #6=VISUAL_APPEARANCE_REPRESENTATION('VAR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation draughtingSubfigure =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation draughtingSymbol =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation shaded =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation visual =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("DSF", draughtingSubfigure.name());
        assertEquals("DSR", draughtingSymbol.name());
        assertEquals("MSPR", shaded.name());
        assertEquals("VAR", visual.name());
        assertEquals(false, draughtingSubfigure.shapeRepresentation());
        assertEquals(false, draughtingSymbol.shapeRepresentation());
        assertEquals(false, shaded.shapeRepresentation());
        assertEquals(false, visual.shapeRepresentation());
    }

    @Test
    void shouldResolveMorePresentationRepresentationSubtypes() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PRESENTATION'));
                #3=MECHANICAL_DESIGN_PRESENTATION_REPRESENTATION_WITH_DRAUGHTING('MDPRD',(#1),#2);
                #4=PRESENTATION_AREA('PA',(#1),#2);
                #5=PRESENTATION_VIEW('PV',(#1),#2);
                #6=PICTURE_REPRESENTATION('PIC',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation mdpr =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation area =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation view =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation picture =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("MDPRD", mdpr.name());
        assertEquals("PA", area.name());
        assertEquals("PV", view.name());
        assertEquals("PIC", picture.name());
        assertEquals(false, mdpr.shapeRepresentation());
        assertEquals(false, area.shapeRepresentation());
        assertEquals(false, view.shapeRepresentation());
        assertEquals(false, picture.shapeRepresentation());
    }

    @Test
    void shouldResolveTextAndSymbolRepresentationSubtypes() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PRESENTATION'));
                #3=SYMBOL_REPRESENTATION('SR',(#1),#2);
                #4=TEXT_STRING_REPRESENTATION('TSR',(#1),#2);
                #5=STRUCTURED_TEXT_REPRESENTATION('STR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation symbol =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation textString =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation structuredText =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        assertEquals("SR", symbol.name());
        assertEquals("TSR", textString.name());
        assertEquals("STR", structuredText.name());
        assertEquals(false, symbol.shapeRepresentation());
        assertEquals(false, textString.shapeRepresentation());
        assertEquals(false, structuredText.shapeRepresentation());
    }

    @Test
    void shouldResolveDrawingAndPathRepresentationSubtypes() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PRESENTATION'));
                #3=DRAWING_SHEET_LAYOUT('DSL',(#1),#2);
                #4=DRAWING_SHEET_REVISION('DSR2',(#1),#2);
                #5=PATH_PARAMETER_REPRESENTATION('PPR',(#1),#2);
                #6=PRESCRIBED_PATH('PPATH',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation sheetLayout =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation sheetRevision =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation pathParameter =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation prescribedPath =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("DSL", sheetLayout.name());
        assertEquals("DSR2", sheetRevision.name());
        assertEquals("PPR", pathParameter.name());
        assertEquals("PPATH", prescribedPath.name());
        assertEquals(false, sheetLayout.shapeRepresentation());
        assertEquals(false, sheetRevision.shapeRepresentation());
        assertEquals(false, pathParameter.shapeRepresentation());
        assertEquals(false, prescribedPath.shapeRepresentation());
    }

    @Test
    void shouldResolveResultingPathAndCharacterGlyphRepresentationSubtypes() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PRESENTATION'));
                #3=RESULTING_PATH('RPATH',(#1),#2);
                #4=CHARACTER_GLYPH_SYMBOL('CGS',(#1),#2);
                #5=CHARACTER_GLYPH_SYMBOL_OUTLINE('CGSO',(#1),#2);
                #6=CHARACTER_GLYPH_SYMBOL_STROKE('CGSS',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation resultingPath =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation glyphSymbol =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation glyphOutline =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation glyphStroke =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("RPATH", resultingPath.name());
        assertEquals("CGS", glyphSymbol.name());
        assertEquals("CGSO", glyphOutline.name());
        assertEquals("CGSS", glyphStroke.name());
        assertEquals(false, resultingPath.shapeRepresentation());
        assertEquals(false, glyphSymbol.shapeRepresentation());
        assertEquals(false, glyphOutline.shapeRepresentation());
        assertEquals(false, glyphStroke.shapeRepresentation());
    }

    @Test
    void shouldResolvePresentationAreaAndGenericGlyphRepresentationSubtypes() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PRESENTATION'));
                #3=MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_AREA('MDGPA',(#1),#2);
                #4=MECHANICAL_DESIGN_SHADED_PRESENTATION_AREA('MDSPA',(#1),#2);
                #5=GENERIC_CHARACTER_GLYPH_SYMBOL('GCGS',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation geometricArea =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation shadedArea =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation genericGlyph =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        assertEquals("MDGPA", geometricArea.name());
        assertEquals("MDSPA", shadedArea.name());
        assertEquals("GCGS", genericGlyph.name());
        assertEquals(false, geometricArea.shapeRepresentation());
        assertEquals(false, shadedArea.shapeRepresentation());
        assertEquals(false, genericGlyph.shapeRepresentation());
    }

    @Test
    void shouldResolveMorePresentationRepresentationFamilies() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PRESENTATION'));
                #3=MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_REPRESENTATION('MDGPR',(#1),#2);
                #4=AREA_DEPENDENT_ANNOTATION_REPRESENTATION('ADAR',(#1),#2);
                #5=SURFACE_TEXTURE_REPRESENTATION('STRX',(#1),#2);
                #6=TACTILE_APPEARANCE_REPRESENTATION('TAR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation geometricPresentation =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation areaDependent =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation surfaceTexture =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation tactileAppearance =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("MDGPR", geometricPresentation.name());
        assertEquals("ADAR", areaDependent.name());
        assertEquals("STRX", surfaceTexture.name());
        assertEquals("TAR", tactileAppearance.name());
        assertEquals(false, geometricPresentation.shapeRepresentation());
        assertEquals(false, areaDependent.shapeRepresentation());
        assertEquals(false, surfaceTexture.shapeRepresentation());
        assertEquals(false, tactileAppearance.shapeRepresentation());
    }

    @Test
    void shouldResolveProceduralAndVariationalRepresentationFamilies() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PRESENTATION'));
                #3=PROCEDURAL_REPRESENTATION('PROC',(#1),#2);
                #4=CONSTRUCTIVE_GEOMETRY_REPRESENTATION('CGR',(#1),#2);
                #5=PRESENTATION_SIZE('PSIZE',(#1),#2);
                #6=VARIATIONAL_REPRESENTATION('VREP',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation procedural =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation constructiveGeometry =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation presentationSize =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation variational =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("PROC", procedural.name());
        assertEquals("CGR", constructiveGeometry.name());
        assertEquals("PSIZE", presentationSize.name());
        assertEquals("VREP", variational.name());
        assertEquals(false, procedural.shapeRepresentation());
        assertEquals(false, constructiveGeometry.shapeRepresentation());
        assertEquals(false, presentationSize.shapeRepresentation());
        assertEquals(false, variational.shapeRepresentation());
    }

    @Test
    void shouldResolveCharacteristicAndUncertaintyRepresentationFamilies() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PRESENTATION'));
                #3=RANGE_CHARACTERISTIC('RCHAR',(#1),#2);
                #4=PLY_ANGLE_REPRESENTATION('PLY',(#1),#2);
                #5=MOMENTS_OF_INERTIA_REPRESENTATION('MOI',(#1),#2);
                #6=UNCERTAINTY_ASSIGNED_REPRESENTATION('UAR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation rangeCharacteristic =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation plyAngle =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation momentsOfInertia =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation uncertaintyAssigned =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("RCHAR", rangeCharacteristic.name());
        assertEquals("PLY", plyAngle.name());
        assertEquals("MOI", momentsOfInertia.name());
        assertEquals("UAR", uncertaintyAssigned.name());
        assertEquals(false, rangeCharacteristic.shapeRepresentation());
        assertEquals(false, plyAngle.shapeRepresentation());
        assertEquals(false, momentsOfInertia.shapeRepresentation());
        assertEquals(false, uncertaintyAssigned.shapeRepresentation());
    }

    @Test
    void shouldResolveInterpolatedAndKinematicRepresentationFamilies() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PRESENTATION'));
                #3=INTERPOLATED_CONFIGURATION_REPRESENTATION('ICR',(#1),#2);
                #4=KINEMATIC_FRAME_BACKGROUND_REPRESENTATION('KFBR',(#1),#2);
                #5=KINEMATIC_GROUND_REPRESENTATION('KGR',(#1),#2);
                #6=KINEMATIC_LINK_REPRESENTATION('KLR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation interpolated =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation frameBackground =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation ground =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation link =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("ICR", interpolated.name());
        assertEquals("KFBR", frameBackground.name());
        assertEquals("KGR", ground.name());
        assertEquals("KLR", link.name());
        assertEquals(false, interpolated.shapeRepresentation());
        assertEquals(false, frameBackground.shapeRepresentation());
        assertEquals(false, ground.shapeRepresentation());
        assertEquals(false, link.shapeRepresentation());
    }

    @Test
    void shouldResolveKinematicTopologyRepresentationFamilies() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PRESENTATION'));
                #3=KINEMATIC_TOPOLOGY_DIRECTED_STRUCTURE('KTDS',(#1),#2);
                #4=KINEMATIC_TOPOLOGY_NETWORK_STRUCTURE('KTNS',(#1),#2);
                #5=KINEMATIC_TOPOLOGY_STRUCTURE('KTS',(#1),#2);
                #6=KINEMATIC_TOPOLOGY_SUBSTRUCTURE('KTSS',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation directed =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation network =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation structure =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation substructure =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("KTDS", directed.name());
        assertEquals("KTNS", network.name());
        assertEquals("KTS", structure.name());
        assertEquals("KTSS", substructure.name());
        assertEquals(false, directed.shapeRepresentation());
        assertEquals(false, network.shapeRepresentation());
        assertEquals(false, structure.shapeRepresentation());
        assertEquals(false, substructure.shapeRepresentation());
    }

    @Test
    void shouldResolveMechanismAndLinkRepresentationFamilies() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PRESENTATION'));
                #3=KINEMATIC_TOPOLOGY_TREE_STRUCTURE('KTTS',(#1),#2);
                #4=LINEAR_FLEXIBLE_LINK_REPRESENTATION('LFLR',(#1),#2);
                #5=RIGID_LINK_REPRESENTATION('RLR',(#1),#2);
                #6=MECHANISM_REPRESENTATION('MR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation tree =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation flexibleLink =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation rigidLink =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation mechanism =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("KTTS", tree.name());
        assertEquals("LFLR", flexibleLink.name());
        assertEquals("RLR", rigidLink.name());
        assertEquals("MR", mechanism.name());
        assertEquals(false, tree.shapeRepresentation());
        assertEquals(false, flexibleLink.shapeRepresentation());
        assertEquals(false, rigidLink.shapeRepresentation());
        assertEquals(false, mechanism.shapeRepresentation());
    }

    @Test
    void shouldResolveAdditionalMotionAndOrientationRepresentationFamilies() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PRESENTATION'));
                #3=MECHANISM_STATE_REPRESENTATION('MSR',(#1),#2);
                #4=LINK_MOTION_REPRESENTATION_ALONG_PATH('LMRAP',(#1),#2);
                #5=REINFORCEMENT_ORIENTATION_BASIS('ROB',(#1),#2);
                #6=CONNECTED_EDGE_WITH_LENGTH_SET_REPRESENTATION('CEWLSR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation mechanismState =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation linkMotion =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation reinforcementBasis =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation connectedEdgeLengthSet =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("MSR", mechanismState.name());
        assertEquals("LMRAP", linkMotion.name());
        assertEquals("ROB", reinforcementBasis.name());
        assertEquals("CEWLSR", connectedEdgeLengthSet.name());
        assertEquals(false, mechanismState.shapeRepresentation());
        assertEquals(false, linkMotion.shapeRepresentation());
        assertEquals(false, reinforcementBasis.shapeRepresentation());
        assertEquals(false, connectedEdgeLengthSet.shapeRepresentation());
    }

    @Test
    void shouldResolveDataQualityRepresentationFamilies() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PRESENTATION'));
                #3=DATA_EQUIVALENCE_CRITERIA_REPRESENTATION('DECR',(#1),#2);
                #4=DATA_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION('DEIR',(#1),#2);
                #5=DATA_QUALITY_CRITERIA_REPRESENTATION('DQCR',(#1),#2);
                #6=DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION('DQIR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation equivalenceCriteria =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation equivalenceResult =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation qualityCriteria =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation qualityResult =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("DECR", equivalenceCriteria.name());
        assertEquals("DEIR", equivalenceResult.name());
        assertEquals("DQCR", qualityCriteria.name());
        assertEquals("DQIR", qualityResult.name());
        assertEquals(false, equivalenceCriteria.shapeRepresentation());
        assertEquals(false, equivalenceResult.shapeRepresentation());
        assertEquals(false, qualityCriteria.shapeRepresentation());
        assertEquals(false, qualityResult.shapeRepresentation());
    }

    @Test
    void shouldResolveExternallyConditionedAndA3mRepresentationFamilies() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PRESENTATION'));
                #3=EXTERNALLY_CONDITIONED_DATA_QUALITY_CRITERIA_REPRESENTATION('ECDQCR',(#1),#2);
                #4=EXTERNALLY_CONDITIONED_DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION('ECDQIR',(#1),#2);
                #5=A3M_EQUIVALENCE_CRITERIA_REPRESENTATION('A3MECR',(#1),#2);
                #6=A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION('A3MEIR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation externallyConditionedCriteria =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation externallyConditionedResult =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation a3mCriteria =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation a3mResult =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("ECDQCR", externallyConditionedCriteria.name());
        assertEquals("ECDQIR", externallyConditionedResult.name());
        assertEquals("A3MECR", a3mCriteria.name());
        assertEquals("A3MEIR", a3mResult.name());
        assertEquals(false, externallyConditionedCriteria.shapeRepresentation());
        assertEquals(false, externallyConditionedResult.shapeRepresentation());
        assertEquals(false, a3mCriteria.shapeRepresentation());
        assertEquals(false, a3mResult.shapeRepresentation());
    }

    @Test
    void shouldResolveA3mAssemblyAndShapeDataQualityRepresentationFamilies() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','QUALITY');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','QUALITY'));
                #3=A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_ASSEMBLY('A3MIRA',(#1),#2);
                #4=A3M_EQUIVALENCE_INSPECTION_RESULT_REPRESENTATION_FOR_SHAPE('A3MIRS',(#1),#2);
                #5=SHAPE_DATA_QUALITY_CRITERIA_REPRESENTATION('SDQCR',(#1),#2);
                #6=SHAPE_DATA_QUALITY_INSPECTION_RESULT_REPRESENTATION('SDQIR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation a3mAssemblyResult =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation a3mShapeResult =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation shapeQualityCriteria =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation shapeQualityResult =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("A3MIRA", a3mAssemblyResult.name());
        assertEquals("A3MIRS", a3mShapeResult.name());
        assertEquals("SDQCR", shapeQualityCriteria.name());
        assertEquals("SDQIR", shapeQualityResult.name());
        assertEquals(false, a3mAssemblyResult.shapeRepresentation());
        assertEquals(false, a3mShapeResult.shapeRepresentation());
        assertEquals(false, shapeQualityCriteria.shapeRepresentation());
        assertEquals(false, shapeQualityResult.shapeRepresentation());
    }

    @Test
    void shouldResolveExternallyDefinedAndAccuracyRepresentationFamilies() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','EXT');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','EXTREP'));
                #3=EXTERNALLY_DEFINED_REPRESENTATION('EDR',(#1),#2);
                #4=EXTERNALLY_DEFINED_REPRESENTATION_WITH_PARAMETERS('EDRP',(#1),#2);
                #5=SHAPE_CRITERIA_REPRESENTATION_WITH_ACCURACY('SCRA',(#1),#2);
                #6=SHAPE_INSPECTION_RESULT_REPRESENTATION_WITH_ACCURACY('SIRA',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation externallyDefined =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation externallyDefinedWithParameters =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation shapeCriteriaWithAccuracy =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation shapeInspectionWithAccuracy =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("EDR", externallyDefined.name());
        assertEquals("EDRP", externallyDefinedWithParameters.name());
        assertEquals("SCRA", shapeCriteriaWithAccuracy.name());
        assertEquals("SIRA", shapeInspectionWithAccuracy.name());
        assertEquals(false, externallyDefined.shapeRepresentation());
        assertEquals(false, externallyDefinedWithParameters.shapeRepresentation());
        assertEquals(false, shapeCriteriaWithAccuracy.shapeRepresentation());
        assertEquals(false, shapeInspectionWithAccuracy.shapeRepresentation());
    }

    @Test
    void shouldResolveAdditionalGeneralRepresentationFamilies() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','GEN');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','GENERAL'));
                #3=ANALYSIS_MODEL('AM',(#1),#2);
                #4=LANGUAGE_ASSIGNMENT('LANG',(#1),#2);
                #5=MESSAGE_CONTENTS_ASSIGNMENT('MSG',(#1),#2);
                #6=MACHINING_TOOL_DIRECTION_REPRESENTATION('MTDR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation analysisModel =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation languageAssignment =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation messageContents =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation machiningToolDirection =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("AM", analysisModel.name());
        assertEquals("LANG", languageAssignment.name());
        assertEquals("MSG", messageContents.name());
        assertEquals("MTDR", machiningToolDirection.name());
        assertEquals(false, analysisModel.shapeRepresentation());
        assertEquals(false, languageAssignment.shapeRepresentation());
        assertEquals(false, messageContents.shapeRepresentation());
        assertEquals(false, machiningToolDirection.shapeRepresentation());
    }

    @Test
    void shouldResolveFoundedPathAndSimplifiedHoleRepresentationFamilies() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','HOLE');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','HOLEDEF'));
                #3=FOUNDED_KINEMATIC_PATH('FKP',(#1),#2);
                #4=SIMPLIFIED_COUNTERBORE_HOLE_DEFINITION('SCBH',(#1),#2);
                #5=SIMPLIFIED_COUNTERDRILL_HOLE_DEFINITION('SCDH',(#1),#2);
                #6=SIMPLIFIED_COUNTERSINK_HOLE_DEFINITION('SCSH',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation foundedPath =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation counterbore =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation counterdrill =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation countersink =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("FKP", foundedPath.name());
        assertEquals("SCBH", counterbore.name());
        assertEquals("SCDH", counterdrill.name());
        assertEquals("SCSH", countersink.name());
        assertEquals(false, foundedPath.shapeRepresentation());
        assertEquals(false, counterbore.shapeRepresentation());
        assertEquals(false, counterdrill.shapeRepresentation());
        assertEquals(false, countersink.shapeRepresentation());
    }

    @Test
    void shouldResolveMachiningRepresentationFamilies() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','MACH');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MACHINING'));
                #3=MACHINING_CUTTING_CORNER_REPRESENTATION('MCCR',(#1),#2);
                #4=MACHINING_DWELL_TIME_REPRESENTATION('MDTR',(#1),#2);
                #5=MACHINING_FEED_SPEED_REPRESENTATION('MFSR',(#1),#2);
                #6=MACHINING_OFFSET_VECTOR_REPRESENTATION('MOVR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation cuttingCorner =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation dwellTime =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation feedSpeed =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation offsetVector =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("MCCR", cuttingCorner.name());
        assertEquals("MDTR", dwellTime.name());
        assertEquals("MFSR", feedSpeed.name());
        assertEquals("MOVR", offsetVector.name());
        assertEquals(false, cuttingCorner.shapeRepresentation());
        assertEquals(false, dwellTime.shapeRepresentation());
        assertEquals(false, feedSpeed.shapeRepresentation());
        assertEquals(false, offsetVector.shapeRepresentation());
    }

    @Test
    void shouldResolveAdditionalMachiningRepresentationFamilies() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','TOOL');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','TOOLING'));
                #3=MACHINING_SPINDLE_SPEED_REPRESENTATION('MSSR',(#1),#2);
                #4=MACHINING_TOOL_BODY_REPRESENTATION('MTBR',(#1),#2);
                #5=MACHINING_TOOL_DIMENSION_REPRESENTATION('MTDR2',(#1),#2);
                #6=MACHINING_TOOLPATH_SPEED_PROFILE_REPRESENTATION('MTSPR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation spindleSpeed =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation toolBody =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation toolDimension =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation toolpathSpeedProfile =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("MSSR", spindleSpeed.name());
        assertEquals("MTBR", toolBody.name());
        assertEquals("MTDR2", toolDimension.name());
        assertEquals("MTSPR", toolpathSpeedProfile.name());
        assertEquals(false, spindleSpeed.shapeRepresentation());
        assertEquals(false, toolBody.shapeRepresentation());
        assertEquals(false, toolDimension.shapeRepresentation());
        assertEquals(false, toolpathSpeedProfile.shapeRepresentation());
    }

    @Test
    void shouldResolveToleranceAndTableRepresentationFamilies() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','TABLE');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','TOL'));
                #3=FREEFORM_MILLING_TOLERANCE_REPRESENTATION('FMTR',(#1),#2);
                #4=HARDNESS_REPRESENTATION('HR',(#1),#2);
                #5=DEFAULT_TOLERANCE_TABLE('DTT',(#1),#2);
                #6=OTHER_LIST_TABLE_REPRESENTATION('OLTR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation freeformTolerance =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation hardness =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation defaultTolerance =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation otherListTable =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("FMTR", freeformTolerance.name());
        assertEquals("HR", hardness.name());
        assertEquals("DTT", defaultTolerance.name());
        assertEquals("OLTR", otherListTable.name());
        assertEquals(false, freeformTolerance.shapeRepresentation());
        assertEquals(false, hardness.shapeRepresentation());
        assertEquals(false, defaultTolerance.shapeRepresentation());
        assertEquals(false, otherListTable.shapeRepresentation());
    }

    @Test
    void shouldResolveCharacterizedAndEvaluatedRepresentationFamilies() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','CHAR');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CHARREP'));
                #3=CHARACTERIZED_REPRESENTATION('CR',(#1),#2);
                #4=CHARACTERIZED_ITEM_WITHIN_REPRESENTATION('CIWR',(#1),#2);
                #5=CHARACTERIZED_CHAIN_BASED_ITEM_WITHIN_REPRESENTATION('CCBIWR',(#1),#2);
                #6=EVALUATED_CHARACTERISTIC_OF_PRODUCT_AS_INDIVIDUAL_TEST_RESULT('ECPITR',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation characterized =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation characterizedItem =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation characterizedChainBasedItem =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation evaluatedCharacteristic =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        assertEquals("CR", characterized.name());
        assertEquals("CIWR", characterizedItem.name());
        assertEquals("CCBIWR", characterizedChainBasedItem.name());
        assertEquals("ECPITR", evaluatedCharacteristic.name());
        assertEquals(false, characterized.shapeRepresentation());
        assertEquals(false, characterizedItem.shapeRepresentation());
        assertEquals(false, characterizedChainBasedItem.shapeRepresentation());
        assertEquals(false, evaluatedCharacteristic.shapeRepresentation());
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
