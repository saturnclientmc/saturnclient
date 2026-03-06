package org.saturnclient.common.minecraft;

public class MinecraftBinding {
    public Object inner;

    public MinecraftBinding(Object binding) {
        this.inner = binding;
    }

    @SuppressWarnings("unchecked")
    public <T> T get() {
        try {
            return (T) inner;
        } catch (ClassCastException e) {
            return null;
        }
    }
}
