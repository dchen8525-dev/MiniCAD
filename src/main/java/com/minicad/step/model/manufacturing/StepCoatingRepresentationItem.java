package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved COATING_REPRESENTATION_ITEM.
 * A coating representation item entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param coatingType coating variance type
 * @param coatingThickness coating variance thickness
 * @param coatingUnit coating variance unit reference
 * @param coatingStatus coating variance status
 */
public record StepCoatingRepresentationItem(
    int id,
    String name,
    String coatingType,
    double coatingThickness,
    StepEntity coatingUnit,
    String coatingStatus) implements StepEntity {
}