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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Guards the removal of StepCadBuilder's dead curve orphans.
 *
 * <p>These eight privates survived the move of curve construction into {@code StepCadCurveBuilder}:
 * they had zero call sites anywhere, and several were the last unmourned dependants of builders that
 * an earlier pass had already deleted. Every one of them was reachable only from a caller that no
 * longer exists:
 *
 * <ul>
 *   <li>{@code sampleParabolaPoints2}/{@code sampleHyperbolaPoints2} -- fed the 2D conic builders,
 *       which the earlier 19-branch cleanup removed;
 *   <li>{@code sampleParabolaPoints3}/{@code sampleHyperbolaPoints3} -- same story on the 3D side;
 *   <li>{@code buildClothoidCurve} + its two helpers {@code fresnelC}/{@code fresnelS} -- a clothoid
 *       sampling path that was never wired to the dispatch table. This is the second Fresnel pair
 *       this class has shed: an earlier cleanup (648cf8bb) already took {@code fresnelCos}/{@code
 *       fresnelSin} out of it. The live clothoid sampling lives in {@code StepCadCurveBuilder}, so
 *       no Fresnel helper belongs in this file at all.
 *   <li>{@code buildReplicaCurve3} -- the 3D replica builder, left behind when the 2D one was
 *       converged onto {@code StepCadCurveBuilder}.
 * </ul>
 *
 * <p>They are one-line-easy to resurrect from history, look plausible next to their live neighbours,
 * and nothing in the suite would notice. Hence this test -- plus a check that the live neighbours
 * that shared their region were not swept up with them.
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
          "sampleHyperbolaPoints2");

  @Test
  @DisplayName("the dead curve orphans stay deleted")
  void deadOrphansStayDeleted() throws IOException {
    String source = Files.readString(BUILDER, StandardCharsets.UTF_8);
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
            + "pass and they were left behind. Restoring one adds code that nothing can reach, and "
            + "each new Fresnel/sampler copy is another thing to drift.");
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
            "private Curve3 buildConicCurve3(StepConicCurve conic)",
            "private Curve3 buildIndexedPolyCurve3(StepIndexedPolyCurve polyCurve)",
            "Plane buildSupportedPlaneGeometry(StepEntity geometry, String faceType)")) {
      assertTrue(
          source.contains(live),
          live
              + " is live (reachable from the dispatch table or the public API) and sits inside the "
              + "region the orphan cleanup edited. It must not be removed with the dead batch.");
    }
  }
}
