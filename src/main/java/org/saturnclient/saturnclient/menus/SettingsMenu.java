package org.saturnclient.saturnclient.menus;

import org.saturnclient.ui.SaturnUi;
import org.saturnclient.ui.Textures;
import org.saturnclient.ui.widgets.SaturnImageButton;
import org.saturnclient.ui.widgets.SaturnInputBox;

import net.minecraft.text.Text;

public class SettingsMenu extends SaturnUi {
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

        searchButton = (SaturnImageButton) new SaturnImageButton(Textures.SEARCH, 10,
                10,
                () -> {
                    searchInputBox.visible = !searchInputBox.visible;
                    if (searchInputBox.visible) {
                        searchButton.sprite = Textures.CLOSE;
                    } else {
                        searchInputBox.clearText();
                        searchButton.sprite = Textures.SEARCH;
                    }
                }).setWidth(30).setHeight(30).setX(30).setY(30);

        draw(searchButton);
    }
}