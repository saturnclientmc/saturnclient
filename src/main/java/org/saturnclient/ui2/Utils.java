package org.saturnclient.ui2;

import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.ui2.elements.Notification;
import org.saturnclient.ui2.elements.Notification.NotificationKind;

public class Utils {
    public static boolean isHovering(int mouseX, int mouseY, int elementWidth, int elementHeight, float elementScale) {
        return mouseX >= 0 &&
                mouseX <= (elementWidth * elementScale) &&
                mouseY >= 0 &&
                mouseY <= (elementHeight * elementScale);
    }

    public static char getCharFromKey(int keyCode, int modifiers) {
        boolean shift = (modifiers & GLFW.GLFW_MOD_SHIFT) != 0;

        // A-Z
        if (keyCode >= GLFW.GLFW_KEY_A && keyCode <= GLFW.GLFW_KEY_Z) {
            return (char) (shift
                    ? 'A' + (keyCode - GLFW.GLFW_KEY_A)
                    : 'a' + (keyCode - GLFW.GLFW_KEY_A));
        }

        // 0-9
        if (keyCode >= GLFW.GLFW_KEY_0 && keyCode <= GLFW.GLFW_KEY_9) {
            return shift
                    ? ")!@#$%^&*(".charAt(keyCode - GLFW.GLFW_KEY_0)
                    : (char) ('0' + (keyCode - GLFW.GLFW_KEY_0));
        }

        // Special characters
        return switch (keyCode) {
            case GLFW.GLFW_KEY_SPACE -> ' ';
            case GLFW.GLFW_KEY_PERIOD -> shift ? '>' : '.';
            case GLFW.GLFW_KEY_COMMA -> shift ? '<' : ',';
            case GLFW.GLFW_KEY_MINUS -> shift ? '_' : '-';
            case GLFW.GLFW_KEY_EQUAL -> shift ? '+' : '=';
            case GLFW.GLFW_KEY_SEMICOLON -> shift ? ':' : ';';
            case GLFW.GLFW_KEY_APOSTROPHE -> shift ? '"' : '\'';
            case GLFW.GLFW_KEY_SLASH -> shift ? '?' : '/';
            case GLFW.GLFW_KEY_BACKSLASH -> shift ? '|' : '\\';
            case GLFW.GLFW_KEY_LEFT_BRACKET -> shift ? '{' : '[';
            case GLFW.GLFW_KEY_RIGHT_BRACKET -> shift ? '}' : ']';
            case GLFW.GLFW_KEY_GRAVE_ACCENT -> shift ? '~' : '`';
            default -> '\0';
        };
    }

    public static <T> T getOrNull(List<T> list, int index) {
        SaturnClient.LOGGER.info("Accessing index {} in list of size {}", index, list.size());
        return (index >= 0 && index < list.size()) ? list.get(index) : null;
    }

    public static void notify(NotificationKind kind, String title, String toast) {
        if (SaturnClient.client.currentScreen instanceof SaturnScreen) {
            ((SaturnScreen) SaturnClient.client.currentScreen)
                    .draw(new Notification(SaturnClient.client.currentScreen.width,
                            SaturnClient.client.currentScreen.height, kind,
                            title, toast));
        }
    }
}
