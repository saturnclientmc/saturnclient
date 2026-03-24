package org.saturnclient.impl.modules.player;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.entity.EquipmentSlot;

import org.saturnclient.common.feature.PlayerFeature;
import org.saturnclient.common.ref.game.EffectRef;
import org.saturnclient.common.ref.game.ItemStackRef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fabric implementation of {@link PlayerFeature}.
 *
 * All access to {@link MinecraftClient} and {@link ClientPlayerEntity}
 * is encapsulated here. Features only ever see the {@link PlayerFeature}
 * interface; they never import Minecraft classes directly.
 *
 * Every method guards against a missing player and returns a safe
 * default so features are free of null checks.
 */
public class PlayerModuleFabric implements PlayerFeature {

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
        return wrap(hasPlayer() ? player().getEquippedStack(EquipmentSlot.HEAD) : ItemStack.EMPTY);
    }

    @Override
    public ItemStackRef getChestplate() {
        return wrap(hasPlayer() ? player().getEquippedStack(EquipmentSlot.CHEST) : ItemStack.EMPTY);
    }

    @Override
    public ItemStackRef getLeggings() {
        return wrap(hasPlayer() ? player().getEquippedStack(EquipmentSlot.LEGS) : ItemStack.EMPTY);
    }

    @Override
    public ItemStackRef getBoots() {
        return wrap(hasPlayer() ? player().getEquippedStack(EquipmentSlot.FEET) : ItemStack.EMPTY);
    }

    // ---------------------------------------------------------------
    // Dummy equipment
    // ---------------------------------------------------------------

    @Override
    public ItemStackRef getDummyMainHand() {
        return wrap(new ItemStack(Items.DIAMOND_SWORD));
    }

    @Override
    public ItemStackRef getDummyHelmet() {
        return wrap(new ItemStack(Items.DIAMOND_HELMET));
    }

    @Override
    public ItemStackRef getDummyChestplate() {
        return wrap(new ItemStack(Items.DIAMOND_CHESTPLATE));
    }

    @Override
    public ItemStackRef getDummyLeggings() {
        return wrap(new ItemStack(Items.DIAMOND_LEGGINGS));
    }

    @Override
    public ItemStackRef getDummyBoots() {
        return wrap(new ItemStack(Items.DIAMOND_BOOTS));
    }

    // ---------------------------------------------------------------
    // Status effects
    // ---------------------------------------------------------------

    @Override
    public List<? extends EffectRef> getActiveEffects() {
        if (!hasPlayer())
            return Collections.emptyList();

        var effects = player().getActiveStatusEffects().values();
        List<EffectRef> result = new ArrayList<>(effects.size());
        for (StatusEffectInstance instance : effects) {
            result.add((EffectRef) instance);
        }
        return result;
    }

    @Override
    public List<? extends EffectRef> getDummyEffects() {
        return List.of(
                (EffectRef) new StatusEffectInstance(
                        Registries.STATUS_EFFECT.getEntry(Identifier.ofVanilla("speed")).get(), 12000, 2),
                (EffectRef) new StatusEffectInstance(
                        Registries.STATUS_EFFECT.getEntry(Identifier.ofVanilla("strength")).get(), 12000, 2),
                (EffectRef) new StatusEffectInstance(
                        Registries.STATUS_EFFECT.getEntry(Identifier.ofVanilla("fire_resistance")).get(), 12000, 2));
    }

    // ---------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------

    private static ItemStackRef wrap(ItemStack stack) {
        return (ItemStackRef) (Object) stack;
    }
}
