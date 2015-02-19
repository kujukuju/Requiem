package com.requiem.interfaces;

import com.requiem.abstractentities.entities.Entity;

/**
 * Created by Trent on 2/2/2015.
 */
//a moveable is an entity that can move in the dynamics world
public interface Moveable extends Entity, Updateable {
    public float getMass();
    public void setMass(float mass);

    public float getMaxSpeed();
    public void setMaxSpeed(float maxSpeed);

    public float getAccel();
    public void setAccel(float accel);

    public float getFriction();
    public void setFriction(float friction);

    public float getMaxSteepness();
    public void setMaxSteepness(float maxSteepness);

    public float getMaxJumpHeight();
    public void setMaxJumpHeight(float maxJumpHeight);
}
