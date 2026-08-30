/** Raycast picking, edge visibility and selected-object styling. */

import * as THREE from 'three';
import { viewState } from './state.js';
import { activateAssemblyInstance, assemblyButtons, assemblyGroups } from './assembly.js';
import { logJson } from './log.js';
import { syncPmiTargetHighlight } from './pmi.js';
import { camera, controls, modelRoot, requestRender } from './scene.js';
import { setSelection, updateEdgeToggleButton } from './ui-panels.js';
export const toggleEdgesButton = document.querySelector('#toggle-edges');

export const raycaster = new THREE.Raycaster();
raycaster.params.Line.threshold = 0.14;

export const pointer = new THREE.Vector2();

export const stepObjects = new Map();

export function isEdgeRenderable(node) {
    return node?.userData?.kind === 'edge'
        || node?.isLine;
}

export function applyEdgeVisibility() {
    modelRoot.traverse((node) => {
        if (isEdgeRenderable(node)) {
            node.visible = viewState.edgeLinesVisible;
        }
    });
    updateEdgeToggleButton();
    requestRender();
}

export function refreshRenderableStyle(object) {
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
    requestRender();
}

export function registerStepObject(stepId, object) {
    if (stepId == null) {
        return;
    }
    if (!stepObjects.has(stepId)) {
        stepObjects.set(stepId, []);
    }
    stepObjects.get(stepId).push(object);
}

export function selectRenderable(object) {
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
    if (viewState.selectedObject && viewState.selectedObject !== object) {
        viewState.selectedObject.userData.objectSelected = false;
        refreshRenderableStyle(viewState.selectedObject);
    }
    viewState.selectedObject = object;
    if (viewState.selectedObject.userData.instanceId) {
        activateAssemblyInstance(
            assemblyGroups.get(viewState.selectedObject.userData.instanceId),
            assemblyButtons.get(viewState.selectedObject.userData.instanceId)
        );
    }
    viewState.selectedObject.userData.objectSelected = true;
    refreshRenderableStyle(viewState.selectedObject);
    setSelection(viewState.selectedObject.userData.selection);
    syncPmiTargetHighlight();
    logJson('selectRenderable:done', {
        stepId: viewState.selectedObject.userData?.stepId ?? null,
        instanceId: viewState.selectedObject.userData?.instanceId ?? null,
        objectType: viewState.selectedObject.type,
        cameraPosition: camera.position.toArray(),
        controlsTarget: controls.target.toArray()
    });
}
