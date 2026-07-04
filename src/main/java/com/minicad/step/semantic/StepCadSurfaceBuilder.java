package com.minicad.step.semantic;

import com.minicad.common.Epsilon;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Axis1Placement;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BSplineSurface3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.HyperboloidSurface;
import com.minicad.geometry.OffsetSurface3;
import com.minicad.geometry.ParaboloidSurface;
import com.minicad.geometry.Plane;
import com.minicad.geometry.RationalBSplineSurface3;
import com.minicad.geometry.RuledSurface3;
import com.minicad.geometry.SphericalSurface;
import com.minicad.geometry.SurfaceGeometry;
import com.minicad.geometry.SurfaceOfConstantRadius3;
import com.minicad.geometry.SurfaceOfLinearExtrusion3;
import com.minicad.geometry.SurfaceOfProjection3;
import com.minicad.geometry.SurfaceOfRevolution3;
import com.minicad.geometry.SurfaceOfTranslation3;
import com.minicad.geometry.ToroidalSurface;
import com.minicad.geometry.Vector3;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Point2;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.geometry.StepAxis1Placement;
import com.minicad.step.model.geometry.StepAxis2Placement2D;
import com.minicad.step.model.geometry.StepAxis2Placement3D;
import com.minicad.step.model.geometry.StepBezierSurface;
import com.minicad.step.model.geometry.StepBlendedSurface;
import com.minicad.step.model.geometry.StepBSplineSurface;
import com.minicad.step.model.geometry.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.geometry.StepBSplineSurfaceWithKnotsAndBreakpoints;
import com.minicad.step.model.geometry.StepBoundedSurface;
import com.minicad.step.model.geometry.StepCartesianPoint;
import com.minicad.step.model.geometry.StepCartesianTransformationOperator;
import com.minicad.step.model.geometry.StepCompositeCurveOnSurface;
import com.minicad.step.model.geometry.StepCompositeCurveSegment;
import com.minicad.step.model.geometry.StepConicalSurface;
import com.minicad.step.model.geometry.StepConicalSurfaceWithEllipticalAxis;
import com.minicad.step.model.geometry.StepCurveBoundedSurface;
import com.minicad.step.model.geometry.StepCylindricalSurface;
import com.minicad.step.model.geometry.StepCylindricalSurfaceWithEllipticalAxis;
import com.minicad.step.model.geometry.StepDegenerateToroidalSurface;
import com.minicad.step.model.geometry.StepDirection;
import com.minicad.step.model.geometry.StepFreeFormSurface;
import com.minicad.step.model.geometry.StepHyperboloidSurface;
import com.minicad.step.model.product.StepGeometricReplica;
import com.minicad.step.model.geometry.StepOffsetSurface;
import com.minicad.step.model.geometry.StepOffsetSurface2;
import com.minicad.step.model.geometry.StepOrientedSurface;
import com.minicad.step.model.geometry.StepParaboloidSurface;
import com.minicad.step.model.geometry.StepPiecewiseBezierSurface;
import com.minicad.step.model.geometry.StepPlane;
import com.minicad.step.model.geometry.StepPcurve;
import com.minicad.step.model.geometry.StepQuasiUniformSurface;
import com.minicad.step.model.geometry.StepRationalBSplineSurface;
import com.minicad.step.model.geometry.StepRectangularCompositeSurface;
import com.minicad.step.model.geometry.StepRectangularTrimmedSurface;
import com.minicad.step.model.geometry.StepRuledSurface;
import com.minicad.step.model.geometry.StepSphericalSurface;
import com.minicad.step.model.geometry.StepSphericalSurfaceWithEllipticalAxis;
import com.minicad.step.model.geometry.StepSurface;
import com.minicad.step.model.geometry.StepSurfaceOfConstantRadius;
import com.minicad.step.model.geometry.StepSurfaceOfLinearExtrusion;
import com.minicad.step.model.geometry.StepSurfaceOfProjection;
import com.minicad.step.model.geometry.StepSurfaceOfRevolution;
import com.minicad.step.model.geometry.StepSurfaceOfTranslation;
import com.minicad.step.model.geometry.StepSurfacePatch;
import com.minicad.step.model.geometry.StepToroidalSurface;
import com.minicad.step.model.geometry.StepToroidalSurfaceWithCylindricalAxis;
import com.minicad.step.model.geometry.StepToroidalSurfaceWithEllipticalAxis;
import com.minicad.step.model.geometry.StepToroidalSurfaceWithSpecifiedBends;
import com.minicad.step.model.geometry.StepUniformSurface;
import com.minicad.step.model.geometry.StepVector;
import com.minicad.step.model.manufacturing.StepMachinedSurface;
import com.minicad.step.model.product.StepMappedItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

/**
 * Builder for surface geometry objects extracted from StepCadBuilder.
 *
 * This class handles construction of surface geometry objects:
 * - Elementary surfaces: Plane, CylindricalSurface, ConicalSurface, SphericalSurface, ToroidalSurface
 * - Swept surfaces: SurfaceOfLinearExtrusion3, SurfaceOfRevolution3, RuledSurface3
 * - B-spline surfaces: BSplineSurface3, RationalBSplineSurface3, BezierSurface, UniformSurface
 * - Analytical surfaces: ParaboloidSurface, HyperboloidSurface, SurfaceOfTranslation3, SurfaceOfProjection3
 * - Offset surfaces: OffsetSurface3
 * - Trimmed/bounded surfaces: RectangularTrimmedSurface, CurveBoundedSurface
 *
 * Phase 1 extraction target: methods that build these surface types.
 * Caching is provided to avoid rebuilding the same geometry multiple times.
 */
final class StepCadSurfaceBuilder {

    // Entity lookup
    private final Map<Integer, StepEntity> entitiesById;

    // Dependencies
    private final StepCadGeometryBuilder geometryBuilder;
    private final StepCadGeometryOps geometryOps;
    private final StepCadCurveBuilder curveBuilder;

    // Surface caches
    private final Map<Integer, Plane> planes;
    private final Map<Integer, CylindricalSurface> cylindricalSurfaces;
    private final Map<Integer, ConicalSurface> conicalSurfaces;
    private final Map<Integer, ToroidalSurface> toroidalSurfaces;
    private final Map<Integer, SphericalSurface> sphericalSurfaces;
    private final Map<Integer, RuledSurface3> ruledSurfaces;
    private final Map<Integer, SurfaceOfConstantRadius3> constantRadiusSurfaces;
    private final Map<Integer, SurfaceOfLinearExtrusion3> linearExtrusionSurfaces;
    private final Map<Integer, SurfaceOfRevolution3> revolutionSurfaces;
    private final Map<Integer, ParaboloidSurface> paraboloidSurfaces;
    private final Map<Integer, HyperboloidSurface> hyperboloidSurfaces;
    private final Map<Integer, SurfaceOfTranslation3> translationSurfaces;
    private final Map<Integer, SurfaceOfProjection3> projectionSurfaces;
    private final Map<Integer, BSplineSurface3> bsplineSurfaces;
    private final Map<Integer, RationalBSplineSurface3> rationalBsplineSurfaces;

    // Callbacks for cross-dependencies
    private final IntFunction<Axis2Placement3D> buildPlacementCallback;
    private final IntFunction<Axis1Placement> buildAxis1PlacementCallback;
    private final IntFunction<Curve3> buildCurve3Callback;
    private final IntFunction<Object> buildCurve2Callback;
    private final IntFunction<Object> buildPcurve2Callback;
    private final IntFunction<CompositeCurve3> buildCompositeCurveCallback;

    /**
     * Creates a new StepCadSurfaceBuilder with the specified cache maps and dependencies.
     *
     * @param entitiesById map of STEP entity ID to resolved entity
     * @param geometryBuilder builder for basic geometry (points, directions, placements)
     * @param geometryOps geometry operations helper
     * @param curveBuilder builder for curve geometry
     * @param planes cache for Plane objects
     * @param cylindricalSurfaces cache for CylindricalSurface objects
     * @param conicalSurfaces cache for ConicalSurface objects
     * @param toroidalSurfaces cache for ToroidalSurface objects
     * @param sphericalSurfaces cache for SphericalSurface objects
     * @param ruledSurfaces cache for RuledSurface3 objects
     * @param constantRadiusSurfaces cache for SurfaceOfConstantRadius3 objects
     * @param linearExtrusionSurfaces cache for SurfaceOfLinearExtrusion3 objects
     * @param revolutionSurfaces cache for SurfaceOfRevolution3 objects
     * @param paraboloidSurfaces cache for ParaboloidSurface objects
     * @param hyperboloidSurfaces cache for HyperboloidSurface objects
     * @param translationSurfaces cache for SurfaceOfTranslation3 objects
     * @param projectionSurfaces cache for SurfaceOfProjection3 objects
     * @param bsplineSurfaces cache for BSplineSurface3 objects
     * @param rationalBsplineSurfaces cache for RationalBSplineSurface3 objects
     * @param buildPlacementCallback callback to build Axis2Placement3D
     * @param buildAxis1PlacementCallback callback to build Axis1Placement
     * @param buildCurve3Callback callback to build Curve3
     * @param buildCurve2Callback callback to build Curve2
     * @param buildPcurve2Callback callback to build PCURVE as 2D curve
     * @param buildCompositeCurveCallback callback to build CompositeCurve3
     */
    StepCadSurfaceBuilder(
            Map<Integer, StepEntity> entitiesById,
            StepCadGeometryBuilder geometryBuilder,
            StepCadGeometryOps geometryOps,
            StepCadCurveBuilder curveBuilder,
            Map<Integer, Plane> planes,
            Map<Integer, CylindricalSurface> cylindricalSurfaces,
            Map<Integer, ConicalSurface> conicalSurfaces,
            Map<Integer, ToroidalSurface> toroidalSurfaces,
            Map<Integer, SphericalSurface> sphericalSurfaces,
            Map<Integer, RuledSurface3> ruledSurfaces,
            Map<Integer, SurfaceOfConstantRadius3> constantRadiusSurfaces,
            Map<Integer, SurfaceOfLinearExtrusion3> linearExtrusionSurfaces,
            Map<Integer, SurfaceOfRevolution3> revolutionSurfaces,
            Map<Integer, ParaboloidSurface> paraboloidSurfaces,
            Map<Integer, HyperboloidSurface> hyperboloidSurfaces,
            Map<Integer, SurfaceOfTranslation3> translationSurfaces,
            Map<Integer, SurfaceOfProjection3> projectionSurfaces,
            Map<Integer, BSplineSurface3> bsplineSurfaces,
            Map<Integer, RationalBSplineSurface3> rationalBsplineSurfaces,
            IntFunction<Axis2Placement3D> buildPlacementCallback,
            IntFunction<Axis1Placement> buildAxis1PlacementCallback,
            IntFunction<Curve3> buildCurve3Callback,
            IntFunction<Object> buildCurve2Callback,
            IntFunction<Object> buildPcurve2Callback,
            IntFunction<CompositeCurve3> buildCompositeCurveCallback) {
        this.entitiesById = entitiesById;
        this.geometryBuilder = geometryBuilder;
        this.geometryOps = geometryOps;
        this.curveBuilder = curveBuilder;
        this.planes = planes;
        this.cylindricalSurfaces = cylindricalSurfaces;
        this.conicalSurfaces = conicalSurfaces;
        this.toroidalSurfaces = toroidalSurfaces;
        this.sphericalSurfaces = sphericalSurfaces;
        this.ruledSurfaces = ruledSurfaces;
        this.constantRadiusSurfaces = constantRadiusSurfaces;
        this.linearExtrusionSurfaces = linearExtrusionSurfaces;
        this.revolutionSurfaces = revolutionSurfaces;
        this.paraboloidSurfaces = paraboloidSurfaces;
        this.hyperboloidSurfaces = hyperboloidSurfaces;
        this.translationSurfaces = translationSurfaces;
        this.projectionSurfaces = projectionSurfaces;
        this.bsplineSurfaces = bsplineSurfaces;
        this.rationalBsplineSurfaces = rationalBsplineSurfaces;
        this.buildPlacementCallback = buildPlacementCallback;
        this.buildAxis1PlacementCallback = buildAxis1PlacementCallback;
        this.buildCurve3Callback = buildCurve3Callback;
        this.buildCurve2Callback = buildCurve2Callback;
        this.buildPcurve2Callback = buildPcurve2Callback;
        this.buildCompositeCurveCallback = buildCompositeCurveCallback;
    }

    // ==================== Elementary Surface Builders ====================

    /**
     * Builds a Plane from a STEP PLANE entity.
     *
     * @param id the STEP entity ID
     * @return the Plane geometry object
     */
    Plane buildPlane(int id) {
        Plane existing = planes.get(id);
        if (existing != null) {
            return existing;
        }
        StepPlane plane = requireEntity(id, StepPlane.class, "PLANE");
        Axis2Placement3D placement = buildPlacementCallback.apply(plane.getPosition().id());
        Plane built = new Plane(placement.getLocation(), placement.getAxis());
        planes.put(id, built);
        return built;
    }

    /**
     * Builds a CylindricalSurface from a STEP CYLINDRICAL_SURFACE entity.
     *
     * @param id the STEP entity ID
     * @return the CylindricalSurface geometry object
     */
    CylindricalSurface buildCylindricalSurface(int id) {
        CylindricalSurface existing = cylindricalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepCylindricalSurface surface = requireEntity(id, StepCylindricalSurface.class, "CYLINDRICAL_SURFACE");
        CylindricalSurface built = new CylindricalSurface(buildPlacementCallback.apply(surface.getPosition().id()), surface.getRadius());
        cylindricalSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a ConicalSurface from a STEP CONICAL_SURFACE entity.
     *
     * @param id the STEP entity ID
     * @return the ConicalSurface geometry object
     */
    ConicalSurface buildConicalSurface(int id) {
        ConicalSurface existing = conicalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepConicalSurface surface = requireEntity(id, StepConicalSurface.class, "CONICAL_SURFACE");
        ConicalSurface built = new ConicalSurface(buildPlacementCallback.apply(surface.getPosition().id()), surface.getRadius(), surface.getSemiAngle());
        conicalSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a SphericalSurface from a STEP SPHERICAL_SURFACE entity.
     *
     * @param id the STEP entity ID
     * @return the SphericalSurface geometry object
     */
    SphericalSurface buildSphericalSurface(int id) {
        SphericalSurface existing = sphericalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepSphericalSurface surface = requireEntity(id, StepSphericalSurface.class, "SPHERICAL_SURFACE");
        SphericalSurface built = new SphericalSurface(buildPlacementCallback.apply(surface.getPosition().id()), surface.getRadius());
        sphericalSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a ToroidalSurface from a STEP TOROIDAL_SURFACE entity.
     *
     * @param id the STEP entity ID
     * @return the ToroidalSurface geometry object
     */
    ToroidalSurface buildToroidalSurface(int id) {
        ToroidalSurface existing = toroidalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepToroidalSurface surface = requireEntity(id, StepToroidalSurface.class, "TOROIDAL_SURFACE");
        ToroidalSurface built = new ToroidalSurface(buildPlacementCallback.apply(surface.getPosition().id()), surface.getMajorRadius(), surface.getMinorRadius());
        toroidalSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a degenerate toroidal surface geometry object.
     *
     * @param id the STEP entity ID
     * @return the ToroidalSurface geometry object
     */
    ToroidalSurface buildDegenerateToroidalSurface(int id) {
        ToroidalSurface existing = toroidalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepDegenerateToroidalSurface surface = requireEntity(id, StepDegenerateToroidalSurface.class, "DEGENERATE_TOROIDAL_SURFACE");
        ToroidalSurface built = new ToroidalSurface(buildPlacementCallback.apply(surface.getPosition().id()), surface.getMajorRadius(), surface.getMinorRadius());
        toroidalSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a toroidal surface from TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS.
     */
    ToroidalSurface buildToroidalSurfaceFromSpecifiedBends(StepToroidalSurfaceWithSpecifiedBends surface) {
        ToroidalSurface existing = toroidalSurfaces.get(surface.id());
        if (existing != null) {
            return existing;
        }
        ToroidalSurface built = new ToroidalSurface(buildPlacementCallback.apply(surface.getPosition().id()), surface.getMajorRadius(), surface.getMinorRadius());
        toroidalSurfaces.put(surface.id(), built);
        return built;
    }

    // ==================== Swept Surface Builders ====================

    /**
     * Builds a RuledSurface3 from a STEP RULED_SURFACE entity.
     *
     * @param id the STEP entity ID
     * @return the RuledSurface3 geometry object
     */
    RuledSurface3 buildRuledSurface(int id) {
        RuledSurface3 existing = ruledSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepRuledSurface surface = requireEntity(id, StepRuledSurface.class, "RULED_SURFACE");
        Axis2Placement3D position = buildPlacementCallback.apply(surface.getPosition().id());
        Curve3 directrix1 = buildCurve3Callback.apply(surface.getDirectrix1().id());
        Curve3 directrix2 = buildCurve3Callback.apply(surface.getDirectrix2().id());
        RuledSurface3 built = new RuledSurface3(directrix1, directrix2);
        ruledSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a SurfaceOfConstantRadius3 from a STEP SURFACE_OF_CONSTANT_RADIUS entity.
     *
     * @param id the STEP entity ID
     * @return the SurfaceOfConstantRadius3 geometry object
     */
    SurfaceOfConstantRadius3 buildSurfaceOfConstantRadius(int id) {
        SurfaceOfConstantRadius3 existing = constantRadiusSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepSurfaceOfConstantRadius surface = requireEntity(id, StepSurfaceOfConstantRadius.class, "SURFACE_OF_CONSTANT_RADIUS");
        SurfaceGeometry sweptSurface = buildSupportedFaceGeometry(surface.getSweptSurface(), "SURFACE_OF_CONSTANT_RADIUS");
        if (sweptSurface == null) {
            throw new UnsupportedGeometryException("Swept surface is null for SURFACE_OF_CONSTANT_RADIUS");
        }
        SurfaceOfConstantRadius3 built = new SurfaceOfConstantRadius3(sweptSurface, surface.getRadius());
        constantRadiusSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a SurfaceOfLinearExtrusion3 from a STEP SURFACE_OF_LINEAR_EXTRUSION entity.
     *
     * @param id the STEP entity ID
     * @return the SurfaceOfLinearExtrusion3 geometry object
     */
    SurfaceOfLinearExtrusion3 buildSurfaceOfLinearExtrusion(int id) {
        SurfaceOfLinearExtrusion3 existing = linearExtrusionSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepSurfaceOfLinearExtrusion surface = requireEntity(id, StepSurfaceOfLinearExtrusion.class, "SURFACE_OF_LINEAR_EXTRUSION");
        SurfaceOfLinearExtrusion3 built = new SurfaceOfLinearExtrusion3(
                buildCurve3Callback.apply(surface.getSweptCurve().id()),
                geometryBuilder.buildVector(surface.extrusionAxis().id())
        );
        linearExtrusionSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a SurfaceOfRevolution3 from a STEP SURFACE_OF_REVOLUTION entity.
     *
     * @param id the STEP entity ID
     * @return the SurfaceOfRevolution3 geometry object
     */
    SurfaceOfRevolution3 buildSurfaceOfRevolution(int id) {
        SurfaceOfRevolution3 existing = revolutionSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepSurfaceOfRevolution surface = requireEntity(id, StepSurfaceOfRevolution.class, "SURFACE_OF_REVOLUTION");
        Axis1Placement axis = buildAxis1PlacementCallback.apply(surface.axisPosition().id());
        SurfaceOfRevolution3 built = new SurfaceOfRevolution3(
                buildCurve3Callback.apply(surface.getSweptCurve().id()),
                axis.getLocation(),
                axis.getAxis()
        );
        revolutionSurfaces.put(id, built);
        return built;
    }

    // ==================== Analytical Surface Builders ====================

    /**
     * Builds a ParaboloidSurface from a STEP PARABOLOID_SURFACE entity.
     *
     * @param id the STEP entity ID
     * @return the ParaboloidSurface geometry object
     */
    ParaboloidSurface buildParaboloidSurface(int id) {
        ParaboloidSurface existing = paraboloidSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepParaboloidSurface step = requireEntity(id, StepParaboloidSurface.class, "PARABOLOID_SURFACE");
        ParaboloidSurface built = new ParaboloidSurface(buildPlacementCallback.apply(step.getPosition().id()), step.getFocalLength());
        paraboloidSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a HyperboloidSurface from a STEP HYPERBOLOID_SURFACE entity.
     *
     * @param id the STEP entity ID
     * @return the HyperboloidSurface geometry object
     */
    HyperboloidSurface buildHyperboloidSurface(int id) {
        HyperboloidSurface existing = hyperboloidSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepHyperboloidSurface step = requireEntity(id, StepHyperboloidSurface.class, "HYPERBOLOID_SURFACE");
        HyperboloidSurface built = new HyperboloidSurface(buildPlacementCallback.apply(step.getPosition().id()), step.getRadius(), step.getSemiAxis());
        hyperboloidSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a SurfaceOfTranslation3 from a STEP SURFACE_OF_TRANSLATION entity.
     *
     * @param id the STEP entity ID
     * @return the SurfaceOfTranslation3 geometry object
     */
    SurfaceOfTranslation3 buildSurfaceOfTranslation(int id) {
        SurfaceOfTranslation3 existing = translationSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepSurfaceOfTranslation step = requireEntity(id, StepSurfaceOfTranslation.class, "SURFACE_OF_TRANSLATION");
        Curve3 profile = buildCurve3Callback.apply(step.getProfile().id());
        Vector3 direction = buildVector3(step.getDirection());
        SurfaceOfTranslation3 built = new SurfaceOfTranslation3(profile, direction);
        translationSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a SurfaceOfProjection3 from a STEP SURFACE_OF_PROJECTION entity.
     *
     * @param id the STEP entity ID
     * @return the SurfaceOfProjection3 geometry object
     */
    SurfaceOfProjection3 buildSurfaceOfProjection(int id) {
        SurfaceOfProjection3 existing = projectionSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepSurfaceOfProjection step = requireEntity(id, StepSurfaceOfProjection.class, "SURFACE_OF_PROJECTION");
        Curve3 profile = buildCurve3Callback.apply(step.getProfile().id());
        Vector3 direction = buildVector3(step.getProjectionDirection());
        SurfaceOfProjection3 built = new SurfaceOfProjection3(profile, direction);
        projectionSurfaces.put(id, built);
        return built;
    }

    // ==================== B-Spline Surface Builders ====================

    /**
     * Builds a BSplineSurface3 from a STEP B_SPLINE_SURFACE_WITH_KNOTS entity.
     *
     * @param id the STEP entity ID
     * @return the BSplineSurface3 geometry object
     */
    BSplineSurface3 buildBSplineSurface(int id) {
        BSplineSurface3 existing = bsplineSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepBSplineSurfaceWithKnots surface = requireEntity(id, StepBSplineSurfaceWithKnots.class, "B_SPLINE_SURFACE_WITH_KNOTS");
        List<List<CartesianPoint>> controlPoints = surface.getControlPoints().stream()
                .map(row -> row.stream().map(point -> geometryBuilder.buildPoint(point.id())).collect(Collectors.toList()))
                .collect(Collectors.toList());
        BSplineSurface3 built = new BSplineSurface3(
                surface.getUDegree(),
                surface.getVDegree(),
                controlPoints,
                surface.getUMultiplicities(),
                surface.getVMultiplicities(),
                surface.getUKnots(),
                surface.getVKnots()
        );
        bsplineSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a generic B_SPLINE_SURFACE without explicit knots.
     */
    BSplineSurface3 buildGenericBSplineSurface(int id) {
        BSplineSurface3 existing = bsplineSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepBSplineSurface surface = requireEntity(id, StepBSplineSurface.class, "B_SPLINE_SURFACE");
        List<List<CartesianPoint>> controlPoints = surface.getControlPoints().stream()
                .map(row -> row.stream().map(point -> geometryBuilder.buildPoint(point.id())).collect(Collectors.toList()))
                .collect(Collectors.toList());
        int uCount = controlPoints.size();
        int vCount = controlPoints.get(0).size();
        List<Double> uKnots = List.of(0.0, 1.0);
        List<Double> vKnots = List.of(0.0, 1.0);
        List<Integer> uMults = List.of(surface.getUDegree() + 1, surface.getUDegree() + 1);
        List<Integer> vMults = List.of(surface.getVDegree() + 1, surface.getVDegree() + 1);
        BSplineSurface3 built = new BSplineSurface3(
                surface.getUDegree(),
                surface.getVDegree(),
                controlPoints,
                uMults,
                vMults,
                uKnots,
                vKnots
        );
        bsplineSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a BSplineSurface3 from a STEP BEZIER_SURFACE entity.
     */
    BSplineSurface3 buildBezierSurface(int id) {
        return buildImplicitBSplineSurface(requireEntity(id, StepBezierSurface.class, "BEZIER_SURFACE"));
    }

    /**
     * Builds a BSplineSurface3 from a STEP UNIFORM_SURFACE entity.
     */
    BSplineSurface3 buildUniformSurface(int id) {
        return buildImplicitBSplineSurface(requireEntity(id, StepUniformSurface.class, "UNIFORM_SURFACE"));
    }

    /**
     * Builds a BSplineSurface3 from a STEP QUASI_UNIFORM_SURFACE entity.
     */
    BSplineSurface3 buildQuasiUniformSurface(int id) {
        return buildImplicitBSplineSurface(requireEntity(id, StepQuasiUniformSurface.class, "QUASI_UNIFORM_SURFACE"));
    }

    /**
     * Builds a BSplineSurface3 from a STEP PIECEWISE_BEZIER_SURFACE entity.
     */
    BSplineSurface3 buildPiecewiseBezierSurface(int id) {
        return buildImplicitBSplineSurface(requireEntity(id, StepPiecewiseBezierSurface.class, "PIECEWISE_BEZIER_SURFACE"));
    }

    private BSplineSurface3 buildImplicitBSplineSurface(StepEntity entity) {
        BSplineSurface3 existing = bsplineSurfaces.get(entity.id());
        if (existing != null) {
            return existing;
        }
        ImplicitBSplineSurfaceData surface = implicitBSplineSurfaceData(entity);
        List<List<CartesianPoint>> controlPoints = surface.getControlPoints().stream()
                .map(row -> row.stream().map(point -> geometryBuilder.buildPoint(point.id())).collect(Collectors.toList()))
                .collect(Collectors.toList());
        BSplineSurface3 built = new BSplineSurface3(
                surface.getUDegree(),
                surface.getVDegree(),
                controlPoints,
                surface.getUMultiplicities(),
                surface.getVMultiplicities(),
                surface.getUKnots(),
                surface.getVKnots()
        );
        bsplineSurfaces.put(entity.id(), built);
        return built;
    }

    /**
     * Builds a RationalBSplineSurface3 from a STEP RATIONAL_B_SPLINE_SURFACE entity.
     *
     * @param id the STEP entity ID
     * @return the RationalBSplineSurface3 geometry object
     */
    RationalBSplineSurface3 buildRationalBSplineSurface(int id) {
        RationalBSplineSurface3 existing = rationalBsplineSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepRationalBSplineSurface surface = requireEntity(id, StepRationalBSplineSurface.class, "RATIONAL_B_SPLINE_SURFACE");
        List<List<CartesianPoint>> controlPoints = surface.getControlPoints().stream()
                .map(row -> row.stream().map(point -> geometryBuilder.buildPoint(point.id())).collect(Collectors.toList()))
                .collect(Collectors.toList());
        RationalBSplineSurface3 built = new RationalBSplineSurface3(
                surface.getUDegree(),
                surface.getVDegree(),
                controlPoints,
                surface.getWeightsData(),
                surface.getUMultiplicities(),
                surface.getVMultiplicities(),
                surface.getUKnots(),
                surface.getVKnots()
        );
        rationalBsplineSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a BSplineSurface3 from a STEP B_SPLINE_SURFACE_WITH_KNOTS_AND_BREAKPOINTS entity.
     */
    BSplineSurface3 buildBSplineSurfaceWithBreakpoints(int id) {
        BSplineSurface3 existing = bsplineSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepBSplineSurfaceWithKnotsAndBreakpoints surface = requireEntity(id, StepBSplineSurfaceWithKnotsAndBreakpoints.class,
                "B_SPLINE_SURFACE_WITH_KNOTS_AND_BREAKPOINTS");
        List<List<CartesianPoint>> controlPoints = new ArrayList<>(surface.getControlPoints().size());
        for (List<StepCartesianPoint> row : surface.getControlPoints()) {
            List<CartesianPoint> pointRow = new ArrayList<>(row.size());
            for (StepCartesianPoint pt : row) {
                pointRow.add(geometryBuilder.buildPoint(pt.id()));
            }
            controlPoints.add(List.copyOf(pointRow));
        }
        BSplineSurface3 built = new BSplineSurface3(
                surface.getUDegree(), surface.getVDegree(), controlPoints,
                surface.uKnotMultiplicities(), surface.vKnotMultiplicities(),
                surface.getUKnots(), surface.getVKnots());
        bsplineSurfaces.put(id, built);
        return built;
    }

    // ==================== Elliptical Axis Surface Builders ====================

    /**
     * Builds a CylindricalSurface from a STEP CYLINDRICAL_SURFACE_WITH_ELLIPTICAL_AXIS entity.
     */
    CylindricalSurface buildCylindricalSurfaceWithEllipticalAxis(int id) {
        CylindricalSurface existing = cylindricalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepCylindricalSurfaceWithEllipticalAxis surface = requireEntity(id, StepCylindricalSurfaceWithEllipticalAxis.class,
                "CYLINDRICAL_SURFACE_WITH_ELLIPTICAL_AXIS");
        double avgRadius = (surface.getSemiAxisA() + surface.getSemiAxisB()) / 2.0;
        CylindricalSurface built = new CylindricalSurface(buildPlacementCallback.apply(surface.getPosition().id()), avgRadius);
        cylindricalSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a ConicalSurface from a STEP CONICAL_SURFACE_WITH_ELLIPTICAL_AXIS entity.
     */
    ConicalSurface buildConicalSurfaceWithEllipticalAxis(int id) {
        ConicalSurface existing = conicalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepConicalSurfaceWithEllipticalAxis surface = requireEntity(id, StepConicalSurfaceWithEllipticalAxis.class,
                "CONICAL_SURFACE_WITH_ELLIPTICAL_AXIS");
        double avgRadius = (surface.getSemiAxisA() + surface.getSemiAxisB()) / 2.0;
        ConicalSurface built = new ConicalSurface(buildPlacementCallback.apply(surface.getPosition().id()), avgRadius, Math.PI / 4);
        conicalSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a SphericalSurface from a STEP SPHERICAL_SURFACE_WITH_ELLIPTICAL_AXIS entity.
     */
    SphericalSurface buildSphericalSurfaceWithEllipticalAxis(int id) {
        SphericalSurface existing = sphericalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepSphericalSurfaceWithEllipticalAxis surface = requireEntity(id, StepSphericalSurfaceWithEllipticalAxis.class,
                "SPHERICAL_SURFACE_WITH_ELLIPTICAL_AXIS");
        SphericalSurface built = new SphericalSurface(buildPlacementCallback.apply(surface.getPosition().id()), surface.getRadius());
        sphericalSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a ToroidalSurface from a STEP TOROIDAL_SURFACE_WITH_CYLINDRICAL_AXIS entity.
     */
    ToroidalSurface buildToroidalSurfaceWithCylindricalAxis(int id) {
        ToroidalSurface existing = toroidalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepToroidalSurfaceWithCylindricalAxis surface = requireEntity(id, StepToroidalSurfaceWithCylindricalAxis.class,
                "TOROIDAL_SURFACE_WITH_CYLINDRICAL_AXIS");
        Axis2Placement3D placement = geometryBuilder.buildAxis1PlacementAsAxis2(surface.getPosition().id());
        ToroidalSurface built = new ToroidalSurface(placement, surface.getMajorRadius(), surface.getMinorRadius());
        toroidalSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a ToroidalSurface from a STEP TOROIDAL_SURFACE_WITH_ELLIPTICAL_AXIS entity.
     */
    ToroidalSurface buildToroidalSurfaceWithEllipticalAxis(int id) {
        ToroidalSurface existing = toroidalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepToroidalSurfaceWithEllipticalAxis surface = requireEntity(id, StepToroidalSurfaceWithEllipticalAxis.class,
                "TOROIDAL_SURFACE_WITH_ELLIPTICAL_AXIS");
        ToroidalSurface built = new ToroidalSurface(buildPlacementCallback.apply(surface.getPosition().id()), surface.getMajorRadius(), surface.getMinorRadius());
        toroidalSurfaces.put(id, built);
        return built;
    }

    // ==================== Trimmed/Bounded Surface Validators ====================

    /**
     * Validates a RECTANGULAR_TRIMMED_SURFACE definition.
     */
    void buildRectangularTrimmedSurface(int id) {
        StepRectangularTrimmedSurface surface = requireEntity(id, StepRectangularTrimmedSurface.class, "RECTANGULAR_TRIMMED_SURFACE");
        buildSupportedSurfaceGeometry(surface.getBasisSurface());
    }

    /**
     * Validates a CURVE_BOUNDED_SURFACE definition.
     */
    void buildCurveBoundedSurface(int id) {
        StepCurveBoundedSurface surface = requireEntity(id, StepCurveBoundedSurface.class, "CURVE_BOUNDED_SURFACE");
        for (StepEntity boundary : surface.boundaries()) {
            buildSurfaceBoundaryCurve(boundary);
        }
        buildSupportedSurfaceGeometry(surface.getBasisSurface());
    }

    /**
     * Validates an ORIENTED_SURFACE definition.
     */
    void buildOrientedSurface(int id) {
        StepOrientedSurface surface = requireEntity(id, StepOrientedSurface.class, "ORIENTED_SURFACE");
        buildSupportedSurfaceGeometry(surface.surfaceElement());
    }

    /**
     * Validates an OFFSET_SURFACE definition.
     */
    void buildOffsetSurface(int id) {
        StepOffsetSurface surface = requireEntity(id, StepOffsetSurface.class, "OFFSET_SURFACE");
        buildSupportedSurfaceGeometry(surface.getBasisSurface());
    }

    // ==================== Surface Geometry Dispatcher ====================

    /**
     * Builds a SurfaceGeometry from a STEP entity, dispatching to the appropriate typed builder.
     *
     * @param geometry the STEP entity
     * @param faceType the face type context for error messages
     * @return the SurfaceGeometry object, or null if unsupported
     */
    SurfaceGeometry buildSupportedFaceGeometry(StepEntity geometry, String faceType) {
        if (geometry instanceof StepPlane) {
            StepPlane plane = (StepPlane) geometry;
            return buildPlane(plane.id());
        }
        if (geometry instanceof StepCylindricalSurface) {
            StepCylindricalSurface cylindricalSurface = (StepCylindricalSurface) geometry;
            return buildCylindricalSurface(cylindricalSurface.id());
        }
        if (geometry instanceof StepConicalSurface) {
            StepConicalSurface conicalSurface = (StepConicalSurface) geometry;
            return buildConicalSurface(conicalSurface.id());
        }
        if (geometry instanceof StepSphericalSurface) {
            StepSphericalSurface sphericalSurface = (StepSphericalSurface) geometry;
            return buildSphericalSurface(sphericalSurface.id());
        }
        if (geometry instanceof StepToroidalSurface) {
            StepToroidalSurface toroidalSurface = (StepToroidalSurface) geometry;
            return buildToroidalSurface(toroidalSurface.id());
        }
        if (geometry instanceof StepToroidalSurfaceWithSpecifiedBends) {
            StepToroidalSurfaceWithSpecifiedBends toroidalSpecBends = (StepToroidalSurfaceWithSpecifiedBends) geometry;
            return buildToroidalSurfaceFromSpecifiedBends(toroidalSpecBends);
        }
        if (geometry instanceof StepDegenerateToroidalSurface) {
            StepDegenerateToroidalSurface degenerateToroidalSurface = (StepDegenerateToroidalSurface) geometry;
            return buildDegenerateToroidalSurface(degenerateToroidalSurface.id());
        }
        if (geometry instanceof StepSurfaceOfLinearExtrusion) {
            StepSurfaceOfLinearExtrusion extrusionSurface = (StepSurfaceOfLinearExtrusion) geometry;
            return buildSurfaceOfLinearExtrusion(extrusionSurface.id());
        }
        if (geometry instanceof StepSurfaceOfRevolution) {
            StepSurfaceOfRevolution revolutionSurface = (StepSurfaceOfRevolution) geometry;
            return buildSurfaceOfRevolution(revolutionSurface.id());
        }
        if (geometry instanceof StepBezierSurface) {
            StepBezierSurface splineSurface = (StepBezierSurface) geometry;
            return buildBezierSurface(splineSurface.id());
        }
        if (geometry instanceof StepUniformSurface) {
            StepUniformSurface splineSurface = (StepUniformSurface) geometry;
            return buildUniformSurface(splineSurface.id());
        }
        if (geometry instanceof StepQuasiUniformSurface) {
            StepQuasiUniformSurface splineSurface = (StepQuasiUniformSurface) geometry;
            return buildQuasiUniformSurface(splineSurface.id());
        }
        if (geometry instanceof StepPiecewiseBezierSurface) {
            StepPiecewiseBezierSurface splineSurface = (StepPiecewiseBezierSurface) geometry;
            return buildPiecewiseBezierSurface(splineSurface.id());
        }
        if (geometry instanceof StepBSplineSurfaceWithKnots) {
            StepBSplineSurfaceWithKnots splineSurface = (StepBSplineSurfaceWithKnots) geometry;
            return buildBSplineSurface(splineSurface.id());
        }
        if (geometry instanceof StepBSplineSurface) {
            StepBSplineSurface splineSurface = (StepBSplineSurface) geometry;
            return buildGenericBSplineSurface(splineSurface.id());
        }
        if (geometry instanceof StepRationalBSplineSurface) {
            StepRationalBSplineSurface rationalSplineSurface = (StepRationalBSplineSurface) geometry;
            return buildRationalBSplineSurface(rationalSplineSurface.id());
        }
        if (geometry instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) geometry;
            buildRectangularTrimmedSurface(trimmedSurface.id());
            return buildSupportedFaceGeometry(trimmedSurface.getBasisSurface(), faceType);
        }
        if (geometry instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) geometry;
            for (StepEntity boundary : boundedSurface.boundaries()) {
                if (boundary instanceof StepPcurve) {
                    StepPcurve pcurve = (StepPcurve) boundary;
                    buildPcurve2Callback.apply(pcurve.id());
                } else if (boundary instanceof StepCompositeCurveOnSurface) {
                    StepCompositeCurveOnSurface compositeCurveOnSurface = (StepCompositeCurveOnSurface) boundary;
                    boolean built2d = true;
                    for (StepCompositeCurveSegment segment : compositeCurveOnSurface.getSegments()) {
                        try {
                            buildCurve2Callback.apply(segment.parentCurve().id());
                        } catch (UnsupportedGeometryException ex) {
                            built2d = false;
                            break;
                        }
                    }
                    if (!built2d) {
                        buildCompositeCurveCallback.apply(compositeCurveOnSurface.id());
                    }
                } else {
                    buildCurve3Callback.apply(boundary.id());
                }
            }
            return buildSupportedFaceGeometry(boundedSurface.getBasisSurface(), faceType);
        }
        if (geometry instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) geometry;
            SurfaceGeometry base = buildSupportedFaceGeometry(orientedSurface.surfaceElement(), faceType);
            if (base == null) {
                return null;
            }
            if (!orientedSurface.isOrientation()) {
                return reverseSurfaceSense(base);
            }
            return base;
        }
        if (geometry instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) geometry;
            return offsetSupportedSurfaceGeometry(offsetSurface, faceType);
        }
        if (geometry instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) geometry).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) geometry;
            String replicaRestriction = geometryOps.unsupportedReplicaSurfaceTransformation(replica.transformation());
            if (replicaRestriction != null) {
                return null;
            }
            SurfaceGeometry base = buildSupportedFaceGeometry(replica.parent(), faceType);
            if (base == null) {
                return null;
            }
            return geometryOps.transformSurfaceGeometry(base, replica.transformation());
        }
        if (geometry instanceof StepRuledSurface) {
            StepRuledSurface ruledSurface = (StepRuledSurface) geometry;
            return buildRuledSurfaceGeometry(ruledSurface);
        }
        if (geometry instanceof StepSurfaceOfConstantRadius) {
            StepSurfaceOfConstantRadius constantRadiusSurface = (StepSurfaceOfConstantRadius) geometry;
            return buildSurfaceOfConstantRadiusGeometry(constantRadiusSurface, faceType);
        }
        if (geometry instanceof StepSurfacePatch) {
            StepSurfacePatch surfacePatch = (StepSurfacePatch) geometry;
            return buildSurfacePatchGeometry(surfacePatch, faceType);
        }
        if (geometry instanceof StepRectangularCompositeSurface) {
            StepRectangularCompositeSurface compositeSurface = (StepRectangularCompositeSurface) geometry;
            return buildRectangularCompositeSurfaceGeometry(compositeSurface, faceType);
        }
        if (geometry instanceof StepCylindricalSurfaceWithEllipticalAxis) {
            StepCylindricalSurfaceWithEllipticalAxis ellipticalCyl = (StepCylindricalSurfaceWithEllipticalAxis) geometry;
            return buildCylindricalSurfaceWithEllipticalAxis(ellipticalCyl.id());
        }
        if (geometry instanceof StepConicalSurfaceWithEllipticalAxis) {
            StepConicalSurfaceWithEllipticalAxis ellipticalCone = (StepConicalSurfaceWithEllipticalAxis) geometry;
            return buildConicalSurfaceWithEllipticalAxis(ellipticalCone.id());
        }
        if (geometry instanceof StepSphericalSurfaceWithEllipticalAxis) {
            StepSphericalSurfaceWithEllipticalAxis ellipticalSphere = (StepSphericalSurfaceWithEllipticalAxis) geometry;
            return buildSphericalSurfaceWithEllipticalAxis(ellipticalSphere.id());
        }
        if (geometry instanceof StepToroidalSurfaceWithCylindricalAxis) {
            StepToroidalSurfaceWithCylindricalAxis toroidalCyl = (StepToroidalSurfaceWithCylindricalAxis) geometry;
            return buildToroidalSurfaceWithCylindricalAxis(toroidalCyl.id());
        }
        if (geometry instanceof StepToroidalSurfaceWithEllipticalAxis) {
            StepToroidalSurfaceWithEllipticalAxis toroidalElliptical = (StepToroidalSurfaceWithEllipticalAxis) geometry;
            return buildToroidalSurfaceWithEllipticalAxis(toroidalElliptical.id());
        }
        if (geometry instanceof StepBSplineSurfaceWithKnotsAndBreakpoints) {
            StepBSplineSurfaceWithKnotsAndBreakpoints splineBreakpoints = (StepBSplineSurfaceWithKnotsAndBreakpoints) geometry;
            return buildBSplineSurfaceWithBreakpoints(splineBreakpoints.id());
        }
        if (geometry instanceof StepOffsetSurface2) {
            StepOffsetSurface2 offsetSurface2 = (StepOffsetSurface2) geometry;
            return buildOffsetSurface2Geometry(offsetSurface2, faceType);
        }
        if (geometry instanceof StepBlendedSurface) {
            StepBlendedSurface blended = (StepBlendedSurface) geometry;
            return buildBlendedSurface(blended, faceType);
        }
        if (geometry instanceof StepFreeFormSurface) {
            StepFreeFormSurface freeForm = (StepFreeFormSurface) geometry;
            return buildFreeFormSurface(freeForm);
        }
        if (geometry instanceof StepMachinedSurface) {
            StepMachinedSurface machinedSurface = (StepMachinedSurface) geometry;
            return buildSupportedFaceGeometry(machinedSurface.face(), faceType);
        }
        if (geometry instanceof StepBoundedSurface) {
            StepBoundedSurface boundedSurface = (StepBoundedSurface) geometry;
            StepEntity actual = entitiesById.get(boundedSurface.id());
            if (actual != null && actual != boundedSurface) {
                return buildSupportedFaceGeometry(actual, faceType);
            }
            return null;
        }
        if (geometry instanceof StepSurface) {
            StepSurface surface = (StepSurface) geometry;
            StepEntity actual = entitiesById.get(surface.id());
            if (actual != null && actual != surface) {
                return buildSupportedFaceGeometry(actual, faceType);
            }
            return null;
        }
        if (geometry instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) geometry;
            return buildSupportedFaceGeometry(mappedItem.mappingTarget(), faceType);
        }
        if (geometry instanceof StepParaboloidSurface) {
            StepParaboloidSurface paraboloid = (StepParaboloidSurface) geometry;
            return buildParaboloidSurface(paraboloid.id());
        }
        if (geometry instanceof StepHyperboloidSurface) {
            StepHyperboloidSurface hyperboloid = (StepHyperboloidSurface) geometry;
            return buildHyperboloidSurface(hyperboloid.id());
        }
        if (geometry instanceof StepSurfaceOfTranslation) {
            StepSurfaceOfTranslation translation = (StepSurfaceOfTranslation) geometry;
            return buildSurfaceOfTranslation(translation.id());
        }
        if (geometry instanceof StepSurfaceOfProjection) {
            StepSurfaceOfProjection projection = (StepSurfaceOfProjection) geometry;
            return buildSurfaceOfProjection(projection.id());
        }
        return null;
    }

    /**
     * Validates a supported surface geometry (for validation-only paths).
     */
    void buildSupportedSurfaceGeometry(StepEntity geometry) {
        if (geometry instanceof StepPlane) {
            StepPlane plane = (StepPlane) geometry;
            buildPlane(plane.id());
            return;
        }
        if (geometry instanceof StepCylindricalSurface) {
            StepCylindricalSurface cylindricalSurface = (StepCylindricalSurface) geometry;
            buildCylindricalSurface(cylindricalSurface.id());
            return;
        }
        if (geometry instanceof StepConicalSurface) {
            StepConicalSurface conicalSurface = (StepConicalSurface) geometry;
            buildConicalSurface(conicalSurface.id());
            return;
        }
        if (geometry instanceof StepSphericalSurface) {
            StepSphericalSurface sphericalSurface = (StepSphericalSurface) geometry;
            buildSphericalSurface(sphericalSurface.id());
            return;
        }
        if (geometry instanceof StepToroidalSurface) {
            StepToroidalSurface toroidalSurface = (StepToroidalSurface) geometry;
            buildToroidalSurface(toroidalSurface.id());
            return;
        }
        if (geometry instanceof StepToroidalSurfaceWithSpecifiedBends) {
            StepToroidalSurfaceWithSpecifiedBends toroidalSpecBends = (StepToroidalSurfaceWithSpecifiedBends) geometry;
            buildToroidalSurfaceFromSpecifiedBends(toroidalSpecBends);
            return;
        }
        if (geometry instanceof StepDegenerateToroidalSurface) {
            StepDegenerateToroidalSurface degenerateToroidalSurface = (StepDegenerateToroidalSurface) geometry;
            buildDegenerateToroidalSurface(degenerateToroidalSurface.id());
            return;
        }
        if (geometry instanceof StepSurfaceOfLinearExtrusion) {
            StepSurfaceOfLinearExtrusion extrusionSurface = (StepSurfaceOfLinearExtrusion) geometry;
            buildSurfaceOfLinearExtrusion(extrusionSurface.id());
            return;
        }
        if (geometry instanceof StepSurfaceOfRevolution) {
            StepSurfaceOfRevolution revolutionSurface = (StepSurfaceOfRevolution) geometry;
            buildSurfaceOfRevolution(revolutionSurface.id());
            return;
        }
        if (geometry instanceof StepBezierSurface) {
            StepBezierSurface splineSurface = (StepBezierSurface) geometry;
            buildBezierSurface(splineSurface.id());
            return;
        }
        if (geometry instanceof StepUniformSurface) {
            StepUniformSurface splineSurface = (StepUniformSurface) geometry;
            buildUniformSurface(splineSurface.id());
            return;
        }
        if (geometry instanceof StepQuasiUniformSurface) {
            StepQuasiUniformSurface splineSurface = (StepQuasiUniformSurface) geometry;
            buildQuasiUniformSurface(splineSurface.id());
            return;
        }
        if (geometry instanceof StepPiecewiseBezierSurface) {
            StepPiecewiseBezierSurface splineSurface = (StepPiecewiseBezierSurface) geometry;
            buildPiecewiseBezierSurface(splineSurface.id());
            return;
        }
        if (geometry instanceof StepBSplineSurfaceWithKnots) {
            StepBSplineSurfaceWithKnots splineSurface = (StepBSplineSurfaceWithKnots) geometry;
            buildBSplineSurface(splineSurface.id());
            return;
        }
        if (geometry instanceof StepBSplineSurface) {
            StepBSplineSurface splineSurface = (StepBSplineSurface) geometry;
            buildGenericBSplineSurface(splineSurface.id());
            return;
        }
        if (geometry instanceof StepRationalBSplineSurface) {
            StepRationalBSplineSurface rationalSplineSurface = (StepRationalBSplineSurface) geometry;
            buildRationalBSplineSurface(rationalSplineSurface.id());
            return;
        }
        if (geometry instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) geometry;
            buildRectangularTrimmedSurface(trimmedSurface.id());
            return;
        }
        if (geometry instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) geometry;
            buildCurveBoundedSurface(boundedSurface.id());
            return;
        }
        if (geometry instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) geometry;
            buildOrientedSurface(orientedSurface.id());
            return;
        }
        if (geometry instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) geometry;
            buildOffsetSurface(offsetSurface.id());
            return;
        }
        if (geometry instanceof StepParaboloidSurface) {
            StepParaboloidSurface paraboloid = (StepParaboloidSurface) geometry;
            buildParaboloidSurface(paraboloid.id());
            return;
        }
        if (geometry instanceof StepHyperboloidSurface) {
            StepHyperboloidSurface hyperboloid = (StepHyperboloidSurface) geometry;
            buildHyperboloidSurface(hyperboloid.id());
            return;
        }
        if (geometry instanceof StepSurfaceOfTranslation) {
            StepSurfaceOfTranslation translation = (StepSurfaceOfTranslation) geometry;
            buildSurfaceOfTranslation(translation.id());
            return;
        }
        if (geometry instanceof StepSurfaceOfProjection) {
            StepSurfaceOfProjection projection = (StepSurfaceOfProjection) geometry;
            buildSurfaceOfProjection(projection.id());
            return;
        }
        // Extended surface types
        if (geometry instanceof StepRuledSurface) {
            StepRuledSurface ruledSurface = (StepRuledSurface) geometry;
            buildRuledSurfaceGeometry(ruledSurface);
            return;
        }
        if (geometry instanceof StepSurfaceOfConstantRadius) {
            StepSurfaceOfConstantRadius constantRadiusSurface = (StepSurfaceOfConstantRadius) geometry;
            buildSurfaceOfConstantRadiusGeometry(constantRadiusSurface, "SURFACE");
            return;
        }
        if (geometry instanceof StepSurfacePatch) {
            StepSurfacePatch surfacePatch = (StepSurfacePatch) geometry;
            buildSurfacePatchGeometry(surfacePatch, "SURFACE");
            return;
        }
        if (geometry instanceof StepRectangularCompositeSurface) {
            StepRectangularCompositeSurface compositeSurface = (StepRectangularCompositeSurface) geometry;
            buildRectangularCompositeSurfaceGeometry(compositeSurface, "SURFACE");
            return;
        }
        if (geometry instanceof StepCylindricalSurfaceWithEllipticalAxis) {
            StepCylindricalSurfaceWithEllipticalAxis ellipticalCyl = (StepCylindricalSurfaceWithEllipticalAxis) geometry;
            buildCylindricalSurfaceWithEllipticalAxis(ellipticalCyl.id());
            return;
        }
        if (geometry instanceof StepConicalSurfaceWithEllipticalAxis) {
            StepConicalSurfaceWithEllipticalAxis ellipticalCone = (StepConicalSurfaceWithEllipticalAxis) geometry;
            buildConicalSurfaceWithEllipticalAxis(ellipticalCone.id());
            return;
        }
        if (geometry instanceof StepSphericalSurfaceWithEllipticalAxis) {
            StepSphericalSurfaceWithEllipticalAxis ellipticalSphere = (StepSphericalSurfaceWithEllipticalAxis) geometry;
            buildSphericalSurfaceWithEllipticalAxis(ellipticalSphere.id());
            return;
        }
        if (geometry instanceof StepToroidalSurfaceWithCylindricalAxis) {
            StepToroidalSurfaceWithCylindricalAxis toroidalCyl = (StepToroidalSurfaceWithCylindricalAxis) geometry;
            buildToroidalSurfaceWithCylindricalAxis(toroidalCyl.id());
            return;
        }
        if (geometry instanceof StepToroidalSurfaceWithEllipticalAxis) {
            StepToroidalSurfaceWithEllipticalAxis toroidalElliptical = (StepToroidalSurfaceWithEllipticalAxis) geometry;
            buildToroidalSurfaceWithEllipticalAxis(toroidalElliptical.id());
            return;
        }
        if (geometry instanceof StepBSplineSurfaceWithKnotsAndBreakpoints) {
            StepBSplineSurfaceWithKnotsAndBreakpoints splineBreakpoints = (StepBSplineSurfaceWithKnotsAndBreakpoints) geometry;
            buildBSplineSurfaceWithBreakpoints(splineBreakpoints.id());
            return;
        }
        if (geometry instanceof StepOffsetSurface2) {
            StepOffsetSurface2 offsetSurface2 = (StepOffsetSurface2) geometry;
            buildOffsetSurface2Geometry(offsetSurface2, "SURFACE");
            return;
        }
        if (geometry instanceof StepBlendedSurface) {
            StepBlendedSurface blended = (StepBlendedSurface) geometry;
            buildBlendedSurface(blended, "SURFACE");
            return;
        }
        if (geometry instanceof StepFreeFormSurface) {
            StepFreeFormSurface freeForm = (StepFreeFormSurface) geometry;
            buildFreeFormSurface(freeForm);
            return;
        }
        if (geometry instanceof StepBoundedSurface) {
            StepBoundedSurface boundedSurface = (StepBoundedSurface) geometry;
            StepEntity actual = entitiesById.get(boundedSurface.id());
            if (actual != null && actual != boundedSurface) {
                buildSupportedSurfaceGeometry(actual);
            }
            return;
        }
    }

    // ==================== Private Surface Geometry Helpers ====================

    private SurfaceGeometry buildRuledSurfaceGeometry(StepRuledSurface ruledSurface) {
        RuledSurface3 existing = ruledSurfaces.get(ruledSurface.id());
        if (existing != null) {
            return existing;
        }
        Axis2Placement3D position = buildPlacementCallback.apply(ruledSurface.getPosition().id());
        Curve3 directrix1 = buildCurve3Callback.apply(ruledSurface.getDirectrix1().id());
        Curve3 directrix2 = buildCurve3Callback.apply(ruledSurface.getDirectrix2().id());
        RuledSurface3 built = new RuledSurface3(directrix1, directrix2);
        ruledSurfaces.put(ruledSurface.id(), built);
        return built;
    }

    private SurfaceGeometry buildSurfaceOfConstantRadiusGeometry(StepSurfaceOfConstantRadius surface, String faceType) {
        SurfaceOfConstantRadius3 existing = constantRadiusSurfaces.get(surface.id());
        if (existing != null) {
            return existing;
        }
        SurfaceGeometry sweptSurface = buildSupportedFaceGeometry(surface.getSweptSurface(), faceType);
        if (sweptSurface == null) {
            return null;
        }
        double radius = surface.getRadius();
        if (radius <= 0.0) {
            return null;
        }
        SurfaceOfConstantRadius3 built = new SurfaceOfConstantRadius3(sweptSurface, radius);
        constantRadiusSurfaces.put(surface.id(), built);
        return built;
    }

    private SurfaceGeometry buildSurfacePatchGeometry(StepSurfacePatch patch, String faceType) {
        SurfaceGeometry basisSurface = buildSupportedFaceGeometry(patch.getBasisSurface(), faceType);
        if (basisSurface == null) {
            return null;
        }
        if (!patch.isSameSense()) {
            if (basisSurface instanceof Plane) {
                Plane plane = (Plane) basisSurface;
                return new Plane(plane.getOrigin(), plane.getNormal().reverse());
            }
        }
        return basisSurface;
    }

    private SurfaceGeometry buildRectangularCompositeSurfaceGeometry(StepRectangularCompositeSurface surface, String faceType) {
        SurfaceGeometry parentSurface = buildSupportedFaceGeometry(surface.parentSurface(), faceType);
        if (parentSurface == null) {
            return null;
        }
        return parentSurface;
    }

    private SurfaceGeometry offsetSupportedSurfaceGeometry(StepOffsetSurface offsetSurface, String faceType) {
        buildOffsetSurface(offsetSurface.id());
        SurfaceGeometry base = buildSupportedFaceGeometry(offsetSurface.getBasisSurface(), faceType);
        if (base == null) {
            return null;
        }
        if (base instanceof Plane) {
            Plane plane = (Plane) base;
            return new Plane(
                    plane.getOrigin().add(plane.getNormal().asVector().scale(offsetSurface.getDistance())),
                    plane.getNormal());
        }
        if (base instanceof CylindricalSurface) {
            CylindricalSurface cylindricalSurface = (CylindricalSurface) base;
            return new CylindricalSurface(
                    cylindricalSurface.getPosition(),
                    cylindricalSurface.getRadius() + offsetSurface.getDistance());
        }
        if (base instanceof SphericalSurface) {
            SphericalSurface sphericalSurface = (SphericalSurface) base;
            return new SphericalSurface(
                    sphericalSurface.getPosition(),
                    sphericalSurface.getRadius() + offsetSurface.getDistance());
        }
        if (base instanceof ConicalSurface) {
            ConicalSurface conicalSurface = (ConicalSurface) base;
            return offsetConicalSurface(conicalSurface, offsetSurface.getDistance());
        }
        if (base instanceof ToroidalSurface) {
            ToroidalSurface toroidalSurface = (ToroidalSurface) base;
            return new ToroidalSurface(
                    toroidalSurface.getPosition(),
                    toroidalSurface.getMajorRadius(),
                    toroidalSurface.getMinorRadius() + offsetSurface.getDistance());
        }
        if (base instanceof OffsetSurface3) {
            OffsetSurface3 nestedOffsetSurface = (OffsetSurface3) base;
            return new OffsetSurface3(
                    nestedOffsetSurface.getBasisSurface(),
                    nestedOffsetSurface.getDistance() + offsetSurface.getDistance());
        }
        return new OffsetSurface3(base, offsetSurface.getDistance());
    }

    private ConicalSurface offsetConicalSurface(ConicalSurface conicalSurface, double distance) {
        double semiAngle = conicalSurface.getSemiAngle();
        double radialOffset = distance * Math.cos(semiAngle);
        double axisOffset = -distance * Math.sin(semiAngle);
        Axis2Placement3D position = conicalSurface.getPosition();
        return new ConicalSurface(
                new Axis2Placement3D(
                        position.getLocation().add(position.getAxis().asVector().scale(axisOffset)),
                        position.getAxis(),
                        position.getRefDirection()),
                conicalSurface.getRadius() + radialOffset,
                semiAngle);
    }

    private SurfaceGeometry buildOffsetSurface2Geometry(StepOffsetSurface2 offsetSurface2, String faceType) {
        SurfaceGeometry base = buildSupportedFaceGeometry(offsetSurface2.getBasisSurface(), faceType);
        if (base == null) {
            return null;
        }
        if (base instanceof Plane) {
            Plane plane = (Plane) base;
            return new Plane(
                    plane.getOrigin().add(plane.getNormal().asVector().scale(offsetSurface2.getDistance())),
                    plane.getNormal());
        }
        if (base instanceof CylindricalSurface) {
            CylindricalSurface cylindricalSurface = (CylindricalSurface) base;
            return new CylindricalSurface(
                    cylindricalSurface.getPosition(),
                    cylindricalSurface.getRadius() + offsetSurface2.getDistance());
        }
        if (base instanceof SphericalSurface) {
            SphericalSurface sphericalSurface = (SphericalSurface) base;
            return new SphericalSurface(
                    sphericalSurface.getPosition(),
                    sphericalSurface.getRadius() + offsetSurface2.getDistance());
        }
        if (base instanceof ConicalSurface) {
            ConicalSurface conicalSurface = (ConicalSurface) base;
            return offsetConicalSurface(conicalSurface, offsetSurface2.getDistance());
        }
        if (base instanceof ToroidalSurface) {
            ToroidalSurface toroidalSurface = (ToroidalSurface) base;
            return new ToroidalSurface(
                    toroidalSurface.getPosition(),
                    toroidalSurface.getMajorRadius(),
                    toroidalSurface.getMinorRadius() + offsetSurface2.getDistance());
        }
        if (base instanceof OffsetSurface3) {
            OffsetSurface3 nestedOffsetSurface = (OffsetSurface3) base;
            return new OffsetSurface3(
                    nestedOffsetSurface.getBasisSurface(),
                    nestedOffsetSurface.getDistance() + offsetSurface2.getDistance());
        }
        return new OffsetSurface3(base, offsetSurface2.getDistance());
    }

    private SurfaceGeometry buildBlendedSurface(StepBlendedSurface blended, String faceType) {
        SurfaceGeometry primary = buildSupportedFaceGeometry(blended.primarySurface(), faceType);
        if (primary != null) {
            return primary;
        }
        return buildSupportedFaceGeometry(blended.secondarySurface(), faceType);
    }

    private SurfaceGeometry buildFreeFormSurface(StepFreeFormSurface surface) {
        int degreeU = surface.degreeU();
        int degreeV = surface.degreeV();
        if (degreeU <= 0 || degreeV <= 0) {
            return null;
        }
        List<List<StepEntity>> controlPoints = surface.getControlPoints();
        if (controlPoints.isEmpty() || controlPoints.get(0).isEmpty()) {
            return null;
        }
        int nRows = controlPoints.size();
        int nCols = controlPoints.get(0).size();

        List<List<CartesianPoint>> grid = new ArrayList<>(nRows);
        for (List<StepEntity> row : controlPoints) {
            List<CartesianPoint> builtRow = new ArrayList<>(row.size());
            for (StepEntity cp : row) {
                if (cp instanceof StepCartesianPoint) {
                    StepCartesianPoint point = (StepCartesianPoint) cp;
                    builtRow.add(geometryBuilder.buildPoint(point.id()));
                } else {
                    return null;
                }
            }
            grid.add(builtRow);
        }

        List<Double> knotU = null;
        List<Double> knotV = null;
        List<Double> providedKnots = surface.knotVectors();
        if (providedKnots != null && providedKnots.size() >= nRows + degreeU + 1) {
            knotU = providedKnots.subList(0, nRows + degreeU + 1);
        }
        if (providedKnots != null && providedKnots.size() >= nRows + nCols + degreeV + 1) {
            knotV = providedKnots.subList(nRows + degreeU + 1, nRows + nCols + degreeV + 1);
        }

        if (knotU == null) {
            knotU = generateUniformKnots(nRows, degreeU);
        }
        if (knotV == null) {
            knotV = generateUniformKnots(nCols, degreeV);
        }

        try {
            List<Integer> multU = generateUniformMultiplicities(knotU.size());
            List<Integer> multV = generateUniformMultiplicities(knotV.size());
            return new BSplineSurface3(degreeU, degreeV, grid, multU, multV, knotU, knotV);
        } catch (Exception e) {
            return null;
        }
    }

    // ==================== Surface Sense Reversal ====================

    /**
     * Reverses the surface sense (normal direction) for oriented surfaces.
     */
    SurfaceGeometry reverseSurfaceSense(SurfaceGeometry surface) {
        if (surface instanceof Plane) {
            Plane plane = (Plane) surface;
            return new Plane(plane.getOrigin(), plane.getNormal().reverse());
        }
        if (surface instanceof CylindricalSurface) {
            CylindricalSurface cyl = (CylindricalSurface) surface;
            Axis2Placement3D p = cyl.getPosition();
            return new CylindricalSurface(
                    new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                    cyl.getRadius());
        }
        if (surface instanceof ConicalSurface) {
            ConicalSurface conic = (ConicalSurface) surface;
            Axis2Placement3D p = conic.getPosition();
            return new ConicalSurface(
                    new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                    conic.getRadius(), conic.getSemiAngle());
        }
        if (surface instanceof SphericalSurface) {
            SphericalSurface sphere = (SphericalSurface) surface;
            Axis2Placement3D p = sphere.getPosition();
            return new SphericalSurface(
                    new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                    sphere.getRadius());
        }
        if (surface instanceof ToroidalSurface) {
            ToroidalSurface torus = (ToroidalSurface) surface;
            Axis2Placement3D p = torus.getPosition();
            return new ToroidalSurface(
                    new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                    torus.getMajorRadius(), torus.getMinorRadius());
        }
        if (surface instanceof SurfaceOfLinearExtrusion3) {
            SurfaceOfLinearExtrusion3 extrusion = (SurfaceOfLinearExtrusion3) surface;
            return new SurfaceOfLinearExtrusion3(extrusion.getSweptCurve(), extrusion.getExtrusionVector().negate());
        }
        if (surface instanceof SurfaceOfRevolution3) {
            SurfaceOfRevolution3 revolution = (SurfaceOfRevolution3) surface;
            return new SurfaceOfRevolution3(
                    revolution.getSweptCurve(),
                    revolution.getAxisOrigin(),
                    revolution.getAxisDirection().reverse());
        }
        if (surface instanceof RuledSurface3) {
            RuledSurface3 ruled = (RuledSurface3) surface;
            return new RuledSurface3(
                    reverseCurve3(ruled.getDirectrix1()),
                    reverseCurve3(ruled.getDirectrix2()));
        }
        if (surface instanceof SurfaceOfConstantRadius3) {
            SurfaceOfConstantRadius3 constant = (SurfaceOfConstantRadius3) surface;
            return new SurfaceOfConstantRadius3(
                    reverseSurfaceSense(constant.getSweptSurface()),
                    constant.getRadius());
        }
        if (surface instanceof OffsetSurface3) {
            OffsetSurface3 offset = (OffsetSurface3) surface;
            return new OffsetSurface3(
                    reverseSurfaceSense(offset.getBasisSurface()),
                    offset.getDistance());
        }
        if (surface instanceof BSplineSurface3) {
            BSplineSurface3 bspline = (BSplineSurface3) surface;
            return new BSplineSurface3(
                    bspline.getUDegree(),
                    bspline.getVDegree(),
                    reverseBSplineControlGrid(bspline.getControlPoints()),
                    bspline.getUMultiplicities(),
                    bspline.getVMultiplicities(),
                    bspline.getUKnots(),
                    bspline.getVKnots());
        }
        if (surface instanceof RationalBSplineSurface3) {
            RationalBSplineSurface3 rational = (RationalBSplineSurface3) surface;
            return new RationalBSplineSurface3(
                    rational.getUDegree(),
                    rational.getVDegree(),
                    reverseBSplineControlGrid(rational.getControlPoints()),
                    rational.getWeightsData(),
                    rational.getUMultiplicities(),
                    rational.getVMultiplicities(),
                    rational.getUKnots(),
                    rational.getVKnots());
        }
        if (surface instanceof ParaboloidSurface) {
            ParaboloidSurface paraboloid = (ParaboloidSurface) surface;
            Axis2Placement3D pp = paraboloid.getPosition();
            return new ParaboloidSurface(
                    new Axis2Placement3D(pp.getLocation(), pp.getAxis(), pp.xDirection().reverse()),
                    paraboloid.getFocalLength());
        }
        if (surface instanceof HyperboloidSurface) {
            HyperboloidSurface hyperboloid = (HyperboloidSurface) surface;
            Axis2Placement3D hp = hyperboloid.getPosition();
            return new HyperboloidSurface(
                    new Axis2Placement3D(hp.getLocation(), hp.getAxis(), hp.xDirection().reverse()),
                    hyperboloid.getRadius(), hyperboloid.getSemiAxis());
        }
        if (surface instanceof SurfaceOfTranslation3) {
            SurfaceOfTranslation3 translation = (SurfaceOfTranslation3) surface;
            return new SurfaceOfTranslation3(
                    reverseCurve3(translation.getProfile()),
                    translation.getDirection());
        }
        if (surface instanceof SurfaceOfProjection3) {
            SurfaceOfProjection3 projection = (SurfaceOfProjection3) surface;
            return new SurfaceOfProjection3(
                    reverseCurve3(projection.getProfile()),
                    projection.getProjectionDirection());
        }
        return surface;
    }

    private static List<List<CartesianPoint>> reverseBSplineControlGrid(List<List<CartesianPoint>> grid) {
        List<List<CartesianPoint>> result = new ArrayList<>(grid.size());
        for (List<CartesianPoint> row : grid) {
            result.add(reverseList(row));
        }
        return List.copyOf(result);
    }

    private static <T> List<T> reverseList(List<T> list) {
        List<T> reversed = new ArrayList<>(list.size());
        for (int i = list.size() - 1; i >= 0; i--) {
            reversed.add(list.get(i));
        }
        return reversed;
    }

    /**
     * Reverses a Curve3. This is a placeholder that delegates to a callback.
     * Full implementation will be provided through StepCadBuilder integration.
     */
    private Curve3 reverseCurve3(Curve3 curve) {
        // For now, return the curve unchanged - full implementation requires
        // integration with StepCadBuilder which has the complete reverseCurve3 method
        return curve;
    }

    // ==================== Implicit B-Spline Surface Data ====================

    private ImplicitBSplineSurfaceData implicitBSplineSurfaceData(StepEntity entity) {
        if (entity instanceof StepBezierSurface) {
            StepBezierSurface surface = (StepBezierSurface) entity;
            return implicitBezierSurface(surface.getUDegree(), surface.getVDegree(), surface.getControlPoints(), stepEntityTypeName(entity));
        }
        if (entity instanceof StepUniformSurface) {
            StepUniformSurface surface = (StepUniformSurface) entity;
            return implicitUniformSurface(surface.getUDegree(), surface.getVDegree(), surface.getControlPoints(), stepEntityTypeName(entity));
        }
        if (entity instanceof StepQuasiUniformSurface) {
            StepQuasiUniformSurface surface = (StepQuasiUniformSurface) entity;
            return implicitQuasiUniformSurface(surface.getUDegree(), surface.getVDegree(), surface.getControlPoints(), stepEntityTypeName(entity));
        }
        if (entity instanceof StepPiecewiseBezierSurface) {
            StepPiecewiseBezierSurface surface = (StepPiecewiseBezierSurface) entity;
            return implicitPiecewiseBezierSurface(surface.getUDegree(), surface.getVDegree(), surface.getControlPoints(), stepEntityTypeName(entity));
        }
        throw new UnsupportedGeometryException(stepEntityTypeName(entity) + " implicit knot data is unsupported");
    }

    private ImplicitBSplineSurfaceData implicitBezierSurface(
            int uDegree, int vDegree,
            List<List<StepCartesianPoint>> controlPoints,
            String typeName) {
        validateImplicitSurfaceData(uDegree, vDegree, controlPoints, typeName);
        if (controlPoints.size() != uDegree + 1 || controlPoints.get(0).size() != vDegree + 1) {
            throw new UnsupportedGeometryException(typeName + " requires controlPointCount = degree + 1 in both directions");
        }
        return new ImplicitBSplineSurfaceData(
                uDegree, vDegree, controlPoints,
                List.of(uDegree + 1, uDegree + 1),
                List.of(vDegree + 1, vDegree + 1),
                List.of(0.0, 1.0),
                List.of(0.0, 1.0));
    }

    private ImplicitBSplineSurfaceData implicitUniformSurface(
            int uDegree, int vDegree,
            List<List<StepCartesianPoint>> controlPoints,
            String typeName) {
        validateImplicitSurfaceData(uDegree, vDegree, controlPoints, typeName);
        return new ImplicitBSplineSurfaceData(
                uDegree, vDegree, controlPoints,
                uniformMultiplicities(controlPoints.size(), uDegree),
                uniformMultiplicities(controlPoints.get(0).size(), vDegree),
                uniformKnots(controlPoints.size(), uDegree),
                uniformKnots(controlPoints.get(0).size(), vDegree));
    }

    private ImplicitBSplineSurfaceData implicitQuasiUniformSurface(
            int uDegree, int vDegree,
            List<List<StepCartesianPoint>> controlPoints,
            String typeName) {
        validateImplicitSurfaceData(uDegree, vDegree, controlPoints, typeName);
        return new ImplicitBSplineSurfaceData(
                uDegree, vDegree, controlPoints,
                quasiUniformMultiplicities(controlPoints.size(), uDegree),
                quasiUniformMultiplicities(controlPoints.get(0).size(), vDegree),
                quasiUniformKnots(controlPoints.size(), uDegree),
                quasiUniformKnots(controlPoints.get(0).size(), vDegree));
    }

    private ImplicitBSplineSurfaceData implicitPiecewiseBezierSurface(
            int uDegree, int vDegree,
            List<List<StepCartesianPoint>> controlPoints,
            String typeName) {
        validateImplicitSurfaceData(uDegree, vDegree, controlPoints, typeName);
        return new ImplicitBSplineSurfaceData(
                uDegree, vDegree, controlPoints,
                piecewiseBezierMultiplicities(controlPoints.size(), uDegree, typeName + " U"),
                piecewiseBezierMultiplicities(controlPoints.get(0).size(), vDegree, typeName + " V"),
                piecewiseBezierKnots(controlPoints.size(), uDegree, typeName + " U"),
                piecewiseBezierKnots(controlPoints.get(0).size(), vDegree, typeName + " V"));
    }

    private void validateImplicitSurfaceData(int uDegree, int vDegree, List<List<StepCartesianPoint>> controlPoints, String typeName) {
        if (uDegree < 1 || vDegree < 1 || controlPoints.isEmpty() || controlPoints.get(0).isEmpty()) {
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

    // ==================== Helper Methods ====================

    private void buildSurfaceBoundaryCurve(StepEntity boundary) {
        if (boundary instanceof StepPcurve) {
            StepPcurve pcurve = (StepPcurve) boundary;
            buildPcurve2Callback.apply(pcurve.id());
            return;
        }
        if (boundary instanceof StepCompositeCurveOnSurface) {
            StepCompositeCurveOnSurface compositeCurveOnSurface = (StepCompositeCurveOnSurface) boundary;
            boolean built2d = true;
            for (StepCompositeCurveSegment segment : compositeCurveOnSurface.getSegments()) {
                try {
                    buildCurve2Callback.apply(segment.parentCurve().id());
                } catch (UnsupportedGeometryException ex) {
                    built2d = false;
                    break;
                }
            }
            if (!built2d) {
                buildCompositeCurveCallback.apply(compositeCurveOnSurface.id());
            }
            return;
        }
        buildCurve3Callback.apply(boundary.id());
    }

    private Vector3 buildVector3(StepEntity entity) {
        if (entity instanceof StepVector) {
            StepVector stepVector = (StepVector) entity;
            Direction3 dir = geometryBuilder.buildDirection(stepVector.isOrientation().id());
            double mag = stepVector.magnitude();
            return dir.asVector().scale(mag);
        }
        if (entity instanceof StepDirection) {
            StepDirection stepDir = (StepDirection) entity;
            return geometryBuilder.buildDirection(stepDir.id()).asVector();
        }
        throw new StepResolutionException("entity is not a supported vector or direction");
    }

    private List<Integer> generateUniformMultiplicities(int size) {
        List<Integer> multiplicities = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            multiplicities.add(1);
        }
        return multiplicities;
    }

    private List<Double> generateUniformKnots(int numControlPoints, int degree) {
        int numKnots = numControlPoints + degree + 1;
        List<Double> knots = new ArrayList<>(numKnots);
        for (int i = 0; i < numKnots; i++) {
            knots.add((double) i);
        }
        return knots;
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

    // ==================== Inner Classes ====================

    private static class ImplicitBSplineSurfaceData {
        private final int uDegree;
        private final int vDegree;
        private final List<List<StepCartesianPoint>> controlPoints;
        private final List<Integer> uMultiplicities;
        private final List<Integer> vMultiplicities;
        private final List<Double> uKnots;
        private final List<Double> vKnots;

        ImplicitBSplineSurfaceData(int uDegree, int vDegree,
                                    List<List<StepCartesianPoint>> controlPoints,
                                    List<Integer> uMultiplicities, List<Integer> vMultiplicities,
                                    List<Double> uKnots, List<Double> vKnots) {
            this.uDegree = uDegree;
            this.vDegree = vDegree;
            this.controlPoints = controlPoints;
            this.uMultiplicities = uMultiplicities;
            this.vMultiplicities = vMultiplicities;
            this.uKnots = uKnots;
            this.vKnots = vKnots;
        }

        int uDegree() { return uDegree; }
        int vDegree() { return vDegree; }
        List<List<StepCartesianPoint>> controlPoints() { return controlPoints; }
        List<Integer> uMultiplicities() { return uMultiplicities; }
        List<Integer> vMultiplicities() { return vMultiplicities; }
        List<Double> uKnots() { return uKnots; }
        List<Double> vKnots() { return vKnots; }

        int getUDegree() { return uDegree; }
        int getVDegree() { return vDegree; }
        List<List<StepCartesianPoint>> getControlPoints() { return controlPoints; }
        List<Integer> getUMultiplicities() { return uMultiplicities; }
        List<Integer> getVMultiplicities() { return vMultiplicities; }
        List<Double> getUKnots() { return uKnots; }
        List<Double> getVKnots() { return vKnots; }
    }
}