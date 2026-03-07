package org.saturnclient.modules.interfaces;

public interface SpeedometerInterface {

    /** Returns the current player or vehicle velocity in blocks per tick */
    Velocity getVelocity();

    /** Returns true if player or vehicle is on ground */
    boolean isOnGround();

    class Velocity {
        public final double x, y, z;

        public Velocity(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}