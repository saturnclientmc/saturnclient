package org.saturnclient.impl.provider;

import net.minecraft.client.MinecraftClient;

import org.saturnclient.common.feature.*;
import org.saturnclient.common.provider.FeatureProvider;
import org.saturnclient.impl.modules.entity.EntityModuleFabric;
import org.saturnclient.impl.modules.network.NetworkModuleFabric;
import org.saturnclient.impl.modules.player.PlayerModuleFabric;
import org.saturnclient.impl.modules.render.RenderModuleFabric;
import org.saturnclient.impl.modules.world.WorldModuleFabric;

/**
 * Fabric implementation of {@link FeatureProvider}.
 *
 * Instantiated once during client startup (e.g. in the mod initialiser)
 * and passed to every feature constructor. All five sub-modules share
 * the same {@link MinecraftClient} reference so there is no
 * inconsistency between modules reading state within the same tick.
 *
 * <pre>{@code
 * ModuleProvider provider = new ModuleProviderFabric(MinecraftClient.getInstance());
 * FeatureManager.register(new AutoSprintFeature(provider));
 * FeatureManager.register(new CoordinatesFeature(provider));
 * // …
 * }</pre>
 */
public class ModuleProviderFabric implements FeatureProvider {

    private final PlayerFeature player;
    private final WorldFeature world;
    private final EntityFeature entity;
    private final RenderFeature render;
    private final NetworkFeature network;

    public ModuleProviderFabric(MinecraftClient mc) {
        this.player = new PlayerModuleFabric(mc);
        this.world = new WorldModuleFabric(mc);
        this.entity = new EntityModuleFabric(mc);
        this.render = new RenderModuleFabric(mc);
        this.network = new NetworkModuleFabric(mc);
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
