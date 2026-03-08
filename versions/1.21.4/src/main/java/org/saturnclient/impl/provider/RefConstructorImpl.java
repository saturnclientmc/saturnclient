package org.saturnclient.impl.provider;

import org.saturnclient.common.provider.RefConstructorProvider;
import org.saturnclient.common.ref.asset.IdentifierRef;

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
}
