package org.saturnclient.common.minecraft;

public class SaturnIdentifier {
    public Object id;

    public SaturnIdentifier(String namespace, String path) {
        this.id = MinecraftProvider.PROVIDER.createIdentifier(namespace, path);
    }
}
