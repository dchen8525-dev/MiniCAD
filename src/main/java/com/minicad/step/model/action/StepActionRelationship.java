package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;public record StepActionRelationship(int id, String name, String description, StepEntity relatingAction, StepEntity relatedAction) implements StepEntity {}