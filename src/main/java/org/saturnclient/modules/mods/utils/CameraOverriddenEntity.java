package org.saturnclient.modules.mods.utils;

public interface CameraOverriddenEntity {
	float freelook$getCameraPitch();
	float freelook$getCameraYaw();

	void freelook$setCameraPitch(float pitch);
	void freelook$setCameraYaw(float yaw);
}