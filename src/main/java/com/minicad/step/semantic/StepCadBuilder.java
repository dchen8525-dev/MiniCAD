package com.minicad.step.semantic;

import com.minicad.common.Epsilon;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.BSplineSurface3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.SurfaceCurve3;
import com.minicad.geometry.TrimmedCurve3;
import com.minicad.geometry.ToroidalSurface;
import com.minicad.geometry.Vector3;
import com.minicad.geometry2d.Direction2;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.geometry2d.Vector2;
import com.minicad.step.model.StepAdvancedFace;
import com.minicad.step.model.StepAxis1Placement;
import com.minicad.step.model.StepAxis2Placement2D;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepBSplineCurveWithKnots;
import com.minicad.step.model.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.StepCircle;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepConicalSurface;
import com.minicad.step.model.StepCylindricalSurface;
import com.minicad.step.model.StepDirection;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEdgeLoop;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepEllipse;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepFaceBound;
import com.minicad.step.model.StepFaceSurface;
import com.minicad.step.model.StepLine;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.model.StepOrientedEdge;
import com.minicad.step.model.StepOrientedFace;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepPcurve;
import com.minicad.step.model.StepSeamCurve;
import com.minicad.step.model.StepSurfaceCurve;
import com.minicad.step.model.StepSurfaceOfLinearExtrusion;
import com.minicad.step.model.StepSurfaceOfRevolution;
import com.minicad.step.model.StepTrimmedCurve;
import com.minicad.step.model.StepToroidalSurface;
import com.minicad.step.model.StepVertexLoop;
import com.minicad.step.model.StepVertexPoint;
import com.minicad.topology.Edge;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
import com.minicad.topology.Loop;
import com.minicad.topology.OrientedEdge;
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
    private final Map<Integer, BSplineCurve2> splineCurves2d = new LinkedHashMap<>();
    private final Map<Integer, TrimmedCurve2> trimmedCurves2d = new LinkedHashMap<>();
    private final Map<Integer, Line3> lines = new LinkedHashMap<>();
    private final Map<Integer, Plane> planes = new LinkedHashMap<>();
    private final Map<Integer, Circle> circles = new LinkedHashMap<>();
    private final Map<Integer, Ellipse3> ellipses = new LinkedHashMap<>();
    private final Map<Integer, CylindricalSurface> cylindricalSurfaces = new LinkedHashMap<>();
    private final Map<Integer, ConicalSurface> conicalSurfaces = new LinkedHashMap<>();
    private final Map<Integer, ToroidalSurface> toroidalSurfaces = new LinkedHashMap<>();
    private final Map<Integer, BSplineCurve3> bsplineCurves = new LinkedHashMap<>();
    private final Map<Integer, BSplineSurface3> bsplineSurfaces = new LinkedHashMap<>();
    private final Map<Integer, SurfaceCurve3> surfaceCurves = new LinkedHashMap<>();
    private final Map<Integer, TrimmedCurve3> trimmedCurves = new LinkedHashMap<>();
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
                point.coordinates().get(2)
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
        StepPcurve pcurve = requireEntity(id, StepPcurve.class, "PCURVE");
        StepEntity item = pcurve.referenceToCurve().items().getFirst();
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
        if (item instanceof StepBSplineCurveWithKnots spline) {
            return buildBSplineCurve2(spline.id());
        }
        if (item instanceof StepTrimmedCurve trimmedCurve) {
            return buildTrimmedCurve2(trimmedCurve.id());
        }
        throw new UnsupportedGeometryException("PCURVE currently supports 2D LINE, CIRCLE, ELLIPSE, B_SPLINE_CURVE_WITH_KNOTS or TRIMMED_CURVE");
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
            case StepLine line -> buildLine(line.id());
            case StepCircle circle -> buildCircle(circle.id());
            case StepEllipse ellipse -> buildEllipse(ellipse.id());
            case StepSurfaceCurve surfaceCurve -> buildSurfaceCurve(surfaceCurve.id());
            case StepBSplineCurveWithKnots spline -> buildBSplineCurve(spline.id());
            default -> throw new UnsupportedGeometryException("TRIMMED_CURVE basis curve requires LINE, CIRCLE, ELLIPSE, SURFACE_CURVE or B_SPLINE_CURVE_WITH_KNOTS");
        };
        for (StepEntity trim : trimmedCurve.trim1()) {
            validateTrimPoint(trim, basis, "trim_1");
        }
        for (StepEntity trim : trimmedCurve.trim2()) {
            validateTrimPoint(trim, basis, "trim_2");
        }
        TrimmedCurve3 built = new TrimmedCurve3(basis);
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
        Curve3 curve3d = switch (surfaceCurve.curve3d()) {
            case StepLine line -> buildLine(line.id());
            case StepCircle circle -> buildCircle(circle.id());
            case StepEllipse ellipse -> buildEllipse(ellipse.id());
            case StepBSplineCurveWithKnots spline -> buildBSplineCurve(spline.id());
            default -> throw new UnsupportedGeometryException("SURFACE_CURVE curve_3d requires LINE, CIRCLE, ELLIPSE or B_SPLINE_CURVE_WITH_KNOTS");
        };
        SurfaceCurve3 built = new SurfaceCurve3(curve3d);
        surfaceCurves.put(id, built);
        return built;
    }

    public SurfaceCurve3 buildSeamCurve(int id) {
        StepSeamCurve seamCurve = requireEntity(id, StepSeamCurve.class, "SEAM_CURVE");
        Curve3 curve3d = switch (seamCurve.curve3d()) {
            case StepLine line -> buildLine(line.id());
            case StepCircle circle -> buildCircle(circle.id());
            case StepEllipse ellipse -> buildEllipse(ellipse.id());
            case StepBSplineCurveWithKnots spline -> buildBSplineCurve(spline.id());
            default -> throw new UnsupportedGeometryException("SEAM_CURVE curve_3d requires LINE, CIRCLE, ELLIPSE or B_SPLINE_CURVE_WITH_KNOTS");
        };
        return new SurfaceCurve3(curve3d);
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
        StepEdgeCurve edgeCurve = requireEntity(id, StepEdgeCurve.class, "EDGE_CURVE");
        Curve3 curve = switch (edgeCurve.edgeGeometry()) {
            case StepLine line -> buildLine(line.id());
            case StepCircle circle -> buildCircle(circle.id());
            case StepEllipse ellipse -> buildEllipse(ellipse.id());
            case StepBSplineCurveWithKnots spline -> buildBSplineCurve(spline.id());
            case StepSurfaceCurve surfaceCurve -> buildSurfaceCurve(surfaceCurve.id());
            case StepSeamCurve seamCurve -> buildSeamCurve(seamCurve.id());
            case StepTrimmedCurve trimmedCurve -> buildTrimmedCurve(trimmedCurve.id());
            default -> throw new UnsupportedGeometryException("EDGE_CURVE topology requires LINE, CIRCLE, ELLIPSE, B_SPLINE_CURVE_WITH_KNOTS, SURFACE_CURVE, SEAM_CURVE or TRIMMED_CURVE geometry");
        };
        Edge built = new Edge(
                buildVertex(edgeCurve.start().id()),
                buildVertex(edgeCurve.end().id()),
                curve,
                edgeCurve.sameSense()
        );
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
            default -> throw new UnsupportedGeometryException("FACE_BOUND construction requires EDGE_LOOP or VERTEX_LOOP");
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
        StepEntity geometry = faceGeometry(stepFace);
        if (!(geometry instanceof StepPlane plane)) {
            if (geometry instanceof StepCylindricalSurface cylindricalSurface) {
                buildCylindricalSurface(cylindricalSurface.id());
                throw new UnsupportedGeometryException(faceType + " construction for CYLINDRICAL_SURFACE is unsupported");
            }
            if (geometry instanceof StepConicalSurface conicalSurface) {
                buildConicalSurface(conicalSurface.id());
                throw new UnsupportedGeometryException(faceType + " construction for CONICAL_SURFACE is unsupported");
            }
            if (geometry instanceof StepSurfaceOfLinearExtrusion extrusionSurface) {
                buildCurve3(extrusionSurface.sweptCurve());
                buildVector(extrusionSurface.extrusionAxis().id());
                throw new UnsupportedGeometryException(faceType + " construction for SURFACE_OF_LINEAR_EXTRUSION is unsupported");
            }
            if (geometry instanceof StepSurfaceOfRevolution revolutionSurface) {
                buildCurve3(revolutionSurface.sweptCurve());
                buildAxis1Placement(revolutionSurface.axisPosition().id());
                throw new UnsupportedGeometryException(faceType + " construction for SURFACE_OF_REVOLUTION is unsupported");
            }
            if (geometry instanceof StepBSplineSurfaceWithKnots splineSurface) {
                buildBSplineSurface(splineSurface.id());
                throw new UnsupportedGeometryException(faceType + " construction for B_SPLINE_SURFACE_WITH_KNOTS is unsupported");
            }
            if (geometry instanceof StepToroidalSurface toroidalSurface) {
                buildToroidalSurface(toroidalSurface.id());
                throw new UnsupportedGeometryException(faceType + " construction for TOROIDAL_SURFACE is unsupported");
            }
            throw new UnsupportedGeometryException(faceType + " construction requires PLANE geometry");
        }
        List<FaceBound> bounds = stepFace.bounds().stream().map(bound -> buildFaceBound(bound.id())).toList();
        if (bounds.stream().noneMatch(FaceBound::outer) && bounds.size() == 1) {
            FaceBound bound = bounds.getFirst();
            bounds = List.of(FaceBound.outer(bound.loop(), bound.orientation()));
        }
        return new Face(buildPlane(plane.id()), bounds, faceSameSense(stepFace));
    }

    /**
     * Builds a shell from OPEN_SHELL or CLOSED_SHELL.
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
        } else if (entity instanceof StepClosedShell closedShell) {
            built = new Shell(closedShell.faces().stream().map(face -> buildFace(face.id())).toList(), true);
        } else {
            throw new StepResolutionException("entity #" + id + " is not an OPEN_SHELL or CLOSED_SHELL");
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
        StepManifoldSolidBrep solidBrep = requireEntity(id, StepManifoldSolidBrep.class, "MANIFOLD_SOLID_BREP");
        Solid built = new Solid(buildShell(solidBrep.outer().id()));
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

    private Curve3 buildCurve3(StepEntity curve) {
        return switch (curve) {
            case StepLine line -> buildLine(line.id());
            case StepCircle circle -> buildCircle(circle.id());
            case StepEllipse ellipse -> buildEllipse(ellipse.id());
            case StepBSplineCurveWithKnots spline -> buildBSplineCurve(spline.id());
            case StepSurfaceCurve surfaceCurve -> buildSurfaceCurve(surfaceCurve.id());
            case StepSeamCurve seamCurve -> buildSeamCurve(seamCurve.id());
            case StepTrimmedCurve trimmedCurve -> buildTrimmedCurve(trimmedCurve.id());
            default -> throw new UnsupportedGeometryException(
                    "surface directrix requires LINE, CIRCLE, ELLIPSE, B_SPLINE_CURVE_WITH_KNOTS, SURFACE_CURVE, SEAM_CURVE or TRIMMED_CURVE"
            );
        };
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
}
