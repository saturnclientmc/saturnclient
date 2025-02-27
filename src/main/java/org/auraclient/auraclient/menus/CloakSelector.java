package org.auraclient.auraclient.menus;

import org.auraclient.auraclient.AuraClient;
import org.auraclient.auraclient.cloaks.Cloaks;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class CloakSelector extends Screen {
    private TextFieldWidget playerNameField;
    
    public CloakSelector() {
        super(Text.literal("Cloaks"));
    }

    @Override
    protected void init() {
        // Add player name text field at the top
        this.playerNameField = new TextFieldWidget(this.textRenderer, 
            (this.width - 200) / 2, 20, 200, 20, Text.literal("Player Name"));
        this.playerNameField.setMaxLength(16);
        addDrawableChild(this.playerNameField);

        // Calculate starting Y position for cloak buttons, accounting for text field
        int y = ((this.height - (35 * (Cloaks.availableCapes.size()))) / 2) + 30;

        for (String cloak : Cloaks.availableCapes) {
            addDrawableChild(ButtonWidget.builder(Text.literal(cloak), (__) -> {
                String playerName = this.playerNameField.getText();
                if (!playerName.isEmpty() && !playerName.equals("Player Name")) {
                    if (cloak.isEmpty()) {
                        Cloaks.playerCapes.remove(playerName);
                        AuraClient.LOGGER.info("Cape removed for player: " + playerName);
                    } else {
                        Cloaks.playerCapes.put(playerName, cloak);
                        AuraClient.LOGGER.info("Cape set for player: " + playerName + " to " + cloak);
                    }
                }
            }).dimensions((this.width - 50) / 2, y, 50, 30).build());

            y += 35;
        }
    }
}
