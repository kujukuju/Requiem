package com.requiem.interfaces;

/**
 * Created by Trent on 2/2/2015.
 */
public interface Moveable {
    public float getMass();
    public void setMass(float mass);

    public float getMaxSpeed();
    public void setMaxSpeed(float maxSpeed);

    public float getAccel();
    public void setAccel(float accel);

    public float getFriction();
    public void setFriction(float friction);
}
