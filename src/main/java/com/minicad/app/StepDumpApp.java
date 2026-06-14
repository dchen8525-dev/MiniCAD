package com.minicad.app;

import com.minicad.common.StepParseException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.common.MiniCadIssue;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.step.model.product.StepBooleanClippingResult;
import com.minicad.common.GeometryException;
import com.minicad.step.model.product.StepBrepWithVoids;
import com.minicad.step.model.product.StepBooleanResult;
import com.minicad.step.model.topology.StepClosedShell;
import com.minicad.step.model.product.StepCsgPrimitive;
import com.minicad.step.model.product.StepCsgSolid;
import com.minicad.step.model.topology.StepConnectedEdgeSet;
import com.minicad.step.model.topology.StepConnectedFaceSet;
import com.minicad.step.model.topology.StepConnectedFaceSubSet;
import com.minicad.step.model.geometry.StepCartesianPoint;
import com.minicad.step.model.geometry.StepCircle;
import com.minicad.step.model.topology.StepEdgeCurve;
import com.minicad.step.model.product.StepEdgeBasedWireframeModel;
import com.minicad.step.model.product.StepExtrudedFaceSolid;
import com.minicad.step.model.product.StepRevolvedFaceSolid;
import com.minicad.step.model.topology.StepEdgeLoop;
import com.minicad.step.model.geometry.StepEllipse;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.base.StepFaceEntity;
import com.minicad.step.model.product.StepFaceBasedSurfaceModel;
import com.minicad.step.model.topology.StepFaceBound;
import com.minicad.step.model.product.StepGeometricCurveSet;
import com.minicad.step.model.product.StepGeometricSet;
import com.minicad.step.model.geometry.StepLine;
import com.minicad.step.model.product.StepManifoldSolidBrep;
import com.minicad.step.model.product.StepItemDefinedTransformation;
import com.minicad.step.model.product.StepMappedItem;
import com.minicad.step.model.geometry.StepOpenPath;
import com.minicad.step.model.topology.StepOpenShell;
import com.minicad.step.model.geometry.StepOffsetCurve2D;
import com.minicad.step.model.geometry.StepOffsetCurve3D;
import com.minicad.step.model.geometry.StepOffsetSurface;
import com.minicad.step.model.topology.StepOrientedEdge;
import com.minicad.step.model.topology.StepOrientedClosedShell;
import com.minicad.step.model.geometry.StepOrientedCurve;
import com.minicad.step.model.topology.StepOrientedOpenShell;
import com.minicad.step.model.geometry.StepOrientedPath;
import com.minicad.step.model.geometry.StepOrientedSurface;
import com.minicad.step.model.annotation.StepOverRidingStyledItem;
import com.minicad.step.model.geometry.StepPath;
import com.minicad.step.model.geometry.StepDegeneratePcurve;
import com.minicad.step.model.geometry.StepPcurve;
import com.minicad.step.model.topology.StepPolyLoop;
import com.minicad.step.model.geometry.StepPolyline;
import com.minicad.step.model.geometry.StepPointSet;
import com.minicad.step.model.geometry.StepPlane;
import com.minicad.step.model.profile.StepProfileDef;
import com.minicad.step.model.geometry.StepRationalBSplineCurve;
import com.minicad.step.model.geometry.StepRationalBSplineSurface;
import com.minicad.step.model.geometry.StepRectangularTrimmedSurface;
import com.minicad.step.model.workflow.StepRepresentation;
import com.minicad.step.model.product.StepRepresentationMap;
import com.minicad.step.model.product.StepRepresentationRelationship;
import com.minicad.step.model.product.StepRepresentationRelationshipWithTransformation;
import com.minicad.step.model.resource.StepResourcePropertyRepresentation;
import com.minicad.step.model.product.StepShellBasedSurfaceModel;
import com.minicad.step.model.product.StepShellBasedWireframeModel;
import com.minicad.step.model.workflow.StepShapeRepresentationRelationship;
import com.minicad.step.model.product.StepSolidReplica;
import com.minicad.step.model.annotation.StepStyledItem;
import com.minicad.step.model.geometry.StepSubpath;
import com.minicad.step.model.topology.StepSubedge;
import com.minicad.step.model.product.StepSweptAreaSolid;
import com.minicad.step.model.geometry.StepSurfacedOpenShell;
import com.minicad.step.model.geometry.StepSeamCurve;
import com.minicad.step.model.geometry.StepSurfaceCurve;
import com.minicad.step.model.geometry.StepSurfaceOfLinearExtrusion;
import com.minicad.step.model.geometry.StepSurfaceOfRevolution;
import com.minicad.step.model.geometry.StepTrimmedCurve;
import com.minicad.step.model.topology.StepVertexLoop;
import com.minicad.step.model.topology.StepVertexShell;
import com.minicad.step.model.topology.StepVertexPoint;
import com.minicad.step.model.topology.StepWireShell;
import com.minicad.step.model.geometry.StepAxis1Placement;
import com.minicad.step.model.geometry.StepAxis2Placement2D;
import com.minicad.step.model.geometry.StepAxis2Placement3D;
import com.minicad.step.model.annotation.StepAnnotationCurveOccurrence;
import com.minicad.step.model.annotation.StepAnnotationFillArea;
import com.minicad.step.model.annotation.StepAnnotationFillAreaOccurrence;
import com.minicad.step.model.annotation.StepAnnotationOccurrenceRelationship;
import com.minicad.step.model.annotation.StepAnnotationPlaceholderOccurrence;
import com.minicad.step.model.annotation.StepAnnotationPlane;
import com.minicad.step.model.annotation.StepAnnotationPointOccurrence;
import com.minicad.step.model.annotation.StepAnnotationSubfigureOccurrence;
import com.minicad.step.model.annotation.StepAnnotationSymbol;
import com.minicad.step.model.annotation.StepAnnotationSymbolOccurrence;
import com.minicad.step.model.annotation.StepAnnotationText;
import com.minicad.step.model.annotation.StepAnnotationTextCharacter;
import com.minicad.step.model.annotation.StepAnnotationTextOccurrence;
import com.minicad.step.model.geometry.StepBSplineCurveWithKnots;
import com.minicad.step.model.geometry.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.geometry.StepCartesianTransformationOperator;
import com.minicad.step.model.annotation.StepCharacterGlyphStyleOutline;
import com.minicad.step.model.annotation.StepCharacterGlyphStyleOutlineWithCharacteristics;
import com.minicad.step.model.annotation.StepCharacterGlyphStyleStroke;
import com.minicad.step.model.base.StepCharacterizedObject;
import com.minicad.step.model.annotation.StepColour;
import com.minicad.step.model.annotation.StepColourSpecification;
import com.minicad.step.model.annotation.StepColourRgb;
import com.minicad.step.model.geometry.StepCompositeCurve;
import com.minicad.step.model.geometry.StepCompositeCurveOnSurface;
import com.minicad.step.model.geometry.StepCompositeCurveSegment;
import com.minicad.step.model.geometry.StepCurveBoundedSurface;
import com.minicad.step.model.annotation.StepCurveStyle;
import com.minicad.step.model.geometry.StepDirection;
import com.minicad.step.model.geometry.StepVector;
import com.minicad.step.model.tolerance.StepDimensionCurve;
import com.minicad.step.model.annotation.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.annotation.StepDraughtingPreDefinedColour;
import com.minicad.step.model.annotation.StepDraughtingPreDefinedCurveFont;
import com.minicad.step.model.annotation.StepDraughtingPreDefinedTextFont;
import com.minicad.step.model.annotation.StepDraughtingCallout;
import com.minicad.step.model.annotation.StepDraughtingCalloutRelationship;
import com.minicad.step.model.annotation.StepDraughtingModelItemAssociation;
import com.minicad.step.model.annotation.StepDraughtingModelItemAssociationWithPlaceholder;
import com.minicad.step.model.approval.StepApproval;
import com.minicad.step.model.approval.StepApprovalAssignment;
import com.minicad.step.model.approval.StepApprovalDateTime;
import com.minicad.step.model.approval.StepApprovalPersonOrganization;
import com.minicad.step.model.approval.StepApprovalRole;
import com.minicad.step.model.approval.StepApprovalStatus;
import com.minicad.step.model.classification.StepAttributeAssertion;
import com.minicad.step.model.document.StepDocument;
import com.minicad.step.model.document.StepDocumentReference;
import com.minicad.step.model.document.StepDocumentRelationship;
import com.minicad.step.model.document.StepDocumentType;
import com.minicad.step.model.document.StepDocumentUsageConstraint;
import com.minicad.step.model.classification.StepExternalSource;
import com.minicad.step.model.classification.StepExternalSourceRelationship;
import com.minicad.step.model.classification.StepExternalIdentificationAssignment;
import com.minicad.step.model.annotation.StepExternallyDefinedItem;
import com.minicad.step.model.organization.StepAddress;
import com.minicad.step.model.approval.StepAppliedApprovalAssignment;
import com.minicad.step.model.action.StepActionPropertyRepresentation;
import com.minicad.step.model.approval.StepAppliedCertificationAssignment;
import com.minicad.step.model.classification.StepAppliedClassificationAssignment;
import com.minicad.step.model.resource.StepAppliedContractAssignment;
import com.minicad.step.model.date_time.StepAppliedDateAssignment;
import com.minicad.step.model.date_time.StepAppliedDateTimeAssignment;
import com.minicad.step.model.document.StepAppliedDocumentReference;
import com.minicad.step.model.classification.StepAppliedExternalIdentificationAssignment;
import com.minicad.step.model.classification.StepAppliedGroupAssignment;
import com.minicad.step.model.classification.StepAppliedIdentificationAssignment;
import com.minicad.step.model.organization.StepAppliedLanguageAssignment;
import com.minicad.step.model.classification.StepAppliedNameAssignment;
import com.minicad.step.model.organization.StepAppliedOrganizationAssignment;
import com.minicad.step.model.organization.StepAppliedPersonAndOrganizationAssignment;
import com.minicad.step.model.security.StepAppliedSecurityClassificationAssignment;
import com.minicad.step.model.document.StepApplicationContext;
import com.minicad.step.model.document.StepApplicationProtocolDefinition;
import com.minicad.step.model.approval.StepCertification;
import com.minicad.step.model.approval.StepCertificationAssignment;
import com.minicad.step.model.approval.StepCertificationType;
import com.minicad.step.model.date_time.StepCalendarDate;
import com.minicad.step.model.product.StepChainBasedGeometricItemSpecificUsage;
import com.minicad.step.model.product.StepChainBasedItemIdentifiedRepresentationUsage;
import com.minicad.step.model.classification.StepClassificationAssignment;
import com.minicad.step.model.classification.StepClassificationRole;
import com.minicad.step.model.date_time.StepCoordinatedUniversalTimeOffset;
import com.minicad.step.model.resource.StepContract;
import com.minicad.step.model.resource.StepContractAssignment;
import com.minicad.step.model.resource.StepContractType;
import com.minicad.step.model.product.StepContextDependentShapeRepresentation;
import com.minicad.step.model.unit.StepContextDependentUnit;
import com.minicad.step.model.product.StepContactRatioRepresentation;
import com.minicad.step.model.geometry.StepConicalSurface;
import com.minicad.step.model.geometry.StepConicCurve;
import com.minicad.step.model.date_time.StepDateAndTime;
import com.minicad.step.model.date_time.StepDateAssignment;
import com.minicad.step.model.date_time.StepDateRole;
import com.minicad.step.model.date_time.StepDateTimeAssignment;
import com.minicad.step.model.date_time.StepDateTimeRole;
import com.minicad.step.model.base.StepDescriptiveRepresentationItem;
import com.minicad.step.model.classification.StepDescriptionAttribute;
import com.minicad.step.model.unit.StepDerivedUnit;
import com.minicad.step.model.unit.StepDerivedUnitElement;
import com.minicad.step.model.unit.StepDimensionalExponents;
import com.minicad.step.model.geometry.StepDegenerateToroidalSurface;
import com.minicad.step.model.product.StepEffectivity;
import com.minicad.step.model.product.StepEffectivityRelationship;
import com.minicad.step.model.annotation.StepFillAreaStyle;
import com.minicad.step.model.annotation.StepFillAreaStyleColour;
import com.minicad.step.model.resource.StepGeneralProperty;
import com.minicad.step.model.resource.StepGeneralPropertyRelationship;
import com.minicad.step.model.product.StepGeometricItemSpecificUsage;
import com.minicad.step.model.workflow.StepGeometricRepresentationContext;
import com.minicad.step.model.base.StepGeometricRepresentationItem;
import com.minicad.step.model.product.StepGeometricReplica;
import com.minicad.step.model.classification.StepGroup;
import com.minicad.step.model.classification.StepGroupAssignment;
import com.minicad.step.model.classification.StepGroupRelationship;
import com.minicad.step.model.product.StepHalfSpaceSolid;
import com.minicad.step.model.classification.StepIdAttribute;
import com.minicad.step.model.classification.StepIdentificationAssignment;
import com.minicad.step.model.classification.StepIdentificationRole;
import com.minicad.step.model.product.StepItemIdentifiedRepresentationUsage;
import com.minicad.step.model.kinematic.StepKinematicPropertyDefinitionRepresentation;
import com.minicad.step.model.kinematic.StepKinematicPropertyMechanismRepresentation;
import com.minicad.step.model.kinematic.StepKinematicPropertyRepresentationRelation;
import com.minicad.step.model.kinematic.StepKinematicPropertyTopologyRepresentation;
import com.minicad.step.model.organization.StepLanguage;
import com.minicad.step.model.organization.StepLanguageAssignment;
import com.minicad.step.model.annotation.StepLeaderCurve;
import com.minicad.step.model.product.StepMechanicalDesignRequirementItemAssociation;
import com.minicad.step.model.base.StepMeasureRepresentationItem;
import com.minicad.step.model.unit.StepMeasureWithUnit;
import com.minicad.step.model.classification.StepNameAttribute;
import com.minicad.step.model.classification.StepNameAssignment;
import com.minicad.step.model.unit.StepNamedUnit;
import com.minicad.step.model.product.StepNextAssemblyUsageOccurrence;
import com.minicad.step.model.geometry.StepBoxDomain;
import com.minicad.step.model.annotation.StepPreDefinedColour;
import com.minicad.step.model.annotation.StepPreDefinedCurveFont;
import com.minicad.step.model.annotation.StepPreDefinedDimensionSymbol;
import com.minicad.step.model.annotation.StepPreDefinedGeometricalToleranceSymbol;
import com.minicad.step.model.base.StepPreDefinedItem;
import com.minicad.step.model.annotation.StepPreDefinedMarker;
import com.minicad.step.model.annotation.StepPreDefinedPointMarkerSymbol;
import com.minicad.step.model.annotation.StepPreDefinedSurfaceSideStyle;
import com.minicad.step.model.annotation.StepPreDefinedSymbol;
import com.minicad.step.model.annotation.StepPreDefinedTerminatorSymbol;
import com.minicad.step.model.annotation.StepPreDefinedTextFont;
import com.minicad.step.model.date_time.StepLocalTime;
import com.minicad.step.model.organization.StepPerson;
import com.minicad.step.model.organization.StepPersonAndOrganization;
import com.minicad.step.model.organization.StepPersonAndOrganizationAssignment;
import com.minicad.step.model.organization.StepPersonAndOrganizationRole;
import com.minicad.step.model.workflow.StepPlacedDatumTargetFeature;
import com.minicad.step.model.workflow.StepPlacedTarget;
import com.minicad.step.model.annotation.StepPmiRequirementItemAssociation;
import com.minicad.step.model.product.StepProductCategory;
import com.minicad.step.model.product.StepProductCategoryRelationship;
import com.minicad.step.model.product.StepProduct;
import com.minicad.step.model.product.StepProductContext;
import com.minicad.step.model.product.StepProductDefinition;
import com.minicad.step.model.product.StepProductDefinitionContext;
import com.minicad.step.model.product.StepProductDefinitionEffectivity;
import com.minicad.step.model.product.StepProductDefinitionFormation;
import com.minicad.step.model.product.StepProductDefinitionFormationRelationship;
import com.minicad.step.model.product.StepProductDefinitionRelationship;
import com.minicad.step.model.product.StepProductDefinitionRelationshipRelationship;
import com.minicad.step.model.product.StepProductDefinitionShape;
import com.minicad.step.model.workflow.StepPropertyDefinition;
import com.minicad.step.model.workflow.StepPropertyDefinitionRepresentation;
import com.minicad.step.model.workflow.StepPropertyDefinitionRelationship;
import com.minicad.step.model.product.StepProductRelatedProductCategory;
import com.minicad.step.model.product.StepProductRelationship;
import com.minicad.step.model.geometry.StepProjectionCurve;
import com.minicad.step.model.annotation.StepPresentationLayerAssignment;
import com.minicad.step.model.annotation.StepPresentationStyleAssignment;
import com.minicad.step.model.geometry.StepPoint;
import com.minicad.step.model.annotation.StepPointStyle;
import com.minicad.step.model.product.StepRepresentationContext;
import com.minicad.step.model.base.StepRepresentationItem;
import com.minicad.step.model.workflow.StepSymbolRepresentationMap;
import com.minicad.step.model.organization.StepOrganization;
import com.minicad.step.model.organization.StepOrganizationAssignment;
import com.minicad.step.model.organization.StepOrganizationRelationship;
import com.minicad.step.model.organization.StepOrganizationRole;
import com.minicad.step.model.security.StepSecurityClassification;
import com.minicad.step.model.security.StepSecurityClassificationAssignment;
import com.minicad.step.model.security.StepSecurityClassificationLevel;
import com.minicad.step.model.geometry.StepSphericalSurface;
import com.minicad.step.model.annotation.StepSurfaceSideStyle;
import com.minicad.step.model.annotation.StepSurfaceStyleBoundary;
import com.minicad.step.model.annotation.StepSurfaceStyleControlGrid;
import com.minicad.step.model.annotation.StepSurfaceStyleFillArea;
import com.minicad.step.model.annotation.StepSurfaceStyleParameterLine;
import com.minicad.step.model.annotation.StepSurfaceStyleReflectanceAmbient;
import com.minicad.step.model.annotation.StepSurfaceStyleReflectanceAmbientDiffuse;
import com.minicad.step.model.annotation.StepSurfaceStyleReflectanceAmbientDiffuseSpecular;
import com.minicad.step.model.annotation.StepSurfaceStyleSegmentationCurve;
import com.minicad.step.model.annotation.StepSurfaceStyleSilhouette;
import com.minicad.step.model.annotation.StepSurfaceStyleTransparent;
import com.minicad.step.model.annotation.StepSurfaceStyleUsage;
import com.minicad.step.model.annotation.StepTerminatorSymbol;
import com.minicad.step.model.annotation.StepTextStyle;
import com.minicad.step.model.annotation.StepTextStyleForDefinedFont;
import com.minicad.step.model.annotation.StepTextStyleWithBoxCharacteristics;
import com.minicad.step.model.annotation.StepTextStyleWithJustification;
import com.minicad.step.model.annotation.StepTextStyleWithMirror;
import com.minicad.step.model.annotation.StepTextStyleWithSpacing;
import com.minicad.step.model.annotation.StepSymbolColour;
import com.minicad.step.model.annotation.StepSymbolStyle;
import com.minicad.step.model.classification.StepShapeAspect;
import com.minicad.step.model.classification.StepShapeAspectOccurrence;
import com.minicad.step.model.classification.StepShapeAspectRelationship;
import com.minicad.step.model.product.StepShapeDefinitionRepresentation;
import com.minicad.step.model.geometry.StepCurve;
import com.minicad.step.model.geometry.StepSurface;
import com.minicad.step.model.geometry.StepSurfaceModel;
import com.minicad.step.model.product.StepSolidModel;
import com.minicad.step.model.geometry.StepToroidalSurface;
import com.minicad.step.model.topology.StepVertex;
import com.minicad.step.model.topology.StepEdge;
import com.minicad.step.model.topology.StepFace;
import com.minicad.step.model.unit.StepSiUnit;
import com.minicad.step.model.base.StepTopologicalRepresentationItem;
import com.minicad.step.model.unit.StepTypedMeasureWithUnit;
import com.minicad.step.model.unit.StepUncertaintyMeasureWithUnit;
import com.minicad.step.model.geometry.StepCylindricalSurface;
import com.minicad.step.model.annotation.StepUserDefinedCurveFont;
import com.minicad.step.model.annotation.StepUserDefinedMarker;
import com.minicad.step.model.annotation.StepUserDefinedTerminatorSymbol;
import com.minicad.step.model.base.StepValueRepresentationItem;
import com.minicad.step.model.unit.StepConversionBasedUnit;
import com.minicad.step.model.unit.StepConversionBasedUnitWithOffset;
import com.minicad.step.model.unit.StepGlobalUnitAssignedContext;
import com.minicad.step.model.workflow.StepGlobalUncertaintyAssignedContext;
import com.minicad.step.model.action.StepAbstractVariable;
import com.minicad.step.model.action.StepBackChainingRuleBody;
import com.minicad.step.model.geometry.StepBezierCurve;
import com.minicad.step.model.geometry.StepBezierSurface;
import com.minicad.step.model.geometry.StepBoundedCurve;
import com.minicad.step.model.geometry.StepBoundedSurface;
import com.minicad.step.model.geometry.StepBSplineCurve;
import com.minicad.step.model.geometry.StepBSplineSurface;
import com.minicad.step.model.action.StepForwardChainingRulePremise;
import com.minicad.step.model.geometry.StepPiecewiseBezierCurve;
import com.minicad.step.model.geometry.StepPiecewiseBezierSurface;
import com.minicad.step.model.geometry.StepQuasiUniformCurve;
import com.minicad.step.model.geometry.StepQuasiUniformSurface;
import com.minicad.step.model.action.StepRowVariable;
import com.minicad.step.model.action.StepScalarVariable;
import com.minicad.step.model.geometry.StepUniformCurve;
import com.minicad.step.model.geometry.StepUniformSurface;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Minimal CLI demo that reads a STEP file and prints a structural summary.
 */
public final class StepDumpApp {
    private static final Logger log = LoggerFactory.getLogger(StepDumpApp.class);

    private StepDumpApp() {
    }

    /**
     * CLI entry point.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        int exitCode = run(args, System.out::println, System.err::println);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    static int run(String[] args, Consumer<String> out, Consumer<String> err) {
        CliOptions options = parseArgs(args, err);
        if (options == null) {
            return 2;
        }

        int exitCode = 0;
        List<DumpFileResult> results = new ArrayList<>();
        for (Path path : options.paths()) {
            DumpFileResult result = processFile(path, options.debug());
            results.add(result);
            if (!result.success()) {
                exitCode = 1;
            }
        }
        if (options.json()) {
            out.accept(toJson(results, exitCode));
            return exitCode;
        }
        boolean first = true;
        for (DumpFileResult result : results) {
            if (!first) {
                out.accept("");
            }
            first = false;
            if (result.success()) {
                renderText(result, options.validateOnly()).forEach(out);
            } else {
                emitFailure(result, err);
            }
        }
        return exitCode;
    }

    private static DumpFileResult processFile(Path path, boolean debug) {
        try {
            String text = StepTextReader.read(path);
            StepFile stepFile = StepParser.parse(text);
            Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
            StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

            List<String> buildLines = new ArrayList<>();
            appendBuildSummary(resolved, builder, buildLines);
            return DumpFileResult.success(
                    path,
                    stepFile,
                    resolved,
                    buildLines,
                    extractUnsupportedCount(buildLines),
                    calculatePointBoundingBox(resolved, builder),
                    successIssues(extractUnsupportedCount(buildLines), UnitExtractor.extract(resolved)));
        } catch (IOException ex) {
            return DumpFileResult.failure(path, "cannot read file: " + ex.getMessage(), ex, debug);
        } catch (StepParseException | StepResolutionException | UnsupportedGeometryException | TopologyException | GeometryException ex) {
            return DumpFileResult.failure(path, ex.getMessage(), ex, debug);
        }
    }

    private static List<MiniCadIssue> successIssues(int unsupportedCount, UnitExtractor.UnitInfo units) {
        List<MiniCadIssue> issues = new ArrayList<>();
        if (units != null && units.scaleToMeters() != null
                && Math.abs(units.scaleToMeters() - 1.0) > 1.0e-12) {
            issues.add(MiniCadIssue.warning(
                    "units.coordinates_not_normalized",
                    null,
                    null,
                    "geometry coordinates are emitted in source STEP units; scaleToMeters is metadata only"));
        }
        if (unsupportedCount > 0) {
            issues.add(MiniCadIssue.warning(
                    "step.unsupported",
                    null,
                    null,
                    "build summary reported " + unsupportedCount + " unsupported face(s)"));
        }
        return List.copyOf(issues);
    }

    private static CliOptions parseArgs(String[] args, Consumer<String> err) {
        boolean debug = false;
        boolean validateOnly = false;
        boolean json = false;
        List<String> paths = new ArrayList<>();
        for (String arg : args) {
            if ("--debug".equals(arg)) {
                debug = true;
            } else if ("--validate-only".equals(arg)) {
                validateOnly = true;
            } else if ("--json".equals(arg)) {
                json = true;
            } else {
                paths.add(arg);
            }
        }
        if (paths.isEmpty()) {
            err.accept("Usage: StepDumpApp [--debug] [--validate-only] [--json] <step-file>...");
            return null;
        }
        return new CliOptions(paths.stream().map(Path::of).collect(Collectors.toList()), debug, validateOnly, json);
    }

    private static void emitFailure(DumpFileResult result, Consumer<String> err) {
        err.accept("STEP processing failed for " + result.path() + ": " + result.errorMessage());
        if (result.stackTrace() != null) {
            result.stackTrace().lines().forEach(err);
        }
    }

    private record CliOptions(List<Path> paths, boolean debug, boolean validateOnly, boolean json) {
    }

    private static List<String> renderText(DumpFileResult result, boolean validateOnly) {
        List<String> lines = new ArrayList<>();
        lines.add("File: " + result.path());
        lines.add("");
        if (validateOnly) {
            appendValidationSummary(result, lines);
        } else {
            appendSyntaxSummary(result.stepFile(), lines);
            lines.add("");
            appendSemanticSummary(result.resolved(), lines);
            lines.add("");
            lines.addAll(result.buildLines());
        }
        return lines;
    }

    private static void appendValidationSummary(DumpFileResult result, List<String> lines) {
        lines.add("Validation Summary");
        lines.add("  status: ok");
        lines.add("  entityCount: " + result.entityCount());
        lines.add("  resolvedCount: " + result.resolvedCount());
        extractBuildTotals(result.buildLines()).ifPresent(total -> lines.add("  " + total));
    }

    private static java.util.Optional<String> extractBuildTotals(List<String> buildValidationLines) {
        return buildValidationLines.stream()
                .map(String::strip)
                .filter(line -> line.startsWith("totals: "))
                .findFirst();
    }

    private static int extractUnsupportedCount(List<String> buildLines) {
        return extractBuildTotals(buildLines)
                .map(total -> total.substring(total.lastIndexOf("unsupportedFaces=") + "unsupportedFaces=".length()))
                .map(Integer::parseInt)
                .orElse(0);
    }

    private static BoundingBox3 calculatePointBoundingBox(Map<Integer, StepEntity> resolved, StepCadBuilder builder) {
        BoundingBox3.Box box = BoundingBox3.mutable();
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepCartesianPoint) { StepCartesianPoint point = (StepCartesianPoint) entity;
                try {
                    box.expand(builder.buildPoint(point.id()));
                } catch (GeometryException | StepResolutionException ex) {
                    box.expand(new CartesianPoint(point.coordinates().get(0), point.coordinates().get(1), point.coordinates().get(2)));
                }
            }
        }
        return box.toImmutable();
    }

    private static String toJson(List<DumpFileResult> results, int exitCode) {
        StringBuilder json = new StringBuilder();
        json.append("{\"status\":\"")
                .append(exitCode == 0 ? "ok" : "failed")
                .append("\",\"exitCode\":")
                .append(exitCode)
                .append(",\"files\":[");
        for (int i = 0; i < results.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            appendFileJson(json, results.get(i));
        }
        json.append("]}");
        return json.toString();
    }

    private static void appendFileJson(StringBuilder json, DumpFileResult result) {
        json.append("{\"path\":");
        appendJsonString(json, result.path().toString());
        json.append(",\"status\":\"").append(result.success() ? "ok" : "failed").append('"');
        if (result.success()) {
            json.append(",\"entityCount\":").append(result.entityCount());
            json.append(",\"resolvedCount\":").append(result.resolvedCount());
            json.append(",\"unsupportedCount\":").append(result.unsupportedCount());
            json.append(",\"bbox\":");
            appendBoundingBoxJson(json, result.bbox());
        } else {
            json.append(",\"error\":");
            appendJsonString(json, result.errorMessage());
        }
        json.append(",\"issues\":");
        appendIssuesJson(json, result.issues());
        json.append('}');
    }

    private static void appendIssuesJson(StringBuilder json, List<MiniCadIssue> issues) {
        json.append('[');
        for (int i = 0; i < issues.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            MiniCadIssue issue = issues.get(i);
            json.append("{\"severity\":");
            appendJsonString(json, issue.severity().name());
            json.append(",\"code\":");
            appendJsonString(json, issue.code());
            if (issue.entityId() != null) {
                json.append(",\"entityId\":").append(issue.entityId());
            }
            if (issue.entityType() != null) {
                json.append(",\"entityType\":");
                appendJsonString(json, issue.entityType());
            }
            json.append(",\"message\":");
            appendJsonString(json, issue.message());
            json.append('}');
        }
        json.append(']');
    }

    private static void appendBoundingBoxJson(StringBuilder json, BoundingBox3 box) {
        if (box == null || box.isEmpty()) {
            json.append("null");
            return;
        }
        json.append("{\"min\":[")
                .append(box.minX()).append(',')
                .append(box.minY()).append(',')
                .append(box.minZ()).append("],\"max\":[")
                .append(box.maxX()).append(',')
                .append(box.maxY()).append(',')
                .append(box.maxZ()).append("]}");
    }

    private static void appendJsonString(StringBuilder json, String value) {
        json.append('"');
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"' -> json.append("\\\"");
                case '\\' -> json.append("\\\\");
                case '\b' -> json.append("\\b");
                case '\f' -> json.append("\\f");
                case '\n' -> json.append("\\n");
                case '\r' -> json.append("\\r");
                case '\t' -> json.append("\\t");
                default -> {
                    if (c < 0x20) {
                        json.append(String.format("\\u%04x", (int) c));
                    } else {
                        json.append(c);
                    }
                }
            }
        }
        json.append('"');
    }

    private record DumpFileResult(
            Path path,
            boolean success,
            StepFile stepFile,
            Map<Integer, StepEntity> resolved,
            List<String> buildLines,
            int unsupportedCount,
            BoundingBox3 bbox,
            List<MiniCadIssue> issues,
            String errorMessage,
            String stackTrace) {

        static DumpFileResult success(
                Path path,
                StepFile stepFile,
                Map<Integer, StepEntity> resolved,
                List<String> buildLines,
                int unsupportedCount,
                BoundingBox3 bbox,
                List<MiniCadIssue> issues) {
            return new DumpFileResult(
                    path,
                    true,
                    stepFile,
                    Map.copyOf(resolved),
                    List.copyOf(buildLines),
                    unsupportedCount,
                    bbox,
                    List.copyOf(issues),
                    null,
                    null);
        }

        static DumpFileResult failure(Path path, String message, Exception exception, boolean debug) {
            return new DumpFileResult(
                    path,
                    false,
                    null,
                    Map.of(),
                    List.of(),
                    0,
                    null,
                    List.of(failureIssue(exception, message)),
                    message,
                    debug ? StepDumpApp.stackTrace(exception) : null);
        }

        int entityCount() {
            return stepFile.entities().size();
        }

        int resolvedCount() {
            return resolved.size();
        }
    }

    private static MiniCadIssue failureIssue(Exception exception, String message) {
        String     code = null;
    switch (exception) {
        default:
            code = "step.failed";
            break;
    };
        return MiniCadIssue.error(code, null, null, message);
    }

    private static String stackTrace(Exception exception) {
        StringWriter writer = new StringWriter();
        exception.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    private static void appendSyntaxSummary(StepFile file, List<String> lines) {
        lines.add("Syntax Summary");
        lines.add("  entityCount: " + file.entities().size());
        if (!file.entities().isEmpty()) {
            lines.add("  firstId: #" + file.entities().get(0).id());
            lines.add("  lastId: #" + file.entities().get(file.entities().size() - 1).id());
        }
    }

    private static void appendSemanticSummary(Map<Integer, StepEntity> resolved, List<String> lines) {
        lines.add("Semantic Summary");
        Map<String, Integer> counts = new TreeMap<>();
        for (StepEntity entity : resolved.values()) {
            counts.merge(stepEntityTypeName(entity), 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            lines.add("  " + entry.getKey() + ": " + entry.getValue());
        }
    }

    private static String stepEntityTypeName(StepEntity entity) {
        if (entity instanceof com.minicad.step.model.topology.StepFaceBound faceBound) {
            return faceBound.outer() ? "FACE_OUTER_BOUND" : "FACE_BOUND";
        }
        if (entity instanceof StepAxis2Placement2D) {
            return "AXIS2_PLACEMENT_2D";
        }
        if (entity instanceof StepAxis2Placement3D) {
            return "AXIS2_PLACEMENT_3D";
        }
        if (entity instanceof StepOffsetCurve2D) {
            return "OFFSET_CURVE_2D";
        }
        if (entity instanceof StepOffsetCurve3D) {
            return "OFFSET_CURVE_3D";
        }
        if (entity instanceof com.minicad.step.model.workflow.StepRepresentation representation) {
            if (representation.entityName() != null
                    && !representation.entityName().isBlank()
                    && !"REPRESENTATION".equals(representation.entityName())
                    && !"SHAPE_REPRESENTATION".equals(representation.entityName())) {
                return representation.entityName();
            }
            if (representation.shapeRepresentation()) {
                return "SHAPE_REPRESENTATION";
            }
            return "REPRESENTATION";
        }
        // Attempt to get entityName via reflection; if the method doesn't exist, fall back to class name
        try {
            var method = entity.getClass().getMethod("entityName");
            Object value = method.invoke(entity);
            if (value instanceof String) { String name = (String) value;
                return name;
            }
        } catch (ReflectiveOperationException ignored) {
            // entityName method not present or not accessible, use class name fallback below
        }
        String simpleName = entity.getClass().getSimpleName();
        if (simpleName.startsWith("Step")) {
            simpleName = simpleName.substring(4);
        }
        return camelToUpperSnake(simpleName);
    }

    private static String camelToUpperSnake(String value) {
        if (value.isEmpty()) {
            return value;
        }
        String normalized = value
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                .replaceAll("([a-z0-9])([A-Z])", "$1_$2");
        return normalized.toUpperCase(java.util.Locale.ROOT);
    }

    private static void appendBuildSummary(Map<Integer, StepEntity> resolved, StepCadBuilder builder, List<String> lines) {
        lines.add("Build Summary");

        int openShells = 0;
        int closedShells = 0;
        int solids = 0;
        int booleanResults = 0;
        int standaloneFaceEntities = 0;
        int standaloneEdgeEntities = 0;
        int standaloneLoopEntities = 0;
        int standalonePathEntities = 0;
        int standaloneContainerEntities = 0;
        int unsupportedFaces = 0;
        int skipped2DEntities = 0;
        Map<String, Integer> unsupportedReasons = new LinkedHashMap<>();
        Map<String, Integer> unsupportedReasonCodes = new LinkedHashMap<>();
        Set<Integer> shellFaceIds = collectShellFaceIds(resolved.values());
        Set<Integer> loopOrientedEdgeIds = collectLoopOrientedEdgeIds(resolved.values());
        Set<Integer> orientedEdgeElementIds = collectOrientedEdgeElementIds(resolved.values());
        Set<Integer> faceBoundLoopIds = collectFaceBoundLoopIds(resolved.values());

        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepOpenShell) {
            StepOpenShell openShell = (StepOpenShell) entity;
                FaceBuildCounts counts = summarizeShell(openShell.faces(), builder);
                lines.add("  " + stepEntityTypeName(openShell) + " #" + openShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                openShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepSurfacedOpenShell) {
            StepSurfacedOpenShell surfacedOpenShell = (StepSurfacedOpenShell) entity;
                FaceBuildCounts counts = summarizeShell(surfacedOpenShell.faces(), builder);
                lines.add("  " + stepEntityTypeName(surfacedOpenShell) + " #" + surfacedOpenShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                openShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepClosedShell) {
            StepClosedShell closedShell = (StepClosedShell) entity;
                FaceBuildCounts counts = summarizeShell(closedShell.faces(), builder);
                lines.add("  " + stepEntityTypeName(closedShell) + " #" + closedShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                closedShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepOrientedOpenShell) {
            StepOrientedOpenShell orientedOpenShell = (StepOrientedOpenShell) entity;
                FaceBuildCounts counts = summarizeShell(orientedOpenShell.faces(), builder);
                lines.add("  " + stepEntityTypeName(orientedOpenShell) + " #" + orientedOpenShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                openShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepOrientedClosedShell) {
            StepOrientedClosedShell orientedClosedShell = (StepOrientedClosedShell) entity;
                FaceBuildCounts counts = summarizeShell(orientedClosedShell.faces(), builder);
                lines.add("  " + stepEntityTypeName(orientedClosedShell) + " #" + orientedClosedShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                closedShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepManifoldSolidBrep) {
            StepManifoldSolidBrep solidBrep = (StepManifoldSolidBrep) entity;
                FaceBuildCounts counts = summarizeShell(shellFaces(solidBrep.outer()), builder);
                lines.add("  " + stepEntityTypeName(solidBrep) + " #" + solidBrep.id() + ": shellFaces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                solids++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepBrepWithVoids) {
            StepBrepWithVoids brepWithVoids = (StepBrepWithVoids) entity;
                FaceBuildCounts counts = summarizeShell(shellFaces(brepWithVoids.outer()), builder);
                for (StepEntity voidShell : brepWithVoids.voids()) {
                    counts = counts.plus(summarizeShell(shellFaces(voidShell), builder));
                }
                lines.add("  " + stepEntityTypeName(brepWithVoids) + " #" + brepWithVoids.id() + ": shellFaces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                solids++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepSweptAreaSolid) {
            StepSweptAreaSolid sweptAreaSolid = (StepSweptAreaSolid) entity;
                try {
                    int faceCount = builder.buildSolid(sweptAreaSolid.id()).outerShell().faces().size();
                    lines.add("  " + stepEntityTypeName(sweptAreaSolid) + " #" + sweptAreaSolid.id() + ": shellFaces=" + faceCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException ex) {
                    Map<String, Integer> reasonCounts = Map.of(ex.getMessage(), 1);
                    Map<String, Integer> reasonCodeCounts = Map.of("unsupported_solid.swept_area", 1);
                    lines.add("  " + stepEntityTypeName(sweptAreaSolid) + " #" + sweptAreaSolid.id() + ": shellFaces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, reasonCounts);
                    appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                    unsupportedFaces++;
                    mergeReasonCounts(unsupportedReasons, reasonCounts);
                    mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
                }
                solids++;
            } else if (entity instanceof StepExtrudedFaceSolid) {
            StepExtrudedFaceSolid extrudedFaceSolid = (StepExtrudedFaceSolid) entity;
                try {
                    int faceCount = builder.buildSolid(extrudedFaceSolid.id()).outerShell().faces().size();
                    lines.add("  " + stepEntityTypeName(extrudedFaceSolid) + " #" + extrudedFaceSolid.id() + ": shellFaces=" + faceCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException ex) {
                    Map<String, Integer> reasonCounts = Map.of(ex.getMessage(), 1);
                    Map<String, Integer> reasonCodeCounts = Map.of("unsupported_solid.extruded_face", 1);
                    lines.add("  " + stepEntityTypeName(extrudedFaceSolid) + " #" + extrudedFaceSolid.id() + ": shellFaces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, reasonCounts);
                    appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                    unsupportedFaces++;
                    mergeReasonCounts(unsupportedReasons, reasonCounts);
                    mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
                }
                solids++;
            } else if (entity instanceof StepRevolvedFaceSolid) {
            StepRevolvedFaceSolid revolvedFaceSolid = (StepRevolvedFaceSolid) entity;
                try {
                    int faceCount = builder.buildSolid(revolvedFaceSolid.id()).outerShell().faces().size();
                    lines.add("  " + stepEntityTypeName(revolvedFaceSolid) + " #" + revolvedFaceSolid.id() + ": shellFaces=" + faceCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException ex) {
                    Map<String, Integer> reasonCounts = Map.of(ex.getMessage(), 1);
                    Map<String, Integer> reasonCodeCounts = Map.of("unsupported_solid.revolved_face", 1);
                    lines.add("  " + stepEntityTypeName(revolvedFaceSolid) + " #" + revolvedFaceSolid.id() + ": shellFaces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, reasonCounts);
                    appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                    unsupportedFaces++;
                    mergeReasonCounts(unsupportedReasons, reasonCounts);
                    mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
                }
                solids++;
            } else if (entity instanceof StepSolidReplica) {
            StepSolidReplica solidReplica = (StepSolidReplica) entity;
                try {
                    int faceCount = builder.buildSolid(solidReplica.id()).outerShell().faces().size();
                    lines.add("  " + stepEntityTypeName(solidReplica) + " #" + solidReplica.id() + ": shellFaces=" + faceCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException ex) {
                    Map<String, Integer> reasonCounts = Map.of(ex.getMessage(), 1);
                    Map<String, Integer> reasonCodeCounts = Map.of("unsupported_solid.replica", 1);
                    lines.add("  " + stepEntityTypeName(solidReplica) + " #" + solidReplica.id() + ": shellFaces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, reasonCounts);
                    appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                    unsupportedFaces++;
                    mergeReasonCounts(unsupportedReasons, reasonCounts);
                    mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
                }
                solids++;
            } else if (entity instanceof StepCsgSolid) {
            StepCsgSolid csgSolid = (StepCsgSolid) entity;
                try {
                    int faceCount = builder.buildSolid(csgSolid.id()).outerShell().faces().size();
                    lines.add("  " + stepEntityTypeName(csgSolid) + " #" + csgSolid.id() + ": shellFaces=" + faceCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException ex) {
                    Map<String, Integer> reasonCounts = Map.of(ex.getMessage(), 1);
                    Map<String, Integer> reasonCodeCounts = Map.of("unsupported_solid.csg", 1);
                    lines.add("  " + stepEntityTypeName(csgSolid) + " #" + csgSolid.id() + ": shellFaces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, reasonCounts);
                    appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                    unsupportedFaces++;
                    mergeReasonCounts(unsupportedReasons, reasonCounts);
                    mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
                }
                solids++;
            } else if (entity instanceof StepCsgPrimitive) {
            StepCsgPrimitive csgPrimitive = (StepCsgPrimitive) entity;
                try {
                    int faceCount = builder.buildSolid(csgPrimitive.id()).outerShell().faces().size();
                    lines.add("  " + stepEntityTypeName(csgPrimitive) + " #" + csgPrimitive.id() + ": shellFaces=" + faceCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException ex) {
                    Map<String, Integer> reasonCounts = Map.of(ex.getMessage(), 1);
                    Map<String, Integer> reasonCodeCounts = Map.of("unsupported_solid.csg_primitive", 1);
                    lines.add("  " + stepEntityTypeName(csgPrimitive) + " #" + csgPrimitive.id() + ": shellFaces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, reasonCounts);
                    appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                    unsupportedFaces++;
                    mergeReasonCounts(unsupportedReasons, reasonCounts);
                    mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
                }
                solids++;
            } else if (entity instanceof StepBooleanClippingResult) {
            StepBooleanClippingResult clippingResult = (StepBooleanClippingResult) entity;
                booleanResults++;
                try {
                    int faceCount = builder.buildSolid(clippingResult.id()).outerShell().faces().size();
                    lines.add("  " + stepEntityTypeName(clippingResult) + " #" + clippingResult.id() + ": faces=" + faceCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException ex) {
                    Map<String, Integer> reasonCounts = Map.of(ex.getMessage(), 1);
                    Map<String, Integer> reasonCodeCounts = Map.of("unsupported_boolean.clipping_result", 1);
                    lines.add("  " + stepEntityTypeName(clippingResult) + " #" + clippingResult.id() + ": faces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, reasonCounts);
                    appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                    unsupportedFaces++;
                    mergeReasonCounts(unsupportedReasons, reasonCounts);
                    mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
                }
            } else if (entity instanceof StepBooleanResult) {
            StepBooleanResult booleanResult = (StepBooleanResult) entity;
                booleanResults++;
                try {
                    int faceCount = builder.buildSolid(booleanResult.id()).outerShell().faces().size();
                    lines.add("  " + stepEntityTypeName(booleanResult) + " #" + booleanResult.id() + ": faces=" + faceCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException ex) {
                    Map<String, Integer> reasonCounts = Map.of(ex.getMessage(), 1);
                    Map<String, Integer> reasonCodeCounts = Map.of("unsupported_boolean.result", 1);
                    lines.add("  " + stepEntityTypeName(booleanResult) + " #" + booleanResult.id() + ": faces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, reasonCounts);
                    appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                    unsupportedFaces++;
                    mergeReasonCounts(unsupportedReasons, reasonCounts);
                    mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
                }
            } else if (entity instanceof StepFaceEntity } else if (entity instanceof StepFaceEntity face && !shellFaceIds.contains(face.id())) {} else if (entity instanceof StepFaceEntity face && !shellFaceIds.contains(face.id())) { !shellFaceIds.contains(((StepFaceEntity) entity).id())) { StepFaceEntity face = (StepFaceEntity) entity;
                standaloneFaceEntities++;
                try {
                    builder.buildFace(face.id());
                    lines.add("  " + stepEntityTypeName(face) + " #" + face.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(face) + " #" + face.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepOrientedEdge orientedEdge && !loopOrientedEdgeIds.contains(orientedEdge.id())) {
                standaloneEdgeEntities++;
                try {
                    builder.buildOrientedEdge(orientedEdge.id());
                    lines.add("  " + stepEntityTypeName(orientedEdge) + " #" + orientedEdge.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(orientedEdge) + " #" + orientedEdge.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepEdgeCurve edgeCurve && !orientedEdgeElementIds.contains(edgeCurve.id())) {
                standaloneEdgeEntities++;
                try {
                    builder.buildEdge(edgeCurve.id());
                    lines.add("  " + stepEntityTypeName(edgeCurve) + " #" + edgeCurve.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(edgeCurve) + " #" + edgeCurve.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepSubedge } else if (entity instanceof StepSubedge subedge && !orientedEdgeElementIds.contains(subedge.id())) {} else if (entity instanceof StepSubedge subedge && !orientedEdgeElementIds.contains(subedge.id())) { !orientedEdgeElementIds.contains(((StepSubedge) entity).id())) { StepSubedge subedge = (StepSubedge) entity;
                standaloneEdgeEntities++;
                try {
                    builder.buildEdge(subedge.id());
                    lines.add("  " + stepEntityTypeName(subedge) + " #" + subedge.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(subedge) + " #" + subedge.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepEdgeLoop edgeLoop && !faceBoundLoopIds.contains(edgeLoop.id())) {
                standaloneLoopEntities++;
                try {
                    builder.buildEdgeLoop(edgeLoop.id());
                    lines.add("  " + stepEntityTypeName(edgeLoop) + " #" + edgeLoop.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(edgeLoop) + " #" + edgeLoop.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepVertexLoop vertexLoop && !faceBoundLoopIds.contains(vertexLoop.id())) {
                standaloneLoopEntities++;
                try {
                    builder.buildVertexLoop(vertexLoop.id());
                    lines.add("  " + stepEntityTypeName(vertexLoop) + " #" + vertexLoop.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(vertexLoop) + " #" + vertexLoop.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepPolyLoop polyLoop && !faceBoundLoopIds.contains(polyLoop.id())) {
                standaloneLoopEntities++;
                try {
                    validatePolyLoop(polyLoop, builder);
                    lines.add("  " + stepEntityTypeName(polyLoop) + " #" + polyLoop.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(polyLoop) + " #" + polyLoop.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepPath) {
            StepPath path = (StepPath) entity;
                standalonePathEntities++;
                try {
                    validatePathEdges(path.edges(), builder);
                    lines.add("  " + stepEntityTypeName(path) + " #" + path.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(path) + " #" + path.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepOpenPath) {
            StepOpenPath openPath = (StepOpenPath) entity;
                standalonePathEntities++;
                try {
                    validatePathEdges(openPath.edges(), builder);
                    lines.add("  " + stepEntityTypeName(openPath) + " #" + openPath.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(openPath) + " #" + openPath.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepSubpath) {
            StepSubpath subpath = (StepSubpath) entity;
                standalonePathEntities++;
                try {
                    validatePathEdges(subpath.edges(), builder);
                    lines.add("  " + stepEntityTypeName(subpath) + " #" + subpath.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(subpath) + " #" + subpath.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepOrientedPath) {
            StepOrientedPath orientedPath = (StepOrientedPath) entity;
                standalonePathEntities++;
                try {
                    validatePathEdges(orientedPath.edges(), builder);
                    lines.add("  " + stepEntityTypeName(orientedPath) + " #" + orientedPath.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(orientedPath) + " #" + orientedPath.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepConnectedEdgeSet) {
            StepConnectedEdgeSet edgeSet = (StepConnectedEdgeSet) entity;
                standaloneContainerEntities++;
                try {
                    int edgeCount = validateConnectedEdgeSet(edgeSet, builder);
                    lines.add("  " + stepEntityTypeName(edgeSet) + " #" + edgeSet.id() + ": builtEdges=" + edgeCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(edgeSet) + " #" + edgeSet.id() + ": builtEdges=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepWireShell) {
            StepWireShell wireShell = (StepWireShell) entity;
                standaloneContainerEntities++;
                try {
                    int loopCount = validateWireShell(wireShell, builder);
                    lines.add("  " + stepEntityTypeName(wireShell) + " #" + wireShell.id() + ": builtLoops=" + loopCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(wireShell) + " #" + wireShell.id() + ": builtLoops=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) entity;
                standaloneContainerEntities++;
                try {
                    builder.buildVertexLoop(vertexShell.extent().id());
                    lines.add("  " + stepEntityTypeName(vertexShell) + " #" + vertexShell.id() + ": builtVertices=1, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(vertexShell) + " #" + vertexShell.id() + ": builtVertices=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepEdgeBasedWireframeModel) {
            StepEdgeBasedWireframeModel wireframeModel = (StepEdgeBasedWireframeModel) entity;
                standaloneContainerEntities++;
                try {
                    int edgeCount = 0;
                    for (StepConnectedEdgeSet boundary : wireframeModel.boundaries()) {
                        edgeCount += validateConnectedEdgeSet(boundary, builder);
                    }
                    lines.add("  " + stepEntityTypeName(wireframeModel) + " #" + wireframeModel.id() + ": builtEdges=" + edgeCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(wireframeModel) + " #" + wireframeModel.id() + ": builtEdges=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepShellBasedWireframeModel) {
            StepShellBasedWireframeModel wireframeModel = (StepShellBasedWireframeModel) entity;
                standaloneContainerEntities++;
                try {
                    int memberCount = validateShellBasedWireframeModel(wireframeModel, builder);
                    lines.add("  " + stepEntityTypeName(wireframeModel) + " #" + wireframeModel.id() + ": builtBoundaries=" + memberCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(wireframeModel) + " #" + wireframeModel.id() + ": builtBoundaries=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepFaceBasedSurfaceModel) {
            StepFaceBasedSurfaceModel surfaceModel = (StepFaceBasedSurfaceModel) entity;
                standaloneContainerEntities++;
                try {
                    FaceBuildCounts counts = validateFaceBasedSurfaceModel(surfaceModel, builder);
                    lines.add("  " + stepEntityTypeName(surfaceModel) + " #" + surfaceModel.id() + ": faces=" + counts.supportedFaces()
                            + ", unsupportedFaces=" + counts.unsupportedFaces());
                    appendUnsupportedReasons(lines, counts.unsupportedReasons());
                    appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                    unsupportedFaces += counts.unsupportedFaces();
                    mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                    mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(surfaceModel) + " #" + surfaceModel.id() + ": faces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepShellBasedSurfaceModel) {
            StepShellBasedSurfaceModel surfaceModel = (StepShellBasedSurfaceModel) entity;
                standaloneContainerEntities++;
                try {
                    FaceBuildCounts counts = validateShellBasedSurfaceModel(surfaceModel, builder);
                    lines.add("  " + stepEntityTypeName(surfaceModel) + " #" + surfaceModel.id() + ": faces=" + counts.supportedFaces()
                            + ", unsupportedFaces=" + counts.unsupportedFaces());
                    appendUnsupportedReasons(lines, counts.unsupportedReasons());
                    appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                    unsupportedFaces += counts.unsupportedFaces();
                    mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                    mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(surfaceModel) + " #" + surfaceModel.id() + ": faces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) entity;
                standaloneContainerEntities++;
                try {
                    int memberCount = validateGeometricCurveSet(curveSet, builder);
                    lines.add("  " + stepEntityTypeName(curveSet) + " #" + curveSet.id() + ": builtMembers=" + memberCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(curveSet) + " #" + curveSet.id() + ": builtMembers=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) entity;
                standaloneContainerEntities++;
                try {
                    int memberCount = validatePointSet(pointSet, builder);
                    lines.add("  " + stepEntityTypeName(pointSet) + " #" + pointSet.id() + ": builtMembers=" + memberCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(pointSet) + " #" + pointSet.id() + ": builtMembers=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) entity;
                standaloneContainerEntities++;
                try {
                    int memberCount = validateGeometricSet(geometricSet, builder);
                    lines.add("  " + stepEntityTypeName(geometricSet) + " #" + geometricSet.id() + ": builtMembers=" + memberCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(geometricSet) + " #" + geometricSet.id() + ": builtMembers=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepRepresentation) {
            StepRepresentation representation = (StepRepresentation) entity;
                standaloneContainerEntities++;
                try {
                    int itemCount = validateRepresentation(representation, builder);
                    lines.add("  " + stepEntityTypeName(representation) + " #" + representation.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    if (is2DPcurveEntity(entity)) {
                        standaloneContainerEntities--;
                        skipped2DEntities++;
                        continue;
                    }
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(representation) + " #" + representation.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepRepresentationMap) {
            StepRepresentationMap representationMap = (StepRepresentationMap) entity;
                standaloneContainerEntities++;
                try {
                    int itemCount = validateRepresentationMap(representationMap, builder);
                    lines.add("  " + stepEntityTypeName(representationMap) + " #" + representationMap.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(representationMap) + " #" + representationMap.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) entity;
                standaloneContainerEntities++;
                try {
                    int itemCount = validateMappedItem(mappedItem, builder);
                    lines.add("  " + stepEntityTypeName(mappedItem) + " #" + mappedItem.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(mappedItem) + " #" + mappedItem.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepStyledItem) {
            StepStyledItem styledItem = (StepStyledItem) entity;
                standaloneContainerEntities++;
                try {
                    int itemCount = validateStyledItem(styledItem, builder);
                    lines.add("  " + stepEntityTypeName(styledItem) + " #" + styledItem.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(styledItem) + " #" + styledItem.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepOverRidingStyledItem) {
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) entity;
                standaloneContainerEntities++;
                try {
                    int itemCount = validateOverridingStyledItem(styledItem, builder);
                    lines.add("  " + stepEntityTypeName(styledItem) + " #" + styledItem.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(styledItem) + " #" + styledItem.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepRepresentationRelationship) {
            StepRepresentationRelationship relationship = (StepRepresentationRelationship) entity;
                standaloneContainerEntities++;
                try {
                    int itemCount = validateRepresentationRelationship(relationship, builder);
                    lines.add("  " + stepEntityTypeName(relationship) + " #" + relationship.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(relationship) + " #" + relationship.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepRepresentationRelationshipWithTransformation) {
            StepRepresentationRelationshipWithTransformation relationship = (StepRepresentationRelationshipWithTransformation) entity;
                standaloneContainerEntities++;
                try {
                    int itemCount = validateRepresentationRelationshipWithTransformation(relationship, builder);
                    lines.add("  " + stepEntityTypeName(relationship) + " #" + relationship.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(relationship) + " #" + relationship.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepShapeRepresentationRelationship) {
            StepShapeRepresentationRelationship relationship = (StepShapeRepresentationRelationship) entity;
                standaloneContainerEntities++;
                try {
                    int itemCount = validateShapeRepresentationRelationship(relationship, builder);
                    lines.add("  " + stepEntityTypeName(relationship) + " #" + relationship.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(relationship) + " #" + relationship.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else {
                standaloneContainerEntities++;
                try {
                    int itemCount = validateSummaryEntity(entity, builder);
                    lines.add("  " + stepEntityTypeName(entity) + " #" + entity.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    if (isGenericDumpUnsupported(entity, reason)) {
                        standaloneContainerEntities--;
                        continue;
                    }
                    if (is2DPcurveEntity(entity)) {
                        standaloneContainerEntities--;
                        skipped2DEntities++;
                        continue;
                    }
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(entity) + " #" + entity.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                } catch (GeometryException | TopologyException | StepResolutionException ex) {
                    if (is2DPcurveEntity(entity)) {
                        standaloneContainerEntities--;
                        skipped2DEntities++;
                        continue;
                    }
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(entity) + " #" + entity.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            }
        }

        lines.add("  totals: openShells=" + openShells + ", closedShells=" + closedShells
                + ", solids=" + solids + ", booleanResults=" + booleanResults
                + ", standaloneFaceEntities=" + standaloneFaceEntities
                + ", standaloneEdgeEntities=" + standaloneEdgeEntities
                + ", standaloneLoopEntities=" + standaloneLoopEntities
                + ", standalonePathEntities=" + standalonePathEntities
                + ", standaloneContainerEntities=" + standaloneContainerEntities
                + ", skipped2DEntities=" + skipped2DEntities
                + ", unsupportedFaces=" + unsupportedFaces);
        appendUnsupportedReasons(lines, unsupportedReasons);
        appendUnsupportedReasonCodes(lines, unsupportedReasonCodes);
    }

    private static Set<Integer> collectShellFaceIds(Iterable<StepEntity> entities) {
        Set<Integer> ids = new HashSet<>();
        for (StepEntity entity : entities) {
            if (entity instanceof StepOpenShell) {
            StepOpenShell openShell = (StepOpenShell) entity;
                openShell.faces().forEach(face -> ids.add(face.id()));
            } else if (entity instanceof StepSurfacedOpenShell) {
            StepSurfacedOpenShell surfacedOpenShell = (StepSurfacedOpenShell) entity;
                surfacedOpenShell.faces().forEach(face -> ids.add(face.id()));
            } else if (entity instanceof StepOrientedOpenShell) {
            StepOrientedOpenShell orientedOpenShell = (StepOrientedOpenShell) entity;
                orientedOpenShell.faces().forEach(face -> ids.add(face.id()));
            } else if (entity instanceof StepClosedShell) {
            StepClosedShell closedShell = (StepClosedShell) entity;
                closedShell.faces().forEach(face -> ids.add(face.id()));
            } else if (entity instanceof StepOrientedClosedShell) {
            StepOrientedClosedShell orientedClosedShell = (StepOrientedClosedShell) entity;
                orientedClosedShell.faces().forEach(face -> ids.add(face.id()));
            }
        }
        return ids;
    }

    private static Set<Integer> collectLoopOrientedEdgeIds(Iterable<StepEntity> entities) {
        Set<Integer> ids = new HashSet<>();
        for (StepEntity entity : entities) {
            if (entity instanceof com.minicad.step.model.topology.StepEdgeLoop edgeLoop) {
                edgeLoop.edges().forEach(edge -> ids.add(edge.id()));
            }
        }
        return ids;
    }

    private static Set<Integer> collectOrientedEdgeElementIds(Iterable<StepEntity> entities) {
        Set<Integer> ids = new HashSet<>();
        for (StepEntity entity : entities) {
            if (entity instanceof StepOrientedEdge) {
            StepOrientedEdge orientedEdge = (StepOrientedEdge) entity;
                ids.add(orientedEdge.edgeElement().id());
            }
        }
        return ids;
    }

    private static Set<Integer> collectFaceBoundLoopIds(Iterable<StepEntity> entities) {
        Set<Integer> ids = new HashSet<>();
        for (StepEntity entity : entities) {
            if (entity instanceof StepFaceBound) {
            StepFaceBound faceBound = (StepFaceBound) entity;
                ids.add(faceBound.loop().id());
            }
        }
        return ids;
    }

    private static void validatePolyLoop(StepPolyLoop polyLoop, StepCadBuilder builder) {
        for (var point : polyLoop.polygon()) {
            builder.buildPoint(point.id());
        }
    }

    private static void validatePathEdges(List<StepOrientedEdge> edges, StepCadBuilder builder) {
        for (StepOrientedEdge edge : edges) {
            builder.buildOrientedEdge(edge.id());
        }
    }

    private static int validateConnectedEdgeSet(StepConnectedEdgeSet edgeSet, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity edge : edgeSet.edges()) {
            if (edge instanceof StepEdgeCurve) {
            StepEdgeCurve edgeCurve = (StepEdgeCurve) edge;
                builder.buildEdge(edgeCurve.id());
                count++;
            } else if (edge instanceof StepSubedge) {
            StepSubedge subedge = (StepSubedge) edge;
                builder.buildEdge(subedge.id());
                count++;
            } else if (edge instanceof StepOrientedEdge) {
            StepOrientedEdge orientedEdge = (StepOrientedEdge) edge;
                builder.buildOrientedEdge(orientedEdge.id());
                count++;
            } else {
                throw new UnsupportedGeometryException("CONNECTED_EDGE_SET requires EDGE_CURVE, SUBEDGE or ORIENTED_EDGE members");
            }
        }
        return count;
    }

    private static int validateWireShell(StepWireShell wireShell, StepCadBuilder builder) {
        int count = 0;
        for (var loop : wireShell.loops()) {
            if (loop instanceof StepEdgeLoop) {
            StepEdgeLoop edgeLoop = (StepEdgeLoop) loop;
                builder.buildEdgeLoop(edgeLoop.id());
            } else if (loop instanceof StepVertexLoop) {
            StepVertexLoop vertexLoop = (StepVertexLoop) loop;
                builder.buildVertexLoop(vertexLoop.id());
            } else if (loop instanceof StepPolyLoop) {
            StepPolyLoop polyLoop = (StepPolyLoop) loop;
                validatePolyLoop(polyLoop, builder);
            } else {
                throw new UnsupportedGeometryException("WIRE_SHELL requires EDGE_LOOP, VERTEX_LOOP or POLY_LOOP members");
            }
            count++;
        }
        return count;
    }

    private static int validateShellBasedWireframeModel(StepShellBasedWireframeModel wireframeModel, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity boundary : wireframeModel.boundaries()) {
            if (boundary instanceof StepWireShell) {
            StepWireShell wireShell = (StepWireShell) boundary;
                validateWireShell(wireShell, builder);
            } else if (boundary instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) boundary;
                builder.buildVertexLoop(vertexShell.extent().id());
            } else {
                throw new UnsupportedGeometryException("SHELL_BASED_WIREFRAME_MODEL requires WIRE_SHELL or VERTEX_SHELL boundaries");
            }
            count++;
        }
        return count;
    }

    private static FaceBuildCounts validateFaceBasedSurfaceModel(StepFaceBasedSurfaceModel surfaceModel, StepCadBuilder builder) {
        FaceBuildCounts counts = new FaceBuildCounts(0, 0, Map.of(), Map.of());
        for (StepEntity faceSet : surfaceModel.faceSets()) {
            if (faceSet instanceof StepConnectedFaceSet) {
            StepConnectedFaceSet connectedFaceSet = (StepConnectedFaceSet) faceSet;
                counts = counts.plus(summarizeShell(connectedFaceSet.faces(), builder));
            } else if (faceSet instanceof StepConnectedFaceSubSet) {
            StepConnectedFaceSubSet connectedFaceSubSet = (StepConnectedFaceSubSet) faceSet;
                counts = counts.plus(summarizeShell(connectedFaceSubSet.faces(), builder));
            } else if (faceSet instanceof StepOpenShell
                    || faceSet instanceof StepSurfacedOpenShell
                    || faceSet instanceof StepOrientedOpenShell
                    || faceSet instanceof StepClosedShell
                    || faceSet instanceof StepOrientedClosedShell) {
                counts = counts.plus(summarizeShell(shellFaces(faceSet), builder));
            } else {
                throw new UnsupportedGeometryException(
                        "FACE_BASED_SURFACE_MODEL requires CONNECTED_FACE_SET, CONNECTED_FACE_SUB_SET or shell members");
            }
        }
        return counts;
    }

    private static FaceBuildCounts validateShellBasedSurfaceModel(StepShellBasedSurfaceModel surfaceModel, StepCadBuilder builder) {
        FaceBuildCounts counts = new FaceBuildCounts(0, 0, Map.of(), Map.of());
        for (StepEntity shell : surfaceModel.shells()) {
            counts = counts.plus(summarizeShell(shellFaces(shell), builder));
        }
        return counts;
    }

    private static int validateGeometricCurveSet(StepGeometricCurveSet curveSet, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity element : curveSet.elements()) {
            if (element instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) element;
                builder.buildPoint(point.id());
            } else if (element instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) element;
                builder.buildVertex(vertexPoint.id());
            } else if (element instanceof StepGeometricReplica } else if (element instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName())) {} else if (element instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName())) { "POINT_REPLICA".equals(((StepGeometricReplica) element).entityName())) { StepGeometricReplica replica = (StepGeometricReplica) element;
                builder.buildPointReference(replica.id());
            } else if (element instanceof StepLine) {
            StepLine line = (StepLine) element;
                builder.buildLine(line.id());
            } else if (element instanceof StepCircle) {
            StepCircle circle = (StepCircle) element;
                builder.buildCircle(circle.id());
            } else if (element instanceof StepEllipse) {
            StepEllipse ellipse = (StepEllipse) element;
                builder.buildEllipse(ellipse.id());
            } else if (element instanceof StepPolyline) {
            StepPolyline polyline = (StepPolyline) element;
                builder.buildPolyline(polyline.id());
            } else if (element instanceof StepEdgeCurve) {
            StepEdgeCurve edgeCurve = (StepEdgeCurve) element;
                builder.buildEdge(edgeCurve.id());
            } else if (element instanceof StepSubedge) {
            StepSubedge subedge = (StepSubedge) element;
                builder.buildEdge(subedge.id());
            } else if (element instanceof StepOrientedEdge) {
            StepOrientedEdge orientedEdge = (StepOrientedEdge) element;
                builder.buildOrientedEdge(orientedEdge.id());
            } else if (element instanceof StepConnectedEdgeSet) {
            StepConnectedEdgeSet edgeSet = (StepConnectedEdgeSet) element;
                validateConnectedEdgeSet(edgeSet, builder);
            } else if (element instanceof StepEdgeLoop) {
            StepEdgeLoop edgeLoop = (StepEdgeLoop) element;
                validatePathEdges(edgeLoop.edges(), builder);
            } else if (element instanceof StepVertexLoop) {
            StepVertexLoop vertexLoop = (StepVertexLoop) element;
                builder.buildVertexLoop(vertexLoop.id());
            } else if (element instanceof StepPath) {
            StepPath path = (StepPath) element;
                validatePathEdges(path.edges(), builder);
            } else if (element instanceof StepOpenPath) {
            StepOpenPath openPath = (StepOpenPath) element;
                validatePathEdges(openPath.edges(), builder);
            } else if (element instanceof StepSubpath) {
            StepSubpath subpath = (StepSubpath) element;
                validatePathEdges(subpath.edges(), builder);
            } else if (element instanceof StepOrientedPath) {
            StepOrientedPath orientedPath = (StepOrientedPath) element;
                validatePathEdges(orientedPath.edges(), builder);
            } else if (element instanceof StepPolyLoop) {
            StepPolyLoop polyLoop = (StepPolyLoop) element;
                validatePolyLoop(polyLoop, builder);
            } else if (element instanceof StepWireShell) {
            StepWireShell wireShell = (StepWireShell) element;
                validateWireShell(wireShell, builder);
            } else if (element instanceof StepVertexShell
                    || element instanceof StepEdgeBasedWireframeModel
                    || element instanceof StepShellBasedWireframeModel) {
                validateSummaryEntity(element, builder);
            } else if (element instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) element;
                validatePointSet(pointSet, builder);
            } else if (element instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) element;
                validateGeometricSet(geometricSet, builder);
            } else if (element instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet nestedCurveSet = (StepGeometricCurveSet) element;
                validateGeometricCurveSet(nestedCurveSet, builder);
            } else {
                throw new UnsupportedGeometryException(
                        "GEOMETRIC_CURVE_SET requires supported curve, point, path, wire, topology or nested set members");
            }
            count++;
        }
        return count;
    }

    private static int validatePointSet(StepPointSet pointSet, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity point : pointSet.points()) {
            if (point instanceof StepCartesianPoint) {
            StepCartesianPoint cartesianPoint = (StepCartesianPoint) point;
                builder.buildPoint(cartesianPoint.id());
            } else if (point instanceof StepGeometricReplica } else if (point instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName())) {} else if (point instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName())) { "POINT_REPLICA".equals(((StepGeometricReplica) point).entityName())) { StepGeometricReplica replica = (StepGeometricReplica) point;
                builder.buildPointReference(replica.id());
            } else if (point instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) point;
                builder.buildVertex(vertexPoint.id());
            } else if (point instanceof StepVertexShell
                    || point instanceof StepAnnotationSymbol
                    || point instanceof StepAnnotationText
                    || point instanceof StepAnnotationTextCharacter
                    || point instanceof StepAnnotationFillArea
                    || point instanceof StepAnnotationPointOccurrence
                    || point instanceof StepAnnotationFillAreaOccurrence
                    || point instanceof StepAnnotationTextOccurrence
                    || point instanceof StepAnnotationPlaceholderOccurrence
                    || point instanceof StepAnnotationSymbolOccurrence
                    || point instanceof StepAnnotationSubfigureOccurrence
                    || point instanceof StepDraughtingAnnotationOccurrence
                    || point instanceof StepAnnotationPlane) {
                validateSummaryEntity(point, builder);
            } else if (point instanceof StepPointSet) {
            StepPointSet nestedPointSet = (StepPointSet) point;
                validatePointSet(nestedPointSet, builder);
            } else if (point instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) point;
                validateGeometricSet(geometricSet, builder);
            } else if (point instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) point;
                validateGeometricCurveSet(curveSet, builder);
            } else {
                throw new UnsupportedGeometryException(
                        "POINT_SET requires supported point carriers, point-like annotation content/occurrences or nested point containers");
            }
            count++;
        }
        return count;
    }

    private static int validateGeometricSet(StepGeometricSet geometricSet, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity element : geometricSet.elements()) {
            if (element instanceof StepCartesianPoint) {
            StepCartesianPoint cartesianPoint = (StepCartesianPoint) element;
                builder.buildPoint(cartesianPoint.id());
            } else if (element instanceof StepGeometricReplica } else if (element instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName())) {} else if (element instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName())) { "POINT_REPLICA".equals(((StepGeometricReplica) element).entityName())) { StepGeometricReplica replica = (StepGeometricReplica) element;
                builder.buildPointReference(replica.id());
            } else if (element instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) element;
                builder.buildVertex(vertexPoint.id());
            } else if (element instanceof StepLine) {
            StepLine line = (StepLine) element;
                builder.buildLine(line.id());
            } else if (element instanceof StepCircle) {
            StepCircle circle = (StepCircle) element;
                builder.buildCircle(circle.id());
            } else if (element instanceof StepEllipse) {
            StepEllipse ellipse = (StepEllipse) element;
                builder.buildEllipse(ellipse.id());
            } else if (element instanceof StepPolyline) {
            StepPolyline polyline = (StepPolyline) element;
                builder.buildPolyline(polyline.id());
            } else if (element instanceof StepEdgeCurve) {
            StepEdgeCurve edgeCurve = (StepEdgeCurve) element;
                builder.buildEdge(edgeCurve.id());
            } else if (element instanceof StepSubedge) {
            StepSubedge subedge = (StepSubedge) element;
                builder.buildEdge(subedge.id());
            } else if (element instanceof StepOrientedEdge) {
            StepOrientedEdge orientedEdge = (StepOrientedEdge) element;
                builder.buildOrientedEdge(orientedEdge.id());
            } else if (element instanceof StepConnectedEdgeSet) {
            StepConnectedEdgeSet edgeSet = (StepConnectedEdgeSet) element;
                validateConnectedEdgeSet(edgeSet, builder);
            } else if (element instanceof StepEdgeLoop) {
            StepEdgeLoop edgeLoop = (StepEdgeLoop) element;
                validatePathEdges(edgeLoop.edges(), builder);
            } else if (element instanceof StepVertexLoop) {
            StepVertexLoop vertexLoop = (StepVertexLoop) element;
                builder.buildVertexLoop(vertexLoop.id());
            } else if (element instanceof StepWireShell) {
            StepWireShell wireShell = (StepWireShell) element;
                validateWireShell(wireShell, builder);
            } else if (element instanceof StepOpenShell
                    || element instanceof StepSurfacedOpenShell
                    || element instanceof StepOrientedOpenShell
                    || element instanceof StepClosedShell
                    || element instanceof StepOrientedClosedShell
                    || element instanceof StepConnectedFaceSet
                    || element instanceof StepConnectedFaceSubSet
                    || element instanceof StepFaceBasedSurfaceModel
                    || element instanceof StepShellBasedSurfaceModel
                    || element instanceof StepEdgeBasedWireframeModel
                    || element instanceof StepShellBasedWireframeModel
                    || element instanceof StepManifoldSolidBrep
                    || element instanceof StepBrepWithVoids
                    || element instanceof StepSweptAreaSolid
                    || element instanceof StepExtrudedFaceSolid
                    || element instanceof StepRevolvedFaceSolid
                    || element instanceof StepSolidReplica
                    || element instanceof StepCsgSolid
                    || element instanceof StepCsgPrimitive
                    || element instanceof StepBooleanResult
                    || element instanceof StepBooleanClippingResult) {
                validateSummaryEntity(element, builder);
            } else if (element instanceof StepPath) {
            StepPath path = (StepPath) element;
                validatePathEdges(path.edges(), builder);
            } else if (element instanceof StepOpenPath) {
            StepOpenPath openPath = (StepOpenPath) element;
                validatePathEdges(openPath.edges(), builder);
            } else if (element instanceof StepSubpath) {
            StepSubpath subpath = (StepSubpath) element;
                validatePathEdges(subpath.edges(), builder);
            } else if (element instanceof StepOrientedPath) {
            StepOrientedPath orientedPath = (StepOrientedPath) element;
                validatePathEdges(orientedPath.edges(), builder);
            } else if (element instanceof StepPolyLoop) {
            StepPolyLoop polyLoop = (StepPolyLoop) element;
                validatePolyLoop(polyLoop, builder);
            } else if (isSupportedGeometricSetSurface(element)) {
                validateSupportedSurfaceReference(element, builder);
            } else if (element instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) element;
                validatePointSet(pointSet, builder);
            } else if (element instanceof StepGeometricSet) {
            StepGeometricSet nestedGeometricSet = (StepGeometricSet) element;
                validateGeometricSet(nestedGeometricSet, builder);
            } else if (element instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) element;
                validateGeometricCurveSet(curveSet, builder);
            } else {
                throw new UnsupportedGeometryException(
                        "GEOMETRIC_SET requires supported point, curve, surface, path, topology, shell/model/solid container or nested set members");
            }
            count++;
        }
        return count;
    }

    private static void validateSupportedSurfaceReference(StepEntity surface, StepCadBuilder builder) {
        if (surface instanceof StepPlane) {
            StepPlane plane = (StepPlane) surface;
            builder.buildPlane(plane.id());
        } else if (surface instanceof StepCylindricalSurface) {
            StepCylindricalSurface cylindricalSurface = (StepCylindricalSurface) surface;
            builder.buildCylindricalSurface(cylindricalSurface.id());
        } else if (surface instanceof StepConicalSurface) {
            StepConicalSurface conicalSurface = (StepConicalSurface) surface;
            builder.buildConicalSurface(conicalSurface.id());
        } else if (surface instanceof StepSphericalSurface) {
            StepSphericalSurface sphericalSurface = (StepSphericalSurface) surface;
            builder.buildSphericalSurface(sphericalSurface.id());
        } else if (surface instanceof StepToroidalSurface) {
            StepToroidalSurface toroidalSurface = (StepToroidalSurface) surface;
            builder.buildToroidalSurface(toroidalSurface.id());
        } else if (surface instanceof StepDegenerateToroidalSurface) {
            StepDegenerateToroidalSurface degenerateToroidalSurface = (StepDegenerateToroidalSurface) surface;
            builder.buildDegenerateToroidalSurface(degenerateToroidalSurface.id());
        } else if (surface instanceof StepSurfaceOfLinearExtrusion) {
            StepSurfaceOfLinearExtrusion extrusionSurface = (StepSurfaceOfLinearExtrusion) surface;
            builder.buildSurfaceOfLinearExtrusion(extrusionSurface.id());
        } else if (surface instanceof StepSurfaceOfRevolution) {
            StepSurfaceOfRevolution revolutionSurface = (StepSurfaceOfRevolution) surface;
            builder.buildSurfaceOfRevolution(revolutionSurface.id());
        } else if (surface instanceof StepBSplineSurfaceWithKnots) {
            StepBSplineSurfaceWithKnots splineSurface = (StepBSplineSurfaceWithKnots) surface;
            builder.buildBSplineSurface(splineSurface.id());
        } else if (surface instanceof StepRationalBSplineSurface) {
            StepRationalBSplineSurface rationalSplineSurface = (StepRationalBSplineSurface) surface;
            builder.buildRationalBSplineSurface(rationalSplineSurface.id());
        } else if (surface instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) surface;
            builder.buildRectangularTrimmedSurface(trimmedSurface.id());
        } else if (surface instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) surface;
            builder.buildCurveBoundedSurface(boundedSurface.id());
        } else if (surface instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) surface;
            builder.buildOrientedSurface(orientedSurface.id());
        } else if (surface instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) surface;
            builder.buildOffsetSurface(offsetSurface.id());
        } else if (surface instanceof StepGeometricReplica } else if (surface instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName())) {} else if (surface instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName())) { "SURFACE_REPLICA".equals(((StepGeometricReplica) surface).entityName())) { StepGeometricReplica replica = (StepGeometricReplica) surface;
            builder.buildSurfaceReplica(replica.id());
        } else {
            throw new UnsupportedGeometryException(
                    "GEOMETRIC_SET requires supported point, curve, surface, path, topology, shell/model/solid container or nested set members");
        }
    }

    private static boolean isSupportedGeometricSetSurface(StepEntity surface) {
        return surface instanceof StepPlane
                || surface instanceof StepCylindricalSurface
                || surface instanceof StepConicalSurface
                || surface instanceof StepSphericalSurface
                || surface instanceof StepToroidalSurface
                || surface instanceof StepDegenerateToroidalSurface
                || surface instanceof StepSurfaceOfLinearExtrusion
                || surface instanceof StepSurfaceOfRevolution
                || surface instanceof StepBSplineSurfaceWithKnots
                || surface instanceof StepRationalBSplineSurface
                || surface instanceof StepRectangularTrimmedSurface
                || surface instanceof StepCurveBoundedSurface
                || surface instanceof StepOrientedSurface
                || surface instanceof StepOffsetSurface
                || surface instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName());
    }

    private static int validateRepresentation(StepRepresentation representation, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity item : representation.items()) {
            count += validateSummaryEntity(item, builder);
        }
        return count;
    }

    private static int validateRepresentationMap(StepRepresentationMap representationMap, StepCadBuilder builder) {
        validateSummaryEntity(representationMap.mappedOrigin(), builder);
        return validateRepresentation(representationMap.mappedRepresentation(), builder);
    }

    private static int validateMappedItem(StepMappedItem mappedItem, StepCadBuilder builder) {
        int count = validateRepresentationMap(mappedItem.mappingSource(), builder);
        validateSummaryEntity(mappedItem.mappingTarget(), builder);
        return count;
    }

    private static int validateStyledItem(StepStyledItem styledItem, StepCadBuilder builder) {
        return validateSummaryEntity(styledItem.item(), builder);
    }

    private static int validateOverridingStyledItem(StepOverRidingStyledItem styledItem, StepCadBuilder builder) {
        int count = validateSummaryEntity(styledItem.item(), builder);
        validateStyledItem(styledItem.overRiddenStyle(), builder);
        return count;
    }

    private static int validateRepresentationRelationship(StepRepresentationRelationship relationship, StepCadBuilder builder) {
        return validateRepresentation(relationship.rep1(), builder)
                + validateRepresentation(relationship.rep2(), builder);
    }

    private static int validateRepresentationRelationshipWithTransformation(
            StepRepresentationRelationshipWithTransformation relationship,
            StepCadBuilder builder
    ) {
        int count = validateRepresentation(relationship.rep1(), builder)
                + validateRepresentation(relationship.rep2(), builder);
        validateSummaryEntity(relationship.transformationOperator(), builder);
        return count;
    }

    private static int validateShapeRepresentationRelationship(
            StepShapeRepresentationRelationship relationship,
            StepCadBuilder builder
    ) {
        return validateRepresentation(relationship.rep1(), builder)
                + validateRepresentation(relationship.rep2(), builder);
    }

    private static int validateAnnotationCurveOccurrence(StepEntity item, StepCadBuilder builder) {
        return validateSummaryEntity(item, builder);
    }

    private static int validateAnnotationFillArea(StepAnnotationFillArea fillArea, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity boundary : fillArea.boundaries()) {
            count += validateSummaryEntity(boundary, builder);
        }
        return count;
    }

    private static int validateAnnotationPlane(StepAnnotationPlane plane, StepCadBuilder builder) {
        int count = validateSummaryEntity(plane.item(), builder);
        for (StepEntity element : plane.elements()) {
            count += validateSummaryEntity(element, builder);
        }
        return count;
    }

    private static int validateDraughtingCallout(StepDraughtingCallout callout, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity content : callout.contents()) {
            count += validateSummaryEntity(content, builder);
        }
        return count;
    }

    private static int validatePresentationStyleAssignment(StepPresentationStyleAssignment assignment, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity style : assignment.styles()) {
            count += validateSummaryEntity(style, builder);
        }
        return count;
    }

    private static int validateCurveStyle(StepCurveStyle curveStyle, StepCadBuilder builder) {
        return validateSummaryEntity(curveStyle.curveFont(), builder)
                + validateSummaryEntity(curveStyle.colour(), builder);
    }

    private static int validateFillAreaStyle(StepFillAreaStyle fillAreaStyle, StepCadBuilder builder) {
        int count = 0;
        for (StepFillAreaStyleColour style : fillAreaStyle.styles()) {
            count += validateSummaryEntity(style, builder);
        }
        return count;
    }

    private static int validateSurfaceSideStyle(StepSurfaceSideStyle style, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity item : style.styles()) {
            count += validateSummaryEntity(item, builder);
        }
        return count;
    }

    private static int validateSummaryItems(List<StepEntity> items, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity item : items) {
            count += validateSummaryEntity(item, builder);
        }
        return count;
    }

    private static int validateRepresentationUsage(
            StepEntity definition,
            StepRepresentation usedRepresentation,
            StepEntity identifiedItem,
            StepCadBuilder builder
    ) {
        return validateSummaryEntity(definition, builder)
                + validateSummaryEntity(usedRepresentation, builder)
                + validateSummaryEntity(identifiedItem, builder);
    }

    private static int validateChainBasedRepresentationUsage(
            StepEntity definition,
            List<StepRepresentation> nodes,
            List<StepRepresentationRelationship> undirectedLinks,
            StepEntity identifiedItem,
            StepCadBuilder builder
    ) {
        int count = validateSummaryEntity(definition, builder)
                + validateSummaryEntity(identifiedItem, builder);
        for (StepRepresentation node : nodes) {
            count += validateSummaryEntity(node, builder);
        }
        for (StepRepresentationRelationship link : undirectedLinks) {
            count += validateSummaryEntity(link, builder);
        }
        return count;
    }

    private static boolean isGenericDumpUnsupported(StepEntity entity, String reason) {
        return reason.equals(stepEntityTypeName(entity) + " dump validation is unsupported");
    }

    /**
     * Returns true if the entity is a 2D/pcurve/semantic support type that should
     * not count toward unsupportedFaces when validation fails. These include 2D
     * curves in surface parameter space, pcurves, and non-geometry support
     * entities (directions, vectors, representation contexts).
     */
    private static boolean is2DPcurveEntity(StepEntity entity) {
        return entity instanceof StepPcurve
                || entity instanceof StepDegeneratePcurve
                || entity instanceof StepOffsetCurve2D
                || entity instanceof StepAxis2Placement2D
                || entity instanceof com.minicad.step.model.geometry.StepCurve2D
                || entity instanceof com.minicad.step.model.geometry.StepCircle2D
                || entity instanceof com.minicad.step.model.geometry.StepEllipse2D
                || entity instanceof com.minicad.step.model.geometry.StepHyperbola2D
                || entity instanceof com.minicad.step.model.geometry.StepParabola2D
                || entity instanceof com.minicad.step.model.geometry.StepTrimmedCurve2D
                || entity instanceof StepTrimmedCurve
                || entity instanceof StepRepresentation
                || entity instanceof StepDirection
                || entity instanceof StepVector
                || entity instanceof StepRepresentationContext;
    }

    private static int validateSummaryEntity(StepEntity entity, StepCadBuilder builder) {
        if (entity instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) entity;
            validatePoint(point, builder);
            return 1;
        }
        if (entity instanceof StepDirection) {
            StepDirection direction = (StepDirection) entity;
            validateDirection(direction, builder);
            return 1;
        }
        if (entity instanceof StepVector) {
            StepVector vector = (StepVector) entity;
            builder.buildVector(vector.id());
            return 1;
        }
        if (entity instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) entity;
            builder.buildVertex(vertexPoint.id());
            return 1;
        }
        if (entity instanceof StepConicCurve) {
            StepConicCurve conicCurve = (StepConicCurve) entity;
            return validateSummaryEntity(conicCurve.position(), builder);
        }
        if (entity instanceof StepLine) {
            StepLine line = (StepLine) entity;
            validateLine(line, builder);
            return 1;
        }
        if (entity instanceof StepCircle) {
            StepCircle circle = (StepCircle) entity;
            validateCircle(circle, builder);
            return 1;
        }
        if (entity instanceof StepEllipse) {
            StepEllipse ellipse = (StepEllipse) entity;
            validateEllipse(ellipse, builder);
            return 1;
        }
        if (entity instanceof StepPolyline) {
            StepPolyline polyline = (StepPolyline) entity;
            validatePolyline(polyline, builder);
            return 1;
        }
        if (entity instanceof StepBSplineCurveWithKnots) {
            StepBSplineCurveWithKnots splineCurve = (StepBSplineCurveWithKnots) entity;
            builder.buildBSplineCurve(splineCurve.id());
            return 1;
        }
        if (entity instanceof StepRationalBSplineCurve) {
            StepRationalBSplineCurve splineCurve = (StepRationalBSplineCurve) entity;
            builder.buildRationalBSplineCurve(splineCurve.id());
            return 1;
        }
        if (entity instanceof StepTrimmedCurve) {
            StepTrimmedCurve trimmedCurve = (StepTrimmedCurve) entity;
            builder.buildTrimmedCurve(trimmedCurve.id());
            return 1;
        }
        if (entity instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) entity;
            builder.buildSurfaceCurve(surfaceCurve.id());
            return 1;
        }
        if (entity instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) entity;
            builder.buildSeamCurve(seamCurve.id());
            return 1;
        }
        if (entity instanceof StepCompositeCurve) {
            StepCompositeCurve compositeCurve = (StepCompositeCurve) entity;
            builder.buildCompositeCurve(compositeCurve.id());
            return 1;
        }
        if (entity instanceof StepCompositeCurveOnSurface) {
            StepCompositeCurveOnSurface compositeCurveOnSurface = (StepCompositeCurveOnSurface) entity;
            builder.buildCompositeCurve(compositeCurveOnSurface.id());
            return 1;
        }
        if (entity instanceof StepCompositeCurveSegment) {
            StepCompositeCurveSegment segment = (StepCompositeCurveSegment) entity;
            return validateSummaryEntity(segment.parentCurve(), builder);
        }
        if (entity instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D offsetCurve2D = (StepOffsetCurve2D) entity;
            builder.buildOffsetCurve2(offsetCurve2D.id());
            return 1;
        }
        if (entity instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) entity;
            builder.buildOffsetCurve3(offsetCurve3D.id());
            return 1;
        }
        if (entity instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) entity;
            builder.buildCurveReference3(orientedCurve.id());
            return validateSummaryEntity(orientedCurve.curveElement(), builder);
        }
        if (entity instanceof StepPcurve) {
            StepPcurve pcurve = (StepPcurve) entity;
            builder.buildPcurve2(pcurve.id());
            return 1;
        }
        if (entity instanceof StepDegeneratePcurve) {
            StepDegeneratePcurve degeneratePcurve = (StepDegeneratePcurve) entity;
            builder.buildPcurve2(degeneratePcurve.id());
            return validateSummaryEntity(degeneratePcurve.basisSurface(), builder)
                    + validateSummaryEntity(degeneratePcurve.referenceToCurve(), builder);
        }
        if (entity instanceof StepGeometricReplica) {
            StepGeometricReplica replica = (StepGeometricReplica) entity;
            if ("POINT_REPLICA".equals(replica.entityName())) {
                builder.buildPointReference(replica.id());
            }
            if ("CURVE_REPLICA".equals(replica.entityName())) {
                builder.buildCurveReference3(replica.id());
            }
            if ("SURFACE_REPLICA".equals(replica.entityName())) {
                builder.buildSurfaceReplica(replica.id());
            }
            return validateSummaryEntity(replica.parent(), builder)
                    + validateSummaryEntity(replica.transformation(), builder);
        }
        if (entity instanceof StepPlane) {
            StepPlane plane = (StepPlane) entity;
            builder.buildPlane(plane.id());
            return 1;
        }
        if (entity instanceof StepCylindricalSurface) {
            StepCylindricalSurface cylindricalSurface = (StepCylindricalSurface) entity;
            builder.buildCylindricalSurface(cylindricalSurface.id());
            return 1;
        }
        if (entity instanceof StepConicalSurface) {
            StepConicalSurface conicalSurface = (StepConicalSurface) entity;
            builder.buildConicalSurface(conicalSurface.id());
            return 1;
        }
        if (entity instanceof StepToroidalSurface) {
            StepToroidalSurface toroidalSurface = (StepToroidalSurface) entity;
            builder.buildToroidalSurface(toroidalSurface.id());
            return 1;
        }
        if (entity instanceof StepSphericalSurface) {
            StepSphericalSurface sphericalSurface = (StepSphericalSurface) entity;
            builder.buildSphericalSurface(sphericalSurface.id());
            return 1;
        }
        if (entity instanceof StepDegenerateToroidalSurface) {
            StepDegenerateToroidalSurface degenerateToroidalSurface = (StepDegenerateToroidalSurface) entity;
            builder.buildDegenerateToroidalSurface(degenerateToroidalSurface.id());
            return 1;
        }
        if (entity instanceof StepBSplineSurfaceWithKnots) {
            StepBSplineSurfaceWithKnots splineSurface = (StepBSplineSurfaceWithKnots) entity;
            builder.buildBSplineSurface(splineSurface.id());
            return 1;
        }
        if (entity instanceof StepRationalBSplineSurface) {
            StepRationalBSplineSurface rationalSplineSurface = (StepRationalBSplineSurface) entity;
            builder.buildRationalBSplineSurface(rationalSplineSurface.id());
            return 1;
        }
        if (entity instanceof StepSurfaceOfLinearExtrusion) {
            StepSurfaceOfLinearExtrusion extrusionSurface = (StepSurfaceOfLinearExtrusion) entity;
            builder.buildSurfaceOfLinearExtrusion(extrusionSurface.id());
            return validateSummaryEntity(extrusionSurface.sweptCurve(), builder)
                    + validateSummaryEntity(extrusionSurface.extrusionAxis(), builder);
        }
        if (entity instanceof StepSurfaceOfRevolution) {
            StepSurfaceOfRevolution revolutionSurface = (StepSurfaceOfRevolution) entity;
            builder.buildSurfaceOfRevolution(revolutionSurface.id());
            return validateSummaryEntity(revolutionSurface.sweptCurve(), builder)
                    + validateSummaryEntity(revolutionSurface.axisPosition(), builder);
        }
        if (entity instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) entity;
            builder.buildRectangularTrimmedSurface(trimmedSurface.id());
            return validateSummaryEntity(trimmedSurface.basisSurface(), builder);
        }
        if (entity instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) entity;
            builder.buildCurveBoundedSurface(boundedSurface.id());
            return validateSummaryEntity(boundedSurface.basisSurface(), builder)
                    + validateSummaryItems(boundedSurface.boundaries(), builder);
        }
        if (entity instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) entity;
            builder.buildOrientedSurface(orientedSurface.id());
            return validateSummaryEntity(orientedSurface.surfaceElement(), builder);
        }
        if (entity instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) entity;
            builder.buildOffsetSurface(offsetSurface.id());
            return validateSummaryEntity(offsetSurface.basisSurface(), builder);
        }
        if (entity instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D placement3D = (StepAxis2Placement3D) entity;
            builder.buildPlacement(placement3D.id());
            return 1;
        }
        if (entity instanceof StepAxis1Placement) {
            StepAxis1Placement axis1Placement = (StepAxis1Placement) entity;
            builder.buildAxis1Placement(axis1Placement.id());
            return 1;
        }
        if (entity instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D placement2D = (StepAxis2Placement2D) entity;
            validatePoint(placement2D.location(), builder);
            validateDirection(placement2D.refDirection(), builder);
            return 1;
        }
        if (entity instanceof StepCartesianTransformationOperator) {
            StepCartesianTransformationOperator transformation = (StepCartesianTransformationOperator) entity;
            if (transformation.axis1() != null) {
                validateDirection(transformation.axis1(), builder);
            }
            if (transformation.axis2() != null) {
                validateDirection(transformation.axis2(), builder);
            }
            if (transformation.axis3() != null) {
                validateDirection(transformation.axis3(), builder);
            }
            validatePoint(transformation.localOrigin(), builder);
            return 1;
        }
        if (entity instanceof StepItemDefinedTransformation) {
            StepItemDefinedTransformation transformation = (StepItemDefinedTransformation) entity;
            builder.buildPlacement(transformation.transformItem1().id());
            builder.buildPlacement(transformation.transformItem2().id());
            return 1;
        }
        if (entity instanceof StepEdgeCurve) {
            StepEdgeCurve edgeCurve = (StepEdgeCurve) entity;
            builder.buildEdge(edgeCurve.id());
            return 1;
        }
        if (entity instanceof StepSubedge) {
            StepSubedge subedge = (StepSubedge) entity;
            builder.buildEdge(subedge.id());
            return 1;
        }
        if (entity instanceof StepOrientedEdge) {
            StepOrientedEdge orientedEdge = (StepOrientedEdge) entity;
            builder.buildOrientedEdge(orientedEdge.id());
            return 1;
        }
        if (entity instanceof StepEdgeLoop) {
            StepEdgeLoop edgeLoop = (StepEdgeLoop) entity;
            builder.buildEdgeLoop(edgeLoop.id());
            return 1;
        }
        if (entity instanceof StepVertexLoop) {
            StepVertexLoop vertexLoop = (StepVertexLoop) entity;
            builder.buildVertexLoop(vertexLoop.id());
            return 1;
        }
        if (entity instanceof StepPolyLoop) {
            StepPolyLoop polyLoop = (StepPolyLoop) entity;
            validatePolyLoop(polyLoop, builder);
            return 1;
        }
        if (entity instanceof StepPath) {
            StepPath path = (StepPath) entity;
            validatePathEdges(path.edges(), builder);
            return 1;
        }
        if (entity instanceof StepOpenPath) {
            StepOpenPath openPath = (StepOpenPath) entity;
            validatePathEdges(openPath.edges(), builder);
            return 1;
        }
        if (entity instanceof StepSubpath) {
            StepSubpath subpath = (StepSubpath) entity;
            validatePathEdges(subpath.edges(), builder);
            return 1;
        }
        if (entity instanceof StepOrientedPath) {
            StepOrientedPath orientedPath = (StepOrientedPath) entity;
            validatePathEdges(orientedPath.edges(), builder);
            return 1;
        }
        if (entity instanceof StepConnectedEdgeSet) {
            StepConnectedEdgeSet edgeSet = (StepConnectedEdgeSet) entity;
            return validateConnectedEdgeSet(edgeSet, builder);
        }
        if (entity instanceof StepWireShell) {
            StepWireShell wireShell = (StepWireShell) entity;
            return validateWireShell(wireShell, builder);
        }
        if (entity instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) entity;
            builder.buildVertexLoop(vertexShell.extent().id());
            return 1;
        }
        if (entity instanceof StepEdgeBasedWireframeModel) {
            StepEdgeBasedWireframeModel wireframeModel = (StepEdgeBasedWireframeModel) entity;
            int count = 0;
            for (StepConnectedEdgeSet boundary : wireframeModel.boundaries()) {
                count += validateConnectedEdgeSet(boundary, builder);
            }
            return count;
        }
        if (entity instanceof StepShellBasedWireframeModel) {
            StepShellBasedWireframeModel wireframeModel = (StepShellBasedWireframeModel) entity;
            return validateShellBasedWireframeModel(wireframeModel, builder);
        }
        if (entity instanceof StepFaceEntity) {
            StepFaceEntity face = (StepFaceEntity) entity;
            builder.buildFace(face.id());
            return 1;
        }
        if (entity instanceof StepFaceBasedSurfaceModel) {
            StepFaceBasedSurfaceModel surfaceModel = (StepFaceBasedSurfaceModel) entity;
            return validateFaceBasedSurfaceModel(surfaceModel, builder).supportedFaces();
        }
        if (entity instanceof StepShellBasedSurfaceModel) {
            StepShellBasedSurfaceModel surfaceModel = (StepShellBasedSurfaceModel) entity;
            return validateShellBasedSurfaceModel(surfaceModel, builder).supportedFaces();
        }
        if (entity instanceof StepConnectedFaceSet) {
            StepConnectedFaceSet connectedFaceSet = (StepConnectedFaceSet) entity;
            return summarizeShell(connectedFaceSet.faces(), builder).supportedFaces();
        }
        if (entity instanceof StepConnectedFaceSubSet) {
            StepConnectedFaceSubSet connectedFaceSubSet = (StepConnectedFaceSubSet) entity;
            return summarizeShell(connectedFaceSubSet.faces(), builder).supportedFaces();
        }
        if (entity instanceof StepOpenShell
                || entity instanceof StepSurfacedOpenShell
                || entity instanceof StepOrientedOpenShell
                || entity instanceof StepClosedShell
                || entity instanceof StepOrientedClosedShell) {
            return builder.buildShell(entity.id()).faces().size();
        }
        if (entity instanceof StepManifoldSolidBrep
                || entity instanceof StepBrepWithVoids
                || entity instanceof StepSweptAreaSolid
                || entity instanceof StepExtrudedFaceSolid
                || entity instanceof StepRevolvedFaceSolid
                || entity instanceof StepSolidReplica
                || entity instanceof StepCsgSolid
                || entity instanceof StepCsgPrimitive
                || entity instanceof StepBooleanResult
                || entity instanceof StepBooleanClippingResult) {
            return builder.buildSolid(entity.id()).outerShell().faces().size();
        }
        if (entity instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) entity;
            return validatePointSet(pointSet, builder);
        }
        if (entity instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) entity;
            return validateGeometricCurveSet(curveSet, builder);
        }
        if (entity instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) entity;
            return validateGeometricSet(geometricSet, builder);
        }
        if (entity instanceof StepBoxDomain) {
            StepBoxDomain boxDomain = (StepBoxDomain) entity;
            return validateSummaryEntity(boxDomain.corner(), builder);
        }
        if (entity instanceof StepHalfSpaceSolid) {
            StepHalfSpaceSolid halfSpaceSolid = (StepHalfSpaceSolid) entity;
            int count = validateSummaryEntity(halfSpaceSolid.baseSurface(), builder);
            if (halfSpaceSolid.enclosure() != null) {
                count += validateSummaryEntity(halfSpaceSolid.enclosure(), builder);
            }
            return count;
        }
        if (entity instanceof StepProfileDef) {
            StepProfileDef profileDef = (StepProfileDef) entity;
            int count = 0;
            if (profileDef.position() != null) {
                count += validateSummaryEntity(profileDef.position(), builder);
            }
            count += validateSummaryItems(profileDef.curves(), builder);
            return Math.max(1, count);
        }
        if (entity instanceof StepRepresentation) {
            StepRepresentation representation = (StepRepresentation) entity;
            return validateRepresentation(representation, builder);
        }
        if (entity instanceof StepRepresentationMap) {
            StepRepresentationMap representationMap = (StepRepresentationMap) entity;
            return validateRepresentationMap(representationMap, builder);
        }
        if (entity instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) entity;
            return validateMappedItem(mappedItem, builder);
        }
        if (entity instanceof StepStyledItem) {
            StepStyledItem styledItem = (StepStyledItem) entity;
            return validateStyledItem(styledItem, builder);
        }
        if (entity instanceof StepOverRidingStyledItem) {
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) entity;
            return validateOverridingStyledItem(styledItem, builder);
        }
        if (entity instanceof StepRepresentationRelationship) {
            StepRepresentationRelationship relationship = (StepRepresentationRelationship) entity;
            return validateRepresentationRelationship(relationship, builder);
        }
        if (entity instanceof StepRepresentationRelationshipWithTransformation) {
            StepRepresentationRelationshipWithTransformation relationship = (StepRepresentationRelationshipWithTransformation) entity;
            return validateRepresentationRelationshipWithTransformation(relationship, builder);
        }
        if (entity instanceof StepShapeRepresentationRelationship) {
            StepShapeRepresentationRelationship relationship = (StepShapeRepresentationRelationship) entity;
            return validateShapeRepresentationRelationship(relationship, builder);
        }
        if (entity instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence annotationCurveOccurrence = (StepAnnotationCurveOccurrence) entity;
            return validateAnnotationCurveOccurrence(annotationCurveOccurrence.item(), builder);
        }
        if (entity instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) entity;
            return validateSummaryEntity(annotationOccurrence.item(), builder);
        }
        if (entity instanceof StepLeaderCurve) {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) entity;
            return validateAnnotationCurveOccurrence(leaderCurve.item(), builder);
        }
        if (entity instanceof StepDimensionCurve) {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) entity;
            return validateAnnotationCurveOccurrence(dimensionCurve.item(), builder);
        }
        if (entity instanceof StepProjectionCurve) {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) entity;
            return validateAnnotationCurveOccurrence(projectionCurve.item(), builder);
        }
        if (entity instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) entity;
            return validateAnnotationFillArea(fillArea, builder);
        }
        if (entity instanceof StepAnnotationFillAreaOccurrence) {
            StepAnnotationFillAreaOccurrence fillAreaOccurrence = (StepAnnotationFillAreaOccurrence) entity;
            return validateAnnotationFillArea(fillAreaOccurrence.item(), builder)
                    + validateSummaryEntity(fillAreaOccurrence.fillStyleTarget(), builder);
        }
        if (entity instanceof StepAnnotationPlaceholderOccurrence) {
            StepAnnotationPlaceholderOccurrence placeholderOccurrence = (StepAnnotationPlaceholderOccurrence) entity;
            return validateSummaryEntity(placeholderOccurrence.item(), builder);
        }
        if (entity instanceof StepAnnotationPointOccurrence) {
            StepAnnotationPointOccurrence pointOccurrence = (StepAnnotationPointOccurrence) entity;
            return validateSummaryEntity(pointOccurrence.item(), builder);
        }
        if (entity instanceof StepAnnotationTextOccurrence) {
            StepAnnotationTextOccurrence textOccurrence = (StepAnnotationTextOccurrence) entity;
            validateSummaryEntity(textOccurrence.position(), builder);
            return 1;
        }
        if (entity instanceof StepAnnotationSymbolOccurrence) {
            StepAnnotationSymbolOccurrence symbolOccurrence = (StepAnnotationSymbolOccurrence) entity;
            return validateSummaryEntity(symbolOccurrence.item(), builder);
        }
        if (entity instanceof StepAnnotationSubfigureOccurrence) {
            StepAnnotationSubfigureOccurrence subfigureOccurrence = (StepAnnotationSubfigureOccurrence) entity;
            return validateSummaryEntity(subfigureOccurrence.item(), builder);
        }
        if (entity instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) entity;
            return validateSummaryEntity(terminatorSymbol.item(), builder)
                    + validateSummaryEntity(terminatorSymbol.annotatedCurve(), builder);
        }
        if (entity instanceof StepAnnotationPlane) {
            StepAnnotationPlane annotationPlane = (StepAnnotationPlane) entity;
            return validateAnnotationPlane(annotationPlane, builder);
        }
        if (entity instanceof StepDraughtingCallout) {
            StepDraughtingCallout callout = (StepDraughtingCallout) entity;
            return validateDraughtingCallout(callout, builder);
        }
        if (entity instanceof StepDraughtingCalloutRelationship) {
            StepDraughtingCalloutRelationship relationship = (StepDraughtingCalloutRelationship) entity;
            return validateDraughtingCallout(relationship.relatingCallout(), builder)
                    + validateDraughtingCallout(relationship.relatedCallout(), builder);
        }
        if (entity instanceof StepAnnotationOccurrenceRelationship) {
            StepAnnotationOccurrenceRelationship relationship = (StepAnnotationOccurrenceRelationship) entity;
            return validateSummaryEntity(relationship.relatingAnnotationOccurrence(), builder)
                    + validateSummaryEntity(relationship.relatedAnnotationOccurrence(), builder);
        }
        if (entity instanceof StepSymbolRepresentationMap) {
            StepSymbolRepresentationMap representationMap = (StepSymbolRepresentationMap) entity;
            validateSummaryEntity(representationMap.mappedOrigin(), builder);
            return validateRepresentation(representationMap.mappedRepresentation(), builder);
        }
        if (entity instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) entity;
            int count = validateSummaryEntity(annotationSymbol.mappingSource(), builder);
            return count + validateSummaryEntity(annotationSymbol.mappingTarget(), builder);
        }
        if (entity instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) entity;
            int count = validateSummaryEntity(annotationText.mappingSource(), builder);
            return count + validateSummaryEntity(annotationText.mappingTarget(), builder);
        }
        if (entity instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) entity;
            int count = validateSummaryEntity(annotationTextCharacter.mappingSource(), builder);
            return count + validateSummaryEntity(annotationTextCharacter.mappingTarget(), builder);
        }
        if (entity instanceof StepPresentationLayerAssignment) {
            StepPresentationLayerAssignment layerAssignment = (StepPresentationLayerAssignment) entity;
            return validateSummaryItems(layerAssignment.assignedItems(), builder);
        }
        if (entity instanceof StepPresentationStyleAssignment) {
            StepPresentationStyleAssignment assignment = (StepPresentationStyleAssignment) entity;
            return validatePresentationStyleAssignment(assignment, builder);
        }
        if (entity instanceof StepCurveStyle) {
            StepCurveStyle curveStyle = (StepCurveStyle) entity;
            return validateCurveStyle(curveStyle, builder);
        }
        if (entity instanceof StepPointStyle) {
            StepPointStyle pointStyle = (StepPointStyle) entity;
            return validateSummaryEntity(pointStyle.marker(), builder)
                    + validateSummaryEntity(pointStyle.colour(), builder);
        }
        if (entity instanceof StepSymbolStyle) {
            StepSymbolStyle symbolStyle = (StepSymbolStyle) entity;
            return validateSummaryEntity(symbolStyle.styleOfSymbol(), builder);
        }
        if (entity instanceof StepFillAreaStyleColour) {
            StepFillAreaStyleColour fillAreaStyleColour = (StepFillAreaStyleColour) entity;
            return validateSummaryEntity(fillAreaStyleColour.colour(), builder);
        }
        if (entity instanceof StepFillAreaStyle) {
            StepFillAreaStyle fillAreaStyle = (StepFillAreaStyle) entity;
            return validateFillAreaStyle(fillAreaStyle, builder);
        }
        if (entity instanceof StepSurfaceStyleFillArea) {
            StepSurfaceStyleFillArea surfaceStyleFillArea = (StepSurfaceStyleFillArea) entity;
            return validateSummaryEntity(surfaceStyleFillArea.fillStyle(), builder);
        }
        if (entity instanceof StepSurfaceStyleBoundary) {
            StepSurfaceStyleBoundary surfaceStyleBoundary = (StepSurfaceStyleBoundary) entity;
            return validateCurveStyle(surfaceStyleBoundary.style(), builder);
        }
        if (entity instanceof StepSurfaceStyleParameterLine) {
            StepSurfaceStyleParameterLine surfaceStyleParameterLine = (StepSurfaceStyleParameterLine) entity;
            return validateCurveStyle(surfaceStyleParameterLine.style(), builder);
        }
        if (entity instanceof StepSurfaceStyleControlGrid) {
            StepSurfaceStyleControlGrid surfaceStyleControlGrid = (StepSurfaceStyleControlGrid) entity;
            return validateCurveStyle(surfaceStyleControlGrid.style(), builder);
        }
        if (entity instanceof StepSurfaceStyleSegmentationCurve) {
            StepSurfaceStyleSegmentationCurve surfaceStyleSegmentationCurve = (StepSurfaceStyleSegmentationCurve) entity;
            return validateCurveStyle(surfaceStyleSegmentationCurve.style(), builder);
        }
        if (entity instanceof StepSurfaceStyleSilhouette) {
            StepSurfaceStyleSilhouette surfaceStyleSilhouette = (StepSurfaceStyleSilhouette) entity;
            return validateCurveStyle(surfaceStyleSilhouette.style(), builder);
        }
        if (entity instanceof StepSurfaceStyleTransparent
                || entity instanceof StepSurfaceStyleReflectanceAmbient
                || entity instanceof StepSurfaceStyleReflectanceAmbientDiffuse) {
            return 1;
        }
        if (entity instanceof StepSurfaceStyleReflectanceAmbientDiffuseSpecular) {
            StepSurfaceStyleReflectanceAmbientDiffuseSpecular specular = (StepSurfaceStyleReflectanceAmbientDiffuseSpecular) entity;
            return 1 + validateSummaryEntity(specular.specularColour(), builder);
        }
        if (entity instanceof StepSurfaceSideStyle) {
            StepSurfaceSideStyle surfaceSideStyle = (StepSurfaceSideStyle) entity;
            return validateSurfaceSideStyle(surfaceSideStyle, builder);
        }
        if (entity instanceof StepSurfaceStyleUsage) {
            StepSurfaceStyleUsage surfaceStyleUsage = (StepSurfaceStyleUsage) entity;
            return validateSurfaceSideStyle(surfaceStyleUsage.style(), builder);
        }
        if (entity instanceof StepTextStyleForDefinedFont) {
            StepTextStyleForDefinedFont textStyleForDefinedFont = (StepTextStyleForDefinedFont) entity;
            return validateSummaryEntity(textStyleForDefinedFont.textColour(), builder);
        }
        if (entity instanceof StepTextStyle) {
            StepTextStyle textStyle = (StepTextStyle) entity;
            return validateSummaryEntity(textStyle.characterAppearance(), builder);
        }
        if (entity instanceof StepTextStyleWithSpacing) {
            StepTextStyleWithSpacing textStyleWithSpacing = (StepTextStyleWithSpacing) entity;
            return validateSummaryEntity(textStyleWithSpacing.characterAppearance(), builder);
        }
        if (entity instanceof StepTextStyleWithJustification) {
            StepTextStyleWithJustification textStyleWithJustification = (StepTextStyleWithJustification) entity;
            return validateSummaryEntity(textStyleWithJustification.characterAppearance(), builder);
        }
        if (entity instanceof StepTextStyleWithMirror) {
            StepTextStyleWithMirror textStyleWithMirror = (StepTextStyleWithMirror) entity;
            return validateSummaryEntity(textStyleWithMirror.characterAppearance(), builder)
                    + validateSummaryEntity(textStyleWithMirror.mirrorPlacement(), builder);
        }
        if (entity instanceof StepTextStyleWithBoxCharacteristics) {
            StepTextStyleWithBoxCharacteristics textStyleWithBoxCharacteristics = (StepTextStyleWithBoxCharacteristics) entity;
            return validateSummaryEntity(textStyleWithBoxCharacteristics.characterAppearance(), builder);
        }
        if (entity instanceof StepSymbolColour) {
            StepSymbolColour symbolColour = (StepSymbolColour) entity;
            return validateSummaryEntity(symbolColour.colour(), builder);
        }
        if (entity instanceof StepCharacterGlyphStyleStroke) {
            StepCharacterGlyphStyleStroke glyphStyleStroke = (StepCharacterGlyphStyleStroke) entity;
            return validateCurveStyle(glyphStyleStroke.strokeStyle(), builder);
        }
        if (entity instanceof StepCharacterGlyphStyleOutline) {
            StepCharacterGlyphStyleOutline glyphStyleOutline = (StepCharacterGlyphStyleOutline) entity;
            return validateCurveStyle(glyphStyleOutline.outlineStyle(), builder);
        }
        if (entity instanceof StepCharacterGlyphStyleOutlineWithCharacteristics) {
            StepCharacterGlyphStyleOutlineWithCharacteristics glyphStyleOutline = (StepCharacterGlyphStyleOutlineWithCharacteristics) entity;
            return validateCurveStyle(glyphStyleOutline.outlineStyle(), builder)
                    + validateFillAreaStyle(glyphStyleOutline.characteristics(), builder);
        }
        if (entity instanceof StepUserDefinedCurveFont) {
            StepUserDefinedCurveFont userDefinedCurveFont = (StepUserDefinedCurveFont) entity;
            return validateRepresentationMap(userDefinedCurveFont.mappingSource(), builder)
                    + validateSummaryEntity(userDefinedCurveFont.mappingTarget(), builder);
        }
        if (entity instanceof StepUserDefinedMarker) {
            StepUserDefinedMarker userDefinedMarker = (StepUserDefinedMarker) entity;
            return validateRepresentationMap(userDefinedMarker.mappingSource(), builder)
                    + validateSummaryEntity(userDefinedMarker.mappingTarget(), builder);
        }
        if (entity instanceof StepUserDefinedTerminatorSymbol) {
            StepUserDefinedTerminatorSymbol userDefinedTerminatorSymbol = (StepUserDefinedTerminatorSymbol) entity;
            return validateRepresentationMap(userDefinedTerminatorSymbol.mappingSource(), builder)
                    + validateSummaryEntity(userDefinedTerminatorSymbol.mappingTarget(), builder);
        }
        if (entity instanceof StepGeometricRepresentationContext) {
            StepGeometricRepresentationContext geometricRepresentationContext = (StepGeometricRepresentationContext) entity;
            int count = 1;
            if (geometricRepresentationContext.globalUnitAssignedContext() != null) {
                count += validateSummaryEntity(geometricRepresentationContext.globalUnitAssignedContext(), builder);
            }
            if (geometricRepresentationContext.globalUncertaintyAssignedContext() != null) {
                count += validateSummaryEntity(geometricRepresentationContext.globalUncertaintyAssignedContext(), builder);
            }
            return count;
        }
        if (entity instanceof StepGlobalUnitAssignedContext) {
            StepGlobalUnitAssignedContext globalUnitAssignedContext = (StepGlobalUnitAssignedContext) entity;
            return validateSummaryItems(globalUnitAssignedContext.units(), builder);
        }
        if (entity instanceof StepGlobalUncertaintyAssignedContext) {
            StepGlobalUncertaintyAssignedContext globalUncertaintyAssignedContext = (StepGlobalUncertaintyAssignedContext) entity;
            int count = 0;
            for (StepUncertaintyMeasureWithUnit uncertainty : globalUncertaintyAssignedContext.uncertainties()) {
                count += validateSummaryEntity(uncertainty, builder);
            }
            return count;
        }
        if (entity instanceof StepMeasureWithUnit) {
            StepMeasureWithUnit measureWithUnit = (StepMeasureWithUnit) entity;
            return validateSummaryEntity(measureWithUnit.unitComponent(), builder);
        }
        if (entity instanceof StepTypedMeasureWithUnit) {
            StepTypedMeasureWithUnit typedMeasureWithUnit = (StepTypedMeasureWithUnit) entity;
            return validateSummaryEntity(typedMeasureWithUnit.unitComponent(), builder);
        }
        if (entity instanceof StepUncertaintyMeasureWithUnit) {
            StepUncertaintyMeasureWithUnit uncertaintyMeasureWithUnit = (StepUncertaintyMeasureWithUnit) entity;
            return validateSummaryEntity(uncertaintyMeasureWithUnit.unitComponent(), builder);
        }
        if (entity instanceof StepConversionBasedUnit) {
            StepConversionBasedUnit conversionBasedUnit = (StepConversionBasedUnit) entity;
            return validateSummaryEntity(conversionBasedUnit.conversionFactor(), builder);
        }
        if (entity instanceof StepConversionBasedUnitWithOffset) {
            StepConversionBasedUnitWithOffset conversionBasedUnitWithOffset = (StepConversionBasedUnitWithOffset) entity;
            return validateSummaryEntity(conversionBasedUnitWithOffset.conversionFactor(), builder);
        }
        if (entity instanceof StepDerivedUnit) {
            StepDerivedUnit derivedUnit = (StepDerivedUnit) entity;
            int count = 0;
            for (StepDerivedUnitElement element : derivedUnit.elements()) {
                count += validateSummaryEntity(element, builder);
            }
            return count;
        }
        if (entity instanceof StepDerivedUnitElement) {
            StepDerivedUnitElement derivedUnitElement = (StepDerivedUnitElement) entity;
            return validateSummaryEntity(derivedUnitElement.unit(), builder);
        }
        if (entity instanceof StepPreDefinedColour
                || entity instanceof StepColourSpecification
                || entity instanceof StepDraughtingPreDefinedColour
                || entity instanceof StepColour
                || entity instanceof StepColourRgb
                || entity instanceof StepPreDefinedCurveFont
                || entity instanceof StepDraughtingPreDefinedCurveFont
                || entity instanceof StepPreDefinedMarker
                || entity instanceof StepPreDefinedTextFont
                || entity instanceof StepPreDefinedItem
                || entity instanceof StepPreDefinedSymbol
                || entity instanceof StepPreDefinedPointMarkerSymbol
                || entity instanceof StepPreDefinedDimensionSymbol
                || entity instanceof StepPreDefinedGeometricalToleranceSymbol
                || entity instanceof StepPreDefinedTerminatorSymbol
                || entity instanceof StepPreDefinedSurfaceSideStyle
                || entity instanceof StepDraughtingPreDefinedTextFont
                || entity instanceof StepExternalSource
                || entity instanceof StepExternallyDefinedItem
                || entity instanceof StepAddress
                || entity instanceof StepGeneralProperty
                || entity instanceof StepCharacterizedObject
                || entity instanceof StepProductCategory
                || entity instanceof StepProductRelatedProductCategory
                || entity instanceof StepEffectivity
                || entity instanceof StepLanguage
                || entity instanceof StepIdentificationRole
                || entity instanceof StepDescriptionAttribute
                || entity instanceof StepNameAttribute
                || entity instanceof StepIdAttribute
                || entity instanceof StepDescriptiveRepresentationItem
                || entity instanceof StepValueRepresentationItem
                || entity instanceof StepMeasureRepresentationItem
                || entity instanceof StepRepresentationItem
                || entity instanceof StepGeometricRepresentationItem
                || entity instanceof StepTopologicalRepresentationItem
                || entity instanceof StepPoint
                || entity instanceof StepCurve
                || entity instanceof StepSurface
                || entity instanceof StepSurfaceModel
                || entity instanceof StepSolidModel
                || entity instanceof StepBoundedCurve
                || entity instanceof StepBSplineCurve
                || entity instanceof StepPiecewiseBezierCurve
                || entity instanceof StepBezierCurve
                || entity instanceof StepUniformCurve
                || entity instanceof StepQuasiUniformCurve
                || entity instanceof StepBoundedSurface
                || entity instanceof StepBSplineSurface
                || entity instanceof StepPiecewiseBezierSurface
                || entity instanceof StepBezierSurface
                || entity instanceof StepUniformSurface
                || entity instanceof StepQuasiUniformSurface
                || entity instanceof StepVertex
                || entity instanceof StepEdge
                || entity instanceof StepFace
                || entity instanceof StepDocumentType
                || entity instanceof StepRepresentationContext
                || entity instanceof StepNamedUnit
                || entity instanceof StepSiUnit
                || entity instanceof StepContextDependentUnit
                || entity instanceof StepDimensionalExponents
                || entity instanceof StepGroup
                || entity instanceof StepClassificationRole
                || entity instanceof StepOrganization
                || entity instanceof StepOrganizationRole
                || entity instanceof StepNameAssignment
                || entity instanceof StepApprovalStatus
                || entity instanceof StepApprovalRole
                || entity instanceof StepContractType
                || entity instanceof StepCertificationType
                || entity instanceof StepSecurityClassificationLevel
                || entity instanceof StepPerson
                || entity instanceof StepPersonAndOrganizationRole
                || entity instanceof StepCalendarDate
                || entity instanceof StepCoordinatedUniversalTimeOffset
                || entity instanceof StepDateRole
                || entity instanceof StepDateTimeRole) {
            return 1;
        }
        if (entity instanceof StepIdentificationAssignment) {
            StepIdentificationAssignment identificationAssignment = (StepIdentificationAssignment) entity;
            return validateSummaryEntity(identificationAssignment.role(), builder);
        }
        if (entity instanceof StepAppliedIdentificationAssignment) {
            StepAppliedIdentificationAssignment appliedIdentificationAssignment = (StepAppliedIdentificationAssignment) entity;
            return validateSummaryEntity(appliedIdentificationAssignment.role(), builder)
                    + validateSummaryItems(appliedIdentificationAssignment.items(), builder);
        }
        if (entity instanceof StepPersonAndOrganization) {
            StepPersonAndOrganization personAndOrganization = (StepPersonAndOrganization) entity;
            return validateSummaryEntity(personAndOrganization.person(), builder)
                    + validateSummaryEntity(personAndOrganization.organization(), builder);
        }
        if (entity instanceof StepPersonAndOrganizationAssignment) {
            StepPersonAndOrganizationAssignment personAndOrganizationAssignment = (StepPersonAndOrganizationAssignment) entity;
            return validateSummaryEntity(personAndOrganizationAssignment.assignedPersonAndOrganization(), builder)
                    + validateSummaryEntity(personAndOrganizationAssignment.role(), builder);
        }
        if (entity instanceof StepAppliedPersonAndOrganizationAssignment) {
            StepAppliedPersonAndOrganizationAssignment appliedPersonAndOrganizationAssignment = (StepAppliedPersonAndOrganizationAssignment) entity;
            return validateSummaryEntity(appliedPersonAndOrganizationAssignment.assignedPersonAndOrganization(), builder)
                    + validateSummaryEntity(appliedPersonAndOrganizationAssignment.role(), builder)
                    + validateSummaryItems(appliedPersonAndOrganizationAssignment.items(), builder);
        }
        if (entity instanceof StepLocalTime) {
            StepLocalTime localTime = (StepLocalTime) entity;
            return 1 + validateSummaryEntity(localTime.zone(), builder);
        }
        if (entity instanceof StepDateAndTime) {
            StepDateAndTime dateAndTime = (StepDateAndTime) entity;
            return validateSummaryEntity(dateAndTime.dateComponent(), builder)
                    + validateSummaryEntity(dateAndTime.timeComponent(), builder);
        }
        if (entity instanceof StepDateAssignment) {
            StepDateAssignment dateAssignment = (StepDateAssignment) entity;
            return validateSummaryEntity(dateAssignment.assignedDate(), builder)
                    + validateSummaryEntity(dateAssignment.role(), builder);
        }
        if (entity instanceof StepAppliedDateAssignment) {
            StepAppliedDateAssignment appliedDateAssignment = (StepAppliedDateAssignment) entity;
            return validateSummaryEntity(appliedDateAssignment.assignedDate(), builder)
                    + validateSummaryEntity(appliedDateAssignment.role(), builder)
                    + validateSummaryItems(appliedDateAssignment.items(), builder);
        }
        if (entity instanceof StepDateTimeAssignment) {
            StepDateTimeAssignment dateTimeAssignment = (StepDateTimeAssignment) entity;
            return validateSummaryEntity(dateTimeAssignment.assignedDateAndTime(), builder)
                    + validateSummaryEntity(dateTimeAssignment.role(), builder);
        }
        if (entity instanceof StepAppliedDateTimeAssignment) {
            StepAppliedDateTimeAssignment appliedDateTimeAssignment = (StepAppliedDateTimeAssignment) entity;
            return validateSummaryEntity(appliedDateTimeAssignment.assignedDateAndTime(), builder)
                    + validateSummaryEntity(appliedDateTimeAssignment.role(), builder)
                    + validateSummaryItems(appliedDateTimeAssignment.items(), builder);
        }
        if (entity instanceof StepDocumentReference) {
            StepDocumentReference documentReference = (StepDocumentReference) entity;
            return validateSummaryEntity(documentReference.assignedDocument(), builder);
        }
        if (entity instanceof StepAppliedDocumentReference) {
            StepAppliedDocumentReference appliedDocumentReference = (StepAppliedDocumentReference) entity;
            return validateSummaryEntity(appliedDocumentReference.assignedDocument(), builder)
                    + validateSummaryItems(appliedDocumentReference.items(), builder);
        }
        if (entity instanceof StepDocumentRelationship) {
            StepDocumentRelationship documentRelationship = (StepDocumentRelationship) entity;
            return validateSummaryEntity(documentRelationship.relatingDocument(), builder)
                    + validateSummaryEntity(documentRelationship.relatedDocument(), builder);
        }
        if (entity instanceof StepPropertyDefinitionRelationship) {
            StepPropertyDefinitionRelationship propertyDefinitionRelationship = (StepPropertyDefinitionRelationship) entity;
            return 2;
        }
        if (entity instanceof StepAbstractVariable) {
            StepAbstractVariable abstractVariable = (StepAbstractVariable) entity;
            return validateSummaryEntity(abstractVariable.definition(), builder)
                    + validateSummaryEntity(abstractVariable.usedRepresentation(), builder);
        }
        if (entity instanceof StepRowVariable) {
            StepRowVariable rowVariable = (StepRowVariable) entity;
            return validateSummaryEntity(rowVariable.definition(), builder)
                    + validateSummaryEntity(rowVariable.usedRepresentation(), builder);
        }
        if (entity instanceof StepScalarVariable) {
            StepScalarVariable scalarVariable = (StepScalarVariable) entity;
            return validateSummaryEntity(scalarVariable.definition(), builder)
                    + validateSummaryEntity(scalarVariable.usedRepresentation(), builder);
        }
        if (entity instanceof StepForwardChainingRulePremise) {
            StepForwardChainingRulePremise rulePremise = (StepForwardChainingRulePremise) entity;
            return validateSummaryEntity(rulePremise.definition(), builder)
                    + validateSummaryEntity(rulePremise.usedRepresentation(), builder);
        }
        if (entity instanceof StepBackChainingRuleBody) {
            StepBackChainingRuleBody ruleBody = (StepBackChainingRuleBody) entity;
            return validateSummaryEntity(ruleBody.definition(), builder)
                    + validateSummaryEntity(ruleBody.usedRepresentation(), builder);
        }
        if (entity instanceof StepAttributeAssertion) {
            StepAttributeAssertion attributeAssertion = (StepAttributeAssertion) entity;
            return validateSummaryEntity(attributeAssertion.usedRepresentation(), builder);
        }
        if (entity instanceof StepApprovalPersonOrganization) {
            StepApprovalPersonOrganization approvalPersonOrganization = (StepApprovalPersonOrganization) entity;
            return validateSummaryEntity(approvalPersonOrganization.personOrganization(), builder)
                    + validateSummaryEntity(approvalPersonOrganization.authorizedApproval(), builder)
                    + validateSummaryEntity(approvalPersonOrganization.role(), builder);
        }
        if (entity instanceof StepApprovalDateTime) {
            StepApprovalDateTime approvalDateTime = (StepApprovalDateTime) entity;
            return validateSummaryEntity(approvalDateTime.dateTime(), builder)
                    + validateSummaryEntity(approvalDateTime.datedApproval(), builder);
        }
        if (entity instanceof StepGroupRelationship) {
            StepGroupRelationship groupRelationship = (StepGroupRelationship) entity;
            return validateSummaryEntity(groupRelationship.relatingGroup(), builder)
                    + validateSummaryEntity(groupRelationship.relatedGroup(), builder);
        }
        if (entity instanceof StepOrganizationRelationship) {
            StepOrganizationRelationship organizationRelationship = (StepOrganizationRelationship) entity;
            return validateSummaryEntity(organizationRelationship.relatingOrganization(), builder)
                    + validateSummaryEntity(organizationRelationship.relatedOrganization(), builder);
        }
        if (entity instanceof StepApplicationContext) {
            return 1;
        }
        if (entity instanceof StepApplicationProtocolDefinition) {
            StepApplicationProtocolDefinition applicationProtocolDefinition = (StepApplicationProtocolDefinition) entity;
            return validateSummaryEntity(applicationProtocolDefinition.application(), builder);
        }
        if (entity instanceof StepProduct) {
            StepProduct product = (StepProduct) entity;
            return 1;
        }
        if (entity instanceof StepProductContext) {
            StepProductContext productContext = (StepProductContext) entity;
            return validateSummaryEntity(productContext.frameOfReference(), builder);
        }
        if (entity instanceof StepProductDefinitionContext) {
            StepProductDefinitionContext productDefinitionContext = (StepProductDefinitionContext) entity;
            return validateSummaryEntity(productDefinitionContext.frameOfReference(), builder);
        }
        if (entity instanceof StepProductDefinitionFormation) {
            StepProductDefinitionFormation formation = (StepProductDefinitionFormation) entity;
            return validateSummaryEntity(formation.ofProduct(), builder);
        }
        if (entity instanceof StepProductDefinition) {
            StepProductDefinition definition = (StepProductDefinition) entity;
            return validateSummaryEntity(definition.formation(), builder)
                    + validateSummaryEntity(definition.frameOfReference(), builder);
        }
        if (entity instanceof StepProductDefinitionShape) {
            StepProductDefinitionShape productDefinitionShape = (StepProductDefinitionShape) entity;
            return validateSummaryEntity(productDefinitionShape.definition(), builder);
        }
        if (entity instanceof StepProductDefinitionEffectivity) {
            StepProductDefinitionEffectivity productDefinitionEffectivity = (StepProductDefinitionEffectivity) entity;
            return validateSummaryEntity(productDefinitionEffectivity.productDefinition(), builder);
        }
        if (entity instanceof StepProductRelationship) {
            StepProductRelationship productRelationship = (StepProductRelationship) entity;
            return validateSummaryEntity(productRelationship.relatingProduct(), builder)
                    + validateSummaryEntity(productRelationship.relatedProduct(), builder);
        }
        if (entity instanceof StepProductDefinitionRelationship) {
            StepProductDefinitionRelationship productDefinitionRelationship = (StepProductDefinitionRelationship) entity;
            return validateSummaryEntity(productDefinitionRelationship.relatingProductDefinition(), builder)
                    + validateSummaryEntity(productDefinitionRelationship.relatedProductDefinition(), builder);
        }
        if (entity instanceof StepProductDefinitionFormationRelationship) {
            StepProductDefinitionFormationRelationship productDefinitionFormationRelationship = (StepProductDefinitionFormationRelationship) entity;
            return validateSummaryEntity(productDefinitionFormationRelationship.relatingFormation(), builder)
                    + validateSummaryEntity(productDefinitionFormationRelationship.relatedFormation(), builder);
        }
        if (entity instanceof StepProductDefinitionRelationshipRelationship) {
            StepProductDefinitionRelationshipRelationship relationshipRelationship = (StepProductDefinitionRelationshipRelationship) entity;
            return validateSummaryEntity(relationshipRelationship.relating(), builder)
                    + validateSummaryEntity(relationshipRelationship.related(), builder);
        }
        if (entity instanceof StepPropertyDefinition) {
            StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) entity;
            return validateSummaryEntity(propertyDefinition.definition(), builder);
        }
        if (entity instanceof StepPropertyDefinitionRepresentation) {
            StepPropertyDefinitionRepresentation propertyDefinitionRepresentation = (StepPropertyDefinitionRepresentation) entity;
            return validateSummaryEntity(propertyDefinitionRepresentation.definition(), builder)
                    + validateSummaryEntity(propertyDefinitionRepresentation.usedRepresentation(), builder);
        }
        if (entity instanceof StepActionPropertyRepresentation) {
            StepActionPropertyRepresentation actionPropertyRepresentation = (StepActionPropertyRepresentation) entity;
            return validateSummaryEntity(actionPropertyRepresentation.definition(), builder)
                    + validateSummaryEntity(actionPropertyRepresentation.usedRepresentation(), builder);
        }
        if (entity instanceof StepContactRatioRepresentation) {
            StepContactRatioRepresentation contactRatioRepresentation = (StepContactRatioRepresentation) entity;
            return validateSummaryEntity(contactRatioRepresentation.definition(), builder)
                    + validateSummaryEntity(contactRatioRepresentation.usedRepresentation(), builder);
        }
        if (entity instanceof StepKinematicPropertyDefinitionRepresentation) {
            StepKinematicPropertyDefinitionRepresentation kinematicPropertyDefinitionRepresentation = (StepKinematicPropertyDefinitionRepresentation) entity;
            return validateSummaryEntity(kinematicPropertyDefinitionRepresentation.definition(), builder)
                    + validateSummaryEntity(kinematicPropertyDefinitionRepresentation.usedRepresentation(), builder);
        }
        if (entity instanceof StepKinematicPropertyMechanismRepresentation) {
            StepKinematicPropertyMechanismRepresentation kinematicPropertyMechanismRepresentation = (StepKinematicPropertyMechanismRepresentation) entity;
            return validateSummaryEntity(kinematicPropertyMechanismRepresentation.definition(), builder)
                    + validateSummaryEntity(kinematicPropertyMechanismRepresentation.usedRepresentation(), builder);
        }
        if (entity instanceof StepKinematicPropertyRepresentationRelation) {
            StepKinematicPropertyRepresentationRelation kinematicPropertyRepresentationRelation = (StepKinematicPropertyRepresentationRelation) entity;
            return validateSummaryEntity(kinematicPropertyRepresentationRelation.definition(), builder)
                    + validateSummaryEntity(kinematicPropertyRepresentationRelation.usedRepresentation(), builder);
        }
        if (entity instanceof StepKinematicPropertyTopologyRepresentation) {
            StepKinematicPropertyTopologyRepresentation kinematicPropertyTopologyRepresentation = (StepKinematicPropertyTopologyRepresentation) entity;
            return validateSummaryEntity(kinematicPropertyTopologyRepresentation.definition(), builder)
                    + validateSummaryEntity(kinematicPropertyTopologyRepresentation.usedRepresentation(), builder);
        }
        if (entity instanceof StepResourcePropertyRepresentation) {
            StepResourcePropertyRepresentation resourcePropertyRepresentation = (StepResourcePropertyRepresentation) entity;
            return validateSummaryEntity(resourcePropertyRepresentation.definition(), builder)
                    + validateSummaryEntity(resourcePropertyRepresentation.usedRepresentation(), builder);
        }
        if (entity instanceof StepShapeDefinitionRepresentation) {
            StepShapeDefinitionRepresentation shapeDefinitionRepresentation = (StepShapeDefinitionRepresentation) entity;
            return validateSummaryEntity(shapeDefinitionRepresentation.definition(), builder)
                    + validateSummaryEntity(shapeDefinitionRepresentation.usedRepresentation(), builder);
        }
        if (entity instanceof StepContextDependentShapeRepresentation) {
            StepContextDependentShapeRepresentation contextDependentShapeRepresentation = (StepContextDependentShapeRepresentation) entity;
            return validateSummaryEntity(contextDependentShapeRepresentation.representationRelationship(), builder)
                    + validateSummaryEntity(contextDependentShapeRepresentation.representedProductRelation(), builder);
        }
        if (entity instanceof StepNextAssemblyUsageOccurrence) {
            StepNextAssemblyUsageOccurrence nextAssemblyUsageOccurrence = (StepNextAssemblyUsageOccurrence) entity;
            return validateSummaryEntity(nextAssemblyUsageOccurrence.relatingProductDefinition(), builder)
                    + validateSummaryEntity(nextAssemblyUsageOccurrence.relatedProductDefinition(), builder);
        }
        if (entity instanceof StepPlacedDatumTargetFeature) {
            StepPlacedDatumTargetFeature placedDatumTargetFeature = (StepPlacedDatumTargetFeature) entity;
            return validateSummaryEntity(placedDatumTargetFeature.usedRepresentation(), builder);
        }
        if (entity instanceof StepShapeAspect) {
            StepShapeAspect shapeAspect = (StepShapeAspect) entity;
            return validateSummaryEntity(shapeAspect.ofShape(), builder);
        }
        if (entity instanceof StepShapeAspectOccurrence) {
            StepShapeAspectOccurrence shapeAspectOccurrence = (StepShapeAspectOccurrence) entity;
            return validateSummaryEntity(shapeAspectOccurrence.ofShape(), builder)
                    + validateSummaryEntity(shapeAspectOccurrence.definition(), builder);
        }
        if (entity instanceof StepShapeAspectRelationship) {
            StepShapeAspectRelationship shapeAspectRelationship = (StepShapeAspectRelationship) entity;
            return validateSummaryEntity(shapeAspectRelationship.relatingShapeAspect(), builder)
                    + validateSummaryEntity(shapeAspectRelationship.relatedShapeAspect(), builder);
        }
        if (entity instanceof StepItemIdentifiedRepresentationUsage) {
            StepItemIdentifiedRepresentationUsage itemIdentifiedRepresentationUsage = (StepItemIdentifiedRepresentationUsage) entity;
            return validateRepresentationUsage(itemIdentifiedRepresentationUsage.definition(),
                    itemIdentifiedRepresentationUsage.usedRepresentation(),
                    itemIdentifiedRepresentationUsage.identifiedItem(),
                    builder);
        }
        if (entity instanceof StepChainBasedItemIdentifiedRepresentationUsage) {
            StepChainBasedItemIdentifiedRepresentationUsage chainBasedItemIdentifiedRepresentationUsage = (StepChainBasedItemIdentifiedRepresentationUsage) entity;
            return validateChainBasedRepresentationUsage(chainBasedItemIdentifiedRepresentationUsage.definition(),
                    chainBasedItemIdentifiedRepresentationUsage.nodes(),
                    chainBasedItemIdentifiedRepresentationUsage.undirectedLinks(),
                    chainBasedItemIdentifiedRepresentationUsage.identifiedItem(),
                    builder);
        }
        if (entity instanceof StepPlacedTarget) {
            StepPlacedTarget placedTarget = (StepPlacedTarget) entity;
            return validateRepresentationUsage(placedTarget.definition(),
                    placedTarget.usedRepresentation(),
                    placedTarget.identifiedItem(),
                    builder);
        }
        if (entity instanceof StepDraughtingModelItemAssociation) {
            StepDraughtingModelItemAssociation draughtingModelItemAssociation = (StepDraughtingModelItemAssociation) entity;
            return validateRepresentationUsage(draughtingModelItemAssociation.definition(),
                    draughtingModelItemAssociation.usedRepresentation(),
                    draughtingModelItemAssociation.identifiedItem(),
                    builder);
        }
        if (entity instanceof StepDraughtingModelItemAssociationWithPlaceholder) {
            StepDraughtingModelItemAssociationWithPlaceholder associationWithPlaceholder = (StepDraughtingModelItemAssociationWithPlaceholder) entity;
            return validateRepresentationUsage(associationWithPlaceholder.definition(),
                    associationWithPlaceholder.usedRepresentation(),
                    associationWithPlaceholder.identifiedItem(),
                    builder) + validateSummaryEntity(associationWithPlaceholder.annotationPlaceholder(), builder);
        }
        if (entity instanceof StepPmiRequirementItemAssociation) {
            StepPmiRequirementItemAssociation pmiRequirementItemAssociation = (StepPmiRequirementItemAssociation) entity;
            return validateRepresentationUsage(pmiRequirementItemAssociation.definition(),
                    pmiRequirementItemAssociation.usedRepresentation(),
                    pmiRequirementItemAssociation.identifiedItem(),
                    builder) + validateSummaryEntity(pmiRequirementItemAssociation.requirement(), builder);
        }
        if (entity instanceof StepMechanicalDesignRequirementItemAssociation) {
            StepMechanicalDesignRequirementItemAssociation requirementItemAssociation = (StepMechanicalDesignRequirementItemAssociation) entity;
            return validateRepresentationUsage(requirementItemAssociation.definition(),
                    requirementItemAssociation.usedRepresentation(),
                    requirementItemAssociation.identifiedItem(),
                    builder) + validateSummaryEntity(requirementItemAssociation.requirement(), builder);
        }
        if (entity instanceof StepGeometricItemSpecificUsage) {
            StepGeometricItemSpecificUsage geometricItemSpecificUsage = (StepGeometricItemSpecificUsage) entity;
            return validateSummaryEntity(geometricItemSpecificUsage.usage(), builder)
                    + validateSummaryEntity(geometricItemSpecificUsage.identifiedItem(), builder);
        }
        if (entity instanceof StepChainBasedGeometricItemSpecificUsage) {
            StepChainBasedGeometricItemSpecificUsage chainBasedGeometricItemSpecificUsage = (StepChainBasedGeometricItemSpecificUsage) entity;
            int count = validateSummaryEntity(chainBasedGeometricItemSpecificUsage.usage(), builder)
                    + validateSummaryEntity(chainBasedGeometricItemSpecificUsage.identifiedItem(), builder);
            for (StepRepresentation node : chainBasedGeometricItemSpecificUsage.nodes()) {
                count += validateSummaryEntity(node, builder);
            }
            for (StepRepresentationRelationship link : chainBasedGeometricItemSpecificUsage.undirectedLinks()) {
                count += validateSummaryEntity(link, builder);
            }
            return count;
        }
        if (entity instanceof StepGroupAssignment) {
            StepGroupAssignment groupAssignment = (StepGroupAssignment) entity;
            return validateSummaryEntity(groupAssignment.assignedGroup(), builder);
        }
        if (entity instanceof StepAppliedGroupAssignment) {
            StepAppliedGroupAssignment appliedGroupAssignment = (StepAppliedGroupAssignment) entity;
            return validateSummaryEntity(appliedGroupAssignment.assignedGroup(), builder)
                    + validateSummaryItems(appliedGroupAssignment.items(), builder);
        }
        if (entity instanceof StepClassificationAssignment) {
            StepClassificationAssignment classificationAssignment = (StepClassificationAssignment) entity;
            return validateSummaryEntity(classificationAssignment.assignedClass(), builder)
                    + validateSummaryEntity(classificationAssignment.role(), builder);
        }
        if (entity instanceof StepAppliedClassificationAssignment) {
            StepAppliedClassificationAssignment appliedClassificationAssignment = (StepAppliedClassificationAssignment) entity;
            return validateSummaryEntity(appliedClassificationAssignment.assignedClass(), builder)
                    + validateSummaryEntity(appliedClassificationAssignment.role(), builder)
                    + validateSummaryItems(appliedClassificationAssignment.items(), builder);
        }
        if (entity instanceof StepOrganizationAssignment) {
            StepOrganizationAssignment organizationAssignment = (StepOrganizationAssignment) entity;
            return validateSummaryEntity(organizationAssignment.assignedOrganization(), builder)
                    + validateSummaryEntity(organizationAssignment.role(), builder);
        }
        if (entity instanceof StepAppliedOrganizationAssignment) {
            StepAppliedOrganizationAssignment appliedOrganizationAssignment = (StepAppliedOrganizationAssignment) entity;
            return validateSummaryEntity(appliedOrganizationAssignment.assignedOrganization(), builder)
                    + validateSummaryEntity(appliedOrganizationAssignment.role(), builder)
                    + validateSummaryItems(appliedOrganizationAssignment.items(), builder);
        }
        if (entity instanceof StepAppliedNameAssignment) {
            StepAppliedNameAssignment appliedNameAssignment = (StepAppliedNameAssignment) entity;
            return validateSummaryItems(appliedNameAssignment.items(), builder);
        }
        if (entity instanceof StepApproval) {
            StepApproval approval = (StepApproval) entity;
            return 1 + validateSummaryEntity(approval.status(), builder);
        }
        if (entity instanceof StepApprovalAssignment) {
            StepApprovalAssignment approvalAssignment = (StepApprovalAssignment) entity;
            return validateSummaryEntity(approvalAssignment.assignedApproval(), builder);
        }
        if (entity instanceof StepAppliedApprovalAssignment) {
            StepAppliedApprovalAssignment appliedApprovalAssignment = (StepAppliedApprovalAssignment) entity;
            return validateSummaryEntity(appliedApprovalAssignment.assignedApproval(), builder)
                    + validateSummaryItems(appliedApprovalAssignment.items(), builder);
        }
        if (entity instanceof StepContract) {
            StepContract contract = (StepContract) entity;
            return 1 + validateSummaryEntity(contract.kind(), builder);
        }
        if (entity instanceof StepContractAssignment) {
            StepContractAssignment contractAssignment = (StepContractAssignment) entity;
            return validateSummaryEntity(contractAssignment.assignedContract(), builder);
        }
        if (entity instanceof StepAppliedContractAssignment) {
            StepAppliedContractAssignment appliedContractAssignment = (StepAppliedContractAssignment) entity;
            return validateSummaryEntity(appliedContractAssignment.assignedContract(), builder)
                    + validateSummaryItems(appliedContractAssignment.items(), builder);
        }
        if (entity instanceof StepCertification) {
            StepCertification certification = (StepCertification) entity;
            return 1 + validateSummaryEntity(certification.kind(), builder);
        }
        if (entity instanceof StepCertificationAssignment) {
            StepCertificationAssignment certificationAssignment = (StepCertificationAssignment) entity;
            return validateSummaryEntity(certificationAssignment.assignedCertification(), builder);
        }
        if (entity instanceof StepAppliedCertificationAssignment) {
            StepAppliedCertificationAssignment appliedCertificationAssignment = (StepAppliedCertificationAssignment) entity;
            return validateSummaryEntity(appliedCertificationAssignment.assignedCertification(), builder)
                    + validateSummaryItems(appliedCertificationAssignment.items(), builder);
        }
        if (entity instanceof StepSecurityClassification) {
            StepSecurityClassification securityClassification = (StepSecurityClassification) entity;
            return 1 + validateSummaryEntity(securityClassification.securityLevel(), builder);
        }
        if (entity instanceof StepSecurityClassificationAssignment) {
            StepSecurityClassificationAssignment securityClassificationAssignment = (StepSecurityClassificationAssignment) entity;
            return validateSummaryEntity(securityClassificationAssignment.assignedSecurityClassification(), builder);
        }
        if (entity instanceof StepAppliedSecurityClassificationAssignment) {
            StepAppliedSecurityClassificationAssignment appliedSecurityClassificationAssignment = (StepAppliedSecurityClassificationAssignment) entity;
            return validateSummaryEntity(appliedSecurityClassificationAssignment.assignedSecurityClassification(), builder)
                    + validateSummaryItems(appliedSecurityClassificationAssignment.items(), builder);
        }
        if (entity instanceof StepExternalSourceRelationship) {
            StepExternalSourceRelationship externalSourceRelationship = (StepExternalSourceRelationship) entity;
            return validateSummaryEntity(externalSourceRelationship.relatingSource(), builder)
                    + validateSummaryEntity(externalSourceRelationship.relatedSource(), builder);
        }
        if (entity instanceof StepGeneralPropertyRelationship) {
            StepGeneralPropertyRelationship generalPropertyRelationship = (StepGeneralPropertyRelationship) entity;
            return validateSummaryEntity(generalPropertyRelationship.relatingGeneralProperty(), builder)
                    + validateSummaryEntity(generalPropertyRelationship.relatedGeneralProperty(), builder);
        }
        if (entity instanceof StepProductCategoryRelationship) {
            StepProductCategoryRelationship productCategoryRelationship = (StepProductCategoryRelationship) entity;
            return validateSummaryEntity(productCategoryRelationship.category(), builder)
                    + validateSummaryEntity(productCategoryRelationship.subCategory(), builder);
        }
        if (entity instanceof StepProductRelatedProductCategory) {
            StepProductRelatedProductCategory productRelatedCategory = (StepProductRelatedProductCategory) entity;
            return validateSummaryItems(List.copyOf(productRelatedCategory.products()), builder);
        }
        if (entity instanceof StepDocument) {
            StepDocument document = (StepDocument) entity;
            return 1 + validateSummaryEntity(document.kind(), builder);
        }
        if (entity instanceof StepDocumentUsageConstraint) {
            StepDocumentUsageConstraint documentUsageConstraint = (StepDocumentUsageConstraint) entity;
            return validateSummaryEntity(documentUsageConstraint.source(), builder);
        }
        if (entity instanceof StepEffectivityRelationship) {
            StepEffectivityRelationship effectivityRelationship = (StepEffectivityRelationship) entity;
            return validateSummaryEntity(effectivityRelationship.relatingEffectivity(), builder)
                    + validateSummaryEntity(effectivityRelationship.relatedEffectivity(), builder);
        }
        if (entity instanceof StepLanguageAssignment) {
            StepLanguageAssignment languageAssignment = (StepLanguageAssignment) entity;
            return validateSummaryEntity(languageAssignment.assignedLanguage(), builder);
        }
        if (entity instanceof StepAppliedLanguageAssignment) {
            StepAppliedLanguageAssignment appliedLanguageAssignment = (StepAppliedLanguageAssignment) entity;
            int count = validateSummaryEntity(appliedLanguageAssignment.assignedLanguage(), builder);
            for (StepEntity item : appliedLanguageAssignment.items()) {
                count += validateSummaryEntity(item, builder);
            }
            return count;
        }
        if (entity instanceof StepExternalIdentificationAssignment) {
            StepExternalIdentificationAssignment externalIdentificationAssignment = (StepExternalIdentificationAssignment) entity;
            return validateSummaryEntity(externalIdentificationAssignment.role(), builder)
                    + validateSummaryEntity(externalIdentificationAssignment.source(), builder);
        }
        if (entity instanceof StepAppliedExternalIdentificationAssignment) {
            StepAppliedExternalIdentificationAssignment appliedExternalIdentificationAssignment = (StepAppliedExternalIdentificationAssignment) entity;
            int count = validateSummaryEntity(appliedExternalIdentificationAssignment.role(), builder)
                    + validateSummaryEntity(appliedExternalIdentificationAssignment.source(), builder);
            for (StepEntity item : appliedExternalIdentificationAssignment.items()) {
                count += validateSummaryEntity(item, builder);
            }
            return count;
        }
        throw new UnsupportedGeometryException(stepEntityTypeName(entity) + " dump validation is unsupported");
    }

    private static void validatePoint(StepCartesianPoint point, StepCadBuilder builder) {
        if (point.coordinates().size() == 2) {
            builder.buildPoint2(point.id());
        } else {
            builder.buildPoint(point.id());
        }
    }

    private static void validateDirection(StepDirection direction, StepCadBuilder builder) {
        if (direction.directionRatios().size() == 2) {
            builder.buildDirection2(direction.id());
        } else {
            builder.buildDirection(direction.id());
        }
    }

    private static void validateLine(StepLine line, StepCadBuilder builder) {
        if (line.point().coordinates().size() == 2) {
            builder.buildLine2(line.id());
        } else {
            builder.buildLine(line.id());
        }
    }

    private static void validateCircle(StepCircle circle, StepCadBuilder builder) {
        if (is2dPlacement(circle.position())) {
            builder.buildCircle2(circle.id());
        } else {
            builder.buildCircle(circle.id());
        }
    }

    private static void validateEllipse(StepEllipse ellipse, StepCadBuilder builder) {
        if (is2dPlacement(ellipse.position())) {
            builder.buildEllipse2(ellipse.id());
        } else {
            builder.buildEllipse(ellipse.id());
        }
    }

    private static void validatePolyline(StepPolyline polyline, StepCadBuilder builder) {
        if (!polyline.points().isEmpty() && polyline.points().get(0).coordinates().size() == 2) {
            builder.buildPolyline2(polyline.id());
        } else {
            builder.buildPolyline(polyline.id());
        }
    }

    private static boolean is2dPlacement(StepEntity placement) {
        return placement instanceof StepAxis2Placement2D
                || placement instanceof StepCartesianPoint point && point.coordinates().size() == 2;
    }

    private static Iterable<StepFaceEntity> shellFaces(StepEntity entity) {
        if (entity instanceof StepOpenShell) {
            StepOpenShell openShell = (StepOpenShell) entity;
            return openShell.faces();
        }
        if (entity instanceof StepSurfacedOpenShell) {
            StepSurfacedOpenShell surfacedOpenShell = (StepSurfacedOpenShell) entity;
            return surfacedOpenShell.faces();
        }
        if (entity instanceof StepOrientedOpenShell) {
            StepOrientedOpenShell orientedOpenShell = (StepOrientedOpenShell) entity;
            return orientedOpenShell.faces();
        }
        if (entity instanceof StepClosedShell) {
            StepClosedShell closedShell = (StepClosedShell) entity;
            return closedShell.faces();
        }
        if (entity instanceof StepOrientedClosedShell) {
            StepOrientedClosedShell orientedClosedShell = (StepOrientedClosedShell) entity;
            return orientedClosedShell.faces();
        }
        throw new StepResolutionException("entity #" + entity.id() + " is not a supported shell");
    }

    private static FaceBuildCounts summarizeShell(Iterable<StepFaceEntity> faces, StepCadBuilder builder) {
        int supported = 0;
        int unsupported = 0;
        Map<String, Integer> unsupportedReasons = new LinkedHashMap<>();
        Map<String, Integer> unsupportedReasonCodes = new LinkedHashMap<>();
        for (StepFaceEntity face : faces) {
            try {
                builder.buildFace(face.id());
                supported++;
            } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                unsupported++;
                String reason = normalizeReason(ex.getMessage());
                unsupportedReasons.merge(reason, 1, Integer::sum);
                unsupportedReasonCodes.merge(classifyReasonCode(ex, reason), 1, Integer::sum);
            }
        }
        return new FaceBuildCounts(
                supported,
                unsupported,
                Map.copyOf(unsupportedReasons),
                Map.copyOf(unsupportedReasonCodes)
        );
    }

    private static void appendUnsupportedReasons(List<String> lines, Map<String, Integer> unsupportedReasons) {
        if (unsupportedReasons.isEmpty()) {
            return;
        }
        lines.add("    unsupportedReasons: " + formatReasonCounts(unsupportedReasons));
    }

    private static void appendUnsupportedReasonCodes(List<String> lines, Map<String, Integer> unsupportedReasonCodes) {
        if (unsupportedReasonCodes.isEmpty()) {
            return;
        }
        lines.add("    unsupportedReasonCodes: " + formatReasonCounts(unsupportedReasonCodes));
    }

    private static void mergeReasonCounts(Map<String, Integer> target, Map<String, Integer> source) {
        for (Map.Entry<String, Integer> entry : source.entrySet()) {
            target.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }

    private static String formatReasonCounts(Map<String, Integer> reasonCounts) {
        return reasonCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("|"));
    }

    private static String normalizeReason(String message) {
        if (message == null || message.isBlank()) {
            return "unknown";
        }
        return message.replace(System.lineSeparator(), " ").trim();
    }

    private static String classifyReasonCode(Exception ex, String reason) {
        if (reason.contains("construction for CYLINDRICAL_SURFACE is unsupported")) {
            return "unsupported_surface.cylindrical";
        }
        if (reason.contains("construction for CONICAL_SURFACE is unsupported")) {
            return "unsupported_surface.conical";
        }
        if (reason.contains("construction for TOROIDAL_SURFACE is unsupported")) {
            return "unsupported_surface.toroidal";
        }
        if (reason.contains("construction for DEGENERATE_TOROIDAL_SURFACE is unsupported")) {
            return "unsupported_surface.degenerate_toroidal";
        }
        if (reason.contains("construction for B_SPLINE_SURFACE_WITH_KNOTS is unsupported")) {
            return "unsupported_surface.bspline";
        }
        if (reason.contains("construction for RATIONAL_B_SPLINE_SURFACE is unsupported")) {
            return "unsupported_surface.rational_bspline";
        }
        if (reason.contains("construction for RECTANGULAR_TRIMMED_SURFACE is unsupported")) {
            return "unsupported_surface.rectangular_trimmed";
        }
        if (reason.contains("construction for CURVE_BOUNDED_SURFACE is unsupported")) {
            return "unsupported_surface.curve_bounded";
        }
        if (reason.contains("construction for ORIENTED_SURFACE is unsupported")) {
            return "unsupported_surface.oriented";
        }
        if (reason.contains("SURFACE_REPLICA zero scale is unsupported")) {
            return "unsupported_surface.replica_zero_scale";
        }
        if (reason.contains("SURFACE_REPLICA non-uniform scale is unsupported")) {
            return "unsupported_surface.replica_non_uniform_scale";
        }
        if (reason.contains("construction for SURFACE_REPLICA zero scale is unsupported")) {
            return "unsupported_surface.replica_zero_scale";
        }
        if (reason.contains("construction for SURFACE_REPLICA non-uniform scale is unsupported")) {
            return "unsupported_surface.replica_non_uniform_scale";
        }
        if (reason.contains("construction for SURFACE_REPLICA")) {
            return "unsupported_surface.replica";
        }
        if (reason.contains("RATIONAL_B_SPLINE_CURVE is unsupported")) {
            return "unsupported_curve.rational_bspline";
        }
        if (reason.contains("for CURVE_REPLICA is unsupported")) {
            return "unsupported_curve.replica";
        }
        if (reason.contains("OFFSET_CURVE_2D is unsupported")) {
            return "unsupported_curve.offset_2d";
        }
        if (reason.contains("ORIENTED_CURVE is unsupported")) {
            return "unsupported_curve.oriented";
        }
        if (reason.contains("for PARABOLA is unsupported")
                || reason.contains("for HYPERBOLA is unsupported")
                || reason.contains("for DEGENERATE_CONIC is unsupported")) {
            return "unsupported_curve.conic";
        }
        if (reason.contains("construction for SURFACE_OF_LINEAR_EXTRUSION is unsupported")) {
            return "unsupported_surface.linear_extrusion";
        }
        if (reason.contains("construction for SURFACE_OF_REVOLUTION is unsupported")) {
            return "unsupported_surface.revolution";
        }
        if (reason.contains("construction for SPHERICAL_SURFACE is unsupported")) {
            return "unsupported_surface.spherical";
        }
        if (reason.contains("BOOLEAN_RESULT construction is unsupported")) {
            return "unsupported_boolean.result";
        }
        if (reason.contains("BOOLEAN_CLIPPING_RESULT construction is unsupported")) {
            return "unsupported_boolean.clipping_result";
        }
        if (reason.contains("FACE_BOUND construction for POLY_LOOP is unsupported")) {
            return "unsupported_loop.poly";
        }
        if (reason.contains("must lie on edge curve")) {
            return "topology.edge_vertex_off_curve";
        }
        if (reason.contains("edge loop must be connected and closed")) {
            return "topology.edge_loop_not_closed";
        }
        if (reason.contains("all face vertices must lie on the plane")) {
            return "topology.face_vertex_off_plane";
        }
        if (reason.contains("face must contain an outer bound")) {
            return "topology.face_missing_outer_bound";
        }
        if (reason.contains("requires PLANE geometry")) {
            return "unsupported_surface.non_planar_for_builder";
        }
        if (ex instanceof UnsupportedGeometryException) {
            return "unsupported_geometry.other";
        }
        if (ex instanceof TopologyException) {
            return "topology.other";
        }
        if (ex instanceof StepResolutionException) {
            return "resolution.other";
        }
        if (ex instanceof GeometryException) {
            return "geometry.other";
        }
        return "unknown";
    }

    private record FaceBuildCounts(
            int supportedFaces,
            int unsupportedFaces,
            Map<String, Integer> unsupportedReasons,
            Map<String, Integer> unsupportedReasonCodes
    ) {
        private FaceBuildCounts plus(FaceBuildCounts other) {
            Map<String, Integer> reasons = new LinkedHashMap<>(unsupportedReasons);
            mergeReasonCounts(reasons, other.unsupportedReasons);
            Map<String, Integer> reasonCodes = new LinkedHashMap<>(unsupportedReasonCodes);
            mergeReasonCounts(reasonCodes, other.unsupportedReasonCodes);
            return new FaceBuildCounts(
                    supportedFaces + other.supportedFaces,
                    unsupportedFaces + other.unsupportedFaces,
                    reasons,
                    reasonCodes
            );
        }
    }
}
