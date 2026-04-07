package org.saturnclient.impl.features.entity;

public interface CameraOverriddenEntity {
    float freelook$getCameraPitch();

    float freelook$getCameraYaw();

    void freelook$setCameraPitch(float pitch);

    void freelook$setCameraYaw(float yaw);
}