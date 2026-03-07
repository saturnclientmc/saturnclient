package org.saturnclient.common.minecraft;

import java.io.File;
import java.io.InputStream;

import org.saturnclient.common.minecraft.bindings.SaturnIdentifier;
import org.saturnclient.common.minecraft.render.IWindow;

public interface IMinecraftClient {

    public File getRunDirectory();

    public InputStream getResource(SaturnIdentifier identifier);

    public IWindow getWindow();
}
