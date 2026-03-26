package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.StepAdvancedFace;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepCircle;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepCylindricalSurface;
import com.minicad.step.model.StepDirection;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEdgeLoop;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceBound;
import com.minicad.step.model.StepLine;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.model.StepOrientedEdge;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepVector;
import com.minicad.step.model.StepVertexPoint;
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

        StepEntity entity = switch (instance.name()) {
            case "CARTESIAN_POINT" -> resolveCartesianPoint(instance);
            case "DIRECTION" -> resolveDirection(instance);
            case "VECTOR" -> resolveVector(instance);
            case "AXIS2_PLACEMENT_3D" -> resolveAxis2Placement3D(instance);
            case "LINE" -> resolveLine(instance);
            case "PLANE" -> resolvePlane(instance);
            case "CIRCLE" -> resolveCircle(instance);
            case "CYLINDRICAL_SURFACE" -> resolveCylindricalSurface(instance);
            case "VERTEX_POINT" -> resolveVertexPoint(instance);
            case "EDGE_CURVE" -> resolveEdgeCurve(instance);
            case "ORIENTED_EDGE" -> resolveOrientedEdge(instance);
            case "EDGE_LOOP" -> resolveEdgeLoop(instance);
            case "FACE_BOUND" -> resolveFaceBound(instance, false);
            case "FACE_OUTER_BOUND" -> resolveFaceBound(instance, true);
            case "ADVANCED_FACE" -> resolveAdvancedFace(instance);
            case "OPEN_SHELL" -> resolveOpenShell(instance);
            case "CLOSED_SHELL" -> resolveClosedShell(instance);
            case "MANIFOLD_SOLID_BREP" -> resolveManifoldSolidBrep(instance);
            default -> throw new UnsupportedStepEntityException("unsupported STEP entity " + instance.name());
        };

        resolved.put(id, entity);
        return entity;
    }

    private StepCartesianPoint resolveCartesianPoint(StepEntityInstance instance) {
        requireParameterCount(instance, 2);
        return new StepCartesianPoint(instance.id(), stringValue(instance, 0), coordinateTriple(instance, 1));
    }

    private StepDirection resolveDirection(StepEntityInstance instance) {
        requireParameterCount(instance, 2);
        return new StepDirection(instance.id(), stringValue(instance, 0), coordinateTriple(instance, 1));
    }

    private StepVector resolveVector(StepEntityInstance instance) {
        requireParameterCount(instance, 3);
        return new StepVector(
                instance.id(),
                stringValue(instance, 0),
                requireEntity(referenceId(instance, 1), StepDirection.class, "VECTOR orientation must reference DIRECTION"),
                numberValue(instance, 2)
        );
    }

    private StepAxis2Placement3D resolveAxis2Placement3D(StepEntityInstance instance) {
        requireParameterCount(instance, 4);
        if (isOmitted(instance.parameters().get(2)) || isOmitted(instance.parameters().get(3))) {
            throw new UnsupportedStepEntityException("AXIS2_PLACEMENT_3D requires explicit axis and ref direction");
        }
        return new StepAxis2Placement3D(
                instance.id(),
                stringValue(instance, 0),
                requireEntity(referenceId(instance, 1), StepCartesianPoint.class,
                        "AXIS2_PLACEMENT_3D location must reference CARTESIAN_POINT"),
                requireEntity(referenceId(instance, 2), StepDirection.class,
                        "AXIS2_PLACEMENT_3D axis must reference DIRECTION"),
                requireEntity(referenceId(instance, 3), StepDirection.class,
                        "AXIS2_PLACEMENT_3D ref direction must reference DIRECTION")
        );
    }

    private StepLine resolveLine(StepEntityInstance instance) {
        requireParameterCount(instance, 3);
        return new StepLine(
                instance.id(),
                stringValue(instance, 0),
                requireEntity(referenceId(instance, 1), StepCartesianPoint.class,
                        "LINE point must reference CARTESIAN_POINT"),
                requireEntity(referenceId(instance, 2), StepVector.class,
                        "LINE vector must reference VECTOR")
        );
    }

    private StepPlane resolvePlane(StepEntityInstance instance) {
        requireParameterCount(instance, 2);
        return new StepPlane(
                instance.id(),
                stringValue(instance, 0),
                requireEntity(referenceId(instance, 1), StepAxis2Placement3D.class,
                        "PLANE position must reference AXIS2_PLACEMENT_3D")
        );
    }

    private StepCircle resolveCircle(StepEntityInstance instance) {
        requireParameterCount(instance, 3);
        return new StepCircle(
                instance.id(),
                stringValue(instance, 0),
                requireEntity(referenceId(instance, 1), StepAxis2Placement3D.class,
                        "CIRCLE position must reference AXIS2_PLACEMENT_3D"),
                numberValue(instance, 2)
        );
    }

    private StepCylindricalSurface resolveCylindricalSurface(StepEntityInstance instance) {
        requireParameterCount(instance, 3);
        return new StepCylindricalSurface(
                instance.id(),
                stringValue(instance, 0),
                requireEntity(referenceId(instance, 1), StepAxis2Placement3D.class,
                        "CYLINDRICAL_SURFACE position must reference AXIS2_PLACEMENT_3D"),
                numberValue(instance, 2)
        );
    }

    private StepVertexPoint resolveVertexPoint(StepEntityInstance instance) {
        requireParameterCount(instance, 2);
        return new StepVertexPoint(
                instance.id(),
                stringValue(instance, 0),
                requireEntity(referenceId(instance, 1), StepCartesianPoint.class,
                        "VERTEX_POINT geometry must reference CARTESIAN_POINT")
        );
    }

    private StepEdgeCurve resolveEdgeCurve(StepEntityInstance instance) {
        requireParameterCount(instance, 5);
        StepEntity edgeGeometry = resolve(referenceId(instance, 3));
        if (!(edgeGeometry instanceof StepLine) && !(edgeGeometry instanceof StepCircle)) {
            throw new UnsupportedStepEntityException("EDGE_CURVE geometry must be LINE or CIRCLE");
        }
        return new StepEdgeCurve(
                instance.id(),
                stringValue(instance, 0),
                requireEntity(referenceId(instance, 1), StepVertexPoint.class,
                        "EDGE_CURVE start must reference VERTEX_POINT"),
                requireEntity(referenceId(instance, 2), StepVertexPoint.class,
                        "EDGE_CURVE end must reference VERTEX_POINT"),
                edgeGeometry,
                booleanValue(instance, 4)
        );
    }

    private StepOrientedEdge resolveOrientedEdge(StepEntityInstance instance) {
        requireParameterCount(instance, 5);
        if (!isOmitted(instance.parameters().get(1)) || !isOmitted(instance.parameters().get(2))) {
            throw new UnsupportedStepEntityException("ORIENTED_EDGE explicit edge_start/edge_end is unsupported");
        }
        return new StepOrientedEdge(
                instance.id(),
                stringValue(instance, 0),
                requireEntity(referenceId(instance, 3), StepEdgeCurve.class,
                        "ORIENTED_EDGE edge_element must reference EDGE_CURVE"),
                booleanValue(instance, 4)
        );
    }

    private StepEdgeLoop resolveEdgeLoop(StepEntityInstance instance) {
        requireParameterCount(instance, 2);
        List<StepOrientedEdge> edges = referenceList(instance, 1, StepOrientedEdge.class,
                "EDGE_LOOP edge list must contain ORIENTED_EDGE references");
        return new StepEdgeLoop(instance.id(), stringValue(instance, 0), edges);
    }

    private StepFaceBound resolveFaceBound(StepEntityInstance instance, boolean outer) {
        requireParameterCount(instance, 3);
        return new StepFaceBound(
                instance.id(),
                stringValue(instance, 0),
                requireEntity(referenceId(instance, 1), StepEdgeLoop.class,
                        "FACE_BOUND loop must reference EDGE_LOOP"),
                booleanValue(instance, 2),
                outer
        );
    }

    private StepAdvancedFace resolveAdvancedFace(StepEntityInstance instance) {
        requireParameterCount(instance, 4);
        StepEntity faceGeometry = resolve(referenceId(instance, 2));
        if (!(faceGeometry instanceof StepPlane) && !(faceGeometry instanceof StepCylindricalSurface)) {
            throw new UnsupportedStepEntityException("ADVANCED_FACE geometry must be PLANE or CYLINDRICAL_SURFACE");
        }
        return new StepAdvancedFace(
                instance.id(),
                stringValue(instance, 0),
                referenceList(instance, 1, StepFaceBound.class,
                        "ADVANCED_FACE bounds must contain FACE_BOUND references"),
                faceGeometry,
                booleanValue(instance, 3)
        );
    }

    private StepOpenShell resolveOpenShell(StepEntityInstance instance) {
        requireParameterCount(instance, 2);
        return new StepOpenShell(
                instance.id(),
                stringValue(instance, 0),
                referenceList(instance, 1, StepAdvancedFace.class,
                        "OPEN_SHELL faces must contain ADVANCED_FACE references")
        );
    }

    private StepClosedShell resolveClosedShell(StepEntityInstance instance) {
        requireParameterCount(instance, 2);
        return new StepClosedShell(
                instance.id(),
                stringValue(instance, 0),
                referenceList(instance, 1, StepAdvancedFace.class,
                        "CLOSED_SHELL faces must contain ADVANCED_FACE references")
        );
    }

    private StepManifoldSolidBrep resolveManifoldSolidBrep(StepEntityInstance instance) {
        requireParameterCount(instance, 2);
        return new StepManifoldSolidBrep(
                instance.id(),
                stringValue(instance, 0),
                requireEntity(referenceId(instance, 1), StepClosedShell.class,
                        "MANIFOLD_SOLID_BREP outer must reference CLOSED_SHELL")
        );
    }

    private static void requireParameterCount(StepEntityInstance instance, int expected) {
        if (instance.parameters().size() != expected) {
            throw new StepResolutionException(
                    instance.name() + " expects " + expected + " parameters but got " + instance.parameters().size()
            );
        }
    }

    private String stringValue(StepEntityInstance instance, int index) {
        StepValue value = instance.parameters().get(index);
        if (value instanceof StepValue.StringValue stringValue) {
            return stringValue.value();
        }
        throw new StepResolutionException(instance.name() + " parameter " + index + " must be a string");
    }

    private double numberValue(StepEntityInstance instance, int index) {
        StepValue value = instance.parameters().get(index);
        if (value instanceof StepValue.NumberValue numberValue) {
            return numberValue.value();
        }
        throw new StepResolutionException(instance.name() + " parameter " + index + " must be a number");
    }

    private boolean booleanValue(StepEntityInstance instance, int index) {
        StepValue value = instance.parameters().get(index);
        if (value instanceof StepValue.EnumValue enumValue) {
            return switch (enumValue.value()) {
                case "T" -> true;
                case "F" -> false;
                default -> throw new StepResolutionException(
                        instance.name() + " parameter " + index + " must be .T. or .F."
                );
            };
        }
        throw new StepResolutionException(instance.name() + " parameter " + index + " must be an enum");
    }

    private int referenceId(StepEntityInstance instance, int index) {
        StepValue value = instance.parameters().get(index);
        if (value instanceof StepValue.ReferenceValue referenceValue) {
            return referenceValue.id();
        }
        throw new StepResolutionException(instance.name() + " parameter " + index + " must be a reference");
    }

    private List<Double> coordinateTriple(StepEntityInstance instance, int index) {
        StepValue value = instance.parameters().get(index);
        if (!(value instanceof StepValue.ListValue listValue)) {
            throw new StepResolutionException(instance.name() + " parameter " + index + " must be a list");
        }
        if (listValue.elements().size() != 3) {
            throw new UnsupportedStepEntityException(instance.name() + " only supports 3D coordinates");
        }
        List<Double> result = new ArrayList<>(3);
        for (StepValue element : listValue.elements()) {
            if (element instanceof StepValue.NumberValue numberValue) {
                result.add(numberValue.value());
            } else {
                throw new StepResolutionException(instance.name() + " coordinate list must contain only numbers");
            }
        }
        return List.copyOf(result);
    }

    private boolean isOmitted(StepValue value) {
        return value instanceof StepValue.OmittedValue;
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
            int index,
            Class<T> type,
            String message
    ) {
        StepValue value = instance.parameters().get(index);
        if (!(value instanceof StepValue.ListValue listValue)) {
            throw new StepResolutionException(instance.name() + " parameter " + index + " must be a list");
        }
        List<T> result = new ArrayList<>();
        for (StepValue element : listValue.elements()) {
            if (!(element instanceof StepValue.ReferenceValue referenceValue)) {
                throw new StepResolutionException(message);
            }
            result.add(requireEntity(referenceValue.id(), type, message));
        }
        return List.copyOf(result);
    }
}
