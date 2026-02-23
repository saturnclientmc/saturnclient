package org.saturnclient.saturnclient.client;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.saturnclient.saturnclient.SaturnClient;
import org.saturnclient.saturnclient.client.player.SaturnPlayer;
import org.saturnclient.saturnclient.cosmetics.Hats;
import org.saturnclient.saturnclient.cosmetics.cloaks.Cloaks;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import dev.selimaj.session.Session;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;

public class ServiceClient {
    private static Session session;
    public static UUID uuid;

    public static boolean connectTimeout() {
        try {
            session = Session.connect("ws://127.0.0.1:8080", 10, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean authenticate() {
        ClientLifecycleEvents.CLIENT_STOPPING.register(_o -> {
            if (session != null)
                try {
                    session.close();
                } catch (Exception e) {
                    SaturnClient.LOGGER.error("Unable to close Saturn session");

                }
        });

        try {
            var mcSession = SaturnClient.client.getSession();
            if (mcSession == null) {
                SaturnClient.LOGGER.error("No active Minecraft session found");
                return false;
            }

            String accessToken = mcSession.getAccessToken();
            uuid = mcSession.getUuidOrNull();
            String username = mcSession.getUsername();

            SaturnClient.LOGGER.info("Authenticating with UUID: " + uuid);

            if (!connectTimeout()) {
                SaturnClient.LOGGER.error("Unable to authenticate: Session Server Timeout");
                return false;
            }

            ServiceMethods.Types.Player response = session.request(ServiceMethods.Authenticate, accessToken)
                    .get();

            for (String availableCloak : response.cloaks()) {
                Cloaks.availableCloaks.add(availableCloak);
            }

            for (String availableHat : response.hats()) {
                Hats.availableHats.add(availableHat);
            }

            eventHandlers();

            SaturnPlayer.set(new SaturnPlayer(uuid, username, response.cloak(), response.hat()));

            Cloaks.loadCloak(uuid);

            return true;
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Authentication failed", e);
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
            SaturnClient.LOGGER.error("Failed to set cloak (service): ", e);
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
            SaturnClient.LOGGER.error("Failed to set hat (service): ", e);
        }
    }

    public static void buyCloak(String itemId) {
        try {
            session.request(ServiceMethods.BuyCloak, itemId).whenComplete((msg, throwable) -> {
                if (throwable != null) {
                    throwable.printStackTrace();
                } else {
                    Cloaks.availableCloaks.add(itemId);
                }
            });
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Failed to buy cloak (service): ", e);
        }
    }

    public static void buyHat(String itemId) {
        try {
            session.request(ServiceMethods.BuyHat, itemId).whenComplete((msg, throwable) -> {
                if (throwable != null) {
                    throwable.printStackTrace();
                } else {
                    Hats.availableHats.add(itemId);
                }
            });
        } catch (Exception e) {
            SaturnClient.LOGGER.error("Failed to buy hat (service): ", e);
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
            SaturnClient.LOGGER.error("Failed to emote (service): ", e);
        }
    }

    public static void eventHandlers() {
        session.onNotification(ServiceMethods.EmoteEvent, (data) -> {
            for (AbstractClientPlayerEntity player : SaturnClient.client.world.getPlayers()) {
                if (player.getUuidAsString().equals(data.from())) {
                    AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer(player);
                    if (data.emote() != null && !data.emote().isEmpty()) {
                        if (animationStack.isActive() && animationStack.getPriority() == 1000) {
                            animationStack.removeLayer(1000);
                        }
                        animationStack.addAnimLayer(1000,
                                PlayerAnimationRegistry
                                        .getAnimation(Identifier.of("saturnclient", data.emote()))
                                        .playAnimation());
                    } else {
                        animationStack.removeLayer(1000);
                    }
                }
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
            SaturnClient.LOGGER.error("Failed to get player", e);
        }
    }
}
