package com.minicad.step.semantic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Guards the single-implementation convergence of the SOLID dispatch predicate.
 *
 * <p>A second copy of the SOLID builder used to live next door as {@code StepSolidBuilder}. It was
 * a frozen fork of {@code StepCadSolidBuilder} left behind by the 2026-07-04 rename: {@code
 * StepCadBuilder} switched to {@code new StepCadSolidBuilder(this)} and the old class survived
 * referenced by nothing but its own reflection test. Its dispatch table was a strict SUBSET of the
 * live one (39 of the live 40 types -- it never learned {@code StepFacetedBrepAndBrepWithVoids}),
 * so deleting it costs no capability.
 *
 * <p>What can regress, and what this test pins:
 *
 * <ol>
 *   <li>the dead class -- plus the guard, frozen-order file and generator scripts that existed only
 *       to service it -- must not be restored from history. A resurrected fork would again diverge
 *       silently, because the live suite cannot tell two byte-identical predicates apart until they
 *       drift.
 *   <li>{@code canBuildAsSolid} is a hand-maintained 37-branch {@code instanceof ||} predicate with
 *       no table, and therefore no frozen-order guard of its own. Two kinds of rot are invisible to
 *       the compiler: a type dropped (an entity that used to resolve now throws) or a type added to
 *       the predicate that the dispatch table cannot handle ({@code canBuildAsSolid} answers yes,
 *       {@code buildSolid} then throws "not a supported SOLID"). The subset invariant in
 *       {@link #predicateTypesAreAllHandledByTheDispatchTable} pins the second.
 * </ol>
 */
class StepCadSolidBuilderConvergenceTest {

  private static final Path SEMANTIC_MAIN =
      Paths.get("src/main/java/com/minicad/step/semantic");
  private static final Path SEMANTIC_TEST =
      Paths.get("src/test/java/com/minicad/step/semantic");
  private static final Path RESOURCES = Paths.get("src/test/resources");
  private static final Path TOOLS = Paths.get("tools");
  private static final Path MAIN_ROOT = Paths.get("src/main/java");
  private static final Path SOLID_BUILDER = SEMANTIC_MAIN.resolve("StepCadSolidBuilder.java");

  /**
   * The predicate body, deliberately matched with {@code [^{}]*}: the method has no nested block,
   * so a delegate such as {@code StepCadBuilder.canBuildAsSolid} (which just returns a call) cannot
   * be conflated with the real implementation.
   */
  private static final Pattern PREDICATE_METHOD =
      Pattern.compile("boolean canBuildAsSolid\\(StepEntity entity\\)\\s*\\{([^{}]*)}");

  private static final Pattern PREDICATE_BODY_WITH_INSTANCEOF =
      Pattern.compile("boolean canBuildAsSolid\\(StepEntity entity\\)\\s*\\{[^{}]*instanceof");

  private static final Pattern INSTANCEOF_TYPE = Pattern.compile("instanceof\\s+(\\w+)");

  private static final Pattern TABLE_ENTRY = Pattern.compile("solidRule\\((\\w+)\\.class");

  private static final Pattern DELEGATION =
      Pattern.compile(
          "public boolean canBuildAsSolid\\(StepEntity entity\\)\\s*\\{\\s*"
              + "return solidBuilder\\.canBuildAsSolid\\(entity\\);\\s*}");

  /** The 37 types the predicate accepted when the fork was deleted. Order is the source order. */
  private static final List<String> EXPECTED_PREDICATE_TYPES =
      List.of(
          "StepManifoldSolidBrep",
          "StepFacettedBrep",
          "StepBrepWithVoids",
          "StepCsgPrimitive",
          "StepCsgSolid",
          "StepSolidReplica",
          "StepSweptAreaSolid",
          "StepSweptDiskSolid",
          "StepExtrudedAreaSolidTapered",
          "StepRevolvedAreaSolidTapered",
          "StepSurfaceCurveSweptAreaSolid",
          "StepBooleanClippingResult",
          "StepBooleanResult",
          "StepNonManifoldSolidBrep",
          "StepAdvancedBrep",
          "StepComplexClippingResult",
          "StepCsgVolume",
          "StepBlockVolume",
          "StepHalfSpaceSolid",
          "StepPolygonalBoundedHalfSpace",
          "StepTessellatedFaceSet",
          "StepTessellatedFace",
          "StepTriangulatedFace",
          "StepComplexTriangulatedFace",
          "StepCubicBezierTriangulatedFace",
          "StepExtrudedFaceSolid",
          "StepRevolvedFaceSolid",
          "StepSweptFaceSolid",
          "StepCylinderVolume",
          "StepSphereVolume",
          "StepTorusVolume",
          "StepPrismVolume",
          "StepRightCircularConeVolume",
          "StepSolidModel",
          "StepFiniteElementMesh",
          "StepFlatPattern",
          "StepMappedItem");

  @Test
  @DisplayName("the dead StepSolidBuilder fork and everything that only served it stay deleted")
  void deadSolidBuilderMustNotComeBack() {
    List<Path> resurrected = new ArrayList<>();
    for (Path candidate :
        List.of(
            SEMANTIC_MAIN.resolve("StepSolidBuilder.java"),
            SEMANTIC_TEST.resolve("SolidBuilderDispatchTableTest.java"),
            RESOURCES.resolve("solid-builder-dispatch-order.txt"),
            TOOLS.resolve("gen_solid_builder_dispatch.py"),
            TOOLS.resolve("verify_solid_builder_dispatch.py"))) {
      if (Files.exists(candidate)) {
        resurrected.add(candidate);
      }
    }
    assertEquals(
        List.of(),
        resurrected,
        "StepSolidBuilder was a frozen fork of StepCadSolidBuilder with zero main-source "
            + "references, and its dispatch table was a strict subset of the live one. Restoring "
            + "it (or the guard/frozen-order/generator trio that existed only to service it) "
            + "restores a second SOLID implementation that no test can distinguish from the live "
            + "one until it drifts.");
  }

  @Test
  @DisplayName("the instanceof predicate body lives in exactly one class")
  void thePredicateBodyLivesInExactlyOneClass() throws IOException {
    List<String> hosts = new ArrayList<>();
    try (Stream<Path> files = Files.walk(MAIN_ROOT)) {
      for (Path file : files.filter(path -> path.toString().endsWith(".java")).toList()) {
        String source = Files.readString(file, StandardCharsets.UTF_8);
        if (PREDICATE_BODY_WITH_INSTANCEOF.matcher(source).find()) {
          hosts.add(file.getFileName().toString());
        }
      }
    }
    assertEquals(
        List.of("StepCadSolidBuilder.java"),
        hosts,
        "canBuildAsSolid must have exactly one implementation (the instanceof chain). A second "
            + "class carrying a full body is a re-forked predicate: StepCadBuilder is expected to "
            + "contain only the one-line delegation.");
  }

  @Test
  @DisplayName("canBuildAsSolid keeps its type predicates, in order")
  void canBuildAsSolidKeepsItsTypePredicates() throws IOException {
    assertEquals(
        EXPECTED_PREDICATE_TYPES,
        typePredicates(),
        "The set/order of instanceof type predicates in canBuildAsSolid changed. Dropping a type "
            + "silently stops resolving entities that used to build; adding one without a matching "
            + "dispatch-table handler makes canBuildAsSolid answer yes where buildSolid then throws "
            + "\"not a supported SOLID\".");
  }

  @Test
  @DisplayName("every type the predicate accepts has a handler in the dispatch table")
  void predicateTypesAreAllHandledByTheDispatchTable() throws IOException {
    Set<String> handled = dispatchTableTypes();
    List<String> unhandled =
        typePredicates().stream().filter(type -> !handled.contains(type)).toList();
    assertEquals(
        List.of(),
        unhandled,
        "These types are accepted by canBuildAsSolid but have no SOLID_RULES entry, so the "
            + "predicate promises a build that the table cannot deliver. The table is allowed to be "
            + "a superset (abstract/fall-through branches), never a subset of the predicate.");
  }

  @Test
  @DisplayName("StepCadBuilder keeps delegating the predicate to the solid builder")
  void stepCadBuilderStillDelegatesThePredicate() throws IOException {
    String source =
        Files.readString(SEMANTIC_MAIN.resolve("StepCadBuilder.java"), StandardCharsets.UTF_8);
    assertTrue(
        DELEGATION.matcher(source).find(),
        "StepCadBuilder.canBuildAsSolid must stay a one-line delegation to "
            + "solidBuilder.canBuildAsSolid(entity). Re-inlining the predicate there would create "
            + "the second copy this convergence removed.");
  }

  private static List<String> typePredicates() throws IOException {
    Matcher method = PREDICATE_METHOD.matcher(readSolidBuilder());
    if (!method.find()) {
      throw new AssertionError(
          "canBuildAsSolid(StepEntity) with a single-block body not found in " + SOLID_BUILDER);
    }
    List<String> types = new ArrayList<>();
    Matcher type = INSTANCEOF_TYPE.matcher(method.group(1));
    while (type.find()) {
      types.add(type.group(1));
    }
    return types;
  }

  private static Set<String> dispatchTableTypes() throws IOException {
    Matcher entry = TABLE_ENTRY.matcher(readSolidBuilder());
    Set<String> types = new HashSet<>();
    while (entry.find()) {
      types.add(entry.group(1));
    }
    return types;
  }

  private static String readSolidBuilder() throws IOException {
    return Files.readString(SOLID_BUILDER, StandardCharsets.UTF_8);
  }
}
