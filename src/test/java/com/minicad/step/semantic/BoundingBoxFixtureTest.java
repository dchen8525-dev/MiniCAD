package com.minicad.step.semantic;

import com.minicad.geometry.BoundingBox3;
import com.minicad.step.model.core.base.StepEntity;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;
import com.minicad.topology.Shell;
import com.minicad.topology.Solid;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests for bounding box computation on example fixtures (I04).
 *
 * Validates that parsing and resolving example STEP files produces
 * expected bounding box values for the resulting geometry.
 */
class BoundingBoxFixtureTest {

  // ---------------------------------------------------------------------------
  // Helper methods
  // ---------------------------------------------------------------------------

  private static StepCadBuilder parseFixture(String name) throws IOException {
    Path path = Path.of("examples", name);
    String stepText = Files.readString(path);
    StepFile stepFile = StepParser.parse(stepText);
    Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
    return StepCadBuilder.fromResolved(resolved);
  }

  // ---------------------------------------------------------------------------
  // minimal-square.step tests
  // ---------------------------------------------------------------------------

  @Test
  void minimalSquareBoundingBox() throws IOException {
    StepCadBuilder builder = parseFixture("minimal-square.step");

    // Entity #100 is MANIFOLD_SOLID_BREP with CLOSED_SHELL
    Solid solid = builder.buildSolid(100);
    assertNotNull(solid);

    BoundingBox3 bbox = solid.boundingBox();
    // 1x1 square in XY plane at origin
    assertEquals(0.0, bbox.minX(), 1e-9, "minX should be 0");
    assertEquals(0.0, bbox.minY(), 1e-9, "minY should be 0");
    assertEquals(0.0, bbox.minZ(), 1e-9, "minZ should be 0");
    assertEquals(1.0, bbox.maxX(), 1e-9, "maxX should be 1");
    assertEquals(1.0, bbox.maxY(), 1e-9, "maxY should be 1");
    assertEquals(0.0, bbox.maxZ(), 1e-9, "maxZ should be 0 (2D face)");
  }

  // ---------------------------------------------------------------------------
  // plate-with-round-hole.step tests
  // ---------------------------------------------------------------------------

  @Test
  void plateWithRoundHoleBoundingBox() throws IOException {
    StepCadBuilder builder = parseFixture("plate-with-round-hole.step");

    // Entity #90 is OPEN_SHELL with ADVANCED_FACE #80
    Shell shell = builder.buildShell(90);
    assertNotNull(shell);

    BoundingBox3 bbox = shell.boundingBox();
    // 4x4 plate with 1-radius circular hole at center (2,2)
    // Outer bounds: (0,0,0) to (4,4,0)
    assertEquals(0.0, bbox.minX(), 1e-9, "minX should be 0");
    assertEquals(0.0, bbox.minY(), 1e-9, "minY should be 0");
    assertEquals(0.0, bbox.minZ(), 1e-9, "minZ should be 0");
    assertEquals(4.0, bbox.maxX(), 1e-9, "maxX should be 4");
    assertEquals(4.0, bbox.maxY(), 1e-9, "maxY should be 4");
    assertEquals(0.0, bbox.maxZ(), 1e-9, "maxZ should be 0 (2D face)");
  }

  // ---------------------------------------------------------------------------
  // rectangular-frame.step tests
  // ---------------------------------------------------------------------------

  @Test
  void rectangularFrameBoundingBox() throws IOException {
    StepCadBuilder builder = parseFixture("rectangular-frame.step");

    // Entity #90 is OPEN_SHELL
    Shell shell = builder.buildShell(90);
    assertNotNull(shell);

    BoundingBox3 bbox = shell.boundingBox();
    // Outer frame: 6x4 (0,0,0) to (6,4,0)
    // Inner hole: 2x2 (2,1,0) to (4,3,0)
    assertEquals(0.0, bbox.minX(), 1e-9, "minX should be 0");
    assertEquals(0.0, bbox.minY(), 1e-9, "minY should be 0");
    assertEquals(0.0, bbox.minZ(), 1e-9, "minZ should be 0");
    assertEquals(6.0, bbox.maxX(), 1e-9, "maxX should be 6");
    assertEquals(4.0, bbox.maxY(), 1e-9, "maxY should be 4");
    assertEquals(0.0, bbox.maxZ(), 1e-9, "maxZ should be 0 (2D face)");
  }

  // ---------------------------------------------------------------------------
  // Edge cases
  // ---------------------------------------------------------------------------

  @Test
  void boundingBoxIsNotEmptyForValidGeometry() throws IOException {
    StepCadBuilder builder = parseFixture("minimal-square.step");
    Solid solid = builder.buildSolid(100);

    BoundingBox3 bbox = solid.boundingBox();
    assertNotNull(bbox);
    assertFalse(bbox.isEmpty(), "bbox should not be empty for valid geometry");
  }
}
