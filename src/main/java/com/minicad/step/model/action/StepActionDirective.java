package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;public record StepActionDirective(int id, String name, String description, String directive) implements StepEntity {}