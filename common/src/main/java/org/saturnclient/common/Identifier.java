package org.saturnclient.common;

public record Identifier(String namespace, String path) {
    public static Identifier of(String namespace, String path) {
        return new Identifier(namespace, path);
    }
}