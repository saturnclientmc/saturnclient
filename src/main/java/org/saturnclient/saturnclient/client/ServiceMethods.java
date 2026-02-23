package org.saturnclient.saturnclient.client;

import java.util.UUID;

import org.saturnclient.saturnclient.client.ServiceMethods.Types.SendPlayerRequest;
import org.saturnclient.saturnclient.client.player.SaturnPlayer;

import dev.selimaj.session.types.Method;

public class ServiceMethods {

    // =========================
    // Types (Rust structs)
    // =========================
    public static class Types {

        public record EmoteRequest(String emote, String[] targets) {
        }

        public record EmoteEvent(String emote, String from) {
        }

        public record Player(
                String cloak,
                String hat,
                String[] cloaks,
                String[] hats) {
            public SaturnPlayer toSaturnPlayer(UUID uuid, String name) {
                return new SaturnPlayer(uuid, name, cloak, hat);
            }
        }

        public record PlayerStream(Player player, String name, String uuid) {
            public SaturnPlayer toSaturnPlayer() {
                return new SaturnPlayer(UUID.fromString(uuid), name, this.player.cloak, this.player.hat);
            }
        }

        public record SendPlayerRequest(String[] targets) {
        }
    }

    // =========================
    // AUTH
    // =========================
    public static final Method<String, Types.Player, String> Authenticate = new Method<>("auth",
            String.class,
            Types.Player.class,
            String.class);

    // =========================
    // EQUIP
    // =========================
    public static final Method<String, String, String> SetCloak = new Method<>("set_cloak",
            String.class,
            String.class,
            String.class);

    public static final Method<String, String, String> SetHat = new Method<>("set_hat",
            String.class,
            String.class,
            String.class);

    // =========================
    // BUY
    // =========================
    public static final Method<String, String, String> BuyCloak = new Method<>("buy_cloak",
            String.class,
            String.class,
            String.class);

    public static final Method<String, String, String> BuyHat = new Method<>("buy_hat",
            String.class,
            String.class,
            String.class);

    // =========================
    // EMOTE
    // =========================
    public static final Method<Types.EmoteRequest, Void, String> Emote = new Method<>("emote",
            Types.EmoteRequest.class,
            Void.class,
            String.class);

    public static final Method<Types.EmoteEvent, Void, Void> EmoteEvent = new Method<>("emote_event",
            Types.EmoteEvent.class,
            Void.class,
            Void.class);

    // =========================
    // GET PLAYER
    // =========================
    public static final Method<String, Types.Player, String> GetPlayer = new Method<>("get_player",
            String.class,
            Types.Player.class,
            String.class);

    // =========================
    // PLAYER (Server Notification)
    // =========================
    public static final Method<Types.PlayerStream, Void, Void> Player = new Method<>("player",
            Types.PlayerStream.class,
            Void.class,
            Void.class);

    // =========================
    // SEND PLAYER
    // =========================
    public static final Method<SendPlayerRequest, Void, String> SendPlayer = new Method<>("send_player",
            SendPlayerRequest.class,
            Void.class,
            String.class);
}