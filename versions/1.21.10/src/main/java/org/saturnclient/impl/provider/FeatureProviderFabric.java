package org.saturnclient.impl.provider;

import net.minecraft.client.MinecraftClient;

import org.saturnclient.common.feature.*;
import org.saturnclient.common.provider.FeatureProvider;
import org.saturnclient.impl.features.entity.EntityFeatureImpl;
import org.saturnclient.impl.features.network.NetworkFeatureImpl;
import org.saturnclient.impl.features.player.PlayerFeatureImpl;
import org.saturnclient.impl.features.render.RenderFeatureImpl;
import org.saturnclient.impl.features.world.WorldFeatureImpl;

/**
 * Fabric implementation of {@link FeatureProvider}.
 *
 * Instantiated once during client startup (e.g. in the mod initialiser)
 * and passed to every feature constructor. All five sub-modules share
 * the same {@link MinecraftClient} reference so there is no
 * inconsistency between modules reading state within the same tick.
 *
 * <pre>{@code
 * FeatureProvider provider = new FeatureProviderFabric(MinecraftClient.getInstance());
 * FeatureManager.register(new AutoSprintFeature(provider));
 * FeatureManager.register(new CoordinatesFeature(provider));
 * // …
 * }</pre>
 */
public class FeatureProviderFabric implements FeatureProvider {

    private final PlayerFeature player;
    private final WorldFeature world;
    private final EntityFeature entity;
    private final RenderFeature render;
    private final NetworkFeature network;

    public FeatureProviderFabric(MinecraftClient mc) {
        this.player = new PlayerFeatureImpl(mc);
        this.world = new WorldFeatureImpl(mc);
        this.entity = new EntityFeatureImpl(mc);
        this.render = new RenderFeatureImpl(mc);
        this.network = new NetworkFeatureImpl(mc);
    }

    @Override
    public PlayerFeature player() {
        return player;
    }

    @Override
    public WorldFeature world() {
        return world;
    }

    @Override
    public EntityFeature entity() {
        return entity;
    }

    @Override
    public RenderFeature render() {
        return render;
    }

    @Override
    public NetworkFeature network() {
        return network;
    }
}
