package org.saturnclient.ui;

import org.saturnclient.common.minecraft.bindings.SaturnIdentifier;
import org.saturnclient.common.minecraft.bindings.SaturnItemStack;
import org.saturnclient.common.minecraft.bindings.SaturnSprite;

public interface RenderScope {
    public void setOpacity(float alpha);

    public int getColor(int color);

    public void drawRect(int x, int y, int width, int height, int color);

    public void drawText(String text, int x, int y, int font, int color);

    public void drawText(float scale, String text, int x, int y, int font, int color);

    public void drawRoundedRectangle(int x, int y, int width, int height, int radius, int color);

    public int getScaledWindowWidth();

    public int getScaledWindowHeight();

    /* Saturn common sprites */
    public void drawTexture(SaturnIdentifier sprite, int x, int y, float u, float v, int width, int height, int color);

    public void drawTexture(SaturnIdentifier sprite, int x, int y, float u, float v, int width, int height);

    public void drawTexture(SaturnIdentifier sprite, int x, int y, float u, float v, int width, int height,
            int regionWith,
            int regionHeight, int textureWidth, int textureHeight);

    public void drawTexture(SaturnIdentifier sprite, int x, int y, float u, float v, int width, int height,
            int regionWidth,
            int regionHeight, int textureWidth, int textureHeight, int color);

    public void enableScissor(int x1, int y1, int x2, int y2);

    public void disableScissor();

    public boolean scissorContains(int x, int y);

    public void draw();

    public void drawItem(SaturnItemStack item, int x, int y);

    public void drawItem(SaturnItemStack stack, int x, int y, int seed);

    public void drawItem(SaturnItemStack stack, int x, int y, int seed, int z);

    public void drawItemWithoutEntity(SaturnItemStack stack, int x, int y);

    public void drawItemWithoutEntity(SaturnItemStack stack, int x, int y, int seed);

    public void drawSpriteStretched(SaturnSprite sprite, int x, int y, int width, int height);

    public void drawSpriteStretched(SaturnSprite sprite, int x, int y, int width, int height, int color);

    public void fill(int x1, int y1, int x2, int y2, int color);

    public void fill(int x1, int y1, int x2, int y2, int z, int color);

    public void drawBorder(int x, int y, int width, int height, int color);
}
