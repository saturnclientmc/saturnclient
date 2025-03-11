package org.saturnclient.saturnclient.menus;

import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.widgets.SaturnButton;
import org.saturnclient.ui.widgets.SaturnImageButton;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SettingsMenu extends SaturnUi {
    public boolean isOpen = false;
    public static Identifier searchTexture = Identifier.of("saturnclient", "textures/gui/components/search.png");
    public static Identifier closeTexture = Identifier.of("saturnclient", "textures/gui/components/close.png");
    public SaturnImageButton searchButton;

    public SettingsMenu() {
        super(Text.literal("Settings"));
    }

    @Override
    protected void init() {
        searchButton = (SaturnImageButton) new SaturnImageButton(searchTexture, 10,
                10,
                () -> {
                    isOpen = !isOpen;
                    if (isOpen) {
                        searchButton.sprite = closeTexture;
                        draw(new SaturnButton("ABC", () -> {
                        }).setWidth(30).setHeight(30).setX(70).setY(70));
                    } else {
                        searchButton.sprite = searchTexture;
                        widgets.remove(widgets.size() - 1);
                    }
                }).setWidth(30).setHeight(30).setX(30).setY(30);

        draw(searchButton);
    }
}