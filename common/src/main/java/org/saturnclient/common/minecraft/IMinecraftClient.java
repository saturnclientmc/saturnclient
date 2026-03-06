package org.saturnclient.common.minecraft;

import java.io.File;
import java.io.InputStream;

import org.saturnclient.common.minecraft.bindings.SaturnIdentifier;

public interface IMinecraftClient {
    public File getRunDirectory();

    public InputStream getResource(SaturnIdentifier identifier);
}
