package org.saturnclient.impl;

import net.minecraft.util.Identifier;

public class Mincraftify {
    public static Identifier identifier(org.saturnclient.common.Identifier val) {
        return Identifier.of(val.namespace(), val.path());
    }
}
