package org.saturnclient.impl.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;

import org.saturnclient.common.module.PlayerModule;
import org.saturnclient.common.ref.asset.SpriteRef;
import org.saturnclient.common.ref.game.ItemStackRef;
import org.saturnclient.feature.features.StatusEffectsFeature;
import org.saturnclient.saturnclient.SaturnClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fabric implementation of {@link PlayerModule}.
 *
 * All access to {@link MinecraftClient} and {@link ClientPlayerEntity}
 * is encapsulated here. Features only ever see the {@link PlayerModule}
 * interface; they never import Minecraft classes directly.
 *
 * Every method guards against a missing player and returns a safe
 * default so features are free of null checks.
 */
public class PlayerModuleFabric implements PlayerModule {

    private final MinecraftClient mc;

    public PlayerModuleFabric(MinecraftClient mc) {
        this.mc = mc;
    }

    // ---------------------------------------------------------------
    // Internal helper — never returns null
    // ---------------------------------------------------------------

    /** Returns the local player, or {@code null} if one is not loaded. */
    private ClientPlayerEntity player() {
        return mc.player;
    }

    // ---------------------------------------------------------------
    // Presence
    // ---------------------------------------------------------------

    @Override
    public boolean hasPlayer() {
        return mc.player != null;
    }

    // ---------------------------------------------------------------
    // Position
    // ---------------------------------------------------------------

    @Override
    public int getX() {
        return hasPlayer() ? (int) player().getX() : 0;
    }

    @Override
    public int getY() {
        return hasPlayer() ? (int) player().getY() : 0;
    }

    @Override
    public int getZ() {
        return hasPlayer() ? (int) player().getZ() : 0;
    }

    // ---------------------------------------------------------------
    // Input / movement
    // ---------------------------------------------------------------

    @Override
    public boolean isForwardPressed() {
        return mc.options.forwardKey.isPressed();
    }

    @Override
    public boolean isBackPressed() {
        return mc.options.backKey.isPressed();
    }

    @Override
    public boolean isLeftPressed() {
        return mc.options.leftKey.isPressed();
    }

    @Override
    public boolean isRightPressed() {
        return mc.options.rightKey.isPressed();
    }

    @Override
    public boolean isJumpPressed() {
        return mc.options.jumpKey.isPressed();
    }

    @Override
    public boolean isAttackPressed() {
        return mc.options.attackKey.isPressed();
    }

    @Override
    public boolean isUsePressed() {
        return mc.options.useKey.isPressed();
    }

    @Override
    public boolean isSneaking() {
        return hasPlayer() && player().isSneaking();
    }

    @Override
    public boolean isUsingItem() {
        return hasPlayer() && player().isUsingItem();
    }

    @Override
    public boolean hasHorizontalCollision() {
        return hasPlayer() && player().horizontalCollision;
    }

    @Override
    public boolean isOnGround() {
        return hasPlayer() && player().isOnGround();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        if (hasPlayer())
            player().setSprinting(sprinting);
    }

    // ---------------------------------------------------------------
    // Velocity
    // ---------------------------------------------------------------

    @Override
    public Velocity getVelocity() {
        if (!hasPlayer())
            return Velocity.ZERO;
        var v = player().getVelocity();
        return new Velocity(v.x, v.y, v.z);
    }

    // ---------------------------------------------------------------
    // Stats
    // ---------------------------------------------------------------

    @Override
    public float getHealth() {
        return hasPlayer() ? player().getHealth() : 0f;
    }

    // ---------------------------------------------------------------
    // Equipment
    // ---------------------------------------------------------------

    @Override
    public ItemStackRef getMainHand() {
        return wrap(hasPlayer() ? player().getMainHandStack() : ItemStack.EMPTY);
    }

    @Override
    public ItemStackRef getHelmet() {
        return wrap(hasPlayer() ? player().getInventory().armor.get(3) : ItemStack.EMPTY);
    }

    @Override
    public ItemStackRef getChestplate() {
        return wrap(hasPlayer() ? player().getInventory().armor.get(2) : ItemStack.EMPTY);
    }

    @Override
    public ItemStackRef getLeggings() {
        return wrap(hasPlayer() ? player().getInventory().armor.get(1) : ItemStack.EMPTY);
    }

    @Override
    public ItemStackRef getBoots() {
        return wrap(hasPlayer() ? player().getInventory().armor.get(0) : ItemStack.EMPTY);
    }

    // ---------------------------------------------------------------
    // Dummy equipment
    // ---------------------------------------------------------------

    @Override
    public ItemStackRef getDummyMainHand() {
        return wrap(hasPlayer() ? player().getInventory().armor.get(3) : ItemStack.EMPTY);
    }

    @Override
    public ItemStackRef getDummyHelmet() {
        return wrap(hasPlayer() ? player().getInventory().armor.get(3) : ItemStack.EMPTY);
    }

    @Override
    public ItemStackRef getDummyChestplate() {
        return wrap(hasPlayer() ? player().getInventory().armor.get(2) : ItemStack.EMPTY);
    }

    @Override
    public ItemStackRef getDummyLeggings() {
        return wrap(hasPlayer() ? player().getInventory().armor.get(1) : ItemStack.EMPTY);
    }

    @Override
    public ItemStackRef getDummyBoots() {
        return wrap(hasPlayer() ? player().getInventory().armor.get(0) : ItemStack.EMPTY);
    }

    // ---------------------------------------------------------------
    // Status effects
    // ---------------------------------------------------------------

    @Override
    public List<? extends StatusEffectsFeature.EffectView> getActiveEffects() {
        if (!hasPlayer())
            return Collections.emptyList();

        var effects = player().getActiveStatusEffects().values();
        List<FabricEffectView> result = new ArrayList<>(effects.size());
        for (StatusEffectInstance instance : effects) {
            result.add(new FabricEffectView(instance));
        }
        return result;
    }

    @Override
    public List<? extends StatusEffectsFeature.EffectView> getDummyEffects() {
        if (!hasPlayer())
            return Collections.emptyList();

        var effects = player().getActiveStatusEffects().values();
        List<FabricEffectView> result = new ArrayList<>(effects.size());
        for (StatusEffectInstance instance : effects) {
            result.add(new FabricEffectView(instance));
        }
        return result;
    }

    // ---------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------

    private static ItemStackRef wrap(ItemStack stack) {
        return (ItemStackRef) (Object) stack;
    }

    // ---------------------------------------------------------------
    // Inner — EffectView adapter
    // ---------------------------------------------------------------

    /**
     * Bridges a Fabric {@link StatusEffectInstance} to the
     * platform-neutral {@link StatusEffectsFeature.EffectView}.
     */
    public static final class FabricEffectView implements StatusEffectsFeature.EffectView {

        private final StatusEffectInstance instance;

        public FabricEffectView(StatusEffectInstance instance) {
            this.instance = instance;
        }

        @Override
        public boolean shouldShowIcon() {
            return instance.shouldShowIcon();
        }

        /**
         * Returns the Minecraft sprite identifier for this effect's icon.
         * The platform-specific renderer knows how to draw a
         * {@link net.minecraft.util.Identifier}.
         */
        @Override
        public SpriteRef getIcon() {
            return (SpriteRef) SaturnClient.client.getStatusEffectSpriteManager().getSprite(instance.getEffectType());
        }

        @Override
        public boolean isInfinite() {
            return instance.isInfinite();
        }

        @Override
        public String getInfiniteText() {
            return "∞";
        }

        @Override
        public int getDurationSeconds() {
            return instance.getDuration() / 20; // ticks → seconds
        }
    }
}
