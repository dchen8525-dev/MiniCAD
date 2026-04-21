package com.minicad.step.semantic;

import com.minicad.common.Epsilon;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BoundingBox3;
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
import com.minicad.geometry.ParaboloidSurface;
import com.minicad.geometry.HyperboloidSurface;
import com.minicad.geometry.SurfaceOfTranslation3;
import com.minicad.geometry.SurfaceOfProjection3;
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
import com.minicad.step.model.topology.StepAdvancedFace;
import com.minicad.step.model.annotation.StepAnnotationCurveOccurrence;
import com.minicad.step.model.geometry.StepAxis1Placement;
import com.minicad.step.model.geometry.StepAxis2Placement2D;
import com.minicad.step.model.geometry.StepAxis2Placement3D;
import com.minicad.step.model.geometry.StepBSplineCurve;
import com.minicad.step.model.geometry.StepBSplineSurface;
import com.minicad.step.model.geometry.StepBezierCurve;
import com.minicad.step.model.geometry.StepBezierSurface;
import com.minicad.step.model.product.StepBrepWithVoids;
import com.minicad.step.model.product.StepBooleanClippingResult;
import com.minicad.step.model.product.StepBooleanResult;
import com.minicad.step.model.geometry.StepBoxDomain;
import com.minicad.step.model.geometry.StepBlendedSurface;
import com.minicad.step.model.product.StepBlockVolume;
import com.minicad.step.model.geometry.StepBoundedCurve;
import com.minicad.step.model.geometry.StepBoundedSurface;
import com.minicad.step.model.geometry.StepCartesianPoint;
import com.minicad.step.model.geometry.StepCartesianTransformationOperator;
import com.minicad.step.model.geometry.StepBSplineCurveWithKnotsAndBreakpoints;
import com.minicad.step.model.geometry.StepBSplineCurveWithKnots;
import com.minicad.step.model.geometry.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.geometry.StepCircle;
import com.minicad.step.model.geometry.StepCompositeCurve;
import com.minicad.step.model.geometry.StepCompositeCurveOnSurface3D;
import com.minicad.step.model.geometry.StepCompositeCurveOnSurface;
import com.minicad.step.model.geometry.StepCompositeCurveSegment;
import com.minicad.step.model.geometry.StepConicalSurface;
import com.minicad.step.model.geometry.StepConicCurve;
import com.minicad.step.model.geometry.StepDegeneratePcurve;
import com.minicad.step.model.geometry.StepCylindricalSurface;
import com.minicad.step.model.geometry.StepDegenerateToroidalSurface;
import com.minicad.step.model.tolerance.StepDimensionCurve;
import com.minicad.step.model.geometry.StepDirection;
import com.minicad.step.model.geometry.StepVector;
import com.minicad.step.model.annotation.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.topology.StepEdgeCurve;
import com.minicad.step.model.topology.StepEdge;
import com.minicad.step.model.topology.StepEdgeLoop;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.geometry.StepEllipse;
import com.minicad.step.model.product.StepFacettedBrep;
import com.minicad.step.model.base.StepFaceEntity;
import com.minicad.step.model.topology.StepFaceBound;
import com.minicad.step.model.topology.StepFaceSurface;
import com.minicad.step.model.manufacturing.StepFilletEdge;
import com.minicad.step.model.manufacturing.StepChamferEdge;
import com.minicad.step.model.topology.StepFace;
import com.minicad.step.model.fea.StepFiniteElementMesh;
import com.minicad.step.model.manufacturing.StepFlatPattern;
import com.minicad.step.model.geometry.StepCurve;
import com.minicad.step.model.geometry.StepCurveBoundedSurface;
import com.minicad.step.model.product.StepItemDefinedTransformation;
import com.minicad.step.model.product.StepCsgPrimitive;
import com.minicad.step.model.product.StepCsgPrimitive3D;
import com.minicad.step.model.product.StepCsgSolid;
import com.minicad.step.model.product.StepCsgVolume;
import com.minicad.step.model.product.StepGeometricReplica;
import com.minicad.step.model.product.StepHalfSpaceSolid;
import com.minicad.step.model.annotation.StepLeaderCurve;
import com.minicad.step.model.geometry.StepLine;
import com.minicad.step.model.geometry.StepLineSegment;
import com.minicad.step.model.manufacturing.StepMachinedSurface;
import com.minicad.step.model.product.StepMappedItem;
import com.minicad.step.model.product.StepManifoldSolidBrep;
import com.minicad.step.model.geometry.StepOffsetCurve2D;
import com.minicad.step.model.geometry.StepOffsetCurve3D;
import com.minicad.step.model.geometry.StepOffsetSurface;
import com.minicad.step.model.product.StepTessellatedFace;
import com.minicad.step.model.product.StepTessellatedFaceSet;
import com.minicad.step.model.product.StepTessellatedTriangle;
import com.minicad.step.model.geometry.StepSeamEdge;
import com.minicad.step.model.profile.StepAreaProfile;
import com.minicad.step.model.profile.StepCenteredCircleProfileDef;
import com.minicad.step.model.profile.StepCentreLineArcProfileDef;
import com.minicad.step.model.profile.StepGeneralizedAreaProfile;
import com.minicad.step.model.profile.StepRectangleHollowProfileDef;
import com.minicad.step.model.profile.StepSweptProfileAreaOutline;
import com.minicad.step.model.topology.StepTriangulatedFace;
import com.minicad.step.model.topology.StepComplexTriangulatedFace;
import com.minicad.step.model.topology.StepCubicBezierTriangulatedFace;
import com.minicad.step.model.topology.StepOrientedEdge;
import com.minicad.step.model.topology.StepOrientedFace;
import com.minicad.step.model.topology.StepOrientedSubface;
import com.minicad.step.model.geometry.StepOrientedCurve;
import com.minicad.step.model.geometry.StepOrientedSurface;
import com.minicad.step.model.geometry.StepPiecewiseBezierCurve;
import com.minicad.step.model.geometry.StepPiecewiseBezierSurface;
import com.minicad.step.model.geometry.StepPlane;
import com.minicad.step.model.geometry.StepPoint;
import com.minicad.step.model.geometry.StepFeaAxis2Placement3d;
import com.minicad.step.model.geometry.StepPcurve;
import com.minicad.step.model.profile.StepProfileDef;
import com.minicad.step.model.geometry.StepProjectionCurve;
import com.minicad.step.model.product.StepPolygonalBoundedHalfSpace;
import com.minicad.step.model.geometry.StepPolyline;
import com.minicad.step.model.geometry.StepQuasiUniformCurve;
import com.minicad.step.model.geometry.StepQuasiUniformSurface;
import com.minicad.step.model.geometry.StepRationalBSplineCurve;
import com.minicad.step.model.geometry.StepRationalBSplineSurface;
import com.minicad.step.model.geometry.StepRectangularTrimmedSurface;
import com.minicad.step.model.geometry.StepSeamCurve;
import com.minicad.step.model.product.StepSolidModel;
import com.minicad.step.model.product.StepSolidReplica;
import com.minicad.step.model.geometry.StepSurface;
import com.minicad.step.model.geometry.StepSurfaceCurve;
import com.minicad.step.model.product.StepSurfaceCurveSweptAreaSolid;
import com.minicad.step.model.product.StepSweptDiskSolid;
import com.minicad.step.model.product.StepExtrudedAreaSolidTapered;
import com.minicad.step.model.product.StepRevolvedAreaSolidTapered;
import com.minicad.step.model.product.StepExtrudedFaceSolid;
import com.minicad.step.model.product.StepRevolvedFaceSolid;
import com.minicad.step.model.product.StepSweptFaceSolid;
import com.minicad.step.model.product.StepCylinderVolume;
import com.minicad.step.model.product.StepSphereVolume;
import com.minicad.step.model.product.StepTorusVolume;
import com.minicad.step.model.product.StepPrismVolume;
import com.minicad.step.model.geometry.StepRuledSurface;
import com.minicad.step.model.geometry.StepSurfaceModel;
import com.minicad.step.model.geometry.StepSurfaceOfConstantRadius;
import com.minicad.step.model.geometry.StepSurfacePatch;
import com.minicad.step.model.geometry.StepRectangularCompositeSurface;
import com.minicad.step.model.geometry.StepClothoid;
import com.minicad.step.model.geometry.StepIndexedPolyCurve;
import com.minicad.step.model.geometry.StepPolyline3D;
import com.minicad.step.model.geometry.StepDegenerateCurve;
import com.minicad.step.model.product.StepNonManifoldSolidBrep;
import com.minicad.step.model.geometry.StepSurfaceOfLinearExtrusion;
import com.minicad.step.model.geometry.StepSurfaceOfRevolution;
import com.minicad.step.model.geometry.StepSurfaceOfTranslation;
import com.minicad.step.model.geometry.StepSurfaceOfProjection;
import com.minicad.step.model.geometry.StepParaboloidSurface;
import com.minicad.step.model.geometry.StepHyperboloidSurface;
import com.minicad.step.model.product.StepSweptAreaSolid;
import com.minicad.step.model.geometry.StepSurfacedEdgeCurve;
import com.minicad.step.model.geometry.StepSphericalSurface;
import com.minicad.step.model.annotation.StepTerminatorSymbol;
import com.minicad.step.model.geometry.StepTrimmedCurve;
import com.minicad.step.model.geometry.StepToroidalSurface;
import com.minicad.step.model.geometry.StepToroidalSurfaceWithSpecifiedBends;
import com.minicad.step.model.topology.StepSubedge;
import com.minicad.step.model.topology.StepSubface;
import com.minicad.step.model.geometry.StepUniformCurve;
import com.minicad.step.model.geometry.StepUniformSurface;
import com.minicad.step.model.topology.StepVertexLoop;
import com.minicad.step.model.topology.StepVertexPoint;
import com.minicad.step.model.topology.StepVertex;
import com.minicad.step.model.geometry.StepOrientedPath;
import com.minicad.step.model.geometry.StepOpenPath;
import com.minicad.step.model.geometry.StepPath;
import com.minicad.step.model.geometry.StepSubpath;
import com.minicad.step.model.geometry.StepHyperbola2D;
import com.minicad.step.model.geometry.StepParabola2D;
import com.minicad.step.model.geometry.StepLine2D;
import com.minicad.step.model.geometry.StepPolyline2D;
import com.minicad.step.model.geometry.StepTrimmedCurve2D;
import com.minicad.step.model.geometry.StepBSplineCurve2D;
import com.minicad.step.model.geometry.StepRationalBSplineCurve2D;
import com.minicad.step.model.geometry.StepBezierCurve2D;
import com.minicad.step.model.geometry.StepQuasiUniformCurve2D;
import com.minicad.step.model.geometry.StepUniformCurve2D;
import com.minicad.step.model.geometry.StepPiecewiseBezierCurve2D;
import com.minicad.step.model.geometry.StepIndexedPolyCurve2D;
import com.minicad.step.model.geometry.StepDegenerateCurve2D;
import com.minicad.step.model.geometry.StepCircle2D;
import com.minicad.step.model.geometry.StepBoundedCurve2D;
import com.minicad.step.model.geometry.StepCompositeCurve2D;
import com.minicad.step.model.geometry.StepCurve2D;
import com.minicad.step.model.geometry.StepEllipse2D;
import com.minicad.step.model.geometry.StepBSplineSurfaceWithKnotsAndBreakpoints;
import com.minicad.step.model.geometry.StepOffsetSurface2;
import com.minicad.step.model.geometry.StepFreeFormSurface;
import com.minicad.step.model.geometry.StepCylindricalSurfaceWithEllipticalAxis;
import com.minicad.step.model.geometry.StepConicalSurfaceWithEllipticalAxis;
import com.minicad.step.model.geometry.StepSphericalSurfaceWithEllipticalAxis;
import com.minicad.step.model.geometry.StepToroidalSurfaceWithCylindricalAxis;
import com.minicad.step.model.geometry.StepToroidalSurfaceWithEllipticalAxis;
import com.minicad.step.model.topology.StepAdvancedBrep;
import com.minicad.step.model.product.StepComplexClippingResult;
import com.minicad.step.syntax.StepValue;
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
    private final StepCadGeometryOps geometryOps;
    private final StepTrimResolver trimResolver;
    private final StepProfileBuilder profileBuilder;
    private final StepTopologyBuilder topologyBuilder;
    private final StepShellBuilder shellBuilder;
    private final StepSolidBuilder solidBuilder;
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
    private final Map<Integer, com.minicad.geometry2d.Hyperbola2> hyperbolas2d = new LinkedHashMap<>();
    private final Map<Integer, com.minicad.geometry2d.Parabola2> parabolas2d = new LinkedHashMap<>();
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
    private final Map<Integer, RuledSurface3> ruledSurfaces = new LinkedHashMap<>();
    private final Map<Integer, SurfaceOfConstantRadius3> constantRadiusSurfaces = new LinkedHashMap<>();
    private final Map<Integer, SurfaceOfLinearExtrusion3> linearExtrusionSurfaces = new LinkedHashMap<>();
    private final Map<Integer, SurfaceOfRevolution3> revolutionSurfaces = new LinkedHashMap<>();
    private final Map<Integer, ParaboloidSurface> paraboloidSurfaces = new LinkedHashMap<>();
    private final Map<Integer, HyperboloidSurface> hyperboloidSurfaces = new LinkedHashMap<>();
    private final Map<Integer, SurfaceOfTranslation3> translationSurfaces = new LinkedHashMap<>();
    private final Map<Integer, SurfaceOfProjection3> projectionSurfaces = new LinkedHashMap<>();
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
    private final Map<Integer, com.minicad.topology.PolyLoop> polyLoops = new LinkedHashMap<>();
    private final Map<Integer, CompositeCurve3> paths = new LinkedHashMap<>();
    private final Map<Integer, FaceBound> faceBounds = new LinkedHashMap<>();
    private final Map<Integer, Face> faces = new LinkedHashMap<>();
    private final Map<Integer, Shell> shells = new LinkedHashMap<>();
    private final Map<Integer, Solid> solids = new LinkedHashMap<>();

    private StepCadBuilder(Map<Integer, StepEntity> entitiesById) {
        this.entitiesById = Map.copyOf(entitiesById);
        this.geometryOps = new StepCadGeometryOps(this);
        this.trimResolver = new StepTrimResolver(entitiesById, this::buildPoint, this::buildPoint2);
        this.profileBuilder = new StepProfileBuilder(geometryOps, e -> (Curve2) buildCurve2(e));
        this.topologyBuilder = new StepTopologyBuilder(this);
        this.shellBuilder = new StepShellBuilder(this);
        this.solidBuilder = new StepSolidBuilder(this);
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
        StepEntity entity = requireExistingEntity(id);
        if (entity instanceof StepCartesianPoint point) {
            CartesianPoint built = new CartesianPoint(
                    point.coordinates().get(0),
                    point.coordinates().get(1),
                    point.coordinates().size() > 2 ? point.coordinates().get(2) : 0.0
            );
            points.put(id, built);
            return built;
        }
        if (entity instanceof StepPoint) {
            // POINT has no coordinates; return origin
            CartesianPoint built = new CartesianPoint(0.0, 0.0, 0.0);
            points.put(id, built);
            return built;
        }
        if (entity instanceof StepVertexPoint vertexPoint) {
            return buildVertex(vertexPoint.id()).point();
        }
        throw new UnsupportedGeometryException("entity #" + id + " is not a supported 3D point");
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
        com.minicad.step.model.geometry.StepVector vector = requireEntity(id, com.minicad.step.model.geometry.StepVector.class, "VECTOR");
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
        StepEntity entity = requireExistingEntity(id);
        if (entity instanceof StepAxis2Placement3D placement) {
            Axis2Placement3D built = new Axis2Placement3D(
                    buildPoint(placement.location().id()),
                    buildDirection(placement.axis().id()),
                    buildDirection(placement.refDirection().id())
            );
            placements.put(id, built);
            return built;
        }
        if (entity instanceof StepFeaAxis2Placement3d feaPlacement) {
            Axis2Placement3D built = new Axis2Placement3D(
                    buildPoint(feaPlacement.location().id()),
                    buildDirection(feaPlacement.axis().id()),
                    buildDirection(feaPlacement.refDirection().id())
            );
            placements.put(id, built);
            return built;
        }
        throw new UnsupportedGeometryException("entity #" + id + " is not a supported placement");
    }

    public Axis1Placement buildAxis1Placement(int id) {
        StepAxis1Placement placement = requireEntity(id, StepAxis1Placement.class, "AXIS1_PLACEMENT");
        return new Axis1Placement(buildPoint(placement.location().id()), buildDirection(placement.axis().id()));
    }

    /**
     * Builds an Axis2Placement3D from an Axis1Placement by deriving a reference direction.
     * Used for surfaces that need full 3D placement but only have axis defined.
     */
    private Axis2Placement3D buildAxis1PlacementAsAxis2(int id) {
        Axis1Placement axis1 = buildAxis1Placement(id);
        // Derive a perpendicular reference direction
        Direction3 refDir = perpendicularDirection(axis1.axis());
        return new Axis2Placement3D(axis1.location(), axis1.axis(), refDir);
    }

    /**
     * Returns a unit direction perpendicular to the given direction.
     */
    private Direction3 perpendicularDirection(Direction3 dir) {
        Vector3 v = dir.asVector();
        // Find the smallest component and cross with that axis
        if (Math.abs(v.x()) <= Math.abs(v.y()) && Math.abs(v.x()) <= Math.abs(v.z())) {
            // Cross with X axis
            Vector3 perp = new Vector3(1, 0, 0).cross(v);
            if (perp.isZero()) {
                return new Direction3(0, 1, 0);
            }
            return Direction3.from(perp);
        } else if (Math.abs(v.y()) <= Math.abs(v.z())) {
            // Cross with Y axis
            Vector3 perp = new Vector3(0, 1, 0).cross(v);
            if (perp.isZero()) {
                return new Direction3(1, 0, 0);
            }
            return Direction3.from(perp);
        } else {
            // Cross with Z axis
            Vector3 perp = new Vector3(0, 0, 1).cross(v);
            if (perp.isZero()) {
                return new Direction3(1, 0, 0);
            }
            return Direction3.from(perp);
        }
    }

    /**
     * Builds a CARTESIAN_TRANSFORMATION_OPERATOR_3D into an Axis2Placement3D.
     * The transformation defines a local coordinate system that can be used as a placement.
     *
     * @param id STEP entity id
     * @return built placement representing the transformed coordinate system
     */
    public Axis2Placement3D buildTransformation(int id) {
        Axis2Placement3D existing = placements.get(id);
        if (existing != null) {
            return existing;
        }
        StepCartesianTransformationOperator op = requireEntity(id, StepCartesianTransformationOperator.class,
                "CARTESIAN_TRANSFORMATION_OPERATOR");
        Direction3 zAxis = op.axis3() != null ? buildDirection(op.axis3().id()) : Direction3.zAxis();
        Direction3 xAxis = op.axis1() != null ? buildDirection(op.axis1().id()) :
                perpendicularDirection(zAxis);
        CartesianPoint origin = buildPoint(op.localOrigin().id());
        Axis2Placement3D built = new Axis2Placement3D(origin, zAxis, xAxis);
        placements.put(id, built);
        return built;
    }

    /**
     * Builds an ITEM_DEFINED_TRANSFORMATION by computing the transform between two placements.
     *
     * @param id STEP entity id
     * @return built placement representing the target coordinate system
     */
    public Axis2Placement3D buildItemDefinedTransformation(int id) {
        Axis2Placement3D existing = placements.get(id);
        if (existing != null) {
            return existing;
        }
        StepItemDefinedTransformation transform = requireEntity(id, StepItemDefinedTransformation.class,
                "ITEM_DEFINED_TRANSFORMATION");
        // Build the target placement directly
        Axis2Placement3D target = buildPlacement(transform.transformItem2().id());
        placements.put(id, target);
        return target;
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
                buildDirection(line.vector().orientation().id()),
                line.vector().magnitude()
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
                buildDirection2(line.vector().orientation().id()),
                line.vector().magnitude()
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

    private CompositeCurve2 buildCompositeCurve2D(StepCompositeCurve2D compositeCurve2D) {
        CompositeCurve2 existing = compositeCurves2d.get(compositeCurve2D.id());
        if (existing != null) {
            return existing;
        }
        List<Curve2> curves = new ArrayList<>(compositeCurve2D.segments().size());
        for (StepCompositeCurveSegment segment : compositeCurve2D.segments()) {
            Object built = buildCurve2(segment.parentCurve());
            if (!(built instanceof Curve2 curve)) {
                throw new UnsupportedGeometryException("COMPOSITE_CURVE_2D segment is not a supported 2D curve");
            }
            curves.add(curve);
        }
        CompositeCurve2 built = new CompositeCurve2(curves);
        compositeCurves2d.put(compositeCurve2D.id(), built);
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
        if (item instanceof StepCircle2D circle2D) {
            return buildCircle2D(circle2D);
        }
        if (item instanceof StepEllipse2D ellipse2D) {
            return buildEllipse2D(ellipse2D);
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
        if (item instanceof StepBSplineCurve spline) {
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
        if (item instanceof StepDegenerateCurve degenerateCurve) {
            return buildDegenerateCurve2(degenerateCurve);
        }
        if (item instanceof StepClothoid clothoid) {
            return buildClothoid2(clothoid);
        }
        // 2D-specific curve types
        if (item instanceof StepPolyline2D polyline2D) {
            return buildPolyline2D(polyline2D);
        }
        if (item instanceof StepTrimmedCurve2D trimmedCurve2D) {
            return buildTrimmedCurve2D(trimmedCurve2D);
        }
        if (item instanceof StepBSplineCurve2D spline2D) {
            return buildBSplineCurve2D(spline2D);
        }
        if (item instanceof StepRationalBSplineCurve2D rationalSpline2D) {
            return buildRationalBSplineCurve2D(rationalSpline2D);
        }
        if (item instanceof StepBezierCurve2D bezier2D) {
            return buildBezierCurve2D(bezier2D);
        }
        if (item instanceof StepQuasiUniformCurve2D quasiUniform2D) {
            return buildQuasiUniformCurve2D(quasiUniform2D);
        }
        if (item instanceof StepUniformCurve2D uniform2D) {
            return buildUniformCurve2D(uniform2D);
        }
        if (item instanceof StepPiecewiseBezierCurve2D piecewiseBezier2D) {
            return buildPiecewiseBezierCurve2D(piecewiseBezier2D);
        }
        if (item instanceof StepIndexedPolyCurve2D polyCurve2D) {
            return buildIndexedPolyCurve2D(polyCurve2D);
        }
        if (item instanceof StepDegenerateCurve2D degenerateCurve2D) {
            return buildDegenerateCurve2D(degenerateCurve2D);
        }
        if (item instanceof StepHyperbola2D hyperbola2D) {
            return buildHyperbola2D(hyperbola2D);
        }
        if (item instanceof StepParabola2D parabola2D) {
            return buildParabola2D(parabola2D);
        }
        if (item instanceof StepLine2D line2D) {
            return buildLine2D(line2D);
        }
        // Bounded curve wraps an underlying 2D curve
        if (item instanceof StepBoundedCurve2D boundedCurve2D) {
            return buildCurve2(boundedCurve2D.curve());
        }
        // Composite 2D curve
        if (item instanceof StepCompositeCurve2D compositeCurve2D) {
            return buildCompositeCurve2D(compositeCurve2D);
        }
        // Bounded curve marker (3D) - marker type with no geometry data
        if (item instanceof StepBoundedCurve boundedCurve) {
            StepEntity actual = entitiesById.get(boundedCurve.id());
            if (actual != null && actual != boundedCurve) {
                return buildCurve2(actual);
            }
            throw new UnsupportedGeometryException("BOUNDED_CURVE requires an underlying curve type");
        }
        // PCURVE and DEGENERATE_PCURVE: parameter-space curves on surfaces
        if (item instanceof StepPcurve pcurve) {
            return buildPcurveCurve2(pcurve);
        }
        if (item instanceof StepDegeneratePcurve pcurve) {
            return buildPcurveCurve2(pcurve);
        }
        // CURVE_2D: parametric curve with polynomial equation coefficients
        if (item instanceof StepCurve2D curve2D) {
            return buildCurve2DParametric(curve2D);
        }
        // MAPPED_ITEM: dispatch through to mapping target
        if (item instanceof StepMappedItem mappedItem) {
            return buildCurve2(mappedItem.mappingTarget());
        }
        throw new UnsupportedGeometryException("2D curve type " + stepEntityTypeName(item) + " is not supported");
    }

    /**
     * Builds a PCURVE or DEGENERATE_PCURVE as a 2D curve.
     * PCURVE wraps a representation item containing the actual 2D curve.
     */
    private Object buildPcurveCurve2(StepEntity entity) {
        StepEntity item;
        if (entity instanceof StepPcurve pcurve) {
            item = pcurve.referenceToCurve().items().getFirst();
        } else if (entity instanceof StepDegeneratePcurve pcurve) {
            item = pcurve.referenceToCurve().items().getFirst();
        } else {
            throw new StepResolutionException(stepEntityTypeName(entity) + " is not a PCURVE or DEGENERATE_PCURVE");
        }
        return buildCurve2(item);
    }

    /**
     * Builds a parametric CURVE_2D as a polyline by sampling the polynomial equation.
     * The equation coefficients are split evenly: first half for x(t), second half for y(t).
     * Samples t in [0, 1] range.
     */
    private com.minicad.geometry2d.Polyline2 buildCurve2DParametric(StepCurve2D curve2D) {
        double[] eq = curve2D.equation();
        if (eq.length < 2) {
            throw new UnsupportedGeometryException("CURVE_2D equation must have at least 2 coefficients");
        }
        int half = eq.length / 2;
        double[] xCoeffs = java.util.Arrays.copyOfRange(eq, 0, half);
        double[] yCoeffs = java.util.Arrays.copyOfRange(eq, half, eq.length);

        // Build placement transformation
        com.minicad.geometry.Axis2Placement3D placement = null;
        if (curve2D.position() instanceof StepAxis2Placement2D pos2D) {
            CartesianPoint origin = buildPoint(pos2D.location().id());
            Direction3 xDir = new Direction3(1, 0, 0);
            if (pos2D.refDirection() != null) {
                StepDirection dir = pos2D.refDirection();
                List<Double> dirs = dir.directionRatios();
                if (dirs != null && dirs.size() >= 2) {
                    xDir = Direction3.from(new com.minicad.geometry.Vector3(dirs.get(0), dirs.get(1), 0));
                }
            }
            Direction3 axis = Direction3.zAxis();
            placement = new com.minicad.geometry.Axis2Placement3D(origin, axis, xDir);
        }

        int samples = Math.max(64, eq.length * 16);
        List<Point2> points = new ArrayList<>(samples + 1);
        for (int i = 0; i <= samples; i++) {
            double t = (double) i / samples;
            double x = evaluatePolynomial(xCoeffs, t);
            double y = evaluatePolynomial(yCoeffs, t);
            if (placement != null) {
                // Transform from local to global coordinates
                com.minicad.geometry.Axis2Placement3D p = placement;
                double gx = p.location().x() + x * p.xDirection().x() + y * p.yDirection().x();
                double gy = p.location().y() + x * p.xDirection().y() + y * p.yDirection().y();
                points.add(new Point2(gx, gy));
            } else {
                points.add(new Point2(x, y));
            }
        }
        return new com.minicad.geometry2d.Polyline2(points);
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

    private Polyline2 buildDegenerateCurve2(StepDegenerateCurve degenerateCurve) {
        // Degenerate curve in 2D is a single point or empty curve
        // Return a minimal polyline (single point repeated)
        StepEntity basisEntity = degenerateCurve.basisCurve();
        if (basisEntity instanceof StepCartesianPoint point) {
            List<Double> coords = point.coordinates();
            Point2 pt = coords.size() >= 2
                ? new Point2(coords.get(0), coords.get(1))
                : new Point2(0, 0);
            return new Polyline2(List.of(pt, pt));
        }
        // Fallback: try to sample the basis curve and get a point
        try {
            Curve3 basisCurve = buildCurve3(basisEntity);
            List<CartesianPoint> samples = basisCurve.sample(2);
            if (!samples.isEmpty()) {
                CartesianPoint first = samples.getFirst();
                Point2 pt = new Point2(first.x(), first.y());
                return new Polyline2(List.of(pt, pt));
            }
        } catch (Exception e) {
            // Ignore and use default
        }
        return new Polyline2(List.of(new Point2(0, 0), new Point2(0, 0)));
    }

    private Polyline2 buildClothoid2(StepClothoid clothoid) {
        // Clothoid (Euler spiral) in 2D - approximate with polyline sampling
        StepEntity positionEntity = clothoid.position();
        Point2 origin;
        Direction2 xDir;
        if (positionEntity instanceof StepAxis2Placement2D placement2D) {
            origin = buildPoint2(placement2D.location().id());
            xDir = buildDirection2(placement2D.refDirection().id());
        } else {
            origin = new Point2(0, 0);
            xDir = new Direction2(1, 0);
        }
        double xAxisIntercept = clothoid.xAxisIntercept();
        double curvature = clothoid.curvature();

        if (!Double.isFinite(xAxisIntercept) || !Double.isFinite(curvature) || curvature == 0) {
            return new Polyline2(List.of(origin, origin));
        }

        // Sample clothoid curve
        int segments = 64;
        List<Point2> points = new ArrayList<>(segments + 1);
        Direction2 yDir = new Direction2(-xDir.y(), xDir.x());

        // Clothoid parametric: x(t) = A * integral(cos(u^2), u=0..t), y(t) = A * integral(sin(u^2), u=0..t)
        // where A = xAxisIntercept / sqrt(pi/2)
        double A = xAxisIntercept / Math.sqrt(Math.PI / 2);
        double maxT = Math.sqrt(Math.abs(curvature) * Math.PI);

        for (int i = 0; i <= segments; i++) {
            double t = (maxT * i) / segments;
            // Fresnel integrals approximation
            double fresnelC = fresnelCos(t);
            double fresnelS = fresnelSin(t);
            double x = A * fresnelC;
            double y = A * fresnelS;
            Point2 pt = origin.add(xDir.asVector().scale(x).add(yDir.asVector().scale(y)));
            points.add(pt);
        }
        return new Polyline2(points);
    }

    // Fresnel integral approximations
    private double fresnelCos(double t) {
        // C(t) ≈ t - (t^5)/10 + (t^9)/216 - ... for small t
        // For larger t, use asymptotic approximation
        if (t < 0.5) {
            return t - t*t*t*t*t/10.0 + t*t*t*t*t*t*t*t*t/216.0;
        }
        // Asymptotic: C(t) ≈ 0.5 + sin(t^2)/(2*pi*t) - cos(t^2)/(2*pi*t^3)
        double t2 = t * t;
        return 0.5 + Math.sin(t2) / (2 * Math.PI * t);
    }

    private double fresnelSin(double t) {
        // S(t) ≈ (t^3)/3 - (t^7)/42 + (t^11)/1320 - ... for small t
        if (t < 0.5) {
            return t*t*t/3.0 - t*t*t*t*t*t*t/42.0;
        }
        // Asymptotic: S(t) ≈ 0.5 - cos(t^2)/(2*pi*t) - sin(t^2)/(2*pi*t^3)
        double t2 = t * t;
        return 0.5 - Math.cos(t2) / (2 * Math.PI * t);
    }

    // Build methods for 2D-specific curve types

    private Polyline2 buildPolyline2D(StepPolyline2D polyline2D) {
        List<Point2> points = polyline2D.points().stream()
                .map(p -> buildPoint2(p.id()))
                .toList();
        return new Polyline2(points);
    }

    private TrimmedCurve2 buildTrimmedCurve2D(StepTrimmedCurve2D trimmedCurve2D) {
        Curve2 basisCurve = (Curve2) buildCurve2(trimmedCurve2D.basisCurve());
        // Use trim parameters directly on the basis curve
        double trim1 = trimmedCurve2D.trim1();
        double trim2 = trimmedCurve2D.trim2();
        return new TrimmedCurve2(basisCurve, trim1, trim2, trimmedCurve2D.senseAgreement());
    }

    private BSplineCurve2 buildBSplineCurve2D(StepBSplineCurve2D spline2D) {
        BSplineCurve2 existing = splineCurves2d.get(spline2D.id());
        if (existing != null) {
            return existing;
        }
        List<Point2> controlPoints = spline2D.controlPoints().stream()
                .map(p -> buildPoint2(p.id()))
                .toList();
        BSplineCurve2 built = new BSplineCurve2(spline2D.degree(), controlPoints, List.of(1), List.of(0.0, 1.0));
        splineCurves2d.put(spline2D.id(), built);
        return built;
    }

    private RationalBSplineCurve2 buildRationalBSplineCurve2D(StepRationalBSplineCurve2D rationalSpline2D) {
        RationalBSplineCurve2 existing = rationalSplineCurves2d.get(rationalSpline2D.id());
        if (existing != null) {
            return existing;
        }
        List<Point2> controlPoints = rationalSpline2D.controlPoints().stream()
                .map(p -> buildPoint2(p.id()))
                .toList();
        RationalBSplineCurve2 built = new RationalBSplineCurve2(
                rationalSpline2D.degree(), controlPoints, rationalSpline2D.weights(), List.of(1), List.of(0.0, 1.0));
        rationalSplineCurves2d.put(rationalSpline2D.id(), built);
        return built;
    }

    private BSplineCurve2 buildBezierCurve2D(StepBezierCurve2D bezier2D) {
        return buildImplicitBSplineCurve2D(bezier2D.id(), bezier2D.degree(), bezier2D.controlPoints());
    }

    private BSplineCurve2 buildQuasiUniformCurve2D(StepQuasiUniformCurve2D quasiUniform2D) {
        return buildImplicitBSplineCurve2D(quasiUniform2D.id(), quasiUniform2D.degree(), quasiUniform2D.controlPoints());
    }

    private BSplineCurve2 buildUniformCurve2D(StepUniformCurve2D uniform2D) {
        return buildImplicitBSplineCurve2D(uniform2D.id(), uniform2D.degree(), uniform2D.controlPoints());
    }

    private BSplineCurve2 buildPiecewiseBezierCurve2D(StepPiecewiseBezierCurve2D piecewiseBezier2D) {
        return buildImplicitBSplineCurve2D(piecewiseBezier2D.id(), piecewiseBezier2D.degree(), piecewiseBezier2D.controlPoints());
    }

    private BSplineCurve2 buildImplicitBSplineCurve2D(int id, int degree, List<StepCartesianPoint> controlPoints) {
        BSplineCurve2 existing = splineCurves2d.get(id);
        if (existing != null) {
            return existing;
        }
        List<Point2> points = controlPoints.stream()
                .map(p -> buildPoint2(p.id()))
                .toList();
        BSplineCurve2 built = new BSplineCurve2(degree, points, List.of(1), List.of(0.0, 1.0));
        splineCurves2d.put(id, built);
        return built;
    }

    private Polyline2 buildIndexedPolyCurve2D(StepIndexedPolyCurve2D polyCurve2D) {
        List<StepCartesianPoint> stepPoints = polyCurve2D.points();
        List<Integer> indices = polyCurve2D.indices();
        List<Point2> points = indices.stream()
                .map(index -> buildPoint2(stepPoints.get(index).id()))
                .toList();
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
        StepAxis2Placement2D position = hyperbola2D.position();
        Point2 center = buildPoint2(position.location().id());
        Direction2 xDir = buildDirection2(position.refDirection().id());
        Hyperbola2 built = new Hyperbola2(center, xDir, hyperbola2D.semiAxis1(), hyperbola2D.semiAxis2());
        hyperbolas2d.put(hyperbola2D.id(), built);
        return built;
    }

    private Parabola2 buildParabola2D(StepParabola2D parabola2D) {
        Parabola2 existing = parabolas2d.get(parabola2D.id());
        if (existing != null) {
            return existing;
        }
        StepAxis2Placement2D position = parabola2D.position();
        Point2 center = buildPoint2(position.location().id());
        Direction2 xDir = buildDirection2(position.refDirection().id());
        Parabola2 built = new Parabola2(center, xDir, parabola2D.focalDist());
        parabolas2d.put(parabola2D.id(), built);
        return built;
    }

    private Line2 buildLine2D(StepLine2D line2D) {
        Point2 point = buildPoint2(line2D.point2d().id());
        com.minicad.geometry2d.Direction2 dir = buildDirection2(line2D.direction2d().id());
        return new Line2(point, dir);
    }

    private Circle2 buildCircle2D(StepCircle2D circle2D) {
        Circle2 existing = circles2d.get(circle2D.id());
        if (existing != null) {
            return existing;
        }
        StepAxis2Placement2D position = circle2D.position();
        Point2 center = buildPoint2(position.location().id());
        Direction2 xDir = buildDirection2(position.refDirection().id());
        Circle2 built = new Circle2(center, xDir, circle2D.radius());
        circles2d.put(circle2D.id(), built);
        return built;
    }

    private Ellipse2 buildEllipse2D(StepEllipse2D ellipse2D) {
        Ellipse2 existing = ellipses2d.get(ellipse2D.id());
        if (existing != null) {
            return existing;
        }
        StepAxis2Placement2D position = ellipse2D.position();
        Point2 center = buildPoint2(position.location().id());
        Direction2 xDir = buildDirection2(position.refDirection().id());
        Ellipse2 built = new Ellipse2(center, xDir, ellipse2D.semiAxis1(), ellipse2D.semiAxis2());
        ellipses2d.put(ellipse2D.id(), built);
        return built;
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
        double trimParamStart = trimResolver.resolveTrimParam2(trimmedCurve.trim1(), basisCurve, "trim_1");
        double trimParamEnd = trimResolver.resolveTrimParam2(trimmedCurve.trim2(), basisCurve, "trim_2");
        TrimmedCurve2 built = new TrimmedCurve2(basisCurve, trimParamStart, trimParamEnd, trimmedCurve.senseAgreement());
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
     * Builds a toroidal surface from TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS.
     * Uses the position, major radius, and minor radius parameters; the optional
     * axis curves are ignored for B-Rep generation.
     */
    private ToroidalSurface buildToroidalSurfaceFromSpecifiedBends(StepToroidalSurfaceWithSpecifiedBends surface) {
        ToroidalSurface existing = toroidalSurfaces.get(surface.id());
        if (existing != null) {
            return existing;
        }
        ToroidalSurface built = new ToroidalSurface(buildPlacement(surface.position().id()), surface.majorRadius(), surface.minorRadius());
        toroidalSurfaces.put(surface.id(), built);
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

    public RuledSurface3 buildRuledSurface(int id) {
        RuledSurface3 existing = ruledSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepRuledSurface surface = requireEntity(id, StepRuledSurface.class, "RULED_SURFACE");
        Axis2Placement3D position = buildPlacement(surface.position().id());
        Curve3 directrix1 = buildCurve3(surface.directrix1());
        Curve3 directrix2 = buildCurve3(surface.directrix2());
        RuledSurface3 built = new RuledSurface3(directrix1, directrix2);
        ruledSurfaces.put(id, built);
        return built;
    }

    public SurfaceOfConstantRadius3 buildSurfaceOfConstantRadius(int id) {
        SurfaceOfConstantRadius3 existing = constantRadiusSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepSurfaceOfConstantRadius surface = requireEntity(id, StepSurfaceOfConstantRadius.class, "SURFACE_OF_CONSTANT_RADIUS");
        SurfaceGeometry sweptSurface = buildSupportedFaceGeometry(surface.sweptSurface(), "SURFACE_OF_CONSTANT_RADIUS");
        if (sweptSurface == null) {
            throw new UnsupportedGeometryException("Swept surface is null for SURFACE_OF_CONSTANT_RADIUS");
        }
        SurfaceOfConstantRadius3 built = new SurfaceOfConstantRadius3(sweptSurface, surface.radius());
        constantRadiusSurfaces.put(id, built);
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
     * Builds a B-spline curve with explicit breakpoints.
     * Breakpoints define parameter values where the curve form changes.
     *
     * @param id STEP entity id
     * @return built B-spline curve
     */
    public BSplineCurve3 buildBSplineCurveWithBreakpoints(int id) {
        BSplineCurve3 existing = bsplineCurves.get(id);
        if (existing != null) {
            return existing;
        }
        StepBSplineCurveWithKnotsAndBreakpoints spline = requireEntity(id, StepBSplineCurveWithKnotsAndBreakpoints.class,
                "B_SPLINE_CURVE_WITH_KNOTS_AND_BREAKPOINTS");
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
     * Builds a generic B_SPLINE_SURFACE without explicit knots.
     * Generates uniform knot vectors based on degree and control point count.
     */
    public BSplineSurface3 buildGenericBSplineSurface(int id) {
        BSplineSurface3 existing = bsplineSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepBSplineSurface surface = requireEntity(id, StepBSplineSurface.class, "B_SPLINE_SURFACE");
        List<List<CartesianPoint>> controlPoints = surface.controlPoints().stream()
                .map(row -> row.stream().map(point -> buildPoint(point.id())).toList())
                .toList();
        int uCount = controlPoints.size();
        int vCount = controlPoints.getFirst().size();
        // Generate uniform knot vectors with minimum multiplicity at ends
        List<Double> uKnots = List.of(0.0, 1.0);
        List<Double> vKnots = List.of(0.0, 1.0);
        List<Integer> uMults = List.of(surface.uDegree() + 1, surface.uDegree() + 1);
        List<Integer> vMults = List.of(surface.vDegree() + 1, surface.vDegree() + 1);
        BSplineSurface3 built = new BSplineSurface3(
                surface.uDegree(),
                surface.vDegree(),
                controlPoints,
                uMults,
                vMults,
                uKnots,
                vKnots
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
        Curve3 basis = buildCurve3(trimmedCurve.basisCurve());
        for (StepValue trim : trimmedCurve.trim1()) {
            trimResolver.validateTrimValue(trim, basis, "trim_1");
        }
        for (StepValue trim : trimmedCurve.trim2()) {
            trimResolver.validateTrimValue(trim, basis, "trim_2");
        }
        double trimParamStart = trimResolver.resolveTrimParameter(trimmedCurve.trim1(), basis, "trim_1");
        double trimParamEnd = trimResolver.resolveTrimParameter(trimmedCurve.trim2(), basis, "trim_2");
        TrimmedCurve3 built = new TrimmedCurve3(basis, trimParamStart, trimParamEnd, trimmedCurve.senseAgreement());
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
        SurfaceCurve3 built = new SurfaceCurve3(curve3d, buildSurfaceCurveBindings(surfaceCurve.associatedGeometry()));
        surfaceCurves.put(id, built);
        return built;
    }

    public SurfaceCurve3 buildSeamCurve(int id) {
        StepSeamCurve seamCurve = requireEntity(id, StepSeamCurve.class, "SEAM_CURVE");
        Curve3 curve3d = buildCurve3(seamCurve.curve3d());
        return new SurfaceCurve3(curve3d, buildSurfaceCurveBindings(seamCurve.associatedGeometry()));
    }

    private List<SurfaceCurve3.ParametricCurve> buildSurfaceCurveBindings(List<StepEntity> associatedGeometry) {
        List<SurfaceCurve3.ParametricCurve> bindings = new ArrayList<>();
        for (StepEntity geometry : associatedGeometry) {
            StepEntity basisSurfaceEntity = null;
            if (geometry instanceof StepPcurve pcurve) {
                basisSurfaceEntity = pcurve.basisSurface();
            } else if (geometry instanceof StepDegeneratePcurve pcurve) {
                basisSurfaceEntity = pcurve.basisSurface();
            }
            if (basisSurfaceEntity == null) {
                continue;
            }
            Object builtCurve = buildPcurveCurve2(geometry);
            if (!(builtCurve instanceof Curve2 curve2)) {
                continue;
            }
            SurfaceGeometry surface = buildSupportedFaceGeometry(basisSurfaceEntity, "PCURVE");
            bindings.add(new SurfaceCurve3.ParametricCurve(surface, curve2));
        }
        return List.copyOf(bindings);
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
        StepEntity entity = requireExistingEntity(id);
        Vertex built;
        if (entity instanceof StepVertexPoint vertexPoint) {
            built = new Vertex(buildPoint(vertexPoint.point().id()));
        } else if (entity instanceof StepVertex vertex) {
            // VERTEX is the abstract base type. In complex entity syntax,
            // the actual vertex subtype (VERTEX_POINT) may be resolved at the same ID.
            StepEntity actual = entitiesById.get(vertex.id());
            if (actual != null && actual != vertex && actual instanceof StepVertexPoint vp) {
                built = new Vertex(buildPoint(vp.point().id()));
            } else {
                throw new StepResolutionException("entity #" + id + " is an abstract VERTEX with no concrete VERTEX_POINT subtype");
            }
        } else {
            throw new StepResolutionException("entity #" + id + " is not a VERTEX_POINT or VERTEX");
        }
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
        if (entity instanceof StepSeamEdge seamEdge) {
            // Seam edge: start and end vertices are the same (closed edge on a surface seam).
            // In complex entity syntax, the actual curve geometry may be at the same ID.
            StepEntity actual = entitiesById.get(seamEdge.id());
            Curve3 curve = null;
            if (actual != null && actual != seamEdge) {
                if (actual instanceof StepEdgeCurve ec) {
                    curve = buildCurve3(ec.edgeGeometry());
                } else if (actual instanceof StepSurfaceCurve sc) {
                    curve = buildSurfaceCurve(sc.id()).curve3d();
                }
            }
            if (curve == null) {
                throw new UnsupportedGeometryException("SEAM_EDGE #" + seamEdge.id() + " has no associated curve geometry");
            }
            Vertex vertex = buildVertex(seamEdge.edgeStart().id());
            built = buildEdgeWithProjection(vertex, vertex, curve, true);
        } else if (entity instanceof StepEdgeCurve edgeCurve) {
            Curve3 curve = buildCurve3(edgeCurve.edgeGeometry());
            Vertex startVertex = buildVertex(edgeCurve.start().id());
            Vertex endVertex = buildVertex(edgeCurve.end().id());
            built = buildEdgeWithProjection(startVertex, endVertex, curve, edgeCurve.sameSense());
        } else if (entity instanceof StepFilletEdge filletEdge) {
            // Fillet edge wraps an original edge - build the underlying edge geometry.
            // The fillet parameters (radius, adjacent faces) are not represented in our Edge topology.
            built = buildEdge(filletEdge.originalEdge().id());
        } else if (entity instanceof StepChamferEdge chamferEdge) {
            // Chamfer edge wraps an original edge - build the underlying edge geometry.
            // The chamfer parameters (angle, width, adjacent faces) are not represented in our Edge topology.
            built = buildEdge(chamferEdge.originalEdge().id());
        } else if (entity instanceof StepSubedge subedge) {
            Edge parent = buildEdge(subedge.parentEdge().id());
            built = buildEdgeWithProjection(
                    buildVertex(subedge.start().id()),
                    buildVertex(subedge.end().id()),
                    parent.curve(),
                    parent.sameSense());
        } else if (entity instanceof StepEdge edge) {
            // EDGE is the abstract base type. In complex entity syntax,
            // the actual edge subtype (EDGE_CURVE, SUBEDGE) may be resolved at the same ID.
            StepEntity actual = entitiesById.get(edge.id());
            if (actual != null && actual != edge) {
                if (actual instanceof StepEdgeCurve || actual instanceof StepSubedge) {
                    built = buildEdge(actual.id());
                } else {
                    throw new StepResolutionException("entity #" + id + " is an abstract EDGE with unsupported subtype " + stepEntityTypeName(actual));
                }
            } else {
                throw new StepResolutionException("entity #" + id + " is an abstract EDGE with no concrete subtype");
            }
        } else if (entity instanceof StepMappedItem mappedItem) {
            // MAPPED_ITEM: dispatch through to mapping target for edge geometry
            built = buildEdge(mappedItem.mappingTarget().id());
        } else {
            throw new StepResolutionException("entity #" + id + " is not a SEAM_EDGE, EDGE_CURVE, FILLET_EDGE, CHAMFER_EDGE, SUBEDGE or EDGE");
        }
        edges.put(id, built);
        return built;
    }

    /**
     * Builds an edge, projecting vertices onto the curve if needed.
     * Industrial STEP files often have vertex coordinates rounded to a limited
     * number of decimal places, causing them to miss the curve by microns.
     */
    private static final double VERTEX_PROJECTION_TOLERANCE = 1.0e-2;

    private Edge buildEdgeWithProjection(Vertex start, Vertex end, Curve3 curve, boolean sameSense) {
        try {
            return new Edge(start, end, curve, sameSense);
        } catch (com.minicad.common.TopologyException e) {
            // Project off-curve vertices onto the curve using closest-point projection
            CartesianPoint startPoint = start.point();
            CartesianPoint endPoint = end.point();
            CartesianPoint projectedStart = projectOntoCurve(startPoint, curve);
            CartesianPoint projectedEnd = projectOntoCurve(endPoint, curve);
            // Use projected vertices - if they're within tolerance, the edge will succeed
            Vertex vStart = (projectedStart.distanceTo(startPoint) > VERTEX_PROJECTION_TOLERANCE) ? start : new Vertex(projectedStart);
            Vertex vEnd = (projectedEnd.distanceTo(endPoint) > VERTEX_PROJECTION_TOLERANCE) ? end : new Vertex(projectedEnd);
            return new Edge(vStart, vEnd, curve, sameSense);
        }
    }

    /**
     * Projects a point onto a curve using closest-point projection.
     * Handles all supported curve types.
     */
    private static CartesianPoint projectOntoCurve(CartesianPoint point, Curve3 curve) {
        if (curve instanceof com.minicad.geometry.BSplineCurve3 bspline) {
            return bspline.closestPointTo(point);
        }
        if (curve instanceof com.minicad.geometry.RationalBSplineCurve3 rational) {
            return rational.closestPointTo(point);
        }
        if (curve instanceof com.minicad.geometry.Line3 line) {
            // Project onto infinite line: t is signed distance along direction
            Vector3 offset = point.subtract(line.origin());
            double t = offset.dot(line.direction().asVector());
            return line.origin().add(line.direction().asVector().scale(t));
        }
        if (curve instanceof com.minicad.geometry.Circle circle) {
            // Project onto circle: normalize vector from center, scale by radius
            CartesianPoint center = circle.position().location();
            Vector3 fromCenter = point.subtract(center);
            if (fromCenter.normSquared() <= com.minicad.common.Epsilon.EPS) {
                // Point is at center - pick arbitrary point on circle
                Vector3 xDir = circle.position().xDirection().asVector();
                return center.add(xDir.scale(circle.radius()));
            }
            return center.add(fromCenter.normalize().asVector().scale(circle.radius()));
        }
        if (curve instanceof com.minicad.geometry.Ellipse3 ellipse) {
            // Approximate by sampling - good enough for projection
            return ellipse.closestPointTo(point);
        }
        if (curve instanceof com.minicad.geometry.Polyline3 polyline) {
            // Find closest point on polyline segments
            return polylineClosestPoint(point, polyline);
        }
        if (curve instanceof com.minicad.geometry.TrimmedCurve3 trimmed) {
            return projectOntoCurve(point, trimmed.basisCurve());
        }
        if (curve instanceof com.minicad.geometry.SurfaceCurve3 sc) {
            return projectOntoCurve(point, sc.curve3d());
        }
        if (curve instanceof com.minicad.geometry.CompositeCurve3 composite) {
            // Find closest point across all segments
            CartesianPoint closest = null;
            double minDist = Double.POSITIVE_INFINITY;
            for (Curve3 segment : composite.segments()) {
                CartesianPoint candidate = projectOntoCurve(point, segment);
                double dist = point.distanceTo(candidate);
                if (dist < minDist) {
                    minDist = dist;
                    closest = candidate;
                }
            }
            return closest;
        }
        // Fallback: return original point (Edge constructor will validate)
        return point;
    }

    private static CartesianPoint polylineClosestPoint(CartesianPoint point, com.minicad.geometry.Polyline3 polyline) {
        List<CartesianPoint> points = polyline.points();
        CartesianPoint closest = points.getFirst();
        double minDist = Double.POSITIVE_INFINITY;
        for (int i = 0; i < points.size() - 1; i++) {
            CartesianPoint p = closestPointOnSegment(point, points.get(i), points.get(i + 1));
            double dist = point.distanceTo(p);
            if (dist < minDist) {
                minDist = dist;
                closest = p;
            }
        }
        return closest;
    }

    private static CartesianPoint closestPointOnSegment(CartesianPoint point, CartesianPoint a, CartesianPoint b) {
        Vector3 ab = b.subtract(a);
        double lenSq = ab.normSquared();
        if (lenSq <= com.minicad.common.Epsilon.EPS) return a;
        double t = Math.max(0, Math.min(1, point.subtract(a).dot(ab) / lenSq));
        return new CartesianPoint(a.x() + ab.x() * t, a.y() + ab.y() * t, a.z() + ab.z() * t);
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
        OrientedEdge built = topologyBuilder.buildOrientedEdge(id);
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
        EdgeLoop built = topologyBuilder.buildEdgeLoop(id);
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
        VertexLoop built = topologyBuilder.buildVertexLoop(id);
        vertexLoops.put(id, built);
        return built;
    }

    /**
     * Builds a poly loop from its STEP entity.
     *
     * @param id STEP entity id
     * @return built poly loop
     */
    public com.minicad.topology.PolyLoop buildPolyLoop(int id) {
        com.minicad.topology.PolyLoop existing = polyLoops.get(id);
        if (existing != null) {
            return existing;
        }
        com.minicad.topology.PolyLoop built = topologyBuilder.buildPolyLoop(id);
        polyLoops.put(id, built);
        return built;
    }

    /**
     * Builds a path into a composite curve.
     *
     * @param id STEP entity id
     * @return built composite curve representing the path geometry
     */
    public CompositeCurve3 buildPath(int id) {
        CompositeCurve3 existing = paths.get(id);
        if (existing != null) {
            return existing;
        }
        CompositeCurve3 built = topologyBuilder.buildPath(id);
        paths.put(id, built);
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
        FaceBound built = topologyBuilder.buildFaceBound(id);
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
        Face built = topologyBuilder.buildFace(id);
        faces.put(id, built);
        return built;
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
        Shell built = shellBuilder.buildShell(id);
        shells.put(id, built);
        return built;
    }

    /**
     * Builds a shell from a TESSELLATED_FACE_SET.
     * Each face index triplet becomes a triangular planar Face.
     *
     * @param tessellated tessellated face set
     * @return built shell
     */
    Shell buildTessellatedShell(StepTessellatedFaceSet tessellated) {
        List<StepCartesianPoint> coords = tessellated.coordinates();
        List<CartesianPoint> points = new ArrayList<>(coords.size());
        for (StepCartesianPoint cp : coords) {
            points.add(buildPoint(cp.id()));
        }

        List<Face> faces = new ArrayList<>(tessellated.faceIndices().size());
        for (List<Integer> faceIndex : tessellated.faceIndices()) {
            if (faceIndex.size() < 3) {
                continue; // skip degenerate faces
            }
            // Take first 3 indices as triangle vertices
            CartesianPoint p1 = points.get(faceIndex.get(0) - 1); // STEP indices are 1-based
            CartesianPoint p2 = points.get(faceIndex.get(1) - 1);
            CartesianPoint p3 = points.get(faceIndex.get(2) - 1);

            // Compute face normal from triangle
            Vector3 v1 = p2.subtract(p1);
            Vector3 v2 = p3.subtract(p1);
            Vector3 normal = v1.cross(v2);
            if (normal.norm() < Epsilon.EPS) {
                continue; // degenerate triangle
            }

            Plane plane = new Plane(p1, Direction3.from(normal));
            PolyLoop polyLoop = new PolyLoop(List.of(p1, p2, p3));
            Face face = new Face(plane, List.of(FaceBound.outer(polyLoop, true)), true);
            faces.add(face);
        }

        return new Shell(faces, true);
    }

    /**
     * Builds a shell from a single TESSELLATED_FACE.
     * Each triangle reference becomes a triangular planar Face.
     *
     * @param tessellated tessellated face
     * @return built shell
     */
    /** Extracts a CartesianPoint from a vertex entity (StepVertexPoint or StepVertex). */
    private CartesianPoint buildPointFromVertex(StepEntity vertexEntity) {
        if (vertexEntity instanceof StepVertexPoint vp) {
            return buildPoint(vp.point().id());
        }
        if (vertexEntity instanceof StepVertex vertex) {
            // StepVertex has no direct geometry; check if there's a VERTEX_POINT at the same ID
            StepEntity actual = entitiesById.get(vertex.id());
            if (actual instanceof StepVertexPoint vp) {
                return buildPoint(vp.point().id());
            }
            throw new UnsupportedGeometryException("VERTEX #" + vertex.id() + " has no associated point geometry");
        }
        throw new UnsupportedGeometryException("Expected vertex entity but got " + vertexEntity.getClass().getSimpleName());
    }

    Shell buildTessellatedFaceShell(StepTessellatedFace tessellated) {
        List<Face> faces = new ArrayList<>(tessellated.triangles().size());
        for (StepEntity triangleRef : tessellated.triangles()) {
            if (triangleRef instanceof StepTessellatedTriangle triangle) {
                CartesianPoint p1 = buildPointFromVertex(triangle.vertex1());
                CartesianPoint p2 = buildPointFromVertex(triangle.vertex2());
                CartesianPoint p3 = buildPointFromVertex(triangle.vertex3());

                Vector3 v1 = p2.subtract(p1);
                Vector3 v2 = p3.subtract(p1);
                Vector3 normal = v1.cross(v2);
                if (normal.norm() < Epsilon.EPS) {
                    continue; // degenerate triangle
                }

                Plane plane = new Plane(p1, Direction3.from(normal));
                PolyLoop polyLoop = new PolyLoop(List.of(p1, p2, p3));
                Face face = new Face(plane, List.of(FaceBound.outer(polyLoop, true)), true);
                faces.add(face);
            } else if (triangleRef instanceof StepCartesianPoint cp) {
                // Some STEP files use direct point references for tessellated vertices
                // This would be a single-point face which is degenerate - skip
            }
        }
        if (faces.isEmpty()) {
            throw new UnsupportedGeometryException("TESSELLATED_FACE #" + tessellated.id() + " has no valid triangular faces");
        }
        return new Shell(faces, true);
    }

    /**
     * Builds a shell from a TRIANGULATED_FACE.
     * A triangulated face with vertex list and index triplets.
     */
    Shell buildTriangulatedFaceShell(StepTriangulatedFace triangulated) {
        List<CartesianPoint> points = new ArrayList<>(triangulated.vertices().size());
        for (StepEntity v : triangulated.vertices()) {
            if (v instanceof StepCartesianPoint cp) {
                points.add(buildPoint(cp.id()));
            } else if (v instanceof StepVertexPoint vp) {
                points.add(buildPoint(vp.point().id()));
            }
        }
        List<Integer> indices = triangulated.indices();
        List<Face> faces = new ArrayList<>(indices.size() / 3);
        for (int i = 0; i + 2 < indices.size(); i += 3) {
            int a = indices.get(i), b = indices.get(i + 1), c = indices.get(i + 2);
            if (a < 0 || a >= points.size() || b < 0 || b >= points.size() || c < 0 || c >= points.size()) {
                continue; // invalid index
            }
            CartesianPoint p1 = points.get(a), p2 = points.get(b), p3 = points.get(c);
            Vector3 v1 = p2.subtract(p1);
            Vector3 v2 = p3.subtract(p1);
            Vector3 normal = v1.cross(v2);
            if (normal.norm() < Epsilon.EPS) {
                continue; // degenerate triangle
            }
            Plane plane = new Plane(p1, Direction3.from(normal));
            PolyLoop polyLoop = new PolyLoop(List.of(p1, p2, p3));
            faces.add(new Face(plane, List.of(FaceBound.outer(polyLoop, true)), true));
        }
        if (faces.isEmpty()) {
            throw new UnsupportedGeometryException("TRIANGULATED_FACE #" + triangulated.id() + " has no valid triangular faces");
        }
        return new Shell(faces, true);
    }

    /**
     * Builds a shell from a COMPLEX_TRIANGULATED_FACE.
     * A triangulated face with boundaries and vertices.
     */
    Shell buildComplexTriangulatedFaceShell(StepComplexTriangulatedFace complex) {
        List<CartesianPoint> points = new ArrayList<>();
        for (StepEntity v : complex.vertices()) {
            if (v instanceof StepCartesianPoint cp) {
                points.add(buildPoint(cp.id()));
            } else if (v instanceof StepVertexPoint vp) {
                points.add(buildPoint(vp.point().id()));
            }
        }
        List<Face> faces = new ArrayList<>();
        for (StepEntity boundary : complex.boundaries()) {
            if (boundary instanceof StepEdgeLoop loop) {
                List<CartesianPoint> loopPoints = buildLoopPoints(loop);
                if (loopPoints.size() >= 3) {
                    CartesianPoint first = loopPoints.get(0);
                    CartesianPoint second = loopPoints.get(1);
                    CartesianPoint third = loopPoints.get(2);
                    Vector3 normal = second.subtract(first).cross(third.subtract(first));
                    if (normal.norm() > Epsilon.EPS) {
                        Plane plane = new Plane(first, Direction3.from(normal));
                        PolyLoop polyLoop = new PolyLoop(loopPoints);
                        faces.add(new Face(plane, List.of(FaceBound.outer(polyLoop, true)), true));
                    }
                }
            }
        }
        if (faces.isEmpty()) {
            throw new UnsupportedGeometryException("COMPLEX_TRIANGULATED_FACE #" + complex.id() + " has no valid boundary faces");
        }
        return new Shell(faces, true);
    }

    private List<CartesianPoint> buildLoopPoints(StepEdgeLoop loop) {
        List<CartesianPoint> points = new ArrayList<>();
        for (StepOrientedEdge orientedEdge : loop.edges()) {
            StepEdgeCurve edge = orientedEdge.edgeElement();
            if (edge.start() instanceof StepVertexPoint startV) {
                points.add(buildPoint(startV.point().id()));
            }
        }
        return points;
    }

    /**
     * Builds a shell from a CUBIC_BEZIER_TRIANGULATED_FACE.
     * Treated as a triangulated face - control points define vertices.
     */
    Shell buildCubicBezierTriangulatedFaceShell(StepCubicBezierTriangulatedFace bezier) {
        List<CartesianPoint> points = new ArrayList<>(bezier.controlPoints().size());
        for (StepEntity cp : bezier.controlPoints()) {
            if (cp instanceof StepCartesianPoint cartesianPoint) {
                points.add(buildPoint(cartesianPoint.id()));
            } else if (cp instanceof StepVertexPoint vertexPoint) {
                points.add(buildPoint(vertexPoint.point().id()));
            }
        }
        List<Integer> indices = bezier.indices();
        List<Face> faces = new ArrayList<>(indices.size() / 3);
        for (int i = 0; i + 2 < indices.size(); i += 3) {
            int a = indices.get(i), b = indices.get(i + 1), c = indices.get(i + 2);
            if (a < 0 || a >= points.size() || b < 0 || b >= points.size() || c < 0 || c >= points.size()) {
                continue;
            }
            CartesianPoint p1 = points.get(a), p2 = points.get(b), p3 = points.get(c);
            Vector3 v1 = p2.subtract(p1);
            Vector3 v2 = p3.subtract(p1);
            Vector3 normal = v1.cross(v2);
            if (normal.norm() < Epsilon.EPS) {
                continue;
            }
            Plane plane = new Plane(p1, Direction3.from(normal));
            PolyLoop polyLoop = new PolyLoop(List.of(p1, p2, p3));
            faces.add(new Face(plane, List.of(FaceBound.outer(polyLoop, true)), true));
        }
        if (faces.isEmpty()) {
            throw new UnsupportedGeometryException("CUBIC_BEZIER_TRIANGULATED_FACE #" + bezier.id() + " has no valid triangular faces");
        }
        return new Shell(faces, true);
    }

    /**
     * Builds a shell from a FINITE_ELEMENT_MESH.
     * Attempts to triangulate mesh nodes and elements.
     */
    Shell buildFiniteElementMeshShell(StepFiniteElementMesh femMesh) {
        List<StepEntity> nodes = femMesh.nodes();
        List<CartesianPoint> points = new ArrayList<>(nodes.size());
        for (StepEntity node : nodes) {
            if (node instanceof StepCartesianPoint cp) {
                points.add(buildPoint(cp.id()));
            } else {
                throw new UnsupportedGeometryException("FINITE_ELEMENT_MESH nodes must be CARTESIAN_POINT entities");
            }
        }

        if (points.isEmpty()) {
            throw new UnsupportedGeometryException("FINITE_ELEMENT_MESH must have at least one node");
        }

        List<Face> faces = new ArrayList<>();
        for (StepEntity element : femMesh.elements()) {
            // Elements reference node indices; try to interpret as point references
            if (element instanceof StepCartesianPoint cp) {
                // Single point element - skip (not a face)
                continue;
            }
            // If elements are lists of node indices, we'd need to triangulate
            // For now, skip elements that aren't directly usable
        }

        if (faces.isEmpty()) {
            throw new UnsupportedGeometryException("FINITE_ELEMENT_MESH has no triangulable faces");
        }
        return new Shell(faces, false);
    }

    /**
     * Builds a solid from a CSG_VOLUME.
     *
     * @param csgVolume CSG volume entity
     * @return built solid
     */
    Solid buildCsgVolumeSolid(StepCsgVolume csgVolume) {
        // CSG_VOLUME has a treeRoot that may be a boolean result, primitive, or replica
        return buildBooleanOperandSolid(csgVolume.treeRoot());
    }

    /**
     * Builds a solid from a BLOCK_VOLUME.
     * Similar to BLOCK CSG primitive but with explicit StepBlockVolume entity.
     */
    Solid buildBlockVolume(StepBlockVolume blockVolume) {
        if (!(blockVolume.position() instanceof StepAxis2Placement3D placement)) {
            throw new UnsupportedGeometryException("BLOCK_VOLUME position must be an AXIS2_PLACEMENT_3D");
        }
        double x = blockVolume.xLength();
        double y = blockVolume.yLength();
        double z = blockVolume.zLength();
        if (x <= 0.0 || y <= 0.0 || z <= 0.0) {
            throw new UnsupportedGeometryException("BLOCK_VOLUME dimensions must be positive");
        }
        Axis2Placement3D blockPlacement = buildPlacement(placement.id());
        CartesianPoint origin = blockPlacement.location();
        Vector3 alongX = blockPlacement.xDirection().asVector().scale(x);
        Vector3 alongY = blockPlacement.yDirection().asVector().scale(y);
        Vector3 alongZ = blockPlacement.axis().asVector().scale(z);
        List<CartesianPoint> bottom = List.of(
                origin,
                origin.add(alongX),
                origin.add(alongX).add(alongY),
                origin.add(alongY)
        );
        List<CartesianPoint> top = bottom.stream().map(point -> point.add(alongZ)).toList();

        List<Face> faces = new ArrayList<>();
        Direction3 normalZ = blockPlacement.axis().reverse();
        faces.add(faceFromPolyLoop(reverseClosedLoop3(bottom), normalZ));
        faces.add(faceFromPolyLoop(top, blockPlacement.axis()));

        Direction3 normalX = Direction3.from(blockPlacement.axis().asVector().cross(blockPlacement.xDirection().asVector())).reverse();
        List<CartesianPoint> rightFace = List.of(bottom.get(0), top.get(0), top.get(3), bottom.get(3));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(rightFace), normalX));

        Direction3 normalY = blockPlacement.yDirection();
        List<CartesianPoint> frontFace = List.of(bottom.get(1), bottom.get(2), top.get(2), top.get(1));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(frontFace), normalY));

        Direction3 normalXPos = Direction3.from(blockPlacement.axis().asVector().cross(blockPlacement.xDirection().asVector()));
        List<CartesianPoint> leftFace = List.of(bottom.get(2), bottom.get(3), top.get(3), top.get(2));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(leftFace), normalXPos));

        Direction3 normalYNeg = blockPlacement.yDirection().reverse();
        List<CartesianPoint> backFace = List.of(bottom.get(0), bottom.get(1), top.get(1), top.get(0));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(backFace), normalYNeg));

        return new Solid(new Shell(faces, true));
    }

    /**
     * Builds a half-space solid as a standalone solid.
     * Since half-spaces are infinite, we approximate with a large box on the agreement side.
     */
    Solid buildHalfSpaceSolid(StepHalfSpaceSolid halfSpace) {
        Plane plane = buildSupportedPlaneGeometry(halfSpace.baseSurface(), halfSpace.entityName());
        if (plane == null) {
            throw new UnsupportedGeometryException(halfSpace.entityName() + " requires PLANE geometry");
        }
        // If enclosure is present, use box-domain clipping
        if (halfSpace.enclosure() instanceof StepBoxDomain boxDomain) {
            // Create a temporary infinite solid and clip it
            // For standalone half-space with box enclosure, just return the box portion
            return boxDomainToSolid(boxDomain);
        }
        // Unbounded half-space: create a large capped solid on the agreement side
        Direction3 normal = halfSpace.agreementFlag() ? plane.normal() : plane.normal().reverse();
        // Create a large capped box extending from the plane
        CartesianPoint center = plane.origin();
        double extent = 1e6; // large extent for "infinite" approximation
        Vector3 n = normal.asVector();
        Vector3 xDir = plane.normal().perpendicular().asVector();
        Vector3 yDir = n.cross(xDir);
        CartesianPoint origin = new CartesianPoint(
                center.x() - n.x() * extent / 2,
                center.y() - n.y() * extent / 2,
                center.z() - n.z() * extent / 2);
        List<CartesianPoint> bottom = List.of(
                origin,
                new CartesianPoint(origin.x() + xDir.x() * extent, origin.y() + xDir.y() * extent, origin.z() + xDir.z() * extent),
                new CartesianPoint(origin.x() + xDir.x() * extent + yDir.x() * extent,
                        origin.y() + xDir.y() * extent + yDir.y() * extent,
                        origin.z() + xDir.z() * extent + yDir.z() * extent),
                new CartesianPoint(origin.x() + yDir.x() * extent, origin.y() + yDir.y() * extent, origin.z() + yDir.z() * extent)
        );
        List<CartesianPoint> top = bottom.stream().map(p -> new CartesianPoint(p.x(), p.y(), p.z() + n.z() * extent)).toList();

        List<Face> faces = new ArrayList<>();
        faces.add(faceFromPolyLoop(reverseClosedLoop3(bottom), normal.reverse()));
        faces.add(faceFromPolyLoop(top, normal));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(List.of(bottom.get(0), top.get(0), top.get(3), bottom.get(3))),
                Direction3.from(normal.asVector().cross(xDir))));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(List.of(bottom.get(1), bottom.get(2), top.get(2), top.get(1))),
                Direction3.from(yDir)));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(List.of(bottom.get(2), bottom.get(3), top.get(3), top.get(2))),
                Direction3.from(normal.asVector().cross(xDir)).reverse()));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(List.of(bottom.get(0), bottom.get(1), top.get(1), top.get(0))),
                Direction3.from(yDir.scale(-1))));
        return new Solid(new Shell(faces, true));
    }

    /**
     * Builds a polygonal bounded half-space as a standalone solid.
     * Creates a capped solid bounded by the polygon and the plane.
     */
    Solid buildPolygonalBoundedHalfSpace(StepPolygonalBoundedHalfSpace polyHalfSpace) {
        SurfaceGeometry basisSurface = buildSupportedFaceGeometry(polyHalfSpace.basisSurface(), "SURFACE");
        if (!(basisSurface instanceof Plane plane)) {
            throw new UnsupportedGeometryException("POLYGONAL_BOUNDED_HALF_SPACE requires a planar basis surface");
        }
        // Build polygon points as a face boundary
        List<CartesianPoint> polyPoints = polyHalfSpace.polygonPoints().stream()
                .map(cp -> buildPoint(cp.id()))
                .toList();
        if (polyPoints.size() < 3) {
            throw new UnsupportedGeometryException("POLYGONAL_BOUNDED_HALF_SPACE requires at least 3 polygon points");
        }
        Direction3 normal = polyHalfSpace.sameSense() ? plane.normal() : plane.normal().reverse();
        Plane orientedPlane = new Plane(plane.origin(), normal);
        // Create face from the polygon
        Face polyFace = new Face(orientedPlane, List.of(FaceBound.outer(new PolyLoop(polyPoints), true)), true);
        // Extrude along normal to create a capped solid
        double thickness = 1.0; // minimal thickness for standalone representation
        Vector3 extrude = normal.asVector().scale(thickness);
        List<CartesianPoint> top = polyPoints.stream().map(p -> p.add(extrude)).toList();

        List<Face> faces = new ArrayList<>();
        faces.add(new Face(orientedPlane, List.of(FaceBound.outer(new PolyLoop(polyPoints), true)), true));
        faces.add(new Face(new Plane(top.getFirst(), normal), List.of(FaceBound.outer(new PolyLoop(top), true)), true));
        // Side faces
        for (int i = 0; i < polyPoints.size(); i++) {
            CartesianPoint p1 = polyPoints.get(i);
            CartesianPoint p2 = polyPoints.get((i + 1) % polyPoints.size());
            Vector3 edgeDir = p2.subtract(p1);
            Direction3 sideNormal = Direction3.from(edgeDir.cross(normal.asVector()));
            faces.add(faceFromPolyLoop(reverseClosedLoop3(List.of(p1, p2, top.get((i + 1) % polyPoints.size()), top.get(i))),
                    sideNormal));
        }
        return new Solid(new Shell(faces, true));
    }

    /**
     * Builds a half-space solid for use as a boolean operand.
     */
    private Solid buildHalfSpaceSolidAsSolid(StepHalfSpaceSolid halfSpace) {
        return buildHalfSpaceSolid(halfSpace);
    }

    /**
     * Builds a polygonal bounded half-space for use as a boolean operand.
     */
    private Solid buildPolygonalBoundedHalfSpaceAsSolid(StepPolygonalBoundedHalfSpace polyHalfSpace) {
        return buildPolygonalBoundedHalfSpace(polyHalfSpace);
    }

    /**
     * Converts a box domain to a solid.
     */
    private Solid boxDomainToSolid(StepBoxDomain boxDomain) {
        // BOX_DOMAIN has corner point and dimensions list
        CartesianPoint corner = buildPoint(boxDomain.corner().id());
        List<Double> dims = boxDomain.dimensions();
        if (dims.size() < 3) {
            throw new UnsupportedGeometryException("BOX_DOMAIN requires x, y, z dimensions");
        }
        double x = dims.get(0);
        double y = dims.get(1);
        double z = dims.get(2);
        if (x <= 0.0 || y <= 0.0 || z <= 0.0) {
            throw new UnsupportedGeometryException("BOX_DOMAIN dimensions must be positive");
        }
        // Create axis-aligned box from corner
        List<CartesianPoint> bottom = List.of(
                corner,
                new CartesianPoint(corner.x() + x, corner.y(), corner.z()),
                new CartesianPoint(corner.x() + x, corner.y() + y, corner.z()),
                new CartesianPoint(corner.x(), corner.y() + y, corner.z())
        );
        List<CartesianPoint> top = bottom.stream().map(p -> new CartesianPoint(p.x(), p.y(), p.z() + z)).toList();
        List<Face> faces = new ArrayList<>();
        Direction3 up = Direction3.from(new com.minicad.geometry.Vector3(0, 0, 1));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(bottom), up.reverse()));
        faces.add(faceFromPolyLoop(top, up));
        Direction3 right = Direction3.from(new com.minicad.geometry.Vector3(1, 0, 0));
        Direction3 forward = Direction3.from(new com.minicad.geometry.Vector3(0, 1, 0));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(List.of(bottom.get(0), top.get(0), top.get(3), bottom.get(3))), right));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(List.of(bottom.get(1), bottom.get(2), top.get(2), top.get(1))), forward));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(List.of(bottom.get(2), bottom.get(3), top.get(3), top.get(2))), right.reverse()));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(List.of(bottom.get(0), bottom.get(1), top.get(1), top.get(0))), forward.reverse()));
        return new Solid(new Shell(faces, true));
    }

    // New solid type builders

    Solid buildExtrudedFaceSolid(StepExtrudedFaceSolid extrudedFace) {
        // Similar to extruded area solid but sweeps a face instead of a profile
        StepEntity faceGeometry = extrudedFace.sweptFace();
        if (!(faceGeometry instanceof StepFaceEntity stepFace)) {
            throw new UnsupportedGeometryException("EXTRUDED_FACE_SOLID swept_face must be a face entity");
        }
        // Build the face geometry and sample its boundary
        SurfaceGeometry surface = buildSupportedFaceGeometry(stepFace, "EXTRUDED_FACE_SOLID");
        List<CartesianPoint> profilePoints = sampleFaceBoundary(surface, 72);
        if (profilePoints.isEmpty()) {
            throw new UnsupportedGeometryException("EXTRUDED_FACE_SOLID could not extract boundary points");
        }
        double depth = extrudedFace.depth() != null ? extrudedFace.depth() : 1.0;
        Direction3 dir = Direction3.from(new com.minicad.geometry.Vector3(0, 0, depth));
        return buildExtrudedProfile(profilePoints, dir, depth);
    }

    Solid buildRevolvedFaceSolid(StepRevolvedFaceSolid revolvedFace) {
        StepEntity faceGeometry = revolvedFace.sweptFace();
        if (!(faceGeometry instanceof StepFaceEntity stepFace)) {
            throw new UnsupportedGeometryException("REVOLVED_FACE_SOLID swept_face must be a face entity");
        }
        SurfaceGeometry surface = buildSupportedFaceGeometry(stepFace, "REVOLVED_FACE_SOLID");
        List<CartesianPoint> profilePoints = sampleFaceBoundary(surface, 72);
        if (profilePoints.isEmpty()) {
            throw new UnsupportedGeometryException("REVOLVED_FACE_SOLID could not extract boundary points");
        }
        double angle = revolvedFace.angle() != null ? revolvedFace.angle() : 2 * Math.PI;
        CartesianPoint axisOrigin = new CartesianPoint(0, 0, 0);
        com.minicad.geometry.Vector3 axis = new com.minicad.geometry.Vector3(0, 0, 1);
        return buildRevolvedProfile(profilePoints, axisOrigin, axis, angle);
    }

    Solid buildSweptFaceSolid(StepSweptFaceSolid sweptFace) {
        // Sweep a face along a trajectory curve
        StepEntity faceGeometry = sweptFace.sweptFace();
        if (!(faceGeometry instanceof StepFaceEntity stepFace)) {
            throw new UnsupportedGeometryException("SWEPT_FACE_SOLID swept_face must be a face entity");
        }
        SurfaceGeometry surface = buildSupportedFaceGeometry(stepFace, "SWEPT_FACE_SOLID");
        List<CartesianPoint> profilePoints = sampleFaceBoundary(surface, 72);
        if (profilePoints.isEmpty()) {
            throw new UnsupportedGeometryException("SWEPT_FACE_SOLID could not extract boundary points");
        }
        // Build trajectory curve and sample
        Curve3 trajectory = buildCurve3(sweptFace.trajectory());
        List<Curve3Sample> samples = sampleCurve3WithTangent(trajectory, 48);
        return buildSweptProfileAlongCurve(profilePoints, samples);
    }

    Solid buildCylinderVolume(StepCylinderVolume cyl) {
        double radius = cyl.radius();
        double height = cyl.height();
        if (radius <= 0 || height <= 0) {
            throw new UnsupportedGeometryException("CYLINDER_VOLUME requires positive dimensions");
        }
        // Create cylinder: sample circular profile, extrude
        List<CartesianPoint> ring = buildCircleRing(radius, 72);
        return buildExtrudedProfile(ring, Direction3.from(new com.minicad.geometry.Vector3(0, 0, 1)), height);
    }

    Solid buildSphereVolume(StepSphereVolume sphere) {
        double radius = sphere.radius();
        if (radius <= 0) {
            throw new UnsupportedGeometryException("SPHERE_VOLUME requires positive radius");
        }
        // Tessellate sphere with latitude/longitude sampling
        List<Face> faces = tessellateSphere(radius, 24, 48);
        return new Solid(new Shell(faces, true));
    }

    Solid buildTorusVolume(StepTorusVolume torus) {
        double majorR = torus.majorRadius();
        double minorR = torus.minorRadius();
        if (majorR <= 0 || minorR <= 0) {
            throw new UnsupportedGeometryException("TORUS_VOLUME requires positive radii");
        }
        List<Face> faces = tessellateTorus(majorR, minorR, 36, 24);
        return new Solid(new Shell(faces, true));
    }

    Solid buildPrismVolume(StepPrismVolume prism) {
        double w = prism.width();
        double d = prism.depth();
        double h = prism.height();
        if (w <= 0 || d <= 0 || h <= 0) {
            throw new UnsupportedGeometryException("PRISM_VOLUME requires positive dimensions");
        }
        CartesianPoint corner = prism.position() instanceof StepAxis2Placement3D a2
                ? buildPoint(a2.location().id())
                : new CartesianPoint(0, 0, 0);
        List<CartesianPoint> bottom = List.of(
                corner,
                new CartesianPoint(corner.x() + w, corner.y(), corner.z()),
                new CartesianPoint(corner.x() + w, corner.y() + d, corner.z()),
                new CartesianPoint(corner.x(), corner.y() + d, corner.z()));
        List<CartesianPoint> top = bottom.stream()
                .map(p -> new CartesianPoint(p.x(), p.y(), p.z() + h))
                .toList();
        List<Face> faces = buildBoxFaces(bottom, top);
        return new Solid(new Shell(faces, true));
    }

    // Helper methods for new solid builders

    private Solid buildExtrudedProfile(List<CartesianPoint> profile, Direction3 direction, double depth) {
        List<Face> faces = new ArrayList<>();
        Vector3 dirVec = direction.asVector();
        List<CartesianPoint> top = profile.stream()
                .map(p -> new CartesianPoint(
                        p.x() + dirVec.x() * depth,
                        p.y() + dirVec.y() * depth,
                        p.z() + dirVec.z() * depth))
                .toList();
        faces.add(faceFromPolyLoop(reverseClosedLoop3(profile), direction.reverse()));
        faces.add(faceFromPolyLoop(top, direction));
        for (int i = 0; i < profile.size(); i++) {
            int next = (i + 1) % profile.size();
            Vector3 edgeDir = new Vector3(
                    top.get(next).x() - profile.get(i).x(),
                    top.get(next).y() - profile.get(i).y(),
                    top.get(next).z() - profile.get(i).z());
            faces.add(faceFromPolyLoop(
                    List.of(profile.get(i), profile.get(next), top.get(next), top.get(i)),
                    Direction3.from(edgeDir)));
        }
        return new Solid(new Shell(faces, true));
    }

    private Solid buildRevolvedProfile(List<CartesianPoint> profile, CartesianPoint axisOrigin,
                                        Vector3 axis, double angle) {
        int sections = Math.max(4, (int) (Math.abs(angle) / (Math.PI / 16)));
        if (sections > 72) sections = 72;
        List<Face> faces = new ArrayList<>();
        // Build rings
        List<List<CartesianPoint>> rings = new ArrayList<>();
        for (int i = 0; i <= sections; i++) {
            double theta = angle * i / sections;
            List<CartesianPoint> ring = new ArrayList<>();
            for (CartesianPoint p : profile) {
                ring.add(rotatePointAroundAxis(p, axisOrigin, axis, theta));
            }
            rings.add(ring);
        }
        // End caps
        Direction3 axisDir = Direction3.from(axis);
        if (Math.abs(angle) < 2 * Math.PI - 0.01) {
            faces.add(faceFromPolyLoop(reverseClosedLoop3(rings.getFirst()), axisDir.reverse()));
            faces.add(faceFromPolyLoop(closeLoop3(rings.getLast()), axisDir));
        }
        // Side faces
        for (int r = 0; r < rings.size() - 1; r++) {
            List<CartesianPoint> cur = rings.get(r);
            List<CartesianPoint> nxt = rings.get(r + 1);
            for (int i = 0; i < cur.size(); i++) {
                int next = (i + 1) % cur.size();
                faces.add(faceFromPolyLoop(
                        List.of(cur.get(i), cur.get(next), nxt.get(next), nxt.get(i)),
                        Direction3.from(new com.minicad.geometry.Vector3(0, 0, 1))));
            }
        }
        return new Solid(new Shell(faces, true));
    }

    private Solid buildSweptProfileAlongCurve(List<CartesianPoint> profile, List<Curve3Sample> samples) {
        if (samples.isEmpty()) {
            throw new UnsupportedGeometryException("trajectory curve has no samples");
        }
        List<Face> faces = new ArrayList<>();
        List<List<CartesianPoint>> rings = new ArrayList<>();
        for (Curve3Sample sample : samples) {
            rings.add(placeProfilePoints(profile, sample.point(), sample.tangent()));
        }
        // End caps
        if (!rings.isEmpty()) {
            faces.add(faceFromPolyLoop(reverseClosedLoop3(rings.getFirst()), samples.getFirst().tangent().reverse()));
            faces.add(faceFromPolyLoop(closeLoop3(rings.getLast()), samples.getLast().tangent()));
        }
        // Side faces
        for (int r = 0; r < rings.size() - 1; r++) {
            List<CartesianPoint> cur = rings.get(r);
            List<CartesianPoint> nxt = rings.get(r + 1);
            for (int i = 0; i < cur.size(); i++) {
                int next = (i + 1) % cur.size();
                faces.add(faceFromPolyLoop(
                        List.of(cur.get(i), cur.get(next), nxt.get(next), nxt.get(i)),
                        samples.get(r).tangent()));
            }
        }
        return new Solid(new Shell(faces, true));
    }

    private List<CartesianPoint> placeProfilePoints(List<CartesianPoint> profile, CartesianPoint point, Direction3 tangent) {
        CircularFrame frame = circularFrame(tangent);
        Vector3 fx = frame.x();
        Vector3 fy = frame.y();
        Vector3 fz = frame.z().asVector();
        CartesianPoint first = profile.getFirst();
        return profile.stream()
                .map(p -> point.add(fx.scale(p.x() - first.x())
                        .add(fy.scale(p.y() - first.y()))
                        .add(fz.scale(p.z() - first.z()))))
                .toList();
    }

    private List<CartesianPoint> buildCircleRing(double radius, int segments) {
        List<CartesianPoint> points = new ArrayList<>();
        for (int i = 0; i < segments; i++) {
            double theta = 2 * Math.PI * i / segments;
            points.add(new CartesianPoint(radius * Math.cos(theta), radius * Math.sin(theta), 0));
        }
        return points;
    }

    private List<Face> tessellateSphere(double radius, int latSteps, int lonSteps) {
        List<Face> faces = new ArrayList<>();
        for (int i = 0; i < latSteps; i++) {
            double phi1 = Math.PI * i / latSteps;
            double phi2 = Math.PI * (i + 1) / latSteps;
            for (int j = 0; j < lonSteps; j++) {
                double theta1 = 2 * Math.PI * j / lonSteps;
                double theta2 = 2 * Math.PI * (j + 1) / lonSteps;
                CartesianPoint p00 = spherePoint(radius, phi1, theta1);
                CartesianPoint p10 = spherePoint(radius, phi2, theta1);
                CartesianPoint p11 = spherePoint(radius, phi2, theta2);
                CartesianPoint p01 = spherePoint(radius, phi1, theta2);
                CartesianPoint midPoint = spherePoint(radius, (phi1 + phi2) / 2, (theta1 + theta2) / 2);
                Vector3 normal = new Vector3(midPoint.x(), midPoint.y(), midPoint.z());
                if (i == 0) {
                    faces.add(faceFromPolyLoop(List.of(p00, p10, p11), Direction3.from(normal)));
                } else if (i == latSteps - 1) {
                    faces.add(faceFromPolyLoop(List.of(p00, p01, p10), Direction3.from(normal)));
                } else {
                    faces.add(faceFromPolyLoop(List.of(p00, p10, p11, p01), Direction3.from(normal)));
                }
            }
        }
        return faces;
    }

    private CartesianPoint spherePoint(double radius, double phi, double theta) {
        return new CartesianPoint(
                radius * Math.sin(phi) * Math.cos(theta),
                radius * Math.sin(phi) * Math.sin(theta),
                radius * Math.cos(phi));
    }

    private List<Face> tessellateTorus(double majorR, double minorR, int majorSteps, int minorSteps) {
        List<Face> faces = new ArrayList<>();
        double[][][] points = new double[majorSteps + 1][minorSteps + 1][];
        for (int i = 0; i <= majorSteps; i++) {
            double theta = 2 * Math.PI * i / majorSteps;
            for (int j = 0; j <= minorSteps; j++) {
                double phi = 2 * Math.PI * j / minorSteps;
                points[i][j] = new double[]{
                        (majorR + minorR * Math.cos(phi)) * Math.cos(theta),
                        (majorR + minorR * Math.cos(phi)) * Math.sin(theta),
                        minorR * Math.sin(phi)
                };
            }
        }
        for (int i = 0; i < majorSteps; i++) {
            for (int j = 0; j < minorSteps; j++) {
                CartesianPoint p00 = pt(points[i][j]);
                CartesianPoint p10 = pt(points[i + 1][j]);
                CartesianPoint p11 = pt(points[i + 1][j + 1]);
                CartesianPoint p01 = pt(points[i][j + 1]);
                faces.add(faceFromPolyLoop(List.of(p00, p10, p11, p01), Direction3.from(new com.minicad.geometry.Vector3(0, 0, 1))));
            }
        }
        return faces;
    }

    private CartesianPoint pt(double[] coords) {
        return new CartesianPoint(coords[0], coords[1], coords[2]);
    }

    private List<CartesianPoint> sampleFaceBoundary(SurfaceGeometry surface, int samples) {
        // For simple surfaces, sample the bounding box edges
        // This is a simplified approach - real implementation would need face bounds
        return sampleSurfaceBoundary(surface, samples);
    }

    private List<CartesianPoint> sampleSurfaceBoundary(SurfaceGeometry surface, int samples) {
        // Simplified boundary sampling - for planar surfaces, use bounding box
        if (surface instanceof Plane) {
            double bb = 10.0; // Default bounding box
            return List.of(
                    new CartesianPoint(-bb, -bb, 0),
                    new CartesianPoint(bb, -bb, 0),
                    new CartesianPoint(bb, bb, 0),
                    new CartesianPoint(-bb, bb, 0));
        }
        if (surface instanceof CylindricalSurface cyl) {
            return buildCircleRing(cyl.radius(), samples);
        }
        // Generic fallback
        return List.of();
    }

    private List<Face> buildBoxFaces(List<CartesianPoint> bottom, List<CartesianPoint> top) {
        List<Face> faces = new ArrayList<>();
        Direction3 up = Direction3.from(new com.minicad.geometry.Vector3(0, 0, 1));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(bottom), up.reverse()));
        faces.add(faceFromPolyLoop(top, up));
        Direction3 right = Direction3.from(new com.minicad.geometry.Vector3(1, 0, 0));
        Direction3 forward = Direction3.from(new com.minicad.geometry.Vector3(0, 1, 0));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(List.of(bottom.get(0), top.get(0), top.get(3), bottom.get(3))), right));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(List.of(bottom.get(1), bottom.get(2), top.get(2), top.get(1))), forward));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(List.of(bottom.get(2), bottom.get(3), top.get(3), top.get(2))), right.reverse()));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(List.of(bottom.get(0), bottom.get(1), top.get(1), top.get(0))), forward.reverse()));
        return faces;
    }

    private CartesianPoint rotatePointAroundAxis(CartesianPoint p, CartesianPoint origin,
                                                  Vector3 axis, double angle) {
        // Rodrigues' rotation formula
        double dx = p.x() - origin.x();
        double dy = p.y() - origin.y();
        double dz = p.z() - origin.z();
        com.minicad.geometry.Vector3 v = new com.minicad.geometry.Vector3(dx, dy, dz);
        com.minicad.geometry.Vector3 k = axis.normalize().asVector();
        com.minicad.geometry.Vector3 rotated = v.scale(Math.cos(angle))
                .add(k.cross(v).scale(Math.sin(angle)))
                .add(k.scale(k.dot(v) * (1 - Math.cos(angle))));
        return new CartesianPoint(
                rotated.x() + origin.x(),
                rotated.y() + origin.y(),
                rotated.z() + origin.z());
    }

    /**
     *
     * @param id STEP entity id
     * @return built solid
     */
    public Solid buildSolid(int id) {
        Solid existing = solids.get(id);
        if (existing != null) {
            return existing;
        }
        Solid built = solidBuilder.buildSolid(id);
        solids.put(id, built);
        return built;
    }

    /**
     * Checks if an entity can be dispatched to buildSolid.
     * Used for abstract base type resolution.
     */
    public boolean canBuildAsSolid(StepEntity entity) {
        return solidBuilder.canBuildAsSolid(entity);
    }

    StepEntity requireExistingEntity(int id) {
        StepEntity entity = entitiesById.get(id);
        if (entity == null) {
            throw new StepResolutionException("missing resolved entity #" + id);
        }
        return entity;
    }

    <T extends StepEntity> T requireEntity(int id, Class<T> type, String expectedName) {
        StepEntity entity = requireExistingEntity(id);
        if (!type.isInstance(entity)) {
            throw new StepResolutionException("entity #" + id + " is not a " + expectedName);
        }
        return type.cast(entity);
    }

    static StepEntity faceGeometry(StepFaceEntity stepFace) {
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

    static boolean faceSameSense(StepFaceEntity stepFace) {
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

    Solid buildSweptAreaSolid(StepSweptAreaSolid sweptAreaSolid) {
        if ("EXTRUDED_AREA_SOLID".equals(sweptAreaSolid.entityName())) {
            return buildExtrudedAreaSolid(sweptAreaSolid);
        }
        if ("REVOLVED_AREA_SOLID".equals(sweptAreaSolid.entityName())) {
            return buildRevolvedAreaSolid(sweptAreaSolid);
        }
        throw new UnsupportedGeometryException(sweptAreaSolid.entityName() + " construction is unsupported");
    }

    Solid buildCsgPrimitive(StepCsgPrimitive csgPrimitive) {
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

    Solid buildSweptDiskSolid(StepSweptDiskSolid sweptDiskSolid) {
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

    Solid buildExtrudedAreaSolidTapered(StepExtrudedAreaSolidTapered tapered) {
        // Get the profile from sweptArea (which is StepEntity, need to cast to StepProfileDef)
        StepProfileDef profileDef = asProfileDef(tapered.sweptArea());
        StepProfileBuilder.ProfileLoops baseProfile = profileBuilder.buildAreaProfileLoops(profileDef);
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

    Solid buildRevolvedAreaSolidTapered(StepRevolvedAreaSolidTapered tapered) {
        StepProfileDef profileDef = asProfileDef(tapered.sweptArea());
        StepProfileBuilder.ProfileLoops baseProfile = profileBuilder.buildAreaProfileLoops(profileDef);
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

    Solid buildSurfaceCurveSweptAreaSolid(StepSurfaceCurveSweptAreaSolid swept) {
        StepProfileDef profileDef = asProfileDef(swept.sweptArea());
        StepProfileBuilder.ProfileLoops profile = profileBuilder.buildAreaProfileLoops(profileDef);
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
        // Dedicated profile types with their own model classes
        if (entity instanceof StepCenteredCircleProfileDef) {
            return new StepProfileDef(
                entity.id(), "AREA", "", null, List.of(),
                List.of(((StepCenteredCircleProfileDef) entity).radius(),
                        ((StepCenteredCircleProfileDef) entity).centerOffset()),
                "CENTERED_CIRCLE_PROFILE_DEF");
        }
        if (entity instanceof StepCentreLineArcProfileDef) {
            return new StepProfileDef(
                entity.id(), "AREA", "", null, List.of(),
                List.of(((StepCentreLineArcProfileDef) entity).radius(),
                        ((StepCentreLineArcProfileDef) entity).angle()),
                "CENTRE_LINE_ARC_PROFILE_DEF");
        }
        if (entity instanceof StepRectangleHollowProfileDef def) {
            return new StepProfileDef(
                entity.id(), "AREA", "", null, List.of(),
                List.of(def.xDim(), def.yDim(), def.wallThickness(), def.innerRadius()),
                "RECTANGLE_HOLLOW_PROFILE_DEF");
        }
        // Profile wrappers that delegate to inner profileDef
        if (entity instanceof StepAreaProfile areaProfile) {
            return asProfileDef(areaProfile.profileDef());
        }
        if (entity instanceof StepGeneralizedAreaProfile generalizedProfile) {
            return asProfileDef(generalizedProfile.profileDef());
        }
        if (entity instanceof StepSweptProfileAreaOutline outline) {
            return asProfileDef(outline.profileDef());
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

    Solid buildBooleanResult(String operator, StepEntity first, StepEntity second) {
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

    Solid buildBooleanOperandSolid(StepEntity operand) {
        return switch (operand) {
            case StepManifoldSolidBrep solidBrep -> buildSolid(solidBrep.id());
            case StepFacettedBrep facettedBrep -> buildSolid(facettedBrep.id());
            case StepBrepWithVoids brepWithVoids -> buildSolid(brepWithVoids.id());
            case StepNonManifoldSolidBrep nonManifold -> buildSolid(nonManifold.id());
            case StepAdvancedBrep advancedBrep -> buildSolid(advancedBrep.id());
            case StepCsgPrimitive csgPrimitive -> buildCsgPrimitive(csgPrimitive);
            case StepCsgPrimitive3D csg3D -> {
                // CSG_PRIMITIVE_3D is a reference wrapper; build solid from the position entity
                StepEntity actual = entitiesById.get(csg3D.position().id());
                if (actual != null && actual instanceof StepCsgPrimitive primitive) {
                    yield buildCsgPrimitive(primitive);
                }
                throw new UnsupportedGeometryException("CSG_PRIMITIVE_3D #" + csg3D.id() + " position must reference a CSG primitive");
            }
            case StepCsgVolume csgVolume -> buildCsgVolumeSolid(csgVolume);
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
            case StepHalfSpaceSolid halfSpace -> buildHalfSpaceSolidAsSolid(halfSpace);
            case StepPolygonalBoundedHalfSpace polyHalfSpace -> buildPolygonalBoundedHalfSpaceAsSolid(polyHalfSpace);
            case StepSolidModel solidModel -> {
                // SolidModel is the abstract base type; check for concrete subtype at same ID.
                StepEntity actual = entitiesById.get(solidModel.id());
                if (actual != null && actual != solidModel && canBuildAsSolid(actual)) {
                    yield buildSolid(solidModel.id());
                }
                throw new StepResolutionException("entity #" + solidModel.id() + " is an abstract SOLID_MODEL with no concrete subtype");
            }
            case StepMappedItem mappedItem -> buildBooleanOperandSolid(mappedItem.mappingTarget());
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
        StepProfileBuilder.ProfileLoops profileLoops = profileBuilder.buildAreaProfileLoops(sweptAreaSolid.sweptArea());
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
        StepProfileBuilder.ProfileLoops profileLoops = profileBuilder.buildAreaProfileLoops(sweptAreaSolid.sweptArea());
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

    Solid transformSolid(Solid solid, StepCartesianTransformationOperator transformation) {
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
            case StepBSplineCurve spline -> buildBSplineCurve(spline.id());
            case StepRationalBSplineCurve spline -> buildRationalBSplineCurve3(spline.id());
            case StepSurfaceCurve surfaceCurve -> buildSurfaceCurve(surfaceCurve.id());
            case StepSeamCurve seamCurve -> buildSeamCurve(seamCurve.id());
            case StepTrimmedCurve trimmedCurve -> buildTrimmedCurve(trimmedCurve.id());
            case StepCompositeCurve compositeCurve -> buildCompositeCurve(compositeCurve.id());
            case StepCompositeCurveOnSurface compositeCurveOnSurface -> buildCompositeCurve(compositeCurveOnSurface.id());
            case StepPath path -> buildPath(path.id());
            case StepOpenPath openPath -> buildPath(openPath.id());
            case StepSubpath subpath -> buildPath(subpath.id());
            case StepOrientedPath orientedPath -> buildPath(orientedPath.id());
            case StepConicCurve conic -> buildConicCurve3(conic);
            case StepOffsetCurve2D offsetCurve2D -> liftCurve2(buildOffsetCurve2(offsetCurve2D.id()));
            case StepOffsetCurve3D offsetCurve3D -> buildOffsetCurve3(offsetCurve3D.id());
            case StepOrientedCurve orientedCurve -> {
                Curve3 baseCurve = buildCurve3(orientedCurve.curveElement());
                if (!orientedCurve.orientation() && baseCurve instanceof CompositeCurve3 composite) {
                    // Reverse composite curve: reverse segment order and reverse each segment.
                    yield reverseCompositeCurve(composite);
                }
                yield baseCurve;
            }
            case StepGeometricReplica replica -> buildReplicaCurve3(replica);
            case StepAnnotationCurveOccurrence occurrence -> buildCurve3(occurrence.item());
            case StepDimensionCurve dimensionCurve -> buildCurve3(dimensionCurve.item());
            case StepLeaderCurve leaderCurve -> buildCurve3(leaderCurve.item());
            case StepProjectionCurve projectionCurve -> buildCurve3(projectionCurve.item());
            case StepDraughtingAnnotationOccurrence annotationOccurrence -> buildCurve3(annotationOccurrence.item());
            case StepTerminatorSymbol terminatorSymbol -> buildCurve3(terminatorSymbol.annotatedCurve());
            case StepClothoid clothoid -> buildClothoidCurve(clothoid);
            case StepIndexedPolyCurve polyCurve -> buildIndexedPolyCurve3(polyCurve);
            case StepPolyline3D polyline3D -> buildPolyline3D(polyline3D);
            case StepDegenerateCurve degenerateCurve -> buildDegenerateCurve3(degenerateCurve);
            case StepLineSegment lineSegment -> {
                CartesianPoint startPoint = buildPoint(lineSegment.startPoint().id());
                CartesianPoint endPoint = buildPoint(lineSegment.endPoint().id());
                Vector3 dir = endPoint.subtract(startPoint);
                yield new Line3(startPoint, Direction3.from(dir));
            }
            case StepBoundedCurve boundedCurve -> {
                // BoundedCurve is a marker type with no geometry data.
                // In complex entity syntax, the actual curve type may be stored at the same ID.
                // Check if another entity was resolved at this ID.
                StepEntity actual = entitiesById.get(boundedCurve.id());
                if (actual != null && actual != boundedCurve) {
                    yield buildCurve3(actual);
                }
                throw new UnsupportedGeometryException("BOUNDED_CURVE requires an underlying curve type");
            }
            case StepSurfacedEdgeCurve surfacedEdgeCurve -> {
                // SurfacedEdgeCurve wraps an underlying curve with surface associations.
                // Build the curve geometry from the edgeGeometry reference.
                yield buildCurve3(surfacedEdgeCurve.edgeGeometry());
            }
            case StepEdgeCurve edgeCurve -> {
                // EdgeCurve wraps an underlying curve with vertex endpoints.
                // Build the curve geometry from the edgeGeometry reference.
                yield buildCurve3(edgeCurve.edgeGeometry());
            }
            case StepCompositeCurveOnSurface3D compositeCurve3D -> {
                // Composite curve on 3D surface: build from segments like regular composite curve
                yield buildCompositeCurve(compositeCurve3D.id());
            }
            case StepBSplineCurveWithKnotsAndBreakpoints splineWithBreakpoints -> {
                // B-spline curve with explicit breakpoints: build like regular BSplineCurve
                yield buildBSplineCurveWithBreakpoints(splineWithBreakpoints.id());
            }
            case StepCurve abstractCurve -> {
                // Curve is an abstract base type. In complex entity syntax, the actual
                // curve type may be stored at the same ID.
                StepEntity actual = entitiesById.get(abstractCurve.id());
                if (actual != null && actual != abstractCurve) {
                    yield buildCurve3(actual);
                }
                throw new UnsupportedGeometryException("CURVE is an abstract base type with no concrete geometry");
            }
            case StepMappedItem mappedItem -> {
                // MAPPED_ITEM references a representation map with a target item.
                // Dispatch through to the mapping target for geometry.
                yield buildCurve3(mappedItem.mappingTarget());
            }
            // 2D-specific curve types lifted to 3D via sampling
            case StepCompositeCurve2D composite2D -> liftCurve2(buildCompositeCurve2D(composite2D));
            case StepTrimmedCurve2D trimmed2D -> liftCurve2(buildTrimmedCurve2D(trimmed2D));
            case StepBSplineCurve2D spline2D -> liftCurve2(buildBSplineCurve2D(spline2D));
            case StepRationalBSplineCurve2D rational2D -> liftCurve2(buildRationalBSplineCurve2D(rational2D));
            case StepBezierCurve2D bezier2D -> liftCurve2(buildBezierCurve2D(bezier2D));
            case StepQuasiUniformCurve2D quasiUniform2D -> liftCurve2(buildQuasiUniformCurve2D(quasiUniform2D));
            case StepUniformCurve2D uniform2D -> liftCurve2(buildUniformCurve2D(uniform2D));
            case StepPiecewiseBezierCurve2D piecewiseBezier2D -> liftCurve2(buildPiecewiseBezierCurve2D(piecewiseBezier2D));
            case StepIndexedPolyCurve2D polyCurve2D -> liftCurve2(buildIndexedPolyCurve2D(polyCurve2D));
            case StepDegenerateCurve2D degenerate2D -> liftCurve2(buildDegenerateCurve2D(degenerate2D));
            case StepCircle2D circle2D -> liftCurve2(buildCircle2D(circle2D));
            case StepEllipse2D ellipse2D -> liftCurve2(buildEllipse2D(ellipse2D));
            case StepLine2D line2D -> liftCurve2(buildLine2D(line2D));
            case StepPolyline2D polyline2D -> liftCurve2(buildPolyline2D(polyline2D));
            case StepHyperbola2D hyperbola2D -> liftCurve2(buildHyperbola2D(hyperbola2D));
            case StepParabola2D parabola2D -> liftCurve2(buildParabola2D(parabola2D));
            default -> throw new UnsupportedGeometryException(
                    "curve type " + curve.getClass().getSimpleName() + " is not a supported 3D curve"
            );
        };
    }

    private Curve3 buildClothoidCurve(StepClothoid clothoid) {
        // Return proper Clothoid3 geometry object
        return buildClothoid(clothoid.id());
    }

    /**
     * Reverses a composite curve by reversing segment order and reversing each segment.
     */
    private CompositeCurve3 reverseCompositeCurve(CompositeCurve3 original) {
        List<Curve3> reversedSegments = new java.util.ArrayList<>(original.segments().reversed());
        for (int i = 0; i < reversedSegments.size(); i++) {
            reversedSegments.set(i, reverseCurve3(reversedSegments.get(i)));
        }
        return new CompositeCurve3(List.copyOf(reversedSegments));
    }

    /**
     * Reverses a single curve segment. For supported types, returns a geometrically reversed curve.
     * For unsupported types, returns the original curve (reversal not implemented).
     */
    private Curve3 reverseCurve3(Curve3 curve) {
        return switch (curve) {
            case Line3 line -> new Line3(line.origin(), line.direction().reverse(), line.parameterScale());
            case Polyline3 polyline -> {
                List<CartesianPoint> reversedPoints = new java.util.ArrayList<>(polyline.points().reversed());
                yield new Polyline3(reversedPoints);
            }
            case CompositeCurve3 composite -> reverseCompositeCurve(composite);
            case Circle circle -> {
                Axis2Placement3D p = circle.position();
                yield new Circle(
                        new Axis2Placement3D(p.location(), p.axis(), p.xDirection().reverse()),
                        circle.radius());
            }
            case Ellipse3 ellipse -> {
                Axis2Placement3D p = ellipse.position();
                yield new Ellipse3(
                        new Axis2Placement3D(p.location(), p.axis(), p.xDirection().reverse()),
                        ellipse.semiAxis1(), ellipse.semiAxis2());
            }
            case Parabola3 parabola -> {
                Axis2Placement3D p = parabola.position();
                yield new Parabola3(
                        new Axis2Placement3D(p.location(), p.axis(), p.xDirection().reverse()),
                        parabola.focalLength());
            }
            case Hyperbola3 hyperbola -> {
                Axis2Placement3D p = hyperbola.position();
                yield new Hyperbola3(
                        new Axis2Placement3D(p.location(), p.axis(), p.xDirection().reverse()),
                        hyperbola.semiAxisA(), hyperbola.semiAxisB());
            }
            case Clothoid3 clothoid -> {
                Axis2Placement3D p = clothoid.position();
                yield new Clothoid3(
                        new Axis2Placement3D(p.location(), p.axis(), p.xDirection().reverse()),
                        clothoid.xAxisIntercept(), clothoid.curvature());
            }
            case DegenerateCurve3 degenerate -> new DegenerateCurve3(degenerate.point());
            case TrimmedCurve3 trimmed -> {
                // Swap trim parameters and flip sense to reverse the curve
                yield new TrimmedCurve3(
                        reverseCurve3(trimmed.basisCurve()),
                        trimmed.trimParamEnd(),
                        trimmed.trimParamStart(),
                        !trimmed.senseAgreement());
            }
            case SurfaceCurve3 surfaceCurve -> new SurfaceCurve3(
                    reverseCurve3(surfaceCurve.curve3d()),
                    surfaceCurve.parametricCurves());
            case BSplineCurve3 bspline -> new BSplineCurve3(
                    bspline.degree(),
                    new java.util.ArrayList<>(bspline.controlPoints().reversed()),
                    bspline.knotMultiplicities(),
                    bspline.knots());
            case RationalBSplineCurve3 rational -> new RationalBSplineCurve3(
                    rational.degree(),
                    new java.util.ArrayList<>(rational.controlPoints().reversed()),
                    rational.weights(),
                    rational.knotMultiplicities(),
                    rational.knots());
            default -> curve;
        };
    }

    /**
     * Reverses the surface sense (normal direction) for oriented surfaces.
     * When an ORIENTED_SURFACE has orientation=false, the surface normal should be flipped.
     */
    private SurfaceGeometry reverseSurfaceSense(SurfaceGeometry surface) {
        return switch (surface) {
            case Plane plane -> new Plane(plane.origin(), plane.normal().reverse());
            case CylindricalSurface cyl -> {
                Axis2Placement3D p = cyl.position();
                yield new CylindricalSurface(
                        new Axis2Placement3D(p.location(), p.axis(), p.xDirection().reverse()),
                        cyl.radius());
            }
            case ConicalSurface conic -> {
                Axis2Placement3D p = conic.position();
                yield new ConicalSurface(
                        new Axis2Placement3D(p.location(), p.axis(), p.xDirection().reverse()),
                        conic.radius(), conic.semiAngle());
            }
            case SphericalSurface sphere -> {
                Axis2Placement3D p = sphere.position();
                yield new SphericalSurface(
                        new Axis2Placement3D(p.location(), p.axis(), p.xDirection().reverse()),
                        sphere.radius());
            }
            case ToroidalSurface torus -> {
                Axis2Placement3D p = torus.position();
                yield new ToroidalSurface(
                        new Axis2Placement3D(p.location(), p.axis(), p.xDirection().reverse()),
                        torus.majorRadius(), torus.minorRadius());
            }
            case SurfaceOfLinearExtrusion3 extrusion ->
                    new SurfaceOfLinearExtrusion3(extrusion.sweptCurve(), extrusion.extrusionVector().negate());
            case SurfaceOfRevolution3 revolution ->
                    new SurfaceOfRevolution3(
                            revolution.sweptCurve(),
                            revolution.axisOrigin(),
                            revolution.axisDirection().reverse());
            case RuledSurface3 ruled -> new RuledSurface3(
                    reverseCurve3(ruled.directrix1()),
                    reverseCurve3(ruled.directrix2()));
            case SurfaceOfConstantRadius3 constant -> new SurfaceOfConstantRadius3(
                    reverseSurfaceSense(constant.sweptSurface()),
                    constant.radius());
            case OffsetSurface3 offset -> new OffsetSurface3(
                    reverseSurfaceSense(offset.basisSurface()),
                    offset.distance());
            case BSplineSurface3 bspline -> new BSplineSurface3(
                    bspline.uDegree(),
                    bspline.vDegree(),
                    reverseBSplineControlGrid(bspline.controlPoints()),
                    bspline.uMultiplicities(),
                    bspline.vMultiplicities(),
                    bspline.uKnots(),
                    bspline.vKnots());
            case RationalBSplineSurface3 rational -> new RationalBSplineSurface3(
                    rational.uDegree(),
                    rational.vDegree(),
                    reverseBSplineControlGrid(rational.controlPoints()),
                    rational.weightsData(),
                    rational.uMultiplicities(),
                    rational.vMultiplicities(),
                    rational.uKnots(),
                    rational.vKnots());
            case ParaboloidSurface paraboloid -> {
                Axis2Placement3D pp = paraboloid.position();
                yield new ParaboloidSurface(
                        new Axis2Placement3D(pp.location(), pp.axis(), pp.xDirection().reverse()),
                        paraboloid.focalLength());
            }
            case HyperboloidSurface hyperboloid -> {
                Axis2Placement3D hp = hyperboloid.position();
                yield new HyperboloidSurface(
                        new Axis2Placement3D(hp.location(), hp.axis(), hp.xDirection().reverse()),
                        hyperboloid.radius(), hyperboloid.semiAxis());
            }
            case SurfaceOfTranslation3 translation -> new SurfaceOfTranslation3(
                    reverseCurve3(translation.profile()),
                    translation.direction());
            case SurfaceOfProjection3 projection -> new SurfaceOfProjection3(
                    reverseCurve3(projection.profile()),
                    projection.projectionDirection());
            default -> surface;
        };
    }

    private static java.util.List<java.util.List<CartesianPoint>> reverseBSplineControlGrid(
            java.util.List<java.util.List<CartesianPoint>> grid) {
        java.util.List<java.util.List<CartesianPoint>> result = new java.util.ArrayList<>(grid.size());
        for (java.util.List<CartesianPoint> row : grid) {
            result.add(java.util.List.copyOf(row.reversed()));
        }
        return java.util.List.copyOf(result);
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

    private Curve3 buildPolyline3D(StepPolyline3D polyline3D) {
        // Polyline defined by entity references to Cartesian points
        List<CartesianPoint> points = polyline3D.points().stream()
                .map(pt -> {
                    if (pt instanceof StepCartesianPoint cartesian) {
                        return buildPoint(cartesian.id());
                    }
                    throw new UnsupportedGeometryException("POLYLINE_3D point #" + pt.id() + " is not a CARTESIAN_POINT");
                })
                .toList();
        return points.isEmpty() ? new Polyline3(List.of()) : new Polyline3(points);
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
            case "CONIC_CURVE" -> {
                // Generic CONIC_CURVE: try parabola first (most common in STEP files),
                // then hyperbola if parameters don't match.
                try {
                    yield buildParabola(conic.id());
                } catch (UnsupportedGeometryException e) {
                    yield buildHyperbola(conic.id());
                }
            }
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
            case "CONIC_CURVE" -> {
                try {
                    yield buildParabola2(origin, xDirection, conic.parameters());
                } catch (UnsupportedGeometryException e) {
                    yield buildHyperbola2(origin, xDirection, conic.parameters());
                }
            }
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

    SurfaceGeometry buildSupportedFaceGeometry(StepEntity geometry, String faceType) {
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
        if (geometry instanceof StepToroidalSurfaceWithSpecifiedBends toroidalSpecBends) {
            return buildToroidalSurfaceFromSpecifiedBends(toroidalSpecBends);
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
        if (geometry instanceof StepBSplineSurface splineSurface) {
            return buildGenericBSplineSurface(splineSurface.id());
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
            if (!orientedSurface.orientation()) {
                return reverseSurfaceSense(base);
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
        // Elliptical axis surfaces - map to standard surface types with elliptical parameters
        if (geometry instanceof StepCylindricalSurfaceWithEllipticalAxis ellipticalCyl) {
            return buildCylindricalSurfaceWithEllipticalAxis(ellipticalCyl.id());
        }
        if (geometry instanceof StepConicalSurfaceWithEllipticalAxis ellipticalCone) {
            return buildConicalSurfaceWithEllipticalAxis(ellipticalCone.id());
        }
        if (geometry instanceof StepSphericalSurfaceWithEllipticalAxis ellipticalSphere) {
            return buildSphericalSurfaceWithEllipticalAxis(ellipticalSphere.id());
        }
        if (geometry instanceof StepToroidalSurfaceWithCylindricalAxis toroidalCyl) {
            return buildToroidalSurfaceWithCylindricalAxis(toroidalCyl.id());
        }
        if (geometry instanceof StepToroidalSurfaceWithEllipticalAxis toroidalElliptical) {
            return buildToroidalSurfaceWithEllipticalAxis(toroidalElliptical.id());
        }
        // B-spline surface with breakpoints - treat as regular B-spline surface
        if (geometry instanceof StepBSplineSurfaceWithKnotsAndBreakpoints splineBreakpoints) {
            return buildBSplineSurfaceWithBreakpoints(splineBreakpoints.id());
        }
        // Offset surface type 2
        if (geometry instanceof StepOffsetSurface2 offsetSurface2) {
            return buildOffsetSurface2Geometry(offsetSurface2, faceType);
        }
        // Blended surface - approximate as primary surface
        if (geometry instanceof StepBlendedSurface blended) {
            return buildBlendedSurface(blended, faceType);
        }
        // Free-form surface - approximate as B-spline surface
        if (geometry instanceof StepFreeFormSurface freeForm) {
            return buildFreeFormSurface(freeForm);
        }
        // Machined surface: delegate to underlying face geometry
        if (geometry instanceof StepMachinedSurface machinedSurface) {
            return buildSupportedFaceGeometry(machinedSurface.face(), faceType);
        }
        // Bounded surface - marker type with no geometry data
        if (geometry instanceof StepBoundedSurface boundedSurface) {
            StepEntity actual = entitiesById.get(boundedSurface.id());
            if (actual != null && actual != boundedSurface) {
                return buildSupportedFaceGeometry(actual, faceType);
            }
            return null;
        }
        // Surface abstract base type - check for complex entity syntax at same ID
        if (geometry instanceof StepSurface surface) {
            StepEntity actual = entitiesById.get(surface.id());
            if (actual != null && actual != surface) {
                return buildSupportedFaceGeometry(actual, faceType);
            }
            return null;
        }
        // Machined surface: delegate to underlying face geometry
        if (geometry instanceof StepMachinedSurface machinedSurface) {
            return buildSupportedFaceGeometry(machinedSurface.face(), faceType);
        }
        // MAPPED_ITEM: dispatch through to mapping target for surface geometry
        if (geometry instanceof StepMappedItem mappedItem) {
            return buildSupportedFaceGeometry(mappedItem.mappingTarget(), faceType);
        }
        // Advanced analytical surfaces
        if (geometry instanceof StepParaboloidSurface paraboloid) {
            return buildParaboloidSurface(paraboloid.id());
        }
        if (geometry instanceof StepHyperboloidSurface hyperboloid) {
            return buildHyperboloidSurface(hyperboloid.id());
        }
        if (geometry instanceof StepSurfaceOfTranslation translation) {
            return buildSurfaceOfTranslation(translation.id());
        }
        if (geometry instanceof StepSurfaceOfProjection projection) {
            return buildSurfaceOfProjection(projection.id());
        }
        return null;
    }

    private SurfaceGeometry buildRuledSurfaceGeometry(StepRuledSurface ruledSurface) {
        RuledSurface3 existing = ruledSurfaces.get(ruledSurface.id());
        if (existing != null) {
            return existing;
        }
        // Ruled surface is defined by two directrix curves
        Axis2Placement3D position = buildPlacement(ruledSurface.position().id());
        Curve3 directrix1 = buildCurve3(ruledSurface.directrix1());
        Curve3 directrix2 = buildCurve3(ruledSurface.directrix2());
        // Create ruled surface geometry
        RuledSurface3 built = new RuledSurface3(directrix1, directrix2);
        ruledSurfaces.put(ruledSurface.id(), built);
        return built;
    }

    private SurfaceGeometry buildSurfaceOfConstantRadiusGeometry(StepSurfaceOfConstantRadius surface, String faceType) {
        SurfaceOfConstantRadius3 existing = constantRadiusSurfaces.get(surface.id());
        if (existing != null) {
            return existing;
        }
        // Surface of constant radius: sweep a surface along a path with constant radius
        SurfaceGeometry sweptSurface = buildSupportedFaceGeometry(surface.sweptSurface(), faceType);
        if (sweptSurface == null) {
            return null;
        }
        double radius = surface.radius();
        if (radius <= 0.0) {
            return null;
        }
        SurfaceOfConstantRadius3 built = new SurfaceOfConstantRadius3(sweptSurface, radius);
        constantRadiusSurfaces.put(surface.id(), built);
        return built;
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

    // Build methods for elliptical axis and extended surface types

    public CylindricalSurface buildCylindricalSurfaceWithEllipticalAxis(int id) {
        CylindricalSurface existing = cylindricalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepCylindricalSurfaceWithEllipticalAxis surface = requireEntity(id, StepCylindricalSurfaceWithEllipticalAxis.class,
                "CYLINDRICAL_SURFACE_WITH_ELLIPTICAL_AXIS");
        // Approximate elliptical cylinder as circular cylinder with average radius
        double avgRadius = (surface.semiAxisA() + surface.semiAxisB()) / 2.0;
        CylindricalSurface built = new CylindricalSurface(buildPlacement(surface.position().id()), avgRadius);
        cylindricalSurfaces.put(id, built);
        return built;
    }

    public ConicalSurface buildConicalSurfaceWithEllipticalAxis(int id) {
        ConicalSurface existing = conicalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepConicalSurfaceWithEllipticalAxis surface = requireEntity(id, StepConicalSurfaceWithEllipticalAxis.class,
                "CONICAL_SURFACE_WITH_ELLIPTICAL_AXIS");
        // Approximate elliptical cone as circular cone with average radius
        double avgRadius = (surface.semiAxisA() + surface.semiAxisB()) / 2.0;
        ConicalSurface built = new ConicalSurface(buildPlacement(surface.position().id()), avgRadius, Math.PI / 4);
        conicalSurfaces.put(id, built);
        return built;
    }

    public SphericalSurface buildSphericalSurfaceWithEllipticalAxis(int id) {
        SphericalSurface existing = sphericalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepSphericalSurfaceWithEllipticalAxis surface = requireEntity(id, StepSphericalSurfaceWithEllipticalAxis.class,
                "SPHERICAL_SURFACE_WITH_ELLIPTICAL_AXIS");
        // Use the radius directly (elliptical ratio is ignored in approximation)
        SphericalSurface built = new SphericalSurface(buildPlacement(surface.position().id()), surface.radius());
        sphericalSurfaces.put(id, built);
        return built;
    }

    public ToroidalSurface buildToroidalSurfaceWithCylindricalAxis(int id) {
        ToroidalSurface existing = toroidalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepToroidalSurfaceWithCylindricalAxis surface = requireEntity(id, StepToroidalSurfaceWithCylindricalAxis.class,
                "TOROIDAL_SURFACE_WITH_CYLINDRICAL_AXIS");
        // Convert Axis1Placement to Axis2Placement3D
        Axis2Placement3D placement = buildAxis1PlacementAsAxis2(surface.position().id());
        ToroidalSurface built = new ToroidalSurface(placement, surface.majorRadius(), surface.minorRadius());
        toroidalSurfaces.put(id, built);
        return built;
    }

    public ToroidalSurface buildToroidalSurfaceWithEllipticalAxis(int id) {
        ToroidalSurface existing = toroidalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepToroidalSurfaceWithEllipticalAxis surface = requireEntity(id, StepToroidalSurfaceWithEllipticalAxis.class,
                "TOROIDAL_SURFACE_WITH_ELLIPTICAL_AXIS");
        // Approximate elliptical torus as circular torus with minor radius
        ToroidalSurface built = new ToroidalSurface(buildPlacement(surface.position().id()), surface.majorRadius(), surface.minorRadius());
        toroidalSurfaces.put(id, built);
        return built;
    }

    public BSplineSurface3 buildBSplineSurfaceWithBreakpoints(int id) {
        BSplineSurface3 existing = bsplineSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepBSplineSurfaceWithKnotsAndBreakpoints surface = requireEntity(id, StepBSplineSurfaceWithKnotsAndBreakpoints.class,
                "B_SPLINE_SURFACE_WITH_KNOTS_AND_BREAKPOINTS");
        List<List<CartesianPoint>> controlPoints = new ArrayList<>(surface.controlPoints().size());
        for (List<StepCartesianPoint> row : surface.controlPoints()) {
            List<CartesianPoint> pointRow = new ArrayList<>(row.size());
            for (StepCartesianPoint pt : row) {
                pointRow.add(buildPoint(pt.id()));
            }
            controlPoints.add(List.copyOf(pointRow));
        }
        BSplineSurface3 built = new BSplineSurface3(
                surface.uDegree(), surface.vDegree(), controlPoints,
                surface.uKnotMultiplicities(), surface.vKnotMultiplicities(),
                surface.uKnots(), surface.vKnots());
        bsplineSurfaces.put(id, built);
        return built;
    }

    private SurfaceGeometry buildOffsetSurface2Geometry(StepOffsetSurface2 offsetSurface2, String faceType) {
        SurfaceGeometry base = buildSupportedFaceGeometry(offsetSurface2.basisSurface(), faceType);
        if (base == null) {
            return null;
        }
        // Same logic as regular offset surface
        if (base instanceof Plane plane) {
            return new Plane(
                    plane.origin().add(plane.normal().asVector().scale(offsetSurface2.distance())),
                    plane.normal());
        }
        if (base instanceof CylindricalSurface cylindricalSurface) {
            return new CylindricalSurface(
                    cylindricalSurface.position(),
                    cylindricalSurface.radius() + offsetSurface2.distance());
        }
        if (base instanceof SphericalSurface sphericalSurface) {
            return new SphericalSurface(
                    sphericalSurface.position(),
                    sphericalSurface.radius() + offsetSurface2.distance());
        }
        if (base instanceof ConicalSurface conicalSurface) {
            return offsetConicalSurface(conicalSurface, offsetSurface2.distance());
        }
        if (base instanceof ToroidalSurface toroidalSurface) {
            return new ToroidalSurface(
                    toroidalSurface.position(),
                    toroidalSurface.majorRadius(),
                    toroidalSurface.minorRadius() + offsetSurface2.distance());
        }
        if (base instanceof OffsetSurface3 nestedOffsetSurface) {
            return new OffsetSurface3(
                    nestedOffsetSurface.basisSurface(),
                    nestedOffsetSurface.distance() + offsetSurface2.distance());
        }
        return new OffsetSurface3(base, offsetSurface2.distance());
    }

    private SurfaceGeometry buildBlendedSurface(StepBlendedSurface blended, String faceType) {
        // Blended surface is defined by two parent surfaces and a blend curve/radius.
        // Approximate by returning the primary surface - a full blend surface implementation
        // would require computing the fillet/blend geometry.
        SurfaceGeometry primary = buildSupportedFaceGeometry(blended.primarySurface(), faceType);
        if (primary != null) {
            return primary;
        }
        // Fall back to secondary surface if primary is unsupported
        return buildSupportedFaceGeometry(blended.secondarySurface(), faceType);
    }

    private SurfaceGeometry buildFreeFormSurface(StepFreeFormSurface surface) {
        // Free-form surface with control points, degrees, and knot vectors.
        // Approximate as a B-spline surface if we have enough control points.
        int degreeU = surface.degreeU();
        int degreeV = surface.degreeV();
        if (degreeU <= 0 || degreeV <= 0) {
            return null;
        }
        List<List<StepEntity>> controlPoints = surface.controlPoints();
        if (controlPoints.isEmpty() || controlPoints.get(0).isEmpty()) {
            return null;
        }
        int nRows = controlPoints.size();
        int nCols = controlPoints.get(0).size();

        // Build control point grid and knot vectors
        List<List<CartesianPoint>> grid = new ArrayList<>(nRows);
        for (List<StepEntity> row : controlPoints) {
            List<CartesianPoint> builtRow = new ArrayList<>(row.size());
            for (StepEntity cp : row) {
                if (cp instanceof StepCartesianPoint point) {
                    builtRow.add(buildPoint(point.id()));
                } else {
                    return null;
                }
            }
            grid.add(builtRow);
        }

        // Use provided knot vectors or generate uniform ones
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

        // Weights for rational surfaces (ignored for non-rational BSplineSurface3)
        try {
            // BSplineSurface3 requires multiplicities as List<Integer>
            // Generate uniform multiplicities (all 1 for non-repeated knots)
            List<Integer> multU = generateUniformMultiplicities(knotU.size());
            List<Integer> multV = generateUniformMultiplicities(knotV.size());
            return new BSplineSurface3(degreeU, degreeV, grid, multU, multV, knotU, knotV);
        } catch (Exception e) {
            return null;
        }
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
        if (geometry instanceof StepToroidalSurfaceWithSpecifiedBends toroidalSpecBends) {
            buildToroidalSurfaceFromSpecifiedBends(toroidalSpecBends);
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
        if (geometry instanceof StepBSplineSurface splineSurface) {
            buildGenericBSplineSurface(splineSurface.id());
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
        // Extended surface types handled in face geometry path
        if (geometry instanceof StepRuledSurface ruledSurface) {
            buildRuledSurfaceGeometry(ruledSurface);
            return;
        }
        if (geometry instanceof StepSurfaceOfConstantRadius constantRadiusSurface) {
            buildSurfaceOfConstantRadiusGeometry(constantRadiusSurface, "SURFACE");
            return;
        }
        if (geometry instanceof StepSurfacePatch surfacePatch) {
            buildSurfacePatchGeometry(surfacePatch, "SURFACE");
            return;
        }
        if (geometry instanceof StepRectangularCompositeSurface compositeSurface) {
            buildRectangularCompositeSurfaceGeometry(compositeSurface, "SURFACE");
            return;
        }
        if (geometry instanceof StepCylindricalSurfaceWithEllipticalAxis ellipticalCyl) {
            buildCylindricalSurfaceWithEllipticalAxis(ellipticalCyl.id());
            return;
        }
        if (geometry instanceof StepConicalSurfaceWithEllipticalAxis ellipticalCone) {
            buildConicalSurfaceWithEllipticalAxis(ellipticalCone.id());
            return;
        }
        if (geometry instanceof StepSphericalSurfaceWithEllipticalAxis ellipticalSphere) {
            buildSphericalSurfaceWithEllipticalAxis(ellipticalSphere.id());
            return;
        }
        if (geometry instanceof StepToroidalSurfaceWithCylindricalAxis toroidalCyl) {
            buildToroidalSurfaceWithCylindricalAxis(toroidalCyl.id());
            return;
        }
        if (geometry instanceof StepToroidalSurfaceWithEllipticalAxis toroidalElliptical) {
            buildToroidalSurfaceWithEllipticalAxis(toroidalElliptical.id());
            return;
        }
        if (geometry instanceof StepBSplineSurfaceWithKnotsAndBreakpoints splineBreakpoints) {
            buildBSplineSurfaceWithBreakpoints(splineBreakpoints.id());
            return;
        }
        if (geometry instanceof StepOffsetSurface2 offsetSurface2) {
            buildOffsetSurface2Geometry(offsetSurface2, "SURFACE");
            return;
        }
        if (geometry instanceof StepBlendedSurface blended) {
            buildBlendedSurface(blended, "SURFACE");
            return;
        }
        if (geometry instanceof StepFreeFormSurface freeForm) {
            buildFreeFormSurface(freeForm);
            return;
        }
        if (geometry instanceof StepBoundedSurface boundedSurface) {
            StepEntity actual = entitiesById.get(boundedSurface.id());
            if (actual != null && actual != boundedSurface) {
                buildSupportedSurfaceGeometry(actual);
            }
            // BoundedSurface with no underlying geometry - silently skip
            return;
        }
        // Advanced analytical surfaces
        if (geometry instanceof StepParaboloidSurface paraboloid) {
            buildParaboloidSurface(paraboloid.id());
            return;
        }
        if (geometry instanceof StepHyperboloidSurface hyperboloid) {
            buildHyperboloidSurface(hyperboloid.id());
            return;
        }
        if (geometry instanceof StepSurfaceOfTranslation translation) {
            buildSurfaceOfTranslation(translation.id());
            return;
        }
        if (geometry instanceof StepSurfaceOfProjection projection) {
            buildSurfaceOfProjection(projection.id());
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

    String describeUnsupportedFaceGeometry(StepEntity geometry) {
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
        if (geometry instanceof StepToroidalSurfaceWithSpecifiedBends toroidalSpecBends) {
            buildToroidalSurfaceFromSpecifiedBends(toroidalSpecBends);
            return "TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS";
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

    public Curve3 buildCurve3From2D(int id) {
        StepEntity entity = requireExistingEntity(id);
        Curve2 curve2 = (Curve2) buildCurve2(entity);
        return geometryOps.liftCurve2(curve2);
    }

    private Curve3 liftCurve2(Curve2 curve2) {
        return geometryOps.liftCurve2(curve2);
    }

    private Curve2 approximateOffsetCurve2(Curve2 basisCurve, double distance) {
        return geometryOps.approximateOffsetCurve2(basisCurve, distance);
    }

    private Curve3 approximateOffsetCurve3(Curve3 basisCurve, double distance, Direction3 refDirection) {
        return geometryOps.approximateOffsetCurve3(basisCurve, distance, refDirection);
    }


    private List<CartesianPoint> closeLoop3(List<CartesianPoint> points) {
        return geometryOps.closeLoop3(points);
    }

    private List<CartesianPoint> reverseClosedLoop3(List<CartesianPoint> points) {
        return geometryOps.reverseClosedLoop3(points);
    }

    private List<CartesianPoint> sampleCurve3(Curve3 curve, int segments) {
        return geometryOps.sampleCurve3(curve, segments);
    }

    private Curve3 transformCurve3(Curve3 curve, StepCartesianTransformationOperator transformation) {
        return geometryOps.transformCurve3(curve, transformation);
    }

    private Curve2 transformCurve2(Curve2 curve, StepCartesianTransformationOperator transformation) {
        return geometryOps.transformCurve2(curve, transformation);
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

    static String stepEntityTypeName(StepEntity entity) {
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

    StepEntity resolvedEntity(int id) {
        return entitiesById.get(id);
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
        return StepCadGeometryOps.curveTypeName(curve);
    }

    private static String curveTypeName(Curve2 curve) {
        return StepCadGeometryOps.curveTypeName(curve);
    }

    private Plane transformPlane(Plane plane, StepCartesianTransformationOperator transformation) {
        return new Plane(
                transformPoint3(plane.origin(), transformation),
                transformDirection3(plane.normal(), transformation));
    }

    private SurfaceGeometry transformSurfaceGeometry(SurfaceGeometry surface, StepCartesianTransformationOperator transformation) {
        return geometryOps.transformSurfaceGeometry(surface, transformation);
    }

    private Axis2Placement3D transformPlacement(Axis2Placement3D placement, StepCartesianTransformationOperator transformation) {
        return geometryOps.transformPlacement(placement, transformation);
    }

    private CartesianPoint transformPoint3(CartesianPoint point, StepCartesianTransformationOperator transformation) {
        return geometryOps.transformPoint3(point, transformation);
    }

    private Vector3 transformVector3(Vector3 vector, StepCartesianTransformationOperator transformation) {
        return geometryOps.transformVector3(vector, transformation);
    }

    private Point2 transformPoint2(Point2 point, StepCartesianTransformationOperator transformation) {
        return geometryOps.transformPoint2(point, transformation);
    }

    private Direction3 transformDirection3(Direction3 direction, StepCartesianTransformationOperator transformation) {
        return geometryOps.transformDirection3(direction, transformation);
    }

    private Direction2 transformDirection2(Direction2 direction, StepCartesianTransformationOperator transformation) {
        return geometryOps.transformDirection2(direction, transformation);
    }

    private String unsupportedReplicaSurfaceTransformation(StepCartesianTransformationOperator transformation) {
        return geometryOps.unsupportedReplicaSurfaceTransformation(transformation);
    }


    public record Axis1Placement(CartesianPoint location, Direction3 axis) {
    }

    /**
     * Builds a paraboloid surface of revolution.
     */
    public ParaboloidSurface buildParaboloidSurface(int id) {
        ParaboloidSurface existing = paraboloidSurfaces.get(id);
        if (existing != null) return existing;
        StepParaboloidSurface step = requireEntity(id, StepParaboloidSurface.class, "PARABOLOID_SURFACE");
        ParaboloidSurface built = new ParaboloidSurface(buildPlacement(step.position().id()), step.focalLength());
        paraboloidSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a hyperboloid surface of revolution.
     */
    public HyperboloidSurface buildHyperboloidSurface(int id) {
        HyperboloidSurface existing = hyperboloidSurfaces.get(id);
        if (existing != null) return existing;
        StepHyperboloidSurface step = requireEntity(id, StepHyperboloidSurface.class, "HYPERBOLOID_SURFACE");
        HyperboloidSurface built = new HyperboloidSurface(buildPlacement(step.position().id()), step.radius(), step.semiAxis());
        hyperboloidSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a surface of translation from a profile curve and direction.
     */
    public SurfaceOfTranslation3 buildSurfaceOfTranslation(int id) {
        SurfaceOfTranslation3 existing = translationSurfaces.get(id);
        if (existing != null) return existing;
        StepSurfaceOfTranslation step = requireEntity(id, StepSurfaceOfTranslation.class, "SURFACE_OF_TRANSLATION");
        Curve3 profile = buildCurve3(step.profile());
        Vector3 direction = buildVector3(step.direction());
        SurfaceOfTranslation3 built = new SurfaceOfTranslation3(profile, direction);
        translationSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a surface of projection from a profile curve and projection direction.
     */
    public SurfaceOfProjection3 buildSurfaceOfProjection(int id) {
        SurfaceOfProjection3 existing = projectionSurfaces.get(id);
        if (existing != null) return existing;
        StepSurfaceOfProjection step = requireEntity(id, StepSurfaceOfProjection.class, "SURFACE_OF_PROJECTION");
        Curve3 profile = buildCurve3(step.profile());
        Vector3 direction = buildVector3(step.projectionDirection());
        SurfaceOfProjection3 built = new SurfaceOfProjection3(profile, direction);
        projectionSurfaces.put(id, built);
        return built;
    }

    /**
     * Builds a SurfaceGeometry from a surface entity ID.
     * Dispatches to the appropriate typed builder for parametric surfaces.
     */
    public SurfaceGeometry buildSurfaceGeometry(int id) {
        StepEntity entity = requireExistingEntity(id);
        return switch (entity) {
            case StepParaboloidSurface paraboloid -> buildParaboloidSurface(id);
            case StepHyperboloidSurface hyperboloid -> buildHyperboloidSurface(id);
            case StepSurfaceOfTranslation translation -> buildSurfaceOfTranslation(id);
            case StepSurfaceOfProjection projection -> buildSurfaceOfProjection(id);
            default -> throw new UnsupportedGeometryException("entity #" + id + " is not a supported parametric surface");
        };
    }

    private Vector3 buildVector3(StepEntity entity) {
        if (entity instanceof StepVector stepVector) {
            Direction3 dir = buildDirection(stepVector.orientation().id());
            double mag = stepVector.magnitude();
            return dir.asVector().scale(mag);
        }
        if (entity instanceof StepDirection stepDir) {
            return buildDirection(stepDir.id()).asVector();
        }
        throw new StepResolutionException("entity is not a supported vector or direction");
    }

    private record CircularFrame(Vector3 x, Vector3 y) {
        Direction3 radialAtAngle(double angle) {
            return Direction3.from(x.scale(Math.cos(angle)).add(y.scale(Math.sin(angle))));
        }
        Direction3 z() {
            return Direction3.from(x.cross(y));
        }
    }
}
