package org.saturnclient.impl.modules;

import org.saturnclient.common.bindings.SaturnItemStack;
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
    public SaturnItemStack getMainHand() {
        return new SaturnItemStack(get(EquipmentSlot.MAINHAND));
    }

    @Override
    public SaturnItemStack getHelmet() {
        return new SaturnItemStack(get(EquipmentSlot.HEAD));
    }

    @Override
    public SaturnItemStack getChestplate() {
        return new SaturnItemStack(get(EquipmentSlot.CHEST));
    }

    @Override
    public SaturnItemStack getLeggings() {
        return new SaturnItemStack(get(EquipmentSlot.LEGS));
    }

    @Override
    public SaturnItemStack getBoots() {
        return new SaturnItemStack(get(EquipmentSlot.FEET));
    }

    @Override
    public boolean isEmpty(SaturnItemStack stack) {
        return ((ItemStack) stack.get()).isEmpty();
    }

    @Override
    public int getMaxDamage(SaturnItemStack stack) {
        return ((ItemStack) stack.get()).getMaxDamage();
    }

    @Override
    public int getDamage(SaturnItemStack stack) {
        return ((ItemStack) stack.get()).getDamage();
    }

    // Dummy items

    @Override
    public SaturnItemStack getDummyMainHand() {
        return new SaturnItemStack(new ItemStack(Items.DIAMOND_SWORD));
    }

    @Override
    public SaturnItemStack getDummyHelmet() {
        return new SaturnItemStack(new ItemStack(Items.DIAMOND_HELMET));
    }

    @Override
    public SaturnItemStack getDummyChestplate() {
        return new SaturnItemStack(new ItemStack(Items.DIAMOND_CHESTPLATE));
    }

    @Override
    public SaturnItemStack getDummyLeggings() {
        return new SaturnItemStack(new ItemStack(Items.DIAMOND_LEGGINGS));
    }

    @Override
    public SaturnItemStack getDummyBoots() {
        return new SaturnItemStack(new ItemStack(Items.DIAMOND_BOOTS));
    }
}