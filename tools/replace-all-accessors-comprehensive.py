#!/usr/bin/env python3
"""
Replace all record-style accessors with Java Bean getters.
Only replaces method calls, not definitions.
"""

import re
from pathlib import Path

def should_skip_line(line):
    """Skip lines that are method definitions or imports."""
    stripped = line.strip()
    if stripped.startswith('//') or stripped.startswith('*') or stripped.startswith('import'):
        return True
    # Skip method definitions like: public double x() { return x; }
    if re.match(r'^\s*public\s+\w+\s+\w+\(\)\s*\{', line):
        return True
    return False

def replace_record_accessors(content):
    """Replace record-style accessor calls with Java Bean getters."""
    lines = content.split('\n')
    result = []
    
    # Map of record-style to Java Bean
    accessors = {
        '.x()': '.getX()',
        '.y()': '.getY()',
        '.z()': '.getZ()',
        '.location()': '.getLocation()',
        '.axis()': '.getAxis()',
        '.refDirection()': '.getRefDirection()',
        '.position()': '.getPosition()',
        '.radius()': '.getRadius()',
        '.segments()': '.getSegments()',
        '.points()': '.getPoints()',
        '.normal()': '.getNormal()',
        '.direction()': '.getDirection()',
        '.origin()': '.getOrigin()',
        '.basisCurve()': '.getBasisCurve()',
        '.trimParamStart()': '.getTrimParamStart()',
        '.trimParamEnd()': '.getTrimParamEnd()',
        '.surface()': '.getSurface()',
        '.curve2()': '.getCurve2()',
        '.curve3d()': '.getCurve3d()',
        '.parametricCurves()': '.getParametricCurves()',
        '.uDegree()': '.getUDegree()',
        '.vDegree()': '.getVDegree()',
        '.controlPoints()': '.getControlPoints()',
        '.uMultiplicities()': '.getUMultiplicities()',
        '.vMultiplicities()': '.getVMultiplicities()',
        '.uKnots()': '.getUKnots()',
        '.vKnots()': '.getVKnots()',
        '.weightsData()': '.getWeightsData()',
        '.knotMultiplicities()': '.getKnotMultiplicities()',
        '.knots()': '.getKnots()',
        '.weights()': '.getWeights()',
        '.degree()': '.getDegree()',
        '.semiAxis1()': '.getSemiAxis1()',
        '.semiAxis2()': '.getSemiAxis2()',
        '.semiAxisA()': '.getSemiAxisA()',
        '.semiAxisB()': '.getSemiAxisB()',
        '.semiAxis()': '.getSemiAxis()',
        '.semiAngle()': '.getSemiAngle()',
        '.majorRadius()': '.getMajorRadius()',
        '.minorRadius()': '.getMinorRadius()',
        '.directrix1()': '.getDirectrix1()',
        '.directrix2()': '.getDirectrix2()',
        '.sweptCurve()': '.getSweptCurve()',
        '.sweptSurface()': '.getSweptSurface()',
        '.extrusionVector()': '.getExtrusionVector()',
        '.axisOrigin()': '.getAxisOrigin()',
        '.axisDirection()': '.getAxisDirection()',
        '.profile()': '.getProfile()',
        '.projectionDirection()': '.getProjectionDirection()',
        '.basisSurface()': '.getBasisSurface()',
        '.distance()': '.getDistance()',
        '.focalLength()': '.getFocalLength()',
        '.parameterScale()': '.getParameterScale()',
        '.minX()': '.getMinX()',
        '.minY()': '.getMinY()',
        '.minZ()': '.getMinZ()',
        '.maxX()': '.getMaxX()',
        '.maxY()': '.getMaxY()',
        '.maxZ()': '.getMaxZ()',
        '.edge()': '.getEdge()',
        '.orientation()': '.isOrientation()',
        '.sameSense()': '.isSameSense()',
        '.outer()': '.isOuter()',
        '.closed()': '.isClosed()',
        '.senseAgreement()': '.isSenseAgreement()',
        '.vertex()': '.getVertex()',
        '.curve()': '.getCurve()',
        '.start()': '.getStart()',
        '.end()': '.getEnd()',
        '.faces()': '.getFaces()',
        '.bounds()': '.getBounds()',
        '.loop()': '.getLoop()',
        '.outerShell()': '.getOuterShell()',
        '.voidShells()': '.getVoidShells()',
        '.issues()': '.getIssues()',
    }
    
    for line in lines:
        if should_skip_line(line):
            result.append(line)
            continue
        
        # Replace each accessor
        for record_style, bean_style in accessors.items():
            line = line.replace(record_style, bean_style)
        
        result.append(line)
    
    return '\n'.join(result)

def process_file(filepath):
    """Process a single Java file."""
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    new_content = replace_record_accessors(content)
    
    if new_content != content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)
        return True
    return False

def main():
    """Process all Java files in src/main/java."""
    java_files = list(Path('src/main/java').rglob('*.java'))
    
    print(f"Found {len(java_files)} Java files")
    
    modified = 0
    for filepath in java_files:
        if process_file(filepath):
            modified += 1
            print(f"Modified: {filepath}")
    
    print(f"\nModified {modified} files")

if __name__ == '__main__':
    main()
