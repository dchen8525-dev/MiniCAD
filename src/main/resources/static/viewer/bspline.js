/** B-spline basis evaluation and surface point/normal sampling. */

import { clamp } from './format.js';
export function expandedKnots(knots, multiplicities) {
    const expanded = [];
    for (let index = 0; index < knots.length; index += 1) {
        for (let repeat = 0; repeat < multiplicities[index]; repeat += 1) {
            expanded.push(knots[index]);
        }
    }
    return expanded;
}

export function basisValue(i, degree, parameter, knots) {
    if (degree === 0) {
        const last = knots[knots.length - 1];
        if ((parameter >= knots[i] && parameter < knots[i + 1]) || (Math.abs(parameter - last) < 1.0e-9 && Math.abs(parameter - knots[i + 1]) < 1.0e-9)) {
            return 1;
        }
        return 0;
    }
    const leftDenominator = knots[i + degree] - knots[i];
    const rightDenominator = knots[i + degree + 1] - knots[i + 1];
    const left = Math.abs(leftDenominator) < 1.0e-9
        ? 0
        : (parameter - knots[i]) / leftDenominator * basisValue(i, degree - 1, parameter, knots);
    const right = Math.abs(rightDenominator) < 1.0e-9
        ? 0
        : (knots[i + degree + 1] - parameter) / rightDenominator * basisValue(i + 1, degree - 1, parameter, knots);
    return left + right;
}

export function findSpan(n, degree, parameter, knots) {
    if (parameter >= knots[n + 1]) {
        return n;
    }
    let low = degree;
    let high = n + 1;
    let mid = Math.floor((low + high) / 2);
    while (parameter < knots[mid] || parameter >= knots[mid + 1]) {
        if (parameter < knots[mid]) {
            high = mid;
        } else {
            low = mid;
        }
        mid = Math.floor((low + high) / 2);
    }
    return mid;
}

export function bsplineSurfacePoint(surface, u, v) {
    const controlPoints = surface.controlPoints;
    const uExpanded = expandedKnots(surface.uKnots, surface.uMultiplicities);
    const vExpanded = expandedKnots(surface.vKnots, surface.vMultiplicities);
    const clampedU = clamp(u, uExpanded[surface.uDegree], uExpanded[controlPoints.length]);
    const clampedV = clamp(v, vExpanded[surface.vDegree], vExpanded[controlPoints[0].length]);
    const uSpan = findSpan(controlPoints.length - 1, surface.uDegree, clampedU, uExpanded);
    const vSpan = findSpan(controlPoints[0].length - 1, surface.vDegree, clampedV, vExpanded);
    const point = new THREE.Vector3();
    for (let i = 0; i <= surface.uDegree; i += 1) {
        const ui = uSpan - surface.uDegree + i;
        const nu = basisValue(ui, surface.uDegree, clampedU, uExpanded);
        for (let j = 0; j <= surface.vDegree; j += 1) {
            const vj = vSpan - surface.vDegree + j;
            const nv = basisValue(vj, surface.vDegree, clampedV, vExpanded);
            const control = surface.controlPoints[ui][vj];
            point.x += control[0] * nu * nv;
            point.y += control[1] * nu * nv;
            point.z += control[2] * nu * nv;
        }
    }
    return point;
}

export function bsplineSurfaceNormal(surface, u, v) {
    const du = Math.max((surface.upperHeight - surface.lowerHeight) / 200.0, 1.0e-4);
    const dv = Math.max((surface.sweepAngle - surface.startAngle) / 200.0, 1.0e-4);
    const p = bsplineSurfacePoint(surface, u, v);
    const pu = bsplineSurfacePoint(surface, clamp(u + du, surface.lowerHeight, surface.upperHeight), v);
    const pv = bsplineSurfacePoint(surface, u, clamp(v + dv, surface.startAngle, surface.sweepAngle));
    const normal = pu.clone().sub(p).cross(pv.clone().sub(p));
    if (normal.lengthSq() < 1.0e-12) {
        return new THREE.Vector3(0, 0, 1);
    }
    return normal.normalize();
}
