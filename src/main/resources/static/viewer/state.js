/**
 * Mutable state shared across viewer modules.
 *
 * ES modules let an importer read a live binding but not assign to it, so any
 * variable written from a module other than the one declaring it has to live
 * somewhere both sides can write. Keeping all six here -- rather than scattered
 * across the modules that happen to touch them most -- makes every shared write
 * greppable, which is the main thing a reader needs from this file.
 */
export const viewState = {
    interactiveObjects: [],
    selectedObject: null,
    pmiLabels: [],
    pmiVisible: true,
    edgeLinesVisible: false,
    modelHasEdgeLines: false
};
