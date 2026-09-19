package com.minicad.architecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.minicad.builder.CompiledStepDocument;
import com.minicad.builder.StepCapabilityRegistry;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Transformation3;
import com.minicad.geometry.Vector3;
import com.minicad.geometry2d.Direction2;
import com.minicad.geometry2d.Parabola2;
import com.minicad.geometry2d.Point2;
import com.minicad.helper.StepMetadataExtractor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pins the retirement of members that nothing in the tree ever called.
 *
 * <p>This package exists for assertions that belong to no single package: the
 * convergence rounds each delete the copies of one algorithm, but the same
 * reference-counting sweep also turns up members with no caller at all, spread
 * across {@code builder}, {@code geometry}, {@code geometry2d}, {@code helper},
 * {@code export} and {@code preview}. Those were removed in one pass, and a
 * single guard over all of them beats six guards that would each restate the
 * same rule.</p>
 *
 * <p>Two shapes dominate, and they are pinned differently.
 * {@linkplain #theBeanSpellingIsGone The first} is a value object that declares
 * the same accessor twice — once in the bean spelling and once in the bare one —
 * where only the bare one has ever been called; that is asserted by reflection,
 * because the retired name is also explained in javadoc and a text scan would
 * trip over its own explanation. The second is a member with no sibling at all,
 * asserted by source scan, each paired with the live text that carries the same
 * capability so a sweep that deleted too much cannot pass.</p>
 *
 * <p>{@code tools/scan_dead_members.py} finds these, and it is deliberately
 * conservative: it counts <em>mentions of the name</em>, so a member whose name
 * also exists on an unrelated class is reported live. {@code Capability.getLevel}
 * and {@code PreviewGlbBuilder.accessorCount} were both hidden that way — their
 * names appear on {@code StepApproval} and as a local variable in a model test.
 * The wave-1 list is therefore a lower bound, which is why these retirements
 * were confirmed by qualified-call grep on top of it.</p>
 */
class RetiredAccessorConvergenceTest {

    private static final String GLB_BUILDER =
            "src/main/java/com/minicad/export/glb/PreviewGlbBuilder.java";

    private static final String STATISTICS_HELPER =
            "src/main/java/com/minicad/preview/statistics/PreviewStatisticsHelper.java";

    private static final String AXIS_PLACEMENT =
            "src/main/java/com/minicad/geometry/Axis2Placement3D.java";

    private static final String DIRECTION3 = "src/main/java/com/minicad/geometry/Direction3.java";

    private static final String PARABOLA2 = "src/main/java/com/minicad/geometry2d/Parabola2.java";

    /** The bean spelling: a getter or an is-getter. */
    private static final Pattern BEAN_SPELLING = Pattern.compile("^(get|is)[A-Z][A-Za-z0-9]*$");

    /** A matrix element accessor, {@code getM00} through {@code getM33}. */
    private static final Pattern MATRIX_ELEMENT = Pattern.compile("^getM[0-3][0-3]$");

    @Test
    @DisplayName("the bean spelling is gone from the value objects that declare a bare one")
    void theBeanSpellingIsGone() {
        assertDeclaresNoBeanSpelling(StepCapabilityRegistry.Capability.class);
        assertDeclaresNoBeanSpelling(CompiledStepDocument.class);
        assertDeclaresNoBeanSpelling(StepMetadataExtractor.DisplayMetadata.class);
    }

    @Test
    @DisplayName("the bare spellings those value objects kept are all still declared")
    void theBareSpellingsSurvive() {
        assertDeclares(StepCapabilityRegistry.Capability.class,
                List.of("entity", "level", "parsed", "resolved", "built", "exported", "tested", "limitations"));
        assertDeclares(CompiledStepDocument.class, List.of("stepText", "stepFile", "resolved", "builder"));
        assertDeclares(StepMetadataExtractor.DisplayMetadata.class,
                List.of("rgb", "layers", "transparency", "pbr"));
    }

    @Test
    @DisplayName("Transformation3 declares no matrix-element accessor, and keeps its algebra")
    void transformation3KeepsItsAlgebra() {
        List<String> survivors = new ArrayList<>();
        for (Method method : Transformation3.class.getDeclaredMethods()) {
            if (isRealMember(method) && MATRIX_ELEMENT.matcher(method.getName()).matches()) {
                survivors.add(method.getName() + "()");
            }
        }
        assertEquals(List.of(), survivors,
                "Transformation3 grew a matrix-element accessor back. Its callers go through "
                        + "transform/compose/inverse; exposing the sixteen elements one by one is "
                        + "how a caller starts hand-multiplying matrices instead.");

        for (String kept : List.of("identity", "translation", "scale", "rotationX", "rotationY",
                "rotationZ", "compose", "transform", "inverse", "from", "at")) {
            assertTrue(declaresAnyMethod(Transformation3.class, kept),
                    "Transformation3 lost " + kept + "(). Removing the dead element accessors was "
                            + "not supposed to touch the algebra they were reading.");
        }
    }

    @Test
    @DisplayName("the counters nothing read are gone, and the glb normals path stayed")
    void theGlbCountersAreGone() throws Exception {
        assertDeclaresNo("PreviewGlbBuilder", GLB_BUILDER,
                List.of("nodeCount", "materialCount", "accessorCount", "bufferViewCount", "triangleNormals"));
        assertCodeContains(GLB_BUILDER, "indexedTriangleMesh(",
                "the per-vertex normal computation lived in the retired triangleNormals() as well as "
                        + "in indexedTriangleMesh(); the indexed path is the live one and must stay.");
        assertCodeContains(GLB_BUILDER, "normalSums", "indexedTriangleMesh no longer accumulates normals.");
    }

    @Test
    @DisplayName("the isolated dead members are gone, each with its capability still reachable")
    void theIsolatedRetirementsAreGone() throws Exception {
        assertDeclaresNo("PreviewStatisticsHelper", STATISTICS_HELPER, List.of("countEntities"));
        assertCodeContains(STATISTICS_HELPER, "countSolidEntities(",
                "countEntities was the unfiltered sibling; the typed counts are what the exporter asks for.");
        assertCodeContains(STATISTICS_HELPER, "countShells(", "the typed counts must survive the sweep.");

        assertDeclaresNo("Axis2Placement3D", AXIS_PLACEMENT, List.of("withOrigin"));
        assertDeclaresNo("Direction3", DIRECTION3, List.of("crossDirection"));
        assertDeclaresNo("Parabola2", PARABOLA2, List.of("withVertex"));

        // Each of those three was a one-line convenience over something public
        // that is still there. Assert the replacement, not the source text: a
        // caller rebuilding the convenience must still be able to.
        assertConstructor(Axis2Placement3D.class, CartesianPoint.class, Direction3.class, Direction3.class);
        assertMethod(Direction3.class, "from", Vector3.class);
        assertMethod(Direction3.class, "cross", Direction3.class);
        assertConstructor(Parabola2.class, Point2.class, Direction2.class, double.class);
    }

    // ─── helpers ─────────────────────────────────────────────────────────

    private static void assertDeclaresNoBeanSpelling(Class<?> type) {
        List<String> survivors = new ArrayList<>();
        for (Method method : type.getDeclaredMethods()) {
            if (isRealMember(method) && BEAN_SPELLING.matcher(method.getName()).matches()) {
                survivors.add(method.getName() + "()");
            }
        }
        assertEquals(List.of(), survivors,
                type.getSimpleName() + " declares a bean-spelling accessor again. Each of these "
                        + "objects already exposes the same property under the bare spelling its "
                        + "callers use, so the bean form is a second door nobody has ever opened.");
    }

    private static void assertDeclares(Class<?> type, List<String> names) {
        List<String> missing = new ArrayList<>();
        for (String name : names) {
            if (!declaresAnyMethod(type, name)) {
                missing.add(name + "()");
            }
        }
        assertEquals(List.of(), missing,
                type.getSimpleName() + " lost a bare accessor. Retiring the bean spelling is only "
                        + "correct while the bare spelling is the one in use; if it went too, the "
                        + "property lost its only door.");
    }

    private static void assertDeclaresNo(String typeName, String path, List<String> names) throws Exception {
        String text = code(Paths.get(path));
        List<String> found = new ArrayList<>();
        for (String name : names) {
            if (text.contains(name + "(")) {
                found.add(name);
            }
        }
        assertEquals(List.of(), found,
                typeName + " declares a member nothing calls again. These are only safe to leave "
                        + "out while no caller exists; if one appeared, delete the copy or give it "
                        + "the caller, but do not leave both.");
    }

    private static void assertCodeContains(String path, String needle, String why) throws Exception {
        String text = code(Paths.get(path));
        assertTrue(text.contains(needle),
                Paths.get(path).getFileName() + " no longer contains '" + needle + "': " + why);
    }

    private static void assertMethod(Class<?> type, String name, Class<?>... parameters) {
        try {
            type.getDeclaredMethod(name, parameters);
        } catch (NoSuchMethodException missing) {
            fail(type.getSimpleName() + "." + name + " is gone, so the retired convenience has no "
                    + "live replacement to rebuild it from.");
        }
    }

    private static void assertConstructor(Class<?> type, Class<?>... parameters) {
        try {
            type.getConstructor(parameters);
        } catch (NoSuchMethodException missing) {
            fail(type.getSimpleName() + " lost its full constructor, which was the live replacement "
                    + "for the retired with-style convenience.");
        }
    }

    private static boolean declaresAnyMethod(Class<?> type, String name) {
        for (Method method : type.getDeclaredMethods()) {
            if (isRealMember(method) && method.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * JaCoCo injects {@code $jacocoInit} and Kotlin-style synthetics into the
     * classes it instruments, and those would otherwise be read as declared
     * members; {@code mvn test} would pass where {@code verify} fails.
     */
    private static boolean isRealMember(Method method) {
        return !method.isSynthetic() && !method.isBridge() && !method.getName().startsWith("$");
    }

    /** Strips comments, so a retired name explained in javadoc does not count as a declaration. */
    private static String code(Path path) throws IOException {
        if (!Files.exists(path)) {
            fail("Cannot read " + path.toAbsolutePath() + " to verify the retirement guard.");
        }
        String text = Files.readString(path, StandardCharsets.UTF_8);
        return text.replaceAll("(?s)/\\*.*?\\*/", "").replaceAll("(?m)//[^\r\n]*", "");
    }
}
