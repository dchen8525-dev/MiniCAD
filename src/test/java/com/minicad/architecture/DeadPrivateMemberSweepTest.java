package com.minicad.architecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minicad.step.semantic.SelectTypeRegistry;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Guards the sweep of dead private members, and pins the rule that finds them.
 *
 * <p>This test lives in {@code architecture} because the sweep it guards crosses packages --
 * {@code step/semantic}, {@code step/syntax}, {@code export/json} and {@code export/mesh} all
 * gave something up -- and because the first half of it is a property of the whole tree rather
 * than of any one layer.
 *
 * <p>The property is the one the four pre-existing scanners are blind to: a <b>private</b>
 * member whose name occurs exactly once in its own file, that one occurrence being its own
 * declaration. Privacy is what makes the rule sound -- nothing outside the file can name the
 * member, so a per-file count of one means no caller anywhere, tests included. Counting per
 * <b>file</b> rather than per tree is the whole point:
 *
 * <ul>
 *   <li>{@code scan_duplicate_methods.py} groups by body text, so a copy that drifted by a line
 *       is skipped;
 *   <li>{@code scan_dead_types.py} needs every member of a type to be dead;
 *   <li>{@code scan_dead_members.py} counts how often a name appears in the <b>tree</b>, so when
 *       a family is extracted rather than moved -- the new class takes a private copy and the old
 *       class keeps the original -- every name in the dead half also appears in the live half and
 *       the leftovers look referenced.
 * </ul>
 *
 * <p>{@code tools/scan_dead_private_members.py} implements the same rule outside the build; this
 * test is the version that cannot be forgotten.
 *
 * <p>The second half pins the deletions that sweep produced. "Deleted a dead copy" and "dropped
 * the capability" look identical from the deleting side, so every retired name is paired with the
 * line that keeps its live home alive.
 */
class DeadPrivateMemberSweepTest {

  private static final Path MAIN = Paths.get("src/main/java");
  private static final Path SEMANTIC = MAIN.resolve("com/minicad/step/semantic");
  private static final Path EXPORT_JSON = MAIN.resolve("com/minicad/export/json");
  private static final Path EXPORT_MESH = MAIN.resolve("com/minicad/export/mesh");

  /**
   * Required by the language or by serialization and legitimately never mentioned again.
   */
  private static final List<String> ALLOWED_DEAD = List.of("serialVersionUID");

  private static final Pattern PRIVATE_MEMBER =
      Pattern.compile(
          "^[ \\t]*private\\b[ \\t]+"
              + "(?:(?:static|final|transient|volatile|abstract|synchronized|native|strictfp)"
              + "[ \\t]+)*"
              + "(?:[\\w$<>,\\[\\]\\.\\?]+[ \\t]+)*?"
              + "([\\w$<>,\\[\\]\\.\\?]+)[ \\t]+"
              + "(\\w+)[ \\t]*[\\(;=]",
          Pattern.MULTILINE);

  /** A member deleted because nothing in its own file ever named it. */
  private record Retired(String file, String name) {}

  /** A line that must stay, because it is the retired member's live home. */
  private record Home(String file, String text) {}

  private static final List<Retired> RETIRED =
      List.of(
          // Readers were deleted, the data they read stayed behind.
          new Retired("StepPreviewJsonExporter.java", "FACE_PROGRESS_INTERVAL"),
          new Retired("StepPreviewJsonExporter.java", "EDGE_PROGRESS_INTERVAL"),
          new Retired("StepPreviewJsonExporter.java", "MAX_TOTAL_TRIANGLE_POINTS"),
          new Retired("StepPreviewJsonExporter.java", "TOPOLOGY_SURFACE_GRID_SEGMENTS"),
          new Retired("StepMeshExporter.java", "STL_HEADER_SIZE"),
          new Retired("StepMeshExporter.java", "STL_TRIANGLE_RECORD_SIZE"),
          new Retired("StepMeshExporter.java", "STL_COUNT_SIZE"),
          new Retired("SelectTypeRegistry.java", "SELECT_TYPE_TO_CATEGORY"),
          new Retired("SelectTypeRegistry.java", "CATEGORY_TO_TYPES"),
          new Retired("SelectTypeRegistry.java", "ACTION_SELECT_TYPES"),
          new Retired("SelectTypeRegistry.java", "DEFINITION_SELECT_TYPES"),
          new Retired("SelectTypeRegistry.java", "GEOMETRIC_SELECT_TYPES"),
          new Retired("SelectTypeRegistry.java", "REPRESENTATION_SELECT_TYPES"),
          new Retired("SelectTypeRegistry.java", "ORGANIZATION_SELECT_TYPES"),
          new Retired("SelectTypeRegistry.java", "DATETIME_SELECT_TYPES"),
          new Retired("StepRepresentationPayloadBuilder.java", "log"),
          // The extraction copied rather than moved.
          new Retired("StepCadCurveBuilder.java", "buildReplicaCurve3"),
          new Retired("StepCadSurfaceBuilder.java", "requireExistingEntity"),
          // A dead twin whose live copy is in another package.
          new Retired("StepFacePayloadBuilder.java", "matchingPcurves"),
          new Retired("StepFacePayloadBuilder.java", "acceptablePcurveBasisSurfaceIds"),
          new Retired("StepAntlrBridge.java", "extractEntityTypeName"),
          new Retired("StepAntlrBridge.java", "convertComplexEntityParameters"),
          new Retired("StepMeshExporter.java", "PLANAR_EPS"),
          new Retired("StepMeshExporter.java", "appendOrientedTriangle"),
          new Retired("StepMeshExporter.java", "triangleArea"));

  private static final List<Home> HOMES =
      List.of(
          new Home("StepLegacyGeometryBuilder.java", "FACE_PROGRESS_INTERVAL"),
          new Home("StepLegacyGeometryBuilder.java", "EDGE_PROGRESS_INTERVAL"),
          new Home("PayloadReductionHelper.java", "MAX_TOTAL_TRIANGLE_POINTS"),
          new Home("PreviewMeshExporter.java", "TOPOLOGY_SURFACE_GRID_SEGMENTS"),
          new Home("MeshTriangulatorParametric.java", "PLANAR_EPS"),
          new Home("MeshTriangulatorParametric.java", "matchingPcurves"),
          new Home("TriangulationHelper.java", "appendOrientedTriangle"),
          new Home("SelectTypeRegistry.java", "ALL_SELECT_TYPES"),
          new Home("SelectTypeRegistry.java", "MEASURE_SELECT_TYPES"),
          new Home("SelectTypeRegistry.java", "isValidSelectType"),
          new Home("StepMeshExporter.java", "exportStlBinary"),
          new Home("StepMeshExporter.java", "exportStlText"),
          new Home("StepAntlrBridge.java", "public static StepFile parse(String stepText)"),
          new Home("StepCadCurveBuilder.java", "StepGeometricReplica.class"),
          new Home("StepCadEntityLookup.java", "requireExisting"));

  @Test
  @DisplayName("no private member is named only by its own declaration")
  void noPrivateMemberIsNamedOnlyByItsOwnDeclaration() throws IOException {
    List<String> violations = new ArrayList<>();
    int declarationsSeen = 0;
    for (Path file : javaSources(MAIN)) {
      String code = code(Files.readString(file, StandardCharsets.UTF_8));
      Matcher matcher = PRIVATE_MEMBER.matcher(code);
      while (matcher.find()) {
        String name = matcher.group(2);
        if (ALLOWED_DEAD.contains(name)) {
          continue;
        }
        declarationsSeen++;
        if (occurrences(code, name) == 1) {
          violations.add(MAIN.relativize(file).toString().replace('\\', '/') + ": " + name);
        }
      }
    }
    // A regex that matches nothing would pass the assertion below for the wrong reason.
    assertTrue(
        declarationsSeen > 1000,
        "the private-member scan matched only "
            + declarationsSeen
            + " declarations; it is broken, not clean. tools/scan_dead_private_members.py is the "
            + "reference implementation of this rule.");
    assertEquals(
        List.of(),
        violations,
        "each of these is private, so nothing outside its file can call it, and its own file "
            + "never names it again. That is a member whose caller was deleted or whose family "
            + "moved out from under it -- dead code the other four scanners structurally cannot "
            + "report, because the same name still lives in the class that replaced it.");
  }

  @Test
  @DisplayName("every retired member stays retired")
  void retiredMembersStayRetired() throws IOException {
    List<String> resurrected = new ArrayList<>();
    for (Retired retired : RETIRED) {
      String code = code(Files.readString(resolve(retired.file()), StandardCharsets.UTF_8));
      if (mentionsWord(code, retired.name())) {
        resurrected.add(retired.file() + ": " + retired.name());
      }
    }
    assertEquals(
        List.of(),
        resurrected,
        "these were deleted because nothing reachable named them, and each sits a few lines from "
            + "a live twin -- so a paste brings it back and no other test notices.");
  }

  @Test
  @DisplayName("every retired member's live home survives")
  void liveHomesSurvive() throws IOException {
    List<String> orphaned = new ArrayList<>();
    for (Home home : HOMES) {
      String code = code(Files.readString(resolve(home.file()), StandardCharsets.UTF_8));
      if (!code.contains(home.text())) {
        orphaned.add(home.file() + ": " + home.text());
      }
    }
    assertEquals(
        List.of(),
        orphaned,
        "each of these is the half that keeps the capability alive, so losing one would turn the "
            + "sweep above from deduplication into a silent feature drop.");
  }

  @Test
  @DisplayName("the surviving SELECT-type surface still answers at runtime")
  void selectTypeRegistryStillAnswers() {
    // The category maps that went were unreachable; this is what remains reachable, and the
    // assertion is behavioural rather than textual so a rename cannot satisfy it.
    assertTrue(
        SelectTypeRegistry.isValidSelectType("LENGTH_MEASURE"),
        "SelectTypeRegistry kept ALL_SELECT_TYPES and isValidSelectType; deleting the two dead "
            + "category maps must not have emptied the registry it still answers from.");
    assertTrue(
        SelectTypeRegistry.MEASURE_SELECT_TYPES.contains("LENGTH_MEASURE"),
        "MEASURE_SELECT_TYPES is read by UnitResolver at runtime, not just by tests.");
  }

  private static Path resolve(String simpleName) {
    List<Path> dirs =
        List.of(
            SEMANTIC,
            EXPORT_JSON,
            EXPORT_MESH,
            MAIN.resolve("com/minicad/step/syntax"),
            MAIN.resolve("com/minicad/export/glb"),
            MAIN.resolve("com/minicad/preview/payload"),
            MAIN.resolve("com/minicad/preview/sampling"));
    for (Path dir : dirs) {
      Path candidate = dir.resolve(simpleName);
      if (Files.exists(candidate)) {
        return candidate;
      }
    }
    throw new IllegalArgumentException("no such source file under src/main/java: " + simpleName);
  }

  private static List<Path> javaSources(Path root) throws IOException {
    try (Stream<Path> stream = Files.walk(root)) {
      return stream.filter(p -> p.toString().endsWith(".java")).sorted().toList();
    }
  }

  private static int occurrences(String code, String name) {
    Matcher matcher = Pattern.compile("\\b" + Pattern.quote(name) + "\\b").matcher(code);
    int count = 0;
    while (matcher.find()) {
      count++;
    }
    return count;
  }

  private static boolean mentionsWord(String code, String name) {
    return Pattern.compile("\\b" + Pattern.quote(name) + "\\b").matcher(code).find();
  }

  /**
   * Blanks comments while keeping string literals, so a name kept alive only by javadoc prose
   * cannot satisfy an assertion -- this repo has already had a guard pass on exactly that.
   */
  private static String code(String source) {
    StringBuilder out = new StringBuilder(source);
    int i = 0;
    while (i < source.length()) {
      char c = source.charAt(i);
      if (c == '/' && i + 1 < source.length() && source.charAt(i + 1) == '/') {
        int j = source.indexOf('\n', i);
        j = j < 0 ? source.length() : j;
        for (int k = i; k < j; k++) {
          out.setCharAt(k, ' ');
        }
        i = j;
      } else if (c == '/' && i + 1 < source.length() && source.charAt(i + 1) == '*') {
        int j = source.indexOf("*/", i + 2);
        j = j < 0 ? source.length() : j + 2;
        for (int k = i; k < j; k++) {
          if (source.charAt(k) != '\n') {
            out.setCharAt(k, ' ');
          }
        }
        i = j;
      } else if (c == '"') {
        i++;
        while (i < source.length() && source.charAt(i) != '"') {
          i += source.charAt(i) == '\\' ? 2 : 1;
        }
        i++;
      } else {
        i++;
      }
    }
    return out.toString();
  }
}
