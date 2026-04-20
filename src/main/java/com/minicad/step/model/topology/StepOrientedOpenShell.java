package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.minicad.step.model.geometry.StepSurfacedOpenShell;
import com.minicad.step.model.base.StepFaceEntity;

/**
 * Resolved ORIENTED_OPEN_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param openShellElement referenced base open shell
 * @param orientation orientation flag
 */
public record StepOrientedOpenShell(
    int id, String name, StepEntity openShellElement, boolean orientation) implements StepEntity {

  public List<StepFaceEntity> faces() {
    List<StepFaceEntity> faces = shellFaces(openShellElement);
    if (orientation) {
      return faces;
    }
    List<StepFaceEntity> reversed = new ArrayList<>(faces);
    Collections.reverse(reversed);
    return List.copyOf(reversed);
  }

  private static List<StepFaceEntity> shellFaces(StepEntity shell) {
    if (shell instanceof StepOpenShell openShell) {
      return openShell.faces();
    }
    if (shell instanceof StepSurfacedOpenShell surfacedOpenShell) {
      return surfacedOpenShell.faces();
    }
    if (shell instanceof StepOrientedOpenShell orientedOpenShell) {
      return orientedOpenShell.faces();
    }
    throw new IllegalArgumentException("ORIENTED_OPEN_SHELL requires OPEN_SHELL-compatible entity");
  }
}
