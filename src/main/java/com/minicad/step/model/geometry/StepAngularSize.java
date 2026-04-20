package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;public record StepAngularSize(int id, String name, String description, double angle) implements StepEntity {}