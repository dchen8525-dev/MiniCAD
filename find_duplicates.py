import re

# Read MiscRegistry.java
with open('src/main/java/com/minicad/step/semantic/MiscRegistry.java', 'r') as f:
    content = f.read()
    lines = content.split('\n')

# Find entities registered via helper methods in Phase 1
phase1_entities = {}

# registerShapeAspectAliases
match = re.search(r'registerShapeAspectAliases\([^)]+\(([^)]+)\)', content, re.DOTALL)
if match:
    aliases_block = match.group(1)
    entities = re.findall(r'"([^"]+)"', aliases_block)
    for entity in entities:
        phase1_entities[entity] = 'registerShapeAspectAliases'

# registerCharacterizedObjectAliases
matches = re.findall(r'registerCharacterizedObjectAliases\([^)]+\(([^)]+)\)', content, re.DOTALL)
for match_text in matches:
    entities = re.findall(r'"([^"]+)"', match_text)
    for entity in entities:
        phase1_entities[entity] = 'registerCharacterizedObjectAliases'

# registerProductDefinitionRelationshipAliases
matches = re.findall(r'registerProductDefinitionRelationshipAliases\([^)]+\(([^)]+)\)', content, re.DOTALL)
for match_text in matches:
    entities = re.findall(r'"([^"]+)"', match_text)
    for entity in entities:
        phase1_entities[entity] = 'registerProductDefinitionRelationshipAliases'

# Find duplicate registrations in Phase 2 (resolveGenericAssignment/resolveGenericProperty)
duplicates = []
for i, line in enumerate(lines, 1):
    if 'resolveGenericAssignment' in line or 'resolveGenericProperty' in line:
        # Look for entity name in previous lines
        for j in range(max(0, i-3), i):
            match = re.search(r'"([^"]+)"', lines[j])
            if match:
                entity = match.group(1)
                if entity in phase1_entities:
                    duplicates.append((i, entity, phase1_entities[entity]))

# Print duplicates
print(f"Found {len(duplicates)} duplicate registrations:")
for line_num, entity, phase1_method in duplicates[:20]:
    print(f"Line {line_num}: {entity} (Phase 1 via {phase1_method})")
