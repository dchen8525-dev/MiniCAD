package com.minicad.step.semantic;

import java.util.Map;
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
  // Action SELECT types (AP242 Part 1, Section 7.3)
  // ---------------------------------------------------------------------------

  /** Action-related SELECT types for process modeling. */
  public static final Set<String> ACTION_SELECT_TYPES = Set.of(
      "action_method",
      "action_method_assignment_select",
      "action_request_solution",
      "action_directive");

  // ---------------------------------------------------------------------------
  // Definition SELECT types (AP242 Part 1, Section 6.2)
  // ---------------------------------------------------------------------------

  /** Characterized definition types for product characterization. */
  public static final Set<String> DEFINITION_SELECT_TYPES = Set.of(
      "characterized_definition",
      "characterized_product_definition",
      "characterized_object");

  // ---------------------------------------------------------------------------
  // Geometric SELECT types (AP242 Part 1, Section 5.2)
  // ---------------------------------------------------------------------------

  /** Geometric model and set SELECT types. */
  public static final Set<String> GEOMETRIC_SELECT_TYPES = Set.of(
      "geometric_model_select",
      "geometric_set_select",
      "transformed_shape_with_scale_and_mirror");

  // ---------------------------------------------------------------------------
  // Representation SELECT types
  // ---------------------------------------------------------------------------

  /** Representation item SELECT types. */
  public static final Set<String> REPRESENTATION_SELECT_TYPES = Set.of(
      "representation_item",
      "founded_item_select",
      "representation_or_representation_item",
      // Additional representation item wrappers found in test files
      "INTEGER_REPRESENTATION_ITEM",
      "REAL_REPRESENTATION_ITEM",
      "NUMBER_REPRESENTATION_ITEM",
      "BOOLEAN_REPRESENTATION_ITEM",
      "STRING_REPRESENTATION_ITEM",
      "DESCRIPTIVE_REPRESENTATION_ITEM");

  // ---------------------------------------------------------------------------
  // Organization and Person SELECT types
  // ---------------------------------------------------------------------------

  /** Organization and person SELECT types. */
  public static final Set<String> ORGANIZATION_SELECT_TYPES = Set.of(
      "organization",
      "person",
      "person_and_organization");

  // ---------------------------------------------------------------------------
  // Date and Time SELECT types
  // ---------------------------------------------------------------------------

  /** Date and time SELECT types. */
  public static final Set<String> DATETIME_SELECT_TYPES = Set.of(
      "date",
      "date_and_time",
      "calendar_date");

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
  // Category mapping
  // ---------------------------------------------------------------------------

  private static final Map<String, String> SELECT_TYPE_TO_CATEGORY = Map.ofEntries(
      // Measure types -> "measure"
      Map.entry("LENGTH_MEASURE", "measure"),
      Map.entry("AREA_MEASURE", "measure"),
      Map.entry("VOLUME_MEASURE", "measure"),
      Map.entry("PLANE_ANGLE_MEASURE", "measure"),
      Map.entry("SOLID_ANGLE_MEASURE", "measure"),
      Map.entry("RATIO_MEASURE", "measure"),
      Map.entry("PARAMETER_VALUE", "measure"),
      Map.entry("POSITIVE_LENGTH_MEASURE", "measure"),
      Map.entry("POSITIVE_PLANE_ANGLE_MEASURE", "measure"),
      Map.entry("POSITIVE_RATIO_MEASURE", "measure"),
      Map.entry("DESCRIPTIVE_MEASURE", "measure"),
      Map.entry("MEASURE_VALUE", "measure"),
      // Action types -> "action"
      Map.entry("action_method", "action"),
      Map.entry("action_method_assignment_select", "action"),
      Map.entry("action_request_solution", "action"),
      Map.entry("action_directive", "action"),
      // Definition types -> "definition"
      Map.entry("characterized_definition", "definition"),
      Map.entry("characterized_product_definition", "definition"),
      Map.entry("characterized_object", "definition"),
      // Geometric types -> "geometric"
      Map.entry("geometric_model_select", "geometric"),
      Map.entry("geometric_set_select", "geometric"),
      Map.entry("transformed_shape_with_scale_and_mirror", "geometric"),
      // Representation types -> "representation"
      Map.entry("representation_item", "representation"),
      Map.entry("founded_item_select", "representation"),
      Map.entry("representation_or_representation_item", "representation"),
      Map.entry("INTEGER_REPRESENTATION_ITEM", "representation"),
      Map.entry("REAL_REPRESENTATION_ITEM", "representation"),
      Map.entry("NUMBER_REPRESENTATION_ITEM", "representation"),
      Map.entry("BOOLEAN_REPRESENTATION_ITEM", "representation"),
      Map.entry("STRING_REPRESENTATION_ITEM", "representation"),
      Map.entry("DESCRIPTIVE_REPRESENTATION_ITEM", "representation"),
      // Organization types -> "organization"
      Map.entry("organization", "organization"),
      Map.entry("person", "organization"),
      Map.entry("person_and_organization", "organization"),
      // Date/time types -> "datetime"
      Map.entry("date", "datetime"),
      Map.entry("date_and_time", "datetime"),
      Map.entry("calendar_date", "datetime"));

  private static final Map<String, Set<String>> CATEGORY_TO_TYPES = Map.of(
      "measure", MEASURE_SELECT_TYPES,
      "action", ACTION_SELECT_TYPES,
      "definition", DEFINITION_SELECT_TYPES,
      "geometric", GEOMETRIC_SELECT_TYPES,
      "representation", REPRESENTATION_SELECT_TYPES,
      "organization", ORGANIZATION_SELECT_TYPES,
      "datetime", DATETIME_SELECT_TYPES);

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

  /**
   * Gets the category for a SELECT type name.
   *
   * @param typeName the SELECT type wrapper name
   * @return the category (e.g., "measure", "action") or null if unknown
   */
  public static String getSelectCategory(String typeName) {
    return SELECT_TYPE_TO_CATEGORY.get(typeName);
  }

  /**
   * Gets all allowed type names for a category.
   *
   * @param category the category name (e.g., "measure", "action")
   * @return the set of allowed type names for that category
   */
  public static Set<String> getAllowedTypesForCategory(String category) {
    return CATEGORY_TO_TYPES.getOrDefault(category, Set.of());
  }

  /**
   * Validates that the SELECT type name matches one of the allowed types.
   *
   * @param typeName the SELECT type wrapper name to validate
   * @param allowedTypes the set of allowed type names
   * @return true if the type name is in the allowed set
   */
  public static boolean isSelectTypeAllowed(String typeName, Set<String> allowedTypes) {
    return allowedTypes.contains(typeName);
  }

  /**
   * Returns a human-readable description of the SELECT type categories.
   *
   * @return formatted description of available categories
   */
  public static String getCategoryDescription() {
    return "Available SELECT type categories:\n"
        + "  - measure: LENGTH_MEASURE, AREA_MEASURE, VOLUME_MEASURE, etc.\n"
        + "  - action: action_method, action_request_solution, etc.\n"
        + "  - definition: characterized_definition, characterized_product_definition\n"
        + "  - geometric: geometric_model_select, geometric_set_select\n"
        + "  - representation: representation_item, founded_item_select\n"
        + "  - organization: organization, person, person_and_organization\n"
        + "  - datetime: date, date_and_time, calendar_date";
  }
}