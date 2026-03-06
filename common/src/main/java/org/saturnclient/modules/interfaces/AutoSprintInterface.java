package org.saturnclient.modules.interfaces;

public interface AutoSprintInterface {

    boolean hasPlayer();

    boolean hasNetwork();

    boolean isForwardPressed();

    boolean isBackPressed();

    boolean isSneaking();

    boolean hasHorizontalCollision();

    boolean isUsingItem();

    void setSprinting(boolean sprinting);
}