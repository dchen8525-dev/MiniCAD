import * as THREE from 'three';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import { GLTFLoader } from 'three/addons/loaders/GLTFLoader.js';

function escHtml(s) {
    return s.replaceAll('&', '&amp;').replaceAll('<', '&lt;').replaceAll('>', '&gt;').replaceAll('"', '&quot;');
}

function setTextAndShow(el, text) {
    el.textContent = text;
    el.style.display = text ? '' : 'none';
}

const fileInput = document.querySelector('#file-input');
const statusText = document.querySelector('#status-text');
const validationDetails = document.querySelector('#validation-details');
const validationReport = document.querySelector('#validation-report');
const unsupportedFacesList = document.querySelector('#unsupported-faces');
const unsupportedBooleansList = document.querySelector('#unsupported-booleans');
const toggleUnsupportedViewButton = document.querySelector('#toggle-unsupported-view');
const selectionDetails = document.querySelector('#selection-details');
const assemblyTree = document.querySelector('#assembly-tree');
const pmiOverlay = document.querySelector('#pmi-overlay');
const isolateSelectionButton = document.querySelector('#isolate-selection');
const showAllButton = document.querySelector('#show-all');
const toggleEdgesButton = document.querySelector('#toggle-edges');
const togglePmiButton = document.querySelector('#toggle-pmi');
const statElements = new Map(
    Array.from(document.querySelectorAll('[data-stat]')).map((element) => [element.dataset.stat, element])
);

const sceneHost = document.querySelector('#scene');
const scene = new THREE.Scene();
scene.background = new THREE.Color(0xdfe7e8);

const camera = new THREE.PerspectiveCamera(55, 1, 0.01, 5000);
camera.position.set(3.5, 2.8, 3.5);

const renderer = new THREE.WebGLRenderer({ antialias: true });
const basePixelRatio = Math.min(window.devicePixelRatio, 4);
renderer.setPixelRatio(basePixelRatio);
renderer.outputColorSpace = THREE.SRGBColorSpace;
renderer.domElement.style.width = '100%';
renderer.domElement.style.height = '100%';
renderer.domElement.style.display = 'block';
sceneHost.appendChild(renderer.domElement);

const renderTarget = new THREE.WebGLRenderTarget(1, 1, {
    colorSpace: THREE.SRGBColorSpace
});
const postScene = new THREE.Scene();
const postCamera = new THREE.OrthographicCamera(-1, 1, 1, -1, 0, 1);
const postMaterial = new THREE.ShaderMaterial({
    uniforms: {
        tDiffuse: { value: renderTarget.texture },
        resolution: { value: new THREE.Vector2(1, 1) }
    },
    vertexShader: `
        varying vec2 vUv;
        void main() {
            vUv = uv;
            gl_Position = vec4(position.xy, 0.0, 1.0);
        }
    `,
    fragmentShader: `
        uniform sampler2D tDiffuse;
        uniform vec2 resolution;
        varying vec2 vUv;

        void main() {
            vec2 inverseResolution = 1.0 / resolution;
            vec3 rgbNW = texture2D(tDiffuse, vUv + vec2(-1.0, -1.0) * inverseResolution).rgb;
            vec3 rgbNE = texture2D(tDiffuse, vUv + vec2(1.0, -1.0) * inverseResolution).rgb;
            vec3 rgbSW = texture2D(tDiffuse, vUv + vec2(-1.0, 1.0) * inverseResolution).rgb;
            vec3 rgbSE = texture2D(tDiffuse, vUv + vec2(1.0, 1.0) * inverseResolution).rgb;
            vec3 rgbM = texture2D(tDiffuse, vUv).rgb;

            vec3 luma = vec3(0.299, 0.587, 0.114);
            float lumaNW = dot(rgbNW, luma);
            float lumaNE = dot(rgbNE, luma);
            float lumaSW = dot(rgbSW, luma);
            float lumaSE = dot(rgbSE, luma);
            float lumaM = dot(rgbM, luma);

            float lumaMin = min(lumaM, min(min(lumaNW, lumaNE), min(lumaSW, lumaSE)));
            float lumaMax = max(lumaM, max(max(lumaNW, lumaNE), max(lumaSW, lumaSE)));

            vec2 dir;
            dir.x = -((lumaNW + lumaNE) - (lumaSW + lumaSE));
            dir.y = ((lumaNW + lumaSW) - (lumaNE + lumaSE));

            float dirReduce = max(
                (lumaNW + lumaNE + lumaSW + lumaSE) * (0.25 * (1.0 / 8.0)),
                1.0 / 128.0
            );
            float reciprocalDirMin = 1.0 / (min(abs(dir.x), abs(dir.y)) + dirReduce);
            dir = clamp(dir * reciprocalDirMin, vec2(-8.0), vec2(8.0)) * inverseResolution;

            vec3 rgbA = 0.5 * (
                texture2D(tDiffuse, vUv + dir * (1.0 / 3.0 - 0.5)).rgb +
                texture2D(tDiffuse, vUv + dir * (2.0 / 3.0 - 0.5)).rgb
            );
            vec3 rgbB = rgbA * 0.5 + 0.25 * (
                texture2D(tDiffuse, vUv + dir * -0.5).rgb +
                texture2D(tDiffuse, vUv + dir * 0.5).rgb
            );

            float lumaB = dot(rgbB, luma);
            if (lumaB < lumaMin || lumaB > lumaMax) {
                gl_FragColor = vec4(rgbA, 1.0);
            } else {
                gl_FragColor = vec4(rgbB, 1.0);
            }
        }
    `
});
postScene.add(new THREE.Mesh(new THREE.PlaneGeometry(2, 2), postMaterial));

let unsupportedViewMode = 'details';
let currentUnsupportedFaces = [];
let currentUnsupportedBooleans = [];

const controls = new OrbitControls(camera, renderer.domElement);
const gltfLoader = new GLTFLoader();
controls.enableDamping = true;
controls.dampingFactor = 0.08;
controls.target.set(0, 0, 0);

if (toggleUnsupportedViewButton) {
    toggleUnsupportedViewButton.addEventListener('click', () => {
        unsupportedViewMode = unsupportedViewMode === 'summary' ? 'details' : 'summary';
        applyUnsupportedViewMode();
    });
    applyUnsupportedViewMode();
}

scene.add(new THREE.AmbientLight(0xffffff, 1.6));

const keyLight = new THREE.DirectionalLight(0xfff5e7, 2.4);
keyLight.position.set(5, 7, 6);
scene.add(keyLight);

const rimLight = new THREE.DirectionalLight(0xd7eef7, 1.3);
rimLight.position.set(-4, -2, -5);
scene.add(rimLight);

let grid = createGridHelper(10, 10);
scene.add(grid);

let axes = createAxesHelper(1.2);
scene.add(axes);

const modelRoot = new THREE.Group();
scene.add(modelRoot);
const pmiRoot = new THREE.Group();
scene.add(pmiRoot);

const raycaster = new THREE.Raycaster();
raycaster.params.Line.threshold = 0.14;
const pointer = new THREE.Vector2();
let interactiveObjects = [];
let selectedObject = null;
let selectedAssemblyButton = null;
let selectedAssemblyGroup = null;
let selectedUnsupportedButton = null;
const assemblyGroups = new Map();
const assemblyButtons = new Map();
const stepObjects = new Map();
let pmiLabels = [];
let pmiVisible = true;
let edgeLinesVisible = false;
let modelHasEdgeLines = false;
let lastRenderScale = -1;
let uploadedFile = null;
const viewerLogPrefix = '[MiniCAD Viewer]';

function logDebug(message, ...args) {
    console.debug(viewerLogPrefix, message, ...args);
}

function logInfo(message, ...args) {
    console.info(viewerLogPrefix, message, ...args);
}

function logWarn(message, ...args) {
    console.warn(viewerLogPrefix, message, ...args);
}

function logError(message, ...args) {
    console.error(viewerLogPrefix, message, ...args);
}

function logJson(label, payload) {
    try {
        console.info(`${viewerLogPrefix} ${label} ${JSON.stringify(payload)}`);
    } catch (error) {
        console.info(`${viewerLogPrefix} ${label}`, payload);
    }
}

function summarizeUnsupportedFaces(unsupportedFaces = []) {
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

function summarizeUnsupportedBooleans(unsupportedBooleans = []) {
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

function summarizeEntries(summaryMap = {}) {
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

function createGridHelper(size, divisions) {
    const helper = new THREE.GridHelper(size, divisions, 0x67767a, 0x9eb2b7);
    const materials = Array.isArray(helper.material) ? helper.material : [helper.material];
    for (const material of materials) {
        material.opacity = 0.55;
        material.transparent = true;
    }
    return helper;
}

function createAxesHelper(size) {
    return new THREE.AxesHelper(size);
}

function niceCeil(value) {
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

function updateReferenceGuides(bounds) {
    const min = toVector3(bounds.min);
    const max = toVector3(bounds.max);
    const size = max.clone().sub(min);
    const center = min.clone().add(max).multiplyScalar(0.5);
    const span = Math.max(size.x, size.y, 1);
    const gridSize = niceCeil(span * 1.4);
    const axesSize = Math.max(niceCeil(Math.max(size.x, size.y, size.z, 1) * 0.2), 1);

    scene.remove(grid);
    disposeObject(grid);
    grid = createGridHelper(gridSize, 10);
    grid.position.set(center.x, center.y, min.z);
    scene.add(grid);

    scene.remove(axes);
    disposeObject(axes);
    axes = createAxesHelper(axesSize);
    axes.position.set(min.x, min.y, min.z);
    scene.add(axes);

    logJson('updateReferenceGuides', {
        gridSize,
        axesSize,
        gridPosition: grid.position.toArray(),
        axesPosition: axes.position.toArray(),
        bounds: {
            min: bounds.min,
            max: bounds.max
        }
    });
}

function currentRenderScale() {
    const distance = camera.position.distanceTo(controls.target);
    if (distance <= 2.5) {
        return 3.4;
    }
    if (distance <= 4.5) {
        return 2.8;
    }
    if (distance <= 8.0) {
        return 2.1;
    }
    return 1.5;
}

function updateRenderResolution(force = false) {
    const width = sceneHost.clientWidth;
    const height = sceneHost.clientHeight;
    if (width === 0 || height === 0) {
        return;
    }
    const renderScale = currentRenderScale();
    if (!force && Math.abs(renderScale - lastRenderScale) < 0.05) {
        return;
    }
    lastRenderScale = renderScale;
    const scaledWidth = Math.max(1, Math.floor(width * renderer.getPixelRatio() * renderScale));
    const scaledHeight = Math.max(1, Math.floor(height * renderer.getPixelRatio() * renderScale));
    renderTarget.setSize(scaledWidth, scaledHeight);
    postMaterial.uniforms.resolution.value.set(scaledWidth, scaledHeight);
}

function resize() {
    const width = sceneHost.clientWidth;
    const height = sceneHost.clientHeight;
    if (width === 0 || height === 0) {
        return;
    }
    renderer.setSize(width, height, false);
    updateRenderResolution(true);
    camera.aspect = width / height;
    camera.updateProjectionMatrix();
}

window.addEventListener('resize', resize);
new ResizeObserver(() => resize()).observe(sceneHost);
resize();

function animate() {
    controls.update();
    updateRenderResolution();
    updatePmiOverlay();
    renderer.setRenderTarget(renderTarget);
    renderer.render(scene, camera);
    renderer.setRenderTarget(null);
    renderer.render(postScene, postCamera);
    requestAnimationFrame(animate);
}

animate();

function setStatus(text) {
    statusText.textContent = text;
}

function updateStats(stats = {}) {
    for (const [key, element] of statElements.entries()) {
        element.textContent = stats[key] ?? 0;
    }
}

function updateValidation(validation = {}) {
    const center = Array.isArray(validation.center) ? formatPoint(validation.center) : '0.000, 0.000, 0.000';
    const checks = Array.isArray(validation.report?.checks)
        ? validation.report.checks
        : (Array.isArray(validation.nativeChecks) ? validation.nativeChecks : []);
    const nativeChecks = checks.length > 0
        ? checks.map((check) => `${check.name}: ${check.matches ? 'OK' : `差异 ${formatMetric(check.delta)}`}`).join(' | ')
        : '无';
    const reportStatus = validation.report?.status ?? 'empty';
    validationDetails.innerHTML = [
        ['面', String(validation.renderedFaceCount ?? 0)],
        ['边', String(validation.renderedEdgeCount ?? 0)],
        ['面积', formatMetric(validation.approxSurfaceArea)],
        ['线长', formatMetric(validation.approxEdgeLength)],
        ['表示', String(validation.representationCount ?? 0)],
        ['实例', String(validation.instanceCount ?? 0)],
        ['中心', center],
        ['状态', reportStatus],
        ['校验', nativeChecks]
    ].map(([label, value]) => `<dt>${label}</dt><dd>${value}</dd>`).join('');
    updateValidationReport(validation.report ?? validation.nativeChecks);
}

function updateProduct(product = {}) {
    const card = document.getElementById('product-card');
    const nameEl = document.getElementById('product-name');
    const descEl = document.getElementById('product-desc');
    const idEl = document.getElementById('product-identifier');
    const schemaEl = document.getElementById('product-schema');
    const compEl = document.getElementById('product-components');

    const productName = product.productName;
    const productDesc = product.productDescription;
    const productId = product.productIdentifier;
    const fileName = product.fileName;
    const schemas = Array.isArray(product.schemas) ? product.schemas : [];
    const components = Array.isArray(product.components) ? product.components : [];

    if (!productName && !productDesc && !productId && !fileName && schemas.length === 0 && components.length === 0) {
        card.style.display = 'none';
        return;
    }

    card.style.display = '';
    setTextAndShow(nameEl, productName || fileName || '');
    setTextAndShow(descEl, productDesc || '');
    setTextAndShow(idEl, productId ? `ID: ${productId}` : '');
    setTextAndShow(schemaEl, schemas.length > 0 ? schemas.join(', ') : '');
    compEl.innerHTML = components.map((c) => {
        const label = c.name || c.identifier || 'Component';
        return `<li class="component-item"><strong>${escHtml(label)}</strong>${c.description ? `<br><span style="color:var(--muted);font-size:0.8rem">${escHtml(c.description)}</span>` : ''}</li>`;
    }).join('');
}

function updateUnits(units = {}) {
    const card = document.getElementById('units-card');
    const unitEl = document.getElementById('unit-value');
    const scaleEl = document.getElementById('unit-scale');
    const angleEl = document.getElementById('unit-angle');

    const lengthUnit = units.lengthUnit;
    const scaleToMeters = units.scaleToMeters;
    const angleUnit = units.angleUnit;

    if (!lengthUnit && !angleUnit) {
        card.style.display = 'none';
        return;
    }

    card.style.display = '';
    setTextAndShow(unitEl, lengthUnit || '未指定');
    setTextAndShow(scaleEl, scaleToMeters != null ? `1 单位 = ${scaleToMeters} 米` : '');
    setTextAndShow(angleEl, angleUnit ? `角度: ${angleUnit}` : '');
}

function updateValidationReport(report = {}) {
    const checks = Array.isArray(report.checks) ? report.checks : (Array.isArray(report) ? report : []);
    const okCount = Number(report.okCount ?? checks.filter((check) => check.matches).length);
    const warnCount = Number(report.warnCount ?? checks.filter((check) => !check.matches).length);
    if (checks.length === 0) {
        validationReport.innerHTML = '<li><strong>无 native validation</strong><span>当前 STEP 未导出可对比的原生校验项。</span></li>';
        return;
    }
    const summary = `<li><strong>汇总</strong><span>OK ${okCount} 项 / Warn ${warnCount} 项</span></li>`;
    validationReport.innerHTML = summary + checks.map((check) => {
        const cssClass = check.matches ? 'ok' : 'warn';
        const detail = `${check.measureType}: 期望 ${formatMetric(check.expected)} / 实际 ${formatMetric(check.actual)} / 差值 ${formatMetric(check.delta)}`;
        return `<li class="${cssClass}"><strong>${check.name}</strong><span>${detail}</span></li>`;
    }).join('');
}

function updateUnsupportedFaces(unsupportedFaces = []) {
    currentUnsupportedFaces = Array.isArray(unsupportedFaces) ? unsupportedFaces : [];
    if (!Array.isArray(unsupportedFaces) || unsupportedFaces.length === 0) {
        unsupportedFacesList.innerHTML = '<li><button type="button" disabled><strong>无</strong><span>当前预览没有被跳过的面。</span></button></li>';
        return;
    }
    unsupportedFacesList.innerHTML = '';
    const summary = summarizeUnsupportedFaces(unsupportedFaces);
    const summaryItem = document.createElement('li');
    summaryItem.className = 'summary';
    summaryItem.innerHTML = `<button type="button" disabled><strong>汇总</strong><span>${summarizeEntries(summary.bySurfaceType)}</span><span>${summarizeEntries(summary.byReason)}</span></button>`;
    unsupportedFacesList.appendChild(summaryItem);
    if (unsupportedViewMode === 'summary') {
        return;
    }
    for (const face of unsupportedFaces) {
        const item = document.createElement('li');
        const button = document.createElement('button');
        button.type = 'button';
        const name = face.name || `FACE #${face.id}`;
        const surfaceType = face.surfaceType || 'UNKNOWN';
        const reason = face.reason || '当前导出器已识别该面，但本轮预览仍将其跳过。';
        button.innerHTML = `<strong>#${face.id} ${name}</strong><span>${surfaceType}</span><span>${reason}</span>`;
        button.addEventListener('click', () => selectUnsupportedFace(face, button));
        item.appendChild(button);
        unsupportedFacesList.appendChild(item);
    }
}

function updateUnsupportedBooleans(unsupportedBooleans = []) {
    currentUnsupportedBooleans = Array.isArray(unsupportedBooleans) ? unsupportedBooleans : [];
    if (!Array.isArray(unsupportedBooleans) || unsupportedBooleans.length === 0) {
        unsupportedBooleansList.innerHTML = '<li><button type="button" disabled><strong>无</strong><span>当前预览没有被跳过的布尔结果。</span></button></li>';
        return;
    }
    unsupportedBooleansList.innerHTML = '';
    const summary = summarizeUnsupportedBooleans(unsupportedBooleans);
    const summaryItem = document.createElement('li');
    summaryItem.className = 'summary';
    summaryItem.innerHTML = `<button type="button" disabled><strong>汇总</strong><span>${summarizeEntries(summary.byType)}</span><span>${summarizeEntries(summary.byReason)}</span></button>`;
    unsupportedBooleansList.appendChild(summaryItem);
    if (unsupportedViewMode === 'summary') {
        return;
    }
    for (const itemData of unsupportedBooleans) {
        const item = document.createElement('li');
        const button = document.createElement('button');
        button.type = 'button';
        button.disabled = true;
        const name = itemData.name || `${itemData.type || 'BOOLEAN'} #${itemData.id}`;
        const type = itemData.type || 'UNKNOWN';
        const reason = itemData.reason || '当前导出器已识别该布尔结果，但本轮预览仍将其跳过。';
        button.innerHTML = `<strong>#${itemData.id} ${name}</strong><span>${type}</span><span>${reason}</span>`;
        item.appendChild(button);
        unsupportedBooleansList.appendChild(item);
    }
}

function applyUnsupportedViewMode() {
    if (toggleUnsupportedViewButton) {
        toggleUnsupportedViewButton.textContent = unsupportedViewMode === 'summary' ? '展开详情' : '只看汇总';
    }
    updateUnsupportedFaces(currentUnsupportedFaces);
    updateUnsupportedBooleans(currentUnsupportedBooleans);
}

function setSelection(entries) {
    selectionDetails.innerHTML = entries.map(([label, value]) => `<dt>${label}</dt><dd>${value}</dd>`).join('');
}

function resetSelection() {
    if (selectedObject) {
        selectedObject.userData.objectSelected = false;
        refreshRenderableStyle(selectedObject);
        selectedObject = null;
    }
    if (selectedAssemblyGroup) {
        applyAssemblyHighlight(selectedAssemblyGroup, false);
        selectedAssemblyGroup = null;
    }
    if (selectedAssemblyButton) {
        selectedAssemblyButton.classList.remove('active');
        selectedAssemblyButton = null;
    }
    if (selectedUnsupportedButton) {
        selectedUnsupportedButton.classList.remove('active');
        selectedUnsupportedButton = null;
    }
    setSelection([
        ['类型', '未选中'],
        ['说明', '点击右侧模型中的面或边查看详情。']
    ]);
    syncPmiTargetHighlight();
}

function selectUnsupportedFace(face, button) {
    if (selectedObject) {
        selectedObject.userData.objectSelected = false;
        refreshRenderableStyle(selectedObject);
        selectedObject = null;
    }
    if (selectedAssemblyGroup) {
        applyAssemblyHighlight(selectedAssemblyGroup, false);
        selectedAssemblyGroup = null;
    }
    if (selectedAssemblyButton) {
        selectedAssemblyButton.classList.remove('active');
        selectedAssemblyButton = null;
    }
    if (selectedUnsupportedButton) {
        selectedUnsupportedButton.classList.remove('active');
    }
    selectedUnsupportedButton = button;
    selectedUnsupportedButton.classList.add('active');
    setSelection([
        ['类型', '未支持面'],
        ['STEP', `#${face.id}`],
        ['名称', face.name || ''],
        ['曲面', face.surfaceType || 'UNKNOWN'],
        ['说明', face.reason || '当前导出器已识别该面，但本轮预览仍将其跳过。']
    ]);
    syncPmiTargetHighlight();
}

function clearModel() {
    logDebug('clearModel', {
        modelChildren: modelRoot.children.length,
        pmiChildren: pmiRoot.children.length,
        interactiveObjects: interactiveObjects.length,
        pmiLabels: pmiLabels.length
    });
    while (modelRoot.children.length > 0) {
        const child = modelRoot.children[0];
        modelRoot.remove(child);
        disposeObject(child);
    }
    while (pmiRoot.children.length > 0) {
        const child = pmiRoot.children[0];
        pmiRoot.remove(child);
        disposeObject(child);
    }
    pmiOverlay.innerHTML = '';
    pmiLabels = [];
    interactiveObjects = [];
    assemblyGroups.clear();
    assemblyButtons.clear();
    stepObjects.clear();
    resetSelection();
    updateValidation();
    updateUnsupportedFaces();
    renderAssemblyTree([]);
    edgeLinesVisible = false;
    modelHasEdgeLines = false;
    updateEdgeToggleButton();
    if (togglePmiButton) {
        togglePmiButton.textContent = '隐藏 PMI';
    }
}

function matrixFromRowMajor(elements) {
    return new THREE.Matrix4().set(
        elements[0], elements[1], elements[2], elements[3],
        elements[4], elements[5], elements[6], elements[7],
        elements[8], elements[9], elements[10], elements[11],
        elements[12], elements[13], elements[14], elements[15]
    );
}

function matrixToRows(matrixLike) {
    if (!Array.isArray(matrixLike) || matrixLike.length !== 16) {
        return matrixLike;
    }
    return [
        matrixLike.slice(0, 4),
        matrixLike.slice(4, 8),
        matrixLike.slice(8, 12),
        matrixLike.slice(12, 16)
    ];
}

function boxToLog(box) {
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

function pointsBounds(points) {
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

function disposeObject(object) {
    object.traverse((node) => {
        if (node.geometry) {
            node.geometry.dispose();
        }
        if (Array.isArray(node.material)) {
            node.material.forEach((material) => material.dispose());
        } else if (node.material) {
            node.material.dispose();
        }
    });
}

function toVector3(point) {
    return new THREE.Vector3(point[0], point[1], point[2]);
}

function renderAssemblyTree(instances) {
    assemblyTree.innerHTML = '';
    assemblyButtons.clear();
    if (!Array.isArray(instances) || instances.length === 0) {
        const item = document.createElement('li');
        item.innerHTML = '<button type="button" disabled><span class="assembly-item-label">无装配实例</span><span class="assembly-item-meta">当前预览未导出实例树。</span></button>';
        assemblyTree.appendChild(item);
        return;
    }

    const childrenByParent = new Map();
    for (const instance of instances) {
        const key = instance.parentId ?? '__root__';
        if (!childrenByParent.has(key)) {
            childrenByParent.set(key, []);
        }
        childrenByParent.get(key).push(instance);
    }

    const appendItems = (parentId, depth) => {
        const items = childrenByParent.get(parentId ?? '__root__') ?? [];
        for (const instance of items) {
            const item = document.createElement('li');
            item.style.paddingLeft = `${depth * 0.9}rem`;
            const button = document.createElement('button');
            button.type = 'button';
            button.dataset.instanceId = instance.id;
            assemblyButtons.set(instance.id, button);
            button.title = instance.description || instance.label || instance.id;
            button.innerHTML = `<span class="assembly-item-label">${instance.label || instance.id}</span>
                <span class="assembly-item-meta">pd #${instance.productDefinitionId}${instance.occurrenceId ? ` / occ #${instance.occurrenceId}` : ''}${Array.isArray(instance.representationIds) && instance.representationIds.length > 0 ? ` / rep ${instance.representationIds.map((id) => `#${id}`).join(', ')}` : ''}</span>`;
            button.addEventListener('click', () => focusAssemblyInstance(instance.id, button));
            item.appendChild(button);
            assemblyTree.appendChild(item);
            appendItems(instance.id, depth + 1);
        }
    };

    appendItems(null, 0);
}

function focusAssemblyInstance(instanceId, button = null) {
    const group = assemblyGroups.get(instanceId);
    if (!group) {
        logJson('focusAssemblyInstance:missing-group', { instanceId });
        return;
    }

    logJson('focusAssemblyInstance:start', {
        instanceId,
        triggeredByButton: Boolean(button),
        cameraPosition: camera.position.toArray(),
        controlsTarget: controls.target.toArray(),
        selectedObjectStepId: selectedObject?.userData?.stepId ?? null
    });

    if (selectedObject) {
        selectedObject.userData.objectSelected = false;
        refreshRenderableStyle(selectedObject);
        selectedObject = null;
    }
    activateAssemblyInstance(group, button ?? assemblyButtons.get(instanceId) ?? null);

    const box = new THREE.Box3().setFromObject(group);
    if (box.isEmpty()) {
        return;
    }
    const center = box.getCenter(new THREE.Vector3());
    const size = box.getSize(new THREE.Vector3());
    const radius = Math.max(size.length() * 0.7, 1);

    controls.target.copy(center);
    camera.position.copy(center.clone().add(new THREE.Vector3(radius, radius * 0.75, radius)));
    camera.near = Math.max(radius / 200, 0.01);
    camera.far = Math.max(radius * 40, 100);
    camera.updateProjectionMatrix();
    controls.update();

    logJson('focusAssemblyInstance:done', {
        instanceId,
        triggeredByButton: Boolean(button),
        bounds: boxToLog(box),
        center: center.toArray(),
        radius,
        cameraPosition: camera.position.toArray(),
        controlsTarget: controls.target.toArray()
    });

    setSelection([
        ['类型', '装配实例'],
        ['实例', group.userData.instanceLabel || instanceId],
        ['描述', group.userData.instanceDescription || ''],
        ['层级', String(group.userData.instanceDepth ?? 0)],
        ['表示', String(group.userData.representationCount ?? 0)],
        ['说明', '已定位并高亮该实例。']
    ]);
}

function activateAssemblyInstance(group, button = null) {
    if (!group) {
        return;
    }
    if (selectedAssemblyGroup && selectedAssemblyGroup !== group) {
        applyAssemblyHighlight(selectedAssemblyGroup, false);
    }
    selectedAssemblyGroup = group;
    applyAssemblyHighlight(group, true);

    if (selectedAssemblyButton) {
        selectedAssemblyButton.classList.remove('active');
    }
    selectedAssemblyButton = button ?? assemblyButtons.get(group.userData.instanceId) ?? null;
    if (selectedAssemblyButton) {
        selectedAssemblyButton.classList.add('active');
    }
}

function formatPoint(point) {
    return point.map((value) => Number(value).toFixed(3)).join(', ');
}

function formatMetric(value) {
    const numeric = Number(value ?? 0);
    return numeric.toFixed(3);
}

function formatColor(color) {
    if (!Array.isArray(color) || color.length !== 3) {
        return '未指定';
    }
    return `rgb(${color[0]}, ${color[1]}, ${color[2]})`;
}

function formatLayers(layers) {
    return Array.isArray(layers) && layers.length > 0 ? layers.join(', ') : '未指定';
}

function vectorFromArray(values) {
    return new THREE.Vector3(values[0], values[1], values[2]);
}

function orthonormalY(axis, xDirection) {
    return axis.clone().cross(xDirection).normalize();
}

function expandedKnots(knots, multiplicities) {
    const expanded = [];
    for (let index = 0; index < knots.length; index += 1) {
        for (let repeat = 0; repeat < multiplicities[index]; repeat += 1) {
            expanded.push(knots[index]);
        }
    }
    return expanded;
}

function clamp(value, min, max) {
    return Math.max(min, Math.min(value, max));
}

function basisValue(i, degree, parameter, knots) {
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

function findSpan(n, degree, parameter, knots) {
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

function bsplineSurfacePoint(surface, u, v) {
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

function bsplineSurfaceNormal(surface, u, v) {
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

function rebuildCurveEdgeGeometry(node) {
    const curve = node.userData?.curve;
    if (!curve || !node.geometry) {
        return;
    }
    const axis = vectorFromArray(curve.axis).normalize();
    const xDirection = vectorFromArray(curve.xDirection).normalize();
    const yDirection = orthonormalY(axis, xDirection);
    const center = vectorFromArray(curve.center);
    const sweep = Math.abs(curve.sweepAngle ?? 0);
    let segments = 0;
    const positions = [];

    if (curve.type === 'circle_arc' && Number.isFinite(curve.radius)) {
        segments = Math.max(128, Math.ceil(sweep / (Math.PI / 180.0)));
        for (let index = 0; index <= segments; index += 1) {
            const angle = curve.startAngle + curve.sweepAngle * index / segments;
            const point = center.clone()
                .addScaledVector(xDirection, Math.cos(angle) * curve.radius)
                .addScaledVector(yDirection, Math.sin(angle) * curve.radius);
            positions.push(point.x, point.y, point.z);
        }
    } else if (curve.type === 'ellipse_arc' && Number.isFinite(curve.semiAxis1) && Number.isFinite(curve.semiAxis2)) {
        segments = Math.max(160, Math.ceil(sweep / (Math.PI / 240.0)));
        for (let index = 0; index <= segments; index += 1) {
            const angle = curve.startAngle + curve.sweepAngle * index / segments;
            const point = center.clone()
                .addScaledVector(xDirection, Math.cos(angle) * curve.semiAxis1)
                .addScaledVector(yDirection, Math.sin(angle) * curve.semiAxis2);
            positions.push(point.x, point.y, point.z);
        }
    }

    if (positions.length >= 6) {
        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.Float32BufferAttribute(positions, 3));
        node.geometry.dispose();
        node.geometry = geometry;
    }
}

function rebuildParametricFaceGeometry(node) {
    const surface = node.userData?.surface;
    if (!surface || !node.isMesh) {
        return;
    }
    const faceLog = {
        stepId: node.userData?.stepId ?? null,
        instanceId: node.userData?.instanceId ?? null,
        surfaceType: node.userData?.selection?.find?.((entry) => entry[0] === '曲面')?.[1] ?? null,
        parametricType: surface.type,
        sameSense: node.userData?.sameSense ?? null,
        hasSurfaceLoops: Array.isArray(node.userData?.surfaceLoops),
        hasSurfaceUvLoops: Array.isArray(node.userData?.surfaceUvLoops),
        originalVertexCount: node.geometry?.getAttribute?.('position')?.count ?? 0
    };

    function evaluateBasisPoint(basis, u, v) {
        const type = basis.type;
        const center = basis.center ? vectorFromArray(basis.center) : new THREE.Vector3(0, 0, 0);
        const axis = basis.axis ? vectorFromArray(basis.axis).normalize() : new THREE.Vector3(0, 0, 1);
        const xDir = basis.xDirection ? vectorFromArray(basis.xDirection).normalize() : new THREE.Vector3(1, 0, 0);
        const yDir = axis.clone().cross(xDir).normalize();
        if (type === 'plane_face' || type === 'PLANE') {
            return [center.x + xDir.x * u + yDir.x * v, center.y + xDir.y * u + yDir.y * v, center.z + xDir.z * u + yDir.z * v];
        }
        if (type === 'cylindrical_strip' || type === 'CYLINDRICAL_SURFACE') {
            const r = basis.radius || 1;
            return [center.x + xDir.x * r * Math.cos(u * Math.PI * 2) + yDir.x * r * Math.sin(u * Math.PI * 2) + axis.x * v,
                     center.y + xDir.y * r * Math.cos(u * Math.PI * 2) + yDir.y * r * Math.sin(u * Math.PI * 2) + axis.y * v,
                     center.z + xDir.z * r * Math.cos(u * Math.PI * 2) + yDir.z * r * Math.sin(u * Math.PI * 2) + axis.z * v];
        }
        if (type === 'conical_strip' || type === 'CONICAL_SURFACE') {
            const r = (basis.radius || 1) + v * Math.tan(basis.semiAngle || 0.5);
            return [center.x + xDir.x * r * Math.cos(u * Math.PI * 2) + yDir.x * r * Math.sin(u * Math.PI * 2) + axis.x * v,
                     center.y + xDir.y * r * Math.cos(u * Math.PI * 2) + yDir.y * r * Math.sin(u * Math.PI * 2) + axis.y * v,
                     center.z + xDir.z * r * Math.cos(u * Math.PI * 2) + yDir.z * r * Math.sin(u * Math.PI * 2) + axis.z * v];
        }
        if (type === 'spherical_surface' || type === 'SPHERICAL_SURFACE') {
            const r = basis.radius || 1;
            const theta = u * Math.PI * 2;
            const phi = v * Math.PI;
            return [center.x + r * Math.sin(phi) * Math.cos(theta) * xDir.x + r * Math.sin(phi) * Math.sin(theta) * yDir.x + r * Math.cos(phi) * axis.x,
                     center.y + r * Math.sin(phi) * Math.cos(theta) * xDir.y + r * Math.sin(phi) * Math.sin(theta) * yDir.y + r * Math.cos(phi) * axis.y,
                     center.z + r * Math.sin(phi) * Math.cos(theta) * xDir.z + r * Math.sin(phi) * Math.sin(theta) * yDir.z + r * Math.cos(phi) * axis.z];
        }
        if (type === 'toroidal_strip' || type === 'TOROIDAL_SURFACE') {
            const R = basis.radius || 1;
            const r = basis.minorRadius || 0.3;
            const theta = u * Math.PI * 2;
            const phi = v * Math.PI * 2;
            const ringR = R + r * Math.cos(phi);
            return [center.x + ringR * Math.cos(theta) * xDir.x + ringR * Math.sin(theta) * yDir.x + r * Math.sin(phi) * axis.x,
                     center.y + ringR * Math.cos(theta) * xDir.y + ringR * Math.sin(theta) * yDir.y + r * Math.sin(phi) * axis.y,
                     center.z + ringR * Math.cos(theta) * xDir.z + ringR * Math.sin(theta) * yDir.z + r * Math.sin(phi) * axis.z];
        }
        if (type === 'surface_of_revolution' || type === 'SURFACE_OF_REVOLUTION') {
            const sweepAngle = basis.sweepAngle ?? Math.PI * 2;
            const startAngle = basis.startAngle ?? 0;
            const angle = startAngle + sweepAngle * u;
            const profileR = (basis.radius || 1) * (1 - v) + ((basis.semiAxis1 || basis.radius) || 1) * v;
            return [center.x + profileR * Math.cos(angle) * xDir.x + profileR * Math.sin(angle) * yDir.x + axis.x * v,
                     center.y + profileR * Math.cos(angle) * xDir.y + profileR * Math.sin(angle) * yDir.y + axis.y * v,
                     center.z + profileR * Math.cos(angle) * xDir.z + profileR * Math.sin(angle) * yDir.z + axis.z * v];
        }
        if (type === 'surface_of_linear_extrusion' || type === 'SURFACE_OF_LINEAR_EXTRUSION') {
            return [center.x + xDir.x * u + yDir.x * v * (basis.upperHeight || 1),
                     center.y + xDir.y * u + yDir.y * v * (basis.upperHeight || 1),
                     center.z + xDir.z * u + yDir.z * v * (basis.upperHeight || 1)];
        }
        if (type === 'ruled_surface' || type === 'RULED_SURFACE') {
            const d1 = basis.directrix1;
            const d2 = basis.directrix2;
            if (!d1 || !d2 || d1.length === 0) return null;
            const ci = Math.min(Math.floor(u * (d1.length - 1)), d1.length - 1);
            const p1 = vectorFromArray(d1[ci]);
            const p2 = vectorFromArray(d2[Math.min(ci, d2.length - 1)]);
            return [p1.x + (p2.x - p1.x) * v, p1.y + (p2.y - p1.y) * v, p1.z + (p2.z - p1.z) * v];
        }
        return null;
    }

    function evaluateBasisNormal(basis, u, v) {
        const type = basis.type;
        const center = basis.center ? vectorFromArray(basis.center) : new THREE.Vector3(0, 0, 0);
        const axis = basis.axis ? vectorFromArray(basis.axis).normalize() : new THREE.Vector3(0, 0, 1);
        const xDir = basis.xDirection ? vectorFromArray(basis.xDirection).normalize() : new THREE.Vector3(1, 0, 0);
        const yDir = axis.clone().cross(xDir).normalize();
        if (type === 'plane_face' || type === 'PLANE') {
            const n = axis.clone().cross(xDir).normalize();
            return [n.x, n.y, n.z];
        }
        if (type === 'cylindrical_strip' || type === 'CYLINDRICAL_SURFACE') {
            const angle = u * Math.PI * 2;
            return [xDir.x * Math.cos(angle) + yDir.x * Math.sin(angle),
                     xDir.y * Math.cos(angle) + yDir.y * Math.sin(angle),
                     xDir.z * Math.cos(angle) + yDir.z * Math.sin(angle)];
        }
        if (type === 'conical_strip' || type === 'CONICAL_SURFACE') {
            const angle = u * Math.PI * 2;
            const semiAngle = basis.semiAngle || 0.5;
            const n = new THREE.Vector3(
                xDir.x * Math.cos(angle) + yDir.x * Math.sin(angle) - axis.x * Math.tan(semiAngle),
                xDir.y * Math.cos(angle) + yDir.y * Math.sin(angle) - axis.y * Math.tan(semiAngle),
                xDir.z * Math.cos(angle) + yDir.z * Math.sin(angle) - axis.z * Math.tan(semiAngle)
            ).normalize();
            return [n.x, n.y, n.z];
        }
        if (type === 'spherical_surface' || type === 'SPHERICAL_SURFACE') {
            const theta = u * Math.PI * 2;
            const phi = v * Math.PI;
            const n = new THREE.Vector3(
                Math.sin(phi) * Math.cos(theta) * xDir.x + Math.sin(phi) * Math.sin(theta) * yDir.x + Math.cos(phi) * axis.x,
                Math.sin(phi) * Math.cos(theta) * xDir.y + Math.sin(phi) * Math.sin(theta) * yDir.y + Math.cos(phi) * axis.y,
                Math.sin(phi) * Math.cos(theta) * xDir.z + Math.sin(phi) * Math.sin(theta) * yDir.z + Math.cos(phi) * axis.z
            ).normalize();
            return [n.x, n.y, n.z];
        }
        if (type === 'toroidal_strip' || type === 'TOROIDAL_SURFACE') {
            const theta = u * Math.PI * 2;
            const phi = v * Math.PI * 2;
            const n = new THREE.Vector3(
                Math.cos(phi) * Math.cos(theta) * xDir.x + Math.cos(phi) * Math.sin(theta) * yDir.x + Math.sin(phi) * axis.x,
                Math.cos(phi) * Math.cos(theta) * xDir.y + Math.cos(phi) * Math.sin(theta) * yDir.y + Math.sin(phi) * axis.y,
                Math.cos(phi) * Math.cos(theta) * xDir.z + Math.cos(phi) * Math.sin(theta) * yDir.z + Math.sin(phi) * axis.z
            ).normalize();
            return [n.x, n.y, n.z];
        }
        // For complex types (bspline, etc.), return null to fall back to mesh
        return null;
    }

    if (surface.type === 'bspline_surface' && Array.isArray(node.userData?.surfaceUvLoops) && node.userData.surfaceUvLoops.length > 0) {
        const outerLoop = node.userData.surfaceUvLoops.find((loop) => loop.outer);
        if (!outerLoop || !Array.isArray(outerLoop.points) || outerLoop.points.length < 3) {
            logJson('parametricFace:skip', { ...faceLog, reason: 'missing-bspline-outer-loop' });
            return;
        }
        const outerPoints = outerLoop.points.slice(0, -1).map((point) => new THREE.Vector2(point[0], point[1]));
        const shapePoints = THREE.ShapeUtils.isClockWise(outerPoints) ? outerPoints.slice().reverse() : outerPoints;
        const shape = new THREE.Shape(shapePoints);
        for (const loop of node.userData.surfaceUvLoops) {
            if (loop.outer || !Array.isArray(loop.points) || loop.points.length < 3) {
                continue;
            }
            const holePoints = loop.points.slice(0, -1).map((point) => new THREE.Vector2(point[0], point[1]));
            const normalizedHole = THREE.ShapeUtils.isClockWise(holePoints) ? holePoints : holePoints.slice().reverse();
            shape.holes.push(new THREE.Path(normalizedHole));
        }
        const geometry2d = new THREE.ShapeGeometry(shape, 48);
        const positions = geometry2d.attributes.position;
        const normals = new Float32Array(positions.count * 3);
        const sameSense = node.userData?.sameSense !== false;
        for (let index = 0; index < positions.count; index += 1) {
            const u = positions.getX(index);
            const v = positions.getY(index);
            const point = bsplineSurfacePoint(surface, u, v);
            const normal = bsplineSurfaceNormal(surface, u, v);
            if (!sameSense) {
                normal.multiplyScalar(-1);
            }
            positions.setXYZ(index, point.x, point.y, point.z);
            normals[index * 3] = normal.x;
            normals[index * 3 + 1] = normal.y;
            normals[index * 3 + 2] = normal.z;
        }
        geometry2d.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        node.geometry.dispose();
        node.geometry = geometry2d;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            uvLoopCount: node.userData.surfaceUvLoops.length,
            rebuiltVertexCount: positions.count,
            rebuiltIndexCount: geometry2d.index?.count ?? 0
        });
        return;
    }
    if (surface.type === 'plane_face' && Array.isArray(node.userData?.surfaceLoops) && node.userData.surfaceLoops.length > 0) {
        const axis = vectorFromArray(surface.axis).normalize();
        const xDirection = vectorFromArray(surface.xDirection).normalize();
        const yDirection = orthonormalY(axis, xDirection);
        const center = vectorFromArray(surface.center);
        const toPlanePoint = (point) => {
            const value = vectorFromArray(point).sub(center);
            return new THREE.Vector2(value.dot(xDirection), value.dot(yDirection));
        };
        const outerLoop = node.userData.surfaceLoops.find((loop) => loop.outer);
        if (!outerLoop || !Array.isArray(outerLoop.points) || outerLoop.points.length < 3) {
            logJson('parametricFace:skip', { ...faceLog, reason: 'missing-plane-outer-loop' });
            return;
        }
        const outerPoints = outerLoop.points.slice(0, -1).map(toPlanePoint);
        const shapePoints = THREE.ShapeUtils.isClockWise(outerPoints) ? outerPoints.slice().reverse() : outerPoints;
        const shape = new THREE.Shape(shapePoints);
        for (const loop of node.userData.surfaceLoops) {
            if (loop.outer || !Array.isArray(loop.points) || loop.points.length < 3) {
                continue;
            }
            const holePoints = loop.points.slice(0, -1).map(toPlanePoint);
            const normalizedHole = THREE.ShapeUtils.isClockWise(holePoints) ? holePoints : holePoints.slice().reverse();
            shape.holes.push(new THREE.Path(normalizedHole));
        }
        const geometry2d = new THREE.ShapeGeometry(shape);
        const positions = geometry2d.attributes.position;
        const normals = new Float32Array(positions.count * 3);
        const sameSense = node.userData?.sameSense !== false;
        const planeNormal = sameSense ? axis : axis.clone().multiplyScalar(-1);
        for (let index = 0; index < positions.count; index += 1) {
            const u = positions.getX(index);
            const v = positions.getY(index);
            const point = center.clone()
                .addScaledVector(xDirection, u)
                .addScaledVector(yDirection, v);
            positions.setXYZ(index, point.x, point.y, point.z);
            normals[index * 3] = planeNormal.x;
            normals[index * 3 + 1] = planeNormal.y;
            normals[index * 3 + 2] = planeNormal.z;
        }
        geometry2d.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        node.geometry.dispose();
        node.geometry = geometry2d;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            loopCount: node.userData.surfaceLoops.length,
            rebuiltVertexCount: positions.count,
            rebuiltIndexCount: geometry2d.index?.count ?? 0
        });
        return;
    }
    if (surface.type === 'spherical_surface' && Number.isFinite(surface.radius)) {
        const axis = vectorFromArray(surface.axis).normalize();
        const xDirection = vectorFromArray(surface.xDirection).normalize();
        const yDirection = orthonormalY(axis, xDirection);
        const center = vectorFromArray(surface.center);
        const uMin = surface.trimU1 ?? 0;
        const uMax = surface.trimU2 ?? Math.PI * 2;
        const vMin = surface.trimV1 ?? -Math.PI / 2;
        const vMax = surface.trimV2 ?? Math.PI / 2;
        const uSpan = uMax - uMin;
        const vSpan = vMax - vMin;
        const radialSegments = Math.max(96, Math.ceil(Math.abs(uSpan) / (Math.PI / 180.0)));
        const heightSegments = Math.max(48, Math.ceil(Math.abs(vSpan) / (Math.PI / 180.0)));
        const vertexCount = (radialSegments + 1) * (heightSegments + 1);
        const positions = new Float32Array(vertexCount * 3);
        const normals = new Float32Array(vertexCount * 3);
        const indices = [];
        const sameSense = node.userData?.sameSense !== false;

        for (let v = 0; v <= heightSegments; v += 1) {
            const lat = vMin + vSpan * v / heightSegments;
            const cosLat = Math.cos(lat);
            const sinLat = Math.sin(lat);
            for (let u = 0; u <= radialSegments; u += 1) {
                const lon = uMin + uSpan * u / radialSegments;
                const cosLon = Math.cos(lon);
                const sinLon = Math.sin(lon);
                const offset = (v * (radialSegments + 1) + u) * 3;
                const dir = xDirection.clone().multiplyScalar(cosLat * cosLon)
                    .addScaledVector(yDirection, cosLat * sinLon)
                    .addScaledVector(axis, sinLat);
                positions[offset] = center.x + dir.x * surface.radius;
                positions[offset + 1] = center.y + dir.y * surface.radius;
                positions[offset + 2] = center.z + dir.z * surface.radius;
                const n = sameSense ? dir : dir.multiplyScalar(-1);
                normals[offset] = n.x;
                normals[offset + 1] = n.y;
                normals[offset + 2] = n.z;
            }
        }

        for (let v = 0; v < heightSegments; v += 1) {
            for (let u = 0; u < radialSegments; u += 1) {
                const a = v * (radialSegments + 1) + u;
                const b = a + 1;
                const c = a + radialSegments + 1;
                const d = c + 1;
                if (sameSense) {
                    indices.push(a, c, d, a, d, b);
                } else {
                    indices.push(a, d, c, a, b, d);
                }
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        geometry.setIndex(indices);
        node.geometry.dispose();
        node.geometry = geometry;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            radialSegments,
            heightSegments,
            rebuiltVertexCount: vertexCount,
            rebuiltIndexCount: indices.length
        });
        return;
    }

    if (surface.type === 'surface_of_revolution') {
        const axis = vectorFromArray(surface.axis).normalize();
        const xDirection = vectorFromArray(surface.xDirection).normalize();
        const yDirection = orthonormalY(axis, xDirection);
        const center = vectorFromArray(surface.center);
        const profileRadius = surface.radius || 1.0;
        const sweep = surface.sweepAngle ?? Math.PI * 2;
        const heightSpan = (surface.upperHeight ?? 0) - (surface.lowerHeight ?? 0);
        const radialSegments = Math.max(192, Math.ceil(Math.abs(sweep) / (Math.PI / 360.0)));
        const heightSegments = Math.max(12, Math.min(96, Math.ceil(Math.abs(heightSpan) / Math.max(profileRadius * 0.08, 1.0))));
        const vertexCount = (radialSegments + 1) * (heightSegments + 1);
        const positions = new Float32Array(vertexCount * 3);
        const normals = new Float32Array(vertexCount * 3);
        const indices = [];
        const sameSense = node.userData?.sameSense !== false;

        for (let v = 0; v <= heightSegments; v += 1) {
            const height = surface.lowerHeight + heightSpan * v / heightSegments;
            for (let u = 0; u <= radialSegments; u += 1) {
                const angle = surface.startAngle + sweep * u / radialSegments;
                const radial = xDirection.clone().multiplyScalar(Math.cos(angle))
                    .addScaledVector(yDirection, Math.sin(angle));
                const offset = (v * (radialSegments + 1) + u) * 3;
                positions[offset] = center.x + radial.x * profileRadius + axis.x * height;
                positions[offset + 1] = center.y + radial.y * profileRadius + axis.y * height;
                positions[offset + 2] = center.z + radial.z * profileRadius + axis.z * height;
                const n = sameSense ? radial : radial.clone().multiplyScalar(-1);
                normals[offset] = n.x;
                normals[offset + 1] = n.y;
                normals[offset + 2] = n.z;
            }
        }

        for (let v = 0; v < heightSegments; v += 1) {
            for (let u = 0; u < radialSegments; u += 1) {
                const a = v * (radialSegments + 1) + u;
                const b = a + 1;
                const c = a + radialSegments + 1;
                const d = c + 1;
                if (sameSense) {
                    indices.push(a, c, d, a, d, b);
                } else {
                    indices.push(a, d, c, a, b, d);
                }
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        geometry.setIndex(indices);
        node.geometry.dispose();
        node.geometry = geometry;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            radialSegments,
            heightSegments,
            rebuiltVertexCount: vertexCount,
            rebuiltIndexCount: indices.length
        });
        return;
    }

    if (surface.type === 'surface_of_linear_extrusion') {
        const axis = vectorFromArray(surface.axis).normalize();
        const xDirection = surface.xDirection ? vectorFromArray(surface.xDirection).normalize() : null;
        const heightSpan = (surface.upperHeight ?? 0) - (surface.lowerHeight ?? 0);
        const uMin = surface.trimU1 ?? 0;
        const uMax = surface.trimU2 ?? 1;
        const uSpan = uMax - uMin;
        const radialSegments = Math.max(96, Math.ceil(Math.abs(uSpan) * 48));
        const heightSegments = Math.max(8, Math.min(48, Math.ceil(Math.abs(heightSpan))));
        const vertexCount = (radialSegments + 1) * (heightSegments + 1);
        const positions = new Float32Array(vertexCount * 3);
        const normals = new Float32Array(vertexCount * 3);
        const indices = [];
        const sameSense = node.userData?.sameSense !== false;

        for (let v = 0; v <= heightSegments; v += 1) {
            const h = surface.lowerHeight + heightSpan * v / heightSegments;
            for (let u = 0; u <= radialSegments; u += 1) {
                const t = uMin + uSpan * u / radialSegments;
                const baseX = xDirection ? xDirection.x * t : t;
                const baseY = xDirection ? xDirection.y * t : 0;
                const baseZ = xDirection ? xDirection.z * t : 0;
                const offset = (v * (radialSegments + 1) + u) * 3;
                positions[offset] = (surface.center?.[0] ?? 0) + baseX + axis.x * h;
                positions[offset + 1] = (surface.center?.[1] ?? 0) + baseY + axis.y * h;
                positions[offset + 2] = (surface.center?.[2] ?? 0) + baseZ + axis.z * h;
                let tangent = xDirection || new THREE.Vector3(1, 0, 0);
                let n = tangent.clone().cross(axis).normalize();
                if (n.length() < 0.001) {
                    n = new THREE.Vector3(0, 1, 0).cross(axis).normalize();
                }
                if (!sameSense) {
                    n.multiplyScalar(-1);
                }
                normals[offset] = n.x;
                normals[offset + 1] = n.y;
                normals[offset + 2] = n.z;
            }
        }

        for (let v = 0; v < heightSegments; v += 1) {
            for (let u = 0; u < radialSegments; u += 1) {
                const a = v * (radialSegments + 1) + u;
                const b = a + 1;
                const c = a + radialSegments + 1;
                const d = c + 1;
                if (sameSense) {
                    indices.push(a, c, d, a, d, b);
                } else {
                    indices.push(a, d, c, a, b, d);
                }
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        geometry.setIndex(indices);
        node.geometry.dispose();
        node.geometry = geometry;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            radialSegments,
            heightSegments,
            rebuiltVertexCount: vertexCount,
            rebuiltIndexCount: indices.length
        });
        return;
    }

    if (surface.type === 'rational_bspline_surface' && Array.isArray(surface.controlPoints) && surface.controlPoints.length > 0) {
        const uMin = surface.trimU1 ?? surface.uStart ?? 0;
        const uMax = surface.trimU2 ?? surface.uEnd ?? 1;
        const vMin = surface.trimV1 ?? surface.vStart ?? 0;
        const vMax = surface.trimV2 ?? surface.vEnd ?? 1;
        const uSegments = Math.min(96, Math.max(24, surface.controlPoints.length * 4));
        const vSegments = Math.min(96, Math.max(24, (surface.controlPoints[0]?.length ?? 1) * 4));
        const vertexCount = (uSegments + 1) * (vSegments + 1);
        const positions = new Float32Array(vertexCount * 3);
        const normals = new Float32Array(vertexCount * 3);
        const indices = [];
        const sameSense = node.userData?.sameSense !== false;

        for (let v = 0; v <= vSegments; v += 1) {
            const sv = vMin + (vMax - vMin) * v / vSegments;
            for (let u = 0; u <= uSegments; u += 1) {
                const su = uMin + (uMax - uMin) * u / uSegments;
                const offset = (v * (uSegments + 1) + u) * 3;
                const point = bsplineSurfacePoint(surface, su, sv);
                positions[offset] = point.x;
                positions[offset + 1] = point.y;
                positions[offset + 2] = point.z;
                const normal = bsplineSurfaceNormal(surface, su, sv);
                const n = sameSense ? normal : normal.clone().multiplyScalar(-1);
                normals[offset] = n.x;
                normals[offset + 1] = n.y;
                normals[offset + 2] = n.z;
            }
        }

        for (let v = 0; v < vSegments; v += 1) {
            for (let u = 0; u < uSegments; u += 1) {
                const a = v * (uSegments + 1) + u;
                const b = a + 1;
                const c = a + uSegments + 1;
                const d = c + 1;
                if (sameSense) {
                    indices.push(a, c, d, a, d, b);
                } else {
                    indices.push(a, d, c, a, b, d);
                }
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        geometry.setIndex(indices);
        node.geometry.dispose();
        node.geometry = geometry;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            uSegments,
            vSegments,
            rebuiltVertexCount: vertexCount,
            rebuiltIndexCount: indices.length
        });
        return;
    }

    if (surface.type === 'paraboloid_surface') {
        const axis = vectorFromArray(surface.axis).normalize();
        const xDirection = vectorFromArray(surface.xDirection).normalize();
        const yDirection = orthonormalY(axis, xDirection);
        const center = vectorFromArray(surface.center);
        const focalLength = surface.radius;
        const uMin = surface.trimU1 ?? 0;
        const uMax = surface.trimU2 ?? Math.PI * 2;
        const vMin = surface.trimV1 ?? 0;
        const vMax = surface.trimV2 ?? 1.0;
        const uSpan = uMax - uMin;
        const vSpan = vMax - vMin;
        const radialSegments = Math.max(96, Math.ceil(Math.abs(uSpan) / (Math.PI / 180.0)));
        const heightSegments = Math.max(24, Math.ceil(Math.abs(vSpan) * 48));
        const vertexCount = (radialSegments + 1) * (heightSegments + 1);
        const positions = new Float32Array(vertexCount * 3);
        const normals = new Float32Array(vertexCount * 3);
        const indices = [];
        const sameSense = node.userData?.sameSense !== false;

        for (let v = 0; v <= heightSegments; v += 1) {
            const vz = vMin + vSpan * v / heightSegments;
            const r = Math.sqrt(4.0 * focalLength * Math.max(0.0, vz));
            for (let u = 0; u <= radialSegments; u += 1) {
                const angle = uMin + uSpan * u / radialSegments;
                const offset = (v * (radialSegments + 1) + u) * 3;
                const lx = r * Math.cos(angle);
                const ly = r * Math.sin(angle);
                const lz = vz;
                positions[offset] = center.x + xDirection.x * lx + yDirection.x * ly + axis.x * lz;
                positions[offset + 1] = center.y + xDirection.y * lx + yDirection.y * ly + axis.y * lz;
                positions[offset + 2] = center.z + xDirection.z * lx + yDirection.z * ly + axis.z * lz;
                // Gradient of F(x,y,z) = x^2 + y^2 - 4*f*z: (2x, 2y, -4f) in local
                const nx = 2.0 * lx;
                const ny = 2.0 * ly;
                const nz = -4.0 * focalLength;
                const localNormal = new THREE.Vector3(nx, ny, nz).normalize();
                const n = new THREE.Vector3(
                    xDirection.x * localNormal.x + yDirection.x * localNormal.y + axis.x * localNormal.z,
                    xDirection.y * localNormal.x + yDirection.y * localNormal.y + axis.y * localNormal.z,
                    xDirection.z * localNormal.x + yDirection.z * localNormal.y + axis.z * localNormal.z
                ).normalize();
                const finalN = sameSense ? n : n.multiplyScalar(-1);
                normals[offset] = finalN.x;
                normals[offset + 1] = finalN.y;
                normals[offset + 2] = finalN.z;
            }
        }

        for (let v = 0; v < heightSegments; v += 1) {
            for (let u = 0; u < radialSegments; u += 1) {
                const a = v * (radialSegments + 1) + u;
                const b = a + 1;
                const c = a + radialSegments + 1;
                const d = c + 1;
                if (sameSense) {
                    indices.push(a, c, d, a, d, b);
                } else {
                    indices.push(a, d, c, a, b, d);
                }
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        geometry.setIndex(indices);
        node.geometry.dispose();
        node.geometry = geometry;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            radialSegments,
            heightSegments,
            rebuiltVertexCount: vertexCount,
            rebuiltIndexCount: indices.length
        });
        return;
    }

    if (surface.type === 'hyperboloid_surface') {
        const axis = vectorFromArray(surface.axis).normalize();
        const xDirection = vectorFromArray(surface.xDirection).normalize();
        const yDirection = orthonormalY(axis, xDirection);
        const center = vectorFromArray(surface.center);
        const waistRadius = surface.radius;
        const semiAxis = surface.semiAngle;
        const uMin = surface.trimU1 ?? 0;
        const uMax = surface.trimU2 ?? Math.PI * 2;
        const vMin = surface.trimV1 ?? -1.0;
        const vMax = surface.trimV2 ?? 1.0;
        const uSpan = uMax - uMin;
        const vSpan = vMax - vMin;
        const radialSegments = Math.max(96, Math.ceil(Math.abs(uSpan) / (Math.PI / 180.0)));
        const heightSegments = Math.max(24, Math.ceil(Math.abs(vSpan) * 48));
        const vertexCount = (radialSegments + 1) * (heightSegments + 1);
        const positions = new Float32Array(vertexCount * 3);
        const normals = new Float32Array(vertexCount * 3);
        const indices = [];
        const sameSense = node.userData?.sameSense !== false;

        for (let v = 0; v <= heightSegments; v += 1) {
            const z = vMin + vSpan * v / heightSegments;
            const factor = Math.sqrt(1.0 + (z * z) / (semiAxis * semiAxis));
            const r = waistRadius * factor;
            for (let u = 0; u <= radialSegments; u += 1) {
                const angle = uMin + uSpan * u / radialSegments;
                const offset = (v * (radialSegments + 1) + u) * 3;
                const lx = r * Math.cos(angle);
                const ly = r * Math.sin(angle);
                positions[offset] = center.x + xDirection.x * lx + yDirection.x * ly + axis.x * z;
                positions[offset + 1] = center.y + xDirection.y * lx + yDirection.y * ly + axis.y * z;
                positions[offset + 2] = center.z + xDirection.z * lx + yDirection.z * ly + axis.z * z;
                // Gradient: (2x, 2y, -2*r0^2*z/b^2) in local
                const nx = 2.0 * lx;
                const ny = 2.0 * ly;
                const nz = -2.0 * waistRadius * waistRadius * z / (semiAxis * semiAxis);
                const localNormal = new THREE.Vector3(nx, ny, nz).normalize();
                const n = new THREE.Vector3(
                    xDirection.x * localNormal.x + yDirection.x * localNormal.y + axis.x * localNormal.z,
                    xDirection.y * localNormal.x + yDirection.y * localNormal.y + axis.y * localNormal.z,
                    xDirection.z * localNormal.x + yDirection.z * localNormal.y + axis.z * localNormal.z
                ).normalize();
                const finalN = sameSense ? n : n.multiplyScalar(-1);
                normals[offset] = finalN.x;
                normals[offset + 1] = finalN.y;
                normals[offset + 2] = finalN.z;
            }
        }

        for (let v = 0; v < heightSegments; v += 1) {
            for (let u = 0; u < radialSegments; u += 1) {
                const a = v * (radialSegments + 1) + u;
                const b = a + 1;
                const c = a + radialSegments + 1;
                const d = c + 1;
                if (sameSense) {
                    indices.push(a, c, d, a, d, b);
                } else {
                    indices.push(a, d, c, a, b, d);
                }
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        geometry.setIndex(indices);
        node.geometry.dispose();
        node.geometry = geometry;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            radialSegments,
            heightSegments,
            rebuiltVertexCount: vertexCount,
            rebuiltIndexCount: indices.length
        });
        return;
    }

    if (surface.type === 'surface_of_translation' || surface.type === 'surface_of_projection') {
        const dir = vectorFromArray(surface.axis).normalize();
        let xDirection = dir.clone().cross(new THREE.Vector3(1, 0, 0)).normalize();
        if (xDirection.length() < 0.001) {
            xDirection.copy(dir.clone().cross(new THREE.Vector3(0, 1, 0)).normalize());
        }
        const yDirection = dir.clone().cross(xDirection).normalize();
        const center = surface.center ? vectorFromArray(surface.center) : new THREE.Vector3(0, 0, 0);
        const uMin = surface.trimU1 ?? 0;
        const uMax = surface.trimU2 ?? 1.0;
        const vSpan = ((surface.upperHeight ?? 0) - (surface.lowerHeight ?? 0)) || 1.0;
        const vStart = surface.lowerHeight ?? 0;
        const uSpan = uMax - uMin;
        const curveSegments = Math.max(48, Math.ceil(Math.abs(uSpan) * 48));
        const heightSegments = Math.max(8, Math.min(48, Math.ceil(Math.abs(vSpan))));
        const vertexCount = (curveSegments + 1) * (heightSegments + 1);
        const positions = new Float32Array(vertexCount * 3);
        const normals = new Float32Array(vertexCount * 3);
        const indices = [];
        const sameSense = node.userData?.sameSense !== false;

        for (let v = 0; v <= heightSegments; v += 1) {
            const t = vStart + vSpan * v / heightSegments;
            for (let u = 0; u <= curveSegments; u += 1) {
                const s = uMin + uSpan * u / curveSegments;
                const offset = (v * (curveSegments + 1) + u) * 3;
                const lx = s;
                const ly = 0;
                const lz = 0;
                positions[offset] = center.x + xDirection.x * lx + yDirection.y * ly + dir.x * t;
                positions[offset + 1] = center.y + xDirection.y * lx + yDirection.y * ly + dir.y * t;
                positions[offset + 2] = center.z + xDirection.z * lx + yDirection.z * ly + dir.z * t;
                const tangent = xDirection;
                let n = tangent.clone().cross(dir).normalize();
                if (n.length() < 0.001) {
                    n = new THREE.Vector3(0, 1, 0).cross(dir).normalize();
                }
                if (!sameSense) {
                    n.multiplyScalar(-1);
                }
                normals[offset] = n.x;
                normals[offset + 1] = n.y;
                normals[offset + 2] = n.z;
            }
        }

        for (let v = 0; v < heightSegments; v += 1) {
            for (let u = 0; u < curveSegments; u += 1) {
                const a = v * (curveSegments + 1) + u;
                const b = a + 1;
                const c = a + curveSegments + 1;
                const d = c + 1;
                if (sameSense) {
                    indices.push(a, c, d, a, d, b);
                } else {
                    indices.push(a, d, c, a, b, d);
                }
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        geometry.setIndex(indices);
        node.geometry.dispose();
        node.geometry = geometry;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            parametricType: surface.type,
            curveSegments,
            heightSegments,
            rebuiltVertexCount: vertexCount,
            rebuiltIndexCount: indices.length
        });
        return;
    }

    if (surface.type === 'ruled_surface') {
        const d1 = surface.directrix1;
        const d2 = surface.directrix2;
        if (!d1 || !d2 || d1.length === 0 || d2.length === 0) {
            logJson('parametricFace:skip', { ...faceLog, reason: 'missing-directrix-curves' });
            return;
        }
        const curveSegments = Math.max(32, d1.length - 1);
        const ruleSegments = 8;
        const vertexCount = (curveSegments + 1) * (ruleSegments + 1);
        const positions = new Float32Array(vertexCount * 3);
        const normals = new Float32Array(vertexCount * 3);
        const indices = [];
        const sameSense = node.userData?.sameSense !== false;

        for (let i = 0; i <= curveSegments; i += 1) {
            const ci = Math.min(i, d1.length - 1);
            const p1 = vectorFromArray(d1[ci]);
            const p2 = vectorFromArray(d2[ci]);
            for (let j = 0; j <= ruleSegments; j += 1) {
                const t = j / ruleSegments;
                const offset = (i * (ruleSegments + 1) + j) * 3;
                positions[offset] = p1.x + (p2.x - p1.x) * t;
                positions[offset + 1] = p1.y + (p2.y - p1.y) * t;
                positions[offset + 2] = p1.z + (p2.z - p1.z) * t;
            }
        }

        for (let i = 0; i < curveSegments; i += 1) {
            const iNext = Math.min(i + 1, curveSegments);
            const p1 = vectorFromArray(d1[Math.min(i, d1.length - 1)]);
            const p1n = vectorFromArray(d1[Math.min(iNext, d1.length - 1)]);
            const p2 = vectorFromArray(d2[Math.min(i, d2.length - 1)]);
            const p2n = vectorFromArray(d2[Math.min(iNext, d2.length - 1)]);
            const tangent = p1n.clone().sub(p1).normalize();
            const ruling = p2.clone().sub(p1).normalize();
            let n = tangent.clone().cross(ruling).normalize();
            if (n.length() < 0.001) n = new THREE.Vector3(0, 0, 1);
            if (!sameSense) n.multiplyScalar(-1);
            for (let j = 0; j <= ruleSegments; j += 1) {
                const offset = (i * (ruleSegments + 1) + j) * 3;
                normals[offset] = n.x;
                normals[offset + 1] = n.y;
                normals[offset + 2] = n.z;
                const offset2 = (iNext * (ruleSegments + 1) + j) * 3;
                normals[offset2] = n.x;
                normals[offset2 + 1] = n.y;
                normals[offset2 + 2] = n.z;
            }
        }

        for (let i = 0; i < curveSegments; i += 1) {
            for (let j = 0; j < ruleSegments; j += 1) {
                const a = i * (ruleSegments + 1) + j;
                const b = a + 1;
                const c = a + ruleSegments + 1;
                const d = c + 1;
                if (sameSense) {
                    indices.push(a, c, d, a, d, b);
                } else {
                    indices.push(a, d, c, a, b, d);
                }
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        geometry.setIndex(indices);
        node.geometry.dispose();
        node.geometry = geometry;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            parametricType: 'ruled_surface',
            curveSegments,
            ruleSegments,
            rebuiltVertexCount: vertexCount,
            rebuiltIndexCount: indices.length
        });
        return;
    }

    if (surface.type === 'constant_radius_surface' || surface.type === 'offset_surface') {
        const basis = surface.basisSurface;
        if (!basis || !basis.type) {
            logJson('parametricFace:skip', { ...faceLog, reason: 'missing-basis-surface' });
            return;
        }
        const radius = surface.radius || 0;
        const isOffset = surface.type === 'offset_surface';
        const dist = isOffset ? (surface.offsetDistance ?? radius) : radius;

        const uSegments = 32;
        const vSegments = 32;
        const vertexCount = (uSegments + 1) * (vSegments + 1);
        const positions = new Float32Array(vertexCount * 3);
        const normals = new Float32Array(vertexCount * 3);
        const indices = [];
        const sameSense = node.userData?.sameSense !== false;

        for (let v = 0; v <= vSegments; v += 1) {
            for (let u = 0; u <= uSegments; u += 1) {
                const uu = u / uSegments;
                const vv = v / vSegments;
                const bp = evaluateBasisPoint(basis, uu, vv);
                const bn = evaluateBasisNormal(basis, uu, vv);
                if (!bp || !bn) {
                    const offset = (v * (uSegments + 1) + u) * 3;
                    positions[offset] = 0;
                    positions[offset + 1] = 0;
                    positions[offset + 2] = 0;
                    normals[offset] = 0;
                    normals[offset + 1] = 0;
                    normals[offset + 2] = 1;
                    continue;
                }
                const offset = (v * (uSegments + 1) + u) * 3;
                positions[offset] = bp[0] + bn[0] * dist;
                positions[offset + 1] = bp[1] + bn[1] * dist;
                positions[offset + 2] = bp[2] + bn[2] * dist;
                let n = new THREE.Vector3(bn[0], bn[1], bn[2]).normalize();
                if (!sameSense) n.multiplyScalar(-1);
                normals[offset] = n.x;
                normals[offset + 1] = n.y;
                normals[offset + 2] = n.z;
            }
        }

        for (let v = 0; v < vSegments; v += 1) {
            for (let u = 0; u < uSegments; u += 1) {
                const a = v * (uSegments + 1) + u;
                const b = a + 1;
                const c = a + uSegments + 1;
                const d = c + 1;
                if (sameSense) {
                    indices.push(a, c, d, a, d, b);
                } else {
                    indices.push(a, d, c, a, b, d);
                }
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        geometry.setIndex(indices);
        node.geometry.dispose();
        node.geometry = geometry;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            parametricType: surface.type,
            basisType: basis.type,
            offsetDistance: dist,
            rebuiltVertexCount: vertexCount,
            rebuiltIndexCount: indices.length
        });
        return;
    }

    if (!Number.isFinite(surface.radius) || surface.radius === 0.0) {
        logJson('parametricFace:skip', { ...faceLog, reason: 'missing-radius' });
        return;
    }

    const axis = vectorFromArray(surface.axis).normalize();
    const xDirection = vectorFromArray(surface.xDirection).normalize();
    const yDirection = orthonormalY(axis, xDirection);
    const center = vectorFromArray(surface.center);
    const sweep = surface.sweepAngle ?? 0;
    const heightSpan = (surface.upperHeight ?? 0) - (surface.lowerHeight ?? 0);
    const radialSegments = Math.max(192, Math.ceil(Math.abs(sweep) / (Math.PI / 360.0)));
    const heightSegments = Math.max(12, Math.min(96, Math.ceil(Math.abs(heightSpan) / Math.max(surface.radius * 0.08, 1.0))));
    const vertexCount = (radialSegments + 1) * (heightSegments + 1);
    const positions = new Float32Array(vertexCount * 3);
    const normals = new Float32Array(vertexCount * 3);
    const indices = [];
    const sameSense = node.userData?.sameSense !== false;

    for (let v = 0; v <= heightSegments; v += 1) {
        const height = surface.lowerHeight + heightSpan * v / heightSegments;
        for (let u = 0; u <= radialSegments; u += 1) {
            const angle = surface.startAngle + sweep * u / radialSegments;
            const radial = xDirection.clone().multiplyScalar(Math.cos(angle))
                .addScaledVector(yDirection, Math.sin(angle));
            let point;
            let normal;

            if (surface.type === 'cylindrical_strip') {
                point = center.clone()
                    .addScaledVector(radial, surface.radius)
                    .addScaledVector(axis, height);
                normal = sameSense ? radial : radial.clone().multiplyScalar(-1);
            } else if (surface.type === 'conical_strip' && Number.isFinite(surface.semiAngle)) {
                const radius = surface.radius + height * Math.tan(surface.semiAngle);
                point = center.clone()
                    .addScaledVector(radial, radius)
                    .addScaledVector(axis, height);
                const baseNormal = radial.clone().addScaledVector(axis, -Math.tan(surface.semiAngle)).normalize();
                normal = sameSense ? baseNormal : baseNormal.clone().multiplyScalar(-1);
            } else if (surface.type === 'toroidal_strip' && Number.isFinite(surface.minorRadius)) {
                const minorAngle = height;
                const ringRadius = surface.radius + surface.minorRadius * Math.cos(minorAngle);
                point = center.clone()
                    .addScaledVector(xDirection, Math.cos(angle) * ringRadius)
                    .addScaledVector(yDirection, Math.sin(angle) * ringRadius)
                    .addScaledVector(axis, surface.minorRadius * Math.sin(minorAngle));
                const baseNormal = xDirection.clone().multiplyScalar(Math.cos(angle) * Math.cos(minorAngle))
                    .addScaledVector(yDirection, Math.sin(angle) * Math.cos(minorAngle))
                    .addScaledVector(axis, Math.sin(minorAngle))
                    .normalize();
                normal = sameSense ? baseNormal : baseNormal.clone().multiplyScalar(-1);
            } else {
                logJson('parametricFace:skip', { ...faceLog, reason: 'unsupported-parametric-type' });
                return;
            }

            const offset = (v * (radialSegments + 1) + u) * 3;
            positions[offset] = point.x;
            positions[offset + 1] = point.y;
            positions[offset + 2] = point.z;
            normals[offset] = normal.x;
            normals[offset + 1] = normal.y;
            normals[offset + 2] = normal.z;
        }
    }

    for (let v = 0; v < heightSegments; v += 1) {
        for (let u = 0; u < radialSegments; u += 1) {
            const a = v * (radialSegments + 1) + u;
            const b = a + 1;
            const c = a + radialSegments + 1;
            const d = c + 1;
            if (sameSense) {
                indices.push(a, c, d, a, d, b);
            } else {
                indices.push(a, d, c, a, b, d);
            }
        }
    }

    const geometry = new THREE.BufferGeometry();
    geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
    geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
    geometry.setIndex(indices);
    node.geometry.dispose();
    node.geometry = geometry;
    logJson('parametricFace:rebuilt', {
        ...faceLog,
        radialSegments,
        heightSegments,
        rebuiltVertexCount: vertexCount,
        rebuiltIndexCount: indices.length
    });
}

function renderPmi(pmi) {
    if (!Array.isArray(pmi) || pmi.length === 0) {
        return;
    }
    for (const item of pmi) {
        if (Array.isArray(item.leader) && item.leader.length >= 2) {
            const geometry = new THREE.BufferGeometry();
            geometry.setAttribute('position', new THREE.Float32BufferAttribute(item.leader.flat(), 3));
            const material = new THREE.LineBasicMaterial({ color: 0x4a423d });
            pmiRoot.add(new THREE.Line(geometry, material));
        }
        const label = document.createElement('div');
        label.className = 'pmi-label';
        label.textContent = item.text || item.name || 'PMI';
        const targets = Array.isArray(item.targets) ? item.targets : [];
        if ((Array.isArray(item.targetIds) && item.targetIds.length > 0) || targets.length > 0) {
            label.style.cursor = 'pointer';
            const displayTargets = targets.length > 0 ? targets.map((target) => `#${target.id} ${target.type}`) : item.targetIds.map((id) => `#${id}`);
            label.title = `关联 STEP 项: ${displayTargets.join(', ')}`;
            label.addEventListener('click', () => selectPmiTargets(targets, item.targetIds));
        }
        pmiOverlay.appendChild(label);
        pmiLabels.push({
            element: label,
            anchor: new THREE.Vector3(item.position[0], item.position[1], item.position[2]),
            targetIds: Array.isArray(item.targetIds) ? item.targetIds : [],
            targets
        });
    }
    applyPmiVisibility();
}

function updatePmiOverlay() {
    if (pmiLabels.length === 0) {
        return;
    }
    const width = sceneHost.clientWidth;
    const height = sceneHost.clientHeight;
    for (const label of pmiLabels) {
        const screen = label.anchor.clone().project(camera);
        if (screen.z < -1 || screen.z > 1) {
            label.element.style.display = 'none';
            continue;
        }
        label.element.style.display = pmiVisible ? '' : 'none';
        label.element.style.left = `${(screen.x * 0.5 + 0.5) * width}px`;
        label.element.style.top = `${(-screen.y * 0.5 + 0.5) * height}px`;
    }
}

function applyPmiVisibility() {
    pmiOverlay.style.display = pmiVisible ? '' : 'none';
    pmiRoot.visible = pmiVisible;
    if (togglePmiButton) {
        togglePmiButton.textContent = pmiVisible ? '隐藏 PMI' : '显示 PMI';
    }
}

function updateEdgeToggleButton() {
    if (!toggleEdgesButton) {
        return;
    }
    toggleEdgesButton.disabled = !modelHasEdgeLines;
    toggleEdgesButton.textContent = edgeLinesVisible ? '隐藏边线' : '显示边线';
}

function isEdgeRenderable(node) {
    return node?.userData?.kind === 'edge'
        || node?.isLine;
}

function applyEdgeVisibility() {
    modelRoot.traverse((node) => {
        if (isEdgeRenderable(node)) {
            node.visible = edgeLinesVisible;
        }
    });
    updateEdgeToggleButton();
}

function refreshRenderableStyle(object) {
    const color = object.userData.objectSelected
        ? object.userData.selectedColor
        : object.userData.instanceHighlighted
            ? object.userData.instanceSelectedColor
            : object.userData.baseColor;
    object.material.color.setHex(color);
    if (object.isMesh) {
        object.material.opacity = 1.0;
        object.material.transparent = false;
        object.material.depthWrite = true;
        object.material.needsUpdate = true;
    }
}

function registerStepObject(stepId, object) {
    if (stepId == null) {
        return;
    }
    if (!stepObjects.has(stepId)) {
        stepObjects.set(stepId, []);
    }
    stepObjects.get(stepId).push(object);
}

function selectRenderable(object) {
    if (!object) {
        logJson('selectRenderable:null-object', {});
        return;
    }
    logJson('selectRenderable:start', {
        stepId: object.userData?.stepId ?? null,
        instanceId: object.userData?.instanceId ?? null,
        objectType: object.type,
        cameraPosition: camera.position.toArray(),
        controlsTarget: controls.target.toArray()
    });
    if (selectedObject && selectedObject !== object) {
        selectedObject.userData.objectSelected = false;
        refreshRenderableStyle(selectedObject);
    }
    selectedObject = object;
    if (selectedObject.userData.instanceId) {
        activateAssemblyInstance(
            assemblyGroups.get(selectedObject.userData.instanceId),
            assemblyButtons.get(selectedObject.userData.instanceId)
        );
    }
    selectedObject.userData.objectSelected = true;
    refreshRenderableStyle(selectedObject);
    setSelection(selectedObject.userData.selection);
    syncPmiTargetHighlight();
    logJson('selectRenderable:done', {
        stepId: selectedObject.userData?.stepId ?? null,
        instanceId: selectedObject.userData?.instanceId ?? null,
        objectType: selectedObject.type,
        cameraPosition: camera.position.toArray(),
        controlsTarget: controls.target.toArray()
    });
}

function setGroupVisibility(group, visible) {
    group.visible = visible;
}

function showOnlyInstance(instanceId) {
    if (assemblyGroups.size === 0) {
        return;
    }
    for (const [id, group] of assemblyGroups.entries()) {
        setGroupVisibility(group, id === instanceId);
    }
}

function showAllInstances() {
    if (assemblyGroups.size > 0) {
        for (const group of assemblyGroups.values()) {
            setGroupVisibility(group, true);
        }
    }
    for (const object of interactiveObjects) {
        object.visible = true;
    }
}

function syncPmiTargetHighlight() {
    const selectedIds = new Set();
    if (selectedObject?.userData?.stepId != null) {
        selectedIds.add(selectedObject.userData.stepId);
    }
    for (const label of pmiLabels) {
        if (selectedIds.size === 0 || label.targetIds.length === 0) {
            label.element.classList.remove('dimmed');
            continue;
        }
        const matches = label.targetIds.some((id) => selectedIds.has(id));
        label.element.classList.toggle('dimmed', !matches);
    }
}

function selectPmiTargets(targets, targetIds = []) {
    const effectiveTargets = Array.isArray(targets) ? targets : [];
    const effectiveIds = effectiveTargets.length > 0
        ? effectiveTargets.map((target) => target.id)
        : (Array.isArray(targetIds) ? targetIds : []);
    const instanceIds = effectiveTargets.flatMap((target) => Array.isArray(target.instanceIds) ? target.instanceIds : []);
    for (const instanceId of instanceIds) {
        if (assemblyGroups.has(instanceId)) {
            focusAssemblyInstance(instanceId, assemblyButtons.get(instanceId));
            break;
        }
    }
    if (effectiveIds.length === 0) {
        return;
    }
    for (const targetId of effectiveIds) {
        const matches = stepObjects.get(targetId);
        if (Array.isArray(matches) && matches.length > 0) {
            selectRenderable(matches[0]);
            return;
        }
    }
    const targetSummary = effectiveTargets.length > 0
        ? effectiveTargets.map((target) => `#${target.id} ${target.type}${target.name ? ` (${target.name})` : ''}`).join(', ')
        : effectiveIds.map((id) => `#${id}`).join(', ');
    setSelection([
        ['类型', 'PMI'],
        ['目标', targetSummary],
        ['实例', instanceIds.length > 0 ? instanceIds.join(', ') : '无实例映射'],
        ['说明', '已解析 semantic PMI 关联，但当前视图中没有可直接选中的对象。']
    ]);
}

function applyAssemblyHighlight(group, selected) {
    group.traverse((node) => {
        if (!node.material) {
            return;
        }
        node.userData.instanceHighlighted = selected;
        refreshRenderableStyle(node);
    });
}

function fitCamera(bounds) {
    logDebug('fitCamera:start', bounds);
    updateReferenceGuides(bounds);
    const min = toVector3(bounds.min);
    const max = toVector3(bounds.max);
    const center = min.clone().add(max).multiplyScalar(0.5);
    const size = max.clone().sub(min);
    const radius = Math.max(size.length() * 0.6, 1);

    controls.target.copy(center);
    camera.position.copy(center.clone().add(new THREE.Vector3(radius, radius * 0.75, radius)));
    camera.near = Math.max(radius / 200, 0.01);
    camera.far = Math.max(radius * 40, 100);
    camera.updateProjectionMatrix();
    controls.update();
    const sceneBox = new THREE.Box3().setFromObject(modelRoot);
    logJson('fitCamera:done', {
        center: center.toArray(),
        radius,
        near: camera.near,
        far: camera.far,
        position: camera.position.toArray(),
        previewBounds: {
            min: bounds.min,
            max: bounds.max
        },
        sceneBounds: boxToLog(sceneBox)
    });
}

function renderGlbPreview(result) {
    const preview = result.preview ?? {};
    logInfo('renderGlbPreview:start', {
        stats: preview?.stats,
        unsupportedFaceCount: Array.isArray(preview?.unsupportedFaces) ? preview.unsupportedFaces.length : 0,
        unsupportedBooleanCount: Array.isArray(preview?.unsupportedBooleans) ? preview.unsupportedBooleans.length : 0,
        instanceCount: Array.isArray(preview?.instances) ? preview.instances.length : 0,
        rootChildren: result.scene.children.length
    });
    clearModel();
    renderPmi(preview.pmi);
    updateUnsupportedFaces(preview.unsupportedFaces);
    updateUnsupportedBooleans(preview.unsupportedBooleans);
    updateStats(preview.stats);
    updateValidation(preview.validation);
    updateProduct(preview.product);
    updateUnits(preview.units);
    renderAssemblyTree(Array.isArray(preview.instances) ? preview.instances : []);

    modelRoot.add(result.scene);
    modelHasEdgeLines = false;
    result.scene.traverse((node) => {
        if (node.userData?.kind === 'instance' && node.userData?.instanceId) {
            assemblyGroups.set(node.userData.instanceId, node);
            return;
        }
        if (!(node.isMesh || isEdgeRenderable(node))) {
            return;
        }
        if (isEdgeRenderable(node)) {
            modelHasEdgeLines = true;
        }
        if (node.isMesh && node.material) {
            const baseColor = node.material.color?.clone() ?? new THREE.Color(0xc87a52);
            const metalness = node.material.metalness ?? 0.08;
            const roughness = node.material.roughness ?? 0.52;
            const opacity = node.material.opacity != null ? node.material.opacity : 1.0;
            const transparent = opacity < 1.0;
            node.material = new THREE.MeshStandardMaterial({
                color: baseColor,
                metalness,
                roughness,
                opacity,
                transparent,
                side: THREE.DoubleSide
            });
        }
        if (node.isMesh && node.userData?.surface) {
            rebuildParametricFaceGeometry(node);
        }
        if (isEdgeRenderable(node) && node.userData?.curve) {
            rebuildCurveEdgeGeometry(node);
        }
        if (Array.isArray(node.userData?.selection)) {
            if (node.material?.color) {
                node.userData.baseColor = node.material.color.getHex();
            }
            node.userData.selectedColor = isEdgeRenderable(node) ? 0xf06d3a : 0xf0b15a;
            node.userData.instanceSelectedColor = isEdgeRenderable(node) ? 0x537983 : 0xe2a46f;
            node.userData.objectSelected = false;
            node.userData.instanceHighlighted = false;
            interactiveObjects.push(node);
        }
        if (node.userData?.stepId != null) {
            registerStepObject(node.userData.stepId, node);
        }
    });
    edgeLinesVisible = false;
    applyEdgeVisibility();

    fitCamera(preview.bounds);
    resetSelection();
    const sceneSummary = {
        meshObjects: 0,
        lineObjects: 0,
        visibleMeshes: 0,
        visibleLines: 0,
        triangleVertices: 0,
        lineVertices: 0,
        parametricFaceTypes: {},
        parametricEdgeTypes: {}
    };
    result.scene.traverse((node) => {
        if (node.isMesh) {
            sceneSummary.meshObjects += 1;
            if (node.visible) {
                sceneSummary.visibleMeshes += 1;
            }
            const position = node.geometry?.getAttribute?.('position');
            if (position) {
                sceneSummary.triangleVertices += position.count;
            }
            const surfaceType = node.userData?.surface?.type;
            if (surfaceType) {
                sceneSummary.parametricFaceTypes[surfaceType] = (sceneSummary.parametricFaceTypes[surfaceType] ?? 0) + 1;
            }
            return;
        }
        if (isEdgeRenderable(node)) {
            sceneSummary.lineObjects += 1;
            if (node.visible) {
                sceneSummary.visibleLines += 1;
            }
            const position = node.geometry?.getAttribute?.('position');
            if (position) {
                sceneSummary.lineVertices += position.count;
            }
            const curveType = node.userData?.curve?.type;
            if (curveType) {
                sceneSummary.parametricEdgeTypes[curveType] = (sceneSummary.parametricEdgeTypes[curveType] ?? 0) + 1;
            }
        }
    });
    logJson('renderGlbPreview:scene-summary', sceneSummary);
    logInfo('renderGlbPreview:done', {
        modelChildren: modelRoot.children.length,
        interactiveObjects: interactiveObjects.length,
        assemblyGroups: assemblyGroups.size,
        meshObjects: interactiveObjects.filter((object) => object.isMesh).length,
        lineObjects: interactiveObjects.filter((object) => isEdgeRenderable(object)).length
    });
}

function parsePreviewGlb(arrayBuffer) {
    return new Promise((resolve, reject) => {
        gltfLoader.parse(arrayBuffer, '', (gltf) => {
            resolve({
                scene: gltf.scene,
                preview: gltf.scene?.userData?.preview ?? {}
            });
        }, reject);
    });
}

async function requestPreview(payload, metadata = {}) {
    let body;
    let contentType = null;
    if (typeof payload === 'string') {
        body = payload;
        contentType = 'text/plain; charset=utf-8';
    } else if (payload instanceof File) {
        const formData = new FormData();
        formData.append('file', payload, payload.name);
        body = formData;
    } else {
        throw new Error('不支持的预览请求载荷');
    }
    logInfo('requestPreview:start', {
        previewRoute: '/api/preview',
        contentType: contentType ?? 'multipart/form-data',
        source: metadata.source ?? 'unknown',
        fileName: metadata.fileName ?? null,
        stepLength: typeof payload === 'string' ? payload.length : null,
        byteLength: payload instanceof File ? payload.size : null
    });
    const headers = {};
    if (contentType) {
        headers['Content-Type'] = contentType;
    }
    const response = await fetch('/api/preview', {
        method: 'POST',
        headers,
        body
    });

    const responseType = response.headers.get('Content-Type') || '';
    if (response.ok && responseType.startsWith('model/gltf-binary')) {
        const arrayBuffer = await response.arrayBuffer();
        const parsed = await parsePreviewGlb(arrayBuffer);
        logInfo('requestPreview:glb-response', {
            ok: response.ok,
            status: response.status,
            previewFormat: response.headers.get('X-MiniCAD-Preview-Format'),
            cache: response.headers.get('X-MiniCAD-Cache'),
            cachePath: response.headers.get('X-MiniCAD-Cache-Path'),
            binaryLength: arrayBuffer.byteLength,
            faceCount: parsed?.preview?.stats?.faceCount ?? null,
            edgeCount: parsed?.preview?.stats?.edgeCount ?? null,
            instanceCount: Array.isArray(parsed?.preview?.instances) ? parsed.preview.instances.length : 0
        });
        logJson('requestPreview:unsupported-summary', summarizeUnsupportedFaces(parsed?.preview?.unsupportedFaces));
        return parsed;
    }

    const text = await response.text();
    logInfo('requestPreview:response', {
        ok: response.ok,
        status: response.status,
        textLength: text.length,
        contentType: responseType
    });
    if (!response.ok) {
        try {
            const errorPayload = JSON.parse(text);
            logError('requestPreview:error-payload', errorPayload);
            throw new Error(errorPayload.error || 'STEP 解析失败');
        } catch (error) {
            if (error instanceof SyntaxError) {
                logError('requestPreview:non-json-error-body', text);
                throw new Error(text || 'STEP 解析失败');
            }
            throw error;
        }
    }
    throw new Error(`预览接口仅支持 GLB 响应，实际收到: ${responseType || 'unknown'}`);
}

async function renderCurrentInput() {
    if (!uploadedFile) {
        setStatus('请先选择 STEP 文件。');
        updateStats();
        clearModel();
        return;
    }

    setStatus('正在解析 STEP 并生成预览...');
    try {
        const preview = await requestPreview(uploadedFile, {
            source: 'file-form',
            fileName: uploadedFile.name
        });
        renderGlbPreview(preview);
        const previewData = preview?.preview ?? {};
        const unsupported = previewData?.stats?.unsupportedFaceCount ?? 0;
        const unsupportedBooleans = previewData?.stats?.unsupportedBooleanCount ?? 0;
        const suffixParts = [];
        if (unsupported > 0) {
            suffixParts.push(`跳过 ${unsupported} 个暂不支持的面`);
        }
        if (unsupportedBooleans > 0) {
            suffixParts.push(`跳过 ${unsupportedBooleans} 个暂不支持的布尔结果`);
        }
        const suffix = suffixParts.length > 0 ? `，${suffixParts.join('，')}。` : '。';
        setStatus(`渲染完成：${previewData?.stats?.faceCount ?? 0} 个面，${previewData?.stats?.edgeCount ?? 0} 条边${suffix}`);
        logInfo('renderCurrentInput:success', previewData?.stats);
    } catch (error) {
        clearModel();
        updateStats();
        setStatus(error.message);
        logError('renderCurrentInput:failed', error);
    }
}

fileInput.addEventListener('change', async (event) => {
    const [file] = event.target.files;
    if (!file) {
        return;
    }
    uploadedFile = file;
    setStatus(`已选择文件：${file.name}，正在生成预览。`);
    const prefixBytes = new Uint8Array(await file.slice(0, 16).arrayBuffer());
    logInfo('fileInput:loaded', {
        fileName: file.name,
        size: file.size,
        textLength: 0,
        byteLength: file.size,
        bodyPrefixHex: Array.from(prefixBytes).map((value) => value.toString(16).padStart(2, '0')).join(' ')
    });
    void renderCurrentInput();
});

renderer.domElement.addEventListener('click', (event) => {
    if (interactiveObjects.length === 0) {
        logJson('canvasClick:no-interactive-objects', {});
        return;
    }

    const rect = renderer.domElement.getBoundingClientRect();
    pointer.x = ((event.clientX - rect.left) / rect.width) * 2 - 1;
    pointer.y = -((event.clientY - rect.top) / rect.height) * 2 + 1;
    raycaster.setFromCamera(pointer, camera);

    logJson('canvasClick:start', {
        clientX: event.clientX,
        clientY: event.clientY,
        pointer: { x: pointer.x, y: pointer.y },
        cameraPosition: camera.position.toArray(),
        controlsTarget: controls.target.toArray(),
        interactiveObjects: interactiveObjects.length
    });

    const hits = raycaster.intersectObjects(interactiveObjects.filter((object) => object.visible), false);
    logJson('canvasClick:hits', {
        hitCount: hits.length,
        hits: hits.slice(0, 5).map((hit) => ({
            distance: hit.distance,
            point: hit.point.toArray(),
            objectType: hit.object.type,
            stepId: hit.object.userData?.stepId ?? null,
            instanceId: hit.object.userData?.instanceId ?? null
        }))
    });
    if (selectedObject) {
        selectedObject.userData.objectSelected = false;
        refreshRenderableStyle(selectedObject);
        selectedObject = null;
    }

    if (hits.length === 0) {
        logJson('canvasClick:no-hit', {
            cameraPosition: camera.position.toArray(),
            controlsTarget: controls.target.toArray()
        });
        setSelection([
            ['类型', '未选中'],
            ['说明', '点击右侧模型中的面或边查看详情。']
        ]);
        return;
    }

    logJson('canvasClick:selecting-hit', {
        stepId: hits[0].object.userData?.stepId ?? null,
        instanceId: hits[0].object.userData?.instanceId ?? null,
        objectType: hits[0].object.type
    });
    selectRenderable(hits[0].object);
    syncPmiTargetHighlight();
});

if (isolateSelectionButton) {
    isolateSelectionButton.addEventListener('click', () => {
        if (selectedAssemblyGroup?.userData?.instanceId) {
            showOnlyInstance(selectedAssemblyGroup.userData.instanceId);
            return;
        }
        if (selectedObject?.userData?.instanceId) {
            showOnlyInstance(selectedObject.userData.instanceId);
            return;
        }
        if (selectedObject) {
            for (const object of interactiveObjects) {
                object.visible = object === selectedObject;
            }
        }
    });
}

if (showAllButton) {
    showAllButton.addEventListener('click', () => {
        showAllInstances();
    });
}

if (togglePmiButton) {
    togglePmiButton.addEventListener('click', () => {
        pmiVisible = !pmiVisible;
        applyPmiVisibility();
    });
}

if (toggleEdgesButton) {
    toggleEdgesButton.addEventListener('click', () => {
        if (!modelHasEdgeLines) {
            return;
        }
        edgeLinesVisible = !edgeLinesVisible;
        applyEdgeVisibility();
    });
    updateEdgeToggleButton();
}
