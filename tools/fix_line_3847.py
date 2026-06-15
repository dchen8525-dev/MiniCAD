#!/usr/bin/env python3
"""
Direct fix for line 3847 in StepPreviewJsonExporter.java
"""

import re

filepath = 'src/main/java/com/minicad/app/StepPreviewJsonExporter.java'

with open(filepath, 'r', encoding='utf-8') as f:
    lines = f.readlines()

# Find and fix line 3847
for i, line in enumerate(lines):
    if i == 3846:  # Line 3847 in file (0-indexed is 3846)
        if 'EdgeLoop outerLoop' in line:
            # Replace pattern matching
            lines[i] = '        if (!(bounds.get(0).loop() instanceof EdgeLoop) || ((EdgeLoop) bounds.get(0).loop()).edges().size() != 4) {\n'
            # Add variable declaration after return null
            if i+1 < len(lines) and 'return null' in lines[i+1]:
                # Insert variable declaration
                lines.insert(i+2, '        EdgeLoop outerLoop = (EdgeLoop) bounds.get(0).loop();\n')
                lines.insert(i+3, '\n')
            print(f"[OK] Fixed line 3847")
            break

with open(filepath, 'w', encoding='utf-8') as f:
    f.writelines(lines)

print("[OK] File saved")