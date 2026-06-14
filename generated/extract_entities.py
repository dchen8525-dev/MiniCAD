#!/usr/bin/env python3
"""
Extract all registered entities from MiscRegistry.java (handles multiline format).
"""

import re
import sys

def extract_entities(file_path):
    with open(file_path, 'r') as f:
        content = f.read()

    # Pattern for registry.put calls (handles multiline)
    # Matches: registry.put("ENTITY_NAME", ... or registry.put(\n    "ENTITY_NAME", ...
    pattern = r'registry\.put\(\s*["\']([^"\']+)["\']'

    matches = re.findall(pattern, content)

    entities = set()
    for match in matches:
        entity = match.upper().strip()
        if entity and not entity.startswith('//'):  # Skip comments
            entities.add(entity)

    return sorted(entities)

def main():
    file_path = sys.argv[1] if len(sys.argv) > 1 else 'src/main/java/com/minicad/step/semantic/MiscRegistry.java'
    output_file = sys.argv[2] if len(sys.argv) > 2 else '/tmp/registered_entities_final.txt'

    entities = extract_entities(file_path)

    with open(output_file, 'w') as f:
        for entity in entities:
            f.write(entity + '\n')

    print(f"Extracted {len(entities)} registered entities")
    print(f"Written to: {output_file}")

    # Show sample
    print("\nSample entities:")
    for i, entity in enumerate(entities[:20], 1):
        print(f"  {i}. {entity}")

if __name__ == '__main__':
    main()