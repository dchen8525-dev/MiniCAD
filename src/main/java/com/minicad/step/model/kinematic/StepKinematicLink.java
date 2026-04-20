package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;public record StepKinematicLink(int id, String name, String description, StepEntity shape) implements StepEntity {}