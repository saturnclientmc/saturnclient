package org.auraclient.auraclient.menus;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.auraclient.auraclient.AuraClient;
import org.auraclient.auraclient.ui.AuraUi;
import org.auraclient.auraclient.ui.Box;
import org.auraclient.auraclient.ui.ImageButton;
import net.minecraft.util.Identifier;

public class CloakSelector extends AuraUi {
    public CloakSelector() {
        super(Text.of("UI"));
    }

    @Override
    public void ui(DrawContext context) {
        int y = (context.getScaledWindowHeight() - 92) / 2;
        int x = (context.getScaledWindowWidth() - ((32 * AuraClient.list.size()) - 8)) / 2;

        widgets.add(new Box(x - 8, y - 8, (40 * AuraClient.list.size() + 8), 108));

        for (int i = 0; i < AuraClient.list.size(); i++) {
            String cloak = AuraClient.list.get(i);
            ImageButton img = new ImageButton(Identifier.of("auraclient", "textures/gui/cloaks/"+cloak.replaceAll("\\.(gif|png)", "")+".prev.png"), x, y, 32, 92);
            img.onClick = () -> {
                AuraClient.cape = cloak;
            };
            widgets.add(img);

            x += 40;
        }
    }
}
