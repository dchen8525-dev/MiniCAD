package com.minicad.app;

import com.minicad.common.StepParseException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.step.model.StepBooleanClippingResult;
import com.minicad.common.GeometryException;
import com.minicad.step.model.StepBrepWithVoids;
import com.minicad.step.model.StepBooleanResult;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepCsgPrimitive;
import com.minicad.step.model.StepCsgSolid;
import com.minicad.step.model.StepConnectedEdgeSet;
import com.minicad.step.model.StepConnectedFaceSet;
import com.minicad.step.model.StepConnectedFaceSubSet;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepCircle;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEdgeBasedWireframeModel;
import com.minicad.step.model.StepEdgeLoop;
import com.minicad.step.model.StepEllipse;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepFaceBasedSurfaceModel;
import com.minicad.step.model.StepFaceBound;
import com.minicad.step.model.StepGeometricCurveSet;
import com.minicad.step.model.StepGeometricSet;
import com.minicad.step.model.StepLine;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepItemDefinedTransformation;
import com.minicad.step.model.StepMappedItem;
import com.minicad.step.model.StepOpenPath;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.model.StepOffsetCurve2D;
import com.minicad.step.model.StepOffsetCurve3D;
import com.minicad.step.model.StepOffsetSurface;
import com.minicad.step.model.StepOrientedEdge;
import com.minicad.step.model.StepOrientedClosedShell;
import com.minicad.step.model.StepOrientedCurve;
import com.minicad.step.model.StepOrientedOpenShell;
import com.minicad.step.model.StepOrientedPath;
import com.minicad.step.model.StepOrientedSurface;
import com.minicad.step.model.StepOverRidingStyledItem;
import com.minicad.step.model.StepPath;
import com.minicad.step.model.StepDegeneratePcurve;
import com.minicad.step.model.StepPcurve;
import com.minicad.step.model.StepPolyLoop;
import com.minicad.step.model.StepPolyline;
import com.minicad.step.model.StepPointSet;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepProfileDef;
import com.minicad.step.model.StepRationalBSplineCurve;
import com.minicad.step.model.StepRationalBSplineSurface;
import com.minicad.step.model.StepRectangularTrimmedSurface;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.model.StepRepresentationMap;
import com.minicad.step.model.StepRepresentationRelationship;
import com.minicad.step.model.StepRepresentationRelationshipWithTransformation;
import com.minicad.step.model.StepResourcePropertyRepresentation;
import com.minicad.step.model.StepShellBasedSurfaceModel;
import com.minicad.step.model.StepShellBasedWireframeModel;
import com.minicad.step.model.StepShapeRepresentationRelationship;
import com.minicad.step.model.StepSolidReplica;
import com.minicad.step.model.StepStyledItem;
import com.minicad.step.model.StepSubpath;
import com.minicad.step.model.StepSubedge;
import com.minicad.step.model.StepSweptAreaSolid;
import com.minicad.step.model.StepSurfacedOpenShell;
import com.minicad.step.model.StepSeamCurve;
import com.minicad.step.model.StepSurfaceCurve;
import com.minicad.step.model.StepSurfaceOfLinearExtrusion;
import com.minicad.step.model.StepSurfaceOfRevolution;
import com.minicad.step.model.StepTrimmedCurve;
import com.minicad.step.model.StepVertexLoop;
import com.minicad.step.model.StepVertexShell;
import com.minicad.step.model.StepVertexPoint;
import com.minicad.step.model.StepWireShell;
import com.minicad.step.model.StepAxis1Placement;
import com.minicad.step.model.StepAxis2Placement2D;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepAnnotationCurveOccurrence;
import com.minicad.step.model.StepAnnotationFillArea;
import com.minicad.step.model.StepAnnotationFillAreaOccurrence;
import com.minicad.step.model.StepAnnotationOccurrenceRelationship;
import com.minicad.step.model.StepAnnotationPlaceholderOccurrence;
import com.minicad.step.model.StepAnnotationPlane;
import com.minicad.step.model.StepAnnotationPointOccurrence;
import com.minicad.step.model.StepAnnotationSubfigureOccurrence;
import com.minicad.step.model.StepAnnotationSymbol;
import com.minicad.step.model.StepAnnotationSymbolOccurrence;
import com.minicad.step.model.StepAnnotationText;
import com.minicad.step.model.StepAnnotationTextCharacter;
import com.minicad.step.model.StepAnnotationTextOccurrence;
import com.minicad.step.model.StepBSplineCurveWithKnots;
import com.minicad.step.model.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.StepCartesianTransformationOperator;
import com.minicad.step.model.StepCharacterGlyphStyleOutline;
import com.minicad.step.model.StepCharacterGlyphStyleOutlineWithCharacteristics;
import com.minicad.step.model.StepCharacterGlyphStyleStroke;
import com.minicad.step.model.StepCharacterizedObject;
import com.minicad.step.model.StepColour;
import com.minicad.step.model.StepColourSpecification;
import com.minicad.step.model.StepColourRgb;
import com.minicad.step.model.StepCompositeCurve;
import com.minicad.step.model.StepCompositeCurveOnSurface;
import com.minicad.step.model.StepCompositeCurveSegment;
import com.minicad.step.model.StepCurveBoundedSurface;
import com.minicad.step.model.StepCurveStyle;
import com.minicad.step.model.StepDirection;
import com.minicad.step.model.StepVector;
import com.minicad.step.model.StepDimensionCurve;
import com.minicad.step.model.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.StepDraughtingPreDefinedColour;
import com.minicad.step.model.StepDraughtingPreDefinedCurveFont;
import com.minicad.step.model.StepDraughtingPreDefinedTextFont;
import com.minicad.step.model.StepDraughtingCallout;
import com.minicad.step.model.StepDraughtingCalloutRelationship;
import com.minicad.step.model.StepDraughtingModelItemAssociation;
import com.minicad.step.model.StepDraughtingModelItemAssociationWithPlaceholder;
import com.minicad.step.model.StepApproval;
import com.minicad.step.model.StepApprovalAssignment;
import com.minicad.step.model.StepApprovalDateTime;
import com.minicad.step.model.StepApprovalPersonOrganization;
import com.minicad.step.model.StepApprovalRole;
import com.minicad.step.model.StepApprovalStatus;
import com.minicad.step.model.StepAttributeAssertion;
import com.minicad.step.model.StepDocument;
import com.minicad.step.model.StepDocumentReference;
import com.minicad.step.model.StepDocumentRelationship;
import com.minicad.step.model.StepDocumentType;
import com.minicad.step.model.StepDocumentUsageConstraint;
import com.minicad.step.model.StepExternalSource;
import com.minicad.step.model.StepExternalSourceRelationship;
import com.minicad.step.model.StepExternalIdentificationAssignment;
import com.minicad.step.model.StepExternallyDefinedItem;
import com.minicad.step.model.StepAddress;
import com.minicad.step.model.StepAppliedApprovalAssignment;
import com.minicad.step.model.StepActionPropertyRepresentation;
import com.minicad.step.model.StepAppliedCertificationAssignment;
import com.minicad.step.model.StepAppliedClassificationAssignment;
import com.minicad.step.model.StepAppliedContractAssignment;
import com.minicad.step.model.StepAppliedDateAssignment;
import com.minicad.step.model.StepAppliedDateTimeAssignment;
import com.minicad.step.model.StepAppliedDocumentReference;
import com.minicad.step.model.StepAppliedExternalIdentificationAssignment;
import com.minicad.step.model.StepAppliedGroupAssignment;
import com.minicad.step.model.StepAppliedIdentificationAssignment;
import com.minicad.step.model.StepAppliedLanguageAssignment;
import com.minicad.step.model.StepAppliedNameAssignment;
import com.minicad.step.model.StepAppliedOrganizationAssignment;
import com.minicad.step.model.StepAppliedPersonAndOrganizationAssignment;
import com.minicad.step.model.StepAppliedSecurityClassificationAssignment;
import com.minicad.step.model.StepApplicationContext;
import com.minicad.step.model.StepApplicationProtocolDefinition;
import com.minicad.step.model.StepCertification;
import com.minicad.step.model.StepCertificationAssignment;
import com.minicad.step.model.StepCertificationType;
import com.minicad.step.model.StepCalendarDate;
import com.minicad.step.model.StepChainBasedGeometricItemSpecificUsage;
import com.minicad.step.model.StepChainBasedItemIdentifiedRepresentationUsage;
import com.minicad.step.model.StepClassificationAssignment;
import com.minicad.step.model.StepClassificationRole;
import com.minicad.step.model.StepCoordinatedUniversalTimeOffset;
import com.minicad.step.model.StepContract;
import com.minicad.step.model.StepContractAssignment;
import com.minicad.step.model.StepContractType;
import com.minicad.step.model.StepContextDependentShapeRepresentation;
import com.minicad.step.model.StepContextDependentUnit;
import com.minicad.step.model.StepContactRatioRepresentation;
import com.minicad.step.model.StepConicalSurface;
import com.minicad.step.model.StepConicCurve;
import com.minicad.step.model.StepDateAndTime;
import com.minicad.step.model.StepDateAssignment;
import com.minicad.step.model.StepDateRole;
import com.minicad.step.model.StepDateTimeAssignment;
import com.minicad.step.model.StepDateTimeRole;
import com.minicad.step.model.StepDescriptiveRepresentationItem;
import com.minicad.step.model.StepDescriptionAttribute;
import com.minicad.step.model.StepDerivedUnit;
import com.minicad.step.model.StepDerivedUnitElement;
import com.minicad.step.model.StepDimensionalExponents;
import com.minicad.step.model.StepDegenerateToroidalSurface;
import com.minicad.step.model.StepEffectivity;
import com.minicad.step.model.StepEffectivityRelationship;
import com.minicad.step.model.StepFillAreaStyle;
import com.minicad.step.model.StepFillAreaStyleColour;
import com.minicad.step.model.StepGeneralProperty;
import com.minicad.step.model.StepGeneralPropertyRelationship;
import com.minicad.step.model.StepGeometricItemSpecificUsage;
import com.minicad.step.model.StepGeometricRepresentationContext;
import com.minicad.step.model.StepGeometricRepresentationItem;
import com.minicad.step.model.StepGeometricReplica;
import com.minicad.step.model.StepGroup;
import com.minicad.step.model.StepGroupAssignment;
import com.minicad.step.model.StepGroupRelationship;
import com.minicad.step.model.StepHalfSpaceSolid;
import com.minicad.step.model.StepIdAttribute;
import com.minicad.step.model.StepIdentificationAssignment;
import com.minicad.step.model.StepIdentificationRole;
import com.minicad.step.model.StepItemIdentifiedRepresentationUsage;
import com.minicad.step.model.StepKinematicPropertyDefinitionRepresentation;
import com.minicad.step.model.StepKinematicPropertyMechanismRepresentation;
import com.minicad.step.model.StepKinematicPropertyRepresentationRelation;
import com.minicad.step.model.StepKinematicPropertyTopologyRepresentation;
import com.minicad.step.model.StepLanguage;
import com.minicad.step.model.StepLanguageAssignment;
import com.minicad.step.model.StepLeaderCurve;
import com.minicad.step.model.StepMechanicalDesignRequirementItemAssociation;
import com.minicad.step.model.StepMeasureRepresentationItem;
import com.minicad.step.model.StepMeasureWithUnit;
import com.minicad.step.model.StepNameAttribute;
import com.minicad.step.model.StepNameAssignment;
import com.minicad.step.model.StepNamedUnit;
import com.minicad.step.model.StepNextAssemblyUsageOccurrence;
import com.minicad.step.model.StepBoxDomain;
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
import com.minicad.step.model.StepLocalTime;
import com.minicad.step.model.StepPerson;
import com.minicad.step.model.StepPersonAndOrganization;
import com.minicad.step.model.StepPersonAndOrganizationAssignment;
import com.minicad.step.model.StepPersonAndOrganizationRole;
import com.minicad.step.model.StepPlacedDatumTargetFeature;
import com.minicad.step.model.StepPlacedTarget;
import com.minicad.step.model.StepPmiRequirementItemAssociation;
import com.minicad.step.model.StepProductCategory;
import com.minicad.step.model.StepProductCategoryRelationship;
import com.minicad.step.model.StepProduct;
import com.minicad.step.model.StepProductContext;
import com.minicad.step.model.StepProductDefinition;
import com.minicad.step.model.StepProductDefinitionContext;
import com.minicad.step.model.StepProductDefinitionEffectivity;
import com.minicad.step.model.StepProductDefinitionFormation;
import com.minicad.step.model.StepProductDefinitionFormationRelationship;
import com.minicad.step.model.StepProductDefinitionRelationship;
import com.minicad.step.model.StepProductDefinitionRelationshipRelationship;
import com.minicad.step.model.StepProductDefinitionShape;
import com.minicad.step.model.StepPropertyDefinition;
import com.minicad.step.model.StepPropertyDefinitionRepresentation;
import com.minicad.step.model.StepPropertyDefinitionRelationship;
import com.minicad.step.model.StepProductRelatedProductCategory;
import com.minicad.step.model.StepProductRelationship;
import com.minicad.step.model.StepProjectionCurve;
import com.minicad.step.model.StepPresentationLayerAssignment;
import com.minicad.step.model.StepPresentationStyleAssignment;
import com.minicad.step.model.StepPoint;
import com.minicad.step.model.StepPointStyle;
import com.minicad.step.model.StepRepresentationContext;
import com.minicad.step.model.StepRepresentationItem;
import com.minicad.step.model.StepSymbolRepresentationMap;
import com.minicad.step.model.StepOrganization;
import com.minicad.step.model.StepOrganizationAssignment;
import com.minicad.step.model.StepOrganizationRelationship;
import com.minicad.step.model.StepOrganizationRole;
import com.minicad.step.model.StepSecurityClassification;
import com.minicad.step.model.StepSecurityClassificationAssignment;
import com.minicad.step.model.StepSecurityClassificationLevel;
import com.minicad.step.model.StepSphericalSurface;
import com.minicad.step.model.StepSurfaceSideStyle;
import com.minicad.step.model.StepSurfaceStyleBoundary;
import com.minicad.step.model.StepSurfaceStyleControlGrid;
import com.minicad.step.model.StepSurfaceStyleFillArea;
import com.minicad.step.model.StepSurfaceStyleParameterLine;
import com.minicad.step.model.StepSurfaceStyleReflectanceAmbient;
import com.minicad.step.model.StepSurfaceStyleReflectanceAmbientDiffuse;
import com.minicad.step.model.StepSurfaceStyleReflectanceAmbientDiffuseSpecular;
import com.minicad.step.model.StepSurfaceStyleSegmentationCurve;
import com.minicad.step.model.StepSurfaceStyleSilhouette;
import com.minicad.step.model.StepSurfaceStyleTransparent;
import com.minicad.step.model.StepSurfaceStyleUsage;
import com.minicad.step.model.StepTerminatorSymbol;
import com.minicad.step.model.StepTextStyle;
import com.minicad.step.model.StepTextStyleForDefinedFont;
import com.minicad.step.model.StepTextStyleWithBoxCharacteristics;
import com.minicad.step.model.StepTextStyleWithJustification;
import com.minicad.step.model.StepTextStyleWithMirror;
import com.minicad.step.model.StepTextStyleWithSpacing;
import com.minicad.step.model.StepSymbolColour;
import com.minicad.step.model.StepSymbolStyle;
import com.minicad.step.model.StepShapeAspect;
import com.minicad.step.model.StepShapeAspectOccurrence;
import com.minicad.step.model.StepShapeAspectRelationship;
import com.minicad.step.model.StepShapeDefinitionRepresentation;
import com.minicad.step.model.StepCurve;
import com.minicad.step.model.StepSurface;
import com.minicad.step.model.StepSurfaceModel;
import com.minicad.step.model.StepSolidModel;
import com.minicad.step.model.StepToroidalSurface;
import com.minicad.step.model.StepVertex;
import com.minicad.step.model.StepEdge;
import com.minicad.step.model.StepFace;
import com.minicad.step.model.StepSiUnit;
import com.minicad.step.model.StepTopologicalRepresentationItem;
import com.minicad.step.model.StepTypedMeasureWithUnit;
import com.minicad.step.model.StepUncertaintyMeasureWithUnit;
import com.minicad.step.model.StepCylindricalSurface;
import com.minicad.step.model.StepUserDefinedCurveFont;
import com.minicad.step.model.StepUserDefinedMarker;
import com.minicad.step.model.StepUserDefinedTerminatorSymbol;
import com.minicad.step.model.StepValueRepresentationItem;
import com.minicad.step.model.StepConversionBasedUnit;
import com.minicad.step.model.StepConversionBasedUnitWithOffset;
import com.minicad.step.model.StepGlobalUnitAssignedContext;
import com.minicad.step.model.StepGlobalUncertaintyAssignedContext;
import com.minicad.step.model.StepAbstractVariable;
import com.minicad.step.model.StepBackChainingRuleBody;
import com.minicad.step.model.StepBezierCurve;
import com.minicad.step.model.StepBezierSurface;
import com.minicad.step.model.StepBoundedCurve;
import com.minicad.step.model.StepBoundedSurface;
import com.minicad.step.model.StepBSplineCurve;
import com.minicad.step.model.StepBSplineSurface;
import com.minicad.step.model.StepForwardChainingRulePremise;
import com.minicad.step.model.StepPiecewiseBezierCurve;
import com.minicad.step.model.StepPiecewiseBezierSurface;
import com.minicad.step.model.StepQuasiUniformCurve;
import com.minicad.step.model.StepQuasiUniformSurface;
import com.minicad.step.model.StepRowVariable;
import com.minicad.step.model.StepScalarVariable;
import com.minicad.step.model.StepUniformCurve;
import com.minicad.step.model.StepUniformSurface;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
     * @throws IOException if reading the file fails
     */
    public static void main(String[] args) throws IOException {
        int exitCode = run(args, log::info, log::error);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    static int run(String[] args, Consumer<String> out, Consumer<String> err) throws IOException {
        if (args.length != 1) {
            err.accept("Usage: StepDumpApp <step-file>");
            return 2;
        }

        Path path = Path.of(args[0]);
        String text = StepTextReader.read(path);

        try {
            StepFile stepFile = StepParser.parse(text);
            Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
            StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

            List<String> lines = new ArrayList<>();
            lines.add("File: " + path);
            lines.add("");
            appendSyntaxSummary(stepFile, lines);
            lines.add("");
            appendSemanticSummary(resolved, lines);
            lines.add("");
            appendBuildSummary(resolved, builder, lines);
            lines.forEach(out);
            return 0;
        } catch (StepParseException | StepResolutionException | UnsupportedGeometryException | TopologyException | GeometryException ex) {
            err.accept("STEP processing failed: " + ex.getMessage());
            return 1;
        }
    }

    private static void appendSyntaxSummary(StepFile file, List<String> lines) {
        lines.add("Syntax Summary");
        lines.add("  entityCount: " + file.entities().size());
        if (!file.entities().isEmpty()) {
            lines.add("  firstId: #" + file.entities().getFirst().id());
            lines.add("  lastId: #" + file.entities().getLast().id());
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
        if (entity instanceof com.minicad.step.model.StepFaceBound faceBound) {
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
        if (entity instanceof com.minicad.step.model.StepRepresentation representation) {
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
            if (value instanceof String name && !name.isBlank()) {
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
        Map<String, Integer> unsupportedReasons = new LinkedHashMap<>();
        Map<String, Integer> unsupportedReasonCodes = new LinkedHashMap<>();
        Set<Integer> shellFaceIds = collectShellFaceIds(resolved.values());
        Set<Integer> loopOrientedEdgeIds = collectLoopOrientedEdgeIds(resolved.values());
        Set<Integer> orientedEdgeElementIds = collectOrientedEdgeElementIds(resolved.values());
        Set<Integer> faceBoundLoopIds = collectFaceBoundLoopIds(resolved.values());

        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepOpenShell openShell) {
                FaceBuildCounts counts = summarizeShell(openShell.faces(), builder);
                lines.add("  " + stepEntityTypeName(openShell) + " #" + openShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                openShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepSurfacedOpenShell surfacedOpenShell) {
                FaceBuildCounts counts = summarizeShell(surfacedOpenShell.faces(), builder);
                lines.add("  " + stepEntityTypeName(surfacedOpenShell) + " #" + surfacedOpenShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                openShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepClosedShell closedShell) {
                FaceBuildCounts counts = summarizeShell(closedShell.faces(), builder);
                lines.add("  " + stepEntityTypeName(closedShell) + " #" + closedShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                closedShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepOrientedOpenShell orientedOpenShell) {
                FaceBuildCounts counts = summarizeShell(orientedOpenShell.faces(), builder);
                lines.add("  " + stepEntityTypeName(orientedOpenShell) + " #" + orientedOpenShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                openShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepOrientedClosedShell orientedClosedShell) {
                FaceBuildCounts counts = summarizeShell(orientedClosedShell.faces(), builder);
                lines.add("  " + stepEntityTypeName(orientedClosedShell) + " #" + orientedClosedShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                closedShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepManifoldSolidBrep solidBrep) {
                FaceBuildCounts counts = summarizeShell(shellFaces(solidBrep.outer()), builder);
                lines.add("  " + stepEntityTypeName(solidBrep) + " #" + solidBrep.id() + ": shellFaces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                solids++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepBrepWithVoids brepWithVoids) {
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
            } else if (entity instanceof StepSweptAreaSolid sweptAreaSolid) {
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
            } else if (entity instanceof StepSolidReplica solidReplica) {
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
            } else if (entity instanceof StepCsgSolid csgSolid) {
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
            } else if (entity instanceof StepCsgPrimitive csgPrimitive) {
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
            } else if (entity instanceof StepBooleanClippingResult clippingResult) {
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
            } else if (entity instanceof StepBooleanResult booleanResult) {
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
            } else if (entity instanceof StepFaceEntity face && !shellFaceIds.contains(face.id())) {
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
            } else if (entity instanceof StepSubedge subedge && !orientedEdgeElementIds.contains(subedge.id())) {
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
            } else if (entity instanceof StepPath path) {
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
            } else if (entity instanceof StepOpenPath openPath) {
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
            } else if (entity instanceof StepSubpath subpath) {
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
            } else if (entity instanceof StepOrientedPath orientedPath) {
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
            } else if (entity instanceof StepConnectedEdgeSet edgeSet) {
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
            } else if (entity instanceof StepWireShell wireShell) {
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
            } else if (entity instanceof StepVertexShell vertexShell) {
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
            } else if (entity instanceof StepEdgeBasedWireframeModel wireframeModel) {
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
            } else if (entity instanceof StepShellBasedWireframeModel wireframeModel) {
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
            } else if (entity instanceof StepFaceBasedSurfaceModel surfaceModel) {
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
            } else if (entity instanceof StepShellBasedSurfaceModel surfaceModel) {
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
            } else if (entity instanceof StepGeometricCurveSet curveSet) {
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
            } else if (entity instanceof StepPointSet pointSet) {
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
            } else if (entity instanceof StepGeometricSet geometricSet) {
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
            } else if (entity instanceof StepRepresentation representation) {
                standaloneContainerEntities++;
                try {
                    int itemCount = validateRepresentation(representation, builder);
                    lines.add("  " + stepEntityTypeName(representation) + " #" + representation.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(representation) + " #" + representation.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepRepresentationMap representationMap) {
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
            } else if (entity instanceof StepMappedItem mappedItem) {
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
            } else if (entity instanceof StepStyledItem styledItem) {
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
            } else if (entity instanceof StepOverRidingStyledItem styledItem) {
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
            } else if (entity instanceof StepRepresentationRelationship relationship) {
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
            } else if (entity instanceof StepRepresentationRelationshipWithTransformation relationship) {
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
            } else if (entity instanceof StepShapeRepresentationRelationship relationship) {
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
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(entity) + " #" + entity.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                } catch (GeometryException | TopologyException | StepResolutionException ex) {
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
                + ", unsupportedFaces=" + unsupportedFaces);
        appendUnsupportedReasons(lines, unsupportedReasons);
        appendUnsupportedReasonCodes(lines, unsupportedReasonCodes);
    }

    private static Set<Integer> collectShellFaceIds(Iterable<StepEntity> entities) {
        Set<Integer> ids = new HashSet<>();
        for (StepEntity entity : entities) {
            if (entity instanceof StepOpenShell openShell) {
                openShell.faces().forEach(face -> ids.add(face.id()));
            } else if (entity instanceof StepSurfacedOpenShell surfacedOpenShell) {
                surfacedOpenShell.faces().forEach(face -> ids.add(face.id()));
            } else if (entity instanceof StepOrientedOpenShell orientedOpenShell) {
                orientedOpenShell.faces().forEach(face -> ids.add(face.id()));
            } else if (entity instanceof StepClosedShell closedShell) {
                closedShell.faces().forEach(face -> ids.add(face.id()));
            } else if (entity instanceof StepOrientedClosedShell orientedClosedShell) {
                orientedClosedShell.faces().forEach(face -> ids.add(face.id()));
            }
        }
        return ids;
    }

    private static Set<Integer> collectLoopOrientedEdgeIds(Iterable<StepEntity> entities) {
        Set<Integer> ids = new HashSet<>();
        for (StepEntity entity : entities) {
            if (entity instanceof com.minicad.step.model.StepEdgeLoop edgeLoop) {
                edgeLoop.edges().forEach(edge -> ids.add(edge.id()));
            }
        }
        return ids;
    }

    private static Set<Integer> collectOrientedEdgeElementIds(Iterable<StepEntity> entities) {
        Set<Integer> ids = new HashSet<>();
        for (StepEntity entity : entities) {
            if (entity instanceof StepOrientedEdge orientedEdge) {
                ids.add(orientedEdge.edgeElement().id());
            }
        }
        return ids;
    }

    private static Set<Integer> collectFaceBoundLoopIds(Iterable<StepEntity> entities) {
        Set<Integer> ids = new HashSet<>();
        for (StepEntity entity : entities) {
            if (entity instanceof StepFaceBound faceBound) {
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
            if (edge instanceof StepEdgeCurve edgeCurve) {
                builder.buildEdge(edgeCurve.id());
                count++;
            } else if (edge instanceof StepSubedge subedge) {
                builder.buildEdge(subedge.id());
                count++;
            } else if (edge instanceof StepOrientedEdge orientedEdge) {
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
            if (loop instanceof StepEdgeLoop edgeLoop) {
                builder.buildEdgeLoop(edgeLoop.id());
            } else if (loop instanceof StepVertexLoop vertexLoop) {
                builder.buildVertexLoop(vertexLoop.id());
            } else if (loop instanceof StepPolyLoop polyLoop) {
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
            if (boundary instanceof StepWireShell wireShell) {
                validateWireShell(wireShell, builder);
            } else if (boundary instanceof StepVertexShell vertexShell) {
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
            if (faceSet instanceof StepConnectedFaceSet connectedFaceSet) {
                counts = counts.plus(summarizeShell(connectedFaceSet.faces(), builder));
            } else if (faceSet instanceof StepConnectedFaceSubSet connectedFaceSubSet) {
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
            if (element instanceof StepCartesianPoint point) {
                builder.buildPoint(point.id());
            } else if (element instanceof StepVertexPoint vertexPoint) {
                builder.buildVertex(vertexPoint.id());
            } else if (element instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName())) {
                builder.buildPointReference(replica.id());
            } else if (element instanceof StepLine line) {
                builder.buildLine(line.id());
            } else if (element instanceof StepCircle circle) {
                builder.buildCircle(circle.id());
            } else if (element instanceof StepEllipse ellipse) {
                builder.buildEllipse(ellipse.id());
            } else if (element instanceof StepPolyline polyline) {
                builder.buildPolyline(polyline.id());
            } else if (element instanceof StepEdgeCurve edgeCurve) {
                builder.buildEdge(edgeCurve.id());
            } else if (element instanceof StepSubedge subedge) {
                builder.buildEdge(subedge.id());
            } else if (element instanceof StepOrientedEdge orientedEdge) {
                builder.buildOrientedEdge(orientedEdge.id());
            } else if (element instanceof StepConnectedEdgeSet edgeSet) {
                validateConnectedEdgeSet(edgeSet, builder);
            } else if (element instanceof StepEdgeLoop edgeLoop) {
                validatePathEdges(edgeLoop.edges(), builder);
            } else if (element instanceof StepVertexLoop vertexLoop) {
                builder.buildVertexLoop(vertexLoop.id());
            } else if (element instanceof StepPath path) {
                validatePathEdges(path.edges(), builder);
            } else if (element instanceof StepOpenPath openPath) {
                validatePathEdges(openPath.edges(), builder);
            } else if (element instanceof StepSubpath subpath) {
                validatePathEdges(subpath.edges(), builder);
            } else if (element instanceof StepOrientedPath orientedPath) {
                validatePathEdges(orientedPath.edges(), builder);
            } else if (element instanceof StepPolyLoop polyLoop) {
                validatePolyLoop(polyLoop, builder);
            } else if (element instanceof StepWireShell wireShell) {
                validateWireShell(wireShell, builder);
            } else if (element instanceof StepVertexShell
                    || element instanceof StepEdgeBasedWireframeModel
                    || element instanceof StepShellBasedWireframeModel) {
                validateSummaryEntity(element, builder);
            } else if (element instanceof StepPointSet pointSet) {
                validatePointSet(pointSet, builder);
            } else if (element instanceof StepGeometricSet geometricSet) {
                validateGeometricSet(geometricSet, builder);
            } else if (element instanceof StepGeometricCurveSet nestedCurveSet) {
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
            if (point instanceof StepCartesianPoint cartesianPoint) {
                builder.buildPoint(cartesianPoint.id());
            } else if (point instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName())) {
                builder.buildPointReference(replica.id());
            } else if (point instanceof StepVertexPoint vertexPoint) {
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
            } else if (point instanceof StepPointSet nestedPointSet) {
                validatePointSet(nestedPointSet, builder);
            } else if (point instanceof StepGeometricSet geometricSet) {
                validateGeometricSet(geometricSet, builder);
            } else if (point instanceof StepGeometricCurveSet curveSet) {
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
            if (element instanceof StepCartesianPoint cartesianPoint) {
                builder.buildPoint(cartesianPoint.id());
            } else if (element instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName())) {
                builder.buildPointReference(replica.id());
            } else if (element instanceof StepVertexPoint vertexPoint) {
                builder.buildVertex(vertexPoint.id());
            } else if (element instanceof StepLine line) {
                builder.buildLine(line.id());
            } else if (element instanceof StepCircle circle) {
                builder.buildCircle(circle.id());
            } else if (element instanceof StepEllipse ellipse) {
                builder.buildEllipse(ellipse.id());
            } else if (element instanceof StepPolyline polyline) {
                builder.buildPolyline(polyline.id());
            } else if (element instanceof StepEdgeCurve edgeCurve) {
                builder.buildEdge(edgeCurve.id());
            } else if (element instanceof StepSubedge subedge) {
                builder.buildEdge(subedge.id());
            } else if (element instanceof StepOrientedEdge orientedEdge) {
                builder.buildOrientedEdge(orientedEdge.id());
            } else if (element instanceof StepConnectedEdgeSet edgeSet) {
                validateConnectedEdgeSet(edgeSet, builder);
            } else if (element instanceof StepEdgeLoop edgeLoop) {
                validatePathEdges(edgeLoop.edges(), builder);
            } else if (element instanceof StepVertexLoop vertexLoop) {
                builder.buildVertexLoop(vertexLoop.id());
            } else if (element instanceof StepWireShell wireShell) {
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
                    || element instanceof StepSolidReplica
                    || element instanceof StepCsgSolid
                    || element instanceof StepCsgPrimitive
                    || element instanceof StepBooleanResult
                    || element instanceof StepBooleanClippingResult) {
                validateSummaryEntity(element, builder);
            } else if (element instanceof StepPath path) {
                validatePathEdges(path.edges(), builder);
            } else if (element instanceof StepOpenPath openPath) {
                validatePathEdges(openPath.edges(), builder);
            } else if (element instanceof StepSubpath subpath) {
                validatePathEdges(subpath.edges(), builder);
            } else if (element instanceof StepOrientedPath orientedPath) {
                validatePathEdges(orientedPath.edges(), builder);
            } else if (element instanceof StepPolyLoop polyLoop) {
                validatePolyLoop(polyLoop, builder);
            } else if (isSupportedGeometricSetSurface(element)) {
                validateSupportedSurfaceReference(element, builder);
            } else if (element instanceof StepPointSet pointSet) {
                validatePointSet(pointSet, builder);
            } else if (element instanceof StepGeometricSet nestedGeometricSet) {
                validateGeometricSet(nestedGeometricSet, builder);
            } else if (element instanceof StepGeometricCurveSet curveSet) {
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
        if (surface instanceof StepPlane plane) {
            builder.buildPlane(plane.id());
        } else if (surface instanceof StepCylindricalSurface cylindricalSurface) {
            builder.buildCylindricalSurface(cylindricalSurface.id());
        } else if (surface instanceof StepConicalSurface conicalSurface) {
            builder.buildConicalSurface(conicalSurface.id());
        } else if (surface instanceof StepSphericalSurface sphericalSurface) {
            builder.buildSphericalSurface(sphericalSurface.id());
        } else if (surface instanceof StepToroidalSurface toroidalSurface) {
            builder.buildToroidalSurface(toroidalSurface.id());
        } else if (surface instanceof StepDegenerateToroidalSurface degenerateToroidalSurface) {
            builder.buildDegenerateToroidalSurface(degenerateToroidalSurface.id());
        } else if (surface instanceof StepSurfaceOfLinearExtrusion extrusionSurface) {
            builder.buildSurfaceOfLinearExtrusion(extrusionSurface.id());
        } else if (surface instanceof StepSurfaceOfRevolution revolutionSurface) {
            builder.buildSurfaceOfRevolution(revolutionSurface.id());
        } else if (surface instanceof StepBSplineSurfaceWithKnots splineSurface) {
            builder.buildBSplineSurface(splineSurface.id());
        } else if (surface instanceof StepRationalBSplineSurface rationalSplineSurface) {
            builder.buildRationalBSplineSurface(rationalSplineSurface.id());
        } else if (surface instanceof StepRectangularTrimmedSurface trimmedSurface) {
            builder.buildRectangularTrimmedSurface(trimmedSurface.id());
        } else if (surface instanceof StepCurveBoundedSurface boundedSurface) {
            builder.buildCurveBoundedSurface(boundedSurface.id());
        } else if (surface instanceof StepOrientedSurface orientedSurface) {
            builder.buildOrientedSurface(orientedSurface.id());
        } else if (surface instanceof StepOffsetSurface offsetSurface) {
            builder.buildOffsetSurface(offsetSurface.id());
        } else if (surface instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName())) {
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

    private static int validateSummaryEntity(StepEntity entity, StepCadBuilder builder) {
        if (entity instanceof StepCartesianPoint point) {
            validatePoint(point, builder);
            return 1;
        }
        if (entity instanceof StepDirection direction) {
            validateDirection(direction, builder);
            return 1;
        }
        if (entity instanceof StepVector vector) {
            builder.buildVector(vector.id());
            return 1;
        }
        if (entity instanceof StepVertexPoint vertexPoint) {
            builder.buildVertex(vertexPoint.id());
            return 1;
        }
        if (entity instanceof StepConicCurve conicCurve) {
            return validateSummaryEntity(conicCurve.position(), builder);
        }
        if (entity instanceof StepLine line) {
            validateLine(line, builder);
            return 1;
        }
        if (entity instanceof StepCircle circle) {
            validateCircle(circle, builder);
            return 1;
        }
        if (entity instanceof StepEllipse ellipse) {
            validateEllipse(ellipse, builder);
            return 1;
        }
        if (entity instanceof StepPolyline polyline) {
            validatePolyline(polyline, builder);
            return 1;
        }
        if (entity instanceof StepBSplineCurveWithKnots splineCurve) {
            builder.buildBSplineCurve(splineCurve.id());
            return 1;
        }
        if (entity instanceof StepRationalBSplineCurve splineCurve) {
            builder.buildRationalBSplineCurve(splineCurve.id());
            return 1;
        }
        if (entity instanceof StepTrimmedCurve trimmedCurve) {
            builder.buildTrimmedCurve(trimmedCurve.id());
            return 1;
        }
        if (entity instanceof StepSurfaceCurve surfaceCurve) {
            builder.buildSurfaceCurve(surfaceCurve.id());
            return 1;
        }
        if (entity instanceof StepSeamCurve seamCurve) {
            builder.buildSeamCurve(seamCurve.id());
            return 1;
        }
        if (entity instanceof StepCompositeCurve compositeCurve) {
            builder.buildCompositeCurve(compositeCurve.id());
            return 1;
        }
        if (entity instanceof StepCompositeCurveOnSurface compositeCurveOnSurface) {
            builder.buildCompositeCurve(compositeCurveOnSurface.id());
            return 1;
        }
        if (entity instanceof StepCompositeCurveSegment segment) {
            return validateSummaryEntity(segment.parentCurve(), builder);
        }
        if (entity instanceof StepOffsetCurve2D offsetCurve2D) {
            builder.buildOffsetCurve2(offsetCurve2D.id());
            return 1;
        }
        if (entity instanceof StepOffsetCurve3D offsetCurve3D) {
            builder.buildOffsetCurve3(offsetCurve3D.id());
            return 1;
        }
        if (entity instanceof StepOrientedCurve orientedCurve) {
            builder.buildCurveReference3(orientedCurve.id());
            return validateSummaryEntity(orientedCurve.curveElement(), builder);
        }
        if (entity instanceof StepPcurve pcurve) {
            builder.buildPcurve2(pcurve.id());
            return 1;
        }
        if (entity instanceof StepDegeneratePcurve degeneratePcurve) {
            builder.buildPcurve2(degeneratePcurve.id());
            return validateSummaryEntity(degeneratePcurve.basisSurface(), builder)
                    + validateSummaryEntity(degeneratePcurve.referenceToCurve(), builder);
        }
        if (entity instanceof StepGeometricReplica replica) {
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
        if (entity instanceof StepPlane plane) {
            builder.buildPlane(plane.id());
            return 1;
        }
        if (entity instanceof StepCylindricalSurface cylindricalSurface) {
            builder.buildCylindricalSurface(cylindricalSurface.id());
            return 1;
        }
        if (entity instanceof StepConicalSurface conicalSurface) {
            builder.buildConicalSurface(conicalSurface.id());
            return 1;
        }
        if (entity instanceof StepToroidalSurface toroidalSurface) {
            builder.buildToroidalSurface(toroidalSurface.id());
            return 1;
        }
        if (entity instanceof StepSphericalSurface sphericalSurface) {
            builder.buildSphericalSurface(sphericalSurface.id());
            return 1;
        }
        if (entity instanceof StepDegenerateToroidalSurface degenerateToroidalSurface) {
            builder.buildDegenerateToroidalSurface(degenerateToroidalSurface.id());
            return 1;
        }
        if (entity instanceof StepBSplineSurfaceWithKnots splineSurface) {
            builder.buildBSplineSurface(splineSurface.id());
            return 1;
        }
        if (entity instanceof StepRationalBSplineSurface rationalSplineSurface) {
            builder.buildRationalBSplineSurface(rationalSplineSurface.id());
            return 1;
        }
        if (entity instanceof StepSurfaceOfLinearExtrusion extrusionSurface) {
            builder.buildSurfaceOfLinearExtrusion(extrusionSurface.id());
            return validateSummaryEntity(extrusionSurface.sweptCurve(), builder)
                    + validateSummaryEntity(extrusionSurface.extrusionAxis(), builder);
        }
        if (entity instanceof StepSurfaceOfRevolution revolutionSurface) {
            builder.buildSurfaceOfRevolution(revolutionSurface.id());
            return validateSummaryEntity(revolutionSurface.sweptCurve(), builder)
                    + validateSummaryEntity(revolutionSurface.axisPosition(), builder);
        }
        if (entity instanceof StepRectangularTrimmedSurface trimmedSurface) {
            builder.buildRectangularTrimmedSurface(trimmedSurface.id());
            return validateSummaryEntity(trimmedSurface.basisSurface(), builder);
        }
        if (entity instanceof StepCurveBoundedSurface boundedSurface) {
            builder.buildCurveBoundedSurface(boundedSurface.id());
            return validateSummaryEntity(boundedSurface.basisSurface(), builder)
                    + validateSummaryItems(boundedSurface.boundaries(), builder);
        }
        if (entity instanceof StepOrientedSurface orientedSurface) {
            builder.buildOrientedSurface(orientedSurface.id());
            return validateSummaryEntity(orientedSurface.surfaceElement(), builder);
        }
        if (entity instanceof StepOffsetSurface offsetSurface) {
            builder.buildOffsetSurface(offsetSurface.id());
            return validateSummaryEntity(offsetSurface.basisSurface(), builder);
        }
        if (entity instanceof StepAxis2Placement3D placement3D) {
            builder.buildPlacement(placement3D.id());
            return 1;
        }
        if (entity instanceof StepAxis1Placement axis1Placement) {
            builder.buildAxis1Placement(axis1Placement.id());
            return 1;
        }
        if (entity instanceof StepAxis2Placement2D placement2D) {
            validatePoint(placement2D.location(), builder);
            validateDirection(placement2D.refDirection(), builder);
            return 1;
        }
        if (entity instanceof StepCartesianTransformationOperator transformation) {
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
        if (entity instanceof StepItemDefinedTransformation transformation) {
            builder.buildPlacement(transformation.transformItem1().id());
            builder.buildPlacement(transformation.transformItem2().id());
            return 1;
        }
        if (entity instanceof StepEdgeCurve edgeCurve) {
            builder.buildEdge(edgeCurve.id());
            return 1;
        }
        if (entity instanceof StepSubedge subedge) {
            builder.buildEdge(subedge.id());
            return 1;
        }
        if (entity instanceof StepOrientedEdge orientedEdge) {
            builder.buildOrientedEdge(orientedEdge.id());
            return 1;
        }
        if (entity instanceof StepEdgeLoop edgeLoop) {
            builder.buildEdgeLoop(edgeLoop.id());
            return 1;
        }
        if (entity instanceof StepVertexLoop vertexLoop) {
            builder.buildVertexLoop(vertexLoop.id());
            return 1;
        }
        if (entity instanceof StepPolyLoop polyLoop) {
            validatePolyLoop(polyLoop, builder);
            return 1;
        }
        if (entity instanceof StepPath path) {
            validatePathEdges(path.edges(), builder);
            return 1;
        }
        if (entity instanceof StepOpenPath openPath) {
            validatePathEdges(openPath.edges(), builder);
            return 1;
        }
        if (entity instanceof StepSubpath subpath) {
            validatePathEdges(subpath.edges(), builder);
            return 1;
        }
        if (entity instanceof StepOrientedPath orientedPath) {
            validatePathEdges(orientedPath.edges(), builder);
            return 1;
        }
        if (entity instanceof StepConnectedEdgeSet edgeSet) {
            return validateConnectedEdgeSet(edgeSet, builder);
        }
        if (entity instanceof StepWireShell wireShell) {
            return validateWireShell(wireShell, builder);
        }
        if (entity instanceof StepVertexShell vertexShell) {
            builder.buildVertexLoop(vertexShell.extent().id());
            return 1;
        }
        if (entity instanceof StepEdgeBasedWireframeModel wireframeModel) {
            int count = 0;
            for (StepConnectedEdgeSet boundary : wireframeModel.boundaries()) {
                count += validateConnectedEdgeSet(boundary, builder);
            }
            return count;
        }
        if (entity instanceof StepShellBasedWireframeModel wireframeModel) {
            return validateShellBasedWireframeModel(wireframeModel, builder);
        }
        if (entity instanceof StepFaceEntity face) {
            builder.buildFace(face.id());
            return 1;
        }
        if (entity instanceof StepFaceBasedSurfaceModel surfaceModel) {
            return validateFaceBasedSurfaceModel(surfaceModel, builder).supportedFaces();
        }
        if (entity instanceof StepShellBasedSurfaceModel surfaceModel) {
            return validateShellBasedSurfaceModel(surfaceModel, builder).supportedFaces();
        }
        if (entity instanceof StepConnectedFaceSet connectedFaceSet) {
            return summarizeShell(connectedFaceSet.faces(), builder).supportedFaces();
        }
        if (entity instanceof StepConnectedFaceSubSet connectedFaceSubSet) {
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
                || entity instanceof StepSolidReplica
                || entity instanceof StepCsgSolid
                || entity instanceof StepCsgPrimitive
                || entity instanceof StepBooleanResult
                || entity instanceof StepBooleanClippingResult) {
            return builder.buildSolid(entity.id()).outerShell().faces().size();
        }
        if (entity instanceof StepPointSet pointSet) {
            return validatePointSet(pointSet, builder);
        }
        if (entity instanceof StepGeometricCurveSet curveSet) {
            return validateGeometricCurveSet(curveSet, builder);
        }
        if (entity instanceof StepGeometricSet geometricSet) {
            return validateGeometricSet(geometricSet, builder);
        }
        if (entity instanceof StepBoxDomain boxDomain) {
            return validateSummaryEntity(boxDomain.corner(), builder);
        }
        if (entity instanceof StepHalfSpaceSolid halfSpaceSolid) {
            int count = validateSummaryEntity(halfSpaceSolid.baseSurface(), builder);
            if (halfSpaceSolid.enclosure() != null) {
                count += validateSummaryEntity(halfSpaceSolid.enclosure(), builder);
            }
            return count;
        }
        if (entity instanceof StepProfileDef profileDef) {
            int count = 0;
            if (profileDef.position() != null) {
                count += validateSummaryEntity(profileDef.position(), builder);
            }
            count += validateSummaryItems(profileDef.curves(), builder);
            return Math.max(1, count);
        }
        if (entity instanceof StepRepresentation representation) {
            return validateRepresentation(representation, builder);
        }
        if (entity instanceof StepRepresentationMap representationMap) {
            return validateRepresentationMap(representationMap, builder);
        }
        if (entity instanceof StepMappedItem mappedItem) {
            return validateMappedItem(mappedItem, builder);
        }
        if (entity instanceof StepStyledItem styledItem) {
            return validateStyledItem(styledItem, builder);
        }
        if (entity instanceof StepOverRidingStyledItem styledItem) {
            return validateOverridingStyledItem(styledItem, builder);
        }
        if (entity instanceof StepRepresentationRelationship relationship) {
            return validateRepresentationRelationship(relationship, builder);
        }
        if (entity instanceof StepRepresentationRelationshipWithTransformation relationship) {
            return validateRepresentationRelationshipWithTransformation(relationship, builder);
        }
        if (entity instanceof StepShapeRepresentationRelationship relationship) {
            return validateShapeRepresentationRelationship(relationship, builder);
        }
        if (entity instanceof StepAnnotationCurveOccurrence annotationCurveOccurrence) {
            return validateAnnotationCurveOccurrence(annotationCurveOccurrence.item(), builder);
        }
        if (entity instanceof StepDraughtingAnnotationOccurrence annotationOccurrence) {
            return validateSummaryEntity(annotationOccurrence.item(), builder);
        }
        if (entity instanceof StepLeaderCurve leaderCurve) {
            return validateAnnotationCurveOccurrence(leaderCurve.item(), builder);
        }
        if (entity instanceof StepDimensionCurve dimensionCurve) {
            return validateAnnotationCurveOccurrence(dimensionCurve.item(), builder);
        }
        if (entity instanceof StepProjectionCurve projectionCurve) {
            return validateAnnotationCurveOccurrence(projectionCurve.item(), builder);
        }
        if (entity instanceof StepAnnotationFillArea fillArea) {
            return validateAnnotationFillArea(fillArea, builder);
        }
        if (entity instanceof StepAnnotationFillAreaOccurrence fillAreaOccurrence) {
            return validateAnnotationFillArea(fillAreaOccurrence.item(), builder)
                    + validateSummaryEntity(fillAreaOccurrence.fillStyleTarget(), builder);
        }
        if (entity instanceof StepAnnotationPlaceholderOccurrence placeholderOccurrence) {
            return validateSummaryEntity(placeholderOccurrence.item(), builder);
        }
        if (entity instanceof StepAnnotationPointOccurrence pointOccurrence) {
            return validateSummaryEntity(pointOccurrence.item(), builder);
        }
        if (entity instanceof StepAnnotationTextOccurrence textOccurrence) {
            validateSummaryEntity(textOccurrence.position(), builder);
            return 1;
        }
        if (entity instanceof StepAnnotationSymbolOccurrence symbolOccurrence) {
            return validateSummaryEntity(symbolOccurrence.item(), builder);
        }
        if (entity instanceof StepAnnotationSubfigureOccurrence subfigureOccurrence) {
            return validateSummaryEntity(subfigureOccurrence.item(), builder);
        }
        if (entity instanceof StepTerminatorSymbol terminatorSymbol) {
            return validateSummaryEntity(terminatorSymbol.item(), builder)
                    + validateSummaryEntity(terminatorSymbol.annotatedCurve(), builder);
        }
        if (entity instanceof StepAnnotationPlane annotationPlane) {
            return validateAnnotationPlane(annotationPlane, builder);
        }
        if (entity instanceof StepDraughtingCallout callout) {
            return validateDraughtingCallout(callout, builder);
        }
        if (entity instanceof StepDraughtingCalloutRelationship relationship) {
            return validateDraughtingCallout(relationship.relatingCallout(), builder)
                    + validateDraughtingCallout(relationship.relatedCallout(), builder);
        }
        if (entity instanceof StepAnnotationOccurrenceRelationship relationship) {
            return validateSummaryEntity(relationship.relatingAnnotationOccurrence(), builder)
                    + validateSummaryEntity(relationship.relatedAnnotationOccurrence(), builder);
        }
        if (entity instanceof StepSymbolRepresentationMap representationMap) {
            validateSummaryEntity(representationMap.mappedOrigin(), builder);
            return validateRepresentation(representationMap.mappedRepresentation(), builder);
        }
        if (entity instanceof StepAnnotationSymbol annotationSymbol) {
            int count = validateSummaryEntity(annotationSymbol.mappingSource(), builder);
            return count + validateSummaryEntity(annotationSymbol.mappingTarget(), builder);
        }
        if (entity instanceof StepAnnotationText annotationText) {
            int count = validateSummaryEntity(annotationText.mappingSource(), builder);
            return count + validateSummaryEntity(annotationText.mappingTarget(), builder);
        }
        if (entity instanceof StepAnnotationTextCharacter annotationTextCharacter) {
            int count = validateSummaryEntity(annotationTextCharacter.mappingSource(), builder);
            return count + validateSummaryEntity(annotationTextCharacter.mappingTarget(), builder);
        }
        if (entity instanceof StepPresentationLayerAssignment layerAssignment) {
            return validateSummaryItems(layerAssignment.assignedItems(), builder);
        }
        if (entity instanceof StepPresentationStyleAssignment assignment) {
            return validatePresentationStyleAssignment(assignment, builder);
        }
        if (entity instanceof StepCurveStyle curveStyle) {
            return validateCurveStyle(curveStyle, builder);
        }
        if (entity instanceof StepPointStyle pointStyle) {
            return validateSummaryEntity(pointStyle.marker(), builder)
                    + validateSummaryEntity(pointStyle.colour(), builder);
        }
        if (entity instanceof StepSymbolStyle symbolStyle) {
            return validateSummaryEntity(symbolStyle.styleOfSymbol(), builder);
        }
        if (entity instanceof StepFillAreaStyleColour fillAreaStyleColour) {
            return validateSummaryEntity(fillAreaStyleColour.colour(), builder);
        }
        if (entity instanceof StepFillAreaStyle fillAreaStyle) {
            return validateFillAreaStyle(fillAreaStyle, builder);
        }
        if (entity instanceof StepSurfaceStyleFillArea surfaceStyleFillArea) {
            return validateSummaryEntity(surfaceStyleFillArea.fillStyle(), builder);
        }
        if (entity instanceof StepSurfaceStyleBoundary surfaceStyleBoundary) {
            return validateCurveStyle(surfaceStyleBoundary.style(), builder);
        }
        if (entity instanceof StepSurfaceStyleParameterLine surfaceStyleParameterLine) {
            return validateCurveStyle(surfaceStyleParameterLine.style(), builder);
        }
        if (entity instanceof StepSurfaceStyleControlGrid surfaceStyleControlGrid) {
            return validateCurveStyle(surfaceStyleControlGrid.style(), builder);
        }
        if (entity instanceof StepSurfaceStyleSegmentationCurve surfaceStyleSegmentationCurve) {
            return validateCurveStyle(surfaceStyleSegmentationCurve.style(), builder);
        }
        if (entity instanceof StepSurfaceStyleSilhouette surfaceStyleSilhouette) {
            return validateCurveStyle(surfaceStyleSilhouette.style(), builder);
        }
        if (entity instanceof StepSurfaceStyleTransparent
                || entity instanceof StepSurfaceStyleReflectanceAmbient
                || entity instanceof StepSurfaceStyleReflectanceAmbientDiffuse) {
            return 1;
        }
        if (entity instanceof StepSurfaceStyleReflectanceAmbientDiffuseSpecular specular) {
            return 1 + validateSummaryEntity(specular.specularColour(), builder);
        }
        if (entity instanceof StepSurfaceSideStyle surfaceSideStyle) {
            return validateSurfaceSideStyle(surfaceSideStyle, builder);
        }
        if (entity instanceof StepSurfaceStyleUsage surfaceStyleUsage) {
            return validateSurfaceSideStyle(surfaceStyleUsage.style(), builder);
        }
        if (entity instanceof StepTextStyleForDefinedFont textStyleForDefinedFont) {
            return validateSummaryEntity(textStyleForDefinedFont.textColour(), builder);
        }
        if (entity instanceof StepTextStyle textStyle) {
            return validateSummaryEntity(textStyle.characterAppearance(), builder);
        }
        if (entity instanceof StepTextStyleWithSpacing textStyleWithSpacing) {
            return validateSummaryEntity(textStyleWithSpacing.characterAppearance(), builder);
        }
        if (entity instanceof StepTextStyleWithJustification textStyleWithJustification) {
            return validateSummaryEntity(textStyleWithJustification.characterAppearance(), builder);
        }
        if (entity instanceof StepTextStyleWithMirror textStyleWithMirror) {
            return validateSummaryEntity(textStyleWithMirror.characterAppearance(), builder)
                    + validateSummaryEntity(textStyleWithMirror.mirrorPlacement(), builder);
        }
        if (entity instanceof StepTextStyleWithBoxCharacteristics textStyleWithBoxCharacteristics) {
            return validateSummaryEntity(textStyleWithBoxCharacteristics.characterAppearance(), builder);
        }
        if (entity instanceof StepSymbolColour symbolColour) {
            return validateSummaryEntity(symbolColour.colour(), builder);
        }
        if (entity instanceof StepCharacterGlyphStyleStroke glyphStyleStroke) {
            return validateCurveStyle(glyphStyleStroke.strokeStyle(), builder);
        }
        if (entity instanceof StepCharacterGlyphStyleOutline glyphStyleOutline) {
            return validateCurveStyle(glyphStyleOutline.outlineStyle(), builder);
        }
        if (entity instanceof StepCharacterGlyphStyleOutlineWithCharacteristics glyphStyleOutline) {
            return validateCurveStyle(glyphStyleOutline.outlineStyle(), builder)
                    + validateFillAreaStyle(glyphStyleOutline.characteristics(), builder);
        }
        if (entity instanceof StepUserDefinedCurveFont userDefinedCurveFont) {
            return validateRepresentationMap(userDefinedCurveFont.mappingSource(), builder)
                    + validateSummaryEntity(userDefinedCurveFont.mappingTarget(), builder);
        }
        if (entity instanceof StepUserDefinedMarker userDefinedMarker) {
            return validateRepresentationMap(userDefinedMarker.mappingSource(), builder)
                    + validateSummaryEntity(userDefinedMarker.mappingTarget(), builder);
        }
        if (entity instanceof StepUserDefinedTerminatorSymbol userDefinedTerminatorSymbol) {
            return validateRepresentationMap(userDefinedTerminatorSymbol.mappingSource(), builder)
                    + validateSummaryEntity(userDefinedTerminatorSymbol.mappingTarget(), builder);
        }
        if (entity instanceof StepGeometricRepresentationContext geometricRepresentationContext) {
            int count = 1;
            if (geometricRepresentationContext.globalUnitAssignedContext() != null) {
                count += validateSummaryEntity(geometricRepresentationContext.globalUnitAssignedContext(), builder);
            }
            if (geometricRepresentationContext.globalUncertaintyAssignedContext() != null) {
                count += validateSummaryEntity(geometricRepresentationContext.globalUncertaintyAssignedContext(), builder);
            }
            return count;
        }
        if (entity instanceof StepGlobalUnitAssignedContext globalUnitAssignedContext) {
            return validateSummaryItems(globalUnitAssignedContext.units(), builder);
        }
        if (entity instanceof StepGlobalUncertaintyAssignedContext globalUncertaintyAssignedContext) {
            int count = 0;
            for (StepUncertaintyMeasureWithUnit uncertainty : globalUncertaintyAssignedContext.uncertainties()) {
                count += validateSummaryEntity(uncertainty, builder);
            }
            return count;
        }
        if (entity instanceof StepMeasureWithUnit measureWithUnit) {
            return validateSummaryEntity(measureWithUnit.unitComponent(), builder);
        }
        if (entity instanceof StepTypedMeasureWithUnit typedMeasureWithUnit) {
            return validateSummaryEntity(typedMeasureWithUnit.unitComponent(), builder);
        }
        if (entity instanceof StepUncertaintyMeasureWithUnit uncertaintyMeasureWithUnit) {
            return validateSummaryEntity(uncertaintyMeasureWithUnit.unitComponent(), builder);
        }
        if (entity instanceof StepConversionBasedUnit conversionBasedUnit) {
            return validateSummaryEntity(conversionBasedUnit.conversionFactor(), builder);
        }
        if (entity instanceof StepConversionBasedUnitWithOffset conversionBasedUnitWithOffset) {
            return validateSummaryEntity(conversionBasedUnitWithOffset.conversionFactor(), builder);
        }
        if (entity instanceof StepDerivedUnit derivedUnit) {
            int count = 0;
            for (StepDerivedUnitElement element : derivedUnit.elements()) {
                count += validateSummaryEntity(element, builder);
            }
            return count;
        }
        if (entity instanceof StepDerivedUnitElement derivedUnitElement) {
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
        if (entity instanceof StepIdentificationAssignment identificationAssignment) {
            return validateSummaryEntity(identificationAssignment.role(), builder);
        }
        if (entity instanceof StepAppliedIdentificationAssignment appliedIdentificationAssignment) {
            return validateSummaryEntity(appliedIdentificationAssignment.role(), builder)
                    + validateSummaryItems(appliedIdentificationAssignment.items(), builder);
        }
        if (entity instanceof StepPersonAndOrganization personAndOrganization) {
            return validateSummaryEntity(personAndOrganization.person(), builder)
                    + validateSummaryEntity(personAndOrganization.organization(), builder);
        }
        if (entity instanceof StepPersonAndOrganizationAssignment personAndOrganizationAssignment) {
            return validateSummaryEntity(personAndOrganizationAssignment.assignedPersonAndOrganization(), builder)
                    + validateSummaryEntity(personAndOrganizationAssignment.role(), builder);
        }
        if (entity instanceof StepAppliedPersonAndOrganizationAssignment appliedPersonAndOrganizationAssignment) {
            return validateSummaryEntity(appliedPersonAndOrganizationAssignment.assignedPersonAndOrganization(), builder)
                    + validateSummaryEntity(appliedPersonAndOrganizationAssignment.role(), builder)
                    + validateSummaryItems(appliedPersonAndOrganizationAssignment.items(), builder);
        }
        if (entity instanceof StepLocalTime localTime) {
            return 1 + validateSummaryEntity(localTime.zone(), builder);
        }
        if (entity instanceof StepDateAndTime dateAndTime) {
            return validateSummaryEntity(dateAndTime.dateComponent(), builder)
                    + validateSummaryEntity(dateAndTime.timeComponent(), builder);
        }
        if (entity instanceof StepDateAssignment dateAssignment) {
            return validateSummaryEntity(dateAssignment.assignedDate(), builder)
                    + validateSummaryEntity(dateAssignment.role(), builder);
        }
        if (entity instanceof StepAppliedDateAssignment appliedDateAssignment) {
            return validateSummaryEntity(appliedDateAssignment.assignedDate(), builder)
                    + validateSummaryEntity(appliedDateAssignment.role(), builder)
                    + validateSummaryItems(appliedDateAssignment.items(), builder);
        }
        if (entity instanceof StepDateTimeAssignment dateTimeAssignment) {
            return validateSummaryEntity(dateTimeAssignment.assignedDateAndTime(), builder)
                    + validateSummaryEntity(dateTimeAssignment.role(), builder);
        }
        if (entity instanceof StepAppliedDateTimeAssignment appliedDateTimeAssignment) {
            return validateSummaryEntity(appliedDateTimeAssignment.assignedDateAndTime(), builder)
                    + validateSummaryEntity(appliedDateTimeAssignment.role(), builder)
                    + validateSummaryItems(appliedDateTimeAssignment.items(), builder);
        }
        if (entity instanceof StepDocumentReference documentReference) {
            return validateSummaryEntity(documentReference.assignedDocument(), builder);
        }
        if (entity instanceof StepAppliedDocumentReference appliedDocumentReference) {
            return validateSummaryEntity(appliedDocumentReference.assignedDocument(), builder)
                    + validateSummaryItems(appliedDocumentReference.items(), builder);
        }
        if (entity instanceof StepDocumentRelationship documentRelationship) {
            return validateSummaryEntity(documentRelationship.relatingDocument(), builder)
                    + validateSummaryEntity(documentRelationship.relatedDocument(), builder);
        }
        if (entity instanceof StepPropertyDefinitionRelationship propertyDefinitionRelationship) {
            return 2;
        }
        if (entity instanceof StepAbstractVariable abstractVariable) {
            return validateSummaryEntity(abstractVariable.definition(), builder)
                    + validateSummaryEntity(abstractVariable.usedRepresentation(), builder);
        }
        if (entity instanceof StepRowVariable rowVariable) {
            return validateSummaryEntity(rowVariable.definition(), builder)
                    + validateSummaryEntity(rowVariable.usedRepresentation(), builder);
        }
        if (entity instanceof StepScalarVariable scalarVariable) {
            return validateSummaryEntity(scalarVariable.definition(), builder)
                    + validateSummaryEntity(scalarVariable.usedRepresentation(), builder);
        }
        if (entity instanceof StepForwardChainingRulePremise rulePremise) {
            return validateSummaryEntity(rulePremise.definition(), builder)
                    + validateSummaryEntity(rulePremise.usedRepresentation(), builder);
        }
        if (entity instanceof StepBackChainingRuleBody ruleBody) {
            return validateSummaryEntity(ruleBody.definition(), builder)
                    + validateSummaryEntity(ruleBody.usedRepresentation(), builder);
        }
        if (entity instanceof StepAttributeAssertion attributeAssertion) {
            return validateSummaryEntity(attributeAssertion.usedRepresentation(), builder);
        }
        if (entity instanceof StepApprovalPersonOrganization approvalPersonOrganization) {
            return validateSummaryEntity(approvalPersonOrganization.personOrganization(), builder)
                    + validateSummaryEntity(approvalPersonOrganization.authorizedApproval(), builder)
                    + validateSummaryEntity(approvalPersonOrganization.role(), builder);
        }
        if (entity instanceof StepApprovalDateTime approvalDateTime) {
            return validateSummaryEntity(approvalDateTime.dateTime(), builder)
                    + validateSummaryEntity(approvalDateTime.datedApproval(), builder);
        }
        if (entity instanceof StepGroupRelationship groupRelationship) {
            return validateSummaryEntity(groupRelationship.relatingGroup(), builder)
                    + validateSummaryEntity(groupRelationship.relatedGroup(), builder);
        }
        if (entity instanceof StepOrganizationRelationship organizationRelationship) {
            return validateSummaryEntity(organizationRelationship.relatingOrganization(), builder)
                    + validateSummaryEntity(organizationRelationship.relatedOrganization(), builder);
        }
        if (entity instanceof StepApplicationContext) {
            return 1;
        }
        if (entity instanceof StepApplicationProtocolDefinition applicationProtocolDefinition) {
            return validateSummaryEntity(applicationProtocolDefinition.application(), builder);
        }
        if (entity instanceof StepProduct product) {
            return 1;
        }
        if (entity instanceof StepProductContext productContext) {
            return validateSummaryEntity(productContext.frameOfReference(), builder);
        }
        if (entity instanceof StepProductDefinitionContext productDefinitionContext) {
            return validateSummaryEntity(productDefinitionContext.frameOfReference(), builder);
        }
        if (entity instanceof StepProductDefinitionFormation formation) {
            return validateSummaryEntity(formation.ofProduct(), builder);
        }
        if (entity instanceof StepProductDefinition definition) {
            return validateSummaryEntity(definition.formation(), builder)
                    + validateSummaryEntity(definition.frameOfReference(), builder);
        }
        if (entity instanceof StepProductDefinitionShape productDefinitionShape) {
            return validateSummaryEntity(productDefinitionShape.definition(), builder);
        }
        if (entity instanceof StepProductDefinitionEffectivity productDefinitionEffectivity) {
            return validateSummaryEntity(productDefinitionEffectivity.productDefinition(), builder);
        }
        if (entity instanceof StepProductRelationship productRelationship) {
            return validateSummaryEntity(productRelationship.relatingProduct(), builder)
                    + validateSummaryEntity(productRelationship.relatedProduct(), builder);
        }
        if (entity instanceof StepProductDefinitionRelationship productDefinitionRelationship) {
            return validateSummaryEntity(productDefinitionRelationship.relatingProductDefinition(), builder)
                    + validateSummaryEntity(productDefinitionRelationship.relatedProductDefinition(), builder);
        }
        if (entity instanceof StepProductDefinitionFormationRelationship productDefinitionFormationRelationship) {
            return validateSummaryEntity(productDefinitionFormationRelationship.relatingFormation(), builder)
                    + validateSummaryEntity(productDefinitionFormationRelationship.relatedFormation(), builder);
        }
        if (entity instanceof StepProductDefinitionRelationshipRelationship relationshipRelationship) {
            return validateSummaryEntity(relationshipRelationship.relating(), builder)
                    + validateSummaryEntity(relationshipRelationship.related(), builder);
        }
        if (entity instanceof StepPropertyDefinition propertyDefinition) {
            return validateSummaryEntity(propertyDefinition.definition(), builder);
        }
        if (entity instanceof StepPropertyDefinitionRepresentation propertyDefinitionRepresentation) {
            return validateSummaryEntity(propertyDefinitionRepresentation.definition(), builder)
                    + validateSummaryEntity(propertyDefinitionRepresentation.usedRepresentation(), builder);
        }
        if (entity instanceof StepActionPropertyRepresentation actionPropertyRepresentation) {
            return validateSummaryEntity(actionPropertyRepresentation.definition(), builder)
                    + validateSummaryEntity(actionPropertyRepresentation.usedRepresentation(), builder);
        }
        if (entity instanceof StepContactRatioRepresentation contactRatioRepresentation) {
            return validateSummaryEntity(contactRatioRepresentation.definition(), builder)
                    + validateSummaryEntity(contactRatioRepresentation.usedRepresentation(), builder);
        }
        if (entity instanceof StepKinematicPropertyDefinitionRepresentation kinematicPropertyDefinitionRepresentation) {
            return validateSummaryEntity(kinematicPropertyDefinitionRepresentation.definition(), builder)
                    + validateSummaryEntity(kinematicPropertyDefinitionRepresentation.usedRepresentation(), builder);
        }
        if (entity instanceof StepKinematicPropertyMechanismRepresentation kinematicPropertyMechanismRepresentation) {
            return validateSummaryEntity(kinematicPropertyMechanismRepresentation.definition(), builder)
                    + validateSummaryEntity(kinematicPropertyMechanismRepresentation.usedRepresentation(), builder);
        }
        if (entity instanceof StepKinematicPropertyRepresentationRelation kinematicPropertyRepresentationRelation) {
            return validateSummaryEntity(kinematicPropertyRepresentationRelation.definition(), builder)
                    + validateSummaryEntity(kinematicPropertyRepresentationRelation.usedRepresentation(), builder);
        }
        if (entity instanceof StepKinematicPropertyTopologyRepresentation kinematicPropertyTopologyRepresentation) {
            return validateSummaryEntity(kinematicPropertyTopologyRepresentation.definition(), builder)
                    + validateSummaryEntity(kinematicPropertyTopologyRepresentation.usedRepresentation(), builder);
        }
        if (entity instanceof StepResourcePropertyRepresentation resourcePropertyRepresentation) {
            return validateSummaryEntity(resourcePropertyRepresentation.definition(), builder)
                    + validateSummaryEntity(resourcePropertyRepresentation.usedRepresentation(), builder);
        }
        if (entity instanceof StepShapeDefinitionRepresentation shapeDefinitionRepresentation) {
            return validateSummaryEntity(shapeDefinitionRepresentation.definition(), builder)
                    + validateSummaryEntity(shapeDefinitionRepresentation.usedRepresentation(), builder);
        }
        if (entity instanceof StepContextDependentShapeRepresentation contextDependentShapeRepresentation) {
            return validateSummaryEntity(contextDependentShapeRepresentation.representationRelationship(), builder)
                    + validateSummaryEntity(contextDependentShapeRepresentation.representedProductRelation(), builder);
        }
        if (entity instanceof StepNextAssemblyUsageOccurrence nextAssemblyUsageOccurrence) {
            return validateSummaryEntity(nextAssemblyUsageOccurrence.relatingProductDefinition(), builder)
                    + validateSummaryEntity(nextAssemblyUsageOccurrence.relatedProductDefinition(), builder);
        }
        if (entity instanceof StepPlacedDatumTargetFeature placedDatumTargetFeature) {
            return validateSummaryEntity(placedDatumTargetFeature.usedRepresentation(), builder);
        }
        if (entity instanceof StepShapeAspect shapeAspect) {
            return validateSummaryEntity(shapeAspect.ofShape(), builder);
        }
        if (entity instanceof StepShapeAspectOccurrence shapeAspectOccurrence) {
            return validateSummaryEntity(shapeAspectOccurrence.ofShape(), builder)
                    + validateSummaryEntity(shapeAspectOccurrence.definition(), builder);
        }
        if (entity instanceof StepShapeAspectRelationship shapeAspectRelationship) {
            return validateSummaryEntity(shapeAspectRelationship.relatingShapeAspect(), builder)
                    + validateSummaryEntity(shapeAspectRelationship.relatedShapeAspect(), builder);
        }
        if (entity instanceof StepItemIdentifiedRepresentationUsage itemIdentifiedRepresentationUsage) {
            return validateRepresentationUsage(itemIdentifiedRepresentationUsage.definition(),
                    itemIdentifiedRepresentationUsage.usedRepresentation(),
                    itemIdentifiedRepresentationUsage.identifiedItem(),
                    builder);
        }
        if (entity instanceof StepChainBasedItemIdentifiedRepresentationUsage chainBasedItemIdentifiedRepresentationUsage) {
            return validateChainBasedRepresentationUsage(chainBasedItemIdentifiedRepresentationUsage.definition(),
                    chainBasedItemIdentifiedRepresentationUsage.nodes(),
                    chainBasedItemIdentifiedRepresentationUsage.undirectedLinks(),
                    chainBasedItemIdentifiedRepresentationUsage.identifiedItem(),
                    builder);
        }
        if (entity instanceof StepPlacedTarget placedTarget) {
            return validateRepresentationUsage(placedTarget.definition(),
                    placedTarget.usedRepresentation(),
                    placedTarget.identifiedItem(),
                    builder);
        }
        if (entity instanceof StepDraughtingModelItemAssociation draughtingModelItemAssociation) {
            return validateRepresentationUsage(draughtingModelItemAssociation.definition(),
                    draughtingModelItemAssociation.usedRepresentation(),
                    draughtingModelItemAssociation.identifiedItem(),
                    builder);
        }
        if (entity instanceof StepDraughtingModelItemAssociationWithPlaceholder associationWithPlaceholder) {
            return validateRepresentationUsage(associationWithPlaceholder.definition(),
                    associationWithPlaceholder.usedRepresentation(),
                    associationWithPlaceholder.identifiedItem(),
                    builder) + validateSummaryEntity(associationWithPlaceholder.annotationPlaceholder(), builder);
        }
        if (entity instanceof StepPmiRequirementItemAssociation pmiRequirementItemAssociation) {
            return validateRepresentationUsage(pmiRequirementItemAssociation.definition(),
                    pmiRequirementItemAssociation.usedRepresentation(),
                    pmiRequirementItemAssociation.identifiedItem(),
                    builder) + validateSummaryEntity(pmiRequirementItemAssociation.requirement(), builder);
        }
        if (entity instanceof StepMechanicalDesignRequirementItemAssociation requirementItemAssociation) {
            return validateRepresentationUsage(requirementItemAssociation.definition(),
                    requirementItemAssociation.usedRepresentation(),
                    requirementItemAssociation.identifiedItem(),
                    builder) + validateSummaryEntity(requirementItemAssociation.requirement(), builder);
        }
        if (entity instanceof StepGeometricItemSpecificUsage geometricItemSpecificUsage) {
            return validateSummaryEntity(geometricItemSpecificUsage.usage(), builder)
                    + validateSummaryEntity(geometricItemSpecificUsage.identifiedItem(), builder);
        }
        if (entity instanceof StepChainBasedGeometricItemSpecificUsage chainBasedGeometricItemSpecificUsage) {
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
        if (entity instanceof StepGroupAssignment groupAssignment) {
            return validateSummaryEntity(groupAssignment.assignedGroup(), builder);
        }
        if (entity instanceof StepAppliedGroupAssignment appliedGroupAssignment) {
            return validateSummaryEntity(appliedGroupAssignment.assignedGroup(), builder)
                    + validateSummaryItems(appliedGroupAssignment.items(), builder);
        }
        if (entity instanceof StepClassificationAssignment classificationAssignment) {
            return validateSummaryEntity(classificationAssignment.assignedClass(), builder)
                    + validateSummaryEntity(classificationAssignment.role(), builder);
        }
        if (entity instanceof StepAppliedClassificationAssignment appliedClassificationAssignment) {
            return validateSummaryEntity(appliedClassificationAssignment.assignedClass(), builder)
                    + validateSummaryEntity(appliedClassificationAssignment.role(), builder)
                    + validateSummaryItems(appliedClassificationAssignment.items(), builder);
        }
        if (entity instanceof StepOrganizationAssignment organizationAssignment) {
            return validateSummaryEntity(organizationAssignment.assignedOrganization(), builder)
                    + validateSummaryEntity(organizationAssignment.role(), builder);
        }
        if (entity instanceof StepAppliedOrganizationAssignment appliedOrganizationAssignment) {
            return validateSummaryEntity(appliedOrganizationAssignment.assignedOrganization(), builder)
                    + validateSummaryEntity(appliedOrganizationAssignment.role(), builder)
                    + validateSummaryItems(appliedOrganizationAssignment.items(), builder);
        }
        if (entity instanceof StepAppliedNameAssignment appliedNameAssignment) {
            return validateSummaryItems(appliedNameAssignment.items(), builder);
        }
        if (entity instanceof StepApproval approval) {
            return 1 + validateSummaryEntity(approval.status(), builder);
        }
        if (entity instanceof StepApprovalAssignment approvalAssignment) {
            return validateSummaryEntity(approvalAssignment.assignedApproval(), builder);
        }
        if (entity instanceof StepAppliedApprovalAssignment appliedApprovalAssignment) {
            return validateSummaryEntity(appliedApprovalAssignment.assignedApproval(), builder)
                    + validateSummaryItems(appliedApprovalAssignment.items(), builder);
        }
        if (entity instanceof StepContract contract) {
            return 1 + validateSummaryEntity(contract.kind(), builder);
        }
        if (entity instanceof StepContractAssignment contractAssignment) {
            return validateSummaryEntity(contractAssignment.assignedContract(), builder);
        }
        if (entity instanceof StepAppliedContractAssignment appliedContractAssignment) {
            return validateSummaryEntity(appliedContractAssignment.assignedContract(), builder)
                    + validateSummaryItems(appliedContractAssignment.items(), builder);
        }
        if (entity instanceof StepCertification certification) {
            return 1 + validateSummaryEntity(certification.kind(), builder);
        }
        if (entity instanceof StepCertificationAssignment certificationAssignment) {
            return validateSummaryEntity(certificationAssignment.assignedCertification(), builder);
        }
        if (entity instanceof StepAppliedCertificationAssignment appliedCertificationAssignment) {
            return validateSummaryEntity(appliedCertificationAssignment.assignedCertification(), builder)
                    + validateSummaryItems(appliedCertificationAssignment.items(), builder);
        }
        if (entity instanceof StepSecurityClassification securityClassification) {
            return 1 + validateSummaryEntity(securityClassification.securityLevel(), builder);
        }
        if (entity instanceof StepSecurityClassificationAssignment securityClassificationAssignment) {
            return validateSummaryEntity(securityClassificationAssignment.assignedSecurityClassification(), builder);
        }
        if (entity instanceof StepAppliedSecurityClassificationAssignment appliedSecurityClassificationAssignment) {
            return validateSummaryEntity(appliedSecurityClassificationAssignment.assignedSecurityClassification(), builder)
                    + validateSummaryItems(appliedSecurityClassificationAssignment.items(), builder);
        }
        if (entity instanceof StepExternalSourceRelationship externalSourceRelationship) {
            return validateSummaryEntity(externalSourceRelationship.relatingSource(), builder)
                    + validateSummaryEntity(externalSourceRelationship.relatedSource(), builder);
        }
        if (entity instanceof StepGeneralPropertyRelationship generalPropertyRelationship) {
            return validateSummaryEntity(generalPropertyRelationship.relatingGeneralProperty(), builder)
                    + validateSummaryEntity(generalPropertyRelationship.relatedGeneralProperty(), builder);
        }
        if (entity instanceof StepProductCategoryRelationship productCategoryRelationship) {
            return validateSummaryEntity(productCategoryRelationship.category(), builder)
                    + validateSummaryEntity(productCategoryRelationship.subCategory(), builder);
        }
        if (entity instanceof StepProductRelatedProductCategory productRelatedCategory) {
            return validateSummaryItems(List.copyOf(productRelatedCategory.products()), builder);
        }
        if (entity instanceof StepDocument document) {
            return 1 + validateSummaryEntity(document.kind(), builder);
        }
        if (entity instanceof StepDocumentUsageConstraint documentUsageConstraint) {
            return validateSummaryEntity(documentUsageConstraint.source(), builder);
        }
        if (entity instanceof StepEffectivityRelationship effectivityRelationship) {
            return validateSummaryEntity(effectivityRelationship.relatingEffectivity(), builder)
                    + validateSummaryEntity(effectivityRelationship.relatedEffectivity(), builder);
        }
        if (entity instanceof StepLanguageAssignment languageAssignment) {
            return validateSummaryEntity(languageAssignment.assignedLanguage(), builder);
        }
        if (entity instanceof StepAppliedLanguageAssignment appliedLanguageAssignment) {
            int count = validateSummaryEntity(appliedLanguageAssignment.assignedLanguage(), builder);
            for (StepEntity item : appliedLanguageAssignment.items()) {
                count += validateSummaryEntity(item, builder);
            }
            return count;
        }
        if (entity instanceof StepExternalIdentificationAssignment externalIdentificationAssignment) {
            return validateSummaryEntity(externalIdentificationAssignment.role(), builder)
                    + validateSummaryEntity(externalIdentificationAssignment.source(), builder);
        }
        if (entity instanceof StepAppliedExternalIdentificationAssignment appliedExternalIdentificationAssignment) {
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
        if (!polyline.points().isEmpty() && polyline.points().getFirst().coordinates().size() == 2) {
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
        if (entity instanceof StepOpenShell openShell) {
            return openShell.faces();
        }
        if (entity instanceof StepSurfacedOpenShell surfacedOpenShell) {
            return surfacedOpenShell.faces();
        }
        if (entity instanceof StepOrientedOpenShell orientedOpenShell) {
            return orientedOpenShell.faces();
        }
        if (entity instanceof StepClosedShell closedShell) {
            return closedShell.faces();
        }
        if (entity instanceof StepOrientedClosedShell orientedClosedShell) {
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
