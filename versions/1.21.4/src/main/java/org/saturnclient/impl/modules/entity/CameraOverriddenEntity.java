package org.saturnclient.impl.modules.entity;

public interface CameraOverriddenEntity {
    float freelook$getCameraPitch();

    float freelook$getCameraYaw();

    void freelook$setCameraPitch(float pitch);

    void freelook$setCameraYaw(float yaw);
}