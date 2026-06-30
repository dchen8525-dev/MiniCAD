package com.minicad.app;

class SerializationHelper {
    static String toJson(PreviewPayload payload) {
        return PreviewSerializers.toJson(payload);
    }

    static byte[] toBinary(PreviewPayload payload) {
        return PreviewSerializers.toBinary(payload);
    }

    static byte[] toGlb(PreviewPayload payload) {
        return PreviewSerializers.toGlb(payload);
    }


    static long elapsedMillis(long startedAt) {
        return (System.nanoTime() - startedAt) / 1_000_000L;
    }
}
