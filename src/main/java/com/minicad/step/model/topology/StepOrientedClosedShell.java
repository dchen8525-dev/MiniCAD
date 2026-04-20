package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.minicad.step.model.base.StepFaceEntity;

/**
 * Resolved ORIENTED_CLOSED_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param closedShellElement referenced base closed shell
 * @param orientation orientation flag
 */
public record StepOrientedClosedShell(
    int id, String name, StepEntity closedShellElement, boolean orientation) implements StepEntity {

  public List<StepFaceEntity> faces() {
    List<StepFaceEntity> faces = shellFaces(closedShellElement);
    if (orientation) {
      return faces;
    }
    List<StepFaceEntity> reversed = new ArrayList<>(faces);
    Collections.reverse(reversed);
    return List.copyOf(reversed);
  }

  private static List<StepFaceEntity> shellFaces(StepEntity shell) {
    if (shell instanceof StepClosedShell closedShell) {
      return closedShell.faces();
    }
    if (shell instanceof StepOrientedClosedShell orientedClosedShell) {
      return orientedClosedShell.faces();
    }
    throw new IllegalArgumentException("ORIENTED_CLOSED_SHELL requires CLOSED_SHELL-compatible entity");
  }
}
