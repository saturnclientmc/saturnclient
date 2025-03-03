package org.auraclient.auraclient.menus;

import org.auraclient.auraclient.cloaks.Cloaks;
import org.auraclient.auraclient.widgets.AuraButton;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class CloakSelector extends Screen {
    public CloakSelector() {
        super(Text.literal("Cloaks"));
    }

    @Override
    protected void init() {
        int y = ((this.height - (35 * (Cloaks.availableCloaks.size()))) / 2);

        for (String cloak : Cloaks.availableCloaks) {
            addDrawableChild(new AuraButton(Text.literal(cloak), (__) -> {
                String uuid = client.player.getUuidAsString().replace("-", "");
                Cloaks.setCloak(uuid, cloak);
            }, (this.width - 50) / 2, y, 50, 30));

            y += 35;
        }
    }
}
