package com.minicad.export.json;

import com.minicad.preview.payload.FaceSurfacePayload;
import com.minicad.preview.payload.UvBounds;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for
 * StepFacePayloadBuilder.faceSurfacePayload, and exercises every rule at
 * runtime.
 *
 * faceSurfacePayload turned each concrete STEP surface entity into the
 * parametric FaceSurfacePayload the preview JSON exports (plane, cylinder,
 * cone, sphere, torus, degenerate torus, linear extrusion, surface of
 * revolution, rational b-spline surface, and the compound b-spline family
 * rule) via a sequential if/else-if chain, with a trailing `return null` for
 * unsupported surfaces. It is now an ordered list of (type, predicate,
 * handler) rules dispatched first-match-wins.
 *
 * The table and entry method are private, so this test reaches them through
 * reflection -- the same convention as TransformSemanticCurve3DispatchTableTest
 * in export.mesh. The frozen file pins the primary-type order captured from
 * the original chain: instanceof also matches subtypes and the first match
 * wins, so a dropped, duplicated or reordered rule silently changes which
 * surface is payloaded how. The single-type primaries are final classes
 * implementing StepEntity directly (no subtype relation today); the last rule
 * is a compound OR whose predicate mirrors the original `||` chain verbatim
 * (it does NOT test StepBSplineSurface, unlike PreviewMeshExporter's copy).
 *
 * Fixtures: one resolved document holding every surface kind. UvBounds
 * (minU=0, minV=2, maxU=1, maxV=3) makes the uv slot mapping observable:
 * the plane rule maps U to lowerHeight/upperHeight, every other rule maps
 * V to lowerHeight/upperHeight and U to startAngle/sweepAngle(uSpan).
 */
class FaceSurfacePayloadDispatchTableTest {

    private static final String TABLE_FIELD = "FACE_SURFACE_PAYLOAD_RULES";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/face-surface-payload-dispatch-order.txt");

    private static final String HOST_SOURCE =
            "src/main/java/com/minicad/export/json/StepFacePayloadBuilder.java";

    private static final String STEP =
            "DATA;\n"
            + "#1=CARTESIAN_POINT('O',(0.0,0.0,0.0));\n"
            + "#2=DIRECTION('DZ',(0.0,0.0,1.0));\n"
            + "#3=DIRECTION('DX',(1.0,0.0,0.0));\n"
            + "#4=AXIS2_PLACEMENT_3D('AX',#1,#2,#3);\n"
            + "#5=PLANE('PL0',#4);\n"
            + "#6=CYLINDRICAL_SURFACE('CY0',#4,2.0);\n"
            + "#7=CONICAL_SURFACE('CO0',#4,2.0,0.5);\n"
            + "#8=SPHERICAL_SURFACE('SPH0',#4,2.0);\n"
            + "#9=TOROIDAL_SURFACE('TO0',#4,5.0,1.0);\n"
            + "#10=DEGENERATE_TOROIDAL_SURFACE('DTS0',#4,5.0,1.0,.T.);\n"
            + "#11=VECTOR('VZ',#2,1.0);\n"
            + "#12=LINE('GEN',#1,#11);\n"
            + "#13=SURFACE_OF_LINEAR_EXTRUSION('SLE0',#12,#11);\n"
            + "#14=AXIS1_PLACEMENT('A1',#1,#2);\n"
            + "#15=SURFACE_OF_REVOLUTION('SOR0',#12,#14);\n"
            + "#16=CARTESIAN_POINT('P10',(1.0,0.0,0.0));\n"
            + "#17=CARTESIAN_POINT('P01',(0.0,1.0,0.0));\n"
            + "#18=CARTESIAN_POINT('P11',(1.0,1.0,1.0));\n"
            + "#19=(B_SPLINE_SURFACE(1,1,((#1,#17),(#16,#18)),.UNSPECIFIED.,.F.,.F.,.F.)\n"
            + "     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.)\n"
            + "     RATIONAL_B_SPLINE_SURFACE(((1.0,1.0),(1.0,0.5))));\n"
            + "#20=(B_SPLINE_SURFACE(1,1,((#1,#17),(#16,#18)),.UNSPECIFIED.,.F.,.F.,.F.)\n"
            + "     B_SPLINE_SURFACE_WITH_KNOTS((2,2),(2,2),(0.0,1.0),(0.0,1.0),.UNSPECIFIED.));\n"
            + "#21=(BEZIER_SURFACE() B_SPLINE_SURFACE(1,1,((#1,#17),(#16,#18)),.UNSPECIFIED.,.F.,.F.,.F.)\n"
            + "     BOUNDED_SURFACE() SURFACE() GEOMETRIC_REPRESENTATION_ITEM() REPRESENTATION_ITEM('BZS0'));\n"
            + "#22=RECTANGULAR_TRIMMED_SURFACE('RTS0',#6,0.0,1.0,0.0,1.0,.T.,.T.);\n"
            + "#23=CARTESIAN_POINT('PT',(0.0,0.0,0.0));\n"
            + "ENDSEC;\n";

    private static StepCadBuilder builder;
    private static Map<Integer, StepEntity> resolved;

    @BeforeAll
    static void setUp() {
        resolved = StepEntityResolver.resolveAll(StepParser.parse(STEP));
        builder = StepCadBuilder.fromResolved(resolved);
    }

    private static FaceSurfacePayload payload(int id) throws Exception {
        Method method = StepFacePayloadBuilder.class.getDeclaredMethod(
                "faceSurfacePayload", StepEntity.class, UvBounds.class, StepCadBuilder.class);
        method.setAccessible(true);
        return (FaceSurfacePayload) method.invoke(null, resolved.get(id), new UvBounds(0.0, 2.0, 1.0, 3.0), builder);
    }

    // ─── guard: table order, wiring, and no chain creep-back ─────────────

    @Test
    @DisplayName("faceSurfacePayload dispatch table keeps the original branch order")
    void tableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveRuleTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which surface is payloaded how.");
    }

    @Test
    @DisplayName("faceSurfacePayload dispatch table has no duplicate primary types")
    void tableShouldHaveNoDuplicateTypes() throws Exception {
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : liveRuleTypes()) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate primary types in FACE_SURFACE_PAYLOAD_RULES: later entries are "
                        + "unreachable, because the first match returns.");
    }

    /**
     * The entry method must dispatch through the rule table, not grow
     * instanceof branches back: a chain next to the table would be a second,
     * silently diverging copy of the same dispatch.
     */
    @Test
    @DisplayName("faceSurfacePayload dispatches through the table, not instanceof branches")
    void entryMethodShouldNotContainInstanceofBranches() throws Exception {
        String text = Files.readString(Paths.get(HOST_SOURCE), StandardCharsets.UTF_8);
        String signature = "private static FaceSurfacePayload faceSurfacePayload(";
        int signatureStart = text.indexOf(signature);
        if (signatureStart < 0) {
            fail("Cannot find method " + signature + " in " + HOST_SOURCE);
        }
        int open = text.indexOf('{', signatureStart);
        int depth = 0;
        for (int i = open; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    String body = text.substring(open, i + 1);
                    assertEquals(false, body.contains("instanceof"),
                            "faceSurfacePayload still contains an instanceof branch; "
                                    + "dispatch must go through FACE_SURFACE_PAYLOAD_RULES.");
                    return;
                }
            }
        }
        fail("Unterminated faceSurfacePayload body in " + HOST_SOURCE);
    }

    // ─── behaviour: one test per rule, plus wrapper unwrap and fallback ──

    @Test
    @DisplayName("StepPlane maps U to lower/upper height and V to start/sweep angle")
    void plane() throws Exception {
        FaceSurfacePayload payload = payload(5);
        assertEquals("plane_face", payload.type());
        assertEquals(List.of(0.0, 0.0, 0.0), payload.center());
        assertEquals(List.of(0.0, 0.0, 1.0), payload.axis());
        assertEquals(List.of(1.0, 0.0, 0.0), payload.xDirection());
        assertEquals(0.0, payload.lowerHeight(), 1.0e-9);
        assertEquals(1.0, payload.upperHeight(), 1.0e-9);
        assertEquals(2.0, payload.startAngle(), 1.0e-9);
        assertEquals(3.0, payload.sweepAngle(), 1.0e-9);
        assertEquals("PLANE", payload.sourceType());
        assertEquals(5, payload.sourceStepId());
    }

    @Test
    @DisplayName("StepCylindricalSurface maps V to lower/upper height and U span to sweep")
    void cylindricalSurface() throws Exception {
        FaceSurfacePayload payload = payload(6);
        assertEquals("cylindrical_strip", payload.type());
        assertEquals(2.0, payload.radius(), 1.0e-9);
        assertEquals(2.0, payload.lowerHeight(), 1.0e-9);
        assertEquals(3.0, payload.upperHeight(), 1.0e-9);
        assertEquals(0.0, payload.startAngle(), 1.0e-9);
        assertEquals(1.0, payload.sweepAngle(), 1.0e-9);
        assertEquals("CYLINDRICAL_SURFACE", payload.sourceType());
    }

    @Test
    @DisplayName("StepConicalSurface keeps radius and semi-angle")
    void conicalSurface() throws Exception {
        FaceSurfacePayload payload = payload(7);
        assertEquals("conical_strip", payload.type());
        assertEquals(2.0, payload.radius(), 1.0e-9);
        assertEquals(0.5, payload.semiAngle(), 1.0e-9);
    }

    @Test
    @DisplayName("StepSphericalSurface keeps radius")
    void sphericalSurface() throws Exception {
        FaceSurfacePayload payload = payload(8);
        assertEquals("spherical_surface", payload.type());
        assertEquals(2.0, payload.radius(), 1.0e-9);
    }

    @Test
    @DisplayName("StepToroidalSurface maps major to radius, minor to minorRadius")
    void toroidalSurface() throws Exception {
        FaceSurfacePayload payload = payload(9);
        assertEquals("toroidal_strip", payload.type());
        assertEquals(5.0, payload.radius(), 1.0e-9);
        assertEquals(1.0, payload.minorRadius(), 1.0e-9);
    }

    @Test
    @DisplayName("StepDegenerateToroidalSurface produces the same toroidal_strip payload")
    void degenerateToroidalSurface() throws Exception {
        FaceSurfacePayload payload = payload(10);
        assertEquals("toroidal_strip", payload.type());
        assertEquals(5.0, payload.radius(), 1.0e-9);
        assertEquals(1.0, payload.minorRadius(), 1.0e-9);
        assertEquals("DEGENERATE_TOROIDAL_SURFACE", payload.sourceType());
    }

    @Test
    @DisplayName("StepSurfaceOfLinearExtrusion carries only the normalized extrusion axis")
    void surfaceOfLinearExtrusion() throws Exception {
        FaceSurfacePayload payload = payload(13);
        assertEquals("surface_of_linear_extrusion", payload.type());
        assertNull(payload.center());
        assertEquals(List.of(0.0, 0.0, 1.0), payload.axis());
        assertNull(payload.xDirection());
    }

    @Test
    @DisplayName("StepSurfaceOfRevolution carries the axis origin and direction")
    void surfaceOfRevolution() throws Exception {
        FaceSurfacePayload payload = payload(15);
        assertEquals("surface_of_revolution", payload.type());
        assertEquals(List.of(0.0, 0.0, 0.0), payload.center());
        assertEquals(List.of(0.0, 0.0, 1.0), payload.axis());
    }

    @Test
    @DisplayName("StepRationalBSplineSurface carries degrees, knots and control points")
    void rationalBsplineSurface() throws Exception {
        FaceSurfacePayload payload = payload(19);
        assertEquals("rational_bspline_surface", payload.type());
        assertEquals(1, payload.uDegree());
        assertEquals(1, payload.vDegree());
        assertEquals(List.of(0.0, 1.0), payload.uKnots());
        assertEquals(2, payload.controlPoints().size());
        assertEquals(2, payload.controlPoints().get(0).size());
        assertEquals(List.of(0.0, 0.0, 0.0), payload.controlPoints().get(0).get(0));
    }

    @Test
    @DisplayName("StepBSplineSurfaceWithKnots hits the compound b-spline rule")
    void bsplineSurfaceWithKnots() throws Exception {
        FaceSurfacePayload payload = payload(20);
        assertEquals("bspline_surface", payload.type());
        assertEquals(1, payload.uDegree().intValue());
    }

    @Test
    @DisplayName("StepBezierSurface reaches the compound rule through its OR predicate")
    void bezierSurfaceViaCompoundRule() throws Exception {
        FaceSurfacePayload payload = payload(21);
        assertEquals("bspline_surface", payload.type());
        assertEquals("BEZIER_SURFACE", payload.sourceType());
    }

    @Test
    @DisplayName("a trimmed wrapper unwraps to the cylinder rule and keeps basis metadata")
    void trimmedWrapperUnwrapsAndRecordsBasis() throws Exception {
        FaceSurfacePayload payload = payload(22);
        assertEquals("cylindrical_strip", payload.type());
        assertEquals("RECTANGULAR_TRIMMED_SURFACE", payload.sourceType());
        assertEquals(22, payload.sourceStepId());
        assertEquals("CYLINDRICAL_SURFACE", payload.basisType());
        assertEquals(6, payload.basisStepId());
        assertEquals(0.0, payload.trimU1(), 1.0e-9);
        assertEquals(1.0, payload.trimU2(), 1.0e-9);
        assertEquals(0.0, payload.trimV1(), 1.0e-9);
        assertEquals(1.0, payload.trimV2(), 1.0e-9);
    }

    @Test
    @DisplayName("a surface with no rule payloads to null")
    void unsupportedTypeReturnsNull() throws Exception {
        assertNull(payload(23));
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
        Field field = StepFacePayloadBuilder.class.getDeclaredField(TABLE_FIELD);
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
