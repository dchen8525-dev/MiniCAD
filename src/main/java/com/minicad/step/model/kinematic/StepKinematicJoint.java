package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;public record StepKinematicJoint(int id, String name, String description, StepEntity jointGeometry) implements StepEntity {}