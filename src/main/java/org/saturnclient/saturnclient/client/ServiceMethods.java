package org.saturnclient.saturnclient.client;

import dev.selimaj.session.types.Method;

public class ServiceMethods {
    public record AuthResponse(String cloak, String hat, String[] cloaks, String[] hats) {
    }

    public static final Method<String, AuthResponse, String> Authenticate = new Method<>("auth",
            String.class, AuthResponse.class, String.class);

    // Equip
    public static final Method<String, String, String> SetCloak = new Method<>("set_cloak",
            String.class, String.class, String.class);
    public static final Method<String, String, String> SetHat = new Method<>("set_hat",
            String.class, String.class, String.class);

    // Buy
    public static final Method<String, String, String> BuyCloak = new Method<>("buy_cloak",
            String.class, String.class, String.class);
    public static final Method<String, String, String> BuyHat = new Method<>("buy_hat",
            String.class, String.class, String.class);

    // Emote
    public static final Method<String, String, String> Emote = new Method<>("emote",
            String.class, String.class, String.class);
}