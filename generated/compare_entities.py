#!/usr/bin/env python3
"""
Compare schema entities with registered entities and output missing list.
"""

import sys

def main():
    schema_file = sys.argv[1] if len(sys.argv) > 1 else 'generated/ap242-entity-names.txt'
    registered_file = sys.argv[2] if len(sys.argv) > 2 else 'generated/registered_entities.txt'
    output_file = sys.argv[3] if len(sys.argv) > 3 else 'generated/final_missing_entities.txt'

    # Read schema entities
    with open(schema_file, 'r') as f:
        schema_entities = set(line.strip().upper() for line in f if line.strip())

    # Read registered entities
    with open(registered_file, 'r') as f:
        registered_entities = set(line.strip().upper() for line in f if line.strip())

    # Find missing
    missing_entities = sorted(schema_entities - registered_entities)

    # Write output
    with open(output_file, 'w') as f:
        for entity in missing_entities:
            f.write(entity + '\n')

    # Print stats
    print(f"Schema entities: {len(schema_entities)}")
    print(f"Registered entities: {len(registered_entities)}")
    print(f"Missing entities: {len(missing_entities)}")
    print(f"Coverage: {len(registered_entities) * 100 / len(schema_entities):.1f}%")
    print(f"\nOutput written to: {output_file}")

    # Show sample missing
    print("\nSample missing entities:")
    for i, entity in enumerate(missing_entities[:20], 1):
        print(f"  {i}. {entity}")

if __name__ == '__main__':
    main()