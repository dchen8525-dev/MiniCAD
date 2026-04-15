package com.minicad.step.semantic;

import com.minicad.common.Epsilon;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.BSplineSurface3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Clothoid3;
import com.minicad.geometry.DegenerateCurve3;
import com.minicad.geometry.Parabola3;
import com.minicad.geometry.Hyperbola3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.OffsetSurface3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.Polyline3;
import com.minicad.geometry.RationalBSplineCurve3;
import com.minicad.geometry.RationalBSplineSurface3;
import com.minicad.geometry.SphericalSurface;
import com.minicad.geometry.SurfaceGeometry;
import com.minicad.geometry.SurfaceOfLinearExtrusion3;
import com.minicad.geometry.SurfaceOfRevolution3;
import com.minicad.geometry.RuledSurface3;
import com.minicad.geometry.SurfaceOfConstantRadius3;
import com.minicad.geometry.SurfaceCurve3;
import com.minicad.geometry.TrimmedCurve3;
import com.minicad.geometry.ToroidalSurface;
import com.minicad.geometry.Vector3;
import com.minicad.geometry2d.Direction2;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.CompositeCurve2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Hyperbola2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Parabola2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.Polyline2;
import com.minicad.geometry2d.RationalBSplineCurve2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.geometry2d.Vector2;
import com.minicad.step.model.StepAdvancedFace;
import com.minicad.step.model.StepAnnotationCurveOccurrence;
import com.minicad.step.model.StepAxis1Placement;
import com.minicad.step.model.StepAxis2Placement2D;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepBSplineCurve;
import com.minicad.step.model.StepBSplineSurface;
import com.minicad.step.model.StepBezierCurve;
import com.minicad.step.model.StepBezierSurface;
import com.minicad.step.model.StepBrepWithVoids;
import com.minicad.step.model.StepBooleanClippingResult;
import com.minicad.step.model.StepBooleanResult;
import com.minicad.step.model.StepBoxDomain;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepCartesianTransformationOperator;
import com.minicad.step.model.StepBSplineCurveWithKnots;
import com.minicad.step.model.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.StepCircle;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepCompositeCurve;
import com.minicad.step.model.StepCompositeCurveOnSurface;
import com.minicad.step.model.StepCompositeCurveSegment;
import com.minicad.step.model.StepConicalSurface;
import com.minicad.step.model.StepConicCurve;
import com.minicad.step.model.StepDegeneratePcurve;
import com.minicad.step.model.StepCylindricalSurface;
import com.minicad.step.model.StepDegenerateToroidalSurface;
import com.minicad.step.model.StepDimensionCurve;
import com.minicad.step.model.StepDirection;
import com.minicad.step.model.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEdgeLoop;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepEllipse;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepFaceBound;
import com.minicad.step.model.StepFaceSurface;
import com.minicad.step.model.StepCurveBoundedSurface;
import com.minicad.step.model.StepCsgPrimitive;
import com.minicad.step.model.StepCsgSolid;
import com.minicad.step.model.StepGeometricReplica;
import com.minicad.step.model.StepHalfSpaceSolid;
import com.minicad.step.model.StepLeaderCurve;
import com.minicad.step.model.StepLine;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepOffsetCurve2D;
import com.minicad.step.model.StepOffsetCurve3D;
import com.minicad.step.model.StepOffsetSurface;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.model.StepOrientedClosedShell;
import com.minicad.step.model.StepOrientedOpenShell;
import com.minicad.step.model.StepOrientedEdge;
import com.minicad.step.model.StepOrientedFace;
import com.minicad.step.model.StepOrientedCurve;
import com.minicad.step.model.StepOrientedSurface;
import com.minicad.step.model.StepPiecewiseBezierCurve;
import com.minicad.step.model.StepPiecewiseBezierSurface;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepPcurve;
import com.minicad.step.model.StepProfileDef;
import com.minicad.step.model.StepProjectionCurve;
import com.minicad.step.model.StepPolyline;
import com.minicad.step.model.StepQuasiUniformCurve;
import com.minicad.step.model.StepQuasiUniformSurface;
import com.minicad.step.model.StepRationalBSplineCurve;
import com.minicad.step.model.StepRationalBSplineSurface;
import com.minicad.step.model.StepRectangularTrimmedSurface;
import com.minicad.step.model.StepSeamCurve;
import com.minicad.step.model.StepSolidReplica;
import com.minicad.step.model.StepSurfaceCurve;
import com.minicad.step.model.StepSurfaceCurveSweptAreaSolid;
import com.minicad.step.model.StepSweptDiskSolid;
import com.minicad.step.model.StepExtrudedAreaSolidTapered;
import com.minicad.step.model.StepRevolvedAreaSolidTapered;
import com.minicad.step.model.StepRuledSurface;
import com.minicad.step.model.StepSurfaceOfConstantRadius;
import com.minicad.step.model.StepSurfacePatch;
import com.minicad.step.model.StepRectangularCompositeSurface;
import com.minicad.step.model.StepClothoid;
import com.minicad.step.model.StepIndexedPolyCurve;
import com.minicad.step.model.StepDegenerateCurve;
import com.minicad.step.model.StepNonManifoldSolidBrep;
import com.minicad.step.model.StepSurfaceOfLinearExtrusion;
import com.minicad.step.model.StepSurfaceOfRevolution;
import com.minicad.step.model.StepSweptAreaSolid;
import com.minicad.step.model.StepSurfacedOpenShell;
import com.minicad.step.model.StepSphericalSurface;
import com.minicad.step.model.StepTerminatorSymbol;
import com.minicad.step.model.StepTrimmedCurve;
import com.minicad.step.model.StepToroidalSurface;
import com.minicad.step.model.StepSubedge;
import com.minicad.step.model.StepUniformCurve;
import com.minicad.step.model.StepUniformSurface;
import com.minicad.step.model.StepVertexLoop;
import com.minicad.step.model.StepVertexPoint;
import com.minicad.topology.Edge;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
import com.minicad.topology.Loop;
import com.minicad.topology.OrientedEdge;
import com.minicad.topology.PolyLoop;
import com.minicad.topology.Shell;
import com.minicad.topology.Solid;
import com.minicad.topology.Vertex;
import com.minicad.topology.VertexLoop;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds internal geometry and topology objects from resolved STEP semantic entities.
 */
public final class StepCadBuilder {

    private final Map<Integer, StepEntity> entitiesById;
    private final Map<Integer, CartesianPoint> points = new LinkedHashMap<>();
    private final Map<Integer, Direction3> directions = new LinkedHashMap<>();
    private final Map<Integer, Vector3> vectors = new LinkedHashMap<>();
    private final Map<Integer, Axis2Placement3D> placements = new LinkedHashMap<>();
    private final Map<Integer, Point2> points2d = new LinkedHashMap<>();
    private final Map<Integer, Direction2> directions2d = new LinkedHashMap<>();
    private final Map<Integer, Line2> lines2d = new LinkedHashMap<>();
    private final Map<Integer, Circle2> circles2d = new LinkedHashMap<>();
    private final Map<Integer, Ellipse2> ellipses2d = new LinkedHashMap<>();
    private final Map<Integer, Polyline2> polylines2d = new LinkedHashMap<>();
    private final Map<Integer, CompositeCurve2> compositeCurves2d = new LinkedHashMap<>();
    private final Map<Integer, BSplineCurve2> splineCurves2d = new LinkedHashMap<>();
    private final Map<Integer, RationalBSplineCurve2> rationalSplineCurves2d = new LinkedHashMap<>();
    private final Map<Integer, TrimmedCurve2> trimmedCurves2d = new LinkedHashMap<>();
    private final Map<Integer, Line3> lines = new LinkedHashMap<>();
    private final Map<Integer, Plane> planes = new LinkedHashMap<>();
    private final Map<Integer, Circle> circles = new LinkedHashMap<>();
    private final Map<Integer, Ellipse3> ellipses = new LinkedHashMap<>();
    private final Map<Integer, Polyline3> polylines = new LinkedHashMap<>();
    private final Map<Integer, CompositeCurve3> compositeCurves = new LinkedHashMap<>();
    private final Map<Integer, CylindricalSurface> cylindricalSurfaces = new LinkedHashMap<>();
    private final Map<Integer, ConicalSurface> conicalSurfaces = new LinkedHashMap<>();
    private final Map<Integer, ToroidalSurface> toroidalSurfaces = new LinkedHashMap<>();
    private final Map<Integer, SphericalSurface> sphericalSurfaces = new LinkedHashMap<>();
    private final Map<Integer, SurfaceOfLinearExtrusion3> linearExtrusionSurfaces = new LinkedHashMap<>();
    private final Map<Integer, SurfaceOfRevolution3> revolutionSurfaces = new LinkedHashMap<>();
    private final Map<Integer, BSplineCurve3> bsplineCurves = new LinkedHashMap<>();
    private final Map<Integer, RationalBSplineCurve3> rationalBsplineCurves = new LinkedHashMap<>();
    private final Map<Integer, BSplineSurface3> bsplineSurfaces = new LinkedHashMap<>();
    private final Map<Integer, RationalBSplineSurface3> rationalBsplineSurfaces = new LinkedHashMap<>();
    private final Map<Integer, SurfaceCurve3> surfaceCurves = new LinkedHashMap<>();
    private final Map<Integer, TrimmedCurve3> trimmedCurves = new LinkedHashMap<>();
    private final Map<Integer, Parabola3> parabolas = new LinkedHashMap<>();
    private final Map<Integer, Hyperbola3> hyperbolas = new LinkedHashMap<>();
    private final Map<Integer, Clothoid3> clothoids = new LinkedHashMap<>();
    private final Map<Integer, Vertex> vertices = new LinkedHashMap<>();
    private final Map<Integer, Edge> edges = new LinkedHashMap<>();
    private final Map<Integer, OrientedEdge> orientedEdges = new LinkedHashMap<>();
    private final Map<Integer, EdgeLoop> loops = new LinkedHashMap<>();
    private final Map<Integer, VertexLoop> vertexLoops = new LinkedHashMap<>();
    private final Map<Integer, FaceBound> faceBounds = new LinkedHashMap<>();
    private final Map<Integer, Face> faces = new LinkedHashMap<>();
    private final Map<Integer, Shell> shells = new LinkedHashMap<>();
    private final Map<Integer, Solid> solids = new LinkedHashMap<>();

    private StepCadBuilder(Map<Integer, StepEntity> entitiesById) {
        this.entitiesById = Map.copyOf(entitiesById);
    }

    /**
     * Creates a builder from resolved STEP semantic entities.
     *
     * @param entitiesById resolved entities indexed by STEP id
     * @return builder instance
     */
    public static StepCadBuilder fromResolved(Map<Integer, StepEntity> entitiesById) {
        return new StepCadBuilder(entitiesById);
    }

    /**
     * Builds a Cartesian point.
     *
     * @param id STEP entity id
     * @return built point
     */
    public CartesianPoint buildPoint(int id) {
        CartesianPoint existing = points.get(id);
        if (existing != null) {
            return existing;
        }
        StepCartesianPoint point = requireEntity(id, StepCartesianPoint.class, "CARTESIAN_POINT");
        CartesianPoint built = new CartesianPoint(
                point.coordinates().get(0),
                point.coordinates().get(1),
                point.coordinates().size() > 2 ? point.coordinates().get(2) : 0.0
        );
        points.put(id, built);
        return built;
    }

    public Point2 buildPoint2(int id) {
        Point2 existing = points2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepCartesianPoint point = requireEntity(id, StepCartesianPoint.class, "CARTESIAN_POINT");
        if (point.coordinates().size() != 2) {
            throw new StepResolutionException("entity #" + id + " is not a 2D CARTESIAN_POINT");
        }
        Point2 built = new Point2(point.coordinates().get(0), point.coordinates().get(1));
        points2d.put(id, built);
        return built;
    }

    /**
     * Builds a supported 3D point reference.
     *
     * @param id STEP entity id
     * @return built point
     */
    public CartesianPoint buildPointReference(int id) {
        StepEntity entity = requireEntity(id, StepEntity.class, "3D point reference");
        if (entity instanceof StepCartesianPoint point) {
            return buildPoint(point.id());
        }
        if (entity instanceof StepVertexPoint vertexPoint) {
            return buildVertex(vertexPoint.id()).point();
        }
        if (entity instanceof StepGeometricReplica replica) {
            if (!"POINT_REPLICA".equals(replica.entityName())) {
                throw new UnsupportedGeometryException(replica.entityName() + " point reference is unsupported");
            }
            CartesianPoint parent = buildPointReference(replica.parent().id());
            return transformPoint3(parent, replica.transformation());
        }
        throw new UnsupportedGeometryException("point reference for " + stepEntityTypeName(entity) + " is unsupported");
    }

    /**
     * Builds a direction.
     *
     * @param id STEP entity id
     * @return built direction
     */
    public Direction3 buildDirection(int id) {
        Direction3 existing = directions.get(id);
        if (existing != null) {
            return existing;
        }
        StepDirection direction = requireEntity(id, StepDirection.class, "DIRECTION");
        if (direction.directionRatios().size() != 3) {
            throw new StepResolutionException("entity #" + id + " is not a 3D DIRECTION");
        }
        Direction3 built = Direction3.from(new Vector3(
                direction.directionRatios().get(0),
                direction.directionRatios().get(1),
                direction.directionRatios().get(2)
        ));
        directions.put(id, built);
        return built;
    }

    public Direction2 buildDirection2(int id) {
        Direction2 existing = directions2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepDirection direction = requireEntity(id, StepDirection.class, "DIRECTION");
        if (direction.directionRatios().size() != 2) {
            throw new StepResolutionException("entity #" + id + " is not a 2D DIRECTION");
        }
        Direction2 built = Direction2.from(new com.minicad.geometry2d.Vector2(
                direction.directionRatios().get(0),
                direction.directionRatios().get(1)
        ));
        directions2d.put(id, built);
        return built;
    }

    /**
     * Builds a vector.
     *
     * @param id STEP entity id
     * @return built vector
     */
    public Vector3 buildVector(int id) {
        Vector3 existing = vectors.get(id);
        if (existing != null) {
            return existing;
        }
        com.minicad.step.model.StepVector vector = requireEntity(id, com.minicad.step.model.StepVector.class, "VECTOR");
        Vector3 built = buildDirection(vector.orientation().id()).asVector().scale(vector.magnitude());
        vectors.put(id, built);
        return built;
    }

    /**
     * Builds a placement.
     *
     * @param id STEP entity id
     * @return built placement
     */
    public Axis2Placement3D buildPlacement(int id) {
        Axis2Placement3D existing = placements.get(id);
        if (existing != null) {
            return existing;
        }
        StepAxis2Placement3D placement = requireEntity(id, StepAxis2Placement3D.class, "AXIS2_PLACEMENT_3D");
        Axis2Placement3D built = new Axis2Placement3D(
                buildPoint(placement.location().id()),
                buildDirection(placement.axis().id()),
                buildDirection(placement.refDirection().id())
        );
        placements.put(id, built);
        return built;
    }

    public Axis1Placement buildAxis1Placement(int id) {
        StepAxis1Placement placement = requireEntity(id, StepAxis1Placement.class, "AXIS1_PLACEMENT");
        return new Axis1Placement(buildPoint(placement.location().id()), buildDirection(placement.axis().id()));
    }

    /**
     * Builds a line.
     *
     * @param id STEP entity id
     * @return built line
     */
    public Line3 buildLine(int id) {
        Line3 existing = lines.get(id);
        if (existing != null) {
            return existing;
        }
        StepLine line = requireEntity(id, StepLine.class, "LINE");
        Line3 built = new Line3(
                buildPoint(line.point().id()),
                buildDirection(line.vector().orientation().id())
        );
        lines.put(id, built);
        return built;
    }

    public Line2 buildLine2(int id) {
        Line2 existing = lines2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepLine line = requireEntity(id, StepLine.class, "LINE");
        if (line.point().coordinates().size() != 2 || line.vector().orientation().directionRatios().size() != 2) {
            throw new StepResolutionException("entity #" + id + " is not a 2D LINE");
        }
        Line2 built = new Line2(
                buildPoint2(line.point().id()),
                buildDirection2(line.vector().orientation().id())
        );
        lines2d.put(id, built);
        return built;
    }

    public Object buildPcurve2(int id) {
        StepEntity entity = requireEntity(id, StepEntity.class, "PCURVE or DEGENERATE_PCURVE");
        StepEntity item;
        if (entity instanceof StepPcurve pcurve) {
            item = pcurve.referenceToCurve().items().getFirst();
        } else if (entity instanceof StepDegeneratePcurve pcurve) {
            item = pcurve.referenceToCurve().items().getFirst();
        } else {
            throw new StepResolutionException("entity #" + id + " is not a PCURVE or DEGENERATE_PCURVE");
        }
        return buildCurve2(item);
    }

    public BSplineCurve2 buildBSplineCurve2(int id) {
        BSplineCurve2 existing = splineCurves2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepBSplineCurveWithKnots spline = requireEntity(id, StepBSplineCurveWithKnots.class, "B_SPLINE_CURVE_WITH_KNOTS");
        List<Point2> controlPoints = new ArrayList<>(spline.controlPoints().size());
        for (StepCartesianPoint point : spline.controlPoints()) {
            if (point.coordinates().size() != 2) {
                throw new UnsupportedGeometryException("B_SPLINE_CURVE_WITH_KNOTS is not a 2D spline");
            }
            controlPoints.add(buildPoint2(point.id()));
        }
        BSplineCurve2 built = new BSplineCurve2(
                spline.degree(),
                controlPoints,
                spline.knotMultiplicities(),
                spline.knots()
        );
        splineCurves2d.put(id, built);
        return built;
    }

    public BSplineCurve2 buildBezierCurve2(int id) {
        return buildImplicitBSplineCurve2(requireEntity(id, StepBezierCurve.class, "BEZIER_CURVE"));
    }

    public BSplineCurve2 buildUniformCurve2(int id) {
        return buildImplicitBSplineCurve2(requireEntity(id, StepUniformCurve.class, "UNIFORM_CURVE"));
    }

    public BSplineCurve2 buildQuasiUniformCurve2(int id) {
        return buildImplicitBSplineCurve2(requireEntity(id, StepQuasiUniformCurve.class, "QUASI_UNIFORM_CURVE"));
    }

    public BSplineCurve2 buildPiecewiseBezierCurve2(int id) {
        return buildImplicitBSplineCurve2(requireEntity(id, StepPiecewiseBezierCurve.class, "PIECEWISE_BEZIER_CURVE"));
    }

    private BSplineCurve2 buildImplicitBSplineCurve2(StepEntity entity) {
        BSplineCurve2 existing = splineCurves2d.get(entity.id());
        if (existing != null) {
            return existing;
        }
        ImplicitBSplineCurveData spline = implicitBSplineCurveData(entity);
        List<Point2> controlPoints = new ArrayList<>(spline.controlPoints().size());
        for (StepCartesianPoint point : spline.controlPoints()) {
            if (point.coordinates().size() != 2) {
                throw new UnsupportedGeometryException(stepEntityTypeName(entity) + " is not a 2D spline");
            }
            controlPoints.add(buildPoint2(point.id()));
        }
        BSplineCurve2 built = new BSplineCurve2(
                spline.degree(),
                controlPoints,
                spline.knotMultiplicities(),
                spline.knots()
        );
        splineCurves2d.put(entity.id(), built);
        return built;
    }

    public Circle2 buildCircle2(int id) {
        Circle2 existing = circles2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepCircle circle = requireEntity(id, StepCircle.class, "CIRCLE");
        if (!(circle.position() instanceof StepAxis2Placement2D placement2d)) {
            throw new StepResolutionException("entity #" + id + " is not a 2D CIRCLE");
        }
        Circle2 built = new Circle2(
                buildPoint2(placement2d.location().id()),
                buildDirection2(placement2d.refDirection().id()),
                circle.radius()
        );
        circles2d.put(id, built);
        return built;
    }

    public Ellipse2 buildEllipse2(int id) {
        Ellipse2 existing = ellipses2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepEllipse ellipse = requireEntity(id, StepEllipse.class, "ELLIPSE");
        if (!(ellipse.position() instanceof StepAxis2Placement2D placement2d)) {
            throw new StepResolutionException("entity #" + id + " is not a 2D ELLIPSE");
        }
        Ellipse2 built = new Ellipse2(
                buildPoint2(placement2d.location().id()),
                buildDirection2(placement2d.refDirection().id()),
                ellipse.semiAxis1(),
                ellipse.semiAxis2()
        );
        ellipses2d.put(id, built);
        return built;
    }

    public Polyline2 buildPolyline2(int id) {
        Polyline2 existing = polylines2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepPolyline polyline = requireEntity(id, StepPolyline.class, "POLYLINE");
        List<Point2> points = new ArrayList<>(polyline.points().size());
        for (StepCartesianPoint point : polyline.points()) {
            if (point.coordinates().size() != 2) {
                throw new StepResolutionException("entity #" + id + " is not a 2D POLYLINE");
            }
            points.add(buildPoint2(point.id()));
        }
        Polyline2 built = new Polyline2(points);
        polylines2d.put(id, built);
        return built;
    }

    public CompositeCurve2 buildCompositeCurve2(int id) {
        CompositeCurve2 existing = compositeCurves2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepEntity entity = requireExistingEntity(id);
        List<StepCompositeCurveSegment> segments;
        if (entity instanceof StepCompositeCurve compositeCurve) {
            segments = compositeCurve.segments();
        } else if (entity instanceof StepCompositeCurveOnSurface compositeCurveOnSurface) {
            segments = compositeCurveOnSurface.segments();
        } else {
            throw new StepResolutionException("entity #" + id + " is not a COMPOSITE_CURVE");
        }
        List<Curve2> curves = new ArrayList<>(segments.size());
        for (StepCompositeCurveSegment segment : segments) {
            Object built = buildCurve2(segment.parentCurve());
            if (!(built instanceof Curve2 curve)) {
                throw new UnsupportedGeometryException("COMPOSITE_CURVE segment is not a supported 2D curve");
            }
            curves.add(curve);
        }
        CompositeCurve2 built = new CompositeCurve2(curves);
        compositeCurves2d.put(id, built);
        return built;
    }

    private Object buildCurve2(StepEntity item) {
        if (item instanceof StepLine line) {
            return buildLine2(line.id());
        }
        if (item instanceof StepCircle circle) {
            return buildCircle2(circle.id());
        }
        if (item instanceof StepEllipse ellipse) {
            return buildEllipse2(ellipse.id());
        }
        if (item instanceof StepPolyline polyline) {
            return buildPolyline2(polyline.id());
        }
        if (item instanceof StepBezierCurve curve) {
            return buildBezierCurve2(curve.id());
        }
        if (item instanceof StepUniformCurve curve) {
            return buildUniformCurve2(curve.id());
        }
        if (item instanceof StepQuasiUniformCurve curve) {
            return buildQuasiUniformCurve2(curve.id());
        }
        if (item instanceof StepPiecewiseBezierCurve curve) {
            return buildPiecewiseBezierCurve2(curve.id());
        }
        if (item instanceof StepCompositeCurve compositeCurve) {
            return buildCompositeCurve2(compositeCurve.id());
        }
        if (item instanceof StepCompositeCurveOnSurface compositeCurveOnSurface) {
            return buildCompositeCurve2(compositeCurveOnSurface.id());
        }
        if (item instanceof StepConicCurve conic) {
            return buildConicCurve2(conic);
        }
        if (item instanceof StepOffsetCurve2D offsetCurve2D) {
            return buildOffsetCurve2(offsetCurve2D.id());
        }
        if (item instanceof StepOrientedCurve orientedCurve) {
            return buildCurve2(orientedCurve.curveElement());
        }
        if (item instanceof StepGeometricReplica replica) {
            return buildReplicaCurve2(replica);
        }
        if (item instanceof StepBSplineCurveWithKnots spline) {
            return buildBSplineCurve2(spline.id());
        }
        if (item instanceof StepRationalBSplineCurve spline) {
            return buildRationalBSplineCurve2(spline.id());
        }
        if (item instanceof StepTrimmedCurve trimmedCurve) {
            return buildTrimmedCurve2(trimmedCurve.id());
        }
        if (item instanceof StepIndexedPolyCurve polyCurve) {
            return buildIndexedPolyCurve2(polyCurve);
        }
        throw new UnsupportedGeometryException("PCURVE currently supports 2D LINE, CIRCLE, ELLIPSE, POLYLINE, COMPOSITE_CURVE, B_SPLINE_CURVE_WITH_KNOTS, RATIONAL_B_SPLINE_CURVE or TRIMMED_CURVE");
    }

    private Polyline2 buildIndexedPolyCurve2(StepIndexedPolyCurve polyCurve) {
        List<StepCartesianPoint> stepPoints = polyCurve.points();
        List<Integer> indices = polyCurve.indices();
        List<Point2> points = indices.stream()
                .map(index -> {
                    StepCartesianPoint stepPoint = stepPoints.get(index);
                    CartesianPoint point3D = buildPoint(stepPoint.id());
                    return new Point2(point3D.x(), point3D.y());
                })
                .toList();
        if (polyCurve.closed() && !points.isEmpty()) {
            points = new ArrayList<>(points);
            points.add(points.getFirst());
            points = List.copyOf(points);
        }
        return new Polyline2(points);
    }

    public TrimmedCurve2 buildTrimmedCurve2(int id) {
        TrimmedCurve2 existing = trimmedCurves2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepTrimmedCurve trimmedCurve = requireEntity(id, StepTrimmedCurve.class, "TRIMMED_CURVE");
        Object basis = buildCurve2(trimmedCurve.basisCurve());
        if (!(basis instanceof Curve2 basisCurve)) {
            throw new UnsupportedGeometryException("TRIMMED_CURVE basis curve is not a supported 2D curve");
        }
        Point2 trimStart = snapTrimPoint2(requireTrimPoint2(trimmedCurve.trim1(), "trim_1"), basisCurve);
        Point2 trimEnd = snapTrimPoint2(requireTrimPoint2(trimmedCurve.trim2(), "trim_2"), basisCurve);
        if (!basisCurve.contains(trimStart)) {
            throw new UnsupportedGeometryException("TRIMMED_CURVE trim_1 point must lie on basis curve");
        }
        if (!basisCurve.contains(trimEnd)) {
            throw new UnsupportedGeometryException("TRIMMED_CURVE trim_2 point must lie on basis curve");
        }
        TrimmedCurve2 built = new TrimmedCurve2(basisCurve, trimStart, trimEnd, trimmedCurve.senseAgreement());
        trimmedCurves2d.put(id, built);
        return built;
    }

    public Curve2 buildOffsetCurve2(int id) {
        StepOffsetCurve2D offsetCurve = requireEntity(id, StepOffsetCurve2D.class, "OFFSET_CURVE_2D");
        Object basisObject = buildCurve2(offsetCurve.basisCurve());
        if (!(basisObject instanceof Curve2 basisCurve)) {
            throw new UnsupportedGeometryException("OFFSET_CURVE_2D basis curve is not a supported 2D curve");
        }
        return approximateOffsetCurve2(basisCurve, offsetCurve.distance());
    }

    /**
     * Builds a plane.
     *
     * @param id STEP entity id
     * @return built plane
     */
    public Plane buildPlane(int id) {
        Plane existing = planes.get(id);
        if (existing != null) {
            return existing;
        }
        StepPlane plane = requireEntity(id, StepPlane.class, "PLANE");
        Axis2Placement3D placement = buildPlacement(plane.position().id());
        Plane built = new Plane(placement.location(), placement.axis());
        planes.put(id, built);
        return built;
    }

    /**
     * Builds a circle geometry object.
     *
     * @param id STEP entity id
     * @return built circle
     */
    public Circle buildCircle(int id) {
        Circle existing = circles.get(id);
        if (existing != null) {
            return existing;
        }
        StepCircle circle = requireEntity(id, StepCircle.class, "CIRCLE");
        if (!(circle.position() instanceof StepAxis2Placement3D placement3d)) {
            throw new StepResolutionException("entity #" + id + " is not a 3D CIRCLE");
        }
        Circle built = new Circle(buildPlacement(placement3d.id()), circle.radius());
        circles.put(id, built);
        return built;
    }

    /**
     * Builds an ellipse geometry object.
     *
     * @param id STEP entity id
     * @return built ellipse
     */
    public Ellipse3 buildEllipse(int id) {
        Ellipse3 existing = ellipses.get(id);
        if (existing != null) {
            return existing;
        }
        StepEllipse ellipse = requireEntity(id, StepEllipse.class, "ELLIPSE");
        if (!(ellipse.position() instanceof StepAxis2Placement3D placement3d)) {
            throw new StepResolutionException("entity #" + id + " is not a 3D ELLIPSE");
        }
        Ellipse3 built = new Ellipse3(buildPlacement(placement3d.id()), ellipse.semiAxis1(), ellipse.semiAxis2());
        ellipses.put(id, built);
        return built;
    }

    public Polyline3 buildPolyline(int id) {
        Polyline3 existing = polylines.get(id);
        if (existing != null) {
            return existing;
        }
        StepPolyline polyline = requireEntity(id, StepPolyline.class, "POLYLINE");
        List<CartesianPoint> points = polyline.points().stream().map(point -> buildPoint(point.id())).toList();
        Polyline3 built = new Polyline3(points);
        polylines.put(id, built);
        return built;
    }

    public CompositeCurve3 buildCompositeCurve(int id) {
        CompositeCurve3 existing = compositeCurves.get(id);
        if (existing != null) {
            return existing;
        }
        StepEntity entity = requireExistingEntity(id);
        List<StepCompositeCurveSegment> segments;
        if (entity instanceof StepCompositeCurve compositeCurve) {
            segments = compositeCurve.segments();
        } else if (entity instanceof StepCompositeCurveOnSurface compositeCurveOnSurface) {
            segments = compositeCurveOnSurface.segments();
        } else {
            throw new StepResolutionException("entity #" + id + " is not a COMPOSITE_CURVE");
        }
        List<Curve3> curves = segments.stream().map(segment -> buildCurve3(segment.parentCurve())).toList();
        CompositeCurve3 built = new CompositeCurve3(curves);
        compositeCurves.put(id, built);
        return built;
    }

    /**
     * Builds a cylindrical surface geometry object.
     *
     * @param id STEP entity id
     * @return built cylindrical surface
     */
    public CylindricalSurface buildCylindricalSurface(int id) {
        CylindricalSurface existing = cylindricalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepCylindricalSurface surface = requireEntity(id, StepCylindricalSurface.class, "CYLINDRICAL_SURFACE");
        CylindricalSurface built = new CylindricalSurface(buildPlacement(surface.position().id()), surface.radius());
        cylindricalSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a conical surface geometry object.
     *
     * @param id STEP entity id
     * @return built conical surface
     */
    public ConicalSurface buildConicalSurface(int id) {
        ConicalSurface existing = conicalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepConicalSurface surface = requireEntity(id, StepConicalSurface.class, "CONICAL_SURFACE");
        ConicalSurface built = new ConicalSurface(buildPlacement(surface.position().id()), surface.radius(), surface.semiAngle());
        conicalSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a toroidal surface geometry object.
     *
     * @param id STEP entity id
     * @return built toroidal surface
     */
    public ToroidalSurface buildToroidalSurface(int id) {
        ToroidalSurface existing = toroidalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepToroidalSurface surface = requireEntity(id, StepToroidalSurface.class, "TOROIDAL_SURFACE");
        ToroidalSurface built = new ToroidalSurface(buildPlacement(surface.position().id()), surface.majorRadius(), surface.minorRadius());
        toroidalSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a degenerate toroidal surface geometry object for validation/preview support paths.
     *
     * @param id STEP entity id
     * @return built toroidal surface
     */
    public ToroidalSurface buildDegenerateToroidalSurface(int id) {
        ToroidalSurface existing = toroidalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepDegenerateToroidalSurface surface = requireEntity(id, StepDegenerateToroidalSurface.class, "DEGENERATE_TOROIDAL_SURFACE");
        ToroidalSurface built = new ToroidalSurface(buildPlacement(surface.position().id()), surface.majorRadius(), surface.minorRadius());
        toroidalSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a spherical surface geometry object.
     *
     * @param id STEP entity id
     * @return built spherical surface
     */
    public SphericalSurface buildSphericalSurface(int id) {
        SphericalSurface existing = sphericalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepSphericalSurface surface = requireEntity(id, StepSphericalSurface.class, "SPHERICAL_SURFACE");
        SphericalSurface built = new SphericalSurface(buildPlacement(surface.position().id()), surface.radius());
        sphericalSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a SURFACE_OF_LINEAR_EXTRUSION geometry object.
     *
     * @param id STEP entity id
     * @return built extrusion surface
     */
    public SurfaceOfLinearExtrusion3 buildSurfaceOfLinearExtrusion(int id) {
        SurfaceOfLinearExtrusion3 existing = linearExtrusionSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepSurfaceOfLinearExtrusion surface =
                requireEntity(id, StepSurfaceOfLinearExtrusion.class, "SURFACE_OF_LINEAR_EXTRUSION");
        SurfaceOfLinearExtrusion3 built = new SurfaceOfLinearExtrusion3(
                buildCurve3(surface.sweptCurve()),
                buildVector(surface.extrusionAxis().id())
        );
        linearExtrusionSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a SURFACE_OF_REVOLUTION geometry object.
     *
     * @param id STEP entity id
     * @return built revolution surface
     */
    public SurfaceOfRevolution3 buildSurfaceOfRevolution(int id) {
        SurfaceOfRevolution3 existing = revolutionSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepSurfaceOfRevolution surface =
                requireEntity(id, StepSurfaceOfRevolution.class, "SURFACE_OF_REVOLUTION");
        Axis1Placement axis = buildAxis1Placement(surface.axisPosition().id());
        SurfaceOfRevolution3 built = new SurfaceOfRevolution3(
                buildCurve3(surface.sweptCurve()),
                axis.location(),
                axis.axis()
        );
        revolutionSurfaces.put(id, built);
        return built;
    }

    /**
     * Validates a RECTANGULAR_TRIMMED_SURFACE definition.
     *
     * @param id STEP entity id
     */
    public void buildRectangularTrimmedSurface(int id) {
        StepRectangularTrimmedSurface surface =
                requireEntity(id, StepRectangularTrimmedSurface.class, "RECTANGULAR_TRIMMED_SURFACE");
        buildSupportedSurfaceGeometry(surface.basisSurface());
    }

    /**
     * Validates a CURVE_BOUNDED_SURFACE definition.
     *
     * @param id STEP entity id
     */
    public void buildCurveBoundedSurface(int id) {
        StepCurveBoundedSurface surface =
                requireEntity(id, StepCurveBoundedSurface.class, "CURVE_BOUNDED_SURFACE");
        for (StepEntity boundary : surface.boundaries()) {
            buildSurfaceBoundaryCurve(boundary);
        }
        buildSupportedSurfaceGeometry(surface.basisSurface());
    }

    /**
     * Validates an ORIENTED_SURFACE definition.
     *
     * @param id STEP entity id
     */
    public void buildOrientedSurface(int id) {
        StepOrientedSurface surface = requireEntity(id, StepOrientedSurface.class, "ORIENTED_SURFACE");
        buildSupportedSurfaceGeometry(surface.surfaceElement());
    }

    /**
     * Validates an OFFSET_SURFACE definition.
     *
     * @param id STEP entity id
     */
    public void buildOffsetSurface(int id) {
        StepOffsetSurface surface = requireEntity(id, StepOffsetSurface.class, "OFFSET_SURFACE");
        buildSupportedSurfaceGeometry(surface.basisSurface());
    }

    /**
     * Validates a SURFACE_REPLICA definition.
     *
     * @param id STEP entity id
     */
    public void buildSurfaceReplica(int id) {
        StepGeometricReplica replica = requireEntity(id, StepGeometricReplica.class, "SURFACE_REPLICA");
        if (!"SURFACE_REPLICA".equals(replica.entityName())) {
            throw new StepResolutionException("entity #" + id + " is not a SURFACE_REPLICA");
        }
        String restriction = unsupportedReplicaSurfaceTransformation(replica.transformation());
        if (restriction != null) {
            throw new UnsupportedGeometryException(restriction + " is unsupported");
        }
        buildSupportedSurfaceGeometry(replica.parent());
    }

    /**
     * Builds a B-spline curve geometry object.
     *
     * @param id STEP entity id
     * @return built B-spline curve
     */
    public BSplineCurve3 buildBSplineCurve(int id) {
        BSplineCurve3 existing = bsplineCurves.get(id);
        if (existing != null) {
            return existing;
        }
        StepBSplineCurveWithKnots spline = requireEntity(id, StepBSplineCurveWithKnots.class, "B_SPLINE_CURVE_WITH_KNOTS");
        List<CartesianPoint> controlPoints = spline.controlPoints().stream().map(point -> buildPoint(point.id())).toList();
        BSplineCurve3 built = new BSplineCurve3(spline.degree(), controlPoints, spline.knotMultiplicities(), spline.knots());
        bsplineCurves.put(id, built);
        return built;
    }

    /**
     * Builds a B-spline surface geometry object.
     *
     * @param id STEP entity id
     * @return built B-spline surface
     */
    public BSplineSurface3 buildBSplineSurface(int id) {
        BSplineSurface3 existing = bsplineSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepBSplineSurfaceWithKnots surface = requireEntity(id, StepBSplineSurfaceWithKnots.class, "B_SPLINE_SURFACE_WITH_KNOTS");
        List<List<CartesianPoint>> controlPoints = surface.controlPoints().stream()
                .map(row -> row.stream().map(point -> buildPoint(point.id())).toList())
                .toList();
        BSplineSurface3 built = new BSplineSurface3(
                surface.uDegree(),
                surface.vDegree(),
                controlPoints,
                surface.uMultiplicities(),
                surface.vMultiplicities(),
                surface.uKnots(),
                surface.vKnots()
        );
        bsplineSurfaces.put(id, built);
        return built;
    }

    public BSplineSurface3 buildBezierSurface(int id) {
        return buildImplicitBSplineSurface(requireEntity(id, StepBezierSurface.class, "BEZIER_SURFACE"));
    }

    public BSplineSurface3 buildUniformSurface(int id) {
        return buildImplicitBSplineSurface(requireEntity(id, StepUniformSurface.class, "UNIFORM_SURFACE"));
    }

    public BSplineSurface3 buildQuasiUniformSurface(int id) {
        return buildImplicitBSplineSurface(requireEntity(id, StepQuasiUniformSurface.class, "QUASI_UNIFORM_SURFACE"));
    }

    public BSplineSurface3 buildPiecewiseBezierSurface(int id) {
        return buildImplicitBSplineSurface(requireEntity(id, StepPiecewiseBezierSurface.class, "PIECEWISE_BEZIER_SURFACE"));
    }

    private BSplineSurface3 buildImplicitBSplineSurface(StepEntity entity) {
        BSplineSurface3 existing = bsplineSurfaces.get(entity.id());
        if (existing != null) {
            return existing;
        }
        ImplicitBSplineSurfaceData surface = implicitBSplineSurfaceData(entity);
        List<List<CartesianPoint>> controlPoints = surface.controlPoints().stream()
                .map(row -> row.stream().map(point -> buildPoint(point.id())).toList())
                .toList();
        BSplineSurface3 built = new BSplineSurface3(
                surface.uDegree(),
                surface.vDegree(),
                controlPoints,
                surface.uMultiplicities(),
                surface.vMultiplicities(),
                surface.uKnots(),
                surface.vKnots()
        );
        bsplineSurfaces.put(entity.id(), built);
        return built;
    }

    public RationalBSplineCurve3 buildRationalBSplineCurve3(int id) {
        RationalBSplineCurve3 existing = rationalBsplineCurves.get(id);
        if (existing != null) {
            return existing;
        }
        StepRationalBSplineCurve spline = requireEntity(id, StepRationalBSplineCurve.class, "RATIONAL_B_SPLINE_CURVE");
        if (spline.weightsData().isEmpty()) {
            throw new UnsupportedGeometryException("RATIONAL_B_SPLINE_CURVE requires weights");
        }
        List<CartesianPoint> controlPoints = spline.controlPoints().stream().map(point -> buildPoint(point.id())).toList();
        RationalBSplineCurve3 built = new RationalBSplineCurve3(
                spline.degree(),
                controlPoints,
                spline.weightsData(),
                spline.knotMultiplicities(),
                spline.knots()
        );
        rationalBsplineCurves.put(id, built);
        return built;
    }

    public RationalBSplineCurve3 buildRationalBSplineCurve(int id) {
        return buildRationalBSplineCurve3(id);
    }

    public RationalBSplineCurve2 buildRationalBSplineCurve2(int id) {
        RationalBSplineCurve2 existing = rationalSplineCurves2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepRationalBSplineCurve spline = requireEntity(id, StepRationalBSplineCurve.class, "RATIONAL_B_SPLINE_CURVE");
        if (spline.weightsData().isEmpty()) {
            throw new UnsupportedGeometryException("RATIONAL_B_SPLINE_CURVE requires weights");
        }
        List<Point2> controlPoints = new ArrayList<>(spline.controlPoints().size());
        for (StepCartesianPoint point : spline.controlPoints()) {
            if (point.coordinates().size() != 2) {
                throw new UnsupportedGeometryException("RATIONAL_B_SPLINE_CURVE is not a 2D spline");
            }
            controlPoints.add(buildPoint2(point.id()));
        }
        RationalBSplineCurve2 built = new RationalBSplineCurve2(
                spline.degree(),
                controlPoints,
                spline.weightsData(),
                spline.knotMultiplicities(),
                spline.knots()
        );
        rationalSplineCurves2d.put(id, built);
        return built;
    }

    public RationalBSplineSurface3 buildRationalBSplineSurface(int id) {
        RationalBSplineSurface3 existing = rationalBsplineSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepRationalBSplineSurface surface =
                requireEntity(id, StepRationalBSplineSurface.class, "RATIONAL_B_SPLINE_SURFACE");
        List<List<CartesianPoint>> controlPoints = surface.controlPoints().stream()
                .map(row -> row.stream().map(point -> buildPoint(point.id())).toList())
                .toList();
        RationalBSplineSurface3 built = new RationalBSplineSurface3(
                surface.uDegree(),
                surface.vDegree(),
                controlPoints,
                surface.weightsData(),
                surface.uMultiplicities(),
                surface.vMultiplicities(),
                surface.uKnots(),
                surface.vKnots()
        );
        rationalBsplineSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a trimmed curve backed by a supported basis curve.
     *
     * @param id STEP entity id
     * @return built trimmed curve
     */
    public TrimmedCurve3 buildTrimmedCurve(int id) {
        TrimmedCurve3 existing = trimmedCurves.get(id);
        if (existing != null) {
            return existing;
        }
        StepTrimmedCurve trimmedCurve = requireEntity(id, StepTrimmedCurve.class, "TRIMMED_CURVE");
        Curve3 basis = switch (trimmedCurve.basisCurve()) {
            case StepConicCurve conic ->
                    throw new UnsupportedGeometryException("TRIMMED_CURVE basis curve for " + conic.entityName() + " is unsupported");
            default -> buildCurve3(trimmedCurve.basisCurve());
        };
        for (StepEntity trim : trimmedCurve.trim1()) {
            validateTrimPoint(trim, basis, "trim_1");
        }
        for (StepEntity trim : trimmedCurve.trim2()) {
            validateTrimPoint(trim, basis, "trim_2");
        }
        CartesianPoint trimStart = requireTrimPoint3(trimmedCurve.trim1(), "trim_1");
        CartesianPoint trimEnd = requireTrimPoint3(trimmedCurve.trim2(), "trim_2");
        TrimmedCurve3 built = new TrimmedCurve3(basis, trimStart, trimEnd, trimmedCurve.senseAgreement());
        trimmedCurves.put(id, built);
        return built;
    }

    /**
     * Builds a surface curve backed by a supported 3D curve.
     *
     * @param id STEP entity id
     * @return built surface curve
     */
    public SurfaceCurve3 buildSurfaceCurve(int id) {
        SurfaceCurve3 existing = surfaceCurves.get(id);
        if (existing != null) {
            return existing;
        }
        StepSurfaceCurve surfaceCurve = requireEntity(id, StepSurfaceCurve.class, "SURFACE_CURVE");
        Curve3 curve3d = buildCurve3(surfaceCurve.curve3d());
        SurfaceCurve3 built = new SurfaceCurve3(curve3d);
        surfaceCurves.put(id, built);
        return built;
    }

    public SurfaceCurve3 buildSeamCurve(int id) {
        StepSeamCurve seamCurve = requireEntity(id, StepSeamCurve.class, "SEAM_CURVE");
        Curve3 curve3d = buildCurve3(seamCurve.curve3d());
        return new SurfaceCurve3(curve3d);
    }

    /**
     * Validates a supported 3D curve reference entity.
     *
     * @param id STEP entity id
     * @return built curve geometry
     */
    public Curve3 buildCurveReference3(int id) {
        StepEntity entity = requireEntity(id, StepEntity.class, "3D curve reference");
        return buildCurve3(entity);
    }

    /**
     * Builds a parabola geometry object.
     *
     * @param id STEP entity id
     * @return built parabola
     */
    public Parabola3 buildParabola(int id) {
        Parabola3 existing = parabolas.get(id);
        if (existing != null) {
            return existing;
        }
        StepConicCurve conic = requireEntity(id, StepConicCurve.class, "PARABOLA");
        if (!"PARABOLA".equals(conic.entityName())) {
            throw new StepResolutionException("entity #" + id + " is not a PARABOLA");
        }
        if (!(conic.position() instanceof StepAxis2Placement3D placement3d)) {
            throw new StepResolutionException("entity #" + id + " is not a 3D PARABOLA");
        }
        if (conic.parameters().isEmpty()) {
            throw new UnsupportedGeometryException("PARABOLA requires focal distance");
        }
        double focalDistance = conic.parameters().getFirst();
        if (!Double.isFinite(focalDistance) || focalDistance <= Epsilon.EPS) {
            throw new UnsupportedGeometryException("PARABOLA focal distance must be positive");
        }
        Parabola3 built = new Parabola3(buildPlacement(placement3d.id()), focalDistance);
        parabolas.put(id, built);
        return built;
    }

    /**
     * Builds a hyperbola geometry object.
     *
     * @param id STEP entity id
     * @return built hyperbola
     */
    public Hyperbola3 buildHyperbola(int id) {
        Hyperbola3 existing = hyperbolas.get(id);
        if (existing != null) {
            return existing;
        }
        StepConicCurve conic = requireEntity(id, StepConicCurve.class, "HYPERBOLA");
        if (!"HYPERBOLA".equals(conic.entityName())) {
            throw new StepResolutionException("entity #" + id + " is not a HYPERBOLA");
        }
        if (!(conic.position() instanceof StepAxis2Placement3D placement3d)) {
            throw new StepResolutionException("entity #" + id + " is not a 3D HYPERBOLA");
        }
        if (conic.parameters().size() < 2) {
            throw new UnsupportedGeometryException("HYPERBOLA requires semi-axis and semi-imaginary-axis");
        }
        double semiAxisA = conic.parameters().get(0);
        double semiAxisB = conic.parameters().get(1);
        if (!Double.isFinite(semiAxisA) || !Double.isFinite(semiAxisB)
                || semiAxisA <= Epsilon.EPS || semiAxisB <= Epsilon.EPS) {
            throw new UnsupportedGeometryException("HYPERBOLA axes must be positive");
        }
        Hyperbola3 built = new Hyperbola3(buildPlacement(placement3d.id()), semiAxisA, semiAxisB);
        hyperbolas.put(id, built);
        return built;
    }

    /**
     * Builds a clothoid geometry object.
     *
     * @param id STEP entity id
     * @return built clothoid
     */
    public Clothoid3 buildClothoid(int id) {
        Clothoid3 existing = clothoids.get(id);
        if (existing != null) {
            return existing;
        }
        StepClothoid clothoid = requireEntity(id, StepClothoid.class, "CLOTHOID");
        // CLOTHOID uses 2D placement, need to convert to 3D
        Axis2Placement3D position = convert2DPlacementTo3D(clothoid.position());
        double xAxisIntercept = clothoid.xAxisIntercept();
        double curvature = clothoid.curvature();
        if (!Double.isFinite(xAxisIntercept) || !Double.isFinite(curvature)) {
            throw new UnsupportedGeometryException("CLOTHOID requires finite xAxisIntercept and curvature");
        }
        if (Math.abs(curvature) < Epsilon.EPS) {
            throw new UnsupportedGeometryException("CLOTHOID curvature must be non-zero");
        }
        Clothoid3 built = new Clothoid3(position, xAxisIntercept, curvature);
        clothoids.put(id, built);
        return built;
    }

    /**
     * Converts a 2D placement to a 3D placement in the XY plane (Z=0).
     */
    private Axis2Placement3D convert2DPlacementTo3D(StepEntity position) {
        if (position instanceof StepAxis2Placement3D placement3D) {
            return buildPlacement(placement3D.id());
        }
        if (!(position instanceof StepAxis2Placement2D placement2D)) {
            throw new StepResolutionException("position must be AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
        }
        Point2 origin = buildPoint2(placement2D.location().id());
        Direction2 refDir = buildDirection2(placement2D.refDirection().id());
        // Create 3D placement: location at (x, y, 0), Z axis as normal, X direction from 2D
        CartesianPoint location3D = new CartesianPoint(origin.x(), origin.y(), 0.0);
        Direction3 axis = new Direction3(0, 0, 1);
        Direction3 xDirection = new Direction3(refDir.x(), refDir.y(), 0);
        return new Axis2Placement3D(location3D, axis, xDirection);
    }

    /**
     * Builds a topological vertex.
     *
     * @param id STEP entity id
     * @return built vertex
     */
    public Vertex buildVertex(int id) {
        Vertex existing = vertices.get(id);
        if (existing != null) {
            return existing;
        }
        StepVertexPoint vertexPoint = requireEntity(id, StepVertexPoint.class, "VERTEX_POINT");
        Vertex built = new Vertex(buildPoint(vertexPoint.point().id()));
        vertices.put(id, built);
        return built;
    }

    /**
     * Builds a topological edge backed by a supported curve.
     *
     * @param id STEP entity id
     * @return built edge
     */
    public Edge buildEdge(int id) {
        Edge existing = edges.get(id);
        if (existing != null) {
            return existing;
        }
        StepEntity entity = requireExistingEntity(id);
        Edge built;
        if (entity instanceof StepEdgeCurve edgeCurve) {
            Curve3 curve = buildCurve3(edgeCurve.edgeGeometry());
            built = new Edge(
                    buildVertex(edgeCurve.start().id()),
                    buildVertex(edgeCurve.end().id()),
                    curve,
                    edgeCurve.sameSense()
            );
        } else if (entity instanceof StepSubedge subedge) {
            Edge parent = buildEdge(subedge.parentEdge().id());
            built = new Edge(
                    buildVertex(subedge.start().id()),
                    buildVertex(subedge.end().id()),
                    parent.curve(),
                    parent.sameSense()
            );
        } else {
            throw new StepResolutionException("entity #" + id + " is not a EDGE_CURVE or SUBEDGE");
        }
        edges.put(id, built);
        return built;
    }

    /**
     * Builds an oriented edge.
     *
     * @param id STEP entity id
     * @return built oriented edge
     */
    public OrientedEdge buildOrientedEdge(int id) {
        OrientedEdge existing = orientedEdges.get(id);
        if (existing != null) {
            return existing;
        }
        StepOrientedEdge stepOrientedEdge = requireEntity(id, StepOrientedEdge.class, "ORIENTED_EDGE");
        OrientedEdge built = new OrientedEdge(buildEdge(stepOrientedEdge.edgeElement().id()), stepOrientedEdge.orientation());
        orientedEdges.put(id, built);
        return built;
    }

    /**
     * Builds an edge loop.
     *
     * @param id STEP entity id
     * @return built edge loop
     */
    public EdgeLoop buildEdgeLoop(int id) {
        EdgeLoop existing = loops.get(id);
        if (existing != null) {
            return existing;
        }
        StepEdgeLoop loop = requireEntity(id, StepEdgeLoop.class, "EDGE_LOOP");
        EdgeLoop built = new EdgeLoop(loop.edges().stream().map(edge -> buildOrientedEdge(edge.id())).toList());
        loops.put(id, built);
        return built;
    }

    /**
     * Builds a vertex loop.
     *
     * @param id STEP entity id
     * @return built vertex loop
     */
    public VertexLoop buildVertexLoop(int id) {
        VertexLoop existing = vertexLoops.get(id);
        if (existing != null) {
            return existing;
        }
        StepVertexLoop loop = requireEntity(id, StepVertexLoop.class, "VERTEX_LOOP");
        VertexLoop built = new VertexLoop(buildVertex(loop.loopVertex().id()));
        vertexLoops.put(id, built);
        return built;
    }

    /**
     * Builds a face bound.
     *
     * @param id STEP entity id
     * @return built face bound
     */
    public FaceBound buildFaceBound(int id) {
        FaceBound existing = faceBounds.get(id);
        if (existing != null) {
            return existing;
        }
        StepFaceBound stepFaceBound = requireEntity(id, StepFaceBound.class, "FACE_BOUND");
        Loop builtLoop = switch (stepFaceBound.loop()) {
            case StepEdgeLoop edgeLoop -> buildEdgeLoop(edgeLoop.id());
            case StepVertexLoop vertexLoop -> buildVertexLoop(vertexLoop.id());
            case com.minicad.step.model.StepPolyLoop polyLoop ->
                    new PolyLoop(polyLoop.polygon().stream().map(point -> buildPoint(point.id())).toList());
            default -> throw new UnsupportedGeometryException(
                    "FACE_BOUND construction requires EDGE_LOOP or VERTEX_LOOP");
        };
        FaceBound built = stepFaceBound.outer()
                ? FaceBound.outer(builtLoop, stepFaceBound.orientation())
                : FaceBound.inner(builtLoop, stepFaceBound.orientation());
        faceBounds.put(id, built);
        return built;
    }

    /**
     * Builds a planar face.
     *
     * @param id STEP entity id
     * @return built face
     */
    public Face buildFace(int id) {
        Face existing = faces.get(id);
        if (existing != null) {
            return existing;
        }
        StepEntity entity = requireExistingEntity(id);
        Face built;
        if (entity instanceof StepOrientedFace orientedFace) {
            Face baseFace = buildFace(orientedFace.faceElement().id());
            built = new Face(
                    baseFace.surface(),
                    baseFace.bounds(),
                    orientedFace.orientation() ? baseFace.sameSense() : !baseFace.sameSense()
            );
        } else if (entity instanceof StepAdvancedFace advancedFace) {
            built = buildFaceSurface(advancedFace, "ADVANCED_FACE");
        } else if (entity instanceof StepFaceSurface faceSurface) {
            built = buildFaceSurface(faceSurface, "FACE_SURFACE");
        } else {
            throw new StepResolutionException("entity #" + id + " is not a FACE");
        }
        faces.put(id, built);
        return built;
    }

    private Face buildFaceSurface(StepFaceEntity stepFace, String faceType) {
        SurfaceGeometry supportedSurface = buildSupportedFaceGeometry(faceGeometry(stepFace), faceType);
        if (supportedSurface == null) {
            StepEntity geometry = faceGeometry(stepFace);
            String unsupportedSurfaceType = describeUnsupportedFaceGeometry(geometry);
            if (unsupportedSurfaceType != null) {
                throw new UnsupportedGeometryException(faceType + " construction for " + unsupportedSurfaceType + " is unsupported");
            }
            throw new UnsupportedGeometryException(faceType + " construction requires PLANE geometry");
        }
        List<FaceBound> bounds = stepFace.bounds().stream().map(bound -> buildFaceBound(bound.id())).toList();
        if (bounds.stream().noneMatch(FaceBound::outer) && bounds.size() == 1) {
            FaceBound bound = bounds.getFirst();
            bounds = List.of(FaceBound.outer(bound.loop(), bound.orientation()));
        }
        return new Face(supportedSurface, bounds, faceSameSense(stepFace));
    }

    /**
     * Builds a shell from OPEN_SHELL, SURFACED_OPEN_SHELL, ORIENTED_OPEN_SHELL, CLOSED_SHELL or ORIENTED_CLOSED_SHELL.
     *
     * @param id STEP entity id
     * @return built shell
     */
    public Shell buildShell(int id) {
        Shell existing = shells.get(id);
        if (existing != null) {
            return existing;
        }
        StepEntity entity = requireExistingEntity(id);
        Shell built;
        if (entity instanceof StepOpenShell openShell) {
            built = new Shell(openShell.faces().stream().map(face -> buildFace(face.id())).toList(), false);
        } else if (entity instanceof StepSurfacedOpenShell surfacedOpenShell) {
            built = new Shell(surfacedOpenShell.faces().stream().map(face -> buildFace(face.id())).toList(), false);
        } else if (entity instanceof StepOrientedOpenShell orientedOpenShell) {
            built = new Shell(orientedOpenShell.faces().stream().map(face -> buildFace(face.id())).toList(), false);
        } else if (entity instanceof StepClosedShell closedShell) {
            built = new Shell(closedShell.faces().stream().map(face -> buildFace(face.id())).toList(), true);
        } else if (entity instanceof StepOrientedClosedShell orientedClosedShell) {
            built = new Shell(orientedClosedShell.faces().stream().map(face -> buildFace(face.id())).toList(), true);
        } else {
            throw new StepResolutionException("entity #" + id + " is not an OPEN_SHELL, SURFACED_OPEN_SHELL, ORIENTED_OPEN_SHELL, CLOSED_SHELL or ORIENTED_CLOSED_SHELL");
        }
        shells.put(id, built);
        return built;
    }

    /**
     * Builds a solid from MANIFOLD_SOLID_BREP.
     *
     * @param id STEP entity id
     * @return built solid
     */
    public Solid buildSolid(int id) {
        Solid existing = solids.get(id);
        if (existing != null) {
            return existing;
        }
        StepEntity entity = requireExistingEntity(id);
        Solid built;
        if (entity instanceof StepManifoldSolidBrep solidBrep) {
            built = new Solid(buildShell(solidBrep.outer().id()));
        } else if (entity instanceof StepBrepWithVoids brepWithVoids) {
            Shell outerShell = buildShell(brepWithVoids.outer().id());
            List<Shell> voidShells = brepWithVoids.voids().stream().map(voidShell -> buildShell(voidShell.id())).toList();
            built = new Solid(outerShell, voidShells);
        } else if (entity instanceof StepCsgPrimitive csgPrimitive) {
            built = buildCsgPrimitive(csgPrimitive);
        } else if (entity instanceof StepCsgSolid csgSolid) {
            built = buildBooleanOperandSolid(csgSolid.treeRootExpression());
        } else if (entity instanceof StepSolidReplica solidReplica) {
            built = transformSolid(buildSolid(solidReplica.parentSolid().id()), solidReplica.transformation());
        } else if (entity instanceof StepSweptAreaSolid sweptAreaSolid) {
            built = buildSweptAreaSolid(sweptAreaSolid);
        } else if (entity instanceof StepSweptDiskSolid sweptDiskSolid) {
            built = buildSweptDiskSolid(sweptDiskSolid);
        } else if (entity instanceof StepExtrudedAreaSolidTapered taperedExtrusion) {
            built = buildExtrudedAreaSolidTapered(taperedExtrusion);
        } else if (entity instanceof StepRevolvedAreaSolidTapered taperedRevolution) {
            built = buildRevolvedAreaSolidTapered(taperedRevolution);
        } else if (entity instanceof StepSurfaceCurveSweptAreaSolid surfaceCurveSweep) {
            built = buildSurfaceCurveSweptAreaSolid(surfaceCurveSweep);
        } else if (entity instanceof StepBooleanClippingResult clippingResult) {
            built = buildBooleanResult(clippingResult.operator(), clippingResult.firstOperand(), clippingResult.secondOperand());
        } else if (entity instanceof StepBooleanResult booleanResult) {
            built = buildBooleanResult(booleanResult.operator(), booleanResult.firstOperand(), booleanResult.secondOperand());
        } else if (entity instanceof StepNonManifoldSolidBrep nonManifoldBrep) {
            // Non-manifold solid brep has an open shell boundary
            // This is used for sheet bodies (e.g., unfolded sheet metal)
            // Build the shell - it may be an open shell which is valid for non-manifold
            Shell outerShell = buildShell(nonManifoldBrep.outer().id());
            // Non-manifold solids have open shells as boundaries
            built = new Solid(outerShell);
        } else {
            throw new StepResolutionException("entity #" + id + " is not a supported SOLID");
        }
        solids.put(id, built);
        return built;
    }

    private StepEntity requireExistingEntity(int id) {
        StepEntity entity = entitiesById.get(id);
        if (entity == null) {
            throw new StepResolutionException("missing resolved entity #" + id);
        }
        return entity;
    }

    private <T extends StepEntity> T requireEntity(int id, Class<T> type, String expectedName) {
        StepEntity entity = requireExistingEntity(id);
        if (!type.isInstance(entity)) {
            throw new StepResolutionException("entity #" + id + " is not a " + expectedName);
        }
        return type.cast(entity);
    }

    private static StepEntity faceGeometry(StepFaceEntity stepFace) {
        if (stepFace instanceof StepAdvancedFace advancedFace) {
            return advancedFace.faceGeometry();
        }
        if (stepFace instanceof StepFaceSurface faceSurface) {
            return faceSurface.faceGeometry();
        }
        if (stepFace instanceof StepOrientedFace orientedFace) {
            return faceGeometry(orientedFace.faceElement());
        }
        throw new UnsupportedGeometryException("unsupported face subtype");
    }

    private static boolean faceSameSense(StepFaceEntity stepFace) {
        if (stepFace instanceof StepAdvancedFace advancedFace) {
            return advancedFace.sameSense();
        }
        if (stepFace instanceof StepFaceSurface faceSurface) {
            return faceSurface.sameSense();
        }
        if (stepFace instanceof StepOrientedFace orientedFace) {
            boolean base = faceSameSense(orientedFace.faceElement());
            return orientedFace.orientation() ? base : !base;
        }
        throw new UnsupportedGeometryException("unsupported face subtype");
    }

    private Solid buildSweptAreaSolid(StepSweptAreaSolid sweptAreaSolid) {
        if ("EXTRUDED_AREA_SOLID".equals(sweptAreaSolid.entityName())) {
            return buildExtrudedAreaSolid(sweptAreaSolid);
        }
        if ("REVOLVED_AREA_SOLID".equals(sweptAreaSolid.entityName())) {
            return buildRevolvedAreaSolid(sweptAreaSolid);
        }
        throw new UnsupportedGeometryException(sweptAreaSolid.entityName() + " construction is unsupported");
    }

    private Solid buildCsgPrimitive(StepCsgPrimitive csgPrimitive) {
        return switch (csgPrimitive.entityName()) {
            case "BLOCK" -> buildBlockPrimitive(csgPrimitive);
            case "SPHERE" -> buildSpherePrimitive(csgPrimitive);
            case "ELLIPSOID" -> buildEllipsoidPrimitive(csgPrimitive);
            case "RIGHT_CIRCULAR_CYLINDER" -> buildRightCircularCylinderPrimitive(csgPrimitive);
            case "TORUS" -> buildTorusPrimitive(csgPrimitive);
            case "RIGHT_ANGULAR_WEDGE" -> buildRightAngularWedgePrimitive(csgPrimitive);
            case "RIGHT_CIRCULAR_CONE" -> buildRightCircularConePrimitive(csgPrimitive);
            default -> throw new UnsupportedGeometryException(csgPrimitive.entityName() + " construction is unsupported");
        };
    }

    private Solid buildBlockPrimitive(StepCsgPrimitive csgPrimitive) {
        if (!(csgPrimitive.position() instanceof StepAxis2Placement3D placement)) {
            throw new UnsupportedGeometryException("BLOCK position must be an AXIS2_PLACEMENT_3D");
        }
        if (csgPrimitive.dimensions().size() < 3) {
            throw new UnsupportedGeometryException("BLOCK requires x, y and z dimensions");
        }
        Axis2Placement3D blockPlacement = buildPlacement(placement.id());
        double x = csgPrimitive.dimensions().get(0);
        double y = csgPrimitive.dimensions().get(1);
        double z = csgPrimitive.dimensions().get(2);
        if (x <= 0.0 || y <= 0.0 || z <= 0.0) {
            throw new UnsupportedGeometryException("BLOCK dimensions must be positive");
        }
        CartesianPoint origin = blockPlacement.location();
        Vector3 alongX = blockPlacement.xDirection().asVector().scale(x);
        Vector3 alongY = blockPlacement.yDirection().asVector().scale(y);
        Vector3 alongZ = blockPlacement.axis().asVector().scale(z);
        List<CartesianPoint> bottom = List.of(
                origin,
                origin.add(alongX),
                origin.add(alongX.add(alongY)),
                origin.add(alongY)
        );
        List<CartesianPoint> top = bottom.stream().map(point -> point.add(alongZ)).toList();

        List<Face> faces = new ArrayList<>();
        faces.add(faceFromPolyLoop(reverseClosedLoop3(bottom), blockPlacement.axis().reverse()));
        faces.add(faceFromPolyLoop(closeLoop3(top), blockPlacement.axis()));
        for (int index = 0; index < bottom.size(); index++) {
            CartesianPoint startBottom = bottom.get(index);
            CartesianPoint endBottom = bottom.get((index + 1) % bottom.size());
            CartesianPoint endTop = top.get((index + 1) % top.size());
            CartesianPoint startTop = top.get(index);
            faces.add(faceFromPolyLoop(
                    List.of(startBottom, endBottom, endTop, startTop, startBottom),
                    quadNormal(startBottom, endBottom, endTop, startTop)
            ));
        }
        return new Solid(new Shell(faces, true));
    }

    private Solid buildSpherePrimitive(StepCsgPrimitive csgPrimitive) {
        if (!(csgPrimitive.position() instanceof StepAxis2Placement3D placement)) {
            throw new UnsupportedGeometryException("SPHERE position must be an AXIS2_PLACEMENT_3D");
        }
        if (csgPrimitive.dimensions().isEmpty()) {
            throw new UnsupportedGeometryException("SPHERE requires a radius");
        }
        double radius = csgPrimitive.dimensions().getFirst();
        if (radius <= 0.0) {
            throw new UnsupportedGeometryException("SPHERE radius must be positive");
        }
        return buildEllipsoidLike(buildPlacement(placement.id()), radius, radius, radius, 24, 12);
    }

    private Solid buildEllipsoidPrimitive(StepCsgPrimitive csgPrimitive) {
        if (!(csgPrimitive.position() instanceof StepAxis2Placement3D placement)) {
            throw new UnsupportedGeometryException("ELLIPSOID position must be an AXIS2_PLACEMENT_3D");
        }
        if (csgPrimitive.dimensions().size() < 3) {
            throw new UnsupportedGeometryException("ELLIPSOID requires three semi axes");
        }
        double rx = csgPrimitive.dimensions().get(0);
        double ry = csgPrimitive.dimensions().get(1);
        double rz = csgPrimitive.dimensions().get(2);
        if (rx <= 0.0 || ry <= 0.0 || rz <= 0.0) {
            throw new UnsupportedGeometryException("ELLIPSOID semi axes must be positive");
        }
        return buildEllipsoidLike(buildPlacement(placement.id()), rx, ry, rz, 24, 12);
    }

    private Solid buildRightCircularCylinderPrimitive(StepCsgPrimitive csgPrimitive) {
        if (!(csgPrimitive.position() instanceof StepAxis1Placement placement)) {
            throw new UnsupportedGeometryException("RIGHT_CIRCULAR_CYLINDER position must be an AXIS1_PLACEMENT");
        }
        if (csgPrimitive.dimensions().size() < 2) {
            throw new UnsupportedGeometryException("RIGHT_CIRCULAR_CYLINDER requires height and radius");
        }
        double height = csgPrimitive.dimensions().get(0);
        double radius = csgPrimitive.dimensions().get(1);
        if (height <= 0.0 || radius <= 0.0) {
            throw new UnsupportedGeometryException("RIGHT_CIRCULAR_CYLINDER dimensions must be positive");
        }
        Axis1Placement axis = buildAxis1Placement(placement.id());
        CircularFrame frame = circularFrame(axis.axis());
        Vector3 alongAxis = axis.axis().asVector().scale(height);
        List<CartesianPoint> bottom = sampleCircle3(axis.location(), frame.x(), frame.y(), radius, 48);
        List<CartesianPoint> top = bottom.stream().map(point -> point.add(alongAxis)).toList();
        List<Face> faces = new ArrayList<>();
        faces.add(faceFromPolyLoop(reverseClosedLoop3(bottom), axis.axis().reverse()));
        faces.add(faceFromPolyLoop(closeLoop3(top), axis.axis()));
        for (int index = 0; index < bottom.size(); index++) {
            CartesianPoint a = bottom.get(index);
            CartesianPoint b = bottom.get((index + 1) % bottom.size());
            CartesianPoint c = top.get((index + 1) % top.size());
            CartesianPoint d = top.get(index);
            faces.add(faceFromPolyLoop(List.of(a, b, c, d, a), quadNormal(a, b, c, d)));
        }
        return new Solid(new Shell(faces, true));
    }

    private Solid buildTorusPrimitive(StepCsgPrimitive csgPrimitive) {
        if (!(csgPrimitive.position() instanceof StepAxis1Placement placement)) {
            throw new UnsupportedGeometryException("TORUS position must be an AXIS1_PLACEMENT");
        }
        if (csgPrimitive.dimensions().size() < 2) {
            throw new UnsupportedGeometryException("TORUS requires major and minor radii");
        }
        double majorRadius = csgPrimitive.dimensions().get(0);
        double minorRadius = csgPrimitive.dimensions().get(1);
        if (majorRadius <= 0.0 || minorRadius <= 0.0 || majorRadius <= minorRadius) {
            throw new UnsupportedGeometryException("TORUS radii must satisfy major > minor > 0");
        }
        Axis1Placement axis = buildAxis1Placement(placement.id());
        CircularFrame frame = circularFrame(axis.axis());
        int uSegments = 32;
        int vSegments = 16;
        List<List<CartesianPoint>> grid = new ArrayList<>(uSegments);
        for (int uIndex = 0; uIndex < uSegments; uIndex++) {
            double u = Math.PI * 2.0 * uIndex / uSegments;
            Vector3 radial = frame.x().scale(Math.cos(u)).add(frame.y().scale(Math.sin(u)));
            Vector3 tangent = frame.x().scale(-Math.sin(u)).add(frame.y().scale(Math.cos(u)));
            List<CartesianPoint> ring = new ArrayList<>(vSegments);
            for (int vIndex = 0; vIndex < vSegments; vIndex++) {
                double v = Math.PI * 2.0 * vIndex / vSegments;
                Vector3 offset = radial.scale(majorRadius + Math.cos(v) * minorRadius)
                        .add(axis.axis().asVector().scale(Math.sin(v) * minorRadius));
                ring.add(axis.location().add(offset));
            }
            grid.add(List.copyOf(ring));
        }
        List<Face> faces = new ArrayList<>();
        for (int uIndex = 0; uIndex < uSegments; uIndex++) {
            List<CartesianPoint> current = grid.get(uIndex);
            List<CartesianPoint> next = grid.get((uIndex + 1) % uSegments);
            for (int vIndex = 0; vIndex < vSegments; vIndex++) {
                CartesianPoint a = current.get(vIndex);
                CartesianPoint b = current.get((vIndex + 1) % vSegments);
                CartesianPoint c = next.get((vIndex + 1) % vSegments);
                CartesianPoint d = next.get(vIndex);
                addTriangleFace(faces, a, b, c, outwardApproximation(a, axis.location()));
                addTriangleFace(faces, a, c, d, outwardApproximation(d, axis.location()));
            }
        }
        return new Solid(new Shell(faces, true));
    }

    private Solid buildRightAngularWedgePrimitive(StepCsgPrimitive csgPrimitive) {
        if (!(csgPrimitive.position() instanceof StepAxis2Placement3D placement)) {
            throw new UnsupportedGeometryException("RIGHT_ANGULAR_WEDGE position must be an AXIS2_PLACEMENT_3D");
        }
        if (csgPrimitive.dimensions().size() < 4) {
            throw new UnsupportedGeometryException("RIGHT_ANGULAR_WEDGE requires x, y, z and ltx dimensions");
        }
        double x = csgPrimitive.dimensions().get(0);
        double y = csgPrimitive.dimensions().get(1);
        double z = csgPrimitive.dimensions().get(2);
        double ltx = csgPrimitive.dimensions().get(3);
        if (x <= 0.0 || y <= 0.0 || z <= 0.0 || ltx <= 0.0 || ltx > x) {
            throw new UnsupportedGeometryException("RIGHT_ANGULAR_WEDGE dimensions must satisfy x,y,z,ltx > 0 and ltx <= x");
        }
        Axis2Placement3D wedgePlacement = buildPlacement(placement.id());
        CartesianPoint a = pointOnPlacement(wedgePlacement, 0.0, 0.0, 0.0);
        CartesianPoint b = pointOnPlacement(wedgePlacement, x, 0.0, 0.0);
        CartesianPoint c = pointOnPlacement(wedgePlacement, x, y, 0.0);
        CartesianPoint d = pointOnPlacement(wedgePlacement, 0.0, y, 0.0);
        CartesianPoint e = pointOnPlacement(wedgePlacement, 0.0, 0.0, z);
        CartesianPoint f = pointOnPlacement(wedgePlacement, x, 0.0, z);
        CartesianPoint g = pointOnPlacement(wedgePlacement, ltx, y, z);
        CartesianPoint h = pointOnPlacement(wedgePlacement, 0.0, y, z);
        List<Face> faces = new ArrayList<>();
        faces.add(faceFromPolyLoop(reverseClosedLoop3(List.of(a, b, c, d)), wedgePlacement.axis().reverse()));
        faces.add(faceFromPolyLoop(closeLoop3(List.of(e, f, g, h)), wedgePlacement.axis()));
        faces.add(faceFromPolyLoop(List.of(a, b, f, e, a), quadNormal(a, b, f, e)));
        faces.add(faceFromPolyLoop(List.of(d, h, g, c, d), quadNormal(d, h, g, c)));
        faces.add(faceFromPolyLoop(List.of(a, d, h, e, a), quadNormal(a, d, h, e)));
        addTriangleFace(faces, b, c, g, quadNormal(b, c, g, f).asVector());
        addTriangleFace(faces, b, g, f, quadNormal(b, c, g, f).asVector());
        return new Solid(new Shell(faces, true));
    }

    private Solid buildRightCircularConePrimitive(StepCsgPrimitive csgPrimitive) {
        // Accept either AXIS1_PLACEMENT or AXIS2_PLACEMENT_3D
        Axis1Placement axis;
        if (csgPrimitive.position() instanceof StepAxis1Placement placement) {
            axis = buildAxis1Placement(placement.id());
        } else if (csgPrimitive.position() instanceof StepAxis2Placement3D placement) {
            // Use the z-axis direction from AXIS2_PLACEMENT_3D as the cone axis
            Axis2Placement3D pl = buildPlacement(placement.id());
            axis = new Axis1Placement(pl.location(), pl.axis());
        } else {
            throw new UnsupportedGeometryException("RIGHT_CIRCULAR_CONE position must be an AXIS1_PLACEMENT or AXIS2_PLACEMENT_3D");
        }
        if (csgPrimitive.dimensions().size() < 2) {
            throw new UnsupportedGeometryException("RIGHT_CIRCULAR_CONE requires height and radius");
        }
        double height = csgPrimitive.dimensions().get(0);
        double radius = csgPrimitive.dimensions().get(1);
        if (height <= 0.0 || radius <= 0.0) {
            throw new UnsupportedGeometryException("RIGHT_CIRCULAR_CONE dimensions must be positive");
        }
        CircularFrame frame = circularFrame(axis.axis());
        Vector3 alongAxis = axis.axis().asVector().scale(height);
        CartesianPoint apex = axis.location().add(alongAxis);
        List<CartesianPoint> base = sampleCircle3(axis.location(), frame.x(), frame.y(), radius, 48);
        List<Face> faces = new ArrayList<>();
        // Base face
        faces.add(faceFromPolyLoop(reverseClosedLoop3(base), axis.axis().reverse()));
        // Lateral faces as triangles connecting base to apex
        for (int index = 0; index < base.size(); index++) {
            CartesianPoint a = base.get(index);
            CartesianPoint b = base.get((index + 1) % base.size());
            Vector3 midVector = new Vector3(
                    (a.x() + b.x()) / 2.0 - apex.x(),
                    (a.y() + b.y()) / 2.0 - apex.y(),
                    (a.z() + b.z()) / 2.0 - apex.z()
            );
            Vector3 edgeVector = new Vector3(b.x() - a.x(), b.y() - a.y(), b.z() - a.z());
            Vector3 normal = edgeVector.cross(midVector).normalize().asVector();
            addTriangleFace(faces, a, b, apex, normal);
        }
        return new Solid(new Shell(faces, true));
    }

    private Solid buildSweptDiskSolid(StepSweptDiskSolid sweptDiskSolid) {
        Curve3 sweptCurve = buildCurve3(sweptDiskSolid.sweptCurve());
        double radius = sweptDiskSolid.radius();
        Double innerRadius = sweptDiskSolid.innerRadius();
        if (radius <= 0.0) {
            throw new UnsupportedGeometryException("SWEPT_DISK_SOLID radius must be positive");
        }
        boolean isTube = innerRadius != null && innerRadius > 0.0;
        if (isTube && innerRadius >= radius) {
            throw new UnsupportedGeometryException("SWEPT_DISK_SOLID inner radius must be less than outer radius");
        }
        // Sample the swept curve to get positions and tangent directions
        int curveSegments = 48;
        int ringSegments = 24;
        List<Curve3Sample> samples = sampleCurve3WithTangent(sweptCurve, curveSegments);
        List<List<CartesianPoint>> outerRings = new ArrayList<>();
        List<List<CartesianPoint>> innerRings = new ArrayList<>();
        for (Curve3Sample sample : samples) {
            CircularFrame frame = circularFrameAtPoint(sample.point(), sample.tangent());
            List<CartesianPoint> outerRing = sampleCircle3(sample.point(), frame.x(), frame.y(), radius, ringSegments);
            outerRings.add(outerRing);
            if (isTube) {
                List<CartesianPoint> innerRing = sampleCircle3(sample.point(), frame.x(), frame.y(), innerRadius, ringSegments);
                innerRings.add(innerRing);
            }
        }
        List<Face> faces = new ArrayList<>();
        // End caps
        if (!isTube) {
            // Solid disk: cap both ends
            faces.add(faceFromPolyLoop(reverseClosedLoop3(outerRings.getFirst()), samples.getFirst().tangent().reverse()));
            faces.add(faceFromPolyLoop(closeLoop3(outerRings.getLast()), samples.getLast().tangent()));
        } else {
            // Tube: ring-shaped caps
            faces.add(faceFromPolyLoop(reverseClosedLoop3(outerRings.getFirst()), samples.getFirst().tangent().reverse()));
            faces.add(faceFromPolyLoop(closeLoop3(innerRings.getFirst()), samples.getFirst().tangent()));
            faces.add(faceFromPolyLoop(closeLoop3(outerRings.getLast()), samples.getLast().tangent()));
            faces.add(faceFromPolyLoop(reverseClosedLoop3(innerRings.getLast()), samples.getLast().tangent().reverse()));
        }
        // Lateral faces connecting adjacent rings
        for (int ringIndex = 0; ringIndex < outerRings.size() - 1; ringIndex++) {
            List<CartesianPoint> currentOuter = outerRings.get(ringIndex);
            List<CartesianPoint> nextOuter = outerRings.get(ringIndex + 1);
            for (int segIndex = 0; segIndex < ringSegments; segIndex++) {
                CartesianPoint a = currentOuter.get(segIndex);
                CartesianPoint b = currentOuter.get((segIndex + 1) % ringSegments);
                CartesianPoint c = nextOuter.get((segIndex + 1) % ringSegments);
                CartesianPoint d = nextOuter.get(segIndex);
                faces.add(faceFromPolyLoop(List.of(a, b, c, d, a), quadNormal(a, b, c, d)));
            }
            if (isTube) {
                List<CartesianPoint> currentInner = innerRings.get(ringIndex);
                List<CartesianPoint> nextInner = innerRings.get(ringIndex + 1);
                for (int segIndex = 0; segIndex < ringSegments; segIndex++) {
                    CartesianPoint a = currentInner.get(segIndex);
                    CartesianPoint b = nextInner.get(segIndex);
                    CartesianPoint c = nextInner.get((segIndex + 1) % ringSegments);
                    CartesianPoint d = currentInner.get((segIndex + 1) % ringSegments);
                    faces.add(faceFromPolyLoop(List.of(a, b, c, d, a), quadNormal(a, b, c, d)));
                }
            }
        }
        return new Solid(new Shell(faces, true));
    }

    private Solid buildExtrudedAreaSolidTapered(StepExtrudedAreaSolidTapered tapered) {
        // Get the profile from sweptArea (which is StepEntity, need to cast to StepProfileDef)
        StepProfileDef profileDef = asProfileDef(tapered.sweptArea());
        ProfileLoops baseProfile = buildAreaProfileLoops(profileDef);
        Vector3 direction = buildDirection(tapered.direction().id()).asVector();
        double depth = tapered.depth();
        double taperAngle = tapered.taperAngle();
        if (depth <= 0.0) {
            throw new UnsupportedGeometryException("EXTRUDED_AREA_SOLID_TAPERED depth must be positive");
        }
        // Calculate scale factor at top based on taper angle
        // Taper angle is the angle of the taper relative to the extrusion direction
        double topScale = 1.0 - depth * Math.tan(Math.abs(taperAngle));
        if (topScale <= 0.0) {
            throw new UnsupportedGeometryException("EXTRUDED_AREA_SOLID_TAPERED taper angle too large, top profile would be zero or negative");
        }
        // Build scaled top profile
        List<Point2> baseOuter = baseProfile.outer();
        List<Point2> topOuter = scaleProfile(baseOuter, topScale);
        // Build 3D geometry
        List<CartesianPoint> bottom3D = baseOuter.stream()
                .map(p -> new CartesianPoint(p.x(), p.y(), 0.0))
                .toList();
        List<CartesianPoint> top3D = topOuter.stream()
                .map(p -> new CartesianPoint(p.x() * topScale, p.y() * topScale, depth))
                .toList();
        // Close loops
        bottom3D = closeLoop3(bottom3D);
        top3D = closeLoop3(top3D);
        List<Face> faces = new ArrayList<>();
        Direction3 extrusionDir = Direction3.from(direction);
        // Bottom face
        faces.add(faceFromPolyLoop(reverseClosedLoop3(bottom3D), extrusionDir.reverse()));
        // Top face
        faces.add(faceFromPolyLoop(top3D, extrusionDir));
        // Side faces connecting bottom to top
        for (int i = 0; i < baseOuter.size(); i++) {
            CartesianPoint a = bottom3D.get(i);
            CartesianPoint b = bottom3D.get((i + 1) % baseOuter.size());
            CartesianPoint c = top3D.get((i + 1) % top3D.size());
            CartesianPoint d = top3D.get(i);
            faces.add(faceFromPolyLoop(List.of(a, b, c, d, a), quadNormal(a, b, c, d)));
        }
        return new Solid(new Shell(faces, true));
    }

    private Solid buildRevolvedAreaSolidTapered(StepRevolvedAreaSolidTapered tapered) {
        StepProfileDef profileDef = asProfileDef(tapered.sweptArea());
        ProfileLoops baseProfile = buildAreaProfileLoops(profileDef);
        Axis1Placement axis = buildAxis1Placement(tapered.axis().id());
        double angle = tapered.angle();
        double taperAngle = tapered.taperAngle();
        if (angle <= 0.0 || angle > Math.PI * 2.0 + 1e-9) {
            throw new UnsupportedGeometryException("REVOLVED_AREA_SOLID_TAPERED angle must be positive and <= 2*PI");
        }
        int segments = Math.max(12, (int) (angle / (Math.PI / 36.0)));
        // Build rings at different heights with taper scaling
        List<Face> faces = new ArrayList<>();
        List<Point2> profileOuter = baseProfile.outer();
        CircularFrame frame = circularFrame(axis.axis());
        for (int seg = 0; seg < segments; seg++) {
            double currentAngle = angle * seg / segments;
            double nextAngle = angle * (seg + 1) / segments;
            double currentScale = 1.0 - Math.tan(Math.abs(taperAngle)) * (angle * seg / segments / angle);
            double nextScale = 1.0 - Math.tan(Math.abs(taperAngle)) * (angle * (seg + 1) / segments / angle);
            if (currentScale <= 0.0 || nextScale <= 0.0) {
                throw new UnsupportedGeometryException("REVOLVED_AREA_SOLID_TAPERED taper produces zero or negative profile");
            }
            // Build profile at current and next angle
            List<CartesianPoint> currentRing = revolveProfileAtAngle(profileOuter, axis, frame, currentAngle, currentScale);
            List<CartesianPoint> nextRing = revolveProfileAtAngle(profileOuter, axis, frame, nextAngle, nextScale);
            // Connect rings with faces
            for (int i = 0; i < profileOuter.size(); i++) {
                CartesianPoint a = currentRing.get(i);
                CartesianPoint b = currentRing.get((i + 1) % currentRing.size());
                CartesianPoint c = nextRing.get((i + 1) % nextRing.size());
                CartesianPoint d = nextRing.get(i);
                faces.add(faceFromPolyLoop(List.of(a, b, c, d, a), quadNormal(a, b, c, d)));
            }
        }
        // End caps
        List<CartesianPoint> startRing = revolveProfileAtAngle(profileOuter, axis, frame, 0.0, 1.0);
        List<CartesianPoint> endRing = revolveProfileAtAngle(profileOuter, axis, frame, angle, 1.0 - Math.tan(Math.abs(taperAngle)));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(startRing), frame.radialAtAngle(0.0).reverse()));
        faces.add(faceFromPolyLoop(closeLoop3(endRing), frame.radialAtAngle(angle)));
        return new Solid(new Shell(faces, true));
    }

    private Solid buildSurfaceCurveSweptAreaSolid(StepSurfaceCurveSweptAreaSolid swept) {
        StepProfileDef profileDef = asProfileDef(swept.sweptArea());
        ProfileLoops profile = buildAreaProfileLoops(profileDef);
        Curve3 trajectory = buildCurve3(swept.trajectory());
        double startPoint = swept.startPoint();
        double endPoint = swept.endPoint();
        // Sample trajectory curve
        int segments = 48;
        List<Curve3Sample> samples = sampleCurve3WithTangent(trajectory, segments);
        // Adjust to start/end points
        int startIndex = (int) (startPoint * samples.size());
        int endIndex = (int) (endPoint * samples.size());
        if (startIndex < 0) startIndex = 0;
        if (endIndex > samples.size()) endIndex = samples.size();
        List<Curve3Sample> usedSamples = samples.subList(startIndex, endIndex);
        List<Face> faces = new ArrayList<>();
        List<List<CartesianPoint>> rings = new ArrayList<>();
        for (Curve3Sample sample : usedSamples) {
            // Build profile at this trajectory point
            List<CartesianPoint> ring = placeProfileAtPoint(profile.outer(), sample.point(), sample.tangent());
            rings.add(closeLoop3(ring));
        }
        // End caps
        if (!rings.isEmpty()) {
            faces.add(faceFromPolyLoop(reverseClosedLoop3(rings.getFirst()), usedSamples.getFirst().tangent().reverse()));
            faces.add(faceFromPolyLoop(closeLoop3(rings.getLast()), usedSamples.getLast().tangent()));
        }
        // Side faces
        for (int ringIndex = 0; ringIndex < rings.size() - 1; ringIndex++) {
            List<CartesianPoint> current = rings.get(ringIndex);
            List<CartesianPoint> next = rings.get(ringIndex + 1);
            for (int seg = 0; seg < current.size(); seg++) {
                CartesianPoint a = current.get(seg);
                CartesianPoint b = current.get((seg + 1) % current.size());
                CartesianPoint c = next.get((seg + 1) % next.size());
                CartesianPoint d = next.get(seg);
                faces.add(faceFromPolyLoop(List.of(a, b, c, d, a), quadNormal(a, b, c, d)));
            }
        }
        return new Solid(new Shell(faces, true));
    }

    private StepProfileDef asProfileDef(StepEntity entity) {
        if (entity instanceof StepProfileDef profileDef) {
            return profileDef;
        }
        throw new UnsupportedGeometryException("swept area must be a profile definition");
    }

    private List<Point2> scaleProfile(List<Point2> profile, double scale) {
        return profile.stream()
                .map(p -> new Point2(p.x() * scale, p.y() * scale))
                .toList();
    }

    private List<CartesianPoint> revolveProfileAtAngle(List<Point2> profile, Axis1Placement axis, CircularFrame frame, double angle, double scale) {
        Direction3 radial = frame.radialAtAngle(angle);
        return profile.stream()
                .map(p -> axis.location().add(
                        radial.asVector().scale(p.x() * scale)
                                .add(frame.z().asVector().scale(p.y()))))
                .toList();
    }

    private List<CartesianPoint> placeProfileAtPoint(List<Point2> profile, CartesianPoint point, Direction3 tangent) {
        CircularFrame frame = circularFrame(tangent);
        return profile.stream()
                .map(p -> point.add(frame.x().scale(p.x()).add(frame.y().scale(p.y()))))
                .toList();
    }

    private List<Curve3Sample> sampleCurve3WithTangent(Curve3 curve, int segments) {
        List<CartesianPoint> points = sampleCurve3(curve, segments);
        List<Curve3Sample> samples = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            CartesianPoint current = points.get(i);
            CartesianPoint next = points.get((i + 1) % points.size());
            Vector3 tangent = new Vector3(next.x() - current.x(), next.y() - current.y(), next.z() - current.z()).normalize().asVector();
            samples.add(new Curve3Sample(current, Direction3.from(tangent)));
        }
        return samples;
    }

    private record Curve3Sample(CartesianPoint point, Direction3 tangent) {}

    private Solid buildEllipsoidLike(
            Axis2Placement3D placement,
            double rx,
            double ry,
            double rz,
            int uSegments,
            int vSegments
    ) {
        List<Face> faces = new ArrayList<>();
        List<List<CartesianPoint>> rings = new ArrayList<>(vSegments - 1);
        for (int vIndex = 1; vIndex < vSegments; vIndex++) {
            double phi = Math.PI * vIndex / vSegments;
            double sinPhi = Math.sin(phi);
            double cosPhi = Math.cos(phi);
            List<CartesianPoint> ring = new ArrayList<>(uSegments);
            for (int uIndex = 0; uIndex < uSegments; uIndex++) {
                double theta = Math.PI * 2.0 * uIndex / uSegments;
                ring.add(pointOnPlacement(
                        placement,
                        Math.cos(theta) * sinPhi * rx,
                        Math.sin(theta) * sinPhi * ry,
                        cosPhi * rz
                ));
            }
            rings.add(List.copyOf(ring));
        }
        CartesianPoint north = pointOnPlacement(placement, 0.0, 0.0, rz);
        CartesianPoint south = pointOnPlacement(placement, 0.0, 0.0, -rz);
        if (!rings.isEmpty()) {
            List<CartesianPoint> firstRing = rings.getFirst();
            for (int uIndex = 0; uIndex < uSegments; uIndex++) {
                CartesianPoint b = firstRing.get(uIndex);
                CartesianPoint c = firstRing.get((uIndex + 1) % uSegments);
                addTriangleFace(faces, north, b, c, north.subtract(placement.location()));
            }
            for (int ringIndex = 0; ringIndex < rings.size() - 1; ringIndex++) {
                List<CartesianPoint> lower = rings.get(ringIndex);
                List<CartesianPoint> upper = rings.get(ringIndex + 1);
                for (int uIndex = 0; uIndex < uSegments; uIndex++) {
                    CartesianPoint a = lower.get(uIndex);
                    CartesianPoint b = lower.get((uIndex + 1) % uSegments);
                    CartesianPoint c = upper.get((uIndex + 1) % uSegments);
                    CartesianPoint d = upper.get(uIndex);
                    addTriangleFace(faces, a, b, c, outwardApproximation(a, placement.location()));
                    addTriangleFace(faces, a, c, d, outwardApproximation(d, placement.location()));
                }
            }
            List<CartesianPoint> lastRing = rings.getLast();
            for (int uIndex = 0; uIndex < uSegments; uIndex++) {
                CartesianPoint a = lastRing.get(uIndex);
                CartesianPoint b = lastRing.get((uIndex + 1) % uSegments);
                addTriangleFace(faces, a, south, b, south.subtract(placement.location()));
            }
        }
        return new Solid(new Shell(faces, true));
    }

    private List<CartesianPoint> sampleCircle3(
            CartesianPoint center,
            Vector3 xAxis,
            Vector3 yAxis,
            double radius,
            int segments
    ) {
        List<CartesianPoint> points = new ArrayList<>(segments);
        for (int index = 0; index < segments; index++) {
            double angle = Math.PI * 2.0 * index / segments;
            Vector3 offset = xAxis.scale(Math.cos(angle) * radius).add(yAxis.scale(Math.sin(angle) * radius));
            points.add(center.add(offset));
        }
        return List.copyOf(points);
    }

    private CircularFrame circularFrame(Direction3 axis) {
        Vector3 z = axis.asVector();
        Vector3 reference = Math.abs(z.z()) < 0.9 ? new Vector3(0.0, 0.0, 1.0) : new Vector3(1.0, 0.0, 0.0);
        Vector3 x = z.cross(reference);
        if (x.isZero()) {
            reference = new Vector3(0.0, 1.0, 0.0);
            x = z.cross(reference);
        }
        x = x.normalize().asVector();
        Vector3 y = z.cross(x).normalize().asVector();
        return new CircularFrame(x, y);
    }

    private CircularFrame circularFrameAtPoint(CartesianPoint point, Direction3 tangent) {
        return circularFrame(tangent);
    }

    private CartesianPoint pointOnPlacement(Axis2Placement3D placement, double x, double y, double z) {
        return placement.location()
                .add(placement.xDirection().asVector().scale(x))
                .add(placement.yDirection().asVector().scale(y))
                .add(placement.axis().asVector().scale(z));
    }

    private void addTriangleFace(
            List<Face> faces,
            CartesianPoint a,
            CartesianPoint b,
            CartesianPoint c,
            Vector3 fallback
    ) {
        faces.add(faceFromPolyLoop(
                List.of(a, b, c, a),
                polygonNormal(List.of(a, b, c), fallback)
        ));
    }

    private Vector3 outwardApproximation(CartesianPoint point, CartesianPoint center) {
        Vector3 vector = point.subtract(center);
        return vector.isZero() ? new Vector3(0.0, 0.0, 1.0) : vector;
    }

    private Solid buildBooleanResult(String operator, StepEntity first, StepEntity second) {
        String normalizedOperator = operator == null ? "" : operator.replace(".", "").trim().toUpperCase();
        return switch (normalizedOperator) {
            case "DIFFERENCE" -> {
                StepHalfSpaceSolid halfSpaceSolid = asHalfSpaceOperand(second);
                if (halfSpaceSolid != null) {
                    yield clipSolidWithHalfSpace(buildBooleanOperandSolid(first), halfSpaceSolid, false);
                }
                throw new UnsupportedGeometryException(
                        "BOOLEAN_RESULT difference requires HALF_SPACE_SOLID or BOXED_HALF_SPACE second operand");
            }
            case "INTERSECTION" -> {
                StepHalfSpaceSolid halfSpaceSolid = asHalfSpaceOperand(second);
                if (halfSpaceSolid != null) {
                    yield clipSolidWithHalfSpace(buildBooleanOperandSolid(first), halfSpaceSolid, true);
                }
                halfSpaceSolid = asHalfSpaceOperand(first);
                if (halfSpaceSolid != null) {
                    yield clipSolidWithHalfSpace(buildBooleanOperandSolid(second), halfSpaceSolid, true);
                }
                throw new UnsupportedGeometryException(
                        "BOOLEAN_RESULT intersection requires one HALF_SPACE_SOLID or BOXED_HALF_SPACE operand");
            }
            case "UNION" -> {
                // UNION with half-space: extend solid into half-space region
                // This is the inverse of DIFFERENCE with half-space
                StepHalfSpaceSolid halfSpaceSolid = asHalfSpaceOperand(second);
                if (halfSpaceSolid != null) {
                    yield unionWithHalfSpace(buildBooleanOperandSolid(first), halfSpaceSolid);
                }
                halfSpaceSolid = asHalfSpaceOperand(first);
                if (halfSpaceSolid != null) {
                    yield unionWithHalfSpace(buildBooleanOperandSolid(second), halfSpaceSolid);
                }
                throw new UnsupportedGeometryException(
                        "BOOLEAN_RESULT union requires one HALF_SPACE_SOLID or BOXED_HALF_SPACE operand; general solid union is not supported");
            }
            default -> throw new UnsupportedGeometryException("BOOLEAN_RESULT operator " + normalizedOperator + " is unsupported");
        };
    }

    private StepHalfSpaceSolid asHalfSpaceOperand(StepEntity operand) {
        return operand instanceof StepHalfSpaceSolid halfSpaceSolid ? halfSpaceSolid : null;
    }

    private Solid buildBooleanOperandSolid(StepEntity operand) {
        return switch (operand) {
            case StepManifoldSolidBrep solidBrep -> buildSolid(solidBrep.id());
            case StepBrepWithVoids brepWithVoids -> buildSolid(brepWithVoids.id());
            case StepCsgPrimitive csgPrimitive -> buildCsgPrimitive(csgPrimitive);
            case StepCsgSolid csgSolid -> buildBooleanOperandSolid(csgSolid.treeRootExpression());
            case StepSolidReplica solidReplica -> buildSolid(solidReplica.id());
            case StepSweptAreaSolid sweptAreaSolid -> buildSweptAreaSolid(sweptAreaSolid);
            case StepSweptDiskSolid sweptDiskSolid -> buildSweptDiskSolid(sweptDiskSolid);
            case StepExtrudedAreaSolidTapered taperedExtrusion -> buildExtrudedAreaSolidTapered(taperedExtrusion);
            case StepRevolvedAreaSolidTapered taperedRevolution -> buildRevolvedAreaSolidTapered(taperedRevolution);
            case StepSurfaceCurveSweptAreaSolid surfaceCurveSweep -> buildSurfaceCurveSweptAreaSolid(surfaceCurveSweep);
            case StepBooleanClippingResult clippingResult ->
                    buildBooleanResult(clippingResult.operator(), clippingResult.firstOperand(), clippingResult.secondOperand());
            case StepBooleanResult booleanResult ->
                    buildBooleanResult(booleanResult.operator(), booleanResult.firstOperand(), booleanResult.secondOperand());
            default -> throw new UnsupportedGeometryException("boolean operand " + stepEntityTypeName(operand) + " is unsupported");
        };
    }

    private Solid clipSolidWithHalfSpace(Solid solid, StepHalfSpaceSolid halfSpaceSolid, boolean keepAgreementSide) {
        Plane plane = buildSupportedPlaneGeometry(halfSpaceSolid.baseSurface(), halfSpaceSolid.entityName());
        if (plane == null) {
            throw new UnsupportedGeometryException(halfSpaceSolid.entityName() + " requires PLANE geometry");
        }
        boolean keepPositive = keepAgreementSide ? halfSpaceSolid.agreementFlag() : !halfSpaceSolid.agreementFlag();
        Solid clipped = clipSolidWithPlane(solid, plane, keepPositive, "BOOLEAN_RESULT clipping");
        if (halfSpaceSolid.enclosure() == null) {
            return clipped;
        }
        if (!(halfSpaceSolid.enclosure() instanceof StepBoxDomain boxDomain)) {
            throw new UnsupportedGeometryException(
                    halfSpaceSolid.entityName() + " construction with "
                            + stepEntityTypeName(halfSpaceSolid.enclosure()) + " enclosure is unsupported");
        }
        return clipSolidWithBoxDomain(clipped, boxDomain, "BOOLEAN_RESULT clipping");
    }

    private Solid unionWithHalfSpace(Solid solid, StepHalfSpaceSolid halfSpaceSolid) {
        Plane plane = buildSupportedPlaneGeometry(halfSpaceSolid.baseSurface(), halfSpaceSolid.entityName());
        if (plane == null) {
            throw new UnsupportedGeometryException(halfSpaceSolid.entityName() + " requires PLANE geometry");
        }
        // Union with half-space: extend solid into half-space agreement side
        // This creates a new solid that includes both the original solid and the half-space region
        // For bounded half-space (BOXED_HALF_SPACE), union creates solid + box portion on agreement side
        if (halfSpaceSolid.enclosure() == null) {
            // Unbounded half-space union would create infinite geometry - not supported
            throw new UnsupportedGeometryException(
                    "BOOLEAN_RESULT union with unbounded HALF_SPACE_SOLID is not supported");
        }
        if (!(halfSpaceSolid.enclosure() instanceof StepBoxDomain boxDomain)) {
            throw new UnsupportedGeometryException(
                    halfSpaceSolid.entityName() + " union with "
                            + stepEntityTypeName(halfSpaceSolid.enclosure()) + " enclosure is unsupported");
        }
        // Build box domain geometry and merge with solid
        Solid boxSolid = buildBoxDomainSolid(boxDomain);
        // Clip box to half-space agreement side
        boolean keepAgreementSide = halfSpaceSolid.agreementFlag();
        Solid halfSpaceBox = clipSolidWithPlane(boxSolid, plane, keepAgreementSide, "UNION half-space box");
        // Merge solids: union of solid and halfSpaceBox
        return mergeSolids(solid, halfSpaceBox);
    }

    private Solid buildBoxDomainSolid(StepBoxDomain boxDomain) {
        CartesianPoint min = buildPoint(boxDomain.corner().id());
        if (boxDomain.dimensions().size() < 3) {
            throw new UnsupportedGeometryException("BOX_DOMAIN requires x, y and z dimensions");
        }
        double dx = boxDomain.dimensions().get(0);
        double dy = boxDomain.dimensions().get(1);
        double dz = boxDomain.dimensions().get(2);
        if (dx <= 0.0 || dy <= 0.0 || dz <= 0.0) {
            throw new UnsupportedGeometryException("BOX_DOMAIN dimensions must be positive");
        }
        CartesianPoint max = new CartesianPoint(min.x() + dx, min.y() + dy, min.z() + dz);
        // Build box from 6 faces
        List<Face> faces = new ArrayList<>();
        faces.add(faceFromPolyLoop(List.of(
                new CartesianPoint(min.x(), min.y(), min.z()),
                new CartesianPoint(max.x(), min.y(), min.z()),
                new CartesianPoint(max.x(), max.y(), min.z()),
                new CartesianPoint(min.x(), max.y(), min.z()),
                new CartesianPoint(min.x(), min.y(), min.z())
        ), new Direction3(0.0, 0.0, -1.0)));
        faces.add(faceFromPolyLoop(List.of(
                new CartesianPoint(min.x(), min.y(), max.z()),
                new CartesianPoint(min.x(), max.y(), max.z()),
                new CartesianPoint(max.x(), max.y(), max.z()),
                new CartesianPoint(max.x(), min.y(), max.z()),
                new CartesianPoint(min.x(), min.y(), max.z())
        ), new Direction3(0.0, 0.0, 1.0)));
        faces.add(faceFromPolyLoop(List.of(
                new CartesianPoint(min.x(), min.y(), min.z()),
                new CartesianPoint(min.x(), max.y(), min.z()),
                new CartesianPoint(min.x(), max.y(), max.z()),
                new CartesianPoint(min.x(), min.y(), max.z()),
                new CartesianPoint(min.x(), min.y(), min.z())
        ), new Direction3(-1.0, 0.0, 0.0)));
        faces.add(faceFromPolyLoop(List.of(
                new CartesianPoint(max.x(), min.y(), min.z()),
                new CartesianPoint(max.x(), min.y(), max.z()),
                new CartesianPoint(max.x(), max.y(), max.z()),
                new CartesianPoint(max.x(), max.y(), min.z()),
                new CartesianPoint(max.x(), min.y(), min.z())
        ), new Direction3(1.0, 0.0, 0.0)));
        faces.add(faceFromPolyLoop(List.of(
                new CartesianPoint(min.x(), min.y(), min.z()),
                new CartesianPoint(min.x(), min.y(), max.z()),
                new CartesianPoint(max.x(), min.y(), max.z()),
                new CartesianPoint(max.x(), min.y(), min.z()),
                new CartesianPoint(min.x(), min.y(), min.z())
        ), new Direction3(0.0, -1.0, 0.0)));
        faces.add(faceFromPolyLoop(List.of(
                new CartesianPoint(min.x(), max.y(), min.z()),
                new CartesianPoint(max.x(), max.y(), min.z()),
                new CartesianPoint(max.x(), max.y(), max.z()),
                new CartesianPoint(min.x(), max.y(), max.z()),
                new CartesianPoint(min.x(), max.y(), min.z())
        ), new Direction3(0.0, 1.0, 0.0)));
        return new Solid(new Shell(faces, true));
    }

    private Solid mergeSolids(Solid first, Solid second) {
        // Simple merge: combine all faces from both solids
        // This works when solids don't overlap or share boundaries
        List<Face> mergedFaces = new ArrayList<>();
        mergedFaces.addAll(first.outerShell().faces());
        mergedFaces.addAll(second.outerShell().faces());
        return new Solid(new Shell(mergedFaces, true));
    }

    private Solid clipSolidWithBoxDomain(Solid solid, StepBoxDomain boxDomain, String context) {
        CartesianPoint min = buildPoint(boxDomain.corner().id());
        if (boxDomain.dimensions().size() < 3) {
            throw new UnsupportedGeometryException("BOX_DOMAIN requires x, y and z dimensions");
        }
        double dx = boxDomain.dimensions().get(0);
        double dy = boxDomain.dimensions().get(1);
        double dz = boxDomain.dimensions().get(2);
        if (dx <= 0.0 || dy <= 0.0 || dz <= 0.0) {
            throw new UnsupportedGeometryException("BOX_DOMAIN dimensions must be positive");
        }
        CartesianPoint max = new CartesianPoint(min.x() + dx, min.y() + dy, min.z() + dz);
        Solid clipped = solid;
        clipped = clipSolidWithPlane(clipped, axisAlignedPlane(min, 1.0, 0.0, 0.0), true, context);
        clipped = clipSolidWithPlane(clipped, axisAlignedPlane(max, 1.0, 0.0, 0.0), false, context);
        clipped = clipSolidWithPlane(clipped, axisAlignedPlane(min, 0.0, 1.0, 0.0), true, context);
        clipped = clipSolidWithPlane(clipped, axisAlignedPlane(max, 0.0, 1.0, 0.0), false, context);
        clipped = clipSolidWithPlane(clipped, axisAlignedPlane(min, 0.0, 0.0, 1.0), true, context);
        return clipSolidWithPlane(clipped, axisAlignedPlane(max, 0.0, 0.0, 1.0), false, context);
    }

    private Plane axisAlignedPlane(CartesianPoint origin, double x, double y, double z) {
        return new Plane(origin, Direction3.from(new Vector3(x, y, z)));
    }

    private Solid clipSolidWithPlane(Solid solid, Plane plane, boolean keepPositive, String context) {
        List<Face> clippedFaces = new ArrayList<>();
        List<CartesianPoint> capPoints = new ArrayList<>();
        for (Face face : solid.outerShell().faces()) {
            List<CartesianPoint> polygon = outerLoopPoints(face);
            List<CartesianPoint> clipped = clipPolygonWithPlane(polygon, plane, keepPositive, capPoints);
            if (clipped.size() >= 3) {
                Plane facePlane = requirePlaneSurface(face, context);
                clippedFaces.add(faceFromPolyLoop(closeLoop3(clipped), polygonNormal(clipped, facePlane.normal().asVector())));
            }
        }
        List<CartesianPoint> capLoop = buildCapLoop(capPoints, plane);
        if (capLoop.size() >= 3) {
            Direction3 capNormal = keepPositive ? plane.normal().reverse() : plane.normal();
            clippedFaces.add(faceFromPolyLoop(closeLoop3(capLoop), capNormal));
        }
        if (clippedFaces.isEmpty()) {
            throw new UnsupportedGeometryException(context + " removed the entire solid");
        }
        return new Solid(new Shell(clippedFaces, true));
    }

    private Solid buildExtrudedAreaSolid(StepSweptAreaSolid sweptAreaSolid) {
        ProfileLoops profileLoops = buildAreaProfileLoops(sweptAreaSolid.sweptArea());
        List<Point2> profile = profileLoops.outer();
        if (profile.size() < 3) {
            throw new UnsupportedGeometryException("EXTRUDED_AREA_SOLID requires at least 3 profile points");
        }
        Axis2Placement3D solidPlacement = buildPlacement(sweptAreaSolid.position().id());
        if (!(sweptAreaSolid.sweepReference() instanceof StepDirection direction)) {
            throw new UnsupportedGeometryException("EXTRUDED_AREA_SOLID extrusion direction must be a DIRECTION");
        }
        Vector3 extrusion = buildDirection(direction.id()).asVector().scale(sweptAreaSolid.parameter());
        if (extrusion.isZero()) {
            throw new UnsupportedGeometryException("EXTRUDED_AREA_SOLID requires a non-zero extrusion depth");
        }

        List<CartesianPoint> bottom = profile.stream()
                .map(point -> mapProfilePoint(sweptAreaSolid.sweptArea(), solidPlacement, point))
                .toList();
        List<CartesianPoint> top = bottom.stream().map(point -> point.add(extrusion)).toList();
        List<List<CartesianPoint>> innerBottomLoops = profileLoops.inner().stream()
                .map(loop -> loop.stream()
                        .map(point -> mapProfilePoint(sweptAreaSolid.sweptArea(), solidPlacement, point))
                        .toList())
                .toList();
        List<List<CartesianPoint>> innerTopLoops = innerBottomLoops.stream()
                .map(loop -> loop.stream().map(point -> point.add(extrusion)).toList())
                .toList();

        List<Face> faces = new ArrayList<>();
        Direction3 axis = Direction3.from(extrusion);
        faces.add(faceFromProfileLoops(reverseClosedLoop3(bottom), reverseClosedLoops3(innerBottomLoops), axis.reverse()));
        faces.add(faceFromProfileLoops(closeLoop3(top), closeLoops3(innerTopLoops), axis));
        for (int index = 0; index < bottom.size(); index++) {
            CartesianPoint startBottom = bottom.get(index);
            CartesianPoint endBottom = bottom.get((index + 1) % bottom.size());
            CartesianPoint endTop = top.get((index + 1) % top.size());
            CartesianPoint startTop = top.get(index);
            Vector3 edge = endBottom.subtract(startBottom);
            Direction3 normal = Direction3.from(edge.cross(extrusion));
            faces.add(faceFromPolyLoop(
                    List.of(startBottom, endBottom, endTop, startTop, startBottom),
                    normal
            ));
        }
        for (int loopIndex = 0; loopIndex < innerBottomLoops.size(); loopIndex++) {
            List<CartesianPoint> innerBottom = innerBottomLoops.get(loopIndex);
            List<CartesianPoint> innerTop = innerTopLoops.get(loopIndex);
            for (int index = 0; index < innerBottom.size(); index++) {
                CartesianPoint startBottom = innerBottom.get(index);
                CartesianPoint endBottom = innerBottom.get((index + 1) % innerBottom.size());
                CartesianPoint endTop = innerTop.get((index + 1) % innerTop.size());
                CartesianPoint startTop = innerTop.get(index);
                Direction3 normal = quadNormal(startBottom, startTop, endTop, endBottom);
                faces.add(faceFromPolyLoop(
                        List.of(startBottom, startTop, endTop, endBottom, startBottom),
                        normal
                ));
            }
        }
        return new Solid(new Shell(faces, true));
    }

    private Solid buildRevolvedAreaSolid(StepSweptAreaSolid sweptAreaSolid) {
        ProfileLoops profileLoops = buildAreaProfileLoops(sweptAreaSolid.sweptArea());
        List<List<Point2>> profileRings = new ArrayList<>();
        profileRings.add(profileLoops.outer());
        profileRings.addAll(profileLoops.inner());
        if (profileLoops.outer().size() < 3) {
            throw new UnsupportedGeometryException("REVOLVED_AREA_SOLID requires at least 3 profile points");
        }
        if (!(sweptAreaSolid.sweepReference() instanceof StepAxis1Placement axisPlacement)) {
            throw new UnsupportedGeometryException("REVOLVED_AREA_SOLID axis must be an AXIS1_PLACEMENT");
        }
        double angle = sweptAreaSolid.parameter();
        if (Math.abs(angle) <= 1.0e-9) {
            throw new UnsupportedGeometryException("REVOLVED_AREA_SOLID requires a non-zero revolution angle");
        }
        if (Math.abs(angle) > Math.PI * 2.0 + 1.0e-9) {
            throw new UnsupportedGeometryException("REVOLVED_AREA_SOLID revolution angle must not exceed 2*PI");
        }

        Axis2Placement3D solidPlacement = buildPlacement(sweptAreaSolid.position().id());
        Axis1Placement revolutionAxis = buildAxis1Placement(axisPlacement.id());
        List<List<CartesianPoint>> sectionRings = profileRings.stream()
                .map(loop -> loop.stream()
                        .map(point -> mapProfilePoint(sweptAreaSolid.sweptArea(), solidPlacement, point))
                        .toList())
                .toList();
        for (List<CartesianPoint> ring : sectionRings) {
            for (CartesianPoint point : ring) {
                if (distanceToAxis(point, revolutionAxis) <= 1.0e-9) {
                    throw new UnsupportedGeometryException("REVOLVED_AREA_SOLID profile must not intersect the revolution axis");
                }
            }
        }

        int stepCount = Math.max(1, (int) Math.ceil(Math.abs(angle) / (Math.PI / 16.0)));
        boolean closedRevolution = Math.abs(Math.abs(angle) - Math.PI * 2.0) <= 1.0e-9;
        int sectionCount = closedRevolution ? stepCount : stepCount + 1;
        List<List<List<CartesianPoint>>> revolvedRings = sectionRings.stream()
                .map(ring -> {
                    List<List<CartesianPoint>> sections = new ArrayList<>(sectionCount);
                    for (int step = 0; step < sectionCount; step++) {
                        double sectionAngle = angle * step / stepCount;
                        sections.add(ring.stream()
                                .map(point -> rotateAroundAxis(point, revolutionAxis, sectionAngle))
                                .toList());
                    }
                    return List.copyOf(sections);
                })
                .toList();

        List<Face> faces = new ArrayList<>();
        if (!closedRevolution) {
            List<CartesianPoint> outerStart = sectionRings.getFirst();
            List<List<CartesianPoint>> innerStart = sectionRings.subList(1, sectionRings.size());
            List<CartesianPoint> outerEnd = revolvedRings.getFirst().getLast();
            List<List<CartesianPoint>> innerEnd = revolvedRings.stream()
                    .skip(1)
                    .map(List::getLast)
                    .toList();
            Vector3 startSweep = sweepDirectionAtSection(outerStart, revolutionAxis, angle >= 0.0);
            Vector3 endSweep = sweepDirectionAtSection(outerEnd, revolutionAxis, angle >= 0.0);
            faces.add(faceFromProfileLoops(
                    closeLoop3(outerStart),
                    closeLoops3(innerStart),
                    polygonNormal(outerStart, startSweep.scale(-1.0))
            ));
            faces.add(faceFromProfileLoops(
                    reverseClosedLoop3(outerEnd),
                    reverseClosedLoops3(innerEnd),
                    polygonNormal(outerEnd, endSweep)
            ));
        }
        for (List<List<CartesianPoint>> ringSections : revolvedRings) {
            int ringSize = ringSections.getFirst().size();
            for (int sectionIndex = 0; sectionIndex < stepCount; sectionIndex++) {
                List<CartesianPoint> current = ringSections.get(sectionIndex);
                List<CartesianPoint> next = ringSections.get((sectionIndex + 1) % ringSections.size());
                for (int pointIndex = 0; pointIndex < ringSize; pointIndex++) {
                    CartesianPoint a = current.get(pointIndex);
                    CartesianPoint b = current.get((pointIndex + 1) % ringSize);
                    CartesianPoint c = next.get((pointIndex + 1) % ringSize);
                    CartesianPoint d = next.get(pointIndex);
                    faces.add(faceFromPolyLoop(
                            List.of(a, b, c, d, a),
                            quadNormal(a, b, c, d)
                    ));
                }
            }
        }
        return new Solid(new Shell(faces, true));
    }

    private ProfileLoops buildAreaProfileLoops(StepProfileDef profile) {
        if (!"AREA".equals(profile.profileType())) {
            throw new UnsupportedGeometryException(profile.entityName() + " must be an AREA profile");
        }
        return switch (profile.entityName()) {
            case "RECTANGLE_PROFILE_DEF", "CENTERED_RECTANGLE_PROFILE_DEF" ->
                    new ProfileLoops(normalizeOuterLoop(rectangleProfile(profile)), List.of());
            case "CIRCLE_PROFILE_DEF" ->
                    new ProfileLoops(normalizeOuterLoop(circleProfile(profile)), List.of());
            case "ELLIPSE_PROFILE_DEF" ->
                    new ProfileLoops(normalizeOuterLoop(ellipseProfile(profile)), List.of());
            case "ROUNDED_RECTANGLE_PROFILE_DEF" ->
                    new ProfileLoops(normalizeOuterLoop(roundedRectangleProfile(profile)), List.of());
            case "CIRCULAR_HOLLOW_PROFILE_DEF" ->
                    circularHollowProfile(profile);
            case "RECTANGLE_HOLLOW_PROFILE_DEF" ->
                    rectangleHollowProfile(profile);
            case "CENTERED_CIRCLE_PROFILE_DEF" ->
                    new ProfileLoops(normalizeOuterLoop(centeredCircleProfile(profile)), List.of());
            case "CENTRE_LINE_ARC_PROFILE_DEF" ->
                    new ProfileLoops(normalizeOuterLoop(centreLineArcProfile(profile)), List.of());
            case "ARBITRARY_CLOSED_PROFILE_DEF", "ARBITRARY_PROFILE_DEF" ->
                    new ProfileLoops(normalizeOuterLoop(arbitraryClosedProfile(profile)), List.of());
            case "ARBITRARY_PROFILE_DEF_WITH_VOIDS" ->
                    arbitraryProfileWithVoids(profile);
            // Standard structural steel profiles (Phase 2E)
            case "I_SHAPE_PROFILE_DEF", "I_PROFILE_DEF" ->
                    new ProfileLoops(normalizeOuterLoop(iShapeProfile(profile)), List.of());
            case "T_SHAPE_PROFILE_DEF", "T_PROFILE_DEF", "TEE_PROFILE_DEF" ->
                    new ProfileLoops(normalizeOuterLoop(tShapeProfile(profile)), List.of());
            case "L_SHAPE_PROFILE_DEF", "L_PROFILE_DEF", "ANGLE_PROFILE_DEF" ->
                    new ProfileLoops(normalizeOuterLoop(lShapeProfile(profile)), List.of());
            case "U_SHAPE_PROFILE_DEF", "U_PROFILE_DEF", "CHANNEL_PROFILE_DEF", "C_SHAPE_PROFILE_DEF" ->
                    new ProfileLoops(normalizeOuterLoop(uShapeProfile(profile)), List.of());
            case "Z_SHAPE_PROFILE_DEF", "Z_PROFILE_DEF" ->
                    new ProfileLoops(normalizeOuterLoop(zShapeProfile(profile)), List.of());
            case "HAT_SHAPE_PROFILE_DEF" ->
                    new ProfileLoops(normalizeOuterLoop(hatShapeProfile(profile)), List.of());
            case "FLAT_BAR_PROFILE_DEF" ->
                    new ProfileLoops(normalizeOuterLoop(flatBarProfile(profile)), List.of());
            case "DOVE_TAIL_PROFILE_DEF" ->
                    new ProfileLoops(normalizeOuterLoop(doveTailProfile(profile)), List.of());
            default -> throw new UnsupportedGeometryException(profile.entityName() + " extrusion is unsupported");
        };
    }

    private List<Point2> rectangleProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 2) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires x and y dimensions");
        }
        double halfX = profile.parameters().get(0) * 0.5;
        double halfY = profile.parameters().get(1) * 0.5;
        return List.of(
                new Point2(-halfX, -halfY),
                new Point2(halfX, -halfY),
                new Point2(halfX, halfY),
                new Point2(-halfX, halfY),
                new Point2(-halfX, -halfY)
        );
    }

    private List<Point2> circleProfile(StepProfileDef profile) {
        if (profile.parameters().isEmpty()) {
            throw new UnsupportedGeometryException("CIRCLE_PROFILE_DEF requires a radius");
        }
        double radius = profile.parameters().getFirst();
        if (radius <= 0.0) {
            throw new UnsupportedGeometryException("CIRCLE_PROFILE_DEF radius must be positive");
        }
        Circle2 circle = new Circle2(new Point2(0.0, 0.0), new Direction2(1.0, 0.0), radius);
        return sampleCurve2(circle, 72);
    }

    private List<Point2> ellipseProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 2) {
            throw new UnsupportedGeometryException("ELLIPSE_PROFILE_DEF requires semi axes");
        }
        double semiAxis1 = profile.parameters().get(0);
        double semiAxis2 = profile.parameters().get(1);
        if (semiAxis1 <= 0.0 || semiAxis2 <= 0.0) {
            throw new UnsupportedGeometryException("ELLIPSE_PROFILE_DEF semi axes must be positive");
        }
        Ellipse2 ellipse = new Ellipse2(new Point2(0.0, 0.0), new Direction2(1.0, 0.0), semiAxis1, semiAxis2);
        return sampleCurve2(ellipse, 72);
    }

    private List<Point2> roundedRectangleProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 3) {
            throw new UnsupportedGeometryException("ROUNDED_RECTANGLE_PROFILE_DEF requires x, y and radius");
        }
        double width = profile.parameters().get(0);
        double height = profile.parameters().get(1);
        double radius = profile.parameters().get(2);
        if (width <= 0.0 || height <= 0.0 || radius <= 0.0) {
            throw new UnsupportedGeometryException("ROUNDED_RECTANGLE_PROFILE_DEF dimensions must be positive");
        }
        if (radius * 2.0 >= width || radius * 2.0 >= height) {
            throw new UnsupportedGeometryException("ROUNDED_RECTANGLE_PROFILE_DEF radius must be smaller than half dimensions");
        }
        List<Point2> points = new ArrayList<>();
        appendArc(points, new Point2(width * 0.5 - radius, height * 0.5 - radius), radius, 0.0, Math.PI * 0.5, 12, true);
        appendArc(points, new Point2(-width * 0.5 + radius, height * 0.5 - radius), radius, Math.PI * 0.5, Math.PI, 12, false);
        appendArc(points, new Point2(-width * 0.5 + radius, -height * 0.5 + radius), radius, Math.PI, Math.PI * 1.5, 12, false);
        appendArc(points, new Point2(width * 0.5 - radius, -height * 0.5 + radius), radius, Math.PI * 1.5, Math.PI * 2.0, 12, false);
        points.add(points.getFirst());
        return List.copyOf(points);
    }

    private void appendArc(
            List<Point2> points,
            Point2 center,
            double radius,
            double startAngle,
            double endAngle,
            int segments,
            boolean includeStart
    ) {
        int startIndex = includeStart ? 0 : 1;
        for (int index = startIndex; index <= segments; index++) {
            double angle = startAngle + (endAngle - startAngle) * index / segments;
            points.add(new Point2(
                    center.x() + Math.cos(angle) * radius,
                    center.y() + Math.sin(angle) * radius
            ));
        }
    }

    private ProfileLoops circularHollowProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 2) {
            throw new UnsupportedGeometryException("CIRCULAR_HOLLOW_PROFILE_DEF requires radius and wall thickness");
        }
        double outerRadius = profile.parameters().get(0);
        double wallThickness = profile.parameters().get(1);
        double innerRadius = outerRadius - wallThickness;
        if (outerRadius <= 0.0 || wallThickness <= 0.0 || innerRadius <= 0.0) {
            throw new UnsupportedGeometryException("CIRCULAR_HOLLOW_PROFILE_DEF dimensions must define a positive inner radius");
        }
        return new ProfileLoops(
                normalizeOuterLoop(circleProfile(new StepProfileDef(profile.id(), profile.profileType(), profile.profileName(), profile.position(), profile.curves(), List.of(outerRadius), "CIRCLE_PROFILE_DEF"))),
                List.of(normalizeInnerLoop(circleProfile(new StepProfileDef(profile.id(), profile.profileType(), profile.profileName(), profile.position(), profile.curves(), List.of(innerRadius), "CIRCLE_PROFILE_DEF"))))
        );
    }

    private ProfileLoops rectangleHollowProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 4) {
            throw new UnsupportedGeometryException("RECTANGLE_HOLLOW_PROFILE_DEF requires xDim, yDim, wallThickness and innerRadius");
        }
        double xDim = profile.parameters().get(0);
        double yDim = profile.parameters().get(1);
        double wallThickness = profile.parameters().get(2);
        double innerRadius = profile.parameters().get(3);
        double innerXDim = xDim - 2.0 * wallThickness;
        double innerYDim = yDim - 2.0 * wallThickness;
        if (xDim <= 0.0 || yDim <= 0.0 || wallThickness <= 0.0 || innerXDim <= 0.0 || innerYDim <= 0.0) {
            throw new UnsupportedGeometryException("RECTANGLE_HOLLOW_PROFILE_DEF dimensions must define a positive inner rectangle");
        }
        double halfOuterX = xDim * 0.5;
        double halfOuterY = yDim * 0.5;
        double halfInnerX = innerXDim * 0.5;
        double halfInnerY = innerYDim * 0.5;
        List<Point2> outer = List.of(
                new Point2(-halfOuterX, -halfOuterY),
                new Point2(halfOuterX, -halfOuterY),
                new Point2(halfOuterX, halfOuterY),
                new Point2(-halfOuterX, halfOuterY),
                new Point2(-halfOuterX, -halfOuterY)
        );
        List<Point2> inner = List.of(
                new Point2(-halfInnerX, -halfInnerY),
                new Point2(-halfInnerX, halfInnerY),
                new Point2(halfInnerX, halfInnerY),
                new Point2(halfInnerX, -halfInnerY),
                new Point2(-halfInnerX, -halfInnerY)
        );
        return new ProfileLoops(normalizeOuterLoop(outer), List.of(normalizeInnerLoop(inner)));
    }

    private List<Point2> centeredCircleProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 2) {
            throw new UnsupportedGeometryException("CENTERED_CIRCLE_PROFILE_DEF requires radius and centerOffset");
        }
        double radius = profile.parameters().get(0);
        double centerOffset = profile.parameters().get(1);
        if (radius <= 0.0) {
            throw new UnsupportedGeometryException("CENTERED_CIRCLE_PROFILE_DEF radius must be positive");
        }
        // Circle centered at (centerOffset, 0)
        Circle2 circle = new Circle2(new Point2(centerOffset, 0.0), new Direction2(1.0, 0.0), radius);
        return sampleCurve2(circle, 72);
    }

    private List<Point2> centreLineArcProfile(StepProfileDef profile) {
        if (profile.parameters().size() < 2) {
            throw new UnsupportedGeometryException("CENTRE_LINE_ARC_PROFILE_DEF requires radius and angle");
        }
        double radius = profile.parameters().get(0);
        double angle = profile.parameters().get(1);
        if (radius <= 0.0 || angle <= 0.0) {
            throw new UnsupportedGeometryException("CENTRE_LINE_ARC_PROFILE_DEF radius and angle must be positive");
        }
        // Arc centered at origin, from -angle/2 to +angle/2
        List<Point2> points = new ArrayList<>();
        int segments = Math.max(12, (int) (Math.abs(angle) / (Math.PI / 36.0)));
        double startAngle = -angle / 2.0;
        double endAngle = angle / 2.0;
        for (int i = 0; i <= segments; i++) {
            double a = startAngle + (endAngle - startAngle) * i / segments;
            points.add(new Point2(radius * Math.cos(a), radius * Math.sin(a)));
        }
        return List.copyOf(points);
    }

    // Standard structural steel profile implementations (Phase 2E)
    private List<Point2> iShapeProfile(StepProfileDef profile) {
        // I-beam/I-shape profile: flangeWidth, webDepth, flangeThickness, webThickness, filletRadius
        if (profile.parameters().size() < 4) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires at least 4 parameters (flangeWidth, webDepth, flangeThickness, webThickness)");
        }
        double flangeWidth = profile.parameters().get(0);
        double webDepth = profile.parameters().get(1);
        double flangeThickness = profile.parameters().get(2);
        double webThickness = profile.parameters().get(3);
        double filletRadius = profile.parameters().size() > 4 ? profile.parameters().get(4) : 0.0;
        double halfFlange = flangeWidth / 2.0;
        double halfWeb = webThickness / 2.0;
        double halfDepth = webDepth / 2.0;
        List<Point2> points = new ArrayList<>();
        // Upper flange (left to right)
        points.add(new Point2(-halfFlange, halfDepth));
        points.add(new Point2(-halfWeb, halfDepth));
        if (filletRadius > 0.0) {
            appendFillet(points, -halfWeb, halfDepth - filletRadius, filletRadius, Math.PI/2, 0, 6);
        }
        points.add(new Point2(-halfWeb, -halfDepth + flangeThickness));
        if (filletRadius > 0.0) {
            appendFillet(points, -halfWeb, -halfDepth + flangeThickness + filletRadius, filletRadius, Math.PI, Math.PI/2, 6);
        }
        // Lower flange left
        points.add(new Point2(-halfFlange, -halfDepth));
        // Lower flange right
        points.add(new Point2(halfFlange, -halfDepth));
        // Web right side
        if (filletRadius > 0.0) {
            appendFillet(points, halfWeb, -halfDepth + flangeThickness + filletRadius, filletRadius, Math.PI/2, 0, 6);
        }
        points.add(new Point2(halfWeb, -halfDepth + flangeThickness));
        if (filletRadius > 0.0) {
            appendFillet(points, halfWeb, halfDepth - filletRadius, filletRadius, 0, -Math.PI/2, 6);
        }
        points.add(new Point2(halfWeb, halfDepth));
        // Upper flange right
        points.add(new Point2(halfFlange, halfDepth));
        points.add(points.getFirst());
        return List.copyOf(points);
    }

    private List<Point2> tShapeProfile(StepProfileDef profile) {
        // T-shape profile: flangeWidth, webDepth, flangeThickness, webThickness, filletRadius
        if (profile.parameters().size() < 4) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires at least 4 parameters");
        }
        double flangeWidth = profile.parameters().get(0);
        double webDepth = profile.parameters().get(1);
        double flangeThickness = profile.parameters().get(2);
        double webThickness = profile.parameters().get(3);
        double filletRadius = profile.parameters().size() > 4 ? profile.parameters().get(4) : 0.0;
        double halfFlange = flangeWidth / 2.0;
        double halfWeb = webThickness / 2.0;
        double halfDepth = webDepth / 2.0;
        List<Point2> points = new ArrayList<>();
        // Top flange
        points.add(new Point2(-halfFlange, halfDepth));
        points.add(new Point2(halfFlange, halfDepth));
        points.add(new Point2(halfFlange, halfDepth - flangeThickness));
        if (filletRadius > 0.0) {
            appendFillet(points, halfWeb, halfDepth - flangeThickness + filletRadius, filletRadius, Math.PI/2, 0, 6);
        }
        points.add(new Point2(halfWeb, halfDepth - flangeThickness));
        points.add(new Point2(halfWeb, -halfDepth));
        points.add(new Point2(-halfWeb, -halfDepth));
        points.add(new Point2(-halfWeb, halfDepth - flangeThickness));
        if (filletRadius > 0.0) {
            appendFillet(points, -halfWeb, halfDepth - flangeThickness + filletRadius, filletRadius, Math.PI, Math.PI/2, 6);
        }
        points.add(new Point2(-halfFlange, halfDepth - flangeThickness));
        points.add(points.getFirst());
        return List.copyOf(points);
    }

    private List<Point2> lShapeProfile(StepProfileDef profile) {
        // L-shape/Angle profile: width, depth, thickness, filletRadius
        if (profile.parameters().size() < 3) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires at least 3 parameters");
        }
        double width = profile.parameters().get(0);
        double depth = profile.parameters().get(1);
        double thickness = profile.parameters().get(2);
        double filletRadius = profile.parameters().size() > 3 ? profile.parameters().get(3) : 0.0;
        double innerRadius = profile.parameters().size() > 4 ? profile.parameters().get(4) : 0.0;
        List<Point2> points = new ArrayList<>();
        // Outer contour
        points.add(new Point2(0.0, 0.0));
        points.add(new Point2(width, 0.0));
        points.add(new Point2(width, thickness));
        if (filletRadius > 0.0) {
            appendFillet(points, thickness - filletRadius, thickness - filletRadius, filletRadius, 0, -Math.PI/2, 6);
        } else {
            points.add(new Point2(thickness, thickness));
        }
        points.add(new Point2(thickness, depth));
        points.add(new Point2(0.0, depth));
        points.add(points.getFirst());
        return List.copyOf(points);
    }

    private List<Point2> uShapeProfile(StepProfileDef profile) {
        // U-shape/Channel profile: flangeWidth, webDepth, flangeThickness, webThickness, filletRadius
        if (profile.parameters().size() < 4) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires at least 4 parameters");
        }
        double flangeWidth = profile.parameters().get(0);
        double webDepth = profile.parameters().get(1);
        double flangeThickness = profile.parameters().get(2);
        double webThickness = profile.parameters().get(3);
        double filletRadius = profile.parameters().size() > 4 ? profile.parameters().get(4) : 0.0;
        double halfFlange = flangeWidth / 2.0;
        double halfDepth = webDepth / 2.0;
        List<Point2> points = new ArrayList<>();
        // Outer contour of U-shape
        points.add(new Point2(-halfFlange, -halfDepth));
        points.add(new Point2(halfFlange, -halfDepth));
        points.add(new Point2(halfFlange, -halfDepth + flangeThickness));
        if (filletRadius > 0.0) {
            appendFillet(points, halfFlange - webThickness - filletRadius, -halfDepth + flangeThickness + filletRadius, filletRadius, 0, -Math.PI/2, 6);
        }
        points.add(new Point2(halfFlange - webThickness, -halfDepth + flangeThickness));
        points.add(new Point2(halfFlange - webThickness, halfDepth - flangeThickness));
        if (filletRadius > 0.0) {
            appendFillet(points, halfFlange - webThickness - filletRadius, halfDepth - flangeThickness - filletRadius, filletRadius, Math.PI/2, Math.PI, 6);
        }
        points.add(new Point2(halfFlange, halfDepth - flangeThickness));
        points.add(new Point2(halfFlange, halfDepth));
        points.add(new Point2(-halfFlange, halfDepth));
        points.add(new Point2(-halfFlange, halfDepth - flangeThickness));
        if (filletRadius > 0.0) {
            appendFillet(points, -halfFlange + webThickness + filletRadius, halfDepth - flangeThickness - filletRadius, filletRadius, Math.PI, Math.PI*1.5, 6);
        }
        points.add(new Point2(-halfFlange + webThickness, halfDepth - flangeThickness));
        points.add(new Point2(-halfFlange + webThickness, -halfDepth + flangeThickness));
        if (filletRadius > 0.0) {
            appendFillet(points, -halfFlange + webThickness + filletRadius, -halfDepth + flangeThickness + filletRadius, filletRadius, Math.PI*1.5, Math.PI*2, 6);
        }
        points.add(new Point2(-halfFlange, -halfDepth + flangeThickness));
        points.add(points.getFirst());
        return List.copyOf(points);
    }

    private List<Point2> zShapeProfile(StepProfileDef profile) {
        // Z-shape profile: flangeWidth, webDepth, flangeThickness, webThickness, filletRadius
        if (profile.parameters().size() < 4) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires at least 4 parameters");
        }
        double flangeWidth = profile.parameters().get(0);
        double webDepth = profile.parameters().get(1);
        double flangeThickness = profile.parameters().get(2);
        double webThickness = profile.parameters().get(3);
        double halfFlange = flangeWidth / 2.0;
        double halfWeb = webThickness / 2.0;
        double halfDepth = webDepth / 2.0;
        List<Point2> points = new ArrayList<>();
        // Upper flange right
        points.add(new Point2(halfFlange, halfDepth));
        points.add(new Point2(halfFlange, halfDepth - flangeThickness));
        points.add(new Point2(halfWeb, halfDepth - flangeThickness));
        points.add(new Point2(halfWeb, -halfDepth + flangeThickness));
        // Lower flange left
        points.add(new Point2(-halfFlange, -halfDepth + flangeThickness));
        points.add(new Point2(-halfFlange, -halfDepth));
        points.add(new Point2(halfFlange - webThickness, -halfDepth));
        points.add(new Point2(halfFlange - webThickness, -halfDepth + flangeThickness));
        points.add(new Point2(-halfWeb, -halfDepth + flangeThickness));
        points.add(new Point2(-halfWeb, halfDepth - flangeThickness));
        // Upper flange left
        points.add(new Point2(-halfFlange, halfDepth - flangeThickness));
        points.add(new Point2(-halfFlange, halfDepth));
        points.add(points.getFirst());
        return List.copyOf(points);
    }

    private List<Point2> hatShapeProfile(StepProfileDef profile) {
        // Hat-shape profile: similar to U-shape but inverted
        if (profile.parameters().size() < 4) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires at least 4 parameters");
        }
        double flangeWidth = profile.parameters().get(0);
        double webDepth = profile.parameters().get(1);
        double flangeThickness = profile.parameters().get(2);
        double webThickness = profile.parameters().get(3);
        double halfFlange = flangeWidth / 2.0;
        double halfDepth = webDepth / 2.0;
        List<Point2> points = new ArrayList<>();
        points.add(new Point2(-halfFlange, halfDepth));
        points.add(new Point2(halfFlange, halfDepth));
        points.add(new Point2(halfFlange, halfDepth - flangeThickness));
        points.add(new Point2(halfFlange - webThickness, halfDepth - flangeThickness));
        points.add(new Point2(halfFlange - webThickness, -halfDepth + flangeThickness));
        points.add(new Point2(halfFlange, -halfDepth + flangeThickness));
        points.add(new Point2(halfFlange, -halfDepth));
        points.add(new Point2(-halfFlange, -halfDepth));
        points.add(new Point2(-halfFlange, -halfDepth + flangeThickness));
        points.add(new Point2(-halfFlange + webThickness, -halfDepth + flangeThickness));
        points.add(new Point2(-halfFlange + webThickness, halfDepth - flangeThickness));
        points.add(new Point2(-halfFlange, halfDepth - flangeThickness));
        points.add(points.getFirst());
        return List.copyOf(points);
    }

    private List<Point2> flatBarProfile(StepProfileDef profile) {
        // Flat bar: simple rectangle
        if (profile.parameters().size() < 2) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires width and thickness");
        }
        double width = profile.parameters().get(0);
        double thickness = profile.parameters().get(1);
        double halfWidth = width / 2.0;
        double halfThickness = thickness / 2.0;
        return List.of(
                new Point2(-halfWidth, -halfThickness),
                new Point2(halfWidth, -halfThickness),
                new Point2(halfWidth, halfThickness),
                new Point2(-halfWidth, halfThickness),
                new Point2(-halfWidth, -halfThickness)
        );
    }

    private List<Point2> doveTailProfile(StepProfileDef profile) {
        // Dove tail profile: width, depth, angle, neckWidth
        if (profile.parameters().size() < 3) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires at least 3 parameters");
        }
        double width = profile.parameters().get(0);
        double depth = profile.parameters().get(1);
        double angle = profile.parameters().size() > 2 ? profile.parameters().get(2) : 60.0; // degrees
        double neckWidth = profile.parameters().size() > 3 ? profile.parameters().get(3) : width * 0.5;
        double halfWidth = width / 2.0;
        double halfNeck = neckWidth / 2.0;
        double angleRad = Math.toRadians(angle);
        double sideOffset = (width - neckWidth) / 2.0;
        List<Point2> points = new ArrayList<>();
        points.add(new Point2(-halfWidth, 0.0));
        points.add(new Point2(halfWidth, 0.0));
        points.add(new Point2(halfNeck, depth));
        points.add(new Point2(-halfNeck, depth));
        points.add(points.getFirst());
        return List.copyOf(points);
    }

    private void appendFillet(List<Point2> points, double centerX, double centerY, double radius, double startAngle, double endAngle, int segments) {
        for (int i = 1; i <= segments; i++) {
            double angle = startAngle + (endAngle - startAngle) * i / segments;
            points.add(new Point2(centerX + radius * Math.cos(angle), centerY + radius * Math.sin(angle)));
        }
    }

    private List<Point2> arbitraryClosedProfile(StepProfileDef profile) {
        if (profile.curves().isEmpty()) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires a profile curve");
        }
        return arbitraryProfileCurve(profile.entityName(), profile.curves().getFirst());
    }

    private ProfileLoops arbitraryProfileWithVoids(StepProfileDef profile) {
        if (profile.curves().isEmpty()) {
            throw new UnsupportedGeometryException(profile.entityName() + " requires an outer profile curve");
        }
        List<Point2> outer = normalizeOuterLoop(arbitraryProfileCurve(profile.entityName(), profile.curves().getFirst()));
        List<List<Point2>> inner = profile.curves().stream()
                .skip(1)
                .map(curve -> normalizeInnerLoop(arbitraryProfileCurve(profile.entityName(), curve)))
                .toList();
        return new ProfileLoops(outer, inner);
    }

    private List<Point2> arbitraryProfileCurve(String entityName, StepEntity curveEntity) {
        Object curve = buildCurve2(curveEntity);
        if (!(curve instanceof Curve2 curve2)) {
            throw new UnsupportedGeometryException(entityName + " profile curve is not 2D");
        }
        return sampleCurve2(curve2, 72);
    }

    private List<Point2> normalizeOuterLoop(List<Point2> points) {
        List<Point2> normalized = normalizeClosedLoop2(points);
        if (Math.abs(signedArea2(normalized)) <= 1.0e-9) {
            throw new UnsupportedGeometryException("profile area must be non-zero");
        }
        if (signedArea2(normalized) < 0.0) {
            normalized = reverseClosedLoop2(normalized);
        }
        return List.copyOf(normalized.subList(0, normalized.size() - 1));
    }

    private List<Point2> normalizeInnerLoop(List<Point2> points) {
        List<Point2> normalized = normalizeClosedLoop2(points);
        if (Math.abs(signedArea2(normalized)) <= 1.0e-9) {
            throw new UnsupportedGeometryException("profile hole area must be non-zero");
        }
        if (signedArea2(normalized) > 0.0) {
            normalized = reverseClosedLoop2(normalized);
        }
        return List.copyOf(normalized.subList(0, normalized.size() - 1));
    }

    private CartesianPoint mapProfilePoint(StepProfileDef profile, Axis2Placement3D solidPlacement, Point2 point) {
        Point2 local = point;
        if (profile.position() instanceof StepAxis2Placement2D placement2D) {
            Point2 origin2 = buildPoint2(placement2D.location().id());
            Direction2 x2 = buildDirection2(placement2D.refDirection().id());
            Direction2 y2 = new Direction2(-x2.y(), x2.x());
            local = origin2.add(x2.asVector().scale(point.x())).add(y2.asVector().scale(point.y()));
        }
        Vector3 alongX = solidPlacement.xDirection().asVector().scale(local.x());
        Vector3 alongY = solidPlacement.yDirection().asVector().scale(local.y());
        return solidPlacement.location().add(alongX.add(alongY));
    }

    private Face faceFromPolyLoop(List<CartesianPoint> points, Direction3 normal) {
        Plane plane = new Plane(points.getFirst(), normal);
        return new Face(plane, List.of(FaceBound.outer(new PolyLoop(points), true)), true);
    }

    private Face faceFromProfileLoops(
            List<CartesianPoint> outer,
            List<List<CartesianPoint>> innerLoops,
            Direction3 normal
    ) {
        Plane plane = new Plane(outer.getFirst(), normal);
        List<FaceBound> bounds = new ArrayList<>();
        bounds.add(FaceBound.outer(new PolyLoop(outer), true));
        for (List<CartesianPoint> inner : innerLoops) {
            bounds.add(FaceBound.inner(new PolyLoop(inner), true));
        }
        return new Face(plane, bounds, true);
    }

    private List<List<CartesianPoint>> closeLoops3(List<List<CartesianPoint>> loops) {
        return loops.stream().map(this::closeLoop3).toList();
    }

    private List<List<CartesianPoint>> reverseClosedLoops3(List<List<CartesianPoint>> loops) {
        return loops.stream().map(this::reverseClosedLoop3).toList();
    }

    private List<CartesianPoint> outerLoopPoints(Face face) {
        for (FaceBound bound : face.bounds()) {
            if (!bound.outer()) {
                continue;
            }
            if (bound.loop() instanceof PolyLoop polyLoop) {
                List<CartesianPoint> points = polyLoop.points();
                return stripClosedPoint(points);
            }
            if (bound.loop() instanceof EdgeLoop edgeLoop) {
                List<CartesianPoint> points = new ArrayList<>(edgeLoop.edges().size());
                for (OrientedEdge edge : edgeLoop.edges()) {
                    points.add(edge.startVertex().point());
                }
                return points;
            }
        }
        throw new UnsupportedGeometryException("boolean clipping requires a polygonal outer loop");
    }

    private Plane requirePlaneSurface(Face face, String context) {
        if (face.surface() instanceof Plane plane) {
            return plane;
        }
        throw new UnsupportedGeometryException(context + " requires planar topology faces");
    }

    private List<CartesianPoint> stripClosedPoint(List<CartesianPoint> points) {
        if (points.size() >= 2 && points.getFirst().distanceTo(points.getLast()) <= 1.0e-9) {
            return List.copyOf(points.subList(0, points.size() - 1));
        }
        return List.copyOf(points);
    }

    private List<CartesianPoint> clipPolygonWithPlane(
            List<CartesianPoint> polygon,
            Plane plane,
            boolean keepPositive,
            List<CartesianPoint> capPoints
    ) {
        List<CartesianPoint> output = new ArrayList<>();
        for (int index = 0; index < polygon.size(); index++) {
            CartesianPoint current = polygon.get(index);
            CartesianPoint next = polygon.get((index + 1) % polygon.size());
            double currentDistance = signedDistanceForHalfSpace(current, plane, keepPositive);
            double nextDistance = signedDistanceForHalfSpace(next, plane, keepPositive);
            boolean currentInside = currentDistance >= -1.0e-9;
            boolean nextInside = nextDistance >= -1.0e-9;
            if (currentInside && nextInside) {
                addDistinctPoint(output, next);
            } else if (currentInside) {
                CartesianPoint intersection = interpolatePlaneIntersection(current, next, currentDistance, nextDistance);
                addDistinctPoint(output, intersection);
                addDistinctPoint(capPoints, intersection);
            } else if (nextInside) {
                CartesianPoint intersection = interpolatePlaneIntersection(current, next, currentDistance, nextDistance);
                addDistinctPoint(output, intersection);
                addDistinctPoint(output, next);
                addDistinctPoint(capPoints, intersection);
            }
        }
        if (!output.isEmpty() && output.getFirst().distanceTo(output.getLast()) <= 1.0e-9) {
            output.remove(output.size() - 1);
        }
        return List.copyOf(output);
    }

    private double signedDistanceForHalfSpace(CartesianPoint point, Plane plane, boolean keepPositive) {
        double distance = plane.signedDistanceTo(point);
        return keepPositive ? distance : -distance;
    }

    private CartesianPoint interpolatePlaneIntersection(
            CartesianPoint start,
            CartesianPoint end,
            double startDistance,
            double endDistance
    ) {
        double t = startDistance / (startDistance - endDistance);
        Vector3 edge = end.subtract(start);
        return start.add(edge.scale(t));
    }

    private void addDistinctPoint(List<CartesianPoint> points, CartesianPoint candidate) {
        if (points.isEmpty() || points.getLast().distanceTo(candidate) > 1.0e-9) {
            points.add(candidate);
        }
    }

    private List<CartesianPoint> buildCapLoop(List<CartesianPoint> capPoints, Plane plane) {
        List<CartesianPoint> unique = uniquePoints(capPoints);
        if (unique.size() < 3) {
            return List.of();
        }
        CartesianPoint centroid = averagePoint(unique);
        Vector3 xAxis = planeBasis(plane.normal());
        Vector3 yAxis = plane.normal().asVector().cross(xAxis);
        unique.sort((left, right) -> {
            double leftAngle = Math.atan2(left.subtract(centroid).dot(yAxis), left.subtract(centroid).dot(xAxis));
            double rightAngle = Math.atan2(right.subtract(centroid).dot(yAxis), right.subtract(centroid).dot(xAxis));
            return Double.compare(leftAngle, rightAngle);
        });
        return List.copyOf(unique);
    }

    private List<CartesianPoint> uniquePoints(List<CartesianPoint> points) {
        List<CartesianPoint> unique = new ArrayList<>();
        for (CartesianPoint point : points) {
            boolean duplicate = false;
            for (CartesianPoint existing : unique) {
                if (existing.distanceTo(point) <= 1.0e-9) {
                    duplicate = true;
                    break;
                }
            }
            if (!duplicate) {
                unique.add(point);
            }
        }
        return unique;
    }

    private CartesianPoint averagePoint(List<CartesianPoint> points) {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        for (CartesianPoint point : points) {
            x += point.x();
            y += point.y();
            z += point.z();
        }
        double scale = 1.0 / points.size();
        return new CartesianPoint(x * scale, y * scale, z * scale);
    }

    private Vector3 planeBasis(Direction3 normal) {
        Vector3 reference = Math.abs(normal.z()) < 0.9 ? new Vector3(0.0, 0.0, 1.0) : new Vector3(1.0, 0.0, 0.0);
        Vector3 xAxis = normal.asVector().cross(reference);
        if (xAxis.isZero()) {
            xAxis = normal.asVector().cross(new Vector3(0.0, 1.0, 0.0));
        }
        return xAxis.normalize().asVector();
    }

    private CartesianPoint rotateAroundAxis(CartesianPoint point, Axis1Placement axis, double angle) {
        Vector3 axisVector = axis.axis().asVector();
        Vector3 relative = point.subtract(axis.location());
        Vector3 parallel = axisVector.scale(relative.dot(axisVector));
        Vector3 radial = relative.subtract(parallel);
        Vector3 rotatedRadial = radial.scale(Math.cos(angle))
                .add(axisVector.cross(radial).scale(Math.sin(angle)));
        return axis.location().add(parallel.add(rotatedRadial));
    }

    private double distanceToAxis(CartesianPoint point, Axis1Placement axis) {
        Vector3 relative = point.subtract(axis.location());
        Vector3 parallel = axis.axis().asVector().scale(relative.dot(axis.axis().asVector()));
        return relative.subtract(parallel).norm();
    }

    private Vector3 sweepDirectionAtSection(List<CartesianPoint> section, Axis1Placement axis, boolean positiveAngle) {
        CartesianPoint sample = section.getFirst();
        Vector3 radial = radialFromAxis(sample, axis);
        Vector3 tangent = axis.axis().asVector().cross(radial);
        if (tangent.isZero()) {
            tangent = axis.axis().asVector();
        }
        return positiveAngle ? tangent : tangent.scale(-1.0);
    }

    private Vector3 radialFromAxis(CartesianPoint point, Axis1Placement axis) {
        Vector3 relative = point.subtract(axis.location());
        Vector3 parallel = axis.axis().asVector().scale(relative.dot(axis.axis().asVector()));
        return relative.subtract(parallel);
    }

    private Direction3 polygonNormal(List<CartesianPoint> points, Vector3 fallback) {
        Vector3 normal = new Vector3(0.0, 0.0, 0.0);
        for (int index = 0; index < points.size(); index++) {
            CartesianPoint current = points.get(index);
            CartesianPoint next = points.get((index + 1) % points.size());
            normal = normal.add(new Vector3(
                    (current.y() - next.y()) * (current.z() + next.z()),
                    (current.z() - next.z()) * (current.x() + next.x()),
                    (current.x() - next.x()) * (current.y() + next.y())
            ));
        }
        if (normal.isZero()) {
            normal = fallback;
        }
        if (normal.isZero()) {
            throw new UnsupportedGeometryException("revolved face normal is degenerate");
        }
        if (!fallback.isZero() && normal.dot(fallback) < 0.0) {
            normal = normal.scale(-1.0);
        }
        return normal.normalize();
    }

    private Direction3 quadNormal(CartesianPoint a, CartesianPoint b, CartesianPoint c, CartesianPoint d) {
        Vector3 normal = b.subtract(a).cross(c.subtract(a));
        if (normal.isZero()) {
            normal = c.subtract(a).cross(d.subtract(a));
        }
        if (normal.isZero()) {
            throw new UnsupportedGeometryException("revolved side face is degenerate");
        }
        return normal.normalize();
    }

    private Solid transformSolid(Solid solid, StepCartesianTransformationOperator transformation) {
        return new Solid(
                transformShell(solid.outerShell(), transformation),
                solid.voidShells().stream()
                        .map(voidShell -> transformShell(voidShell, transformation))
                        .toList()
        );
    }

    private Shell transformShell(Shell shell, StepCartesianTransformationOperator transformation) {
        return new Shell(
                shell.faces().stream()
                        .map(face -> transformFace(face, transformation))
                        .toList(),
                shell.closed()
        );
    }

    private Face transformFace(Face face, StepCartesianTransformationOperator transformation) {
        return new Face(
                transformSurfaceGeometry(face.surface(), transformation),
                face.bounds().stream()
                        .map(bound -> transformFaceBound(bound, transformation))
                        .toList(),
                face.sameSense()
        );
    }

    private FaceBound transformFaceBound(FaceBound bound, StepCartesianTransformationOperator transformation) {
        return bound.outer()
                ? FaceBound.outer(transformLoop(bound.loop(), transformation), bound.orientation())
                : FaceBound.inner(transformLoop(bound.loop(), transformation), bound.orientation());
    }

    private Loop transformLoop(Loop loop, StepCartesianTransformationOperator transformation) {
        return switch (loop) {
            case EdgeLoop edgeLoop -> new EdgeLoop(edgeLoop.edges().stream()
                    .map(edge -> transformOrientedEdge(edge, transformation))
                    .toList());
            case VertexLoop vertexLoop -> new VertexLoop(transformVertex(vertexLoop.vertex(), transformation));
            case PolyLoop polyLoop -> new PolyLoop(polyLoop.points().stream()
                    .map(point -> transformPoint3(point, transformation))
                    .toList());
            default -> throw new UnsupportedGeometryException("loop replica for " + loopTypeName(loop) + " is unsupported");
        };
    }

    private OrientedEdge transformOrientedEdge(OrientedEdge edge, StepCartesianTransformationOperator transformation) {
        return new OrientedEdge(transformEdge(edge.edge(), transformation), edge.orientation());
    }

    private Edge transformEdge(Edge edge, StepCartesianTransformationOperator transformation) {
        return new Edge(
                transformVertex(edge.start(), transformation),
                transformVertex(edge.end(), transformation),
                transformCurve3(edge.curve(), transformation),
                edge.sameSense()
        );
    }

    private Vertex transformVertex(Vertex vertex, StepCartesianTransformationOperator transformation) {
        return new Vertex(transformPoint3(vertex.point(), transformation));
    }

    private BSplineCurve3 buildImplicitBSplineCurve3(StepEntity entity) {
        BSplineCurve3 existing = bsplineCurves.get(entity.id());
        if (existing != null) {
            return existing;
        }
        ImplicitBSplineCurveData spline = implicitBSplineCurveData(entity);
        List<CartesianPoint> controlPoints = spline.controlPoints().stream().map(point -> buildPoint(point.id())).toList();
        BSplineCurve3 built = new BSplineCurve3(
                spline.degree(),
                controlPoints,
                spline.knotMultiplicities(),
                spline.knots()
        );
        bsplineCurves.put(entity.id(), built);
        return built;
    }

    private Curve3 buildCurve3(StepEntity curve) {
        return switch (curve) {
            case StepLine line -> buildLine(line.id());
            case StepCircle circle -> buildCircle(circle.id());
            case StepEllipse ellipse -> buildEllipse(ellipse.id());
            case StepPolyline polyline -> buildPolyline(polyline.id());
            case StepBezierCurve spline -> buildImplicitBSplineCurve3(spline);
            case StepUniformCurve spline -> buildImplicitBSplineCurve3(spline);
            case StepQuasiUniformCurve spline -> buildImplicitBSplineCurve3(spline);
            case StepPiecewiseBezierCurve spline -> buildImplicitBSplineCurve3(spline);
            case StepBSplineCurveWithKnots spline -> buildBSplineCurve(spline.id());
            case StepRationalBSplineCurve spline -> buildRationalBSplineCurve3(spline.id());
            case StepSurfaceCurve surfaceCurve -> buildSurfaceCurve(surfaceCurve.id());
            case StepSeamCurve seamCurve -> buildSeamCurve(seamCurve.id());
            case StepTrimmedCurve trimmedCurve -> buildTrimmedCurve(trimmedCurve.id());
            case StepCompositeCurve compositeCurve -> buildCompositeCurve(compositeCurve.id());
            case StepCompositeCurveOnSurface compositeCurveOnSurface -> buildCompositeCurve(compositeCurveOnSurface.id());
            case StepConicCurve conic -> buildConicCurve3(conic);
            case StepOffsetCurve2D offsetCurve2D -> liftCurve2(buildOffsetCurve2(offsetCurve2D.id()));
            case StepOffsetCurve3D offsetCurve3D -> buildOffsetCurve3(offsetCurve3D.id());
            case StepOrientedCurve orientedCurve -> buildCurve3(orientedCurve.curveElement());
            case StepGeometricReplica replica -> buildReplicaCurve3(replica);
            case StepAnnotationCurveOccurrence occurrence -> buildCurve3(occurrence.item());
            case StepDimensionCurve dimensionCurve -> buildCurve3(dimensionCurve.item());
            case StepLeaderCurve leaderCurve -> buildCurve3(leaderCurve.item());
            case StepProjectionCurve projectionCurve -> buildCurve3(projectionCurve.item());
            case StepDraughtingAnnotationOccurrence annotationOccurrence -> buildCurve3(annotationOccurrence.item());
            case StepTerminatorSymbol terminatorSymbol -> buildCurve3(terminatorSymbol.annotatedCurve());
            case StepClothoid clothoid -> buildClothoidCurve(clothoid);
            case StepIndexedPolyCurve polyCurve -> buildIndexedPolyCurve3(polyCurve);
            case StepDegenerateCurve degenerateCurve -> buildDegenerateCurve3(degenerateCurve);
            default -> throw new UnsupportedGeometryException(
                    "surface directrix requires LINE, CIRCLE, ELLIPSE, POLYLINE, BEZIER_CURVE, UNIFORM_CURVE, QUASI_UNIFORM_CURVE, PIECEWISE_BEZIER_CURVE, COMPOSITE_CURVE, B_SPLINE_CURVE_WITH_KNOTS, RATIONAL_B_SPLINE_CURVE, SURFACE_CURVE, SEAM_CURVE or TRIMMED_CURVE"
            );
        };
    }

    private Curve3 buildClothoidCurve(StepClothoid clothoid) {
        // Return proper Clothoid3 geometry object
        return buildClothoid(clothoid.id());
    }

    private double fresnelC(double x) {
        // Fresnel cosine integral approximation
        // C(x) ≈ integral_0^x cos(pi*t^2/2) dt
        // Simplified approximation for small x
        return x * Math.cos(Math.PI * x / 2.0) / 2.0;
    }

    private double fresnelS(double x) {
        // Fresnel sine integral approximation
        // S(x) ≈ integral_0^x sin(pi*t^2/2) dt
        return x * Math.sin(Math.PI * x / 2.0) / 2.0;
    }

    private Curve3 buildIndexedPolyCurve3(StepIndexedPolyCurve polyCurve) {
        // Indexed poly curve is defined by indices into a point list
        List<StepCartesianPoint> stepPoints = polyCurve.points();
        List<Integer> indices = polyCurve.indices();
        List<CartesianPoint> points = indices.stream()
                .map(index -> buildPoint(stepPoints.get(index).id()))
                .toList();
        if (polyCurve.closed() && !points.isEmpty()) {
            points = new ArrayList<>(points);
            points.add(points.getFirst());
            points = List.copyOf(points);
        }
        return new Polyline3(points);
    }

    private Curve3 buildDegenerateCurve3(StepDegenerateCurve degenerateCurve) {
        // Degenerate curve collapses to a point
        Curve3 basis = buildCurve3(degenerateCurve.basisCurve());
        List<CartesianPoint> sampledPoints = sampleCurve3(basis, 2);
        if (sampledPoints.isEmpty()) {
            throw new UnsupportedGeometryException("DEGENERATE_CURVE basis curve has no sample points");
        }
        // Return a degenerate curve at the first sample point
        CartesianPoint point = sampledPoints.getFirst();
        return new DegenerateCurve3(point);
    }

    private ImplicitBSplineCurveData implicitBSplineCurveData(StepEntity entity) {
        if (entity instanceof StepBezierCurve curve) {
            return implicitBezierCurve(curve.degree(), curve.controlPoints(), stepEntityTypeName(entity));
        }
        if (entity instanceof StepUniformCurve curve) {
            return implicitUniformCurve(curve.degree(), curve.controlPoints(), stepEntityTypeName(entity));
        }
        if (entity instanceof StepQuasiUniformCurve curve) {
            return implicitQuasiUniformCurve(curve.degree(), curve.controlPoints(), stepEntityTypeName(entity));
        }
        if (entity instanceof StepPiecewiseBezierCurve curve) {
            return implicitPiecewiseBezierCurve(curve.degree(), curve.controlPoints(), stepEntityTypeName(entity));
        }
        throw new UnsupportedGeometryException(stepEntityTypeName(entity) + " implicit knot data is unsupported");
    }

    private ImplicitBSplineSurfaceData implicitBSplineSurfaceData(StepEntity entity) {
        if (entity instanceof StepBezierSurface surface) {
            return implicitBezierSurface(surface.uDegree(), surface.vDegree(), surface.controlPoints(), stepEntityTypeName(entity));
        }
        if (entity instanceof StepUniformSurface surface) {
            return implicitUniformSurface(surface.uDegree(), surface.vDegree(), surface.controlPoints(), stepEntityTypeName(entity));
        }
        if (entity instanceof StepQuasiUniformSurface surface) {
            return implicitQuasiUniformSurface(surface.uDegree(), surface.vDegree(), surface.controlPoints(), stepEntityTypeName(entity));
        }
        if (entity instanceof StepPiecewiseBezierSurface surface) {
            return implicitPiecewiseBezierSurface(surface.uDegree(), surface.vDegree(), surface.controlPoints(), stepEntityTypeName(entity));
        }
        throw new UnsupportedGeometryException(stepEntityTypeName(entity) + " implicit knot data is unsupported");
    }

    public Curve3 buildOffsetCurve3(int id) {
        StepOffsetCurve3D offsetCurve = requireEntity(id, StepOffsetCurve3D.class, "OFFSET_CURVE_3D");
        Curve3 basisCurve = buildCurve3(offsetCurve.basisCurve());
        return approximateOffsetCurve3(basisCurve, offsetCurve.distance(), buildDirection(offsetCurve.refDirection().id()));
    }

    private Curve3 buildReplicaCurve3(StepGeometricReplica replica) {
        Curve3 parent = buildCurve3(replica.parent());
        return transformCurve3(parent, replica.transformation());
    }

    private Curve2 buildReplicaCurve2(StepGeometricReplica replica) {
        Object built = buildCurve2(replica.parent());
        if (!(built instanceof Curve2 parent)) {
            throw new UnsupportedGeometryException(replica.entityName() + " parent is not a supported 2D curve");
        }
        return transformCurve2(parent, replica.transformation());
    }

    private Curve3 buildConicCurve3(StepConicCurve conic) {
        if (!(conic.position() instanceof StepAxis2Placement3D placement3D)) {
            throw new UnsupportedGeometryException("3D conic curve for " + conic.entityName() + " requires AXIS2_PLACEMENT_3D");
        }
        return switch (conic.entityName()) {
            case "PARABOLA" -> buildParabola(conic.id());
            case "HYPERBOLA" -> buildHyperbola(conic.id());
            case "DEGENERATE_CONIC" -> new DegenerateCurve3(buildPlacement(placement3D.id()).location());
            default -> throw new UnsupportedGeometryException("surface directrix for " + conic.entityName() + " is unsupported");
        };
    }

    private Curve2 buildConicCurve2(StepConicCurve conic) {
        if (!(conic.position() instanceof StepAxis2Placement2D placement2D)) {
            throw new UnsupportedGeometryException("2D conic curve for " + conic.entityName() + " requires AXIS2_PLACEMENT_2D");
        }
        Point2 origin = buildPoint2(placement2D.location().id());
        Direction2 xDirection = buildDirection2(placement2D.refDirection().id());
        return switch (conic.entityName()) {
            case "PARABOLA" -> buildParabola2(origin, xDirection, conic.parameters());
            case "HYPERBOLA" -> buildHyperbola2(origin, xDirection, conic.parameters());
            case "DEGENERATE_CONIC" -> new Polyline2(List.of(origin, origin));
            default -> throw new UnsupportedGeometryException("PCURVE 2D item for " + conic.entityName() + " is unsupported");
        };
    }

    private Parabola2 buildParabola2(Point2 origin, Direction2 xDirection, List<Double> parameters) {
        if (parameters.isEmpty()) {
            throw new UnsupportedGeometryException("PARABOLA requires focal distance");
        }
        double focalDistance = parameters.getFirst();
        if (!Double.isFinite(focalDistance) || focalDistance <= Epsilon.EPS) {
            throw new UnsupportedGeometryException("PARABOLA focal distance must be positive");
        }
        // Parabola vertex is at origin, axis direction is yDirection (perpendicular to x)
        Direction2 yDirection = new Direction2(-xDirection.y(), xDirection.x());
        return new Parabola2(origin, yDirection, focalDistance);
    }

    private Hyperbola2 buildHyperbola2(Point2 origin, Direction2 xDirection, List<Double> parameters) {
        if (parameters.size() < 2) {
            throw new UnsupportedGeometryException("HYPERBOLA requires semi-axis and semi-imaginary-axis");
        }
        double semiAxisA = parameters.get(0);
        double semiAxisB = parameters.get(1);
        if (!Double.isFinite(semiAxisA) || !Double.isFinite(semiAxisB)
                || semiAxisA <= Epsilon.EPS || semiAxisB <= Epsilon.EPS) {
            throw new UnsupportedGeometryException("HYPERBOLA axes must be positive");
        }
        return new Hyperbola2(origin, xDirection, semiAxisA, semiAxisB);
    }

    private List<CartesianPoint> sampleParabolaPoints3(Axis2Placement3D placement, List<Double> parameters) {
        if (parameters.isEmpty()) {
            throw new UnsupportedGeometryException("PARABOLA requires focal distance");
        }
        double focalDistance = parameters.getFirst();
        if (!Double.isFinite(focalDistance) || focalDistance <= Epsilon.EPS) {
            throw new UnsupportedGeometryException("PARABOLA focal distance must be positive");
        }
        double yExtent = Math.max(1.0, focalDistance * 4.0);
        int segments = 96;
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        Vector3 xAxis = placement.xDirection().asVector();
        Vector3 yAxis = placement.yDirection().asVector();
        for (int index = 0; index <= segments; index++) {
            double t = -yExtent + (2.0 * yExtent * index) / segments;
            double x = (t * t) / (4.0 * focalDistance);
            points.add(placement.location().add(xAxis.scale(x).add(yAxis.scale(t))));
        }
        return List.copyOf(points);
    }

    private List<CartesianPoint> sampleHyperbolaPoints3(Axis2Placement3D placement, List<Double> parameters) {
        if (parameters.size() < 2) {
            throw new UnsupportedGeometryException("HYPERBOLA requires semi-axis and semi-imaginary-axis");
        }
        double semiAxis = parameters.get(0);
        double semiImaginaryAxis = parameters.get(1);
        if (!Double.isFinite(semiAxis)
                || !Double.isFinite(semiImaginaryAxis)
                || semiAxis <= Epsilon.EPS
                || semiImaginaryAxis <= Epsilon.EPS) {
            throw new UnsupportedGeometryException("HYPERBOLA axes must be positive");
        }
        double extent = 1.75;
        int segments = 96;
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        Vector3 xAxis = placement.xDirection().asVector();
        Vector3 yAxis = placement.yDirection().asVector();
        for (int index = 0; index <= segments; index++) {
            double t = -extent + (2.0 * extent * index) / segments;
            double x = semiAxis * Math.cosh(t);
            double y = semiImaginaryAxis * Math.sinh(t);
            points.add(placement.location().add(xAxis.scale(x).add(yAxis.scale(y))));
        }
        return List.copyOf(points);
    }

    private List<Point2> sampleParabolaPoints2(Point2 origin, Direction2 xDirection, List<Double> parameters) {
        if (parameters.isEmpty()) {
            throw new UnsupportedGeometryException("PARABOLA requires focal distance");
        }
        double focalDistance = parameters.getFirst();
        if (!Double.isFinite(focalDistance) || focalDistance <= Epsilon.EPS) {
            throw new UnsupportedGeometryException("PARABOLA focal distance must be positive");
        }
        double yExtent = Math.max(1.0, focalDistance * 4.0);
        int segments = 96;
        List<Point2> points = new ArrayList<>(segments + 1);
        Vector2 xAxis = xDirection.asVector();
        Vector2 yAxis = new Vector2(-xAxis.y(), xAxis.x());
        for (int index = 0; index <= segments; index++) {
            double t = -yExtent + (2.0 * yExtent * index) / segments;
            double x = (t * t) / (4.0 * focalDistance);
            points.add(origin.add(xAxis.scale(x).add(yAxis.scale(t))));
        }
        return List.copyOf(points);
    }

    private List<Point2> sampleHyperbolaPoints2(Point2 origin, Direction2 xDirection, List<Double> parameters) {
        if (parameters.size() < 2) {
            throw new UnsupportedGeometryException("HYPERBOLA requires semi-axis and semi-imaginary-axis");
        }
        double semiAxis = parameters.get(0);
        double semiImaginaryAxis = parameters.get(1);
        if (!Double.isFinite(semiAxis)
                || !Double.isFinite(semiImaginaryAxis)
                || semiAxis <= Epsilon.EPS
                || semiImaginaryAxis <= Epsilon.EPS) {
            throw new UnsupportedGeometryException("HYPERBOLA axes must be positive");
        }
        double extent = 1.75;
        int segments = 96;
        List<Point2> points = new ArrayList<>(segments + 1);
        Vector2 xAxis = xDirection.asVector();
        Vector2 yAxis = new Vector2(-xAxis.y(), xAxis.x());
        for (int index = 0; index <= segments; index++) {
            double t = -extent + (2.0 * extent * index) / segments;
            double x = semiAxis * Math.cosh(t);
            double y = semiImaginaryAxis * Math.sinh(t);
            points.add(origin.add(xAxis.scale(x).add(yAxis.scale(y))));
        }
        return List.copyOf(points);
    }

    private Plane buildSupportedPlaneGeometry(StepEntity geometry, String faceType) {
        SurfaceGeometry surface = buildSupportedFaceGeometry(geometry, faceType);
        return surface instanceof Plane plane ? plane : null;
    }

    private SurfaceGeometry buildSupportedFaceGeometry(StepEntity geometry, String faceType) {
        if (geometry instanceof StepPlane plane) {
            return buildPlane(plane.id());
        }
        if (geometry instanceof StepCylindricalSurface cylindricalSurface) {
            return buildCylindricalSurface(cylindricalSurface.id());
        }
        if (geometry instanceof StepConicalSurface conicalSurface) {
            return buildConicalSurface(conicalSurface.id());
        }
        if (geometry instanceof StepSphericalSurface sphericalSurface) {
            return buildSphericalSurface(sphericalSurface.id());
        }
        if (geometry instanceof StepToroidalSurface toroidalSurface) {
            return buildToroidalSurface(toroidalSurface.id());
        }
        if (geometry instanceof StepDegenerateToroidalSurface degenerateToroidalSurface) {
            return buildDegenerateToroidalSurface(degenerateToroidalSurface.id());
        }
        if (geometry instanceof StepSurfaceOfLinearExtrusion extrusionSurface) {
            return buildSurfaceOfLinearExtrusion(extrusionSurface.id());
        }
        if (geometry instanceof StepSurfaceOfRevolution revolutionSurface) {
            return buildSurfaceOfRevolution(revolutionSurface.id());
        }
        if (geometry instanceof StepBezierSurface splineSurface) {
            return buildBezierSurface(splineSurface.id());
        }
        if (geometry instanceof StepUniformSurface splineSurface) {
            return buildUniformSurface(splineSurface.id());
        }
        if (geometry instanceof StepQuasiUniformSurface splineSurface) {
            return buildQuasiUniformSurface(splineSurface.id());
        }
        if (geometry instanceof StepPiecewiseBezierSurface splineSurface) {
            return buildPiecewiseBezierSurface(splineSurface.id());
        }
        if (geometry instanceof StepBSplineSurfaceWithKnots splineSurface) {
            return buildBSplineSurface(splineSurface.id());
        }
        if (geometry instanceof StepRationalBSplineSurface rationalSplineSurface) {
            return buildRationalBSplineSurface(rationalSplineSurface.id());
        }
        if (geometry instanceof StepRectangularTrimmedSurface trimmedSurface) {
            buildRectangularTrimmedSurface(trimmedSurface.id());
            return buildSupportedFaceGeometry(trimmedSurface.basisSurface(), faceType);
        }
        if (geometry instanceof StepCurveBoundedSurface boundedSurface) {
            for (StepEntity boundary : boundedSurface.boundaries()) {
                if (boundary instanceof StepPcurve pcurve) {
                    buildPcurve2(pcurve.id());
                } else if (boundary instanceof StepCompositeCurveOnSurface compositeCurveOnSurface) {
                    boolean built2d = true;
                    for (StepCompositeCurveSegment segment : compositeCurveOnSurface.segments()) {
                        try {
                            buildCurve2(segment.parentCurve());
                        } catch (UnsupportedGeometryException ex) {
                            built2d = false;
                            break;
                        }
                    }
                    if (!built2d) {
                        buildCompositeCurve(compositeCurveOnSurface.id());
                    }
                } else {
                    buildCurve3(boundary);
                }
            }
            return buildSupportedFaceGeometry(boundedSurface.basisSurface(), faceType);
        }
        if (geometry instanceof StepOrientedSurface orientedSurface) {
            SurfaceGeometry base = buildSupportedFaceGeometry(orientedSurface.surfaceElement(), faceType);
            if (base == null) {
                return null;
            }
            if (base instanceof Plane plane && !orientedSurface.orientation()) {
                return new Plane(plane.origin(), plane.normal().reverse());
            }
            return base;
        }
        if (geometry instanceof StepOffsetSurface offsetSurface) {
            return offsetSupportedSurfaceGeometry(offsetSurface, faceType);
        }
        if (geometry instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName())) {
            String replicaRestriction = unsupportedReplicaSurfaceTransformation(replica.transformation());
            if (replicaRestriction != null) {
                return null;
            }
            SurfaceGeometry base = buildSupportedFaceGeometry(replica.parent(), faceType);
            if (base == null) {
                return null;
            }
            return transformSurfaceGeometry(base, replica.transformation());
        }
        if (geometry instanceof StepRuledSurface ruledSurface) {
            return buildRuledSurfaceGeometry(ruledSurface);
        }
        if (geometry instanceof StepSurfaceOfConstantRadius constantRadiusSurface) {
            return buildSurfaceOfConstantRadiusGeometry(constantRadiusSurface, faceType);
        }
        if (geometry instanceof StepSurfacePatch surfacePatch) {
            return buildSurfacePatchGeometry(surfacePatch, faceType);
        }
        if (geometry instanceof StepRectangularCompositeSurface compositeSurface) {
            return buildRectangularCompositeSurfaceGeometry(compositeSurface, faceType);
        }
        return null;
    }

    private SurfaceGeometry buildRuledSurfaceGeometry(StepRuledSurface ruledSurface) {
        // Ruled surface is defined by two directrix curves
        Axis2Placement3D position = buildPlacement(ruledSurface.position().id());
        Curve3 directrix1 = buildCurve3(ruledSurface.directrix1());
        Curve3 directrix2 = buildCurve3(ruledSurface.directrix2());
        // Create ruled surface geometry
        return new RuledSurface3(directrix1, directrix2);
    }

    private SurfaceGeometry buildSurfaceOfConstantRadiusGeometry(StepSurfaceOfConstantRadius surface, String faceType) {
        // Surface of constant radius: sweep a surface along a path with constant radius
        SurfaceGeometry sweptSurface = buildSupportedFaceGeometry(surface.sweptSurface(), faceType);
        if (sweptSurface == null) {
            return null;
        }
        double radius = surface.radius();
        if (radius <= 0.0) {
            return null;
        }
        return new SurfaceOfConstantRadius3(sweptSurface, radius);
    }

    private SurfaceGeometry buildSurfacePatchGeometry(StepSurfacePatch patch, String faceType) {
        SurfaceGeometry basisSurface = buildSupportedFaceGeometry(patch.basisSurface(), faceType);
        if (basisSurface == null) {
            return null;
        }
        // Surface patch is just a bounded portion of a surface
        // The sameSense flag determines orientation
        if (!patch.sameSense()) {
            // Reverse orientation if needed
            if (basisSurface instanceof Plane plane) {
                return new Plane(plane.origin(), plane.normal().reverse());
            }
        }
        return basisSurface;
    }

    private SurfaceGeometry buildRectangularCompositeSurfaceGeometry(StepRectangularCompositeSurface surface, String faceType) {
        SurfaceGeometry parentSurface = buildSupportedFaceGeometry(surface.parentSurface(), faceType);
        if (parentSurface == null) {
            return null;
        }
        // Rectangular composite surface is a bounded rectangular region of the parent surface
        // The u1, u2, v1, v2 parameters define the boundaries
        // For now, return the parent surface - proper implementation would trim to bounds
        return parentSurface;
    }

    private SurfaceGeometry offsetSupportedSurfaceGeometry(StepOffsetSurface offsetSurface, String faceType) {
        buildOffsetSurface(offsetSurface.id());
        SurfaceGeometry base = buildSupportedFaceGeometry(offsetSurface.basisSurface(), faceType);
        if (base == null) {
            return null;
        }
        if (base instanceof Plane plane) {
            return new Plane(
                    plane.origin().add(plane.normal().asVector().scale(offsetSurface.distance())),
                    plane.normal());
        }
        if (base instanceof CylindricalSurface cylindricalSurface) {
            return new CylindricalSurface(
                    cylindricalSurface.position(),
                    cylindricalSurface.radius() + offsetSurface.distance());
        }
        if (base instanceof SphericalSurface sphericalSurface) {
            return new SphericalSurface(
                    sphericalSurface.position(),
                    sphericalSurface.radius() + offsetSurface.distance());
        }
        if (base instanceof ConicalSurface conicalSurface) {
            return offsetConicalSurface(conicalSurface, offsetSurface.distance());
        }
        if (base instanceof ToroidalSurface toroidalSurface) {
            return new ToroidalSurface(
                    toroidalSurface.position(),
                    toroidalSurface.majorRadius(),
                    toroidalSurface.minorRadius() + offsetSurface.distance());
        }
        if (base instanceof OffsetSurface3 nestedOffsetSurface) {
            return new OffsetSurface3(
                    nestedOffsetSurface.basisSurface(),
                    nestedOffsetSurface.distance() + offsetSurface.distance());
        }
        return new OffsetSurface3(base, offsetSurface.distance());
    }

    private ConicalSurface offsetConicalSurface(ConicalSurface conicalSurface, double distance) {
        double semiAngle = conicalSurface.semiAngle();
        double radialOffset = distance * Math.cos(semiAngle);
        double axisOffset = -distance * Math.sin(semiAngle);
        Axis2Placement3D position = conicalSurface.position();
        return new ConicalSurface(
                new Axis2Placement3D(
                        position.location().add(position.axis().asVector().scale(axisOffset)),
                        position.axis(),
                        position.refDirection()),
                conicalSurface.radius() + radialOffset,
                semiAngle);
    }

    private void buildSupportedSurfaceGeometry(StepEntity geometry) {
        if (geometry instanceof StepPlane plane) {
            buildPlane(plane.id());
            return;
        }
        if (geometry instanceof StepCylindricalSurface cylindricalSurface) {
            buildCylindricalSurface(cylindricalSurface.id());
            return;
        }
        if (geometry instanceof StepConicalSurface conicalSurface) {
            buildConicalSurface(conicalSurface.id());
            return;
        }
        if (geometry instanceof StepSphericalSurface sphericalSurface) {
            buildSphericalSurface(sphericalSurface.id());
            return;
        }
        if (geometry instanceof StepToroidalSurface toroidalSurface) {
            buildToroidalSurface(toroidalSurface.id());
            return;
        }
        if (geometry instanceof StepDegenerateToroidalSurface degenerateToroidalSurface) {
            buildDegenerateToroidalSurface(degenerateToroidalSurface.id());
            return;
        }
        if (geometry instanceof StepSurfaceOfLinearExtrusion extrusionSurface) {
            buildSurfaceOfLinearExtrusion(extrusionSurface.id());
            return;
        }
        if (geometry instanceof StepSurfaceOfRevolution revolutionSurface) {
            buildSurfaceOfRevolution(revolutionSurface.id());
            return;
        }
        if (geometry instanceof StepBezierSurface splineSurface) {
            buildBezierSurface(splineSurface.id());
            return;
        }
        if (geometry instanceof StepUniformSurface splineSurface) {
            buildUniformSurface(splineSurface.id());
            return;
        }
        if (geometry instanceof StepQuasiUniformSurface splineSurface) {
            buildQuasiUniformSurface(splineSurface.id());
            return;
        }
        if (geometry instanceof StepPiecewiseBezierSurface splineSurface) {
            buildPiecewiseBezierSurface(splineSurface.id());
            return;
        }
        if (geometry instanceof StepBSplineSurfaceWithKnots splineSurface) {
            buildBSplineSurface(splineSurface.id());
            return;
        }
        if (geometry instanceof StepRationalBSplineSurface rationalSplineSurface) {
            buildRationalBSplineSurface(rationalSplineSurface.id());
            return;
        }
        if (geometry instanceof StepRectangularTrimmedSurface trimmedSurface) {
            buildRectangularTrimmedSurface(trimmedSurface.id());
            return;
        }
        if (geometry instanceof StepCurveBoundedSurface boundedSurface) {
            buildCurveBoundedSurface(boundedSurface.id());
            return;
        }
        if (geometry instanceof StepOrientedSurface orientedSurface) {
            buildOrientedSurface(orientedSurface.id());
            return;
        }
        if (geometry instanceof StepOffsetSurface offsetSurface) {
            buildOffsetSurface(offsetSurface.id());
            return;
        }
        if (geometry instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName())) {
            buildSurfaceReplica(replica.id());
            return;
        }
        throw new UnsupportedGeometryException("surface geometry " + stepEntityTypeName(geometry) + " is unsupported");
    }

    private void buildSurfaceBoundaryCurve(StepEntity boundary) {
        if (boundary instanceof StepPcurve pcurve) {
            buildPcurve2(pcurve.id());
            return;
        }
        if (boundary instanceof StepCompositeCurveOnSurface compositeCurveOnSurface) {
            boolean built2d = true;
            for (StepCompositeCurveSegment segment : compositeCurveOnSurface.segments()) {
                try {
                    buildCurve2(segment.parentCurve());
                } catch (UnsupportedGeometryException ex) {
                    built2d = false;
                    break;
                }
            }
            if (!built2d) {
                buildCompositeCurve(compositeCurveOnSurface.id());
            }
            return;
        }
        buildCurve3(boundary);
    }

    private String describeUnsupportedFaceGeometry(StepEntity geometry) {
        if (geometry instanceof StepCylindricalSurface cylindricalSurface) {
            buildCylindricalSurface(cylindricalSurface.id());
            return "CYLINDRICAL_SURFACE";
        }
        if (geometry instanceof StepConicalSurface conicalSurface) {
            buildConicalSurface(conicalSurface.id());
            return "CONICAL_SURFACE";
        }
        if (geometry instanceof StepSphericalSurface sphericalSurface) {
            buildSphericalSurface(sphericalSurface.id());
            return "SPHERICAL_SURFACE";
        }
        if (geometry instanceof StepSurfaceOfLinearExtrusion extrusionSurface) {
            buildSurfaceOfLinearExtrusion(extrusionSurface.id());
            return "SURFACE_OF_LINEAR_EXTRUSION";
        }
        if (geometry instanceof StepSurfaceOfRevolution revolutionSurface) {
            buildSurfaceOfRevolution(revolutionSurface.id());
            return "SURFACE_OF_REVOLUTION";
        }
        if (geometry instanceof StepBezierSurface splineSurface) {
            buildBezierSurface(splineSurface.id());
            return "BEZIER_SURFACE";
        }
        if (geometry instanceof StepUniformSurface splineSurface) {
            buildUniformSurface(splineSurface.id());
            return "UNIFORM_SURFACE";
        }
        if (geometry instanceof StepQuasiUniformSurface splineSurface) {
            buildQuasiUniformSurface(splineSurface.id());
            return "QUASI_UNIFORM_SURFACE";
        }
        if (geometry instanceof StepPiecewiseBezierSurface splineSurface) {
            buildPiecewiseBezierSurface(splineSurface.id());
            return "PIECEWISE_BEZIER_SURFACE";
        }
        if (geometry instanceof StepBSplineSurfaceWithKnots splineSurface) {
            buildBSplineSurface(splineSurface.id());
            return "B_SPLINE_SURFACE_WITH_KNOTS";
        }
        if (geometry instanceof StepRationalBSplineSurface rationalSplineSurface) {
            buildRationalBSplineSurface(rationalSplineSurface.id());
            return "RATIONAL_B_SPLINE_SURFACE";
        }
        if (geometry instanceof StepToroidalSurface toroidalSurface) {
            buildToroidalSurface(toroidalSurface.id());
            return "TOROIDAL_SURFACE";
        }
        if (geometry instanceof StepDegenerateToroidalSurface degenerateToroidalSurface) {
            buildDegenerateToroidalSurface(degenerateToroidalSurface.id());
            return "DEGENERATE_TOROIDAL_SURFACE";
        }
        if (geometry instanceof StepRectangularTrimmedSurface trimmedSurface) {
            buildRectangularTrimmedSurface(trimmedSurface.id());
            return describeUnsupportedFaceGeometry(trimmedSurface.basisSurface());
        }
        if (geometry instanceof StepCurveBoundedSurface boundedSurface) {
            buildCurveBoundedSurface(boundedSurface.id());
            return describeUnsupportedFaceGeometry(boundedSurface.basisSurface());
        }
        if (geometry instanceof StepOrientedSurface orientedSurface) {
            buildOrientedSurface(orientedSurface.id());
            return describeUnsupportedFaceGeometry(orientedSurface.surfaceElement());
        }
        if (geometry instanceof StepOffsetSurface offsetSurface) {
            buildOffsetSurface(offsetSurface.id());
            return describeUnsupportedFaceGeometry(offsetSurface.basisSurface());
        }
        if (geometry instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName())) {
            buildSurfaceReplica(replica.id());
            return describeUnsupportedFaceGeometry(replica.parent());
        }
        return null;
    }

    private ImplicitBSplineCurveData implicitBezierCurve(int degree, List<StepCartesianPoint> controlPoints, String typeName) {
        validateImplicitCurveData(degree, controlPoints, typeName);
        if (controlPoints.size() != degree + 1) {
            throw new UnsupportedGeometryException(typeName + " requires controlPointCount = degree + 1");
        }
        return new ImplicitBSplineCurveData(
                degree,
                controlPoints,
                List.of(degree + 1, degree + 1),
                List.of(0.0, 1.0)
        );
    }

    private ImplicitBSplineCurveData implicitUniformCurve(int degree, List<StepCartesianPoint> controlPoints, String typeName) {
        validateImplicitCurveData(degree, controlPoints, typeName);
        int knotCount = controlPoints.size() + degree + 1;
        List<Integer> multiplicities = new ArrayList<>(knotCount);
        List<Double> knots = new ArrayList<>(knotCount);
        for (int index = 0; index < knotCount; index++) {
            multiplicities.add(1);
            knots.add((double) index);
        }
        return new ImplicitBSplineCurveData(degree, controlPoints, multiplicities, knots);
    }

    private ImplicitBSplineCurveData implicitQuasiUniformCurve(int degree, List<StepCartesianPoint> controlPoints, String typeName) {
        validateImplicitCurveData(degree, controlPoints, typeName);
        int interiorCount = controlPoints.size() - degree - 1;
        List<Integer> multiplicities = new ArrayList<>();
        List<Double> knots = new ArrayList<>();
        multiplicities.add(degree + 1);
        knots.add(0.0);
        for (int index = 1; index <= interiorCount; index++) {
            multiplicities.add(1);
            knots.add((double) index);
        }
        multiplicities.add(degree + 1);
        knots.add((double) (interiorCount + 1));
        return new ImplicitBSplineCurveData(degree, controlPoints, List.copyOf(multiplicities), List.copyOf(knots));
    }

    private ImplicitBSplineCurveData implicitPiecewiseBezierCurve(int degree, List<StepCartesianPoint> controlPoints, String typeName) {
        validateImplicitCurveData(degree, controlPoints, typeName);
        int segmentCount = controlPoints.size() - 1;
        if (segmentCount % degree != 0) {
            throw new UnsupportedGeometryException(typeName + " requires (controlPointCount - 1) to be divisible by degree");
        }
        int pieceCount = segmentCount / degree;
        List<Integer> multiplicities = new ArrayList<>();
        List<Double> knots = new ArrayList<>();
        multiplicities.add(degree + 1);
        knots.add(0.0);
        for (int index = 1; index < pieceCount; index++) {
            multiplicities.add(degree);
            knots.add((double) index);
        }
        multiplicities.add(degree + 1);
        knots.add((double) pieceCount);
        return new ImplicitBSplineCurveData(degree, controlPoints, List.copyOf(multiplicities), List.copyOf(knots));
    }

    private ImplicitBSplineSurfaceData implicitBezierSurface(
            int uDegree,
            int vDegree,
            List<List<StepCartesianPoint>> controlPoints,
            String typeName
    ) {
        validateImplicitSurfaceData(uDegree, vDegree, controlPoints, typeName);
        if (controlPoints.size() != uDegree + 1 || controlPoints.getFirst().size() != vDegree + 1) {
            throw new UnsupportedGeometryException(typeName + " requires controlPointCount = degree + 1 in both directions");
        }
        return new ImplicitBSplineSurfaceData(
                uDegree,
                vDegree,
                controlPoints,
                List.of(uDegree + 1, uDegree + 1),
                List.of(vDegree + 1, vDegree + 1),
                List.of(0.0, 1.0),
                List.of(0.0, 1.0)
        );
    }

    private ImplicitBSplineSurfaceData implicitUniformSurface(
            int uDegree,
            int vDegree,
            List<List<StepCartesianPoint>> controlPoints,
            String typeName
    ) {
        validateImplicitSurfaceData(uDegree, vDegree, controlPoints, typeName);
        return new ImplicitBSplineSurfaceData(
                uDegree,
                vDegree,
                controlPoints,
                uniformMultiplicities(controlPoints.size(), uDegree),
                uniformMultiplicities(controlPoints.getFirst().size(), vDegree),
                uniformKnots(controlPoints.size(), uDegree),
                uniformKnots(controlPoints.getFirst().size(), vDegree)
        );
    }

    private ImplicitBSplineSurfaceData implicitQuasiUniformSurface(
            int uDegree,
            int vDegree,
            List<List<StepCartesianPoint>> controlPoints,
            String typeName
    ) {
        validateImplicitSurfaceData(uDegree, vDegree, controlPoints, typeName);
        return new ImplicitBSplineSurfaceData(
                uDegree,
                vDegree,
                controlPoints,
                quasiUniformMultiplicities(controlPoints.size(), uDegree),
                quasiUniformMultiplicities(controlPoints.getFirst().size(), vDegree),
                quasiUniformKnots(controlPoints.size(), uDegree),
                quasiUniformKnots(controlPoints.getFirst().size(), vDegree)
        );
    }

    private ImplicitBSplineSurfaceData implicitPiecewiseBezierSurface(
            int uDegree,
            int vDegree,
            List<List<StepCartesianPoint>> controlPoints,
            String typeName
    ) {
        validateImplicitSurfaceData(uDegree, vDegree, controlPoints, typeName);
        return new ImplicitBSplineSurfaceData(
                uDegree,
                vDegree,
                controlPoints,
                piecewiseBezierMultiplicities(controlPoints.size(), uDegree, typeName + " U"),
                piecewiseBezierMultiplicities(controlPoints.getFirst().size(), vDegree, typeName + " V"),
                piecewiseBezierKnots(controlPoints.size(), uDegree, typeName + " U"),
                piecewiseBezierKnots(controlPoints.getFirst().size(), vDegree, typeName + " V")
        );
    }

    private void validateImplicitCurveData(int degree, List<StepCartesianPoint> controlPoints, String typeName) {
        if (degree < 1 || controlPoints.isEmpty()) {
            throw new UnsupportedGeometryException(typeName + " marker does not carry inherited B-spline geometry");
        }
    }

    private void validateImplicitSurfaceData(
            int uDegree,
            int vDegree,
            List<List<StepCartesianPoint>> controlPoints,
            String typeName
    ) {
        if (uDegree < 1 || vDegree < 1 || controlPoints.isEmpty() || controlPoints.getFirst().isEmpty()) {
            throw new UnsupportedGeometryException(typeName + " marker does not carry inherited B-spline geometry");
        }
    }

    private List<Integer> uniformMultiplicities(int controlPointCount, int degree) {
        int knotCount = controlPointCount + degree + 1;
        List<Integer> multiplicities = new ArrayList<>(knotCount);
        for (int index = 0; index < knotCount; index++) {
            multiplicities.add(1);
        }
        return List.copyOf(multiplicities);
    }

    private List<Double> uniformKnots(int controlPointCount, int degree) {
        int knotCount = controlPointCount + degree + 1;
        List<Double> knots = new ArrayList<>(knotCount);
        for (int index = 0; index < knotCount; index++) {
            knots.add((double) index);
        }
        return List.copyOf(knots);
    }

    private List<Integer> quasiUniformMultiplicities(int controlPointCount, int degree) {
        int interiorCount = controlPointCount - degree - 1;
        List<Integer> multiplicities = new ArrayList<>();
        multiplicities.add(degree + 1);
        for (int index = 0; index < interiorCount; index++) {
            multiplicities.add(1);
        }
        multiplicities.add(degree + 1);
        return List.copyOf(multiplicities);
    }

    private List<Double> quasiUniformKnots(int controlPointCount, int degree) {
        int interiorCount = controlPointCount - degree - 1;
        List<Double> knots = new ArrayList<>();
        for (int index = 0; index <= interiorCount + 1; index++) {
            knots.add((double) index);
        }
        return List.copyOf(knots);
    }

    private List<Integer> piecewiseBezierMultiplicities(int controlPointCount, int degree, String axisLabel) {
        int segmentCount = controlPointCount - 1;
        if (segmentCount % degree != 0) {
            throw new UnsupportedGeometryException(axisLabel + " requires (controlPointCount - 1) to be divisible by degree");
        }
        int pieceCount = segmentCount / degree;
        List<Integer> multiplicities = new ArrayList<>();
        multiplicities.add(degree + 1);
        for (int index = 1; index < pieceCount; index++) {
            multiplicities.add(degree);
        }
        multiplicities.add(degree + 1);
        return List.copyOf(multiplicities);
    }

    private List<Double> piecewiseBezierKnots(int controlPointCount, int degree, String axisLabel) {
        int segmentCount = controlPointCount - 1;
        if (segmentCount % degree != 0) {
            throw new UnsupportedGeometryException(axisLabel + " requires (controlPointCount - 1) to be divisible by degree");
        }
        int pieceCount = segmentCount / degree;
        List<Double> knots = new ArrayList<>();
        for (int index = 0; index <= pieceCount; index++) {
            knots.add((double) index);
        }
        return List.copyOf(knots);
    }

    private Curve3 liftCurve2(Curve2 curve2) {
        List<Point2> points2 = sampleCurve2(curve2, 72);
        List<CartesianPoint> points3 = points2.stream()
                .map(point -> new CartesianPoint(point.x(), point.y(), 0.0))
                .toList();
        return new Polyline3(points3);
    }

    private Curve2 approximateOffsetCurve2(Curve2 basisCurve, double distance) {
        List<Point2> sampled = sampleCurve2(basisCurve, 72);
        List<Point2> offsetPoints = new ArrayList<>(sampled.size());
        for (int index = 0; index < sampled.size(); index++) {
            Point2 point = sampled.get(index);
            Vector2 tangent = tangentAt(sampled, index);
            Vector2 normal = new Vector2(-tangent.y(), tangent.x());
            Direction2 direction = normal.isZero() ? new Direction2(0.0, 1.0) : normal.normalize();
            offsetPoints.add(point.add(direction.asVector().scale(distance)));
        }
        return new Polyline2(offsetPoints);
    }

    private Curve3 approximateOffsetCurve3(Curve3 basisCurve, double distance, Direction3 refDirection) {
        List<CartesianPoint> sampled = sampleCurve3(basisCurve, 72);
        List<CartesianPoint> offsetPoints = new ArrayList<>(sampled.size());
        Vector3 ref = refDirection.asVector();
        for (int index = 0; index < sampled.size(); index++) {
            CartesianPoint point = sampled.get(index);
            Vector3 tangent = tangentAt3(sampled, index);
            Vector3 normal = tangent.cross(ref);
            if (normal.isZero()) {
                normal = ref;
            }
            offsetPoints.add(point.add(normal.normalize().asVector().scale(distance)));
        }
        return new Polyline3(offsetPoints);
    }

    private List<Point2> sampleCurve2(Curve2 curve, int segments) {
        if (curve instanceof Line2 line) {
            return List.of(line.pointAt(0.0), line.pointAt(1.0));
        }
        if (curve instanceof Circle2 circle) {
            List<Point2> points = new ArrayList<>(segments + 1);
            for (int index = 0; index <= segments; index++) {
                points.add(circle.pointAt(Math.PI * 2.0 * index / segments));
            }
            return List.copyOf(points);
        }
        if (curve instanceof Ellipse2 ellipse) {
            List<Point2> points = new ArrayList<>(segments + 1);
            for (int index = 0; index <= segments; index++) {
                points.add(ellipse.pointAt(Math.PI * 2.0 * index / segments));
            }
            return List.copyOf(points);
        }
        if (curve instanceof BSplineCurve2 spline) {
            List<Point2> points = new ArrayList<>(segments + 1);
            double start = spline.startParameter();
            double end = spline.endParameter();
            for (int index = 0; index <= segments; index++) {
                points.add(spline.pointAt(start + (end - start) * index / segments));
            }
            return List.copyOf(points);
        }
        if (curve instanceof RationalBSplineCurve2 spline) {
            return spline.sample(segments);
        }
        if (curve instanceof TrimmedCurve2 trimmedCurve) {
            return sampleTrimmedCurve2(trimmedCurve, segments);
        }
        if (curve instanceof Polyline2 polyline) {
            return polyline.points();
        }
        if (curve instanceof CompositeCurve2 compositeCurve) {
            List<Point2> points = new ArrayList<>();
            boolean first = true;
            for (Curve2 segment : compositeCurve.segments()) {
                List<Point2> segmentPoints = sampleCurve2(segment, segments);
                int start = first ? 0 : 1;
                for (int i = start; i < segmentPoints.size(); i++) {
                    points.add(segmentPoints.get(i));
                }
                first = false;
            }
            return List.copyOf(points);
        }
        throw new UnsupportedGeometryException("curve sampling for " + curveTypeName(curve) + " is unsupported");
    }

    private List<Point2> sampleTrimmedCurve2(TrimmedCurve2 trimmedCurve, int segments) {
        List<Point2> sampled = sampleCurve2(trimmedCurve.basisCurve(), segments);
        if (sampled.size() < 2) {
            return List.of(trimmedCurve.trimStart(), trimmedCurve.trimEnd());
        }
        boolean closed = sampled.getFirst().subtract(sampled.getLast()).norm() <= 1.0e-9;
        List<Point2> basisPoints = closed ? List.copyOf(sampled.subList(0, sampled.size() - 1)) : sampled;
        int startIndex = nearestPointIndex2(basisPoints, trimmedCurve.trimStart());
        int endIndex = nearestPointIndex2(basisPoints, trimmedCurve.trimEnd());

        List<Point2> trimmed = new ArrayList<>();
        trimmed.add(trimmedCurve.trimStart());
        if (closed) {
            appendClosedTrimmedPoints2(trimmed, basisPoints, startIndex, endIndex, trimmedCurve.senseAgreement());
        } else {
            appendOpenTrimmedPoints2(trimmed, basisPoints, startIndex, endIndex);
        }
        addDistinctPoint2(trimmed, trimmedCurve.trimEnd());
        return List.copyOf(trimmed);
    }

    private int nearestPointIndex2(List<Point2> points, Point2 target) {
        int nearestIndex = 0;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index < points.size(); index++) {
            double distance = points.get(index).subtract(target).norm();
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestIndex = index;
            }
        }
        return nearestIndex;
    }

    private void appendClosedTrimmedPoints2(
            List<Point2> target,
            List<Point2> basisPoints,
            int startIndex,
            int endIndex,
            boolean senseAgreement
    ) {
        int size = basisPoints.size();
        int index = startIndex;
        while (index != endIndex) {
            index = senseAgreement ? (index + 1) % size : (index - 1 + size) % size;
            addDistinctPoint2(target, basisPoints.get(index));
        }
    }

    private void appendOpenTrimmedPoints2(
            List<Point2> target,
            List<Point2> basisPoints,
            int startIndex,
            int endIndex
    ) {
        if (startIndex <= endIndex) {
            for (int index = startIndex + 1; index <= endIndex; index++) {
                addDistinctPoint2(target, basisPoints.get(index));
            }
            return;
        }
        for (int index = startIndex - 1; index >= endIndex; index--) {
            addDistinctPoint2(target, basisPoints.get(index));
        }
    }

    private void addDistinctPoint2(List<Point2> points, Point2 candidate) {
        if (points.isEmpty() || points.getLast().subtract(candidate).norm() > 1.0e-9) {
            points.add(candidate);
        }
    }

    private List<Point2> normalizeClosedLoop2(List<Point2> points) {
        if (points.size() < 3) {
            throw new UnsupportedGeometryException("profile loop must contain at least 3 points");
        }
        List<Point2> normalized = new ArrayList<>();
        for (Point2 point : points) {
            if (normalized.isEmpty() || point.subtract(normalized.get(normalized.size() - 1)).norm() > 1.0e-9) {
                normalized.add(point);
            }
        }
        if (normalized.size() < 3) {
            throw new UnsupportedGeometryException("profile loop must contain at least 3 distinct points");
        }
        if (normalized.getFirst().subtract(normalized.getLast()).norm() > 1.0e-9) {
            normalized.add(normalized.getFirst());
        }
        return List.copyOf(normalized);
    }

    private List<Point2> reverseClosedLoop2(List<Point2> points) {
        List<Point2> reversed = new ArrayList<>(points.size());
        for (int index = points.size() - 2; index >= 0; index--) {
            reversed.add(points.get(index));
        }
        reversed.add(reversed.getFirst());
        return List.copyOf(reversed);
    }

    private List<CartesianPoint> closeLoop3(List<CartesianPoint> points) {
        List<CartesianPoint> closed = new ArrayList<>(points);
        if (closed.getFirst().distanceTo(closed.getLast()) > 1.0e-9) {
            closed.add(closed.getFirst());
        }
        return List.copyOf(closed);
    }

    private List<CartesianPoint> reverseClosedLoop3(List<CartesianPoint> points) {
        List<CartesianPoint> closed = closeLoop3(points);
        List<CartesianPoint> reversed = new ArrayList<>(closed.size());
        for (int index = closed.size() - 2; index >= 0; index--) {
            reversed.add(closed.get(index));
        }
        reversed.add(reversed.getFirst());
        return List.copyOf(reversed);
    }

    private double signedArea2(List<Point2> points) {
        double area = 0.0;
        for (int index = 0; index < points.size() - 1; index++) {
            Point2 current = points.get(index);
            Point2 next = points.get(index + 1);
            area += current.x() * next.y() - next.x() * current.y();
        }
        return area * 0.5;
    }

    private List<CartesianPoint> sampleCurve3(Curve3 curve, int segments) {
        if (curve instanceof Line3 line) {
            return List.of(line.origin(), line.pointAt(1.0));
        }
        if (curve instanceof Circle circle) {
            List<CartesianPoint> points = new ArrayList<>(segments + 1);
            for (int index = 0; index <= segments; index++) {
                points.add(circle.pointAt(Math.PI * 2.0 * index / segments));
            }
            return List.copyOf(points);
        }
        if (curve instanceof Ellipse3 ellipse) {
            List<CartesianPoint> points = new ArrayList<>(segments + 1);
            for (int index = 0; index <= segments; index++) {
                points.add(ellipse.pointAt(Math.PI * 2.0 * index / segments));
            }
            return List.copyOf(points);
        }
        if (curve instanceof BSplineCurve3 spline) {
            return spline.sample(segments);
        }
        if (curve instanceof RationalBSplineCurve3 spline) {
            return spline.sample(segments);
        }
        if (curve instanceof TrimmedCurve3 trimmedCurve) {
            return sampleTrimmedCurve3(trimmedCurve, segments);
        }
        if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return sampleCurve3(surfaceCurve.curve3d(), segments);
        }
        if (curve instanceof Polyline3 polyline) {
            return polyline.points();
        }
        if (curve instanceof CompositeCurve3 compositeCurve) {
            List<CartesianPoint> points = new ArrayList<>();
            boolean first = true;
            for (Curve3 segment : compositeCurve.segments()) {
                List<CartesianPoint> segmentPoints = sampleCurve3(segment, segments);
                int start = first ? 0 : 1;
                for (int i = start; i < segmentPoints.size(); i++) {
                    points.add(segmentPoints.get(i));
                }
                first = false;
            }
            return List.copyOf(points);
        }
        if (curve instanceof Clothoid3 clothoid) {
            return clothoid.sample(segments);
        }
        if (curve instanceof Parabola3 parabola) {
            return parabola.sample(segments);
        }
        if (curve instanceof Hyperbola3 hyperbola) {
            return hyperbola.sample(segments);
        }
        throw new UnsupportedGeometryException("curve sampling for " + curveTypeName(curve) + " is unsupported");
    }

    private static Vector2 tangentAt(List<Point2> points, int index) {
        Point2 previous = points.get(Math.max(index - 1, 0));
        Point2 next = points.get(Math.min(index + 1, points.size() - 1));
        Vector2 tangent = next.subtract(previous);
        return tangent.isZero() ? new Vector2(1.0, 0.0) : tangent;
    }

    private List<CartesianPoint> sampleTrimmedCurve3(TrimmedCurve3 trimmedCurve, int segments) {
        List<CartesianPoint> sampled = sampleCurve3(trimmedCurve.basisCurve(), segments);
        if (sampled.size() < 2) {
            return List.of(trimmedCurve.trimStart(), trimmedCurve.trimEnd());
        }
        boolean closed = sampled.getFirst().distanceTo(sampled.getLast()) <= 1.0e-9;
        List<CartesianPoint> basisPoints = closed ? List.copyOf(sampled.subList(0, sampled.size() - 1)) : sampled;
        int startIndex = nearestPointIndex(basisPoints, trimmedCurve.trimStart());
        int endIndex = nearestPointIndex(basisPoints, trimmedCurve.trimEnd());

        List<CartesianPoint> trimmed = new ArrayList<>();
        trimmed.add(trimmedCurve.trimStart());
        if (closed) {
            appendClosedTrimmedPoints(trimmed, basisPoints, startIndex, endIndex, trimmedCurve.senseAgreement());
        } else {
            appendOpenTrimmedPoints(trimmed, basisPoints, startIndex, endIndex);
        }
        addDistinctPoint(trimmed, trimmedCurve.trimEnd());
        return List.copyOf(trimmed);
    }

    private int nearestPointIndex(List<CartesianPoint> points, CartesianPoint target) {
        int nearestIndex = 0;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index < points.size(); index++) {
            double distance = points.get(index).distanceTo(target);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestIndex = index;
            }
        }
        return nearestIndex;
    }

    private void appendClosedTrimmedPoints(
            List<CartesianPoint> target,
            List<CartesianPoint> basisPoints,
            int startIndex,
            int endIndex,
            boolean senseAgreement
    ) {
        int size = basisPoints.size();
        int index = startIndex;
        while (index != endIndex) {
            index = senseAgreement ? (index + 1) % size : (index - 1 + size) % size;
            addDistinctPoint(target, basisPoints.get(index));
        }
    }

    private void appendOpenTrimmedPoints(
            List<CartesianPoint> target,
            List<CartesianPoint> basisPoints,
            int startIndex,
            int endIndex
    ) {
        if (startIndex <= endIndex) {
            for (int index = startIndex + 1; index <= endIndex; index++) {
                addDistinctPoint(target, basisPoints.get(index));
            }
            return;
        }
        for (int index = startIndex - 1; index >= endIndex; index--) {
            addDistinctPoint(target, basisPoints.get(index));
        }
    }

    private static Vector3 tangentAt3(List<CartesianPoint> points, int index) {
        CartesianPoint previous = points.get(Math.max(index - 1, 0));
        CartesianPoint next = points.get(Math.min(index + 1, points.size() - 1));
        Vector3 tangent = next.subtract(previous);
        return tangent.isZero() ? new Vector3(1.0, 0.0, 0.0) : tangent;
    }

    private Curve3 transformCurve3(Curve3 curve, StepCartesianTransformationOperator transformation) {
        double scale = transformationScale(transformation);
        return switch (curve) {
            case Line3 line -> new Line3(
                    transformPoint3(line.origin(), transformation),
                    transformDirection3(line.direction(), transformation));
            case Circle circle -> new Circle(
                    transformPlacement(circle.position(), transformation),
                    circle.radius() * scale);
            case Ellipse3 ellipse -> new Ellipse3(
                    transformPlacement(ellipse.position(), transformation),
                    ellipse.semiAxis1() * scale,
                    ellipse.semiAxis2() * scale);
            case Polyline3 polyline -> new Polyline3(polyline.points().stream()
                    .map(point -> transformPoint3(point, transformation))
                    .toList());
            case BSplineCurve3 spline -> new BSplineCurve3(
                    spline.degree(),
                    spline.controlPoints().stream().map(point -> transformPoint3(point, transformation)).toList(),
                    spline.knotMultiplicities(),
                    spline.knots());
            case RationalBSplineCurve3 spline -> new RationalBSplineCurve3(
                    spline.degree(),
                    spline.controlPoints().stream().map(point -> transformPoint3(point, transformation)).toList(),
                    spline.weights(),
                    spline.knotMultiplicities(),
                    spline.knots());
            case SurfaceCurve3 surfaceCurve -> new SurfaceCurve3(transformCurve3(surfaceCurve.curve3d(), transformation));
            case TrimmedCurve3 trimmedCurve -> new TrimmedCurve3(
                    transformCurve3(trimmedCurve.basisCurve(), transformation),
                    transformPoint3(trimmedCurve.trimStart(), transformation),
                    transformPoint3(trimmedCurve.trimEnd(), transformation),
                    trimmedCurve.senseAgreement());
            case CompositeCurve3 compositeCurve -> new CompositeCurve3(
                    compositeCurve.segments().stream()
                            .map(segment -> transformCurve3(segment, transformation))
                            .toList());
            default -> throw new UnsupportedGeometryException("curve replica for " + curveTypeName(curve) + " is unsupported");
        };
    }

    private Curve2 transformCurve2(Curve2 curve, StepCartesianTransformationOperator transformation) {
        double scale = transformationScale(transformation);
        return switch (curve) {
            case Line2 line -> new Line2(
                    transformPoint2(line.origin(), transformation),
                    transformDirection2(line.direction(), transformation));
            case Circle2 circle -> new Circle2(
                    transformPoint2(circle.center(), transformation),
                    transformDirection2(circle.xDirection(), transformation),
                    circle.radius() * scale);
            case Ellipse2 ellipse -> new Ellipse2(
                    transformPoint2(ellipse.center(), transformation),
                    transformDirection2(ellipse.xDirection(), transformation),
                    ellipse.semiAxis1() * scale,
                    ellipse.semiAxis2() * scale);
            case Polyline2 polyline -> new Polyline2(polyline.points().stream()
                    .map(point -> transformPoint2(point, transformation))
                    .toList());
            case BSplineCurve2 spline -> new BSplineCurve2(
                    spline.degree(),
                    spline.controlPoints().stream().map(point -> transformPoint2(point, transformation)).toList(),
                    spline.knotMultiplicities(),
                    spline.knots());
            case RationalBSplineCurve2 spline -> new RationalBSplineCurve2(
                    spline.degree(),
                    spline.controlPoints().stream().map(point -> transformPoint2(point, transformation)).toList(),
                    spline.weights(),
                    spline.knotMultiplicities(),
                    spline.knots());
            case TrimmedCurve2 trimmedCurve -> new TrimmedCurve2(
                    transformCurve2(trimmedCurve.basisCurve(), transformation),
                    transformPoint2(trimmedCurve.trimStart(), transformation),
                    transformPoint2(trimmedCurve.trimEnd(), transformation),
                    trimmedCurve.senseAgreement());
            case CompositeCurve2 compositeCurve -> new CompositeCurve2(
                    compositeCurve.segments().stream()
                            .map(segment -> transformCurve2(segment, transformation))
                            .toList());
            default -> throw new UnsupportedGeometryException("curve replica for " + curveTypeName(curve) + " is unsupported");
        };
    }

    private record ImplicitBSplineCurveData(
            int degree,
            List<StepCartesianPoint> controlPoints,
            List<Integer> knotMultiplicities,
            List<Double> knots
    ) {
    }

    private record ImplicitBSplineSurfaceData(
            int uDegree,
            int vDegree,
            List<List<StepCartesianPoint>> controlPoints,
            List<Integer> uMultiplicities,
            List<Integer> vMultiplicities,
            List<Double> uKnots,
            List<Double> vKnots
    ) {
    }

    private static String stepEntityTypeName(StepEntity entity) {
        if (entity instanceof StepGeometricReplica replica) {
            return replica.entityName();
        }
        if (entity instanceof StepCsgPrimitive primitive) {
            return primitive.entityName();
        }
        if (entity instanceof StepSweptAreaSolid sweptAreaSolid) {
            return sweptAreaSolid.entityName();
        }
        if (entity instanceof StepConicCurve conic) {
            return conic.entityName();
        }
        if (entity instanceof StepFaceBound faceBound) {
            return faceBound.outer() ? "FACE_OUTER_BOUND" : "FACE_BOUND";
        }
        if (entity instanceof StepProfileDef profile) {
            return profile.entityName();
        }
        if (entity instanceof StepBooleanClippingResult) {
            return "BOOLEAN_CLIPPING_RESULT";
        }
        if (entity instanceof StepBooleanResult) {
            return "BOOLEAN_RESULT";
        }
        if (entity instanceof StepManifoldSolidBrep) {
            return "MANIFOLD_SOLID_BREP";
        }
        if (entity instanceof StepBrepWithVoids) {
            return "BREP_WITH_VOIDS";
        }
        if (entity instanceof StepCsgSolid) {
            return "CSG_SOLID";
        }
        if (entity instanceof StepSolidReplica) {
            return "SOLID_REPLICA";
        }
        if (entity instanceof StepLine) {
            return "LINE";
        }
        if (entity instanceof StepCircle) {
            return "CIRCLE";
        }
        if (entity instanceof StepEllipse) {
            return "ELLIPSE";
        }
        if (entity instanceof StepPolyline) {
            return "POLYLINE";
        }
        if (entity instanceof StepBSplineCurveWithKnots) {
            return "B_SPLINE_CURVE_WITH_KNOTS";
        }
        if (entity instanceof StepRationalBSplineCurve) {
            return "RATIONAL_B_SPLINE_CURVE";
        }
        if (entity instanceof StepTrimmedCurve) {
            return "TRIMMED_CURVE";
        }
        if (entity instanceof StepSurfaceCurve) {
            return "SURFACE_CURVE";
        }
        if (entity instanceof StepSeamCurve) {
            return "SEAM_CURVE";
        }
        if (entity instanceof StepCompositeCurve) {
            return "COMPOSITE_CURVE";
        }
        if (entity instanceof StepCompositeCurveOnSurface) {
            return "COMPOSITE_CURVE_ON_SURFACE";
        }
        if (entity instanceof StepOffsetCurve2D) {
            return "OFFSET_CURVE_2D";
        }
        if (entity instanceof StepOffsetCurve3D) {
            return "OFFSET_CURVE_3D";
        }
        if (entity instanceof StepOrientedCurve) {
            return "ORIENTED_CURVE";
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

    private static String loopTypeName(Loop loop) {
        if (loop instanceof EdgeLoop) {
            return "EDGE_LOOP";
        }
        if (loop instanceof VertexLoop) {
            return "VERTEX_LOOP";
        }
        if (loop instanceof PolyLoop) {
            return "POLY_LOOP";
        }
        return loop.getClass().getSimpleName();
    }

    private static String curveTypeName(Curve3 curve) {
        if (curve instanceof Line3) {
            return "LINE";
        }
        if (curve instanceof Circle) {
            return "CIRCLE";
        }
        if (curve instanceof Ellipse3) {
            return "ELLIPSE";
        }
        if (curve instanceof Polyline3) {
            return "POLYLINE";
        }
        if (curve instanceof BSplineCurve3) {
            return "B_SPLINE_CURVE";
        }
        if (curve instanceof RationalBSplineCurve3) {
            return "RATIONAL_B_SPLINE_CURVE";
        }
        if (curve instanceof TrimmedCurve3) {
            return "TRIMMED_CURVE";
        }
        if (curve instanceof SurfaceCurve3) {
            return "SURFACE_CURVE";
        }
        if (curve instanceof CompositeCurve3) {
            return "COMPOSITE_CURVE";
        }
        return curve.getClass().getSimpleName();
    }

    private static String curveTypeName(Curve2 curve) {
        if (curve instanceof Line2) {
            return "LINE";
        }
        if (curve instanceof Circle2) {
            return "CIRCLE";
        }
        if (curve instanceof Ellipse2) {
            return "ELLIPSE";
        }
        if (curve instanceof Polyline2) {
            return "POLYLINE";
        }
        if (curve instanceof BSplineCurve2) {
            return "B_SPLINE_CURVE";
        }
        if (curve instanceof RationalBSplineCurve2) {
            return "RATIONAL_B_SPLINE_CURVE";
        }
        if (curve instanceof TrimmedCurve2) {
            return "TRIMMED_CURVE";
        }
        if (curve instanceof CompositeCurve2) {
            return "COMPOSITE_CURVE";
        }
        return curve.getClass().getSimpleName();
    }

    private Plane transformPlane(Plane plane, StepCartesianTransformationOperator transformation) {
        return new Plane(
                transformPoint3(plane.origin(), transformation),
                transformDirection3(plane.normal(), transformation));
    }

    private SurfaceGeometry transformSurfaceGeometry(SurfaceGeometry surface, StepCartesianTransformationOperator transformation) {
        double scale = Math.abs(transformationScale(transformation));
        return switch (surface) {
            case Plane plane -> transformPlane(plane, transformation);
            case OffsetSurface3 offsetSurface -> new OffsetSurface3(
                    transformSurfaceGeometry(offsetSurface.basisSurface(), transformation),
                    offsetSurface.distance() * scale);
            case CylindricalSurface cylindricalSurface -> new CylindricalSurface(
                    transformPlacement(cylindricalSurface.position(), transformation),
                    cylindricalSurface.radius() * scale);
            case ConicalSurface conicalSurface -> new ConicalSurface(
                    transformPlacement(conicalSurface.position(), transformation),
                    conicalSurface.radius() * scale,
                    conicalSurface.semiAngle());
            case SphericalSurface sphericalSurface -> new SphericalSurface(
                    transformPlacement(sphericalSurface.position(), transformation),
                    sphericalSurface.radius() * scale);
            case ToroidalSurface toroidalSurface -> new ToroidalSurface(
                    transformPlacement(toroidalSurface.position(), transformation),
                    toroidalSurface.majorRadius() * scale,
                    toroidalSurface.minorRadius() * scale);
            case BSplineSurface3 splineSurface -> new BSplineSurface3(
                    splineSurface.uDegree(),
                    splineSurface.vDegree(),
                    splineSurface.controlPoints().stream()
                            .map(row -> row.stream().map(point -> transformPoint3(point, transformation)).toList())
                            .toList(),
                    splineSurface.uMultiplicities(),
                    splineSurface.vMultiplicities(),
                    splineSurface.uKnots(),
                    splineSurface.vKnots());
            case RationalBSplineSurface3 splineSurface -> new RationalBSplineSurface3(
                    splineSurface.uDegree(),
                    splineSurface.vDegree(),
                    splineSurface.controlPoints().stream()
                            .map(row -> row.stream().map(point -> transformPoint3(point, transformation)).toList())
                            .toList(),
                    splineSurface.weightsData(),
                    splineSurface.uMultiplicities(),
                    splineSurface.vMultiplicities(),
                    splineSurface.uKnots(),
                    splineSurface.vKnots());
            case SurfaceOfLinearExtrusion3 extrusionSurface -> new SurfaceOfLinearExtrusion3(
                    transformCurve3(extrusionSurface.sweptCurve(), transformation),
                    transformVector3(extrusionSurface.extrusionVector(), transformation));
            case SurfaceOfRevolution3 revolutionSurface -> new SurfaceOfRevolution3(
                    transformCurve3(revolutionSurface.sweptCurve(), transformation),
                    transformPoint3(revolutionSurface.axisOrigin(), transformation),
                    transformDirection3(revolutionSurface.axisDirection(), transformation));
            case RuledSurface3 ruledSurface -> new RuledSurface3(
                    transformCurve3(ruledSurface.directrix1(), transformation),
                    transformCurve3(ruledSurface.directrix2(), transformation));
            case SurfaceOfConstantRadius3 constantRadiusSurface -> new SurfaceOfConstantRadius3(
                    transformSurfaceGeometry(constantRadiusSurface.sweptSurface(), transformation),
                    constantRadiusSurface.radius() * scale);
        };
    }

    private Axis2Placement3D transformPlacement(Axis2Placement3D placement, StepCartesianTransformationOperator transformation) {
        return new Axis2Placement3D(
                transformPoint3(placement.location(), transformation),
                transformDirection3(placement.axis(), transformation),
                transformDirection3(placement.refDirection(), transformation));
    }

    private CartesianPoint transformPoint3(CartesianPoint point, StepCartesianTransformationOperator transformation) {
        Vector3 basisX = transformAxis1_3(transformation);
        Vector3 basisY = transformAxis2OrDefault3(transformation, basisX);
        Vector3 basisZ = transformAxis3OrDefault3(transformation, basisX, basisY);
        double scale = transformationScale(transformation);
        Vector3 offset = basisX.scale(point.x() * scale)
                .add(basisY.scale(point.y() * scale))
                .add(basisZ.scale(point.z() * scale));
        return buildPoint(transformation.localOrigin().id()).add(offset);
    }

    private Vector3 transformVector3(Vector3 vector, StepCartesianTransformationOperator transformation) {
        Vector3 basisX = transformAxis1_3(transformation);
        Vector3 basisY = transformAxis2OrDefault3(transformation, basisX);
        Vector3 basisZ = transformAxis3OrDefault3(transformation, basisX, basisY);
        double scale = transformationScale(transformation);
        return basisX.scale(vector.x() * scale)
                .add(basisY.scale(vector.y() * scale))
                .add(basisZ.scale(vector.z() * scale));
    }

    private Point2 transformPoint2(Point2 point, StepCartesianTransformationOperator transformation) {
        Vector2 basisX = transformAxis1_2(transformation);
        Vector2 basisY = transformAxis2OrDefault2(transformation, basisX);
        double scale = transformationScale(transformation);
        Vector2 offset = basisX.scale(point.x() * scale).add(basisY.scale(point.y() * scale));
        Point2 origin = origin2(transformation);
        return origin.add(offset);
    }

    private Direction3 transformDirection3(Direction3 direction, StepCartesianTransformationOperator transformation) {
        Vector3 basisX = transformAxis1_3(transformation);
        Vector3 basisY = transformAxis2OrDefault3(transformation, basisX);
        Vector3 basisZ = transformAxis3OrDefault3(transformation, basisX, basisY);
        Vector3 source = direction.asVector();
        return Direction3.from(
                basisX.scale(source.x())
                        .add(basisY.scale(source.y()))
                        .add(basisZ.scale(source.z()))
        );
    }

    private Direction2 transformDirection2(Direction2 direction, StepCartesianTransformationOperator transformation) {
        Vector2 basisX = transformAxis1_2(transformation);
        Vector2 basisY = transformAxis2OrDefault2(transformation, basisX);
        Vector2 source = direction.asVector();
        return Direction2.from(basisX.scale(source.x()).add(basisY.scale(source.y())));
    }

    private Vector3 transformAxis1_3(StepCartesianTransformationOperator transformation) {
        return transformation.axis1() == null ? new Vector3(1.0, 0.0, 0.0) : buildDirection(transformation.axis1().id()).asVector();
    }

    private Vector3 transformAxis2OrDefault3(StepCartesianTransformationOperator transformation, Vector3 axis1) {
        if (transformation.axis2() != null) {
            return buildDirection(transformation.axis2().id()).asVector();
        }
        Vector3 fallback = new Vector3(0.0, 1.0, 0.0);
        return axis1.cross(fallback).isZero() ? new Vector3(0.0, 0.0, 1.0) : fallback;
    }

    private Vector3 transformAxis3OrDefault3(StepCartesianTransformationOperator transformation, Vector3 axis1, Vector3 axis2) {
        if (transformation.axis3() != null) {
            return buildDirection(transformation.axis3().id()).asVector();
        }
        Vector3 cross = axis1.cross(axis2);
        return cross.isZero() ? new Vector3(0.0, 0.0, 1.0) : cross.normalize().asVector();
    }

    private Vector2 transformAxis1_2(StepCartesianTransformationOperator transformation) {
        if (transformation.axis1() == null) {
            return new Vector2(1.0, 0.0);
        }
        StepDirection direction = transformation.axis1();
        if (direction.directionRatios().size() != 2) {
            throw new UnsupportedGeometryException("2D replica transformation axis1 must be 2D");
        }
        return buildDirection2(direction.id()).asVector();
    }

    private Vector2 transformAxis2OrDefault2(StepCartesianTransformationOperator transformation, Vector2 axis1) {
        if (transformation.axis2() != null) {
            StepDirection direction = transformation.axis2();
            if (direction.directionRatios().size() != 2) {
                throw new UnsupportedGeometryException("2D replica transformation axis2 must be 2D");
            }
            return buildDirection2(direction.id()).asVector();
        }
        return new Vector2(-axis1.y(), axis1.x());
    }

    private Point2 origin2(StepCartesianTransformationOperator transformation) {
        StepCartesianPoint origin = transformation.localOrigin();
        if (origin.coordinates().size() != 2) {
            throw new UnsupportedGeometryException("2D replica transformation origin must be 2D");
        }
        return buildPoint2(origin.id());
    }

    private double transformationScale(StepCartesianTransformationOperator transformation) {
        return transformation.scale() == null ? 1.0 : transformation.scale();
    }

    private String unsupportedReplicaSurfaceTransformation(StepCartesianTransformationOperator transformation) {
        double scale = transformationScale(transformation);
        if (Math.abs(scale) <= 1.0e-9) {
            return "SURFACE_REPLICA zero scale";
        }
        Vector3 axis1 = transformAxis1_3(transformation);
        Vector3 axis2 = transformAxis2OrDefault3(transformation, axis1);
        Vector3 axis3 = transformAxis3OrDefault3(transformation, axis1, axis2);
        double tolerance = 1.0e-6;
        if (Math.abs(axis1.dot(axis2)) > tolerance
                || Math.abs(axis1.dot(axis3)) > tolerance
                || Math.abs(axis2.dot(axis3)) > tolerance) {
            return "SURFACE_REPLICA non-uniform scale";
        }
        return null;
    }

    private static void validateTrimPoint(StepEntity trim, Curve3 basis, String slot) {
        if (!(trim instanceof StepCartesianPoint point)) {
            throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " only supports CARTESIAN_POINT trims");
        }
        CartesianPoint trimPoint = new CartesianPoint(
                point.coordinates().get(0),
                point.coordinates().get(1),
                point.coordinates().get(2)
        );
        if (!basis.contains(trimPoint)) {
            throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " point must lie on basis curve");
        }
    }

    private CartesianPoint requireTrimPoint3(List<StepEntity> trims, String slot) {
        if (trims.isEmpty() || !(trims.getFirst() instanceof StepCartesianPoint point)) {
            throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " only supports CARTESIAN_POINT trims");
        }
        if (point.coordinates().size() != 3) {
            throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " point must be 3D");
        }
        return buildPoint(point.id());
    }

    private Point2 requireTrimPoint2(List<StepEntity> trims, String slot) {
        if (trims.isEmpty() || !(trims.getFirst() instanceof StepCartesianPoint point)) {
            throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " only supports CARTESIAN_POINT trims");
        }
        if (point.coordinates().size() != 2) {
            throw new UnsupportedGeometryException("TRIMMED_CURVE " + slot + " point must be 2D for PCURVE");
        }
        return buildPoint2(point.id());
    }

    private Point2 snapTrimPoint2(Point2 point, Curve2 basisCurve) {
        if (basisCurve.contains(point)) {
            return point;
        }
        if (basisCurve instanceof Line2 line) {
            return line.closestPoint(point);
        }
        if (basisCurve instanceof Circle2 circle) {
            Vector2 offset = point.subtract(circle.center());
            double norm = offset.norm();
            if (norm <= Epsilon.EPS) {
                return circle.pointAt(0.0);
            }
            return circle.center().add(offset.scale(circle.radius() / norm));
        }
        if (basisCurve instanceof Ellipse2 ellipse) {
            Vector2 offset = point.subtract(ellipse.center());
            if (offset.norm() <= Epsilon.EPS) {
                return ellipse.pointAt(0.0);
            }
            Vector2 x = ellipse.xDirection().asVector();
            Vector2 y = new Vector2(-x.y(), x.x());
            double nx = offset.dot(x) / ellipse.semiAxis1();
            double ny = offset.dot(y) / ellipse.semiAxis2();
            double norm = Math.hypot(nx, ny);
            if (norm <= Epsilon.EPS) {
                return ellipse.pointAt(0.0);
            }
            double angle = Math.atan2(ny / norm, nx / norm);
            return ellipse.pointAt(angle);
        }
        if (basisCurve instanceof BSplineCurve2 spline) {
            Point2 best = null;
            double bestDistance = Double.POSITIVE_INFINITY;
            for (Point2 sample : spline.sample(192)) {
                double distance = sample.subtract(point).norm();
                if (distance < bestDistance) {
                    bestDistance = distance;
                    best = sample;
                }
            }
            if (best != null) {
                return best;
            }
        }
        if (basisCurve instanceof TrimmedCurve2 trimmed) {
            return snapTrimPoint2(point, trimmed.basisCurve());
        }
        return point;
    }

    public record Axis1Placement(CartesianPoint location, Direction3 axis) {
    }

    private record CircularFrame(Vector3 x, Vector3 y) {
        Direction3 radialAtAngle(double angle) {
            return Direction3.from(x.scale(Math.cos(angle)).add(y.scale(Math.sin(angle))));
        }
        Direction3 z() {
            return Direction3.from(x.cross(y));
        }
    }

    private record ProfileLoops(List<Point2> outer, List<List<Point2>> inner) {
        private ProfileLoops {
            outer = List.copyOf(outer);
            inner = inner.stream().map(List::copyOf).toList();
        }
    }
}
