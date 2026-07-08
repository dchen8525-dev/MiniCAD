package com.minicad.tool;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

/**
 * Parser for EXPRESS schema files (ISO 10303-11).
 * Extracts ENTITY definitions with attributes, types, and inheritance.
 *
 * Usage: java ExpressSchemaParser <schema.exp> <output.json>
 */
public class ExpressSchemaParser {

  public static class EntityMetadata {
    String name;
    List<Attribute> attributes = new ArrayList<>();
    List<String> supertypes = new ArrayList<>();
    List<String> subtypes = new ArrayList<>();
    boolean isAbstract = false;

    public JSONObject toJson() {
      JSONObject obj = new JSONObject();
      obj.put("name", name);
      obj.put("is_abstract", isAbstract);

      JSONArray attrsArray = new JSONArray();
      for (Attribute attr : attributes) {
        attrsArray.add(attr.toJson());
      }
      obj.put("attributes", attrsArray);

      JSONArray supertypesArray = new JSONArray();
      supertypesArray.addAll(supertypes);
      obj.put("supertypes", supertypesArray);

      JSONArray subtypesArray = new JSONArray();
      subtypesArray.addAll(subtypes);
      obj.put("subtypes", subtypesArray);

      return obj;
    }
  }

  public static class Attribute {
    String name;
    String type;
    boolean optional = false;
    boolean isList = false;
    String listLowerBound = null;
    String listUpperBound = null;
    boolean isEntityRef = false;
    String referencedEntity = null;

    public JSONObject toJson() {
      JSONObject obj = new JSONObject();
      obj.put("name", name);
      obj.put("type", type);
      obj.put("optional", optional);
      obj.put("is_list", isList);
      if (isList) {
        obj.put("list_lower_bound", listLowerBound);
        obj.put("list_upper_bound", listUpperBound);
      }
      obj.put("is_entity_ref", isEntityRef);
      if (isEntityRef) {
        obj.put("referenced_entity", referencedEntity);
      }
      return obj;
    }
  }

  private Map<String, EntityMetadata> entities = new LinkedHashMap<>();
  private Map<String, List<String>> subtypeRegistry = new HashMap<>();

  /**
   * Parse EXPRESS schema file and extract all entities.
   */
  public Map<String, EntityMetadata> parse(Path schemaPath) throws IOException {
    String content = Files.readString(schemaPath);

    // Remove comments
    content = removeComments(content);

    // Extract all ENTITY blocks
    Pattern entityPattern = Pattern.compile(
      "ENTITY\\s+([A-Za-z0-9_]+)([^;]*?);(.*?)END_ENTITY;",
      Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    Matcher matcher = entityPattern.matcher(content);
    while (matcher.find()) {
      String entityName = matcher.group(1).toUpperCase();
      String headerContent = matcher.group(2); // SUPERTYPE, SUBTYPE, abstract
      String bodyContent = matcher.group(3);    // Attributes

      EntityMetadata entity = parseEntityHeader(entityName, headerContent);
      parseEntityBody(entity, bodyContent);

      entities.put(entityName, entity);
    }

    // Build subtype registry from supertype references
    buildSubtypeRegistry();

    return entities;
  }

  /**
   * Remove EXPRESS comments (both (* *) and -- styles).
   */
  private String removeComments(String content) {
    // Remove (* *) comments
    content = content.replaceAll("\\(\\*.*?\\*\\)", "");
    // Remove -- line comments
    content = content.replaceAll("--[^\n]*", "");
    return content;
  }

  /**
   * Parse ENTITY header (SUPERTYPE, SUBTYPE, abstract declarations).
   */
  private EntityMetadata parseEntityHeader(String name, String header) {
    EntityMetadata entity = new EntityMetadata();
    entity.name = name;

    // Check for ABSTRACT
    if (header.toUpperCase().contains("ABSTRACT")) {
      entity.isAbstract = true;
    }

    // Extract SUPERTYPE OF (ONEOF ...) - subtypes declaration
    Pattern subtypePattern = Pattern.compile(
      "SUPERTYPE\\s+OF\\s+\\(\\s*ONEOF\\s*\\(([^)]+)\\)",
      Pattern.CASE_INSENSITIVE
    );
    Matcher subtypeMatcher = subtypePattern.matcher(header);
    if (subtypeMatcher.find()) {
      String[] subtypes = subtypeMatcher.group(1).split(",");
      for (String st : subtypes) {
        entity.subtypes.add(st.trim().toUpperCase());
      }
    }

    // Extract SUBTYPE OF (parent) - supertype reference
    Pattern supertypePattern = Pattern.compile(
      "SUBTYPE\\s+OF\\s+\\(\\s*([A-Za-z0-9_]+)\\s*\\)",
      Pattern.CASE_INSENSITIVE
    );
    Matcher supertypeMatcher = supertypePattern.matcher(header);
    if (supertypeMatcher.find()) {
      entity.supertypes.add(supertypeMatcher.group(1).toUpperCase());
    }

    return entity;
  }

  /**
   * Parse ENTITY body (attribute definitions).
   */
  private void parseEntityBody(EntityMetadata entity, String body) {
    // Split by semicolons to get individual attribute declarations
    // Skip DERIVE and INVERSE sections
    String[] sections = body.split("(DERIVE|INVERSE|WHERE|UNIQUE)", Pattern.CASE_INSENSITIVE);
    String attrSection = sections[0];

    // Parse each attribute line
    String[] lines = attrSection.split(";");
    for (String line : lines) {
      line = line.trim();
      if (line.isEmpty()) continue;

      Attribute attr = parseAttribute(line);
      if (attr != null) {
        entity.attributes.add(attr);
      }
    }
  }

  /**
   * Parse a single attribute declaration.
   * Format: [OPTIONAL] name : [LIST [lower:upper] OF] type;
   */
  private Attribute parseAttribute(String line) {
    line = line.trim();
    if (line.isEmpty()) return null;

    Attribute attr = new Attribute();

    // Check for OPTIONAL
    if (line.toUpperCase().startsWith("OPTIONAL")) {
      attr.optional = true;
      line = line.substring(8).trim();
    }

    // Split name and type by colon
    int colonIndex = line.indexOf(':');
    if (colonIndex < 0) return null;

    attr.name = line.substring(0, colonIndex).trim();
    String typeSpec = line.substring(colonIndex + 1).trim();

    // Check for LIST [lower:upper] OF pattern
    Pattern listPattern = Pattern.compile(
      "LIST\\s*\\[\\s*(\\d+)\\s*:\\s*(\\?|\\d+)\\s*\\]\\s*OF\\s+(.+)",
      Pattern.CASE_INSENSITIVE
    );
    Matcher listMatcher = listPattern.matcher(typeSpec);

    if (listMatcher.find()) {
      attr.isList = true;
      attr.listLowerBound = listMatcher.group(1);
      attr.listUpperBound = listMatcher.group(2);
      typeSpec = listMatcher.group(3).trim();
    }

    // Check for SET pattern (similar to LIST)
    Pattern setPattern = Pattern.compile(
      "SET\\s*\\[\\s*(\\d+)\\s*:\\s*(\\?|\\d+)\\s*\\]\\s*OF\\s+(.+)",
      Pattern.CASE_INSENSITIVE
    );
    Matcher setMatcher = setPattern.matcher(typeSpec);

    if (setMatcher.find()) {
      attr.isList = true;  // Treat SET as List in Java
      attr.listLowerBound = setMatcher.group(1);
      attr.listUpperBound = setMatcher.group(2);
      typeSpec = setMatcher.group(3).trim();
    }

    // Clean up type name
    attr.type = typeSpec.toUpperCase();

    // Check if this is an entity reference (capitalized name, not primitive)
    if (!isPrimitiveType(attr.type)) {
      attr.isEntityRef = true;
      attr.referencedEntity = attr.type;
    }

    return attr;
  }

  /**
   * Check if type is a EXPRESS primitive.
   */
  private boolean isPrimitiveType(String type) {
    return type.equals("STRING") || type.equals("LABEL") || type.equals("TEXT") ||
           type.equals("INTEGER") || type.equals("REAL") || type.equals("NUMBER") ||
           type.equals("BOOLEAN") || type.equals("LOGICAL") ||
           type.equals("BINARY") || type.equals("LENGTH_MEASURE") ||
           type.equals("PLANE_ANGLE_MEASURE") || type.equals("POSITIVE_PLANE_ANGLE_MEASURE") ||
           type.equals("AREA_MEASURE") || type.equals("VOLUME_MEASURE") ||
           type.equals("MASS_MEASURE") || type.equals("TIME_MEASURE") ||
           type.equals("DURATION") || type.equals("FORCE_MEASURE") ||
           type.equals("PRESSURE_MEASURE") || type.equals("TEMPERATURE_MEASURE") ||
           type.equals("ELECTRIC_CURRENT_MEASURE") ||
           type.equals("LUMINOUS_INTENSITY_MEASURE") ||
           type.equals("AMOUNT_OF_SUBSTANCE_MEASURE") ||
           type.equals("ENERGY_MEASURE") || type.equals("POWER_MEASURE") ||
           type.equals("SPEED_MEASURE") || type.equals("ACCELERATION_MEASURE") ||
           type.equals("ABSORBED_DOSE_MEASURE") || type.equals("RADIOACTIVITY_MEASURE") ||
           type.equals("FREQUENCY_MEASURE") || type.equals("ILLUMINANCE_MEASURE") ||
           type.equals("MAGNETIC_FLUX_DENSITY_MEASURE") ||
           type.equals("MAGNETIC_FLUX_MEASURE") || type.equals("ELECTRIC_CHARGE_MEASURE") ||
           type.equals("ELECTRIC_POTENTIAL_MEASURE") ||
           type.equals("ELECTRIC_CAPACITANCE_MEASURE") ||
           type.equals("ELECTRIC_RESISTANCE_MEASURE") ||
           type.equals("ELECTRIC_CONDUCTANCE_MEASURE") ||
           type.equals("INDUCTANCE_MEASURE") ||
           type.equals("CELSIUS_TEMPERATURE_MEASURE") ||
           type.equals("SI_UNIT_NAME") || type.equals("CONTEXT_DEPENDENT_UNIT_NAME");
  }

  /**
   * Build subtype registry from supertype references.
   */
  private void buildSubtypeRegistry() {
    for (EntityMetadata entity : entities.values()) {
      for (String supertype : entity.supertypes) {
        subtypeRegistry.computeIfAbsent(supertype, k -> new ArrayList<>())
          .add(entity.name);
      }
    }

    // Update supertype entities with their subtypes
    for (Map.Entry<String, List<String>> entry : subtypeRegistry.entrySet()) {
      EntityMetadata supertype = entities.get(entry.getKey());
      if (supertype != null) {
        supertype.subtypes.addAll(entry.getValue());
      }
    }
  }

  /**
   * Output entities to JSON file.
   */
  public void writeJson(Path outputPath) throws IOException {
    JSONObject root = new JSONObject();
    JSONObject entitiesObj = new JSONObject();

    for (Map.Entry<String, EntityMetadata> entry : entities.entrySet()) {
      entitiesObj.put(entry.getKey(), entry.getValue().toJson());
    }

    root.put("entities", entitiesObj);
    root.put("total_count", entities.size());

    Files.writeString(outputPath, root.toJSONString());
  }

  /**
   * Main entry point.
   */
  public static void main(String[] args) throws IOException {
    if (args.length < 2) {
      System.err.println("Usage: java ExpressSchemaParser <schema.exp> <output.json>");
      System.exit(1);
    }

    Path schemaPath = Paths.get(args[0]);
    Path outputPath = Paths.get(args[1]);

    System.out.println("Parsing EXPRESS schema: " + schemaPath);

    ExpressSchemaParser parser = new ExpressSchemaParser();
    Map<String, EntityMetadata> entities = parser.parse(schemaPath);

    System.out.println("Found " + entities.size() + " entities");

    parser.writeJson(outputPath);

    System.out.println("Written to: " + outputPath);

    // Print summary stats
    int abstractCount = 0;
    int withSupertype = 0;
    int withSubtypes = 0;
    int simpleAttrs = 0;
    int complexAttrs = 0;

    for (EntityMetadata entity : entities.values()) {
      if (entity.isAbstract) abstractCount++;
      if (!entity.supertypes.isEmpty()) withSupertype++;
      if (!entity.subtypes.isEmpty()) withSubtypes++;

      for (Attribute attr : entity.attributes) {
        if (attr.isEntityRef || attr.isList) complexAttrs++;
        else simpleAttrs++;
      }
    }

    System.out.println("\nStatistics:");
    System.out.println("  Abstract entities: " + abstractCount);
    System.out.println("  Entities with supertype: " + withSupertype);
    System.out.println("  Entities with subtypes: " + withSubtypes);
    System.out.println("  Simple attributes: " + simpleAttrs);
    System.out.println("  Complex attributes (refs/lists): " + complexAttrs);
  }
}