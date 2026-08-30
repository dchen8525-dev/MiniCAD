/** Console logging with a shared prefix. */
export const viewerLogPrefix = '[MiniCAD Viewer]';

export function logDebug(message, ...args) {
    console.debug(viewerLogPrefix, message, ...args);
}

export function logInfo(message, ...args) {
    console.info(viewerLogPrefix, message, ...args);
}

export function logWarn(message, ...args) {
    console.warn(viewerLogPrefix, message, ...args);
}

export function logError(message, ...args) {
    console.error(viewerLogPrefix, message, ...args);
}

export function logJson(label, payload) {
    try {
        console.info(`${viewerLogPrefix} ${label} ${JSON.stringify(payload)}`);
    } catch (error) {
        console.info(`${viewerLogPrefix} ${label}`, payload);
    }
}
