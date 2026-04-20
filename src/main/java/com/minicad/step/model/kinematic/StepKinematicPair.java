package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;public record StepKinematicPair(int id, String name, String description, StepEntity link1, StepEntity link2) implements StepEntity {}