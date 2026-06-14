#!/usr/bin/env python3
"""
Entity Classifier - Classifies missing STEP entities by complexity and domain.
"""

import re
import sys
from collections import defaultdict

def classify_complexity(entity_name):
    """Classify entity complexity based on name patterns."""
    name = entity_name.upper()

    # Simple patterns - measure, unit, function, simple attributes
    if re.search(r'(MEASURE_WITH_UNIT|UNIT|_FUNCTION|_ACTUAL|_HAPPENING|_STATUS|_RECORD|_ENTRY|_LOG)', name):
        return 'SIMPLE'

    # Medium patterns - relationships, assignments, properties, roles
    if re.search(r'(RELATIONSHIP|ASSIGNMENT|_PROPERTY|_ROLE|_REPRESENTATION|_DEFINITION|_CONTEXT|_FORMATION)', name):
        return 'MEDIUM'

    # Complex patterns - geometry, topology, tessellation
    if re.search(r'(BSPLINE|B_SPLINE|SURFACE_|CURVE_|VERTEX|EDGE|FACE|SHELL|LOOP|TOPOLOGY|TESSELLATED|TRIANGULATED|GEOMETRY)', name):
        return 'COMPLEX'

    # Assembly and product structure (medium complexity)
    if re.search(r'(ASSEMBLY|PRODUCT_DEFINITION|STRUCTURE|_SOLID|_SHAPE)', name):
        return 'MEDIUM'

    # Default simple
    return 'SIMPLE'

def classify_domain(entity_name):
    """Classify entity domain based on name patterns."""
    name = entity_name.upper()

    # Domain patterns in priority order
    patterns = {
        'geometry': r'(GEOMETRY|CURVE|SURFACE|POINT|LINE|PLANE|CIRCLE|ELLIPSE|BSPLINE|B_SPLINE|AXIS|DIRECTION|VECTOR|TRANSFORMATION|QUASI_UNIFORM|UNIFORM|RATIONAL|BEZIER|PARABOLA|HYPERBOLA|CLOTHOID)',
        'topology': r'(VERTEX|EDGE|FACE|SHELL|LOOP|BOUND|TOPOLOGY|BREP|ORIENTED_|CONNECTED_|ADVANCED_|OPEN_|CLOSED_)',
        'annotation': r'(ANNOTATION|PMI|DRAUGHTING|TEXT|SYMBOL|COLOUR|COLOR|STYLE|PRESENTATION|PLANAR_|RENDERING|LIGHT|CAMERA)',
        'tolerance': r'(TOLERANCE|DATUM|DIMENSION|MODIFIER|FLATNESS|ANGULAR|POSITION|RUNOUT|ROUNDNESS|CYLINDRICITY)',
        'product': r'(PRODUCT|ASSEMBLY|COMPONENT|SHAPE|REPRESENTATION|MAPPED_|ITEM_|SOLID|BREP_|MODEL)',
        'unit': r'(MEASURE_WITH_UNIT|_UNIT|_MEASURE$)',
        'action': r'(ACTION|PROCESS|WORKFLOW|STEP|METHOD|DIRECTIVE|REQUEST|SOLUTION)',
        'kinematic': r'(KINEMATIC|MECHANISM|PAIR|JOINT|LINK|MOTION|REVOLUTE|PRISMATIC|PLANAR|CYLINDRICAL|SPHERICAL|UNIVERSAL|SCREW|GEAR|RACK)',
        'fea': r'(FEA|ELEMENT|MESH|NODE|BOUNDARY_CONDITION|VOLUME_|SURFACE_|CURVE_ELEMENT|FINITE)',
        'manufacturing': r'(MANUFACTURING|FEATURE|MACHINING|OPERATION|TOOL|WORKPLAN|SETUP|SHEET|BEND|THREAD|GROOVE|POCKET|BORE|HOLE|CHAMFER|FILLET|SLOT)',
        'classification': r'(APPLIED_|ASSIGNMENT|CLASSIFICATION|GROUP|IDENTIFICATION|EXTERNAL_|NAME_|DESCRIPTION_|ID_ATTRIBUTE)',
        'validation': r'(VALIDATION|VERIFICATION|INSPECTION|A3M_|TEST|RESULT|COMPLIANCE|RISK|FAILURE|FAULT|ERROR)',
        'approval': r'(APPROVAL|CERTIFICATION|STATUS)',
        'document': r'(DOCUMENT|FILE|SPECIFICATION|VERSION|DIGITAL|EXTERNAL_FILE)',
        'organization': r'(ORGANIZATION|PERSON|ADDRESS|DEPARTMENT|CONTACT|ORGAN)',
        'date_time': r'(DATE|TIME|CALENDAR|SCHEDULE|DURATION|INTERVAL|EFFECTIVITY|YEAR|MONTH|WEEK)',
        'config_mgmt': r'(CONFIG|CHANGE|VERSION|AUDIT|BASELINE|TRACE|LIFECYCLE|MIGRATION|UPGRADE)',
        'security': r'(SECURITY|ACCESS|PERMISSION|AUTH|ENCRYPTION|LOCK|CREDENTIAL)',
        'resource': r'(RESOURCE|COST|JOB|CAPABILITY|TASK|TEAM|SCHEDULE_|CONTRACT|ORDER|INVENTORY|STOCK)',
        'log_audit': r'(LOG|RECORD|ENTRY|HISTORY|TRACKING|PERFORMANCE|METRIC|EVENT_|NOTIFICATION)',
        'backup_recovery': r'(BACKUP|ARCHIVE|RECOVERY|RESTORE|MIGRATION_|DECOMMISSION)',
        'analysis': r'(ANALYSIS|CALCULATION|STUDY|OPTIMIZATION|SIMULATION|IMPROVEMENT)',
        'profile': r'(PROFILE|PROPERTY_|GENERAL_PROPERTY)',
        'system': r'(SYSTEM|PLATFORM|ENVIRONMENT|ALERT|HEALTH)',
    }

    for domain, pattern in patterns.items():
        if re.search(pattern, name):
            return domain

    return 'misc'

def main():
    input_file = sys.argv[1] if len(sys.argv) > 1 else 'generated/ap242-missing-entities-final.txt'
    output_file = sys.argv[2] if len(sys.argv) > 2 else 'generated/ap242-entity-priority.csv'

    # Read missing entities
    with open(input_file, 'r') as f:
        entities = [line.strip() for line in f if line.strip()]

    # Classify
    results = []
    complexity_counts = defaultdict(int)
    domain_counts = defaultdict(int)
    domain_complexity = defaultdict(lambda: defaultdict(int))

    for entity in entities:
        complexity = classify_complexity(entity)
        domain = classify_domain(entity)

        results.append({
            'entity': entity,
            'complexity': complexity,
            'domain': domain,
            'priority': 0  # Will be calculated later
        })

        complexity_counts[complexity] += 1
        domain_counts[domain] += 1
        domain_complexity[domain][complexity] += 1

    # Calculate priority score (simple heuristic)
    # Higher score = implement first
    for r in results:
        score = 0

        # Complexity: simple = higher priority
        if r['complexity'] == 'SIMPLE':
            score += 100
        elif r['complexity'] == 'MEDIUM':
            score += 50
        else:
            score += 10

        # Domain: core domains = higher priority
        domain_priority = {
            'unit': 90,
            'geometry': 85,
            'topology': 80,
            'product': 75,
            'annotation': 70,
            'action': 65,
            'classification': 60,
            'validation': 55,
            'tolerance': 50,
            'kinematic': 45,
            'fea': 40,
            'manufacturing': 35,
            'organization': 30,
            'date_time': 25,
            'document': 20,
            'misc': 15,
        }
        score += domain_priority.get(r['domain'], 15)

        r['priority'] = score

    # Sort by priority (descending)
    results.sort(key=lambda x: x['priority'], reverse=True)

    # Write output
    with open(output_file, 'w') as f:
        f.write('entity,complexity,domain,priority\n')
        for r in results:
            f.write(f"{r['entity']},{r['complexity']},{r['domain']},{r['priority']}\n")

    # Print statistics
    print(f"\n=== Entity Classification Report ===")
    print(f"Total entities: {len(entities)}")
    print(f"\n=== Complexity Distribution ===")
    for c, count in sorted(complexity_counts.items(), key=lambda x: x[1], reverse=True):
        print(f"  {c}: {count} ({count*100/len(entities):.1f}%)")

    print(f"\n=== Domain Distribution (Top 10) ===")
    for d, count in sorted(domain_counts.items(), key=lambda x: x[1], reverse=True)[:10]:
        print(f"  {d}: {count} ({count*100/len(entities):.1f}%)")

    print(f"\n=== Domain × Complexity Matrix ===")
    for domain in sorted(domain_counts.keys(), key=lambda x: domain_counts[x], reverse=True)[:5]:
        print(f"  {domain}:")
        for complexity in ['SIMPLE', 'MEDIUM', 'COMPLEX']:
            count = domain_complexity[domain][complexity]
            if count > 0:
                print(f"    {complexity}: {count}")

    print(f"\n=== Top 20 Priority Entities ===")
    for i, r in enumerate(results[:20], 1):
        print(f"  {i}. {r['entity']} ({r['complexity']}, {r['domain']}, score={r['priority']})")

    print(f"\nOutput written to: {output_file}")

if __name__ == '__main__':
    main()