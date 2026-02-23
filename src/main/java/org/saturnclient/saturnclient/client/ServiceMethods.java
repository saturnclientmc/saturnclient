package org.saturnclient.saturnclient.client;

import java.util.UUID;

import org.saturnclient.saturnclient.client.player.SaturnPlayer;

import dev.selimaj.session.types.Method;

public class ServiceMethods {
    public class Types {
        public record EmoteRequest(String emote, String[] targets) {
        }

        public record EmoteEvent(String emote, String from) {
        }

        public record Player(String cloak, String hat, String[] cloaks, String[] hats) {
            public SaturnPlayer toSaturnPlayer(UUID uuid, String name) {
                return new SaturnPlayer(uuid, name, cloak, hat);
            }
        }
    }

    public static final Method<String, Types.Player, String> Authenticate = new Method<>("auth",
            String.class, Types.Player.class, String.class);

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
    public static final Method<Types.EmoteRequest, String, String> Emote = new Method<>("emote",
            Types.EmoteRequest.class, String.class, String.class);

    public static final Method<Types.EmoteEvent, String, String> EmoteEvent = new Method<>("emote_event",
            Types.EmoteEvent.class, String.class, String.class);

    public static final Method<String, Types.Player, String> Player = new Method<>("player",
            String.class, Types.Player.class, String.class);
}