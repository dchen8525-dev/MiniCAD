package com.minicad.export.mesh;

import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Direction2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Point2;
import com.minicad.preview.payload.UvPoint;
import com.minicad.preview.sampling.PcurveSamplingHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the pcurve-sampling convergence in MeshTriangulatorParametric:
 * PcurveSamplingHelper now owns the line/circle/ellipse/spline samplers, and the
 * UV type the mesh side speaks is com.minicad.preview.payload.UvPoint rather
 * than the nested copy this file used to declare.
 *
 * <p>The trap this convergence walked into is worth keeping in mind: the private
 * copies were text-identical to the helper's methods, but the unqualified
 * {@code UvPoint} in each file resolved to a <em>different</em> class -- the
 * nested one here, com.minicad.preview.payload.UvPoint there. Identical text is
 * not identical code; the copies could only be delegated to once the nested type
 * was dropped too.
 *
 * <p>It pins:
 *
 * <ul>
 *   <li>none of the five converged samplers is declared locally any more, and
 *       the four with a call site reach PcurveSamplingHelper through the
 *       qualifier;</li>
 *   <li>the nested UvPoint type stays deleted and the shared one stays
 *       imported;</li>
 *   <li>the deliberate non-convergence: sampleTrimmedPcurve, score,
 *       alignTrimmedSamples and uvDistance stay local because this file's UV
 *       distance is null guarded and the helper's is not;</li>
 *   <li>at runtime the table's dispatch and the helper still agree, so the
 *       delegation is real rather than asserted.</li>
 * </ul>
 */
class MeshPcurveSamplingConvergenceTest {

    private static final String PARAMETRIC =
            "src/main/java/com/minicad/export/mesh/MeshTriangulatorParametric.java";

    /** Samplers whose bodies were identical to PcurveSamplingHelper's. */
    private static final List<String> CONVERGED = List.of(
            "sampleLinePcurve",
            "sampleCirclePcurve",
            "sampleEllipsePcurve",
            "sampleSplinePcurve");

    /** Only reached from inside the helper, so no qualifier call is expected. */
    private static final String INDEX_HELPER = "closestPointIndex";

    /** Kept local on purpose -- see the class comment. */
    private static final List<String> DELIBERATELY_LOCAL = List.of(
            "sampleTrimmedPcurve",
            "score",
            "alignTrimmedSamples",
            "uvDistance");

    private static final Point2 ORIGIN = new Point2(0.0, 0.0);
    private static final Direction2 X_DIR = new Direction2(1.0, 0.0);

    // ─── guard: the copies are gone, the calls are qualified ──────────────

    @Test
    @DisplayName("no converged sampler is declared locally")
    void samplersAreNotRedeclared() throws Exception {
        String text = read();

        for (String name : CONVERGED) {
            assertFalse(declares(text, name),
                    "MeshTriangulatorParametric re-declared " + name + ": that copy was "
                            + "deleted by the pcurve convergence. Call "
                            + "PcurveSamplingHelper." + name + " instead.");
        }
        assertFalse(declares(text, INDEX_HELPER),
                INDEX_HELPER + " must not be re-declared here: it belongs to "
                        + "PcurveSamplingHelper, which uses it for its own samplers.");
    }

    @Test
    @DisplayName("the dispatch table calls the samplers through the helper qualifier")
    void tableShouldDelegate() throws Exception {
        String text = read();

        for (String name : CONVERGED) {
            assertTrue(text.contains("PcurveSamplingHelper." + name + "("),
                    "MeshTriangulatorParametric must call " + name + " through the "
                            + "PcurveSamplingHelper qualifier; a bare call means a local "
                            + "copy or a static import re-entered the file.");
        }
    }

    @Test
    @DisplayName("the nested UvPoint type stays deleted in favour of the shared one")
    void nestedUvPointStaysDeleted() throws Exception {
        String text = read();

        assertFalse(Pattern.compile("(?m)^\\s*(?:static\\s+)?(?:final\\s+)?class\\s+UvPoint\\b")
                        .matcher(text).find(),
                "MeshTriangulatorParametric declared its own UvPoint again. That nested "
                        + "type was text-identical to com.minicad.preview.payload.UvPoint, "
                        + "which is why the samplers looked duplicated but could not be "
                        + "shared; keep the single shared type.");
        assertTrue(text.contains("import com.minicad.preview.payload.UvPoint;"),
                "MeshTriangulatorParametric must keep importing the shared UvPoint.");
    }

    @Test
    @DisplayName("the null-guarded trimmed path stays local on purpose")
    void trimmedPathStaysLocal() throws Exception {
        String text = read();
        for (String name : DELIBERATELY_LOCAL) {
            assertTrue(declares(text, name),
                    name + " was dropped from MeshTriangulatorParametric, but it is not part "
                            + "of the convergence: this file's uvDistance is null guarded "
                            + "(a null endpoint scores as +inf) while "
                            + "PcurveSamplingHelper.distanceSquared is not, so routing the "
                            + "trimmed path through the helper would drop that guard.");
        }
    }

    // ─── runtime: the delegation is real ─────────────────────────────────

    @Test
    @DisplayName("the table's line/circle/ellipse dispatch still matches the helper")
    void dispatchMatchesTheHelper() throws Exception {
        UvPoint start = new UvPoint(0.0, 0.0);
        UvPoint end = new UvPoint(5.0, 0.0);
        Line2 line = new Line2(ORIGIN, X_DIR);
        assertEquals(PcurveSamplingHelper.sampleLinePcurve(line, start, end),
                dispatch(line, start, end),
                "the line rule drifted from PcurveSamplingHelper.sampleLinePcurve");

        UvPoint circleStart = new UvPoint(3.0, 0.0);
        UvPoint circleEnd = new UvPoint(0.0, 3.0);
        Circle2 circle = new Circle2(ORIGIN, X_DIR, 3.0);
        assertEquals(PcurveSamplingHelper.sampleCirclePcurve(circle, circleStart, circleEnd),
                dispatch(circle, circleStart, circleEnd),
                "the circle rule drifted from PcurveSamplingHelper.sampleCirclePcurve");

        UvPoint ellipseStart = new UvPoint(4.0, 0.0);
        UvPoint ellipseEnd = new UvPoint(0.0, 2.0);
        Ellipse2 ellipse = new Ellipse2(ORIGIN, X_DIR, 4.0, 2.0);
        assertEquals(PcurveSamplingHelper.sampleEllipsePcurve(ellipse, ellipseStart, ellipseEnd),
                dispatch(ellipse, ellipseStart, ellipseEnd),
                "the ellipse rule drifted from PcurveSamplingHelper.sampleEllipsePcurve");
    }

    @Test
    @DisplayName("the spline rule matches the helper's sampler too")
    void splineDispatchMatchesTheHelper() throws Exception {
        BSplineCurve2 spline = new BSplineCurve2(
                1,
                List.of(new Point2(0.0, 0.0), new Point2(1.0, 0.0)),
                List.of(2, 2),
                List.of(0.0, 1.0));
        UvPoint start = new UvPoint(0.0, 0.0);
        UvPoint end = new UvPoint(1.0, 0.0);
        assertEquals(PcurveSamplingHelper.sampleSplinePcurve(spline, start, end),
                dispatch(spline, start, end),
                "the spline rule drifted from PcurveSamplingHelper.sampleSplinePcurve");
    }

    /** Walks the private sampleCurve2 table, the same entry the rules live behind. */
    private static List<UvPoint> dispatch(Curve2 curve, UvPoint start, UvPoint end)
            throws Exception {
        Method method = MeshTriangulatorParametric.class.getDeclaredMethod(
                "sampleCurve2", Curve2.class, UvPoint.class, UvPoint.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<UvPoint> samples = (List<UvPoint>) method.invoke(null, curve, start, end);
        return samples;
    }

    // ─── source helpers ──────────────────────────────────────────────────

    private static String read() throws IOException {
        Path path = Paths.get(PARAMETRIC);
        assertTrue(Files.exists(path), "Missing source file " + path.toAbsolutePath());
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    private static boolean declares(String text, String name) {
        Pattern declaration = Pattern.compile(
                "(?m)^\\s*(?:public |private |protected )?(?:static )?[\\w<>\\[\\], .]+\\s+"
                        + Pattern.quote(name) + "\\s*\\(");
        return declaration.matcher(text).find();
    }
}
