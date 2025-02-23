package org.auraclient.auraclient.menus;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.auraclient.auraclient.ui.AuraUi;
import org.auraclient.auraclient.ui.Button;

public class MainMenu extends AuraUi {
    public MainMenu() {
        super(Text.of("Main Menu"));
    }

    @Override
    public void ui(DrawContext context) {
        widgets.add(new Button("Cloaks", (context.getScaledWindowWidth() - 50) / 2, (context.getScaledWindowHeight() - 20) / 2, 50, 20, () -> {
            client.setScreen(new CloakSelector());
        }));
    }
}
