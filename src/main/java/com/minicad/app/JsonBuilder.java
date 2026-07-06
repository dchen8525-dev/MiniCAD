package com.minicad.app;

import java.util.List;
import java.util.Map;

/**
 * Simple JSON serialization utilities for StepDumpApp output.
 * Package-private helper class with static methods.
 */
final class JsonBuilder {

    private JsonBuilder() {
    }

    static String toJson(List<Map<String, Object>> results) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < results.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(toJsonMap(results.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    static String toJsonMap(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        int i = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (i > 0) sb.append(",");
            sb.append("\"").append(entry.getKey()).append("\":");
            sb.append(toJsonValue(entry.getValue()));
            i++;
        }
        sb.append("}");
        return sb.toString();
    }

    static String toJsonValue(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            return "\"" + jsonEscape((String) value) + "\"";
        } else if (value instanceof Number) {
            return value.toString();
        } else if (value instanceof Boolean) {
            return value.toString();
        } else if (value instanceof List) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            List<?> list = (List<?>) value;
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(toJsonValue(list.get(i)));
            }
            sb.append("]");
            return sb.toString();
        } else if (value instanceof Map) {
            return toJsonMap((Map<String, Object>) value);
        } else if (value instanceof double[]) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            double[] arr = (double[]) value;
            for (int i = 0; i < arr.length; i++) {
                if (i > 0) sb.append(",");
                sb.append(arr[i]);
            }
            sb.append("]");
            return sb.toString();
        }
        return "\"" + jsonEscape(value.toString()) + "\"";
    }

    static String jsonEscape(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default: sb.append(c);
            }
        }
        return sb.toString();
    }
}