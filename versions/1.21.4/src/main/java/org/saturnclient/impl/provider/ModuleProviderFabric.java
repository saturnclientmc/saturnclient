package org.saturnclient.impl.provider;

import net.minecraft.client.MinecraftClient;

import org.saturnclient.common.provider.ModuleProvider;
import org.saturnclient.impl.modules.entity.EntityModuleFabric;
import org.saturnclient.impl.modules.network.NetworkModuleFabric;
import org.saturnclient.impl.modules.player.PlayerModuleFabric;
import org.saturnclient.impl.modules.render.RenderModuleFabric;
import org.saturnclient.impl.modules.world.WorldModuleFabric;
import org.saturnclient.common.module.*;

/**
 * Fabric implementation of {@link ModuleProvider}.
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
public class ModuleProviderFabric implements ModuleProvider {

    private final PlayerModule player;
    private final WorldModule world;
    private final EntityModule entity;
    private final RenderModule render;
    private final NetworkModule network;

    public ModuleProviderFabric(MinecraftClient mc) {
        this.player = new PlayerModuleFabric(mc);
        this.world = new WorldModuleFabric(mc);
        this.entity = new EntityModuleFabric(mc);
        this.render = new RenderModuleFabric(mc);
        this.network = new NetworkModuleFabric(mc);
    }

    @Override
    public PlayerModule player() {
        return player;
    }

    @Override
    public WorldModule world() {
        return world;
    }

    @Override
    public EntityModule entity() {
        return entity;
    }

    @Override
    public RenderModule render() {
        return render;
    }

    @Override
    public NetworkModule network() {
        return network;
    }
}
