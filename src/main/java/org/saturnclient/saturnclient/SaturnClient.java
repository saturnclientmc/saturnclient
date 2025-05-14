package org.saturnclient.saturnclient;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = SaturnClient.MODID, version = SaturnClient.VERSION)
public class SaturnClient
{
    public static final String MODID = "saturnclient";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
        Keybindings.init();
        MinecraftForge.EVENT_BUS.register(new TickHandler());
    }

    public static class MyGuiScreen extends GuiScreen {
        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            this.fontRendererObj.drawString("Hello from GUI!", this.width / 2 - 50, this.height / 2, 0xFFFFFF);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        public boolean doesGuiPauseGame() {
            return false;
        }
    }
}
