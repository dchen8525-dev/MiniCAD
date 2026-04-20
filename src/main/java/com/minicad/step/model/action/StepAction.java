package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;public record StepAction(int id, String name, String description, String actionMethod) implements StepEntity {}