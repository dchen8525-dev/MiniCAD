/** Text, number and box formatting helpers. No state, no DOM writes. */
export function escHtml(s) {
    return s.replaceAll('&', '&amp;').replaceAll('<', '&lt;').replaceAll('>', '&gt;').replaceAll('"', '&quot;');
}

export function setTextAndShow(el, text) {
    el.textContent = text;
    el.style.display = text ? '' : 'none';
}

export function summarizeUnsupportedFaces(unsupportedFaces = []) {
    const summary = {
        bySurfaceType: {},
        byReason: {}
    };
    if (!Array.isArray(unsupportedFaces)) {
        return summary;
    }
    for (const face of unsupportedFaces) {
        const surfaceType = face?.surfaceType || 'UNKNOWN';
        const reason = face?.reason || 'unknown';
        summary.bySurfaceType[surfaceType] = (summary.bySurfaceType[surfaceType] ?? 0) + 1;
        summary.byReason[reason] = (summary.byReason[reason] ?? 0) + 1;
    }
    return summary;
}

export function summarizeUnsupportedBooleans(unsupportedBooleans = []) {
    const summary = {
        byType: {},
        byReason: {}
    };
    if (!Array.isArray(unsupportedBooleans)) {
        return summary;
    }
    for (const item of unsupportedBooleans) {
        const type = item?.type || 'UNKNOWN';
        const reason = item?.reason || 'unknown';
        summary.byType[type] = (summary.byType[type] ?? 0) + 1;
        summary.byReason[reason] = (summary.byReason[reason] ?? 0) + 1;
    }
    return summary;
}

export function summarizeEntries(summaryMap = {}) {
    return Object.entries(summaryMap)
        .sort((left, right) => {
            if (right[1] !== left[1]) {
                return right[1] - left[1];
            }
            return String(left[0]).localeCompare(String(right[0]), 'zh-Hans-CN');
        })
        .map(([key, count]) => `${key}:${count}`)
        .join(' | ');
}

export function niceCeil(value) {
    const safe = Math.max(value, 1);
    const magnitude = 10 ** Math.floor(Math.log10(safe));
    const normalized = safe / magnitude;
    if (normalized <= 1) {
        return magnitude;
    }
    if (normalized <= 2) {
        return 2 * magnitude;
    }
    if (normalized <= 5) {
        return 5 * magnitude;
    }
    return 10 * magnitude;
}

export function formatBytes(bytes) {
    if (bytes >= 1024 * 1024) {
        return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
    }
    if (bytes >= 1024) {
        return `${(bytes / 1024).toFixed(1)} KB`;
    }
    return `${bytes} B`;
}

export function fileExtension(fileName = '') {
    const dot = fileName.lastIndexOf('.');
    return dot >= 0 ? fileName.slice(dot).toLowerCase() : '';
}

export function boxToLog(box) {
    if (!box || box.isEmpty()) {
        return { empty: true };
    }
    return {
        min: box.min.toArray(),
        max: box.max.toArray(),
        size: box.getSize(new THREE.Vector3()).toArray(),
        center: box.getCenter(new THREE.Vector3()).toArray()
    };
}

export function pointsBounds(points) {
    if (!Array.isArray(points) || points.length === 0) {
        return null;
    }
    const box = new THREE.Box3();
    for (const point of points) {
        if (Array.isArray(point) && point.length >= 3) {
            box.expandByPoint(new THREE.Vector3(point[0], point[1], point[2]));
        }
    }
    return box;
}

export function formatPoint(point) {
    return point.map((value) => Number(value).toFixed(3)).join(', ');
}

export function formatMetric(value) {
    const numeric = Number(value ?? 0);
    return numeric.toFixed(3);
}

export function formatColor(color) {
    if (!Array.isArray(color) || color.length !== 3) {
        return '未指定';
    }
    return `rgb(${color[0]}, ${color[1]}, ${color[2]})`;
}

export function formatLayers(layers) {
    return Array.isArray(layers) && layers.length > 0 ? layers.join(', ') : '未指定';
}

export function vectorFromArray(values) {
    return new THREE.Vector3(values[0], values[1], values[2]);
}

export function orthonormalY(axis, xDirection) {
    return axis.clone().cross(xDirection).normalize();
}

export function clamp(value, min, max) {
    return Math.max(min, Math.min(value, max));
}
