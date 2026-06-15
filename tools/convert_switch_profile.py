#!/usr/bin/env python3
"""
Convert switch expression in StepProfileBuilder.java to if-else for Java 11.
"""

filepath = 'src/main/java/com/minicad/step/semantic/StepProfileBuilder.java'

with open(filepath, 'r', encoding='utf-8') as f:
    lines = f.readlines()

# Find the switch expression (lines 66-109)
start_idx = None
end_idx = None

for i, line in enumerate(lines):
    if 'return switch (profile.entityName())' in line:
        start_idx = i
        break

if start_idx:
    # Find the closing brace and semicolon
    for i in range(start_idx + 1, len(lines)):
        if 'default -> throw' in lines[i]:
            # Next line should be "};"
            end_idx = i + 1
            break

if start_idx and end_idx:
    # Extract the switch cases
    switch_lines = lines[start_idx:end_idx+1]

    # Build if-else replacement
    replacement = []
    replacement.append('        String entityName = profile.entityName();\n')

    first_case = True
    for line in switch_lines[1:-1]:  # Skip first (return switch) and last (};)
        # Parse case line
        if 'case "' in line:
            # Extract case values and return value
            match = line.strip().match(r'case "([^"]+)"[^>]*->\s*(.+);')
            if not match:
                # Try multi-case
                match = line.strip().match(r'case "([^"]+)",[^>]*->\s*(.+);')
            if not match:
                # Try simple case
                parts = line.strip().split(' -> ')
                if len(parts) == 2:
                    case_part = parts[0].replace('case ', '').replace('"', '').replace(',', ' || entityName.equals("')
                    result_part = parts[1].rstrip(';')

                    if first_case:
                        replacement.append(f'        if (entityName.equals("{case_part}")) {{\n')
                        first_case = False
                    else:
                        replacement.append(f'        }} else if (entityName.equals("{case_part}")) {{\n')

                    replacement.append(f'            return {result_part};\n')

    # Add default case
    replacement.append('        } else {\n')
    replacement.append('            throw new UnsupportedGeometryException(profile.entityName() + " extrusion is unsupported");\n')
    replacement.append('        }\n')

    # Replace lines
    new_lines = lines[:start_idx] + replacement + lines[end_idx+1:]

    with open(filepath, 'w', encoding='utf-8') as f:
        f.writelines(new_lines)

    print(f"[OK] Converted switch expression to if-else")
else:
    print("[ERROR] Could not find switch expression")