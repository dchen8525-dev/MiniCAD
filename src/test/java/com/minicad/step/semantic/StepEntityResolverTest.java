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
import com.minicad.step.model.StepConicCurve;
import com.minicad.step.model.StepColourRgb;
import com.minicad.step.model.StepConnectedEdgeSet;
import com.minicad.step.model.StepConnectedFaceSet;
import com.minicad.step.model.StepConnectedFaceSubSet;
import com.minicad.step.model.StepCompositeCurve;
import com.minicad.step.model.StepCompositeCurveOnSurface;
import com.minicad.step.model.StepCompositeCurveSegment;
import com.minicad.step.model.StepCylindricalSurface;
import com.minicad.step.model.StepBoxDomain;
import com.minicad.step.model.StepCartesianTransformationOperator;
import com.minicad.step.model.StepCsgPrimitive;
import com.minicad.step.model.StepCsgSolid;
import com.minicad.step.model.StepConversionBasedUnit;
import com.minicad.step.model.StepConversionBasedUnitWithOffset;
import com.minicad.step.model.StepContextDependentUnit;
import com.minicad.step.model.StepContactRatioRepresentation;
import com.minicad.step.model.StepCoordinatedUniversalTimeOffset;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEdgeLoop;
import com.minicad.step.model.StepEdgeBasedWireframeModel;
import com.minicad.step.model.StepEffectivity;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepEffectivityRelationship;
import com.minicad.step.model.StepExternalSource;
import com.minicad.step.model.StepExternalIdentificationAssignment;
import com.minicad.step.model.StepExternalSourceRelationship;
import com.minicad.step.model.StepExternallyDefinedItem;
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
import com.minicad.step.model.StepChainBasedGeometricItemSpecificUsage;
import com.minicad.step.model.StepChainBasedItemIdentifiedRepresentationUsage;
import com.minicad.step.model.StepCalendarDate;
import com.minicad.step.model.StepCharacterizedObject;
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
import com.minicad.step.model.StepAnnotationSubfigureOccurrence;
import com.minicad.step.model.StepAnnotationSymbol;
import com.minicad.step.model.StepAnnotationSymbolOccurrence;
import com.minicad.step.model.StepTerminatorSymbol;
import com.minicad.step.model.StepAnnotationText;
import com.minicad.step.model.StepAnnotationTextCharacter;
import com.minicad.step.model.StepAbstractVariable;
import com.minicad.step.model.StepActionPropertyRepresentation;
import com.minicad.step.model.StepAddress;
import com.minicad.step.model.StepApplicationProtocolDefinition;
import com.minicad.step.model.StepAppliedApprovalAssignment;
import com.minicad.step.model.StepAppliedClassificationAssignment;
import com.minicad.step.model.StepAppliedCertificationAssignment;
import com.minicad.step.model.StepAppliedContractAssignment;
import com.minicad.step.model.StepAppliedDateAssignment;
import com.minicad.step.model.StepAppliedDateTimeAssignment;
import com.minicad.step.model.StepAppliedDocumentReference;
import com.minicad.step.model.StepAppliedExternalIdentificationAssignment;
import com.minicad.step.model.StepAppliedIdentificationAssignment;
import com.minicad.step.model.StepAppliedLanguageAssignment;
import com.minicad.step.model.StepAppliedNameAssignment;
import com.minicad.step.model.StepAppliedOrganizationAssignment;
import com.minicad.step.model.StepAppliedGroupAssignment;
import com.minicad.step.model.StepAppliedPersonAndOrganizationAssignment;
import com.minicad.step.model.StepAppliedSecurityClassificationAssignment;
import com.minicad.step.model.StepApproval;
import com.minicad.step.model.StepApprovalAssignment;
import com.minicad.step.model.StepApprovalDateTime;
import com.minicad.step.model.StepApprovalPersonOrganization;
import com.minicad.step.model.StepApprovalRole;
import com.minicad.step.model.StepApprovalStatus;
import com.minicad.step.model.StepAdvancedFace;
import com.minicad.step.model.StepAttributeAssertion;
import com.minicad.step.model.StepAxis1Placement;
import com.minicad.step.model.StepAxis2Placement2D;
import com.minicad.step.model.StepBackChainingRuleBody;
import com.minicad.step.model.StepCertification;
import com.minicad.step.model.StepCertificationAssignment;
import com.minicad.step.model.StepCertificationType;
import com.minicad.step.model.StepClassificationAssignment;
import com.minicad.step.model.StepClassificationRole;
import com.minicad.step.model.StepDescriptiveRepresentationItem;
import com.minicad.step.model.StepDescriptionAttribute;
import com.minicad.step.model.StepDegenerateToroidalSurface;
import com.minicad.step.model.StepDegeneratePcurve;
import com.minicad.step.model.StepDatum;
import com.minicad.step.model.StepDatumFeature;
import com.minicad.step.model.StepDatumTarget;
import com.minicad.step.model.StepDimensionalLocation;
import com.minicad.step.model.StepDimensionalSize;
import com.minicad.step.model.StepDimensionCurve;
import com.minicad.step.model.StepDimensionalExponents;
import com.minicad.step.model.StepDateAndTime;
import com.minicad.step.model.StepDateAssignment;
import com.minicad.step.model.StepDateRole;
import com.minicad.step.model.StepDateTimeAssignment;
import com.minicad.step.model.StepDateTimeRole;
import com.minicad.step.model.StepDocument;
import com.minicad.step.model.StepDocumentReference;
import com.minicad.step.model.StepDocumentRelationship;
import com.minicad.step.model.StepDocumentType;
import com.minicad.step.model.StepDocumentUsageConstraint;
import com.minicad.step.model.StepDerivedUnit;
import com.minicad.step.model.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.StepDraughtingCallout;
import com.minicad.step.model.StepDraughtingCalloutRelationship;
import com.minicad.step.model.StepDraughtingModelItemAssociation;
import com.minicad.step.model.StepDraughtingModelItemAssociationWithPlaceholder;
import com.minicad.step.model.StepDraughtingPreDefinedColour;
import com.minicad.step.model.StepDraughtingPreDefinedCurveFont;
import com.minicad.step.model.StepDraughtingPreDefinedTextFont;
import com.minicad.step.model.StepCurveStyle;
import com.minicad.step.model.StepContract;
import com.minicad.step.model.StepContractAssignment;
import com.minicad.step.model.StepContractType;
import com.minicad.step.model.StepFillAreaStyleColour;
import com.minicad.step.model.StepForwardChainingRulePremise;
import com.minicad.step.model.StepGeometricCurveSet;
import com.minicad.step.model.StepGeometricSet;
import com.minicad.step.model.StepGeometricItemSpecificUsage;
import com.minicad.step.model.StepGeneralProperty;
import com.minicad.step.model.StepGeneralPropertyRelationship;
import com.minicad.step.model.StepGroup;
import com.minicad.step.model.StepGroupAssignment;
import com.minicad.step.model.StepGroupRelationship;
import com.minicad.step.model.StepHalfSpaceSolid;
import com.minicad.step.model.StepIdAttribute;
import com.minicad.step.model.StepIdentificationAssignment;
import com.minicad.step.model.StepIdentificationRole;
import com.minicad.step.model.StepMeasureRepresentationItem;
import com.minicad.step.model.StepMechanicalDesignRequirementItemAssociation;
import com.minicad.step.model.StepGlobalUncertaintyAssignedContext;
import com.minicad.step.model.StepGlobalUnitAssignedContext;
import com.minicad.step.model.StepItemDefinedTransformation;
import com.minicad.step.model.StepItemIdentifiedRepresentationUsage;
import com.minicad.step.model.StepKinematicPropertyDefinitionRepresentation;
import com.minicad.step.model.StepKinematicPropertyMechanismRepresentation;
import com.minicad.step.model.StepKinematicPropertyRepresentationRelation;
import com.minicad.step.model.StepKinematicPropertyTopologyRepresentation;
import com.minicad.step.model.StepLanguage;
import com.minicad.step.model.StepLanguageAssignment;
import com.minicad.step.model.StepLeaderCurve;
import com.minicad.step.model.StepLocalTime;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepMappedItem;
import com.minicad.step.model.StepMeasureWithUnit;
import com.minicad.step.model.StepNamedUnit;
import com.minicad.step.model.StepNameAssignment;
import com.minicad.step.model.StepNameAttribute;
import com.minicad.step.model.StepOrganization;
import com.minicad.step.model.StepOrganizationAssignment;
import com.minicad.step.model.StepOrganizationRelationship;
import com.minicad.step.model.StepOrganizationRole;
import com.minicad.step.model.StepOrientedFace;
import com.minicad.step.model.StepOrientedEdge;
import com.minicad.step.model.StepOrientedClosedShell;
import com.minicad.step.model.StepOrientedOpenShell;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.model.StepOrientedPath;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepPlacedDatumTargetFeature;
import com.minicad.step.model.StepPlacedTarget;
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
import com.minicad.step.model.StepPmiRequirementItemAssociation;
import com.minicad.step.model.StepPolyLoop;
import com.minicad.step.model.StepPolyline;
import com.minicad.step.model.StepPcurve;
import com.minicad.step.model.StepPerson;
import com.minicad.step.model.StepPersonAndOrganization;
import com.minicad.step.model.StepPersonAndOrganizationAssignment;
import com.minicad.step.model.StepPersonAndOrganizationRole;
import com.minicad.step.model.StepPoint;
import com.minicad.step.model.StepPointSet;
import com.minicad.step.model.StepPointStyle;
import com.minicad.step.model.StepProfileDef;
import com.minicad.step.model.StepOffsetCurve2D;
import com.minicad.step.model.StepOrientedCurve;
import com.minicad.step.model.StepOrientedSurface;
import com.minicad.step.model.StepProduct;
import com.minicad.step.model.StepProductCategory;
import com.minicad.step.model.StepProductCategoryRelationship;
import com.minicad.step.model.StepProjectionCurve;
import com.minicad.step.model.StepCurveBoundedSurface;
import com.minicad.step.model.StepProductDefinition;
import com.minicad.step.model.StepProductContext;
import com.minicad.step.model.StepProductDefinitionContext;
import com.minicad.step.model.StepProductDefinitionEffectivity;
import com.minicad.step.model.StepProductDefinitionFormation;
import com.minicad.step.model.StepProductDefinitionFormationRelationship;
import com.minicad.step.model.StepProductDefinitionRelationship;
import com.minicad.step.model.StepProductDefinitionRelationshipRelationship;
import com.minicad.step.model.StepProductDefinitionShape;
import com.minicad.step.model.StepProductRelatedProductCategory;
import com.minicad.step.model.StepProductRelationship;
import com.minicad.step.model.StepPropertyDefinition;
import com.minicad.step.model.StepPropertyDefinitionRelationship;
import com.minicad.step.model.StepPropertyDefinitionRepresentation;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.model.StepRepresentationMap;
import com.minicad.step.model.StepRepresentationItem;
import com.minicad.step.model.StepRepresentationRelationship;
import com.minicad.step.model.StepResourcePropertyRepresentation;
import com.minicad.step.model.StepRowVariable;
import com.minicad.step.model.StepCurve;
import com.minicad.step.model.StepGeometricReplica;
import com.minicad.step.model.StepGeometricRepresentationContext;
import com.minicad.step.model.StepGeometricRepresentationItem;
import com.minicad.step.model.StepRepresentationRelationshipWithTransformation;
import com.minicad.step.model.StepRectangularTrimmedSurface;
import com.minicad.step.model.StepScalarVariable;
import com.minicad.step.model.StepSeamCurve;
import com.minicad.step.model.StepSecurityClassification;
import com.minicad.step.model.StepSecurityClassificationAssignment;
import com.minicad.step.model.StepSecurityClassificationLevel;
import com.minicad.step.model.StepShapeAspect;
import com.minicad.step.model.StepShapeAspectOccurrence;
import com.minicad.step.model.StepShapeAspectRelationship;
import com.minicad.step.model.StepShapeRepresentationRelationship;
import com.minicad.step.model.StepShapeDefinitionRepresentation;
import com.minicad.step.model.StepShellBasedSurfaceModel;
import com.minicad.step.model.StepSiUnit;
import com.minicad.step.model.StepSolidModel;
import com.minicad.step.model.StepSolidReplica;
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
import com.minicad.step.model.StepSweptAreaSolid;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
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
                "ADVANCED_FACE geometry must reference a supported surface but got StepCircle",
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
    void shouldResolveNestedOrientedFace() {
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
                #14=ORIENTED_FACE('OF1',#13,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepOrientedFace outer = assertInstanceOf(StepOrientedFace.class, resolved.get(14));
        assertEquals(13, outer.faceElement().id());
        assertEquals(true, outer.orientation());
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
    void shouldResolveNestedOrientedShells() {
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
                #11=ORIENTED_OPEN_SHELL('OOS0',#9,.F.);
                #12=ORIENTED_OPEN_SHELL('OOS1',#11,.T.);
                #13=ORIENTED_CLOSED_SHELL('OCS0',#10,.F.);
                #14=ORIENTED_CLOSED_SHELL('OCS1',#13,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepOrientedOpenShell openShell = assertInstanceOf(StepOrientedOpenShell.class, resolved.get(12));
        StepOrientedClosedShell closedShell = assertInstanceOf(StepOrientedClosedShell.class, resolved.get(14));
        assertEquals(11, openShell.openShellElement().id());
        assertEquals(13, closedShell.closedShellElement().id());
        assertEquals(1, openShell.faces().size());
        assertEquals(1, closedShell.faces().size());
    }

    @Test
    void shouldResolveOrientedEdgeWithExplicitVertices() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=VERTEX_POINT('V0',#1);
                #4=VERTEX_POINT('V1',#2);
                #5=DIRECTION('DX',(1.0,0.0,0.0));
                #6=VECTOR('VX',#5,1.0);
                #7=LINE('L0',#1,#6);
                #8=EDGE_CURVE('E0',#3,#4,#7,.T.);
                #9=ORIENTED_EDGE('OE0',#4,#3,#8,.F.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepOrientedEdge orientedEdge = assertInstanceOf(StepOrientedEdge.class, resolved.get(9));
        assertEquals(8, orientedEdge.edgeElement().id());
        assertEquals(false, orientedEdge.orientation());
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
    void shouldResolveWireShellWithMixedLoopFamilies() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=VECTOR('VX',#4,1.0);
                #6=LINE('L0',#1,#5);
                #7=VERTEX_POINT('V0',#1);
                #8=VERTEX_POINT('V1',#2);
                #9=EDGE_CURVE('E0',#7,#8,#6,.T.);
                #10=ORIENTED_EDGE('OE0',$,$,#9,.T.);
                #11=EDGE_LOOP('EL0',(#10));
                #12=POLY_LOOP('PL0',(#1,#2,#3));
                #13=WIRE_SHELL('WS0',(#11,#12));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepWireShell shell = assertInstanceOf(StepWireShell.class, resolved.get(13));
        assertEquals("WS0", shell.name());
        assertEquals(2, shell.loops().size());
        assertSame(StepEdgeLoop.class, shell.loops().get(0).getClass());
        assertSame(StepPolyLoop.class, shell.loops().get(1).getClass());
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
    void shouldResolveNestedOrientedPath() {
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
                #16=ORIENTED_PATH('OP0',#15,.F.);
                #17=ORIENTED_PATH('OP1',#16,.F.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepOrientedPath path = assertInstanceOf(StepOrientedPath.class, resolved.get(17));
        assertEquals("OP1", path.name());
        assertEquals(16, path.pathElement().id());
        assertEquals(false, path.orientation());
        assertEquals(2, path.edges().size());
        assertEquals(13, path.edges().getFirst().id());
        assertEquals(14, path.edges().get(1).id());
    }

    @Test
    void shouldResolveOpenPathUsingOrientedEdgeEndpoints() {
        String step = """
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
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepOpenPath path = assertInstanceOf(StepOpenPath.class, resolved.get(15));
        assertEquals("OP0", path.name());
        assertEquals(2, path.edges().size());
        assertEquals(13, path.edges().getFirst().id());
        assertEquals(14, path.edges().get(1).id());
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
    void shouldResolveFaceBasedSurfaceModelWithShellMember() {
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
                #9=OPEN_SHELL('OS0',(#8));
                #10=(FACE_BASED_SURFACE_MODEL('FBSM',(#9)) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('fbsm-item'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepFaceBasedSurfaceModel model = assertInstanceOf(StepFaceBasedSurfaceModel.class, resolved.get(10));
        assertEquals("FBSM", model.name());
        assertEquals(1, model.faceSets().size());
        assertEquals(9, model.faceSets().getFirst().id());
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
    void shouldResolveParabolaHyperbolaAndConicReferences() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PARABOLA('P0',#4,2.0);
                #6=HYPERBOLA('H0',#4,4.0,2.0);
                #7=CARTESIAN_POINT('S',(0.0,0.0,0.0));
                #8=CARTESIAN_POINT('E',(1.0,0.0,0.0));
                #9=VERTEX_POINT('VS',#7);
                #10=VERTEX_POINT('VE',#8);
                #11=EDGE_CURVE('EDGE-P',#9,#10,#5,.T.);
                #12=TRIMMED_CURVE('TRIM-H',#6,(#7),(#8),.T.,.CARTESIAN.);
                #13=SURFACE_CURVE('SURF-P',#5,(),.T.);
                #14=COMPOSITE_CURVE_SEGMENT(.CONTINUOUS.,.T.,#6);
                #15=DEGENERATE_CONIC('DC0',#4);
                #16=GEOMETRIC_CURVE_SET('GCS',(#5,#6,#15));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepConicCurve parabola = assertInstanceOf(StepConicCurve.class, resolved.get(5));
        StepConicCurve hyperbola = assertInstanceOf(StepConicCurve.class, resolved.get(6));
        StepEdgeCurve edgeCurve = assertInstanceOf(StepEdgeCurve.class, resolved.get(11));
        StepTrimmedCurve trimmedCurve = assertInstanceOf(StepTrimmedCurve.class, resolved.get(12));
        StepSurfaceCurve surfaceCurve = assertInstanceOf(StepSurfaceCurve.class, resolved.get(13));
        StepCompositeCurveSegment segment =
                assertInstanceOf(StepCompositeCurveSegment.class, resolved.get(14));
        StepConicCurve degenerateConic = assertInstanceOf(StepConicCurve.class, resolved.get(15));
        StepGeometricCurveSet set = assertInstanceOf(StepGeometricCurveSet.class, resolved.get(16));
        assertEquals("PARABOLA", parabola.entityName());
        assertEquals(List.of(2.0), parabola.parameters());
        assertEquals("HYPERBOLA", hyperbola.entityName());
        assertEquals(List.of(4.0, 2.0), hyperbola.parameters());
        assertEquals(5, edgeCurve.edgeGeometry().id());
        assertEquals(6, trimmedCurve.basisCurve().id());
        assertEquals(5, surfaceCurve.curve3d().id());
        assertEquals(6, segment.parentCurve().id());
        assertEquals("DEGENERATE_CONIC", degenerateConic.entityName());
        assertEquals(List.of(), degenerateConic.parameters());
        assertEquals(3, set.elements().size());
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
    void shouldAllowRationalBsplineCurveAsReferencedCurveGeometry() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(0.0,1.0,0.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=DIRECTION('DX',(1.0,0.0,0.0));
                #7=AXIS2_PLACEMENT_3D('AX',#1,#5,#6);
                #8=PLANE('PL',#7);
                #9=VECTOR('VEC',#5,1.0);
                #10=VERTEX_POINT('V0',#1);
                #11=VERTEX_POINT('V1',#4);
                #20=(BOUNDED_CURVE()
                     B_SPLINE_CURVE(3,(#1,#2,#3,#4),.UNSPECIFIED.,.F.,.F.)
                     B_SPLINE_CURVE_WITH_KNOTS((4,4),(0.0,1.0),.UNSPECIFIED.)
                     CURVE()
                     GEOMETRIC_REPRESENTATION_ITEM()
                     RATIONAL_B_SPLINE_CURVE((1.0,0.5,0.5,1.0))
                     REPRESENTATION_ITEM('RBK'));
                #21=TRIMMED_CURVE('TC',#20,(#1),(#4),.T.,.CARTESIAN.);
                #22=SURFACE_CURVE('SC',#20,(),.T.);
                #23=EDGE_CURVE('EC',#10,#11,#20,.T.);
                #24=SURFACE_OF_LINEAR_EXTRUSION('EX',#20,#9);
                #25=AXIS1_PLACEMENT('A1',#1,#5);
                #26=SURFACE_OF_REVOLUTION('REV',#20,#25);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRationalBSplineCurve rational =
                assertInstanceOf(StepRationalBSplineCurve.class, resolved.get(20));
        StepTrimmedCurve trimmedCurve = assertInstanceOf(StepTrimmedCurve.class, resolved.get(21));
        StepSurfaceCurve surfaceCurve = assertInstanceOf(StepSurfaceCurve.class, resolved.get(22));
        StepEdgeCurve edgeCurve = assertInstanceOf(StepEdgeCurve.class, resolved.get(23));
        StepSurfaceOfLinearExtrusion extrusion =
                assertInstanceOf(StepSurfaceOfLinearExtrusion.class, resolved.get(24));
        StepSurfaceOfRevolution revolution =
                assertInstanceOf(StepSurfaceOfRevolution.class, resolved.get(26));

        assertEquals(4, rational.weightsData().size());
        assertEquals(20, trimmedCurve.basisCurve().id());
        assertEquals(20, surfaceCurve.curve3d().id());
        assertEquals(20, edgeCurve.edgeGeometry().id());
        assertEquals(20, extrusion.sweptCurve().id());
        assertEquals(20, revolution.sweptCurve().id());
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
    void shouldResolveImplicitBsplineDataForCurveSubtypeMarkers() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(2.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(3.0,1.0,0.0));
                #10=(BEZIER_CURVE() B_SPLINE_CURVE(3,(#1,#2,#3,#4),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('bz'));
                #11=(UNIFORM_CURVE() B_SPLINE_CURVE(2,(#1,#2,#3,#4),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('uc'));
                #12=(QUASI_UNIFORM_CURVE() B_SPLINE_CURVE(2,(#1,#2,#3,#4),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('quc'));
                #13=(PIECEWISE_BEZIER_CURVE() BEZIER_CURVE() B_SPLINE_CURVE(1,(#1,#2,#3,#4),.UNSPECIFIED.,.F.,.F.) BOUNDED_CURVE() CURVE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('pbc'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepBezierCurve bezier = assertInstanceOf(StepBezierCurve.class, resolved.get(10));
        StepUniformCurve uniform = assertInstanceOf(StepUniformCurve.class, resolved.get(11));
        StepQuasiUniformCurve quasiUniform = assertInstanceOf(StepQuasiUniformCurve.class, resolved.get(12));
        StepPiecewiseBezierCurve piecewise = assertInstanceOf(StepPiecewiseBezierCurve.class, resolved.get(13));

        assertEquals(3, bezier.degree());
        assertEquals(4, bezier.controlPoints().size());
        assertEquals(2, uniform.degree());
        assertEquals(4, uniform.controlPoints().size());
        assertEquals(2, quasiUniform.degree());
        assertEquals(4, quasiUniform.controlPoints().size());
        assertEquals(1, piecewise.degree());
        assertEquals(4, piecewise.controlPoints().size());
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
    void shouldResolveRationalBsplineSurfaceAsFaceGeometry() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P00',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P10',(2.0,0.0,0.0));
                #3=CARTESIAN_POINT('P01',(0.0,2.0,0.0));
                #4=CARTESIAN_POINT('P11',(2.0,2.0,1.0));
                #10=(B_SPLINE_SURFACE(1,1,((#1,#3),(#2,#4)),.UNSPECIFIED.,.F.,.F.,.F.)
                     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.)
                     RATIONAL_B_SPLINE_SURFACE(((1.0,1.0),(1.0,0.5))));
                #11=EDGE_LOOP('L0',());
                #12=FACE_OUTER_BOUND('B0',#11,.T.);
                #13=ADVANCED_FACE('AF',(#12),#10,.T.);
                #14=FACE_SURFACE('FS',(#12),#10,.T.);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRationalBSplineSurface surface =
                assertInstanceOf(StepRationalBSplineSurface.class, resolved.get(10));
        StepAdvancedFace advancedFace = assertInstanceOf(StepAdvancedFace.class, resolved.get(13));
        StepFaceSurface faceSurface = assertInstanceOf(StepFaceSurface.class, resolved.get(14));
        assertEquals(2, surface.uKnots().size());
        assertEquals(10, advancedFace.faceGeometry().id());
        assertEquals(10, faceSurface.faceGeometry().id());
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
    void shouldResolveImplicitBsplineDataForSurfaceSubtypeMarkers() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(0.0,1.0,0.0));
                #4=CARTESIAN_POINT('P3',(1.0,1.0,0.0));
                #10=(BEZIER_SURFACE() B_SPLINE_SURFACE(1,1,((#1,#2),(#3,#4)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('bzs'));
                #11=(UNIFORM_SURFACE() B_SPLINE_SURFACE(1,1,((#1,#2),(#3,#4)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('us'));
                #12=(QUASI_UNIFORM_SURFACE() B_SPLINE_SURFACE(1,1,((#1,#2),(#3,#4)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('qus'));
                #13=(PIECEWISE_BEZIER_SURFACE() BEZIER_SURFACE() B_SPLINE_SURFACE(1,1,((#1,#2),(#3,#4)),.UNSPECIFIED.,.F.,.F.,.F.) BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('pbs'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepBezierSurface bezier = assertInstanceOf(StepBezierSurface.class, resolved.get(10));
        StepUniformSurface uniform = assertInstanceOf(StepUniformSurface.class, resolved.get(11));
        StepQuasiUniformSurface quasiUniform = assertInstanceOf(StepQuasiUniformSurface.class, resolved.get(12));
        StepPiecewiseBezierSurface piecewise = assertInstanceOf(StepPiecewiseBezierSurface.class, resolved.get(13));

        assertEquals(1, bezier.uDegree());
        assertEquals(2, bezier.controlPoints().size());
        assertEquals(1, uniform.uDegree());
        assertEquals(2, uniform.controlPoints().size());
        assertEquals(1, quasiUniform.vDegree());
        assertEquals(2, quasiUniform.controlPoints().getFirst().size());
        assertEquals(1, piecewise.uDegree());
        assertEquals(2, piecewise.controlPoints().getFirst().size());
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
    void shouldResolveDegenerateToroidalSurfaceAsFaceGeometryAndSetElement() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=DEGENERATE_TOROIDAL_SURFACE('DT0',#4,5.0,1.0,.T.);
                #6=EDGE_LOOP('L0',());
                #7=FACE_OUTER_BOUND('B0',#6,.T.);
                #8=ADVANCED_FACE('F0',(#7),#5,.T.);
                #9=GEOMETRIC_SET('SET',(#5));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepDegenerateToroidalSurface surface =
                assertInstanceOf(StepDegenerateToroidalSurface.class, resolved.get(5));
        StepAdvancedFace face = assertInstanceOf(StepAdvancedFace.class, resolved.get(8));
        StepGeometricSet set = assertInstanceOf(StepGeometricSet.class, resolved.get(9));
        assertEquals(5.0, surface.majorRadius());
        assertEquals(1.0, surface.minorRadius());
        assertEquals(true, surface.selectOuter());
        assertEquals(5, face.faceGeometry().id());
        assertEquals(5, set.elements().getFirst().id());
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
    void shouldResolveAnnotationTextOccurrenceWithPointReplicaPosition() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=DIRECTION('DY',(0.0,1.0,0.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=CARTESIAN_POINT('O',(10.0,20.0,30.0));
                #6=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#2,#3,#5,2.0,#4);
                #7=POINT_REPLICA('PR0',#1,#6);
                #8=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#7);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationTextOccurrence text = assertInstanceOf(StepAnnotationTextOccurrence.class, resolved.get(8));
        assertEquals("A=2.0", text.text());
        assertEquals(7, text.position().id());
    }

    @Test
    void shouldResolveAnnotationPointAndFillAreaOccurrencesWithVertexPointTargets() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=POLYLINE('B0',(#1,#1));
                #4=(ANNOTATION_FILL_AREA('FA0',(#3))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                #5=PRESENTATION_STYLE_ASSIGNMENT(());
                #6=(ANNOTATION_POINT_OCCURRENCE('AP0',(#5),#2)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('AP0',(#5),#2));
                #7=(ANNOTATION_FILL_AREA_OCCURRENCE('FAO0',(#5),#4,#2)
                    STYLED_ITEM('FAO0',(#5),#4)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FAO0'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationPointOccurrence pointOccurrence =
                assertInstanceOf(StepAnnotationPointOccurrence.class, resolved.get(6));
        StepAnnotationFillAreaOccurrence fillAreaOccurrence =
                assertInstanceOf(StepAnnotationFillAreaOccurrence.class, resolved.get(7));
        assertEquals(2, pointOccurrence.item().id());
        assertEquals(2, fillAreaOccurrence.fillStyleTarget().id());
    }

    @Test
    void shouldResolveAnnotationTextOccurrenceWithVertexPointPosition() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationTextOccurrence text = assertInstanceOf(StepAnnotationTextOccurrence.class, resolved.get(3));
        assertEquals("A=2.0", text.text());
        assertEquals(2, text.position().id());
    }

    @Test
    void shouldResolveAnnotationPointLikeOccurrencesWithContainerCarriers() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertEquals(3, assertInstanceOf(StepAnnotationTextOccurrence.class, resolved.get(7)).position().id());
        assertEquals(4, assertInstanceOf(StepAnnotationPointOccurrence.class, resolved.get(8)).item().id());
        assertEquals(5, assertInstanceOf(StepAnnotationFillAreaOccurrence.class, resolved.get(11)).fillStyleTarget().id());
    }

    @Test
    void shouldResolveAnnotationPointLikeOccurrencesWithVertexShellCarrier() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertEquals(4, assertInstanceOf(StepAnnotationTextOccurrence.class, resolved.get(6)).position().id());
        assertEquals(4, assertInstanceOf(StepAnnotationPointOccurrence.class, resolved.get(7)).item().id());
        assertEquals(4, assertInstanceOf(StepAnnotationFillAreaOccurrence.class, resolved.get(10)).fillStyleTarget().id());
    }

    @Test
    void shouldResolvePointContainersWithVertexPointMembers() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=POINT_SET('PS0',(#2));
                #4=GEOMETRIC_SET('GS0',(#2));
                #5=GEOMETRIC_CURVE_SET('GCS0',(#2));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepPointSet pointSet = assertInstanceOf(StepPointSet.class, resolved.get(3));
        StepGeometricSet geometricSet = assertInstanceOf(StepGeometricSet.class, resolved.get(4));
        StepGeometricCurveSet curveSet = assertInstanceOf(StepGeometricCurveSet.class, resolved.get(5));
        assertEquals(2, pointSet.points().getFirst().id());
        assertEquals(2, geometricSet.elements().getFirst().id());
        assertEquals(2, curveSet.elements().getFirst().id());
    }

    @Test
    void shouldResolveGeometricSetWithNestedPointAndCurveSets() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=POINT_SET('PS0',(#2));
                #4=GEOMETRIC_CURVE_SET('GCS0',(#2));
                #5=GEOMETRIC_SET('GS0',(#3,#4));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepGeometricSet geometricSet = assertInstanceOf(StepGeometricSet.class, resolved.get(5));
        assertEquals(2, geometricSet.elements().size());
        assertEquals(3, geometricSet.elements().get(0).id());
        assertEquals(4, geometricSet.elements().get(1).id());
    }

    @Test
    void shouldResolvePointReplicaWithVertexPointParent() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(1.0,2.0,3.0));
                #2=VERTEX_POINT('VP0',#1);
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=DIRECTION('DY',(0.0,1.0,0.0));
                #5=DIRECTION('DZ',(0.0,0.0,1.0));
                #6=CARTESIAN_POINT('O',(10.0,20.0,30.0));
                #7=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#3,#4,#6,2.0,#5);
                #8=POINT_REPLICA('PR0',#2,#7);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepGeometricReplica pointReplica = assertInstanceOf(StepGeometricReplica.class, resolved.get(8));
        assertEquals("POINT_REPLICA", pointReplica.entityName());
        assertEquals(2, pointReplica.parent().id());
    }

    @Test
    void shouldRejectPointReplicaWithPointMarkerParent() {
        String step = """
                DATA;
                #1=(POINT() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('P'));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=DIRECTION('DY',(0.0,1.0,0.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=CARTESIAN_POINT('O',(10.0,20.0,30.0));
                #6=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T0',#2,#3,#5,2.0,#4);
                #7=POINT_REPLICA('PR0',#1,#6);
                ENDSEC;
                """;

        StepResolutionException error = assertThrows(
                StepResolutionException.class,
                () -> StepEntityResolver.resolveAll(StepParser.parse(step))
        );

        assertTrue(error.getMessage().contains("POINT_REPLICA parent must reference a supported point"));
    }

    @Test
    void shouldRejectPointMarkerInAnnotationPointCarriers() {
        String step = """
                DATA;
                #1=(POINT() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('P'));
                #2=PRESENTATION_STYLE_ASSIGNMENT(());
                #3=(ANNOTATION_POINT_OCCURRENCE('AP0',(#2),#1)
                    DRAUGHTING_ANNOTATION_OCCURRENCE('AP0',(#2),#1));
                #4=ANNOTATION_TEXT_OCCURRENCE('NOTE','text',#1);
                #5=POLYLINE('B0',());
                #6=(ANNOTATION_FILL_AREA('FA0',(#5))
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FA0'));
                #7=(ANNOTATION_FILL_AREA_OCCURRENCE('FAO0',(#2),#6,#1)
                    STYLED_ITEM('FAO0',(#2),#6)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FAO0'));
                ENDSEC;
                """;

        StepResolutionException error = assertThrows(
                StepResolutionException.class,
                () -> StepEntityResolver.resolveAll(StepParser.parse(step))
        );

        assertTrue(error.getMessage().contains(
                "must reference supported point carriers or point-like annotation content/occurrences"));
    }

    @Test
    void shouldRejectPointMarkerInPointSet() {
        String step = """
                DATA;
                #1=(POINT() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('P'));
                #2=POINT_SET('PS0',(#1));
                ENDSEC;
                """;

        UnsupportedStepEntityException error = assertThrows(
                UnsupportedStepEntityException.class,
                () -> StepEntityResolver.resolveAll(StepParser.parse(step))
        );

        assertTrue(error.getMessage().contains(
                "POINT_SET points must reference supported point carriers or point-like annotation content/occurrences"));
    }

    @Test
    void shouldResolveNestedPointSetAndAnnotationPlaneOccurrenceElements() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepPointSet pointSet = (StepPointSet) resolved.get(6);
        assertEquals(List.of(3, 5), pointSet.points().stream().map(StepEntity::id).toList());

        StepAnnotationPlane annotationPlane = (StepAnnotationPlane) resolved.get(14);
        assertEquals(List.of(12, 13), annotationPlane.elements().stream().map(StepEntity::id).toList());
    }

    @Test
    void shouldResolveItemIdentifiedRepresentationUsageFamily() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PMICTX'));
                #3=DRAUGHTING_MODEL('DM',(#1),#2);
                #4=PROPERTY_DEFINITION('PD','',#1);
                #5=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #6=DIRECTION('DZ',(0.0,0.0,1.0));
                #7=DIRECTION('DX',(1.0,0.0,0.0));
                #8=AXIS2_PLACEMENT_3D('AX',#5,#6,#7);
                #9=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#5);
                #10=GEOMETRIC_CURVE_SET('LEADER',(#5));
                #11=GEOMETRIC_SET('PHSET',(#5));
                #12=DRAUGHTING_CALLOUT('CALLOUT',(#9,#10));
                #13=ANNOTATION_PLACEHOLDER_OCCURRENCE('PH',(),#11,.TITLE.,1.0);
                #14=ITEM_IDENTIFIED_REPRESENTATION_USAGE('USAGE','generic',#4,#3,#12);
                #15=DRAUGHTING_MODEL_ITEM_ASSOCIATION('DMIA','assoc',#4,#3,#12);
                #16=DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER('DMIAP','assocph',#4,#3,#12,#13);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepItemIdentifiedRepresentationUsage generic =
                assertInstanceOf(StepItemIdentifiedRepresentationUsage.class, resolved.get(14));
        StepDraughtingModelItemAssociation association =
                assertInstanceOf(StepDraughtingModelItemAssociation.class, resolved.get(15));
        StepDraughtingModelItemAssociationWithPlaceholder withPlaceholder =
                assertInstanceOf(StepDraughtingModelItemAssociationWithPlaceholder.class, resolved.get(16));
        assertEquals(4, generic.definition().id());
        assertEquals(3, generic.usedRepresentation().id());
        assertEquals(12, generic.identifiedItem().id());
        assertEquals(4, association.definition().id());
        assertEquals(3, association.usedRepresentation().id());
        assertEquals(12, association.identifiedItem().id());
        assertEquals(4, withPlaceholder.definition().id());
        assertEquals(3, withPlaceholder.usedRepresentation().id());
        assertEquals(12, withPlaceholder.identifiedItem().id());
        assertEquals(13, withPlaceholder.annotationPlaceholder().id());
    }

    @Test
    void shouldResolveExtendedAnnotationUsageFamilyEntities() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepGeometricItemSpecificUsage geometricUsage =
                assertInstanceOf(StepGeometricItemSpecificUsage.class, resolved.get(15));
        StepChainBasedGeometricItemSpecificUsage chainUsage =
                assertInstanceOf(StepChainBasedGeometricItemSpecificUsage.class, resolved.get(16));
        StepDraughtingModelItemAssociationWithPlaceholder withPlaceholder =
                assertInstanceOf(StepDraughtingModelItemAssociationWithPlaceholder.class, resolved.get(17));

        assertEquals(7, geometricUsage.usage().id());
        assertEquals(11, geometricUsage.identifiedItem().id());
        assertEquals(10, chainUsage.usage().id());
        assertEquals(11, chainUsage.identifiedItem().id());
        assertEquals(2, chainUsage.nodes().size());
        assertEquals(1, chainUsage.undirectedLinks().size());
        assertEquals(7, withPlaceholder.identifiedItem().id());
        assertEquals(9, withPlaceholder.annotationPlaceholder().id());
    }

    @Test
    void shouldResolveGeometricItemSpecificUsageWithPathAndWireTargets() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepGeometricItemSpecificUsage geometricUsage =
                assertInstanceOf(StepGeometricItemSpecificUsage.class, resolved.get(18));
        StepChainBasedGeometricItemSpecificUsage chainUsage =
                assertInstanceOf(StepChainBasedGeometricItemSpecificUsage.class, resolved.get(19));

        assertEquals(17, geometricUsage.usage().id());
        assertEquals(10, geometricUsage.identifiedItem().id());
        assertEquals(17, chainUsage.usage().id());
        assertEquals(11, chainUsage.identifiedItem().id());
    }

    @Test
    void shouldResolveGeometricItemSpecificUsageWithShellModelAndSolidTargets() {
        String step = """
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
                #31=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU','',#22,#16);
                #32=CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE('CGU','',#23,(#19,#20),(#21),#17);
                #33=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU_SOLID','',#22,#24);
                #34=GEOMETRIC_ITEM_SPECIFIC_USAGE('GIU_SET','',#22,#27);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepGeometricItemSpecificUsage shellUsage =
                assertInstanceOf(StepGeometricItemSpecificUsage.class, resolved.get(31));
        StepChainBasedGeometricItemSpecificUsage modelUsage =
                assertInstanceOf(StepChainBasedGeometricItemSpecificUsage.class, resolved.get(32));
        StepGeometricItemSpecificUsage solidUsage =
                assertInstanceOf(StepGeometricItemSpecificUsage.class, resolved.get(33));
        StepGeometricItemSpecificUsage setUsage =
                assertInstanceOf(StepGeometricItemSpecificUsage.class, resolved.get(34));

        assertEquals(22, shellUsage.usage().id());
        assertEquals(16, shellUsage.identifiedItem().id());
        assertEquals(23, modelUsage.usage().id());
        assertEquals(17, modelUsage.identifiedItem().id());
        assertEquals(2, modelUsage.nodes().size());
        assertEquals(1, modelUsage.undirectedLinks().size());
        assertEquals(22, solidUsage.usage().id());
        assertEquals(24, solidUsage.identifiedItem().id());
        assertEquals(22, setUsage.usage().id());
        assertEquals(27, setUsage.identifiedItem().id());
    }

    @Test
    void shouldResolveDraughtingCalloutFamilyEntities() {
        String step = """
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
                #25=DRAUGHTING_CALLOUT_RELATIONSHIP('REL','callout-link',#15,#16);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepDraughtingCallout leaderDirected = assertInstanceOf(StepDraughtingCallout.class, resolved.get(15));
        StepDraughtingCallout projectionDirected = assertInstanceOf(StepDraughtingCallout.class, resolved.get(16));
        StepDraughtingCallout dimensionCurveDirected = assertInstanceOf(StepDraughtingCallout.class, resolved.get(17));
        StepDraughtingCallout dimension = assertInstanceOf(StepDraughtingCallout.class, resolved.get(18));
        StepDraughtingCallout structuredDimension = assertInstanceOf(StepDraughtingCallout.class, resolved.get(19));
        StepDraughtingCallout surfaceCondition = assertInstanceOf(StepDraughtingCallout.class, resolved.get(20));
        StepDraughtingCallout datumFeature = assertInstanceOf(StepDraughtingCallout.class, resolved.get(21));
        StepDraughtingCallout datumTarget = assertInstanceOf(StepDraughtingCallout.class, resolved.get(22));
        StepDraughtingCallout geometricalTolerance = assertInstanceOf(StepDraughtingCallout.class, resolved.get(23));
        StepDraughtingCallout roughness = assertInstanceOf(StepDraughtingCallout.class, resolved.get(24));
        StepDraughtingCalloutRelationship relationship =
                assertInstanceOf(StepDraughtingCalloutRelationship.class, resolved.get(25));
        assertEquals(2, leaderDirected.contents().size());
        assertEquals(2, projectionDirected.contents().size());
        assertEquals(2, dimensionCurveDirected.contents().size());
        assertEquals(2, dimension.contents().size());
        assertEquals(2, structuredDimension.contents().size());
        assertEquals(2, surfaceCondition.contents().size());
        assertEquals(2, datumFeature.contents().size());
        assertEquals(2, datumTarget.contents().size());
        assertEquals(2, geometricalTolerance.contents().size());
        assertEquals(2, roughness.contents().size());
        assertEquals("LEADER_DIRECTED_CALLOUT", leaderDirected.entityName());
        assertEquals("PROJECTION_DIRECTED_CALLOUT", projectionDirected.entityName());
        assertEquals("DIMENSION_CURVE_DIRECTED_CALLOUT", dimensionCurveDirected.entityName());
        assertEquals("DIMENSION_CALLOUT", dimension.entityName());
        assertEquals("STRUCTURED_DIMENSION_CALLOUT", structuredDimension.entityName());
        assertEquals("SURFACE_CONDITION_CALLOUT", surfaceCondition.entityName());
        assertEquals("DATUM_FEATURE_CALLOUT", datumFeature.entityName());
        assertEquals("DATUM_TARGET_CALLOUT", datumTarget.entityName());
        assertEquals("GEOMETRICAL_TOLERANCE_CALLOUT", geometricalTolerance.entityName());
        assertEquals("ROUGHNESS_CALLOUT", roughness.entityName());
        assertEquals(15, relationship.relatingCallout().id());
        assertEquals(16, relationship.relatedCallout().id());
    }

    @Test
    void shouldResolveDraughtingCalloutWithDirectAnnotationContent() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepDraughtingCallout symbolCallout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(20));
        StepDraughtingCallout textCallout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(21));
        StepDraughtingCallout textCharacterCallout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(22));
        StepDraughtingCallout fillCallout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(23));
        assertEquals(12, symbolCallout.contents().get(1).id());
        assertEquals(15, textCallout.contents().get(1).id());
        assertEquals(16, textCharacterCallout.contents().get(1).id());
        assertEquals(17, fillCallout.contents().get(1).id());
    }

    @Test
    void shouldResolveDraughtingCalloutWithPointContainersAndPlaneContents() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepDraughtingCallout pointSetCallout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(13));
        StepDraughtingCallout placeholderCallout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(14));
        StepDraughtingCallout planeCallout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(15));
        assertEquals(4, pointSetCallout.contents().get(1).id());
        assertEquals(7, placeholderCallout.contents().get(1).id());
        assertEquals(12, planeCallout.contents().get(1).id());
    }

    @Test
    void shouldResolveDraughtingCalloutWithGeometricSetContent() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#1);
                #4=GEOMETRIC_SET('GS',(#2));
                #5=DRAUGHTING_CALLOUT('GS_CALLOUT',(#3,#4));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepDraughtingCallout callout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(5));
        assertEquals(2, callout.contents().size());
        assertEquals(4, callout.contents().get(1).id());
    }

    @Test
    void shouldResolveDraughtingCalloutWithPathAndWireContents() {
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepDraughtingCallout pathCallout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(16));
        StepDraughtingCallout edgeSetCallout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(17));
        StepDraughtingCallout wireCallout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(18));
        assertEquals(11, pathCallout.contents().get(1).id());
        assertEquals(12, edgeSetCallout.contents().get(1).id());
        assertEquals(14, wireCallout.contents().get(1).id());
    }

    @Test
    void shouldResolveDraughtingCalloutWithDirectGeometryContents() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepDraughtingCallout callout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(19));
        assertEquals(8, callout.contents().size());
        assertEquals(1, callout.contents().get(1).id());
        assertEquals(5, callout.contents().get(2).id());
        assertEquals(17, callout.contents().get(3).id());
        assertEquals(11, callout.contents().get(4).id());
        assertEquals(12, callout.contents().get(5).id());
        assertEquals(13, callout.contents().get(6).id());
        assertEquals(14, callout.contents().get(7).id());
    }

    @Test
    void shouldResolveDraughtingCalloutWithAdvancedCurveContents() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepDraughtingCallout callout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(14));
        assertEquals(4, callout.contents().size());
        assertEquals(8, callout.contents().get(1).id());
        assertEquals(9, callout.contents().get(2).id());
        assertEquals(12, callout.contents().get(3).id());
    }

    @Test
    void shouldResolveDraughtingCalloutWithExtendedContainerContents() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepDraughtingCallout polyLoopCallout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(23));
        StepDraughtingCallout vertexLoopCallout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(24));
        StepDraughtingCallout vertexShellCallout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(25));
        StepDraughtingCallout edgeWireframeCallout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(26));
        StepDraughtingCallout shellWireframeCallout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(27));
        assertEquals(17, polyLoopCallout.contents().get(1).id());
        assertEquals(18, vertexLoopCallout.contents().get(1).id());
        assertEquals(19, vertexShellCallout.contents().get(1).id());
        assertEquals(16, edgeWireframeCallout.contents().get(1).id());
        assertEquals(21, shellWireframeCallout.contents().get(1).id());
    }

    @Test
    void shouldResolveDraughtingCalloutWithFaceShellAndSurfaceModelContents() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertEquals(80, assertInstanceOf(StepDraughtingCallout.class, resolved.get(93)).contents().get(1).id());
        assertEquals(81, assertInstanceOf(StepDraughtingCallout.class, resolved.get(94)).contents().get(1).id());
        assertEquals(82, assertInstanceOf(StepDraughtingCallout.class, resolved.get(95)).contents().get(1).id());
        assertEquals(83, assertInstanceOf(StepDraughtingCallout.class, resolved.get(96)).contents().get(1).id());
        assertEquals(84, assertInstanceOf(StepDraughtingCallout.class, resolved.get(97)).contents().get(1).id());
        assertEquals(85, assertInstanceOf(StepDraughtingCallout.class, resolved.get(98)).contents().get(1).id());
        assertEquals(86, assertInstanceOf(StepDraughtingCallout.class, resolved.get(99)).contents().get(1).id());
        assertEquals(87, assertInstanceOf(StepDraughtingCallout.class, resolved.get(100)).contents().get(1).id());
        assertEquals(88, assertInstanceOf(StepDraughtingCallout.class, resolved.get(101)).contents().get(1).id());
        assertEquals(89, assertInstanceOf(StepDraughtingCallout.class, resolved.get(102)).contents().get(1).id());
        assertEquals(90, assertInstanceOf(StepDraughtingCallout.class, resolved.get(103)).contents().get(1).id());
        assertEquals(91, assertInstanceOf(StepDraughtingCallout.class, resolved.get(104)).contents().get(1).id());
    }

    @Test
    void shouldResolveDraughtingCalloutWithBrepSolidContents() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertEquals(82, assertInstanceOf(StepDraughtingCallout.class, resolved.get(85)).contents().get(1).id());
        assertEquals(83, assertInstanceOf(StepDraughtingCallout.class, resolved.get(86)).contents().get(1).id());
    }

    @Test
    void shouldResolveDraughtingCalloutWithBuildableSolidFamilyContents() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertEquals(5, assertInstanceOf(StepDraughtingCallout.class, resolved.get(31)).contents().get(1).id());
        assertEquals(10, assertInstanceOf(StepDraughtingCallout.class, resolved.get(32)).contents().get(1).id());
        assertEquals(11, assertInstanceOf(StepDraughtingCallout.class, resolved.get(33)).contents().get(1).id());
        assertEquals(12, assertInstanceOf(StepDraughtingCallout.class, resolved.get(34)).contents().get(1).id());
        assertEquals(25, assertInstanceOf(StepDraughtingCallout.class, resolved.get(35)).contents().get(1).id());
        assertEquals(29, assertInstanceOf(StepDraughtingCallout.class, resolved.get(36)).contents().get(1).id());
    }

    @Test
    void shouldResolveDraughtingCalloutWithOccurrenceWrappers() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepDraughtingCallout subfigureCallout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(17));
        StepDraughtingCallout draughtingCallout = assertInstanceOf(StepDraughtingCallout.class, resolved.get(18));
        assertEquals(13, subfigureCallout.contents().get(1).id());
        assertEquals(14, draughtingCallout.contents().get(1).id());
    }

    @Test
    void shouldResolvePlacedTarget() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','TARGET');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','TARGETCTX'));
                #3=REPRESENTATION('R',(#1),#2);
                #4=PROPERTY_DEFINITION('PD','',#1);
                #5=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #6=ANNOTATION_TEXT_OCCURRENCE('NOTE','T',#5);
                #7=GEOMETRIC_CURVE_SET('LEADER',(#5));
                #8=DRAUGHTING_CALLOUT('CALLOUT',(#6,#7));
                #9=PLACED_TARGET('PT','target',#4,#3,#8);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepPlacedTarget placedTarget = assertInstanceOf(StepPlacedTarget.class, resolved.get(9));
        assertEquals(4, placedTarget.definition().id());
        assertEquals(3, placedTarget.usedRepresentation().id());
        assertEquals(8, placedTarget.identifiedItem().id());
    }

    @Test
    void shouldResolveExtendedItemIdentifiedRepresentationUsageFamily() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','CTX'));
                #3=REPRESENTATION('REP_A',(#1),#2);
                #4=REPRESENTATION('REP_B',(#1),#2);
                #5=REPRESENTATION_RELATIONSHIP('RR','chain',#3,#4);
                #6=PROPERTY_DEFINITION('PD','',#1);
                #7=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #8=ANNOTATION_TEXT_OCCURRENCE('NOTE','A=2.0',#7);
                #9=GEOMETRIC_CURVE_SET('LEADER',(#7));
                #10=DRAUGHTING_CALLOUT('CALLOUT',(#8,#9));
                #11=ADVANCED_FACE('FACE0',(),#14,.T.);
                #12=CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE('CBIIRU','chain',#6,(#3,#4),(#5),#10);
                #13=CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE('CBGISU','chain-geo',#10,(#3,#4),(#5),#11);
                #14=PLANE('PL0',#15);
                #15=AXIS2_PLACEMENT_3D('AX',#7,#16,#17);
                #16=DIRECTION('DZ',(0.0,0.0,1.0));
                #17=DIRECTION('DX',(1.0,0.0,0.0));
                #18=PMI_REQUIREMENT_ITEM_ASSOCIATION('PRIA','req',#6,#4,#10,#6);
                #19=MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION('MDRIA','mdreq',#6,#4,#10,#6);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepChainBasedItemIdentifiedRepresentationUsage chainUsage =
                assertInstanceOf(StepChainBasedItemIdentifiedRepresentationUsage.class, resolved.get(12));
        StepChainBasedGeometricItemSpecificUsage chainGeometric =
                assertInstanceOf(StepChainBasedGeometricItemSpecificUsage.class, resolved.get(13));
        StepPmiRequirementItemAssociation pmiRequirement =
                assertInstanceOf(StepPmiRequirementItemAssociation.class, resolved.get(18));
        StepMechanicalDesignRequirementItemAssociation mechanicalRequirement =
                assertInstanceOf(StepMechanicalDesignRequirementItemAssociation.class, resolved.get(19));
        assertEquals(6, chainUsage.definition().id());
        assertEquals(2, chainUsage.nodes().size());
        assertEquals(1, chainUsage.undirectedLinks().size());
        assertEquals(3, chainUsage.root().id());
        assertEquals(4, chainUsage.leaf().id());
        assertEquals(10, chainUsage.identifiedItem().id());
        assertEquals(10, chainGeometric.usage().id());
        assertEquals(2, chainGeometric.nodes().size());
        assertEquals(1, chainGeometric.undirectedLinks().size());
        assertEquals(11, chainGeometric.identifiedItem().id());
        assertEquals(6, pmiRequirement.definition().id());
        assertEquals(4, pmiRequirement.usedRepresentation().id());
        assertEquals(10, pmiRequirement.identifiedItem().id());
        assertEquals(6, pmiRequirement.requirement().id());
        assertEquals(6, mechanicalRequirement.definition().id());
        assertEquals(4, mechanicalRequirement.usedRepresentation().id());
        assertEquals(10, mechanicalRequirement.identifiedItem().id());
        assertEquals(6, mechanicalRequirement.requirement().id());
    }

    @Test
    void shouldResolveUsageAndAssociationFamilyWithVertexLoopTarget() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertEquals(10, assertInstanceOf(StepItemIdentifiedRepresentationUsage.class, resolved.get(11)).identifiedItem().id());
        assertEquals(10, assertInstanceOf(StepChainBasedItemIdentifiedRepresentationUsage.class, resolved.get(12)).identifiedItem().id());
        assertEquals(10, assertInstanceOf(StepDraughtingModelItemAssociation.class, resolved.get(13)).identifiedItem().id());
        assertEquals(10, assertInstanceOf(StepPmiRequirementItemAssociation.class, resolved.get(14)).identifiedItem().id());
        assertEquals(10, assertInstanceOf(StepMechanicalDesignRequirementItemAssociation.class, resolved.get(15)).identifiedItem().id());
        assertEquals(10, assertInstanceOf(StepPlacedTarget.class, resolved.get(16)).identifiedItem().id());
    }

    @Test
    void shouldResolveUsageFamiliesWithDirectAnnotationContentEntities() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertEquals(14, assertInstanceOf(StepItemIdentifiedRepresentationUsage.class, resolved.get(27)).identifiedItem().id());
        assertEquals(18, assertInstanceOf(StepChainBasedItemIdentifiedRepresentationUsage.class, resolved.get(28)).identifiedItem().id());
        assertEquals(19, assertInstanceOf(StepDraughtingModelItemAssociation.class, resolved.get(29)).identifiedItem().id());
        assertEquals(24, assertInstanceOf(StepPmiRequirementItemAssociation.class, resolved.get(30)).identifiedItem().id());
        assertEquals(14, assertInstanceOf(StepMechanicalDesignRequirementItemAssociation.class, resolved.get(31)).identifiedItem().id());
        assertEquals(18, assertInstanceOf(StepPlacedTarget.class, resolved.get(32)).identifiedItem().id());
        assertEquals(24, assertInstanceOf(StepGeometricItemSpecificUsage.class, resolved.get(33)).usage().id());
        assertEquals(14, assertInstanceOf(StepChainBasedGeometricItemSpecificUsage.class, resolved.get(34)).usage().id());
        assertEquals(19, assertInstanceOf(StepDraughtingModelItemAssociationWithPlaceholder.class, resolved.get(35)).identifiedItem().id());
    }

    @Test
    void shouldResolveAdditionalAnnotationOccurrenceFamilyEntities() {
        String step = """
                DATA;
                #1=(GEOMETRIC_REPRESENTATION_CONTEXT(2) REPRESENTATION_CONTEXT('ID','SYMBOL'));
                #2=REPRESENTATION('SYMREP',(),#1);
                #3=CARTESIAN_POINT('O',(0.0,0.0));
                #4=DIRECTION('X',(1.0,0.0));
                #5=AXIS2_PLACEMENT_2D('MAP',#3,#4);
                #6=SYMBOL_REPRESENTATION_MAP(#5,#2);
                #7=CARTESIAN_POINT('P',(10.0,20.0));
                #8=DIRECTION('DX',(1.0,0.0));
                #9=AXIS2_PLACEMENT_2D('TGT',#7,#8);
                #10=ANNOTATION_SYMBOL('AS0',#6,#9);
                #11=ANNOTATION_SYMBOL_OCCURRENCE('ASO',(),#10);
                #12=ANNOTATION_SUBFIGURE_OCCURRENCE('SUB',(),#10);
                #13=DRAUGHTING_ANNOTATION_OCCURRENCE('DAO',(),#10);
                #14=ANNOTATION_OCCURRENCE_ASSOCIATIVITY('AOA','assoc',#11,#12);
                #15=DIMENSION_CURVE_TERMINATOR_TO_PROJECTION_CURVE_ASSOCIATIVITY('DCTPCA','assoc',#12,#13);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationSubfigureOccurrence subfigure =
                assertInstanceOf(StepAnnotationSubfigureOccurrence.class, resolved.get(12));
        StepDraughtingAnnotationOccurrence draughting =
                assertInstanceOf(StepDraughtingAnnotationOccurrence.class, resolved.get(13));
        StepAnnotationOccurrenceRelationship associativity =
                assertInstanceOf(StepAnnotationOccurrenceRelationship.class, resolved.get(14));
        StepAnnotationOccurrenceRelationship dimensionAssociativity =
                assertInstanceOf(StepAnnotationOccurrenceRelationship.class, resolved.get(15));
        assertEquals(10, subfigure.item().id());
        assertEquals(10, draughting.item().id());
        assertEquals(11, associativity.relatingAnnotationOccurrence().id());
        assertEquals(12, associativity.relatedAnnotationOccurrence().id());
        assertEquals(12, dimensionAssociativity.relatingAnnotationOccurrence().id());
        assertEquals(13, dimensionAssociativity.relatedAnnotationOccurrence().id());
    }

    @Test
    void shouldResolveAnnotationWrapperOccurrencesWithAdditionalAnnotationItems() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationSymbolOccurrence symbolOccurrence =
                assertInstanceOf(StepAnnotationSymbolOccurrence.class, resolved.get(6));
        StepAnnotationSubfigureOccurrence subfigureOccurrence =
                assertInstanceOf(StepAnnotationSubfigureOccurrence.class, resolved.get(7));
        assertEquals(4, symbolOccurrence.item().id());
        assertEquals(5, subfigureOccurrence.item().id());
    }

    @Test
    void shouldResolveAnnotationOccurrenceAssociativityWithTerminatorSymbol() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationOccurrenceRelationship associativity =
                assertInstanceOf(StepAnnotationOccurrenceRelationship.class, resolved.get(19));
        StepAnnotationOccurrenceRelationship dimensionAssociativity =
                assertInstanceOf(StepAnnotationOccurrenceRelationship.class, resolved.get(20));
        assertEquals(12, associativity.relatingAnnotationOccurrence().id());
        assertEquals(18, associativity.relatedAnnotationOccurrence().id());
        assertEquals(18, dimensionAssociativity.relatingAnnotationOccurrence().id());
        assertEquals(17, dimensionAssociativity.relatedAnnotationOccurrence().id());
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
    void shouldResolveTerminatorSymbolWithWrappedAnnotationItem() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepTerminatorSymbol terminatorSymbol =
                assertInstanceOf(StepTerminatorSymbol.class, resolved.get(10));
        assertEquals("TS0", terminatorSymbol.name());
        assertEquals(5, terminatorSymbol.item().id());
        assertEquals(9, terminatorSymbol.annotatedCurve().id());
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
    void shouldResolveAnnotationOccurrenceRelationshipWithTerminatorSymbol() {
        String step = """
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
                #19=ANNOTATION_OCCURRENCE_RELATIONSHIP('rel','links symbol to terminator',#18,#17);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationOccurrenceRelationship relationship =
                assertInstanceOf(StepAnnotationOccurrenceRelationship.class, resolved.get(19));
        assertEquals("rel", relationship.name());
        assertEquals("links symbol to terminator", relationship.description());
        assertEquals(18, relationship.relatingAnnotationOccurrence().id());
        assertEquals(17, relationship.relatedAnnotationOccurrence().id());
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
    void shouldResolveGeometricCurveSetWithTopologyAndPaths() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=VERTEX_POINT('V0',#1);
                #5=VERTEX_POINT('V1',#2);
                #6=VERTEX_POINT('V2',#3);
                #7=LINE('L0',#1,#10);
                #8=EDGE_CURVE('E0',#4,#5,#7,.T.);
                #9=(SUBEDGE('SE0',#4,#5,#8) EDGE() TOPOLOGICAL_REPRESENTATION_ITEM('subedge'));
                #10=VECTOR('VX',#11,1.0);
                #11=DIRECTION('DX',(1.0,0.0,0.0));
                #12=ORIENTED_EDGE('OE0',*,*,#8,.T.);
                #13=PATH('PTH',(#12));
                #14=POLY_LOOP('PL0',(#1,#2,#3));
                #15=GEOMETRIC_CURVE_SET('GCS0',(#9,#13,#14));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepGeometricCurveSet curveSet = assertInstanceOf(StepGeometricCurveSet.class, resolved.get(15));
        assertEquals("GCS0", curveSet.name());
        assertEquals(3, curveSet.elements().size());
        assertSame(StepSubedge.class, curveSet.elements().get(0).getClass());
        assertSame(StepPath.class, curveSet.elements().get(1).getClass());
        assertSame(StepPolyLoop.class, curveSet.elements().get(2).getClass());
    }

    @Test
    void shouldResolveGeometricSetWithSurfaceAndTopologyMembers() {
        String step = """
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
                #14=ORIENTED_EDGE('OE0',*,*,#13,.T.);
                #15=ORIENTED_PATH('OP0',#16,.T.);
                #16=PATH('PTH',(#14));
                #17=POLY_LOOP('PL0',(#6,#7,#8));
                #18=GEOMETRIC_SET('GS0',(#5,#15,#17));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepGeometricSet set = assertInstanceOf(StepGeometricSet.class, resolved.get(18));
        assertEquals("GS0", set.name());
        assertEquals(3, set.elements().size());
        assertSame(StepCylindricalSurface.class, set.elements().get(0).getClass());
        assertSame(StepOrientedPath.class, set.elements().get(1).getClass());
        assertSame(StepPolyLoop.class, set.elements().get(2).getClass());
    }

    @Test
    void shouldResolveGeometricSetsWithWireAndLoopContainers() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=CARTESIAN_POINT('P2',(1.0,1.0,0.0));
                #4=VERTEX_POINT('V0',#1);
                #5=VERTEX_POINT('V1',#2);
                #6=VECTOR('VX',#7,1.0);
                #7=DIRECTION('DX',(1.0,0.0,0.0));
                #8=LINE('L0',#1,#6);
                #9=EDGE_CURVE('E0',#4,#5,#8,.T.);
                #10=ORIENTED_EDGE('OE0',*,*,#9,.T.);
                #11=ORIENTED_EDGE('OE1',*,*,#9,.F.);
                #12=CONNECTED_EDGE_SET('CES0',(#9,#10));
                #13=VERTEX_LOOP('VL0',#4);
                #14=EDGE_LOOP('EL0',(#10,#11));
                #15=WIRE_SHELL('WS0',(#14,#13));
                #16=GEOMETRIC_CURVE_SET('GCS0',(#12,#14));
                #17=GEOMETRIC_SET('GS0',(#12,#14,#13,#15));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepGeometricCurveSet curveSet = assertInstanceOf(StepGeometricCurveSet.class, resolved.get(16));
        assertEquals(2, curveSet.elements().size());
        assertSame(StepConnectedEdgeSet.class, curveSet.elements().get(0).getClass());
        assertSame(StepEdgeLoop.class, curveSet.elements().get(1).getClass());

        StepGeometricSet set = assertInstanceOf(StepGeometricSet.class, resolved.get(17));
        assertEquals(4, set.elements().size());
        assertSame(StepConnectedEdgeSet.class, set.elements().get(0).getClass());
        assertSame(StepEdgeLoop.class, set.elements().get(1).getClass());
        assertSame(StepVertexLoop.class, set.elements().get(2).getClass());
        assertSame(StepWireShell.class, set.elements().get(3).getClass());
    }

    @Test
    void shouldResolveGeometricCurveSetWithWireContainersAndWireframeModels() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepGeometricCurveSet curveSet = assertInstanceOf(StepGeometricCurveSet.class, resolved.get(18));
        assertEquals(6, curveSet.elements().size());
        assertSame(StepConnectedEdgeSet.class, curveSet.elements().get(0).getClass());
        assertSame(StepEdgeLoop.class, curveSet.elements().get(1).getClass());
        assertSame(StepVertexLoop.class, curveSet.elements().get(2).getClass());
        assertSame(StepWireShell.class, curveSet.elements().get(3).getClass());
        assertSame(StepEdgeBasedWireframeModel.class, curveSet.elements().get(4).getClass());
        assertSame(StepShellBasedWireframeModel.class, curveSet.elements().get(5).getClass());
    }

    @Test
    void shouldResolveGeometricSetWithShellModelAndSolidMembers() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepGeometricSet set = assertInstanceOf(StepGeometricSet.class, resolved.get(15));
        assertEquals(3, set.elements().size());
        assertSame(StepOpenShell.class, set.elements().get(0).getClass());
        assertSame(StepFaceBasedSurfaceModel.class, set.elements().get(1).getClass());
        assertSame(StepCsgPrimitive.class, set.elements().get(2).getClass());
    }

    @Test
    void shouldResolveGeometricSetsWithNestedSetMembers() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepGeometricCurveSet outerCurveSet = assertInstanceOf(StepGeometricCurveSet.class, resolved.get(9));
        assertEquals(3, outerCurveSet.elements().size());
        assertSame(StepPointSet.class, outerCurveSet.elements().get(0).getClass());
        assertSame(StepGeometricSet.class, outerCurveSet.elements().get(1).getClass());
        assertSame(StepGeometricCurveSet.class, outerCurveSet.elements().get(2).getClass());

        StepGeometricSet outerSet = assertInstanceOf(StepGeometricSet.class, resolved.get(10));
        assertEquals(2, outerSet.elements().size());
        assertSame(StepGeometricSet.class, outerSet.elements().get(0).getClass());
        assertSame(StepGeometricCurveSet.class, outerSet.elements().get(1).getClass());
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
    void shouldResolveNamedUnitWithDimensionalExponentsReference() {
        String step = """
                DATA;
                #1=DIMENSIONAL_EXPONENTS(1.0,0.0,0.0,0.0,0.0,0.0,0.0);
                #2=(LENGTH_UNIT() NAMED_UNIT(#1) SI_UNIT($,.METRE.));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepDimensionalExponents dimensions =
                assertInstanceOf(StepDimensionalExponents.class, resolved.get(1));
        StepSiUnit unit = assertInstanceOf(StepSiUnit.class, resolved.get(2));
        assertEquals(1.0, dimensions.lengthExponent());
        assertEquals(0.0, dimensions.massExponent());
        assertEquals("LENGTH_UNIT", unit.unitKind());
        assertEquals("METRE", unit.unitName());
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
    void shouldResolveAdditionalPhysicalTypedMeasureWithUnitSubtypes() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        List<String> unitKinds = List.of(
                "MASS_DENSITY_UNIT",
                "DYNAMIC_VISCOSITY_UNIT",
                "KINEMATIC_VISCOSITY_UNIT",
                "MOMENT_OF_INERTIA_UNIT",
                "THERMAL_CONDUCTIVITY_UNIT",
                "HEAT_FLUX_DENSITY_UNIT",
                "SPECIFIC_HEAT_CAPACITY_UNIT",
                "AREA_DENSITY_UNIT",
                "VOLUMETRIC_FLOW_RATE_UNIT",
                "MASS_FLOW_RATE_UNIT",
                "ROTATIONAL_FREQUENCY_UNIT",
                "ANGULAR_VELOCITY_UNIT",
                "ANGULAR_ACCELERATION_UNIT",
                "TORQUE_UNIT",
                "LINEAR_FORCE_UNIT",
                "LINEAR_STIFFNESS_UNIT",
                "ROTATIONAL_STIFFNESS_UNIT",
                "LINEAR_MOMENT_UNIT");

        for (int i = 0; i < unitKinds.size(); i++) {
            String unitKind = unitKinds.get(i);
            assertEquals(unitKind, assertInstanceOf(StepDerivedUnit.class, resolved.get(i + 1)).unitKind());
            assertEquals(
                    unitKind.replace("_UNIT", "_MEASURE_WITH_UNIT"),
                    assertInstanceOf(StepTypedMeasureWithUnit.class, resolved.get(i + 21)).entityName());
        }
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
    void shouldResolveAnnotationFillAreaOccurrenceWithPointReplicaTarget() {
        String step = """
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
                    STYLED_ITEM('FAO0',(#14),#13)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('FAO0'));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationFillAreaOccurrence occurrence =
                assertInstanceOf(StepAnnotationFillAreaOccurrence.class, resolved.get(15));
        assertEquals("FAO0", occurrence.name());
        assertEquals(1, occurrence.styles().size());
        assertEquals(13, occurrence.item().id());
        assertEquals(7, occurrence.fillStyleTarget().id());
    }

    @Test
    void shouldResolveAnnotationFillAreaWithPathAndWireBoundaries() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationFillArea fillArea = assertInstanceOf(StepAnnotationFillArea.class, resolved.get(18));
        assertEquals(2, fillArea.boundaries().size());
        assertEquals(15, fillArea.boundaries().get(0).id());
        assertEquals(17, fillArea.boundaries().get(1).id());
    }

    @Test
    void shouldResolveAnnotationCurveAndFillAreaWithWireframeModelCarriers() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertEquals(16, assertInstanceOf(StepAnnotationCurveOccurrence.class, resolved.get(21)).item().id());
        assertEquals(19, assertInstanceOf(StepProjectionCurve.class, resolved.get(22)).item().id());
        StepAnnotationFillArea fillArea = assertInstanceOf(StepAnnotationFillArea.class, resolved.get(23));
        assertEquals(2, fillArea.boundaries().size());
        assertEquals(16, fillArea.boundaries().get(0).id());
        assertEquals(19, fillArea.boundaries().get(1).id());
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
    void shouldResolveAnnotationPlaceholderOccurrenceWithPointContainers() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationPlaceholderOccurrence pointSetOccurrence =
                assertInstanceOf(StepAnnotationPlaceholderOccurrence.class, resolved.get(5));
        StepAnnotationPlaceholderOccurrence curveSetOccurrence =
                assertInstanceOf(StepAnnotationPlaceholderOccurrence.class, resolved.get(6));
        assertEquals(2, pointSetOccurrence.item().id());
        assertEquals(3, curveSetOccurrence.item().id());
    }

    @Test
    void shouldResolvePointLikeAnnotationOccurrenceCarriers() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationTextOccurrence textOccurrence =
                assertInstanceOf(StepAnnotationTextOccurrence.class, resolved.get(4));
        StepAnnotationFillAreaOccurrence fillAreaOccurrence =
                assertInstanceOf(StepAnnotationFillAreaOccurrence.class, resolved.get(7));
        StepAnnotationPointOccurrence pointOccurrence =
                assertInstanceOf(StepAnnotationPointOccurrence.class, resolved.get(8));

        assertEquals(3, textOccurrence.position().id());
        assertEquals(4, fillAreaOccurrence.fillStyleTarget().id());
        assertEquals(7, pointOccurrence.item().id());
    }

    @Test
    void shouldResolveDirectAnnotationContentPointCarriers() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationTextOccurrence textOccurrence =
                assertInstanceOf(StepAnnotationTextOccurrence.class, resolved.get(21));
        StepAnnotationPointOccurrence pointOccurrence =
                assertInstanceOf(StepAnnotationPointOccurrence.class, resolved.get(22));
        StepAnnotationFillAreaOccurrence fillAreaOccurrence =
                assertInstanceOf(StepAnnotationFillAreaOccurrence.class, resolved.get(23));
        StepPointSet pointSet = assertInstanceOf(StepPointSet.class, resolved.get(24));
        StepAnnotationPlane annotationPlane =
                assertInstanceOf(StepAnnotationPlane.class, resolved.get(29));

        assertEquals(9, textOccurrence.position().id());
        assertEquals(13, pointOccurrence.item().id());
        assertEquals(14, fillAreaOccurrence.fillStyleTarget().id());
        assertEquals(List.of(9, 19), pointSet.points().stream().map(StepEntity::id).toList());
        assertEquals(List.of(9, 19), annotationPlane.elements().stream().map(StepEntity::id).toList());
    }

    @Test
    void shouldResolvePointSetWithPointLikeAnnotationOccurrenceCarriers() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepPointSet pointSet = assertInstanceOf(StepPointSet.class, resolved.get(10));
        assertEquals(2, pointSet.points().size());
        assertEquals(3, pointSet.points().get(0).id());
        assertEquals(9, pointSet.points().get(1).id());
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
    void shouldResolveAnnotationPlaneWithNestedPointContainers() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationPlane annotationPlane = assertInstanceOf(StepAnnotationPlane.class, resolved.get(11));
        assertEquals(2, annotationPlane.elements().size());
        assertEquals(8, annotationPlane.elements().get(0).id());
        assertEquals(9, annotationPlane.elements().get(1).id());
    }

    @Test
    void shouldResolveAnnotationPlaneWithNestedGeometricSet() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAnnotationPlane annotationPlane = assertInstanceOf(StepAnnotationPlane.class, resolved.get(12));
        assertEquals(1, annotationPlane.elements().size());
        assertEquals(10, annotationPlane.elements().getFirst().id());
    }

    @Test
    void shouldRejectPointMarkerInAnnotationPlaneElements() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('N',(0.0,0.0,1.0));
                #3=DIRECTION('X',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);
                #5=PLANE('AP',#4);
                #6=(POINT() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('PM'));
                #7=PRESENTATION_STYLE_ASSIGNMENT(());
                #8=(ANNOTATION_PLANE((#6))
                    STYLED_ITEM('AP',(#7),#5)
                    GEOMETRIC_REPRESENTATION_ITEM()
                    REPRESENTATION_ITEM('AP'));
                ENDSEC;
                """;

        UnsupportedStepEntityException exception = assertThrows(
                UnsupportedStepEntityException.class,
                () -> StepEntityResolver.resolveAll(StepParser.parse(step))
        );

        assertTrue(exception.getMessage().contains(
                "ANNOTATION_PLANE elements must reference supported point carriers or point-like annotation content/occurrences"));
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
    void shouldResolveAnnotationCurveFamilyWithPathAndWireCarriers() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertEquals(17, assertInstanceOf(StepAnnotationCurveOccurrence.class, resolved.get(23)).item().id());
        assertEquals(18, assertInstanceOf(StepLeaderCurve.class, resolved.get(24)).item().id());
        assertEquals(19, assertInstanceOf(StepDimensionCurve.class, resolved.get(25)).item().id());
        assertEquals(21, assertInstanceOf(StepProjectionCurve.class, resolved.get(26)).item().id());
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
    void shouldResolveAdditionalCurveAndEvaluatedRepresentationFamilies() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PMI');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PRESENTATION'));
                #3=CLOSED_CURVE_STYLE_PARAMETERS('CCSP',(#1),#2);
                #4=CURVE_STYLE_PARAMETERS_REPRESENTATION('CSPR',(#1),#2);
                #5=CURVE_STYLE_PARAMETERS_WITH_ENDS('CSPWE',(#1),#2);
                #6=EDGE_BASED_TOPOLOGICAL_REPRESENTATION_WITH_LENGTH_CONSTRAINT('EBTRWLC',(#1),#2);
                #7=EVALUATED_CHARACTERISTIC('EC',(#1),#2);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepRepresentation closedCurveStyle =
                assertInstanceOf(StepRepresentation.class, resolved.get(3));
        StepRepresentation curveStyleParameters =
                assertInstanceOf(StepRepresentation.class, resolved.get(4));
        StepRepresentation curveStyleParametersWithEnds =
                assertInstanceOf(StepRepresentation.class, resolved.get(5));
        StepRepresentation edgeBasedTopological =
                assertInstanceOf(StepRepresentation.class, resolved.get(6));
        StepRepresentation evaluatedCharacteristic =
                assertInstanceOf(StepRepresentation.class, resolved.get(7));
        assertEquals("CCSP", closedCurveStyle.name());
        assertEquals("CSPR", curveStyleParameters.name());
        assertEquals("CSPWE", curveStyleParametersWithEnds.name());
        assertEquals("EBTRWLC", edgeBasedTopological.name());
        assertEquals("EC", evaluatedCharacteristic.name());
        assertEquals(false, closedCurveStyle.shapeRepresentation());
        assertEquals(false, curveStyleParameters.shapeRepresentation());
        assertEquals(false, curveStyleParametersWithEnds.shapeRepresentation());
        assertEquals(false, edgeBasedTopological.shapeRepresentation());
        assertEquals(false, evaluatedCharacteristic.shapeRepresentation());
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
        assertEquals("ANALYSIS_MODEL", analysisModel.entityName());
        assertEquals("LANG", languageAssignment.name());
        assertEquals("LANGUAGE_ASSIGNMENT", languageAssignment.entityName());
        assertEquals("MSG", messageContents.name());
        assertEquals("MESSAGE_CONTENTS_ASSIGNMENT", messageContents.entityName());
        assertEquals("MTDR", machiningToolDirection.name());
        assertEquals("MACHINING_TOOL_DIRECTION_REPRESENTATION", machiningToolDirection.entityName());
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
        assertEquals("FOUNDED_KINEMATIC_PATH", foundedPath.entityName());
        assertEquals("SCBH", counterbore.name());
        assertEquals("SIMPLIFIED_COUNTERBORE_HOLE_DEFINITION", counterbore.entityName());
        assertEquals("SCDH", counterdrill.name());
        assertEquals("SIMPLIFIED_COUNTERDRILL_HOLE_DEFINITION", counterdrill.entityName());
        assertEquals("SCSH", countersink.name());
        assertEquals("SIMPLIFIED_COUNTERSINK_HOLE_DEFINITION", countersink.entityName());
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
        assertEquals("MACHINING_SPINDLE_SPEED_REPRESENTATION", spindleSpeed.entityName());
        assertEquals("MTBR", toolBody.name());
        assertEquals("MACHINING_TOOL_BODY_REPRESENTATION", toolBody.entityName());
        assertEquals("MTDR2", toolDimension.name());
        assertEquals("MACHINING_TOOL_DIMENSION_REPRESENTATION", toolDimension.entityName());
        assertEquals("MTSPR", toolpathSpeedProfile.name());
        assertEquals("MACHINING_TOOLPATH_SPEED_PROFILE_REPRESENTATION", toolpathSpeedProfile.entityName());
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
        assertEquals("FREEFORM_MILLING_TOLERANCE_REPRESENTATION", freeformTolerance.entityName());
        assertEquals("HR", hardness.name());
        assertEquals("HARDNESS_REPRESENTATION", hardness.entityName());
        assertEquals("DTT", defaultTolerance.name());
        assertEquals("DEFAULT_TOLERANCE_TABLE", defaultTolerance.entityName());
        assertEquals("OLTR", otherListTable.name());
        assertEquals("OTHER_LIST_TABLE_REPRESENTATION", otherListTable.entityName());
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
        assertEquals("CHARACTERIZED_REPRESENTATION", characterized.entityName());
        assertEquals("CIWR", characterizedItem.name());
        assertEquals("CHARACTERIZED_ITEM_WITHIN_REPRESENTATION", characterizedItem.entityName());
        assertEquals("CCBIWR", characterizedChainBasedItem.name());
        assertEquals("CHARACTERIZED_CHAIN_BASED_ITEM_WITHIN_REPRESENTATION", characterizedChainBasedItem.entityName());
        assertEquals("ECPITR", evaluatedCharacteristic.name());
        assertEquals("EVALUATED_CHARACTERISTIC_OF_PRODUCT_AS_INDIVIDUAL_TEST_RESULT", evaluatedCharacteristic.entityName());
        assertEquals(false, characterized.shapeRepresentation());
        assertEquals(false, characterizedItem.shapeRepresentation());
        assertEquals(false, characterizedChainBasedItem.shapeRepresentation());
        assertEquals(false, evaluatedCharacteristic.shapeRepresentation());
    }

    @Test
    void shouldResolvePropertyDefinitionRepresentationFamilyEntities() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PDR');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PDRCTX'));
                #3=REPRESENTATION('R',(#1),#2);
                #4=PROPERTY_DEFINITION('PD','',#1);
                #5=ABSTRACT_VARIABLE(#4,#3);
                #6=ROW_VARIABLE(#4,#3);
                #7=SCALAR_VARIABLE(#4,#3);
                #8=ATTRIBUTE_ASSERTION(#4,#3);
                #9=FORWARD_CHAINING_RULE_PREMISE(#4,#3);
                #10=BACK_CHAINING_RULE_BODY(#4,#3);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepAbstractVariable abstractVariable =
                assertInstanceOf(StepAbstractVariable.class, resolved.get(5));
        StepRowVariable rowVariable =
                assertInstanceOf(StepRowVariable.class, resolved.get(6));
        StepScalarVariable scalarVariable =
                assertInstanceOf(StepScalarVariable.class, resolved.get(7));
        StepAttributeAssertion attributeAssertion =
                assertInstanceOf(StepAttributeAssertion.class, resolved.get(8));
        StepForwardChainingRulePremise forwardChainingRulePremise =
                assertInstanceOf(StepForwardChainingRulePremise.class, resolved.get(9));
        StepBackChainingRuleBody backChainingRuleBody =
                assertInstanceOf(StepBackChainingRuleBody.class, resolved.get(10));
        assertEquals(4, abstractVariable.definition().id());
        assertEquals(3, abstractVariable.usedRepresentation().id());
        assertEquals(4, rowVariable.definition().id());
        assertEquals(3, rowVariable.usedRepresentation().id());
        assertEquals(4, scalarVariable.definition().id());
        assertEquals(3, scalarVariable.usedRepresentation().id());
        assertEquals(4, attributeAssertion.definition().id());
        assertEquals(3, attributeAssertion.usedRepresentation().id());
        assertEquals(4, forwardChainingRulePremise.definition().id());
        assertEquals(3, forwardChainingRulePremise.usedRepresentation().id());
        assertEquals(4, backChainingRuleBody.definition().id());
        assertEquals(3, backChainingRuleBody.usedRepresentation().id());
    }

    @Test
    void shouldResolveAdditionalPropertyDefinitionRepresentationFamilyEntities() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','PDR2');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','PDRCTX2'));
                #3=REPRESENTATION('R2',(#1),#2);
                #4=PROPERTY_DEFINITION('PD2','',#1);
                #5=ACTION_PROPERTY_REPRESENTATION(#4,#3);
                #6=CONTACT_RATIO_REPRESENTATION(#4,#3);
                #7=KINEMATIC_PROPERTY_DEFINITION_REPRESENTATION(#4,#3);
                #8=KINEMATIC_PROPERTY_MECHANISM_REPRESENTATION(#4,#3);
                #9=KINEMATIC_PROPERTY_REPRESENTATION_RELATION(#4,#3);
                #10=KINEMATIC_PROPERTY_TOPOLOGY_REPRESENTATION(#4,#3);
                #11=PLACED_DATUM_TARGET_FEATURE(#4,#3);
                #12=RESOURCE_PROPERTY_REPRESENTATION(#4,#3);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepActionPropertyRepresentation actionProperty =
                assertInstanceOf(StepActionPropertyRepresentation.class, resolved.get(5));
        StepContactRatioRepresentation contactRatio =
                assertInstanceOf(StepContactRatioRepresentation.class, resolved.get(6));
        StepKinematicPropertyDefinitionRepresentation kinematicDefinition =
                assertInstanceOf(StepKinematicPropertyDefinitionRepresentation.class, resolved.get(7));
        StepKinematicPropertyMechanismRepresentation kinematicMechanism =
                assertInstanceOf(StepKinematicPropertyMechanismRepresentation.class, resolved.get(8));
        StepKinematicPropertyRepresentationRelation kinematicRelation =
                assertInstanceOf(StepKinematicPropertyRepresentationRelation.class, resolved.get(9));
        StepKinematicPropertyTopologyRepresentation kinematicTopology =
                assertInstanceOf(StepKinematicPropertyTopologyRepresentation.class, resolved.get(10));
        StepPlacedDatumTargetFeature placedDatumTarget =
                assertInstanceOf(StepPlacedDatumTargetFeature.class, resolved.get(11));
        StepResourcePropertyRepresentation resourceProperty =
                assertInstanceOf(StepResourcePropertyRepresentation.class, resolved.get(12));
        assertEquals(4, actionProperty.definition().id());
        assertEquals(3, actionProperty.usedRepresentation().id());
        assertEquals(4, contactRatio.definition().id());
        assertEquals(3, contactRatio.usedRepresentation().id());
        assertEquals(4, kinematicDefinition.definition().id());
        assertEquals(3, kinematicDefinition.usedRepresentation().id());
        assertEquals(4, kinematicMechanism.definition().id());
        assertEquals(3, kinematicMechanism.usedRepresentation().id());
        assertEquals(4, kinematicRelation.definition().id());
        assertEquals(3, kinematicRelation.usedRepresentation().id());
        assertEquals(4, kinematicTopology.definition().id());
        assertEquals(3, kinematicTopology.usedRepresentation().id());
        assertEquals(4, placedDatumTarget.definition().id());
        assertEquals(3, placedDatumTarget.usedRepresentation().id());
        assertEquals(4, resourceProperty.definition().id());
        assertEquals(3, resourceProperty.usedRepresentation().id());
    }

    @Test
    void shouldResolveAdditionalRepresentationRelationshipSubtypes() {
        String step = """
                DATA;
                #1=DESCRIPTIVE_REPRESENTATION_ITEM('LABEL','REL');
                #2=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','RELCTX'));
                #3=REPRESENTATION('REP_A',(#1),#2);
                #4=REPRESENTATION('REP_B',(#1),#2);
                #5=CONSTRUCTIVE_GEOMETRY_REPRESENTATION_RELATIONSHIP('CGRR','cg',#3,#4);
                #6=DATA_EQUIVALENCE_DEFINITION_REPRESENTATION_RELATIONSHIP('DEDRR','eqdef',#3,#4);
                #7=DATA_QUALITY_DEFINITION_REPRESENTATION_RELATIONSHIP('DQDRR','dqdef',#3,#4);
                #8=DEFINITIONAL_REPRESENTATION_RELATIONSHIP('DRR','def',#3,#4);
                #9=DEFINITIONAL_REPRESENTATION_RELATIONSHIP_WITH_SAME_CONTEXT('DRRSC','defctx',#3,#4);
                #10=DRAWING_SHEET_REVISION_SEQUENCE('DSRS','sheet',#3,#4);
                #11=EXPLICIT_PROCEDURAL_REPRESENTATION_RELATIONSHIP('EPRR','proc',#3,#4);
                #12=EXPLICIT_PROCEDURAL_SHAPE_REPRESENTATION_RELATIONSHIP('EPSRR','shapeproc',#3,#4);
                #13=FACE_SHAPE_REPRESENTATION_RELATIONSHIP('FSRR','face',#3,#4);
                #14=FLAT_PATTERN_PLY_REPRESENTATION_RELATIONSHIP('FPPRR','ply',#3,#4);
                #15=MECHANICAL_DESIGN_AND_DRAUGHTING_RELATIONSHIP('MDDR','mech',#3,#4);
                #16=PAIR_REPRESENTATION_RELATIONSHIP('PRR','pair',#3,#4);
                #17=REPRESENTATION_RELATIONSHIP_WITH_CLASS('RRWC','classed',#3,#4);
                #18=SHAPE_DATA_QUALITY_INSPECTED_SHAPE_AND_RESULT_RELATIONSHIP('SDQISR','quality',#3,#4);
                #19=SHAPE_REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION('SRRWT','shapex',#3,#4);
                #20=TOPOLOGY_TO_GEOMETRY_MODEL_ASSOCIATION('TTGMA','tg',#3,#4);
                #21=GEOMETRY_TO_TOPOLOGY_MODEL_ASSOCIATION('GTTMA','gt',#3,#4);
                #22=VARIATIONAL_CURRENT_REPRESENTATION_RELATIONSHIP('VCRR','var',#3,#4);
                #23=COAXIAL_ASSEMBLY_CONSTRAINT('CAC','coax',#3,#4);
                #24=PARALLEL_ASSEMBLY_CONSTRAINT('PAC','parallel',#3,#4);
                #25=PERPENDICULAR_ASSEMBLY_CONSTRAINT('PEAC','perp',#3,#4);
                #26=INCIDENCE_ASSEMBLY_CONSTRAINT('IAC','inc',#3,#4);
                #27=TANGENT_ASSEMBLY_CONSTRAINT('TAC','tan',#3,#4);
                #28=COAXIAL_ASSEMBLY_CONSTRAINT_WITH_DIMENSION('CACD','coaxd',#3,#4);
                #29=PARALLEL_ASSEMBLY_CONSTRAINT_WITH_DIMENSION('PACD','pard',#3,#4);
                #30=PERPENDICULAR_ASSEMBLY_CONSTRAINT_WITH_DIMENSION('PEACD','perpd',#3,#4);
                #31=INCIDENCE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION('IACD','incd',#3,#4);
                #32=SURFACE_DISTANCE_ASSEMBLY_CONSTRAINT_WITH_DIMENSION('SDACD','surfd',#3,#4);
                #33=ANGULARITY_TOLERANCE_WITH_MODIFIERS('ATWM','ang',#3,#4);
                #34=REPRESENTATION_RELATIONSHIP_WITH_SAME_CONTEXT('RRSC','samectx',#3,#4);
                #35=KINEMATIC_FRAME_BACKGROUND_REPRESENTATION_RELATIONSHIP('KFBRR','framebg',#3,#4);
                #36=KINEMATIC_FRAME_REPRESENTATION_RELATIONSHIP('KFRR','frame',#3,#4);
                #37=KINEMATIC_GROUND_REPRESENTATION_RELATIONSHIP('KGRR','ground',#3,#4);
                #38=KINEMATIC_LINK_REPRESENTATION_RELATIONSHIP('KLRR','link',#3,#4);
                #39=KINEMATIC_PAIR_REPRESENTATION_RELATIONSHIP('KPRR','pair',#3,#4);
                #40=MECHANISM_REPRESENTATION_RELATIONSHIP('MRR','mechanism',#3,#4);
                #41=MECHANISM_STATE_REPRESENTATION_RELATIONSHIP('MSRR','state',#3,#4);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        for (int id = 5; id <= 41; id++) {
            StepRepresentationRelationship relationship =
                    assertInstanceOf(StepRepresentationRelationship.class, resolved.get(id));
            assertEquals(3, relationship.rep1().id());
            assertEquals(4, relationship.rep2().id());
        }
        assertEquals("CGRR", ((StepRepresentationRelationship) resolved.get(5)).name());
        assertEquals("DEDRR", ((StepRepresentationRelationship) resolved.get(6)).name());
        assertEquals("DQDRR", ((StepRepresentationRelationship) resolved.get(7)).name());
        assertEquals("DRR", ((StepRepresentationRelationship) resolved.get(8)).name());
        assertEquals("DRRSC", ((StepRepresentationRelationship) resolved.get(9)).name());
        assertEquals("DSRS", ((StepRepresentationRelationship) resolved.get(10)).name());
        assertEquals("EPRR", ((StepRepresentationRelationship) resolved.get(11)).name());
        assertEquals("EPSRR", ((StepRepresentationRelationship) resolved.get(12)).name());
        assertEquals("FSRR", ((StepRepresentationRelationship) resolved.get(13)).name());
        assertEquals("FPPRR", ((StepRepresentationRelationship) resolved.get(14)).name());
        assertEquals("MDDR", ((StepRepresentationRelationship) resolved.get(15)).name());
        assertEquals("PRR", ((StepRepresentationRelationship) resolved.get(16)).name());
        assertEquals("RRWC", ((StepRepresentationRelationship) resolved.get(17)).name());
        assertEquals("SDQISR", ((StepRepresentationRelationship) resolved.get(18)).name());
        assertEquals("SRRWT", ((StepRepresentationRelationship) resolved.get(19)).name());
        assertEquals("TTGMA", ((StepRepresentationRelationship) resolved.get(20)).name());
        assertEquals("GTTMA", ((StepRepresentationRelationship) resolved.get(21)).name());
        assertEquals("VCRR", ((StepRepresentationRelationship) resolved.get(22)).name());
        assertEquals("CAC", ((StepRepresentationRelationship) resolved.get(23)).name());
        assertEquals("PAC", ((StepRepresentationRelationship) resolved.get(24)).name());
        assertEquals("PEAC", ((StepRepresentationRelationship) resolved.get(25)).name());
        assertEquals("IAC", ((StepRepresentationRelationship) resolved.get(26)).name());
        assertEquals("TAC", ((StepRepresentationRelationship) resolved.get(27)).name());
        assertEquals("CACD", ((StepRepresentationRelationship) resolved.get(28)).name());
        assertEquals("PACD", ((StepRepresentationRelationship) resolved.get(29)).name());
        assertEquals("PEACD", ((StepRepresentationRelationship) resolved.get(30)).name());
        assertEquals("IACD", ((StepRepresentationRelationship) resolved.get(31)).name());
        assertEquals("SDACD", ((StepRepresentationRelationship) resolved.get(32)).name());
        assertEquals("ATWM", ((StepRepresentationRelationship) resolved.get(33)).name());
        assertEquals("RRSC", ((StepRepresentationRelationship) resolved.get(34)).name());
        assertEquals("KFBRR", ((StepRepresentationRelationship) resolved.get(35)).name());
        assertEquals("CONSTRUCTIVE_GEOMETRY_REPRESENTATION_RELATIONSHIP",
                ((StepRepresentationRelationship) resolved.get(5)).entityName());
        assertEquals("DEFINITIONAL_REPRESENTATION_RELATIONSHIP",
                ((StepRepresentationRelationship) resolved.get(8)).entityName());
        assertEquals("SHAPE_REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION",
                ((StepRepresentationRelationship) resolved.get(19)).entityName());
        assertEquals("COAXIAL_ASSEMBLY_CONSTRAINT",
                ((StepRepresentationRelationship) resolved.get(23)).entityName());
        assertEquals("REPRESENTATION_RELATIONSHIP_WITH_SAME_CONTEXT",
                ((StepRepresentationRelationship) resolved.get(34)).entityName());
        assertEquals("KINEMATIC_FRAME_BACKGROUND_REPRESENTATION_RELATIONSHIP",
                ((StepRepresentationRelationship) resolved.get(35)).entityName());
        assertEquals("MSRR", ((StepRepresentationRelationship) resolved.get(41)).name());
        assertEquals("MECHANISM_STATE_REPRESENTATION_RELATIONSHIP",
                ((StepRepresentationRelationship) resolved.get(41)).entityName());
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
    void shouldResolveCsgPrimitiveSolids() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=AXIS1_PLACEMENT('AX1',#1,#2);
                #6=BLOCK('BLK',#4,10.0,20.0,30.0);
                #7=SPHERE('SPH',#4,5.0);
                #8=RIGHT_CIRCULAR_CYLINDER('CYL',#5,12.0,3.0);
                #9=TORUS('TOR',#5,8.0,2.0);
                #10=ELLIPSOID('ELL',#4,2.0,3.0,4.0);
                #11=RIGHT_ANGULAR_WEDGE('WEDGE',#4,10.0,20.0,30.0,5.0);
                #12=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #13=CSG_SHAPE_REPRESENTATION('CSG',(#6,#7,#8,#9,#10,#11),#12);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepCsgPrimitive block = assertInstanceOf(StepCsgPrimitive.class, resolved.get(6));
        StepCsgPrimitive sphere = assertInstanceOf(StepCsgPrimitive.class, resolved.get(7));
        StepCsgPrimitive cylinder = assertInstanceOf(StepCsgPrimitive.class, resolved.get(8));
        StepCsgPrimitive torus = assertInstanceOf(StepCsgPrimitive.class, resolved.get(9));
        StepCsgPrimitive ellipsoid = assertInstanceOf(StepCsgPrimitive.class, resolved.get(10));
        StepCsgPrimitive wedge = assertInstanceOf(StepCsgPrimitive.class, resolved.get(11));
        assertEquals("BLOCK", block.entityName());
        assertEquals(List.of(10.0, 20.0, 30.0), block.dimensions());
        assertEquals(4, block.position().id());
        assertEquals("SPHERE", sphere.entityName());
        assertEquals(List.of(5.0), sphere.dimensions());
        assertEquals("RIGHT_CIRCULAR_CYLINDER", cylinder.entityName());
        assertEquals(List.of(12.0, 3.0), cylinder.dimensions());
        assertEquals(5, cylinder.position().id());
        assertEquals("TORUS", torus.entityName());
        assertEquals(List.of(8.0, 2.0), torus.dimensions());
        assertEquals("ELLIPSOID", ellipsoid.entityName());
        assertEquals(List.of(2.0, 3.0, 4.0), ellipsoid.dimensions());
        assertEquals("RIGHT_ANGULAR_WEDGE", wedge.entityName());
        assertEquals(List.of(10.0, 20.0, 30.0, 5.0), wedge.dimensions());

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(13));
        assertEquals(6, representation.items().size());
    }

    @Test
    void shouldResolveCsgSolidAndSolidReplica() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=DIRECTION('DY',(0.0,1.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #6=BLOCK('BLK',#5,10.0,20.0,30.0);
                #7=PLANE('PLANE',#5);
                #8=HALF_SPACE_SOLID('HS',#7,.T.);
                #9=(BOOLEAN_RESULT(.DIFFERENCE.,#6,#8) GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BOOL0'));
                #10=CSG_SOLID('CSG0',#9);
                #11=CARTESIAN_TRANSFORMATION_OPERATOR_3D('XFORM',#3,#4,#1,1.0,#2);
                #12=SOLID_REPLICA('REPLICA0',#10,#11);
                #13=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #14=CSG_SHAPE_REPRESENTATION('CSG',(#10,#12),#13);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepBooleanResult booleanResult = assertInstanceOf(StepBooleanResult.class, resolved.get(9));
        assertEquals(6, booleanResult.firstOperand().id());
        assertEquals(8, booleanResult.secondOperand().id());
        StepCsgSolid csgSolid = assertInstanceOf(StepCsgSolid.class, resolved.get(10));
        assertEquals("CSG0", csgSolid.name());
        assertEquals(9, csgSolid.treeRootExpression().id());
        StepSolidReplica solidReplica = assertInstanceOf(StepSolidReplica.class, resolved.get(12));
        assertEquals("REPLICA0", solidReplica.name());
        assertEquals(10, solidReplica.parentSolid().id());
        assertEquals(11, solidReplica.transformation().id());
        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(14));
        assertEquals(2, representation.items().size());
    }

    @Test
    void shouldResolveProfileDefsAndSweptAreaSolids() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=DIRECTION('DZ',(0.0,0.0,1.0));
                #3=DIRECTION('DX',(1.0,0.0,0.0));
                #4=AXIS2_PLACEMENT_3D('AX3',#1,#2,#3);
                #5=AXIS1_PLACEMENT('AX1',#1,#2);
                #6=AXIS2_PLACEMENT_2D('AX2',#1,#3);
                #7=CIRCLE_PROFILE_DEF(.AREA.,'circle profile',#6,2.5);
                #8=RECTANGLE_PROFILE_DEF(.AREA.,'rect profile',#6,4.0,6.0);
                #9=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #10=POLYLINE('PLINE',(#1,#9));
                #11=ARBITRARY_CLOSED_PROFILE_DEF(.AREA.,'poly profile',#10);
                #12=EXTRUDED_AREA_SOLID('EXTRUDE',#7,#4,#2,10.0);
                #13=REVOLVED_AREA_SOLID('REVOLVE',#8,#4,#5,1.57079632679);
                #14=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #15=CSG_SHAPE_REPRESENTATION('SWEPT',(#12,#13),#14);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepProfileDef circleProfile = assertInstanceOf(StepProfileDef.class, resolved.get(7));
        StepProfileDef rectangleProfile = assertInstanceOf(StepProfileDef.class, resolved.get(8));
        StepProfileDef arbitraryProfile = assertInstanceOf(StepProfileDef.class, resolved.get(11));
        assertEquals("CIRCLE_PROFILE_DEF", circleProfile.entityName());
        assertEquals("AREA", circleProfile.profileType());
        assertEquals(List.of(2.5), circleProfile.parameters());
        assertEquals("RECTANGLE_PROFILE_DEF", rectangleProfile.entityName());
        assertEquals(List.of(4.0, 6.0), rectangleProfile.parameters());
        assertEquals("ARBITRARY_CLOSED_PROFILE_DEF", arbitraryProfile.entityName());
        assertEquals(10, arbitraryProfile.curves().getFirst().id());

        StepSweptAreaSolid extruded = assertInstanceOf(StepSweptAreaSolid.class, resolved.get(12));
        StepSweptAreaSolid revolved = assertInstanceOf(StepSweptAreaSolid.class, resolved.get(13));
        assertEquals("EXTRUDED_AREA_SOLID", extruded.entityName());
        assertEquals(7, extruded.sweptArea().id());
        assertEquals(4, extruded.position().id());
        assertEquals(2, extruded.sweepReference().id());
        assertEquals(10.0, extruded.parameter());
        assertEquals("REVOLVED_AREA_SOLID", revolved.entityName());
        assertEquals(8, revolved.sweptArea().id());
        assertEquals(5, revolved.sweepReference().id());

        StepRepresentation representation = assertInstanceOf(StepRepresentation.class, resolved.get(15));
        assertEquals(2, representation.items().size());
    }

    @Test
    void shouldResolveAdditionalProfileDefsAndHalfSpaceSolids() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P1',(1.0,0.0,0.0));
                #3=DIRECTION('DZ',(0.0,0.0,1.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_2D('AX2',#1,#4);
                #6=ELLIPSE_PROFILE_DEF(.AREA.,'ellipse profile',#5,5.0,2.0);
                #7=ROUNDED_RECTANGLE_PROFILE_DEF(.AREA.,'rounded rectangle',#5,8.0,4.0,0.5);
                #8=CENTERED_RECTANGLE_PROFILE_DEF(.AREA.,'centered rectangle',#5,3.0,7.0);
                #9=CIRCULAR_HOLLOW_PROFILE_DEF(.AREA.,'hollow circle',#5,6.0,0.5);
                #10=POLYLINE('OPEN',(#1,#2));
                #11=ARBITRARY_PROFILE_DEF(.AREA.,'any profile',#10);
                #12=ARBITRARY_OPEN_PROFILE_DEF(.CURVE.,'open profile',#10);
                #13=AXIS2_PLACEMENT_3D('AX3',#1,#3,#4);
                #14=PLANE('PLANE',#13);
                #15=BOX_DOMAIN(#1,10.0,20.0,30.0);
                #16=HALF_SPACE_SOLID('HS',#14,.T.);
                #17=BOXED_HALF_SPACE('BHS',#14,.F.,#15);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepProfileDef ellipse = assertInstanceOf(StepProfileDef.class, resolved.get(6));
        assertEquals("ELLIPSE_PROFILE_DEF", ellipse.entityName());
        assertEquals(List.of(5.0, 2.0), ellipse.parameters());
        StepProfileDef roundedRectangle = assertInstanceOf(StepProfileDef.class, resolved.get(7));
        StepProfileDef centeredRectangle = assertInstanceOf(StepProfileDef.class, resolved.get(8));
        StepProfileDef circularHollow = assertInstanceOf(StepProfileDef.class, resolved.get(9));
        StepProfileDef arbitrary = assertInstanceOf(StepProfileDef.class, resolved.get(11));
        StepProfileDef arbitraryOpen = assertInstanceOf(StepProfileDef.class, resolved.get(12));
        assertEquals(10, arbitrary.curves().getFirst().id());
        assertEquals("ARBITRARY_OPEN_PROFILE_DEF", arbitraryOpen.entityName());

        StepBoxDomain boxDomain = assertInstanceOf(StepBoxDomain.class, resolved.get(15));
        assertEquals(1, boxDomain.corner().id());
        assertEquals(List.of(10.0, 20.0, 30.0), boxDomain.dimensions());

        StepHalfSpaceSolid halfSpace = assertInstanceOf(StepHalfSpaceSolid.class, resolved.get(16));
        StepHalfSpaceSolid boxedHalfSpace =
                assertInstanceOf(StepHalfSpaceSolid.class, resolved.get(17));
        assertEquals("HALF_SPACE_SOLID", halfSpace.entityName());
        assertEquals(14, halfSpace.baseSurface().id());
        assertEquals(true, halfSpace.agreementFlag());
        assertEquals(null, halfSpace.enclosure());
        assertEquals("BOXED_HALF_SPACE", boxedHalfSpace.entityName());
        assertEquals(false, boxedHalfSpace.agreementFlag());
        assertEquals(15, boxedHalfSpace.enclosure().id());
    }

    @Test
    void shouldResolveArbitraryProfileDefWithVoids() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0));
                #2=CARTESIAN_POINT('A',(4.0,0.0));
                #3=CARTESIAN_POINT('B',(4.0,4.0));
                #4=CARTESIAN_POINT('C',(0.0,4.0));
                #5=CARTESIAN_POINT('D',(1.0,1.0));
                #6=CARTESIAN_POINT('E',(3.0,1.0));
                #7=CARTESIAN_POINT('F',(3.0,3.0));
                #8=CARTESIAN_POINT('G',(1.0,3.0));
                #9=POLYLINE('OUTER',(#1,#2,#3,#4,#1));
                #10=POLYLINE('INNER',(#5,#6,#7,#8,#5));
                #11=ARBITRARY_PROFILE_DEF_WITH_VOIDS(.AREA.,'APV',#9,(#10));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepProfileDef profile = assertInstanceOf(StepProfileDef.class, resolved.get(11));
        assertEquals("ARBITRARY_PROFILE_DEF_WITH_VOIDS", profile.entityName());
        assertEquals("AREA", profile.profileType());
        assertEquals(2, profile.curves().size());
        assertEquals(9, profile.curves().get(0).id());
        assertEquals(10, profile.curves().get(1).id());
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
    void shouldResolveProductRelationship() {
        String step = """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('ASM','assembly','',(#2));
                #4=PRODUCT('PRT','part','machined part',(#2));
                #5=PRODUCT_RELATIONSHIP('PR','contains','assembly contains part',#3,#4);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepProductRelationship relationship =
                assertInstanceOf(StepProductRelationship.class, resolved.get(5));
        assertEquals("PR", relationship.identifier());
        assertEquals("contains", relationship.name());
        assertEquals("assembly contains part", relationship.description());
        assertEquals(3, relationship.relatingProduct().id());
        assertEquals(4, relationship.relatedProduct().id());
        assertEquals("PRODUCT_RELATIONSHIP", relationship.entityName());
    }

    @Test
    void shouldResolveProductDefinitionRelationshipFamilyEntities() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        for (int id = 10; id <= 15; id++) {
            StepProductDefinitionRelationship relationship =
                    assertInstanceOf(StepProductDefinitionRelationship.class, resolved.get(id));
            assertEquals(8, relationship.relatingProductDefinition().id());
            assertEquals(9, relationship.relatedProductDefinition().id());
        }
        assertEquals("PRODUCT_DEFINITION_RELATIONSHIP",
                ((StepProductDefinitionRelationship) resolved.get(10)).entityName());
        assertEquals("PRODUCT_DEFINITION_USAGE",
                ((StepProductDefinitionRelationship) resolved.get(11)).entityName());
        assertEquals("BREAKDOWN_CONTEXT",
                ((StepProductDefinitionRelationship) resolved.get(12)).entityName());
        assertEquals("BREAKDOWN_ELEMENT_USAGE",
                ((StepProductDefinitionRelationship) resolved.get(13)).entityName());
        assertEquals("BREAKDOWN_OF",
                ((StepProductDefinitionRelationship) resolved.get(14)).entityName());
        assertEquals("SUPPLIED_PART_RELATIONSHIP",
                ((StepProductDefinitionRelationship) resolved.get(15)).entityName());
        StepProductDefinitionRelationshipRelationship relationshipRelationship =
                assertInstanceOf(StepProductDefinitionRelationshipRelationship.class, resolved.get(16));
        assertEquals(10, relationshipRelationship.relating().id());
        assertEquals(11, relationshipRelationship.related().id());
        assertEquals("PRODUCT_DEFINITION_RELATIONSHIP_RELATIONSHIP",
                relationshipRelationship.entityName());
        assertEquals("PRODUCT_DEFINITION_USAGE_RELATIONSHIP",
                ((StepProductDefinitionRelationshipRelationship) resolved.get(17)).entityName());
        assertEquals("ASSEMBLY_COMPONENT_USAGE_SUBSTITUTE",
                assertInstanceOf(StepProductDefinitionRelationshipRelationship.class, resolved.get(22)).entityName());
        assertEquals("PRODUCT_DEFINITION_SUBSTITUTE",
                assertInstanceOf(StepProductDefinitionRelationshipRelationship.class, resolved.get(23)).entityName());
        StepProductDefinitionFormationRelationship formationRelationship =
                assertInstanceOf(StepProductDefinitionFormationRelationship.class, resolved.get(18));
        assertEquals(5, formationRelationship.relatingFormation().id());
        assertEquals(6, formationRelationship.relatedFormation().id());
        StepPropertyDefinitionRelationship propertyRelationship =
                assertInstanceOf(StepPropertyDefinitionRelationship.class, resolved.get(21));
        assertEquals("PROPERTY_DEFINITION_RELATIONSHIP", propertyRelationship.entityName());
        assertEquals(19, propertyRelationship.relatingPropertyDefinition().id());
        assertEquals(20, propertyRelationship.relatedPropertyDefinition().id());
    }

    @Test
    void shouldResolveGroupAndGroupRelationship() {
        String step = """
                DATA;
                #1=GROUP('inspection group','group description');
                #2=GROUP('child group',$);
                #3=GROUP_RELATIONSHIP('group rel','relates groups',#1,#2);
                #4=CLASS_SYSTEM('classification system','class system description');
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepGroup group = assertInstanceOf(StepGroup.class, resolved.get(1));
        StepGroup child = assertInstanceOf(StepGroup.class, resolved.get(2));
        StepGroupRelationship relationship =
                assertInstanceOf(StepGroupRelationship.class, resolved.get(3));
        StepGroup classSystem = assertInstanceOf(StepGroup.class, resolved.get(4));
        assertEquals("GROUP", group.entityName());
        assertEquals("", child.description());
        assertEquals(1, relationship.relatingGroup().id());
        assertEquals(2, relationship.relatedGroup().id());
        assertEquals("GROUP_RELATIONSHIP", relationship.entityName());
        assertEquals("CLASS_SYSTEM", classSystem.entityName());
    }

    @Test
    void shouldResolveDocumentPersonAndOrganizationMetadata() {
        String step = """
                DATA;
                #1=DOCUMENT_TYPE('drawing');
                #2=DOCUMENT('DOC-1','Spec','primary spec',#1);
                #3=DOCUMENT('DOC-2','Spec child',$,#1);
                #4=DOCUMENT_RELATIONSHIP('doc rel','revision link',#2,#3);
                #5=PERSON('p-1','Doe','Jane',('Q'),('Dr.'),('PE'));
                #6=PERSON('p-2',$,$,$,$,$);
                #7=ORGANIZATION('org-1','Acme','engineering');
                #8=ORGANIZATION('org-2','Supplier',$);
                #9=PERSON_AND_ORGANIZATION(#5,#7);
                #10=ORGANIZATION_RELATIONSHIP('org rel','supplier link',#7,#8);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepDocumentType type = assertInstanceOf(StepDocumentType.class, resolved.get(1));
        StepDocument document = assertInstanceOf(StepDocument.class, resolved.get(2));
        StepDocument child = assertInstanceOf(StepDocument.class, resolved.get(3));
        StepDocumentRelationship documentRelationship =
                assertInstanceOf(StepDocumentRelationship.class, resolved.get(4));
        StepPerson person = assertInstanceOf(StepPerson.class, resolved.get(5));
        StepPerson sparsePerson = assertInstanceOf(StepPerson.class, resolved.get(6));
        StepOrganization organization = assertInstanceOf(StepOrganization.class, resolved.get(7));
        StepPersonAndOrganization personAndOrganization =
                assertInstanceOf(StepPersonAndOrganization.class, resolved.get(9));
        StepOrganizationRelationship organizationRelationship =
                assertInstanceOf(StepOrganizationRelationship.class, resolved.get(10));

        assertEquals("drawing", type.productDataType());
        assertEquals(1, document.kind().id());
        assertEquals("", child.description());
        assertEquals(2, documentRelationship.relatingDocument().id());
        assertEquals(3, documentRelationship.relatedDocument().id());
        assertEquals("Jane Doe", person.name());
        assertEquals(List.of("Q"), person.middleNames());
        assertEquals(List.of("Dr."), person.prefixTitles());
        assertEquals(List.of("PE"), person.suffixTitles());
        assertEquals("", sparsePerson.name());
        assertEquals(List.of(), sparsePerson.middleNames());
        assertEquals("Acme", organization.name());
        assertEquals(5, personAndOrganization.person().id());
        assertEquals(7, personAndOrganization.organization().id());
        assertEquals(7, organizationRelationship.relatingOrganization().id());
        assertEquals(8, organizationRelationship.relatedOrganization().id());
    }

    @Test
    void shouldResolveApprovalDateTimeAndSecurityMetadata() {
        String step = """
                DATA;
                #1=PERSON('p-1','Doe','Jane',$,$,$);
                #2=ORGANIZATION('org-1','Acme','engineering');
                #3=PERSON_AND_ORGANIZATION(#1,#2);
                #4=PERSON_AND_ORGANIZATION_ROLE('creator');
                #5=CALENDAR_DATE(2026,11,4);
                #6=COORDINATED_UNIVERSAL_TIME_OFFSET(8,$,.AHEAD.);
                #7=LOCAL_TIME(9,15,30.5,#6);
                #8=DATE_AND_TIME(#5,#7);
                #9=DATE_TIME_ROLE('creation date');
                #10=DATE_TIME_ASSIGNMENT(#8,#9);
                #11=APPROVAL_STATUS('approved');
                #12=APPROVAL(#11,'design');
                #13=APPROVAL_ROLE('approver');
                #14=APPROVAL_PERSON_ORGANIZATION(#3,#12,#13);
                #15=APPROVAL_DATE_TIME(#8,#12);
                #16=SECURITY_CLASSIFICATION_LEVEL('unclassified');
                #17=SECURITY_CLASSIFICATION('sec','export control',#16);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepPersonAndOrganizationRole personRole =
                assertInstanceOf(StepPersonAndOrganizationRole.class, resolved.get(4));
        StepCalendarDate date = assertInstanceOf(StepCalendarDate.class, resolved.get(5));
        StepCoordinatedUniversalTimeOffset offset =
                assertInstanceOf(StepCoordinatedUniversalTimeOffset.class, resolved.get(6));
        StepLocalTime time = assertInstanceOf(StepLocalTime.class, resolved.get(7));
        StepDateAndTime dateAndTime = assertInstanceOf(StepDateAndTime.class, resolved.get(8));
        StepDateTimeRole dateTimeRole = assertInstanceOf(StepDateTimeRole.class, resolved.get(9));
        StepDateTimeAssignment dateTimeAssignment =
                assertInstanceOf(StepDateTimeAssignment.class, resolved.get(10));
        StepApprovalStatus status = assertInstanceOf(StepApprovalStatus.class, resolved.get(11));
        StepApproval approval = assertInstanceOf(StepApproval.class, resolved.get(12));
        StepApprovalRole approvalRole = assertInstanceOf(StepApprovalRole.class, resolved.get(13));
        StepApprovalPersonOrganization approvalPersonOrganization =
                assertInstanceOf(StepApprovalPersonOrganization.class, resolved.get(14));
        StepApprovalDateTime approvalDateTime =
                assertInstanceOf(StepApprovalDateTime.class, resolved.get(15));
        StepSecurityClassificationLevel securityLevel =
                assertInstanceOf(StepSecurityClassificationLevel.class, resolved.get(16));
        StepSecurityClassification security =
                assertInstanceOf(StepSecurityClassification.class, resolved.get(17));

        assertEquals("creator", personRole.name());
        assertEquals("2026-04-11", date.name());
        assertEquals(8, offset.hourOffset());
        assertEquals(null, offset.minuteOffset());
        assertEquals("AHEAD", offset.sense());
        assertEquals(30.5, time.secondComponent());
        assertEquals("2026-04-11 09:15:30.5", dateAndTime.name());
        assertEquals("creation date", dateTimeRole.name());
        assertEquals(8, dateTimeAssignment.assignedDateAndTime().id());
        assertEquals("approved", status.name());
        assertEquals(11, approval.status().id());
        assertEquals("design", approval.level());
        assertEquals("approver", approvalRole.name());
        assertEquals(3, approvalPersonOrganization.personOrganization().id());
        assertEquals(12, approvalPersonOrganization.authorizedApproval().id());
        assertEquals(8, approvalDateTime.dateTime().id());
        assertEquals("unclassified", securityLevel.name());
        assertEquals(16, security.securityLevel().id());
    }

    @Test
    void shouldResolveAppliedMetadataAssignments() {
        String step = """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=PERSON('p-1','Doe','Jane',$,$,$);
                #5=ORGANIZATION('org-1','Acme','engineering');
                #6=PERSON_AND_ORGANIZATION(#4,#5);
                #7=PERSON_AND_ORGANIZATION_ROLE('creator');
                #8=PERSON_AND_ORGANIZATION_ASSIGNMENT(#6,#7);
                #9=APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT(#6,#7,(#3));
                #10=CALENDAR_DATE(2026,11,4);
                #11=COORDINATED_UNIVERSAL_TIME_OFFSET(8,30,.AHEAD.);
                #12=LOCAL_TIME(9,15,$,#11);
                #13=DATE_AND_TIME(#10,#12);
                #14=DATE_TIME_ROLE('created');
                #15=APPLIED_DATE_AND_TIME_ASSIGNMENT(#13,#14,(#3,#6));
                #16=APPROVAL_STATUS('approved');
                #17=APPROVAL(#16,'design');
                #18=APPROVAL_ASSIGNMENT(#17);
                #19=APPLIED_APPROVAL_ASSIGNMENT(#17,(#3));
                #20=SECURITY_CLASSIFICATION_LEVEL('unclassified');
                #21=SECURITY_CLASSIFICATION('sec','export control',#20);
                #22=SECURITY_CLASSIFICATION_ASSIGNMENT(#21);
                #23=APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT(#21,(#3));
                #24=DOCUMENT_TYPE('specification');
                #25=DOCUMENT('DOC-1','Spec','primary spec',#24);
                #26=DOCUMENT_REFERENCE(#25,'internal');
                #27=APPLIED_DOCUMENT_REFERENCE(#25,'internal',(#3));
                #28=CONTRACT_TYPE('purchase');
                #29=CONTRACT('C-1','supply',#28);
                #30=CONTRACT_ASSIGNMENT(#29);
                #31=APPLIED_CONTRACT_ASSIGNMENT(#29,(#3));
                #32=CERTIFICATION_TYPE('material');
                #33=CERTIFICATION('CERT-1','compliance',#32);
                #34=CERTIFICATION_ASSIGNMENT(#33);
                #35=APPLIED_CERTIFICATION_ASSIGNMENT(#33,(#3));
                #36=DATE_ROLE('release');
                #37=DATE_ASSIGNMENT(#10,#36);
                #38=APPLIED_DATE_ASSIGNMENT(#10,#36,(#3));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepPersonAndOrganizationAssignment personAssignment =
                assertInstanceOf(StepPersonAndOrganizationAssignment.class, resolved.get(8));
        StepAppliedPersonAndOrganizationAssignment appliedPersonAssignment =
                assertInstanceOf(StepAppliedPersonAndOrganizationAssignment.class, resolved.get(9));
        StepAppliedDateTimeAssignment appliedDateTime =
                assertInstanceOf(StepAppliedDateTimeAssignment.class, resolved.get(15));
        StepApprovalAssignment approvalAssignment =
                assertInstanceOf(StepApprovalAssignment.class, resolved.get(18));
        StepAppliedApprovalAssignment appliedApproval =
                assertInstanceOf(StepAppliedApprovalAssignment.class, resolved.get(19));
        StepSecurityClassificationAssignment securityAssignment =
                assertInstanceOf(StepSecurityClassificationAssignment.class, resolved.get(22));
        StepAppliedSecurityClassificationAssignment appliedSecurity =
                assertInstanceOf(StepAppliedSecurityClassificationAssignment.class, resolved.get(23));
        StepDocumentReference documentReference =
                assertInstanceOf(StepDocumentReference.class, resolved.get(26));
        StepAppliedDocumentReference appliedDocument =
                assertInstanceOf(StepAppliedDocumentReference.class, resolved.get(27));
        StepContractType contractType = assertInstanceOf(StepContractType.class, resolved.get(28));
        StepContract contract = assertInstanceOf(StepContract.class, resolved.get(29));
        StepContractAssignment contractAssignment =
                assertInstanceOf(StepContractAssignment.class, resolved.get(30));
        StepAppliedContractAssignment appliedContract =
                assertInstanceOf(StepAppliedContractAssignment.class, resolved.get(31));
        StepCertificationType certificationType =
                assertInstanceOf(StepCertificationType.class, resolved.get(32));
        StepCertification certification = assertInstanceOf(StepCertification.class, resolved.get(33));
        StepCertificationAssignment certificationAssignment =
                assertInstanceOf(StepCertificationAssignment.class, resolved.get(34));
        StepAppliedCertificationAssignment appliedCertification =
                assertInstanceOf(StepAppliedCertificationAssignment.class, resolved.get(35));
        StepDateRole dateRole = assertInstanceOf(StepDateRole.class, resolved.get(36));
        StepDateAssignment dateAssignment =
                assertInstanceOf(StepDateAssignment.class, resolved.get(37));
        StepAppliedDateAssignment appliedDate =
                assertInstanceOf(StepAppliedDateAssignment.class, resolved.get(38));

        assertEquals(6, personAssignment.assignedPersonAndOrganization().id());
        assertEquals(7, personAssignment.role().id());
        assertEquals(1, appliedPersonAssignment.items().size());
        assertEquals("APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT", appliedPersonAssignment.entityName());
        assertEquals(3, appliedPersonAssignment.items().getFirst().id());
        assertEquals(2, appliedDateTime.items().size());
        assertEquals("APPLIED_DATE_AND_TIME_ASSIGNMENT", appliedDateTime.entityName());
        assertEquals(13, appliedDateTime.assignedDateAndTime().id());
        assertEquals(17, approvalAssignment.assignedApproval().id());
        assertEquals("APPLIED_APPROVAL_ASSIGNMENT", appliedApproval.entityName());
        assertEquals(3, appliedApproval.items().getFirst().id());
        assertEquals(21, securityAssignment.assignedSecurityClassification().id());
        assertEquals("APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT", appliedSecurity.entityName());
        assertEquals(3, appliedSecurity.items().getFirst().id());
        assertEquals(25, documentReference.assignedDocument().id());
        assertEquals("APPLIED_DOCUMENT_REFERENCE", appliedDocument.entityName());
        assertEquals("internal", appliedDocument.source());
        assertEquals(3, appliedDocument.items().getFirst().id());
        assertEquals("purchase", contractType.description());
        assertEquals(28, contract.kind().id());
        assertEquals(29, contractAssignment.assignedContract().id());
        assertEquals("APPLIED_CONTRACT_ASSIGNMENT", appliedContract.entityName());
        assertEquals(3, appliedContract.items().getFirst().id());
        assertEquals("material", certificationType.description());
        assertEquals(32, certification.kind().id());
        assertEquals(33, certificationAssignment.assignedCertification().id());
        assertEquals("APPLIED_CERTIFICATION_ASSIGNMENT", appliedCertification.entityName());
        assertEquals(3, appliedCertification.items().getFirst().id());
        assertEquals("release", dateRole.name());
        assertEquals(10, dateAssignment.assignedDate().id());
        assertEquals(36, dateAssignment.role().id());
        assertEquals("APPLIED_DATE_ASSIGNMENT", appliedDate.entityName());
        assertEquals(3, appliedDate.items().getFirst().id());
    }

    @Test
    void shouldResolveCommonControlDesignAliases() {
        String step = """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=MECHANICAL_CONTEXT('part',#1,'mechanical');
                #3=PRODUCT('PRT','part','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','first',#3);
                #5=DESIGN_CONTEXT('design',#1,'design');
                #6=PRODUCT_DEFINITION('def','part def',#4,#5);
                #7=PERSON('p-1','Doe','Jane',$,$,$);
                #8=ORGANIZATION('org-1','Acme','engineering');
                #9=PERSON_AND_ORGANIZATION(#7,#8);
                #10=PERSON_AND_ORGANIZATION_ROLE('creator');
                #11=CC_DESIGN_PERSON_AND_ORGANIZATION_ASSIGNMENT(#9,#10,(#3,#6));
                #12=CALENDAR_DATE(2026,11,4);
                #13=DATE_ROLE('released');
                #14=CC_DESIGN_DATE_ASSIGNMENT(#12,#13,(#6));
                #15=COORDINATED_UNIVERSAL_TIME_OFFSET(8,$,.AHEAD.);
                #16=LOCAL_TIME(9,15,30.0,#15);
                #17=DATE_AND_TIME(#12,#16);
                #18=DATE_TIME_ROLE('created');
                #19=CC_DESIGN_DATE_AND_TIME_ASSIGNMENT(#17,#18,(#6));
                #20=APPLIED_DATE_TIME_ASSIGNMENT(#17,#18,(#3));
                #21=APPROVAL_STATUS('approved');
                #22=APPROVAL(#21,'design');
                #23=CC_DESIGN_APPROVAL(#22,(#6));
                #24=SECURITY_CLASSIFICATION_LEVEL('unclassified');
                #25=SECURITY_CLASSIFICATION('sec','export control',#24);
                #26=CC_DESIGN_SECURITY_CLASSIFICATION(#25,(#6));
                #27=CONTRACT_TYPE('purchase');
                #28=CONTRACT('C-1','supply',#27);
                #29=CC_DESIGN_CONTRACT(#28,(#6));
                #30=CERTIFICATION_TYPE('material');
                #31=CERTIFICATION('CERT-1','compliance',#30);
                #32=CC_DESIGN_CERTIFICATION(#31,(#6));
                #33=DOCUMENT_TYPE('specification');
                #34=DOCUMENT('DOC-1','Spec','primary spec',#33);
                #35=CC_DESIGN_SPECIFICATION_REFERENCE(#34,'internal',(#4,#6));
                #36=CLASS('fixture','classification');
                #37=ASSEMBLY_COMPONENT_USAGE('acu','assembly usage','',#6,#6);
                #38=PROMISSORY_USAGE_OCCURRENCE('puo','promised usage','',#6,#6);
                #39=ORGANIZATION_ROLE('design supplier');
                #40=CC_DESIGN_ORGANIZATION_ASSIGNMENT(#8,#39,(#3,#6));
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepProductContext mechanicalContext = assertInstanceOf(StepProductContext.class, resolved.get(2));
        StepProductDefinitionContext designContext =
                assertInstanceOf(StepProductDefinitionContext.class, resolved.get(5));
        StepAppliedPersonAndOrganizationAssignment personAssignment =
                assertInstanceOf(StepAppliedPersonAndOrganizationAssignment.class, resolved.get(11));
        StepAppliedDateAssignment dateAssignment =
                assertInstanceOf(StepAppliedDateAssignment.class, resolved.get(14));
        StepAppliedDateTimeAssignment dateTimeAssignment =
                assertInstanceOf(StepAppliedDateTimeAssignment.class, resolved.get(19));
        StepAppliedDateTimeAssignment compactDateTimeAssignment =
                assertInstanceOf(StepAppliedDateTimeAssignment.class, resolved.get(20));
        StepAppliedApprovalAssignment approvalAssignment =
                assertInstanceOf(StepAppliedApprovalAssignment.class, resolved.get(23));
        StepAppliedSecurityClassificationAssignment securityAssignment =
                assertInstanceOf(StepAppliedSecurityClassificationAssignment.class, resolved.get(26));
        StepAppliedContractAssignment contractAssignment =
                assertInstanceOf(StepAppliedContractAssignment.class, resolved.get(29));
        StepAppliedCertificationAssignment certificationAssignment =
                assertInstanceOf(StepAppliedCertificationAssignment.class, resolved.get(32));
        StepAppliedDocumentReference specificationReference =
                assertInstanceOf(StepAppliedDocumentReference.class, resolved.get(35));
        StepGroup classGroup = assertInstanceOf(StepGroup.class, resolved.get(36));
        StepProductDefinitionRelationship assemblyUsage =
                assertInstanceOf(StepProductDefinitionRelationship.class, resolved.get(37));
        StepProductDefinitionRelationship promissoryUsage =
                assertInstanceOf(StepProductDefinitionRelationship.class, resolved.get(38));
        StepAppliedOrganizationAssignment organizationAssignment =
                assertInstanceOf(StepAppliedOrganizationAssignment.class, resolved.get(40));

        assertEquals("MECHANICAL_CONTEXT", mechanicalContext.entityName());
        assertEquals("mechanical", mechanicalContext.disciplineType());
        assertEquals("DESIGN_CONTEXT", designContext.entityName());
        assertEquals("design", designContext.lifeCycleStage());
        assertEquals("CC_DESIGN_PERSON_AND_ORGANIZATION_ASSIGNMENT", personAssignment.entityName());
        assertEquals(2, personAssignment.items().size());
        assertEquals("CC_DESIGN_DATE_ASSIGNMENT", dateAssignment.entityName());
        assertEquals(6, dateAssignment.items().getFirst().id());
        assertEquals("CC_DESIGN_DATE_AND_TIME_ASSIGNMENT", dateTimeAssignment.entityName());
        assertEquals(17, dateTimeAssignment.assignedDateAndTime().id());
        assertEquals("APPLIED_DATE_TIME_ASSIGNMENT", compactDateTimeAssignment.entityName());
        assertEquals(3, compactDateTimeAssignment.items().getFirst().id());
        assertEquals("CC_DESIGN_APPROVAL", approvalAssignment.entityName());
        assertEquals(22, approvalAssignment.assignedApproval().id());
        assertEquals("CC_DESIGN_SECURITY_CLASSIFICATION", securityAssignment.entityName());
        assertEquals(25, securityAssignment.assignedSecurityClassification().id());
        assertEquals("CC_DESIGN_CONTRACT", contractAssignment.entityName());
        assertEquals(28, contractAssignment.assignedContract().id());
        assertEquals("CC_DESIGN_CERTIFICATION", certificationAssignment.entityName());
        assertEquals(31, certificationAssignment.assignedCertification().id());
        assertEquals("CC_DESIGN_SPECIFICATION_REFERENCE", specificationReference.entityName());
        assertEquals(2, specificationReference.items().size());
        assertEquals("CLASS", classGroup.entityName());
        assertEquals("ASSEMBLY_COMPONENT_USAGE", assemblyUsage.entityName());
        assertEquals("PROMISSORY_USAGE_OCCURRENCE", promissoryUsage.entityName());
        assertEquals("CC_DESIGN_ORGANIZATION_ASSIGNMENT", organizationAssignment.entityName());
        assertEquals(2, organizationAssignment.items().size());
        assertEquals(8, organizationAssignment.assignedOrganization().id());
    }

    @Test
    void shouldResolveEffectivityMetadata() {
        String step = """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical');
                #2=PRODUCT_CONTEXT('part','',#1);
                #3=PRODUCT('PRT','part','',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','first',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('def','part def',#4,#5);
                #7=EFFECTIVITY('E-1');
                #8=PRODUCT_DEFINITION_EFFECTIVITY('PDE-1','serial usage',#6);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepEffectivity effectivity = assertInstanceOf(StepEffectivity.class, resolved.get(7));
        StepProductDefinitionEffectivity productDefinitionEffectivity =
                assertInstanceOf(StepProductDefinitionEffectivity.class, resolved.get(8));
        assertEquals("E-1", effectivity.effectivityId());
        assertEquals("PDE-1", productDefinitionEffectivity.effectivityId());
        assertEquals("serial usage", productDefinitionEffectivity.usage());
        assertEquals(6, productDefinitionEffectivity.productDefinition().id());
    }

    @Test
    void shouldResolveClassificationIdentificationAndExternalMetadata() {
        String step = """
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
                #13=DESCRIPTION_ATTRIBUTE('attribute description',#3);
                #14=NAME_ATTRIBUTE('attribute name',#3);
                #15=ID_ATTRIBUTE('attribute-id',#3);
                #16=EXTERNAL_SOURCE('supplier-catalog');
                #17=EXTERNALLY_DEFINED_ITEM('item-1',#16);
                #18=EXTERNALLY_DEFINED_CLASS('class-1',#16);
                #19=EXTERNALLY_DEFINED_GENERAL_PROPERTY('property-1',#16);
                #20=GROUP_ASSIGNMENT(#4);
                #21=APPLIED_GROUP_ASSIGNMENT(#4,(#3));
                #22=ORGANIZATION('org-1','Acme','supplier');
                #23=ORGANIZATION_ROLE('supplier');
                #24=ORGANIZATION_ASSIGNMENT(#22,#23);
                #25=APPLIED_ORGANIZATION_ASSIGNMENT(#22,#23,(#3));
                #26=ADDRESS('HQ','42','Market St',$,'Shanghai','Shanghai','200000','CN',$,'+86','cad@example.com',$);
                #27=GENERAL_PROPERTY('gp-1','material','material property');
                #28=GENERAL_PROPERTY('gp-2','finish','finish property');
                #29=GENERAL_PROPERTY_RELATIONSHIP('property rel','linked property',#27,#28);
                #30=LANGUAGE('en-US');
                #31=LANGUAGE_ASSIGNMENT(#30);
                #32=APPLIED_LANGUAGE_ASSIGNMENT(#30,(#3));
                #33=EXTERNAL_SOURCE('erp');
                #34=EXTERNAL_SOURCE_RELATIONSHIP('source rel','catalog to erp',#16,#33);
                #35=EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT-42',#8,#33);
                #36=APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT('EXT-42',#8,#33,(#3));
                #37=PRODUCT_CATEGORY('hardware','hardware parts');
                #38=PRODUCT_CATEGORY('fastener','fastener parts');
                #39=PRODUCT_CATEGORY_RELATIONSHIP('category rel','parent child',#37,#38);
                #40=DOCUMENT_TYPE('spec');
                #41=DOCUMENT('DOC-1','datasheet','supplier document',#40);
                #42=DOCUMENT_USAGE_CONSTRAINT(#41,'scope','assembly only');
                #43=EFFECTIVITY('E-2');
                #44=EFFECTIVITY('E-3');
                #45=EFFECTIVITY_RELATIONSHIP('effectivity rel','range chain',#43,#44);
                #46=EXTERNALLY_DEFINED_CURVE_FONT('font-1',#16);
                #47=EXTERNALLY_DEFINED_HATCH_STYLE('hatch-1',#16);
                #48=EXTERNALLY_DEFINED_MARKER('marker-1',#16);
                #49=EXTERNALLY_DEFINED_SYMBOL('symbol-1',#16);
                #50=EXTERNALLY_DEFINED_TEXT_FONT('text-font-1',#16);
                #51=EXTERNALLY_DEFINED_CHARACTER_GLYPH('glyph-1',#16);
                #52=EXTERNALLY_DEFINED_DIMENSION_DEFINITION('dimension-1',#16);
                #53=EXTERNALLY_DEFINED_PICTURE_REPRESENTATION_ITEM('picture-1',#16);
                #54=EXTERNALLY_DEFINED_STYLE('style-1',#16);
                #55=EXTERNALLY_DEFINED_TERMINATOR_SYMBOL('terminator-1',#16);
                #56=EXTERNALLY_DEFINED_TEXT_STYLE('text-style-1',#16);
                #57=EXTERNALLY_DEFINED_TILE('tile-1',#16);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepClassificationRole classificationRole =
                assertInstanceOf(StepClassificationRole.class, resolved.get(5));
        StepClassificationAssignment classificationAssignment =
                assertInstanceOf(StepClassificationAssignment.class, resolved.get(6));
        StepAppliedClassificationAssignment appliedClassification =
                assertInstanceOf(StepAppliedClassificationAssignment.class, resolved.get(7));
        StepIdentificationRole identificationRole =
                assertInstanceOf(StepIdentificationRole.class, resolved.get(8));
        StepIdentificationAssignment identificationAssignment =
                assertInstanceOf(StepIdentificationAssignment.class, resolved.get(9));
        StepAppliedIdentificationAssignment appliedIdentification =
                assertInstanceOf(StepAppliedIdentificationAssignment.class, resolved.get(10));
        StepNameAssignment nameAssignment =
                assertInstanceOf(StepNameAssignment.class, resolved.get(11));
        StepAppliedNameAssignment appliedName =
                assertInstanceOf(StepAppliedNameAssignment.class, resolved.get(12));
        StepDescriptionAttribute descriptionAttribute =
                assertInstanceOf(StepDescriptionAttribute.class, resolved.get(13));
        StepNameAttribute nameAttribute = assertInstanceOf(StepNameAttribute.class, resolved.get(14));
        StepIdAttribute idAttribute = assertInstanceOf(StepIdAttribute.class, resolved.get(15));
        StepExternalSource externalSource =
                assertInstanceOf(StepExternalSource.class, resolved.get(16));
        StepExternallyDefinedItem externallyDefinedItem =
                assertInstanceOf(StepExternallyDefinedItem.class, resolved.get(17));
        StepExternallyDefinedItem externallyDefinedClass =
                assertInstanceOf(StepExternallyDefinedItem.class, resolved.get(18));
        StepExternallyDefinedItem externallyDefinedGeneralProperty =
                assertInstanceOf(StepExternallyDefinedItem.class, resolved.get(19));
        StepGroupAssignment groupAssignment =
                assertInstanceOf(StepGroupAssignment.class, resolved.get(20));
        StepAppliedGroupAssignment appliedGroup =
                assertInstanceOf(StepAppliedGroupAssignment.class, resolved.get(21));
        StepOrganizationRole organizationRole =
                assertInstanceOf(StepOrganizationRole.class, resolved.get(23));
        StepOrganizationAssignment organizationAssignment =
                assertInstanceOf(StepOrganizationAssignment.class, resolved.get(24));
        StepAppliedOrganizationAssignment appliedOrganization =
                assertInstanceOf(StepAppliedOrganizationAssignment.class, resolved.get(25));
        StepAddress address = assertInstanceOf(StepAddress.class, resolved.get(26));
        StepGeneralProperty generalProperty =
                assertInstanceOf(StepGeneralProperty.class, resolved.get(27));
        StepGeneralPropertyRelationship generalPropertyRelationship =
                assertInstanceOf(StepGeneralPropertyRelationship.class, resolved.get(29));
        StepLanguage language = assertInstanceOf(StepLanguage.class, resolved.get(30));
        StepLanguageAssignment languageAssignment =
                assertInstanceOf(StepLanguageAssignment.class, resolved.get(31));
        StepAppliedLanguageAssignment appliedLanguage =
                assertInstanceOf(StepAppliedLanguageAssignment.class, resolved.get(32));
        StepExternalSourceRelationship externalSourceRelationship =
                assertInstanceOf(StepExternalSourceRelationship.class, resolved.get(34));
        StepExternalIdentificationAssignment externalIdentification =
                assertInstanceOf(StepExternalIdentificationAssignment.class, resolved.get(35));
        StepAppliedExternalIdentificationAssignment appliedExternalIdentification =
                assertInstanceOf(StepAppliedExternalIdentificationAssignment.class, resolved.get(36));
        StepProductCategory productCategory =
                assertInstanceOf(StepProductCategory.class, resolved.get(37));
        StepProductCategoryRelationship productCategoryRelationship =
                assertInstanceOf(StepProductCategoryRelationship.class, resolved.get(39));
        StepDocumentUsageConstraint documentUsageConstraint =
                assertInstanceOf(StepDocumentUsageConstraint.class, resolved.get(42));
        StepEffectivityRelationship effectivityRelationship =
                assertInstanceOf(StepEffectivityRelationship.class, resolved.get(45));
        StepExternallyDefinedItem externallyDefinedCurveFont =
                assertInstanceOf(StepExternallyDefinedItem.class, resolved.get(46));
        StepExternallyDefinedItem externallyDefinedHatchStyle =
                assertInstanceOf(StepExternallyDefinedItem.class, resolved.get(47));
        StepExternallyDefinedItem externallyDefinedMarker =
                assertInstanceOf(StepExternallyDefinedItem.class, resolved.get(48));
        StepExternallyDefinedItem externallyDefinedSymbol =
                assertInstanceOf(StepExternallyDefinedItem.class, resolved.get(49));
        StepExternallyDefinedItem externallyDefinedTextFont =
                assertInstanceOf(StepExternallyDefinedItem.class, resolved.get(50));
        StepExternallyDefinedItem externallyDefinedCharacterGlyph =
                assertInstanceOf(StepExternallyDefinedItem.class, resolved.get(51));
        StepExternallyDefinedItem externallyDefinedDimensionDefinition =
                assertInstanceOf(StepExternallyDefinedItem.class, resolved.get(52));
        StepExternallyDefinedItem externallyDefinedPictureRepresentationItem =
                assertInstanceOf(StepExternallyDefinedItem.class, resolved.get(53));
        StepExternallyDefinedItem externallyDefinedStyle =
                assertInstanceOf(StepExternallyDefinedItem.class, resolved.get(54));
        StepExternallyDefinedItem externallyDefinedTerminatorSymbol =
                assertInstanceOf(StepExternallyDefinedItem.class, resolved.get(55));
        StepExternallyDefinedItem externallyDefinedTextStyle =
                assertInstanceOf(StepExternallyDefinedItem.class, resolved.get(56));
        StepExternallyDefinedItem externallyDefinedTile =
                assertInstanceOf(StepExternallyDefinedItem.class, resolved.get(57));

        assertEquals("part family", classificationRole.name());
        assertEquals(4, classificationAssignment.assignedClass().id());
        assertEquals(5, classificationAssignment.role().id());
        assertEquals(3, appliedClassification.items().getFirst().id());
        assertEquals("erp id", identificationRole.name());
        assertEquals("ERP-42", identificationAssignment.assignedId());
        assertEquals(3, appliedIdentification.items().getFirst().id());
        assertEquals("display name", nameAssignment.assignedName());
        assertEquals(3, appliedName.items().getFirst().id());
        assertEquals(3, descriptionAttribute.describedItem().id());
        assertEquals(3, nameAttribute.namedItem().id());
        assertEquals(3, idAttribute.identifiedItem().id());
        assertEquals("supplier-catalog", externalSource.sourceId());
        assertEquals("EXTERNALLY_DEFINED_ITEM", externallyDefinedItem.entityName());
        assertEquals("EXTERNALLY_DEFINED_CLASS", externallyDefinedClass.entityName());
        assertEquals("EXTERNALLY_DEFINED_GENERAL_PROPERTY",
                externallyDefinedGeneralProperty.entityName());
        assertEquals(16, externallyDefinedGeneralProperty.source().id());
        assertEquals(4, groupAssignment.assignedGroup().id());
        assertEquals(3, appliedGroup.items().getFirst().id());
        assertEquals("supplier", organizationRole.name());
        assertEquals(22, organizationAssignment.assignedOrganization().id());
        assertEquals(23, organizationAssignment.role().id());
        assertEquals(3, appliedOrganization.items().getFirst().id());
        assertEquals("Shanghai", address.town());
        assertEquals("cad@example.com", address.electronicMailAddress());
        assertEquals("material", generalProperty.name());
        assertEquals(27, generalPropertyRelationship.relatingGeneralProperty().id());
        assertEquals(28, generalPropertyRelationship.relatedGeneralProperty().id());
        assertEquals("en-US", language.name());
        assertEquals(30, languageAssignment.assignedLanguage().id());
        assertEquals(3, appliedLanguage.items().getFirst().id());
        assertEquals(16, externalSourceRelationship.relatingSource().id());
        assertEquals(33, externalSourceRelationship.relatedSource().id());
        assertEquals("EXT-42", externalIdentification.assignedId());
        assertEquals(33, externalIdentification.source().id());
        assertEquals(3, appliedExternalIdentification.items().getFirst().id());
        assertEquals("hardware", productCategory.name());
        assertEquals(37, productCategoryRelationship.category().id());
        assertEquals(38, productCategoryRelationship.subCategory().id());
        assertEquals(41, documentUsageConstraint.source().id());
        assertEquals("assembly only", documentUsageConstraint.subjectElementValue());
        assertEquals(43, effectivityRelationship.relatingEffectivity().id());
        assertEquals(44, effectivityRelationship.relatedEffectivity().id());
        assertEquals("EXTERNALLY_DEFINED_CURVE_FONT", externallyDefinedCurveFont.entityName());
        assertEquals("EXTERNALLY_DEFINED_HATCH_STYLE", externallyDefinedHatchStyle.entityName());
        assertEquals("EXTERNALLY_DEFINED_MARKER", externallyDefinedMarker.entityName());
        assertEquals("EXTERNALLY_DEFINED_SYMBOL", externallyDefinedSymbol.entityName());
        assertEquals("EXTERNALLY_DEFINED_TEXT_FONT", externallyDefinedTextFont.entityName());
        assertEquals("EXTERNALLY_DEFINED_CHARACTER_GLYPH",
                externallyDefinedCharacterGlyph.entityName());
        assertEquals("EXTERNALLY_DEFINED_DIMENSION_DEFINITION",
                externallyDefinedDimensionDefinition.entityName());
        assertEquals("EXTERNALLY_DEFINED_PICTURE_REPRESENTATION_ITEM",
                externallyDefinedPictureRepresentationItem.entityName());
        assertEquals("EXTERNALLY_DEFINED_STYLE", externallyDefinedStyle.entityName());
        assertEquals("EXTERNALLY_DEFINED_TERMINATOR_SYMBOL",
                externallyDefinedTerminatorSymbol.entityName());
        assertEquals("EXTERNALLY_DEFINED_TEXT_STYLE", externallyDefinedTextStyle.entityName());
        assertEquals("EXTERNALLY_DEFINED_TILE", externallyDefinedTile.entityName());
    }

    @Test
    void shouldResolveShapeAspectFamilyEntities() {
        String step = """
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
                #19=INSTANCED_FEATURE('IF','instanced feature',#7,.T.);
                #20=INSTANCED_SHAPE_ASPECT('ISA','instanced aspect',#7,.T.);
                #21=SINGULAR_SHAPE_ASPECT('SSA','singular',#7,.T.);
                #22=SYMMETRIC_SHAPE_ASPECT('SYSA','symmetric',#7,.T.);
                #23=ALL_AROUND_SHAPE_ASPECT('AASA','all around',#7,.T.);
                #24=BETWEEN_SHAPE_ASPECT('BSA','between',#7,.T.);
                #25=CHAMFER('CH','chamfer',#7,.T.);
                #26=CHAMFER_OFFSET('CHO','chamfer offset',#7,.T.);
                #27=COMPOSITE_GROUP_SHAPE_ASPECT('CGSA','composite group',#7,.T.);
                #28=COMPOSITE_UNIT_SHAPE_ASPECT('CUSA','composite unit',#7,.T.);
                #29=CONTINUOUS_SHAPE_ASPECT('CONSA','continuous',#7,.T.);
                #30=EDGE_ROUND('ER','edge round',#7,.T.);
                #31=EXTENSION('EXT','extension',#7,.T.);
                #32=FILLET('FIL','fillet',#7,.T.);
                #33=GEOMETRIC_INTERSECTION('GI','intersection',#7,.T.);
                #34=PARALLEL_OFFSET('PO','parallel offset',#7,.T.);
                #35=PERPENDICULAR_TO('PT','perpendicular',#7,.T.);
                #36=TANGENT('TAN','tangent',#7,.T.);
                #37=SHAPE_ASPECT_RELATIONSHIP('SAR','base rel',#8,#9);
                #38=COMPOSITE_SHAPE_ASPECT_RELATIONSHIP('CSAR','composite rel',#12,#8);
                #39=DIMENSIONAL_LOCATION('DL','location',#8,#10);
                #40=DIMENSIONAL_SIZE('DS','size',#8,#11);
                #41=FEATURE_COMPONENT_RELATIONSHIP('FCR','feature',#11,#12);
                #42=GEOMETRIC_ALIGNMENT_RELATIONSHIP('GAR','align rel',#16,#8);
                #43=GEOMETRIC_CONTACT_RELATIONSHIP('GCR','contact rel',#17,#8);
                #44=SHAPE_ASPECT_ASSOCIATIVITY('SAA','assoc',#18,#8);
                #45=SHAPE_ASPECT_DERIVING_RELATIONSHIP('SADR','derive',#22,#8);
                #46=ANGULAR_LOCATION('AL','angular',#8,#9);
                #47=DIRECTED_DIMENSIONAL_LOCATION('DDL','directed',#8,#9);
                #48=FEATURE_FOR_DATUM_TARGET_RELATIONSHIP('FFDTR','feature target',#14,#15);
                #49=MAKE_FROM_FEATURE_RELATIONSHIP('MFFR','make from',#11,#12);
                #50=PATTERN_OFFSET_MEMBERSHIP('POM','offset',#18,#8);
                #51=PATTERN_OMIT_MEMBERSHIP('POMIT','omit',#18,#8);
                #52=SHAPE_ASPECT_TRANSITION('SAT','transition',#8,#9);
                #53=SHAPE_DEFINING_RELATIONSHIP('SDR','shape defining',#8,#9);
                #54=SHAPE_FEATURE_FIT_RELATIONSHIP('SFFR','fit',#11,#12);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        // IDs 8-12, 16-36 are shape aspect subtypes; 13-15 are DATUM types with own resolvers
        for (int id = 8; id <= 12; id++) {
            StepShapeAspect aspect = assertInstanceOf(StepShapeAspect.class, resolved.get(id));
            assertEquals(7, aspect.ofShape().id());
        }
        for (int id = 16; id <= 36; id++) {
            StepShapeAspect aspect = assertInstanceOf(StepShapeAspect.class, resolved.get(id));
            assertEquals(7, aspect.ofShape().id());
        }
        // Verify DATUM types have their own resolvers
        assertInstanceOf(StepDatum.class, resolved.get(13));
        assertInstanceOf(StepDatumFeature.class, resolved.get(14));
        assertInstanceOf(StepDatumTarget.class, resolved.get(15));
        assertEquals("SHAPE_ASPECT", ((StepShapeAspect) resolved.get(8)).entityName());
        assertEquals("APEX", ((StepShapeAspect) resolved.get(9)).entityName());
        assertEquals("CENTRE_OF_SYMMETRY", ((StepShapeAspect) resolved.get(10)).entityName());
        assertEquals("T", ((StepShapeAspect) resolved.get(8)).productDefinitional());
        assertEquals("F", ((StepShapeAspect) resolved.get(9)).productDefinitional());
        assertEquals("U", ((StepShapeAspect) resolved.get(10)).productDefinitional());
        assertEquals("TANGENT", ((StepShapeAspect) resolved.get(36)).entityName());
        for (int id = 37; id <= 54; id++) {
            if (id == 39 || id == 40) continue;
            StepShapeAspectRelationship relationship =
                    assertInstanceOf(StepShapeAspectRelationship.class, resolved.get(id));
            assertInstanceOf(StepEntity.class, relationship.relatingShapeAspect());
            assertInstanceOf(StepEntity.class, relationship.relatedShapeAspect());
        }
        // Verify DIMENSIONAL_SIZE and DIMENSIONAL_LOCATION have their own resolvers
        assertInstanceOf(StepDimensionalLocation.class, resolved.get(39));
        assertInstanceOf(StepDimensionalSize.class, resolved.get(40));
        assertEquals("SHAPE_ASPECT_RELATIONSHIP", ((StepShapeAspectRelationship) resolved.get(37)).entityName());
        assertEquals("DL", ((StepDimensionalLocation) resolved.get(39)).name());
        assertEquals("SHAPE_ASPECT_DERIVING_RELATIONSHIP", ((StepShapeAspectRelationship) resolved.get(45)).entityName());
        assertEquals("SHAPE_FEATURE_FIT_RELATIONSHIP", ((StepShapeAspectRelationship) resolved.get(54)).entityName());
    }

    @Test
    void shouldResolveAdditionalShapeAspectAliases() {
        String step = """
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
                #10=BEAD_END('BE','bead end',#7,.T.);
                #11=BOSS_TOP('BT','boss top',#7,.T.);
                #12=CIRCULAR_CLOSED_PROFILE('CCP','circular profile',#7,.T.);
                #13=COMPONENT_TERMINAL('CT','component terminal',#7,.T.);
                #14=CONSTITUENT_SHAPE_ASPECT('CSA2','constituent',#7,.T.);
                #15=CONTACTING_FEATURE('CF2','contacting',#7,.T.);
                #16=COUNTERBORE_HOLE_OCCURRENCE('CBHO','counterbore',#7,.T.,#51);
                #17=COUNTERDRILL_HOLE_OCCURRENCE('CDHO','counterdrill',#7,.T.,#52);
                #18=COUNTERSINK_HOLE_OCCURRENCE('CSHO','countersink',#7,.T.,#53);
                #19=DATUM_REFERENCE_ELEMENT('DRE','datum ref',#7,.T.);
                #20=DATUM_SYSTEM('DSYS','datum system',#7,.T.);
                #21=DEFAULT_MODEL_GEOMETRIC_VIEW('DMGV','default view',#7,.T.);
                #22=GENERAL_DATUM_REFERENCE('GDR','general datum',#7,.T.);
                #23=GEOMETRIC_TOLERANCE_WITH_MODIFIERS('GTWM','tolerance modifiers',#7,.T.);
                #24=LAYOUT_SPACING_CONTEXTUAL_AREA('LSCA','layout area',#7,.T.);
                #25=MATED_PART_RELATIONSHIP('MPR','mated part',#7,.T.);
                #26=MOUNTING_RESTRICTION_AREA('MRA','mounting area',#7,.T.);
                #27=MOUNTING_RESTRICTION_VOLUME('MRV','mounting volume',#7,.T.);
                #28=PATH_FEATURE_COMPONENT('PFC','path feature',#7,.T.);
                #29=PHYSICAL_COMPONENT_FEATURE('PCF','physical feature',#7,.T.);
                #30=PHYSICAL_COMPONENT_TERMINAL('PCT','physical terminal',#7,.T.);
                #31=PROJECTED_ZONE_DEFINITION('PZD','projected zone',#7,.T.);
                #32=REFERENCE_GRAPHIC_REGISTRATION_MARK('RGRM','registration mark',#7,.T.);
                #33=SEATING_PLANE('SP','seating plane',#7,.T.);
                #34=TERMINAL_FEATURE('TF','terminal feature',#7,.T.);
                #35=TERMINAL_LOCATION_GROUP('TLG','terminal location',#7,.T.);
                #36=TOLERANCE_ZONE_DEFINITION('TZD','tolerance zone',#7,.T.);
                #37=ASSEMBLY_SHAPE_CONSTRAINT_ITEM_RELATIONSHIP('ASCIR','constraint item',#8,#10);
                #38=ASSEMBLY_SHAPE_JOINT_ITEM_RELATIONSHIP('ASJIR','joint item',#8,#10);
                #39=COMPONENT_FEATURE_JOINT('CFJ','joint',#13,#29);
                #40=COMPONENT_FEATURE_RELATIONSHIP_WITH_TRANSFORMATION('CFRWT','transform',#13,#29);
                #41=COMPONENT_MATING_CONSTRAINT_CONDITION('CMCC','mating',#8,#10);
                #42=COMPONENT_PATH_SHAPE_ASPECT_RELATIONSHIP('CPSAR','component path',#28,#29);
                #43=CONNECTION_ZONE_INTERFACE_PLANE_RELATIONSHIP('CZIPR','interface plane',#8,#10);
                #44=CONNECTIVITY_DEFINITION_ITEM_RELATIONSHIP('CDIR','connectivity',#8,#10);
                #45=CONTACT_FEATURE_FIT_RELATIONSHIP('CFFR','contact fit',#15,#29);
                #46=DIMENSIONAL_LOCATION_WITH_DATUM_FEATURE('DLWDF','datum location',#19,#8);
                #47=DIMENSIONAL_LOCATION_WITH_PATH('DLWP','path location',#8,#28);
                #48=POSITIONED_SKETCH_TO_PART_ASSOCIATION('PSTPA','sketch part',#8,#10);
                #49=SHAPE_FEATURE_DEFINITION_ELEMENT_RELATIONSHIP('SFDERR','definition element',#8,#10);
                #50=BASIC_ROUND_HOLE('BRH','round hole definition');
                #51=COUNTERBORE_HOLE_DEFINITION('CBHD','counterbore definition');
                #52=COUNTERDRILL_HOLE_DEFINITION('CDHD','counterdrill definition');
                #53=COUNTERSINK_HOLE_DEFINITION('CSHD','countersink definition');
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        for (int id = 8; id <= 36; id++) {
            StepEntity entity = resolved.get(id);
            if (entity instanceof StepShapeAspectOccurrence occurrence) {
                assertEquals(7, occurrence.ofShape().id());
            } else {
                StepShapeAspect aspect = assertInstanceOf(StepShapeAspect.class, entity);
                assertEquals(7, aspect.ofShape().id());
            }
        }
        assertEquals("APPLIED_AREA", ((StepShapeAspect) resolved.get(8)).entityName());
        assertEquals("BASIC_ROUND_HOLE_OCCURRENCE",
                ((StepShapeAspectOccurrence) resolved.get(9)).entityName());
        assertEquals(50, ((StepShapeAspectOccurrence) resolved.get(9)).definition().id());
        assertEquals("TOLERANCE_ZONE_DEFINITION", ((StepShapeAspect) resolved.get(36)).entityName());
        for (int id = 37; id <= 49; id++) {
            StepShapeAspectRelationship relationship =
                    assertInstanceOf(StepShapeAspectRelationship.class, resolved.get(id));
            assertEquals(StepShapeAspect.class, relationship.relatingShapeAspect().getClass());
            assertEquals(StepShapeAspect.class, relationship.relatedShapeAspect().getClass());
        }
        assertEquals("ASSEMBLY_SHAPE_CONSTRAINT_ITEM_RELATIONSHIP",
                ((StepShapeAspectRelationship) resolved.get(37)).entityName());
        assertEquals("SHAPE_FEATURE_DEFINITION_ELEMENT_RELATIONSHIP",
                ((StepShapeAspectRelationship) resolved.get(49)).entityName());
        for (int id = 50; id <= 53; id++) {
            StepCharacterizedObject definition =
                    assertInstanceOf(StepCharacterizedObject.class, resolved.get(id));
            assertEquals(id, definition.id());
        }
    }

    @Test
    void shouldResolveBaseShapeAspectOccurrence() {
        String step = """
                DATA;
                #1=APPLICATION_CONTEXT('mechanical design');
                #2=PRODUCT_CONTEXT('part definition','mechanical',#1);
                #3=PRODUCT('PRT','Part','Part',(#2));
                #4=PRODUCT_DEFINITION_FORMATION('v1','',#3);
                #5=PRODUCT_DEFINITION_CONTEXT('design','released',#1);
                #6=PRODUCT_DEFINITION('pd','part def',#4,#5);
                #7=PRODUCT_DEFINITION_SHAPE('pds','shape',#6);
                #8=SHAPE_ASPECT('SA0','base',#7,.T.);
                #9=SHAPE_ASPECT_OCCURRENCE('SAO0','occ',#7,.T.,#8);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepShapeAspectOccurrence occurrence =
                assertInstanceOf(StepShapeAspectOccurrence.class, resolved.get(9));
        assertEquals("SHAPE_ASPECT_OCCURRENCE", occurrence.entityName());
        assertEquals(7, occurrence.ofShape().id());
        assertEquals("T", occurrence.productDefinitional());
        assertEquals(8, occurrence.definition().id());
    }

    @Test
    void shouldResolveFeatureDefinitionAliases() {
        String step = """
                DATA;
                #1=CHARACTERIZED_OBJECT('CO','base characterized object');
                #2=FEATURE_DEFINITION('FD','feature definition');
                #3=ADDITIVE_MANUFACTURING_FEATURE('AMF','additive feature');
                #4=BARRING_HOLE('BH','barring hole');
                #5=BEAD('BEAD','bead');
                #6=BOSS('BOSS','boss');
                #7=CIRCULAR_PATTERN('CP','circular pattern');
                #8=COMPOUND_FEATURE('CF','compound feature');
                #9=COMPOSITE_HOLE('CH','composite hole');
                #10=CONTACT_FEATURE_DEFINITION('CFD','contact feature');
                #11=EXPLICIT_COMPOSITE_HOLE('ECH','explicit composite hole');
                #12=EXPLICIT_ROUND_HOLE('ERH','explicit round hole');
                #13=EXTERNALLY_DEFINED_FEATURE_DEFINITION('EDFD','external feature');
                #14=FEATURE_DEFINITION_WITH_CONNECTION_AREA('FDWCA','connection area');
                #15=FEATURE_IN_PANEL('FIP','feature in panel');
                #16=FEATURE_PATTERN('FP','feature pattern');
                #17=FLAT_FACE('FF','flat face');
                #18=GEAR('GEAR','gear');
                #19=GENERAL_FEATURE('GF','general feature');
                #20=HOLE_IN_PANEL('HIP','hole in panel');
                #21=JOGGLE('JOG','joggle');
                #22=LOCATOR('LOC','locator');
                #23=MARKING('MRK','marking');
                #24=OUTER_ROUND('OR','outer round');
                #25=OUTSIDE_PROFILE('OP','outside profile');
                #26=POCKET('POC','pocket');
                #27=PROTRUSION('PRO','protrusion');
                #28=RECTANGULAR_PATTERN('RP','rectangular pattern');
                #29=REMOVAL_VOLUME('RV','removal volume');
                #30=REPLICATE_FEATURE('RF','replicate feature');
                #31=REVOLVED_PROFILE('RVP','revolved profile');
                #32=RIB('RIB','rib');
                #33=RIB_TOP('RT','rib top');
                #34=ROUND_HOLE('RH','round hole');
                #35=ROUNDED_END('RE','rounded end');
                #36=SHAPE_FEATURE_DEFINITION('SFD','shape feature');
                #37=BASIC_ROUND_HOLE('BRH','round hole');
                #38=COUNTERBORE_HOLE_DEFINITION('CBHD','counterbore');
                #39=COUNTERDRILL_HOLE_DEFINITION('CDHD','counterdrill');
                #40=COUNTERSINK_HOLE_DEFINITION('CSHD','countersink');
                #41=SLOT('SLOT','slot');
                #42=SPHERICAL_CAP('SC','spherical cap');
                #43=SPOTFACE_DEFINITION('SD','spotface');
                #44=SPOTFACE_HOLE_DEFINITION('SHD','spotface hole');
                #45=THREAD('TH','thread');
                #46=TURNED_KNURL('TK','turned knurl');
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        for (int id = 1; id <= 46; id++) {
            StepCharacterizedObject object =
                    assertInstanceOf(StepCharacterizedObject.class, resolved.get(id));
            assertEquals(id, object.id());
        }
        assertEquals("CHARACTERIZED_OBJECT", ((StepCharacterizedObject) resolved.get(1)).entityName());
        assertEquals("FEATURE_DEFINITION", ((StepCharacterizedObject) resolved.get(2)).entityName());
        assertEquals("TURNED_KNURL", ((StepCharacterizedObject) resolved.get(46)).entityName());
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
    void shouldResolveMappedItemAndCartesianTransformationOperators() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=DIRECTION('DX',(1.0,0.0,0.0));
                #3=DIRECTION('DY',(0.0,1.0,0.0));
                #4=DIRECTION('DZ',(0.0,0.0,1.0));
                #5=AXIS2_PLACEMENT_3D('AX3',#1,#4,#2);
                #6=AXIS2_PLACEMENT_2D('AX2',#1,#2);
                #7=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #8=CARTESIAN_POINT('P',(2.0,0.0,0.0));
                #9=REPRESENTATION('BASE',(#8),#7);
                #10=REPRESENTATION_MAP(#5,#9);
                #11=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T3',#2,#3,#1,2.0,#4);
                #12=MAPPED_ITEM(#10,#11);
                #13=CARTESIAN_TRANSFORMATION_OPERATOR_2D('T2',$,#3,#1,$);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepCartesianTransformationOperator operator3d =
                assertInstanceOf(StepCartesianTransformationOperator.class, resolved.get(11));
        assertEquals("CARTESIAN_TRANSFORMATION_OPERATOR_3D", operator3d.entityName());
        assertEquals(2, operator3d.axis1().id());
        assertEquals(3, operator3d.axis2().id());
        assertEquals(1, operator3d.localOrigin().id());
        assertEquals(2.0, operator3d.scale());
        assertEquals(4, operator3d.axis3().id());

        StepMappedItem mappedItem = assertInstanceOf(StepMappedItem.class, resolved.get(12));
        assertEquals(10, mappedItem.mappingSource().id());
        assertEquals(11, mappedItem.mappingTarget().id());

        StepCartesianTransformationOperator operator2d =
                assertInstanceOf(StepCartesianTransformationOperator.class, resolved.get(13));
        assertEquals("CARTESIAN_TRANSFORMATION_OPERATOR_2D", operator2d.entityName());
        assertEquals(null, operator2d.axis1());
        assertEquals(3, operator2d.axis2().id());
        assertEquals(null, operator2d.scale());
        assertEquals(null, operator2d.axis3());
    }

    @Test
    void shouldResolveReplicasAndBoundedSurfaceEntities() {
        String step = """
                DATA;
                #1=CARTESIAN_POINT('O',(0.0,0.0,0.0));
                #2=CARTESIAN_POINT('P',(1.0,0.0,0.0));
                #3=DIRECTION('DZ',(0.0,0.0,1.0));
                #4=DIRECTION('DX',(1.0,0.0,0.0));
                #5=AXIS2_PLACEMENT_3D('AX',#1,#3,#4);
                #6=PLANE('PL',#5);
                #7=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T',$,$,#1,1.0,$);
                #8=SURFACE_REPLICA('SR',#6,#7);
                #9=RECTANGULAR_TRIMMED_SURFACE('RTS',#8,0.0,1.0,0.0,1.0,.T.,.T.);
                #10=VECTOR('VX',#4,1.0);
                #11=LINE('L',#1,#10);
                #12=CURVE_REPLICA('CR',#11,#7);
                #13=CURVE_BOUNDED_SURFACE('CBS',#8,(#12),.T.);
                #14=OFFSET_SURFACE('OS',#9,2.0,.T.);
                #15=(GEOMETRIC_REPRESENTATION_CONTEXT(3) REPRESENTATION_CONTEXT('ID','MODEL'));
                #16=REPRESENTATION('PCURVE_REP',(#12),#15);
                #17=PCURVE('PC',#13,#16);
                #18=ADVANCED_FACE('AF',(),#13,.T.);
                #19=GEOMETRIC_SET('GS',(#8,#9,#13,#14,#26,#12,#17,#1));
                #20=VERTEX_POINT('V1',#1);
                #21=VERTEX_POINT('V2',#2);
                #22=EDGE_CURVE('EC',#20,#21,#12,.T.);
                #23=TRIMMED_CURVE('TC',#12,(#1),(#2),.T.,.CARTESIAN.);
                #24=SURFACE_CURVE('SC',#12,(#17),.PCURVE_S1.);
                #25=POINT_REPLICA('PR',#1,#7);
                #26=ORIENTED_SURFACE('OR',#13,.F.);
                #27=INTERSECTION_CURVE('IC',#12,(#17),.PCURVE_S1.);
                #28=COMPOSITE_CURVE_SEGMENT(.CONTINUOUS.,.T.,#27);
                #29=BOUNDARY_CURVE('BC',(#28),.T.);
                #30=OUTER_BOUNDARY_CURVE('OBC',(#28),.T.);
                #31=ORIENTED_CURVE('OC',#12,.F.);
                #32=OFFSET_CURVE_2D('OC2',#31,0.25,.F.);
                #33=REPRESENTATION('PCURVE_OFFSET_REP',(#32),#15);
                #34=PCURVE('PC_OFFSET',#13,#33);
                ENDSEC;
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        StepGeometricReplica surfaceReplica = assertInstanceOf(StepGeometricReplica.class, resolved.get(8));
        StepRectangularTrimmedSurface trimmedSurface =
                assertInstanceOf(StepRectangularTrimmedSurface.class, resolved.get(9));
        StepGeometricReplica curveReplica = assertInstanceOf(StepGeometricReplica.class, resolved.get(12));
        StepCurveBoundedSurface boundedSurface =
                assertInstanceOf(StepCurveBoundedSurface.class, resolved.get(13));
        StepOffsetSurface offsetSurface = assertInstanceOf(StepOffsetSurface.class, resolved.get(14));
        StepPcurve pcurve = assertInstanceOf(StepPcurve.class, resolved.get(17));
        StepAdvancedFace advancedFace = assertInstanceOf(StepAdvancedFace.class, resolved.get(18));
        StepGeometricSet geometricSet = assertInstanceOf(StepGeometricSet.class, resolved.get(19));
        StepEdgeCurve edgeCurve = assertInstanceOf(StepEdgeCurve.class, resolved.get(22));
        StepTrimmedCurve trimmedCurve = assertInstanceOf(StepTrimmedCurve.class, resolved.get(23));
        StepSurfaceCurve surfaceCurve = assertInstanceOf(StepSurfaceCurve.class, resolved.get(24));
        StepGeometricReplica pointReplica = assertInstanceOf(StepGeometricReplica.class, resolved.get(25));
        StepOrientedSurface orientedSurface =
                assertInstanceOf(StepOrientedSurface.class, resolved.get(26));
        StepSurfaceCurve intersectionCurve =
                assertInstanceOf(StepSurfaceCurve.class, resolved.get(27));
        StepCompositeCurveOnSurface boundaryCurve =
                assertInstanceOf(StepCompositeCurveOnSurface.class, resolved.get(29));
        StepCompositeCurveOnSurface outerBoundaryCurve =
                assertInstanceOf(StepCompositeCurveOnSurface.class, resolved.get(30));
        StepOrientedCurve orientedCurve = assertInstanceOf(StepOrientedCurve.class, resolved.get(31));
        StepOffsetCurve2D offsetCurve2d =
                assertInstanceOf(StepOffsetCurve2D.class, resolved.get(32));
        StepPcurve offsetPcurve = assertInstanceOf(StepPcurve.class, resolved.get(34));

        assertEquals("SURFACE_REPLICA", surfaceReplica.entityName());
        assertEquals(6, surfaceReplica.parent().id());
        assertEquals(7, surfaceReplica.transformation().id());
        assertEquals(8, trimmedSurface.basisSurface().id());
        assertEquals(1.0, trimmedSurface.u2());
        assertEquals(8, boundedSurface.basisSurface().id());
        assertEquals(12, boundedSurface.boundaries().getFirst().id());
        assertEquals(9, offsetSurface.basisSurface().id());
        assertEquals(13, pcurve.basisSurface().id());
        assertEquals(13, advancedFace.faceGeometry().id());
        assertEquals(8, geometricSet.elements().size());
        assertEquals(12, edgeCurve.edgeGeometry().id());
        assertEquals(12, trimmedCurve.basisCurve().id());
        assertEquals(12, surfaceCurve.curve3d().id());
        assertEquals("POINT_REPLICA", pointReplica.entityName());
        assertEquals(13, orientedSurface.surfaceElement().id());
        assertEquals(false, orientedSurface.orientation());
        assertEquals(12, intersectionCurve.curve3d().id());
        assertEquals(17, intersectionCurve.associatedGeometry().getFirst().id());
        assertEquals(28, boundaryCurve.segments().getFirst().id());
        assertEquals(28, outerBoundaryCurve.segments().getFirst().id());
        assertEquals(12, orientedCurve.curveElement().id());
        assertEquals(false, orientedCurve.orientation());
        assertEquals(31, offsetCurve2d.basisCurve().id());
        assertEquals(0.25, offsetCurve2d.distance());
        assertEquals(32, offsetPcurve.referenceToCurve().items().getFirst().id());
    }

    @Test
    void shouldResolveAdditionalShapeRepresentationAliases() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        for (int id = 3; id <= 8; id++) {
            StepRepresentation representation =
                    assertInstanceOf(StepRepresentation.class, resolved.get(id));
            assertEquals(true, representation.shapeRepresentation());
            assertEquals(1, representation.items().size());
            assertEquals(2, representation.context().id());
        }
        assertEquals("GEOMETRICALLY_BOUNDED_SURFACE_SHAPE_REPRESENTATION",
                assertInstanceOf(StepRepresentation.class, resolved.get(3)).entityName());
        assertEquals("GEOMETRICALLY_BOUNDED_WIREFRAME_SHAPE_REPRESENTATION",
                assertInstanceOf(StepRepresentation.class, resolved.get(4)).entityName());
        assertEquals("GEOMETRIC_SET_SHAPE_REPRESENTATION",
                assertInstanceOf(StepRepresentation.class, resolved.get(5)).entityName());
        assertEquals("MANIFOLD_SURFACE_SHAPE_REPRESENTATION",
                assertInstanceOf(StepRepresentation.class, resolved.get(6)).entityName());
        assertEquals("SHELL_BASED_SURFACE_MODEL_SHAPE_REPRESENTATION",
                assertInstanceOf(StepRepresentation.class, resolved.get(7)).entityName());
        assertEquals("SURFACE_MODEL_SHAPE_REPRESENTATION",
                assertInstanceOf(StepRepresentation.class, resolved.get(8)).entityName());
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
        assertEquals("REPRESENTATION_RELATIONSHIP", relationship.entityName());
    }

    @Test
    void shouldResolveRepresentationRelationshipAliases() {
        String step = """
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
                """;

        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));

        assertEquals("DEFINITIONAL_REPRESENTATION_RELATIONSHIP",
                assertInstanceOf(StepRepresentationRelationship.class, resolved.get(5)).entityName());
        assertEquals("TOPOLOGY_TO_GEOMETRY_MODEL_ASSOCIATION",
                assertInstanceOf(StepRepresentationRelationship.class, resolved.get(6)).entityName());
        assertEquals("KINEMATIC_FRAME_REPRESENTATION_RELATIONSHIP",
                assertInstanceOf(StepRepresentationRelationship.class, resolved.get(7)).entityName());
        assertEquals("COAXIAL_ASSEMBLY_CONSTRAINT",
                assertInstanceOf(StepRepresentationRelationship.class, resolved.get(8)).entityName());
    }
}
