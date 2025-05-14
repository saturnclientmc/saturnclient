package com.example.examplemod;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickHandler {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        // Only run during the actual game (not in menus/loading screens)
        if (event.phase == TickEvent.Phase.END) {
            System.out.println("Tick!");
            Minecraft mc = Minecraft.getMinecraft();

            if (Keybindings.openGuiKey.isPressed()) {
                mc.displayGuiScreen(new ExampleMod.MyGuiScreen());
            }
        }
    }
}
