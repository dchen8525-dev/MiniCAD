#!/usr/bin/env python3
"""
Replace record-style method calls with Java Bean getters.
Handles: location(), axis(), radius(), position(), majorRadius(), minorRadius(), etc.
"""
import re
from pathlib import Path
import sys

# Mapping of record-style methods to Java Bean getters
REPLACEMENTS = {
    'location': 'getLocation',
    'axis': 'getAxis',
    'radius': 'getRadius',
    'position': 'getPosition',
    'majorRadius': 'getMajorRadius',
    'minorRadius': 'getMinorRadius',
    'sweptCurve': 'getSweptCurve',
    'extrusionVector': 'getExtrusionVector',
    'axisOrigin': 'getAxisOrigin',
    'axisDirection': 'getAxisDirection',
    'directrix1': 'getDirectrix1',
    'directrix2': 'getDirectrix2',
    'xDirection': 'getXDirection',
    'yDirection': 'getYDirection',
    'semiAngle': 'getSemiAngle',
    'distance': 'getDistance',
    'focalLength': 'getFocalLength',
    'semiAxis': 'getSemiAxis',
    'basisSurface': 'getBasisSurface',
    'controlPoints': 'getControlPoints',
    'weights': 'getWeights',
    'knots': 'getKnots',
    'knotMultiplicities': 'getKnotMultiplicities',
    'degree': 'getDegree',
    'segments': 'getSegments',
    'points': 'getPoints',
    'normal': 'getNormal',
    'origin': 'getOrigin',
    'direction': 'getDirection',
    'refDirection': 'getRefDirection',
    'basisCurve': 'getBasisCurve',
    'trimParamStart': 'getTrimParamStart',
    'trimParamEnd': 'getTrimParamEnd',
    'semiAxis1': 'getSemiAxis1',
    'semiAxis2': 'getSemiAxis2',
    'semiAxisA': 'getSemiAxisA',
    'semiAxisB': 'getSemiAxisB',
    'uDegree': 'getUDegree',
    'vDegree': 'getVDegree',
    'uMultiplicities': 'getUMultiplicities',
    'vMultiplicities': 'getVMultiplicities',
    'uKnots': 'getUKnots',
    'vKnots': 'getVKnots',
    'weightsData': 'getWeightsData',
    'surface': 'getSurface',
    'curve2': 'getCurve2',
    'profile': 'getProfile',
    'projectionDirection': 'getProjectionDirection',
}

def replace_method_calls(content):
    """Replace record-style method calls with Java Bean getters."""
    result = content
    for record_method, bean_method in REPLACEMENTS.items():
        # Match pattern: .methodName() but not .getMethodName() or methodName() definition
        # Use word boundary to avoid partial matches
        pattern = r'\.' + re.escape(record_method) + r'\(\)'
        result = re.sub(pattern, '.' + bean_method + '()', result)
    return result

def process_file(filepath):
    """Process a single Java file."""
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    new_content = replace_method_calls(content)
    
    if new_content != content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)
        return True
    return False

def main():
    """Process Java files in semantic and app packages."""
    src_dir = Path('src/main/java/com/minicad')
    modified = 0
    
    # Process specific packages that likely have these calls
    packages = ['step/semantic', 'app', 'step/syntax']
    
    for package in packages:
        package_dir = src_dir / package
        if package_dir.exists():
            for java_file in package_dir.rglob('*.java'):
                if process_file(java_file):
                    modified += 1
                    print(f'Updated: {java_file.relative_to(src_dir.parent.parent.parent)}')
    
    print(f'\nTotal files modified: {modified}')

if __name__ == '__main__':
    main()
