/**
 * Viewer entry point: wires DOM events to the viewer modules and boots once.
 */
import { viewState } from './viewer/state.js';
import {
    applyEdgeVisibility, pointer, raycaster, refreshRenderableStyle,
    selectRenderable, toggleEdgesButton
} from './viewer/selection.js';
import { applyPmiVisibility, syncPmiTargetHighlight, togglePmiButton } from './viewer/pmi.js';
import { setSelection, updateEdgeToggleButton } from './viewer/ui-panels.js';
import {
    isolateSelectionButton, selectedAssemblyGroup, showAllButton,
    showAllInstances, showOnlyInstance
} from './viewer/assembly.js';
import { fileInput, handleSelectedFile, loadViewerConfig } from './viewer/model-io.js';
import { camera, controls, renderer, sceneHost } from './viewer/scene.js';
import { logJson } from './viewer/log.js';

fileInput.addEventListener('change', async (event) => {
    const [file] = event.target.files;
    await handleSelectedFile(file, 'input');
});

sceneHost.addEventListener('dragover', (event) => {
    event.preventDefault();
    event.dataTransfer.dropEffect = 'copy';
    sceneHost.classList.add('drag-over');
});

sceneHost.addEventListener('dragleave', () => {
    sceneHost.classList.remove('drag-over');
});

sceneHost.addEventListener('drop', async (event) => {
    event.preventDefault();
    sceneHost.classList.remove('drag-over');
    const [file] = event.dataTransfer.files;
    await handleSelectedFile(file, 'drop');
});

renderer.domElement.addEventListener('click', (event) => {
    if (viewState.interactiveObjects.length === 0) {
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
        interactiveObjects: viewState.interactiveObjects.length
    });

    const hits = raycaster.intersectObjects(viewState.interactiveObjects.filter((object) => object.visible), false);
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
    if (viewState.selectedObject) {
        viewState.selectedObject.userData.objectSelected = false;
        refreshRenderableStyle(viewState.selectedObject);
        viewState.selectedObject = null;
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
        if (viewState.selectedObject?.userData?.instanceId) {
            showOnlyInstance(viewState.selectedObject.userData.instanceId);
            return;
        }
        if (viewState.selectedObject) {
            for (const object of viewState.interactiveObjects) {
                object.visible = object === viewState.selectedObject;
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
        viewState.pmiVisible = !viewState.pmiVisible;
        applyPmiVisibility();
    });
}

if (toggleEdgesButton) {
    toggleEdgesButton.addEventListener('click', () => {
        if (!viewState.modelHasEdgeLines) {
            return;
        }
        viewState.edgeLinesVisible = !viewState.edgeLinesVisible;
        applyEdgeVisibility();
    });
    updateEdgeToggleButton();
}

loadViewerConfig();
