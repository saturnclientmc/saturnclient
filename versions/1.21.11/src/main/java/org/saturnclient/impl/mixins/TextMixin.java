package org.saturnclient.impl.mixins;

import org.saturnclient.common.ref.asset.IdentifierRef;
import org.saturnclient.common.ref.render.TextRef;
import org.saturnclient.saturnclient.SaturnClient;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.StyleSpriteSource.Font;
import net.minecraft.util.Identifier;

@Mixin(Text.class)
public interface TextMixin extends TextRef {

    @Override
    public default TextRef withFont(IdentifierRef font) {
        return (TextRef) (Text) (((Text) this).copy().setStyle(Style.EMPTY.withFont(new Font((Identifier) (Object) font))));
    }

    @Override
    public default int getWidth() {
        return SaturnClient.client.textRenderer.getWidth((Text) this);
    }
}
