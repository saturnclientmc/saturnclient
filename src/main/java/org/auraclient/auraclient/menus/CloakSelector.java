package org.auraclient.auraclient.menus;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.auraclient.auraclient.AuraClient;
import org.auraclient.auraclient.ui.AuraUi;
import org.auraclient.auraclient.ui.ImageButton;
import net.minecraft.util.Identifier;

public class CloakSelector extends AuraUi {
    public CloakSelector() {
        super(Text.of("UI"));
    }

    @Override
    public void ui(DrawContext context) {
        int padding = 0;
        for (int i = 0; i < AuraClient.list.size(); i++) {
            ImageButton img = new ImageButton(Identifier.of("minecraft", "textures/block/deepslate.png"), (32 * i) + padding, 0, 32, 32);
            img.textureHeight = 16;
            img.textureWidth = 16;
            String cloak = AuraClient.list.get(i);
            img.onClick = () -> {
                AuraClient.cape = cloak;
            };
            widgets.add(img);

            padding += 8;
        }
    }
}
