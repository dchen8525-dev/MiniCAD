package com.minicad.step.semantic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minicad.common.Epsilon;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.Clothoid3;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.DegenerateCurve3;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Hyperbola3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Parabola3;
import com.minicad.geometry.Polyline3;
import com.minicad.geometry.RationalBSplineCurve3;
import com.minicad.geometry.SurfaceCurve3;
import com.minicad.geometry.TrimmedCurve3;
import com.minicad.geometry.Vector3;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.CompositeCurve2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.DegenerateCurve2;
import com.minicad.geometry2d.Direction2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Hyperbola2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Parabola2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.Polyline2;
import com.minicad.geometry2d.RationalBSplineCurve2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.geometry2d.Vector2;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepAxis1Placement;
import com.minicad.step.model.StepAxis2Placement2D;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepBezierCurve;
import com.minicad.step.model.StepBezierCurve2D;
import com.minicad.step.model.StepBSplineCurve;
import com.minicad.step.model.StepBSplineCurve2D;
import com.minicad.step.model.StepBSplineCurveWithKnots;
import com.minicad.step.model.StepBSplineCurveWithKnotsAndBreakpoints;
import com.minicad.step.model.StepBoundedCurve;
import com.minicad.step.model.StepBoundedCurve2D;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepCartesianTransformationOperator;
import com.minicad.step.model.StepCircle;
import com.minicad.step.model.StepCircle2D;
import com.minicad.step.model.StepClothoid;
import com.minicad.step.model.StepCompositeCurve;
import com.minicad.step.model.StepCompositeCurve2D;
import com.minicad.step.model.StepCompositeCurveOnSurface;
import com.minicad.step.model.StepCompositeCurveSegment;
import com.minicad.step.model.StepConicCurve;
import com.minicad.step.model.StepDegenerateCurve;
import com.minicad.step.model.StepDegenerateCurve2D;
import com.minicad.step.model.StepDegeneratePcurve;
import com.minicad.step.model.StepDirection;
import com.minicad.step.model.StepEllipse;
import com.minicad.step.model.StepEllipse2D;
import com.minicad.step.model.StepHyperbola2D;
import com.minicad.step.model.StepIndexedPolyCurve;
import com.minicad.step.model.StepIndexedPolyCurve2D;
import com.minicad.step.model.StepLine;
import com.minicad.step.model.StepLine2D;
import com.minicad.step.model.StepOffsetCurve2D;
import com.minicad.step.model.StepOffsetCurve3D;
import com.minicad.step.model.StepOrientedCurve;
import com.minicad.step.model.StepParabola2D;
import com.minicad.step.model.StepPiecewiseBezierCurve;
import com.minicad.step.model.StepPiecewiseBezierCurve2D;
import com.minicad.step.model.StepPolyline;
import com.minicad.step.model.StepPolyline2D;
import com.minicad.step.model.StepPcurve;
import com.minicad.step.model.StepQuasiUniformCurve;
import com.minicad.step.model.StepQuasiUniformCurve2D;
import com.minicad.step.model.StepRationalBSplineCurve;
import com.minicad.step.model.StepRationalBSplineCurve2D;
import com.minicad.step.model.StepSeamCurve;
import com.minicad.step.model.StepSurfaceCurve;
import com.minicad.step.model.StepTrimmedCurve;
import com.minicad.step.model.StepTrimmedCurve2D;
import com.minicad.step.model.StepUniformCurve;
import com.minicad.step.model.StepUniformCurve2D;
import com.minicad.step.model.StepCurve2D;
import com.minicad.step.model.StepVector;
import com.minicad.step.syntax.StepValue;
import com.minicad.step.model.StepGeometricReplica;
import com.minicad.step.model.StepMappedItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

/**
 * Builder for 2D and 3D curve geometry objects extracted from StepCadBuilder.
 *
 * This class handles construction of curve geometry objects:
 * - 2D curves: Line2, Circle2, Ellipse2, BSplineCurve2, Polyline2, etc.
 * - 3D curves: Line3, Circle, Ellipse3, BSplineCurve3, etc.
 *
 * Caching is provided to avoid rebuilding the same geometry multiple times.
 */
final class StepCadCurveBuilder {

    private static final Logger log = LoggerFactory.getLogger(StepCadCurveBuilder.class);

    // Entity lookup
    private final Map<Integer, StepEntity> entitiesById;

    // Dependencies
    private final StepCadGeometryBuilder geometryBuilder;
    private final StepCadGeometryOps geometryOps;
    private final StepTrimResolver trimResolver;

    // 2D geometry caches
    private final Map<Integer, Point2> points2d;
    private final Map<Integer, Direction2> directions2d;
    private final Map<Integer, Line2> lines2d;
    private final Map<Integer, Circle2> circles2d;
    private final Map<Integer, Ellipse2> ellipses2d;
    private final Map<Integer, Polyline2> polylines2d;
    private final Map<Integer, CompositeCurve2> compositeCurves2d;
    private final Map<Integer, BSplineCurve2> splineCurves2d;
    private final Map<Integer, RationalBSplineCurve2> rationalSplineCurves2d;
    private final Map<Integer, TrimmedCurve2> trimmedCurves2d;
    private final Map<Integer, Hyperbola2> hyperbolas2d;
    private final Map<Integer, Parabola2> parabolas2d;

    // 3D geometry caches
    private final Map<Integer, Line3> lines3d;
    private final Map<Integer, Circle> circles3d;
    private final Map<Integer, Ellipse3> ellipses3d;
    private final Map<Integer, Polyline3> polylines3d;
    private final Map<Integer, CompositeCurve3> compositeCurves3d;
    private final Map<Integer, BSplineCurve3> bsplineCurves3d;
    private final Map<Integer, RationalBSplineCurve3> rationalBsplineCurves3d;
    private final Map<Integer, TrimmedCurve3> trimmedCurves3d;
    private final Map<Integer, SurfaceCurve3> surfaceCurves3d;
    private final Map<Integer, Parabola3> parabolas3d;
    private final Map<Integer, Hyperbola3> hyperbolas3d;
    private final Map<Integer, Clothoid3> clothoids3d;

    // Callbacks for cross-dependencies
    private final IntFunction<Axis2Placement3D> buildPlacementCallback;
    private final IntFunction<com.minicad.geometry.SurfaceGeometry> buildSurfaceCallback;
    private final IntFunction<Curve3> buildCurve3Callback; // For curves not handled by this builder

    /**
     * Creates a new StepCadCurveBuilder with the specified cache maps and dependencies.
     *
     * @param entitiesById map of STEP entity ID to resolved entity
     * @param geometryBuilder builder for basic geometry (points, directions)
     * @param geometryOps geometry operations helper
     * @param trimResolver resolver for trim parameters
     * @param points2d cache for Point2 objects
     * @param directions2d cache for Direction2 objects
     * @param lines2d cache for Line2 objects
     * @param circles2d cache for Circle2 objects
     * @param ellipses2d cache for Ellipse2 objects
     * @param polylines2d cache for Polyline2 objects
     * @param compositeCurves2d cache for CompositeCurve2 objects
     * @param splineCurves2d cache for BSplineCurve2 objects
     * @param rationalSplineCurves2d cache for RationalBSplineCurve2 objects
     * @param trimmedCurves2d cache for TrimmedCurve2 objects
     * @param hyperbolas2d cache for Hyperbola2 objects
     * @param parabolas2d cache for Parabola2 objects
     * @param lines3d cache for Line3 objects
     * @param circles3d cache for Circle objects
     * @param ellipses3d cache for Ellipse3 objects
     * @param polylines3d cache for Polyline3 objects
     * @param compositeCurves3d cache for CompositeCurve3 objects
     * @param bsplineCurves3d cache for BSplineCurve3 objects
     * @param rationalBsplineCurves3d cache for RationalBSplineCurve3 objects
     * @param trimmedCurves3d cache for TrimmedCurve3 objects
     * @param surfaceCurves3d cache for SurfaceCurve3 objects
     * @param parabolas3d cache for Parabola3 objects
     * @param hyperbolas3d cache for Hyperbola3 objects
     * @param clothoids3d cache for Clothoid3 objects
     * @param buildPlacementCallback callback to build Axis2Placement3D
     * @param buildSurfaceCallback callback to build SurfaceGeometry
     * @param buildCurve3Callback callback to build Curve3 for types not handled by this builder
     */
    StepCadCurveBuilder(
            Map<Integer, StepEntity> entitiesById,
            StepCadGeometryBuilder geometryBuilder,
            StepCadGeometryOps geometryOps,
            StepTrimResolver trimResolver,
            Map<Integer, Point2> points2d,
            Map<Integer, Direction2> directions2d,
            Map<Integer, Line2> lines2d,
            Map<Integer, Circle2> circles2d,
            Map<Integer, Ellipse2> ellipses2d,
            Map<Integer, Polyline2> polylines2d,
            Map<Integer, CompositeCurve2> compositeCurves2d,
            Map<Integer, BSplineCurve2> splineCurves2d,
            Map<Integer, RationalBSplineCurve2> rationalSplineCurves2d,
            Map<Integer, TrimmedCurve2> trimmedCurves2d,
            Map<Integer, Hyperbola2> hyperbolas2d,
            Map<Integer, Parabola2> parabolas2d,
            Map<Integer, Line3> lines3d,
            Map<Integer, Circle> circles3d,
            Map<Integer, Ellipse3> ellipses3d,
            Map<Integer, Polyline3> polylines3d,
            Map<Integer, CompositeCurve3> compositeCurves3d,
            Map<Integer, BSplineCurve3> bsplineCurves3d,
            Map<Integer, RationalBSplineCurve3> rationalBsplineCurves3d,
            Map<Integer, TrimmedCurve3> trimmedCurves3d,
            Map<Integer, SurfaceCurve3> surfaceCurves3d,
            Map<Integer, Parabola3> parabolas3d,
            Map<Integer, Hyperbola3> hyperbolas3d,
            Map<Integer, Clothoid3> clothoids3d,
            IntFunction<Axis2Placement3D> buildPlacementCallback,
            IntFunction<com.minicad.geometry.SurfaceGeometry> buildSurfaceCallback,
            IntFunction<Curve3> buildCurve3Callback) {
        this.entitiesById = entitiesById;
        this.geometryBuilder = geometryBuilder;
        this.geometryOps = geometryOps;
        this.trimResolver = trimResolver;
        this.points2d = points2d;
        this.directions2d = directions2d;
        this.lines2d = lines2d;
        this.circles2d = circles2d;
        this.ellipses2d = ellipses2d;
        this.polylines2d = polylines2d;
        this.compositeCurves2d = compositeCurves2d;
        this.splineCurves2d = splineCurves2d;
        this.rationalSplineCurves2d = rationalSplineCurves2d;
        this.trimmedCurves2d = trimmedCurves2d;
        this.hyperbolas2d = hyperbolas2d;
        this.parabolas2d = parabolas2d;
        this.lines3d = lines3d;
        this.circles3d = circles3d;
        this.ellipses3d = ellipses3d;
        this.polylines3d = polylines3d;
        this.compositeCurves3d = compositeCurves3d;
        this.bsplineCurves3d = bsplineCurves3d;
        this.rationalBsplineCurves3d = rationalBsplineCurves3d;
        this.trimmedCurves3d = trimmedCurves3d;
        this.surfaceCurves3d = surfaceCurves3d;
        this.parabolas3d = parabolas3d;
        this.hyperbolas3d = hyperbolas3d;
        this.clothoids3d = clothoids3d;
        this.buildPlacementCallback = buildPlacementCallback;
        this.buildSurfaceCallback = buildSurfaceCallback;
        this.buildCurve3Callback = buildCurve3Callback;
    }

    // ==================== 2D Point and Direction ====================

    /**
     * Builds a Point2 from a STEP CARTESIAN_POINT entity.
     *
     * @param id the STEP entity ID
     * @return the Point2 geometry object
     */
    Point2 buildPoint2(int id) {
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
     * Builds a Direction2 from a STEP DIRECTION entity.
     *
     * @param id the STEP entity ID
     * @return the Direction2 geometry object
     */
    Direction2 buildDirection2(int id) {
        Direction2 existing = directions2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepDirection direction = requireEntity(id, StepDirection.class, "DIRECTION");
        if (direction.directionRatios().size() != 2) {
            throw new StepResolutionException("entity #" + id + " is not a 2D DIRECTION");
        }
        Direction2 built = Direction2.from(new Vector2(
                direction.directionRatios().get(0),
                direction.directionRatios().get(1)
        ));
        directions2d.put(id, built);
        return built;
    }

    // ==================== 2D Curve Builders ====================

    /**
     * Builds a Line2 from a STEP LINE entity.
     */
    public Line2 buildLine2(int id) {
        Line2 existing = lines2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepLine line = requireEntity(id, StepLine.class, "LINE");
        if (line.point().coordinates().size() != 2 || line.vector().isOrientation().directionRatios().size() != 2) {
            throw new StepResolutionException("entity #" + id + " is not a 2D LINE");
        }
        Line2 built = new Line2(
                buildPoint2(line.point().id()),
                buildDirection2(line.vector().isOrientation().id()),
                line.vector().magnitude()
        );
        lines2d.put(id, built);
        return built;
    }

    /**
     * Builds a Circle2 from a STEP CIRCLE entity.
     */
    public Circle2 buildCircle2(int id) {
        Circle2 existing = circles2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepCircle circle = requireEntity(id, StepCircle.class, "CIRCLE");
        if (!(circle.getPosition() instanceof StepAxis2Placement2D)) {
            throw new StepResolutionException("entity #" + id + " is not a 2D CIRCLE");
        }
        StepAxis2Placement2D placement2d = (StepAxis2Placement2D) circle.getPosition();
        Circle2 built = new Circle2(
                buildPoint2(placement2d.getLocation().id()),
                buildDirection2(placement2d.getRefDirection().id()),
                circle.getRadius()
        );
        circles2d.put(id, built);
        return built;
    }

    /**
     * Builds an Ellipse2 from a STEP ELLIPSE entity.
     */
    public Ellipse2 buildEllipse2(int id) {
        Ellipse2 existing = ellipses2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepEllipse ellipse = requireEntity(id, StepEllipse.class, "ELLIPSE");
        if (!(ellipse.getPosition() instanceof StepAxis2Placement2D)) {
            throw new StepResolutionException("entity #" + id + " is not a 2D ELLIPSE");
        }
        StepAxis2Placement2D placement2d = (StepAxis2Placement2D) ellipse.getPosition();
        Ellipse2 built = new Ellipse2(
                buildPoint2(placement2d.getLocation().id()),
                buildDirection2(placement2d.getRefDirection().id()),
                ellipse.getSemiAxis1(),
                ellipse.getSemiAxis2()
        );
        ellipses2d.put(id, built);
        return built;
    }

    /**
     * Builds a BSplineCurve2 from a STEP B_SPLINE_CURVE_WITH_KNOTS entity.
     */
    public BSplineCurve2 buildBSplineCurve2(int id) {
        BSplineCurve2 existing = splineCurves2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepBSplineCurveWithKnots spline = requireEntity(id, StepBSplineCurveWithKnots.class, "B_SPLINE_CURVE_WITH_KNOTS");
        List<Point2> controlPoints = new ArrayList<>(spline.getControlPoints().size());
        for (StepCartesianPoint point : spline.getControlPoints()) {
            if (point.coordinates().size() != 2) {
                throw new UnsupportedGeometryException("B_SPLINE_CURVE_WITH_KNOTS is not a 2D spline");
            }
            controlPoints.add(buildPoint2(point.id()));
        }
        BSplineCurve2 built = new BSplineCurve2(
                spline.getDegree(),
                controlPoints,
                spline.getKnotMultiplicities(),
                spline.getKnots()
        );
        splineCurves2d.put(id, built);
        return built;
    }

    /**
     * Builds a BSplineCurve2 from a STEP BEZIER_CURVE entity.
     */
    public BSplineCurve2 buildBezierCurve2(int id) {
        return buildImplicitBSplineCurve2(requireEntity(id, StepBezierCurve.class, "BEZIER_CURVE"));
    }

    /**
     * Builds a BSplineCurve2 from a STEP UNIFORM_CURVE entity.
     */
    public BSplineCurve2 buildUniformCurve2(int id) {
        return buildImplicitBSplineCurve2(requireEntity(id, StepUniformCurve.class, "UNIFORM_CURVE"));
    }

    /**
     * Builds a BSplineCurve2 from a STEP QUASI_UNIFORM_CURVE entity.
     */
    public BSplineCurve2 buildQuasiUniformCurve2(int id) {
        return buildImplicitBSplineCurve2(requireEntity(id, StepQuasiUniformCurve.class, "QUASI_UNIFORM_CURVE"));
    }

    /**
     * Builds a BSplineCurve2 from a STEP PIECEWISE_BEZIER_CURVE entity.
     */
    public BSplineCurve2 buildPiecewiseBezierCurve2(int id) {
        return buildImplicitBSplineCurve2(requireEntity(id, StepPiecewiseBezierCurve.class, "PIECEWISE_BEZIER_CURVE"));
    }

    /**
     * Builds a BSplineCurve2 from implicit B-spline curve data.
     */
    private BSplineCurve2 buildImplicitBSplineCurve2(StepEntity entity) {
        BSplineCurve2 existing = splineCurves2d.get(entity.id());
        if (existing != null) {
            return existing;
        }
        StepBSplineKnotGenerator.ImplicitBSplineCurveData spline = implicitBSplineCurveData(entity);
        List<Point2> controlPoints = new ArrayList<>(spline.getControlPoints().size());
        for (StepCartesianPoint point : spline.getControlPoints()) {
            if (point.coordinates().size() != 2) {
                throw new UnsupportedGeometryException(stepEntityTypeName(entity) + " is not a 2D spline");
            }
            controlPoints.add(buildPoint2(point.id()));
        }
        BSplineCurve2 built = new BSplineCurve2(
                spline.getDegree(),
                controlPoints,
                spline.getKnotMultiplicities(),
                spline.getKnots()
        );
        splineCurves2d.put(entity.id(), built);
        return built;
    }

    /**
     * Builds a RationalBSplineCurve2 from a STEP RATIONAL_B_SPLINE_CURVE entity.
     */
    public RationalBSplineCurve2 buildRationalBSplineCurve2(int id) {
        RationalBSplineCurve2 existing = rationalSplineCurves2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepRationalBSplineCurve spline = requireEntity(id, StepRationalBSplineCurve.class, "RATIONAL_B_SPLINE_CURVE");
        if (spline.getWeightsData().isEmpty()) {
            throw new UnsupportedGeometryException("RATIONAL_B_SPLINE_CURVE requires weights");
        }
        List<Point2> controlPoints = new ArrayList<>(spline.getControlPoints().size());
        for (StepCartesianPoint point : spline.getControlPoints()) {
            if (point.coordinates().size() != 2) {
                throw new UnsupportedGeometryException("RATIONAL_B_SPLINE_CURVE is not a 2D spline");
            }
            controlPoints.add(buildPoint2(point.id()));
        }
        RationalBSplineCurve2 built = new RationalBSplineCurve2(
                spline.getDegree(),
                controlPoints,
                spline.getWeightsData(),
                spline.getKnotMultiplicities(),
                spline.getKnots()
        );
        rationalSplineCurves2d.put(id, built);
        return built;
    }

    /**
     * Builds a Polyline2 from a STEP POLYLINE entity.
     */
    public Polyline2 buildPolyline2(int id) {
        Polyline2 existing = polylines2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepPolyline polyline = requireEntity(id, StepPolyline.class, "POLYLINE");
        List<Point2> points = new ArrayList<>(polyline.getPoints().size());
        for (StepCartesianPoint point : polyline.getPoints()) {
            if (point.coordinates().size() != 2) {
                throw new StepResolutionException("entity #" + id + " is not a 2D POLYLINE");
            }
            points.add(buildPoint2(point.id()));
        }
        Polyline2 built = new Polyline2(points);
        polylines2d.put(id, built);
        return built;
    }

    /**
     * Builds a TrimmedCurve2 from a STEP TRIMMED_CURVE entity.
     */
    public TrimmedCurve2 buildTrimmedCurve2(int id) {
        TrimmedCurve2 existing = trimmedCurves2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepTrimmedCurve trimmedCurve = requireEntity(id, StepTrimmedCurve.class, "TRIMMED_CURVE");
        Object basis = buildCurve2(trimmedCurve.getBasisCurve());
        if (!(basis instanceof Curve2)) {
            throw new UnsupportedGeometryException("TRIMMED_CURVE basis curve is not a supported 2D curve");
        }
        Curve2 basisCurve = (Curve2) basis;
        double trimParamStart = trimResolver.resolveTrimParam2(trimmedCurve.trim1(), basisCurve, "trim_1");
        double trimParamEnd = trimResolver.resolveTrimParam2(trimmedCurve.trim2(), basisCurve, "trim_2");
        TrimmedCurve2 built = new TrimmedCurve2(basisCurve, trimParamStart, trimParamEnd, trimmedCurve.isSenseAgreement());
        trimmedCurves2d.put(id, built);
        return built;
    }

    /**
     * Builds a CompositeCurve2 from a STEP COMPOSITE_CURVE entity.
     */
    public CompositeCurve2 buildCompositeCurve2(int id) {
        CompositeCurve2 existing = compositeCurves2d.get(id);
        if (existing != null) {
            return existing;
        }
        StepEntity entity = requireExistingEntity(id);
        List<StepCompositeCurveSegment> segments;
        if (entity instanceof StepCompositeCurve) {
            StepCompositeCurve compositeCurve = (StepCompositeCurve) entity;
            segments = compositeCurve.getSegments();
        } else if (entity instanceof StepCompositeCurveOnSurface) {
            StepCompositeCurveOnSurface compositeCurveOnSurface = (StepCompositeCurveOnSurface) entity;
            segments = compositeCurveOnSurface.getSegments();
        } else {
            throw new StepResolutionException("entity #" + id + " is not a COMPOSITE_CURVE");
        }
        List<Curve2> curves = new ArrayList<>(segments.size());
        for (StepCompositeCurveSegment segment : segments) {
            Object built = buildCurve2(segment.parentCurve());
            if (!(built instanceof Curve2)) {
                throw new UnsupportedGeometryException("COMPOSITE_CURVE segment is not a supported 2D curve");
            }
            Curve2 curve = (Curve2) built;
            curves.add(curve);
        }
        CompositeCurve2 built = new CompositeCurve2(curves);
        compositeCurves2d.put(id, built);
        return built;
    }

    /**
     * Builds an OffsetCurve2 from a STEP OFFSET_CURVE_2D entity.
     */
    public Curve2 buildOffsetCurve2(int id) {
        StepOffsetCurve2D offsetCurve = requireEntity(id, StepOffsetCurve2D.class, "OFFSET_CURVE_2D");
        Object basisObject = buildCurve2(offsetCurve.getBasisCurve());
        if (!(basisObject instanceof Curve2)) {
            throw new UnsupportedGeometryException("OFFSET_CURVE_2D basis curve is not a supported 2D curve");
        }
        Curve2 basisCurve = (Curve2) basisObject;
        return approximateOffsetCurve2(basisCurve, offsetCurve.getDistance());
    }

    /**
     * Builds a PCURVE or DEGENERATE_PCURVE as a 2D curve.
     */
    public Object buildPcurve2(int id) {
        StepEntity entity = requireEntity(id, StepEntity.class, "PCURVE or DEGENERATE_PCURVE");
        StepEntity item;
        if (entity instanceof StepPcurve) {
            StepPcurve pcurve = (StepPcurve) entity;
            item = pcurve.referenceToCurve().items().get(0);
        } else if (entity instanceof StepDegeneratePcurve) {
            StepDegeneratePcurve pcurve = (StepDegeneratePcurve) entity;
            item = pcurve.referenceToCurve().items().get(0);
        } else {
            throw new StepResolutionException("entity #" + id + " is not a PCURVE or DEGENERATE_PCURVE");
        }
        return buildCurve2(item);
    }

    /**
     * Builds a 2D curve from a STEP entity.
     * Dispatches to the appropriate build method based on entity type.
     */
    Object buildCurve2(StepEntity item) {
        if (item instanceof StepLine) {
            StepLine line = (StepLine) item;
            return buildLine2(line.id());
        }
        if (item instanceof StepCircle) {
            StepCircle circle = (StepCircle) item;
            return buildCircle2(circle.id());
        }
        if (item instanceof StepEllipse) {
            StepEllipse ellipse = (StepEllipse) item;
            return buildEllipse2(ellipse.id());
        }
        if (item instanceof StepPolyline) {
            StepPolyline polyline = (StepPolyline) item;
            return buildPolyline2(polyline.id());
        }
        if (item instanceof StepBezierCurve) {
            StepBezierCurve curve = (StepBezierCurve) item;
            return buildBezierCurve2(curve.id());
        }
        if (item instanceof StepUniformCurve) {
            StepUniformCurve curve = (StepUniformCurve) item;
            return buildUniformCurve2(curve.id());
        }
        if (item instanceof StepQuasiUniformCurve) {
            StepQuasiUniformCurve curve = (StepQuasiUniformCurve) item;
            return buildQuasiUniformCurve2(curve.id());
        }
        if (item instanceof StepPiecewiseBezierCurve) {
            StepPiecewiseBezierCurve curve = (StepPiecewiseBezierCurve) item;
            return buildPiecewiseBezierCurve2(curve.id());
        }
        if (item instanceof StepCompositeCurve) {
            StepCompositeCurve compositeCurve = (StepCompositeCurve) item;
            return buildCompositeCurve2(compositeCurve.id());
        }
        if (item instanceof StepCompositeCurveOnSurface) {
            StepCompositeCurveOnSurface compositeCurveOnSurface = (StepCompositeCurveOnSurface) item;
            return buildCompositeCurve2(compositeCurveOnSurface.id());
        }
        if (item instanceof StepConicCurve) {
            StepConicCurve conic = (StepConicCurve) item;
            return buildConicCurve2(conic);
        }
        if (item instanceof StepCircle2D) {
            StepCircle2D circle2D = (StepCircle2D) item;
            return buildCircle2D(circle2D);
        }
        if (item instanceof StepEllipse2D) {
            StepEllipse2D ellipse2D = (StepEllipse2D) item;
            return buildEllipse2D(ellipse2D);
        }
        if (item instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D offsetCurve2D = (StepOffsetCurve2D) item;
            return buildOffsetCurve2(offsetCurve2D.id());
        }
        if (item instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) item;
            return buildCurve2(orientedCurve.curveElement());
        }
        if (item instanceof StepGeometricReplica) {
            StepGeometricReplica replica = (StepGeometricReplica) item;
            return buildReplicaCurve2(replica);
        }
        if (item instanceof StepBSplineCurveWithKnots) {
            StepBSplineCurveWithKnots spline = (StepBSplineCurveWithKnots) item;
            return buildBSplineCurve2(spline.id());
        }
        if (item instanceof StepBSplineCurve) {
            StepBSplineCurve spline = (StepBSplineCurve) item;
            return buildBSplineCurve2(spline.id());
        }
        if (item instanceof StepRationalBSplineCurve) {
            StepRationalBSplineCurve spline = (StepRationalBSplineCurve) item;
            return buildRationalBSplineCurve2(spline.id());
        }
        if (item instanceof StepTrimmedCurve) {
            StepTrimmedCurve trimmedCurve = (StepTrimmedCurve) item;
            return buildTrimmedCurve2(trimmedCurve.id());
        }
        if (item instanceof StepIndexedPolyCurve) {
            StepIndexedPolyCurve polyCurve = (StepIndexedPolyCurve) item;
            return buildIndexedPolyCurve2(polyCurve);
        }
        if (item instanceof StepDegenerateCurve) {
            StepDegenerateCurve degenerateCurve = (StepDegenerateCurve) item;
            return buildDegenerateCurve2(degenerateCurve);
        }
        if (item instanceof StepClothoid) {
            StepClothoid clothoid = (StepClothoid) item;
            return buildClothoid2(clothoid);
        }
        // 2D-specific curve types
        if (item instanceof StepPolyline2D) {
            StepPolyline2D polyline2D = (StepPolyline2D) item;
            return buildPolyline2D(polyline2D);
        }
        if (item instanceof StepTrimmedCurve2D) {
            StepTrimmedCurve2D trimmedCurve2D = (StepTrimmedCurve2D) item;
            return buildTrimmedCurve2D(trimmedCurve2D);
        }
        if (item instanceof StepBSplineCurve2D) {
            StepBSplineCurve2D spline2D = (StepBSplineCurve2D) item;
            return buildBSplineCurve2D(spline2D);
        }
        if (item instanceof StepRationalBSplineCurve2D) {
            StepRationalBSplineCurve2D rationalSpline2D = (StepRationalBSplineCurve2D) item;
            return buildRationalBSplineCurve2D(rationalSpline2D);
        }
        if (item instanceof StepBezierCurve2D) {
            StepBezierCurve2D bezier2D = (StepBezierCurve2D) item;
            return buildBezierCurve2D(bezier2D);
        }
        if (item instanceof StepQuasiUniformCurve2D) {
            StepQuasiUniformCurve2D quasiUniform2D = (StepQuasiUniformCurve2D) item;
            return buildQuasiUniformCurve2D(quasiUniform2D);
        }
        if (item instanceof StepUniformCurve2D) {
            StepUniformCurve2D uniform2D = (StepUniformCurve2D) item;
            return buildUniformCurve2D(uniform2D);
        }
        if (item instanceof StepPiecewiseBezierCurve2D) {
            StepPiecewiseBezierCurve2D piecewiseBezier2D = (StepPiecewiseBezierCurve2D) item;
            return buildPiecewiseBezierCurve2D(piecewiseBezier2D);
        }
        if (item instanceof StepIndexedPolyCurve2D) {
            StepIndexedPolyCurve2D polyCurve2D = (StepIndexedPolyCurve2D) item;
            return buildIndexedPolyCurve2D(polyCurve2D);
        }
        if (item instanceof StepDegenerateCurve2D) {
            StepDegenerateCurve2D degenerateCurve2D = (StepDegenerateCurve2D) item;
            return buildDegenerateCurve2D(degenerateCurve2D);
        }
        if (item instanceof StepHyperbola2D) {
            StepHyperbola2D hyperbola2D = (StepHyperbola2D) item;
            return buildHyperbola2D(hyperbola2D);
        }
        if (item instanceof StepParabola2D) {
            StepParabola2D parabola2D = (StepParabola2D) item;
            return buildParabola2D(parabola2D);
        }
        if (item instanceof StepLine2D) {
            StepLine2D line2D = (StepLine2D) item;
            return buildLine2D(line2D);
        }
        // Bounded curve wraps an underlying 2D curve
        if (item instanceof StepBoundedCurve2D) {
            StepBoundedCurve2D boundedCurve2D = (StepBoundedCurve2D) item;
            return buildCurve2(boundedCurve2D.getCurve());
        }
        // Composite 2D curve
        if (item instanceof StepCompositeCurve2D) {
            StepCompositeCurve2D compositeCurve2D = (StepCompositeCurve2D) item;
            return buildCompositeCurve2D(compositeCurve2D);
        }
        // Bounded curve marker (3D) - marker type with no geometry data
        if (item instanceof StepBoundedCurve) {
            StepBoundedCurve boundedCurve = (StepBoundedCurve) item;
            StepEntity actual = entitiesById.get(boundedCurve.id());
            if (actual != null && actual != boundedCurve) {
                return buildCurve2(actual);
            }
            throw new UnsupportedGeometryException("BOUNDED_CURVE requires an underlying curve type");
        }
        // PCURVE and DEGENERATE_PCURVE: parameter-space curves on surfaces
        if (item instanceof StepPcurve) {
            StepPcurve pcurve = (StepPcurve) item;
            return buildPcurveCurve2(pcurve);
        }
        if (item instanceof StepDegeneratePcurve) {
            StepDegeneratePcurve pcurve = (StepDegeneratePcurve) item;
            return buildPcurveCurve2(pcurve);
        }
        // CURVE_2D: parametric curve with polynomial equation coefficients
        if (item instanceof StepCurve2D) {
            StepCurve2D curve2D = (StepCurve2D) item;
            return buildCurve2DParametric(curve2D);
        }
        // MAPPED_ITEM: dispatch through to mapping target
        if (item instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) item;
            return buildCurve2(mappedItem.mappingTarget());
        }
        throw new UnsupportedGeometryException("2D curve type " + stepEntityTypeName(item) + " is not supported");
    }

    // ==================== Private 2D Curve Builders ====================

    private CompositeCurve2 buildCompositeCurve2D(StepCompositeCurve2D compositeCurve2D) {
        CompositeCurve2 existing = compositeCurves2d.get(compositeCurve2D.id());
        if (existing != null) {
            return existing;
        }
        List<Curve2> curves = new ArrayList<>(compositeCurve2D.getSegments().size());
        for (StepCompositeCurveSegment segment : compositeCurve2D.getSegments()) {
            Object built = buildCurve2(segment.parentCurve());
            if (!(built instanceof Curve2)) {
                throw new UnsupportedGeometryException("COMPOSITE_CURVE_2D segment is not a supported 2D curve");
            }
            Curve2 curve = (Curve2) built;
            curves.add(curve);
        }
        CompositeCurve2 built = new CompositeCurve2(curves);
        compositeCurves2d.put(compositeCurve2D.id(), built);
        return built;
    }

    private Object buildPcurveCurve2(StepEntity entity) {
        StepEntity item;
        if (entity instanceof StepPcurve) {
            StepPcurve pcurve = (StepPcurve) entity;
            item = pcurve.referenceToCurve().items().get(0);
        } else if (entity instanceof StepDegeneratePcurve) {
            StepDegeneratePcurve pcurve = (StepDegeneratePcurve) entity;
            item = pcurve.referenceToCurve().items().get(0);
        } else {
            throw new StepResolutionException(stepEntityTypeName(entity) + " is not a PCURVE or DEGENERATE_PCURVE");
        }
        return buildCurve2(item);
    }

    private Curve2 buildConicCurve2(StepConicCurve conic) {
        if (!(conic.getPosition() instanceof StepAxis2Placement2D)) {
            throw new UnsupportedGeometryException("2D conic curve for " + conic.entityName() + " requires AXIS2_PLACEMENT_2D");
        }
        StepAxis2Placement2D placement2D = (StepAxis2Placement2D) conic.getPosition();
        Point2 origin = buildPoint2(placement2D.getLocation().id());
        Direction2 xDirection = buildDirection2(placement2D.getRefDirection().id());
        String entityName = conic.entityName();
        switch (entityName) {
            case "PARABOLA":
                return buildParabola2(origin, xDirection, conic.parameters());
            case "HYPERBOLA":
                return buildHyperbola2(origin, xDirection, conic.parameters());
            case "DEGENERATE_CONIC":
                return new Polyline2(List.of(origin, origin));
            case "CONIC_CURVE":
                try {
                    return buildParabola2(origin, xDirection, conic.parameters());
                } catch (UnsupportedGeometryException e) {
                    return buildHyperbola2(origin, xDirection, conic.parameters());
                }
            default:
                throw new UnsupportedGeometryException("PCURVE 2D item for " + conic.entityName() + " is unsupported");
        }
    }

    private Parabola2 buildParabola2(Point2 origin, Direction2 xDirection, List<Double> parameters) {
        if (parameters.isEmpty()) {
            throw new UnsupportedGeometryException("PARABOLA requires focal distance");
        }
        double focalDistance = parameters.get(0);
        if (!Double.isFinite(focalDistance) || focalDistance <= Epsilon.EPS) {
            throw new UnsupportedGeometryException("PARABOLA focal distance must be positive");
        }
        Direction2 yDirection = new Direction2(-xDirection.getY(), xDirection.getX());
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

    private Curve2 buildReplicaCurve2(StepGeometricReplica replica) {
        Object built = buildCurve2(replica.parent());
        if (!(built instanceof Curve2)) {
            throw new UnsupportedGeometryException(replica.entityName() + " parent is not a supported 2D curve");
        }
        Curve2 parent = (Curve2) built;
        return transformCurve2(parent, replica.transformation());
    }

    private Polyline2 buildIndexedPolyCurve2(StepIndexedPolyCurve polyCurve) {
        List<StepCartesianPoint> stepPoints = polyCurve.getPoints();
        List<Integer> indices = polyCurve.indices();
        List<Point2> points = indices.stream()
                .map(index -> {
                    StepCartesianPoint stepPoint = stepPoints.get(index);
                    CartesianPoint point3D = geometryBuilder.buildPoint(stepPoint.id());
                    return new Point2(point3D.getX(), point3D.getY());
                })
                .collect(Collectors.toList());
        if (polyCurve.isClosed() && !points.isEmpty()) {
            points = new ArrayList<>(points);
            points.add(points.get(0));
            points = List.copyOf(points);
        }
        return new Polyline2(points);
    }

    private Polyline2 buildDegenerateCurve2(StepDegenerateCurve degenerateCurve) {
        StepEntity basisEntity = degenerateCurve.getBasisCurve();
        if (basisEntity instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) basisEntity;
            List<Double> coords = point.coordinates();
            Point2 pt = coords.size() >= 2
                ? new Point2(coords.get(0), coords.get(1))
                : new Point2(0, 0);
            return new Polyline2(List.of(pt, pt));
        }
        try {
            Curve3 basisCurve = buildCurve3Internal(basisEntity);
            List<CartesianPoint> samples = basisCurve.sample(2);
            if (!samples.isEmpty()) {
                CartesianPoint first = samples.get(0);
                Point2 pt = new Point2(first.getX(), first.getY());
                return new Polyline2(List.of(pt, pt));
            }
        } catch (Exception e) {
            // Recoverable degradation: fall back to a degenerate default polyline.
            log.warn("2D curve projection failed; using degenerate default polyline", e);
        }
        return new Polyline2(List.of(new Point2(0, 0), new Point2(0, 0)));
    }

    private Polyline2 buildClothoid2(StepClothoid clothoid) {
        StepEntity positionEntity = clothoid.getPosition();
        Point2 origin;
        Direction2 xDir;
        if (positionEntity instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D placement2D = (StepAxis2Placement2D) positionEntity;
            origin = buildPoint2(placement2D.getLocation().id());
            xDir = buildDirection2(placement2D.getRefDirection().id());
        } else {
            origin = new Point2(0, 0);
            xDir = new Direction2(1, 0);
        }
        double xAxisIntercept = clothoid.xAxisIntercept();
        double curvature = clothoid.curvature();

        if (!Double.isFinite(xAxisIntercept) || !Double.isFinite(curvature) || curvature == 0) {
            return new Polyline2(List.of(origin, origin));
        }

        int segments = 64;
        List<Point2> points = new ArrayList<>(segments + 1);
        Direction2 yDir = new Direction2(-xDir.getY(), xDir.getX());

        double A = xAxisIntercept / Math.sqrt(Math.PI / 2);
        double maxT = Math.sqrt(Math.abs(curvature) * Math.PI);

        for (int i = 0; i <= segments; i++) {
            double t = (maxT * i) / segments;
            double fresnelC = fresnelCos(t);
            double fresnelS = fresnelSin(t);
            double x = A * fresnelC;
            double y = A * fresnelS;
            Point2 pt = origin.add(xDir.asVector().scale(x).add(yDir.asVector().scale(y)));
            points.add(pt);
        }
        return new Polyline2(points);
    }

    private double fresnelCos(double t) {
        if (t < 0.5) {
            return t - t*t*t*t*t/10.0 + t*t*t*t*t*t*t*t*t/216.0;
        }
        double t2 = t * t;
        return 0.5 + Math.sin(t2) / (2 * Math.PI * t);
    }

    private double fresnelSin(double t) {
        if (t < 0.5) {
            return t*t*t/3.0 - t*t*t*t*t*t*t/42.0;
        }
        double t2 = t * t;
        return 0.5 - Math.cos(t2) / (2 * Math.PI * t);
    }

    // 2D-specific curve types

    private Polyline2 buildPolyline2D(StepPolyline2D polyline2D) {
        List<Point2> points = polyline2D.getPoints().stream()
                .map(p -> buildPoint2(p.id()))
                .collect(Collectors.toList());
        return new Polyline2(points);
    }

    private TrimmedCurve2 buildTrimmedCurve2D(StepTrimmedCurve2D trimmedCurve2D) {
        Curve2 basisCurve = (Curve2) buildCurve2(trimmedCurve2D.getBasisCurve());
        double trim1 = trimmedCurve2D.trim1();
        double trim2 = trimmedCurve2D.trim2();
        return new TrimmedCurve2(basisCurve, trim1, trim2, trimmedCurve2D.isSenseAgreement());
    }

    private BSplineCurve2 buildBSplineCurve2D(StepBSplineCurve2D spline2D) {
        BSplineCurve2 existing = splineCurves2d.get(spline2D.id());
        if (existing != null) {
            return existing;
        }
        List<Point2> controlPoints = spline2D.getControlPoints().stream()
                .map(p -> buildPoint2(p.id()))
                .collect(Collectors.toList());
        // A bare b_spline_curve carries no knot data; STEP's implied form is uniform.
        BSplineCurve2 built = new BSplineCurve2(
                spline2D.getDegree(),
                controlPoints,
                StepBSplineKnotGenerator.uniformMultiplicities(controlPoints.size(), spline2D.getDegree()),
                StepBSplineKnotGenerator.uniformKnots(controlPoints.size(), spline2D.getDegree()));
        splineCurves2d.put(spline2D.id(), built);
        return built;
    }

    private RationalBSplineCurve2 buildRationalBSplineCurve2D(StepRationalBSplineCurve2D rationalSpline2D) {
        RationalBSplineCurve2 existing = rationalSplineCurves2d.get(rationalSpline2D.id());
        if (existing != null) {
            return existing;
        }
        List<Point2> controlPoints = rationalSpline2D.getControlPoints().stream()
                .map(p -> buildPoint2(p.id()))
                .collect(Collectors.toList());
        RationalBSplineCurve2 built = new RationalBSplineCurve2(
                rationalSpline2D.getDegree(),
                controlPoints,
                rationalSpline2D.getWeights(),
                StepBSplineKnotGenerator.uniformMultiplicities(controlPoints.size(), rationalSpline2D.getDegree()),
                StepBSplineKnotGenerator.uniformKnots(controlPoints.size(), rationalSpline2D.getDegree()));
        rationalSplineCurves2d.put(rationalSpline2D.id(), built);
        return built;
    }

    private BSplineCurve2 buildBezierCurve2D(StepBezierCurve2D bezier2D) {
        return buildImplicitBSplineCurve2D(bezier2D.id(), bezier2D.getDegree(), bezier2D.getControlPoints(), "BEZIER");
    }

    private BSplineCurve2 buildQuasiUniformCurve2D(StepQuasiUniformCurve2D quasiUniform2D) {
        return buildImplicitBSplineCurve2D(quasiUniform2D.id(), quasiUniform2D.getDegree(), quasiUniform2D.getControlPoints(), "QUASI_UNIFORM");
    }

    private BSplineCurve2 buildUniformCurve2D(StepUniformCurve2D uniform2D) {
        return buildImplicitBSplineCurve2D(uniform2D.id(), uniform2D.getDegree(), uniform2D.getControlPoints(), "UNIFORM");
    }

    private BSplineCurve2 buildPiecewiseBezierCurve2D(StepPiecewiseBezierCurve2D piecewiseBezier2D) {
        return buildImplicitBSplineCurve2D(piecewiseBezier2D.id(), piecewiseBezier2D.getDegree(), piecewiseBezier2D.getControlPoints(), "PIECEWISE_BEZIER");
    }

    private BSplineCurve2 buildImplicitBSplineCurve2D(int id, int degree, List<StepCartesianPoint> controlPoints, String impliedForm) {
        BSplineCurve2 existing = splineCurves2d.get(id);
        if (existing != null) {
            return existing;
        }
        List<Point2> points = controlPoints.stream()
                .map(p -> buildPoint2(p.id()))
                .collect(Collectors.toList());
        int count = points.size();
        List<Integer> multiplicities;
        List<Double> knots;
        if ("BEZIER".equals(impliedForm)) {
            if (count != degree + 1) {
                throw new UnsupportedGeometryException("BEZIER_CURVE_2D requires controlPointCount = degree + 1");
            }
            multiplicities = List.of(degree + 1, degree + 1);
            knots = List.of(0.0, 1.0);
        } else if ("UNIFORM".equals(impliedForm)) {
            multiplicities = StepBSplineKnotGenerator.uniformMultiplicities(count, degree);
            knots = StepBSplineKnotGenerator.uniformKnots(count, degree);
        } else if ("QUASI_UNIFORM".equals(impliedForm)) {
            multiplicities = StepBSplineKnotGenerator.quasiUniformMultiplicities(count, degree);
            knots = StepBSplineKnotGenerator.quasiUniformKnots(count, degree);
        } else {
            multiplicities = StepBSplineKnotGenerator.piecewiseBezierMultiplicities(count, degree, impliedForm);
            knots = StepBSplineKnotGenerator.piecewiseBezierKnots(count, degree, impliedForm);
        }
        BSplineCurve2 built = new BSplineCurve2(degree, points, multiplicities, knots);
        splineCurves2d.put(id, built);
        return built;
    }

    private Polyline2 buildIndexedPolyCurve2D(StepIndexedPolyCurve2D polyCurve2D) {
        List<StepCartesianPoint> stepPoints = polyCurve2D.getPoints();
        List<Integer> indices = polyCurve2D.indices();
        List<Point2> points = indices.stream()
                .map(index -> buildPoint2(stepPoints.get(index).id()))
                .collect(Collectors.toList());
        return new Polyline2(points);
    }

    private DegenerateCurve2 buildDegenerateCurve2D(StepDegenerateCurve2D degenerateCurve2D) {
        Point2 point = buildPoint2(degenerateCurve2D.point().id());
        return new DegenerateCurve2(point);
    }

    private Hyperbola2 buildHyperbola2D(StepHyperbola2D hyperbola2D) {
        Hyperbola2 existing = hyperbolas2d.get(hyperbola2D.id());
        if (existing != null) {
            return existing;
        }
        StepAxis2Placement2D position = hyperbola2D.getPosition();
        Point2 center = buildPoint2(position.getLocation().id());
        Direction2 xDir = buildDirection2(position.getRefDirection().id());
        Hyperbola2 built = new Hyperbola2(center, xDir, hyperbola2D.getSemiAxis1(), hyperbola2D.getSemiAxis2());
        hyperbolas2d.put(hyperbola2D.id(), built);
        return built;
    }

    private Parabola2 buildParabola2D(StepParabola2D parabola2D) {
        Parabola2 existing = parabolas2d.get(parabola2D.id());
        if (existing != null) {
            return existing;
        }
        StepAxis2Placement2D position = parabola2D.getPosition();
        Point2 center = buildPoint2(position.getLocation().id());
        Direction2 xDir = buildDirection2(position.getRefDirection().id());
        Parabola2 built = new Parabola2(center, xDir, parabola2D.focalDist());
        parabolas2d.put(parabola2D.id(), built);
        return built;
    }

    private Line2 buildLine2D(StepLine2D line2D) {
        Point2 point = buildPoint2(line2D.point2d().id());
        Direction2 dir = buildDirection2(line2D.direction2d().id());
        return new Line2(point, dir);
    }

    private Circle2 buildCircle2D(StepCircle2D circle2D) {
        Circle2 existing = circles2d.get(circle2D.id());
        if (existing != null) {
            return existing;
        }
        StepAxis2Placement2D position = circle2D.getPosition();
        Point2 center = buildPoint2(position.getLocation().id());
        Direction2 xDir = buildDirection2(position.getRefDirection().id());
        Circle2 built = new Circle2(center, xDir, circle2D.getRadius());
        circles2d.put(circle2D.id(), built);
        return built;
    }

    private Ellipse2 buildEllipse2D(StepEllipse2D ellipse2D) {
        Ellipse2 existing = ellipses2d.get(ellipse2D.id());
        if (existing != null) {
            return existing;
        }
        StepAxis2Placement2D position = ellipse2D.getPosition();
        Point2 center = buildPoint2(position.getLocation().id());
        Direction2 xDir = buildDirection2(position.getRefDirection().id());
        Ellipse2 built = new Ellipse2(center, xDir, ellipse2D.getSemiAxis1(), ellipse2D.getSemiAxis2());
        ellipses2d.put(ellipse2D.id(), built);
        return built;
    }

    private Polyline2 buildCurve2DParametric(StepCurve2D curve2D) {
        double[] eq = curve2D.equation();
        if (eq.length < 2) {
            throw new UnsupportedGeometryException("CURVE_2D equation must have at least 2 coefficients");
        }
        int half = eq.length / 2;
        double[] xCoeffs = Arrays.copyOfRange(eq, 0, half);
        double[] yCoeffs = Arrays.copyOfRange(eq, half, eq.length);

        Axis2Placement3D placement = null;
        if (curve2D.getPosition() instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D pos2D = (StepAxis2Placement2D) curve2D.getPosition();
            CartesianPoint origin = geometryBuilder.buildPoint(pos2D.getLocation().id());
            Direction3 xDir = new Direction3(1, 0, 0);
            if (pos2D.getRefDirection() != null) {
                StepDirection dir = pos2D.getRefDirection();
                List<Double> dirs = dir.directionRatios();
                if (dirs != null && dirs.size() >= 2) {
                    xDir = Direction3.from(new com.minicad.geometry.Vector3(dirs.get(0), dirs.get(1), 0));
                }
            }
            Direction3 axis = Direction3.zAxis();
            placement = new Axis2Placement3D(origin, axis, xDir);
        }

        int samples = Math.max(64, eq.length * 16);
        List<Point2> points = new ArrayList<>(samples + 1);
        for (int i = 0; i <= samples; i++) {
            double t = (double) i / samples;
            double x = evaluatePolynomial(xCoeffs, t);
            double y = evaluatePolynomial(yCoeffs, t);
            if (placement != null) {
                double gx = placement.getLocation().getX() + x * placement.xDirection().getX() + y * placement.yDirection().getX();
                double gy = placement.getLocation().getY() + x * placement.xDirection().getY() + y * placement.yDirection().getY();
                points.add(new Point2(gx, gy));
            } else {
                points.add(new Point2(x, y));
            }
        }
        return new Polyline2(points);
    }

    private static double evaluatePolynomial(double[] coeffs, double t) {
        double result = 0.0;
        double tPower = 1.0;
        for (double coeff : coeffs) {
            result += coeff * tPower;
            tPower *= t;
        }
        return result;
    }

    // ==================== Helper Methods ====================

    private Curve2 approximateOffsetCurve2(Curve2 basisCurve, double distance) {
        return geometryOps.approximateOffsetCurve2(basisCurve, distance);
    }

    private Curve2 transformCurve2(Curve2 curve, StepCartesianTransformationOperator transformation) {
        return geometryOps.transformCurve2(curve, transformation);
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

    private static String stepEntityTypeName(StepEntity entity) {
        return entity.getClass().getSimpleName().replace("Step", "");
    }

    // ==================== Implicit B-Spline Curve Data ====================

    private StepBSplineKnotGenerator.ImplicitBSplineCurveData implicitBSplineCurveData(StepEntity entity) {
        if (entity instanceof StepBezierCurve) {
            StepBezierCurve curve = (StepBezierCurve) entity;
            return StepBSplineKnotGenerator.implicitBezierCurve(curve.getDegree(), curve.getControlPoints(), stepEntityTypeName(entity));
        }
        if (entity instanceof StepUniformCurve) {
            StepUniformCurve curve = (StepUniformCurve) entity;
            return StepBSplineKnotGenerator.implicitUniformCurve(curve.getDegree(), curve.getControlPoints(), stepEntityTypeName(entity));
        }
        if (entity instanceof StepQuasiUniformCurve) {
            StepQuasiUniformCurve curve = (StepQuasiUniformCurve) entity;
            return StepBSplineKnotGenerator.implicitQuasiUniformCurve(curve.getDegree(), curve.getControlPoints(), stepEntityTypeName(entity));
        }
        if (entity instanceof StepPiecewiseBezierCurve) {
            StepPiecewiseBezierCurve curve = (StepPiecewiseBezierCurve) entity;
            return StepBSplineKnotGenerator.implicitPiecewiseBezierCurve(curve.getDegree(), curve.getControlPoints(), stepEntityTypeName(entity));
        }
        throw new UnsupportedGeometryException(stepEntityTypeName(entity) + " implicit knot data is unsupported");
    }

    // ==================== 3D Curve Builders ====================

    /**
     * Builds a Line3 from a STEP LINE entity.
     */
    public Line3 buildLine3(int id) {
        Line3 existing = lines3d.get(id);
        if (existing != null) {
            return existing;
        }
        StepLine line = requireEntity(id, StepLine.class, "LINE");
        Line3 built = new Line3(
                geometryBuilder.buildPoint(line.point().id()),
                geometryBuilder.buildDirection(line.vector().isOrientation().id()),
                line.vector().magnitude()
        );
        lines3d.put(id, built);
        return built;
    }

    /**
     * Builds a Circle from a STEP CIRCLE entity (3D).
     */
    public Circle buildCircle3(int id) {
        Circle existing = circles3d.get(id);
        if (existing != null) {
            return existing;
        }
        StepCircle circle = requireEntity(id, StepCircle.class, "CIRCLE");
        if (!(circle.getPosition() instanceof StepAxis2Placement3D)) {
            throw new StepResolutionException("entity #" + id + " is not a 3D CIRCLE");
        }
        StepAxis2Placement3D placement3d = (StepAxis2Placement3D) circle.getPosition();
        Circle built = new Circle(buildPlacementCallback.apply(placement3d.id()), circle.getRadius());
        circles3d.put(id, built);
        return built;
    }

    /**
     * Builds an Ellipse3 from a STEP ELLIPSE entity (3D).
     */
    public Ellipse3 buildEllipse3(int id) {
        Ellipse3 existing = ellipses3d.get(id);
        if (existing != null) {
            return existing;
        }
        StepEllipse ellipse = requireEntity(id, StepEllipse.class, "ELLIPSE");
        if (!(ellipse.getPosition() instanceof StepAxis2Placement3D)) {
            throw new StepResolutionException("entity #" + id + " is not a 3D ELLIPSE");
        }
        StepAxis2Placement3D placement3d = (StepAxis2Placement3D) ellipse.getPosition();
        Ellipse3 built = new Ellipse3(buildPlacementCallback.apply(placement3d.id()), ellipse.getSemiAxis1(), ellipse.getSemiAxis2());
        ellipses3d.put(id, built);
        return built;
    }

    /**
     * Builds a Polyline3 from a STEP POLYLINE entity.
     */
    public Polyline3 buildPolyline3(int id) {
        Polyline3 existing = polylines3d.get(id);
        if (existing != null) {
            return existing;
        }
        StepPolyline polyline = requireEntity(id, StepPolyline.class, "POLYLINE");
        List<CartesianPoint> points = polyline.getPoints().stream()
                .map(point -> geometryBuilder.buildPoint(point.id()))
                .collect(Collectors.toList());
        Polyline3 built = new Polyline3(points);
        polylines3d.put(id, built);
        return built;
    }

    /**
     * Builds a CompositeCurve3 from a STEP COMPOSITE_CURVE entity.
     */
    public CompositeCurve3 buildCompositeCurve3(int id) {
        CompositeCurve3 existing = compositeCurves3d.get(id);
        if (existing != null) {
            return existing;
        }
        StepEntity entity = requireExistingEntity(id);
        List<StepCompositeCurveSegment> segments;
        if (entity instanceof StepCompositeCurve) {
            StepCompositeCurve compositeCurve = (StepCompositeCurve) entity;
            segments = compositeCurve.getSegments();
        } else if (entity instanceof StepCompositeCurveOnSurface) {
            StepCompositeCurveOnSurface compositeCurveOnSurface = (StepCompositeCurveOnSurface) entity;
            segments = compositeCurveOnSurface.getSegments();
        } else {
            throw new StepResolutionException("entity #" + id + " is not a COMPOSITE_CURVE");
        }
        List<Curve3> curves = segments.stream()
                .map(segment -> buildCurve3Internal(segment.parentCurve()))
                .collect(Collectors.toList());
        CompositeCurve3 built = new CompositeCurve3(curves);
        compositeCurves3d.put(id, built);
        return built;
    }

    /**
     * Builds a BSplineCurve3 from a STEP B_SPLINE_CURVE_WITH_KNOTS entity.
     */
    public BSplineCurve3 buildBSplineCurve3(int id) {
        BSplineCurve3 existing = bsplineCurves3d.get(id);
        if (existing != null) {
            return existing;
        }
        StepBSplineCurveWithKnots spline = requireEntity(id, StepBSplineCurveWithKnots.class, "B_SPLINE_CURVE_WITH_KNOTS");
        List<CartesianPoint> controlPoints = spline.getControlPoints().stream()
                .map(point -> geometryBuilder.buildPoint(point.id()))
                .collect(Collectors.toList());
        BSplineCurve3 built = new BSplineCurve3(spline.getDegree(), controlPoints, spline.getKnotMultiplicities(), spline.getKnots());
        bsplineCurves3d.put(id, built);
        return built;
    }

    /**
     * Builds a BSplineCurve3 from a STEP B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS entity.
     */
    public BSplineCurve3 buildBSplineCurveWithBreakpoints(int id) {
        BSplineCurve3 existing = bsplineCurves3d.get(id);
        if (existing != null) {
            return existing;
        }
        StepBSplineCurveWithKnotsAndBreakpoints spline = requireEntity(id, StepBSplineCurveWithKnotsAndBreakpoints.class,
                "B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS");
        List<CartesianPoint> controlPoints = spline.getControlPoints().stream()
                .map(point -> geometryBuilder.buildPoint(point.id()))
                .collect(Collectors.toList());
        BSplineCurve3 built = new BSplineCurve3(spline.getDegree(), controlPoints, spline.getKnotMultiplicities(), spline.getKnots());
        bsplineCurves3d.put(id, built);
        return built;
    }

    /**
     * Builds a RationalBSplineCurve3 from a STEP RATIONAL_B_SPLINE_CURVE entity.
     */
    public RationalBSplineCurve3 buildRationalBSplineCurve3(int id) {
        RationalBSplineCurve3 existing = rationalBsplineCurves3d.get(id);
        if (existing != null) {
            return existing;
        }
        StepRationalBSplineCurve spline = requireEntity(id, StepRationalBSplineCurve.class, "RATIONAL_B_SPLINE_CURVE");
        if (spline.getWeightsData().isEmpty()) {
            throw new UnsupportedGeometryException("RATIONAL_B_SPLINE_CURVE requires weights");
        }
        List<CartesianPoint> controlPoints = spline.getControlPoints().stream()
                .map(point -> geometryBuilder.buildPoint(point.id()))
                .collect(Collectors.toList());
        RationalBSplineCurve3 built = new RationalBSplineCurve3(
                spline.getDegree(),
                controlPoints,
                spline.getWeightsData(),
                spline.getKnotMultiplicities(),
                spline.getKnots()
        );
        rationalBsplineCurves3d.put(id, built);
        return built;
    }

    /**
     * Builds a TrimmedCurve3 from a STEP TRIMMED_CURVE entity.
     */
    public TrimmedCurve3 buildTrimmedCurve3(int id) {
        TrimmedCurve3 existing = trimmedCurves3d.get(id);
        if (existing != null) {
            return existing;
        }
        StepTrimmedCurve trimmedCurve = requireEntity(id, StepTrimmedCurve.class, "TRIMMED_CURVE");
        Curve3 basis = buildCurve3Internal(trimmedCurve.getBasisCurve());
        for (StepValue trim : trimmedCurve.trim1()) {
            trimResolver.validateTrimValue(trim, basis, "trim_1");
        }
        for (StepValue trim : trimmedCurve.trim2()) {
            trimResolver.validateTrimValue(trim, basis, "trim_2");
        }
        double trimParamStart = trimResolver.resolveTrimParameter(trimmedCurve.trim1(), basis, "trim_1");
        double trimParamEnd = trimResolver.resolveTrimParameter(trimmedCurve.trim2(), basis, "trim_2");
        TrimmedCurve3 built = new TrimmedCurve3(basis, trimParamStart, trimParamEnd, trimmedCurve.isSenseAgreement());
        trimmedCurves3d.put(id, built);
        return built;
    }

    /**
     * Builds a SurfaceCurve3 from a STEP SURFACE_CURVE entity.
     */
    public SurfaceCurve3 buildSurfaceCurve3(int id) {
        SurfaceCurve3 existing = surfaceCurves3d.get(id);
        if (existing != null) {
            return existing;
        }
        StepSurfaceCurve surfaceCurve = requireEntity(id, StepSurfaceCurve.class, "SURFACE_CURVE");
        Curve3 curve3d = buildCurve3Internal(surfaceCurve.getCurve3d());
        SurfaceCurve3 built = new SurfaceCurve3(curve3d, buildSurfaceCurveBindings(surfaceCurve.associatedGeometry()));
        surfaceCurves3d.put(id, built);
        return built;
    }

    /**
     * Builds a SurfaceCurve3 from a STEP SEAM_CURVE entity.
     */
    public SurfaceCurve3 buildSeamCurve(int id) {
        StepSeamCurve seamCurve = requireEntity(id, StepSeamCurve.class, "SEAM_CURVE");
        Curve3 curve3d = buildCurve3Internal(seamCurve.getCurve3d());
        return new SurfaceCurve3(curve3d, buildSurfaceCurveBindings(seamCurve.associatedGeometry()));
    }

    /**
     * Builds a Parabola3 from a STEP PARABOLA (via StepConicCurve) entity.
     */
    public Parabola3 buildParabola3(int id) {
        Parabola3 existing = parabolas3d.get(id);
        if (existing != null) {
            return existing;
        }
        StepConicCurve conic = requireEntity(id, StepConicCurve.class, "PARABOLA");
        if (!"PARABOLA".equals(conic.entityName())) {
            throw new StepResolutionException("entity #" + id + " is not a PARABOLA");
        }
        if (!(conic.getPosition() instanceof StepAxis2Placement3D)) {
            throw new StepResolutionException("entity #" + id + " is not a 3D PARABOLA");
        }
        StepAxis2Placement3D placement3d = (StepAxis2Placement3D) conic.getPosition();
        if (conic.parameters().isEmpty()) {
            throw new UnsupportedGeometryException("PARABOLA requires focal distance");
        }
        double focalDistance = conic.parameters().get(0);
        if (!Double.isFinite(focalDistance) || focalDistance <= Epsilon.EPS) {
            throw new UnsupportedGeometryException("PARABOLA focal distance must be positive");
        }
        Parabola3 built = new Parabola3(buildPlacementCallback.apply(placement3d.id()), focalDistance);
        parabolas3d.put(id, built);
        return built;
    }

    /**
     * Builds a Hyperbola3 from a STEP HYPERBOLA (via StepConicCurve) entity.
     */
    public Hyperbola3 buildHyperbola3(int id) {
        Hyperbola3 existing = hyperbolas3d.get(id);
        if (existing != null) {
            return existing;
        }
        StepConicCurve conic = requireEntity(id, StepConicCurve.class, "HYPERBOLA");
        if (!"HYPERBOLA".equals(conic.entityName())) {
            throw new StepResolutionException("entity #" + id + " is not a HYPERBOLA");
        }
        if (!(conic.getPosition() instanceof StepAxis2Placement3D)) {
            throw new StepResolutionException("entity #" + id + " is not a 3D HYPERBOLA");
        }
        StepAxis2Placement3D placement3d = (StepAxis2Placement3D) conic.getPosition();
        if (conic.parameters().size() < 2) {
            throw new UnsupportedGeometryException("HYPERBOLA requires semi-axis and semi-imaginary-axis");
        }
        double semiAxisA = conic.parameters().get(0);
        double semiAxisB = conic.parameters().get(1);
        if (!Double.isFinite(semiAxisA) || !Double.isFinite(semiAxisB)
                || semiAxisA <= Epsilon.EPS || semiAxisB <= Epsilon.EPS) {
            throw new UnsupportedGeometryException("HYPERBOLA axes must be positive");
        }
        Hyperbola3 built = new Hyperbola3(buildPlacementCallback.apply(placement3d.id()), semiAxisA, semiAxisB);
        hyperbolas3d.put(id, built);
        return built;
    }

    /**
     * Builds a Clothoid3 from a STEP CLOTHOID entity.
     */
    public Clothoid3 buildClothoid3(int id) {
        Clothoid3 existing = clothoids3d.get(id);
        if (existing != null) {
            return existing;
        }
        StepClothoid clothoid = requireEntity(id, StepClothoid.class, "CLOTHOID");
        // CLOTHOID uses 2D placement, need to convert to 3D
        Axis2Placement3D position = convert2DPlacementTo3D(clothoid.getPosition());
        double xAxisIntercept = clothoid.xAxisIntercept();
        double curvature = clothoid.curvature();
        if (!Double.isFinite(xAxisIntercept) || !Double.isFinite(curvature)) {
            throw new UnsupportedGeometryException("CLOTHOID requires finite xAxisIntercept and curvature");
        }
        if (Math.abs(curvature) < Epsilon.EPS) {
            throw new UnsupportedGeometryException("CLOTHOID curvature must be non-zero");
        }
        Clothoid3 built = new Clothoid3(position, xAxisIntercept, curvature);
        clothoids3d.put(id, built);
        return built;
    }

    /**
     * Builds a Curve3 from a STEP OFFSET_CURVE_3D entity.
     */
    public Curve3 buildOffsetCurve3(int id) {
        StepOffsetCurve3D offsetCurve = requireEntity(id, StepOffsetCurve3D.class, "OFFSET_CURVE_3D");
        Curve3 basisCurve = buildCurve3Internal(offsetCurve.getBasisCurve());
        return approximateOffsetCurve3(basisCurve, offsetCurve.getDistance(), geometryBuilder.buildDirection(offsetCurve.getRefDirection().id()));
    }

    // ==================== 3D Curve Internal Dispatcher ====================

    /**
     * Internal dispatcher for building 3D curves from STEP entities.
     * Handles the core 3D curve types managed by this builder.
     */
    Curve3 buildCurve3Internal(StepEntity curve) {
        if (curve instanceof StepLine) {
            StepLine line = (StepLine) curve;
            return buildLine3(line.id());
        }
        if (curve instanceof StepCircle) {
            StepCircle circle = (StepCircle) curve;
            return buildCircle3(circle.id());
        }
        if (curve instanceof StepEllipse) {
            StepEllipse ellipse = (StepEllipse) curve;
            return buildEllipse3(ellipse.id());
        }
        if (curve instanceof StepPolyline) {
            StepPolyline polyline = (StepPolyline) curve;
            return buildPolyline3(polyline.id());
        }
        if (curve instanceof StepBezierCurve) {
            return buildImplicitBSplineCurve3((StepBezierCurve) curve);
        }
        if (curve instanceof StepUniformCurve) {
            return buildImplicitBSplineCurve3((StepUniformCurve) curve);
        }
        if (curve instanceof StepQuasiUniformCurve) {
            return buildImplicitBSplineCurve3((StepQuasiUniformCurve) curve);
        }
        if (curve instanceof StepPiecewiseBezierCurve) {
            return buildImplicitBSplineCurve3((StepPiecewiseBezierCurve) curve);
        }
        if (curve instanceof StepBSplineCurveWithKnots) {
            StepBSplineCurveWithKnots spline = (StepBSplineCurveWithKnots) curve;
            return buildBSplineCurve3(spline.id());
        }
        if (curve instanceof StepBSplineCurve) {
            StepBSplineCurve spline = (StepBSplineCurve) curve;
            return buildBSplineCurve3(spline.id());
        }
        if (curve instanceof StepRationalBSplineCurve) {
            StepRationalBSplineCurve spline = (StepRationalBSplineCurve) curve;
            return buildRationalBSplineCurve3(spline.id());
        }
        if (curve instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) curve;
            return buildSurfaceCurve3(surfaceCurve.id());
        }
        if (curve instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) curve;
            return buildSeamCurve(seamCurve.id());
        }
        if (curve instanceof StepTrimmedCurve) {
            StepTrimmedCurve trimmedCurve = (StepTrimmedCurve) curve;
            return buildTrimmedCurve3(trimmedCurve.id());
        }
        if (curve instanceof StepCompositeCurve) {
            StepCompositeCurve compositeCurve = (StepCompositeCurve) curve;
            return buildCompositeCurve3(compositeCurve.id());
        }
        if (curve instanceof StepCompositeCurveOnSurface) {
            StepCompositeCurveOnSurface compositeCurveOnSurface = (StepCompositeCurveOnSurface) curve;
            return buildCompositeCurve3(compositeCurveOnSurface.id());
        }
        if (curve instanceof StepConicCurve) {
            return buildConicCurve3((StepConicCurve) curve);
        }
        if (curve instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) curve;
            return buildOffsetCurve3(offsetCurve3D.id());
        }
        if (curve instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) curve;
            Curve3 baseCurve = buildCurve3Callback.apply(orientedCurve.curveElement().id());
            if (!orientedCurve.isOrientation() && baseCurve instanceof CompositeCurve3) {
                CompositeCurve3 composite = (CompositeCurve3) baseCurve;
                return reverseCompositeCurve(composite);
            }
            return baseCurve;
        }
        if (curve instanceof StepGeometricReplica) {
            StepGeometricReplica replica = (StepGeometricReplica) curve;
            Curve3 parent = buildCurve3Callback.apply(replica.parent().id());
            return geometryOps.transformCurve3(parent, replica.transformation());
        }
        if (curve instanceof StepClothoid) {
            StepClothoid clothoid = (StepClothoid) curve;
            return buildClothoid3(clothoid.id());
        }
        if (curve instanceof StepIndexedPolyCurve) {
            return buildIndexedPolyCurve3((StepIndexedPolyCurve) curve);
        }
        if (curve instanceof StepDegenerateCurve) {
            return buildDegenerateCurve3((StepDegenerateCurve) curve);
        }
        if (curve instanceof StepBoundedCurve) {
            StepBoundedCurve boundedCurve = (StepBoundedCurve) curve;
            StepEntity actual = entitiesById.get(boundedCurve.id());
            if (actual != null && actual != boundedCurve) {
                return buildCurve3Internal(actual);
            }
            throw new UnsupportedGeometryException("BOUNDED_CURVE requires an underlying curve type");
        }
        if (curve instanceof StepBSplineCurveWithKnotsAndBreakpoints) {
            StepBSplineCurveWithKnotsAndBreakpoints spline = (StepBSplineCurveWithKnotsAndBreakpoints) curve;
            return buildBSplineCurveWithBreakpoints(spline.id());
        }
        // For curve types not handled by this builder, delegate to the callback
        return buildCurve3Callback.apply(curve.id());
    }

    // ==================== Private 3D Curve Helper Methods ====================

    private BSplineCurve3 buildImplicitBSplineCurve3(StepEntity entity) {
        BSplineCurve3 existing = bsplineCurves3d.get(entity.id());
        if (existing != null) {
            return existing;
        }
        StepBSplineKnotGenerator.ImplicitBSplineCurveData spline = implicitBSplineCurveData(entity);
        List<CartesianPoint> controlPoints = spline.getControlPoints().stream()
                .map(point -> geometryBuilder.buildPoint(point.id()))
                .collect(Collectors.toList());
        BSplineCurve3 built = new BSplineCurve3(
                spline.getDegree(),
                controlPoints,
                spline.getKnotMultiplicities(),
                spline.getKnots()
        );
        bsplineCurves3d.put(entity.id(), built);
        return built;
    }

    private Curve3 buildConicCurve3(StepConicCurve conic) {
        if (!(conic.getPosition() instanceof StepAxis2Placement3D)) {
            throw new UnsupportedGeometryException("3D conic curve for " + conic.entityName() + " requires AXIS2_PLACEMENT_3D");
        }
        String entityName = conic.entityName();
        switch (entityName) {
            case "PARABOLA":
                return buildParabola3(conic.id());
            case "HYPERBOLA":
                return buildHyperbola3(conic.id());
            case "DEGENERATE_CONIC":
                StepAxis2Placement3D placement3D = (StepAxis2Placement3D) conic.getPosition();
                return new DegenerateCurve3(buildPlacementCallback.apply(placement3D.id()).getLocation());
            case "CONIC_CURVE":
                try {
                    return buildParabola3(conic.id());
                } catch (UnsupportedGeometryException e) {
                    return buildHyperbola3(conic.id());
                }
            default:
                throw new UnsupportedGeometryException("conic curve type " + entityName + " is unsupported");
        }
    }

    private Curve3 buildIndexedPolyCurve3(StepIndexedPolyCurve polyCurve) {
        List<StepCartesianPoint> stepPoints = polyCurve.getPoints();
        List<Integer> indices = polyCurve.indices();
        List<CartesianPoint> points = indices.stream()
                .map(index -> geometryBuilder.buildPoint(stepPoints.get(index).id()))
                .collect(Collectors.toList());
        if (polyCurve.isClosed() && !points.isEmpty()) {
            points = new ArrayList<>(points);
            points.add(points.get(0));
            points = List.copyOf(points);
        }
        return new Polyline3(points);
    }

    private Curve3 buildDegenerateCurve3(StepDegenerateCurve degenerateCurve) {
        Curve3 basis = buildCurve3Internal(degenerateCurve.getBasisCurve());
        List<CartesianPoint> sampledPoints = geometryOps.sampleCurve3(basis, 2);
        if (sampledPoints.isEmpty()) {
            throw new UnsupportedGeometryException("DEGENERATE_CURVE basis curve has no sample points");
        }
        return new DegenerateCurve3(sampledPoints.get(0));
    }

    private Curve3 buildReplicaCurve3(StepGeometricReplica replica) {
        Curve3 parent = buildCurve3Internal(replica.parent());
        return geometryOps.transformCurve3(parent, replica.transformation());
    }

    private List<SurfaceCurve3.ParametricCurve> buildSurfaceCurveBindings(List<StepEntity> associatedGeometry) {
        List<SurfaceCurve3.ParametricCurve> bindings = new ArrayList<>();
        for (StepEntity geometry : associatedGeometry) {
            StepEntity basisSurfaceEntity = null;
            if (geometry instanceof StepPcurve) {
                StepPcurve pcurve = (StepPcurve) geometry;
                basisSurfaceEntity = pcurve.getBasisSurface();
            } else if (geometry instanceof StepDegeneratePcurve) {
                StepDegeneratePcurve pcurve = (StepDegeneratePcurve) geometry;
                basisSurfaceEntity = pcurve.getBasisSurface();
            }
            if (basisSurfaceEntity == null) {
                continue;
            }
            Object builtCurve = buildPcurveCurve2Internal(geometry);
            if (!(builtCurve instanceof Curve2)) {
                continue;
            }
            Curve2 curve2 = (Curve2) builtCurve;
            com.minicad.geometry.SurfaceGeometry surface = buildSurfaceCallback.apply(basisSurfaceEntity.id());
            bindings.add(new SurfaceCurve3.ParametricCurve(surface, curve2));
        }
        return List.copyOf(bindings);
    }

    private Object buildPcurveCurve2Internal(StepEntity entity) {
        StepEntity item;
        if (entity instanceof StepPcurve) {
            StepPcurve pcurve = (StepPcurve) entity;
            item = pcurve.referenceToCurve().items().get(0);
        } else if (entity instanceof StepDegeneratePcurve) {
            StepDegeneratePcurve pcurve = (StepDegeneratePcurve) entity;
            item = pcurve.referenceToCurve().items().get(0);
        } else {
            throw new StepResolutionException(stepEntityTypeName(entity) + " is not a PCURVE or DEGENERATE_PCURVE");
        }
        return buildCurve2(item);
    }

    private Axis2Placement3D convert2DPlacementTo3D(StepEntity position) {
        if (position instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D placement3D = (StepAxis2Placement3D) position;
            return buildPlacementCallback.apply(placement3D.id());
        }
        if (!(position instanceof StepAxis2Placement2D)) {
            throw new StepResolutionException("position must be AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
        }
        StepAxis2Placement2D placement2D = (StepAxis2Placement2D) position;
        Point2 origin = buildPoint2(placement2D.getLocation().id());
        Direction2 refDir = buildDirection2(placement2D.getRefDirection().id());
        CartesianPoint location3D = new CartesianPoint(origin.getX(), origin.getY(), 0.0);
        Direction3 axis = new Direction3(0, 0, 1);
        Direction3 xDirection = new Direction3(refDir.getX(), refDir.getY(), 0);
        return new Axis2Placement3D(location3D, axis, xDirection);
    }

    private CompositeCurve3 reverseCompositeCurve(CompositeCurve3 original) {
        List<Curve3> reversedSegments = new ArrayList<>(original.getSegments());
        java.util.Collections.reverse(reversedSegments);
        for (int i = 0; i < reversedSegments.size(); i++) {
            reversedSegments.set(i, reverseCurve3(reversedSegments.get(i)));
        }
        return new CompositeCurve3(List.copyOf(reversedSegments));
    }

    private Curve3 reverseCurve3(Curve3 curve) {
        if (curve instanceof Line3) {
            Line3 line = (Line3) curve;
            return new Line3(line.getOrigin(), line.getDirection().reverse(), line.getParameterScale());
        }
        if (curve instanceof Polyline3) {
            Polyline3 polyline = (Polyline3) curve;
            List<CartesianPoint> reversedPoints = new ArrayList<>(polyline.getPoints());
            java.util.Collections.reverse(reversedPoints);
            return new Polyline3(reversedPoints);
        }
        if (curve instanceof CompositeCurve3) {
            return reverseCompositeCurve((CompositeCurve3) curve);
        }
        if (curve instanceof Circle) {
            Circle circle = (Circle) curve;
            Axis2Placement3D p = circle.getPosition();
            return new Circle(new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()), circle.getRadius());
        }
        if (curve instanceof Ellipse3) {
            Ellipse3 ellipse = (Ellipse3) curve;
            Axis2Placement3D p = ellipse.getPosition();
            return new Ellipse3(new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()), ellipse.getSemiAxis1(), ellipse.getSemiAxis2());
        }
        if (curve instanceof Parabola3) {
            Parabola3 parabola = (Parabola3) curve;
            Axis2Placement3D p = parabola.getPosition();
            return new Parabola3(new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()), parabola.getFocalLength());
        }
        if (curve instanceof Hyperbola3) {
            Hyperbola3 hyperbola = (Hyperbola3) curve;
            Axis2Placement3D p = hyperbola.getPosition();
            return new Hyperbola3(new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()), hyperbola.getSemiAxisA(), hyperbola.getSemiAxisB());
        }
        if (curve instanceof Clothoid3) {
            Clothoid3 clothoid = (Clothoid3) curve;
            Axis2Placement3D p = clothoid.getPosition();
            return new Clothoid3(new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()), clothoid.xAxisIntercept(), clothoid.curvature());
        }
        if (curve instanceof DegenerateCurve3) {
            DegenerateCurve3 degenerate = (DegenerateCurve3) curve;
            return new DegenerateCurve3(degenerate.point());
        }
        if (curve instanceof TrimmedCurve3) {
            TrimmedCurve3 trimmed = (TrimmedCurve3) curve;
            return new TrimmedCurve3(reverseCurve3(trimmed.getBasisCurve()), trimmed.getTrimParamEnd(), trimmed.getTrimParamStart(), !trimmed.isSenseAgreement());
        }
        if (curve instanceof SurfaceCurve3) {
            SurfaceCurve3 surfaceCurve = (SurfaceCurve3) curve;
            return new SurfaceCurve3(reverseCurve3(surfaceCurve.getCurve3d()), surfaceCurve.getParametricCurves());
        }
        if (curve instanceof BSplineCurve3) {
            BSplineCurve3 bspline = (BSplineCurve3) curve;
            return new BSplineCurve3(bspline.getDegree(), reverseList(bspline.getControlPoints()), bspline.getKnotMultiplicities(), bspline.getKnots());
        }
        if (curve instanceof RationalBSplineCurve3) {
            RationalBSplineCurve3 rational = (RationalBSplineCurve3) curve;
            return new RationalBSplineCurve3(rational.getDegree(), reverseList(rational.getControlPoints()), rational.getWeights(), rational.getKnotMultiplicities(), rational.getKnots());
        }
        return curve;
    }

    private static <T> List<T> reverseList(List<T> list) {
        List<T> reversed = new ArrayList<>(list);
        java.util.Collections.reverse(reversed);
        return List.copyOf(reversed);
    }

    private Curve3 approximateOffsetCurve3(Curve3 basisCurve, double distance, Direction3 refDirection) {
        return geometryOps.approximateOffsetCurve3(basisCurve, distance, refDirection);
    }
}
