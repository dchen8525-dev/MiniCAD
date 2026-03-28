package com.minicad.step.semantic;

import com.minicad.common.StepParseException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepConicalSurface;
import com.minicad.step.model.StepColourRgb;
import com.minicad.step.model.StepCylindricalSurface;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepBSplineCurveWithKnots;
import com.minicad.step.model.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.StepEllipse;
import com.minicad.step.model.StepFaceSurface;
import com.minicad.step.model.StepPresentationLayerAssignment;
import com.minicad.step.model.StepStyledItem;
import com.minicad.step.model.StepAnnotationTextOccurrence;
import com.minicad.step.model.StepApplicationProtocolDefinition;
import com.minicad.step.model.StepAxis2Placement2D;
import com.minicad.step.model.StepDescriptiveRepresentationItem;
import com.minicad.step.model.StepDerivedUnit;
import com.minicad.step.model.StepDraughtingCallout;
import com.minicad.step.model.StepGeometricCurveSet;
import com.minicad.step.model.StepGeometricItemSpecificUsage;
import com.minicad.step.model.StepMeasureRepresentationItem;
import com.minicad.step.model.StepGlobalUncertaintyAssignedContext;
import com.minicad.step.model.StepGlobalUnitAssignedContext;
import com.minicad.step.model.StepItemDefinedTransformation;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepMeasureWithUnit;
import com.minicad.step.model.StepOrientedFace;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepPcurve;
import com.minicad.step.model.StepProduct;
import com.minicad.step.model.StepProductDefinition;
import com.minicad.step.model.StepProductDefinitionShape;
import com.minicad.step.model.StepProductRelatedProductCategory;
import com.minicad.step.model.StepPropertyDefinition;
import com.minicad.step.model.StepPropertyDefinitionRepresentation;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.model.StepGeometricRepresentationContext;
import com.minicad.step.model.StepRepresentationRelationshipWithTransformation;
import com.minicad.step.model.StepSeamCurve;
import com.minicad.step.model.StepShapeRepresentationRelationship;
import com.minicad.step.model.StepShapeDefinitionRepresentation;
import com.minicad.step.model.StepSiUnit;
import com.minicad.step.model.StepSurfaceCurve;
import com.minicad.step.model.StepToroidalSurface;
import com.minicad.step.model.StepTrimmedCurve;
import com.minicad.step.model.StepNextAssemblyUsageOccurrence;
import com.minicad.step.model.StepContextDependentShapeRepresentation;
import com.minicad.step.model.StepCircle;
import com.minicad.step.model.StepUncertaintyMeasureWithUnit;
import com.minicad.step.model.StepVertexLoop;
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

        assertEquals("ADVANCED_FACE geometry must be PLANE, CYLINDRICAL_SURFACE, CONICAL_SURFACE, B_SPLINE_SURFACE_WITH_KNOTS or TOROIDAL_SURFACE", exception.getMessage());
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

        StepContextDependentShapeRepresentation contextRepresentation = assertInstanceOf(
                StepContextDependentShapeRepresentation.class,
                resolved.get(17)
        );
        assertEquals(15, contextRepresentation.representationRelationship().id());
        assertEquals(16, contextRepresentation.representedProductRelation().id());
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
}
