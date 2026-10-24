package com.minicad.export.mesh;
import com.minicad.geometry.*;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.helper.ShapeRepresentationHelper;
import com.minicad.helper.ShellHelper;
import com.minicad.step.model.StepAdvancedBrep;
import com.minicad.step.model.StepBrepWithVoids;
import com.minicad.step.model.StepCompoundRepresentationItem;
import com.minicad.step.model.StepConnectedFaceSet;
import com.minicad.step.model.StepConnectedFaceSubSet;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFacettedBrep;
import com.minicad.step.model.StepFaceBasedSurfaceModel;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepManifoldSurfaceModel;
import com.minicad.step.model.StepNonManifoldSolidBrep;
import com.minicad.step.model.StepOverRidingStyledItem;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.model.StepShellBasedSurfaceModel;
import com.minicad.step.model.StepStyledItem;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.TransformationOperatorBasis;
import com.minicad.builder.CompiledStepDocument;
import com.minicad.builder.StepAssemblyGraphBuilder;
import com.minicad.topology.*;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exports STEP files to OBJ and STL mesh formats.
 * Uses the existing STEP parser/resolver/builder pipeline, then triangulates
 * all faces and formats the result as OBJ or STL.
 */
public final class StepMeshExporter {

    private static final Logger LOG = LoggerFactory.getLogger(StepMeshExporter.class);

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

    static StepEntity semanticFaceGeometry(com.minicad.step.model.StepFaceEntity stepFace) {
        if (stepFace instanceof com.minicad.step.model.StepAdvancedFace) {
            com.minicad.step.model.StepAdvancedFace advancedFace = (com.minicad.step.model.StepAdvancedFace) stepFace;
            return advancedFace.faceGeometry();
        }
        if (stepFace instanceof com.minicad.step.model.StepFaceSurface) {
            com.minicad.step.model.StepFaceSurface faceSurface = (com.minicad.step.model.StepFaceSurface) stepFace;
            return faceSurface.faceGeometry();
        }
        if (stepFace instanceof com.minicad.step.model.StepOrientedFace) {
            com.minicad.step.model.StepOrientedFace orientedFace = (com.minicad.step.model.StepOrientedFace) stepFace;
            return semanticFaceGeometry(orientedFace.faceElement());
        }
        throw new IllegalArgumentException("unsupported face subtype");
    }

    static boolean semanticFaceSameSense(com.minicad.step.model.StepFaceEntity stepFace) {
        if (stepFace instanceof com.minicad.step.model.StepAdvancedFace) {
            com.minicad.step.model.StepAdvancedFace advancedFace = (com.minicad.step.model.StepAdvancedFace) stepFace;
            return advancedFace.sameSense();
        }
        if (stepFace instanceof com.minicad.step.model.StepFaceSurface) {
            com.minicad.step.model.StepFaceSurface faceSurface = (com.minicad.step.model.StepFaceSurface) stepFace;
            return faceSurface.sameSense();
        }
        if (stepFace instanceof com.minicad.step.model.StepOrientedFace) {
            com.minicad.step.model.StepOrientedFace orientedFace = (com.minicad.step.model.StepOrientedFace) stepFace;
            boolean base = semanticFaceSameSense(orientedFace.faceElement());
            return orientedFace.orientation() ? base : !base;
        }
        throw new IllegalArgumentException("unsupported face subtype");
    }

    // buildSemanticSurfaceGeometry dispatch table (null means keep looking,
    // mirrors the original sequential ifs).
    private record SemanticSurfaceGeometryRule(
            Class<? extends StepEntity> type, SemanticSurfaceGeometryHandler handler) {}

    private interface SemanticSurfaceGeometryHandler {
        SurfaceGeometry build(StepEntity geometry, StepCadBuilder builder);
    }

    private static SemanticSurfaceGeometryRule semanticSurfaceGeometryRule(
            Class<? extends StepEntity> type, SemanticSurfaceGeometryHandler handler) {
        return new SemanticSurfaceGeometryRule(type, handler);
    }

    private static final List<SemanticSurfaceGeometryRule> SEMANTIC_SURFACE_GEOMETRY_RULES = List.of(
        semanticSurfaceGeometryRule(com.minicad.step.model.StepPlane.class, (geometry, builder) -> {
            com.minicad.step.model.StepPlane plane = (com.minicad.step.model.StepPlane) geometry;
            return builder.buildPlane(plane.id());
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepCylindricalSurface.class, (geometry, builder) -> {
            com.minicad.step.model.StepCylindricalSurface cylindricalSurface = (com.minicad.step.model.StepCylindricalSurface) geometry;
            return builder.buildCylindricalSurface(cylindricalSurface.id());
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepConicalSurface.class, (geometry, builder) -> {
            com.minicad.step.model.StepConicalSurface conicalSurface = (com.minicad.step.model.StepConicalSurface) geometry;
            return builder.buildConicalSurface(conicalSurface.id());
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepSphericalSurface.class, (geometry, builder) -> {
            com.minicad.step.model.StepSphericalSurface sphericalSurface = (com.minicad.step.model.StepSphericalSurface) geometry;
            return builder.buildSphericalSurface(sphericalSurface.id());
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepToroidalSurface.class, (geometry, builder) -> {
            com.minicad.step.model.StepToroidalSurface toroidalSurface = (com.minicad.step.model.StepToroidalSurface) geometry;
            return builder.buildToroidalSurface(toroidalSurface.id());
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepDegenerateToroidalSurface.class, (geometry, builder) -> {
            com.minicad.step.model.StepDegenerateToroidalSurface degenerateToroidalSurface = (com.minicad.step.model.StepDegenerateToroidalSurface) geometry;
            return builder.buildDegenerateToroidalSurface(degenerateToroidalSurface.id());
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepSurfaceOfLinearExtrusion.class, (geometry, builder) -> {
            com.minicad.step.model.StepSurfaceOfLinearExtrusion extrusionSurface = (com.minicad.step.model.StepSurfaceOfLinearExtrusion) geometry;
            return builder.buildSurfaceOfLinearExtrusion(extrusionSurface.id());
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepSurfaceOfRevolution.class, (geometry, builder) -> {
            com.minicad.step.model.StepSurfaceOfRevolution revolutionSurface = (com.minicad.step.model.StepSurfaceOfRevolution) geometry;
            return builder.buildSurfaceOfRevolution(revolutionSurface.id());
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepRationalBSplineSurface.class, (geometry, builder) -> {
            com.minicad.step.model.StepRationalBSplineSurface rationalSplineSurface = (com.minicad.step.model.StepRationalBSplineSurface) geometry;
            return builder.buildRationalBSplineSurface(rationalSplineSurface.id());
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepBSplineSurfaceWithKnots.class, (geometry, builder) -> {
            com.minicad.step.model.StepBSplineSurfaceWithKnots splineSurface = (com.minicad.step.model.StepBSplineSurfaceWithKnots) geometry;
            return builder.buildBSplineSurface(splineSurface.id());
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepBSplineSurface.class, (geometry, builder) -> {
            com.minicad.step.model.StepBSplineSurface splineSurface = (com.minicad.step.model.StepBSplineSurface) geometry;
            return builder.buildGenericBSplineSurface(splineSurface.id());
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepBezierSurface.class, (geometry, builder) -> {
            com.minicad.step.model.StepBezierSurface splineSurface = (com.minicad.step.model.StepBezierSurface) geometry;
            return builder.buildBezierSurface(splineSurface.id());
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepUniformSurface.class, (geometry, builder) -> {
            com.minicad.step.model.StepUniformSurface splineSurface = (com.minicad.step.model.StepUniformSurface) geometry;
            return builder.buildUniformSurface(splineSurface.id());
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepQuasiUniformSurface.class, (geometry, builder) -> {
            com.minicad.step.model.StepQuasiUniformSurface splineSurface = (com.minicad.step.model.StepQuasiUniformSurface) geometry;
            return builder.buildQuasiUniformSurface(splineSurface.id());
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepPiecewiseBezierSurface.class, (geometry, builder) -> {
            com.minicad.step.model.StepPiecewiseBezierSurface splineSurface = (com.minicad.step.model.StepPiecewiseBezierSurface) geometry;
            return builder.buildPiecewiseBezierSurface(splineSurface.id());
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepRectangularTrimmedSurface.class, (geometry, builder) -> {
            com.minicad.step.model.StepRectangularTrimmedSurface trimmedSurface = (com.minicad.step.model.StepRectangularTrimmedSurface) geometry;
            return buildSemanticSurfaceGeometry(trimmedSurface.basisSurface(), builder);
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepCurveBoundedSurface.class, (geometry, builder) -> {
            com.minicad.step.model.StepCurveBoundedSurface boundedSurface = (com.minicad.step.model.StepCurveBoundedSurface) geometry;
            return buildSemanticSurfaceGeometry(boundedSurface.basisSurface(), builder);
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepOrientedSurface.class, (geometry, builder) -> {
            com.minicad.step.model.StepOrientedSurface orientedSurface = (com.minicad.step.model.StepOrientedSurface) geometry;
            builder.buildOrientedSurface(orientedSurface.id());
            return buildSemanticSurfaceGeometry(orientedSurface.surfaceElement(), builder);
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepOffsetSurface.class, (geometry, builder) -> {
            com.minicad.step.model.StepOffsetSurface offsetSurface = (com.minicad.step.model.StepOffsetSurface) geometry;
            builder.buildOffsetSurface(offsetSurface.id());
            SurfaceGeometry base = buildSemanticSurfaceGeometry(offsetSurface.basisSurface(), builder);
            return offsetSemanticSurfaceGeometry(base, offsetSurface.distance());
        }),
        semanticSurfaceGeometryRule(com.minicad.step.model.StepGeometricReplica.class, (geometry, builder) -> {
            com.minicad.step.model.StepGeometricReplica replica = (com.minicad.step.model.StepGeometricReplica) geometry;
            if ("SURFACE_REPLICA".equals(replica.entityName())) {
                builder.buildSurfaceReplica(replica.id());
                SurfaceGeometry base = buildSemanticSurfaceGeometry(replica.parent(), builder);
                return transformSemanticSurfaceGeometry(base, replica.transformation(), builder);
            }
            return null;
        })
    );

    static SurfaceGeometry buildSemanticSurfaceGeometry(StepEntity geometry, StepCadBuilder builder) {
        for (SemanticSurfaceGeometryRule rule : SEMANTIC_SURFACE_GEOMETRY_RULES) {
            if (rule.type().isInstance(geometry)) {
                SurfaceGeometry result = rule.handler().build(geometry, builder);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

    // offsetSemanticSurfaceGeometry dispatch table (first match wins, mirrors
    // the original sequential ifs; an unsupported base returns null, as the old
    // trailing statement did). Kept separate from StepCadBuilder's
    // OFFSET_SURFACE_RULES on purpose -- different component, and the two
    // differ: this one has no OffsetSurface3 branch and returns null instead of
    // wrapping, and it uses the record-style accessors. All 5 types are final
    // direct SurfaceGeometry implementations, so the order is behaviour neutral
    // today but frozen by mesh-offset-surface-dispatch-order.txt.
    private record OffsetSemanticSurfaceRule(
            Class<? extends SurfaceGeometry> type, OffsetSemanticSurfaceHandler handler) {}

    private interface OffsetSemanticSurfaceHandler {
        SurfaceGeometry offset(SurfaceGeometry surface, double distance);
    }

    private static OffsetSemanticSurfaceRule offsetSemanticSurfaceRule(
            Class<? extends SurfaceGeometry> type, OffsetSemanticSurfaceHandler handler) {
        return new OffsetSemanticSurfaceRule(type, handler);
    }

    private static final List<OffsetSemanticSurfaceRule> OFFSET_SEMANTIC_SURFACE_RULES = List.of(
        offsetSemanticSurfaceRule(Plane.class, (surface, distance) -> {
            Plane plane = (Plane) surface;
            return new Plane(
                    plane.origin().add(plane.normal().asVector().scale(distance)),
                    plane.normal());
        }),
        offsetSemanticSurfaceRule(CylindricalSurface.class, (surface, distance) -> {
            CylindricalSurface cylindricalSurface = (CylindricalSurface) surface;
            return new CylindricalSurface(
                    cylindricalSurface.position(),
                    cylindricalSurface.radius() + distance);
        }),
        offsetSemanticSurfaceRule(SphericalSurface.class, (surface, distance) -> {
            SphericalSurface sphericalSurface = (SphericalSurface) surface;
            return new SphericalSurface(
                    sphericalSurface.position(),
                    sphericalSurface.radius() + distance);
        }),
        offsetSemanticSurfaceRule(ConicalSurface.class, (surface, distance) ->
                offsetConicalSurface((ConicalSurface) surface, distance)),
        offsetSemanticSurfaceRule(ToroidalSurface.class, (surface, distance) -> {
            ToroidalSurface toroidalSurface = (ToroidalSurface) surface;
            return new ToroidalSurface(
                    toroidalSurface.position(),
                    toroidalSurface.majorRadius(),
                    toroidalSurface.minorRadius() + distance);
        })
    );

    private static SurfaceGeometry offsetSemanticSurfaceGeometry(SurfaceGeometry base, double distance) {
        if (base == null) {
            return null;
        }
        for (OffsetSemanticSurfaceRule rule : OFFSET_SEMANTIC_SURFACE_RULES) {
            if (rule.type().isInstance(base)) {
                return rule.handler().offset(base, distance);
            }
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

    // transformSemanticSurfaceGeometry dispatch table (first match wins,
    // mirrors the original sequential ifs; a null result propagates to the
    // caller). The scale is hoisted by the entry method as Math.abs(...) of
    // the operator scale, matching the original chain.
    private record TransformSemanticSurfaceRule(
            Class<? extends SurfaceGeometry> type, TransformSemanticSurfaceHandler handler) {}

    private interface TransformSemanticSurfaceHandler {
        SurfaceGeometry transform(SurfaceGeometry surface,
                com.minicad.step.model.StepCartesianTransformationOperator transformation,
                double scale, StepCadBuilder builder);
    }

    private static TransformSemanticSurfaceRule transformSemanticSurfaceRule(
            Class<? extends SurfaceGeometry> type, TransformSemanticSurfaceHandler handler) {
        return new TransformSemanticSurfaceRule(type, handler);
    }

    private static final List<TransformSemanticSurfaceRule> TRANSFORM_SEMANTIC_SURFACE_RULES = List.of(
        transformSemanticSurfaceRule(Plane.class, (surface, transformation, scale, builder) -> {
            Plane plane = (Plane) surface;
            return new Plane(
                    transformPoint3(plane.origin(), transformation, builder),
                    transformDirection3(plane.normal(), transformation, builder));
        }),
        transformSemanticSurfaceRule(CylindricalSurface.class, (surface, transformation, scale, builder) -> {
            CylindricalSurface cylindricalSurface = (CylindricalSurface) surface;
            return new CylindricalSurface(
                    transformPlacement(cylindricalSurface.position(), transformation, builder),
                    cylindricalSurface.radius() * scale);
        }),
        transformSemanticSurfaceRule(ConicalSurface.class, (surface, transformation, scale, builder) -> {
            ConicalSurface conicalSurface = (ConicalSurface) surface;
            return new ConicalSurface(
                    transformPlacement(conicalSurface.position(), transformation, builder),
                    conicalSurface.radius() * scale,
                    conicalSurface.semiAngle());
        }),
        transformSemanticSurfaceRule(SphericalSurface.class, (surface, transformation, scale, builder) -> {
            SphericalSurface sphericalSurface = (SphericalSurface) surface;
            return new SphericalSurface(
                    transformPlacement(sphericalSurface.position(), transformation, builder),
                    sphericalSurface.radius() * scale);
        }),
        transformSemanticSurfaceRule(ToroidalSurface.class, (surface, transformation, scale, builder) -> {
            ToroidalSurface toroidalSurface = (ToroidalSurface) surface;
            return new ToroidalSurface(
                    transformPlacement(toroidalSurface.position(), transformation, builder),
                    toroidalSurface.majorRadius() * scale,
                    toroidalSurface.minorRadius() * scale);
        }),
        transformSemanticSurfaceRule(SurfaceOfRevolution3.class, (surface, transformation, scale, builder) -> {
            SurfaceOfRevolution3 revolutionSurface = (SurfaceOfRevolution3) surface;
            Curve3 sweptCurve = transformSemanticCurve3(revolutionSurface.sweptCurve(), transformation, builder);
            if (sweptCurve == null) {
                return null;
            }
            return new SurfaceOfRevolution3(
                    sweptCurve,
                    transformPoint3(revolutionSurface.axisOrigin(), transformation, builder),
                    transformDirection3(revolutionSurface.axisDirection(), transformation, builder));
        }),
        transformSemanticSurfaceRule(SurfaceOfLinearExtrusion3.class, (surface, transformation, scale, builder) -> {
            SurfaceOfLinearExtrusion3 extrusionSurface = (SurfaceOfLinearExtrusion3) surface;
            Curve3 sweptCurve = transformSemanticCurve3(extrusionSurface.sweptCurve(), transformation, builder);
            if (sweptCurve == null) {
                return null;
            }
            return new SurfaceOfLinearExtrusion3(
                    sweptCurve,
                    transformVector3(extrusionSurface.extrusionVector(), transformation, builder));
        })
    );

    private static SurfaceGeometry transformSemanticSurfaceGeometry(
            SurfaceGeometry surface,
            com.minicad.step.model.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        if (surface == null) {
            return null;
        }
        double scale = Math.abs(TransformationOperatorBasis.scaleOf(transformation));
        for (TransformSemanticSurfaceRule rule : TRANSFORM_SEMANTIC_SURFACE_RULES) {
            if (rule.type().isInstance(surface)) {
                return rule.handler().transform(surface, transformation, scale, builder);
            }
        }
        return null;
    }

    // transformSemanticCurve3 dispatch table (first match wins, mirrors the
    // original sequential ifs; a null result propagates to the caller).
    private record TransformSemanticCurve3Rule(
            Class<? extends Curve3> type, TransformSemanticCurve3Handler handler) {}

    private interface TransformSemanticCurve3Handler {
        Curve3 transform(Curve3 curve,
                com.minicad.step.model.StepCartesianTransformationOperator transformation,
                double scale, StepCadBuilder builder);
    }

    private static TransformSemanticCurve3Rule transformSemanticCurve3Rule(
            Class<? extends Curve3> type, TransformSemanticCurve3Handler handler) {
        return new TransformSemanticCurve3Rule(type, handler);
    }

    private static final List<TransformSemanticCurve3Rule> TRANSFORM_SEMANTIC_CURVE3_RULES = List.of(
        transformSemanticCurve3Rule(Line3.class, (curve, transformation, scale, builder) -> {
            Line3 line = (Line3) curve;
            return new Line3(
                    transformPoint3(line.origin(), transformation, builder),
                    transformDirection3(line.direction(), transformation, builder),
                    line.parameterScale() * Math.abs(scale));
        }),
        transformSemanticCurve3Rule(Circle.class, (curve, transformation, scale, builder) -> {
            Circle circle = (Circle) curve;
            return new Circle(
                    transformPlacement(circle.position(), transformation, builder),
                    circle.radius() * Math.abs(scale));
        }),
        transformSemanticCurve3Rule(Ellipse3.class, (curve, transformation, scale, builder) -> {
            Ellipse3 ellipse = (Ellipse3) curve;
            return new Ellipse3(
                    transformPlacement(ellipse.position(), transformation, builder),
                    ellipse.semiAxis1() * Math.abs(scale),
                    ellipse.semiAxis2() * Math.abs(scale));
        }),
        transformSemanticCurve3Rule(Polyline3.class, (curve, transformation, scale, builder) -> {
            Polyline3 polyline = (Polyline3) curve;
            return new Polyline3(polyline.points().stream()
                    .map(point -> transformPoint3(point, transformation, builder))
                    .collect(Collectors.toList()));
        }),
        transformSemanticCurve3Rule(BSplineCurve3.class, (curve, transformation, scale, builder) -> {
            BSplineCurve3 bsplineCurve = (BSplineCurve3) curve;
            return new BSplineCurve3(
                    bsplineCurve.degree(),
                    bsplineCurve.controlPoints().stream()
                            .map(point -> transformPoint3(point, transformation, builder))
                            .collect(Collectors.toList()),
                    bsplineCurve.knotMultiplicities(),
                    bsplineCurve.knots());
        }),
        transformSemanticCurve3Rule(RationalBSplineCurve3.class, (curve, transformation, scale, builder) -> {
            RationalBSplineCurve3 rationalBSplineCurve = (RationalBSplineCurve3) curve;
            return new RationalBSplineCurve3(
                    rationalBSplineCurve.degree(),
                    rationalBSplineCurve.controlPoints().stream()
                            .map(point -> transformPoint3(point, transformation, builder))
                            .collect(Collectors.toList()),
                    rationalBSplineCurve.weights(),
                    rationalBSplineCurve.knotMultiplicities(),
                    rationalBSplineCurve.knots());
        }),
        transformSemanticCurve3Rule(CompositeCurve3.class, (curve, transformation, scale, builder) -> {
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
        }),
        transformSemanticCurve3Rule(TrimmedCurve3.class, (curve, transformation, scale, builder) -> {
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
        }),
        transformSemanticCurve3Rule(SurfaceCurve3.class, (curve, transformation, scale, builder) -> {
            SurfaceCurve3 surfaceCurve = (SurfaceCurve3) curve;
            Curve3 curve3d = transformSemanticCurve3(surfaceCurve.curve3d(), transformation, builder);
            if (curve3d == null) {
                return null;
            }
            return new SurfaceCurve3(curve3d, surfaceCurve.parametricCurves());
        })
    );

    private static Curve3 transformSemanticCurve3(
            Curve3 curve,
            com.minicad.step.model.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        double scale = TransformationOperatorBasis.scaleOf(transformation);
        for (TransformSemanticCurve3Rule rule : TRANSFORM_SEMANTIC_CURVE3_RULES) {
            if (rule.type().isInstance(curve)) {
                return rule.handler().transform(curve, transformation, scale, builder);
            }
        }
        return null;
    }

    private static Axis2Placement3D transformPlacement(
            Axis2Placement3D placement,
            com.minicad.step.model.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        return new Axis2Placement3D(
                transformPoint3(placement.location(), transformation, builder),
                transformDirection3(placement.axis(), transformation, builder),
                transformDirection3(placement.refDirection(), transformation, builder));
    }

    static CartesianPoint transformPoint3(
            CartesianPoint point,
            com.minicad.step.model.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        return TransformationOperatorBasis.transformPoint(point, transformation, builder);
    }

    private static Direction3 transformDirection3(
            Direction3 direction,
            com.minicad.step.model.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        Vector3 source = direction.asVector();
        return Direction3.from(TransformationOperatorBasis.resolve(transformation, builder)
                .applyToDirection(source.x(), source.y(), source.z()));
    }

    private static Vector3 transformVector3(
            Vector3 vector,
            com.minicad.step.model.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        return TransformationOperatorBasis.resolve(transformation, builder)
                .applyTo(vector.x(), vector.y(), vector.z());
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

        // Faces drawn by an assembly instance are emitted once per instance at
        // that instance's world placement; everything else keeps the single
        // local copy this exporter has always written.
        Map<Integer, List<double[]>> placementsByFace = assemblyPlacementsByFace(resolved);

        // Collect face entities for deterministic triangulation.
        List<com.minicad.step.model.StepFaceEntity> faceEntities = resolved.values().stream()
                .filter(e -> e instanceof com.minicad.step.model.StepFaceEntity)
                .map(e -> (com.minicad.step.model.StepFaceEntity) e)
                .collect(Collectors.toList());
        Triangulator t = new Triangulator();
        int faceFailures = 0;
        String firstFaceFailure = null;
        for (com.minicad.step.model.StepFaceEntity faceEntity : faceEntities) {
            List<double[]> placements = placementsByFace.get(faceEntity.id());
            int copies = placements == null ? 1 : placements.size();
            for (int copy = 0; copy < copies; copy++) {
                try {
                    t.setTransform(placements == null ? null : placements.get(copy));
                    t.triangulateSemanticFace(faceEntity, builder);
                } catch (Exception e) {
                    faceFailures++;
                    if (firstFaceFailure == null) {
                        firstFaceFailure = "#" + faceEntity.id() + ": " + e.getMessage();
                    }
                }
            }
        }
        t.setTransform(null);
        if (faceFailures > 0) {
            LOG.warn("Skipped {} face(s) during mesh export; first failure: {}", faceFailures, firstFaceFailure);
        }
        // Sequential triangulation of solids/shells (complex dependencies)
        int solidFailures = 0;
        String firstSolidFailure = null;
        int shellFailures = 0;
        String firstShellFailure = null;
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
                    solidFailures++;
                    if (firstSolidFailure == null) {
                        firstSolidFailure = "#" + id + ": " + e.getMessage();
                    }
                }
            } else if (ShellHelper.isShellCandidate(entity)) {
                try {
                    Shell shell = builder.buildShell(id);
                    t.triangulateShell(shell);
                } catch (Exception e) {
                    shellFailures++;
                    if (firstShellFailure == null) {
                        firstShellFailure = "#" + id + ": " + e.getMessage();
                    }
                }
            }
        }
        if (solidFailures > 0) {
            LOG.warn("Skipped {} solid(s) during mesh export; first failure: {}", solidFailures, firstSolidFailure);
        }
        if (shellFailures > 0) {
            LOG.warn("Skipped {} shell(s) during mesh export; first failure: {}", shellFailures, firstShellFailure);
        }
        return t.toMeshData();
    }
    private static boolean isSemanticFaceBackedEntity(StepEntity entity) {
        return entity instanceof com.minicad.step.model.StepFaceEntity
                || entity instanceof com.minicad.step.model.StepOpenShell
                || entity instanceof com.minicad.step.model.StepClosedShell
                || entity instanceof com.minicad.step.model.StepSurfacedOpenShell
                || entity instanceof com.minicad.step.model.StepOrientedOpenShell
                || entity instanceof com.minicad.step.model.StepOrientedClosedShell
                || entity instanceof com.minicad.step.model.StepConnectedFaceSet
                || entity instanceof com.minicad.step.model.StepConnectedFaceSubSet
                || entity instanceof com.minicad.step.model.StepFaceBasedSurfaceModel
                || entity instanceof com.minicad.step.model.StepManifoldSurfaceModel
                || entity instanceof com.minicad.step.model.StepShellBasedSurfaceModel
                || entity instanceof com.minicad.step.model.StepManifoldSolidBrep
                || entity instanceof com.minicad.step.model.StepAdvancedBrep
                || entity instanceof com.minicad.step.model.StepFacettedBrep
                || entity instanceof com.minicad.step.model.StepNonManifoldSolidBrep
                || entity instanceof com.minicad.step.model.StepBrepWithVoids;
    }

    /**
     * Maps each face to the world placements of the product-structure instances
     * that draw it. A lone root product is a plain part, not an assembly: its
     * representation holds the same geometry the local face pass already writes,
     * so no placements are returned and the output stays byte-identical.
     *
     * <p>Faces reachable from more than one node are emitted once per distinct
     * placement. Faces no representation covers are absent and keep the single
     * local copy.
     */
    private static Map<Integer, List<double[]>> assemblyPlacementsByFace(Map<Integer, StepEntity> resolved) {
        StepAssemblyGraphBuilder.AssemblyGraph graph;
        try {
            graph = StepAssemblyGraphBuilder.build(resolved, 1.0);
        } catch (RuntimeException e) {
            // A degenerate placement should not cost the whole export: without a
            // graph the local geometry is still written, exactly as before.
            LOG.warn("Assembly placements unavailable, exporting local geometry only: {}", e.getMessage());
            return Map.of();
        }
        if (graph.nodes().size() < 2) {
            return Map.of();
        }
        Map<Integer, List<double[]>> placementsByFace = new LinkedHashMap<>();
        for (StepAssemblyGraphBuilder.AssemblyNode node : graph.nodes()) {
            for (Integer representationId : node.representationIds()) {
                StepEntity representationEntity = resolved.get(representationId);
                if (!(representationEntity instanceof StepRepresentation)) {
                    continue;
                }
                for (Integer faceId : representationFaceIds((StepRepresentation) representationEntity, resolved)) {
                    addPlacement(placementsByFace, faceId, node.worldMatrix());
                }
            }
        }
        return placementsByFace;
    }

    private static void addPlacement(
            Map<Integer, List<double[]>> placementsByFace, int faceId, double[] placement) {
        List<double[]> placements = placementsByFace.computeIfAbsent(faceId, ignored -> new ArrayList<>());
        for (double[] existing : placements) {
            if (Arrays.equals(existing, placement)) {
                return;
            }
        }
        placements.add(placement);
    }

    private static Set<Integer> representationFaceIds(
            StepRepresentation representation, Map<Integer, StepEntity> resolved) {
        Set<Integer> faceIds = new LinkedHashSet<>();
        Set<Integer> visited = new LinkedHashSet<>();
        for (StepRepresentation linked : ShapeRepresentationHelper.linkedRepresentations(representation, resolved)) {
            for (StepEntity item : linked.items()) {
                collectRepresentationFaceIds(item, faceIds, visited);
            }
        }
        return faceIds;
    }

    /**
     * Collects the faces an assembly representation item draws, descending
     * through styled wrappers, solids, surface models and nested representations.
     * Items with no face-based geometry (placements, mapped items, CSG) fall
     * through, leaving their faces on the local pass.
     */
    private static void collectRepresentationFaceIds(
            StepEntity entity, Set<Integer> faceIds, Set<Integer> visited) {
        StepEntity item = entity;
        while (item instanceof StepStyledItem || item instanceof StepOverRidingStyledItem) {
            item = item instanceof StepStyledItem
                    ? ((StepStyledItem) item).item()
                    : ((StepOverRidingStyledItem) item).item();
        }
        if (item == null || !visited.add(item.id())) {
            return;
        }
        if (item instanceof StepFaceEntity) {
            faceIds.add(item.id());
            return;
        }
        if (item instanceof StepManifoldSolidBrep) {
            collectShellFaceIds(((StepManifoldSolidBrep) item).outer(), faceIds, visited);
            return;
        }
        if (item instanceof StepBrepWithVoids) {
            StepBrepWithVoids brep = (StepBrepWithVoids) item;
            collectShellFaceIds(brep.outer(), faceIds, visited);
            for (StepEntity voidShell : brep.voids()) {
                collectShellFaceIds(voidShell, faceIds, visited);
            }
            return;
        }
        if (item instanceof StepAdvancedBrep) {
            StepAdvancedBrep brep = (StepAdvancedBrep) item;
            collectShellFaceIds(brep.outer(), faceIds, visited);
            for (StepEntity voidShell : brep.voids()) {
                collectShellFaceIds(voidShell, faceIds, visited);
            }
            return;
        }
        if (item instanceof StepFacettedBrep) {
            collectShellFaceIds(((StepFacettedBrep) item).outer(), faceIds, visited);
            return;
        }
        if (item instanceof StepNonManifoldSolidBrep) {
            collectShellFaceIds(((StepNonManifoldSolidBrep) item).outer(), faceIds, visited);
            return;
        }
        if (item instanceof StepShellBasedSurfaceModel) {
            for (StepEntity shell : ((StepShellBasedSurfaceModel) item).shells()) {
                collectShellFaceIds(shell, faceIds, visited);
            }
            return;
        }
        if (item instanceof StepManifoldSurfaceModel) {
            for (StepEntity shell : ((StepManifoldSurfaceModel) item).shells()) {
                collectShellFaceIds(shell, faceIds, visited);
            }
            return;
        }
        if (item instanceof StepFaceBasedSurfaceModel) {
            for (StepEntity faceSet : ((StepFaceBasedSurfaceModel) item).faceSets()) {
                collectShellFaceIds(faceSet, faceIds, visited);
            }
            return;
        }
        if (item instanceof StepRepresentation) {
            for (StepEntity nestedItem : ((StepRepresentation) item).items()) {
                collectRepresentationFaceIds(nestedItem, faceIds, visited);
            }
            return;
        }
        if (item instanceof StepCompoundRepresentationItem) {
            for (StepEntity nestedItem : ((StepCompoundRepresentationItem) item).items()) {
                collectRepresentationFaceIds(nestedItem, faceIds, visited);
            }
            return;
        }
        collectShellFaceIds(item, faceIds, visited);
    }

    private static void collectShellFaceIds(
            StepEntity shellEntity, Set<Integer> faceIds, Set<Integer> visited) {
        if (shellEntity == null) {
            return;
        }
        boolean hasFaces = ShellHelper.isShellEntity(shellEntity)
                || shellEntity instanceof StepConnectedFaceSet
                || shellEntity instanceof StepConnectedFaceSubSet;
        if (!hasFaces) {
            return;
        }
        for (StepFaceEntity face : ShellHelper.shellFaces(shellEntity)) {
            collectRepresentationFaceIds(face, faceIds, visited);
        }
    }
    private static class Triangulator {
        private final Map<MeshVertex, Integer> vertexIndex = new LinkedHashMap<>();
        private final List<int[]> faceIndices = new ArrayList<>();
        // World placement of the assembly instance being triangulated, or null
        // for the plain local pass. Vertices and normals are placed before dedup
        // so two instances of one part get their own copies.
        private double[] placement;
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
        void setTransform(double[] placement) {
            this.placement = isIdentityPlacement(placement) ? null : placement;
        }
        private static boolean isIdentityPlacement(double[] placement) {
            if (placement == null) {
                return true;
            }
            double[] identity = StepAssemblyGraphBuilder.identityMatrix();
            for (int i = 0; i < identity.length; i++) {
                if (placement[i] != identity[i]) {
                    return false;
                }
            }
            return true;
        }
        private static CartesianPoint placePoint(CartesianPoint p, double[] placement) {
            return new CartesianPoint(
                    placement[0] * p.x() + placement[1] * p.y() + placement[2] * p.z() + placement[3],
                    placement[4] * p.x() + placement[5] * p.y() + placement[6] * p.z() + placement[7],
                    placement[8] * p.x() + placement[9] * p.y() + placement[10] * p.z() + placement[11]);
        }
        private static Vector3 placeNormal(Vector3 n, double[] placement) {
            Vector3 placed = new Vector3(
                    placement[0] * n.x() + placement[1] * n.y() + placement[2] * n.z(),
                    placement[4] * n.x() + placement[5] * n.y() + placement[6] * n.z(),
                    placement[8] * n.x() + placement[9] * n.y() + placement[10] * n.z());
            double length = placed.norm();
            return length < MIN_TRIANGLE_AREA ? placed : placed.normalize().asVector();
        }
        int addVertex(CartesianPoint p, Vector3 n) {
            MeshVertex key = placement == null
                    ? new MeshVertex(p, n)
                    : new MeshVertex(placePoint(p, placement), placeNormal(n, placement));
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
        void triangulateSemanticFace(com.minicad.step.model.StepFaceEntity stepFace, StepCadBuilder builder) {
            StepEntity faceGeometry = semanticFaceGeometry(stepFace);
            boolean flipped = !semanticFaceSameSense(stepFace);
            SurfaceGeometry surface = buildSemanticSurfaceGeometry(faceGeometry, builder);
            // The semantic surface is the one the STEP face was resolved against,
            // so try the parametric walk on it first. When buildSemanticSurfaceGeometry
            // returned nothing, the walk below runs once on the topology face's
            // surface instead; it must not be repeated on a surface it already
            // rejected, because the walk is expensive and deterministic.
            boolean parametricAttempted = false;
            if (surface != null && !(surface instanceof Plane)) {
                if (triangulateSemanticParametricFace(stepFace, faceGeometry, surface, builder, flipped)) {
                    return;
                }
                parametricAttempted = true;
            }
            Face builtFace = builder.buildFace(stepFace.id());
            if (surface == null) {
                surface = builtFace.surface();
            }
            if (surface instanceof Plane) {
            Plane plane = (Plane) surface;
                triangulatePlanarFace(builtFace, plane, flipped);
                return;
            }
            if (!parametricAttempted
                    && triangulateSemanticParametricFace(stepFace, faceGeometry, surface, builder, flipped)) {
                return;
            }
            if (MeshTriangulatorParametric.mapperFor(surface) == null || builtFace.bounds().isEmpty()) {
                // A face with trim bounds and a mapper was trimmed: if the walk
                // found no cells, the loops enclose no area (dead seam loops), and
                // gridding the whole surface would add the geometry the bounds
                // exclude. Without a mapper, or without bounds, the whole-surface
                // grid is the only representation the exporter has.
                triangulateCurvedFace(builtFace, surface, flipped);
            }
        }
        private void triangulatePlanarFace(Face face, Plane plane, boolean flipped) {
            int before = faceIndices.size();
            MeshTriangulatorPlanar.triangulatePlanarFace(face, plane, flipped,
                    this::addVertex, tri -> addTriangle(tri[0], tri[1], tri[2]));
            if (faceIndices.size() > before) {
                return;
            }
            // Ear clipping drops the whole face when no hole bridge is visible,
            // which happens whenever an inner loop is not fully nested inside the
            // outer loop. Cover those faces with the same containment walk the
            // parametric faces use instead of losing their area.
            MeshTriangulatorParametric.triangulatePlanarGridFallback(face, plane, flipped,
                    () -> faceIndices.size(),
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
                com.minicad.step.model.StepFaceEntity stepFace,
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
