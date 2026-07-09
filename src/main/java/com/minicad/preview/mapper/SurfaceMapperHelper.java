package com.minicad.preview.mapper;

import com.minicad.common.Epsilon;
import com.minicad.export.json.StepPreviewJsonExporter;
import com.minicad.geometry.Axis1Placement;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BSplineSurface3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.RationalBSplineSurface3;
import com.minicad.geometry.SphericalSurface;
import com.minicad.geometry.ToroidalSurface;
import com.minicad.geometry.Vector3;
import com.minicad.helper.MathUtilityHelper;
import com.minicad.helper.SurfaceGeometryHelper;
import com.minicad.preview.payload.UvPoint;
import com.minicad.preview.payload.VectorPayload;
import com.minicad.preview.sampling.CurveEvaluator;
import com.minicad.step.model.StepEntity;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.model.StepGeometricReplica;
import com.minicad.step.model.StepBezierSurface;
import com.minicad.step.model.StepBSplineSurface;
import com.minicad.step.model.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.StepBSplineSurfaceWithKnotsAndBreakpoints;
import com.minicad.step.model.StepCylindricalSurface;
import com.minicad.step.model.StepConicalSurface;
import com.minicad.step.model.StepConicalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepCylindricalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepCurveBoundedSurface;
import com.minicad.step.model.StepDegenerateToroidalSurface;
import com.minicad.step.model.StepFreeFormSurface;
import com.minicad.step.model.StepOrientedSurface;
import com.minicad.step.model.StepOffsetSurface;
import com.minicad.step.model.StepOffsetSurface2;
import com.minicad.step.model.StepPiecewiseBezierSurface;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepQuasiUniformSurface;
import com.minicad.step.model.StepRationalBSplineSurface;
import com.minicad.step.model.StepRectangularCompositeSurface;
import com.minicad.step.model.StepRectangularTrimmedSurface;
import com.minicad.step.model.StepSphericalSurface;
import com.minicad.step.model.StepSphericalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepSurfaceOfLinearExtrusion;
import com.minicad.step.model.StepSurfaceOfRevolution;
import com.minicad.step.model.StepSurfacePatch;
import com.minicad.step.model.StepToroidalSurface;
import com.minicad.step.model.StepToroidalSurfaceWithCylindricalAxis;
import com.minicad.step.model.StepToroidalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepUniformSurface;
import com.minicad.step.model.StepBlendedSurface;

import java.util.List;

/**
 * Helper class for parametric surface mapping operations.
 * Extracted from StepPreviewJsonExporter for maintainability.
 */
public class SurfaceMapperHelper {
    public static ParametricSurfaceMapper mapperForSurface(StepEntity geometry, StepCadBuilder builder) {
        if (geometry instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) geometry;
            return mapperForSurface(trimmedSurface.basisSurface(), builder);
        }
        if (geometry instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) geometry;
            return mapperForSurface(boundedSurface.basisSurface(), builder);
        }
        if (geometry instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) geometry;
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
        if (geometry instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) geometry;
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
        if (geometry instanceof StepCylindricalSurfaceWithEllipticalAxis) {
            StepCylindricalSurfaceWithEllipticalAxis ellipticalAxis = (StepCylindricalSurfaceWithEllipticalAxis) geometry;
            CylindricalSurface surface = builder.buildCylindricalSurfaceWithEllipticalAxis(ellipticalAxis.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = MathUtilityHelper.unwrapPeriodic(SurfaceGeometryHelper.cylindricalAngle(surface.position(), point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, SurfaceGeometryHelper.axialHeight(surface.position(), point));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return SurfaceGeometryHelper.surfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return SurfaceGeometryHelper.cylindricalNormal(surface, u, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepConicalSurfaceWithEllipticalAxis) {
            StepConicalSurfaceWithEllipticalAxis ellipticalAxis = (StepConicalSurfaceWithEllipticalAxis) geometry;
            ConicalSurface surface = builder.buildConicalSurfaceWithEllipticalAxis(ellipticalAxis.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = MathUtilityHelper.unwrapPeriodic(SurfaceGeometryHelper.cylindricalAngle(surface.position(), point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, SurfaceGeometryHelper.axialHeight(surface.position(), point));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return SurfaceGeometryHelper.conicalSurfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return SurfaceGeometryHelper.conicalNormal(surface, u, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepSphericalSurfaceWithEllipticalAxis) {
            StepSphericalSurfaceWithEllipticalAxis ellipticalAxis = (StepSphericalSurfaceWithEllipticalAxis) geometry;
            SphericalSurface surface = builder.buildSphericalSurfaceWithEllipticalAxis(ellipticalAxis.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = MathUtilityHelper.unwrapPeriodic(SurfaceGeometryHelper.sphericalU(surface.position(), point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, SurfaceGeometryHelper.sphericalV(surface.position(), point, surface.radius()));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return SurfaceGeometryHelper.sphericalSurfacePoint(surface.position(), surface.radius(), u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return SurfaceGeometryHelper.sphericalNormal(surface.position(), u, v, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepToroidalSurfaceWithCylindricalAxis) {
            StepToroidalSurfaceWithCylindricalAxis ellipticalAxis = (StepToroidalSurfaceWithCylindricalAxis) geometry;
            ToroidalSurface surface = builder.buildToroidalSurfaceWithCylindricalAxis(ellipticalAxis.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Double previousU = previous == null ? null : previous.u();
                    Double previousV = previous == null ? null : previous.v();
                    double u = MathUtilityHelper.unwrapPeriodic(SurfaceGeometryHelper.toroidalU(surface, point), previousU, Math.PI * 2.0);
                    double v = MathUtilityHelper.unwrapPeriodic(SurfaceGeometryHelper.toroidalV(surface, point), previousV, Math.PI * 2.0);
                    return new UvPoint(u, v);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return SurfaceGeometryHelper.toroidalSurfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return SurfaceGeometryHelper.toroidalNormal(surface, u, v, true);
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
        if (geometry instanceof StepToroidalSurfaceWithEllipticalAxis) {
            StepToroidalSurfaceWithEllipticalAxis ellipticalAxis = (StepToroidalSurfaceWithEllipticalAxis) geometry;
            ToroidalSurface surface = builder.buildToroidalSurfaceWithEllipticalAxis(ellipticalAxis.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Double previousU = previous == null ? null : previous.u();
                    Double previousV = previous == null ? null : previous.v();
                    double u = MathUtilityHelper.unwrapPeriodic(SurfaceGeometryHelper.toroidalU(surface, point), previousU, Math.PI * 2.0);
                    double v = MathUtilityHelper.unwrapPeriodic(SurfaceGeometryHelper.toroidalV(surface, point), previousV, Math.PI * 2.0);
                    return new UvPoint(u, v);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return SurfaceGeometryHelper.toroidalSurfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return SurfaceGeometryHelper.toroidalNormal(surface, u, v, true);
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
        if (geometry instanceof StepOffsetSurface2) {
            StepOffsetSurface2 offsetSurface2 = (StepOffsetSurface2) geometry;
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
        if (geometry instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) geometry).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) geometry;
            if (!(replica.transformation() instanceof com.minicad.step.model.StepCartesianTransformationOperator)) {
                return null;
            }
            com.minicad.step.model.StepCartesianTransformationOperator transformation = (com.minicad.step.model.StepCartesianTransformationOperator) replica.transformation();
            ParametricSurfaceMapper base = mapperForSurface(replica.parent(), builder);
            if (base == null) {
                return null;
            }
            double[] matrix = StepPreviewJsonExporter.matrixForTransformationOperator(transformation, builder);
            double[] inverse = MathUtilityHelper.inverseUniformScaleTransform(matrix);
            if (inverse == null) {
                return null;
            }
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    return base.project(MathUtilityHelper.transformCartesian(point, inverse), previous);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return MathUtilityHelper.transformCartesian(base.pointAt(u, v), matrix);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    VectorPayload transformed = MathUtilityHelper.transform(
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
        if (geometry instanceof StepPlane) {
            StepPlane stepPlane = (StepPlane) geometry;
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
        if (geometry instanceof StepCylindricalSurface) {
            StepCylindricalSurface cylindricalSurface = (StepCylindricalSurface) geometry;
            CylindricalSurface surface = builder.buildCylindricalSurface(cylindricalSurface.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = MathUtilityHelper.unwrapPeriodic(SurfaceGeometryHelper.cylindricalAngle(surface, point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, SurfaceGeometryHelper.axialHeight(surface, point));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return SurfaceGeometryHelper.surfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return SurfaceGeometryHelper.cylindricalNormal(surface, u, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepConicalSurface) {
            StepConicalSurface conicalSurface = (StepConicalSurface) geometry;
            ConicalSurface surface = builder.buildConicalSurface(conicalSurface.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = MathUtilityHelper.unwrapPeriodic(SurfaceGeometryHelper.cylindricalAngle(surface.position(), point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, SurfaceGeometryHelper.axialHeight(surface.position(), point));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return SurfaceGeometryHelper.conicalSurfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return SurfaceGeometryHelper.conicalNormal(surface, u, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepSphericalSurface) {
            StepSphericalSurface sphericalSurface = (StepSphericalSurface) geometry;
            Axis2Placement3D placement = builder.buildPlacement(sphericalSurface.position().id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = MathUtilityHelper.unwrapPeriodic(SurfaceGeometryHelper.sphericalU(placement, point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, SurfaceGeometryHelper.sphericalV(placement, point, sphericalSurface.radius()));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return SurfaceGeometryHelper.sphericalSurfacePoint(placement, sphericalSurface.radius(), u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return SurfaceGeometryHelper.sphericalNormal(placement, u, v, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepDegenerateToroidalSurface) {
            StepDegenerateToroidalSurface degenerateToroidalSurface = (StepDegenerateToroidalSurface) geometry;
            Axis2Placement3D placement = builder.buildPlacement(degenerateToroidalSurface.position().id());
            double majorRadius = degenerateToroidalSurface.majorRadius();
            double minorRadius = degenerateToroidalSurface.minorRadius();
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Double previousU = previous == null ? null : previous.u();
                    Double previousV = previous == null ? null : previous.v();
                    double u = MathUtilityHelper.unwrapPeriodic(SurfaceGeometryHelper.toroidalU(placement, point), previousU, Math.PI * 2.0);
                    double v = MathUtilityHelper.unwrapPeriodic(SurfaceGeometryHelper.toroidalV(placement, majorRadius, point), previousV, Math.PI * 2.0);
                    return new UvPoint(u, v);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return SurfaceGeometryHelper.toroidalSurfacePoint(placement, majorRadius, minorRadius, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return SurfaceGeometryHelper.toroidalNormal(placement, u, v, true);
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
        if (geometry instanceof StepToroidalSurface) {
            StepToroidalSurface toroidalSurface = (StepToroidalSurface) geometry;
            ToroidalSurface surface = builder.buildToroidalSurface(toroidalSurface.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Double previousU = previous == null ? null : previous.u();
                    Double previousV = previous == null ? null : previous.v();
                    double u = MathUtilityHelper.unwrapPeriodic(SurfaceGeometryHelper.toroidalU(surface, point), previousU, Math.PI * 2.0);
                    double v = MathUtilityHelper.unwrapPeriodic(SurfaceGeometryHelper.toroidalV(surface, point), previousV, Math.PI * 2.0);
                    return new UvPoint(u, v);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return SurfaceGeometryHelper.toroidalSurfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return SurfaceGeometryHelper.toroidalNormal(surface, u, v, true);
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
        if (geometry instanceof StepRationalBSplineSurface) {
            StepRationalBSplineSurface splineSurface = (StepRationalBSplineSurface) geometry;
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
            BSplineSurface3 surface = StepPreviewJsonExporter.buildBsplineSurface(geometry, builder);
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
        if (geometry instanceof StepSurfaceOfLinearExtrusion) {
            StepSurfaceOfLinearExtrusion extrusionSurface = (StepSurfaceOfLinearExtrusion) geometry;
            return extrusionMapper(extrusionSurface, builder);
        }
        if (geometry instanceof StepSurfaceOfRevolution) {
            StepSurfaceOfRevolution revolutionSurface = (StepSurfaceOfRevolution) geometry;
            return revolutionMapper(revolutionSurface, builder);
        }
        // Rectangular composite surface: delegate to parent surface mapper
        if (geometry instanceof StepRectangularCompositeSurface) {
            StepRectangularCompositeSurface compositeSurface = (StepRectangularCompositeSurface) geometry;
            return mapperForSurface(compositeSurface.parentSurface(), builder);
        }
        // Surface patch: delegate to basis surface mapper
        if (geometry instanceof StepSurfacePatch) {
            StepSurfacePatch surfacePatch = (StepSurfacePatch) geometry;
            return mapperForSurface(surfacePatch.basisSurface(), builder);
        }
        // Blended surface: delegate to primary surface mapper
        if (geometry instanceof StepBlendedSurface) {
            StepBlendedSurface blended = (StepBlendedSurface) geometry;
            return mapperForSurface(blended.primarySurface(), builder);
        }
        // Free-form surface: build as BSplineSurface3 and use grid-based parametric mapping
        if (geometry instanceof StepFreeFormSurface) {
            StepFreeFormSurface freeForm = (StepFreeFormSurface) geometry;
            BSplineSurface3 surface = StepPreviewJsonExporter.buildFreeFormSurface(freeForm, builder);
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
    public static UvPoint nearestUvOnBSplineSurface(BSplineSurface3 surface, CartesianPoint point, UvPoint previous) {
        double uStart = surface.uStart();
        double uEnd = surface.uEnd();
        double vStart = surface.vStart();
        double vEnd = surface.vEnd();
        boolean hasPrevious = previous != null;

        double bestU = hasPrevious ? MathUtilityHelper.clamp(previous.u(), uStart, uEnd) : uStart;
        double bestV = hasPrevious ? MathUtilityHelper.clamp(previous.v(), vStart, vEnd) : vStart;
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

        double bestU = hasPrevious ? MathUtilityHelper.clamp(previous.u(), uStart, uEnd) : uStart;
        double bestV = hasPrevious ? MathUtilityHelper.clamp(previous.v(), vStart, vEnd) : vStart;
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
                    double u = MathUtilityHelper.clamp(bestU + du * stepU, uStart, uEnd);
                    double v = MathUtilityHelper.clamp(bestV + dv * stepV, vStart, vEnd);
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
    public static ParametricSurfaceMapper extrusionMapper(
            StepSurfaceOfLinearExtrusion extrusionSurface,
            StepCadBuilder builder
    ) {
        CurveEvaluator directrix = StepPreviewJsonExporter.curveEvaluator(extrusionSurface.sweptCurve(), builder);
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
                double u = closestParameter(directrix, basePoint, previous == null ? null : previous.u());
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
        CurveEvaluator directrix = StepPreviewJsonExporter.curveEvaluator(revolutionSurface.sweptCurve(), builder);
        if (directrix == null) {
            return null;
        }
        Axis1Placement axisPlacement = builder.buildAxis1Placement(revolutionSurface.axisPosition().id());
        Direction3 axisDirection = axisPlacement.axis();
        CartesianPoint axisOrigin = axisPlacement.location();
        Direction3 radialReference = revolutionReferenceDirection(directrix, axisOrigin, axisDirection);
        Direction3 tangentialReference = Direction3.from(axisDirection.asVector().cross(radialReference.asVector()));
        return new ParametricSurfaceMapper() {
            @Override
            public UvPoint project(CartesianPoint point, UvPoint previous) {
                Vector3 offset = point.subtract(axisOrigin);
                double v = MathUtilityHelper.unwrapPeriodic(
                        Math.atan2(offset.dot(tangentialReference.asVector()), offset.dot(radialReference.asVector())),
                        previous == null ? null : previous.v(),
                        Math.PI * 2.0
                );
                CartesianPoint meridianPoint = toRevolutionMeridianPoint(point, axisOrigin, axisDirection, radialReference);
                double u = closestParameter(directrix, meridianPoint, previous == null ? null : previous.u());
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
    public static double closestParameter(CurveEvaluator curve, CartesianPoint point, Double preferred) {
        int coarseSegments = 160;
        double start = curve.start();
        double end = curve.end();
        double bestParameter = start;
        double bestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index <= coarseSegments; index++) {
            double parameter = start + (end - start) * index / coarseSegments;
            double distance = curve.pointAt(parameter).distanceTo(point);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestParameter = parameter;
            }
        }
        if (preferred != null && preferred >= start && preferred <= end) {
            double preferredDistance = curve.pointAt(preferred).distanceTo(point);
            if (preferredDistance <= bestDistance * 1.25) {
                bestDistance = preferredDistance;
                bestParameter = preferred;
            }
        }
        double window = Math.max((end - start) / coarseSegments, 1.0e-6);
        for (int refinement = 0; refinement < 5; refinement++) {
            double min = Math.max(start, bestParameter - window);
            double max = Math.min(end, bestParameter + window);
            for (int index = 0; index <= 12; index++) {
                double parameter = min + (max - min) * index / 12.0;
                double distance = curve.pointAt(parameter).distanceTo(point);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestParameter = parameter;
                }
            }
            window *= 0.35;
        }
        return bestParameter;
    }
    public static Direction3 revolutionReferenceDirection(
            CurveEvaluator directrix,
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
    public static CartesianPoint toRevolutionMeridianPoint(
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
    public static CartesianPoint revolveAroundAxis(
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

    public static Vector3 tangentAlongRevolutionDirectrix(
            CurveEvaluator directrix,
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
    public static Vector3 tangentAroundRevolution(
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
    public static Vector3 radialComponent(CartesianPoint point, CartesianPoint axisOrigin, Direction3 axisDirection) {
        Vector3 offset = point.subtract(axisOrigin);
        return offset.subtract(axisDirection.asVector().scale(offset.dot(axisDirection.asVector())));
    }
    public static Vector3 fallbackNormal(Vector3 preferredAxis) {
        Vector3 seed = Math.abs(preferredAxis.x()) < 0.9 ? new Vector3(1.0, 0.0, 0.0) : new Vector3(0.0, 1.0, 0.0);
        Vector3 normal = preferredAxis.cross(seed);
        if (normal.norm() <= Epsilon.EPS) {
            normal = preferredAxis.cross(new Vector3(0.0, 0.0, 1.0));
        }
        return normal.norm() <= Epsilon.EPS ? new Vector3(0.0, 0.0, 1.0) : normal;
    }
}
