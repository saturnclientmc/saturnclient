package org.auraclient.auraclient.menus;

import org.auraclient.auraclient.cloaks.Cloaks;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CloakSelector extends Screen {
    public CloakSelector() {
        super(Text.literal("Cloaks"));
    }

    @Override
    protected void init() {
        int y = ((this.height - (35 * (Cloaks.availableCloaks.size()))) / 2) + 30;

        for (String cloak : Cloaks.availableCloaks) {
            addDrawableChild(ButtonWidget.builder(Text.literal(cloak), (__) -> {
                String uuid = client.player.getUuidAsString().replace("-", "");
                Cloaks.setCape(uuid, cloak);
                // Cloaks.playerCapes.put(uuid, cloak);
                // AuraApi.setCloak(cloak);
            }).dimensions((this.width - 50) / 2, y, 50, 30).build());

            y += 35;
        }
    }
}
