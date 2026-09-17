package com.minicad.preview.sampling;

import com.minicad.export.json.StepRepresentationPayloadBuilder;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Polyline3;
import com.minicad.helper.StepTextReader;
import com.minicad.step.model.StepEntity;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the merge of the two {@code CurveEvaluator} declarations and the
 * convergence of the sampled-curve evaluator onto a single implementation.
 *
 * <p>{@code PreviewCurveEvaluator} used to declare a nested {@code CurveEvaluator}
 * interface that was byte-identical to the sibling top-level type of the same
 * name. Inside that file the nested copy shadowed the top-level one, so the two
 * were not interchangeable: {@code StepRepresentationPayloadBuilder} (json) could
 * only see the top-level type while the preview factories returned the nested one.
 * That in turn forced the json pipeline to keep its own copy of
 * {@code sampledCurveEvaluator}, itself byte-identical to the preview one, and its
 * own 59-rule dispatch table to go with it. The nested interface is gone, and
 * {@code StepRepresentationPayloadBuilder.curveEvaluator} now delegates to the
 * preview table instead of holding a second one.
 *
 * <p>The two bodies were compared after whitespace normalisation, so this is a
 * behaviour-preserving convergence rather than a behaviour-compatible one.
 *
 * <p>Convergence without a guard regresses silently: a later edit can re-declare
 * the nested interface (shadowing the shared name again), fork the sampler body
 * into a second copy, or re-add a dead import, and every existing test still
 * passes -- both copies agree until they drift. It pins:
 *
 * <ul>
 *   <li>exactly one {@code interface CurveEvaluator} declaration in main sources,
 *       the top-level one, and no nested look-alike on {@code PreviewCurveEvaluator};</li>
 *   <li>the public factories still expose that shared type (not a nested leak);</li>
 *   <li>the json payload builder holds a delegating facade and none of the body;</li>
 *   <li>the evaluator body has exactly one home;</li>
 *   <li>the facade and the sampler still agree at runtime, degenerate input
 *       included, and the shared default {@code tangentAt}/{@code sample} still work;</li>
 *   <li>no main source keeps a dead {@code CurveEvaluator} import.</li>
 * </ul>
 */
class CurveEvaluatorSamplerConvergenceTest {

    private static final String EVALUATOR =
            "src/main/java/com/minicad/preview/sampling/CurveEvaluator.java";
    private static final String PREVIEW_CURVE_EVALUATOR =
            "src/main/java/com/minicad/preview/sampling/PreviewCurveEvaluator.java";
    private static final String JSON_PAYLOAD_BUILDER =
            "src/main/java/com/minicad/export/json/StepRepresentationPayloadBuilder.java";

    /** Sample whose resolved entities drive the two entry points against each other. */
    private static final String SAMPLE = "plate-with-round-hole.step";

    /** A verbatim line from the interpolating body, used to find every copy of it. */
    private static final String SAMPLER_FINGERPRINT = "p0.x() + (p1.x() - p0.x()) * f";

    private static final Pattern INTERFACE_DECLARATION =
            Pattern.compile("\\binterface\\s+CurveEvaluator\\b");

    private static final String EVALUATOR_IMPORT =
            "import com.minicad.preview.sampling.CurveEvaluator;";

    @Test
    @DisplayName("the CurveEvaluator interface is declared exactly once, at top level")
    void evaluatorShouldBeDeclaredExactlyOnce() throws Exception {
        List<String> declarations = new ArrayList<>();
        for (Path file : mainSources()) {
            Matcher matcher = INTERFACE_DECLARATION.matcher(read(file));
            while (matcher.find()) {
                declarations.add(file.toString().replace('\\', '/'));
            }
        }
        assertEquals(List.of(EVALUATOR), declarations,
                "PreviewCurveEvaluator used to declare a byte-identical nested CurveEvaluator, "
                        + "which shadowed the top-level type inside that file and left the json "
                        + "pipeline unable to share it. Exactly one declaration may exist.");

        for (Class<?> nested : PreviewCurveEvaluator.class.getDeclaredClasses()) {
            assertFalse("CurveEvaluator".equals(nested.getSimpleName()),
                    "PreviewCurveEvaluator re-declared a nested CurveEvaluator: it would shadow "
                            + "the shared top-level type again and re-split the two pipelines");
        }
    }

    @Test
    @DisplayName("the public factories expose the shared type, not a nested leak")
    void publicFactoriesShouldExposeTheSharedType() {
        for (String name : List.of("curveEvaluator", "sampledCurveEvaluator")) {
            Method found = null;
            for (Method candidate : PreviewCurveEvaluator.class.getDeclaredMethods()) {
                if (candidate.getName().equals(name)) {
                    found = candidate;
                    break;
                }
            }
            assertNotNull(found, "PreviewCurveEvaluator must still declare " + name);
            assertTrue(Modifier.isStatic(found.getModifiers()),
                    name + " must stay static -- it is called on the class");
            assertSame(CurveEvaluator.class, found.getReturnType(),
                    name + " must return the shared top-level CurveEvaluator; returning "
                            + "PreviewCurveEvaluator.CurveEvaluator was the nested leak");
        }
    }

    @Test
    @DisplayName("the shared interface still carries the evaluator contract and its defaults")
    void sharedInterfaceShouldKeepItsShape() throws Exception {
        assertTrue(CurveEvaluator.class.isInterface(), "CurveEvaluator must stay an interface");
        assertTrue(Modifier.isAbstract(
                        CurveEvaluator.class.getMethod("pointAt", double.class).getModifiers()),
                "pointAt is the one abstract member -- it must not gain a default");
        assertTrue(CurveEvaluator.class.getMethod("tangentAt", double.class).isDefault(),
                "tangentAt is a shared default: both pipelines inherit the same finite-difference "
                        + "tangent, and turning it abstract would break every lambda evaluator");
        assertTrue(CurveEvaluator.class.getMethod("sample", int.class).isDefault(),
                "sample is a shared default, so a lambda evaluator can be sampled without "
                        + "declaring an implementation");
    }

    @Test
    @DisplayName("the json payload builder delegates the evaluator instead of copying it")
    void jsonPipelineShouldDelegate() throws Exception {
        String text = read(Paths.get(JSON_PAYLOAD_BUILDER));

        assertTrue(text.contains("return PreviewCurveEvaluator.curveEvaluator(curve, builder);"),
                "the json curveEvaluator must delegate to the preview evaluator: it used to "
                        + "carry its own copy of the 59-rule table and of the sampler");
        assertFalse(text.contains("sampledCurveEvaluator"),
                "a local sampler reappeared in the json payload builder; its copy was deleted "
                        + "by this convergence and only PreviewCurveEvaluator may hold it");
        assertFalse(text.contains(SAMPLER_FINGERPRINT),
                "the interpolating body came back to the json payload builder: the copy was "
                        + "deleted by this convergence and only PreviewCurveEvaluator may hold it");
        assertFalse(text.contains("new CurveEvaluator()"),
                "the json payload builder must not implement curve evaluators at all any more; "
                        + "it delegates to the preview table, which owns every evaluator body");
    }

    @Test
    @DisplayName("the sampled-curve evaluator body has exactly one home")
    void samplerBodyShouldHaveASingleHome() throws Exception {
        List<String> homes = new ArrayList<>();
        for (Path file : mainSources()) {
            if (read(file).contains(SAMPLER_FINGERPRINT)) {
                homes.add(file.toString().replace('\\', '/'));
            }
        }
        assertEquals(List.of(PREVIEW_CURVE_EVALUATOR), homes,
                "the sampler body must live in PreviewCurveEvaluator only; a second copy is "
                        + "free to drift and the two pipelines would silently diverge");
    }

    @Test
    @DisplayName("the json entry point resolves the sample's curves exactly like the preview one")
    void jsonEntryPointShouldMatchThePreviewTable() throws Exception {
        String text =
                StepTextReader.readDecoded(Files.readAllBytes(Path.of("samples", SAMPLE))).text();
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(text));
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        int compared = 0;
        for (StepEntity entity : resolved.values()) {
            Throwable fromJsonFailure = null;
            CurveEvaluator fromJson = null;
            try {
                fromJson = StepRepresentationPayloadBuilder.curveEvaluator(entity, builder);
            } catch (RuntimeException failure) {
                fromJsonFailure = failure;
            }
            Throwable fromPreviewFailure = null;
            CurveEvaluator fromPreview = null;
            try {
                fromPreview = PreviewCurveEvaluator.curveEvaluator(entity, builder);
            } catch (RuntimeException failure) {
                fromPreviewFailure = failure;
            }

            assertEquals(
                    fromPreviewFailure == null ? null : fromPreviewFailure.getClass(),
                    fromJsonFailure == null ? null : fromJsonFailure.getClass(),
                    "entity #" + entity.id() + " (" + entity.getClass().getSimpleName()
                            + ") failed differently on the json and the preview entry point");
            assertEquals(fromPreview == null, fromJson == null,
                    "the json entry point answered null (or not) differently from the preview "
                            + "one for entity #" + entity.id());
            if (fromJson == null || fromPreview == null) {
                continue;
            }
            assertEquals(fromPreview.start(), fromJson.start(), 0.0,
                    "start() differs for entity #" + entity.id());
            assertEquals(fromPreview.end(), fromJson.end(), 0.0,
                    "end() differs for entity #" + entity.id());
            double middle = (fromPreview.start() + fromPreview.end()) / 2.0;
            assertEquals(fromPreview.pointAt(middle), fromJson.pointAt(middle),
                    "pointAt(" + middle + ") differs for entity #" + entity.id());
            compared++;
        }
        assertTrue(compared >= 3,
                "the fixture must expose several curve entities, compared " + compared);
    }

    @Test
    @DisplayName("a curve that cannot be sampled has no evaluator")
    void degenerateCurveYieldsNull() {
        // Curve3.sample(int) has an empty default body, so this lambda is the
        // shortest reachable route into the "fewer than two points" branch.
        Curve3 unsamplable = parameter -> CartesianPoint.origin();
        assertNull(PreviewCurveEvaluator.sampledCurveEvaluator(unsamplable),
                "a curve with no samples has no evaluator");
    }

    @Test
    @DisplayName("the sampled evaluator keeps the shared default behaviour")
    void samplerShouldKeepTheDefaults() {
        CurveEvaluator evaluator =
                PreviewCurveEvaluator.sampledCurveEvaluator(
                        new Polyline3(List.of(p(0, 0, 0), p(1, 0, 0), p(1, 1, 0))));

        assertNotNull(evaluator, "a valid polyline must produce an evaluator");
        assertEquals(0.0, evaluator.start());
        assertEquals(1.0, evaluator.end());
        assertEquals(5, evaluator.sample(4).size(),
                "the inherited sample() default emits segments + 1 points");
        assertEquals(evaluator.pointAt(0.0), evaluator.pointAt(-3.0),
                "parameters below the domain must clamp to start()");
        assertEquals(evaluator.pointAt(1.0), evaluator.pointAt(9.0),
                "parameters above the domain must clamp to end()");
        assertTrue(evaluator.tangentAt(0.5).norm() > 0.0,
                "the inherited tangentAt default must yield a non-degenerate tangent");
    }

    @Test
    @DisplayName("no main source keeps a dead CurveEvaluator import")
    void noDeadEvaluatorImports() throws Exception {
        List<String> dead = new ArrayList<>();
        for (Path file : mainSources()) {
            String text = read(file);
            if (!text.contains(EVALUATOR_IMPORT)) {
                continue;
            }
            if (!text.replace(EVALUATOR_IMPORT, "").contains("CurveEvaluator")) {
                dead.add(file.toString().replace('\\', '/'));
            }
        }
        assertEquals(List.of(), dead,
                "StepPreviewJsonExporter used to import CurveEvaluator without ever referring "
                        + "to it; a dead explicit import is back. Drop the import or use it.");
    }

    private static List<Path> mainSources() throws IOException {
        try (Stream<Path> files = Files.walk(Paths.get("src/main/java"))) {
            return files.filter(path -> path.toString().endsWith(".java")).toList();
        }
    }

    private static CartesianPoint p(double x, double y, double z) {
        return new CartesianPoint(x, y, z);
    }

    private static String read(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }
}
