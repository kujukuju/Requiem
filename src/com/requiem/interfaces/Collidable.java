package com.requiem.interfaces;

import com.bulletphysics.dynamics.DynamicsWorld;

/**
 * Created by Trent on 11/29/2014.
 */
public interface Collidable {
    public void createRigidBody();
    public void addToDynamicsWorld(DynamicsWorld dynamicsWorld);
}
