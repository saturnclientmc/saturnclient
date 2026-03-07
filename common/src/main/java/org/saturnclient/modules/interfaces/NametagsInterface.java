package org.saturnclient.modules.interfaces;

public interface NametagsInterface {

    public static interface EntityState {

        String getCustomName();

        float getHealth();

        float getMaxHealth();

        EntityType getEntityType();
    }

    public static enum EntityType {
        PLAYER,
        PASSIVE,
        HOSTILE,
        OTHER
    }

}