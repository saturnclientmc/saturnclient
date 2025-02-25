package org.auraclient.auraclient.menus;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.auraclient.auraclient.AuraClient;
import org.auraclient.auraclient.ui.AuraUi;
import org.auraclient.auraclient.ui.AuraTexture;
import net.minecraft.util.Identifier;
import org.auraclient.auraclient.ui.AuraWidget;

public class CloakSelector extends AuraUi {
    public CloakSelector() {
        super(Text.of("UI"));
    }

    @Override
    public void ui(DrawContext context) {
        int width = (32 * (AuraClient.list.size() + 1));
        int x = (context.getScaledWindowWidth() - (width - 8)) / 2;
        int y = (context.getScaledWindowHeight() - 92) / 2;

        widgets.add(new AuraWidget(AuraTexture.BOX, x - 8, y - 8, width, 108));

        for (int i = 0; i < AuraClient.list.size(); i++) {
            String cloak = AuraClient.list.get(i);

            widgets.add(new AuraWidget(Identifier.of("auraclient", "textures/gui/cloaks/"+cloak.replaceAll("\\.(gif|png)", "")+".prev.png"), x, y, 32, 92, () -> {
                AuraClient.cape = cloak;
            }));

            x += 40;
        }
    }
}
