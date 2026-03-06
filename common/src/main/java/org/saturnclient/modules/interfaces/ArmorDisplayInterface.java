package org.saturnclient.modules.interfaces;

import org.saturnclient.common.minecraft.bindings.SaturnItemStack;

public interface ArmorDisplayInterface {

    SaturnItemStack getMainHand();

    SaturnItemStack getHelmet();

    SaturnItemStack getChestplate();

    SaturnItemStack getLeggings();

    SaturnItemStack getBoots();

    SaturnItemStack getDummyMainHand();

    SaturnItemStack getDummyHelmet();

    SaturnItemStack getDummyChestplate();

    SaturnItemStack getDummyLeggings();

    SaturnItemStack getDummyBoots();

    boolean isEmpty(SaturnItemStack stack);

    int getMaxDamage(SaturnItemStack stack);

    int getDamage(SaturnItemStack stack);
}