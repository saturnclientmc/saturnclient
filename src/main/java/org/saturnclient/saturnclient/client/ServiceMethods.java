package org.saturnclient.saturnclient.client;

import dev.selimaj.session.types.Method;

public class ServiceMethods {
    public record AuthResponse(String cloak, String hat, String[] cloaks, String[] hats) {
    }

    public static final Method<String, AuthResponse, String> Authenticate = new Method<>("auth",
            String.class, AuthResponse.class, String.class);
}