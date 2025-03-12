package org.saturnclient.saturnclient.menus;

import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.widgets.SaturnImageButton;
import org.saturnclient.ui.widgets.SaturnInputBox;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SettingsMenu extends SaturnUi {
    public static Identifier searchTexture = Identifier.of("saturnclient", "textures/gui/components/search.png");
    public static Identifier closeTexture = Identifier.of("saturnclient", "textures/gui/components/close.png");
    public SaturnImageButton searchButton;
    public SaturnInputBox searchInputBox;

    public SettingsMenu() {
        super(Text.literal("Settings"));
    }

    @Override
    protected void init() {
        searchInputBox = new SaturnInputBox("Search", 10, 10, 100, 20);
        searchInputBox.visible = false;
        draw(searchInputBox);

        searchButton = (SaturnImageButton) new SaturnImageButton(searchTexture, 10,
                10,
                () -> {
                    searchInputBox.visible = !searchInputBox.visible;
                    if (searchInputBox.visible) {
                        searchButton.sprite = closeTexture;
                    } else {
                        searchInputBox.clearText();
                        searchButton.sprite = searchTexture;
                    }
                }).setWidth(30).setHeight(30).setX(30).setY(30);

        draw(searchButton);
    }
}