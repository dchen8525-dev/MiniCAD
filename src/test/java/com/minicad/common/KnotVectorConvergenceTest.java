package com.minicad.common;

import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.BSplineSurface3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.RationalBSplineCurve3;
import com.minicad.geometry.RationalBSplineSurface3;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.RationalBSplineCurve2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the convergence of the B-spline knot vector onto {@link KnotVector}, and of the
 * curve sampling contract onto {@link BSplineKernel#sampleDomain}.
 *
 * <p>Before this convergence the expanded-knot cache was written out eight times - once
 * per curve class and twice per surface class - and every copy had to be kept in step by
 * hand. It also caught the sampling rule split down the middle of the same family: the
 * two non-rational curves pinned a floor of 8 segments (and their tests said so), while
 * the two rational curves sampled whatever they were handed, so {@code sample(2)}
 * returned 9 points on one and 3 on its twin.</p>
 *
 * <p>Convergence without a guard regresses silently: a later edit can paste a private
 * cache back into one shape class, or re-inline the segment floor into a curve, and
 * every existing test still passes. It pins:</p>
 * <ul>
 *   <li>the expanded-knot cache exists in exactly one file in main sources;</li>
 *   <li>no shape class keeps a cache field of its own, and each holds its knots in
 *       {@link KnotVector} fields - the curves directly, the surface pair inside its
 *       shared domain object;</li>
 *   <li>every B-spline curve honours the family floor of 8 segments, and each rational
 *       curve samples exactly like its non-rational twin, in both dimensions;</li>
 *   <li>the two readings of "the natural domain" - first/last unique knot versus the
 *       expanded vector at the degree and at the control-point count - are the ones the
 *       families use, and coincide only for a clamped vector.</li>
 * </ul>
 */
class KnotVectorConvergenceTest {

    private static final String KNOT_VECTOR = "src/main/java/com/minicad/common/KnotVector.java";

    private static final String KERNEL = "src/main/java/com/minicad/common/BSplineKernel.java";

    /** Every shape class of the family, with the knot-vector fields it must hold. */
    private static final List<Class<?>> SHAPES = List.of(
            BSplineCurve3.class, RationalBSplineCurve3.class,
            BSplineCurve2.class, RationalBSplineCurve2.class,
            BSplineSurface3.class, RationalBSplineSurface3.class);

    private static final List<String> CURVE_SOURCES = List.of(
            "src/main/java/com/minicad/geometry/BSplineCurve3.java",
            "src/main/java/com/minicad/geometry/RationalBSplineCurve3.java",
            "src/main/java/com/minicad/geometry2d/BSplineCurve2.java",
            "src/main/java/com/minicad/geometry2d/RationalBSplineCurve2.java");

    // ------------------------------------------------------------------
    // Structure
    // ------------------------------------------------------------------

    @Test
    @DisplayName("the expanded-knot cache is declared in exactly one file")
    void theExpandedKnotCacheShouldHaveExactlyOneHome() throws IOException {
        Pattern cacheField = Pattern.compile("volatile\\s+List<Double>\\s+\\w+");
        List<String> homes = new ArrayList<>();
        for (Path file : mainSources()) {
            Matcher matcher = cacheField.matcher(read(file));
            if (matcher.find()) {
                homes.add(unix(file.toString()));
            }
        }
        assertEquals(List.of(KNOT_VECTOR), homes,
                "the cache body was written out once per curve and twice per surface; a second "
                        + "copy is free to drift from the shared one (and one of the eight had already "
                        + "gone missing before this fold)");
    }

    @Test
    @DisplayName("no shape class keeps its own knot cache; each holds KnotVectors")
    void noShapeClassShouldKeepItsOwnKnotCache() throws Exception {
        for (Class<?> shape : SHAPES) {
            for (String stale : List.of("expandedKnotsCache", "uExpandedKnots", "vExpandedKnots")) {
                assertThrows(NoSuchFieldException.class, () -> shape.getDeclaredField(stale),
                        shape.getSimpleName() + " declares " + stale + " again: the cache belongs to "
                                + "KnotVector, and a private copy is free to drift from it");
            }
            boolean surface = BSplineSurface3.class.equals(shape) || RationalBSplineSurface3.class.equals(shape);
            if (!surface) {
                assertNotNull(shape.getDeclaredField("knotVector"),
                        shape.getSimpleName() + " must keep its knots in a KnotVector");
                continue;
            }
            // The surface pair keeps both parameter directions inside the one domain object
            // rather than in two fields of its own. The KnotVectors are still the only place
            // the knots live - one indirection deeper, so the assertion follows it there.
            for (String direction : List.of("uKnotVector", "vKnotVector")) {
                assertThrows(NoSuchFieldException.class, () -> shape.getDeclaredField(direction),
                        shape.getSimpleName() + " must not grow back its own per-direction knot fields");
            }
            Class<?> domain = shape.getDeclaredField("domain").getType();
            for (String direction : List.of("uKnotVector", "vKnotVector")) {
                assertEquals(KnotVector.class, domain.getDeclaredField(direction).getType(),
                        shape.getSimpleName() + " must keep " + direction + " in a KnotVector");
            }
        }
    }

    // ------------------------------------------------------------------
    // Behaviour
    // ------------------------------------------------------------------

    @Test
    @DisplayName("every B-spline curve applies the family floor of 8 segments")
    void everySplineCurveShouldApplyTheSegmentFloor() {
        for (Curve3 curve : List.of(curve3d(), rationalCurve3d())) {
            assertEquals(9, curve.sample(2).size(),
                    curve.getClass().getSimpleName() + ".sample(2) must honour the family floor of 8 "
                            + "segments - a spline is never sampled so coarsely that its shape cannot be read");
            assertEquals(9, curve.sample(0).size(),
                    curve.getClass().getSimpleName() + ".sample(0) used to divide by zero");
        }
        for (Curve2 curve : List.of(curve2d(), rationalCurve2d())) {
            assertEquals(9, curve.sample(2).size(),
                    curve.getClass().getSimpleName() + ".sample(2) must honour the family floor of 8 segments");
            assertEquals(9, curve.sample(0).size(),
                    curve.getClass().getSimpleName() + ".sample(0) used to divide by zero");
        }
    }

    @Test
    @DisplayName("each rational curve samples exactly like its non-rational twin")
    void rationalCurvesShouldSampleLikeTheirNonRationalTwin() {
        Curve3 plain = curve3d();
        Curve3 weighted = rationalCurve3d();
        Curve2 flat = curve2d();
        Curve2 flatWeighted = rationalCurve2d();

        for (int segments : new int[]{0, 1, 2, 3, 4, 7, 8, 9, 12}) {
            List<CartesianPoint> fromPlain = plain.sample(segments);
            List<CartesianPoint> fromWeighted = weighted.sample(segments);
            assertEquals(fromPlain.size(), fromWeighted.size(),
                    "the 3D pair disagrees on sample(" + segments + ") size");
            for (int i = 0; i < fromPlain.size(); i++) {
                assertEquals(fromPlain.get(i).x(), fromWeighted.get(i).x(), 1e-12, "sample(" + segments + ") point " + i);
                assertEquals(fromPlain.get(i).y(), fromWeighted.get(i).y(), 1e-12, "sample(" + segments + ") point " + i);
            }

            List<Point2> fromFlat = flat.sample(segments);
            List<Point2> fromFlatWeighted = flatWeighted.sample(segments);
            assertEquals(fromFlat.size(), fromFlatWeighted.size(),
                    "the 2D pair disagrees on sample(" + segments + ") size");
            for (int i = 0; i < fromFlat.size(); i++) {
                assertEquals(fromFlat.get(i).getX(), fromFlatWeighted.get(i).getX(), 1e-12,
                        "sample(" + segments + ") point " + i);
            }
        }
    }

    @Test
    @DisplayName("the segment floor is not re-implemented inside a curve")
    void theSegmentFloorShouldStayInTheKernel() throws IOException {
        String kernel = code(read(Paths.get(KERNEL)));
        assertTrue(kernel.contains("sampleDomain("),
                "BSplineKernel must own the shared domain sampling");

        for (String curve : CURVE_SOURCES) {
            String body = code(read(Paths.get(curve)));
            assertTrue(body.contains("BSplineKernel.sampleDomain("),
                    curve + " does not delegate its sampling to the shared kernel member");
            assertFalse(body.contains("Math.max(8,"),
                    curve + " re-inlined the segment floor: that is how the rational curves ended up "
                            + "sampling three points where their twins sampled nine");
        }
    }

    @Test
    @DisplayName("the domain readings coincide only for a clamped knot vector")
    void theTwoDomainReadingsShouldBeNamedApart() {
        // Clamped: first/last unique knot equals the expanded entries the surfaces read.
        KnotVector clamped = new KnotVector(List.of(0.0, 1.0), List.of(4, 4));
        assertEquals(0.0, clamped.start());
        assertEquals(1.0, clamped.end());
        assertEquals(List.of(0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0), clamped.expanded());
        assertEquals(clamped.start(), clamped.expandedStart(3), "clamped: both readings agree at the start");
        assertEquals(clamped.end(), clamped.expandedEnd(4), "clamped: both readings agree at the end");

        // Unclamped: the two readings differ, which is why they are not one accessor.
        KnotVector open = new KnotVector(List.of(0.0, 0.4, 1.0), List.of(1, 2, 3));
        assertEquals(List.of(0.0, 0.4, 0.4, 1.0, 1.0, 1.0), open.expanded());
        assertEquals(0.0, open.start());
        assertEquals(1.0, open.end());
        assertEquals(0.4, open.expandedStart(1));
        assertEquals(0.4, open.expandedEnd(2));
        assertNotEquals(open.start(), open.expandedStart(1),
                "an unclamped vector separates the unique-knot reading from the expanded-index one");
    }

    @Test
    @DisplayName("the expansion cache is per KnotVector and stable")
    void theExpansionCacheShouldBePerVectorAndStable() {
        KnotVector first = new KnotVector(List.of(0.0, 1.0), List.of(2, 2));
        KnotVector second = new KnotVector(List.of(0.0, 1.0), List.of(2, 2));

        assertSame(first.expanded(), first.expanded(), "cache must not be rebuilt");
        assertEquals(first.expanded(), second.expanded(), "equal definitions expand equally");
        assertNotSame(first.expanded(), second.expanded(),
                "the cache is per instance; sharing it would make one shape's evaluation depend on another's");
    }

    @Test
    @DisplayName("the curvature of the inputs is still rejected")
    void theKnotVectorShouldRejectMismatchedSizes() {
        assertThrows(GeometryException.class,
                () -> new KnotVector(List.of(0.0, 1.0), List.of(2)),
                "knot values and multiplicities must have matching sizes");
    }

    // ------------------------------------------------------------------
    // Fixtures and helpers
    // ------------------------------------------------------------------

    private static List<CartesianPoint> points3d() {
        return List.of(
                new CartesianPoint(0, 0, 0),
                new CartesianPoint(1, 1, 0),
                new CartesianPoint(2, 1, 0),
                new CartesianPoint(3, 0, 0));
    }

    private static List<Point2> points2d() {
        return List.of(new Point2(0, 0), new Point2(1, 1), new Point2(2, 1), new Point2(3, 0));
    }

    private static Curve3 curve3d() {
        return new BSplineCurve3(3, points3d(), List.of(4, 4), List.of(0.0, 2.0));
    }

    private static Curve3 rationalCurve3d() {
        return new RationalBSplineCurve3(3, points3d(), List.of(1.0, 1.0, 1.0, 1.0), List.of(4, 4), List.of(0.0, 2.0));
    }

    private static Curve2 curve2d() {
        return new BSplineCurve2(3, points2d(), List.of(4, 4), List.of(0.0, 2.0));
    }

    private static Curve2 rationalCurve2d() {
        return new RationalBSplineCurve2(3, points2d(), List.of(1.0, 1.0, 1.0, 1.0), List.of(4, 4), List.of(0.0, 2.0));
    }

    /** Strips comments so a "must not contain" assertion cannot be satisfied by its own prose. */
    private static String code(String source) {
        return source.replaceAll("(?s)/\\*.*?\\*/", " ").replaceAll("//[^\n]*", " ");
    }

    private static List<Path> mainSources() throws IOException {
        try (Stream<Path> files = Files.walk(Paths.get("src/main/java"))) {
            return files.filter(path -> path.toString().endsWith(".java")).toList();
        }
    }

    private static String unix(String path) {
        return path.replace('\\', '/');
    }

    private static String read(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }
}
