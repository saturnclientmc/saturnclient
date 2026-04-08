package org.saturnclient.impl.provider;

import org.saturnclient.common.provider.RefConstructorProvider;
import org.saturnclient.common.ref.asset.IdentifierRef;
import org.saturnclient.common.ref.render.TextRef;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RefConstructorImpl implements RefConstructorProvider {
    @Override
    public IdentifierRef identifier(String fullPath) {
        return (IdentifierRef) (Object) Identifier.of(fullPath);
    }

    @Override
    public IdentifierRef identifier(String namespace, String path) {
        return (IdentifierRef) (Object) Identifier.of(namespace, path);
    }

    @Override
    public TextRef text(String text) {
        return (TextRef) (Text) Text.literal(text);
    }
}
