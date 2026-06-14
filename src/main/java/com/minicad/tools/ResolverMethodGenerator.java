package com.minicad.tools;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONArray;

/**
 * Resolver Method Generator - Generates resolver methods for StepEntityResolver.
 *
 * Input:
 * - Priority queue (generated/priority-queue.tsv)
 * - Entity catalog (generated/ap242-entity-catalog.json)
 *
 * Output:
 * - Resolver method fragments (generated/resolver-methods.txt)
 * - Registration code fragments (generated/registration-code.txt)
 *
 * Template pattern based on existing resolver methods in StepEntityResolver.java.
 */
public class ResolverMethodGenerator {

  private JSONObject entityCatalog;
  private List<String> priorityQueue;

  /**
   * Load entity catalog.
   */
  public void loadCatalog(Path catalogPath) throws IOException {
    String content = Files.readString(catalogPath);
    entityCatalog = JSONObject.parseObject(content);
  }

  /**
   * Load priority queue.
   */
  public void loadPriorityQueue(Path queuePath) throws IOException {
    priorityQueue = new ArrayList<>();
    List<String> lines = Files.readAllLines(queuePath);
    for (String line : lines) {
      if (line.startsWith("entity\t")) continue; // Skip header
      String[] parts = line.split("\t");
      if (parts.length > 0) {
        priorityQueue.add(parts[0]);
      }
    }
  }

  /**
   * Generate resolver method for a single entity.
   */
  public String generateResolverMethod(String entityName) {
    JSONObject entities = entityCatalog.getJSONObject("entities");
    JSONObject entityObj = entities.getJSONObject(entityName);

    if (entityObj == null) {
      return null; // Entity not found
    }

    String className = "Step" + toCamelCase(entityName);
    String methodName = "resolve" + toCamelCase(entityName);
    JSONArray attributes = entityObj.getJSONArray("attributes");

    StringBuilder sb = new StringBuilder();

    // Method header
    sb.append("  /**\n");
    sb.append("   * Resolve ").append(entityName).append(" entity.\n");
    sb.append("   */\n");
    sb.append("  ").append(className).append(" ").append(methodName)
      .append("(StepEntityInstance instance) {\n");

    // Get definition
    sb.append("    StepEntityDefinition definition = definition(instance, \"")
      .append(entityName).append("\");\n");

    // Validate parameter count
    int paramCount = attributes != null ? attributes.size() : 0;
    sb.append("    requireParameterCount(instance, definition, ").append(paramCount).append(");\n\n");

    // Extract parameters
    sb.append("    return new ").append(className).append("(\n");
    sb.append("        instance.id(),\n");
    sb.append("        stringValue(instance, definition, 0)");

    for (int i = 0; i < paramCount; i++) {
      JSONObject attr = attributes.getJSONObject(i);
      String extractor = getExtractor(attr, i);

      sb.append(",\n        ").append(extractor);
    }

    sb.append(");\n");
    sb.append("  }\n");

    return sb.toString();
  }

  /**
   * Get extractor method call for an attribute.
   */
  private String getExtractor(JSONObject attr, int index) {
    String type = attr.getString("type");
    boolean optional = attr.getBooleanValue("optional");
    boolean isList = attr.getBooleanValue("is_list");
    boolean isEntityRef = attr.getBooleanValue("is_entity_ref");

    if (isEntityRef) {
      String refEntity = attr.getString("referenced_entity");
      String refClass = "Step" + toCamelCase(refEntity);

      if (optional) {
        return "optionalEntity(referenceId(instance, definition, " + (index + 1) + "), " + refClass + ".class, resolver)";
      } else {
        return "requireEntity(referenceId(instance, definition, " + (index + 1) + "), " + refClass + ".class, resolver)";
      }
    }

    if (isList) {
      if (isEntityRef) {
        return "entityList(instance, definition, " + (index + 1) + ", resolver)";
      } else {
        return "literalList(instance, definition, " + (index + 1) + ")";
      }
    }

    // Primitive types
    switch (type) {
      case "STRING":
      case "LABEL":
      case "TEXT":
        return optional ?
          "optionalStringValue(instance, definition, " + (index + 1) + ")" :
          "stringValue(instance, definition, " + (index + 1) + ")";
      case "INTEGER":
        return optional ?
          "optionalIntegerValue(instance, definition, " + (index + 1) + ")" :
          "integerValue(instance, definition, " + (index + 1) + ")";
      case "REAL":
      case "NUMBER":
        return optional ?
          "optionalNumberValue(instance, definition, " + (index + 1) + ")" :
          "numberValue(instance, definition, " + (index + 1) + ")";
      case "BOOLEAN":
      case "LOGICAL":
        return optional ?
          "optionalBooleanValue(instance, definition, " + (index + 1) + ")" :
          "booleanValue(instance, definition, " + (index + 1) + ")";
      default:
        // Custom type - treat as string or enum
        return optional ?
          "optionalStringValue(instance, definition, " + (index + 1) + ")" :
          "stringValue(instance, definition, " + (index + 1) + ")";
    }
  }

  /**
   * Generate registration code for an entity.
   */
  public String generateRegistrationCode(String entityName) {
    String methodName = "resolve" + toCamelCase(entityName);

    return "    registry.put(\"" + entityName + "\", StepEntityResolver::" + methodName + ");";
  }

  /**
   * Generate all resolver methods and registration code.
   */
  public void generateAll() throws IOException {
    List<String> resolverMethods = new ArrayList<>();
    List<String> registrationCodes = new ArrayList<>();

    for (String entityName : priorityQueue) {
      String resolver = generateResolverMethod(entityName);
      if (resolver != null) {
        resolverMethods.add(resolver);
        registrationCodes.add(generateRegistrationCode(entityName));
      }
    }

    // Write resolver methods
    Path resolverPath = Paths.get("generated/resolver-methods.txt");
    Files.write(resolverPath, resolverMethods);
    System.out.println("Written resolver methods: " + resolverPath);

    // Write registration code
    Path registrationPath = Paths.get("generated/registration-code.txt");
    Files.write(registrationPath, registrationCodes);
    System.out.println("Written registration code: " + registrationPath);
  }

  /**
   * Detect alias families (entities with identical structure).
   */
  public Map<String, List<String>> detectAliasFamilies() {
    Map<String, List<String>> families = new HashMap<>();

    JSONObject entities = entityCatalog.getJSONObject("entities");

    // Group by attribute signature
    Map<String, List<String>> signatureGroups = new HashMap<>();

    for (String entityName : priorityQueue) {
      JSONObject entityObj = entities.getJSONObject(entityName);
      if (entityObj == null) continue;

      JSONArray attributes = entityObj.getJSONArray("attributes");
      String signature = generateAttributeSignature(attributes);

      signatureGroups.computeIfAbsent(signature, k -> new ArrayList<>())
        .add(entityName);
    }

    // Find groups with more than 1 entity (potential aliases)
    for (Map.Entry<String, List<String>> entry : signatureGroups.entrySet()) {
      if (entry.getValue().size() > 1) {
        String baseEntity = entry.getValue().get(0);
        families.put(baseEntity, entry.getValue());
      }
    }

    return families;
  }

  /**
   * Generate attribute signature for comparison.
   */
  private String generateAttributeSignature(JSONArray attributes) {
    if (attributes == null) return "EMPTY";

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < attributes.size(); i++) {
      JSONObject attr = attributes.getJSONObject(i);
      sb.append(attr.getString("type"));
      sb.append(attr.getBooleanValue("optional") ? "?" : "");
      sb.append(attr.getBooleanValue("is_list") ? "[]" : "");
      sb.append(",");
    }
    return sb.toString();
  }

  /**
   * Write alias family registration helper methods.
   */
  public void writeAliasHelpers(Path outputPath) throws IOException {
    Map<String, List<String>> families = detectAliasFamilies();

    List<String> lines = new ArrayList<>();
    lines.add("// Alias family registration helpers (auto-generated)\n");

    for (Map.Entry<String, List<String>> entry : families.entrySet()) {
      String baseEntity = entry.getKey();
      List<String> aliases = entry.getValue();

      if (aliases.size() > 5) { // Only for significant alias families
        String methodName = "register" + toCamelCase(baseEntity) + "Aliases";

        lines.add("  private static void " + methodName + "(Map<String, EntityFactory> registry) {");
        lines.add("    // Entities sharing same structure:");
        for (String alias : aliases) {
          lines.add("    registry.put(\"" + alias + "\",");
          lines.add("        (resolver, instance) -> resolver.resolve" + toCamelCase(baseEntity) + "(instance));");
        }
        lines.add("  }\n");
      }
    }

    Files.write(outputPath, lines);
    System.out.println("Written alias helpers: " + outputPath);
  }

  /**
   * Convert UPPER_CASE to CamelCase.
   */
  private String toCamelCase(String upper) {
    if (upper == null) return "";
    String[] parts = upper.toLowerCase().split("_");
    StringBuilder sb = new StringBuilder();
    for (String part : parts) {
      if (!part.isEmpty()) {
        sb.append(capitalize(part));
      }
    }
    return sb.toString();
  }

  /**
   * Capitalize first letter.
   */
  private String capitalize(String str) {
    if (str == null || str.isEmpty()) return str;
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }

  /**
   * Main entry point.
   */
  public static void main(String[] args) throws IOException {
    ResolverMethodGenerator generator = new ResolverMethodGenerator();

    System.out.println("Loading entity catalog...");
    generator.loadCatalog(Paths.get("generated/ap242-entity-catalog.json"));

    System.out.println("Loading priority queue...");
    generator.loadPriorityQueue(Paths.get("generated/priority-queue.tsv"));

    System.out.println("Generating resolver methods...");
    generator.generateAll();

    System.out.println("Detecting alias families...");
    generator.writeAliasHelpers(Paths.get("generated/alias-helpers.txt"));

    System.out.println("Done!");
  }
}