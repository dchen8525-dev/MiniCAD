package com.minicad.step.semantic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
 * Guards the removal of the unreferenced {@code StepEntityValidator} class.
 *
 * <p>{@code StepEntityValidator} was a static-only predicate container whose own javadoc described
 * it as "validation methods extracted from StepEntityResolver" -- an extraction that was never
 * wired up. {@code StepEntityResolver} kept its own instance-level predicates (delegating to
 * {@code topologyResolver}), so the extracted class ended up referenced by nothing at all: no
 * qualified call, no static import, no reflection. It also carried the tell-tale scar of the
 * half-finished move -- six identical consecutive {@code import com.minicad.step.model.*;} lines.
 *
 * <p>Its one predicate that also existed elsewhere, {@code isAnnotationOccurrence}, was byte-for-byte
 * identical to the live {@code StepResolverValueHelpers} copy -- and every real caller already used
 * the qualified helper. So the class is gone, and this test keeps it gone and keeps the surviving
 * predicate honest.
 */
class StepEntityValidatorRemovalTest {

  private static final Path SEMANTIC = Paths.get("src/main/java/com/minicad/step/semantic");

  /**
   * Matches the whole declaration of {@code isAnnotationOccurrence} and captures its body. The body
   * is matched with {@code [^{}]*} so this cannot run past the method into a neighbouring block.
   */
  private static final Pattern DECLARATION =
      Pattern.compile(
          "static boolean isAnnotationOccurrence\\(\\s*StepEntity\\s+\\w+\\)\\s*\\{([^{}]*)}");

  private static final Pattern INSTANCEOF_TYPE = Pattern.compile("instanceof\\s+(\\w+)");

  /** The 13 types the surviving predicate accepted when the dead container was removed. */
  private static final List<String> EXPECTED_TYPES =
      List.of(
          "StepAnnotationTextOccurrence",
          "StepAnnotationPointOccurrence",
          "StepAnnotationCurveOccurrence",
          "StepLeaderCurve",
          "StepProjectionCurve",
          "StepDimensionCurve",
          "StepAnnotationFillAreaOccurrence",
          "StepAnnotationPlaceholderOccurrence",
          "StepAnnotationPlane",
          "StepAnnotationSymbolOccurrence",
          "StepAnnotationSubfigureOccurrence",
          "StepDraughtingAnnotationOccurrence",
          "StepTerminatorSymbol");

  @Test
  @DisplayName("the unreferenced StepEntityValidator container stays deleted")
  void deadValidatorContainerMustNotComeBack() {
    Path resurrected = SEMANTIC.resolve("StepEntityValidator.java");
    assertTrue(
        Files.notExists(resurrected),
        "StepEntityValidator had zero references (no qualified call, no static import) -- it was "
            + "an extraction from StepEntityResolver that was never wired up, while the resolver "
            + "kept its own instance-level predicates. Re-adding it puts an unused second copy of "
            + "these predicates back in the tree.");
  }

  @Test
  @DisplayName("isAnnotationOccurrence has exactly one implementation in the package")
  void isAnnotationOccurrenceHasExactlyOneImplementation() throws IOException {
    List<String> hosts = new ArrayList<>();
    try (Stream<Path> files = Files.list(SEMANTIC)) {
      for (Path file : files.filter(path -> path.toString().endsWith(".java")).toList()) {
        if (DECLARATION.matcher(Files.readString(file, StandardCharsets.UTF_8)).find()) {
          hosts.add(file.getFileName().toString());
        }
      }
    }
    assertEquals(
        List.of("StepResolverValueHelpers.java"),
        hosts,
        "isAnnotationOccurrence must have a single implementation. A second copy is exactly the "
            + "duplication this removal consolidated -- and the copies can silently drift, since "
            + "they are plain instanceof chains that no compiler check compares.");
  }

  @Test
  @DisplayName("the surviving predicate keeps its 13 type predicates, in order")
  void isAnnotationOccurrenceKeepsItsTypePredicates() throws IOException {
    assertEquals(
        EXPECTED_TYPES,
        annotationTypes(),
        "The instanceof types of isAnnotationOccurrence changed. Dropping one silently stops "
            + "treating that entity as an annotation occurrence; adding one widens the acceptance "
            + "set for annotation resolution.");
  }

  @Test
  @DisplayName("callers still reach the predicate through the qualified helper")
  void callersStillRouteThroughTheQualifiedHelper() throws IOException {
    for (String caller : List.of("DraughtingResolver.java", "StepEntityResolver.java")) {
      String source = Files.readString(SEMANTIC.resolve(caller), StandardCharsets.UTF_8);
      assertTrue(
          source.contains("StepResolverValueHelpers.isAnnotationOccurrence"),
          caller
              + " must call StepResolverValueHelpers.isAnnotationOccurrence. Re-inlining the "
              + "predicate (or reaching it unqualified) re-creates the copy that was deleted.");
    }
  }

  private static List<String> annotationTypes() throws IOException {
    String source = Files.readString(SEMANTIC.resolve("StepResolverValueHelpers.java"),
        StandardCharsets.UTF_8);
    Matcher declaration = DECLARATION.matcher(source);
    if (!declaration.find()) {
      throw new AssertionError(
          "isAnnotationOccurrence(StepEntity) declaration not found in StepResolverValueHelpers");
    }
    List<String> types = new ArrayList<>();
    Matcher type = INSTANCEOF_TYPE.matcher(declaration.group(1));
    while (type.find()) {
      types.add(type.group(1));
    }
    return types;
  }
}
