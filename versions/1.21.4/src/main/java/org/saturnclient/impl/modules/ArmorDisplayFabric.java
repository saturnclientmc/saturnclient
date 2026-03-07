package org.saturnclient.impl.modules;

import org.saturnclient.common.ref.game.ItemStackRef;
import org.saturnclient.modules.interfaces.ArmorDisplayInterface;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ArmorDisplayFabric implements ArmorDisplayInterface {

    private ItemStack get(EquipmentSlot slot) {
        return MinecraftClient.getInstance().player.getEquippedStack(slot);
    }

    @Override
    public ItemStackRef getMainHand() {
        return new ItemStackRef(get(EquipmentSlot.MAINHAND));
    }

    @Override
    public ItemStackRef getHelmet() {
        return new ItemStackRef(get(EquipmentSlot.HEAD));
    }

    @Override
    public ItemStackRef getChestplate() {
        return new ItemStackRef(get(EquipmentSlot.CHEST));
    }

    @Override
    public ItemStackRef getLeggings() {
        return new ItemStackRef(get(EquipmentSlot.LEGS));
    }

    @Override
    public ItemStackRef getBoots() {
        return new ItemStackRef(get(EquipmentSlot.FEET));
    }

    @Override
    public boolean isEmpty(ItemStackRef stack) {
        return ((ItemStack) stack.get()).isEmpty();
    }

    @Override
    public int getMaxDamage(ItemStackRef stack) {
        return ((ItemStack) stack.get()).getMaxDamage();
    }

    @Override
    public int getDamage(ItemStackRef stack) {
        return ((ItemStack) stack.get()).getDamage();
    }

    // Dummy items

    @Override
    public ItemStackRef getDummyMainHand() {
        return new ItemStackRef(new ItemStack(Items.DIAMOND_SWORD));
    }

    @Override
    public ItemStackRef getDummyHelmet() {
        return new ItemStackRef(new ItemStack(Items.DIAMOND_HELMET));
    }

    @Override
    public ItemStackRef getDummyChestplate() {
        return new ItemStackRef(new ItemStack(Items.DIAMOND_CHESTPLATE));
    }

    @Override
    public ItemStackRef getDummyLeggings() {
        return new ItemStackRef(new ItemStack(Items.DIAMOND_LEGGINGS));
    }

    @Override
    public ItemStackRef getDummyBoots() {
        return new ItemStackRef(new ItemStack(Items.DIAMOND_BOOTS));
    }
}