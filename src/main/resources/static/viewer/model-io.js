/** File validation, preview requests, GLB parsing and model teardown. */

import * as THREE from 'three';
import { GLTFLoader } from 'three/addons/loaders/GLTFLoader.js';
import { viewState } from './state.js';
import { assemblyButtons, assemblyGroups, renderAssemblyTree } from './assembly.js';
import { fileExtension, formatBytes, summarizeUnsupportedFaces } from './format.js';
import { logDebug, logError, logInfo, logJson, logWarn } from './log.js';
import { rebuildCurveEdgeGeometry, rebuildParametricFaceGeometry } from './parametric-geometry.js';
import { pmiOverlay, renderPmi, togglePmiButton } from './pmi.js';
import { disposeMaterial, disposeObject, fitCamera, gltfLoader, modelRoot, pmiRoot, requestRender, scene } from './scene.js';
import { applyEdgeVisibility, isEdgeRenderable, registerStepObject, stepObjects } from './selection.js';
import { resetSelection, setStatus, updateEdgeToggleButton, updateProduct, updateStats, updateUnits, updateUnsupportedBooleans, updateUnsupportedFaces, updateValidation } from './ui-panels.js';
export const fileInput = document.querySelector('#file-input');

export let uploadedFile = null;

export const defaultMaxUploadBytes = 50 * 1024 * 1024;
export let maxUploadBytes = defaultMaxUploadBytes;
export const acceptedStepExtensions = new Set(['.step', '.stp', '.p21']);

export function validateStepFile(file) {
    if (!file) {
        return 'No STEP file selected.';
    }
    const extension = fileExtension(file.name);
    if (!acceptedStepExtensions.has(extension)) {
        return 'Only .step, .stp, and .p21 files are accepted.';
    }
    if (file.size > maxUploadBytes) {
        return `File is ${formatBytes(file.size)}. The viewer upload limit is ${formatBytes(maxUploadBytes)}.`;
    }
    return null;
}

export async function loadViewerConfig() {
    try {
        const response = await fetch('/api/config', { method: 'GET' });
        if (!response.ok) {
            throw new Error(`config request failed with status ${response.status}`);
        }
        const config = await response.json();
        if (Number.isSafeInteger(config.maxUploadBytes) && config.maxUploadBytes >= 0) {
            maxUploadBytes = config.maxUploadBytes;
        }
        logInfo('viewerConfig:loaded', {
            maxUploadBytes,
            previewCacheEnabled: Boolean(config.previewCacheEnabled)
        });
    } catch (error) {
        maxUploadBytes = defaultMaxUploadBytes;
        logWarn('viewerConfig:fallback', {
            maxUploadBytes,
            reason: error.message
        });
    }
}

export function clearModel() {
    logDebug('clearModel', {
        modelChildren: modelRoot.children.length,
        pmiChildren: pmiRoot.children.length,
        interactiveObjects: viewState.interactiveObjects.length,
        pmiLabels: viewState.pmiLabels.length
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
    viewState.pmiLabels = [];
    viewState.interactiveObjects = [];
    assemblyGroups.clear();
    assemblyButtons.clear();
    stepObjects.clear();
    resetSelection();
    updateValidation();
    updateProduct();
    updateUnits();
    updateUnsupportedFaces();
    updateUnsupportedBooleans();
    renderAssemblyTree([]);
    viewState.edgeLinesVisible = false;
    viewState.modelHasEdgeLines = false;
    updateEdgeToggleButton();
    if (togglePmiButton) {
        togglePmiButton.textContent = '隐藏 PMI';
    }
    requestRender();
}

export function renderGlbPreview(result) {
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
    viewState.modelHasEdgeLines = false;
    result.scene.traverse((node) => {
        if (node.userData?.kind === 'instance' && node.userData?.instanceId) {
            assemblyGroups.set(node.userData.instanceId, node);
            return;
        }
        if (!(node.isMesh || isEdgeRenderable(node))) {
            return;
        }
        if (isEdgeRenderable(node)) {
            viewState.modelHasEdgeLines = true;
        }
        if (node.isMesh && node.material) {
            const sourceMaterial = node.material;
            const baseColor = sourceMaterial.color?.clone() ?? new THREE.Color(0xc87a52);
            const metalness = sourceMaterial.metalness ?? 0.08;
            const roughness = sourceMaterial.roughness ?? 0.52;
            const opacity = sourceMaterial.opacity != null ? sourceMaterial.opacity : 1.0;
            const transparent = opacity < 1.0;
            node.material = new THREE.MeshStandardMaterial({
                color: baseColor,
                metalness,
                roughness,
                opacity,
                transparent,
                side: THREE.DoubleSide
            });
            if (Array.isArray(sourceMaterial)) {
                sourceMaterial.forEach((material) => disposeMaterial(material));
            } else {
                disposeMaterial(sourceMaterial);
            }
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
            viewState.interactiveObjects.push(node);
        }
        if (node.userData?.stepId != null) {
            registerStepObject(node.userData.stepId, node);
        }
    });
    viewState.edgeLinesVisible = false;
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
        interactiveObjects: viewState.interactiveObjects.length,
        assemblyGroups: assemblyGroups.size,
        meshObjects: viewState.interactiveObjects.filter((object) => object.isMesh).length,
        lineObjects: viewState.interactiveObjects.filter((object) => isEdgeRenderable(object)).length
    });
}

export function parsePreviewGlb(arrayBuffer) {
    return new Promise((resolve, reject) => {
        gltfLoader.parse(arrayBuffer, '', (gltf) => {
            resolve({
                scene: gltf.scene,
                preview: gltf.scene?.userData?.preview ?? {}
            });
        }, reject);
    });
}

export async function requestPreview(payload, metadata = {}) {
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

export async function renderCurrentInput() {
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

export async function handleSelectedFile(file, source) {
    if (!file) {
        return;
    }
    const validationError = validateStepFile(file);
    if (validationError) {
        uploadedFile = null;
        fileInput.value = '';
        clearModel();
        updateStats();
        setStatus(validationError);
        logWarn('fileInput:rejected', {
            source,
            fileName: file.name,
            size: file.size,
            reason: validationError
        });
        return;
    }
    uploadedFile = file;
    setStatus(`已选择文件：${file.name}，正在生成预览。`);
    logInfo('fileInput:loaded', {
        source,
        fileName: file.name,
        size: file.size,
        textLength: 0,
        byteLength: file.size
    });
    void renderCurrentInput();
}
