package org.saturnclient.impl.modules;

import org.saturnclient.common.ref.game.ItemStackRef;
import org.saturnclient.feature.features.featuresinterfaces.ArmorDisplayInterface;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ArmorDisplayFabric implements ArmorDisplayInterface {

    private ItemStackRef get(EquipmentSlot slot) {
        return (ItemStackRef) (Object) MinecraftClient.getInstance().player.getEquippedStack(slot);
    }

    @Override
    public ItemStackRef getMainHand() {
        return get(EquipmentSlot.MAINHAND);
    }

    @Override
    public ItemStackRef getHelmet() {
        return get(EquipmentSlot.HEAD);
    }

    @Override
    public ItemStackRef getChestplate() {
        return get(EquipmentSlot.CHEST);
    }

    @Override
    public ItemStackRef getLeggings() {
        return get(EquipmentSlot.LEGS);
    }

    @Override
    public ItemStackRef getBoots() {
        return get(EquipmentSlot.FEET);
    }

    // Dummy items

    @Override
    public ItemStackRef getDummyMainHand() {
        return (ItemStackRef) (Object) new ItemStack(Items.DIAMOND_SWORD);
    }

    @Override
    public ItemStackRef getDummyHelmet() {
        return (ItemStackRef) (Object) new ItemStack(Items.DIAMOND_HELMET);
    }

    @Override
    public ItemStackRef getDummyChestplate() {
        return (ItemStackRef) (Object) new ItemStack(Items.DIAMOND_CHESTPLATE);
    }

    @Override
    public ItemStackRef getDummyLeggings() {
        return (ItemStackRef) (Object) new ItemStack(Items.DIAMOND_LEGGINGS);
    }

    @Override
    public ItemStackRef getDummyBoots() {
        return (ItemStackRef) (Object) new ItemStack(Items.DIAMOND_BOOTS);
    }
}