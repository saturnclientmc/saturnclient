package org.auraclient.auraclient.menus;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.auraclient.auraclient.AuraClient;
import org.auraclient.auraclient.ui.AuraTexture;
import org.auraclient.auraclient.ui.AuraUi;
import org.auraclient.auraclient.ui.AuraWidget;

public class MainMenu extends AuraUi {
    public MainMenu() {
        super(Text.of("Main Menu"));
    }

    @Override
    public void ui(DrawContext context) {
        int x = (context.getScaledWindowWidth() - 30) / 2;
        int y = (context.getScaledWindowHeight() - 14) / 2;

        widgets.add(new AuraWidget(AuraTexture.BOX, x, y, 30, 14, () -> {
            client.setScreen(new CloakSelector());
        }));
    }
}