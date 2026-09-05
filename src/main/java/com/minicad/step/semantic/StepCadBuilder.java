package com.minicad.step.semantic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minicad.common.Epsilon;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.topology.TopologyValidator;
import com.minicad.geometry.Axis1Placement;
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
import com.minicad.step.model.StepBlendedSurface;
import com.minicad.step.model.StepBlockVolume;
import com.minicad.step.model.StepBoundedCurve;
import com.minicad.step.model.StepBoundedSurface;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepCartesianTransformationOperator;
import com.minicad.step.model.StepBSplineCurveWithKnotsAndBreakpoints;
import com.minicad.step.model.StepBSplineCurveWithKnots;
import com.minicad.step.model.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.StepCircle;
import com.minicad.step.model.StepCompositeCurve;
import com.minicad.step.model.StepCompositeCurveOnSurface3D;
import com.minicad.step.model.StepCompositeCurveOnSurface;
import com.minicad.step.model.StepCompositeCurveSegment;
import com.minicad.step.model.StepConicalSurface;
import com.minicad.step.model.StepConicCurve;
import com.minicad.step.model.StepDegeneratePcurve;
import com.minicad.step.model.StepCylindricalSurface;
import com.minicad.step.model.StepDegenerateToroidalSurface;
import com.minicad.step.model.StepDimensionCurve;
import com.minicad.step.model.StepDirection;
import com.minicad.step.model.StepVector;
import com.minicad.step.model.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEdge;
import com.minicad.step.model.StepEdgeLoop;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepEllipse;
import com.minicad.step.model.StepFacettedBrep;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepFaceBound;
import com.minicad.step.model.StepFaceSurface;
import com.minicad.step.model.StepFilletEdge;
import com.minicad.step.model.StepChamferEdge;
import com.minicad.step.model.StepFace;
import com.minicad.step.model.StepFiniteElementMesh;
import com.minicad.step.model.StepFlatPattern;
import com.minicad.step.model.StepCurve;
import com.minicad.step.model.StepCurveBoundedSurface;
import com.minicad.step.model.StepItemDefinedTransformation;
import com.minicad.step.model.StepCsgPrimitive;
import com.minicad.step.model.StepCsgPrimitive3D;
import com.minicad.step.model.StepCsgSolid;
import com.minicad.step.model.StepCsgVolume;
import com.minicad.step.model.StepGeometricReplica;
import com.minicad.step.model.StepHalfSpaceSolid;
import com.minicad.step.model.StepLeaderCurve;
import com.minicad.step.model.StepLine;
import com.minicad.step.model.StepLineSegment;
import com.minicad.step.model.StepMachinedSurface;
import com.minicad.step.model.StepMappedItem;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepOffsetCurve2D;
import com.minicad.step.model.StepOffsetCurve3D;
import com.minicad.step.model.StepOffsetSurface;
import com.minicad.step.model.StepTessellatedFace;
import com.minicad.step.model.StepTessellatedFaceSet;
import com.minicad.step.model.StepTessellatedTriangle;
import com.minicad.step.model.StepSeamEdge;
import com.minicad.step.model.StepAreaProfile;
import com.minicad.step.model.StepCenteredCircleProfileDef;
import com.minicad.step.model.StepCentreLineArcProfileDef;
import com.minicad.step.model.StepGeneralizedAreaProfile;
import com.minicad.step.model.StepRectangleHollowProfileDef;
import com.minicad.step.model.StepSweptProfileAreaOutline;
import com.minicad.step.model.StepTriangulatedFace;
import com.minicad.step.model.StepComplexTriangulatedFace;
import com.minicad.step.model.StepCubicBezierTriangulatedFace;
import com.minicad.step.model.StepOrientedEdge;
import com.minicad.step.model.StepOrientedFace;
import com.minicad.step.model.StepOrientedSubface;
import com.minicad.step.model.StepOrientedCurve;
import com.minicad.step.model.StepOrientedSurface;
import com.minicad.step.model.StepPiecewiseBezierCurve;
import com.minicad.step.model.StepPiecewiseBezierSurface;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepPoint;
import com.minicad.step.model.StepFeaAxis2Placement3d;
import com.minicad.step.model.StepPcurve;
import com.minicad.step.model.StepProfileDef;
import com.minicad.step.model.StepProjectionCurve;
import com.minicad.step.model.StepPolygonalBoundedHalfSpace;
import com.minicad.step.model.StepPolyline;
import com.minicad.step.model.StepQuasiUniformCurve;
import com.minicad.step.model.StepQuasiUniformSurface;
import com.minicad.step.model.StepRationalBSplineCurve;
import com.minicad.step.model.StepRationalBSplineSurface;
import com.minicad.step.model.StepRectangularTrimmedSurface;
import com.minicad.step.model.StepSeamCurve;
import com.minicad.step.model.StepSolidModel;
import com.minicad.step.model.StepSolidReplica;
import com.minicad.step.model.StepSurface;
import com.minicad.step.model.StepSurfaceCurve;
import com.minicad.step.model.StepSurfaceCurveSweptAreaSolid;
import com.minicad.step.model.StepSweptDiskSolid;
import com.minicad.step.model.StepExtrudedAreaSolidTapered;
import com.minicad.step.model.StepRevolvedAreaSolidTapered;
import com.minicad.step.model.StepExtrudedFaceSolid;
import com.minicad.step.model.StepRevolvedFaceSolid;
import com.minicad.step.model.StepSweptFaceSolid;
import com.minicad.step.model.StepCylinderVolume;
import com.minicad.step.model.StepSphereVolume;
import com.minicad.step.model.StepTorusVolume;
import com.minicad.step.model.StepPrismVolume;
import com.minicad.step.model.StepRightCircularConeVolume;
import com.minicad.step.model.StepRuledSurface;
import com.minicad.step.model.StepSurfaceModel;
import com.minicad.step.model.StepSurfaceOfConstantRadius;
import com.minicad.step.model.StepSurfacePatch;
import com.minicad.step.model.StepRectangularCompositeSurface;
import com.minicad.step.model.StepClothoid;
import com.minicad.step.model.StepIndexedPolyCurve;
import com.minicad.step.model.StepPolyline3D;
import com.minicad.step.model.StepDegenerateCurve;
import com.minicad.step.model.StepNonManifoldSolidBrep;
import com.minicad.step.model.StepSurfaceOfLinearExtrusion;
import com.minicad.step.model.StepSurfaceOfRevolution;
import com.minicad.step.model.StepSurfaceOfTranslation;
import com.minicad.step.model.StepSurfaceOfProjection;
import com.minicad.step.model.StepParaboloidSurface;
import com.minicad.step.model.StepHyperboloidSurface;
import com.minicad.step.model.StepSweptAreaSolid;
import com.minicad.step.model.StepSurfacedEdgeCurve;
import com.minicad.step.model.StepSphericalSurface;
import com.minicad.step.model.StepTerminatorSymbol;
import com.minicad.step.model.StepTrimmedCurve;
import com.minicad.step.model.StepToroidalSurface;
import com.minicad.step.model.StepToroidalSurfaceWithSpecifiedBends;
import com.minicad.step.model.StepSubedge;
import com.minicad.step.model.StepSubface;
import com.minicad.step.model.StepUniformCurve;
import com.minicad.step.model.StepUniformSurface;
import com.minicad.step.model.StepVertexLoop;
import com.minicad.step.model.StepVertexPoint;
import com.minicad.step.model.StepVertex;
import com.minicad.step.model.StepOrientedPath;
import com.minicad.step.model.StepOpenPath;
import com.minicad.step.model.StepPath;
import com.minicad.step.model.StepSubpath;
import com.minicad.step.model.StepHyperbola2D;
import com.minicad.step.model.StepParabola2D;
import com.minicad.step.model.StepLine2D;
import com.minicad.step.model.StepPolyline2D;
import com.minicad.step.model.StepTrimmedCurve2D;
import com.minicad.step.model.StepBSplineCurve2D;
import com.minicad.step.model.StepRationalBSplineCurve2D;
import com.minicad.step.model.StepBezierCurve2D;
import com.minicad.step.model.StepQuasiUniformCurve2D;
import com.minicad.step.model.StepUniformCurve2D;
import com.minicad.step.model.StepPiecewiseBezierCurve2D;
import com.minicad.step.model.StepIndexedPolyCurve2D;
import com.minicad.step.model.StepDegenerateCurve2D;
import com.minicad.step.model.StepCircle2D;
import com.minicad.step.model.StepBoundedCurve2D;
import com.minicad.step.model.StepCompositeCurve2D;
import com.minicad.step.model.StepCurve2D;
import com.minicad.step.model.StepEllipse2D;
import com.minicad.step.model.StepBSplineSurfaceWithKnotsAndBreakpoints;
import com.minicad.step.model.StepOffsetSurface2;
import com.minicad.step.model.StepFreeFormSurface;
import com.minicad.step.model.StepCylindricalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepConicalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepSphericalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepToroidalSurfaceWithCylindricalAxis;
import com.minicad.step.model.StepToroidalSurfaceWithEllipticalAxis;
import com.minicad.step.model.StepAdvancedBrep;
import com.minicad.step.model.StepComplexClippingResult;
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
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Objects;

/**
 * Builds internal geometry and topology objects from resolved STEP semantic entities.
 */
public final class StepCadBuilder {

    private static final Logger log = LoggerFactory.getLogger(StepCadBuilder.class);

    private final Map<Integer, StepEntity> entitiesById;
    private final StepCadGeometryBuilder geometryBuilder;
    private final StepCadGeometryOps geometryOps;
    private final StepTrimResolver trimResolver;
    private final StepProfileBuilder profileBuilder;
    private final StepCadTopologyBuilder topologyBuilder;
    private final StepShellBuilder shellBuilder;
    private final StepCadSolidBuilder solidBuilder;
    private final StepCadBooleanBuilder booleanBuilder;
    private final StepCadCurveBuilder curveBuilder;
    private final StepCadSurfaceBuilder surfaceBuilder;
    private final StepCadSweptBuilder sweptBuilder;
    private final Map<Integer, CartesianPoint> points = new LinkedHashMap<>();
    private final Map<Integer, Direction3> directions = new LinkedHashMap<>();
    private final Map<Integer, Vector3> vectors = new LinkedHashMap<>();
    private final Map<Integer, Axis2Placement3D> placements = new LinkedHashMap<>();
    private final Map<Integer, Axis1Placement> axis1Placements = new LinkedHashMap<>();
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
    // Topology caches are now managed by StepCadTopologyBuilder
    private final Map<Integer, Shell> shells = new LinkedHashMap<>();
    private final Map<Integer, Solid> solids = new LinkedHashMap<>();

    private StepCadBuilder(Map<Integer, StepEntity> entitiesById) {
        // Holds the caller's map directly: it is an unmodifiable view from
        // StepEntityResolver.resolveAll and the builder only reads it.
        this.entitiesById = entitiesById;
        this.geometryBuilder = new StepCadGeometryBuilder(
            this.entitiesById,
            points,
            directions,
            vectors,
            placements,
            axis1Placements,
            this::buildVertex
        );
        this.geometryOps = new StepCadGeometryOps(this);
        this.trimResolver = new StepTrimResolver(entitiesById, this::buildPoint, this::buildPoint2);
        this.curveBuilder = new StepCadCurveBuilder(
            this.entitiesById,
            geometryBuilder,
            geometryOps,
            trimResolver,
            points2d,
            directions2d,
            lines2d,
            circles2d,
            ellipses2d,
            polylines2d,
            compositeCurves2d,
            splineCurves2d,
            rationalSplineCurves2d,
            trimmedCurves2d,
            hyperbolas2d,
            parabolas2d,
            lines,
            circles,
            ellipses,
            polylines,
            compositeCurves,
            bsplineCurves,
            rationalBsplineCurves,
            trimmedCurves,
            surfaceCurves,
            parabolas,
            hyperbolas,
            clothoids,
            this::buildPlacement,
            this::buildSupportedFaceGeometryById,
            this::buildCurve3ById
        );
        this.profileBuilder = new StepProfileBuilder(geometryOps, e -> (Curve2) buildCurve2(e));
        this.sweptBuilder = new StepCadSweptBuilder(this, profileBuilder);
        this.topologyBuilder = new StepCadTopologyBuilder(
            this,
            this.entitiesById,
            geometryBuilder::buildPoint,
            this::buildCurve3ById,
            this::buildSupportedFaceGeometryById
        );
        this.shellBuilder = new StepShellBuilder(this);
        this.solidBuilder = new StepCadSolidBuilder(this);
        this.booleanBuilder = new StepCadBooleanBuilder(this, this.entitiesById);
        this.surfaceBuilder = new StepCadSurfaceBuilder(
            this.entitiesById,
            planes,
            cylindricalSurfaces,
            conicalSurfaces,
            toroidalSurfaces,
            this::buildPlacement
        );
        CURVE3_RULES = List.of(
            curve3Rule(StepPath.class, (curve) -> {
                StepPath path = (StepPath) curve;
                return buildPath(path.id());
            }),
            curve3Rule(StepOpenPath.class, (curve) -> {
                StepOpenPath openPath = (StepOpenPath) curve;
                return buildPath(openPath.id());
            }),
            curve3Rule(StepSubpath.class, (curve) -> {
                StepSubpath subpath = (StepSubpath) curve;
                return buildPath(subpath.id());
            }),
            curve3Rule(StepOrientedPath.class, (curve) -> {
                StepOrientedPath orientedPath = (StepOrientedPath) curve;
                return buildPath(orientedPath.id());
            }),
            curve3Rule(StepOffsetCurve2D.class, (curve) -> {
                StepOffsetCurve2D offsetCurve2D = (StepOffsetCurve2D) curve;
                return liftCurve2(buildOffsetCurve2(offsetCurve2D.id()));
            }),
            curve3Rule(StepAnnotationCurveOccurrence.class, (curve) -> {
                StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) curve;
                return buildCurve3(occurrence.item());
            }),
            curve3Rule(StepDimensionCurve.class, (curve) -> {
                StepDimensionCurve dimensionCurve = (StepDimensionCurve) curve;
                return buildCurve3(dimensionCurve.item());
            }),
            curve3Rule(StepLeaderCurve.class, (curve) -> {
                StepLeaderCurve leaderCurve = (StepLeaderCurve) curve;
                return buildCurve3(leaderCurve.item());
            }),
            curve3Rule(StepProjectionCurve.class, (curve) -> {
                StepProjectionCurve projectionCurve = (StepProjectionCurve) curve;
                return buildCurve3(projectionCurve.item());
            }),
            curve3Rule(StepDraughtingAnnotationOccurrence.class, (curve) -> {
                StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) curve;
                return buildCurve3(annotationOccurrence.item());
            }),
            curve3Rule(StepTerminatorSymbol.class, (curve) -> {
                StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) curve;
                return buildCurve3(terminatorSymbol.annotatedCurve());
            }),
            curve3Rule(StepPolyline3D.class, (curve) -> {
                StepPolyline3D polyline3D = (StepPolyline3D) curve;
                return buildPolyline3D(polyline3D);
            }),
            curve3Rule(StepLineSegment.class, (curve) -> {
                StepLineSegment lineSegment = (StepLineSegment) curve;
                CartesianPoint startPoint = buildPoint(lineSegment.startPoint().id());
                CartesianPoint endPoint = buildPoint(lineSegment.endPoint().id());
                Vector3 dir = endPoint.subtract(startPoint);
                return new Line3(startPoint, Direction3.from(dir), dir.norm());
            }),
            curve3Rule(StepSurfacedEdgeCurve.class, (curve) -> {
                StepSurfacedEdgeCurve surfacedEdgeCurve = (StepSurfacedEdgeCurve) curve;
                return buildCurve3(surfacedEdgeCurve.edgeGeometry());
            }),
            curve3Rule(StepEdgeCurve.class, (curve) -> {
                StepEdgeCurve edgeCurve = (StepEdgeCurve) curve;
                return buildCurve3(edgeCurve.edgeGeometry());
            }),
            curve3Rule(StepCompositeCurveOnSurface3D.class, (curve) -> {
                StepCompositeCurveOnSurface3D compositeCurve3D = (StepCompositeCurveOnSurface3D) curve;
                return buildCompositeCurve(compositeCurve3D.id());
            }),
            curve3Rule(StepCurve.class, (curve) -> {
                StepCurve abstractCurve = (StepCurve) curve;
                StepEntity actual = entitiesById.get(abstractCurve.id());
                if (actual != null && actual != abstractCurve) {
                    return buildCurve3(actual);
                }
                throw new UnsupportedGeometryException("CURVE is an abstract base type with no concrete geometry");
            }),
            curve3Rule(StepMappedItem.class, (curve) -> {
                StepMappedItem mappedItem = (StepMappedItem) curve;
                return buildCurve3(mappedItem.mappingTarget());
            }),
            curve3Rule(StepCompositeCurve2D.class, (curve) -> {
                StepCompositeCurve2D composite2D = (StepCompositeCurve2D) curve;
                return liftCurve2(buildCompositeCurve2D(composite2D));
            }),
            curve3Rule(StepTrimmedCurve2D.class, (curve) -> {
                StepTrimmedCurve2D trimmed2D = (StepTrimmedCurve2D) curve;
                return liftCurve2(buildTrimmedCurve2D(trimmed2D));
            }),
            curve3Rule(StepBSplineCurve2D.class, (curve) -> {
                StepBSplineCurve2D spline2D = (StepBSplineCurve2D) curve;
                return liftCurve2(curveBuilder.buildBSplineCurve2D(spline2D));
            }),
            curve3Rule(StepRationalBSplineCurve2D.class, (curve) -> {
                StepRationalBSplineCurve2D rational2D = (StepRationalBSplineCurve2D) curve;
                return liftCurve2(curveBuilder.buildRationalBSplineCurve2D(rational2D));
            }),
            curve3Rule(StepBezierCurve2D.class, (curve) -> {
                StepBezierCurve2D bezier2D = (StepBezierCurve2D) curve;
                return liftCurve2(curveBuilder.buildBezierCurve2D(bezier2D));
            }),
            curve3Rule(StepQuasiUniformCurve2D.class, (curve) -> {
                StepQuasiUniformCurve2D quasiUniform2D = (StepQuasiUniformCurve2D) curve;
                return liftCurve2(curveBuilder.buildQuasiUniformCurve2D(quasiUniform2D));
            }),
            curve3Rule(StepUniformCurve2D.class, (curve) -> {
                StepUniformCurve2D uniform2D = (StepUniformCurve2D) curve;
                return liftCurve2(curveBuilder.buildUniformCurve2D(uniform2D));
            }),
            curve3Rule(StepPiecewiseBezierCurve2D.class, (curve) -> {
                StepPiecewiseBezierCurve2D piecewiseBezier2D = (StepPiecewiseBezierCurve2D) curve;
                return liftCurve2(curveBuilder.buildPiecewiseBezierCurve2D(piecewiseBezier2D));
            }),
            curve3Rule(StepIndexedPolyCurve2D.class, (curve) -> {
                StepIndexedPolyCurve2D polyCurve2D = (StepIndexedPolyCurve2D) curve;
                return liftCurve2(buildIndexedPolyCurve2D(polyCurve2D));
            }),
            curve3Rule(StepDegenerateCurve2D.class, (curve) -> {
                StepDegenerateCurve2D degenerate2D = (StepDegenerateCurve2D) curve;
                return liftCurve2(buildDegenerateCurve2D(degenerate2D));
            }),
            curve3Rule(StepCircle2D.class, (curve) -> {
                StepCircle2D circle2D = (StepCircle2D) curve;
                return liftCurve2(buildCircle2D(circle2D));
            }),
            curve3Rule(StepEllipse2D.class, (curve) -> {
                StepEllipse2D ellipse2D = (StepEllipse2D) curve;
                return liftCurve2(buildEllipse2D(ellipse2D));
            }),
            curve3Rule(StepLine2D.class, (curve) -> {
                StepLine2D line2D = (StepLine2D) curve;
                return liftCurve2(buildLine2D(line2D));
            }),
            curve3Rule(StepPolyline2D.class, (curve) -> {
                StepPolyline2D polyline2D = (StepPolyline2D) curve;
                return liftCurve2(buildPolyline2D(polyline2D));
            }),
            curve3Rule(StepHyperbola2D.class, (curve) -> {
                StepHyperbola2D hyperbola2D = (StepHyperbola2D) curve;
                return liftCurve2(buildHyperbola2D(hyperbola2D));
            }),
            curve3Rule(StepParabola2D.class, (curve) -> {
                StepParabola2D parabola2D = (StepParabola2D) curve;
                return liftCurve2(buildParabola2D(parabola2D));
            })
        );

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
        return geometryBuilder.buildPoint(id);
    }

    public Point2 buildPoint2(int id) {
        return curveBuilder.buildPoint2(id);
    }

    /**
     * Builds a supported 3D point reference.
     *
     * @param id STEP entity id
     * @return built point
     */
    public CartesianPoint buildPointReference(int id) {
        StepEntity entity = requireEntity(id, StepEntity.class, "3D point reference");
        if (entity instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) entity;
            return buildPoint(point.id());
        }
        if (entity instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) entity;
            return buildVertex(vertexPoint.id()).point();
        }
        if (entity instanceof StepGeometricReplica) {
            StepGeometricReplica replica = (StepGeometricReplica) entity;
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
        return geometryBuilder.buildDirection(id);
    }

    public Direction2 buildDirection2(int id) {
        return curveBuilder.buildDirection2(id);
    }

    /**
     * Builds a vector.
     *
     * @param id STEP entity id
     * @return built vector
     */
    public Vector3 buildVector(int id) {
        return geometryBuilder.buildVector(id);
    }

    /**
     * Builds a placement.
     *
     * @param id STEP entity id
     * @return built placement
     */
    public Axis2Placement3D buildPlacement(int id) {
        return geometryBuilder.buildPlacement(id);
    }

    public Axis1Placement buildAxis1Placement(int id) {
        return geometryBuilder.buildAxis1Placement(id);
    }

    /**
     * Builds an Axis2Placement3D from an Axis1Placement by deriving a reference direction.
     * Used for surfaces that need full 3D placement but only have axis defined.
     */
    Axis2Placement3D buildAxis1PlacementAsAxis2(int id) {
        return geometryBuilder.buildAxis1PlacementAsAxis2(id);
    }

    /**
     * Returns a unit direction perpendicular to the given direction.
     */
    private Direction3 perpendicularDirection(Direction3 dir) {
        Vector3 v = dir.asVector();
        // Find the smallest component and cross with that axis
        if (Math.abs(v.getX()) <= Math.abs(v.getY()) && Math.abs(v.getX()) <= Math.abs(v.getZ())) {
            // Cross with X axis
            Vector3 perp = new Vector3(1, 0, 0).cross(v);
            if (perp.isZero()) {
                return new Direction3(0, 1, 0);
            }
            return Direction3.from(perp);
        } else if (Math.abs(v.getY()) <= Math.abs(v.getZ())) {
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
        return curveBuilder.buildLine3(id);
    }

    public Line2 buildLine2(int id) {
        return curveBuilder.buildLine2(id);
    }

    public Object buildPcurve2(int id) {
        return curveBuilder.buildPcurve2(id);
    }

    public BSplineCurve2 buildBSplineCurve2(int id) {
        return curveBuilder.buildBSplineCurve2(id);
    }

    public BSplineCurve2 buildBezierCurve2(int id) {
        return curveBuilder.buildBezierCurve2(id);
    }

    public BSplineCurve2 buildUniformCurve2(int id) {
        return curveBuilder.buildUniformCurve2(id);
    }

    public BSplineCurve2 buildQuasiUniformCurve2(int id) {
        return curveBuilder.buildQuasiUniformCurve2(id);
    }

    public BSplineCurve2 buildPiecewiseBezierCurve2(int id) {
        return curveBuilder.buildPiecewiseBezierCurve2(id);
    }

    public Circle2 buildCircle2(int id) {
        return curveBuilder.buildCircle2(id);
    }

    public Ellipse2 buildEllipse2(int id) {
        return curveBuilder.buildEllipse2(id);
    }

    public Polyline2 buildPolyline2(int id) {
        return curveBuilder.buildPolyline2(id);
    }

    public CompositeCurve2 buildCompositeCurve2(int id) {
        return curveBuilder.buildCompositeCurve2(id);
    }

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

    Object buildCurve2(StepEntity item) {
        return curveBuilder.buildCurve2(item);
    }

    /**
     * Builds a PCURVE or DEGENERATE_PCURVE as a 2D curve.
     * PCURVE wraps a representation item containing the actual 2D curve.
     */
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
        if (curve2D.getPosition() instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D pos2D = (StepAxis2Placement2D) curve2D.getPosition();
            CartesianPoint origin = buildPoint(pos2D.getLocation().id());
            Direction3 xDir = new Direction3(1, 0, 0);
            if (pos2D.getRefDirection() != null) {
                StepDirection dir = pos2D.getRefDirection();
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
                double gx = p.getLocation().getX() + x * p.xDirection().getX() + y * p.yDirection().getX();
                double gy = p.getLocation().getY() + x * p.xDirection().getY() + y * p.yDirection().getY();
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
        List<StepCartesianPoint> stepPoints = polyCurve.getPoints();
        List<Integer> indices = polyCurve.indices();
        List<Point2> points = indices.stream()
                .map(index -> {
                    StepCartesianPoint stepPoint = stepPoints.get(index);
                    CartesianPoint point3D = buildPoint(stepPoint.id());
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
        // Degenerate curve in 2D is a single point or empty curve
        // Return a minimal polyline (single point repeated)
        StepEntity basisEntity = degenerateCurve.getBasisCurve();
        if (basisEntity instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) basisEntity;
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
        // Clothoid (Euler spiral) in 2D - approximate with polyline sampling
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

        // Sample clothoid curve
        int segments = 64;
        List<Point2> points = new ArrayList<>(segments + 1);
        Direction2 yDir = new Direction2(-xDir.getY(), xDir.getX());

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
        List<Point2> points = polyline2D.getPoints().stream()
                .map(p -> buildPoint2(p.id()))
                .collect(Collectors.toList());
        return new Polyline2(points);
    }

    private TrimmedCurve2 buildTrimmedCurve2D(StepTrimmedCurve2D trimmedCurve2D) {
        Curve2 basisCurve = (Curve2) buildCurve2(trimmedCurve2D.getBasisCurve());
        // Use trim parameters directly on the basis curve
        double trim1 = trimmedCurve2D.trim1();
        double trim2 = trimmedCurve2D.trim2();
        return new TrimmedCurve2(basisCurve, trim1, trim2, trimmedCurve2D.isSenseAgreement());
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
        com.minicad.geometry2d.Direction2 dir = buildDirection2(line2D.direction2d().id());
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

    public TrimmedCurve2 buildTrimmedCurve2(int id) {
        return curveBuilder.buildTrimmedCurve2(id);
    }

    public Curve2 buildOffsetCurve2(int id) {
        return curveBuilder.buildOffsetCurve2(id);
    }

    /**
     * Builds a plane.
     *
     * @param id STEP entity id
     * @return built plane
     */
    public Plane buildPlane(int id) {
        return surfaceBuilder.buildPlane(id);
    }

    /**
     * Builds a circle geometry object.
     *
     * @param id STEP entity id
     * @return built circle
     */
    public Circle buildCircle(int id) {
        return curveBuilder.buildCircle3(id);
    }

    /**
     * Builds an ellipse geometry object.
     *
     * @param id STEP entity id
     * @return built ellipse
     */
    public Ellipse3 buildEllipse(int id) {
        return curveBuilder.buildEllipse3(id);
    }

    public Polyline3 buildPolyline(int id) {
        return curveBuilder.buildPolyline3(id);
    }

    public CompositeCurve3 buildCompositeCurve(int id) {
        return curveBuilder.buildCompositeCurve3(id);
    }

    /**
     * Builds a cylindrical surface geometry object.
     *
     * @param id STEP entity id
     * @return built cylindrical surface
     */
    public CylindricalSurface buildCylindricalSurface(int id) {
        return surfaceBuilder.buildCylindricalSurface(id);
    }

    /**
     * Builds a conical surface geometry object.
     *
     * @param id STEP entity id
     * @return built conical surface
     */
    public ConicalSurface buildConicalSurface(int id) {
        return surfaceBuilder.buildConicalSurface(id);
    }

    /**
     * Builds a toroidal surface geometry object.
     *
     * @param id STEP entity id
     * @return built toroidal surface
     */
    public ToroidalSurface buildToroidalSurface(int id) {
        return surfaceBuilder.buildToroidalSurface(id);
    }

    /**
     * Builds a degenerate toroidal surface geometry object for validation/preview support paths.
     *
     * @param id STEP entity id
     * @return built toroidal surface
     */
    public ToroidalSurface buildDegenerateToroidalSurface(int id) {
        return surfaceBuilder.buildDegenerateToroidalSurface(id);
    }

    /**
     * Builds a toroidal surface from TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS.
     */
    private ToroidalSurface buildToroidalSurfaceFromSpecifiedBends(StepToroidalSurfaceWithSpecifiedBends surface) {
        return surfaceBuilder.buildToroidalSurfaceFromSpecifiedBends(surface);
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
        SphericalSurface built = new SphericalSurface(buildPlacement(surface.getPosition().id()), surface.getRadius());
        sphericalSurfaces.put(id, built);
        return built;
    }

    public RuledSurface3 buildRuledSurface(int id) {
        RuledSurface3 existing = ruledSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepRuledSurface surface = requireEntity(id, StepRuledSurface.class, "RULED_SURFACE");
        Axis2Placement3D position = buildPlacement(surface.getPosition().id());
        Curve3 directrix1 = buildCurve3(surface.getDirectrix1());
        Curve3 directrix2 = buildCurve3(surface.getDirectrix2());
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
        SurfaceGeometry sweptSurface = buildSupportedFaceGeometry(surface.getSweptSurface(), "SURFACE_OF_CONSTANT_RADIUS");
        if (sweptSurface == null) {
            throw new UnsupportedGeometryException("Swept surface is null for SURFACE_OF_CONSTANT_RADIUS");
        }
        SurfaceOfConstantRadius3 built = new SurfaceOfConstantRadius3(sweptSurface, surface.getRadius());
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
                buildCurve3(surface.getSweptCurve()),
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
                buildCurve3(surface.getSweptCurve()),
                axis.getLocation(),
                axis.getAxis()
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
        buildSupportedSurfaceGeometry(surface.getBasisSurface());
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
        buildSupportedSurfaceGeometry(surface.getBasisSurface());
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
        buildSupportedSurfaceGeometry(surface.getBasisSurface());
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
        return curveBuilder.buildBSplineCurve3(id);
    }

    /**
     * Builds a B-spline curve with explicit breakpoints.
     * Breakpoints define parameter values where the curve form changes.
     *
     * @param id STEP entity id
     * @return built B-spline curve
     */
    public BSplineCurve3 buildBSplineCurveWithBreakpoints(int id) {
        return curveBuilder.buildBSplineCurveWithBreakpoints(id);
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
        List<List<CartesianPoint>> controlPoints = surface.getControlPoints().stream()
                .map(row -> row.stream().map(point -> buildPoint(point.id())).collect(Collectors.toList()))
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
     * Generates uniform knot vectors based on degree and control point count.
     */
    public BSplineSurface3 buildGenericBSplineSurface(int id) {
        BSplineSurface3 existing = bsplineSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepBSplineSurface surface = requireEntity(id, StepBSplineSurface.class, "B_SPLINE_SURFACE");
        List<List<CartesianPoint>> controlPoints = surface.getControlPoints().stream()
                .map(row -> row.stream().map(point -> buildPoint(point.id())).collect(Collectors.toList()))
                .collect(Collectors.toList());
        int uCount = controlPoints.size();
        int vCount = controlPoints.get(0).size();
        // Generate uniform knot vectors with minimum multiplicity at ends
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
        StepBSplineKnotGenerator.ImplicitBSplineSurfaceData surface = implicitBSplineSurfaceData(entity);
        List<List<CartesianPoint>> controlPoints = surface.getControlPoints().stream()
                .map(row -> row.stream().map(point -> buildPoint(point.id())).collect(Collectors.toList()))
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

    public RationalBSplineCurve3 buildRationalBSplineCurve3(int id) {
        return curveBuilder.buildRationalBSplineCurve3(id);
    }

    public RationalBSplineCurve3 buildRationalBSplineCurve(int id) {
        return curveBuilder.buildRationalBSplineCurve3(id);
    }

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

    public RationalBSplineSurface3 buildRationalBSplineSurface(int id) {
        RationalBSplineSurface3 existing = rationalBsplineSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepRationalBSplineSurface surface =
                requireEntity(id, StepRationalBSplineSurface.class, "RATIONAL_B_SPLINE_SURFACE");
        List<List<CartesianPoint>> controlPoints = surface.getControlPoints().stream()
                .map(row -> row.stream().map(point -> buildPoint(point.id())).collect(Collectors.toList()))
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
     * Builds a trimmed curve backed by a supported basis curve.
     *
     * @param id STEP entity id
     * @return built trimmed curve
     */
    public TrimmedCurve3 buildTrimmedCurve(int id) {
        return curveBuilder.buildTrimmedCurve3(id);
    }

    /**
     * Builds a surface curve backed by a supported 3D curve.
     *
     * @param id STEP entity id
     * @return built surface curve
     */
    public SurfaceCurve3 buildSurfaceCurve(int id) {
        return curveBuilder.buildSurfaceCurve3(id);
    }

    public SurfaceCurve3 buildSeamCurve(int id) {
        return curveBuilder.buildSeamCurve(id);
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
        return curveBuilder.buildParabola3(id);
    }

    /**
     * Builds a hyperbola geometry object.
     *
     * @param id STEP entity id
     * @return built hyperbola
     */
    public Hyperbola3 buildHyperbola(int id) {
        return curveBuilder.buildHyperbola3(id);
    }

    /**
     * Builds a clothoid geometry object.
     *
     * @param id STEP entity id
     * @return built clothoid
     */
    public Clothoid3 buildClothoid(int id) {
        return curveBuilder.buildClothoid3(id);
    }

    /**
     * Converts a 2D placement to a 3D placement in the XY plane (Z=0).
     */
    private Axis2Placement3D convert2DPlacementTo3D(StepEntity position) {
        if (position instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D placement3D = (StepAxis2Placement3D) position;
            return buildPlacement(placement3D.id());
        }
        if (!(position instanceof StepAxis2Placement2D)) {
            throw new StepResolutionException("position must be AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
        }
        StepAxis2Placement2D placement2D = (StepAxis2Placement2D) position;
        Point2 origin = buildPoint2(placement2D.getLocation().id());
        Direction2 refDir = buildDirection2(placement2D.getRefDirection().id());
        // Create 3D placement: location at (x, y, 0), Z axis as normal, X direction from 2D
        CartesianPoint location3D = new CartesianPoint(origin.getX(), origin.getY(), 0.0);
        Direction3 axis = new Direction3(0, 0, 1);
        Direction3 xDirection = new Direction3(refDir.getX(), refDir.getY(), 0);
        return new Axis2Placement3D(location3D, axis, xDirection);
    }

    /**
     * Builds a topological vertex.
     *
     * @param id STEP entity id
     * @return built vertex
     */
    public Vertex buildVertex(int id) {
        return topologyBuilder.buildVertex(id);
    }

    /**
     * Builds a topological edge backed by a supported curve.
     *
     * @param id STEP entity id
     * @return built edge
     */
    public Edge buildEdge(int id) {
        return topologyBuilder.buildEdge(id);
    }

    /**
     * Builds an oriented edge.
     *
     * @param id STEP entity id
     * @return built oriented edge
     */
    public OrientedEdge buildOrientedEdge(int id) {
        return topologyBuilder.buildOrientedEdge(id);
    }

    /**
     * Builds an edge loop.
     *
     * @param id STEP entity id
     * @return built edge loop
     */
    public EdgeLoop buildEdgeLoop(int id) {
        return topologyBuilder.buildEdgeLoop(id);
    }

    /**
     * Builds a vertex loop.
     *
     * @param id STEP entity id
     * @return built vertex loop
     */
    public VertexLoop buildVertexLoop(int id) {
        return topologyBuilder.buildVertexLoop(id);
    }

    /**
     * Builds a poly loop from its STEP entity.
     *
     * @param id STEP entity id
     * @return built poly loop
     */
    public com.minicad.topology.PolyLoop buildPolyLoop(int id) {
        return topologyBuilder.buildPolyLoop(id);
    }

    /**
     * Builds a path into a composite curve.
     *
     * @param id STEP entity id
     * @return built composite curve representing the path geometry
     */
    public CompositeCurve3 buildPath(int id) {
        return topologyBuilder.buildPath(id);
    }

    /**
     * Builds a face bound.
     *
     * @param id STEP entity id
     * @return built face bound
     */
    public FaceBound buildFaceBound(int id) {
        return topologyBuilder.buildFaceBound(id);
    }

    /**
     * Builds a planar face.
     *
     * @param id STEP entity id
     * @return built face
     */
    public Face buildFace(int id) {
        return topologyBuilder.buildFace(id);
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
        if (vertexEntity instanceof StepVertexPoint) {
            StepVertexPoint vp = (StepVertexPoint) vertexEntity;
            return buildPoint(vp.point().id());
        }
        if (vertexEntity instanceof StepVertex) {
            StepVertex vertex = (StepVertex) vertexEntity;
            // StepVertex has no direct geometry; check if there's a VERTEX_POINT at the same ID
            StepEntity actual = entitiesById.get(vertex.id());
            if (actual instanceof StepVertexPoint) {
            StepVertexPoint vp = (StepVertexPoint) actual;
                return buildPoint(vp.point().id());
            }
            throw new UnsupportedGeometryException("VERTEX #" + vertex.id() + " has no associated point geometry");
        }
        throw new UnsupportedGeometryException("Expected vertex entity but got " + vertexEntity.getClass().getSimpleName());
    }

    Shell buildTessellatedFaceShell(StepTessellatedFace tessellated) {
        List<Face> faces = new ArrayList<>(tessellated.triangles().size());
        for (StepEntity triangleRef : tessellated.triangles()) {
            if (triangleRef instanceof StepTessellatedTriangle) {
            StepTessellatedTriangle triangle = (StepTessellatedTriangle) triangleRef;
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
            } else if (triangleRef instanceof StepCartesianPoint) {
            StepCartesianPoint cp = (StepCartesianPoint) triangleRef;
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
            if (v instanceof StepCartesianPoint) {
            StepCartesianPoint cp = (StepCartesianPoint) v;
                points.add(buildPoint(cp.id()));
            } else if (v instanceof StepVertexPoint) {
            StepVertexPoint vp = (StepVertexPoint) v;
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
            if (v instanceof StepCartesianPoint) {
            StepCartesianPoint cp = (StepCartesianPoint) v;
                points.add(buildPoint(cp.id()));
            } else if (v instanceof StepVertexPoint) {
            StepVertexPoint vp = (StepVertexPoint) v;
                points.add(buildPoint(vp.point().id()));
            }
        }
        List<Face> faces = new ArrayList<>();
        for (StepEntity boundary : complex.boundaries()) {
            if (boundary instanceof StepEdgeLoop) {
            StepEdgeLoop loop = (StepEdgeLoop) boundary;
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
            if (edge.getStart() instanceof StepVertexPoint) {
                StepVertexPoint startV = (StepVertexPoint) edge.getStart();
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
        List<CartesianPoint> points = new ArrayList<>(bezier.getControlPoints().size());
        for (StepEntity cp : bezier.getControlPoints()) {
            if (cp instanceof StepCartesianPoint) {
            StepCartesianPoint cartesianPoint = (StepCartesianPoint) cp;
                points.add(buildPoint(cartesianPoint.id()));
            } else if (cp instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) cp;
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
            if (node instanceof StepCartesianPoint) {
            StepCartesianPoint cp = (StepCartesianPoint) node;
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
            if (element instanceof StepCartesianPoint) {
            StepCartesianPoint cp = (StepCartesianPoint) element;
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
        return booleanBuilder.buildCsgVolumeSolid(csgVolume);
    }

    /**
     * Builds a solid from a BLOCK_VOLUME.
     * Similar to BLOCK CSG primitive but with explicit StepBlockVolume entity.
     */
    Solid buildBlockVolume(StepBlockVolume blockVolume) {
        if (!(blockVolume.getPosition() instanceof StepAxis2Placement3D)) {
            throw new UnsupportedGeometryException("BLOCK_VOLUME position must be an AXIS2_PLACEMENT_3D");
        }
        StepAxis2Placement3D placement = (StepAxis2Placement3D) blockVolume.getPosition();
        double x = blockVolume.xLength();
        double y = blockVolume.yLength();
        double z = blockVolume.zLength();
        if (x <= 0.0 || y <= 0.0 || z <= 0.0) {
            throw new UnsupportedGeometryException("BLOCK_VOLUME dimensions must be positive");
        }
        Axis2Placement3D blockPlacement = buildPlacement(placement.id());
        CartesianPoint origin = blockPlacement.getLocation();
        Vector3 alongX = blockPlacement.xDirection().asVector().scale(x);
        Vector3 alongY = blockPlacement.yDirection().asVector().scale(y);
        Vector3 alongZ = blockPlacement.getAxis().asVector().scale(z);
        List<CartesianPoint> bottom = List.of(
                origin,
                origin.add(alongX),
                origin.add(alongX).add(alongY),
                origin.add(alongY)
        );
        List<CartesianPoint> top = bottom.stream().map(point -> point.add(alongZ)).collect(Collectors.toList());

        List<Face> faces = new ArrayList<>();
        Direction3 normalZ = blockPlacement.getAxis().reverse();
        faces.add(faceFromPolyLoop(reverseClosedLoop3(bottom), normalZ));
        faces.add(faceFromPolyLoop(top, blockPlacement.getAxis()));

        Direction3 normalX = Direction3.from(blockPlacement.getAxis().asVector().cross(blockPlacement.xDirection().asVector())).reverse();
        List<CartesianPoint> rightFace = List.of(bottom.get(0), top.get(0), top.get(3), bottom.get(3));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(rightFace), normalX));

        Direction3 normalY = blockPlacement.yDirection();
        List<CartesianPoint> frontFace = List.of(bottom.get(1), bottom.get(2), top.get(2), top.get(1));
        faces.add(faceFromPolyLoop(reverseClosedLoop3(frontFace), normalY));

        Direction3 normalXPos = Direction3.from(blockPlacement.getAxis().asVector().cross(blockPlacement.xDirection().asVector()));
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
        if (halfSpace.enclosure() instanceof StepBoxDomain) {
            StepBoxDomain boxDomain = (StepBoxDomain) halfSpace.enclosure();
            // Create a temporary infinite solid and clip it
            // For standalone half-space with box enclosure, just return the box portion
            return boxDomainToSolid(boxDomain);
        }
        // Unbounded half-space: create a large capped solid on the agreement side
        Direction3 normal = halfSpace.agreementFlag() ? plane.getNormal() : plane.getNormal().reverse();
        // Create a large capped box extending from the plane
        CartesianPoint center = plane.getOrigin();
        double extent = 1e6; // large extent for "infinite" approximation
        Vector3 n = normal.asVector();
        Vector3 xDir = plane.getNormal().perpendicular().asVector();
        Vector3 yDir = n.cross(xDir);
        CartesianPoint origin = new CartesianPoint(
                center.getX() - n.getX() * extent / 2,
                center.getY() - n.getY() * extent / 2,
                center.getZ() - n.getZ() * extent / 2);
        List<CartesianPoint> bottom = List.of(
                origin,
                new CartesianPoint(origin.getX() + xDir.getX() * extent, origin.getY() + xDir.getY() * extent, origin.getZ() + xDir.getZ() * extent),
                new CartesianPoint(origin.getX() + xDir.getX() * extent + yDir.getX() * extent,
                        origin.getY() + xDir.getY() * extent + yDir.getY() * extent,
                        origin.getZ() + xDir.getZ() * extent + yDir.getZ() * extent),
                new CartesianPoint(origin.getX() + yDir.getX() * extent, origin.getY() + yDir.getY() * extent, origin.getZ() + yDir.getZ() * extent)
        );
        List<CartesianPoint> top = bottom.stream().map(p -> new CartesianPoint(p.getX(), p.getY(), p.getZ() + n.getZ() * extent)).collect(Collectors.toList());

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
        SurfaceGeometry basisSurface = buildSupportedFaceGeometry(polyHalfSpace.getBasisSurface(), "SURFACE");
        if (!(basisSurface instanceof Plane)) {
            throw new UnsupportedGeometryException("POLYGONAL_BOUNDED_HALF_SPACE requires a planar basis surface");
        }
        Plane plane = (Plane) basisSurface;
        // Build polygon points as a face boundary
        List<CartesianPoint> polyPoints = polyHalfSpace.polygonPoints().stream()
                .map(cp -> buildPoint(cp.id()))
                .collect(Collectors.toList());
        if (polyPoints.size() < 3) {
            throw new UnsupportedGeometryException("POLYGONAL_BOUNDED_HALF_SPACE requires at least 3 polygon points");
        }
        Direction3 normal = polyHalfSpace.isSameSense() ? plane.getNormal() : plane.getNormal().reverse();
        Plane orientedPlane = new Plane(plane.getOrigin(), normal);
        // Create face from the polygon
        Face polyFace = new Face(orientedPlane, List.of(FaceBound.outer(new PolyLoop(polyPoints), true)), true);
        // Extrude along normal to create a capped solid
        double thickness = 1.0; // minimal thickness for standalone representation
        Vector3 extrude = normal.asVector().scale(thickness);
        List<CartesianPoint> top = polyPoints.stream().map(p -> p.add(extrude)).collect(Collectors.toList());

        List<Face> faces = new ArrayList<>();
        faces.add(new Face(orientedPlane, List.of(FaceBound.outer(new PolyLoop(polyPoints), true)), true));
        faces.add(new Face(new Plane(top.get(0), normal), List.of(FaceBound.outer(new PolyLoop(top), true)), true));
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
                new CartesianPoint(corner.getX() + x, corner.getY(), corner.getZ()),
                new CartesianPoint(corner.getX() + x, corner.getY() + y, corner.getZ()),
                new CartesianPoint(corner.getX(), corner.getY() + y, corner.getZ())
        );
        List<CartesianPoint> top = bottom.stream().map(p -> new CartesianPoint(p.getX(), p.getY(), p.getZ() + z)).collect(Collectors.toList());
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
        return sweptBuilder.buildExtrudedFaceSolid(extrudedFace);
    }

    Solid buildRevolvedFaceSolid(StepRevolvedFaceSolid revolvedFace) {
        return sweptBuilder.buildRevolvedFaceSolid(revolvedFace);
    }

    Solid buildSweptFaceSolid(StepSweptFaceSolid sweptFace) {
        return sweptBuilder.buildSweptFaceSolid(sweptFace);
    }

    Solid buildCylinderVolume(StepCylinderVolume cyl) {
        double radius = cyl.getRadius();
        double height = cyl.height();
        if (radius <= 0 || height <= 0) {
            throw new UnsupportedGeometryException("CYLINDER_VOLUME requires positive dimensions");
        }
        Axis2Placement3D placement;
        if (cyl.getPosition() instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D a2 = (StepAxis2Placement3D) cyl.getPosition();
            placement = buildPlacement(a2.id());
        } else {
            placement = null;
        }
        // Create cylinder: sample circular profile in local XY plane, extrude along local Z
        List<CartesianPoint> localRing = buildCircleRing(radius, 72);
        List<CartesianPoint> ring = placement != null
                ? localRing.stream().map(placement::transformToWorld).collect(Collectors.toList())
                : localRing;
        Direction3 direction = placement != null
                ? placement.transformDirectionToWorld(Direction3.from(new com.minicad.geometry.Vector3(0, 0, 1)))
                : Direction3.from(new com.minicad.geometry.Vector3(0, 0, 1));
        return buildExtrudedProfile(ring, direction, height);
    }

    Solid buildSphereVolume(StepSphereVolume sphere) {
        double radius = sphere.getRadius();
        if (radius <= 0) {
            throw new UnsupportedGeometryException("SPHERE_VOLUME requires positive radius");
        }
        CartesianPoint center;
        if (sphere.center() instanceof StepCartesianPoint) {
            StepCartesianPoint cp = (StepCartesianPoint) sphere.center();
            center = buildPoint(cp.id());
        } else {
            center = new CartesianPoint(0, 0, 0);
        }
        // Tessellate sphere centered at the specified center point
        List<Face> faces = StepPrimitiveTessellator.tessellateSphereAt(center, radius, 24, 48);
        return new Solid(new Shell(faces, true));
    }

    Solid buildTorusVolume(StepTorusVolume torus) {
        double majorR = torus.getMajorRadius();
        double minorR = torus.getMinorRadius();
        if (majorR <= 0 || minorR <= 0) {
            throw new UnsupportedGeometryException("TORUS_VOLUME requires positive radii");
        }
        Axis2Placement3D placement;
        if (torus.getPosition() instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D a2 = (StepAxis2Placement3D) torus.getPosition();
            placement = buildPlacement(a2.id());
        } else {
            placement = null;
        }
        List<Face> faces = placement != null
                ? StepPrimitiveTessellator.tessellateTorusAt(placement, majorR, minorR, 36, 24)
                : StepPrimitiveTessellator.tessellateTorus(majorR, minorR, 36, 24);
        return new Solid(new Shell(faces, true));
    }

    Solid buildPrismVolume(StepPrismVolume prism) {
        double w = prism.width();
        double d = prism.depth();
        double h = prism.height();
        if (w <= 0 || d <= 0 || h <= 0) {
            throw new UnsupportedGeometryException("PRISM_VOLUME requires positive dimensions");
        }
        Axis2Placement3D placement;
        if (prism.getPosition() instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D a2 = (StepAxis2Placement3D) prism.getPosition();
            placement = buildPlacement(a2.id());
        } else {
            placement = null;
        }
        // Build prism in local coordinate system: XY rectangle, extrude along local Z
        CartesianPoint origin = placement != null ? placement.getLocation() : new CartesianPoint(0, 0, 0);
        com.minicad.geometry.Vector3 xDir = placement != null ? placement.xDirection().asVector() : new com.minicad.geometry.Vector3(1, 0, 0);
        com.minicad.geometry.Vector3 yDir = placement != null ? placement.yDirection().asVector() : new com.minicad.geometry.Vector3(0, 1, 0);
        com.minicad.geometry.Vector3 zDir = placement != null ? placement.getAxis().asVector() : new com.minicad.geometry.Vector3(0, 0, 1);
        List<CartesianPoint> bottom = List.of(
                origin,
                origin.add(xDir.scale(w)),
                origin.add(xDir.scale(w)).add(yDir.scale(d)),
                origin.add(yDir.scale(d)));
        List<CartesianPoint> top = bottom.stream()
                .map(p -> p.add(zDir.scale(h)))
                .collect(Collectors.toList());
        List<Face> faces = StepPrimitiveTessellator.buildBoxFaces(bottom, top);
        return new Solid(new Shell(faces, true));
    }

    Solid buildRightCircularConeVolume(StepRightCircularConeVolume cone) {
        double height = cone.height();
        double bottomRadius = cone.bottomRadius();
        double topRadius = cone.topRadius() != null ? cone.topRadius() : 0.0;
        if (bottomRadius <= 0 || height <= 0) {
            throw new UnsupportedGeometryException("CONE_VOLUME requires positive dimensions");
        }
        if (topRadius < 0) {
            throw new UnsupportedGeometryException("CONE_VOLUME topRadius must be non-negative");
        }
        // Build cone: bottom ring at z=0, top ring at z=height
        int segments = 72;
        List<CartesianPoint> bottomRing = buildCircleRing(bottomRadius, segments);
        List<CartesianPoint> topRing = topRadius > 0
                ? buildCircleRingAtZ(topRadius, height, segments)
                : List.of(new CartesianPoint(0, 0, height)); // apex point

        List<Face> faces = new ArrayList<>();
        // Bottom cap
        faces.add(faceFromPolyLoop(reverseClosedLoop3(bottomRing), Direction3.from(new com.minicad.geometry.Vector3(0, 0, -1))));
        // Side faces (triangles for apex, quads for truncated cone)
        for (int i = 0; i < segments; i++) {
            int next = (i + 1) % segments;
            if (topRadius > 0) {
                faces.add(faceFromPolyLoop(
                        List.of(bottomRing.get(i), bottomRing.get(next), topRing.get(next), topRing.get(i)),
                        quadNormal(bottomRing.get(i), bottomRing.get(next), topRing.get(next), topRing.get(i))));
            } else {
                CartesianPoint apex = topRing.get(0);
                faces.add(faceFromPolyLoop(
                        List.of(bottomRing.get(i), bottomRing.get(next), apex),
                        triangleNormal(bottomRing.get(i), bottomRing.get(next), apex)));
            }
        }
        // Top cap for truncated cone
        if (topRadius > 0) {
            faces.add(faceFromPolyLoop(closeLoop3(topRing), Direction3.from(new com.minicad.geometry.Vector3(0, 0, 1))));
        }
        return new Solid(new Shell(faces, true));
    }

    // Helper methods for new solid builders

    Solid buildExtrudedProfile(List<CartesianPoint> profile, Direction3 direction, double depth) {
        return sweptBuilder.buildExtrudedProfile(profile, direction, depth);
    }

    Solid buildRevolvedProfile(List<CartesianPoint> profile, CartesianPoint axisOrigin,
                                        Vector3 axis, double angle) {
        return sweptBuilder.buildRevolvedProfile(profile, axisOrigin, axis, angle);
    }

    Solid buildSweptProfileAlongCurve(List<CartesianPoint> profile, List<StepCadSweptBuilder.Curve3Sample> samples) {
        return sweptBuilder.buildSweptProfileAlongCurve(profile, samples);
    }

    List<CartesianPoint> buildCircleRing(double radius, int segments) {
        List<CartesianPoint> points = new ArrayList<>();
        for (int i = 0; i < segments; i++) {
            double theta = 2 * Math.PI * i / segments;
            points.add(new CartesianPoint(radius * Math.cos(theta), radius * Math.sin(theta), 0));
        }
        return points;
    }

    private List<CartesianPoint> buildCircleRingAtZ(double radius, double z, int segments) {
        List<CartesianPoint> points = new ArrayList<>();
        for (int i = 0; i < segments; i++) {
            double theta = 2 * Math.PI * i / segments;
            points.add(new CartesianPoint(radius * Math.cos(theta), radius * Math.sin(theta), z));
        }
        return points;
    }

    private Direction3 triangleNormal(CartesianPoint a, CartesianPoint b, CartesianPoint c) {
        Vector3 ab = new Vector3(b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ());
        Vector3 ac = new Vector3(c.getX() - a.getX(), c.getY() - a.getY(), c.getZ() - a.getZ());
        return Direction3.from(ab.cross(ac));
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
        if (stepFace instanceof StepAdvancedFace) {
            StepAdvancedFace advancedFace = (StepAdvancedFace) stepFace;
            return advancedFace.faceGeometry();
        }
        if (stepFace instanceof StepFaceSurface) {
            StepFaceSurface faceSurface = (StepFaceSurface) stepFace;
            return faceSurface.faceGeometry();
        }
        if (stepFace instanceof StepOrientedFace) {
            StepOrientedFace orientedFace = (StepOrientedFace) stepFace;
            return faceGeometry(orientedFace.faceElement());
        }
        throw new UnsupportedGeometryException("unsupported face subtype");
    }

    static boolean faceSameSense(StepFaceEntity stepFace) {
        if (stepFace instanceof StepAdvancedFace) {
            StepAdvancedFace advancedFace = (StepAdvancedFace) stepFace;
            return advancedFace.isSameSense();
        }
        if (stepFace instanceof StepFaceSurface) {
            StepFaceSurface faceSurface = (StepFaceSurface) stepFace;
            return faceSurface.isSameSense();
        }
        if (stepFace instanceof StepOrientedFace) {
            StepOrientedFace orientedFace = (StepOrientedFace) stepFace;
            boolean base = faceSameSense(orientedFace.faceElement());
            return orientedFace.isOrientation() ? base : !base;
        }
        throw new UnsupportedGeometryException("unsupported face subtype");
    }

    Solid buildSweptAreaSolid(StepSweptAreaSolid sweptAreaSolid) {
        return sweptBuilder.buildSweptAreaSolid(sweptAreaSolid);
    }

    Solid buildCsgPrimitive(StepCsgPrimitive csgPrimitive) {
        return booleanBuilder.buildCsgPrimitive(csgPrimitive);
    }

    Solid buildSweptDiskSolid(StepSweptDiskSolid sweptDiskSolid) {
        return sweptBuilder.buildSweptDiskSolid(sweptDiskSolid);
    }

    Solid buildExtrudedAreaSolidTapered(StepExtrudedAreaSolidTapered tapered) {
        return sweptBuilder.buildExtrudedAreaSolidTapered(tapered);
    }

    Solid buildRevolvedAreaSolidTapered(StepRevolvedAreaSolidTapered tapered) {
        return sweptBuilder.buildRevolvedAreaSolidTapered(tapered);
    }

    Solid buildSurfaceCurveSweptAreaSolid(StepSurfaceCurveSweptAreaSolid swept) {
        return sweptBuilder.buildSurfaceCurveSweptAreaSolid(swept);
    }

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
            List<CartesianPoint> firstRing = rings.get(0);
            for (int uIndex = 0; uIndex < uSegments; uIndex++) {
                CartesianPoint b = firstRing.get(uIndex);
                CartesianPoint c = firstRing.get((uIndex + 1) % uSegments);
                addTriangleFace(faces, north, b, c, north.subtract(placement.getLocation()));
            }
            for (int ringIndex = 0; ringIndex < rings.size() - 1; ringIndex++) {
                List<CartesianPoint> lower = rings.get(ringIndex);
                List<CartesianPoint> upper = rings.get(ringIndex + 1);
                for (int uIndex = 0; uIndex < uSegments; uIndex++) {
                    CartesianPoint a = lower.get(uIndex);
                    CartesianPoint b = lower.get((uIndex + 1) % uSegments);
                    CartesianPoint c = upper.get((uIndex + 1) % uSegments);
                    CartesianPoint d = upper.get(uIndex);
                    addTriangleFace(faces, a, b, c, outwardApproximation(a, placement.getLocation()));
                    addTriangleFace(faces, a, c, d, outwardApproximation(d, placement.getLocation()));
                }
            }
            List<CartesianPoint> lastRing = rings.get(rings.size() - 1);
            for (int uIndex = 0; uIndex < uSegments; uIndex++) {
                CartesianPoint a = lastRing.get(uIndex);
                CartesianPoint b = lastRing.get((uIndex + 1) % uSegments);
                addTriangleFace(faces, a, south, b, south.subtract(placement.getLocation()));
            }
        }
        return new Solid(new Shell(faces, true));
    }

    private CartesianPoint pointOnPlacement(Axis2Placement3D placement, double x, double y, double z) {
        return placement.getLocation()
                .add(placement.xDirection().asVector().scale(x))
                .add(placement.yDirection().asVector().scale(y))
                .add(placement.getAxis().asVector().scale(z));
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

    private Direction3 polygonNormal(List<CartesianPoint> points, Vector3 fallback) {
        Vector3 normal = new Vector3(0.0, 0.0, 0.0);
        for (int index = 0; index < points.size(); index++) {
            CartesianPoint current = points.get(index);
            CartesianPoint next = points.get((index + 1) % points.size());
            normal = normal.add(new Vector3(
                    (current.getY() - next.getY()) * (current.getZ() + next.getZ()),
                    (current.getZ() - next.getZ()) * (current.getX() + next.getX()),
                    (current.getX() - next.getX()) * (current.getY() + next.getY())
            ));
        }
        if (normal.isZero()) {
            normal = fallback;
        }
        if (normal.isZero()) {
            throw new UnsupportedGeometryException("polygon normal is degenerate");
        }
        if (!fallback.isZero() && normal.dot(fallback) < 0.0) {
            normal = normal.scale(-1.0);
        }
        return Direction3.from(normal.normalize());
    }

    private Direction3 quadNormal(CartesianPoint a, CartesianPoint b, CartesianPoint c, CartesianPoint d) {
        Vector3 normal = b.subtract(a).cross(c.subtract(a));
        if (normal.isZero()) {
            normal = c.subtract(a).cross(d.subtract(a));
        }
        if (normal.isZero()) {
            throw new UnsupportedGeometryException("quad face is degenerate");
        }
        return Direction3.from(normal.normalize());
    }

    Solid buildBooleanResult(String operator, StepEntity first, StepEntity second) {
        return booleanBuilder.buildBooleanResult(operator, first, second);
    }

    Solid buildBooleanOperandSolid(StepEntity operand) {
        return booleanBuilder.buildBooleanOperandSolid(operand);
    }

    Face faceFromPolyLoop(List<CartesianPoint> points, Direction3 normal) {
        Plane plane = new Plane(points.get(0), normal);
        return new Face(plane, List.of(FaceBound.outer(new PolyLoop(points), true)), true);
    }

    Face faceFromProfileLoops(
            List<CartesianPoint> outer,
            List<List<CartesianPoint>> innerLoops,
            Direction3 normal
    ) {
        Plane plane = new Plane(outer.get(0), normal);
        List<FaceBound> bounds = new ArrayList<>();
        bounds.add(FaceBound.outer(new PolyLoop(outer), true));
        for (List<CartesianPoint> inner : innerLoops) {
            bounds.add(FaceBound.inner(new PolyLoop(inner), true));
        }
        return new Face(plane, bounds, true);
    }

    List<List<CartesianPoint>> closeLoops3(List<List<CartesianPoint>> loops) {
        return loops.stream().map(this::closeLoop3).collect(Collectors.toList());
    }

    List<List<CartesianPoint>> reverseClosedLoops3(List<List<CartesianPoint>> loops) {
        return loops.stream().map(this::reverseClosedLoop3).collect(Collectors.toList());
    }

    Solid transformSolid(Solid solid, StepCartesianTransformationOperator transformation) {
        return new Solid(
                transformShell(solid.getOuterShell(), transformation),
                solid.getVoidShells().stream()
                        .map(voidShell -> transformShell(voidShell, transformation))
                        .collect(Collectors.toList())
        );
    }

    private Shell transformShell(Shell shell, StepCartesianTransformationOperator transformation) {
        return new Shell(
                shell.getFaces().stream()
                        .map(face -> transformFace(face, transformation))
                        .collect(Collectors.toList()),
                shell.isClosed()
        );
    }

    private Face transformFace(Face face, StepCartesianTransformationOperator transformation) {
        return new Face(
                transformSurfaceGeometry(face.getSurface(), transformation),
                face.getBounds().stream()
                        .map(bound -> transformFaceBound(bound, transformation))
                        .collect(Collectors.toList()),
                face.isSameSense()
        );
    }

    private FaceBound transformFaceBound(FaceBound bound, StepCartesianTransformationOperator transformation) {
        return bound.isOuter()
                ? FaceBound.outer(transformLoop(bound.getLoop(), transformation), bound.isOrientation())
                : FaceBound.inner(transformLoop(bound.getLoop(), transformation), bound.isOrientation());
    }

    private Loop transformLoop(Loop loop, StepCartesianTransformationOperator transformation) {
        if (loop instanceof EdgeLoop) {
            EdgeLoop edgeLoop = (EdgeLoop) loop;
            return new EdgeLoop(edgeLoop.edges().stream()
                    .map(edge -> transformOrientedEdge(edge, transformation))
                    .collect(Collectors.toList()));
        }
        if (loop instanceof VertexLoop) {
            VertexLoop vertexLoop = (VertexLoop) loop;
            return new VertexLoop(transformVertex(vertexLoop.getVertex(), transformation));
        }
        if (loop instanceof PolyLoop) {
            PolyLoop polyLoop = (PolyLoop) loop;
            return new PolyLoop(polyLoop.getPoints().stream()
                    .map(point -> transformPoint3(point, transformation))
                    .collect(Collectors.toList()));
        }
        throw new UnsupportedGeometryException("loop replica for " + loopTypeName(loop) + " is unsupported");
    }

    private OrientedEdge transformOrientedEdge(OrientedEdge edge, StepCartesianTransformationOperator transformation) {
        return new OrientedEdge(transformEdge(edge.getEdge(), transformation), edge.isOrientation());
    }

    private Edge transformEdge(Edge edge, StepCartesianTransformationOperator transformation) {
        return new Edge(
                transformVertex(edge.getStart(), transformation),
                transformVertex(edge.getEnd(), transformation),
                transformCurve3(edge.getCurve(), transformation),
                edge.isSameSense()
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
        StepBSplineKnotGenerator.ImplicitBSplineCurveData spline = implicitBSplineCurveData(entity);
        List<CartesianPoint> controlPoints = spline.getControlPoints().stream().map(point -> buildPoint(point.id())).collect(Collectors.toList());
        BSplineCurve3 built = new BSplineCurve3(
                spline.getDegree(),
                controlPoints,
                spline.getKnotMultiplicities(),
                spline.getKnots()
        );
        bsplineCurves.put(entity.id(), built);
        return built;
    }

    /**
     * Builds a Curve3 by entity ID (for callback from StepCadCurveBuilder).
     */
    private Curve3 buildCurve3ById(int id) {
        StepEntity entity = requireExistingEntity(id);
        return buildCurve3(entity);
    }

    // buildCurve3 dispatch table (first-match-return,
    // mirrors the original sequential ifs).
    private record Curve3Rule(
            Class<? extends StepEntity> type, Curve3Handler handler) {}

    private interface Curve3Handler {
        Curve3 build(StepEntity curve);
    }

    private static Curve3Rule curve3Rule(
            Class<? extends StepEntity> type, Curve3Handler handler) {
        return new Curve3Rule(type, handler);
    }

    private final List<Curve3Rule> CURVE3_RULES;

    Curve3 buildCurve3(StepEntity curve) {
        // Handle special curve types first (annotation wrappers, paths, etc.)
        // These are not handled by curveBuilder and must be processed here
        for (Curve3Rule rule : CURVE3_RULES) {
            if (rule.type().isInstance(curve)) {
                return rule.handler().build(curve);
            }
        }

        // Delegate core 3D curve types to curveBuilder
        return curveBuilder.buildCurve3Internal(curve);
    }

    private Curve3 buildClothoidCurve(StepClothoid clothoid) {
        // Return proper Clothoid3 geometry object
        return buildClothoid(clothoid.id());
    }

    /**
     * Reverses a composite curve by reversing segment order and reversing each segment.
     */
    private CompositeCurve3 reverseCompositeCurve(CompositeCurve3 original) {
        List<Curve3> reversedSegments = new java.util.ArrayList<>(original.getSegments());
        java.util.Collections.reverse(reversedSegments);
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
        if (curve instanceof Line3) {
            Line3 line = (Line3) curve;
            return new Line3(line.getOrigin(), line.getDirection().reverse(), line.getParameterScale());
        }
        if (curve instanceof Polyline3) {
            Polyline3 polyline = (Polyline3) curve;
            List<CartesianPoint> reversedPoints = new java.util.ArrayList<>(polyline.getPoints());
            java.util.Collections.reverse(reversedPoints);
            return new Polyline3(reversedPoints);
        }
        if (curve instanceof CompositeCurve3) {
            CompositeCurve3 composite = (CompositeCurve3) curve;
            return reverseCompositeCurve(composite);
        }
        if (curve instanceof Circle) {
            Circle circle = (Circle) curve;
            Axis2Placement3D p = circle.getPosition();
            return new Circle(
                    new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                    circle.getRadius());
        }
        if (curve instanceof Ellipse3) {
            Ellipse3 ellipse = (Ellipse3) curve;
            Axis2Placement3D p = ellipse.getPosition();
            return new Ellipse3(
                    new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                    ellipse.getSemiAxis1(), ellipse.getSemiAxis2());
        }
        if (curve instanceof Parabola3) {
            Parabola3 parabola = (Parabola3) curve;
            Axis2Placement3D p = parabola.getPosition();
            return new Parabola3(
                    new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                    parabola.getFocalLength());
        }
        if (curve instanceof Hyperbola3) {
            Hyperbola3 hyperbola = (Hyperbola3) curve;
            Axis2Placement3D p = hyperbola.getPosition();
            return new Hyperbola3(
                    new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                    hyperbola.getSemiAxisA(), hyperbola.getSemiAxisB());
        }
        if (curve instanceof Clothoid3) {
            Clothoid3 clothoid = (Clothoid3) curve;
            Axis2Placement3D p = clothoid.getPosition();
            return new Clothoid3(
                    new Axis2Placement3D(p.getLocation(), p.getAxis(), p.xDirection().reverse()),
                    clothoid.xAxisIntercept(), clothoid.curvature());
        }
        if (curve instanceof DegenerateCurve3) {
            DegenerateCurve3 degenerate = (DegenerateCurve3) curve;
            return new DegenerateCurve3(degenerate.point());
        }
        if (curve instanceof TrimmedCurve3) {
            TrimmedCurve3 trimmed = (TrimmedCurve3) curve;
            // Swap trim parameters and flip sense to reverse the curve
            return new TrimmedCurve3(
                    reverseCurve3(trimmed.getBasisCurve()),
                    trimmed.getTrimParamEnd(),
                    trimmed.getTrimParamStart(),
                    !trimmed.isSenseAgreement());
        }
        if (curve instanceof SurfaceCurve3) {
            SurfaceCurve3 surfaceCurve = (SurfaceCurve3) curve;
            return new SurfaceCurve3(
                    reverseCurve3(surfaceCurve.getCurve3d()),
                    surfaceCurve.getParametricCurves());
        }
        if (curve instanceof BSplineCurve3) {
            BSplineCurve3 bspline = (BSplineCurve3) curve;
            return new BSplineCurve3(
                    bspline.getDegree(),
                    reverseList(bspline.getControlPoints()),
                    bspline.getKnotMultiplicities(),
                    bspline.getKnots());
        }
        if (curve instanceof RationalBSplineCurve3) {
            RationalBSplineCurve3 rational = (RationalBSplineCurve3) curve;
            return new RationalBSplineCurve3(
                    rational.getDegree(),
                    reverseList(rational.getControlPoints()),
                    rational.getWeights(),
                    rational.getKnotMultiplicities(),
                    rational.getKnots());
        }
        return curve;
    }

    /**
     * Reverses the surface sense (normal direction) for oriented surfaces.
     * When an ORIENTED_SURFACE has orientation=false, the surface normal should be flipped.
     */
    private SurfaceGeometry reverseSurfaceSense(SurfaceGeometry surface) {
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

    private static java.util.List<java.util.List<CartesianPoint>> reverseBSplineControlGrid(
            java.util.List<java.util.List<CartesianPoint>> grid) {
        java.util.List<java.util.List<CartesianPoint>> result = new java.util.ArrayList<>(grid.size());
        for (java.util.List<CartesianPoint> row : grid) {
            result.add(reverseList(row));
        }
        return java.util.List.copyOf(result);
    }

    /**
     * Reverses a list in Java 11 compatible way.
     */
    private static <T> java.util.List<T> reverseList(java.util.List<T> list) {
        java.util.List<T> reversed = new java.util.ArrayList<>(list.size());
        for (int i = list.size() - 1; i >= 0; i--) {
            reversed.add(list.get(i));
        }
        return reversed;
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
        List<StepCartesianPoint> stepPoints = polyCurve.getPoints();
        List<Integer> indices = polyCurve.indices();
        List<CartesianPoint> points = indices.stream()
                .map(index -> buildPoint(stepPoints.get(index).id()))
                .collect(Collectors.toList());
        if (polyCurve.isClosed() && !points.isEmpty()) {
            points = new ArrayList<>(points);
            points.add(points.get(0));
            points = List.copyOf(points);
        }
        return new Polyline3(points);
    }

    private Curve3 buildPolyline3D(StepPolyline3D polyline3D) {
        // Polyline defined by entity references to Cartesian points
        List<CartesianPoint> points = polyline3D.getPoints().stream()
                .map(pt -> {
                    if (pt instanceof StepCartesianPoint) {
            StepCartesianPoint cartesian = (StepCartesianPoint) pt;
                        return buildPoint(cartesian.id());
                    }
                    throw new UnsupportedGeometryException("POLYLINE_3D point #" + pt.id() + " is not a CARTESIAN_POINT");
                })
                .collect(Collectors.toList());
        return points.isEmpty() ? new Polyline3(List.of()) : new Polyline3(points);
    }

    private Curve3 buildDegenerateCurve3(StepDegenerateCurve degenerateCurve) {
        // Degenerate curve collapses to a point
        Curve3 basis = buildCurve3(degenerateCurve.getBasisCurve());
        List<CartesianPoint> sampledPoints = sampleCurve3(basis, 2);
        if (sampledPoints.isEmpty()) {
            throw new UnsupportedGeometryException("DEGENERATE_CURVE basis curve has no sample points");
        }
        // Return a degenerate curve at the first sample point
        CartesianPoint point = sampledPoints.get(0);
        return new DegenerateCurve3(point);
    }

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

    private StepBSplineKnotGenerator.ImplicitBSplineSurfaceData implicitBSplineSurfaceData(StepEntity entity) {
        if (entity instanceof StepBezierSurface) {
            StepBezierSurface surface = (StepBezierSurface) entity;
            return StepBSplineKnotGenerator.implicitBezierSurface(surface.getUDegree(), surface.getVDegree(), surface.getControlPoints(), stepEntityTypeName(entity));
        }
        if (entity instanceof StepUniformSurface) {
            StepUniformSurface surface = (StepUniformSurface) entity;
            return StepBSplineKnotGenerator.implicitUniformSurface(surface.getUDegree(), surface.getVDegree(), surface.getControlPoints(), stepEntityTypeName(entity));
        }
        if (entity instanceof StepQuasiUniformSurface) {
            StepQuasiUniformSurface surface = (StepQuasiUniformSurface) entity;
            return StepBSplineKnotGenerator.implicitQuasiUniformSurface(surface.getUDegree(), surface.getVDegree(), surface.getControlPoints(), stepEntityTypeName(entity));
        }
        if (entity instanceof StepPiecewiseBezierSurface) {
            StepPiecewiseBezierSurface surface = (StepPiecewiseBezierSurface) entity;
            return StepBSplineKnotGenerator.implicitPiecewiseBezierSurface(surface.getUDegree(), surface.getVDegree(), surface.getControlPoints(), stepEntityTypeName(entity));
        }
        throw new UnsupportedGeometryException(stepEntityTypeName(entity) + " implicit knot data is unsupported");
    }

    public Curve3 buildOffsetCurve3(int id) {
        return curveBuilder.buildOffsetCurve3(id);
    }

    private Curve3 buildReplicaCurve3(StepGeometricReplica replica) {
        Curve3 parent = buildCurve3(replica.parent());
        return transformCurve3(parent, replica.transformation());
    }

    private Curve2 buildReplicaCurve2(StepGeometricReplica replica) {
        Object built = buildCurve2(replica.parent());
        if (!(built instanceof Curve2)) {
            throw new UnsupportedGeometryException(replica.entityName() + " parent is not a supported 2D curve");
        }
        Curve2 parent = (Curve2) built;
        return transformCurve2(parent, replica.transformation());
    }

    private Curve3 buildConicCurve3(StepConicCurve conic) {
        if (!(conic.getPosition() instanceof StepAxis2Placement3D)) {
            throw new UnsupportedGeometryException("3D conic curve for " + conic.entityName() + " requires AXIS2_PLACEMENT_3D");
        }
        StepAxis2Placement3D placement3D = (StepAxis2Placement3D) conic.getPosition();
        String entityName = conic.entityName();
        switch (entityName) {
            case "PARABOLA":
                return buildParabola(conic.id());
            case "HYPERBOLA":
                return buildHyperbola(conic.id());
            case "DEGENERATE_CONIC":
                return new DegenerateCurve3(buildPlacement(placement3D.id()).getLocation());
            case "CONIC_CURVE":
                // Generic CONIC_CURVE: try parabola first (most common in STEP files),
                // then hyperbola if parameters don't match.
                try {
                    return buildParabola(conic.id());
                } catch (UnsupportedGeometryException e) {
                    return buildHyperbola(conic.id());
                }
            default:
                throw new UnsupportedGeometryException("surface directrix for " + conic.entityName() + " is unsupported");
        }
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
        // Parabola vertex is at origin, axis direction is yDirection (perpendicular to x)
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

    private List<CartesianPoint> sampleParabolaPoints3(Axis2Placement3D placement, List<Double> parameters) {
        if (parameters.isEmpty()) {
            throw new UnsupportedGeometryException("PARABOLA requires focal distance");
        }
        double focalDistance = parameters.get(0);
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
            points.add(placement.getLocation().add(xAxis.scale(x).add(yAxis.scale(t))));
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
            points.add(placement.getLocation().add(xAxis.scale(x).add(yAxis.scale(y))));
        }
        return List.copyOf(points);
    }

    private List<Point2> sampleParabolaPoints2(Point2 origin, Direction2 xDirection, List<Double> parameters) {
        if (parameters.isEmpty()) {
            throw new UnsupportedGeometryException("PARABOLA requires focal distance");
        }
        double focalDistance = parameters.get(0);
        if (!Double.isFinite(focalDistance) || focalDistance <= Epsilon.EPS) {
            throw new UnsupportedGeometryException("PARABOLA focal distance must be positive");
        }
        double yExtent = Math.max(1.0, focalDistance * 4.0);
        int segments = 96;
        List<Point2> points = new ArrayList<>(segments + 1);
        Vector2 xAxis = xDirection.asVector();
        Vector2 yAxis = new Vector2(-xAxis.getY(), xAxis.getX());
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
        Vector2 yAxis = new Vector2(-xAxis.getY(), xAxis.getX());
        for (int index = 0; index <= segments; index++) {
            double t = -extent + (2.0 * extent * index) / segments;
            double x = semiAxis * Math.cosh(t);
            double y = semiImaginaryAxis * Math.sinh(t);
            points.add(origin.add(xAxis.scale(x).add(yAxis.scale(y))));
        }
        return List.copyOf(points);
    }

    Plane buildSupportedPlaneGeometry(StepEntity geometry, String faceType) {
        SurfaceGeometry surface = buildSupportedFaceGeometry(geometry, faceType);
        if (surface instanceof Plane) {
            Plane plane = (Plane) surface;
            return plane;
        }
        return null;
    }

    @FunctionalInterface
    private interface FaceGeometryHandler {
        SurfaceGeometry build(StepEntity geometry, String faceType);
    }

    private record FaceGeometryRule(Class<?> type, Predicate<StepEntity> guard, FaceGeometryHandler handler) {
        boolean matches(StepEntity geometry) {
            return type.isInstance(geometry) && (guard == null || guard.test(geometry));
        }
    }

    /** Face geometry built from the STEP id, ignoring the face type. */
    private FaceGeometryRule idFaceRule(Class<?> type, Function<Integer, SurfaceGeometry> builder) {
        return new FaceGeometryRule(type, null, (geometry, faceType) -> builder.apply(geometry.id()));
    }

    /** Face geometry delegated to another referenced entity. */
    private FaceGeometryRule recurseFaceRule(Class<?> type, Function<StepEntity, StepEntity> next) {
        return new FaceGeometryRule(type, null,
                (geometry, faceType) -> buildSupportedFaceGeometry(next.apply(geometry), faceType));
    }

    /**
     * Supported-face rules keyed by concrete type, replacing the former
     * 37-branch if/else-if chain. Order mirrors the original chain (first
     * match wins). One dead duplicate branch (a second StepMachinedSurface
     * entry after the identical earlier rule) is dropped. Unmatched geometry
     * yields null, as the old trailing return did.
     */
    private final List<FaceGeometryRule> supportedFaceGeometryRules = createSupportedFaceGeometryRules();

    private List<FaceGeometryRule> createSupportedFaceGeometryRules() {
        List<FaceGeometryRule> rules = new ArrayList<>();
        rules.add(idFaceRule(StepPlane.class, this::buildPlane));
        rules.add(idFaceRule(StepCylindricalSurface.class, this::buildCylindricalSurface));
        rules.add(idFaceRule(StepConicalSurface.class, this::buildConicalSurface));
        rules.add(idFaceRule(StepSphericalSurface.class, this::buildSphericalSurface));
        rules.add(idFaceRule(StepToroidalSurface.class, this::buildToroidalSurface));
        rules.add(new FaceGeometryRule(StepToroidalSurfaceWithSpecifiedBends.class, null,
                (geometry, faceType) -> buildToroidalSurfaceFromSpecifiedBends((StepToroidalSurfaceWithSpecifiedBends) geometry)));
        rules.add(idFaceRule(StepDegenerateToroidalSurface.class, this::buildDegenerateToroidalSurface));
        rules.add(idFaceRule(StepSurfaceOfLinearExtrusion.class, this::buildSurfaceOfLinearExtrusion));
        rules.add(idFaceRule(StepSurfaceOfRevolution.class, this::buildSurfaceOfRevolution));
        rules.add(idFaceRule(StepBezierSurface.class, this::buildBezierSurface));
        rules.add(idFaceRule(StepUniformSurface.class, this::buildUniformSurface));
        rules.add(idFaceRule(StepQuasiUniformSurface.class, this::buildQuasiUniformSurface));
        rules.add(idFaceRule(StepPiecewiseBezierSurface.class, this::buildPiecewiseBezierSurface));
        rules.add(idFaceRule(StepBSplineSurfaceWithKnots.class, this::buildBSplineSurface));
        rules.add(idFaceRule(StepBSplineSurface.class, this::buildGenericBSplineSurface));
        rules.add(idFaceRule(StepRationalBSplineSurface.class, this::buildRationalBSplineSurface));
        rules.add(new FaceGeometryRule(StepRectangularTrimmedSurface.class, null, (geometry, faceType) -> {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) geometry;
            buildRectangularTrimmedSurface(trimmedSurface.id());
            return buildSupportedFaceGeometry(trimmedSurface.getBasisSurface(), faceType);
        }));
        rules.add(new FaceGeometryRule(StepCurveBoundedSurface.class, null, (geometry, faceType) -> {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) geometry;
            for (StepEntity boundary : boundedSurface.boundaries()) {
                if (boundary instanceof StepPcurve) {
                    buildPcurve2(((StepPcurve) boundary).id());
                } else if (boundary instanceof StepCompositeCurveOnSurface) {
                    StepCompositeCurveOnSurface compositeCurveOnSurface = (StepCompositeCurveOnSurface) boundary;
                    boolean built2d = true;
                    for (StepCompositeCurveSegment segment : compositeCurveOnSurface.getSegments()) {
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
            return buildSupportedFaceGeometry(boundedSurface.getBasisSurface(), faceType);
        }));
        rules.add(new FaceGeometryRule(StepOrientedSurface.class, null, (geometry, faceType) -> {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) geometry;
            SurfaceGeometry base = buildSupportedFaceGeometry(orientedSurface.surfaceElement(), faceType);
            if (base == null) {
                return null;
            }
            if (!orientedSurface.isOrientation()) {
                return reverseSurfaceSense(base);
            }
            return base;
        }));
        rules.add(new FaceGeometryRule(StepOffsetSurface.class, null,
                (geometry, faceType) -> offsetSupportedSurfaceGeometry((StepOffsetSurface) geometry, faceType)));
        rules.add(new FaceGeometryRule(StepGeometricReplica.class,
                geometry -> "SURFACE_REPLICA".equals(((StepGeometricReplica) geometry).entityName()),
                (geometry, faceType) -> {
                    StepGeometricReplica replica = (StepGeometricReplica) geometry;
                    String replicaRestriction = unsupportedReplicaSurfaceTransformation(replica.transformation());
                    if (replicaRestriction != null) {
                        return null;
                    }
                    SurfaceGeometry base = buildSupportedFaceGeometry(replica.parent(), faceType);
                    if (base == null) {
                        return null;
                    }
                    return transformSurfaceGeometry(base, replica.transformation());
                }));
        rules.add(new FaceGeometryRule(StepRuledSurface.class, null,
                (geometry, faceType) -> buildRuledSurfaceGeometry((StepRuledSurface) geometry)));
        rules.add(new FaceGeometryRule(StepSurfaceOfConstantRadius.class, null,
                (geometry, faceType) -> buildSurfaceOfConstantRadiusGeometry((StepSurfaceOfConstantRadius) geometry, faceType)));
        rules.add(new FaceGeometryRule(StepSurfacePatch.class, null,
                (geometry, faceType) -> buildSurfacePatchGeometry((StepSurfacePatch) geometry, faceType)));
        rules.add(new FaceGeometryRule(StepRectangularCompositeSurface.class, null,
                (geometry, faceType) -> buildRectangularCompositeSurfaceGeometry((StepRectangularCompositeSurface) geometry, faceType)));
        // Elliptical axis surfaces - map to standard surface types with elliptical parameters
        rules.add(idFaceRule(StepCylindricalSurfaceWithEllipticalAxis.class, this::buildCylindricalSurfaceWithEllipticalAxis));
        rules.add(idFaceRule(StepConicalSurfaceWithEllipticalAxis.class, this::buildConicalSurfaceWithEllipticalAxis));
        rules.add(idFaceRule(StepSphericalSurfaceWithEllipticalAxis.class, this::buildSphericalSurfaceWithEllipticalAxis));
        rules.add(idFaceRule(StepToroidalSurfaceWithCylindricalAxis.class, this::buildToroidalSurfaceWithCylindricalAxis));
        rules.add(idFaceRule(StepToroidalSurfaceWithEllipticalAxis.class, this::buildToroidalSurfaceWithEllipticalAxis));
        // B-spline surface with breakpoints - treat as regular B-spline surface
        rules.add(idFaceRule(StepBSplineSurfaceWithKnotsAndBreakpoints.class, this::buildBSplineSurfaceWithBreakpoints));
        // Offset surface type 2
        rules.add(new FaceGeometryRule(StepOffsetSurface2.class, null,
                (geometry, faceType) -> buildOffsetSurface2Geometry((StepOffsetSurface2) geometry, faceType)));
        // Blended surface - approximate as primary surface
        rules.add(new FaceGeometryRule(StepBlendedSurface.class, null,
                (geometry, faceType) -> buildBlendedSurface((StepBlendedSurface) geometry, faceType)));
        // Free-form surface - approximate as B-spline surface
        rules.add(new FaceGeometryRule(StepFreeFormSurface.class, null,
                (geometry, faceType) -> buildFreeFormSurface((StepFreeFormSurface) geometry)));
        // Machined surface: delegate to underlying face geometry
        rules.add(recurseFaceRule(StepMachinedSurface.class, geometry -> ((StepMachinedSurface) geometry).face()));
        // Bounded surface - marker type with no geometry data
        rules.add(new FaceGeometryRule(StepBoundedSurface.class, null, (geometry, faceType) -> {
            StepEntity actual = entitiesById.get(geometry.id());
            if (actual != null && actual != geometry) {
                return buildSupportedFaceGeometry(actual, faceType);
            }
            return null;
        }));
        // Surface abstract base type - check for complex entity syntax at same ID
        rules.add(new FaceGeometryRule(StepSurface.class, null, (geometry, faceType) -> {
            StepEntity actual = entitiesById.get(geometry.id());
            if (actual != null && actual != geometry) {
                return buildSupportedFaceGeometry(actual, faceType);
            }
            return null;
        }));
        // MAPPED_ITEM: dispatch through to mapping target for surface geometry
        rules.add(recurseFaceRule(StepMappedItem.class, geometry -> ((StepMappedItem) geometry).mappingTarget()));
        // Advanced analytical surfaces
        rules.add(idFaceRule(StepParaboloidSurface.class, this::buildParaboloidSurface));
        rules.add(idFaceRule(StepHyperboloidSurface.class, this::buildHyperboloidSurface));
        rules.add(idFaceRule(StepSurfaceOfTranslation.class, this::buildSurfaceOfTranslation));
        rules.add(idFaceRule(StepSurfaceOfProjection.class, this::buildSurfaceOfProjection));
        return List.copyOf(rules);
    }

    SurfaceGeometry buildSupportedFaceGeometry(StepEntity geometry, String faceType) {
        for (FaceGeometryRule rule : supportedFaceGeometryRules) {
            if (rule.matches(geometry)) {
                return rule.handler().build(geometry, faceType);
            }
        }
        return null;
    }

    /**
     * Callback for StepCadCurveBuilder to build SurfaceGeometry by entity ID.
     */
    private SurfaceGeometry buildSupportedFaceGeometryById(int id) {
        StepEntity entity = requireExistingEntity(id);
        return buildSupportedFaceGeometry(entity, "PCURVE");
    }

    private SurfaceGeometry buildRuledSurfaceGeometry(StepRuledSurface ruledSurface) {
        RuledSurface3 existing = ruledSurfaces.get(ruledSurface.id());
        if (existing != null) {
            return existing;
        }
        // Ruled surface is defined by two directrix curves
        Axis2Placement3D position = buildPlacement(ruledSurface.getPosition().id());
        Curve3 directrix1 = buildCurve3(ruledSurface.getDirectrix1());
        Curve3 directrix2 = buildCurve3(ruledSurface.getDirectrix2());
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
        // Surface patch is just a bounded portion of a surface
        // The sameSense flag determines orientation
        if (!patch.isSameSense()) {
            // Reverse orientation if needed
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
        // Rectangular composite surface is a bounded rectangular region of the parent surface
        // The u1, u2, v1, v2 parameters define the boundaries
        // For now, return the parent surface - proper implementation would trim to bounds
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

    // Build methods for elliptical axis and extended surface types

    public CylindricalSurface buildCylindricalSurfaceWithEllipticalAxis(int id) {
        CylindricalSurface existing = cylindricalSurfaces.get(id);
        if (existing != null) {
            return existing;
        }
        StepCylindricalSurfaceWithEllipticalAxis surface = requireEntity(id, StepCylindricalSurfaceWithEllipticalAxis.class,
                "CYLINDRICAL_SURFACE_WITH_ELLIPTICAL_AXIS");
        // Approximate elliptical cylinder as circular cylinder with average radius
        double avgRadius = (surface.getSemiAxisA() + surface.getSemiAxisB()) / 2.0;
        CylindricalSurface built = new CylindricalSurface(buildPlacement(surface.getPosition().id()), avgRadius);
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
        double avgRadius = (surface.getSemiAxisA() + surface.getSemiAxisB()) / 2.0;
        ConicalSurface built = new ConicalSurface(buildPlacement(surface.getPosition().id()), avgRadius, Math.PI / 4);
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
        SphericalSurface built = new SphericalSurface(buildPlacement(surface.getPosition().id()), surface.getRadius());
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
        Axis2Placement3D placement = buildAxis1PlacementAsAxis2(surface.getPosition().id());
        ToroidalSurface built = new ToroidalSurface(placement, surface.getMajorRadius(), surface.getMinorRadius());
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
        ToroidalSurface built = new ToroidalSurface(buildPlacement(surface.getPosition().id()), surface.getMajorRadius(), surface.getMinorRadius());
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
        List<List<CartesianPoint>> controlPoints = new ArrayList<>(surface.getControlPoints().size());
        for (List<StepCartesianPoint> row : surface.getControlPoints()) {
            List<CartesianPoint> pointRow = new ArrayList<>(row.size());
            for (StepCartesianPoint pt : row) {
                pointRow.add(buildPoint(pt.id()));
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

    private SurfaceGeometry buildOffsetSurface2Geometry(StepOffsetSurface2 offsetSurface2, String faceType) {
        SurfaceGeometry base = buildSupportedFaceGeometry(offsetSurface2.getBasisSurface(), faceType);
        if (base == null) {
            return null;
        }
        // Same logic as regular offset surface
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
        List<List<StepEntity>> controlPoints = surface.getControlPoints();
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
                if (cp instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) cp;
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
            // Recoverable degradation: surface build failed, signal and return null.
            log.warn("BSplineSurface3 construction failed; returning null", e);
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

    @FunctionalInterface
    private interface SurfaceGeometryHandler {
        void build(StepEntity geometry);
    }

    private record SurfaceGeometryRule(Class<?> type, Predicate<StepEntity> guard, SurfaceGeometryHandler handler) {
        boolean matches(StepEntity geometry) {
            return type.isInstance(geometry) && (guard == null || guard.test(geometry));
        }
    }

    /** Analytical and spline surface types built from their STEP id. */
    private SurfaceGeometryRule idRule(Class<?> type, IntConsumer builder) {
        return new SurfaceGeometryRule(type, null, geometry -> builder.accept(geometry.id()));
    }

    /**
     * Supported-surface rules keyed by concrete type, replacing the former
     * 32-branch if/else-if chain. Order mirrors the original chain (first
     * match wins). The SURFACE_REPLICA entity-name guard is kept as a rule
     * guard; extended surface types keep their dedicated geometry builders.
     */
    private final List<SurfaceGeometryRule> supportedSurfaceGeometryRules = createSupportedSurfaceGeometryRules();

    private List<SurfaceGeometryRule> createSupportedSurfaceGeometryRules() {
        List<SurfaceGeometryRule> rules = new ArrayList<>();
        rules.add(idRule(StepPlane.class, this::buildPlane));
        rules.add(idRule(StepCylindricalSurface.class, this::buildCylindricalSurface));
        rules.add(idRule(StepConicalSurface.class, this::buildConicalSurface));
        rules.add(idRule(StepSphericalSurface.class, this::buildSphericalSurface));
        rules.add(idRule(StepToroidalSurface.class, this::buildToroidalSurface));
        rules.add(new SurfaceGeometryRule(StepToroidalSurfaceWithSpecifiedBends.class, null,
                geometry -> buildToroidalSurfaceFromSpecifiedBends((StepToroidalSurfaceWithSpecifiedBends) geometry)));
        rules.add(idRule(StepDegenerateToroidalSurface.class, this::buildDegenerateToroidalSurface));
        rules.add(idRule(StepSurfaceOfLinearExtrusion.class, this::buildSurfaceOfLinearExtrusion));
        rules.add(idRule(StepSurfaceOfRevolution.class, this::buildSurfaceOfRevolution));
        rules.add(idRule(StepBezierSurface.class, this::buildBezierSurface));
        rules.add(idRule(StepUniformSurface.class, this::buildUniformSurface));
        rules.add(idRule(StepQuasiUniformSurface.class, this::buildQuasiUniformSurface));
        rules.add(idRule(StepPiecewiseBezierSurface.class, this::buildPiecewiseBezierSurface));
        rules.add(idRule(StepBSplineSurfaceWithKnots.class, this::buildBSplineSurface));
        rules.add(idRule(StepBSplineSurface.class, this::buildGenericBSplineSurface));
        rules.add(idRule(StepRationalBSplineSurface.class, this::buildRationalBSplineSurface));
        rules.add(idRule(StepRectangularTrimmedSurface.class, this::buildRectangularTrimmedSurface));
        rules.add(idRule(StepCurveBoundedSurface.class, this::buildCurveBoundedSurface));
        rules.add(idRule(StepOrientedSurface.class, this::buildOrientedSurface));
        rules.add(idRule(StepOffsetSurface.class, this::buildOffsetSurface));
        rules.add(new SurfaceGeometryRule(StepGeometricReplica.class,
                geometry -> "SURFACE_REPLICA".equals(((StepGeometricReplica) geometry).entityName()),
                geometry -> buildSurfaceReplica(geometry.id())));
        // Extended surface types handled in face geometry path
        rules.add(new SurfaceGeometryRule(StepRuledSurface.class, null,
                geometry -> buildRuledSurfaceGeometry((StepRuledSurface) geometry)));
        rules.add(new SurfaceGeometryRule(StepSurfaceOfConstantRadius.class, null,
                geometry -> buildSurfaceOfConstantRadiusGeometry((StepSurfaceOfConstantRadius) geometry, "SURFACE")));
        rules.add(new SurfaceGeometryRule(StepSurfacePatch.class, null,
                geometry -> buildSurfacePatchGeometry((StepSurfacePatch) geometry, "SURFACE")));
        rules.add(new SurfaceGeometryRule(StepRectangularCompositeSurface.class, null,
                geometry -> buildRectangularCompositeSurfaceGeometry((StepRectangularCompositeSurface) geometry, "SURFACE")));
        rules.add(idRule(StepCylindricalSurfaceWithEllipticalAxis.class, this::buildCylindricalSurfaceWithEllipticalAxis));
        rules.add(idRule(StepConicalSurfaceWithEllipticalAxis.class, this::buildConicalSurfaceWithEllipticalAxis));
        rules.add(idRule(StepSphericalSurfaceWithEllipticalAxis.class, this::buildSphericalSurfaceWithEllipticalAxis));
        rules.add(idRule(StepToroidalSurfaceWithCylindricalAxis.class, this::buildToroidalSurfaceWithCylindricalAxis));
        rules.add(idRule(StepToroidalSurfaceWithEllipticalAxis.class, this::buildToroidalSurfaceWithEllipticalAxis));
        rules.add(idRule(StepBSplineSurfaceWithKnotsAndBreakpoints.class, this::buildBSplineSurfaceWithBreakpoints));
        rules.add(new SurfaceGeometryRule(StepOffsetSurface2.class, null,
                geometry -> buildOffsetSurface2Geometry((StepOffsetSurface2) geometry, "SURFACE")));
        rules.add(new SurfaceGeometryRule(StepBlendedSurface.class, null,
                geometry -> buildBlendedSurface((StepBlendedSurface) geometry, "SURFACE")));
        rules.add(new SurfaceGeometryRule(StepFreeFormSurface.class, null,
                geometry -> buildFreeFormSurface((StepFreeFormSurface) geometry)));
        rules.add(new SurfaceGeometryRule(StepBoundedSurface.class, null, geometry -> {
            StepEntity actual = entitiesById.get(geometry.id());
            if (actual != null && actual != geometry) {
                buildSupportedSurfaceGeometry(actual);
            }
            // BoundedSurface with no underlying geometry - silently skip
        }));
        // Advanced analytical surfaces
        rules.add(idRule(StepParaboloidSurface.class, this::buildParaboloidSurface));
        rules.add(idRule(StepHyperboloidSurface.class, this::buildHyperboloidSurface));
        rules.add(idRule(StepSurfaceOfTranslation.class, this::buildSurfaceOfTranslation));
        rules.add(idRule(StepSurfaceOfProjection.class, this::buildSurfaceOfProjection));
        return List.copyOf(rules);
    }

    private void buildSupportedSurfaceGeometry(StepEntity geometry) {
        for (SurfaceGeometryRule rule : supportedSurfaceGeometryRules) {
            if (rule.matches(geometry)) {
                rule.handler().build(geometry);
                return;
            }
        }
        throw new UnsupportedGeometryException("surface geometry " + stepEntityTypeName(geometry) + " is unsupported");
    }

    private void buildSurfaceBoundaryCurve(StepEntity boundary) {
        if (boundary instanceof StepPcurve) {
            StepPcurve pcurve = (StepPcurve) boundary;
            buildPcurve2(pcurve.id());
            return;
        }
        if (boundary instanceof StepCompositeCurveOnSurface) {
            StepCompositeCurveOnSurface compositeCurveOnSurface = (StepCompositeCurveOnSurface) boundary;
            boolean built2d = true;
            for (StepCompositeCurveSegment segment : compositeCurveOnSurface.getSegments()) {
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
        if (geometry instanceof StepCylindricalSurface) {
            StepCylindricalSurface cylindricalSurface = (StepCylindricalSurface) geometry;
            buildCylindricalSurface(cylindricalSurface.id());
            return "CYLINDRICAL_SURFACE";
        }
        if (geometry instanceof StepConicalSurface) {
            StepConicalSurface conicalSurface = (StepConicalSurface) geometry;
            buildConicalSurface(conicalSurface.id());
            return "CONICAL_SURFACE";
        }
        if (geometry instanceof StepSphericalSurface) {
            StepSphericalSurface sphericalSurface = (StepSphericalSurface) geometry;
            buildSphericalSurface(sphericalSurface.id());
            return "SPHERICAL_SURFACE";
        }
        if (geometry instanceof StepSurfaceOfLinearExtrusion) {
            StepSurfaceOfLinearExtrusion extrusionSurface = (StepSurfaceOfLinearExtrusion) geometry;
            buildSurfaceOfLinearExtrusion(extrusionSurface.id());
            return "SURFACE_OF_LINEAR_EXTRUSION";
        }
        if (geometry instanceof StepSurfaceOfRevolution) {
            StepSurfaceOfRevolution revolutionSurface = (StepSurfaceOfRevolution) geometry;
            buildSurfaceOfRevolution(revolutionSurface.id());
            return "SURFACE_OF_REVOLUTION";
        }
        if (geometry instanceof StepBezierSurface) {
            StepBezierSurface splineSurface = (StepBezierSurface) geometry;
            buildBezierSurface(splineSurface.id());
            return "BEZIER_SURFACE";
        }
        if (geometry instanceof StepUniformSurface) {
            StepUniformSurface splineSurface = (StepUniformSurface) geometry;
            buildUniformSurface(splineSurface.id());
            return "UNIFORM_SURFACE";
        }
        if (geometry instanceof StepQuasiUniformSurface) {
            StepQuasiUniformSurface splineSurface = (StepQuasiUniformSurface) geometry;
            buildQuasiUniformSurface(splineSurface.id());
            return "QUASI_UNIFORM_SURFACE";
        }
        if (geometry instanceof StepPiecewiseBezierSurface) {
            StepPiecewiseBezierSurface splineSurface = (StepPiecewiseBezierSurface) geometry;
            buildPiecewiseBezierSurface(splineSurface.id());
            return "PIECEWISE_BEZIER_SURFACE";
        }
        if (geometry instanceof StepBSplineSurfaceWithKnots) {
            StepBSplineSurfaceWithKnots splineSurface = (StepBSplineSurfaceWithKnots) geometry;
            buildBSplineSurface(splineSurface.id());
            return "B_SPLINE_SURFACE_WITH_KNOTS";
        }
        if (geometry instanceof StepRationalBSplineSurface) {
            StepRationalBSplineSurface rationalSplineSurface = (StepRationalBSplineSurface) geometry;
            buildRationalBSplineSurface(rationalSplineSurface.id());
            return "RATIONAL_B_SPLINE_SURFACE";
        }
        if (geometry instanceof StepToroidalSurface) {
            StepToroidalSurface toroidalSurface = (StepToroidalSurface) geometry;
            buildToroidalSurface(toroidalSurface.id());
            return "TOROIDAL_SURFACE";
        }
        if (geometry instanceof StepToroidalSurfaceWithSpecifiedBends) {
            StepToroidalSurfaceWithSpecifiedBends toroidalSpecBends = (StepToroidalSurfaceWithSpecifiedBends) geometry;
            buildToroidalSurfaceFromSpecifiedBends(toroidalSpecBends);
            return "TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS";
        }
        if (geometry instanceof StepDegenerateToroidalSurface) {
            StepDegenerateToroidalSurface degenerateToroidalSurface = (StepDegenerateToroidalSurface) geometry;
            buildDegenerateToroidalSurface(degenerateToroidalSurface.id());
            return "DEGENERATE_TOROIDAL_SURFACE";
        }
        if (geometry instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) geometry;
            buildRectangularTrimmedSurface(trimmedSurface.id());
            return describeUnsupportedFaceGeometry(trimmedSurface.getBasisSurface());
        }
        if (geometry instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) geometry;
            buildCurveBoundedSurface(boundedSurface.id());
            return describeUnsupportedFaceGeometry(boundedSurface.getBasisSurface());
        }
        if (geometry instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) geometry;
            buildOrientedSurface(orientedSurface.id());
            return describeUnsupportedFaceGeometry(orientedSurface.surfaceElement());
        }
        if (geometry instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) geometry;
            buildOffsetSurface(offsetSurface.id());
            return describeUnsupportedFaceGeometry(offsetSurface.getBasisSurface());
        }
        if (geometry instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) geometry).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) geometry;
            buildSurfaceReplica(replica.id());
            return describeUnsupportedFaceGeometry(replica.parent());
        }
        return null;
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


    List<CartesianPoint> closeLoop3(List<CartesianPoint> points) {
        return geometryOps.closeLoop3(points);
    }

    List<CartesianPoint> reverseClosedLoop3(List<CartesianPoint> points) {
        return geometryOps.reverseClosedLoop3(points);
    }

    List<CartesianPoint> sampleCurve3(Curve3 curve, int segments) {
        return geometryOps.sampleCurve3(curve, segments);
    }

    private Curve3 transformCurve3(Curve3 curve, StepCartesianTransformationOperator transformation) {
        return geometryOps.transformCurve3(curve, transformation);
    }

    private Curve2 transformCurve2(Curve2 curve, StepCartesianTransformationOperator transformation) {
        return geometryOps.transformCurve2(curve, transformation);
    }

    static String stepEntityTypeName(StepEntity entity) {
        return StepEntityNamingUtils.stepEntityTypeName(entity);
    }

    StepEntity resolvedEntity(int id) {
        return entitiesById.get(id);
    }

    private static String camelToUpperSnake(String value) {
        return StepEntityNamingUtils.camelToUpperSnake(value);
    }

    private static String loopTypeName(Loop loop) {
        return StepEntityNamingUtils.loopTypeName(loop);
    }

    private static String curveTypeName(Curve3 curve) {
        return StepEntityNamingUtils.curveTypeName(curve);
    }

    private static String curveTypeName(Curve2 curve) {
        return StepEntityNamingUtils.curveTypeName(curve);
    }

    private Plane transformPlane(Plane plane, StepCartesianTransformationOperator transformation) {
        return new Plane(
                transformPoint3(plane.getOrigin(), transformation),
                transformDirection3(plane.getNormal(), transformation));
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

    /**
     * Builds a paraboloid surface of revolution.
     */
    public ParaboloidSurface buildParaboloidSurface(int id) {
        ParaboloidSurface existing = paraboloidSurfaces.get(id);
        if (existing != null) return existing;
        StepParaboloidSurface step = requireEntity(id, StepParaboloidSurface.class, "PARABOLOID_SURFACE");
        ParaboloidSurface built = new ParaboloidSurface(buildPlacement(step.getPosition().id()), step.getFocalLength());
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
        HyperboloidSurface built = new HyperboloidSurface(buildPlacement(step.getPosition().id()), step.getRadius(), step.getSemiAxis());
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
        Curve3 profile = buildCurve3(step.getProfile());
        Vector3 direction = buildVector3(step.getDirection());
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
        Curve3 profile = buildCurve3(step.getProfile());
        Vector3 direction = buildVector3(step.getProjectionDirection());
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
        if (entity instanceof StepParaboloidSurface) {
            return buildParaboloidSurface(id);
        }
        if (entity instanceof StepHyperboloidSurface) {
            return buildHyperboloidSurface(id);
        }
        if (entity instanceof StepSurfaceOfTranslation) {
            return buildSurfaceOfTranslation(id);
        }
        if (entity instanceof StepSurfaceOfProjection) {
            return buildSurfaceOfProjection(id);
        }
        throw new UnsupportedGeometryException("entity #" + id + " is not a supported parametric surface");
    }

    private Vector3 buildVector3(StepEntity entity) {
        if (entity instanceof StepVector) {
            StepVector stepVector = (StepVector) entity;
            Direction3 dir = buildDirection(stepVector.isOrientation().id());
            double mag = stepVector.magnitude();
            return dir.asVector().scale(mag);
        }
        if (entity instanceof StepDirection) {
            StepDirection stepDir = (StepDirection) entity;
            return buildDirection(stepDir.id()).asVector();
        }
        throw new StepResolutionException("entity is not a supported vector or direction");
    }
}
