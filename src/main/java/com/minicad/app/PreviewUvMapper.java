package com.minicad.app;

import com.minicad.common.Epsilon;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.*;
import com.minicad.geometry2d.*;
import com.minicad.step.model.annotation.StepAnnotationCurveOccurrence;
import com.minicad.step.model.annotation.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.annotation.StepLeaderCurve;
import com.minicad.step.model.annotation.StepTerminatorSymbol;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.base.StepFaceEntity;
import com.minicad.step.model.geometry.*;
import com.minicad.step.model.product.StepGeometricReplica;
import com.minicad.step.model.tolerance.StepDimensionCurve;
import com.minicad.step.model.topology.*;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.FaceBound;
import com.minicad.topology.OrientedEdge;
import com.minicad.topology.PolyLoop;
import com.minicad.topology.VertexLoop;
import com.minicad.step.semantic.StepCadBuilder;

import java.util.*;

/** UV mapping, parametric surface projection, and pcurve sampling.
 *  Extracted from StepPreviewJsonExporter to isolate UV mapping logic. */
public final class PreviewUvMapper {

    private PreviewUvMapper() {}

    // ─── ParametricSurfaceMapper interface ────────────────────────────────

    public interface ParametricSurfaceMapper {
        UvPoint project(CartesianPoint point, UvPoint previous);

        CartesianPoint pointAt(double u, double v);

        Vector3 normalAt(double u, double v);

        default Double uPeriod() {
            return null;
        }

        default Double vPeriod() {
            return null;
        }
    }

    // ─── mapperForSurface ─────────────────────────────────────────────────

    public static ParametricSurfaceMapper mapperForSurface(StepEntity geometry, StepCadBuilder builder) {
        if (geometry instanceof StepRectangularTrimmedSurface trimmedSurface) {
            return mapperForSurface(trimmedSurface.basisSurface(), builder);
        }
        if (geometry instanceof StepCurveBoundedSurface boundedSurface) {
            return mapperForSurface(boundedSurface.basisSurface(), builder);
        }
        if (geometry instanceof StepOrientedSurface orientedSurface) {
            ParametricSurfaceMapper base = mapperForSurface(orientedSurface.surfaceElement(), builder);
            if (base == null) {
                return null;
            }
            if (orientedSurface.orientation()) {
                return base;
            }
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    return base.project(point, previous);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return base.pointAt(u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return base.normalAt(u, v).scale(-1.0);
                }

                @Override
                public Double uPeriod() {
                    return base.uPeriod();
                }

                @Override
                public Double vPeriod() {
                    return base.vPeriod();
                }
            };
        }
        if (geometry instanceof StepOffsetSurface offsetSurface) {
            ParametricSurfaceMapper base = mapperForSurface(offsetSurface.basisSurface(), builder);
            if (base == null) {
                return null;
            }
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    return base.project(point, previous);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    CartesianPoint basePoint = base.pointAt(u, v);
                    Vector3 normal = base.normalAt(u, v);
                    return basePoint.add(normal.scale(offsetSurface.distance()));
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return base.normalAt(u, v);
                }

                @Override
                public Double uPeriod() {
                    return base.uPeriod();
                }

                @Override
                public Double vPeriod() {
                    return base.vPeriod();
                }
            };
        }
        // Elliptical-axis surfaces — CadBuilder approximates these as standard surfaces
        if (geometry instanceof StepCylindricalSurfaceWithEllipticalAxis ellipticalAxis) {
            CylindricalSurface surface = builder.buildCylindricalSurfaceWithEllipticalAxis(ellipticalAxis.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = unwrapPeriodic(cylindricalAngle(surface.position(), point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, axialHeight(surface.position(), point));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return surfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return cylindricalNormal(surface, u, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepConicalSurfaceWithEllipticalAxis ellipticalAxis) {
            ConicalSurface surface = builder.buildConicalSurfaceWithEllipticalAxis(ellipticalAxis.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = unwrapPeriodic(cylindricalAngle(surface.position(), point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, axialHeight(surface.position(), point));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return conicalSurfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return conicalNormal(surface, u, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepSphericalSurfaceWithEllipticalAxis ellipticalAxis) {
            SphericalSurface surface = builder.buildSphericalSurfaceWithEllipticalAxis(ellipticalAxis.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = unwrapPeriodic(sphericalU(surface.position(), point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, sphericalV(surface.position(), point, surface.radius()));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return sphericalSurfacePoint(surface.position(), surface.radius(), u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return sphericalNormal(surface.position(), u, v, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepToroidalSurfaceWithCylindricalAxis ellipticalAxis) {
            ToroidalSurface surface = builder.buildToroidalSurfaceWithCylindricalAxis(ellipticalAxis.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Double previousU = previous == null ? null : previous.u();
                    Double previousV = previous == null ? null : previous.v();
                    double u = unwrapPeriodic(toroidalU(surface, point), previousU, Math.PI * 2.0);
                    double v = unwrapPeriodic(toroidalV(surface, point), previousV, Math.PI * 2.0);
                    return new UvPoint(u, v);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return toroidalSurfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return toroidalNormal(surface, u, v, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }

                @Override
                public Double vPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepToroidalSurfaceWithEllipticalAxis ellipticalAxis) {
            ToroidalSurface surface = builder.buildToroidalSurfaceWithEllipticalAxis(ellipticalAxis.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Double previousU = previous == null ? null : previous.u();
                    Double previousV = previous == null ? null : previous.v();
                    double u = unwrapPeriodic(toroidalU(surface, point), previousU, Math.PI * 2.0);
                    double v = unwrapPeriodic(toroidalV(surface, point), previousV, Math.PI * 2.0);
                    return new UvPoint(u, v);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return toroidalSurfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return toroidalNormal(surface, u, v, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }

                @Override
                public Double vPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepOffsetSurface2 offsetSurface2) {
            ParametricSurfaceMapper base = mapperForSurface(offsetSurface2.basisSurface(), builder);
            if (base == null) {
                return null;
            }
            double dist = offsetSurface2.sameSense() ? offsetSurface2.distance() : -offsetSurface2.distance();
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    return base.project(point, previous);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    CartesianPoint basePoint = base.pointAt(u, v);
                    Vector3 normal = base.normalAt(u, v);
                    return basePoint.add(normal.scale(dist));
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return base.normalAt(u, v);
                }

                @Override
                public Double uPeriod() {
                    return base.uPeriod();
                }

                @Override
                public Double vPeriod() {
                    return base.vPeriod();
                }
            };
        }
        if (geometry instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName())) {
            if (!(replica.transformation() instanceof com.minicad.step.model.geometry.StepCartesianTransformationOperator transformation)) {
                return null;
            }
            ParametricSurfaceMapper base = mapperForSurface(replica.parent(), builder);
            if (base == null) {
                return null;
            }
            double[] matrix = matrixForTransformationOperator(transformation, builder);
            double[] inverse = inverseUniformScaleTransform(matrix);
            if (inverse == null) {
                return null;
            }
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    return base.project(transformCartesian(point, inverse), previous);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return transformCartesian(base.pointAt(u, v), matrix);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    VectorPayload transformed = transform(
                            new VectorPayload(base.normalAt(u, v).x(), base.normalAt(u, v).y(), base.normalAt(u, v).z()),
                            matrix
                    );
                    return new Vector3(transformed.x(), transformed.y(), transformed.z());
                }

                @Override
                public Double uPeriod() {
                    return base.uPeriod();
                }

                @Override
                public Double vPeriod() {
                    return base.vPeriod();
                }
            };
        }
        if (geometry instanceof StepPlane stepPlane) {
            Axis2Placement3D placement = builder.buildPlacement(stepPlane.position().id());
            Plane plane = builder.buildPlane(stepPlane.id());
            Direction3 uDirection = placement.xDirection();
            Direction3 vDirection = placement.yDirection();
            CartesianPoint origin = plane.origin();
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Vector3 offset = point.subtract(origin);
                    return new UvPoint(offset.dot(uDirection.asVector()), offset.dot(vDirection.asVector()));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return origin
                            .add(uDirection.asVector().scale(u))
                            .add(vDirection.asVector().scale(v));
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return plane.normal().asVector();
                }
            };
        }
        if (geometry instanceof StepCylindricalSurface cylindricalSurface) {
            CylindricalSurface surface = builder.buildCylindricalSurface(cylindricalSurface.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = unwrapPeriodic(cylindricalAngle(surface, point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, axialHeight(surface, point));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return surfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return cylindricalNormal(surface, u, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepConicalSurface conicalSurface) {
            ConicalSurface surface = builder.buildConicalSurface(conicalSurface.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = unwrapPeriodic(cylindricalAngle(surface.position(), point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, axialHeight(surface.position(), point));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return conicalSurfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return conicalNormal(surface, u, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepSphericalSurface sphericalSurface) {
            Axis2Placement3D placement = builder.buildPlacement(sphericalSurface.position().id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = unwrapPeriodic(sphericalU(placement, point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, sphericalV(placement, point, sphericalSurface.radius()));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return sphericalSurfacePoint(placement, sphericalSurface.radius(), u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return sphericalNormal(placement, u, v, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepDegenerateToroidalSurface degenerateToroidalSurface) {
            Axis2Placement3D placement = builder.buildPlacement(degenerateToroidalSurface.position().id());
            double majorRadius = degenerateToroidalSurface.majorRadius();
            double minorRadius = degenerateToroidalSurface.minorRadius();
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Double previousU = previous == null ? null : previous.u();
                    Double previousV = previous == null ? null : previous.v();
                    double u = unwrapPeriodic(toroidalU(placement, point), previousU, Math.PI * 2.0);
                    double v = unwrapPeriodic(toroidalV(placement, majorRadius, point), previousV, Math.PI * 2.0);
                    return new UvPoint(u, v);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return toroidalSurfacePoint(placement, majorRadius, minorRadius, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return toroidalNormal(placement, u, v, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }

                @Override
                public Double vPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepToroidalSurface toroidalSurface) {
            ToroidalSurface surface = builder.buildToroidalSurface(toroidalSurface.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Double previousU = previous == null ? null : previous.u();
                    Double previousV = previous == null ? null : previous.v();
                    double u = unwrapPeriodic(toroidalU(surface, point), previousU, Math.PI * 2.0);
                    double v = unwrapPeriodic(toroidalV(surface, point), previousV, Math.PI * 2.0);
                    return new UvPoint(u, v);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return toroidalSurfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return toroidalNormal(surface, u, v, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }

                @Override
                public Double vPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepRationalBSplineSurface splineSurface) {
            RationalBSplineSurface3 surface = builder.buildRationalBSplineSurface(splineSurface.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    return nearestUvOnRationalBSplineSurface(surface, point, previous);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return surface.pointAt(u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return surface.normalAt(u, v);
                }
            };
        }
        if (geometry instanceof StepBSplineSurfaceWithKnots
                || geometry instanceof StepBSplineSurface
                || geometry instanceof StepBSplineSurfaceWithKnotsAndBreakpoints
                || geometry instanceof StepBezierSurface
                || geometry instanceof StepUniformSurface
                || geometry instanceof StepQuasiUniformSurface
                || geometry instanceof StepPiecewiseBezierSurface) {
            BSplineSurface3 surface = buildBsplineSurface(geometry, builder);
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    return nearestUvOnBSplineSurface(surface, point, previous);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return surface.pointAt(u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return surface.normalAt(u, v);
                }
            };
        }
        if (geometry instanceof StepSurfaceOfLinearExtrusion extrusionSurface) {
            return extrusionMapper(extrusionSurface, builder);
        }
        if (geometry instanceof StepSurfaceOfRevolution revolutionSurface) {
            return revolutionMapper(revolutionSurface, builder);
        }
        // Rectangular composite surface: delegate to parent surface mapper
        if (geometry instanceof StepRectangularCompositeSurface compositeSurface) {
            return mapperForSurface(compositeSurface.parentSurface(), builder);
        }
        // Surface patch: delegate to basis surface mapper
        if (geometry instanceof StepSurfacePatch surfacePatch) {
            return mapperForSurface(surfacePatch.basisSurface(), builder);
        }
        // Blended surface: delegate to primary surface mapper
        if (geometry instanceof StepBlendedSurface blended) {
            return mapperForSurface(blended.primarySurface(), builder);
        }
        // Free-form surface: build as BSplineSurface3 and use grid-based parametric mapping
        if (geometry instanceof StepFreeFormSurface freeForm) {
            BSplineSurface3 surface = buildFreeFormSurface(freeForm, builder);
            double uSpan = surface.uEnd() - surface.uStart();
            double vSpan = surface.vEnd() - surface.vStart();
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = previous != null ? previous.u() : surface.uStart() + uSpan * 0.5;
                    double v = previous != null ? previous.v() : surface.vStart() + vSpan * 0.5;
                    return new UvPoint(u, v);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return surface.pointAt(u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return surface.normalAt(u, v);
                }
            };
        }
        return null;
    }

    // ─── extrusionMapper / revolutionMapper ───────────────────────────────

    public static ParametricSurfaceMapper extrusionMapper(
            StepSurfaceOfLinearExtrusion extrusionSurface,
            StepCadBuilder builder
    ) {
        PreviewCurveEvaluator.CurveEvaluator directrix = PreviewCurveEvaluator.curveEvaluator(extrusionSurface.sweptCurve(), builder);
        if (directrix == null) {
            return null;
        }
        Vector3 extrusionDirection = builder.buildVector(extrusionSurface.extrusionAxis().id()).normalize().asVector();
        return new ParametricSurfaceMapper() {
            @Override
            public UvPoint project(CartesianPoint point, UvPoint previous) {
                Vector3 offset = point.subtract(directrix.pointAt(directrix.start()));
                double v = offset.dot(extrusionDirection);
                CartesianPoint basePoint = point.add(extrusionDirection.scale(-v));
                double u = PreviewCurveEvaluator.closestParameter(directrix, basePoint, previous == null ? null : previous.u());
                return new UvPoint(u, v);
            }

            @Override
            public CartesianPoint pointAt(double u, double v) {
                return directrix.pointAt(u).add(extrusionDirection.scale(v));
            }

            @Override
            public Vector3 normalAt(double u, double v) {
                Vector3 tangent = directrix.tangentAt(u);
                Vector3 normal = tangent.cross(extrusionDirection);
                if (normal.norm() <= Epsilon.EPS) {
                    normal = fallbackNormal(extrusionDirection);
                }
                return normal.normalize().asVector();
            }
        };
    }

    public static ParametricSurfaceMapper revolutionMapper(
            StepSurfaceOfRevolution revolutionSurface,
            StepCadBuilder builder
    ) {
        PreviewCurveEvaluator.CurveEvaluator directrix = PreviewCurveEvaluator.curveEvaluator(revolutionSurface.sweptCurve(), builder);
        if (directrix == null) {
            return null;
        }
        StepCadBuilder.Axis1Placement axisPlacement = builder.buildAxis1Placement(revolutionSurface.axisPosition().id());
        Direction3 axisDirection = axisPlacement.axis();
        CartesianPoint axisOrigin = axisPlacement.location();
        Direction3 radialReference = revolutionReferenceDirection(directrix, axisOrigin, axisDirection);
        Direction3 tangentialReference = Direction3.from(axisDirection.asVector().cross(radialReference.asVector()));
        return new ParametricSurfaceMapper() {
            @Override
            public UvPoint project(CartesianPoint point, UvPoint previous) {
                Vector3 offset = point.subtract(axisOrigin);
                double v = unwrapPeriodic(
                        Math.atan2(offset.dot(tangentialReference.asVector()), offset.dot(radialReference.asVector())),
                        previous == null ? null : previous.v(),
                        Math.PI * 2.0
                );
                CartesianPoint meridianPoint = toRevolutionMeridianPoint(point, axisOrigin, axisDirection, radialReference);
                double u = PreviewCurveEvaluator.closestParameter(directrix, meridianPoint, previous == null ? null : previous.u());
                return new UvPoint(u, v);
            }

            @Override
            public CartesianPoint pointAt(double u, double v) {
                return revolveAroundAxis(directrix.pointAt(u), axisOrigin, axisDirection, radialReference, tangentialReference, v);
            }

            @Override
            public Vector3 normalAt(double u, double v) {
                Vector3 tangentU = tangentAlongRevolutionDirectrix(
                        directrix,
                        axisOrigin,
                        axisDirection,
                        radialReference,
                        tangentialReference,
                        u,
                        v
                );
                Vector3 tangentV = tangentAroundRevolution(
                        axisOrigin,
                        axisDirection,
                        radialReference,
                        tangentialReference,
                        directrix.pointAt(u),
                        v
                );
                Vector3 normal = tangentU.cross(tangentV);
                if (normal.norm() <= Epsilon.EPS) {
                    normal = fallbackNormal(axisDirection.asVector());
                }
                return normal.normalize().asVector();
            }

            @Override
            public Double vPeriod() {
                return Math.PI * 2.0;
            }
        };
    }

    // ─── nearestUvOnBSplineSurface / nearestUvOnRationalBSplineSurface ────

    public static UvPoint nearestUvOnBSplineSurface(BSplineSurface3 surface, CartesianPoint point, UvPoint previous) {
        double uStart = surface.uStart();
        double uEnd = surface.uEnd();
        double vStart = surface.vStart();
        double vEnd = surface.vEnd();
        boolean hasPrevious = previous != null;

        double bestU = hasPrevious ? clamp(previous.u(), uStart, uEnd) : uStart;
        double bestV = hasPrevious ? clamp(previous.v(), vStart, vEnd) : vStart;
        double bestDistance = surface.pointAt(bestU, bestV).distanceTo(point);

        int uSamples = hasPrevious ? 4 : 12;
        int vSamples = hasPrevious ? 4 : 12;
        double coarseWindowU = (uEnd - uStart) * (hasPrevious ? 0.08 : 0.25);
        double coarseWindowV = (vEnd - vStart) * (hasPrevious ? 0.08 : 0.25);
        double coarseMinU = hasPrevious ? Math.max(uStart, bestU - coarseWindowU) : uStart;
        double coarseMaxU = hasPrevious ? Math.min(uEnd, bestU + coarseWindowU) : uEnd;
        double coarseMinV = hasPrevious ? Math.max(vStart, bestV - coarseWindowV) : vStart;
        double coarseMaxV = hasPrevious ? Math.min(vEnd, bestV + coarseWindowV) : vEnd;

        for (int ui = 0; ui <= uSamples; ui++) {
            double u = coarseMinU + (coarseMaxU - coarseMinU) * ui / (double) uSamples;
            for (int vi = 0; vi <= vSamples; vi++) {
                double v = coarseMinV + (coarseMaxV - coarseMinV) * vi / (double) vSamples;
                double distance = surface.pointAt(u, v).distanceTo(point);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestU = u;
                    bestV = v;
                }
            }
        }

        double windowU = Math.max((uEnd - uStart) * (hasPrevious ? 0.03 : 0.08), 1.0e-5);
        double windowV = Math.max((vEnd - vStart) * (hasPrevious ? 0.03 : 0.08), 1.0e-5);
        int refinements = hasPrevious ? 3 : 4;
        int refinementSamples = hasPrevious ? 4 : 6;
        for (int refinement = 0; refinement < refinements; refinement++) {
            double minU = Math.max(uStart, bestU - windowU);
            double maxU = Math.min(uEnd, bestU + windowU);
            double minV = Math.max(vStart, bestV - windowV);
            double maxV = Math.min(vEnd, bestV + windowV);
            for (int ui = 0; ui <= refinementSamples; ui++) {
                double u = minU + (maxU - minU) * ui / (double) refinementSamples;
                for (int vi = 0; vi <= refinementSamples; vi++) {
                    double v = minV + (maxV - minV) * vi / (double) refinementSamples;
                    double distance = surface.pointAt(u, v).distanceTo(point);
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestU = u;
                        bestV = v;
                    }
                }
            }
            if (bestDistance <= 1.0e-6) {
                break;
            }
            windowU *= 0.5;
            windowV *= 0.5;
        }
        return new UvPoint(bestU, bestV);
    }

    public static UvPoint nearestUvOnRationalBSplineSurface(
            RationalBSplineSurface3 surface,
            CartesianPoint point,
            UvPoint previous
    ) {
        double uStart = surface.uStart();
        double uEnd = surface.uEnd();
        double vStart = surface.vStart();
        double vEnd = surface.vEnd();
        boolean hasPrevious = previous != null;

        double bestU = hasPrevious ? clamp(previous.u(), uStart, uEnd) : uStart;
        double bestV = hasPrevious ? clamp(previous.v(), vStart, vEnd) : vStart;
        double bestDistance = surface.pointAt(bestU, bestV).distanceTo(point);

        int uSamples = hasPrevious ? 4 : 12;
        int vSamples = hasPrevious ? 4 : 12;
        double coarseWindowU = (uEnd - uStart) * (hasPrevious ? 0.08 : 0.25);
        double coarseWindowV = (vEnd - vStart) * (hasPrevious ? 0.08 : 0.25);
        double coarseMinU = hasPrevious ? Math.max(uStart, bestU - coarseWindowU) : uStart;
        double coarseMaxU = hasPrevious ? Math.min(uEnd, bestU + coarseWindowU) : uEnd;
        double coarseMinV = hasPrevious ? Math.max(vStart, bestV - coarseWindowV) : vStart;
        double coarseMaxV = hasPrevious ? Math.min(vEnd, bestV + coarseWindowV) : vEnd;

        for (int i = 0; i <= uSamples; i++) {
            double u = coarseMinU + (coarseMaxU - coarseMinU) * i / Math.max(uSamples, 1);
            for (int j = 0; j <= vSamples; j++) {
                double v = coarseMinV + (coarseMaxV - coarseMinV) * j / Math.max(vSamples, 1);
                double distance = surface.pointAt(u, v).distanceTo(point);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestU = u;
                    bestV = v;
                }
            }
        }

        for (int iteration = 0; iteration < 4; iteration++) {
            double stepU = (uEnd - uStart) / Math.pow(4.0, iteration + 2);
            double stepV = (vEnd - vStart) / Math.pow(4.0, iteration + 2);
            for (int du = -1; du <= 1; du++) {
                for (int dv = -1; dv <= 1; dv++) {
                    double u = clamp(bestU + du * stepU, uStart, uEnd);
                    double v = clamp(bestV + dv * stepV, vStart, vEnd);
                    double distance = surface.pointAt(u, v).distanceTo(point);
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestU = u;
                        bestV = v;
                    }
                }
            }
        }
        return new UvPoint(bestU, bestV);
    }

    // ─── inverseUniformScaleTransform ─────────────────────────────────────

    public static double[] inverseUniformScaleTransform(double[] matrix) {
        double sx = Math.sqrt(matrix[0] * matrix[0] + matrix[4] * matrix[4] + matrix[8] * matrix[8]);
        double sy = Math.sqrt(matrix[1] * matrix[1] + matrix[5] * matrix[5] + matrix[9] * matrix[9]);
        double sz = Math.sqrt(matrix[2] * matrix[2] + matrix[6] * matrix[6] + matrix[10] * matrix[10]);
        if (sx <= 1.0e-12 || sy <= 1.0e-12 || sz <= 1.0e-12) {
            return null;
        }
        double maxScale = Math.max(sx, Math.max(sy, sz));
        double tolerance = maxScale * 1.0e-6;
        if (Math.abs(sx - sy) > tolerance || Math.abs(sx - sz) > tolerance || Math.abs(sy - sz) > tolerance) {
            return null;
        }
        double n01 = ((matrix[0] / sx) * (matrix[1] / sy)) + ((matrix[4] / sx) * (matrix[5] / sy)) + ((matrix[8] / sx) * (matrix[9] / sy));
        double n02 = ((matrix[0] / sx) * (matrix[2] / sz)) + ((matrix[4] / sx) * (matrix[6] / sz)) + ((matrix[8] / sx) * (matrix[10] / sz));
        double n12 = ((matrix[1] / sy) * (matrix[2] / sz)) + ((matrix[5] / sy) * (matrix[6] / sz)) + ((matrix[9] / sy) * (matrix[10] / sz));
        if (Math.abs(n01) > 1.0e-6 || Math.abs(n02) > 1.0e-6 || Math.abs(n12) > 1.0e-6) {
            return null;
        }
        double scale = (sx + sy + sz) / 3.0;
        double scaleSquared = scale * scale;
        if (scaleSquared <= 1.0e-18) {
            return null;
        }
        double tx = matrix[3];
        double ty = matrix[7];
        double tz = matrix[11];
        return new double[]{
                matrix[0] / scaleSquared, matrix[4] / scaleSquared, matrix[8] / scaleSquared,
                -((matrix[0] * tx) + (matrix[4] * ty) + (matrix[8] * tz)) / scaleSquared,
                matrix[1] / scaleSquared, matrix[5] / scaleSquared, matrix[9] / scaleSquared,
                -((matrix[1] * tx) + (matrix[5] * ty) + (matrix[9] * tz)) / scaleSquared,
                matrix[2] / scaleSquared, matrix[6] / scaleSquared, matrix[10] / scaleSquared,
                -((matrix[2] * tx) + (matrix[6] * ty) + (matrix[10] * tz)) / scaleSquared,
                0.0, 0.0, 0.0, 1.0
        };
    }

    // ─── clamp ────────────────────────────────────────────────────────────

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    // ─── buildParametricLoops ─────────────────────────────────────────────

    public static List<ParametricLoopPayload> buildParametricLoops(List<FaceBound> bounds, ParametricSurfaceMapper mapper) {
        List<ParametricLoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            if (bound.loop() instanceof VertexLoop) {
                return List.of();
            }
            List<CartesianPoint> points3d = sampleLoop(bound);
            if (points3d.size() < 4) {
                return List.of();
            }
            List<UvPoint> uvPoints = new ArrayList<>(points3d.size());
            UvPoint previous = null;
            for (CartesianPoint point : points3d) {
                UvPoint uv = mapper.project(point, previous);
                if (uv == null) {
                    return List.of();
                }
                uvPoints.add(uv);
                previous = uv;
            }
            uvPoints = normalizePeriodicLoop(uvPoints, mapper);
            uvPoints.set(0, uvPoints.getFirst());
            uvPoints.set(uvPoints.size() - 1, uvPoints.getFirst());
            loops.add(new ParametricLoopPayload(bound.outer(), List.copyOf(uvPoints)));
        }
        return List.copyOf(loops);
    }

    public static List<ParametricLoopPayload> buildParametricLoops(
            StepFaceEntity stepFace,
            StepEntity geometry,
            ParametricSurfaceMapper mapper,
            StepCadBuilder builder
    ) {
        List<ParametricLoopPayload> loops = new ArrayList<>();
        boolean promoteSingleOuter = stepFace.bounds().size() == 1
                && stepFace.bounds().stream().noneMatch(com.minicad.step.model.topology.StepFaceBound::outer);
        for (com.minicad.step.model.topology.StepFaceBound bound : stepFace.bounds()) {
            if (!(bound.loop() instanceof com.minicad.step.model.topology.StepEdgeLoop edgeLoop)) {
                return List.of();
            }
            List<UvPoint> loopPoints = new ArrayList<>();
            boolean firstEdge = true;
            for (com.minicad.step.model.topology.StepOrientedEdge orientedEdge : edgeLoop.edges()) {
                List<UvPoint> edgePoints = sampleParametricOrientedEdge(orientedEdge, geometry, mapper, builder);
                if (edgePoints == null || edgePoints.size() < 2) {
                    return List.of();
                }
                int startIndex = firstEdge ? 0 : 1;
                for (int index = startIndex; index < edgePoints.size(); index++) {
                    loopPoints.add(edgePoints.get(index));
                }
                firstEdge = false;
            }
            if (loopPoints.size() < 4) {
                return List.of();
            }
            if (!bound.orientation()) {
                loopPoints = reverseClosedLoop(loopPoints);
            }
            loopPoints = normalizePeriodicLoop(loopPoints, mapper);
            if (!sameUv(loopPoints.getFirst(), loopPoints.getLast())) {
                loopPoints.add(loopPoints.getFirst());
            }
            loops.add(new ParametricLoopPayload(bound.outer() || promoteSingleOuter, List.copyOf(loopPoints)));
        }
        return List.copyOf(loops);
    }

    // ─── normalizePeriodicLoop ────────────────────────────────────────────

    public static List<UvPoint> normalizePeriodicLoop(List<UvPoint> points, ParametricSurfaceMapper mapper) {
        if (points.size() < 2) {
            return points;
        }
        Double uPeriod = mapper.uPeriod();
        Double vPeriod = mapper.vPeriod();
        List<UvPoint> normalized = new ArrayList<>(points.size());
        UvPoint previous = null;
        for (UvPoint point : points) {
            double u = point.u();
            double v = point.v();
            if (previous != null) {
                if (uPeriod != null) {
                    u = unwrapPeriodic(u, previous.u(), uPeriod);
                }
                if (vPeriod != null) {
                    v = unwrapPeriodic(v, previous.v(), vPeriod);
                }
            }
            UvPoint normalizedPoint = new UvPoint(u, v);
            normalized.add(normalizedPoint);
            previous = normalizedPoint;
        }
        if (normalized.size() >= 2) {
            UvPoint first = normalized.getFirst();
            UvPoint last = normalized.getLast();
            double u = last.u();
            double v = last.v();
            if (uPeriod != null) {
                u = unwrapPeriodic(u, first.u(), uPeriod);
            }
            if (vPeriod != null) {
                v = unwrapPeriodic(v, first.v(), vPeriod);
            }
            normalized.set(normalized.size() - 1, new UvPoint(u, v));
        }
        return normalized;
    }

    // ─── boundsOf ─────────────────────────────────────────────────────────

    public static UvBounds boundsOf(List<ParametricLoopPayload> loops) {
        double minU = Double.POSITIVE_INFINITY;
        double minV = Double.POSITIVE_INFINITY;
        double maxU = Double.NEGATIVE_INFINITY;
        double maxV = Double.NEGATIVE_INFINITY;
        for (ParametricLoopPayload loop : loops) {
            for (UvPoint point : loop.points()) {
                minU = Math.min(minU, point.u());
                minV = Math.min(minV, point.v());
                maxU = Math.max(maxU, point.u());
                maxV = Math.max(maxV, point.v());
            }
        }
        if (!Double.isFinite(minU) || !Double.isFinite(minV) || !Double.isFinite(maxU) || !Double.isFinite(maxV)) {
            return null;
        }
        return new UvBounds(minU, minV, maxU, maxV);
    }

    // ─── withSurfaceSourceMetadata ────────────────────────────────────────

    public static FaceSurfacePayload withSurfaceSourceMetadata(FaceSurfacePayload base, StepEntity geometry) {
        if (base == null || geometry == null) {
            return base;
        }
        String basisType = null;
        Integer basisStepId = null;
        Boolean orientation = null;
        Double offsetDistance = null;
        Double trimU1 = null;
        Double trimU2 = null;
        Double trimV1 = null;
        Double trimV2 = null;
        Boolean implicitOuter = null;
        Double transformScale = null;

        if (geometry instanceof StepRectangularTrimmedSurface trimmedSurface) {
            basisType = StepPreviewJsonExporter.surfaceTypeName(trimmedSurface.basisSurface());
            basisStepId = trimmedSurface.basisSurface().id();
            trimU1 = trimmedSurface.u1();
            trimU2 = trimmedSurface.u2();
            trimV1 = trimmedSurface.v1();
            trimV2 = trimmedSurface.v2();
        } else if (geometry instanceof StepCurveBoundedSurface boundedSurface) {
            basisType = StepPreviewJsonExporter.surfaceTypeName(boundedSurface.basisSurface());
            basisStepId = boundedSurface.basisSurface().id();
            implicitOuter = boundedSurface.implicitOuter();
        } else if (geometry instanceof StepOrientedSurface orientedSurface) {
            basisType = StepPreviewJsonExporter.surfaceTypeName(orientedSurface.surfaceElement());
            basisStepId = orientedSurface.surfaceElement().id();
            orientation = orientedSurface.orientation();
        } else if (geometry instanceof StepOffsetSurface offsetSurface) {
            basisType = StepPreviewJsonExporter.surfaceTypeName(offsetSurface.basisSurface());
            basisStepId = offsetSurface.basisSurface().id();
            offsetDistance = offsetSurface.distance();
        } else if (geometry instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName())) {
            basisType = StepPreviewJsonExporter.surfaceTypeName(replica.parent());
            basisStepId = replica.parent().id();
            transformScale = replica.transformation().scale();
        }

        return new FaceSurfacePayload(
                base.type(),
                base.center(),
                base.axis(),
                base.xDirection(),
                base.radius(),
                base.minorRadius(),
                base.semiAngle(),
                base.lowerHeight(),
                base.upperHeight(),
                base.startAngle(),
                base.sweepAngle(),
                base.uDegree(),
                base.vDegree(),
                base.controlPoints(),
                base.uMultiplicities(),
                base.vMultiplicities(),
                base.uKnots(),
                base.vKnots(),
                StepPreviewJsonExporter.surfaceTypeName(geometry),
                geometry.id(),
                basisType,
                basisStepId,
                orientation,
                offsetDistance,
                trimU1,
                trimU2,
                trimV1,
                trimV2,
                implicitOuter,
                transformScale
        );
    }

    // ─── basisDirectionForNormal ──────────────────────────────────────────

    public static List<Double> basisDirectionForNormal(Direction3 normal) {
        Vector3 axis = normal.asVector();
        Vector3 reference = Math.abs(axis.x()) < 0.9
                ? new Vector3(1.0, 0.0, 0.0)
                : new Vector3(0.0, 1.0, 0.0);
        Direction3 xDirection = reference.subtract(axis.scale(reference.dot(axis))).normalize();
        return List.of(xDirection.x(), xDirection.y(), xDirection.z());
    }

    // ─── sampleParametricOrientedEdge ─────────────────────────────────────

    private static List<UvPoint> sampleParametricOrientedEdge(
            com.minicad.step.model.topology.StepOrientedEdge orientedEdge,
            StepEntity faceGeometry,
            ParametricSurfaceMapper mapper,
            StepCadBuilder builder
    ) {
        StepVertexPoint startVertex = orientedEdge.orientation()
                ? orientedEdge.edgeElement().start()
                : orientedEdge.edgeElement().end();
        StepVertexPoint endVertex = orientedEdge.orientation()
                ? orientedEdge.edgeElement().end()
                : orientedEdge.edgeElement().start();
        StepEntity edgeGeometry = orientedEdge.edgeElement().edgeGeometry();
        StepEntity associatedSource = unwrapAssociatedCurveGeometry(edgeGeometry);
        List<StepEntity> pcurves = switch (associatedSource) {
            case StepSurfaceCurve surfaceCurve -> matchingPcurves(surfaceCurve.associatedGeometry(), faceGeometry);
            case StepSeamCurve seamCurve -> matchingPcurves(seamCurve.associatedGeometry(), faceGeometry);
            default -> List.of();
        };
        if (pcurves.isEmpty()) {
            if (shouldFallbackToProjectedEdge(edgeGeometry)) {
                List<UvPoint> fallback = projectSampledEdge(orientedEdge, mapper, builder);
                if (fallback != null) {
                    return fallback;
                }
            }
            return null;
        }
        UvPoint projectedStart = mapper.project(pointFromStep(startVertex.point()), null);
        UvPoint projectedEnd = mapper.project(pointFromStep(endVertex.point()), projectedStart);
        List<UvPoint> best = null;
        double bestScore = Double.POSITIVE_INFINITY;
        int unsupportedPcurveCount = 0;
        for (StepEntity pcurve : pcurves) {
            Object built;
            try {
                built = builder.buildPcurve2(pcurve.id());
            } catch (UnsupportedGeometryException ex) {
                unsupportedPcurveCount++;
                continue;
            }
            if (built instanceof Line2 line) {
                UvPoint start = snapToLine(projectedStart, line);
                UvPoint end = snapToLine(projectedEnd, line);
                double score = distanceSquared(projectedStart, start) + distanceSquared(projectedEnd, end);
                List<UvPoint> samples = sampleLinePcurve(line, start, end);
                if (best == null || score < bestScore) {
                    best = samples;
                    bestScore = score;
                }
                continue;
            }
            if (built instanceof BSplineCurve2 spline) {
                List<UvPoint> samples = sampleSplinePcurve(spline, projectedStart, projectedEnd);
                if (!samples.isEmpty()) {
                    double score = distanceSquared(projectedStart, samples.getFirst()) + distanceSquared(projectedEnd, samples.getLast());
                    if (best == null || score < bestScore) {
                        best = samples;
                        bestScore = score;
                    }
                }
                continue;
            }
            if (built instanceof Circle2 circle) {
                UvPoint start = snapToCircle(projectedStart, circle);
                UvPoint end = snapToCircle(projectedEnd, circle);
                double score = distanceSquared(projectedStart, start) + distanceSquared(projectedEnd, end);
                List<UvPoint> samples = sampleCirclePcurve(circle, start, end);
                if (!samples.isEmpty() && (best == null || score < bestScore)) {
                    best = samples;
                    bestScore = score;
                }
                continue;
            }
            if (built instanceof Ellipse2 ellipse) {
                UvPoint start = snapToEllipse(projectedStart, ellipse);
                UvPoint end = snapToEllipse(projectedEnd, ellipse);
                double score = distanceSquared(projectedStart, start) + distanceSquared(projectedEnd, end);
                List<UvPoint> samples = sampleEllipsePcurve(ellipse, start, end);
                if (!samples.isEmpty() && (best == null || score < bestScore)) {
                    best = samples;
                    bestScore = score;
                }
                continue;
            }
            if (built instanceof TrimmedCurve2 trimmed) {
                List<UvPoint> samples = sampleTrimmedPcurve(trimmed, projectedStart, projectedEnd);
                if (!samples.isEmpty()) {
                    double score = distanceSquared(projectedStart, samples.getFirst()) + distanceSquared(projectedEnd, samples.getLast());
                    if (best == null || score < bestScore) {
                        best = samples;
                        bestScore = score;
                    }
                }
            }
        }
        if (best == null) {
            List<UvPoint> fallback = projectSampledEdge(orientedEdge, mapper, builder);
            if (fallback != null) {
                return fallback;
            }
        }
        return best;
    }

    // ─── projectSampledEdge ───────────────────────────────────────────────

    private static List<UvPoint> projectSampledEdge(
            com.minicad.step.model.topology.StepOrientedEdge orientedEdge,
            ParametricSurfaceMapper mapper,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> sampled = sampleStepOrientedEdge(orientedEdge, builder);
        if (sampled.size() < 2) {
            return null;
        }
        List<UvPoint> points = new ArrayList<>(sampled.size());
        UvPoint previous = null;
        for (CartesianPoint point : sampled) {
            UvPoint uv = mapper.project(point, previous);
            if (uv == null) {
                return null;
            }
            points.add(uv);
            previous = uv;
        }
        return List.copyOf(points);
    }

    // ─── sampleStepOrientedEdge ───────────────────────────────────────────

    private static List<CartesianPoint> sampleStepOrientedEdge(
            com.minicad.step.model.topology.StepOrientedEdge orientedEdge,
            StepCadBuilder builder
    ) {
        com.minicad.step.model.topology.StepEdgeCurve edge = orientedEdge.edgeElement();
        CartesianPoint start = pointFromStep(orientedEdge.orientation() ? edge.start().point() : edge.end().point());
        CartesianPoint end = pointFromStep(orientedEdge.orientation() ? edge.end().point() : edge.start().point());
        boolean naturalForward = orientedEdge.orientation() ? edge.sameSense() : !edge.sameSense();
        Curve3 curve = curveForLooseEdge(edge.edgeGeometry(), builder);
        if (curve == null) {
            return List.of();
        }
        try {
            return sampleEdge(start, end, curve, naturalForward);
        } catch (com.minicad.common.GeometryException ex) {
            return List.of(start, end);
        }
    }

    // ─── shouldFallbackToProjectedEdge / associatedGeometrySummary ────────

    public static boolean shouldFallbackToProjectedEdge(StepEntity edgeGeometry) {
        return switch (unwrapAssociatedCurveGeometry(edgeGeometry)) {
            case StepSurfaceCurve surfaceCurve -> surfaceCurve.associatedGeometry().isEmpty();
            case StepSeamCurve seamCurve -> seamCurve.associatedGeometry().isEmpty();
            default -> true;
        };
    }

    public static String associatedGeometrySummary(StepEntity edgeGeometry) {
        List<StepEntity> associated = switch (unwrapAssociatedCurveGeometry(edgeGeometry)) {
            case StepSurfaceCurve surfaceCurve -> surfaceCurve.associatedGeometry();
            case StepSeamCurve seamCurve -> seamCurve.associatedGeometry();
            default -> List.of();
        };
        if (associated.isEmpty()) {
            return "[]";
        }
        return associated.stream()
                .map(entity -> StepPreviewJsonExporter.surfaceTypeName(entity) + "#" + entity.id())
                .collect(java.util.stream.Collectors.joining("|"));
    }

    // ─── unwrapAssociatedCurveGeometry ────────────────────────────────────

    public static StepEntity unwrapAssociatedCurveGeometry(StepEntity edgeGeometry) {
        StepEntity current = edgeGeometry;
        for (int depth = 0; depth < 16; depth++) {
            if (current instanceof StepOrientedCurve orientedCurve) {
                current = orientedCurve.curveElement();
                continue;
            }
            if (current instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName())) {
                current = replica.parent();
                continue;
            }
            if (current instanceof StepAnnotationCurveOccurrence occurrence) {
                current = occurrence.item();
                continue;
            }
            if (current instanceof StepDimensionCurve dimensionCurve) {
                current = dimensionCurve.item();
                continue;
            }
            if (current instanceof StepLeaderCurve leaderCurve) {
                current = leaderCurve.item();
                continue;
            }
            if (current instanceof StepProjectionCurve projectionCurve) {
                current = projectionCurve.item();
                continue;
            }
            if (current instanceof StepDraughtingAnnotationOccurrence annotationOccurrence) {
                current = annotationOccurrence.item();
                continue;
            }
            if (current instanceof StepTerminatorSymbol terminatorSymbol) {
                current = terminatorSymbol.annotatedCurve();
                continue;
            }
            return current;
        }
        return current;
    }

    // ─── pcurveBasisSurfaceSummary ────────────────────────────────────────

    public static String pcurveBasisSurfaceSummary(List<StepEntity> pcurves) {
        return pcurves.stream()
                .map(pcurve -> {
                    if (pcurve instanceof StepPcurve exact) {
                        return "#" + exact.id() + "->#" + exact.basisSurface().id();
                    }
                    if (pcurve instanceof StepDegeneratePcurve degenerate) {
                        return "#" + degenerate.id() + "->#" + degenerate.basisSurface().id();
                    }
                    return "#" + pcurve.id();
                })
                .collect(java.util.stream.Collectors.joining("|"));
    }

    // ─── matchingPcurves ──────────────────────────────────────────────────

    public static List<StepEntity> matchingPcurves(List<StepEntity> associatedGeometry, StepEntity faceGeometry) {
        Set<Integer> acceptableSurfaceIds = acceptablePcurveBasisSurfaceIds(faceGeometry);
        List<StepEntity> matches = new ArrayList<>();
        for (StepEntity associated : associatedGeometry) {
            if (associated instanceof StepPcurve pcurve && acceptableSurfaceIds.contains(pcurve.basisSurface().id())) {
                matches.add(pcurve);
            } else if (associated instanceof StepDegeneratePcurve pcurve && acceptableSurfaceIds.contains(pcurve.basisSurface().id())) {
                matches.add(pcurve);
            }
        }
        return List.copyOf(matches);
    }

    // ─── acceptablePcurveBasisSurfaceIds ──────────────────────────────────

    public static Set<Integer> acceptablePcurveBasisSurfaceIds(StepEntity faceGeometry) {
        LinkedHashSet<Integer> ids = new LinkedHashSet<>();
        StepEntity current = faceGeometry;
        for (int depth = 0; depth < 16 && current != null; depth++) {
            ids.add(current.id());
            if (current instanceof StepRectangularTrimmedSurface trimmedSurface) {
                current = trimmedSurface.basisSurface();
                continue;
            }
            if (current instanceof StepCurveBoundedSurface boundedSurface) {
                current = boundedSurface.basisSurface();
                continue;
            }
            if (current instanceof StepOrientedSurface orientedSurface) {
                current = orientedSurface.surfaceElement();
                continue;
            }
            if (current instanceof StepOffsetSurface offsetSurface) {
                current = offsetSurface.basisSurface();
                continue;
            }
            if (current instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName())) {
                current = replica.parent();
                continue;
            }
            break;
        }
        return Set.copyOf(ids);
    }

    // ─── pcurve snap / sample helpers ─────────────────────────────────────

    public static UvPoint snapToLine(UvPoint point, Line2 line) {
        com.minicad.geometry2d.Point2 snapped = line.closestPoint(new com.minicad.geometry2d.Point2(point.u(), point.v()));
        return new UvPoint(snapped.x(), snapped.y());
    }

    public static UvPoint snapToCircle(UvPoint point, Circle2 circle) {
        com.minicad.geometry2d.Vector2 offset = new com.minicad.geometry2d.Point2(point.u(), point.v()).subtract(circle.center());
        double norm = offset.norm();
        if (norm <= Epsilon.EPS) {
            com.minicad.geometry2d.Point2 fallback = circle.pointAt(0.0);
            return new UvPoint(fallback.x(), fallback.y());
        }
        com.minicad.geometry2d.Point2 snapped = circle.center().add(offset.scale(circle.radius() / norm));
        return new UvPoint(snapped.x(), snapped.y());
    }

    public static UvPoint snapToEllipse(UvPoint point, Ellipse2 ellipse) {
        double angle = ellipse.angleOf(ellipse.pointAt(ellipse.angleOf(snapEllipseSeed(point, ellipse))));
        com.minicad.geometry2d.Point2 snapped = ellipse.pointAt(angle);
        return new UvPoint(snapped.x(), snapped.y());
    }

    public static List<UvPoint> sampleLinePcurve(Line2 line, UvPoint start, UvPoint end) {
        com.minicad.geometry2d.Point2 startPoint = new com.minicad.geometry2d.Point2(start.u(), start.v());
        com.minicad.geometry2d.Point2 endPoint = new com.minicad.geometry2d.Point2(end.u(), end.v());
        double startParameter = line.parameterOf(startPoint);
        double endParameter = line.parameterOf(endPoint);
        int segments = Math.max(12, (int) Math.ceil(Math.abs(endParameter - startParameter) * 6.0));
        List<UvPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double parameter = startParameter + (endParameter - startParameter) * index / segments;
            com.minicad.geometry2d.Point2 point = line.pointAt(parameter);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return List.copyOf(points);
    }

    public static List<UvPoint> sampleSplinePcurve(BSplineCurve2 spline, UvPoint projectedStart, UvPoint projectedEnd) {
        List<com.minicad.geometry2d.Point2> sampled = spline.sample(48);
        if (sampled.size() < 2) {
            return List.of();
        }
        int startIndex = closestPointIndex(sampled, projectedStart);
        int endIndex = closestPointIndex(sampled, projectedEnd);
        if (startIndex == endIndex) {
            return List.of(projectedStart, projectedEnd);
        }
        List<UvPoint> points = new ArrayList<>();
        int step = startIndex <= endIndex ? 1 : -1;
        for (int index = startIndex; index != endIndex + step; index += step) {
            com.minicad.geometry2d.Point2 point = sampled.get(index);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, projectedStart);
        points.set(points.size() - 1, projectedEnd);
        return List.copyOf(points);
    }

    public static List<UvPoint> sampleCirclePcurve(Circle2 circle, UvPoint start, UvPoint end) {
        com.minicad.geometry2d.Point2 startPoint = new com.minicad.geometry2d.Point2(start.u(), start.v());
        com.minicad.geometry2d.Point2 endPoint = new com.minicad.geometry2d.Point2(end.u(), end.v());
        double startAngle = circle.angleOf(startPoint);
        double endAngle = circle.angleOf(endPoint);
        double delta = endAngle - startAngle;
        if (delta > Math.PI) {
            delta -= Math.PI * 2.0;
        } else if (delta < -Math.PI) {
            delta += Math.PI * 2.0;
        }
        int segments = Math.max(18, (int) Math.ceil(Math.abs(delta) * 18.0));
        List<UvPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double angle = startAngle + delta * index / segments;
            com.minicad.geometry2d.Point2 point = circle.pointAt(angle);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return List.copyOf(points);
    }

    public static List<UvPoint> sampleEllipsePcurve(Ellipse2 ellipse, UvPoint start, UvPoint end) {
        com.minicad.geometry2d.Point2 startPoint = new com.minicad.geometry2d.Point2(start.u(), start.v());
        com.minicad.geometry2d.Point2 endPoint = new com.minicad.geometry2d.Point2(end.u(), end.v());
        double startAngle = ellipse.angleOf(startPoint);
        double endAngle = ellipse.angleOf(endPoint);
        double delta = endAngle - startAngle;
        if (delta > Math.PI) {
            delta -= Math.PI * 2.0;
        } else if (delta < -Math.PI) {
            delta += Math.PI * 2.0;
        }
        int segments = Math.max(18, (int) Math.ceil(Math.abs(delta) * 18.0));
        List<UvPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double angle = startAngle + delta * index / segments;
            com.minicad.geometry2d.Point2 point = ellipse.pointAt(angle);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return List.copyOf(points);
    }

    public static List<UvPoint> sampleTrimmedPcurve(TrimmedCurve2 trimmed, UvPoint projectedStart, UvPoint projectedEnd) {
        UvPoint trimStart = new UvPoint(trimmed.trimStart().x(), trimmed.trimStart().y());
        UvPoint trimEnd = new UvPoint(trimmed.trimEnd().x(), trimmed.trimEnd().y());
        List<UvPoint> forward = sampleCurve2(trimmed.basisCurve(), trimStart, trimEnd);
        List<UvPoint> reverse = sampleCurve2(trimmed.basisCurve(), trimEnd, trimStart);
        if (forward.isEmpty() && reverse.isEmpty()) {
            return List.of();
        }
        List<UvPoint> preferred;
        if (!trimmed.senseAgreement()) {
            preferred = reverse.isEmpty() ? forward : reverse;
        } else {
            preferred = score(projectedStart, projectedEnd, forward) <= score(projectedStart, projectedEnd, reverse)
                    ? forward
                    : reverse;
        }
        return alignTrimmedSamples(preferred, projectedStart, projectedEnd);
    }

    public static List<UvPoint> sampleCurve2(com.minicad.geometry2d.Curve2 curve, UvPoint start, UvPoint end) {
        if (curve instanceof Line2 line) {
            return sampleLinePcurve(line, start, end);
        }
        if (curve instanceof Circle2 circle) {
            return sampleCirclePcurve(circle, start, end);
        }
        if (curve instanceof Ellipse2 ellipse) {
            return sampleEllipsePcurve(ellipse, start, end);
        }
        if (curve instanceof BSplineCurve2 spline) {
            return sampleSplinePcurve(spline, start, end);
        }
        if (curve instanceof TrimmedCurve2 trimmed) {
            return sampleTrimmedPcurve(trimmed, start, end);
        }
        return List.of();
    }

    public static double score(UvPoint start, UvPoint end, List<UvPoint> samples) {
        if (samples.isEmpty()) {
            return Double.POSITIVE_INFINITY;
        }
        return distanceSquared(start, samples.getFirst()) + distanceSquared(end, samples.getLast());
    }

    public static List<UvPoint> alignTrimmedSamples(List<UvPoint> samples, UvPoint projectedStart, UvPoint projectedEnd) {
        if (samples.isEmpty()) {
            return samples;
        }
        List<UvPoint> aligned = new ArrayList<>(samples);
        double forwardScore = distanceSquared(projectedStart, aligned.getFirst()) + distanceSquared(projectedEnd, aligned.getLast());
        double reverseScore = distanceSquared(projectedStart, aligned.getLast()) + distanceSquared(projectedEnd, aligned.getFirst());
        if (reverseScore < forwardScore) {
            java.util.Collections.reverse(aligned);
        }
        aligned.set(0, projectedStart);
        aligned.set(aligned.size() - 1, projectedEnd);
        return List.copyOf(aligned);
    }

    public static com.minicad.geometry2d.Point2 snapEllipseSeed(UvPoint point, Ellipse2 ellipse) {
        com.minicad.geometry2d.Vector2 offset = new com.minicad.geometry2d.Point2(point.u(), point.v()).subtract(ellipse.center());
        if (offset.norm() <= Epsilon.EPS) {
            return ellipse.pointAt(0.0);
        }
        com.minicad.geometry2d.Vector2 x = ellipse.xDirection().asVector();
        com.minicad.geometry2d.Vector2 y = new com.minicad.geometry2d.Vector2(-x.y(), x.x());
        double nx = offset.dot(x) / ellipse.semiAxis1();
        double ny = offset.dot(y) / ellipse.semiAxis2();
        double norm = Math.hypot(nx, ny);
        if (norm <= Epsilon.EPS) {
            return ellipse.pointAt(0.0);
        }
        double angle = Math.atan2(ny / norm, nx / norm);
        return ellipse.pointAt(angle);
    }

    // ─── closestPointIndex / sameUv / distanceSquared ─────────────────────

    public static int closestPointIndex(List<com.minicad.geometry2d.Point2> points, UvPoint target) {
        int bestIndex = 0;
        double bestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index < points.size(); index++) {
            com.minicad.geometry2d.Point2 point = points.get(index);
            double du = point.x() - target.u();
            double dv = point.y() - target.v();
            double distance = du * du + dv * dv;
            if (distance < bestDistance) {
                bestDistance = distance;
                bestIndex = index;
            }
        }
        return bestIndex;
    }

    public static boolean sameUv(UvPoint left, UvPoint right) {
        return distanceSquared(left, right) <= 1.0e-12;
    }

    public static double distanceSquared(UvPoint left, UvPoint right) {
        double du = left.u() - right.u();
        double dv = left.v() - right.v();
        return du * du + dv * dv;
    }

    // ─── signedArea / contains / isOnPolygonBoundary / isOnSegment ────────

    public static double signedArea(List<UvPoint> points) {
        if (points.size() < 3) {
            return 0.0;
        }
        double area = 0.0;
        for (int index = 0; index + 1 < points.size(); index++) {
            UvPoint current = points.get(index);
            UvPoint next = points.get(index + 1);
            area += current.u() * next.v() - next.u() * current.v();
        }
        return area * 0.5;
    }

    public static boolean contains(List<UvPoint> polygon, UvPoint point) {
        if (polygon.size() < 3) {
            return false;
        }
        if (isOnPolygonBoundary(polygon, point)) {
            return true;
        }
        boolean inside = false;
        for (int i = 0, j = polygon.size() - 1; i < polygon.size(); j = i++) {
            UvPoint a = polygon.get(i);
            UvPoint b = polygon.get(j);
            boolean intersects = ((a.v() > point.v()) != (b.v() > point.v()))
                    && (point.u() < (b.u() - a.u()) * (point.v() - a.v()) / ((b.v() - a.v()) + 1.0e-12) + a.u());
            if (intersects) {
                inside = !inside;
            }
        }
        return inside;
    }

    public static boolean isOnPolygonBoundary(List<UvPoint> polygon, UvPoint point) {
        for (int index = 0; index + 1 < polygon.size(); index++) {
            if (isOnSegment(polygon.get(index), polygon.get(index + 1), point)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOnSegment(UvPoint a, UvPoint b, UvPoint point) {
        double abU = b.u() - a.u();
        double abV = b.v() - a.v();
        double lengthSquared = abU * abU + abV * abV;
        if (lengthSquared <= 1.0e-18) {
            return distanceSquared(a, point) <= 1.0e-18;
        }
        double apU = point.u() - a.u();
        double apV = point.v() - a.v();
        double cross = abU * apV - abV * apU;
        if (Math.abs(cross) > 1.0e-9) {
            return false;
        }
        double dot = apU * abU + apV * abV;
        if (dot < -1.0e-9) {
            return false;
        }
        return dot <= lengthSquared + 1.0e-9;
    }

    // ─── Cylindrical / conical / spherical / toroidal coordinate math ────

    public static List<Double> unwrapAngles(CylindricalSurface surface, List<CartesianPoint> points) {
        return unwrapAngles(surface.position(), points);
    }

    public static List<Double> unwrapAngles(Axis2Placement3D placement, List<CartesianPoint> points) {
        List<Double> angles = new ArrayList<>(points.size());
        for (CartesianPoint point : points) {
            double angle = cylindricalAngle(placement, point);
            if (!angles.isEmpty()) {
                double previous = angles.getLast();
                while (angle - previous > Math.PI) {
                    angle -= Math.PI * 2.0;
                }
                while (angle - previous < -Math.PI) {
                    angle += Math.PI * 2.0;
                }
            }
            angles.add(angle);
        }
        return List.copyOf(angles);
    }

    public static double averageAxialHeight(CylindricalSurface surface, List<CartesianPoint> points) {
        return averageAxialHeight(surface.position(), points);
    }

    public static double averageAxialHeight(Axis2Placement3D placement, List<CartesianPoint> points) {
        double total = 0.0;
        for (CartesianPoint point : points) {
            total += axialHeight(placement, point);
        }
        return total / points.size();
    }

    public static double axialHeight(CylindricalSurface surface, CartesianPoint point) {
        return axialHeight(surface.position(), point);
    }

    public static double axialHeight(Axis2Placement3D placement, CartesianPoint point) {
        return point.subtract(placement.location()).dot(placement.axis().asVector());
    }

    public static double cylindricalAngle(CylindricalSurface surface, CartesianPoint point) {
        return cylindricalAngle(surface.position(), point);
    }

    public static double cylindricalAngle(Axis2Placement3D placement, CartesianPoint point) {
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        return Math.atan2(y, x);
    }

    public static CartesianPoint surfacePoint(CylindricalSurface surface, double angle, double height) {
        Axis2Placement3D placement = surface.position();
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle) * surface.radius())
                .add(placement.yDirection().asVector().scale(Math.sin(angle) * surface.radius()));
        Vector3 axial = placement.axis().asVector().scale(height);
        return placement.location().add(radial.add(axial));
    }

    public static Vector3 cylindricalNormal(CylindricalSurface surface, double angle, boolean sameSense) {
        Axis2Placement3D placement = surface.position();
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle))
                .add(placement.yDirection().asVector().scale(Math.sin(angle)));
        return sameSense ? radial : radial.scale(-1.0);
    }

    public static CartesianPoint conicalSurfacePoint(ConicalSurface surface, double angle, double height) {
        Axis2Placement3D placement = surface.position();
        double radius = surface.radius() + height * Math.tan(surface.semiAngle());
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle) * radius)
                .add(placement.yDirection().asVector().scale(Math.sin(angle) * radius));
        Vector3 axial = placement.axis().asVector().scale(height);
        return placement.location().add(radial.add(axial));
    }

    public static Vector3 conicalNormal(ConicalSurface surface, double angle, boolean sameSense) {
        Axis2Placement3D placement = surface.position();
        double slope = Math.tan(surface.semiAngle());
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle))
                .add(placement.yDirection().asVector().scale(Math.sin(angle)));
        Vector3 normal = radial.subtract(placement.axis().asVector().scale(slope));
        return sameSense ? normal.normalize().asVector() : normal.normalize().reverse().asVector();
    }

    public static double sphericalU(Axis2Placement3D placement, CartesianPoint point) {
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        return Math.atan2(y, x);
    }

    public static double sphericalV(Axis2Placement3D placement, CartesianPoint point, double radius) {
        Vector3 offset = point.subtract(placement.location());
        double z = offset.dot(placement.axis().asVector());
        double normalized = radius <= 1.0e-12 ? 0.0 : z / radius;
        normalized = Math.max(-1.0, Math.min(1.0, normalized));
        return Math.asin(normalized);
    }

    public static CartesianPoint sphericalSurfacePoint(Axis2Placement3D placement, double radius, double u, double v) {
        double cosV = Math.cos(v);
        Vector3 normal = placement.xDirection().asVector().scale(Math.cos(u) * cosV)
                .add(placement.yDirection().asVector().scale(Math.sin(u) * cosV))
                .add(placement.axis().asVector().scale(Math.sin(v)));
        return placement.location().add(normal.scale(radius));
    }

    public static Vector3 sphericalNormal(Axis2Placement3D placement, double u, double v, boolean sameSense) {
        double cosV = Math.cos(v);
        Vector3 normal = placement.xDirection().asVector().scale(Math.cos(u) * cosV)
                .add(placement.yDirection().asVector().scale(Math.sin(u) * cosV))
                .add(placement.axis().asVector().scale(Math.sin(v)));
        return sameSense ? normal.normalize().asVector() : normal.normalize().reverse().asVector();
    }

    public static CartesianPoint toroidalSurfacePoint(ToroidalSurface surface, double u, double v) {
        return toroidalSurfacePoint(surface.position(), surface.majorRadius(), surface.minorRadius(), u, v);
    }

    public static CartesianPoint toroidalSurfacePoint(
            Axis2Placement3D placement,
            double majorRadius,
            double minorRadius,
            double u,
            double v
    ) {
        double radial = majorRadius + minorRadius * Math.cos(v);
        Vector3 xy = placement.xDirection().asVector().scale(Math.cos(u) * radial)
                .add(placement.yDirection().asVector().scale(Math.sin(u) * radial));
        Vector3 z = placement.axis().asVector().scale(minorRadius * Math.sin(v));
        return placement.location().add(xy.add(z));
    }

    public static Vector3 toroidalNormal(ToroidalSurface surface, double u, double v, boolean sameSense) {
        return toroidalNormal(surface.position(), u, v, sameSense);
    }

    public static Vector3 toroidalNormal(Axis2Placement3D placement, double u, double v, boolean sameSense) {
        Vector3 normal = placement.xDirection().asVector().scale(Math.cos(u) * Math.cos(v))
                .add(placement.yDirection().asVector().scale(Math.sin(u) * Math.cos(v)))
                .add(placement.axis().asVector().scale(Math.sin(v)));
        return sameSense ? normal.normalize().asVector() : normal.normalize().reverse().asVector();
    }

    public static List<Double> unwrapToroidalU(ToroidalSurface surface, List<CartesianPoint> points) {
        List<Double> values = new ArrayList<>(points.size());
        for (CartesianPoint point : points) {
            double value = toroidalU(surface, point);
            if (!values.isEmpty()) {
                double previous = values.getLast();
                while (value - previous > Math.PI) {
                    value -= Math.PI * 2.0;
                }
                while (value - previous < -Math.PI) {
                    value += Math.PI * 2.0;
                }
            }
            values.add(value);
        }
        return List.copyOf(values);
    }

    public static List<Double> unwrapToroidalV(ToroidalSurface surface, List<CartesianPoint> points) {
        List<Double> values = new ArrayList<>(points.size());
        for (CartesianPoint point : points) {
            double value = toroidalV(surface, point);
            if (!values.isEmpty()) {
                double previous = values.getLast();
                while (value - previous > Math.PI) {
                    value -= Math.PI * 2.0;
                }
                while (value - previous < -Math.PI) {
                    value += Math.PI * 2.0;
                }
            }
            values.add(value);
        }
        return List.copyOf(values);
    }

    public static double averageToroidalV(ToroidalSurface surface, List<CartesianPoint> points) {
        double total = 0.0;
        for (CartesianPoint point : points) {
            total += toroidalV(surface, point);
        }
        return total / points.size();
    }

    public static double toroidalU(ToroidalSurface surface, CartesianPoint point) {
        return toroidalU(surface.position(), point);
    }

    public static double toroidalU(Axis2Placement3D placement, CartesianPoint point) {
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        return Math.atan2(y, x);
    }

    public static double toroidalV(ToroidalSurface surface, CartesianPoint point) {
        return toroidalV(surface.position(), surface.majorRadius(), point);
    }

    public static double toroidalV(Axis2Placement3D placement, double majorRadius, CartesianPoint point) {
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        double z = offset.dot(placement.axis().asVector());
        double rho = Math.sqrt(x * x + y * y);
        return Math.atan2(z, rho - majorRadius);
    }

    // ─── Private helper methods (copied from StepPreviewJsonExporter) ─────

    private static double unwrapPeriodic(double value, Double previous, double period) {
        if (previous == null) {
            return value;
        }
        while (value - previous > period * 0.5) {
            value -= period;
        }
        while (value - previous < -period * 0.5) {
            value += period;
        }
        return value;
    }

    private static BSplineSurface3 buildBsplineSurface(StepEntity geometry, StepCadBuilder builder) {
        if (geometry instanceof StepBSplineSurfaceWithKnots splineSurface) {
            return builder.buildBSplineSurface(splineSurface.id());
        }
        if (geometry instanceof StepBSplineSurface splineSurface) {
            return builder.buildGenericBSplineSurface(splineSurface.id());
        }
        if (geometry instanceof StepBSplineSurfaceWithKnotsAndBreakpoints splineSurface) {
            return builder.buildBSplineSurfaceWithBreakpoints(splineSurface.id());
        }
        if (geometry instanceof StepBezierSurface splineSurface) {
            return builder.buildBezierSurface(splineSurface.id());
        }
        if (geometry instanceof StepUniformSurface splineSurface) {
            return builder.buildUniformSurface(splineSurface.id());
        }
        if (geometry instanceof StepQuasiUniformSurface splineSurface) {
            return builder.buildQuasiUniformSurface(splineSurface.id());
        }
        if (geometry instanceof StepPiecewiseBezierSurface splineSurface) {
            return builder.buildPiecewiseBezierSurface(splineSurface.id());
        }
        throw new UnsupportedGeometryException(StepPreviewJsonExporter.surfaceTypeName(geometry) + " is not a supported B-spline-like surface");
    }

    private static BSplineSurface3 buildFreeFormSurface(StepFreeFormSurface surface, StepCadBuilder builder) {
        int uCount = surface.controlPoints().size();
        int vCount = surface.controlPoints().isEmpty() ? 0 : surface.controlPoints().getFirst().size();
        if (uCount < 2 || vCount < 2) {
            throw new UnsupportedGeometryException("FREE_FORM_SURFACE requires at least 2x2 control points");
        }
        List<List<CartesianPoint>> controlPoints = new ArrayList<>(uCount);
        for (List<StepEntity> row : surface.controlPoints()) {
            List<CartesianPoint> pointRow = new ArrayList<>(row.size());
            for (StepEntity pt : row) {
                if (pt instanceof com.minicad.step.model.geometry.StepCartesianPoint cartesianPoint) {
                    pointRow.add(builder.buildPoint(cartesianPoint.id()));
                } else {
                    throw new UnsupportedGeometryException("FREE_FORM_SURFACE control points must be Cartesian points");
                }
            }
            controlPoints.add(List.copyOf(pointRow));
        }
        int uDegree = surface.degreeU();
        int vDegree = surface.degreeV();
        // Generate uniform knot vectors with multiplicities
        int uKnotCount = uCount - uDegree;
        int vKnotCount = vCount - vDegree;
        List<Integer> uMults = new ArrayList<>();
        uMults.add(uDegree + 1);
        for (int i = 0; i < uKnotCount - 2; i++) uMults.add(1);
        uMults.add(uDegree + 1);
        List<Integer> vMults = new ArrayList<>();
        vMults.add(vDegree + 1);
        for (int i = 0; i < vKnotCount - 2; i++) vMults.add(1);
        vMults.add(vDegree + 1);
        List<Double> uKnots = new ArrayList<>();
        for (int i = 0; i < uKnotCount; i++) uKnots.add((double) i);
        List<Double> vKnots = new ArrayList<>();
        for (int i = 0; i < vKnotCount; i++) vKnots.add((double) i);
        return new BSplineSurface3(uDegree, vDegree, controlPoints, uMults, vMults, uKnots, vKnots);
    }

    private static double[] matrixForTransformationOperator(
            com.minicad.step.model.geometry.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        Vector3 axis1 = transformation.axis1() == null
                ? new Vector3(1.0, 0.0, 0.0)
                : builder.buildDirection(transformation.axis1().id()).asVector();
        Vector3 axis2;
        if (transformation.axis2() != null) {
            axis2 = builder.buildDirection(transformation.axis2().id()).asVector();
        } else {
            Vector3 fallback = new Vector3(0.0, 1.0, 0.0);
            axis2 = axis1.cross(fallback).isZero() ? new Vector3(0.0, 0.0, 1.0) : fallback;
        }
        Vector3 axis3;
        if (transformation.axis3() != null) {
            axis3 = builder.buildDirection(transformation.axis3().id()).asVector();
        } else {
            Vector3 cross = axis1.cross(axis2);
            axis3 = cross.isZero() ? new Vector3(0.0, 0.0, 1.0) : cross.normalize().asVector();
        }
        double scale = transformation.scale() == null ? 1.0 : transformation.scale();
        CartesianPoint origin = builder.buildPoint(transformation.localOrigin().id());
        return new double[]{
                axis1.x() * scale, axis2.x() * scale, axis3.x() * scale, origin.x(),
                axis1.y() * scale, axis2.y() * scale, axis3.y() * scale, origin.y(),
                axis1.z() * scale, axis2.z() * scale, axis3.z() * scale, origin.z(),
                0.0, 0.0, 0.0, 1.0
        };
    }

    private static CartesianPoint transformCartesian(CartesianPoint point, double[] matrix) {
        double x = point.x();
        double y = point.y();
        double z = point.z();
        return new CartesianPoint(
                matrix[0] * x + matrix[1] * y + matrix[2] * z + matrix[3],
                matrix[4] * x + matrix[5] * y + matrix[6] * z + matrix[7],
                matrix[8] * x + matrix[9] * y + matrix[10] * z + matrix[11]
        );
    }

    private static VectorPayload transform(VectorPayload vector, double[] matrix) {
        double x = matrix[0] * vector.x() + matrix[1] * vector.y() + matrix[2] * vector.z();
        double y = matrix[4] * vector.x() + matrix[5] * vector.y() + matrix[6] * vector.z();
        double z = matrix[8] * vector.x() + matrix[9] * vector.y() + matrix[10] * vector.z();
        double length = Math.sqrt(x * x + y * y + z * z);
        if (length <= Epsilon.EPS) {
            return vector;
        }
        return new VectorPayload(x / length, y / length, z / length);
    }

    private static Vector3 fallbackNormal(Vector3 preferredAxis) {
        Vector3 seed = Math.abs(preferredAxis.x()) < 0.9 ? new Vector3(1.0, 0.0, 0.0) : new Vector3(0.0, 1.0, 0.0);
        Vector3 normal = preferredAxis.cross(seed);
        if (normal.norm() <= Epsilon.EPS) {
            normal = preferredAxis.cross(new Vector3(0.0, 0.0, 1.0));
        }
        return normal.norm() <= Epsilon.EPS ? new Vector3(0.0, 0.0, 1.0) : normal;
    }

    private static Direction3 revolutionReferenceDirection(
            PreviewCurveEvaluator.CurveEvaluator directrix,
            CartesianPoint axisOrigin,
            Direction3 axisDirection
    ) {
        for (CartesianPoint sample : directrix.sample(96)) {
            Vector3 radial = radialComponent(sample, axisOrigin, axisDirection);
            if (radial.norm() > Epsilon.EPS) {
                return Direction3.from(radial);
            }
        }
        Vector3 axis = axisDirection.asVector();
        Vector3 seed = Math.abs(axis.x()) < 0.9 ? new Vector3(1.0, 0.0, 0.0) : new Vector3(0.0, 0.0, 1.0);
        Vector3 radial = seed.subtract(axis.scale(seed.dot(axis)));
        return Direction3.from(radial);
    }

    private static CartesianPoint toRevolutionMeridianPoint(
            CartesianPoint point,
            CartesianPoint axisOrigin,
            Direction3 axisDirection,
            Direction3 radialReference
    ) {
        Vector3 offset = point.subtract(axisOrigin);
        double axisCoordinate = offset.dot(axisDirection.asVector());
        Vector3 radial = radialComponent(point, axisOrigin, axisDirection);
        double radius = radial.norm();
        return axisOrigin
                .add(axisDirection.asVector().scale(axisCoordinate))
                .add(radialReference.asVector().scale(radius));
    }

    private static CartesianPoint revolveAroundAxis(
            CartesianPoint point,
            CartesianPoint axisOrigin,
            Direction3 axisDirection,
            Direction3 radialReference,
            Direction3 tangentialReference,
            double angle
    ) {
        Vector3 offset = point.subtract(axisOrigin);
        double axisCoordinate = offset.dot(axisDirection.asVector());
        double radius = radialComponent(point, axisOrigin, axisDirection).norm();
        Vector3 rotated = radialReference.asVector().scale(Math.cos(angle) * radius)
                .add(tangentialReference.asVector().scale(Math.sin(angle) * radius))
                .add(axisDirection.asVector().scale(axisCoordinate));
        return axisOrigin.add(rotated);
    }

    private static Vector3 tangentAlongRevolutionDirectrix(
            PreviewCurveEvaluator.CurveEvaluator directrix,
            CartesianPoint axisOrigin,
            Direction3 axisDirection,
            Direction3 radialReference,
            Direction3 tangentialReference,
            double u,
            double v
    ) {
        double span = Math.max(directrix.end() - directrix.start(), 1.0);
        double step = Math.max(span * 1.0e-4, 1.0e-5);
        double u0 = Math.max(directrix.start(), u - step);
        double u1 = Math.min(directrix.end(), u + step);
        if (u1 - u0 <= Epsilon.EPS) {
            u0 = Math.max(directrix.start(), u - step * 2.0);
            u1 = Math.min(directrix.end(), u + step * 2.0);
        }
        CartesianPoint p0 = revolveAroundAxis(directrix.pointAt(u0), axisOrigin, axisDirection, radialReference, tangentialReference, v);
        CartesianPoint p1 = revolveAroundAxis(directrix.pointAt(u1), axisOrigin, axisDirection, radialReference, tangentialReference, v);
        return p1.subtract(p0);
    }

    private static Vector3 tangentAroundRevolution(
            CartesianPoint axisOrigin,
            Direction3 axisDirection,
            Direction3 radialReference,
            Direction3 tangentialReference,
            CartesianPoint point,
            double angle
    ) {
        CartesianPoint rotated = revolveAroundAxis(point, axisOrigin, axisDirection, radialReference, tangentialReference, angle);
        Vector3 radial = radialComponent(rotated, axisOrigin, axisDirection);
        return axisDirection.asVector().cross(radial);
    }

    private static Vector3 radialComponent(CartesianPoint point, CartesianPoint axisOrigin, Direction3 axisDirection) {
        Vector3 offset = point.subtract(axisOrigin);
        return offset.subtract(axisDirection.asVector().scale(offset.dot(axisDirection.asVector())));
    }

    private static List<CartesianPoint> sampleLoop(FaceBound bound) {
        if (bound.loop() instanceof VertexLoop vertexLoop) {
            return List.of(vertexLoop.vertex().point());
        }
        if (bound.loop() instanceof PolyLoop polyLoop) {
            List<CartesianPoint> sampled = new ArrayList<>(polyLoop.points());
            if (!sampled.isEmpty() && sampled.getFirst().distanceTo(sampled.getLast()) > 1.0e-9) {
                sampled.add(sampled.getFirst());
            }
            return bound.orientation() ? sampled : reverseClosedLoop(sampled);
        }
        if (!(bound.loop() instanceof EdgeLoop edgeLoop)) {
            throw new com.minicad.common.UnsupportedGeometryException("preview export requires EDGE_LOOP, POLY_LOOP or VERTEX_LOOP");
        }
        List<CartesianPoint> sampled = new ArrayList<>();
        boolean firstEdge = true;
        for (OrientedEdge orientedEdge : edgeLoop.edges()) {
            List<CartesianPoint> edgePoints = StepPreviewJsonExporter.sampleOrientedEdge(orientedEdge);
            int startIndex = firstEdge ? 0 : 1;
            for (int i = startIndex; i < edgePoints.size(); i++) {
                sampled.add(edgePoints.get(i));
            }
            firstEdge = false;
        }
        if (!sampled.isEmpty() && sampled.getFirst().distanceTo(sampled.getLast()) > 1.0e-9) {
            sampled.add(sampled.getFirst());
        }
        return bound.orientation() ? sampled : reverseClosedLoop(sampled);
    }

    private static <T> List<T> reverseClosedLoop(List<T> points) {
        if (points.size() < 2) {
            return points;
        }
        List<T> reversed = new ArrayList<>(points);
        if (reversed.getFirst().equals(reversed.getLast())) {
            T start = reversed.removeLast();
            java.util.Collections.reverse(reversed);
            reversed.add(reversed.getFirst());
            reversed.set(0, start);
            reversed.set(reversed.size() - 1, start);
            return reversed;
        }
        java.util.Collections.reverse(reversed);
        return reversed;
    }

    private static CartesianPoint pointFromStep(StepCartesianPoint point) {
        return StepPreviewJsonExporter.pointFromStep(point);
    }

    private static Curve3 curveForLooseEdge(StepEntity item, StepCadBuilder builder) {
        try {
            if (item instanceof StepLine line) {
                return builder.buildLine(line.id());
            }
            if (item instanceof StepCircle circle) {
                return builder.buildCircle(circle.id());
            }
            if (item instanceof StepEllipse ellipse) {
                return builder.buildEllipse(ellipse.id());
            }
            if (item instanceof StepPolyline polyline) {
                return builder.buildPolyline(polyline.id());
            }
            if (item instanceof StepBSplineSurfaceWithKnots spline) {
                return builder.buildBSplineCurve(spline.id());
            }
            if (item instanceof StepTrimmedCurve trimmedCurve) {
                return builder.buildTrimmedCurve(trimmedCurve.id());
            }
            if (item instanceof com.minicad.step.model.geometry.StepCompositeCurve compositeCurve) {
                return builder.buildCompositeCurve(compositeCurve.id());
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    private static List<CartesianPoint> sampleEdge(CartesianPoint start, CartesianPoint end, Curve3 curve, boolean naturalForward) {
        if (curve instanceof Line3) {
            return List.of(start, end);
        }
        if (curve instanceof Polyline3 polyline) {
            List<CartesianPoint> points = new ArrayList<>(polyline.points());
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }
        List<CartesianPoint> points = new ArrayList<>(curve.sample(72));
        if (!naturalForward) {
            java.util.Collections.reverse(points);
        }
        if (!points.isEmpty()) {
            points.set(0, start);
            points.set(points.size() - 1, end);
        }
        return List.copyOf(points);
    }
}
