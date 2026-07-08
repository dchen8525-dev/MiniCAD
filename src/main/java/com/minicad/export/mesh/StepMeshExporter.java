package com.minicad.export.mesh;
import com.minicad.geometry.*;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.builder.CompiledStepDocument;
import com.minicad.topology.*;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Exports STEP files to OBJ and STL mesh formats.
 * Uses the existing STEP parser/resolver/builder pipeline, then triangulates
 * all faces and formats the result as OBJ or STL.
 */
public final class StepMeshExporter {

    private static final Logger LOG = Logger.getLogger(StepMeshExporter.class.getName());

    // STL binary layout constants
    private static final int STL_HEADER_SIZE = 80;
    private static final int STL_TRIANGLE_RECORD_SIZE = 50;
    private static final int STL_COUNT_SIZE = 4;

    // Tessellation defaults
    private static final int DEFAULT_CURVE_SEGMENTS = 32;
    private static final int MIN_CURVE_SEGMENTS = 8;
    private static final int MAX_CURVE_SEGMENTS = 64;
    private static final double BBOX_SEGMENT_MULTIPLIER = 10;

    // Vertex dedup precision
    private static final double VERTEX_ROUNDING = 1e9;

    // Degenerate triangle threshold
    private static final double MIN_TRIANGLE_AREA = 1e-12;

    private StepMeshExporter() {
    }

    // --- Semantic face processing methods (used by Triangulator and MeshTriangulatorParametric) ---
    
    static StepEntity semanticFaceGeometry(com.minicad.step.model.base.StepFaceEntity stepFace) {
        if (stepFace instanceof com.minicad.step.model.topology.StepAdvancedFace) {
            com.minicad.step.model.topology.StepAdvancedFace advancedFace = (com.minicad.step.model.topology.StepAdvancedFace) stepFace;
            return advancedFace.faceGeometry();
        }
        if (stepFace instanceof com.minicad.step.model.topology.StepFaceSurface) {
            com.minicad.step.model.topology.StepFaceSurface faceSurface = (com.minicad.step.model.topology.StepFaceSurface) stepFace;
            return faceSurface.faceGeometry();
        }
        if (stepFace instanceof com.minicad.step.model.topology.StepOrientedFace) {
            com.minicad.step.model.topology.StepOrientedFace orientedFace = (com.minicad.step.model.topology.StepOrientedFace) stepFace;
            return semanticFaceGeometry(orientedFace.faceElement());
        }
        throw new IllegalArgumentException("unsupported face subtype");
    }
    
    static boolean semanticFaceSameSense(com.minicad.step.model.base.StepFaceEntity stepFace) {
        if (stepFace instanceof com.minicad.step.model.topology.StepAdvancedFace) {
            com.minicad.step.model.topology.StepAdvancedFace advancedFace = (com.minicad.step.model.topology.StepAdvancedFace) stepFace;
            return advancedFace.sameSense();
        }
        if (stepFace instanceof com.minicad.step.model.topology.StepFaceSurface) {
            com.minicad.step.model.topology.StepFaceSurface faceSurface = (com.minicad.step.model.topology.StepFaceSurface) stepFace;
            return faceSurface.sameSense();
        }
        if (stepFace instanceof com.minicad.step.model.topology.StepOrientedFace) {
            com.minicad.step.model.topology.StepOrientedFace orientedFace = (com.minicad.step.model.topology.StepOrientedFace) stepFace;
            boolean base = semanticFaceSameSense(orientedFace.faceElement());
            return orientedFace.orientation() ? base : !base;
        }
        throw new IllegalArgumentException("unsupported face subtype");
    }
    
    static SurfaceGeometry buildSemanticSurfaceGeometry(StepEntity geometry, StepCadBuilder builder) {
        if (geometry instanceof com.minicad.step.model.geometry.StepPlane) {
            com.minicad.step.model.geometry.StepPlane plane = (com.minicad.step.model.geometry.StepPlane) geometry;
            return builder.buildPlane(plane.id());
        }
        if (geometry instanceof com.minicad.step.model.geometry.StepCylindricalSurface) {
            com.minicad.step.model.geometry.StepCylindricalSurface cylindricalSurface = (com.minicad.step.model.geometry.StepCylindricalSurface) geometry;
            return builder.buildCylindricalSurface(cylindricalSurface.id());
        }
        if (geometry instanceof com.minicad.step.model.geometry.StepConicalSurface) {
            com.minicad.step.model.geometry.StepConicalSurface conicalSurface = (com.minicad.step.model.geometry.StepConicalSurface) geometry;
            return builder.buildConicalSurface(conicalSurface.id());
        }
        if (geometry instanceof com.minicad.step.model.geometry.StepSphericalSurface) {
            com.minicad.step.model.geometry.StepSphericalSurface sphericalSurface = (com.minicad.step.model.geometry.StepSphericalSurface) geometry;
            return builder.buildSphericalSurface(sphericalSurface.id());
        }
        if (geometry instanceof com.minicad.step.model.geometry.StepToroidalSurface) {
            com.minicad.step.model.geometry.StepToroidalSurface toroidalSurface = (com.minicad.step.model.geometry.StepToroidalSurface) geometry;
            return builder.buildToroidalSurface(toroidalSurface.id());
        }
        if (geometry instanceof com.minicad.step.model.geometry.StepDegenerateToroidalSurface) {
            com.minicad.step.model.geometry.StepDegenerateToroidalSurface degenerateToroidalSurface = (com.minicad.step.model.geometry.StepDegenerateToroidalSurface) geometry;
            return builder.buildDegenerateToroidalSurface(degenerateToroidalSurface.id());
        }
        if (geometry instanceof com.minicad.step.model.geometry.StepSurfaceOfLinearExtrusion) {
            com.minicad.step.model.geometry.StepSurfaceOfLinearExtrusion extrusionSurface = (com.minicad.step.model.geometry.StepSurfaceOfLinearExtrusion) geometry;
            return builder.buildSurfaceOfLinearExtrusion(extrusionSurface.id());
        }
        if (geometry instanceof com.minicad.step.model.geometry.StepSurfaceOfRevolution) {
            com.minicad.step.model.geometry.StepSurfaceOfRevolution revolutionSurface = (com.minicad.step.model.geometry.StepSurfaceOfRevolution) geometry;
            return builder.buildSurfaceOfRevolution(revolutionSurface.id());
        }
        if (geometry instanceof com.minicad.step.model.geometry.StepRationalBSplineSurface) {
            com.minicad.step.model.geometry.StepRationalBSplineSurface rationalSplineSurface = (com.minicad.step.model.geometry.StepRationalBSplineSurface) geometry;
            return builder.buildRationalBSplineSurface(rationalSplineSurface.id());
        }
        if (geometry instanceof com.minicad.step.model.geometry.StepBSplineSurfaceWithKnots) {
            com.minicad.step.model.geometry.StepBSplineSurfaceWithKnots splineSurface = (com.minicad.step.model.geometry.StepBSplineSurfaceWithKnots) geometry;
            return builder.buildBSplineSurface(splineSurface.id());
        }
        if (geometry instanceof com.minicad.step.model.geometry.StepBSplineSurface) {
            com.minicad.step.model.geometry.StepBSplineSurface splineSurface = (com.minicad.step.model.geometry.StepBSplineSurface) geometry;
            return builder.buildGenericBSplineSurface(splineSurface.id());
        }
        if (geometry instanceof com.minicad.step.model.geometry.StepBezierSurface) {
            com.minicad.step.model.geometry.StepBezierSurface splineSurface = (com.minicad.step.model.geometry.StepBezierSurface) geometry;
            return builder.buildBezierSurface(splineSurface.id());
        }
        if (geometry instanceof com.minicad.step.model.geometry.StepUniformSurface) {
            com.minicad.step.model.geometry.StepUniformSurface splineSurface = (com.minicad.step.model.geometry.StepUniformSurface) geometry;
            return builder.buildUniformSurface(splineSurface.id());
        }
        if (geometry instanceof com.minicad.step.model.geometry.StepQuasiUniformSurface) {
            com.minicad.step.model.geometry.StepQuasiUniformSurface splineSurface = (com.minicad.step.model.geometry.StepQuasiUniformSurface) geometry;
            return builder.buildQuasiUniformSurface(splineSurface.id());
        }
        if (geometry instanceof com.minicad.step.model.geometry.StepPiecewiseBezierSurface) {
            com.minicad.step.model.geometry.StepPiecewiseBezierSurface splineSurface = (com.minicad.step.model.geometry.StepPiecewiseBezierSurface) geometry;
            return builder.buildPiecewiseBezierSurface(splineSurface.id());
        }
        if (geometry instanceof com.minicad.step.model.geometry.StepRectangularTrimmedSurface) {
            com.minicad.step.model.geometry.StepRectangularTrimmedSurface trimmedSurface = (com.minicad.step.model.geometry.StepRectangularTrimmedSurface) geometry;
            return buildSemanticSurfaceGeometry(trimmedSurface.basisSurface(), builder);
        }
        if (geometry instanceof com.minicad.step.model.geometry.StepCurveBoundedSurface) {
            com.minicad.step.model.geometry.StepCurveBoundedSurface boundedSurface = (com.minicad.step.model.geometry.StepCurveBoundedSurface) geometry;
            return buildSemanticSurfaceGeometry(boundedSurface.basisSurface(), builder);
        }
        if (geometry instanceof com.minicad.step.model.geometry.StepOrientedSurface) {
            com.minicad.step.model.geometry.StepOrientedSurface orientedSurface = (com.minicad.step.model.geometry.StepOrientedSurface) geometry;
            builder.buildOrientedSurface(orientedSurface.id());
            return buildSemanticSurfaceGeometry(orientedSurface.surfaceElement(), builder);
        }
        if (geometry instanceof com.minicad.step.model.geometry.StepOffsetSurface) {
            com.minicad.step.model.geometry.StepOffsetSurface offsetSurface = (com.minicad.step.model.geometry.StepOffsetSurface) geometry;
            builder.buildOffsetSurface(offsetSurface.id());
            SurfaceGeometry base = buildSemanticSurfaceGeometry(offsetSurface.basisSurface(), builder);
            return offsetSemanticSurfaceGeometry(base, offsetSurface.distance());
        }
        if (geometry instanceof com.minicad.step.model.product.StepGeometricReplica) {
            com.minicad.step.model.product.StepGeometricReplica replica = (com.minicad.step.model.product.StepGeometricReplica) geometry;
            if ("SURFACE_REPLICA".equals(replica.entityName())) {
                builder.buildSurfaceReplica(replica.id());
                SurfaceGeometry base = buildSemanticSurfaceGeometry(replica.parent(), builder);
                return transformSemanticSurfaceGeometry(base, replica.transformation(), builder);
            }
        }
        return null;
    }
    
    private static SurfaceGeometry offsetSemanticSurfaceGeometry(SurfaceGeometry base, double distance) {
        if (base == null) {
            return null;
        }
        if (base instanceof Plane) {
            Plane plane = (Plane) base;
            return new Plane(
                    plane.origin().add(plane.normal().asVector().scale(distance)),
                    plane.normal());
        }
        if (base instanceof CylindricalSurface) {
            CylindricalSurface cylindricalSurface = (CylindricalSurface) base;
            return new CylindricalSurface(
                    cylindricalSurface.position(),
                    cylindricalSurface.radius() + distance);
        }
        if (base instanceof SphericalSurface) {
            SphericalSurface sphericalSurface = (SphericalSurface) base;
            return new SphericalSurface(
                    sphericalSurface.position(),
                    sphericalSurface.radius() + distance);
        }
        if (base instanceof ConicalSurface) {
            ConicalSurface conicalSurface = (ConicalSurface) base;
            return offsetConicalSurface(conicalSurface, distance);
        }
        if (base instanceof ToroidalSurface) {
            ToroidalSurface toroidalSurface = (ToroidalSurface) base;
            return new ToroidalSurface(
                    toroidalSurface.position(),
                    toroidalSurface.majorRadius(),
                    toroidalSurface.minorRadius() + distance);
        }
        return null;
    }
    
    private static ConicalSurface offsetConicalSurface(ConicalSurface conicalSurface, double distance) {
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
    
    private static SurfaceGeometry transformSemanticSurfaceGeometry(
            SurfaceGeometry surface,
            com.minicad.step.model.geometry.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        if (surface == null) {
            return null;
        }
        double scale = Math.abs(transformationScale(transformation));
        if (surface instanceof Plane) {
            Plane plane = (Plane) surface;
            return new Plane(
                    transformPoint3(plane.origin(), transformation, builder),
                    transformDirection3(plane.normal(), transformation, builder));
        }
        if (surface instanceof CylindricalSurface) {
            CylindricalSurface cylindricalSurface = (CylindricalSurface) surface;
            return new CylindricalSurface(
                    transformPlacement(cylindricalSurface.position(), transformation, builder),
                    cylindricalSurface.radius() * scale);
        }
        if (surface instanceof ConicalSurface) {
            ConicalSurface conicalSurface = (ConicalSurface) surface;
            return new ConicalSurface(
                    transformPlacement(conicalSurface.position(), transformation, builder),
                    conicalSurface.radius() * scale,
                    conicalSurface.semiAngle());
        }
        if (surface instanceof SphericalSurface) {
            SphericalSurface sphericalSurface = (SphericalSurface) surface;
            return new SphericalSurface(
                    transformPlacement(sphericalSurface.position(), transformation, builder),
                    sphericalSurface.radius() * scale);
        }
        if (surface instanceof ToroidalSurface) {
            ToroidalSurface toroidalSurface = (ToroidalSurface) surface;
            return new ToroidalSurface(
                    transformPlacement(toroidalSurface.position(), transformation, builder),
                    toroidalSurface.majorRadius() * scale,
                    toroidalSurface.minorRadius() * scale);
        }
        if (surface instanceof SurfaceOfRevolution3) {
            SurfaceOfRevolution3 revolutionSurface = (SurfaceOfRevolution3) surface;
            Curve3 sweptCurve = transformSemanticCurve3(revolutionSurface.sweptCurve(), transformation, builder);
            if (sweptCurve == null) {
                return null;
            }
            return new SurfaceOfRevolution3(
                    sweptCurve,
                    transformPoint3(revolutionSurface.axisOrigin(), transformation, builder),
                    transformDirection3(revolutionSurface.axisDirection(), transformation, builder));
        }
        if (surface instanceof SurfaceOfLinearExtrusion3) {
            SurfaceOfLinearExtrusion3 extrusionSurface = (SurfaceOfLinearExtrusion3) surface;
            Curve3 sweptCurve = transformSemanticCurve3(extrusionSurface.sweptCurve(), transformation, builder);
            if (sweptCurve == null) {
                return null;
            }
            return new SurfaceOfLinearExtrusion3(
                    sweptCurve,
                    transformVector3(extrusionSurface.extrusionVector(), transformation, builder));
        }
        return null;
    }
    
    private static Curve3 transformSemanticCurve3(
            Curve3 curve,
            com.minicad.step.model.geometry.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        double scale = transformationScale(transformation);
        if (curve instanceof Line3) {
            Line3 line = (Line3) curve;
            return new Line3(
                    transformPoint3(line.origin(), transformation, builder),
                    transformDirection3(line.direction(), transformation, builder),
                    line.parameterScale() * Math.abs(scale));
        }
        if (curve instanceof Circle) {
            Circle circle = (Circle) curve;
            return new Circle(
                    transformPlacement(circle.position(), transformation, builder),
                    circle.radius() * Math.abs(scale));
        }
        if (curve instanceof Ellipse3) {
            Ellipse3 ellipse = (Ellipse3) curve;
            return new Ellipse3(
                    transformPlacement(ellipse.position(), transformation, builder),
                    ellipse.semiAxis1() * Math.abs(scale),
                    ellipse.semiAxis2() * Math.abs(scale));
        }
        if (curve instanceof Polyline3) {
            Polyline3 polyline = (Polyline3) curve;
            return new Polyline3(polyline.points().stream()
                    .map(point -> transformPoint3(point, transformation, builder))
                    .collect(Collectors.toList()));
        }
        if (curve instanceof BSplineCurve3) {
            BSplineCurve3 bsplineCurve = (BSplineCurve3) curve;
            return new BSplineCurve3(
                    bsplineCurve.degree(),
                    bsplineCurve.controlPoints().stream()
                            .map(point -> transformPoint3(point, transformation, builder))
                            .collect(Collectors.toList()),
                    bsplineCurve.knotMultiplicities(),
                    bsplineCurve.knots());
        }
        if (curve instanceof RationalBSplineCurve3) {
            RationalBSplineCurve3 rationalBSplineCurve = (RationalBSplineCurve3) curve;
            return new RationalBSplineCurve3(
                    rationalBSplineCurve.degree(),
                    rationalBSplineCurve.controlPoints().stream()
                            .map(point -> transformPoint3(point, transformation, builder))
                            .collect(Collectors.toList()),
                    rationalBSplineCurve.weights(),
                    rationalBSplineCurve.knotMultiplicities(),
                    rationalBSplineCurve.knots());
        }
        if (curve instanceof CompositeCurve3) {
            CompositeCurve3 compositeCurve = (CompositeCurve3) curve;
            List<Curve3> transformedSegments = new ArrayList<>(compositeCurve.segments().size());
            for (Curve3 segment : compositeCurve.segments()) {
                Curve3 transformed = transformSemanticCurve3(segment, transformation, builder);
                if (transformed == null) {
                    return null;
                }
                transformedSegments.add(transformed);
            }
            return new CompositeCurve3(transformedSegments);
        }
        if (curve instanceof TrimmedCurve3) {
            TrimmedCurve3 trimmedCurve = (TrimmedCurve3) curve;
            Curve3 basisCurve = transformSemanticCurve3(trimmedCurve.basisCurve(), transformation, builder);
            if (basisCurve == null) {
                return null;
            }
            return new TrimmedCurve3(
                    basisCurve,
                    trimmedCurve.trimParamStart(),
                    trimmedCurve.trimParamEnd(),
                    trimmedCurve.senseAgreement());
        }
        if (curve instanceof SurfaceCurve3) {
            SurfaceCurve3 surfaceCurve = (SurfaceCurve3) curve;
            Curve3 curve3d = transformSemanticCurve3(surfaceCurve.curve3d(), transformation, builder);
            if (curve3d == null) {
                return null;
            }
            return new SurfaceCurve3(curve3d, surfaceCurve.parametricCurves());
        }
        return null;
    }
    
    private static Axis2Placement3D transformPlacement(
            Axis2Placement3D placement,
            com.minicad.step.model.geometry.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        return new Axis2Placement3D(
                transformPoint3(placement.location(), transformation, builder),
                transformDirection3(placement.axis(), transformation, builder),
                transformDirection3(placement.refDirection(), transformation, builder));
    }
    
    static CartesianPoint transformPoint3(
            CartesianPoint point,
            com.minicad.step.model.geometry.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        Vector3 basisX = transformAxis1_3(transformation, builder);
        Vector3 basisY = transformAxis2OrDefault3(transformation, basisX, builder);
        Vector3 basisZ = transformAxis3OrDefault3(transformation, basisX, basisY, builder);
        double scale = transformationScale(transformation);
        Vector3 offset = basisX.scale(point.x() * scale)
                .add(basisY.scale(point.y() * scale))
                .add(basisZ.scale(point.z() * scale));
        return builder.buildPoint(transformation.localOrigin().id()).add(offset);
    }
    
    private static Direction3 transformDirection3(
            Direction3 direction,
            com.minicad.step.model.geometry.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        Vector3 basisX = transformAxis1_3(transformation, builder);
        Vector3 basisY = transformAxis2OrDefault3(transformation, basisX, builder);
        Vector3 basisZ = transformAxis3OrDefault3(transformation, basisX, basisY, builder);
        Vector3 source = direction.asVector();
        return Direction3.from(
                basisX.scale(source.x())
                        .add(basisY.scale(source.y()))
                        .add(basisZ.scale(source.z())));
    }
    
    private static Vector3 transformVector3(
            Vector3 vector,
            com.minicad.step.model.geometry.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        Vector3 basisX = transformAxis1_3(transformation, builder);
        Vector3 basisY = transformAxis2OrDefault3(transformation, basisX, builder);
        Vector3 basisZ = transformAxis3OrDefault3(transformation, basisX, basisY, builder);
        double scale = transformationScale(transformation);
        return basisX.scale(vector.x() * scale)
                .add(basisY.scale(vector.y() * scale))
                .add(basisZ.scale(vector.z() * scale));
    }
    
    private static Vector3 transformAxis1_3(
            com.minicad.step.model.geometry.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        return transformation.axis1() == null
                ? new Vector3(1.0, 0.0, 0.0)
                : builder.buildDirection(transformation.axis1().id()).asVector();
    }
    
    private static Vector3 transformAxis2OrDefault3(
            com.minicad.step.model.geometry.StepCartesianTransformationOperator transformation,
            Vector3 axis1,
            StepCadBuilder builder
    ) {
        if (transformation.axis2() != null) {
            return builder.buildDirection(transformation.axis2().id()).asVector();
        }
        Vector3 fallback = new Vector3(0.0, 1.0, 0.0);
        return axis1.cross(fallback).isZero() ? new Vector3(0.0, 0.0, 1.0) : fallback;
    }
    
    private static Vector3 transformAxis3OrDefault3(
            com.minicad.step.model.geometry.StepCartesianTransformationOperator transformation,
            Vector3 axis1,
            Vector3 axis2,
            StepCadBuilder builder
    ) {
        if (transformation.axis3() != null) {
            return builder.buildDirection(transformation.axis3().id()).asVector();
        }
        Vector3 cross = axis1.cross(axis2);
        return cross.isZero() ? new Vector3(0.0, 0.0, 1.0) : cross.normalize().asVector();
    }
    
    private static double transformationScale(com.minicad.step.model.geometry.StepCartesianTransformationOperator transformation) {
        return transformation.scale() == null ? 1.0 : transformation.scale();
    }
    
    /**
     * Exports STEP text to OBJ format.
     */
    public static String exportObj(String stepText) {
        MeshData mesh = buildMesh(CompiledStepDocument.compile(stepText));
        return formatObj(mesh);
    }
    public static String exportObj(CompiledStepDocument compiled) {
        MeshData mesh = buildMesh(compiled);
        return formatObj(mesh);
    }
    /**
     * Exports STEP text to binary STL format.
     */
    public static byte[] exportStlBinary(String stepText) {
        MeshData mesh = buildMesh(CompiledStepDocument.compile(stepText));
        return formatStlBinary(mesh);
    }
    static byte[] exportStlBinary(CompiledStepDocument compiled) {
        MeshData mesh = buildMesh(compiled);
        return formatStlBinary(mesh);
    }
    /**
     * Exports STEP text to text STL format.
     */
    public static String exportStlText(String stepText) {
        MeshData mesh = buildMesh(CompiledStepDocument.compile(stepText));
        return formatStlText(mesh);
    }
    static String exportStlText(CompiledStepDocument compiled) {
        MeshData mesh = buildMesh(compiled);
        return formatStlText(mesh);
    }
    private static MeshData buildMesh(CompiledStepDocument compiled) {
        Map<Integer, StepEntity> resolved = compiled.resolved();
        StepCadBuilder builder = compiled.builder();

        // Collect face entities for deterministic triangulation.
        List<com.minicad.step.model.base.StepFaceEntity> faceEntities = resolved.values().stream()
                .filter(e -> e instanceof com.minicad.step.model.base.StepFaceEntity)
                .map(e -> (com.minicad.step.model.base.StepFaceEntity) e)
                .collect(Collectors.toList());
        Triangulator t = new Triangulator();
        for (com.minicad.step.model.base.StepFaceEntity faceEntity : faceEntities) {
            try {
                t.triangulateSemanticFace(faceEntity, builder);
            } catch (Exception e) {
                LOG.log(Level.FINE, "Skipping semantic face #{0}: {1}",
                        new Object[]{faceEntity.id(), e.getMessage()});
            }
        }
        // Sequential triangulation of solids/shells (complex dependencies)
        for (Map.Entry<Integer, StepEntity> entry : resolved.entrySet()) {
            int id = entry.getKey();
            StepEntity entity = entry.getValue();
            if (isSemanticFaceBackedEntity(entity)) {
                continue;
            }
            if (builder.canBuildAsSolid(entity)) {
                try {
                    Solid solid = builder.buildSolid(id);
                    t.triangulateSolid(solid);
                } catch (Exception e) {
                    LOG.log(Level.FINE, "Skipping solid #{0}: {1}", new Object[]{id, e.getMessage()});
                }
            } else if (isShellCandidate(entity)) {
                try {
                    Shell shell = builder.buildShell(id);
                    t.triangulateShell(shell);
                } catch (Exception e) {
                    LOG.log(Level.FINE, "Skipping shell #{0}: {1}", new Object[]{id, e.getMessage()});
                }
            }
        }
        return t.toMeshData();
    }
    private static boolean isSemanticFaceBackedEntity(StepEntity entity) {
        return entity instanceof com.minicad.step.model.base.StepFaceEntity
                || entity instanceof com.minicad.step.model.topology.StepOpenShell
                || entity instanceof com.minicad.step.model.topology.StepClosedShell
                || entity instanceof com.minicad.step.model.geometry.StepSurfacedOpenShell
                || entity instanceof com.minicad.step.model.topology.StepOrientedOpenShell
                || entity instanceof com.minicad.step.model.topology.StepOrientedClosedShell
                || entity instanceof com.minicad.step.model.topology.StepConnectedFaceSet
                || entity instanceof com.minicad.step.model.topology.StepConnectedFaceSubSet
                || entity instanceof com.minicad.step.model.product.StepFaceBasedSurfaceModel
                || entity instanceof com.minicad.step.model.geometry.StepManifoldSurfaceModel
                || entity instanceof com.minicad.step.model.product.StepShellBasedSurfaceModel
                || entity instanceof com.minicad.step.model.product.StepManifoldSolidBrep
                || entity instanceof com.minicad.step.model.product.StepBrepWithVoids;
    }
    private static boolean isShellCandidate(StepEntity entity) {
        return entity instanceof com.minicad.step.model.topology.StepOpenShell
                || entity instanceof com.minicad.step.model.topology.StepClosedShell
                || entity instanceof com.minicad.step.model.geometry.StepSurfacedOpenShell
                || entity instanceof com.minicad.step.model.topology.StepConnectedFaceSet
                || entity instanceof com.minicad.step.model.product.StepTessellatedFaceSet
                || entity instanceof com.minicad.step.model.product.StepTessellatedFace
                || entity instanceof com.minicad.step.model.product.StepFaceBasedSurfaceModel
                || entity instanceof com.minicad.step.model.geometry.StepManifoldSurfaceModel
                || entity instanceof com.minicad.step.model.product.StepShellBasedSurfaceModel;
    }
    private static class Triangulator {
        private final Map<MeshVertex, Integer> vertexIndex = new LinkedHashMap<>();
        private final List<int[]> faceIndices = new ArrayList<>();
        private static final double PLANAR_EPS = 1e-9;

        private static class MeshVertex {
            private final double x;
            private final double y;
            private final double z;
            private final double nx;
            private final double ny;
            private final double nz;

            MeshVertex(double x, double y, double z, double nx, double ny, double nz) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.nx = nx;
                this.ny = ny;
                this.nz = nz;
            }
            MeshVertex(CartesianPoint p, Vector3 n) {
                this(Math.round(p.x() * VERTEX_ROUNDING) / VERTEX_ROUNDING,
                     Math.round(p.y() * VERTEX_ROUNDING) / VERTEX_ROUNDING,
                     Math.round(p.z() * VERTEX_ROUNDING) / VERTEX_ROUNDING,
                     Math.round(n.x() * VERTEX_ROUNDING) / VERTEX_ROUNDING,
                     Math.round(n.y() * VERTEX_ROUNDING) / VERTEX_ROUNDING,
                     Math.round(n.z() * VERTEX_ROUNDING) / VERTEX_ROUNDING);
            }
            double x() { return x; }
            double y() { return y; }
            double z() { return z; }
            double nx() { return nx; }
            double ny() { return ny; }
            double nz() { return nz; }
            @Override public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof MeshVertex)) return false;
                MeshVertex that = (MeshVertex) o;
                return Double.compare(x, that.x) == 0 && Double.compare(y, that.y) == 0
                       && Double.compare(z, that.z) == 0 && Double.compare(nx, that.nx) == 0
                       && Double.compare(ny, that.ny) == 0 && Double.compare(nz, that.nz) == 0;
            }
            @Override public int hashCode() { return java.util.Objects.hash(x, y, z, nx, ny, nz); }
        }
        int addVertex(CartesianPoint p, Vector3 n) {
            MeshVertex key = new MeshVertex(p, n);
            Integer idx = vertexIndex.get(key);
            if (idx != null) {
                return idx;
            }
            idx = vertexIndex.size();
            vertexIndex.put(key, idx);
            return idx;
        }
        void addTriangle(int v0, int v1, int v2) {
            faceIndices.add(new int[]{v0, v1, v2});
        }
        void merge(Triangulator other) {
            for (Map.Entry<MeshVertex, Integer> entry : other.vertexIndex.entrySet()) {
                MeshVertex key = entry.getKey();
                if (!vertexIndex.containsKey(key)) {
                    int idx = vertexIndex.size();
                    vertexIndex.put(key, idx);
                }
            }
            // Re-index triangles: build a remap for other's local indices
            List<Integer> otherToLocal = new ArrayList<>(other.vertexIndex.size());
            for (Map.Entry<MeshVertex, Integer> entry : other.vertexIndex.entrySet()) {
                otherToLocal.add(vertexIndex.get(entry.getKey()));
            }
            for (int[] tri : other.faceIndices) {
                faceIndices.add(new int[]{
                        otherToLocal.get(tri[0]),
                        otherToLocal.get(tri[1]),
                        otherToLocal.get(tri[2])
                });
            }
        }
        void triangulateSolid(Solid solid) {
            triangulateShell(solid.outerShell());
            for (Shell shell : solid.voidShells()) {
                triangulateShell(shell);
            }
        }
        void triangulateShell(Shell shell) {
            for (Face face : shell.faces()) {
                triangulateFace(face);
            }
        }
        void triangulateFace(Face face) {
            SurfaceGeometry surface = face.surface();
            boolean flipped = !face.sameSense();

            if (surface instanceof Plane) {
            Plane plane = (Plane) surface;
                triangulatePlanarFace(face, plane, flipped);
            } else if (triangulateParametricFace(face, surface, flipped)) {
                return;
            } else {
                triangulateCurvedFace(face, surface, flipped);
            }
        }
        void triangulateSemanticFace(com.minicad.step.model.base.StepFaceEntity stepFace, StepCadBuilder builder) {
            StepEntity faceGeometry = semanticFaceGeometry(stepFace);
            boolean flipped = !semanticFaceSameSense(stepFace);
            SurfaceGeometry surface = buildSemanticSurfaceGeometry(faceGeometry, builder);
            if (!(surface instanceof Plane) && surface != null
                    && triangulateSemanticParametricFace(stepFace, faceGeometry, surface, builder, flipped)) {
                return;
            }
            Face builtFace = builder.buildFace(stepFace.id());
            if (surface == null) {
                surface = builtFace.surface();
            }
            if (surface instanceof Plane) {
            Plane plane = (Plane) surface;
                triangulatePlanarFace(builtFace, plane, flipped);
            } else if (!triangulateSemanticParametricFace(stepFace, faceGeometry, surface, builder, flipped)) {
                triangulateFace(builtFace);
            }
        }
        private void triangulatePlanarFace(Face face, Plane plane, boolean flipped) {
            MeshTriangulatorPlanar.triangulatePlanarFace(face, plane, flipped,
                    this::addVertex, tri -> addTriangle(tri[0], tri[1], tri[2]));
        }
        private void triangulateCurvedFace(Face face, SurfaceGeometry surface, boolean flipped) {
            int uSegs = DEFAULT_CURVE_SEGMENTS;
            int vSegs = DEFAULT_CURVE_SEGMENTS;
            BoundingBox3 bbox = surface.boundingBox();
            double diag = Math.sqrt(
                Math.pow(bbox.maxX() - bbox.minX(), 2) +
                Math.pow(bbox.maxY() - bbox.minY(), 2) +
                Math.pow(bbox.maxZ() - bbox.minZ(), 2)
            );
            if (diag > 0) {
                int base = Math.max(MIN_CURVE_SEGMENTS,
                    Math.min(MAX_CURVE_SEGMENTS, (int) Math.ceil(diag * BBOX_SEGMENT_MULTIPLIER)));
                uSegs = base;
                vSegs = base;
            }
            List<List<CartesianPoint>> grid = surface.sampleGrid(uSegs, vSegs);
            if (grid.isEmpty() || grid.get(0).isEmpty()) return;
            int rows = grid.size();
            int cols = grid.get(0).size();

            for (int i = 0; i < rows - 1; i++) {
                for (int j = 0; j < cols - 1; j++) {
                    CartesianPoint p00 = grid.get(i).get(j);
                    CartesianPoint p10 = grid.get(i + 1).get(j);
                    CartesianPoint p11 = grid.get(i + 1).get(j + 1);
                    CartesianPoint p01 = grid.get(i).get(j + 1);
                    Vector3 n = computeNormal(p00, p10, p11);
                    if (flipped) n = n.negate();
                    int v00 = addVertex(p00, n);
                    int v10 = addVertex(p10, n);
                    int v11 = addVertex(p11, n);
                    int v01 = addVertex(p01, n);

                    if (flipped) {
                        addTriangle(v00, v11, v10);
                        addTriangle(v00, v01, v11);
                    } else {
                        addTriangle(v00, v10, v11);
                        addTriangle(v00, v11, v01);
                    }
                }
            }
        }
        private boolean triangulateParametricFace(Face face, SurfaceGeometry surface, boolean flipped) {
            return MeshTriangulatorParametric.triangulateParametricFace(
                    face, surface, flipped,
                    () -> faceIndices.size(),
                    this::addVertex,
                    tri -> addTriangle(tri[0], tri[1], tri[2])
            );
        }
        private boolean triangulateSemanticParametricFace(
                com.minicad.step.model.base.StepFaceEntity stepFace,
                StepEntity faceGeometry,
                SurfaceGeometry surface,
                StepCadBuilder builder,
                boolean flipped
        ) {
            return MeshTriangulatorParametric.triangulateSemanticParametricFace(
                    stepFace, faceGeometry, surface, builder, flipped,
                    () -> faceIndices.size(),
                    this::addVertex,
                    tri -> addTriangle(tri[0], tri[1], tri[2])
            );
        }
        private Vector3 computeNormal(CartesianPoint a, CartesianPoint b, CartesianPoint c) {
            Vector3 ab = b.subtract(a);
            Vector3 ac = c.subtract(a);
            Vector3 n = ab.cross(ac);
            double len = n.norm();
            if (len < MIN_TRIANGLE_AREA) {
                return new Vector3(0, 0, 1);
            }
            return n.normalize().asVector();
        }
        private void appendOrientedTriangle(
                CartesianPoint p0,
                CartesianPoint p1,
                CartesianPoint p2,
                Vector3 normal,
                boolean flipped
        ) {
            if (triangleArea(p0, p1, p2) <= MIN_TRIANGLE_AREA) {
                return;
            }
            int v0 = addVertex(p0, normal);
            int v1 = addVertex(p1, normal);
            int v2 = addVertex(p2, normal);
            if (flipped) {
                addTriangle(v0, v2, v1);
            } else {
                addTriangle(v0, v1, v2);
            }
        }
        private List<CartesianPoint> orientSamples(OrientedEdge orientedEdge, List<CartesianPoint> samples) {
            if (samples.isEmpty()) {
                return List.of(
                        orientedEdge.startVertex().point(),
                        orientedEdge.endVertex().point()
                );
            }
            List<CartesianPoint> oriented = new ArrayList<>(samples);
            CartesianPoint expectedStart = orientedEdge.startVertex().point();
            CartesianPoint expectedEnd = orientedEdge.endVertex().point();
            double forward = samples.get(0).distanceTo(expectedStart) + samples.get(samples.size() - 1).distanceTo(expectedEnd);
            double backward = samples.get(0).distanceTo(expectedEnd) + samples.get(samples.size() - 1).distanceTo(expectedStart);
            if (backward < forward) {
                Collections.reverse(oriented);
            }
            if (!oriented.get(0).equals(expectedStart)) {
                oriented.set(0, expectedStart);
            }
            if (!oriented.get(oriented.size() - 1).equals(expectedEnd)) {
                oriented.set(oriented.size() - 1, expectedEnd);
            }
            return List.copyOf(oriented);
        }
        private double triangleArea(CartesianPoint a, CartesianPoint b, CartesianPoint c) {
            return b.subtract(a).cross(c.subtract(a)).norm() * 0.5;
        }
        MeshData toMeshData() {
            List<double[]> v = new ArrayList<>(vertexIndex.size());
            List<double[]> n = new ArrayList<>(vertexIndex.size());
            // Invert the map: index -> MeshVertex
            MeshVertex[] byIndex = new MeshVertex[vertexIndex.size()];
            for (Map.Entry<MeshVertex, Integer> e : vertexIndex.entrySet()) {
                byIndex[e.getValue()] = e.getKey();
            }
            for (MeshVertex mv : byIndex) {
                v.add(new double[]{mv.x(), mv.y(), mv.z()});
                n.add(new double[]{mv.nx(), mv.ny(), mv.nz()});
            }
            return new MeshData(v, n, faceIndices);
        }
    }

    public static final class MeshData {
        private final List<double[]> vertices;
        private final List<double[]> normals;
        private final List<int[]> triangles;

        public MeshData(List<double[]> vertices, List<double[]> normals, List<int[]> triangles) {
            this.vertices = vertices == null ? null : java.util.List.copyOf(vertices);
            this.normals = normals == null ? null : java.util.List.copyOf(normals);
            this.triangles = triangles == null ? null : java.util.List.copyOf(triangles);
        }
        public List<double[]> getVertices() { return vertices; }
        public List<double[]> getNormals() { return normals; }
        public List<int[]> getTriangles() { return triangles; }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MeshData that = (MeshData) o;
            return Objects.equals(vertices, that.vertices) && Objects.equals(normals, that.normals) && Objects.equals(triangles, that.triangles);
        }
        @Override public int hashCode() { return Objects.hash(vertices, normals, triangles); }
        @Override
        public String toString() {
            return "MeshData{" + "vertices=" + vertices + "normals=" + normals + "triangles=" + triangles + "}";
        }
    }

    private static String formatObj(MeshData mesh) {
        int vCount = mesh.getVertices().size();
        int nCount = mesh.getNormals().size();
        int fCount = mesh.getTriangles().size();
        // Estimate: "v " + 3 doubles (~15 each) + "\n" = ~50 per vertex
        // "vn " + 3 doubles = ~50 per normal, "f 1//1 2//2 3//3\n" = ~30 per face
        StringBuilder sb = new StringBuilder(vCount * 50 + nCount * 50 + fCount * 30);
        sb.append("# Generated by MiniCAD STEP Mesh Exporter\n");

        for (double[] v : mesh.getVertices()) {
            sb.append("v ");
            append6(sb, v[0]); sb.append(' ');
            append6(sb, v[1]); sb.append(' ');
            append6(sb, v[2]); sb.append('\n');
        }
        for (double[] n : mesh.getNormals()) {
            sb.append("vn ");
            append6(sb, n[0]); sb.append(' ');
            append6(sb, n[1]); sb.append(' ');
            append6(sb, n[2]); sb.append('\n');
        }
        for (int[] tri : mesh.getTriangles()) {
            int v0 = tri[0] + 1;
            int v1 = tri[1] + 1;
            int v2 = tri[2] + 1;
            sb.append("f ").append(v0).append("//").append(v0).append(' ')
                    .append(v1).append("//").append(v1).append(' ')
                    .append(v2).append("//").append(v2).append('\n');
        }
        return sb.toString();
    }
    private static byte[] formatStlBinary(MeshData mesh) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.writeBytes(new byte[80]);
        ByteBuffer bb = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        bb.putInt(mesh.getTriangles().size());
        baos.writeBytes(bb.array());
        bb = ByteBuffer.allocate(50).order(ByteOrder.LITTLE_ENDIAN);
        for (int[] tri : mesh.getTriangles()) {
            double[] n = mesh.getNormals().get(tri[0]);
            bb.putFloat((float) n[0]);
            bb.putFloat((float) n[1]);
            bb.putFloat((float) n[2]);

            for (int vi : tri) {
                double[] v = mesh.getVertices().get(vi);
                bb.putFloat((float) v[0]);
                bb.putFloat((float) v[1]);
                bb.putFloat((float) v[2]);
            }
            bb.putShort((short) 0);
            baos.writeBytes(bb.array());
            bb.clear();
        }
        return baos.toByteArray();
    }
    private static String formatStlText(MeshData mesh) {
        StringBuilder sb = new StringBuilder();
        sb.append("solid MiniCAD\n");

        for (int[] tri : mesh.getTriangles()) {
            double[] n = mesh.getNormals().get(tri[0]);
            sb.append("  facet normal ");
            append6(sb, n[0]); sb.append(' ');
            append6(sb, n[1]); sb.append(' ');
            append6(sb, n[2]); sb.append('\n');
            sb.append("    outer loop\n");
            for (int vi : tri) {
                double[] v = mesh.getVertices().get(vi);
                sb.append("      vertex ");
                append6(sb, v[0]); sb.append(' ');
                append6(sb, v[1]); sb.append(' ');
                append6(sb, v[2]); sb.append('\n');
            }
            sb.append("    endloop\n");
            sb.append("  endfacet\n");
        }
        sb.append("endsolid MiniCAD\n");
        return sb.toString();
    }
    /** Fast double-to-string with 6 decimal places, avoiding String.format overhead. */
    private static void append6(StringBuilder sb, double d) {
        // Handle sign
        if (d < 0) {
            sb.append('-');
            d = -d;
        }
        long scaled = Math.round(d * 1_000_000.0);
        long intPart = scaled / 1_000_000;
        long fracPart = scaled % 1_000_000;
        sb.append(intPart).append('.');
        if (fracPart < 100_000) sb.append('0');
        if (fracPart < 10_000) sb.append('0');
        if (fracPart < 1_000) sb.append('0');
        if (fracPart < 100) sb.append('0');
        if (fracPart < 10) sb.append('0');
        sb.append(fracPart);
    }
}