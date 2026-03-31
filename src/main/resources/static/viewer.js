import * as THREE from 'three';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import { GLTFLoader } from 'three/addons/loaders/GLTFLoader.js';

const fileInput = document.querySelector('#file-input');
const renderButton = document.querySelector('#render-button');
const loadExampleButton = document.querySelector('#load-example');
const exampleSelect = document.querySelector('#example-select');
const statusText = document.querySelector('#status-text');
const validationDetails = document.querySelector('#validation-details');
const validationReport = document.querySelector('#validation-report');
const unsupportedFacesList = document.querySelector('#unsupported-faces');
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
const basePixelRatio = Math.min(window.devicePixelRatio, 3);
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

const controls = new OrbitControls(camera, renderer.domElement);
const gltfLoader = new GLTFLoader();
controls.enableDamping = true;
controls.dampingFactor = 0.08;
controls.target.set(0, 0, 0);

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
let loadedExampleText = '';
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
        return 2.8;
    }
    if (distance <= 4.5) {
        return 2.2;
    }
    if (distance <= 8.0) {
        return 1.7;
    }
    return 1.25;
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
    if (!Array.isArray(unsupportedFaces) || unsupportedFaces.length === 0) {
        unsupportedFacesList.innerHTML = '<li><button type="button" disabled><strong>无</strong><span>当前预览没有被跳过的面。</span></button></li>';
        return;
    }
    unsupportedFacesList.innerHTML = '';
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

function applyEdgeVisibility() {
    modelRoot.traverse((node) => {
        if (node.isLine) {
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
        instanceCount: Array.isArray(preview?.instances) ? preview.instances.length : 0,
        rootChildren: result.scene.children.length
    });
    clearModel();
    renderPmi(preview.pmi);
    updateUnsupportedFaces(preview.unsupportedFaces);
    updateStats(preview.stats);
    updateValidation(preview.validation);
    renderAssemblyTree(Array.isArray(preview.instances) ? preview.instances : []);

    modelRoot.add(result.scene);
    modelHasEdgeLines = false;
    result.scene.traverse((node) => {
        if (node.userData?.kind === 'instance' && node.userData?.instanceId) {
            assemblyGroups.set(node.userData.instanceId, node);
            return;
        }
        if (!(node.isMesh || node.isLine)) {
            return;
        }
        if (node.isLine) {
            modelHasEdgeLines = true;
        }
        if (node.isMesh && node.material) {
            const baseColor = node.material.color?.clone() ?? new THREE.Color(0xc87a52);
            node.material = new THREE.MeshStandardMaterial({
                color: baseColor,
                metalness: 0.08,
                roughness: 0.52,
                side: THREE.DoubleSide
            });
        }
        if (Array.isArray(node.userData?.selection)) {
            if (node.material?.color) {
                node.userData.baseColor = node.material.color.getHex();
            }
            node.userData.selectedColor = node.isLine ? 0xf06d3a : 0xf0b15a;
            node.userData.instanceSelectedColor = node.isLine ? 0x537983 : 0xe2a46f;
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
        lineVertices: 0
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
            return;
        }
        if (node.isLine) {
            sceneSummary.lineObjects += 1;
            if (node.visible) {
                sceneSummary.visibleLines += 1;
            }
            const position = node.geometry?.getAttribute?.('position');
            if (position) {
                sceneSummary.lineVertices += position.count;
            }
        }
    });
    logJson('renderGlbPreview:scene-summary', sceneSummary);
    logInfo('renderGlbPreview:done', {
        modelChildren: modelRoot.children.length,
        interactiveObjects: interactiveObjects.length,
        assemblyGroups: assemblyGroups.size,
        meshObjects: interactiveObjects.filter((object) => object.isMesh).length,
        lineObjects: interactiveObjects.filter((object) => object.isLine).length
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
    const stepText = loadedExampleText.trim();
    if (!stepText && !uploadedFile) {
        setStatus('请先选择 STEP 文件，或加载一个示例。');
        updateStats();
        clearModel();
        return;
    }

    setStatus('正在解析 STEP 并生成预览...');
    try {
        const preview = uploadedFile
            ? await requestPreview(uploadedFile, {
                source: 'file-form',
                fileName: uploadedFile.name
            })
            : await requestPreview(stepText, {
                source: 'textarea'
            });
        renderGlbPreview(preview);
        const previewData = preview?.preview ?? {};
        const unsupported = previewData?.stats?.unsupportedFaceCount ?? 0;
        const suffix = unsupported > 0 ? `，跳过 ${unsupported} 个暂不支持的面。` : '。';
        setStatus(`渲染完成：${previewData?.stats?.faceCount ?? 0} 个面，${previewData?.stats?.edgeCount ?? 0} 条边${suffix}`);
        logInfo('renderCurrentInput:success', previewData?.stats);
    } catch (error) {
        clearModel();
        updateStats();
        setStatus(error.message);
        logError('renderCurrentInput:failed', error);
    }
}

renderButton.addEventListener('click', () => {
    void renderCurrentInput();
});

loadExampleButton.addEventListener('click', async () => {
    setStatus('正在加载示例...');
    try {
        logInfo('loadExample:start', { example: exampleSelect.value });
        const response = await fetch(`/api/example?name=${encodeURIComponent(exampleSelect.value)}`);
        if (!response.ok) {
            throw new Error('示例文件不可用');
        }
        uploadedFile = null;
        loadedExampleText = await response.text();
        setStatus(`示例 ${exampleSelect.value} 已加载，正在生成预览...`);
        logInfo('loadExample:done', {
            example: exampleSelect.value,
            length: loadedExampleText.length
        });
        await renderCurrentInput();
    } catch (error) {
        setStatus(error.message);
        logError('loadExample:failed', error);
    }
});

fileInput.addEventListener('change', async (event) => {
    const [file] = event.target.files;
    if (!file) {
        return;
    }
    uploadedFile = file;
    loadedExampleText = '';
    setStatus(`已选择文件：${file.name}，点击“解析并渲染”开始预览。`);
    const prefixBytes = new Uint8Array(await file.slice(0, 16).arrayBuffer());
    logInfo('fileInput:loaded', {
        fileName: file.name,
        size: file.size,
        textLength: 0,
        byteLength: file.size,
        bodyPrefixHex: Array.from(prefixBytes).map((value) => value.toString(16).padStart(2, '0')).join(' ')
    });
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
