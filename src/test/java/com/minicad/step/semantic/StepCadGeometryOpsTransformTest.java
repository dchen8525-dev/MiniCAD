package com.minicad.step.semantic;

import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.BSplineSurface3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.Clothoid3;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.DegenerateCurve3;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Hyperbola3;
import com.minicad.geometry.HyperboloidSurface;
import com.minicad.geometry.Line3;
import com.minicad.geometry.OffsetSurface3;
import com.minicad.geometry.Parabola3;
import com.minicad.geometry.ParaboloidSurface;
import com.minicad.geometry.Plane;
import com.minicad.geometry.Polyline3;
import com.minicad.geometry.RationalBSplineCurve3;
import com.minicad.geometry.RationalBSplineSurface3;
import com.minicad.geometry.RuledSurface3;
import com.minicad.geometry.SphericalSurface;
import com.minicad.geometry.SurfaceCurve3;
import com.minicad.geometry.SurfaceGeometry;
import com.minicad.geometry.SurfaceOfConstantRadius3;
import com.minicad.geometry.SurfaceOfLinearExtrusion3;
import com.minicad.geometry.SurfaceOfProjection3;
import com.minicad.geometry.SurfaceOfRevolution3;
import com.minicad.geometry.SurfaceOfTranslation3;
import com.minicad.geometry.ToroidalSurface;
import com.minicad.geometry.TrimmedCurve3;
import com.minicad.geometry.Vector3;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Direction2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.CompositeCurve2;
import com.minicad.geometry2d.DegenerateCurve2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Hyperbola2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Parabola2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.Polyline2;
import com.minicad.geometry2d.RationalBSplineCurve2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.geometry2d.Vector2;
import com.minicad.step.model.StepCartesianTransformationOperator;
import com.minicad.step.model.StepEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Runtime behaviour tests for the Cartesian-transformation dispatch tables in
 * StepCadGeometryOps: transformCurve3 (13 branches), transformCurve2 (10) and
 * transformSurfaceGeometry (16), one test per branch, plus the terminal-throw
 * paths for types no rule covers.
 *
 * The fixtures are real STEP entities: two CARTESIAN_TRANSFORMATION_OPERATOR_3D
 * instances parsed from STEP text and resolved into a real StepCadBuilder,
 * because transformPoint3/transformPoint2 resolve the operator's local origin
 * and axes through the builder.
 *
 * Operator T3 (#5): origin (10,0,0), axes X/Y/Z, scale 2.0 -- a point (x,y,z)
 * maps to (10+2x, 2y, 2z); a direction maps without scale; a vector maps with
 * scale. Operator T2 (#8): 2D origin (5,5), axis1 (1,0), scale 2.0 -- a point
 * (x,y) maps to (5+2x, 5+2y); directions map without scale.
 *
 * Every handler must return a transformed COPY of the input (the originals
 * were `return new X(...)` branches), so each test also checks that the input
 * geometry is left untouched where the type allows an easy check.
 */
class StepCadGeometryOpsTransformTest {

    private static final String STEP =
            "DATA;\n"
            + "#1=CARTESIAN_POINT('O',(10.0,0.0,0.0));\n"
            + "#2=DIRECTION('AX',(1.0,0.0,0.0));\n"
            + "#3=DIRECTION('AY',(0.0,1.0,0.0));\n"
            + "#4=DIRECTION('AZ',(0.0,0.0,1.0));\n"
            + "#5=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T3',#2,#3,#1,2.0,#4);\n"
            + "#6=DIRECTION('AX2',(1.0,0.0));\n"
            + "#7=CARTESIAN_POINT('O2',(5.0,5.0));\n"
            + "#8=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T2',#6,$,#7,2.0,$);\n"
            + "ENDSEC;\n";

    private static StepCadGeometryOps ops;
    private static StepCartesianTransformationOperator t3;
    private static StepCartesianTransformationOperator t2;

    @BeforeAll
    static void setUp() {
        Map<Integer, StepEntity> resolved =
                StepEntityResolver.resolveAll(com.minicad.step.syntax.StepParser.parse(STEP));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);
        ops = new StepCadGeometryOps(builder);
        t3 = (StepCartesianTransformationOperator) resolved.get(5);
        t2 = (StepCartesianTransformationOperator) resolved.get(8);
    }

    private static Axis2Placement3D placement3(CartesianPoint location) {
        return new Axis2Placement3D(location, new Direction3(0.0, 0.0, 1.0), new Direction3(1.0, 0.0, 0.0));
    }

    private static void assertPoint(CartesianPoint point, double x, double y, double z) {
        assertEquals(x, point.getX(), 1.0e-9);
        assertEquals(y, point.getY(), 1.0e-9);
        assertEquals(z, point.getZ(), 1.0e-9);
    }

    private static void assertPoint2(Point2 point, double x, double y) {
        assertEquals(x, point.getX(), 1.0e-9);
        assertEquals(y, point.getY(), 1.0e-9);
    }

    // ─── transformCurve3 (13 branches) ───────────────────────────────────

    @Test
    @DisplayName("transformCurve3: Line3 moves origin, keeps direction, scales parameterization")
    void transformLine3() {
        Line3 line = new Line3(new CartesianPoint(0.0, 0.0, 0.0), new Direction3(1.0, 0.0, 0.0), 3.0);
        Line3 transformed = assertInstanceOf(Line3.class, ops.transformCurve3(line, t3));
        assertPoint(transformed.getOrigin(), 10.0, 0.0, 0.0);
        assertEquals(1.0, transformed.getDirection().x(), 1.0e-9);
        assertEquals(6.0, transformed.getParameterScale(), 1.0e-9);
        assertPoint(line.getOrigin(), 0.0, 0.0, 0.0);
    }

    @Test
    @DisplayName("transformCurve3: Circle scales radius and transforms placement")
    void transformCircle() {
        Circle circle = new Circle(placement3(new CartesianPoint(1.0, 2.0, 3.0)), 3.0);
        Circle transformed = assertInstanceOf(Circle.class, ops.transformCurve3(circle, t3));
        assertEquals(6.0, transformed.getRadius(), 1.0e-9);
        assertPoint(transformed.getPosition().getLocation(), 12.0, 4.0, 6.0);
    }

    @Test
    @DisplayName("transformCurve3: Ellipse3 scales both semi axes")
    void transformEllipse3() {
        Ellipse3 ellipse = new Ellipse3(placement3(new CartesianPoint(0.0, 0.0, 0.0)), 3.0, 2.0);
        Ellipse3 transformed = assertInstanceOf(Ellipse3.class, ops.transformCurve3(ellipse, t3));
        assertEquals(6.0, transformed.getSemiAxis1(), 1.0e-9);
        assertEquals(4.0, transformed.getSemiAxis2(), 1.0e-9);
    }

    @Test
    @DisplayName("transformCurve3: Polyline3 transforms every point")
    void transformPolyline3() {
        Polyline3 polyline = new Polyline3(List.of(
                new CartesianPoint(0.0, 0.0, 0.0), new CartesianPoint(1.0, 1.0, 1.0)));
        Polyline3 transformed = assertInstanceOf(Polyline3.class, ops.transformCurve3(polyline, t3));
        assertEquals(2, transformed.getPoints().size());
        assertPoint(transformed.getPoints().get(0), 10.0, 0.0, 0.0);
        assertPoint(transformed.getPoints().get(1), 12.0, 2.0, 2.0);
    }

    @Test
    @DisplayName("transformCurve3: BSplineCurve3 transforms control points, keeps knots")
    void transformBSplineCurve3() {
        BSplineCurve3 spline = new BSplineCurve3(1,
                List.of(new CartesianPoint(0.0, 0.0, 0.0), new CartesianPoint(1.0, 0.0, 0.0)),
                List.of(2, 2), List.of(0.0, 1.0));
        BSplineCurve3 transformed = assertInstanceOf(BSplineCurve3.class, ops.transformCurve3(spline, t3));
        assertPoint(transformed.getControlPoints().get(0), 10.0, 0.0, 0.0);
        assertPoint(transformed.getControlPoints().get(1), 12.0, 0.0, 0.0);
        assertEquals(List.of(2, 2), transformed.getKnotMultiplicities());
        assertEquals(List.of(0.0, 1.0), transformed.getKnots());
    }

    @Test
    @DisplayName("transformCurve3: RationalBSplineCurve3 transforms control points, keeps weights")
    void transformRationalBSplineCurve3() {
        RationalBSplineCurve3 spline = new RationalBSplineCurve3(1,
                List.of(new CartesianPoint(0.0, 0.0, 0.0), new CartesianPoint(1.0, 0.0, 0.0)),
                List.of(1.0, 2.0), List.of(2, 2), List.of(0.0, 1.0));
        RationalBSplineCurve3 transformed =
                assertInstanceOf(RationalBSplineCurve3.class, ops.transformCurve3(spline, t3));
        assertPoint(transformed.getControlPoints().get(1), 12.0, 0.0, 0.0);
        assertEquals(List.of(1.0, 2.0), transformed.getWeights());
    }

    @Test
    @DisplayName("transformCurve3: SurfaceCurve3 transforms basis curve and binding surfaces")
    void transformSurfaceCurve3() {
        SurfaceCurve3 surfaceCurve = new SurfaceCurve3(
                new Line3(new CartesianPoint(0.0, 0.0, 0.0), new Direction3(1.0, 0.0, 0.0), 1.0),
                List.of(new SurfaceCurve3.ParametricCurve(
                        new Plane(new CartesianPoint(0.0, 0.0, 0.0), new Direction3(0.0, 0.0, 1.0)),
                        new Line2(new Point2(0.0, 0.0), new Direction2(1.0, 0.0), 1.0))));
        SurfaceCurve3 transformed = assertInstanceOf(SurfaceCurve3.class, ops.transformCurve3(surfaceCurve, t3));
        Line3 basis = assertInstanceOf(Line3.class, transformed.getCurve3d());
        assertPoint(basis.getOrigin(), 10.0, 0.0, 0.0);
        Plane bindingSurface = assertInstanceOf(Plane.class, transformed.getParametricCurves().get(0).getSurface());
        assertPoint(bindingSurface.getOrigin(), 10.0, 0.0, 0.0);
    }

    @Test
    @DisplayName("transformCurve3: TrimmedCurve3 transforms basis, keeps trim parameters")
    void transformTrimmedCurve3() {
        TrimmedCurve3 trimmed = new TrimmedCurve3(
                new Line3(new CartesianPoint(0.0, 0.0, 0.0), new Direction3(1.0, 0.0, 0.0), 1.0),
                0.5, 2.5, false);
        TrimmedCurve3 transformed = assertInstanceOf(TrimmedCurve3.class, ops.transformCurve3(trimmed, t3));
        Line3 basis = assertInstanceOf(Line3.class, transformed.getBasisCurve());
        assertPoint(basis.getOrigin(), 10.0, 0.0, 0.0);
        assertEquals(0.5, transformed.getTrimParamStart(), 1.0e-9);
        assertEquals(2.5, transformed.getTrimParamEnd(), 1.0e-9);
        assertEquals(false, transformed.isSenseAgreement());
    }

    @Test
    @DisplayName("transformCurve3: CompositeCurve3 transforms every segment")
    void transformCompositeCurve3() {
        CompositeCurve3 composite = new CompositeCurve3(List.of(
                new Line3(new CartesianPoint(0.0, 0.0, 0.0), new Direction3(1.0, 0.0, 0.0), 1.0),
                new Line3(new CartesianPoint(1.0, 0.0, 0.0), new Direction3(0.0, 1.0, 0.0), 1.0)));
        CompositeCurve3 transformed = assertInstanceOf(CompositeCurve3.class, ops.transformCurve3(composite, t3));
        assertEquals(2, transformed.getSegments().size());
        assertPoint(assertInstanceOf(Line3.class, transformed.getSegments().get(0)).getOrigin(), 10.0, 0.0, 0.0);
        assertPoint(assertInstanceOf(Line3.class, transformed.getSegments().get(1)).getOrigin(), 12.0, 0.0, 0.0);
    }

    @Test
    @DisplayName("transformCurve3: Parabola3 scales focal distance")
    void transformParabola3() {
        Parabola3 parabola = new Parabola3(placement3(new CartesianPoint(0.0, 0.0, 0.0)), 3.0);
        Parabola3 transformed = assertInstanceOf(Parabola3.class, ops.transformCurve3(parabola, t3));
        assertEquals(6.0, transformed.focalDistance(), 1.0e-9);
    }

    @Test
    @DisplayName("transformCurve3: Hyperbola3 scales both semi axes")
    void transformHyperbola3() {
        Hyperbola3 hyperbola = new Hyperbola3(placement3(new CartesianPoint(0.0, 0.0, 0.0)), 3.0, 2.0);
        Hyperbola3 transformed = assertInstanceOf(Hyperbola3.class, ops.transformCurve3(hyperbola, t3));
        assertEquals(6.0, transformed.getSemiAxisA(), 1.0e-9);
        assertEquals(4.0, transformed.getSemiAxisB(), 1.0e-9);
    }

    @Test
    @DisplayName("transformCurve3: Clothoid3 scales intercept and curvature")
    void transformClothoid3() {
        Clothoid3 clothoid = new Clothoid3(placement3(new CartesianPoint(0.0, 0.0, 0.0)), 3.0, 0.5);
        Clothoid3 transformed = assertInstanceOf(Clothoid3.class, ops.transformCurve3(clothoid, t3));
        assertEquals(6.0, transformed.xAxisIntercept(), 1.0e-9);
        assertEquals(1.0, transformed.curvature(), 1.0e-9);
    }

    @Test
    @DisplayName("transformCurve3: DegenerateCurve3 moves its collapsed point")
    void transformDegenerateCurve3() {
        DegenerateCurve3 degenerate = new DegenerateCurve3(new CartesianPoint(1.0, 2.0, 3.0));
        DegenerateCurve3 transformed = assertInstanceOf(DegenerateCurve3.class, ops.transformCurve3(degenerate, t3));
        assertPoint(transformed.point(), 12.0, 4.0, 6.0);
    }

    // ─── transformCurve2 (10 branches) ───────────────────────────────────

    @Test
    @DisplayName("transformCurve2: Line2 moves origin, keeps direction, scales parameterization")
    void transformLine2() {
        Line2 line = new Line2(new Point2(0.0, 0.0), new Direction2(1.0, 0.0), 3.0);
        Line2 transformed = assertInstanceOf(Line2.class, ops.transformCurve2(line, t2));
        assertPoint2(transformed.getOrigin(), 5.0, 5.0);
        assertEquals(1.0, transformed.getDirection().x(), 1.0e-9);
        assertEquals(6.0, transformed.getParameterScale(), 1.0e-9);
    }

    @Test
    @DisplayName("transformCurve2: Circle2 scales radius and moves center")
    void transformCircle2() {
        Circle2 circle = new Circle2(new Point2(1.0, 2.0),
                new Direction2(1.0, 0.0), 3.0);
        Circle2 transformed = assertInstanceOf(Circle2.class, ops.transformCurve2(circle, t2));
        assertEquals(6.0, transformed.getRadius(), 1.0e-9);
        assertPoint2(transformed.center(), 7.0, 9.0);
    }

    @Test
    @DisplayName("transformCurve2: Ellipse2 scales both semi axes")
    void transformEllipse2() {
        Ellipse2 ellipse = new Ellipse2(new Point2(0.0, 0.0),
                new Direction2(1.0, 0.0), 3.0, 2.0);
        Ellipse2 transformed = assertInstanceOf(Ellipse2.class, ops.transformCurve2(ellipse, t2));
        assertEquals(6.0, transformed.getSemiAxis1(), 1.0e-9);
        assertEquals(4.0, transformed.getSemiAxis2(), 1.0e-9);
    }

    @Test
    @DisplayName("transformCurve2: Polyline2 transforms every point")
    void transformPolyline2() {
        Polyline2 polyline = new Polyline2(List.of(new Point2(0.0, 0.0), new Point2(1.0, 1.0)));
        Polyline2 transformed = assertInstanceOf(Polyline2.class, ops.transformCurve2(polyline, t2));
        assertPoint2(transformed.getPoints().get(0), 5.0, 5.0);
        assertPoint2(transformed.getPoints().get(1), 7.0, 7.0);
    }

    @Test
    @DisplayName("transformCurve2: BSplineCurve2 transforms control points, keeps knots")
    void transformBSplineCurve2() {
        BSplineCurve2 spline = new BSplineCurve2(1,
                List.of(new Point2(0.0, 0.0), new Point2(1.0, 0.0)),
                List.of(2, 2), List.of(0.0, 1.0));
        BSplineCurve2 transformed = assertInstanceOf(BSplineCurve2.class, ops.transformCurve2(spline, t2));
        assertPoint2(transformed.getControlPoints().get(0), 5.0, 5.0);
        assertPoint2(transformed.getControlPoints().get(1), 7.0, 5.0);
        assertEquals(List.of(0.0, 1.0), transformed.getKnots());
    }

    @Test
    @DisplayName("transformCurve2: RationalBSplineCurve2 transforms control points, keeps weights")
    void transformRationalBSplineCurve2() {
        RationalBSplineCurve2 spline = new RationalBSplineCurve2(1,
                List.of(new Point2(0.0, 0.0), new Point2(1.0, 0.0)),
                List.of(1.0, 2.0), List.of(2, 2), List.of(0.0, 1.0));
        RationalBSplineCurve2 transformed =
                assertInstanceOf(RationalBSplineCurve2.class, ops.transformCurve2(spline, t2));
        assertPoint2(transformed.getControlPoints().get(1), 7.0, 5.0);
        assertEquals(List.of(1.0, 2.0), transformed.getWeights());
    }

    @Test
    @DisplayName("transformCurve2: TrimmedCurve2 transforms basis, keeps trim parameters")
    void transformTrimmedCurve2() {
        TrimmedCurve2 trimmed = new TrimmedCurve2(
                new Line2(new Point2(0.0, 0.0), new Direction2(1.0, 0.0), 1.0),
                0.0, 1.0, true);
        TrimmedCurve2 transformed = assertInstanceOf(TrimmedCurve2.class, ops.transformCurve2(trimmed, t2));
        Line2 basis = assertInstanceOf(Line2.class, transformed.getBasisCurve());
        assertPoint2(basis.getOrigin(), 5.0, 5.0);
        assertPoint2(transformed.trimStart(), 5.0, 5.0);
        assertPoint2(transformed.trimEnd(), 7.0, 5.0);
    }

    @Test
    @DisplayName("transformCurve2: CompositeCurve2 transforms every segment")
    void transformCompositeCurve2() {
        CompositeCurve2 composite = new CompositeCurve2(List.of(
                new Line2(new Point2(0.0, 0.0), new Direction2(1.0, 0.0), 1.0),
                new Line2(new Point2(1.0, 0.0), new Direction2(0.0, 1.0), 1.0)));
        CompositeCurve2 transformed = assertInstanceOf(CompositeCurve2.class, ops.transformCurve2(composite, t2));
        assertEquals(2, transformed.getSegments().size());
        assertPoint2(assertInstanceOf(Line2.class, transformed.getSegments().get(0)).getOrigin(), 5.0, 5.0);
        assertPoint2(assertInstanceOf(Line2.class, transformed.getSegments().get(1)).getOrigin(), 7.0, 5.0);
    }

    @Test
    @DisplayName("transformCurve2: Parabola2 moves vertex and scales focal distance")
    void transformParabola2() {
        Parabola2 parabola = new Parabola2(new Point2(1.0, 2.0),
                new Direction2(1.0, 0.0), 3.0);
        Parabola2 transformed = assertInstanceOf(Parabola2.class, ops.transformCurve2(parabola, t2));
        assertPoint2(transformed.getVertex(), 7.0, 9.0);
        assertEquals(6.0, transformed.focalDistance(), 1.0e-9);
    }

    @Test
    @DisplayName("transformCurve2: Hyperbola2 scales both semi axes")
    void transformHyperbola2() {
        Hyperbola2 hyperbola = new Hyperbola2(new Point2(0.0, 0.0),
                new Direction2(1.0, 0.0), 3.0, 2.0);
        Hyperbola2 transformed = assertInstanceOf(Hyperbola2.class, ops.transformCurve2(hyperbola, t2));
        assertEquals(6.0, transformed.getSemiAxisA(), 1.0e-9);
        assertEquals(4.0, transformed.getSemiAxisB(), 1.0e-9);
    }

    @Test
    @DisplayName("transformCurve2: DegenerateCurve2 has no rule and throws the terminal error")
    void transformDegenerateCurve2Throws() {
        UnsupportedGeometryException exception = assertThrows(UnsupportedGeometryException.class,
                () -> ops.transformCurve2(new DegenerateCurve2(new Point2(1.0, 1.0)), t2));
        assertTrue(exception.getMessage().contains("curve replica"),
                "unexpected message: " + exception.getMessage());
    }

    // ─── transformSurfaceGeometry (16 branches) ──────────────────────────

    @Test
    @DisplayName("transformSurfaceGeometry: Plane moves origin, keeps normal direction")
    void transformPlane() {
        Plane plane = new Plane(new CartesianPoint(1.0, 2.0, 3.0), new Direction3(0.0, 0.0, 1.0));
        Plane transformed = assertInstanceOf(Plane.class, ops.transformSurfaceGeometry(plane, t3));
        assertPoint(transformed.getOrigin(), 12.0, 4.0, 6.0);
        assertEquals(1.0, transformed.getNormal().z(), 1.0e-9);
    }

    @Test
    @DisplayName("transformSurfaceGeometry: OffsetSurface3 scales distance, transforms basis")
    void transformOffsetSurface3() {
        OffsetSurface3 offset = new OffsetSurface3(
                new Plane(new CartesianPoint(0.0, 0.0, 0.0), new Direction3(0.0, 0.0, 1.0)), 3.0);
        OffsetSurface3 transformed = assertInstanceOf(OffsetSurface3.class, ops.transformSurfaceGeometry(offset, t3));
        assertEquals(6.0, transformed.getDistance(), 1.0e-9);
        assertPoint(assertInstanceOf(Plane.class, transformed.getBasisSurface()).getOrigin(), 10.0, 0.0, 0.0);
    }

    @Test
    @DisplayName("transformSurfaceGeometry: CylindricalSurface scales radius")
    void transformCylindricalSurface() {
        CylindricalSurface surface = new CylindricalSurface(placement3(new CartesianPoint(0.0, 0.0, 0.0)), 3.0);
        CylindricalSurface transformed =
                assertInstanceOf(CylindricalSurface.class, ops.transformSurfaceGeometry(surface, t3));
        assertEquals(6.0, transformed.getRadius(), 1.0e-9);
    }

    @Test
    @DisplayName("transformSurfaceGeometry: ConicalSurface scales radius, keeps semi angle")
    void transformConicalSurface() {
        ConicalSurface surface = new ConicalSurface(placement3(new CartesianPoint(0.0, 0.0, 0.0)), 3.0, 0.5);
        ConicalSurface transformed = assertInstanceOf(ConicalSurface.class, ops.transformSurfaceGeometry(surface, t3));
        assertEquals(6.0, transformed.getRadius(), 1.0e-9);
        assertEquals(0.5, transformed.getSemiAngle(), 1.0e-9);
    }

    @Test
    @DisplayName("transformSurfaceGeometry: ToroidalSurface scales both radii")
    void transformToroidalSurface() {
        ToroidalSurface surface = new ToroidalSurface(placement3(new CartesianPoint(0.0, 0.0, 0.0)), 5.0, 1.0);
        ToroidalSurface transformed = assertInstanceOf(ToroidalSurface.class, ops.transformSurfaceGeometry(surface, t3));
        assertEquals(10.0, transformed.getMajorRadius(), 1.0e-9);
        assertEquals(2.0, transformed.getMinorRadius(), 1.0e-9);
    }

    @Test
    @DisplayName("transformSurfaceGeometry: SphericalSurface scales radius")
    void transformSphericalSurface() {
        SphericalSurface surface = new SphericalSurface(placement3(new CartesianPoint(0.0, 0.0, 0.0)), 3.0);
        SphericalSurface transformed = assertInstanceOf(SphericalSurface.class, ops.transformSurfaceGeometry(surface, t3));
        assertEquals(6.0, transformed.getRadius(), 1.0e-9);
    }

    @Test
    @DisplayName("transformSurfaceGeometry: BSplineSurface3 transforms the control grid")
    void transformBSplineSurface3() {
        BSplineSurface3 surface = new BSplineSurface3(1, 1,
                List.of(
                        List.of(new CartesianPoint(0.0, 0.0, 0.0), new CartesianPoint(1.0, 0.0, 0.0)),
                        List.of(new CartesianPoint(0.0, 1.0, 0.0), new CartesianPoint(1.0, 1.0, 0.0))),
                List.of(2, 2), List.of(2, 2), List.of(0.0, 1.0), List.of(0.0, 1.0));
        BSplineSurface3 transformed = assertInstanceOf(BSplineSurface3.class, ops.transformSurfaceGeometry(surface, t3));
        List<List<CartesianPoint>> grid = transformed.getControlPoints();
        assertPoint(grid.get(0).get(0), 10.0, 0.0, 0.0);
        assertPoint(grid.get(0).get(1), 12.0, 0.0, 0.0);
        assertPoint(grid.get(1).get(0), 10.0, 2.0, 0.0);
        assertPoint(grid.get(1).get(1), 12.0, 2.0, 0.0);
        assertEquals(List.of(0.0, 1.0), transformed.getUKnots());
    }

    @Test
    @DisplayName("transformSurfaceGeometry: RationalBSplineSurface3 transforms grid, keeps weights")
    void transformRationalBSplineSurface3() {
        RationalBSplineSurface3 surface = new RationalBSplineSurface3(1, 1,
                List.of(
                        List.of(new CartesianPoint(0.0, 0.0, 0.0), new CartesianPoint(1.0, 0.0, 0.0)),
                        List.of(new CartesianPoint(0.0, 1.0, 0.0), new CartesianPoint(1.0, 1.0, 0.0))),
                List.of(List.of(1.0, 1.0), List.of(1.0, 1.0)),
                List.of(2, 2), List.of(2, 2), List.of(0.0, 1.0), List.of(0.0, 1.0));
        RationalBSplineSurface3 transformed =
                assertInstanceOf(RationalBSplineSurface3.class, ops.transformSurfaceGeometry(surface, t3));
        assertPoint(transformed.getControlPoints().get(1).get(1), 12.0, 2.0, 0.0);
        assertEquals(List.of(List.of(1.0, 1.0), List.of(1.0, 1.0)), transformed.getWeightsData());
    }

    @Test
    @DisplayName("transformSurfaceGeometry: SurfaceOfLinearExtrusion3 transforms curve and scales vector")
    void transformSurfaceOfLinearExtrusion3() {
        SurfaceOfLinearExtrusion3 surface = new SurfaceOfLinearExtrusion3(
                new Line3(new CartesianPoint(0.0, 0.0, 0.0), new Direction3(1.0, 0.0, 0.0), 1.0),
                new Vector3(0.0, 0.0, 1.0));
        SurfaceOfLinearExtrusion3 transformed =
                assertInstanceOf(SurfaceOfLinearExtrusion3.class, ops.transformSurfaceGeometry(surface, t3));
        assertPoint(assertInstanceOf(Line3.class, transformed.getSweptCurve()).getOrigin(), 10.0, 0.0, 0.0);
        assertEquals(2.0, transformed.getExtrusionVector().getZ(), 1.0e-9);
    }

    @Test
    @DisplayName("transformSurfaceGeometry: SurfaceOfRevolution3 transforms axis origin, keeps axis direction")
    void transformSurfaceOfRevolution3() {
        SurfaceOfRevolution3 surface = new SurfaceOfRevolution3(
                new Line3(new CartesianPoint(0.0, 0.0, 0.0), new Direction3(1.0, 0.0, 0.0), 1.0),
                new CartesianPoint(1.0, 2.0, 3.0), new Direction3(0.0, 0.0, 1.0));
        SurfaceOfRevolution3 transformed =
                assertInstanceOf(SurfaceOfRevolution3.class, ops.transformSurfaceGeometry(surface, t3));
        assertPoint(transformed.getAxisOrigin(), 12.0, 4.0, 6.0);
        assertEquals(1.0, transformed.getAxisDirection().z(), 1.0e-9);
    }

    @Test
    @DisplayName("transformSurfaceGeometry: RuledSurface3 transforms both directrices")
    void transformRuledSurface3() {
        RuledSurface3 surface = new RuledSurface3(
                new Line3(new CartesianPoint(0.0, 0.0, 0.0), new Direction3(1.0, 0.0, 0.0), 1.0),
                new Line3(new CartesianPoint(0.0, 0.0, 1.0), new Direction3(1.0, 0.0, 0.0), 1.0));
        RuledSurface3 transformed = assertInstanceOf(RuledSurface3.class, ops.transformSurfaceGeometry(surface, t3));
        assertPoint(assertInstanceOf(Line3.class, transformed.getDirectrix1()).getOrigin(), 10.0, 0.0, 0.0);
        assertPoint(assertInstanceOf(Line3.class, transformed.getDirectrix2()).getOrigin(), 10.0, 0.0, 2.0);
    }

    @Test
    @DisplayName("transformSurfaceGeometry: SurfaceOfConstantRadius3 scales radius, transforms swept surface")
    void transformSurfaceOfConstantRadius3() {
        SurfaceOfConstantRadius3 surface = new SurfaceOfConstantRadius3(
                new Plane(new CartesianPoint(0.0, 0.0, 0.0), new Direction3(0.0, 0.0, 1.0)), 3.0);
        SurfaceOfConstantRadius3 transformed =
                assertInstanceOf(SurfaceOfConstantRadius3.class, ops.transformSurfaceGeometry(surface, t3));
        assertEquals(6.0, transformed.getRadius(), 1.0e-9);
        assertPoint(assertInstanceOf(Plane.class, transformed.getSweptSurface()).getOrigin(), 10.0, 0.0, 0.0);
    }

    @Test
    @DisplayName("transformSurfaceGeometry: ParaboloidSurface scales focal length")
    void transformParaboloidSurface() {
        ParaboloidSurface surface = new ParaboloidSurface(placement3(new CartesianPoint(0.0, 0.0, 0.0)), 3.0);
        ParaboloidSurface transformed = assertInstanceOf(ParaboloidSurface.class, ops.transformSurfaceGeometry(surface, t3));
        assertEquals(6.0, transformed.getFocalLength(), 1.0e-9);
    }

    @Test
    @DisplayName("transformSurfaceGeometry: HyperboloidSurface scales radius and semi axis")
    void transformHyperboloidSurface() {
        HyperboloidSurface surface = new HyperboloidSurface(placement3(new CartesianPoint(0.0, 0.0, 0.0)), 3.0, 2.0);
        HyperboloidSurface transformed = assertInstanceOf(HyperboloidSurface.class, ops.transformSurfaceGeometry(surface, t3));
        assertEquals(6.0, transformed.getRadius(), 1.0e-9);
        assertEquals(4.0, transformed.getSemiAxis(), 1.0e-9);
    }

    @Test
    @DisplayName("transformSurfaceGeometry: SurfaceOfTranslation3 transforms profile and scales direction")
    void transformSurfaceOfTranslation3() {
        SurfaceOfTranslation3 surface = new SurfaceOfTranslation3(
                new Line3(new CartesianPoint(0.0, 0.0, 0.0), new Direction3(1.0, 0.0, 0.0), 1.0),
                new Vector3(0.0, 0.0, 1.0));
        SurfaceOfTranslation3 transformed =
                assertInstanceOf(SurfaceOfTranslation3.class, ops.transformSurfaceGeometry(surface, t3));
        assertPoint(assertInstanceOf(Line3.class, transformed.getProfile()).getOrigin(), 10.0, 0.0, 0.0);
        assertEquals(2.0, transformed.getDirection().getZ(), 1.0e-9);
    }

    @Test
    @DisplayName("transformSurfaceGeometry: SurfaceOfProjection3 transforms profile and scales direction")
    void transformSurfaceOfProjection3() {
        SurfaceOfProjection3 surface = new SurfaceOfProjection3(
                new Line3(new CartesianPoint(0.0, 0.0, 0.0), new Direction3(1.0, 0.0, 0.0), 1.0),
                new Vector3(0.0, 1.0, 0.0));
        SurfaceOfProjection3 transformed =
                assertInstanceOf(SurfaceOfProjection3.class, ops.transformSurfaceGeometry(surface, t3));
        assertPoint(assertInstanceOf(Line3.class, transformed.getProfile()).getOrigin(), 10.0, 0.0, 0.0);
        assertEquals(2.0, transformed.getProjectionDirection().getY(), 1.0e-9);
    }

    @Test
    @DisplayName("transformSurfaceGeometry: unknown SurfaceGeometry has no rule and throws the terminal error")
    void transformUnknownSurfaceThrows() {
        SurfaceGeometry unknown = new SurfaceGeometry() {
            @Override
            public CartesianPoint pointAt(double u, double v) {
                return new CartesianPoint(u, v, 0.0);
            }
        };
        UnsupportedGeometryException exception = assertThrows(UnsupportedGeometryException.class,
                () -> ops.transformSurfaceGeometry(unknown, t3));
        assertTrue(exception.getMessage().contains("surface replica"),
                "unexpected message: " + exception.getMessage());
    }
}
