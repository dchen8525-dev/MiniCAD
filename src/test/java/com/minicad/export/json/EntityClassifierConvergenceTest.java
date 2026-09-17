package com.minicad.export.json;

import com.minicad.step.model.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the entity-classifier convergence.
 *
 * <p>Three classifiers that decide what a STEP entity <em>is</em> had drifted
 * into four declarations across three classes, and the copies had stopped
 * agreeing:
 *
 * <ul>
 *   <li>{@code isStandaloneEdgeSource} had a live 29-type copy that matched the
 *       home byte for byte, plus a facade nothing called;</li>
 *   <li>{@code isRepresentationSolidItem} had <em>two</em> live copies that
 *       agreed on 27 types, while the {@code StepValidationHelper} home held
 *       only the first 13 <em>and had no caller at all</em> -- the reachable
 *       answer and the declared answer had already diverged by 14 types, so any
 *       future caller of the home would have got a quietly wrong answer for
 *       tessellated faces, FEA meshes, flat patterns, mapped items, surface
 *       patches, the tapered sweeps and the CSG volumes;</li>
 *   <li>{@code isSampledCurveSource} was reached through two such indirections
 *       -- a facade in {@code PreviewFaceBuilder} and a private stub in
 *       {@code StepEdgePayloadBuilder}, each a single line, each sitting in the
 *       same package as the one caller it served.</li>
 * </ul>
 *
 * <p>So this convergence is not only de-duplication: it makes the single home
 * carry the semantics the callers were already getting. The 27-type body was
 * moved verbatim, so no live path changes behaviour -- only the unreachable
 * method changes its answer, and it changes it to the one its callers expect.
 *
 * <p>The guard matters because convergence regresses silently: a later edit can
 * paste a copy back and every other test still passes, because the copies agree
 * until they drift. It pins the single home, the canonical modifiers, the frozen
 * type lists (a shorter list here is the narrowing coming back) and the answers
 * on real entities.
 */
class EntityClassifierConvergenceTest {

    private static final Path MAIN_SOURCES = Paths.get("src/main/java");
    private static final Path VALIDATION_HELPER =
            Paths.get("src/main/java/com/minicad/export/json/StepValidationHelper.java");
    private static final Path FACE_BUILDER =
            Paths.get("src/main/java/com/minicad/preview/builder/PreviewFaceBuilder.java");

    /** The classifiers that must exist exactly once, in {@link StepValidationHelper}. */
    private static final List<String> CLASSIFIERS = List.of(
            "isStandaloneEdgeSource",
            "isRepresentationSolidItem",
            "isSampledCurveSource");

    private static final String HOME = "StepValidationHelper.java";

    /**
     * The 27 types the two deleted copies agreed on, in declaration order. The
     * last 14 of these were missing from the home before the convergence, which
     * is precisely the repair being guarded.
     */
    private static final List<String> REPRESENTATION_SOLID_TYPES = List.of(
            "StepManifoldSolidBrep",
            "StepFacettedBrep",
            "StepNonManifoldSolidBrep",
            "StepAdvancedBrep",
            "StepBrepWithVoids",
            "StepSweptAreaSolid",
            "StepSolidReplica",
            "StepCsgSolid",
            "StepCsgPrimitive",
            "StepBooleanClippingResult",
            "StepBooleanResult",
            "StepTessellatedFaceSet",
            "StepTessellatedFace",
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
            "StepMappedItem",
            "StepSolidModel",
            "StepSurfacePatch");

    /** The 29 types the single home lists, in declaration order. */
    private static final List<String> STANDALONE_EDGE_TYPES = List.of(
            "StepPolyline",
            "StepGeometricCurveSet",
            "StepGeometricSet",
            "StepShellBasedWireframeModel",
            "StepEdgeBasedWireframeModel",
            "StepConnectedEdgeSet",
            "StepEdgeWire",
            "StepPath",
            "StepOpenPath",
            "StepSubpath",
            "StepOrientedPath",
            "StepWireShell",
            "StepAnnotationCurveOccurrence",
            "StepAnnotationFillArea",
            "StepAnnotationFillAreaOccurrence",
            "StepAnnotationSymbol",
            "StepAnnotationSymbolOccurrence",
            "StepAnnotationSubfigureOccurrence",
            "StepFilletEdge",
            "StepChamferEdge",
            "StepSubedge",
            "StepAnnotationText",
            "StepAnnotationTextCharacter",
            "StepDimensionCurve",
            "StepLeaderCurve",
            "StepProjectionCurve",
            "StepDraughtingAnnotationOccurrence",
            "StepTerminatorSymbol",
            "StepGeometricSurfaceSet");

    @Test
    @DisplayName("each classifier is declared exactly once, in StepValidationHelper")
    void classifiersHaveExactlyOneHome() throws IOException {
        Map<String, List<String>> declaringFiles = new HashMap<>();
        for (String classifier : CLASSIFIERS) {
            declaringFiles.put(classifier, new ArrayList<>());
        }

        List<Path> sources = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(MAIN_SOURCES)) {
            walk.filter(path -> path.toString().endsWith(".java")).forEach(sources::add);
        }
        for (Path source : sources) {
            String text = read(source);
            for (String classifier : CLASSIFIERS) {
                if (declares(text, classifier)) {
                    declaringFiles.get(classifier).add(source.getFileName().toString());
                }
            }
        }

        for (String classifier : CLASSIFIERS) {
            assertEquals(List.of(HOME), declaringFiles.get(classifier),
                    classifier + " must be declared in exactly one place, " + HOME + ". A second "
                            + "declaration is a copy that will drift from the home -- and the last "
                            + "time that happened the two answers differed by 14 types while every "
                            + "test still passed. Route the call site at StepValidationHelper "
                            + "instead of declaring a local or facade copy.");
        }
    }

    @Test
    @DisplayName("no classifier facade grows back in PreviewFaceBuilder")
    void classifierFacadesStayDeleted() throws IOException {
        String text = read(FACE_BUILDER);
        List<String> reappeared = new ArrayList<>();
        for (String classifier : CLASSIFIERS) {
            if (declares(text, classifier)) {
                reappeared.add(classifier);
            }
        }
        assertEquals(List.of(), reappeared,
                "PreviewFaceBuilder declares " + reappeared + " again. These were facades with no "
                        + "caller at all, which is the opposite of why the five real facades in "
                        + "this class were kept -- the preview call sites name StepValidationHelper "
                        + "directly.");
    }

    @Test
    @DisplayName("the classifier home stays public static and reachable across packages")
    void canonicalHomeStaysPublicStatic() {
        assertPublicStatic(StepValidationHelper.class, "isStandaloneEdgeSource");
        assertPublicStatic(StepValidationHelper.class, "isRepresentationSolidItem");
        assertPublicStatic(StepValidationHelper.class, "isSampledCurveSource");
    }

    @Test
    @DisplayName("the representation-solid list keeps all 27 types its callers relied on")
    void representationSolidListKeepsEveryTypeItsCallersReliedOn() throws IOException {
        assertEquals(REPRESENTATION_SOLID_TYPES,
                instanceofTypes(read(VALIDATION_HELPER), "isRepresentationSolidItem"),
                "the home must keep the union of what it and the two deleted copies said. The 14 "
                        + "types at the tail (tessellated face sets and faces, FEA meshes, flat "
                        + "patterns, mapped items, surface patches, the tapered sweeps and the CSG "
                        + "volumes) are exactly what this convergence repaired, so a shorter list "
                        + "here is the narrowing coming back.");
    }

    @Test
    @DisplayName("the standalone-edge list keeps all 29 types")
    void standaloneEdgeListKeepsEveryType() throws IOException {
        assertEquals(STANDALONE_EDGE_TYPES,
                instanceofTypes(read(VALIDATION_HELPER), "isStandaloneEdgeSource"),
                "the home absorbed the copy byte for byte; any change here is a silent behaviour "
                        + "change for wireframe, annotation and curve-set entities.");
    }

    @Test
    @DisplayName("every type the deleted copies called a solid still answers true")
    void everyTypeTheDeletedCopiesCalledASolidStillDoes() {
        List<StepEntity> solids = addedSolidTypes();
        for (StepEntity entity : solids) {
            assertTrue(StepValidationHelper.isRepresentationSolidItem(entity),
                    entity.getClass().getSimpleName() + " was a solid in both deleted copies and "
                            + "must stay a solid in the single home");
        }
        assertEquals(14, solids.size(),
                "the guard must cover every one of the 14 types the copies added over the old "
                        + "13-type home");
    }

    @Test
    @DisplayName("the solid classifier still covers the types the old home already had")
    void theOriginalSolidTypesStillAnswerTrue() {
        StepEntity origin = new StepCartesianPoint(1, "origin", List.of(0.0, 0.0, 0.0));
        assertTrue(StepValidationHelper.isRepresentationSolidItem(
                new StepManifoldSolidBrep(2, "brep", origin)),
                "a manifold solid brep is the archetypal solid");
        assertTrue(StepValidationHelper.isRepresentationSolidItem(new StepSolidModel(3, "solid")),
                "StepSolidModel was in the original 13-type list");
    }

    @Test
    @DisplayName("the solid classifier stays narrow: wireframe, points and null are not solids")
    void solidClassifierStaysNarrow() {
        List<StepEntity> notSolids = List.of(
                new StepCartesianPoint(90, "p", List.of(0.0, 0.0, 0.0)),
                new StepPolyline(91, "pl", List.of()),
                new StepGeometricCurveSet(92, "gcs", List.of()));
        for (StepEntity entity : notSolids) {
            assertFalse(StepValidationHelper.isRepresentationSolidItem(entity),
                    entity.getClass().getSimpleName() + " is not solid geometry; widening the list "
                            + "must not have turned wireframe or points into solids");
        }
        assertFalse(StepValidationHelper.isRepresentationSolidItem(null),
                "a null entity is not a solid");
    }

    @Test
    @DisplayName("the two classifiers disagree about a polyline and agree about nothing solid")
    void theTwoClassifiersStayIndependent() {
        StepEntity polyline = new StepPolyline(4, "pl", List.of());
        assertTrue(StepValidationHelper.isStandaloneEdgeSource(polyline),
                "a polyline is wireframe, so it is a standalone edge source");
        assertFalse(StepValidationHelper.isRepresentationSolidItem(polyline),
                "and it is not solid geometry -- the two classifiers are not the same question");

        assertTrue(StepValidationHelper.isStandaloneEdgeSource(
                new StepGeometricCurveSet(5, "gcs", List.of())));
        assertFalse(StepValidationHelper.isStandaloneEdgeSource(
                new StepCartesianPoint(6, "p", List.of(0.0, 0.0, 0.0))));
        assertFalse(StepValidationHelper.isStandaloneEdgeSource(null),
                "a null entity is not a standalone edge source");
    }

    private static List<StepEntity> addedSolidTypes() {
        StepEntity origin = new StepCartesianPoint(10, "origin", List.of(0.0, 0.0, 0.0));
        List<StepEntity> solids = List.of(
                new StepTessellatedFaceSet(11, "tfs", List.of(), List.of()),
                new StepTessellatedFace(12, "tf", List.of()),
                new StepExtrudedAreaSolidTapered(13, "eat", origin, null, 1.0, 0.1),
                new StepRevolvedAreaSolidTapered(14, "rat", origin, null, 1.0, 0.1),
                new StepSurfaceCurveSweptAreaSolid(15, "scs", origin, origin, origin, 0.0, 1.0),
                new StepPolygonalBoundedHalfSpace(16, "pbh", origin, null, List.of(), true),
                new StepComplexClippingResult(17, "ccr", origin, origin, "DIFFERENCE"),
                new StepHalfSpaceSolid(18, "hss", origin, true, null, "HALF_SPACE_SOLID"),
                new StepCsgVolume(19, "csgv", origin),
                new StepBlockVolume(20, "bv", origin, 1.0, 2.0, 3.0),
                new StepFiniteElementMesh(21, "fem", "MESH", List.of(), List.of(), List.of(), 1.0),
                new StepFlatPattern(22, "fp", origin, List.of(), List.of(), null, List.of()),
                new StepMappedItem(23, null, origin));
        List<StepEntity> all = new ArrayList<>(solids);
        all.add(new StepSurfacePatch(24, "sp", origin, true));
        return all;
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
                name + " must stay public -- preview and export both call it across packages");
    }

    /**
     * The {@code instanceof} types of a one-argument boolean method, in source
     * order. Read from the source rather than reflection because the order is
     * part of what the copies agreed on and reflection does not preserve it.
     */
    private static List<String> instanceofTypes(String text, String method) {
        Pattern signature = Pattern.compile(
                "(?m)^\\s*(?:public |private |protected )?(?:static )?(?:final )?"
                        + "[\\w<>\\[\\], .]+\\s+" + Pattern.quote(method) + "\\s*\\([^)]*\\)\\s*\\{");
        Matcher matcher = signature.matcher(text);
        assertTrue(matcher.find(), "StepValidationHelper must declare " + method);
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
