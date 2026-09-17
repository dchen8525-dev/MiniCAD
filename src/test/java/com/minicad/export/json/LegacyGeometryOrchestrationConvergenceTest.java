package com.minicad.export.json;

import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepCylinderVolume;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepExtrudedFaceSolid;
import com.minicad.step.model.StepFacetedBrepAndBrepWithVoids;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepPolyline;
import com.minicad.step.model.StepTessellatedFace;
import com.minicad.step.model.StepTorusVolume;
import com.minicad.step.model.StepTriangulatedFace;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the legacy-geometry orchestration convergence.
 *
 * <p>{@code StepLegacyGeometryBuilder} was extracted out of the {@code preview}
 * package on 2026-08-26, and the four methods it took with it --
 * {@code buildLegacyGeometry}, {@code buildGeometryForShells},
 * {@code buildGeometryForSolids} and {@code mergeGeometry} -- were left behind in
 * {@code PreviewGeometryCollector} as a second copy. The copy was reachable only
 * through four {@code PreviewFaceBuilder} facades that nothing called, so the
 * duplication was invisible to call-graph review; only the source text showed it.
 *
 * <p>The copies were compared body-for-body (whitespace-normalised, and again with
 * {@code com.minicad.*} qualifiers and the {@code public} modifier erased) before
 * anything moved. {@code mergeGeometry} was identical. The other three differed
 * only in fully-qualified names and delegating aliases <em>except</em> for one
 * real divergence: the export-side copy buckets
 * {@code StepFacetedBrepAndBrepWithVoids} as a solid and the preview copy did not,
 * because {@code 91269f9a} widened only the copy that had callers. So the two
 * copies would have answered differently for that entity, which is why this round
 * deleted the copy instead of re-synchronising it.
 *
 * <p>The same commit's follow-up extracted the 38-line inline {@code instanceof}
 * chain in the surviving {@code buildLegacyGeometry} into the named predicate
 * {@code isLegacyGeometrySolidItem}. That is the whole point of the second half of
 * this guard: the rule now has a name, so it can be asserted directly, and its
 * relationship to {@link StepValidationHelper#isRepresentationSolidItem} is pinned
 * as a strict superset. Those two are deliberately <em>different questions</em>
 * ("would a representation list this as a solid item?" versus "does the legacy
 * geometry pipeline bucket this as solid geometry?"), so the 12 types this rule
 * adds are enumerated here. Folding one into the other is the tempting wrong fix
 * and would silently drop the primitive volumes from the solid bucket.
 *
 * <p>A later round finished the file off. With the orchestration gone, what
 * {@code PreviewGeometryCollector} still held was a dead twin of every rule the
 * export side already owned -- the standalone-edge table, the mapped-annotation
 * carrier table, the five mapped-representation helpers -- plus the shell-like id
 * walk, whose only live caller was {@code StepLegacyGeometryBuilder}. So the class
 * was deleted, the walk was inlined into that caller, and the eleven
 * {@code PreviewFaceBuilder} entry points that forwarded into it (seven facades and
 * the four helpers that existed only to serve them) went with it.
 */
class LegacyGeometryOrchestrationConvergenceTest {

    private static final Path MAIN_SOURCES = Paths.get("src/main/java");
    private static final Path STEP_LEGACY =
            Paths.get("src/main/java/com/minicad/export/json/StepLegacyGeometryBuilder.java");
    /**
     * The preview-side twin of this orchestration. It is deleted -- every member it still
     * held was either a dead twin of a live export-side rule or the shell-like id walk --
     * so the guard asserts the path stays absent instead of reading it.
     */
    private static final Path DELETED_PREVIEW_COLLECTOR =
            Paths.get("src/main/java/com/minicad/preview/builder/PreviewGeometryCollector.java");
    private static final Path PREVIEW_FACE_BUILDER =
            Paths.get("src/main/java/com/minicad/preview/builder/PreviewFaceBuilder.java");
    private static final Path VALIDATION_HELPER =
            Paths.get("src/main/java/com/minicad/export/json/StepValidationHelper.java");

    /** The orchestration whose single home is {@code StepLegacyGeometryBuilder}. */
    private static final List<String> ORCHESTRATION = List.of(
            "buildLegacyGeometry",
            "buildGeometryForShells",
            "buildGeometryForSolids",
            "mergeGeometry");

    /**
     * The 39 types the named predicate lists, in source order. The order is the
     * order the original inline chain used, which is why it is frozen here rather
     * than read from the predicate and mirrored back.
     */
    private static final List<String> LEGACY_SOLID_TYPES = List.of(
            "StepSweptAreaSolid",
            "StepSolidReplica",
            "StepCsgSolid",
            "StepCsgPrimitive",
            "StepBooleanClippingResult",
            "StepBooleanResult",
            "StepSweptDiskSolid",
            "StepExtrudedAreaSolidTapered",
            "StepRevolvedAreaSolidTapered",
            "StepSurfaceCurveSweptAreaSolid",
            "StepPolygonalBoundedHalfSpace",
            "StepComplexClippingResult",
            "StepHalfSpaceSolid",
            "StepCsgVolume",
            "StepBlockVolume",
            "StepFiniteElementMesh",
            "StepFlatPattern",
            "StepBrepWithVoids",
            "StepFacetedBrepAndBrepWithVoids",
            "StepManifoldSolidBrep",
            "StepFacettedBrep",
            "StepNonManifoldSolidBrep",
            "StepAdvancedBrep",
            "StepMappedItem",
            "StepSolidModel",
            "StepSurfacePatch",
            "StepExtrudedFaceSolid",
            "StepRevolvedFaceSolid",
            "StepSweptFaceSolid",
            "StepCylinderVolume",
            "StepSphereVolume",
            "StepTorusVolume",
            "StepPrismVolume",
            "StepRightCircularConeVolume",
            "StepTessellatedFace",
            "StepTessellatedFaceSet",
            "StepTriangulatedFace",
            "StepComplexTriangulatedFace",
            "StepCubicBezierTriangulatedFace");

    /**
     * The 12 types the legacy rule adds over {@code isRepresentationSolidItem},
     * in the order they appear in the predicate. {@code StepFacetedBrepAndBrepWithVoids}
     * is the one the deleted preview copy was missing.
     */
    private static final List<String> LEGACY_ONLY_SOLID_TYPES = List.of(
            "StepFacetedBrepAndBrepWithVoids",
            "StepExtrudedFaceSolid",
            "StepRevolvedFaceSolid",
            "StepSweptFaceSolid",
            "StepCylinderVolume",
            "StepSphereVolume",
            "StepTorusVolume",
            "StepPrismVolume",
            "StepRightCircularConeVolume",
            "StepTriangulatedFace",
            "StepComplexTriangulatedFace",
            "StepCubicBezierTriangulatedFace");

    @Test
    @DisplayName("the preview twin of the legacy-geometry home stays deleted")
    void previewCopiesStayDeleted() throws IOException {
        assertFalse(Files.exists(DELETED_PREVIEW_COLLECTOR),
                "PreviewGeometryCollector is back. After the orchestration moved out, what it "
                        + "still held was the standalone-edge table, the mapped-annotation "
                        + "carrier table and the five mapped-representation helpers -- all dead "
                        + "twins of live export-side rules -- plus the shell-like id walk, which "
                        + "now lives in StepLegacyGeometryBuilder. A second copy is exactly what "
                        + "drifted the last time.");

        List<String> reappeared = new ArrayList<>();
        String text = read(PREVIEW_FACE_BUILDER);
        for (String method : ORCHESTRATION) {
            if (declares(text, method)) {
                reappeared.add(PREVIEW_FACE_BUILDER.getFileName() + "#" + method);
            }
        }
        assertEquals(List.of(), reappeared,
                "the legacy-geometry orchestration has one home, StepLegacyGeometryBuilder. "
                        + "A re-declared "
                        + reappeared + " is a second copy, and the last one drifted: it never "
                        + "received the StepFacetedBrepAndBrepWithVoids bucket entry, so it "
                        + "classified that entity differently from the copy the live callers used. "
                        + "Route the call site at StepLegacyGeometryBuilder instead.");
    }

    @Test
    @DisplayName("the shell-like id walk has exactly one home")
    void shellLikeIdWalkHasOneHome() throws IOException {
        List<String> carriers = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(MAIN_SOURCES)) {
            for (Path source : walk.filter(path -> path.toString().endsWith(".java"))
                    .collect(Collectors.toList())) {
                if (declares(read(source), "collectShellLikeIds")) {
                    carriers.add(source.getFileName().toString());
                }
            }
        }
        assertEquals(List.of("StepLegacyGeometryBuilder.java"), carriers,
                "collectShellLikeIds is declared in " + carriers + ". The preview package used "
                        + "to carry a second copy behind a facade nothing called, and this side "
                        + "was one line forwarding to it; the body now lives next to the "
                        + "pipeline that drives it, and a second declaration means the twin is "
                        + "growing back.");
        assertTrue(declares(read(STEP_LEGACY), "collectShellLikeIds"),
                "StepLegacyGeometryBuilder must keep the walk it now owns");
    }

    @Test
    @DisplayName("PreviewFaceBuilder no longer forwards into a preview-side collector")
    void delegatingFacadesStayDeleted() throws IOException {
        String text = read(PREVIEW_FACE_BUILDER);
        assertFalse(text.contains("PreviewGeometryCollector."),
                "PreviewFaceBuilder calls PreviewGeometryCollector again. The seven facades "
                        + "that did had no caller outside their own chain, so they pinned a "
                        + "second copy of rules whose homes are on the export side. Route the "
                        + "call site at StepLegacyGeometryBuilder or "
                        + "StepRepresentationPayloadBuilder instead.");
    }

    @Test
    @DisplayName("StepLegacyGeometryBuilder keeps all four methods, public where tests drive them")
    void canonicalHomeKeepsTheOrchestration() throws Exception {
        String text = read(STEP_LEGACY);
        for (String method : ORCHESTRATION) {
            assertTrue(declares(text, method),
                    "StepLegacyGeometryBuilder must stay the home of " + method);
        }
        assertPublicStatic(StepLegacyGeometryBuilder.class, "buildLegacyGeometry");
        assertPublicStatic(StepLegacyGeometryBuilder.class, "mergeGeometry");
    }

    @Test
    @DisplayName("the solid bucket rule keeps its name and its 39 types in source order")
    void legacySolidRuleKeepsItsTypes() throws IOException {
        assertEquals(LEGACY_SOLID_TYPES,
                instanceofTypes(read(STEP_LEGACY), "isLegacyGeometrySolidItem"),
                "the legacy solid bucket is a named rule now; a shorter list means types were "
                        + "dropped from the solid bucket (they would then be built as shells), and "
                        + "a reordered list means the predicate was rewritten rather than edited.");
    }

    @Test
    @DisplayName("the legacy solid rule is exactly the representation-solid list plus twelve types")
    void legacySolidRuleIsAStrictSuperset() throws IOException {
        List<String> legacy = instanceofTypes(read(STEP_LEGACY), "isLegacyGeometrySolidItem");
        List<String> representation =
                instanceofTypes(read(VALIDATION_HELPER), "isRepresentationSolidItem");

        assertTrue(legacy.containsAll(representation),
                "every type the representation-solid classifier accepts must also be bucketed as a "
                        + "legacy solid; missing: " + difference(representation, legacy));
        assertEquals(LEGACY_ONLY_SOLID_TYPES, difference(legacy, representation),
                "the types this rule adds over isRepresentationSolidItem changed. That margin is "
                        + "the entire reason the two classifiers are kept apart, so it must move "
                        + "deliberately rather than by a list being copy-pasted over its sibling.");
        assertNotEquals(representation, legacy,
                "if the two lists became equal, one of them is being maintained as a copy of the "
                        + "other -- which is the duplication this round removed.");
    }

    @Test
    @DisplayName("the types the legacy rule adds over the representation classifier answer true")
    void everyLegacyOnlyTypeIsBucketedAsASolid() {
        StepEntity origin = new StepCartesianPoint(1, "origin", List.of(0.0, 0.0, 0.0));
        List<StepEntity> legacyOnly = List.of(
                new StepFacetedBrepAndBrepWithVoids(2, "fbbwv", origin, List.of()),
                new StepExtrudedFaceSolid(3, "efs", origin, origin, origin, 1.0),
                new StepCylinderVolume(4, "cyl", origin, 1.0, 2.0),
                new StepTorusVolume(5, "tor", origin, 2.0, 0.5),
                new StepTriangulatedFace(6, "tri", List.of(), List.of()),
                new StepTessellatedFace(7, "tf", List.of()));

        for (StepEntity entity : legacyOnly) {
            assertTrue(StepLegacyGeometryBuilder.isLegacyGeometrySolidItem(entity),
                    entity.getClass().getSimpleName() + " is one of the types the legacy solid "
                            + "bucket adds, so it must be bucketed as a solid");
        }
    }

    @Test
    @DisplayName("the two solid classifiers stay different questions")
    void theTwoSolidClassifiersStayIndependent() {
        StepEntity origin = new StepCartesianPoint(11, "origin", List.of(0.0, 0.0, 0.0));
        StepEntity cylinder = new StepCylinderVolume(12, "cyl", origin, 1.0, 2.0);

        assertTrue(StepLegacyGeometryBuilder.isLegacyGeometrySolidItem(cylinder),
                "the legacy pipeline builds a solid for a primitive volume");
        assertFalse(StepValidationHelper.isRepresentationSolidItem(cylinder),
                "and a representation still does not list it as a solid item. Folding the wide "
                        + "rule into the narrow one would drop the primitive volumes, the "
                        + "face-backed solids and the triangulated faces from the solid bucket.");

        assertTrue(StepValidationHelper.isRepresentationSolidItem(
                new StepManifoldSolidBrep(13, "brep", origin)));
        assertTrue(StepLegacyGeometryBuilder.isLegacyGeometrySolidItem(
                new StepManifoldSolidBrep(14, "brep", origin)),
                "the narrow list's members are also legacy solids");
    }

    @Test
    @DisplayName("the legacy solid rule stays narrow: points, wireframe and null are not solids")
    void legacySolidRuleStaysNarrow() {
        List<StepEntity> notSolids = List.of(
                new StepCartesianPoint(21, "p", List.of(0.0, 0.0, 0.0)),
                new StepPolyline(22, "pl", List.of()));
        for (StepEntity entity : notSolids) {
            assertFalse(StepLegacyGeometryBuilder.isLegacyGeometrySolidItem(entity),
                    entity.getClass().getSimpleName() + " is not solid geometry; widening the rule "
                            + "must not have turned wireframe or points into solids");
        }
        assertFalse(StepLegacyGeometryBuilder.isLegacyGeometrySolidItem(null),
                "a null entity is not bucketed as a solid");
    }

    private static List<String> difference(List<String> from, List<String> without) {
        return from.stream().filter(type -> !without.contains(type)).collect(Collectors.toList());
    }

    private static void assertPublicStatic(Class<?> owner, String name) {
        Method found = null;
        for (Method candidate : owner.getDeclaredMethods()) {
            if (candidate.getName().equals(name)) {
                found = candidate;
                break;
            }
        }
        assertNotNull(found, owner.getSimpleName() + " must still declare " + name);
        assertTrue(Modifier.isStatic(found.getModifiers()),
                name + " must stay static -- it is called on the class");
        assertTrue(Modifier.isPublic(found.getModifiers()),
                name + " must stay public -- the preview-side tests drive it across packages");
    }

    /**
     * The {@code instanceof} types of a one-argument boolean method, in source
     * order. Read from the source rather than reflection because the order is part
     * of what the original chain fixed, and reflection does not preserve it.
     */
    private static List<String> instanceofTypes(String text, String method) {
        Pattern signature = Pattern.compile(
                "(?m)^\\s*(?:public |private |protected )?(?:static )?(?:final )?"
                        + "[\\w<>\\[\\], .]+\\s+" + Pattern.quote(method) + "\\s*\\([^)]*\\)\\s*\\{");
        Matcher matcher = signature.matcher(text);
        assertTrue(matcher.find(), "the source must declare " + method);
        int open = text.indexOf('{', matcher.start());
        int depth = 0;
        int close = -1;
        for (int i = open; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    close = i;
                    break;
                }
            }
        }
        assertTrue(close > open, "unbalanced body for " + method);

        List<String> types = new ArrayList<>();
        Matcher type = Pattern.compile("instanceof\\s+([\\w.]+)").matcher(text.substring(open, close));
        while (type.find()) {
            String raw = type.group(1);
            int dot = raw.lastIndexOf('.');
            types.add(dot < 0 ? raw : raw.substring(dot + 1));
        }
        return types;
    }

    private static String read(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    private static boolean declares(String text, String name) {
        Pattern declaration = Pattern.compile(
                "(?m)^\\s*(?:public |private |protected )?(?:static )?[\\w<>\\[\\], .]+\\s+"
                        + Pattern.quote(name) + "\\s*\\(");
        return declaration.matcher(text).find();
    }
}
