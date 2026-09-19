package com.minicad.step.semantic;

import java.util.Set;

/**
 * Registry for AP242 SELECT type definitions.
 *
 * SELECT types in STEP are type unions that allow a parameter to accept multiple entity types.
 * This registry provides validation and categorization for common AP242 SELECT types.
 */
public final class SelectTypeRegistry {

  private SelectTypeRegistry() {}

  // ---------------------------------------------------------------------------
  // Measure SELECT types (AP242 Part 1, Section 8.2)
  // ---------------------------------------------------------------------------

  /** Common measure types used in LENGTH_MEASURE_WITH_UNIT and similar entities. */
  public static final Set<String> MEASURE_SELECT_TYPES = Set.of(
      "LENGTH_MEASURE",
      "AREA_MEASURE",
      "VOLUME_MEASURE",
      "PLANE_ANGLE_MEASURE",
      "SOLID_ANGLE_MEASURE",
      "RATIO_MEASURE",
      "PARAMETER_VALUE",
      "POSITIVE_LENGTH_MEASURE",
      "POSITIVE_PLANE_ANGLE_MEASURE",
      "POSITIVE_RATIO_MEASURE",
      "DESCRIPTIVE_MEASURE",
      "MEASURE_VALUE");

  // ---------------------------------------------------------------------------
  // All known SELECT types combined
  // ---------------------------------------------------------------------------

  /** All known AP242 SELECT type names. */
  public static final Set<String> ALL_SELECT_TYPES = Set.of(
      // Measure types
      "LENGTH_MEASURE",
      "AREA_MEASURE",
      "VOLUME_MEASURE",
      "PLANE_ANGLE_MEASURE",
      "SOLID_ANGLE_MEASURE",
      "RATIO_MEASURE",
      "PARAMETER_VALUE",
      "POSITIVE_LENGTH_MEASURE",
      "POSITIVE_PLANE_ANGLE_MEASURE",
      "POSITIVE_RATIO_MEASURE",
      "DESCRIPTIVE_MEASURE",
      "MEASURE_VALUE",
      // Action types
      "action_method",
      "action_method_assignment_select",
      "action_request_solution",
      "action_directive",
      // Definition types
      "characterized_definition",
      "characterized_product_definition",
      "characterized_object",
      // Geometric types
      "geometric_model_select",
      "geometric_set_select",
      "transformed_shape_with_scale_and_mirror",
      // Representation types
      "representation_item",
      "founded_item_select",
      "representation_or_representation_item",
      "INTEGER_REPRESENTATION_ITEM",
      "REAL_REPRESENTATION_ITEM",
      "NUMBER_REPRESENTATION_ITEM",
      "BOOLEAN_REPRESENTATION_ITEM",
      "STRING_REPRESENTATION_ITEM",
      "DESCRIPTIVE_REPRESENTATION_ITEM",
      // Organization types
      "organization",
      "person",
      "person_and_organization",
      // Date/time types
      "date",
      "date_and_time",
      "calendar_date");

  // ---------------------------------------------------------------------------
  // Validation methods
  // ---------------------------------------------------------------------------

  /**
   * Checks if the given type name is a known AP242 SELECT type.
   *
   * @param typeName the SELECT type wrapper name (e.g., "LENGTH_MEASURE")
   * @return true if the type name is a known SELECT type
   */
  public static boolean isValidSelectType(String typeName) {
    return ALL_SELECT_TYPES.contains(typeName);
  }



}
