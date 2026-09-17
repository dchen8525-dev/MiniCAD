package com.minicad.step.semantic;

import com.minicad.common.UnsupportedGeometryException;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Vector3;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the convergence of the {@code StepCad*} shell helpers onto
 * {@link StepCadShellGeometry}.
 *
 * <p>Three different things happened in that pass and each needs a different
 * guard:</p>
 *
 * <ul>
 *   <li>StepCadBooleanBuilder and StepCadSweptBuilder still expose the same
 *       private entry points, but their bodies are now one-line delegations.
 *       Both halves matter: the delegation must be there, and the old body must
 *       not creep back in beside it.</li>
 *   <li>StepCadBuilder's copy of the cluster was <em>dead</em>, not duplicated.
 *       It is gone, and the guard pins that it stays gone -- folding it would
 *       have been the wrong repair, so a later pass must not "restore" it to
 *       converge it.</li>
 *   <li>The runtime agreement. A reflection call through each builder's private
 *       wrapper must return what the helper returns for the same inputs, which
 *       is what turns "the source looks like a delegation" into "it is one".</li>
 * </ul>
 */
class StepCadShellGeometryConvergenceTest {

    private static final Path MAIN = Path.of("src/main/java/com/minicad/step/semantic");

    private static String source(String simpleName) throws IOException {
        return Files.readString(MAIN.resolve(simpleName + ".java"));
    }

    // ─── the canonical home ───────────────────────────────────────────────

    @Test
    @DisplayName("StepCadShellGeometry owns the shared shell helpers as static methods")
    void canonicalHelpersLiveInTheShellGeometryClass() throws Exception {
        for (String name : List.of("circularFrame", "circularFrameAtPoint",
                "sampleCircle3", "polygonNormal", "quadNormal")) {
            Method found = null;
            for (Method candidate : StepCadShellGeometry.class.getDeclaredMethods()) {
                if (candidate.getName().equals(name)) {
                    found = candidate;
                }
            }
            assertNotNull(found, "StepCadShellGeometry must declare " + name);
            assertTrue(java.lang.reflect.Modifier.isStatic(found.getModifiers()),
                    name + " must stay static so both builders can share it");
        }
        // The frame itself moved here; it used to be declared privately in two builders.
        assertTrue(java.lang.reflect.Modifier.isStatic(
                        StepCadShellGeometry.class.getDeclaredClasses()[0].getModifiers()),
                "CircularFrame must be a nested type of the shared helper");
        assertEquals("CircularFrame",
                StepCadShellGeometry.class.getDeclaredClasses()[0].getSimpleName());
    }

    // ─── the two live copies became delegations ───────────────────────────

    @Test
    @DisplayName("the builders keep their entry points but delegate the bodies")
    void buildersDelegateThroughTheQualifier() throws IOException {
        for (String builder : List.of("StepCadBooleanBuilder", "StepCadSweptBuilder")) {
            String text = source(builder);
            assertTrue(text.contains("StepCadShellGeometry.circularFrame(axis)"),
                    builder + " must delegate circularFrame");
            assertTrue(text.contains("StepCadShellGeometry.circularFrameAtPoint(point, tangent)"),
                    builder + " must delegate circularFrameAtPoint");
            assertTrue(text.contains(
                            "StepCadShellGeometry.sampleCircle3(center, xAxis, yAxis, radius, segments)"),
                    builder + " must delegate sampleCircle3");
            assertTrue(text.contains("StepCadShellGeometry.polygonNormal(points, fallback)"),
                    builder + " must delegate polygonNormal");
            assertTrue(text.contains("StepCadShellGeometry.quadNormal(a, b, c, d)"),
                    builder + " must delegate quadNormal");
        }
    }

    @Test
    @DisplayName("the retired bodies and the nested frame are gone from both builders")
    void retiredBodiesAndFrameAreGone() throws IOException {
        // Distinctive fragments of the bodies that moved. If one reappears, the
        // copy came back and the delegation is only a facade over a second body.
        List<String> bodyMarkers = List.of(
                "normal = normal.add(new Vector3(",
                "Vector3 reference = Math.abs(z.getZ()) < 0.9",
                "xAxis.scale(Math.cos(angle) * radius)",
                "normal = b.subtract(a).cross(c.subtract(a))");
        // The wording moved with the bodies; the builders must no longer name it.
        List<String> messages = List.of(
                "revolved face normal is degenerate",
                "revolved side face is degenerate");
        for (String builder : List.of("StepCadBooleanBuilder", "StepCadSweptBuilder")) {
            String text = source(builder);
            for (String marker : bodyMarkers) {
                assertTrue(!text.contains(marker),
                        builder + " must not re-declare the shared body: " + marker);
            }
            for (String message : messages) {
                assertTrue(!text.contains(message),
                        builder + " must not keep its own copy of the message: " + message);
            }
            assertTrue(!text.contains("class CircularFrame"),
                    builder + " must import the shared CircularFrame, not declare one");
            assertTrue(!text.contains("new CircularFrame("),
                    builder + " must not construct a frame of its own");
        }
    }

    // ─── the third copy was dead, so it was dropped ───────────────────────

    @Test
    @DisplayName("StepCadBuilder's unreachable copy stays deleted")
    void deadClusterStaysDeleted() throws IOException {
        String text = source("StepCadBuilder");
        for (String name : List.of("buildEllipsoidLike", "pointOnPlacement",
                "addTriangleFace", "outwardApproximation", "polygonNormal")) {
            assertTrue(!text.contains(name),
                    "StepCadBuilder." + name + " had no callers and must stay deleted; "
                            + "re-adding it to converge it would be the wrong repair");
        }
        // quadNormal is the one that survived: it has a live caller.
        assertTrue(text.contains("private Direction3 quadNormal("),
                "StepCadBuilder.quadNormal is still reached, so it stays");
        assertTrue(text.contains("quadNormal(bottomRing.get("),
                "and its call site must still be there -- otherwise it is dead too");
    }

    @Test
    @DisplayName("the boolean builder keeps the helpers that have no twin left")
    void soleSurvivorsAreStillDeclared() throws IOException {
        // Once StepCadBuilder's copy went, these have exactly one declaration in
        // the package. Pinning them keeps a later pass from looking for a twin
        // that does not exist and "converging" them onto nothing.
        String text = source("StepCadBooleanBuilder");
        for (String name : List.of("buildEllipsoidLike", "pointOnPlacement",
                "outwardApproximation", "addTriangleFace")) {
            assertTrue(text.contains(name),
                    "StepCadBooleanBuilder." + name + " is the only copy left and is still called");
        }
    }

    // ─── runtime agreement ────────────────────────────────────────────────

    @Test
    @DisplayName("the delegating wrappers return exactly what the helper returns")
    void wrappersAgreeWithTheHelperAtRuntime() throws Exception {
        StepCadBuilder builder = StepCadBuilder.fromResolved(Map.of());
        StepCadBooleanBuilder booleanBuilder = new StepCadBooleanBuilder(builder, Map.of());
        StepCadSweptBuilder sweptBuilder = new StepCadSweptBuilder(
                builder, new StepProfileBuilder(new StepCadGeometryOps(builder), entity -> null));

        CartesianPoint a = new CartesianPoint(0.0, 0.0, 0.0);
        CartesianPoint b = new CartesianPoint(1.0, 0.0, 0.0);
        CartesianPoint c = new CartesianPoint(1.0, 1.0, 0.0);
        CartesianPoint d = new CartesianPoint(0.0, 1.0, 0.0);
        Direction3 axis = new Direction3(0.0, 0.0, 1.0);
        Vector3 xAxis = new Vector3(1.0, 0.0, 0.0);
        Vector3 yAxis = new Vector3(0.0, 1.0, 0.0);

        for (Object owner : List.of(booleanBuilder, sweptBuilder)) {
            assertEquals(StepCadShellGeometry.quadNormal(a, b, c, d),
                    call(owner, "quadNormal", new Class<?>[]{CartesianPoint.class, CartesianPoint.class,
                            CartesianPoint.class, CartesianPoint.class}, a, b, c, d),
                    owner.getClass().getSimpleName() + ".quadNormal drifted");
            assertEquals(StepCadShellGeometry.polygonNormal(List.of(a, b, c), new Vector3(0, 0, 1)),
                    call(owner, "polygonNormal", new Class<?>[]{List.class, Vector3.class},
                            List.of(a, b, c), new Vector3(0, 0, 1)),
                    owner.getClass().getSimpleName() + ".polygonNormal drifted");
            assertEquals(StepCadShellGeometry.sampleCircle3(a, xAxis, yAxis, 2.0, 6),
                    call(owner, "sampleCircle3", new Class<?>[]{CartesianPoint.class, Vector3.class,
                            Vector3.class, double.class, int.class}, a, xAxis, yAxis, 2.0, 6),
                    owner.getClass().getSimpleName() + ".sampleCircle3 drifted");

            StepCadShellGeometry.CircularFrame expected = StepCadShellGeometry.circularFrame(axis);
            StepCadShellGeometry.CircularFrame direct = (StepCadShellGeometry.CircularFrame)
                    call(owner, "circularFrame", new Class<?>[]{Direction3.class}, axis);
            assertEquals(expected.getX(), direct.getX(),
                    owner.getClass().getSimpleName() + ".circularFrame x drifted");
            assertEquals(expected.getY(), direct.getY(),
                    owner.getClass().getSimpleName() + ".circularFrame y drifted");

            StepCadShellGeometry.CircularFrame atPoint = (StepCadShellGeometry.CircularFrame)
                    call(owner, "circularFrameAtPoint",
                            new Class<?>[]{CartesianPoint.class, Direction3.class}, a, axis);
            assertEquals(expected.getX(), atPoint.getX(),
                    owner.getClass().getSimpleName() + ".circularFrameAtPoint drifted");
        }
    }

    private static Object call(Object owner, String name, Class<?>[] types, Object... args)
            throws Exception {
        Method method = owner.getClass().getDeclaredMethod(name, types);
        method.setAccessible(true);
        return method.invoke(owner, args);
    }

    // ─── the helper's own behaviour ───────────────────────────────────────

    @Test
    @DisplayName("polygonNormal keeps its fallback sign flip and its wording")
    void polygonNormalBehaviour() {
        List<CartesianPoint> square = List.of(
                new CartesianPoint(0.0, 0.0, 0.0),
                new CartesianPoint(1.0, 0.0, 0.0),
                new CartesianPoint(1.0, 1.0, 0.0),
                new CartesianPoint(0.0, 1.0, 0.0));
        assertEquals(new Vector3(0.0, 0.0, 1.0),
                StepCadShellGeometry.polygonNormal(square, new Vector3(0, 0, 1)).asVector());
        // A fallback pointing the other way flips the winding to match it.
        assertEquals(new Vector3(0.0, 0.0, -1.0),
                StepCadShellGeometry.polygonNormal(square, new Vector3(0, 0, -1)).asVector());

        List<CartesianPoint> allSame = List.of(
                new CartesianPoint(1.0, 1.0, 1.0),
                new CartesianPoint(1.0, 1.0, 1.0),
                new CartesianPoint(1.0, 1.0, 1.0));
        UnsupportedGeometryException degenerate = assertThrows(
                UnsupportedGeometryException.class,
                () -> StepCadShellGeometry.polygonNormal(allSame, new Vector3(0, 0, 0)));
        assertEquals("revolved face normal is degenerate", degenerate.getMessage(),
                "the lifted wording is observable behaviour, not an implementation detail");
    }

    @Test
    @DisplayName("quadNormal keeps its wording")
    void quadNormalBehaviour() {
        CartesianPoint a = new CartesianPoint(0.0, 0.0, 0.0);
        CartesianPoint b = new CartesianPoint(1.0, 0.0, 0.0);
        CartesianPoint c = new CartesianPoint(1.0, 1.0, 0.0);
        CartesianPoint d = new CartesianPoint(0.0, 1.0, 0.0);
        assertEquals(new Vector3(0.0, 0.0, 1.0),
                StepCadShellGeometry.quadNormal(a, b, c, d).asVector());

        UnsupportedGeometryException degenerate = assertThrows(
                UnsupportedGeometryException.class,
                () -> StepCadShellGeometry.quadNormal(a, a, a, a));
        assertEquals("revolved side face is degenerate", degenerate.getMessage());
    }

    @Test
    @DisplayName("sampleCircle3 walks a full ring on the supplied axes")
    void sampleCircle3Behaviour() {
        CartesianPoint center = new CartesianPoint(1.0, 2.0, 3.0);
        List<CartesianPoint> ring = StepCadShellGeometry.sampleCircle3(
                center, new Vector3(1, 0, 0), new Vector3(0, 1, 0), 2.0, 4);
        assertEquals(4, ring.size());
        assertEquals(new CartesianPoint(3.0, 2.0, 3.0), ring.get(0),
                "the first sample sits along the x axis");
        // The quarter-turn sample is only exact up to cos(pi/2)'s rounding.
        assertEquals(1.0, ring.get(1).getX(), 1.0e-12);
        assertEquals(4.0, ring.get(1).getY(), 1.0e-12);
        assertEquals(3.0, ring.get(1).getZ(), 1.0e-12);
        for (CartesianPoint point : ring) {
            assertEquals(2.0, point.distanceTo(center), 1.0e-12);
        }
    }

    @Test
    @DisplayName("circularFrame yields an orthonormal frame whose z is the axis")
    void circularFrameBehaviour() {
        for (Direction3 axis : List.of(new Direction3(0, 0, 1), new Direction3(1, 0, 0),
                new Direction3(0.3, -0.4, 0.5))) {
            StepCadShellGeometry.CircularFrame frame = StepCadShellGeometry.circularFrame(axis);
            Vector3 x = frame.getX();
            Vector3 y = frame.getY();
            assertEquals(1.0, x.norm(), 1.0e-9);
            assertEquals(1.0, y.norm(), 1.0e-9);
            assertEquals(0.0, x.dot(y), 1.0e-9);
            // z = x cross y must reproduce the axis direction.
            Vector3 z = x.cross(y).normalize();
            Vector3 expected = axis.asVector().normalize();
            assertEquals(0.0, z.subtract(expected).norm(), 1.0e-9);
        }
    }

    @Test
    @DisplayName("the shared frame is what the builders compiled against")
    void buildersResolveTheSharedFrameType() throws IOException {
        Set<String> imported = Set.of(
                "import com.minicad.step.semantic.StepCadShellGeometry.CircularFrame;");
        for (String builder : List.of("StepCadBooleanBuilder", "StepCadSweptBuilder")) {
            String text = source(builder);
            assertTrue(imported.stream().anyMatch(text::contains),
                    builder + " must import the shared CircularFrame so its signatures "
                            + "keep resolving after the nested class was removed");
        }
    }
}
