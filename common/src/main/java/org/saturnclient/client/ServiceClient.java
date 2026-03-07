package org.saturnclient.client;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.saturnclient.client.player.SaturnPlayer;
import org.saturnclient.common.minecraft.bindings.SaturnClientBindings;

import dev.selimaj.session.Session;

public class ServiceClient {
    private static Session session;
    public static UUID uuid;

    public static final List<String> availableCloaks = new ArrayList<>();
    public static final List<String> availableHats = new ArrayList<>();

    public static boolean connectTimeout() {
        try {
            session = Session.connect("wss://saturn-server.selimaj.dev", 10, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean authenticate() {
        SaturnClientBindings.platform().onClientStopping(() -> {
            if (session != null) {
                try {
                    session.close();
                } catch (Exception e) {
                    SaturnClientBindings.platform().logError("Unable to close Saturn session", e);
                }
            }
        });

        try {
            String accessToken = SaturnClientBindings.platform().getAccessToken();
            uuid = SaturnClientBindings.platform().getUuid();
            String username = SaturnClientBindings.platform().getUsername();

            if (accessToken == null || uuid == null || username == null) {
                SaturnClientBindings.platform().logError("No active Minecraft session found");
                return false;
            }

            SaturnClientBindings.platform().logInfo("Authenticating with UUID: " + uuid);

            if (!connectTimeout()) {
                SaturnClientBindings.platform().logError("Unable to authenticate: Session Server Timeout");
                return false;
            }

            ServiceMethods.Types.Player response = session.request(ServiceMethods.Authenticate, accessToken)
                    .get();

            for (String availableCloak : response.cloaks()) {
                availableCloaks.add(availableCloak);
            }

            for (String availableHat : response.hats()) {
                availableHats.add(availableHat);
            }

            eventHandlers();

            SaturnPlayer.set(new SaturnPlayer(uuid, username, response.cloak(), response.hat()));

            return true;
        } catch (Exception e) {
            SaturnClientBindings.platform().logError("Authentication failed", e);
            return false;
        }
    }

    public static void setCloak(String itemId) {
        try {
            session.request(ServiceMethods.SetCloak, itemId).whenComplete((msg, throwable) -> {
                if (throwable != null) {
                    throwable.printStackTrace();
                } else {
                    try {
                        session.request(ServiceMethods.SendPlayer,
                                new ServiceMethods.Types.SendPlayerRequest(SaturnPlayer.getExternalUUIDAsString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            SaturnClientBindings.platform().logError("Failed to set cloak (service): ", e);
        }
    }

    public static void setHat(String itemId) {
        try {
            session.request(ServiceMethods.SetHat, itemId).whenComplete((msg, throwable) -> {
                if (throwable != null) {
                    throwable.printStackTrace();
                } else {
                    try {
                        session.request(ServiceMethods.SendPlayer,
                                new ServiceMethods.Types.SendPlayerRequest(SaturnPlayer.getExternalUUIDAsString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            SaturnClientBindings.platform().logError("Failed to set hat (service): ", e);
        }
    }

    public static void buyCloak(String itemId) {
        try {
            session.request(ServiceMethods.BuyCloak, itemId).whenComplete((msg, throwable) -> {
                if (throwable != null) {
                    throwable.printStackTrace();
                } else {
                    availableCloaks.add(itemId);
                }
            });
        } catch (Exception e) {
            SaturnClientBindings.platform().logError("Failed to buy cloak (service): ", e);
        }
    }

    public static void buyHat(String itemId) {
        try {
            session.request(ServiceMethods.BuyHat, itemId).whenComplete((msg, throwable) -> {
                if (throwable != null) {
                    throwable.printStackTrace();
                } else {
                    availableHats.add(itemId);
                }
            });
        } catch (Exception e) {
            SaturnClientBindings.platform().logError("Failed to buy hat (service): ", e);
        }
    }

    public static void emote(String emote) {
        try {
            session.request(ServiceMethods.Emote,
                    new ServiceMethods.Types.EmoteRequest(emote, SaturnPlayer.getExternalUUIDAsString()))
                    .whenComplete((msg, throwable) -> {
                        if (throwable != null) {
                            throwable.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            SaturnClientBindings.platform().logError("Failed to emote (service): ", e);
        }
    }

    public static void eventHandlers() {
        session.onNotification(ServiceMethods.EmoteEvent, (data) -> {
            if (data == null) {
                return;
            }
            UUID from;
            try {
                from = UUID.fromString(data.from());
            } catch (Exception e) {
                return;
            }

            if (data.emote() != null && !data.emote().isEmpty()) {
                SaturnClientBindings.emotes().setEmote(from, data.emote());
            } else {
                SaturnClientBindings.emotes().setEmote(from, null);
            }
        });

        session.onNotification(ServiceMethods.Player, (player) -> {
            System.out.println(player);
            SaturnPlayer.set(player.toSaturnPlayer());
        });
    }

    public static void player(UUID uuid, String name) {
        try {
            session.request(ServiceMethods.GetPlayer, uuid.toString())
                    .whenComplete((user, throwable) -> {
                        if (throwable != null) {
                            throwable.printStackTrace();
                        } else if (user != null) {
                            SaturnPlayer.set(user.toSaturnPlayer(uuid, name));
                        }
                    });
        } catch (Exception e) {
            SaturnClientBindings.platform().logError("Failed to get player", e);
        }
    }
}
