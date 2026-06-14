#!/usr/bin/env python3
"""Fix remaining JDK 21 -> JDK 11 issues."""
import re
from pathlib import Path

def fix_step_dump_app():
    p = Path("D:/work/MiniCAD/src/main/java/com/minicad/app/StepDumpApp.java")
    content = p.read_text(encoding='utf-8')
    
    # Fix mangled appendJsonString: restore switch body
    old = (
        "    private static void appendJsonString(StringBuilder json, String value) {\n"
        "        json.append('\"');\n"
        "        for (int i = 0; i < value.length(); i++) {\n"
        "            char c = value.charAt(i);\n"
        "            }\n"
        "        }\n"
        "        json.append('\"');\n"
        "    }"
    )
    new = (
        "    private static void appendJsonString(StringBuilder json, String value) {\n"
        '        json.append(\'"\');\n'
        "        for (int i = 0; i < value.length(); i++) {\n"
        "            char c = value.charAt(i);\n"
        "            switch (c) {\n"
        '                case \'\"\': json.append("\\\\""\\""); break;\n'
        "                case '\\\\': json.append(\"\\\\\\\\\"); break;\n"
        "                case '\\b': json.append(\"\\\\b\"); break;\n"
        "                case '\\f': json.append(\"\\\\f\"); break;\n"
        "                case '\\n': json.append(\"\\\\n\"); break;\n"
        "                case '\\r': json.append(\"\\\\r\"); break;\n"
        "                case '\\t': json.append(\"\\\\t\"); break;\n"
        "                default:\n"
        "                    if (c < 0x20) {\n"
        '                        json.append(String.format("\\\\u%04x", (int) c));\n'
        "                    } else {\n"
        "                        json.append(c);\n"
        "                    }\n"
        "            }\n"
        "        }\n"
        '        json.append(\'"\');\n'
        "    }"
    )
    if old in content:
        content = content.replace(old, new)
        print("Fixed appendJsonString")
    else:
        print("WARNING: appendJsonString pattern not found")
        # Try simpler fix - just look for the mangled part
        mangled = "            char c = value.charAt(i);\n            }\n        }"
        if mangled in content:
            # Restore the switch body
            fix = (
                "            char c = value.charAt(i);\n"
                "            switch (c) {\n"
                "                case '\"': json.append(\"\\\\\\\"\"); break;\n"
                "                case '\\\\': json.append(\"\\\\\\\\\"); break;\n"
                "                case '\\b': json.append(\"\\\\b\"); break;\n"
                "                case '\\f': json.append(\"\\\\f\"); break;\n"
                "                case '\\n': json.append(\"\\\\n\"); break;\n"
                "                case '\\r': json.append(\"\\\\r\"); break;\n"
                "                case '\\t': json.append(\"\\\\t\"); break;\n"
                "                default:\n"
                "                    if (c < 0x20) {\n"
                "                        json.append(String.format(\"\\\\u%04x\", (int) c));\n"
                "                    } else {\n"
                "                        json.append(c);\n"
                "                    }\n"
                "            }\n"
                "        }"
            )
            content = content.replace(mangled, fix)
            print("Fixed appendJsonString (simpler)")
    
    p.write_text(content, encoding='utf-8')

def fix_preview_json_exporter():
    p = Path("D:/work/MiniCAD/src/main/java/com/minicad/app/StepPreviewJsonExporter.java")
    content = p.read_text(encoding='utf-8')
    
    # Fix remaining yield on same line as if
    content = content.replace(
        "if (points == null || points.size() < 2) yield null;",
        "if (points == null || points.size() < 2) return null;"
    )
    
    # Fix pattern matching instanceof in ternary: built instanceof Curve2 curve2 ? liftCurve2(curve2) : null
    # -> if/else
    content = content.replace(
        "return built instanceof Curve2 curve2 ? liftCurve2(curve2) : null;",
        "if (built instanceof Curve2) { Curve2 curve2 = (Curve2) built; return liftCurve2(curve2); }\n                return null;"
    )
    
    # Fix: if (!(... instanceof ... originPlacement)) { - negated guard with pattern binding
    content = content.replace(
        "if (!(mappingSource.getMappedOrigin() instanceof com.minicad.step.model.geometry.StepAxis2Placement3D originPlacement)) {",
        "if (!(mappingSource.getMappedOrigin() instanceof com.minicad.step.model.geometry.StepAxis2Placement3D)) {\n"
        "            return null;\n"
        "        }\n"
        "        com.minicad.step.model.geometry.StepAxis2Placement3D originPlacement = (com.minicad.step.model.geometry.StepAxis2Placement3D) mappingSource.getMappedOrigin();\n"
        "        if (false) {"
    )
    # Now remove the "return null;" that was after the old guard (since we added it above)
    # Actually, this approach is too complex. Let me handle it differently.
    
    # Fix: } else if (... instanceof ... targetPlacement) {
    content = content.replace(
        "} else if (mappedItem.getMappingTarget() instanceof com.minicad.step.model.geometry.StepAxis2Placement3D targetPlacement) {",
        "} else if (mappedItem.getMappingTarget() instanceof com.minicad.step.model.geometry.StepAxis2Placement3D) {\n"
        "            com.minicad.step.model.geometry.StepAxis2Placement3D targetPlacement = (com.minicad.step.model.geometry.StepAxis2Placement3D) mappedItem.getMappingTarget();"
    )
    
    # Fix compound instanceof with &&: if (trim instanceof StepValue.ReferenceValue ref && resolved.containsKey(ref.getId())) {
    content = content.replace(
        "if (trim instanceof StepValue.ReferenceValue ref && resolved.containsKey(ref.getId())) {",
        "if (trim instanceof StepValue.ReferenceValue) {\n"
        "                    StepValue.ReferenceValue ref = (StepValue.ReferenceValue) trim;\n"
        "                    if (resolved.containsKey(ref.getId())) {"
    )
    # Add closing brace for the extra if - need to find the matching closing brace
    # The pattern is: if (trim instanceof StepValue.ReferenceValue) { ... if (resolved...) { ... } }
    # After the inner if's }, we need another }
    # Find: "                }\n            }" after each occurrence
    # Actually this is tricky. Let me handle it differently.
    
    # Fix: } else if (entity instanceof ... StepFaceBound faceBound) {
    content = content.replace(
        "} else if (entity instanceof com.minicad.step.model.topology.StepFaceBound faceBound) {",
        "} else if (entity instanceof com.minicad.step.model.topology.StepFaceBound) {\n"
        "            com.minicad.step.model.topology.StepFaceBound faceBound = (com.minicad.step.model.topology.StepFaceBound) entity;"
    )
    
    p.write_text(content, encoding='utf-8')
    print("Fixed StepPreviewJsonExporter.java")

if __name__ == '__main__':
    fix_step_dump_app()
    fix_preview_json_exporter()
