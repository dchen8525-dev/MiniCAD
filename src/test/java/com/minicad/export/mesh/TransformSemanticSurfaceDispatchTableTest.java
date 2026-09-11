package com.minicad.export.mesh;

import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.DegenerateCurve3;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.ParaboloidSurface;
import com.minicad.geometry.Plane;
import com.minicad.geometry.SphericalSurface;
import com.minicad.geometry.SurfaceGeometry;
import com.minicad.geometry.SurfaceOfLinearExtrusion3;
import com.minicad.geometry.SurfaceOfRevolution3;
import com.minicad.geometry.ToroidalSurface;
import com.minicad.geometry.Vector3;
import com.minicad.step.model.StepCartesianTransformationOperator;
import com.minicad.step.model.StepEntity;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for
 * StepMeshExporter.transformSemanticSurfaceGeometry, and exercises every rule
 * at runtime.
 *
 * transformSemanticSurfaceGeometry transformed each concrete analytic surface
 * type through a STEP Cartesian transformation operator (placements and points
 * translated/scaled, radii scaled, swept curves recursed through
 * transformSemanticCurve3 with null propagation) via a sequential if/else-if
 * chain, with a trailing `return null` the callers treat as "unsupported". It
 * is now an ordered list of (type, handler) rules.
 *
 * The table and entry method are private, so this test reaches them through
 * reflection -- the same convention as TransformSemanticCurve3DispatchTableTest
 * in this package. The frozen file pins the type order captured from the
 * original chain: instanceof matches subtypes and the first match wins, so a
 * dropped, duplicated or reordered rule silently changes which surface is
 * transformed how. All 7 types are final classes implementing SurfaceGeometry
 * directly (no subtype relation today), so the order is currently behaviour
 * neutral.
 *
 * Fixtures: operator T3 (#5) has origin (10,0,0), axes X/Y/Z and scale 2.0, so
 * a point (x,y,z) maps to (10+2x, 2y, 2z), a direction maps without scale and
 * a vector scales by 2. The scale the entry method hoists is
 * Math.abs(transformationScale) = 2.0.
 */
class TransformSemanticSurfaceDispatchTableTest {

    private static final String TABLE_FIELD = "TRANSFORM_SEMANTIC_SURFACE_RULES";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/transform-semantic-surface-dispatch-order.txt");

    private static final String STEP =
            "DATA;\n"
            + "#1=CARTESIAN_POINT('O',(10.0,0.0,0.0));\n"
            + "#2=DIRECTION('AX',(1.0,0.0,0.0));\n"
            + "#3=DIRECTION('AY',(0.0,1.0,0.0));\n"
            + "#4=DIRECTION('AZ',(0.0,0.0,1.0));\n"
            + "#5=CARTESIAN_TRANSFORMATION_OPERATOR_3D('T3',#2,#3,#1,2.0,#4);\n"
            + "ENDSEC;\n";

    private static StepCadBuilder builder;
    private static StepCartesianTransformationOperator t3;

    @BeforeAll
    static void setUp() {
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(STEP));
        builder = StepCadBuilder.fromResolved(resolved);
        t3 = (StepCartesianTransformationOperator) resolved.get(5);
    }

    private static SurfaceGeometry transform(SurfaceGeometry surface) throws Exception {
        Method method = StepMeshExporter.class.getDeclaredMethod(
                "transformSemanticSurfaceGeometry", SurfaceGeometry.class,
                StepCartesianTransformationOperator.class, StepCadBuilder.class);
        method.setAccessible(true);
        return (SurfaceGeometry) method.invoke(null, surface, t3, builder);
    }

    private static void assertPoint(CartesianPoint point, double x, double y, double z) {
        assertEquals(x, point.getX(), 1.0e-9);
        assertEquals(y, point.getY(), 1.0e-9);
        assertEquals(z, point.getZ(), 1.0e-9);
    }

    private static Axis2Placement3D placementAtOrigin() {
        return new Axis2Placement3D(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1), new Direction3(1, 0, 0));
    }

    private static Line3 sweptLine() {
        return new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0), 1.0);
    }

    // ─── guard: table order and wiring ───────────────────────────────────

    @Test
    @DisplayName("transformSemanticSurfaceGeometry dispatch table keeps the original branch order")
    void tableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveRuleTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which surface is transformed.");
    }

    @Test
    @DisplayName("transformSemanticSurfaceGeometry dispatch table has no duplicate types")
    void tableShouldHaveNoDuplicateTypes() throws Exception {
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : liveRuleTypes()) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in TRANSFORM_SEMANTIC_SURFACE_RULES: later entries are "
                        + "unreachable, because the first match returns.");
    }

    // ─── behaviour: one test per rule, plus fallback and null handling ───

    @Test
    @DisplayName("Plane transforms origin and rotates normal without scaling")
    void plane() throws Exception {
        Plane plane = new Plane(new CartesianPoint(1, 2, 3), new Direction3(0, 0, 1));
        Plane transformed = assertInstanceOf(Plane.class, transform(plane));
        assertPoint(transformed.origin(), 12.0, 4.0, 6.0);
        assertEquals(1.0, transformed.normal().z(), 1.0e-9);
        assertEquals(0.0, transformed.normal().x(), 1.0e-9);
    }

    @Test
    @DisplayName("CylindricalSurface transforms placement and scales radius")
    void cylindricalSurface() throws Exception {
        CylindricalSurface cylinder = new CylindricalSurface(placementAtOrigin(), 3.0);
        CylindricalSurface transformed = assertInstanceOf(CylindricalSurface.class, transform(cylinder));
        assertEquals(6.0, transformed.radius(), 1.0e-9);
        assertPoint(transformed.position().location(), 10.0, 0.0, 0.0);
    }

    @Test
    @DisplayName("ConicalSurface scales radius but keeps semi-angle")
    void conicalSurface() throws Exception {
        ConicalSurface cone = new ConicalSurface(placementAtOrigin(), 3.0, 0.5);
        ConicalSurface transformed = assertInstanceOf(ConicalSurface.class, transform(cone));
        assertEquals(6.0, transformed.radius(), 1.0e-9);
        assertEquals(0.5, transformed.semiAngle(), 1.0e-9);
        assertPoint(transformed.position().location(), 10.0, 0.0, 0.0);
    }

    @Test
    @DisplayName("SphericalSurface transforms placement and scales radius")
    void sphericalSurface() throws Exception {
        SphericalSurface sphere = new SphericalSurface(placementAtOrigin(), 3.0);
        SphericalSurface transformed = assertInstanceOf(SphericalSurface.class, transform(sphere));
        assertEquals(6.0, transformed.radius(), 1.0e-9);
        assertPoint(transformed.position().location(), 10.0, 0.0, 0.0);
    }

    @Test
    @DisplayName("ToroidalSurface scales both radii")
    void toroidalSurface() throws Exception {
        ToroidalSurface torus = new ToroidalSurface(placementAtOrigin(), 5.0, 1.5);
        ToroidalSurface transformed = assertInstanceOf(ToroidalSurface.class, transform(torus));
        assertEquals(10.0, transformed.majorRadius(), 1.0e-9);
        assertEquals(3.0, transformed.minorRadius(), 1.0e-9);
        assertPoint(transformed.position().location(), 10.0, 0.0, 0.0);
    }

    @Test
    @DisplayName("SurfaceOfRevolution3 transforms swept curve and axis")
    void surfaceOfRevolution3() throws Exception {
        SurfaceOfRevolution3 revolution = new SurfaceOfRevolution3(
                sweptLine(), new CartesianPoint(1, 1, 1), new Direction3(0, 0, 1));
        SurfaceOfRevolution3 transformed = assertInstanceOf(SurfaceOfRevolution3.class, transform(revolution));
        assertPoint(((Line3) transformed.sweptCurve()).origin(), 10.0, 0.0, 0.0);
        assertPoint(transformed.axisOrigin(), 12.0, 2.0, 2.0);
        assertEquals(1.0, transformed.axisDirection().z(), 1.0e-9);
    }

    @Test
    @DisplayName("SurfaceOfLinearExtrusion3 transforms swept curve and scales vector")
    void surfaceOfLinearExtrusion3() throws Exception {
        SurfaceOfLinearExtrusion3 extrusion = new SurfaceOfLinearExtrusion3(
                sweptLine(), new Vector3(1, 0, 0));
        SurfaceOfLinearExtrusion3 transformed = assertInstanceOf(SurfaceOfLinearExtrusion3.class, transform(extrusion));
        assertPoint(((Line3) transformed.sweptCurve()).origin(), 10.0, 0.0, 0.0);
        assertEquals(2.0, transformed.extrusionVector().x(), 1.0e-9);
        assertEquals(0.0, transformed.extrusionVector().y(), 1.0e-9);
    }

    @Test
    @DisplayName("SurfaceOfRevolution3 propagates null from an unsupported swept curve")
    void revolutionPropagatesNull() throws Exception {
        SurfaceOfRevolution3 revolution = new SurfaceOfRevolution3(
                new DegenerateCurve3(new CartesianPoint(1, 1, 1)),
                new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));
        assertNull(transform(revolution));
    }

    @Test
    @DisplayName("SurfaceOfLinearExtrusion3 propagates null from an unsupported swept curve")
    void extrusionPropagatesNull() throws Exception {
        SurfaceOfLinearExtrusion3 extrusion = new SurfaceOfLinearExtrusion3(
                new DegenerateCurve3(new CartesianPoint(1, 1, 1)), new Vector3(0, 0, 1));
        assertNull(transform(extrusion));
    }

    @Test
    @DisplayName("a surface type with no rule transforms to null")
    void unsupportedTypeReturnsNull() throws Exception {
        assertNull(transform(new ParaboloidSurface(placementAtOrigin(), 2.0)));
    }

    @Test
    @DisplayName("a null surface returns null without touching the table")
    void nullSurfaceReturnsNull() throws Exception {
        assertNull(transform(null));
    }

    // ─── reflection helpers ──────────────────────────────────────────────

    private static List<String> frozenTypes() throws IOException {
        if (!Files.exists(FROZEN_ORDER)) {
            fail("Missing frozen dispatch order at " + FROZEN_ORDER.toAbsolutePath());
        }
        List<String> types = new ArrayList<>();
        for (String line : Files.readAllLines(FROZEN_ORDER, StandardCharsets.UTF_8)) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                types.add(trimmed);
            }
        }
        return types;
    }

    private static List<String> liveRuleTypes() throws Exception {
        Field field = StepMeshExporter.class.getDeclaredField(TABLE_FIELD);
        field.setAccessible(true);
        List<?> rules = (List<?>) field.get(null);

        List<String> types = new ArrayList<>();
        for (Object rule : rules) {
            Method accessor = rule.getClass().getDeclaredMethod("type");
            accessor.setAccessible(true);
            types.add(((Class<?>) accessor.invoke(rule)).getSimpleName());
        }
        return types;
    }
}
