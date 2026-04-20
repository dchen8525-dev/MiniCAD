package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;public record StepActionMethod(int id, String name, String description, String method) implements StepEntity {}