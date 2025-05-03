package org.saturnclient.ui2;

public class Utils {
    public static boolean isHovering(int mouseX, int mouseY, int elementWidth, int elementHeight, float elementScale) {
        return mouseX >= 0 &&
               mouseX <= (elementWidth * elementScale) &&
               mouseY >= 0 &&
               mouseY <= (elementHeight * elementScale);
    }
}
