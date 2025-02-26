package org.auraclient.auraclient.menus;

import org.auraclient.auraclient.AuraClient;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CloakSelector extends Screen {
    public CloakSelector() {
        super(Text.literal("Cloaks"));
    }

    @Override
    protected void init() {
        int y = ((this.height - (35 * (AuraClient.list.size()))) / 2);

        for (String cloak : AuraClient.list) {
            addDrawableChild(ButtonWidget.builder(Text.literal(cloak), (__) -> {
                AuraClient.cape = cloak;
            }).dimensions((this.width - 50) / 2, y, 50, 30).build());

            y += 35;
        }
    }
}
