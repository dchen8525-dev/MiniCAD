package com.minicad.step.semantic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Guards the sweep of step/semantic's dead helpers.
 *
 * <p>{@code tools/scan_dead_methods.py} lists the methods whose name occurs nowhere in the tree
 * but at its own declaration. That list was not empty in this package, and it was not random:
 * almost every entry was one half of a pair whose other half is live, left behind when its caller
 * was deleted or when the family was refactored onto a different shape.
 *
 * <ul>
 *   <li>{@code StepPrimitiveTessellator.tessellateSphere} and its helper {@code spherePoint} --
 *       the origin-centred twins of the {@code ...At} forms {@code StepCadBuilder} actually calls.
 *       {@code tessellateTorus} stayed: nothing ever replaced the torus with an {@code At} form.
 *   <li>{@code StepParameterReader.optionalRequireEntity}, {@code optionalResolveReference},
 *       {@code listElements} and {@code entityReferenceGrid} -- the optional/raw variants of
 *       {@code requireEntity}, {@code resolveReference}, {@code literalList} and
 *       {@code referenceGrid}. Removing {@code optionalResolveReference} took
 *       {@code resolveReference} with it: that had been its only caller.
 *   <li>{@code RegistryHelpers} -- extracted from {@code StepEntityResolver} "for reuse across all
 *       registry classes", but only {@code registerGeometricToleranceAliases} was ever adopted
 *       (ToleranceRegistry calls it twice). The rest of the register family was declared and never
 *       wired.
 *   <li>{@code StepEntityResolver} kept its own copies of the same register helpers; the two
 *       {@code registerProductDefinitionRelationship*} twins and {@code registerRepresentationAliases}
 *       had no caller in either file.
 *   <li>{@code SelectTypeRegistry}'s category-query surface ({@code getCategoryDescription},
 *       {@code getSelectCategory}, {@code getAllowedTypesForCategory}, {@code isSelectTypeAllowed}).
 *       The class survives as the holder of {@code MEASURE_SELECT_TYPES} and
 *       {@code isValidSelectType}, which UnitResolver and StepParameterReader do use.
 *   <li>{@code StepTrimResolver.requireTrimPoint2} -- the dead twin of {@code resolveTrimPoint2}.
 * </ul>
 *
 * <p>Each of these sits a few lines from a live twin, so every one is one paste away from coming
 * back, and nothing else in the suite would notice. Hence this test.
 */
class SemanticDeadHelperRemovalTest {

  private static final Path SEMANTIC = Paths.get("src/main/java/com/minicad/step/semantic");

  private static final Map<String, List<String>> REMOVED =
      Map.of(
          "RegistryHelpers.java",
          List.of(
              "registerShapeAspectRelationshipAliases",
              "registerRepresentationRelationshipAliases",
              "registerTypedMeasureWithUnit",
              "registerTypedMeasureWithUnitPairs",
              "registerStandaloneDerivedUnitKinds",
              "registerProductDefinitionRelationshipAliases",
              "registerProductDefinitionRelationshipRelationshipAliases"),
          "SelectTypeRegistry.java",
          List.of(
              "getCategoryDescription",
              "getSelectCategory",
              "getAllowedTypesForCategory",
              "isSelectTypeAllowed"),
          "StepEntityResolver.java",
          List.of(
              "resolveBoundingBox",
              "resolvePointOnFace",
              "registerProductDefinitionRelationshipAliases",
              "registerProductDefinitionRelationshipRelationshipAliases",
              "registerRepresentationAliases"),
          "StepParameterReader.java",
          List.of(
              "optionalRequireEntity",
              "optionalResolveReference",
              "resolveReference",
              "listElements",
              "entityReferenceGrid"),
          "StepPrimitiveTessellator.java",
          List.of("tessellateSphere", "rotatePointAroundAxis", "spherePoint"),
          "StepTrimResolver.java",
          List.of("requireTrimPoint2"));

  @Test
  @DisplayName("the dead helpers stay deleted")
  void deadHelpersStayDeleted() throws IOException {
    Map<String, List<String>> resurrected = new LinkedHashMap<>();
    for (Map.Entry<String, List<String>> entry : REMOVED.entrySet()) {
      String source = Files.readString(SEMANTIC.resolve(entry.getKey()), StandardCharsets.UTF_8);
      List<String> hits = new ArrayList<>();
      for (String name : entry.getValue()) {
        if (Pattern.compile("\\b" + Pattern.quote(name) + "\\b").matcher(source).find()) {
          hits.add(name);
        }
      }
      if (!hits.isEmpty()) {
        resurrected.put(entry.getKey(), hits);
      }
    }
    assertEquals(
        Map.of(),
        resurrected,
        "These names had no call site anywhere in the tree, tests included, and several are the "
            + "optional or origin-centred twin of a live method a few lines away -- so each one is "
            + "one paste away from returning silently.");
  }

  @Test
  @DisplayName("the live twin of every removed helper survives")
  void theLiveTwinSurvives() throws IOException {
    String[][] live = {
      {"RegistryHelpers.java", "public static void registerGeometricToleranceAliases("},
      {"SelectTypeRegistry.java", "public static boolean isValidSelectType("},
      {"StepEntityResolver.java", "private static Map<String, EntityFactory> createRegistry()"},
      {"StepParameterReader.java", "public static <T extends StepEntity> T requireEntity("},
      {"StepParameterReader.java", "public static StepEntity tryResolveReference("},
      {"StepParameterReader.java", "public static List<String> literalList("},
      {"StepParameterReader.java", "public static List<StepEntity> entityReferenceList("},
      {"StepPrimitiveTessellator.java", "public static List<Face> tessellateSphereAt("},
      {"StepPrimitiveTessellator.java", "public static List<Face> tessellateTorus("},
      {"StepPrimitiveTessellator.java", "private static CartesianPoint spherePointAt("},
      {"StepTrimResolver.java", "Point2 resolveTrimPoint2("},
    };
    for (String[] pair : live) {
      String source = Files.readString(SEMANTIC.resolve(pair[0]), StandardCharsets.UTF_8);
      assertTrue(
          source.contains(pair[1]),
          pair[1]
              + " is the live half of a pair whose dead half was just removed, and sits inside the "
              + "region the sweep edited. It must not be swept up with it.");
    }
  }

  @Test
  @DisplayName("RegistryHelpers keeps the one helper a production caller adopted")
  void registryHelpersKeepsItsAdoptedHelper() throws IOException {
    String tolerance =
        Files.readString(SEMANTIC.resolve("ToleranceRegistry.java"), StandardCharsets.UTF_8);
    assertTrue(
        Pattern.compile("RegistryHelpers\\.registerGeometricToleranceAliases\\(")
            .matcher(tolerance)
            .find(),
        "registerGeometricToleranceAliases is the only RegistryHelpers method a registry ever "
            + "called. If this call goes too, the class is fully abandoned and should be retired, "
            + "not left as a library of dead registrations.");
  }

  @Test
  @DisplayName("SelectTypeRegistry is not left an empty shell")
  void selectTypeRegistryKeepsItsLiveMembers() throws IOException {
    String source =
        Files.readString(SEMANTIC.resolve("SelectTypeRegistry.java"), StandardCharsets.UTF_8);
    assertTrue(
        source.contains("MEASURE_SELECT_TYPES"),
        "The category-query methods were dead, but MEASURE_SELECT_TYPES is read by UnitResolver "
            + "and a test, and isValidSelectType by StepParameterReader. The class must survive "
            + "with those members rather than be deleted wholesale as dead.");
  }
}
