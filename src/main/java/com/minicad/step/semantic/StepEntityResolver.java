package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.StepAdvancedFace;
import com.minicad.step.model.StepAnnotationTextOccurrence;
import com.minicad.step.model.StepApplicationContext;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepBSplineCurveWithKnots;
import com.minicad.step.model.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.StepCircle;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepColourRgb;
import com.minicad.step.model.StepConicalSurface;
import com.minicad.step.model.StepCylindricalSurface;
import com.minicad.step.model.StepDirection;
import com.minicad.step.model.StepDraughtingCallout;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEdgeLoop;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepFaceBound;
import com.minicad.step.model.StepFaceSurface;
import com.minicad.step.model.StepFillAreaStyle;
import com.minicad.step.model.StepFillAreaStyleColour;
import com.minicad.step.model.StepGeometricCurveSet;
import com.minicad.step.model.StepGeometricItemSpecificUsage;
import com.minicad.step.model.StepGeometricRepresentationContext;
import com.minicad.step.model.StepGlobalUncertaintyAssignedContext;
import com.minicad.step.model.StepGlobalUnitAssignedContext;
import com.minicad.step.model.StepItemDefinedTransformation;
import com.minicad.step.model.StepLine;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepMeasureWithUnit;
import com.minicad.step.model.StepMeasureRepresentationItem;
import com.minicad.step.model.StepNamedUnit;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.model.StepOrientedEdge;
import com.minicad.step.model.StepOrientedFace;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepPcurve;
import com.minicad.step.model.StepEllipse;
import com.minicad.step.model.StepProduct;
import com.minicad.step.model.StepProductContext;
import com.minicad.step.model.StepProductDefinition;
import com.minicad.step.model.StepProductDefinitionContext;
import com.minicad.step.model.StepProductDefinitionFormation;
import com.minicad.step.model.StepProductDefinitionShape;
import com.minicad.step.model.StepPresentationLayerAssignment;
import com.minicad.step.model.StepPresentationStyleAssignment;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.model.StepRepresentationContext;
import com.minicad.step.model.StepRepresentationRelationshipWithTransformation;
import com.minicad.step.model.StepShapeRepresentationRelationship;
import com.minicad.step.model.StepShapeDefinitionRepresentation;
import com.minicad.step.model.StepSeamCurve;
import com.minicad.step.model.StepSiUnit;
import com.minicad.step.model.StepSurfaceCurve;
import com.minicad.step.model.StepSurfaceSideStyle;
import com.minicad.step.model.StepSurfaceStyleFillArea;
import com.minicad.step.model.StepSurfaceStyleUsage;
import com.minicad.step.model.StepStyledItem;
import com.minicad.step.model.StepTrimmedCurve;
import com.minicad.step.model.StepToroidalSurface;
import com.minicad.step.model.StepUncertaintyMeasureWithUnit;
import com.minicad.step.model.StepNextAssemblyUsageOccurrence;
import com.minicad.step.model.StepContextDependentShapeRepresentation;
import com.minicad.step.model.StepLoop;
import com.minicad.step.model.StepVector;
import com.minicad.step.model.StepVertexLoop;
import com.minicad.step.model.StepVertexPoint;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepValue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolves raw STEP AST entities into a minimal semantic model.
 */
public final class StepEntityResolver {

    private static final Map<String, EntityFactory> REGISTRY = createRegistry();

    private final Map<Integer, StepEntityInstance> instancesById;
    private final Map<Integer, StepEntity> resolved = new LinkedHashMap<>();

    private StepEntityResolver(StepFile file) {
        this.instancesById = file.entitiesById();
    }

    /**
     * Resolves all supported entities in the file.
     *
     * @param file parsed STEP file
     * @return resolved entities indexed by id
     */
    public static Map<Integer, StepEntity> resolveAll(StepFile file) {
        return new StepEntityResolver(file).resolveAll();
    }

    private Map<Integer, StepEntity> resolveAll() {
        for (Integer id : instancesById.keySet()) {
            resolve(id);
        }
        return Map.copyOf(resolved);
    }

    private StepEntity resolve(int id) {
        StepEntity existing = resolved.get(id);
        if (existing != null) {
            return existing;
        }

        StepEntityInstance instance = instancesById.get(id);
        if (instance == null) {
            throw new StepResolutionException("missing referenced entity #" + id);
        }

        for (Map.Entry<String, EntityFactory> entry : REGISTRY.entrySet()) {
            if (!instance.hasDefinition(entry.getKey())) {
                continue;
            }
            StepEntity entity = entry.getValue().create(this, instance);
            resolved.put(id, entity);
            return entity;
        }

        throw new UnsupportedStepEntityException("unsupported STEP entity " + instance.name());
    }

    private StepCartesianPoint resolveCartesianPoint(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "CARTESIAN_POINT");
        requireParameterCount(instance, definition, 2);
        return new StepCartesianPoint(instance.id(), stringValue(instance, definition, 0), coordinateList(instance, definition, 1, 2, 3));
    }

    private StepDirection resolveDirection(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "DIRECTION");
        requireParameterCount(instance, definition, 2);
        return new StepDirection(instance.id(), stringValue(instance, definition, 0), coordinateList(instance, definition, 1, 2, 3));
    }

    private StepVector resolveVector(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "VECTOR");
        requireParameterCount(instance, definition, 3);
        return new StepVector(
                instance.id(),
                stringValue(instance, definition, 0),
                requireEntity(referenceId(instance, definition, 1), StepDirection.class, "VECTOR orientation must reference DIRECTION"),
                numberValue(instance, definition, 2)
        );
    }

    private StepAxis2Placement3D resolveAxis2Placement3D(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "AXIS2_PLACEMENT_3D");
        requireParameterCount(instance, definition, 4);
        if (isUnset(definition.parameters().get(2)) || isUnset(definition.parameters().get(3))) {
            throw new UnsupportedStepEntityException("AXIS2_PLACEMENT_3D requires explicit axis and ref direction");
        }
        return new StepAxis2Placement3D(
                instance.id(),
                stringValue(instance, definition, 0),
                requireEntity(referenceId(instance, definition, 1), StepCartesianPoint.class,
                        "AXIS2_PLACEMENT_3D location must reference CARTESIAN_POINT"),
                requireEntity(referenceId(instance, definition, 2), StepDirection.class,
                        "AXIS2_PLACEMENT_3D axis must reference DIRECTION"),
                requireEntity(referenceId(instance, definition, 3), StepDirection.class,
                        "AXIS2_PLACEMENT_3D ref direction must reference DIRECTION")
        );
    }

    private StepLine resolveLine(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "LINE");
        requireParameterCount(instance, definition, 3);
        return new StepLine(
                instance.id(),
                stringValue(instance, definition, 0),
                requireEntity(referenceId(instance, definition, 1), StepCartesianPoint.class,
                        "LINE point must reference CARTESIAN_POINT"),
                requireEntity(referenceId(instance, definition, 2), StepVector.class,
                        "LINE vector must reference VECTOR")
        );
    }

    private StepPlane resolvePlane(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "PLANE");
        requireParameterCount(instance, definition, 2);
        return new StepPlane(
                instance.id(),
                stringValue(instance, definition, 0),
                requireEntity(referenceId(instance, definition, 1), StepAxis2Placement3D.class,
                        "PLANE position must reference AXIS2_PLACEMENT_3D")
        );
    }

    private StepCircle resolveCircle(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "CIRCLE");
        requireParameterCount(instance, definition, 3);
        return new StepCircle(
                instance.id(),
                stringValue(instance, definition, 0),
                requireEntity(referenceId(instance, definition, 1), StepAxis2Placement3D.class,
                        "CIRCLE position must reference AXIS2_PLACEMENT_3D"),
                numberValue(instance, definition, 2)
        );
    }

    private StepEllipse resolveEllipse(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "ELLIPSE");
        requireParameterCount(instance, definition, 4);
        return new StepEllipse(
                instance.id(),
                stringValue(instance, definition, 0),
                requireEntity(referenceId(instance, definition, 1), StepAxis2Placement3D.class,
                        "ELLIPSE position must reference AXIS2_PLACEMENT_3D"),
                numberValue(instance, definition, 2),
                numberValue(instance, definition, 3)
        );
    }

    private StepCylindricalSurface resolveCylindricalSurface(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "CYLINDRICAL_SURFACE");
        requireParameterCount(instance, definition, 3);
        return new StepCylindricalSurface(
                instance.id(),
                stringValue(instance, definition, 0),
                requireEntity(referenceId(instance, definition, 1), StepAxis2Placement3D.class,
                        "CYLINDRICAL_SURFACE position must reference AXIS2_PLACEMENT_3D"),
                numberValue(instance, definition, 2)
        );
    }

    private StepConicalSurface resolveConicalSurface(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "CONICAL_SURFACE");
        requireParameterCount(instance, definition, 4);
        return new StepConicalSurface(
                instance.id(),
                stringValue(instance, definition, 0),
                requireEntity(referenceId(instance, definition, 1), StepAxis2Placement3D.class,
                        "CONICAL_SURFACE position must reference AXIS2_PLACEMENT_3D"),
                numberValue(instance, definition, 2),
                numberValue(instance, definition, 3)
        );
    }

    private StepToroidalSurface resolveToroidalSurface(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "TOROIDAL_SURFACE");
        requireParameterCount(instance, definition, 4);
        return new StepToroidalSurface(
                instance.id(),
                stringValue(instance, definition, 0),
                requireEntity(referenceId(instance, definition, 1), StepAxis2Placement3D.class,
                        "TOROIDAL_SURFACE position must reference AXIS2_PLACEMENT_3D"),
                numberValue(instance, definition, 2),
                numberValue(instance, definition, 3)
        );
    }

    private StepTrimmedCurve resolveTrimmedCurve(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "TRIMMED_CURVE");
        requireParameterCount(instance, definition, 6);
        StepEntity basisCurve = resolve(referenceId(instance, definition, 1));
        if (!(basisCurve instanceof StepLine)
                && !(basisCurve instanceof StepCircle)
                && !(basisCurve instanceof StepEllipse)
                && !(basisCurve instanceof StepSurfaceCurve)
                && !(basisCurve instanceof StepBSplineCurveWithKnots)) {
            throw new UnsupportedStepEntityException("TRIMMED_CURVE basis curve must be LINE, CIRCLE, ELLIPSE, SURFACE_CURVE or B_SPLINE_CURVE_WITH_KNOTS");
        }
        return new StepTrimmedCurve(
                instance.id(),
                stringValue(instance, definition, 0),
                basisCurve,
                entityReferenceList(instance, definition, 2, "TRIMMED_CURVE trim_1 must contain entity references"),
                entityReferenceList(instance, definition, 3, "TRIMMED_CURVE trim_2 must contain entity references"),
                booleanValue(instance, definition, 4),
                enumValue(instance, definition, 5)
        );
    }

    private StepSurfaceCurve resolveSurfaceCurve(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "SURFACE_CURVE");
        requireParameterCount(instance, definition, 4);
        StepEntity curve3d = resolve(referenceId(instance, definition, 1));
        if (!(curve3d instanceof StepLine)
                && !(curve3d instanceof StepCircle)
                && !(curve3d instanceof StepEllipse)
                && !(curve3d instanceof StepBSplineCurveWithKnots)) {
            throw new UnsupportedStepEntityException("SURFACE_CURVE curve_3d must be LINE, CIRCLE, ELLIPSE or B_SPLINE_CURVE_WITH_KNOTS");
        }
        List<StepEntity> associatedGeometry = entityReferenceList(instance, definition, 2,
                "SURFACE_CURVE associated_geometry must contain entity references");
        for (StepEntity associated : associatedGeometry) {
            if (!(associated instanceof StepPcurve)) {
                throw new UnsupportedStepEntityException("SURFACE_CURVE associated_geometry currently supports PCURVE references");
            }
        }
        return new StepSurfaceCurve(
                instance.id(),
                stringValue(instance, definition, 0),
                curve3d,
                associatedGeometry,
                enumValue(instance, definition, 3)
        );
    }

    private StepSeamCurve resolveSeamCurve(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "SEAM_CURVE");
        requireParameterCount(instance, definition, 4);
        StepEntity curve3d = resolve(referenceId(instance, definition, 1));
        if (!(curve3d instanceof StepLine)
                && !(curve3d instanceof StepCircle)
                && !(curve3d instanceof StepEllipse)
                && !(curve3d instanceof StepBSplineCurveWithKnots)) {
            throw new UnsupportedStepEntityException("SEAM_CURVE curve_3d must be LINE, CIRCLE, ELLIPSE or B_SPLINE_CURVE_WITH_KNOTS");
        }
        List<StepEntity> associatedGeometry = entityReferenceList(instance, definition, 2,
                "SEAM_CURVE associated_geometry must contain entity references");
        if (associatedGeometry.size() != 2) {
            throw new UnsupportedStepEntityException("SEAM_CURVE associated_geometry must contain exactly two PCURVE references");
        }
        for (StepEntity associated : associatedGeometry) {
            if (!(associated instanceof StepPcurve)) {
                throw new UnsupportedStepEntityException("SEAM_CURVE associated_geometry currently supports PCURVE references");
            }
        }
        return new StepSeamCurve(
                instance.id(),
                stringValue(instance, definition, 0),
                curve3d,
                associatedGeometry,
                enumValue(instance, definition, 3)
        );
    }

    private StepPcurve resolvePcurve(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "PCURVE");
        requireParameterCount(instance, definition, 3);
        StepEntity basisSurface = resolve(referenceId(instance, definition, 1));
        if (!(basisSurface instanceof StepPlane)
                && !(basisSurface instanceof StepCylindricalSurface)
                && !(basisSurface instanceof StepConicalSurface)
                && !(basisSurface instanceof StepToroidalSurface)
                && !(basisSurface instanceof StepBSplineSurfaceWithKnots)) {
            throw new UnsupportedStepEntityException("PCURVE basis surface must reference a supported surface");
        }
        StepRepresentation representation = requireEntity(referenceId(instance, definition, 2), StepRepresentation.class,
                "PCURVE reference_to_curve must reference REPRESENTATION");
        if (representation.items().size() != 1) {
            throw new UnsupportedStepEntityException("PCURVE reference_to_curve must contain exactly one 2D curve item");
        }
        StepEntity item = representation.items().getFirst();
        if (!(item instanceof StepLine) && !(item instanceof StepBSplineCurveWithKnots)) {
            throw new UnsupportedStepEntityException("PCURVE currently supports 2D LINE or B_SPLINE_CURVE_WITH_KNOTS items");
        }
        return new StepPcurve(instance.id(), stringValue(instance, definition, 0), basisSurface, representation);
    }

    private StepBSplineCurveWithKnots resolveBSplineCurveWithKnots(StepEntityInstance instance) {
        StepEntityDefinition spline = definition(instance, "B_SPLINE_CURVE_WITH_KNOTS");
        requireParameterCount(instance, spline, 3);
        if (!instance.hasDefinition("B_SPLINE_CURVE")) {
            throw new StepResolutionException("B_SPLINE_CURVE_WITH_KNOTS requires B_SPLINE_CURVE definition in same entity");
        }
        StepEntityDefinition base = definition(instance, "B_SPLINE_CURVE");
        requireParameterCount(instance, base, 6);
        return new StepBSplineCurveWithKnots(
                instance.id(),
                stringValue(instance, base, 0),
                integerValue(instance, base, 1),
                referenceList(instance, base, 2, StepCartesianPoint.class,
                        "B_SPLINE_CURVE control points must reference CARTESIAN_POINT"),
                enumValue(instance, base, 3),
                booleanValue(instance, base, 4),
                booleanValue(instance, base, 5),
                integerList(instance, spline, 0),
                numberList(instance, spline, 1),
                enumValue(instance, spline, 2)
        );
    }

    private StepBSplineSurfaceWithKnots resolveBSplineSurfaceWithKnots(StepEntityInstance instance) {
        StepEntityDefinition knots = definition(instance, "B_SPLINE_SURFACE_WITH_KNOTS");
        requireParameterCount(instance, knots, 5);
        if (!instance.hasDefinition("B_SPLINE_SURFACE")) {
            throw new StepResolutionException("B_SPLINE_SURFACE_WITH_KNOTS requires B_SPLINE_SURFACE definition in same entity");
        }
        StepEntityDefinition base = definition(instance, "B_SPLINE_SURFACE");
        requireParameterCount(instance, base, 7);
        return new StepBSplineSurfaceWithKnots(
                instance.id(),
                "",
                integerValue(instance, base, 0),
                integerValue(instance, base, 1),
                referenceGrid(instance, base, 2, StepCartesianPoint.class,
                        "B_SPLINE_SURFACE control points must reference CARTESIAN_POINT"),
                enumValue(instance, base, 3),
                booleanValue(instance, base, 4),
                booleanValue(instance, base, 5),
                booleanValue(instance, base, 6),
                integerList(instance, knots, 0),
                integerList(instance, knots, 1),
                numberList(instance, knots, 2),
                numberList(instance, knots, 3),
                enumValue(instance, knots, 4)
        );
    }

    private StepVertexPoint resolveVertexPoint(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "VERTEX_POINT");
        requireParameterCount(instance, definition, 2);
        return new StepVertexPoint(
                instance.id(),
                stringValue(instance, definition, 0),
                requireEntity(referenceId(instance, definition, 1), StepCartesianPoint.class,
                        "VERTEX_POINT geometry must reference CARTESIAN_POINT")
        );
    }

    private StepEdgeCurve resolveEdgeCurve(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "EDGE_CURVE");
        requireParameterCount(instance, definition, 5);
        StepEntity edgeGeometry = resolve(referenceId(instance, definition, 3));
        if (!(edgeGeometry instanceof StepLine)
                && !(edgeGeometry instanceof StepCircle)
                && !(edgeGeometry instanceof StepEllipse)
                && !(edgeGeometry instanceof StepBSplineCurveWithKnots)
                && !(edgeGeometry instanceof StepSurfaceCurve)
                && !(edgeGeometry instanceof StepSeamCurve)
                && !(edgeGeometry instanceof StepTrimmedCurve)) {
            throw new UnsupportedStepEntityException("EDGE_CURVE geometry must be LINE, CIRCLE, ELLIPSE, B_SPLINE_CURVE_WITH_KNOTS, SURFACE_CURVE, SEAM_CURVE or TRIMMED_CURVE");
        }
        return new StepEdgeCurve(
                instance.id(),
                stringValue(instance, definition, 0),
                requireEntity(referenceId(instance, definition, 1), StepVertexPoint.class,
                        "EDGE_CURVE start must reference VERTEX_POINT"),
                requireEntity(referenceId(instance, definition, 2), StepVertexPoint.class,
                        "EDGE_CURVE end must reference VERTEX_POINT"),
                edgeGeometry,
                booleanValue(instance, definition, 4)
        );
    }

    private StepOrientedEdge resolveOrientedEdge(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "ORIENTED_EDGE");
        requireParameterCount(instance, definition, 5);
        if (!isUnset(definition.parameters().get(1)) || !isUnset(definition.parameters().get(2))) {
            throw new UnsupportedStepEntityException("ORIENTED_EDGE explicit edge_start/edge_end is unsupported");
        }
        return new StepOrientedEdge(
                instance.id(),
                stringValue(instance, definition, 0),
                requireEntity(referenceId(instance, definition, 3), StepEdgeCurve.class,
                        "ORIENTED_EDGE edge_element must reference EDGE_CURVE"),
                booleanValue(instance, definition, 4)
        );
    }

    private StepEdgeLoop resolveEdgeLoop(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "EDGE_LOOP");
        requireParameterCount(instance, definition, 2);
        List<StepOrientedEdge> edges = referenceList(instance, definition, 1, StepOrientedEdge.class,
                "EDGE_LOOP edge list must contain ORIENTED_EDGE references");
        return new StepEdgeLoop(instance.id(), stringValue(instance, definition, 0), edges);
    }

    private StepVertexLoop resolveVertexLoop(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "VERTEX_LOOP");
        requireParameterCount(instance, definition, 2);
        return new StepVertexLoop(
                instance.id(),
                stringValue(instance, definition, 0),
                requireEntity(referenceId(instance, definition, 1), StepVertexPoint.class,
                        "VERTEX_LOOP loop_vertex must reference VERTEX_POINT")
        );
    }

    private StepFaceBound resolveFaceBound(StepEntityInstance instance, boolean outer) {
        StepEntityDefinition definition = definition(instance, outer ? "FACE_OUTER_BOUND" : "FACE_BOUND");
        requireParameterCount(instance, definition, 3);
        return new StepFaceBound(
                instance.id(),
                stringValue(instance, definition, 0),
                requireEntity(referenceId(instance, definition, 1), StepLoop.class,
                        "FACE_BOUND loop must reference LOOP subtype"),
                booleanValue(instance, definition, 2),
                outer
        );
    }

    private StepAdvancedFace resolveAdvancedFace(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "ADVANCED_FACE");
        requireParameterCount(instance, definition, 4);
        StepEntity faceGeometry = resolve(referenceId(instance, definition, 2));
        if (!(faceGeometry instanceof StepPlane)
                && !(faceGeometry instanceof StepCylindricalSurface)
                && !(faceGeometry instanceof StepConicalSurface)
                && !(faceGeometry instanceof StepBSplineSurfaceWithKnots)
                && !(faceGeometry instanceof StepToroidalSurface)) {
            throw new UnsupportedStepEntityException("ADVANCED_FACE geometry must be PLANE, CYLINDRICAL_SURFACE, CONICAL_SURFACE, B_SPLINE_SURFACE_WITH_KNOTS or TOROIDAL_SURFACE");
        }
        return new StepAdvancedFace(
                instance.id(),
                stringValue(instance, definition, 0),
                referenceList(instance, definition, 1, StepFaceBound.class,
                        "ADVANCED_FACE bounds must contain FACE_BOUND references"),
                faceGeometry,
                booleanValue(instance, definition, 3)
        );
    }

    private StepFaceSurface resolveFaceSurface(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "FACE_SURFACE");
        requireParameterCount(instance, definition, 4);
        StepEntity faceGeometry = resolve(referenceId(instance, definition, 2));
        if (!(faceGeometry instanceof StepPlane)
                && !(faceGeometry instanceof StepCylindricalSurface)
                && !(faceGeometry instanceof StepConicalSurface)
                && !(faceGeometry instanceof StepBSplineSurfaceWithKnots)
                && !(faceGeometry instanceof StepToroidalSurface)) {
            throw new UnsupportedStepEntityException("FACE_SURFACE geometry must be PLANE, CYLINDRICAL_SURFACE, CONICAL_SURFACE, B_SPLINE_SURFACE_WITH_KNOTS or TOROIDAL_SURFACE");
        }
        return new StepFaceSurface(
                instance.id(),
                stringValue(instance, definition, 0),
                referenceList(instance, definition, 1, StepFaceBound.class,
                        "FACE_SURFACE bounds must contain FACE_BOUND references"),
                faceGeometry,
                booleanValue(instance, definition, 3)
        );
    }

    private StepOrientedFace resolveOrientedFace(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "ORIENTED_FACE");
        requireParameterCount(instance, definition, 3);
        StepFaceEntity faceElement = requireEntity(referenceId(instance, definition, 1), StepFaceEntity.class,
                "ORIENTED_FACE face_element must reference FACE subtype");
        if (faceElement instanceof StepOrientedFace) {
            throw new UnsupportedStepEntityException("ORIENTED_FACE nesting is unsupported");
        }
        return new StepOrientedFace(
                instance.id(),
                stringValue(instance, definition, 0),
                faceElement,
                booleanValue(instance, definition, 2)
        );
    }

    private StepOpenShell resolveOpenShell(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "OPEN_SHELL");
        requireParameterCount(instance, definition, 2);
        return new StepOpenShell(
                instance.id(),
                stringValue(instance, definition, 0),
                referenceList(instance, definition, 1, StepFaceEntity.class,
                        "OPEN_SHELL faces must contain FACE subtype references")
        );
    }

    private StepClosedShell resolveClosedShell(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "CLOSED_SHELL");
        requireParameterCount(instance, definition, 2);
        return new StepClosedShell(
                instance.id(),
                stringValue(instance, definition, 0),
                referenceList(instance, definition, 1, StepFaceEntity.class,
                        "CLOSED_SHELL faces must contain FACE subtype references")
        );
    }

    private StepManifoldSolidBrep resolveManifoldSolidBrep(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "MANIFOLD_SOLID_BREP");
        requireParameterCount(instance, definition, 2);
        return new StepManifoldSolidBrep(
                instance.id(),
                stringValue(instance, definition, 0),
                requireEntity(referenceId(instance, definition, 1), StepClosedShell.class,
                        "MANIFOLD_SOLID_BREP outer must reference CLOSED_SHELL")
        );
    }

    private StepRepresentationContext resolveRepresentationContext(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "REPRESENTATION_CONTEXT");
        requireParameterCount(instance, definition, 2);
        return new StepRepresentationContext(
                instance.id(),
                stringValue(instance, definition, 0),
                stringValue(instance, definition, 1)
        );
    }

    private StepApplicationContext resolveApplicationContext(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "APPLICATION_CONTEXT");
        requireParameterCount(instance, definition, 1);
        return new StepApplicationContext(instance.id(), stringValue(instance, definition, 0));
    }

    private StepProductContext resolveProductContext(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "PRODUCT_CONTEXT");
        requireParameterCount(instance, definition, 3);
        return new StepProductContext(
                instance.id(),
                stringValue(instance, definition, 0),
                stringValue(instance, definition, 1),
                requireEntity(referenceId(instance, definition, 2), StepApplicationContext.class,
                        "PRODUCT_CONTEXT frame_of_reference must reference APPLICATION_CONTEXT")
        );
    }

    private StepProduct resolveProduct(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "PRODUCT");
        requireParameterCount(instance, definition, 4);
        return new StepProduct(
                instance.id(),
                stringValue(instance, definition, 0),
                stringValue(instance, definition, 1),
                stringValue(instance, definition, 2),
                referenceList(instance, definition, 3, StepProductContext.class,
                        "PRODUCT frame_of_reference must contain PRODUCT_CONTEXT references")
        );
    }

    private StepProductDefinitionFormation resolveProductDefinitionFormation(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "PRODUCT_DEFINITION_FORMATION");
        requireParameterCount(instance, definition, 3);
        return new StepProductDefinitionFormation(
                instance.id(),
                stringValue(instance, definition, 0),
                stringValue(instance, definition, 1),
                requireEntity(referenceId(instance, definition, 2), StepProduct.class,
                        "PRODUCT_DEFINITION_FORMATION of_product must reference PRODUCT")
        );
    }

    private StepProductDefinitionContext resolveProductDefinitionContext(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "PRODUCT_DEFINITION_CONTEXT");
        requireParameterCount(instance, definition, 3);
        return new StepProductDefinitionContext(
                instance.id(),
                stringValue(instance, definition, 0),
                stringValue(instance, definition, 1),
                requireEntity(referenceId(instance, definition, 2), StepApplicationContext.class,
                        "PRODUCT_DEFINITION_CONTEXT frame_of_reference must reference APPLICATION_CONTEXT")
        );
    }

    private StepProductDefinition resolveProductDefinition(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "PRODUCT_DEFINITION");
        requireParameterCount(instance, definition, 4);
        return new StepProductDefinition(
                instance.id(),
                stringValue(instance, definition, 0),
                stringValue(instance, definition, 1),
                requireEntity(referenceId(instance, definition, 2), StepProductDefinitionFormation.class,
                        "PRODUCT_DEFINITION formation must reference PRODUCT_DEFINITION_FORMATION"),
                requireEntity(referenceId(instance, definition, 3), StepProductDefinitionContext.class,
                        "PRODUCT_DEFINITION frame_of_reference must reference PRODUCT_DEFINITION_CONTEXT")
        );
    }

    private StepProductDefinitionShape resolveProductDefinitionShape(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "PRODUCT_DEFINITION_SHAPE");
        requireParameterCount(instance, definition, 3);
        return new StepProductDefinitionShape(
                instance.id(),
                stringValue(instance, definition, 0),
                stringValue(instance, definition, 1),
                requireEntity(referenceId(instance, definition, 2), StepProductDefinition.class,
                        "PRODUCT_DEFINITION_SHAPE definition must reference PRODUCT_DEFINITION")
        );
    }

    private StepShapeDefinitionRepresentation resolveShapeDefinitionRepresentation(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "SHAPE_DEFINITION_REPRESENTATION");
        requireParameterCount(instance, definition, 2);
        return new StepShapeDefinitionRepresentation(
                instance.id(),
                requireEntity(referenceId(instance, definition, 0), StepProductDefinitionShape.class,
                        "SHAPE_DEFINITION_REPRESENTATION definition must reference PRODUCT_DEFINITION_SHAPE"),
                requireEntity(referenceId(instance, definition, 1), StepRepresentation.class,
                        "SHAPE_DEFINITION_REPRESENTATION used_representation must reference SHAPE_REPRESENTATION")
        );
    }

    private StepItemDefinedTransformation resolveItemDefinedTransformation(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "ITEM_DEFINED_TRANSFORMATION");
        requireParameterCount(instance, definition, 4);
        return new StepItemDefinedTransformation(
                instance.id(),
                stringValue(instance, definition, 0),
                stringValue(instance, definition, 1),
                requireEntity(referenceId(instance, definition, 2), StepAxis2Placement3D.class,
                        "ITEM_DEFINED_TRANSFORMATION transform_item_1 must reference AXIS2_PLACEMENT_3D"),
                requireEntity(referenceId(instance, definition, 3), StepAxis2Placement3D.class,
                        "ITEM_DEFINED_TRANSFORMATION transform_item_2 must reference AXIS2_PLACEMENT_3D")
        );
    }

    private StepRepresentationRelationshipWithTransformation resolveRepresentationRelationshipWithTransformation(
            StepEntityInstance instance
    ) {
        StepEntityDefinition relationship = definition(instance, "REPRESENTATION_RELATIONSHIP");
        StepEntityDefinition transformation = definition(instance, "REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION");
        requireParameterCount(instance, relationship, 4);
        requireParameterCount(instance, transformation, 1);
        return new StepRepresentationRelationshipWithTransformation(
                instance.id(),
                stringValue(instance, relationship, 0),
                stringValue(instance, relationship, 1),
                requireEntity(referenceId(instance, relationship, 2), StepRepresentation.class,
                        "REPRESENTATION_RELATIONSHIP rep_1 must reference REPRESENTATION"),
                requireEntity(referenceId(instance, relationship, 3), StepRepresentation.class,
                        "REPRESENTATION_RELATIONSHIP rep_2 must reference REPRESENTATION"),
                requireEntity(referenceId(instance, transformation, 0), StepItemDefinedTransformation.class,
                        "REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION transformation_operator must reference ITEM_DEFINED_TRANSFORMATION")
        );
    }

    private StepShapeRepresentationRelationship resolveShapeRepresentationRelationship(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "SHAPE_REPRESENTATION_RELATIONSHIP");
        requireParameterCount(instance, definition, 4);
        return new StepShapeRepresentationRelationship(
                instance.id(),
                stringValue(instance, definition, 0),
                stringValue(instance, definition, 1),
                requireEntity(referenceId(instance, definition, 2), StepRepresentation.class,
                        "SHAPE_REPRESENTATION_RELATIONSHIP rep_1 must reference REPRESENTATION"),
                requireEntity(referenceId(instance, definition, 3), StepRepresentation.class,
                        "SHAPE_REPRESENTATION_RELATIONSHIP rep_2 must reference REPRESENTATION")
        );
    }

    private StepUncertaintyMeasureWithUnit resolveUncertaintyMeasureWithUnit(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "UNCERTAINTY_MEASURE_WITH_UNIT");
        requireParameterCount(instance, definition, 4);
        return new StepUncertaintyMeasureWithUnit(
                instance.id(),
                numberValue(instance, definition, 0),
                resolve(referenceId(instance, definition, 1)),
                stringValue(instance, definition, 2),
                stringValue(instance, definition, 3)
        );
    }

    private StepGlobalUnitAssignedContext resolveGlobalUnitAssignedContext(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "GLOBAL_UNIT_ASSIGNED_CONTEXT");
        requireParameterCount(instance, definition, 1);
        return new StepGlobalUnitAssignedContext(
                instance.id(),
                entityReferenceList(instance, definition, 0,
                        "GLOBAL_UNIT_ASSIGNED_CONTEXT units must contain entity references")
        );
    }

    private StepGlobalUncertaintyAssignedContext resolveGlobalUncertaintyAssignedContext(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT");
        requireParameterCount(instance, definition, 1);
        return new StepGlobalUncertaintyAssignedContext(
                instance.id(),
                referenceList(instance, definition, 0, StepUncertaintyMeasureWithUnit.class,
                        "GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT uncertainties must contain UNCERTAINTY_MEASURE_WITH_UNIT references")
        );
    }

    private StepNextAssemblyUsageOccurrence resolveNextAssemblyUsageOccurrence(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "NEXT_ASSEMBLY_USAGE_OCCURRENCE");
        requireParameterCount(instance, definition, 5);
        return new StepNextAssemblyUsageOccurrence(
                instance.id(),
                stringValue(instance, definition, 0),
                stringValue(instance, definition, 1),
                stringValue(instance, definition, 2),
                requireEntity(referenceId(instance, definition, 3), StepProductDefinition.class,
                        "NEXT_ASSEMBLY_USAGE_OCCURRENCE relating_product_definition must reference PRODUCT_DEFINITION"),
                requireEntity(referenceId(instance, definition, 4), StepProductDefinition.class,
                        "NEXT_ASSEMBLY_USAGE_OCCURRENCE related_product_definition must reference PRODUCT_DEFINITION")
        );
    }

    private StepContextDependentShapeRepresentation resolveContextDependentShapeRepresentation(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "CONTEXT_DEPENDENT_SHAPE_REPRESENTATION");
        requireParameterCount(instance, definition, 2);
        StepEntity relationship = resolve(referenceId(instance, definition, 0));
        if (!(relationship instanceof StepShapeRepresentationRelationship)
                && !(relationship instanceof StepRepresentationRelationshipWithTransformation)) {
            throw new StepResolutionException(
                    "CONTEXT_DEPENDENT_SHAPE_REPRESENTATION representation_relation must reference a representation relationship"
                            + " but got " + relationship.getClass().getSimpleName()
            );
        }
        return new StepContextDependentShapeRepresentation(
                instance.id(),
                relationship,
                requireEntity(referenceId(instance, definition, 1), StepNextAssemblyUsageOccurrence.class,
                        "CONTEXT_DEPENDENT_SHAPE_REPRESENTATION represented_product_relation must reference NEXT_ASSEMBLY_USAGE_OCCURRENCE")
        );
    }

    private StepMeasureWithUnit resolveMeasureWithUnit(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "MEASURE_WITH_UNIT");
        requireParameterCount(instance, definition, 2);
        return new StepMeasureWithUnit(
                instance.id(),
                numberValue(instance, definition, 0),
                resolve(referenceId(instance, definition, 1))
        );
    }

    private StepGeometricRepresentationContext resolveGeometricRepresentationContext(StepEntityInstance instance) {
        StepEntityDefinition geometric = definition(instance, "GEOMETRIC_REPRESENTATION_CONTEXT");
        StepEntityDefinition representation = definition(instance, "REPRESENTATION_CONTEXT");
        requireParameterCount(instance, geometric, 1);
        requireParameterCount(instance, representation, 2);
        StepGlobalUnitAssignedContext globalUnits = instance.hasDefinition("GLOBAL_UNIT_ASSIGNED_CONTEXT")
                ? resolveGlobalUnitAssignedContext(instance)
                : null;
        StepGlobalUncertaintyAssignedContext globalUncertainty = instance.hasDefinition("GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT")
                ? resolveGlobalUncertaintyAssignedContext(instance)
                : null;
        return new StepGeometricRepresentationContext(
                instance.id(),
                integerValue(instance, geometric, 0),
                stringValue(instance, representation, 0),
                stringValue(instance, representation, 1),
                globalUnits,
                globalUncertainty
        );
    }

    private StepNamedUnit resolveNamedUnit(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "NAMED_UNIT");
        requireParameterCount(instance, definition, 1);
        if (!isUnset(definition.parameters().getFirst())) {
            throw new UnsupportedStepEntityException("NAMED_UNIT dimensions must be omitted or not provided");
        }
        return new StepNamedUnit(instance.id(), deriveUnitKind(instance));
    }

    private StepSiUnit resolveSiUnit(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "SI_UNIT");
        requireParameterCount(instance, definition, 2);
        String prefix = null;
        if (!isUnset(definition.parameters().get(0))) {
            prefix = enumValue(instance, definition, 0);
        }
        return new StepSiUnit(
                instance.id(),
                deriveUnitKind(instance),
                prefix,
                enumValue(instance, definition, 1)
        );
    }

    private StepRepresentation resolveRepresentation(StepEntityInstance instance, boolean shapeRepresentation) {
        String entityName = shapeRepresentation ? "SHAPE_REPRESENTATION" : "REPRESENTATION";
        return resolveRepresentation(instance, entityName, shapeRepresentation);
    }

    private StepRepresentation resolveRepresentation(
            StepEntityInstance instance,
            String entityName,
            boolean shapeRepresentation
    ) {
        StepEntityDefinition definition = definition(instance, entityName);
        requireParameterCount(instance, definition, 3);
        List<StepEntity> items = entityReferenceList(instance, definition, 1,
                entityName + " items must contain entity references");
        StepEntity context = resolve(referenceId(instance, definition, 2));
        if (!(context instanceof StepRepresentationContext) && !(context instanceof StepGeometricRepresentationContext)) {
            throw new StepResolutionException(entityName + " context must reference a representation context");
        }
        return new StepRepresentation(
                instance.id(),
                stringValue(instance, definition, 0),
                items,
                context,
                shapeRepresentation
        );
    }

    private StepColourRgb resolveColourRgb(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "COLOUR_RGB");
        requireParameterCount(instance, definition, 4);
        return new StepColourRgb(
                instance.id(),
                stringValue(instance, definition, 0),
                numberValue(instance, definition, 1),
                numberValue(instance, definition, 2),
                numberValue(instance, definition, 3)
        );
    }

    private StepFillAreaStyleColour resolveFillAreaStyleColour(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "FILL_AREA_STYLE_COLOUR");
        requireParameterCount(instance, definition, 2);
        return new StepFillAreaStyleColour(
                instance.id(),
                optionalStringValue(instance, definition, 0),
                requireEntity(referenceId(instance, definition, 1), StepColourRgb.class,
                        "FILL_AREA_STYLE_COLOUR colour must reference COLOUR_RGB")
        );
    }

    private StepFillAreaStyle resolveFillAreaStyle(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "FILL_AREA_STYLE");
        requireParameterCount(instance, definition, 2);
        return new StepFillAreaStyle(
                instance.id(),
                optionalStringValue(instance, definition, 0),
                referenceList(instance, definition, 1, StepFillAreaStyleColour.class,
                        "FILL_AREA_STYLE styles must contain FILL_AREA_STYLE_COLOUR references")
        );
    }

    private StepSurfaceStyleFillArea resolveSurfaceStyleFillArea(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_FILL_AREA");
        requireParameterCount(instance, definition, 1);
        return new StepSurfaceStyleFillArea(
                instance.id(),
                requireEntity(referenceId(instance, definition, 0), StepFillAreaStyle.class,
                        "SURFACE_STYLE_FILL_AREA fill style must reference FILL_AREA_STYLE")
        );
    }

    private StepSurfaceSideStyle resolveSurfaceSideStyle(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "SURFACE_SIDE_STYLE");
        requireParameterCount(instance, definition, 2);
        return new StepSurfaceSideStyle(
                instance.id(),
                optionalStringValue(instance, definition, 0),
                referenceList(instance, definition, 1, StepSurfaceStyleFillArea.class,
                        "SURFACE_SIDE_STYLE styles must contain SURFACE_STYLE_FILL_AREA references")
        );
    }

    private StepSurfaceStyleUsage resolveSurfaceStyleUsage(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_USAGE");
        requireParameterCount(instance, definition, 2);
        return new StepSurfaceStyleUsage(
                instance.id(),
                enumValue(instance, definition, 0),
                requireEntity(referenceId(instance, definition, 1), StepSurfaceSideStyle.class,
                        "SURFACE_STYLE_USAGE style must reference SURFACE_SIDE_STYLE")
        );
    }

    private StepPresentationStyleAssignment resolvePresentationStyleAssignment(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "PRESENTATION_STYLE_ASSIGNMENT");
        requireParameterCount(instance, definition, 1);
        return new StepPresentationStyleAssignment(
                instance.id(),
                referenceList(instance, definition, 0, StepSurfaceStyleUsage.class,
                        "PRESENTATION_STYLE_ASSIGNMENT styles must contain SURFACE_STYLE_USAGE references")
        );
    }

    private StepStyledItem resolveStyledItem(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "STYLED_ITEM");
        requireParameterCount(instance, definition, 3);
        return new StepStyledItem(
                instance.id(),
                optionalStringValue(instance, definition, 0),
                referenceList(instance, definition, 1, StepPresentationStyleAssignment.class,
                        "STYLED_ITEM styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
                resolve(referenceId(instance, definition, 2))
        );
    }

    private StepPresentationLayerAssignment resolvePresentationLayerAssignment(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "PRESENTATION_LAYER_ASSIGNMENT");
        requireParameterCount(instance, definition, 3);
        return new StepPresentationLayerAssignment(
                instance.id(),
                stringValue(instance, definition, 0),
                optionalStringValue(instance, definition, 1),
                entityReferenceList(instance, definition, 2, "PRESENTATION_LAYER_ASSIGNMENT assigned items must contain entity references")
        );
    }

    private StepAnnotationTextOccurrence resolveAnnotationTextOccurrence(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "ANNOTATION_TEXT_OCCURRENCE");
        requireParameterCount(instance, definition, 3);
        return new StepAnnotationTextOccurrence(
                instance.id(),
                stringValue(instance, definition, 0),
                stringValue(instance, definition, 1),
                requireEntity(referenceId(instance, definition, 2), StepCartesianPoint.class,
                        "ANNOTATION_TEXT_OCCURRENCE position must reference CARTESIAN_POINT")
        );
    }

    private StepGeometricCurveSet resolveGeometricCurveSet(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "GEOMETRIC_CURVE_SET");
        requireParameterCount(instance, definition, 2);
        List<StepEntity> elements = entityReferenceList(instance, definition, 1,
                "GEOMETRIC_CURVE_SET elements must contain entity references");
        for (StepEntity element : elements) {
            if (!(element instanceof StepLine)
                    && !(element instanceof StepCircle)
                    && !(element instanceof StepEllipse)
                    && !(element instanceof StepTrimmedCurve)
                    && !(element instanceof StepSurfaceCurve)
                    && !(element instanceof StepBSplineCurveWithKnots)
                    && !(element instanceof StepCartesianPoint)) {
                throw new UnsupportedStepEntityException("GEOMETRIC_CURVE_SET elements must be supported curves or points");
            }
        }
        return new StepGeometricCurveSet(instance.id(), stringValue(instance, definition, 0), elements);
    }

    private StepDraughtingCallout resolveDraughtingCallout(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "DRAUGHTING_CALLOUT");
        requireParameterCount(instance, definition, 2);
        List<StepEntity> contents = entityReferenceList(instance, definition, 1,
                "DRAUGHTING_CALLOUT contents must contain entity references");
        for (StepEntity content : contents) {
            if (!(content instanceof StepAnnotationTextOccurrence) && !(content instanceof StepGeometricCurveSet)) {
                throw new UnsupportedStepEntityException("DRAUGHTING_CALLOUT contents must reference ANNOTATION_TEXT_OCCURRENCE or GEOMETRIC_CURVE_SET");
            }
        }
        return new StepDraughtingCallout(instance.id(), stringValue(instance, definition, 0), contents);
    }

    private StepMeasureRepresentationItem resolveMeasureRepresentationItem(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "MEASURE_REPRESENTATION_ITEM");
        requireParameterCount(instance, definition, 3);
        StepValue value = definition.parameters().get(1);
        if (!(value instanceof StepValue.TypedValue typedValue)) {
            throw new StepResolutionException("MEASURE_REPRESENTATION_ITEM parameter 1 must be a typed measure value");
        }
        StepValue unwrapped = unwrapTyped(typedValue.value());
        if (!(unwrapped instanceof StepValue.NumberValue numberValue)) {
            throw new StepResolutionException("MEASURE_REPRESENTATION_ITEM typed measure must wrap a number");
        }
        return new StepMeasureRepresentationItem(
                instance.id(),
                stringValue(instance, definition, 0),
                typedValue.typeName(),
                numberValue.value(),
                resolve(referenceId(instance, definition, 2))
        );
    }

    private StepGeometricItemSpecificUsage resolveGeometricItemSpecificUsage(StepEntityInstance instance) {
        StepEntityDefinition definition = definition(instance, "GEOMETRIC_ITEM_SPECIFIC_USAGE");
        requireParameterCount(instance, definition, 4);
        StepEntity usage = resolve(referenceId(instance, definition, 2));
        if (!(usage instanceof StepDraughtingCallout) && !(usage instanceof StepAnnotationTextOccurrence)) {
            throw new UnsupportedStepEntityException("GEOMETRIC_ITEM_SPECIFIC_USAGE usage must reference DRAUGHTING_CALLOUT or ANNOTATION_TEXT_OCCURRENCE");
        }
        StepEntity identifiedItem = resolve(referenceId(instance, definition, 3));
        if (!(identifiedItem instanceof StepFaceEntity)
                && !(identifiedItem instanceof StepEdgeCurve)
                && !(identifiedItem instanceof StepRepresentation)) {
            throw new UnsupportedStepEntityException("GEOMETRIC_ITEM_SPECIFIC_USAGE identified item must reference FACE, EDGE_CURVE or REPRESENTATION");
        }
        return new StepGeometricItemSpecificUsage(
                instance.id(),
                stringValue(instance, definition, 0),
                stringValue(instance, definition, 1),
                usage,
                identifiedItem
        );
    }

    private StepEntityDefinition definition(StepEntityInstance instance, String name) {
        return instance.requireDefinition(name);
    }

    private static void requireParameterCount(StepEntityInstance instance, StepEntityDefinition definition, int expected) {
        if (definition.parameters().size() != expected) {
            throw new StepResolutionException(
                    definition.name() + " expects " + expected + " parameters but got " + definition.parameters().size()
                            + " in entity #" + instance.id()
            );
        }
    }

    private String stringValue(StepEntityInstance instance, StepEntityDefinition definition, int index) {
        StepValue value = unwrapTyped(definition.parameters().get(index));
        if (value instanceof StepValue.StringValue stringValue) {
            return stringValue.value();
        }
        throw new StepResolutionException(definition.name() + " parameter " + index + " must be a string");
    }

    private String optionalStringValue(StepEntityInstance instance, StepEntityDefinition definition, int index) {
        StepValue value = definition.parameters().get(index);
        if (isUnset(value)) {
            return "";
        }
        return stringValue(instance, definition, index);
    }

    private double numberValue(StepEntityInstance instance, StepEntityDefinition definition, int index) {
        StepValue value = unwrapTyped(definition.parameters().get(index));
        if (value instanceof StepValue.NumberValue numberValue) {
            return numberValue.value();
        }
        throw new StepResolutionException(definition.name() + " parameter " + index + " must be a number");
    }

    private int integerValue(StepEntityInstance instance, StepEntityDefinition definition, int index) {
        double value = numberValue(instance, definition, index);
        if (value != Math.rint(value)) {
            throw new StepResolutionException(definition.name() + " parameter " + index + " must be an integer");
        }
        return (int) value;
    }

    private String enumValue(StepEntityInstance instance, StepEntityDefinition definition, int index) {
        StepValue value = unwrapTyped(definition.parameters().get(index));
        if (value instanceof StepValue.EnumValue enumValue) {
            return enumValue.value();
        }
        throw new StepResolutionException(definition.name() + " parameter " + index + " must be an enum");
    }

    private boolean booleanValue(StepEntityInstance instance, StepEntityDefinition definition, int index) {
        return switch (enumValue(instance, definition, index)) {
            case "T" -> true;
            case "F" -> false;
            default -> throw new StepResolutionException(definition.name() + " parameter " + index + " must be .T. or .F.");
        };
    }

    private int referenceId(StepEntityInstance instance, StepEntityDefinition definition, int index) {
        StepValue value = unwrapTyped(definition.parameters().get(index));
        if (value instanceof StepValue.ReferenceValue referenceValue) {
            return referenceValue.id();
        }
        throw new StepResolutionException(definition.name() + " parameter " + index + " must be a reference");
    }

    private List<Double> coordinateTriple(StepEntityInstance instance, StepEntityDefinition definition, int index) {
        return coordinateList(instance, definition, index, 3, 3);
    }

    private List<Double> coordinateList(
            StepEntityInstance instance,
            StepEntityDefinition definition,
            int index,
            int minSize,
            int maxSize
    ) {
        StepValue value = unwrapTyped(definition.parameters().get(index));
        if (!(value instanceof StepValue.ListValue listValue)) {
            throw new StepResolutionException(definition.name() + " parameter " + index + " must be a list");
        }
        if (listValue.elements().size() < minSize || listValue.elements().size() > maxSize) {
            throw new UnsupportedStepEntityException(definition.name() + " only supports " + minSize + "D to " + maxSize + "D coordinates");
        }
        List<Double> result = new ArrayList<>(listValue.elements().size());
        for (StepValue element : listValue.elements()) {
            StepValue unwrapped = unwrapTyped(element);
            if (unwrapped instanceof StepValue.NumberValue numberValue) {
                result.add(numberValue.value());
            } else {
                throw new StepResolutionException(definition.name() + " coordinate list must contain only numbers");
            }
        }
        return List.copyOf(result);
    }

    private List<Double> numberList(StepEntityInstance instance, StepEntityDefinition definition, int index) {
        StepValue value = unwrapTyped(definition.parameters().get(index));
        if (!(value instanceof StepValue.ListValue listValue)) {
            throw new StepResolutionException(definition.name() + " parameter " + index + " must be a list");
        }
        List<Double> result = new ArrayList<>(listValue.elements().size());
        for (StepValue element : listValue.elements()) {
            StepValue unwrapped = unwrapTyped(element);
            if (!(unwrapped instanceof StepValue.NumberValue numberValue)) {
                throw new StepResolutionException(definition.name() + " numeric list must contain only numbers");
            }
            result.add(numberValue.value());
        }
        return List.copyOf(result);
    }

    private <T extends StepEntity> List<List<T>> referenceGrid(
            StepEntityInstance instance,
            StepEntityDefinition definition,
            int index,
            Class<T> type,
            String message
    ) {
        StepValue value = unwrapTyped(definition.parameters().get(index));
        if (!(value instanceof StepValue.ListValue outerList)) {
            throw new StepResolutionException(definition.name() + " parameter " + index + " must be a nested list");
        }
        List<List<T>> grid = new ArrayList<>(outerList.elements().size());
        for (StepValue rowValue : outerList.elements()) {
            StepValue row = unwrapTyped(rowValue);
            if (!(row instanceof StepValue.ListValue rowList)) {
                throw new StepResolutionException(message);
            }
            List<T> entries = new ArrayList<>(rowList.elements().size());
            for (StepValue element : rowList.elements()) {
                StepValue unwrapped = unwrapTyped(element);
                if (!(unwrapped instanceof StepValue.ReferenceValue referenceValue)) {
                    throw new StepResolutionException(message);
                }
                entries.add(requireEntity(referenceValue.id(), type, message));
            }
            grid.add(List.copyOf(entries));
        }
        return List.copyOf(grid);
    }

    private List<Integer> integerList(StepEntityInstance instance, StepEntityDefinition definition, int index) {
        List<Double> values = numberList(instance, definition, index);
        List<Integer> result = new ArrayList<>(values.size());
        for (double value : values) {
            if (value != Math.rint(value)) {
                throw new StepResolutionException(definition.name() + " integer list must contain only integers");
            }
            result.add((int) value);
        }
        return List.copyOf(result);
    }

    private boolean isUnset(StepValue value) {
        StepValue unwrapped = unwrapTyped(value);
        return unwrapped instanceof StepValue.OmittedValue || unwrapped instanceof StepValue.NotProvidedValue;
    }

    private StepValue unwrapTyped(StepValue value) {
        StepValue current = value;
        while (current instanceof StepValue.TypedValue typedValue) {
            current = typedValue.value();
        }
        return current;
    }

    private <T extends StepEntity> T requireEntity(int id, Class<T> type, String message) {
        StepEntity entity = resolve(id);
        if (!type.isInstance(entity)) {
            throw new StepResolutionException(message + " but got " + entity.getClass().getSimpleName());
        }
        return type.cast(entity);
    }

    private <T extends StepEntity> List<T> referenceList(
            StepEntityInstance instance,
            StepEntityDefinition definition,
            int index,
            Class<T> type,
            String message
    ) {
        StepValue value = unwrapTyped(definition.parameters().get(index));
        if (!(value instanceof StepValue.ListValue listValue)) {
            throw new StepResolutionException(definition.name() + " parameter " + index + " must be a list");
        }
        List<T> result = new ArrayList<>();
        for (StepValue element : listValue.elements()) {
            StepValue unwrapped = unwrapTyped(element);
            if (!(unwrapped instanceof StepValue.ReferenceValue referenceValue)) {
                throw new StepResolutionException(message);
            }
            result.add(requireEntity(referenceValue.id(), type, message));
        }
        return List.copyOf(result);
    }

    private List<StepEntity> entityReferenceList(
            StepEntityInstance instance,
            StepEntityDefinition definition,
            int index,
            String message
    ) {
        StepValue value = unwrapTyped(definition.parameters().get(index));
        if (!(value instanceof StepValue.ListValue listValue)) {
            throw new StepResolutionException(definition.name() + " parameter " + index + " must be a list");
        }
        List<StepEntity> result = new ArrayList<>();
        for (StepValue element : listValue.elements()) {
            StepValue unwrapped = unwrapTyped(element);
            if (!(unwrapped instanceof StepValue.ReferenceValue referenceValue)) {
                throw new StepResolutionException(message);
            }
            result.add(resolve(referenceValue.id()));
        }
        return List.copyOf(result);
    }

    private String deriveUnitKind(StepEntityInstance instance) {
        for (String candidate : List.of("LENGTH_UNIT", "PLANE_ANGLE_UNIT", "SOLID_ANGLE_UNIT")) {
            if (instance.hasDefinition(candidate)) {
                return candidate;
            }
        }
        return "NAMED_UNIT";
    }

    private static Map<String, EntityFactory> createRegistry() {
        Map<String, EntityFactory> registry = new LinkedHashMap<>();
        registry.put("GEOMETRIC_REPRESENTATION_CONTEXT", StepEntityResolver::resolveGeometricRepresentationContext);
        registry.put("SHAPE_REPRESENTATION", (resolver, instance) -> resolver.resolveRepresentation(instance, true));
        registry.put("REPRESENTATION", (resolver, instance) -> resolver.resolveRepresentation(instance, false));
        registry.put("APPLICATION_CONTEXT", StepEntityResolver::resolveApplicationContext);
        registry.put("PRODUCT_CONTEXT", StepEntityResolver::resolveProductContext);
        registry.put("PRODUCT", StepEntityResolver::resolveProduct);
        registry.put("PRODUCT_DEFINITION_FORMATION", StepEntityResolver::resolveProductDefinitionFormation);
        registry.put("PRODUCT_DEFINITION_CONTEXT", StepEntityResolver::resolveProductDefinitionContext);
        registry.put("PRODUCT_DEFINITION", StepEntityResolver::resolveProductDefinition);
        registry.put("PRODUCT_DEFINITION_SHAPE", StepEntityResolver::resolveProductDefinitionShape);
        registry.put("SHAPE_DEFINITION_REPRESENTATION", StepEntityResolver::resolveShapeDefinitionRepresentation);
        registry.put("ITEM_DEFINED_TRANSFORMATION", StepEntityResolver::resolveItemDefinedTransformation);
        registry.put("REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION",
                StepEntityResolver::resolveRepresentationRelationshipWithTransformation);
        registry.put("SHAPE_REPRESENTATION_RELATIONSHIP", StepEntityResolver::resolveShapeRepresentationRelationship);
        registry.put("NEXT_ASSEMBLY_USAGE_OCCURRENCE", StepEntityResolver::resolveNextAssemblyUsageOccurrence);
        registry.put("CONTEXT_DEPENDENT_SHAPE_REPRESENTATION", StepEntityResolver::resolveContextDependentShapeRepresentation);
        registry.put("UNCERTAINTY_MEASURE_WITH_UNIT", StepEntityResolver::resolveUncertaintyMeasureWithUnit);
        registry.put("GLOBAL_UNIT_ASSIGNED_CONTEXT", StepEntityResolver::resolveGlobalUnitAssignedContext);
        registry.put("GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT", StepEntityResolver::resolveGlobalUncertaintyAssignedContext);
        registry.put("MEASURE_WITH_UNIT", StepEntityResolver::resolveMeasureWithUnit);
        registry.put("SI_UNIT", StepEntityResolver::resolveSiUnit);
        registry.put("NAMED_UNIT", StepEntityResolver::resolveNamedUnit);
        registry.put("REPRESENTATION_CONTEXT", StepEntityResolver::resolveRepresentationContext);
        registry.put("DEFINITIONAL_REPRESENTATION", (resolver, instance) -> resolver.resolveRepresentation(instance, "DEFINITIONAL_REPRESENTATION", false));
        registry.put("COLOUR_RGB", StepEntityResolver::resolveColourRgb);
        registry.put("FILL_AREA_STYLE_COLOUR", StepEntityResolver::resolveFillAreaStyleColour);
        registry.put("FILL_AREA_STYLE", StepEntityResolver::resolveFillAreaStyle);
        registry.put("SURFACE_STYLE_FILL_AREA", StepEntityResolver::resolveSurfaceStyleFillArea);
        registry.put("SURFACE_SIDE_STYLE", StepEntityResolver::resolveSurfaceSideStyle);
        registry.put("SURFACE_STYLE_USAGE", StepEntityResolver::resolveSurfaceStyleUsage);
        registry.put("PRESENTATION_STYLE_ASSIGNMENT", StepEntityResolver::resolvePresentationStyleAssignment);
        registry.put("STYLED_ITEM", StepEntityResolver::resolveStyledItem);
        registry.put("PRESENTATION_LAYER_ASSIGNMENT", StepEntityResolver::resolvePresentationLayerAssignment);
        registry.put("ANNOTATION_TEXT_OCCURRENCE", StepEntityResolver::resolveAnnotationTextOccurrence);
        registry.put("GEOMETRIC_CURVE_SET", StepEntityResolver::resolveGeometricCurveSet);
        registry.put("DRAUGHTING_CALLOUT", StepEntityResolver::resolveDraughtingCallout);
        registry.put("GEOMETRIC_ITEM_SPECIFIC_USAGE", StepEntityResolver::resolveGeometricItemSpecificUsage);
        registry.put("MEASURE_REPRESENTATION_ITEM", StepEntityResolver::resolveMeasureRepresentationItem);
        registry.put("CARTESIAN_POINT", StepEntityResolver::resolveCartesianPoint);
        registry.put("DIRECTION", StepEntityResolver::resolveDirection);
        registry.put("VECTOR", StepEntityResolver::resolveVector);
        registry.put("AXIS2_PLACEMENT_3D", StepEntityResolver::resolveAxis2Placement3D);
        registry.put("LINE", StepEntityResolver::resolveLine);
        registry.put("PLANE", StepEntityResolver::resolvePlane);
        registry.put("CIRCLE", StepEntityResolver::resolveCircle);
        registry.put("ELLIPSE", StepEntityResolver::resolveEllipse);
        registry.put("SURFACE_CURVE", StepEntityResolver::resolveSurfaceCurve);
        registry.put("SEAM_CURVE", StepEntityResolver::resolveSeamCurve);
        registry.put("PCURVE", StepEntityResolver::resolvePcurve);
        registry.put("B_SPLINE_CURVE_WITH_KNOTS", StepEntityResolver::resolveBSplineCurveWithKnots);
        registry.put("B_SPLINE_SURFACE_WITH_KNOTS", StepEntityResolver::resolveBSplineSurfaceWithKnots);
        registry.put("CYLINDRICAL_SURFACE", StepEntityResolver::resolveCylindricalSurface);
        registry.put("CONICAL_SURFACE", StepEntityResolver::resolveConicalSurface);
        registry.put("TOROIDAL_SURFACE", StepEntityResolver::resolveToroidalSurface);
        registry.put("TRIMMED_CURVE", StepEntityResolver::resolveTrimmedCurve);
        registry.put("VERTEX_POINT", StepEntityResolver::resolveVertexPoint);
        registry.put("EDGE_CURVE", StepEntityResolver::resolveEdgeCurve);
        registry.put("ORIENTED_EDGE", StepEntityResolver::resolveOrientedEdge);
        registry.put("VERTEX_LOOP", StepEntityResolver::resolveVertexLoop);
        registry.put("EDGE_LOOP", StepEntityResolver::resolveEdgeLoop);
        registry.put("FACE_OUTER_BOUND", (resolver, instance) -> resolver.resolveFaceBound(instance, true));
        registry.put("FACE_BOUND", (resolver, instance) -> resolver.resolveFaceBound(instance, false));
        registry.put("FACE_SURFACE", StepEntityResolver::resolveFaceSurface);
        registry.put("ADVANCED_FACE", StepEntityResolver::resolveAdvancedFace);
        registry.put("ORIENTED_FACE", StepEntityResolver::resolveOrientedFace);
        registry.put("OPEN_SHELL", StepEntityResolver::resolveOpenShell);
        registry.put("CLOSED_SHELL", StepEntityResolver::resolveClosedShell);
        registry.put("MANIFOLD_SOLID_BREP", StepEntityResolver::resolveManifoldSolidBrep);
        return registry;
    }

    private interface EntityFactory {
        StepEntity create(StepEntityResolver resolver, StepEntityInstance instance);
    }
}
