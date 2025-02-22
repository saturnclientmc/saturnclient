package org.auraclient.auraclient.menus;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.auraclient.auraclient.AuraClient;
import org.auraclient.auraclient.ui.AuraUi;
import org.auraclient.auraclient.ui.Button;

public class MainMenu extends AuraUi {
    public MainMenu() {
        super(Text.of("Main Menu"));
    }

    @Override
    public void ui(DrawContext context) {
        widgets.add(new Button("Cloaks", (context.getScaledWindowWidth() - 50) / 2, (context.getScaledWindowHeight() - 20) / 2, 50, 20, () -> {
            if (AuraClient.list.indexOf(AuraClient.cape) + 1 == AuraClient.list.size()) {
                AuraClient.cape = AuraClient.list.get(0);
                AuraClient.capeCacheIdentifier = null;
            } else {
                AuraClient.cape = AuraClient.list.get(AuraClient.list.indexOf(AuraClient.cape) + 1);
            };
        }));
    }
}
