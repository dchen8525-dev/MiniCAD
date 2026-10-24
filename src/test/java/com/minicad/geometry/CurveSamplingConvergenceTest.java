package com.minicad.geometry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the convergence of the closed-form 3D curves onto {@link CurveSampling3}.
 *
 * <p>{@link Circle}, {@link Ellipse3}, {@link Parabola3}, {@link Clothoid3} and
 * {@link SurfaceCurve3} used to carry eight private copies of "walk {@code [0, segments]}
 * and evaluate the curve at one parameter per step". Folding them is only safe if the
 * samples do not move, and "do not move" here has to mean bit for bit: every copy wrote a
 * different but mathematically equivalent parameter expression, and the order of the
 * multiplications and divisions decides the last ulp. A tolerance-based assertion would
 * have accepted a silent rewrite of that order.</p>
 *
 * <p>So this test reproduces each curve's <em>pre-convergence</em> expression verbatim,
 * rebuilds the samples from it, and compares raw bits against what the curve now returns.
 * It also pins the shape of the fold: the five curves reach the shared helper and no
 * longer walk a sampling loop themselves, while the curves whose samples are not a plain
 * uniform walk keep theirs.</p>
 */
class CurveSamplingConvergenceTest {

    /** One curve's sampling entry point, paired with the parameter arithmetic it used to own. */
    private record SampleCase(String name, Curve3 curve, IntFunction<List<CartesianPoint>> sample, Parameter parameter) {

        @Override
        public String toString() {
            return name;
        }
    }

    /** One sample position, in the exact arithmetic the curve used to inline. */
    private interface Parameter {
        double at(int index, int segments);
    }

    private static final int[] SEGMENTS = {1, 2, 3, 5, 8, 32};

    private static final Axis2Placement3D PLACEMENT = new Axis2Placement3D(
            new CartesianPoint(1, 2, 3),
            Direction3.from(new Vector3(0, 0, 1)),
            Direction3.from(new Vector3(1, 0, 0)));

    // ─── the samples must not move ───────────────────────────────────────────────

    @ParameterizedTest(name = "{0}")
    @MethodSource("sampleCases")
    void samplesKeepThePreConvergenceCoordinates(SampleCase sampleCase) {
        List<String> drift = new ArrayList<>();

        for (int segments : SEGMENTS) {
            List<CartesianPoint> samples = sampleCase.sample().apply(segments);
            assertEquals(segments + 1, samples.size(),
                    () -> sampleCase + ": " + segments + " segments must still yield one point per step");
            for (int i = 0; i <= segments; i++) {
                CartesianPoint expected = sampleCase.curve().pointAt(sampleCase.parameter().at(i, segments));
                if (!sameBits(expected, samples.get(i))) {
                    drift.add("[" + i + "] of " + segments + ": " + samples.get(i) + " instead of " + expected);
                }
            }
        }

        assertTrue(drift.isEmpty(), () -> sampleCase + " no longer samples where it used to:\n"
                + String.join("\n", drift.subList(0, Math.min(4, drift.size()))));
    }

    // ─── the source must keep exactly one home ────────────────────────────────────

    @Test
    void theClosedFormCurvesReachTheOneHome() throws IOException {
        Path root = Path.of("src", "main", "java", "com", "minicad", "geometry");
        Map<String, Integer> delegates = new LinkedHashMap<>();
        delegates.put("Circle.java", 1);
        delegates.put("Ellipse3.java", 3);
        delegates.put("Parabola3.java", 2);
        delegates.put("Clothoid3.java", 1);
        delegates.put("SurfaceCurve3.java", 1);

        for (Map.Entry<String, Integer> entry : delegates.entrySet()) {
            String body = withoutComments(Files.readString(root.resolve(entry.getKey()), StandardCharsets.UTF_8));
            assertEquals(entry.getValue(), countOf(body, "CurveSampling3.between("),
                    () -> entry.getKey() + " must reach the shared sampler once per sampling entry point");
            assertFalse(body.contains("for (int i = 0; i <= segments"),
                    () -> entry.getKey() + " must not carry a sampling loop of its own any more");
            assertTrue(body.contains("this::pointAt"),
                    () -> entry.getKey() + " must hand its own point evaluation to the shared sampler");
        }

        String helper = withoutComments(
                Files.readString(root.resolve("CurveSampling3.java"), StandardCharsets.UTF_8));
        assertTrue(helper.contains("start + (end - start) * i / segments"),
                "the shared sampler must keep the one-rounding arithmetic every copy used");
    }

    /**
     * Pins who may still own a sampling loop. These are curves whose samples are not a
     * plain uniform walk: {@link Hyperbola3} emits both branches, {@link Line3} walks a
     * finite window around its anchor, and {@link CompositeCurve3} turns the global
     * parameter into a segment index before sampling. {@link CurveSampling3} itself is the
     * home the others reach.
     */
    @Test
    void onlyTheCurvesWithTheirOwnSamplingSemanticsStillWalkByHand() throws IOException {
        Path root = Path.of("src", "main", "java", "com", "minicad", "geometry");
        List<String> handWalkers = new ArrayList<>();
        try (Stream<Path> files = Files.walk(root)) {
            for (Path file : files.filter(path -> path.toString().endsWith(".java")).toList()) {
                String body = withoutComments(Files.readString(file, StandardCharsets.UTF_8));
                if (body.contains("for (int i = 0; i <= segments")) {
                    handWalkers.add(root.relativize(file).toString().replace('\\', '/'));
                }
            }
        }
        handWalkers.sort(null);
        assertEquals(List.of("CompositeCurve3.java", "CurveSampling3.java", "Hyperbola3.java", "Line3.java"),
                handWalkers,
                "Uniform parameter sampling has one home, CurveSampling3. The other three own a"
                        + " different sampling semantics and must not be folded into it.");
    }

    // ─── fixtures ────────────────────────────────────────────────────────────────

    static Stream<SampleCase> sampleCases() {
        Circle circle = new Circle(PLACEMENT, 1.5);
        Ellipse3 ellipse = new Ellipse3(PLACEMENT, 2.0, 1.0);
        Parabola3 parabola = new Parabola3(PLACEMENT, 1.25);
        Clothoid3 clothoid = new Clothoid3(PLACEMENT, 0.5, 0.25);
        SurfaceCurve3 surfaceCurve = new SurfaceCurve3(new Circle(PLACEMENT, 2.0));

        return Stream.of(
                new SampleCase("Circle.sample", circle, circle::sample,
                        (index, segments) -> 2 * Math.PI * index / segments),
                new SampleCase("Ellipse3.sample", ellipse, ellipse::sample,
                        (index, segments) -> 2 * Math.PI * index / segments),
                new SampleCase("Ellipse3.sample(arcFraction)", ellipse, segments -> ellipse.sample(segments, 0.75),
                        (index, segments) -> 2 * Math.PI * 0.75 * index / segments),
                new SampleCase("Ellipse3.sample(angleStart, angleEnd)", ellipse,
                        segments -> ellipse.sample(segments, 0.4, 2.7),
                        (index, segments) -> 0.4 + (2.7 - 0.4) * index / segments),
                new SampleCase("Parabola3.sample", parabola, parabola::sample,
                        (index, segments) -> -1.0 + 2.0 * index / segments),
                new SampleCase("Parabola3.sample(tMin, tMax)", parabola,
                        segments -> parabola.sample(segments, -0.6, 1.7),
                        (index, segments) -> -0.6 + (1.7 - -0.6) * index / segments),
                new SampleCase("Clothoid3.sample", clothoid, clothoid::sample,
                        (index, segments) -> (double) index / segments),
                new SampleCase("SurfaceCurve3.sample", surfaceCurve, surfaceCurve::sample,
                        (index, segments) -> (double) index / segments));
    }

    // ─── helpers ─────────────────────────────────────────────────────────────────

    private static boolean sameBits(CartesianPoint expected, CartesianPoint actual) {
        return sameBits(expected.x(), actual.x())
                && sameBits(expected.y(), actual.y())
                && sameBits(expected.z(), actual.z());
    }

    private static boolean sameBits(double expected, double actual) {
        return Double.doubleToRawLongBits(expected) == Double.doubleToRawLongBits(actual);
    }

    private static String withoutComments(String text) {
        return text.replaceAll("(?s)/\\*.*?\\*/", "").replaceAll("(?m)//.*$", "");
    }

    private static int countOf(String text, String needle) {
        int count = 0;
        int from = text.indexOf(needle);
        while (from >= 0) {
            count++;
            from = text.indexOf(needle, from + needle.length());
        }
        return count;
    }
}
