package com.minicad.step.semantic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Vector3;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Guards the removal of StepCadBuilder's dead curve orphans.
 *
 * <p>These privates survived the move of curve construction into {@code StepCadCurveBuilder}:
 * they had zero call sites anywhere, and several were the last unmourned dependants of builders
 * that an earlier pass had already deleted. Every one of them was reachable only from a caller
 * that no longer exists:
 *
 * <ul>
 *   <li>{@code sampleParabolaPoints2}/{@code sampleHyperbolaPoints2} -- fed the 2D conic
 *       builders, which the earlier 19-branch cleanup removed;
 *   <li>{@code sampleParabolaPoints3}/{@code sampleHyperbolaPoints3} -- same story on the 3D side;
 *   <li>{@code buildClothoidCurve} + its two helpers {@code fresnelC}/{@code fresnelS} -- a
 *       clothoid sampling path that was never wired to the dispatch table. This is the second
 *       Fresnel pair this class has shed: an earlier cleanup (648cf8bb) already took {@code
 *       fresnelCos}/{@code fresnelSin} out of it. The live clothoid sampling lives in {@code
 *       StepCadCurveBuilder}, so no Fresnel helper belongs in this file at all.
 *   <li>{@code buildReplicaCurve3} -- the 3D replica builder, left behind when the 2D one was
 *       converged onto {@code StepCadCurveBuilder}.
 * </ul>
 *
 * <p>A second sweep took the rest of the same shape -- the extraction copied into the new class
 * and left the original in place, so the copy is live and the original is dead. {@code
 * convert2DPlacementTo3D}, {@code buildImplicitBSplineCurve3}, {@code buildIndexedPolyCurve3},
 * {@code buildDegenerateCurve3}, {@code buildConicCurve3} and {@code transformPlane} all have a
 * live twin in {@code StepCadCurveBuilder} or {@code StepCadGeometryOps}, and all six were
 * invisible until then: {@code scan_dead_members.py} counts how often a *name* appears in the
 * tree, and every one of these names appears in the twin that replaced it. Only a per-file count
 * -- {@code tools/scan_dead_private_members.py} -- tells the two apart.
 *
 * <p>The second sweep also <b>reversed a premise this file used to assert</b>. {@code
 * buildConicCurve3} and {@code buildIndexedPolyCurve3} sat in {@code liveNeighboursSurvive}
 * under a message claiming they were "reachable from the dispatch table or the public API".
 * They are not: {@code StepCadBuilder} routes no {@code CONIC_CURVE} and no {@code
 * INDEXED_POLY_CURVE} any more -- {@code buildCurve3} hands the core 3D types to {@code
 * curveBuilder}, whose own table calls {@code self.buildConicCurve3}. The pin passed for as long
 * as it did because it only ever asked whether the string was present, and the string was
 * present in a class that was not this one. The check below now reads the source with comments
 * stripped, so a name kept alive by prose cannot satisfy it either.
 *
 * <p>They are one-line-easy to resurrect from history, look plausible next to their live
 * neighbours, and nothing in the suite would notice. Hence this test -- plus a check that the
 * live neighbours that shared their region were not swept up with them.
 */
class StepCadBuilderDeadOrphansTest {

  private static final Path SEMANTIC = Paths.get("src/main/java/com/minicad/step/semantic");
  private static final Path BUILDER = SEMANTIC.resolve("StepCadBuilder.java");

  private static final List<String> REMOVED =
      List.of(
          "buildClothoidCurve",
          "fresnelC",
          "fresnelS",
          "buildReplicaCurve3",
          "sampleParabolaPoints3",
          "sampleHyperbolaPoints3",
          "sampleParabolaPoints2",
          "sampleHyperbolaPoints2",
          // Second sweep: extraction copies whose live half is in the extracted class.
          "convert2DPlacementTo3D",
          "buildImplicitBSplineCurve3",
          "buildIndexedPolyCurve3",
          "buildDegenerateCurve3",
          "buildConicCurve3",
          "transformPlane");

  @Test
  @DisplayName("the dead curve orphans stay deleted")
  void deadOrphansStayDeleted() throws IOException {
    String source = code(Files.readString(BUILDER, StandardCharsets.UTF_8));
    List<String> resurrected = new ArrayList<>();
    for (String name : REMOVED) {
      if (Pattern.compile("\\b" + Pattern.quote(name) + "\\b").matcher(source).find()) {
        resurrected.add(name);
      }
    }
    assertEquals(
        List.of(),
        resurrected,
        "These methods had zero call sites: their callers were deleted by an earlier convergence "
            + "pass and they were left behind, or their live half moved into the extracted class "
            + "and the original stayed. Restoring one adds code that nothing can reach, and each "
            + "new Fresnel/sampler/copy is another thing to drift.");
  }

  @Test
  @DisplayName("StepCadBuilder does not regain a Fresnel approximation")
  void noFresnelApproximationReturnsToThisBuilder() throws IOException {
    String source = Files.readString(BUILDER, StandardCharsets.UTF_8);
    List<String> hits = new ArrayList<>();
    Matcher matcher = Pattern.compile("\\b[Ff]resnel\\w*").matcher(source);
    while (matcher.find()) {
      hits.add(matcher.group());
    }
    assertEquals(
        List.of(),
        hits,
        "StepCadBuilder has shed two Fresnel pairs -- fresnelCos/fresnelSin with the 648cf8bb "
            + "cleanup, then fresnelC/fresnelS with the clothoid orphan -- and both times the helper "
            + "was unreachable from the dispatch table. The live clothoid sampling lives in "
            + "StepCadCurveBuilder (buildClothoid2, which keeps its own fresnelCos/fresnelSin); any "
            + "copy here would again be dead on arrival.");
  }

  @Test
  @DisplayName("buildRationalBSplineCurve2 stays a one-line delegation")
  void rationalBsplineCurve2StaysDelegated() throws IOException {
    String source = Files.readString(BUILDER, StandardCharsets.UTF_8);
    assertTrue(
        Pattern.compile(
                "public RationalBSplineCurve2 buildRationalBSplineCurve2\\(int id\\)\\s*\\{\\s*"
                    + "return curveBuilder\\.buildRationalBSplineCurve2\\(id\\);\\s*}")
            .matcher(source)
            .find(),
        "StepCadBuilder.buildRationalBSplineCurve2 held a full copy of the curve builder's body "
            + "while its 3D sibling two lines above was already a one-line delegation. It must "
            + "stay delegated, not re-inlined.");
  }

  @Test
  @DisplayName("the live neighbours of the removed region survive")
  void liveNeighboursSurvive() throws IOException {
    String source = Files.readString(BUILDER, StandardCharsets.UTF_8);
    for (String live :
        List.of(
            "public Clothoid3 buildClothoid(int id)",
            "public Curve3 buildOffsetCurve3(int id)",
            "Plane buildSupportedPlaneGeometry(StepEntity geometry, String faceType)",
            // The shared implicit-knot table the deleted 3D wrapper used to read.
            "implicitBSplineCurveDataOrNull(StepEntity entity)")) {
      assertTrue(
          source.contains(live),
          live
              + " is live (reachable from the dispatch table, the public API, or the extracted "
              + "curve builder) and sits inside the region the orphan cleanup edited. It must not "
              + "be removed with the dead batch.");
    }
  }

  @Test
  @DisplayName("the capability every removed method carried still has a home")
  void theCapabilityKeepsItsHome() throws IOException {
    String curveBuilder =
        Files.readString(SEMANTIC.resolve("StepCadCurveBuilder.java"), StandardCharsets.UTF_8);
    String geometryOps =
        Files.readString(SEMANTIC.resolve("StepCadGeometryOps.java"), StandardCharsets.UTF_8);
    // Deleting a dead copy and dropping the capability look identical from StepCadBuilder's
    // side, so each deletion is paired with the twin that answers the same question.
    String[][] homes = {
      {"buildConicCurve3", curveBuilder},
      {"buildIndexedPolyCurve3", curveBuilder},
      {"buildDegenerateCurve3", curveBuilder},
      {"buildImplicitBSplineCurve3", curveBuilder},
      {"convert2DPlacementTo3D", curveBuilder},
      {"transformPlane", geometryOps},
    };
    List<String> orphaned = new ArrayList<>();
    for (String[] pair : homes) {
      if (!Pattern.compile("\\b" + Pattern.quote(pair[0]) + "\\b").matcher(pair[1]).find()) {
        orphaned.add(pair[0]);
      }
    }
    assertEquals(
        List.of(),
        orphaned,
        "each of these was deleted from StepCadBuilder because an extracted class answers the "
            + "same question; if the twin is gone too, the capability was dropped rather than "
            + "deduplicated.");
  }

  @Test
  @DisplayName("the axis-completion rule has one home and keeps its own answer")
  void perpendicularDirectionHasOneHome() throws IOException {
    String builder = code(Files.readString(BUILDER, StandardCharsets.UTF_8));
    String geometryBuilder =
        code(
            Files.readString(
                SEMANTIC.resolve("StepCadGeometryBuilder.java"), StandardCharsets.UTF_8));
    assertEquals(
        0,
        count(builder, "perpendicularDirection(Direction3"),
        "StepCadBuilder kept a 20-line copy of the axis-completion rule after "
            + "StepCadGeometryBuilder had been extracted for exactly that job. Two copies of an "
            + "axis choice are two answers the moment one is tuned: buildTransformation must call "
            + "geometryBuilder.perpendicularDirection(zAxis) rather than re-inline the body.");
    assertEquals(
        1,
        count(geometryBuilder, "perpendicularDirection(Direction3"),
        "the rule must have exactly one declaration, in the builder extracted to own it");
    assertTrue(
        builder.contains("geometryBuilder.perpendicularDirection(zAxis)"),
        "buildTransformation must complete the axis system through the extracted builder");
    Direction3 plusX = new Direction3(1, 0, 0);
    Vector3 completed = newBuilder().perpendicularDirection(plusX).asVector();
    assertEquals(0.0, completed.getY(), 1e-12, "the axis-completion rule returns -Z for +X");
    assertEquals(-1.0, completed.getZ(), 1e-12);
    assertEquals(
        1.0,
        plusX.perpendicular().asVector().getY(),
        1e-12,
        "Direction3.perpendicular() returns +Y for +X: it is a different rule, so converging this "
            + "method onto it would silently flip the x-axis of every placement that leans on it.");
  }

  @Test
  @DisplayName("faceGeometry is declared once and the swept builder calls it by qualifier")
  void faceGeometryHasOneHome() throws IOException {
    String builder = code(Files.readString(BUILDER, StandardCharsets.UTF_8));
    String swept =
        code(
            Files.readString(
                SEMANTIC.resolve("StepCadSweptBuilder.java"), StandardCharsets.UTF_8));
    assertEquals(
        1,
        count(builder, "faceGeometry(StepFaceEntity"),
        "StepCadBuilder is the topology-side home of faceGeometry; it throws where "
            + "StepGeometryHelper returns null, which is why the two are deliberately separate.");
    assertEquals(
        0,
        count(swept, "faceGeometry(StepFaceEntity"),
        "StepCadSweptBuilder carried a private copy of faceGeometry. It must reach the shared home "
            + "as StepCadBuilder.faceGeometry(stepFace).");
    assertEquals(
        2,
        count(swept, "StepCadBuilder.faceGeometry(stepFace)"),
        "both EXTRUDED_FACE_SOLID and REVOLVED_FACE_SOLID need a face geometry; the "
            + "SWEPT_FACE_SOLID branch bails out before it reaches one.");
  }

  private static long count(String haystack, String needle) {
    long total = 0;
    for (int at = haystack.indexOf(needle); at >= 0; at = haystack.indexOf(needle, at + 1)) {
      total++;
    }
    return total;
  }

  private static StepCadGeometryBuilder newBuilder() {
    return new StepCadGeometryBuilder(
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>(),
        id -> null);
  }

  /**
   * Blanks comments while keeping string literals, so a name kept alive only by javadoc prose
   * cannot satisfy an assertion.
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
