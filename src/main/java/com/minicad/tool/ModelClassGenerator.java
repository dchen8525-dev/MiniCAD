package com.minicad.tool;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONArray;

/**
 * Model Class Generator - Generates Java model classes from EXPRESS metadata.
 *
 * Input:
 * - Priority queue (generated/priority-queue.tsv)
 * - Entity catalog (generated/ap242-entity-catalog.json)
 *
 * Output:
 * - Java model class files (src/main/java/com/minicad/step/model/{domain}/)
 *
 * Template pattern based on existing 1,246 model classes in MiniCAD.
 */
public class ModelClassGenerator {

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
   * Load priority queue (entities to generate).
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
   * Generate model class for a single entity.
   */
  public String generateModelClass(String entityName) {
    JSONObject entities = entityCatalog.getJSONObject("entities");
    JSONObject entityObj = entities.getJSONObject(entityName);

    if (entityObj == null) {
      return null; // Entity not found
    }

    String domain = classifyDomain(entityName);
    String className = "Step" + toCamelCase(entityName);
    JSONArray attributes = entityObj.getJSONArray("attributes");

    StringBuilder sb = new StringBuilder();

    // Package declaration
    sb.append("package com.minicad.step.model.").append(domain).append(";\n\n");

    // Imports
    Set<String> imports = new TreeSet<>();
    imports.add("com.minicad.step.model.StepEntity");

    for (int i = 0; i < attributes.size(); i++) {
      JSONObject attr = attributes.getJSONObject(i);
      String type = attr.getString("type");
      if (attr.getBooleanValue("is_list")) {
        imports.add("java.util.List");
      }
      if (attr.getBooleanValue("is_entity_ref")) {
        // Add import for referenced entity type
        String refEntity = attr.getString("referenced_entity");
        if (refEntity != null && !refEntity.isEmpty()) {
          String refDomain = classifyDomain(refEntity);
          imports.add("com.minicad.step.model." + refDomain + ".Step" + toCamelCase(refEntity));
        }
      }
    }

    for (String imp : imports) {
      sb.append("import ").append(imp).append(";\n");
    }
    sb.append("\n");

    // Class header
    sb.append("/**\n");
    sb.append(" * ").append(entityName).append(" entity model.\n");
    sb.append(" */\n");
    sb.append("public final class ").append(className).append(" implements StepEntity {\n\n");

    // Fields
    sb.append("  private final int id;\n");
    sb.append("  private final String name;\n");

    for (int i = 0; i < attributes.size(); i++) {
      JSONObject attr = attributes.getJSONObject(i);
      String fieldName = toCamelCase(attr.getString("name"));
      String javaType = mapToJavaType(attr);

      sb.append("  private final ").append(javaType).append(" ").append(fieldName).append(";\n");
    }
    sb.append("\n");

    // Constructor
    sb.append("  public ").append(className).append("(\n");
    sb.append("      int id,\n");
    sb.append("      String name");

    for (int i = 0; i < attributes.size(); i++) {
      JSONObject attr = attributes.getJSONObject(i);
      String fieldName = toCamelCase(attr.getString("name"));
      String javaType = mapToJavaType(attr);

      sb.append(",\n      ").append(javaType).append(" ").append(fieldName);
    }
    sb.append(") {\n");

    sb.append("    this.id = id;\n");
    sb.append("    this.name = name;\n");

    for (int i = 0; i < attributes.size(); i++) {
      JSONObject attr = attributes.getJSONObject(i);
      String fieldName = toCamelCase(attr.getString("name"));

      sb.append("    this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
    }
    sb.append("  }\n\n");

    // Getters
    sb.append("  @Override\n");
    sb.append("  public int getId() {\n");
    sb.append("    return id;\n");
    sb.append("  }\n\n");

    sb.append("  @Override\n");
    sb.append("  public String getName() {\n");
    sb.append("    return name;\n");
    sb.append("  }\n\n");

    for (int i = 0; i < attributes.size(); i++) {
      JSONObject attr = attributes.getJSONObject(i);
      String fieldName = toCamelCase(attr.getString("name"));
      String javaType = mapToJavaType(attr);

      sb.append("  public ").append(javaType).append(" get").append(capitalize(fieldName))
        .append("() {\n");
      sb.append("    return ").append(fieldName).append(";\n");
      sb.append("  }\n\n");
    }

    // equals
    sb.append("  @Override\n");
    sb.append("  public boolean equals(Object o) {\n");
    sb.append("    if (this == o) return true;\n");
    sb.append("    if (o == null || getClass() != o.getClass()) return false;\n");
    sb.append("    ").append(className).append(" that = (").append(className).append(") o;\n");
    sb.append("    return id == that.id;\n");
    sb.append("  }\n\n");

    // hashCode
    sb.append("  @Override\n");
    sb.append("  public int hashCode() {\n");
    sb.append("    return Integer.hashCode(id);\n");
    sb.append("  }\n\n");

    // toString
    sb.append("  @Override\n");
    sb.append("  public String toString() {\n");
    sb.append("    return \"").append(className).append("{\" +\n");
    sb.append("        \"id=\" + id +\n");
    sb.append("        \", name='\" + name + '\\'' +\n");
    for (int i = 0; i < attributes.size(); i++) {
      JSONObject attr = attributes.getJSONObject(i);
      String fieldName = toCamelCase(attr.getString("name"));
      sb.append("        \", ").append(fieldName).append("=").append(fieldName);
      if (i < attributes.size() - 1) {
        sb.append(" +\n");
      } else {
        sb.append(" +\n        '}';\n");
      }
    }
    if (attributes.isEmpty()) {
      sb.append("        '}';\n");
    }
    sb.append("  }\n");

    sb.append("}\n");

    return sb.toString();
  }

  /**
   * Map EXPRESS type to Java type.
   */
  private String mapToJavaType(JSONObject attr) {
    String type = attr.getString("type");

    boolean isList = attr.getBooleanValue("is_list");
    boolean isEntityRef = attr.getBooleanValue("is_entity_ref");

    String baseType;
    if (isEntityRef) {
      String refEntity = attr.getString("referenced_entity");
      baseType = "Step" + toCamelCase(refEntity);
    } else if (isPrimitiveType(type)) {
      baseType = mapPrimitiveType(type);
    } else {
      // Custom type - treat as String for now
      baseType = "String";
    }

    if (isList) {
      return "List<" + baseType + ">";
    } else {
      return baseType;
    }
  }

  /**
   * Map EXPRESS primitive to Java type.
   */
  private String mapPrimitiveType(String expressType) {
    switch (expressType) {
      case "STRING":
      case "LABEL":
      case "TEXT":
        return "String";
      case "INTEGER":
        return "int";
      case "REAL":
      case "NUMBER":
      case "LENGTH_MEASURE":
      case "PLANE_ANGLE_MEASURE":
      case "AREA_MEASURE":
      case "VOLUME_MEASURE":
      case "MASS_MEASURE":
      case "TIME_MEASURE":
      case "DURATION":
        return "double";
      case "BOOLEAN":
      case "LOGICAL":
        return "boolean";
      default:
        return "String";
    }
  }

  /**
   * Check if type is EXPRESS primitive.
   */
  private boolean isPrimitiveType(String type) {
    return type.equals("STRING") || type.equals("LABEL") || type.equals("TEXT") ||
           type.equals("INTEGER") || type.equals("REAL") || type.equals("NUMBER") ||
           type.equals("BOOLEAN") || type.equals("LOGICAL") ||
           type.startsWith("LENGTH_MEASURE") || type.startsWith("PLANE_ANGLE_MEASURE");
  }

  /**
   * Classify entity domain (for package placement).
   */
  private String classifyDomain(String entityName) {
    entityName = entityName.toUpperCase();

    if (entityName.contains("GEOMETRY") || entityName.contains("CURVE") ||
        entityName.contains("SURFACE") || entityName.contains("POINT") ||
        entityName.contains("LINE") || entityName.contains("PLANE") ||
        entityName.contains("CIRCLE") || entityName.contains("ELLIPSE") ||
        entityName.contains("BSPLINE") || entityName.contains("AXIS") ||
        entityName.contains("DIRECTION") || entityName.contains("VECTOR") ||
        entityName.contains("TRANSFORMATION")) {
      return "geometry";
    }

    if (entityName.contains("VERTEX") || entityName.contains("EDGE") ||
        entityName.contains("FACE") || entityName.contains("SHELL") ||
        entityName.contains("LOOP") || entityName.contains("BOUND") ||
        entityName.contains("TOPOLOGY") || entityName.contains("BREP")) {
      return "topology";
    }

    if (entityName.contains("ANNOTATION") || entityName.contains("PMI") ||
        entityName.contains("DRAUGHTING") || entityName.contains("TEXT") ||
        entityName.contains("SYMBOL") || entityName.contains("COLOUR") ||
        entityName.contains("STYLE") || entityName.contains("PRESENTATION")) {
      return "annotation";
    }

    if (entityName.contains("TOLERANCE") || entityName.contains("DATUM") ||
        entityName.contains("DIMENSION") || entityName.contains("MODIFIER")) {
      return "tolerance";
    }

    if (entityName.contains("PRODUCT") || entityName.contains("ASSEMBLY") ||
        entityName.contains("COMPONENT") || entityName.contains("DEFINITION") ||
        entityName.contains("SHAPE") || entityName.contains("REPRESENTATION")) {
      return "product";
    }

    if (entityName.contains("MEASURE") || entityName.contains("UNIT")) {
      return "unit";
    }

    if (entityName.contains("ACTION") || entityName.contains("PROCESS") ||
        entityName.contains("WORKFLOW") || entityName.contains("STEP")) {
      return "action";
    }

    if (entityName.contains("KINEMATIC") || entityName.contains("MECHANISM") ||
        entityName.contains("PAIR") || entityName.contains("JOINT") ||
        entityName.contains("LINK") || entityName.contains("MOTION")) {
      return "kinematic";
    }

    if (entityName.contains("FEA") || entityName.contains("ELEMENT") ||
        entityName.contains("MESH") || entityName.contains("NODE") ||
        entityName.contains("BOUNDARY_CONDITION")) {
      return "fea";
    }

    if (entityName.contains("MANUFACTURING") || entityName.contains("FEATURE") ||
        entityName.contains("MACHINING") || entityName.contains("OPERATION") ||
        entityName.contains("TOOL") || entityName.contains("WORKPLAN")) {
      return "manufacturing";
    }

    if (entityName.contains("APPLIED") || entityName.contains("ASSIGNMENT") ||
        entityName.contains("CLASSIFICATION") || entityName.contains("GROUP")) {
      return "classification";
    }

    if (entityName.contains("VALIDATION") || entityName.contains("VERIFICATION") ||
        entityName.contains("INSPECTION") || entityName.contains("A3M") ||
        entityName.contains("TEST") || entityName.contains("RESULT")) {
      return "validation";
    }

    if (entityName.contains("APPROVAL") || entityName.contains("CERTIFICATION")) {
      return "approval";
    }

    if (entityName.contains("DOCUMENT") || entityName.contains("FILE") ||
        entityName.contains("SPECIFICATION")) {
      return "document";
    }

    if (entityName.contains("ORGANIZATION") || entityName.contains("PERSON") ||
        entityName.contains("ADDRESS") || entityName.contains("DEPARTMENT")) {
      return "organization";
    }

    if (entityName.contains("DATE") || entityName.contains("TIME") ||
        entityName.contains("CALENDAR") || entityName.contains("SCHEDULE")) {
      return "date_time";
    }

    if (entityName.contains("CONFIG") || entityName.contains("CHANGE") ||
        entityName.contains("VERSION") || entityName.contains("AUDIT") ||
        entityName.contains("BASELINE")) {
      return "config_mgmt";
    }

    if (entityName.contains("SECURITY") || entityName.contains("ACCESS") ||
        entityName.contains("PERMISSION") || entityName.contains("AUTH")) {
      return "security";
    }

    if (entityName.contains("RESOURCE") || entityName.contains("COST") ||
        entityName.contains("JOB") || entityName.contains("CAPABILITY")) {
      return "resource";
    }

    if (entityName.contains("LOG") || entityName.contains("RECORD") ||
        entityName.contains("ENTRY") || entityName.contains("HISTORY")) {
      return "log_audit";
    }

    if (entityName.contains("BACKUP") || entityName.contains("ARCHIVE") ||
        entityName.contains("RECOVERY") || entityName.contains("RESTORE")) {
      return "backup_recovery";
    }

    if (entityName.contains("ANALYSIS") || entityName.contains("CALCULATION") ||
        entityName.contains("STUDY") || entityName.contains("MODEL")) {
      return "analysis";
    }

    if (entityName.contains("PROFILE") || entityName.contains("PROPERTY")) {
      return "profile";
    }

    if (entityName.contains("SYSTEM") || entityName.contains("PLATFORM") ||
        entityName.contains("ENVIRONMENT")) {
      return "system";
    }

    return "misc";
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
   * Generate model classes for entities in priority queue.
   * Outputs to src/main/java/com/minicad/step/model/{domain}/ directories.
   */
  public void generateAll(Path baseOutputPath) throws IOException {
    for (String entityName : priorityQueue) {
      String code = generateModelClass(entityName);
      if (code == null) continue;

      String domain = classifyDomain(entityName);
      String className = "Step" + toCamelCase(entityName);

      Path packagePath = baseOutputPath.resolve(domain);
      Files.createDirectories(packagePath);

      Path file = packagePath.resolve(className + ".java");
      Files.writeString(file, code);

      System.out.println("Generated: " + file);
    }
  }

  /**
   * Main entry point.
   */
  public static void main(String[] args) throws IOException {
    ModelClassGenerator generator = new ModelClassGenerator();

    System.out.println("Loading entity catalog...");
    generator.loadCatalog(Paths.get("generated/ap242-entity-catalog.json"));

    System.out.println("Loading priority queue...");
    generator.loadPriorityQueue(Paths.get("generated/priority-queue.tsv"));

    System.out.println("Generating model classes...");
    Path basePath = Paths.get("src/main/java/com/minicad/step/model");
    generator.generateAll(basePath);

    System.out.println("Done!");
  }
}
